package com.github.technus.tectech;

import com.github.technus.tectech.loader.MainLoader;
import com.github.technus.tectech.loader.TecTechConfig;
import com.github.technus.tectech.mechanics.ConvertFloat;
import com.github.technus.tectech.mechanics.ConvertInteger;
import com.github.technus.tectech.mechanics.chunkData.ChunkDataHandler;
import com.github.technus.tectech.mechanics.elementalMatter.core.commands.GiveEM;
import com.github.technus.tectech.mechanics.elementalMatter.core.commands.ListEM;
import com.github.technus.tectech.proxy.CommonProxy;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import eu.usrv.yamcore.auxiliary.IngameErrorLog;
import eu.usrv.yamcore.auxiliary.LogHelper;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;

@Mod(modid = Reference.MODID, name = Reference.NAME, version = Reference.VERSION, dependencies = "required-after:Forge@[10.13.4.1614,);"
        + "required-after:YAMCore@[0.5.70,);" + "required-after:gregtech;" + "after:CoFHCore;" + "after:Thaumcraft;" + "after:dreamcraft;")
public class TecTech {
    @SidedProxy(clientSide = Reference.CLIENTSIDE, serverSide = Reference.SERVERSIDE)
    public static CommonProxy proxy;

    @Mod.Instance(Reference.MODID)
    public static TecTech instance;

    public static final XSTR RANDOM = XSTR.XSTR_INSTANCE;
    public static final LogHelper LOGGER = new LogHelper(Reference.MODID);

    private static IngameErrorLog moduleAdminErrorLogs;
    public static TecTechConfig configTecTech;

    public static ChunkDataHandler chunkDataHandler=new ChunkDataHandler();

    /**
     * For Loader.isModLoaded checks during the runtime
     */
    public static boolean hasCOFH = false;

    public static final byte tectechTexturePage1=8;

    public static void AddLoginError(String pMessage) {
        if (moduleAdminErrorLogs != null) {
            moduleAdminErrorLogs.AddErrorLogOnAdminJoin(pMessage);
        }
    }

    static {
        MainLoader.staticLoad();
    }

    @Mod.EventHandler
    public void PreLoad(FMLPreInitializationEvent PreEvent) {
        LOGGER.setDebugOutput(true);

        configTecTech = new TecTechConfig(PreEvent.getModConfigurationDirectory(), Reference.COLLECTIONNAME,
                Reference.MODID);

        if (!configTecTech.LoadConfig()) {
            LOGGER.error(Reference.MODID + " could not load its config file. Things are going to be weird!");
        }

        if (configTecTech.modAdminErrorLogs) {
            LOGGER.setDebugOutput(DEBUG_MODE);
            LOGGER.debug("moduleAdminErrorLogs is enabled");
            moduleAdminErrorLogs = new IngameErrorLog();
        }

        MainLoader.preLoad();
    }

    @Mod.EventHandler
    public void Load(FMLInitializationEvent event) {
        hasCOFH = Loader.isModLoaded(Reference.COFHCORE);

        MainLoader.load();
        MainLoader.addAfterGregTechPostLoadRunner();
    }

    @Mod.EventHandler
    public void PostLoad(FMLPostInitializationEvent PostEvent) {
        MainLoader.postLoad();
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent pEvent) {
        pEvent.registerServerCommand(new ConvertInteger());
        pEvent.registerServerCommand(new ConvertFloat());
        pEvent.registerServerCommand(new ListEM());
        if(DEBUG_MODE) {
            pEvent.registerServerCommand(new GiveEM());
        }
    }

    @Mod.EventHandler
    public void onServerStarting(FMLServerStartingEvent aEvent) {
        chunkDataHandler.onServerStarting();
    }
}
