package bartworks.API.enums;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.VoltageIndex;
import net.minecraft.item.EnumRarity;

public enum BioDataEnum {
    BetaLactamase("beta-Lactamase", 0, EnumRarity.uncommon, 100_00, VoltageIndex.ULV, ItemList.DNABetaLactamase, ItemList.PlasmidBetaLactamase),
    SaccharomycesCerevisiae("Saccharomyces cerevisiae", 1, EnumRarity.common, 75_00, VoltageIndex.ULV, ItemList.DNASaccharomycesCerevisiae, ItemList.PlasmidSaccharomycesCerevisiae),
    NullBioData("", 2, EnumRarity.epic, 75_00, VoltageIndex.ULV, ItemList.DNANull, ItemList.PlasmidNull),
    SaccharomycesCerevisiaeVarBayanus("Saccharomyces cerevisiae var bayanus", 3, EnumRarity.uncommon, 75_00, VoltageIndex.ULV, ItemList.DNASaccharomycesCerevisiaeVarBayanus, ItemList.PlasmidSaccharomycesCerevisiaeVarBayanus ),
    SaccharomycesCerevisiaeVarCerevisiae("Saccharomyces cerevisiae var cerevisiae", 4, EnumRarity.uncommon, 75_00, VoltageIndex.ULV, ItemList.DNASaccharomycesCerevisiaeVarCerevisiae, ItemList.PlasmidSaccharomycesCerevisiaeVarCerevisiae ),
    EscherichiaKoli("Escherichia koli", 5, EnumRarity.uncommon, 100_00, VoltageIndex.ULV, ItemList.DNAEscherichiaKoli, ItemList.PlasmidEscherichiaKoli),
    PseudomonasVeronii("Pseudomonas Veronii", 6, EnumRarity.uncommon, 75_00, VoltageIndex.ULV, ItemList.DNAPseudomonasVeronii, ItemList.PlasmidPseudomonasVeronii),
    PseudomonasVeronii2("Pseudomonas Veronii", 7, EnumRarity.uncommon, 50_00, VoltageIndex.LV, ItemList.DNAPseudomonasVeronii2, ItemList.PlasmidPseudomonasVeronii2),
    BarnadafisArboriatoris("Barnadafis Arboriatoris", 8, EnumRarity.rare, 7_50, VoltageIndex.MV, ItemList.DNABarnadafisArboriatoris, ItemList.PlasmidBarnadafisArboriatoris),
    TcetieisFucusSerratus("TCetiEis Fucus Serratus", 9, EnumRarity.rare, 7_50, VoltageIndex.MV, ItemList.DNATcetieisFucusSerratus, ItemList.PlasmidTcetieisFucusSerratus),
    Ovumbac("OvumBac", 10, EnumRarity.rare, 15_00, VoltageIndex.MV, ItemList.DNAOvumbac, ItemList.PlasmidOvumbac),
    Stemcellbac("StemCellBac", 11, EnumRarity.rare, 7_50, VoltageIndex.HV, ItemList.DNAStemcellbac, ItemList.PlasmidStemcellbac),
    Biocellbac("BioCellBac", 12, EnumRarity.epic, 3_00, VoltageIndex.EV, ItemList.DNABiocellbac, ItemList.PlasmidBiocellbac),
    Binnigrowthmedium("BinniGrowthMedium", 13, EnumRarity.common, 90_00, VoltageIndex.ULV, ItemList.DNABinnigrowthmedium, ItemList.PlasmidBinnigrowthmedium ),
    Binnibacteria("BinniBacteria", 14, EnumRarity.common, 60_00, VoltageIndex.ULV, ItemList.DNABinnibacteria, ItemList.PlasmidBinnibacteria),
    Bacterialsludgebac("BacterialSludgeBac", 15, EnumRarity.uncommon, 30_00, VoltageIndex.LV, ItemList.DNABacterialsludgebac, ItemList.PlasmidBacterialsludgebac),
    Mutagen("Mutagen", 16, EnumRarity.rare, 15_00, VoltageIndex.MV, ItemList.DNAMutagen, ItemList.PlasmidMutagen ),


    ;
    public final String name;
    public final int id;
    public final EnumRarity rarity;
    public final int chance;
    public final int tier;
    public final ItemList DNASampleFlask;
    public final ItemList plasmidCell;

    BioDataEnum(String name, int id, EnumRarity rarity, int chance, int tier, ItemList DNASampleFlask, ItemList plasmidCell){
        this.name = name;
        this.id = id;
        this.rarity = rarity;
        this.chance = chance;
        this.tier=tier;
        this.DNASampleFlask = DNASampleFlask;
        this.plasmidCell = plasmidCell;
    }
}
