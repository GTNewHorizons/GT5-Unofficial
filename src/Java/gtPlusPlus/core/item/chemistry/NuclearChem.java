package gtPlusPlus.core.item.chemistry;

import gtPlusPlus.core.util.minecraft.FluidUtils;
import net.minecraftforge.fluids.Fluid;

public class NuclearChem {

	public static Fluid Burnt_LiFBeF2ThF4UF4;
	public static Fluid Burnt_LiFBeF2ZrF4UF4;
	public static Fluid Burnt_LiFBeF2ZrF4U235;

	public static void run(){

		//Create Coal Gas
		Burnt_LiFBeF2ThF4UF4 = FluidUtils.generateFluidNonMolten("BurntLiFBeF2ThF4UF4", "Burnt LiFBeF2ThF4UF4 Salt", 545, new short[]{48, 48, 175, 100}, null, null);
		Burnt_LiFBeF2ZrF4UF4 = FluidUtils.generateFluidNonMolten("Burnt_LiFBeF2ZrF4UF4", "Burnt LiFBeF2ZrF4UF4 Salt", 520, new short[]{48, 68, 165, 100}, null, null);
		Burnt_LiFBeF2ZrF4U235 = FluidUtils.generateFluidNonMolten("Burnt_LiFBeF2ZrF4U235", "Burnt LiFBeF2ZrF4U235 Salt", 533, new short[]{68, 48, 185, 100}, null, null);
		
		createRecipes();


	}

	private static void createRecipes() {
	}
}
