package com.github.technus.tectech;

import com.github.technus.tectech.auxiliary.Reference;
import com.github.technus.tectech.auxiliary.TecTechConfig;
import com.github.technus.tectech.loader.GT_CustomLoader;
import com.github.technus.tectech.proxy.CommonProxy;
import com.github.technus.tectech.thing.block.QuantumGlass;
import com.github.technus.tectech.thing.casing.GT_Container_CasingsTT;
import com.github.technus.tectech.thing.item.debug_container_EM;
import com.github.technus.tectech.thing.machineTT;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eu.usrv.yamcore.auxiliary.IngameErrorLog;
import eu.usrv.yamcore.auxiliary.LogHelper;
import gregtech.api.GregTech_API;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.List;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:Forge@[10.13.4.1614,);"
        + "required-after:YAMCore@[0.5.70,);" + "required-after:gregtech;")
public class TecTech {

    @SidedProxy(clientSide = Reference.CLIENTSIDE, serverSide = Reference.SERVERSIDE)
    public static CommonProxy proxy;

    @Instance(Reference.MODID)
    public static TecTech instance;

    private static IngameErrorLog Module_AdminErrorLogs = null;
    public static GT_CustomLoader GTCustomLoader = null;
    public static TecTechConfig ModConfig;
    public static XSTR Rnd = null;
    public static LogHelper Logger = new LogHelper(Reference.MODID);
    public static CreativeTabs mainTab = null;

    public static void AddLoginError(String pMessage) {
        if (Module_AdminErrorLogs != null)
            Module_AdminErrorLogs.AddErrorLogOnAdminJoin(pMessage);
    }

    @EventHandler
    public void PreLoad(FMLPreInitializationEvent PreEvent) {
        Logger.setDebugOutput(true);
        Rnd = new XSTR();

        ModConfig = new TecTechConfig(PreEvent.getModConfigurationDirectory(), Reference.COLLECTIONNAME,
                Reference.MODID);
        if (!ModConfig.LoadConfig())
            Logger.error(Reference.MODID + " could not load its config file. Things are going to be weird!");

        if (ModConfig.ModAdminErrorLogs_Enabled) {
            Logger.debug("Module_AdminErrorLogs is enabled");
            Module_AdminErrorLogs = new IngameErrorLog();
        }
    }

    @EventHandler
    public void load(FMLInitializationEvent event) {

        proxy.registerRenderInfo();
    }

    @EventHandler
    public void PostLoad(FMLPostInitializationEvent PostEvent) {
        QuantumGlass.run();
        debug_container_EM.run();

        GTCustomLoader = new GT_CustomLoader();
        GTCustomLoader.run();
        GTCustomLoader.run2();

        mainTab = new CreativeTabs("TecTech") {
            @SideOnly(Side.CLIENT)
            @Override
            public Item getTabIconItem() {
                return debug_container_EM.INSTANCE;
            }

            @Override
            public void displayAllReleventItems(List stuffToShow) {
                for (int i = 1; i < GregTech_API.METATILEENTITIES.length; i++) {
                    if (GregTech_API.METATILEENTITIES[i] instanceof machineTT) {
                        stuffToShow.add(new ItemStack(GregTech_API.sBlockMachines, 1, i));
                    }
                }
                super.displayAllReleventItems(stuffToShow);
            }
        };

        RegisterThingsInTabs();
        if (Loader.isModLoaded("dreamcraft")) ;//TODO init recipes for GTNH coremod
    }

    public void RegisterThingsInTabs() {
        QuantumGlass.INSTANCE.setCreativeTab(mainTab);
        GT_Container_CasingsTT.sBlockCasingsTT.setCreativeTab(mainTab);
        debug_container_EM.INSTANCE.setCreativeTab(mainTab);
    }

    /**
     * Do some stuff once the server starts
     *
     * @param pEvent
     */
    @EventHandler
    public void serverLoad(FMLServerStartingEvent pEvent) {
    }
}
