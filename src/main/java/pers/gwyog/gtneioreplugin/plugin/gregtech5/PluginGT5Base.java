package pers.gwyog.gtneioreplugin.plugin.gregtech5;

import codechicken.lib.gui.GuiDraw;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import pers.gwyog.gtneioreplugin.plugin.PluginBase;
import pers.gwyog.gtneioreplugin.util.DimensionHelper;

import java.util.List;

import static pers.gwyog.gtneioreplugin.GTNEIOrePlugin.hideBackground;
import static pers.gwyog.gtneioreplugin.GTNEIOrePlugin.toolTips;

public class PluginGT5Base extends PluginBase {

    protected boolean ttDisplayed = false;

    protected static String getLocalizedNameForItem(Materials aMaterial, String aFormat) {
        return String.format(aFormat.replace("%s", "%temp").replace("%material", "%s"), aMaterial.mDefaultLocalName).replace("%temp", "%s");
    }

    protected static int calculateMaxW(List L) {
        int w = 0;
        FontRenderer font = GuiDraw.fontRenderer;
        for (int i = 0; i < L.size(); ++i) {
            String s = (String) L.get(i);
            w = Math.max(font.getStringWidth(s), w);
        }
        return w;
    }

    protected static String getLocalizedNameForItem(String aFormat, int aMaterialID) {
        if (aMaterialID >= 0 && aMaterialID < 1000) {
            Materials aMaterial = GregTech_API.sGeneratedMaterials[aMaterialID];
            if (aMaterial != null) {
                return getLocalizedNameForItem(aMaterial, aFormat);
            }
        }
        return aFormat;
    }

    public static String getGTOreLocalizedName(short index) {

        if (!getLocalizedNameForItem(GT_LanguageManager.getTranslation(getGTOreUnlocalizedName(index)), index % 1000).contains("Awakened"))
            return getLocalizedNameForItem(GT_LanguageManager.getTranslation(getGTOreUnlocalizedName(index)), index % 1000);
        else
            return "Aw. Draconium Ore";
    }

    protected static String getGTOreUnlocalizedName(short index) {
        return "gt.blockores." + index + ".name";
    }

    protected void drawToolTip(String sDimNames) {
        if (toolTips) {
            ttDisplayed = false;
            if (GuiDraw.getMousePosition().y > (int) (Minecraft.getMinecraft().currentScreen.height * 0.6f) && GuiDraw.getMousePosition().y < (int) (Minecraft.getMinecraft().currentScreen.height * 0.8f)) {
                List<String> dims = DimensionHelper.convertCondensedStringToToolTip(sDimNames);
                int w = calculateMaxW(dims);
                int x = GuiDraw.getMousePosition().x > Minecraft.getMinecraft().currentScreen.width / 2 ? this.getGuiWidth() - w - 8 : 0;
                if (dims.size() > 10) {
                    List<String> dims2 = dims.subList(11, dims.size());
                    int w2 = calculateMaxW(dims2);
                    dims = dims.subList(0, 11);
                    w = calculateMaxW(dims);
                    GuiDraw.drawMultilineTip(x == 0 ? 16 + w : x - (w2 + 8), 108 - (dims.size() * 8), dims2);
                }
                GuiDraw.drawMultilineTip(x, 108 - (dims.size() * 8), dims);

                ttDisplayed = hideBackground;
            }
        }
    }

    protected int getMaximumMaterialIndex(short meta, boolean smallOre) {
        int offset = smallOre ? 16000 : 0;
        if (!getGTOreLocalizedName((short) (meta + offset + 5000)).equals(getGTOreUnlocalizedName((short) (meta + offset + 5000))))
            return 7;
        else if (!getGTOreLocalizedName((short) (meta + offset + 5000)).equals(getGTOreUnlocalizedName((short) (meta + offset + 5000))))
            return 6;
        else
            return 5;
    }

}
