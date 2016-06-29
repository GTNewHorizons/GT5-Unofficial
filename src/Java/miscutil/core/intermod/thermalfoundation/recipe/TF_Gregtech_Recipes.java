package miscutil.core.intermod.thermalfoundation.recipe;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.intermod.thermalfoundation.item.TF_Items;
import miscutil.core.util.Utils;
import miscutil.core.util.item.UtilsItems;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class TF_Gregtech_Recipes {

	public static void run(){
		start();
	}

	private static void start(){
		//Get Items to work with
		ItemStack dust_Cryotheum = TF_Items.itemDustCryotheum.copy();
		ItemStack dust_Pyrotheum = TF_Items.itemDustPyrotheum.copy();
		ItemStack dust_Blizz = TF_Items.itemDustBlizz.copy();
		ItemStack dust_Blizz3 = UtilsItems.simpleMetaStack(TF_Items.itemMaterial, 2, 3);
		ItemStack rod_Blizz = TF_Items.itemRodBlizz.copy();	

		//Gelid Cryotheum
		Utils.LOG_INFO("Adding Recipes for Gelid Cryotheum");
		GT_Values.RA.addFluidExtractionRecipe(dust_Cryotheum, GT_Values.NI, getFluidStack("cryotheum", 250), 10000, 32, 2);
		GT_Values.RA.addChemicalBathRecipe((GT_OreDictUnificator.get(OrePrefixes.ore, Materials.Cinnabar, 1L)), getFluidStack("cryotheum", 200), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cinnabar, 2L), GT_Values.NI, GT_Values.NI, null, 400, 2);

		//Blizz Powder
		Utils.LOG_INFO("Adding Recipes for Blizz Powder");
		GT_Values.RA.addChemicalBathRecipe(new ItemStack(Items.snowball, 1), getFluidStack("molten.redstone", 200), dust_Blizz, GT_Values.NI, GT_Values.NI, null, 400, 2);

		//Blizz Rod
		Utils.LOG_INFO("Adding Recipes for Blizz Rod");
		GT_ModHandler.addPulverisationRecipe(rod_Blizz, dust_Blizz3, new ItemStack(Items.snowball, 1), 50, false);

		//Blazing Pyrotheum
		Utils.LOG_INFO("Adding Recipes for Blazing Pyrotheum");	
		GT_Values.RA.addFluidExtractionRecipe(dust_Pyrotheum, GT_Values.NI, getFluidStack("pyrotheum", 250), 10000, 32, 2);	

	}

	private static FluidStack getFluidStack(String fluidName, int amount){
		Utils.LOG_INFO("Trying to get a fluid stack of "+fluidName);
		try {
			return FluidRegistry.getFluidStack(fluidName, amount);
		} 
		catch (Throwable e){
			return null;
		}

	}

}
