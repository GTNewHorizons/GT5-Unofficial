package gtPlusPlus.xmod.forestry.bees.custom;

import gregtech.api.enums.Materials;
import gregtech.api.util.ColorUtil;
import gregtech.api.util.GTLanguageManager;

public enum CustomCombs {
    // Custom Bees

    // Rubbers & Silicons
    SILICON("silicon", true, Materials.Silicon, 100),
    RUBBER("rubber", true, Materials.Rubber, 100),
    PLASTIC("polyethylene", true, Materials.Plastic, 75),
    PTFE("polytetrafluoroethylene", true, GTPPBees.PTFE, 50),
    PBS("styrene-butadiene", true, GTPPBees.PBS, 25),

    // Fuels
    BIOMASS("biomass", true, Materials.Biomass, 100),
    ETHANOL("ethanol", true, Materials.Ethanol, 75),
    DIESEL("diesel", true, Materials.Fuel, 50),
    NITRO("nitro", true, Materials.NitroFuel, 25),
    HOOTCH("hootch", true, Materials.Silicon, 50),
    ROCKETFUEL("rocket", true, Materials.Silicon, 25),

    // Materials which are hard, if not impossible to obtain.
    FLUORINE("fluorine", true, Materials.Fluorine, 25),
    COKE("coke", true, Materials._NULL, 50),
    FORCE("force", true, Materials.Force, 50),
    NIKOLITE("nikolite", true, Materials.Electrotine, 75), // nikolite is deprecated as a material, swap to electrotine
    MITHRIL("mithril", true, Materials.Mithril, 10),
    ADAMANTIUM("adamantium", true, Materials.Adamantium, 5),

    // Trash
    SALT("salt", true, Materials.Salt, 75),
    SAND("sand", true, Materials.Sand, 100),;

    private static final int[][] colours = new int[][] {
        { ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125) }, // SILICON
        { ColorUtil.toRGB(55, 55, 55), ColorUtil.toRGB(75, 75, 75) }, // RUBBER
        { ColorUtil.toRGB(245, 245, 245), ColorUtil.toRGB(175, 175, 175) }, // PLASTIC
        { ColorUtil.toRGB(150, 150, 150), ColorUtil.toRGB(75, 75, 75) }, // PTFE
        { ColorUtil.toRGB(33, 26, 24), ColorUtil.toRGB(23, 16, 14) }, // PBS
        // Unused
        { ColorUtil.toRGB(33, 225, 24), ColorUtil.toRGB(23, 175, 14) }, // Biofuel
        { ColorUtil.toRGB(255, 128, 0), ColorUtil.toRGB(220, 156, 32) }, // Ethanol
        { ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125) }, //
        { ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125) }, //
        { ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125) }, //
        { ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125) }, //
        { ColorUtil.toRGB(30, 230, 230), ColorUtil.toRGB(10, 150, 150) }, // Fluorine
        { ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125) }, //
        { ColorUtil.toRGB(250, 250, 20), ColorUtil.toRGB(200, 200, 5) }, // Force
        { ColorUtil.toRGB(60, 180, 200), ColorUtil.toRGB(40, 150, 170) }, // Nikolite
        { ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125) }, //
        { ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125) }, //
        { ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125) }, //
        { ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125) }, //
        { ColorUtil.toRGB(75, 75, 75), ColorUtil.toRGB(125, 125, 125) }, { 0x666666, 0x525252 }, { 0x2E8F5B, 0xDCC289 },
        { 0x4C4C4C, 0x333333 }, { 0x808080, 0x999999 }, { 0x57CFFB, 0xBBEEFF }, { 0x7D0F0F, 0xD11919 },
        { 0x1947D1, 0x476CDA }, { 0xE6005C, 0xCC0052 }, { 0x0033CC, 0x00248F }, { 0xCCFFFF, 0xA3CCCC },
        { 0x248F24, 0xCCFFCC }, { 0x248F24, 0x2EB82E }, { 0xD4D4D4, 0x58300B }, { 0xFF6600, 0xE65C00 },
        { 0xD4D4D4, 0xDDDDDD }, { 0x666699, 0xA3A3CC }, { 0xDA9147, 0xDE9C59 }, { 0x808080, 0x999999 },
        { 0x8585AD, 0x9D9DBD }, { 0xF0DEF0, 0xF2E1F2 }, { 0xC2C2D6, 0xCECEDE }, { 0xE6B800, 0xCFA600 },
        { 0x008AB8, 0xD6D6FF }, { 0xD5D5D5, 0xAAAAAA }, { 0xCC99FF, 0xDBB8FF }, { 0xEBA1EB, 0xF2C3F2 },
        { 0x62626D, 0x161620 }, { 0xE6E6E6, 0xFFFFCC }, { 0xDADADA, 0xD1D1E0 }, { 0x19AF19, 0x169E16 },
        { 0x335C33, 0x6B8F00 }, { 0x003300, 0x002400 }, };
    public boolean showInList;
    public final Materials material;
    public final int chance;
    private final String name;

    CustomCombs(String pName, boolean show, Materials material, int chance) {
        this.name = pName;
        this.material = material;
        this.chance = chance;
        this.showInList = show;
    }

    public void setHidden() {
        this.showInList = false;
    }

    public String getName() {
        // return "gt.comb."+this.name;
        return GTLanguageManager.addStringLocalization(
            "comb." + this.name,
            this.name.substring(0, 1)
                .toUpperCase() + this.name.substring(1) + " Comb");
    }

    public int[] getColours() {
        return colours[this.ordinal()];
    }
}
