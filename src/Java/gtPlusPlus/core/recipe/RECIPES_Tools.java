package gtPlusPlus.core.recipe;

import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class RECIPES_Tools {

	// Outputs
	public static ItemStack	RECIPE_StaballoyPickaxe	= new ItemStack(ModItems.itemStaballoyPickaxe);
	public static ItemStack	RECIPE_StaballoyAxe		= new ItemStack(ModItems.itemStaballoyAxe);
	public static ItemStack	RECIPE_SandstoneHammer	= new ItemStack(ModItems.itemSandstoneHammer);
	// public static ItemStack RECIPE_StaballoyIngot = new
	// ItemStack(ModItems.itemIngotStaballoy);
	public static ItemStack	RECIPE_SandStone		= new ItemStack(Blocks.sandstone, 2);
	public static ItemStack	RECIPE_Sand				= new ItemStack(Blocks.sand, 4);

	public static ItemStack	RECIPE_DivisionSigil;

	// MC Items
	public static Item		Apple					= Items.apple;

	// MC Blocks
	public static Block		CobbleStone				= Blocks.cobblestone;
	public static Block		Dirt					= Blocks.dirt;
	public static Block		Sand					= Blocks.sand;
	public static Block		Gravel					= Blocks.gravel;
	public static Block		Sandstone				= Blocks.sandstone;

	// null
	public static String	empty					= " ";

	// Batteries
	public static String	batteryBasic			= "batteryBasic";
	public static String	batteryAdvanced			= "batteryAdvanced";
	public static String	batteryElite			= "batteryElite";
	public static String	batteryMaster			= "batteryMaster";
	public static String	batteryUltimate			= "batteryUltimate";

	// Circuits
	public static String	circuitPrimitive		= "circuitPrimitive";
	public static String	circuitBasic			= "circuitBasic";
	public static String	circuitGood				= "circuitGood";
	public static String	circuitAdvanced			= "circuitAdvanced";
	public static String	circuitElite			= "circuitElite";
	public static String	circuitMaster			= "circuitMaster";
	public static String	circuitUltimate			= "circuitUltimate";

	// Cables
	public static String	cableGt01Electrum		= "cableGt01Electrum";
	public static String	cableGt02Electrum		= "cableGt02Electrum";
	public static String	cableGt01RedstoneAlloy	= "cableGt01RedstoneAlloy";
	public static String	cableGt02RedstoneAlloy	= "cableGt02RedstoneAlloy";
	public static String	cableGt01Copper			= "cableGt01Copper";
	public static String	cableGt02Copper			= "cableGt02Copper";
	public static String	cableGt01AnnealedCopper	= "cableGt01AnnealedCopper";
	public static String	cableGt02AnnealedCopper	= "cableGt02AnnealedCopper";

	// Rods
	public static String	stickStaballoy			= "stickStaballoy";
	public static String	stickTitanium			= "stickTitanium";
	public static String	stickIron				= "stickIron";
	public static String	stickGold				= "stickGold";
	public static String	stickSilver				= "stickSilver";
	public static String	stickSteel				= "stickSteel";
	public static String	stickBronze				= "stickBronze";
	public static String	stickTungsten			= "stickTungsten";
	public static String	stickRedAlloy			= "stickRedAlloy";
	public static String	stickInvar				= "stickInvar";
	public static String	stickElectrum			= "stickElectrum";
	public static String	stickElectricalSteel	= "stickElectricalSteel";

	// Plates
	public static String	plateStaballoy			= "plateStaballoy";
	public static String	plateTitanium			= "plateTitanium";
	public static String	plateIron				= "plateIron";
	public static String	plateGold				= "plateGold";
	public static String	plateSilver				= "plateSilver";
	public static String	plateSteel				= "plateSteel";
	public static String	plateBronze				= "plateBronze";
	public static String	plateTungsten			= "plateTungsten";
	public static String	plateRedAlloy			= "plateRedAlloy";
	public static String	plateInvar				= "plateInvar";
	public static String	plateElectrum			= "plateElectrum";
	public static String	plateElectricalSteel	= "plateElectricalSteel";

	// Ingots
	public static String	ingotStaballoy			= "ingotStaballoy";
	public static String	ingotTitanium			= "ingotTitanium";
	public static String	ingotIron				= "ingotIron";
	public static String	ingotGold				= "ingotGold";
	public static String	ingotSilver				= "ingotSilver";
	public static String	ingotSteel				= "ingotSteel";
	public static String	ingotBronze				= "ingotBronze";
	public static String	ingotTungsten			= "ingotTungsten";
	public static String	ingotRedAlloy			= "ingotRedAlloy";
	public static String	ingotInvar				= "ingotInvar";
	public static String	ingotElectrum			= "ingotElectrum";
	public static String	ingotUranium			= "ingotUranium";
	public static String	ingotElectricalSteel	= "ingotElectricalSteel";

	// Crafting Tools
	public static String	craftingToolHardHammer	= "craftingToolHardHammer";
	public static String	craftingToolSoftHammer	= "craftingToolSoftHammer";
	public static String	craftingToolFile		= "craftingToolFile";
	public static String	craftingToolSaw			= "craftingToolSaw";
	public static String	craftingToolPickaxe		= "craftingToolPickaxe";
	public static String	craftingToolWrench		= "craftingToolWrench";
	public static String	craftingToolCrowbar		= "craftingToolCrowbar";
	public static String	craftingToolKnife		= "craftingToolKnife";
	public static String	craftingToolScrewdriver	= "craftingToolScrewdriver";

	public static ItemStack	sandHammer				= new ItemStack(ModItems.itemSandstoneHammer, 1,
			OreDictionary.WILDCARD_VALUE);
	public static String	craftingToolSandHammer	= "craftingToolSandHammer";

	public static ItemStack	personalCloakingDevice	= ItemUtils.getSimpleStack(ModItems.itemPersonalCloakingDevice);
	public static String	plateDoubleNiChrome		= "plateDoubleNichrome";
	public static String	plateIridiumAlloy		= "plateAlloyIridium";

	public static final void RECIPES_LOAD() {

		RECIPES_Tools.run();

	}

	private static void run() {
		// Staballoy Pickaxe
		RecipeUtils.recipeBuilder(RECIPES_Tools.plateStaballoy, RECIPES_Tools.plateStaballoy,
				RECIPES_Tools.ingotStaballoy, RECIPES_Tools.craftingToolFile, RECIPES_Tools.stickTungsten,
				RECIPES_Tools.craftingToolHardHammer, RECIPES_Tools.craftingToolWrench, RECIPES_Tools.stickTungsten,
				RECIPES_Tools.craftingToolHardHammer, RECIPES_Tools.RECIPE_StaballoyPickaxe);

		// Staballoy Axe
		RecipeUtils.recipeBuilder(RECIPES_Tools.plateStaballoy, RECIPES_Tools.ingotStaballoy,
				RECIPES_Tools.craftingToolHardHammer, RECIPES_Tools.plateStaballoy, RECIPES_Tools.stickTungsten,
				RECIPES_Tools.craftingToolHardHammer, RECIPES_Tools.craftingToolFile, RECIPES_Tools.stickTungsten,
				RECIPES_Tools.craftingToolWrench, RECIPES_Tools.RECIPE_StaballoyAxe);

		// Cobble to Sand
		RecipeUtils.recipeBuilder(RECIPES_Tools.CobbleStone, RECIPES_Tools.CobbleStone, RECIPES_Tools.CobbleStone,
				RECIPES_Tools.CobbleStone, RECIPES_Tools.sandHammer, RECIPES_Tools.CobbleStone,
				RECIPES_Tools.CobbleStone, RECIPES_Tools.CobbleStone, RECIPES_Tools.CobbleStone,
				RECIPES_Tools.RECIPE_Sand);

		if (LoadedMods.Baubles) {
			// Cloaking Device
			RecipeUtils.recipeBuilder(RECIPES_Tools.plateDoubleNiChrome, RECIPES_Tools.plateIridiumAlloy,
					RECIPES_Tools.plateDoubleNiChrome, RECIPES_Tools.plateIridiumAlloy, RECIPES_Tools.batteryUltimate,
					RECIPES_Tools.plateIridiumAlloy, RECIPES_Tools.plateDoubleNiChrome, RECIPES_Tools.plateIridiumAlloy,
					RECIPES_Tools.plateDoubleNiChrome, RECIPES_Tools.personalCloakingDevice);

		}
		// Sand to Sandstone
		RecipeUtils.recipeBuilder(RECIPES_Tools.Sand, RECIPES_Tools.Sand, RECIPES_Tools.Sand, RECIPES_Tools.Sand,
				RECIPES_Tools.sandHammer, RECIPES_Tools.Sand, RECIPES_Tools.Sand, RECIPES_Tools.Sand,
				RECIPES_Tools.Sand, RECIPES_Tools.RECIPE_SandStone);

		// Sandstone Hammer
		RecipeUtils.recipeBuilder(RECIPES_Tools.plateElectrum, RECIPES_Tools.ingotElectrum, RECIPES_Tools.plateElectrum,
				RECIPES_Tools.craftingToolScrewdriver, RECIPES_Tools.stickBronze, RECIPES_Tools.craftingToolHardHammer,
				null, RECIPES_Tools.stickSteel, null, RECIPES_Tools.RECIPE_SandstoneHammer);

	}

}
