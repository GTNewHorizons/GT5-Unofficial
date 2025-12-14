package kubatech.tileentity.gregtech.multiblock.eigbuckets;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import kubatech.api.eig.EIGBucket;
import kubatech.api.eig.EIGDropTable;
import kubatech.api.eig.IEIGBucketFactory;
import kubatech.kubatech;
import kubatech.tileentity.gregtech.multiblock.MTEExtremeIndustrialGreenhouse;
import thaumcraft.api.aspects.Aspect;
import thaumic.tinkerer.common.core.helper.AspectCropLootManager;
import thaumic.tinkerer.common.item.ItemInfusedSeeds;

public class EIGInfusedSeedBucket extends EIGBucket {

    private static HashMap<Aspect, HashMap<ItemStack, Integer>> lootMap = null;

    @SuppressWarnings("unchecked")
    private static void tryGetLootMap() {
        if (lootMap == null) {
            Field field = null;
            try {
                field = AspectCropLootManager.class.getDeclaredField("lootMap");
                field.setAccessible(true);
                lootMap = (HashMap<Aspect, HashMap<ItemStack, Integer>>) field.get(null);
            } catch (NoSuchFieldException exception) {
                kubatech.error(
                    "EIGInfusedSeedBucket: NoSuchFieldException when trying to query AspectCropLootManager.lootMap");
            } catch (IllegalAccessException exception) {
                kubatech.error(
                    "EIGInfusedSeedBucket: IllegalAccessException when trying to query AspectCropLootManager.lootMap");
            } finally {
                if (field != null) {
                    field.setAccessible(false);
                }
            }

        }
    }

    public final static IEIGBucketFactory factory = new EIGInfusedSeedBucket.Factory();
    private static final String NBT_IDENTIFIER = "TTINKER:INFUSEDSEED";
    private static final int REVISION_NUMBER = 0;

    public static class Factory implements IEIGBucketFactory {

        @Override
        public String getNBTIdentifier() {
            return NBT_IDENTIFIER;
        }

        @Override
        public EIGBucket tryCreateBucket(MTEExtremeIndustrialGreenhouse greenhouse, ItemStack input) {
            if (!(input.getItem() instanceof ItemInfusedSeeds)) return null;
            return new EIGInfusedSeedBucket(input, 1);
        }

        @Override
        public EIGBucket restore(NBTTagCompound nbt) {
            return new EIGInfusedSeedBucket(nbt);
        }

    }

    private final Random random = new Random();

    public EIGInfusedSeedBucket(ItemStack seed, int seedCount) {
        super(seed, seedCount, null);
    }

    public EIGInfusedSeedBucket(NBTTagCompound nbt) {
        super(nbt);
    }

    @Override
    public boolean revalidate(MTEExtremeIndustrialGreenhouse greenhouse) {
        return this.isValid();
    }

    @Override
    protected String getNBTIdentifier() {
        return NBT_IDENTIFIER;
    }

    @Override
    public void addProgress(double multiplier, EIGDropTable tracker) {
        if (!this.isValid()) return;

        tryGetLootMap();
        if (lootMap == null) return;

        Aspect aspect = ItemInfusedSeeds.getAspect(this.seed);
        if (aspect == null) return;

        HashMap<ItemStack, Integer> aspectDrops = lootMap.get(aspect);
        if (aspectDrops == null || aspectDrops.size() == 0) return; // no drops

        int totalDrop = this.seedCount;
        int ordoTendency = ItemInfusedSeeds.getAspectTendencies(this.seed)
            .getAmount(Aspect.ORDER);
        if (ordoTendency > 0) {
            double addNewDropChance = (double) ordoTendency / 75;
            double stopNewDropChance = 1.0 - addNewDropChance;

            double expectedAdditionalPerSeed = (stopNewDropChance > 0) ? addNewDropChance / stopNewDropChance : 0;
            double variancePerSeed = (stopNewDropChance > 0)
                ? addNewDropChance / (stopNewDropChance * stopNewDropChance)
                : 0;

            double averageAdditionalDrops = this.seedCount * expectedAdditionalPerSeed;
            double standardDeviationForAdditionalDrops = Math.sqrt(this.seedCount * variancePerSeed);
            int additionalDrops = Math.max(
                0,
                (int) Math.round(averageAdditionalDrops + standardDeviationForAdditionalDrops * random.nextGaussian()));
            totalDrop += additionalDrops;
        }

        if (aspectDrops.size() == 1) {
            // fast path, get first ItemStack of HashMap (as it's the only one)
            ItemStack item = aspectDrops.keySet()
                .iterator()
                .next();
            tracker.addDrop(item.copy(), multiplier * totalDrop * item.stackSize);
            return;
        }

        // 2+ drops from aspect, uses weights

        int lowestWeight = Integer.MAX_VALUE;
        int totalWeight = 0;
        for (Integer weight : aspectDrops.values()) {
            lowestWeight = Math.min(lowestWeight, weight);
            totalWeight += weight;
        }

        double chanceOfTheRarest = lowestWeight / totalWeight;
        // > than 1 of the rarest per average AND calculation is not too expensive
        // OR too cheap for approximation
        if ((chanceOfTheRarest * totalDrop > 1 && totalDrop <= 128) || totalDrop < Math.max(16, aspectDrops.size())) {
            // slow approach, but if `totalDrop` is small enough
            // then it's better to simulate this way for accuracy

            HashMap<ItemStack, Integer> accumulated = new HashMap<>();
            for (int i = 0; i < totalDrop; i++) {
                int roll = random.nextInt(totalWeight);
                for (Map.Entry<ItemStack, Integer> kv : aspectDrops.entrySet()) {
                    int weight = kv.getValue();
                    roll -= weight;
                    if (roll > 0) continue;

                    ItemStack item = kv.getKey();
                    accumulated.merge(item, item.stackSize, Integer::sum);
                    break;
                }
            }

            for (Map.Entry<ItemStack, Integer> kv : accumulated.entrySet()) {
                tracker.addDrop(kv.getKey(), multiplier * kv.getValue());
            }
            return;
        }

        int remainingDrops = totalDrop;
        int remainingWeight = totalWeight;
        Iterator<Map.Entry<ItemStack, Integer>> it = aspectDrops.entrySet()
            .iterator();
        while (it.hasNext()) {
            Map.Entry<ItemStack, Integer> entry = it.next();
            ItemStack item = entry.getKey();
            int weight = entry.getValue();
            if (weight == 0) continue;
            if (!it.hasNext()) { // last one
                if (remainingDrops > 0) {
                    tracker.addDrop(item, multiplier * remainingDrops * item.stackSize);
                }
                break;
            }
            double relativeChance = (double) weight / remainingWeight;
            double averageDrops = remainingDrops * relativeChance;
            double standardDeviation = Math.sqrt(remainingDrops * relativeChance * (1 - relativeChance));
            int drops = (int) Math.round(averageDrops + standardDeviation * random.nextGaussian());
            // Math.clamp is introduced in java21 (idk if JABEL fixes it)
            drops = Math.max(0, Math.min(drops, remainingDrops));
            if (drops > 0) {
                tracker.addDrop(item, multiplier * drops * item.stackSize);
            }
            remainingDrops -= drops;
            remainingWeight -= weight;
            if (remainingDrops <= 0) break;
        }
    }

}
