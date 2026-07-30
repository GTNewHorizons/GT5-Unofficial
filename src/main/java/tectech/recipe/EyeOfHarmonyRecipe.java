package tectech.recipe;

import static com.google.common.math.IntMath.pow;
import static gregtech.api.GregTechAPI.getUnificatedOreDictStack;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTUtility.getPlasmaFuelValueInEUPerLiterFromFluid;
import static java.lang.Math.min;

import java.util.ArrayDeque;
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
import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gnu.trove.map.TMap;
import gnu.trove.map.hash.TCustomHashMap;
import gnu.trove.strategy.HashingStrategy;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.BlockShapes;
import gregtech.api.enums.materials2.FluidShapes;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialRefStack;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.loaders.materials.LegacyNameDomain;
import gtneioreplugin.plugin.block.BlockDimensionDisplay;
import gtneioreplugin.util.GT5OreLayerHelper;
import gtneioreplugin.util.GT5OreSmallHelper;
import tectech.util.FluidStackLong;
import tectech.util.ItemStackLong;

@SuppressWarnings("SpellCheckingInspection")
public class EyeOfHarmonyRecipe {

    static final FluidStackLong[] SPECIAL_FLUIDS = new FluidStackLong[] {
        new FluidStackLong(
            MaterialLibAPI.getFluidStack(Materials.WhiteDwarfMatter, FluidShapes.fluidMolten, (int) (1_152)),
            1_152),
        new FluidStackLong(
            MaterialLibAPI.getFluidStack(Materials.WhiteDwarfMatter, FluidShapes.fluidMolten, (int) (1_152)),
            1_152),
        new FluidStackLong(
            MaterialLibAPI.getFluidStack(Materials.WhiteDwarfMatter, FluidShapes.fluidMolten, (int) (4_608)),
            4_608),
        new FluidStackLong(
            MaterialLibAPI.getFluidStack(Materials.WhiteDwarfMatter, FluidShapes.fluidMolten, (int) (18_432)),
            18_432),
        new FluidStackLong(
            MaterialLibAPI.getFluidStack(Materials.BlackDwarfMatter, FluidShapes.fluidMolten, (int) (1_152)),
            1_152),
        new FluidStackLong(
            MaterialLibAPI.getFluidStack(Materials.BlackDwarfMatter, FluidShapes.fluidMolten, (int) (4_608)),
            4_608),
        new FluidStackLong(
            MaterialLibAPI.getFluidStack(Materials.BlackDwarfMatter, FluidShapes.fluidMolten, (int) (18_432)),
            18_432),
        new FluidStackLong(
            MaterialLibAPI.getFluidStack(Materials.Universium, FluidShapes.fluidMolten, (int) (1_152)),
            1_152),
        new FluidStackLong(
            MaterialLibAPI.getFluidStack(Materials.Universium, FluidShapes.fluidMolten, (int) (4_608)),
            4_608),
        new FluidStackLong(
            MaterialLibAPI.getFluidStack(Materials.Universium, FluidShapes.fluidMolten, (int) (18_432)),
            18_432) };

    HashingStrategy<ItemStack> itemStackHashingStrategy = new HashingStrategy<>() {

        private static final long serialVersionUID = -3966004160368229212L;

        @Override
        public int computeHashCode(ItemStack stack) {
            int result = stack.getItem()
                .hashCode();
            result = 31 * result + stack.getItemDamage();
            return result;
        }

        // Compares the same fields the hash uses: unlocalized-name comparison broke the hashCode/equals
        // contract for MaterialLib shape items, whose unlocalized name is not damage-specific.
        @Override
        public boolean equals(ItemStack item1, ItemStack item2) {
            return item1.getItem() == item2.getItem() && item1.getItemDamage() == item2.getItemDamage();
        }
    };

    private final TMap<ItemStack, Double> itemStackToProbabilityMap = new TCustomHashMap<>(itemStackHashingStrategy);
    private final TMap<ItemStack, Long> itemStackToTrueStackSizeMap = new TCustomHashMap<>(itemStackHashingStrategy);

