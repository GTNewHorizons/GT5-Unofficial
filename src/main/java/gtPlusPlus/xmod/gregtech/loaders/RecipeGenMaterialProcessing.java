package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
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
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.state.MaterialState;

public class RecipeGenMaterialProcessing extends RecipeGenBase {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.add(mRecipeGenMap);
    }

    public RecipeGenMaterialProcessing(final Material M) {
        this(M, false);
    }

    public RecipeGenMaterialProcessing(final Material M, final boolean O) {
        this.toGenerate = M;
        this.disableOptional = O;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate, this.disableOptional);
    }

    private void generateRecipes(final Material material, final boolean disableOptional) {
        if (disableOptional) {
            return;
        }

        if (material.getMaterialComposites().length > 1) {
            Logger.MATERIALS("[Recipe Generator Debug] [" + material.getDefaultLocalName() + "]");
            final int tVoltageMultiplier = material.vVoltageMultiplier;
            int[] partSizes = new int[99];
            if (material.vSmallestRatio != null) {
                partSizes = new int[material.vSmallestRatio.length];
                for (int hu = 0; hu < material.vSmallestRatio.length; hu++) {
                    partSizes[hu] = (int) material.vSmallestRatio[hu];
                }
            }
            ArrayList<Pair<Integer, Material>> componentMap = new ArrayList<>();
            int alnsnfds = 0;
            for (MaterialStack r : material.getComposites()) {
                if (r != null) {
                    componentMap.add(Pair.of(partSizes[alnsnfds], r.getStackMaterial()));
                }
                alnsnfds++;
            }

            /**
             * Centrifuge
             */

            // Process Dust
            if (!componentMap.isEmpty() && componentMap.size() <= 6 && material.smallestStackSizeWhenProcessing <= 64) {
                ItemStack[] mInternalOutputs = new ItemStack[6];
                int[] mChances = new int[6];
                int mCellCount = 0;

                int mTotalCount = 0;

                int mCounter = 0;
                for (Pair<Integer, Material> f : componentMap) {
                    if (f.getValue()
                        .getState() != MaterialState.SOLID) {
                        Logger.MATERIALS(
                            "[Centrifuge] Found Fluid Component, adding " + f.getKey()
                                + " cells of "
                                + f.getValue()
                                    .getDefaultLocalName()
                                + ".");
                        mInternalOutputs[mCounter++] = f.getValue()
                            .getCell(f.getKey());
                        mCellCount += f.getKey();
                        mTotalCount += f.getKey();
                        Logger.MATERIALS(
                            "[Centrifuge] In total, adding " + mCellCount
                                + " cells for "
                                + material.getDefaultLocalName()
                                + " processing.");
                    } else {
                        Logger.MATERIALS(
                            "[Centrifuge] Found Solid Component, adding " + f.getKey()
                                + " dusts of "
                                + f.getValue()
                                    .getDefaultLocalName()
                                + ".");
                        mInternalOutputs[mCounter++] = f.getValue()
                            .getDust(f.getKey());
                        mTotalCount += f.getKey();
                    }
                }

                // Build Output Array
                for (int g = 0; g < mInternalOutputs.length; g++) {
                    Logger.MATERIALS(
                        "[Centrifuge] Is output[" + g
                            + "] valid with a chance? "
                            + (mInternalOutputs[g] != null ? 10000 : 0));
                    mChances[g] = (mInternalOutputs[g] != null ? 10000 : 0);
                }

                ItemStack emptyCell = null;
                if (mCellCount > 0) {
                    emptyCell = ItemList.Cell_Empty.get(mCellCount);
                    Logger.MATERIALS("[Centrifuge] Recipe now requires " + mCellCount + " empty cells as input.");
                }

                ItemStack mainDust = material.getDust(material.smallestStackSizeWhenProcessing);
                if (mainDust != null) {
                    Logger.MATERIALS(
                        "[Centrifuge] Recipe now requires " + material.smallestStackSizeWhenProcessing
                            + "x "
                            + mainDust.getDisplayName()
                            + " as input.");
                } else {
                    mainDust = material.getDust(mTotalCount);
                    Logger.MATERIALS("[Centrifuge] Could not find valid input dust, trying alternative.");
                    if (mainDust != null) {
                        Logger.MATERIALS(
                            "[Centrifuge] Recipe now requires " + mTotalCount
                                + "x "
                                + mainDust.getDisplayName()
                                + " as input.");
                    } else {
                        Logger.MATERIALS("[Centrifuge] Could not find valid input dust, exiting.");
                        return;
                    }
                }

                for (int j = 0; j < mInternalOutputs.length; j++) {
                    if (mInternalOutputs[j] == null) {
                        mInternalOutputs[j] = GTValues.NI;
                        Logger.MATERIALS("[Centrifuge] Set slot " + j + "  to null.");
                    } else {
                        Logger.MATERIALS(
                            "[Centrifuge] Set slot " + j + " to " + mInternalOutputs[j].getDisplayName() + ".");
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
                    .duration((tVoltageMultiplier / 10) * SECONDS)
                    .addTo(centrifugeRecipes);

                Logger.MATERIALS(
                    "[Centrifuge] Generated Centrifuge recipe for " + material.getDust(1)
                        .getDisplayName());

            } else if (componentMap.size() > 6 && componentMap.size() <= 9) {
                Logger.MATERIALS(
                    "[Issue][Electrolyzer] " + material.getDefaultLocalName()
                        + " is composed of over 6 materials, so an electrolyzer recipe for processing cannot be generated. Trying to create one for the Dehydrator instead.");

                ItemStack[] mInternalOutputs = new ItemStack[9];
                int[] mChances = new int[9];
                int mCellCount = 0;

                int mTotalCount = 0;

                int mCounter = 0;
                for (Pair<Integer, Material> f : componentMap) {
                    if (f.getValue()
                        .getState() != MaterialState.SOLID) {
                        Logger.MATERIALS(
                            "[Dehydrator] Found Fluid Component, adding " + f.getKey()
                                + " cells of "
                                + f.getValue()
                                    .getDefaultLocalName()
                                + ".");
                        mInternalOutputs[mCounter++] = f.getValue()
                            .getCell(f.getKey());
                        mCellCount += f.getKey();
                        mTotalCount += f.getKey();
                        Logger.MATERIALS(
                            "[Dehydrator] In total, adding " + mCellCount
                                + " cells for "
                                + material.getDefaultLocalName()
                                + " processing.");
                    } else {
                        Logger.MATERIALS(
                            "[Dehydrator] Found Solid Component, adding " + f.getKey()
                                + " dusts of "
                                + f.getValue()
                                    .getDefaultLocalName()
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
                    emptyCell = ItemList.Cell_Empty.get(mCellCount);
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
                    .duration(20 * (tVoltageMultiplier / 10))
                    .addTo(chemicalDehydratorRecipes);

                Logger.MATERIALS(
                    "[Dehydrator] Generated Dehydrator recipe for " + material.getDust(1)
                        .getDisplayName());
            }
        }
    }
}
