package gregtech.client.hud.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import gregtech.client.hud.HUDGui;
import gregtech.client.hud.HUDManager;

public class RectElement extends WidgetElement<RectElement> implements Configurable {

    public RectElement() {
        hoverHighlight = false;
    }

    protected TextInputElement createHeightInput(int yOff) {
        return new TextInputElement(text -> {
            try {
                height = Integer.parseInt(text);
                updateAfterConfigChange();
            } catch (NumberFormatException ignored) {}
        }).pos(70, yOff - HUDManager.UIConstants.TEXT_INPUT_HEIGHT_ADJUST)
            .size(HUDManager.UIConstants.TEXT_INPUT_WIDTH, HUDManager.UIConstants.TEXT_INPUT_HEIGHT);
    }

    protected TextInputElement createColorInput(int yOff, String component) {
        return new TextInputElement(text -> {
            try {
                float value = Float.parseFloat(text);
                switch (component) {
                    case "red":
                        red = value;
                        break;
                    case "green":
                        green = value;
                        break;
                    case "blue":
                        blue = value;
                        break;
                    case "alpha":
                        alpha = value;
                        break;
                }
                hasColor = true;
                updateAfterConfigChange();
            } catch (NumberFormatException ignored) {}
        }).pos(
            HUDManager.UIConstants.CONFIG_BUTTON_OFFSET_X
                + (component.equals("red") ? 0 : component.equals("green") ? 50 : component.equals("blue") ? 100 : 150),
            yOff - HUDManager.UIConstants.TEXT_INPUT_HEIGHT_ADJUST)
            .size(HUDManager.UIConstants.COLOR_INPUT_WIDTH, HUDManager.UIConstants.TEXT_INPUT_HEIGHT);
    }

    @Override
    protected void renderSelf(Minecraft mc, FontRenderer fr, int baseX, int baseY, double mouseX, double mouseY) {
        renderBackground(
            baseX,
            baseY,
            mouseX,
            mouseY,
            HUDManager.UIConstants.TILE_BG_R,
            HUDManager.UIConstants.TILE_BG_G,
            HUDManager.UIConstants.TILE_BG_B,
            HUDManager.UIConstants.TILE_ALPHA,
            HUDManager.UIConstants.TILE_HOVER_R,
            HUDManager.UIConstants.TILE_HOVER_G,
            HUDManager.UIConstants.TILE_HOVER_B,
            HUDManager.UIConstants.TILE_ALPHA);
    }

    @Override
    public int configure(HUDGui.GuiConfigureElement gui, int yOff) {
        // spotless:off
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Width:", this.width, val -> { this.width = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Height:", this.height, val -> { this.height = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset Horizontal:", this.offsetHorizontal, val -> { this.offsetHorizontal = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset Vertical:", this.offsetVertical, val -> { this.offsetVertical = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createFloatConfig(gui, "Red:", this.red, val -> { this.red = (float) val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createFloatConfig(gui, "Green:", this.green, val -> { this.green = (float) val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createFloatConfig(gui, "Blue:", this.blue, val -> { this.blue = (float) val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createFloatConfig(gui, "Alpha:", this.alpha, val -> { this.alpha = (float) val; updateAfterConfigChange(); }, yOff);
        //spotless:on
        return yOff;
    }

    @Override
    public int getConfigSpacingCount() {
        return 8;
    }
}
