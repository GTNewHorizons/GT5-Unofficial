package miscutil.core.xmod.gregtech.api.util;

import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.GT_Values.RES_PATH_GUI;
import static gregtech.api.enums.GT_Values.W;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.objects.GT_FluidStack;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.api.util.GT_Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import miscutil.core.util.Utils;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This File contains the functions used for Recipes. Please do not include this File AT ALL in your Moddownload as it ruins compatibility
 * This is just the Core of my Recipe System, if you just want to GET the Recipes I add, then you can access this File.
 * Do NOT add Recipes using the Constructors inside this Class, The GregTech_API File calls the correct Functions for these Constructors.
 * <p/>
 * I know this File causes some Errors, because of missing Main Functions, but if you just need to compile Stuff, then remove said erroreous Functions.
 */
public class GregtechRecipe {

	public static volatile int VERSION = 508;
	/**
	 * If you want to change the Output, feel free to modify or even replace the whole ItemStack Array, for Inputs, please add a new Recipe, because of the HashMaps.
	 */
	public ItemStack[] mInputs, mOutputs;
	/**
	 * If you want to change the Output, feel free to modify or even replace the whole ItemStack Array, for Inputs, please add a new Recipe, because of the HashMaps.
	 */
	public FluidStack[] mFluidInputs, mFluidOutputs;
	/**
	 * If you changed the amount of Array-Items inside the Output Array then the length of this Array must be larger or equal to the Output Array. A chance of 10000 equals 100%
	 */
	public int[] mChances;
	/**
	 * An Item that needs to be inside the Special Slot, like for example the Copy Slot inside the Printer. This is only useful for Fake Recipes in NEI, since findRecipe() and containsInput() don't give a shit about this Field. Lists are also possible.
	 */
	public Object mSpecialItems;
	public int mDuration, mEUt, mSpecialValue;
	/**
	 * Use this to just disable a specific Recipe, but the Configuration enables that already for every single Recipe.
	 */
	public boolean mEnabled = true;
	/**
	 * If this Recipe is hidden from NEI
	 */
	public boolean mHidden = false;
	/**
	 * If this Recipe is Fake and therefore doesn't get found by the findRecipe Function (It is still in the HashMaps, so that containsInput does return T on those fake Inputs)
	 */
	public boolean mFakeRecipe = false;
	/**
	 * If this Recipe can be stored inside a Machine in order to make Recipe searching more Efficient by trying the previously used Recipe first. In case you have a Recipe Map overriding things and returning one time use Recipes, you have to set this to F.
	 */
	public boolean mCanBeBuffered = true;
	/**
	 * If this Recipe needs the Output Slots to be completely empty. Needed in case you have randomised Outputs
	 */
	public boolean mNeedsEmptyOutput = false;
	private GregtechRecipe(GregtechRecipe aRecipe) {
		mInputs = GT_Utility.copyStackArray((Object[]) aRecipe.mInputs);
		mOutputs = GT_Utility.copyStackArray((Object[]) aRecipe.mOutputs);
		mSpecialItems = aRecipe.mSpecialItems;
		mChances = aRecipe.mChances;
		mFluidInputs = GT_Utility.copyFluidArray(aRecipe.mFluidInputs);
		mFluidOutputs = GT_Utility.copyFluidArray(aRecipe.mFluidOutputs);
		mDuration = aRecipe.mDuration;
		mSpecialValue = aRecipe.mSpecialValue;
		mEUt = aRecipe.mEUt;
		mNeedsEmptyOutput = aRecipe.mNeedsEmptyOutput;
		mCanBeBuffered = aRecipe.mCanBeBuffered;
		mFakeRecipe = aRecipe.mFakeRecipe;
		mEnabled = aRecipe.mEnabled;
		mHidden = aRecipe.mHidden;
	}
	protected GregtechRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecialItems, int[] aChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
		if (aInputs == null) aInputs = new ItemStack[0];
		if (aOutputs == null) aOutputs = new ItemStack[0];
		if (aFluidInputs == null) aFluidInputs = new FluidStack[0];
		if (aFluidOutputs == null) aFluidOutputs = new FluidStack[0];
		if (aChances == null) aChances = new int[aOutputs.length];
		if (aChances.length < aOutputs.length) aChances = Arrays.copyOf(aChances, aOutputs.length);

