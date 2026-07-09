package gtnhlanth.common.register;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffReconstruction;

public class WerkstoffMaterialPool implements Runnable {

    // Current highest ID = 11_499

    private static final int offsetID = 11_000;
    private static final int offsetID2 = 11_100;
    private static final int offsetID3 = 11_300;
    private static final int offsetID3b = 11_350;
    private static final int offsetID4 = 11_400;
    private static final int offsetID5 = 11_460;

    /*
     * public static final Werkstoff __ = new Werkstoff( new short[] {_, _, _}, "__", new Werkstoff.Stats(),
     * Werkstoff.Types.MIXTURE, new Werkstoff.GenerationFeatures().disable(), offsetID_, TextureSet.SET_DULL );
     */

    // Misc.
    public static final Werkstoff Hafnium = WerkstoffReconstruction.byId(11000);

    public static final Werkstoff LowPurityHafnium = WerkstoffReconstruction.byId(11001);

    public static final Werkstoff Hafnia = WerkstoffReconstruction.byId(11002);

    public static final Werkstoff HafniumTetrachloride = WerkstoffReconstruction.byId(11003);

    public static final Werkstoff HafniumTetrachlorideSolution = WerkstoffReconstruction.byId(11004);

    public static final Werkstoff HafniumIodide = WerkstoffReconstruction.byId(11005);

    public static final Werkstoff HafniumRunoff = WerkstoffReconstruction.byId(11006);

    public static final Werkstoff Zirconia = WerkstoffReconstruction.byId(11008);

    public static final Werkstoff ZirconiumTetrachloride = WerkstoffReconstruction.byId(11009);

    public static final Werkstoff ZirconiumTetrachlorideSolution = WerkstoffReconstruction.byId(11010);

    public static final Werkstoff HafniaZirconiaBlend = WerkstoffReconstruction.byId(11011);

    public static final Werkstoff Iodine = WerkstoffReconstruction.byId(11012);

    // Lanthanide Line
    public static final Werkstoff MuddyRareEarthMonaziteSolution = WerkstoffReconstruction.byId(11014);

    public static final Werkstoff DilutedRareEarthMonaziteMud = WerkstoffReconstruction.byId(11015);

    public static final Werkstoff DilutedMonaziteSulfate = WerkstoffReconstruction.byId(11016);

    public static final Werkstoff NitratedRareEarthMonaziteConcentrate = WerkstoffReconstruction.byId(11017);

    public static final Werkstoff NitricMonaziteLeachedConcentrate = WerkstoffReconstruction.byId(11018);

    public static final Werkstoff MonaziteSulfate = WerkstoffReconstruction.byId(11019);

    public static final Werkstoff AcidicMonazitePowder = WerkstoffReconstruction.byId(11020);

    public static final Werkstoff MonaziteRareEarthFiltrate = WerkstoffReconstruction.byId(11021);

    public static final Werkstoff NeutralizedMonaziteRareEarthFiltrate = WerkstoffReconstruction.byId(11022);

    public static final Werkstoff MonaziteRareEarthHydroxideConcentrate = WerkstoffReconstruction.byId(11023);

    public static final Werkstoff DriedMonaziteRareEarthConcentrate = WerkstoffReconstruction.byId(11024);

    public static final Werkstoff CeriumDioxide = WerkstoffReconstruction.byId(11025);

    public static final Werkstoff CeriumChloride = WerkstoffReconstruction.byId(11026);

    public static final Werkstoff CeriumOxalate = WerkstoffReconstruction.byId(11027);

    public static final Werkstoff CeriumIIIOxide = WerkstoffReconstruction.byId(11028);

    public static final Werkstoff CeriumRichMixture = WerkstoffReconstruction.byId(11029);

    public static final Werkstoff CooledMonaziteRareEarthConcentrate = WerkstoffReconstruction.byId(11030);

    public static final Werkstoff MonaziteRarerEarthSediment = WerkstoffReconstruction.byId(11031);

    public static final Werkstoff MonaziteHeterogenousHalogenicRareEarthMixture = WerkstoffReconstruction.byId(11032);

    public static final Werkstoff SaturatedMonaziteRareEarthMixture = WerkstoffReconstruction.byId(11033);

