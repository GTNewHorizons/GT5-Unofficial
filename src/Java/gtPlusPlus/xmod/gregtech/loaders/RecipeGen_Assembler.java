package gtPlusPlus.xmod.gregtech.loaders;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;

public class RecipeGen_Assembler  implements Runnable{

	final Material toGenerate;
	
	public RecipeGen_Assembler(final Material M){
		this.toGenerate = M;
	}
	
	@Override
	public void run() {
		generateRecipes(toGenerate);		
	}
	
	public static void generateRecipes(final Material material){
			
		//Frame Box
		GT_Values.RA.addAssemblerRecipe(
				material.getRod(4),
				ItemUtils.getGregtechCircuit(4),
				material.getBlock(1),
				60,
				8);         
		
		//Rotor - Soldering Alloy
		GT_Values.RA.addAssemblerRecipe(
				material.getPlate(4),
				material.getRing(1),
				FluidUtils.getFluidStack("molten.solderingalloy", 16),
				material.getRotor(1),
				240,
				24);      

		//Rotor - Lead
		GT_Values.RA.addAssemblerRecipe(
				material.getPlate(4),
				material.getRing(1),
				FluidUtils.getFluidStack("molten.lead", 48),
				material.getRotor(1),
				240,
				24);      

		//Rotor - Tin
		GT_Values.RA.addAssemblerRecipe(
				material.getPlate(4),
				material.getRing(1),
				FluidUtils.getFluidStack("molten.tin", 32),
				material.getRotor(1),
				240,
				24);      
		
	}
}

