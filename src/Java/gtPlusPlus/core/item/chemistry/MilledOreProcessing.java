package gtPlusPlus.core.item.chemistry;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.item.base.ore.BaseItemMilledOre;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import gtPlusPlus.xmod.bop.HANDLER_BiomesOPlenty;
import gtPlusPlus.xmod.bop.blocks.BOP_Block_Registrator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class MilledOreProcessing extends ItemPackage {

	/**
	 * Fluids
	 */

	public static Fluid ZincFlotationFroth;
	public static Fluid CopperFlotationFroth;
	public static Fluid NickelFlotationFroth;
	public static Fluid PlatinumFlotationFroth;
	public static Fluid PentlanditeFlotationFroth;

	public static Fluid PineOil;


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

		PineOil = FluidUtils.generateFluidNoPrefix("pineoil", "Pine Oil", 32 + 175, new short[] { 250, 200, 60, 100 }, true);

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
		addPineOilExtraction();
		return true;
	}

	private void addPineOilExtraction() {
		AutoMap<ItemStack> aLogs = new AutoMap<ItemStack>();
		AutoMap<ItemStack> aLeaves = new AutoMap<ItemStack>();
		AutoMap<ItemStack> aSaplings = new AutoMap<ItemStack>();
		AutoMap<ItemStack> aPinecones = new AutoMap<ItemStack>();

		ItemStack aCrushedPine = ItemUtils.getSimpleStack(AgriculturalChem.mCrushedPine, 1);

		aLogs.add(ItemUtils.getSimpleStack(BOP_Block_Registrator.log_Pine));
		aLeaves.add(ItemUtils.getSimpleStack(BOP_Block_Registrator.leaves_Pine));
		aSaplings.add(ItemUtils.getSimpleStack(BOP_Block_Registrator.sapling_Pine));
		aPinecones.add(ItemUtils.getSimpleStack(AgriculturalChem.mPinecone, 1));

		if (LoadedMods.BiomesOPlenty) {
			aLogs.add(HANDLER_BiomesOPlenty.getStack(HANDLER_BiomesOPlenty.logs4, 0, 1));
			aLeaves.add(HANDLER_BiomesOPlenty.getStack(HANDLER_BiomesOPlenty.colorizedLeaves2, 1, 1));
			aSaplings.add(HANDLER_BiomesOPlenty.getStack(HANDLER_BiomesOPlenty.colorizedSaplings, 5, 1));
			aPinecones.add(ItemUtils.simpleMetaStack(HANDLER_BiomesOPlenty.mPineCone, 13, 1));			
		}		
		if (LoadedMods.Forestry) {
			ItemStack aForestryLog = ItemUtils.getItemStackFromFQRN("Forestry:logs", 1);
			if (aForestryLog != null) {
				aForestryLog.setItemDamage(20); // Set to Pine
				aLogs.add(aForestryLog);
			}
			ItemStack aForestryLeaves = ItemUtils.getItemStackFromFQRN("Forestry:leaves", 1);
			if (aForestryLeaves != null) {
				NBTUtils.setString(aForestryLeaves, "species", "forestry.treePine"); // Set to Pine
				aLeaves.add(aForestryLeaves);
			}
		}
		
		for (ItemStack aLog : aLogs) {
			addRecipe(aLog, ItemUtils.getSimpleStack(aCrushedPine, 16), new int[] {10000, 7500, 5000, 2500}, 10, 120);
		}
		for (ItemStack aLeaf : aLeaves) {
			addRecipe(aLeaf, ItemUtils.getSimpleStack(aCrushedPine, 2), new int[] {5000, 5000, 2500, 2500}, 10, 30);
		}
		for (ItemStack aSapling : aSaplings) {
			addRecipe(aSapling, ItemUtils.getSimpleStack(aCrushedPine, 4), new int[] {7500, 7500, 2500, 2500}, 10, 60);
		}
		for (ItemStack aCone : aPinecones) {
			addRecipe(aCone, ItemUtils.getSimpleStack(aCrushedPine, 1), new int[] {7500, 7500, 5000, 2500}, 10, 60);
		}
		

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						CI.getNumberedBioCircuit(16),
						ItemUtils.getSimpleStack(aCrushedPine, 64)
				}, 
				new FluidStack[] {
						FluidUtils.getSteam(5000),
				}, 
				new ItemStack[] {
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5)
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(PineOil, 1000)						
				}, 
				new int[] {
					2000, 2000, 2000, 2000	
				},
				20 *60,
				120, 
				2);
		
		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						CI.getNumberedBioCircuit(18),
						ItemUtils.getSimpleStack(aCrushedPine, 64)
				}, 
				new FluidStack[] {
						FluidUtils.getSuperHeatedSteam(5000),
				}, 
				new ItemStack[] {
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAsh", 5),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5),
						ItemUtils.getItemStackOfAmountFromOreDict("dustTinyDarkAsh", 5)
				}, 
				new FluidStack[] {
						FluidUtils.getFluidStack(PineOil, 2000)						
				}, 
				new int[] {
					3000, 3000, 3000, 3000	
				},
				20 *60,
				120, 
				3);

	}

	public boolean addRecipe(ItemStack aInput, ItemStack aOutput1, int[] aChances, int aTime, int aEU) {
		aOutput1 = GT_OreDictUnificator.get(true, aOutput1);
		ItemStack aOutputs[] = new ItemStack[4];
		for (int i=0;i<aChances.length;i++) {
			aOutputs[i] = aOutput1;
		}
		aOutputs = cleanArray(aOutputs);
		if ((GT_Utility.isStackInvalid(aInput)) || (GT_Utility.isStackInvalid(aOutput1) || (GT_Utility.getContainerItem(aInput, false) != null))) {
			return false;
		}
		
		return CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						CI.getNumberedBioCircuit(14),
						aInput
				}, 
				new FluidStack[] {
						
				}, 
				aOutputs, 
				new FluidStack[] {
						
				}, 
				aChances,
				aTime * 20,
				aEU, 
				1);
	}

	public static ItemStack[] cleanArray(ItemStack[] input) {
		int aArraySize = input.length;
		AutoMap<ItemStack> aCleanedItems = new AutoMap<ItemStack>();		
		for (ItemStack checkStack : input) {
			if (ItemUtils.checkForInvalidItems(checkStack)) {
				aCleanedItems.put(checkStack);
			}
		}		
		ItemStack[] aOutput = new ItemStack[aCleanedItems.size()];
		for (int i=0;i<aArraySize;i++) {
			ItemStack aMappedStack = aCleanedItems.get(i);
			if (aMappedStack != null){
				aOutput[i] = aMappedStack;
			}
		}		
		return aOutput;
	}
}
