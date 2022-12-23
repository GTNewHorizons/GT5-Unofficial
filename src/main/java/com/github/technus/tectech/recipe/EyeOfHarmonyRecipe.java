package com.github.technus.tectech.recipe;

import static com.github.technus.tectech.recipe.EyeOfHarmonyRecipeStorage.BILLION;
import static gregtech.api.util.GT_Utility.getPlasmaFuelValueInEUPerLiterFromMaterial;

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

    public TMap<ItemStack, Double> getItemStackToProbabilityMap() {
        return itemStackToProbabilityMap;
    }

    public TMap<ItemStack, Long> getItemStackToTrueStackSizeMap() {
        return itemStackToTrueStackSizeMap;
    }

    public double getRecipeEnergyEfficiency() {
        return recipeEnergyEfficiency;
    }

    private final long standardRecipeEUPerTick = 500 * BILLION;

    public EyeOfHarmonyRecipe(
            final GT5OreLayerHelper.NormalOreDimensionWrapper normalOreDimensionWrapper,
            final GT5OreSmallHelper.SmallOreDimensionWrapper smallOreDimensionWrapper,
            final double recipeEnergyEfficiency, // E.g. 90% efficient = 0.9 = lose 10% EU from plasma + EU output.
            final long hydrogenRequirement,
            final long heliumRequirement,
            final long miningTimeSeconds,
            final long spacetimeCasingTierRequired,
            final Block block,
            final double baseSuccessChance) {

        this.euOutput = (long) ((recipeEnergyEfficiency / 2) * standardRecipeEUPerTick * miningTimeSeconds * 20);

        recipeTriggerItem = new ItemStack(block);

        // Process recipes output items.
        // 6 * 64 = 6 stacks/second for VM tier 3 + Og gas.
        ArrayList<Pair<Materials, Long>> materialList =
                processDimension(normalOreDimensionWrapper, smallOreDimensionWrapper, miningTimeSeconds, 6 * 64);

        this.outputItems = validDustGenerator(materialList);
        this.outputItems.sort(Comparator.comparingLong(ItemStackLong::getStackSize));
        Collections.reverse(this.outputItems);

        long sumOfItems =
                this.outputItems.stream().map(ItemStackLong::getStackSize).reduce(0L, Long::sum);

        for (ItemStackLong itemStackLong : outputItems) {
            double stackSize = (double) itemStackLong.getStackSize();
            double probability = Math.round(100_000 * stackSize / sumOfItems) / 1000.0;

            itemStackToProbabilityMap.put(itemStackLong.itemStack, probability);
            itemStackToTrueStackSizeMap.put(itemStackLong.itemStack, itemStackLong.stackSize);
        }
        // End item processing.

        // --- Output and process fluids of the recipe.

        // The idea behind this is to generate

        Pair<Long, ArrayList<FluidStack>> pair = validPlasmaGenerator(materialList);
        // The idea behind this is to get e.g.
        // totalEuOutput = timeRun * standardRecipeEUPerTick.
        // recipeEnergyEfficiency * (10 EU * plasmaAmount + 100 EU * plasmaAmount) = totalEuOutput.
        // Where 10 EU and 100 EU are the (plasmaFuelValues * maxTurbineEfficiency) of 1L of that specific plasma.
        // Then we solve for plasmaAmount, so that each plasma has the same output amount.
        long totalEUSumOfIndividualPlasmaFuels = pair.getLeft();
        ArrayList<FluidStack> fluidStackArrayList = pair.getRight();

        for (FluidStack fluidStack : fluidStackArrayList) {
            if (this.euOutput / totalEUSumOfIndividualPlasmaFuels < Integer.MAX_VALUE) {
                // EU is split between Plasma output and EU output, hence /2.
                // recipeEnergyEfficiency > 1 determines if this recipe makes more EU than you put in when you sum the
                // EU in the plasma + the EU output.
                fluidStack.amount = (int) ((recipeEnergyEfficiency * 0.01) * this.euOutput / totalEUSumOfIndividualPlasmaFuels);
            } else {
                // Hopefully won't happen but just in case.
                fluidStack.amount = Integer.MAX_VALUE;
            }
        }

        // Add a bonus fluid of compressed star matter which is equal to 10% of 1 plasma fluid stacks output.
        if (fluidStackArrayList.size() > 0) {
            // todo replace with Bonus star matter when added to GT5.
            fluidStackArrayList.add(Materials.Infinity.getMolten((long) (0.1 * fluidStackArrayList.get(0).amount)));
        }

//         Sort fluids by stack size.
//        fluidStackArrayList.sort(Comparator.comparingLong((FluidStack fluid) -> fluid.amount));
//        Collections.reverse(fluidStackArrayList);

        outputFluids = fluidStackArrayList;
        // End fluid processing.

        this.spacetimeCasingTierRequired = spacetimeCasingTierRequired;

        this.hydrogenRequirement = hydrogenRequirement;
        this.heliumRequirement = heliumRequirement;

        this.baseSuccessChance = baseSuccessChance;

        this.miningTimeSeconds = miningTimeSeconds;
        this.recipeEnergyEfficiency = recipeEnergyEfficiency;

        long a = (long) (recipeEnergyEfficiency * (plasmaCostCalculator(outputFluids) + this.euOutput) / 2);
        this.euStartCost = a;
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

    static final double primaryMultiplier = (0.1 + 1.0 / 9.0); // Byproduct from macerating/washing chance.
    static final double secondaryMultiplier = (1.0 / 9.0); // Thermal centrifuge byproduct chance.
    static final double tertiaryMultiplier = (0.1); // Macerating thermal centrifuged byproduct chance.
    static final double quaternaryMultiplier = (0.7); // Mercury/chem bath processing chance.

    static final double[] oreMultiplier = {
        primaryMultiplier, secondaryMultiplier, tertiaryMultiplier, quaternaryMultiplier
    };

    private static class HashMapHelper extends HashMap<Materials, Double> {

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

    private static void processHelper(HashMapHelper outputMap, Materials material, double mainMultiplier, double probability) {
        outputMap.add(material.mDirectSmelting, (material.mOreMultiplier * 2) * mainMultiplier * probability);

        int index = 0;
        for (Materials byProductMaterial : material.mOreByProducts) {
            outputMap.add(
                    byProductMaterial.mDirectSmelting, mainMultiplier * (oreMultiplier[index++] * 2) * probability);
        }
    }

    private static ArrayList<Pair<Materials, Long>> processDimension(
            GT5OreLayerHelper.NormalOreDimensionWrapper normalOreDimWrapper,
            GT5OreSmallHelper.SmallOreDimensionWrapper smallOreDimWrapper,
            long timeInSeconds,
            long miningMultiplier) {
        HashMapHelper outputMap = new HashMapHelper();

        double mainMultiplier = timeInSeconds * miningMultiplier;

        normalOreDimWrapper.oreVeinToProbabilityInDimension.forEach((veinInfo, probability) -> {
            processHelper(outputMap, veinInfo.mPrimaryVeinMaterial, mainMultiplier, probability);
            processHelper(outputMap, veinInfo.mSecondaryMaterial, mainMultiplier, probability);
            // 8.0 to replicate void miner getDropsVanillaVeins method yields.
            processHelper(outputMap, veinInfo.mBetweenMaterial, mainMultiplier / 8.0, probability);
            processHelper(outputMap, veinInfo.mSporadicMaterial, mainMultiplier / 8.0, probability);
        });

        // Iterate over small ores in dimension and add them, kinda hacky but works and is close enough.
        smallOreDimWrapper.oreVeinToProbabilityInDimension.forEach((veinInfo, probability) -> {
            processHelper(outputMap, veinInfo.getOreMaterial(), mainMultiplier, probability);
        });

        ArrayList<Pair<Materials, Long>> outputList = new ArrayList<>();

        outputMap.forEach((material, quantity) -> outputList.add(Pair.of(material, (long) Math.floor(quantity))));

        return outputList;
    }

    private static Pair<Long, ArrayList<FluidStack>> validPlasmaGenerator(
            final List<Pair<Materials, Long>> planetList) {

        ArrayList<FluidStack> plasmaList = new ArrayList<>();

        long sumOfPlasmasEU = 0;

        for (Pair<Materials, Long> pair : planetList) {
            if (validPlasmas.contains(pair.getLeft())) {
                plasmaList.add(pair.getLeft().getPlasma(1));
                String plasmaString = pair.getLeft().getPlasma(1).getFluid().getUnlocalizedName();
                sumOfPlasmasEU += plasmaEnergyMap.getOrDefault(plasmaString, -100000000000000000L);
            }
        }

        return Pair.of(sumOfPlasmasEU, plasmaList);
    }

    private static ArrayList<ItemStackLong> validDustGenerator(final ArrayList<Pair<Materials, Long>> planetList) {

        ArrayList<ItemStackLong> dustList = new ArrayList<>();

        for (Pair<Materials, Long> pair : planetList) {
            ItemStack dust = pair.getLeft().getDust(1);
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
                total += plasmaEnergyMap.get(plasmaName) * plasma.amount;
            } catch(Exception e) {
                e.printStackTrace();
            }
        }

        return (long) (total * getMaxPlasmaTurbineEfficiency());
    }

    private static double getMaxPlasmaTurbineEfficiency() {
        // I hate Shirabon.
        //        return getMaxPlasmaTurbineEfficiency();
        return 3.85;
    }

    private static final List<Materials> validPlasmas = Stream.of(
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
            validPlasmas.forEach((material -> put(
                    material.getPlasma(1).getFluid().getUnlocalizedName(), (long) (getPlasmaFuelValueInEUPerLiterFromMaterial(material) * getMaxPlasmaTurbineEfficiency()))));
        }
    };
}
