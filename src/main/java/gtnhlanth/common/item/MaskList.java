package gtnhlanth.common.item;

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
    // tcTargetItem specifies the target item used for non-wafer mask+TC recipes, e.g. crystal chips, set to null for
    // wafer masks
    ERROR("error", "ERROR", 0, "", null, null, 0, 0, 0, 0, 0, null, null),
    BLANK1("blank1", "T1 Blank", 0, "VISIBLE", null, null, 0, 0, 0, 0, 0, null, null),
    BLANK2("blank2", "T2 Blank", 0, "UV", null, null, 0, 0, 0, 0, 0, null, null),
    BLANK3("blank3", "T3 Blank", 0, "X-RAY", null, null, 0, 0, 0, 0, 0, null, null),

    CBLANK("cblank", "Crystal", 0, "X-RAY", null, null, 0, 0, 0, 0, 0, null, null), // Better at resolving smaller
                                                                                    // features, in theory

    ILC("ilc", "Integrated Logic Circuit", 100, "", BLANK1, Dyes.dyeRed, TierEU.RECIPE_MV, 0.5e-3f, 4e-3f, 35, 1,
        ItemList.Circuit_Wafer_ILC.get(1), null),
    RAM("ram", "Random Access Memory", 200, "", BLANK1, Dyes.dyeCyan, TierEU.RECIPE_MV, 2e-3f, 4e-3f, 40, 2,
        ItemList.Circuit_Wafer_Ram.get(1), null, ItemList.Circuit_Silicon_Wafer),
    NAND("nand", "NAND", 200, "", BLANK2, Dyes._NULL, TierEU.RECIPE_HV, 7e-3f, 12e-3f, 40, 1,
        ItemList.Circuit_Wafer_NAND.get(1), null, ItemList.Circuit_Silicon_Wafer), // NAND uses only Ender Pearl
                                                                                   // lens,
                                                                                   // don't
    // ask me why
    NOR("nor", "NOR", 100, "", BLANK2, Dyes._NULL, TierEU.RECIPE_HV, 8e-3f, 10e-3f, 40, 1,
        ItemList.Circuit_Wafer_NOR.get(1), null, ItemList.Circuit_Silicon_Wafer, ItemList.Circuit_Silicon_Wafer2), // Same
                                                                                                                   // as
    // above,
    // but with
    // ender
    // eye
    CPU("cpu", "Central Processing Unit", 100, "", BLANK2, Dyes.dyeWhite, TierEU.RECIPE_MV, 6e-3f, 12e-3f, 45, 2,
        ItemList.Circuit_Wafer_CPU.get(1), null),
    PrCPU("prcpu", "Prepared Central Processing Unit", 0, "", CPU, null, TierEU.RECIPE_EV, 0, 0, 0, 0, null, null),

    SOC("soc", "SoC", 150, "", BLANK2, Dyes.dyeYellow, TierEU.RECIPE_EV, 3e-3f, 10e-3f, 45, 2,
        ItemList.Circuit_Wafer_SoC.get(1), null, ItemList.Circuit_Silicon_Wafer, ItemList.Circuit_Silicon_Wafer2),
    ASOC("asoc", "Advanced SoC", 120, "", BLANK2, Dyes.dyeGreen, TierEU.RECIPE_EV, 100e-3f, 200e-3f, 50, 2,
        ItemList.Circuit_Wafer_SoC2.get(1), null, ItemList.Circuit_Silicon_Wafer, ItemList.Circuit_Silicon_Wafer2),

    PIC("pic", "Power IC", 100, "", BLANK2, Dyes.dyeBlue, TierEU.RECIPE_HV, 5e-3f, 10e-3f, 50, 4,
        ItemList.Circuit_Wafer_PIC.get(1), null, ItemList.Circuit_Silicon_Wafer),
    PrPIC("prpic", "Prepared Power IC", 0, "", PIC, null, TierEU.RECIPE_IV, 0, 0, 0, 0, null, null), // Made in CR from
                                                                                                     // PIC

    HPIC("hpic", "High Power IC", 80, "", PrPIC, Dyes.dyeBlue, TierEU.RECIPE_IV, 100e-3f, 200e-3f, 50, 6,
        ItemList.Circuit_Wafer_HPIC.get(1), null, ItemList.Circuit_Silicon_Wafer),
    PrHPIC("prhpic", "Prepared High Power IC", 0, "", HPIC, null, TierEU.RECIPE_LuV, 0, 0, 0, 0, null, null), // Made in
                                                                                                              // CR from
                                                                                                              // HPIC

    NCPU("ncpu", "NanoCPU", 60, "", PrCPU, Dyes.dyeWhite, TierEU.RECIPE_EV, 5e-3f, 10e-3f, 50, 4,
        ItemList.Circuit_Wafer_NanoCPU.get(1), null, ItemList.Circuit_Silicon_Wafer),
    PrNCPU("prncpu", "Prepared NanoCPU", 0, "", NCPU, null, TierEU.RECIPE_EV, 0, 0, 0, 0, null, null),

    QBIT("qbit", "QBit", 50, "", PrNCPU, Dyes.dyeWhite, TierEU.RECIPE_EV, 3e-3f, 10e-3f, 50, 4,
        ItemList.Circuit_Wafer_QuantumCPU.get(1), null, ItemList.Circuit_Silicon_Wafer), // ^
    UHPIC("uhpic", "Ultra High Power IC", 60, "", PrHPIC, Dyes.dyeBlue, TierEU.RECIPE_LuV, 190e-3f, 400e-3f, 50, 8,
        ItemList.Circuit_Wafer_UHPIC.get(1), null, ItemList.Circuit_Silicon_Wafer, ItemList.Circuit_Silicon_Wafer2),
    SSOC("ssoc", "Simple SoC", 150, "", BLANK1, Dyes.dyeOrange, TierEU.RECIPE_MV, 2e-3f, 4e-3f, 25, 1,
        ItemList.Circuit_Wafer_Simple_SoC.get(1), null),
    ULPIC("ulpic", "Ultra Low Power IC", 200, "", BLANK1, Dyes.dyeGreen, TierEU.RECIPE_LV, 2e-3f, 4e-3f, 30, 1,
        ItemList.Circuit_Wafer_ULPIC.get(1), null), // Can use green for this as well as asoc, given
    // the latter uses a different base mask
    LPIC("lpic", "Low Power IC", 150, "", BLANK1, Dyes.dyeYellow, TierEU.RECIPE_MV, 2e-3f, 4e-3f, 30, 2,
        ItemList.Circuit_Wafer_LPIC.get(1), null), // Same as above, except for yellow

    NPIC("npic", "Nano Power IC", 70, "", BLANK2, Dyes.dyeRed, TierEU.RECIPE_LuV, 1, 4, 50, 4,
        ItemList.Circuit_Wafer_NPIC.get(1), null, ItemList.Circuit_Silicon_Wafer, ItemList.Circuit_Silicon_Wafer2,
        ItemList.Circuit_Silicon_Wafer3), // Same
    PrNPIC("prnpic", "Prepared Nano Power IC", 0, "", NPIC, null, TierEU.RECIPE_ZPM, 0, 0, 0, 0, null, null), // Made in
                                                                                                              // CR from
                                                                                                              // NPIC

    PPIC("ppic", "PPIC", 50, "", PrNPIC, Dyes.dyeRed, TierEU.RECIPE_ZPM, 1.5f, 10, 50, 6,
        ItemList.Circuit_Wafer_PPIC.get(1), null, ItemList.Circuit_Silicon_Wafer, ItemList.Circuit_Silicon_Wafer2,
        ItemList.Circuit_Silicon_Wafer3),

    QPIC("qpic", "QPIC", 50, "", BLANK3, Dyes.dyeBlue, TierEU.RECIPE_UV, 3.2f, 9, 50, 6,
        ItemList.Circuit_Wafer_QPIC.get(1), null, ItemList.Circuit_Silicon_Wafer, ItemList.Circuit_Silicon_Wafer2,
        ItemList.Circuit_Silicon_Wafer3, ItemList.Circuit_Silicon_Wafer4), // Different base mask to PIC

    CCPU("ccpu", "Crystal Central Processing Unit", 100, "", CBLANK, Dyes.dyeGreen, 10_000, 1, 3, 50, 6,
        ItemList.Circuit_Chip_CrystalCPU.get(1), ItemList.Circuit_Parts_Crystal_Chip_Elite.get(1)), // For producing
                                                                                                    // Crystal CPUs
                                                                                                    // from
                                                                                                    // Engraved CCs
    CSOC("csoc", "Crystal SoC", 100, "", CBLANK, Dyes.dyeBlue, 40_000, 2, 7, 50, 8,
        ItemList.Circuit_Chip_CrystalSoC.get(1), ItemList.Circuit_Chip_CrystalCPU.get(1)),
    ACC("acc", "Advanced Crystal Chip", 100, "", CBLANK, Dyes.dyeLime, 80_000, 3, 9, 55, 12,
        ItemList.Circuit_Chip_CrystalSoC2.get(1), ItemList.Circuit_Chip_CrystalSoC.get(1)),
    LCC("lcc", "Living Crystal Chip", 75, "", CBLANK, Dyes.dyeWhite, 160_000, 5, 12, 60, 16,
        ItemList.Circuit_Parts_Crystal_Chip_Wetware.get(1), ItemList.Circuit_Chip_CrystalSoC2.get(1))

    ;

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

    ItemStack tcTargetItem;
    ItemStack producedItem;

    ItemList[] forbiddenWafers;

    MaskList(String name, String englishName, int maxUses, String spectrum, MaskList precursor, Dyes lensColour,
        long engraverEUt, float minEnergy, float maxEnergy, float minFocus, int baselineAmount, ItemStack producedItem,
        ItemStack tcTargetItem, ItemList... forbiddenWafers) {
        this.name = name;
        this.englishName = englishName;
        this.spectrum = spectrum;
        this.maxDamage = maxUses - 1; // 0-durability masks still function, so e.g. maxUses = 100 corresponds to
                                      // durability levels 0-99
        this.precursor = precursor;
        this.lensColour = lensColour;
        this.engraverEUt = engraverEUt;
        this.minFocus = minFocus;
        this.minEnergy = minEnergy;
        this.maxEnergy = maxEnergy;
        this.baselineAmount = baselineAmount;
        this.tcTargetItem = tcTargetItem;
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

    public ItemStack getTCTargetItem() {
        return this.tcTargetItem;
    }

    public ItemStack getProducedItem() {
        return this.producedItem;
    }

    public ItemList[] getForbiddenWafers() {
        return this.forbiddenWafers;
    }

}
