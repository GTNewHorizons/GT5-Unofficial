package gregtech.client.hud.elements;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.client.hud.CompositeWidget;
import gregtech.client.hud.HUDGui;
import gregtech.client.hud.HUDManager;

@SideOnly(Side.CLIENT)
public class CheckboxElement extends WidgetElement<CheckboxElement> implements Configurable {

    private String label;
    private boolean checked;
    private final Consumer<Boolean> onToggle;

    public CheckboxElement(String label, boolean initialState, Consumer<Boolean> onToggle) {
        this.label = label;
        this.checked = initialState;
        this.onToggle = onToggle;
        this.width = HUDManager.UIConstants.CONFIG_BUTTON_WIDTH;
        this.height = HUDManager.UIConstants.CONFIG_BUTTON_HEIGHT;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    protected void renderSelf(Minecraft mc, FontRenderer fr, int baseX, int baseY, double mouseX, double mouseY) {
        boolean hovered = isMouseWithinBounds(mouseX, mouseY, baseX, baseY);
        renderBackground(
            baseX,
            baseY,
            mouseX,
            mouseY,
            HUDManager.UIConstants.BG_R,
            HUDManager.UIConstants.BG_G,
            HUDManager.UIConstants.BG_B,
            HUDManager.UIConstants.BG_ALPHA,
            HUDManager.UIConstants.HOVER_R,
            HUDManager.UIConstants.HOVER_G,
            HUDManager.UIConstants.HOVER_B,
            HUDManager.UIConstants.BG_ALPHA);

        int checkboxSize = height - 2 * HUDManager.UIConstants.CHECKBOX_INNER_OFFSET;
        int checkboxX = absLeft(baseX) + HUDManager.UIConstants.CHECKBOX_INNER_OFFSET;
        int checkboxY = absTop(baseY) + HUDManager.UIConstants.CHECKBOX_INNER_OFFSET;
        float shade = hovered ? HUDManager.UIConstants.CHECKBOX_SHADE_HOVER
            : HUDManager.UIConstants.CHECKBOX_SHADE_NORMAL;
        HUDManager.HUDUtils.drawHudRect(
            checkboxX,
            checkboxY,
            checkboxX + checkboxSize,
            checkboxY + checkboxSize,
            shade,
            shade,
            shade,
            1.0f);

        if (checked) {
            HUDManager.HUDUtils.drawHudRect(
                checkboxX + HUDManager.UIConstants.CHECKBOX_INNER_OFFSET,
                checkboxY + HUDManager.UIConstants.CHECKBOX_INNER_OFFSET,
                checkboxX + checkboxSize - HUDManager.UIConstants.CHECKBOX_INNER_OFFSET,
                checkboxY + checkboxSize - HUDManager.UIConstants.CHECKBOX_INNER_OFFSET,
                HUDManager.UIConstants.CHECKED_R,
                HUDManager.UIConstants.CHECKED_G,
                HUDManager.UIConstants.CHECKED_B,
                HUDManager.UIConstants.CHECKED_ALPHA);
        }

        HUDManager.UIConstants.renderAlignedText(
            fr,
            label,
            absLeft(baseX) + checkboxSize + HUDManager.UIConstants.CHECKBOX_TEXT_OFFSET_X,
            absTop(baseY) + HUDManager.UIConstants.CHECKBOX_TEXT_OFFSET_Y,
            width - checkboxSize - HUDManager.UIConstants.CHECKBOX_TEXT_OFFSET_X,
            CompositeWidget.HudAlignment.LEFT,
            HUDManager.UIConstants.TEXT_COLOR);
    }

    @Override
    public boolean onMouseClick(double mouseX, double mouseY, int button, int baseX, int baseY) {
        if (button == 0 && isMouseWithinBounds(mouseX, mouseY, baseX, baseY)) {
            checked = !checked;
            onToggle.accept(checked);
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
        yOff = HUDGui.GuiConfigureElement.createStringConfig(gui, "Text:", this.label, val -> { setLabel(val); updateAfterConfigChange(); }, yOff);
        //spotless:on
        return yOff;
    }

    @Override
    public int getConfigSpacingCount() {
        return 5;
    }
}
