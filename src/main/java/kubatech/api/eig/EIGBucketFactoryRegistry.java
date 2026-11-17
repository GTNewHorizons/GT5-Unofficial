package kubatech.api.eig;

import static kubatech.kubatech.LOG;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;

public final class EIGBucketFactoryRegistry {

    private static final Map<String, IEIGBucketFactory> factories = new HashMap<>();
    private static final List<IEIGBucketFactory> orderedFactories = new LinkedList<>();

    private EIGBucketFactoryRegistry() {}

    public static void addLowPriorityFactory(@NotNull IEIGBucketFactory factory) {
        addFactory(factory, false);
    }

    public static void addHighPriorityFactory(@NotNull IEIGBucketFactory factory) {
        addFactory(factory, true);
    }

    private static void addFactory(@NotNull IEIGBucketFactory factory, boolean highPriority) {
        String factoryId = factory.getNBTIdentifier();
        IEIGBucketFactory previous = factories.put(factoryId, factory);

        if (previous != null) {
            LOG.error("Duplicate EIG bucket index detected: {}", factoryId);
            orderedFactories.remove(previous);
        }

        if (highPriority) {
            orderedFactories.add(0, factory);
        } else {
            orderedFactories.add(factory);
        }
    }

    public static @Nullable EIGBucket tryCreateNewBucket(@NotNull MTEExtremeIndustrialGreenhouse greenhouse,
        @Nullable ItemStack input, int maxConsume, boolean simulate) {

        if (input == null) return null;

        maxConsume = Math.min(input.stackSize, maxConsume);
        if (maxConsume <= 0) return null;

        for (IEIGBucketFactory factory : orderedFactories) {
            EIGBucket bucket = factory.tryCreateBucket(greenhouse, input);
            if (bucket == null || !bucket.isValid()) continue;

            if (!simulate) {
                input.stackSize--;
            }

            bucket.tryAddSeed(greenhouse, input, --maxConsume, simulate);

            return bucket;
        }

        return null;
    }

    public static void restoreBuckets(@NotNull NBTTagList bucketNBTList, @NotNull List<EIGBucket> destination) {

        for (int i = 0; i < bucketNBTList.tagCount(); i++) {
            NBTTagCompound bucketNBT = bucketNBTList.getCompoundTagAt(i);

            if (bucketNBT.hasNoTags()) {
                LOG.error("Empty NBT bucket found in EIG NBT.");
                continue;
            }

            if (!bucketNBT.hasKey("type", 8)) {
                LOG.error("Failed to identify bucket type in EIG NBT.");
                continue;
            }

            String bucketType = bucketNBT.getString("type");
            IEIGBucketFactory factory = factories.get(bucketType);

            if (factory == null) {
                LOG.error("Failed to find EIG bucket factory for type: {}", bucketType);
                continue;
            }

            destination.add(factory.restore(bucketNBT));
        }
    }
}
