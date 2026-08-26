package com.cleanroommc.modularui.screen;

import com.cleanroommc.modularui.ModularUI;
import com.cleanroommc.modularui.animation.Animator;
import com.cleanroommc.modularui.api.IPanelHandler;
import com.cleanroommc.modularui.api.ITheme;
import com.cleanroommc.modularui.api.IThemeApi;
import com.cleanroommc.modularui.api.MCHelper;
import com.cleanroommc.modularui.api.UpOrDown;
import com.cleanroommc.modularui.api.layout.IViewport;
import com.cleanroommc.modularui.api.layout.IViewportStack;
import com.cleanroommc.modularui.api.value.ISyncOrValue;
import com.cleanroommc.modularui.api.widget.IDragResizeable;
import com.cleanroommc.modularui.api.widget.IFocusedWidget;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.api.widget.ModernInteractable;
import com.cleanroommc.modularui.api.widget.ResizeDragArea;
import com.cleanroommc.modularui.integration.nei.NEIUtil;
import com.cleanroommc.modularui.integration.recipeviewer.RecipeViewerGhostIngredientSlot;
import com.cleanroommc.modularui.screen.viewport.GuiViewportStack;
import com.cleanroommc.modularui.screen.viewport.LocatedWidget;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.utils.HoveredWidgetList;
import com.cleanroommc.modularui.utils.Interpolation;
import com.cleanroommc.modularui.utils.Interpolations;
import com.cleanroommc.modularui.utils.ObjectList;
import com.cleanroommc.modularui.value.sync.PanelSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.ParentWidget;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widget.sizer.Area;
import com.cleanroommc.modularui.widgets.SlotGroupWidget;
import com.cleanroommc.neverenoughanimations.NEAConfig;

import cpw.mods.fml.common.Optional;

import me.eigenraven.lwjgl3ify.api.InputEvents;

