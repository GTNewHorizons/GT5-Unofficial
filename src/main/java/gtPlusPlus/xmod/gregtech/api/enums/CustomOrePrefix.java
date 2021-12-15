package gtPlusPlus.xmod.gregtech.api.enums;

import static gregtech.api.enums.GT_Values.B;

import gregtech.api.enums.OrePrefixes;
import net.minecraftforge.common.util.EnumHelper;

public enum CustomOrePrefix {

	milled("Milled Ores", "Milled ", " Ore", true, true, false, false, false, false, false, false, false, true, B[3], -1, 64, -1);
	
	private final String mRegularLocalName;
	private final String mLocalizedMaterialPre;
	private final String mLocalizedMaterialPost;
	private final boolean mIsUnificatable;
	private final boolean mIsMaterialBased;
	private final boolean mIsSelfReferencing;
	private final boolean mIsContainer;
	private final boolean mDontUnificateActively;
	private final boolean mIsUsedForBlocks;
	private final boolean mAllowNormalRecycling;
	private final boolean mGenerateDefaultItem;
	private final boolean mIsEnchantable;
	private final boolean mIsUsedForOreProcessing;
	private final int mMaterialGenerationBits;
	private final long mMaterialAmount;
	private final int mDefaultStackSize;
	private final int mTextureindex;	
	
	private OrePrefixes mSelfReference;
	
	private CustomOrePrefix(
			String aRegularLocalName,
			String aLocalizedMaterialPre,
			String aLocalizedMaterialPost,
			boolean aIsUnificatable,
			boolean aIsMaterialBased,
			boolean aIsSelfReferencing,
			boolean aIsContainer,
			boolean aDontUnificateActively,
			boolean aIsUsedForBlocks,
			boolean aAllowNormalRecycling,
			boolean aGenerateDefaultItem,
			boolean aIsEnchantable,
			boolean aIsUsedForOreProcessing,
			int aMaterialGenerationBits,
			long aMaterialAmount,
			int aDefaultStackSize,
			int aTextureindex) {		
		
		mRegularLocalName = aRegularLocalName;
		mLocalizedMaterialPre = aLocalizedMaterialPre;
		mLocalizedMaterialPost = aLocalizedMaterialPost;
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
		mDefaultStackSize = aDefaultStackSize;
		mTextureindex = aTextureindex;	
		
	}
	
	public final boolean addToEnum() {
		
		mSelfReference = EnumHelper.addEnum(OrePrefixes.class, this.name(),
				new Class[] {
						String.class,
						String.class, String.class, boolean.class, 
						boolean.class, boolean.class, boolean.class, 
						boolean.class, boolean.class, boolean.class, 
						boolean.class, boolean.class, boolean.class, 
						int.class, long.class, int.class, int.class	
				},
				new Object[] {
				mRegularLocalName,
				mLocalizedMaterialPre,
				mLocalizedMaterialPost,
				mIsUnificatable,
				mIsMaterialBased,
				mIsSelfReferencing,
				mIsContainer,
				mDontUnificateActively,
				mIsUsedForBlocks,
				mAllowNormalRecycling,
				mGenerateDefaultItem,
				mIsEnchantable,
				mIsUsedForOreProcessing,
				mMaterialGenerationBits,
				mMaterialAmount,
				mDefaultStackSize,
				mTextureindex});
		
		return mSelfReference != null;
	}
	
	public static final boolean checkEntryWasAdded(CustomOrePrefix aCustomPrefixObject) {
		return aCustomPrefixObject.mSelfReference != null;
	}
	
	public OrePrefixes get() {
		return mSelfReference;
	}

}
