package gtPlusPlus.xmod.thermalfoundation.recipe;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.xmod.thermalfoundation.item.TF_Items;
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
		final ItemStack dust_Cryotheum = TF_Items.itemDustCryotheum.copy();
		final ItemStack dust_Pyrotheum = TF_Items.itemDustPyrotheum.copy();
		final ItemStack dust_Blizz = TF_Items.itemDustBlizz.copy();
		final ItemStack dust_Blizz3 = ItemUtils.simpleMetaStack(TF_Items.itemMaterial, 2, 3);
		final ItemStack rod_Blizz = TF_Items.itemRodBlizz.copy();
		final FluidStack moltenRedstone = getFluidStack("molten.redstone", 250);

		//Gelid Cryotheum
		Logger.INFO("Adding Recipes for Gelid Cryotheum");
		GT_Values.RA.addFluidExtractionRecipe(dust_Cryotheum, GT_Values.NI, getFluidStack("cryotheum", 250), 10000, 200, 240);
		GT_Values.RA.addChemicalBathRecipe((GT_OreDictUnificator.get(OrePrefixes.ore, Materials.Cinnabar, 1L)), getFluidStack("cryotheum", 200), GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cinnabar, 3L), GT_Values.NI, GT_Values.NI, null, 400, 30);

		//Blizz Powder
		Logger.INFO("Adding Recipes for Blizz Powder");
		GT_Values.RA.addChemicalBathRecipe(new ItemStack(Items.snowball, 4), moltenRedstone, dust_Blizz, GT_Values.NI, GT_Values.NI, null, 400, 240);

		//Blizz Rod
		Logger.INFO("Adding Recipes for Blizz Rod");
		GT_Values.RA.addVacuumFreezerRecipe(new ItemStack(Items.blaze_rod), rod_Blizz, (int) Math.max((Materials.Blaze.getMass()*4) * 3L, 1L));
		GT_ModHandler.addPulverisationRecipe(rod_Blizz, dust_Blizz3, new ItemStack(Items.snowball, 1), 50, false);

		//Blazing Pyrotheum
		Logger.INFO("Adding Recipes for Blazing Pyrotheum");
		GT_Values.RA.addFluidExtractionRecipe(dust_Pyrotheum, GT_Values.NI, getFluidStack("pyrotheum", 250), 10000, 200, 240);

	}

	private static FluidStack getFluidStack(final String fluidName, final int amount){
		Logger.WARNING("Trying to get a fluid stack of "+fluidName);
		try {
			return FluidRegistry.getFluidStack(fluidName, amount);
		}
		catch (final Throwable e){
			return null;
		}

	}

}
