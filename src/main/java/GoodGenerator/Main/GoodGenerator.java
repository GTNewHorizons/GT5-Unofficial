package GoodGenerator.Main;

import GoodGenerator.Common.CommonProxy;
import GoodGenerator.Loader.Loaders;
import GoodGenerator.Tabs.MyTabs;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.creativetab.CreativeTabs;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;

@SuppressWarnings("ALL")
@Mod(modid = GoodGenerator.MOD_ID, version = GoodGenerator.VERSION)
public final class GoodGenerator {
    public static final String MOD_ID = "GoodGenerator";
    public static final String VERSION = "1.0";

    public static final CreativeTabs GG = new MyTabs("Good Generator");

    @SidedProxy(clientSide = "GoodGenerator.Client.ClientProxy",serverSide = "GoodGenerator.Common.CommonProxy")
    public static CommonProxy proxy;

    @Mod.Instance(GoodGenerator.MOD_ID)
    public static GoodGenerator instance;

    @Mod.EventHandler
    public static void preInit(FMLPreInitializationEvent event){
        proxy.preInit(event);
    }
    @Mod.EventHandler
    public static void init(FMLInitializationEvent event){
        proxy.init(event);
        Loaders.Register();
    }
    @Mod.EventHandler
    public static void postInit(FMLPostInitializationEvent event){
        proxy.postInit(event);
    }


}
