package kubatech.api.eig;

import static kubatech.api.utils.ItemUtils.readItemStackFromNBT;
import static kubatech.api.utils.ItemUtils.writeItemStackToNBT;

import java.util.LinkedList;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;

import gregtech.api.util.GTUtility;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;

public abstract class EIGBucket {

    protected ItemStack seed;
    protected int seedCount;
    protected ItemStack[] supportItems;

    public EIGBucket(ItemStack seed, int seedCount, ItemStack[] supportItem) {
        this.seed = seed.copy();
        this.seed.stackSize = 1;
        this.seedCount = seedCount;
        this.supportItems = supportItem;
    }

    public EIGBucket(NBTTagCompound nbt) {
        this.seed = readItemStackFromNBT(nbt.getCompoundTag("seed"));
        this.seedCount = nbt.getInteger("count");

        // parse support items
        if (nbt.hasKey("supportItems", 9)) {
            NBTTagList supportItemsNBTList = nbt.getTagList("supportItems", 10);
            if (supportItemsNBTList.tagCount() > 0) {
                this.supportItems = new ItemStack[supportItemsNBTList.tagCount()];
                for (int i = 0; i < supportItemsNBTList.tagCount(); i++) {
                    this.supportItems[i] = readItemStackFromNBT(supportItemsNBTList.getCompoundTagAt(i));
                }
            } else {
                supportItems = null;
            }
        } else {
            supportItems = null;
        }
    }

