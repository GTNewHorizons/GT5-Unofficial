package gtPlusPlus.plugin.agrichem;

import java.util.List;

import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.plugin.agrichem.fluids.FluidLoader;
import gtPlusPlus.plugin.agrichem.item.algae.ItemAgrichemBase;
import gtPlusPlus.plugin.agrichem.item.algae.ItemAlgaeBase;
import gtPlusPlus.plugin.manager.Core_Manager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Core_Agrichem implements IPlugin {

	final static Core_Agrichem mInstance;
	
	public static Item mAlgae;
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
	

	static {
		mInstance = new Core_Agrichem();
		Core_Manager.registerPlugin(mInstance);
		mInstance.log("Preparing "+mInstance.getPluginName()+" for use.");
	}

	@Override
	public boolean preInit() {		
		FluidLoader.generate();
		mAlgae = new ItemAlgaeBase();
		mAgrichemItem1 = new ItemAgrichemBase();
		return true;
	}

	@Override
	public boolean init() {
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
		if (mSodiumHydroxide.getItem() instanceof ItemAgrichemBase) {
			ItemUtils.addItemToOreDictionary(mSodiumHydroxide, "dustSodiumHydroxide");			
		}
		ItemUtils.addItemToOreDictionary(mSodiumCarbonate, "dustSodiumCarbonate");
		ItemUtils.addItemToOreDictionary(mLithiumChloride, "dustLithiumChloride");
		
		return true;
	}

	@Override
	public boolean postInit() {
		return true;
	}

	@Override
	public boolean serverStart() {
		return true;
	}

	@Override
	public boolean serverStop() {
		return true;
	}

	@Override
	public String getPluginName() {
		return "GT++ Agrichemistry Module";
	}

	@Override
	public String getPluginAbbreviation() {
		return "FARM";
	}

}