    private final ArrayList<ItemStackLong> outputItems;
    private final ArrayList<FluidStackLong> outputFluids;

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
    private final long standardRecipeEUOutPerTick = 100 * EyeOfHarmonyRecipeStorage.BILLION;

    public long getSumOfItems() {
        return sumOfItems;
    }

    public long getRocketTier() {
        return rocketTier;
    }

    public EyeOfHarmonyRecipe(final ArrayList<Pair<Object, Long>> materialList, final BlockDimensionDisplay block,
        final double recipeEnergyEfficiency, final long hydrogenRequirement, final long heliumRequirement,
        final long miningTimeSeconds, final long rocketTierOfRecipe, final double baseSuccessChance) {

        this.rocketTier = rocketTierOfRecipe;
        this.spacetimeCasingTierRequired = min(8, rocketTierOfRecipe);

        this.recipeTriggerItem = new ItemStack(block);

        final TMap<ItemStack, Long> outputItemsTemp = validDustGenerator(materialList);

        this.sumOfItems = outputItemsTemp.values()
            .stream()
            .reduce(0L, Long::sum);

        outputItemsTemp.merge(getStoneDustType(block.getDimension()), this.sumOfItems * 3L, Long::sum);
        this.outputItems = new ArrayList<>();
        outputItemsTemp
            .forEach((itemstack, stacksize) -> this.outputItems.add(new ItemStackLong(itemstack, stacksize)));
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
        ArrayList<FluidStackLong> fluidStackLongArrayList = new ArrayList<>();

        int plasmaAmount = (int) ((this.spacetimeCasingTierRequired + 1) * 8_000_000L);

        // If DeepDark then it should output all plasmas involved in making exotic catalyst.
        if (rocketTier == 9) {
            for (com.ruling_0.materiallib.api.Material material : VALID_PLASMAS) {
                fluidStackLongArrayList
                    .add(new FluidStackLong(MaterialUtils.plasma(material, plasmaAmount), plasmaAmount));
            }
        } else {
            // --- Output and process fluids of the recipe.
            ArrayList<FluidStack> fluidStackArrayList = new ArrayList<>(validPlasmaGenerator(materialList));
            for (FluidStack fluidStack : fluidStackArrayList) {
                fluidStack = new FluidStack(fluidStack, plasmaAmount);
                fluidStackLongArrayList.add(new FluidStackLong(fluidStack, plasmaAmount));
            }
        }

        // Add a bonus fluid of compressed star matter.
        fluidStackLongArrayList.add(
            new FluidStackLong(
                MaterialLibAPI.getFluidStack(
                    Materials.RawStarMatter,
                    FluidShapes.fluidLiquid,
                    (int) ((this.spacetimeCasingTierRequired + 1) * 100_000)),
                (this.spacetimeCasingTierRequired + 1) * 100_000));

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
        fluidStackLongArrayList.add(SPECIAL_FLUIDS[spacetimeTier + 1]);

        outputFluids = fluidStackLongArrayList;
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
        ItemStack placeholder = MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, (int) (1));
        return switch (key) {
            case "Ne" -> MaterialLibAPI.getStack(Materials.Netherrack, Shapes.dust, (int) (1));
            case "ED", "VA", "EA" -> MaterialLibAPI.getStack(Materials.Endstone, Shapes.dust, (int) (1));
            case "Mo", "Ra" -> getModItem(NewHorizonsCoreMod.ID, "MoonStoneDust", 1, placeholder);
            case "De" -> getModItem(NewHorizonsCoreMod.ID, "DeimosStoneDust", 1, placeholder);
            case "Ma" -> getModItem(NewHorizonsCoreMod.ID, "MarsStoneDust", 1, placeholder);
            case "Ph" -> getModItem(NewHorizonsCoreMod.ID, "PhobosStoneDust", 1, placeholder);
            case "As", "KB" -> getModItem(NewHorizonsCoreMod.ID, "AsteroidsStoneDust", 1, placeholder);
            case "Ca" -> getModItem(NewHorizonsCoreMod.ID, "CallistoStoneDust", 1, placeholder);
            case "Ce" -> getModItem(NewHorizonsCoreMod.ID, "CeresStoneDust", 1, placeholder);
            case "Eu" -> getModItem(NewHorizonsCoreMod.ID, "EuropaStoneDust", 1, placeholder);
            case "Ga" -> getModItem(NewHorizonsCoreMod.ID, "GanymedeStoneDust", 1, placeholder);
            case "Io" -> getModItem(NewHorizonsCoreMod.ID, "IoStoneDust", 1, placeholder);
            case "Me" -> getModItem(NewHorizonsCoreMod.ID, "MercuryStoneDust", 1, placeholder);
            case "Ve" -> getModItem(NewHorizonsCoreMod.ID, "VenusStoneDust", 1, placeholder);
            case "En" -> getModItem(NewHorizonsCoreMod.ID, "EnceladusStoneDust", 1, placeholder);
            case "Mi" -> getModItem(NewHorizonsCoreMod.ID, "MirandaStoneDust", 1, placeholder);
            case "Ob" -> getModItem(NewHorizonsCoreMod.ID, "OberonStoneDust", 1, placeholder);
            case "Ti" -> getModItem(NewHorizonsCoreMod.ID, "TitanStoneDust", 1, placeholder);
            case "Pr" -> getModItem(NewHorizonsCoreMod.ID, "ProteusStoneDust", 1, placeholder);
            case "Tr" -> getModItem(NewHorizonsCoreMod.ID, "TritonStoneDust", 1, placeholder);
            case "Ha" -> getModItem(NewHorizonsCoreMod.ID, "HaumeaStoneDust", 1, placeholder);
            case "MM" -> getModItem(NewHorizonsCoreMod.ID, "MakeMakeStoneDust", 1, placeholder);
            case "Pl" -> getModItem(NewHorizonsCoreMod.ID, "PlutoStoneDust", 1, placeholder);
            case "BE" -> getModItem(NewHorizonsCoreMod.ID, "BarnardaEStoneDust", 1, placeholder);
            case "BF" -> getModItem(NewHorizonsCoreMod.ID, "BarnardaFStoneDust", 1, placeholder);
            case "CB" -> getModItem(NewHorizonsCoreMod.ID, "CentauriAStoneDust", 1, placeholder);
            case "TE" -> getModItem(NewHorizonsCoreMod.ID, "TCetiEStoneDust", 1, placeholder);
            case "VB" -> getModItem(NewHorizonsCoreMod.ID, "VegaBStoneDust", 1, placeholder);
            default -> placeholder;
        };
    }

    public EyeOfHarmonyRecipe(final GT5OreLayerHelper.NormalOreDimensionWrapper normalOreDimensionWrapper,
        final GT5OreSmallHelper.SmallOreDimensionWrapper smallOreDimensionWrapper, final BlockDimensionDisplay block,
        final double recipeEnergyEfficiency, final long hydrogenRequirement, final long heliumRequirement,
        final long miningTimeSeconds, final long spacetimeCasingTierRequired, final double baseSuccessChance) {

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
    public ArrayList<FluidStackLong> getOutputFluids() {
        ArrayList<FluidStackLong> copyOutputList = new ArrayList<>();

        for (FluidStackLong fluidStackLong : outputFluids) {
            copyOutputList.add(new FluidStackLong(fluidStackLong));
        }

        return copyOutputList;
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
    private static final double QUATERNARY99_MULTIPLIER = (0.99); // Mercury/chem bath processing chance.
    private static final double ELECTROMAGNETIC_MULTIPLIER = (0.4 / 4.0 + 0.2 / 9.0); // MagneticSep. processing chance.

    private static final double[] ORE_MULTIPLIER = { PRIMARY_MULTIPLIER, SECONDARY_MULTIPLIER, TERTIARY_MULTIPLIER };

    /// Accumulates output quantities keyed by canonical [com.ruling_0.materiallib.api.Material] -- every source
    /// (gregtech, bartworks, or a gtpp-flavored material) resolves to the same registry singleton, so
    /// contributions naming the same material always merge into one entry.
    public static class HashMapHelper extends HashMap<Object, Double> {

        private static final long serialVersionUID = 2297018142561480614L;

        private void addRaw(Object material, double value) {
            if (material == null) return;

            // If key already exists.
            if (this.containsKey(material)) {
                this.put(material, value + this.get(material));
                return;
            }

            // Otherwise, add value to hashmap entry.
            this.put(material, value);
        }

        void add(com.ruling_0.materiallib.api.Material material, double value) {
            addRaw(material, value);
        }
    }

    /// Reads every legacy field through [MaterialUtils] property accessors, each resolving independently against
    /// the live facade rather than a single upfront snapshot. Neither the bartworks nor gtpp bridge loader ever
    /// writes `mOreMultiplier`/`mByProductMultiplier`/`mSmeltingMultiplier` independently of
    /// [GTMaterialProperties], so this reads the same single source of truth those legacy fields do.
    public static void processHelper(HashMapHelper outputMap, com.ruling_0.materiallib.api.Material material,
        double mainMultiplier, double probability) {
        if (material == null) return;
        outputMap.add(
            MaterialUtils.directSmelting(material),
            (MaterialUtils.oreMultiplier(material) * 2) * mainMultiplier * probability);

        if (MaterialUtils.hasFlag(material, GTMaterialFlag.ELECTROMAGNETIC_SEPERATION_GOLD))
            outputMap.add(Materials.Gold, mainMultiplier * (ELECTROMAGNETIC_MULTIPLIER * 2) * probability);
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.ELECTROMAGNETIC_SEPERATION_IRON))
            outputMap.add(Materials.Iron, mainMultiplier * (ELECTROMAGNETIC_MULTIPLIER * 2) * probability);
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM))
            outputMap.add(Materials.Neodymium, mainMultiplier * (ELECTROMAGNETIC_MULTIPLIER * 2) * probability);

        List<com.ruling_0.materiallib.api.Material> byProducts = MaterialUtils.oreByProducts(material);

        if (byProducts.isEmpty()) {
            if (MaterialUtils.hasFlag(material, GTMaterialFlag.WASHING_MERCURY_99_PERCENT)) outputMap.add(
                MaterialUtils.directSmelting(material),
                mainMultiplier * (QUATERNARY99_MULTIPLIER * 2) * probability);
            else if (MaterialUtils.hasFlag(material, GTMaterialFlag.WASHING_MERCURY)) outputMap.add(
                MaterialUtils.directSmelting(material),
                mainMultiplier * (QUATERNARY_MULTIPLIER * 2) * probability);
            else if (MaterialUtils.hasFlag(material, GTMaterialFlag.WASHING_SODIUMPERSULFATE)) outputMap.add(
                MaterialUtils.directSmelting(material),
                mainMultiplier * (QUATERNARY_MULTIPLIER * 2) * probability);
        }

        if (MaterialUtils.hasFlag(material, GTMaterialFlag.WASHING_MERCURY_99_PERCENT)) outputMap
            .add(MaterialUtils.directSmelting(material), mainMultiplier * (QUATERNARY99_MULTIPLIER * 2) * probability);
        else if (MaterialUtils.hasFlag(material, GTMaterialFlag.WASHING_MERCURY)) outputMap
            .add(MaterialUtils.directSmelting(material), mainMultiplier * (QUATERNARY_MULTIPLIER * 2) * probability);
        else if (MaterialUtils.hasFlag(material, GTMaterialFlag.WASHING_SODIUMPERSULFATE)) outputMap
            .add(MaterialUtils.directSmelting(material), mainMultiplier * (QUATERNARY_MULTIPLIER * 2) * probability);

        int index = 0;
        for (com.ruling_0.materiallib.api.Material byProductMaterial : byProducts) {
            if (index < 3) outputMap.add(
                MaterialUtils.directSmelting(byProductMaterial),
                mainMultiplier * (ORE_MULTIPLIER[index] * 2) * probability);
            // For Materials that index is > 3, normally they will not be used (unless using Chem bath).

            // MaterialLib hands out one canonical instance per material, so reference identity is the
            // self-byproduct test.
            if (byProductMaterial == material) continue;

            // Will never duplicate since mOreByProducts does not support duplicate.
            if (MaterialUtils.hasFlag(byProductMaterial, GTMaterialFlag.WASHING_MERCURY_99_PERCENT)) outputMap.add(
                MaterialUtils.directSmelting(byProductMaterial),
                mainMultiplier * (QUATERNARY99_MULTIPLIER * 2) * probability);
            else if (MaterialUtils.hasFlag(byProductMaterial, GTMaterialFlag.WASHING_MERCURY)) outputMap.add(
                MaterialUtils.directSmelting(byProductMaterial),
                mainMultiplier * (QUATERNARY_MULTIPLIER * 2) * probability);
            else if (MaterialUtils.hasFlag(byProductMaterial, GTMaterialFlag.WASHING_SODIUMPERSULFATE)) outputMap.add(
                MaterialUtils.directSmelting(byProductMaterial),
                mainMultiplier * (QUATERNARY_MULTIPLIER * 2) * probability);
            else if (index >= 3) outputMap.add(
                MaterialUtils.directSmelting(byProductMaterial),
                mainMultiplier * (QUATERNARY_MULTIPLIER * 2) * probability);
            // EOH is better than other ore processing so it can get products that normally cannot get.

            index++;
        }

        for (int i = index; i < 3; i++) {
            com.ruling_0.materiallib.api.Material byProductMaterial = GTUtility
                .selectItemInList(i, MaterialUtils.macerateInto(material), byProducts);
            outputMap.add(
                MaterialUtils.directSmelting(byProductMaterial),
                mainMultiplier * (ORE_MULTIPLIER[i] * 2) * probability);
            // Since it's duplicate, do not check if it can Mercury/chem bath.
        }
    }

    private static final double GTPP_PRIMARY_MULTIPLIER = (2.0 / 9.0 + 0.1);
    private static final double GTPP_SECONDARY_MULTIPLIER = (1.0 / 9.0);

    /// Whether a material carries every shape the gtpp bonus-byproduct algorithm below requires to consider it
    /// a usable bonus output -- the [GTMaterialProperties#COMPOSITION]-based port of the retired gtPlusPlus
    /// `Material#hasSolidForm`, which likewise required all four shapes present (not merely one).
    private static boolean hasSolidForm(com.ruling_0.materiallib.api.Material material) {
        return material.hasShape(Shapes.dust) && material.hasShape(BlockShapes.block)
            && material.hasShape(Shapes.dustTiny)
            && material.hasShape(Shapes.dustSmall);
    }

    /// A breadth-first walk of `material`'s [GTMaterialProperties#COMPOSITION] tree, collecting every leaf
    /// (a material with no composition of its own) -- the [com.ruling_0.materiallib.api.Material]-based port of
    /// the retired gtPlusPlus `MaterialUtils#getCompoundMaterialsRecursively`, which walked the equivalent
    /// legacy composite tree the same way. A composition entry that fails to resolve is dropped rather than
    /// descended into.
    private static ArrayList<com.ruling_0.materiallib.api.Material> compoundMaterialsRecursively(
        com.ruling_0.materiallib.api.Material material) {
        ArrayList<com.ruling_0.materiallib.api.Material> resultList = new ArrayList<>();
        ArrayDeque<com.ruling_0.materiallib.api.Material> toCheck = new ArrayDeque<>();
        toCheck.add(material);

        final int HARD_LIMIT = 1000;
        int processed = 0;
        while (!toCheck.isEmpty() && processed < HARD_LIMIT) {
            com.ruling_0.materiallib.api.Material current = toCheck.remove();
            List<MaterialRefStack> composition = current.getProperty(GTMaterialProperties.COMPOSITION);
            if (composition == null || composition.isEmpty()) {
                resultList.add(current);
            } else {
                for (MaterialRefStack entry : composition) {
                    com.ruling_0.materiallib.api.Material child = entry.material()
                        .resolve();
                    if (child != null) toCheck.add(child);
                }
            }
            processed++;
        }
        return resultList;
    }

    public static void processHelperGTpp(HashMapHelper outputMap, com.ruling_0.materiallib.api.Material material,
        double mainMultiplier, double probability) {
        if (material == null) return;
        outputMap.add(material, 2 * mainMultiplier * probability);

        // Copied from the retired src/main/java/gtPlusPlus/core/material/Material.java
        com.ruling_0.materiallib.api.Material bonusA = null; // Ni
        com.ruling_0.materiallib.api.Material bonusB = null; // Tin

        // Setup Bonuses
        ArrayList<com.ruling_0.materiallib.api.Material> aMatComp = compoundMaterialsRecursively(material);

        if (aMatComp.size() < 3) {
            while (aMatComp.size() < 3) {
                aMatComp.add(material);
            }
        }

        final ArrayList<com.ruling_0.materiallib.api.Material> amJ = new ArrayList<>(2);
        for (com.ruling_0.materiallib.api.Material g : aMatComp) {
            if (hasSolidForm(g)) {
                amJ.add(g);
                if (amJ.size() >= 2) break;
            }
        }

        boolean allFailed = false;
        List<MaterialRefStack> composites = material.getProperty(GTMaterialProperties.COMPOSITION);
        if (composites == null) composites = Collections.emptyList();

        if (amJ.size() < 2) {
            allFailed = true;
            bonusA = composites.isEmpty() ? material
                : composites.get(0)
                    .material()
                    .resolve();

            // If Secondary Output has no solid output, try the third (If it exists), then the fourth/fifth
            for (int i = 1; i < Math.min(composites.size(), 5); i++) {
                bonusB = composites.get(i)
                    .material()
                    .resolve();
                if (bonusB != null && hasSolidForm(bonusB)) {
                    allFailed = false;
                    break;
                }
            }
            // If Fifth Output has no solid output, default {see if(allFailed...)}
        } else {
            bonusA = amJ.get(0);
            bonusB = amJ.get(1);
        }

        int materialTier = MaterialUtils.tier(material);

        // Default out if it's made of fluids or some stuff.
        if (bonusA == null && materialTier >= 2) {
            bonusA = material;
        }
        // Default out if it's made of fluids or some stuff.
        if ((allFailed || bonusB == null) && materialTier >= 2) {
            bonusB = material;
        }

        // Need two valid outputs
        if (bonusA != null && hasSolidForm(bonusA)) {
            outputMap.add(bonusA, 2 * GTPP_PRIMARY_MULTIPLIER * mainMultiplier * probability);
        } else outputMap.add(Materials.Stone, 2 * GTPP_PRIMARY_MULTIPLIER * mainMultiplier * probability);
        if (bonusB != null && hasSolidForm(bonusB)) {
            outputMap.add(bonusB, 2 * GTPP_SECONDARY_MULTIPLIER * mainMultiplier * probability);
        } else outputMap.add(Materials.Stone, 2 * GTPP_SECONDARY_MULTIPLIER * mainMultiplier * probability);
    }

    /// Accumulates a vein material's dust yield. Only a gregtech-declared or werkstoff-origin material carries the
    /// composition this reads; a vein material from any other family contributes nothing.
    public static void processHelperIfPossible(HashMapHelper outputMap,
        @Nullable com.ruling_0.materiallib.api.Material material, double mainMultiplier, double probability) {
        if (material == null) return;
        if (LegacyNameDomain.contains(material) || material.getProperty(GTMaterialProperties.WERKSTOFF_IDS) != null) {
            processHelper(outputMap, material, mainMultiplier, probability);
        }
    }

    private static ArrayList<Pair<Object, Long>> processDimension(
        GT5OreLayerHelper.NormalOreDimensionWrapper normalOreDimWrapper,
        GT5OreSmallHelper.SmallOreDimensionWrapper smallOreDimWrapper, long timeInSeconds) {
        HashMapHelper outputMap = new HashMapHelper();

        double mainMultiplier = timeInSeconds * 384.0;

        if (normalOreDimWrapper != null) {
            normalOreDimWrapper.oreVeinToProbabilityInDimension.forEach((veinInfo, probability) -> {
                processHelperIfPossible(outputMap, veinInfo.mPrimaryVeinMaterial, mainMultiplier, probability);
                processHelperIfPossible(outputMap, veinInfo.mSecondaryMaterial, mainMultiplier, probability);
                // 8.0 to replicate void miner getDropsVanillaVeins method yields.
                processHelperIfPossible(outputMap, veinInfo.mBetweenMaterial, mainMultiplier / 8.0, probability);
                processHelperIfPossible(outputMap, veinInfo.mSporadicMaterial, mainMultiplier / 8.0, probability);
            });
        }

        // Iterate over small ores in dimension and add them, kinda hacky but works and is close enough.
        if (smallOreDimWrapper != null) {
            smallOreDimWrapper.oreVeinProbabilities.forEach(
                (veinInfo, probability) -> {
                    processHelperIfPossible(outputMap, veinInfo.material, mainMultiplier, probability);
                });
        }

        ArrayList<Pair<Object, Long>> outputList = new ArrayList<>();

        outputMap.forEach((material, quantity) -> outputList.add(Pair.of(material, (long) Math.floor(quantity))));

        return outputList;
    }

    private static ArrayList<FluidStack> validPlasmaGenerator(final List<Pair<Object, Long>> planetList) {

        ArrayList<FluidStack> plasmaList = new ArrayList<>();

        for (Pair<Object, Long> pair : planetList) {
            if (!(pair.getLeft() instanceof com.ruling_0.materiallib.api.Material left)) continue;
            if (VALID_PLASMAS.contains(left)) {
                plasmaList.add(MaterialUtils.plasma(left, 1));
            }
        }

        return plasmaList;
    }

    private TMap<ItemStack, Long> validDustGenerator(final ArrayList<Pair<Object, Long>> planetList) {
        TMap<ItemStack, Long> dustList = new TCustomHashMap<>(itemStackHashingStrategy);

        for (Pair<Object, Long> pair : planetList) {
            final Object mat = pair.getLeft();
            final ItemStack dust;
            if (mat instanceof com.ruling_0.materiallib.api.Material ml)
                dust = getUnificatedOreDictStack(GTOreDictUnificator.get(OrePrefixes.dust, ml, 1L));
            else dust = null;
            if (dust != null) {
                dustList.merge(dust, pair.getRight(), Long::sum);
            }
        }
        return dustList;
    }

    private static long plasmaCostCalculator(ArrayList<FluidStackLong> plasmas) {
        long total = 0;

        for (FluidStackLong plasma : plasmas) {
            FluidStack plasmaFluid = plasma.getRegularFluidStack(plasma, 1);
            try {
                String plasmaName = plasmaFluid.getFluid()
                    .getUnlocalizedName();
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

    private static final List<com.ruling_0.materiallib.api.Material> VALID_PLASMAS = Stream
        .of(
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

    private static final HashMap<String, Long> plasmaEnergyMap = new HashMap<>() {

        private static final long serialVersionUID = 7933945171103801933L;

        {
            VALID_PLASMAS.forEach(
                (material -> put(
                    MaterialUtils.plasma(material, 1)
                        .getFluid()
                        .getUnlocalizedName(),
                    (long) (getPlasmaFuelValueInEUPerLiterFromFluid(MaterialUtils.plasma(material, 1))
                        * getMaxPlasmaTurbineEfficiency()))));
        }
    };
}
