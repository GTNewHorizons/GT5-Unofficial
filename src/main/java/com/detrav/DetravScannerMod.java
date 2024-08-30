package com.detrav;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraftforge.common.config.Configuration;

import com.detrav.net.DetravNetwork;
import com.detrav.proxies.CommonProxy;
import com.detrav.utils.DetravCreativeTab;
import com.detrav.utils.FluidColors;
import com.detrav.utils.GTppHelper;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import gregtech.GT_Version;
import gregtech.api.GregTech_API;

@Mod(
    modid = DetravScannerMod.MODID,
    version = GT_Version.VERSION,
    dependencies = "required-after:IC2;required-after:gregtech;after:miscutils;after:bartworks")
public class DetravScannerMod {

    public static final String MODID = "detravscannermod";
    public static final boolean DEBUG_ENABLED = Boolean.parseBoolean(System.getProperty("com.detrav.debug", "false"));
    public static final CreativeTabs TAB_DETRAV = new DetravCreativeTab();
    public static boolean isDreamcraftLoaded = false;
    public static boolean isBartWorksLoaded = false;
    public static boolean isGTppLoaded = false;

    @SidedProxy(clientSide = "com.detrav.proxies.ClientProxy", serverSide = "com.detrav.proxies.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance(DetravScannerMod.MODID)
    public static DetravScannerMod instance;

    public DetravScannerMod() {
        GregTech_API.sAfterGTPreload.add(new Detrav_AfterGTPreload_Loader());
        isDreamcraftLoaded = Loader.isModLoaded("dreamcraft");
        isBartWorksLoaded = Loader.isModLoaded("bartworks");
        isGTppLoaded = Loader.isModLoaded("miscutils");

        new DetravNetwork();
    }

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        Configuration Config = new Configuration(event.getSuggestedConfigurationFile());
        Config.load();

        if (Config.hasChanged()) {
            Config.save();
        }

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
        if (isGTppLoaded) GTppHelper.generate_OreIDs();
        FluidColors.makeColors();
    }
}
