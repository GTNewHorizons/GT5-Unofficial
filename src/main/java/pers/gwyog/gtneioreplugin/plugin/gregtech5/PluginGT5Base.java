package pers.gwyog.gtneioreplugin.plugin.gregtech5;

import java.awt.Rectangle;

import codechicken.lib.gui.GuiDraw;
import codechicken.nei.recipe.TemplateRecipeHandler;
import codechicken.nei.recipe.TemplateRecipeHandler.RecipeTransferRect;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import pers.gwyog.gtneioreplugin.plugin.PluginBase;

public class PluginGT5Base extends PluginBase {
    
    public int getMaximumMaterialIndex(short meta, boolean smallOre) {
        int offset = smallOre? 16000: 0;
        if (!getGTOreLocalizedName((short)(meta+offset+5000)).equals(getGTOreUnlocalizedName((short)(meta+offset+5000))))
            return 7;
        else if (!getGTOreLocalizedName((short)(meta+offset+5000)).equals(getGTOreUnlocalizedName((short)(meta+offset+5000))))
            return 6;
        else 
            return 5;
    }
    
    public String getGTOreLocalizedName(short index) {
    	
    	if (!Materials.getLocalizedNameForItem(GT_LanguageManager.getTranslation(getGTOreUnlocalizedName(index)), index%1000).contains("Awakened"))
        return Materials.getLocalizedNameForItem(GT_LanguageManager.getTranslation(getGTOreUnlocalizedName(index)), index%1000);
    	else
    	return "Aw. Draconium Ore";
    }
    
    public String getGTOreUnlocalizedName(short index) {
        return "gt.blockores." + index + ".name";
    }

}
