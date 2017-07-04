package gtPlusPlus.core.recipe;

import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.util.*;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.nuclear.NUCLIDE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RECIPES_GREGTECH {

	public static void run(){
		Utils.LOG_INFO("Loading Recipes through GregAPI for Industrial Multiblocks.");
		execute();
	}

	private static void execute(){
		cokeOvenRecipes();
		matterFabRecipes();
		assemblerRecipes();
		distilleryRecipes();
		extractorRecipes();
		fluidExtractorRecipes();
		chemicalBathRecipes();
		chemicalReactorRecipes();
		dehydratorRecipes();
		blastFurnaceRecipes();
		lftrRecipes();
		fissionFuelRecipes();
		autoclaveRecipes();
		compressorRecipes();
		mixerRecipes();
		macerationRecipes();
		centrifugeRecipes();
		addFuels();
	}

	private static void cokeOvenRecipes(){
		Utils.LOG_INFO("Loading Recipes for Industrial Coking Oven.");

		try {

			//GT Logs to Charcoal Recipe
			//With Sulfuric Acid
			CORE.RA.addCokeOvenRecipe(
					GT_OreDictUnificator.get(OrePrefixes.log, Materials.Wood, 1L), //Input 1
					GT_OreDictUnificator.get(OrePrefixes.log, Materials.Wood, 1L), //Input 2
					Materials.SulfuricAcid.getFluid(20L), //Fluid Input
					Materials.Creosote.getFluid(175L), //Fluid Output
					GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 2L), //Item Output
					800,  //Time in ticks
					30); //EU
		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}
		try {

			//Coal -> Coke Recipe
			//With Sulfuric Acid
			CORE.RA.addCokeOvenRecipe(
					GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1L), //Input 1
					GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1L), //Input 2
					Materials.SulfuricAcid.getFluid(60L), //Fluid Input
					Materials.Creosote.getFluid(250L), //Fluid Output
					ItemUtils.getItemStack("Railcraft:fuel.coke", 2), //Item Output
					600,  //Time in ticks
					120); //EU
		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}

		try {
			//GT Logs to Charcoal Recipe
			//Without Sulfuric Acid
			CORE.RA.addCokeOvenRecipe(
					GT_OreDictUnificator.get(OrePrefixes.log, Materials.Wood, 1L), //Input 1
					GT_OreDictUnificator.get(OrePrefixes.log, Materials.Wood, 1L), //Input 2
					FluidUtils.getFluidStack("oxygen", 80), //Fluid Input
					Materials.Creosote.getFluid(145L), //Fluid Output
					GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 2L), //Item Output
					1200,  //Time in ticks
					30); //EU
		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}

		try {
			//Coal -> Coke Recipe
			//Without Sulfuric Acid
			CORE.RA.addCokeOvenRecipe(
					GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1L), //Input 1
					GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 1L), //Input 2
					FluidUtils.getFluidStack("oxygen", 185), //Fluid Input
					Materials.Creosote.getFluid(200L), //Fluid Output
					ItemUtils.getItemStack("Railcraft:fuel.coke", 2), //Item Output
					900,  //Time in ticks
					120); //EU
		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}
	}

	private static void matterFabRecipes(){
		Utils.LOG_INFO("Loading Recipes for Matter Fabricator.");

		try {

			CORE.RA.addMatterFabricatorRecipe(
					Materials.UUAmplifier.getFluid(1L), //Fluid Input
					Materials.UUMatter.getFluid(1L), //Fluid Output
					800,  //Time in ticks
					32); //EU
		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}
		try {

			CORE.RA.addMatterFabricatorRecipe(
					null, //Fluid Input
					Materials.UUMatter.getFluid(1L), //Fluid Output
					3200,  //Time in ticks
					32); //EU
		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}

	}

	private static void dehydratorRecipes(){
		Utils.LOG_INFO("Loading Recipes for Chemical Dehydrator.");

		try {
			//Makes Lithium Carbonate
			CORE.RA.addDehydratorRecipe(
					ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricLithium", 1), //Item Input
					FluidUtils.getFluidStack("sulfuriclithium", 440), //Fluid input (slot 1)
					new ItemStack[]{
							ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustSulfur", 3),
							ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustSodium", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustCarbon", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustLithium7", 3)
					}, //Output Array of Items - Upto 9
					30*20, //Time in ticks
					30); //EU
		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}
		try {

			ItemStack cells = ItemUtils.getItemStackWithMeta(LoadedMods.IndustrialCraft2, "IC2:itemCellEmpty", "Empty Fluid Cells", 0, 12);

			if (cells == null){
				cells = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellEmpty", 12);
			}

			final ItemStack[] input = {cells, ItemUtils.getItemStackOfAmountFromOreDict("dustLepidolite", 20)};

			CORE.RA.addDehydratorRecipe(
					input, //Item input (Array, up to 2)
					FluidUtils.getFluidStack("sulfuricacid", 10000), //Fluid input (slot 1)
					FluidUtils.getFluidStack("sulfuriclithium", 10000), //Fluid output (slot 2)
					new ItemStack[]{
							ItemUtils.getItemStackOfAmountFromOreDict("dustPotassium", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustAluminium", 4),
							ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 10),
							ItemUtils.getItemStackOfAmountFromOreDict("cellFluorine", 2),
							ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumCarbonate", 3), //LithiumCarbonate
					}, //Output Array of Items - Upto 9,
					new int[]{0},
					75*20, //Time in ticks
					1000); //EU

		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}
		try {

			CORE.RA.addDehydratorRecipe(
					new ItemStack[]{
							ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 10)
					}, //Item input (Array, up to 2)
					FluidUtils.getFluidStack("molten.uraniumtetrafluoride", 1440), //Fluid input (slot 1)
					null, //Fluid output (slot 2)
					new ItemStack[]{
							ItemUtils.getItemStackOfAmountFromOreDict("dustUraniumTetrafluoride", 10),
							ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellEmpty", 10)
					}, //Output Array of Items - Upto 9,
					new int[]{0},
					150*20, //Time in ticks
					2000); //EU

		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}
		try {

			CORE.RA.addDehydratorRecipe(
					new ItemStack[]{
							ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 10)
					}, //Item input (Array, up to 2)
					FluidUtils.getFluidStack("molten.uraniumhexafluoride", 1440), //Fluid input (slot 1)
					null, //Fluid output (slot 2)
					new ItemStack[]{
							ItemUtils.getItemStackOfAmountFromOreDict("dustUraniumHexafluoride", 10),
							ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellEmpty", 10)
					}, //Output Array of Items - Upto 9,
					new int[]{0},
					300*20, //Time in ticks
					4000); //EU

		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}

		//Raisins from Grapes
		try {

			CORE.RA.addDehydratorRecipe(
					new ItemStack[]{
							ItemUtils.getItemStackOfAmountFromOreDict("cropGrape", 1)
					}, //Item input (Array, up to 2)
					null, //Fluid input (slot 1)
					null, //Fluid output (slot 2)
					new ItemStack[]{
							ItemUtils.getItemStackOfAmountFromOreDict("foodRaisins", 1)
					}, //Output Array of Items - Upto 9,
					new int[]{0},
					10*20, //Time in ticks
					8); //EU

		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}

		//Calcium Hydroxide
		if ((ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 1).getItem() != ModItems.AAA_Broken) || LoadedMods.IHL){
			try {

				CORE.RA.addDehydratorRecipe(
						new ItemStack[]{
								ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 10)
						}, //Item input (Array, up to 2)
						FluidUtils.getFluidStack("water", 10000), //Fluid input (slot 1)
						null, //Fluid output (slot 2)
						new ItemStack[]{
								ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumHydroxide", 20)
						}, //Output Array of Items - Upto 9,
						new int[]{0},
						120*20, //Time in ticks
						120); //EU

			}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}

			//2 LiOH + CaCO3
			try {

				CORE.RA.addDehydratorRecipe(
						new ItemStack[]{
								ItemUtils.getItemStackOfAmountFromOreDict("dustLi2CO3CaOH2", 5)
						}, //Item input (Array, up to 2)
						null, //Fluid input (slot 1)
						null, //Fluid output (slot 2)
						new ItemStack[]{
								ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 2),
								ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumCarbonate", 3)
						}, //Output Array of Items - Upto 9,
						new int[]{0},
						120*20, //Time in ticks
						1000); //EU

			}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}

			//LiOH Liquid to Dust
			try {

				CORE.RA.addDehydratorRecipe(
						new ItemStack[]{
								ItemUtils.getGregtechCircuit(0)
						}, //Item input (Array, up to 2)
						FluidUtils.getFluidStack("lithiumhydroxide", 144), //Fluid input (slot 1)
						null, //Fluid output (slot 2)
						new ItemStack[]{
								ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 1)
						}, //Output Array of Items - Upto 9,
						new int[]{0},
						1*20, //Time in ticks
						64); //EU

			}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}

			//Zirconium Chloride -> TetraFluoride
			try {

				CORE.RA.addDehydratorRecipe(
						new ItemStack[]{
								ItemUtils.getItemStackOfAmountFromOreDict("dustCookedZrCl4", 9),
								ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 9)
						}, //Item input (Array, up to 2)
						FluidUtils.getFluidStack("hydrofluoricacid", 9*144), //Fluid input (slot 1)
						null, //Fluid output (slot 2)
						new ItemStack[]{
								ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenChloride", 9),
								ItemUtils.getItemStackOfAmountFromOreDict("dustZrF4", 9)
						}, //Output Array of Items - Upto 9,
						new int[]{0},
						120*20, //Time in ticks
						500); //EU

			}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}

			//CaF2 + H2SO4 → CaSO4(solid) + 2 HF
			try {

				CORE.RA.addDehydratorRecipe(
						new ItemStack[]{
								ItemUtils.getItemStackOfAmountFromOreDict("dustFluorite", 37),
								ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 16)
						}, //Item input (Array, up to 2)
						FluidUtils.getFluidStack("sulfuricacid", 56*144), //Fluid input (slot 1)
						null, //Fluid output (slot 2)
						new ItemStack[]{
								ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumSulfate", 30),
								ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 16),
								ItemUtils.getItemStackOfAmountFromOreDict("dustSilver", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustGold", 2),
								ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 2)
						}, //Output Array of Items - Upto 9,
						new int[]{0, 0, 100, 100, 300, 200},
						10*60*20, //Time in ticks
						230); //EU

			}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}
		}

	}

	private static void lftrRecipes(){
		try {

		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}
		try {
			//Fli2BeF4 + Thorium TetraFluoride = Uranium233
			CORE.RA.addLFTRRecipe(
					FluidUtils.getFluidStack("molten.LiFBeF2ThF4UF4".toLowerCase(), 144*4), //Fluid input (slot 1)
					FluidUtils.getFluidStack("molten.li2bef4", 1200), //Fluid output (slot 2)
					FluidUtils.getFluidStack("molten.uraniumhexafluoride", (1200+(144*4))), //Output Array of Items - Upto 9,
					300*60*20, //Time in ticks
					3500); //EU

		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}
		try {
			//Fli2BeF4 + Uranium235 = 1x Uranium233
			CORE.RA.addLFTRRecipe(
					FluidUtils.getFluidStack("molten.LiFBeF2ZrF4U235".toLowerCase(), 144*16), //Fluid input (slot 1)
					FluidUtils.getFluidStack("molten.li2bef4", 144*12), //Fluid output (slot 2)
					FluidUtils.getFluidStack("molten.uraniumhexafluoride", 3*144), //Output Array of Items - Upto 9,
					120*60*20, //Time in ticks
					8000); //EU
		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}
		try {
			//Fli2BeF4 + Uranium233 TetraFluoride = Uranium233
			CORE.RA.addLFTRRecipe(
					FluidUtils.getFluidStack("molten.LiFBeF2ZrF4UF4".toLowerCase(), 144*2), //Fluid input (slot 1)
					FluidUtils.getFluidStack("molten.li2bef4", 500), //Fluid output (slot 2)
					FluidUtils.getFluidStack("molten.uraniumhexafluoride", 1288), //Output Array of Items - Upto 9,
					420*60*20, //Time in ticks
					4000); //EU

		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}
	}

	private static void fissionFuelRecipes(){
		try {

			final String salt_LiFBeF2ThF4UF4 = "LiFBeF2ThF4UF4".toLowerCase();
			final String salt_LiFBeF2ZrF4U235 = "LiFBeF2ZrF4U235".toLowerCase();
			final String salt_LiFBeF2ZrF4UF4 = "LiFBeF2ZrF4UF4".toLowerCase();

			final FluidStack LithiumFluoride = FluidUtils.getFluidStack("molten.lithiumfluoride", 100); //Re-usable FluidStacks
			final FluidStack BerylliumFluoride = FluidUtils.getFluidStack("molten.berylliumfluoride", 100); //Re-usable FluidStacks
			final FluidStack ThoriumFluoride = FluidUtils.getFluidStack("molten.thoriumtetrafluoride", 100); //Re-usable FluidStacks
			final FluidStack ZirconiumFluoride = FluidUtils.getFluidStack("molten.zirconiumtetrafluoride", 100); //Re-usable FluidStacks
			final FluidStack UraniumTetraFluoride = FluidUtils.getFluidStack("molten.uraniumtetrafluoride", 100); //Re-usable FluidStacks
			final FluidStack Uranium235 = FluidUtils.getFluidStack("molten.uranium235", 1000); //Re-usable FluidStacks

			final FluidStack LiFBeF2ThF4UF4 = FluidUtils.getFluidStack("molten."+salt_LiFBeF2ThF4UF4, 100); //Re-usable FluidStacks
			final FluidStack LiFBeF2ZrF4U235 = FluidUtils.getFluidStack("molten."+salt_LiFBeF2ZrF4U235, 100); //Re-usable FluidStacks
			final FluidStack LiFBeF2ZrF4UF4 = FluidUtils.getFluidStack("molten."+salt_LiFBeF2ZrF4UF4, 100); //Re-usable FluidStacks

			//7LiF - BeF2 - ZrF4 - UF4 - 650C
			CORE.RA.addFissionFuel(
					FluidUtils.getFluidStack(LithiumFluoride, 6500), //Input A
					FluidUtils.getFluidStack(BerylliumFluoride, 2500), //Input B
					FluidUtils.getFluidStack(ZirconiumFluoride, 800), //Input C
					FluidUtils.getFluidStack(UraniumTetraFluoride, 700), //Input D
					null, null, null, null, null, //Extra 5 inputs
					FluidUtils.getFluidStack(LiFBeF2ZrF4UF4, 10000), //Output Fluid 1
					null, //Output Fluid 2
					60*60*20, //Duration
					4740);

			//7LiF - BeF2 - ZrF4 - U235 - 590C
			CORE.RA.addFissionFuel(
					FluidUtils.getFluidStack(LithiumFluoride, 5500), //Input A
					FluidUtils.getFluidStack(BerylliumFluoride, 1500), //Input B
					FluidUtils.getFluidStack(ZirconiumFluoride, 600), //Input C
					FluidUtils.getFluidStack(Uranium235, 2400), //Input D
					null, null, null, null, null, //Extra 5 inputs
					FluidUtils.getFluidStack(LiFBeF2ZrF4U235, 10000), //Output Fluid 1
					null, //Output Fluid 2
					45*60*20, //Duration
					4740);

			//7liF - BeF2 - ThF4 - UF4 - 566C
			CORE.RA.addFissionFuel(
					FluidUtils.getFluidStack(LithiumFluoride, 6200), //Input A
					FluidUtils.getFluidStack(BerylliumFluoride, 2800), //Input B
					FluidUtils.getFluidStack(ThoriumFluoride, 700), //Input C
					FluidUtils.getFluidStack(UraniumTetraFluoride, 700), //Input D
					null, null, null, null, null, //Extra 5 inputs
					FluidUtils.getFluidStack(LiFBeF2ThF4UF4, 10000), //Output Fluid 1
					null, //Output Fluid 2
					60*60*20, //Duration
					4740);

		}catch (final NullPointerException e){Utils.LOG_INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");}
	}

	private static void assemblerRecipes(){
		//GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6L), ItemList.Casing_Turbine.get(1L, new Object[0]), ItemList.Casing_Turbine2.get(1L, new Object[0]), 50, 16);
		//GT_Values.RA.addAssemblerRecipe(GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6L), ItemList.Casing_Turbine.get(1L, new Object[0]), ItemList.Casing_Turbine3.get(1L, new Object[0]), 50, 16);

	}

	private static void distilleryRecipes(){
		Utils.LOG_INFO("Registering Distillery/Distillation Tower Recipes.");
		GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]), FluidUtils.getFluidStack("air", 1000), FluidUtils.getFluidStack("helium", 1), 400, 30, false);
		GT_Values.RA.addDistillationTowerRecipe(FluidUtils.getFluidStack("air", 20000), FluidUtils.getFluidStackArray("helium", 25), ItemUtils.getSimpleStack(ModItems.itemHydrogenBlob, 1), 200, 60);

		//Apatite Distillation
		/*
		 * so if you dissolve aparite in sulphuric acid
		 * you'll get a mixture of SO2, H2O, HF and HCl
		 */
		final FluidStack[] apatiteOutput = {
				FluidUtils.getFluidStack("sulfurousacid", 3800),
				FluidUtils.getFluidStack("hydrogenchloride", 1000),
				FluidUtils.getFluidStack("hydrofluoricacid", 400)
		};
		GT_Values.RA.addDistillationTowerRecipe(
				FluidUtils.getFluidStack("sulfuricapatite", 5200),
				apatiteOutput,
				null,
				45*20,
				256);

		final FluidStack[] sulfurousacidOutput = {
				FluidUtils.getFluidStack("sulfurdioxide", 500),
				FluidUtils.getFluidStack("water", 500)
		};
		GT_Values.RA.addDistillationTowerRecipe(
				FluidUtils.getFluidStack("sulfurousacid", 1000),
				sulfurousacidOutput,
				null,
				10*20,
				60);

		final FluidStack[] sulfurdioxideOutput = {
				FluidUtils.getFluidStack("oxygen", 144*2)
		};
		GT_Values.RA.addDistillationTowerRecipe(
				FluidUtils.getFluidStack("sulfurdioxide", 144*3),
				sulfurdioxideOutput,
				ItemUtils.getItemStackOfAmountFromOreDict("dustSulfur", 1),
				5*20,
				30);
	}

	private static void addFuels(){
		Utils.LOG_INFO("Registering New Fuels.");
		GT_Values.RA.addFuel(ItemUtils.simpleMetaStack("EnderIO:bucketFire_water", 0, 1), null, 120, 0);
		GT_Values.RA.addFuel(ItemUtils.simpleMetaStack("EnderIO:bucketRocket_fuel", 0, 1), null, 112, 0);
		GT_Values.RA.addFuel(ItemUtils.simpleMetaStack("EnderIO:bucketHootch", 0, 1), null, 36, 0);



		//CORE.RA.addFuel(UtilsItems.simpleMetaStack("EnderIO:bucketRocket_fuel", 0, 1), null, 112, 0);
		GT_Values.RA.addFuel(ItemUtils.getSimpleStack(Items.lava_bucket), null, 32, 2);
		GT_Values.RA.addFuel(ItemUtils.getIC2Cell(2), null, 32, 2);
		GT_Values.RA.addFuel(ItemUtils.getIC2Cell(11), null, 24, 2);
		//System.exit(1);
	}

	private static void extractorRecipes(){
		Utils.LOG_INFO("Registering Extractor Recipes.");
		GT_ModHandler.addExtractionRecipe(GregtechItemList.Battery_RE_EV_Sodium.get(1L, new Object[0]), ItemList.Battery_Hull_HV.get(4L, new Object[0]));
		GT_ModHandler.addExtractionRecipe(GregtechItemList.Battery_RE_EV_Cadmium.get(1L, new Object[0]), ItemList.Battery_Hull_HV.get(4L, new Object[0]));
		GT_ModHandler.addExtractionRecipe(GregtechItemList.Battery_RE_EV_Lithium.get(1L, new Object[0]), ItemList.Battery_Hull_HV.get(4L, new Object[0]));
	}
	
	private static void fluidExtractorRecipes(){
		GT_Values.RA.addFluidExtractionRecipe(ItemUtils.getSimpleStack(Items.ender_pearl), null, FluidUtils.getFluidStack("ender", 250), 10000, 100, 30);
	}

	private static void chemicalBathRecipes(){
		final int[] chances = {};
		GT_Values.RA.addChemicalBathRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 12), FluidUtils.getFluidStack("chlorine", 2400),
				ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 3),
				ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 3),
				ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 3),
				chances,
				30*20,
				240);

		GT_Values.RA.addChemicalBathRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumCarbonate", 10),
				FluidUtils.getFluidStack("hydrofluoricacid", 10*144),
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumFluoride", 5),
				null,
				null,
				new int[]{},
				90*20,
				500);


	}

	private static void centrifugeRecipes(){
		GT_Values.RA.addCentrifugeRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("dustThorium", 8),
				GT_Values.NI,
				GT_Values.NF,
				GT_Values.NF,
				NUCLIDE.getInstance().THORIUM232.getDust(2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallThorium", 20),
				NUCLIDE.getInstance().URANIUM232.getDust(1),
				GT_Values.NI,
				GT_Values.NI,
				GT_Values.NI,
				new int[]{0, 0, 10},
				500*20,
				2000);

	}

	private static void mixerRecipes(){
		GT_Values.RA.addMixerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustSulfur", 1), null, null, null, FluidUtils.getFluidStack("oxygen", 288), FluidUtils.getFluidStack("sulfurdioxide", 432), null, 600, 60);
		GT_Values.RA.addMixerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustUranium233", 4), ItemUtils.getItemStackOfAmountFromOreDict("dustUranium235", 1), null, null, FluidUtils.getFluidStack("hydrofluoricacid", 144*5), FluidUtils.getFluidStack("molten.uraniumtetrafluoride", 144*5), null, 3000, 500);
		//GT_Values.RA.addMixerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellFluorine", 1), ItemUtils.getItemStackOfAmountFromOreDict("cellFluorine", 1), null, null, FluidUtils.getFluidStack("molten.uraniumtetrafluoride", 720), FluidUtils.getFluidStack("molten.uraniumhexafluoride", 288), null, 5000, 2000);
		GT_Values.RA.addMixerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustSteel", 20), ItemUtils.getItemStackOfAmountFromOreDict("dustSilicon", 1), ItemUtils.getItemStackOfAmountFromOreDict("dustNickel", 5), ItemUtils.getItemStackOfAmountFromOreDict("dustAluminium", 4), null, null, ItemUtils.getItemStackOfAmountFromOreDict("dustEglinSteel", 30), 1200, 60);
		}

	private static void chemicalReactorRecipes(){
		GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumCarbonate", 5), //Input Stack 1
				ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumHydroxide", 5), //Input Stack 2
				null, //Fluid Input
				null, //Fluid Output
				ItemUtils.getItemStackOfAmountFromOreDict("dustLi2CO3CaOH2", 10), //Output Stack
				600*20
				);

		GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 5), //Input Stack 1
				null, //Input Stack 2
				FluidUtils.getFluidStack("hydrofluoricacid", 5*144), //Fluid Input
				FluidUtils.getFluidStack("water", 5*144), //Fluid Output
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumFluoride", 5), //Output Stack
				600*20
				);

		GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("dustApatite", 16),
				null,
				FluidUtils.getFluidStack("sulfuricacid", 144*32),
				FluidUtils.getFluidStack("sulfuricapatite", 144*4),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallSulfur", 1),
				20*20);

		GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithium7", 1),
				null,
				FluidUtils.getFluidStack("sulfuricacid", 144*8),
				FluidUtils.getFluidStack("sulfuriclithium", 144*2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallLithium7", 1),
				20*20);

		GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithium7", 16),
				FluidUtils.getFluidStack("water", 1000),
				FluidUtils.getFluidStack("lithiumhydroxide", 144*4),
				null,
				300*20);
	}

	private static void blastFurnaceRecipes(){
		GT_Values.RA.addBlastRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumFluoride", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustBerylliumFluoride", 1),
				GT_Values.NF, GT_Values.NF,
				ItemUtils.getItemStackOfAmountFromOreDict("dustLi2BeF4", 3),
				null,
				60*20,
				2000,
				3000);
		GT_Values.RA.addBlastRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("dustZrCl4", 1),
				null,
				GT_Values.NF, GT_Values.NF,
				ItemUtils.getItemStackOfAmountFromOreDict("dustCookedZrCl4", 1),
				null,
				60*20,
				340,
				300);
	}

	private static void autoclaveRecipes(){
		GT_Values.RA.addAutoclaveRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 9),
				FluidUtils.getFluidStack("chlorine", 9*4*144),
				ItemUtils.getItemStackOfAmountFromOreDict("pelletZirconium", 9),
				0,
				120*20,
				30);
	}
	
	private static void compressorRecipes(){
		GT_ModHandler.addCompressionRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustSmallClay", 4), ItemUtils.getItemStackOfAmountFromOreDict("plateClay", 1));
	}

	private static void macerationRecipes(){
		GT_ModHandler.addPulverisationRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("pelletZirconium", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustZrCl4", 1));
		GT_ModHandler.addPulverisationRecipe(
				ItemUtils.getSimpleStack(Item.getItemFromBlock(ModBlocks.blockOreFluorite)),
				ItemUtils.getItemStackOfAmountFromOreDict("dustFluorite", 4));
	}

	public static boolean addPulverisationRecipe(final ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2, final ItemStack aOutput3) {
		aOutput1 = GT_OreDictUnificator.get(true, aOutput1);
		aOutput2 = GT_OreDictUnificator.get(true, aOutput2);
		if ((GT_Utility.isStackInvalid(aInput)) || (GT_Utility.isStackInvalid(aOutput1))){
			return false;
		}
		if (GT_Utility.getContainerItem(aInput, false) == null) {

			if (GregTech_API.sRecipeFile.get(ConfigCategories.Machines.maceration, aInput, true)) {
				GT_Utility.addSimpleIC2MachineRecipe(aInput, GT_ModHandler.getMaceratorRecipeList(), null, new Object[] { aOutput1 });
			}
			GT_Values.RA.addPulveriserRecipe(aInput, new ItemStack[] {
					aOutput1, aOutput2, aOutput3 },
					new int[] {10000, 10000, 10000},
					400,
					2);
		}
		return true;
	}

}