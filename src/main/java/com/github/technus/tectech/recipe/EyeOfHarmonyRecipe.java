package com.github.technus.tectech.recipe;

import static com.github.technus.tectech.recipe.EyeOfHarmonyRecipeStorage.BILLION;
import static com.google.common.math.IntMath.pow;
import static gregtech.api.GregTech_API.getUnificatedOreDictStack;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_Utility.getPlasmaFuelValueInEUPerLiterFromMaterial;
import static java.lang.Math.min;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import pers.gwyog.gtneioreplugin.plugin.block.BlockDimensionDisplay;
import pers.gwyog.gtneioreplugin.util.GT5OreLayerHelper;
import pers.gwyog.gtneioreplugin.util.GT5OreSmallHelper;

import com.github.technus.tectech.util.ItemStackLong;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

@SuppressWarnings("SpellCheckingInspection")
public class EyeOfHarmonyRecipe {

    static final FluidStack[] SPECIAL_FLUIDS = new FluidStack[] { Materials.WhiteDwarfMatter.getMolten(1152),
            Materials.WhiteDwarfMatter.getMolten(1152), Materials.WhiteDwarfMatter.getMolten(4_608),
            Materials.WhiteDwarfMatter.getMolten(18_432), Materials.BlackDwarfMatter.getMolten(1152),
            Materials.BlackDwarfMatter.getMolten(4_608), Materials.BlackDwarfMatter.getMolten(18_432),
            Materials.Universium.getMolten(1152), Materials.Universium.getMolten(4_608),
            Materials.Universium.getMolten(18_432) };

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

