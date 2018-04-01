package gtPlusPlus.core.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.util.*;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.AddGregtechRecipe;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraftforge.fluids.FluidStack;

public class RECIPES_GREGTECH {

	public static void run() {
		Logger.INFO("Loading Recipes through GregAPI for Industrial Multiblocks.");
		execute();
	}

	private static void execute() {
		cokeOvenRecipes();
		electrolyzerRecipes();
		// matterFabRecipes();
		assemblerRecipes();
		fluidcannerRecipes();
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
		benderRecipes();
		cyclotronRecipes();
		blastSmelterRecipes();
		advancedMixerRecipes();
		sifterRecipes();
		electroMagneticSeperatorRecipes();
		extruderRecipes();
		cuttingSawRecipes();
		addFuels();
	}

	private static void cuttingSawRecipes() {
		GT_Values.RA.addCutterRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("blockMeatRaw", 1), //Input
				ItemUtils.getItemStackOfAmountFromOreDict("plateMeatRaw", 9), //Output
				null,
				16, //Time
				8); //EU
	}

	private static void electrolyzerRecipes() {
		GT_Values.RA.addElectrolyzerRecipe(
				ItemUtils.getSimpleStack(ModItems.dustDecayedRadium226, 1),
				null,
				null,
				FluidUtils.getFluidStack("radon", 500),
				null,
				null, 
				null, 
				null,
				null,
				null,
				new int[]{}, 
				20*90, 
				240);
	}

	private static void extruderRecipes() {
		// Osmium Credits
		if (GT_Values.RA.addExtruderRecipe(ItemUtils.getItemStackOfAmountFromOreDict("blockOsmium", 1),
				ItemList.Shape_Mold_Credit.get(0), ItemList.Credit_Greg_Osmium.get(1),
				(int) Math.max(Materials.Osmium.getMass() * 2L * 20, 1), 1024)) {
			Logger.WARNING("Extruder Recipe: Osmium Credit - Success");
		}
		else {
			Logger.WARNING("Extruder Recipe: Osmium Credit - Failed");
		}
	}

	private static void blastSmelterRecipes() {

		/*// Black Bronze
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { ItemUtils.getGregtechCircuit(13),
						ItemUtils.getItemStackOfAmountFromOreDict("dustGold", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustSilver", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 3), },
				FluidUtils.getFluidStack("molten.blackbronze", 5 * 144), 0, MathUtils.findPercentageOfInt(200 * 20, 80),
				!CORE.GTNH ? 120 : 480);

		// Black Steel
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { ItemUtils.getGregtechCircuit(5),
						ItemUtils.getItemStackOfAmountFromOreDict("dustNickel", 5),
						ItemUtils.getItemStackOfAmountFromOreDict("dustSteel", 15),
						ItemUtils.getItemStackOfAmountFromOreDict("dustGold", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustSilver", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 3) },
				FluidUtils.getFluidStack("molten.blacksteel", 25 * 144), 0, MathUtils.findPercentageOfInt(60 * 20, 80),
				!CORE.GTNH ? 120 : 480);

		// Red Steel
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { ItemUtils.getGregtechCircuit(6),
						ItemUtils.getItemStackOfAmountFromOreDict("dustSilver", 4),
						ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 4),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZinc", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustBismuth", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustSteel", 10),
						ItemUtils.getItemStackOfAmountFromOreDict("dustBlackSteel", 20) },
				FluidUtils.getFluidStack("molten.redsteel", 40 * 144), 0, MathUtils.findPercentageOfInt(65 * 20, 80),
				!CORE.GTNH ? 120 : 480);

		// Blue Steel
		CORE.RA.addBlastSmelterRecipe(new ItemStack[] { ItemUtils.getGregtechCircuit(5),
				ItemUtils.getItemStackOfAmountFromOreDict("dustGold", 12),
				ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 18),
				ItemUtils.getItemStackOfAmountFromOreDict("dustZinc", 5),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSteel", 30),
				ItemUtils.getItemStackOfAmountFromOreDict("dustBlackSteel", 60)

		}, FluidUtils.getFluidStack("molten.bluesteel", 125 * 144), 0, MathUtils.findPercentageOfInt(70 * 20, 80), !CORE.GTNH ? 120 : 480);

		// Stainless Steel
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { ItemUtils.getGregtechCircuit(14),
						ItemUtils.getItemStackOfAmountFromOreDict("dustIron", 6),
						ItemUtils.getItemStackOfAmountFromOreDict("dustNickel", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustManganese", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustChrome", 1) },
				FluidUtils.getFluidStack("molten.stainlesssteel", 9 * 144), 0,
				MathUtils.findPercentageOfInt(85 * 20, 80), !CORE.GTNH ? 120 : 480);

		// Eglin
		CORE.RA.addBlastSmelterRecipe(
				new ItemStack[] { ItemUtils.getGregtechCircuit(7),
						ItemUtils.getItemStackOfAmountFromOreDict("dustNickel", 5),
						ItemUtils.getItemStackOfAmountFromOreDict("dustIron", 23),
						ItemUtils.getItemStackOfAmountFromOreDict("dustAluminium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustChrome", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustSulfur", 3),
						ItemUtils.getItemStackOfAmountFromOreDict("dustCarbon", 3),
						ItemUtils.getItemStackOfAmountFromOreDict("dustSilicon", 12) },
				FluidUtils.getFluidStack("molten.eglinsteel", 48 * 144), 0, MathUtils.findPercentageOfInt(30 * 20, 80),
				120);
		if (!CORE.GTNH) {			

			// TungstenSteel
			CORE.RA.addBlastSmelterRecipe(
					new ItemStack[] { ItemUtils.getGregtechCircuit(2),
							ItemUtils.getItemStackOfAmountFromOreDict("ingotTungsten", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("ingotSteel", 1) },
					FluidUtils.getFluidStack("molten.tungstensteel", 2 * 144), 0,
					MathUtils.findPercentageOfInt(75 * 20, 80), 480);
			
			// HSS-G
			CORE.RA.addBlastSmelterRecipe(
					new ItemStack[] { ItemUtils.getGregtechCircuit(14),
							ItemUtils.getItemStackOfAmountFromOreDict("dustTungstenSteel", 5),
							ItemUtils.getItemStackOfAmountFromOreDict("dustVanadium", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustMolybdenum", 2),
							ItemUtils.getItemStackOfAmountFromOreDict("dustChrome", 1) },
					FluidUtils.getFluidStack("molten.hssg", 9 * 144), 0, MathUtils.findPercentageOfInt(450 * 20, 80),
					120);

			// HSS-G
			CORE.RA.addBlastSmelterRecipe(
					new ItemStack[] { ItemUtils.getGregtechCircuit(5),
							ItemUtils.getItemStackOfAmountFromOreDict("dustTungsten", 5),
							ItemUtils.getItemStackOfAmountFromOreDict("dustSteel", 5),
							ItemUtils.getItemStackOfAmountFromOreDict("dustVanadium", 2),
							ItemUtils.getItemStackOfAmountFromOreDict("dustMolybdenum", 4),
							ItemUtils.getItemStackOfAmountFromOreDict("dustChrome", 2) },
					FluidUtils.getFluidStack("molten.hssg", 18 * 144), 0, MathUtils.findPercentageOfInt(900 * 20, 80),
					120);

			// HSS-E
			CORE.RA.addBlastSmelterRecipe(
					new ItemStack[] { ItemUtils.getGregtechCircuit(14),
							ItemUtils.getItemStackOfAmountFromOreDict("dustHSSG", 6),
							ItemUtils.getItemStackOfAmountFromOreDict("dustCobalt", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustSilicon", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustManganese", 1) },
					FluidUtils.getFluidStack("molten.hsse", 9 * 144), 0, MathUtils.findPercentageOfInt(540 * 20, 80),
					120);

			// HSS-S
			CORE.RA.addBlastSmelterRecipe(
					new ItemStack[] { ItemUtils.getGregtechCircuit(3),
							ItemUtils.getItemStackOfAmountFromOreDict("dustHSSG", 6),
							ItemUtils.getItemStackOfAmountFromOreDict("dustOsmium", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustIridium", 2) },
					FluidUtils.getFluidStack("molten.hsss", 9 * 144), 0, MathUtils.findPercentageOfInt(810 * 20, 80),
					120);

			// Osmiridium
			CORE.RA.addBlastSmelterRecipe(
					new ItemStack[] { ItemUtils.getGregtechCircuit(2),
							ItemUtils.getItemStackOfAmountFromOreDict("dustIridium", 3),
							ItemUtils.getItemStackOfAmountFromOreDict("dustOsmium", 1) },
					Materials.Helium.getGas(1000), FluidUtils.getFluidStack("molten.osmiridium", 4 * 144), 0,
					MathUtils.findPercentageOfInt(500 * 20, 80), 1920);

			// Naq Alloy
			CORE.RA.addBlastSmelterRecipe(
					new ItemStack[] { ItemUtils.getGregtechCircuit(2),
							ItemUtils.getItemStackOfAmountFromOreDict("dustNaquadah", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustOsmiridium", 1) },
					Materials.Argon.getGas(1000), FluidUtils.getFluidStack("molten.naquadahalloy", 2 * 144), 0,
					MathUtils.findPercentageOfInt(500 * 20, 80), 30720);

			// Nickel-Zinc-Ferrite
			if (Materials.get("NickelZincFerrite") != null) {
				CORE.RA.addBlastSmelterRecipe(
						new ItemStack[] { ItemUtils.getGregtechCircuit(2),
								ItemUtils.getItemStackOfAmountFromOreDict("dustFerriteMixture", 6) },
						Materials.Oxygen.getGas(2000), FluidUtils.getFluidStack("molten.nickelzincferrite", 2 * 144), 0,
						MathUtils.findPercentageOfInt(600 * 20, 80), 120);
			}

			// Gallium-Arsenide
			if (Materials.get("GalliumArsenide") != null) {
				CORE.RA.addBlastSmelterRecipe(
						new ItemStack[] { ItemUtils.getGregtechCircuit(2),
								ItemUtils.getItemStackOfAmountFromOreDict("dustGallium", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustArsenic", 1) },
						FluidUtils.getFluidStack("molten.galliumarsenide", 2 * 144), 0,
						MathUtils.findPercentageOfInt(600 * 20, 80), 120);
			}

			// TungstenCarbide
			if (Materials.get("TungstenCarbide") != null) {
				CORE.RA.addBlastSmelterRecipe(
						new ItemStack[] { ItemUtils.getGregtechCircuit(12),
								ItemUtils.getItemStackOfAmountFromOreDict("dustTungsten", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustCarbon", 1) },
						FluidUtils.getFluidStack("molten.tungstencarbide", 2 * 144), 0,
						MathUtils.findPercentageOfInt(
								(int) Math.max(Materials.get("TungstenCarbide").getMass() / 40L, 1L)
								* Materials.get("TungstenCarbide").mBlastFurnaceTemp,
								80),
						480);
			}

			// Vanadium-Gallium
			if (Materials.get("VanadiumGallium") != null) {
				CORE.RA.addBlastSmelterRecipe(
						new ItemStack[] { ItemUtils.getGregtechCircuit(12),
								ItemUtils.getItemStackOfAmountFromOreDict("dustGallium", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustVanadium", 3) },
						FluidUtils.getFluidStack("molten.vanadiumgallium", 4 * 144), 0,
						MathUtils.findPercentageOfInt((int) Math.max(Materials.VanadiumGallium.getMass() / 40L, 1L)
								* Materials.VanadiumGallium.mBlastFurnaceTemp, 80),
						480);
			}

			// EIO
			// Dark Steel
			if (ItemUtils.getItemStackOfAmountFromOreDict("dustElectricalSteel", 1) != ItemUtils
					.getSimpleStack(ModItems.AAA_Broken)) {
				CORE.RA.addBlastSmelterRecipe(
						new ItemStack[] { ItemUtils.getGregtechCircuit(2),
								ItemUtils.getItemStackOfAmountFromOreDict("dustElectricalSteel", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustObsidian", 1) },
						FluidUtils.getFluidStack("molten.darksteel", 2 * 144), 0,
						MathUtils.findPercentageOfInt(200 * 20, 80), 120);
			}

			// Pulsating Iron
			if (ItemUtils.getItemStackOfAmountFromOreDict("dustIron", 1) != ItemUtils
					.getSimpleStack(ModItems.AAA_Broken)) {
				CORE.RA.addBlastSmelterRecipe(
						new ItemStack[] { ItemUtils.getGregtechCircuit(2),
								ItemUtils.getItemStackOfAmountFromOreDict("dustIron", 1),
								ItemUtils.getSimpleStack(Items.ender_pearl) },
						FluidUtils.getFluidStack("molten.pulsatingiron", 2 * 144), 0,
						MathUtils.findPercentageOfInt(8 * 20, 80), 120);
			}

			// Energetic Alloy
			if (ItemUtils.getItemStackOfAmountFromOreDict("dustEnergeticAlloy", 1) != ItemUtils
					.getSimpleStack(ModItems.AAA_Broken)) {
				CORE.RA.addBlastSmelterRecipe(
						new ItemStack[] { ItemUtils.getGregtechCircuit(12),
								ItemUtils.getItemStackOfAmountFromOreDict("dustIron", 1),
								ItemUtils.getSimpleStack(Items.glowstone_dust) },
						FluidUtils.getFluidStack("molten.redstone", 144),
						FluidUtils.getFluidStack("molten.energeticalloy", 144), 0,
						MathUtils.findPercentageOfInt(9 * 20, 80), 120);
			}

			// Vibrant Alloy
			if (ItemUtils.getItemStackOfAmountFromOreDict("dustVibrantAlloy", 1) != ItemUtils
					.getSimpleStack(ModItems.AAA_Broken)) {
				CORE.RA.addBlastSmelterRecipe(
						new ItemStack[] { ItemUtils.getGregtechCircuit(12),
								ItemUtils.getItemStackOfAmountFromOreDict("dustEnergeticAlloy", 1),
								ItemUtils.getSimpleStack(Items.ender_pearl) },
						FluidUtils.getFluidStack("molten.vibrantalloy", 144), 0,
						MathUtils.findPercentageOfInt(16 * 20, 80), 480);
			}
		}*/
	}

	private static void fluidcannerRecipes() {
		// Sulfuric Acid
		GT_Values.RA.addFluidCannerRecipe(ItemUtils.getSimpleStack(Items.glass_bottle),
				ItemUtils.getSimpleStack(ModItems.itemSulfuricPotion), FluidUtils.getFluidStack("sulfuricacid", 250),
				null);
		GT_Values.RA.addFluidCannerRecipe(ItemUtils.getSimpleStack(ModItems.itemSulfuricPotion),
				ItemUtils.getSimpleStack(Items.glass_bottle), null, FluidUtils.getFluidStack("sulfuricacid", 250));

		// Hydrofluoric Acid
		GT_Values.RA.addFluidCannerRecipe(ItemUtils.getSimpleStack(Items.glass_bottle),
				ItemUtils.getSimpleStack(ModItems.itemHydrofluoricPotion),
				FluidUtils.getFluidStack("hydrofluoricacid", 250), null);
		GT_Values.RA.addFluidCannerRecipe(ItemUtils.getSimpleStack(ModItems.itemHydrofluoricPotion),
				ItemUtils.getSimpleStack(Items.glass_bottle), null, FluidUtils.getFluidStack("hydrofluoricacid", 250));
	}

	private static void cokeOvenRecipes() {
		Logger.INFO("Loading Recipes for Industrial Coking Oven.");

		// Wood to Charcoal
		AddGregtechRecipe.addCokeAndPyrolyseRecipes(GT_OreDictUnificator.get(OrePrefixes.log, Materials.Wood, 20L), 20,
				GT_ModHandler.getSteam(1000), GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Charcoal, 24L),
				FluidUtils.getFluidStack("fluid.coalgas", 1440), 60, 30);

		// Coal to Coke
		AddGregtechRecipe.addCokeAndPyrolyseRecipes(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 16L), 22,
				GT_ModHandler.getSteam(1000), ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 10),
				FluidUtils.getFluidStack("fluid.coalgas", 2880), 30, 120);

		// Coke & Coal
		CORE.RA.addCokeOvenRecipe(GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Coal, 12L),
				ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 6), GT_ModHandler.getSteam(2000),
				FluidUtils.getFluidStack("fluid.coalgas", 5040),
				ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 14), 60 * 20, 240);

	}

	private static void matterFabRecipes() {
		Logger.INFO("Loading Recipes for Matter Fabricator.");

		try {

			CORE.RA.addMatterFabricatorRecipe(Materials.UUAmplifier.getFluid(1L), // Fluid
					// Input
					Materials.UUMatter.getFluid(1L), // Fluid Output
					800, // Time in ticks
					32); // EU
		}
		catch (final NullPointerException e) {
			Logger.INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}
		try {

			CORE.RA.addMatterFabricatorRecipe(null, // Fluid Input
					Materials.UUMatter.getFluid(1L), // Fluid Output
					3200, // Time in ticks
					32); // EU
		}
		catch (final NullPointerException e) {
			Logger.INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}

	}

	private static void dehydratorRecipes() {
		Logger.INFO("Loading Recipes for Chemical Dehydrator.");

		try {
			// Makes Lithium Carbonate
			CORE.RA.addDehydratorRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricLithium", 1), // Item
					// Input
					FluidUtils.getFluidStack("sulfuriclithium", 440), // Fluid
					// input
					// (slot
					// 1)
					new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustSulfur", 3),
							ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustSodium", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustCarbon", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustLithium7", 3) }, // Output
					// Array
					// of
					// Items
					// -
					// Upto
					// 9
					30 * 20, // Time in ticks
					30); // EU
		}
		catch (final NullPointerException e) {
			Logger.INFO("[cellSulfuricLithium] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}
		try {

			ItemStack cells = ItemUtils.getItemStackWithMeta(LoadedMods.IndustrialCraft2, "IC2:itemCellEmpty",
					"Empty Fluid Cells", 0, 12);

			if (cells == null) {
				cells = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellEmpty", 12);
			}

			final ItemStack[] input = { cells, ItemUtils.getItemStackOfAmountFromOreDict("dustLepidolite", 20) };

			CORE.RA.addDehydratorRecipe(input, // Item input (Array, up to 2)
					FluidUtils.getFluidStack("sulfuricacid", 10000), // Fluid
					// input
					// (slot
					// 1)
					FluidUtils.getFluidStack("sulfuriclithium", 10000), // Fluid
					// output
					// (slot
					// 2)
					new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustPotassium", 1),
							ItemUtils.getItemStackOfAmountFromOreDict("dustAluminium", 4),
							ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 10),
							ItemUtils.getItemStackOfAmountFromOreDict("cellFluorine", 2),
							ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumCarbonate", 3), // LithiumCarbonate
			}, // Output Array of Items - Upto 9,
					new int[] { 0 }, 75 * 20, // Time in ticks
					1000); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("[dustLepidolite] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}
		try {

			CORE.RA.addDehydratorRecipe(new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 10) }, // Item
					// input
					// (Array,
					// up
					// to
					// 2)
					FluidUtils.getFluidStack("molten.uraniumtetrafluoride", 1440), // Fluid
					// input
					// (slot
					// 1)
					null, // Fluid output (slot 2)
					new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustUraniumTetrafluoride", 10),
							ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellEmpty", 10) }, // Output
					// Array
					// of
					// Items
					// -
					// Upto
					// 9,
					new int[] { 0 }, 150 * 20, // Time in ticks
					2000); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("[dustUraniumTetrafluoride] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}
		try {

			CORE.RA.addDehydratorRecipe(new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 10) }, // Item
					// input
					// (Array,
					// up
					// to
					// 2)
					FluidUtils.getFluidStack("molten.uraniumhexafluoride", 1440), // Fluid
					// input
					// (slot
					// 1)
					null, // Fluid output (slot 2)
					new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustUraniumHexafluoride", 10),
							ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellEmpty", 10) }, // Output
					// Array
					// of
					// Items
					// -
					// Upto
					// 9,
					new int[] { 0 }, 300 * 20, // Time in ticks
					4000); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("[dustUraniumHexafluoride] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}

		// Raisins from Grapes
		try {

			CORE.RA.addDehydratorRecipe(new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cropGrape", 1) }, // Item
					// input
					// (Array,
					// up
					// to
					// 2)
					null, // Fluid input (slot 1)
					null, // Fluid output (slot 2)
					new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("foodRaisins", 1) }, // Output
					// Array
					// of
					// Items
					// -
					// Upto
					// 9,
					new int[] { 0 }, 10 * 20, // Time in ticks
					8); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("[foodRaisins] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}

		// Calcium Hydroxide
		if ((ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 1).getItem() != ModItems.AAA_Broken)
				|| LoadedMods.IHL) {
			try {

				CORE.RA.addDehydratorRecipe(
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustQuicklime", 10) }, // Item
						// input
						// (Array,
						// up
						// to
						// 2)
						FluidUtils.getFluidStack("water", 10000), // Fluid input
						// (slot 1)
						null, // Fluid output (slot 2)
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumHydroxide", 20) }, // Output
						// Array
						// of
						// Items
						// -
						// Upto
						// 9,
						new int[] { 0 }, 120 * 20, // Time in ticks
						120); // EU

			}
			catch (final NullPointerException e) {
				Logger.INFO("[dustCalciumHydroxide] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
			}

			// 2 LiOH + CaCO3
			try {

				CORE.RA.addDehydratorRecipe(
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustLi2CO3CaOH2", 5) }, // Item
						// input
						// (Array,
						// up
						// to
						// 2)
						null, // Fluid input (slot 1)
						null, // Fluid output (slot 2)
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 2),
								ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumCarbonate", 3) }, // Output
						// Array
						// of
						// Items
						// -
						// Upto
						// 9,
						new int[] { 0 }, 120 * 20, // Time in ticks
						1000); // EU

			}
			catch (final NullPointerException e) {
				Logger.INFO("[dustLi2CO3CaOH2] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
			}

			// LiOH Liquid to Dust
			try {

				CORE.RA.addDehydratorRecipe(new ItemStack[] { ItemUtils.getGregtechCircuit(0) }, // Item
						// input
						// (Array,
						// up
						// to
						// 2)
						FluidUtils.getFluidStack("lithiumhydroxide", 144), // Fluid
						// input
						// (slot
						// 1)
						null, // Fluid output (slot 2)
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 1) }, // Output
						// Array
						// of
						// Items
						// -
						// Upto
						// 9,
						new int[] { 0 }, 1 * 20, // Time in ticks
						64); // EU

			}
			catch (final NullPointerException e) {
				Logger.INFO("[dustLithiumHydroxide] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
			}

			// Zirconium Chloride -> TetraFluoride
			try {

				CORE.RA.addDehydratorRecipe(
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustCookedZrCl4", 9),
								ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 9) }, // Item
						// input
						// (Array,
						// up
						// to
						// 2)
						FluidUtils.getFluidStack("hydrofluoricacid", 9 * 144), // Fluid
						// input
						// (slot
						// 1)
						null, // Fluid output (slot 2)
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenChloride", 9),
								ItemUtils.getItemStackOfAmountFromOreDict("dustZrF4", 9) }, // Output
						// Array
						// of
						// Items
						// -
						// Upto
						// 9,
						new int[] { 0 }, 120 * 20, // Time in ticks
						500); // EU

			}
			catch (final NullPointerException e) {
				Logger.INFO("[dustZrF4] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
			}

			// CaF2 + H2SO4 â†’ CaSO4(solid) + 2 HF
			try {

				CORE.RA.addDehydratorRecipe(
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustFluorite", 37),
								ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 16) }, // Item
						// input
						// (Array,
						// up
						// to
						// 2)
						FluidUtils.getFluidStack("sulfuricacid", 56 * 144), // Fluid
						// input
						// (slot
						// 1)
						null, // Fluid output (slot 2)
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumSulfate", 30),
								ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 16),
								ItemUtils.getItemStackOfAmountFromOreDict("dustSilver", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustGold", 2),
								ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 2) }, // Output
						// Array
						// of
						// Items
						// -
						// Upto
						// 9,
						new int[] { 0, 0, 100, 100, 300, 200 }, 10 * 60 * 20, // Time
						// in
						// ticks
						230); // EU

			}
			catch (final NullPointerException e) {
				Logger.INFO("[dustFluorite] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
			}

			// Be(OH)2 + 2 (NH4)HF2 → (NH4)2BeF4 + 2 H2O
			try {
				CORE.RA.addDehydratorRecipe(
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellBerylliumHydroxide", 2),
								ItemUtils.getItemStackOfAmountFromOreDict("cellAmmoniumBifluoride", 4) }, // Item
						// input
						// (Array,
						// up
						// to
						// 2)
						null, // Fluid input (slot 1)
						FluidUtils.getFluidStack("ammoniumtetrafluoroberyllate", 6000), // Fluid
						// output
						// (slot
						// 2)
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 4),
								ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2) }, // Output
						// Array
						// of
						// Items
						// -
						// Upto
						// 9,
						new int[] { 0, 0, 0 }, 32 * 20, // Time in ticks
						64); // EU

			}
			catch (final NullPointerException e) {
				Logger.INFO("[ammoniumtetrafluoroberyllate] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
			}

			// (NH4)2BeF4 → 2 NH3 + 2 HF + BeF2
			try {
				CORE.RA.addDehydratorRecipe(
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 5) }, // Item
						// input
						// (Array,
						// up
						// to
						// 2)
						FluidUtils.getFluidStack("ammoniumtetrafluoroberyllate", 5000), // Fluid
						// input
						// (slot
						// 1)
						null, // Fluid output (slot 2)
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("cellAmmonia", 2),
								ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 2),
								ItemUtils.getItemStackOfAmountFromOreDict("cellBerylliumFluoride", 1) }, // Output
						// Array
						// of
						// Items
						// -
						// Upto
						// 9,
						new int[] { 0, 0, 0 }, 5 * 60 * 20, // Time in ticks
						120); // EU

			}
			catch (final NullPointerException e) {
				Logger.INFO("[cellBerylliumFluoride] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
			}

			// Process Waste Water
			try {

				CORE.RA.addDehydratorRecipe(null, // Item input (Array, up to 2)
						FluidUtils.getFluidStack("sludge", 1000), // Fluid input
						// (slot 1)
						FluidUtils.getFluidStack("nitricacid", 10), // Fluid
						// output
						// (slot 2)
						new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustTinyIron", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustTinyCopper", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustTinyTin", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustTinyNickel", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustTinyCobalt", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAluminium", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustTinySilver", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustTinyGold", 1),
								ItemUtils.getItemStackOfAmountFromOreDict("dustTinyIridium", 1) }, // Output
						// Array
						// of
						// Items
						// -
						// Upto
						// 9,
						new int[] { 10, 5, 5, 4, 4, 3, 2, 2, 1 }, 2 * 20, // Time
						// in
						// ticks
						500); // EU

			}
			catch (final NullPointerException e) {
				Logger.INFO("[sludge] FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
			}

		}

	}

	private static void lftrRecipes() {
		try {

		}
		catch (final NullPointerException e) {
			Logger.INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}
		try {
			// Fli2BeF4 + Thorium TetraFluoride = Uranium233
			CORE.RA.addLFTRRecipe(FluidUtils.getFluidStack("molten.LiFBeF2ThF4UF4".toLowerCase(), 144 * 4), // Fluid
					// input
					// (slot
					// 1)
					FluidUtils.getFluidStack("molten.li2bef4", 1200), // Fluid
					// output
					// (slot
					// 2)
					FluidUtils.getFluidStack("molten.uraniumhexafluoride", (1200 + (144 * 4))), // Output
					// Array
					// of
					// Items
					// -
					// Upto
					// 9,
					300 * 60 * 20, // Time in ticks
					3500); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}
		try {
			// Fli2BeF4 + Uranium235 = 1x Uranium233
			CORE.RA.addLFTRRecipe(FluidUtils.getFluidStack("molten.LiFBeF2ZrF4U235".toLowerCase(), 144 * 16), // Fluid
					// input
					// (slot
					// 1)
					FluidUtils.getFluidStack("molten.li2bef4", 144 * 12), // Fluid
					// output
					// (slot
					// 2)
					FluidUtils.getFluidStack("molten.uraniumhexafluoride", 3 * 144), // Output
					// Array
					// of
					// Items
					// -
					// Upto
					// 9,
					120 * 60 * 20, // Time in ticks
					8000); // EU
		}
		catch (final NullPointerException e) {
			Logger.INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}
		try {
			// Fli2BeF4 + Uranium233 TetraFluoride = Uranium233
			CORE.RA.addLFTRRecipe(FluidUtils.getFluidStack("molten.LiFBeF2ZrF4UF4".toLowerCase(), 144 * 2), // Fluid
					// input
					// (slot
					// 1)
					FluidUtils.getFluidStack("molten.li2bef4", 500), // Fluid
					// output
					// (slot
					// 2)
					FluidUtils.getFluidStack("molten.uraniumhexafluoride", 1288), // Output
					// Array
					// of
					// Items
					// -
					// Upto
					// 9,
					420 * 60 * 20, // Time in ticks
					4000); // EU

		}
		catch (final NullPointerException e) {
			Logger.INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}
	}

	private static void fissionFuelRecipes() {
		try {

			final String salt_LiFBeF2ThF4UF4 = "LiFBeF2ThF4UF4".toLowerCase();
			final String salt_LiFBeF2ZrF4U235 = "LiFBeF2ZrF4U235".toLowerCase();
			final String salt_LiFBeF2ZrF4UF4 = "LiFBeF2ZrF4UF4".toLowerCase();

			final FluidStack LithiumFluoride = FluidUtils.getFluidStack("molten.lithiumfluoride", 100); // Re-usable
			// FluidStacks
			final FluidStack BerylliumFluoride = FluidUtils.getFluidStack("molten.berylliumfluoride", 100); // Re-usable
			// FluidStacks
			final FluidStack ThoriumFluoride = FluidUtils.getFluidStack("molten.thoriumtetrafluoride", 100); // Re-usable
			// FluidStacks
			final FluidStack ZirconiumFluoride = FluidUtils.getFluidStack("molten.zirconiumtetrafluoride", 100); // Re-usable
			// FluidStacks
			final FluidStack UraniumTetraFluoride = FluidUtils.getFluidStack("molten.uraniumtetrafluoride", 100); // Re-usable
			// FluidStacks
			final FluidStack Uranium235 = FluidUtils.getFluidStack("molten.uranium235", 1000); // Re-usable
			// FluidStacks

			final FluidStack LiFBeF2ThF4UF4 = FluidUtils.getFluidStack("molten." + salt_LiFBeF2ThF4UF4, 100); // Re-usable
			// FluidStacks
			final FluidStack LiFBeF2ZrF4U235 = FluidUtils.getFluidStack("molten." + salt_LiFBeF2ZrF4U235, 100); // Re-usable
			// FluidStacks
			final FluidStack LiFBeF2ZrF4UF4 = FluidUtils.getFluidStack("molten." + salt_LiFBeF2ZrF4UF4, 100); // Re-usable
			// FluidStacks

			// 7LiF - BeF2 - ZrF4 - UF4 - 650C
			CORE.RA.addFissionFuel(FluidUtils.getFluidStack(LithiumFluoride, 6500), // Input
					// A
					FluidUtils.getFluidStack(BerylliumFluoride, 2500), // Input
					// B
					FluidUtils.getFluidStack(ZirconiumFluoride, 800), // Input C
					FluidUtils.getFluidStack(UraniumTetraFluoride, 700), // Input
					// D
					null, null, null, null, null, // Extra 5 inputs
					FluidUtils.getFluidStack(LiFBeF2ZrF4UF4, 10000), // Output
					// Fluid
					// 1
					null, // Output Fluid 2
					60 * 60 * 20, // Duration
					500);

			// 7LiF - BeF2 - ZrF4 - U235 - 590C
			CORE.RA.addFissionFuel(FluidUtils.getFluidStack(LithiumFluoride, 5500), // Input
					// A
					FluidUtils.getFluidStack(BerylliumFluoride, 1500), // Input
					// B
					FluidUtils.getFluidStack(ZirconiumFluoride, 600), // Input C
					FluidUtils.getFluidStack(Uranium235, 2400), // Input D
					null, null, null, null, null, // Extra 5 inputs
					FluidUtils.getFluidStack(LiFBeF2ZrF4U235, 10000), // Output
					// Fluid
					// 1
					null, // Output Fluid 2
					45 * 60 * 20, // Duration
					500);

			// 7liF - BeF2 - ThF4 - UF4 - 566C
			CORE.RA.addFissionFuel(FluidUtils.getFluidStack(LithiumFluoride, 6200), // Input
					// A
					FluidUtils.getFluidStack(BerylliumFluoride, 2800), // Input
					// B
					FluidUtils.getFluidStack(ThoriumFluoride, 700), // Input C
					FluidUtils.getFluidStack(UraniumTetraFluoride, 700), // Input
					// D
					null, null, null, null, null, // Extra 5 inputs
					FluidUtils.getFluidStack(LiFBeF2ThF4UF4, 10000), // Output
					// Fluid
					// 1
					null, // Output Fluid 2
					60 * 60 * 20, // Duration
					500);

		}
		catch (final NullPointerException e) {
			Logger.INFO("FAILED TO LOAD RECIPES - NULL POINTER SOMEWHERE");
		}
	}

	private static void assemblerRecipes() {
		// ItemUtils.getSimpleStack(GregtechItemList.Casing_Vanadium_Redox.get(1)
		addAR(ItemUtils.getItemStackOfAmountFromOreDict("plateVanadium", 32),
				ItemUtils.getItemStackOfAmountFromOreDict("frameGtVanadiumSteel", 8),
				FluidUtils.getFluidStack("oxygen", 8000),
				ItemUtils.simpleMetaStack(ModItems.itemHalfCompleteCasings, 0, 4), 16, 64);
		addAR(ItemUtils.simpleMetaStack(ModItems.itemHalfCompleteCasings, 0, 2),
				ItemUtils.getItemStackOfAmountFromOreDict("plateVanadiumGallium", 8),
				FluidUtils.getFluidStack("molten.tantalum", 144 * 4),
				ItemUtils.simpleMetaStack(ModItems.itemHalfCompleteCasings, 1, 8), 32, 128);
		addAR(ItemUtils.simpleMetaStack(ModItems.itemHalfCompleteCasings, 1, 1),
				ItemUtils.getItemStackOfAmountFromOreDict("plateLead", 4), FluidUtils.getFluidStack("nitrogen", 1000),
				ItemUtils.getSimpleStack(GregtechItemList.Casing_Vanadium_Redox.get(1), 1), 64, 256);
		addAR(ItemUtils.getItemStackOfAmountFromOreDict("plateIncoloy020", 16),
				ItemUtils.getItemStackOfAmountFromOreDict("frameGtIncoloyMA956", 4), null,
				GregtechItemList.Casing_Power_SubStation.get(4), 80, 128);
	}

	private static boolean addAR(final ItemStack inputA, final ItemStack inputB, final ItemStack outputA,
			final int seconds, final int voltage) {
		// return GT_Values.RA.addAssemblerRecipe(inputA, inputB, outputA,
		// seconds*20, voltage);
		return addAR(inputA, inputB, null, outputA, seconds * 20, voltage);
	}

	private static boolean addAR(final ItemStack inputA, final ItemStack inputB, final FluidStack inputFluidA,
			final ItemStack outputA, final int seconds, final int voltage) {
		// return GT_Values.RA.addAssemblerRecipe(inputA, inputB, outputA,
		// seconds*20, voltage);
		return GT_Values.RA.addAssemblerRecipe(inputA, inputB, inputFluidA, outputA, seconds * 20, voltage);
	}

	private static void distilleryRecipes() {
		Logger.INFO("Registering Distillery/Distillation Tower Recipes.");
		GT_Values.RA.addDistilleryRecipe(ItemList.Circuit_Integrated.getWithDamage(0L, 4L, new Object[0]),
				FluidUtils.getFluidStack("air", 1000), FluidUtils.getFluidStack("helium", 1), 400, 30, false);
		GT_Values.RA.addDistillationTowerRecipe(FluidUtils.getFluidStack("air", 20000),
				FluidUtils.getFluidStackArray("helium", 25), ItemUtils.getSimpleStack(ModItems.itemHydrogenBlob, 1),
				200, 60);

		// Apatite Distillation
		/*
		 * so if you dissolve aparite in sulphuric acid you'll get a mixture of
		 * SO2, H2O, HF and HCl
		 */
		final FluidStack[] apatiteOutput = { FluidUtils.getFluidStack("sulfurousacid", 3800),
				FluidUtils.getFluidStack("hydrogenchloride", 1000), FluidUtils.getFluidStack("hydrofluoricacid", 400) };
		GT_Values.RA.addDistillationTowerRecipe(FluidUtils.getFluidStack("sulfuricapatite", 5200), apatiteOutput, null,
				45 * 20, 256);

		final FluidStack[] sulfurousacidOutput = { FluidUtils.getFluidStack("sulfurdioxide", 500),
				FluidUtils.getFluidStack("water", 500) };
		GT_Values.RA.addDistillationTowerRecipe(FluidUtils.getFluidStack("sulfurousacid", 1000), sulfurousacidOutput,
				null, 10 * 20, 60);

		final FluidStack[] sulfurdioxideOutput = { FluidUtils.getFluidStack("oxygen", 144 * 2) };
		GT_Values.RA.addDistillationTowerRecipe(FluidUtils.getFluidStack("sulfurdioxide", 144 * 3), sulfurdioxideOutput,
				ItemUtils.getItemStackOfAmountFromOreDict("dustSulfur", 1), 5 * 20, 30);
	}

	private static void addFuels() {
		Logger.INFO("Registering New Fuels.");
		GT_Values.RA.addFuel(ItemUtils.simpleMetaStack("EnderIO:bucketFire_water", 0, 1), null, 120, 0);
		GT_Values.RA.addFuel(ItemUtils.simpleMetaStack("EnderIO:bucketRocket_fuel", 0, 1), null, 112, 0);
		GT_Values.RA.addFuel(ItemUtils.simpleMetaStack("EnderIO:bucketHootch", 0, 1), null, 36, 0);

		HotFuel.addNewHotFuel(GT_ModHandler.getLava(83), GT_Values.NF,
				new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("nuggetCopper", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("nuggetTin", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("nuggetGold", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("nuggetSilver", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("nuggetTantalum", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustSmallTungstate", 1),
						ItemUtils.getSimpleStack(Blocks.obsidian) },
				new int[] { 2000, 1000, 250, 250, 250, 250, 500 }, 0);

		HotFuel.addNewHotFuel(FluidUtils.getFluidStack("ic2pahoehoelava", 83), GT_Values.NF,
				new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("nuggetCopper", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("nuggetTin", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("nuggetElectrum", 1),
						ItemUtils.getSimpleStack(Blocks.obsidian) },
				new int[] { 1000, 500, 125, 1850 }, 0);

		/*
		 * HotFuel.addNewHotFuel( FluidUtils.getFluidStack("ic2hotcoolant",
		 * 100), GT_Values.NF, new ItemStack[]{}, new int[]{}, 0);
		 */

		ThermalFuel.addSteamTurbineFuel(FluidUtils.getFluidStack("steam", 1024));

		// CORE.RA.addFuel(UtilsItems.simpleMetaStack("EnderIO:bucketRocket_fuel",
		// 0, 1), null, 112, 0);
		GT_Values.RA.addFuel(ItemUtils.getSimpleStack(Items.lava_bucket), null, 32, 2);
		GT_Values.RA.addFuel(ItemUtils.getIC2Cell(2), null, 32, 2);
		GT_Values.RA.addFuel(ItemUtils.getIC2Cell(11), null, 24, 2);
		// System.exit(1);
	}

	private static void extractorRecipes() {
		Logger.INFO("Registering Extractor Recipes.");
		GT_ModHandler.addExtractionRecipe(GregtechItemList.Battery_RE_EV_Sodium.get(1L, new Object[0]),
				ItemList.Battery_Hull_HV.get(4L, new Object[0]));
		GT_ModHandler.addExtractionRecipe(GregtechItemList.Battery_RE_EV_Cadmium.get(1L, new Object[0]),
				ItemList.Battery_Hull_HV.get(4L, new Object[0]));
		GT_ModHandler.addExtractionRecipe(GregtechItemList.Battery_RE_EV_Lithium.get(1L, new Object[0]),
				ItemList.Battery_Hull_HV.get(4L, new Object[0]));
	}

	private static void fluidExtractorRecipes() {
		GT_Values.RA.addFluidExtractionRecipe(ItemUtils.getSimpleStack(Items.ender_pearl), null,
				FluidUtils.getFluidStack("ender", 250), 10000, 100, 30);
	}

	private static void chemicalBathRecipes() {
		final int[] chances = {};
		GT_Values.RA.addChemicalBathRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 12),
				FluidUtils.getFluidStack("chlorine", 2400),
				ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 3),
				ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 3),
				ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 3), chances, 30 * 20, 240);

		GT_Values.RA.addChemicalBathRecipe(FLUORIDES.FLUORITE.getCrushed(2), FluidUtils.getFluidStack("hydrogen", 2000),
				FLUORIDES.FLUORITE.getCrushedPurified(8), FLUORIDES.FLUORITE.getDustImpure(4),
				FLUORIDES.FLUORITE.getDustPurified(2), new int[] { 10000, 5000, 1000 }, 30 * 20, 240);

		GT_Values.RA.addChemicalBathRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumCarbonate", 10),
				FluidUtils.getFluidStack("hydrofluoricacid", 10 * 144),
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumFluoride", 5), null, null, new int[] {}, 90 * 20,
				500);

	}

	private static void centrifugeRecipes() {
		GT_Values.RA.addCentrifugeRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustThorium", 8), GT_Values.NI,
				GT_Values.NF, GT_Values.NF, ELEMENT.getInstance().THORIUM232.getDust(2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallThorium", 20),
				ELEMENT.getInstance().URANIUM232.getDust(1), GT_Values.NI, GT_Values.NI, GT_Values.NI,
				new int[] { 0, 0, 10 }, 500 * 20, 2000);

	}

	private static void mixerRecipes() {
		GT_Values.RA.addMixerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustSulfur", 1), null, null, null,
				FluidUtils.getFluidStack("oxygen", 288), FluidUtils.getFluidStack("sulfurdioxide", 432), null, 600, 60);
		GT_Values.RA.addMixerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustUranium233", 4),
				ItemUtils.getItemStackOfAmountFromOreDict("dustUranium235", 1), null, null,
				FluidUtils.getFluidStack("hydrofluoricacid", 144 * 5),
				FluidUtils.getFluidStack("molten.uraniumtetrafluoride", 144 * 5), null, 3000, 500);
		// GT_Values.RA.addMixerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellFluorine",
		// 1), ItemUtils.getItemStackOfAmountFromOreDict("cellFluorine", 1),
		// null, null, FluidUtils.getFluidStack("molten.uraniumtetrafluoride",
		// 720), FluidUtils.getFluidStack("molten.uraniumhexafluoride", 288),
		// null, 5000, 2000);
		// GT_Values.RA.addMixerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustSteel",
		// 20), ItemUtils.getItemStackOfAmountFromOreDict("dustSilicon", 1),
		// ItemUtils.getItemStackOfAmountFromOreDict("dustNickel", 5),
		// ItemUtils.getItemStackOfAmountFromOreDict("dustAluminium", 4), null,
		// null, ItemUtils.getItemStackOfAmountFromOreDict("dustEglinSteel",
		// 30), 1200, 60);
	}

	private static void chemicalReactorRecipes() {
		GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumCarbonate", 5), // Input
				// Stack
				// 1
				ItemUtils.getItemStackOfAmountFromOreDict("dustCalciumHydroxide", 5), // Input
				// Stack
				// 2
				null, // Fluid Input
				null, // Fluid Output
				ItemUtils.getItemStackOfAmountFromOreDict("dustLi2CO3CaOH2", 10), // Output
				// Stack
				600 * 20);

		GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumHydroxide", 5), // Input
				// Stack
				// 1
				null, // Input Stack 2
				FluidUtils.getFluidStack("hydrofluoricacid", 5 * 144), // Fluid
				// Input
				FluidUtils.getFluidStack("water", 5 * 144), // Fluid Output
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumFluoride", 5), // Output
				// Stack
				600 * 20);

		GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustApatite", 16), null,
				FluidUtils.getFluidStack("sulfuricacid", 144 * 32),
				FluidUtils.getFluidStack("sulfuricapatite", 144 * 4),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallSulfur", 1), 20 * 20);

		GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustLithium7", 1), null,
				FluidUtils.getFluidStack("sulfuricacid", 144 * 8), FluidUtils.getFluidStack("sulfuriclithium", 144 * 2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallLithium7", 1), 20 * 20);

		GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustLithium7", 16), FluidUtils.getFluidStack("water", 1000),
				FluidUtils.getFluidStack("lithiumhydroxide", 144 * 4),
				ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 1), 300 * 20);

		// LFTR Fuel Related Compounds

		if (CORE.GTNH) {
			// Hydroxide
			AddGregtechRecipe.addChemicalRecipeForBasicMachineOnly(
					ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 1),
					ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 1), GT_Values.NF,
					FluidUtils.getFluidStack("hydroxide", 2000),
					ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2), GT_Values.NI, 8 * 20, 30);
			// Beryllium Hydroxide
			GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustBeryllium", 7),
					ItemUtils.getGregtechCircuit(3), FluidUtils.getFluidStack("hydroxide", 1000),
					FluidUtils.getFluidStack("berylliumhydroxide", 2000), GT_Values.NI, 8 * 20);
			// Ammonium Bifluoride
			GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 1),
					ItemUtils.getGregtechCircuit(3), FluidUtils.getFluidStack("ammonium", 1000),
					FluidUtils.getFluidStack("ammoniumbifluoride", 2000),
					ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 1), 26 * 20);
			// Ammonium
			AddGregtechRecipe.addChemicalRecipeForBasicMachineOnly(
					ItemUtils.getItemStackOfAmountFromOreDict("cellAmmonia", 1),
					ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 1), GT_Values.NF,
					FluidUtils.getFluidStack("ammonium", 2000),
					ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2), GT_Values.NI, 20 * 20, 30);
		}

		if (!CORE.GTNH) {
			// Hydroxide
			GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 1),
					ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 1), GT_Values.NF,
					FluidUtils.getFluidStack("hydroxide", 2000),
					ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2), 8 * 20);
			// Ammonia (moved to GTNH core mod)
			GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 3),
					ItemUtils.getItemStackOfAmountFromOreDict("dustMagnetite", 0),
					FluidUtils.getFluidStack("nitrogen", 1000), FluidUtils.getFluidStack("ammonia", 1000),
					ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 3), 14 * 20);
			// Beryllium Hydroxide
			GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustBeryllium", 7), GT_Values.NI,
					FluidUtils.getFluidStack("hydroxide", 1000), FluidUtils.getFluidStack("berylliumhydroxide", 2000),
					GT_Values.NI, 8 * 20);
			// Ammonium Bifluoride
			GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellHydrofluoricAcid", 1),
					GT_Values.NI, FluidUtils.getFluidStack("ammonium", 1000),
					FluidUtils.getFluidStack("ammoniumbifluoride", 2000),
					ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 1), 26 * 20);
			// Ammonium
			GT_Values.RA.addChemicalRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellAmmonia", 1),
					ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 1), GT_Values.NF,
					FluidUtils.getFluidStack("ammonium", 2000),
					ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 2), 20 * 20);
		}
	}

	private static void blastFurnaceRecipes() {
		GT_Values.RA.addBlastRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustLithiumFluoride", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustBerylliumFluoride", 1), GT_Values.NF, GT_Values.NF,
				ItemUtils.getItemStackOfAmountFromOreDict("dustLi2BeF4", 3), null, 60 * 20, 2000, 3000);
		GT_Values.RA.addBlastRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustZrCl4", 1), null, GT_Values.NF,
				GT_Values.NF, ItemUtils.getItemStackOfAmountFromOreDict("dustCookedZrCl4", 1), null, 60 * 20, 340, 300);
	}

	private static void autoclaveRecipes() {
		GT_Values.RA.addAutoclaveRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 9),
				FluidUtils.getFluidStack("chlorine", 9 * 4 * 144),
				ItemUtils.getItemStackOfAmountFromOreDict("pelletZirconium", 9), 0, 120 * 20, 30);
	}

	private static void benderRecipes() {

		if (CORE.ConfigSwitches.enableMultiblock_PowerSubstation) {
			GT_Values.RA.addBenderRecipe(ItemUtils.getItemStackOfAmountFromOreDict("ingotVanadium", 1),
					ItemUtils.getItemStackOfAmountFromOreDict("plateVanadium", 1), 8, 16);
		}
	}

	private static void compressorRecipes() {
		GT_ModHandler.addCompressionRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustSmallClay", 4),
				ItemUtils.getItemStackOfAmountFromOreDict("plateClay", 1));
		GT_ModHandler.addCompressionRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustSmallMeatRaw", 4),
				ItemUtils.getItemStackOfAmountFromOreDict("plateMeatRaw", 1));
		GT_ModHandler.addCompressionRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustMeatRaw", 9),
				ItemUtils.getItemStackOfAmountFromOreDict("blockMeatRaw", 1));
	}

	private static void macerationRecipes() {
		GT_ModHandler.addPulverisationRecipe(ItemUtils.getItemStackOfAmountFromOreDict("pelletZirconium", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustZrCl4", 1));
		
		GT_ModHandler.addPulverisationRecipe(ItemUtils.getItemStackOfAmountFromOreDict("blockMeatRaw", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustMeatRaw", 9));
		/*
		 * GT_ModHandler.addPulverisationRecipe( FLUORIDES.FLUORITE.getOre(1),
		 * FLUORIDES.FLUORITE.getDust(4));
		 */

		if (ItemUtils.simpleMetaStack("chisel:limestone", 0, 1) != null) {
			GT_ModHandler.addPulverisationRecipe(ItemUtils.getItemStackOfAmountFromOreDict("limestone", 1),
					ItemUtils.getItemStackOfAmountFromOreDict("dustCalcite", 4));
		}

	}

	public static boolean addPulverisationRecipe(final ItemStack aInput, ItemStack aOutput1, ItemStack aOutput2,
			final ItemStack aOutput3) {
		aOutput1 = GT_OreDictUnificator.get(true, aOutput1);
		aOutput2 = GT_OreDictUnificator.get(true, aOutput2);
		if ((GT_Utility.isStackInvalid(aInput)) || (GT_Utility.isStackInvalid(aOutput1))) {
			return false;
		}
		if (GT_Utility.getContainerItem(aInput, false) == null) {

			if (GregTech_API.sRecipeFile.get(ConfigCategories.Machines.maceration, aInput, true)) {
				GT_Utility.addSimpleIC2MachineRecipe(aInput, GT_ModHandler.getMaceratorRecipeList(), null,
						new Object[] { aOutput1 });
			}
			GT_Values.RA.addPulveriserRecipe(aInput, new ItemStack[] { aOutput1, aOutput2, aOutput3 },
					new int[] { 10000, 10000, 10000 }, 400, 2);
		}
		return true;
	}

	private static void cyclotronRecipes() {

		//Polonium
		CORE.RA.addCyclotronRecipe(CI.getNumberedCircuit(0), FluidUtils.getFluidStack("molten.bismuth", 1),
				new ItemStack[] { GregtechItemList.Pellet_RTG_PO210.get(1) }, null, new int[] { 100 }, 20 * 300, 2040,
				500 * 20);

		//Americium
		CORE.RA.addCyclotronRecipe(CI.getNumberedCircuit(0), FluidUtils.getFluidStack("molten.americium", 1),
				new ItemStack[] { GregtechItemList.Pellet_RTG_AM241.get(4) }, null, new int[] { 2500 }, 20 * 300, 1020,
				500 * 20); //PO Special Value

		//Strontium u235
		CORE.RA.addCyclotronRecipe(CI.getNumberedCircuit(0), FluidUtils.getFluidStack("molten.uranium235", 10),
				new ItemStack[] { GregtechItemList.Pellet_RTG_SR90.get(1) }, null, new int[] { 570 }, 20 * 300, 1020,
				500 * 20); //PO Special Value

		//Strontium u233
		CORE.RA.addCyclotronRecipe(CI.getNumberedCircuit(0), FluidUtils.getFluidStack("molten.uranium233", 10),
				new ItemStack[] { GregtechItemList.Pellet_RTG_SR90.get(1) }, null, new int[] { 660 }, 20 * 300, 1020,
				500 * 20); //PO Special Value

		//Strontium pu239
		CORE.RA.addCyclotronRecipe(CI.getNumberedCircuit(0), FluidUtils.getFluidStack("molten.plutonium239", 10),
				new ItemStack[] { GregtechItemList.Pellet_RTG_SR90.get(1) }, null, new int[] { 220 }, 20 * 300, 1020,
				500 * 20); //PO Special Value

		//Plutonium
		CORE.RA.addCyclotronRecipe(CI.getNumberedCircuit(0), FluidUtils.getFluidStack("molten.plutonium238", 1),
				new ItemStack[] { GregtechItemList.Pellet_RTG_PU238.get(2) }, null, new int[] { 780 }, 20 * 300, 1020,
				500 * 20); //PO Special Value


		//Neptunium
		CORE.RA.addCyclotronRecipe(new ItemStack[] {ELEMENT.getInstance().URANIUM238.getDust(1) }, FluidUtils.getFluidStack("deuterium", 400),
				ItemUtils.getSimpleStack(ModItems.dustNeptunium238), null, new int[] { 500 }, 20 * 5, 500,
				500 * 20); //PO Special Value


	}

	private static void sifterRecipes() {
		// Zirconium
		GT_Values.RA.addSifterRecipe(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedTin", 1),
				new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustTin", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyZinc", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1) },
				new int[] { 10000, 5000, 1500, 1000, 500, 500 }, 20 * 30, 60);

		// Zirconium
		GT_Values.RA.addSifterRecipe(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedCassiterite", 1),
				new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustCassiterite", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyTin", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustZirconium", 1) },
				new int[] { 10000, 5000, 1500, 1000, 500, 500 }, 20 * 30, 60);

		// Radium
		GT_Values.RA.addSifterRecipe(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedUranium", 1),
				new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustUranium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyLead", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1) },
				new int[] { 10000, 5000, 1000, 500, 500, 500 }, 20 * 30, 60);
		// Radium
		GT_Values.RA.addSifterRecipe(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedUraninite", 1),
				new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustUraninite", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyUranium", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1) },
				new int[] { 10000, 5000, 500, 250, 250, 250 }, 20 * 30, 60);
		// Radium
		GT_Values.RA.addSifterRecipe(ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedPitchblende", 1),
				new ItemStack[] { ItemUtils.getItemStackOfAmountFromOreDict("dustPitchblende", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyLead", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1),
						ItemUtils.getItemStackOfAmountFromOreDict("dustRadium226", 1) },
				new int[] { 10000, 5000, 500, 250, 250, 250 }, 20 * 30, 60);
	}

	private static void electroMagneticSeperatorRecipes() {
		// Zirconium
		GT_Values.RA.addElectromagneticSeparatorRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedBauxite", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustBauxite", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallRutile", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("nuggetZirconium", 1), new int[] { 10000, 2500, 4000 },
				20 * 20, 24);

		// Trinium
		GT_Values.RA.addElectromagneticSeparatorRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedNaquadah", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustNaquadah", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallNaquadahEnriched", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallTrinium", 1), new int[] { 10000, 2500, 5000 },
				20 * 20, 24);

		// Trinium
		GT_Values.RA.addElectromagneticSeparatorRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedIridium", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustIridium", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallOsmium", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallTrinium", 1), new int[] { 10000, 2500, 5000 },
				20 * 20, 24);

		// Trinium
		GT_Values.RA.addElectromagneticSeparatorRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedWulfenite", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustWulfenite", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallTrinium", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSmallTrinium", 1), new int[] { 10000, 3000, 3000 },
				20 * 20, 24);
	}

	private static void advancedMixerRecipes() {
		// HgBa2Ca2Cu3O8
		CORE.RA.addMixerRecipe(ItemUtils.getItemStackOfAmountFromOreDict("cellMercury", 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustBarium", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustCalcium", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("dustCopper", 3), FluidUtils.getFluidStack("oxygen", 8000),
				null, ItemUtils.getItemStackOfAmountFromOreDict("cellEmpty", 1), ALLOY.HG1223.getDust(16), null, null,
				30 * 20, 500);
	}

}
