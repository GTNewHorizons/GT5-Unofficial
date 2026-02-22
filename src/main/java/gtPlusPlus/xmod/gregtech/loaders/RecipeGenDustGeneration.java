package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGenDustGeneration extends RecipeGenBase {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.add(mRecipeGenMap);
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
            GTModHandler.addCraftingRecipe(normalDust, new Object[] { "TTT", "TTT", "TTT", 'T', tinyDust });
            GTModHandler.addCraftingRecipe(M.getTinyDust(9), new Object[] { "D  ", "   ", "   ", 'D', normalDust });
        }

        if (smallDust != null && normalDust != null) {
            GTModHandler.addCraftingRecipe(normalDust, new Object[] { "SS ", "SS ", "   ", 'S', smallDust });
            GTModHandler.addCraftingRecipe(M.getSmallDust(4), new Object[] { " D ", "   ", "   ", 'D', normalDust });
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

        if (smallDust != null && tinyDust != null) {
            generatePackagerRecipes(material);
        }

        ItemStack ingot = material.getIngot(1);
        if (normalDust != null && ingot != null) {
            addFurnaceRecipe(material);
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

        ItemStack[] cleanedInputs = Arrays.stream(inputStacks)
            .filter(Objects::nonNull)
            .toArray(ItemStack[]::new);

        // Circuit Number Declaration
        int circuitNumber = -1;

        if (inputStacks.length == 1) {
            circuitNumber = 11;
        } else if (inputStacks.length == 2) {
            circuitNumber = 12;
        } else if (inputStacks.length == 3) {
            circuitNumber = 13;
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

        // Add mixer Recipe
        GTRecipeBuilder builder = GTValues.RA.stdBuilder()
            .itemInputs(cleanedInputs)
            .itemOutputs(outputStacks);
        if (oxygen != null) {
            builder.fluidInputs(oxygen);
        }
        if (circuitNumber > 0) {
            builder.circuit(circuitNumber);
        }
        builder.duration((int) Math.max(material.getMass() * 2L, 1))
            .eut(material.vVoltageMultiplier)
            .addTo(mixerRecipes);

        Logger.WARNING("Dust Mixer Recipe: " + material.getLocalizedName() + " - Success");
    }

    public static void addMixerRecipe_Standalone(final Material material) {
        final ItemStack[] inputStacks = material.getMaterialComposites();
        final ItemStack outputStacks = material.getDust(material.smallestStackSizeWhenProcessing);
        // Is this a composite?
        if (inputStacks == null) {
            Logger.WARNING("InputStacks == NUll - " + material.getLocalizedName());
            return;
        }

        // Is this a composite?
        Logger.WARNING("mixer length: " + inputStacks.length);
        if (!((inputStacks.length >= 1) && (inputStacks.length <= 4))) {
            Logger.WARNING("InputStacks is out range 1-4 - " + material.getLocalizedName());
            return;
        }
        // Log Input items
        Logger.WARNING(ItemUtils.getArrayStackNames(inputStacks));
        final long[] inputStackSize = material.vSmallestRatio;
        Logger.WARNING("mixer is stacksizeVar not null? " + (inputStackSize != null));

        // Is smallest ratio invalid?
        if (inputStackSize == null) {
            Logger.WARNING("inputStackSize == NUll - " + material.getLocalizedName());
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

        ItemStack[] cleanedInputs = Arrays.stream(inputStacks)
            .filter(Objects::nonNull)
            .toArray(ItemStack[]::new);

        boolean addCircuit = inputStacks.length <= 3;

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
        GTRecipeBuilder builder = GTValues.RA.stdBuilder()
            .itemInputs(cleanedInputs)
            .itemOutputs(outputStacks);
        if (oxygen != null) {
            builder.fluidInputs(oxygen);
        }
        if (addCircuit) {
            builder.circuit(20);
        }
        builder.duration((int) Math.max(material.getMass() * 2L, 1))
            .eut(material.vVoltageMultiplier)
            .addTo(mixerRecipes);

        Logger.WARNING("Dust Mixer Recipe: " + material.getLocalizedName() + " - Success");

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
            .itemInputs(GTUtility.copyAmount(9, aMatInfo.getTinyDust(9)), ItemList.Schematic_Dust.get(0))
            .itemOutputs(aMatInfo.getDust(1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(packagerRecipes);

        // Normal Dust
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aMatInfo.getDust(1)), ItemList.Schematic_Dust_Small.get(0))
            .itemOutputs(aMatInfo.getSmallDust(4))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(packagerRecipes);
        return true;
    }

    private void addFurnaceRecipe(Material aMatInfo) {

        ItemStack aDust = aMatInfo.getDust(1);
        if (aMatInfo.requiresBlastFurnace()) {
            ItemStack aOutput = aMatInfo.getHotIngot(1);
            if (aOutput != null) {
                if (addBlastFurnaceRecipe(aMatInfo, aDust, aOutput, aMatInfo.getMeltingPointK())) {
                    Logger.MATERIALS("Successfully added a blast furnace recipe for " + aMatInfo.getLocalizedName());
                } else {
                    Logger.MATERIALS("Failed to add a blast furnace recipe for " + aMatInfo.getLocalizedName());
                }
            } else {
                Logger.MATERIALS("Failed to add a blast furnace recipe for " + aMatInfo.getLocalizedName());
            }
        } else {
            ItemStack aOutput = aMatInfo.getIngot(1);
            if (aOutput != null) {
                if (GTModHandler.addSmeltingAndAlloySmeltingRecipe(aDust, aOutput, false)) {
                    Logger.MATERIALS("Successfully added a furnace recipe for " + aMatInfo.getLocalizedName());
                } else {
                    Logger.MATERIALS("Failed to add a furnace recipe for " + aMatInfo.getLocalizedName());
                }
            }
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
