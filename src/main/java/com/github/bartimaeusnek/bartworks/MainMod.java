package com.github.bartimaeusnek.bartworks;


import com.github.bartimaeusnek.bartworks.client.creativetabs.GT2Tab;
import com.github.bartimaeusnek.bartworks.client.creativetabs.bartworksTab;
import com.github.bartimaeusnek.bartworks.common.ConfigHandler;
import com.github.bartimaeusnek.bartworks.common.blocks.BW_TileEntityContainer;
import com.github.bartimaeusnek.bartworks.common.loaders.LoaderRegistry;
import com.github.bartimaeusnek.bartworks.common.tileentities.BW_RotorBlock;
import com.github.bartimaeusnek.bartworks.common.tileentities.GT_TileEntity_Windmill;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
        modid = MainMod.modID, name = MainMod.name, version = MainMod.version,
        dependencies =    "required-after:IC2; "
                        + "required-after:gregtech; "
    )
public final class MainMod {
    public static final String name = "BartWorks";
    public static final String version = "@version@";
    public static final String modID = "bartworks";
    public static final Logger logger = LogManager.getLogger(name);
    public static boolean GTNH  = false;
    public static final CreativeTabs GT2 = new GT2Tab("GT2C");
    public static final CreativeTabs BWT = new bartworksTab("bartworks");
    public static final IGuiHandler GH = new GuiHandler();

    @Mod.Instance(modID)
    public static MainMod instance;
    public static ConfigHandler CHandler;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent preinit) {
        if(Loader.isModLoaded("dreamcraft")) {
            GTNH = true;
        }
        CHandler= new ConfigHandler(preinit);
        if (GTNH)
            logger.info("GTNH-Detected . . . ACTIVATE HARDMODE.");
    }
    @Mod.EventHandler
    public void init(FMLInitializationEvent init) {
       new LoaderRegistry().run();
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent postinit) {
        NetworkRegistry.INSTANCE.registerGuiHandler(instance,GH);
    }



}
