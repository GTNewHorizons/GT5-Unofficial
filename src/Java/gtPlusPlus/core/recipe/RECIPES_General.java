package gtPlusPlus.core.recipe;

import static gtPlusPlus.core.recipe.common.CI.bitsd;
import static gtPlusPlus.core.util.minecraft.ItemUtils.getSimpleStack;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits.generatePipeRecipes;
import static gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits.generateWireRecipes;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.nuclear.FLUORIDES;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.bop.blocks.BOP_Block_Registrator;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechConduits;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class RECIPES_General {

	static final ItemStack NULL = null;
	static ItemStack RECIPE_Paper;
	static ItemStack RECIPE_Dirt;
	static ItemStack RECIPE_Snow;
	static ItemStack RECIPE_Obsidian;
	static String RECIPE_LapisDust = "dustLazurite";
	static ItemStack OUTPUT_Blueprint;
	static ItemStack RECIPE_CraftingTable;
	static String RECIPE_BronzePlate = "plateBronze";
	static ItemStack RECIPE_BasicCasingIC2;
	static ItemStack OUTPUT_Workbench_Bronze;
	static ItemStack RECIPE_HydrogenDust;

	public static void loadRecipes(){		
		if (LoadedMods.Gregtech){			
			RECIPE_Paper = ItemUtils.getSimpleStack(Items.paper);
			RECIPE_Dirt = ItemUtils.getSimpleStack(Blocks.dirt);
			RECIPE_Snow = ItemUtils.getSimpleStack(Blocks.snow);
			RECIPE_Obsidian = ItemUtils.getSimpleStack(Blocks.obsidian);
			RECIPE_CraftingTable = ItemUtils.getSimpleStack(Blocks.crafting_table);
			RECIPE_HydrogenDust = ItemUtils.getSimpleStack(ModItems.itemHydrogenBlob);	
			RECIPE_BasicCasingIC2 = ItemUtils.getItemStackFromFQRN("IC2:blockMachine", 1);			
			OUTPUT_Workbench_Bronze = ItemUtils.getSimpleStack(ModBlocks.blockWorkbench);
			OUTPUT_Blueprint = ItemUtils.getSimpleStack(ModItems.itemBlueprintBase);	
			run();
			addCompressedObsidian();
			addHandPumpRecipes();
			migratedRecipes();
		}
	}

	private static void run() {
		//Workbench Blueprint
		/*RecipeUtils.recipeBuilder(
				RECIPE_Paper, RECIPE_LapisDust, NULL,
				RECIPE_Paper, RECIPE_LapisDust, NULL,
				RECIPE_LapisDust, RECIPE_LapisDust, NULL,
				OUTPUT_Blueprint);*/

		//Bronze Workbench
		/*RecipeUtils.recipeBuilder(
				RECIPE_BronzePlate, RECIPE_CraftingTable, RECIPE_BronzePlate,
				RECIPE_BronzePlate, RECIPE_BasicCasingIC2, RECIPE_BronzePlate,
				RECIPE_BronzePlate, RECIPE_BronzePlate, RECIPE_BronzePlate,
				OUTPUT_Workbench_Bronze);*/

		//Generates recipes for the Dull shard when TC is not installed.
		if (!LoadedMods.Thaumcraft) {
			//Dull Shard to Aer
			RecipeUtils.recipeBuilder(
					RECIPE_HydrogenDust, RECIPE_HydrogenDust, RECIPE_HydrogenDust,
					RECIPE_HydrogenDust, ItemUtils.getSimpleStack(ModItems.shardDull), RECIPE_HydrogenDust,
					RECIPE_HydrogenDust, RECIPE_HydrogenDust, RECIPE_HydrogenDust,
					ItemUtils.getSimpleStack(ModItems.shardAer));
			//Dull Shard to Ignis
			RecipeUtils.recipeBuilder(
					RECIPE_Obsidian, RECIPE_Obsidian, RECIPE_Obsidian,
					RECIPE_Obsidian, ItemUtils.getSimpleStack(ModItems.shardDull), RECIPE_Obsidian,
					RECIPE_Obsidian, RECIPE_Obsidian, RECIPE_Obsidian,
					ItemUtils.getSimpleStack(ModItems.shardIgnis));
			//Dull Shard to Terra
			RecipeUtils.recipeBuilder(
					RECIPE_Dirt, RECIPE_Dirt, RECIPE_Dirt,
					RECIPE_Dirt, ItemUtils.getSimpleStack(ModItems.shardDull), RECIPE_Dirt,
					RECIPE_Dirt, RECIPE_Dirt, RECIPE_Dirt,
					ItemUtils.getSimpleStack(ModItems.shardTerra));
			//Dull Shard to Aqua
			RecipeUtils.recipeBuilder(
					RECIPE_LapisDust, RECIPE_LapisDust, RECIPE_LapisDust,
					RECIPE_LapisDust, ItemUtils.getSimpleStack(ModItems.shardDull), RECIPE_LapisDust,
					RECIPE_LapisDust, RECIPE_LapisDust, RECIPE_LapisDust,
					ItemUtils.getSimpleStack(ModItems.shardAqua));

			GT_ModHandler.addPulverisationRecipe(ItemUtils.getSimpleStack(ModItems.shardAer), ItemUtils.getSimpleStack(ModItems.dustAer, 2));
			GT_ModHandler.addPulverisationRecipe(ItemUtils.getSimpleStack(ModItems.shardIgnis), ItemUtils.getSimpleStack(ModItems.dustIgnis, 2));
			GT_ModHandler.addPulverisationRecipe(ItemUtils.getSimpleStack(ModItems.shardTerra), ItemUtils.getSimpleStack(ModItems.dustTerra, 2));
			GT_ModHandler.addPulverisationRecipe(ItemUtils.getSimpleStack(ModItems.shardAqua), ItemUtils.getSimpleStack(ModItems.dustAqua, 2));

		}

		//Rainforest oak Sapling
		if (RecipeUtils.recipeBuilder(
				"stickWood", "stickWood", "stickWood",
				"stickWood", "treeSapling", "stickWood",
				"stickWood", "dustBone", "stickWood",
				ItemUtils.getSimpleStack(BOP_Block_Registrator.sapling_Rainforest))){
			Logger.INFO("Added a recipe for Rainforest oak Saplings.");
		}


		// Try fix this ore
		if (ModBlocks.blockOreFluorite != null){
			RecipeUtils.addShapelessGregtechRecipe(new ItemStack[]{ItemUtils.getSimpleStack(ModBlocks.blockOreFluorite)}, FLUORIDES.FLUORITE.getOre(1));
		}

		//Iron bars
		final ItemStack ironBars;
		if (CORE.GTNH) {
			ironBars = ItemUtils.getItemStackFromFQRN("dreamcraft:item.SteelBars", 1);
		} else {
			ironBars = ItemUtils.getItemStackFromFQRN("minecraft:iron_bars", 1);
		}

		//Fish Trap
		if (RecipeUtils.recipeBuilder(
				ironBars, ironBars, ironBars,
				ironBars, "frameGtWroughtIron", ironBars,
				ironBars, ironBars, ironBars,
				ItemUtils.getSimpleStack(ModBlocks.blockFishTrap))){
			Logger.INFO("Added a recipe for the Fish Trap.");
		}

		//Small Gear Extruder Shape
		if (!CORE.GTNH) {
			GT_ModHandler.addCraftingRecipe(GregtechItemList.Shape_Extruder_SmallGear.get(1L, new Object[0]), GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, new Object[]{"hXS", "XPX", "fXd", Character.valueOf('P'), ItemList.Shape_Extruder_Gear, Character.valueOf('X'), OrePrefixes.plate.get(Materials.Steel), Character.valueOf('S'), OrePrefixes.screw.get(Materials.Steel)});


			String[] ironTypes = {"", "Wrought", "Pig", "Any"};
			for (int y=0;y<ironTypes.length;y++) {
				//Iron bars
				String ironRecipe = "stick"+ironTypes[y]+"Iron";
				if (RecipeUtils.recipeBuilder(
						null, CI.craftingToolWrench, null,
						ironRecipe, ironRecipe, ironRecipe,
						ironRecipe, ironRecipe, ironRecipe,
						ItemUtils.getItemStackFromFQRN("minecraft:iron_bars", 8))) {
					Logger.INFO("Re-added old GT recipe for Iron Bars.");			
				}
			}
		}


		//Shaped Crafting for ULV Material Dusts		
		
		//Potin
		if (RecipeUtils.addShapelessGregtechRecipe(new Object[] {"dustLead", "dustBronze", "dustTin",
				"dustLead", "dustBronze"}, ALLOY.POTIN.getDust(5))){
			Logger.INFO("Added shapeless recipe for Potin Dust.");
		}
		
		//Tumbaga
		if (RecipeUtils.addShapelessGregtechRecipe(new Object[] {
				"dustGold", "dustGold", "dustCopper"}, ItemUtils.getSimpleStack(ModItems.dustTumbagaMix))){
			Logger.INFO("Added shapeless recipe for Tumbaga Mix.");
		}	
		if (RecipeUtils.addShapelessGregtechRecipe(new Object[] {
				ItemUtils.getSimpleStack(ModItems.dustTumbagaMix),
				ItemUtils.getSimpleStack(ModItems.dustTumbagaMix),
				ItemUtils.getSimpleStack(ModItems.dustTumbagaMix),
				"dustGold"
				},
				ALLOY.TUMBAGA.getDust(10))){
			Logger.INFO("Added shapeless recipe for Tumbaga Dust.");
		}









		//Mining Explosive
		Logger.RECIPE("[Inspection] Explosives");
		if (RecipeUtils.recipeBuilder(
				CI.explosiveITNT, CI.explosiveTNT, CI.explosiveITNT,
				CI.explosiveTNT, "frameGtIron", CI.explosiveTNT,
				"dustSulfur", CI.explosiveTNT, "dustSulfur",
				ItemUtils.getSimpleStack(ModBlocks.blockMiningExplosive, 3))){
			Logger.INFO("Added a recipe for Mining Explosives.");
		}

		//Alkalus Coin
		if (RecipeUtils.recipeBuilder(
				"gemExquisiteRuby", "gemFlawlessDiamond", "gemExquisiteDiamond",
				"gemFlawlessRuby", ItemList.Credit_Greg_Osmium.get(1), "gemFlawlessSapphire",
				"gemExquisiteEmerald", "gemFlawlessEmerald", "gemExquisiteSapphire",
				ItemUtils.getSimpleStack(ModItems.itemAlkalusDisk))){
			Logger.INFO("Added a recipe for The Alkalus Disk.");
		}

		/*final String fancyGems[] = new String[]{"gemExquisiteDiamond", "gemExquisiteEmerald", "gemExquisiteRuby", "gemExquisiteSapphire"};
		final ItemStack gemShards[] = new ItemStack[]{ItemUtils.simpleMetaStack(ModItems.itemGemShards, 0, 1),
				ItemUtils.simpleMetaStack(ModItems.itemGemShards, 1, 1),
				ItemUtils.simpleMetaStack(ModItems.itemGemShards, 2, 1),
				ItemUtils.simpleMetaStack(ModItems.itemGemShards, 3, 1)};

		int l=0;
		for (final String gem : fancyGems){
			GameRegistry.addShapelessRecipe(
					gemShards[l],
					ItemUtils.getItemStackOfAmountFromOreDict(gem, 1),
					new ItemStack(ModItems.itemAlkalusDisk, 1, OreDictionary.WILDCARD_VALUE));
			l++;
		}*/

		Logger.RECIPE("[Inspection] Wither Cage");
		if (RecipeUtils.recipeBuilder(
				"stickBlackSteel", "plateTungstenSteel", "stickBlackSteel",
				"plateTungstenSteel", getSimpleStack(Items.nether_star), "plateTungstenSteel",
				"stickBlackSteel", "plateTungstenSteel", "stickBlackSteel",
				ItemUtils.getSimpleStack(ModBlocks.blockWitherGuard, 32))){
			Logger.INFO("Added a recipe for Wither Cages.");
		}

		Logger.RECIPE("[Inspection] Xp Converter");
		if (RecipeUtils.recipeBuilder(
				getSimpleStack(Items.experience_bottle), ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 2, 1), getSimpleStack(Items.experience_bottle),
				ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 5, 1), getSimpleStack(Items.nether_star), ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 5, 1),
				getSimpleStack(Items.experience_bottle), ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 2, 1), getSimpleStack(Items.experience_bottle),
				ItemUtils.getSimpleStack(ModBlocks.blockXpConverter, 1))){
			Logger.INFO("Added a recipe for XP Converter.");
		}




		// Rope/Fiber/Net
		if (RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDictNoBroken(CI.craftingToolKnife, 1), ItemUtils.getSimpleStack(Items.reeds)},
				ItemUtils.getSimpleStack(ModItems.itemFiber, 16)
				)){
			Logger.INFO("Added a recipe for Fiber.");
		}

		if (RecipeUtils.addShapelessGregtechRecipe(
				new ItemStack[]{
						ItemUtils.getItemStackOfAmountFromOreDictNoBroken(CI.craftingToolKnife, 1), ItemUtils.getSimpleStack(Blocks.sapling)},
				ItemUtils.getSimpleStack(ModItems.itemFiber, 32)
				)){
			Logger.INFO("Added a recipe for Fiber.");
		}

		if (RecipeUtils.recipeBuilder(
				null, ItemUtils.getSimpleStack(ModItems.itemFiber, 1), null,
				ItemUtils.getSimpleStack(ModItems.itemFiber, 1), CI.craftingToolKnife, ItemUtils.getSimpleStack(ModItems.itemFiber, 1),
				null, ItemUtils.getSimpleStack(ModItems.itemFiber, 1), null,
				ItemUtils.getSimpleStack(ModItems.itemRope, 3))){
			Logger.INFO("Added a recipe for Rope.");
		}

		Logger.RECIPE("[Inspection] Net");
		if (RecipeUtils.recipeBuilder(
				ItemUtils.getSimpleStack(ModItems.itemRope, 1), ItemUtils.getSimpleStack(ModItems.itemRope, 1), ItemUtils.getSimpleStack(ModItems.itemRope, 1),
				ItemUtils.getSimpleStack(ModItems.itemRope, 1), ItemUtils.getSimpleStack(ModItems.itemRope, 1), ItemUtils.getSimpleStack(ModItems.itemRope, 1),
				null, null, null,
				ItemUtils.getSimpleStack(ModBlocks.blockNet, 2))){
			Logger.INFO("Added a recipe for Nets.");
		}

		// Slow Builders Ring
		CORE.RA.addSixSlotAssemblingRecipe(
				new ItemStack[] { ItemUtils.getSimpleStack(Blocks.ice, 8),
						ItemUtils.getSimpleStack(ModBlocks.blockNet, 8), ItemUtils.getSimpleStack(Blocks.vine, 8),
						ALLOY.TUMBAGA.getRing(1), },
				FluidUtils.getWater(1000), // Fluid
				ItemUtils.getItemStackFromFQRN("miscutils:SlowBuildingRing", 1), // Output
				20 * 30, // Dur
				16); // Eu





	}

	private static boolean addCompressedObsidian(){
		//Invert Obsidian
		if (RecipeUtils.recipeBuilder(
				getSimpleStack(Items.redstone), getSimpleStack(Items.glowstone_dust), getSimpleStack(Items.redstone),
				getSimpleStack(Items.glowstone_dust), ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 1, 1), getSimpleStack(Items.glowstone_dust),
				getSimpleStack(Items.redstone), getSimpleStack(Items.glowstone_dust), getSimpleStack(Items.redstone),
				ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, 5, 1))){
			Logger.INFO("Added a recipe for Inverted Obsidian.");
		}

		final ItemStack[] mItems = new ItemStack[6];
		mItems[0] = ItemUtils.getSimpleStack(Blocks.obsidian);
		for (int r=0;r<5;r++){
			mItems[r+1] = ItemUtils.simpleMetaStack(ModBlocks.blockCompressedObsidian, r, 1);
		}

		//Compressed Obsidian 1-5
		for (int r=0;r<5;r++){

			final ItemStack input = mItems[r];
			final ItemStack output = mItems[r+1];

			if (RecipeUtils.recipeBuilder(
					input, input, input,
					input, input, input,
					input, input, input,
					output)){
				Logger.INFO("Added a recipe for Compressed Obsidian ["+r+"]");
			}

			if (RecipeUtils.addShapelessGregtechRecipe(new ItemStack[]{output}, ItemUtils.getSimpleStack(input, 9))){
				Logger.INFO("Added a shapeless recipe for Compressed Obsidian ["+r+"]");
			}

		}
		return true;
	}

	private static void addHandPumpRecipes() {
		if (RecipeUtils.recipeBuilder(
				CI.electricPump_LV, "circuitBasic", null,
				"ringBrass", CI.electricMotor_LV, "circuitBasic",
				"plateSteel", "plateSteel", "rodBrass",
				ItemUtils.simpleMetaStack(ModItems.itemGenericToken, 1, 1)))	
			Logger.INFO("Added recipe for Hand Pump I - true");
		if (RecipeUtils.recipeBuilder(
				CI.electricPump_MV, "circuitAdvanced", null,
				"ringMagnalium", CI.electricMotor_MV, "circuitAdvanced",
				"plateAluminium", "plateAluminium", "rodMagnalium",
				ItemUtils.simpleMetaStack(ModItems.itemGenericToken, 2, 1)))	
			Logger.INFO("Added recipe for Hand Pump II - true");	
		if (RecipeUtils.recipeBuilder(
				CI.electricPump_HV, "circuitData", null,
				"ringChrome", CI.electricMotor_HV, "circuitData",
				"plateStainlessSteel", "plateStainlessSteel", "rodChrome",
				ItemUtils.simpleMetaStack(ModItems.itemGenericToken, 3, 1)))		
			Logger.INFO("Added recipe for Hand Pump III - true");
		if (RecipeUtils.recipeBuilder(
				CI.electricPump_EV, "circuitElite", null,
				"ringTitanium", CI.electricMotor_EV, "circuitElite",
				"plateTungstenSteel", "plateTungstenSteel", "rodTitanium",
				ItemUtils.simpleMetaStack(ModItems.itemGenericToken, 4, 1)))	
			Logger.INFO("Added recipe for Hand Pump IV - true");



		GT_Values.RA.addAssemblerRecipe(ItemUtils.simpleMetaStack(ModItems.itemGenericToken, 1, 1), CI.getNumberedCircuit(20), ItemUtils.simpleMetaStack(ModItems.toolGregtechPump, 1000, 1), 30, 30);
		GT_Values.RA.addAssemblerRecipe(ItemUtils.simpleMetaStack(ModItems.itemGenericToken, 2, 1), CI.getNumberedCircuit(20), ItemUtils.simpleMetaStack(ModItems.toolGregtechPump, 1001, 1), 120, 120);
		GT_Values.RA.addAssemblerRecipe(ItemUtils.simpleMetaStack(ModItems.itemGenericToken, 3, 1), CI.getNumberedCircuit(20), ItemUtils.simpleMetaStack(ModItems.toolGregtechPump, 1002, 1), 480, 480);
		GT_Values.RA.addAssemblerRecipe(ItemUtils.simpleMetaStack(ModItems.itemGenericToken, 4, 1), CI.getNumberedCircuit(20), ItemUtils.simpleMetaStack(ModItems.toolGregtechPump, 1003, 1), 1820, 1820);


	}

	private static void migratedRecipes() {

		RecipeUtils.generateMortarRecipe(ItemUtils.getSimpleStack(ModItems.itemPlateRawMeat), ItemUtils.getItemStackOfAmountFromOreDict("dustMeatRaw", 1));

		generateWireRecipes(ELEMENT.getInstance().ZIRCONIUM);
		generateWireRecipes(ALLOY.HG1223);
		generateWireRecipes(ALLOY.LEAGRISIUM);
		generateWireRecipes(ALLOY.TRINIUM_TITANIUM);

		GT_Materials[] g = new GT_Materials[] {
				GT_Materials.Staballoy,
				GT_Materials.Tantalloy60,
				GT_Materials.Tantalloy61,
				GT_Materials.Void,
				GT_Materials.Potin,
				GT_Materials.MaragingSteel300,
				GT_Materials.MaragingSteel350,
				GT_Materials.Inconel690,
				GT_Materials.Inconel792,
				GT_Materials.HastelloyX,
				GT_Materials.TriniumNaquadahCarbonite,

		};
		for (GT_Materials e : g) {
			if (e == GT_Materials.Void) {
				if (!LoadedMods.Thaumcraft) {
					continue;
				}
			}			
			int tVoltageMultiplier = (e.mBlastFurnaceTemp >= 2800) ? 64 : 16;
			generatePipeRecipes(e.mDefaultLocalName, e.getMass(), tVoltageMultiplier);
		}

		Materials[] h = new Materials[] {
				Materials.Europium,
				Materials.Tungsten,
				Materials.DarkSteel,
				Materials.Clay,
				Materials.Lead,

		};

		for (Materials e : h) {
			if (e == Materials.DarkSteel) {
				if (!LoadedMods.EnderIO) {
					continue;
				}
			}			
			int tVoltageMultiplier = (e.mBlastFurnaceTemp >= 2800) ? 64 : 16;
			generatePipeRecipes(e.mDefaultLocalName, e.getMass(), tVoltageMultiplier);
		}

		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK)
			RecipeUtils.addShapedGregtechRecipe(
					CI.component_Plate[4], "rotorGtStainlessSteel", CI.component_Plate[4],
					CI.getTieredCircuitOreDictName(3), CI.machineHull_HV, CI.getTieredCircuitOreDictName(3),
					CI.component_Plate[4], CI.electricPump_HV, CI.component_Plate[4],
					GregtechItemList.Hatch_Air_Intake.get(1L, new Object[0]));

		RecipeUtils.addShapedGregtechRecipe(CI.component_Plate[6], ALLOY.MARAGING250.getGear(1), CI.component_Plate[6],
				CI.getTieredCircuitOreDictName(4), GregtechItemList.Casing_AdvancedVacuum.get(1),
				CI.getTieredCircuitOreDictName(4), CI.component_Plate[5], ItemList.Hatch_Input_IV.get(1),
				CI.component_Plate[5], GregtechItemList.Hatch_Input_Cryotheum.get(1L, new Object[0]));

		RecipeUtils.addShapedGregtechRecipe(CI.component_Plate[5], ALLOY.MARAGING300.getGear(1), CI.component_Plate[5],
				CI.getTieredCircuitOreDictName(4), GregtechItemList.Casing_Adv_BlastFurnace.get(1),
				CI.getTieredCircuitOreDictName(4), CI.component_Plate[6], ItemList.Hatch_Input_IV.get(1),
				CI.component_Plate[6], GregtechItemList.Hatch_Input_Pyrotheum.get(1L, new Object[0]));

		RecipeUtils.addShapedGregtechRecipe(CI.component_Plate[8], ALLOY.PIKYONIUM.getGear(1), CI.component_Plate[9],
				CI.getTieredCircuitOreDictName(7), GregtechItemList.Casing_Naq_Reactor_A.get(1),
				CI.getTieredCircuitOreDictName(7), CI.component_Plate[9], ItemList.Hatch_Input_ZPM.get(1),
				CI.component_Plate[8], GregtechItemList.Hatch_Input_Naquadah.get(1L, new Object[0]));


		if (CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {
			GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_LV.get(1L, new Object[0]), bitsd,
					new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_LV.get(1), Character.valueOf('P'),
							GregtechItemList.Pollution_Cleaner_LV.get(1) });
			GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_MV.get(1L, new Object[0]), bitsd,
					new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_MV.get(1), Character.valueOf('P'),
							GregtechItemList.Pollution_Cleaner_MV.get(1) });
			GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_HV.get(1L, new Object[0]), bitsd,
					new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_HV.get(1), Character.valueOf('P'),
							GregtechItemList.Pollution_Cleaner_HV.get(1) });
			GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_EV.get(1L, new Object[0]), bitsd,
					new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_EV.get(1), Character.valueOf('P'),
							GregtechItemList.Pollution_Cleaner_EV.get(1) });		
			GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_IV.get(1L, new Object[0]), bitsd,
					new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_IV.get(1), Character.valueOf('P'),
							GregtechItemList.Pollution_Cleaner_IV.get(1) });
			GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_LuV.get(1L, new Object[0]), bitsd,
					new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_LuV.get(1), Character.valueOf('P'),
							GregtechItemList.Pollution_Cleaner_LuV.get(1) });
			GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_ZPM.get(1L, new Object[0]), bitsd,
					new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_ZPM.get(1), Character.valueOf('P'),
							GregtechItemList.Pollution_Cleaner_ZPM.get(1) });
			GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_UV.get(1L, new Object[0]), bitsd,
					new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_UV.get(1), Character.valueOf('P'),
							GregtechItemList.Pollution_Cleaner_UV.get(1) });
			GT_ModHandler.addCraftingRecipe(GregtechItemList.Hatch_Muffler_Adv_MAX.get(1L, new Object[0]), bitsd,
					new Object[] { "M", "P", Character.valueOf('M'), ItemList.Hatch_Muffler_MAX.get(1), Character.valueOf('P'),
							GregtechItemList.Pollution_Cleaner_MAX.get(1) });
		}










	}









}


