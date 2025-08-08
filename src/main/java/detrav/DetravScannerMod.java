package detrav;

import net.minecraft.creativetab.CreativeTabs;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import detrav.items.DetravMetaGeneratedTool01;
import detrav.items.processing.ProcessingDetravToolProspector;
import detrav.net.DetravNetwork;
import detrav.proxies.CommonProxy;
import detrav.utils.DetravCreativeTab;
import gregtech.GT_Version;
import gregtech.api.GregTechAPI;

@Mod(
    modid = DetravScannerMod.MODID,
    version = GT_Version.VERSION,
    dependencies = "required-after:IC2;required-after:gregtech;after:miscutils;after:bartworks")
public class DetravScannerMod {

    public static final String MODID = "detravscannermod";
    public static final boolean DEBUG_ENABLED = Boolean.parseBoolean(System.getProperty("com.detrav.debug", "false"));
    public static final CreativeTabs TAB_DETRAV = new DetravCreativeTab();

    @SidedProxy(clientSide = "detrav.proxies.ClientProxy", serverSide = "detrav.proxies.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance(DetravScannerMod.MODID)
    public static DetravScannerMod instance;

    public DetravScannerMod() {
        GregTechAPI.sAfterGTPreload.add(() -> {
            new DetravMetaGeneratedTool01(); // items
            new ProcessingDetravToolProspector(); // recipes and etc
        });
        new DetravNetwork();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        proxy.onPreInit();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy);
        proxy.onLoad();
    }

    @EventHandler
    public void onPostLoad(FMLPostInitializationEvent aEvent) {
        proxy.onPostLoad();
    }
}
