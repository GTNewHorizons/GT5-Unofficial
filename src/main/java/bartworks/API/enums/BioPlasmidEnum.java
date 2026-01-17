package bartworks.API.enums;

import net.minecraft.item.EnumRarity;

public enum BioPlasmidEnum {
    BetaLactamase("beta-Lactamase", 0, EnumRarity.uncommon),
    SaccharomycesCerevisiae("Saccharomyces cerevisiae", 1, EnumRarity.common),
    NullBioPlasmid("", 2, EnumRarity.epic),
    SaccharomycesCerevisiaeVarBayanus("Saccharomyces cerevisiae var bayanus", 3, EnumRarity.uncommon),
    SaccharomycesCerevisiaeVarCerevisiae("Saccharomyces cerevisiae var cerevisiae", 4, EnumRarity.uncommon),
    EscherichiaKoli("Escherichia koli", 5, EnumRarity.uncommon),
    PseudomonasVeronii("Pseudomonas Veronii", 6, EnumRarity.uncommon),
    BarnadafisArboriatoris("Barnadafis Arboriatoris", 8, EnumRarity.rare),
    TcetieisFucusSerratus("TCetiEis Fucus Serratus", 9, EnumRarity.rare),
    Ovumbac("OvumBac", 10, EnumRarity.rare),
    Stemcellbac("StemCellBac", 11, EnumRarity.rare),
    Biocellbac("BioCellBac", 12, EnumRarity.epic),
    Binnigrowthmedium("BinniGrowthMedium", 13, EnumRarity.common),
    Binnibacteria("BinniBacteria", 14, EnumRarity.common),
    Bacterialsludgebac("BacterialSludgeBac", 15, EnumRarity.uncommon),
    Mutagen("Mutagen", 16, EnumRarity.rare),
    ;

    public final String name;
    public final int id;
    public final EnumRarity rarity;

    BioPlasmidEnum(String name, int id, EnumRarity rarity){
        this.name = name;
        this.id = id;
        this.rarity = rarity;
    }
}