		aInputs = GT_Utility.getArrayListWithoutTrailingNulls(aInputs).toArray(new ItemStack[0]);
		aOutputs = GT_Utility.getArrayListWithoutTrailingNulls(aOutputs).toArray(new ItemStack[0]);
		aFluidInputs = GT_Utility.getArrayListWithoutNulls(aFluidInputs).toArray(new FluidStack[0]);
		aFluidOutputs = GT_Utility.getArrayListWithoutNulls(aFluidOutputs).toArray(new FluidStack[0]);

		GT_OreDictUnificator.setStackArray(true, aInputs);
		GT_OreDictUnificator.setStackArray(true, aOutputs);

		for (ItemStack tStack : aOutputs) GT_Utility.updateItemStack(tStack);

		for (int i = 0; i < aChances.length; i++) if (aChances[i] <= 0) aChances[i] = 10000;
		for (int i = 0; i < aFluidInputs.length; i++) aFluidInputs[i] = new GT_FluidStack(aFluidInputs[i]);
		for (int i = 0; i < aFluidOutputs.length; i++) aFluidOutputs[i] = new GT_FluidStack(aFluidOutputs[i]);

		for (int i = 0; i < aInputs.length; i++)
			if (aInputs[i] != null && Items.feather.getDamage(aInputs[i]) != W)
				for (int j = 0; j < aOutputs.length; j++) {
					if (GT_Utility.areStacksEqual(aInputs[i], aOutputs[j])) {
						if (aInputs[i].stackSize >= aOutputs[j].stackSize) {
							aInputs[i].stackSize -= aOutputs[j].stackSize;
							aOutputs[j] = null;
						} else {
							aOutputs[j].stackSize -= aInputs[i].stackSize;
						}
					}
				}

		if (aOptimize && aDuration >= 32) {
			ArrayList<ItemStack> tList = new ArrayList<ItemStack>();
			tList.addAll(Arrays.asList(aInputs));
			tList.addAll(Arrays.asList(aOutputs));
			for (int i = 0; i < tList.size(); i++) if (tList.get(i) == null) tList.remove(i--);

			for (byte i = (byte) Math.min(64, aDuration / 16); i > 1; i--)
				if (aDuration / i >= 16) {
					boolean temp = true;
					for (int j = 0, k = tList.size(); temp && j < k; j++)
						if (tList.get(j).stackSize % i != 0) temp = false;
					for (int j = 0; temp && j < aFluidInputs.length; j++)
						if (aFluidInputs[j].amount % i != 0) temp = false;
					for (int j = 0; temp && j < aFluidOutputs.length; j++)
						if (aFluidOutputs[j].amount % i != 0) temp = false;
					if (temp) {
						for (int j = 0, k = tList.size(); j < k; j++) tList.get(j).stackSize /= i;
						for (int j = 0; j < aFluidInputs.length; j++) aFluidInputs[j].amount /= i;
						for (int j = 0; j < aFluidOutputs.length; j++) aFluidOutputs[j].amount /= i;
						aDuration /= i;
					}
				}
		}

		mInputs = aInputs;
		mOutputs = aOutputs;
		mSpecialItems = aSpecialItems;
		mChances = aChances;
		mFluidInputs = aFluidInputs;
		mFluidOutputs = aFluidOutputs;
		mDuration = aDuration;
		mSpecialValue = aSpecialValue;
		mEUt = aEUt;

