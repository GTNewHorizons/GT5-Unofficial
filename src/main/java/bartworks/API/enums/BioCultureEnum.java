package bartworks.API.enums;

import net.minecraft.item.EnumRarity;

public enum BioCultureEnum {
    NullBioCulture("", 0, EnumRarity.epic, false, BioDNAEnum.NullBioDNA, BioPlasmidEnum.NullBioPlasmid),
    SaccharomycesCerevisiae("Saccharomyces cerevisiae", 1, EnumRarity.common, true, BioDNAEnum.SaccharomycesCerevisiae, BioPlasmidEnum.SaccharomycesCerevisiae),
    SaccharomycesCerevisiaeVarBayanus("Saccharomyces cerevisiae var bayanus", 2, EnumRarity.uncommon, true, BioDNAEnum.SaccharomycesCerevisiaeVarBayanus, BioPlasmidEnum.SaccharomycesCerevisiaeVarBayanus),
    SaccharomycesCerevisiaeVarCerevisiae("Saccharomyces cerevisiae var cerevisiae", 3, EnumRarity.uncommon, true, BioDNAEnum.SaccharomycesCerevisiaeVarCerevisiae, BioPlasmidEnum.SaccharomycesCerevisiaeVarCerevisiae),
    EscherichiaCadaver("Escherichia cadaver", 4, EnumRarity.uncommon, false, BioDNAEnum.BetaLactamase, BioPlasmidEnum.BetaLactamase),
    EscherichiaKoli("Escherichia koli", 5, EnumRarity.uncommon, true, BioDNAEnum.EscherichiaKoli, BioPlasmidEnum.EscherichiaKoli),
    PseudomonasVeronii("Pseudomonas Veronii", 6, EnumRarity.uncommon, true, BioDNAEnum.PseudomonasVeronii, BioPlasmidEnum.PseudomonasVeronii),
    SaccharomycesEscherichia("Saccharomyces escherichia", 7, EnumRarity.epic, true, BioDNAEnum.EscherichiaKoli, BioPlasmidEnum.SaccharomycesCerevisiae),
    BarnadafisArboriatoris("Barnadafis Arboriatoris", 8, EnumRarity.rare, true, BioDNAEnum.BarnadafisArboriatoris, BioPlasmidEnum.BarnadafisArboriatoris),
    TcetieisFucusSerratus("TCetiEis Fucus Serratus", 9, EnumRarity.rare, true, BioDNAEnum.TcetieisFucusSerratus, BioPlasmidEnum.TcetieisFucusSerratus),
    XenoxeneXenoxsis("Xenoxene Xenoxsis", 10, EnumRarity.epic, false, BioDNAEnum.TcetieisFucusSerratus, BioPlasmidEnum.BarnadafisArboriatoris),
    OvaEvolutionis("Ova Evolutionis", 11, EnumRarity.rare, false, BioDNAEnum.Ovumbac, BioPlasmidEnum.Ovumbac),
    DerivanturCellulaEvolutionis("Derivantur Cellula Evolutionis", 12, EnumRarity.rare, false, BioDNAEnum.Stemcellbac, BioPlasmidEnum.Stemcellbac),
    CellulaBiologicumEvolutione("Cellula Biologicum Evolutione", 13, EnumRarity.epic, false, BioDNAEnum.Biocellbac, BioPlasmidEnum.Biocellbac),
    BinniGrowthMedium("Binni Growth Medium", 14, EnumRarity.common, false, BioDNAEnum.Binnigrowthmedium, BioPlasmidEnum.Binnigrowthmedium),
    BinniBacteria("Binni Bacteria", 15, EnumRarity.common, true, BioDNAEnum.Binnibacteria, BioPlasmidEnum.Binnibacteria),
    CorynebacteriumSludgeMarsensis("Corynebacterium Sludge Marsensis", 16, EnumRarity.uncommon, false, BioDNAEnum.Bacterialsludgebac, BioPlasmidEnum.Bacterialsludgebac),
    MutagenBacteriaASpatio("Mutagen Bacteria a Spatio", 17, EnumRarity.rare, false, BioDNAEnum.Mutagen, BioPlasmidEnum.Mutagen),
    ;

    public final String name;
    public final int id;
    public final EnumRarity rarity;
    public final boolean breedable;
    public final BioDNAEnum dna;
    public final BioPlasmidEnum plasmid;

    BioCultureEnum(String name, int id, EnumRarity rarity, boolean breedable, BioDNAEnum dna, BioPlasmidEnum plasmid){
        this.name = name;
        this.id = id;
        this.rarity = rarity;
        this.breedable = breedable;
        this.dna = dna;
        this.plasmid = plasmid;
    }
}
