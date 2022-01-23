package gtPlusPlus.xmod.forestry.bees.handler;

import gregtech.api.util.GT_LanguageManager;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;
import net.minecraft.item.ItemStack;

public enum GTPP_PollenType {

	DRAGONBLOOD(0, "Dragon Blood", true, Utils.rgbtoHexValue(220, 20, 20), Utils.rgbtoHexValue(20, 20, 20));

	public boolean mShowInList;
	public Material mMaterial;
	public int mChance;
	public int mID;

	private String mName;
	private String mNameUnlocal;
	private int[] mColour;

	private static void map(int aId, GTPP_PollenType aType) {
		GTPP_Bees.sPollenMappings.put(aId, aType);
	}
	
	public static GTPP_PollenType get(int aID) {
		return GTPP_Bees.sPollenMappings.get(aID);
	}
	
	private GTPP_PollenType(int aID, String aName, boolean aShow, int... aColour) {
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
		return GT_LanguageManager.addStringLocalization("pollen." + this.mNameUnlocal, this.mName + " Pollen");
	}

	public int[] getColours() {
		return mColour;
	}

	public ItemStack getStackForType(int count) {
		return new ItemStack(GTPP_Bees.pollen, count, mID);
	}
}
