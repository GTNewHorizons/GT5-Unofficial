package com.cleanroommc.modularui.widget;

import com.cleanroommc.modularui.api.ITheme;
import com.cleanroommc.modularui.api.IThemeApi;
import com.cleanroommc.modularui.api.drawable.IDrawable;
import com.cleanroommc.modularui.api.layout.IViewportStack;
import com.cleanroommc.modularui.api.value.ISyncOrValue;
import com.cleanroommc.modularui.api.value.IValue;
import com.cleanroommc.modularui.api.widget.IDragResizeable;
import com.cleanroommc.modularui.api.widget.IGuiAction;
import com.cleanroommc.modularui.api.widget.IPositioned;
import com.cleanroommc.modularui.api.widget.ISynced;
import com.cleanroommc.modularui.api.widget.ITooltip;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.RichTooltip;
import com.cleanroommc.modularui.screen.viewport.ModularGuiContext;
import com.cleanroommc.modularui.theme.WidgetTheme;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.theme.WidgetThemeKey;
import com.cleanroommc.modularui.value.sync.ISyncRegistrar;
import com.cleanroommc.modularui.value.sync.ModularSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandler;
import com.cleanroommc.modularui.value.sync.ValueSyncHandler;
import com.cleanroommc.modularui.widget.sizer.StandardResizer;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.MustBeInvokedByOverriders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A very modular implementation of {@link IWidget}. This is the base class for almost all UI elements.
 * This class is perfectly fine for displaying drawables (although {@link com.cleanroommc.modularui.api.drawable.IDrawable.DrawableWidget DrawableWidget}
 * is preferred) or even nothing.
 * <p>
 * References to widgets should not be stored after the screen closed. While the screen is open its usually fine to remove and a widget
 * as many times as you want.
 *
 * @param <W> the type of this widget. This is used for proper return types in builder like methodsY
 */
public class Widget<W extends Widget<W>> extends AbstractWidget implements IPositioned<W>, ITooltip<W>, ISynced<W> {

    // other
    private boolean excludeAreaInRecipeViewer = false;
    // sizing
    private BiConsumer<W, IViewportStack> transform;
    // syncing
    @Nullable private IValue<?> value;
    @Nullable private String syncKey;
    @Nullable private SyncHandler<?> syncHandler;
    // rendering
    private boolean disableThemeBackground = false;
    private boolean disableHoverThemeBackground = false;
    @Nullable private IDrawable shadow = null;
    @Nullable private IDrawable background = null;
    @Nullable private IDrawable overlay = null;
    @Nullable private IDrawable hoverBackground = null;
    @Nullable private IDrawable hoverOverlay = null;
    @Nullable private RichTooltip tooltip;
    @Nullable private WidgetThemeKey<?> widgetThemeOverride = null;
    // listener
    @Nullable private List<IGuiAction> guiActionListeners; // TODO replace with proper event system
    @Nullable private Consumer<W> onUpdateListener;

    public Widget() {
        resizer(new StandardResizer(this));
    }

    // -----------------
    // === Lifecycle ===
    // -----------------

    @Override
    void onInitInternal(boolean late) {
        if (this.guiActionListeners != null) {
            for (IGuiAction action : this.guiActionListeners) {
                getContext().getScreen().registerGuiActionListener(action);
            }
        }

        if (this.value != null && this.syncKey != null) {
            throw new IllegalStateException("Widget has a value and a sync key for a synced value. This is not allowed!");
        }
        if (!getScreen().isClientOnly()) {
            initialiseSyncHandler(getScreen().getSyncManager(), late);
        }
        if (isExcludeAreaInRecipeViewer()) {
            getContext().getRecipeViewerSettings().addExclusionArea(this);
        }
    }

