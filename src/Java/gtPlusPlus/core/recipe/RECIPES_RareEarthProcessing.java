package gtPlusPlus.core.recipe;

import static gtPlusPlus.core.material.MISC_MATERIALS.BRINE;
import static gtPlusPlus.core.material.MISC_MATERIALS.HYDROGEN_CHLORIDE;
import static gtPlusPlus.core.material.MISC_MATERIALS.RARE_EARTH_HIGH;
import static gtPlusPlus.core.material.MISC_MATERIALS.RARE_EARTH_LOW;
import static gtPlusPlus.core.material.MISC_MATERIALS.RARE_EARTH_MID;
import static gtPlusPlus.core.material.MISC_MATERIALS.SALT_WATER;
import static gtPlusPlus.core.material.MISC_MATERIALS.SODIUM_CHLORIDE;
import static gtPlusPlus.core.material.MISC_MATERIALS.SODIUM_HYDROXIDE;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.ORES;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class RECIPES_RareEarthProcessing {

	private static ItemStack mDustSodiumHydroxide;
	private static ItemStack mDustSalt;
	private static FluidStack mSaltWater;
	private static FluidStack mBrine;
	private static FluidStack mHydrogenChloride;

	public static void init() {

		// Salt Check and Assignment
		mDustSalt = ItemUtils.getItemStackOfAmountFromOreDict("dustSalt", 1);
		if (mDustSalt == null) {
			MaterialUtils.generateSpecialDustAndAssignToAMaterial(SODIUM_CHLORIDE, false);
			mDustSalt = SODIUM_CHLORIDE.getDust(1);
		}
		else {
			SODIUM_CHLORIDE.registerComponentForMaterial(OrePrefixes.dust, mDustSalt);
		}    	

		// Salt water Check and Assignment
		mSaltWater = FluidUtils.getFluidStack("saltwater", 1000);
		if (mSaltWater == null) {
			Fluid f = SALT_WATER.generateFluid();
			SALT_WATER.registerComponentForMaterial(FluidUtils.getFluidStack(f, 1000));
			mSaltWater = SALT_WATER.getFluid(1000);
		}
		else {
			SALT_WATER.registerComponentForMaterial(FluidUtils.getFluidStack(mSaltWater, 1000));    		
		}

		// Brine Check and assignment
		mBrine = FluidUtils.getFluidStack("brine", 1000);
		if (mBrine == null) {
			Fluid f = BRINE.generateFluid();
			BRINE.registerComponentForMaterial(FluidUtils.getFluidStack(f, 1000));
			mBrine = BRINE.getFluid(1000);
		}
		else {
			BRINE.registerComponentForMaterial(FluidUtils.getFluidStack(mBrine, 1000));    		
		}    	

		// Check Sodium Hydroxide Exists, generate if not.
		mDustSodiumHydroxide = ItemUtils.getItemStackOfAmountFromOreDict("dustSodiumHydroxide", 1);
		if (mDustSodiumHydroxide == null) {
			mDustSodiumHydroxide = ItemUtils.getItemStackOfAmountFromOreDict("dustSodiumHydroxide_GT5U", 1);
			if (mDustSodiumHydroxide == null) {
				MaterialUtils.generateSpecialDustAndAssignToAMaterial(SODIUM_HYDROXIDE, false);
				mDustSodiumHydroxide = SODIUM_HYDROXIDE.getDust(1);
			}
			else {
				SODIUM_HYDROXIDE.registerComponentForMaterial(OrePrefixes.dust, mDustSodiumHydroxide);
			}
		}
		else {
			SODIUM_HYDROXIDE.registerComponentForMaterial(OrePrefixes.dust, mDustSodiumHydroxide);
		}

		// Hydrogen Chloride Check and assignment
		mHydrogenChloride = FluidUtils.getFluidStack("hydrogenchloride", 1000);
		if (mHydrogenChloride == null) {
			HYDROGEN_CHLORIDE.generateFluid();
			mHydrogenChloride = BRINE.getFluid(1000);
		}
		else {
			HYDROGEN_CHLORIDE.registerComponentForMaterial(FluidUtils.getFluidStack(mHydrogenChloride, 1000));    		
		} 


		// Add Process for creating Brine
		CORE.RA.addBrewingRecipe(
				ItemUtils.getSimpleStack(mDustSalt, 16),
				MISC_MATERIALS.SALT_WATER.getFluid(2000),
				FluidUtils.getFluidStack(mBrine, 4000),
				20 * 20,
				120,
				false);

		// Chloralkali process
		GT_Values.RA.addElectrolyzerRecipe(
				CI.getNumberedCircuit(16),
				CI.emptyCells(4),
				FluidUtils.getFluidStack(mBrine, 4000),
				null, // Out
				ItemUtils.getItemStackOfAmountFromOreDict("cellChlorine", 2),
				ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogen", 2),
				ItemUtils.getSimpleStack(mDustSodiumHydroxide, 2),
				null,
				null,
				null,
				new int[] {10000, 10000, 10000},
				20 * 30,
				(int) GT_Values.V[2]);

		// Generate Special Laser Recipe
		CORE.RA.addUvLaserRecipe(
				ELEMENT.getInstance().CHLORINE.getCell(2),
				ELEMENT.getInstance().HYDROGEN.getCell(2),
				ItemUtils.getItemStackOfAmountFromOreDict("cellHydrogenChloride", 4),
				20 * 30,
				480);


		// Set Material Tiers correctly 
		ORES.GREENOCKITE.vTier = 1;   	
		RARE_EARTH_LOW.vTier = 1;
		RARE_EARTH_MID.vTier = 3;
		RARE_EARTH_HIGH.vTier = 5;

		// Set Material Voltages correctly    	
		ORES.GREENOCKITE.vVoltageMultiplier = 30;   	
		RARE_EARTH_LOW.vVoltageMultiplier = 30;
		RARE_EARTH_MID.vVoltageMultiplier = 480;
		RARE_EARTH_HIGH.vVoltageMultiplier = 7680;   

		// Set Material Tooltips to be shorter
		RARE_EARTH_LOW.vChemicalFormula = "??????";
		RARE_EARTH_MID.vChemicalFormula = "??????";
		RARE_EARTH_HIGH.vChemicalFormula = "??????";

		// Set Material Tooltips to be shorter
		RARE_EARTH_LOW.vChemicalSymbol = "??";
		RARE_EARTH_MID.vChemicalSymbol = "??";
		RARE_EARTH_HIGH.vChemicalSymbol = "??";

		// Generate Ore Materials
		MaterialGenerator.generateOreMaterial(RARE_EARTH_LOW);
		MaterialGenerator.generateOreMaterial(RARE_EARTH_MID);
		MaterialGenerator.generateOreMaterial(RARE_EARTH_HIGH);

		ItemStack aRareEarth = GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RareEarth, 1L);

		Fluid aSulfuric = FluidUtils.getFluidStack("sulfuricacid", 1).getFluid();
		Fluid aHydrocholric = FluidUtils.getFluidStack("hydrogenchloride", 1).getFluid();
		Fluid aNitric = FluidUtils.getFluidStack("hydrofluoricacid", 1).getFluid();



		// LV Rare Earth
		GT_Values.RA.addChemicalBathRecipe(
				ItemUtils.getSimpleStack(aRareEarth, 3),
				FluidUtils.getFluidStack(aSulfuric, 1000),
				RARE_EARTH_LOW.getCrushed(1),
				RARE_EARTH_LOW.getCrushed(1),
				RARE_EARTH_LOW.getCrushed(1),
				new int[] {10000, 10000, 9000}, 
				20 * 30, 
				(int) GT_Values.V[1]);

		// HV Rare Earth
		GT_Values.RA.addChemicalBathRecipe(
				ItemUtils.getSimpleStack(aRareEarth, 6),
				FluidUtils.getFluidStack(aHydrocholric, 3000),
				RARE_EARTH_MID.getCrushed(2),
				RARE_EARTH_MID.getCrushed(2),
				RARE_EARTH_MID.getCrushed(2),
				new int[] {10000, 9000, 8000}, 
				20 * 60, 
				(int) GT_Values.V[3]);

		// IV Rare Earth
		GT_Values.RA.addChemicalBathRecipe(
				ItemUtils.getSimpleStack(aRareEarth, 9),
				FluidUtils.getFluidStack(aNitric, 6000),
				RARE_EARTH_HIGH.getCrushed(3),
				RARE_EARTH_HIGH.getCrushed(3),
				RARE_EARTH_HIGH.getCrushed(3),
				new int[] {9000, 8000, 7000}, 
				20 * 90, 
				(int) GT_Values.V[5]);


	}


	public static void processCopperRecipes() {

		// Rare Earth Processing
		/*GT_Values.RA.addSifterRecipe(ItemUtils.getItemStackOfAmountFromOreDict("dustRareEarth", 1),
				new ItemStack[] { 
						ELEMENT.getInstance().YTTRIUM.getSmallDust(1),
						ELEMENT.getInstance().NEODYMIUM.getSmallDust(1),
						ELEMENT.getInstance().LANTHANUM.getSmallDust(1),
						ELEMENT.getInstance().CERIUM.getSmallDust(1),
						ELEMENT.getInstance().CADMIUM.getSmallDust(1),
						ELEMENT.getInstance().CAESIUM.getSmallDust(1),
						ORES.SAMARSKITE_YB.getSmallDust(1),
						ORES.FLORENCITE.getSmallDust(1),
						ORES.FLUORCAPHITE.getSmallDust(1),
						//ELEMENT.getInstance().YTTERBIUM.getTinyDust(1),
						//ELEMENT.getInstance().SAMARIUM.getTinyDust(1),
						//ELEMENT.getInstance().GADOLINIUM.getTinyDust(1)
		},
				new int[] { 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000, 2000 }, 20 * 30, 500);*/

	}

}
