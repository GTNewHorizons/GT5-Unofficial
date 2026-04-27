package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.oreWasherRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public class RecipeGenOre extends RecipeGenBase {

    public static final Set<Runnable> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.add(mRecipeGenMap);
    }

    public RecipeGenOre(final Material M) {
        this(M, false);
    }

    public RecipeGenOre(final Material M, final boolean O) {
        this.toGenerate = M;
        this.disableOptional = O;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate, this.disableOptional);
    }

    private static Material mStone;

    private void generateRecipes(final Material material, final boolean disableOptional) {

        if (mStone == null) {
            mStone = MaterialUtils.generateMaterialFromGtENUM(Materials.Stone);
        }

        // if (material.getMaterialComposites().length > 1){
        int tVoltageMultiplier = MaterialUtils.getVoltageForTier(material.vTier);

        final ItemStack dustStone = ItemUtils.getItemStackOfAmountFromOreDict("dustStone", 1);
        Material bonusA = null; // Ni
        Material bonusB = null; // Tin

        if (!material.getComposites()
            .isEmpty()
            && material.getComposites()
                .get(0) != null) {
            bonusA = material.getComposites()
                .get(0)
                .getStackMaterial();
        } else {
            bonusA = material;
        }

        boolean allFailed = false;

        // Setup Bonuses
        ArrayList<Material> aMatComp = new ArrayList<>(MaterialUtils.getCompoundMaterialsRecursively(material));

        if (aMatComp.size() < 3) {
            while (aMatComp.size() < 3) {
                aMatComp.add(material);
            }
        }

        ArrayList<Material> amJ = new ArrayList<>();
        for (Material g : aMatComp) {
            if (g.hasSolidForm()) {
                if (getDust(g) != null && getTinyDust(g) != null) {
                    amJ.add(g);
                }
            }
        }

        if (amJ.size() < 2) {
            if (material.getComposites()
                .size() >= 2
                && material.getComposites()
                    .get(1) != null) {
                bonusB = material.getComposites()
                    .get(1)
                    .getStackMaterial();
                // If Secondary Output has no solid output, try the third (If it exists)
                if (!bonusB.hasSolidForm() && material.getComposites()
                    .size() >= 3
                    && material.getComposites()
                        .get(2) != null) {
                    bonusB = material.getComposites()
                        .get(2)
                        .getStackMaterial();
                    // If Third Output has no solid output, try the Fourth (If it exists)
                    if (!bonusB.hasSolidForm() && material.getComposites()
                        .size() >= 4
                        && material.getComposites()
                            .get(3) != null) {
                        bonusB = material.getComposites()
                            .get(3)
                            .getStackMaterial();
                        // If Fourth Output has no solid output, try the Fifth (If it exists)
                        if (!bonusB.hasSolidForm() && material.getComposites()
                            .size() >= 5
                            && material.getComposites()
                                .get(4) != null) {
                            bonusB = material.getComposites()
                                .get(4)
                                .getStackMaterial();
                            // If Fifth Output has no solid output, default out to Stone dust.
                            if (!bonusB.hasSolidForm()) {
                                allFailed = true;
                                bonusB = mStone;
                            }
                        }
                    }
                }
            } else {
                allFailed = true;
            }
        } else {
            bonusA = amJ.get(0);
            bonusB = amJ.get(1);
        }

        // Default out if it's made of fluids or some stuff.
        if (bonusA == null) {
            bonusA = tVoltageMultiplier > 100 ? material : mStone;
        }
        // Default out if it's made of fluids or some stuff.
        if (allFailed || bonusB == null) {
            bonusB = tVoltageMultiplier > 100 ? material : mStone;
        }

        ArrayList<Pair<Integer, Material>> componentMap = new ArrayList<>();
        for (MaterialStack r : material.getComposites()) {
            if (r != null) {
                componentMap.add(Pair.of(r.getPartsPerOneHundred(), r.getStackMaterial()));
            }
        }

        // Need two valid outputs
        if (bonusA == null || bonusB == null || !bonusA.hasSolidForm() || !bonusB.hasSolidForm()) {
            if (bonusA == null) {
                bonusA = mStone;
            }
            if (bonusB == null) {
                bonusB = mStone;
            }
            if (!bonusA.hasSolidForm()) {
                bonusA = mStone;
            }
            if (!bonusB.hasSolidForm()) {
                bonusB = mStone;
            }
        }

        ItemStack matDust = getDust(material);
        ItemStack matDustA = getDust(bonusA);
        ItemStack matDustB = getDust(bonusB);

        /**
         * Macerate
         */
        // Macerate ore to Crushed
        GTValues.RA.stdBuilder()
            .itemInputs(material.getOre(1))
            .itemOutputs(material.getCrushed(2))
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        // Macerate raw ore to Crushed
        GTValues.RA.stdBuilder()
            .itemInputs(material.getRawOre(1))
            .itemOutputs(material.getCrushed(2))
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        // Macerate Crushed to Impure Dust
        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushed(1))
            .itemOutputs(material.getDustImpure(1), matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        // Macerate Washed to Purified Dust
        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedPurified(1))
            .itemOutputs(material.getDustPurified(1), matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        // Macerate Centrifuged to Pure Dust
        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedCentrifuged(1))
            .itemOutputs(matDust, matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        // Wash
        RA.stdBuilder()
            .itemInputs(material.getCrushed(1))
            .itemOutputs(material.getCrushedPurified(1), matDustA, dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .fluidInputs(Materials.Water.getFluid(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(oreWasherRecipes);

        RA.stdBuilder()
            .itemInputs(material.getCrushed(1))
            .itemOutputs(material.getCrushedPurified(1), matDustA, dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .fluidInputs(GTModHandler.getDistilledWater(200))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(oreWasherRecipes);

        // Thermal Centrifuge

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushed(1))
            .itemOutputs(material.getCrushedCentrifuged(1), matDustB, dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedPurified(1))
            .itemOutputs(material.getCrushedCentrifuged(1), matDustA, dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        // Forge Hammer

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedCentrifuged(1))
            .itemOutputs(matDust)
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushed(1))
            .itemOutputs(material.getDustImpure(1))
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedPurified(1))
            .itemOutputs(material.getDustPurified(1))
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(material.getOre(1))
            .itemOutputs(material.getCrushed(1))
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);

        // Centrifuge

        // Purified Dust to Clean
        GTValues.RA.stdBuilder()
            .itemInputs(material.getDustPurified(1))
            .itemOutputs(matDust, matDustA)
            .outputChances(100_00, 11_11)
            .eut(tVoltageMultiplier / 2)
            .duration((int) Math.max(1L, material.getMass() * 8L))
            .addTo(centrifugeRecipes);

        // Impure Dust to Clean
        GTValues.RA.stdBuilder()
            .itemInputs(material.getDustImpure(1))
            .itemOutputs(matDust, matDustB)
            .outputChances(100_00, 11_11)
            .eut(tVoltageMultiplier / 2)
            .duration((int) Math.max(1L, material.getMass() * 8L))
            .addTo(centrifugeRecipes);

        // Electrolyzer

        if (!disableOptional) {
            // Process Dust
            if (!componentMap.isEmpty() && componentMap.size() <= 6) {

                ItemStack[] mInternalOutputs = new ItemStack[6];
                int[] mChances = new int[6];
                int mCellCount = 0;

                int mTotalCount = 0;

                int mCounter = 0;
                for (Pair<Integer, Material> f : componentMap) {
                    if (f.getValue()
                        .getState() != MaterialState.SOLID) {
                        mInternalOutputs[mCounter++] = f.getValue()
                            .getCell(f.getKey());
                        mCellCount += f.getKey();
                        mTotalCount += f.getKey();
                    } else {
                        mInternalOutputs[mCounter++] = f.getValue()
                            .getDust(f.getKey());
                        mTotalCount += f.getKey();
                    }
                }

                // Build Output Array
                for (int g = 0; g < mInternalOutputs.length; g++) {
                    mChances[g] = (mInternalOutputs[g] != null ? 10000 : 0);
                }

                ItemStack emptyCell = null;
                if (mCellCount > 0) {
                    emptyCell = ItemList.Cell_Empty.get(mCellCount);
                }

                ItemStack mainDust = material.getDust(material.smallestStackSizeWhenProcessing);
                if (mainDust == null) {
                    mainDust = material.getDust(mTotalCount);
                    if (mainDust == null) {
                        return;
                    }
                }

                for (int j = 0; j < mInternalOutputs.length; j++) {
                    if (mInternalOutputs[j] == null) {
                        mInternalOutputs[j] = GTValues.NI;
                    }
                }

                // i don't understand the mess above, so let's just strip nulls and assume the chances are in correct
                // order
                List<ItemStack> internalOutputs = new ArrayList<>(Arrays.asList(mInternalOutputs));
                internalOutputs.removeIf(Objects::isNull);
                int[] chances = new int[internalOutputs.size()];
                System.arraycopy(mChances, 0, chances, 0, internalOutputs.size());
                ItemStack[] inputs;
                if (emptyCell == null) {
                    inputs = new ItemStack[] { mainDust };
                } else {
                    inputs = new ItemStack[] { mainDust, emptyCell };
                }
                GTValues.RA.stdBuilder()
                    .itemInputs(inputs)
                    .itemOutputs(internalOutputs.toArray(new ItemStack[0]))
                    .outputChances(chances)
                    .duration(Math.max(material.getMass() * 3L * 1, 1))
                    .eut(tVoltageMultiplier)
                    .addTo(electrolyzerRecipes);

            } else if (componentMap.size() > 6 && componentMap.size() <= 9) {

                ItemStack[] mInternalOutputs = new ItemStack[9];
                int[] mChances = new int[9];
                int mCellCount = 0;

                int mTotalCount = 0;

                int mCounter = 0;
                for (Pair<Integer, Material> f : componentMap) {
                    if (f.getValue()
                        .getState() != MaterialState.SOLID
                        && f.getValue()
                            .getState() != MaterialState.ORE) {
                        mInternalOutputs[mCounter++] = f.getValue()
                            .getCell(f.getKey());
                        mCellCount += f.getKey();
                        mTotalCount += f.getKey();
                    } else {
                        mInternalOutputs[mCounter++] = f.getValue()
                            .getDust(f.getKey());
                        mTotalCount += f.getKey();
                    }
                }

                // Build Output Array
                for (int g = 0; g < mInternalOutputs.length; g++) {
                    mChances[g] = (mInternalOutputs[g] != null ? 10000 : 0);
                }

                ItemStack emptyCell = null;
                if (mCellCount > 0) {
                    emptyCell = ItemList.Cell_Empty.get(mCellCount);;
                }

                ItemStack mainDust = material.getDust(material.smallestStackSizeWhenProcessing);
                if (mainDust == null) {
                    mainDust = material.getDust(mTotalCount);
                    if (mainDust == null) {
                        return;
                    }
                }

                for (int j = 0; j < mInternalOutputs.length; j++) {
                    if (mInternalOutputs[j] == null) {
                        mInternalOutputs[j] = GTValues.NI;
                    }
                }

                // i don't understand the mess above, so let's just strip nulls and assume the chances are in correct
                // order
                List<ItemStack> internalOutputs = new ArrayList<>(Arrays.asList(mInternalOutputs));
                internalOutputs.removeIf(Objects::isNull);
                int[] chances = new int[internalOutputs.size()];
                System.arraycopy(mChances, 0, chances, 0, internalOutputs.size());

                ItemStack[] inputs;
                if (emptyCell == null) {
                    inputs = new ItemStack[] { mainDust };
                } else {
                    inputs = new ItemStack[] { mainDust, emptyCell };
                }

                GTValues.RA.stdBuilder()
                    .itemInputs(inputs)
                    .itemOutputs(internalOutputs.toArray(new ItemStack[0]))
                    .outputChances(chances)
                    .eut(tVoltageMultiplier)
                    .duration((int) Math.max(material.getMass() * 4L * 1, 1))
                    .addTo(chemicalDehydratorRecipes);

            }
        }

        // Shaped Crafting

        GTModHandler.addCraftingRecipe(
            material.getDustPurified(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "h  ", "P  ", "   ", 'P', material.getCrushedPurified(1) });

        GTModHandler.addCraftingRecipe(
            material.getDustImpure(1),
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "h  ", "C  ", "   ", 'C', material.getCrushed(1) });

        GTModHandler.addCraftingRecipe(
            matDust,
            GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "h  ", "C  ", "   ", 'C', material.getCrushedCentrifuged(1) });

        final ItemStack smallDust = material.getSmallDust(1);
        final ItemStack tinyDust = material.getTinyDust(1);

        if (tinyDust != null) {
            GTModHandler.addCraftingRecipe(
                matDust,
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "TTT", "TTT", "TTT", 'T', tinyDust });
            GTModHandler.addCraftingRecipe(
                material.getTinyDust(9),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "D  ", "   ", "   ", 'D', matDust });
        }

        if (smallDust != null) {
            GTModHandler.addCraftingRecipe(
                matDust,
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "SS ", "SS ", "   ", 'S', smallDust });
            GTModHandler.addCraftingRecipe(
                material.getSmallDust(4),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { " D ", "   ", "   ", 'D', matDust });
        }
    }

    public static ItemStack getTinyDust(Material m) {
        ItemStack x = m.getTinyDust(1);
        if (x == null) {
            x = mStone.getDust(1);
        }
        return x;
    }

    public static ItemStack getDust(Material m) {
        ItemStack x = m.getDust(1);
        if (x == null) {
            x = mStone.getDust(1);
        }
        return x;
    }
}
