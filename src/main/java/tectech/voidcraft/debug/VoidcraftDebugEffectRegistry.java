package tectech.voidcraft.debug;

import java.util.LinkedHashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import tectech.voidcraft.uss.MTEUnstableSolarSystem;

/**
 * Debug item registry: right-clicking a registered item on the UnstableSolarSystem machine applies the
 * item's debug effect to the machine (the item is NOT consumed).
 *
 * <p>
 * The mapping is item → effect, one entry per debug item: registering a new item with its effect adds a new
 * debug tool without touching the machine's right-click handling.
 */
public final class VoidcraftDebugEffectRegistry {

    /**
     * A debug effect applied to the UnstableSolarSystem machine.
     */
    public interface Effect {

        /**
         * Applies the effect to the machine (server side only).
         *
         * @param machine the machine the player right-clicked
         * @param player  the player who right-clicked
         */
        void apply(MTEUnstableSolarSystem machine, EntityPlayer player);
    }

    private static final Map<Item, Effect> EFFECTS = new LinkedHashMap<>();

    private VoidcraftDebugEffectRegistry() {}

    /**
     * Registers a debug item with its effect (replaces any previous effect of the same item).
     *
     * @param item   the debug item (its {@link Item} identity — damage values and NBT are irrelevant)
     * @param effect the effect to apply
     */
    public static void register(Item item, Effect effect) {
        EFFECTS.put(item, effect);
    }

    /**
     * @return the effect registered for the held item, null when the stack is null or its item is not a debug
     *         item
     */
    public static Effect effectFor(ItemStack held) {
        if (held == null) {
            return null;
        }
        return EFFECTS.get(held.getItem());
    }

    /**
     * Clears all registrations (unit tests).
     */
    public static void resetForTests() {
        EFFECTS.clear();
    }
}
