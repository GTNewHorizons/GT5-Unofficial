package gregtech.loaders.materials;

import static gregtech.api.enums.Materials.Almandine;
import static gregtech.api.enums.Materials.Aluminium;
import static gregtech.api.enums.Materials.Americium;
import static gregtech.api.enums.Materials.Andradite;
import static gregtech.api.enums.Materials.Antimony;
import static gregtech.api.enums.Materials.Ardite;
import static gregtech.api.enums.Materials.Arsenic;
import static gregtech.api.enums.Materials.Asbestos;
import static gregtech.api.enums.Materials.Ash;
import static gregtech.api.enums.Materials.BandedIron;
import static gregtech.api.enums.Materials.Barium;
import static gregtech.api.enums.Materials.Basalt;
import static gregtech.api.enums.Materials.Beryllium;
import static gregtech.api.enums.Materials.Biotite;
import static gregtech.api.enums.Materials.Bismuth;
import static gregtech.api.enums.Materials.BismuthBronze;
import static gregtech.api.enums.Materials.BlackBronze;
import static gregtech.api.enums.Materials.BlackSteel;
import static gregtech.api.enums.Materials.Blaze;
import static gregtech.api.enums.Materials.Blizz;
import static gregtech.api.enums.Materials.Boron;
import static gregtech.api.enums.Materials.Brass;
import static gregtech.api.enums.Materials.Brick;
import static gregtech.api.enums.Materials.Butadiene;
import static gregtech.api.enums.Materials.Cadmium;
import static gregtech.api.enums.Materials.Caesium;
import static gregtech.api.enums.Materials.Calcite;
import static gregtech.api.enums.Materials.Calcium;
import static gregtech.api.enums.Materials.Carbon;
import static gregtech.api.enums.Materials.Cerium;
import static gregtech.api.enums.Materials.CertusQuartz;
import static gregtech.api.enums.Materials.Chlorine;
import static gregtech.api.enums.Materials.Chrome;
import static gregtech.api.enums.Materials.Clay;
import static gregtech.api.enums.Materials.Coal;
import static gregtech.api.enums.Materials.CoalFuel;
import static gregtech.api.enums.Materials.Cobalt;
import static gregtech.api.enums.Materials.ConductiveIron;
import static gregtech.api.enums.Materials.Copper;
import static gregtech.api.enums.Materials.CosmicNeutronium;
import static gregtech.api.enums.Materials.CrystallineAlloy;
import static gregtech.api.enums.Materials.DarkAsh;
import static gregtech.api.enums.Materials.DarkSteel;
import static gregtech.api.enums.Materials.Diamond;
import static gregtech.api.enums.Materials.Draconium;
import static gregtech.api.enums.Materials.ElectricalSteel;
import static gregtech.api.enums.Materials.Electrotine;
import static gregtech.api.enums.Materials.Electrum;
import static gregtech.api.enums.Materials.EndSteel;
import static gregtech.api.enums.Materials.EnderEye;
import static gregtech.api.enums.Materials.EnderPearl;
import static gregtech.api.enums.Materials.EnderiumBase;
import static gregtech.api.enums.Materials.Endstone;
import static gregtech.api.enums.Materials.EnergeticAlloy;
import static gregtech.api.enums.Materials.EnergeticSilver;
import static gregtech.api.enums.Materials.Epoxid;
import static gregtech.api.enums.Materials.Europium;
import static gregtech.api.enums.Materials.Flint;
import static gregtech.api.enums.Materials.Fluorine;
import static gregtech.api.enums.Materials.Gallium;
import static gregtech.api.enums.Materials.GarnetRed;
import static gregtech.api.enums.Materials.GarnetYellow;
import static gregtech.api.enums.Materials.Glass;
import static gregtech.api.enums.Materials.Glyceryl;
import static gregtech.api.enums.Materials.Gold;
import static gregtech.api.enums.Materials.GraniteBlack;
import static gregtech.api.enums.Materials.Grossular;
import static gregtech.api.enums.Materials.HSSG;
import static gregtech.api.enums.Materials.Hydrogen;
import static gregtech.api.enums.Materials.Indium;
import static gregtech.api.enums.Materials.Iridium;
import static gregtech.api.enums.Materials.Iron;
import static gregtech.api.enums.Materials.Lazurite;
import static gregtech.api.enums.Materials.Lead;
import static gregtech.api.enums.Materials.Lithium;
import static gregtech.api.enums.Materials.LiveRoot;
import static gregtech.api.enums.Materials.Magic;
import static gregtech.api.enums.Materials.Magnesium;
import static gregtech.api.enums.Materials.Magnetite;
import static gregtech.api.enums.Materials.Manganese;
import static gregtech.api.enums.Materials.MelodicAlloy;
import static gregtech.api.enums.Materials.Mercury;
import static gregtech.api.enums.Materials.Molybdenum;
import static gregtech.api.enums.Materials.Naquadah;
import static gregtech.api.enums.Materials.Naquadria;
import static gregtech.api.enums.Materials.Neodymium;
import static gregtech.api.enums.Materials.NetherStar;
import static gregtech.api.enums.Materials.Nickel;
import static gregtech.api.enums.Materials.Niobium;
import static gregtech.api.enums.Materials.Nitrogen;
import static gregtech.api.enums.Materials.Obsidian;
import static gregtech.api.enums.Materials.Olivine;
import static gregtech.api.enums.Materials.Oriharukon;
import static gregtech.api.enums.Materials.Osmiridium;
import static gregtech.api.enums.Materials.Osmium;
import static gregtech.api.enums.Materials.Oxygen;
import static gregtech.api.enums.Materials.Palladium;
import static gregtech.api.enums.Materials.Phosphate;
import static gregtech.api.enums.Materials.PhosphoricAcid;
import static gregtech.api.enums.Materials.Phosphorus;
import static gregtech.api.enums.Materials.Platinum;
import static gregtech.api.enums.Materials.Potassium;
import static gregtech.api.enums.Materials.PotassiumFeldspar;
import static gregtech.api.enums.Materials.PulsatingIron;
import static gregtech.api.enums.Materials.Pyrite;
import static gregtech.api.enums.Materials.Pyrope;
import static gregtech.api.enums.Materials.Quartzite;
import static gregtech.api.enums.Materials.RareEarth;
import static gregtech.api.enums.Materials.Redstone;
import static gregtech.api.enums.Materials.RedstoneAlloy;
import static gregtech.api.enums.Materials.RoseGold;
import static gregtech.api.enums.Materials.Ruby;
import static gregtech.api.enums.Materials.Rutile;
import static gregtech.api.enums.Materials.Saltpeter;
import static gregtech.api.enums.Materials.Samarium;
import static gregtech.api.enums.Materials.Sapphire;
import static gregtech.api.enums.Materials.Silicon;
import static gregtech.api.enums.Materials.SiliconDioxide;
import static gregtech.api.enums.Materials.Silver;
import static gregtech.api.enums.Materials.Snow;
import static gregtech.api.enums.Materials.Sodalite;
import static gregtech.api.enums.Materials.Sodium;
import static gregtech.api.enums.Materials.SoulSand;
import static gregtech.api.enums.Materials.Spessartine;
import static gregtech.api.enums.Materials.Steel;
import static gregtech.api.enums.Materials.SterlingSilver;
import static gregtech.api.enums.Materials.Stone;
import static gregtech.api.enums.Materials.Styrene;
import static gregtech.api.enums.Materials.Sulfur;
import static gregtech.api.enums.Materials.SulfuricAcid;
import static gregtech.api.enums.Materials.Tantalum;
import static gregtech.api.enums.Materials.Thaumium;
import static gregtech.api.enums.Materials.Thorium;
import static gregtech.api.enums.Materials.Tin;
import static gregtech.api.enums.Materials.Titanium;
import static gregtech.api.enums.Materials.Tritanium;
import static gregtech.api.enums.Materials.Tungsten;
import static gregtech.api.enums.Materials.TungstenSteel;
import static gregtech.api.enums.Materials.Uraninite;
import static gregtech.api.enums.Materials.Uranium;
import static gregtech.api.enums.Materials.Uvarovite;
import static gregtech.api.enums.Materials.Vanadium;
import static gregtech.api.enums.Materials.Water;
import static gregtech.api.enums.Materials.Wood;
import static gregtech.api.enums.Materials.Yttrium;
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
        loadNotExact();
        loadTODOThis();

        Materials.Methane = loadMethane();
        Materials.CarbonDioxide = loadCarbonDioxide();
        Materials.NobleGases = loadNobleGases();
        Materials.Air = loadAir();
        Materials.LiquidAir = loadLiquidAir();
        Materials.LiquidNitrogen = loadLiquidNitrogen();
        Materials.LiquidOxygen = loadLiquidOxygen();
        Materials.SiliconDioxide = loadSiliconDioxide();
        Materials.Jasper = loadJasper();
        Materials.Almandine = loadAlmandine();
        Materials.Andradite = loadAndradite();
        Materials.AnnealedCopper = loadAnnealedCopper();

        // spotless:off
        Materials.Asbestos                = new Materials( 946, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   , 230, 230, 230,   0,   "Asbestos"                ,   "Asbestos"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Magnesium, 3), new MaterialStack(Silicon, 2), new MaterialStack(Hydrogen, 4), new MaterialStack(Oxygen, 9))); // Mg3Si2O5(OH)4
        Materials.Ash                     = new Materials( 815, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1                         , 150, 150, 150,   0,   "Ash"                     ,   "Ashes"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , 0, Collections.singletonList(new MaterialStack(Carbon, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 1)));
        Materials.BandedIron              = new Materials( 917, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 145,  90,  90,   0,   "BandedIron"              ,   "Banded Iron"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       , 1, Arrays.asList(new MaterialStack(Iron, 2), new MaterialStack(Oxygen, 3)));
        Materials.BatteryAlloy            = new Materials( 315, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1|2                       , 156, 124, 160,   0,   "BatteryAlloy"            ,   "Battery Alloy"                 ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyePurple      , 2, Arrays.asList(new MaterialStack(Lead, 4), new MaterialStack(Antimony, 1)));
        Materials.BlueTopaz               = new Materials( 513, TextureSet.SET_GEM_HORIZONTAL    ,   7.0F,    256,  3, 1  |4|8      |64          ,   0,   0, 255, 127,   "BlueTopaz"               ,   "Blue Topaz"                    ,    0,       0,         -1,    0, false,  true,   3,   1,   1, Dyes.dyeBlue        , 0, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 1), new MaterialStack(Fluorine, 2), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 6)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 6), new TCAspects.TC_AspectStack(TCAspects.VITREUS, 4)));
        Materials.Bone                    = new Materials( 806, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1                         , 250, 250, 250,   0,   "Bone"                    ,   "Bone"                          ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Collections.singletonList(new MaterialStack(Calcium, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.MORTUUS, 2), new TCAspects.TC_AspectStack(TCAspects.CORPUS, 1)));
        Materials.Brass                   = new Materials( 301, TextureSet.SET_METALLIC          ,   7.0F,     96,  1, 1|2          |64|128      , 255, 180,   0,   0,   "Brass"                   ,   "Brass"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Zinc, 1), new MaterialStack(Copper, 3)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 1)));
        Materials.Bronze                  = new Materials( 300, TextureSet.SET_METALLIC          ,   6.0F,    192,  2, 1|2          |64|128      , 255, 128,   0,   0,   "Bronze"                  ,   "Bronze"                        ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 2, Arrays.asList(new MaterialStack(Tin, 1), new MaterialStack(Copper, 3)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 1)));
        Materials.BrownLimonite           = new Materials( 930, TextureSet.SET_METALLIC          ,   1.0F,      0,  1, 1    |8                   , 200, 100,   0,   0,   "BrownLimonite"           ,   "Brown Limonite"                ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       , 2, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Hydrogen, 1), new MaterialStack(Oxygen, 2))); // FeO(OH)
        Materials.Calcite                 = new Materials( 823, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   , 250, 230, 220,   0,   "Calcite"                 ,   "Calcite"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 1, Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 3)));
        Materials.Cassiterite             = new Materials( 824, TextureSet.SET_METALLIC          ,   1.0F,      0,  3, 1    |8                   , 220, 220, 220,   0,   "Cassiterite"             ,   "Cassiterite"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Tin, 1), new MaterialStack(Oxygen, 2)));
        Materials.CassiteriteSand         = new Materials( 937, TextureSet.SET_SAND              ,   1.0F,      0,  1, 1    |8                   , 220, 220, 220,   0,   "CassiteriteSand"         ,   "Cassiterite Sand"              ,    0,       0,         -1,    0, false, false,   4,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Tin, 1), new MaterialStack(Oxygen, 2)));
        Materials.Chalcopyrite            = new Materials( 855, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   , 160, 120,  40,   0,   "Chalcopyrite"            ,   "Chalcopyrite"                  ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      , 1, Arrays.asList(new MaterialStack(Copper, 1), new MaterialStack(Iron, 1), new MaterialStack(Sulfur, 2)));
        Materials.Charcoal                = new Materials( 536, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1  |4                     , 100,  70,  70,   0,   "Charcoal"                ,   "Charcoal"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 1, Collections.singletonList(new MaterialStack(Carbon, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2), new TCAspects.TC_AspectStack(TCAspects.IGNIS, 2)));
        Materials.Chromite                = new Materials( 825, TextureSet.SET_METALLIC          ,   1.0F,      0,  1, 1    |8                   ,  35,  20,  15,   0,   "Chromite"                ,   "Chromite"                      ,    0,       0,       1700, 1700,  true, false,   6,   1,   1, Dyes.dyePink        , 1, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Chrome, 2), new MaterialStack(Oxygen, 4)));
        Materials.ChromiumDioxide         = new Materials( 361, TextureSet.SET_DULL              ,  11.0F,    256,  3, 1|2                       , 230, 200, 200,   0,   "ChromiumDioxide"         ,   "Chromium Dioxide"              ,    0,       0,        650,  650, false, false,   5,   1,   1, Dyes.dyePink        , 1, Arrays.asList(new MaterialStack(Chrome, 1), new MaterialStack(Oxygen, 2)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1)));
        Materials.Cinnabar                = new Materials( 826, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1    |8                   , 150,   0,   0,   0,   "Cinnabar"                ,   "Cinnabar"                      ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeBrown       , 2, Arrays.asList(new MaterialStack(Mercury, 1), new MaterialStack(Sulfur, 1)));
        Materials.Water                   = new Materials( 701, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,   0,   0, 255,   0,   "Water"                   ,   "Water"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlue        , 0, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 2)));
        Materials.Steam = Materials.Water;
        Materials.Clay                    = new Materials( 805, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1                         , 200, 200, 220,   0,   "Clay"                    ,   "Clay"                          ,    0,       0,         -1,    0, false, false,   5,   1,   1, Dyes.dyeLightBlue   , 0, Arrays.asList(new MaterialStack(Sodium, 2), new MaterialStack(Lithium, 1), new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 2),new MaterialStack(Oxygen,7),new MaterialStack(Water,2)));
        Materials.Coal                    = new Materials( 535, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1  |4|8                   ,  70,  70,  70,   0,   "Coal"                    ,   "Coal"                          ,    0,       0,         -1,    0, false, false,   2,   2,   1, Dyes.dyeBlack       , 1, Collections.singletonList(new MaterialStack(Carbon, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2), new TCAspects.TC_AspectStack(TCAspects.IGNIS, 2)));
        Materials.Cobaltite               = new Materials( 827, TextureSet.SET_METALLIC          ,   1.0F,      0,  1, 1    |8                   ,  80,  80, 250,   0,   "Cobaltite"               ,   "Cobaltite"                     ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeBlue        , 1, Arrays.asList(new MaterialStack(Cobalt, 1), new MaterialStack(Arsenic, 1), new MaterialStack(Sulfur, 1)));
        Materials.Cooperite               = new Materials( 828, TextureSet.SET_METALLIC          ,   1.0F,      0,  1, 1    |8                   , 255, 255, 200,   0,   "Cooperite"               ,   "Sheldonite"                    ,    0,       0,         -1,    0, false, false,   5,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Platinum, 3), new MaterialStack(Nickel, 1), new MaterialStack(Sulfur, 1), new MaterialStack(Palladium, 1)));
        Materials.Cupronickel             = new Materials( 310, TextureSet.SET_METALLIC          ,   6.0F,     64,  1, 1|2          |64          , 227, 150, 128,   0,   "Cupronickel"             ,   "Cupronickel"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 2, Arrays.asList(new MaterialStack(Copper, 1), new MaterialStack(Nickel, 1)));
        Materials.DarkAsh                 = new Materials( 816, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1                         ,  50,  50,  50,   0,   "DarkAsh"                 ,   "Dark Ashes"                    ,    0,       0,         -1,    0, false, false,   1,   2,   1, Dyes.dyeGray        , 1, Collections.singletonList(new MaterialStack(Carbon, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1), new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 1)));
        Materials.DeepIron                = new Materials( 829, TextureSet.SET_METALLIC          ,   6.0F,    384,  2, 1|2  |8      |64          , 150, 140, 140,   0,   "DeepIron"                ,   "Deep Iron"                     ,    0,       0,       7500, 7500,  true, false,   3,   1,   1, Dyes.dyePink        , 0, Collections.singletonList(new MaterialStack(Iron, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.Diamond                 = new Materials( 500, TextureSet.SET_DIAMOND           ,   8.0F,   1280,  4, 1  |4|8      |64|128      , 200, 255, 255, 127,   "Diamond"                 ,   "Diamond"                       ,    0,       0,         -1,    0, false,  true,   5,  64,   1, Dyes.dyeWhite       , 1, Collections.singletonList(new MaterialStack(Carbon, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.VITREUS, 3), new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 4)));
        Materials.Electrum                = new Materials( 303, TextureSet.SET_SHINY             ,  12.0F,     64,  2, 1|2  |8      |64|128      , 255, 255, 100,   0,   "Electrum"                ,   "Electrum"                      ,    0,       0,         -1,    0, false, false,   4,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Silver, 1), new MaterialStack(Gold, 1)));
        Materials.Emerald                 = new Materials( 501, TextureSet.SET_EMERALD           ,   7.0F,    256,  4, 1  |4|8      |64          ,  80, 255,  80, 127,   "Emerald"                 ,   "Emerald"                       ,    0,       0,         -1,    0, false,  true,   5,   1,   1, Dyes.dyeGreen       , 0, Arrays.asList(new MaterialStack(Beryllium, 3), new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 6), new MaterialStack(Oxygen, 18)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.VITREUS, 3), new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 5)));
        Materials.FreshWater              = new Materials(  -1, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,   0,   0, 255,   0,   "FreshWater"              ,   "Fresh Water"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlue        , 0, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 2)));
        Materials.Galena                  = new Materials( 830, TextureSet.SET_DULL              ,   1.0F,      0,  3, 1 |8                      , 100,  60, 100,   0,   "Galena"                  ,   "Galena"                        ,    0,       0,         -1,    0, false, false,   4,   1,   1, Dyes.dyePurple      , 1, Arrays.asList(new MaterialStack(Lead, 1), new MaterialStack(Sulfur, 1)));
        Materials.Garnierite              = new Materials( 906, TextureSet.SET_METALLIC          ,   1.0F,      0,  3, 1    |8                   ,  50, 200,  70,   0,   "Garnierite"              ,   "Garnierite"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightBlue   , 1, Arrays.asList(new MaterialStack(Nickel, 1), new MaterialStack(Oxygen, 1)));
        Materials.Glyceryl                = new Materials( 714, TextureSet.SET_FLUID             ,   1.0F,      0,  1,         16                ,   0, 150, 150,   0,   "Glyceryl"                ,   "Glyceryl Trinitrate"           ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeCyan        , 1, Arrays.asList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 5), new MaterialStack(Nitrogen, 3), new MaterialStack(Oxygen, 9)));
        Materials.GreenSapphire           = new MaterialBuilder(504, TextureSet.SET_GEM_HORIZONTAL, "Green Sapphire").setToolSpeed(7.0F).setDurability(256).setToolQuality(2).addDustItems().addGemItems().setTransparent(true).addOreItems().addToolHeadItems().setRGBA(100, 200, 130, 127).setColor(Dyes.dyeCyan).setExtraData(0).setMaterialList(new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3)).setAspects(Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 5), new TCAspects.TC_AspectStack(TCAspects.VITREUS, 3))).constructMaterial().disableAutoGeneratedBlastFurnaceRecipes();
        Materials.Grossular               = new Materials( 831, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1    |8                   , 200, 100,   0,   0,   "Grossular"               ,   "Grossular"                     ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeOrange      , 0, Arrays.asList(new MaterialStack(Calcium, 3), new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12)));
        Materials.HolyWater               = new Materials( 729, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,   0,   0, 255,   0,   "HolyWater"               ,   "Holy Water"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlue        , 0, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 2), new TCAspects.TC_AspectStack(TCAspects.AURAM, 1)));
        Materials.Ice                     = new Materials( 702, TextureSet.SET_SHINY             ,   1.0F,      0,  0, 1|      16                , 200, 200, 255,   0,   "Ice"                     ,   "Ice"                           ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlue        , 0, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.GELUM, 2)));
        Materials.Ilmenite                = new Materials( 918, TextureSet.SET_METALLIC          ,   1.0F,      0,  3, 1    |8                   ,  70,  55,  50,   0,   "Ilmenite"                ,   "Ilmenite"                      ,    0,       0,         -1,    0, false, false,   1,   2,   1, Dyes.dyePurple      , 0, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Titanium, 1), new MaterialStack(Oxygen, 3)));
        Materials.Rutile                  = new Materials( 375, TextureSet.SET_GEM_HORIZONTAL    ,   1.0F,      0,  2, 1    |8                   , 212,  13,  92,   0,   "Rutile"                  ,   "Rutile"                        ,    0,       0,         -1,    0, false, false,   1,   2,   1, Dyes.dyeRed         , 0, Arrays.asList(new MaterialStack(Titanium, 1), new MaterialStack(Oxygen, 2)));
        Materials.Bauxite                 = new Materials( 822, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   , 200, 100,   0,   0,   "Bauxite"                 ,   "Bauxite"                       ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeBrown       , 1, Arrays.asList(new MaterialStack(Rutile, 2), new MaterialStack(Aluminium, 16), new MaterialStack(Hydrogen, 10), new MaterialStack(Oxygen, 11)));
        Materials.Titaniumtetrachloride   = new Materials( 376, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16                , 212,  13,  92,   0,   "Titaniumtetrachloride"   ,   "Titaniumtetrachloride"         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeRed         , 0, Arrays.asList(new MaterialStack(Titanium, 1), new MaterialStack(Chlorine, 4)));
        Materials.Magnesiumchloride       = new Materials( 377, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1|16                      , 212,  13,  92,   0,   "Magnesiumchloride"       ,   "Magnesiumchloride"             ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeRed         , 0, Arrays.asList(new MaterialStack(Magnesium, 1), new MaterialStack(Chlorine, 2)));
        Materials.Invar                   = new Materials( 302, TextureSet.SET_METALLIC          ,   6.0F,    256,  2, 1|2          |64|128      , 180, 180, 120,   0,   "Invar"                   ,   "Invar"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       , 2, Arrays.asList(new MaterialStack(Iron, 2), new MaterialStack(Nickel, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.GELUM, 1)));
        Materials.Kanthal                 = new Materials( 312, TextureSet.SET_METALLIC          ,   6.0F,     64,  2, 1|2          |64          , 194, 210, 223,   0,   "Kanthal"                 ,   "Kanthal"                       ,    0,       0,       1800, 1800,  true, false,   1,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Aluminium, 1), new MaterialStack(Chrome, 1))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.Lazurite                = new Materials( 524, TextureSet.SET_LAPIS             ,   1.0F,      0,  1, 1  |4|8                   , 100, 120, 255,   0,   "Lazurite"                ,   "Lazurite"                      ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeCyan        , 1, Arrays.asList(new MaterialStack(Aluminium, 6), new MaterialStack(Silicon, 6), new MaterialStack(Calcium, 8), new MaterialStack(Sodium, 8)));
        Materials.Magnalium               = new Materials( 313, TextureSet.SET_DULL              ,   6.0F,    256,  2, 1|2          |64|128      , 200, 190, 255,   0,   "Magnalium"               ,   "Magnalium"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightBlue   , 2, Arrays.asList(new MaterialStack(Magnesium, 1), new MaterialStack(Aluminium, 2)));
        Materials.Magnesite               = new Materials( 908, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   , 250, 250, 180,   0,   "Magnesite"               ,   "Magnesite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyePink        , 1, Arrays.asList(new MaterialStack(Magnesium, 1), new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 3)));
        Materials.Magnetite               = new Materials( 870, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   ,  30,  30,  30,   0,   "Magnetite"               ,   "Magnetite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeGray        , 1, Arrays.asList(new MaterialStack(Iron, 3), new MaterialStack(Oxygen, 4)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 1)));
        Materials.Molybdenite             = new Materials( 942, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   ,  25,  25,  25,   0,   "Molybdenite"             ,   "Molybdenite"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlue        , 1, Arrays.asList(new MaterialStack(Molybdenum, 1), new MaterialStack(Sulfur, 2))); // MoS2 (also source of Re)
        Materials.Nichrome                = new Materials( 311, TextureSet.SET_METALLIC          ,   6.0F,     64,  2, 1|2          |64          , 205, 206, 246,   0,   "Nichrome"                ,   "Nichrome"                      ,    0,       0,       2700, 2700,  true, false,   1,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(Nickel, 4), new MaterialStack(Chrome, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.NiobiumNitride          = new Materials( 359, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1|2                       ,  29,  41,  29,   0,   "NiobiumNitride"          ,   "Niobium Nitride"               ,    0,       0,       2573, 2573,  true, false,   1,   1,   1, Dyes.dyeBlack       , 1, Arrays.asList(new MaterialStack(Niobium, 1), new MaterialStack(Nitrogen, 1))); // Anti-Reflective Material
        Materials.NiobiumTitanium         = new Materials( 360, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1|2                       ,  29,  29,  41,   0,   "NiobiumTitanium"         ,   "Niobium-Titanium"              ,    0,       0,       4500, 4500,  true, false,   1,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Niobium, 1), new MaterialStack(Titanium, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.NitroCarbon             = new Materials( 716, TextureSet.SET_FLUID             ,   1.0F,      0,  1,         16                ,   0,  75, 100,   0,   "NitroCarbon"             ,   "Nitro-Carbon"                  ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeCyan        , 1, Arrays.asList(new MaterialStack(Nitrogen, 1), new MaterialStack(Carbon, 1)));
        Materials.NitrogenDioxide         = new Materials( 717, TextureSet.SET_FLUID             ,   1.0F,      0,  1,         16                , 100, 175, 255,   0,   "NitrogenDioxide"         ,   "Nitrogen Dioxide"              ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeCyan        , 1, Arrays.asList(new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 2)));
        Materials.Obsidian                = new Materials( 804, TextureSet.SET_DULL              ,   1.0F,      0,  3, 1|2                       ,  80,  50, 100,   0,   "Obsidian"                ,   "Obsidian"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 1, Arrays.asList(new MaterialStack(Magnesium, 1), new MaterialStack(Iron, 1), new MaterialStack(Silicon, 2), new MaterialStack(Oxygen, 8)));
        Materials.Phosphate               = new Materials( 833, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8|16                , 255, 255,   0,   0,   "Phosphate"               ,   "Phosphate"                     ,    0,       0,         -1,    0, false, false,   2,   1,   1, Dyes.dyeYellow      , 1, Arrays.asList(new MaterialStack(Phosphorus, 1), new MaterialStack(Oxygen, 4)));
        Materials.PigIron                 = new Materials( 307, TextureSet.SET_METALLIC          ,   6.0F,    384,  2, 1|2  |8      |64          , 200, 180, 180,   0,   "PigIron"                 ,   "Pig Iron"                      ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyePink        , 2, Collections.singletonList(new MaterialStack(Iron, 1)));
        Materials.Plastic                 = new Materials( 874, TextureSet.SET_DULL              ,   3.0F,     32,  1, 1|2          |64|128      , 200, 200, 200,   0,   "Plastic"                 ,   "Polyethylene"                  ,    0,       0,        400,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Carbon, 1), new MaterialStack(Hydrogen, 2)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2)));
        Materials.Epoxid                  = new Materials( 470, TextureSet.SET_DULL              ,   3.0F,     32,  1, 1|2          |64|128      , 200, 140,  20,   0,   "Epoxid"                  ,   "Epoxid"                        ,    0,       0,        400,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Carbon, 21), new MaterialStack(Hydrogen, 24), new MaterialStack(Oxygen, 4)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2)));
        Materials.Polydimethylsiloxane    = new MaterialBuilder(633, TextureSet.SET_FLUID         ,                                                                                                     "Polydimethylsiloxane").addDustItems().setRGB(245, 245, 245).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 1), new MaterialStack(Silicon, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.Silicone                = new Materials( 471, TextureSet.SET_DULL              ,   3.0F,    128,  1, 1|2          |64|128      , 220, 220, 220,   0,   "Silicone"                ,   "Silicone Rubber"               ,    0,       0,        900,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 1), new MaterialStack(Silicon, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2)));
        Materials.Polycaprolactam         = new Materials( 472, TextureSet.SET_DULL              ,   3.0F,     32,  1, 1|2          |64|128      ,  50,  50,  50,   0,   "Polycaprolactam"         ,   "Polycaprolactam"               ,    0,       0,        500,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 11), new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2)));
        Materials.Polytetrafluoroethylene = new Materials( 473, TextureSet.SET_DULL              ,   3.0F,     32,  1, 1|2          |64|128      , 100, 100, 100,   0,   "Polytetrafluoroethylene" ,   "Polytetrafluoroethylene"       ,    0,       0,        600,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Carbon, 2), new MaterialStack(Fluorine, 4)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2)));
        Materials.Powellite               = new Materials( 883, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 255, 255,   0,   0,   "Powellite"               ,   "Powellite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Molybdenum, 1), new MaterialStack(Oxygen, 4)));
        Materials.Pumice                  = new Materials( 926, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 230, 185, 185,   0,   "Pumice"                  ,   "Pumice"                        ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeGray        , 2, Collections.singletonList(new MaterialStack(Stone, 1)));
        Materials.Pyrite                  = new Materials( 834, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1    |8                   , 150, 120,  40,   0,   "Pyrite"                  ,   "Pyrite"                        ,    0,       0,         -1,    0, false, false,   2,   1,   1, Dyes.dyeOrange      , 1, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Sulfur, 2)));
        Materials.Pyrolusite              = new Materials( 943, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 150, 150, 170,   0,   "Pyrolusite"              ,   "Pyrolusite"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , 1, Arrays.asList(new MaterialStack(Manganese, 1), new MaterialStack(Oxygen, 2)));
        Materials.Pyrope                  = new Materials( 835, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   , 120,  50, 100,   0,   "Pyrope"                  ,   "Pyrope"                        ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyePurple      , 0, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Magnesium, 3), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12)));
        Materials.RockSalt                = new Materials( 944, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1    |8                   , 240, 200, 200,   0,   "RockSalt"                ,   "Rock Salt"                     ,    0,       0,         -1,    0, false, false,   2,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Chlorine, 1)));
        Materials.Rubber                  = new Materials( 880, TextureSet.SET_SHINY             ,   1.5F,     32,  0, 1|2          |64|128      ,   0,   0,   0,   0,   "Rubber"                  ,   "Rubber"                        ,    0,       0,        400,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 0, Arrays.asList(new MaterialStack(Carbon, 5), new MaterialStack(Hydrogen, 8)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2)));
        Materials.RawRubber               = new Materials( 896, TextureSet.SET_DULL              ,   1.0F,      0,  0, 1                         , 204, 199, 137,   0,   "RawRubber"               ,   "Raw Rubber"                    ,    0,       0,        400,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Carbon, 5), new MaterialStack(Hydrogen, 8)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2)));
        Materials.Ruby                    = new Materials( 502, TextureSet.SET_RUBY              ,   7.0F,    256,  2, 1  |4|8      |64          , 255, 100, 100, 127,   "Ruby"                    ,   "Ruby"                          ,    0,       0,         -1,    0, false,  true,   5,   1,   1, Dyes.dyeRed         , 0, Arrays.asList(new MaterialStack(Chrome, 1), new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 6), new TCAspects.TC_AspectStack(TCAspects.VITREUS, 4))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.Salt                    = new Materials( 817, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1    |8                   , 250, 250, 250,   0,   "Salt"                    ,   "Salt"                          ,    0,       0,         -1,    0, false, false,   2,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Sodium, 1), new MaterialStack(Chlorine, 1)));
        Materials.Saltpeter               = new Materials( 836, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1    |8                   , 230, 230, 230,   0,   "Saltpeter"               ,   "Saltpeter"                     ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 3)));
        Materials.Sapphire                = new MaterialBuilder(503, TextureSet.SET_GEM_VERTICAL, "Sapphire").setToolSpeed(7.0F).setDurability(256).setToolQuality(2).addDustItems().addGemItems().setTransparent(true).addOreItems().addToolHeadItems().setRGBA(100, 100, 200, 127).setColor(Dyes.dyeBlue).setExtraData(0).setMaterialList(new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3)).setAspects(Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 5), new TCAspects.TC_AspectStack(TCAspects.VITREUS, 3))).constructMaterial().disableAutoGeneratedBlastFurnaceRecipes();

        Materials.Scheelite               = new Materials( 910, TextureSet.SET_DULL              ,   1.0F,      0,  3, 1    |8                   , 200, 140,  20,   0,   "Scheelite"               ,   "Scheelite"                     ,    0,       0,       2500, 2500, false, false,   4,   1,   1, Dyes.dyeBlack       , 0, Arrays.asList(new MaterialStack(Tungsten, 1), new MaterialStack(Calcium, 1), new MaterialStack(Oxygen, 4)));
        Materials.Snow                    = new Materials( 728, TextureSet.SET_FINE              ,   1.0F,      0,  0, 1|      16                , 250, 250, 250,   0,   "Snow"                    ,   "Snow"                          ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.GELUM, 1)));
        Materials.Sodalite                = new Materials( 525, TextureSet.SET_LAPIS             ,   1.0F,      0,  1, 1  |4|8                   ,  20,  20, 255,   0,   "Sodalite"                ,   "Sodalite"                      ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeBlue        , 1, Arrays.asList(new MaterialStack(Aluminium, 3), new MaterialStack(Silicon, 3), new MaterialStack(Sodium, 4), new MaterialStack(Chlorine, 1)));
        Materials.SodiumPersulfate        = new Materials( 718, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16                , 255, 255, 255,   0,   "SodiumPersulfate"        ,   "Sodium Persulfate"             ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 1, Arrays.asList(new MaterialStack(Sodium, 2), new MaterialStack(Sulfur, 2), new MaterialStack(Oxygen, 8)));
        Materials.SodiumSulfide           = new Materials( 719, TextureSet.SET_FLUID             ,   1.0F,      0,  2, 1                         , 255, 230, 128,   0,   "SodiumSulfide"           ,   "Sodium Sulfide"                ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 1, Arrays.asList(new MaterialStack(Sodium, 2), new MaterialStack(Sulfur, 1)));
        Materials.HydricSulfide           = new Materials( 460, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16                , 255, 255, 255,   0,   "HydricSulfide"           ,   "Hydrogen Sulfide"              ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 0, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Sulfur, 1)));

        Materials.OilExtraHeavy           = new Materials( 570, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,  10,  10,  10,   0,   "OilExtraHeavy"           ,   "Very Heavy Oil"                ,    3,      45,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       );
        Materials.OilHeavy                = new Materials( 730, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,  10,  10,  10,   0,   "OilHeavy"                ,   "Heavy Oil"                     ,    3,      40,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       );
        Materials.OilMedium               = new Materials( 731, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,  10,  10,  10,   0,   "OilMedium"               ,   "Raw Oil"                       ,    3,      30,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       );
        Materials.OilLight                = new Materials( 732, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,  10,  10,  10,   0,   "OilLight"                ,   "Light Oil"                     ,    3,      20,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       );
        Materials.NatruralGas             = new Materials( 733, TextureSet.SET_FLUID             ,   1.0F,      0,  1,         16                , 255, 255, 255,   0,   "NatruralGas"             ,   "Natural Gas"                   ,    1,      20,         -1,    0, false, false,   3,   1,   1, Dyes.dyeWhite       );
        Materials.SulfuricGas             = new Materials( 734, TextureSet.SET_FLUID             ,   1.0F,      0,  1,         16                , 255, 255, 255,   0,   "SulfuricGas"             ,   "Sulfuric Gas"                  ,    1,      25,         -1,    0, false, false,   3,   1,   1, Dyes.dyeWhite       );
        Materials.Gas                     = new Materials( 735, TextureSet.SET_FLUID             ,   1.0F,      0,  1,         16                , 255, 255, 255,   0,   "Gas"                     ,   "Refinery Gas"                  ,    1,     160,         -1,    0, false, false,   3,   1,   1, Dyes.dyeWhite).setCanBeCracked(true);
        Materials.SulfuricNaphtha         = new Materials( 736, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 255,   0,   0,   "SulfuricNaphtha"         ,   "Sulfuric Naphtha"              ,    1,      40,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      );
        Materials.SulfuricLightFuel       = new Materials( 737, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 255,   0,   0,   "SulfuricLightFuel"       ,   "Sulfuric Light Fuel"           ,    0,      40,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      );
        Materials.SulfuricHeavyFuel       = new Materials( 738, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 255,   0,   0,   "SulfuricHeavyFuel"       ,   "Sulfuric Heavy Fuel"           ,    3,      40,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       );
        Materials.Naphtha                 = new Materials( 739, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 255,   0,   0,   "Naphtha"                 ,   "Naphtha"                       ,    1,     220,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow).setCanBeCracked(true);
        Materials.LightFuel               = new Materials( 740, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 255,   0,   0,   "LightFuel"               ,   "Light Fuel"                    ,    0,     305,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow).setCanBeCracked(true);
        Materials.HeavyFuel               = new Materials( 741, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 255,   0,   0,   "HeavyFuel"               ,   "Heavy Fuel"                    ,    3,     240,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack).setCanBeCracked(true);
        Materials.LPG                     = new Materials( 742, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 255,   0,   0,   "LPG"                     ,   "LPG"                           ,    1,     320,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      );
        Materials.ReinforceGlass          = new MaterialBuilder(602, TextureSet.SET_FLUID      ,                                                                                                     "Reinforced Glass").setName("ReinforcedGlass").setRGB(192, 245, 254).setColor(Dyes.dyeWhite).setMeltingPoint(2000).constructMaterial().disableAutoGeneratedRecycleRecipes();
        Materials.BioMediumRaw            = new MaterialBuilder(603, TextureSet.SET_FLUID      ,                                                                                                     "Raw Bio Catalyst Medium").setName("BioMediumRaw").addCell().addFluid().setRGB(97, 147, 46).setColor(Dyes.dyeLime).constructMaterial();
        Materials.BioMediumSterilized     = new MaterialBuilder(604, TextureSet.SET_FLUID      ,                                                                                                     "Sterilized Bio Catalyst Medium").setName("BiohMediumSterilized").addCell().addFluid().setRGB(162, 253, 53).setColor(Dyes.dyeLime).constructMaterial();
        Materials.Chlorobenzene             = new MaterialBuilder(605, TextureSet.SET_FLUID      ,                                                                                                     "Chlorobenzene").addCell().addFluid().setRGB(0, 50, 65).setColor(Dyes.dyeGray).setMaterialList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 5), new MaterialStack(Chlorine, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.DilutedHydrochloricAcid   = new MaterialBuilder(606, TextureSet.SET_FLUID      ,                                                                                                     "Diluted Hydrochloric Acid").setName("DilutedHydrochloricAcid_GT5U").addCell().addFluid().setRGB(153, 167, 163).setColor(Dyes.dyeLightGray).setMaterialList(new MaterialStack(Hydrogen, 1), new MaterialStack(Chlorine, 1)).constructMaterial();
        Materials.Pyrochlore                = new MaterialBuilder(607, TextureSet.SET_METALLIC   ,                                                                                                     "Pyrochlore").addDustItems().addOreItems().setRGB(43, 17, 0).setColor(Dyes.dyeBlack).setMaterialList(new MaterialStack(Calcium, 2), new MaterialStack(Niobium, 2), new MaterialStack(Oxygen, 7)).addElectrolyzerRecipe().constructMaterial();
        Materials.GrowthMediumRaw           = new MaterialBuilder(608, TextureSet.SET_FLUID      ,                                                                                                     "Raw Growth Catalyst Medium").setName("GrowthMediumRaw").addCell().addFluid().setRGB(211, 141, 95).setColor(Dyes.dyeOrange).constructMaterial();
        Materials.GrowthMediumSterilized    = new MaterialBuilder(609, TextureSet.SET_FLUID      ,                                                                                                     "Growth Catalyst Medium").setName("GrowthMediumSterilized").addCell().addFluid().setRGB(222, 170, 135).setColor(Dyes.dyeOrange).constructMaterial();
        Materials.FerriteMixture            = new MaterialBuilder(612, TextureSet.SET_METALLIC   ,                                                                                                     "Ferrite Mixture").addDustItems().setRGB(180, 180, 180).setColor(Dyes.dyeGray).setMaterialList(new MaterialStack(Nickel, 1), new MaterialStack(Zinc, 1), new MaterialStack(Iron, 4)).constructMaterial();
        Materials.NickelZincFerrite         = new MaterialBuilder(613, TextureSet.SET_ROUGH      ,                                                                                                     "Nickel-Zinc Ferrite").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(3.0f).setDurability(32).setRGB(60, 60, 60).setColor(Dyes.dyeBlack).setBlastFurnaceRequired(true).setBlastFurnaceTemp(1500).setMaterialList(new MaterialStack(Nickel, 1), new MaterialStack(Zinc, 1), new MaterialStack(Iron, 4), new MaterialStack(Oxygen, 8)).constructMaterial();
        Materials.Massicot                  = new MaterialBuilder(614, TextureSet.SET_DULL       ,                                                                                                     "Massicot").addDustItems().setRGB(255, 221, 85).setColor(Dyes.dyeYellow).setMaterialList(new MaterialStack(Lead, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.ArsenicTrioxide           = new MaterialBuilder(615, TextureSet.SET_SHINY      ,                                                                                                     "Arsenic Trioxide").addDustItems().setRGB(255, 255, 255).setColor(Dyes.dyeGreen).setMaterialList(new MaterialStack(Arsenic, 2), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();
        Materials.CobaltOxide               = new MaterialBuilder(616, TextureSet.SET_DULL       ,                                                                                                     "Cobalt Oxide").addDustItems().setRGB(102, 128, 0).setColor(Dyes.dyeGreen).setMaterialList(new MaterialStack(Cobalt, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.Zincite                   = new MaterialBuilder(617, TextureSet.SET_DULL       ,                                                                                                     "Zincite").addDustItems().setRGB(255, 255, 245).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Zinc, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.AntimonyTrioxide          = new MaterialBuilder(618, TextureSet.SET_DULL       ,                                                                                                     "Antimony Trioxide").addDustItems().setRGB(230, 230, 240).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Antimony, 2), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();
        Materials.CupricOxide               = new MaterialBuilder(619, TextureSet.SET_DULL       ,                                                                                                     "Cupric Oxide").addDustItems().setRGB(15, 15, 15).setColor(Dyes.dyeBlack).setMeltingPoint(1599).setMaterialList(new MaterialStack(Copper, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.Ferrosilite               = new MaterialBuilder(620, TextureSet.SET_DULL       ,                                                                                                     "Ferrosilite").addDustItems().setRGB(151, 99, 42).setColor(Dyes.dyeBrown).setMaterialList(new MaterialStack(Iron, 1), new MaterialStack(Silicon, 1), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();
        Materials.Magnesia                  = new MaterialBuilder(621, TextureSet.SET_DULL       ,                                                                                                     "Magnesia").addDustItems().setRGB(255, 225, 225).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Magnesium, 1), new MaterialStack(Oxygen, 1)).constructMaterial();
        Materials.Quicklime                 = new MaterialBuilder(622, TextureSet.SET_DULL       ,                                                                                                     "Quicklime").addDustItems().setRGB(240, 240, 240).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Calcium, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.Potash                    = new MaterialBuilder(623, TextureSet.SET_DULL       ,                                                                                                     "Potash").addDustItems().setRGB(120, 66, 55).setColor(Dyes.dyeBrown).setMaterialList(new MaterialStack(Potassium, 2), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.SodaAsh                   = new MaterialBuilder(624, TextureSet.SET_DULL       ,                                                                                                     "Soda Ash").addDustItems().setRGB(220, 220, 255).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Sodium, 2), new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();
        Materials.BioDiesel                 = new MaterialBuilder(627, TextureSet.SET_FLUID      ,                                                                                                     "Bio Diesel").addCell().addFluid().setRGB(255, 128, 0).setColor(Dyes.dyeOrange).setFuelType(MaterialBuilder.DIESEL).setFuelPower(320).constructMaterial();
        Materials.NitrationMixture          = new MaterialBuilder(628, TextureSet.SET_FLUID      ,                                                                                                     "Nitration Mixture").addCell().setRGB(230, 226, 171).setColor(Dyes.dyeBrown).constructMaterial();
        Materials.Glycerol                  = new MaterialBuilder(629, TextureSet.SET_FLUID      ,                                                                                                     "Glycerol").addCell().addFluid().setRGB(135, 222, 135).setColor(Dyes.dyeLime).setFuelType(MaterialBuilder.SEMIFLUID).setFuelPower(164).setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 8), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();
        Materials.SodiumBisulfate           = new MaterialBuilder(630, TextureSet.SET_FLUID      ,                                                                                                     "Sodium Bisulfate").addDustItems().setRGB(0, 68, 85).setColor(Dyes.dyeBlue).setMaterialList(new MaterialStack(Sodium, 1), new MaterialStack(Hydrogen, 1), new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 4)).constructMaterial();
        Materials.PolyphenyleneSulfide      = new MaterialBuilder(631, TextureSet.SET_DULL       ,                                                                                                     "Polyphenylene Sulfide").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(3.0f).setDurability(32).setToolQuality(1).setRGB(170, 136, 0).setColor(Dyes.dyeBrown).setMaterialList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 4), new MaterialStack(Sulfur, 1)).constructMaterial();
        Materials.Dichlorobenzene           = new MaterialBuilder(632, TextureSet.SET_FLUID      ,                                                                                                     "Dichlorobenzene").addCell().addFluid().setRGB(0, 68, 85).setColor(Dyes.dyeBlue).setMaterialList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 4), new MaterialStack(Chlorine, 2)).addElectrolyzerRecipe().constructMaterial();
        Materials.Polystyrene               = new MaterialBuilder(636, TextureSet.SET_DULL       ,                                                                                                     "Polystyrene").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(3.0f).setDurability(32).setToolQuality(1).setRGB(190, 180, 170).setColor(Dyes.dyeLightGray).setMaterialList(new MaterialStack(Carbon, 8), new MaterialStack(Hydrogen, 8)).constructMaterial();
        Materials.Styrene                   = new MaterialBuilder(637, TextureSet.SET_FLUID      ,                                                                                                     "Styrene").addCell().addFluid().setRGB(210, 200, 190).setColor(Dyes.dyeBlack).setMaterialList(new MaterialStack(Carbon, 8), new MaterialStack(Hydrogen, 8)).addElectrolyzerRecipe().constructMaterial();
        Materials.Isoprene                  = new MaterialBuilder(638, TextureSet.SET_FLUID      ,                                                                                                     "Isoprene").addCell().addFluid().setRGB(20, 20, 20).setColor(Dyes.dyeBlack).setMaterialList(new MaterialStack(Carbon, 5), new MaterialStack(Hydrogen, 8)).addElectrolyzerRecipe().constructMaterial();
        Materials.Tetranitromethane         = new MaterialBuilder(639, TextureSet.SET_FLUID      ,                                                                                                     "Tetranitromethane").addCell().addFluid().setRGB(15, 40, 40).setColor(Dyes.dyeBlack).setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(Nitrogen, 4), new MaterialStack(Oxygen, 8)).addElectrolyzerRecipe().constructMaterial();
        Materials.Ethenone                  = new MaterialBuilder(641, TextureSet.SET_FLUID      ,                                                                                                     "Ethenone").addCell().addGas().setRGB(20, 20, 70).setColor(Dyes.dyeBlack).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.Ethane                    = new MaterialBuilder(642, TextureSet.SET_FLUID      ,                                                                                                     "Ethane").addCell().addGas().setRGB(200, 200, 255).setColor(Dyes.dyeLightBlue).setFuelType(MaterialBuilder.GAS).setFuelPower(168).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 6)).addElectrolyzerRecipe().setCanBeCracked(true).constructMaterial();
        Materials.Propane                   = new MaterialBuilder(643, TextureSet.SET_FLUID      ,                                                                                                     "Propane").addCell().addGas().setRGB(250, 226, 80).setColor(Dyes.dyeYellow).setFuelType(MaterialBuilder.GAS).setFuelPower(232).setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 8)).addElectrolyzerRecipe().setCanBeCracked(true).constructMaterial();
        Materials.Butane                    = new MaterialBuilder(644, TextureSet.SET_FLUID      ,                                                                                                     "Butane").addCell().addGas().setRGB(182, 55, 30).setColor(Dyes.dyeOrange).setFuelType(MaterialBuilder.GAS).setFuelPower(296).setMaterialList(new MaterialStack(Carbon, 4), new MaterialStack(Hydrogen, 10)).addElectrolyzerRecipe().setCanBeCracked(true).constructMaterial();
        Materials.Butene                    = new MaterialBuilder(645, TextureSet.SET_FLUID      ,                                                                                                     "Butene").addCell().addGas().setRGB(207, 80, 5).setColor(Dyes.dyeOrange).setFuelType(MaterialBuilder.GAS).setFuelPower(256).setMaterialList(new MaterialStack(Carbon, 4), new MaterialStack(Hydrogen, 8)).addElectrolyzerRecipe().setCanBeCracked(true).constructMaterial();
        Materials.Butadiene                 = new MaterialBuilder(646, TextureSet.SET_FLUID      ,                                                                                                     "Butadiene").addCell().addGas().setRGB(232, 105, 0).setColor(Dyes.dyeOrange).setFuelType(MaterialBuilder.GAS).setFuelPower(206).setMaterialList(new MaterialStack(Carbon, 4), new MaterialStack(Hydrogen, 6)).addElectrolyzerRecipe().setCanBeCracked(true).constructMaterial();
        Materials.RawStyreneButadieneRubber = new MaterialBuilder(634, TextureSet.SET_SHINY      ,                                                                                                     "Raw Styrene-Butadiene Rubber").addDustItems().setRGB(84, 64, 61).setColor(Dyes.dyeGray).setMaterialList(new MaterialStack(Styrene, 1), new MaterialStack(Butadiene, 3)).constructMaterial();
        Materials.StyreneButadieneRubber    = new MaterialBuilder(635, TextureSet.SET_SHINY      ,                                                                                                     "Styrene-Butadiene Rubber").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(3.0f).setDurability(128).setToolQuality(1).setRGB(33, 26, 24).setColor(Dyes.dyeBlack).setMaterialList(new MaterialStack(Styrene, 1), new MaterialStack(Butadiene, 3)).constructMaterial();
        Materials.Toluene                   = new MaterialBuilder(647, TextureSet.SET_FLUID      ,                                                                                                     "Toluene").addCell().setRGB(80, 29, 5).setColor(Dyes.dyeBrown).setFuelType(MaterialBuilder.GAS).setFuelPower(328).setMaterialList(new MaterialStack(Carbon, 7), new MaterialStack(Hydrogen, 8)).addElectrolyzerRecipe().constructMaterial();
        Materials.Epichlorohydrin           = new MaterialBuilder(648, TextureSet.SET_FLUID      ,                                                                                                     "Epichlorohydrin").addCell().setRGB(80, 29, 5).setColor(Dyes.dyeBrown).setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 5), new MaterialStack(Chlorine, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.PolyvinylChloride         = new MaterialBuilder(649, TextureSet.SET_DULL       ,                                                                                                     "Polyvinyl Chloride").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(3.0f).setDurability(32).setToolQuality(1).setRGB(215, 230, 230).setColor(Dyes.dyeLightGray).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 3), new MaterialStack(Chlorine, 1)).constructMaterial();
        Materials.VinylChloride             = new MaterialBuilder(650, TextureSet.SET_FLUID      ,                                                                                                     "Vinyl Chloride").addCell().addGas().setRGB(225, 240, 240).setColor(Dyes.dyeLightGray).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 3), new MaterialStack(Chlorine, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.SulfurDioxide             = new MaterialBuilder(651, TextureSet.SET_FLUID      ,                                                                                                     "Sulfur Dioxide").addCell().addGas().setRGB(200, 200, 25).setColor(Dyes.dyeYellow).setMaterialList(new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 2)).constructMaterial();
        Materials.SulfurTrioxide            = new MaterialBuilder(652, TextureSet.SET_FLUID      ,                                                                                                     "Sulfur Trioxide").addCell().addGas().setGasTemperature(344).setRGB(160, 160, 20).setColor(Dyes.dyeYellow).setMaterialList(new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();
        Materials.NitricAcid                = new MaterialBuilder(653, TextureSet.SET_FLUID      ,                                                                                                     "Nitric Acid").addCell().addFluid().setRGB(230, 226, 171).setMaterialList(new MaterialStack(Hydrogen, 1), new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();
        Materials.Dimethylhydrazine         = new MaterialBuilder(654, TextureSet.SET_FLUID      ,                                                                                                     "1,1-Dimethylhydrazine").addCell().addFluid().setRGB(0, 0, 85).setColor(Dyes.dyeBlue).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 8), new MaterialStack(Nitrogen, 2)).addElectrolyzerRecipe().constructMaterial();
        Materials.Chloramine                = new MaterialBuilder(655, TextureSet.SET_FLUID      ,                                                                                                     "Chloramine").addCell().addFluid().setRGB(63, 159, 128).setColor(Dyes.dyeCyan).setMaterialList(new MaterialStack(Nitrogen, 1), new MaterialStack(Hydrogen, 2), new MaterialStack(Chlorine, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.Dimethylamine             = new MaterialBuilder(656, TextureSet.SET_FLUID      ,                                                                                                     "Dimethylamine").addCell().addGas().setRGB(85, 68, 105).setColor(Dyes.dyeGray).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 7), new MaterialStack(Nitrogen, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.DinitrogenTetroxide       = new MaterialBuilder(657, TextureSet.SET_FLUID      ,                                                                                                     "Dinitrogen Tetroxide").addCell().addGas().setRGB(0, 65, 132).setColor(Dyes.dyeBlue).setMaterialList(new MaterialStack(Nitrogen, 2), new MaterialStack(Oxygen, 4)).addElectrolyzerRecipe().constructMaterial();
        Materials.NitricOxide               = new MaterialBuilder(658, TextureSet.SET_FLUID      ,                                                                                                     "Nitric Oxide").addCell().addGas().setRGB(125, 200, 240).setColor(Dyes.dyeCyan).setMaterialList(new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.Ammonia                   = new MaterialBuilder(659, TextureSet.SET_FLUID      ,                                                                                                     "Ammonia").addCell().addGas().setRGB(63, 52, 128).setColor(Dyes.dyeBlue).setMaterialList(new MaterialStack(Nitrogen, 1), new MaterialStack(Hydrogen, 3)).addElectrolyzerRecipe().constructMaterial();
        Materials.Dimethyldichlorosilane    = new MaterialBuilder(663, TextureSet.SET_FLUID      ,                                                                                                     "Dimethyldichlorosilane").addCell().addFluid().setRGB(68, 22, 80).setColor(Dyes.dyePurple).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 6), new MaterialStack(Chlorine, 2), new MaterialStack(Silicon, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.Chloromethane             = new MaterialBuilder(664, TextureSet.SET_FLUID      ,                                                                                                     "Chloromethane").addCell().addGas().setRGB(200, 44, 160).setColor(Dyes.dyeMagenta).setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(Hydrogen, 3), new MaterialStack(Chlorine, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.PhosphorousPentoxide      = new MaterialBuilder(665, TextureSet.SET_FLUID      ,                                                                                                     "Phosphorous Pentoxide").addCell().addDustItems().setRGB(220, 220, 0).setColor(Dyes.dyeYellow).setMaterialList(new MaterialStack(Phosphorus, 4), new MaterialStack(Oxygen, 10)).addElectrolyzerRecipe().constructMaterial();
        Materials.Tetrafluoroethylene       = new MaterialBuilder(666, TextureSet.SET_FLUID      ,                                                                                                     "Tetrafluoroethylene").addCell().addGas().setRGB(125, 125, 125).setColor(Dyes.dyeGray).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Fluorine, 4)).addElectrolyzerRecipe().constructMaterial();
        Materials.HydrofluoricAcid          = new MaterialBuilder(667, TextureSet.SET_FLUID      ,                                                                                                     "Hydrofluoric Acid").setName("HydrofluoricAcid_GT5U").addCell().addFluid().setRGB(0, 136, 170).setColor(Dyes.dyeLightBlue).setMaterialList(new MaterialStack(Hydrogen, 1), new MaterialStack(Fluorine, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.Chloroform                = new MaterialBuilder(668, TextureSet.SET_FLUID      ,                                                                                                     "Chloroform").addCell().addFluid().setRGB(137, 44, 160).setColor(Dyes.dyePurple).setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(Hydrogen, 1), new MaterialStack(Chlorine, 3)).addElectrolyzerRecipe().constructMaterial();
        Materials.BisphenolA                = new MaterialBuilder(669, TextureSet.SET_FLUID      ,                                                                                                     "Bisphenol A").addCell().setRGB(212, 170, 0).setColor(Dyes.dyeBrown).setMaterialList(new MaterialStack(Carbon, 15), new MaterialStack(Hydrogen, 16), new MaterialStack(Oxygen, 2)).addElectrolyzerRecipe().constructMaterial();
        Materials.AceticAcid                = new MaterialBuilder(670, TextureSet.SET_FLUID      ,                                                                                                     "Acetic Acid").addCell().addFluid().setRGB(200, 180, 160).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 4), new MaterialStack(Oxygen, 2)).addElectrolyzerRecipe().constructMaterial();
        Materials.CalciumAcetateSolution    = new MaterialBuilder(671, TextureSet.SET_RUBY       ,                                                                                                     "Calcium Acetate Solution").addCell().addFluid().setRGB(220, 200, 180).setColor(Dyes.dyeCyan).setMaterialList(new MaterialStack(Calcium, 1), new MaterialStack(Carbon, 4), new MaterialStack(Oxygen, 4), new MaterialStack(Hydrogen, 6)).addElectrolyzerRecipe().constructMaterial();
        Materials.Acetone                   = new MaterialBuilder(672, TextureSet.SET_FLUID      ,                                                                                                     "Acetone").addCell().addFluid().setRGB(175, 175, 175).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.Methanol                  = new MaterialBuilder(673, TextureSet.SET_FLUID      ,                                                                                                     "Methanol").addCell().addFluid().setRGB(170, 136, 0).setColor(Dyes.dyeBrown).setFuelPower(84).setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(Hydrogen, 4), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.CarbonMonoxide            = new MaterialBuilder(674, TextureSet.SET_FLUID      ,                                                                                                     "Carbon Monoxide").addCell().addGas().setRGB(14, 72, 128).setColor(Dyes.dyeBrown).setFuelType(MaterialBuilder.GAS).setFuelPower(24).setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 1)).constructMaterial();
        Materials.MetalMixture              = new MaterialBuilder(676, TextureSet.SET_METALLIC   ,                                                                                                     "Metal Mixture").addDustItems().setRGB(80, 45, 22).setColor(Dyes.dyeBrown).constructMaterial();
        Materials.Ethylene                  = new MaterialBuilder(677, TextureSet.SET_FLUID      ,                                                                                                     "Ethylene").addCell().addGas().setRGB(225, 225, 225).setColor(Dyes.dyeWhite).setFuelType(MaterialBuilder.GAS).setFuelPower(128).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 4)).addElectrolyzerRecipe().setCanBeCracked(true).constructMaterial();
        Materials.Propene                   = new MaterialBuilder(678, TextureSet.SET_FLUID      ,                                                                                                     "Propene").addCell().addGas().setRGB(255, 221, 85).setColor(Dyes.dyeYellow).setFuelType(MaterialBuilder.GAS).setFuelPower(192).setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 6)).addElectrolyzerRecipe().setCanBeCracked(true).constructMaterial();
        Materials.VinylAcetate              = new MaterialBuilder(679, TextureSet.SET_FLUID      ,                                                                                                     "Vinyl Acetate").addCell().addFluid().setRGB(255, 179, 128).setColor(Dyes.dyeOrange).setMaterialList(new MaterialStack(Carbon, 4), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 2)).addElectrolyzerRecipe().constructMaterial();
        Materials.PolyvinylAcetate          = new MaterialBuilder(680, TextureSet.SET_FLUID      ,                                                                                                     "Polyvinyl Acetate").addCell().addFluid().setRGB(255, 153, 85).setColor(Dyes.dyeOrange).setMaterialList(new MaterialStack(Carbon, 4), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 2)).constructMaterial();
        Materials.MethylAcetate             = new MaterialBuilder(681, TextureSet.SET_FLUID      ,                                                                                                     "Methyl Acetate").addCell().addFluid().setRGB(238, 198, 175).setColor(Dyes.dyeOrange).setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 2)).addElectrolyzerRecipe().constructMaterial();
        Materials.AllylChloride             = new MaterialBuilder(682, TextureSet.SET_FLUID      ,                                                                                                     "Allyl Chloride").addCell().addFluid().setRGB(135, 222, 170).setColor(Dyes.dyeCyan).setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 5), new MaterialStack(Chlorine, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.HydrochloricAcid          = new MaterialBuilder(683, TextureSet.SET_FLUID      ,                                                                                                     "Hydrochloric Acid").setName("HydrochloricAcid_GT5U").addCell().addFluid().setRGB(183, 200, 196).setColor(Dyes.dyeLightGray).setMaterialList(new MaterialStack(Hydrogen, 1), new MaterialStack(Chlorine, 1)).constructMaterial();
        Materials.HypochlorousAcid          = new MaterialBuilder(684, TextureSet.SET_FLUID      ,                                                                                                     "Hypochlorous Acid").addCell().addFluid().setRGB(111, 138, 145).setColor(Dyes.dyeGray).setMaterialList(new MaterialStack(Hydrogen, 1), new MaterialStack(Chlorine, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.SodiumOxide               = new MaterialBuilder(744, TextureSet.SET_DULL       ,                                                                                                     "Sodium Oxide").setName("SodiumOxide").addDustItems().setRGB(255, 255, 235).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Sodium, 2), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.SodiumHydroxide           = new MaterialBuilder(685, TextureSet.SET_DULL       ,                                                                                                     "Sodium Hydroxide").setName("SodiumHydroxide_GT5U").addDustItems().setRGB(0, 51, 128).setColor(Dyes.dyeBlue).setMaterialList(new MaterialStack(Sodium, 1), new MaterialStack(Oxygen, 1), new MaterialStack(Hydrogen, 1)).constructMaterial();
        Materials.Benzene                   = new MaterialBuilder(686, TextureSet.SET_FLUID      ,                                                                                                     "Benzene").addCell().addFluid().setRGB(26, 26, 26).setColor(Dyes.dyeGray).setFuelType(MaterialBuilder.GAS).setFuelPower(360).setMaterialList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 6)).addElectrolyzerRecipe().constructMaterial();
        Materials.Phenol                    = new MaterialBuilder(687, TextureSet.SET_FLUID      ,                                                                                                     "Phenol").addCell().addFluid().setRGB(120, 68, 33).setColor(Dyes.dyeBrown).setFuelType(MaterialBuilder.GAS).setFuelPower(288).setMaterialList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
        Materials.Cumene                    = new MaterialBuilder(688, TextureSet.SET_FLUID      ,                                                                                                     "Isopropylbenzene").addCell().addFluid().setRGB(85, 34, 0).setColor(Dyes.dyeBrown).setMaterialList(new MaterialStack(Carbon, 9), new MaterialStack(Hydrogen, 12)).addElectrolyzerRecipe().constructMaterial();
        Materials.PhosphoricAcid            = new MaterialBuilder(689, TextureSet.SET_FLUID      ,                                                                                                     "Phosphoric Acid").setName("PhosphoricAcid_GT5U").addCell().addFluid().setRGB(220, 220, 0).setColor(Dyes.dyeYellow).setMaterialList(new MaterialStack(Hydrogen, 3), new MaterialStack(Phosphorus, 1), new MaterialStack(Oxygen, 4)).constructMaterial();
        Materials.SaltWater                 = new MaterialBuilder(692, TextureSet.SET_FLUID      ,                                                                                                     "Salt Water").addCell().addFluid().setRGB(0, 0, 200).setColor(Dyes.dyeBlue).constructMaterial();
        Materials.IronIIIChloride           = new MaterialBuilder(693, TextureSet.SET_FLUID      ,                                                                                                     "Iron III Chloride").setName("IronIIIChloride").addCell().addFluid().setRGB(22, 21, 14).setColor(Dyes.dyeBlack).setMaterialList(new MaterialStack(Iron, 1), new MaterialStack(Chlorine, 3)).addElectrolyzerRecipe().constructMaterial();
        Materials.LifeEssence               = new MaterialBuilder(694, TextureSet.SET_FLUID      ,                                                                                                     "Life").setName("lifeessence").addCell().addFluid().setFuelPower(100).setFuelType(5).setRGB(110, 3, 3).setColor(Dyes.dyeRed).setMaterialList().constructMaterial();

        Materials.RoastedCopper             = new MaterialBuilder(546, TextureSet.SET_DULL    , "Roasted Copper").setName("RoastedCopper").addDustItems().setRGB(77, 18, 18).constructMaterial();
        Materials.RoastedAntimony           = new MaterialBuilder(547, TextureSet.SET_DULL    , "Roasted Antimony").setName("RoastedAntimony").addDustItems().setRGB(196, 178, 194).constructMaterial();
        Materials.RoastedIron               = new MaterialBuilder(548, TextureSet.SET_DULL    , "Roasted Iron").setName("RoastedIron").addDustItems().setRGB(148, 98, 98).addOreItems().constructMaterial();
        Materials.RoastedNickel             = new MaterialBuilder(549, TextureSet.SET_METALLIC, "Roasted Nickel").setName("RoastedNickel").addDustItems().setRGB(70, 140, 45).addOreItems().constructMaterial();
        Materials.RoastedZinc               = new MaterialBuilder(550, TextureSet.SET_DULL    , "Roasted Zinc").setName("RoastedZinc").addDustItems().setRGB(209, 209, 209).constructMaterial();
        Materials.RoastedCobalt             = new MaterialBuilder(551, TextureSet.SET_METALLIC, "Roasted Cobalt").setName("RoastedCobalt").addDustItems().setRGB(8, 64, 9).constructMaterial();
        Materials.RoastedArsenic            = new MaterialBuilder(552, TextureSet.SET_SHINY   , "Roasted Arsenic").setName("RoastedArsenic").addDustItems().setRGB(240, 240, 240).constructMaterial();
        Materials.RoastedLead               = new MaterialBuilder(553, TextureSet.SET_SHINY   , "Roasted Lead").setName("RoastedLead").addDustItems().setRGB(168, 149, 43).constructMaterial();

        Materials.SiliconSG               = new Materials(  856, TextureSet.SET_METALLIC         ,   1.0F,      0,  2, 1|2  |8   |32             ,  80,  80, 100,   0,   "SiliconSolarGrade"       ,   "Silicon Solar Grade (Poly SI)" ,    0,       0,       2273, 2273,  true, false,   1,   1,   1, Dyes.dyeBlack       , Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 4), new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 2))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.CalciumDisilicide       = new Materials(  971, TextureSet.SET_METALLIC         ,   1.0F,      0,  2, 1    |8                   , 180, 180, 180,   0,   "CalciumDisilicide"       ,   "Calcium Disilicide"            ,    0,       0,       1313,   -1, false, false,   1,   1,   1, Dyes.dyeGray        , 1, Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Silicon, 2)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.TERRA, 1), new TCAspects.TC_AspectStack(TCAspects.ORDO, 1)));//CaSi2
        Materials.SiliconTetrafluoride    = new MaterialBuilder(  967, TextureSet.SET_FLUID            , "Silicon Tetrafluoride" ).setName("SiliconTetrafluoride").addCell().addGas().setTransparent(true).setRGB(200, 200, 200).setColor(Dyes.dyeWhite).setMeltingPoint(178).setMaterialList(new MaterialStack(Silicon, 1), new MaterialStack(Fluorine, 4)).setAspects(Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 1), new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1))).constructMaterial();//SIF4
        Materials.SiliconTetrachloride    = new MaterialBuilder(  968, TextureSet.SET_FLUID            ,  "Silicon Tetrachloride").setName("SiliconTetrachloride").addCell().addFluid().setRGB(220, 220, 220).setColor(Dyes.dyeWhite).setMeltingPoint(204).setMaterialList(new MaterialStack(Silicon, 1), new MaterialStack(Chlorine, 4)).setAspects(Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 1), new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1))).constructMaterial();//SICL4
        Materials.Trichlorosilane         = new MaterialBuilder(  972, TextureSet.SET_FLUID            ,   "Trichlorosilane" ).setName("Trichlorosilane" ).addCell().addFluid().setRGB( 255, 255, 255).setColor(Dyes.dyeWhite).setMeltingPoint(139).setMaterialList(new MaterialStack(Hydrogen, 1), new MaterialStack(Silicon, 1), new MaterialStack(Chlorine, 3)).setAspects(Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 1), new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1))).constructMaterial();//HSICL3
        Materials.Hexachlorodisilane      = new MaterialBuilder(  973, TextureSet.SET_FLUID            ,  "Hexachlorodisilane").setName("Hexachlorodisilane" ).addCell().addFluid().setRGB( 255, 255, 255).setColor(Dyes.dyeWhite).setMeltingPoint(272).setExtraData(1).setMaterialList(new MaterialStack(Silicon, 2), new MaterialStack(Chlorine, 6)).setAspects(Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 1))).constructMaterial();//SI2CL6
        Materials.Dichlorosilane          = new MaterialBuilder(  799, TextureSet.SET_FLUID            ,   "Dichlorosilane").setName("Dichlorosilane").addCell().addGas().setTransparent(true).setRGB( 255, 255, 255).setColor(Dyes.dyeWhite).setMeltingPoint(151).setExtraData(1).setMaterialList(new MaterialStack(Silicon, 1), new MaterialStack(Hydrogen, 2), new MaterialStack(Chlorine, 2)).setAspects(Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 1), new TCAspects.TC_AspectStack(TCAspects.VENENUM, 1))).constructMaterial();//SIH2CL2
        Materials.Silane                  = new MaterialBuilder(  798, TextureSet.SET_FLUID            ,   "Silane").setName( "Silane").addCell().addGas().setRGB( 255, 255, 255).setColor(Dyes.dyeWhite).setMeltingPoint(88).setMaterialList(new MaterialStack(Silicon, 1), new MaterialStack(Hydrogen, 4)).setAspects(Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.AQUA, 1))).constructMaterial();//SIH4
        Materials.Calciumhydride          = new Materials(  797, TextureSet.SET_METALLIC         ,   1.0F,      0,  2, 1    |8                   , 220, 220, 220,   0,   "CalciumHydride"          ,   "Calcium Hydride"               ,    0,       0,       1089,   -1, false, false,   1,   1,   1, Dyes.dyeGray        , 1, Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Hydrogen, 2)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.TERRA, 1), new TCAspects.TC_AspectStack(TCAspects.ORDO, 1)));//CaH2
        Materials.AluminiumFluoride       = new Materials(  969, TextureSet.SET_METALLIC         ,   1.0F,      0,  2, 1    |8                   , 255, 255, 255,   0,   "Aluminiumfluoride"       ,   "Aluminium Fluoride"            ,    0,       0,       1533,   -1, false, false,   1,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Aluminium, 1), new MaterialStack(Fluorine, 3)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.TERRA, 1), new TCAspects.TC_AspectStack(TCAspects.ORDO, 1)));//ALF3

        Materials.SolderingAlloy          = new Materials( 314, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1|2                       , 220, 220, 230,   0,   "SolderingAlloy"          ,   "Soldering Alloy"               ,    0,       0,        400,  400, false, false,   1,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Tin, 9), new MaterialStack(Antimony, 1)));
        Materials.GalliumArsenide         = new Materials( 980, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1|2                       , 160, 160, 160,   0,   "GalliumArsenide"         ,   "Gallium Arsenide"              ,    0,       0,         -1, 1200,  true, false,   1,   1,   1, Dyes.dyeGray        , 2, Arrays.asList(new MaterialStack(Arsenic, 1), new MaterialStack(Gallium, 1)));
        Materials.IndiumGalliumPhosphide  = new Materials( 981, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1|2                       , 160, 140, 190,   0,   "IndiumGalliumPhosphide"  ,   "Indium Gallium Phosphide"      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , 2, Arrays.asList(new MaterialStack(Indium, 1), new MaterialStack(Gallium, 1), new MaterialStack(Phosphorus, 1)));
        Materials.Spessartine             = new Materials( 838, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 255, 100, 100,   0,   "Spessartine"             ,   "Spessartine"                   ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeRed         , 0, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Manganese, 3), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12)));
        Materials.Sphalerite              = new Materials( 839, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   , 255, 255, 255,   0,   "Sphalerite"              ,   "Sphalerite"                    ,    0,       0,         -1,    0, false, false,   2,   1,   1, Dyes.dyeYellow      , 0, Arrays.asList(new MaterialStack(Zinc, 1), new MaterialStack(Sulfur, 1)));
        Materials.StainlessSteel          = new Materials( 306, TextureSet.SET_SHINY             ,   7.0F,    480,  4, 1|2          |64|128      , 200, 200, 220,   0,   "StainlessSteel"          ,   "Stainless Steel"               ,    0,       0,         -1, 1700,  true, false,   1,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Iron, 6), new MaterialStack(Chrome, 1), new MaterialStack(Manganese, 1), new MaterialStack(Nickel, 1))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.Steel                   = new Materials( 305, TextureSet.SET_METALLIC          ,   6.0F,    512,  3, 1|2          |64|128      , 128, 128, 128,   0,   "Steel"                   ,   "Steel"                         ,    0,       0,       1811, 1000,  true, false,   4,  51,  50, Dyes.dyeGray        , 1, Arrays.asList(new MaterialStack(Iron, 50), new MaterialStack(Carbon, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.ORDO, 1)));
        Materials.Stibnite                = new Materials( 945, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   ,  70,  70,  70,   0,   "Stibnite"                ,   "Stibnite"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Antimony, 2), new MaterialStack(Sulfur, 3)));
        Materials.SulfuricAcid            = new Materials( 720, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16                , 255, 128,   0,   0,   "SulfuricAcid"            ,   "Sulfuric Acid"                 ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 1, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 4)));
        Materials.Tanzanite               = new Materials( 508, TextureSet.SET_GEM_VERTICAL      ,   7.0F,    256,  2, 1  |4|8      |64          ,  64,   0, 200, 127,   "Tanzanite"               ,   "Tanzanite"                     ,    0,       0,         -1,    0, false,  true,   5,   1,   1, Dyes.dyePurple      , 0, Arrays.asList(new MaterialStack(Calcium, 2), new MaterialStack(Aluminium, 3), new MaterialStack(Silicon, 3), new MaterialStack(Hydrogen, 1), new MaterialStack(Oxygen, 13)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 5), new TCAspects.TC_AspectStack(TCAspects.VITREUS, 3)));
        Materials.Tetrahedrite            = new Materials( 840, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 200,  32,   0,   0,   "Tetrahedrite"            ,   "Tetrahedrite"                  ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(Copper, 3), new MaterialStack(Antimony, 1), new MaterialStack(Sulfur, 3), new MaterialStack(Iron, 1))); //Cu3SbS3 + x(Fe,Zn)6Sb2S9
        Materials.TinAlloy                = new Materials( 363, TextureSet.SET_METALLIC          ,   6.5F,     96,  2, 1|2          |64|128      , 200, 200, 200,   0,   "TinAlloy"                ,   "Tin Alloy"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Tin, 1), new MaterialStack(Iron, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 1)));
        Materials.Topaz                   = new Materials( 507, TextureSet.SET_GEM_HORIZONTAL    ,   7.0F,    256,  3, 1  |4|8      |64          , 255, 128,   0, 127,   "Topaz"                   ,   "Topaz"                         ,    0,       0,         -1,    0, false,  true,   5,   1,   1, Dyes.dyeOrange      , 0, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 1), new MaterialStack(Fluorine, 2), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 6)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 6), new TCAspects.TC_AspectStack(TCAspects.VITREUS, 4)));
        Materials.Tungstate               = new Materials( 841, TextureSet.SET_DULL              ,   1.0F,      0,  3, 1    |8                   ,  55,  50,  35,   0,   "Tungstate"               ,   "Tungstate"                     ,    0,       0,       2500, 2500,  true, false,   4,   1,   1, Dyes.dyeBlack       , 0, Arrays.asList(new MaterialStack(Tungsten, 1), new MaterialStack(Lithium, 2), new MaterialStack(Oxygen, 4)));
        Materials.Ultimet                 = new Materials( 344, TextureSet.SET_SHINY             ,   9.0F,   2048,  4, 1|2          |64|128      , 180, 180, 230,   0,   "Ultimet"                 ,   "Ultimet"                       ,    0,       0,       2700, 2700,  true, false,   1,   1,   1, Dyes.dyeLightBlue   , 1, Arrays.asList(new MaterialStack(Cobalt, 5), new MaterialStack(Chrome, 2), new MaterialStack(Nickel, 1), new MaterialStack(Molybdenum, 1))); // 54% Cobalt, 26% Chromium, 9% Nickel, 5% Molybdenum, 3% Iron, 2% Tungsten, 0.8% Manganese, 0.3% Silicon, 0.08% Nitrogen and 0.06% Carbon
        Materials.Uraninite               = new Materials( 922, TextureSet.SET_METALLIC          ,   1.0F,      0,  3, 1    |8                   ,  35,  35,  35,   0,   "Uraninite"               ,   "Uraninite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLime        , 2, Arrays.asList(new MaterialStack(Uranium, 1), new MaterialStack(Oxygen, 2)));
        Materials.Uvarovite               = new Materials( 842, TextureSet.SET_DIAMOND           ,   1.0F,      0,  2, 1    |8                   , 180, 255, 180,   0,   "Uvarovite"               ,   "Uvarovite"                     ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeGreen       , 1, Arrays.asList(new MaterialStack(Calcium, 3), new MaterialStack(Chrome, 2), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12)));
        Materials.VanadiumGallium         = new Materials( 357, TextureSet.SET_SHINY             ,   1.0F,      0,  2, 1|2             |128      , 128, 128, 140,   0,   "VanadiumGallium"         ,   "Vanadium-Gallium"              ,    0,       0,       4500, 4500,  true, false,   1,   1,   1, Dyes.dyeGray        , 2, Arrays.asList(new MaterialStack(Vanadium, 3), new MaterialStack(Gallium, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.Wood                    = new Materials( 809, TextureSet.SET_WOOD              ,   2.0F,     16,  0, 1|2          |64|128      , 100,  50,   0,   0,   "Wood"                    ,   "Wood"                          ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       , 0, Arrays.asList(new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 1), new MaterialStack(Hydrogen, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2)));
        Materials.WroughtIron             = new Materials( 304, TextureSet.SET_METALLIC          ,   6.0F,    384,  2, 1|2          |64|128      , 200, 180, 180,   0,   "WroughtIron"             ,   "Wrought Iron"                  ,    0,       0,       1811,    0, false, false,   3,   1,   1, Dyes.dyeLightGray   , 2, Collections.singletonList(new MaterialStack(Iron, 1)));
        Materials.Wulfenite               = new Materials( 882, TextureSet.SET_DULL              ,   1.0F,      0,  3, 1    |8                   , 255, 128,   0,   0,   "Wulfenite"               ,   "Wulfenite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 2, Arrays.asList(new MaterialStack(Lead, 1), new MaterialStack(Molybdenum, 1), new MaterialStack(Oxygen, 4)));
        Materials.YellowLimonite          = new Materials( 931, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   , 200, 200,   0,   0,   "YellowLimonite"          ,   "Yellow Limonite"               ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Hydrogen, 1), new MaterialStack(Oxygen, 2))); // FeO(OH) + a bit of Ni and Co
        Materials.YttriumBariumCuprate    = new Materials( 358, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1|2                       ,  80,  64,  70,   0,   "YttriumBariumCuprate"    ,   "Yttrium Barium Cuprate"        ,    0,       0,       4500, 4500,  true, false,   1,   1,   1, Dyes.dyeGray        , 0, Arrays.asList(new MaterialStack(Yttrium, 1), new MaterialStack(Barium, 2), new MaterialStack(Copper, 3), new MaterialStack(Oxygen, 7))).disableAutoGeneratedVacuumFreezerRecipe();

        Materials.WoodSealed              = new Materials( 889, TextureSet.SET_WOOD              ,   3.0F,     24,  0, 1|2          |64|128      ,  80,  40,   0,   0,   "WoodSealed"              ,   "Sealed Wood"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       , 0, Collections.singletonList(new MaterialStack(Wood, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2), new TCAspects.TC_AspectStack(TCAspects.FABRICO, 1)));
        Materials.LiveRoot                = new Materials( 832, TextureSet.SET_WOOD              ,   1.0F,      0,  1, 1                         , 220, 200,   0,   0,   "LiveRoot"                ,   "Liveroot"                      ,    5,      16,         -1,    0, false, false,   2,   4,   3, Dyes.dyeBrown       , 2, Arrays.asList(new MaterialStack(Wood, 3), new MaterialStack(Magic, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2), new TCAspects.TC_AspectStack(TCAspects.VICTUS, 2), new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 1)));
        Materials.IronWood                = new Materials( 338, TextureSet.SET_WOOD              ,   6.0F,    384,  2, 1|2          |64|128      , 150, 140, 110,   0,   "IronWood"                ,   "Ironwood"                      ,    5,       8,         -1,    0, false, false,   2,  19,  18, Dyes.dyeBrown       , 2, Arrays.asList(new MaterialStack(Iron, 9), new MaterialStack(LiveRoot, 9), new MaterialStack(Gold, 1)));
        Materials.Glass                   = new Materials( 890, TextureSet.SET_GLASS             ,   1.0F,      4,  0, 1  |4                     , 250, 250, 250, 220,   "Glass"                   ,   "Glass"                         ,    0,       0,       1500,    0, false,  true,   1,   1,   1, Dyes.dyeWhite       , 2, Collections.singletonList(new MaterialStack(SiliconDioxide, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.VITREUS, 2)));
        Materials.BorosilicateGlass  = new MaterialBuilder(611, TextureSet.SET_GLASS             ,                                                                                                     "Borosilicate Glass").addDustItems().addMetalItems().setRGB(230, 243, 230).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Boron, 1), new MaterialStack(Glass, 7)).addCentrifugeRecipe().constructMaterial();
        Materials.Perlite                 = new Materials( 925, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   ,  30,  20,  30,   0,   "Perlite"                 ,   "Perlite"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Obsidian, 2), new MaterialStack(Water, 1)));
        Materials.Borax                   = new Materials( 941, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1    |8                   , 250, 250, 250,   0,   "Borax"                   ,   "Borax"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Sodium, 2), new MaterialStack(Boron, 4), new MaterialStack(Oxygen, 7), new MaterialStack(Water, 10)));
        Materials.Lignite                 = new Materials( 538, TextureSet.SET_LIGNITE           ,   1.0F,      0,  0, 1  |4|8                   , 100,  70,  70,   0,   "Lignite"                 ,   "Lignite Coal"                  ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 1, Arrays.asList(new MaterialStack(Carbon, 3), new MaterialStack(Water, 1)));
        Materials.Olivine                 = new Materials( 505, TextureSet.SET_RUBY              ,   7.0F,    256,  2, 1  |4|8      |64          , 150, 255, 150, 127,   "Olivine"                 ,   "Olivine"                       ,    0,       0,         -1,    0, false,  true,   5,   1,   1, Dyes.dyeLime        , 1, Arrays.asList(new MaterialStack(Magnesium, 2), new MaterialStack(Iron, 1), new MaterialStack(SiliconDioxide, 2)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 4), new TCAspects.TC_AspectStack(TCAspects.VITREUS, 2)));
        Materials.Opal                    = new Materials( 510, TextureSet.SET_OPAL              ,   7.0F,    256,  2, 1  |4|8      |64          ,   0,   0, 255,   0,   "Opal"                    ,   "Opal"                          ,    0,       0,         -1,    0, false,  true,   3,   1,   1, Dyes.dyeBlue        , 1, Collections.singletonList(new MaterialStack(SiliconDioxide, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 5), new TCAspects.TC_AspectStack(TCAspects.VITREUS, 3)));
        Materials.Amethyst                = new Materials( 509, TextureSet.SET_FLINT             ,   7.0F,    256,  3, 1  |4|8      |64          , 210,  50, 210, 127,   "Amethyst"                ,   "Amethyst"                      ,    0,       0,         -1,    0, false,  true,   3,   1,   1, Dyes.dyePink        , 1, Arrays.asList(new MaterialStack(SiliconDioxide, 4), new MaterialStack(Iron, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 6), new TCAspects.TC_AspectStack(TCAspects.VITREUS, 4)));
        Materials.Redstone                = new Materials( 810, TextureSet.SET_ROUGH             ,   1.0F,      0,  2, 1    |8                   , 200,   0,   0,   0,   "Redstone"                ,   "Redstone"                      ,    0,       0,        500,    0, false, false,   3,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(Silicon, 1), new MaterialStack(Pyrite, 5), new MaterialStack(Ruby, 1), new MaterialStack(Mercury, 3)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.MACHINA, 1), new TCAspects.TC_AspectStack(TCAspects.POTENTIA, 2)));
        Materials.Lapis                   = new Materials( 526, TextureSet.SET_LAPIS             ,   1.0F,      0,  1, 1  |4|8                   ,  70,  70, 220,   0,   "Lapis"                   ,   "Lapis"                         ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeBlue        , 2, Arrays.asList(new MaterialStack(Lazurite, 12), new MaterialStack(Sodalite, 2), new MaterialStack(Pyrite, 1), new MaterialStack(Calcite, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.SENSUS, 1)));
        Materials.Blaze                   = new Materials( 801, TextureSet.SET_POWDER            ,   2.0F,     16,  1, 1            |64          , 255, 200,   0,   0,   "Blaze"                   ,   "Blaze"                         ,    0,       0,       6400,    0, false, false,   2,   3,   2, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(DarkAsh, 1), new MaterialStack(Sulfur, 1), new MaterialStack(Magic, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 2), new TCAspects.TC_AspectStack(TCAspects.IGNIS, 4)));
        Materials.EnderPearl              = new Materials( 532, TextureSet.SET_SHINY             ,   1.0F,     16,  1, 1  |4                     , 108, 220, 200,   0,   "EnderPearl"              ,   "Enderpearl"                    ,    0,       0,         -1,    0, false, false,   1,  16,  10, Dyes.dyeGreen       , 1, Arrays.asList(new MaterialStack(Beryllium, 1), new MaterialStack(Potassium, 4), new MaterialStack(Nitrogen, 5), new MaterialStack(Magic, 6)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 4), new TCAspects.TC_AspectStack(TCAspects.ITER, 4), new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 2)));
        Materials.EnderEye                = new Materials( 533, TextureSet.SET_SHINY             ,   1.0F,     16,  1, 1  |4                     , 160, 250, 230,   0,   "EnderEye"                ,   "Endereye"                      ,    5,      10,         -1,    0, false, false,   1,   2,   1, Dyes.dyeGreen       , 2, Arrays.asList(new MaterialStack(EnderPearl, 1), new MaterialStack(Blaze, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.SENSUS, 4), new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 4), new TCAspects.TC_AspectStack(TCAspects.ITER, 4), new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 3), new TCAspects.TC_AspectStack(TCAspects.IGNIS, 2)));
        Materials.Flint                   = new Materials( 802, TextureSet.SET_FLINT             ,   2.5F,    128,  1, 1            |64          ,   0,  32,  64,   0,   "Flint"                   ,   "Flint"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeGray        , 2, Collections.singletonList(new MaterialStack(SiliconDioxide, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.TERRA, 1), new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 1)));
        Materials.Diatomite               = new Materials( 948, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   , 225, 225, 225,   0,   "Diatomite"               ,   "Diatomite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeGray        , 2, Arrays.asList(new MaterialStack(Flint, 8), new MaterialStack(BandedIron, 1), new MaterialStack(Sapphire, 1)));
        Materials.VolcanicAsh             = new Materials( 940, TextureSet.SET_FLINT             ,   1.0F,      0,  0, 1                         ,  60,  50,  50,   0,   "VolcanicAsh"             ,   "Volcanic Ashes"                ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Flint, 6), new MaterialStack(Iron, 1), new MaterialStack(Magnesium, 1)));
        Materials.Niter                   = new Materials( 531, TextureSet.SET_FLINT             ,   1.0F,      0,  1, 1  |4|8                   , 255, 200, 200,   0,   "Niter"                   ,   "Niter"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyePink        , 2, Collections.singletonList(new MaterialStack(Saltpeter, 1)));
        Materials.Pyrotheum               = new Materials( 843, TextureSet.SET_FIERY             ,   1.0F,      0,  1, 1                         , 255, 128,   0,   0,   "Pyrotheum"               ,   "Pyrotheum"                     ,    2,      62,         -1,    0, false, false,   2,   4,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Coal, 1), new MaterialStack(Redstone, 1), new MaterialStack(Blaze, 1), new MaterialStack(Sulfur, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 2), new TCAspects.TC_AspectStack(TCAspects.IGNIS, 1))).disableAutoGeneratedRecycleRecipes();;
        Materials.Cryotheum               = new Materials( 898, TextureSet.SET_SHINY             ,   1.0F,      0,  1, 1                         ,   0, 148, 203,   0,   "Cryotheum"               ,   "Cryotheum"                     ,    2,      62,         -1,    0, false, false,   2,   4,   1, Dyes.dyeLightBlue   , 2, Arrays.asList(new MaterialStack(Saltpeter, 1), new MaterialStack(Redstone, 1), new MaterialStack(Snow, 1), new MaterialStack(Blizz, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 2), new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 1),  new TCAspects.TC_AspectStack(TCAspects.GELUM, 1))).disableAutoGeneratedRecycleRecipes();;
        Materials.HydratedCoal            = new Materials( 818, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1                         ,  70,  70, 100,   0,   "HydratedCoal"            ,   "Hydrated Coal"                 ,    0,       0,         -1,    0, false, false,   1,   9,   8, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Coal, 8), new MaterialStack(Water, 1)));
        Materials.Apatite                 = new Materials( 530, TextureSet.SET_DIAMOND           ,   1.0F,      0,  1, 1  |4|8                   , 200, 200, 255,   0,   "Apatite"                 ,   "Apatite"                       ,    0,       0,         -1,    0, false, false,   2,   1,   1, Dyes.dyeCyan        , 1, Arrays.asList(new MaterialStack(Calcium, 5), new MaterialStack(Phosphate, 3), new MaterialStack(Chlorine, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.MESSIS, 2)));
        Materials.Alumite                 = new Materials( 400, TextureSet.SET_METALLIC          ,   5.0F,    768,  2, 1|2          |64|128      , 255, 105, 180,   0,   "Alumite"                 ,   "Obzinite"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyePink        , 2, Arrays.asList(new MaterialStack(Zinc, 5), new MaterialStack(Steel, 2), new MaterialStack(Obsidian, 2)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.STRONTIO, 2)));
        Materials.Manyullyn               = new Materials( 386, TextureSet.SET_SHINY             ,  25.0F,   2048,  5, 1|2  |8      |64|128      , 154,  76, 185,   0,   "Manyullyn"               ,   "Manyullyn"                     ,    0,       0,       3600, 3600,  true, false,   1,   1,   1, Dyes.dyePurple      , 2, Arrays.asList(new MaterialStack(Cobalt, 1), new MaterialStack(Ardite, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.STRONTIO, 2))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.Steeleaf                = new Materials( 339, TextureSet.SET_LEAF              ,   8.0F,    768,  3, 1|2          |64|128      ,  50, 127,  50,   0,   "Steeleaf"                ,   "Steeleaf"                      ,    5,      24,         -1,    0, false, false,   4,   1,   1, Dyes.dyeGreen       , 2, Arrays.asList(new MaterialStack(Steel, 1), new MaterialStack(Magic, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.HERBA, 2), new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 1)));
        Materials.Knightmetal             = new Materials( 362, TextureSet.SET_METALLIC          ,   8.0F,   1024,  3, 1|2          |64|128      , 210, 240, 200,   0,   "Knightmetal"             ,   "Knightmetal"                   ,    5,      24,         -1,    0, false, false,   4,   1,   1, Dyes.dyeLime        , 2, Arrays.asList(new MaterialStack(Steel, 2), new MaterialStack(Magic, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 1), new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2)));
        Materials.SterlingSilver          = new Materials( 350, TextureSet.SET_SHINY             ,  13.0F,    128,  2, 1|2          |64|128      , 250, 220, 225,   0,   "SterlingSilver"          ,   "Sterling Silver"               ,    0,       0,         -1, 1700,  true, false,   4,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Copper, 1), new MaterialStack(Silver, 4)));
        Materials.RoseGold                = new Materials( 351, TextureSet.SET_SHINY             ,  14.0F,    128,  2, 1|2          |64|128      , 255, 230,  30,   0,   "RoseGold"                ,   "Rose Gold"                     ,    0,       0,         -1, 1600,  true, false,   4,   1,   1, Dyes.dyeOrange      , 2, Arrays.asList(new MaterialStack(Copper, 1), new MaterialStack(Gold, 4)));
        Materials.BlackBronze             = new Materials( 352, TextureSet.SET_DULL              ,  12.0F,    256,  2, 1|2          |64|128      , 100,  50, 125,   0,   "BlackBronze"             ,   "Black Bronze"                  ,    0,       0,         -1, 2000,  true, false,   4,   1,   1, Dyes.dyePurple      , 2, Arrays.asList(new MaterialStack(Gold, 1), new MaterialStack(Silver, 1), new MaterialStack(Copper, 3)));
        Materials.BismuthBronze           = new Materials( 353, TextureSet.SET_DULL              ,   8.0F,    256,  2, 1|2          |64|128      , 100, 125, 125,   0,   "BismuthBronze"           ,   "Bismuth Bronze"                ,    0,       0,         -1, 1100,  true, false,   4,   1,   1, Dyes.dyeCyan        , 2, Arrays.asList(new MaterialStack(Bismuth, 1), new MaterialStack(Zinc, 1), new MaterialStack(Copper, 3)));
        Materials.BlackSteel              = new Materials( 334, TextureSet.SET_METALLIC          ,   6.5F,    768,  3, 1|2          |64|128      , 100, 100, 100,   0,   "BlackSteel"              ,   "Black Steel"                   ,    0,       0,         -1, 1200,  true, false,   4,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Nickel, 1), new MaterialStack(BlackBronze, 1), new MaterialStack(Steel, 3))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.RedSteel                = new Materials( 348, TextureSet.SET_METALLIC          ,   7.0F,    896,  4, 1|2          |64|128      , 140, 100, 100,   0,   "RedSteel"                ,   "Red Steel"                     ,    0,       0,         -1, 1300,  true, false,   4,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(SterlingSilver, 1), new MaterialStack(BismuthBronze, 1), new MaterialStack(Steel, 2), new MaterialStack(BlackSteel, 4))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.BlueSteel               = new Materials( 349, TextureSet.SET_METALLIC          ,   7.5F,   1024,  4, 1|2          |64|128      , 100, 100, 140,   0,   "BlueSteel"               ,   "Blue Steel"                    ,    0,       0,         -1, 1400,  true, false,   4,   1,   1, Dyes.dyeBlue        , 2, Arrays.asList(new MaterialStack(RoseGold, 1), new MaterialStack(Brass, 1), new MaterialStack(Steel, 2), new MaterialStack(BlackSteel, 4))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.DamascusSteel           = new Materials( 335, TextureSet.SET_METALLIC          ,   8.0F,   1280,  3, 1|2          |64|128      , 110, 110, 110,   0,   "DamascusSteel"           ,   "Damascus Steel"                ,    0,       0,       2000, 1500,  true, false,   4,   1,   1, Dyes.dyeGray        , 2, Collections.singletonList(new MaterialStack(Steel, 1))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.TungstenSteel           = new Materials( 316, TextureSet.SET_METALLIC          ,   8.0F,   2560,  4, 1|2          |64|128      , 100, 100, 160,   0,   "TungstenSteel"           ,   "Tungstensteel"                 ,    0,       0,       4000, 4000,  true, false,   4,   1,   1, Dyes.dyeBlue        , 2, Arrays.asList(new MaterialStack(Steel, 1), new MaterialStack(Tungsten, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.NitroCoalFuel           = new Materials(  -1, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16                ,  50,  70,  50,   0,   "NitroCoalFuel"           ,   "Nitro-Coalfuel"                ,    0,      48,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 0, Arrays.asList(new MaterialStack(Glyceryl, 1), new MaterialStack(CoalFuel, 4)));
        Materials.NitroFuel               = new Materials( 709, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16                , 200, 255,   0,   0,   "NitroFuel"               ,   "Cetane-Boosted Diesel"         ,    0,    1000,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLime        );
        Materials.RedAlloy                = new Materials( 308, TextureSet.SET_DULL              ,   1.0F,      0,  0, 1|2                       , 200,   0,   0,   0,   "RedAlloy"                ,   "Red Alloy"                     ,    0,       0,        500,    0, false, false,   3,   5,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(Copper, 1), new MaterialStack(Redstone, 4)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.MACHINA, 3)));
        Materials.CobaltBrass             = new Materials( 343, TextureSet.SET_METALLIC          ,   8.0F,    256,  2, 1|2          |64|128      , 180, 180, 160,   0,   "CobaltBrass"             ,   "Cobalt Brass"                  ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeOrange      , 2, Arrays.asList(new MaterialStack(Brass, 7), new MaterialStack(Tin, 1), new MaterialStack(Cobalt, 1)));
        Materials.TricalciumPhosphate     = new Materials( 534, TextureSet.SET_FLINT             ,   1.0F,      0, 2,  1|4|8|16                  , 255, 255,   0,   0,   "TricalciumPhosphate"     ,   "Tricalcium Phosphate"          ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Calcium, 3), new MaterialStack(Phosphate, 2)));
        Materials.Basalt                  = new Materials( 844, TextureSet.SET_ROUGH             ,   1.0F,     64,  1, 1            |64|128      ,  30,  20,  20,   0,   "Basalt"                  ,   "Basalt"                        ,    0,       0,         -1,    0, false, false,   2,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Olivine, 1), new MaterialStack(Calcite, 3), new MaterialStack(Flint, 8), new MaterialStack(DarkAsh, 4)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.TENEBRAE, 1))).disableAutoGeneratedRecycleRecipes();
        Materials.GarnetRed               = new Materials( 527, TextureSet.SET_RUBY              ,   7.0F,    128,  2, 1  |4|8      |64          , 200,  80,  80, 127,   "GarnetRed"               ,   "Red Garnet"                    ,    0,       0,         -1,    0, false,  true,   4,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(Pyrope, 3), new MaterialStack(Almandine, 5), new MaterialStack(Spessartine, 8)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.VITREUS, 3)));
        Materials.GarnetYellow            = new Materials( 528, TextureSet.SET_RUBY              ,   7.0F,    128,  2, 1  |4|8      |64          , 200, 200,  80, 127,   "GarnetYellow"            ,   "Yellow Garnet"                 ,    0,       0,         -1,    0, false,  true,   4,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Andradite, 5), new MaterialStack(Grossular, 8), new MaterialStack(Uvarovite, 3)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.VITREUS, 3)));
        Materials.Marble                  = new Materials( 845, TextureSet.SET_FINE              ,   1.0F,     16,  1, 1            |64|128      , 200, 200, 200,   0,   "Marble"                  ,   "Marble"                        ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Magnesium, 1), new MaterialStack(Calcite, 7)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 1)));
        Materials.Sugar                   = new Materials( 803, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1                         , 250, 250, 250,   0,   "Sugar"                   ,   "Sugar"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Carbon, 2), new MaterialStack(Water, 5), new MaterialStack(Oxygen, 25)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.HERBA, 1), new TCAspects.TC_AspectStack(TCAspects.AQUA, 1), new TCAspects.TC_AspectStack(TCAspects.AER, 1)));
        Materials.Thaumium                = new Materials( 330, TextureSet.SET_METALLIC          ,  12.0F,    256,  3, 1|2          |64|128      , 150, 100, 200,   0,   "Thaumium"                ,   "Thaumium"                      ,    0,       0,         -1,    0, false, false,   5,   2,   1, Dyes.dyePurple      , 0, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Magic, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 1)));
        Materials.Vinteum                 = new Materials( 529, TextureSet.SET_METALLIC          ,  10.0F,    128,  3, 1|2|4|8      |64|128      , 100, 200, 255,   0,   "Vinteum"                 ,   "Vinteum"                       ,    5,      32,         -1,    0, false, false,   4,   1,   1, Dyes.dyeLightBlue   , 2, Collections.singletonList(new MaterialStack(Thaumium, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.VITREUS, 2), new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 1)));
        Materials.Vis                     = new Materials(  -1, TextureSet.SET_SHINY             ,   1.0F,      0,  3, 0                         , 128,   0, 255,   0,   "Vis"                     ,   "Vis"                           ,    5,      32,         -1,    0, false, false,   1,   1,   1, Dyes.dyePurple      , 2, Collections.singletonList(new MaterialStack(Magic, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.AURAM, 2), new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 1)));
        Materials.Redrock                 = new Materials( 846, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1                         , 255,  80,  50,   0,   "Redrock"                 ,   "Redrock"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(Calcite, 2), new MaterialStack(Flint, 1), new MaterialStack(Clay, 1)));
        Materials.PotassiumFeldspar       = new Materials( 847, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1                         , 120,  40,  40,   0,   "PotassiumFeldspar"       ,   "Potassium Feldspar"            ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyePink        , 0, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Aluminium, 1), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 8)));
        Materials.Biotite                 = new Materials( 848, TextureSet.SET_METALLIC          ,   1.0F,      0,  1, 1                         ,  20,  30,  20,   0,   "Biotite"                 ,   "Biotite"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeGray        , 0, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Magnesium, 3), new MaterialStack(Aluminium, 3), new MaterialStack(Fluorine, 2), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 10)));
        Materials.GraniteBlack            = new Materials( 849, TextureSet.SET_ROUGH             ,   4.0F,     64,  3, 1            |64|128      ,  10,  10,  10,   0,   "GraniteBlack"            ,   "Black Granite"                 ,    0,       0,         -1,    0, false, false,   0,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(SiliconDioxide, 4), new MaterialStack(Biotite, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 1)));
        Materials.GraniteRed              = new Materials( 850, TextureSet.SET_ROUGH             ,   4.0F,     64,  3, 1            |64|128      , 255,   0, 128,   0,   "GraniteRed"              ,   "Red Granite"                   ,    0,       0,         -1,    0, false, false,   0,   1,   1, Dyes.dyeMagenta     , 0, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(PotassiumFeldspar, 1), new MaterialStack(Oxygen, 3)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.TUTAMEN, 1)));
        Materials.Chrysotile              = new Materials( 912, TextureSet.SET_DULL              ,  32.0F,  10240,  3, 1|2  |8      |64|128      , 110, 140, 110,   0,   "Chrysotile"              ,   "Chrysotile"                    ,    0,       0,       9400, 9400,  true, false,   1,   1,   1, Dyes.dyeWhite       , 2, Collections.singletonList(new MaterialStack(Asbestos, 1))).disableAutoGeneratedBlastFurnaceRecipes().setTurbineMultipliers(280, 280, 1);
        Materials.Realgar                 = new Materials( 913, TextureSet.SET_DULL              ,   1.0F,     32,  1, 1|2  |8      |64|128      , 140, 100, 100,   0,   "Realgar"                 ,   "Realgar"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Arsenic, 4), new MaterialStack(Sulfur,4)));
        Materials.VanadiumMagnetite       = new Materials( 923, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   ,  35,  35,  60,   0,   "VanadiumMagnetite"       ,   "Vanadium Magnetite"            ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Magnetite, 1), new MaterialStack(Vanadium, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 1))); // Mixture of Fe3O4 and V2O5
        Materials.BasalticMineralSand     = new Materials( 935, TextureSet.SET_SAND              ,   1.0F,      0,  1, 1    |8                   ,  40,  50,  40,   0,   "BasalticMineralSand"     ,   "Basaltic Mineral Sand"         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Magnetite, 1), new MaterialStack(Basalt, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 1)));
        Materials.GraniticMineralSand     = new Materials( 936, TextureSet.SET_SAND              ,   1.0F,      0,  1, 1    |8                   ,  40,  60,  60,   0,   "GraniticMineralSand"     ,   "Granitic Mineral Sand"         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Magnetite, 1), new MaterialStack(GraniteBlack, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 1)));
        Materials.GarnetSand              = new Materials( 938, TextureSet.SET_SAND              ,   1.0F,      0,  1, 1    |8                   , 200, 100,   0,   0,   "GarnetSand"              ,   "Garnet Sand"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 2, Arrays.asList(new MaterialStack(GarnetRed, 1), new MaterialStack(GarnetYellow, 1)));
        Materials.QuartzSand              = new Materials( 939, TextureSet.SET_SAND              ,   1.0F,      0,  1, 1    |8                   , 194, 178, 128,   0,   "QuartzSand"              ,   "Quartz Sand"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(CertusQuartz, 1), new MaterialStack(Quartzite, 1)));
        Materials.Bastnasite              = new Materials( 905, TextureSet.SET_FINE              ,   1.0F,      0,  2, 1    |8                   , 200, 110,  45,   0,   "Bastnasite"              ,   "Bastnasite"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Cerium, 1), new MaterialStack(Carbon, 1), new MaterialStack(Fluorine, 1), new MaterialStack(Oxygen, 3))); // (Ce, La, Y)CO3F
        Materials.Pentlandite             = new Materials( 909, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 165, 150,   5,   0,   "Pentlandite"             ,   "Pentlandite"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Nickel, 9), new MaterialStack(Sulfur, 8))); // (Fe,Ni)9S8
        Materials.Spodumene               = new Materials( 920, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 190, 170, 170,   0,   "Spodumene"               ,   "Spodumene"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Lithium, 1), new MaterialStack(Aluminium, 1), new MaterialStack(Silicon, 2), new MaterialStack(Oxygen, 6))); // LiAl(SiO3)2
        Materials.Pollucite               = new Materials( 919, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 240, 210, 210,   0,   "Pollucite"               ,   "Pollucite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Caesium, 2), new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 4), new MaterialStack(Water, 2), new MaterialStack(Oxygen, 12))); // (Cs,Na)2Al2Si4O12 2H2O (also a source of Rb)
        Materials.Tantalite               = new Materials( 921, TextureSet.SET_METALLIC          ,   1.0F,      0,  3, 1    |8                   , 145,  80,  40,   0,   "Tantalite"               ,   "Tantalite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Manganese, 1), new MaterialStack(Tantalum, 2), new MaterialStack(Oxygen, 6))); // (Fe, Mn)Ta2O6 (also source of Nb)
        Materials.Lepidolite              = new Materials( 907, TextureSet.SET_FINE              ,   1.0F,      0,  2, 1    |8                   , 240,  50, 140,   0,   "Lepidolite"              ,   "Lepidolite"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Lithium, 3), new MaterialStack(Aluminium, 4), new MaterialStack(Fluorine, 2), new MaterialStack(Oxygen, 10))); // K(Li,Al,Rb)3(Al,Si)4O10(F,OH)2
        Materials.Glauconite              = new Materials( 933, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 130, 180,  60,   0,   "Glauconite"              ,   "Glauconite"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Magnesium, 2), new MaterialStack(Aluminium, 4), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 12))); // (K,Na)(Fe3+,Al,Mg)2(Si,Al)4O10(OH)2
        Materials.GlauconiteSand          = new Materials( 949, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 130, 180,  60,   0,   "GlauconiteSand"          ,   "Glauconite Sand"               ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Magnesium, 2), new MaterialStack(Aluminium, 4), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 12))); // (K,Na)(Fe3+,Al,Mg)2(Si,Al)4O10(OH)2
        Materials.Vermiculite             = new Materials( 932, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   , 200, 180,  15,   0,   "Vermiculite"             ,   "Vermiculite"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Iron, 3), new MaterialStack(Aluminium, 4), new MaterialStack(Silicon, 4), new MaterialStack(Hydrogen, 2), new MaterialStack(Water, 4), new MaterialStack(Oxygen, 12))); // (Mg+2, Fe+2, Fe+3)3 [(AlSi)4O10] (OH)2 4H2O)
        Materials.Bentonite               = new Materials( 927, TextureSet.SET_ROUGH             ,   1.0F,      0,  2, 1    |8                   , 245, 215, 210,   0,   "Bentonite"               ,   "Bentonite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Sodium, 1), new MaterialStack(Magnesium, 6), new MaterialStack(Silicon, 12), new MaterialStack(Hydrogen, 6), new MaterialStack(Water, 5), new MaterialStack(Oxygen, 36))); // (Na,Ca)0.33(Al,Mg)2(Si4O10)(OH)2 nH2O
        Materials.FullersEarth            = new Materials( 928, TextureSet.SET_FINE              ,   1.0F,      0,  2, 1    |8                   , 160, 160, 120,   0,   "FullersEarth"            ,   "Fullers Earth"                 ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Magnesium, 1), new MaterialStack(Silicon, 4), new MaterialStack(Hydrogen, 1), new MaterialStack(Water, 4), new MaterialStack(Oxygen, 11))); // (Mg,Al)2Si4O10(OH) 4(H2O)
        Materials.Pitchblende             = new Materials( 873, TextureSet.SET_DULL              ,   1.0F,      0,  3, 1    |8                   , 200, 210,   0,   0,   "Pitchblende"             ,   "Pitchblende"                   ,    0,       0,         -1,    0, false, false,   5,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Uraninite, 3), new MaterialStack(Thorium, 1), new MaterialStack(Lead, 1)));
        Materials.Monazite                = new Materials( 520, TextureSet.SET_DIAMOND           ,   1.0F,      0,  1, 1  |4|8                   ,  50,  70,  50,   0,   "Monazite"                ,   "Monazite"                      ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeGreen       , 1, Arrays.asList(new MaterialStack(RareEarth, 1), new MaterialStack(Phosphate, 1))); // Wikipedia: (Ce, La, Nd, Th, Sm, Gd)PO4 Monazite also smelt-extract to Helium, it is brown like the rare earth Item Monazite sand deposits are inevitably of the monazite-(Ce) composition. Typically, the lanthanides in such monazites contain about 45.8% cerium, about 24% lanthanum, about 17% neodymium, about 5% praseodymium, and minor quantities of samarium, gadolinium, and yttrium. Europium concentrations tend to be low, about 0.05% Thorium content of monazite is variable.
        Materials.Malachite               = new Materials( 871, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   ,   5,  95,   5,   0,   "Malachite"               ,   "Malachite"                     ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeGreen       , 1, Arrays.asList(new MaterialStack(Copper, 2), new MaterialStack(Carbon, 1), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 5))); // Cu2CO3(OH)2
        Materials.Mirabilite              = new Materials( 900, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 240, 250, 210,   0,   "Mirabilite"              ,   "Mirabilite"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Sodium, 2), new MaterialStack(Sulfur, 1), new MaterialStack(Water, 10), new MaterialStack(Oxygen, 4))); // Na2SO4 10H2O
        Materials.Mica                    = new Materials( 901, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1    |8                   , 195, 195, 205,   0,   "Mica"                    ,   "Mica"                          ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Aluminium, 3), new MaterialStack(Silicon, 3), new MaterialStack(Fluorine, 2), new MaterialStack(Oxygen, 10))); // KAl2(AlSi3O10)(F,OH)2
        Materials.Trona                   = new Materials( 903, TextureSet.SET_METALLIC          ,   1.0F,      0,  1, 1    |8                   , 135, 135,  95,   0,   "Trona"                   ,   "Trona"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Sodium, 3), new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 1), new MaterialStack(Water, 2), new MaterialStack(Oxygen, 6))); // Na3(CO3)(HCO3) 2H2O
        Materials.Barite                  = new Materials( 904, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 230, 235, 255,   0,   "Barite"                  ,   "Barite"                        ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Barium, 1), new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 4)));
        Materials.Gypsum                  = new Materials( 934, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   , 230, 230, 250,   0,   "Gypsum"                  ,   "Gypsum"                        ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 4), new MaterialStack(Water, 2))); // CaSO4 2H2O
        Materials.Alunite                 = new Materials( 911, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   , 225, 180,  65,   0,   "Alunite"                 ,   "Alunite"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Aluminium, 3), new MaterialStack(Silicon, 2), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 14))); // KAl3(SO4)2(OH)6
        Materials.Dolomite                = new Materials( 914, TextureSet.SET_FLINT             ,   1.0F,      0,  1, 1    |8                   , 225, 205, 205,   0,   "Dolomite"                ,   "Dolomite"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Magnesium, 1), new MaterialStack(Carbon, 2), new MaterialStack(Oxygen, 6))); // CaMg(CO3)2
        Materials.Wollastonite            = new Materials( 915, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 240, 240, 240,   0,   "Wollastonite"            ,   "Wollastonite"                  ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Silicon, 1), new MaterialStack(Oxygen, 3))); // CaSiO3
        Materials.Zeolite                 = new Materials( 916, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 240, 230, 230,   0,   "Zeolite"                 ,   "Zeolite"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Sodium, 1), new MaterialStack(Calcium, 4), new MaterialStack(Silicon, 27), new MaterialStack(Aluminium, 9), new MaterialStack(Oxygen, 72))); // NaCa4(Si27Al9)O72
        Materials.Kyanite                 = new Materials( 924, TextureSet.SET_FLINT             ,   1.0F,      0,  2, 1    |8                   , 110, 110, 250,   0,   "Kyanite"                 ,   "Kyanite"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 1), new MaterialStack(Oxygen, 5))); // Al2SiO5
        Materials.Kaolinite               = new Materials( 929, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 245, 235, 235,   0,   "Kaolinite"               ,   "Kaolinite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 2), new MaterialStack(Hydrogen, 4), new MaterialStack(Oxygen, 9))); // Al2Si2O5(OH)4
        Materials.Talc                    = new Materials( 902, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   ,  90, 180,  90,   0,   "Talc"                    ,   "Talc"                          ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Magnesium, 3), new MaterialStack(Silicon, 4), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 12))); // H2Mg3(SiO3)4
        Materials.Soapstone               = new Materials( 877, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   ,  95, 145,  95,   0,   "Soapstone"               ,   "Soapstone"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Magnesium, 3), new MaterialStack(Silicon, 4), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 12))); // H2Mg3(SiO3)4
        Materials.Concrete                = new Materials( 947, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1                         , 100, 100, 100,   0,   "Concrete"                ,   "Concrete"                      ,    0,       0,        300,    0, false, false,   0,   1,   1, Dyes.dyeGray        , 0, Collections.singletonList(new MaterialStack(Stone, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.TERRA, 1)));
        Materials.IronMagnetic            = new Materials( 354, TextureSet.SET_MAGNETIC          ,   6.0F,    256,  2, 1|2          |64|128      , 200, 200, 200,   0,   "IronMagnetic"            ,   "Magnetic Iron"                 ,    0,       0,         -1,    0, false, false,   4,  51,  50, Dyes.dyeGray        , 1, Collections.singletonList(new MaterialStack(Iron, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 1)));
        Materials.SteelMagnetic           = new Materials( 355, TextureSet.SET_MAGNETIC          ,   6.0F,    512,  3, 1|2          |64|128      , 128, 128, 128,   0,   "SteelMagnetic"           ,   "Magnetic Steel"                ,    0,       0,       1000, 1000,  true, false,   4,  51,  50, Dyes.dyeGray        , 1, Collections.singletonList(new MaterialStack(Steel, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1), new TCAspects.TC_AspectStack(TCAspects.ORDO, 1), new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 1)));
        Materials.NeodymiumMagnetic       = new Materials( 356, TextureSet.SET_MAGNETIC          ,   7.0F,    512,  2, 1|2          |64|128      , 100, 100, 100,   0,   "NeodymiumMagnetic"       ,   "Magnetic Neodymium"            ,    0,       0,       1297, 1297,  true, false,   4,  51,  50, Dyes.dyeGray        , 1, Collections.singletonList(new MaterialStack(Neodymium, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 1), new TCAspects.TC_AspectStack(TCAspects.MAGNETO, 3)));
        Materials.SamariumMagnetic        = new Materials( 399, TextureSet.SET_MAGNETIC          ,   1.0F,      0,  2, 1|2          |64|128      , 255, 255, 204,   0,   "SamariumMagnetic"        ,   "Magnetic Samarium"             ,    0,       0,       1345, 1345,  true, false,   4,   1,   1, Dyes.dyeWhite       , 1, Collections.singletonList(new MaterialStack(Samarium, 1)),Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.RADIO, 1), new TCAspects.TC_AspectStack(TCAspects.MAGNETO,10)));
        Materials.TungstenCarbide         = new Materials( 370, TextureSet.SET_METALLIC          ,  14.0F,   1280,  4, 1|2          |64|128      ,  51,   0, 102,   0,   "TungstenCarbide"         ,   "Tungstencarbide"               ,    0,       0,       2460, 2460,  true, false,   4,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Tungsten, 1), new MaterialStack(Carbon, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.VanadiumSteel           = new Materials( 371, TextureSet.SET_METALLIC          ,   3.0F,   1920,  3, 1|2          |64|128      , 192, 192, 192,   0,   "VanadiumSteel"           ,   "Vanadiumsteel"                 ,    0,       0,       1453, 1453,  true, false,   4,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Vanadium, 1), new MaterialStack(Chrome, 1), new MaterialStack(Steel, 7))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.HSSG                    = new Materials( 372, TextureSet.SET_METALLIC          ,  10.0F,   4000,  3, 1|2          |64|128      , 153, 153,   0,   0,   "HSSG"                    ,   "HSS-G"                         ,    0,       0,       4500, 4500,  true, false,   4,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(TungstenSteel, 5), new MaterialStack(Chrome, 1), new MaterialStack(Molybdenum, 2), new MaterialStack(Vanadium, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.HSSE                    = new Materials( 373, TextureSet.SET_METALLIC          ,  32.0F,  10240,  7, 1|2          |64|128      ,  51, 102,   0,   0,   "HSSE"                    ,   "HSS-E"                         ,    0,       0,       5400, 5400,  true, false,   4,   1,   1, Dyes.dyeGreen       , 2, Arrays.asList(new MaterialStack(HSSG, 6), new MaterialStack(Cobalt, 1),new MaterialStack(Manganese, 1), new MaterialStack(Silicon, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.HSSS                    = new Materials( 374, TextureSet.SET_METALLIC          ,  32.0F,  10240,  8, 1|2          |64|128      , 102,   0,  51,   0,   "HSSS"                    ,   "HSS-S"                         ,    0,       0,       5400, 5400,  true, false,   4,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(HSSG, 6), new MaterialStack(Iridium, 2), new MaterialStack(Osmium, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.TPV                     = new Materials( 576, TextureSet.SET_METALLIC          ,  16.0F,   4000,  5, 1|2          |64|128      , 250,  170,250,   0,   "TPVAlloy"                ,   "TPV-Alloy"                     ,    0,       0,       3000, 3000,  true, false,   4,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(Titanium, 3), new MaterialStack(Platinum, 3), new MaterialStack(Vanadium, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.DilutedSulfuricAcid     = new MaterialBuilder(640, TextureSet.SET_FLUID             ,                                                                                                     "Diluted Sulfuric Acid").addCell().addFluid().setRGB(192, 120, 32).setColor(Dyes.dyeOrange).setMaterialList(new MaterialStack(SulfuricAcid, 1)).constructMaterial();
        Materials.EpoxidFiberReinforced   = new Materials( 610, TextureSet.SET_DULL                 ,3.0F,     64,  1, 1|2          |64|128      , 160, 112,  16,   0,   "EpoxidFiberReinforced"   ,   "Fiber-Reinforced Epoxy Resin"  ,    0,       0,        400,    0, false, false,   1,   1,   1, Dyes.dyeBrown       , 2, Collections.singletonList(new MaterialStack(Epoxid, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.MOTUS, 2)));
        Materials.SodiumCarbonate         = new MaterialBuilder(695, TextureSet.SET_QUARTZ, "Sodium Carbonate").setToolSpeed(1.0F).setDurability(64).setToolQuality(1).addDustItems().setRGB(255, 255, 235).setColor(Dyes.dyeWhite).setBlastFurnaceTemp(851 ).setMeltingPoint(851 ).setBlastFurnaceRequired(false).setExtraData(1).setMaterialList(new MaterialStack(Sodium, 2), new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 3)).constructMaterial().disableAutoGeneratedBlastFurnaceRecipes();
        Materials.SodiumAluminate         = new MaterialBuilder(696, TextureSet.SET_QUARTZ, "Sodium Aluminate").setToolSpeed(1.0F).setDurability(64).setToolQuality(1).addDustItems().setRGB(255, 235, 255).setColor(Dyes.dyeWhite).setBlastFurnaceTemp(1800).setMeltingPoint(1800).setBlastFurnaceRequired(false).setExtraData(0).setMaterialList(new MaterialStack(Sodium, 1), new MaterialStack(Aluminium, 1), new MaterialStack(Oxygen, 2)).constructMaterial().disableAutoGeneratedBlastFurnaceRecipes();
        Materials.Aluminiumoxide          = new MaterialBuilder(697, TextureSet.SET_QUARTZ, "Alumina").setToolSpeed(1.0F).setDurability(64).setToolQuality(1).addDustItems().setRGB(235, 255, 255).setColor(Dyes.dyeWhite).setBlastFurnaceTemp(2054).setMeltingPoint(2054).setBlastFurnaceRequired(true).setExtraData(0).setMaterialList(new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3)).setAspects(Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.GELUM, 2))).constructMaterial().disableAutoGeneratedBlastFurnaceRecipes();
        Materials.Aluminiumhydroxide      = new MaterialBuilder(698, TextureSet.SET_QUARTZ, "Aluminium Hydroxide").setToolSpeed(1.0F).setDurability(64).setToolQuality(1).addDustItems().setRGB(235, 235, 255).setColor(Dyes.dyeWhite).setBlastFurnaceTemp(1200).setMeltingPoint(1200).setBlastFurnaceRequired(true).setExtraData(0).setMaterialList(new MaterialStack(Aluminium, 1), new MaterialStack(Oxygen, 3),  new MaterialStack(Hydrogen, 3)).setAspects(Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.GELUM, 2))).constructMaterial().disableAutoGeneratedBlastFurnaceRecipes();
        Materials.Cryolite                = new MaterialBuilder(699, TextureSet.SET_QUARTZ, "Cryolite").setToolSpeed(1.0F).setDurability(64).setToolQuality(1).addOreItems().setRGB(191, 239, 255).setColor(Dyes.dyeLightBlue).setMeltingPoint(1012).setBlastFurnaceTemp(1012).setExtraData(0).setMaterialList(new MaterialStack(Sodium, 3), new MaterialStack(Aluminium, 1), new MaterialStack(Fluorine, 6)).constructMaterial().disableAutoGeneratedBlastFurnaceRecipes();
        Materials.RedMud                  = new MaterialBuilder(743, TextureSet.SET_FLUID, "Red Mud").addCell().addFluid().setRGB(140, 22, 22).setColor(Dyes.dyeRed).constructMaterial();

        Materials.Brick                   = new MaterialBuilder(625, TextureSet.SET_ROUGH      ,                                                                                                     "Brick").addDustItems().setRGB(155, 86, 67).setColor(Dyes.dyeBrown).setExtraData(0).setMaterialList(new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 4), new MaterialStack(Oxygen, 11)).constructMaterial();
        Materials.Fireclay                = new MaterialBuilder(626, TextureSet.SET_ROUGH      ,                                                                                                     "Fireclay").addDustItems().setRGB(173, 160, 155).setExtraData(2).setColor(Dyes.dyeBrown).setMaterialList(new MaterialStack(Brick, 1), new MaterialStack(Clay, 1)).constructMaterial();

        Materials.PotassiumNitrade     = loadPotassiumNitrade();
        Materials.ChromiumTrioxide = loadChromiumTrioxide();
        Materials.Nitrochlorobenzene = loadNitrochlorobenzene();
        Materials.Dimethylbenzene = loadDimethylbenzene();
        Materials.Potassiumdichromate = loadPotassiumdichromate();
        Materials.PhthalicAcid = loadPhthalicAcid();
        Materials.Dichlorobenzidine = loadDichlorobenzidine();
        Materials.Diaminobenzidin = loadDiaminobenzidin();
        Materials.Diphenylisophthalate = loadDiphenylisophthalate();
        Materials.Polybenzimidazole   = new Materials(599, TextureSet.SET_DULL                 ,3.0F,     64,  1, 1|2          |64|128      , 45, 45,  45,   0,   "Polybenzimidazole"   ,   "Polybenzimidazole"  ,    0,       0,        1450,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 0, Arrays.asList(new MaterialStack(Carbon, 20), new MaterialStack(Nitrogen, 4), new MaterialStack(Hydrogen, 12)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.ORDO, 2),new TCAspects.TC_AspectStack(TCAspects.VOLATUS, 1)));

        Materials.MTBEMixture        = loadMtbeMixture();
        Materials.MTBEMixtureAlt = loadMtbeMixtureAlt();
        Materials.NitrousOxide       = loadNitrousOxide();
        Materials.AntiKnock          = loadEthylTertButylEther();
        Materials.Octane             = loadOctane();
        Materials.GasolineRaw        = loadRawGasoline();
        Materials.GasolineRegular    = loadGasoline();
        Materials.GasolinePremium    = loadHighOctaneGasoline();

        Materials.Electrotine             = new Materials( 812, TextureSet.SET_SHINY             ,   1.0F,      0,  1, 1    |8                   ,  60, 180, 200,   0,   "Electrotine"             ,   "Electrotine"                   ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeCyan        , 0, Arrays.asList(new MaterialStack(Redstone, 1), new MaterialStack(Electrum, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 2)));
        Materials.Galgadorian             = new Materials( 384, TextureSet.SET_METALLIC          ,  16.0F,   3600,  3, 1|2          |64|128      , 154, 105, 119,   0,   "Galgadorian"             ,   "Galgadorian"                   ,    0,       0,       3000, 3000,  true, false,   1,   1,   1, Dyes.dyePink        ).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.EnhancedGalgadorian     = new Materials( 385, TextureSet.SET_METALLIC          ,  32.0F,   7200,  5, 1|2|          64|128      , 152, 93, 133,    0,   "EnhancedGalgadorian"     ,   "Enhanced Galgadorian"          ,    0,       0,       4500, 4500,  true, false,   1,   1,   1, Dyes.dyePink        ).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.BloodInfusedIron        = new Materials( 977, TextureSet.SET_METALLIC          ,  10.0F,    384,  2, 1|2          |64|128      ,  69,   9,  10,   0,   "BloodInfusedIron"        ,   "Blood Infused Iron"            ,    0,       0,       2400,    0, false, false,   3,   1,   1, Dyes.dyeRed         , Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 3),  new TCAspects.TC_AspectStack(TCAspects.PRAECANTATIO, 1)));
        Materials.Shadow                  = new Materials( 368, TextureSet.SET_METALLIC          ,  32.0F,   8192,  4, 1|2  |8      |64|128      ,  16,   3,  66,   0,   "Shadow"                  ,   "Shadow Metal"                  ,    0,       0,       1800, 1800,  true, false,   3,   4,   3, Dyes.dyeBlue        );

        Materials.Ledox                   = new Materials( 390, TextureSet.SET_SHINY             ,  15.0F,   1024,  4, 1|2  |8      |64|128      ,   0, 116, 255,   0,   "Ledox"                   ,   "Ledox"                         ,    0,       0,         -1,    0, false, false,   4,   1,   1, Dyes.dyeBlue        );
        Materials.Quantium                = new Materials( 391, TextureSet.SET_SHINY             ,  18.0F,   2048,  4, 1|2  |8      |64|128      ,   0, 209,  11,   0,   "Quantium"                ,   "Quantium"                      ,    0,       0,      9900,  9900,  true, false,   4,   1,   1, Dyes.dyeLime        ).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.Mytryl                  = new Materials( 387, TextureSet.SET_SHINY             ,   8.0F,    512,  4, 1|2  |8      |64|128      , 242, 100,   4,   0,   "Mytryl"                  ,   "Mytryl"                        ,    0,       0,      3600,  3600,  true, false,   4,   1,   1, Dyes.dyeOrange      );
        Materials.BlackPlutonium          = new Materials( 388, TextureSet.SET_DULL              ,  36.0F,   8192,  8, 1|2  |8      |64|128      ,  50,  50,  50,   0,   "BlackPlutonium"          ,   "Black Plutonium"               ,    0,       0,      9000,  9000,  true, false,   4,   1,   1, Dyes.dyeBlack       ).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.CallistoIce             = new Materials( 389, TextureSet.SET_SHINY             ,   9.0F,   1024,  4, 1|2  |8      |64|128      ,  30, 177, 255,   0,   "CallistoIce"             ,   "Callisto Ice"                  ,    0,       0,        -1,     0, false, false,   4,   1,   1, Dyes.dyeLightBlue   );
        Materials.Duralumin               = new Materials( 392, TextureSet.SET_SHINY             ,  16.0F,    512,  3, 1|2  |8      |64|128      , 235, 209, 160,   0,   "Duralumin"               ,   "Duralumin"                     ,    0,       0,      1600,  1600,  true, false,   4,   1,   1, Dyes.dyeOrange      , 2, Arrays.asList(new MaterialStack(Aluminium, 6), new MaterialStack(Copper, 1), new MaterialStack(Manganese, 1), new MaterialStack(Magnesium, 1)));
        Materials.Oriharukon              = new Materials( 393, TextureSet.SET_SHINY             ,  32.0F,  10240,  5, 1|2  |8   |32|64|128      , 103, 125, 104,   0,   "Oriharukon"              ,   "Oriharukon"                    ,    0,       0,      5400,  5400,  true, false,   4,   1,   1, Dyes.dyeLime        , Element.Oh, Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2),new TCAspects.TC_AspectStack(TCAspects.LUCRUM, 2), new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.MysteriousCrystal       = new Materials( 398, TextureSet.SET_SHINY             ,   8.0F,    256,  6, 1|2  |8      |64|128      ,  22, 133, 108,   0,   "MysteriousCrystal"       ,   "Mysterious Crystal"            ,    0,       0,      7200,  7200,  true, false,   4,   1,   1, Dyes.dyeCyan        ).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();

        Materials.RedstoneAlloy           = new Materials( 381, TextureSet.SET_DARKSTEEL         ,   3.0F,    128,  2, 1|2          |64|128      , 255,  67,  50,   0,   "RedstoneAlloy"           ,   "Redstone Alloy"                ,    0,       0,        671, 1000,  true, false,   1,   1,   1, Dyes.dyeRed         , 1, Arrays.asList(new MaterialStack(Redstone, 1), new MaterialStack(Silicon, 1), new MaterialStack(Coal, 1))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.Soularium               = new Materials( 379, TextureSet.SET_DARKSTEEL         ,   8.0F,    256,  2, 1|2          |64|128      , 145,  109, 62,   0,   "Soularium"               ,   "Soularium"                     ,    0,       0,        800, 1000,  true, false,   3,   1,   1, Dyes.dyeBrown       , 1, Arrays.asList(new MaterialStack(SoulSand, 1), new MaterialStack(Gold, 1), new MaterialStack(Ash, 1))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.ConductiveIron          = new Materials( 369, TextureSet.SET_DARKSTEEL         ,   6.0F,    256,  3, 1|2          |64|128      , 255, 191, 195,   0,   "ConductiveIron"          ,   "Conductive Iron"               ,    0,       0,         -1, 1200,  true, false,   4,   1,   1, Dyes.dyeRed         , 1, Arrays.asList(new MaterialStack(RedstoneAlloy, 1), new MaterialStack(Iron, 1), new MaterialStack(Silver, 1))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.ElectricalSteel         = new Materials( 365, TextureSet.SET_DARKSTEEL         ,   6.0F,    512,  2, 1|2          |64|128      , 216, 216, 216,   0,   "ElectricalSteel"         ,   "Electrical Steel"              ,    0,       0,       1811, 1000,  true, false,   4,   1,   1, Dyes.dyeGray        , 1, Arrays.asList(new MaterialStack(Steel, 1), new MaterialStack(Coal, 1), new MaterialStack(Silicon, 1))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.EnergeticAlloy          = new Materials( 366, TextureSet.SET_ENERGETIC         ,  12.0F,   1024,  3, 1|2          |64|128      , 255, 140,  25,   0,   "EnergeticAlloy"          ,   "Energetic Alloy"               ,    0,       0,         -1, 2200,  true, false,   3,   1,   1, Dyes.dyeOrange      , 1, Arrays.asList(new MaterialStack(ConductiveIron, 1), new MaterialStack(Gold, 1), new MaterialStack(BlackSteel, 1))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.VibrantAlloy            = new Materials( 367, TextureSet.SET_VIBRANT           ,  18.0F,   4048,  4, 1|2          |64|128      , 149, 224,  17,   0,   "VibrantAlloy"            ,   "Vibrant Alloy"                 ,    0,       0,       3300, 3300,  true, false,   4,   1,   1, Dyes.dyeLime        , 1, Arrays.asList(new MaterialStack(EnergeticAlloy, 1), new MaterialStack(EnderEye, 1), new MaterialStack(Chrome, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.PulsatingIron           = new Materials( 378, TextureSet.SET_DARKSTEEL         ,   6.0F,    256,  3, 1|2          |64|128      , 128, 246, 155,   0,   "PulsatingIron"           ,   "Pulsating Iron"                ,    0,       0,         -1, 1800,  true, false,   4,   1,   1, Dyes.dyeLime        , 1, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(EnderPearl, 1), new MaterialStack(RedstoneAlloy, 1))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.DarkSteel               = new Materials( 364, TextureSet.SET_DARKSTEEL         ,   8.0F,    512,  3, 1|2          |64|128      ,  80,  70,  80,   0,   "DarkSteel"               ,   "Dark Steel"                    ,    0,       0,         -1, 1800,  true, false,   3,   1,   1, Dyes.dyePurple      , 1, Arrays.asList(new MaterialStack(ElectricalSteel, 1), new MaterialStack(Coal, 1), new MaterialStack(Obsidian, 1))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.EndSteel                = new Materials( 401, TextureSet.SET_VIVID             ,  12.0F,   2000,  4, 1|2          |64|128      , 219, 206, 125,   0,   "EndSteel"                ,   "End Steel"                     ,    0,       0,        940, 3600,  true, false,   3,   1,   1, Dyes.dyeYellow      , 1, Arrays.asList(new MaterialStack(DarkSteel, 1), new MaterialStack(Tungsten, 1), new MaterialStack(Endstone, 1))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.CrudeSteel              = new Materials( 402, TextureSet.SET_VIVID             ,   2.0F,     64,  2, 1|2          |64|128      , 158, 144, 135,   0,   "CrudeSteel"              ,   "Clay Compound"                 ,    0,       0,         -1, 1000, false, false,   4,   1,   1, Dyes.dyeGray        , 1, Arrays.asList(new MaterialStack(Stone, 1), new MaterialStack(Clay, 1), new MaterialStack(Flint, 1))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.CrystallineAlloy        = new Materials( 403, TextureSet.SET_CRYSTALLINE       ,  18.0F,    768,  4, 1|2          |64|128      ,  74, 219, 219,   0,   "CrystallineAlloy"        ,   "Crystalline Alloy"             ,    0,       0,       4500, 4500,  true, false,   4,   1,   1, Dyes.dyeCyan        , 1, Arrays.asList(new MaterialStack(Gold, 1), new MaterialStack(Diamond, 1), new MaterialStack(PulsatingIron, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.MelodicAlloy            = new Materials( 404, TextureSet.SET_MELODIC           ,  24.0F,   1024,  5, 1|2          |64|128      , 193,  85, 193,   0,   "MelodicAlloy"            ,   "Melodic Alloy"                 ,    0,       0,       5400, 5400,  true, false,   4,   1,   1, Dyes.dyeMagenta     , 1, Arrays.asList(new MaterialStack(EndSteel, 1), new MaterialStack(EnderEye, 1), new MaterialStack(Oriharukon, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.StellarAlloy            = new Materials( 405, TextureSet.SET_STELLAR           ,  96.0F,  10240,  7, 1|2          |64|128      , 211, 255, 255,   0,   "StellarAlloy"            ,   "Stellar Alloy"                 ,    0,       0,       7200, 7200,  true, false,   4,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(NetherStar, 1), new MaterialStack(MelodicAlloy, 1), new MaterialStack(Naquadah, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.CrystallinePinkSlime    = new Materials( 406, TextureSet.SET_CRYSTALLINE       ,   6.0F,    128,  3, 1|2          |64|128      , 226,  54, 203,   0,   "CrystallinePinkSlime"    ,   "Crystalline Pink Slime"        ,    0,       0,       5000, 5000,  true, false,   4,   1,   1, Dyes.dyePink        , 1, Arrays.asList(new MaterialStack(CrystallineAlloy, 1), new MaterialStack(Diamond, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.EnergeticSilver         = new Materials( 407, TextureSet.SET_VIVID             ,   8.0F,    512,  3, 1|2          |64|128      ,  56, 135, 181,   0,   "EnergeticSilver"         ,   "Energetic Silver"              ,    0,       0,         -1, 2200,  true, false,   4,   1,   1, Dyes.dyeLightBlue   , 1, Arrays.asList(new MaterialStack(Silver, 1), new MaterialStack(ConductiveIron, 1), new MaterialStack(BlackSteel, 1))).disableAutoGeneratedBlastFurnaceRecipes();
        Materials.VividAlloy              = new Materials( 408, TextureSet.SET_VIVID             ,  12.0F,    768,  4, 1|2          |64|128      ,  70, 188, 219,   0,   "VividAlloy"              ,   "Vivid Alloy"                   ,    0,       0,       3300, 3300,  true, false,   4,   1,   1, Dyes.dyeBlue        , 1, Arrays.asList(new MaterialStack(EnergeticSilver, 1), new MaterialStack(EnderEye, 1), new MaterialStack(Chrome, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.Enderium                = new Materials( 321, TextureSet.SET_DULL              ,   8.0F,   1500,  3, 1|2          |64|128      ,  89, 145, 135,   0,   "Enderium"                ,   "Enderium"                      ,    0,       0,       4500, 4500,  true, false,   1,   1,   1, Dyes.dyeGreen       , 1, Arrays.asList(new MaterialStack(EnderiumBase, 2), new MaterialStack(Thaumium, 1), new MaterialStack(EnderPearl, 1)), Arrays.asList(new TCAspects.TC_AspectStack(TCAspects.METALLUM, 2), new TCAspects.TC_AspectStack(TCAspects.ALIENIS, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.Mithril                 = new Materials( 331, TextureSet.SET_SHINY             ,  32.0F,     64,  2, 1|2  |8      |64          , 255, 255, 210,   0,   "Mithril"                 ,   "Mithril"                       ,    0,       0,       6600, 6600,  true, false,   4,   1,   1, Dyes.dyeLightBlue   , 2, Arrays.asList(new MaterialStack(Platinum, 2), new MaterialStack(Thaumium, 1))).disableAutoGeneratedBlastFurnaceRecipes().setTurbineMultipliers(22, 1, 1);
        Materials.BlueAlloy               = new Materials( 309, TextureSet.SET_DULL              ,   1.0F,      0,  0, 1|2                       , 100, 180, 255,   0,   "BlueAlloy"               ,   "Blue Alloy"                    ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeLightBlue   , 2, Arrays.asList(new MaterialStack(Silver, 1), new MaterialStack(Electrotine, 4)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 3)));
        Materials.ShadowIron              = new Materials( 336, TextureSet.SET_METALLIC          ,  32.0F,  10240,  2, 1|2  |8      |64          , 120, 120, 120,   0,   "ShadowIron"              ,   "Shadow Iron"                   ,    0,       0,       8400, 8400,  true, false,   3,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Thaumium, 3))).disableAutoGeneratedBlastFurnaceRecipes().setTurbineMultipliers(1, 76, 1);
        Materials.ShadowSteel             = new Materials( 337, TextureSet.SET_METALLIC          ,   6.0F,    768,  4, 1|2          |64          ,  90,  90,  90,   0,   "ShadowSteel"             ,   "Shadow Steel"                  ,    0,       0,         -1, 1700,  true, false,   4,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Steel, 1), new MaterialStack(Thaumium, 3)));
        Materials.AstralSilver            = new Materials( 333, TextureSet.SET_SHINY             ,  10.0F,     64,  2, 1|2          |64          , 230, 230, 255,   0,   "AstralSilver"            ,   "Astral Silver"                 ,    0,       0,         -1,    0, false, false,   4,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Silver, 2), new MaterialStack(Thaumium, 1)));

        Materials.InfinityCatalyst        = new Materials( 394, TextureSet.SET_SHINY             ,  64.0F,1310720, 10, 1|2  |8      |64|128      , 255, 255, 255,   0,   "InfinityCatalyst"        ,   "Infinity Catalyst"             ,    5,  500000,      10800,10800,  true, false,  20,   1,   1, Dyes.dyeLightGray    ).setProcessingMaterialTierEU(TierEU.RECIPE_UV).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.Infinity                = new Materials( 397, new TextureSet("infinity", true) , 256.0F,2621440, 17, 1|2       |32|64|128      , 255, 255, 255,   0,   "Infinity"                ,   "Infinity"                      ,    5, 5000000,      10800,10800,  true, false,  40,   1,   1, Dyes.dyeLightGray    ).setProcessingMaterialTierEU(TierEU.RECIPE_UV).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe().setHasCorrespondingPlasma(true);
        Materials.Bedrockium              = loadBedrockium();
        Materials.Trinium                 = new Materials( 868, TextureSet.SET_SHINY             , 128.0F,  51200,  8, 1|2  |8      |64|128      , 200, 200, 210,   0,   "Trinium"                 ,   "Trinium"                       ,    0,       0,       7200, 7200,  true, false,   4,   1,   1, Dyes.dyeLightGray    ).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.Ichorium                = new Materials( 978, new TextureSet("ichorium", true) ,  32.0F, 850000, 12, 1|2  |8   |32|64|128      , 211, 120,   6,   0,   "Ichorium"                ,   "Ichorium"                      ,    5,  250000,       9000, 9000,  true, false,   4,   1,   1, Dyes.dyeOrange       ).setTurbineMultipliers(6, 6, 3).setHasCorrespondingPlasma(true);
        Materials.CosmicNeutronium        = new Materials( 982, new TextureSet("cosmicneutronium", true),96.0F,163840,12,1|2|8   |32|64|128      ,  50,  50,  55,   0,   "CosmicNeutronium"        ,   "Cosmic Neutronium"             ,    0,       0,       9900, 9900,  true, false,   4,   1,   1, Dyes.dyeBlack        ).setProcessingMaterialTierEU(TierEU.RECIPE_ZPM).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe().setHasCorrespondingPlasma(true);

        Materials.Pentacadmiummagnesiumhexaoxid                                   = new Materials( 987, TextureSet.SET_SHINY          ,   1.0F,      0,  3, 1|2                ,  85,  85,  85,   0,   "Pentacadmiummagnesiumhexaoxid"                                 ,   "Superconductor Base MV"       ,     0,       0,     2500,  2500,  true,  false,  1,   1,   1, Dyes.dyeGray       , 1, Arrays.asList(new MaterialStack(Cadmium, 5), new MaterialStack(Magnesium, 1), new MaterialStack(Oxygen, 6)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 3))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.Titaniumonabariumdecacoppereikosaoxid                           = new Materials( 988, TextureSet.SET_METALLIC       ,   1.0F,      0,  3, 1|2                ,  51,  25,   0,   0,   "Titaniumonabariumdecacoppereikosaoxid"                         ,   "Superconductor Base HV"       ,     0,       0,     3300,  3300,  true,  false,  1,   1,   1, Dyes.dyeBrown      , 1, Arrays.asList(new MaterialStack(Titanium, 1), new MaterialStack(Barium, 9),  new MaterialStack(Copper, 10), new MaterialStack(Oxygen, 20)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 6))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.Uraniumtriplatinid                                              = new Materials( 989, TextureSet.SET_SHINY          ,   1.0F,      0,  3, 1|2                ,   0, 135,   0,   0,   "Uraniumtriplatinid"                                            ,   "Superconductor Base EV"       ,     0,       0,     4400,  4400,  true,  false,  1,   1,   1, Dyes.dyeLime       , 1, Arrays.asList(new MaterialStack(Uranium, 1), new MaterialStack(Platinum, 3)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 9))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.Vanadiumtriindinid                                              = new Materials( 990, TextureSet.SET_SHINY          ,   1.0F,      0,  3, 1|2                ,  51,   0,  51,   0,   "Vanadiumtriindinid"                                            ,   "Superconductor Base IV"       ,     0,       0,     5200,  5200,  true,  false,  1,   1,   1, Dyes.dyeMagenta    , 1, Arrays.asList(new MaterialStack(Vanadium , 1), new MaterialStack(Indium, 3)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 12))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid     = new Materials( 991, TextureSet.SET_METALLIC       ,   1.0F,      0,  3, 1|2                , 153,  76,   0,   0,   "Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid"   ,   "Superconductor Base LuV"      ,     0,       0,     6000,  6000,  true,  false,  1,   1,   1, Dyes.dyeBrown      , 1, Arrays.asList(new MaterialStack(Indium, 4), new MaterialStack(Tin, 2), new MaterialStack(Barium, 2), new MaterialStack(Titanium, 1), new MaterialStack(Copper, 7), new MaterialStack(Oxygen, 14)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 15))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.Tetranaquadahdiindiumhexaplatiumosminid                         = new Materials( 992, TextureSet.SET_METALLIC       ,   1.0F,      0,  3, 1|2                ,  10,  10,  10,   0,   "Tetranaquadahdiindiumhexaplatiumosminid"                       ,   "Superconductor Base ZPM"      ,     0,       0,     8100,  8100,  true,  false,  1,   1,   1, Dyes.dyeBlack      , 1, Arrays.asList(new MaterialStack(Naquadah, 4), new MaterialStack(Indium, 2), new MaterialStack(Palladium, 6), new MaterialStack(Osmium, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 18))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.Longasssuperconductornameforuvwire                              = new Materials( 986, TextureSet.SET_METALLIC       ,   1.0F,      0,  3, 1|2                , 224, 210,   7,   0,   "Longasssuperconductornameforuvwire"                            ,   "Superconductor Base UV"       ,     0,       0,     9900,  9900,  true,  false,  1,   1,   1, Dyes.dyeYellow     , 1, Arrays.asList(new MaterialStack(Naquadria, 4), new MaterialStack(Osmiridium, 3), new MaterialStack(Europium, 1), new MaterialStack(Samarium, 1)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 21))).setProcessingMaterialTierEU(TierEU.RECIPE_LuV).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.Longasssuperconductornameforuhvwire                             = new Materials( 985, TextureSet.SET_SHINY          ,   1.0F,      0,  3, 1|2                ,  38, 129, 189,   0,   "Longasssuperconductornameforuhvwire"                           ,   "Superconductor Base UHV"      ,     0,       0,    10800, 10800,  true,  false,  1,   1,   1, Dyes.dyeWhite      , 1, Arrays.asList(new MaterialStack(Draconium, 6), new MaterialStack(CosmicNeutronium, 7), new MaterialStack(Tritanium, 5), new MaterialStack(Americium, 6)), Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 24))).setProcessingMaterialTierEU(TierEU.RECIPE_ZPM).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.SuperconductorUEVBase                                           = new Materials( 974, TextureSet.SET_SHINY          ,   1.0F,      0,  3, 1|2                , 174,   8,   8,   0,   "SuperconductorUEVBase"                                         ,   "Superconductor Base UEV"      ,     0,       0,    11700, 11800,  true,  false,  1,   1,   1, Dyes.dyeWhite, Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 27))).setProcessingMaterialTierEU(TierEU.RECIPE_UV).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.SuperconductorUIVBase                                           = new Materials( 131, TextureSet.SET_SHINY          ,   1.0F,      0,  3, 1|2                , 229,  88, 177,   0,   "SuperconductorUIVBase"                                         ,   "Superconductor Base UIV"      ,     0,       0,    12700, 12700,  true,  false,  1,   1,   1, Dyes.dyeWhite, Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 34))).setProcessingMaterialTierEU(TierEU.RECIPE_UHV).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
        Materials.SuperconductorUMVBase                                           = new Materials( 134, TextureSet.SET_SHINY          ,   1.0F,      0,  3, 1|2                , 181,  38, 205,   0,   "SuperconductorUMVBase"                                         ,   "Superconductor Base UMV"      ,     0,       0,    13600, 13600,  true,  false,  1,   1,   1, Dyes.dyeWhite, Collections.singletonList(new TCAspects.TC_AspectStack(TCAspects.ELECTRUM, 40))).setProcessingMaterialTierEU(TierEU.RECIPE_UEV).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();

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
        Materials.Grade1PurifiedWater = loadGrade1PurifiedWater();
        Materials.Grade2PurifiedWater = loadGrade2PurifiedWater();
        Materials.Grade3PurifiedWater = loadGrade3PurifiedWater();
        Materials.Grade4PurifiedWater = loadGrade4PurifiedWater();
        Materials.Grade5PurifiedWater = loadGrade5PurifiedWater();
        Materials.Grade6PurifiedWater = loadGrade6PurifiedWater();
        Materials.Grade7PurifiedWater = loadGrade7PurifiedWater();
        Materials.Grade8PurifiedWater = loadGrade8PurifiedWater();
        Materials.FlocculationWasteLiquid = loadFlocculationWasteLiquid();
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

    private static void loadRandom() {
        Materials.Organic = loadOrganic();
        Materials.AnyCopper = loadAnyCopper();
        Materials.AnyBronze = loadAnyBronze();
        Materials.AnyIron = loadAnyIron();
        Materials.AnyRubber = loadAnyRubber();
        Materials.AnySyntheticRubber = loadAnySyntheticRubber();
        Materials.Crystal = loadCrystal();
        Materials.Quartz = loadQuartz();
        Materials.Metal = loadMetal();
        Materials.Unknown = loadUnknown();
        Materials.Cobblestone = loadCobblestone();
        Materials.BrickNether = loadBrickNether();
    }

    private static Materials loadOrganic() {
        return new MaterialBuilder().setName("Organic")
            .setDefaultLocalName("Organic")
            .setMetaItemSubID(-1)
            .setIconSet(TextureSet.SET_LEAF)
            .constructMaterial();
    }

    private static Materials loadAnyCopper() {
        return new MaterialBuilder().setName("AnyCopper")
            .setDefaultLocalName("AnyCopper")
            .setMetaItemSubID(-1)
            .setIconSet(TextureSet.SET_SHINY)
            .constructMaterial();
    }

    private static Materials loadAnyBronze() {
        return new MaterialBuilder().setName("AnyBronze")
            .setDefaultLocalName("AnyBronze")
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

    private static Materials loadCrystal() {
        return new MaterialBuilder().setName("Crystal")
            .setDefaultLocalName("Crystal")
            .setIconSet(TextureSet.SET_SHINY)
            .constructMaterial();
    }

    private static Materials loadQuartz() {
        return new MaterialBuilder().setName("Quartz")
            .setDefaultLocalName("Quartz")
            .setIconSet(TextureSet.SET_QUARTZ)
            .constructMaterial();
    }

    private static Materials loadMetal() {
        return new MaterialBuilder().setName("Metal")
            .setDefaultLocalName("Metal")
            .setIconSet(TextureSet.SET_METALLIC)
            .constructMaterial();
    }

    private static Materials loadUnknown() {
        return new MaterialBuilder().setName("Unknown")
            .setDefaultLocalName("Unknown")
            .setIconSet(TextureSet.SET_DULL)
            .constructMaterial();
    }

    private static Materials loadCobblestone() {
        return new MaterialBuilder().setName("Cobblestone")
            .setDefaultLocalName("Cobblestone")
            .setIconSet(TextureSet.SET_DULL)
            .constructMaterial();
    }

    private static Materials loadBrickNether() {
        return new MaterialBuilder().setName("BrickNether")
            .setDefaultLocalName("BrickNether")
            .setIconSet(TextureSet.SET_DULL)
            .constructMaterial();
    }

    private static void loadDontCare() {
        Materials.Serpentine = loadSerpentine();
        Materials.Flux = loadFlux();
        Materials.OsmiumTetroxide = loadOsmiumTetroxide();
        Materials.RubberTreeSap = loadRubberTreeSap();
        Materials.PhasedIron = loadPhasedIron();
        Materials.PhasedGold = loadPhasedGold();
        Materials.HeeEndium = loadHeeEndium();
        Materials.Teslatite = loadTeslatite();
        Materials.Fluix = loadFluix();
        Materials.DarkThaumium = loadDarkThaumium();
        Materials.Alfium = loadAlfium();
        Materials.Mutation = loadMutation();
        Materials.Aquamarine = loadAquamarine();
        Materials.Ender = loadEnder();
        Materials.SodiumPeroxide = loadSodiumPeroxide();
        Materials.IridiumSodiumOxide = loadIridiumSodiumOxide();
        Materials.PlatinumGroupSludge = loadPlatinumGroupSludge();
        Materials.Draconium = loadDraconium();
        Materials.DraconiumAwakened = loadDraconiumAwakened();
        Materials.PurpleAlloy = loadPurpleAlloy();
        Materials.InfusedTeslatite = loadInfusedTeslatite();
    }

    private static Materials loadSerpentine() {
        return new MaterialBuilder().setName("Serpentine")
            .setDefaultLocalName("Serpentine")
            .addDustItems()
            .addMetalItems()
            .addOreItems()
            .constructMaterial();
    }

    private static Materials loadFlux() {
        return new MaterialBuilder().setName("Flux")
            .setDefaultLocalName("Flux")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadOsmiumTetroxide() {
        return new MaterialBuilder().setName("OsmiumTetroxide")
            .setDefaultLocalName("Osmium Tetroxide")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadRubberTreeSap() {
        return new MaterialBuilder().setName("RubberTreeSap")
            .setDefaultLocalName("Rubber Tree Sap")
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

    private static Materials loadPhasedGold() {
        return new MaterialBuilder().setName("PhasedGold")
            .setDefaultLocalName("Phased Gold")
            .addDustItems()
            .addMetalItems()
            .setBlastFurnaceTemp(1800)
            .setBlastFurnaceRequired(true)
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

    private static Materials loadTeslatite() {
        return new MaterialBuilder().setName("Teslatite")
            .setDefaultLocalName("Teslatite")
            .setRGB(0x3cb4c8)
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

    private static Materials loadDarkThaumium() {
        return new MaterialBuilder().setName("DarkThaumium")
            .setDefaultLocalName("Dark Thaumium")
            .addDustItems()
            .addMetalItems()
            .constructMaterial();
    }

    private static Materials loadAlfium() {
        return new MaterialBuilder().setName("Alfium")
            .setDefaultLocalName("Alfium")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadMutation() {
        return new MaterialBuilder().setName("Mutation")
            .setDefaultLocalName("Mutation")
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

    private static Materials loadEnder() {
        return new MaterialBuilder().setName("Ender")
            .setDefaultLocalName("Ender")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadSodiumPeroxide() {
        return new MaterialBuilder().setName("SodiumPeroxide")
            .setDefaultLocalName("Sodium Peroxide")
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadIridiumSodiumOxide() {
        return new MaterialBuilder().setName("IridiumSodiumOxide")
            .setDefaultLocalName("Iridium Sodium Oxide")
            .addDustItems()
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

    private static Materials loadPurpleAlloy() {
        return new MaterialBuilder().setName("PurpleAlloy")
            .setDefaultLocalName("Purple Alloy")
            .setRGB(0x64b4ff)
            .constructMaterial();
    }

    private static Materials loadInfusedTeslatite() {
        return new MaterialBuilder().setName("InfusedTeslatite")
            .setDefaultLocalName("Infused Teslatite")
            .setRGB(0x64b4ff)
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
        Materials.Blueschist = loadBlueschist();
        Materials.Bluestone = loadBluestone();
        Materials.Bloodstone = loadBloodstone();
        Materials.Blutonium = loadBlutonium();
        Materials.Carmot = loadCarmot();
        Materials.Celenegil = loadCelenegil();
        Materials.CertusQuartz = loadCertusQuartz();
        Materials.CertusQuartzCharged = loadCertusQuartzCharged();
        Materials.Ceruclase = loadCeruclase();
        Materials.Citrine = loadCitrine();
        Materials.CobaltHexahydrate = loadCobaltHexahydrate();
        Materials.ConstructionFoam = loadConstructionFoam();
        Materials.Chert = loadChert();
        Materials.Chimerite = loadChimerite();
        Materials.Coral = loadCoral();
        Materials.CrudeOil = loadCrudeOil();
        Materials.Chrysocolla = loadChrysocolla();
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
        Materials.Graphite = loadGraphite();
        Materials.Graphene = loadGraphene();
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
        Materials.InfusedGold = loadInfusedGold();
        Materials.InfusedAir = loadInfusedAir();
        Materials.InfusedFire = loadInfusedFire();
        Materials.InfusedEarth = loadInfusedEarth();
        Materials.InfusedWater = loadInfusedWater();
        Materials.InfusedEntropy = loadInfusedEntropy();
        Materials.InfusedOrder = loadInfusedOrder();
        Materials.InfusedVis = loadInfusedVis();
        Materials.InfusedDull = loadInfusedDull();
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

    private static Materials loadBloodstone() {
        return new MaterialBuilder().setName("Bloodstone")
            .setDefaultLocalName("Bloodstone")
            .setColor(Dyes.dyeRed)
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

    private static Materials loadChrysocolla() {
        return new MaterialBuilder().setName("Chrysocolla")
            .setDefaultLocalName("Chrysocolla")
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
            .setRGB(0x1c0639)
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

    private static void loadNotExact() {
        Materials.Antimatter = loadAntimatter();
        Materials.AdvancedGlue = loadAdvancedGlue();
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
        Materials.FishOil = loadFishOil();
        Materials.FermentedBiomass = loadFermentedBiomass();
        Materials.Fuel = loadFuel();
        Materials.Glue = loadGlue();
        Materials.Gunpowder = loadGunpowder();
        Materials.FryingOilHot = loadFryingOilHot();
        Materials.Honey = loadHoney();
        Materials.Leather = loadLeather();
        Materials.Lubricant = loadLubricant();
        Materials.McGuffium239 = loadMcGuffium239();
        Materials.MeatRaw = loadMeatRaw();
        Materials.MeatCooked = loadMeatCooked();
        Materials.Milk = loadMilk();
        Materials.Mud = loadMud();
        Materials.Oil = loadOil();
        Materials.Paper = loadPaper();
        Materials.Peat = loadPeat();
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
        Materials.Wheat = loadWheat();
        Materials.WoodGas = loadWoodGas();
        Materials.WoodTar = loadWoodTar();
        Materials.WoodVinegar = loadWoodVinegar();
        Materials.WeedEX9000 = loadWeedEX9000();
    }

    private static Materials loadAntimatter() {
        return new MaterialBuilder().setName("Antimatter")
            .setDefaultLocalName("Antimatter")
            .setColor(Dyes.dyePink)
            .addAspect(TCAspects.POTENTIA, 9)
            .addAspect(TCAspects.PERFODIO, 8)
            .constructMaterial();
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

    private static Materials loadFermentedBiomass() {
        return new MaterialBuilder(691, TextureSet.SET_FLUID, "Fermented Biomass").addCell()
            .addFluid()
            .setRGB(68, 85, 0)
            .setColor(Dyes.dyeBrown)
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

    private static Materials loadWeedEX9000() {
        return new MaterialBuilder(242, TextureSet.SET_FLUID, "Weed-EX 9000").addFluid()
            .setRGB(64, 224, 86)
            .setColor(Dyes.dyeGreen)
            .constructMaterial();
    }

    private static void loadTODOThis() {
        Materials.AluminiumBrass = loadAluminiumBrass();
        Materials.Osmiridium = loadOsmiridium();
        Materials.Sunnarium = loadSunnarium();
        Materials.Endstone = loadEndstone();
        Materials.Netherrack = loadNetherrack();
        Materials.SoulSand = loadSoulSand();
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

    private static Materials loadSoulSand() {
        return new MaterialBuilder().setName("SoulSand")
            .setDefaultLocalName("Soulsand")
            .setIconSet(TextureSet.SET_DULL)
            .setColor(Dyes.dyeBrown)
            .addDustItems()
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

    private static Materials loadChromiumTrioxide() {
        return new MaterialBuilder(591, TextureSet.SET_DULL, "Chromium Trioxide").setName("Chromiumtrioxide")
            .addDustItems()
            .setRGB(255, 228, 225)
            .setColor(Dyes.dyePink)
            .setMaterialList(new MaterialStack(Chrome, 1), new MaterialStack(Oxygen, 3))
            .addElectrolyzerRecipe()
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

    private static Materials loadPhthalicAcid() {
        return new MaterialBuilder(595, TextureSet.SET_FLUID, "Phthalic Acid").setName("phtalicacid")
            .addCell()
            .addFluid()
            .setRGB(54, 133, 71)
            .setColor(Dyes.dyeOrange)
            .setMaterialList(new MaterialStack(Carbon, 8), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 4))
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

    private static Materials loadMtbeMixture() {
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

    private static Materials loadMtbeMixtureAlt() {
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

    private static Materials loadEthylTertButylEther() {
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

    private static Materials loadRawGasoline() {
        return new MaterialBuilder(996, TextureSet.SET_FLUID, "Raw Gasoline").addCell()
            .addFluid()
            .setRGB(255, 100, 0)
            .setColor(Dyes.dyeOrange)
            .constructMaterial();
    }

    private static Materials loadGasoline() {
        return new MaterialBuilder(997, TextureSet.SET_FLUID, "Gasoline").addCell()
            .addFluid()
            .setRGB(255, 165, 0)
            .setColor(Dyes.dyeOrange)
            .setFuelType(MaterialBuilder.DIESEL)
            .setFuelPower(576)
            .constructMaterial();
    }

    private static Materials loadHighOctaneGasoline() {
        return new MaterialBuilder(998, TextureSet.SET_FLUID, "High Octane Gasoline").addCell()
            .addFluid()
            .setRGB(255, 165, 0)
            .setColor(Dyes.dyeOrange)
            .setFuelType(MaterialBuilder.DIESEL)
            .setFuelPower(2500)
            .constructMaterial();
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
