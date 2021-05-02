package GoodGenerator.Main;

import GoodGenerator.Common.CommonProxy;
import GoodGenerator.Items.MyMaterial;
import GoodGenerator.Loader.FuelRecipeLoader;
import GoodGenerator.Loader.Loaders;
import GoodGenerator.Loader.RecipeLoader;
import GoodGenerator.Tabs.MyTabs;
import GoodGenerator.Blocks.MyFluids.FluidsBuilder;
import com.github.bartimaeusnek.bartworks.API.WerkstoffAdderRegistry;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;

import static GoodGenerator.Loader.Loaders.addOreDic;

@SuppressWarnings("ALL")
@Mod(modid = GoodGenerator.MOD_ID, version = GoodGenerator.VERSION)
public final class GoodGenerator {
    public static final String MOD_ID = "GoodGenerator";
    public static final String VERSION = "GRADLETOKEN_VERSION";

    public static final CreativeTabs GG = new MyTabs("Good Generator");

    @SidedProxy(clientSide = "GoodGenerator.Client.ClientProxy",serverSide = "GoodGenerator.Common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(GoodGenerator.MOD_ID)
    public static GoodGenerator instance;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event){
        WerkstoffAdderRegistry.addWerkstoffAdder(new MyMaterial());
        new FluidsBuilder();
        Loaders.Register();
        addOreDic();
        proxy.preInit(event);
    }
    @Mod.EventHandler
    public static void init(FMLInitializationEvent event){
        proxy.init(event);
        RecipeLoader.RecipeLoad();
        RecipeLoader.Fixer();
        FuelRecipeLoader.RegisterFuel();
    }
    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
    }

}
