package pers.gwyog.gtneioreplugin;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.init.Items;
import pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreSmallHelper;
import pers.gwyog.gtneioreplugin.util.Oremix;

@Mod(modid = GTNEIOrePlugin.MODID, name = GTNEIOrePlugin.NAME, version = GTNEIOrePlugin.VERSION, dependencies = "required-after:gregtech;required-after:NotEnoughItems")
public class GTNEIOrePlugin {
    public static final String MODID = "gtneioreplugin";
    public static final String NAME = "GT NEI Ore Plugin GT:NH Mod";
    public static final String VERSION = "@version@";
    public static boolean csv = false;
    public static String CSVname; 
    public static List<Oremix> OreVeins=new ArrayList();
    public static HashSet OreV=new HashSet();
    
    @Mod.Instance(MODID)
    public static GTNEIOrePlugin instance;
    
    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
    	Config c = new Config(event, this.MODID+".cfg");
    	csv = c.tConfig.getBoolean("print csv","ALL", false, "princsv, you need apache commons collections to be injected in the minecraft jar.");
    	CSVname = c.tConfig.getString("CSV_name", "ALL", event.getModConfigurationDirectory()+"/GTNH-Oresheet.csv", "rename the oresheet here, it will appear in /config");
    	c.save();
    }
    
    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        if (event.getSide() == Side.CLIENT) {
        	GT5OreLayerHelper h = new GT5OreLayerHelper();
                new GT5OreSmallHelper();
                if (csv)
                	h.make_csv();
            	}
            }

}
