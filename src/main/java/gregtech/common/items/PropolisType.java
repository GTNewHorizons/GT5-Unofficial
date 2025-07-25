package gregtech.common.items;

import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.StringUtils;

public enum PropolisType {

    Arcaneshard(0x9010AD, true),
    Dragonessence(0x911ECE, true),
    Ectoplasma(0xDCB0E5, true),
    End(0xCC00FA, true),
    Enderman(0x161616, true),
    Endium(0xA0FFFF, true),
    Fireessence(0xD41238, true),
    Silverfish(0xEE053D, true),
    Stardust(0xFFFF00, true);

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
