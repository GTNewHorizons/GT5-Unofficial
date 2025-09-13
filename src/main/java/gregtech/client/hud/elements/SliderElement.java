package gregtech.client.hud.elements;

import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.client.hud.CompositeWidget;
import gregtech.client.hud.HUDGui;
import gregtech.client.hud.HUDManager;

@SideOnly(Side.CLIENT)
public class SliderElement extends WidgetElement<SliderElement> implements Configurable {

    private final Supplier<Double> valueSupplier;
    private final Consumer<Double> onChange;
    private double min, max;
    private boolean isDragging;

    public SliderElement(Supplier<Double> valueSupplier, Consumer<Double> onChange, double min, double max) {
        this.valueSupplier = valueSupplier;
        this.onChange = onChange;
        this.min = min;
        this.max = max;
        this.width = HUDManager.UIConstants.CONFIG_BUTTON_WIDTH;
        this.height = HUDManager.UIConstants.CONFIG_BUTTON_HEIGHT;
    }

    @Override
    protected void renderSelf(Minecraft mc, FontRenderer fr, int baseX, int baseY, double mouseX, double mouseY) {
        renderBackground(
            baseX,
            baseY,
            mouseX,
            mouseY,
            HUDManager.UIConstants.SLIDER_BG_R,
            HUDManager.UIConstants.SLIDER_BG_G,
            HUDManager.UIConstants.SLIDER_BG_B,
            HUDManager.UIConstants.BG_ALPHA,
            HUDManager.UIConstants.HOVER_R,
            HUDManager.UIConstants.HOVER_G,
            HUDManager.UIConstants.HOVER_B,
            HUDManager.UIConstants.BG_ALPHA);

        double value = valueSupplier.get();
        double ratio = (value - min) / (max - min);
        int knobX = absLeft(baseX) + (int) (ratio * (width - 2 * HUDManager.UIConstants.SLIDER_KNOB_HALF));
        HUDManager.HUDUtils.drawHudRect(
            knobX - HUDManager.UIConstants.SLIDER_KNOB_HALF,
            absTop(baseY),
            knobX + HUDManager.UIConstants.SLIDER_KNOB_HALF,
            absBottom(baseY),
            HUDManager.UIConstants.SLIDER_KNOB_R,
            HUDManager.UIConstants.SLIDER_KNOB_G,
            HUDManager.UIConstants.SLIDER_KNOB_B,
            HUDManager.UIConstants.SLIDER_KNOB_ALPHA);

        String displayText = String.format("%.2f", value);
        HUDManager.UIConstants.renderAlignedText(
            fr,
            displayText,
            absLeft(baseX),
            absTop(baseY) + HUDManager.UIConstants.BUTTON_TEXT_OFFSET_Y,
            width,
            CompositeWidget.HudAlignment.CENTER,
            HUDManager.UIConstants.TEXT_COLOR);
    }

    @Override
    public boolean onMouseClick(double mouseX, double mouseY, int button, int baseX, int baseY) {
        if (button == 0 && isMouseWithinBounds(mouseX, mouseY, baseX, baseY)) {
            isDragging = true;
            updateValue(mouseX, baseX);
            return true;
        }
        return super.onMouseClick(mouseX, mouseY, button, baseX, baseY);
    }

    @Override
    public void onMouseRelease(int button) {
        if (button == 0) {
            isDragging = false;
        }
        super.onMouseRelease(button);
    }

    @Override
    public void handleMouseDrag(double mouseX, double mouseY, int baseX, int baseY) {
        if (isDragging) {
            updateValue(mouseX, baseX);
        }
        super.handleMouseDrag(mouseX, mouseY, baseX, baseY);
    }

    private void updateValue(double mouseX, int baseX) {
        double ratio = (mouseX - absLeft(baseX)) / (width - 2 * HUDManager.UIConstants.SLIDER_KNOB_HALF);
        ratio = Math.max(0, Math.min(1, ratio));
        double newValue = min + ratio * (max - min);
        onChange.accept(newValue);
    }

    @Override
    public int configure(HUDGui.GuiConfigureElement gui, int yOff) {
        // spotless:off
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Width:", this.width, val -> { this.width = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Height:", this.height, val -> { this.height = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset Horizontal:", this.offsetHorizontal, val -> { this.offsetHorizontal = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset Vertical:", this.offsetVertical, val -> { this.offsetVertical = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createDoubleConfig(gui, "Min:", this.min, val -> { this.min = val; updateAfterConfigChange(); }, yOff);
        yOff = HUDGui.GuiConfigureElement.createDoubleConfig(gui, "Max:", this.max, val -> { this.max = val; updateAfterConfigChange(); }, yOff);
        //spotless:on
        return yOff;
    }
}
