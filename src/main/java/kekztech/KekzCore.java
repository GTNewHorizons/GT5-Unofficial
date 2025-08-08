package kekztech;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import gregtech.GT_Version;
import kekztech.common.CommonProxy;

/**
 * My GT-Meta-IDs are: 13101 - 13500
 *
 * @author kekzdealer
 */
@Mod(
    modid = KekzCore.MODID,
    name = KekzCore.NAME,
    version = KekzCore.VERSION,
    dependencies = "required-after:IC2;" + "required-after:gregtech;"
        + "required-after:tectech;"
        + "after:Thaumcraft;"
        + "after:ThaumicTinkerer;"
        + "after:bartworks;"
        + "after:dreamcraft")
public class KekzCore {

    public static final String NAME = "KekzTech";
    public static final String MODID = "kekztech";
    public static final String VERSION = GT_Version.VERSION;
    public static final Logger LOGGER = LogManager.getLogger(NAME);

    @Mod.Instance(MODID)
    public static KekzCore instance;

    @SidedProxy(clientSide = "kekztech.client.ClientProxy", serverSide = "kekztech.ServerProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }
}
