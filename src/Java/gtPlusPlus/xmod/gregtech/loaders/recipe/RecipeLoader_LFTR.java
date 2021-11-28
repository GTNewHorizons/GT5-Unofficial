package gtPlusPlus.xmod.gregtech.loaders.recipe;

import gregtech.api.enums.Materials;
import gregtech.api.util.GTPP_Recipe;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GasSpargingRecipe;
import gregtech.api.util.GasSpargingRecipeMap;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.chemistry.NuclearChem;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
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
			mNobleGases.add(ELEMENT.getInstance().XENON.getFluid(1).getFluid());
			mNobleGases.add(ELEMENT.getInstance().NEON.getFluid(1).getFluid());
			mNobleGases.add(ELEMENT.getInstance().ARGON.getFluid(1).getFluid());
			mNobleGases.add(ELEMENT.getInstance().KRYPTON.getFluid(1).getFluid());
			mNobleGases.add(ELEMENT.getInstance().RADON.getFluid(1).getFluid());
		}
		if (mFluorideGases == null) {
			mFluorideGases = new AutoMap<Fluid>();
			mFluorideGases.add(mSpargeGases.get(1));
			mFluorideGases.add(FLUORIDES.LITHIUM_FLUORIDE.getFluid(1).getFluid());
			mFluorideGases.add(FLUORIDES.NEPTUNIUM_HEXAFLUORIDE.getFluid(1).getFluid());
			mFluorideGases.add(FLUORIDES.TECHNETIUM_HEXAFLUORIDE.getFluid(1).getFluid());
			mFluorideGases.add(FLUORIDES.SELENIUM_HEXAFLUORIDE.getFluid(1).getFluid());
			mFluorideGases.add(FLUORIDES.THORIUM_TETRAFLUORIDE.getFluid(1).getFluid());
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
		FluidStack Li2BeF4 = new FluidStack(ModItems.fluidFLiBeSalt, 34);
		FluidStack aBurntLi2BeF4 = new FluidStack(ModItems.fluidFLiBeSaltBurnt, 34);

		//LiFBeF2ThF4UF4
		GT_Recipe LFTR1 = new GTPP_Recipe(
				true, 
				new ItemStack[] {},
				new ItemStack[] {},
				null, new int[] {10000, 10000, 5000, 2500},
				new FluidStack[] {
						NUCLIDE.LiFBeF2ThF4UF4.getFluid(17),
						Li2BeF4
				},
				new FluidStack[] {
						new FluidStack(NuclearChem.Burnt_LiFBeF2ThF4UF4, 17),
						aBurntLi2BeF4,
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
				null, new int[] {10000, 10000, 2500, 1250},
				new FluidStack[] {
						NUCLIDE.LiFBeF2ZrF4UF4.getFluid(17),
						Li2BeF4
				},
				new FluidStack[] {
						new FluidStack(NuclearChem.Burnt_LiFBeF2ZrF4UF4, 17),
						aBurntLi2BeF4,
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
				null, new int[] {10000, 10000, 1000, 500},
				new FluidStack[] {
						NUCLIDE.LiFBeF2ZrF4U235.getFluid(17),
						Li2BeF4
				},
				new FluidStack[] {
						new FluidStack(NuclearChem.Burnt_LiFBeF2ZrF4U235, 17),
						aBurntLi2BeF4,
						FluidUtils.getFluidStack("molten.uraniumhexafluoride", 2),
						FluidUtils.getFluidStack("molten.uraniumhexafluoride", 1)
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