    public static final Werkstoff SamaricResidue = WerkstoffReconstruction.byId(11034);

    public static final Werkstoff AmmoniumNitrate = WerkstoffReconstruction.byId(11036);

    public static final Werkstoff ThoriumPhosphateCake = WerkstoffReconstruction.byId(11037);

    public static final Werkstoff ThoriumPhosphateConcentrate = WerkstoffReconstruction.byId(11038);

    public static final Werkstoff UraniumFiltrate = WerkstoffReconstruction.byId(11039);

    public static final Werkstoff NeutralizedUraniumFiltrate = WerkstoffReconstruction.byId(11040);

    public static final Werkstoff SeaweedAsh = WerkstoffReconstruction.byId(11041);

    public static final Werkstoff SeaweedConcentrate = WerkstoffReconstruction.byId(11042);

    public static final Werkstoff PotassiumPermanganate = WerkstoffReconstruction.byId(11043);

    public static final Werkstoff PotassiumPermanganateSolution = WerkstoffReconstruction.byId(11044);

    public static final Werkstoff SeaweedByproducts = WerkstoffReconstruction.byId(11045);

    public static final Werkstoff NitricLeachedMonaziteMixture = WerkstoffReconstruction.byId(11046);

    public static final Werkstoff EuropiumOxide = WerkstoffReconstruction.byId(11047);

    public static final Werkstoff EuropiumSulfide = WerkstoffReconstruction.byId(11048);

    public static final Werkstoff UnknownBlend = WerkstoffReconstruction.byId(11049);

    public static final Werkstoff EuropiumIIIOxide = WerkstoffReconstruction.byId(11050);

    // TODO

    // BASTNASITE
    public static final Werkstoff MuddyRareEarthBastnasiteSolution = WerkstoffReconstruction.byId(11100);
    /*
     * public static final Werkstoff FluorosilicicAcid = new Werkstoff( new short[] {205, 133, 63},
     * "Hexafluorosilicic Acid", subscriptNumbers("H2SiF6"), new Werkstoff.Stats(), Werkstoff.Types.COMPOUND, new
     * Werkstoff.GenerationFeatures().disable().addCells(), offsetID2 + 1, TextureSet.SET_FLUID );
     */
    public static final Werkstoff SodiumFluorosilicate = WerkstoffReconstruction.byId(11102);

    public static final Werkstoff SteamCrackedBasnasiteSolution = WerkstoffReconstruction.byId(11103);

    public static final Werkstoff ConditionedBastnasiteMud = WerkstoffReconstruction.byId(11104);

    public static final Werkstoff DiltedRareEarthBastnasiteMud = WerkstoffReconstruction.byId(11105);

    public static final Werkstoff FilteredBastnasiteMud = WerkstoffReconstruction.byId(11106);

    public static final Werkstoff BastnasiteRareEarthOxidePowder = WerkstoffReconstruction.byId(11107);

    public static final Werkstoff LeachedBastnasiteRareEarthOxides = WerkstoffReconstruction.byId(11108);

    public static final Werkstoff Gangue = WerkstoffReconstruction.byId(11109);
    // TODO: Deal with colouring
    public static final Werkstoff RoastedRareEarthOxides = WerkstoffReconstruction.byId(11110);

    public static final Werkstoff WetRareEarthOxides = WerkstoffReconstruction.byId(11111);

    public static final Werkstoff CeriumOxidisedRareEarthOxides = WerkstoffReconstruction.byId(11112);

    public static final Werkstoff BastnasiteRarerEarthOxides = WerkstoffReconstruction.byId(11113);

    public static final Werkstoff NitratedBastnasiteRarerEarthOxides = WerkstoffReconstruction.byId(11114);

    public static final Werkstoff SaturatedBastnasiteRarerEarthOxides = WerkstoffReconstruction.byId(11115);

    public static final Werkstoff SamaricRareEarthConcentrate = WerkstoffReconstruction.byId(11116);

    public static final Werkstoff NeodymicRareEarthConcentrate = WerkstoffReconstruction.byId(11117);
    public static final Werkstoff LanthaniumChloride = WerkstoffReconstruction.byId(11121);

    public static final Werkstoff NeodymiumOxide = WerkstoffReconstruction.byId(11122);

