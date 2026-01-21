package tectech.recipe;

import static tectech.recipe.EyeOfHarmonyRecipe.processHelper;
import static tectech.recipe.TecTechRecipeMaps.eyeOfHarmonyRecipes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.math.LongMath;

import galacticgreg.api.ModDimensionDef;
import galacticgreg.api.enums.DimensionDef;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtneioreplugin.plugin.block.BlockDimensionDisplay;
import gtneioreplugin.plugin.block.ModBlocks;
import gtneioreplugin.util.DimensionHelper;
import gtneioreplugin.util.GT5OreLayerHelper;
import gtneioreplugin.util.GT5OreLayerHelper.NormalOreDimensionWrapper;
import gtneioreplugin.util.GT5OreSmallHelper;
import gtneioreplugin.util.GT5OreSmallHelper.SmallOreDimensionWrapper;
import tectech.util.FluidStackLong;
import tectech.util.ItemStackLong;

public class EyeOfHarmonyRecipeStorage {

    public static final long BILLION = LongMath.pow(10, 9);
    private static final double CHANCE_DECREASE_PER_DIMENSION = 0.05;

    // Map is unique so this is fine.
    HashMap<Block, String> blocksMapInverted = new HashMap<>() {

        private static final long serialVersionUID = -1634011860327553337L;

        {
            ModBlocks.blocks.forEach((dimString, dimBlock) -> put(dimBlock, dimString));
        }
    };

    private final HashMap<String, EyeOfHarmonyRecipe> recipeHashMap = new HashMap<>() {

        private static final long serialVersionUID = -3501819612517400500L;

        {
            for (String dimAbbreviation : DimensionHelper.getAllDisplayedNames()) {
                BlockDimensionDisplay blockDimensionDisplay = (BlockDimensionDisplay) ModBlocks.blocks
                    .get(dimAbbreviation);

                ModDimensionDef dimensionDef = DimensionDef.getDefByName(DimensionHelper.getFullName(dimAbbreviation));

                if (dimensionDef != null && !dimensionDef.hasEoHRecipe()) continue;

                if (dimAbbreviation.equals("DD")) {
                    specialDeepDarkRecipe(this, blockDimensionDisplay);
                } else {

                    NormalOreDimensionWrapper normalOre = GT5OreLayerHelper.getVeinByDim(dimAbbreviation);
                    SmallOreDimensionWrapper smallOre = GT5OreSmallHelper.getSmallOrebyDim(dimAbbreviation);

                    if (normalOre == null && smallOre == null) {
                        // No ores are generated in this dimension. Fail silently.
                        continue;
                    }

                    long spacetimeTier = blockDimensionDisplay.getDimensionRocketTier();
                    if (spacetimeTier == 0) {
                        spacetimeTier += 1;
                    }

                    put(
                        dimAbbreviation,
                        new EyeOfHarmonyRecipe(
                            normalOre,
                            smallOre,
                            blockDimensionDisplay,
                            0.6 + blockDimensionDisplay.getDimensionRocketTier() / 10.0,
                            BILLION * (blockDimensionDisplay.getDimensionRocketTier() + 1),
                            BILLION * (blockDimensionDisplay.getDimensionRocketTier() + 1),
                            timeCalculator(blockDimensionDisplay.getDimensionRocketTier()),
                            spacetimeTier - 1,
                            1.0 - CHANCE_DECREASE_PER_DIMENSION * blockDimensionDisplay.getDimensionRocketTier()));
                }
            }
        }
    };

    public EyeOfHarmonyRecipe recipeLookUp(final ItemStack aStack) {
        String dimAbbreviation = blocksMapInverted.get(Block.getBlockFromItem(aStack.getItem()));
        return recipeHashMap.get(dimAbbreviation);
    }

    public EyeOfHarmonyRecipeStorage() {

        for (EyeOfHarmonyRecipe recipe : recipeHashMap.values()) {

            ArrayList<ItemStack> outputItems = new ArrayList<>();
            for (ItemStackLong itemStackLong : recipe.getOutputItems()) {
                outputItems.add(itemStackLong.itemStack);
            }

            ArrayList<FluidStack> outputFluids = new ArrayList<>();
            for (FluidStackLong fluidStackLong : recipe.getOutputFluids()) {
                outputFluids.add(fluidStackLong.fluidStack);
            }

            ItemStack planetItem = recipe.getRecipeTriggerItem()
                .copy();
            planetItem.stackSize = 0;

            GTValues.RA.stdBuilder()
                .itemInputs(planetItem)
                .itemOutputs(outputItems.toArray(new ItemStack[0]))
                .fluidInputs(
                    Materials.Hydrogen.getGas(0),
                    Materials.Helium.getGas(0),
                    Materials.RawStarMatter.getFluid(0))
                .fluidOutputs(outputFluids.toArray(new FluidStack[0]))
                .duration(recipe.getRecipeTimeInTicks())
                .eut(0)
                .special(recipe)
                .addTo(eyeOfHarmonyRecipes);
        }
    }

    private void specialDeepDarkRecipe(final HashMap<String, EyeOfHarmonyRecipe> hashMap,
        final BlockDimensionDisplay planetItem) {

        HashSet<Materials> validMaterialSet = new HashSet<>();

        for (Materials material : Materials.values()) {

            ItemStack normalOre = GTOreDictUnificator.get(OrePrefixes.ore, material, 1);

            if ((normalOre != null)) {
                validMaterialSet.add(material);
            }

            ItemStack smallOre = GTOreDictUnificator.get(OrePrefixes.oreSmall, material, 1);

            if ((smallOre != null)) {
                validMaterialSet.add(material);
            }
        }

        ArrayList<Materials> validMaterialList = new ArrayList<>(validMaterialSet);

        long rocketTier = 9;

        hashMap.put(
            "DD",
            new EyeOfHarmonyRecipe(
                processDD(validMaterialList),
                planetItem,
                0.6 + rocketTier / 10.0,
                BILLION * (rocketTier + 1),
                BILLION * (rocketTier + 1),
                timeCalculator(rocketTier),
                rocketTier, // -1 so that we avoid out of bounds exception on NEI render.
                1.0 - rocketTier * CHANCE_DECREASE_PER_DIMENSION));
    }

    private static long timeCalculator(final long rocketTier) {
        return (long) (18_000L * GTUtility.powInt(1.4, rocketTier));
    }

    private ArrayList<Pair<Materials, Long>> processDD(final ArrayList<Materials> validMaterialList) {
        EyeOfHarmonyRecipe.HashMapHelper outputMap = new EyeOfHarmonyRecipe.HashMapHelper();

        // 10 from rocketTier + 1, 6 * 64 = VM3 + Og, 1.4 = time increase per tier.
        double mainMultiplier = (timeCalculator(10) * (6 * 64));
        double probability = 1.0 / validMaterialList.size();

        validMaterialList.forEach((material) -> processHelper(outputMap, material, mainMultiplier, probability));

        ArrayList<Pair<Materials, Long>> outputList = new ArrayList<>();

        outputMap.forEach((material, quantity) -> outputList.add(Pair.of(material, (long) Math.floor(quantity))));

        return outputList;
    }
}
