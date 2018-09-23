package gregtech.common.items;

import gregtech.api.enums.Materials;
import gregtech.api.util.GT_LanguageManager;

public enum DropType {

    OIL("oil",true),
    MUTAGEN("small mutagen catalyst",true),
    COOLANT("coolant",true),
    HOT_COOLANT("hot coolant",true),
    HYDRA("hydra blood",true),
    SNOW_QUEEN("snowqueen blood",true),
    OXYGEN("oxygen",true),
    LAPIS("lapis coolant",true);

    private static int[][] colours = new int[][]{
            {0x19191B, 0x303032},
            {0xffc100, 0x00ff11},
            {0x144F5A, 0x2494A2},
            {0xC11F1F, 0xEBB9B9},
            {0x872836, 0xB8132C},
            {0xD02001, 0x9C0018},
            {0x003366, 0x0066BB},
            {0x1727b1, 0x008ce3},
    };
    public boolean showInList;
    public Materials material;
    public int chance;
    private String name;
    private DropType(String pName, boolean show) {
        this.name = pName;
        this.showInList = show;
    }

    public void setHidden() {
        this.showInList = false;
    }

    public String getName() {
//		return "gt.comb."+this.name;
        return GT_LanguageManager.addStringLocalization("drop." + this.name, this.name.substring(0, 1).toUpperCase() + this.name.substring(1) + " Drop");
    }

    public int[] getColours() {
        return colours[this.ordinal()];
    }
}