package goodgenerator.items;

import static gregtech.api.enums.Materials.*;

import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffReconstruction;

public class GGMaterial implements Runnable {

    protected static final int OffsetID = 10001;

    // Uranium Based Fuel Line
    public static final Werkstoff graphiteUraniumMixture = WerkstoffReconstruction.byId(10001);

    public static final Werkstoff uraniumBasedLiquidFuel = WerkstoffReconstruction.byId(10002);

    public static final Werkstoff uraniumBasedLiquidFuelExcited = WerkstoffReconstruction.byId(10003);

    public static final Werkstoff uraniumBasedLiquidFuelDepleted = WerkstoffReconstruction.byId(10004);

    // Thorium Based Fuel
    public static final Werkstoff uraniumCarbideThoriumMixture = WerkstoffReconstruction.byId(10005);

    public static final Werkstoff thoriumBasedLiquidFuel = WerkstoffReconstruction.byId(10006);

    public static final Werkstoff thoriumBasedLiquidFuelExcited = WerkstoffReconstruction.byId(10007);

    public static final Werkstoff thoriumBasedLiquidFuelDepleted = WerkstoffReconstruction.byId(10008);

    // Plutonium Based Fuel
    public static final Werkstoff plutoniumOxideUraniumMixture = WerkstoffReconstruction.byId(10009);

    public static final Werkstoff plutoniumBasedLiquidFuel = WerkstoffReconstruction.byId(10010);

    public static final Werkstoff plutoniumBasedLiquidFuelExcited = WerkstoffReconstruction.byId(10011);

    public static final Werkstoff plutoniumBasedLiquidFuelDepleted = WerkstoffReconstruction.byId(10012);

    // Thorium-233
    public static final Werkstoff oxalate = WerkstoffReconstruction.byId(10013);

    public static final Werkstoff vanadiumPentoxide = WerkstoffReconstruction.byId(10014);

    public static final Werkstoff thoriumNitrate = WerkstoffReconstruction.byId(10015);

    public static final Werkstoff thoriumOxalate = WerkstoffReconstruction.byId(10016);

    public static final Werkstoff thoriumHydroxide = WerkstoffReconstruction.byId(10017);

    public static final Werkstoff sodiumOxalate = WerkstoffReconstruction.byId(10018);

    public static final Werkstoff thoriumTetrachloride = WerkstoffReconstruction.byId(10019);

    @Deprecated // use GT++ ThoriumTetraFluoride
    public static final Werkstoff thoriumTetrafluoride = WerkstoffReconstruction.byId(10020);

    public static final Werkstoff thorium232Tetrafluoride = WerkstoffReconstruction.byId(10021);

    // Orundum
    public static final Werkstoff orundum = WerkstoffReconstruction.byId(10023);

    // Atomic Separation Catalyst
    public static final Werkstoff atomicSeparationCatalyst = WerkstoffReconstruction.byId(10022);

    // Naquadah Fuel Rework
    public static final Werkstoff extremelyUnstableNaquadah = WerkstoffReconstruction.byId(10024);

    public static final Werkstoff lightNaquadahFuel = WerkstoffReconstruction.byId(10025);

    public static final Werkstoff heavyNaquadahFuel = WerkstoffReconstruction.byId(10026);

    public static final Werkstoff naquadahGas = WerkstoffReconstruction.byId(10027);

    public static final Werkstoff naquadahAsphalt = WerkstoffReconstruction.byId(10028);

    public static final Werkstoff ether = WerkstoffReconstruction.byId(10029);

    public static final Werkstoff antimonyTrichloride = WerkstoffReconstruction.byId(10030);

    public static final Werkstoff antimonyPentachlorideSolution = WerkstoffReconstruction.byId(10031);

    public static final Werkstoff antimonyPentachloride = WerkstoffReconstruction.byId(10032);

    public static final Werkstoff antimonyPentafluoride = WerkstoffReconstruction.byId(10033);

    public static final Werkstoff fluoroantimonicAcid = WerkstoffReconstruction.byId(10034);

    public static final Werkstoff radioactiveSludge = WerkstoffReconstruction.byId(10035);

    public static final Werkstoff acidNaquadahEmulsion = WerkstoffReconstruction.byId(10036);

    public static final Werkstoff naquadahEmulsion = WerkstoffReconstruction.byId(10037);

    public static final Werkstoff naquadahSolution = WerkstoffReconstruction.byId(10038);

