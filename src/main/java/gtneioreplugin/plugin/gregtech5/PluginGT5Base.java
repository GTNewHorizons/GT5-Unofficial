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

        String text2 = text;

        if (GuiDraw.fontRenderer.getStringWidth(text) > getGuiWidth()) {
            text2 = GuiDraw.fontRenderer.trimStringToWidth(text, getGuiWidth() - 10);
        }

        GuiDraw.drawString(text2 + (text2.length() < text.length() ? "..." : ""), x, y, 0x404040, false);
    }

    /**
     * Draw the dimension header and the dimension names over up to 3 lines
     *
     */
    protected void drawDimNames() {
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.worldNames") + ": ", 2, 100, 0x404040, false);
    }
}