    /**
     * Retrieves, verifies, and initialises a linked sync handler.
     * Custom logic should be handled in {@link #setSyncOrValue(ISyncOrValue)}.
     */
    @Override
    public void initialiseSyncHandler(ModularSyncManager syncManager, boolean late) {
        SyncHandler<?> handler = this.syncHandler;
        if (handler == null && this.syncKey != null) {
            handler = syncManager.getSyncHandler(getPanel().getName(), this.syncKey);
            if (handler == null && !syncManager.getMainPSM().getPanelName().equals(getPanel().getName())) {
                handler = syncManager.getMainPSM().getSyncHandlerFromMapKey(this.syncKey);
            }
        }
        if (handler != null) setSyncOrValue(handler);
        if (this.syncHandler instanceof ValueSyncHandler<?, ?> valueSyncHandler && valueSyncHandler.getChangeListener() == null) {
            valueSyncHandler.setChangeListener(this::markTooltipDirty);
        }
    }

    /**
     * Called when this widget is removed from the widget tree or after the panel is closed.
     * Overriding this is fine, but super must be called.
     */
    @MustBeInvokedByOverriders
    @Override
    public void dispose() {
        if (isValid()) {
            if (this.guiActionListeners != null) {
                for (IGuiAction action : this.guiActionListeners) {
                    getScreen().removeGuiActionListener(action);
                }
            }
            if (isExcludeAreaInRecipeViewer()) {
                getContext().getRecipeViewerSettings().removeExclusionArea(this);
            }
        }
        super.dispose();
    }

    // -----------------
    // === Rendering ===
    // -----------------