    public static final Werkstoff naquadahBasedFuelMkI = WerkstoffReconstruction.byId(10039);

    public static final Werkstoff naquadahBasedFuelMkIDepleted = WerkstoffReconstruction.byId(10040);

    public static final Werkstoff naquadahBasedFuelMkII = WerkstoffReconstruction.byId(10041);

    public static final Werkstoff naquadahBasedFuelMkIIDepleted = WerkstoffReconstruction.byId(10042);
    /* These materials will be enable when they are removed in GregTech */
    /*
     * public static final Werkstoff praseodymium = new Werkstoff( new short[]{0xff,0xff,0xff}, "praseodymium", "Pr",
     * new Werkstoff.Stats(), Werkstoff.Types.ELEMENT, new
     * Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems(), OffsetID + 42,
     * TextureSet.SET_METALLIC ); public static final Werkstoff rubidium = new Werkstoff( new short[]{0xff,0x2a,0x00},
     * "rubidium", "Rb", new Werkstoff.Stats(), Werkstoff.Types.ELEMENT, new
     * Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems(), OffsetID + 43,
     * TextureSet.SET_SHINY ); public static final Werkstoff thulium = new Werkstoff( new short[]{0xff,0xff,0xff},
     * "Thulium", "Tm", new Werkstoff.Stats(), Werkstoff.Types.ELEMENT, new
     * Werkstoff.GenerationFeatures().disable().onlyDust().addMolten().addMetalItems(), OffsetID + 44,
     * TextureSet.SET_METALLIC );
     */
    public static final Werkstoff naquadahBasedFuelMkIII = WerkstoffReconstruction.byId(10046);

    public static final Werkstoff naquadahBasedFuelMkIIIDepleted = WerkstoffReconstruction.byId(10047);

    public static final Werkstoff naquadahBasedFuelMkIV = WerkstoffReconstruction.byId(10048);

    public static final Werkstoff naquadahBasedFuelMkIVDepleted = WerkstoffReconstruction.byId(10049);

    public static final Werkstoff naquadahBasedFuelMkV = WerkstoffReconstruction.byId(10050);

    public static final Werkstoff naquadahBasedFuelMkVDepleted = WerkstoffReconstruction.byId(10051);

    public static final Werkstoff naquadahBasedFuelMkVI = WerkstoffReconstruction.byId(10116);

    public static final Werkstoff naquadahBasedFuelMkVIDepleted = WerkstoffReconstruction.byId(10117);

    public static final Werkstoff zincChloride = WerkstoffReconstruction.byId(10052);

    public static final Werkstoff zincThoriumAlloy = WerkstoffReconstruction.byId(10053);

    // Naquadah Rework Materials
    public static final Werkstoff naquadahEarth = WerkstoffReconstruction.byId(10054);

    public static final Werkstoff titaniumTrifluoride = WerkstoffReconstruction.byId(10055);

    public static final Werkstoff lowQualityNaquadahEmulsion = WerkstoffReconstruction.byId(10056);

    public static final Werkstoff galliumHydroxide = WerkstoffReconstruction.byId(10057);

    public static final Werkstoff lowQualityNaquadahSolution = WerkstoffReconstruction.byId(10058);

    public static final Werkstoff towEthyl1Hexanol = WerkstoffReconstruction.byId(10059);

    public static final Werkstoff P507 = WerkstoffReconstruction.byId(10060);

    public static final Werkstoff naquadahAdamantiumSolution = WerkstoffReconstruction.byId(10061);

    public static final Werkstoff naquadahRichSolution = WerkstoffReconstruction.byId(10062);

    public static final Werkstoff naquadahine = WerkstoffReconstruction.byId(10063);

    public static final Werkstoff fluorineRichWasteLiquid = WerkstoffReconstruction.byId(10064);

    public static final Werkstoff wasteLiquid = WerkstoffReconstruction.byId(10065);

    public static final Werkstoff adamantine = WerkstoffReconstruction.byId(10066);

    public static final Werkstoff enrichedNaquadahEarth = WerkstoffReconstruction.byId(10067);

    public static final Werkstoff triniumSulphate = WerkstoffReconstruction.byId(10068);

    public static final Werkstoff enrichedNaquadahRichSolution = WerkstoffReconstruction.byId(10069);

    public static final Werkstoff concentratedEnrichedNaquadahSludge = WerkstoffReconstruction.byId(10070);

