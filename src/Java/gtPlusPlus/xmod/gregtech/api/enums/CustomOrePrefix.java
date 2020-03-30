package gtPlusPlus.xmod.gregtech.api.enums;

import static gregtech.api.enums.GT_Values.B;

import gregtech.api.enums.OrePrefixes;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import net.minecraftforge.common.util.EnumHelper;

public class CustomOrePrefix {

	public static CustomOrePrefix Milled;

	static {
		Milled = new CustomOrePrefix("Milled Ores", "Milled ", " Ore", true, true, false, false, false, false, false, false, false, true, B[3], -1, 64, -1);
	}
	
	public static void init() {
		// Does nothing except run the static{} block
	}

	private final String mLocalizedMaterialPre;
	
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

		mLocalizedMaterialPre = aLocalizedMaterialPre;
		
		// Add this to the GT Enum
		EnumHelper.addEnum(OrePrefixes.class, aLocalizedMaterialPre, new Object[] {
				aRegularLocalName,
				aLocalizedMaterialPre,
				aLocalizedMaterialPost,
				aIsUnificatable,
				aIsMaterialBased,
				aIsSelfReferencing,
				aIsContainer,
				aDontUnificateActively,
				aIsUsedForBlocks,
				aAllowNormalRecycling,
				aGenerateDefaultItem,
				aIsEnchantable,
				aIsUsedForOreProcessing,
				aMaterialGenerationBits,
				aMaterialAmount,
				aDefaultStackSize,
				aTextureindex}
		);
		
		Logger.INFO("Registered custom OrePrefixes '"+aLocalizedMaterialPre+"'. Success? "+checkEntryWasAdded(this));
		
	}
	
	private static final boolean checkEntryWasAdded(CustomOrePrefix aCustomPrefixObject) {
		for (OrePrefixes o :OrePrefixes.values()) {
			if (o.mLocalizedMaterialPre.equals(aCustomPrefixObject.mLocalizedMaterialPre)) {
				return true;
			}
		}
		return false;
	}
	
	public static OrePrefixes get(CustomOrePrefix aCustomPrefixObject) {
		for (OrePrefixes o :OrePrefixes.values()) {
			if (o.mLocalizedMaterialPre.equals(aCustomPrefixObject.mLocalizedMaterialPre)) {
				return o;
			}
		}		
		CORE.crash("Tried to obtain an invalid custom OrePrefixes object");
		return null;
	}

}
