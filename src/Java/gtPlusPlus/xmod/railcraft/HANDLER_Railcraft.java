package gtPlusPlus.xmod.railcraft;

import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;

import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.BaseItemBurnable;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.railcraft.utils.RailcraftUtils;
import net.minecraft.item.ItemStack;

public class HANDLER_Railcraft {
	
	

	public static void preInit() {
		if (LoadedMods.Railcraft) {
			
		}
		
		//Register Custom Coal Coke
		ModItems.itemCoalCoke = new BaseItemBurnable("itemCoalCoke", "Coking Coal", tabMisc, 64, 0, "Used for metallurgy.", "fuelCoke", 3200, 0).setTextureName(CORE.MODID + ":itemCoalCoke");
		
		//Add in things that once existed in 1.5.2
		ModItems.itemCactusCharcoal = new BaseItemBurnable("itemCactusCharcoal", "Cactus Charcoal", tabMisc, 64, 0, "Used for smelting.", "fuelCactusCharcoal", 400, 0).setTextureName(CORE.MODID + ":itemCactusCharcoal");
		ModItems.itemSugarCharcoal = new BaseItemBurnable("itemSugarCharcoal", "Sugar Charcoal", tabMisc, 64, 0, "Used for smelting.", "fuelSugarCharcoal", 400, 0).setTextureName(CORE.MODID + ":itemSugarCharcoal");
		ModItems.itemCactusCoke = new BaseItemBurnable("itemCactusCoke", "Cactus Coke", tabMisc, 64, 0, "Used for smelting.", "fuelCactusCoke", 800, 0).setTextureName(CORE.MODID + ":itemCactusCoke");
		ModItems.itemSugarCoke = new BaseItemBurnable("itemSugarCoke", "Sugar Coke", tabMisc, 64, 0, "Used for smelting.", "fuelSugarCoke", 800, 0).setTextureName(CORE.MODID + ":itemSugarCoke");

		ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(ModItems.itemCactusCharcoal), "itemCharcoalCactus");
		ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(ModItems.itemCactusCoke), "itemCokeCactus");
		ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(ModItems.itemSugarCharcoal), "itemCharcoalSugar");
		ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(ModItems.itemSugarCoke), "itemCokeSugar");
		
	}

	public static void init() {
		if (LoadedMods.Railcraft) {
			
		}
	}

	public static void postInit() {
		if (LoadedMods.Railcraft) {
			
		}
		generateCokeOvenRecipes();
	}
	
	
	private static void generateCokeOvenRecipes() {
		ItemStack[] aInputs = new ItemStack[] {
				ItemUtils.getSimpleStack(ModItems.itemCactusCharcoal),
				ItemUtils.getSimpleStack(ModItems.itemSugarCharcoal)
		};
		ItemStack[] aOutputs = new ItemStack[] {
				ItemUtils.getSimpleStack(ModItems.itemCactusCoke),
				ItemUtils.getSimpleStack(ModItems.itemSugarCoke)
				};
		for (int i=0;i<aOutputs.length;i++) {
			CORE.RA.addCokeOvenRecipe(aInputs[i], CI.getNumberedCircuit(4), null, FluidUtils.getFluidStack("creosote", 30), aOutputs[i], 750, 16);			
		}

		if (LoadedMods.Railcraft) {			
			for (int i=0;i<aOutputs.length;i++) {
				RailcraftUtils.addCokeOvenRecipe(aInputs[i], true, true, aOutputs[i], FluidUtils.getFluidStack("creosote", 30), 750);			
			}			
		}		
	}

}
