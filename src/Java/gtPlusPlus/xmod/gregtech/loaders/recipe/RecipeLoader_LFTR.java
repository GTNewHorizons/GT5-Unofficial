package gtPlusPlus.xmod.gregtech.loaders.recipe;

import java.util.Collection;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GT_Recipe;
import gregtech.api.util.Recipe_GT;

import gtPlusPlus.api.objects.minecraft.NoConflictGTRecipeMap;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import net.minecraftforge.fluids.FluidStack;

public class RecipeLoader_LFTR {


	protected final static NoConflictGTRecipeMap mRecipesLFTR = new NoConflictGTRecipeMap();

	public static Collection<GT_Recipe> getRecipes(){
		return mRecipesLFTR.getRecipeMap();
	}

	public static void generate() {
		// Fli2BeF4 + Thorium TetraFluoride = Uranium233
		//72k Ticks/hr
		//1l/4t = 1000l/hr
		//1l/40t = 1000l/10hr (Probably better) LiFBeF2ThF4UF4
		//1l/20t= 1000l/2.5hr LiFBeF2ZrF4UF4
		//1l/10t= 1000l/2.5hr LiFBeF2ZrF4U235

		//LiFBeF2ThF4UF4
		GT_Recipe LFTR1 = new Recipe_GT(
				true, 
				new ItemStack[] {CI.getNumberedCircuit(1)},
				new ItemStack[] {},
				null, new int[] {5000, 2500},
				new FluidStack[] {
						FluidUtils.getFluidStack("molten.li2bef4", 34),
						FluidUtils.getFluidStack("molten.LiFBeF2ThF4UF4".toLowerCase(), 17)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack("molten.uraniumhexafluoride", 10),
						FluidUtils.getFluidStack("molten.uraniumhexafluoride", 5)
				},
				12000,//time
				0,//cost
				4096//fuel value
				);

		//LiFBeF2ZrF4UF4
		GT_Recipe LFTR2 = new Recipe_GT(
				true, 
				new ItemStack[] {CI.getNumberedCircuit(2)},
				new ItemStack[] {},
				null, new int[] {2500, 1250},
				new FluidStack[] {
						FluidUtils.getFluidStack("molten.li2bef4", 34),
						FluidUtils.getFluidStack("molten.LiFBeF2ZrF4UF4".toLowerCase(), 17)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack("molten.uraniumhexafluoride", 4),
						FluidUtils.getFluidStack("molten.uraniumhexafluoride", 2)						
				},
				6000,//time
				0,//cost
				4096//fuel value
				);

		//LiFBeF2ZrF4U235
		GT_Recipe LFTR3 = new Recipe_GT(
				true, 
				new ItemStack[] {CI.getNumberedCircuit(3)},
				new ItemStack[] {},
				null, new int[] {1000, 500},
				new FluidStack[] {
						FluidUtils.getFluidStack("molten.li2bef4", 34),
						FluidUtils.getFluidStack("molten.LiFBeF2ZrF4U235".toLowerCase(), 17)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack("molten.uraniumhexafluoride", 2),
						FluidUtils.getFluidStack("molten.uraniumhexafluoride", 1)
				},
				3000,//time
				0,//cost
				4096//fuel value
				);

		/*mRecipesLFTR.add(LFTR1);
		mRecipesLFTR.add(LFTR2);
		mRecipesLFTR.add(LFTR3);*/		
		Recipe_GT.Gregtech_Recipe_Map.sLiquidFluorineThoriumReactorRecipesEx.add(LFTR1);
		Recipe_GT.Gregtech_Recipe_Map.sLiquidFluorineThoriumReactorRecipesEx.add(LFTR2);
		Recipe_GT.Gregtech_Recipe_Map.sLiquidFluorineThoriumReactorRecipesEx.add(LFTR3);
		

	}
}
