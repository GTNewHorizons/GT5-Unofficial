package gtPlusPlus.xmod.gregtech.loaders.recipe;

import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.chemistry.NuclearChem;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeLoader_LFTR {	

	public static void generate() {
		// Fli2BeF4 + Thorium TetraFluoride = Uranium233
		//72k Ticks/hr
		//1l/4t = 1000l/hr
		//1l/40t = 1000l/10hr (Probably better) LiFBeF2ThF4UF4
		//1l/20t= 1000l/2.5hr LiFBeF2ZrF4UF4
		//1l/10t= 1000l/2.5hr LiFBeF2ZrF4U235
		
		FluidStack Li2BeF4 = new FluidStack(ModItems.fluidFLiBeSalt, 34);

		//LiFBeF2ThF4UF4
		GT_Recipe LFTR1 = new GTPP_Recipe(
				true, 
				new ItemStack[] {},
				new ItemStack[] {},
				null, new int[] {10000, 5000, 2500},
				new FluidStack[] {
						NUCLIDE.LiFBeF2ThF4UF4.getFluid(17),
						Li2BeF4
				},
				new FluidStack[] {
						new FluidStack(NuclearChem.Burnt_LiFBeF2ThF4UF4, 17),
						FluidUtils.getFluidStack("molten.uraniumhexafluoride", 10),
						FluidUtils.getFluidStack("molten.uraniumhexafluoride", 5)
				},
				12000,//time
				0,//cost
				8192//fuel value
				);

		//LiFBeF2ZrF4UF4
		GT_Recipe LFTR2 = new GTPP_Recipe(
				true, 
				new ItemStack[] {},
				new ItemStack[] {},
				null, new int[] {10000, 2500, 1250},
				new FluidStack[] {
						NUCLIDE.LiFBeF2ZrF4UF4.getFluid(17),
						Li2BeF4
				},
				new FluidStack[] {
						new FluidStack(NuclearChem.Burnt_LiFBeF2ZrF4UF4, 17),
						FluidUtils.getFluidStack("molten.uraniumhexafluoride", 4),
						FluidUtils.getFluidStack("molten.uraniumhexafluoride", 2)						
				},
				6000,//time
				0,//cost
				8192//fuel value
				);

		//LiFBeF2ZrF4U235
		GT_Recipe LFTR3 = new GTPP_Recipe(
				true, 
				new ItemStack[] {},
				new ItemStack[] {},
				null, new int[] {10000, 1000, 500},
				new FluidStack[] {
						NUCLIDE.LiFBeF2ZrF4U235.getFluid(17),
						Li2BeF4
				},
				new FluidStack[] {
						new FluidStack(NuclearChem.Burnt_LiFBeF2ZrF4U235, 17),
						FluidUtils.getFluidStack("molten.uraniumhexafluoride", 2),
						FluidUtils.getFluidStack("molten.uraniumhexafluoride", 1)
				},
				3000,//time
				0,//cost
				8192//fuel value
				);

		/*mRecipesLFTR.add(LFTR1);
		mRecipesLFTR.add(LFTR2);
		mRecipesLFTR.add(LFTR3);*/		
		GTPP_Recipe.GTPP_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.add(LFTR1);
		GTPP_Recipe.GTPP_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.add(LFTR2);
		GTPP_Recipe.GTPP_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.add(LFTR3);
		

	}
}
