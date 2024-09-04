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

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;

public class RecipeGenOre extends RecipeGenBase {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
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
        Logger.MATERIALS("[Recipe Generator Debug] [" + material.getLocalizedName() + "]");
        int tVoltageMultiplier = MaterialUtils.getVoltageForTier(material.vTier);

        final ItemStack dustStone = ItemUtils.getItemStackOfAmountFromOreDict("dustStone", 1);
        Material bonusA = null; // Ni
        Material bonusB = null; // Tin

        if (material.getComposites()
            .size() >= 1
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
        ArrayList<Material> aMatComp = new ArrayList<>();
        for (Material j : MaterialUtils.getCompoundMaterialsRecursively(material)) {
            aMatComp.add(j);
        }

        if (aMatComp.size() < 3) {
            while (aMatComp.size() < 3) {
                aMatComp.add(material);
            }
        }

        AutoMap<Material> amJ = new AutoMap<>();
        int aIndexCounter = 0;
        for (Material g : aMatComp) {
            if (g.hasSolidForm()) {
                if (getDust(g) != null && getTinyDust(g) != null) {
                    amJ.put(g);
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

        AutoMap<Pair<Integer, Material>> componentMap = new AutoMap<>();
        for (MaterialStack r : material.getComposites()) {
            if (r != null) {
                componentMap.put(new Pair<>(r.getPartsPerOneHundred(), r.getStackMaterial()));
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
         * Package
         */
        // Allow ore dusts to be packaged
        if (ItemUtils.checkForInvalidItems(material.getSmallDust(1))
            && ItemUtils.checkForInvalidItems(material.getTinyDust(1))) {
            RecipeGenDustGeneration.generatePackagerRecipes(material);
        }

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

        Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate ore to Crushed ore'");

        // Macerate raw ore to Crushed
        GTValues.RA.stdBuilder()
            .itemInputs(material.getRawOre(1))
            .itemOutputs(material.getCrushed(2))
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate raw ore to Crushed ore'");

        // Macerate Crushed to Impure Dust
        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushed(1))
            .itemOutputs(material.getDustImpure(1), matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate Crushed ore to Impure Dust'");

        // Macerate Washed to Purified Dust
        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedPurified(1))
            .itemOutputs(material.getDustPurified(1), matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate Washed ore to Purified Dust'");

        // Macerate Centrifuged to Pure Dust
        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedCentrifuged(1))
            .itemOutputs(matDust, matDustA)
            .outputChances(100_00, 10_00)
            .duration(20 * SECONDS)
            .eut(tVoltageMultiplier / 2)
            .addTo(maceratorRecipes);

        Logger.MATERIALS("[Macerator] Added Recipe: 'Macerate Centrifuged ore to Pure Dust'");

        // Wash
        RA.stdBuilder()
            .itemInputs(material.getCrushed(1))
            .itemOutputs(material.getCrushedPurified(1), matDustA, dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .fluidInputs(GTModHandler.getWater(1000))
            .duration(25 * SECONDS)
            .eut(16)
            .addTo(oreWasherRecipes);

        RA.stdBuilder()
            .itemInputs(material.getCrushed(1))
            .itemOutputs(material.getCrushedPurified(1), matDustA, dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .fluidInputs(GTModHandler.getDistilledWater(200))
            .duration(15 * SECONDS)
            .eut(16)
            .addTo(oreWasherRecipes);
        Logger.MATERIALS("[OreWasher] Added Recipe: 'Wash Crushed ore into Purified Crushed ore'");

        // Thermal Centrifuge

        Logger.MATERIALS("material.getCrushed(1): " + (material.getCrushed(1) != null));
        Logger.MATERIALS("material.getCrushedPurified(1): " + (material.getCrushedPurified(1) != null));

        Logger.MATERIALS("material.getTinyDust(1): " + (ItemUtils.getItemName(bonusA.getCrushed(1))));
        Logger.MATERIALS("material.getTinyDust(1): " + (ItemUtils.getItemName(bonusB.getCrushed(1))));

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushed(1))
            .itemOutputs(material.getCrushedCentrifuged(1), matDustB, dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        Logger.MATERIALS(
            "[ThermalCentrifuge] Added Recipe: 'Crushed ore to Centrifuged Ore' | Input: " + material.getCrushed(1)
                .getDisplayName()
                + " | Outputs: "
                + material.getCrushedCentrifuged(1)
                    .getDisplayName()
                + ", "
                + matDustB.getDisplayName()
                + ", "
                + dustStone.getDisplayName()
                + ".");

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedPurified(1))
            .itemOutputs(material.getCrushedCentrifuged(1), matDustA, dustStone)
            .outputChances(100_00, 11_11, 100_00)
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        Logger.MATERIALS(
            "[ThermalCentrifuge] Added Recipe: 'Washed ore to Centrifuged Ore' | Input: "
                + material.getCrushedPurified(1)
                    .getDisplayName()
                + " | Outputs: "
                + material.getCrushedCentrifuged(1)
                    .getDisplayName()
                + ", "
                + matDustA.getDisplayName()
                + ", "
                + dustStone.getDisplayName()
                + ".");

        // Forge Hammer

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedCentrifuged(1))
            .itemOutputs(matDust)
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);

        Logger.MATERIALS("[ForgeHammer] Added Recipe: 'Crushed Centrifuged to Pure Dust'");

        GTValues.RA.stdBuilder()
            .itemInputs(material.getCrushedPurified(1))
            .itemOutputs(material.getDustPurified(1))
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);
        Logger.MATERIALS("[ForgeHammer] Added Recipe: 'Crushed Purified to Purified Dust'");

        GTValues.RA.stdBuilder()
            .itemInputs(material.getOre(1))
            .itemOutputs(material.getCrushed(1))
            .duration(10 * TICKS)
            .eut(tVoltageMultiplier / 4)
            .addTo(hammerRecipes);

        Logger.MATERIALS("[ForgeHammer] Added Recipe: 'Ore to Crushed'");

        // Centrifuge

        // Purified Dust to Clean
        GTValues.RA.stdBuilder()
            .itemInputs(material.getDustPurified(1))
            .itemOutputs(matDust, matDustA)
            .outputChances(100_00, 11_11)
            .eut(tVoltageMultiplier / 2)
            .duration((int) Math.max(1L, material.getMass() * 8L))
            .addTo(centrifugeRecipes);

        Logger.MATERIALS("[Centrifuge] Added Recipe: Purified Dust to Clean Dust");

        // Impure Dust to Clean
        GTValues.RA.stdBuilder()
            .itemInputs(material.getDustImpure(1))
            .itemOutputs(matDust, matDustB)
            .outputChances(100_00, 11_11)
            .eut(tVoltageMultiplier / 2)
            .duration((int) Math.max(1L, material.getMass() * 8L))
            .addTo(centrifugeRecipes);

        Logger.MATERIALS("[Centrifuge] Added Recipe: Inpure Dust to Clean Dust");

        // Electrolyzer

        if (!disableOptional) {
            // Process Dust
            if (componentMap.size() > 0 && componentMap.size() <= 6) {

                ItemStack mInternalOutputs[] = new ItemStack[6];
                int mChances[] = new int[6];
                int mCellCount = 0;

                int mTotalCount = 0;

                int mCounter = 0;
                for (Pair<Integer, Material> f : componentMap) {
                    if (f.getValue()
                        .getState() != MaterialState.SOLID) {
                        Logger.MATERIALS(
                            "[Electrolyzer] Found Fluid Component, adding " + f.getKey()
                                + " cells of "
                                + f.getValue()
                                    .getLocalizedName()
                                + ".");
                        mInternalOutputs[mCounter++] = f.getValue()
                            .getCell(f.getKey());
                        mCellCount += f.getKey();
                        mTotalCount += f.getKey();
                        Logger.MATERIALS(
                            "[Electrolyzer] In total, adding " + mCellCount
                                + " cells for "
                                + material.getLocalizedName()
                                + " processing.");
                    } else {
                        Logger.MATERIALS(
                            "[Electrolyzer] Found Solid Component, adding " + f.getKey()
                                + " dusts of "
                                + f.getValue()
                                    .getLocalizedName()
                                + ".");
                        mInternalOutputs[mCounter++] = f.getValue()
                            .getDust(f.getKey());
                        mTotalCount += f.getKey();
                    }
                }

                // Build Output Array
                for (int g = 0; g < mInternalOutputs.length; g++) {
                    Logger.MATERIALS(
                        "[Electrolyzer] Is output[" + g
                            + "] valid with a chance? "
                            + (mInternalOutputs[g] != null ? 10000 : 0));
                    mChances[g] = (mInternalOutputs[g] != null ? 10000 : 0);
                }

                ItemStack emptyCell = null;
                if (mCellCount > 0) {
                    emptyCell = ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", mCellCount);
                    Logger.MATERIALS("[Electrolyzer] Recipe now requires " + mCellCount + " empty cells as input.");
                }

                ItemStack mainDust = material.getDust(material.smallestStackSizeWhenProcessing);
                if (mainDust != null) {
                    Logger.MATERIALS(
                        "[Electrolyzer] Recipe now requires " + material.smallestStackSizeWhenProcessing
                            + "x "
                            + mainDust.getDisplayName()
                            + " as input.");
                } else {
                    mainDust = material.getDust(mTotalCount);
                    Logger.MATERIALS("[Electrolyzer] Could not find valid input dust, trying alternative.");
                    if (mainDust != null) {
                        Logger.MATERIALS(
                            "[Electrolyzer] Recipe now requires " + mTotalCount
                                + "x "
                                + mainDust.getDisplayName()
                                + " as input.");
                    } else {
                        Logger.MATERIALS("[Electrolyzer] Could not find valid input dust, exiting.");
                        return;
                    }
                }

                for (int j = 0; j < mInternalOutputs.length; j++) {
                    if (mInternalOutputs[j] == null) {
                        mInternalOutputs[j] = GTValues.NI;
                        Logger.MATERIALS("[Electrolyzer] Set slot " + j + "  to null.");
                    } else {
                        Logger.MATERIALS(
                            "[Electrolyzer] Set slot " + j + " to " + mInternalOutputs[j].getDisplayName() + ".");
                    }
                }

                // i don't understand the mess above, so let's just strip nulls and assume the chances are in correct
                // order
                List<ItemStack> internalOutputs = new ArrayList<>(Arrays.asList(mInternalOutputs));
                internalOutputs.removeIf(Objects::isNull);
                int[] chances = new int[internalOutputs.size()];
                for (int i = 0; i < internalOutputs.size(); i++) {
                    chances[i] = mChances[i];
                }
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

                Logger.MATERIALS("[Electrolyzer] Generated Electrolyzer recipe for " + matDust.getDisplayName());

            } else if (componentMap.size() > 6 && componentMap.size() <= 9) {
                Logger.MATERIALS(
                    "[Issue][Electrolyzer] " + material.getLocalizedName()
                        + " is composed of over 6 materials, so an electrolyzer recipe for processing cannot be generated. Trying to create one for the Dehydrator instead.");

                ItemStack mInternalOutputs[] = new ItemStack[9];
                int mChances[] = new int[9];
                int mCellCount = 0;

                int mTotalCount = 0;

                int mCounter = 0;
                for (Pair<Integer, Material> f : componentMap) {
                    if (f.getValue()
                        .getState() != MaterialState.SOLID
                        && f.getValue()
                            .getState() != MaterialState.ORE) {
                        Logger.MATERIALS(
                            "[Dehydrator] Found Fluid Component, adding " + f.getKey()
                                + " cells of "
                                + f.getValue()
                                    .getLocalizedName()
                                + ".");
                        mInternalOutputs[mCounter++] = f.getValue()
                            .getCell(f.getKey());
                        mCellCount += f.getKey();
                        mTotalCount += f.getKey();
                        Logger.MATERIALS(
                            "[Dehydrator] In total, adding " + mCellCount
                                + " cells for "
                                + material.getLocalizedName()
                                + " processing.");
                    } else {
                        Logger.MATERIALS(
                            "[Dehydrator] Found Solid Component, adding " + f.getKey()
                                + " dusts of "
                                + f.getValue()
                                    .getLocalizedName()
                                + ".");
                        mInternalOutputs[mCounter++] = f.getValue()
                            .getDust(f.getKey());
                        mTotalCount += f.getKey();
                    }
                }

                // Build Output Array
                for (int g = 0; g < mInternalOutputs.length; g++) {
                    Logger.MATERIALS(
                        "[Dehydrator] Is output[" + g
                            + "] valid with a chance? "
                            + (mInternalOutputs[g] != null ? 10000 : 0));
                    mChances[g] = (mInternalOutputs[g] != null ? 10000 : 0);
                }

                ItemStack emptyCell = null;
                if (mCellCount > 0) {
                    emptyCell = ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", mCellCount);
                    Logger.MATERIALS("[Dehydrator] Recipe now requires " + mCellCount + " empty cells as input.");
                }

                ItemStack mainDust = material.getDust(material.smallestStackSizeWhenProcessing);
                if (mainDust != null) {
                    Logger.MATERIALS(
                        "[Dehydrator] Recipe now requires " + material.smallestStackSizeWhenProcessing
                            + "x "
                            + mainDust.getDisplayName()
                            + " as input.");
                } else {
                    mainDust = material.getDust(mTotalCount);
                    Logger.MATERIALS("[Dehydrator] Could not find valid input dust, trying alternative.");
                    if (mainDust != null) {
                        Logger.MATERIALS(
                            "[Dehydrator] Recipe now requires " + mTotalCount
                                + "x "
                                + mainDust.getDisplayName()
                                + " as input.");
                    } else {
                        Logger.MATERIALS("[Dehydrator] Could not find valid input dust, exiting.");
                        return;
                    }
                }

                for (int j = 0; j < mInternalOutputs.length; j++) {
                    if (mInternalOutputs[j] == null) {
                        mInternalOutputs[j] = GTValues.NI;
                        Logger.MATERIALS("[Dehydrator] Set slot " + j + "  to null.");
                    } else {
                        Logger.MATERIALS(
                            "[Dehydrator] Set slot " + j + " to " + mInternalOutputs[j].getDisplayName() + ".");
                    }
                }

                // i don't understand the mess above, so let's just strip nulls and assume the chances are in correct
                // order
                List<ItemStack> internalOutputs = new ArrayList<>(Arrays.asList(mInternalOutputs));
                internalOutputs.removeIf(Objects::isNull);
                int[] chances = new int[internalOutputs.size()];
                for (int i = 0; i < internalOutputs.size(); i++) {
                    chances[i] = mChances[i];
                }

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

                Logger.MATERIALS("[Dehydrator] Generated Dehydrator recipe for " + matDust.getDisplayName());
                Logger.MATERIALS(
                    "Inputs: " + mainDust.getDisplayName()
                        + " x"
                        + mainDust.stackSize
                        + ", "
                        + (emptyCell == null ? "No Cells"
                            : "" + emptyCell.getDisplayName() + " x" + emptyCell.stackSize));
                Logger.MATERIALS("Outputs " + ItemUtils.getArrayStackNames(mInternalOutputs));
                Logger.MATERIALS("Time: " + ((int) Math.max(material.getMass() * 4L * 1, 1)));
                Logger.MATERIALS("EU: " + tVoltageMultiplier);

            }
        }

        // Shaped Crafting

        RecipeUtils.addShapedRecipe(
            CI.craftingToolHammer_Hard,
            null,
            null,
            material.getCrushedPurified(1),
            null,
            null,
            null,
            null,
            null,
            material.getDustPurified(1));

        RecipeUtils.addShapedRecipe(
            CI.craftingToolHammer_Hard,
            null,
            null,
            material.getCrushed(1),
            null,
            null,
            null,
            null,
            null,
            material.getDustImpure(1));

        RecipeUtils.addShapedRecipe(
            CI.craftingToolHammer_Hard,
            null,
            null,
            material.getCrushedCentrifuged(1),
            null,
            null,
            null,
            null,
            null,
            matDust);

        final ItemStack normalDust = matDust;
        final ItemStack smallDust = material.getSmallDust(1);
        final ItemStack tinyDust = material.getTinyDust(1);

        if (RecipeUtils.addShapedRecipe(
            tinyDust,
            tinyDust,
            tinyDust,
            tinyDust,
            tinyDust,
            tinyDust,
            tinyDust,
            tinyDust,
            tinyDust,
            normalDust)) {
            Logger.WARNING("9 Tiny dust to 1 Dust Recipe: " + material.getLocalizedName() + " - Success");
        } else {
            Logger.WARNING("9 Tiny dust to 1 Dust Recipe: " + material.getLocalizedName() + " - Failed");
        }

        if (RecipeUtils
            .addShapedRecipe(normalDust, null, null, null, null, null, null, null, null, material.getTinyDust(9))) {
            Logger.WARNING("9 Tiny dust from 1 Recipe: " + material.getLocalizedName() + " - Success");
        } else {
            Logger.WARNING("9 Tiny dust from 1 Recipe: " + material.getLocalizedName() + " - Failed");
        }

        if (RecipeUtils
            .addShapedRecipe(smallDust, smallDust, null, smallDust, smallDust, null, null, null, null, normalDust)) {
            Logger.WARNING("4 Small dust to 1 Dust Recipe: " + material.getLocalizedName() + " - Success");
        } else {
            Logger.WARNING("4 Small dust to 1 Dust Recipe: " + material.getLocalizedName() + " - Failed");
        }

        if (RecipeUtils
            .addShapedRecipe(null, normalDust, null, null, null, null, null, null, null, material.getSmallDust(4))) {
            Logger.WARNING("4 Small dust from 1 Dust Recipe: " + material.getLocalizedName() + " - Success");
        } else {
            Logger.WARNING("4 Small dust from 1 Dust Recipe: " + material.getLocalizedName() + " - Failed");
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
