package gtPlusPlus.plugin.agrichem;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_ModHandler.RecipeBits;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.plugin.agrichem.block.AgrichemFluids;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.railcraft.utils.RailcraftUtils;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

public class BioRecipes {

	private static Item mFert;
	private static Item mDustDirt;

	private static Fluid mSalineWater;
	private static Fluid mDistilledWater;
	private static Fluid mThermalWater;
	private static Fluid mAir;
	private static Fluid mSulfuricWasteWater;
	private static Fluid mAmmonia;
	private static Fluid mMethanol;
	private static Fluid mAceticAcid;
	private static Fluid mPropionicAcid;
	private static Fluid mLiquidPlastic;
	private static Fluid mFermentationBase;
	private static Fluid mCarbonDioxide;
	private static Fluid mCarbonMonoxide;
	private static Fluid mEthylene;
	private static Fluid mEthanol;
	private static Fluid mChlorine;
	private static Fluid mHydrogen;
	private static Fluid mDilutedSulfuricAcid;
	private static Fluid mSulfuricAcid;
	private static Fluid mUrea;
	private static Fluid mFormaldehyde;
	private static Fluid mLiquidResin;
	private static Fluid mMethane;
	private static Fluid mBenzene;
	private static Fluid mEthylbenzene;
	private static Fluid mStyrene;


	private static final ItemStack getGreenAlgaeRecipeChip() {
		return getBioChip(4);
	}
	private static final ItemStack getBrownAlgaeRecipeChip() {
		return getBioChip(8);
	}
	private static final ItemStack getGoldenBrownAlgaeRecipeChip() {
		return getBioChip(12);
	}
	private static final ItemStack getRedAlgaeRecipeChip() {
		return getBioChip(16);
	}	

	private static final ItemStack getBioChip(int aID) {
		return ItemUtils.simpleMetaStack(Core_Agrichem.mBioCircuit, aID, 0);
	}

	public static void init() {
		Core_Agrichem.mInstance.log("Setting Variables");
		initRecipeVars();
		Core_Agrichem.mInstance.log("Generating Biochip Recipes");
		recipeBioChip();
		Core_Agrichem.mInstance.log("Generating Recipes");
		recipeAlgaeBiomass();
		Core_Agrichem.mInstance.log("Finished with recipes");
	}

	private static final void initRecipeVars() {
		mFert = AgriculturalChem.dustOrganicFertilizer;
		mDustDirt = AgriculturalChem.dustDirt;


		mDistilledWater = FluidUtils.getDistilledWater(1).getFluid();
		mSalineWater = FluidUtils.getFluidStack("saltwater", 1).getFluid();
		mThermalWater = FluidUtils.getFluidStack("ic2hotwater", 1).getFluid();		
		mAir = FluidUtils.getFluidStack("air", 1).getFluid();		
		mSulfuricWasteWater = FluidUtils.getFluidStack("sulfuricapatite", 1).getFluid();
		mAmmonia = MISC_MATERIALS.AMMONIA.getFluid(1).getFluid();
		mEthylene = FluidUtils.getFluidStack("ethylene", 1).getFluid();
		mEthanol = FluidUtils.getFluidStack("bioethanol", 1).getFluid();
		mDilutedSulfuricAcid = FluidUtils.getFluidStack("dilutedsulfuricacid", 1).getFluid();
		mSulfuricAcid = FluidUtils.getFluidStack("sulfuricacid", 1).getFluid();		 
		mFormaldehyde = FluidUtils.getFluidStack("fluid.formaldehyde", 1).getFluid();		 
		mMethane = FluidUtils.getFluidStack("methane", 1).getFluid();
		mBenzene = FluidUtils.getFluidStack("benzene", 1).getFluid();
		mEthylbenzene = FluidUtils.getFluidStack("fluid.ethylbenzene", 1).getFluid();
		mStyrene = FluidUtils.getFluidStack("styrene", 1).getFluid();		
		mMethanol = FluidUtils.getFluidStack("methanol", 1).getFluid();
		mLiquidPlastic = FluidUtils.getFluidStack("plastic", 1).getFluid();
		mCarbonDioxide = MISC_MATERIALS.CARBON_DIOXIDE.getFluid(1).getFluid();
		mCarbonMonoxide = MISC_MATERIALS.CARBON_MONOXIDE.getFluid(1).getFluid();
		mChlorine = FluidUtils.getFluidStack("chlorine", 1).getFluid();
		mHydrogen = FluidUtils.getFluidStack("hydrogen", 1).getFluid();		
		mAceticAcid = AgrichemFluids.mAceticAcid;
		mPropionicAcid = AgrichemFluids.mPropionicAcid;
		mUrea = AgrichemFluids.mUrea;
		mLiquidResin = AgrichemFluids.mLiquidResin;
		mFermentationBase = AgrichemFluids.mFermentationBase;



	}

