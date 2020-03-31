package gtPlusPlus.core.item.chemistry;

import gregtech.api.enums.Materials;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.item.base.ore.BaseItemMilledOre;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;

public class MilledOreProcessing extends ItemPackage {

	/**
	 * Fluids
	 */
	
	public static Fluid ZincFlotationFroth;
	public static Fluid CopperFlotationFroth;
	public static Fluid NickelFlotationFroth;
	public static Fluid PlatinumFlotationFroth;
	public static Fluid PentlanditeFlotationFroth;
	
	
	/**
	 * Items
	 */

	// Zinc, Iron, Indium, Germanium
	public static Item milledSphalerite;
	
	// Copper, Iron, Cadmium, Indium
	public static Item milledChalcopyrite;
	
	// Nickel, Cobalt, Rhodium, Ruthenium
	public static Item milledNickel;
	
	// Platinum, Rhodium, Selenium, Tellurium 
	public static Item milledPlatinum;

	// Iron, Nickel, Promethium, Hafnium 
	public static Item milledPentlandite;
	

	@Override
	public void items() {

		milledSphalerite = BaseItemMilledOre.generate(Materials.Sphalerite, MaterialUtils.getVoltageForTier(5));
		milledChalcopyrite = BaseItemMilledOre.generate(Materials.Chalcopyrite, MaterialUtils.getVoltageForTier(4));
		milledNickel = BaseItemMilledOre.generate(Materials.Nickel, MaterialUtils.getVoltageForTier(4));
		milledPlatinum = BaseItemMilledOre.generate(Materials.Platinum, MaterialUtils.getVoltageForTier(5));
		milledPentlandite = BaseItemMilledOre.generate(Materials.Pentlandite, MaterialUtils.getVoltageForTier(5));
		
	}

	@Override
	public void blocks() {
		// None yet
	}

	@Override
	public void fluids() {
		
		short[] aZincFrothRGB = Materials.Sphalerite.mRGBa;
		ZincFlotationFroth = FluidUtils.generateFluidNoPrefix("froth.zincflotation", "Zinc Froth", 32 + 175, new short[] { aZincFrothRGB[0], aZincFrothRGB[1], aZincFrothRGB[2], 100 }, true);
		short[] aCopperFrothRGB = Materials.Chalcopyrite.mRGBa;
		CopperFlotationFroth = FluidUtils.generateFluidNoPrefix("froth.copperflotation", "Copper Froth", 32 + 175, new short[] { aCopperFrothRGB[0], aCopperFrothRGB[1], aCopperFrothRGB[2], 100 }, true);
		short[] aNickelFrothRGB = Materials.Nickel.mRGBa;
		NickelFlotationFroth = FluidUtils.generateFluidNoPrefix("froth.nickelflotation", "Nickel Froth", 32 + 175, new short[] { aNickelFrothRGB[0], aNickelFrothRGB[1], aNickelFrothRGB[2], 100 }, true);
		short[] aPlatinumFrothRGB = Materials.Platinum.mRGBa;
		PlatinumFlotationFroth = FluidUtils.generateFluidNoPrefix("froth.platinumflotation", "Platinum Froth", 32 + 175, new short[] { aPlatinumFrothRGB[0], aPlatinumFrothRGB[1], aPlatinumFrothRGB[2], 100 }, true);
		short[] aPentlanditeFrothRGB = Materials.Pentlandite.mRGBa;
		PentlanditeFlotationFroth = FluidUtils.generateFluidNoPrefix("froth.pentlanditeflotation", "Pentlandite Froth", 32 + 175, new short[] { aPentlanditeFrothRGB[0], aPentlanditeFrothRGB[1], aPentlanditeFrothRGB[2], 100 }, true);

	}
	
	

	public MilledOreProcessing() {
		super();
		Logger.INFO("Adding Ore Milling content");
	}

	private static void addMiscRecipes() {

		/*GT_Values.RA.addCentrifugeRecipe(
				CI.getNumberedBioCircuit(10),
				GT_Values.NI,
				FluidUtils.getFluidStack(MilledOreProcessing.ZincFlotationFroth, 1000),
				FluidUtils.getWater(500),
				ELEMENT.getInstance().IRON.getSmallDust(1),
				ELEMENT.getInstance().COPPER.getSmallDust(1),
				ELEMENT.getInstance().TIN.getSmallDust(1),
				ELEMENT.getInstance().SULFUR.getSmallDust(1),
				ELEMENT.getInstance().NICKEL.getTinyDust(1),
				ELEMENT.getInstance().LEAD.getTinyDust(1),
				new int[] { 3000, 3000, 2000, 2000, 1000, 1000 }, 
				30 * 20, 
				30);*/

	}

	@Override
	public String errorMessage() {
		return "Failed to generate recipes for OreMillingProc.";
	}

	@Override
	public boolean generateRecipes() {
		addMiscRecipes();
		return true;
	}
}
