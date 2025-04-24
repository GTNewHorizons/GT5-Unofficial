package gtnhintergalactic.item;

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
    public static Item DysonSwarmItems;
    public static Item MiningDrones;
    public static ItemStack SpaceElevatorCasing0;
    public static ItemStack SpaceElevatorCasing1;
    public static ItemStack SpaceElevatorCasing2;
    public static ItemStack SpaceElevatorMotorT1;
    public static ItemStack SpaceElevatorMotorT2;
    public static ItemStack SpaceElevatorMotorT3;
    public static ItemStack SpaceElevatorMotorT4;
    public static ItemStack SpaceElevatorMotorT5;

    /**
     * Initialize the items of this mod
     */
    public static void init() {
        SpaceElevatorItems = new ItemSpaceElevatorParts();
        MiningDrones = new ItemMiningDrones();
        DysonSwarmItems = new ItemDysonSwarmParts();
        registerItem(SpaceElevatorItems);
        registerItem(MiningDrones);
        registerItem(DysonSwarmItems);
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
