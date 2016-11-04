package gtPlusPlus.xmod.forestry.bees.recipe;

import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.recipe.RecipeUtils;
import gtPlusPlus.xmod.forestry.bees.items.FR_ItemRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class FR_Gregtech_Recipes {

	private static String		rod_ElectrumFlux		= "stickElectrumFlux";
	private static String		rod_LongElectrumFlux	= "stickLongElectrumFlux";
	private static String		foil_Electrum			= "foilElectrum";
	private static String		rod_Uranium				= "stickUranium";
	private static String		rod_LongUranium			= "stickLongUranium";
	private static String		foil_Uranium235			= "foilUranium235";
	private static ItemStack	hiveFrameAccelerated	= ItemUtils
			.getSimpleStack(FR_ItemRegistry.hiveFrameAccelerated);
	private static ItemStack	hiveFrameMutagenic		= ItemUtils.getSimpleStack(FR_ItemRegistry.hiveFrameMutagenic);

	private static ItemStack	hiveFrameCocoa			= ItemUtils.getSimpleStack(FR_ItemRegistry.hiveFrameCocoa);
	private static ItemStack	hiveFrameCaged			= ItemUtils.getSimpleStack(FR_ItemRegistry.hiveFrameCaged);
	private static ItemStack	hiveFrameSoul			= ItemUtils.getSimpleStack(FR_ItemRegistry.hiveFrameSoul);
	private static ItemStack	hiveFrameClay			= ItemUtils.getSimpleStack(FR_ItemRegistry.hiveFrameClay);
	private static ItemStack	hiveFrameNova			= ItemUtils.getSimpleStack(FR_ItemRegistry.hiveFrameNova);

	private static ItemStack	hiveFrameImpregnated	= ItemUtils.getItemStack("Forestry:frameImpregnated", 1);
	private static ItemStack	blockSoulSand			= new ItemStack(Blocks.soul_sand, 1);
	private static ItemStack	blockIronBars			= new ItemStack(Blocks.iron_bars, 1);
	private static ItemStack	itemClayDust			= new ItemStack(Items.clay_ball, 1);
	private static ItemStack	itemCocoaBeans			= new ItemStack(Items.dye, 1, 3);

	public static void registerItems() {

		// Magic Bee Like Frames
		RecipeUtils.recipeBuilder(FR_Gregtech_Recipes.rod_LongElectrumFlux, FR_Gregtech_Recipes.rod_ElectrumFlux,
				FR_Gregtech_Recipes.rod_LongElectrumFlux, FR_Gregtech_Recipes.rod_LongElectrumFlux,
				FR_Gregtech_Recipes.foil_Electrum, FR_Gregtech_Recipes.rod_LongElectrumFlux,
				FR_Gregtech_Recipes.rod_ElectrumFlux, FR_Gregtech_Recipes.rod_ElectrumFlux,
				FR_Gregtech_Recipes.rod_ElectrumFlux, FR_Gregtech_Recipes.hiveFrameAccelerated);

		RecipeUtils.recipeBuilder(FR_Gregtech_Recipes.rod_LongUranium, FR_Gregtech_Recipes.rod_Uranium,
				FR_Gregtech_Recipes.rod_LongUranium, FR_Gregtech_Recipes.rod_LongUranium,
				FR_Gregtech_Recipes.foil_Uranium235, FR_Gregtech_Recipes.rod_LongUranium,
				FR_Gregtech_Recipes.rod_Uranium, FR_Gregtech_Recipes.rod_Uranium, FR_Gregtech_Recipes.rod_Uranium,
				FR_Gregtech_Recipes.hiveFrameMutagenic);

		if (!LoadedMods.ExtraBees) {
			// Extra Bee Like Frames
			RecipeUtils.recipeBuilder(null, FR_Gregtech_Recipes.itemCocoaBeans, null,
					FR_Gregtech_Recipes.itemCocoaBeans, FR_Gregtech_Recipes.hiveFrameImpregnated,
					FR_Gregtech_Recipes.itemCocoaBeans, null, FR_Gregtech_Recipes.itemCocoaBeans, null,
					FR_Gregtech_Recipes.hiveFrameCocoa);

			RecipeUtils.recipeBuilder(FR_Gregtech_Recipes.hiveFrameImpregnated, FR_Gregtech_Recipes.blockIronBars, null,
					null, null, null, null, null, null, FR_Gregtech_Recipes.hiveFrameCaged);

			RecipeUtils.recipeBuilder(FR_Gregtech_Recipes.hiveFrameImpregnated, FR_Gregtech_Recipes.blockSoulSand, null,
					null, null, null, null, null, null, FR_Gregtech_Recipes.hiveFrameSoul);

			RecipeUtils.recipeBuilder(null, FR_Gregtech_Recipes.itemClayDust, null, FR_Gregtech_Recipes.itemClayDust,
					FR_Gregtech_Recipes.hiveFrameImpregnated, FR_Gregtech_Recipes.itemClayDust, null,
					FR_Gregtech_Recipes.itemClayDust, null, FR_Gregtech_Recipes.hiveFrameClay);
		}

	}

}
