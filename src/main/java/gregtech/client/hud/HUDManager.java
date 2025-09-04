package gregtech.client.hud;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.github.bsideup.jabel.Desugar;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.client.hud.elements.DroplistElement;
import gregtech.client.hud.elements.TextInputElement;

/**
 * Manages HUD widgets, handling rendering, mouse interactions, and edit mode.
 * Usage: Register with HUDManager.getInstance().register(), enable with setEnabled(true), add widgets with
 * addWidget(widget).
 */
public class HUDManager {

    private static final HUDManager INSTANCE = new HUDManager();

    public static HUDManager getInstance() {
        return INSTANCE;
    }

    // ---------------- State ----------------

    private final List<CompositeWidget> widgets = new ArrayList<>();
    private boolean enabled, isEditMode, wasShiftDown;
    private CompositeWidget activeWidget;
    private boolean isDragging;
    private double dragOffsetX, dragOffsetY;
    private Supplier<GuiScreen> editGuiSupplier;

    // ---------------- Registration and API ----------------

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public HUDManager addWidget(CompositeWidget widget) {
        widgets.add(widget);
        widget.updateLayouts();
        return this;
    }

    public void removeWidget(CompositeWidget widget) {
        widgets.remove(widget);
    }

    public void clearWidgets() {
        widgets.clear();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEditGuiSupplier(Supplier<GuiScreen> supplier) {
        this.editGuiSupplier = supplier;
    }

    public boolean isEditMode() {
        return isEditMode;
    }

    public List<CompositeWidget> getWidgets() {
        return widgets;
    }

    // ----- UI Constants -----

    public static final class UIConstants {

        // spotless:off
        // Colors
        public static final float BG_R = 0.2f, BG_G = 0.2f, BG_B = 0.2f, BG_ALPHA = 0.8f;
        public static final float HOVER_R = 0.4f, HOVER_G = 0.4f, HOVER_B = 0.8f;
        public static final float BUTTON_BG_R = BG_R, BUTTON_BG_G = BG_G, BUTTON_BG_B = BG_B;
        public static final float BUTTON_HOVER_R = HOVER_R, BUTTON_HOVER_G = HOVER_G, BUTTON_HOVER_B = HOVER_B;
        public static final float BUTTON_ALPHA = 0.8f;
        public static final float CHECKBOX_SHADE_NORMAL = 0.3f, CHECKBOX_SHADE_HOVER = 0.6f;
        public static final float CHECKED_R = 0f, CHECKED_G = 1f, CHECKED_B = 0f, CHECKED_ALPHA = 0.8f;
        public static final float DROPLIST_PANE_R = 0.1f, DROPLIST_PANE_G = 0.1f, DROPLIST_PANE_B = 0.1f, DROPLIST_PANE_ALPHA = 0.9f;
        public static final float DROPLIST_ITEM_HOVER_R = 0.3f, DROPLIST_ITEM_HOVER_G = 0.3f, DROPLIST_ITEM_HOVER_B = 0.6f;
        public static final float DROPLIST_ITEM_ALPHA = 0.8f;
        public static final float TILE_HOVER_R = 0.4f, TILE_HOVER_G = 0.4f, TILE_HOVER_B = 0.8f;
        public static final float TILE_BG_R = 0.2f, TILE_BG_G = 0.2f, TILE_BG_B = 0.2f, TILE_ALPHA = 0.8f;
        public static final float TILE_SELECTED_R = 0f, TILE_SELECTED_G = 1f, TILE_SELECTED_B = 0f, TILE_SELECTED_ALPHA = 0.4f;
        public static final float SLIDER_BG_R = BG_R, SLIDER_BG_G = BG_G, SLIDER_BG_B = BG_B;
        public static final float SLIDER_KNOB_R = 0.4f, SLIDER_KNOB_G = 0.6f, SLIDER_KNOB_B = 0.8f, SLIDER_KNOB_ALPHA = 0.9f;
        public static final float PROGRESS_FILL_R = 0.2f, PROGRESS_FILL_G = 0.8f, PROGRESS_FILL_B = 0.2f, PROGRESS_FILL_ALPHA = 0.9f;
        public static final float OVERLAY_BG_R = 0f, OVERLAY_BG_G = 0f, OVERLAY_BG_B = 0f, OVERLAY_BG_ALPHA = 0.5f;
        public static final float MODAL_BG_R = 0.2f, MODAL_BG_G = 0.2f, MODAL_BG_B = 0.2f, MODAL_BG_ALPHA = 0.9f;
        public static final float GRAPH_UP_R = 1f, GRAPH_UP_G = 0f, GRAPH_UP_B = 0f, GRAPH_UP_ALPHA = 1f;
        public static final float GRAPH_DOWN_R = 0f, GRAPH_DOWN_G = 1f, GRAPH_DOWN_B = 0f, GRAPH_DOWN_ALPHA = 1f;
        public static final float GRAPH_NEUTRAL_R = 0.5f, GRAPH_NEUTRAL_G = 0.5f, GRAPH_NEUTRAL_B = 0.5f, GRAPH_NEUTRAL_ALPHA = 1f;

        // Layout
        public static final int BUTTON_TEXT_OFFSET_X = 3, BUTTON_TEXT_OFFSET_Y = 2;
        public static final int CHECKBOX_TEXT_OFFSET_X = 3, CHECKBOX_TEXT_OFFSET_Y = 2, CHECKBOX_INNER_OFFSET = 2;
        public static final int DROPLIST_TEXT_OFFSET_X = 3, DROPLIST_TEXT_OFFSET_Y = 2;
        public static final int TILE_OUTLINE_WIDTH = 2;
        public static final int SLIDER_KNOB_HALF = 3;
        public static final int TEXT_COLOR = 0xFFFFFF;
        public static final long CURSOR_BLINK_MS = 500L;
        public static final int TEXT_INPUT_OFFSET_X = 3, TEXT_INPUT_HEIGHT_ADJUST = 2;
        public static final int CONFIG_PANEL_WIDTH = 150, CONFIG_BUTTON_HEIGHT = 20, CONFIG_BUTTON_WIDTH = 140;
        public static final int CONFIG_BUTTON_OFFSET_X = 5, CONFIG_BUTTON_OFFSET_Y = 5, CONFIG_BUTTON_SPACING = 25;
        public static final int CONFIG_PANEL_HEIGHT_BUFFER = 20;
        public static final int CONFIG_PANEL_PADDING = 10;
        public static final int GRID_SPACING = 2;
        public static final int DEFAULT_CELL_WIDTH = 20, DEFAULT_CELL_HEIGHT = 20;
        public static final int TEXT_INPUT_WIDTH = 50, TEXT_INPUT_HEIGHT = 12;
        public static final int MARGIN_INPUT_WIDTH = 30;
        public static final int COLOR_INPUT_WIDTH = 40;
        public static final int DEFAULT_TEXT_WIDTH = 100;
        // spotless:on

        public static void renderAlignedText(FontRenderer fr, String text, int x, int y, int width,
            CompositeWidget.HudAlignment alignment, int color) {
            int textWidth = fr.getStringWidth(text);
            int textX = x;
            if (alignment == CompositeWidget.HudAlignment.CENTER) textX += (width - textWidth) / 2;
            else if (alignment == CompositeWidget.HudAlignment.RIGHT)
                textX += width - textWidth - 2 * BUTTON_TEXT_OFFSET_X;
            fr.drawStringWithShadow(text, textX, y, color);
        }
    }

    // ----- HUD Renderer -----

    private static class HUDRenderer {

        public static void renderOverlay(RenderGameOverlayEvent.Post event, boolean shouldDraw, float hudScale) {
            if (!shouldDraw) return;
            Minecraft mc = Minecraft.getMinecraft();
            FontRenderer font = mc.fontRenderer;
            HUDUtils.Point mouse = HUDInputHandler.getScaledMouseCoords(event.resolution, mc);

            HUDInputHandler.handleShiftToggle(mc, INSTANCE);

            GL11.glPushMatrix();
            GL11.glScalef(hudScale, hudScale, hudScale);
            double modelMouseX = mouse.x() / hudScale;
            double modelMouseY = mouse.y() / hudScale;
            INSTANCE.widgets.forEach(w -> w.render(mc, font, modelMouseX, modelMouseY));
            GL11.glPopMatrix();
        }
    }

    // ----- HUD Input Handler -----

    private static class HUDInputHandler {

        public static HUDUtils.Point getScaledMouseCoords(ScaledResolution scaled, Minecraft mc) {
            double screenWidth = scaled.getScaledWidth_double();
            double screenHeight = scaled.getScaledHeight_double();
            double mouseX = Mouse.getX() * screenWidth / mc.displayWidth;
            double mouseY = (mc.displayHeight - Mouse.getY()) * screenHeight / mc.displayHeight;
            return new HUDUtils.Point(mouseX, mouseY);
        }

        public static void handleShiftToggle(Minecraft mc, HUDManager manager) {
            boolean shiftDown = Keyboard.isKeyDown(Keyboard.KEY_RSHIFT);
            if (shiftDown && !manager.wasShiftDown) {
                manager.isEditMode = !manager.isEditMode;
                mc.displayGuiScreen(
                    manager.isEditMode && manager.editGuiSupplier != null ? manager.editGuiSupplier.get() : null);
            }
            manager.wasShiftDown = shiftDown;
        }

        public static void handleMouseClick(int mouseXScaled, int mouseYScaled, int button, float hudScale,
            HUDManager manager) {
            double modelMouseX = mouseXScaled / hudScale;
            double modelMouseY = mouseYScaled / hudScale;

            manager.widgets.forEach(
                w -> w.elements.stream()
                    .filter(e -> e instanceof TextInputElement)
                    .forEach(e -> ((TextInputElement) e).unfocus()));

            manager.activeWidget = findWidgetUnderMouse(modelMouseX, modelMouseY, manager);
            if (manager.activeWidget == null) {
                closeAllDroplists(manager);
                return;
            }

            if (button == 1 && (manager.activeWidget.getIsDeletable() || manager.activeWidget.getIsConfigurable())) {
                Minecraft.getMinecraft()
                    .displayGuiScreen(new HUDGui.GuiWidgetManager(manager.activeWidget));
                return;
            }

            boolean clickedInteractive = manager.activeWidget.elements.stream()
                .anyMatch(
                    e -> e.onMouseClick(
                        modelMouseX,
                        modelMouseY,
                        button,
                        manager.activeWidget.x,
                        manager.activeWidget.y));

            if (manager.isEditMode && !clickedInteractive) {
                manager.isDragging = true;
                manager.dragOffsetX = modelMouseX - manager.activeWidget.x;
                manager.dragOffsetY = modelMouseY - manager.activeWidget.y;
                closeAllDroplists(manager);
            }
        }

        public static void handleMouseRelease(int button, HUDManager manager) {
            if (manager.activeWidget != null) manager.activeWidget.elements.forEach(e -> e.onMouseRelease(button));
            if (button == 0) manager.isDragging = false;
        }

        public static void handleMouseDrag(int mouseXScaled, int mouseYScaled, int button, float hudScale,
            HUDManager manager) {
            double modelMouseX = mouseXScaled / hudScale;
            double modelMouseY = mouseYScaled / hudScale;
            if (manager.activeWidget != null) {
                manager.activeWidget.elements.forEach(
                    e -> e.handleMouseDrag(modelMouseX, modelMouseY, manager.activeWidget.x, manager.activeWidget.y));
                if (manager.isDragging && button == 0) {
                    manager.activeWidget.setPosition(
                        (int) (modelMouseX - manager.dragOffsetX),
                        (int) (modelMouseY - manager.dragOffsetY));
                }
            }
        }

        private static CompositeWidget findWidgetUnderMouse(double modelMouseX, double modelMouseY,
            HUDManager manager) {
            for (int i = manager.widgets.size() - 1; i >= 0; i--) {
                CompositeWidget w = manager.widgets.get(i);
                if (w.elements.stream()
                    .anyMatch(
                        e -> e.isMouseWithin(modelMouseX, modelMouseY, w.x, w.y)
                            || e.acceptsMouseOutside(modelMouseX, modelMouseY, w.x, w.y))) {
                    return w;
                }
            }
            return null;
        }

        private static void closeAllDroplists(HUDManager manager) {
            manager.widgets.forEach(
                w -> w.elements.stream()
                    .filter(e -> e instanceof DroplistElement)
                    .forEach(e -> ((DroplistElement) e).setExpanded(false)));
        }
    }

    // ----- HUD Utils -----

    public static class HUDUtils {

        public static void drawHudRect(int left, int top, int right, int bottom, float r, float g, float b, float a) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(r, g, b, a);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glVertex2i(left, top);
            GL11.glVertex2i(left, bottom);
            GL11.glVertex2i(right, bottom);
            GL11.glVertex2i(right, top);
            GL11.glEnd();
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        public static void drawRectOutline(int left, int top, int right, int bottom, float r, float g, float b, float a,
            int thickness) {
            GL11.glPushAttrib(GL11.GL_ENABLE_BIT);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glColor4f(r, g, b, a);
            GL11.glLineWidth(thickness);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            GL11.glVertex2d(left, bottom);
            GL11.glVertex2d(right, bottom);
            GL11.glVertex2d(right, top);
            GL11.glVertex2d(left, top);
            GL11.glEnd();
            GL11.glPopAttrib();
        }

        @Desugar
        public record Point(double x, double y) {}
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Post event) {
        if (event.type != RenderGameOverlayEvent.ElementType.ALL || !enabled) return;
        HUDRenderer.renderOverlay(event, true, 1.0f);
    }

    // Mouse handling methods now delegate to HUDInputHandler
    public void handleMouseClick(int mouseXScaled, int mouseYScaled, int button, float hudScale) {
        HUDInputHandler.handleMouseClick(mouseXScaled, mouseYScaled, button, hudScale, this);
    }

    public void handleMouseRelease(int button) {
        HUDInputHandler.handleMouseRelease(button, this);
    }

    public void handleMouseDrag(int mouseXScaled, int mouseYScaled, int button, float hudScale) {
        HUDInputHandler.handleMouseDrag(mouseXScaled, mouseYScaled, button, hudScale, this);
    }
}
