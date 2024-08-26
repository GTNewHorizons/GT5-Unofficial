package com.elisis.gtnhlanth.common.item;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;

public enum MaskList {

    // There are absolutely better ways of doing this than a GT Materials-esque Enum, some method of automatically
    // scraping the wafer types would be preferable in particular
    // Use Dyes._NULL to indicate a wafer's lack of a dedicated lens instead of null, if the wafer's mask is to be
    // generated
    // Ignore last argument if using all wafers
    ERROR("error", "ERROR", 0, "", null, null, 0, 0, 0, 0, 0, null),
    BLANK1("blank1", "T1 Blank", 0, "VISIBLE", null, null, 0, 0, 0, 0, 0, null),
    BLANK2("blank2", "T2 Blank", 0, "UV", null, null, 0, 0, 0, 0, 0, null),
    BLANK3("blank3", "T3 Blank", 0, "X-RAY", null, null, 0, 0, 0, 0, 0, null),
    ILC("ilc", "Integrated Logic Circuit", 100, "", BLANK1, Dyes.dyeRed, TierEU.RECIPE_MV, 0.5e-3f, 4e-3f, 35, 1,
        ItemList.Circuit_Wafer_ILC.get(1)),
    RAM("ram", "Random Access Memory", 200, "", BLANK1, Dyes.dyeCyan, TierEU.RECIPE_MV, 2e-3f, 4e-3f, 40, 2,
        ItemList.Circuit_Wafer_Ram.get(1), ItemList.Circuit_Silicon_Wafer),
    NAND("nand", "NAND", 200, "", BLANK2, Dyes._NULL, TierEU.RECIPE_HV, 7e-3f, 12e-3f, 40, 1,
        ItemList.Circuit_Wafer_NAND.get(1), ItemList.Circuit_Silicon_Wafer), // NAND uses only Ender Pearl lens, don't
                                                                             // ask me why
    NOR("nor", "NOR", 100, "", BLANK2, Dyes._NULL, TierEU.RECIPE_LuV, 8e-3f, 10e-3f, 40, 1,
        ItemList.Circuit_Wafer_NOR.get(1), ItemList.Circuit_Silicon_Wafer, ItemList.Circuit_Silicon_Wafer2), // Same as
                                                                                                             // above,
                                                                                                             // but with
                                                                                                             // ender
                                                                                                             // eye
    CPU("cpu", "Central Processing Unit", 10, "", BLANK2, Dyes.dyeWhite, TierEU.RECIPE_MV, 6e-3f, 12e-3f, 45, 2,
        ItemList.Circuit_Wafer_CPU.get(1)),
    SOC("soc", "SoC", 150, "", BLANK2, Dyes.dyeYellow, TierEU.RECIPE_EV, 3e-3f, 10e-3f, 45, 2,
        ItemList.Circuit_Wafer_SoC.get(1), ItemList.Circuit_Silicon_Wafer, ItemList.Circuit_Silicon_Wafer2),
    ASOC("asoc", "Advanced SoC", 120, "", BLANK2, Dyes.dyeGreen, TierEU.RECIPE_EV, 100e-3f, 200e-3f, 50, 2,
        ItemList.Circuit_Wafer_SoC2.get(1), ItemList.Circuit_Silicon_Wafer, ItemList.Circuit_Silicon_Wafer2),
    PIC("pic", "Power IC", 100, "", BLANK2, Dyes.dyeBlue, TierEU.RECIPE_HV, 5e-3f, 10e-3f, 50, 4,
        ItemList.Circuit_Wafer_PIC.get(1), ItemList.Circuit_Silicon_Wafer),
    HPIC("hpic", "High Power IC", 80, "", BLANK3, null, TierEU.RECIPE_IV, 100e-3f, 200e-3f, 50, 6,
        ItemList.Circuit_Wafer_HPIC.get(1), ItemList.Circuit_Silicon_Wafer), // Different, made in chemical reactor.
                                                                             // Figure out something for
    // this later?
    NCPU("ncpu", "NanoCPU", 60, "", BLANK2, null, TierEU.RECIPE_EV, 5e-3f, 10e-3f, 50, 4,
        ItemList.Circuit_Wafer_NanoCPU.get(1), ItemList.Circuit_Silicon_Wafer), // Same as above
    QBIT("qbit", "QBit", 50, "", BLANK2, null, TierEU.RECIPE_EV, 3e-3f, 10e-3f, 50, 4,
        ItemList.Circuit_Wafer_QuantumCPU.get(1), ItemList.Circuit_Silicon_Wafer), // ^
    UHPIC("uhpic", "Ultra High Power IC", 60, "", BLANK3, null, TierEU.RECIPE_LuV, 200e-3f, 400e-3f, 50, 8,
        ItemList.Circuit_Wafer_UHPIC.get(1), ItemList.Circuit_Silicon_Wafer, ItemList.Circuit_Silicon_Wafer2), // You
                                                                                                               // get
                                                                                                               // the
                                                                                                               // gist
    SSOC("ssoc", "Simple SoC", 150, "", BLANK1, Dyes.dyeOrange, TierEU.RECIPE_MV, 2e-3f, 4e-3f, 25, 1,
        ItemList.Circuit_Wafer_Simple_SoC.get(1)),
    ULPIC("ulpic", "Ultra Low Power IC", 200, "", BLANK1, Dyes.dyeGreen, TierEU.RECIPE_LV, 2e-3f, 4e-3f, 30, 1,
        ItemList.Circuit_Wafer_ULPIC.get(1)), // Can use green for this as well as asoc, given
    // the latter uses a different base mask
    LPIC("lpic", "Low Power IC", 150, "", BLANK1, Dyes.dyeYellow, TierEU.RECIPE_MV, 2e-3f, 4e-3f, 30, 2,
        ItemList.Circuit_Wafer_LPIC.get(1)), // Same as above, except for yellow
    NPIC("npic", "Nano Power IC", 70, "", BLANK3, Dyes.dyeRed, TierEU.RECIPE_LuV, 1, 100000, 50, 4,
        ItemList.Circuit_Wafer_NPIC.get(1), ItemList.Circuit_Silicon_Wafer, ItemList.Circuit_Silicon_Wafer2,
        ItemList.Circuit_Silicon_Wafer3), // Same
    PPIC("ppic", "PPIC", 50, "", BLANK3, null, TierEU.RECIPE_ZPM, 10, 15, 50, 6, ItemList.Circuit_Wafer_PPIC.get(1),
        ItemList.Circuit_Silicon_Wafer, ItemList.Circuit_Silicon_Wafer2, ItemList.Circuit_Silicon_Wafer3), // CR
                                                                                                           // recipe
    QPIC("qpic", "QPIC", 50, "", BLANK3, Dyes.dyeBlue, TierEU.RECIPE_UV, 5, 9, 50, 6,
        ItemList.Circuit_Wafer_QPIC.get(1), ItemList.Circuit_Silicon_Wafer, ItemList.Circuit_Silicon_Wafer2,
        ItemList.Circuit_Silicon_Wafer3, ItemList.Circuit_Silicon_Wafer4); // Different base mask to PIC

