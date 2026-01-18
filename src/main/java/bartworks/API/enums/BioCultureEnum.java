package bartworks.API.enums;

import bartworks.util.BioCulture;
import net.minecraft.item.EnumRarity;

import java.awt.Color;

public enum BioCultureEnum {
    NullBioCulture("", 0, EnumRarity.epic, false, BioDataEnum.NullBioData, BioDataEnum.NullBioData, new Color(0, 0, 255)),
    SaccharomycesCerevisiae("Saccharomyces cerevisiae", 1, EnumRarity.common, true, BioDataEnum.SaccharomycesCerevisiae, BioDataEnum.SaccharomycesCerevisiae, new Color(255, 248, 200)),
    SaccharomycesCerevisiaeVarBayanus("Saccharomyces cerevisiae var bayanus", 2, EnumRarity.uncommon, true, BioDataEnum.SaccharomycesCerevisiaeVarBayanus, BioDataEnum.SaccharomycesCerevisiaeVarBayanus, new Color(255, 248, 200)),
    SaccharomycesCerevisiaeVarCerevisiae("Saccharomyces cerevisiae var cerevisiae", 3, EnumRarity.uncommon, true, BioDataEnum.SaccharomycesCerevisiaeVarCerevisiae, BioDataEnum.SaccharomycesCerevisiaeVarCerevisiae, new Color(255, 248, 200)),
    EscherichiaCadaver("Escherichia cadaver", 4, EnumRarity.uncommon, false, BioDataEnum.BetaLactamase, BioDataEnum.BetaLactamase, new Color(110, 40, 25)),
    EscherichiaKoli("Escherichia koli", 5, EnumRarity.uncommon, true, BioDataEnum.EscherichiaKoli, BioDataEnum.EscherichiaKoli, new Color(149, 132, 75)),
    PseudomonasVeronii("Pseudomonas Veronii", 6, EnumRarity.uncommon, true, BioDataEnum.PseudomonasVeronii2, BioDataEnum.PseudomonasVeronii, new Color(0, 0, 0)),
    SaccharomycesEscherichia("Saccharomyces escherichia", 7, EnumRarity.epic, true, BioDataEnum.EscherichiaKoli, BioDataEnum.SaccharomycesCerevisiae, new Color(127, 69, 26)),
    BarnadafisArboriatoris("Barnadafis Arboriatoris", 8, EnumRarity.rare, true, BioDataEnum.BarnadafisArboriatoris, BioDataEnum.BarnadafisArboriatoris, new Color(133, 0, 128)),
    TcetieisFucusSerratus("TCetiEis Fucus Serratus", 9, EnumRarity.rare, true, BioDataEnum.TcetieisFucusSerratus, BioDataEnum.TcetieisFucusSerratus, new Color(27, 153, 94)),
    XenoxeneXenoxsis("Xenoxene Xenoxsis", 10, EnumRarity.epic, false, BioDataEnum.TcetieisFucusSerratus, BioDataEnum.BarnadafisArboriatoris, new Color(54, 119, 181)),
    OvaEvolutionis("Ova Evolutionis", 11, EnumRarity.rare, false, BioDataEnum.Ovumbac, BioDataEnum.Ovumbac, new Color(223, 206, 155)),
    DerivanturCellulaEvolutionis("Derivantur Cellula Evolutionis", 12, EnumRarity.rare, false, BioDataEnum.Stemcellbac, BioDataEnum.Stemcellbac, new Color(26, 59, 137)),
    CellulaBiologicumEvolutione("Cellula Biologicum Evolutione", 13, EnumRarity.epic, false, BioDataEnum.Biocellbac, BioDataEnum.Biocellbac, new Color(91, 255, 41)),
    BinniGrowthMedium("Binni Growth Medium", 14, EnumRarity.common, false, BioDataEnum.Binnigrowthmedium, BioDataEnum.Binnigrowthmedium, new Color(219, 223, 138)),
    BinniBacteria("Binni Bacteria", 15, EnumRarity.common, true, BioDataEnum.Binnibacteria, BioDataEnum.Binnibacteria, new Color(209, 181, 129)),
    CorynebacteriumSludgeMarsensis("Corynebacterium Sludge Marsensis", 16, EnumRarity.uncommon, false, BioDataEnum.Bacterialsludgebac, BioDataEnum.Bacterialsludgebac, new Color(10, 62, 13)),
    MutagenBacteriaASpatio("Mutagen Bacteria a Spatio", 17, EnumRarity.rare, false, BioDataEnum.Mutagen, BioDataEnum.Mutagen, new Color(29, 149, 50)),
;

    public final String name;
    public final int id;
    public final EnumRarity rarity;
    public final boolean breedable;
    public final BioDataEnum dna;
    public final BioDataEnum plasmid;
    public final Color color;

    BioCultureEnum(String name, int id, EnumRarity rarity, boolean breedable, BioDataEnum dna, BioDataEnum plasmid, Color color){
        this.name = name;
        this.id = id;
        this.rarity = rarity;
        this.breedable = breedable;
        this.dna = dna;
        this.plasmid = plasmid;
        this.color = color;
    }

    public BioCulture getBioCulture(){
        return new BioCulture(this);
    }
}
