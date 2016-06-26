package miscutil.gregtech.api.enums;

import static gregtech.api.enums.GT_Values.D2;
import static gregtech.api.enums.GT_Values.M;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Element;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.ICondition;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.GT_FluidStack;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import miscutil.core.lib.CORE;
import miscutil.gregtech.api.interfaces.internal.Interface_OreRecipeRegistrator;
import miscutil.gregtech.api.objects.GregtechItemData;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public enum GregtechOrePrefixes {
	/* Electric Components.
	 *
	 * usual Materials for this are:
	 * Primitive (Tier 1)
	 * Basic (Tier 2) as used by UE as well : IC2 Circuit and RE-Battery
	 * Good (Tier 3)
	 * Advanced (Tier 4) as used by UE as well : Advanced Circuit, Advanced Battery and Lithium Battery
	 * Data (Tier 5) : Data Storage Circuit
	 * Elite (Tier 6) as used by UE as well : Energy Crystal and Data Control Circuit
	 * Master (Tier 7) : Energy Flow Circuit and Lapotron Crystal
	 * Ultimate (Tier 8) : Data Orb and Lapotronic Energy Orb
	 * Infinite (Cheaty)
	 */
	batterySingleuse("Single Use Batteries", "", "", false, true, false, false, false, false, false, false, false, false, 0, -1, 64, -1),
	battery("Reusable Batteries", "", "", false, true, false, false, false, false, false, false, false, false, 0, -1, 64, -1), // Introduced by Calclavia
	circuit("Circuits", "", "", true, true, false, false, false, false, false, false, false, false, 0, -1, 64, -1), // Introduced by Calclavia
	chipset("Chipsets", "", "", true, true, false, false, false, false, false, false, false, false, 0, -1, 64, -1), // Introduced by Buildcraft
	computer("Computers", "", "", true, true, false, false, true, false, false, false, false, false, 0, -1, 64, -1); // A whole Computer. "computerMaster" = ComputerCube

	//public static volatile int VERSION = 508;

	public final ArrayList<ItemStack> mPrefixedItems = new ArrayList<ItemStack>();
	public final short mTextureIndex;
	public final String mRegularLocalName, mLocalizedMaterialPre, mLocalizedMaterialPost;
	public final boolean mIsUsedForOreProcessing, mIsEnchantable, mIsUnificatable, mIsMaterialBased, mIsSelfReferencing, mIsContainer, mDontUnificateActively, mIsUsedForBlocks, mAllowNormalRecycling, mGenerateDefaultItem;
	public final List<TC_AspectStack> mAspects = new ArrayList<TC_AspectStack>();
	public final Collection<GregtechOrePrefixes> mFamiliarPrefixes = new HashSet<GregtechOrePrefixes>();
	/**
	 * Used to determine the amount of Material this Prefix contains.
	 * Multiply or Divide GregTech_API.MATERIAL_UNIT to get the Amounts in comparision to one Ingot.
	 * 0 = Null
	 * Negative = Undefined Amount
	 */
	public final long mMaterialAmount;
	private final Collection<Materials> mNotGeneratedItems = new HashSet<Materials>(), mIgnoredMaterials = new HashSet<Materials>(), mGeneratedItems = new HashSet<Materials>();
	private final ArrayList<Interface_OreRecipeRegistrator> mOreProcessing = new ArrayList<Interface_OreRecipeRegistrator>();
	public ItemStack mContainerItem = null;
	public ICondition<ISubTagContainer> mCondition = null;
	public byte mDefaultStackSize = 64;
	public MaterialStack mSecondaryMaterial = null;
	public GregtechOrePrefixes mPrefixInto = this;
	public float mHeatDamage = 0.0F; // Negative for Frost Damage
	/**
	 * Yes this Value can be changed to add Bits for the MetaGenerated-Item-Check.
	 */
	public int mMaterialGenerationBits = 0;
	private GregtechOrePrefixes(String aRegularLocalName, String aLocalizedMaterialPre, String aLocalizedMaterialPost, boolean aIsUnificatable, boolean aIsMaterialBased, boolean aIsSelfReferencing, boolean aIsContainer, boolean aDontUnificateActively, boolean aIsUsedForBlocks, boolean aAllowNormalRecycling, boolean aGenerateDefaultItem, boolean aIsEnchantable, boolean aIsUsedForOreProcessing, int aMaterialGenerationBits, long aMaterialAmount, int aDefaultStackSize, int aTextureindex) {
		mIsUnificatable = aIsUnificatable;
		mIsMaterialBased = aIsMaterialBased;
		mIsSelfReferencing = aIsSelfReferencing;
		mIsContainer = aIsContainer;
		mDontUnificateActively = aDontUnificateActively;
		mIsUsedForBlocks = aIsUsedForBlocks;
		mAllowNormalRecycling = aAllowNormalRecycling;
		mGenerateDefaultItem = aGenerateDefaultItem;
		mIsEnchantable = aIsEnchantable;
		mIsUsedForOreProcessing = aIsUsedForOreProcessing;
		mMaterialGenerationBits = aMaterialGenerationBits;
		mMaterialAmount = aMaterialAmount;
		mRegularLocalName = aRegularLocalName;
		mLocalizedMaterialPre = aLocalizedMaterialPre;
		mLocalizedMaterialPost = aLocalizedMaterialPost;
		mDefaultStackSize = (byte) aDefaultStackSize;
		mTextureIndex = (short) aTextureindex;

		if (name().startsWith("ore")) {
			new TC_AspectStack(TC_Aspects.TERRA, 1).addToAspectList(mAspects);
		} else if (name().startsWith("wire") || name().startsWith("cable")) {
			new TC_AspectStack(TC_Aspects.ELECTRUM, 1).addToAspectList(mAspects);
		} else if (name().startsWith("dust")) {
			new TC_AspectStack(TC_Aspects.PERDITIO, 1).addToAspectList(mAspects);
		} else if (name().startsWith("crushed")) {
			new TC_AspectStack(TC_Aspects.PERFODIO, 1).addToAspectList(mAspects);
		} else if (name().startsWith("ingot") || name().startsWith("nugget")) {
			new TC_AspectStack(TC_Aspects.METALLUM, 1).addToAspectList(mAspects);
		} else if (name().startsWith("armor")) {
			new TC_AspectStack(TC_Aspects.TUTAMEN, 1).addToAspectList(mAspects);
		} else if (name().startsWith("stone")) {
			new TC_AspectStack(TC_Aspects.TERRA, 1).addToAspectList(mAspects);
		} else if (name().startsWith("pipe")) {
			new TC_AspectStack(TC_Aspects.ITER, 1).addToAspectList(mAspects);
		} else if (name().startsWith("gear")) {
			new TC_AspectStack(TC_Aspects.MOTUS, 1).addToAspectList(mAspects);
			new TC_AspectStack(TC_Aspects.MACHINA, 1).addToAspectList(mAspects);
		} else if (name().startsWith("frame") || name().startsWith("plate")) {
			new TC_AspectStack(TC_Aspects.FABRICO, 1).addToAspectList(mAspects);
		} else if (name().startsWith("tool")) {
			new TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2).addToAspectList(mAspects);
		} else if (name().startsWith("gem") || name().startsWith("crystal") || name().startsWith("lens")) {
			new TC_AspectStack(TC_Aspects.VITREUS, 1).addToAspectList(mAspects);
		} else if (name().startsWith("crate")) {
			new TC_AspectStack(TC_Aspects.ITER, 2).addToAspectList(mAspects);
		} else if (name().startsWith("circuit")) {
			new TC_AspectStack(TC_Aspects.COGNITIO, 1).addToAspectList(mAspects);
		} else if (name().startsWith("computer")) {
			new TC_AspectStack(TC_Aspects.COGNITIO, 4).addToAspectList(mAspects);
		} else if (name().startsWith("battery")) {
			new TC_AspectStack(TC_Aspects.ELECTRUM, 1).addToAspectList(mAspects);
		}
	}

	public static GregtechOrePrefixes getOrePrefix(String aOre) {
		for (GregtechOrePrefixes tPrefix : values())
			if (aOre.startsWith(tPrefix.toString())) {                
				return tPrefix;
			}
		return null;
	}

	public static String stripPrefix(String aOre) {
		for (GregtechOrePrefixes tPrefix : values()) {
			if (aOre.startsWith(tPrefix.toString())) {
				return aOre.replaceFirst(tPrefix.toString(), "");
			}
		}
		return aOre;
	}

	public static String replacePrefix(String aOre, GregtechOrePrefixes aPrefix) {
		for (GregtechOrePrefixes tPrefix : values()) {
			if (aOre.startsWith(tPrefix.toString())) {
				return aOre.replaceFirst(tPrefix.toString(), aPrefix.toString());
			}
		}
		return "";
	}

	public static GregtechOrePrefixes getPrefix(String aPrefixName) {
		return getPrefix(aPrefixName, null);
	}

	public static GregtechOrePrefixes getPrefix(String aPrefixName, GregtechOrePrefixes aReplacement) {
		Object tObject = GT_Utility.getFieldContent(GregtechOrePrefixes.class, aPrefixName, false, false);
		if (tObject != null && tObject instanceof GregtechOrePrefixes) return (GregtechOrePrefixes) tObject;
		return aReplacement;
	}

	public static Materials getMaterial(String aOre) {
		return Materials.get(stripPrefix(aOre));
	}

	public static Materials getMaterial(String aOre, GregtechOrePrefixes aPrefix) {
		return Materials.get(aOre.replaceFirst(aPrefix.toString(), ""));
	}

	public static Materials getRealMaterial(String aOre, GregtechOrePrefixes aPrefix) {
		return Materials.getRealMaterial(aOre.replaceFirst(aPrefix.toString(), ""));
	}

	public static boolean isInstanceOf(String aName, GregtechOrePrefixes aPrefix) {
		return aName == null ? false : aName.startsWith(aPrefix.toString());
	}

	public boolean add(ItemStack aStack) {
		if (aStack == null) return false;
		if (!contains(aStack)) mPrefixedItems.add(aStack);
		while (mPrefixedItems.contains(null)) mPrefixedItems.remove(null);
		return true;
	}

	public boolean contains(ItemStack aStack) {
		if (aStack == null) return false;
		for (ItemStack tStack : mPrefixedItems)
			if (GT_Utility.areStacksEqual(aStack, tStack, !tStack.hasTagCompound())) return true;
		return false;
	}

	public boolean doGenerateItem(Materials aMaterial) {
		return aMaterial != null && aMaterial != Materials._NULL && ((aMaterial.mTypes & mMaterialGenerationBits) != 0 || mGeneratedItems.contains(aMaterial)) && !mNotGeneratedItems.contains(aMaterial) && (mCondition == null || mCondition.isTrue(aMaterial));
	}

	public boolean ignoreMaterials(Materials... aMaterials) {
		for (Materials tMaterial : aMaterials) if (tMaterial != null) mIgnoredMaterials.add(tMaterial);
		return true;
	}

	public boolean isIgnored(Materials aMaterial) {
		if (aMaterial != null && (!aMaterial.mUnificatable || aMaterial != aMaterial.mMaterialInto)) return true;
		return mIgnoredMaterials.contains(aMaterial);
	}

	public boolean addFamiliarPrefix(GregtechOrePrefixes aPrefix) {
		if (aPrefix == null || mFamiliarPrefixes.contains(aPrefix) || aPrefix == this) return false;
		return mFamiliarPrefixes.add(aPrefix);
	}

	public boolean add(Interface_OreRecipeRegistrator aRegistrator) {
		if (aRegistrator == null) return false;
		return mOreProcessing.add(aRegistrator);
	}

	public void processOre(Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
		if (aMaterial != null && (aMaterial != Materials._NULL || mIsSelfReferencing || !mIsMaterialBased) && GT_Utility.isStackValid(aStack))
			for (Interface_OreRecipeRegistrator tRegistrator : mOreProcessing) {
				if (D2)
					GT_Log.ore.println("Processing '" + aOreDictName + "' with the Prefix '" + name() + "' and the Material '" + aMaterial.name() + "' at " + GT_Utility.getClassName(tRegistrator));
				tRegistrator.registerOre(this, aMaterial, aOreDictName, aModName, GT_Utility.copyAmount(1, aStack));
			}
	}

	public Object get(Object aMaterial) {
		if (aMaterial instanceof Materials) return new GregtechItemData(this, (Materials) aMaterial);
		return name() + aMaterial;
	}

	@SuppressWarnings("incomplete-switch")
	public String getDefaultLocalNameForItem(Materials aMaterial) {


		// Use Standard Localization
		return mLocalizedMaterialPre + aMaterial.mDefaultLocalName + mLocalizedMaterialPost;
	}

	public enum GT_Materials implements IColorModulationContainer, ISubTagContainer {



		/**
		 * This is the Default Material returned in case no Material has been found or a NullPointer has been inserted at a location where it shouldn't happen.
		 * <p/>
		 * Mainly for preventing NullPointer Exceptions and providing Default Values.
    	 *
		 * Unknown Material Components. Dead End Section.
		 * 
		 * Alkalus Range 730-799 & 970-998
		 * (aMetaItemSubID, TextureSet, aToolSpeed, aToolDurability, aToolQuality, aTypes, R, G, B, Alpha, aLocalName, 
		 * aFuelType, aFuelPower, aMeltingPoint, aBlastFurnaceTemp, aBlastFurnaceRequired, aTransparent, aOreValue, aDensityMultiplier, aDensityDivider, aColor
         *	this(aMetaItemSubID, aIconSet, aToolSpeed, aToolDurability, aToolQuality, true);
		 *
		 */
		_NULL(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "NULL", 0, 0, 0, 0, false, false, 1, 1, 1, Dyes._NULL, Element._NULL, Arrays.asList(new TC_AspectStack(TC_Aspects.VACUOS, 1))),


		//Lapis(526, TextureSet.SET_LAPIS, 1.0F, 0, 1, 1 | 4 | 8, 70, 70, 220, 0, "Lapis", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes.dyeBlue, 2, Arrays.asList(new MaterialStack(Materials.Lazurite, 12), new MaterialStack(Materials.Sodalite, 2), new MaterialStack(Materials.Pyrite, 1), new MaterialStack(Materials.Calcite, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.SENSUS, 1))),
		Pyrotheum(20, TextureSet.SET_FIERY, 1.0F, 0, 1, 1, 255, 128, 0, 0, "Pyrotheum", 2, 62, -1, 0, false, false, 2, 3, 1, Dyes.dyeYellow, 2, Arrays.asList(new MaterialStack(Materials.Coal, 1), new MaterialStack(Materials.Redstone, 1), new MaterialStack(Materials.Blaze, 1), new MaterialStack(Materials.Sulfur, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.PRAECANTATIO, 2), new TC_AspectStack(TC_Aspects.IGNIS, 1))),
		Cryotheum(21, TextureSet.SET_FIERY, 1.0F, 0, 1, 1, 102, 178, 255, 0, "Cryotheum", 2, 62, -1, 0, false, false, 2, 3, 1, Dyes.dyeLightBlue, 2, Arrays.asList(new MaterialStack(Materials.Blizz, 1), new MaterialStack(Materials.Redstone, 1), new MaterialStack(Materials.Snow, 1), new MaterialStack(Materials.Niter, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.PRAECANTATIO, 2), new TC_AspectStack(TC_Aspects.GELUM, 1))),

		/**
		 * Circuitry, Batteries and other Technical things
		 */
		Symbiotic(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "IV Tier", 0, 0, -1, 0, false, false, 1, 1, 1, Dyes.dyeLightGray, Arrays.asList(new TC_AspectStack(TC_Aspects.ELECTRUM, 4), new TC_AspectStack(TC_Aspects.MACHINA, 4))),
		Neutronic(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "LuV Tier", 0, 0, -1, 0, false, false, 1, 1, 1, Dyes.dyeLightGray, Arrays.asList(new TC_AspectStack(TC_Aspects.ELECTRUM, 6), new TC_AspectStack(TC_Aspects.MACHINA, 6))),
		Quantum(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "ZPM Tier", 0, 0, -1, 0, false, false, 1, 1, 1, Dyes.dyeLightGray, Arrays.asList(new TC_AspectStack(TC_Aspects.ELECTRUM, 8), new TC_AspectStack(TC_Aspects.MACHINA, 8)));

		/*Advanced(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "Advanced", 0, 0, -1, 0, false, false, 1, 1, 1, Dyes.dyeLightGray, Arrays.asList(new TC_AspectStack(TC_Aspects.MACHINA, 4))),
    	Data(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "Data", 0, 0, -1, 0, false, false, 1, 1, 1, Dyes.dyeLightGray, Arrays.asList(new TC_AspectStack(TC_Aspects.MACHINA, 5))),
    	Elite(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "Elite", 0, 0, -1, 0, false, false, 1, 1, 1, Dyes.dyeLightGray, Arrays.asList(new TC_AspectStack(TC_Aspects.MACHINA, 6))),
    	Master(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "Master", 0, 0, -1, 0, false, false, 1, 1, 1, Dyes.dyeLightGray, Arrays.asList(new TC_AspectStack(TC_Aspects.MACHINA, 7))),
    	Ultimate(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "Ultimate", 0, 0, -1, 0, false, false, 1, 1, 1, Dyes.dyeLightGray, Arrays.asList(new TC_AspectStack(TC_Aspects.MACHINA, 8))),
    	Superconductor(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "Superconductor", 0, 0, -1, 0, false, false, 1, 1, 1, Dyes.dyeLightGray, Arrays.asList(new TC_AspectStack(TC_Aspects.ELECTRUM, 8))),
    	Infinite(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "Infinite", 0, 0, -1, 0, false, false, 1, 1, 1, Dyes.dyeLightGray);*/

		/**
		 * List of all Materials.
		 */
		public static final Collection<GT_Materials> VALUES = new HashSet<GT_Materials>(Arrays.asList(values()));


		static {
			/*Primitive.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
    		Basic.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
    		Good.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
    		Advanced.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
    		Data.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
    		Elite.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
    		Master.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
    		Ultimate.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
    		Superconductor.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
    		Infinite.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);*/
			Symbiotic.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
			Neutronic.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
			Quantum.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
		}


		/**
		 * This Array can be changed dynamically by a Tick Handler in order to get a glowing Effect on all GT Meta Items out of this Material.
		 */
		public final short[] mRGBa = new short[]{255, 255, 255, 0}, mMoltenRGBa = new short[]{255, 255, 255, 0};
		public final TextureSet mIconSet;
		public final int mMetaItemSubID;
		public final boolean mUnificatable;
		public final GT_Materials mMaterialInto;
		public final List<MaterialStack> mMaterialList = new ArrayList<MaterialStack>();
		public final List<GT_Materials> mOreByProducts = new ArrayList<GT_Materials>(), mOreReRegistrations = new ArrayList<GT_Materials>();
		public final List<TC_AspectStack> mAspects = new ArrayList<TC_AspectStack>();
		private final ArrayList<ItemStack> mMaterialItems = new ArrayList<ItemStack>();
		private final Collection<SubTag> mSubTags = new HashSet<SubTag>();
		public Enchantment mEnchantmentTools = null, mEnchantmentArmors = null;
		public byte mEnchantmentToolsLevel = 0, mEnchantmentArmorsLevel = 0;
		public boolean mBlastFurnaceRequired = false;
		public float mToolSpeed = 1.0F, mHeatDamage = 0.0F;
		public String mChemicalFormula = "?", mDefaultLocalName = "null";
		public Dyes mColor = Dyes._NULL;
		public short mMeltingPoint = 0, mBlastFurnaceTemp = 0;
		public int mTypes = 0, mDurability = 16, mFuelPower = 0, mFuelType = 0, mExtraData = 0, mOreValue = 0, mOreMultiplier = 1, mByProductMultiplier = 1, mSmeltingMultiplier = 1;
		public long mDensity = M;
		public Element mElement = null;
		public GT_Materials mDirectSmelting = this, mOreReplacement = this, mMacerateInto = this, mSmeltInto = this, mArcSmeltInto = this, mHandleMaterial = this;
		public byte mToolQuality = 0;
		public Fluid mSolid = null, mFluid = null, mGas = null, mPlasma = null;
		/**
		 * This Fluid is used as standard Unit for Molten Materials. 1296 is a Molten Block, what means 144 is one Material Unit worth
		 */
		public Fluid mStandardMoltenFluid = null;

		private GT_Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aToolDurability, int aToolQuality, boolean aUnificatable) {
			mUnificatable = aUnificatable;
			mMaterialInto = this;
			mMetaItemSubID = aMetaItemSubID;
			mToolQuality = (byte) aToolQuality;
			mDurability = aToolDurability;
			mToolSpeed = aToolSpeed;
			mIconSet = aIconSet;
			if (aMetaItemSubID >= 0) {
				if (CORE.sMU_GeneratedMaterials[aMetaItemSubID] == null) {
					CORE.sMU_GeneratedMaterials[aMetaItemSubID] = this;
				} else {
					throw new IllegalArgumentException("The Index " + aMetaItemSubID + " is already used!");
				}
			}
		}

		private GT_Materials(GT_Materials aMaterialInto, boolean aReRegisterIntoThis) {
			mUnificatable = false;
			mDefaultLocalName = aMaterialInto.mDefaultLocalName;
			mMaterialInto = aMaterialInto.mMaterialInto;
			if (aReRegisterIntoThis) mMaterialInto.mOreReRegistrations.add(this);
			mChemicalFormula = aMaterialInto.mChemicalFormula;
			mMetaItemSubID = -1;
			mIconSet = TextureSet.SET_NONE;
		}

		/**
		 * @param aMetaItemSubID        the Sub-ID used in my own MetaItems. Range 0-1000. -1 for no Material
		 * @param aTypes                which kind of Items should be generated. Bitmask as follows:
		 *                              1 = Dusts of all kinds.
		 *                              2 = Dusts, Ingots, Plates, Rods/Sticks, Machine Components and other Metal specific things.
		 *                              4 = Dusts, Gems, Plates, Lenses (if transparent).
		 *                              8 = Dusts, Impure Dusts, crushed Ores, purified Ores, centrifuged Ores etc.
		 *                              16 = Cells
		 *                              32 = Plasma Cells
		 *                              64 = Tool Heads
		 *                              128 = Gears
		 * @param aR,                   aG, aB Color of the Material 0-255 each.
		 * @param aA                    transparency of the Material Texture. 0 = fully visible, 255 = Invisible.
		 * @param aLocalName            The Name used as Default for localization.
		 * @param aFuelType             Type of Generator to get Energy from this Material.
		 * @param aFuelPower            EU generated. Will be multiplied by 1000, also additionally multiplied by 2 for Gems.
		 * @param aAmplificationValue   Amount of UUM amplifier gotten from this.
		 * @param aUUMEnergy            Amount of EU needed to shape the UUM into this Material.
		 * @param aMeltingPoint         Used to determine the smelting Costs in Furnii.
		 * @param aBlastFurnaceTemp     Used to determine the needed Heat capactiy Costs in Blast Furnii.
		 * @param aBlastFurnaceRequired If this requires a Blast Furnace.
		 * @param aColor                Vanilla MC Wool Color which comes the closest to this.
		 */
		private GT_Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aToolDurability, int aToolQuality, int aTypes, int aR, int aG, int aB, int aA, String aLocalName, int aFuelType, int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor) {
			this(aMetaItemSubID, aIconSet, aToolSpeed, aToolDurability, aToolQuality, true);
			mDefaultLocalName = aLocalName;
			mMeltingPoint = (short) aMeltingPoint;
			mBlastFurnaceTemp = (short) aBlastFurnaceTemp;
			mBlastFurnaceRequired = aBlastFurnaceRequired;
			if (aTransparent) add(SubTag.TRANSPARENT);
			mFuelPower = aFuelPower;
			mFuelType = aFuelType;
			mOreValue = aOreValue;
			mDensity = (M * aDensityMultiplier) / aDensityDivider;
			mColor = aColor == null ? Dyes._NULL : aColor;
			if (mColor != null) add(SubTag.HAS_COLOR);
			mRGBa[0] = mMoltenRGBa[0] = (short) aR;
			mRGBa[1] = mMoltenRGBa[1] = (short) aG;
			mRGBa[2] = mMoltenRGBa[2] = (short) aB;
			mRGBa[3] = mMoltenRGBa[3] = (short) aA;
			mTypes = aTypes;
			if ((mTypes & 2) != 0) add(SubTag.SMELTING_TO_FLUID);
		}

		private GT_Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aToolDurability, int aToolQuality, int aTypes, int aR, int aG, int aB, int aA, String aLocalName, int aFuelType, int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, List<TC_AspectStack> aAspects) {
			this(aMetaItemSubID, aIconSet, aToolSpeed, aToolDurability, aToolQuality, aTypes, aR, aG, aB, aA, aLocalName, aFuelType, aFuelPower, aMeltingPoint, aBlastFurnaceTemp, aBlastFurnaceRequired, aTransparent, aOreValue, aDensityMultiplier, aDensityDivider, aColor);
			mAspects.addAll(aAspects);
		}

		/**
		 * @param aElement The Element Enum represented by this Material
		 */
		private GT_Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aToolDurability, int aToolQuality, int aTypes, int aR, int aG, int aB, int aA, String aLocalName, int aFuelType, int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, Element aElement, List<TC_AspectStack> aAspects) {
			this(aMetaItemSubID, aIconSet, aToolSpeed, aToolDurability, aToolQuality, aTypes, aR, aG, aB, aA, aLocalName, aFuelType, aFuelPower, aMeltingPoint, aBlastFurnaceTemp, aBlastFurnaceRequired, aTransparent, aOreValue, aDensityMultiplier, aDensityDivider, aColor);
			mElement = aElement;
			//mElement.mLinkedMaterials.add(this);
			if (aElement == Element._NULL) {
				mChemicalFormula = "Empty";
			} else {
				mChemicalFormula = aElement.toString();
				mChemicalFormula = mChemicalFormula.replaceAll("_", "-");
			}
			mAspects.addAll(aAspects);
		}

		private GT_Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aToolDurability, int aToolQuality, int aTypes, int aR, int aG, int aB, int aA, String aLocalName, int aFuelType, int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, int aExtraData, List<MaterialStack> aMaterialList) {
			this(aMetaItemSubID, aIconSet, aToolSpeed, aToolDurability, aToolQuality, aTypes, aR, aG, aB, aA, aLocalName, aFuelType, aFuelPower, aMeltingPoint, aBlastFurnaceTemp, aBlastFurnaceRequired, aTransparent, aOreValue, aDensityMultiplier, aDensityDivider, aColor, aExtraData, aMaterialList, null);
		}

		private GT_Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aToolDurability, int aToolQuality, int aTypes, int aR, int aG, int aB, int aA, String aLocalName, int aFuelType, int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, int aExtraData, List<MaterialStack> aMaterialList, List<TC_AspectStack> aAspects) {
			this(aMetaItemSubID, aIconSet, aToolSpeed, aToolDurability, aToolQuality, aTypes, aR, aG, aB, aA, aLocalName, aFuelType, aFuelPower, aMeltingPoint, aBlastFurnaceTemp, aBlastFurnaceRequired, aTransparent, aOreValue, aDensityMultiplier, aDensityDivider, aColor);
			mExtraData = aExtraData;
			mMaterialList.addAll(aMaterialList);
			mChemicalFormula = "";
			for (MaterialStack tMaterial : mMaterialList) mChemicalFormula += tMaterial.toString();
			mChemicalFormula = mChemicalFormula.replaceAll("_", "-");

			int tAmountOfComponents = 0, tMeltingPoint = 0;
			for (MaterialStack tMaterial : mMaterialList) {
				tAmountOfComponents += tMaterial.mAmount;
				if (tMaterial.mMaterial.mMeltingPoint > 0)
					tMeltingPoint += tMaterial.mMaterial.mMeltingPoint * tMaterial.mAmount;
				if (aAspects == null)
					for (TC_AspectStack tAspect : tMaterial.mMaterial.mAspects) tAspect.addToAspectList(mAspects);
			}

			if (mMeltingPoint < 0) mMeltingPoint = (short) (tMeltingPoint / tAmountOfComponents);

			tAmountOfComponents *= aDensityMultiplier;
			tAmountOfComponents /= aDensityDivider;
			if (aAspects == null) for (TC_AspectStack tAspect : mAspects)
				tAspect.mAmount = Math.max(1, tAspect.mAmount / Math.max(1, tAmountOfComponents));
			else mAspects.addAll(aAspects);
		}

		public static GT_Materials get(String aMaterialName) {
			Object tObject = GT_Utility.getFieldContent(GT_Materials.class, aMaterialName, false, false);
			if (tObject != null && tObject instanceof Materials) return (GT_Materials) tObject;
			return _NULL;
		}

		public static GT_Materials getRealMaterial(String aMaterialName) {
			return get(aMaterialName).mMaterialInto;
		}

		/**
		 * Called in preInit with the Config to set Values.
		 *
		 * @param aConfiguration
		 */
		public static void init(GT_Config aConfiguration) {
			for (GT_Materials tMaterial : VALUES) {
				String tString = tMaterial.toString().toLowerCase();
				tMaterial.mHeatDamage = (float) aConfiguration.get(ConfigCategories.Materials.heatdamage, tString, tMaterial.mHeatDamage);
				if (tMaterial.mBlastFurnaceRequired)
					tMaterial.mBlastFurnaceRequired = aConfiguration.get(ConfigCategories.Materials.blastfurnacerequirements, tString, true);
				if (tMaterial.mBlastFurnaceRequired && aConfiguration.get(ConfigCategories.Materials.blastinductionsmelter, tString, tMaterial.mBlastFurnaceTemp < 1500)){}
				//GT_ModHandler.ThermalExpansion.addSmelterBlastOre(tMaterial);
				//tMaterial.mHandleMaterial = (tMaterial == Desh ? tMaterial.mHandleMaterial : tMaterial == Diamond || tMaterial == Thaumium ? Wood : tMaterial.contains(SubTag.BURNING) ? Blaze : tMaterial.contains(SubTag.MAGICAL) && tMaterial.contains(SubTag.CRYSTAL) && Loader.isModLoaded(MOD_ID_TC) ? Thaumium : tMaterial.getMass() > Element.Tc.getMass() * 2 ? TungstenSteel : tMaterial.getMass() > Element.Tc.getMass() ? Steel : Wood);
			}
		}

		public boolean isRadioactive() {
			if (mElement != null) return mElement.mHalfLifeSeconds >= 0;
			for (MaterialStack tMaterial : mMaterialList) if (tMaterial.mMaterial.isRadioactive()) return true;
			return false;
		}

		public long getProtons() {
			if (mElement != null) return mElement.getProtons();
			if (mMaterialList.size() <= 0) return Element.Tc.getProtons();
			long rAmount = 0, tAmount = 0;
			for (MaterialStack tMaterial : mMaterialList) {
				tAmount += tMaterial.mAmount;
				rAmount += tMaterial.mAmount * tMaterial.mMaterial.getProtons();
			}
			return (getDensity() * rAmount) / (tAmount * M);
		}

		public long getNeutrons() {
			if (mElement != null) return mElement.getNeutrons();
			if (mMaterialList.size() <= 0) return Element.Tc.getNeutrons();
			long rAmount = 0, tAmount = 0;
			for (MaterialStack tMaterial : mMaterialList) {
				tAmount += tMaterial.mAmount;
				rAmount += tMaterial.mAmount * tMaterial.mMaterial.getNeutrons();
			}
			return (getDensity() * rAmount) / (tAmount * M);
		}

		public long getMass() {
			if (mElement != null) return mElement.getMass();
			if (mMaterialList.size() <= 0) return Element.Tc.getMass();
			long rAmount = 0, tAmount = 0;
			for (MaterialStack tMaterial : mMaterialList) {
				tAmount += tMaterial.mAmount;
				rAmount += tMaterial.mAmount * tMaterial.mMaterial.getMass();
			}
			return (getDensity() * rAmount) / (tAmount * M);
		}

		public long getDensity() {
			return mDensity;
		}

		public String getToolTip() {
			return getToolTip(1, false);
		}

		public String getToolTip(boolean aShowQuestionMarks) {
			return getToolTip(1, aShowQuestionMarks);
		}

		public String getToolTip(long aMultiplier) {
			return getToolTip(aMultiplier, false);
		}

		public String getToolTip(long aMultiplier, boolean aShowQuestionMarks) {
			if (!aShowQuestionMarks && mChemicalFormula.equals("?")) return "";
			if (aMultiplier >= M * 2 && !mMaterialList.isEmpty()) {
				return ((mElement != null || (mMaterialList.size() < 2 && mMaterialList.get(0).mAmount == 1)) ? mChemicalFormula : "(" + mChemicalFormula + ")") + aMultiplier;
			}
			return mChemicalFormula;
		}

		/**
		 * Adds an ItemStack to this Material.
		 */
		public GT_Materials add(ItemStack aStack) {
			if (aStack != null && !contains(aStack)) mMaterialItems.add(aStack);
			return this;
		}

		/**
		 * This is used to determine if any of the ItemStacks belongs to this Material.
		 */
		public boolean contains(ItemStack... aStacks) {
			if (aStacks == null || aStacks.length <= 0) return false;
			for (ItemStack tStack : mMaterialItems)
				for (ItemStack aStack : aStacks)
					if (GT_Utility.areStacksEqual(aStack, tStack, !tStack.hasTagCompound())) return true;
			return false;
		}

		/**
		 * This is used to determine if an ItemStack belongs to this Material.
		 */
		public boolean remove(ItemStack aStack) {
			if (aStack == null) return false;
			boolean temp = false;
			for (int i = 0; i < mMaterialItems.size(); i++)
				if (GT_Utility.areStacksEqual(aStack, mMaterialItems.get(i))) {
					mMaterialItems.remove(i--);
					temp = true;
				}
			return temp;
		}

		/**
		 * Adds a SubTag to this Material
		 */
		@Override
		public ISubTagContainer add(SubTag... aTags) {
			if (aTags != null) for (SubTag aTag : aTags)
				if (aTag != null && !contains(aTag)) {
					aTag.addContainerToList(this);
					mSubTags.add(aTag);
				}
			return this;
		}

		/**
		 * If this Material has this exact SubTag
		 */
		@Override
		public boolean contains(SubTag aTag) {
			return mSubTags.contains(aTag);
		}

		/**
		 * Removes a SubTag from this Material
		 */
		@Override
		public boolean remove(SubTag aTag) {
			return mSubTags.remove(aTag);
		}

		/**
		 * Sets the Heat Damage for this Material (negative = frost)
		 */
		public GT_Materials setHeatDamage(float aHeatDamage) {
			mHeatDamage = aHeatDamage;
			return this;
		}

		/**
		 * Adds a Material to the List of Byproducts when grinding this Ore.
		 * Is used for more precise Ore grinding, so that it is possible to choose between certain kinds of Materials.
		 */
		public GT_Materials addOreByProduct(GT_Materials aMaterial) {
			if (!mOreByProducts.contains(aMaterial.mMaterialInto)) mOreByProducts.add(aMaterial.mMaterialInto);
			return this;
		}

		/**
		 * Adds multiple Materials to the List of Byproducts when grinding this Ore.
		 * Is used for more precise Ore grinding, so that it is possible to choose between certain kinds of Materials.
		 */
		public GT_Materials addOreByProducts(GT_Materials... aMaterials) {
			for (GT_Materials tMaterial : aMaterials) if (tMaterial != null) addOreByProduct(tMaterial);
			return this;
		}

		/**
		 * If this Ore gives multiple drops of its Main Material.
		 * Lapis Ore for example gives about 6 drops.
		 */
		public GT_Materials setOreMultiplier(int aOreMultiplier) {
			if (aOreMultiplier > 0) mOreMultiplier = aOreMultiplier;
			return this;
		}

		/**
		 * If this Ore gives multiple drops of its Byproduct Material.
		 */
		public GT_Materials setByProductMultiplier(int aByProductMultiplier) {
			if (aByProductMultiplier > 0) mByProductMultiplier = aByProductMultiplier;
			return this;
		}

		/**
		 * If this Ore gives multiple drops of its Main Material.
		 * Lapis Ore for example gives about 6 drops.
		 */
		public GT_Materials setSmeltingMultiplier(int aSmeltingMultiplier) {
			if (aSmeltingMultiplier > 0) mSmeltingMultiplier = aSmeltingMultiplier;
			return this;
		}

		/**
		 * This Ore should be smolten directly into an Ingot of this Material instead of an Ingot of itself.
		 */
		public GT_Materials setDirectSmelting(GT_Materials aMaterial) {
			if (aMaterial != null) mDirectSmelting = aMaterial.mMaterialInto.mDirectSmelting;
			return this;
		}

		/**
		 * This Material should be the Main Material this Ore gets ground into.
		 * Example, Chromite giving Chrome or Tungstate giving Tungsten.
		 */
		public GT_Materials setOreReplacement(GT_Materials aMaterial) {
			if (aMaterial != null) mOreReplacement = aMaterial.mMaterialInto.mOreReplacement;
			return this;
		}

		/**
		 * This Material smelts always into an instance of aMaterial. Used for Magnets.
		 */
		public GT_Materials setSmeltingInto(GT_Materials aMaterial) {
			if (aMaterial != null) mSmeltInto = aMaterial.mMaterialInto.mSmeltInto;
			return this;
		}

		/**
		 * This Material arc smelts always into an instance of aMaterial. Used for Wrought Iron.
		 */
		public GT_Materials setArcSmeltingInto(GT_Materials aMaterial) {
			if (aMaterial != null) mArcSmeltInto = aMaterial.mMaterialInto.mArcSmeltInto;
			return this;
		}

		/**
		 * This Material macerates always into an instance of aMaterial.
		 */
		public GT_Materials setMaceratingInto(GT_Materials aMaterial) {
			if (aMaterial != null) mMacerateInto = aMaterial.mMaterialInto.mMacerateInto;
			return this;
		}

		public GT_Materials setEnchantmentForTools(Enchantment aEnchantment, int aEnchantmentLevel) {
			mEnchantmentTools = aEnchantment;
			mEnchantmentToolsLevel = (byte) aEnchantmentLevel;
			return this;
		}

		public GT_Materials setEnchantmentForArmors(Enchantment aEnchantment, int aEnchantmentLevel) {
			mEnchantmentArmors = aEnchantment;
			mEnchantmentArmorsLevel = (byte) aEnchantmentLevel;
			return this;
		}

		public FluidStack getSolid(long aAmount) {
			if (mSolid == null) return null;
			return new GT_FluidStack(mSolid, (int) aAmount);
		}

		public FluidStack getFluid(long aAmount) {
			if (mFluid == null) return null;
			return new GT_FluidStack(mFluid, (int) aAmount);
		}

		public FluidStack getGas(long aAmount) {
			if (mGas == null) return null;
			return new GT_FluidStack(mGas, (int) aAmount);
		}

		public FluidStack getPlasma(long aAmount) {
			if (mPlasma == null) return null;
			return new GT_FluidStack(mPlasma, (int) aAmount);
		}

		public FluidStack getMolten(long aAmount) {
			if (mStandardMoltenFluid == null) return null;
			return new GT_FluidStack(mStandardMoltenFluid, (int) aAmount);
		}

		@Override
		public short[] getRGBA() {
			return mRGBa;
		}

		public static volatile int VERSION = 508;

	}

}