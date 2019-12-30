package gtPlusPlus.core.item.chemistry;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gtPlusPlus.api.helpers.MaterialHelper;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.plugin.agrichem.BioRecipes;
import gtPlusPlus.plugin.agrichem.item.algae.ItemAgrichemBase;
import gtPlusPlus.plugin.agrichem.item.algae.ItemAlgaeBase;
import gtPlusPlus.plugin.agrichem.item.algae.ItemBioChip;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class AgriculturalChem extends ItemPackage {

	private static boolean aBOP;
	private static boolean aTiCon;

	private static AutoMap<FluidStack> mBloodFluids = new AutoMap<FluidStack>();

	/**
	 * Fluids
	 */

	// Poop Juice
	public static Fluid PoopJuice;
	// Manure Slurry
	public static Fluid ManureSlurry;
	// Fertile Manure Slurry
	public static Fluid FertileManureSlurry;
	// Blood
	public static Fluid CustomBlood;
	// Red Mud
	public static Fluid RedMud;
	/**
	 * Items
	 */

	// Manure Byproducts
	public static Item dustManureByproducts;
	// Organic Fertilizer
	public static Item dustOrganicFertilizer;
	// Dirt
	public static Item dustDirt;

	// Poop Juice
	// vv - Centrifuge
	// Manure Slurry && Manure Byproducts -> (Elements) Centrifuge to several tiny
	// piles
	// vv - Chem Reactor - Add Peat, Meat
	// Organic Fertilizer
	// vv - Dehydrate
	// Fertilizer

	// Poop Juice
	// vv - Mixer - Add Blood, Bone, Meat (1000L Poo, 200L Blood, x2 Bone, x3 Meat)
	// Fertile Manure Slurry
	// vv - Chem Reactor - Add Peat x1.5
	// Organic Fertilizer x3
	// vv - Dehydrate
	// Fertilizer


	
	public static Item mAlgae;
	public static Item mBioCircuit;
	public static Item mAgrichemItem1;
	
	/*
	 * 0 - Algae Biomass
	 * 1 - Green Algae Biomass
	 * 2 - Brown Algae Biomass
	 * 3 - Golden-Brown Algae Biomass
	 * 4 - Red Algae Biomass
	 * 5 - Cellulose Fiber
	 * 6 - Golden-Brown Cellulose Fiber
	 * 7 - Red Cellulose Fiber
	 * 8 - Compost
	 * 9 - Wood Pellet
	 * 10 - Wood Brick
	 * 11 - Cellulose Pulp
	 * 12 - Raw Bio Resin
	 * 13 - Catalyst Carrier
	 * 14 - Green Metal Catalyst
	 * 15 - Alginic Acid
	 * 16 - Alumina
	 * 17 - Aluminium Pellet
	 * 18 - Sodium Aluminate
	 * 19 - Sodium Hydroxide // Exists in Newer GT
	 * 20 - Sodium Carbonate
	 * 21 - Lithium Chloride
	 * 22 - Pellet Mold
	 * 23 - Clean Aluminium Mix
	 */

	public static ItemStack mAlgaeBiosmass;
	public static ItemStack mGreenAlgaeBiosmass;
	public static ItemStack mBrownAlgaeBiosmass;
	public static ItemStack mGoldenBrownAlgaeBiosmass;
	public static ItemStack mRedAlgaeBiosmass;
	public static ItemStack mCelluloseFiber;
	public static ItemStack mGoldenBrownCelluloseFiber;
	public static ItemStack mRedCelluloseFiber;
	public static ItemStack mCompost;
	public static ItemStack mWoodPellet;
	public static ItemStack mWoodBrick;
	public static ItemStack mCellulosePulp;
	public static ItemStack mRawBioResin;
	public static ItemStack mCatalystCarrier;
	public static ItemStack mGreenCatalyst;
	public static ItemStack mAlginicAcid;
	public static ItemStack mAlumina;
	public static ItemStack mAluminiumPellet;
	public static ItemStack mSodiumAluminate;
	public static ItemStack mSodiumHydroxide;
	public static ItemStack mSodiumCarbonate;
	public static ItemStack mLithiumChloride;
	public static ItemStack mPelletMold;
	public static ItemStack mCleanAluminiumMix;
	

	@Override
	public void items() {
		// Nitrogen, Ammonium Nitrate, Phosphates, Calcium, Copper, Carbon
		dustManureByproducts = ItemUtils.generateSpecialUseDusts("ManureByproducts", "Manure Byproduct",
				"(N2H4O3)N2P2Ca3CuC8", Utils.rgbtoHexValue(110, 75, 25))[0];

		// Basically Guano
		dustOrganicFertilizer = ItemUtils.generateSpecialUseDusts("OrganicFertilizer", "Organic Fertilizer",
				"Ca5(PO4)3(OH)", Utils.rgbtoHexValue(240, 240, 240))[0];

		// Dirt Dust :)
		dustDirt = ItemUtils.generateSpecialUseDusts("Dirt", "Dried Earth", Utils.rgbtoHexValue(65, 50, 15))[0];	
		
		mAlgae = new ItemAlgaeBase();
		mAgrichemItem1 = new ItemAgrichemBase();
		mBioCircuit = new ItemBioChip();		
		GregtechItemList.Circuit_BioRecipeSelector.set(mBioCircuit);
		
		

		mAlgaeBiosmass = ItemUtils.simpleMetaStack(mAgrichemItem1, 0, 1);
		mGreenAlgaeBiosmass = ItemUtils.simpleMetaStack(mAgrichemItem1, 1, 1);
		mBrownAlgaeBiosmass = ItemUtils.simpleMetaStack(mAgrichemItem1, 2, 1);
		mGoldenBrownAlgaeBiosmass = ItemUtils.simpleMetaStack(mAgrichemItem1, 3, 1);
		mRedAlgaeBiosmass = ItemUtils.simpleMetaStack(mAgrichemItem1, 4, 1);
		mCelluloseFiber = ItemUtils.simpleMetaStack(mAgrichemItem1, 5, 1);
		mGoldenBrownCelluloseFiber = ItemUtils.simpleMetaStack(mAgrichemItem1, 6, 1);
		mRedCelluloseFiber = ItemUtils.simpleMetaStack(mAgrichemItem1, 7, 1);
		mCompost = ItemUtils.simpleMetaStack(mAgrichemItem1, 8, 1);
		mWoodPellet = ItemUtils.simpleMetaStack(mAgrichemItem1, 9, 1);
		mWoodBrick = ItemUtils.simpleMetaStack(mAgrichemItem1, 10, 1);
		mCellulosePulp = ItemUtils.simpleMetaStack(mAgrichemItem1, 11, 1);
		mRawBioResin = ItemUtils.simpleMetaStack(mAgrichemItem1, 12, 1);
		mCatalystCarrier = ItemUtils.simpleMetaStack(mAgrichemItem1, 13, 1);
		mGreenCatalyst = ItemUtils.simpleMetaStack(mAgrichemItem1, 14, 1);
		mAlginicAcid = ItemUtils.simpleMetaStack(mAgrichemItem1, 15, 1);
		mAlumina = ItemUtils.simpleMetaStack(mAgrichemItem1, 16, 1);
		mAluminiumPellet = ItemUtils.simpleMetaStack(mAgrichemItem1, 17, 1);
		mSodiumAluminate = ItemUtils.simpleMetaStack(mAgrichemItem1, 18, 1);
		
		/**
		 * If It exists, don't add a new one.
		 */
		if (OreDictionary.doesOreNameExist("dustSodiumHydroxide_GT5U") || OreDictionary.doesOreNameExist("dustSodiumHydroxide")) {
			List<ItemStack> aTest = OreDictionary.getOres("dustSodiumHydroxide", false);
			ItemStack aTestStack;
			if (aTest.isEmpty()) {
				aTest = OreDictionary.getOres("dustSodiumHydroxide_GT5U", false);
				if (aTest.isEmpty()) {
					aTestStack = ItemUtils.simpleMetaStack(mAgrichemItem1, 19, 1);	
				}
				else {
					aTestStack = aTest.get(0);
				}
			}
			else {
				aTestStack = aTest.get(0);
			}
			mSodiumHydroxide = aTestStack;
		}
		else {
			mSodiumHydroxide = ItemUtils.simpleMetaStack(mAgrichemItem1, 19, 1);			
		}		
		mSodiumCarbonate = ItemUtils.simpleMetaStack(mAgrichemItem1, 20, 1);
		mLithiumChloride = ItemUtils.simpleMetaStack(mAgrichemItem1, 21, 1);
		mPelletMold = ItemUtils.simpleMetaStack(mAgrichemItem1, 22, 1);
		mCleanAluminiumMix = ItemUtils.simpleMetaStack(mAgrichemItem1, 23, 1);

		ItemUtils.addItemToOreDictionary(mGreenAlgaeBiosmass, "biomassGreenAlgae");
		ItemUtils.addItemToOreDictionary(mBrownAlgaeBiosmass, "biomassBrownAlgae");
		ItemUtils.addItemToOreDictionary(mGoldenBrownAlgaeBiosmass, "biomassGoldenBrownAlgae");
		ItemUtils.addItemToOreDictionary(mRedAlgaeBiosmass, "biomassRedAlgae");

		ItemUtils.addItemToOreDictionary(mCelluloseFiber, "fiberCellulose");
		ItemUtils.addItemToOreDictionary(mGoldenBrownCelluloseFiber, "fiberCellulose");
		ItemUtils.addItemToOreDictionary(mGoldenBrownCelluloseFiber, "fiberGoldenBrownCellulose");
		ItemUtils.addItemToOreDictionary(mRedCelluloseFiber, "fiberCellulose");
		ItemUtils.addItemToOreDictionary(mRedCelluloseFiber, "fiberRedCellulose");
		
		ItemUtils.addItemToOreDictionary(mWoodPellet, "pelletWood");
		ItemUtils.addItemToOreDictionary(mWoodBrick, "brickWood");
		ItemUtils.addItemToOreDictionary(mCellulosePulp, "pulpCellulose");

		ItemUtils.addItemToOreDictionary(mCatalystCarrier, "catalystEmpty");
		ItemUtils.addItemToOreDictionary(mGreenCatalyst, "catalystAluminiumSilver");
		ItemUtils.addItemToOreDictionary(mAlginicAcid, "dustAlginicAcid");
		ItemUtils.addItemToOreDictionary(mAlumina, "dustAlumina");
		ItemUtils.addItemToOreDictionary(mAluminiumPellet, "pelletAluminium");

		ItemUtils.addItemToOreDictionary(mSodiumAluminate, "dustSodiumAluminate");
		ItemUtils.addItemToOreDictionary(mSodiumHydroxide, "dustSodiumHydroxide");	
		ItemUtils.addItemToOreDictionary(mSodiumCarbonate, "dustSodiumCarbonate");
		ItemUtils.addItemToOreDictionary(mLithiumChloride, "dustLithiumChloride");	
		
		//Handle GT NaOH dusts
		List<ItemStack> NaOHSmall = OreDictionary.getOres("dustSmallSodiumHydroxide_GT5U", false);
		if (!NaOHSmall.isEmpty()) {
			ItemUtils.addItemToOreDictionary(NaOHSmall.get(0), "dustSmallSodiumHydroxide");				
		}
		List<ItemStack> NaOHTiny = OreDictionary.getOres("dustTinySodiumHydroxide_GT5U", false);
		if (!NaOHTiny.isEmpty()) {
			ItemUtils.addItemToOreDictionary(NaOHTiny.get(0), "dustTinySodiumHydroxide");				
		}
		
		
	}

	@Override
	public void blocks() {
		// None yet
	}

	@Override
	public void fluids() {
		// Sewage
		PoopJuice = FluidUtils.generateFluidNonMolten("raw.waste", "Raw Animal Waste", 32 + 175,
				new short[] { 100, 70, 30, 100 }, null, null, 0, true);

		// Sewage
		ManureSlurry = FluidUtils.generateFluidNonMolten("manure.slurry", "Manure Slurry", 39 + 175,
				new short[] { 75, 45, 15, 100 }, null, null, 0, true);

		// Sewage
		FertileManureSlurry = FluidUtils.generateFluidNonMolten("fertile.manure.slurry", "Fertile Manure Slurry",
				45 + 175, new short[] { 65, 50, 15, 100 }, null, null, 0, true);
		
		RedMud = FluidUtils.generateFluidNoPrefix("mud.red.slurry", "Red Mud Slurry", 32 + 175, new short[] { 180, 35, 25, 100 }, true);
		
	}
	
	

	public AgriculturalChem() {
		super();
		
		aBOP = LoadedMods.BiomesOPlenty;
		aTiCon = LoadedMods.TiCon;

		Logger.INFO("Adding Agrochemical content");

		FluidStack aBlood;
		if (aBOP) {
			aBlood = FluidUtils.getFluidStack("hell_blood", 100);
			if (aBlood != null) {
				Logger.INFO("Found Biome's o Plenty, enabled Blood support.");
				CustomBlood = aBlood.getFluid();
				mBloodFluids.put(aBlood);
			}
		}

		if (aTiCon) {
			aBlood = FluidUtils.getFluidStack("hell_blood", 100);
			if (aBlood != null) {
				Logger.INFO("Found Tinker's Construct, enabled Blood support.");
				CustomBlood = aBlood.getFluid();
				mBloodFluids.put(FluidUtils.getFluidStack("blood", 100));
			}
		}

		// Handle Blood Internally, Create if required.
		if (mBloodFluids.isEmpty() || CustomBlood == null) {
			Logger.INFO(
					"Did not find any existing Blood fluids. Trying to wildcard search the fluid registry, then generate our own if that fails.");
			FluidStack aTempBlood = FluidUtils.getWildcardFluidStack("blood", 100);
			if (aTempBlood != null) {
				CustomBlood = aTempBlood.getFluid();
			} else {
				aTempBlood = FluidUtils.getWildcardFluidStack("hell_blood", 100);
				if (aTempBlood == null) {
					CustomBlood = FluidUtils.generateFluidNoPrefix("blood", "Blood", 32 + 175,
							new short[] { 175, 25, 25, 100 }, true);
				} else {
					CustomBlood = aTempBlood.getFluid();
				}
			}
			Logger.INFO("Using " + CustomBlood.getName());
			mBloodFluids.put(FluidUtils.getFluidStack(CustomBlood, 100));
		}

	}

	private final static AutoMap<ItemStack> mMeats = new AutoMap<ItemStack>();
	private final static AutoMap<ItemStack> mFish = new AutoMap<ItemStack>();
	private final static AutoMap<ItemStack> mFruits = new AutoMap<ItemStack>();
	private final static AutoMap<ItemStack> mVege = new AutoMap<ItemStack>();
	private final static AutoMap<ItemStack> mNuts = new AutoMap<ItemStack>();
	private final static AutoMap<ItemStack> mSeeds = new AutoMap<ItemStack>();
	private final static AutoMap<ItemStack> mPeat = new AutoMap<ItemStack>();
	private final static AutoMap<ItemStack> mBones = new AutoMap<ItemStack>();
	private final static AutoMap<ItemStack> mBoneMeal = new AutoMap<ItemStack>();

	private final static AutoMap<ItemStack> mList_Master_Meats = new AutoMap<ItemStack>();
	private final static AutoMap<ItemStack> mList_Master_FruitVege = new AutoMap<ItemStack>();
	private final static AutoMap<ItemStack> mList_Master_Seeds = new AutoMap<ItemStack>();
	private final static AutoMap<ItemStack> mList_Master_Bones = new AutoMap<ItemStack>();

	private static void processAllOreDict() {
		processOreDict("listAllmeatraw", mMeats);
		processOreDict("listAllfishraw", mFish);
		processOreDict("listAllfruit", mFruits);
		processOreDict("listAllVeggie", mVege);
		processOreDict("listAllnut", mNuts);
		processOreDict("listAllSeed", mSeeds);
		processOreDict("brickPeat", mPeat);
		processOreDict("bone", mBones);
		processOreDict("dustBone", mBoneMeal);
		// Just make a mega list, makes life easier.
		if (!mMeats.isEmpty()) {
			for (ItemStack g : mMeats) {
				mList_Master_Meats.put(g);
			}
		}
		if (!mFish.isEmpty()) {
			for (ItemStack g : mFish) {
				mList_Master_Meats.put(g);
			}
		}
		if (!mFruits.isEmpty()) {
			for (ItemStack g : mFruits) {
				mList_Master_FruitVege.put(g);
			}
		}
		if (!mVege.isEmpty()) {
			for (ItemStack g : mVege) {
				mList_Master_FruitVege.put(g);
			}
		}
		if (!mNuts.isEmpty()) {
			for (ItemStack g : mNuts) {
				mList_Master_FruitVege.put(g);
			}
		}
		if (!mSeeds.isEmpty()) {
			for (ItemStack g : mSeeds) {
				mList_Master_Seeds.put(g);
			}
		}
		if (!mBoneMeal.isEmpty()) {
			for (ItemStack g : mBoneMeal) {
				mList_Master_Bones.put(g);
			}
		}
		if (!mBones.isEmpty()) {
			for (ItemStack g : mBones) {
				mList_Master_Bones.put(g);
			}
		}
	}

	private static void processOreDict(String aOreName, AutoMap<ItemStack> aMap) {
		ArrayList<ItemStack> aTemp = OreDictionary.getOres(aOreName);
		if (!aTemp.isEmpty()) {
			for (ItemStack stack : aTemp) {
				aMap.put(stack);
			}
		}
	}

	private static void addBasicSlurryRecipes() {

		ItemStack aManureByprod1 = ItemUtils.getItemStackOfAmountFromOreDict("dustTinyManureByproducts", 1);
		ItemStack aManureByprod2 = ItemUtils.getItemStackOfAmountFromOreDict("dustSmallManureByproducts", 1);
		ItemStack aDirtDust = ItemUtils.getSimpleStack(dustDirt, 1);

		// Poop Juice to Basic Slurry
		GT_Values.RA.addCentrifugeRecipe(CI.getNumberedCircuit(10), null, FluidUtils.getFluidStack(PoopJuice, 1000), // In
				// Fluid
				FluidUtils.getFluidStack(ManureSlurry, 250), // Out Fluid
				aDirtDust, aDirtDust, aManureByprod1, aManureByprod1, aManureByprod1, aManureByprod1,
				new int[] { 2000, 2000, 500, 500, 250, 250 }, // Chances
				10 * 20, // Time
				30); // EU

		// More Efficient way to get byproducts, less Slurry
		GT_Values.RA.addCentrifugeRecipe(CI.getNumberedCircuit(20), null, FluidUtils.getFluidStack(PoopJuice, 1000), // In
				// Fluid
				FluidUtils.getFluidStack(ManureSlurry, 50), // Out Fluid
				aDirtDust, aDirtDust, aManureByprod1, aManureByprod1, aManureByprod2, aManureByprod2,
				new int[] { 4000, 3000, 1250, 1250, 675, 675 }, // Chances
				20 * 20, // Time
				60); // EU

	}

	private static void addAdvancedSlurryRecipes() {

		ItemStack aCircuit = CI.getNumberedCircuit(10);
		ItemStack aBone;
		ItemStack aMeat;
		ItemStack aEmptyCells = CI.emptyCells(2);
		ItemStack aInputCells = ItemUtils.getItemStackOfAmountFromOreDict("cellRawWaste", 2);
		FluidStack aOutput = FluidUtils.getFluidStack(FertileManureSlurry, 1000);

		for (FluidStack aBloodStack : mBloodFluids) {
			for (ItemStack aBoneStack : mList_Master_Bones) {
				aBone = ItemUtils.getSimpleStack(aBoneStack, 2);
				for (ItemStack aMeatStack : mList_Master_Meats) {
					aMeat = ItemUtils.getSimpleStack(aMeatStack, 5);
					// Poop Juice to Fertile Slurry
					GT_Values.RA.addMixerRecipe(aCircuit, aBone, aMeat, aInputCells, aBloodStack, // Input Fluid
							aOutput, // Output Fluid
							aEmptyCells, // Output Item
							20 * 8, // Time?
							60 // Eu?
							);
				}
			}
		}
	}

	private static void addBasicOrganiseFertRecipes() {
		FluidStack aInputFluid = FluidUtils.getFluidStack(ManureSlurry, 1000);
		ItemStack aOutputDust = ItemUtils.getSimpleStack(dustOrganicFertilizer, 3);
		ItemStack aPeat;
		ItemStack aMeat;
		for (ItemStack aPeatStack : mPeat) {
			aPeat = ItemUtils.getSimpleStack(aPeatStack, 3);
			for (ItemStack aMeatStack : mList_Master_Meats) {
				aMeat = ItemUtils.getSimpleStack(aMeatStack, 5);
				CORE.RA.addChemicalRecipe(aPeat, aMeat, aInputFluid, null, aOutputDust, 20 * 20, 120);
			}

			aPeat = ItemUtils.getSimpleStack(aPeatStack, 2);
			for (ItemStack aMeatStack : mList_Master_FruitVege) {
				aMeat = ItemUtils.getSimpleStack(aMeatStack, 9);
				CORE.RA.addChemicalRecipe(aPeat, aMeat, aInputFluid, null, aOutputDust, 10 * 20, 120);
			}
		}
	}

	private static void addAdvancedOrganiseFertRecipes() {
		FluidStack aInputFluid = FluidUtils.getFluidStack(FertileManureSlurry, 1000);
		ItemStack aOutputDust = ItemUtils.getSimpleStack(dustOrganicFertilizer, 7);
		ItemStack aPeat;
		ItemStack aMeat;
		for (ItemStack aPeatStack : mPeat) {
			aPeat = ItemUtils.getSimpleStack(aPeatStack, 5);
			for (ItemStack aMeatStack : mList_Master_Meats) {
				aMeat = ItemUtils.getSimpleStack(aMeatStack, 7);
				CORE.RA.addChemicalRecipe(aPeat, aMeat, aInputFluid, null, aOutputDust, 10 * 20, 140);
			}
			aPeat = ItemUtils.getSimpleStack(aPeatStack, 3);
			for (ItemStack aMeatStack : mList_Master_FruitVege) {
				aMeat = ItemUtils.getSimpleStack(aMeatStack, 12);
				CORE.RA.addChemicalRecipe(aPeat, aMeat, aInputFluid, null, aOutputDust, 5 * 20, 140);
			}
		}
	}
	
	public static ItemStack aFertForestry;
	public static ItemStack aFertIC2;
	

	private static void addMiscRecipes() {

		ItemStack aDustOrganicFert = ItemUtils.getSimpleStack(dustOrganicFertilizer, 1);
		ItemStack aManureByprod = ItemUtils.getSimpleStack(dustManureByproducts, 1);

		// Dehydrate Organise Fert to Normal Fert.

		/**
		 * Forestry Support
		 */
		if (LoadedMods.Forestry) {
			Field aItemField = ReflectionUtils.getField(ReflectionUtils.getClass("forestry.plugins.PluginCore"), "items");
			try {
				Object aItemRegInstance = aItemField != null ? aItemField.get(aItemField) : null;
				if (aItemRegInstance != null) {
					Field aFertField = ReflectionUtils.getField(aItemRegInstance.getClass(), "fertilizerCompound");	
					Object aItemInstance = aFertField.get(aItemRegInstance);
					if (aItemInstance instanceof Item) {
						aFertForestry = ItemUtils.getSimpleStack((Item) aItemInstance);
						Item aForestryFert = (Item) aItemInstance;
						CORE.RA.addDehydratorRecipe(
								new ItemStack[] { CI.getNumberedCircuit(11), ItemUtils.getSimpleStack(aDustOrganicFert, 4) }, null,
								null, new ItemStack[] { ItemUtils.getSimpleStack(aForestryFert, 3), aManureByprod, aManureByprod },
								new int[] { 10000, 2000, 2000 }, 20 * 20, 240);
					}
				}
			}
			catch (IllegalArgumentException | IllegalAccessException e) {
				
			}		
		}

		/**
		 * IC2 Support
		 */
		if (LoadedMods.IndustrialCraft2) {
			aFertIC2 = ItemUtils.getItemStackFromFQRN("IC2:itemFertilizer", 1);
			CORE.RA.addDehydratorRecipe(
					new ItemStack[] { CI.getNumberedCircuit(12), ItemUtils.getSimpleStack(aDustOrganicFert, 4) }, null,
					null, new ItemStack[] { ItemUtils.getItemStackFromFQRN("IC2:itemFertilizer", 3), aManureByprod,
							aManureByprod },
					new int[] { 10000, 2000, 2000 }, 20 * 20, 240);
		}

		// Dirt Production
		CORE.RA.addCompressorRecipe(ItemUtils.getSimpleStack(dustDirt, 9), ItemUtils.getSimpleStack(Blocks.dirt),
				20 * 2, 8);

		// Centrifuge Byproducts

		// Ammonium Nitrate, Phosphates, Calcium, Copper, Carbon
		GT_Values.RA.addCentrifugeRecipe(CI.getNumberedCircuit(20), ItemUtils.getSimpleStack(aManureByprod, 4),
				FluidUtils.getFluidStack("sulfuricacid", 250), // In Fluid
				FluidUtils.getFluidStack("sulfuricapatite", 50), // Out Fluid
				MaterialHelper.getDustSmall(Materials.Phosphorus, 2), MaterialHelper.getDustSmall(Materials.Calcium, 2),
				MaterialHelper.getDustTiny(Materials.Copper, 1), MaterialHelper.getDust(Materials.Carbon, 1), ItemUtils.getSimpleStack(dustDirt, 1),
				ItemUtils.getItemStackOfAmountFromOreDict("dustTinyAmmoniumNitrate", 1),
				new int[] { 2500, 2500, 750, 1000, 5000, 250 }, // Chances
				20 * 20, // Time
				60); // EU

		// Add Fuel Usages
		CORE.RA.addSemifluidFuel(FluidUtils.getFluidStack(PoopJuice, 1000), 12);
		CORE.RA.addSemifluidFuel(FluidUtils.getFluidStack(ManureSlurry, 1000), 24);
		CORE.RA.addSemifluidFuel(FluidUtils.getFluidStack(FertileManureSlurry, 1000), 32);

	}

	@Override
	public String errorMessage() {
		return "Failed to generate recipes for AgroChem.";
	}

	@Override
	public boolean generateRecipes() {
		if (mBloodFluids.isEmpty()) {
			Logger.INFO("Could not find, nor create Blood fluid. Unable to add recipes.");
			return false;
		}

		// Organise OreDict
		processAllOreDict();

		// Slurry Production
		addBasicSlurryRecipes();
		addAdvancedSlurryRecipes();

		// Organic Fert. Production
		addBasicOrganiseFertRecipes();
		addAdvancedOrganiseFertRecipes();

		addMiscRecipes();
		
		BioRecipes.init();
		
		return true;
	}
}
