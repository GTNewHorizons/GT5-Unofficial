package gregtech.client.hud.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.client.hud.CompositeWidget;
import gregtech.client.hud.HUDGui;
import gregtech.client.hud.HUDManager;

@SideOnly(Side.CLIENT)
public class ButtonElement extends WidgetElement<ButtonElement> implements Configurable {

    private String label;
    private final Runnable action;
    private CompositeWidget.HudAlignment alignment = CompositeWidget.HudAlignment.CENTER;

    public ButtonElement(String label, Runnable action) {
        this.label = label;
        this.action = action;
        this.width = HUDManager.UIConstants.CONFIG_BUTTON_WIDTH;
        this.height = HUDManager.UIConstants.CONFIG_BUTTON_HEIGHT;
    }

    public ButtonElement align(CompositeWidget.HudAlignment alignment) {
        this.alignment = alignment;
        return this;
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

    protected TextInputElement createTextInput(int yOff) {
        return new TextInputElement(text -> {
            setLabel(text);
            updateAfterConfigChange();
        }).pos(70, yOff - HUDManager.UIConstants.TEXT_INPUT_HEIGHT_ADJUST)
            .size(HUDManager.UIConstants.TEXT_INPUT_WIDTH, HUDManager.UIConstants.TEXT_INPUT_HEIGHT);
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

    @Override
    protected void renderSelf(Minecraft mc, FontRenderer fr, int baseX, int baseY, double mouseX, double mouseY) {
        renderBackground(
            baseX,
            baseY,
            mouseX,
            mouseY,
            HUDManager.UIConstants.BUTTON_BG_R,
            HUDManager.UIConstants.BUTTON_BG_G,
            HUDManager.UIConstants.BUTTON_BG_B,
            HUDManager.UIConstants.BUTTON_ALPHA,
            HUDManager.UIConstants.BUTTON_HOVER_R,
            HUDManager.UIConstants.BUTTON_HOVER_G,
            HUDManager.UIConstants.BUTTON_HOVER_B,
            HUDManager.UIConstants.BUTTON_ALPHA);
        HUDManager.UIConstants.renderAlignedText(
            fr,
            label,
            absLeft(baseX) + HUDManager.UIConstants.BUTTON_TEXT_OFFSET_X,
            absTop(baseY) + HUDManager.UIConstants.BUTTON_TEXT_OFFSET_Y,
            width,
            alignment,
            HUDManager.UIConstants.TEXT_COLOR);
    }

    @Override
    public boolean onMouseClick(double mouseX, double mouseY, int button, int baseX, int baseY) {
        if (button == 0 && isMouseWithinBounds(mouseX, mouseY, baseX, baseY)) {
            action.run();
            return true;
        }
        return super.onMouseClick(mouseX, mouseY, button, baseX, baseY);
    }

    @Override
    public int configure(HUDGui.GuiConfigureElement gui, int yOff) {
        // spotless:off
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Width:", this.width, val -> { this.width = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Height:", this.height, val -> { this.height = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset Horizontal:", this.offsetHorizontal, val -> { this.offsetHorizontal = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset Vertical:", this.offsetVertical, val -> { this.offsetVertical = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createStringConfig(gui, "Text:", getLabel(), val -> { setLabel(val); updateAfterConfigChange(); }, yOff);
        //spotless:on
        return yOff;
    }

    @Override
    public int getConfigSpacingCount() {
        return 5;
    }
}
