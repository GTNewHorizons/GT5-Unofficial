package com.minecraft7771.gtnhintergalactic.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class IGItems {
    public static Item SpaceElevatorItems;
    public static Item MiningDrones;
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
    public static ItemStack SpaceElevatorModuleManager;
    public static ItemStack SpaceElevatorModuleResearch;

    public static void init() {
        SpaceElevatorItems = new ItemSpaceElevatorParts();
        MiningDrones = new ItemMiningDrones();
        registerItem(SpaceElevatorItems);
        registerItem(MiningDrones);
    }

    private static void registerItem(final Item item) {
        GameRegistry.registerItem(item, item.getUnlocalizedName());
    }
}
