package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.storage.data.IAEItemStack;
import appeng.util.item.AEItemStack;
import it.unimi.dsi.fastutil.Pair;

/**
 * Something that can accept and provide items/fluids.
 */
public interface IPseudoInventory {

    /** Items will not actually be consumed. */
    public static final int CONSUME_SIMULATED = 0b1;
    /** Items will be fuzzily-matched. Ignores NBT and ignores damage for items without subitems */
    public static final int CONSUME_FUZZY = 0b10;
    /** Not all items must be extracted. */
    public static final int CONSUME_PARTIAL = 0b100;
    /** Creative mode infinite supply will be ignored, but not 111 stacks. */
    public static final int CONSUME_IGNORE_CREATIVE = 0b1000;

    /**
     * Atomically extracts items from this pseudo inventory.
     * 
     * The returned list is guaranteed to at minimum be equal to the items param.
     * If the extraction succeeded and partial mode wasn't enabled, extraneous items will not be extracted and the
     * returned list will contain the same items as the request.
     * If fuzzy mode is enabled there may be several stacks with different tags (and damages where relevant), but every
     * stackable item will be merged into the same IAEItemStack.
     * 
     * @param items The list of items to extract.
     * @param flags The flags (see {@link IPseudoInventory#CONSUME_SIMULATED}, {@link IPseudoInventory#CONSUME_FUZZY},
     *              etc).
     * @return Key = whether the extract was successful. Value = the list of items extracted (only relevant for fuzzy
     *         mode).
     */
    public Pair<Boolean, List<IAEItemStack>> tryConsumeItems(List<IAEItemStack> items, int flags);

    /**
     * Consumes a set of items.
     * 
     * @return True when the items were successfully consumed.
     */
    public default boolean tryConsumeItems(ItemStack... items) {
        List<IAEItemStack> stacks = new ArrayList<>(items.length);
        for (int i = 0; i < items.length; i++) stacks.add(AEItemStack.create(items[i]));
        return tryConsumeItems(stacks, 0).first();
    }

    public void givePlayerItems(ItemStack... items);

    public void givePlayerFluids(FluidStack... fluids);
}