    public static final Werkstoff FluorinatedSamaricConcentrate = WerkstoffReconstruction.byId(11123);

    public static final Werkstoff CalciumFluoride = WerkstoffReconstruction.byId(11124);

    public static final Werkstoff SamariumTerbiumMixture = WerkstoffReconstruction.byId(11125);

    public static final Werkstoff NitratedSamariumTerbiumMixture = WerkstoffReconstruction.byId(11126);

    public static final Werkstoff TerbiumNitrate = WerkstoffReconstruction.byId(11127);

    public static final Werkstoff SamariumOreConcentrate = WerkstoffReconstruction.byId(11128);

    public static final Werkstoff DephosphatedSamariumConcentrate = WerkstoffReconstruction.byId(11129);

    // Material removed for 2.9, do not use until 2.10
    // offsetID3

    // 1,4-Butanediol
    public static final Werkstoff Butanediol = WerkstoffReconstruction.byId(11301);

    // Material removed for 2.9, do not use until 2.10
    // offsetID3 + 2

    // Tellurium-Molybdenum-Oxide Catalyst
    public static final Werkstoff MoTeOCatalyst = WerkstoffReconstruction.byId(11303);

    // Tellurium Oxide
    public static final Werkstoff TelluriumIVOxide = WerkstoffReconstruction.byId(11304);

    public static final Werkstoff MolybdenumIVOxide = WerkstoffReconstruction.byId(11305);

    // Material removed for 2.9, do not use until 2.10
    // offsetID3 + 6

    // Material removed for 2.9, do not use until 2.10
    // offsetID3 + 7

    // Material removed for 2.9, do not use until 2.10
    // offsetID3 + 8

    public static final Werkstoff Dinitrotoluene = WerkstoffReconstruction.byId(11309);

    public static final Werkstoff Diaminotoluene = WerkstoffReconstruction.byId(11310);

    // Material removed for 2.9, do not use until 2.10
    // offsetID3 + 11

    // Material removed for 2.9, do not use until 2.10
    // offsetID3 + 12

    public static final Werkstoff PotassiumChlorate = WerkstoffReconstruction.byId(11314);

    public static final Werkstoff DilutedAcetone = WerkstoffReconstruction.byId(11316);

    public static final Werkstoff MolybdenumTrioxide = WerkstoffReconstruction.byId(11317);

    // TODO Samarium Processing Line Material regist

    public static final Werkstoff MuddySamariumRareEarthSolution = WerkstoffReconstruction.byId(11461);

    public static final Werkstoff SamariumRareEarthMud = WerkstoffReconstruction.byId(11462);

    public static final Werkstoff DilutedSamariumRareEarthSolution = WerkstoffReconstruction.byId(11463);

    public static final Werkstoff SamariumOxalate = WerkstoffReconstruction.byId(11464);

    public static final Werkstoff SamariumChloride = WerkstoffReconstruction.byId(11465);

    public static final Werkstoff SamariumChlorideSodiumChlorideBlend = WerkstoffReconstruction.byId(11466);

    public static final Werkstoff ImpureLanthanumChloride = WerkstoffReconstruction.byId(11467);

    public static final Werkstoff SamariumOxide = WerkstoffReconstruction.byId(11468);

    public static final Werkstoff ChlorinatedRareEarthConcentrate = WerkstoffReconstruction.byId(11469);

    public static final Werkstoff ChlorinatedRareEarthEnrichedSolution = WerkstoffReconstruction.byId(11470);

    public static final Werkstoff ChlorinatedRareEarthDilutedSolution = WerkstoffReconstruction.byId(11471);

    public static final Werkstoff RarestEarthResidue = WerkstoffReconstruction.byId(11472);

    // TODO reg Lanth Ore Concentrate
    /**
     * Extracted Nano Resin Lanthanum1.2 Praseodymium3.4 Cerium5.6 Neodymium7.8 Promethium9.10 Samarium11.12 √
     * Europium13.14 Gadolinium15.16 Terbium17.18 Dysprosium19.20 Holmium21.22 Erbium23.24 Thulium25.26 Ytterbium27.28
     * Lutetium29.30
     */

    public static final Werkstoff LanthanumExtractingNanoResin = WerkstoffReconstruction.byId(11401);

