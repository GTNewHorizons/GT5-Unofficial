package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.Recipe_GT;
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
		GT_Recipe UUA_From_Scrap = new Recipe_GT(
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
		GT_Recipe UUA_From_ScrapBoxes = new Recipe_GT(
				false, 
				new ItemStack[] {CI.getNumberedCircuit(19), ItemUtils.getSimpleStack(getScrapBox(), 1)}, 
				new ItemStack[] {GT_Values.NI}, 
				null, null, 
				new FluidStack[] {GT_Values.NF}, 
				new FluidStack[] {Materials.UUAmplifier.getFluid(1)},
				9*20, 
				32, 
				0);

		Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.add(UUA_From_Scrap);
		Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.add(UUA_From_ScrapBoxes);		
		
		if (!CORE.MAIN_GREGTECH_5U_EXPERIMENTAL_FORK) {					
			//Basic UUM
			GT_Recipe generateUUM = new Recipe_GT(
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
			GT_Recipe generateUUMFromUUA = new Recipe_GT(
					false, 
					new ItemStack[] {CI.getNumberedCircuit(2)}, 
					new ItemStack[] {GT_Values.NI}, 
					null, null, 
					new FluidStack[] {Materials.UUAmplifier.getFluid(1)}, 
					new FluidStack[] {Materials.UUMatter.getFluid(1)},
					40*20, 
					32, 
					0);

			Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.add(generateUUM);
			Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.add(generateUUMFromUUA);			
		}
		else {

			//Basic UUM
			GT_Recipe generateUUM_LV = new Recipe_GT(
					false, 
					new ItemStack[] {CI.getNumberedCircuit(15)}, 
					new ItemStack[] {GT_Values.NI}, 
					null, null, 
					new FluidStack[] {GT_Values.NF}, 
					new FluidStack[] {Materials.UUMatter.getFluid(1)},
					160*20, 
					256, 
					0);
			GT_Recipe generateUUM_MV = new Recipe_GT(
					false, 
					new ItemStack[] {CI.getNumberedCircuit(14)}, 
					new ItemStack[] {GT_Values.NI}, 
					null, null, 
					new FluidStack[] {GT_Values.NF}, 
					new FluidStack[] {Materials.UUMatter.getFluid(1)},
					160*20, 
					512, 
					0);
			GT_Recipe generateUUM_HV = new Recipe_GT(
					false, 
					new ItemStack[] {CI.getNumberedCircuit(13)}, 
					new ItemStack[] {GT_Values.NI}, 
					null, null, 
					new FluidStack[] {GT_Values.NF}, 
					new FluidStack[] {Materials.UUMatter.getFluid(1)},
					160*20, 
					1024, 
					0);
			GT_Recipe generateUUM_EV = new Recipe_GT(
					false, 
					new ItemStack[] {CI.getNumberedCircuit(12)}, 
					new ItemStack[] {GT_Values.NI}, 
					null, null, 
					new FluidStack[] {GT_Values.NF}, 
					new FluidStack[] {Materials.UUMatter.getFluid(1)},
					160*20, 
					2048, 
					0);
			GT_Recipe generateUUM_IV = new Recipe_GT(
					false, 
					new ItemStack[] {CI.getNumberedCircuit(11)}, 
					new ItemStack[] {GT_Values.NI}, 
					null, null, 
					new FluidStack[] {GT_Values.NF}, 
					new FluidStack[] {Materials.UUMatter.getFluid(1)},
					160*20, 
					4096, 
					0);

			//Basic UUM
			GT_Recipe generateUUMFromUUA_LV = new Recipe_GT(
					false, 
					new ItemStack[] {CI.getNumberedCircuit(5)}, 
					new ItemStack[] {GT_Values.NI}, 
					null, null, 
					new FluidStack[] {Materials.UUAmplifier.getFluid(1)}, 
					new FluidStack[] {Materials.UUMatter.getFluid(1)},
					40*20, 
					256, 
					0);
			GT_Recipe generateUUMFromUUA_MV = new Recipe_GT(
					false, 
					new ItemStack[] {CI.getNumberedCircuit(4)}, 
					new ItemStack[] {GT_Values.NI}, 
					null, null, 
					new FluidStack[] {Materials.UUAmplifier.getFluid(1)}, 
					new FluidStack[] {Materials.UUMatter.getFluid(1)},
					40*20, 
					512, 
					0);
			GT_Recipe generateUUMFromUUA_HV = new Recipe_GT(
					false, 
					new ItemStack[] {CI.getNumberedCircuit(3)}, 
					new ItemStack[] {GT_Values.NI}, 
					null, null, 
					new FluidStack[] {Materials.UUAmplifier.getFluid(1)}, 
					new FluidStack[] {Materials.UUMatter.getFluid(1)},
					40*20, 
					1024, 
					0);
			GT_Recipe generateUUMFromUUA_EV = new Recipe_GT(
					false, 
					new ItemStack[] {CI.getNumberedCircuit(2)}, 
					new ItemStack[] {GT_Values.NI}, 
					null, null, 
					new FluidStack[] {Materials.UUAmplifier.getFluid(1)}, 
					new FluidStack[] {Materials.UUMatter.getFluid(1)},
					40*20, 
					2048, 
					0);
			GT_Recipe generateUUMFromUUA_IV = new Recipe_GT(
					false, 
					new ItemStack[] {CI.getNumberedCircuit(1)}, 
					new ItemStack[] {GT_Values.NI}, 
					null, null, 
					new FluidStack[] {Materials.UUAmplifier.getFluid(1)}, 
					new FluidStack[] {Materials.UUMatter.getFluid(1)},
					40*20, 
					4096, 
					0);

			Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.add(generateUUM_LV);
			Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.add(generateUUM_MV);
			Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.add(generateUUM_HV);
			Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.add(generateUUM_EV);
			Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.add(generateUUM_IV);
			Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.add(generateUUMFromUUA_LV);
			Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.add(generateUUMFromUUA_MV);
			Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.add(generateUUMFromUUA_HV);
			Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.add(generateUUMFromUUA_EV);
			Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.add(generateUUMFromUUA_IV);			

		}
		
		Logger.INFO("Generated "+Recipe_GT.Gregtech_Recipe_Map.sMatterFab2Recipes.mRecipeList.size()+" Matter Fabricator recipes.");

		
		

	}
	


	public static ItemStack getScrapPile() {
		return ItemUtils.getSimpleStack(ItemUtils.getItem("IC2:itemScrap"));
	}	
	public static ItemStack getScrapBox() {
		return ItemUtils.getSimpleStack(ItemUtils.getItem("IC2:itemScrapbox"));
	}
	
}