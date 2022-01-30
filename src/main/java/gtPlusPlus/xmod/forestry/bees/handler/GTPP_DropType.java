package gtPlusPlus.xmod.forestry.bees.handler;

import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;
import net.minecraft.item.ItemStack;

public enum GTPP_DropType {

	DRAGONBLOOD(0, "Dragon Blood", true, Utils.rgbtoHexValue(220, 20, 20), Utils.rgbtoHexValue(20, 20, 20)),
	FORCE(1, "Force", true, Utils.rgbtoHexValue(250, 250, 20), Utils.rgbtoHexValue(200, 200, 5));
    
	public boolean mShowInList;
	public Material mMaterial;
	public int mChance;
	public int mID;

	private String mName;
	private String mNameUnlocal;
	private int[] mColour;
	
	private static void map(int aId, GTPP_DropType aType) {
		GTPP_Bees.sDropMappings.put(aId, aType);
	}
	
	public static GTPP_DropType get(int aID) {
		return GTPP_Bees.sDropMappings.get(aID);
	}
	
    private GTPP_DropType(int aID, String aName, boolean aShow, int... aColour) {
    	this.mID = aID;
        this.mName = aName;
		this.mNameUnlocal = aName.toLowerCase().replaceAll(" ", "");
        this.mShowInList = aShow;
        this.mColour = aColour;
		map(aID, this);
		this.mMaterial = GTPP_Bees.sMaterialMappings.get(aName.toLowerCase().replaceAll(" ", ""));
    }

    public void setHidden() {
        this.mShowInList = false;
    }

    public String getName() {
        return GT_LanguageManager.addStringLocalization("drop." + this.mNameUnlocal, this.mName + " Drop");
    }

    public int[] getColours() {
        return mColour;
    }

	public ItemStack getStackForType(int count) {
		return new ItemStack(GTPP_Bees.drop, count, mID);
	}
}
