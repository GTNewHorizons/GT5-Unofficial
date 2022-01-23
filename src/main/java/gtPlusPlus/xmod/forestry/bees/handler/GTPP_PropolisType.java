package gtPlusPlus.xmod.forestry.bees.handler;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;

public enum GTPP_PropolisType {

    DRAGONBLOOD(0, "Dragon Blood", true, Utils.rgbtoHexValue(220, 20, 20));

	public boolean mShowInList;
	public Materials mMaterial;
	public int mChance;
	public int mID;

	private String mName;
	private String mNameUnlocal;
	private int mColour;

	private static void map(int aId, GTPP_PropolisType aType) {
		GTPP_Bees.sPropolisMappings.put(aId, aType);
	}
	
	public static GTPP_PropolisType get(int aID) {
		return GTPP_Bees.sPropolisMappings.get(aID);
	}

    private GTPP_PropolisType(int aID, String aName, boolean aShow, int aColour) {
    	this.mID = aID;
        this.mName = aName;
		this.mNameUnlocal = aName.toLowerCase().replaceAll(" ", "");
        this.mShowInList = aShow;
        this.mColour = aColour;
		map(aID, this);
    }

    public void setHidden() {
        this.mShowInList = false;
    }

    public String getName() {
        return GT_LanguageManager.addStringLocalization("propolis." + this.mNameUnlocal, this.mName + " Propolis");
    }

    public int getColours() {
        return mColour;
    }
}
