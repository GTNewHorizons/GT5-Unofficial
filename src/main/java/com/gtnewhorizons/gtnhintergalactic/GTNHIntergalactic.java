package com.gtnewhorizons.gtnhintergalactic;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;
import com.gtnewhorizons.gtnhintergalactic.block.IGBlocks;
import com.gtnewhorizons.gtnhintergalactic.config.IGConfig;
import com.gtnewhorizons.gtnhintergalactic.item.IGItems;
import com.gtnewhorizons.gtnhintergalactic.proxy.CommonProxy;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(
        modid = GTNHIntergalactic.MODID,
        version = Tags.VERSION,
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

    public static final String MODID = "gtnhintergalactic";
    public static final String MODNAME = "GTNH-Intergalactic";
    /** Logger used by this mod */
    public static final Logger LOG = LogManager.getLogger(MODID);
    /** Prefix for assets */
    public static final String ASSET_PREFIX = "gtnhintergalactic";
    /** Creative tab for mod items */
    public static CreativeTabs tab;
    /** Proxy used for loading */
    @SidedProxy(
            clientSide = "com.gtnewhorizons.gtnhintergalactic.proxy.ClientProxy",
            serverSide = "com.gtnewhorizons.gtnhintergalactic.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        tab = new CreativeTabs(CreativeTabs.getNextID(), GTNHIntergalactic.MODNAME) {

            @Override
            public Item getTabIconItem() {
                return IGItems.SpaceElevatorController.getItem();
            }

            @Override
            public int func_151243_f() {
                return IGItems.SpaceElevatorController.getItemDamage();
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
                    case "GalaxySpace:spaceelevatorparts" -> mapping.remap(IGBlocks.SpaceElevatorCasing);
                    case "GalaxySpace:spaceelevatormotors" -> mapping.remap(IGBlocks.SpaceElevatorMotor);
                    case "GalaxySpace:spaceelevatorcable" -> mapping.remap(IGBlocks.SpaceElevatorCable);
                    case "GalaxySpace:dysonswarmparts" -> mapping.remap(IGBlocks.DysonSwarmCasing);
                    case "GalaxySpace:machineframes" -> mapping.remap(IGBlocks.GasSiphonCasing);
                }
            } else if (mapping.type == GameRegistry.Type.ITEM) {
                switch (mapping.name) {
                    case "GalaxySpace:spaceelevatorparts" -> mapping
                            .remap(Item.getItemFromBlock(IGBlocks.SpaceElevatorCasing));
                    case "GalaxySpace:spaceelevatormotors" -> mapping
                            .remap(Item.getItemFromBlock(IGBlocks.SpaceElevatorMotor));
                    case "GalaxySpace:spaceelevatorcable" -> mapping
                            .remap(Item.getItemFromBlock(IGBlocks.SpaceElevatorCable));
                    case "GalaxySpace:dysonswarmparts" -> mapping
                            .remap(Item.getItemFromBlock(IGBlocks.DysonSwarmCasing));
                    case "GalaxySpace:machineframes" -> mapping.remap(Item.getItemFromBlock(IGBlocks.GasSiphonCasing));
                    case "GalaxySpace:item.SpaceElevatorParts" -> mapping.remap(IGItems.SpaceElevatorItems);
                    case "GalaxySpace:item.MiningDrone" -> mapping.remap(IGItems.MiningDrones);
                    case "GalaxySpace:item.DysonSwarmParts" -> mapping.remap(IGItems.DysonSwarmItems);
                }
            }
        }
    }
}
