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
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class HANDLER_Railcraft {
	
	

	public static void preInit() {
		if (LoadedMods.Railcraft) {
			
		}
		
		//Register Custom Coal Coke
		ModItems.itemCoalCoke = new BaseItemBurnable("itemCoalCoke", "Coking Coal", tabMisc, 64, 0, "Used for metallurgy.", "fuelCoke", 3200, 0).setTextureName(CORE.MODID + ":burnables/itemCoalCoke");
		
		//Add in things that once existed in 1.5.2
		ModItems.itemCactusCharcoal = new BaseItemBurnable("itemCactusCharcoal", "Cactus Charcoal", tabMisc, 64, 0, "Used for smelting.", "fuelCactusCharcoal", 400, 0).setTextureName(CORE.MODID + ":burnables/itemCactusCharcoal");
		ModItems.itemSugarCharcoal = new BaseItemBurnable("itemSugarCharcoal", "Sugar Charcoal", tabMisc, 64, 0, "Used for smelting.", "fuelSugarCharcoal", 400, 0).setTextureName(CORE.MODID + ":burnables/itemSugarCharcoal");
		ModItems.itemCactusCoke = new BaseItemBurnable("itemCactusCoke", "Cactus Coke", tabMisc, 64, 0, "Used for smelting.", "fuelCactusCoke", 800, 0).setTextureName(CORE.MODID + ":burnables/itemCactusCoke");
		ModItems.itemSugarCoke = new BaseItemBurnable("itemSugarCoke", "Sugar Coke", tabMisc, 64, 0, "Used for smelting.", "fuelSugarCoke", 800, 0).setTextureName(CORE.MODID + ":burnables/itemSugarCoke");

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
		ItemStack[] aInputs1 = new ItemStack[] {
				ItemUtils.getSimpleStack(Blocks.cactus),
				ItemUtils.getSimpleStack(Items.reeds)
		};
		ItemStack[] aInputs2 = new ItemStack[] {
				ItemUtils.getSimpleStack(ModItems.itemCactusCharcoal),
				ItemUtils.getSimpleStack(ModItems.itemSugarCharcoal)
		};
		ItemStack[] aOutputs = new ItemStack[] {
				ItemUtils.getSimpleStack(ModItems.itemCactusCoke),
				ItemUtils.getSimpleStack(ModItems.itemSugarCoke)
				};
		for (int i=0;i<aOutputs.length;i++) {
			CORE.RA.addCokeOvenRecipe(aInputs1[i], CI.getNumberedCircuit(3), null, FluidUtils.getFluidStack("creosote", 30), aInputs2[i], 125, 16);			
			CORE.RA.addCokeOvenRecipe(aInputs2[i], CI.getNumberedCircuit(4), null, FluidUtils.getFluidStack("creosote", 30), aOutputs[i], 125, 16);			
		}
		if (LoadedMods.Railcraft) {		
			for (int i=0;i<aOutputs.length;i++) {
				RailcraftUtils.addCokeOvenRecipe(aInputs1[i], true, true, aInputs2[i], FluidUtils.getFluidStack("creosote", 30), 500);			
			}
			for (int i=0;i<aOutputs.length;i++) {
				RailcraftUtils.addCokeOvenRecipe(aInputs2[i], true, true, aOutputs[i], FluidUtils.getFluidStack("creosote", 30), 500);			
			}			
		}
	}

}