    String name;
    String englishName;
    String spectrum;

    int maxDamage;

    MaskList precursor;
    Dyes lensColour;

    long engraverEUt;

    float minEnergy;
    float maxEnergy;

    float minFocus;
    int baselineAmount;

    ItemStack producedItem;

    ItemList[] forbiddenWafers;

    MaskList(String name, String englishName, int maxDamage, String spectrum, MaskList precursor, Dyes lensColour,
        long engraverEUt, float minEnergy, float maxEnergy, float minFocus, int baselineAmount, ItemStack producedItem,
        ItemList... forbiddenWafers) {
        this.name = name;
        this.englishName = englishName;
        this.spectrum = spectrum;
        this.maxDamage = maxDamage;
        this.precursor = precursor;
        this.lensColour = lensColour;
        this.engraverEUt = engraverEUt;
        this.minFocus = minFocus;
        this.minEnergy = minEnergy;
        this.maxEnergy = maxEnergy;
        this.baselineAmount = baselineAmount;
        this.producedItem = producedItem;
        this.forbiddenWafers = forbiddenWafers;
    }

    public String getName() {
        return this.name;
    }

    public String getEnglishName() {
        return this.englishName;
    }

    public String getSpectrum() {
        return this.spectrum;
    }

    public int getDamage() {
        return this.maxDamage;
    }

    public MaskList getPrecursor() {
        return this.precursor;
    }

    public Dyes getLensColour() {
        return this.lensColour;
    }

    public long getEngraverEUt() {
        return this.engraverEUt;
    }

    public float getMinEnergy() {
        return this.minEnergy;
    }

    public float getMaxEnergy() {
        return this.maxEnergy;
    }

    public float getMinFocus() {
        return this.minFocus;
    }

    public int getBaselineAmount() {
        return this.baselineAmount;
    }

    public ItemStack getProducedItem() {
        return this.producedItem;
    }

    public ItemList[] getForbiddenWafers() {
        return this.forbiddenWafers;
    }

}
