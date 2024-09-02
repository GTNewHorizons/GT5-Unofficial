package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;

public class RecipeGenDustGeneration extends RecipeGenBase {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
    }

    public RecipeGenDustGeneration(final Material M) {
        this(M, false);
    }

    public RecipeGenDustGeneration(final Material M, final boolean O) {
        this.toGenerate = M;
        this.disableOptional = O;
        mRecipeGenMap.add(this);
        final ItemStack normalDust = M.getDust(1);
        final ItemStack smallDust = M.getSmallDust(1);
        final ItemStack tinyDust = M.getTinyDust(1);
        if (tinyDust != null && normalDust != null) {
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
                Logger.INFO("9 Tiny dust to 1 Dust Recipe: " + M.getLocalizedName() + " - Success");
            } else {
                Logger.INFO("9 Tiny dust to 1 Dust Recipe: " + M.getLocalizedName() + " - Failed");
            }

            if (RecipeUtils
                .addShapedRecipe(normalDust, null, null, null, null, null, null, null, null, M.getTinyDust(9))) {
                Logger.INFO("9 Tiny dust from 1 Recipe: " + M.getLocalizedName() + " - Success");
            } else {
                Logger.INFO("9 Tiny dust from 1 Recipe: " + M.getLocalizedName() + " - Failed");
            }
        }

        if (smallDust != null && normalDust != null) {
            if (RecipeUtils.addShapedRecipe(
                smallDust,
                smallDust,
                null,
                smallDust,
                smallDust,
                null,
                null,
                null,
                null,
                normalDust)) {
                Logger.INFO("4 Small dust to 1 Dust Recipe: " + M.getLocalizedName() + " - Success");
            } else {
                Logger.INFO("4 Small dust to 1 Dust Recipe: " + M.getLocalizedName() + " - Failed");
            }
            if (RecipeUtils
                .addShapedRecipe(null, normalDust, null, null, null, null, null, null, null, M.getSmallDust(4))) {
                Logger.INFO("4 Small dust from 1 Dust Recipe: " + M.getLocalizedName() + " - Success");
            } else {
                Logger.INFO("4 Small dust from 1 Dust Recipe: " + M.getLocalizedName() + " - Failed");
            }
        }
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate, this.disableOptional);
    }

    private void generateRecipes(final Material material, final boolean disableOptional) {

        Logger.INFO("Generating Shaped Crafting recipes for " + material.getLocalizedName());

        final ItemStack normalDust = material.getDust(1);
        final ItemStack smallDust = material.getSmallDust(1);
        final ItemStack tinyDust = material.getTinyDust(1);

        final ItemStack[] inputStacks = material.getMaterialComposites();
        final ItemStack outputStacks = material.getDust(material.smallestStackSizeWhenProcessing);

        // Macerate blocks back to dusts.
        final ItemStack materialBlock = material.getBlock(1);
        final ItemStack materialFrameBox = material.getFrameBox(1);

        if (ItemUtils.checkForInvalidItems(materialBlock)) {
            RA.stdBuilder()
                .itemInputs(materialBlock)
                .itemOutputs(material.getDust(9))
                .eut(2)
                .duration(20 * SECONDS)
                .addTo(maceratorRecipes);
        }

        if (ItemUtils.checkForInvalidItems(materialFrameBox)) {
            RA.stdBuilder()
                .itemInputs(materialFrameBox)
                .itemOutputs(material.getDust(2))
                .eut(2)
                .duration(20 * SECONDS)
                .addTo(maceratorRecipes);
        }

        if (ItemUtils.checkForInvalidItems(smallDust) && ItemUtils.checkForInvalidItems(tinyDust)) {
            generatePackagerRecipes(material);
        }

        ItemStack ingot = material.getIngot(1);
        if (ItemUtils.checkForInvalidItems(normalDust) && ItemUtils.checkForInvalidItems(ingot)) {
            addFurnaceRecipe(material);
            addMacerationRecipe(material);
        }

        // Is this a composite?
        if ((inputStacks == null) || disableOptional) {
            return;
        }

        // Is this a composite?
        Logger.WARNING("mixer length: " + inputStacks.length);
        if (!((inputStacks.length != 0) && (inputStacks.length <= 4))) {
            return;
        }
        // Log Input items
        Logger.WARNING(ItemUtils.getArrayStackNames(inputStacks));
        final long[] inputStackSize = material.vSmallestRatio;
        Logger.WARNING("mixer is stacksizeVar null? " + (inputStackSize != null));
        // Is smallest ratio invalid?
        if (inputStackSize == null) {
            return;
        }
        // set stack sizes on an input ItemStack[]
        for (short x = 0; x < inputStacks.length; x++) {
            if ((inputStacks[x] != null) && (inputStackSize[x] != 0)) {
                inputStacks[x].stackSize = (int) inputStackSize[x];
            }
        }
        // Relog input values, with stack sizes
        Logger.WARNING(ItemUtils.getArrayStackNames(inputStacks));

        // Get us four ItemStacks to input into the mixer
        ItemStack[] input = new ItemStack[4];

        input[0] = (inputStacks.length >= 1) ? ((inputStacks[0] == null) ? null : inputStacks[0]) : null;
        input[1] = (inputStacks.length >= 2) ? ((inputStacks[1] == null) ? null : inputStacks[1]) : null;
        input[2] = (inputStacks.length >= 3) ? ((inputStacks[2] == null) ? null : inputStacks[2]) : null;
        input[3] = (inputStacks.length >= 4) ? ((inputStacks[3] == null) ? null : inputStacks[3]) : null;

        if (inputStacks.length == 1) {
            input[1] = input[0];
            input[0] = GTUtility.getIntegratedCircuit(inputStacks.length + 10);
        } else if (inputStacks.length == 2) {
            input[2] = input[1];
            input[1] = input[0];
            input[0] = GTUtility.getIntegratedCircuit(inputStacks.length + 10);

        } else if (inputStacks.length == 3) {
            input[3] = input[2];
            input[2] = input[1];
            input[1] = input[0];
            input[0] = GTUtility.getIntegratedCircuit(inputStacks.length + 10);
        }

        // Add mixer Recipe
        FluidStack oxygen = GTValues.NF;
        if (material.getComposites() != null) {
            for (final MaterialStack x : material.getComposites()) {
                if (material.getComposites()
                    .isEmpty()) {
                    continue;
                }
                if (x == null) {
                    continue;
                }
                if (x.getStackMaterial() == null) {
                    continue;
                }

                if (x.getStackMaterial()
                    .getDust(1) != null) {
                    continue;
                }

                if (x.getStackMaterial()
                    .getState() != MaterialState.SOLID
                    && x.getStackMaterial()
                        .getState() != MaterialState.ORE
                    && x.getStackMaterial()
                        .getState() != MaterialState.PLASMA) {
                    oxygen = x.getStackMaterial()
                        .getFluidStack(1000);
                    break;
                }
            }
        }

        input = ItemUtils.cleanItemStackArray(input);

        // Add mixer Recipe
        List<ItemStack> inputs = new ArrayList<>(Arrays.asList(input));
        inputs.removeIf(Objects::isNull);

        if (oxygen == null) {
            GTValues.RA.stdBuilder()
                .itemInputs(inputs.toArray(new ItemStack[0]))
                .itemOutputs(outputStacks)
                .duration((int) Math.max(material.getMass() * 2L * 1, 1))
                .eut(material.vVoltageMultiplier)
                .addTo(mixerRecipes);
        } else {
            GTValues.RA.stdBuilder()
                .itemInputs(inputs.toArray(new ItemStack[0]))
                .itemOutputs(outputStacks)
                .fluidInputs(oxygen)
                .duration((int) Math.max(material.getMass() * 2L * 1, 1))
                .eut(material.vVoltageMultiplier)
                .addTo(mixerRecipes);
        }

        Logger.WARNING("Dust Mixer Recipe: " + material.getLocalizedName() + " - Success");
    }

    public static boolean addMixerRecipe_Standalone(final Material material) {
        final ItemStack[] inputStacks = material.getMaterialComposites();
        final ItemStack outputStacks = material.getDust(material.smallestStackSizeWhenProcessing);
        // Is this a composite?
        if (inputStacks == null) {
            Logger.WARNING("InputStacks == NUll - " + material.getLocalizedName());
            return false;
        }

        // Is this a composite?
        Logger.WARNING("mixer length: " + inputStacks.length);
        if (!((inputStacks.length >= 1) && (inputStacks.length <= 4))) {
            Logger.WARNING("InputStacks is out range 1-4 - " + material.getLocalizedName());
            return false;
        }
        // Log Input items
        Logger.WARNING(ItemUtils.getArrayStackNames(inputStacks));
        final long[] inputStackSize = material.vSmallestRatio;
        Logger.WARNING("mixer is stacksizeVar not null? " + (inputStackSize != null));

        // Is smallest ratio invalid?
        if (inputStackSize == null) {
            Logger.WARNING("inputStackSize == NUll - " + material.getLocalizedName());
            return true;
        }

        // set stack sizes on an input ItemStack[]
        for (short x = 0; x < inputStacks.length; x++) {
            if ((inputStacks[x] != null) && (inputStackSize[x] != 0)) {
                inputStacks[x].stackSize = (int) inputStackSize[x];
            }
        }

        // Relog input values, with stack sizes
        Logger.WARNING(ItemUtils.getArrayStackNames(inputStacks));

        // Get us four ItemStacks to input into the mixer
        ItemStack input1, input2, input3, input4;
        input1 = inputStacks[0];
        input2 = (inputStacks.length >= 2) ? (input2 = (inputStacks[1] == null) ? null : inputStacks[1]) : null;
        input3 = (inputStacks.length >= 3) ? (input3 = (inputStacks[2] == null) ? null : inputStacks[2]) : null;
        input4 = (inputStacks.length >= 4) ? (input4 = (inputStacks[3] == null) ? null : inputStacks[3]) : null;

        if (inputStacks.length == 1) {
            input2 = input1;
            input1 = GTUtility.getIntegratedCircuit(20);
        } else if (inputStacks.length == 2) {
            input3 = input2;
            input2 = input1;
            input1 = GTUtility.getIntegratedCircuit(20);

        } else if (inputStacks.length == 3) {
            input4 = input3;
            input3 = input2;
            input2 = input1;
            input1 = GTUtility.getIntegratedCircuit(20);
        }

        // Add mixer Recipe
        FluidStack oxygen = GTValues.NF;
        if (material.getComposites() != null) {
            int compSlot = 0;
            for (final MaterialStack x : material.getComposites()) {

                if (material.getComposites()
                    .isEmpty()) {
                    compSlot++;
                    continue;
                }
                if (x == null) {
                    compSlot++;
                    continue;
                }

                if (x.getStackMaterial() == null) {
                    compSlot++;
                    continue;
                }

                if (x.getStackMaterial()
                    .getDust(1) == null) {
                    compSlot++;
                    continue;
                }

                MaterialState f = x.getStackMaterial()
                    .getState();
                if (f == MaterialState.GAS || f == MaterialState.LIQUID
                    || f == MaterialState.PURE_LIQUID
                    || f == MaterialState.PURE_GAS) {
                    oxygen = x.getStackMaterial()
                        .getFluidStack((int) (material.vSmallestRatio[compSlot] * 1000));
                }
                compSlot++;
            }
        }

        // Add mixer Recipe
        try {
            if (oxygen == null) {
                GTValues.RA.stdBuilder()
                    .itemInputs(input1, input2, input3, input4)
                    .itemOutputs(outputStacks)
                    .duration((int) Math.max(material.getMass() * 2L * 1, 1))
                    .eut(material.vVoltageMultiplier)
                    .addTo(mixerRecipes);
            } else {
                GTValues.RA.stdBuilder()
                    .itemInputs(input1, input2, input3, input4)
                    .itemOutputs(outputStacks)
                    .fluidInputs(oxygen)
                    .duration((int) Math.max(material.getMass() * 2L * 1, 1))
                    .eut(material.vVoltageMultiplier)
                    .addTo(mixerRecipes);
            }

            Logger.WARNING("Dust Mixer Recipe: " + material.getLocalizedName() + " - Success");

        } catch (Throwable t) {
            t.printStackTrace();
        }
        return true;
    }

    public static boolean generatePackagerRecipes(Material aMatInfo) {
        // Small Dust
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(4, aMatInfo.getSmallDust(4)), ItemList.Schematic_Dust.get(0))
            .itemOutputs(aMatInfo.getDust(1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(packagerRecipes);

        // Tiny Dust
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(4, aMatInfo.getTinyDust(9)), ItemList.Schematic_Dust.get(0))
            .itemOutputs(aMatInfo.getDust(1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(packagerRecipes);
        return true;
    }

    private void addMacerationRecipe(Material aMatInfo) {
        try {
            Logger.MATERIALS("Adding Maceration recipe for " + aMatInfo.getLocalizedName() + " Ingot -> Dusts");
            RA.stdBuilder()
                .itemInputs(aMatInfo.getIngot(1))
                .itemOutputs(aMatInfo.getDust(1))
                .eut(2)
                .duration(20 * SECONDS)
                .addTo(maceratorRecipes);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private void addFurnaceRecipe(Material aMatInfo) {

        ItemStack aDust = aMatInfo.getDust(1);
        ItemStack aOutput;
        try {
            if (aMatInfo.requiresBlastFurnace()) {
                aOutput = aMatInfo.getHotIngot(1);
                if (ItemUtils.checkForInvalidItems(aOutput)) {
                    if (addBlastFurnaceRecipe(aMatInfo, aDust, aOutput, aMatInfo.getMeltingPointK())) {
                        Logger
                            .MATERIALS("Successfully added a blast furnace recipe for " + aMatInfo.getLocalizedName());
                    } else {
                        Logger.MATERIALS("Failed to add a blast furnace recipe for " + aMatInfo.getLocalizedName());
                    }
                } else {
                    Logger.MATERIALS("Failed to add a blast furnace recipe for " + aMatInfo.getLocalizedName());
                }
            } else {
                aOutput = aMatInfo.getIngot(1);
                if (ItemUtils.checkForInvalidItems(aOutput)) {
                    if (GTModHandler.addSmeltingAndAlloySmeltingRecipe(aDust, aOutput, false)) {
                        Logger.MATERIALS("Successfully added a furnace recipe for " + aMatInfo.getLocalizedName());
                    } else {
                        Logger.MATERIALS("Failed to add a furnace recipe for " + aMatInfo.getLocalizedName());
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    private boolean addBlastFurnaceRecipe(Material aMatInfo, final ItemStack input1, final ItemStack output1,
        final int tempRequired) {

        int timeTaken;
        if (aMatInfo.vTier <= 4) {
            timeTaken = 25 * aMatInfo.vTier * 10;
        } else {
            timeTaken = 125 * aMatInfo.vTier * 10;
        }

        long aVoltage = aMatInfo.vVoltageMultiplier;

        GTValues.RA.stdBuilder()
            .itemInputs(input1)
            .itemOutputs(output1)
            .duration(timeTaken)
            .eut(aVoltage)
            .metadata(COIL_HEAT, tempRequired)
            .addTo(blastFurnaceRecipes);
        return true;

    }
}
