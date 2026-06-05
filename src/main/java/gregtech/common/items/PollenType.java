package gregtech.common.items;

import net.minecraft.util.StatCollector;

import gregtech.api.enums.Materials;

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

    public String getLocalizedName() {
        return StatCollector.translateToLocal("gt.pollenType." + this.name);
    }

    public int[] getColours() {
        return colours[this.ordinal()];
    }
}
