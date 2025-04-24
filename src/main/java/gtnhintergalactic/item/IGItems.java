package gtnhintergalactic.item;

import net.minecraft.item.Item;

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
