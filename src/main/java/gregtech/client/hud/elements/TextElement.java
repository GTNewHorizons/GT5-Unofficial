package gregtech.client.hud.elements;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.client.hud.CompositeWidget;
import gregtech.client.hud.HUDGui;
import gregtech.client.hud.HUDManager;

@SideOnly(Side.CLIENT)
public class TextElement extends WidgetElement<TextElement> implements Configurable {

    private Supplier<String> textSupplier;
    private Supplier<Integer> colorSupplier = () -> HUDManager.UIConstants.TEXT_COLOR;
    private CompositeWidget.HudAlignment alignment = CompositeWidget.HudAlignment.LEFT;

    public TextElement(String text) {
        this(() -> text);
    }

    public TextElement(Supplier<String> textSupplier) {
        this.textSupplier = textSupplier;
        this.hoverHighlight = false;
        this.width = HUDManager.UIConstants.DEFAULT_TEXT_WIDTH;
        this.height = 9; // Font height
    }

    public TextElement setTextSupplier(Supplier<String> supplier) {
        this.textSupplier = supplier;
        return this;
    }

    public TextElement colorSupplier(Supplier<Integer> supplier) {
        this.colorSupplier = supplier;
        return this;
    }

    public TextElement align(CompositeWidget.HudAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    @Override
    protected void renderSelf(Minecraft mc, FontRenderer fr, int baseX, int baseY, double mouseX, double mouseY) {
        HUDManager.UIConstants.renderAlignedText(
            fr,
            textSupplier.get(),
            absLeft(baseX),
            absTop(baseY),
            width,
            alignment,
            colorSupplier.get());
    }

    @Override
    public int configure(HUDGui.GuiConfigureElement gui, int yOff) {
        // spotless:off
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Width:", width, val -> {width = val;updateAfterConfigChange();}, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Height:", this.height, val -> {this.height = val;updateAfterConfigChange();}, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset Horizontal:", offsetHorizontal, val -> {offsetHorizontal = val;updateAfterConfigChange();}, yOff);
        yOff = HUDGui.GuiConfigureElement.createIntConfig(gui, "Offset Vertical:", offsetVertical, val -> {offsetVertical = val;updateAfterConfigChange();}, yOff);
        //spotless:on
        return yOff;
    }

    @Override
    public int getConfigSpacingCount() {
        return 4;
    }
}