    public static final Werkstoff FilledLanthanumExtractingNanoResin = WerkstoffReconstruction.byId(11402);

    public static final Werkstoff PraseodymiumExtractingNanoResin = WerkstoffReconstruction.byId(11403);

    public static final Werkstoff FilledPraseodymiumExtractingNanoResin = WerkstoffReconstruction.byId(11404);

    public static final Werkstoff CeriumExtractingNanoResin = WerkstoffReconstruction.byId(11405);

    public static final Werkstoff FilledCeriumExtractingNanoResin = WerkstoffReconstruction.byId(11406);

    public static final Werkstoff NeodymiumExtractingNanoResin = WerkstoffReconstruction.byId(11407);

    public static final Werkstoff FilledNeodymiumExtractingNanoResin = WerkstoffReconstruction.byId(11408);

    public static final Werkstoff SamariumExtractingNanoResin = WerkstoffReconstruction.byId(11411);

    public static final Werkstoff FilledSamariumExtractingNanoResin = WerkstoffReconstruction.byId(11412);

    public static final Werkstoff EuropiumExtractingNanoResin = WerkstoffReconstruction.byId(11413);

    public static final Werkstoff FilledEuropiumExtractingNanoResin = WerkstoffReconstruction.byId(11414);

    public static final Werkstoff GadoliniumExtractingNanoResin = WerkstoffReconstruction.byId(11415);

    public static final Werkstoff FilledGadoliniumExtractingNanoResin = WerkstoffReconstruction.byId(11416);

    public static final Werkstoff TerbiumExtractingNanoResin = WerkstoffReconstruction.byId(11417);

    public static final Werkstoff FilledTerbiumExtractingNanoResin = WerkstoffReconstruction.byId(11418);

    public static final Werkstoff DysprosiumExtractingNanoResin = WerkstoffReconstruction.byId(11419);

    public static final Werkstoff FilledDysprosiumExtractingNanoResin = WerkstoffReconstruction.byId(11420);

    public static final Werkstoff HolmiumExtractingNanoResin = WerkstoffReconstruction.byId(11421);

    public static final Werkstoff FilledHolmiumExtractingNanoResin = WerkstoffReconstruction.byId(11422);

    public static final Werkstoff ErbiumExtractingNanoResin = WerkstoffReconstruction.byId(11423);

    public static final Werkstoff FilledErbiumExtractingNanoResin = WerkstoffReconstruction.byId(11424);

    public static final Werkstoff ThuliumExtractingNanoResin = WerkstoffReconstruction.byId(11425);

    public static final Werkstoff FilledThuliumExtractingNanoResin = WerkstoffReconstruction.byId(11426);

    public static final Werkstoff YtterbiumExtractingNanoResin = WerkstoffReconstruction.byId(11427);

    public static final Werkstoff FilledYtterbiumExtractingNanoResin = WerkstoffReconstruction.byId(11428);

    public static final Werkstoff LutetiumExtractingNanoResin = WerkstoffReconstruction.byId(11429);

    public static final Werkstoff FilledLutetiumExtractingNanoResin = WerkstoffReconstruction.byId(11430);

    // Lanthanides Chloride Concentrate
    // Lanthanum
    public static final Werkstoff LanthanumChlorideConcentrate = WerkstoffReconstruction.byId(11431);

    // Praseodymium
    public static final Werkstoff PraseodymiumChlorideConcentrate = WerkstoffReconstruction.byId(11432);

    // Cerium
    public static final Werkstoff CeriumChlorideConcentrate = WerkstoffReconstruction.byId(11433);

    // Neodymium
    public static final Werkstoff NeodymiumChlorideConcentrate = WerkstoffReconstruction.byId(11434);

    //
    public static final Werkstoff PromethiumChlorideConcentrate = WerkstoffReconstruction.byId(11435);

    //
    public static final Werkstoff SamariumChlorideConcentrate = WerkstoffReconstruction.byId(11436);

    //
    public static final Werkstoff EuropiumChlorideConcentrate = WerkstoffReconstruction.byId(11437);

    //
    public static final Werkstoff GadoliniumChlorideConcentrate = WerkstoffReconstruction.byId(11438);

