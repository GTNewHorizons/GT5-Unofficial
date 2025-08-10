package gregtech.common.items;

import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.StringUtils;

public enum PropolisType {

    // DO NOT RE-ORDER THIS ENUM, ORDER MATTERS

    End(0xCC00FA, true),
    Ectoplasma(0xDCB0E5, true),
    Arcaneshard(0x9010AD, true),
    Stardust(0xFFFF00, true),
    Dragonessence(0x911ECE, true),
    Enderman(0x161616, true),
    Silverfish(0xEE053D, true),
    Endium(0xA0FFFF, true),
    Fireessence(0xD41238, true);

    public static final PropolisType[] VALUES = PropolisType.values();

    private final int colorRGB;
    public boolean showInList;

    PropolisType(int colorRGB, boolean show) {
        this.colorRGB = colorRGB;
        this.showInList = show;
    }

    public void setHidden() {
        this.showInList = false;
    }

    public String getName() {
        // return "gt.comb."+this.name;
        final String name = this.name();
        return GTLanguageManager.addStringLocalization("propolis." + name, StringUtils.capitalize(name) + " Propolis");
    }

    public int getColours() {
        return colorRGB;
    }
}
