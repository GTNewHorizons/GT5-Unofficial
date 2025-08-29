package gregtech.loaders.materials;

import static gregtech.api.enums.Materials.Aluminium;
import static gregtech.api.enums.Materials.Antimony;
import static gregtech.api.enums.Materials.Arsenic;
import static gregtech.api.enums.Materials.Boron;
import static gregtech.api.enums.Materials.Brick;
import static gregtech.api.enums.Materials.Butadiene;
import static gregtech.api.enums.Materials.Calcium;
import static gregtech.api.enums.Materials.Carbon;
import static gregtech.api.enums.Materials.Chlorine;
import static gregtech.api.enums.Materials.Chrome;
import static gregtech.api.enums.Materials.Clay;
import static gregtech.api.enums.Materials.Cobalt;
import static gregtech.api.enums.Materials.Copper;
import static gregtech.api.enums.Materials.Fluorine;
import static gregtech.api.enums.Materials.Glass;
import static gregtech.api.enums.Materials.Hydrogen;
import static gregtech.api.enums.Materials.Iron;
import static gregtech.api.enums.Materials.Lead;
import static gregtech.api.enums.Materials.Magnesium;
import static gregtech.api.enums.Materials.Nickel;
import static gregtech.api.enums.Materials.Niobium;
import static gregtech.api.enums.Materials.Nitrogen;
import static gregtech.api.enums.Materials.Oxygen;
import static gregtech.api.enums.Materials.PhosphoricAcid;
import static gregtech.api.enums.Materials.Phosphorus;
import static gregtech.api.enums.Materials.Potassium;
import static gregtech.api.enums.Materials.Silicon;
import static gregtech.api.enums.Materials.Sodium;
import static gregtech.api.enums.Materials.Styrene;
import static gregtech.api.enums.Materials.Sulfur;
import static gregtech.api.enums.Materials.SulfuricAcid;
import static gregtech.api.enums.Materials.Zinc;

import java.util.Arrays;
import java.util.Collections;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.Element;
import gregtech.api.enums.MaterialBuilder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TextureSet;
import gregtech.api.enums.TierEU;
import gregtech.api.objects.MaterialStack;

public class MaterialsInit1 {

    public static void load() {
        loadElements();
        loadIsotopes();
        loadWaterLine();
        loadRandom();
        loadDontCare();
        loadUnknownComponents();
        loadTiers();
        loadNotExact();
        loadTODOThis();
        loadDegree1Compounds();
        loadUnclassified01();
        loadUnclassified02();
        loadUnclassified03();
        loadUnclassified04();
        loadUnclassified05();
        loadRoastedOres();
        loadSiliconLine();
        loadUnclassified06();
        loadDegree2Compounds();
        loadDegree3Compounds();
        loadDegree4Compounds();
        loadPolybenzimidazoleLine();
        loadGasolineLine();
        loadAdded();
        loadGalaxySpace();
        loadUnclassified07();
        loadOverpoweredMaterials();
        loadSuperConductorBases();

        // spotless:off
        Materials.SuperconductorMV      = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                ,  85,  85,  85,   0,   "SuperconductorMV"   ,   "Superconductor MV"       ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeGray       , Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 6)));
        Materials.SuperconductorHV      = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                ,  51,  25,   0,   0,   "SuperconductorHV"   ,   "Superconductor HV"       ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeBrown      , Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 12)));
        Materials.SuperconductorEV      = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                ,   0, 135,   0,   0,   "SuperconductorEV"   ,   "Superconductor EV"       ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeLime       , Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 18)));
        Materials.SuperconductorIV      = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                ,  51,   0,  51,   0,   "SuperconductorIV"   ,   "Superconductor IV"       ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeMagenta    , Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 24)));
        Materials.SuperconductorLuV     = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                , 153,  76,   0,   0,   "SuperconductorLuV"  ,   "Superconductor LuV"      ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeBrown      , Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 30)));
        Materials.SuperconductorZPM     = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                ,  10,  10,  10,   0,   "SuperconductorZPM"  ,   "Superconductor ZPM"      ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeBlack      , Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 36)));
        Materials.SuperconductorUV      = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                , 224, 210,   7,   0,   "SuperconductorUV"   ,   "Superconductor UV"       ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeYellow     , Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 42)));
        Materials.SuperconductorUHV     = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                ,  38, 129, 189,   0,   "Superconductor"     ,   "Superconductor UHV"      ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeWhite      , Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 48)));
        Materials.SuperconductorUEV     = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                , 174,   8,   8,   0,   "SuperconductorUEV"  ,   "Superconductor UEV"      ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeWhite      , Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 54)));
        Materials.SuperconductorUIV     = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                , 229,  88, 177,   0,   "SuperconductorUIV"  ,   "Superconductor UIV"      ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeWhite      , Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 60)));
        Materials.SuperconductorUMV     = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                , 181,  38, 205,   0,   "SuperconductorUMV"  ,   "Superconductor UMV"      ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeWhite      , Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 66)));

        Materials.SuperCoolant = loadSuperCoolant();

        Materials.EnrichedHolmium       = loadEnrichedHolmium();

        Materials.TengamPurified = loadTengamPurified();
        Materials.TengamAttuned  = loadTengamAttuned();
        Materials.TengamRaw      = loadTengamRaw();

        Materials.ActivatedCarbon = loadActivatedCarbon();
        Materials.PreActivatedCarbon = loadPreActivatedCarbon();
        Materials.DirtyActivatedCarbon = loadDirtyActivatedCarbon();
        Materials.PolyAluminiumChloride = loadPolyAluminiumChloride();
        Materials.Ozone = loadOzone();
        Materials.StableBaryonicMatter = loadStableBaryonicMatter();

        Materials.RawRadox = loadRawRadox();
        Materials.RadoxSuperLight = loadSuperLightRadox();
        Materials.RadoxLight = loadLightRadox();
        Materials.RadoxHeavy = loadHeavyRadox();
        Materials.RadoxSuperHeavy = loadSuperHeavyRadox();
        Materials.Xenoxene = loadXenoxene();
        Materials.DilutedXenoxene = loadDilutedXenoxene();
        Materials.RadoxCracked = loadCrackedRadox();
        Materials.RadoxGas = loadRadoxGas();

        // Material ID was choosen randomly//
        Materials.RadoxPolymer = loadRadoxPolymer();

        Materials.NetherAir = loadNetherAir();
        Materials.NetherSemiFluid = loadNethersemifluid();
        Materials.NefariousGas = loadNefariousGas();
        Materials.NefariousOil = loadNefariousOil();
        Materials.PoorNetherWaste = loadPoorNetherWaste();
        Materials.RichNetherWaste = loadRichNetherWaste();
        Materials.HellishMetal = loadHellishMetal();
        Materials.Netherite = loadNetherite();
        Materials.ActivatedNetherite = loadActivatedNetherite();

        Materials.PrismarineSolution = loadPrismarineSolution();
        Materials.PrismarineContaminatedHydrogenPeroxide = loadPrismarinecontaminatedhydrogenperoxide();
        Materials.PrismarineRichNitrobenzeneSolution = loadPrismarinerichnitrobenzenesolution();
        Materials.PrismarineContaminatedNitrobenzeSolution = loadPrismarinecontaminatednitrobenzenesolution();
        Materials.PrismaticGas = loadPrismaticGas();
        Materials.PrismaticAcid = loadPrismaticAcid();
        Materials.PrismaticNaquadah = loadPrismaticNaquadah();
        Materials.PrismaticNaquadahCompositeSlurry = loadPrismaticNaquadahCompositeSlurry();

        Materials.ComplexityCatalyst = loadComplexityCatalyst();
        Materials.EntropicCatalyst = loadEntropicCatalyst();

        // spotless:on
    }

    private static void loadElements() {
        Materials.Aluminium = loadAluminium();
        Materials.Americium = loadAmericium();
        Materials.Antimony = loadAntimony();
        Materials.Argon = loadArgon();
        Materials.Arsenic = loadArsenic();
        Materials.Barium = loadBarium();
        Materials.Beryllium = loadBeryllium();
        Materials.Bismuth = loadBismuth();
        Materials.Boron = loadBoron();
        Materials.Caesium = loadCaesium();
        Materials.Calcium = loadCalcium();
        Materials.Carbon = loadCarbon();
        Materials.Cadmium = loadCadmium();
        Materials.Cerium = loadCerium();
        Materials.Chlorine = loadChlorine();
        Materials.Chrome = loadChrome();
        Materials.Cobalt = loadCobalt();
        Materials.Copper = loadCopper();
        Materials.Desh = loadDesh(); // Not a real element
        Materials.Dysprosium = loadDysprosium();
        Materials.Empty = loadEmpty(); // Not a real element
        Materials.Erbium = loadErbium();
        Materials.Europium = loadEuropium();
        Materials.Flerovium = loadFlerovium();
        Materials.Fluorine = loadFluorine();
        Materials.Gadolinium = loadGadolinium();
        Materials.Gallium = loadGallium();
        Materials.Gold = loadGold();
        Materials.Holmium = loadHolmium();
        Materials.Hydrogen = loadHydrogen();
        Materials.Helium = loadHelium();
        Materials.Indium = loadIndium();
        Materials.Iridium = loadIridium();
        Materials.Iron = loadIron();
        Materials.Lanthanum = loadLanthanum();
        Materials.Lead = loadLead();
        Materials.Lithium = loadLithium();
        Materials.Lutetium = loadLutetium();
        Materials.Magic = loadMagic(); // Not a real element
        Materials.Magnesium = loadMagnesium();
        Materials.Manganese = loadManganese();
        Materials.Mercury = loadMercury();
        Materials.MeteoricIron = loadMeteoricIron(); // Not a real element
        Materials.Molybdenum = loadMolybdenum();
        Materials.Naquadah = loadNaquadah(); // Not a real element
        Materials.Neodymium = loadNeodymium();
        Materials.Neutronium = loadNeutronium();
        Materials.Nickel = loadNickel();
        Materials.Niobium = loadNiobium();
        Materials.Nitrogen = loadNitrogen();
        Materials.Osmium = loadOsmium();
        Materials.Oxygen = loadOxygen();
        Materials.Palladium = loadPalladium();
        Materials.Phosphorus = loadPhosphorus();
        Materials.Platinum = loadPlatinum();
        Materials.Plutonium = loadPlutonium();
        Materials.Potassium = loadPotassium();
        Materials.Praseodymium = loadPraseodymium();
        Materials.Promethium = loadPromethium();
        Materials.Radon = loadRadon();
        Materials.Rubidium = loadRubidium();
        Materials.Samarium = loadSamarium();
        Materials.Scandium = loadScandium();
        Materials.Silicon = loadSilicon();
        Materials.Silver = loadSilver();
        Materials.Sodium = loadSodium();
        Materials.Strontium = loadStrontium();
        Materials.Sulfur = loadSulfur();
        Materials.Tantalum = loadTantalum();
        Materials.Tellurium = loadTellurium();
        Materials.Terbium = loadTerbium();
        Materials.Thorium = loadThorium();
        Materials.Thulium = loadThulium();
        Materials.Tin = loadTin();
        Materials.Titanium = loadTitanium();
        Materials.Tritanium = loadTritanium(); // Not a real element
        Materials.Tungsten = loadTungsten();
        Materials.Uranium = loadUranium();
        Materials.Vanadium = loadVanadium();
        Materials.Ytterbium = loadYtterbium();
        Materials.Yttrium = loadYttrium();
        Materials.Zinc = loadZinc();
    }

    private static Materials loadAluminium() {
        return new MaterialBuilder().setName("Aluminium")
            .setDefaultLocalName("Aluminium")
            .setElement(Element.Al)
            .setMetaItemSubID(19)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightBlue)
            .setRGB(0x80c8f0)
            .setToolSpeed(10.0f)
            .setDurability(128)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(933)
            .setBlastFurnaceTemp(1700)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.VOLATUS, 1)
            .constructMaterial();
    }

    private static Materials loadAmericium() {
        return new MaterialBuilder().setName("Americium")
            .setDefaultLocalName("Americium")
            .setElement(Element.Am)
            .setMetaItemSubID(103)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightGray)
            .setRGB(0xc8c8c8)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1449)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadAntimony() {
        return new MaterialBuilder().setName("Antimony")
            .setDefaultLocalName("Antimony")
            .setElement(Element.Sb)
            .setMetaItemSubID(58)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightGray)
            .setRGB(0xdcdcf0)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(903)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial();
    }

    private static Materials loadArgon() {
        return new MaterialBuilder().setName("Argon")
            .setDefaultLocalName("Argon")
            .setElement(Element.Ar)
            .setMetaItemSubID(24)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGreen)
            .setARGB(0xf000ff00)
            .addCell()
            .addPlasma()
            .setMeltingPoint(83)
            .addAspect(TCAspects.AER, 2)
            .constructMaterial();
    }

    private static Materials loadArsenic() {
        return new MaterialBuilder().setName("Arsenic")
            .setDefaultLocalName("Arsenic")
            .setElement(Element.As)
            .setMetaItemSubID(39)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeOrange)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addCell()
            .addPlasma()
            .setMeltingPoint(1090)
            .addAspect(TCAspects.VENENUM, 3)
            .constructMaterial();
    }

    private static Materials loadBarium() {
        return new MaterialBuilder().setName("Barium")
            .setDefaultLocalName("Barium")
            .setElement(Element.Ba)
            .setMetaItemSubID(63)
            .setIconSet(TextureSet.SET_METALLIC)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1000)
            .addAspect(TCAspects.VINCULUM, 3)
            .constructMaterial();
    }

    private static Materials loadBeryllium() {
        return new MaterialBuilder().setName("Beryllium")
            .setDefaultLocalName("Beryllium")
            .setElement(Element.Be)
            .setMetaItemSubID(8)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x64b464)
            .setToolSpeed(14.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(1560)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.LUCRUM, 1)
            .constructMaterial();
    }

    private static Materials loadBismuth() {
        return new MaterialBuilder().setName("Bismuth")
            .setDefaultLocalName("Bismuth")
            .setElement(Element.Bi)
            .setMetaItemSubID(90)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeCyan)
            .setRGB(0x64a0a0)
            .setToolSpeed(6.0f)
            .setDurability(64)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(544)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.INSTRUMENTUM, 1)
            .constructMaterial();
    }

    private static Materials loadBoron() {
        return new MaterialBuilder().setName("Boron")
            .setDefaultLocalName("Boron")
            .setElement(Element.B)
            .setMetaItemSubID(9)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xd2fad2)
            .addDustItems()
            .addPlasma()
            .setMeltingPoint(2349)
            .addAspect(TCAspects.VITREUS, 3)
            .constructMaterial();
    }

    private static Materials loadCaesium() {
        return new MaterialBuilder().setName("Caesium")
            .setDefaultLocalName("Caesium")
            .setElement(Element.Cs)
            .setMetaItemSubID(62)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0xb0c4de)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(301)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadCalcium() {
        return new MaterialBuilder().setName("Calcium")
            .setDefaultLocalName("Calcium")
            .setElement(Element.Ca)
            .setMetaItemSubID(26)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setRGB(0xfff5f5)
            .addDustItems()
            .addPlasma()
            .setMeltingPoint(1115)
            .setBlastFurnaceTemp(1115)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.SANO, 1)
            .addAspect(TCAspects.TUTAMEN, 1)
            .constructMaterial();
    }

    private static Materials loadCarbon() {
        return new MaterialBuilder().setName("Carbon")
            .setDefaultLocalName("Carbon")
            .setElement(Element.C)
            .setMetaItemSubID(10)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x141414)
            .setToolSpeed(1.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addCell()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3800)
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.IGNIS, 1)
            .constructMaterial();
    }

    private static Materials loadCadmium() {
        return new MaterialBuilder().setName("Cadmium")
            .setDefaultLocalName("Cadmium")
            .setElement(Element.Cd)
            .setMetaItemSubID(55)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGray)
            .setRGB(0x32323c)
            .addDustItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(594)
            .addAspect(TCAspects.METALLUM, 1)
            .addAspect(TCAspects.POTENTIA, 1)
            .addAspect(TCAspects.VENENUM, 1)
            .constructMaterial();
    }

    private static Materials loadCerium() {
        return new MaterialBuilder().setName("Cerium")
            .setDefaultLocalName("Cerium")
            .setElement(Element.Ce)
            .setMetaItemSubID(65)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x7bd490)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1068)
            .setBlastFurnaceTemp(1068)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadChlorine() {
        return new MaterialBuilder().setName("Chlorine")
            .setDefaultLocalName("Chlorine")
            .setElement(Element.Cl)
            .setMetaItemSubID(23)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeCyan)
            .addCell()
            .addPlasma()
            .setMeltingPoint(171)
            .addAspect(TCAspects.AQUA, 2)
            .addAspect(TCAspects.PANNUS, 1)
            .constructMaterial();
    }

    private static Materials loadChrome() {
        return new MaterialBuilder().setName("Chrome")
            .setDefaultLocalName("Chrome")
            .setElement(Element.Cr)
            .setMetaItemSubID(30)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyePink)
            .setRGB(0xffe6e6)
            .setToolSpeed(11.0f)
            .setDurability(256)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2180)
            .setBlastFurnaceTemp(1700)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MACHINA, 1)
            .constructMaterial();
    }

    private static Materials loadCobalt() {
        return new MaterialBuilder().setName("Cobalt")
            .setDefaultLocalName("Cobalt")
            .setElement(Element.Co)
            .setMetaItemSubID(33)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x5050fa)
            .setToolSpeed(8.0f)
            .setDurability(512)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1768)
            .setBlastFurnaceTemp(1700)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.INSTRUMENTUM, 1)
            .constructMaterial();
    }

    private static Materials loadCopper() {
        return new MaterialBuilder().setName("Copper")
            .setDefaultLocalName("Copper")
            .setElement(Element.Cu)
            .setMetaItemSubID(35)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xff6400)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addGearItems()
            .setMeltingPoint(1357)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.PERMUTATIO, 1)
            .constructMaterial();
    }

    private static Materials loadDesh() {
        return new MaterialBuilder().setName("Desh")
            .setDefaultLocalName("Desh")
            .setElement(Element.De)
            .setMetaItemSubID(884)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x282828)
            .setToolSpeed(20.0f)
            .setDurability(1280)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2500)
            .setBlastFurnaceTemp(2500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ALIENIS, 1)
            .constructMaterial();
    }

    private static Materials loadDysprosium() {
        return new MaterialBuilder().setName("Dysprosium")
            .setDefaultLocalName("Dysprosium")
            .setElement(Element.Dy)
            .setMetaItemSubID(73)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x69d150)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1680)
            .setBlastFurnaceTemp(1680)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 3)
            .constructMaterial();
    }

    private static Materials loadEmpty() {
        return new MaterialBuilder().setName("Empty")
            .setDefaultLocalName("Empty")
            .setElement(Element._NULL)
            .setMetaItemSubID(0)
            .setARGB(0xffffffff)
            .setTypes(256) // Only when needed
            .addAspect(TCAspects.VACUOS, 2)
            .constructMaterial();
    }

    private static Materials loadErbium() {
        return new MaterialBuilder().setName("Erbium")
            .setDefaultLocalName("Erbium")
            .setElement(Element.Er)
            .setMetaItemSubID(75)
            .setIconSet(TextureSet.SET_SHINY)
            .setRGB(0xb09851)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1802)
            .setBlastFurnaceTemp(1802)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadEuropium() {
        return new MaterialBuilder().setName("Europium")
            .setDefaultLocalName("Europium")
            .setElement(Element.Eu)
            .setMetaItemSubID(70)
            .setIconSet(TextureSet.SET_SHINY)
            .setRGB(0xf6b5ff)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1099)
            .setBlastFurnaceTemp(1099)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadFlerovium() {
        return new MaterialBuilder().setName("Flerovium_GT5U")
            .setDefaultLocalName("Flerovium")
            .setElement(Element.Fl)
            .setMetaItemSubID(984)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 3)
            .constructMaterial();
    }

    private static Materials loadFluorine() {
        return new MaterialBuilder().setName("Fluorine")
            .setDefaultLocalName("Fluorine")
            .setElement(Element.F)
            .setMetaItemSubID(14)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x7fffffff)
            .addCell()
            .addPlasma()
            .setMeltingPoint(53)
            .addAspect(TCAspects.PERDITIO, 2)
            .constructMaterial();
    }

    private static Materials loadGadolinium() {
        return new MaterialBuilder().setName("Gadolinium")
            .setDefaultLocalName("Gadolinium")
            .setElement(Element.Gd)
            .setMetaItemSubID(71)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x3bba1c)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1585)
            .setBlastFurnaceTemp(1585)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadGallium() {
        return new MaterialBuilder().setName("Gallium")
            .setDefaultLocalName("Gallium")
            .setElement(Element.Ga)
            .setMetaItemSubID(37)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightGray)
            .setRGB(0xdcdcff)
            .setToolSpeed(1.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(302)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ELECTRUM, 1)
            .constructMaterial();
    }

    private static Materials loadGold() {
        return new MaterialBuilder().setName("Gold")
            .setDefaultLocalName("Gold")
            .setElement(Element.Au)
            .setMetaItemSubID(86)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff1e)
            .setToolSpeed(12.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1337)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.LUCRUM, 2)
            .constructMaterial();
    }

    private static Materials loadHolmium() {
        return new MaterialBuilder().setName("Holmium")
            .setDefaultLocalName("Holmium")
            .setElement(Element.Ho)
            .setMetaItemSubID(74)
            .setIconSet(TextureSet.SET_SHINY)
            .setRGB(0x1608a6)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1734)
            .setBlastFurnaceTemp(1734)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadHydrogen() {
        return new MaterialBuilder().setName("Hydrogen")
            .setDefaultLocalName("Hydrogen")
            .setElement(Element.H)
            .setMetaItemSubID(1)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0xf00000ff)
            .addCell()
            .addPlasma()
            .setMeltingPoint(14)
            .setFuelType(1)
            .setFuelPower(20)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial();
    }

    private static Materials loadHelium() {
        return new MaterialBuilder().setName("Helium")
            .setDefaultLocalName("Helium")
            .setElement(Element.He)
            .setMetaItemSubID(4)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0xf0ffff00)
            .addCell()
            .addPlasma()
            .setMeltingPoint(1)
            .addAspect(TCAspects.AER, 2)
            .constructMaterial();
    }

    private static Materials loadIndium() {
        return new MaterialBuilder().setName("Indium")
            .setDefaultLocalName("Indium")
            .setElement(Element.In)
            .setMetaItemSubID(56)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setRGB(0x400080)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(429)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadIridium() {
        return new MaterialBuilder().setName("Iridium")
            .setDefaultLocalName("Iridium")
            .setElement(Element.Ir)
            .setMetaItemSubID(84)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xf0f0f5)
            .setToolSpeed(6.0f)
            .setDurability(2560)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2719)
            .setBlastFurnaceTemp(4500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MACHINA, 1)
            .constructMaterial();
    }

    private static Materials loadIron() {
        return new MaterialBuilder().setName("Iron")
            .setDefaultLocalName("Iron")
            .setElement(Element.Fe)
            .setMetaItemSubID(32)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightGray)
            .setRGB(0xc8c8c8)
            .setToolSpeed(6.0f)
            .setDurability(256)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1811)
            .addAspect(TCAspects.METALLUM, 3)
            .constructMaterial();
    }

    private static Materials loadLanthanum() {
        return new MaterialBuilder().setName("Lanthanum")
            .setDefaultLocalName("Lanthanum")
            .setElement(Element.La)
            .setMetaItemSubID(64)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x8a8a8a)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1193)
            .setBlastFurnaceTemp(1193)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadLead() {
        return new MaterialBuilder().setName("Lead")
            .setDefaultLocalName("Lead")
            .setElement(Element.Pb)
            .setMetaItemSubID(89)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePurple)
            .setRGB(0x8c648c)
            .setToolSpeed(8.0f)
            .setDurability(64)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(600)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ORDO, 1)
            .constructMaterial();
    }

    private static Materials loadLithium() {
        return new MaterialBuilder().setName("Lithium")
            .setDefaultLocalName("Lithium")
            .setElement(Element.Li)
            .setMetaItemSubID(6)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightBlue)
            .setRGB(0xe1dcff)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(454)
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.POTENTIA, 2)
            .constructMaterial();
    }

    private static Materials loadLutetium() {
        return new MaterialBuilder().setName("Lutetium")
            .setDefaultLocalName("Lutetium")
            .setElement(Element.Lu)
            .setMetaItemSubID(78)
            .setIconSet(TextureSet.SET_SHINY)
            .setRGB(0xbc3ec7)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1925)
            .setBlastFurnaceTemp(1925)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadMagic() {
        return new MaterialBuilder().setName("Magic")
            .setDefaultLocalName("Magic")
            .setElement(Element.Ma)
            .setMetaItemSubID(-128)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyePurple)
            .setRGB(0x6400c8)
            .setToolSpeed(8.0f)
            .setDurability(5120)
            .setToolQuality(5)
            .addDustItems()
            .addMetalItems()
            .addGemItems()
            .addOreItems()
            .addCell()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5000)
            .setFuelType(5)
            .setFuelPower(32)
            .addAspect(TCAspects.PRAECANTATIO, 4)
            .constructMaterial();
    }

    private static Materials loadMagnesium() {
        return new MaterialBuilder().setName("Magnesium")
            .setDefaultLocalName("Magnesium")
            .setElement(Element.Mg)
            .setMetaItemSubID(18)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setRGB(0xffc8c8)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(923)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.SANO, 1)
            .constructMaterial();
    }

    private static Materials loadManganese() {
        return new MaterialBuilder().setName("Manganese")
            .setDefaultLocalName("Manganese")
            .setElement(Element.Mn)
            .setMetaItemSubID(31)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xfafafa)
            .setToolSpeed(7.0f)
            .setDurability(512)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(1519)
            .addAspect(TCAspects.METALLUM, 3)
            .constructMaterial();
    }

    private static Materials loadMercury() {
        return new MaterialBuilder().setName("Mercury")
            .setDefaultLocalName("Mercury")
            .setElement(Element.Hg)
            .setMetaItemSubID(87)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightGray)
            .setRGB(0xffdcdc)
            .addCell()
            .addPlasma()
            .setMeltingPoint(234)
            .setFuelType(5)
            .setFuelPower(32)
            .addAspect(TCAspects.METALLUM, 1)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.VENENUM, 1)
            .constructMaterial();
    }

    private static Materials loadMeteoricIron() {
        return new MaterialBuilder().setName("MeteoricIron")
            .setDefaultLocalName("Meteoric Iron")
            .setElement(Element.SpFe)
            .setMetaItemSubID(340)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setRGB(0x643250)
            .setToolSpeed(6.0f)
            .setDurability(384)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(1811)
            .setBlastFurnaceTemp(1000)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .constructMaterial();
    }

    private static Materials loadMolybdenum() {
        return new MaterialBuilder().setName("Molybdenum")
            .setDefaultLocalName("Molybdenum")
            .setElement(Element.Mo)
            .setMetaItemSubID(48)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlue)
            .setRGB(0xb4b4dc)
            .setToolSpeed(7.0f)
            .setDurability(512)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(2896)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.INSTRUMENTUM, 1)
            .constructMaterial();
    }

    private static Materials loadNaquadah() {
        return new MaterialBuilder().setName("Naquadah")
            .setDefaultLocalName("Naquadah")
            .setElement(Element.Nq)
            .setMetaItemSubID(324)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x323232)
            .setToolSpeed(6.0f)
            .setDurability(1280)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5400)
            .setBlastFurnaceTemp(5400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 3)
            .addAspect(TCAspects.RADIO, 1)
            .addAspect(TCAspects.NEBRISUM, 1)
            .constructMaterial();
    }

    private static Materials loadNeodymium() {
        return new MaterialBuilder().setName("Neodymium")
            .setDefaultLocalName("Neodymium")
            .setElement(Element.Nd)
            .setMetaItemSubID(67)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x646464)
            .setToolSpeed(7.0f)
            .setDurability(512)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1297)
            .setBlastFurnaceTemp(1297)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 2)
            .constructMaterial();
    }

    private static Materials loadNeutronium() {
        return new MaterialBuilder().setName("Neutronium")
            .setDefaultLocalName("Neutronium")
            .setElement(Element.Nt)
            .setMetaItemSubID(129)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xfafafa)
            .setToolSpeed(24.0f)
            .setDurability(655360)
            .setToolQuality(6)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(10000)
            .setBlastFurnaceTemp(10000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 4)
            .addAspect(TCAspects.VITREUS, 3)
            .addAspect(TCAspects.ALIENIS, 2)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_LuV);
    }

    private static Materials loadNickel() {
        return new MaterialBuilder().setName("Nickel")
            .setDefaultLocalName("Nickel")
            .setElement(Element.Ni)
            .setMetaItemSubID(34)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightBlue)
            .setRGB(0xc8c8fa)
            .setToolSpeed(6.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1728)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.IGNIS, 1)
            .constructMaterial();
    }

    private static Materials loadNiobium() {
        return new MaterialBuilder().setName("Niobium")
            .setDefaultLocalName("Niobium")
            .setElement(Element.Nb)
            .setMetaItemSubID(47)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0xbeb4c8)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(2750)
            .setBlastFurnaceTemp(2750)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ELECTRUM, 1)
            .constructMaterial();
    }

    private static Materials loadNitrogen() {
        return new MaterialBuilder().setName("Nitrogen")
            .setDefaultLocalName("Nitrogen")
            .setElement(Element.N)
            .setMetaItemSubID(12)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeCyan)
            .setARGB(0xf00096c8)
            .addCell()
            .addPlasma()
            .setMeltingPoint(63)
            .addAspect(TCAspects.AER, 2)
            .constructMaterial();
    }

    private static Materials loadOsmium() {
        return new MaterialBuilder().setName("Osmium")
            .setDefaultLocalName("Osmium")
            .setElement(Element.Os)
            .setMetaItemSubID(83)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x3232ff)
            .setToolSpeed(16.0f)
            .setDurability(1280)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3306)
            .setBlastFurnaceTemp(4500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MACHINA, 1)
            .addAspect(TCAspects.NEBRISUM, 1)
            .constructMaterial();
    }

    private static Materials loadOxygen() {
        return new MaterialBuilder().setName("Oxygen")
            .setDefaultLocalName("Oxygen")
            .setElement(Element.O)
            .setMetaItemSubID(13)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .setARGB(0xf00064c8)
            .addCell()
            .addPlasma()
            .setMeltingPoint(54)
            .addAspect(TCAspects.AER, 1)
            .constructMaterial();
    }

    private static Materials loadPalladium() {
        return new MaterialBuilder().setName("Palladium")
            .setDefaultLocalName("Palladium")
            .setElement(Element.Pd)
            .setMetaItemSubID(52)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGray)
            .setRGB(0x808080)
            .setToolSpeed(8.0f)
            .setDurability(512)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1828)
            .setBlastFurnaceTemp(1828)
            .setBlastFurnaceRequired(true)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 3)
            .constructMaterial();
    }

    private static Materials loadPhosphorus() {
        return new MaterialBuilder().setName("Phosphorus")
            .setDefaultLocalName("Phosphorus")
            .setElement(Element.P)
            .setMetaItemSubID(21)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff00)
            .addDustItems()
            .addPlasma()
            .setMeltingPoint(317)
            .addAspect(TCAspects.IGNIS, 2)
            .addAspect(TCAspects.POTENTIA, 1)
            .constructMaterial();
    }

    private static Materials loadPlatinum() {
        return new MaterialBuilder().setName("Platinum")
            .setDefaultLocalName("Platinum")
            .setElement(Element.Pt)
            .setMetaItemSubID(85)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xffffc8)
            .setToolSpeed(12.0f)
            .setDurability(64)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2041)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.NEBRISUM, 1)
            .constructMaterial();
    }

    private static Materials loadPlutonium() {
        return new MaterialBuilder().setName("Plutonium")
            .setDefaultLocalName("Plutonium 239")
            .setElement(Element.Pu)
            .setMetaItemSubID(100)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLime)
            .setRGB(0xf03232)
            .setToolSpeed(6.0f)
            .setDurability(512)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(912)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 2)
            .constructMaterial();
    }

    private static Materials loadPotassium() {
        return new MaterialBuilder().setName("Potassium")
            .setDefaultLocalName("Potassium")
            .setElement(Element.K)
            .setMetaItemSubID(25)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setRGB(0x9aacdf)
            .addDustItems()
            .addMetalItems()
            .addPlasma()
            .setMeltingPoint(336)
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.POTENTIA, 1)
            .constructMaterial();
    }

    private static Materials loadPraseodymium() {
        return new MaterialBuilder().setName("Praseodymium")
            .setDefaultLocalName("Praseodymium")
            .setElement(Element.Pr)
            .setMetaItemSubID(66)
            .setIconSet(TextureSet.SET_DULL)
            .setRGB(0x75d681)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1208)
            .setBlastFurnaceTemp(1208)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadPromethium() {
        return new MaterialBuilder().setName("Promethium")
            .setDefaultLocalName("Promethium")
            .setElement(Element.Pm)
            .setMetaItemSubID(68)
            .setIconSet(TextureSet.SET_SHINY)
            .setRGB(0x24b535)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1315)
            .setBlastFurnaceTemp(1315)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadRadon() {
        return new MaterialBuilder().setName("Radon")
            .setDefaultLocalName("Radon")
            .setElement(Element.Rn)
            .setMetaItemSubID(93)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyePurple)
            .setARGB(0xf0ff00ff)
            .addCell()
            .addPlasma()
            .setMeltingPoint(202)
            .addAspect(TCAspects.AER, 1)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadRubidium() {
        return new MaterialBuilder().setName("Rubidium")
            .setDefaultLocalName("Rubidium")
            .setElement(Element.Rb)
            .setMetaItemSubID(43)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setRGB(0xf01e1e)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(312)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.VITREUS, 1)
            .constructMaterial();
    }

    private static Materials loadSamarium() {
        return new MaterialBuilder().setName("Samarium")
            .setDefaultLocalName("Samarium")
            .setElement(Element.Sm)
            .setMetaItemSubID(69)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xffffcc)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1345)
            .setBlastFurnaceTemp(1345)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addAspect(TCAspects.MAGNETO, 10)
            .constructMaterial();
    }

    private static Materials loadScandium() {
        return new MaterialBuilder().setName("Scandium")
            .setDefaultLocalName("Scandium")
            .setElement(Element.Sc)
            .setMetaItemSubID(27)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xcccccc)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1814)
            .setBlastFurnaceTemp(1814)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadSilicon() {
        return new MaterialBuilder().setName("Silicon")
            .setDefaultLocalName("Raw Silicon")
            .setElement(Element.Si)
            .setMetaItemSubID(20)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x3c3c50)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(2273)
            .setBlastFurnaceTemp(2273)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.TENEBRAE, 1)
            .constructMaterial();
    }

    private static Materials loadSilver() {
        return new MaterialBuilder().setName("Silver")
            .setDefaultLocalName("Silver")
            .setElement(Element.Ag)
            .setMetaItemSubID(54)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightGray)
            .setRGB(0xdcdcff)
            .setToolSpeed(10.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1234)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.LUCRUM, 1)
            .constructMaterial();
    }

    private static Materials loadSodium() {
        return new MaterialBuilder().setName("Sodium")
            .setDefaultLocalName("Sodium")
            .setElement(Element.Na)
            .setMetaItemSubID(17)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x000096)
            .addDustItems()
            .addCell()
            .addPlasma()
            .setMeltingPoint(370)
            .addAspect(TCAspects.VITREUS, 2)
            .addAspect(TCAspects.LUX, 1)
            .constructMaterial();
    }

    private static Materials loadStrontium() {
        return new MaterialBuilder().setName("Strontium")
            .setDefaultLocalName("Strontium")
            .setElement(Element.Sr)
            .setMetaItemSubID(44)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightGray)
            .setRGB(0xc8c8c8)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addGearItems()
            .setMeltingPoint(1050)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.STRONTIO, 1)
            .constructMaterial();
    }

    private static Materials loadSulfur() {
        return new MaterialBuilder().setName("Sulfur")
            .setDefaultLocalName("Sulfur")
            .setElement(Element.S)
            .setMetaItemSubID(22)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xc8c800)
            .addDustItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(388)
            .addAspect(TCAspects.IGNIS, 1)
            .constructMaterial();
    }

    private static Materials loadTantalum() {
        return new MaterialBuilder().setName("Tantalum")
            .setDefaultLocalName("Tantalum")
            .setElement(Element.Ta)
            .setMetaItemSubID(80)
            .setIconSet(TextureSet.SET_SHINY)
            .setRGB(0x69b7ff)
            .setToolSpeed(6.0f)
            .setDurability(2560)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(3290)
            .setBlastFurnaceTemp(3290)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.VINCULUM, 1)
            .constructMaterial();
    }

    private static Materials loadTellurium() {
        return new MaterialBuilder().setName("Tellurium")
            .setDefaultLocalName("Tellurium")
            .setElement(Element.Te)
            .setMetaItemSubID(59)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setRGB(0xceff56)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(722)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadTerbium() {
        return new MaterialBuilder().setName("Terbium")
            .setDefaultLocalName("Terbium")
            .setElement(Element.Tb)
            .setMetaItemSubID(72)
            .setIconSet(TextureSet.SET_METALLIC)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1629)
            .setBlastFurnaceTemp(1629)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadThorium() {
        return new MaterialBuilder().setName("Thorium")
            .setDefaultLocalName("Thorium")
            .setElement(Element.Th)
            .setMetaItemSubID(96)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x001e00)
            .setToolSpeed(6.0f)
            .setDurability(512)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(2115)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadThulium() {
        return new MaterialBuilder().setName("Thulium")
            .setDefaultLocalName("Thulium")
            .setElement(Element.Tm)
            .setMetaItemSubID(76)
            .setIconSet(TextureSet.SET_SHINY)
            .setRGB(0x596bc2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1818)
            .setBlastFurnaceTemp(1818)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadTin() {
        return new MaterialBuilder().setName("Tin")
            .setDefaultLocalName("Tin")
            .setElement(Element.Sn)
            .setMetaItemSubID(57)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xdcdcdc)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addGearItems()
            .setMeltingPoint(505)
            .setBlastFurnaceTemp(505)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.VITREUS, 1)
            .constructMaterial();
    }

    private static Materials loadTitanium() {
        return new MaterialBuilder().setName("Titanium")
            .setDefaultLocalName("Titanium")
            .setElement(Element.Ti)
            .setMetaItemSubID(28)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePurple)
            .setRGB(0xdca0f0)
            .setToolSpeed(7.0f)
            .setDurability(1600)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1941)
            .setBlastFurnaceTemp(1940)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.TUTAMEN, 1)
            .constructMaterial();
    }

    private static Materials loadTritanium() {
        return new MaterialBuilder().setName("Tritanium")
            .setDefaultLocalName("Tritanium")
            .setElement(Element.Tn)
            .setMetaItemSubID(329)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setRGB(0x600000)
            .setToolSpeed(20.0f)
            .setDurability(1435392)
            .setToolQuality(6)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(9900)
            .setBlastFurnaceTemp(9900)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ORDO, 2)
            .constructMaterial();
    }

    private static Materials loadTungsten() {
        return new MaterialBuilder().setName("Tungsten")
            .setDefaultLocalName("Tungsten")
            .setElement(Element.W)
            .setMetaItemSubID(81)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x323232)
            .setToolSpeed(7.0f)
            .setDurability(2560)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3695)
            .setBlastFurnaceTemp(3000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 3)
            .addAspect(TCAspects.TUTAMEN, 1)
            .constructMaterial();
    }

    private static Materials loadUranium() {
        return new MaterialBuilder().setName("Uranium")
            .setDefaultLocalName("Uranium 238")
            .setElement(Element.U)
            .setMetaItemSubID(98)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x32f032)
            .setToolSpeed(6.0f)
            .setDurability(512)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(1405)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadVanadium() {
        return new MaterialBuilder().setName("Vanadium")
            .setDefaultLocalName("Vanadium")
            .setElement(Element.V)
            .setMetaItemSubID(29)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x323232)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(2183)
            .setBlastFurnaceTemp(2183)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadYtterbium() {
        return new MaterialBuilder().setName("Ytterbium")
            .setDefaultLocalName("Ytterbium")
            .setElement(Element.Yb)
            .setMetaItemSubID(77)
            .setIconSet(TextureSet.SET_SHINY)
            .setRGB(0x2cc750)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1097)
            .setBlastFurnaceTemp(1097)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadYttrium() {
        return new MaterialBuilder().setName("Yttrium")
            .setDefaultLocalName("Yttrium")
            .setElement(Element.Y)
            .setMetaItemSubID(45)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0xdcfadc)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(1799)
            .setBlastFurnaceTemp(1799)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .constructMaterial();
    }

    private static Materials loadZinc() {
        return new MaterialBuilder().setName("Zinc")
            .setDefaultLocalName("Zinc")
            .setElement(Element.Zn)
            .setMetaItemSubID(36)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xfaf0f0)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(692)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.SANO, 1)
            .constructMaterial();
    }

    private static void loadIsotopes() {
        Materials.Deuterium = loadDeuterium();
        Materials.Helium_3 = loadHelium3();
        Materials.Plutonium241 = loadPlutonium241();
        Materials.Tritium = loadTritium();
        Materials.Uranium235 = loadUranium235();
    }

    private static Materials loadDeuterium() {
        return new MaterialBuilder().setName("Deuterium")
            .setDefaultLocalName("Deuterium")
            .setElement(Element.D)
            .setMetaItemSubID(2)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0xf0ffff00)
            .addCell()
            .addPlasma()
            .setMeltingPoint(14)
            .addAspect(TCAspects.AQUA, 3)
            .constructMaterial();
    }

    private static Materials loadHelium3() {
        return new MaterialBuilder().setName("Helium_3")
            .setDefaultLocalName("Helium-3")
            .setElement(Element.He_3)
            .setMetaItemSubID(5)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setARGB(0xf0ffff00)
            .addCell()
            .addPlasma()
            .setMeltingPoint(1)
            .addAspect(TCAspects.AER, 3)
            .constructMaterial();
    }

    private static Materials loadPlutonium241() {
        return new MaterialBuilder().setName("Plutonium241")
            .setDefaultLocalName("Plutonium 241")
            .setElement(Element.Pu_241)
            .setMetaItemSubID(101)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLime)
            .setRGB(0xfa4646)
            .setToolSpeed(6.0f)
            .setDurability(512)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(912)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 3)
            .constructMaterial();
    }

    private static Materials loadTritium() {
        return new MaterialBuilder().setName("Tritium")
            .setDefaultLocalName("Tritium")
            .setElement(Element.T)
            .setMetaItemSubID(3)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setARGB(0xf0ff0000)
            .addCell()
            .addPlasma()
            .setMeltingPoint(14)
            .addAspect(TCAspects.AQUA, 4)
            .constructMaterial();
    }

    private static Materials loadUranium235() {
        return new MaterialBuilder().setName("Uranium235")
            .setDefaultLocalName("Uranium 235")
            .setElement(Element.U_235)
            .setMetaItemSubID(97)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x46fa46)
            .setToolSpeed(6.0f)
            .setDurability(512)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(1405)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 2)
            .constructMaterial();
    }

    private static void loadWaterLine() {
        Materials.FlocculationWasteLiquid = loadFlocculationWasteLiquid();
        Materials.Grade1PurifiedWater = loadGrade1PurifiedWater();
        Materials.Grade2PurifiedWater = loadGrade2PurifiedWater();
        Materials.Grade3PurifiedWater = loadGrade3PurifiedWater();
        Materials.Grade4PurifiedWater = loadGrade4PurifiedWater();
        Materials.Grade5PurifiedWater = loadGrade5PurifiedWater();
        Materials.Grade6PurifiedWater = loadGrade6PurifiedWater();
        Materials.Grade7PurifiedWater = loadGrade7PurifiedWater();
        Materials.Grade8PurifiedWater = loadGrade8PurifiedWater();
    }

    private static Materials loadFlocculationWasteLiquid() {
        return new MaterialBuilder().setName("FlocculationWasteLiquid")
            .setDefaultLocalName("Flocculation Waste Liquid")
            .setMetaItemSubID(562)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x003d3a52)
            .addCell()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadGrade1PurifiedWater() {
        return new MaterialBuilder().setName("Grade1PurifiedWater")
            .setDefaultLocalName("Filtered Water (Grade 1)")
            .setMetaItemSubID(554)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x003f4cfd)
            .addCell()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadGrade2PurifiedWater() {
        return new MaterialBuilder().setName("Grade2PurifiedWater")
            .setDefaultLocalName("Ozonated Water (Grade 2)")
            .setMetaItemSubID(555)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x005d5dfe)
            .addCell()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadGrade3PurifiedWater() {
        return new MaterialBuilder().setName("Grade3PurifiedWater")
            .setDefaultLocalName("Flocculated Water (Grade 3)")
            .setMetaItemSubID(556)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00736dfe)
            .addCell()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadGrade4PurifiedWater() {
        return new MaterialBuilder().setName("Grade4PurifiedWater")
            .setDefaultLocalName("pH Neutralized Water (Grade 4)")
            .setMetaItemSubID(557)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00877eff)
            .addCell()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadGrade5PurifiedWater() {
        return new MaterialBuilder().setName("Grade5PurifiedWater")
            .setDefaultLocalName("Extreme-Temperature Treated Water (Grade 5)")
            .setMetaItemSubID(558)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x009890ff)
            .addCell()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadGrade6PurifiedWater() {
        return new MaterialBuilder().setName("Grade6PurifiedWater")
            .setDefaultLocalName("Ultraviolet Treated Electrically Neutral Water (Grade 6)")
            .setMetaItemSubID(559)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00a8a1ff)
            .addCell()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadGrade7PurifiedWater() {
        return new MaterialBuilder().setName("Grade7PurifiedWater")
            .setDefaultLocalName("Degassed Decontaminant-Free Water (Grade 7)")
            .setMetaItemSubID(560)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00b7b3ff)
            .addCell()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadGrade8PurifiedWater() {
        return new MaterialBuilder().setName("Grade8PurifiedWater")
            .setDefaultLocalName("Subatomically Perfect Water (Grade 8)")
            .setMetaItemSubID(561)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x00c5c5ff)
            .addCell()
            .setMeltingPoint(273)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static void loadRandom() {
        Materials.AnyBronze = loadAnyBronze();
        Materials.AnyCopper = loadAnyCopper();
        Materials.AnyIron = loadAnyIron();
        Materials.AnyRubber = loadAnyRubber();
        Materials.AnySyntheticRubber = loadAnySyntheticRubber();
        Materials.BrickNether = loadBrickNether();
        Materials.Cobblestone = loadCobblestone();
        Materials.Crystal = loadCrystal();
        Materials.Metal = loadMetal();
        Materials.Organic = loadOrganic();
        Materials.Quartz = loadQuartz();
        Materials.Unknown = loadUnknown();
    }

    private static Materials loadAnyBronze() {
        return new MaterialBuilder().setName("AnyBronze")
            .setDefaultLocalName("AnyBronze")
            .setIconSet(TextureSet.SET_SHINY)
            .constructMaterial();
    }

    private static Materials loadAnyCopper() {
        return new MaterialBuilder().setName("AnyCopper")
            .setDefaultLocalName("AnyCopper")
            .setMetaItemSubID(-1)
            .setIconSet(TextureSet.SET_SHINY)
            .constructMaterial();
    }

    private static Materials loadAnyIron() {
        return new MaterialBuilder().setName("AnyIron")
            .setDefaultLocalName("AnyIron")
            .setIconSet(TextureSet.SET_SHINY)
            .constructMaterial();
    }

    private static Materials loadAnyRubber() {
        return new MaterialBuilder().setName("AnyRubber")
            .setDefaultLocalName("AnyRubber")
            .setIconSet(TextureSet.SET_SHINY)
            .constructMaterial();
    }

    private static Materials loadAnySyntheticRubber() {
        return new MaterialBuilder().setName("AnySyntheticRubber")
            .setDefaultLocalName("AnySyntheticRubber")
            .setIconSet(TextureSet.SET_SHINY)
            .constructMaterial();
    }

    private static Materials loadBrickNether() {
        return new MaterialBuilder().setName("BrickNether")
            .setDefaultLocalName("BrickNether")
            .setIconSet(TextureSet.SET_DULL)
            .constructMaterial();
    }

    private static Materials loadCobblestone() {
        return new MaterialBuilder().setName("Cobblestone")
            .setDefaultLocalName("Cobblestone")
            .setIconSet(TextureSet.SET_DULL)
            .constructMaterial();
    }

    private static Materials loadCrystal() {
        return new MaterialBuilder().setName("Crystal")
            .setDefaultLocalName("Crystal")
            .setIconSet(TextureSet.SET_SHINY)
            .constructMaterial();
    }

    private static Materials loadMetal() {
        return new MaterialBuilder().setName("Metal")
            .setDefaultLocalName("Metal")
            .setIconSet(TextureSet.SET_METALLIC)
            .constructMaterial();
    }

    private static Materials loadOrganic() {
        return new MaterialBuilder().setName("Organic")
            .setDefaultLocalName("Organic")
            .setMetaItemSubID(-1)
            .setIconSet(TextureSet.SET_LEAF)
            .constructMaterial();
    }

    private static Materials loadQuartz() {
        return new MaterialBuilder().setName("Quartz")
            .setDefaultLocalName("Quartz")
            .setIconSet(TextureSet.SET_QUARTZ)
            .constructMaterial();
    }

    private static Materials loadUnknown() {
        return new MaterialBuilder().setName("Unknown")
            .setDefaultLocalName("Unknown")
            .setIconSet(TextureSet.SET_DULL)
            .constructMaterial();
    }

    private static void loadDontCare() {
        Materials.Alfium = loadAlfium();
        Materials.Aquamarine = loadAquamarine();
        Materials.DarkThaumium = loadDarkThaumium();
        Materials.Draconium = loadDraconium();
        Materials.DraconiumAwakened = loadDraconiumAwakened();
        Materials.Ender = loadEnder();
        Materials.Fluix = loadFluix();
        Materials.Flux = loadFlux();
        Materials.HeeEndium = loadHeeEndium();
        Materials.InfusedTeslatite = loadInfusedTeslatite();
        Materials.IridiumSodiumOxide = loadIridiumSodiumOxide();
        Materials.Mutation = loadMutation();
        Materials.OsmiumTetroxide = loadOsmiumTetroxide();
        Materials.PhasedGold = loadPhasedGold();
        Materials.PhasedIron = loadPhasedIron();
        Materials.PlatinumGroupSludge = loadPlatinumGroupSludge();
        Materials.PurpleAlloy = loadPurpleAlloy();
        Materials.RubberTreeSap = loadRubberTreeSap();
        Materials.Serpentine = loadSerpentine();
        Materials.SodiumPeroxide = loadSodiumPeroxide();
        Materials.Teslatite = loadTeslatite();
    }

    private static Materials loadAlfium() {
        return new MaterialBuilder().setName("Alfium")
            .setDefaultLocalName("Alfium")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadAquamarine() {
        return new MaterialBuilder().setName("Aquamarine")
            .setDefaultLocalName("Aquamarine")
            .addDustItems()
            .addGemItems()
            .constructMaterial();
    }

    private static Materials loadDarkThaumium() {
        return new MaterialBuilder().setName("DarkThaumium")
            .setDefaultLocalName("Dark Thaumium")
            .addDustItems()
            .addMetalItems()
            .constructMaterial();
    }

    private static Materials loadDraconium() {
        return new MaterialBuilder().setName("Draconium")
            .setDefaultLocalName("Draconium")
            .setMetaItemSubID(975)
            .setIconSet(TextureSet.SET_DRACONIUM)
            .setColor(Dyes.dyePink)
            .setRGB(0x7a44b0)
            .setToolSpeed(20.0f)
            .setDurability(32768)
            .setToolQuality(7)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5000)
            .setBlastFurnaceTemp(7200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .constructMaterial()
            .setHasCorrespondingPlasma(true);
    }

    private static Materials loadDraconiumAwakened() {
        return new MaterialBuilder().setName("DraconiumAwakened")
            .setDefaultLocalName("Awakened Draconium")
            .setMetaItemSubID(976)
            .setIconSet(TextureSet.SET_AWOKEN_DRACONIUM)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xf44e00)
            .setToolSpeed(40.0f)
            .setDurability(65536)
            .setToolQuality(9)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(9900)
            .setBlastFurnaceTemp(9900)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .constructMaterial()
            .setHasCorrespondingPlasma(true);
    }

    private static Materials loadEnder() {
        return new MaterialBuilder().setName("Ender")
            .setDefaultLocalName("Ender")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadFluix() {
        return new MaterialBuilder().setName("Fluix")
            .setDefaultLocalName("Fluix")
            .addDustItems()
            .addGemItems()
            .constructMaterial();
    }

    private static Materials loadFlux() {
        return new MaterialBuilder().setName("Flux")
            .setDefaultLocalName("Flux")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadHeeEndium() {
        return new MaterialBuilder().setName("HeeEndium")
            .setDefaultLocalName("Endium")
            .setMetaItemSubID(770)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightBlue)
            .setRGB(0xa5dcfa)
            .setToolSpeed(16.0f)
            .setDurability(1024)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .constructMaterial();
    }

    private static Materials loadInfusedTeslatite() {
        return new MaterialBuilder().setName("InfusedTeslatite")
            .setDefaultLocalName("Infused Teslatite")
            .setRGB(0x64b4ff)
            .constructMaterial();
    }

    private static Materials loadIridiumSodiumOxide() {
        return new MaterialBuilder().setName("IridiumSodiumOxide")
            .setDefaultLocalName("Iridium Sodium Oxide")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadMutation() {
        return new MaterialBuilder().setName("Mutation")
            .setDefaultLocalName("Mutation")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadOsmiumTetroxide() {
        return new MaterialBuilder().setName("OsmiumTetroxide")
            .setDefaultLocalName("Osmium Tetroxide")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadPhasedGold() {
        return new MaterialBuilder().setName("PhasedGold")
            .setDefaultLocalName("Phased Gold")
            .addDustItems()
            .addMetalItems()
            .setBlastFurnaceTemp(1800)
            .setBlastFurnaceRequired(true)
            .constructMaterial();
    }

    private static Materials loadPhasedIron() {
        return new MaterialBuilder().setName("PhasedIron")
            .setDefaultLocalName("Phased Iron")
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(3300)
            .setBlastFurnaceTemp(3300)
            .setBlastFurnaceRequired(true)
            .constructMaterial();
    }

    private static Materials loadPlatinumGroupSludge() {
        return new MaterialBuilder().setName("PlatinumGroupSludge")
            .setDefaultLocalName("Platinum Group Sludge")
            .setMetaItemSubID(241)
            .setIconSet(TextureSet.SET_POWDER)
            .setRGB(0x001e00)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadPurpleAlloy() {
        return new MaterialBuilder().setName("PurpleAlloy")
            .setDefaultLocalName("Purple Alloy")
            .setRGB(0x64b4ff)
            .constructMaterial();
    }

    private static Materials loadRubberTreeSap() {
        return new MaterialBuilder().setName("RubberTreeSap")
            .setDefaultLocalName("Rubber Tree Sap")
            .constructMaterial();
    }

    private static Materials loadSerpentine() {
        return new MaterialBuilder().setName("Serpentine")
            .setDefaultLocalName("Serpentine")
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadSodiumPeroxide() {
        return new MaterialBuilder().setName("SodiumPeroxide")
            .setDefaultLocalName("Sodium Peroxide")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadTeslatite() {
        return new MaterialBuilder().setName("Teslatite")
            .setDefaultLocalName("Teslatite")
            .setRGB(0x3cb4c8)
            .addDustItems()
            .constructMaterial();
    }

    private static void loadUnknownComponents() {
        Materials.Adamantium = loadAdamantium();
        Materials.Adamite = loadAdamite();
        Materials.Adluorite = loadAdluorite();
        Materials.Agate = loadAgate();
        Materials.Alduorite = loadAlduorite();
        Materials.Amber = loadAmber();
        Materials.Ammonium = loadAmmonium();
        Materials.Amordrine = loadAmordrine();
        Materials.Andesite = loadAndesite();
        Materials.Angmallen = loadAngmallen();
        Materials.Ardite = loadArdite();
        Materials.Aredrite = loadAredrite();
        Materials.Atlarus = loadAtlarus();
        Materials.Bitumen = loadBitumen();
        Materials.Black = loadBlack();
        Materials.Blizz = loadBlizz();
        Materials.Bloodstone = loadBloodstone();
        Materials.Blueschist = loadBlueschist();
        Materials.Bluestone = loadBluestone();
        Materials.Blutonium = loadBlutonium();
        Materials.Carmot = loadCarmot();
        Materials.Celenegil = loadCelenegil();
        Materials.CertusQuartz = loadCertusQuartz();
        Materials.CertusQuartzCharged = loadCertusQuartzCharged();
        Materials.Ceruclase = loadCeruclase();
        Materials.Chert = loadChert();
        Materials.Chimerite = loadChimerite();
        Materials.Chrysocolla = loadChrysocolla();
        Materials.Citrine = loadCitrine();
        Materials.CobaltHexahydrate = loadCobaltHexahydrate();
        Materials.ConstructionFoam = loadConstructionFoam();
        Materials.Coral = loadCoral();
        Materials.CrudeOil = loadCrudeOil();
        Materials.CrystalFlux = loadCrystalFlux();
        Materials.Cyanite = loadCyanite();
        Materials.Dacite = loadDacite();
        Materials.DarkIron = loadDarkIron();
        Materials.DarkStone = loadDarkStone();
        Materials.Demonite = loadDemonite();
        Materials.Desichalkos = loadDesichalkos();
        Materials.Dilithium = loadDilithium();
        Materials.Draconic = loadDraconic();
        Materials.Drulloy = loadDrulloy();
        Materials.Duranium = loadDuranium();
        Materials.Eclogite = loadEclogite();
        Materials.ElectrumFlux = loadElectrumFlux();
        Materials.Emery = loadEmery();
        Materials.EnderiumBase = loadEnderiumBase();
        Materials.Energized = loadEnergized();
        Materials.Epidote = loadEpidote();
        Materials.Eximite = loadEximite();
        Materials.FierySteel = loadFierySteel();
        Materials.Firestone = loadFirestone();
        Materials.Fluorite = loadFluorite();
        Materials.FoolsRuby = loadFoolsRuby();
        Materials.Force = loadForce();
        Materials.Forcicium = loadForcicium();
        Materials.Forcillium = loadForcillium();
        Materials.Gabbro = loadGabbro();
        Materials.Glowstone = loadGlowstone();
        Materials.Gneiss = loadGneiss();
        Materials.Graphene = loadGraphene();
        Materials.Graphite = loadGraphite();
        Materials.Greenschist = loadGreenschist();
        Materials.Greenstone = loadGreenstone();
        Materials.Greywacke = loadGreywacke();
        Materials.Haderoth = loadHaderoth();
        Materials.Hematite = loadHematite();
        Materials.Hepatizon = loadHepatizon();
        Materials.HSLA = loadHSLA();
        Materials.Ignatius = loadIgnatius();
        Materials.Infernal = loadInfernal();
        Materials.Infuscolium = loadInfuscolium();
        Materials.InfusedAir = loadInfusedAir();
        Materials.InfusedDull = loadInfusedDull();
        Materials.InfusedEarth = loadInfusedEarth();
        Materials.InfusedEntropy = loadInfusedEntropy();
        Materials.InfusedFire = loadInfusedFire();
        Materials.InfusedGold = loadInfusedGold();
        Materials.InfusedOrder = loadInfusedOrder();
        Materials.InfusedVis = loadInfusedVis();
        Materials.InfusedWater = loadInfusedWater();
        Materials.Inolashite = loadInolashite();
        Materials.Invisium = loadInvisium();
        Materials.Jade = loadJade();
        Materials.Kalendrite = loadKalendrite();
        Materials.Komatiite = loadKomatiite();
        Materials.Lava = loadLava();
        Materials.Lemurite = loadLemurite();
        Materials.Limestone = loadLimestone();
        Materials.Magma = loadMagma();
        Materials.Mawsitsit = loadMawsitsit();
        Materials.Mercassium = loadMercassium();
        Materials.MeteoricSteel = loadMeteoricSteel();
        Materials.Meteorite = loadMeteorite();
        Materials.Meutoite = loadMeutoite();
        Materials.Migmatite = loadMigmatite();
        Materials.Mimichite = loadMimichite();
        Materials.Moonstone = loadMoonstone();
        Materials.NaquadahAlloy = loadNaquadahAlloy();
        Materials.NaquadahEnriched = loadNaquadahEnriched();
        Materials.Naquadria = loadNaquadria();
        Materials.Nether = loadNether();
        Materials.NetherBrick = loadNetherBrick();
        Materials.NetherQuartz = loadNetherQuartz();
        Materials.NetherStar = loadNetherStar();
        Materials.ObsidianFlux = loadObsidianFlux();
        Materials.Oilsands = loadOilsands();
        Materials.Onyx = loadOnyx();
        Materials.Orichalcum = loadOrichalcum();
        Materials.Osmonium = loadOsmonium();
        Materials.Oureclase = loadOureclase();
        Materials.Painite = loadPainite();
        Materials.Peanutwood = loadPeanutwood();
        Materials.Petroleum = loadPetroleum();
        Materials.Pewter = loadPewter();
        Materials.Phoenixite = loadPhoenixite();
        Materials.Prometheum = loadPrometheum();
        Materials.Quartzite = loadQuartzite();
        Materials.Randomite = loadRandomite();
        Materials.Rhyolite = loadRhyolite();
        Materials.Rubracium = loadRubracium();
        Materials.Sand = loadSand();
        Materials.Sanguinite = loadSanguinite();
        Materials.Siltstone = loadSiltstone();
        Materials.Sunstone = loadSunstone();
        Materials.Tar = loadTar();
        Materials.Tartarite = loadTartarite();
        Materials.UUAmplifier = loadUUAmplifier();
        Materials.UUMatter = loadUUMatter();
        Materials.Void = loadVoid();
        Materials.Voidstone = loadVoidstone();
        Materials.Vulcanite = loadVulcanite();
        Materials.Vyroxeres = loadVyroxeres();
        Materials.Yellorium = loadYellorium();
        Materials.Zectium = loadZectium();
    }

    private static Materials loadAdamantium() {
        return new MaterialBuilder().setName("Adamantium")
            .setDefaultLocalName("Adamantium")
            .setMetaItemSubID(319)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightGray)
            .setToolSpeed(32.0f)
            .setDurability(8192)
            .setToolQuality(10)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(7200)
            .setBlastFurnaceTemp(7200)
            .setBlastFurnaceRequired(true)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .constructMaterial()
            .setTurbineMultipliers(1, 5, 1);
    }

    private static Materials loadAdamite() {
        return new MaterialBuilder().setName("Adamite")
            .setDefaultLocalName("Adamite")
            .setColor(Dyes.dyeLightGray)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadAdluorite() {
        return new MaterialBuilder().setName("Adluorite")
            .setDefaultLocalName("Adluorite")
            .setColor(Dyes.dyeLightBlue)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .constructMaterial();
    }

    private static Materials loadAgate() {
        return new MaterialBuilder().setName("Agate")
            .setDefaultLocalName("Agate")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadAlduorite() {
        return new MaterialBuilder().setName("Alduorite")
            .setDefaultLocalName("Alduorite")
            .setMetaItemSubID(485)
            .setIconSet(TextureSet.SET_SHINY)
            .setRGB(0x9fb4b4)
            .setToolSpeed(32.0f)
            .setDurability(8192)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(6600)
            .setBlastFurnaceTemp(6600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .constructMaterial()
            .setTurbineMultipliers(6, 1, 1);
    }

    private static Materials loadAmber() {
        return new MaterialBuilder().setName("Amber")
            .setDefaultLocalName("Amber")
            .setMetaItemSubID(514)
            .setIconSet(TextureSet.SET_RUBY)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x7fff8000)
            .setToolSpeed(4.0f)
            .setDurability(128)
            .setToolQuality(2)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .setFuelType(5)
            .setFuelPower(3)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 10)
            .addMaterial(Materials.Hydrogen, 10)
            .addMaterial(Materials.Oxygen, 16)
            .addAspect(TCAspects.VINCULUM, 2)
            .addAspect(TCAspects.VITREUS, 1)
            .constructMaterial();
    }

    private static Materials loadAmmonium() {
        return new MaterialBuilder().setName("Ammonium")
            .setDefaultLocalName("Ammonium")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadAmordrine() {
        return new MaterialBuilder().setName("Amordrine")
            .setDefaultLocalName("Amordrine")
            .setToolSpeed(6.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadAndesite() {
        return new MaterialBuilder().setName("Andesite")
            .setDefaultLocalName("Andesite")
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadAngmallen() {
        return new MaterialBuilder().setName("Angmallen")
            .setDefaultLocalName("Angmallen")
            .setMetaItemSubID(958)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0xd7e18a)
            .setToolSpeed(10.0f)
            .setDurability(128)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadArdite() {
        return new MaterialBuilder().setName("Ardite")
            .setDefaultLocalName("Ardite")
            .setMetaItemSubID(382)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setRGB(0xfa8100)
            .setToolSpeed(18.0f)
            .setDurability(1024)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1600)
            .setBlastFurnaceTemp(1600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .constructMaterial()
            .setHasCorrespondingPlasma(true);
    }

    private static Materials loadAredrite() {
        return new MaterialBuilder().setName("Aredrite")
            .setDefaultLocalName("Aredrite")
            .setColor(Dyes.dyeYellow)
            .setRGB(0xff0000)
            .setToolSpeed(6.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadAtlarus() {
        return new MaterialBuilder().setName("Atlarus")
            .setDefaultLocalName("Atlarus")
            .setMetaItemSubID(965)
            .setIconSet(TextureSet.SET_METALLIC)
            .setToolSpeed(6.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadBitumen() {
        return new MaterialBuilder().setName("Bitumen")
            .setDefaultLocalName("Bitumen")
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadBlack() {
        return new MaterialBuilder().setName("Black")
            .setDefaultLocalName("Black")
            .setColor(Dyes.dyeBlack)
            .setRGB(0x000000)
            .constructMaterial();
    }

    private static Materials loadBlizz() {
        return new MaterialBuilder().setName("Blizz")
            .setDefaultLocalName("Blizz")
            .setMetaItemSubID(851)
            .setIconSet(TextureSet.SET_SHINY)
            .setRGB(0xdce9ff)
            .addDustItems()
            .constructMaterial()
            .disableAutoGeneratedRecycleRecipes();
    }

    private static Materials loadBloodstone() {
        return new MaterialBuilder().setName("Bloodstone")
            .setDefaultLocalName("Bloodstone")
            .setColor(Dyes.dyeRed)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadBlueschist() {
        return new MaterialBuilder().setName("Blueschist")
            .setDefaultLocalName("Blueschist")
            .setMetaItemSubID(852)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightBlue)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadBluestone() {
        return new MaterialBuilder().setName("Bluestone")
            .setDefaultLocalName("Bluestone")
            .setMetaItemSubID(813)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlue)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadBlutonium() {
        return new MaterialBuilder().setName("Blutonium")
            .setDefaultLocalName("Blutonium")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x0000ff)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadCarmot() {
        return new MaterialBuilder().setName("Carmot")
            .setDefaultLocalName("Carmot")
            .setMetaItemSubID(962)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0xd9cd8c)
            .setToolSpeed(16.0f)
            .setDurability(128)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadCelenegil() {
        return new MaterialBuilder().setName("Celenegil")
            .setDefaultLocalName("Celenegil")
            .setMetaItemSubID(964)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x94cc48)
            .setToolSpeed(10.0f)
            .setDurability(4096)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadCertusQuartz() {
        return new MaterialBuilder().setName("CertusQuartz")
            .setDefaultLocalName("Certus Quartz")
            .setMetaItemSubID(516)
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeLightGray)
            .setRGB(0xd2d2e6)
            .setToolSpeed(5.0f)
            .setDurability(32)
            .setToolQuality(1)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addAspect(TCAspects.POTENTIA, 1)
            .addAspect(TCAspects.VITREUS, 1)
            .constructMaterial();
    }

    private static Materials loadCertusQuartzCharged() {
        return new MaterialBuilder().setName("ChargedCertusQuartz")
            .setDefaultLocalName("Charged Certus Quartz")
            .setMetaItemSubID(517)
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeLightGray)
            .setRGB(0xddddec)
            .setToolSpeed(5.0f)
            .setDurability(32)
            .setToolQuality(1)
            .addOreItems()
            .addAspect(TCAspects.POTENTIA, 1)
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.ELECTRUM, 1)
            .constructMaterial();
    }

    private static Materials loadCeruclase() {
        return new MaterialBuilder().setName("Ceruclase")
            .setDefaultLocalName("Ceruclase")
            .setMetaItemSubID(952)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x8cbdd0)
            .setToolSpeed(32.0f)
            .setDurability(1280)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(6600)
            .setBlastFurnaceTemp(6600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .constructMaterial()
            .setTurbineMultipliers(1, 22, 1);
    }

    private static Materials loadChert() {
        return new MaterialBuilder().setName("Chert")
            .setDefaultLocalName("Chert")
            .setMetaItemSubID(857)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadChimerite() {
        return new MaterialBuilder().setName("Chimerite")
            .setDefaultLocalName("Chimerite")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadChrysocolla() {
        return new MaterialBuilder().setName("Chrysocolla")
            .setDefaultLocalName("Chrysocolla")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadCitrine() {
        return new MaterialBuilder().setName("Citrine")
            .setDefaultLocalName("Citrine")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadCobaltHexahydrate() {
        return new MaterialBuilder().setName("CobaltHexahydrate")
            .setDefaultLocalName("Cobalt Hexahydrate")
            .setMetaItemSubID(853)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x5050fa)
            .addDustItems()
            .addCell()
            .constructMaterial();
    }

    private static Materials loadConstructionFoam() {
        return new MaterialBuilder().setName("ConstructionFoam")
            .setDefaultLocalName("Construction Foam")
            .setMetaItemSubID(854)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setRGB(0x808080)
            .addDustItems()
            .addCell()
            .addToolHeadItems()
            .addGearItems()
            .constructMaterial();
    }

    private static Materials loadCoral() {
        return new MaterialBuilder().setName("Coral")
            .setDefaultLocalName("Coral")
            .setRGB(0xff80ff)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadCrudeOil() {
        return new MaterialBuilder().setName("CrudeOil")
            .setDefaultLocalName("Crude Oil")
            .setMetaItemSubID(858)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x0a0a0a)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadCrystalFlux() {
        return new MaterialBuilder().setName("CrystalFlux")
            .setDefaultLocalName("Flux Crystal")
            .setIconSet(TextureSet.SET_QUARTZ)
            .setRGB(0x643264)
            .addDustItems()
            .addGemItems()
            .constructMaterial();
    }

    private static Materials loadCyanite() {
        return new MaterialBuilder().setName("Cyanite")
            .setDefaultLocalName("Cyanite")
            .setColor(Dyes.dyeCyan)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadDacite() {
        return new MaterialBuilder().setName("Dacite")
            .setDefaultLocalName("Dacite")
            .setMetaItemSubID(859)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightGray)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadDarkIron() {
        return new MaterialBuilder().setName("DarkIron")
            .setDefaultLocalName("Deep Dark Iron")
            .setMetaItemSubID(342)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePurple)
            .setRGB(0x37283c)
            .setToolSpeed(7.0f)
            .setDurability(384)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadDarkStone() {
        return new MaterialBuilder().setName("DarkStone")
            .setDefaultLocalName("Dark Stone")
            .setColor(Dyes.dyeBlack)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadDemonite() {
        return new MaterialBuilder().setName("Demonite")
            .setDefaultLocalName("Demonite")
            .setColor(Dyes.dyeRed)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadDesichalkos() {
        return new MaterialBuilder().setName("Desichalkos")
            .setDefaultLocalName("Desichalkos")
            .setToolSpeed(6.0f)
            .setDurability(1280)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadDilithium() {
        return new MaterialBuilder().setName("Dilithium")
            .setDefaultLocalName("Dilithium")
            .setMetaItemSubID(515)
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x7ffffafa)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addCell()
            .constructMaterial();
    }

    private static Materials loadDraconic() {
        return new MaterialBuilder().setName("Draconic")
            .setDefaultLocalName("Draconic")
            .setColor(Dyes.dyeRed)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadDrulloy() {
        return new MaterialBuilder().setName("Drulloy")
            .setDefaultLocalName("Drulloy")
            .setColor(Dyes.dyeRed)
            .addDustItems()
            .addCell()
            .constructMaterial();
    }

    private static Materials loadDuranium() {
        return new MaterialBuilder().setName("Duranium")
            .setDefaultLocalName("Duranium")
            .setMetaItemSubID(328)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightGray)
            .setToolSpeed(32.0f)
            .setDurability(40960)
            .setToolQuality(11)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .constructMaterial()
            .setTurbineMultipliers(16, 16, 1);
    }

    private static Materials loadEclogite() {
        return new MaterialBuilder().setName("Eclogite")
            .setDefaultLocalName("Eclogite")
            .setMetaItemSubID(860)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadElectrumFlux() {
        return new MaterialBuilder().setName("ElectrumFlux")
            .setDefaultLocalName("Fluxed Electrum")
            .setMetaItemSubID(320)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff78)
            .setToolSpeed(16.0f)
            .setDurability(512)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(9000)
            .setBlastFurnaceTemp(9000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .constructMaterial();
    }

    private static Materials loadEmery() {
        return new MaterialBuilder().setName("Emery")
            .setDefaultLocalName("Emery")
            .setMetaItemSubID(861)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadEnderiumBase() {
        return new MaterialBuilder().setName("EnderiumBase")
            .setDefaultLocalName("Enderium Base")
            .setMetaItemSubID(380)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x487799)
            .setToolSpeed(16.0f)
            .setDurability(768)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3600)
            .setBlastFurnaceTemp(3600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Tin, 2)
            .addMaterial(Materials.Silver, 2)
            .addMaterial(Materials.Platinum, 2)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ALIENIS, 1)
            .constructMaterial();
    }

    private static Materials loadEnergized() {
        return new MaterialBuilder().setName("Energized")
            .setDefaultLocalName("Energized")
            .constructMaterial();
    }

    private static Materials loadEpidote() {
        return new MaterialBuilder().setName("Epidote")
            .setDefaultLocalName("Epidote")
            .setMetaItemSubID(862)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadEximite() {
        return new MaterialBuilder().setName("Eximite")
            .setDefaultLocalName("Eximite")
            .setMetaItemSubID(959)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x7c5a96)
            .setToolSpeed(5.0f)
            .setDurability(2560)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadFierySteel() {
        return new MaterialBuilder().setName("FierySteel")
            .setDefaultLocalName("Fiery Steel")
            .setMetaItemSubID(346)
            .setIconSet(TextureSet.SET_FIERY)
            .setColor(Dyes.dyeRed)
            .setRGB(0x400000)
            .setToolSpeed(8.0f)
            .setDurability(256)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1811)
            .setBlastFurnaceTemp(1800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setFuelType(5)
            .setFuelPower(2048)
            .addAspect(TCAspects.PRAECANTATIO, 3)
            .addAspect(TCAspects.IGNIS, 3)
            .addAspect(TCAspects.CORPUS, 3)
            .constructMaterial();
    }

    private static Materials loadFirestone() {
        return new MaterialBuilder().setName("Firestone")
            .setDefaultLocalName("Firestone")
            .setMetaItemSubID(347)
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeRed)
            .setRGB(0xc81400)
            .setToolSpeed(6.0f)
            .setDurability(1280)
            .setToolQuality(3)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadFluorite() {
        return new MaterialBuilder().setName("Fluorite")
            .setDefaultLocalName("Fluorite")
            .setColor(Dyes.dyeGreen)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadFoolsRuby() {
        return new MaterialBuilder().setName("FoolsRuby")
            .setDefaultLocalName("Spinel")
            .setMetaItemSubID(512)
            .setIconSet(TextureSet.SET_RUBY)
            .setColor(Dyes.dyeRed)
            .setARGB(0x7fff6464)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Oxygen, 4)
            .addAspect(TCAspects.LUCRUM, 2)
            .addAspect(TCAspects.VITREUS, 2)
            .constructMaterial();
    }

    private static Materials loadForce() {
        return new MaterialBuilder().setName("Force")
            .setDefaultLocalName("Force")
            .setMetaItemSubID(521)
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff00)
            .setToolSpeed(10.0f)
            .setDurability(128)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.POTENTIA, 5)
            .constructMaterial();
    }

    private static Materials loadForcicium() {
        return new MaterialBuilder().setName("Forcicium")
            .setDefaultLocalName("Forcicium")
            .setMetaItemSubID(518)
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x323246)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addCell()
            .addAspect(TCAspects.POTENTIA, 2)
            .constructMaterial();
    }

    private static Materials loadForcillium() {
        return new MaterialBuilder().setName("Forcillium")
            .setDefaultLocalName("Forcillium")
            .setMetaItemSubID(519)
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x323246)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addCell()
            .addAspect(TCAspects.POTENTIA, 2)
            .constructMaterial();
    }

    private static Materials loadGabbro() {
        return new MaterialBuilder().setName("Gabbro")
            .setDefaultLocalName("Gabbro")
            .setMetaItemSubID(863)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadGlowstone() {
        return new MaterialBuilder().setName("Glowstone")
            .setDefaultLocalName("Glowstone")
            .setMetaItemSubID(811)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff00)
            .addDustItems()
            .addCell()
            .addAspect(TCAspects.LUX, 2)
            .addAspect(TCAspects.SENSUS, 1)
            .constructMaterial();
    }

    private static Materials loadGneiss() {
        return new MaterialBuilder().setName("Gneiss")
            .setDefaultLocalName("Gneiss")
            .setMetaItemSubID(864)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadGraphene() {
        return new MaterialBuilder().setName("Graphene")
            .setDefaultLocalName("Graphene")
            .setMetaItemSubID(819)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setRGB(0x808080)
            .setToolSpeed(6.0f)
            .setDurability(32)
            .setToolQuality(1)
            .addDustItems()
            .addToolHeadItems()
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.VITREUS, 2)
            .addAspect(TCAspects.ELECTRUM, 1)
            .constructMaterial();
    }

    private static Materials loadGraphite() {
        return new MaterialBuilder().setName("Graphite")
            .setDefaultLocalName("Graphite")
            .setMetaItemSubID(865)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setRGB(0x808080)
            .setToolSpeed(5.0f)
            .setDurability(32)
            .setToolQuality(2)
            .addDustItems()
            .addOreItems()
            .addCell()
            .addToolHeadItems()
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.VITREUS, 2)
            .addAspect(TCAspects.IGNIS, 1)
            .constructMaterial();
    }

    private static Materials loadGreenschist() {
        return new MaterialBuilder().setName("Greenschist")
            .setDefaultLocalName("Green Schist")
            .setMetaItemSubID(866)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGreen)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadGreenstone() {
        return new MaterialBuilder().setName("Greenstone")
            .setDefaultLocalName("Greenstone")
            .setMetaItemSubID(867)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGreen)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadGreywacke() {
        return new MaterialBuilder().setName("Greywacke")
            .setDefaultLocalName("Greywacke")
            .setMetaItemSubID(897)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadHaderoth() {
        return new MaterialBuilder().setName("Haderoth")
            .setDefaultLocalName("Haderoth")
            .setMetaItemSubID(963)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x77341e)
            .setToolSpeed(10.0f)
            .setDurability(3200)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadHematite() {
        return new MaterialBuilder().setName("Hematite")
            .setDefaultLocalName("Hematite")
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadHepatizon() {
        return new MaterialBuilder().setName("Hepatizon")
            .setDefaultLocalName("Hepatizon")
            .setMetaItemSubID(957)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x755e75)
            .setToolSpeed(12.0f)
            .setDurability(128)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadHSLA() {
        return new MaterialBuilder().setName("HSLA")
            .setDefaultLocalName("HSLA Steel")
            .setMetaItemSubID(322)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x808080)
            .setToolSpeed(6.0f)
            .setDurability(500)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1811)
            .setBlastFurnaceTemp(1000)
            .setBlastFurnaceRequired(true)
            .addAspect(TCAspects.METALLUM, 1)
            .addAspect(TCAspects.ORDO, 1)
            .constructMaterial();
    }

    private static Materials loadIgnatius() {
        return new MaterialBuilder().setName("Ignatius")
            .setDefaultLocalName("Ignatius")
            .setMetaItemSubID(950)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0xffa953)
            .setToolSpeed(12.0f)
            .setDurability(512)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .constructMaterial();
    }

    private static Materials loadInfernal() {
        return new MaterialBuilder().setName("Infernal")
            .setDefaultLocalName("Infernal")
            .constructMaterial();
    }

    private static Materials loadInfuscolium() {
        return new MaterialBuilder().setName("Infuscolium")
            .setDefaultLocalName("Infuscolium")
            .setMetaItemSubID(490)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x922156)
            .setToolSpeed(6.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadInfusedAir() {
        return new MaterialBuilder().setName("InfusedAir")
            .setDefaultLocalName("Aer")
            .setMetaItemSubID(540)
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x00ffff00)
            .setToolSpeed(8.0f)
            .setDurability(64)
            .setToolQuality(3)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(-1)
            .setFuelType(5)
            .setFuelPower(160)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.AER, 2)
            .constructMaterial();
    }

    private static Materials loadInfusedDull() {
        return new MaterialBuilder().setName("InfusedDull")
            .setDefaultLocalName("Vacuus")
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyeLightGray)
            .setARGB(0x00646464)
            .setToolSpeed(32.0f)
            .setDurability(64)
            .setToolQuality(3)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setFuelType(5)
            .setFuelPower(160)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.VACUOS, 2)
            .constructMaterial();
    }

    private static Materials loadInfusedEarth() {
        return new MaterialBuilder().setName("InfusedEarth")
            .setDefaultLocalName("Terra")
            .setMetaItemSubID(542)
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x0000ff00)
            .setToolSpeed(8.0f)
            .setDurability(256)
            .setToolQuality(3)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setFuelType(5)
            .setFuelPower(160)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.TERRA, 2)
            .constructMaterial();
    }

    private static Materials loadInfusedEntropy() {
        return new MaterialBuilder().setName("InfusedEntropy")
            .setDefaultLocalName("Perditio")
            .setMetaItemSubID(544)
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x003e3e3e)
            .setToolSpeed(32.0f)
            .setDurability(64)
            .setToolQuality(4)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setFuelType(5)
            .setFuelPower(320)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.PERDITIO, 2)
            .constructMaterial();
    }

    private static Materials loadInfusedFire() {
        return new MaterialBuilder().setName("InfusedFire")
            .setDefaultLocalName("Ignis")
            .setMetaItemSubID(541)
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyeRed)
            .setARGB(0x00ff0000)
            .setToolSpeed(8.0f)
            .setDurability(64)
            .setToolQuality(3)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setFuelType(5)
            .setFuelPower(320)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.IGNIS, 2)
            .constructMaterial();
    }

    private static Materials loadInfusedGold() {
        return new MaterialBuilder().setName("InfusedGold")
            .setDefaultLocalName("Infused Gold")
            .setMetaItemSubID(323)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffc83c)
            .setToolSpeed(12.0f)
            .setDurability(64)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .constructMaterial();
    }

    private static Materials loadInfusedOrder() {
        return new MaterialBuilder().setName("InfusedOrder")
            .setDefaultLocalName("Ordo")
            .setMetaItemSubID(545)
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x00fcfcfc)
            .setToolSpeed(8.0f)
            .setDurability(64)
            .setToolQuality(3)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setFuelType(5)
            .setFuelPower(240)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.ORDO, 2)
            .constructMaterial();
    }

    private static Materials loadInfusedVis() {
        return new MaterialBuilder().setName("InfusedVis")
            .setDefaultLocalName("Auram")
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyePurple)
            .setARGB(0x00ff00ff)
            .setToolSpeed(8.0f)
            .setDurability(64)
            .setToolQuality(3)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setFuelType(5)
            .setFuelPower(240)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.AURAM, 2)
            .constructMaterial();
    }

    private static Materials loadInfusedWater() {
        return new MaterialBuilder().setName("InfusedWater")
            .setDefaultLocalName("Aqua")
            .setMetaItemSubID(543)
            .setIconSet(TextureSet.SET_SHARDS)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x000000ff)
            .setToolSpeed(8.0f)
            .setDurability(64)
            .setToolQuality(3)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setFuelType(5)
            .setFuelPower(160)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .addAspect(TCAspects.AQUA, 2)
            .constructMaterial();
    }

    private static Materials loadInolashite() {
        return new MaterialBuilder().setName("Inolashite")
            .setDefaultLocalName("Inolashite")
            .setMetaItemSubID(954)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x94d8bb)
            .setToolSpeed(8.0f)
            .setDurability(2304)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadInvisium() {
        return new MaterialBuilder().setName("Invisium")
            .setDefaultLocalName("Invisium")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadJade() {
        return new MaterialBuilder().setName("Jade")
            .setDefaultLocalName("Jade")
            .setMetaItemSubID(537)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x006400)
            .setToolSpeed(1.0f)
            .setDurability(16)
            .setToolQuality(2)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Aluminium, 1)
            .addMaterial(Materials.Silicon, 2)
            .addMaterial(Materials.Oxygen, 6)
            .addAspect(TCAspects.LUCRUM, 6)
            .addAspect(TCAspects.VITREUS, 3)
            .constructMaterial();
    }

    private static Materials loadKalendrite() {
        return new MaterialBuilder().setName("Kalendrite")
            .setDefaultLocalName("Kalendrite")
            .setMetaItemSubID(953)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0xaa5bbd)
            .setToolSpeed(5.0f)
            .setDurability(2560)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .constructMaterial();
    }

    private static Materials loadKomatiite() {
        return new MaterialBuilder().setName("Komatiite")
            .setDefaultLocalName("Komatiite")
            .setMetaItemSubID(869)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadLava() {
        return new MaterialBuilder().setName("Lava")
            .setDefaultLocalName("Lava")
            .setMetaItemSubID(700)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xff4000)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadLemurite() {
        return new MaterialBuilder().setName("Lemurite")
            .setDefaultLocalName("Lemurite")
            .setMetaItemSubID(486)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0xdbdbdb)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadLimestone() {
        return new MaterialBuilder().setName("Limestone")
            .setDefaultLocalName("Limestone")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadMagma() {
        return new MaterialBuilder().setName("Magma")
            .setDefaultLocalName("Magma")
            .setColor(Dyes.dyeOrange)
            .setRGB(0xff4000)
            .constructMaterial();
    }

    private static Materials loadMawsitsit() {
        return new MaterialBuilder().setName("Mawsitsit")
            .setDefaultLocalName("Mawsitsit")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadMercassium() {
        return new MaterialBuilder().setName("Mercassium")
            .setDefaultLocalName("Mercassium")
            .setToolSpeed(6.0f)
            .setDurability(64)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadMeteoricSteel() {
        return new MaterialBuilder().setName("MeteoricSteel")
            .setDefaultLocalName("Meteoric Steel")
            .setMetaItemSubID(341)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setRGB(0x321928)
            .setToolSpeed(6.0f)
            .setDurability(768)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .setMeltingPoint(1811)
            .setBlastFurnaceTemp(1000)
            .setBlastFurnaceRequired(true)
            .setDensityMultiplier(51)
            .setDensityDivider(50)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.MeteoricIron, 50)
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .addAspect(TCAspects.ORDO, 1)
            .constructMaterial();
    }

    private static Materials loadMeteorite() {
        return new MaterialBuilder().setName("Meteorite")
            .setDefaultLocalName("Meteorite")
            .setColor(Dyes.dyePurple)
            .setRGB(0x50233c)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadMeutoite() {
        return new MaterialBuilder().setName("Meutoite")
            .setDefaultLocalName("Meutoite")
            .setMetaItemSubID(487)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x5f5269)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadMigmatite() {
        return new MaterialBuilder().setName("Migmatite")
            .setDefaultLocalName("Migmatite")
            .setMetaItemSubID(872)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadMimichite() {
        return new MaterialBuilder().setName("Mimichite")
            .setDefaultLocalName("Mimichite")
            .setIconSet(TextureSet.SET_GEM_VERTICAL)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadMoonstone() {
        return new MaterialBuilder().setName("Moonstone")
            .setDefaultLocalName("Moonstone")
            .setColor(Dyes.dyeWhite)
            .addDustItems()
            .addOreItems()
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.ALIENIS, 1)
            .constructMaterial();
    }

    private static Materials loadNaquadahAlloy() {
        return new MaterialBuilder().setName("NaquadahAlloy")
            .setDefaultLocalName("Naquadah Alloy")
            .setMetaItemSubID(325)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x282828)
            .setToolSpeed(8.0f)
            .setDurability(5120)
            .setToolQuality(5)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(7200)
            .setBlastFurnaceTemp(7200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 4)
            .addAspect(TCAspects.NEBRISUM, 1)
            .constructMaterial();
    }

    private static Materials loadNaquadahEnriched() {
        return new MaterialBuilder().setName("NaquadahEnriched")
            .setDefaultLocalName("Enriched Naquadah")
            .setMetaItemSubID(326)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x323232)
            .setToolSpeed(6.0f)
            .setDurability(1280)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .setMeltingPoint(4500)
            .setBlastFurnaceTemp(4500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 3)
            .addAspect(TCAspects.RADIO, 2)
            .addAspect(TCAspects.NEBRISUM, 2)
            .constructMaterial();
    }

    private static Materials loadNaquadria() {
        return new MaterialBuilder().setName("Naquadria")
            .setDefaultLocalName("Naquadria")
            .setMetaItemSubID(327)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x1e1e1e)
            .setToolSpeed(1.0f)
            .setDurability(512)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .setMeltingPoint(9000)
            .setBlastFurnaceTemp(9000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 4)
            .addAspect(TCAspects.RADIO, 3)
            .addAspect(TCAspects.NEBRISUM, 3)
            .constructMaterial()
            .setHasCorrespondingPlasma(true);
    }

    private static Materials loadNether() {
        return new MaterialBuilder().setName("Nether")
            .setDefaultLocalName("Nether")
            .constructMaterial();
    }

    private static Materials loadNetherBrick() {
        return new MaterialBuilder().setName("NetherBrick")
            .setDefaultLocalName("Nether Brick")
            .setMetaItemSubID(814)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeRed)
            .setRGB(0x640000)
            .addDustItems()
            .addAspect(TCAspects.IGNIS, 1)
            .constructMaterial();
    }

    private static Materials loadNetherQuartz() {
        return new MaterialBuilder().setName("NetherQuartz")
            .setDefaultLocalName("Nether Quartz")
            .setMetaItemSubID(522)
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xe6d2d2)
            .setToolSpeed(1.0f)
            .setDurability(32)
            .setToolQuality(1)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addAspect(TCAspects.POTENTIA, 1)
            .addAspect(TCAspects.VITREUS, 1)
            .constructMaterial();
    }

    private static Materials loadNetherStar() {
        return new MaterialBuilder().setName("NetherStar")
            .setDefaultLocalName("Nether Star")
            .setMetaItemSubID(506)
            .setIconSet(TextureSet.SET_NETHERSTAR)
            .setColor(Dyes.dyeWhite)
            .setToolSpeed(6.0f)
            .setDurability(5120)
            .setToolQuality(4)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .setFuelType(5)
            .setFuelPower(50000)
            .constructMaterial();
    }

    private static Materials loadObsidianFlux() {
        return new MaterialBuilder().setName("ObsidianFlux")
            .setDefaultLocalName("Fluxed Obsidian")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePurple)
            .setRGB(0x503264)
            .addDustItems()
            .addMetalItems()
            .constructMaterial();
    }

    private static Materials loadOilsands() {
        return new MaterialBuilder().setName("Oilsands")
            .setDefaultLocalName("Oilsands")
            .setMetaItemSubID(878)
            .setRGB(0x0a0a0a)
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadOnyx() {
        return new MaterialBuilder().setName("Onyx")
            .setDefaultLocalName("Onyx")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadOrichalcum() {
        return new MaterialBuilder().setName("Orichalcum")
            .setDefaultLocalName("Orichalcum")
            .setMetaItemSubID(966)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x547a38)
            .setToolSpeed(32.0f)
            .setDurability(20480)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(6000)
            .setBlastFurnaceTemp(6000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .constructMaterial()
            .setTurbineMultipliers(1, 6, 1);
    }

    private static Materials loadOsmonium() {
        return new MaterialBuilder().setName("Osmonium")
            .setDefaultLocalName("Osmonium")
            .setColor(Dyes.dyeBlue)
            .setToolSpeed(6.0f)
            .setDurability(64)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadOureclase() {
        return new MaterialBuilder().setName("Oureclase")
            .setDefaultLocalName("Oureclase")
            .setMetaItemSubID(961)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0xb76215)
            .setToolSpeed(6.0f)
            .setDurability(1920)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadPainite() {
        return new MaterialBuilder().setName("Painite")
            .setDefaultLocalName("Painite")
            .constructMaterial();
    }

    private static Materials loadPeanutwood() {
        return new MaterialBuilder().setName("Peanutwood")
            .setDefaultLocalName("Peanut Wood")
            .constructMaterial();
    }

    private static Materials loadPetroleum() {
        return new MaterialBuilder().setName("Petroleum")
            .setDefaultLocalName("Petroleum")
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadPewter() {
        return new MaterialBuilder().setName("Pewter")
            .setDefaultLocalName("Pewter")
            .constructMaterial();
    }

    private static Materials loadPhoenixite() {
        return new MaterialBuilder().setName("Phoenixite")
            .setDefaultLocalName("Phoenixite")
            .setToolSpeed(6.0f)
            .setDurability(64)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadPrometheum() {
        return new MaterialBuilder().setName("Prometheum")
            .setDefaultLocalName("Prometheum")
            .setMetaItemSubID(960)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x5a8156)
            .setToolSpeed(8.0f)
            .setDurability(512)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadQuartzite() {
        return new MaterialBuilder().setName("Quartzite")
            .setDefaultLocalName("Quartzite")
            .setMetaItemSubID(523)
            .setIconSet(TextureSet.SET_QUARTZ)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xd2e6d2)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadRandomite() {
        return new MaterialBuilder().setName("Randomite")
            .setDefaultLocalName("Randomite")
            .addDustItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadRhyolite() {
        return new MaterialBuilder().setName("Rhyolite")
            .setDefaultLocalName("Rhyolite")
            .setMetaItemSubID(875)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadRubracium() {
        return new MaterialBuilder().setName("Rubracium")
            .setDefaultLocalName("Rubracium")
            .setMetaItemSubID(488)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setRGB(0x972d2d)
            .setToolSpeed(1.0f)
            .setDurability(128)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .constructMaterial();
    }

    private static Materials loadSand() {
        return new MaterialBuilder().setName("Sand")
            .setDefaultLocalName("Sand")
            .setColor(Dyes.dyeYellow)
            .constructMaterial();
    }

    private static Materials loadSanguinite() {
        return new MaterialBuilder().setName("Sanguinite")
            .setDefaultLocalName("Sanguinite")
            .setMetaItemSubID(955)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0xb90000)
            .setToolSpeed(3.0f)
            .setDurability(4480)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadSiltstone() {
        return new MaterialBuilder().setName("Siltstone")
            .setDefaultLocalName("Siltstone")
            .setMetaItemSubID(876)
            .setIconSet(TextureSet.SET_DULL)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadSunstone() {
        return new MaterialBuilder().setName("Sunstone")
            .setDefaultLocalName("Sunstone")
            .setColor(Dyes.dyeYellow)
            .addDustItems()
            .addOreItems()
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.ALIENIS, 1)
            .constructMaterial();
    }

    private static Materials loadTar() {
        return new MaterialBuilder().setName("Tar")
            .setDefaultLocalName("Tar")
            .setColor(Dyes.dyeBlack)
            .setRGB(0x0a0a0a)
            .constructMaterial();
    }

    private static Materials loadTartarite() {
        return new MaterialBuilder().setName("Tartarite")
            .setDefaultLocalName("Tartarite")
            .setMetaItemSubID(956)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0xff763c)
            .setToolSpeed(32.0f)
            .setDurability(20480)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .setMeltingPoint(10400)
            .setBlastFurnaceTemp(10400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .constructMaterial()
            .setTurbineMultipliers(1120, 1120, 1);
    }

    private static Materials loadUUAmplifier() {
        return new MaterialBuilder().setName("UUAmplifier")
            .setDefaultLocalName("UU-Amplifier")
            .setMetaItemSubID(721)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyePink)
            .setRGB(0x600080)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadUUMatter() {
        return new MaterialBuilder().setName("UUMatter")
            .setDefaultLocalName("UU-Matter")
            .setMetaItemSubID(703)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyePink)
            .setRGB(0x8000c4)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadVoid() {
        return new MaterialBuilder().setName("Void")
            .setDefaultLocalName("Void")
            .setMetaItemSubID(970)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setARGB(0x001c0639)
            .setToolSpeed(32.0f)
            .setDurability(512)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setFuelType(5)
            .setFuelPower(1500)
            .setDensityMultiplier(2)
            .addAspect(TCAspects.VACUOS, 1)
            .constructMaterial();
    }

    private static Materials loadVoidstone() {
        return new MaterialBuilder().setName("Voidstone")
            .setDefaultLocalName("Voidstone")
            .setARGB(0xc8ffffff)
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.VACUOS, 1)
            .constructMaterial();
    }

    private static Materials loadVulcanite() {
        return new MaterialBuilder().setName("Vulcanite")
            .setDefaultLocalName("Vulcanite")
            .setMetaItemSubID(489)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0xff8448)
            .setToolSpeed(32.0f)
            .setDurability(20480)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(8400)
            .setBlastFurnaceTemp(8400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .constructMaterial()
            .setTurbineMultipliers(40, 1, 1);
    }

    private static Materials loadVyroxeres() {
        return new MaterialBuilder().setName("Vyroxeres")
            .setDefaultLocalName("Vyroxeres")
            .setMetaItemSubID(951)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x55e001)
            .setToolSpeed(32.0f)
            .setDurability(7680)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .setMeltingPoint(5400)
            .setBlastFurnaceTemp(5400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .constructMaterial()
            .setTurbineMultipliers(1, 3, 1);
    }

    private static Materials loadYellorium() {
        return new MaterialBuilder().setName("Yellorium")
            .setDefaultLocalName("Yellorium")
            .setColor(Dyes.dyeYellow)
            .addDustItems()
            .addMetalItems()
            .constructMaterial();
    }

    private static Materials loadZectium() {
        return new MaterialBuilder().setName("Zectium")
            .setDefaultLocalName("Zectium")
            .setColor(Dyes.dyeBlack)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .constructMaterial();
    }

    private static void loadTiers() {
        Materials.ULV = Materials.Primitive = loadULV();
        Materials.LV = Materials.Basic = loadLV();
        Materials.MV = Materials.Good = loadMV();
        Materials.HV = Materials.Advanced = loadHV();
        Materials.EV = Materials.Data = loadEV();
        Materials.IV = Materials.Elite = loadIV();
        Materials.LuV = Materials.Master = loadLuV();
        Materials.ZPM = Materials.Ultimate = loadZPM();
        Materials.UV = /* Materials.Superconductor = */ loadUV();
        Materials.UHV = Materials.Infinite = loadUHV();
        Materials.UEV = Materials.Bio = loadUEV();
        Materials.UIV = Materials.Optical = loadUIV();
        Materials.UMV = Materials.Exotic = loadUMV();
        Materials.UXV = Materials.Cosmic = loadUXV();
        Materials.MAX = Materials.Transcendent = loadMAX();
    }

    private static Materials loadULV() {
        return new MaterialBuilder().setName("Primitive")
            .setDefaultLocalName("Primitive")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 1)
            .constructMaterial();
    }

    private static Materials loadLV() {
        return new MaterialBuilder().setName("Basic")
            .setDefaultLocalName("Basic")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 2)
            .constructMaterial();
    }

    private static Materials loadMV() {
        return new MaterialBuilder().setName("Good")
            .setDefaultLocalName("Good")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 3)
            .constructMaterial();
    }

    private static Materials loadHV() {
        return new MaterialBuilder().setName("Advanced")
            .setDefaultLocalName("Advanced")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 4)
            .constructMaterial();
    }

    private static Materials loadEV() {
        return new MaterialBuilder().setName("Data")
            .setDefaultLocalName("Data")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 5)
            .constructMaterial();
    }

    private static Materials loadIV() {
        return new MaterialBuilder().setName("Elite")
            .setDefaultLocalName("Elite")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 6)
            .constructMaterial();
    }

    private static Materials loadLuV() {
        return new MaterialBuilder().setName("Master")
            .setDefaultLocalName("Master")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 7)
            .constructMaterial();
    }

    private static Materials loadZPM() {
        return new MaterialBuilder().setName("Ultimate")
            .setDefaultLocalName("Ultimate")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 8)
            .constructMaterial();
    }

    private static Materials loadUV() {
        return new MaterialBuilder().setName("Superconductor")
            .setDefaultLocalName("Superconductor")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.MACHINA, 9)
            .constructMaterial();
    }

    private static Materials loadUHV() {
        return new MaterialBuilder().setName("Infinite")
            .setDefaultLocalName("Infinite")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 10)
            .constructMaterial();
    }

    private static Materials loadUEV() {
        return new MaterialBuilder().setName("Bio")
            .setDefaultLocalName("Bio")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 11)
            .constructMaterial();
    }

    private static Materials loadUIV() {
        return new MaterialBuilder().setName("Optical")
            .setDefaultLocalName("Optical")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 12)
            .constructMaterial();
    }

    private static Materials loadUMV() {
        return new MaterialBuilder().setName("Exotic")
            .setDefaultLocalName("Exotic")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 13)
            .constructMaterial();
    }

    private static Materials loadUXV() {
        return new MaterialBuilder().setName("Cosmic")
            .setDefaultLocalName("Cosmic")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 14)
            .constructMaterial();
    }

    private static Materials loadMAX() {
        return new MaterialBuilder().setName("Transcendent")
            .setDefaultLocalName("Transcendent")
            .setColor(Dyes.dyeLightGray)
            .addAspect(TCAspects.ELECTRUM, 15)
            .constructMaterial();
    }

    private static void loadNotExact() {
        Materials.AdvancedGlue = loadAdvancedGlue();
        Materials.Antimatter = loadAntimatter();
        Materials.Biomass = loadBiomass();
        Materials.CharcoalByproducts = loadCharcoalByproducts();
        Materials.Cheese = loadCheese();
        Materials.Chili = loadChili();
        Materials.Chocolate = loadChocolate();
        Materials.Cluster = loadCluster();
        Materials.CoalFuel = loadCoalFuel();
        Materials.Cocoa = loadCocoa();
        Materials.Coffee = loadCoffee();
        Materials.Creosote = loadCreosote();
        Materials.Ethanol = loadEthanol();
        Materials.FermentedBiomass = loadFermentedBiomass();
        Materials.FishOil = loadFishOil();
        Materials.FryingOilHot = loadFryingOilHot();
        Materials.Fuel = loadFuel();
        Materials.Glue = loadGlue();
        Materials.Gunpowder = loadGunpowder();
        Materials.Honey = loadHoney();
        Materials.Leather = loadLeather();
        Materials.Lubricant = loadLubricant();
        Materials.McGuffium239 = loadMcGuffium239();
        Materials.MeatCooked = loadMeatCooked();
        Materials.MeatRaw = loadMeatRaw();
        Materials.Milk = loadMilk();
        Materials.Mud = loadMud();
        Materials.Oil = loadOil();
        Materials.Paper = loadPaper();
        Materials.Peat = loadPeat();
        // Materials.Protomatter = loadProtomatter();
        Materials.RareEarth = loadRareEarth();
        Materials.Red = loadRed();
        Materials.Reinforced = loadReinforced();
        Materials.SeedOil = loadSeedOil();
        Materials.SeedOilHemp = loadSeedOilHemp();
        Materials.SeedOilLin = loadSeedOilLin();
        Materials.Stone = loadStone();
        Materials.TNT = loadTNT();
        Materials.Unstable = loadUnstable();
        Materials.Unstableingot = loadUnstableingot();
        Materials.Vinegar = loadVinegar();
        Materials.WeedEX9000 = loadWeedEX9000();
        Materials.Wheat = loadWheat();
        Materials.WoodGas = loadWoodGas();
        Materials.WoodTar = loadWoodTar();
        Materials.WoodVinegar = loadWoodVinegar();
    }

    private static Materials loadAdvancedGlue() {
        return new MaterialBuilder(567, TextureSet.SET_FLUID, "Advanced Glue").setName("AdvancedGlue")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 185)
            .setColor(Dyes.dyeYellow)
            .setAspects(Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.LIMUS, 5)))
            .constructMaterial();
    }

    private static Materials loadAntimatter() {
        return new MaterialBuilder().setName("Antimatter")
            .setDefaultLocalName("Antimatter")
            .setColor(Dyes.dyePink)
            .addAspect(TCAspects.POTENTIA, 9)
            .addAspect(TCAspects.PERFODIO, 8)
            .constructMaterial();
    }

    private static Materials loadBiomass() {
        return new MaterialBuilder().setName("Biomass")
            .setDefaultLocalName("Forestry Biomass")
            .setMetaItemSubID(704)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x00ff00)
            .addCell()
            .setFuelType(3)
            .setFuelPower(8)
            .constructMaterial();
    }

    private static Materials loadCharcoalByproducts() {
        return new MaterialBuilder(675, TextureSet.SET_FLUID, "Charcoal Byproducts").addCell()
            .setRGB(120, 68, 33)
            .setColor(Dyes.dyeBrown)
            .constructMaterial();
    }

    private static Materials loadCheese() {
        return new MaterialBuilder().setName("Cheese")
            .setDefaultLocalName("Cheese")
            .setMetaItemSubID(894)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff00)
            .addDustItems()
            .addOreItems()
            .setMeltingPoint(320)
            .constructMaterial();
    }

    private static Materials loadChili() {
        return new MaterialBuilder().setName("Chili")
            .setDefaultLocalName("Chili")
            .setMetaItemSubID(895)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeRed)
            .setRGB(0xc80000)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadChocolate() {
        return new MaterialBuilder().setName("Chocolate")
            .setDefaultLocalName("Chocolate")
            .setMetaItemSubID(886)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeBrown)
            .setRGB(0xbe5f00)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadCluster() {
        return new MaterialBuilder().setName("Cluster")
            .setDefaultLocalName("Cluster")
            .setColor(Dyes.dyeWhite)
            .setARGB(0x7fffffff)
            .constructMaterial();
    }

    private static Materials loadCoalFuel() {
        return new MaterialBuilder().setName("CoalFuel")
            .setDefaultLocalName("Coalfuel")
            .setMetaItemSubID(710)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x323246)
            .addCell()
            .setFuelPower(16)
            .constructMaterial();
    }

    private static Materials loadCocoa() {
        return new MaterialBuilder().setName("Cocoa")
            .setDefaultLocalName("Cocoa")
            .setMetaItemSubID(887)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeBrown)
            .setRGB(0xbe5f00)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadCoffee() {
        return new MaterialBuilder().setName("Coffee")
            .setDefaultLocalName("Coffee")
            .setMetaItemSubID(888)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeBrown)
            .setRGB(0x964b00)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadCreosote() {
        return new MaterialBuilder().setName("Creosote")
            .setDefaultLocalName("Creosote")
            .setMetaItemSubID(712)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBrown)
            .setRGB(0x804000)
            .addCell()
            .setFuelType(3)
            .setFuelPower(8)
            .constructMaterial();
    }

    private static Materials loadEthanol() {
        return new MaterialBuilder().setName("Ethanol")
            .setDefaultLocalName("Ethanol")
            .setMetaItemSubID(706)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xff8000)
            .addCell()
            .setFuelPower(192)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.VENENUM, 1)
            .addAspect(TCAspects.AQUA, 1)
            .constructMaterial();
    }

    private static Materials loadFermentedBiomass() {
        return new MaterialBuilder(691, TextureSet.SET_FLUID, "Fermented Biomass").addCell()
            .addFluid()
            .setRGB(68, 85, 0)
            .setColor(Dyes.dyeBrown)
            .constructMaterial();
    }

    private static Materials loadFishOil() {
        return new MaterialBuilder().setName("FishOil")
            .setDefaultLocalName("Fish Oil")
            .setMetaItemSubID(711)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffc400)
            .addCell()
            .setFuelType(3)
            .setFuelPower(2)
            .addAspect(TCAspects.CORPUS, 2)
            .constructMaterial();
    }

    private static Materials loadFryingOilHot() {
        return new MaterialBuilder().setName("FryingOilHot")
            .setDefaultLocalName("Hot Frying Oil")
            .setMetaItemSubID(727)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xc8c400)
            .addCell()
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.IGNIS, 1)
            .constructMaterial();
    }

    private static Materials loadFuel() {
        return new MaterialBuilder().setName("Fuel")
            .setDefaultLocalName("Diesel")
            .setMetaItemSubID(708)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff00)
            .addCell()
            .setFuelPower(480)
            .constructMaterial();
    }

    private static Materials loadGlue() {
        return new MaterialBuilder().setName("Glue")
            .setDefaultLocalName("Refined Glue")
            .setMetaItemSubID(726)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xc8c400)
            .addCell()
            .addAspect(TCAspects.LIMUS, 2)
            .constructMaterial();
    }

    private static Materials loadGunpowder() {
        return new MaterialBuilder().setName("Gunpowder")
            .setDefaultLocalName("Gunpowder")
            .setMetaItemSubID(800)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setRGB(0x808080)
            .addDustItems()
            .addAspect(TCAspects.PERDITIO, 3)
            .addAspect(TCAspects.IGNIS, 4)
            .constructMaterial();
    }

    private static Materials loadHoney() {
        return new MaterialBuilder().setName("Honey")
            .setDefaultLocalName("Honey")
            .setMetaItemSubID(725)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xd2c800)
            .addCell()
            .constructMaterial();
    }

    private static Materials loadLeather() {
        return new MaterialBuilder().setName("Leather")
            .setDefaultLocalName("Leather")
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x7f969650)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadLubricant() {
        return new MaterialBuilder().setName("Lubricant")
            .setDefaultLocalName("Lubricant")
            .setMetaItemSubID(724)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xffc400)
            .addCell()
            .addAspect(TCAspects.AQUA, 2)
            .addAspect(TCAspects.MACHINA, 1)
            .constructMaterial();
    }

    private static Materials loadMcGuffium239() {
        return new MaterialBuilder().setName("McGuffium239")
            .setDefaultLocalName("Mc Guffium 239")
            .setMetaItemSubID(999)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyePink)
            .setRGB(0xc83296)
            .addCell()
            .addAspect(TCAspects.ALIENIS, 8)
            .addAspect(TCAspects.PERMUTATIO, 8)
            .addAspect(TCAspects.SPIRITUS, 8)
            .addAspect(TCAspects.AURAM, 8)
            .addAspect(TCAspects.VITIUM, 8)
            .addAspect(TCAspects.RADIO, 8)
            .addAspect(TCAspects.MAGNETO, 8)
            .addAspect(TCAspects.ELECTRUM, 8)
            .addAspect(TCAspects.NEBRISUM, 8)
            .addAspect(TCAspects.STRONTIO, 8)
            .constructMaterial();
    }

    private static Materials loadMeatCooked() {
        return new MaterialBuilder().setName("MeatCooked")
            .setDefaultLocalName("Cooked Meat")
            .setMetaItemSubID(893)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyePink)
            .setRGB(0x963c14)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadMeatRaw() {
        return new MaterialBuilder().setName("MeatRaw")
            .setDefaultLocalName("Raw Meat")
            .setMetaItemSubID(892)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyePink)
            .setRGB(0xff6464)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadMilk() {
        return new MaterialBuilder().setName("Milk")
            .setDefaultLocalName("Milk")
            .setMetaItemSubID(885)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xfefefe)
            .addDustItems()
            .addCell()
            .addAspect(TCAspects.SANO, 2)
            .constructMaterial();
    }

    private static Materials loadMud() {
        return new MaterialBuilder().setName("Mud")
            .setDefaultLocalName("Mud")
            .setColor(Dyes.dyeBrown)
            .constructMaterial();
    }

    private static Materials loadOil() {
        return new MaterialBuilder().setName("Oil")
            .setDefaultLocalName("Oil")
            .setMetaItemSubID(707)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x0a0a0a)
            .addCell()
            .setFuelType(3)
            .setFuelPower(20)
            .constructMaterial();
    }

    private static Materials loadPaper() {
        return new MaterialBuilder().setName("Paper")
            .setDefaultLocalName("Paper")
            .setMetaItemSubID(879)
            .setIconSet(TextureSet.SET_PAPER)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xfafafa)
            .addDustItems()
            .addAspect(TCAspects.COGNITIO, 1)
            .constructMaterial();
    }

    private static Materials loadPeat() {
        return new MaterialBuilder().setName("Peat")
            .setDefaultLocalName("Peat")
            .setColor(Dyes.dyeBrown)
            .addAspect(TCAspects.POTENTIA, 2)
            .addAspect(TCAspects.IGNIS, 2)
            .constructMaterial();
    }

    private static Materials loadRareEarth() {
        return new MaterialBuilder().setName("RareEarth")
            .setDefaultLocalName("Rare Earth")
            .setMetaItemSubID(891)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeGray)
            .setRGB(0x808064)
            .addDustItems()
            .addAspect(TCAspects.VITREUS, 1)
            .addAspect(TCAspects.LUCRUM, 1)
            .constructMaterial();
    }

    private static Materials loadRed() {
        return new MaterialBuilder().setName("Red")
            .setDefaultLocalName("Red")
            .setColor(Dyes.dyeRed)
            .setRGB(0xff0000)
            .constructMaterial();
    }

    private static Materials loadReinforced() {
        return new MaterialBuilder().setName("Reinforced")
            .setDefaultLocalName("Reinforced")
            .setMetaItemSubID(383)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x698da5)
            .setToolSpeed(7.0f)
            .setDurability(480)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1700)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .constructMaterial();
    }

    private static Materials loadSeedOil() {
        return new MaterialBuilder().setName("SeedOil")
            .setDefaultLocalName("Seed Oil")
            .setMetaItemSubID(713)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setRGB(0xc4ff00)
            .addCell()
            .setFuelType(3)
            .setFuelPower(2)
            .addAspect(TCAspects.GRANUM, 2)
            .constructMaterial();
    }

    private static Materials loadSeedOilHemp() {
        return new MaterialBuilder().setName("SeedOilHemp")
            .setDefaultLocalName("Hemp Seed Oil")
            .setMetaItemSubID(722)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setRGB(0xc4ff00)
            .addCell()
            .setFuelType(3)
            .setFuelPower(2)
            .addAspect(TCAspects.GRANUM, 2)
            .constructMaterial();
    }

    private static Materials loadSeedOilLin() {
        return new MaterialBuilder().setName("SeedOilLin")
            .setDefaultLocalName("Lin Seed Oil")
            .setMetaItemSubID(723)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setRGB(0xc4ff00)
            .addCell()
            .setFuelType(3)
            .setFuelPower(2)
            .addAspect(TCAspects.GRANUM, 2)
            .constructMaterial();
    }

    private static Materials loadStone() {
        return new MaterialBuilder().setName("Stone")
            .setDefaultLocalName("Stone")
            .setMetaItemSubID(299)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeLightGray)
            .setRGB(0xcdcdcd)
            .setToolSpeed(4.0f)
            .setDurability(32)
            .setToolQuality(1)
            .addDustItems()
            .addToolHeadItems()
            .addGearItems()
            .addAspect(TCAspects.TERRA, 1)
            .constructMaterial();
    }

    private static Materials loadTNT() {
        return new MaterialBuilder().setName("TNT")
            .setDefaultLocalName("TNT")
            .setColor(Dyes.dyeRed)
            .addAspect(TCAspects.PERDITIO, 7)
            .addAspect(TCAspects.IGNIS, 4)
            .constructMaterial();
    }

    private static Materials loadUnstable() {
        return new MaterialBuilder().setName("Unstable")
            .setDefaultLocalName("Unstable")
            .setMetaItemSubID(396)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x7fdcdcdc)
            .addDustItems()
            .addAspect(TCAspects.PERDITIO, 4)
            .constructMaterial();
    }

    private static Materials loadUnstableingot() {
        return new MaterialBuilder().setName("Unstableingot")
            .setDefaultLocalName("Unstable")
            .setColor(Dyes.dyeWhite)
            .setARGB(0x7fffffff)
            .addAspect(TCAspects.PERDITIO, 4)
            .constructMaterial();
    }

    private static Materials loadVinegar() {
        return new MaterialBuilder(690, TextureSet.SET_FLUID, "Vinegar").setColor(Dyes.dyeBrown)
            .constructMaterial();
    }

    private static Materials loadWeedEX9000() {
        return new MaterialBuilder(242, TextureSet.SET_FLUID, "Weed-EX 9000").addFluid()
            .setRGB(64, 224, 86)
            .setColor(Dyes.dyeGreen)
            .constructMaterial();
    }

    private static Materials loadWheat() {
        return new MaterialBuilder().setName("Wheat")
            .setDefaultLocalName("Wheat")
            .setMetaItemSubID(881)
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffffc4)
            .addDustItems()
            .addAspect(TCAspects.MESSIS, 2)
            .constructMaterial();
    }

    private static Materials loadWoodGas() {
        return new MaterialBuilder(660, TextureSet.SET_FLUID, "Wood Gas").addCell()
            .addGas()
            .setRGB(222, 205, 135)
            .setColor(Dyes.dyeBrown)
            .setFuelType(MaterialBuilder.GAS)
            .setFuelPower(24)
            .constructMaterial();
    }

    private static Materials loadWoodTar() {
        return new MaterialBuilder(662, TextureSet.SET_FLUID, "Wood Tar").addCell()
            .addFluid()
            .setRGB(40, 23, 11)
            .setColor(Dyes.dyeBrown)
            .constructMaterial();
    }

    private static Materials loadWoodVinegar() {
        return new MaterialBuilder(661, TextureSet.SET_FLUID, "Wood Vinegar").addCell()
            .addFluid()
            .setRGB(212, 85, 0)
            .setColor(Dyes.dyeBrown)
            .constructMaterial();
    }

    private static void loadTODOThis() {
        Materials.AluminiumBrass = loadAluminiumBrass();
        Materials.Endstone = loadEndstone();
        Materials.Netherrack = loadNetherrack();
        Materials.Osmiridium = loadOsmiridium();
        Materials.SoulSand = loadSoulSand();
        Materials.Sunnarium = loadSunnarium();
    }

    private static Materials loadAluminiumBrass() {
        return new MaterialBuilder().setName("AluminiumBrass")
            .setDefaultLocalName("Aluminium Brass")
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setToolSpeed(6.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .constructMaterial();
    }

    private static Materials loadEndstone() {
        return new MaterialBuilder().setName("Endstone")
            .setDefaultLocalName("Endstone")
            .setMetaItemSubID(808)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadNetherrack() {
        return new MaterialBuilder().setName("Netherrack")
            .setDefaultLocalName("Netherrack")
            .setMetaItemSubID(807)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeRed)
            .setRGB(0xc80000)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadOsmiridium() {
        return new MaterialBuilder().setName("Osmiridium")
            .setDefaultLocalName("Osmiridium")
            .setMetaItemSubID(317)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightBlue)
            .setRGB(0x6464ff)
            .setToolSpeed(7.0f)
            .setDurability(1600)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3500)
            .setBlastFurnaceTemp(4500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iridium, 3)
            .addMaterial(Materials.Osmium, 1)
            .constructMaterial();
    }

    private static Materials loadSoulSand() {
        return new MaterialBuilder().setName("SoulSand")
            .setDefaultLocalName("Soulsand")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBrown)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadSunnarium() {
        return new MaterialBuilder().setName("Sunnarium")
            .setDefaultLocalName("Sunnarium")
            .setMetaItemSubID(318)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff00)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(4200)
            .setBlastFurnaceTemp(4200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .constructMaterial();
    }

    private static void loadDegree1Compounds() {
        Materials.AceticAcid = loadAceticAcid();
        Materials.Acetone = loadAcetone();
        Materials.Air = loadAir();
        Materials.AllylChloride = loadAllylChloride();
        Materials.Almandine = loadAlmandine();
        Materials.Ammonia = loadAmmonia();
        Materials.Andradite = loadAndradite();
        Materials.AnnealedCopper = loadAnnealedCopper();
        Materials.AntimonyTrioxide = loadAntimonyTrioxide();
        Materials.ArsenicTrioxide = loadArsenicTrioxide();
        Materials.Asbestos = loadAsbestos();
        Materials.Ash = loadAsh();
        Materials.BandedIron = loadBandedIron();
        Materials.BatteryAlloy = loadBatteryAlloy();
        Materials.Benzene = loadBenzene();
        Materials.BlueTopaz = loadBlueTopaz();
        Materials.Bone = loadBone();
        Materials.Brass = loadBrass();
        Materials.Brick = loadBrick();
        Materials.Bronze = loadBronze();
        Materials.BrownLimonite = loadBrownLimonite();
        Materials.Calcite = loadCalcite();
        Materials.CarbonDioxide = loadCarbonDioxide();
        Materials.Cassiterite = loadCassiterite();
        Materials.CassiteriteSand = loadCassiteriteSand();
        Materials.Chalcopyrite = loadChalcopyrite();
        Materials.Charcoal = loadCharcoal();
        Materials.Chlorobenzene = loadChlorobenzene();
        Materials.Chromite = loadChromite();
        Materials.ChromiumDioxide = loadChromiumDioxide();
        Materials.Cinnabar = loadCinnabar();
        Materials.Coal = loadCoal();
        Materials.CobaltOxide = loadCobaltOxide();
        Materials.Cobaltite = loadCobaltite();
        Materials.Cooperite = loadCooperite();
        Materials.CupricOxide = loadCupricOxide();
        Materials.Cupronickel = loadCupronickel();
        Materials.DarkAsh = loadDarkAsh();
        Materials.DeepIron = loadDeepIron();
        Materials.Diamond = loadDiamond();
        Materials.DilutedHydrochloricAcid = loadDilutedHydrochloricAcid();
        Materials.Electrum = loadElectrum();
        Materials.Emerald = loadEmerald();
        Materials.Epoxid = loadEpoxid();
        Materials.FerriteMixture = loadFerriteMixture();
        Materials.Ferrosilite = loadFerrosilite();
        Materials.FreshWater = loadFreshWater();
        Materials.Galena = loadGalena();
        Materials.Garnierite = loadGarnierite();
        Materials.Glyceryl = loadGlyceryl();
        Materials.GreenSapphire = loadGreenSapphire();
        Materials.Grossular = loadGrossular();
        Materials.HolyWater = loadHolyWater();
        Materials.HydricSulfide = loadHydricSulfide();
        Materials.Ice = loadIce();
        Materials.Ilmenite = loadIlmenite();
        Materials.Invar = loadInvar();
        Materials.Kanthal = loadKanthal();
        Materials.Lazurite = loadLazurite();
        Materials.LiquidAir = loadLiquidAir();
        Materials.LiquidNitrogen = loadLiquidNitrogen();
        Materials.LiquidOxygen = loadLiquidOxygen();
        Materials.Magnalium = loadMagnalium();
        Materials.Magnesia = loadMagnesia();
        Materials.Magnesite = loadMagnesite();
        Materials.Magnesiumchloride = loadMagnesiumchloride();
        Materials.Magnetite = loadMagnetite();
        Materials.Massicot = loadMassicot();
        Materials.Methane = loadMethane();
        Materials.Molybdenite = loadMolybdenite();
        Materials.Nichrome = loadNichrome();
        Materials.NickelZincFerrite = loadNickelZincFerrite();
        Materials.NiobiumNitride = loadNiobiumNitride();
        Materials.NiobiumTitanium = loadNiobiumTitanium();
        Materials.NitroCarbon = loadNitroCarbon();
        Materials.NitrogenDioxide = loadNitrogenDioxide();
        Materials.NobleGases = loadNobleGases();
        Materials.Obsidian = loadObsidian();
        Materials.Phosphate = loadPhosphate();
        Materials.PigIron = loadPigIron();
        Materials.Plastic = loadPlastic();
        Materials.Polycaprolactam = loadPolycaprolactam();
        Materials.Polydimethylsiloxane = loadPolydimethylsiloxane();
        Materials.Polytetrafluoroethylene = loadPolytetrafluoroethylene();
        Materials.Potash = loadPotash();
        Materials.Powellite = loadPowellite();
        Materials.Pumice = loadPumice();
        Materials.Pyrite = loadPyrite();
        Materials.Pyrochlore = loadPyrochlore();
        Materials.Pyrolusite = loadPyrolusite();
        Materials.Pyrope = loadPyrope();
        Materials.Quicklime = loadQuicklime();
        Materials.RawRubber = loadRawRubber();
        Materials.RockSalt = loadRockSalt();
        Materials.Rubber = loadRubber();
        Materials.Ruby = loadRuby();
        Materials.Rutile = loadRutile();
        Materials.Salt = loadSalt();
        Materials.Saltpeter = loadSaltpeter();
        Materials.Sapphire = loadSapphire();
        Materials.Scheelite = loadScheelite();
        Materials.SiliconDioxide = loadSiliconDioxide();
        Materials.Silicone = loadSilicone();
        Materials.Snow = loadSnow();
        Materials.SodaAsh = loadSodaAsh();
        Materials.Sodalite = loadSodalite();
        Materials.SodiumPersulfate = loadSodiumPersulfate();
        Materials.SodiumSulfide = loadSodiumSulfide();
        Materials.Titaniumtetrachloride = loadTitaniumtetrachloride();
        Materials.Water = Materials.Steam = loadWater();
        Materials.Zincite = loadZincite();
    }

    private static Materials loadAceticAcid() {
        return new MaterialBuilder(670, TextureSet.SET_FLUID, "Acetic Acid").addCell()
            .addFluid()
            .setRGB(200, 180, 160)
            .setColor(Dyes.dyeWhite)
            .setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 4), new MaterialStack(Oxygen, 2))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadAcetone() {
        return new MaterialBuilder(672, TextureSet.SET_FLUID, "Acetone").addCell()
            .addFluid()
            .setRGB(175, 175, 175)
            .setColor(Dyes.dyeWhite)
            .setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadAir() {
        return new MaterialBuilder().setName("Air")
            .setDefaultLocalName("Air")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0xf0a9d0f5)
            .addCell()
            .addPlasma()
            .addMaterial(Materials.Nitrogen, 40)
            .addMaterial(Materials.Oxygen, 11)
            .addMaterial(Materials.Argon, 1)
            .addMaterial(Materials.NobleGases, 1)
            .constructMaterial();
    }

    private static Materials loadAllylChloride() {
        return new MaterialBuilder(682, TextureSet.SET_FLUID, "Allyl Chloride").addCell()
            .addFluid()
            .setRGB(135, 222, 170)
            .setColor(Dyes.dyeCyan)
            .setMaterialList(
                new MaterialStack(Carbon, 3),
                new MaterialStack(Hydrogen, 5),
                new MaterialStack(Chlorine, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadAlmandine() {
        return new MaterialBuilder().setName("Almandine")
            .setDefaultLocalName("Almandine")
            .setMetaItemSubID(820)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeRed)
            .setRGB(0xff0000)
            .addDustItems()
            .addOreItems()
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Iron, 3)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 12)
            .constructMaterial();
    }

    private static Materials loadAmmonia() {
        return new MaterialBuilder(659, TextureSet.SET_FLUID, "Ammonia").addCell()
            .addGas()
            .setRGB(63, 52, 128)
            .setColor(Dyes.dyeBlue)
            .setMaterialList(new MaterialStack(Nitrogen, 1), new MaterialStack(Hydrogen, 3))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadAndradite() {
        return new MaterialBuilder().setName("Andradite")
            .setDefaultLocalName("Andradite")
            .setMetaItemSubID(821)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeYellow)
            .setRGB(0x967800)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Calcium, 3)
            .addMaterial(Materials.Iron, 2)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 12)
            .constructMaterial();
    }

    private static Materials loadAnnealedCopper() {
        return new MaterialBuilder().setName("AnnealedCopper")
            .setDefaultLocalName("Annealed Copper")
            .setMetaItemSubID(345)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xff7814)
            .addDustItems()
            .addMetalItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Copper, 1)
            .constructMaterial();
    }

    private static Materials loadAntimonyTrioxide() {
        return new MaterialBuilder(618, TextureSet.SET_DULL, "Antimony Trioxide").addDustItems()
            .setRGB(230, 230, 240)
            .setColor(Dyes.dyeWhite)
            .setMaterialList(new MaterialStack(Antimony, 2), new MaterialStack(Oxygen, 3))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadArsenicTrioxide() {
        return new MaterialBuilder(615, TextureSet.SET_SHINY, "Arsenic Trioxide").addDustItems()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeGreen)
            .setMaterialList(new MaterialStack(Arsenic, 2), new MaterialStack(Oxygen, 3))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadAsbestos() {
        return new MaterialBuilder().setName("Asbestos")
            .setDefaultLocalName("Asbestos")
            .setMetaItemSubID(946)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xe6e6e6)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // Mg3Si2O5(OH)4
            .addMaterial(Materials.Magnesium, 3)
            .addMaterial(Materials.Silicon, 2)
            .addMaterial(Materials.Hydrogen, 4)
            .addMaterial(Materials.Oxygen, 9)
            .constructMaterial();
    }

    private static Materials loadAsh() {
        return new MaterialBuilder().setName("Ash")
            .setDefaultLocalName("Ashes")
            .setMetaItemSubID(815)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightGray)
            .setRGB(0x969696)
            .addDustItems()
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.PERDITIO, 1)
            .constructMaterial();
    }

    private static Materials loadBandedIron() {
        return new MaterialBuilder().setName("BandedIron")
            .setDefaultLocalName("Banded Iron")
            .setMetaItemSubID(917)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBrown)
            .setRGB(0x915a5a)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 2)
            .addMaterial(Materials.Oxygen, 3)
            .constructMaterial();
    }

    private static Materials loadBatteryAlloy() {
        return new MaterialBuilder().setName("BatteryAlloy")
            .setDefaultLocalName("Battery Alloy")
            .setMetaItemSubID(315)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePurple)
            .setRGB(0x9c7ca0)
            .addDustItems()
            .addMetalItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Lead, 4)
            .addMaterial(Materials.Antimony, 1)
            .constructMaterial();
    }

    private static Materials loadBenzene() {
        return new MaterialBuilder(686, TextureSet.SET_FLUID, "Benzene").addCell()
            .addFluid()
            .setRGB(26, 26, 26)
            .setColor(Dyes.dyeGray)
            .setFuelType(MaterialBuilder.GAS)
            .setFuelPower(360)
            .setMaterialList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 6))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadBlueTopaz() {
        return new MaterialBuilder().setName("BlueTopaz")
            .setDefaultLocalName("Blue Topaz")
            .setMetaItemSubID(513)
            .setIconSet(TextureSet.SET_GEM_HORIZONTAL)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x7f0000ff)
            .setToolSpeed(7.0f)
            .setDurability(256)
            .setToolQuality(3)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Fluorine, 2)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 6)
            .addAspect(TCAspects.LUCRUM, 6)
            .addAspect(TCAspects.VITREUS, 4)
            .constructMaterial();
    }

    private static Materials loadBone() {
        return new MaterialBuilder().setName("Bone")
            .setDefaultLocalName("Bone")
            .setMetaItemSubID(806)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xfafafa)
            .addDustItems()
            .addMaterial(Materials.Calcium, 1)
            .addAspect(TCAspects.MORTUUS, 2)
            .addAspect(TCAspects.CORPUS, 1)
            .constructMaterial();
    }

    private static Materials loadBrass() {
        return new MaterialBuilder().setName("Brass")
            .setDefaultLocalName("Brass")
            .setMetaItemSubID(301)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffb400)
            .setToolSpeed(7.0f)
            .setDurability(96)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Zinc, 1)
            .addMaterial(Materials.Copper, 3)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.INSTRUMENTUM, 1)
            .constructMaterial();
    }

    private static Materials loadBrick() {
        return new MaterialBuilder(625, TextureSet.SET_ROUGH, "Brick").addDustItems()
            .setRGB(155, 86, 67)
            .setColor(Dyes.dyeBrown)
            .setExtraData(0)
            .setMaterialList(
                new MaterialStack(Aluminium, 2),
                new MaterialStack(Silicon, 4),
                new MaterialStack(Oxygen, 11))
            .constructMaterial();
    }

    private static Materials loadBronze() {
        return new MaterialBuilder().setName("Bronze")
            .setDefaultLocalName("Bronze")
            .setMetaItemSubID(300)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xff8000)
            .setToolSpeed(6.0f)
            .setDurability(192)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Tin, 1)
            .addMaterial(Materials.Copper, 3)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.INSTRUMENTUM, 1)
            .constructMaterial();
    }

    private static Materials loadBrownLimonite() {
        return new MaterialBuilder().setName("BrownLimonite")
            .setDefaultLocalName("Brown Limonite")
            .setMetaItemSubID(930)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBrown)
            .setRGB(0xc86400)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            // FeO(OH)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadCalcite() {
        return new MaterialBuilder().setName("Calcite")
            .setDefaultLocalName("Calcite")
            .setMetaItemSubID(823)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xfae6dc)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Oxygen, 3)
            .constructMaterial();
    }

    private static Materials loadCarbonDioxide() {
        return new MaterialBuilder().setName("CarbonDioxide")
            .setDefaultLocalName("Carbon Dioxide")
            .setMetaItemSubID(497)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0xf0a9d0f5)
            .addCell()
            .addPlasma()
            .setMeltingPoint(25)
            .setBlastFurnaceTemp(1)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial()
            .setHasCorrespondingGas(true);
    }

    private static Materials loadCassiterite() {
        return new MaterialBuilder().setName("Cassiterite")
            .setDefaultLocalName("Cassiterite")
            .setMetaItemSubID(824)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xdcdcdc)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Tin, 1)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadCassiteriteSand() {
        return new MaterialBuilder().setName("CassiteriteSand")
            .setDefaultLocalName("Cassiterite Sand")
            .setMetaItemSubID(937)
            .setIconSet(TextureSet.SET_SAND)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xdcdcdc)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Tin, 1)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadChalcopyrite() {
        return new MaterialBuilder().setName("Chalcopyrite")
            .setDefaultLocalName("Chalcopyrite")
            .setMetaItemSubID(855)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xa07828)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Copper, 1)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Sulfur, 2)
            .constructMaterial();
    }

    private static Materials loadCharcoal() {
        return new MaterialBuilder().setName("Charcoal")
            .setDefaultLocalName("Charcoal")
            .setMetaItemSubID(536)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x644646)
            .addDustItems()
            .addGemItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.POTENTIA, 2)
            .addAspect(TCAspects.IGNIS, 2)
            .constructMaterial();
    }

    private static Materials loadChlorobenzene() {
        return new MaterialBuilder(605, TextureSet.SET_FLUID, "Chlorobenzene").addCell()
            .addFluid()
            .setRGB(0, 50, 65)
            .setColor(Dyes.dyeGray)
            .setMaterialList(
                new MaterialStack(Carbon, 6),
                new MaterialStack(Hydrogen, 5),
                new MaterialStack(Chlorine, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadChromite() {
        return new MaterialBuilder().setName("Chromite")
            .setDefaultLocalName("Chromite")
            .setMetaItemSubID(825)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setRGB(0x23140f)
            .addDustItems()
            .addOreItems()
            .setMeltingPoint(1700)
            .setBlastFurnaceTemp(1700)
            .setBlastFurnaceRequired(true)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Chrome, 2)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadChromiumDioxide() {
        return new MaterialBuilder().setName("ChromiumDioxide")
            .setDefaultLocalName("Chromium Dioxide")
            .setMetaItemSubID(361)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePink)
            .setRGB(0xe6c8c8)
            .setToolSpeed(11.0f)
            .setDurability(256)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(650)
            .setBlastFurnaceTemp(650)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Chrome, 1)
            .addMaterial(Materials.Oxygen, 2)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MACHINA, 1)
            .constructMaterial();
    }

    private static Materials loadCinnabar() {
        return new MaterialBuilder().setName("Cinnabar")
            .setDefaultLocalName("Cinnabar")
            .setMetaItemSubID(826)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeBrown)
            .setRGB(0x960000)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Mercury, 1)
            .addMaterial(Materials.Sulfur, 1)
            .constructMaterial();
    }

    private static Materials loadCoal() {
        return new MaterialBuilder().setName("Coal")
            .setDefaultLocalName("Coal")
            .setMetaItemSubID(535)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x464646)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .setDensityMultiplier(2)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.POTENTIA, 2)
            .addAspect(TCAspects.IGNIS, 2)
            .constructMaterial();
    }

    private static Materials loadCobaltOxide() {
        return new MaterialBuilder(616, TextureSet.SET_DULL, "Cobalt Oxide").addDustItems()
            .setRGB(102, 128, 0)
            .setColor(Dyes.dyeGreen)
            .setMaterialList(new MaterialStack(Cobalt, 1), new MaterialStack(Oxygen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadCobaltite() {
        return new MaterialBuilder().setName("Cobaltite")
            .setDefaultLocalName("Cobaltite")
            .setMetaItemSubID(827)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x5050fa)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Cobalt, 1)
            .addMaterial(Materials.Arsenic, 1)
            .addMaterial(Materials.Sulfur, 1)
            .constructMaterial();
    }

    private static Materials loadCooperite() {
        return new MaterialBuilder().setName("Cooperite")
            .setDefaultLocalName("Sheldonite")
            .setMetaItemSubID(828)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffffc8)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Platinum, 3)
            .addMaterial(Materials.Nickel, 1)
            .addMaterial(Materials.Sulfur, 1)
            .addMaterial(Materials.Palladium, 1)
            .constructMaterial();
    }

    private static Materials loadCupricOxide() {
        return new MaterialBuilder(619, TextureSet.SET_DULL, "Cupric Oxide").addDustItems()
            .setRGB(15, 15, 15)
            .setColor(Dyes.dyeBlack)
            .setMeltingPoint(1599)
            .setMaterialList(new MaterialStack(Copper, 1), new MaterialStack(Oxygen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadCupronickel() {
        return new MaterialBuilder().setName("Cupronickel")
            .setDefaultLocalName("Cupronickel")
            .setMetaItemSubID(310)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xe39680)
            .setToolSpeed(6.0f)
            .setDurability(64)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Copper, 1)
            .addMaterial(Materials.Nickel, 1)
            .constructMaterial();
    }

    private static Materials loadDarkAsh() {
        return new MaterialBuilder().setName("DarkAsh")
            .setDefaultLocalName("Dark Ashes")
            .setMetaItemSubID(816)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setRGB(0x323232)
            .addDustItems()
            .setDensityMultiplier(2)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.IGNIS, 1)
            .addAspect(TCAspects.PERDITIO, 1)
            .constructMaterial();
    }

    private static Materials loadDeepIron() {
        return new MaterialBuilder().setName("DeepIron")
            .setDefaultLocalName("Deep Iron")
            .setMetaItemSubID(829)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setRGB(0x968c8c)
            .setToolSpeed(6.0f)
            .setDurability(384)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .setMeltingPoint(7500)
            .setBlastFurnaceTemp(7500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addMaterial(Materials.Iron, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .constructMaterial();
    }

    private static Materials loadDiamond() {
        return new MaterialBuilder().setName("Diamond")
            .setDefaultLocalName("Diamond")
            .setMetaItemSubID(500)
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeWhite)
            .setARGB(0x7fc8ffff)
            .setToolSpeed(8.0f)
            .setDurability(1280)
            .setToolQuality(4)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setDensityMultiplier(64)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.VITREUS, 3)
            .addAspect(TCAspects.LUCRUM, 4)
            .constructMaterial();
    }

    private static Materials loadDilutedHydrochloricAcid() {
        return new MaterialBuilder(606, TextureSet.SET_FLUID, "Diluted Hydrochloric Acid")
            .setName("DilutedHydrochloricAcid_GT5U")
            .addCell()
            .addFluid()
            .setRGB(153, 167, 163)
            .setColor(Dyes.dyeLightGray)
            .setMaterialList(new MaterialStack(Hydrogen, 1), new MaterialStack(Chlorine, 1))
            .constructMaterial();
    }

    private static Materials loadElectrum() {
        return new MaterialBuilder().setName("Electrum")
            .setDefaultLocalName("Electrum")
            .setMetaItemSubID(303)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff64)
            .setToolSpeed(12.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Silver, 1)
            .addMaterial(Materials.Gold, 1)
            .constructMaterial();
    }

    private static Materials loadEmerald() {
        return new MaterialBuilder().setName("Emerald")
            .setDefaultLocalName("Emerald")
            .setMetaItemSubID(501)
            .setIconSet(TextureSet.SET_EMERALD)
            .setColor(Dyes.dyeGreen)
            .setARGB(0x7f50ff50)
            .setToolSpeed(7.0f)
            .setDurability(256)
            .setToolQuality(4)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addMaterial(Materials.Beryllium, 3)
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 6)
            .addMaterial(Materials.Oxygen, 18)
            .addAspect(TCAspects.VITREUS, 3)
            .addAspect(TCAspects.LUCRUM, 5)
            .constructMaterial();
    }

    private static Materials loadEpoxid() {
        return new MaterialBuilder().setName("Epoxid")
            .setDefaultLocalName("Epoxid")
            .setMetaItemSubID(470)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xc88c14)
            .setToolSpeed(3.0f)
            .setDurability(32)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(400)
            .addMaterial(Materials.Carbon, 21)
            .addMaterial(Materials.Hydrogen, 24)
            .addMaterial(Materials.Oxygen, 4)
            .addAspect(TCAspects.MOTUS, 2)
            .constructMaterial();
    }

    private static Materials loadFerriteMixture() {
        return new MaterialBuilder(612, TextureSet.SET_METALLIC, "Ferrite Mixture").addDustItems()
            .setRGB(180, 180, 180)
            .setColor(Dyes.dyeGray)
            .setMaterialList(new MaterialStack(Nickel, 1), new MaterialStack(Zinc, 1), new MaterialStack(Iron, 4))
            .constructMaterial();
    }

    private static Materials loadFerrosilite() {
        return new MaterialBuilder(620, TextureSet.SET_DULL, "Ferrosilite").addDustItems()
            .setRGB(151, 99, 42)
            .setColor(Dyes.dyeBrown)
            .setMaterialList(new MaterialStack(Iron, 1), new MaterialStack(Silicon, 1), new MaterialStack(Oxygen, 3))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadFreshWater() {
        return new MaterialBuilder().setName("FreshWater")
            .setDefaultLocalName("Fresh Water")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x0000ff)
            .addCell()
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.AQUA, 2)
            .constructMaterial();
    }

    private static Materials loadGalena() {
        return new MaterialBuilder().setName("Galena")
            .setDefaultLocalName("Galena")
            .setMetaItemSubID(830)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePurple)
            .setRGB(0x643c64)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Lead, 1)
            .addMaterial(Materials.Sulfur, 1)
            .constructMaterial();
    }

    private static Materials loadGarnierite() {
        return new MaterialBuilder().setName("Garnierite")
            .setDefaultLocalName("Garnierite")
            .setMetaItemSubID(906)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightBlue)
            .setRGB(0x32c846)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Nickel, 1)
            .addMaterial(Materials.Oxygen, 1)
            .constructMaterial();
    }

    private static Materials loadGlyceryl() {
        return new MaterialBuilder().setName("Glyceryl")
            .setDefaultLocalName("Glyceryl Trinitrate")
            .setMetaItemSubID(714)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeCyan)
            .setRGB(0x009696)
            .addCell()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 3)
            .addMaterial(Materials.Hydrogen, 5)
            .addMaterial(Materials.Nitrogen, 3)
            .addMaterial(Materials.Oxygen, 9)
            .constructMaterial();
    }

    private static Materials loadGreenSapphire() {
        return new MaterialBuilder(504, TextureSet.SET_GEM_HORIZONTAL, "Green Sapphire").setToolSpeed(7.0F)
            .setDurability(256)
            .setToolQuality(2)
            .addDustItems()
            .addGemItems()
            .setTransparent(true)
            .addOreItems()
            .addToolHeadItems()
            .setRGBA(100, 200, 130, 127)
            .setColor(Dyes.dyeCyan)
            .setExtraData(0)
            .setMaterialList(new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 5),
                    new TCAspects.TC_AspectStack(TCAspects.VITREUS, 3)))
            .constructMaterial()
            .disableAutoGeneratedBlastFurnaceRecipes();
    }

    private static Materials loadGrossular() {
        return new MaterialBuilder().setName("Grossular")
            .setDefaultLocalName("Grossular")
            .setMetaItemSubID(831)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xc86400)
            .addDustItems()
            .addOreItems()
            .addMaterial(Materials.Calcium, 3)
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 12)
            .constructMaterial();
    }

    private static Materials loadHolyWater() {
        return new MaterialBuilder().setName("HolyWater")
            .setDefaultLocalName("Holy Water")
            .setMetaItemSubID(729)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x0000ff)
            .addCell()
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.AQUA, 2)
            .addAspect(TCAspects.AURAM, 1)
            .constructMaterial();
    }

    private static Materials loadHydricSulfide() {
        return new MaterialBuilder().setName("HydricSulfide")
            .setDefaultLocalName("Hydrogen Sulfide")
            .setMetaItemSubID(460)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .addCell()
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Sulfur, 1)
            .constructMaterial();
    }

    private static Materials loadIce() {
        return new MaterialBuilder().setName("Ice")
            .setDefaultLocalName("Ice")
            .setMetaItemSubID(702)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlue)
            .setRGB(0xc8c8ff)
            .addDustItems()
            .addCell()
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.GELUM, 2)
            .constructMaterial();
    }

    private static Materials loadIlmenite() {
        return new MaterialBuilder().setName("Ilmenite")
            .setDefaultLocalName("Ilmenite")
            .setMetaItemSubID(918)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePurple)
            .setRGB(0x463732)
            .addDustItems()
            .addOreItems()
            .setDensityMultiplier(2)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Titanium, 1)
            .addMaterial(Materials.Oxygen, 3)
            .constructMaterial();
    }

    private static Materials loadInvar() {
        return new MaterialBuilder().setName("Invar")
            .setDefaultLocalName("Invar")
            .setMetaItemSubID(302)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBrown)
            .setRGB(0xb4b478)
            .setToolSpeed(6.0f)
            .setDurability(256)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Iron, 2)
            .addMaterial(Materials.Nickel, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.GELUM, 1)
            .constructMaterial();
    }

    private static Materials loadKanthal() {
        return new MaterialBuilder().setName("Kanthal")
            .setDefaultLocalName("Kanthal")
            .setMetaItemSubID(312)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xc2d2df)
            .setToolSpeed(6.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .setMeltingPoint(1800)
            .setBlastFurnaceTemp(1800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Aluminium, 1)
            .addMaterial(Materials.Chrome, 1)
            .constructMaterial();
    }

    private static Materials loadLazurite() {
        return new MaterialBuilder().setName("Lazurite")
            .setDefaultLocalName("Lazurite")
            .setMetaItemSubID(524)
            .setIconSet(TextureSet.SET_LAPIS)
            .setColor(Dyes.dyeCyan)
            .setRGB(0x6478ff)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Aluminium, 6)
            .addMaterial(Materials.Silicon, 6)
            .addMaterial(Materials.Calcium, 8)
            .addMaterial(Materials.Sodium, 8)
            .constructMaterial();
    }

    private static Materials loadLiquidAir() {
        return new MaterialBuilder().setName("LiquidAir")
            .setDefaultLocalName("Liquid Air")
            .setMetaItemSubID(495)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0xf0a9d0f5)
            .addCell()
            .addPlasma()
            .setMeltingPoint(4)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Nitrogen, 40)
            .addMaterial(Materials.Oxygen, 11)
            .addMaterial(Materials.Argon, 1)
            .addMaterial(Materials.NobleGases, 1)
            .constructMaterial();
    }

    private static Materials loadLiquidNitrogen() {
        return new MaterialBuilder().setName("LiquidNitrogen")
            .setDefaultLocalName("Liquid Nitrogen")
            .setMetaItemSubID(494)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0xf0a9d0f5)
            .addCell()
            .addPlasma()
            .setMeltingPoint(4)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Nitrogen, 1)
            .constructMaterial();
    }

    private static Materials loadLiquidOxygen() {
        return new MaterialBuilder().setName("LiquidOxygen")
            .setDefaultLocalName("Liquid Oxygen")
            .setMetaItemSubID(493)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0xf0a9d0f5)
            .addCell()
            .addPlasma()
            .setMeltingPoint(4)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Oxygen, 1)
            .constructMaterial();
    }

    private static Materials loadMagnalium() {
        return new MaterialBuilder().setName("Magnalium")
            .setDefaultLocalName("Magnalium")
            .setMetaItemSubID(313)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightBlue)
            .setRGB(0xc8beff)
            .setToolSpeed(6.0f)
            .setDurability(256)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Aluminium, 2)
            .constructMaterial();
    }

    private static Materials loadMagnesia() {
        return new MaterialBuilder(621, TextureSet.SET_DULL, "Magnesia").addDustItems()
            .setRGB(255, 225, 225)
            .setColor(Dyes.dyeWhite)
            .setMaterialList(new MaterialStack(Magnesium, 1), new MaterialStack(Oxygen, 1))
            .constructMaterial();
    }

    private static Materials loadMagnesite() {
        return new MaterialBuilder().setName("Magnesite")
            .setDefaultLocalName("Magnesite")
            .setMetaItemSubID(908)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setRGB(0xfafab4)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Oxygen, 3)
            .constructMaterial();
    }

    private static Materials loadMagnesiumchloride() {
        return new MaterialBuilder().setName("Magnesiumchloride")
            .setDefaultLocalName("Magnesiumchloride")
            .setMetaItemSubID(377)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeRed)
            .setRGB(0xd40d5c)
            .addDustItems()
            .addCell()
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Chlorine, 2)
            .constructMaterial();
    }

    private static Materials loadMagnetite() {
        return new MaterialBuilder().setName("Magnetite")
            .setDefaultLocalName("Magnetite")
            .setMetaItemSubID(870)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setRGB(0x1e1e1e)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 3)
            .addMaterial(Materials.Oxygen, 4)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .constructMaterial();
    }

    private static Materials loadMassicot() {
        return new MaterialBuilder(614, TextureSet.SET_DULL, "Massicot").addDustItems()
            .setRGB(255, 221, 85)
            .setColor(Dyes.dyeYellow)
            .setMaterialList(new MaterialStack(Lead, 1), new MaterialStack(Oxygen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadMethane() {
        return new MaterialBuilder().setName("Methane")
            .setDefaultLocalName("Methane")
            .setMetaItemSubID(715)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeMagenta)
            .addCell()
            .setFuelType(1)
            .setFuelPower(104)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Hydrogen, 4)
            .constructMaterial();
    }

    private static Materials loadMolybdenite() {
        return new MaterialBuilder().setName("Molybdenite")
            .setDefaultLocalName("Molybdenite")
            .setMetaItemSubID(942)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x191919)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // MoS2 (also source of Re)
            .addMaterial(Materials.Molybdenum, 1)
            .addMaterial(Materials.Sulfur, 2)
            .constructMaterial();
    }

    private static Materials loadNichrome() {
        return new MaterialBuilder().setName("Nichrome")
            .setDefaultLocalName("Nichrome")
            .setMetaItemSubID(311)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setRGB(0xcdcef6)
            .setToolSpeed(6.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .setMeltingPoint(2700)
            .setBlastFurnaceTemp(2700)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Nickel, 4)
            .addMaterial(Materials.Chrome, 1)
            .constructMaterial();
    }

    private static Materials loadNickelZincFerrite() {
        return new MaterialBuilder(613, TextureSet.SET_ROUGH, "Nickel-Zinc Ferrite").addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setToolSpeed(3.0f)
            .setDurability(32)
            .setRGB(60, 60, 60)
            .setColor(Dyes.dyeBlack)
            .setBlastFurnaceRequired(true)
            .setBlastFurnaceTemp(1500)
            .setMaterialList(
                new MaterialStack(Nickel, 1),
                new MaterialStack(Zinc, 1),
                new MaterialStack(Iron, 4),
                new MaterialStack(Oxygen, 8))
            .constructMaterial();
    }

    private static Materials loadNiobiumNitride() {
        // Anti-Reflective Material
        return new MaterialBuilder().setName("NiobiumNitride")
            .setDefaultLocalName("Niobium Nitride")
            .setMetaItemSubID(359)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x1d291d)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(2573)
            .setBlastFurnaceTemp(2573)
            .setBlastFurnaceRequired(true)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Niobium, 1)
            .addMaterial(Materials.Nitrogen, 1)
            .constructMaterial();
    }

    private static Materials loadNiobiumTitanium() {
        return new MaterialBuilder().setName("NiobiumTitanium")
            .setDefaultLocalName("Niobium-Titanium")
            .setMetaItemSubID(360)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x1d1d29)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(4500)
            .setBlastFurnaceTemp(4500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Niobium, 1)
            .addMaterial(Materials.Titanium, 1)
            .constructMaterial();
    }

    private static Materials loadNitroCarbon() {
        return new MaterialBuilder().setName("NitroCarbon")
            .setDefaultLocalName("Nitro-Carbon")
            .setMetaItemSubID(716)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeCyan)
            .setRGB(0x004b64)
            .addCell()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Nitrogen, 1)
            .addMaterial(Materials.Carbon, 1)
            .constructMaterial();
    }

    private static Materials loadNitrogenDioxide() {
        return new MaterialBuilder().setName("NitrogenDioxide")
            .setDefaultLocalName("Nitrogen Dioxide")
            .setMetaItemSubID(717)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeCyan)
            .setRGB(0x64afff)
            .addCell()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Nitrogen, 1)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadNobleGases() {
        return new MaterialBuilder().setName("NobleGases")
            .setDefaultLocalName("Noble Gases")
            .setMetaItemSubID(496)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLightBlue)
            .setARGB(0xf0a9d0f5)
            .addCell()
            .addPlasma()
            .setMeltingPoint(4)
            .addCentrifugeRecipe()
            .addMaterial(Materials.CarbonDioxide, 21)
            .addMaterial(Materials.Helium, 9)
            .addMaterial(Materials.Methane, 3)
            .addMaterial(Materials.Deuterium, 1)
            .constructMaterial()
            .setHasCorrespondingGas(true);
    }

    private static Materials loadObsidian() {
        return new MaterialBuilder().setName("Obsidian")
            .setDefaultLocalName("Obsidian")
            .setMetaItemSubID(804)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x503264)
            .addDustItems()
            .addMetalItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Silicon, 2)
            .addMaterial(Materials.Oxygen, 8)
            .constructMaterial();
    }

    private static Materials loadPhosphate() {
        return new MaterialBuilder().setName("Phosphate")
            .setDefaultLocalName("Phosphate")
            .setMetaItemSubID(833)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff00)
            .addDustItems()
            .addOreItems()
            .addCell()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Phosphorus, 1)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadPigIron() {
        return new MaterialBuilder().setName("PigIron")
            .setDefaultLocalName("Pig Iron")
            .setMetaItemSubID(307)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setRGB(0xc8b4b4)
            .setToolSpeed(6.0f)
            .setDurability(384)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Iron, 1)
            .constructMaterial();
    }

    private static Materials loadPlastic() {
        return new MaterialBuilder().setName("Plastic")
            .setDefaultLocalName("Polyethylene")
            .setMetaItemSubID(874)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xc8c8c8)
            .setToolSpeed(3.0f)
            .setDurability(32)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(400)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Hydrogen, 2)
            .addAspect(TCAspects.MOTUS, 2)
            .constructMaterial();
    }

    private static Materials loadPolycaprolactam() {
        return new MaterialBuilder().setName("Polycaprolactam")
            .setDefaultLocalName("Polycaprolactam")
            .setMetaItemSubID(472)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0x323232)
            .setToolSpeed(3.0f)
            .setDurability(32)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(500)
            .addMaterial(Materials.Carbon, 6)
            .addMaterial(Materials.Hydrogen, 11)
            .addMaterial(Materials.Nitrogen, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.MOTUS, 2)
            .constructMaterial();
    }

    private static Materials loadPolydimethylsiloxane() {
        return new MaterialBuilder(633, TextureSet.SET_FLUID, "Polydimethylsiloxane").addDustItems()
            .setRGB(245, 245, 245)
            .setColor(Dyes.dyeWhite)
            .setMaterialList(
                new MaterialStack(Carbon, 2),
                new MaterialStack(Hydrogen, 6),
                new MaterialStack(Oxygen, 1),
                new MaterialStack(Silicon, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadPolytetrafluoroethylene() {
        return new MaterialBuilder().setName("Polytetrafluoroethylene")
            .setDefaultLocalName("Polytetrafluoroethylene")
            .setMetaItemSubID(473)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0x646464)
            .setToolSpeed(3.0f)
            .setDurability(32)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(600)
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Fluorine, 4)
            .addAspect(TCAspects.MOTUS, 2)
            .constructMaterial();
    }

    private static Materials loadPotash() {
        return new MaterialBuilder(623, TextureSet.SET_DULL, "Potash").addDustItems()
            .setRGB(120, 66, 55)
            .setColor(Dyes.dyeBrown)
            .setMaterialList(new MaterialStack(Potassium, 2), new MaterialStack(Oxygen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadPowellite() {
        return new MaterialBuilder().setName("Powellite")
            .setDefaultLocalName("Powellite")
            .setMetaItemSubID(883)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff00)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Molybdenum, 1)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadPumice() {
        return new MaterialBuilder().setName("Pumice")
            .setDefaultLocalName("Pumice")
            .setMetaItemSubID(926)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setRGB(0xe6b9b9)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Stone, 1)
            .constructMaterial();
    }

    private static Materials loadPyrite() {
        return new MaterialBuilder().setName("Pyrite")
            .setDefaultLocalName("Pyrite")
            .setMetaItemSubID(834)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeOrange)
            .setRGB(0x967828)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Sulfur, 2)
            .constructMaterial();
    }

    private static Materials loadPyrochlore() {
        return new MaterialBuilder(607, TextureSet.SET_METALLIC, "Pyrochlore").addDustItems()
            .addOreItems()
            .setRGB(43, 17, 0)
            .setColor(Dyes.dyeBlack)
            .setMaterialList(new MaterialStack(Calcium, 2), new MaterialStack(Niobium, 2), new MaterialStack(Oxygen, 7))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadPyrolusite() {
        return new MaterialBuilder().setName("Pyrolusite")
            .setDefaultLocalName("Pyrolusite")
            .setMetaItemSubID(943)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightGray)
            .setRGB(0x9696aa)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Manganese, 1)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadPyrope() {
        return new MaterialBuilder().setName("Pyrope")
            .setDefaultLocalName("Pyrope")
            .setMetaItemSubID(835)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePurple)
            .setRGB(0x783264)
            .addDustItems()
            .addOreItems()
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Magnesium, 3)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 12)
            .constructMaterial();
    }

    private static Materials loadQuicklime() {
        return new MaterialBuilder(622, TextureSet.SET_DULL, "Quicklime").addDustItems()
            .setRGB(240, 240, 240)
            .setColor(Dyes.dyeWhite)
            .setMaterialList(new MaterialStack(Calcium, 1), new MaterialStack(Oxygen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadRawRubber() {
        return new MaterialBuilder().setName("RawRubber")
            .setDefaultLocalName("Raw Rubber")
            .setMetaItemSubID(896)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xccc789)
            .addDustItems()
            .setMeltingPoint(400)
            .addMaterial(Materials.Carbon, 5)
            .addMaterial(Materials.Hydrogen, 8)
            .addAspect(TCAspects.MOTUS, 2)
            .constructMaterial();
    }

    private static Materials loadRockSalt() {
        return new MaterialBuilder().setName("RockSalt")
            .setDefaultLocalName("Rock Salt")
            .setMetaItemSubID(944)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xf0c8c8)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Chlorine, 1)
            .constructMaterial();
    }

    private static Materials loadRubber() {
        return new MaterialBuilder().setName("Rubber")
            .setDefaultLocalName("Rubber")
            .setMetaItemSubID(880)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x000000)
            .setToolSpeed(1.5f)
            .setDurability(32)
            .setToolQuality(0)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(400)
            .addMaterial(Materials.Carbon, 5)
            .addMaterial(Materials.Hydrogen, 8)
            .addAspect(TCAspects.MOTUS, 2)
            .constructMaterial();
    }

    private static Materials loadRuby() {
        return new MaterialBuilder().setName("Ruby")
            .setDefaultLocalName("Ruby")
            .setMetaItemSubID(502)
            .setIconSet(TextureSet.SET_RUBY)
            .setColor(Dyes.dyeRed)
            .setARGB(0x7fff6464)
            .setToolSpeed(7.0f)
            .setDurability(256)
            .setToolQuality(2)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addMaterial(Materials.Chrome, 1)
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Oxygen, 3)
            .addAspect(TCAspects.LUCRUM, 6)
            .addAspect(TCAspects.VITREUS, 4)
            .constructMaterial();
    }

    private static Materials loadRutile() {
        return new MaterialBuilder().setName("Rutile")
            .setDefaultLocalName("Rutile")
            .setMetaItemSubID(375)
            .setIconSet(TextureSet.SET_GEM_HORIZONTAL)
            .setColor(Dyes.dyeRed)
            .setRGB(0xd40d5c)
            .addDustItems()
            .addOreItems()
            .setDensityMultiplier(2)
            .addMaterial(Materials.Titanium, 1)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadSalt() {
        return new MaterialBuilder().setName("Salt")
            .setDefaultLocalName("Salt")
            .setMetaItemSubID(817)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xfafafa)
            .addDustItems()
            .addOreItems()
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Chlorine, 1)
            .constructMaterial();
    }

    private static Materials loadSaltpeter() {
        return new MaterialBuilder().setName("Saltpeter")
            .setDefaultLocalName("Saltpeter")
            .setMetaItemSubID(836)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xe6e6e6)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Nitrogen, 1)
            .addMaterial(Materials.Oxygen, 3)
            .constructMaterial();
    }

    private static Materials loadSapphire() {
        return new MaterialBuilder(503, TextureSet.SET_GEM_VERTICAL, "Sapphire").setToolSpeed(7.0F)
            .setDurability(256)
            .setToolQuality(2)
            .addDustItems()
            .addGemItems()
            .setTransparent(true)
            .addOreItems()
            .addToolHeadItems()
            .setRGBA(100, 100, 200, 127)
            .setColor(Dyes.dyeBlue)
            .setExtraData(0)
            .setMaterialList(new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 5),
                    new TCAspects.TC_AspectStack(TCAspects.VITREUS, 3)))
            .constructMaterial();
    }

    private static Materials loadScheelite() {
        return new MaterialBuilder().setName("Scheelite")
            .setDefaultLocalName("Scheelite")
            .setMetaItemSubID(910)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setRGB(0xc88c14)
            .addDustItems()
            .addOreItems()
            .setMeltingPoint(2500)
            .setBlastFurnaceTemp(2500)
            .addMaterial(Materials.Tungsten, 1)
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadSiliconDioxide() {
        return new MaterialBuilder(837, TextureSet.SET_QUARTZ, "Silicon Dioxide").setToolSpeed(1.0F)
            .setDurability(0)
            .setToolQuality(1)
            .addDustItems()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeLightGray)
            .setExtraData(0)
            .setMaterialList(new MaterialStack(Silicon, 1), new MaterialStack(Oxygen, 2))
            .constructMaterial();
    }

    private static Materials loadSilicone() {
        return new MaterialBuilder().setName("Silicone")
            .setDefaultLocalName("Silicone Rubber")
            .setMetaItemSubID(471)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xdcdcdc)
            .setToolSpeed(3.0f)
            .setDurability(128)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(900)
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 1)
            .addMaterial(Materials.Silicon, 1)
            .addAspect(TCAspects.MOTUS, 2)
            .constructMaterial();
    }

    private static Materials loadSnow() {
        return new MaterialBuilder().setName("Snow")
            .setDefaultLocalName("Snow")
            .setMetaItemSubID(728)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xfafafa)
            .addDustItems()
            .addCell()
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.GELUM, 1)
            .constructMaterial();
    }

    private static Materials loadSodaAsh() {
        return new MaterialBuilder(624, TextureSet.SET_DULL, "Soda Ash").addDustItems()
            .setRGB(220, 220, 255)
            .setColor(Dyes.dyeWhite)
            .setMaterialList(new MaterialStack(Sodium, 2), new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 3))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadSodalite() {
        return new MaterialBuilder().setName("Sodalite")
            .setDefaultLocalName("Sodalite")
            .setMetaItemSubID(525)
            .setIconSet(TextureSet.SET_LAPIS)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x1414ff)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Aluminium, 3)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Sodium, 4)
            .addMaterial(Materials.Chlorine, 1)
            .constructMaterial();
    }

    private static Materials loadSodiumPersulfate() {
        return new MaterialBuilder().setName("SodiumPersulfate")
            .setDefaultLocalName("Sodium Persulfate")
            .setMetaItemSubID(718)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .addCell()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Sodium, 2)
            .addMaterial(Materials.Sulfur, 2)
            .addMaterial(Materials.Oxygen, 8)
            .constructMaterial();
    }

    private static Materials loadSodiumSulfide() {
        return new MaterialBuilder().setName("SodiumSulfide")
            .setDefaultLocalName("Sodium Sulfide")
            .setMetaItemSubID(719)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xffe680)
            .addDustItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Sodium, 2)
            .addMaterial(Materials.Sulfur, 1)
            .constructMaterial();
    }

    private static Materials loadTitaniumtetrachloride() {
        return new MaterialBuilder().setName("Titaniumtetrachloride")
            .setDefaultLocalName("Titaniumtetrachloride")
            .setMetaItemSubID(376)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeRed)
            .setRGB(0xd40d5c)
            .addCell()
            .addMaterial(Materials.Titanium, 1)
            .addMaterial(Materials.Chlorine, 4)
            .constructMaterial();
    }

    private static Materials loadWater() {
        return new MaterialBuilder().setName("Water")
            .setDefaultLocalName("Water")
            .setMetaItemSubID(701)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x0000ff)
            .addCell()
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 1)
            .addAspect(TCAspects.AQUA, 2)
            .constructMaterial();
    }

    private static Materials loadZincite() {
        return new MaterialBuilder(617, TextureSet.SET_DULL, "Zincite").addDustItems()
            .setRGB(255, 255, 245)
            .setColor(Dyes.dyeWhite)
            .setMaterialList(new MaterialStack(Zinc, 1), new MaterialStack(Oxygen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static void loadUnclassified01() {
        Materials.OilExtraHeavy = loadOilExtraHeavy();
        Materials.OilHeavy = loadOilHeavy();
        Materials.OilLight = loadOilLight();
        Materials.OilMedium = loadOilMedium();
    }

    private static Materials loadOilExtraHeavy() {
        return new MaterialBuilder().setName("OilExtraHeavy")
            .setDefaultLocalName("Very Heavy Oil")
            .setMetaItemSubID(570)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x0a0a0a)
            .addCell()
            .setFuelType(3)
            .setFuelPower(45)
            .constructMaterial();
    }

    private static Materials loadOilHeavy() {
        return new MaterialBuilder().setName("OilHeavy")
            .setDefaultLocalName("Heavy Oil")
            .setMetaItemSubID(730)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x0a0a0a)
            .addCell()
            .setFuelType(3)
            .setFuelPower(40)
            .constructMaterial();
    }

    private static Materials loadOilLight() {
        return new MaterialBuilder().setName("OilLight")
            .setDefaultLocalName("Light Oil")
            .setMetaItemSubID(732)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x0a0a0a)
            .addCell()
            .setFuelType(3)
            .setFuelPower(20)
            .constructMaterial();
    }

    private static Materials loadOilMedium() {
        return new MaterialBuilder().setName("OilMedium")
            .setDefaultLocalName("Raw Oil")
            .setMetaItemSubID(731)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x0a0a0a)
            .addCell()
            .setFuelType(3)
            .setFuelPower(30)
            .constructMaterial();
    }

    private static void loadUnclassified02() {
        Materials.Gas = loadGas();
        Materials.HeavyFuel = loadHeavyFuel();
        Materials.LightFuel = loadLightFuel();
        Materials.LPG = loadLPG();
        Materials.Naphtha = loadNaphtha();
        Materials.NatruralGas = loadNatruralGas(); // Nat"r"uralGas lol
        Materials.SulfuricGas = loadSulfuricGas();
        Materials.SulfuricHeavyFuel = loadSulfuricHeavyFuel();
        Materials.SulfuricLightFuel = loadSulfuricLightFuel();
        Materials.SulfuricNaphtha = loadSulfuricNaphtha();
    }

    private static Materials loadGas() {
        return new MaterialBuilder().setName("Gas")
            .setDefaultLocalName("Refinery Gas")
            .setMetaItemSubID(735)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .setFuelType(1)
            .setFuelPower(160)
            .constructMaterial()
            .setCanBeCracked(true);
    }

    private static Materials loadHeavyFuel() {
        return new MaterialBuilder().setName("HeavyFuel")
            .setDefaultLocalName("Heavy Fuel")
            .setMetaItemSubID(741)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setRGB(0xffff00)
            .addCell()
            .setFuelType(3)
            .setFuelPower(240)
            .constructMaterial()
            .setCanBeCracked(true);
    }

    private static Materials loadLightFuel() {
        return new MaterialBuilder().setName("LightFuel")
            .setDefaultLocalName("Light Fuel")
            .setMetaItemSubID(740)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff00)
            .addCell()
            .setFuelPower(305)
            .constructMaterial()
            .setCanBeCracked(true);
    }

    private static Materials loadLPG() {
        return new MaterialBuilder().setName("LPG")
            .setDefaultLocalName("LPG")
            .setMetaItemSubID(742)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff00)
            .addCell()
            .setFuelType(1)
            .setFuelPower(320)
            .constructMaterial();
    }

    private static Materials loadNaphtha() {
        return new MaterialBuilder().setName("Naphtha")
            .setDefaultLocalName("Naphtha")
            .setMetaItemSubID(739)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff00)
            .addCell()
            .setFuelType(1)
            .setFuelPower(220)
            .constructMaterial()
            .setCanBeCracked(true);
    }

    private static Materials loadNatruralGas() {
        return new MaterialBuilder().setName("NatruralGas")
            .setDefaultLocalName("Natural Gas")
            .setMetaItemSubID(733)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .setFuelType(1)
            .setFuelPower(20)
            .constructMaterial();
    }

    private static Materials loadSulfuricGas() {
        return new MaterialBuilder().setName("SulfuricGas")
            .setDefaultLocalName("Sulfuric Gas")
            .setMetaItemSubID(734)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeWhite)
            .addCell()
            .setFuelType(1)
            .setFuelPower(25)
            .constructMaterial();
    }

    private static Materials loadSulfuricHeavyFuel() {
        return new MaterialBuilder().setName("SulfuricHeavyFuel")
            .setDefaultLocalName("Sulfuric Heavy Fuel")
            .setMetaItemSubID(738)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setRGB(0xffff00)
            .addCell()
            .setFuelType(3)
            .setFuelPower(40)
            .constructMaterial();
    }

    private static Materials loadSulfuricLightFuel() {
        return new MaterialBuilder().setName("SulfuricLightFuel")
            .setDefaultLocalName("Sulfuric Light Fuel")
            .setMetaItemSubID(737)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff00)
            .addCell()
            .setFuelPower(40)
            .constructMaterial();
    }

    private static Materials loadSulfuricNaphtha() {
        return new MaterialBuilder().setName("SulfuricNaphtha")
            .setDefaultLocalName("Sulfuric Naphtha")
            .setMetaItemSubID(736)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff00)
            .addCell()
            .setFuelType(1)
            .setFuelPower(40)
            .constructMaterial();
    }

    private static void loadUnclassified03() {
        Materials.BioMediumRaw = loadBioMediumRaw();
        Materials.BioMediumSterilized = loadBioMediumSterilized();
        Materials.ReinforceGlass = loadReinforceGlass();
    }

    private static Materials loadBioMediumRaw() {
        return new MaterialBuilder(603, TextureSet.SET_FLUID, "Raw Bio Catalyst Medium").setName("BioMediumRaw")
            .addCell()
            .addFluid()
            .setRGB(97, 147, 46)
            .setColor(Dyes.dyeLime)
            .constructMaterial();
    }

    private static Materials loadBioMediumSterilized() {
        return new MaterialBuilder(604, TextureSet.SET_FLUID, "Sterilized Bio Catalyst Medium")
            .setName("BiohMediumSterilized")
            .addCell()
            .addFluid()
            .setRGB(162, 253, 53)
            .setColor(Dyes.dyeLime)
            .constructMaterial();
    }

    private static Materials loadReinforceGlass() {
        return new MaterialBuilder(602, TextureSet.SET_FLUID, "Reinforced Glass").setName("ReinforcedGlass")
            .setRGB(192, 245, 254)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(2000)
            .constructMaterial()
            .disableAutoGeneratedRecycleRecipes();
    }

    private static void loadUnclassified04() {
        Materials.GrowthMediumRaw = loadGrowthMediumRaw();
        Materials.GrowthMediumSterilized = loadGrowthMediumSterilized();
    }

    private static Materials loadGrowthMediumRaw() {
        return new MaterialBuilder(608, TextureSet.SET_FLUID, "Raw Growth Catalyst Medium").setName("GrowthMediumRaw")
            .addCell()
            .addFluid()
            .setRGB(211, 141, 95)
            .setColor(Dyes.dyeOrange)
            .constructMaterial();
    }

    private static Materials loadGrowthMediumSterilized() {
        return new MaterialBuilder(609, TextureSet.SET_FLUID, "Growth Catalyst Medium")
            .setName("GrowthMediumSterilized")
            .addCell()
            .addFluid()
            .setRGB(222, 170, 135)
            .setColor(Dyes.dyeOrange)
            .constructMaterial();
    }

    private static void loadUnclassified05() {
        Materials.BioDiesel = loadBioDiesel();
        Materials.BisphenolA = loadBisphenolA();
        Materials.Butadiene = loadButadiene();
        Materials.Butane = loadButane();
        Materials.Butene = loadButene();
        Materials.CalciumAcetateSolution = loadCalciumAcetateSolution();
        Materials.CarbonMonoxide = loadCarbonMonoxide();
        Materials.Chloramine = loadChloramine();
        Materials.Chloroform = loadChloroform();
        Materials.Chloromethane = loadChloromethane();
        Materials.Cumene = loadCumene();
        Materials.Dichlorobenzene = loadDichlorobenzene();
        Materials.Dimethylamine = loadDimethylamine();
        Materials.Dimethyldichlorosilane = loadDimethyldichlorosilane();
        Materials.Dimethylhydrazine = loadDimethylhydrazine();
        Materials.DinitrogenTetroxide = loadDinitrogenTetroxide();
        Materials.Epichlorohydrin = loadEpichlorohydrin();
        Materials.Ethane = loadEthane();
        Materials.Ethenone = loadEthenone();
        Materials.Ethylene = loadEthylene();
        Materials.Glycerol = loadGlycerol();
        Materials.HydrochloricAcid = loadHydrochloricAcid();
        Materials.HydrofluoricAcid = loadHydrofluoricAcid();
        Materials.HypochlorousAcid = loadHypochlorousAcid();
        Materials.IronIIIChloride = loadIronIIIChloride();
        Materials.Isoprene = loadIsoprene();
        Materials.LifeEssence = loadLifeEssence();
        Materials.MetalMixture = loadMetalMixture();
        Materials.Methanol = loadMethanol();
        Materials.MethylAcetate = loadMethylAcetate();
        Materials.NitrationMixture = loadNitrationMixture();
        Materials.NitricAcid = loadNitricAcid();
        Materials.NitricOxide = loadNitricOxide();
        Materials.Phenol = loadPhenol();
        Materials.PhosphoricAcid = loadPhosphoricAcid();
        Materials.PhosphorousPentoxide = loadPhosphorousPentoxide();
        Materials.PolyphenyleneSulfide = loadPolyphenyleneSulfide();
        Materials.Polystyrene = loadPolystyrene();
        Materials.PolyvinylAcetate = loadPolyvinylAcetate();
        Materials.PolyvinylChloride = loadPolyvinylChloride();
        Materials.Propane = loadPropane();
        Materials.Propene = loadPropene();
        Materials.SaltWater = loadSaltWater();
        Materials.SodiumBisulfate = loadSodiumBisulfate();
        Materials.SodiumHydroxide = loadSodiumHydroxide();
        Materials.SodiumOxide = loadSodiumOxide();
        Materials.Styrene = loadStyrene();
        Materials.StyreneButadieneRubber = loadStyreneButadieneRubber();
        Materials.SulfurDioxide = loadSulfurDioxide();
        Materials.SulfurTrioxide = loadSulfurTrioxide();
        Materials.Tetrafluoroethylene = loadTetrafluoroethylene();
        Materials.Tetranitromethane = loadTetranitromethane();
        Materials.Toluene = loadToluene();
        Materials.VinylAcetate = loadVinylAcetate();
        Materials.VinylChloride = loadVinylChloride();
    }

    private static Materials loadBioDiesel() {
        return new MaterialBuilder(627, TextureSet.SET_FLUID, "Bio Diesel").addCell()
            .addFluid()
            .setRGB(255, 128, 0)
            .setColor(Dyes.dyeOrange)
            .setFuelType(MaterialBuilder.DIESEL)
            .setFuelPower(320)
            .constructMaterial();
    }

    private static Materials loadBisphenolA() {
        return new MaterialBuilder(669, TextureSet.SET_FLUID, "Bisphenol A").addCell()
            .setRGB(212, 170, 0)
            .setColor(Dyes.dyeBrown)
            .setMaterialList(
                new MaterialStack(Carbon, 15),
                new MaterialStack(Hydrogen, 16),
                new MaterialStack(Oxygen, 2))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadButadiene() {
        return new MaterialBuilder(646, TextureSet.SET_FLUID, "Butadiene").addCell()
            .addGas()
            .setRGB(232, 105, 0)
            .setColor(Dyes.dyeOrange)
            .setFuelType(MaterialBuilder.GAS)
            .setFuelPower(206)
            .setMaterialList(new MaterialStack(Carbon, 4), new MaterialStack(Hydrogen, 6))
            .addElectrolyzerRecipe()
            .setCanBeCracked(true)
            .constructMaterial();
    }

    private static Materials loadButane() {
        return new MaterialBuilder(644, TextureSet.SET_FLUID, "Butane").addCell()
            .addGas()
            .setRGB(182, 55, 30)
            .setColor(Dyes.dyeOrange)
            .setFuelType(MaterialBuilder.GAS)
            .setFuelPower(296)
            .setMaterialList(new MaterialStack(Carbon, 4), new MaterialStack(Hydrogen, 10))
            .addElectrolyzerRecipe()
            .setCanBeCracked(true)
            .constructMaterial();
    }

    private static Materials loadButene() {
        return new MaterialBuilder(645, TextureSet.SET_FLUID, "Butene").addCell()
            .addGas()
            .setRGB(207, 80, 5)
            .setColor(Dyes.dyeOrange)
            .setFuelType(MaterialBuilder.GAS)
            .setFuelPower(256)
            .setMaterialList(new MaterialStack(Carbon, 4), new MaterialStack(Hydrogen, 8))
            .addElectrolyzerRecipe()
            .setCanBeCracked(true)
            .constructMaterial();
    }

    private static Materials loadCalciumAcetateSolution() {
        return new MaterialBuilder(671, TextureSet.SET_RUBY, "Calcium Acetate Solution").addCell()
            .addFluid()
            .setRGB(220, 200, 180)
            .setColor(Dyes.dyeCyan)
            .setMaterialList(
                new MaterialStack(Calcium, 1),
                new MaterialStack(Carbon, 4),
                new MaterialStack(Oxygen, 4),
                new MaterialStack(Hydrogen, 6))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadCarbonMonoxide() {
        return new MaterialBuilder(674, TextureSet.SET_FLUID, "Carbon Monoxide").addCell()
            .addGas()
            .setRGB(14, 72, 128)
            .setColor(Dyes.dyeBrown)
            .setFuelType(MaterialBuilder.GAS)
            .setFuelPower(24)
            .setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 1))
            .constructMaterial();
    }

    private static Materials loadChloramine() {
        return new MaterialBuilder(655, TextureSet.SET_FLUID, "Chloramine").addCell()
            .addFluid()
            .setRGB(63, 159, 128)
            .setColor(Dyes.dyeCyan)
            .setMaterialList(
                new MaterialStack(Nitrogen, 1),
                new MaterialStack(Hydrogen, 2),
                new MaterialStack(Chlorine, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadChloroform() {
        return new MaterialBuilder(668, TextureSet.SET_FLUID, "Chloroform").addCell()
            .addFluid()
            .setRGB(137, 44, 160)
            .setColor(Dyes.dyePurple)
            .setMaterialList(
                new MaterialStack(Carbon, 1),
                new MaterialStack(Hydrogen, 1),
                new MaterialStack(Chlorine, 3))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadChloromethane() {
        return new MaterialBuilder(664, TextureSet.SET_FLUID, "Chloromethane").addCell()
            .addGas()
            .setRGB(200, 44, 160)
            .setColor(Dyes.dyeMagenta)
            .setMaterialList(
                new MaterialStack(Carbon, 1),
                new MaterialStack(Hydrogen, 3),
                new MaterialStack(Chlorine, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadCumene() {
        return new MaterialBuilder(688, TextureSet.SET_FLUID, "Isopropylbenzene").addCell()
            .addFluid()
            .setRGB(85, 34, 0)
            .setColor(Dyes.dyeBrown)
            .setMaterialList(new MaterialStack(Carbon, 9), new MaterialStack(Hydrogen, 12))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadDichlorobenzene() {
        return new MaterialBuilder(632, TextureSet.SET_FLUID, "Dichlorobenzene").addCell()
            .addFluid()
            .setRGB(0, 68, 85)
            .setColor(Dyes.dyeBlue)
            .setMaterialList(
                new MaterialStack(Carbon, 6),
                new MaterialStack(Hydrogen, 4),
                new MaterialStack(Chlorine, 2))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadDimethylamine() {
        return new MaterialBuilder(656, TextureSet.SET_FLUID, "Dimethylamine").addCell()
            .addGas()
            .setRGB(85, 68, 105)
            .setColor(Dyes.dyeGray)
            .setMaterialList(
                new MaterialStack(Carbon, 2),
                new MaterialStack(Hydrogen, 7),
                new MaterialStack(Nitrogen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadDimethyldichlorosilane() {
        return new MaterialBuilder(663, TextureSet.SET_FLUID, "Dimethyldichlorosilane").addCell()
            .addFluid()
            .setRGB(68, 22, 80)
            .setColor(Dyes.dyePurple)
            .setMaterialList(
                new MaterialStack(Carbon, 2),
                new MaterialStack(Hydrogen, 6),
                new MaterialStack(Chlorine, 2),
                new MaterialStack(Silicon, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadDimethylhydrazine() {
        return new MaterialBuilder(654, TextureSet.SET_FLUID, "1,1-Dimethylhydrazine").addCell()
            .addFluid()
            .setRGB(0, 0, 85)
            .setColor(Dyes.dyeBlue)
            .setMaterialList(
                new MaterialStack(Carbon, 2),
                new MaterialStack(Hydrogen, 8),
                new MaterialStack(Nitrogen, 2))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadDinitrogenTetroxide() {
        return new MaterialBuilder(657, TextureSet.SET_FLUID, "Dinitrogen Tetroxide").addCell()
            .addGas()
            .setRGB(0, 65, 132)
            .setColor(Dyes.dyeBlue)
            .setMaterialList(new MaterialStack(Nitrogen, 2), new MaterialStack(Oxygen, 4))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadEpichlorohydrin() {
        return new MaterialBuilder(648, TextureSet.SET_FLUID, "Epichlorohydrin").addCell()
            .setRGB(80, 29, 5)
            .setColor(Dyes.dyeBrown)
            .setMaterialList(
                new MaterialStack(Carbon, 3),
                new MaterialStack(Hydrogen, 5),
                new MaterialStack(Chlorine, 1),
                new MaterialStack(Oxygen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadEthane() {
        return new MaterialBuilder(642, TextureSet.SET_FLUID, "Ethane").addCell()
            .addGas()
            .setRGB(200, 200, 255)
            .setColor(Dyes.dyeLightBlue)
            .setFuelType(MaterialBuilder.GAS)
            .setFuelPower(168)
            .setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 6))
            .addElectrolyzerRecipe()
            .setCanBeCracked(true)
            .constructMaterial();
    }

    private static Materials loadEthenone() {
        return new MaterialBuilder(641, TextureSet.SET_FLUID, "Ethenone").addCell()
            .addGas()
            .setRGB(20, 20, 70)
            .setColor(Dyes.dyeBlack)
            .setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadEthylene() {
        return new MaterialBuilder(677, TextureSet.SET_FLUID, "Ethylene").addCell()
            .addGas()
            .setRGB(225, 225, 225)
            .setColor(Dyes.dyeWhite)
            .setFuelType(MaterialBuilder.GAS)
            .setFuelPower(128)
            .setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 4))
            .addElectrolyzerRecipe()
            .setCanBeCracked(true)
            .constructMaterial();
    }

    private static Materials loadGlycerol() {
        return new MaterialBuilder(629, TextureSet.SET_FLUID, "Glycerol").addCell()
            .addFluid()
            .setRGB(135, 222, 135)
            .setColor(Dyes.dyeLime)
            .setFuelType(MaterialBuilder.SEMIFLUID)
            .setFuelPower(164)
            .setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 8), new MaterialStack(Oxygen, 3))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadHydrochloricAcid() {
        return new MaterialBuilder(683, TextureSet.SET_FLUID, "Hydrochloric Acid").setName("HydrochloricAcid_GT5U")
            .addCell()
            .addFluid()
            .setRGB(183, 200, 196)
            .setColor(Dyes.dyeLightGray)
            .setMaterialList(new MaterialStack(Hydrogen, 1), new MaterialStack(Chlorine, 1))
            .constructMaterial();
    }

    private static Materials loadHydrofluoricAcid() {
        return new MaterialBuilder(667, TextureSet.SET_FLUID, "Hydrofluoric Acid").setName("HydrofluoricAcid_GT5U")
            .addCell()
            .addFluid()
            .setRGB(0, 136, 170)
            .setColor(Dyes.dyeLightBlue)
            .setMaterialList(new MaterialStack(Hydrogen, 1), new MaterialStack(Fluorine, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadHypochlorousAcid() {
        return new MaterialBuilder(684, TextureSet.SET_FLUID, "Hypochlorous Acid").addCell()
            .addFluid()
            .setRGB(111, 138, 145)
            .setColor(Dyes.dyeGray)
            .setMaterialList(
                new MaterialStack(Hydrogen, 1),
                new MaterialStack(Chlorine, 1),
                new MaterialStack(Oxygen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadIronIIIChloride() {
        return new MaterialBuilder(693, TextureSet.SET_FLUID, "Iron III Chloride").setName("IronIIIChloride")
            .addCell()
            .addFluid()
            .setRGB(22, 21, 14)
            .setColor(Dyes.dyeBlack)
            .setMaterialList(new MaterialStack(Iron, 1), new MaterialStack(Chlorine, 3))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadIsoprene() {
        return new MaterialBuilder(638, TextureSet.SET_FLUID, "Isoprene").addCell()
            .addFluid()
            .setRGB(20, 20, 20)
            .setColor(Dyes.dyeBlack)
            .setMaterialList(new MaterialStack(Carbon, 5), new MaterialStack(Hydrogen, 8))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadLifeEssence() {
        return new MaterialBuilder(694, TextureSet.SET_FLUID, "Life").setName("lifeessence")
            .addCell()
            .addFluid()
            .setFuelPower(100)
            .setFuelType(5)
            .setRGB(110, 3, 3)
            .setColor(Dyes.dyeRed)
            .setMaterialList()
            .constructMaterial();
    }

    private static Materials loadMetalMixture() {
        return new MaterialBuilder(676, TextureSet.SET_METALLIC, "Metal Mixture").addDustItems()
            .setRGB(80, 45, 22)
            .setColor(Dyes.dyeBrown)
            .constructMaterial();
    }

    private static Materials loadMethanol() {
        return new MaterialBuilder(673, TextureSet.SET_FLUID, "Methanol").addCell()
            .addFluid()
            .setRGB(170, 136, 0)
            .setColor(Dyes.dyeBrown)
            .setFuelPower(84)
            .setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(Hydrogen, 4), new MaterialStack(Oxygen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadMethylAcetate() {
        return new MaterialBuilder(681, TextureSet.SET_FLUID, "Methyl Acetate").addCell()
            .addFluid()
            .setRGB(238, 198, 175)
            .setColor(Dyes.dyeOrange)
            .setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 2))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadNitrationMixture() {
        return new MaterialBuilder(628, TextureSet.SET_FLUID, "Nitration Mixture").addCell()
            .setRGB(230, 226, 171)
            .setColor(Dyes.dyeBrown)
            .constructMaterial();
    }

    private static Materials loadNitricAcid() {
        return new MaterialBuilder(653, TextureSet.SET_FLUID, "Nitric Acid").addCell()
            .addFluid()
            .setRGB(230, 226, 171)
            .setMaterialList(
                new MaterialStack(Hydrogen, 1),
                new MaterialStack(Nitrogen, 1),
                new MaterialStack(Oxygen, 3))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadNitricOxide() {
        return new MaterialBuilder(658, TextureSet.SET_FLUID, "Nitric Oxide").addCell()
            .addGas()
            .setRGB(125, 200, 240)
            .setColor(Dyes.dyeCyan)
            .setMaterialList(new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadPhenol() {
        return new MaterialBuilder(687, TextureSet.SET_FLUID, "Phenol").addCell()
            .addFluid()
            .setRGB(120, 68, 33)
            .setColor(Dyes.dyeBrown)
            .setFuelType(MaterialBuilder.GAS)
            .setFuelPower(288)
            .setMaterialList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadPhosphoricAcid() {
        return new MaterialBuilder(689, TextureSet.SET_FLUID, "Phosphoric Acid").setName("PhosphoricAcid_GT5U")
            .addCell()
            .addFluid()
            .setRGB(220, 220, 0)
            .setColor(Dyes.dyeYellow)
            .setMaterialList(
                new MaterialStack(Hydrogen, 3),
                new MaterialStack(Phosphorus, 1),
                new MaterialStack(Oxygen, 4))
            .constructMaterial();
    }

    private static Materials loadPhosphorousPentoxide() {
        return new MaterialBuilder(665, TextureSet.SET_FLUID, "Phosphorous Pentoxide").addCell()
            .addDustItems()
            .setRGB(220, 220, 0)
            .setColor(Dyes.dyeYellow)
            .setMaterialList(new MaterialStack(Phosphorus, 4), new MaterialStack(Oxygen, 10))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadPolyphenyleneSulfide() {
        return new MaterialBuilder(631, TextureSet.SET_DULL, "Polyphenylene Sulfide").addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setToolSpeed(3.0f)
            .setDurability(32)
            .setToolQuality(1)
            .setRGB(170, 136, 0)
            .setColor(Dyes.dyeBrown)
            .setMaterialList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 4), new MaterialStack(Sulfur, 1))
            .constructMaterial();
    }

    private static Materials loadPolystyrene() {
        return new MaterialBuilder(636, TextureSet.SET_DULL, "Polystyrene").addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setToolSpeed(3.0f)
            .setDurability(32)
            .setToolQuality(1)
            .setRGB(190, 180, 170)
            .setColor(Dyes.dyeLightGray)
            .setMaterialList(new MaterialStack(Carbon, 8), new MaterialStack(Hydrogen, 8))
            .constructMaterial();
    }

    private static Materials loadPolyvinylAcetate() {
        return new MaterialBuilder(680, TextureSet.SET_FLUID, "Polyvinyl Acetate").addCell()
            .addFluid()
            .setRGB(255, 153, 85)
            .setColor(Dyes.dyeOrange)
            .setMaterialList(new MaterialStack(Carbon, 4), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 2))
            .constructMaterial();
    }

    private static Materials loadPolyvinylChloride() {
        return new MaterialBuilder(649, TextureSet.SET_DULL, "Polyvinyl Chloride").addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setToolSpeed(3.0f)
            .setDurability(32)
            .setToolQuality(1)
            .setRGB(215, 230, 230)
            .setColor(Dyes.dyeLightGray)
            .setMaterialList(
                new MaterialStack(Carbon, 2),
                new MaterialStack(Hydrogen, 3),
                new MaterialStack(Chlorine, 1))
            .constructMaterial();
    }

    private static Materials loadPropane() {
        return new MaterialBuilder(643, TextureSet.SET_FLUID, "Propane").addCell()
            .addGas()
            .setRGB(250, 226, 80)
            .setColor(Dyes.dyeYellow)
            .setFuelType(MaterialBuilder.GAS)
            .setFuelPower(232)
            .setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 8))
            .addElectrolyzerRecipe()
            .setCanBeCracked(true)
            .constructMaterial();
    }

    private static Materials loadPropene() {
        return new MaterialBuilder(678, TextureSet.SET_FLUID, "Propene").addCell()
            .addGas()
            .setRGB(255, 221, 85)
            .setColor(Dyes.dyeYellow)
            .setFuelType(MaterialBuilder.GAS)
            .setFuelPower(192)
            .setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 6))
            .addElectrolyzerRecipe()
            .setCanBeCracked(true)
            .constructMaterial();
    }

    private static Materials loadSaltWater() {
        return new MaterialBuilder(692, TextureSet.SET_FLUID, "Salt Water").addCell()
            .addFluid()
            .setRGB(0, 0, 200)
            .setColor(Dyes.dyeBlue)
            .constructMaterial();
    }

    private static Materials loadSodiumBisulfate() {
        return new MaterialBuilder(630, TextureSet.SET_FLUID, "Sodium Bisulfate").addDustItems()
            .setRGB(0, 68, 85)
            .setColor(Dyes.dyeBlue)
            .setMaterialList(
                new MaterialStack(Sodium, 1),
                new MaterialStack(Hydrogen, 1),
                new MaterialStack(Sulfur, 1),
                new MaterialStack(Oxygen, 4))
            .constructMaterial();
    }

    private static Materials loadSodiumHydroxide() {
        return new MaterialBuilder(685, TextureSet.SET_DULL, "Sodium Hydroxide").setName("SodiumHydroxide_GT5U")
            .addDustItems()
            .setRGB(0, 51, 128)
            .setColor(Dyes.dyeBlue)
            .setMaterialList(new MaterialStack(Sodium, 1), new MaterialStack(Oxygen, 1), new MaterialStack(Hydrogen, 1))
            .constructMaterial();
    }

    private static Materials loadSodiumOxide() {
        return new MaterialBuilder(744, TextureSet.SET_DULL, "Sodium Oxide").setName("SodiumOxide")
            .addDustItems()
            .setRGB(255, 255, 235)
            .setColor(Dyes.dyeWhite)
            .setMaterialList(new MaterialStack(Sodium, 2), new MaterialStack(Oxygen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadStyrene() {
        return new MaterialBuilder(637, TextureSet.SET_FLUID, "Styrene").addCell()
            .addFluid()
            .setRGB(210, 200, 190)
            .setColor(Dyes.dyeBlack)
            .setMaterialList(new MaterialStack(Carbon, 8), new MaterialStack(Hydrogen, 8))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadStyreneButadieneRubber() {
        return new MaterialBuilder(635, TextureSet.SET_SHINY, "Styrene-Butadiene Rubber").addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setToolSpeed(3.0f)
            .setDurability(128)
            .setToolQuality(1)
            .setRGB(33, 26, 24)
            .setColor(Dyes.dyeBlack)
            .setMaterialList(new MaterialStack(Styrene, 1), new MaterialStack(Butadiene, 3))
            .constructMaterial();
    }

    private static Materials loadSulfurDioxide() {
        return new MaterialBuilder(651, TextureSet.SET_FLUID, "Sulfur Dioxide").addCell()
            .addGas()
            .setRGB(200, 200, 25)
            .setColor(Dyes.dyeYellow)
            .setMaterialList(new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 2))
            .constructMaterial();
    }

    private static Materials loadSulfurTrioxide() {
        return new MaterialBuilder(652, TextureSet.SET_FLUID, "Sulfur Trioxide").addCell()
            .addGas()
            .setGasTemperature(344)
            .setRGB(160, 160, 20)
            .setColor(Dyes.dyeYellow)
            .setMaterialList(new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 3))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadTetrafluoroethylene() {
        return new MaterialBuilder(666, TextureSet.SET_FLUID, "Tetrafluoroethylene").addCell()
            .addGas()
            .setRGB(125, 125, 125)
            .setColor(Dyes.dyeGray)
            .setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Fluorine, 4))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadTetranitromethane() {
        return new MaterialBuilder(639, TextureSet.SET_FLUID, "Tetranitromethane").addCell()
            .addFluid()
            .setRGB(15, 40, 40)
            .setColor(Dyes.dyeBlack)
            .setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(Nitrogen, 4), new MaterialStack(Oxygen, 8))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadToluene() {
        return new MaterialBuilder(647, TextureSet.SET_FLUID, "Toluene").addCell()
            .setRGB(80, 29, 5)
            .setColor(Dyes.dyeBrown)
            .setFuelType(MaterialBuilder.GAS)
            .setFuelPower(328)
            .setMaterialList(new MaterialStack(Carbon, 7), new MaterialStack(Hydrogen, 8))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadVinylAcetate() {
        return new MaterialBuilder(679, TextureSet.SET_FLUID, "Vinyl Acetate").addCell()
            .addFluid()
            .setRGB(255, 179, 128)
            .setColor(Dyes.dyeOrange)
            .setMaterialList(new MaterialStack(Carbon, 4), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 2))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadVinylChloride() {
        return new MaterialBuilder(650, TextureSet.SET_FLUID, "Vinyl Chloride").addCell()
            .addGas()
            .setRGB(225, 240, 240)
            .setColor(Dyes.dyeLightGray)
            .setMaterialList(
                new MaterialStack(Carbon, 2),
                new MaterialStack(Hydrogen, 3),
                new MaterialStack(Chlorine, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static void loadRoastedOres() {
        Materials.RoastedAntimony = loadRoastedAntimony();
        Materials.RoastedArsenic = loadRoastedArsenic();
        Materials.RoastedCobalt = loadRoastedCobalt();
        Materials.RoastedCopper = loadRoastedCopper();
        Materials.RoastedIron = loadRoastedIron();
        Materials.RoastedLead = loadRoastedLead();
        Materials.RoastedNickel = loadRoastedNickel();
        Materials.RoastedZinc = loadRoastedZinc();
    }

    private static Materials loadRoastedAntimony() {
        return new MaterialBuilder(547, TextureSet.SET_DULL, "Roasted Antimony").setName("RoastedAntimony")
            .addDustItems()
            .setRGB(196, 178, 194)
            .constructMaterial();
    }

    private static Materials loadRoastedArsenic() {
        return new MaterialBuilder(552, TextureSet.SET_SHINY, "Roasted Arsenic").setName("RoastedArsenic")
            .addDustItems()
            .setRGB(240, 240, 240)
            .constructMaterial();
    }

    private static Materials loadRoastedCobalt() {
        return new MaterialBuilder(551, TextureSet.SET_METALLIC, "Roasted Cobalt").setName("RoastedCobalt")
            .addDustItems()
            .setRGB(8, 64, 9)
            .constructMaterial();
    }

    private static Materials loadRoastedCopper() {
        return new MaterialBuilder(546, TextureSet.SET_DULL, "Roasted Copper").setName("RoastedCopper")
            .addDustItems()
            .setRGB(77, 18, 18)
            .constructMaterial();
    }

    private static Materials loadRoastedIron() {
        return new MaterialBuilder(548, TextureSet.SET_DULL, "Roasted Iron").setName("RoastedIron")
            .addDustItems()
            .setRGB(148, 98, 98)
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadRoastedLead() {
        return new MaterialBuilder(553, TextureSet.SET_SHINY, "Roasted Lead").setName("RoastedLead")
            .addDustItems()
            .setRGB(168, 149, 43)
            .constructMaterial();
    }

    private static Materials loadRoastedNickel() {
        return new MaterialBuilder(549, TextureSet.SET_METALLIC, "Roasted Nickel").setName("RoastedNickel")
            .addDustItems()
            .setRGB(70, 140, 45)
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadRoastedZinc() {
        return new MaterialBuilder(550, TextureSet.SET_DULL, "Roasted Zinc").setName("RoastedZinc")
            .addDustItems()
            .setRGB(209, 209, 209)
            .constructMaterial();
    }

    private static void loadSiliconLine() {
        Materials.AluminiumFluoride = loadAluminiumFluoride();
        Materials.CalciumDisilicide = loadCalciumDisilicide();
        Materials.Calciumhydride = loadCalciumhydride();
        Materials.Dichlorosilane = loadDichlorosilane();
        Materials.Hexachlorodisilane = loadHexachlorodisilane();
        Materials.Silane = loadSilane();
        Materials.SiliconSG = loadSiliconSG();
        Materials.SiliconTetrachloride = loadSiliconTetrachloride();
        Materials.SiliconTetrafluoride = loadSiliconTetrafluoride();
        Materials.Trichlorosilane = loadTrichlorosilane();
    }

    private static Materials loadAluminiumFluoride() {
        return new MaterialBuilder().setName("Aluminiumfluoride")
            .setDefaultLocalName("Aluminium Fluoride")
            .setMetaItemSubID(969)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .addDustItems()
            .addOreItems()
            .setMeltingPoint(1533)
            .addElectrolyzerRecipe()
            // ALF3
            .addMaterial(Materials.Aluminium, 1)
            .addMaterial(Materials.Fluorine, 3)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.ORDO, 1)
            .constructMaterial();
    }

    private static Materials loadCalciumDisilicide() {
        return new MaterialBuilder().setName("CalciumDisilicide")
            .setDefaultLocalName("Calcium Disilicide")
            .setMetaItemSubID(971)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setRGB(0xb4b4b4)
            .addDustItems()
            .addOreItems()
            .setMeltingPoint(1313)
            .addElectrolyzerRecipe()
            // CaSi2
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Silicon, 2)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.ORDO, 1)
            .constructMaterial();
    }

    private static Materials loadCalciumhydride() {
        return new MaterialBuilder().setName("CalciumHydride")
            .setDefaultLocalName("Calcium Hydride")
            .setMetaItemSubID(797)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setRGB(0xdcdcdc)
            .addDustItems()
            .addOreItems()
            .setMeltingPoint(1089)
            .addElectrolyzerRecipe()
            // CaH2
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Hydrogen, 2)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.ORDO, 1)
            .constructMaterial();
    }

    private static Materials loadDichlorosilane() {
        return new MaterialBuilder(799, TextureSet.SET_FLUID, "Dichlorosilane").setName("Dichlorosilane")
            .addCell()
            .addGas()
            .setTransparent(true)
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(151)
            .setExtraData(1)
            // SIH2CL2
            .setMaterialList(
                new MaterialStack(Silicon, 1),
                new MaterialStack(Hydrogen, 2),
                new MaterialStack(Chlorine, 2))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1)))
            .constructMaterial();
    }

    private static Materials loadHexachlorodisilane() {
        return new MaterialBuilder(973, TextureSet.SET_FLUID, "Hexachlorodisilane").setName("Hexachlorodisilane")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(272)
            .setExtraData(1)
            // SI2CL6
            .setMaterialList(new MaterialStack(Silicon, 2), new MaterialStack(Chlorine, 6))
            .setAspects(Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 1)))
            .constructMaterial();
    }

    private static Materials loadSilane() {
        return new MaterialBuilder(798, TextureSet.SET_FLUID, "Silane").setName("Silane")
            .addCell()
            .addGas()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(88)
            // SIH4
            .setMaterialList(new MaterialStack(Silicon, 1), new MaterialStack(Hydrogen, 4))
            .setAspects(Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 1)))
            .constructMaterial();
    }

    private static Materials loadSiliconSG() {
        return new MaterialBuilder().setName("SiliconSolarGrade")
            .setDefaultLocalName("Silicon Solar Grade (Poly SI)")
            .setMetaItemSubID(856)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x505064)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .setMeltingPoint(2273)
            .setBlastFurnaceTemp(2273)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addAspect(TCAspects.METALLUM, 4)
            .addAspect(TCAspects.TENEBRAE, 2)
            .constructMaterial();
    }

    private static Materials loadSiliconTetrachloride() {
        return new MaterialBuilder(968, TextureSet.SET_FLUID, "Silicon Tetrachloride").setName("SiliconTetrachloride")
            .addCell()
            .addFluid()
            .setRGB(220, 220, 220)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(204)
            // SICL4
            .setMaterialList(new MaterialStack(Silicon, 1), new MaterialStack(Chlorine, 4))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1)))
            .constructMaterial();
    }

    private static Materials loadSiliconTetrafluoride() {
        return new MaterialBuilder(967, TextureSet.SET_FLUID, "Silicon Tetrafluoride").setName("SiliconTetrafluoride")
            .addCell()
            .addGas()
            .setTransparent(true)
            .setRGB(200, 200, 200)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(178)
            // SIF4
            .setMaterialList(new MaterialStack(Silicon, 1), new MaterialStack(Fluorine, 4))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1)))
            .constructMaterial();
    }

    private static Materials loadTrichlorosilane() {
        return new MaterialBuilder(972, TextureSet.SET_FLUID, "Trichlorosilane").setName("Trichlorosilane")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMeltingPoint(139)
            // HSICL3
            .setMaterialList(
                new MaterialStack(Hydrogen, 1),
                new MaterialStack(Silicon, 1),
                new MaterialStack(Chlorine, 3))
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.AQUA, 1),
                    new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1)))
            .constructMaterial();
    }

    private static void loadUnclassified06() {
        Materials.GalliumArsenide = loadGalliumArsenide();
        Materials.IndiumGalliumPhosphide = loadIndiumGalliumPhosphide();
        Materials.SolderingAlloy = loadSolderingAlloy();
        Materials.Spessartine = loadSpessartine();
        Materials.Sphalerite = loadSphalerite();
        Materials.StainlessSteel = loadStainlessSteel();
        Materials.Steel = loadSteel();
        Materials.Stibnite = loadStibnite();
        Materials.SulfuricAcid = loadSulfuricAcid();
        Materials.Tanzanite = loadTanzanite();
        Materials.Tetrahedrite = loadTetrahedrite();
        Materials.TinAlloy = loadTinAlloy();
        Materials.Topaz = loadTopaz();
        Materials.Tungstate = loadTungstate();
        Materials.Ultimet = loadUltimet();
        Materials.Uraninite = loadeUraninite();
        Materials.Uvarovite = loadUvarovite();
        Materials.VanadiumGallium = loadVanadiumGallium();
        Materials.Wood = loadWood();
        Materials.WroughtIron = loadWroughtIron();
        Materials.Wulfenite = loadWulfenite();
        Materials.YellowLimonite = loadYellowLimonite();
        Materials.YttriumBariumCuprate = loadYttriumBariumCuprate();
    }

    private static Materials loadGalliumArsenide() {
        return new MaterialBuilder().setName("GalliumArsenide")
            .setDefaultLocalName("Gallium Arsenide")
            .setMetaItemSubID(980)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setRGB(0xa0a0a0)
            .addDustItems()
            .addMetalItems()
            .setBlastFurnaceTemp(1200)
            .setBlastFurnaceRequired(true)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Arsenic, 1)
            .addMaterial(Materials.Gallium, 1)
            .constructMaterial();
    }

    private static Materials loadIndiumGalliumPhosphide() {
        return new MaterialBuilder().setName("IndiumGalliumPhosphide")
            .setDefaultLocalName("Indium Gallium Phosphide")
            .setMetaItemSubID(981)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightGray)
            .setRGB(0xa08cbe)
            .addDustItems()
            .addMetalItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Indium, 1)
            .addMaterial(Materials.Gallium, 1)
            .addMaterial(Materials.Phosphorus, 1)
            .constructMaterial();
    }

    private static Materials loadSolderingAlloy() {
        return new MaterialBuilder().setName("SolderingAlloy")
            .setDefaultLocalName("Soldering Alloy")
            .setMetaItemSubID(314)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xdcdce6)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(400)
            .setBlastFurnaceTemp(400)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Tin, 9)
            .addMaterial(Materials.Antimony, 1)
            .constructMaterial();
    }

    private static Materials loadSpessartine() {
        return new MaterialBuilder().setName("Spessartine")
            .setDefaultLocalName("Spessartine")
            .setMetaItemSubID(838)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeRed)
            .setRGB(0xff6464)
            .addDustItems()
            .addOreItems()
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Manganese, 3)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 12)
            .constructMaterial();
    }

    private static Materials loadSphalerite() {
        return new MaterialBuilder().setName("Sphalerite")
            .setDefaultLocalName("Sphalerite")
            .setMetaItemSubID(839)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .addDustItems()
            .addOreItems()
            .addMaterial(Materials.Zinc, 1)
            .addMaterial(Materials.Sulfur, 1)
            .constructMaterial();
    }

    private static Materials loadStainlessSteel() {
        return new MaterialBuilder().setName("StainlessSteel")
            .setDefaultLocalName("Stainless Steel")
            .setMetaItemSubID(306)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xc8c8dc)
            .setToolSpeed(7.0f)
            .setDurability(480)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1700)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 6)
            .addMaterial(Materials.Chrome, 1)
            .addMaterial(Materials.Manganese, 1)
            .addMaterial(Materials.Nickel, 1)
            .constructMaterial();
    }

    private static Materials loadSteel() {
        return new MaterialBuilder().setName("Steel")
            .setDefaultLocalName("Steel")
            .setMetaItemSubID(305)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setRGB(0x808080)
            .setToolSpeed(6.0f)
            .setDurability(512)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1811)
            .setBlastFurnaceTemp(1000)
            .setBlastFurnaceRequired(true)
            .setDensityMultiplier(51)
            .setDensityDivider(50)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 50)
            .addMaterial(Materials.Carbon, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ORDO, 1)
            .constructMaterial();
    }

    private static Materials loadStibnite() {
        return new MaterialBuilder().setName("Stibnite")
            .setDefaultLocalName("Stibnite")
            .setMetaItemSubID(945)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setRGB(0x464646)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Antimony, 2)
            .addMaterial(Materials.Sulfur, 3)
            .constructMaterial();
    }

    private static Materials loadSulfuricAcid() {
        return new MaterialBuilder().setName("SulfuricAcid")
            .setDefaultLocalName("Sulfuric Acid")
            .setMetaItemSubID(720)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xff8000)
            .addCell()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Sulfur, 1)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadTanzanite() {
        return new MaterialBuilder().setName("Tanzanite")
            .setDefaultLocalName("Tanzanite")
            .setMetaItemSubID(508)
            .setIconSet(TextureSet.SET_GEM_VERTICAL)
            .setColor(Dyes.dyePurple)
            .setARGB(0x7f4000c8)
            .setToolSpeed(7.0f)
            .setDurability(256)
            .setToolQuality(2)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addMaterial(Materials.Calcium, 2)
            .addMaterial(Materials.Aluminium, 3)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Oxygen, 13)
            .addAspect(TCAspects.LUCRUM, 5)
            .addAspect(TCAspects.VITREUS, 3)
            .constructMaterial();
    }

    private static Materials loadTetrahedrite() {
        return new MaterialBuilder().setName("Tetrahedrite")
            .setDefaultLocalName("Tetrahedrite")
            .setMetaItemSubID(840)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeRed)
            .setRGB(0xc82000)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            // Cu3SbS3 + x(Fe,Zn)6Sb2S9
            .addMaterial(Materials.Copper, 3)
            .addMaterial(Materials.Antimony, 1)
            .addMaterial(Materials.Sulfur, 3)
            .addMaterial(Materials.Iron, 1)
            .constructMaterial();
    }

    private static Materials loadTinAlloy() {
        return new MaterialBuilder().setName("TinAlloy")
            .setDefaultLocalName("Tin Alloy")
            .setMetaItemSubID(363)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xc8c8c8)
            .setToolSpeed(6.5f)
            .setDurability(96)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Tin, 1)
            .addMaterial(Materials.Iron, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.INSTRUMENTUM, 1)
            .constructMaterial();
    }

    private static Materials loadTopaz() {
        return new MaterialBuilder().setName("Topaz")
            .setDefaultLocalName("Topaz")
            .setMetaItemSubID(507)
            .setIconSet(TextureSet.SET_GEM_HORIZONTAL)
            .setColor(Dyes.dyeOrange)
            .setARGB(0x7fff8000)
            .setToolSpeed(7.0f)
            .setDurability(256)
            .setToolQuality(3)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Fluorine, 2)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 6)
            .addAspect(TCAspects.LUCRUM, 6)
            .addAspect(TCAspects.VITREUS, 4)
            .constructMaterial();
    }

    private static Materials loadTungstate() {
        return new MaterialBuilder().setName("Tungstate")
            .setDefaultLocalName("Tungstate")
            .setMetaItemSubID(841)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x373223)
            .addDustItems()
            .addOreItems()
            .setMeltingPoint(2500)
            .setBlastFurnaceTemp(2500)
            .setBlastFurnaceRequired(true)
            .addMaterial(Materials.Tungsten, 1)
            .addMaterial(Materials.Lithium, 2)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadUltimet() {
        return new MaterialBuilder().setName("Ultimet")
            .setDefaultLocalName("Ultimet")
            .setMetaItemSubID(344)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightBlue)
            .setRGB(0xb4b4e6)
            .setToolSpeed(9.0f)
            .setDurability(2048)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2700)
            .setBlastFurnaceTemp(2700)
            .setBlastFurnaceRequired(true)
            .addElectrolyzerRecipe()
            // 54% Cobalt, 26% Chromium, 9% Nickel, 5% Molybdenum, 3% Iron, 2% Tungsten, 0.8% Manganese, 0.3% Silicon,
            // 0.08% Nitrogen and 0.06% Carbon
            .addMaterial(Materials.Cobalt, 5)
            .addMaterial(Materials.Chrome, 2)
            .addMaterial(Materials.Nickel, 1)
            .addMaterial(Materials.Molybdenum, 1)
            .constructMaterial();
    }

    private static Materials loadeUraninite() {
        return new MaterialBuilder().setName("Uraninite")
            .setDefaultLocalName("Uraninite")
            .setMetaItemSubID(922)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLime)
            .setRGB(0x232323)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Uranium, 1)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadUvarovite() {
        return new MaterialBuilder().setName("Uvarovite")
            .setDefaultLocalName("Uvarovite")
            .setMetaItemSubID(842)
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeGreen)
            .setRGB(0xb4ffb4)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Calcium, 3)
            .addMaterial(Materials.Chrome, 2)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 12)
            .constructMaterial();
    }

    private static Materials loadVanadiumGallium() {
        return new MaterialBuilder().setName("VanadiumGallium")
            .setDefaultLocalName("Vanadium-Gallium")
            .setMetaItemSubID(357)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGray)
            .setRGB(0x80808c)
            .addDustItems()
            .addMetalItems()
            .addGearItems()
            .setMeltingPoint(4500)
            .setBlastFurnaceTemp(4500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Vanadium, 3)
            .addMaterial(Materials.Gallium, 1)
            .constructMaterial();
    }

    private static Materials loadWood() {
        return new MaterialBuilder().setName("Wood")
            .setDefaultLocalName("Wood")
            .setMetaItemSubID(809)
            .setIconSet(TextureSet.SET_WOOD)
            .setColor(Dyes.dyeBrown)
            .setRGB(0x643200)
            .setToolSpeed(2.0f)
            .setDurability(16)
            .setToolQuality(0)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Oxygen, 1)
            .addMaterial(Materials.Hydrogen, 1)
            .addAspect(TCAspects.ARBOR, 2)
            .constructMaterial();
    }

    private static Materials loadWroughtIron() {
        return new MaterialBuilder().setName("WroughtIron")
            .setDefaultLocalName("Wrought Iron")
            .setMetaItemSubID(304)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightGray)
            .setRGB(0xc8b4b4)
            .setToolSpeed(6.0f)
            .setDurability(384)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1811)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Iron, 1)
            .constructMaterial();
    }

    private static Materials loadWulfenite() {
        return new MaterialBuilder().setName("Wulfenite")
            .setDefaultLocalName("Wulfenite")
            .setMetaItemSubID(882)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xff8000)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Lead, 1)
            .addMaterial(Materials.Molybdenum, 1)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadYellowLimonite() {
        return new MaterialBuilder().setName("YellowLimonite")
            .setDefaultLocalName("Yellow Limonite")
            .setMetaItemSubID(931)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xc8c800)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            // FeO(OH) + a bit of Ni and Co
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Oxygen, 2)
            .constructMaterial();
    }

    private static Materials loadYttriumBariumCuprate() {
        return new MaterialBuilder().setName("YttriumBariumCuprate")
            .setDefaultLocalName("Yttrium Barium Cuprate")
            .setMetaItemSubID(358)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setRGB(0x504046)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(4500)
            .setBlastFurnaceTemp(4500)
            .setBlastFurnaceRequired(true)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addMaterial(Materials.Yttrium, 1)
            .addMaterial(Materials.Barium, 2)
            .addMaterial(Materials.Copper, 3)
            .addMaterial(Materials.Oxygen, 7)
            .constructMaterial();
    }

    private static void loadDegree2Compounds() {
        Materials.Aluminiumhydroxide = loadAluminiumHydroxide();
        Materials.Aluminiumoxide = loadAluminiumoxide();
        Materials.Alumite = loadAlumite();
        Materials.Alunite = loadAlunite();
        Materials.Amethyst = loadAmethyst();
        Materials.Apatite = loadApatite();
        Materials.Barite = loadBarite();
        Materials.Bastnasite = loadBastnasite();
        Materials.Bauxite = loadBauxite();
        Materials.Bentonite = loadBentonite();
        Materials.Biotite = loadBiotite();
        Materials.BismuthBronze = loadBismuthBronze();
        Materials.BlackBronze = loadBlackBronze();
        Materials.BlackSteel = loadBlackSteel();
        Materials.Blaze = loadBlaze();
        Materials.Borax = loadBorax();
        Materials.Chrysotile = loadChrysotile();
        Materials.Clay = loadClay();
        Materials.CobaltBrass = loadCobaltBrass();
        Materials.Concrete = loadConcrete();
        Materials.Cryolite = loadCryolite();
        Materials.Cryotheum = loadCryotheum();
        Materials.DamascusSteel = loadDamascusSteel();
        Materials.Diatomite = loadDiatomite();
        Materials.DilutedSulfuricAcid = loadDilutedSulfuricAcid();
        Materials.Dolomite = loadDolomite();
        Materials.EnderPearl = loadEnderPearl();
        Materials.EpoxidFiberReinforced = loadEpoxidFiberReinforced();
        Materials.Flint = loadFlint();
        Materials.FullersEarth = loadFullersEarth();
        Materials.GarnetRed = loadGarnetRed();
        Materials.GarnetYellow = loadGarnetYellow();
        Materials.Glass = loadGlass();
        Materials.Glauconite = loadGlauconite();
        Materials.GlauconiteSand = loadGlauconiteSand();
        Materials.GraniteBlack = loadGraniteBlack();
        Materials.GraniticMineralSand = loadGraniticMineralSand();
        Materials.Gypsum = loadGypsum();
        Materials.HydratedCoal = loadHydratedCoal();
        Materials.IronMagnetic = loadIronMagnetic();
        Materials.Jasper = loadJasper();
        Materials.Kaolinite = loadKaolinite();
        Materials.Knightmetal = loadKnightmetal();
        Materials.Kyanite = loadKyanite();
        Materials.Lapis = loadLapis();
        Materials.Lepidolite = loadLepidolite();
        Materials.Lignite = loadLignite();
        Materials.LiveRoot = loadLiveRoot();
        Materials.Malachite = loadMalachite();
        Materials.Manyullyn = loadManyullyn();
        Materials.Marble = loadMarble();
        Materials.Mica = loadMica();
        Materials.Mirabilite = loadMirabilite();
        Materials.Monazite = loadMonazite();
        Materials.NeodymiumMagnetic = loadNeodymiumMagnetic();
        Materials.Niter = loadNiter();
        Materials.NitroCoalFuel = loadNitroCoalFuel();
        Materials.NitroFuel = loadNitroFuel();
        Materials.Olivine = loadOlivine();
        Materials.Opal = loadOpal();
        Materials.Pentlandite = loadPentlandite();
        Materials.Perlite = loadPerlite();
        Materials.Pitchblende = loadPitchblende();
        Materials.Pollucite = loadPollucite();
        Materials.PotassiumFeldspar = loadPotassiumFeldspar();
        Materials.QuartzSand = loadQuartzSand();
        Materials.RawStyreneButadieneRubber = loadRawStyreneButadieneRubber();
        Materials.Realgar = loadRealgar();
        Materials.RedAlloy = loadRedAlloy();
        Materials.RedMud = loadRedMud();
        Materials.Redrock = loadRedrock();
        Materials.Redstone = loadRedstone();
        Materials.RoseGold = loadRoseGold();
        Materials.SamariumMagnetic = loadSamariumMagnetic();
        Materials.Soapstone = loadSoapstone();
        Materials.SodiumAluminate = loadSodiumAluminate();
        Materials.SodiumCarbonate = loadSodiumCarbonate();
        Materials.Spodumene = loadSpodumene();
        Materials.SteelMagnetic = loadSteelMagnetic();
        Materials.Steeleaf = loadSteeleaf();
        Materials.SterlingSilver = loadSterlingSilver();
        Materials.Sugar = loadSugar();
        Materials.Talc = loadTalc();
        Materials.Tantalite = loadTantalite();
        Materials.Thaumium = loadThaumium();
        Materials.TPV = loadTPV();
        Materials.TricalciumPhosphate = loadTricalciumPhosphate();
        Materials.Trona = loadTrona();
        Materials.TungstenCarbide = loadTungstenCarbide();
        Materials.TungstenSteel = loadTungstenSteel();
        Materials.VanadiumMagnetite = loadVanadiumMagnetite();
        Materials.VanadiumSteel = loadVanadiumSteel();
        Materials.Vermiculite = loadVermiculite();
        Materials.Vinteum = loadVinteum();
        Materials.Vis = loadVis();
        Materials.VolcanicAsh = loadVolcanicAsh();
        Materials.Wollastonite = loadWollastonite();
        Materials.WoodSealed = loadWoodSealed();
        Materials.Zeolite = loadZeolite();
    }

    private static Materials loadAluminiumHydroxide() {
        return new MaterialBuilder(698, TextureSet.SET_QUARTZ, "Aluminium Hydroxide").setToolSpeed(1.0F)
            .setDurability(64)
            .setToolQuality(1)
            .addDustItems()
            .setRGB(235, 235, 255)
            .setColor(Dyes.dyeWhite)
            .setBlastFurnaceTemp(1200)
            .setMeltingPoint(1200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setExtraData(0)
            .setMaterialList(
                new MaterialStack(Aluminium, 1),
                new MaterialStack(Oxygen, 3),
                new MaterialStack(Hydrogen, 3))
            .setAspects(Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.GELUM, 2)))
            .constructMaterial();
    }

    private static Materials loadAluminiumoxide() {
        return new MaterialBuilder(697, TextureSet.SET_QUARTZ, "Alumina").setToolSpeed(1.0F)
            .setDurability(64)
            .setToolQuality(1)
            .addDustItems()
            .setRGB(235, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setBlastFurnaceTemp(2054)
            .setMeltingPoint(2054)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setExtraData(0)
            .setMaterialList(new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3))
            .setAspects(Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.GELUM, 2)))
            .constructMaterial();
    }

    private static Materials loadAlumite() {
        return new MaterialBuilder().setName("Alumite")
            .setDefaultLocalName("Obzinite")
            .setMetaItemSubID(400)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setRGB(0xff69b4)
            .setToolSpeed(5.0f)
            .setDurability(768)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Zinc, 5)
            .addMaterial(Materials.Steel, 2)
            .addMaterial(Materials.Obsidian, 2)
            .addAspect(TCAspects.STRONTIO, 2)
            .constructMaterial();
    }

    private static Materials loadAlunite() {
        return new MaterialBuilder().setName("Alunite")
            .setDefaultLocalName("Alunite")
            .setMetaItemSubID(911)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0xe1b441)
            .addDustItems()
            .addOreItems()
            // KAl3(SO4)2(OH)6
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Aluminium, 3)
            .addMaterial(Materials.Silicon, 2)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Oxygen, 14)
            .constructMaterial();
    }

    private static Materials loadAmethyst() {
        return new MaterialBuilder().setName("Amethyst")
            .setDefaultLocalName("Amethyst")
            .setMetaItemSubID(509)
            .setIconSet(TextureSet.SET_FLINT)
            .setColor(Dyes.dyePink)
            .setARGB(0x7fd232d2)
            .setToolSpeed(7.0f)
            .setDurability(256)
            .setToolQuality(3)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.SiliconDioxide, 4)
            .addMaterial(Materials.Iron, 1)
            .addAspect(TCAspects.LUCRUM, 6)
            .addAspect(TCAspects.VITREUS, 4)
            .constructMaterial();
    }

    private static Materials loadApatite() {
        return new MaterialBuilder().setName("Apatite")
            .setDefaultLocalName("Apatite")
            .setMetaItemSubID(530)
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeCyan)
            .setRGB(0xc8c8ff)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Calcium, 5)
            .addMaterial(Materials.Phosphate, 3)
            .addMaterial(Materials.Chlorine, 1)
            .addAspect(TCAspects.MESSIS, 2)
            .constructMaterial();
    }

    private static Materials loadBarite() {
        return new MaterialBuilder().setName("Barite")
            .setDefaultLocalName("Barite")
            .setMetaItemSubID(904)
            .setIconSet(TextureSet.SET_DULL)
            .setRGB(0xe6ebff)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Barium, 1)
            .addMaterial(Materials.Sulfur, 1)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadBastnasite() {
        return new MaterialBuilder().setName("Bastnasite")
            .setDefaultLocalName("Bastnasite")
            .setMetaItemSubID(905)
            .setIconSet(TextureSet.SET_FINE)
            .setRGB(0xc86e2d)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // (Ce, La, Y)CO3F
            .addMaterial(Materials.Cerium, 1)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Fluorine, 1)
            .addMaterial(Materials.Oxygen, 3)
            .constructMaterial();
    }

    private static Materials loadBauxite() {
        return new MaterialBuilder().setName("Bauxite")
            .setDefaultLocalName("Bauxite")
            .setMetaItemSubID(822)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBrown)
            .setRGB(0xc86400)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Rutile, 2)
            .addMaterial(Materials.Aluminium, 16)
            .addMaterial(Materials.Hydrogen, 10)
            .addMaterial(Materials.Oxygen, 11)
            .constructMaterial();
    }

    private static Materials loadBentonite() {
        return new MaterialBuilder().setName("Bentonite")
            .setDefaultLocalName("Bentonite")
            .setMetaItemSubID(927)
            .setIconSet(TextureSet.SET_ROUGH)
            .setRGB(0xf5d7d2)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // (Na,Ca)0.33(Al,Mg)2(Si4O10)(OH)2 nH2O
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Magnesium, 6)
            .addMaterial(Materials.Silicon, 12)
            .addMaterial(Materials.Hydrogen, 6)
            .addMaterial(Materials.Water, 5)
            .addMaterial(Materials.Oxygen, 36)
            .constructMaterial();
    }

    private static Materials loadBiotite() {
        return new MaterialBuilder().setName("Biotite")
            .setDefaultLocalName("Biotite")
            .setMetaItemSubID(848)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setRGB(0x141e14)
            .addDustItems()
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Magnesium, 3)
            .addMaterial(Materials.Aluminium, 3)
            .addMaterial(Materials.Fluorine, 2)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 10)
            .constructMaterial();
    }

    private static Materials loadBismuthBronze() {
        return new MaterialBuilder().setName("BismuthBronze")
            .setDefaultLocalName("Bismuth Bronze")
            .setMetaItemSubID(353)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeCyan)
            .setRGB(0x647d7d)
            .setToolSpeed(8.0f)
            .setDurability(256)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1100)
            .setBlastFurnaceRequired(true)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Bismuth, 1)
            .addMaterial(Materials.Zinc, 1)
            .addMaterial(Materials.Copper, 3)
            .constructMaterial();
    }

    private static Materials loadBlackBronze() {
        return new MaterialBuilder().setName("BlackBronze")
            .setDefaultLocalName("Black Bronze")
            .setMetaItemSubID(352)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyePurple)
            .setRGB(0x64327d)
            .setToolSpeed(12.0f)
            .setDurability(256)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(2000)
            .setBlastFurnaceRequired(true)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Gold, 1)
            .addMaterial(Materials.Silver, 1)
            .addMaterial(Materials.Copper, 3)
            .constructMaterial();
    }

    private static Materials loadBlackSteel() {
        return new MaterialBuilder().setName("BlackSteel")
            .setDefaultLocalName("Black Steel")
            .setMetaItemSubID(334)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x646464)
            .setToolSpeed(6.5f)
            .setDurability(768)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Nickel, 1)
            .addMaterial(Materials.BlackBronze, 1)
            .addMaterial(Materials.Steel, 3)
            .constructMaterial();
    }

    private static Materials loadBlaze() {
        return new MaterialBuilder().setName("Blaze")
            .setDefaultLocalName("Blaze")
            .setMetaItemSubID(801)
            .setIconSet(TextureSet.SET_POWDER)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffc800)
            .setToolSpeed(2.0f)
            .setDurability(16)
            .setToolQuality(1)
            .addDustItems()
            .addToolHeadItems()
            .setMeltingPoint(6400)
            .setDensityMultiplier(3)
            .setDensityDivider(2)
            .addCentrifugeRecipe()
            .addMaterial(Materials.DarkAsh, 1)
            .addMaterial(Materials.Sulfur, 1)
            .addMaterial(Materials.Magic, 1)
            .addAspect(TCAspects.PRAECANTATIO, 2)
            .addAspect(TCAspects.IGNIS, 4)
            .constructMaterial();
    }

    private static Materials loadBorax() {
        return new MaterialBuilder().setName("Borax")
            .setDefaultLocalName("Borax")
            .setMetaItemSubID(941)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xfafafa)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Sodium, 2)
            .addMaterial(Materials.Boron, 4)
            .addMaterial(Materials.Oxygen, 7)
            .addMaterial(Materials.Water, 10)
            .constructMaterial();
    }

    private static Materials loadChrysotile() {
        return new MaterialBuilder().setName("Chrysotile")
            .setDefaultLocalName("Chrysotile")
            .setMetaItemSubID(912)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0x6e8c6e)
            .setToolSpeed(32.0f)
            .setDurability(10240)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(9400)
            .setBlastFurnaceTemp(9400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Asbestos, 1)
            .constructMaterial()
            .setTurbineMultipliers(280, 280, 1);
    }

    private static Materials loadClay() {
        return new MaterialBuilder().setName("Clay")
            .setDefaultLocalName("Clay")
            .setMetaItemSubID(805)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeLightBlue)
            .setRGB(0xc8c8dc)
            .addDustItems()
            .addMaterial(Materials.Sodium, 2)
            .addMaterial(Materials.Lithium, 1)
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 2)
            .addMaterial(Materials.Oxygen, 7)
            .addMaterial(Materials.Water, 2)
            .constructMaterial();
    }

    private static Materials loadCobaltBrass() {
        return new MaterialBuilder().setName("CobaltBrass")
            .setDefaultLocalName("Cobalt Brass")
            .setMetaItemSubID(343)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xb4b4a0)
            .setToolSpeed(8.0f)
            .setDurability(256)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Brass, 7)
            .addMaterial(Materials.Tin, 1)
            .addMaterial(Materials.Cobalt, 1)
            .constructMaterial();
    }

    private static Materials loadConcrete() {
        return new MaterialBuilder().setName("Concrete")
            .setDefaultLocalName("Concrete")
            .setMetaItemSubID(947)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeGray)
            .setRGB(0x646464)
            .addDustItems()
            .setMeltingPoint(300)
            .addMaterial(Materials.Stone, 1)
            .addAspect(TCAspects.TERRA, 1)
            .constructMaterial();
    }

    private static Materials loadCryolite() {
        return new MaterialBuilder(699, TextureSet.SET_QUARTZ, "Cryolite").setToolSpeed(1.0F)
            .setDurability(64)
            .setToolQuality(1)
            .addOreItems()
            .setRGB(191, 239, 255)
            .setColor(Dyes.dyeLightBlue)
            .setMeltingPoint(1012)
            .setBlastFurnaceTemp(1012)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setExtraData(0)
            .setMaterialList(
                new MaterialStack(Sodium, 3),
                new MaterialStack(Aluminium, 1),
                new MaterialStack(Fluorine, 6))
            .constructMaterial();
    }

    private static Materials loadCryotheum() {
        return new MaterialBuilder().setName("Cryotheum")
            .setDefaultLocalName("Cryotheum")
            .setMetaItemSubID(898)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightBlue)
            .setRGB(0x0094cb)
            .addDustItems()
            .setFuelType(2)
            .setFuelPower(62)
            .setDensityMultiplier(4)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Saltpeter, 1)
            .addMaterial(Materials.Redstone, 1)
            .addMaterial(Materials.Snow, 1)
            .addMaterial(Materials.Blizz, 1)
            .addAspect(TCAspects.PRAECANTATIO, 2)
            .addAspect(TCAspects.ELECTRUM, 1)
            .addAspect(TCAspects.GELUM, 1)
            .constructMaterial()
            .disableAutoGeneratedRecycleRecipes();
    }

    private static Materials loadDamascusSteel() {
        return new MaterialBuilder().setName("DamascusSteel")
            .setDefaultLocalName("Damascus Steel")
            .setMetaItemSubID(335)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGray)
            .setRGB(0x6e6e6e)
            .setToolSpeed(8.0f)
            .setDurability(1280)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2000)
            .setBlastFurnaceTemp(1500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Steel, 1)
            .constructMaterial();
    }

    private static Materials loadDiatomite() {
        return new MaterialBuilder().setName("Diatomite")
            .setDefaultLocalName("Diatomite")
            .setMetaItemSubID(948)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGray)
            .setRGB(0xe1e1e1)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Flint, 8)
            .addMaterial(Materials.BandedIron, 1)
            .addMaterial(Materials.Sapphire, 1)
            .constructMaterial();
    }

    private static Materials loadDilutedSulfuricAcid() {
        return new MaterialBuilder(640, TextureSet.SET_FLUID, "Diluted Sulfuric Acid").addCell()
            .addFluid()
            .setRGB(192, 120, 32)
            .setColor(Dyes.dyeOrange)
            .setMaterialList(new MaterialStack(SulfuricAcid, 1))
            .constructMaterial();
    }

    private static Materials loadDolomite() {
        return new MaterialBuilder().setName("Dolomite")
            .setDefaultLocalName("Dolomite")
            .setMetaItemSubID(914)
            .setIconSet(TextureSet.SET_FLINT)
            .setRGB(0xe1cdcd)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // CaMg(CO3)2
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Oxygen, 6)
            .constructMaterial();
    }

    private static Materials loadEnderPearl() {
        return new MaterialBuilder().setName("EnderPearl")
            .setDefaultLocalName("Enderpearl")
            .setMetaItemSubID(532)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x6cdcc8)
            .setToolSpeed(1.0f)
            .setDurability(16)
            .setToolQuality(1)
            .addDustItems()
            .addGemItems()
            .setDensityMultiplier(16)
            .setDensityDivider(10)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Beryllium, 1)
            .addMaterial(Materials.Potassium, 4)
            .addMaterial(Materials.Nitrogen, 5)
            .addMaterial(Materials.Magic, 6)
            .addAspect(TCAspects.ALIENIS, 4)
            .addAspect(TCAspects.ITER, 4)
            .addAspect(TCAspects.PRAECANTATIO, 2)
            .constructMaterial();
    }

    private static Materials loadEpoxidFiberReinforced() {
        return new MaterialBuilder().setName("EpoxidFiberReinforced")
            .setDefaultLocalName("Fiber-Reinforced Epoxy Resin")
            .setMetaItemSubID(610)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBrown)
            .setRGB(0xa07010)
            .setToolSpeed(3.0f)
            .setDurability(64)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(400)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Epoxid, 1)
            .addAspect(TCAspects.MOTUS, 2)
            .constructMaterial();
    }

    private static Materials loadFlint() {
        return new MaterialBuilder().setName("Flint")
            .setDefaultLocalName("Flint")
            .setMetaItemSubID(802)
            .setIconSet(TextureSet.SET_FLINT)
            .setColor(Dyes.dyeGray)
            .setRGB(0x002040)
            .setToolSpeed(2.5f)
            .setDurability(128)
            .setToolQuality(1)
            .addDustItems()
            .addToolHeadItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.SiliconDioxide, 1)
            .addAspect(TCAspects.TERRA, 1)
            .addAspect(TCAspects.INSTRUMENTUM, 1)
            .constructMaterial();
    }

    private static Materials loadFullersEarth() {
        return new MaterialBuilder().setName("FullersEarth")
            .setDefaultLocalName("Fullers Earth")
            .setMetaItemSubID(928)
            .setIconSet(TextureSet.SET_FINE)
            .setRGB(0xa0a078)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // (Mg,Al)2Si4O10(OH) 4(H2O)
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Silicon, 4)
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Water, 4)
            .addMaterial(Materials.Oxygen, 11)
            .constructMaterial();
    }

    private static Materials loadGarnetRed() {
        return new MaterialBuilder().setName("GarnetRed")
            .setDefaultLocalName("Red Garnet")
            .setMetaItemSubID(527)
            .setIconSet(TextureSet.SET_RUBY)
            .setColor(Dyes.dyeRed)
            .setARGB(0x7fc85050)
            .setToolSpeed(7.0f)
            .setDurability(128)
            .setToolQuality(2)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Pyrope, 3)
            .addMaterial(Materials.Almandine, 5)
            .addMaterial(Materials.Spessartine, 8)
            .addAspect(TCAspects.VITREUS, 3)
            .constructMaterial();
    }

    private static Materials loadGarnetYellow() {
        return new MaterialBuilder().setName("GarnetYellow")
            .setDefaultLocalName("Yellow Garnet")
            .setMetaItemSubID(528)
            .setIconSet(TextureSet.SET_RUBY)
            .setColor(Dyes.dyeYellow)
            .setARGB(0x7fc8c850)
            .setToolSpeed(7.0f)
            .setDurability(128)
            .setToolQuality(2)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Andradite, 5)
            .addMaterial(Materials.Grossular, 8)
            .addMaterial(Materials.Uvarovite, 3)
            .addAspect(TCAspects.VITREUS, 3)
            .constructMaterial();
    }

    private static Materials loadGlass() {
        return new MaterialBuilder().setName("Glass")
            .setDefaultLocalName("Glass")
            .setMetaItemSubID(890)
            .setIconSet(TextureSet.SET_GLASS)
            .setColor(Dyes.dyeWhite)
            .setARGB(0xdcfafafa)
            .setToolSpeed(1.0f)
            .setDurability(4)
            .setToolQuality(0)
            .addDustItems()
            .addGemItems()
            .setMeltingPoint(1500)
            .addCentrifugeRecipe()
            .addMaterial(Materials.SiliconDioxide, 1)
            .addAspect(TCAspects.VITREUS, 2)
            .constructMaterial();
    }

    private static Materials loadGlauconite() {
        return new MaterialBuilder().setName("Glauconite")
            .setDefaultLocalName("Glauconite")
            .setMetaItemSubID(933)
            .setIconSet(TextureSet.SET_DULL)
            .setRGB(0x82b43c)
            .addDustItems()
            .addOreItems()
            // (K,Na)(Fe3+,Al,Mg)2(Si,Al)4O10(OH)2
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Magnesium, 2)
            .addMaterial(Materials.Aluminium, 4)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 12)
            .constructMaterial();
    }

    private static Materials loadGlauconiteSand() {
        return new MaterialBuilder().setName("GlauconiteSand")
            .setDefaultLocalName("Glauconite Sand")
            .setMetaItemSubID(949)
            .setIconSet(TextureSet.SET_DULL)
            .setRGB(0x82b43c)
            .addDustItems()
            .addOreItems()
            // (K,Na)(Fe3+,Al,Mg)2(Si,Al)4O10(OH)2
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Magnesium, 2)
            .addMaterial(Materials.Aluminium, 4)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 12)
            .constructMaterial();
    }

    private static Materials loadGraniteBlack() {
        return new MaterialBuilder().setName("GraniteBlack")
            .setDefaultLocalName("Black Granite")
            .setMetaItemSubID(849)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x0a0a0a)
            .setToolSpeed(4.0f)
            .setDurability(64)
            .setToolQuality(3)
            .addDustItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.SiliconDioxide, 4)
            .addMaterial(Materials.Biotite, 1)
            .addAspect(TCAspects.TUTAMEN, 1)
            .constructMaterial();
    }

    private static Materials loadGraniticMineralSand() {
        return new MaterialBuilder().setName("GraniticMineralSand")
            .setDefaultLocalName("Granitic Mineral Sand")
            .setMetaItemSubID(936)
            .setIconSet(TextureSet.SET_SAND)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x283c3c)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Magnetite, 1)
            .addMaterial(Materials.GraniteBlack, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .constructMaterial();
    }

    private static Materials loadGypsum() {
        return new MaterialBuilder().setName("Gypsum")
            .setDefaultLocalName("Gypsum")
            .setMetaItemSubID(934)
            .setIconSet(TextureSet.SET_DULL)
            .setRGB(0xe6e6fa)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // CaSO4 2H2O
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Sulfur, 1)
            .addMaterial(Materials.Oxygen, 4)
            .addMaterial(Materials.Water, 2)
            .constructMaterial();
    }

    private static Materials loadHydratedCoal() {
        return new MaterialBuilder().setName("HydratedCoal")
            .setDefaultLocalName("Hydrated Coal")
            .setMetaItemSubID(818)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x464664)
            .addDustItems()
            .setDensityMultiplier(9)
            .setDensityDivider(8)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Coal, 8)
            .addMaterial(Materials.Water, 1)
            .constructMaterial();
    }

    private static Materials loadIronMagnetic() {
        return new MaterialBuilder().setName("IronMagnetic")
            .setDefaultLocalName("Magnetic Iron")
            .setMetaItemSubID(354)
            .setIconSet(TextureSet.SET_MAGNETIC)
            .setColor(Dyes.dyeGray)
            .setRGB(0xc8c8c8)
            .setToolSpeed(6.0f)
            .setDurability(256)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setDensityMultiplier(51)
            .setDensityDivider(50)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .constructMaterial();
    }

    private static Materials loadJasper() {
        return new MaterialBuilder().setName("Jasper")
            .setDefaultLocalName("Jasper")
            .setMetaItemSubID(511)
            .setIconSet(TextureSet.SET_EMERALD)
            .setColor(Dyes.dyeRed)
            .setARGB(0x64c85050)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.SiliconDioxide, 1)
            .addAspect(TCAspects.LUCRUM, 4)
            .addAspect(TCAspects.VITREUS, 2)
            .constructMaterial();
    }

    private static Materials loadKaolinite() {
        return new MaterialBuilder().setName("Kaolinite")
            .setDefaultLocalName("Kaolinite")
            .setMetaItemSubID(929)
            .setIconSet(TextureSet.SET_DULL)
            .setRGB(0xf5ebeb)
            .addDustItems()
            .addOreItems()
            // Al2Si2O5(OH)4
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 2)
            .addMaterial(Materials.Hydrogen, 4)
            .addMaterial(Materials.Oxygen, 9)
            .constructMaterial();
    }

    private static Materials loadKnightmetal() {
        return new MaterialBuilder().setName("Knightmetal")
            .setDefaultLocalName("Knightmetal")
            .setMetaItemSubID(362)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLime)
            .setRGB(0xd2f0c8)
            .setToolSpeed(8.0f)
            .setDurability(1024)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setFuelType(5)
            .setFuelPower(24)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Steel, 2)
            .addMaterial(Materials.Magic, 1)
            .addAspect(TCAspects.LUCRUM, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .constructMaterial();
    }

    private static Materials loadKyanite() {
        return new MaterialBuilder().setName("Kyanite")
            .setDefaultLocalName("Kyanite")
            .setMetaItemSubID(924)
            .setIconSet(TextureSet.SET_FLINT)
            .setRGB(0x6e6efa)
            .addDustItems()
            .addOreItems()
            // Al2SiO5
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Oxygen, 5)
            .constructMaterial();
    }

    private static Materials loadLapis() {
        return new MaterialBuilder().setName("Lapis")
            .setDefaultLocalName("Lapis")
            .setMetaItemSubID(526)
            .setIconSet(TextureSet.SET_LAPIS)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x4646dc)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Lazurite, 12)
            .addMaterial(Materials.Sodalite, 2)
            .addMaterial(Materials.Pyrite, 1)
            .addMaterial(Materials.Calcite, 1)
            .addAspect(TCAspects.SENSUS, 1)
            .constructMaterial();
    }

    private static Materials loadLepidolite() {
        return new MaterialBuilder().setName("Lepidolite")
            .setDefaultLocalName("Lepidolite")
            .setMetaItemSubID(907)
            .setIconSet(TextureSet.SET_FINE)
            .setRGB(0xf0328c)
            .addDustItems()
            .addOreItems()
            // K(Li,Al,Rb)3(Al,Si)4O10(F,OH)2
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Lithium, 3)
            .addMaterial(Materials.Aluminium, 4)
            .addMaterial(Materials.Fluorine, 2)
            .addMaterial(Materials.Oxygen, 10)
            .constructMaterial();
    }

    private static Materials loadLignite() {
        return new MaterialBuilder().setName("Lignite")
            .setDefaultLocalName("Lignite Coal")
            .setMetaItemSubID(538)
            .setIconSet(TextureSet.SET_LIGNITE)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x644646)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 3)
            .addMaterial(Materials.Water, 1)
            .constructMaterial();
    }

    private static Materials loadLiveRoot() {
        return new MaterialBuilder().setName("LiveRoot")
            .setDefaultLocalName("Liveroot")
            .setMetaItemSubID(832)
            .setIconSet(TextureSet.SET_WOOD)
            .setColor(Dyes.dyeBrown)
            .setRGB(0xdcc800)
            .addDustItems()
            .setFuelType(5)
            .setFuelPower(16)
            .setDensityMultiplier(4)
            .setDensityDivider(3)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Wood, 3)
            .addMaterial(Materials.Magic, 1)
            .addAspect(TCAspects.ARBOR, 2)
            .addAspect(TCAspects.VICTUS, 2)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .constructMaterial();
    }

    private static Materials loadMalachite() {
        return new MaterialBuilder().setName("Malachite")
            .setDefaultLocalName("Malachite")
            .setMetaItemSubID(871)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x055f05)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // Cu2CO3(OH)2
            .addMaterial(Materials.Copper, 2)
            .addMaterial(Materials.Carbon, 1)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 5)
            .constructMaterial();
    }

    private static Materials loadManyullyn() {
        return new MaterialBuilder().setName("Manyullyn")
            .setDefaultLocalName("Manyullyn")
            .setMetaItemSubID(386)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyePurple)
            .setRGB(0x9a4cb9)
            .setToolSpeed(25.0f)
            .setDurability(2048)
            .setToolQuality(5)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3600)
            .setBlastFurnaceTemp(3600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Cobalt, 1)
            .addMaterial(Materials.Ardite, 1)
            .addAspect(TCAspects.STRONTIO, 2)
            .constructMaterial();
    }

    private static Materials loadMarble() {
        return new MaterialBuilder().setName("Marble")
            .setDefaultLocalName("Marble")
            .setMetaItemSubID(845)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xc8c8c8)
            .setToolSpeed(1.0f)
            .setDurability(16)
            .setToolQuality(1)
            .addDustItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Calcite, 7)
            .addAspect(TCAspects.PERFODIO, 1)
            .constructMaterial();
    }

    private static Materials loadMica() {
        return new MaterialBuilder().setName("Mica")
            .setDefaultLocalName("Mica")
            .setMetaItemSubID(901)
            .setIconSet(TextureSet.SET_FINE)
            .setRGB(0xc3c3cd)
            .addDustItems()
            .addOreItems()
            // KAl2(AlSi3O10)(F,OH)2
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Aluminium, 3)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Fluorine, 2)
            .addMaterial(Materials.Oxygen, 10)
            .constructMaterial();
    }

    private static Materials loadMirabilite() {
        return new MaterialBuilder().setName("Mirabilite")
            .setDefaultLocalName("Mirabilite")
            .setMetaItemSubID(900)
            .setIconSet(TextureSet.SET_DULL)
            .setRGB(0xf0fad2)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // Na2SO4 10H2O
            .addMaterial(Materials.Sodium, 2)
            .addMaterial(Materials.Sulfur, 1)
            .addMaterial(Materials.Water, 10)
            .addMaterial(Materials.Oxygen, 4)
            .constructMaterial();
    }

    private static Materials loadMonazite() {
        return new MaterialBuilder().setName("Monazite")
            .setDefaultLocalName("Monazite")
            .setMetaItemSubID(520)
            .setIconSet(TextureSet.SET_DIAMOND)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x324632)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // Wikipedia: (Ce, La, Nd, Th, Sm, Gd)PO4 Monazite also smelt-extract to Helium, it is brown like the rare
            // earth Item Monazite sand deposits are inevitably of the monazite-(Ce) composition. Typically, the
            // lanthanides in such monazites contain about 45.8% cerium, about 24% lanthanum, about 17% neodymium, about
            // 5% praseodymium, and minor quantities of samarium, gadolinium, and yttrium. Europium concentrations tend
            // to be low, about 0.05% Thorium content of monazite is variable.
            .addMaterial(Materials.RareEarth, 1)
            .addMaterial(Materials.Phosphate, 1)
            .constructMaterial();
    }

    private static Materials loadNeodymiumMagnetic() {
        return new MaterialBuilder().setName("NeodymiumMagnetic")
            .setDefaultLocalName("Magnetic Neodymium")
            .setMetaItemSubID(356)
            .setIconSet(TextureSet.SET_MAGNETIC)
            .setColor(Dyes.dyeGray)
            .setRGB(0x646464)
            .setToolSpeed(7.0f)
            .setDurability(512)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1297)
            .setBlastFurnaceTemp(1297)
            .setBlastFurnaceRequired(true)
            .setDensityMultiplier(51)
            .setDensityDivider(50)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Neodymium, 1)
            .addAspect(TCAspects.METALLUM, 1)
            .addAspect(TCAspects.MAGNETO, 3)
            .constructMaterial();
    }

    private static Materials loadNiter() {
        return new MaterialBuilder().setName("Niter")
            .setDefaultLocalName("Niter")
            .setMetaItemSubID(531)
            .setIconSet(TextureSet.SET_FLINT)
            .setColor(Dyes.dyePink)
            .setRGB(0xffc8c8)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Saltpeter, 1)
            .constructMaterial();
    }

    private static Materials loadNitroCoalFuel() {
        return new MaterialBuilder().setName("NitroCoalFuel")
            .setDefaultLocalName("Nitro-Coalfuel")
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x324632)
            .addCell()
            .setFuelPower(48)
            .addMaterial(Materials.Glyceryl, 1)
            .addMaterial(Materials.CoalFuel, 4)
            .constructMaterial();
    }

    private static Materials loadNitroFuel() {
        return new MaterialBuilder().setName("NitroFuel")
            .setDefaultLocalName("Cetane-Boosted Diesel")
            .setMetaItemSubID(709)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeLime)
            .setRGB(0xc8ff00)
            .addCell()
            .setFuelPower(1000)
            .constructMaterial();
    }

    private static Materials loadOlivine() {
        return new MaterialBuilder().setName("Olivine")
            .setDefaultLocalName("Olivine")
            .setMetaItemSubID(505)
            .setIconSet(TextureSet.SET_RUBY)
            .setColor(Dyes.dyeLime)
            .setARGB(0x7f96ff96)
            .setToolSpeed(7.0f)
            .setDurability(256)
            .setToolQuality(2)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Magnesium, 2)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.SiliconDioxide, 2)
            .addAspect(TCAspects.LUCRUM, 4)
            .addAspect(TCAspects.VITREUS, 2)
            .constructMaterial();
    }

    private static Materials loadOpal() {
        return new MaterialBuilder().setName("Opal")
            .setDefaultLocalName("Opal")
            .setMetaItemSubID(510)
            .setIconSet(TextureSet.SET_OPAL)
            .setColor(Dyes.dyeBlue)
            .setARGB(0x000000ff)
            .setToolSpeed(7.0f)
            .setDurability(256)
            .setToolQuality(2)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.SiliconDioxide, 1)
            .addAspect(TCAspects.LUCRUM, 5)
            .addAspect(TCAspects.VITREUS, 3)
            .constructMaterial();
    }

    private static Materials loadPentlandite() {
        return new MaterialBuilder().setName("Pentlandite")
            .setDefaultLocalName("Pentlandite")
            .setMetaItemSubID(909)
            .setIconSet(TextureSet.SET_DULL)
            .setRGB(0xa59605)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // (Fe,Ni)9S8
            .addMaterial(Materials.Nickel, 9)
            .addMaterial(Materials.Sulfur, 8)
            .constructMaterial();
    }

    private static Materials loadPerlite() {
        return new MaterialBuilder().setName("Perlite")
            .setDefaultLocalName("Perlite")
            .setMetaItemSubID(925)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x1e141e)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Obsidian, 2)
            .addMaterial(Materials.Water, 1)
            .constructMaterial();
    }

    private static Materials loadPitchblende() {
        return new MaterialBuilder().setName("Pitchblende")
            .setDefaultLocalName("Pitchblende")
            .setMetaItemSubID(873)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xc8d200)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Uraninite, 3)
            .addMaterial(Materials.Thorium, 1)
            .addMaterial(Materials.Lead, 1)
            .constructMaterial();
    }

    private static Materials loadPollucite() {
        return new MaterialBuilder().setName("Pollucite")
            .setDefaultLocalName("Pollucite")
            .setMetaItemSubID(919)
            .setIconSet(TextureSet.SET_DULL)
            .setRGB(0xf0d2d2)
            .addDustItems()
            .addOreItems()
            // (Cs,Na)2Al2Si4O12 2H2O (also a source of Rb)
            .addMaterial(Materials.Caesium, 2)
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.Silicon, 4)
            .addMaterial(Materials.Water, 2)
            .addMaterial(Materials.Oxygen, 12)
            .constructMaterial();
    }

    private static Materials loadPotassiumFeldspar() {
        return new MaterialBuilder().setName("PotassiumFeldspar")
            .setDefaultLocalName("Potassium Feldspar")
            .setMetaItemSubID(847)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyePink)
            .setRGB(0x782828)
            .addDustItems()
            .addMaterial(Materials.Potassium, 1)
            .addMaterial(Materials.Aluminium, 1)
            .addMaterial(Materials.Silicon, 3)
            .addMaterial(Materials.Oxygen, 8)
            .constructMaterial();
    }

    private static Materials loadQuartzSand() {
        return new MaterialBuilder().setName("QuartzSand")
            .setDefaultLocalName("Quartz Sand")
            .setMetaItemSubID(939)
            .setIconSet(TextureSet.SET_SAND)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xc2b280)
            .addDustItems()
            .addOreItems()
            .addMaterial(Materials.CertusQuartz, 1)
            .addMaterial(Materials.Quartzite, 1)
            .constructMaterial();
    }

    private static Materials loadRawStyreneButadieneRubber() {
        return new MaterialBuilder(634, TextureSet.SET_SHINY, "Raw Styrene-Butadiene Rubber").addDustItems()
            .setRGB(84, 64, 61)
            .setColor(Dyes.dyeGray)
            .setMaterialList(new MaterialStack(Styrene, 1), new MaterialStack(Butadiene, 3))
            .constructMaterial();
    }

    private static Materials loadRealgar() {
        return new MaterialBuilder().setName("Realgar")
            .setDefaultLocalName("Realgar")
            .setMetaItemSubID(913)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeWhite)
            .setRGB(0x8c6464)
            .setToolSpeed(1.0f)
            .setDurability(32)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Arsenic, 4)
            .addMaterial(Materials.Sulfur, 4)
            .constructMaterial();
    }

    private static Materials loadRedAlloy() {
        return new MaterialBuilder().setName("RedAlloy")
            .setDefaultLocalName("Red Alloy")
            .setMetaItemSubID(308)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeRed)
            .setRGB(0xc80000)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(500)
            .setDensityMultiplier(5)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Copper, 1)
            .addMaterial(Materials.Redstone, 4)
            .addAspect(TCAspects.MACHINA, 3)
            .constructMaterial();
    }

    private static Materials loadRedMud() {
        return new MaterialBuilder(743, TextureSet.SET_FLUID, "Red Mud").addCell()
            .addFluid()
            .setRGB(140, 22, 22)
            .setColor(Dyes.dyeRed)
            .constructMaterial();
    }

    private static Materials loadRedrock() {
        return new MaterialBuilder().setName("Redrock")
            .setDefaultLocalName("Redrock")
            .setMetaItemSubID(846)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeRed)
            .setRGB(0xff5032)
            .addDustItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Calcite, 2)
            .addMaterial(Materials.Flint, 1)
            .addMaterial(Materials.Clay, 1)
            .constructMaterial();
    }

    private static Materials loadRedstone() {
        return new MaterialBuilder().setName("Redstone")
            .setDefaultLocalName("Redstone")
            .setMetaItemSubID(810)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeRed)
            .setRGB(0xc80000)
            .addDustItems()
            .addOreItems()
            .setMeltingPoint(500)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Pyrite, 5)
            .addMaterial(Materials.Ruby, 1)
            .addMaterial(Materials.Mercury, 3)
            .addAspect(TCAspects.MACHINA, 1)
            .addAspect(TCAspects.POTENTIA, 2)
            .constructMaterial();
    }

    private static Materials loadRoseGold() {
        return new MaterialBuilder().setName("RoseGold")
            .setDefaultLocalName("Rose Gold")
            .setMetaItemSubID(351)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xffe61e)
            .setToolSpeed(14.0f)
            .setDurability(128)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1600)
            .setBlastFurnaceRequired(true)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Copper, 1)
            .addMaterial(Materials.Gold, 4)
            .constructMaterial();
    }

    private static Materials loadSamariumMagnetic() {
        return new MaterialBuilder().setName("SamariumMagnetic")
            .setDefaultLocalName("Magnetic Samarium")
            .setMetaItemSubID(399)
            .setIconSet(TextureSet.SET_MAGNETIC)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xffffcc)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1345)
            .setBlastFurnaceTemp(1345)
            .setBlastFurnaceRequired(true)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Samarium, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.RADIO, 1)
            .addAspect(TCAspects.MAGNETO, 10)
            .constructMaterial();
    }

    private static Materials loadSoapstone() {
        return new MaterialBuilder().setName("Soapstone")
            .setDefaultLocalName("Soapstone")
            .setMetaItemSubID(877)
            .setIconSet(TextureSet.SET_DULL)
            .setRGB(0x5f915f)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // H2Mg3(SiO3)4
            .addMaterial(Materials.Magnesium, 3)
            .addMaterial(Materials.Silicon, 4)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 12)
            .constructMaterial();
    }

    private static Materials loadSodiumAluminate() {
        return new MaterialBuilder(696, TextureSet.SET_QUARTZ, "Sodium Aluminate").setToolSpeed(1.0F)
            .setDurability(64)
            .setToolQuality(1)
            .addDustItems()
            .setRGB(255, 235, 255)
            .setColor(Dyes.dyeWhite)
            .setBlastFurnaceTemp(1800)
            .setMeltingPoint(1800)
            .setBlastFurnaceRequired(false)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setExtraData(0)
            .setMaterialList(
                new MaterialStack(Sodium, 1),
                new MaterialStack(Aluminium, 1),
                new MaterialStack(Oxygen, 2))
            .constructMaterial();
    }

    private static Materials loadSodiumCarbonate() {
        return new MaterialBuilder(695, TextureSet.SET_QUARTZ, "Sodium Carbonate").setToolSpeed(1.0F)
            .setDurability(64)
            .setToolQuality(1)
            .addDustItems()
            .setRGB(255, 255, 235)
            .setColor(Dyes.dyeWhite)
            .setBlastFurnaceTemp(851)
            .setMeltingPoint(851)
            .setBlastFurnaceRequired(false)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setExtraData(1)
            .setMaterialList(new MaterialStack(Sodium, 2), new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 3))
            .constructMaterial();
    }

    private static Materials loadSpodumene() {
        return new MaterialBuilder().setName("Spodumene")
            .setDefaultLocalName("Spodumene")
            .setMetaItemSubID(920)
            .setIconSet(TextureSet.SET_DULL)
            .setRGB(0xbeaaaa)
            .addDustItems()
            .addOreItems()
            // LiAl(SiO3)2
            .addMaterial(Materials.Lithium, 1)
            .addMaterial(Materials.Aluminium, 1)
            .addMaterial(Materials.Silicon, 2)
            .addMaterial(Materials.Oxygen, 6)
            .constructMaterial();
    }

    private static Materials loadSteelMagnetic() {
        return new MaterialBuilder().setName("SteelMagnetic")
            .setDefaultLocalName("Magnetic Steel")
            .setMetaItemSubID(355)
            .setIconSet(TextureSet.SET_MAGNETIC)
            .setColor(Dyes.dyeGray)
            .setRGB(0x808080)
            .setToolSpeed(6.0f)
            .setDurability(512)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1000)
            .setBlastFurnaceTemp(1000)
            .setBlastFurnaceRequired(true)
            .setDensityMultiplier(51)
            .setDensityDivider(50)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Steel, 1)
            .addAspect(TCAspects.METALLUM, 1)
            .addAspect(TCAspects.ORDO, 1)
            .addAspect(TCAspects.MAGNETO, 1)
            .constructMaterial();
    }

    private static Materials loadSteeleaf() {
        return new MaterialBuilder().setName("Steeleaf")
            .setDefaultLocalName("Steeleaf")
            .setMetaItemSubID(339)
            .setIconSet(TextureSet.SET_LEAF)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x327f32)
            .setToolSpeed(8.0f)
            .setDurability(768)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setFuelType(5)
            .setFuelPower(24)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Steel, 1)
            .addMaterial(Materials.Magic, 1)
            .addAspect(TCAspects.HERBA, 2)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .constructMaterial();
    }

    private static Materials loadSterlingSilver() {
        return new MaterialBuilder().setName("SterlingSilver")
            .setDefaultLocalName("Sterling Silver")
            .setMetaItemSubID(350)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xfadce1)
            .setToolSpeed(13.0f)
            .setDurability(128)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1700)
            .setBlastFurnaceRequired(true)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Copper, 1)
            .addMaterial(Materials.Silver, 4)
            .constructMaterial();
    }

    private static Materials loadSugar() {
        return new MaterialBuilder().setName("Sugar")
            .setDefaultLocalName("Sugar")
            .setMetaItemSubID(803)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xfafafa)
            .addDustItems()
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Water, 5)
            .addMaterial(Materials.Oxygen, 25)
            .addAspect(TCAspects.HERBA, 1)
            .addAspect(TCAspects.AQUA, 1)
            .addAspect(TCAspects.AER, 1)
            .constructMaterial();
    }

    private static Materials loadTalc() {
        return new MaterialBuilder().setName("Talc")
            .setDefaultLocalName("Talc")
            .setMetaItemSubID(902)
            .setIconSet(TextureSet.SET_DULL)
            .setRGB(0x5ab45a)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // H2Mg3(SiO3)4
            .addMaterial(Materials.Magnesium, 3)
            .addMaterial(Materials.Silicon, 4)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Oxygen, 12)
            .constructMaterial();
    }

    private static Materials loadTantalite() {
        return new MaterialBuilder().setName("Tantalite")
            .setDefaultLocalName("Tantalite")
            .setMetaItemSubID(921)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x915028)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // (Fe, Mn)Ta2O6 (also source of Nb)
            .addMaterial(Materials.Manganese, 1)
            .addMaterial(Materials.Tantalum, 2)
            .addMaterial(Materials.Oxygen, 6)
            .constructMaterial();
    }

    private static Materials loadThaumium() {
        return new MaterialBuilder().setName("Thaumium")
            .setDefaultLocalName("Thaumium")
            .setMetaItemSubID(330)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePurple)
            .setRGB(0x9664c8)
            .setToolSpeed(12.0f)
            .setDurability(256)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setDensityMultiplier(2)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Magic, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .constructMaterial();
    }

    private static Materials loadTPV() {
        return new MaterialBuilder().setName("TPVAlloy")
            .setDefaultLocalName("TPV-Alloy")
            .setMetaItemSubID(576)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setRGB(0xfaaafa)
            .setToolSpeed(16.0f)
            .setDurability(4000)
            .setToolQuality(5)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3000)
            .setBlastFurnaceTemp(3000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Titanium, 3)
            .addMaterial(Materials.Platinum, 3)
            .addMaterial(Materials.Vanadium, 1)
            .constructMaterial();
    }

    private static Materials loadTricalciumPhosphate() {
        return new MaterialBuilder().setName("TricalciumPhosphate")
            .setDefaultLocalName("Tricalcium Phosphate")
            .setMetaItemSubID(534)
            .setIconSet(TextureSet.SET_FLINT)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xffff00)
            .addDustItems()
            .addGemItems()
            .addOreItems()
            .addCell()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Calcium, 3)
            .addMaterial(Materials.Phosphate, 2)
            .constructMaterial();
    }

    private static Materials loadTrona() {
        return new MaterialBuilder().setName("Trona")
            .setDefaultLocalName("Trona")
            .setMetaItemSubID(903)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0x87875f)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // Na3(CO3)(HCO3) 2H2O
            .addMaterial(Materials.Sodium, 3)
            .addMaterial(Materials.Carbon, 2)
            .addMaterial(Materials.Hydrogen, 1)
            .addMaterial(Materials.Water, 2)
            .addMaterial(Materials.Oxygen, 6)
            .constructMaterial();
    }

    private static Materials loadTungstenCarbide() {
        return new MaterialBuilder().setName("TungstenCarbide")
            .setDefaultLocalName("Tungstencarbide")
            .setMetaItemSubID(370)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x330066)
            .setToolSpeed(14.0f)
            .setDurability(1280)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2460)
            .setBlastFurnaceTemp(2460)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Tungsten, 1)
            .addMaterial(Materials.Carbon, 1)
            .constructMaterial();
    }

    private static Materials loadTungstenSteel() {
        return new MaterialBuilder().setName("TungstenSteel")
            .setDefaultLocalName("Tungstensteel")
            .setMetaItemSubID(316)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x6464a0)
            .setToolSpeed(8.0f)
            .setDurability(2560)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(4000)
            .setBlastFurnaceTemp(4000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Steel, 1)
            .addMaterial(Materials.Tungsten, 1)
            .constructMaterial();
    }

    private static Materials loadVanadiumMagnetite() {
        return new MaterialBuilder().setName("VanadiumMagnetite")
            .setDefaultLocalName("Vanadium Magnetite")
            .setMetaItemSubID(923)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x23233c)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            // Mixture of Fe3O4 and V2O5
            .addMaterial(Materials.Magnetite, 1)
            .addMaterial(Materials.Vanadium, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .constructMaterial();
    }

    private static Materials loadVanadiumSteel() {
        return new MaterialBuilder().setName("VanadiumSteel")
            .setDefaultLocalName("Vanadiumsteel")
            .setMetaItemSubID(371)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xc0c0c0)
            .setToolSpeed(3.0f)
            .setDurability(1920)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1453)
            .setBlastFurnaceTemp(1453)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Vanadium, 1)
            .addMaterial(Materials.Chrome, 1)
            .addMaterial(Materials.Steel, 7)
            .constructMaterial();
    }

    private static Materials loadVermiculite() {
        return new MaterialBuilder().setName("Vermiculite")
            .setDefaultLocalName("Vermiculite")
            .setMetaItemSubID(932)
            .setIconSet(TextureSet.SET_METALLIC)
            .setRGB(0xc8b40f)
            .addDustItems()
            .addOreItems()
            // (Mg+2, Fe+2, Fe+3)3 [(AlSi)4O10] (OH)2 4H2O)
            .addMaterial(Materials.Iron, 3)
            .addMaterial(Materials.Aluminium, 4)
            .addMaterial(Materials.Silicon, 4)
            .addMaterial(Materials.Hydrogen, 2)
            .addMaterial(Materials.Water, 4)
            .addMaterial(Materials.Oxygen, 12)
            .constructMaterial();
    }

    private static Materials loadVinteum() {
        return new MaterialBuilder().setName("Vinteum")
            .setDefaultLocalName("Vinteum")
            .setMetaItemSubID(529)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeLightBlue)
            .setRGB(0x64c8ff)
            .setToolSpeed(10.0f)
            .setDurability(128)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addGemItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setFuelType(5)
            .setFuelPower(32)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Thaumium, 1)
            .addAspect(TCAspects.VITREUS, 2)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .constructMaterial();
    }

    private static Materials loadVis() {
        return new MaterialBuilder().setName("Vis")
            .setDefaultLocalName("Vis")
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyePurple)
            .setRGB(0x8000ff)
            .setFuelType(5)
            .setFuelPower(32)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Magic, 1)
            .addAspect(TCAspects.AURAM, 2)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .constructMaterial();
    }

    private static Materials loadVolcanicAsh() {
        return new MaterialBuilder().setName("VolcanicAsh")
            .setDefaultLocalName("Volcanic Ashes")
            .setMetaItemSubID(940)
            .setIconSet(TextureSet.SET_FLINT)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x3c3232)
            .addDustItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Flint, 6)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Magnesium, 1)
            .constructMaterial();
    }

    private static Materials loadWollastonite() {
        return new MaterialBuilder().setName("Wollastonite")
            .setDefaultLocalName("Wollastonite")
            .setMetaItemSubID(915)
            .setIconSet(TextureSet.SET_DULL)
            .setRGB(0xf0f0f0)
            .addDustItems()
            .addOreItems()
            .addElectrolyzerRecipe()
            // CaSiO3
            .addMaterial(Materials.Calcium, 1)
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Oxygen, 3)
            .constructMaterial();
    }

    private static Materials loadWoodSealed() {
        return new MaterialBuilder().setName("WoodSealed")
            .setDefaultLocalName("Sealed Wood")
            .setMetaItemSubID(889)
            .setIconSet(TextureSet.SET_WOOD)
            .setColor(Dyes.dyeBrown)
            .setRGB(0x502800)
            .setToolSpeed(3.0f)
            .setDurability(24)
            .setToolQuality(0)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .addMaterial(Materials.Wood, 1)
            .addAspect(TCAspects.ARBOR, 2)
            .addAspect(TCAspects.FABRICO, 1)
            .constructMaterial();
    }

    private static Materials loadZeolite() {
        return new MaterialBuilder().setName("Zeolite")
            .setDefaultLocalName("Zeolite")
            .setMetaItemSubID(916)
            .setIconSet(TextureSet.SET_DULL)
            .setRGB(0xf0e6e6)
            .addDustItems()
            .addOreItems()
            // NaCa4(Si27Al9)O72
            .addMaterial(Materials.Sodium, 1)
            .addMaterial(Materials.Calcium, 4)
            .addMaterial(Materials.Silicon, 27)
            .addMaterial(Materials.Aluminium, 9)
            .addMaterial(Materials.Oxygen, 72)
            .constructMaterial();
    }

    private static void loadDegree3Compounds() {
        Materials.Basalt = loadBasalt();
        Materials.BlueSteel = loadBlueSteel();
        Materials.BorosilicateGlass = loadBorosilicateGlass();
        Materials.EnderEye = loadEnderEye();
        Materials.Fireclay = loadFireclay();
        Materials.GarnetSand = loadGarnetSand();
        Materials.HSSG = loadHSSG();
        Materials.IronWood = loadIronWood();
        Materials.Pyrotheum = loadPyrotheum();
        Materials.GraniteRed = loadGraniteRed();
        Materials.RedSteel = loadRedSteel();
    }

    private static Materials loadBasalt() {
        return new MaterialBuilder().setName("Basalt")
            .setDefaultLocalName("Basalt")
            .setMetaItemSubID(844)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x1e1414)
            .setToolSpeed(1.0f)
            .setDurability(64)
            .setToolQuality(1)
            .addDustItems()
            .addToolHeadItems()
            .addGearItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Olivine, 1)
            .addMaterial(Materials.Calcite, 3)
            .addMaterial(Materials.Flint, 8)
            .addMaterial(Materials.DarkAsh, 4)
            .addAspect(TCAspects.TENEBRAE, 1)
            .constructMaterial()
            .disableAutoGeneratedRecycleRecipes();
    }

    private static Materials loadBlueSteel() {
        return new MaterialBuilder().setName("BlueSteel")
            .setDefaultLocalName("Blue Steel")
            .setMetaItemSubID(349)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x64648c)
            .setToolSpeed(7.5f)
            .setDurability(1024)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.RoseGold, 1)
            .addMaterial(Materials.Brass, 1)
            .addMaterial(Materials.Steel, 2)
            .addMaterial(Materials.BlackSteel, 4)
            .constructMaterial();
    }

    private static Materials loadBorosilicateGlass() {
        return new MaterialBuilder(611, TextureSet.SET_GLASS, "Borosilicate Glass").addDustItems()
            .addMetalItems()
            .setRGB(230, 243, 230)
            .setColor(Dyes.dyeWhite)
            .setMaterialList(new MaterialStack(Boron, 1), new MaterialStack(Glass, 7))
            .addCentrifugeRecipe()
            .constructMaterial();
    }

    private static Materials loadEnderEye() {
        return new MaterialBuilder().setName("EnderEye")
            .setDefaultLocalName("Endereye")
            .setMetaItemSubID(533)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGreen)
            .setRGB(0xa0fae6)
            .setToolSpeed(1.0f)
            .setDurability(16)
            .setToolQuality(1)
            .addDustItems()
            .addGemItems()
            .setFuelType(5)
            .setFuelPower(10)
            .setDensityMultiplier(2)
            .addCentrifugeRecipe()
            .addMaterial(Materials.EnderPearl, 1)
            .addMaterial(Materials.Blaze, 1)
            .addAspect(TCAspects.SENSUS, 4)
            .addAspect(TCAspects.ALIENIS, 4)
            .addAspect(TCAspects.ITER, 4)
            .addAspect(TCAspects.PRAECANTATIO, 3)
            .addAspect(TCAspects.IGNIS, 2)
            .constructMaterial();
    }

    private static Materials loadFireclay() {
        return new MaterialBuilder(626, TextureSet.SET_ROUGH, "Fireclay").addDustItems()
            .setRGB(173, 160, 155)
            .setExtraData(2)
            .setColor(Dyes.dyeBrown)
            .setMaterialList(new MaterialStack(Brick, 1), new MaterialStack(Clay, 1))
            .constructMaterial();
    }

    private static Materials loadGarnetSand() {
        return new MaterialBuilder().setName("GarnetSand")
            .setDefaultLocalName("Garnet Sand")
            .setMetaItemSubID(938)
            .setIconSet(TextureSet.SET_SAND)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xc86400)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.GarnetRed, 1)
            .addMaterial(Materials.GarnetYellow, 1)
            .constructMaterial();
    }

    private static Materials loadHSSG() {
        return new MaterialBuilder().setName("HSSG")
            .setDefaultLocalName("HSS-G")
            .setMetaItemSubID(372)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setRGB(0x999900)
            .setToolSpeed(10.0f)
            .setDurability(4000)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(4500)
            .setBlastFurnaceTemp(4500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.TungstenSteel, 5)
            .addMaterial(Materials.Chrome, 1)
            .addMaterial(Materials.Molybdenum, 2)
            .addMaterial(Materials.Vanadium, 1)
            .constructMaterial();
    }

    private static Materials loadIronWood() {
        return new MaterialBuilder().setName("IronWood")
            .setDefaultLocalName("Ironwood")
            .setMetaItemSubID(338)
            .setIconSet(TextureSet.SET_WOOD)
            .setColor(Dyes.dyeBrown)
            .setRGB(0x968c6e)
            .setToolSpeed(6.0f)
            .setDurability(384)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setFuelType(5)
            .setFuelPower(8)
            .setDensityMultiplier(19)
            .setDensityDivider(18)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Iron, 9)
            .addMaterial(Materials.LiveRoot, 9)
            .addMaterial(Materials.Gold, 1)
            .constructMaterial();
    }

    private static Materials loadPyrotheum() {
        return new MaterialBuilder().setName("Pyrotheum")
            .setDefaultLocalName("Pyrotheum")
            .setMetaItemSubID(843)
            .setIconSet(TextureSet.SET_FIERY)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xff8000)
            .addDustItems()
            .setFuelType(2)
            .setFuelPower(62)
            .setDensityMultiplier(4)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Coal, 1)
            .addMaterial(Materials.Redstone, 1)
            .addMaterial(Materials.Blaze, 1)
            .addMaterial(Materials.Sulfur, 1)
            .addAspect(TCAspects.PRAECANTATIO, 2)
            .addAspect(TCAspects.IGNIS, 1)
            .constructMaterial()
            .disableAutoGeneratedRecycleRecipes();
    }

    private static Materials loadGraniteRed() {
        return new MaterialBuilder().setName("GraniteRed")
            .setDefaultLocalName("Red Granite")
            .setMetaItemSubID(850)
            .setIconSet(TextureSet.SET_ROUGH)
            .setColor(Dyes.dyeMagenta)
            .setRGB(0xff0080)
            .setToolSpeed(4.0f)
            .setDurability(64)
            .setToolQuality(3)
            .addDustItems()
            .addToolHeadItems()
            .addGearItems()
            .addMaterial(Materials.Aluminium, 2)
            .addMaterial(Materials.PotassiumFeldspar, 1)
            .addMaterial(Materials.Oxygen, 3)
            .addAspect(TCAspects.TUTAMEN, 1)
            .constructMaterial();
    }

    private static Materials loadRedSteel() {
        return new MaterialBuilder().setName("RedSteel")
            .setDefaultLocalName("Red Steel")
            .setMetaItemSubID(348)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setRGB(0x8c6464)
            .setToolSpeed(7.0f)
            .setDurability(896)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1300)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.SterlingSilver, 1)
            .addMaterial(Materials.BismuthBronze, 1)
            .addMaterial(Materials.Steel, 2)
            .addMaterial(Materials.BlackSteel, 4)
            .constructMaterial();
    }

    private static void loadDegree4Compounds() {
        Materials.BasalticMineralSand = loadBasalticMineralSand();
        Materials.HSSE = loadHSSE();
        Materials.HSSS = loadHSSS();
    }

    private static Materials loadBasalticMineralSand() {
        return new MaterialBuilder().setName("BasalticMineralSand")
            .setDefaultLocalName("Basaltic Mineral Sand")
            .setMetaItemSubID(935)
            .setIconSet(TextureSet.SET_SAND)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x283228)
            .addDustItems()
            .addOreItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Magnetite, 1)
            .addMaterial(Materials.Basalt, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.MAGNETO, 1)
            .constructMaterial();
    }

    private static Materials loadHSSE() {
        return new MaterialBuilder().setName("HSSE")
            .setDefaultLocalName("HSS-E")
            .setMetaItemSubID(373)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x336600)
            .setToolSpeed(32.0f)
            .setDurability(10240)
            .setToolQuality(7)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5400)
            .setBlastFurnaceTemp(5400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.HSSG, 6)
            .addMaterial(Materials.Cobalt, 1)
            .addMaterial(Materials.Manganese, 1)
            .addMaterial(Materials.Silicon, 1)
            .constructMaterial();
    }

    private static Materials loadHSSS() {
        return new MaterialBuilder().setName("HSSS")
            .setDefaultLocalName("HSS-S")
            .setMetaItemSubID(374)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setRGB(0x660033)
            .setToolSpeed(32.0f)
            .setDurability(10240)
            .setToolQuality(8)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5400)
            .setBlastFurnaceTemp(5400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.HSSG, 6)
            .addMaterial(Materials.Iridium, 2)
            .addMaterial(Materials.Osmium, 1)
            .constructMaterial();
    }

    private static void loadPolybenzimidazoleLine() {
        Materials.ChromiumTrioxide = loadChromiumTrioxide();
        Materials.Diaminobenzidin = loadDiaminobenzidin();
        Materials.Dichlorobenzidine = loadDichlorobenzidine();
        Materials.Dimethylbenzene = loadDimethylbenzene();
        Materials.Diphenylisophthalate = loadDiphenylisophthalate();
        Materials.Nitrochlorobenzene = loadNitrochlorobenzene();
        Materials.PhthalicAcid = loadPhthalicAcid();
        Materials.Polybenzimidazole = loadPolybenzimidazole();
        Materials.PotassiumNitrade = loadPotassiumNitrade();
        Materials.Potassiumdichromate = loadPotassiumdichromate();
    }

    private static Materials loadChromiumTrioxide() {
        return new MaterialBuilder(591, TextureSet.SET_DULL, "Chromium Trioxide").setName("Chromiumtrioxide")
            .addDustItems()
            .setRGB(255, 228, 225)
            .setColor(Dyes.dyePink)
            .setMaterialList(new MaterialStack(Chrome, 1), new MaterialStack(Oxygen, 3))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadDiaminobenzidin() {
        return new MaterialBuilder(597, TextureSet.SET_FLUID, "3,3-Diaminobenzidine").addCell()
            .addFluid()
            .setRGB(51, 125, 89)
            .setColor(Dyes.dyeOrange)
            .setMaterialList(
                new MaterialStack(Carbon, 12),
                new MaterialStack(Hydrogen, 14),
                new MaterialStack(Nitrogen, 4))
            .constructMaterial();
    }

    private static Materials loadDichlorobenzidine() {
        return new MaterialBuilder(596, TextureSet.SET_FLUID, "3,3-Dichlorobenzidine").addCell()
            .addFluid()
            .setRGB(161, 222, 166)
            .setColor(Dyes.dyeOrange)
            .setMaterialList(
                new MaterialStack(Carbon, 12),
                new MaterialStack(Hydrogen, 10),
                new MaterialStack(Nitrogen, 2),
                new MaterialStack(Chlorine, 2))
            .constructMaterial();
    }

    private static Materials loadDimethylbenzene() {
        return new MaterialBuilder(593, TextureSet.SET_FLUID, "1,2-Dimethylbenzene").setName("Dimethylbenzene")
            .addCell()
            .addFluid()
            .setRGB(102, 156, 64)
            .setColor(Dyes.dyeLime)
            .setMeltingPoint(248)
            .setMaterialList(new MaterialStack(Carbon, 8), new MaterialStack(Hydrogen, 10))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadDiphenylisophthalate() {
        return new MaterialBuilder(598, TextureSet.SET_FLUID, "Diphenyl Isophthalate").setName("DiphenylIsophtalate")
            .addCell()
            .addFluid()
            .setRGB(36, 110, 87)
            .setColor(Dyes.dyeOrange)
            .setMaterialList(
                new MaterialStack(Carbon, 20),
                new MaterialStack(Hydrogen, 14),
                new MaterialStack(Oxygen, 4))
            .constructMaterial();
    }

    private static Materials loadNitrochlorobenzene() {
        return new MaterialBuilder(592, TextureSet.SET_FLUID, "2-Nitrochlorobenzene").addCell()
            .addFluid()
            .setRGB(143, 181, 26)
            .setColor(Dyes.dyeLime)
            .setMaterialList(
                new MaterialStack(Carbon, 6),
                new MaterialStack(Hydrogen, 4),
                new MaterialStack(Chlorine, 1),
                new MaterialStack(Nitrogen, 1),
                new MaterialStack(Oxygen, 2))
            .constructMaterial();
    }

    private static Materials loadPhthalicAcid() {
        return new MaterialBuilder(595, TextureSet.SET_FLUID, "Phthalic Acid").setName("phtalicacid")
            .addCell()
            .addFluid()
            .setRGB(54, 133, 71)
            .setColor(Dyes.dyeOrange)
            .setMaterialList(new MaterialStack(Carbon, 8), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 4))
            .constructMaterial();
    }

    private static Materials loadPolybenzimidazole() {
        return new MaterialBuilder().setName("Polybenzimidazole")
            .setDefaultLocalName("Polybenzimidazole")
            .setMetaItemSubID(599)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x2d2d2d)
            .setToolSpeed(3.0f)
            .setDurability(64)
            .setToolQuality(1)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1450)
            .addMaterial(Materials.Carbon, 20)
            .addMaterial(Materials.Nitrogen, 4)
            .addMaterial(Materials.Hydrogen, 12)
            .addAspect(TCAspects.ORDO, 2)
            .addAspect(TCAspects.VOLATUS, 1)
            .constructMaterial();
    }

    private static Materials loadPotassiumNitrade() {
        return new MaterialBuilder(590, TextureSet.SET_DULL, "Potassium Nitrate").setName("PotassiumNitrate")
            .addDustItems()
            .setRGB(129, 34, 141)
            .setColor(Dyes.dyePurple)
            .setMaterialList(
                new MaterialStack(Potassium, 1),
                new MaterialStack(Nitrogen, 1),
                new MaterialStack(Oxygen, 3))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadPotassiumdichromate() {
        return new MaterialBuilder(594, TextureSet.SET_DULL, "Potassium Dichromate").setName("PotassiumDichromate")
            .addDustItems()
            .setRGB(255, 8, 127)
            .setColor(Dyes.dyePink)
            .setMaterialList(
                new MaterialStack(Potassium, 2),
                new MaterialStack(Chrome, 2),
                new MaterialStack(Oxygen, 7))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static void loadGasolineLine() {
        Materials.AntiKnock = loadAntiKnock();
        Materials.GasolinePremium = loadGasolinePremium();
        Materials.GasolineRaw = loadGasolineRaw();
        Materials.GasolineRegular = loadGasolineRegular();
        Materials.MTBEMixture = loadMTBEMixture();
        Materials.MTBEMixtureAlt = loadMTBEMixtureAlt();
        Materials.NitrousOxide = loadNitrousOxide();
        Materials.Octane = loadOctane();
    }

    private static Materials loadAntiKnock() {
        return new MaterialBuilder(994, TextureSet.SET_FLUID, "Anti-Knock Agent").setName("EthylTertButylEther")
            .addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMaterialList(
                new MaterialStack(Carbon, 6),
                new MaterialStack(Hydrogen, 14),
                new MaterialStack(Oxygen, 1))
            .constructMaterial();
    }

    private static Materials loadGasolinePremium() {
        return new MaterialBuilder(998, TextureSet.SET_FLUID, "High Octane Gasoline").addCell()
            .addFluid()
            .setRGB(255, 165, 0)
            .setColor(Dyes.dyeOrange)
            .setFuelType(MaterialBuilder.DIESEL)
            .setFuelPower(2500)
            .constructMaterial();
    }

    private static Materials loadGasolineRaw() {
        return new MaterialBuilder(996, TextureSet.SET_FLUID, "Raw Gasoline").addCell()
            .addFluid()
            .setRGB(255, 100, 0)
            .setColor(Dyes.dyeOrange)
            .constructMaterial();
    }

    private static Materials loadGasolineRegular() {
        return new MaterialBuilder(997, TextureSet.SET_FLUID, "Gasoline").addCell()
            .addFluid()
            .setRGB(255, 165, 0)
            .setColor(Dyes.dyeOrange)
            .setFuelType(MaterialBuilder.DIESEL)
            .setFuelPower(576)
            .constructMaterial();
    }

    private static Materials loadMTBEMixture() {
        return new MaterialBuilder(983, TextureSet.SET_FLUID, "MTBE Reaction Mixture (Butene)").addCell()
            .addGas()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMaterialList(
                new MaterialStack(Carbon, 5),
                new MaterialStack(Hydrogen, 12),
                new MaterialStack(Oxygen, 1))
            .constructMaterial();
    }

    private static Materials loadMTBEMixtureAlt() {
        return new MaterialBuilder(425, TextureSet.SET_FLUID, "MTBE Reaction Mixture (Butane)").addCell()
            .addGas()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setMaterialList(
                new MaterialStack(Carbon, 5),
                new MaterialStack(Hydrogen, 14),
                new MaterialStack(Oxygen, 1))
            .constructMaterial();
    }

    private static Materials loadNitrousOxide() {
        return new MaterialBuilder(993, TextureSet.SET_FLUID, "Nitrous Oxide").addCell()
            .addGas()
            .setRGB(125, 200, 255)
            .setColor(Dyes.dyeBlue)
            .setMaterialList(new MaterialStack(Nitrogen, 2), new MaterialStack(Oxygen, 1))
            .addElectrolyzerRecipe()
            .constructMaterial();
    }

    private static Materials loadOctane() {
        return new MaterialBuilder(995, TextureSet.SET_FLUID, "Octane").addCell()
            .addFluid()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .setFuelType(MaterialBuilder.DIESEL)
            .setFuelPower(80)
            .setMaterialList(new MaterialStack(Carbon, 8), new MaterialStack(Hydrogen, 18))
            .constructMaterial();
    }

    private static void loadAdded() {
        Materials.BloodInfusedIron = loadBloodInfusedIron();
        Materials.Electrotine = loadElectrotine();
        Materials.EnhancedGalgadorian = loadEnhancedGalgadorian();
        Materials.Galgadorian = loadGalgadorian();
        Materials.Shadow = loadShadow();
    }

    private static Materials loadBloodInfusedIron() {
        return new MaterialBuilder().setName("BloodInfusedIron")
            .setDefaultLocalName("Blood Infused Iron")
            .setMetaItemSubID(977)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeRed)
            .setRGB(0x45090a)
            .setToolSpeed(10.0f)
            .setDurability(384)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(2400)
            .addAspect(TCAspects.METALLUM, 3)
            .addAspect(TCAspects.PRAECANTATIO, 1)
            .constructMaterial();
    }

    private static Materials loadElectrotine() {
        return new MaterialBuilder().setName("Electrotine")
            .setDefaultLocalName("Electrotine")
            .setMetaItemSubID(812)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeCyan)
            .setRGB(0x3cb4c8)
            .addDustItems()
            .addOreItems()
            .addMaterial(Materials.Redstone, 1)
            .addMaterial(Materials.Electrum, 1)
            .addAspect(TCAspects.ELECTRUM, 2)
            .constructMaterial();
    }

    private static Materials loadEnhancedGalgadorian() {
        return new MaterialBuilder().setName("EnhancedGalgadorian")
            .setDefaultLocalName("Enhanced Galgadorian")
            .setMetaItemSubID(385)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setRGB(0x985d85)
            .setToolSpeed(32.0f)
            .setDurability(7200)
            .setToolQuality(5)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(4500)
            .setBlastFurnaceTemp(4500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .constructMaterial();
    }

    private static Materials loadGalgadorian() {
        return new MaterialBuilder().setName("Galgadorian")
            .setDefaultLocalName("Galgadorian")
            .setMetaItemSubID(384)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyePink)
            .setRGB(0x9a6977)
            .setToolSpeed(16.0f)
            .setDurability(3600)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3000)
            .setBlastFurnaceTemp(3000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .constructMaterial();
    }

    private static Materials loadShadow() {
        return new MaterialBuilder().setName("Shadow")
            .setDefaultLocalName("Shadow Metal")
            .setMetaItemSubID(368)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x100342)
            .setToolSpeed(32.0f)
            .setDurability(8192)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1800)
            .setBlastFurnaceTemp(1800)
            .setBlastFurnaceRequired(true)
            .setDensityMultiplier(4)
            .setDensityDivider(3)
            .constructMaterial();
    }

    private static void loadGalaxySpace() {
        Materials.BlackPlutonium = loadBlackPlutonium();
        Materials.CallistoIce = loadCallistoIce();
        Materials.Duralumin = loadDuralumin();
        Materials.Ledox = loadLedox();
        Materials.MysteriousCrystal = loadMysteriousCrystal();
        Materials.Mytryl = loadMytryl();
        Materials.Oriharukon = loadOriharukon();
        Materials.Quantium = loadQuantium();
    }

    private static Materials loadBlackPlutonium() {
        return new MaterialBuilder().setName("BlackPlutonium")
            .setDefaultLocalName("Black Plutonium")
            .setMetaItemSubID(388)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x323232)
            .setToolSpeed(36.0f)
            .setDurability(8192)
            .setToolQuality(8)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(9000)
            .setBlastFurnaceTemp(9000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .constructMaterial();
    }

    private static Materials loadCallistoIce() {
        return new MaterialBuilder().setName("CallistoIce")
            .setDefaultLocalName("Callisto Ice")
            .setMetaItemSubID(389)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightBlue)
            .setRGB(0x1eb1ff)
            .setToolSpeed(9.0f)
            .setDurability(1024)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .constructMaterial();
    }

    private static Materials loadDuralumin() {
        return new MaterialBuilder().setName("Duralumin")
            .setDefaultLocalName("Duralumin")
            .setMetaItemSubID(392)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xebd1a0)
            .setToolSpeed(16.0f)
            .setDurability(512)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1600)
            .setBlastFurnaceTemp(1600)
            .setBlastFurnaceRequired(true)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Aluminium, 6)
            .addMaterial(Materials.Copper, 1)
            .addMaterial(Materials.Manganese, 1)
            .addMaterial(Materials.Magnesium, 1)
            .constructMaterial();
    }

    private static Materials loadLedox() {
        return new MaterialBuilder().setName("Ledox")
            .setDefaultLocalName("Ledox")
            .setMetaItemSubID(390)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x0074ff)
            .setToolSpeed(15.0f)
            .setDurability(1024)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .constructMaterial();
    }

    private static Materials loadMysteriousCrystal() {
        return new MaterialBuilder().setName("MysteriousCrystal")
            .setDefaultLocalName("Mysterious Crystal")
            .setMetaItemSubID(398)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeCyan)
            .setRGB(0x16856c)
            .setToolSpeed(8.0f)
            .setDurability(256)
            .setToolQuality(6)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(7200)
            .setBlastFurnaceTemp(7200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .constructMaterial();
    }

    private static Materials loadMytryl() {
        return new MaterialBuilder().setName("Mytryl")
            .setDefaultLocalName("Mytryl")
            .setMetaItemSubID(387)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xf26404)
            .setToolSpeed(8.0f)
            .setDurability(512)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3600)
            .setBlastFurnaceTemp(3600)
            .setBlastFurnaceRequired(true)
            .constructMaterial();
    }

    private static Materials loadOriharukon() {
        return new MaterialBuilder().setName("Oriharukon")
            .setDefaultLocalName("Oriharukon")
            .setElement(Element.Oh)
            .setMetaItemSubID(393)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLime)
            .setRGB(0x677d68)
            .setToolSpeed(32.0f)
            .setDurability(10240)
            .setToolQuality(5)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5400)
            .setBlastFurnaceTemp(5400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.LUCRUM, 2)
            .addAspect(TCAspects.ALIENIS, 1)
            .constructMaterial();
    }

    private static Materials loadQuantium() {
        return new MaterialBuilder().setName("Quantium")
            .setDefaultLocalName("Quantium")
            .setMetaItemSubID(391)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLime)
            .setRGB(0x00d10b)
            .setToolSpeed(18.0f)
            .setDurability(2048)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(9900)
            .setBlastFurnaceTemp(9900)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .constructMaterial();
    }

    private static void loadUnclassified07() {
        Materials.AstralSilver = loadAstralSilver();
        Materials.BlueAlloy = loadBlueAlloy();
        Materials.ConductiveIron = loadConductiveIron();
        Materials.CrudeSteel = loadCrudeSteel();
        Materials.CrystallineAlloy = loadCrystallineAlloy();
        Materials.CrystallinePinkSlime = loadCrystallinePinkSlime();
        Materials.DarkSteel = loadDarkSteel();
        Materials.ElectricalSteel = loadElectricalSteel();
        Materials.EndSteel = loadEndSteel();
        Materials.Enderium = loadEnderium();
        Materials.EnergeticAlloy = loadEnergeticAlloy();
        Materials.EnergeticSilver = loadEnergeticSilver();
        Materials.MelodicAlloy = loadMelodicAlloy();
        Materials.Mithril = loadMithril();
        Materials.PulsatingIron = loadPulsatingIron();
        Materials.RedstoneAlloy = loadRedstoneAlloy();
        Materials.ShadowIron = loadShadowIron();
        Materials.ShadowSteel = loadShadowSteel();
        Materials.Soularium = loadSoularium();
        Materials.StellarAlloy = loadStellarAlloy();
        Materials.VibrantAlloy = loadVibrantAlloy();
        Materials.VividAlloy = loadVividAlloy();
    }

    private static void loadOverpoweredMaterials() {
        Materials.Bedrockium = loadBedrockium();
        Materials.CosmicNeutronium = loadCosmicNeutronium();
        Materials.Ichorium = loadIchorium();
        Materials.Infinity = loadInfinity();
        Materials.InfinityCatalyst = loadInfinityCatalyst();
        Materials.Trinium = loadTrinium();
    }

    private static Materials loadBedrockium() {
        return new MaterialBuilder(395, new TextureSet("bedrockium", true), "Bedrockium").addOreItems()
            .addDustItems()
            .addPlasma()
            .addMetalItems()
            .setDurability(327680)
            .setToolSpeed(8f)
            .setToolQuality(9)
            .setName("Bedrockium")
            .setBlastFurnaceRequired(true)
            .setBlastFurnaceTemp(9900)
            .setMeltingPoint(9900)
            .setMaterialList(
                new MaterialStack(Materials.SiliconDioxide, 26244),
                new MaterialStack(Materials.Diamond, 9))
            .setColor(Dyes.dyeBlack)
            .setDensityDivider(1)
            .setDensityMultiplier(1)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_EV)
            .disableAutoGeneratedBlastFurnaceRecipes()
            .disableAutoGeneratedVacuumFreezerRecipe()
            .setHasCorrespondingPlasma(true);
    }

    private static Materials loadCosmicNeutronium() {
        return new MaterialBuilder().setName("CosmicNeutronium")
            .setDefaultLocalName("Cosmic Neutronium")
            .setMetaItemSubID(982)
            .setIconSet(new TextureSet("cosmicneutronium", true))
            .setColor(Dyes.dyeBlack)
            .setRGB(50, 50, 55)
            .setToolSpeed(96.0f)
            .setDurability(163840)
            .setToolQuality(12)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(9900)
            .setBlastFurnaceTemp(9900)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .constructMaterial()
            .setTurbineMultipliers(6, 6, 3)
            .setProcessingMaterialTierEU(TierEU.RECIPE_ZPM)
            .setHasCorrespondingPlasma(true);
    }

    private static Materials loadIchorium() {
        return new MaterialBuilder().setName("Ichorium")
            .setDefaultLocalName("Ichorium")
            .setMetaItemSubID(978)
            .setIconSet(new TextureSet("ichorium", true))
            .setColor(Dyes.dyeOrange)
            .setRGB(211, 120, 6)
            .setToolSpeed(32.0f)
            .setDurability(850000)
            .setToolQuality(12)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setFuelType(5)
            .setFuelPower(250000)
            .setMeltingPoint(9000)
            .setBlastFurnaceTemp(9000)
            .setBlastFurnaceRequired(true)
            .constructMaterial()
            .setTurbineMultipliers(6, 6, 3)
            .setHasCorrespondingPlasma(true);
    }

    private static Materials loadInfinity() {
        return new MaterialBuilder().setName("Infinity")
            .setDefaultLocalName("Infinity")
            .setMetaItemSubID(397)
            .setIconSet(new TextureSet("infinity", true))
            .setColor(Dyes.dyeLightGray)
            .setToolSpeed(256.0f)
            .setDurability(2621440)
            .setToolQuality(17)
            .addDustItems()
            .addMetalItems()
            .addPlasma()
            .addToolHeadItems()
            .addGearItems()
            .setFuelType(5)
            .setFuelPower(5000000)
            .setMeltingPoint(10800)
            .setBlastFurnaceTemp(10800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UV)
            .setHasCorrespondingPlasma(true);
    }

    private static Materials loadAstralSilver() {
        return new MaterialBuilder().setName("AstralSilver")
            .setDefaultLocalName("Astral Silver")
            .setMetaItemSubID(333)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xe6e6ff)
            .setToolSpeed(10.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Silver, 2)
            .addMaterial(Materials.Thaumium, 1)
            .constructMaterial();
    }

    private static Materials loadBlueAlloy() {
        return new MaterialBuilder().setName("BlueAlloy")
            .setDefaultLocalName("Blue Alloy")
            .setMetaItemSubID(309)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeLightBlue)
            .setRGB(0x64b4ff)
            .addDustItems()
            .addMetalItems()
            .addCentrifugeRecipe()
            .addMaterial(Materials.Silver, 1)
            .addMaterial(Materials.Electrotine, 4)
            .addAspect(TCAspects.ELECTRUM, 3)
            .constructMaterial();
    }

    private static Materials loadConductiveIron() {
        return new MaterialBuilder().setName("ConductiveIron")
            .setDefaultLocalName("Conductive Iron")
            .setMetaItemSubID(369)
            .setIconSet(TextureSet.SET_DARKSTEEL)
            .setColor(Dyes.dyeRed)
            .setRGB(0xffbfc3)
            .setToolSpeed(6.0f)
            .setDurability(256)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.RedstoneAlloy, 1)
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Silver, 1)
            .constructMaterial();
    }

    private static Materials loadCrudeSteel() {
        return new MaterialBuilder().setName("CrudeSteel")
            .setDefaultLocalName("Clay Compound")
            .setMetaItemSubID(402)
            .setIconSet(TextureSet.SET_VIVID)
            .setColor(Dyes.dyeGray)
            .setRGB(0x9e9087)
            .setToolSpeed(2.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1000)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Stone, 1)
            .addMaterial(Materials.Clay, 1)
            .addMaterial(Materials.Flint, 1)
            .constructMaterial();
    }

    private static Materials loadCrystallineAlloy() {
        return new MaterialBuilder().setName("CrystallineAlloy")
            .setDefaultLocalName("Crystalline Alloy")
            .setMetaItemSubID(403)
            .setIconSet(TextureSet.SET_CRYSTALLINE)
            .setColor(Dyes.dyeCyan)
            .setRGB(0x4adbdb)
            .setToolSpeed(18.0f)
            .setDurability(768)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(4500)
            .setBlastFurnaceTemp(4500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Gold, 1)
            .addMaterial(Materials.Diamond, 1)
            .addMaterial(Materials.PulsatingIron, 1)
            .constructMaterial();
    }

    private static Materials loadCrystallinePinkSlime() {
        return new MaterialBuilder().setName("CrystallinePinkSlime")
            .setDefaultLocalName("Crystalline Pink Slime")
            .setMetaItemSubID(406)
            .setIconSet(TextureSet.SET_CRYSTALLINE)
            .setColor(Dyes.dyePink)
            .setRGB(0xe236cb)
            .setToolSpeed(6.0f)
            .setDurability(128)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5000)
            .setBlastFurnaceTemp(5000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.CrystallineAlloy, 1)
            .addMaterial(Materials.Diamond, 1)
            .constructMaterial();
    }

    private static Materials loadDarkSteel() {
        return new MaterialBuilder().setName("DarkSteel")
            .setDefaultLocalName("Dark Steel")
            .setMetaItemSubID(364)
            .setIconSet(TextureSet.SET_DARKSTEEL)
            .setColor(Dyes.dyePurple)
            .setRGB(0x504650)
            .setToolSpeed(8.0f)
            .setDurability(512)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.ElectricalSteel, 1)
            .addMaterial(Materials.Coal, 1)
            .addMaterial(Materials.Obsidian, 1)
            .constructMaterial();
    }

    private static Materials loadElectricalSteel() {
        return new MaterialBuilder().setName("ElectricalSteel")
            .setDefaultLocalName("Electrical Steel")
            .setMetaItemSubID(365)
            .setIconSet(TextureSet.SET_DARKSTEEL)
            .setColor(Dyes.dyeGray)
            .setRGB(0xd8d8d8)
            .setToolSpeed(6.0f)
            .setDurability(512)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(1811)
            .setBlastFurnaceTemp(1000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Steel, 1)
            .addMaterial(Materials.Coal, 1)
            .addMaterial(Materials.Silicon, 1)
            .constructMaterial();
    }

    private static Materials loadEndSteel() {
        return new MaterialBuilder().setName("EndSteel")
            .setDefaultLocalName("End Steel")
            .setMetaItemSubID(401)
            .setIconSet(TextureSet.SET_VIVID)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xdbce7d)
            .setToolSpeed(12.0f)
            .setDurability(2000)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(940)
            .setBlastFurnaceTemp(3600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.DarkSteel, 1)
            .addMaterial(Materials.Tungsten, 1)
            .addMaterial(Materials.Endstone, 1)
            .constructMaterial();
    }

    private static Materials loadEnderium() {
        return new MaterialBuilder().setName("Enderium")
            .setDefaultLocalName("Enderium")
            .setMetaItemSubID(321)
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x599187)
            .setToolSpeed(8.0f)
            .setDurability(1500)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(4500)
            .setBlastFurnaceTemp(4500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.EnderiumBase, 2)
            .addMaterial(Materials.Thaumium, 1)
            .addMaterial(Materials.EnderPearl, 1)
            .addAspect(TCAspects.METALLUM, 2)
            .addAspect(TCAspects.ALIENIS, 1)
            .constructMaterial();
    }

    private static Materials loadEnergeticAlloy() {
        return new MaterialBuilder().setName("EnergeticAlloy")
            .setDefaultLocalName("Energetic Alloy")
            .setMetaItemSubID(366)
            .setIconSet(TextureSet.SET_ENERGETIC)
            .setColor(Dyes.dyeOrange)
            .setRGB(0xff8c19)
            .setToolSpeed(12.0f)
            .setDurability(1024)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(2200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.ConductiveIron, 1)
            .addMaterial(Materials.Gold, 1)
            .addMaterial(Materials.BlackSteel, 1)
            .constructMaterial();
    }

    private static Materials loadEnergeticSilver() {
        return new MaterialBuilder().setName("EnergeticSilver")
            .setDefaultLocalName("Energetic Silver")
            .setMetaItemSubID(407)
            .setIconSet(TextureSet.SET_VIVID)
            .setColor(Dyes.dyeLightBlue)
            .setRGB(0x3887b5)
            .setToolSpeed(8.0f)
            .setDurability(512)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(2200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Silver, 1)
            .addMaterial(Materials.ConductiveIron, 1)
            .addMaterial(Materials.BlackSteel, 1)
            .constructMaterial();
    }

    private static Materials loadMelodicAlloy() {
        return new MaterialBuilder().setName("MelodicAlloy")
            .setDefaultLocalName("Melodic Alloy")
            .setMetaItemSubID(404)
            .setIconSet(TextureSet.SET_MELODIC)
            .setColor(Dyes.dyeMagenta)
            .setRGB(0xc155c1)
            .setToolSpeed(24.0f)
            .setDurability(1024)
            .setToolQuality(5)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(5400)
            .setBlastFurnaceTemp(5400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.EndSteel, 1)
            .addMaterial(Materials.EnderEye, 1)
            .addMaterial(Materials.Oriharukon, 1)
            .constructMaterial();
    }

    private static Materials loadMithril() {
        return new MaterialBuilder().setName("Mithril")
            .setDefaultLocalName("Mithril")
            .setMetaItemSubID(331)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightBlue)
            .setRGB(0xffffd2)
            .setToolSpeed(32.0f)
            .setDurability(64)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .setMeltingPoint(6600)
            .setBlastFurnaceTemp(6600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Platinum, 2)
            .addMaterial(Materials.Thaumium, 1)
            .constructMaterial()
            .setTurbineMultipliers(22, 1, 1);
    }

    private static Materials loadPulsatingIron() {
        return new MaterialBuilder().setName("PulsatingIron")
            .setDefaultLocalName("Pulsating Iron")
            .setMetaItemSubID(378)
            .setIconSet(TextureSet.SET_DARKSTEEL)
            .setColor(Dyes.dyeLime)
            .setRGB(0x80f69b)
            .setToolSpeed(6.0f)
            .setDurability(256)
            .setToolQuality(3)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setBlastFurnaceTemp(1800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.EnderPearl, 1)
            .addMaterial(Materials.RedstoneAlloy, 1)
            .constructMaterial();
    }

    private static Materials loadRedstoneAlloy() {
        return new MaterialBuilder().setName("RedstoneAlloy")
            .setDefaultLocalName("Redstone Alloy")
            .setMetaItemSubID(381)
            .setIconSet(TextureSet.SET_DARKSTEEL)
            .setColor(Dyes.dyeRed)
            .setRGB(0xff4332)
            .setToolSpeed(3.0f)
            .setDurability(128)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(671)
            .setBlastFurnaceTemp(1000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Redstone, 1)
            .addMaterial(Materials.Silicon, 1)
            .addMaterial(Materials.Coal, 1)
            .constructMaterial();
    }

    private static Materials loadShadowIron() {
        return new MaterialBuilder().setName("ShadowIron")
            .setDefaultLocalName("Shadow Iron")
            .setMetaItemSubID(336)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x787878)
            .setToolSpeed(32.0f)
            .setDurability(10240)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .setMeltingPoint(8400)
            .setBlastFurnaceTemp(8400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Iron, 1)
            .addMaterial(Materials.Thaumium, 3)
            .constructMaterial()
            .setTurbineMultipliers(1, 76, 1);
    }

    private static Materials loadShadowSteel() {
        return new MaterialBuilder().setName("ShadowSteel")
            .setDefaultLocalName("Shadow Steel")
            .setMetaItemSubID(337)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x5a5a5a)
            .setToolSpeed(6.0f)
            .setDurability(768)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .setBlastFurnaceTemp(1700)
            .setBlastFurnaceRequired(true)
            .addCentrifugeRecipe()
            .addMaterial(Materials.Steel, 1)
            .addMaterial(Materials.Thaumium, 3)
            .constructMaterial();
    }

    private static Materials loadSoularium() {
        return new MaterialBuilder().setName("Soularium")
            .setDefaultLocalName("Soularium")
            .setMetaItemSubID(379)
            .setIconSet(TextureSet.SET_DARKSTEEL)
            .setColor(Dyes.dyeBrown)
            .setRGB(0x916d3e)
            .setToolSpeed(8.0f)
            .setDurability(256)
            .setToolQuality(2)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(800)
            .setBlastFurnaceTemp(1000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.SoulSand, 1)
            .addMaterial(Materials.Gold, 1)
            .addMaterial(Materials.Ash, 1)
            .constructMaterial();
    }

    private static Materials loadStellarAlloy() {
        return new MaterialBuilder().setName("StellarAlloy")
            .setDefaultLocalName("Stellar Alloy")
            .setMetaItemSubID(405)
            .setIconSet(TextureSet.SET_STELLAR)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xd3ffff)
            .setToolSpeed(96.0f)
            .setDurability(10240)
            .setToolQuality(7)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(7200)
            .setBlastFurnaceTemp(7200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.NetherStar, 1)
            .addMaterial(Materials.MelodicAlloy, 1)
            .addMaterial(Materials.Naquadah, 1)
            .constructMaterial();
    }

    private static Materials loadVibrantAlloy() {
        return new MaterialBuilder().setName("VibrantAlloy")
            .setDefaultLocalName("Vibrant Alloy")
            .setMetaItemSubID(367)
            .setIconSet(TextureSet.SET_VIBRANT)
            .setColor(Dyes.dyeLime)
            .setRGB(0x95e011)
            .setToolSpeed(18.0f)
            .setDurability(4048)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3300)
            .setBlastFurnaceTemp(3300)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.EnergeticAlloy, 1)
            .addMaterial(Materials.EnderEye, 1)
            .addMaterial(Materials.Chrome, 1)
            .constructMaterial();
    }

    private static Materials loadVividAlloy() {
        return new MaterialBuilder().setName("VividAlloy")
            .setDefaultLocalName("Vivid Alloy")
            .setMetaItemSubID(408)
            .setIconSet(TextureSet.SET_VIVID)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x46bcdb)
            .setToolSpeed(12.0f)
            .setDurability(768)
            .setToolQuality(4)
            .addDustItems()
            .addMetalItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(3300)
            .setBlastFurnaceTemp(3300)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.EnergeticSilver, 1)
            .addMaterial(Materials.EnderEye, 1)
            .addMaterial(Materials.Chrome, 1)
            .constructMaterial();
    }

    private static Materials loadInfinityCatalyst() {
        return new MaterialBuilder().setName("InfinityCatalyst")
            .setDefaultLocalName("Infinity Catalyst")
            .setMetaItemSubID(394)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightGray)
            .setToolSpeed(64.0f)
            .setDurability(1310720)
            .setToolQuality(10)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(10800)
            .setBlastFurnaceTemp(10800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .setFuelType(5)
            .setFuelPower(500000)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UV);
    }

    private static Materials loadTrinium() {
        return new MaterialBuilder().setName("Trinium")
            .setDefaultLocalName("Trinium")
            .setMetaItemSubID(868)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLightGray)
            .setRGB(0xc8c8d2)
            .setToolSpeed(128.0f)
            .setDurability(51200)
            .setToolQuality(8)
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .addToolHeadItems()
            .addGearItems()
            .setMeltingPoint(7200)
            .setBlastFurnaceTemp(7200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .constructMaterial();
    }

    private static void loadSuperConductorBases() {
        Materials.Pentacadmiummagnesiumhexaoxid = loadPentacadmiummagnesiumhexaoxid();
        Materials.Titaniumonabariumdecacoppereikosaoxid = loadTitaniumonabariumdecacoppereikosaoxid();
        Materials.Uraniumtriplatinid = loadUraniumtriplatinid();
        Materials.Vanadiumtriindinid = loadVanadiumtriindinid();
        Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid = loadTetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid();
        Materials.Tetranaquadahdiindiumhexaplatiumosminid = loadTetranaquadahdiindiumhexaplatiumosminid();
        Materials.Longasssuperconductornameforuvwire = loadLongasssuperconductornameforuvwire();
        Materials.Longasssuperconductornameforuhvwire = loadLongasssuperconductornameforuhvwire();
        Materials.SuperconductorUEVBase = loadSuperconductorUEVBase();
        Materials.SuperconductorUIVBase = loadSuperconductorUIVBase();
        Materials.SuperconductorUMVBase = loadSuperconductorUMVBase();
    }

    private static Materials loadPentacadmiummagnesiumhexaoxid() {
        return new MaterialBuilder().setName("Pentacadmiummagnesiumhexaoxid")
            .setDefaultLocalName("Superconductor Base MV")
            .setMetaItemSubID(987)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeGray)
            .setRGB(0x555555)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(2500)
            .setBlastFurnaceTemp(2500)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Cadmium, 5)
            .addMaterial(Materials.Magnesium, 1)
            .addMaterial(Materials.Oxygen, 6)
            .addAspect(TCAspects.ELECTRUM, 3)
            .constructMaterial();
    }

    private static Materials loadTitaniumonabariumdecacoppereikosaoxid() {
        return new MaterialBuilder().setName("Titaniumonabariumdecacoppereikosaoxid")
            .setDefaultLocalName("Superconductor Base HV")
            .setMetaItemSubID(988)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBrown)
            .setRGB(0x331900)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(3300)
            .setBlastFurnaceTemp(3300)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Titanium, 1)
            .addMaterial(Materials.Barium, 9)
            .addMaterial(Materials.Copper, 10)
            .addMaterial(Materials.Oxygen, 20)
            .addAspect(TCAspects.ELECTRUM, 6)
            .constructMaterial();
    }

    private static Materials loadUraniumtriplatinid() {
        return new MaterialBuilder().setName("Uraniumtriplatinid")
            .setDefaultLocalName("Superconductor Base EV")
            .setMetaItemSubID(989)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeLime)
            .setRGB(0x008700)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(4400)
            .setBlastFurnaceTemp(4400)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Uranium, 1)
            .addMaterial(Materials.Platinum, 3)
            .addAspect(TCAspects.ELECTRUM, 9)
            .constructMaterial();
    }

    private static Materials loadVanadiumtriindinid() {
        return new MaterialBuilder().setName("Vanadiumtriindinid")
            .setDefaultLocalName("Superconductor Base IV")
            .setMetaItemSubID(990)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeMagenta)
            .setRGB(0x330033)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(5200)
            .setBlastFurnaceTemp(5200)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Vanadium, 1)
            .addMaterial(Materials.Indium, 3)
            .addAspect(TCAspects.ELECTRUM, 12)
            .constructMaterial();
    }

    private static Materials loadTetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid() {
        return new MaterialBuilder().setName("Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid")
            .setDefaultLocalName("Superconductor Base LuV")
            .setMetaItemSubID(991)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBrown)
            .setRGB(0x994c00)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(6000)
            .setBlastFurnaceTemp(6000)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Indium, 4)
            .addMaterial(Materials.Tin, 2)
            .addMaterial(Materials.Barium, 2)
            .addMaterial(Materials.Titanium, 1)
            .addMaterial(Materials.Copper, 7)
            .addMaterial(Materials.Oxygen, 14)
            .addAspect(TCAspects.ELECTRUM, 15)
            .constructMaterial();
    }

    private static Materials loadTetranaquadahdiindiumhexaplatiumosminid() {
        return new MaterialBuilder().setName("Tetranaquadahdiindiumhexaplatiumosminid")
            .setDefaultLocalName("Superconductor Base ZPM")
            .setMetaItemSubID(992)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeBlack)
            .setRGB(0x0a0a0a)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(8100)
            .setBlastFurnaceTemp(8100)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Naquadah, 4)
            .addMaterial(Materials.Indium, 2)
            .addMaterial(Materials.Palladium, 6)
            .addMaterial(Materials.Osmium, 1)
            .addAspect(TCAspects.ELECTRUM, 18)
            .constructMaterial();
    }

    private static Materials loadLongasssuperconductornameforuvwire() {
        return new MaterialBuilder().setName("Longasssuperconductornameforuvwire")
            .setDefaultLocalName("Superconductor Base UV")
            .setMetaItemSubID(986)
            .setIconSet(TextureSet.SET_METALLIC)
            .setColor(Dyes.dyeYellow)
            .setRGB(0xe0d207)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(9900)
            .setBlastFurnaceTemp(9900)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Naquadria, 4)
            .addMaterial(Materials.Osmiridium, 3)
            .addMaterial(Materials.Europium, 1)
            .addMaterial(Materials.Samarium, 1)
            .addAspect(TCAspects.ELECTRUM, 21)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_LuV);
    }

    private static Materials loadLongasssuperconductornameforuhvwire() {
        return new MaterialBuilder().setName("Longasssuperconductornameforuhvwire")
            .setDefaultLocalName("Superconductor Base UHV")
            .setMetaItemSubID(985)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setRGB(0x2681bd)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(10800)
            .setBlastFurnaceTemp(10800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addElectrolyzerRecipe()
            .addMaterial(Materials.Draconium, 6)
            .addMaterial(Materials.CosmicNeutronium, 7)
            .addMaterial(Materials.Tritanium, 5)
            .addMaterial(Materials.Americium, 6)
            .addAspect(TCAspects.ELECTRUM, 24)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_ZPM);
    }

    private static Materials loadSuperconductorUEVBase() {
        return new MaterialBuilder().setName("SuperconductorUEVBase")
            .setDefaultLocalName("Superconductor Base UEV")
            .setMetaItemSubID(974)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xae0808)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(11700)
            .setBlastFurnaceTemp(11800)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.ELECTRUM, 27)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UV);
    }

    private static Materials loadSuperconductorUIVBase() {
        return new MaterialBuilder().setName("SuperconductorUIVBase")
            .setDefaultLocalName("Superconductor Base UIV")
            .setMetaItemSubID(131)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xe558b1)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(12700)
            .setBlastFurnaceTemp(12700)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.ELECTRUM, 34)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UHV);
    }

    private static Materials loadSuperconductorUMVBase() {
        return new MaterialBuilder().setName("SuperconductorUMVBase")
            .setDefaultLocalName("Superconductor Base UMV")
            .setMetaItemSubID(134)
            .setIconSet(TextureSet.SET_SHINY)
            .setColor(Dyes.dyeWhite)
            .setRGB(0xb526cd)
            .addDustItems()
            .addMetalItems()
            .setMeltingPoint(13600)
            .setBlastFurnaceTemp(13600)
            .setBlastFurnaceRequired(true)
            .setAutoGenerateBlastFurnaceRecipes(false)
            .setAutoGeneratedVacuumFreezerRecipe(false)
            .addAspect(TCAspects.ELECTRUM, 40)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UEV);
    }

    private static Materials loadSuperCoolant() {
        return new MaterialBuilder(140, TextureSet.SET_DULL, "Super Coolant").setRGB(2, 91, 111)
            .addCell()
            .addFluid()
            .constructMaterial()
            .setLiquidTemperature(1);
    }

    private static Materials loadEnrichedHolmium() {
        return new MaterialBuilder().setName("EnrichedHolmium")
            .setDefaultLocalName("Enriched Holmium")
            .setMetaItemSubID(582)
            .setIconSet(TextureSet.SET_METALLIC)
            .setToolSpeed(1.0f)
            .setDurability(0)
            .setToolQuality(2)
            .setTypes(2)
            .setARGB(0, 18, 100, 255)
            .setFuelType(-1)
            .setFuelPower(-1)
            .setMeltingPoint(0)
            .setBlastFurnaceTemp(3000)
            .setBlastFurnaceRequired(true)
            .setDensityDivider(1)
            .setDensityMultiplier(1)
            .setColor(Dyes.dyePurple)
            .constructMaterial();
    }

    private static Materials loadTengamPurified() {
        return new MaterialBuilder(111, TextureSet.SET_METALLIC, "Purified Tengam").addDustItems()
            .addGearItems()
            .addMetalItems()
            .addToolHeadItems()
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 2),
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2)))
            .setColor(Dyes.dyeLime)
            .setName("TengamPurified")
            .setRGB(186, 223, 112)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UV);
    }

    private static Materials loadTengamAttuned() {
        return new MaterialBuilder(112, TextureSet.SET_MAGNETIC, "Attuned Tengam").addDustItems()
            .addGearItems()
            .addMetalItems()
            .addToolHeadItems()
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 4),
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1)))
            .setColor(Dyes.dyeLime)
            .setName("TengamAttuned")
            .setRGB(213, 255, 128)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UV);
    }

    private static Materials loadTengamRaw() {
        return new MaterialBuilder(110, TextureSet.SET_ROUGH, "Raw Tengam").addOreItems()
            .setAspects(
                Arrays.asList(
                    new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 1),
                    new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 4)))
            .setColor(Dyes.dyeLime)
            .setName("TengamRaw")
            .setRGB(160, 191, 96)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_UV);
    }

    private static Materials loadActivatedCarbon() {
        return new MaterialBuilder(563, TextureSet.SET_DULL, "Activated Carbon").addDustItems()
            .setRGB(20, 20, 20)
            .setName("ActivatedCarbon")
            .setMaterialList(new MaterialStack(Carbon, 1))
            .constructMaterial()
            .disableAutoGeneratedRecycleRecipes();
    }

    private static Materials loadPreActivatedCarbon() {
        return new MaterialBuilder(564, TextureSet.SET_DULL, "Pre-Activated Carbon").addDustItems()
            .setRGB(15, 51, 65)
            .setName("PreActivatedCarbon")
            .setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(PhosphoricAcid, 1))
            .constructMaterial()
            .disableAutoGeneratedRecycleRecipes();
    }

    private static Materials loadDirtyActivatedCarbon() {
        return new MaterialBuilder(565, TextureSet.SET_DULL, "Dirty Activated Carbon").addDustItems()
            .setRGB(110, 110, 110)
            .setName("carbonactivateddirty") // don't change this to the more sensible name or a centrifuge recipe
                                             // appears
            .setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(PhosphoricAcid, 1))
            .constructMaterial()
            .disableAutoGeneratedRecycleRecipes();
    }

    private static Materials loadPolyAluminiumChloride() {
        return new MaterialBuilder(566, TextureSet.SET_FLUID, "Polyaluminium Chloride").addFluid()
            .addCell()
            .setRGB(252, 236, 5)
            .setName("PolyaluminiumChloride")
            .constructMaterial();
    }

    private static Materials loadOzone() {
        return new MaterialBuilder(568, TextureSet.SET_FLUID, "Ozone").addGas()
            .addCell()
            .setRGB(190, 244, 250)
            .setName("Ozone")
            .setMaterialList(new MaterialStack(Oxygen, 3))
            .constructMaterial();
    }

    private static Materials loadStableBaryonicMatter() {
        return new MaterialBuilder(569, new TextureSet("stablebaryonicmatter", true), "Stabilised Baryonic Matter")
            .addFluid()
            .addCell()
            .setRGBA(255, 255, 255, 0)
            .setTransparent(true)
            .setName("stablebaryonicmatter")
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadRawRadox() {
        return new MaterialBuilder(-1, TextureSet.SET_DULL, "Raw Radox").setRGB(80, 30, 80)
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadSuperLightRadox() {
        return new MaterialBuilder(-1, TextureSet.SET_DULL, "Super Light Radox").setRGB(155, 0, 155)
            .addGas()
            .constructMaterial();
    }

    private static Materials loadLightRadox() {
        return new MaterialBuilder(-1, TextureSet.SET_DULL, "Light Radox").setRGB(140, 0, 140)
            .addGas()
            .constructMaterial();
    }

    private static Materials loadHeavyRadox() {
        return new MaterialBuilder(-1, TextureSet.SET_DULL, "Heavy Radox").setRGB(115, 0, 115)
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadSuperHeavyRadox() {
        return new MaterialBuilder(-1, TextureSet.SET_DULL, "Super Heavy Radox").setRGB(100, 0, 100)
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadXenoxene() {
        return new MaterialBuilder(-1, TextureSet.SET_DULL, "Xenoxene").setRGB(133, 130, 128)
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadDilutedXenoxene() {
        return new MaterialBuilder(-1, TextureSet.SET_DULL, "Diluted Xenoxene").setRGB(206, 200, 196)
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadCrackedRadox() {
        return new MaterialBuilder(-1, TextureSet.SET_DULL, "Cracked Radox").setRGB(180, 130, 180)
            .addGas()
            .constructMaterial();
    }

    private static Materials loadRadoxGas() {
        return new MaterialBuilder(-1, TextureSet.SET_DULL, "Radox Gas").setRGB(255, 130, 255)
            .addGas()
            .constructMaterial();
    }

    private static Materials loadRadoxPolymer() {
        return new Materials(
            979,
            TextureSet.SET_DULL,
            8.0F,
            346,
            3,
            1 | 2 | 16,
            133,
            0,
            128,
            0,
            "RadoxPoly",
            "Radox Polymer",
            0,
            0,
            6203,
            0,
            false,
            false,
            1,
            1,
            1,
            Dyes.dyePurple,
            0,
            Arrays.asList(
                new MaterialStack(Materials.Carbon, 14),
                new MaterialStack(Materials.Osmium, 11),
                new MaterialStack(Materials.Oxygen, 7),
                new MaterialStack(Materials.Silver, 3),
                new MaterialStack(Materials.CallistoIce, 1)),
            Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.HUMANUS, 2))).setHasCorrespondingGas(true)
                .setGasTemperature(12406);
    }

    private static Materials loadNetherAir() {
        return new MaterialBuilder(118, TextureSet.SET_FLUID, "Nether Air").addFluid()
            .addCell()
            .setRGB(238, 163, 154)
            .setName("netherair")
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadNethersemifluid() {
        return new MaterialBuilder(119, TextureSet.SET_FLUID, "Nether Semi-Fluid").addFluid()
            .addCell()
            .setRGB(218, 193, 114)
            .setName("nethersemifluid")
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadNefariousGas() {
        return new MaterialBuilder(120, TextureSet.SET_FLUID, "Nefarious Gas").addFluid()
            .addCell()
            .setRGB(48, 10, 5)
            .setName("nefariousgas")
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadNefariousOil() {
        return new MaterialBuilder(122, TextureSet.SET_FLUID, "Nefarious Oil").addFluid()
            .addCell()
            .setRGB(57, 22, 22)
            .setName("nefariousoil")
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadPoorNetherWaste() {
        return new MaterialBuilder(123, TextureSet.SET_FLUID, "Poor Nether Waste").addFluid()
            .addCell()
            .setRGB(160, 130, 126)
            .setName("poornetherwaste")
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadRichNetherWaste() {
        return new MaterialBuilder(124, TextureSet.SET_FLUID, "Rich Nether Waste").addFluid()
            .addCell()
            .setRGB(2490, 130, 126)
            .setName("richnetherwaste")
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadHellishMetal() {
        return new Materials(
            125,
            TextureSet.SET_FIERY,
            1.0F,
            0,
            2,
            2,
            170,
            170,
            170,
            255,
            "HellishMetal",
            "Hellish Metal",
            -1,
            -1,
            1200,
            1900,
            false,
            false,
            200,
            1,
            1,
            Dyes.dyeLightGray).disableAutoGeneratedVacuumFreezerRecipe()
                .disableAutoGeneratedBlastFurnaceRecipes();
    }

    private static Materials loadNetherite() {
        return new Materials(
            132,
            new TextureSet("netherite", true),
            1.0F,
            0,
            16,
            2 | 128,
            255,
            255,
            255,
            255,
            "Netherite",
            "Netherite",
            -1,
            -1,
            1200,
            1900,
            true,
            false,
            200,
            1,
            1,
            Dyes.dyeLightGray).disableAutoGeneratedBlastFurnaceRecipes()
                .disableAutoGeneratedVacuumFreezerRecipe()
                .setProcessingMaterialTierEU(TierEU.RECIPE_ZPM);
    }

    private static Materials loadActivatedNetherite() {
        return new MaterialBuilder(133, TextureSet.SET_METALLIC, "Activated Netherite").addFluid()
            .addCell()
            .setRGB(156, 87, 90)
            .setName("activatednetherite")
            .constructMaterial();
    }

    private static Materials loadPrismarineSolution() {
        return new MaterialBuilder(135, TextureSet.SET_METALLIC, "Prismarine Solution").addFluid()
            .addCell()
            .setRGB(85, 154, 138)
            .setName("prismarinesolution")
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadPrismarinecontaminatedhydrogenperoxide() {
        return new MaterialBuilder(136, TextureSet.SET_METALLIC, "Prismarine-Contaminated Hydrogen Peroxide").addFluid()
            .addCell()
            .setRGB(68, 95, 89)
            .setName("prismarinecontaminatedhydrogenperoxide")
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadPrismarinerichnitrobenzenesolution() {
        return new MaterialBuilder(137, TextureSet.SET_METALLIC, "Prismarine-Rich Nitrobenzene Solution").addFluid()
            .addCell()
            .setRGB(93, 118, 63)
            .setName("prismarinerichnitrobenzenesolution")
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadPrismarinecontaminatednitrobenzenesolution() {
        return new MaterialBuilder(138, TextureSet.SET_METALLIC, "Prismarine-Contaminated Nitrobenzene Solution")
            .addFluid()
            .addCell()
            .setRGB(47, 51, 30)
            .setName("prismarinecontaminatednitrobenzenesolution")
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadPrismaticGas() {
        return new MaterialBuilder(161, TextureSet.SET_METALLIC, "Prismatic Gas").addFluid()
            .addCell()
            .setRGB(118, 186, 189)
            .setName("prismaticgas")
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadPrismaticAcid() {
        return new MaterialBuilder(162, new TextureSet("prismaticacid", true), "Prismatic Acid").addFluid()
            .addCell()
            .setRGBA(255, 255, 255, 0)
            .setTransparent(true)
            .setName("prismaticacid")
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadPrismaticNaquadah() {
        return new MaterialBuilder(163, TextureSet.SET_METALLIC, "Prismatic Naquadah").setName("prismaticnaquadah")
            .setTypes(1 | 2)
            .setRGBA(55, 55, 55, 0)
            .setTransparent(false)
            .constructMaterial()
            .setProcessingMaterialTierEU(TierEU.RECIPE_ZPM);
    }

    private static Materials loadPrismaticNaquadahCompositeSlurry() {
        return new MaterialBuilder(164, TextureSet.SET_FLUID, "Prismatic Naquadah Composite Slurry").addFluid()
            .addCell()
            .setRGBA(75, 75, 75, 0)
            .setTransparent(true)
            .setName("prismaticnaquadahcompositeslurry")
            .constructMaterial()
            .setHasCorrespondingFluid(true);
    }

    private static Materials loadEntropicCatalyst() {
        return new MaterialBuilder(899, TextureSet.SET_DULL, "Entropic Catalyst").setRGB(0xa99da5)
            .addFluid()
            .addCell()
            .constructMaterial();
    }

    private static Materials loadComplexityCatalyst() {
        return new MaterialBuilder(897, TextureSet.SET_DULL, "Complexity Catalyst").setRGB(0x8b93a9)
            .addFluid()
            .addCell()
            .constructMaterial();
    }
}
