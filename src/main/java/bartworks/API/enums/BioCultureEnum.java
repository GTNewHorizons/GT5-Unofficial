package bartworks.API.enums;

import bartworks.common.loaders.BioItemList;
import bartworks.util.BioCulture;
import bartworks.util.BioData;
import gregtech.api.enums.ItemList;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public enum BioCultureEnum {
    NullBioCulture("", 0, EnumRarity.epic, false, BioDataEnum.NullBioData, BioDataEnum.NullBioData, new Color(0, 0, 255), ItemList.CultureNullBioCulture),
    SaccharomycesCerevisiae("Saccharomyces cerevisiae", 1, EnumRarity.common, true, BioDataEnum.SaccharomycesCerevisiae, BioDataEnum.SaccharomycesCerevisiae, new Color(255, 248, 200), ItemList.CultureSaccharomycesCerevisiae),
    SaccharomycesCerevisiaeVarBayanus("Saccharomyces cerevisiae var bayanus", 2, EnumRarity.uncommon, true, BioDataEnum.SaccharomycesCerevisiaeVarBayanus, BioDataEnum.SaccharomycesCerevisiaeVarBayanus, new Color(255, 248, 200), ItemList.CultureSaccharomycesCerevisiaeVarBayanus),
    SaccharomycesCerevisiaeVarCerevisiae("Saccharomyces cerevisiae var cerevisiae", 3, EnumRarity.uncommon, true, BioDataEnum.SaccharomycesCerevisiaeVarCerevisiae, BioDataEnum.SaccharomycesCerevisiaeVarCerevisiae, new Color(255, 248, 200), ItemList.CultureSaccharomycesCerevisiaeVarCerevisiae),
    EscherichiaCadaver("Escherichia cadaver", 4, EnumRarity.uncommon, false, BioDataEnum.BetaLactamase, BioDataEnum.BetaLactamase, new Color(110, 40, 25), ItemList.CultureEscherichiaCadaver),
    EscherichiaKoli("Escherichia koli", 5, EnumRarity.uncommon, true, BioDataEnum.EscherichiaKoli, BioDataEnum.EscherichiaKoli, new Color(149, 132, 75), ItemList.CultureEscherichiaKoli),
    PseudomonasVeronii("Pseudomonas Veronii", 6, EnumRarity.uncommon, true, BioDataEnum.PseudomonasVeronii2, BioDataEnum.PseudomonasVeronii, new Color(0, 0, 0), ItemList.CulturePseudomonasVeronii),
    SaccharomycesEscherichia("Saccharomyces escherichia", 7, EnumRarity.epic, true, BioDataEnum.EscherichiaKoli, BioDataEnum.SaccharomycesCerevisiae, new Color(127, 69, 26), ItemList.CultureSaccharomycesEscherichia),
    BarnadafisArboriatoris("Barnadafis Arboriatoris", 8, EnumRarity.rare, true, BioDataEnum.BarnadafisArboriatoris, BioDataEnum.BarnadafisArboriatoris, new Color(133, 0, 128), ItemList.CultureBarnadafisArboriatoris),
    TcetieisFucusSerratus("TCetiEis Fucus Serratus", 9, EnumRarity.rare, true, BioDataEnum.TcetieisFucusSerratus, BioDataEnum.TcetieisFucusSerratus, new Color(27, 153, 94), ItemList.CultureTcetieisFucusSerratus),
    XenoxeneXenoxsis("Xenoxene Xenoxsis", 10, EnumRarity.epic, false, BioDataEnum.TcetieisFucusSerratus, BioDataEnum.BarnadafisArboriatoris, new Color(54, 119, 181), ItemList.CultureXenoxeneXenoxsis),
    OvaEvolutionis("Ova Evolutionis", 11, EnumRarity.rare, false, BioDataEnum.Ovumbac, BioDataEnum.Ovumbac, new Color(223, 206, 155), ItemList.CultureOvaEvolutionis),
    DerivanturCellulaEvolutionis("Derivantur Cellula Evolutionis", 12, EnumRarity.rare, false, BioDataEnum.Stemcellbac, BioDataEnum.Stemcellbac, new Color(26, 59, 137), ItemList.CultureDerivanturCellulaEvolutionis),
    CellulaBiologicumEvolutione("Cellula Biologicum Evolutione", 13, EnumRarity.epic, false, BioDataEnum.Biocellbac, BioDataEnum.Biocellbac, new Color(91, 255, 41), ItemList.CultureCellulaBiologicumEvolutione),
    BinniGrowthMedium("Binni Growth Medium", 14, EnumRarity.common, false, BioDataEnum.Binnigrowthmedium, BioDataEnum.Binnigrowthmedium, new Color(219, 223, 138), ItemList.CultureBinniGrowthMedium),
    BinniBacteria("Binni Bacteria", 15, EnumRarity.common, true, BioDataEnum.Binnibacteria, BioDataEnum.Binnibacteria, new Color(209, 181, 129), ItemList.CultureBinniBacteria),
    CorynebacteriumSludgeMarsensis("Corynebacterium Sludge Marsensis", 16, EnumRarity.uncommon, false, BioDataEnum.Bacterialsludgebac, BioDataEnum.Bacterialsludgebac, new Color(10, 62, 13), ItemList.CultureCorynebacteriumSludgeMarsensis),
    MutagenBacteriaASpatio("Mutagen Bacteria a Spatio", 17, EnumRarity.rare, false, BioDataEnum.Mutagen, BioDataEnum.Mutagen, new Color(29, 149, 50), ItemList.CultureMutagenBacteriaASpatio),;

    public static final List<BioCulture> BIO_CULTURES = new ArrayList<>();
    public static final List<ItemStack> BIO_CULTURE_STACKS = new ArrayList<>();
    public static final Map<String, BioCultureEnum> LOOKUPS_BY_NAME = new HashMap<>();
    public static final Map<BioCulture, BioCultureEnum> LOOKUPS_BY_BIODATA = new HashMap<>();

    public final String name;
    public final int id;
    public final EnumRarity rarity;
    public final boolean breedable;
    public final BioDataEnum dna;
    public final BioDataEnum plasmid;
    public final Color color;
    public final ItemList culture;
    public final BioCulture bioCulture;

    BioCultureEnum(String name, int id, EnumRarity rarity, boolean breedable, BioDataEnum dna, BioDataEnum plasmid, Color color, ItemList culture){
        this.name = name;
        this.id = id;
        this.rarity = rarity;
        this.breedable = breedable;
        this.dna = dna;
        this.plasmid = plasmid;
        this.color = color;
        this.culture = culture;
        this.bioCulture = new BioCulture(this);
    }

    public static void registerLoopkups(){
        for (BioCultureEnum culture : BioCultureEnum.values()) {
            LOOKUPS_BY_NAME.put(culture.name, culture);
            LOOKUPS_BY_BIODATA.put(culture.bioCulture, culture);
            BIO_CULTURES.add(culture.bioCulture);
        }
    }

    public static void registerAllCultures(){
        for (BioCultureEnum culture : BioCultureEnum.values()){
            ItemStack stack = new ItemStack(BioItemList.vanillaBioLabParts, 1, 0);
            stack.setTagCompound(BioCulture.getNBTTagFromCulture(culture.bioCulture));
            culture.culture.set(stack);
        }
    }

    public static Collection<ItemStack> getAllPetriDishes() {
        if (!BIO_CULTURE_STACKS.isEmpty()) return BIO_CULTURE_STACKS;
        for (BioCultureEnum data : BioCultureEnum.values()) {
            BIO_CULTURE_STACKS.add(data.culture.get(1));
        }
        return BIO_CULTURE_STACKS;
    }

    public static ItemStack getPetriDish(@Nonnull BioCulture culture) {
        return BioCultureEnum.LOOKUPS_BY_BIODATA.getOrDefault(culture, BioCultureEnum.NullBioCulture).culture.get(1);
    }
}
