package gtPlusPlus.core.item.chemistry;

import net.minecraft.init.Items;

import gregtech.api.enums.GT_Values;

import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraftforge.fluids.Fluid;

public class NuclearChem {

	public static Fluid Burnt_LiFBeF2ThF4UF4;
	public static Fluid Burnt_LiFBeF2ZrF4UF4;
	public static Fluid Burnt_LiFBeF2ZrF4U235;
	
	public static Fluid GeneticMutagen;
	private static boolean generateMutagenRecipe = false;

	public static void run(){

		//Create Coal Gas
		Burnt_LiFBeF2ThF4UF4 = FluidUtils.generateFluidNonMolten("BurntLiFBeF2ThF4UF4", "Burnt LiFBeF2ThF4UF4 Salt", 545, new short[]{48, 175, 48, 100}, null, null);
		Burnt_LiFBeF2ZrF4UF4 = FluidUtils.generateFluidNonMolten("BurntLiFBeF2ZrF4UF4", "Burnt LiFBeF2ZrF4UF4 Salt", 520, new short[]{48, 168, 68, 100}, null, null);
		Burnt_LiFBeF2ZrF4U235 = FluidUtils.generateFluidNonMolten("BurntLiFBeF2ZrF4U235", "Burnt LiFBeF2ZrF4U235 Salt", 533, new short[]{68, 185, 48, 100}, null, null);
		
		if (FluidUtils.getFluidStack("fluid.Mutagen", 1) == null) {
			GeneticMutagen = FluidUtils.generateFluidNonMolten("GeneticMutagen", "Genetic Mutagen", 12, new short[]{22, 148, 185, 100}, null, null);
			generateMutagenRecipe = true;
		}
		else {
			GeneticMutagen = FluidUtils.getFluidStack("fluid.Mutagen", 1).getFluid();
		}
		
		createRecipes();


	}

	private static void createRecipes() {
		
		if (generateMutagenRecipe)
		chemReator_CreateMutagen();	
		
		chemReactor_MutagenWithEggs();
	}
	
	private static void chemReator_CreateMutagen() {
		GT_Values.RA.addChemicalRecipe(
				CI.getNumberedCircuit(20),
				ItemUtils.getSimpleStack(Items.nether_star, 2),
				FluidUtils.getMobEssence(5000),
				FluidUtils.getFluidStack(GeneticMutagen, 8000),
				null,
				30*20,
				500);
	}
	
	private static void chemReactor_MutagenWithEggs() {
		GT_Values.RA.addChemicalRecipe(
				CI.getNumberedCircuit(20),
				ItemUtils.getSimpleStack(Items.egg, 2),
				FluidUtils.getFluidStack(GeneticMutagen, 500),
				null,
				ItemUtils.getSimpleStack(ModItems.itemBigEgg, 2),
				300*20,
				500);
	}
}
