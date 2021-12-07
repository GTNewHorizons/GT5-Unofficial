package gtPlusPlus.xmod.gregtech.loaders.recipe;

import gregtech.api.enums.Materials;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GasSpargingRecipeMap;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class RecipeLoader_LFTR {	

	private static AutoMap<Fluid> mNobleGases;
	private static AutoMap<Fluid> mFluorideGases;
	private static AutoMap<Fluid> mSpargeGases;

	private static void configureSparging() {
		if (mSpargeGases == null) {
			mSpargeGases = new AutoMap<Fluid>();
			mSpargeGases.add(Materials.Helium.getGas(1).getFluid());
			mSpargeGases.add(Materials.Fluorine.getGas(1).getFluid());
		}
		if (mNobleGases == null) {
			mNobleGases = new AutoMap<Fluid>();
			mNobleGases.add(mSpargeGases.get(0));
			mNobleGases.add(ELEMENT.getInstance().XENON.getFluid());
			mNobleGases.add(ELEMENT.getInstance().NEON.getFluid());
			mNobleGases.add(ELEMENT.getInstance().ARGON.getFluid());
			mNobleGases.add(ELEMENT.getInstance().KRYPTON.getFluid());
			mNobleGases.add(ELEMENT.getInstance().RADON.getFluid());
		}
		if (mFluorideGases == null) {
			mFluorideGases = new AutoMap<Fluid>();
			mFluorideGases.add(mSpargeGases.get(1));
			mFluorideGases.add(FLUORIDES.LITHIUM_FLUORIDE.getFluid());
			mFluorideGases.add(FLUORIDES.NEPTUNIUM_HEXAFLUORIDE.getFluid());
			mFluorideGases.add(FLUORIDES.TECHNETIUM_HEXAFLUORIDE.getFluid());
			mFluorideGases.add(FLUORIDES.SELENIUM_HEXAFLUORIDE.getFluid());
			mFluorideGases.add(FLUORIDES.THORIUM_TETRAFLUORIDE.getFluid());
		}
	}

	public static void generate() {
		// Fli2BeF4 + Thorium TetraFluoride = Uranium233
		//72k Ticks/hr
		//1l/4t = 1000l/hr
		//1l/40t = 1000l/10hr (Probably better) LiFBeF2ThF4UF4
		//1l/20t= 1000l/2.5hr LiFBeF2ZrF4UF4
		//1l/10t= 1000l/2.5hr LiFBeF2ZrF4U235

		configureSparging();
		FluidStack Li2BeF4 = NUCLIDE.Li2BeF4.getFluidStack(36);

		//LiFBeF2ThF4UF4 - T3
		GT_Recipe LFTR1 = new GTPP_Recipe(
				false, 
				new ItemStack[] {},
				new ItemStack[] {},
				null, new int[] {10000, 10000, 5000, 2500},
				new FluidStack[] {
						NUCLIDE.LiFBeF2ThF4UF4.getFluidStack(18),
						Li2BeF4
				},
				new FluidStack[] {
						NUCLIDE.LiFBeF2UF4FP.getFluidStack(18),
						NUCLIDE.LiFBeF2ThF4.getFluidStack(36),
						FLUORIDES.URANIUM_HEXAFLUORIDE.getFluidStack(10),
						FLUORIDES.URANIUM_HEXAFLUORIDE.getFluidStack(5)
				},
				9000,//time
				0,//cost
				8192*4//fuel value
				);

		//LiFBeF2ZrF4UF4 - T2
		GT_Recipe LFTR2 = new GTPP_Recipe(
				false, 
				new ItemStack[] {},
				new ItemStack[] {},
				null, new int[] {10000, 10000, 2500, 1250},
				new FluidStack[] {
						NUCLIDE.LiFBeF2ZrF4UF4.getFluidStack(18),
						Li2BeF4
				},
				new FluidStack[] {
						NUCLIDE.LiFBeF2UF4FP.getFluidStack(12),
						NUCLIDE.LiFBeF2ThF4.getFluidStack(24),
						FLUORIDES.URANIUM_HEXAFLUORIDE.getFluidStack(4),
						FLUORIDES.URANIUM_HEXAFLUORIDE.getFluidStack(2)						
				},
				6000,//time
				0,//cost
				8192//fuel value
				);

		//LiFBeF2ZrF4U235 - T1
		GT_Recipe LFTR3 = new GTPP_Recipe(
				false, 
				new ItemStack[] {},
				new ItemStack[] {},
				null, new int[] {10000, 10000, 1000, 500},
				new FluidStack[] {
						NUCLIDE.LiFBeF2ZrF4U235.getFluidStack(18),
						Li2BeF4
				},
				new FluidStack[] {
						NUCLIDE.LiFBeF2UF4FP.getFluidStack(6),
						NUCLIDE.LiFThF4.getFluidStack(12),
						FLUORIDES.URANIUM_HEXAFLUORIDE.getFluidStack(2),
						FLUORIDES.URANIUM_HEXAFLUORIDE.getFluidStack(1)
				},
				3000,//time
				0,//cost
				8192//fuel value
				);

		// Sparging NEI Recipes
		GasSpargingRecipeMap.addRecipe(
				new FluidStack(mSpargeGases.get(0), 50),
				new FluidStack[] {
						new FluidStack(mNobleGases.get(0), 50),
						new FluidStack(mNobleGases.get(1), 10),
						new FluidStack(mNobleGases.get(2), 10),
						new FluidStack(mNobleGases.get(3), 10),
						new FluidStack(mNobleGases.get(4), 10),
						new FluidStack(mNobleGases.get(5), 10)
				},
				new int[] {
						5000, 1000, 1000, 1000, 1000, 1000
				});

		GasSpargingRecipeMap.addRecipe(
				new FluidStack(mSpargeGases.get(1), 100),
				new FluidStack[] {
						new FluidStack(mFluorideGases.get(0), 100),
						new FluidStack(mFluorideGases.get(1), 20),
						new FluidStack(mFluorideGases.get(2), 20),
						new FluidStack(mFluorideGases.get(3), 20),
						new FluidStack(mFluorideGases.get(4), 20),
						new FluidStack(mFluorideGases.get(5), 20)
				},
				new int[] {
						10000, 2000, 2000, 2000, 2000, 2000
				});
	
		GTPP_Recipe.GTPP_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.add(LFTR1);
		GTPP_Recipe.GTPP_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.add(LFTR2);
		GTPP_Recipe.GTPP_Recipe_Map.sLiquidFluorineThoriumReactorRecipes.add(LFTR3);


	}
}
