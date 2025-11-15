package kubatech.modularui2;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kubatech.api.implementations.KubaTechGTMultiBlockBase;

// Most of the code is from kubatech.api.eig.EIGBucket
public interface InventoryBucket<MTE extends KubaTechGTMultiBlockBase<MTE>> {

    /**
     * Creates a persistent save of the bucket's current data.
     *
     * @return The nbt data for this bucket.
     */
    NBTTagCompound save();

    /**
     * Gets the display name of the ItemStack in this object.
     *
     * @return The display name of the ItemStack.
     */
    String getDisplayName();

    /**
     * Attempts to add seeds to bucket if the input is compatible
     *
     * @param mte        The mte that the bucket is in.
     * @param input      A stack of an item that may be able to be added to our current bucket.
     * @param maxConsume The maximum amount of seeds to add to this bucket.
     * @param simulate   True if you want to see if you can add more seeds (useful for support item checks)
     * @return number of seeds consumed, 0 for wrong item, -1 if it missed the support items, -2 if you tried to consume
     *         0 or less items;
     */
    int tryAddSeed(@NotNull MTE mte, @Nullable ItemStack input, int maxConsume, boolean simulate);

    /**
     * Attempts to remove a seed from bucket.
     *
     * @param toRemove The maximum amount of items to remove.
     * @return The items that were removed from the bucket. Null if the bucket is empty or toRemove <= 0.
     */

    ItemStack @Nullable [] tryRemoveSeed(int toRemove, boolean simulate);

    /**
     * Returns true if the bucket can output items.
     *
     * @return true if the bucket is valid.
     */
    boolean isValid();
}
