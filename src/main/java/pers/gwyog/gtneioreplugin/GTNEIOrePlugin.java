package pers.gwyog.gtneioreplugin;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import pers.gwyog.gtneioreplugin.util.GT6OreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GT6OreSmallHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreSmallHelper;
import pers.gwyog.gtneioreplugin.util.GT6OreBedrockHelper;

@Mod(modid = GTNEIOrePlugin.MODID, name = GTNEIOrePlugin.NAME, version = GTNEIOrePlugin.VERSION, dependencies = "required-after:gregtech;required-after:NotEnoughItems")
public class GTNEIOrePlugin {
    public static final String MODID = "gtneioreplugin";
    public static final String NAME = "GT NEI Ore Plugin";
    public static final String VERSION = "1.0.3";
    
    // default is GT5, however it will be "GT6" when GT6 is installed
    public static String GTVersion = "GT5";
    
    @Mod.Instance(MODID)
    public static GTNEIOrePlugin instance;
    
    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        if (event.getSide() == Side.CLIENT) {
            try {
                Class clazzGT6API = Class.forName("gregapi.GT_API");
                GTVersion = "GT6";
            } catch (ClassNotFoundException e) {}
            if (GTVersion.equals("GT5")) {
                new GT5OreLayerHelper();
                new GT5OreSmallHelper();
            }
            else { 
                new GT6OreLayerHelper();
                new GT6OreSmallHelper();
                new GT6OreBedrockHelper();
            }
        }
    }

}
