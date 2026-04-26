package gtneioreplugin.plugin.gregtech5;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;

import codechicken.lib.gui.GuiDraw;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.common.ores.OreInfo;
import gregtech.common.ores.OreManager;
import gtneioreplugin.plugin.PluginBase;

public abstract class PluginGT5Base extends PluginBase {

    protected static final int LEFT_PADDING = 2;
    protected static final int TITLE_Y_POS = 1;

    protected String getGTOreLocalizedName(IOreMaterial ore, boolean small) {
        if (ore == Materials.DraconiumAwakened) return "Aw. Draconium Ore";

        try (OreInfo<IOreMaterial> info = OreInfo.getNewInfo()) {
            info.material = ore;
            info.isSmall = small;

            return OreManager.getLocalizedName(info);
        }
    }

    protected int drawTitle(String text) {
        List<String> lines = GuiDraw.fontRenderer.listFormattedStringToWidth(text, getGuiWidth() - 4);
        GuiDraw.drawRect(0, TITLE_Y_POS - 1, getGuiWidth(), lines.size() * 10 + 1, 0xffefd790);
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            int x = (getGuiWidth() - GuiDraw.fontRenderer.getStringWidth(line)) / 2;
            GuiDraw.drawString(line, x, TITLE_Y_POS + i * 11, 0xfafafa, true);
        }
        return lines.size();
    }

    protected void drawLine(String lineKey, String value, int x, int y) {
        String text = I18n.format(lineKey) + ": " + value;
        drawLine(text, x, y);
    }

    protected void drawLine(String text, int x, int y) {
        String trimmed = text;
        if (GuiDraw.fontRenderer.getStringWidth(text) > getGuiWidth() - x) {
            trimmed = GuiDraw.fontRenderer.trimStringToWidth(text, getGuiWidth() - x - 10);
        }

        GuiDraw.drawString(trimmed + (trimmed.length() < text.length() ? "..." : ""), x, y, 0x404040, false);
    }

    protected void drawHeader(String key, int x, int y) {
        GuiDraw.drawString(EnumChatFormatting.UNDERLINE + I18n.format(key), x, y, 0x303030, false);
    }

    protected void drawDimHeader(int y) {
        GuiDraw.drawString(I18n.format("gtnop.gui.nei.worldNames") + ": ", 2, y, 0x404040, false);
    }
}