import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * This class is like a window in windows. It can hold any amount of widgets. It may also be draggable.
 * To open another panel on top of the main panel you must use {@link IPanelHandler#simple(ModularPanel, SecondaryPanel.IPanelBuilder, boolean)}
 * or {@link PanelSyncManager#panel(String, PanelSyncHandler.IPanelBuilder, boolean)} if the panel should be synced.
 */
public class ModularPanel extends ParentWidget<ModularPanel> implements IViewport, IDragResizeable {

    public static ModularPanel defaultPanel(@NotNull String name) {
        return defaultPanel(name, 176, 166);
    }

    public static ModularPanel defaultPanel(@NotNull String name, int width, int height) {
        return new ModularPanel(name).size(width, height);
    }

    private static final int tapTime = 200;

    @NotNull
    private final String name;
    private ModularScreen screen;
    private IPanelHandler panelHandler;
    private State state = State.CLOSED;
    private boolean cantCloseNow = false;
    private final ObjectList<LocatedWidget> hovering = ObjectList.create();
    private final Input keyboard = new Input();
    private final Input mouse = new Input();

    // drag resizing
    private IDragResizeable currentResizing = null;
    private LocatedWidget currentResizingWidget = null;
    private ResizeDragArea draggingDragArea = null;
    private final Area startArea = new Area();
    private int dragX, dragY;

    private final List<IPanelHandler> clientSubPanels = new ArrayList<>();
    private boolean invisible = false;
    private Animator animator;

    private boolean resizable = false;
    private String themeOverride;
    private ITheme theme;

    private Runnable onCloseAction;

    public ModularPanel(@NotNull String name) {
        this.name = Objects.requireNonNull(name, "A panels name must not be null and should be unique!");
        center();
    }

    @Override
    public @NotNull ModularPanel getPanel() {
        return this;
    }

    @Override
    public Area getParentArea() {
        return getScreen().getScreenArea();
    }

    @Override
    public void onInit() {
        getScreen().registerFrameUpdateListener(this, this::findHoveredWidgets, false);
    }

    @Override
    public boolean isValidSyncOrValue(@NotNull ISyncOrValue syncOrValue) {
        return syncOrValue.isTypeOrEmpty(IPanelHandler.class);
    }

    @Override
    protected void setSyncOrValue(@NotNull ISyncOrValue syncOrValue) {
        super.setSyncOrValue(syncOrValue);
        setPanelHandler(syncOrValue.castNullable(IPanelHandler.class));
    }

    @ApiStatus.Internal
    public void setPanelSyncHandler(PanelSyncHandler syncHandler) {
        setSyncOrValue(ISyncOrValue.orEmpty(syncHandler));
    }

    /**
     * @return true if this panel is currently open on a screen
     */
    public boolean isOpen() {
        return this.state.open;
    }

    /**
     * If this panel is open it will be closed.
     * If animating is enabled and an animation is already playing this method will do nothing.
     */
    public void closeIfOpen() {
        if (!isOpen()) return;
        if (this.cantCloseNow) {
            if (!this.state.disposing) {
                this.state = State.WAIT_CLOSING;
            } else {
                this.state = State.WAIT_CLOSING_AND_DISPOSING;
            }
            return;
        }
        closeSubPanels();
        if (isMainPanel()) {
            // close screen and let NEA handle animation
            MCHelper.popScreen(getScreen().isOpenParentOnClose(), getContext().getParentScreen());
            return;
        }
        if (!shouldAnimate()) {
            this.screen.getPanelManager().closePanel(this);
            return;
        }
        if (!isOpening() && !isClosing()) {
            if (isMainPanel()) {
                // if this is the main panel, start closing animation for all panels
                for (ModularPanel panel : getScreen().getPanelManager().getOpenPanels()) {
                    if (!panel.isMainPanel()) {
                        panel.closeIfOpen();
                    }
                }
            }
            getAnimator().onFinish(() -> this.screen.getPanelManager().closePanel(this));
            getAnimator().reset(true);
            getAnimator().animate(true);
        }
    }

    protected void closeSubPanels() {
        if (this.panelHandler != null) {
            this.panelHandler.closeSubPanels();
        }
    }

    @ApiStatus.ScheduledForRemoval(inVersion = "3.2.0")
    @Deprecated
    public void animateClose() {
        closeIfOpen();
    }

    void setPanelHandler(IPanelHandler panelHandler) {
        this.panelHandler = panelHandler;
    }

    @Override
    public boolean hasParent() {
        return false;
    }

    @Override
    public WidgetThemeEntry<?> getWidgetThemeInternal(ITheme theme) {
        return theme.getPanelTheme();
    }

    @Override
    public void transform(IViewportStack stack) {
        super.transform(stack);
        // apply scaling for animation
        float scale = getScale();
        if (scale != 1f) {
            float x = getArea().w() / 2f;
            float y = getArea().h() / 2f;
            stack.translate(x, y);
            stack.scale(scale, scale);
            stack.translate(-x, -y);
        }
    }

    private void findHoveredWidgets() {
        this.hovering.clear();
        if (!isEnabled()) {
            return;
        }
        HoveredWidgetList widgetList = new HoveredWidgetList(this.hovering);
        getContext().reset();
        GuiViewportStack stack = new GuiViewportStack();
        stack.pushViewport(null, getScreen().getScreenArea());
        stack.pushViewport(this, getArea());
        transform(stack);
        getSelfAt(stack, widgetList, getContext().getAbsMouseX(), getContext().getAbsMouseY());
        transformChildren(stack);
        getWidgetsAt(stack, widgetList, getContext().getAbsMouseX(), getContext().getAbsMouseY());
        stack.popViewport(this);
        stack.popViewport(null);
        stack.reset();
    }

    @Override
    public boolean canHover() {
        return !this.invisible && super.canHover();
    }

    @MustBeInvokedByOverriders
    public void onOpen(ModularScreen screen) {
        this.screen = screen;
        getArea().z(1);
        resizer().initialize(this.screen.getResizeNode(), this.screen.getResizeNode());
        initialise(this, false);
        // call first tick after everything is initialised
        WidgetTree.onUpdate(this);
        if (!isMainPanel() && shouldAnimate()) {
            getAnimator().onFinish(() -> {});
            getAnimator().reset();
            getAnimator().animate();
        }
        this.state = State.OPEN;
    }

    void reopen() {
        this.state = State.OPEN;
    }

    @MustBeInvokedByOverriders
    public void onClose() {
        if (this.onCloseAction != null) {
            this.onCloseAction.run();
        }
        this.state = State.CLOSED;
        if (this.panelHandler != null) {
            this.panelHandler.closePanelInternal();
        }
    }

    @MustBeInvokedByOverriders
    @Override
    public void dispose() {
        if (this.state.disposing) return;
        if (this.state.open && !this.state.closing) {
            throw new IllegalStateException("Panel must be closed before disposing!");
        }
        if (this.cantCloseNow) {
            if (this.state.closing) {
                this.state = State.WAIT_CLOSING_AND_DISPOSING;
            } else {
                this.state = State.WAIT_DISPOSING;
            }
            return;
        }
        super.dispose();
        this.screen = null;
        this.state = State.DISPOSED;
    }

    /**
     * Wraps a function so it can be called safely. This is needed in methods where the panel can be closed and disposed, but doing
     * so will result in unexpected errors. This wrapper stops the disposal until the function has been fully executed.
     * The return value of the function is then returned.
     *
     * @param runnable function to be called safely
     * @param <T>      return type
     * @return return value of function
     */
    public final <T> T doSafe(Supplier<T> runnable) {
        if (this.state == State.DISPOSED) return null;
        // make sure the screen is also not disposed
        return getScreen().getPanelManager().doSafe(() -> {
            this.cantCloseNow = true;
            T t = runnable.get();
            this.cantCloseNow = false;
            if (this.state.open) {
                if (this.state.closing) {
                    this.state = State.OPEN;
                    closeIfOpen();
                }
                if (this.state.disposing) {
                    this.state = State.CLOSED;
                    dispose();
                }
            }
            return t;
        });
    }

    public final boolean doSafeBool(BooleanSupplier runnable) {
        return Objects.requireNonNull(doSafe(runnable::getAsBoolean));
    }

    public final int doSafeInt(IntSupplier runnable) {
        return Objects.requireNonNull(doSafe(runnable::getAsInt));
    }

    public boolean onMousePressed(int mouseButton) {
        return doSafeBool(() -> {
            LocatedWidget pressed = LocatedWidget.EMPTY;
            boolean result = false;

            if (this.hovering.isEmpty()) {
                // no element is hovered -> try close panel
                if (closeOnOutOfBoundsClick()) {
                    closeIfOpen();
                    result = true;
                }
            } else if (checkRecipeViewerGhostIngredient(mouseButton)) {
                return true;
            } else {
                for (LocatedWidget widget : this.hovering) {
                    if (widget.getElement() == null || !widget.getElement().isValid()) continue;
                    widget.applyMatrix(getContext());
                    IWidget w = widget.getElement();
                    if (w instanceof IDragResizeable resizeable && widget.getAdditionalHoverInfo() instanceof ResizeDragArea dragArea) {
                        this.currentResizing = resizeable;
                        this.currentResizingWidget = widget;
                        this.dragX = getContext().getMouseX();
                        this.dragY = getContext().getMouseY();
                        this.startArea.set(w.getArea());
                        this.startArea.rx = w.getArea().rx;
                        this.startArea.ry = w.getArea().ry;
                        this.draggingDragArea = dragArea;
                        widget.unapplyMatrix(getContext());
                        break;
                    }
                    // click widget and see how it reacts
                    if (widget.getElement() instanceof Interactable interactable) {
                        Interactable.Result interactResult = interactable.onMousePressed(mouseButton);
                        if (interactResult.accepts) {
                            this.mouse.addAcceptedInteractable(interactable);
                            pressed = widget;
                        } else if (interactResult.stops) {
                            pressed = LocatedWidget.EMPTY;
                        }
                        if (interactResult.stops) {
                            result = true;
                            widget.unapplyMatrix(getContext());
                            break;
                        }
                    }
                    // see if widget can be dragged
                    if (getContext().onHoveredClick(mouseButton, widget)) {
                        pressed = LocatedWidget.EMPTY;
                        result = true;
                        widget.unapplyMatrix(getContext());
                        break;
                    }
                    widget.unapplyMatrix(getContext());
                    // see if widgets below this can be interacted with
                    if (!widget.getElement().canClickThrough()) {
                        // act as if the widget was clicked and accepted
                        result = true;
                        pressed = widget;
                        break;
                    }
                }
            }

            if (result && pressed.getElement() instanceof IFocusedWidget) {
                getContext().focus(pressed);
            } else {
                getContext().removeFocus();
            }
            this.mouse.pressed(pressed, mouseButton);
            return result;
        });
    }

    /**
     * Called on mouse pressed. Similar logic on 1.7.10 and 1.12.2
     */
    private boolean checkRecipeViewerGhostIngredient(int mouseButton) {
        // NEI injects GuiContainerManager#mouseClicked at the start of GuiContainer#mouseClicked,
        // so at this point NEI has not handled drag-and-drop yet.
        // Also, we cannot rely on INEIGuiHandler, as root panel will always return true for interaction,
        // and click never gets propagated to NEI.
        if (ModularUI.Mods.NEI.isLoaded()) {
            ItemStack dndTarget = NEIUtil.getNEIDragAndDropTarget(getContext());
            if (dndTarget != null) {
                for (LocatedWidget widget : this.hovering) {
                    if (widget.getElement() instanceof RecipeViewerGhostIngredientSlot<?> dndHandler) {
                        if (dndHandler.handleDragAndDrop(dndTarget, mouseButton)) {
                            NEIUtil.stopNEIGhostDrag();
                            // SUCCESS, except that onMouseTapped should not be fired
                            this.mouse.pressed(widget, mouseButton);
                            this.mouse.doRelease = false;
                            getContext().removeFocus();
                            return true;
                        }
                        // we can't really predict if the interactable would stop further interaction
                        // so we assume worst
                        if (widget.getElement() instanceof Interactable || !widget.getElement().canClickThrough()) {
                            break;
                        }
                    }
                }
                // no target found -> tell nei to drop the ghost ingredient
                // stop all further interaction since dropping the ingredient counts as an interaction
                NEIUtil.stopNEIGhostDrag();
                this.mouse.pressed(LocatedWidget.EMPTY, mouseButton);
                this.mouse.doRelease = false;
                getContext().removeFocus();
                return true;
            }
        }
        return false;
    }

    public boolean onMouseRelease(int mouseButton) {
        return isEnabled() && doSafeBool(() -> {
            if (!this.mouse.doRelease) {
                this.mouse.reset();
                return false;
            }
            if (this.currentResizing != null) {
                this.mouse.reset();
                this.currentResizing = null;
                this.currentResizingWidget = null;
                return true;
            }
            if (interactFocused(widget -> widget.onMouseRelease(mouseButton), false)) {
                return true;
            }
            boolean lastPressedIsHovered = false;
            boolean tryTap = this.mouse.tryTap(mouseButton);
            // first see if the clicked widget is still hovered and try to interact with it
            for (LocatedWidget widget : this.hovering) {
                if (widget.getElement() == null || !widget.getElement().isValid()) continue;
                if (this.mouse.isWidget(widget)) {
                    if (widget.getElement() instanceof Interactable interactable &&
                            onMouseRelease(mouseButton, tryTap, widget, interactable)) {
                        return true;
                    }
                    lastPressedIsHovered = true;
                    break;
                }
            }
            // now try all other hovered
            for (LocatedWidget widget : this.hovering) {
                if (widget.getElement() == null || !widget.getElement().isValid()) continue;
                if (!this.mouse.isWidget(widget) && widget.getElement() instanceof Interactable interactable && onMouseRelease(mouseButton, tryTap, widget, interactable)) {
                    return true;
                }
            }
            // nothing worked, but since the pressed widget is still hovered we assume success
            // otherwise recipe viewer tries to pull some weird shit
            if (lastPressedIsHovered) {
                this.mouse.reset();
                return true;
            }
            this.mouse.reset();
            return false;
        });
    }

    private boolean onMouseRelease(int mouseButton, boolean tryTap, LocatedWidget widget, Interactable interactable) {
        boolean stop = false;
        widget.applyMatrix(getContext());
        if (tryTap && this.mouse.acceptedInteractions.remove(interactable)) {
            Interactable.Result tabResult = interactable.onMouseTapped(mouseButton);
            if (tabResult.stops) {
                stop = true;
                // we will try to trigger onMouseRelease() even after tapping tells to stop
            }
        }
        if (interactable.onMouseRelease(mouseButton)) {
            stop = true;
        }
        widget.unapplyMatrix(getContext());
        if (stop) {
            this.mouse.reset();
            return true;
        }
        return false;
    }

    public boolean onKeyPressed(char typedChar, int keyCode) {
        return doSafeBool(() -> {
            switch (interactFocused(widget -> widget.onKeyPressed(typedChar, keyCode), Interactable.Result.IGNORE)) {
                case STOP:
                    this.keyboard.pressed(LocatedWidget.EMPTY, keyCode);
                    return true;
                case SUCCESS:
                    this.keyboard.pressed(getContext().getFocusedWidget(), keyCode);
                    return true;
            }
            LocatedWidget pressed = null;
            boolean result = false;
            for (LocatedWidget widget : this.hovering) {
                if (widget.getElement() == null || !widget.getElement().isValid()) continue;
                if (widget.getElement() instanceof Interactable interactable) {
                    widget.applyMatrix(getContext());
                    Interactable.Result interactResult = interactable.onKeyPressed(typedChar, keyCode);
                    if (interactResult.accepts) {
                        this.keyboard.addAcceptedInteractable(interactable);
                        pressed = widget;
                    } else if (interactResult.stops) {
                        pressed = null;
                    }
                    if (interactResult.stops) {
                        result = true;
                        widget.unapplyMatrix(getContext());
                        break;
                    }
                    widget.unapplyMatrix(getContext());
                }
                if (!widget.getElement().canClickThrough()) break;
            }
            this.keyboard.pressed(pressed, keyCode);
            return result;
        });
    }

    public boolean onKeyRelease(char typedChar, int keyCode) {
        return doSafeBool(() -> {
            if (!this.keyboard.doRelease) {
                this.keyboard.reset();
                return false;
            }
            if (interactFocused(widget -> widget.onKeyRelease(typedChar, keyCode), false)) {
                return true;
            }
            boolean lastPressedIsHovered = false;
            boolean tryTap = this.keyboard.tryTap(keyCode);
            // first see if the clicked widget is still hovered and try to interact with it
            for (LocatedWidget widget : this.hovering) {
                if (this.keyboard.isWidget(widget)) {
                    if (widget.getElement() instanceof Interactable interactable &&
                            onKeyRelease(typedChar, keyCode, tryTap, widget, interactable)) {
                        return true;
                    }
                    lastPressedIsHovered = true;
                    break;
                }
            }
            // now try all other hovered
            for (LocatedWidget widget : this.hovering) {
                if (!this.keyboard.isWidget(widget) && widget.getElement() instanceof Interactable interactable &&
                        onKeyRelease(typedChar, keyCode, tryTap, widget, interactable)) {
                    return true;
                }
            }
            // nothing worked, but since the pressed widget is still hovered we assume success
            // otherwise recipe viewer tries to pull some weird shit
            if (lastPressedIsHovered) {
                this.keyboard.reset();
                return true;
            }
            this.keyboard.reset();
            return false;
        });
    }

    private boolean onKeyRelease(char typedChar, int keyCode, boolean tryTap, LocatedWidget widget, Interactable interactable) {
        boolean stop = false;
        widget.applyMatrix(getContext());
        if (tryTap && this.mouse.acceptedInteractions.remove(interactable)) {
            Interactable.Result tabResult = interactable.onKeyTapped(typedChar, keyCode);
            if (tabResult.stops) {
                stop = true;
                // we will try to trigger onMouseRelease() even after tapping tells to stop
            }
        }
        if (interactable.onKeyRelease(typedChar, keyCode)) {
            stop = true;
        }
        widget.unapplyMatrix(getContext());
        if (stop) {
            this.mouse.reset();
            return true;
        }
        return false;
    }

    public boolean onMouseScroll(UpOrDown scrollDirection, int amount) {
        return doSafeBool(() -> {
            if (interactFocused(widget -> widget.onMouseScroll(scrollDirection, amount), false)) {
                return true;
            }
            if (this.hovering.isEmpty()) return false;
            for (LocatedWidget widget : this.hovering) {
                if (widget.getElement() instanceof Interactable interactable && widget.getElement().isValid()) {
                    widget.applyMatrix(getContext());
                    boolean result = interactable.onMouseScroll(scrollDirection, amount);
                    widget.unapplyMatrix(getContext());
                    if (result) return true;
                }
            }
            return false;
        });
    }

    public boolean onMouseDrag(int mouseButton, long timeSinceClick) {
        return doSafeBool(() -> {
            if (this.currentResizing != null) {
                this.currentResizingWidget.applyMatrix(getContext());
                int mx = getContext().getMouseX();
                int my = getContext().getMouseY();
                this.currentResizingWidget.unapplyMatrix(getContext());
                int dx = mx - this.dragX;
                int dy = my - this.dragY;
                if (dx != 0 || dy != 0) {
                    IDragResizeable.applyDrag(this.currentResizing, (IWidget) this.currentResizing, this.draggingDragArea, this.startArea, dx, dy);
                }
                return true;
            }
            if (this.mouse.held &&
                    mouseButton == this.mouse.lastButton &&
                    this.mouse.lastPressed != null &&
                    this.mouse.lastPressed.getElement() instanceof Interactable interactable &&
                    this.mouse.lastPressed.getElement().isValid()) {
                this.mouse.lastPressed.applyMatrix(getContext());
                interactable.onMouseDrag(mouseButton, timeSinceClick);
                this.mouse.lastPressed.unapplyMatrix(getContext());
                return true;
            }
            return false;
        });
    }

    /** Lwjgl3 key press event */
    @Optional.Method(modid = ModularUI.ModIds.LWJGL3IFY)
    public boolean onKeyEvent(InputEvents.KeyEvent event) {
        return doSafeBool(() -> {
            switch (modernInteractFocused(w -> w.onKeyEvent(event), Interactable.Result.IGNORE)) {
                case STOP:
                    this.keyboard.pressed(LocatedWidget.EMPTY, event.sdlKeyCode);
                    return true;
                case SUCCESS:
                    this.keyboard.pressed(getContext().getFocusedWidget(), event.sdlKeyCode);
                    return true;
            }
            LocatedWidget pressed = null;
            boolean result = false;
            for (LocatedWidget widget : this.hovering) {
                if (widget.getElement() == null || !widget.getElement().isValid()) continue;
                if (widget.getElement() instanceof ModernInteractable interactable) {
                    widget.applyMatrix(getContext());
                    Interactable.Result interactResult = interactable.onKeyEvent(event);
                    widget.unapplyMatrix(getContext());
                    if (interactResult.accepts) {
                        this.keyboard.addAcceptedInteractable((Interactable) interactable);
                        pressed = widget;
                    } else if (interactResult.stops) {
                        pressed = null;
                    }
                    if (interactResult.stops) {
                        result = true;
                        break;
                    }
                }
                if (!widget.getElement().canClickThrough()) break;
            }
            this.keyboard.pressed(pressed, event.sdlKeyCode);
            return result;
        });
    }

    /** Lwjgl3 text input event */
    @Optional.Method(modid = ModularUI.ModIds.LWJGL3IFY)
    public boolean onTextEvent(InputEvents.TextEvent event) {
        return doSafeBool(() -> {
            if (modernInteractFocused(w -> w.onTextInput(event), false)) {
                return true;
            }
            for (LocatedWidget widget : this.hovering) {
                if (widget.getElement() == null || !widget.getElement().isValid()) continue;
                if (widget.getElement() instanceof ModernInteractable interactable) {
                    widget.applyMatrix(getContext());
                    boolean res = interactable.onTextInput(event);
                    widget.unapplyMatrix(getContext());
                    if (res) {
                        return true;
                    }
                }
            }
            return false;
        });
    }

    @SuppressWarnings("unchecked")
    private <T, W extends IWidget & IFocusedWidget & Interactable> T interactFocused(Function<W, T> function, T defaultValue) {
        LocatedWidget focused = this.getContext().getFocusedWidget();
        T result = defaultValue;
        if (focused.getElement() instanceof Interactable interactable && focused.getElement().isValid()) {
            focused.applyMatrix(getContext());
            result = function.apply((W) interactable);
            focused.unapplyMatrix(getContext());
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    private <T, W extends IWidget & IFocusedWidget & ModernInteractable> T modernInteractFocused(Function<W, T> function, T defaultValue) {
        LocatedWidget focused = this.getContext().getFocusedWidget();
        T result = defaultValue;
        if (focused.getElement() instanceof ModernInteractable interactable && focused.getElement().isValid()) {
            focused.applyMatrix(getContext());
            result = function.apply((W) interactable);
            focused.unapplyMatrix(getContext());
        }
        return result;
    }

    /**
     * @return if this panel can be dragged. Never works on the main panel.
     */
    public boolean isDraggable() {
        return getScreen().getMainPanel() != this;
    }

    /**
     * @return if panels below this can still be interacted with.
     */
    public boolean disablePanelsBelow() {
        return false;
    }

    /**
     * @return if this panel should be closed if outside of this panel is clicked.
     */
    public boolean closeOnOutOfBoundsClick() {
        return false;
    }

    @Override
    public boolean isCurrentlyResizable() {
        return this.resizable;
    }

    @Override
    public boolean keepPosOnDragResize() {
        return !isDraggable();
    }

    @Override
    public @NotNull String getName() {
        return this.name;
    }

    @Override
    public ModularScreen getScreen() {
        if (!isValid()) {
            throw new IllegalStateException();
        }
        return this.screen;
    }

    @NotNull
    public ObjectList<LocatedWidget> getHovering() {
        return this.hovering;
    }

    @Nullable
    public IWidget getTopHovering() {
        LocatedWidget lw = getTopHoveringLocated(false);
        return lw == null ? null : lw.getElement();
    }

    @Nullable
    public LocatedWidget getTopHoveringLocated(boolean debug) {
        int i = 0;
        while (i < this.hovering.size()) {
            LocatedWidget widget = this.hovering.get(i);
            if (!widget.getElement().isValid()) {
                this.hovering.remove(i);
                continue;
            }
            if (debug || widget.getElement().canHover()) {
                return widget;
            }
            i++;
        }
        return null;
    }

    public @NotNull List<LocatedWidget> getAllHoveringList(boolean debug) {
        if (this.hovering.isEmpty()) return Collections.emptyList();
        return new ArrayList<>(this.hovering);
    }

    public boolean isBelowMouse(IWidget widget) {
        if (!widget.isValid() || widget.getPanel() != this) return false;
        for (LocatedWidget lw : this.hovering) {
            if (lw.getElement() == widget) return true;
        }
        return false;
    }

    public boolean isAnyHovered() {
        if (this.hovering.isEmpty()) return false;
        if (this.hovering.size() == 1 && this.hovering.get(0).getElement() instanceof ModularPanel panel) {
            return panel.canHover();
        }
        return true;
    }

    final void setPanelGuiContext(@NotNull ModularGuiContext context) {
        setContext(context);
    }

    public boolean isOpening() {
        return this.animator != null && this.animator.isAnimatingForward();
    }

    public boolean isClosing() {
        return this.animator != null && this.animator.isAnimatingReverse();
    }

    public float getScale() {
        if (!ModularUI.Mods.NEA.isLoaded() || NEAConfig.openingAnimationTime == 0) return 1f;
        return Interpolations.lerp(NEAConfig.openingStartScale, 1f, getAnimator().getValue());
    }

    public float getAlpha() {
        if (!ModularUI.Mods.NEA.isLoaded() || NEAConfig.openingAnimationTime == 0) return 1f;
        return getAnimator().getValue();
    }

    public final boolean isMainPanel() {
        return getScreen().getMainPanel() == this;
    }

    @NotNull
    protected Animator getAnimator() {
        if (this.animator == null) {
            this.animator = new Animator()
                    .bounds(0f, 1f)
                    .duration(NEAConfig.openingAnimationTime)
                    .curve(Interpolation.getForName(NEAConfig.openingAnimationCurve.getName()));
            this.animator.reset(true);
        }
        return this.animator;
    }

    /**
     * This method determines if the panel should be animated.
     * It is strongly discouraged to override this method. Users should have animations when they have NEA installed, and it's enabled in
     * the config. Only override if the animation looks seriously wrong with your specific panel.
     */
    @ApiStatus.Internal
    public boolean shouldAnimate() {
        if (this.invisible || !ModularUI.Mods.NEA.isLoaded() || NEAConfig.openingAnimationTime <= 0) {
            return false;
        }
        if (!isMainPanel() || !getScreen().isOpenParentOnClose()) return true;
        return getContext().getParentScreen() == null;
    }

    void registerSubPanel(IPanelHandler handler) {
        if (!this.clientSubPanels.contains(handler)) {
            this.clientSubPanels.add(handler);
        }
    }

    void closeClientSubPanels() {
        for (IPanelHandler handler : this.clientSubPanels) {
            if (handler.isSubPanel()) {
                handler.closePanel();
            }
        }
    }

    public ITheme getTheme() {
        if (this.theme == null) {
            this.theme = IThemeApi.get().getThemeForScreen(this, this.themeOverride);
        }
        return this.theme;
    }

    @Override
    public boolean isExcludeAreaInRecipeViewer() {
        return super.isExcludeAreaInRecipeViewer() || (!getScreen().isOverlay() && !this.invisible && !resizer().isFullSize());
    }

    public ModularPanel bindPlayerInventory() {
        return child(SlotGroupWidget.playerInventory(true));
    }

    public ModularPanel bindPlayerInventory(int bottom) {
        return child(SlotGroupWidget.playerInventory(bottom, true));
    }

    @Override
    public ModularPanel invisible() {
        this.invisible = true;
        return super.invisible();
    }

    public ModularPanel fullScreenInvisible() {
        return invisible().full();
    }

    public ModularPanel resizeableOnDrag(boolean resizeable) {
        this.resizable = resizeable;
        return this;
    }

    public ModularPanel onCloseAction(Runnable onCloseAction) {
        this.onCloseAction = onCloseAction;
        return this;
    }

    public ModularPanel themeOverride(String id) {
        this.themeOverride = id;
        this.theme = null;
        return this;
    }

    @Deprecated
    @Override
    public ModularPanel name(String name) {
        throw new IllegalStateException("Name for ModularPanels are final!");
    }

    public State getState() {
        return this.state;
    }

    public enum State {

        /**
         * State after the panel opened.
         */
        OPEN(true, false, false),
        /**
         * State after panel closed. Panel can still be reopened in this state.
         */
        CLOSED(false, true, false),
        /**
         * State after panel disposed. The panel is now lost and has to be rebuilt, when reopening it.
         */
        DISPOSED(false, true, true),
        /**
         * Panel is open and is waiting to be closed.
         */
        WAIT_CLOSING(true, true, false),
        /**
         * Panel is closed and is waiting to be disposed.
         */
        WAIT_DISPOSING(true, false, true),
        /**
         * Panel is open abd is waiting to close and then to be disposed.
         */
        WAIT_CLOSING_AND_DISPOSING(true, true, true);

        public final boolean open, closing, disposing;

        State(boolean open, boolean closing, boolean disposing) {
            this.open = open;
            this.closing = closing;
            this.disposing = disposing;
        }
    }

    /**
     * A helper class to handle input states for mouse and keyboard separatly
     */
    private static class Input {

        private final ObjectList<Interactable> acceptedInteractions = ObjectList.create();
        @Nullable
        private LocatedWidget lastPressed;
        private boolean held;
        private long time;
        private int lastButton;
        private boolean doRelease = true;

        private Input() {
            reset();
        }

        private void addAcceptedInteractable(Interactable interactable) {
            if (!this.held) {
                this.acceptedInteractions.add(interactable);
            }
        }

        private void reset() {
            this.acceptedInteractions.clear();
            this.held = false;
            this.time = -1;
            this.lastButton = -1;
            this.doRelease = true;
        }

        private boolean isValid() {
            return this.lastPressed != null && this.time > 0;
        }

        private int getTimeSinceEvent() {
            return (int) Math.min(Minecraft.getSystemTime() - this.time, Integer.MAX_VALUE);
        }

        private boolean tryTap(int button) {
            return this.lastButton == button && getTimeSinceEvent() <= tapTime;
        }

        private boolean isWidget(IWidget widget) {
            return this.lastPressed != null && this.lastPressed.getElement() == widget;
        }

        private boolean isWidget(LocatedWidget widget) {
            return isWidget(widget.getElement());
        }

        private void pressed(LocatedWidget pressed, int button) {
            if (!this.held) {
                this.lastPressed = pressed;
                if (this.lastPressed != null) {
                    this.time = Minecraft.getSystemTime();
                }
                this.lastButton = button;
                this.held = true;
            }
        }
    }
}
