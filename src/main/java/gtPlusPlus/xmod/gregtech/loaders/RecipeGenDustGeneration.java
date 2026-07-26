package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;

public class RecipeGenDustGeneration extends RecipeGenBase {

    public static final Set<Runnable> mRecipeGenMap = new HashSet<>();

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
            GTModHandler.addCraftingRecipe(
                normalDust,
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "TTT", "TTT", "TTT", 'T', tinyDust });
            GTModHandler.addCraftingRecipe(
                M.getTinyDust(9),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "D  ", "   ", "   ", 'D', normalDust });
        }

        if (smallDust != null && normalDust != null) {
            GTModHandler.addCraftingRecipe(
                normalDust,
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { "SS ", "SS ", "   ", 'S', smallDust });
            GTModHandler.addCraftingRecipe(
                M.getSmallDust(4),
                GTModHandler.RecipeBits.BUFFERED,
                new Object[] { " D ", "   ", "   ", 'D', normalDust });
        }
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate, this.disableOptional);
    }

    private void generateRecipes(final Material material, final boolean disableOptional) {
        final ItemStack normalDust = material.getDust(1);
        final ItemStack smallDust = material.getSmallDust(1);
        final ItemStack tinyDust = material.getTinyDust(1);

        if (smallDust != null) {
            generatePackagerRecipes(material);
        }

        ItemStack ingot = material.getIngot(1);
        if (normalDust != null && ingot != null) {
            addFurnaceRecipe(material);
        }
    }

    public static boolean generatePackagerRecipes(Material aMatInfo) {
        // Small Dust → Normal Dust
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(4, aMatInfo.getSmallDust(4)), ItemList.Schematic_Dust.get(0))
            .itemOutputs(aMatInfo.getDust(1))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(packagerRecipes);

        // Tiny Dust → Normal Dust
        if (aMatInfo.getTinyDust(1) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(9, aMatInfo.getTinyDust(9)), ItemList.Schematic_Dust.get(0))
                .itemOutputs(aMatInfo.getDust(1))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(packagerRecipes);
        }

        // Normal Dust → Small Dust
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, aMatInfo.getDust(1)), ItemList.Schematic_Dust_Small.get(0))
            .itemOutputs(aMatInfo.getSmallDust(4))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(packagerRecipes);

        // Normal Dust → Tiny Dust
        if (aMatInfo.getTinyDust(1) != null) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, aMatInfo.getDust(1)), ItemList.Schematic_Dust.get(0))
                .itemOutputs(aMatInfo.getTinyDust(9))
                .duration(5 * SECONDS)
                .eut(4)
                .addTo(packagerRecipes);
        }

        return true;
    }

    private void addFurnaceRecipe(Material aMatInfo) {

        ItemStack aDust = aMatInfo.getDust(1);
        if (aMatInfo.requiresBlastFurnace()) {
            ItemStack aOutput = aMatInfo.getHotIngot(1);
            if (aOutput != null) {
                addBlastFurnaceRecipe(aMatInfo, aDust, aOutput, aMatInfo.getMeltingPointK());
            }
        } else {
            ItemStack aOutput = aMatInfo.getIngot(1);
            if (aOutput != null) {
                GTModHandler.addSmeltingAndAlloySmeltingRecipe(aDust, aOutput, false);
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
