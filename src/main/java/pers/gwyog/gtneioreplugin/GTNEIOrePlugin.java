package pers.gwyog.gtneioreplugin;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;
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
import gregtech.GT_Version;
import pers.gwyog.gtneioreplugin.plugin.IMCForNEI;
import pers.gwyog.gtneioreplugin.plugin.block.ModBlocks;
import pers.gwyog.gtneioreplugin.util.CSVMaker;
import pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreSmallHelper;
import pers.gwyog.gtneioreplugin.util.GT5UndergroundFluidHelper;

import java.io.File;

@Mod(
    modid = GTNEIOrePlugin.MODID,
    name = GTNEIOrePlugin.NAME,
    version = GTNEIOrePlugin.VERSION,
    dependencies = "required-after:gregtech;required-after:NotEnoughItems")
public class GTNEIOrePlugin {
    static {
        try {
            ConfigurationManager.registerConfig(Config.class);
        }
        catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String MODID = "gtneioreplugin";
    public static final String NAME = "GT NEI Ore Plugin GT:NH Mod";
    public static final String VERSION = GT_Version.VERSION;
    public static final Logger LOG = LogManager.getLogger(NAME);
    public static File instanceDir;
    public static final CreativeTabs creativeTab = new CreativeTabs(MODID) {

        @Override
        public Item getTabIconItem() {
            return GameRegistry.makeItemStack("gregtech:gt.blockores", 386, 1, null)
                .getItem();
        }
    };

    @Mod.Instance(MODID)
    public static GTNEIOrePlugin instance;

    @EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        instanceDir = event.getModConfigurationDirectory().getParentFile();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ModBlocks.init();
        IMCForNEI.IMCSender();
    }

    @EventHandler
    public void onLoadComplete(FMLLoadCompleteEvent event) {
        GT5OreLayerHelper.init();
        GT5OreSmallHelper.init();
        GT5UndergroundFluidHelper.init();
        if (event.getSide() == Side.CLIENT) {
            if (Config.printCsv) {
                new CSVMaker().run();
            }
        }
    }
}
