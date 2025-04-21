package gtrailways;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import gregtech.GT_Version;
import gregtech.api.enums.Mods;
import gtrailways.items.Items;

@Mod(
    modid = GTRailways.MODID,
    name = GTRailways.NAME,
    version = GTRailways.VERSION,
    dependencies = "required-after:Forge@[10.13.4.1614,);" + "required-after:gregtech;" + " after:Railcraft;")
public class GTRailways {

    public static final String MODID = Mods.Names.G_T_RAILWAYS;
    public static final String NAME = "GT Railways";
    public static final String VERSION = GT_Version.VERSION;

    @Mod.Instance(MODID)
    public static GTRailways instance;

    public static final Logger LOGGER = LogManager.getLogger(NAME);

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info("choo choo dingdindingdingding");
        Items.init();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent postinit) {
        Items.addRecipes();
    }
}