	private static void recipeAlgaeBiomass() {

		// TODO
		// Add in recipes to get initial Biomass

		recipeGreenAlgae();
		recipeBrownAlgae();
		recipeGoldenBrownAlgae();
		recipeRedAlgae();
		recipeWoodPellets();
		recipeWoodBricks();
		recipeCellulosePulp();
		recipeCatalystCarrier();
		recipeAluminiumSilverCatalyst();
		recipeAceticAcid();
		recipePropionicAcid();
		recipeFermentationBase();
		recipeEthanol();
		recipeCelluloseFibre();
		recipeGoldenBrownCelluloseFiber();
		recipeRedCelluloseFiber();		
		recipeSodiumHydroxide();
		recipeSodiumCarbonate();		
		recipeAluminiumPellet();
		recipeAlumina();
		recipeAluminium();
		recipeCalciumCarbonate();
		recipeLithiumChloride();
		recipeAlginicAcid();
		recipeSulfuricAcid();
		recipeUrea();		
		recipeRawBioResin();
		recipeLiquidResin();
		recipeCompost();
		recipeMethane();
		recipeBenzene();
		recipeStyrene();
	}

	private static void recipeGreenAlgae() {
		// Compost
		GT_ModHandler.addPulverisationRecipe(
				ItemUtils.getSimpleStack(Core_Agrichem.mGreenAlgaeBiosmass, 10),
				ItemUtils.getSimpleStack(Core_Agrichem.mCompost, 1));

		// Turn into Cellulose
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getGreenAlgaeRecipeChip(),
				ItemUtils.getSimpleStack(Core_Agrichem.mGreenAlgaeBiosmass, 30)
		},
				GT_Values.NF,
				ItemUtils.getSimpleStack(Core_Agrichem.mCelluloseFiber, 5),
				20 * 30,
				16);


	}

	private static void recipeBrownAlgae() {
		// Compost
		GT_ModHandler.addPulverisationRecipe(
				ItemUtils.getSimpleStack(Core_Agrichem.mBrownAlgaeBiosmass, 10),
				ItemUtils.getSimpleStack(Core_Agrichem.mCompost, 1));

		// Alginic acid
		GT_Values.RA.addExtractorRecipe(
				ItemUtils.getSimpleStack(Core_Agrichem.mBrownAlgaeBiosmass, 10),
				ItemUtils.getSimpleStack(Core_Agrichem.mAlginicAcid, 1),
				20 * 15,
				30);

		// Lithium Chloride
		GT_Values.RA.addBlastRecipe(
				getBrownAlgaeRecipeChip(),
				ItemUtils.getSimpleStack(Core_Agrichem.mBrownAlgaeBiosmass, 20),
				GT_Values.NF,
				GT_Values.NF,
				ItemUtils.getSimpleStack(Core_Agrichem.mLithiumChloride, 1),
				GT_Values.NI,
				120, 
				120,
				1200);

		// Sodium Carbonate
		CORE.RA.addChemicalRecipe(
				getBrownAlgaeRecipeChip(),
				ItemUtils.getSimpleStack(Core_Agrichem.mBrownAlgaeBiosmass, 20),
				FluidUtils.getDistilledWater(2000), 
				GT_Values.NF, 
				ItemUtils.getSimpleStack(Core_Agrichem.mSodiumCarbonate, 1),
				20 * 30,
				30);		

	}

	private static void recipeGoldenBrownAlgae() {
		// Compost
		GT_ModHandler.addPulverisationRecipe(
				ItemUtils.getSimpleStack(Core_Agrichem.mGoldenBrownAlgaeBiosmass, 10),
				ItemUtils.getSimpleStack(Core_Agrichem.mCompost, 1));

		// Turn into Cellulose
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getGoldenBrownAlgaeRecipeChip(),
				ItemUtils.getSimpleStack(Core_Agrichem.mGoldenBrownAlgaeBiosmass, 30)
		},
				GT_Values.NF,
				ItemUtils.getSimpleStack(Core_Agrichem.mGoldenBrownCelluloseFiber, 5),
				20 * 30,
				64);

	}

	private static void recipeRedAlgae() {
		// Compost
		GT_ModHandler.addPulverisationRecipe(
				ItemUtils.getSimpleStack(Core_Agrichem.mRedAlgaeBiosmass, 10),
				ItemUtils.getSimpleStack(Core_Agrichem.mCompost, 1));

		// Turn into Cellulose
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getRedAlgaeRecipeChip(),
				ItemUtils.getSimpleStack(Core_Agrichem.mRedAlgaeBiosmass, 30)
		},
				GT_Values.NF,
				ItemUtils.getSimpleStack(Core_Agrichem.mRedCelluloseFiber, 5),
				20 * 30,
				256);

	}

	private static void recipeCelluloseFibre() {

		CORE.RA.addChemicalRecipe(
				ItemUtils.getSimpleStack(Core_Agrichem.mCelluloseFiber, 20),
				ItemUtils.getSimpleStack(Core_Agrichem.mAlginicAcid, 2),
				GT_Values.NF, 
				GT_Values.NF,
				ItemUtils.getSimpleStack(Core_Agrichem.mCellulosePulp, 10),
				30 * 20,
				16);

		// Craft into Wood Pellets
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getBioChip(2),
				ItemUtils.getSimpleStack(Core_Agrichem.mCelluloseFiber, 12)
		},
				GT_Values.NF,
				ItemUtils.getSimpleStack(Core_Agrichem.mWoodPellet, 2),
				20 * 30,
				30);

		// Methanol Extraction
		GT_Values.RA.addFluidExtractionRecipe(
				ItemUtils.getSimpleStack(Core_Agrichem.mCelluloseFiber, 12),
				GT_Values.NI,
				FluidUtils.getFluidStack(mMethanol, 50),
				10000,
				20 * 30,
				30);

		// Compost
		GT_ModHandler.addPulverisationRecipe(
				ItemUtils.getSimpleStack(Core_Agrichem.mCelluloseFiber, 5),
				ItemUtils.getSimpleStack(Core_Agrichem.mCompost, 1));


	}

	private static void recipeWoodPellets() {
		// Shapeless Recipe
		RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] {
				ItemUtils.getSimpleStack(Core_Agrichem.mWoodPellet, 1),
				ItemUtils.getSimpleStack(Core_Agrichem.mWoodPellet, 1),
				ItemUtils.getSimpleStack(Core_Agrichem.mWoodPellet, 1),
				ItemUtils.getSimpleStack(Core_Agrichem.mWoodPellet, 1),
				ItemUtils.getSimpleStack(Core_Agrichem.mWoodPellet, 1),
				ItemUtils.getSimpleStack(Core_Agrichem.mWoodPellet, 1),
				ItemUtils.getSimpleStack(Core_Agrichem.mWoodPellet, 1),
				ItemUtils.getSimpleStack(Core_Agrichem.mWoodPellet, 1)
		}, ItemUtils.getSimpleStack(Core_Agrichem.mWoodBrick, 2));

		// Assembly Recipe
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getBioChip(2),
				ItemUtils.getSimpleStack(Core_Agrichem.mWoodPellet, 8)
		}, 
				GT_Values.NF, 
				ItemUtils.getSimpleStack(Core_Agrichem.mWoodBrick, 2),
				20, 
				8);

		// CO2
		CORE.RA.addFluidExtractionRecipe(
				GT_Values.NI,
				ItemUtils.getSimpleStack(Core_Agrichem.mWoodPellet, 1),
				FluidUtils.getFluidStack(mCarbonDioxide, 70),
				10*20,
				30);


		// Add Charcoal Recipe
		if (LoadedMods.Railcraft) {
			RailcraftUtils.addCokeOvenRecipe(
					ItemUtils.getSimpleStack(Core_Agrichem.mWoodPellet, 2),
					true,
					true,
					ItemUtils.getItemStackOfAmountFromOreDict("gemCharcoal", 3),
					GT_Values.NF, 
					1200);			
		}
		CORE.RA.addCokeOvenRecipe(
				ItemUtils.getSimpleStack(Core_Agrichem.mWoodPellet, 1),
				getBioChip(3),
				null,
				GT_Values.NF, 
				ItemUtils.getItemStackOfAmountFromOreDict("gemCharcoal", 3),
				120, 
				16);			


	}

	private static void recipeWoodBricks() {

		// Assembly Recipe
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getBioChip(3),
				ItemUtils.getOrePrefixStack(OrePrefixes.dust, Materials.Wood, 50)
		}, 
				GT_Values.NF, 
				ItemUtils.getSimpleStack(Core_Agrichem.mWoodBrick, 1),
				100, 
				16);
	}

	private static void recipeCellulosePulp() {

		// Assembly Recipe
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getBioChip(2),
				ItemUtils.getSimpleStack(Core_Agrichem.mCellulosePulp, 4)
		}, 
				GT_Values.NF, 
				ItemUtils.getSimpleStack(Items.paper, 1),
				50, 
				16);
	}

	private static void recipeCatalystCarrier() {

	}	

	private static void recipeAluminiumSilverCatalyst() {

	}

	private static void recipeAceticAcid() {

	}

	private static void recipeFermentationBase() {

	}

	private static void recipePropionicAcid() {

	}

	private static void recipeEthanol() {

	}

	private static void recipeGoldenBrownCelluloseFiber() {

	}

	private static void recipeRedCelluloseFiber() {

	}

	private static void recipeSodiumHydroxide() {

	}

	private static void recipeSodiumCarbonate() {

	}

	private static void recipeAluminiumPellet() {

	}

	private static void recipeAlumina() {

	}

	private static void recipeAluminium() {

	}

	private static void recipeCalciumCarbonate() {

	}

	private static void recipeLithiumChloride() {

	}

	private static void recipeAlginicAcid() {

	}

	private static void recipeSulfuricAcid() {

	}

	private static void recipeUrea() {

	}

	private static void recipeRawBioResin() {

	}

	private static void recipeLiquidResin() {

	}

	private static void recipeCompost() {

	}

	private static void recipeMethane() {

	}

	private static void recipeBenzene() {

	}

	private static void recipeStyrene() {

	}

	private static void recipeBioChip() {

		GT_ModHandler.addShapelessCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 0L, new Object[0]),
				RecipeBits.NOT_REMOVABLE, new Object[]{OrePrefixes.circuit.get(Materials.Primitive)});
		
		
		long bits = RecipeBits.BUFFERED | RecipeBits.NOT_REMOVABLE;
		
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 1L, new Object[0]), bits,
				new Object[]{"d  ", " P ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 2L, new Object[0]), bits,
				new Object[]{" d ", " P ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 3L, new Object[0]), bits,
				new Object[]{"  d", " P ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 4L, new Object[0]), bits,
				new Object[]{"   ", " Pd", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 5L, new Object[0]), bits,
				new Object[]{"   ", " P ", "  d", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 6L, new Object[0]), bits,
				new Object[]{"   ", " P ", " d ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 7L, new Object[0]), bits,
				new Object[]{"   ", " P ", "d  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 8L, new Object[0]), bits,
				new Object[]{"   ", "dP ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 9L, new Object[0]), bits,
				new Object[]{"P d", "   ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 10L, new Object[0]), bits,
				new Object[]{"P  ", "  d", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 11L, new Object[0]), bits,
				new Object[]{"P  ", "   ", "  d", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 12L, new Object[0]), bits,
				new Object[]{"P  ", "   ", " d ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 13L, new Object[0]), bits,
				new Object[]{"  P", "   ", "  d", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 14L, new Object[0]), bits,
				new Object[]{"  P", "   ", " d ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 15L, new Object[0]), bits,
				new Object[]{"  P", "   ", "d  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 16L, new Object[0]), bits,
				new Object[]{"  P", "d  ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 17L, new Object[0]), bits,
				new Object[]{"   ", "   ", "d P", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 18L, new Object[0]), bits,
				new Object[]{"   ", "d  ", "  P", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 19L, new Object[0]), bits,
				new Object[]{"d  ", "   ", "  P", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 20L, new Object[0]), bits,
				new Object[]{" d ", "   ", "  P", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 21L, new Object[0]), bits,
				new Object[]{"d  ", "   ", "P  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 22L, new Object[0]), bits,
				new Object[]{" d ", "   ", "P  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 23L, new Object[0]), bits,
				new Object[]{"  d", "   ", "P  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		GT_ModHandler.addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 24L, new Object[0]), bits,
				new Object[]{"   ", "  d", "P  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
	}


}