    /**
     * Creates a persistent save of the bucket's current data.
     *
     * @return The nbt data for this bucket.
     */
    public NBTTagCompound save() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString("type", this.getNBTIdentifier());
        nbt.setTag("seed", writeItemStackToNBT(this.seed));
        nbt.setInteger("count", this.seedCount);
        if (this.supportItems != null && this.supportItems.length > 0) {
            NBTTagList supportItemNBT = new NBTTagList();
            for (ItemStack supportItem : this.supportItems) {
                supportItemNBT.appendTag(writeItemStackToNBT(supportItem));
            }
            nbt.setTag("supportItems", supportItemNBT);
        }
        return nbt;
    }

    /**
     * Gets an item stack representing the seeds in this bucket
     *
     * @return an item stack representing the seeds in this bucket.
     */
    public ItemStack getSeedStack() {
        ItemStack copied = this.seed.copy();
        copied.stackSize = this.seedCount;
        return copied;
    }

    /**
     * Gets the number of seeds in this bucket
     *
     * @return gets the number of seeds in this bucket.
     */
    public int getSeedCount() {
        return this.seedCount;
    }

    /**
     * Gets the display name of the seed in this bucket
     *
     * @return The display name of the seed.
     */
    public String getDisplayName() {
        return this.seed.getDisplayName();
    }

    public String getInfoData() {
        StringBuilder sb = new StringBuilder();
        // display invalid buckets, we don't want people to think they lost their seeds or something.
        sb.append(this.isValid() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED);
        sb.append("x");
        sb.append(this.getSeedCount());
        sb.append(" ");
        sb.append(this.getDisplayName());
        this.getAdditionalInfoData(sb);
        sb.append(EnumChatFormatting.RESET);
        return sb.toString();
    }

    protected void getAdditionalInfoData(StringBuilder sb) {}

    /**
     * Attempts to add seeds to tbe bucket if the input is compatible
     *
     * @param input      A stack of an item that may be able to be added to our current bucket.
     * @param maxConsume The maximum amount of seeds to add to this bucket.
     * @param simulate   True if you want to see if you can add more seeds (useful for support item checks)
     * @return number of seeds consumed, 0 for wrong item, -1 if it missed the support items, -2 if you tried to consume
     *         0 or less items;
     */
    public int tryAddSeed(MTEExtremeIndustrialGreenhouse greenhouse, ItemStack input, int maxConsume,
        boolean simulate) {
        // Abort is input if empty
        if (input == null || input.stackSize <= 0) return -2;
        // Cap max to input count
        maxConsume = Math.min(maxConsume, input.stackSize);
        // Abort if item isn't an identical seed.
        if (!GTUtility.areStacksEqual(this.seed, input, false)) return 0;

        // no support items, consume and exit early.
        if (this.supportItems == null || this.supportItems.length <= 0) {
            if (!simulate) {
                input.stackSize -= maxConsume;
                this.seedCount += maxConsume;
            }
            return maxConsume;
        }

        // Check if the item is found
        LinkedList<ItemStack> toConsumeFrom = new LinkedList<>();
        supportLoop: for (ItemStack supportItem : this.supportItems) {
            for (ItemStack otherInput : greenhouse.getStoredInputs()) {
                // filter usable inputs
                if (otherInput == null || otherInput.stackSize <= 0) continue;
                if (!GTUtility.areStacksEqual(supportItem, otherInput, false)) continue;
                // update max consume again
                maxConsume = Math.min(maxConsume, otherInput.stackSize);
                toConsumeFrom.addLast(otherInput);
                continue supportLoop;
            }
            // no support found, no seeds added
            return -1;
        }

        // consume items
        if (!simulate) {
            input.stackSize -= maxConsume;
            for (ItemStack stack : toConsumeFrom) {
                stack.stackSize -= maxConsume;
            }
            this.seedCount += maxConsume;
        }
        return maxConsume;
    }

    /**
     * Attempts to remove a seed from the bucket
     *
     * @param toRemove The maximum amount of items to remove.
     * @return The items that were removed from the bucket. Null if the bucket is empty.
     */
    public ItemStack[] tryRemoveSeed(int toRemove, boolean simulate) {
        // validate inputs
        toRemove = Math.min(this.seedCount, toRemove);
        if (toRemove <= 0) return null;

        // consume and return output
        ItemStack[] ret = new ItemStack[1 + (this.supportItems == null ? 0 : this.supportItems.length)];
        ret[0] = this.seed.copy();
        ret[0].stackSize = toRemove;
        if (this.supportItems != null) {
            for (int i = 0; i < this.supportItems.length; i++) {
                ret[i + 1] = this.supportItems[i].copy();
                ret[i + 1].stackSize = toRemove;
            }
        }
        if (!simulate) {
            this.seedCount -= toRemove;
        }
        return ret;
    }

    /**
     * Sets the seed count to 0 and returns item stacks representing every item in this bucket.
     *
     * @return The contents of the bucket
     */
    public ItemStack[] emptyBucket() {
        if (this.seedCount <= 0) return null;
        ItemStack[] ret = new ItemStack[1 + (this.supportItems == null ? 0 : this.supportItems.length)];
        ret[0] = this.seed.copy();
        ret[0].stackSize = this.seedCount;
        if (this.supportItems != null) {
            for (int i = 0; i < this.supportItems.length; i++) {
                ret[i + 1] = this.supportItems[i].copy();
                ret[i + 1].stackSize = this.seedCount;
            }
        }
        this.seedCount = 0;
        return ret;
    }

    /**
     * Returns true if the bucket can output items.
     *
     * @return true if the bucket is valid.
     */
    public boolean isValid() {
        return this.seed != null && this.seedCount > 0;
    }

    /**
     * Gets the identifier used to identify this class during reconstruction
     *
     * @return the identifier for this bucket type.
     */
    protected abstract String getNBTIdentifier();

    /**
     * Adds item drops to the item tracker.
     *
     * @param multiplier A multiplier to apply to the output.
     * @param tracker    The item drop tracker
     */
    public abstract void addProgress(double multiplier, EIGDropTable tracker);

    /**
     * Attempts to revalidate a seed bucket. If it returns false, attempt to seed and support items and delete the
     * bucket.
     *
     * @param greenhouse The greenhouse that contains the bucket.
     * @return True if the bucket was successfully validated. {@link EIGBucket#isValid()} should also return true.
     */
    public abstract boolean revalidate(MTEExtremeIndustrialGreenhouse greenhouse);

}