    public static final Werkstoff enrichedNaquadahSulphate = WerkstoffReconstruction.byId(10071);

    public static final Werkstoff naquadriaEarth = WerkstoffReconstruction.byId(10072);

    public static final Werkstoff indiumPhosphate = WerkstoffReconstruction.byId(10073);

    public static final Werkstoff lowQualityNaquadriaPhosphate = WerkstoffReconstruction.byId(10074);

    public static final Werkstoff naquadriaRichSolution = WerkstoffReconstruction.byId(10075);

    public static final Werkstoff lowQualityNaquadriaSulphate = WerkstoffReconstruction.byId(10076);

    public static final Werkstoff lowQualityNaquadriaSolution = WerkstoffReconstruction.byId(10077);

    public static final Werkstoff naquadriaSulphate = WerkstoffReconstruction.byId(10078);

    public static final Werkstoff naquadahGoo = WerkstoffReconstruction.byId(10079);

    public static final Werkstoff enrichedNaquadahGoo = WerkstoffReconstruction.byId(10080);

    public static final Werkstoff naquadriaGoo = WerkstoffReconstruction.byId(10081);

    // material for reactor stuff
    public static final Werkstoff zircaloy4 = WerkstoffReconstruction.byId(10082);

    public static final Werkstoff zircaloy2 = WerkstoffReconstruction.byId(10083);

    public static final Werkstoff incoloy903 = WerkstoffReconstruction.byId(10084);

    public static final Werkstoff adamantiumAlloy = WerkstoffReconstruction.byId(10085);

    public static final Werkstoff ethanolGasoline = WerkstoffReconstruction.byId(10086);

    public static final Werkstoff cyclopentadiene = WerkstoffReconstruction.byId(10087);

    public static final Werkstoff ferrousChloride = WerkstoffReconstruction.byId(10088);

    public static final Werkstoff diethylamine = WerkstoffReconstruction.byId(10089);

    public static final Werkstoff impureFerroceneMixture = WerkstoffReconstruction.byId(10090);

    public static final Werkstoff ferroceneSolution = WerkstoffReconstruction.byId(10091);

    public static final Werkstoff ferroceneWaste = WerkstoffReconstruction.byId(10092);

    public static final Werkstoff ferrocene = WerkstoffReconstruction.byId(10093);

    public static final Werkstoff ironedKerosene = WerkstoffReconstruction.byId(10094);

    public static final Werkstoff ironedFuel = WerkstoffReconstruction.byId(10095);

    public static final Werkstoff marM200 = WerkstoffReconstruction.byId(10096);

    public static final Werkstoff marCeM200 = WerkstoffReconstruction.byId(10097);

    public static final Werkstoff lithiumChloride = WerkstoffReconstruction.byId(10098);

    public static final Werkstoff signalium = WerkstoffReconstruction.byId(10099);

    public static final Werkstoff lumiinessence = WerkstoffReconstruction.byId(10100);

    public static final Werkstoff lumiium = WerkstoffReconstruction.byId(10101);

    public static final Werkstoff artheriumSn = WerkstoffReconstruction.byId(10102);

    public static final Werkstoff titaniumBetaC = WerkstoffReconstruction.byId(10103);

    public static final Werkstoff dalisenite = WerkstoffReconstruction.byId(10104);

    public static final Werkstoff hikarium = WerkstoffReconstruction.byId(10105);

    public static final Werkstoff tairitsu = WerkstoffReconstruction.byId(10106);

    public static final Werkstoff antimonyPentafluorideSolution = WerkstoffReconstruction.byId(10107);

    public static final Werkstoff magnesiumSulphate = WerkstoffReconstruction.byId(10108);

    public static final Werkstoff preciousMetalAlloy = WerkstoffReconstruction.byId(10109);

    public static final Werkstoff enrichedNaquadahAlloy = WerkstoffReconstruction.byId(10110);

    public static final Werkstoff metastableOganesson = WerkstoffReconstruction.byId(10111);

    public static final Werkstoff shirabon = WerkstoffReconstruction.byId(10112);

    public static final Werkstoff inertNaquadah = WerkstoffReconstruction.byId(10113);

    public static final Werkstoff inertEnrichedNaquadah = WerkstoffReconstruction.byId(10114);

    public static final Werkstoff inertNaquadria = WerkstoffReconstruction.byId(10115);

    @Override
    public void run() {}
}
