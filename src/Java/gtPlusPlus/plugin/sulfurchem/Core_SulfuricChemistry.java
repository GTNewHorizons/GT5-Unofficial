package gtPlusPlus.plugin.sulfurchem;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Recipe;
import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.plugin.manager.Core_Manager;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class Core_SulfuricChemistry implements IPlugin {

	final static Core_SulfuricChemistry mInstance;
	private static boolean shouldLoad = false;

	static {
		mInstance = new Core_SulfuricChemistry();
		Core_Manager.registerPlugin(mInstance);
		Logger.INFO("Preparing "+mInstance.getPluginName()+" for use.");
	}

	@Override
	public boolean preInit() {
		if (CORE.ConfigSwitches.enableSulfuricAcidFix) {
			shouldLoad = true;
		}
		if (shouldLoad)
			return true;
		return false;
	}

	@Override
	public boolean init() {
		if (shouldLoad)
			return true;
		return false;
	}

	@Override
	public boolean postInit() {		
		if (shouldLoad) {
			int disabled = disableSulfurTrioxide();
			Logger.INFO("[RSCM] Disabled "+disabled+" Sulfur Trioxide Chemistry recipes.");
			disabled = disableSulfuricAcid();
			Logger.INFO("[RSCM] Disabled "+disabled+" Sulfuric Acid Chemistry recipes.");
			int addedNew = addRevisedGT6Recipes();
			Logger.INFO("[RSCM] Added "+addedNew+" new Sulfuric Chemistry recipes.");
			return disabled > 0 && addedNew > 0;
		}
		return false;
	}

	@Override
	public String getPluginName() {
		return "GT++ Revised Sulfuric Chemistry Module";
	}

	public int addRevisedGT6Recipes() {

		String catalyst = "dustPlatinum";
		int mCountAdded = 0;

		/**
		 * Sulfur Trioxide Recipes
		 */		
		//Air
		if (GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(catalyst, 0),
				ItemUtils.getItemStackOfAmountFromOreDict("cellAir", 1),
				FluidUtils.getFluidStack("sulfurdioxide", 3000),
				FluidUtils.getFluidStack("sulfurtrioxide", 4000),
				CI.emptyCells(1),
				null,
				16, 
				16)) {
			mCountAdded++;
		}
		if (GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(catalyst, 0),
				ItemUtils.getItemStackOfAmountFromOreDict("cellSulfurDioxide", 3),
				FluidUtils.getFluidStack("air", 1000),
				FluidUtils.getFluidStack("sulfurtrioxide", 4000),
				CI.emptyCells(3),
				null,
				16, 
				16)) {
			mCountAdded++;
		}		
		//Oxygen
		if (GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(catalyst, 0),
				ItemUtils.getItemStackOfAmountFromOreDict("cellOxygen", 1),
				FluidUtils.getFluidStack("sulfurdioxide", 3000),
				FluidUtils.getFluidStack("sulfurtrioxide", 4000),
				CI.emptyCells(1),
				null,
				16, 
				16)) {
			mCountAdded++;
		}
		if (GT_Values.RA.addChemicalRecipe(
				ItemUtils.getItemStackOfAmountFromOreDict(catalyst, 0),
				ItemUtils.getItemStackOfAmountFromOreDict("cellSulfurDioxide", 3),
				FluidUtils.getFluidStack("oxygen", 1000),
				FluidUtils.getFluidStack("sulfurtrioxide", 4000),
				CI.emptyCells(3),
				null,
				16, 
				16)) {
			mCountAdded++;
		}

		/**
		 * Sulfuric Acid Recipes
		 */

		if (GT_Values.RA.addChemicalRecipe(
				CI.getNumberedCircuit(22),
				ItemUtils.getItemStackOfAmountFromOreDict("cellSulfurTrioxide", 1),
				FluidUtils.getFluidStack("water", 750),
				Materials.SulfuricAcid.getFluid(1750),
				CI.emptyCells(1),
				null,
				20, 
				20)) {
			mCountAdded++;
		}		
		if (GT_Values.RA.addChemicalRecipe(
				CI.getNumberedCircuit(22),
				ItemUtils.getItemStackOfAmountFromOreDict("cellWater", 3),
				FluidUtils.getFluidStack("sulfurtrioxide", 4000),
				Materials.SulfuricAcid.getFluid(7000),
				CI.emptyCells(3),
				null,
				20, 
				20)) {
			mCountAdded++;
		}

		return mCountAdded;
	}


	public int disableSulfurTrioxide() {
		int mDisabled = 0;		
		//Single Block Recipes
		recipe : for (GT_Recipe r : GT_Recipe.GT_Recipe_Map.sChemicalRecipes.mRecipeList) {
			for (ItemStack i : r.mOutputs) {
				i.stackSize = 1;
				if (ItemStack.areItemStacksEqual(i, ItemUtils.getItemStackOfAmountFromOreDict("cellSulfurTrioxide", 1))) {
					r.mEnabled = false;
					r.mHidden = true;
					mDisabled++;
					continue recipe;
				}
				continue;
			}
			for (FluidStack f : r.mFluidOutputs) {
				if (FluidStack.areFluidStackTagsEqual(f, Materials.SulfurTrioxide.getFluid(1))) {
					r.mEnabled = false;
					r.mHidden = true;
					mDisabled++;
					continue recipe;
				}
				continue;
			}
		}

		//Multi Block Recipes
		recipe : for (GT_Recipe r : GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.mRecipeList) {
			for (ItemStack i : r.mOutputs) {
				i.stackSize = 1;
				if (ItemStack.areItemStacksEqual(i, ItemUtils.getItemStackOfAmountFromOreDict("cellSulfurTrioxide", 1))) {
					r.mEnabled = false;
					r.mHidden = true;
					mDisabled++;
					continue recipe;
				}
				continue;
			}
			for (FluidStack f : r.mFluidOutputs) {
				if (FluidStack.areFluidStackTagsEqual(f, Materials.SulfurTrioxide.getFluid(1))) {
					r.mEnabled = false;
					r.mHidden = true;
					mDisabled++;
					continue recipe;
				}
				continue;
			}
		}

		return mDisabled;
	}

	public int disableSulfuricAcid() {
		int mDisabled = 0;		
		//Single Block Recipes
		recipe : for (GT_Recipe r : GT_Recipe.GT_Recipe_Map.sChemicalRecipes.mRecipeList) {
			for (ItemStack i : r.mOutputs) {
				i.stackSize = 1;
				if (ItemStack.areItemStacksEqual(i, ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricAcid", 1))) {
					r.mEnabled = false;
					r.mHidden = true;
					mDisabled++;
					continue recipe;
				}
				continue;
			}
			for (FluidStack f : r.mFluidOutputs) {
				if (FluidStack.areFluidStackTagsEqual(f, Materials.SulfuricAcid.getFluid(1))) {
					r.mEnabled = false;
					r.mHidden = true;
					mDisabled++;
					continue recipe;
				}
				continue;
			}
		}

		//Multi Block Recipes
		recipe : for (GT_Recipe r : GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes.mRecipeList) {
			for (ItemStack i : r.mOutputs) {
				i.stackSize = 1;
				if (ItemStack.areItemStacksEqual(i, ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricAcid", 1))) {
					r.mEnabled = false;
					r.mHidden = true;
					mDisabled++;
					continue recipe;
				}
				continue;
			}
			for (FluidStack f : r.mFluidOutputs) {
				if (FluidStack.areFluidStackTagsEqual(f, Materials.SulfuricAcid.getFluid(1))) {
					r.mEnabled = false;
					r.mHidden = true;
					mDisabled++;
					continue recipe;
				}
				continue;
			}
		}

		return mDisabled;
	}

	@Override
	public String getPluginAbbreviation() {
		return "RSCM";
	}

}
