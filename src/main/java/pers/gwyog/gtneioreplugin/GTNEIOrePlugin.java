package pers.gwyog.gtneioreplugin;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import pers.gwyog.gtneioreplugin.plugin.IMCForNEI;
import pers.gwyog.gtneioreplugin.plugin.block.ModBlocks;
import pers.gwyog.gtneioreplugin.util.CSVMaker;
import pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreSmallHelper;
import pers.gwyog.gtneioreplugin.util.GT5UndergroundFluidHelper;
import pers.gwyog.gtneioreplugin.util.GuiRecipeHelper;

@Mod(
        modid = GTNEIOrePlugin.MODID,
        name = GTNEIOrePlugin.NAME,
        version = GTNEIOrePlugin.VERSION,
        dependencies = "required-after:gregtech;required-after:NotEnoughItems")
public class GTNEIOrePlugin {

    public static final String MODID = "gtneioreplugin";
    public static final String NAME = "GT NEI Ore Plugin GT:NH Mod";
    public static final String VERSION = Tags.VERSION;
    public static final Logger LOG = LogManager.getLogger(NAME);
    public static boolean csv = false;
    public static String CSVname;
    public static String CSVnameSmall;
    public static boolean toolTips = true;
    public static int maxTooltipLines = 11;
    public static final CreativeTabs creativeTab = new CreativeTabs(MODID) {

        @Override
        public Item getTabIconItem() {
            return GameRegistry.makeItemStack("gregtech:gt.blockores", 386, 1, null).getItem();
        }
    };

    @Mod.Instance(MODID)
    public static GTNEIOrePlugin instance;

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        Config c = new Config(event, MODID + ".cfg");
        csv = c.tConfig.getBoolean(
                "print csv",
                "ALL",
                false,
                "print csv, you need apache commons collections to be injected in the minecraft jar.");
        CSVname = c.tConfig.getString(
                "CSV_name",
                "ALL",
                event.getModConfigurationDirectory() + "/GTNH-Oresheet.csv",
                "rename the oresheet here, it will appear in /config");
        CSVnameSmall = c.tConfig.getString(
                "CSV_name_for_Small_Ore_Sheet",
                "ALL",
                event.getModConfigurationDirectory() + "/GTNH-Small-Ores-Sheet.csv",
                "rename the oresheet here, it will appear in /config");
        toolTips = c.tConfig.getBoolean("DimTooltip", "ALL", true, "Activates Dimension Tooltips");
        maxTooltipLines = c.tConfig.getInt(
                "MaxToolTipLines",
                "ALL",
                11,
                1,
                Integer.MAX_VALUE,
                "Maximum number of lines the dimension names tooltip can have before it wraps around.");

        c.save();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ModBlocks.init();
        IMCForNEI.IMCSender();
    }

    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        new GT5OreLayerHelper();
        new GT5OreSmallHelper();
        new GT5UndergroundFluidHelper();
        if (event.getSide() == Side.CLIENT) {
            new GuiRecipeHelper();
            if (csv) {
                new CSVMaker().run();
            }
        }
    }
}