    public EyeOfHarmonyRecipe(final ArrayList<Pair<Materials, Long>> materialList, final BlockDimensionDisplay block,
            final double recipeEnergyEfficiency, final long hydrogenRequirement, final long heliumRequirement,
            final long miningTimeSeconds, final long rocketTierOfRecipe, final double baseSuccessChance) {

        this.rocketTier = rocketTierOfRecipe;
        this.spacetimeCasingTierRequired = min(8, rocketTierOfRecipe);

        this.recipeTriggerItem = new ItemStack(block);

        this.outputItems = validDustGenerator(materialList);

        this.sumOfItems = this.outputItems.stream().map(ItemStackLong::getStackSize).reduce(0L, Long::sum);

        this.outputItems.add(new ItemStackLong(getStoneDustType(block.getDimension()), this.sumOfItems * 3L));
        this.outputItems.sort(Comparator.comparingLong(ItemStackLong::getStackSize));
        Collections.reverse(this.outputItems);

        for (ItemStackLong itemStackLong : outputItems) {
            double stackSize = (double) itemStackLong.getStackSize();
            double probability = Math.round(100_000 * stackSize / sumOfItems) / 1000.0;

            itemStackToProbabilityMap.put(itemStackLong.itemStack, probability);
            itemStackToTrueStackSizeMap.put(itemStackLong.itemStack, itemStackLong.stackSize);
        }
        // End item processing.

        // --- Fluid handling ---
        ArrayList<FluidStack> fluidStackArrayList = new ArrayList<>();

        // If DeepDark then it should output all plasmas involved in making exotic catalyst.
        if (rocketTier == 9) {
            for (Materials material : VALID_PLASMAS) {
                fluidStackArrayList.add(material.getPlasma(1));
            }
        } else {
            // --- Output and process fluids of the recipe.
            fluidStackArrayList.addAll(validPlasmaGenerator(materialList));
        }

        for (FluidStack fluidStack : fluidStackArrayList) {
            fluidStack.amount = (int) ((this.spacetimeCasingTierRequired + 1) * 8_000_000L);
        }

        // Add a bonus fluid of compressed star matter.
        fluidStackArrayList.add(Materials.RawStarMatter.getFluid((this.spacetimeCasingTierRequired + 1) * 100_000));

        // Tier 0 & 1 - 576 White
        // Tier 2 - 2304 White
        // Tier 3 - 9216 White

        // Tier 4 - 576 Black
        // Tier 5 - 2304 Black
        // Tier 6 - 9216 Black

        // Tier 7 - 576 Universium
        // Tier 8 - 2304 Universium
        // Tier 9 - 9216 Universium
        int spacetimeTier = (int) rocketTierOfRecipe;
        if (spacetimeTier == 0 || spacetimeTier == 9) {
            spacetimeTier -= 1;
        }
        fluidStackArrayList.add(SPECIAL_FLUIDS[spacetimeTier + 1]);

        outputFluids = fluidStackArrayList;
        // --- End fluid handling ---.

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

    private ItemStack getStoneDustType(String key) {

        switch (key) {
            case "Ne":
                return GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Netherrack, 1);
            case "ED":
            case "VA":
            case "EA":
                return GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Endstone, 1);
            case "Mo":
                return getModItem(NewHorizonsCoreMod.ID, "item.MoonStoneDust", 1);
            case "De":
                return getModItem(NewHorizonsCoreMod.ID, "item.DeimosStoneDust", 1);
            case "Ma":
                return getModItem(NewHorizonsCoreMod.ID, "item.MarsStoneDust", 1);
            case "Ph":
                return getModItem(NewHorizonsCoreMod.ID, "item.PhobosStoneDust", 1);
            case "As":
            case "KB":
                return getModItem(NewHorizonsCoreMod.ID, "item.AsteroidsStoneDust", 1);
            case "Ca":
                return getModItem(NewHorizonsCoreMod.ID, "item.CallistoStoneDust", 1);
            case "Ce":
                return getModItem(NewHorizonsCoreMod.ID, "item.CeresStoneDust", 1);
            case "Eu":
                return getModItem(NewHorizonsCoreMod.ID, "item.EuropaStoneDust", 1);
            case "Ga":
                return getModItem(NewHorizonsCoreMod.ID, "item.GanymedeStoneDust", 1);
            case "Io":
                return getModItem(NewHorizonsCoreMod.ID, "item.IoStoneDust", 1);
            case "Me":
                return getModItem(NewHorizonsCoreMod.ID, "item.MercuryStoneDust", 1);
            case "Ve":
                return getModItem(NewHorizonsCoreMod.ID, "item.VenusStoneDust", 1);
            case "En":
                return getModItem(NewHorizonsCoreMod.ID, "item.EnceladusStoneDust", 1);
            case "Mi":
                return getModItem(NewHorizonsCoreMod.ID, "item.MirandaStoneDust", 1);
            case "Ob":
                return getModItem(NewHorizonsCoreMod.ID, "item.OberonStoneDust", 1);
            case "Ti":
                return getModItem(NewHorizonsCoreMod.ID, "item.TitanStoneDust", 1);
            case "Pr":
                return getModItem(NewHorizonsCoreMod.ID, "item.ProteusStoneDust", 1);
            case "Tr":
                return getModItem(NewHorizonsCoreMod.ID, "item.TritonStoneDust", 1);
            case "Ha":
                return getModItem(NewHorizonsCoreMod.ID, "item.HaumeaStoneDust", 1);
            case "MM":
                return getModItem(NewHorizonsCoreMod.ID, "item.MakeMakeStoneDust", 1);
            case "Pl":
                return getModItem(NewHorizonsCoreMod.ID, "item.PlutoStoneDust", 1);
            case "BE":
                return getModItem(NewHorizonsCoreMod.ID, "item.BarnardaEStoneDust", 1);
            case "BF":
                return getModItem(NewHorizonsCoreMod.ID, "item.BarnardaFStoneDust", 1);
            case "CB":
                return getModItem(NewHorizonsCoreMod.ID, "item.CentauriAStoneDust", 1);
            case "TE":
                return getModItem(NewHorizonsCoreMod.ID, "item.TCetiEStoneDust", 1);
            case "VB":
                return getModItem(NewHorizonsCoreMod.ID, "item.VegaBStoneDust", 1);
            default:
                return GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Stone, 1);
        }
    }

    public EyeOfHarmonyRecipe(final GT5OreLayerHelper.NormalOreDimensionWrapper normalOreDimensionWrapper,
            final GT5OreSmallHelper.SmallOreDimensionWrapper smallOreDimensionWrapper,
            final BlockDimensionDisplay block, final double recipeEnergyEfficiency, final long hydrogenRequirement,
            final long heliumRequirement, final long miningTimeSeconds, final long spacetimeCasingTierRequired,
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

    private static final double[] ORE_MULTIPLIER = { PRIMARY_MULTIPLIER, SECONDARY_MULTIPLIER, TERTIARY_MULTIPLIER,
            QUATERNARY_MULTIPLIER };

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

    public static void processHelper(HashMapHelper outputMap, Materials material, double mainMultiplier,
            double probability) {
        if (material == null) return;
        outputMap.add(material.mDirectSmelting, (material.mOreMultiplier * 2) * mainMultiplier * probability);

        int index = 0;
        for (Materials byProductMaterial : material.mOreByProducts) {
            outputMap.add(
                    byProductMaterial.mDirectSmelting,
                    mainMultiplier * (ORE_MULTIPLIER[index++] * 2) * probability);
        }
    }

    private static ArrayList<Pair<Materials, Long>> processDimension(
            GT5OreLayerHelper.NormalOreDimensionWrapper normalOreDimWrapper,
            GT5OreSmallHelper.SmallOreDimensionWrapper smallOreDimWrapper, long timeInSeconds) {
        HashMapHelper outputMap = new HashMapHelper();

        double mainMultiplier = timeInSeconds * 384.0;

        if (normalOreDimWrapper != null) {
            normalOreDimWrapper.oreVeinToProbabilityInDimension.forEach((veinInfo, probability) -> {
                processHelper(outputMap, veinInfo.mPrimaryVeinMaterial, mainMultiplier, probability);
                processHelper(outputMap, veinInfo.mSecondaryMaterial, mainMultiplier, probability);
                // 8.0 to replicate void miner getDropsVanillaVeins method yields.
                processHelper(outputMap, veinInfo.mBetweenMaterial, mainMultiplier / 8.0, probability);
                processHelper(outputMap, veinInfo.mSporadicMaterial, mainMultiplier / 8.0, probability);
            });
        }

        // Iterate over small ores in dimension and add them, kinda hacky but works and is close enough.
        if (smallOreDimWrapper != null) {
            smallOreDimWrapper.oreVeinToProbabilityInDimension.forEach(
                    (veinInfo, probability) -> processHelper(
                            outputMap,
                            veinInfo.getOreMaterial(),
                            mainMultiplier,
                            probability));
        }

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
            Materials.Tin).collect(Collectors.toList());

    private static final HashMap<String, Long> plasmaEnergyMap = new HashMap<String, Long>() {

        {
            VALID_PLASMAS.forEach(
                    (material -> put(
                            material.getPlasma(1).getFluid().getUnlocalizedName(),
                            (long) (getPlasmaFuelValueInEUPerLiterFromMaterial(material)
                                    * getMaxPlasmaTurbineEfficiency()))));
        }
    };
}
