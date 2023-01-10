package com.github.technus.tectech.recipe;

import static com.github.technus.tectech.recipe.EyeOfHarmonyRecipeStorage.BILLION;
import static com.google.common.math.IntMath.pow;
import static gregtech.api.GregTech_API.getUnificatedOreDictStack;
import static gregtech.api.util.GT_Utility.getPlasmaFuelValueInEUPerLiterFromMaterial;
import static java.lang.Math.min;

import com.github.technus.tectech.util.ItemStackLong;
import gnu.trove.map.TMap;
import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;
import gregtech.api.enums.Materials;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;
import pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreSmallHelper;

@SuppressWarnings("SpellCheckingInspection")
public class EyeOfHarmonyRecipe {

    HashingStrategy<ItemStack> itemStackHashingStrategy = new HashingStrategy<ItemStack>() {
        @Override
        public int computeHashCode(ItemStack stack) {
            // Not really sure how this works or if it is "unique enough".
            int result = stack.getItem().hashCode();
            result = 31 * result + stack.getItemDamage();
            return result;
        }

        @Override
        public boolean equals(ItemStack item1, ItemStack item2) {
            return item1.getUnlocalizedName().equals(item2.getUnlocalizedName());
        }
    };

    private final TMap<ItemStack, Double> itemStackToProbabilityMap = new TCustomHashMap<>(itemStackHashingStrategy);
    private final TMap<ItemStack, Long> itemStackToTrueStackSizeMap = new TCustomHashMap<>(itemStackHashingStrategy);

    private final ArrayList<ItemStackLong> outputItems;
    private final ArrayList<FluidStack> outputFluids;

    private final long hydrogenRequirement;
    private final long heliumRequirement;

    private final long euOutput;
    private final long euStartCost;

    private final double baseSuccessChance;

    private final long spacetimeCasingTierRequired;

    private final long miningTimeSeconds;

    private final double recipeEnergyEfficiency;

    private final ItemStack recipeTriggerItem;

    private final long sumOfItems;
    private final long rocketTier;

    public TMap<ItemStack, Double> getItemStackToProbabilityMap() {
        return itemStackToProbabilityMap;
    }

    public TMap<ItemStack, Long> getItemStackToTrueStackSizeMap() {
        return itemStackToTrueStackSizeMap;
    }

    public double getRecipeEnergyEfficiency() {
        return recipeEnergyEfficiency;
    }

    @SuppressWarnings("FieldCanBeLocal")
    private final long standardRecipeEUOutPerTick = 100 * BILLION;

    public long getSumOfItems() {
        return sumOfItems;
    }

    public long getRocketTier() {
        return rocketTier;
    }

    public EyeOfHarmonyRecipe(
            ArrayList<Pair<Materials, Long>> materialList,
            Block block,
            final double recipeEnergyEfficiency,
            final long hydrogenRequirement,
            final long heliumRequirement,
            final long miningTimeSeconds,
            final long rocketTierOfRecipe,
            final double baseSuccessChance) {

        this.rocketTier = rocketTierOfRecipe;
        this.spacetimeCasingTierRequired = min(8, rocketTierOfRecipe);

        this.recipeTriggerItem = new ItemStack(block);

        this.outputItems = validDustGenerator(materialList);
        this.outputItems.sort(Comparator.comparingLong(ItemStackLong::getStackSize));
        Collections.reverse(this.outputItems);

        this.sumOfItems =
                this.outputItems.stream().map(ItemStackLong::getStackSize).reduce(0L, Long::sum);

        for (ItemStackLong itemStackLong : outputItems) {
            double stackSize = (double) itemStackLong.getStackSize();
            double probability = Math.round(100_000 * stackSize / sumOfItems) / 1000.0;

            itemStackToProbabilityMap.put(itemStackLong.itemStack, probability);
            itemStackToTrueStackSizeMap.put(itemStackLong.itemStack, itemStackLong.stackSize);
        }
        // End item processing.

        // --- Output and process fluids of the recipe.
        ArrayList<FluidStack> fluidStackArrayList = validPlasmaGenerator(materialList);

        for (FluidStack fluidStack : fluidStackArrayList) {
            fluidStack.amount = (int) ((this.spacetimeCasingTierRequired + 1) * 1_000_000L);
        }

        // Add a bonus fluid of compressed star matter.
        // todo replace with Bonus star matter when added to GT5.
        fluidStackArrayList.add(Materials.Infinity.getMolten((this.spacetimeCasingTierRequired + 1) * 100_000));

        outputFluids = fluidStackArrayList;
        // End fluid processing.

        this.hydrogenRequirement = hydrogenRequirement;
        this.heliumRequirement = heliumRequirement;

        this.baseSuccessChance = baseSuccessChance;

        this.miningTimeSeconds = miningTimeSeconds;
        this.recipeEnergyEfficiency = recipeEnergyEfficiency;

        long plasmaEU = plasmaCostCalculator(outputFluids);
        long VM3EU = miningTimeSeconds * pow(2, 19) * 20;
        this.euStartCost = (plasmaEU + VM3EU + standardRecipeEUOutPerTick * 20 * miningTimeSeconds);
        this.euOutput = (long) (euStartCost * recipeEnergyEfficiency);
    }

