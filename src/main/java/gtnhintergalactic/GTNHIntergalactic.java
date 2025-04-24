package gtnhintergalactic;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.GT_Version;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Mods;
import gtnhintergalactic.config.IGConfig;
import gtnhintergalactic.item.IGItems;
import gtnhintergalactic.proxy.CommonProxy;

@Mod(
    modid = GTNHIntergalactic.MODID,
    version = GT_Version.VERSION,
    name = GTNHIntergalactic.MODNAME,
    acceptedMinecraftVersions = "[1.7.10]",
    dependencies = "required-after:GalacticraftCore@[3.0.36,);" + "required-after:GalacticraftMars;"
        + "required-after:gregtech;"
        + "required-after:gtnhlib@[0.5.21,);"
        + "required-after:tectech;"
        + "required-after:structurelib;"
        + "after:GalaxySpace;"
        + "after:bartworks;"
        + "after:GoodGenerator;"
        + "after:miscutils;"
        + "after:dreamcraft;"
        + "after:openmodularturrets;"
        + "after:IronChest;")
public class GTNHIntergalactic {

    static {
        try {
            ConfigurationManager.registerConfig(IGConfig.class);
        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String MODID = Mods.Names.G_T_N_H_INTERGALACTIC;
    public static final String MODNAME = "GTNH-Intergalactic";
    /** Logger used by this mod */
    public static final Logger LOG = LogManager.getLogger(MODID);
    /** Prefix for assets */
    public static final String ASSET_PREFIX = MODID;
    /** Creative tab for mod items */
    public static CreativeTabs tab;
    /** Proxy used for loading */
    @SidedProxy(clientSide = "gtnhintergalactic.proxy.ClientProxy", serverSide = "gtnhintergalactic.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        tab = new CreativeTabs(CreativeTabs.getNextID(), GTNHIntergalactic.MODNAME) {

            @Override
            public Item getTabIconItem() {
                return ItemList.SpaceElevatorController.getItem();
            }

            @Override
            public int func_151243_f() {
                return ItemList.SpaceElevatorController.get(1)
                    .getItemDamage();
            }
        };
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void onMissingMapping(FMLMissingMappingsEvent event) {
        for (FMLMissingMappingsEvent.MissingMapping mapping : event.getAll()) {
            if (mapping.type == GameRegistry.Type.BLOCK) {
                switch (mapping.name) {
                    case "GalaxySpace:spaceelevatorparts" -> mapping.remap(GregTechAPI.sBlockCasingsSE);
                    case "GalaxySpace:spaceelevatormotors" -> mapping.remap(GregTechAPI.sBlockCasingsSEMotor);
                    case "GalaxySpace:spaceelevatorcable" -> mapping.remap(GregTechAPI.sSpaceElevatorCable);
                    case "GalaxySpace:dysonswarmparts" -> mapping.remap(GregTechAPI.sBlockCasingsDyson);
                    case "GalaxySpace:machineframes" -> mapping.remap(GregTechAPI.sBlockCasingsSiphon);
                }
            } else if (mapping.type == GameRegistry.Type.ITEM) {
                switch (mapping.name) {
                    case "GalaxySpace:spaceelevatorparts" -> mapping
                        .remap(Item.getItemFromBlock(GregTechAPI.sBlockCasingsSE));
                    case "GalaxySpace:spaceelevatormotors" -> mapping
                        .remap(Item.getItemFromBlock(GregTechAPI.sBlockCasingsSEMotor));
                    case "GalaxySpace:spaceelevatorcable" -> mapping
                        .remap(Item.getItemFromBlock(GregTechAPI.sSpaceElevatorCable));
                    case "GalaxySpace:dysonswarmparts" -> mapping
                        .remap(Item.getItemFromBlock(GregTechAPI.sBlockCasingsDyson));
                    case "GalaxySpace:machineframes" -> mapping
                        .remap(Item.getItemFromBlock(GregTechAPI.sBlockCasingsSiphon));
                    case "GalaxySpace:item.SpaceElevatorParts" -> mapping.remap(IGItems.SpaceElevatorItems);
                    case "GalaxySpace:item.MiningDrone" -> mapping.remap(IGItems.MiningDrones);
                    case "GalaxySpace:item.DysonSwarmParts" -> mapping.remap(IGItems.DysonSwarmItems);
                }
            }
        }
    }
}
