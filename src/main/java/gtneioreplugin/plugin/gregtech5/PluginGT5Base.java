package gtneioreplugin.plugin.gregtech5;

import net.minecraft.client.resources.I18n;

import codechicken.lib.gui.GuiDraw;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gtneioreplugin.plugin.PluginBase;

public abstract class PluginGT5Base extends PluginBase {

    protected String getGTOreLocalizedName(IOreMaterial ore, boolean small) {
        if (ore == Materials.DraconiumAwakened) return "Aw. Draconium Ore";

        try (OreInfo<IOreMaterial> info = OreInfo.getNewInfo()) {
            info.material = ore;
            info.isSmall = small;

            return OreManager.getLocalizedName(info);
        }
    }

    protected void drawLine(String lineKey, String value, int x, int y) {
        String text = I18n.format(lineKey) + ": " + value;
        drawLineWithWidth(text, x, y, getGuiWidth() - 10);
    }

    protected void drawLine(String lineKey, String value, int x, int y, int maxWidth) {
        String text = I18n.format(lineKey) + ": " + value;
        drawLineWithWidth(text, x, y, maxWidth);
    }

    private void drawLineWithWidth(String text, int x, int y, int maxWidth) {
        String textToDraw = text;
        int clampedMaxWidth = Math.max(0, maxWidth);
        int ellipsisWidth = GuiDraw.fontRenderer.getStringWidth("...");

        if (GuiDraw.fontRenderer.getStringWidth(text) > clampedMaxWidth) {
            if (clampedMaxWidth > ellipsisWidth) {
                String trimmed = GuiDraw.fontRenderer.trimStringToWidth(text, clampedMaxWidth - ellipsisWidth);
                textToDraw = trimmed + "...";
            } else {
                textToDraw = GuiDraw.fontRenderer.trimStringToWidth(text, clampedMaxWidth);
            }
        }

        GuiDraw.drawString(textToDraw, x, y, 0x404040, false);
    }

    /**
     * Draw the dimension header and the dimension names over up to 3 lines
     *
     */
    protected void drawDimNames() {
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.worldNames") + ": ", 2, 100, 0x404040, false);
    }
}