    public EyeOfHarmonyRecipe(
            final GT5OreLayerHelper.NormalOreDimensionWrapper normalOreDimensionWrapper,
            final GT5OreSmallHelper.SmallOreDimensionWrapper smallOreDimensionWrapper,
            final Block block,
            final double recipeEnergyEfficiency,
            final long hydrogenRequirement,
            final long heliumRequirement,
            final long miningTimeSeconds,
            final long spacetimeCasingTierRequired,
            final double baseSuccessChance) {

        // Process recipes output items.
        // 6 * 64 = 6 stacks/second for VM tier 3 + Og gas.
        this(
                processDimension(normalOreDimensionWrapper, smallOreDimensionWrapper, miningTimeSeconds),
                block,
                recipeEnergyEfficiency,
                hydrogenRequirement,
                heliumRequirement,
                miningTimeSeconds,
                spacetimeCasingTierRequired,
                baseSuccessChance);
    }

    // Return clone of list. Deep copy. Maybe a better way to do this?
    public ArrayList<ItemStackLong> getOutputItems() {
        ArrayList<ItemStackLong> copyOutputList = new ArrayList<>();
        for (ItemStackLong itemStackLong : outputItems) {
            copyOutputList.add(new ItemStackLong(itemStackLong));
        }

        return copyOutputList;
    }

    // Deep copy.
    public FluidStack[] getOutputFluids() {
        ArrayList<FluidStack> copyOutputList = new ArrayList<>();

        for (FluidStack fluidStack : outputFluids) {
            copyOutputList.add(fluidStack.copy());
        }

        return copyOutputList.toArray(new FluidStack[0]);
    }

    public long getHydrogenRequirement() {
        return hydrogenRequirement;
    }

    public long getHeliumRequirement() {
        return heliumRequirement;
    }

    public long getEUOutput() {
        return euOutput;
    }

    public long getEUStartCost() {
        return euStartCost;
    }

    public long getRecipeTimeInTicks() {
        return miningTimeSeconds * 20;
    }

    public double getBaseRecipeSuccessChance() {
        return baseSuccessChance;
    }

    public long getSpacetimeCasingTierRequired() {
        return spacetimeCasingTierRequired;
    }

    public ItemStack getRecipeTriggerItem() {
        return recipeTriggerItem;
    }

    private static final double PRIMARY_MULTIPLIER = (0.1 + 1.0 / 9.0); // Byproduct from macerating/washing chance.
    private static final double SECONDARY_MULTIPLIER = (1.0 / 9.0); // Thermal centrifuge byproduct chance.
    private static final double TERTIARY_MULTIPLIER = (0.1); // Macerating thermal centrifuged byproduct chance.
    private static final double QUATERNARY_MULTIPLIER = (0.7); // Mercury/chem bath processing chance.

    private static final double[] ORE_MULTIPLIER = {
        PRIMARY_MULTIPLIER, SECONDARY_MULTIPLIER, TERTIARY_MULTIPLIER, QUATERNARY_MULTIPLIER
    };

    public static class HashMapHelper extends HashMap<Materials, Double> {

        void add(Materials material, double value) {

            // If key already exists.
            if (this.containsKey(material)) {
                this.put(material, value + this.get(material));
                return;
            }

            // Otherwise, add value to hashmap entry.
            this.put(material, value);
        }
    }

