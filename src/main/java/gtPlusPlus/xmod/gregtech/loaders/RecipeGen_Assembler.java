package gtPlusPlus.xmod.gregtech.loaders;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGen_Assembler extends RecipeGen_Base {

    public static final Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<>();

    static {
        MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
    }

    public RecipeGen_Assembler(final Material M) {
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
            GT_Values.RA.stdBuilder()
                .itemInputs(material.getRod(4), GT_Utility.getIntegratedCircuit(4))
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
        GT_Values.RA.stdBuilder()
            .itemInputs(input1, input2)
            .itemOutputs(output1)
            .fluidInputs(FluidUtils.getFluidStack("molten.solderingalloy", 16))
            .duration(seconds)
            .eut(euCost)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(input1, input2)
            .itemOutputs(output1)
            .fluidInputs(FluidUtils.getFluidStack("molten.tin", 32))
            .duration(seconds)
            .eut(euCost)
            .addTo(assemblerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(input1, input2)
            .itemOutputs(output1)
            .fluidInputs(FluidUtils.getFluidStack("molten.lead", 48))
            .duration(seconds)
            .eut(euCost)
            .addTo(assemblerRecipes);
    }
}