    //
    public static final Werkstoff TerbiumChlorideConcentrate = WerkstoffReconstruction.byId(11439);

    //
    public static final Werkstoff DysprosiumChlorideConcentrate = WerkstoffReconstruction.byId(11440);

    //
    public static final Werkstoff HolmiumChlorideConcentrate = WerkstoffReconstruction.byId(11441);

    //
    public static final Werkstoff ErbiumChlorideConcentrate = WerkstoffReconstruction.byId(11442);

    //
    public static final Werkstoff ThuliumChlorideConcentrate = WerkstoffReconstruction.byId(11443);

    //
    public static final Werkstoff YtterbiumChlorideConcentrate = WerkstoffReconstruction.byId(11444);

    //
    public static final Werkstoff LutetiumChlorideConcentrate = WerkstoffReconstruction.byId(11445);

    // Lanthanides Ore Concentrate

    // Lanthanum
    public static final Werkstoff LanthanumOreConcentrate = WerkstoffReconstruction.byId(11446);

    // Praseodymium
    public static final Werkstoff PraseodymiumOreConcentrate = WerkstoffReconstruction.byId(11447);

    // Cerium
    public static final Werkstoff CeriumOreConcentrate = WerkstoffReconstruction.byId(11029);

    // Neodymium
    public static final Werkstoff NeodymiumOreConcentrate = WerkstoffReconstruction.byId(11449);

    // Promethium
    public static final Werkstoff PromethiumOreConcentrate = WerkstoffReconstruction.byId(11450);

    // Europium
    public static final Werkstoff EuropiumOreConcentrate = WerkstoffReconstruction.byId(11451);

    // Gadolinium
    public static final Werkstoff GadoliniumOreConcentrate = WerkstoffReconstruction.byId(11452);

    // Terbium
    public static final Werkstoff TerbiumOreConcentrate = WerkstoffReconstruction.byId(11453);

    // Dysprosium
    public static final Werkstoff DysprosiumOreConcentrate = WerkstoffReconstruction.byId(11454);

    // Holmium
    public static final Werkstoff HolmiumOreConcentrate = WerkstoffReconstruction.byId(11455);

    // Erbium
    public static final Werkstoff ErbiumOreConcentrate = WerkstoffReconstruction.byId(11456);

    // Thulium
    public static final Werkstoff ThuliumOreConcentrate = WerkstoffReconstruction.byId(11457);

    // Ytterbium
    public static final Werkstoff YtterbiumOreConcentrate = WerkstoffReconstruction.byId(11458);

    // Lutetium
    public static final Werkstoff LutetiumOreConcentrate = WerkstoffReconstruction.byId(11459);

    // LuAG
    public static final Werkstoff CeriumDopedLutetiumAluminiumOxygenBlend = WerkstoffReconstruction.byId(11498);

    public static final Werkstoff CeriumDopedLutetiumAluminiumGarnet = WerkstoffReconstruction.byId(11499);

    public static final Werkstoff Permalloy = WerkstoffReconstruction.byId(11350);

    public static final Werkstoff MuMetal = WerkstoffReconstruction.byId(11351);

    public static final Werkstoff Thorium234 = WerkstoffReconstruction.byId(11352);

    public static final Werkstoff SiliconNitride = WerkstoffReconstruction.byId(11353);

    public static final Werkstoff Fluoroform = WerkstoffReconstruction.byId(11354);

    public static final Werkstoff FluoroformOxygenMix = WerkstoffReconstruction.byId(11355);

    public static final Werkstoff BoronTrioxide = WerkstoffReconstruction.byId(11356);

    public static final Werkstoff BoronTrichloride = WerkstoffReconstruction.byId(11357);

    public static final Werkstoff LanthanumHexaboride = WerkstoffReconstruction.byId(11358);

    public static final Werkstoff LanthanumOxide = WerkstoffReconstruction.byId(11359);

    public static final Werkstoff NitrogenPlasmaSilaneMix = WerkstoffReconstruction.byId(11360);

    public static final Werkstoff HotSuperCoolant = WerkstoffReconstruction.byId(11361);

    public static void runInit() {
        addSubTags();
    }

    private static void addSubTags() {

    }

    @Override
    public void run() {}
}
