package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import gregtech.common.tileentities.machines.basic.GT_MetaTileEntity_Massfabricator;
import gregtech.api.util.GTPP_Recipe;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_MassFabricator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GregtechIndustrialMassFabricator {

	public static void run() {
		if (gtPlusPlus.core.lib.LoadedMods.Gregtech) {
			Logger.INFO("Gregtech5u Content | Registering Industrial Matter Fabricator Multiblock.");
			if (CORE.ConfigSwitches.enableMultiblock_MatterFabricator) {
				generateRecipes();
				run1();
			}
		}

	}

	private static void run1() {
		// Industrial Matter Fabricator Multiblock
		GregtechItemList.Industrial_MassFab.set(new GregtechMetaTileEntity_MassFabricator(799,
				"industrialmassfab.controller.tier.single", "Matter Fabrication CPU").getStackForm(1L));
	}

	private static void generateRecipes() {

		//Generate Scrap->UUA Recipes



		//Basic UUA1
		GT_Recipe UUA_From_Scrap = new GTPP_Recipe(
				false, 
				new ItemStack[] {CI.getNumberedCircuit(9), ItemUtils.getSimpleStack(getScrapPile(), 9)}, 
				new ItemStack[] {GT_Values.NI}, 
				null, null, 
				new FluidStack[] {GT_Values.NF}, 
				new FluidStack[] {Materials.UUAmplifier.getFluid(1)},
				9*20, 
				32, 
				0);
		//Basic UUA2
		GT_Recipe UUA_From_ScrapBoxes = new GTPP_Recipe(
				false, 
				new ItemStack[] {CI.getNumberedCircuit(19), ItemUtils.getSimpleStack(getScrapBox(), 1)}, 
				new ItemStack[] {GT_Values.NI}, 
				null, null, 
				new FluidStack[] {GT_Values.NF}, 
				new FluidStack[] {Materials.UUAmplifier.getFluid(1)},
				9*20, 
				32, 
				0);

		GTPP_Recipe.GTPP_Recipe_Map.sMatterFab2Recipes.add(UUA_From_Scrap);
		GTPP_Recipe.GTPP_Recipe_Map.sMatterFab2Recipes.add(UUA_From_ScrapBoxes);		

		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {					
			//Basic UUM
			GT_Recipe generateUUM = new GTPP_Recipe(
					false, 
					new ItemStack[] {CI.getNumberedCircuit(1)}, 
					new ItemStack[] {GT_Values.NI}, 
					null, null, 
					new FluidStack[] {GT_Values.NF}, 
					new FluidStack[] {Materials.UUMatter.getFluid(1)},
					160*20, 
					32, 
					0);

			//Basic UUM
			GT_Recipe generateUUMFromUUA = new GTPP_Recipe(
					false, 
					new ItemStack[] {CI.getNumberedCircuit(2)}, 
					new ItemStack[] {GT_Values.NI}, 
					null, null, 
					new FluidStack[] {Materials.UUAmplifier.getFluid(1)}, 
					new FluidStack[] {Materials.UUMatter.getFluid(1)},
					40*20, 
					32, 
					0);

			GTPP_Recipe.GTPP_Recipe_Map.sMatterFab2Recipes.add(generateUUM);
			GTPP_Recipe.GTPP_Recipe_Map.sMatterFab2Recipes.add(generateUUMFromUUA);			
		}
		else {

			GT_Recipe.GT_Recipe_Map.sMassFabFakeRecipes.addFakeRecipe(false, null, null, null, null, new FluidStack[]{Materials.UUMatter.getFluid(1L)}, GT_MetaTileEntity_Massfabricator.sDurationMultiplier, 256, 0);
			GT_Recipe.GT_Recipe_Map.sMassFabFakeRecipes.addFakeRecipe(false, null, null, null, new FluidStack[]{Materials.UUAmplifier.getFluid(GT_MetaTileEntity_Massfabricator.sUUAperUUM)}, new FluidStack[]{Materials.UUMatter.getFluid(1L)}, GT_MetaTileEntity_Massfabricator.sDurationMultiplier / GT_MetaTileEntity_Massfabricator.sUUASpeedBonus, 256, 0);

			//Basic UUM
			GT_Recipe generateUUM_LV = new GTPP_Recipe(
					false, 
					new ItemStack[] {CI.getNumberedCircuit(1)}, 
					new ItemStack[] {GT_Values.NI}, 
					null, null, 
					new FluidStack[] {GT_Values.NF}, 
					new FluidStack[] {Materials.UUMatter.getFluid(1)},
					160*20, 
					256, 
					0);

			//Basic UUM
			GT_Recipe generateUUMFromUUA_LV = new GTPP_Recipe(
					false, 
					new ItemStack[] {CI.getNumberedCircuit(2)}, 
					new ItemStack[] {GT_Values.NI}, 
					null, null, 
					new FluidStack[] {Materials.UUAmplifier.getFluid(1)}, 
					new FluidStack[] {Materials.UUMatter.getFluid(1)},
					40*20, 
					256, 
					0);

			GTPP_Recipe.GTPP_Recipe_Map.sMatterFab2Recipes.add(generateUUM_LV);
			GTPP_Recipe.GTPP_Recipe_Map.sMatterFab2Recipes.add(generateUUMFromUUA_LV);	

		}

		Logger.INFO("Generated "+GTPP_Recipe.GTPP_Recipe_Map.sMatterFab2Recipes.mRecipeList.size()+" Matter Fabricator recipes.");




	}



	public static ItemStack getScrapPile() {
		return ItemUtils.getSimpleStack(ItemUtils.getItemFromFQRN("IC2:itemScrap"));
	}	
	public static ItemStack getScrapBox() {
		return ItemUtils.getSimpleStack(ItemUtils.getItemFromFQRN("IC2:itemScrapbox"));
	}

}