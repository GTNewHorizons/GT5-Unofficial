package tectech;

import net.minecraftforge.common.MinecraftForge;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import eu.usrv.yamcore.auxiliary.LogHelper;
import gregtech.api.enums.Mods;
import gregtech.api.objects.XSTR;
import tectech.loader.ConfigHandler;
import tectech.loader.MainLoader;
import tectech.loader.gui.CreativeTabTecTech;
import tectech.loader.thing.MuTeLoader;
import tectech.mechanics.enderStorage.EnderWorldSavedData;
import tectech.proxy.CommonProxy;
import tectech.recipe.EyeOfHarmonyRecipeStorage;
import tectech.recipe.TecTechRecipeMaps;

@Mod(
    modid = Reference.MODID,
    name = Reference.NAME,
    version = Reference.VERSION,
    guiFactory = "tectech.loader.gui.TecTechGUIFactory",
    dependencies = "required-after:Forge@[10.13.4.1614,);" + "required-after:YAMCore@[0.5.70,);"
        + "required-after:structurelib;"
        + "after:ComputerCraft;"
        + "after:OpenComputers;"
        + "required-after:gtneioreplugin;"
        + "required-after:gregtech;"
        + "after:dreamcraft;"
        + "after:appliedenergistics2;"
        + "after:CoFHCore;"
        + "after:Thaumcraft;")
public class TecTech {

    static {
        try {
            ConfigurationManager.registerConfig(ConfigHandler.class);
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }
    @SidedProxy(clientSide = Reference.CLIENTSIDE, serverSide = Reference.SERVERSIDE)
    public static CommonProxy proxy;

    @Mod.Instance(Reference.MODID)
    public static TecTech instance;

    public static final XSTR RANDOM = XSTR.XSTR_INSTANCE;
    public static final LogHelper LOGGER = new LogHelper(Reference.MODID);
    public static CreativeTabTecTech creativeTabTecTech;

    public static EnderWorldSavedData enderWorldSavedData;

    /**
     * For Loader.isModLoaded checks during the runtime
     */
    public static boolean hasCOFH = false;

    public static final byte tectechTexturePage1 = 8;

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void PreLoad(FMLPreInitializationEvent PreEvent) {
        LOGGER.setDebugOutput(true);

        enderWorldSavedData = new EnderWorldSavedData();
        FMLCommonHandler.instance()
            .bus()
            .register(enderWorldSavedData);
        MinecraftForge.EVENT_BUS.register(enderWorldSavedData);
        TecTechEventHandlers.init();

        TecTechRecipeMaps.init();
        MainLoader.preLoad();
        new MuTeLoader().run();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void Load(FMLInitializationEvent event) {
        hasCOFH = Mods.COFHCore.isModLoaded();

        MainLoader.load();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void PostLoad(FMLPostInitializationEvent PostEvent) {
        MainLoader.postLoad();
    }

    @Mod.EventHandler
    @SuppressWarnings("unused")
    public void onLoadCompleted(FMLLoadCompleteEvent event) {
        eyeOfHarmonyRecipeStorage = new EyeOfHarmonyRecipeStorage();
        MainLoader.onLoadCompleted();
    }

    public static EyeOfHarmonyRecipeStorage eyeOfHarmonyRecipeStorage = null;

}
