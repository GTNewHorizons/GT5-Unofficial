package miscutil.core.intermod.thermalfoundation.recipe;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.intermod.thermalfoundation.item.TF_Items;
import miscutil.core.util.Utils;
import miscutil.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class TF_Gregtech_Recipes {

	public static void run(){
		start();
	}
	
	private static void start(){
		//Get Items to work with
		Item dust_Cryotheum = TF_Items.dustCryotheum.getItem();
		Item dust_Pyrotheum = TF_Items.dustPyrotheum.getItem();
		Item dust_Blizz = TF_Items.dustBlizz.getItem();
		Item rod_Blizz = TF_Items.rodBlizz.getItem();		
		
		//Gelid Cryotheum
		Utils.LOG_INFO("Adding Recipes for Gelid Cryotheum");
		GT_Values.RA.addFluidExtractionRecipe(new ItemStack(dust_Cryotheum, 6664, 1), GT_Values.NI, GT_Materials.Cryotheum.getFluid(250L), 10000, 32, 2);
		GT_Values.RA.addChemicalBathRecipe((GT_OreDictUnificator.get(OrePrefixes.ore, Materials.Cinnabar, 1L)), GT_Materials.Cryotheum.getFluid(200L), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cinnabar, 2L), GT_Values.NI, GT_Values.NI, null, 400, 2);
		
		//Blizz Powder
		Utils.LOG_INFO("Adding Recipes for Blizz Powder");
		GT_Values.RA.addChemicalBathRecipe(new ItemStack(Items.snowball, 1), Materials.Redstone.getFluid(200L), new ItemStack(dust_Blizz, 6666, 1), GT_Values.NI, GT_Values.NI, null, 400, 2);
		
		//Blizz Rod
		Utils.LOG_INFO("Adding Recipes for Blizz Rod");
		GT_ModHandler.addPulverisationRecipe(new ItemStack(rod_Blizz, 6665, 1), new ItemStack(dust_Blizz, 6666, 3), new ItemStack(Items.snowball, 1), 50, false);
		
		
		//Blazing Pyrotheum
		GT_Values.RA.addFluidExtractionRecipe(new ItemStack(dust_Pyrotheum, 6663, 1), GT_Values.NI, GT_Materials.Pyrotheum.getFluid(250L), 10000, 32, 2);
		Utils.LOG_INFO("Adding Recipes for Blazing Pyrotheum");		
				
	}
	
}
