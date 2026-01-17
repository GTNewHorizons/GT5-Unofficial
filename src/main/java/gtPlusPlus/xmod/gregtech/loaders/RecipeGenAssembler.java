package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGenAssembler extends RecipeGenBase {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.add(mRecipeGenMap);
    }

    public RecipeGenAssembler(final Material M) {
        this.toGenerate = M;
        mRecipeGenMap.add(this);
    }

    @Override
    public void run() {
        generateRecipes(this.toGenerate);
    }

    private void generateRecipes(final Material material) {

        // Frame Box
        if (ItemUtils.checkForInvalidItems(new ItemStack[] { material.getRod(1), material.getFrameBox(1) })) {
            GTValues.RA.stdBuilder()
                .itemInputs(material.getRod(4))
                .circuit(4)
                .itemOutputs(material.getFrameBox(1))
                .duration(3 * SECONDS)
                .eut(material.vVoltageMultiplier)
                .addTo(assemblerRecipes);
        }

        // Rotor
        if (ItemUtils
            .checkForInvalidItems(new ItemStack[] { material.getPlate(1), material.getRing(1), material.getRotor(1) }))
            addAssemblerRecipe(
                material.getPlate(4),
                material.getRing(1),
                material.getRotor(1),
                240,
                material.vVoltageMultiplier);
    }

    @Deprecated
    private static void addAssemblerRecipe(final ItemStack input1, final ItemStack input2, final ItemStack output1,
        final int seconds, final int euCost) {
        GTValues.RA.stdBuilder()
            .itemInputs(input1, input2)
            .itemOutputs(output1)
            .fluidInputs(Materials.SolderingAlloy.getMolten(16))
            .duration(seconds)
            .eut(euCost)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(input1, input2)
            .itemOutputs(output1)
            .fluidInputs(Materials.Tin.getMolten(32))
            .duration(seconds)
            .eut(euCost)
            .addTo(assemblerRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(input1, input2)
            .itemOutputs(output1)
            .fluidInputs(Materials.Lead.getMolten(48))
            .duration(seconds)
            .eut(euCost)
            .addTo(assemblerRecipes);
    }
}
