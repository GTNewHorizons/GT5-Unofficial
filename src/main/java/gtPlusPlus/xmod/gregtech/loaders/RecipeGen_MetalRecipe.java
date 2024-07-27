package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.compressorRecipes;
import static gregtech.api.recipe.RecipeMaps.cutterRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.latheRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import java.util.HashSet;
import java.util.Set;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGen_MetalRecipe extends RecipeGen_Base {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
    }

    public RecipeGen_MetalRecipe(final Material M) {
        this.toGenerate = M;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    private void generateRecipes(final Material material) {

        Logger.WARNING("Generating Metal recipes for " + material.getLocalizedName());
        if (ItemUtils.checkForInvalidItems(material.getIngot(1))
            && ItemUtils.checkForInvalidItems(material.getBlock(1))) {

            GT_Values.RA.stdBuilder()
                .itemInputs(material.getIngot(9))
                .itemOutputs(material.getBlock(1))
                .duration(15 * SECONDS)
                .eut(2)
                .addTo(compressorRecipes);
            Logger.WARNING("Compress Block Recipe: " + material.getLocalizedName() + " - Success");

        }

        if (ItemUtils.checkForInvalidItems(material.getIngot(1))
            && ItemUtils.checkForInvalidItems(material.getRod(1))) {
            GT_Values.RA.stdBuilder()
                .itemInputs(material.getIngot(1))
                .itemOutputs(material.getRod(1), material.getSmallDust(2))
                .duration(Math.max(material.getMass() / 8L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(latheRecipes);

            Logger.WARNING("Lathe Rod Recipe: " + material.getLocalizedName() + " - Success");
        }

        if (ItemUtils.checkForInvalidItems(material.getRod(1)) && ItemUtils.checkForInvalidItems(material.getBolt(1))) {
            GT_Values.RA.stdBuilder()
                .itemInputs(material.getRod(1))
                .itemOutputs(material.getBolt(4))
                .duration(Math.max(material.getMass() * 2L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(cutterRecipes);

            Logger.WARNING("Cut Bolt Recipe: " + material.getLocalizedName() + " - Success");
        }

        if (ItemUtils.checkForInvalidItems(material.getIngot(1))
            && ItemUtils.checkForInvalidItems(material.getHotIngot(1))) {

            GT_Values.RA.stdBuilder()
                .itemInputs(material.getHotIngot(1))
                .itemOutputs(material.getIngot(1))
                .duration((int) Math.max(material.getMass() * 3L, 1L))
                .eut(material.vVoltageMultiplier)
                .addTo(vacuumFreezerRecipes);
            Logger.WARNING("Cool Hot Ingot Recipe: " + material.getLocalizedName() + " - Success");
        }

        if (ItemUtils.checkForInvalidItems(material.getRod(1))
            && ItemUtils.checkForInvalidItems(material.getLongRod(1))) {
            GT_Values.RA.stdBuilder()
                .itemInputs(material.getRod(2))
                .itemOutputs(material.getLongRod(1))
                .duration((int) Math.max(material.getMass(), 1L))
                .eut(16)
                .addTo(hammerRecipes);

            Logger.WARNING("Hammer Long Rod Recipe: " + material.getLocalizedName() + " - Success");

            GT_Values.RA.stdBuilder()
                .itemInputs(material.getLongRod(1))
                .itemOutputs(material.getRod(2))
                .duration(Math.max(material.getMass(), 1L))
                .eut(4)
                .addTo(cutterRecipes);

        }

        if (ItemUtils.checkForInvalidItems(material.getBolt(1))
            && ItemUtils.checkForInvalidItems(material.getScrew(1))) {
            GT_Values.RA.stdBuilder()
                .itemInputs(material.getBolt(1))
                .itemOutputs(material.getScrew(1))
                .duration(Math.max(material.getMass() / 8L, 1L))
                .eut(4)
                .addTo(latheRecipes);

            Logger.WARNING("Lathe Screw Recipe: " + material.getLocalizedName() + " - Success");
        }
    }
}
