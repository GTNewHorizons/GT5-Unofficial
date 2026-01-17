package bartworks.API.enums;

import net.minecraft.item.EnumRarity;

public enum BioCultureEnum {
    NullBioCulture("", 0, EnumRarity.epic, false, BioDataEnum.NullBioData, BioDataEnum.NullBioData),
    SaccharomycesCerevisiae("Saccharomyces cerevisiae", 1, EnumRarity.common, true, BioDataEnum.SaccharomycesCerevisiae, BioDataEnum.SaccharomycesCerevisiae),
    SaccharomycesCerevisiaeVarBayanus("Saccharomyces cerevisiae var bayanus", 2, EnumRarity.uncommon, true, BioDataEnum.SaccharomycesCerevisiaeVarBayanus, BioDataEnum.SaccharomycesCerevisiaeVarBayanus),
    SaccharomycesCerevisiaeVarCerevisiae("Saccharomyces cerevisiae var cerevisiae", 3, EnumRarity.uncommon, true, BioDataEnum.SaccharomycesCerevisiaeVarCerevisiae, BioDataEnum.SaccharomycesCerevisiaeVarCerevisiae),
    EscherichiaCadaver("Escherichia cadaver", 4, EnumRarity.uncommon, false, BioDataEnum.BetaLactamase, BioDataEnum.BetaLactamase),
    EscherichiaKoli("Escherichia koli", 5, EnumRarity.uncommon, true, BioDataEnum.EscherichiaKoli, BioDataEnum.EscherichiaKoli),
    PseudomonasVeronii("Pseudomonas Veronii", 6, EnumRarity.uncommon, true, BioDataEnum.PseudomonasVeronii, BioDataEnum.PseudomonasVeronii),
    SaccharomycesEscherichia("Saccharomyces escherichia", 7, EnumRarity.epic, true, BioDataEnum.EscherichiaKoli, BioDataEnum.SaccharomycesCerevisiae),
    BarnadafisArboriatoris("Barnadafis Arboriatoris", 8, EnumRarity.rare, true, BioDataEnum.BarnadafisArboriatoris, BioDataEnum.BarnadafisArboriatoris),
    TcetieisFucusSerratus("TCetiEis Fucus Serratus", 9, EnumRarity.rare, true, BioDataEnum.TcetieisFucusSerratus, BioDataEnum.TcetieisFucusSerratus),
    XenoxeneXenoxsis("Xenoxene Xenoxsis", 10, EnumRarity.epic, false, BioDataEnum.TcetieisFucusSerratus, BioDataEnum.BarnadafisArboriatoris),
    OvaEvolutionis("Ova Evolutionis", 11, EnumRarity.rare, false, BioDataEnum.Ovumbac, BioDataEnum.Ovumbac),
    DerivanturCellulaEvolutionis("Derivantur Cellula Evolutionis", 12, EnumRarity.rare, false, BioDataEnum.Stemcellbac, BioDataEnum.Stemcellbac),
    CellulaBiologicumEvolutione("Cellula Biologicum Evolutione", 13, EnumRarity.epic, false, BioDataEnum.Biocellbac, BioDataEnum.Biocellbac),
    BinniGrowthMedium("Binni Growth Medium", 14, EnumRarity.common, false, BioDataEnum.Binnigrowthmedium, BioDataEnum.Binnigrowthmedium),
    BinniBacteria("Binni Bacteria", 15, EnumRarity.common, true, BioDataEnum.Binnibacteria, BioDataEnum.Binnibacteria),
    CorynebacteriumSludgeMarsensis("Corynebacterium Sludge Marsensis", 16, EnumRarity.uncommon, false, BioDataEnum.Bacterialsludgebac, BioDataEnum.Bacterialsludgebac),
    MutagenBacteriaASpatio("Mutagen Bacteria a Spatio", 17, EnumRarity.rare, false, BioDataEnum.Mutagen, BioDataEnum.Mutagen),
    ;

    public final String name;
    public final int id;
    public final EnumRarity rarity;
    public final boolean breedable;
    public final BioDataEnum dna;
    public final BioDataEnum plasmid;

    BioCultureEnum(String name, int id, EnumRarity rarity, boolean breedable, BioDataEnum dna, BioDataEnum plasmid){
        this.name = name;
        this.id = id;
        this.rarity = rarity;
        this.breedable = breedable;
        this.dna = dna;
        this.plasmid = plasmid;
    }
}
