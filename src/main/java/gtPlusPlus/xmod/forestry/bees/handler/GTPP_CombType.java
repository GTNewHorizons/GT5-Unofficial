package gtPlusPlus.xmod.forestry.bees.handler;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;

public enum GTPP_CombType {

	DRAGONBLOOD(0, "Dragon Blood", true, Materials._NULL, 30, Utils.rgbtoHexValue(220, 20, 20), Utils.rgbtoHexValue(20, 20, 20)),
	FORCE(1, "Force", true, Materials.Force, 30, Utils.rgbtoHexValue(250, 250, 20), Utils.rgbtoHexValue(200, 200, 5));

	public boolean mShowInList;
	public Materials mMaterial;
	public int mChance;
	public int mID;

	private String mName;
	private String mNameUnlocal;
	private int[] mColour;

	private static void map(int aId, GTPP_CombType aType) {
		GTPP_Bees.sCombMappings.put(aId, aType);
	}
	
	public static GTPP_CombType get(int aID) {
		return GTPP_Bees.sCombMappings.get(aID);
	}

	GTPP_CombType(int aID, String aName, boolean aShow, Materials aMaterial, int aChance, int... aColour) {
		this.mID = aID;
		this.mName = aName;
		this.mNameUnlocal = aName.toLowerCase().replaceAll(" ", "");
		this.mMaterial = aMaterial;
		this.mChance = aChance;
		this.mShowInList = aShow;
		this.mColour = aColour;
		map(aID, this);
	}

	public void setHidden() {
		this.mShowInList = false;
	}

	public String getName() {
		return GT_LanguageManager.addStringLocalization("comb." + this.mNameUnlocal, this.mName + " Comb");
	}

	public int[] getColours() {
		return mColour == null || mColour.length != 2 ? new int[]{0, 0} : mColour;
	}
}
