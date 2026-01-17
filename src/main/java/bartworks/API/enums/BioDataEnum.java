package bartworks.API.enums;

import gregtech.api.enums.VoltageIndex;
import net.minecraft.item.EnumRarity;

public enum BioDataEnum {
    BetaLactamase("beta-Lactamase", 0, EnumRarity.uncommon, 100_00, VoltageIndex.ULV),
    SaccharomycesCerevisiae("Saccharomyces cerevisiae", 1, EnumRarity.common, 75_00, VoltageIndex.ULV),
    NullBioData("", 2, EnumRarity.epic, 75_00, VoltageIndex.ULV),
    SaccharomycesCerevisiaeVarBayanus("Saccharomyces cerevisiae var bayanus", 3, EnumRarity.uncommon, 75_00, VoltageIndex.ULV),
    SaccharomycesCerevisiaeVarCerevisiae("Saccharomyces cerevisiae var cerevisiae", 4, EnumRarity.uncommon, 75_00, VoltageIndex.ULV),
    EscherichiaKoli("Escherichia koli", 5, EnumRarity.uncommon, 100_00, VoltageIndex.ULV),
    PseudomonasVeronii("Pseudomonas Veronii", 6, EnumRarity.uncommon, 75_00, VoltageIndex.ULV),
    PseudomonasVeronii2("Pseudomonas Veronii", 7, EnumRarity.uncommon, 50_00, VoltageIndex.LV),
    BarnadafisArboriatoris("Barnadafis Arboriatoris", 8, EnumRarity.rare, 7_50, VoltageIndex.MV),
    TcetieisFucusSerratus("TCetiEis Fucus Serratus", 9, EnumRarity.rare, 7_50, VoltageIndex.MV),
    Ovumbac("OvumBac", 10, EnumRarity.rare, 15_00, VoltageIndex.MV),
    Stemcellbac("StemCellBac", 11, EnumRarity.rare, 7_50, VoltageIndex.HV),
    Biocellbac("BioCellBac", 12, EnumRarity.epic, 3_00, VoltageIndex.EV),
    Binnigrowthmedium("BinniGrowthMedium", 13, EnumRarity.common, 90_00, VoltageIndex.ULV),
    Binnibacteria("BinniBacteria", 14, EnumRarity.common, 60_00, VoltageIndex.ULV),
    Bacterialsludgebac("BacterialSludgeBac", 15, EnumRarity.uncommon, 30_00, VoltageIndex.LV),
    Mutagen("Mutagen", 16, EnumRarity.rare, 15_00, VoltageIndex.MV),


    ;
    public final String name;
    public final int id;
    public final EnumRarity rarity;
    public final int chance;
    public final int tier;

    BioDataEnum(String name, int id, EnumRarity rarity, int chance, int tier){
        this.name = name;
        this.id = id;
        this.rarity = rarity;
        this.chance = chance;
        this.tier=tier;
    }
}
