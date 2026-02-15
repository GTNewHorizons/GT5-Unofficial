package bartworks.API.enums;

import bartworks.common.loaders.BioItemList;
import bartworks.util.BioData;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.VoltageIndex;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


// Todo: change the NullBioData rarity later because it's the fallback in ItemLabParts
public enum BioDataEnum {
    BetaLactamase("beta-Lactamase", 0, EnumRarity.uncommon, 100_00, VoltageIndex.HV, ItemList.DNABetaLactamase, ItemList.PlasmidBetaLactamase),
    SaccharomycesCerevisiae("Saccharomyces cerevisiae", 1, EnumRarity.common, 75_00, VoltageIndex.HV, ItemList.DNASaccharomycesCerevisiae, ItemList.PlasmidSaccharomycesCerevisiae),
    NullBioData("", 2, EnumRarity.epic, 75_00, VoltageIndex.HV, ItemList.DNANull, ItemList.PlasmidNull),
    SaccharomycesCerevisiaeVarBayanus("Saccharomyces cerevisiae var bayanus", 3, EnumRarity.uncommon, 75_00, VoltageIndex.HV, ItemList.DNASaccharomycesCerevisiaeVarBayanus, ItemList.PlasmidSaccharomycesCerevisiaeVarBayanus ),
    SaccharomycesCerevisiaeVarCerevisiae("Saccharomyces cerevisiae var cerevisiae", 4, EnumRarity.uncommon, 75_00, VoltageIndex.HV, ItemList.DNASaccharomycesCerevisiaeVarCerevisiae, ItemList.PlasmidSaccharomycesCerevisiaeVarCerevisiae ),
    EscherichiaKoli("Escherichia koli", 5, EnumRarity.uncommon, 100_00, VoltageIndex.HV, ItemList.DNAEscherichiaKoli, ItemList.PlasmidEscherichiaKoli),
    PseudomonasVeronii("Pseudomonas Veronii", 6, EnumRarity.uncommon, 75_00, VoltageIndex.HV, ItemList.DNAPseudomonasVeronii, ItemList.PlasmidPseudomonasVeronii),
    PseudomonasVeronii2("Pseudomonas Veronii", 7, EnumRarity.uncommon, 50_00, VoltageIndex.EV, ItemList.DNAPseudomonasVeronii2, ItemList.PlasmidPseudomonasVeronii2),
    BarnadafisArboriatoris("Barnadafis Arboriatoris", 8, EnumRarity.rare, 7_50, VoltageIndex.IV, ItemList.DNABarnadafisArboriatoris, ItemList.PlasmidBarnadafisArboriatoris),
    TcetieisFucusSerratus("TCetiEis Fucus Serratus", 9, EnumRarity.rare, 7_50, VoltageIndex.IV, ItemList.DNATcetieisFucusSerratus, ItemList.PlasmidTcetieisFucusSerratus),
    Ovumbac("OvumBac", 10, EnumRarity.rare, 15_00, VoltageIndex.IV, ItemList.DNAOvumbac, ItemList.PlasmidOvumbac),
    Stemcellbac("StemCellBac", 11, EnumRarity.rare, 7_50, VoltageIndex.LuV, ItemList.DNAStemcellbac, ItemList.PlasmidStemcellbac),
    Biocellbac("BioCellBac", 12, EnumRarity.epic, 3_00, VoltageIndex.ZPM, ItemList.DNABiocellbac, ItemList.PlasmidBiocellbac),
    Binnigrowthmedium("BinniGrowthMedium", 13, EnumRarity.common, 90_00, VoltageIndex.HV, ItemList.DNABinnigrowthmedium, ItemList.PlasmidBinnigrowthmedium ),
    Binnibacteria("BinniBacteria", 14, EnumRarity.common, 60_00, VoltageIndex.HV, ItemList.DNABinnibacteria, ItemList.PlasmidBinnibacteria),
    Bacterialsludgebac("BacterialSludgeBac", 15, EnumRarity.uncommon, 30_00, VoltageIndex.EV, ItemList.DNABacterialsludgebac, ItemList.PlasmidBacterialsludgebac),
    Mutagen("Mutagen", 16, EnumRarity.rare, 15_00, VoltageIndex.EV, ItemList.DNAMutagen, ItemList.PlasmidMutagen ),


    ;

    public static final Map<String, BioDataEnum> LOOKUPS_BY_NAME = new HashMap<>();
    public static final Map<BioData, BioDataEnum> LOOKUPS_BY_BIODATA = new HashMap<>();
    public static final ArrayList<ItemStack> DNA_SAMPLE_FLASKS = new ArrayList<>();
    public static final ArrayList<ItemStack> PLASMID_CELLS = new ArrayList<>();
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

    public static void registerLoopkups(){
        for (BioDataEnum data : BioDataEnum.values()) {
            LOOKUPS_BY_NAME.put(data.name, data);
            LOOKUPS_BY_BIODATA.put(data.getBioData(), data);
        }
    }

    public static void registerAllDNAItemStacks(){
        for (BioDataEnum data : BioDataEnum.values()){
            ItemStack stack = new ItemStack(BioItemList.vanillaBioLabParts, 1, 1);
            stack.setTagCompound(BioData.getNBTTagFromBioData(data.getBioData()));
            data.DNASampleFlask.set(stack);
        }
    }

    public static void registerAllPlasmidItemStacks(){
        for (BioDataEnum data : BioDataEnum.values()){
            ItemStack stack = new ItemStack(BioItemList.vanillaBioLabParts, 1, 2);
            stack.setTagCompound(BioData.getNBTTagFromBioData(data.getBioData()));
            data.plasmidCell.set(stack);
        }
    }

    public static Collection<ItemStack> getAllDNASampleFlasks() {
        if (!DNA_SAMPLE_FLASKS.isEmpty()) return DNA_SAMPLE_FLASKS;
        for (BioDataEnum data : BioDataEnum.values()) {
            DNA_SAMPLE_FLASKS.add(data.DNASampleFlask.get(1));
        }
        return DNA_SAMPLE_FLASKS;
    }

    public static Collection<ItemStack> getAllPlasmidCells() {
        if (!PLASMID_CELLS.isEmpty()) return PLASMID_CELLS;
        for (BioDataEnum data : BioDataEnum.values()) {
            PLASMID_CELLS.add(data.plasmidCell.get(1));
        }
        return PLASMID_CELLS;
    }

    public static ItemStack getDNASampleFlask(@Nonnull BioData dna) {
        return BioDataEnum.LOOKUPS_BY_BIODATA.getOrDefault(dna, BioDataEnum.NullBioData).DNASampleFlask.get(1);
    }

    public static ItemStack getPlasmidCell(@Nonnull BioData plasmid) {
        return BioDataEnum.LOOKUPS_BY_BIODATA.getOrDefault(plasmid, BioDataEnum.NullBioData).DNASampleFlask.get(1);
    }

    public BioData getBioData(){
        return new BioData(this);
    }
}
