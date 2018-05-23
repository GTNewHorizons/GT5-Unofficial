package gtPlusPlus.xmod.gregtech.loaders;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GT_Values;

import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class RecipeGen_Assembler extends RecipeGen_Base {

	public final static Set<RunnableWithInfo<Material>> mRecipeGenMap = new HashSet<RunnableWithInfo<Material>>();
	static {
		MaterialGenerator.mRecipeMapsToGenerate.put(mRecipeGenMap);
	}

	public RecipeGen_Assembler(final Material M){
		this.toGenerate = M;
		mRecipeGenMap.add(this);
	}

	@Override
	public void run() {
		generateRecipes(this.toGenerate);
	}

	private void generateRecipes(final Material material){

		//Frame Box
		GT_Values.RA.addAssemblerRecipe(
				material.getRod(4),
				ItemUtils.getGregtechCircuit(4),
				material.getFrameBox(1),
				60,
				8);

		//Rotor
		addAssemblerRecipe(
				material.getPlate(4),
				material.getRing(1),
				material.getRotor(1),
				240,
				24);

	}

	private static void addAssemblerRecipe(final ItemStack input1, final ItemStack input2, final ItemStack output1, final int seconds, final int euCost){
		GT_Values.RA.addAssemblerRecipe(
				input1,
				input2,
				FluidUtils.getFluidStack("molten.solderingalloy", 16),
				output1,
				seconds,
				euCost);
		GT_Values.RA.addAssemblerRecipe(
				input1,
				input2,
				FluidUtils.getFluidStack("molten.tin", 32),
				output1,
				seconds,
				euCost);
		GT_Values.RA.addAssemblerRecipe(
				input1,
				input2,
				FluidUtils.getFluidStack("molten.lead", 48),
				output1,
				seconds,
				euCost);
	}


}

