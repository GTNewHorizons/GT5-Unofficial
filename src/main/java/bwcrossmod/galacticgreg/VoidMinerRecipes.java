package bwcrossmod.galacticgreg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import galacticgreg.api.ModDimensionDef;
import galacticgreg.api.enums.DimensionDef;
import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import gtneioreplugin.plugin.block.ModBlocks;
import gtneioreplugin.util.DimensionHelper;

/**
 * One NEI recipe per void-mineable dimension: the planet marker as input, every ore the Void Miner can produce there as
 * output with its chance of being mined as the output chance; {@link Weights} keeps the exact weights.
 */
public final class VoidMinerRecipes {

    private VoidMinerRecipes() {}

    public static final class Weights {

        private final Map<GTUtility.ItemId, Float> weights;
        private final float totalWeight;

        private Weights(Map<GTUtility.ItemId, Float> weights, float totalWeight) {
            this.weights = weights;
            this.totalWeight = totalWeight;
        }

        public float getTotalWeight() {
            return totalWeight;
        }

        public float getWeight(ItemStack ore) {
            return weights.getOrDefault(GTUtility.ItemId.createNoCopy(ore), 0f);
        }
    }

    public static void register() {
        for (String dimAbbreviation : DimensionHelper.getAllDisplayedNames()) {
            Block marker = ModBlocks.blocks.get(dimAbbreviation);
            if (marker == null) continue;

            ModDimensionDef dimensionDef = DimensionDef.getDefByName(DimensionHelper.getFullName(dimAbbreviation));
            if (dimensionDef == null || !dimensionDef.canBeVoidMined()) continue;

            Weights weights = collectWeights(dimensionDef.getDimensionName());
            if (weights == null) continue;

            ItemStack markerStack = new ItemStack(marker);
            markerStack.stackSize = 0;

            List<ItemStack> ores = new ArrayList<>(weights.weights.size());
            List<Integer> chances = new ArrayList<>(weights.weights.size());
            weights.weights.entrySet()
                .stream()
                .sorted(
                    Map.Entry.<GTUtility.ItemId, Float>comparingByValue(Comparator.reverseOrder())
                        .thenComparingInt(
                            entry -> Item.getIdFromItem(
                                entry.getKey()
                                    .item()))
                        .thenComparingInt(
                            entry -> entry.getKey()
                                .metaData()))
                .forEach(entry -> {
                    ItemStack ore = entry.getKey()
                        .getItemStack();
                    ores.add(ore);
                    // In units of 0.01%
                    chances.add(Math.max(1, Math.round(10_000 * entry.getValue() / weights.totalWeight)));
                });

            GTValues.RA.stdBuilder()
                .itemInputs(markerStack)
                .itemOutputs(ores.toArray(new ItemStack[0]))
                .outputChances(
                    chances.stream()
                        .mapToInt(Integer::intValue)
                        .toArray())
                .duration(1)
                .eut(0)
                .special(weights)
                .addTo(RecipeMaps.voidMinerRecipes);
        }
    }

    // Like DropMap#isDistributionCached, but without merging the extra drops into the shared drop map
    @Nullable
    private static Weights collectWeights(String dimName) {
        VoidMinerUtility.DropMap dropMap = VoidMinerUtility.dropMapsByDimName.get(dimName);
        VoidMinerUtility.DropMap extraDropMap = VoidMinerUtility.extraDropsByDimName.get(dimName);

        Map<GTUtility.ItemId, Float> weights = new HashMap<>();
        float totalWeight = 0;
        for (VoidMinerUtility.DropMap map : new VoidMinerUtility.DropMap[] { dropMap, extraDropMap }) {
            if (map == null) continue;
            for (Map.Entry<GTUtility.ItemId, Float> entry : map.getInternalMap()
                .entrySet()) {
                if (!(entry.getValue() > 0)) continue;
                weights.merge(entry.getKey(), entry.getValue(), Float::sum);
                totalWeight += entry.getValue();
            }
        }

        if (weights.isEmpty()) return null;
        return new Weights(Collections.unmodifiableMap(weights), totalWeight);
    }
}
