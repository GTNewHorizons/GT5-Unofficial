package gregtech.client.hud.elements;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.client.hud.HUDGui;
import gregtech.client.hud.HUDManager;

@SideOnly(Side.CLIENT)
public class RectElement extends WidgetElement<RectElement> implements Configurable {

    public RectElement() {
        hoverHighlight = false;
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
        yOff = HUDGui.GuiConfigureElement.createColorConfig(gui, "Color", this.red, this.green, this.blue, this.alpha,
            val -> { this.red = (float) val; updateAfterConfigChange(); },
            val -> { this.green = (float) val; updateAfterConfigChange(); },
            val -> { this.blue = (float) val; updateAfterConfigChange(); },
            val -> { this.alpha = (float) val; updateAfterConfigChange(); }, yOff);
        //spotless:on
        return yOff;
    }
}
