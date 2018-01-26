package pers.gwyog.gtneioreplugin;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.relauncher.Side;
import pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreSmallHelper;

@Mod(modid = GTNEIOrePlugin.MODID, name = GTNEIOrePlugin.NAME, version = GTNEIOrePlugin.VERSION, dependencies = "required-after:gregtech;required-after:NotEnoughItems")
public class GTNEIOrePlugin {
    public static final String MODID = "gtneioreplugin";
    public static final String NAME = "GT NEI Ore Plugin GT:NH Mod";
    public static final String VERSION = "1.0.5-GTNH";
    
    public static String GTVersion = "GT5";
    
    @Mod.Instance(MODID)
    public static GTNEIOrePlugin instance;
    
    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        if (event.getSide() == Side.CLIENT) {
                new GT5OreLayerHelper();
                new GT5OreSmallHelper();
            }
        }

}
