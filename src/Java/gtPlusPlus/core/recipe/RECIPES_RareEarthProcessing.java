package gtPlusPlus.core.recipe;

import static gtPlusPlus.core.material.MISC_MATERIALS.RARE_EARTH_HIGH;
import static gtPlusPlus.core.material.MISC_MATERIALS.RARE_EARTH_LOW;
import static gtPlusPlus.core.material.MISC_MATERIALS.RARE_EARTH_MID;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.ORES;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class RECIPES_RareEarthProcessing {

    public static void init() { 

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

    	// Generate Ore Materials
		MaterialGenerator.generateOreMaterial(RARE_EARTH_LOW);
		MaterialGenerator.generateOreMaterial(RARE_EARTH_MID);
		MaterialGenerator.generateOreMaterial(RARE_EARTH_HIGH);
		
		ItemStack aRareEarth = GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RareEarth, 1L);
		
		Fluid aSulfuric = FluidUtils.getFluidStack("sulfuricacid", 1).getFluid();
		Fluid aHydrocholric = FluidUtils.getFluidStack("hydrochloricacid_gt5u", 1).getFluid();
		Fluid aNitric = FluidUtils.getFluidStack("nitricacid", 1).getFluid();

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
