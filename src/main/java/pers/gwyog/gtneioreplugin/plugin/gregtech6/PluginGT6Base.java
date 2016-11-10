package pers.gwyog.gtneioreplugin.plugin.gregtech6;

import gregapi.data.CS;
import gregapi.lang.LanguageHandler;
import net.minecraft.client.resources.I18n;
import pers.gwyog.gtneioreplugin.plugin.PluginBase;

public class PluginGT6Base extends PluginBase {
    
    public static String getLocalizedOreName(short meta) {
        // meta==-1 means OreDictMaterial is MT.NULL
        if (meta==-1)
            return I18n.format("gtnop.ore.null.name");
        else
            return LanguageHandler.translate("oredict.ore" + CS.BlocksGT.ore.getMetaMaterial(meta).mNameInternal + ".name");
    } 
    
    public static String getLocalizedSmallOreName(short meta) {
        // meta==-1 means OreDictMaterial is MT.NULL
        if (meta==-1)
            return I18n.format("gtnop.ore.null.name");
        else
            return LanguageHandler.translate("oredict.oreSmall" + CS.BlocksGT.oreSmall.getMetaMaterial(meta).mNameInternal + ".name");
    }
    
    public static String getLocalizedOreBedrockName(short meta) {
        // meta==-1 means OreDictMaterial is MT.NULL
        if (meta==-1)
            return I18n.format("gtnop.ore.null.name");
        else
            return LanguageHandler.translate("oredict.oreBedrock" + CS.BlocksGT.ore.getMetaMaterial(meta).mNameInternal + ".name");
    } 
    
    
}
