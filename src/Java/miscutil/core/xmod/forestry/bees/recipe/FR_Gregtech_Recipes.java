package miscutil.core.xmod.forestry.bees.recipe;

import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.recipe.UtilsRecipe;
import miscutil.core.xmod.forestry.bees.items.FR_ItemRegistry;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class FR_Gregtech_Recipes {
	
	private static String rod_ElectrumFlux = "stickElectrumFlux";
	private static String rod_LongElectrumFlux = "stickLongElectrumFlux";
	private static String foil_Electrum = "foilElectrum";
	private static String rod_Uranium = "stickUranium";
	private static String rod_LongUranium = "stickLongUranium";
	private static String foil_Uranium235 = "foilUranium235";
	private static ItemStack hiveFrameAccelerated = UtilsItems.getSimpleStack(FR_ItemRegistry.hiveFrameAccelerated);
	private static ItemStack hiveFrameMutagenic = UtilsItems.getSimpleStack(FR_ItemRegistry.hiveFrameMutagenic);
	

	private static ItemStack hiveFrameCocoa = UtilsItems.getSimpleStack(FR_ItemRegistry.hiveFrameCocoa);
	private static ItemStack hiveFrameCaged = UtilsItems.getSimpleStack(FR_ItemRegistry.hiveFrameCaged);
	private static ItemStack hiveFrameSoul = UtilsItems.getSimpleStack(FR_ItemRegistry.hiveFrameSoul);
	private static ItemStack hiveFrameClay = UtilsItems.getSimpleStack(FR_ItemRegistry.hiveFrameClay);
	private static ItemStack hiveFrameNova = UtilsItems.getSimpleStack(FR_ItemRegistry.hiveFrameNova);
	
	private static ItemStack hiveFrameImpregnated = UtilsItems.getItemStack("Forestry:frameImpregnated", 1);
	private static ItemStack blockSoulSand = new ItemStack(Blocks.soul_sand, 1);
	private static ItemStack blockIronBars = new ItemStack (Blocks.iron_bars, 1);
	private static ItemStack itemClayDust = new ItemStack(Items.clay_ball, 1);
	private static ItemStack itemCocoaBeans = new ItemStack(Items.dye, 1, 3);


	public static void registerItems(){
		
		//Magic Bee Like Frames
		UtilsRecipe.recipeBuilder(
				rod_LongElectrumFlux, rod_ElectrumFlux, rod_LongElectrumFlux,
				rod_LongElectrumFlux, foil_Electrum, rod_LongElectrumFlux,
				rod_ElectrumFlux, rod_ElectrumFlux, rod_ElectrumFlux,
				hiveFrameAccelerated);
		
		UtilsRecipe.recipeBuilder(
				rod_LongUranium, rod_Uranium, rod_LongUranium,
				rod_LongUranium, foil_Uranium235, rod_LongUranium,
				rod_Uranium, rod_Uranium, rod_Uranium,
				hiveFrameMutagenic);
		
		//Extra Bee Like Frames
		UtilsRecipe.recipeBuilder(
				null, itemCocoaBeans, null,
				itemCocoaBeans, hiveFrameImpregnated, itemCocoaBeans,
				null, itemCocoaBeans, null,
				hiveFrameCocoa);
		
		UtilsRecipe.recipeBuilder(
				hiveFrameImpregnated, blockIronBars, null,
				null, null, null,
				null, null, null,
				hiveFrameCaged);
		
		UtilsRecipe.recipeBuilder(
				hiveFrameImpregnated, blockSoulSand, null,
				null, null, null,
				null, null, null,
				hiveFrameSoul);
		
		UtilsRecipe.recipeBuilder(
				null, itemClayDust, null,
				itemClayDust, hiveFrameImpregnated, itemClayDust,
				null, itemClayDust, null,
				hiveFrameClay);
		
		
		
	}
	
}