    /**
     * Called directly before {@link IWidget#draw(ModularGuiContext, WidgetThemeEntry)}. Draws background textures.
     * It is highly recommended to at least replicate this behaviour when overriding.
     * Overriding {@link IWidget#draw(ModularGuiContext, WidgetThemeEntry)} for custom visuals is preferred.
     * If a parent of this widget is disabled, this widget will not be drawn.
     *
     * @param context     gui context
     * @param widgetTheme widget theme of this widget
     */
    @Override
    public void drawBackground(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {
        WidgetTheme theme = getActiveWidgetTheme(widgetTheme, isHovering());
        if (this.shadow != null) {
            this.shadow.drawAtZero(context, getArea(), theme);
        }
        if (!this.disableThemeBackground || !this.disableHoverThemeBackground) {
            IDrawable bg = getThemeBackground(widgetTheme, theme);
            if (bg != null) {
                bg.drawAtZero(context, getArea(), theme);
            }
        }
        IDrawable bg = getCurrentBackground(widgetTheme);
        if (bg != null) {
            bg.drawAtZero(context, getArea(), theme);
        }
    }

    /**
     * Called between {@link IWidget#drawBackground(ModularGuiContext, WidgetThemeEntry)} and {@link IWidget#drawOverlay(ModularGuiContext, WidgetThemeEntry)}.
     * Custom visuals should be drawn here. For example the {@link com.cleanroommc.modularui.widgets.slot.ItemSlot ItemSlot} draws its item
     * here. If a parent of this widget is disabled, this widget will not be drawn.
     *
     * @param context     gui context
     * @param widgetTheme widget theme
     */
    @Override
    public void draw(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {}

    /**
     * Called directly after {@link IWidget#draw(ModularGuiContext, WidgetThemeEntry)}. Draws overlay textures.
     * It is highly recommended to at least replicate this behaviour when overriding.
     * Overriding {@link IWidget#draw(ModularGuiContext, WidgetThemeEntry)} for custom visuals is preferred.
     * If a parent of this widget is disabled, this widget will not be drawn.
     *
     * @param context     gui context
     * @param widgetTheme widget theme
     */
    @Override
    public void drawOverlay(ModularGuiContext context, WidgetThemeEntry<?> widgetTheme) {
        IDrawable bg = getCurrentOverlay(widgetTheme);
        if (bg != null) {
            bg.drawAtZeroPadded(context, getArea(), getActiveWidgetTheme(widgetTheme, isHovering()));
        }
    }

    /**
     * Called after every widget of every panel and screen has been drawn. This is usually used to draw a tooltip, which is the default
     * behaviour. If a parent of this widget is disabled, this widget will not be drawn.
     *
     * @param context gui context
     */
    @Override
    public void drawForeground(ModularGuiContext context) {
        RichTooltip tooltip = getTooltip();
        if (tooltip != null && isHoveringFor(tooltip.getShowUpTimer()) && !context.hasDraggable()) {
            tooltip.draw(context);
        }
    }

    public boolean isDisableThemeBackground() {
        return disableThemeBackground;
    }

    public boolean isDisableHoverThemeBackground() {
        return disableHoverThemeBackground;
    }

    public @Nullable IDrawable getShadow() {
        return shadow;
    }

    /**
     * The current set background. This is not an accurate representation of what is actually being displayed currently.
     * Usually background is handled by the theme, which is when this is null.
     * Backgrounds are drawn in {@link IWidget#drawBackground(ModularGuiContext, WidgetThemeEntry)}.
     *
     * @return background of this widget
     */
    public @Nullable IDrawable getBackground() {
        return this.background;
    }

    /**
     * The current set overlay. This is used when the widget is not hovered or no hovered overlay is set.
     * Overlays are drawn in {@link IWidget#drawOverlay(ModularGuiContext, WidgetThemeEntry)}.
     *
     * @return overlay of this widget
     */
    public @Nullable IDrawable getOverlay() {
        return this.overlay;
    }

    /**
     * The current set hover background. Usually this is handled by the theme.
     *
     * @return hover background of this widget
     */
    public @Nullable IDrawable getHoverBackground() {
        return this.hoverBackground;
    }

    /**
     * The current set hover overlay.
     *
     * @return hover background of this widget
     */
    public @Nullable IDrawable getHoverOverlay() {
        return this.hoverOverlay;
    }

    public @Nullable IDrawable getThemeBackground(WidgetThemeEntry<?> widgetTheme) {
        return getThemeBackground(widgetTheme, getActiveWidgetTheme(widgetTheme, isHovering()));
    }

    public @Nullable IDrawable getThemeBackground(WidgetThemeEntry<?> widgetTheme, WidgetTheme activeTheme) {
        IDrawable bg = null;
        if (isHovering()) {
            bg = activeTheme.getBackground();
            if (bg == null || bg == IDrawable.NONE || this.disableHoverThemeBackground) {
                bg = getActiveWidgetTheme(widgetTheme, false).getBackground();
            }
        } else if (!this.disableThemeBackground) {
            bg = activeTheme.getBackground();
        }
        return bg;
    }

    /**
     * Returns the actual currently displayed background.
     *
     * @param widgetTheme widget theme which is used by this widget
     * @return currently displayed background
     */
    public @Nullable IDrawable getCurrentBackground(WidgetThemeEntry<?> widgetTheme) {
        if (isHovering()) {
            IDrawable hoverBackground = getHoverBackground();
            if (hoverBackground != null && hoverBackground != IDrawable.NONE) return hoverBackground;
        }
        return getBackground();
    }

    /**
     * Returns the actual currently displayed overlay.
     *
     * @param widgetTheme widget theme which is used by this widget
     * @return currently displayed background
     */
    public @Nullable IDrawable getCurrentOverlay(WidgetThemeEntry<?> widgetTheme) {
        if (isHovering()) {
            IDrawable hoverBackground = getHoverOverlay();
            if (hoverBackground != null && hoverBackground != IDrawable.NONE) return hoverBackground;
        }
        return getOverlay();
    }

    /**
     * @return the tooltip object of this widget, might have not been created yet
     */
    @Nullable
    @Override
    public RichTooltip getTooltip() {
        return this.tooltip;
    }

    /**
     * @return the tooltip object of this widget and creates a new one if there is currently none.
     */
    @Override
    public @NotNull RichTooltip tooltip() {
        if (this.tooltip == null) {
            this.tooltip = new RichTooltip().parent(this);
        }
        return this.tooltip;
    }

    /**
     * Sets a tooltip object.
     *
     * @param tooltip new tooltip
     * @return this
     */
    @Override
    public W tooltip(RichTooltip tooltip) {
        this.tooltip = tooltip;
        return getThis();
    }

    /**
     * Should be called when information which is displayed in the tooltip via {@link ITooltip#tooltipDynamic(Consumer)}.
     * It will invalidate the current tooltip and be caused to rebuild.
     */
    public void markTooltipDirty() {
        if (this.tooltip != null) {
            this.tooltip.markDirty();
        }
    }

    /**
     * Returns the widget theme this widget class would like to use. Overriding is fine.
     *
     * @param theme theme to get widget theme from
     * @return widget theme this widget wishes to use
     */
    @ApiStatus.OverrideOnly
    protected WidgetThemeEntry<?> getWidgetThemeInternal(ITheme theme) {
        return theme.getFallback();
    }

    @ApiStatus.OverrideOnly
    protected WidgetTheme getActiveWidgetTheme(WidgetThemeEntry<?> widgetTheme, boolean hover) {
        return widgetTheme.getTheme(hover);
    }

    @Override
    public final WidgetThemeEntry<?> getWidgetTheme(ITheme theme) {
        if (this.widgetThemeOverride != null) {
            return theme.getWidgetTheme(this.widgetThemeOverride);
        }
        return getWidgetThemeInternal(theme);
    }

    public final @Nullable WidgetThemeKey<?> getWidgetThemeOverride() {
        return widgetThemeOverride;
    }

    /**
     * Returns the actual used widget theme. Uses {@link #widgetTheme(String)} if it has been set, otherwise calls
     * {@link #getWidgetThemeInternal(ITheme)}
     *
     * @param theme        theme to get widget theme from
     * @param expectedType type of the widget theme to expect used for validation
     * @return widget theme this widget will use
     * @throws IllegalStateException if the received widget theme type doesn't match the expected type
     */
    @SuppressWarnings("unchecked")
    public final <T extends WidgetTheme> WidgetThemeEntry<T> getWidgetTheme(ITheme theme, Class<T> expectedType) {
        WidgetThemeEntry<?> entry = getWidgetTheme(theme);
        if (entry.getKey().isOfType(expectedType)) {
            return (WidgetThemeEntry<T>) entry;
        }
        throw new IllegalStateException(
                String.format("Got widget theme with invalid type in widget '%s'. Got type '%s', but expected type '%s'!",
                        this, entry.getKey().getWidgetThemeType().getSimpleName(), expectedType.getSimpleName()));
    }

    /**
     * Sets a shadow. Shadows are drawn before the background and don't actually have to be shadows. This doesn't effect theme backgrounds
     * and doesn't change when hovered.
     *
     * @param shadow background to use.
     * @return this
     */
    public W shadow(IDrawable... shadow) {
        this.shadow = IDrawable.of(shadow);
        return getThis();
    }

    /**
     * Sets a background. The theme background will be drawn before this override.
     * <p>
     * NOTE: This will NOT disable the theme background. Disable it separately with {@link #disableThemeBackground(boolean)}.
     * </p>
     * This method is meant for unique textures. For generic backgrounds please use themes. Also consider using
     * {@link #overlay(IDrawable...)} instead. Using {@link IDrawable#EMPTY} will make the background invisible while still overriding
     * the widget theme. Background are drawn before the widget and overlays are drawn.
     *
     * @param background background to use.
     * @return this
     */
    public W backgroundOverlay(IDrawable... background) {
        this.background = IDrawable.of(background);
        return getThis();
    }

    /**
     * Sets a background and disables the theme background. See {@link #backgroundOverlay(IDrawable...)} for more information.
     *
     * @param background background
     * @return this
     */
    public W background(IDrawable... background) {
        return backgroundOverlay(background).disableThemeBackground(true);
    }

    /**
     * Sets an overlay. Does not interfere with themes. Overlays are drawn after the widget and backgrounds.
     *
     * @param overlay overlay to use.
     * @return this
     */
    public W overlay(IDrawable... overlay) {
        this.overlay = IDrawable.of(overlay);
        return getThis();
    }

    /**
     * Sets a hover background. The hover theme background will be drawn before this override.
     * <p>
     * NOTE: This will NOT disable the hover theme background. Disable it separately with {@link #disableHoverThemeBackground(boolean)}.
     * </p>
     * <p>
     * This method is meant for unique textures. For generic backgrounds please use themes. Also consider using
     * {@link #hoverOverlay(IDrawable...)} instead. Using {@link IDrawable#EMPTY} will make the background invisible while still
     * overriding the widget theme. Background are drawn before the widget and overlays are drawn.
     * <p>
     * Following argument special cases should be considered:
     * <ul>
     *     <li>{@code null} will fallback to {@link WidgetThemeEntry#getHoverTheme()}</li>
     *     <li>{@link IDrawable#EMPTY} will make the hover background invisible</li>
     *     <li>{@link IDrawable#NONE} will use the normal background instead (which is also achieved using {@link #disableHoverBackground()})
     *     (note that this won't disable the hover theme background)</li>
     *     <li>multiple drawables, will result in them being drawn on top of each other in the order they are passed to the method</li>
     * </ul>
     *
     * @param background hover background to use.
     * @return this
     */
    public W hoverBackgroundOverlay(IDrawable... background) {
        this.hoverBackground = IDrawable.of(background);
        return getThis();
    }

    /**
     * Sets a background and disables the theme background. See {@link #backgroundOverlay(IDrawable...)} for more information.
     *
     * @param background background
     * @return this
     */
    public W hoverBackground(IDrawable... background) {
        return hoverBackgroundOverlay(background).disableHoverThemeBackground(true);
    }

    /**
     * Sets a hover overlay.
     * Using {@link IDrawable#EMPTY} will make the background invisible while still overriding the widget theme.
     * Background are drawn before the widget and overlays are drawn.
     * <p>
     * Following argument special cases should be considered:
     * <ul>
     *     <li>{@link IDrawable#EMPTY} will make the hover overlay invisible</li>
     *     <li>{@code null} and {@link IDrawable#NONE} will use the normal overlay instead (which is also achieved using {@link #disableHoverOverlay()})</li>
     *     <li>multiple drawables, will result in them being drawn on top of each other in the order they are passed to the method</li>
     * </ul>
     *
     * @param overlay hover overlay to use.
     * @return this
     */
    public W hoverOverlay(IDrawable... overlay) {
        this.hoverOverlay = IDrawable.of(overlay);
        return getThis();
    }

    public W disableThemeBackground(boolean b) {
        this.disableThemeBackground = b;
        return getThis();
    }

    public W disableHoverThemeBackground(boolean b) {
        this.disableHoverThemeBackground = b;
        return getThis();
    }

    /**
     * Forces the hover background to use the normal background instead. This also diables the hover theme background.
     *
     * @return this
     */
    public W disableHoverBackground() {
        return hoverBackgroundOverlay(IDrawable.NONE).disableHoverThemeBackground(true);
    }

    /**
     * Forces the hover overlay to use the normal overlay instead.
     *
     * @return this
     */
    public W disableHoverOverlay() {
        return hoverOverlay(IDrawable.NONE);
    }

    /**
     * Sets an override widget theme. This will change of the appearance of this widget according to the widget theme.
     *
     * @param s id of the widget theme (see constants in {@link IThemeApi})
     * @return this
     */
    public W widgetTheme(String s) {
        WidgetThemeKey<?> widgetThemeKey = WidgetThemeKey.getFromFullName(s);
        if (widgetThemeKey == null) {
            throw new IllegalArgumentException("No widget theme for id '" + s + "' exists.");
        }
        return widgetTheme(widgetThemeKey);
    }

    /**
     * Sets an override widget theme. This will change of the appearance of this widget according to the widget theme.
     *
     * @param s id of the widget theme (see constants in {@link IThemeApi})
     * @return this
     */
    public W widgetTheme(WidgetThemeKey<?> s) {
        this.widgetThemeOverride = s;
        return getThis();
    }

    public W invisible() {
        return disableThemeBackground(true)
                .disableHoverBackground();
    }

    // --------------
    // === Events ===
    // --------------

    /**
     * Called once every tick (20 times per second). Overriding is fine, but super should be called. This will be called even of the widget
     * is not enabled.
     * By default, this will invoke update listeners set via setters.
     */
    @MustBeInvokedByOverriders
    @Override
    public void onUpdate() {
        super.onUpdate();
        if (this.onUpdateListener != null) {
            this.onUpdateListener.accept(getThis());
        }
    }

    /**
     * @return the current tick listener which will be called every tick
     */
    @Nullable
    public Consumer<W> getOnUpdateListener() {
        return this.onUpdateListener;
    }

    /**
     * Registers a gui action this widget can listen to. Gui action listeners can listen to several mouse and keyboard input events.
     * The listeners are called first, before any widgets are interacted with. The listeners will always be called, even if the widget
     * is disabled or not hovered!
     * <p>
     * Lambdas must be cast to the appropriate functional interface.
     * These actions are automatically unregistered when the widget is removed from the widget tree.
     *
     * @param action gui action to register
     * @return this
     */
    public W listenGuiAction(IGuiAction action) {
        if (this.guiActionListeners == null) {
            this.guiActionListeners = new ArrayList<>();
        }
        this.guiActionListeners.add(action);
        if (isValid()) {
            getScreen().registerGuiActionListener(action);
        }
        return getThis();
    }

    /**
     * Sets an update listener which is called once every tick even when this widget is disabled.
     *
     * @param listener update listener
     * @return this
     */
    public W onUpdateListener(Consumer<W> listener) {
        return onUpdateListener(listener, false);
    }

    /**
     * Sets an update listener which is called once every tick even when this widget is disabled.
     * If a listener is already set and {@code merge} is true, the listeners will be merged, so that both will be called on tick.
     *
     * @param listener update listener
     * @return this
     */
    public W onUpdateListener(Consumer<W> listener, boolean merge) {
        if (merge && this.onUpdateListener != null) {
            final Consumer<W> oldListener = this.onUpdateListener;
            if (listener != null) {
                this.onUpdateListener = w -> {
                    oldListener.accept(w);
                    listener.accept(w);
                };
            }
        } else {
            this.onUpdateListener = listener;
        }
        return getThis();
    }

    /**
     * Sets a condition for when to enable/disable this widget. This register an update listener which checks the condition every tick.
     * Careful not to overwrite this when calling {@link #onUpdateListener(Consumer)} afterward!
     *
     * @param condition condition when to enable this widget
     * @return this
     */
    public W setEnabledIf(Predicate<W> condition) {
        return onUpdateListener(w -> setEnabled(condition.test(w)), true);
    }

    // ----------------
    // === Resizing ===
    // ----------------

    @Override
    public int getDefaultWidth() {
        return isValid() ? getWidgetTheme(getPanel().getTheme()).getTheme().getDefaultWidth() : 18;
    }

    @Override
    public int getDefaultHeight() {
        return isValid() ? getWidgetTheme(getPanel().getTheme()).getTheme().getDefaultHeight() : 18;
    }

    @Override
    public void transform(IViewportStack stack) {
        super.transform(stack);
        if (this.transform != null) {
            this.transform.accept(getThis(), stack);
        }
    }

    public W transform(BiConsumer<W, IViewportStack> transform) {
        this.transform = transform;
        return getThis();
    }

    // ---------------
    // === Syncing ===
    // --------------

    /**
     * Returns if this widget has a valid sync handler.
     *
     * @return true if this widget has a valid sync handler
     */
    @Override
    public boolean isSynced() {
        return this.syncHandler != null;
    }

    /**
     * Returns the sync handler of this widget.
     *
     * @return sync handler of this widget
     * @throws IllegalStateException if this widget has no sync handler ({@link #isSynced()} returns false)
     */
    @Override
    public @NotNull SyncHandler<?> getSyncHandler() {
        if (this.syncHandler == null) {
            throw new IllegalStateException("Widget is not initialised or not synced!");
        }
        return this.syncHandler;
    }

    /**
     * Returns the value handler of this widget. Value handlers can provide and update any kind of objects like numbers and strings.
     * For example text fields uses this get the current set string and updates the string after it is unfocused.
     *
     * @return the value handler of this widget
     */
    public @Nullable IValue<?> getValue() {
        return this.value;
    }

    /**
     * Sets a sync handler id. A sync handler with the same id must have been registered to the appropriate
     * {@link com.cleanroommc.modularui.value.sync.PanelSyncManager PanelSyncManager} for this to work.
     * This method is preferred over setting a sync handler directly since this does not require the widget to be defined on both sides.
     *
     * @param name sync handler key name
     * @param id   sync handler key id
     * @return this
     */
    @Override
    public W syncHandler(String name, int id) {
        this.syncKey = ISyncRegistrar.makeSyncKey(name, id);
        return getThis();
    }

    /**
     * Used for widgets to set a value handler. <br />
     * Will also call {@link #setSyncHandler(SyncHandler)} if it is a SyncHandler
     */
    @ApiStatus.ScheduledForRemoval(inVersion = "3.2.0")
    @Deprecated
    protected void setValue(IValue<?> value) {
        this.value = value;
        if (value instanceof SyncHandler<?> handler) {
            setSyncHandler(handler);
        }
    }

    /**
     * Used for widgets to set a sync handler.
     */
    @ApiStatus.ScheduledForRemoval(inVersion = "3.2.0")
    @Deprecated
    protected void setSyncHandler(@Nullable SyncHandler<?> syncHandler) {
        if (syncHandler != null) checkValidSyncOrValue(syncHandler);
        this.syncHandler = syncHandler;
    }

    @MustBeInvokedByOverriders
    protected void setSyncOrValue(@NotNull ISyncOrValue syncOrValue) {
        if (!syncOrValue.isSyncHandler() && !syncOrValue.isValueHandler()) return;
        checkValidSyncOrValue(syncOrValue);
        if (syncOrValue instanceof SyncHandler<?> syncHandler1) setSyncHandler(syncHandler1);
        if (syncOrValue instanceof IValue<?> value1) setValue(value1);
    }

    // -------------
    // === Other ===
    // -------------


    /**
     * Disables the widget from start. Useful inside widget tree creation, where widget references are usually not stored.
     *
     * @return this
     */
    public W disabled() {
        setEnabled(false);
        return getThis();
    }

    @Override
    public Object getAdditionalHoverInfo(IViewportStack viewportStack, int mouseX, int mouseY) {
        if (this instanceof IDragResizeable dragResizeable) {
            return IDragResizeable.getDragResizeCorner(dragResizeable, getArea(), viewportStack, mouseX, mouseY);
        }
        return null;
    }

    public boolean isExcludeAreaInRecipeViewer() {
        return this.excludeAreaInRecipeViewer;
    }

    public W excludeAreaInRecipeViewer() {
        return excludeAreaInRecipeViewer(true);
    }

    public W excludeAreaInRecipeViewer(boolean val) {
        this.excludeAreaInRecipeViewer = val;
        if (isValid()) {
            getContext().getRecipeViewerSettings().addExclusionArea(this);
        }
        return getThis();
    }

    /**
     * @deprecated this got renamed to name
     */
    @ApiStatus.ScheduledForRemoval(inVersion = "3.2.0")
    @Deprecated
    public W debugName(String name) {
        return name(name);
    }

    /**
     * This can be used to find the widget with various methods from {@link WidgetTree} from a parent. The name is also included in {@link #toString()}.
     *
     * @param name debug name to use
     * @return this
     */
    public W name(String name) {
        setName(name);
        return getThis();
    }

    /**
     * Returns this widget with proper generic type.
     *
     * @return this
     */
    @SuppressWarnings("unchecked")
    @Override
    public W getThis() {
        return (W) this;
    }
}
