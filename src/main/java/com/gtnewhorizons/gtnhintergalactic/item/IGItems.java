package com.gtnewhorizons.gtnhintergalactic.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * List of all items of this mod
 *
 * @author minecraft7771
 */
public class IGItems {

    public static Item SpaceElevatorItems;
    public static Item MiningDrones;
    public static ItemStack PlanetaryGasSiphon;
    public static ItemStack SpaceElevatorCasing0;
    public static ItemStack SpaceElevatorCasing1;
    public static ItemStack SpaceElevatorCasing2;
    public static ItemStack SpaceElevatorMotorT1;
    public static ItemStack SpaceElevatorMotorT2;
    public static ItemStack SpaceElevatorMotorT3;
    public static ItemStack SpaceElevatorMotorT4;
    public static ItemStack SpaceElevatorMotorT5;
    public static ItemStack SpaceElevatorController;
    public static ItemStack SpaceElevatorModuleAssemblerT1;
    public static ItemStack SpaceElevatorModuleAssemblerT2;
    public static ItemStack SpaceElevatorModuleAssemblerT3;
    public static ItemStack SpaceElevatorModuleMinerT1;
    public static ItemStack SpaceElevatorModuleMinerT2;
    public static ItemStack SpaceElevatorModuleMinerT3;
    public static ItemStack SpaceElevatorModulePumpT1;
    public static ItemStack SpaceElevatorModulePumpT2;
    public static ItemStack SpaceElevatorModulePumpT3;
    public static ItemStack SpaceElevatorModuleManager;
    public static ItemStack SpaceElevatorModuleResearch;

    /**
     * Initialize the items of this mod
     */
    public static void init() {
        SpaceElevatorItems = new ItemSpaceElevatorParts();
        MiningDrones = new ItemMiningDrones();
        registerItem(SpaceElevatorItems);
        registerItem(MiningDrones);
    }

    /**
     * Register an item in the game registry, using its unlocalized name
     *
     * @param item Item to be registered
     */
    private static void registerItem(final Item item) {
        GameRegistry.registerItem(item, item.getUnlocalizedName());
    }
}
