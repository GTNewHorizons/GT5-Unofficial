package gregtech.loaders.postload;

/// Save migration table for gtPlusPlus ore blocks, one row per legacy `BlockBaseOre`. See [PosteaTransformers] for
/// the migration.
///
/// Rows are never removed. A missing row orphans that block in existing worlds.
public final class GtppOreCutoverTable {

    public record Entry(String unlocalizedName, String registryName) {}

    private GtppOreCutoverTable() {}

    // spotless:off
    public static final Entry[] ENTRIES = new Entry[] {
        new Entry("AgarditeCd", "miscutils:oreAgarditeCd"),
        new Entry("AgarditeLa", "miscutils:oreAgarditeLa"),
        new Entry("AgarditeNd", "miscutils:oreAgarditeNd"),
        new Entry("AgarditeY", "miscutils:oreAgarditeY"),
        new Entry("Alburnite", "miscutils:oreAlburnite"),
        new Entry("AncientGranite", "miscutils:oreAncientGranite"),
        new Entry("BariteRa", "miscutils:oreBariteRa"),
        new Entry("Cerite", "miscutils:oreCerite"),
        new Entry("Comancheite", "miscutils:oreComancheite"),
        new Entry("Crocoite", "miscutils:oreCrocoite"),
        new Entry("CryoliteF", "miscutils:oreCryoliteF"),
        new Entry("CryoliteF", "miscutils:oreCryolite"),
        new Entry("DemicheleiteBr", "miscutils:oreDemicheleiteBr"),
        new Entry("Florencite", "miscutils:oreFlorencite"),
        new Entry("Fluorcaphite", "miscutils:oreFluorcaphite"),
        new Entry("FluoriteF", "miscutils:oreFluoriteF"),
        new Entry("FluoriteF", "miscutils:oreFluorite"),
        new Entry("GadoliniteCe", "miscutils:oreGadoliniteCe"),
        new Entry("GadoliniteY", "miscutils:oreGadoliniteY"),
        new Entry("Geikielite", "miscutils:oreGeikielite"),
        new Entry("Greenockite", "miscutils:oreGreenockite"),
        new Entry("Hibonite", "miscutils:oreHibonite"),
        new Entry("Honeaite", "miscutils:oreHoneaite"),
        new Entry("Irarsite", "miscutils:oreIrarsite"),
        new Entry("Kashinite", "miscutils:oreKashinite"),
        new Entry("Koboldite", "miscutils:oreKoboldite"),
        new Entry("Lafossaite", "miscutils:oreLafossaite"),
        new Entry("LanthaniteCe", "miscutils:oreLanthaniteCe"),
        new Entry("LanthaniteLa", "miscutils:oreLanthaniteLa"),
        new Entry("LanthaniteNd", "miscutils:oreLanthaniteNd"),
        new Entry("Lautarite", "miscutils:oreLautarite"),
        new Entry("Lepersonnite", "miscutils:oreLepersonnite"),
        new Entry("Miessiite", "miscutils:oreMiessiite"),
        new Entry("Nichromite", "miscutils:oreNichromite"),
        new Entry("Perroudite", "miscutils:orePerroudite"),
        new Entry("Polycrase", "miscutils:orePolycrase"),
        new Entry("RadioactiveMineralMix", "miscutils:oreRadioactiveMineralMix"),
        new Entry("RareEarthI", "miscutils:oreRareEarthI"),
        new Entry("RareEarthII", "miscutils:oreRareEarthII"),
        new Entry("RareEarthIII", "miscutils:oreRareEarthIII"),
        new Entry("Runite", "miscutils:oreRunite"),
        new Entry("SamarskiteY", "miscutils:oreSamarskiteY"),
        new Entry("SamarskiteYb", "miscutils:oreSamarskiteYb"),
        new Entry("Titanite", "miscutils:oreTitanite"),
        new Entry("Xenotime", "miscutils:oreXenotime"),
        new Entry("Yttriaite", "miscutils:oreYttriaite"),
        new Entry("Yttrialite", "miscutils:oreYttrialite"),
        new Entry("Yttrocerite", "miscutils:oreYttrocerite"),
        new Entry("Zimbabweite", "miscutils:oreZimbabweite"),
        new Entry("Zircon", "miscutils:oreZircon"),
        new Entry("Zirconolite", "miscutils:oreZirconolite"),
        new Entry("Zircophyllite", "miscutils:oreZircophyllite"),
        new Entry("Zirkelite", "miscutils:oreZirkelite"),
    };
    // spotless:on
}
