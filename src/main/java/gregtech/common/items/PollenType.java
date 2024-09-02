package gregtech.common.items;

import gregtech.api.enums.Materials;
import gregtech.api.util.GTLanguageManager;

public enum PollenType {

    MATRIX("matrix", true);

    private static final int[][] colours = new int[][] { { 0x19191B, 0x303032 }, };
    public boolean showInList;
    public Materials material;
    public int chance;
    private final String name;

    PollenType(String pName, boolean show) {
        this.name = pName;
        this.showInList = show;
    }

    public void setHidden() {
        this.showInList = false;
    }

    public String getName() {
        return GTLanguageManager.addStringLocalization(
            "pollen." + this.name,
            this.name.substring(0, 1)
                .toUpperCase() + this.name.substring(1) + " Pollen");
    }

    public int[] getColours() {
        return colours[this.ordinal()];
    }
}