    public static void processHelper(
            HashMapHelper outputMap, Materials material, double mainMultiplier, double probability) {
        outputMap.add(material.mDirectSmelting, (material.mOreMultiplier * 2) * mainMultiplier * probability);

        int index = 0;
        for (Materials byProductMaterial : material.mOreByProducts) {
            outputMap.add(
                    byProductMaterial.mDirectSmelting, mainMultiplier * (ORE_MULTIPLIER[index++] * 2) * probability);
        }
    }

    private static ArrayList<Pair<Materials, Long>> processDimension(
            GT5OreLayerHelper.NormalOreDimensionWrapper normalOreDimWrapper,
            GT5OreSmallHelper.SmallOreDimensionWrapper smallOreDimWrapper,
            long timeInSeconds) {
        HashMapHelper outputMap = new HashMapHelper();

        double mainMultiplier = timeInSeconds * 384.0;

        normalOreDimWrapper.oreVeinToProbabilityInDimension.forEach((veinInfo, probability) -> {
            processHelper(outputMap, veinInfo.mPrimaryVeinMaterial, mainMultiplier, probability);
            processHelper(outputMap, veinInfo.mSecondaryMaterial, mainMultiplier, probability);
            // 8.0 to replicate void miner getDropsVanillaVeins method yields.
            processHelper(outputMap, veinInfo.mBetweenMaterial, mainMultiplier / 8.0, probability);
            processHelper(outputMap, veinInfo.mSporadicMaterial, mainMultiplier / 8.0, probability);
        });

        // Iterate over small ores in dimension and add them, kinda hacky but works and is close enough.
        smallOreDimWrapper.oreVeinToProbabilityInDimension.forEach((veinInfo, probability) ->
                processHelper(outputMap, veinInfo.getOreMaterial(), mainMultiplier, probability));

        ArrayList<Pair<Materials, Long>> outputList = new ArrayList<>();

        outputMap.forEach((material, quantity) -> outputList.add(Pair.of(material, (long) Math.floor(quantity))));

        return outputList;
    }

    private static ArrayList<FluidStack> validPlasmaGenerator(final List<Pair<Materials, Long>> planetList) {

        ArrayList<FluidStack> plasmaList = new ArrayList<>();

        for (Pair<Materials, Long> pair : planetList) {
            if (VALID_PLASMAS.contains(pair.getLeft())) {
                plasmaList.add(pair.getLeft().getPlasma(1));
            }
        }

        return plasmaList;
    }

    private static ArrayList<ItemStackLong> validDustGenerator(final ArrayList<Pair<Materials, Long>> planetList) {

        ArrayList<ItemStackLong> dustList = new ArrayList<>();

        for (Pair<Materials, Long> pair : planetList) {
            ItemStack dust = getUnificatedOreDictStack(pair.getLeft().getDust(1));
            if (dust != null) {
                dustList.add(new ItemStackLong(dust, pair.getRight()));
            }
        }
        return dustList;
    }

    private static long plasmaCostCalculator(ArrayList<FluidStack> plasmas) {
        long total = 0;

        for (FluidStack plasma : plasmas) {
            try {
                String plasmaName = plasma.getFluid().getUnlocalizedName();
                total += plasmaEnergyMap.getOrDefault(plasmaName, 0L) * plasma.amount;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return (long) (total * getMaxPlasmaTurbineEfficiency());
    }

    private static double getMaxPlasmaTurbineEfficiency() {
        // I hate Shirabon.
        return 3.85;
    }

    private static final List<Materials> VALID_PLASMAS = Stream.of(
                    Materials.Helium,
                    Materials.Iron,
                    Materials.Calcium,
                    Materials.Niobium,
                    Materials.Nitrogen,
                    Materials.Zinc,
                    Materials.Silver,
                    Materials.Titanium,
                    Materials.Radon,
                    Materials.Nickel,
                    Materials.Boron,
                    Materials.Sulfur,
                    Materials.Americium,
                    Materials.Bismuth,
                    Materials.Oxygen,
                    Materials.Tin)
            .collect(Collectors.toList());

    private static final HashMap<String, Long> plasmaEnergyMap = new HashMap<String, Long>() {
        {
            VALID_PLASMAS.forEach(
                    (material -> put(material.getPlasma(1).getFluid().getUnlocalizedName(), (long)
                            (getPlasmaFuelValueInEUPerLiterFromMaterial(material) * getMaxPlasmaTurbineEfficiency()))));
        }
    };
}
