package gtPlusPlus.xmod.forestry.bees.handler;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.util.GTLanguageManager;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.xmod.forestry.bees.registry.GTPP_Bees;

public enum GTPPPollenType {

    DRAGONBLOOD(0, "Dragonblood", true, Utils.rgbtoHexValue(220, 20, 20), Utils.rgbtoHexValue(20, 20, 20));

    public boolean mShowInList;
    public final Material mMaterial;
    public int mChance;
    public final int mID;

    private final String mName;
    private final String mNameUnlocal;
    private final int[] mColour;

    private static void map(int aId, GTPPPollenType aType) {
        GTPP_Bees.sPollenMappings.put(aId, aType);
    }

    public static GTPPPollenType get(int aID) {
        return GTPP_Bees.sPollenMappings.get(aID);
    }

    GTPPPollenType(int aID, String aName, boolean aShow, int... aColour) {
        this.mID = aID;
        this.mName = aName;
        this.mNameUnlocal = aName.toLowerCase()
            .replaceAll(" ", "");
        this.mShowInList = aShow;
        this.mColour = aColour;
        map(aID, this);
        this.mMaterial = GTPP_Bees.sMaterialMappings.get(
            aName.toLowerCase()
                .replaceAll(" ", ""));
        GTLanguageManager.addStringLocalization("gtplusplus.pollen." + this.mNameUnlocal, this.mName + " Pollen");
    }

    public void setHidden() {
        this.mShowInList = false;
    }

    public String getLocalizedName() {
        return StatCollector.translateToLocal("gtplusplus.pollen." + this.mNameUnlocal);
    }

    public int[] getColours() {
        return mColour;
    }

    public ItemStack getStackForType(int count) {
        return new ItemStack(GTPP_Bees.pollen, count, mID);
    }
}
