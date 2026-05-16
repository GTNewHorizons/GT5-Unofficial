package gregtech.common.items;

import net.minecraft.util.StatCollector;

import gregtech.api.enums.Materials;
import gregtech.api.util.GTLanguageManager;

public enum PropolisType {

    End("End", true),
    Ectoplasma("Ectoplasma", true),
    Arcaneshard("Arcaneshard", true),
    Stardust("Stardust", true),
    Dragonessence("Dragonessence", true),
    Enderman("Enderman", true),
    Silverfish("Silverfish", true),
    Endium("Endium", true),
    Fireessence("Fireessence", true);

    private static final int[] colours = new int[] { 0xCC00FA, 0xDCB0E5, 0x9010AD, 0xFFFF00, 0x911ECE, 0x161616,
        0xEE053D, 0xa0ffff, 0xD41238 };

    public boolean showInList;
    public Materials material;
    public int chance;
    private final String name;

    PropolisType(String pName, boolean show) {
        this.name = pName;
        this.showInList = show;
        GTLanguageManager.addStringLocalization(
            "propolis." + this.name,
            this.name.substring(0, 1)
                .toUpperCase() + this.name.substring(1) + " Propolis");
    }

    public void setHidden() {
        this.showInList = false;
    }

    public String getLocalizedName() {
        // return "gt.comb."+this.name;
        return StatCollector.translateToLocal("propolis." + this.name);
    }

    public int getColours() {
        return colours[this.ordinal()];
    }
}
