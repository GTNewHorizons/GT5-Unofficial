package kubatech.api.eig;

import static kubatech.kubatech.error;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import gregtech.api.util.MultiblockTooltipBuilder;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;

public abstract class EIGMode {

    public abstract int getUIIndex();

    public abstract String getName();

    public abstract int getMinVoltageTier();

    public abstract int getMinGlassTier();

    public abstract int getStartingSlotCount();

    public abstract int getSlotPerTierMultiplier();

    public abstract int getSlotCount(int machineTier);

    public abstract int getSeedCapacityPerSlot();

    public abstract int getWeedEXMultiplier();

    public abstract int getMaxFertilizerUsagePerSeed();

    public abstract double getFertilizerBoost();

    public abstract MultiblockTooltipBuilder addTooltipInfo(MultiblockTooltipBuilder builder);

    /**
     * Used to resolve factory type to an identifier.
     */
    private final HashMap<String, IEIGBucketFactory> factories;
    /**
     * A way to have other mods submit custom buckets that can be prioritized over our default buckets
     */
    private final LinkedList<IEIGBucketFactory> orderedFactories;

    public EIGMode() {
        this.factories = new HashMap<>();
        this.orderedFactories = new LinkedList<>();
    }

    /**
     * Adds a bucket factory to the EIG mode and gives it a low priority. Factories with using existing IDs will
     * overwrite each other.
     *
     * @param factory The bucket factory to add.
     */
    public void addLowPriorityFactory(IEIGBucketFactory factory) {
        String factoryId = factory.getNBTIdentifier();
        dealWithDuplicateFactoryId(factoryId);
        // add factory as lowest priority
        this.factories.put(factoryId, factory);
        this.orderedFactories.addLast(factory);
    }

    /**
     * Adds a bucket factory to the EIG mode and gives it a high priority. Factories with using existing IDs will
     * overwrite each other.
     *
     * @param factory The bucket factory to add.
     */
    public void addHighPriorityFactory(IEIGBucketFactory factory) {
        String factoryId = factory.getNBTIdentifier();
        dealWithDuplicateFactoryId(factoryId);
        // add factory as lowest priority
        this.factories.put(factoryId, factory);
        this.orderedFactories.addFirst(factory);
    }

    /**
     * A standardized way to deal with duplicate factory type identifiers.
     *
     * @param factoryId The ID of the factory
     */
    private void dealWithDuplicateFactoryId(String factoryId) {
        if (this.factories.containsKey(factoryId)) {
            // TODO: Check with devs to see if they want a throw instead.
            error("Duplicate EIG bucket index detected!!!: " + factoryId);
            // remove duplicate from ordered list
            this.orderedFactories.remove(this.factories.get(factoryId));
        }
    }

    /**
     * Attempts to create a new bucket from a given item. Returns if the item cannot be inserted into the EIG.
     *
     * @see IEIGBucketFactory#tryCreateBucket(MTEExtremeIndustrialGreenhouse, ItemStack)
     * @param greenhouse The {@link MTEExtremeIndustrialGreenhouse} that will contain the seed.
     * @param input      The {@link ItemStack} for the input item.
     * @param maxConsume The maximum amount of items to consume.
     * @param simulate   Whether to actually consume the seed.
     * @return Null if no bucket could be created from the item.
     */
    public EIGBucket tryCreateNewBucket(MTEExtremeIndustrialGreenhouse greenhouse, ItemStack input, int maxConsume,
        boolean simulate) {
        // Validate inputs
        if (input == null) return null;
        maxConsume = Math.min(input.stackSize, maxConsume);
        if (maxConsume <= 0) return null;
        for (IEIGBucketFactory factory : this.orderedFactories) {
            EIGBucket bucket = factory.tryCreateBucket(greenhouse, input);
            if (bucket == null || !bucket.isValid()) continue;
            if (!simulate) input.stackSize--;
            maxConsume--;
            bucket.tryAddSeed(greenhouse, input, maxConsume, simulate);
            return bucket;
        }
        return null;
    }

    /**
     * Restores the buckets of an EIG for the given mode.
     *
     * @see IEIGBucketFactory#restore(NBTTagCompound)
     * @param bucketNBTList The
     */
    public void restoreBuckets(NBTTagList bucketNBTList, List<EIGBucket> loadTo) {
        for (int i = 0; i < bucketNBTList.tagCount(); i++) {
            // validate nbt
            NBTTagCompound bucketNBT = bucketNBTList.getCompoundTagAt(i);
            if (bucketNBT.hasNoTags()) {
                error("Empty nbt bucket found in EIG nbt.");
                continue;
            }
            if (!bucketNBT.hasKey("type", 8)) {
                error("Failed to identify bucket type in EIG nbt.");
                continue;
            }
            // identify bucket type
            String bucketType = bucketNBT.getString("type");
            IEIGBucketFactory factory = factories.getOrDefault(bucketType, null);
            if (factory == null) {
                error("failed to find EIG bucket factory for type: " + bucketType);
                continue;
            }
            // restore bucket
            loadTo.add(factory.restore(bucketNBT));
        }
    }
}
