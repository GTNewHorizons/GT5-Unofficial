package gtnhintergalactic.item;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

/**
 * List of all items of this mod
 *
 * @author minecraft7771
 */
public class IGItems {

    /**
     * Initialize the items of this mod
     */
    public static void init() {
        registerItem(new ItemSpaceElevatorParts());
        registerItem(new ItemMiningDrones());
        registerItem(new ItemDysonSwarmParts());
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