		//		checkCellBalance();
	}

	public GregtechRecipe(ItemStack aInput1, ItemStack aOutput1, int aFuelValue, int aType) {
		this(aInput1, aOutput1, null, null, null, aFuelValue, aType);
	}

	// aSpecialValue = EU per Liter! If there is no Liquid for this Object, then it gets multiplied with 1000!
	public GregtechRecipe(ItemStack aInput1, ItemStack aOutput1, ItemStack aOutput2, ItemStack aOutput3, ItemStack aOutput4, int aSpecialValue, int aType) {
		this(true, new ItemStack[]{aInput1}, new ItemStack[]{aOutput1, aOutput2, aOutput3, aOutput4}, null, null, null, null, 0, 0, Math.max(1, aSpecialValue));

		Utils.LOG_INFO("Switch case method for adding fuels");
		if (mInputs.length > 0 && aSpecialValue > 0) {
			switch (aType) {
			// Diesel Generator
			case 0:
				Utils.LOG_INFO("Added fuel "+aInput1.getDisplayName()+" is ROCKET FUEL - continuing");
				Gregtech_Recipe_Map.sRocketFuels.addRecipe(this);				
				break;
				// Gas Turbine
			case 1:
				Gregtech_Recipe_Map.sGeoThermalFuels.addRecipe(this);
				break;
				// Thermal Generator
			case 2:
				//Gregtech_Recipe_Map.sHotFuels.addRecipe(this);
				break;
				// Plasma Generator
			case 4:
				//Gregtech_Recipe_Map.sPlasmaFuels.addRecipe(this);
				break;
				// Magic Generator
			case 5:
				//Gregtech_Recipe_Map.sMagicFuels.addRecipe(this);
				break;
				// Fluid Generator. Usually 3. Every wrong Type ends up in the Semifluid Generator
			default:
				//Gregtech_Recipe_Map.sDenseLiquidFuels.addRecipe(this);
				break;
			}
		}
	}

	public static void reInit() {
		GT_Log.out.println("GT_Mod: Re-Unificating Recipes.");
		for (Gregtech_Recipe_Map tMapEntry : Gregtech_Recipe_Map.sMappings) tMapEntry.reInit();
	}
	
	public ItemStack getRepresentativeInput(int aIndex) {
        if (aIndex < 0 || aIndex >= mInputs.length) return null;
        return GT_Utility.copy(mInputs[aIndex]);
    }

    public ItemStack getOutput(int aIndex) {
        if (aIndex < 0 || aIndex >= mOutputs.length) return null;
        return GT_Utility.copy(mOutputs[aIndex]);
    }

    public int getOutputChance(int aIndex) {
        if (aIndex < 0 || aIndex >= mChances.length) return 10000;
        return mChances[aIndex];
    }

    public FluidStack getRepresentativeFluidInput(int aIndex) {
        if (aIndex < 0 || aIndex >= mFluidInputs.length || mFluidInputs[aIndex] == null) return null;
        return mFluidInputs[aIndex].copy();
    }

    public FluidStack getFluidOutput(int aIndex) {
        if (aIndex < 0 || aIndex >= mFluidOutputs.length || mFluidOutputs[aIndex] == null) return null;
        return mFluidOutputs[aIndex].copy();
    }

	public GregtechRecipe copy() {
		return new GregtechRecipe(this);
	}

	public boolean isRecipeInputEqual(boolean aDecreaseStacksizeBySuccess, FluidStack[] aFluidInputs, ItemStack... aInputs) {
		return isRecipeInputEqual(aDecreaseStacksizeBySuccess, false, aFluidInputs, aInputs);
	}

	public boolean isRecipeInputEqual(boolean aDecreaseStacksizeBySuccess, boolean aDontCheckStackSizes, FluidStack[] aFluidInputs, ItemStack... aInputs) {
		if (mFluidInputs.length > 0 && aFluidInputs == null) return false;
		for (FluidStack tFluid : mFluidInputs)
			if (tFluid != null) {
				boolean temp = true;
				for (FluidStack aFluid : aFluidInputs)
					if (aFluid != null && aFluid.isFluidEqual(tFluid) && (aDontCheckStackSizes || aFluid.amount >= tFluid.amount)) {
						temp = false;
						break;
					}
				if (temp) return false;
			}

		if (mInputs.length > 0 && aInputs == null) return false;

		for (ItemStack tStack : mInputs)
			if (tStack != null) {
				boolean temp = true;
				for (ItemStack aStack : aInputs)
					if ((GT_Utility.areUnificationsEqual(aStack, tStack, true) || GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true)) && (aDontCheckStackSizes || aStack.stackSize >= tStack.stackSize)) {
						temp = false;
						break;
					}
				if (temp) return false;
			}

		if (aDecreaseStacksizeBySuccess) {
			if (aFluidInputs != null) {
				for (FluidStack tFluid : mFluidInputs)
					if (tFluid != null) {
						for (FluidStack aFluid : aFluidInputs)
							if (aFluid != null && aFluid.isFluidEqual(tFluid) && (aDontCheckStackSizes || aFluid.amount >= tFluid.amount)) {
								aFluid.amount -= tFluid.amount;
								break;
							}
					}
			}

			if (aInputs != null) {
				for (ItemStack tStack : mInputs)
					if (tStack != null) {
						for (ItemStack aStack : aInputs)
							if ((GT_Utility.areUnificationsEqual(aStack, tStack, true) || GT_Utility.areUnificationsEqual(GT_OreDictUnificator.get(false, aStack), tStack, true)) && (aDontCheckStackSizes || aStack.stackSize >= tStack.stackSize)) {
								aStack.stackSize -= tStack.stackSize;
								break;
							}
					}
			}
		}

		return true;
	}

	public static class Gregtech_Recipe_Map {
		/**
		 * Contains all Recipe Maps
		 */
		public static final Collection<Gregtech_Recipe_Map> sMappings = new ArrayList<Gregtech_Recipe_Map>();
		//public static final GT_Recipe_Map sChemicalBathRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(200), "gt.recipe.chemicalbath", "Chemical Bath", null, RES_PATH_GUI + "basicmachines/ChemicalBath", 1, 3, 1, 1, 1, E, 1, E, true, true);
		public static final GT_Recipe_Map sCokeOvenRecipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(200), "gt.recipe.cokeoven", "Coke Oven", null, RES_PATH_GUI + "basicmachines/ChemicalBath", 2, 2, 1, 0, 1, E, 1, E, true, true);
		public static final GT_Recipe_Map sMatterFab2Recipes = new GT_Recipe_Map(new HashSet<GT_Recipe>(200), "gt.recipe.matterfab2", "Matter Fabricator", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, E, 1, E, true, true);
		//public static final Gregtech_Recipe_Map sMatterFabRecipes = new Gregtech_Recipe_Map(new HashSet<GregtechRecipe>(200), "gt.recipe.matterfab", "Matter Fabricator", null, RES_PATH_GUI + "basicmachines/Massfabricator", 1, 3, 1, 1, 1, E, 1, E, true, true);
		public static final Gregtech_Recipe_Map_Fuel sRocketFuels = new Gregtech_Recipe_Map_Fuel(new HashSet<GregtechRecipe>(10), "gt.recipe.rocketenginefuel", "Rocket Engine Fuel", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 3000, " EU", true, true);
		public static final Gregtech_Recipe_Map_Fuel sGeoThermalFuels = new Gregtech_Recipe_Map_Fuel(new HashSet<GregtechRecipe>(10), "gt.recipe.geothermalfuel", "GeoThermal Fuel", null, RES_PATH_GUI + "basicmachines/Default", 1, 1, 0, 0, 1, "Fuel Value: ", 1000, " EU", true, true);

		/**
		 * HashMap of Recipes based on their Items
		 */
		public final Map<GT_ItemStack, Collection<GregtechRecipe>> mRecipeItemMap = new HashMap<GT_ItemStack, Collection<GregtechRecipe>>();
		/**
		 * HashMap of Recipes based on their Fluids
		 */
		public final Map<Fluid, Collection<GregtechRecipe>> mRecipeFluidMap = new HashMap<Fluid, Collection<GregtechRecipe>>();
		/**
		 * The List of all Recipes
		 */
		public final Collection<GregtechRecipe> mRecipeList;
		/**
		 * String used as an unlocalised Name.
		 */
		public final String mUnlocalizedName;
		/**
		 * String used in NEI for the Recipe Lists. If null it will use the unlocalised Name instead
		 */
		public final String mNEIName;
		/**
		 * GUI used for NEI Display. Usually the GUI of the Machine itself
		 */
		public final String mNEIGUIPath;
		public final String mNEISpecialValuePre, mNEISpecialValuePost;
		public final int mUsualInputCount, mUsualOutputCount, mNEISpecialValueMultiplier, mMinimalInputItems, mMinimalInputFluids, mAmperage;
		public final boolean mNEIAllowed, mShowVoltageAmperageInNEI;

		/**
		 * Initialises a new type of Recipe Handler.
		 *
		 * @param aRecipeList                a List you specify as Recipe List. Usually just an ArrayList with a pre-initialised Size.
		 * @param aUnlocalizedName           the unlocalised Name of this Recipe Handler, used mainly for NEI.
		 * @param aLocalName                 the displayed Name inside the NEI Recipe GUI.
		 * @param aNEIGUIPath                the displayed GUI Texture, usually just a Machine GUI. Auto-Attaches ".png" if forgotten.
		 * @param aUsualInputCount           the usual amount of Input Slots this Recipe Class has.
		 * @param aUsualOutputCount          the usual amount of Output Slots this Recipe Class has.
		 * @param aNEISpecialValuePre        the String in front of the Special Value in NEI.
		 * @param aNEISpecialValueMultiplier the Value the Special Value is getting Multiplied with before displaying
		 * @param aNEISpecialValuePost       the String after the Special Value. Usually for a Unit or something.
		 * @param aNEIAllowed                if NEI is allowed to display this Recipe Handler in general.
		 */
		public Gregtech_Recipe_Map(Collection<GregtechRecipe> aRecipeList,
				String aUnlocalizedName, String aLocalName, String aNEIName,
				String aNEIGUIPath, int aUsualInputCount,
				int aUsualOutputCount, int aMinimalInputItems,
				int aMinimalInputFluids, int aAmperage,
				String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
				String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
				boolean aNEIAllowed) {
			sMappings.add(this);
			mNEIAllowed = aNEIAllowed;
			mShowVoltageAmperageInNEI = aShowVoltageAmperageInNEI;
			mRecipeList = aRecipeList;
			mNEIName = aNEIName == null ? aUnlocalizedName : aNEIName;
			mNEIGUIPath = aNEIGUIPath.endsWith(".png") ? aNEIGUIPath : aNEIGUIPath + ".png";
			mNEISpecialValuePre = aNEISpecialValuePre;
			mNEISpecialValueMultiplier = aNEISpecialValueMultiplier;
			mNEISpecialValuePost = aNEISpecialValuePost;
			mAmperage = aAmperage;
			mUsualInputCount = aUsualInputCount;
			mUsualOutputCount = aUsualOutputCount;
			mMinimalInputItems = aMinimalInputItems;
			mMinimalInputFluids = aMinimalInputFluids;
			GregTech_API.sFluidMappings.add(mRecipeFluidMap);
			GregTech_API.sItemStackMappings.add(mRecipeItemMap);
			GT_LanguageManager.addStringLocalization(mUnlocalizedName = aUnlocalizedName, aLocalName);
		}

		public GregtechRecipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return addRecipe(new GregtechRecipe(aOptimize, aInputs, aOutputs, aSpecial, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		public GregtechRecipe addRecipe(int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return addRecipe(new GregtechRecipe(false, null, null, null, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue), false, false, false);
		}

		public GregtechRecipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return addRecipe(new GregtechRecipe(aOptimize, aInputs, aOutputs, aSpecial, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		public GregtechRecipe addRecipe(boolean aOptimize, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return addRecipe(new GregtechRecipe(aOptimize, null, null, null, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		/*public GregtechRecipe addRecipe(boolean aOptimize, FluidStack aInput1, FluidStack aOutput1, ItemStack[] bInput1, ItemStack[] bOutput1, int aDuration, int aEUt, int aSpecialValue) {
			 return addRecipe(new GregtechRecipe(aOptimize, aInput1, aOutput1, bInput1,bOutput1, aDuration, aEUt, aSpecialValue));

			}*/

		public GregtechRecipe addRecipe(GregtechRecipe aRecipe) {
			Utils.LOG_INFO("Adding Recipe Method 1");
			return addRecipe(aRecipe, true, false, false);
		}

		protected GregtechRecipe addRecipe(GregtechRecipe aRecipe, boolean aCheckForCollisions, boolean aFakeRecipe, boolean aHidden) {
			Utils.LOG_INFO("Adding Recipe Method 2");
			aRecipe.mHidden = aHidden;
			aRecipe.mFakeRecipe = aFakeRecipe;
			if (aRecipe.mFluidInputs.length < mMinimalInputFluids && aRecipe.mInputs.length < mMinimalInputItems){
				Utils.LOG_INFO("Step 2 failed");
				return null;}
			if (aCheckForCollisions && findRecipe(null, false, Long.MAX_VALUE, aRecipe.mFluidInputs, aRecipe.mInputs) != null){
				Utils.LOG_INFO("Step 2 failed - 2");
				return null;
			}			
			return add(aRecipe);
		}
		
		

		/**
		 * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes! findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
		 */
		public GregtechRecipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return addFakeRecipe(aCheckForCollisions, new GregtechRecipe(false, aInputs, aOutputs, aSpecial, aOutputChances, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		/**
		 * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes! findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
		 */
		public GregtechRecipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return addFakeRecipe(aCheckForCollisions, new GregtechRecipe(false, aInputs, aOutputs, aSpecial, null, aFluidInputs, aFluidOutputs, aDuration, aEUt, aSpecialValue));
		}

		/**
		 * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes! findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
		 */
		public GregtechRecipe addFakeRecipe(boolean aCheckForCollisions, GregtechRecipe aRecipe) {
			return addRecipe(aRecipe, aCheckForCollisions, true, false);
		}

		public GregtechRecipe add(GregtechRecipe aRecipe) {
			Utils.LOG_INFO("Adding Recipe Method 3");
			mRecipeList.add(aRecipe);
			for (FluidStack aFluid : aRecipe.mFluidInputs)
				if (aFluid != null) {
					Utils.LOG_INFO("Fluid is valid - getting some kind of fluid instance to add to the recipe hashmap.");
					Collection<GregtechRecipe> tList = mRecipeFluidMap.get(aFluid.getFluid());
					if (tList == null) mRecipeFluidMap.put(aFluid.getFluid(), tList = new HashSet<GregtechRecipe>(1));
					tList.add(aRecipe);
				}
			return addToItemMap(aRecipe);
		}

		public void reInit() {
			Map<GT_ItemStack, Collection<GregtechRecipe>> tMap = mRecipeItemMap;
			if (tMap != null) tMap.clear();
			for (GregtechRecipe tRecipe : mRecipeList) {
				GT_OreDictUnificator.setStackArray(true, tRecipe.mInputs);
				GT_OreDictUnificator.setStackArray(true, tRecipe.mOutputs);
				if (tMap != null) addToItemMap(tRecipe);
			}
		}

		/**
		 * @return if this Item is a valid Input for any for the Recipes
		 */
		public boolean containsInput(ItemStack aStack) {
			return aStack != null && (mRecipeItemMap.containsKey(new GT_ItemStack(aStack)) || mRecipeItemMap.containsKey(new GT_ItemStack(GT_Utility.copyMetaData(W, aStack))));
		}

		/**
		 * @return if this Fluid is a valid Input for any for the Recipes
		 */
		public boolean containsInput(FluidStack aFluid) {
			return aFluid != null && containsInput(aFluid.getFluid());
		}

		/**
		 * @return if this Fluid is a valid Input for any for the Recipes
		 */
		public boolean containsInput(Fluid aFluid) {
			return aFluid != null && mRecipeFluidMap.containsKey(aFluid);
		}

		public GregtechRecipe findRecipe(IHasWorldObjectAndCoords aTileEntity, boolean aNotUnificated, long aVoltage, FluidStack[] aFluids, ItemStack... aInputs) {
			return findRecipe(aTileEntity, null, aNotUnificated, aVoltage, aFluids, null, aInputs);
		}

		public GregtechRecipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GregtechRecipe aRecipe, boolean aNotUnificated, long aVoltage, FluidStack[] aFluids, ItemStack... aInputs) {
			return findRecipe(aTileEntity, aRecipe, aNotUnificated, aVoltage, aFluids, null, aInputs);
		}

		/**
		 * finds a Recipe matching the aFluid and ItemStack Inputs.
		 *
		 * @param aTileEntity    an Object representing the current coordinates of the executing Block/Entity/Whatever. This may be null, especially during Startup.
		 * @param aRecipe        in case this is != null it will try to use this Recipe first when looking things up.
		 * @param aNotUnificated if this is T the Recipe searcher will unificate the ItemStack Inputs
		 * @param aVoltage       Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
		 * @param aFluids        the Fluid Inputs
		 * @param aSpecialSlot   the content of the Special Slot, the regular Manager doesn't do anything with this, but some custom ones do.
		 * @param aInputs        the Item Inputs
		 * @return the Recipe it has found or null for no matching Recipe
		 */
		public GregtechRecipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GregtechRecipe aRecipe, boolean aNotUnificated, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot, ItemStack... aInputs) {
			// No Recipes? Well, nothing to be found then.
			if (mRecipeList.isEmpty()) return null;

			// Some Recipe Classes require a certain amount of Inputs of certain kinds. Like "at least 1 Fluid + 1 Stack" or "at least 2 Stacks" before they start searching for Recipes.
			// This improves Performance massively, especially if people leave things like Circuits, Molds or Shapes in their Machines to select Sub Recipes.
			if (GregTech_API.sPostloadFinished) {
				if (mMinimalInputFluids > 0) {
					if (aFluids == null) return null;
					int tAmount = 0;
					for (FluidStack aFluid : aFluids) if (aFluid != null) tAmount++;
					if (tAmount < mMinimalInputFluids) return null;
				}
				if (mMinimalInputItems > 0) {
					if (aInputs == null) return null;
					int tAmount = 0;
					for (ItemStack aInput : aInputs) if (aInput != null) tAmount++;
					if (tAmount < mMinimalInputItems) return null;
				}
			}

			// Unification happens here in case the Input isn't already unificated.
			if (aNotUnificated) aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);

			// Check the Recipe which has been used last time in order to not have to search for it again, if possible.
			if (aRecipe != null)
				if (!aRecipe.mFakeRecipe && aRecipe.mCanBeBuffered && aRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
					return aRecipe.mEnabled && aVoltage * mAmperage >= aRecipe.mEUt ? aRecipe : null;

					// Now look for the Recipes inside the Item HashMaps, but only when the Recipes usually have Items.
					if (mUsualInputCount > 0 && aInputs != null) for (ItemStack tStack : aInputs)
						if (tStack != null) {
							Collection<GregtechRecipe>
							tRecipes = mRecipeItemMap.get(new GT_ItemStack(tStack));
							if (tRecipes != null) for (GregtechRecipe tRecipe : tRecipes)
								if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
									return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt ? tRecipe : null;
									tRecipes = mRecipeItemMap.get(new GT_ItemStack(GT_Utility.copyMetaData(W, tStack)));
									if (tRecipes != null) for (GregtechRecipe tRecipe : tRecipes)
										if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
											return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt ? tRecipe : null;
						}

					// If the minimal Amount of Items for the Recipe is 0, then it could be a Fluid-Only Recipe, so check that Map too.
					if (mMinimalInputItems == 0 && aFluids != null) for (FluidStack aFluid : aFluids)
						if (aFluid != null) {
							Collection<GregtechRecipe>
							tRecipes = mRecipeFluidMap.get(aFluid.getFluid());
							if (tRecipes != null) for (GregtechRecipe tRecipe : tRecipes)
								if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
									return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt ? tRecipe : null;
						}

					// And nothing has been found.
					return null;
		}

		protected GregtechRecipe addToItemMap(GregtechRecipe aRecipe) {
			Utils.LOG_INFO("Adding Recipe Method 4");
			for (ItemStack aStack : aRecipe.mInputs)
				if (aStack != null) {
					Utils.LOG_INFO("Method 4 - Manipulating "+aStack.getDisplayName());
					GT_ItemStack tStack = new GT_ItemStack(aStack);
					Utils.LOG_INFO("Method 4 - Made gt stack of item "+tStack.toStack().getDisplayName());
					Collection<GregtechRecipe> tList = mRecipeItemMap.get(tStack);
					if (tList != null){
						Utils.LOG_INFO("Method 4 - Gt Recipe Hashmap: "+tList.toString());
					}					
					if (tList == null){ 
						Utils.LOG_INFO("Method 4 - brrr list was NUll");	
						mRecipeItemMap.put(tStack, tList = new HashSet<GregtechRecipe>(1));
					}
					tList.add(aRecipe);
					Utils.LOG_INFO("Method 4 - Added recipe to map? I think.");					
				}
			return aRecipe;
		}

		public GregtechRecipe findRecipe(IGregTechTileEntity baseMetaTileEntity, GregtechRecipe aRecipe, boolean aNotUnificated,
				long aVoltage, FluidStack[] aFluids, FluidStack[] fluidStacks) {

			ItemStack aInputs[] = null;
			// No Recipes? Well, nothing to be found then.
			if (mRecipeList.isEmpty()) return null;

			// Some Recipe Classes require a certain amount of Inputs of certain kinds. Like "at least 1 Fluid + 1 Stack" or "at least 2 Stacks" before they start searching for Recipes.
			// This improves Performance massively, especially if people leave things like Circuits, Molds or Shapes in their Machines to select Sub Recipes.
			if (GregTech_API.sPostloadFinished) {
				if (mMinimalInputFluids > 0) {
					if (aFluids == null) return null;
					int tAmount = 0;
					for (FluidStack aFluid : aFluids) if (aFluid != null) tAmount++;
					if (tAmount < mMinimalInputFluids) return null;
				}
				if (mMinimalInputItems > 0) {
					if (aInputs == null) return null;
					int tAmount = 0;
					for (ItemStack aInput : aInputs) if (aInput != null) tAmount++;
					if (tAmount < mMinimalInputItems) return null;
				}
			}

			// Unification happens here in case the Input isn't already unificated.
			if (aNotUnificated) aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);

			// Check the Recipe which has been used last time in order to not have to search for it again, if possible.
			if (aRecipe != null)
				if (!aRecipe.mFakeRecipe && aRecipe.mCanBeBuffered && aRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
					return aRecipe.mEnabled && aVoltage * mAmperage >= aRecipe.mEUt ? aRecipe : null;

					// Now look for the Recipes inside the Item HashMaps, but only when the Recipes usually have Items.
					if (mUsualInputCount > 0 && aInputs != null) for (ItemStack tStack : aInputs)
						if (tStack != null) {
							Collection<GregtechRecipe>
							tRecipes = mRecipeItemMap.get(new GT_ItemStack(tStack));
							if (tRecipes != null) for (GregtechRecipe tRecipe : tRecipes)
								if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
									return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt ? tRecipe : null;
									tRecipes = mRecipeItemMap.get(new GT_ItemStack(GT_Utility.copyMetaData(W, tStack)));
									if (tRecipes != null) for (GregtechRecipe tRecipe : tRecipes)
										if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
											return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt ? tRecipe : null;
						}

					// If the minimal Amount of Items for the Recipe is 0, then it could be a Fluid-Only Recipe, so check that Map too.
					if (mMinimalInputItems == 0 && aFluids != null) for (FluidStack aFluid : aFluids)
						if (aFluid != null) {
							Collection<GregtechRecipe>
							tRecipes = mRecipeFluidMap.get(aFluid.getFluid());
							if (tRecipes != null) for (GregtechRecipe tRecipe : tRecipes)
								if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, true, aFluids, aInputs))
									return tRecipe.mEnabled && aVoltage * mAmperage >= tRecipe.mEUt ? tRecipe : null;
						}

					// And nothing has been found.
					return null;
		}
	}

	// -----------------------------------------------------------------------------------------------------------------
	// Here are a few Classes I use for Special Cases in some Machines without having to write a separate Machine Class.
	// -----------------------------------------------------------------------------------------------------------------

	/**
	 * Abstract Class for general Recipe Handling of non GT Recipes
	 */
	public static abstract class GT_Recipe_Map_NonGTRecipes extends Gregtech_Recipe_Map {
		public GT_Recipe_Map_NonGTRecipes(Collection<GregtechRecipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
			super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
		}

		@Override
		public boolean containsInput(ItemStack aStack) {
			return false;
		}

		@Override
		public boolean containsInput(FluidStack aFluid) {
			return false;
		}

		@Override
		public boolean containsInput(Fluid aFluid) {
			return false;
		}

		@Override
		public GregtechRecipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return null;
		}

		@Override
		public GregtechRecipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return null;
		}

		@Override
		public GregtechRecipe addRecipe(GregtechRecipe aRecipe) {
			return null;
		}

		@Override
		public GregtechRecipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return null;
		}

		@Override
		public GregtechRecipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
			return null;
		}

		@Override
		public GregtechRecipe addFakeRecipe(boolean aCheckForCollisions, GregtechRecipe aRecipe) {
			return null;
		}

		@Override
		public GregtechRecipe add(GregtechRecipe aRecipe) {
			return null;
		}

		@Override
		public void reInit() {/**/}

		@Override
		protected GregtechRecipe addToItemMap(GregtechRecipe aRecipe) {
			return null;
		}		
	}

	/**
	 * Just a Recipe Map with Utility specifically for Fuels.
	 */
	public static class Gregtech_Recipe_Map_Fuel extends Gregtech_Recipe_Map {
		public Gregtech_Recipe_Map_Fuel(Collection<GregtechRecipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
			super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
		}

		public GregtechRecipe addFuel(ItemStack aInput, ItemStack aOutput, int aFuelValueInEU) {
			Utils.LOG_INFO("Adding Fuel using method 1");
			return addFuel(aInput, aOutput, null, null, 10000, aFuelValueInEU);
		}

		public GregtechRecipe addFuel(ItemStack aInput, ItemStack aOutput, int aChance, int aFuelValueInEU) {
			Utils.LOG_INFO("Adding Fuel using method 2");
			return addFuel(aInput, aOutput, null, null, aChance, aFuelValueInEU);
		}

		public GregtechRecipe addFuel(FluidStack aFluidInput, FluidStack aFluidOutput, int aFuelValueInEU) {
			Utils.LOG_INFO("Adding Fuel using method 3");
			return addFuel(null, null, aFluidInput, aFluidOutput, 10000, aFuelValueInEU);
		}

		public GregtechRecipe addFuel(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput, FluidStack aFluidOutput, int aFuelValueInEU) {
			Utils.LOG_INFO("Adding Fuel using method 4");
			return addFuel(aInput, aOutput, aFluidInput, aFluidOutput, 10000, aFuelValueInEU);
		}

		public GregtechRecipe addFuel(ItemStack aInput, ItemStack aOutput, FluidStack aFluidInput, FluidStack aFluidOutput, int aChance, int aFuelValueInEU) {
			Utils.LOG_INFO("Adding Fuel using method 5");
			return addRecipe(true, new ItemStack[]{aInput}, new ItemStack[]{aOutput}, null, new int[]{aChance}, new FluidStack[]{aFluidInput}, new FluidStack[]{aFluidOutput}, 0, 0, aFuelValueInEU);
		}
	}

}