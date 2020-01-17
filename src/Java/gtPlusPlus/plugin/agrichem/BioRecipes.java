package gtPlusPlus.plugin.agrichem;

import static gtPlusPlus.core.lib.CORE.GTNH;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.item.chemistry.AgriculturalChem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.minecraft.OreDictUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.plugin.agrichem.block.AgrichemFluids;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.railcraft.utils.RailcraftUtils;
import ic2.core.Ic2Items;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

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
	private static Fluid mButanol;
	private static Fluid mAcetone;


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
		return ItemUtils.simpleMetaStack(AgriculturalChem.mBioCircuit, aID, 0);
	}

	public static void init() {
		Logger.INFO("[Bio] Setting Variables");
		initRecipeVars();
		Logger.INFO("[Bio] Generating Biochip Recipes");
		recipeBioChip();
		Logger.INFO("[Bio] Generating Recipes");
		recipeAlgaeBiomass();
		Logger.INFO("[Bio] Finished with recipes");
	}

	private static final void initRecipeVars() {
		mFert = AgriculturalChem.dustOrganicFertilizer;
		mDustDirt = AgriculturalChem.dustDirt;

		// 5.08 Salt Water Solution ;)
		if (!FluidUtils.doesFluidExist("saltwater")){
			mSalineWater = FluidUtils.generateFluidNoPrefix("saltwater", "Salt Water", 200, new short[]{10, 30, 220, 100});
		}
		else {
			Materials aSaltWater = MaterialUtils.getMaterial("saltwater");
			if (aSaltWater != null) {
				FluidStack aWaterStack = aSaltWater.getFluid(1);
				if (aWaterStack != null) {
					mSalineWater = aSaltWater.getFluid(1).getFluid();					
				}				
			}
			if (mSalineWater == null) {
				mSalineWater = FluidUtils.getWildcardFluidStack("saltwater", 1).getFluid();
			}
			if (ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cellSaltWater", 1) == null){				
				new BaseItemComponent("saltwater", "Salt Water", new short[] {10, 30, 220});
			}
		}

		mDistilledWater = FluidUtils.getDistilledWater(1).getFluid();
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
		mLiquidPlastic = FluidUtils.getWildcardFluidStack("plastic", 1).getFluid();
		mCarbonDioxide = MISC_MATERIALS.CARBON_DIOXIDE.getFluid(1).getFluid();
		mCarbonMonoxide = MISC_MATERIALS.CARBON_MONOXIDE.getFluid(1).getFluid();
		mChlorine = FluidUtils.getFluidStack("chlorine", 1).getFluid();
		mHydrogen = FluidUtils.getFluidStack("hydrogen", 1).getFluid();		
		mAceticAcid = AgrichemFluids.mAceticAcid;
		mPropionicAcid = AgrichemFluids.mPropionicAcid;
		mUrea = AgrichemFluids.mUrea;
		mLiquidResin = AgrichemFluids.mLiquidResin;
		mFermentationBase = AgrichemFluids.mFermentationBase;		
		mButanol = AgrichemFluids.mButanol;
		mAcetone = AgrichemFluids.mAcetone;
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
		recipePelletMold();
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
		registerFuels();
	}

	private static void registerFuels() {

		// Burnables
		ItemUtils.registerFuel(ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1), 800);
		ItemUtils.registerFuel(ItemUtils.getSimpleStack(AgriculturalChem.mWoodBrick, 1), 4800);

		// Gas Fuels
		//GT_Values.RA.addFuel(ItemUtils.getItemStackOfAmountFromOreDict("cellCoalGas", 1), null, 96, 1);

		//Combustion Fuels
		GT_Values.RA.addFuel(ItemUtils.getItemStackOfAmountFromOreDict("cellButanol", 1), null, 1125, 0);

	}
	private static void recipeGreenAlgae() {
		// Compost
		GT_ModHandler.addPulverisationRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 10),
				ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 1));

		// Turn into Cellulose
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getGreenAlgaeRecipeChip(),
				ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 30)
		},
				GT_Values.NF,
				ItemUtils.getSimpleStack(AgriculturalChem.mCelluloseFiber, 5),
				30 * 30,
				16);


	}

	private static void recipeBrownAlgae() {
		// Compost
		GT_ModHandler.addPulverisationRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 10),
				ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 1));

		// Alginic acid
		CORE.RA.addExtractorRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 10),
				ItemUtils.getSimpleStack(AgriculturalChem.mAlginicAcid, 1),
				20 * 15,
				30);

		// Lithium Chloride
		GT_Values.RA.addBlastRecipe(
				getBrownAlgaeRecipeChip(),
				ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 20),
				GT_Values.NF,
				GT_Values.NF,
				ItemUtils.getSimpleStack(AgriculturalChem.mLithiumChloride, 1),
				GT_Values.NI,
				120, 
				120,
				1200);

		// Sodium Carbonate
		CORE.RA.addChemicalRecipe(
				getBrownAlgaeRecipeChip(),
				ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 20),
				FluidUtils.getDistilledWater(2000), 
				GT_Values.NF, 
				ItemUtils.getSimpleStack(AgriculturalChem.mSodiumCarbonate, 1),
				20 * 30,
				30);		

	}

	private static void recipeGoldenBrownAlgae() {
		// Compost
		GT_ModHandler.addPulverisationRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, 10),
				ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 1));

		// Turn into Cellulose
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getGoldenBrownAlgaeRecipeChip(),
				ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownAlgaeBiosmass, 30)
		},
				GT_Values.NF,
				ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownCelluloseFiber, 5),
				30 * 30,
				120);

	}

	private static void recipeRedAlgae() {
		// Compost
		GT_ModHandler.addPulverisationRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, 10),
				ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 1));

		// Turn into Cellulose
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getRedAlgaeRecipeChip(),
				ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, 30)
		},
				GT_Values.NF,
				ItemUtils.getSimpleStack(AgriculturalChem.mRedCelluloseFiber, 5),
				30 * 30,
				240);

	}

	private static void recipeCelluloseFibre() {

		CORE.RA.addChemicalRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mCelluloseFiber, 20),
				ItemUtils.getSimpleStack(AgriculturalChem.mAlginicAcid, 2),
				GT_Values.NF, 
				GT_Values.NF,
				ItemUtils.getSimpleStack(AgriculturalChem.mCellulosePulp, 10),
				45 * 20,
				16);

		// Craft into Wood Pellets
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getBioChip(2),
				ItemUtils.getSimpleStack(AgriculturalChem.mCelluloseFiber, 12)
		},
				GT_Values.NF,
				ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 2),
				20 * 4,
				8);

		// Methanol Extraction
		GT_Values.RA.addFluidExtractionRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mCelluloseFiber, 12),
				GT_Values.NI,
				FluidUtils.getFluidStack(mMethanol, 50),
				10000,
				20 * 30,
				30);

		// Compost
		GT_ModHandler.addPulverisationRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mCelluloseFiber, 5),
				ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 1));

		// Plastic		
		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(16),
						ItemUtils.getSimpleStack(AgriculturalChem.mCellulosePulp, 64),
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mAceticAcid, 500),
						FluidUtils.getFluidStack(BioRecipes.mPropionicAcid, 500),
				},
				new ItemStack[] {

				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mLiquidPlastic, (144)),						
				},
				120 * 20,
				240,
				2);


	}

	private static void recipeWoodPellets() {
		// Shapeless Recipe
		RecipeUtils.addShapelessGregtechRecipe(new ItemStack[] {
				ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1),
				ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1),
				ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1),
				ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1),
				ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1),
				ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1),
				ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1),
				ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1)
		}, ItemUtils.getSimpleStack(AgriculturalChem.mWoodBrick, 2));

		// Extruder Recipe
		GT_Values.RA.addExtruderRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mCelluloseFiber, 12),
				ItemUtils.getSimpleStack(AgriculturalChem.mPelletMold, 0),
				ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 3),
				20 * 10, 
				16);

		// Assembly Recipe
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getBioChip(2),
				ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 8)
		}, 
				GT_Values.NF, 
				ItemUtils.getSimpleStack(AgriculturalChem.mWoodBrick, 2),
				20 * 5, 
				8);

		// CO2
		CORE.RA.addFluidExtractionRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 1),
				FluidUtils.getFluidStack(mCarbonDioxide, 70),
				10*20,
				30);


		// Add Charcoal Recipe
		if (LoadedMods.Railcraft) {
			RailcraftUtils.addCokeOvenRecipe(
					ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 2),
					true,
					true,
					ItemUtils.getItemStackOfAmountFromOreDict("gemCharcoal", 3),
					GT_Values.NF, 
					1200);			
		}
		CORE.RA.addCokeOvenRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mWoodPellet, 2),
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
				ItemUtils.getSimpleStack(AgriculturalChem.mWoodBrick, 1),
				100 * 20, 
				16);
	}

	private static void recipeCellulosePulp() {

		// Assembly Recipe
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getBioChip(2),
				ItemUtils.getSimpleStack(AgriculturalChem.mCellulosePulp, 4)
		}, 
				GT_Values.NF, 
				ItemUtils.getSimpleStack(Items.paper, 1),
				60 * 20, 
				16);
	}

	private static void recipeCatalystCarrier() {
		// Assembly Recipe
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getBioChip(20),
				ItemUtils.getItemStackOfAmountFromOreDict("plateSteel", GTNH ? 8 : 4),
				ItemUtils.getItemStackOfAmountFromOreDict("wireFineCopper", GTNH ? 4 : 2),
				ItemUtils.getItemStackOfAmountFromOreDict("screwTin", GTNH ? 6 : 3)
		}, 
				GT_Values.NF, 
				CI.getEmptyCatalyst(1),
				300 * 20, 
				16);
	}	

	private static void recipeAluminiumSilverCatalyst() {
		// Assembly Recipe
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getBioChip(4),
				CI.getEmptyCatalyst(10),
				ItemUtils.getItemStackOfAmountFromOreDict("dustAluminium", 4),
				ItemUtils.getItemStackOfAmountFromOreDict("dustSilver", 4)
		}, 
				GT_Values.NF, 
				CI.getGreenCatalyst(10),
				20 * 20, 
				30);
	}

	private static void recipeAceticAcid() {

		/*		GT_Values.RA.addMixerRecipe(
						CI.getGreenCatalyst(10),
						var2,
						var3, var4,
						var5, // Fluid in
						var6, // Fluid out
						var7, // Item Out
						var8, // Time
						var9); // Eu
		 */	

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						CI.getGreenCatalyst(0)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mMethanol, 700),
						FluidUtils.getFluidStack(BioRecipes.mCarbonMonoxide, 300),
						FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 2000),
				},
				new ItemStack[] {
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mAceticAcid, 250),						
				},
				120 * 20,
				60,
				1);

		CORE.RA.addDehydratorRecipe(
				CI.emptyCells(1),
				FluidUtils.getFluidStack(mFermentationBase, 4000),
				new ItemStack[] {
						ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 1),
						ItemUtils.getItemStackOfAmountFromOreDict("cellAceticAcid", 1)						
				},
				60 * 20,
				16);


	}



	public final static HashSet<GT_ItemStack> mFruits = new HashSet<GT_ItemStack>();
	public final static HashSet<GT_ItemStack> mVege = new HashSet<GT_ItemStack>();
	public final static HashSet<GT_ItemStack> mNuts = new HashSet<GT_ItemStack>();
	public final static HashSet<GT_ItemStack> mSeeds = new HashSet<GT_ItemStack>();

	public final static AutoMap<ItemStack> mList_Master_FruitVege = new AutoMap<ItemStack>();
	public final static AutoMap<ItemStack> mList_Master_Seeds = new AutoMap<ItemStack>();

	private static void processFermentationOreDict() {
		processOreDictEntry("listAllfruit", mFruits);		
		processOreDictEntry("listAllFruit", mFruits);
		processOreDictEntry("listAllveggie", mVege);
		processOreDictEntry("listAllVeggie", mVege);
		processOreDictEntry("listAllnut", mNuts);
		processOreDictEntry("listAllNut", mNuts);
		processOreDictEntry("listAllseed", mSeeds);
		processOreDictEntry("listAllSeed", mSeeds);

		if (!mFruits.isEmpty()) {
			for (GT_ItemStack g : mFruits) {
				mList_Master_FruitVege.put(g.toStack());
			}
		}
		if (!mVege.isEmpty()) {
			for (GT_ItemStack g : mVege) {
				mList_Master_FruitVege.put(g.toStack());
			}
		}
		if (!mNuts.isEmpty()) {
			for (GT_ItemStack g : mNuts) {
				mList_Master_FruitVege.put(g.toStack());
			}
		}
		if (!mSeeds.isEmpty()) {
			for (GT_ItemStack g : mSeeds) {
				mList_Master_Seeds.put(g.toStack());
			}
		}
	}

	// Make Fermentation
	private static void processOreDictEntry(String aOreName, HashSet<GT_ItemStack> mfruits2) {
		ArrayList<ItemStack> aTemp = OreDictionary.getOres(aOreName);
		if (!aTemp.isEmpty()) {
			for (ItemStack stack : aTemp) {
				mfruits2.add(new GT_ItemStack(stack));
			}
		}
	}


	private static void recipeFermentationBase() {
		processFermentationOreDict();
		AutoMap<ItemStack> aFruitVege = mList_Master_FruitVege;
		AutoMap<ItemStack> aSeeds = mList_Master_Seeds;
		ArrayList<ItemStack> aMap = OreDictionary.getOres("cropSugarbeet");		
		for (ItemStack a : aFruitVege) {
			if (aMap.contains(a)) {
				continue;
			}
			if (ItemUtils.checkForInvalidItems(a)) {
				CORE.RA.addChemicalPlantRecipe(
						new ItemStack[] {
								getBioChip(2),
								ItemUtils.getSimpleStack(a, 10)
						},
						new FluidStack[] {
								FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 1000),
						},
						new ItemStack[] {

						},
						new FluidStack[] {
								FluidUtils.getFluidStack(BioRecipes.mFermentationBase, 200),						
						},
						60 * 20,
						2,
						1);
			}
			
		}
		for (ItemStack a : aSeeds) {
			if (ItemUtils.checkForInvalidItems(a)) {
				CORE.RA.addChemicalPlantRecipe(
						new ItemStack[] {
								getBioChip(3),
								ItemUtils.getSimpleStack(a, 20)
						},
						new FluidStack[] {
								FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 1000),
						},
						new ItemStack[] {

						},
						new FluidStack[] {
								FluidUtils.getFluidStack(BioRecipes.mFermentationBase, 50),						
						},
						60 * 20,
						2,
						1);
			}			
		}

		// Sugar Cane
		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(4),
						ItemUtils.getSimpleStack(Items.reeds, 16)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 1000),
				},
				new ItemStack[] {

				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mFermentationBase, 75),						
				},
				90 * 20,
				30,
				1);

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(5),
						ItemUtils.getSimpleStack(Items.reeds, 32),
						ItemUtils.getSimpleStack(ModItems.dustCalciumCarbonate, 4)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mThermalWater, 2000),
				},
				new ItemStack[] {

				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mFermentationBase, 150),						
				},
				120 * 20,
				30,
				1);

		// Sugar Beet
		if (OreDictUtils.containsValidEntries("cropSugarbeet")) {

			CORE.RA.addChemicalPlantRecipe(
					new ItemStack[] {
							getBioChip(4),
							ItemUtils.getItemStackOfAmountFromOreDict("cropSugarbeet", 8),
					},
					new FluidStack[] {
							FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 1000),
					},
					new ItemStack[] {

					},
					new FluidStack[] {
							FluidUtils.getFluidStack(BioRecipes.mFermentationBase, 75),						
					},
					60 * 20,
					30,
					1);

			CORE.RA.addChemicalPlantRecipe(
					new ItemStack[] {
							getBioChip(5),
							ItemUtils.getItemStackOfAmountFromOreDict("cropSugarbeet", 16),
							ItemUtils.getSimpleStack(ModItems.dustCalciumCarbonate, 4)
					},
					new FluidStack[] {
							FluidUtils.getFluidStack(BioRecipes.mThermalWater, 2000),
					},
					new ItemStack[] {

					},
					new FluidStack[] {
							FluidUtils.getFluidStack(BioRecipes.mFermentationBase, 150),						
					},
					90 * 20,
					30,
					1);
		}

		// Produce Acetone, Butanol and Ethanol
		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(5),
						ItemUtils.getItemStackOfAmountFromOreDict("cellFermentationBase", 48),
						ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownCelluloseFiber, 16),
						ItemUtils.getSimpleStack(AgriculturalChem.mRedCelluloseFiber, 16),
				},
				new FluidStack[] {

				},
				new ItemStack[] {
						ItemUtils.getItemStackOfAmountFromOreDict("cellButanol", 6),
						ItemUtils.getItemStackOfAmountFromOreDict("cellAcetone", 3),
						ItemUtils.getItemStackOfAmountFromOreDict("cellEthanol", 1),
						CI.emptyCells(38)
				},
				new FluidStack[] {

				},
				900 * 20,
				32,
				2);


	}

	private static void recipePropionicAcid() {
		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						CI.getGreenCatalyst(0)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mEthylene, 500),
						FluidUtils.getFluidStack(BioRecipes.mCarbonMonoxide, 500),
						FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 2000),
				},
				new ItemStack[] {
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mPropionicAcid, 250),						
				},
				20 * 20,
				60,
				1);
	}

	private static void recipeEthanol() {

		CORE.RA.addDistilleryRecipe(
				BioRecipes.getBioChip(2),
				FluidUtils.getFluidStack(BioRecipes.mFermentationBase, 1000),
				FluidUtils.getFluidStack(BioRecipes.mEthanol, 100),
				null,
				20 * 20,
				60,
				false);

	}

	private static void recipeGoldenBrownCelluloseFiber() {
		CORE.RA.addFluidExtractionRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownCelluloseFiber, 20),
				FluidUtils.getFluidStack(BioRecipes.mAmmonia, 50),
				20 * 30,
				120);
	}

	private static void recipeRedCelluloseFiber() {
		CORE.RA.addExtractorRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mRedCelluloseFiber, 20),
				ItemUtils.getSimpleStack(ModItems.dustCalciumCarbonate, 5),
				20 * 30,
				240);		
	}

	private static void recipeSodiumHydroxide() {
		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(4)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mSalineWater, 1000),
				},
				new ItemStack[] {
						ItemUtils.getSimpleStack(AgriculturalChem.mSodiumHydroxide, 1)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mChlorine, 400),
						FluidUtils.getFluidStack(BioRecipes.mHydrogen, 600),						
				},
				300 * 20,
				120,
				1);

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(5),
						ItemUtils.getItemStackOfAmountFromOreDict("dustSodium", 5)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 5000),
				},
				new ItemStack[] {
						ItemUtils.getSimpleStack(AgriculturalChem.mSodiumHydroxide, 5)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mHydrogen, 5000),						
				},
				60 * 20,
				60,
				1);
	}

	private static void recipeSodiumCarbonate() {

		if (OreDictUtils.containsValidEntries("fuelCoke")) {			
			CORE.RA.addChemicalPlantRecipe(
					new ItemStack[] {
							getBioChip(18),
							ItemUtils.getItemStackOfAmountFromOreDict("fuelCoke", 3),
							ItemUtils.getSimpleStack(AgriculturalChem.mSodiumCarbonate, 3),
							ItemUtils.getSimpleStack(AgriculturalChem.mAluminiumPellet, 6)
					},
					new FluidStack[] {
							FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 1000),
					},
					new ItemStack[] {
							ItemUtils.getSimpleStack(AgriculturalChem.mSodiumAluminate, 18)
					},
					new FluidStack[] {
							FluidUtils.getFluidStack(BioRecipes.mHydrogen, 200),
							FluidUtils.getFluidStack(BioRecipes.mCarbonDioxide, 1600),
					},
					120 * 20,
					120,
					1);

		}	

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(18),
						ItemUtils.getItemStackOfAmountFromOreDict("dustCoal", 6),
						ItemUtils.getSimpleStack(AgriculturalChem.mSodiumCarbonate, 4),
						ItemUtils.getSimpleStack(AgriculturalChem.mAluminiumPellet, 6)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 1000),
				},
				new ItemStack[] {
						ItemUtils.getSimpleStack(AgriculturalChem.mSodiumAluminate, 16)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mHydrogen, 100),
						FluidUtils.getFluidStack(BioRecipes.mCarbonDioxide, 1750),
				},
				180 * 20,
				120,
				1);

	}

	private static void recipePelletMold() {
		GregtechItemList.Pellet_Mold.set(ItemUtils.getSimpleStack(AgriculturalChem.mPelletMold, 1));		
		GT_Values.RA.addLatheRecipe(
				ALLOY.TUMBAGA.getBlock(1),
				GregtechItemList.Pellet_Mold.get(1),
				null,
				20 * 30 * 15,
				90);	
	}

	private static void recipeAluminiumPellet() {

		// Ore Names, no prefix
		AutoMap<String> aOreNames = new AutoMap<String>();	

		aOreNames.put("Lazurite");
		aOreNames.put("Bauxite");
		aOreNames.put("Grossular");
		aOreNames.put("Pyrope");
		aOreNames.put("Sodalite");
		aOreNames.put("Spodumene");
		aOreNames.put("Ruby");
		aOreNames.put("Sapphire");
		aOreNames.put("GreenSapphire");

		// Voltage
		HashMap<String, Integer> aOreData1 = new HashMap<String, Integer>();
		// Input Count
		HashMap<String, Integer> aOreData2 = new HashMap<String, Integer>();
		// Output Count
		HashMap<String, Integer> aOreData3 = new HashMap<String, Integer>();

		aOreData1.put("Lazurite", 120);
		aOreData1.put("Bauxite", 90);
		aOreData1.put("Grossular", 90);
		aOreData1.put("Pyrope", 90);
		aOreData1.put("Sodalite", 90);
		aOreData1.put("Spodumene", 90);
		aOreData1.put("Ruby", 60);
		aOreData1.put("Sapphire", 30);
		aOreData1.put("GreenSapphire", 30);		
		aOreData2.put("Lazurite", 14);
		aOreData2.put("Bauxite", 39);
		aOreData2.put("Grossular", 20);
		aOreData2.put("Pyrope", 20);
		aOreData2.put("Sodalite", 11);
		aOreData2.put("Spodumene", 10);
		aOreData2.put("Ruby", 6);
		aOreData2.put("Sapphire", 5);
		aOreData2.put("GreenSapphire", 5);
		aOreData3.put("Lazurite", 3);
		aOreData3.put("Bauxite", 16);
		aOreData3.put("Grossular", 2);
		aOreData3.put("Pyrope", 2);
		aOreData3.put("Sodalite", 3);
		aOreData3.put("Spodumene", 1);
		aOreData3.put("Ruby", 2);
		aOreData3.put("Sapphire", 2);
		aOreData3.put("GreenSapphire", 2);

		// Assemble all valid crushed ore types for making pellet mix
		HashMap<String, ItemStack> aOreCache = new HashMap<String, ItemStack>();
		for (String aOreName : aOreNames) {
			String aTemp = aOreName;
			aOreName = "crushedPurified" + aOreName;
			if (ItemUtils.doesOreDictHaveEntryFor(aOreName)) {
				aOreCache.put(aTemp, ItemUtils.getItemStackOfAmountFromOreDict(aOreName, 1));
			}
		}

		for (String aOreName : aOreNames) {
			CORE.RA.addChemicalPlantRecipe(
					new ItemStack[] {
							CI.getNumberedBioCircuit(14),
							ItemUtils.getSimpleStack(aOreCache.get(aOreName), aOreData2.get(aOreName))
					}, 
					new FluidStack[] {
							FluidUtils.getSteam(2000 * aOreData2.get(aOreName))
					}, 
					new ItemStack[] {
							ItemUtils.getSimpleStack(AgriculturalChem.mCleanAluminiumMix, (int) (Math.ceil(aOreData3.get(aOreName) * 1.4)))
					}, 
					new FluidStack[] {
							FluidUtils.getFluidStack(AgriculturalChem.RedMud, 100 * (int) (Math.ceil(aOreData3.get(aOreName) * 1.4)))
					}, 
					20 * 60, 
					aOreData1.get(aOreName), 
					aOreName.equals("Bauxite") ? 2 : 1);
		}

		GT_Values.RA.addExtruderRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mCleanAluminiumMix, 3),
				ItemUtils.getSimpleStack(AgriculturalChem.mPelletMold, 0),
				ItemUtils.getSimpleStack(AgriculturalChem.mAluminiumPellet, 4),
				20 * 30, 
				64);


	}

	private static void recipeAlumina() {
		GT_Values.RA.addBlastRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mSodiumAluminate, 24),
				ItemUtils.getSimpleStack(AgriculturalChem.mSodiumHydroxide, 3),
				FluidUtils.getFluidStack(mCarbonDioxide, 6000), 
				GT_Values.NF,
				ItemUtils.getSimpleStack(AgriculturalChem.mAlumina, 24),
				ItemUtils.getSimpleStack(AgriculturalChem.mSodiumCarbonate, 2),
				20 * 120,
				120,
				1200);
	}

	private static void recipeAluminium() {
		GT_Values.RA.addBlastRecipe(
				ItemUtils.getSimpleStack(AgriculturalChem.mAlumina, 24),
				ItemUtils.getItemStackOfAmountFromOreDict("dustCarbon", 4),
				GT_Values.NF,
				GT_Values.NF,
				ItemUtils.getItemStackOfAmountFromOreDict("dustAluminium", 24),
				null,
				20 * 120,
				120,
				1600);		
	}

	private static void recipeCalciumCarbonate() {

	}

	private static void recipeLithiumChloride() {

		if (OreDictUtils.containsValidEntries("dustRockSalt")) {
			GT_Values.RA.addElectrolyzerRecipe(
					ItemUtils.getItemStackOfAmountFromOreDict("dustRockSalt", 8), 
					ItemUtils.getSimpleStack(AgriculturalChem.mLithiumChloride, 10),
					FluidUtils.getFluidStack(BioRecipes.mAir, 4000),
					FluidUtils.getFluidStack(BioRecipes.mChlorine, 500),					
					ItemUtils.getItemStackOfAmountFromOreDict("dustLithium", 2), 
					ItemUtils.getItemStackOfAmountFromOreDict("dustSmallLithium", 3), 
					ItemUtils.getItemStackOfAmountFromOreDict("dustSmallLithium", 3), 
					ItemUtils.getItemStackOfAmountFromOreDict("dustTinyLithium", 5), 
					ItemUtils.getItemStackOfAmountFromOreDict("dustPotassium", 2), 
					ItemUtils.getItemStackOfAmountFromOreDict("dustSmallPotassium", 5),					
					new int[] {7500, 8000, 8500, 9000, 7500, 8500}, 					
					60 * 30, 
					60);
		}
		if (OreDictUtils.containsValidEntries("dustPotash")) {
			GT_Values.RA.addElectrolyzerRecipe(
					ItemUtils.getItemStackOfAmountFromOreDict("dustPotash", 10), 
					ItemUtils.getSimpleStack(AgriculturalChem.mLithiumChloride, 16),
					FluidUtils.getFluidStack(BioRecipes.mThermalWater, 2000),
					FluidUtils.getFluidStack(BioRecipes.mChlorine, 250),					
					ItemUtils.getItemStackOfAmountFromOreDict("dustLithium", 3), 
					ItemUtils.getItemStackOfAmountFromOreDict("dustSmallLithium", 5), 
					ItemUtils.getItemStackOfAmountFromOreDict("dustSmallLithium", 5), 
					ItemUtils.getItemStackOfAmountFromOreDict("dustTinyLithium", 7), 
					ItemUtils.getItemStackOfAmountFromOreDict("dustAsh", 2), 
					ItemUtils.getItemStackOfAmountFromOreDict("dustAsh", 2),					
					new int[] {7500, 8000, 8500, 9000, 9000, 9000}, 					
					45 * 30, 
					90);
		}

	}

	private static void recipeAlginicAcid() {

		/*// Turn into Cellulose Pulp
		CORE.RA.addSixSlotAssemblingRecipe(new ItemStack[] {
				getBioChip(7),
				ItemUtils.getSimpleStack(AgriculturalChem.mCelluloseFiber, 20),
				ItemUtils.getSimpleStack(AgriculturalChem.mAlginicAcid, 5)
		},
				GT_Values.NF,
				ItemUtils.getSimpleStack(AgriculturalChem.mCellulosePulp, 20),
				90 * 20,
				16);*/
	}

	private static void recipeSulfuricAcid() {


		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(7),
						ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownCelluloseFiber, 10),
						ItemUtils.getSimpleStack(AgriculturalChem.mBrownAlgaeBiosmass, 30)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 2000),
				},
				new ItemStack[] {

				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mSulfuricAcid, 50),						
				},
				60 * 20,
				60,
				1);

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(7),
						ItemUtils.getSimpleStack(AgriculturalChem.mRedCelluloseFiber, 5),
						ItemUtils.getSimpleStack(AgriculturalChem.mRedAlgaeBiosmass, 30)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 2000),
				},
				new ItemStack[] {

				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mSulfuricAcid, 250),						
				},
				60 * 20,
				120,
				2);

	}

	private static void recipeUrea() {


		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(9),
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mAmmonia, 600),
						FluidUtils.getFluidStack(BioRecipes.mCarbonDioxide, 400),
				},
				new ItemStack[] {

				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mUrea, 400),	
						FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 600),						
				},
				120 * 20,
				30,
				1);

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(9),
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mUrea, 200),
						FluidUtils.getFluidStack(BioRecipes.mFormaldehyde, 200),
				},
				new ItemStack[] {

				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mLiquidResin, 200)						
				},
				120 * 20,
				30,
				1);
	}

	private static void recipeRawBioResin() {

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(3),
						ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 10),
						ItemUtils.getSimpleStack(Blocks.dirt, 20)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 1000),
				},
				new ItemStack[] {
						ItemUtils.getSimpleStack(AgriculturalChem.mRawBioResin, 1),						
				},
				new FluidStack[] {

				},
				60 * 20,
				30,
				1);
	}

	private static void recipeLiquidResin() {

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(3),
						ItemUtils.getSimpleStack(AgriculturalChem.mRawBioResin, 1)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mEthanol, 200),
				},
				new ItemStack[] {

				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mLiquidResin, 50),						
				},
				60 * 20,
				30,
				1);

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(3),
						ItemUtils.getSimpleStack(AgriculturalChem.mCellulosePulp, 8)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mLiquidResin, 144),
				},
				new ItemStack[] {
						ItemUtils.getSimpleStack(Ic2Items.resin, 1)
				},
				new FluidStack[] {

				},
				120 * 20,
				30,
				1);


	}

	private static void recipeCompost() {
		ItemStack aFert;
		if (LoadedMods.Forestry) {
			aFert = ItemUtils.getSimpleStack(AgriculturalChem.aFertForestry, 2);			
			CORE.RA.addChemicalPlantRecipe(
					new ItemStack[] {
							getBioChip(11),
							ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 16),
							ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 8)
					},
					new FluidStack[] {
							FluidUtils.getFluidStack(BioRecipes.mUrea, 500),
					},
					new ItemStack[] {
							aFert
					},
					new FluidStack[] {

					},
					120 * 20,
					60,
					2);
		}

		aFert = ItemUtils.getSimpleStack(AgriculturalChem.aFertIC2, 2);
		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(12),
						ItemUtils.getSimpleStack(AgriculturalChem.mGreenAlgaeBiosmass, 16),
						ItemUtils.getSimpleStack(AgriculturalChem.mCompost, 8)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mUrea, 500),
				},
				new ItemStack[] {
						aFert
				},
				new FluidStack[] {

				},
				120 * 20,
				60,
				2);

	}

	private static void recipeMethane() {

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(12),
						ItemUtils.getSimpleStack(AgriculturalChem.mAlgaeBiosmass, 20)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mDistilledWater, 500),
				},
				new ItemStack[] {

				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mMethane, 20),						
				},
				20 * 20,
				8,
				1);

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(13),
						ItemUtils.getSimpleStack(AgriculturalChem.mCelluloseFiber, 64),
						ItemUtils.getSimpleStack(AgriculturalChem.mGoldenBrownCelluloseFiber, 48),
						ItemUtils.getSimpleStack(AgriculturalChem.mRedCelluloseFiber, 32)
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mMethane, 2000),
				},
				new ItemStack[] {

				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mEthylene, 1250),						
				},
				60 * 20,
				60,
				2);
	}

	private static void recipeBenzene() {

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(19),
						CI.getGreenCatalyst(0),
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mMethane, 1000),
				},
				new ItemStack[] {
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mBenzene, 500),					
				},
				8 * 20,
				120,
				2);
	}

	private static void recipeStyrene() {

		CORE.RA.addChemicalPlantRecipe(
				new ItemStack[] {
						getBioChip(20),
						CI.getGreenCatalyst(0),
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mEthylbenzene, 100),
				},
				new ItemStack[] {
				},
				new FluidStack[] {
						FluidUtils.getFluidStack(BioRecipes.mStyrene, 25),					
				},
				16 * 20,
				480,
				3);
	}

	private static void recipeBioChip() {

		GT_ModHandler.addShapelessCraftingRecipe(
				GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 0L), 0, new Object[]{OrePrefixes.circuit.get(Materials.Primitive)});

		long bits = 0;



		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 1L, new Object[0]), bits,
				new Object[]{"d  ", " P ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 2L, new Object[0]), bits,
				new Object[]{" d ", " P ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 3L, new Object[0]), bits,
				new Object[]{"  d", " P ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 4L, new Object[0]), bits,
				new Object[]{"   ", " Pd", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 5L, new Object[0]), bits,
				new Object[]{"   ", " P ", "  d", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 6L, new Object[0]), bits,
				new Object[]{"   ", " P ", " d ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 7L, new Object[0]), bits,
				new Object[]{"   ", " P ", "d  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 8L, new Object[0]), bits,
				new Object[]{"   ", "dP ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 9L, new Object[0]), bits,
				new Object[]{"P d", "   ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 10L, new Object[0]), bits,
				new Object[]{"P  ", "  d", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 11L, new Object[0]), bits,
				new Object[]{"P  ", "   ", "  d", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 12L, new Object[0]), bits,
				new Object[]{"P  ", "   ", " d ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 13L, new Object[0]), bits,
				new Object[]{"  P", "   ", "  d", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 14L, new Object[0]), bits,
				new Object[]{"  P", "   ", " d ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 15L, new Object[0]), bits,
				new Object[]{"  P", "   ", "d  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 16L, new Object[0]), bits,
				new Object[]{"  P", "d  ", "   ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 17L, new Object[0]), bits,
				new Object[]{"   ", "   ", "d P", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 18L, new Object[0]), bits,
				new Object[]{"   ", "d  ", "  P", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 19L, new Object[0]), bits,
				new Object[]{"d  ", "   ", "  P", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 20L, new Object[0]), bits,
				new Object[]{" d ", "   ", "  P", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 21L, new Object[0]), bits,
				new Object[]{"d  ", "   ", "P  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 22L, new Object[0]), bits,
				new Object[]{" d ", "   ", "P  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 23L, new Object[0]), bits,
				new Object[]{"  d", "   ", "P  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
		addCraftingRecipe(GregtechItemList.Circuit_BioRecipeSelector.getWithDamage(1L, 24L, new Object[0]), bits,
				new Object[]{"   ", "  d", "P  ", 'P', GregtechItemList.Circuit_BioRecipeSelector.getWildcard(1L, new Object[0])});
	}

	public static boolean addCraftingRecipe(ItemStack aResult, long aBitMask, Object[] aRecipe) {
		Method mAddRecipe = ReflectionUtils.getMethod(GT_ModHandler.class, "addCraftingRecipe", new Class[] {
				ItemStack.class, Enchantment[].class, int[].class,
				boolean.class, boolean.class, boolean.class, boolean.class,
				boolean.class, boolean.class, boolean.class, boolean.class,
				boolean.class, boolean.class, boolean.class, boolean.class,
				boolean.class, Object[].class});
		boolean didInvoke = false;
		if (mAddRecipe != null) {
			try {
				didInvoke = (boolean) mAddRecipe.invoke(null, aResult,
						new Enchantment[] {},
						new int[] {},
						false,
						false,
						false,
						false,
						false,
						false,
						false,
						false,
						false,
						false,
						false, 
						false, 
						true, 
						aRecipe);
			}
			catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return didInvoke;


	}


}
