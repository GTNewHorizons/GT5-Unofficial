package com.detrav;

import com.detrav.proxies.CommonProxy;
import com.detrav.utils.DetravCreativeTab;
import com.detrav.net.DetravNetwork;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import gregtech.api.GregTech_API;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;

@Mod(modid = DetravScannerMod.MODID, version = DetravScannerMod.VERSION,dependencies = "required-after:IC2;required-after:gregtech")
public class DetravScannerMod
{
    public static final String MODID = "detravscannermod";
    public static final String VERSION = "0.6";

    public static final CreativeTabs TAB_DETRAV = new DetravCreativeTab();

    @SidedProxy(clientSide = "com.detrav.proxies.ClientProxy", serverSide = "com.detrav.proxies.ServerProxy")
    public static CommonProxy proxy;

    @Mod.Instance(DetravScannerMod.MODID)
    public static DetravScannerMod instance;

    public  DetravScannerMod()
    {
        GregTech_API.sAfterGTPreload.add(new DetravLoader());
        new DetravNetwork();
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
		// some example code
        //System.out.println("DIRT BLOCK >> "+Blocks.dirt.getUnlocalizedName());
        NetworkRegistry.INSTANCE.registerGuiHandler(instance, proxy );
    }

    @EventHandler
    public void onLoad(FMLInitializationEvent aEvent)
    {
        proxy.onLoad();
    }

    @EventHandler
    public void onPostLoad(FMLPostInitializationEvent aEvent)
    {
        proxy.onPostLoad();
    }

    @EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        //event.registerServerCommand(new DetravScannerCommand());
    }
}
