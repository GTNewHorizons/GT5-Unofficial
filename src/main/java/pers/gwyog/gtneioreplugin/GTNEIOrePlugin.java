package pers.gwyog.gtneioreplugin;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreSmallHelper;

import java.util.HashSet;

@Mod(modid = GTNEIOrePlugin.MODID, name = GTNEIOrePlugin.NAME, version = GTNEIOrePlugin.VERSION, dependencies = "required-after:gregtech;required-after:NotEnoughItems")
public class GTNEIOrePlugin {
    public static final String MODID = "gtneioreplugin";
    public static final String NAME = "GT NEI Ore Plugin GT:NH Mod";
    public static final String VERSION = "@version@";
    public static final Logger LOG = LogManager.getLogger(NAME);
    public static boolean csv = false;
    public static String CSVname;
    public static String CSVnameSmall;
    public static HashSet OreV = new HashSet();
    public static boolean hideBackground = true;
    public static boolean toolTips = true;
    @Mod.Instance(MODID)
    public static GTNEIOrePlugin instance;

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        Config c = new Config(event, this.MODID + ".cfg");
        csv = c.tConfig.getBoolean("print csv", "ALL", false, "princsv, you need apache commons collections to be injected in the minecraft jar.");
        CSVname = c.tConfig.getString("CSV_name", "ALL", event.getModConfigurationDirectory() + "/GTNH-Oresheet.csv", "rename the oresheet here, it will appear in /config");
        CSVnameSmall= c.tConfig.getString("CSV_name_for_Small_Ore_Sheet", "ALL", event.getModConfigurationDirectory() + "/GTNH-Small-Ores-Sheet.csv", "rename the oresheet here, it will appear in /config");
        hideBackground = c.tConfig.getBoolean("Hide Background", "ALL", true, "Hides the Background when the tooltip for the Dimensions is rendered");
        toolTips = c.tConfig.getBoolean("DimTooltip", "ALL", true, "Activates Dimensison Tooltips");

        c.save();
    }

    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        if (event.getSide() == Side.CLIENT) {
            new GT5OreLayerHelper();
            new GT5OreSmallHelper();
            if (csv) {
                new pers.gwyog.gtneioreplugin.util.CSVMaker().run();
            }
        }
    }

}
