package gtPlusPlus.xmod.forestry.bees.custom;

import gregtech.api.util.ColorUtil;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.StringUtils;

public enum CustomCombs {

    // DO NOT RE-ORDER THIS ENUM, ORDER MATTERS

    // Rubbers & Silicons
    SILICON("silicon", true, 100, ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125)),
    RUBBER("rubber", true, 100, ColorUtil.toRGB(55, 55, 55), ColorUtil.toRGB(75, 75, 75)),
    PLASTIC("polyethylene", true, 75, ColorUtil.toRGB(245, 245, 245), ColorUtil.toRGB(175, 175, 175)),
    PTFE("polytetrafluoroethylene", true, 50, ColorUtil.toRGB(150, 150, 150), ColorUtil.toRGB(75, 75, 75)),
    PBS("styrene-butadiene", true, 25, ColorUtil.toRGB(33, 26, 24), ColorUtil.toRGB(23, 16, 14)),

    // Fuels
    BIOMASS("biomass", true, 100, ColorUtil.toRGB(33, 225, 24), ColorUtil.toRGB(23, 175, 14)),
    ETHANOL("ethanol", true, 75, ColorUtil.toRGB(255, 128, 0), ColorUtil.toRGB(220, 156, 32)),
    DIESEL("diesel", true, 50, ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125)),
    NITRO("nitro", true, 25, ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125)),
    HOOTCH("hootch", true, 50, ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125)),
    ROCKETFUEL("rocket", true, 25, ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125)),

    // Materials which are hard, if not impossible to obtain.
    FLUORINE("fluorine", true, 25, ColorUtil.toRGB(30, 230, 230), ColorUtil.toRGB(10, 150, 150)),
    COKE("coke", true, 50, ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125)),
    FORCE("force", true, 50, ColorUtil.toRGB(250, 250, 20), ColorUtil.toRGB(200, 200, 5)),
    NIKOLITE("nikolite", true, 75, ColorUtil.toRGB(60, 180, 200), ColorUtil.toRGB(40, 150, 170)),
    MITHRIL("mithril", true, 10, ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125)),
    ADAMANTIUM("adamantium", true, 5, ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125)),

    // Trash
    SALT("salt", true, 75, ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125)),
    SAND("sand", true, 100, ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125));

    public static final CustomCombs[] VALUES = CustomCombs.values();

    public final boolean showInList;
    public final int chance;
    private final String name;
    private final int primaryColorRGB;
    private final int secondaryColorRGB;

    CustomCombs(String pName, boolean show, int chance, int color1, int color2) {
        this.name = pName;
        this.chance = chance;
        this.showInList = show;
        this.primaryColorRGB = color1;
        this.secondaryColorRGB = color2;
    }

    public String getName() {
        // return "gt.comb."+this.name;
        return GTLanguageManager
            .addStringLocalization("comb." + this.name, StringUtils.capitalize(this.name) + " Comb");
    }

    public int getColorForPass(int pass) {
        if (pass == 0) {
            return primaryColorRGB;
        }
        return secondaryColorRGB;
    }
}
