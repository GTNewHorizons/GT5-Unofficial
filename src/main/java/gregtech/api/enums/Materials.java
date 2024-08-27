package gregtech.api.enums;

import static gregtech.api.enums.FluidState.GAS;
import static gregtech.api.enums.GT_Values.M;
import static gregtech.api.enums.Mods.Thaumcraft;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.IllegalFormatException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.fluid.GT_FluidFactory;
import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.IMaterialHandler;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gregtech.common.config.gregtech.ConfigHarvestLevel;
import gregtech.common.render.items.CosmicNeutroniumRenderer;
import gregtech.common.render.items.GT_GeneratedMaterial_Renderer;
import gregtech.common.render.items.GaiaSpiritRenderer;
import gregtech.common.render.items.InfinityRenderer;
import gregtech.common.render.items.TranscendentMetalRenderer;
import gregtech.common.render.items.UniversiumRenderer;
import gregtech.loaders.materialprocessing.ProcessingConfig;
import gregtech.loaders.materialprocessing.ProcessingModSupport;
import gregtech.loaders.materials.MaterialsInit1;

@SuppressWarnings("unused") // API Legitimately has unused Members and Methods
public class Materials implements IColorModulationContainer, ISubTagContainer {

    public static final List<IMaterialHandler> mMaterialHandlers = new ArrayList<>();
    private static final Map<String, Materials> MATERIALS_MAP = new LinkedHashMap<>();

    public static final Map<Fluid, Materials> FLUID_MAP = new LinkedHashMap<>();

    /**
     * This is for keeping compatibility with addons mods (Such as TinkersGregworks etc.) that looped over the old
     * materials enum
     */
    @Deprecated
    public static Collection<Materials> VALUES = new LinkedHashSet<>();
    /**
     * This is the Default Material returned in case no Material has been found or a NullPointer has been inserted at a
     * location where it shouldn't happen.
     */

    // Spotless breaks the table below into many, many lines
    // spotless:off
    public static Materials _NULL = new Materials(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "NULL", "NULL", 0, 0, 0, 0, false, false, 1, 1, 1, Dyes._NULL, Element._NULL, Collections.singletonList(new TC_AspectStack(TC_Aspects.VACUOS, 1)));
    /**
     * Direct Elements
     */
    public static Materials Aluminium;
    public static Materials Americium;
    public static Materials Antimony;
    public static Materials Argon;
    public static Materials Arsenic;
    public static Materials Barium;
    public static Materials Beryllium;
    public static Materials Bismuth;
    public static Materials Boron;
    public static Materials Caesium;
    public static Materials Calcium;
    public static Materials Carbon;
    public static Materials Cadmium;
    public static Materials Cerium;
    public static Materials Chlorine;
    public static Materials Chrome;
    public static Materials Cobalt;
    public static Materials Copper;
    public static Materials Deuterium;
    public static Materials Dysprosium;
    public static Materials Empty;
    public static Materials Erbium;
    public static Materials Europium;
    public static Materials Fluorine;
    public static Materials Gadolinium;
    public static Materials Gallium;
    public static Materials Gold;
    public static Materials Holmium;
    public static Materials Hydrogen;
    public static Materials Helium;
    public static Materials Helium_3;
    public static Materials Indium;
    public static Materials Iridium;
    public static Materials Iron;
    public static Materials Lanthanum;
    public static Materials Lead;
    public static Materials Lithium;
    public static Materials Lutetium;
    public static Materials Magic;
    public static Materials Magnesium;
    public static Materials Manganese;
    public static Materials Mercury;
    public static Materials Molybdenum;
    public static Materials Neodymium;
    public static Materials Neutronium;
    public static Materials Nickel;
    public static Materials Niobium;
    public static Materials Nitrogen;
    public static Materials Osmium;
    public static Materials Oxygen;
    public static Materials Palladium;
    public static Materials Phosphorus;
    public static Materials Platinum;
    public static Materials Plutonium;
    public static Materials Plutonium241;
    public static Materials Potassium;
    public static Materials Praseodymium;
    public static Materials Promethium;
    public static Materials Radon;
    public static Materials Rubidium;
    public static Materials Samarium;
    public static Materials Scandium;
    public static Materials Silicon;
    public static Materials Silver;
    public static Materials Sodium;
    public static Materials Strontium;
    public static Materials Sulfur;
    public static Materials Tantalum;
    public static Materials Tellurium;
    public static Materials Terbium;
    public static Materials Thorium;
    public static Materials Thulium;
    public static Materials Tin;
    public static Materials Titanium;
    public static Materials Tritanium;
    public static Materials Tritium;
    public static Materials Tungsten;
    public static Materials Uranium;
    public static Materials Uranium235;
    public static Materials Vanadium;
    public static Materials Ytterbium;
    public static Materials Yttrium;
    public static Materials Zinc;
    public static Materials Grade1PurifiedWater;
    public static Materials Grade2PurifiedWater;
    public static Materials Grade3PurifiedWater;
    public static Materials Grade4PurifiedWater;
    public static Materials Grade5PurifiedWater;
    public static Materials Grade6PurifiedWater;
    public static Materials Grade7PurifiedWater;
    public static Materials Grade8PurifiedWater;
    public static Materials FlocculationWasteLiquid;

    //GT++ materials

    public static Materials Flerovium;

    /**
     * The "Random Material" ones.
     */
    public static Materials Organic;
    public static Materials AnyCopper;
    public static Materials AnyBronze;
    public static Materials AnyIron;
    public static Materials AnyRubber;
    public static Materials AnySyntheticRubber;
    public static Materials Crystal;
    public static Materials Quartz;
    public static Materials Metal;
    public static Materials Unknown;
    public static Materials Cobblestone;
    public static Materials BrickNether;

    /**
     * The "I don't care" Section, everything I don't want to do anything with right now, is right here. Just to make the Material Finder shut up about them.
     * But I do see potential uses in some of these Materials.
     */
    public static Materials Serpentine;
    public static Materials Flux;
    public static Materials OsmiumTetroxide;
    public static Materials RubberTreeSap;
    public static Materials PhasedIron;
    public static Materials PhasedGold;
    public static Materials HeeEndium;
    public static Materials Teslatite;
    public static Materials Fluix;
    public static Materials DarkThaumium;
    public static Materials Alfium;
    public static Materials Mutation;
    public static Materials Aquamarine;
    public static Materials Ender;
    public static Materials SodiumPeroxide;
    public static Materials IridiumSodiumOxide;
    public static Materials PlatinumGroupSludge;
    public static Materials Draconium;
    public static Materials DraconiumAwakened;
    public static Materials PurpleAlloy;
    public static Materials InfusedTeslatite;

    /**
     * Unknown Material Components. Dead End Section.
     */
    public static Materials Adamantium;
    public static Materials Adamite;
    public static Materials Adluorite;
    public static Materials Agate;
    public static Materials Alduorite;
    public static Materials Amber;
    public static Materials Ammonium;
    public static Materials Amordrine;
    public static Materials Andesite;
    public static Materials Angmallen;
    public static Materials Ardite;
    public static Materials Aredrite;
    public static Materials Atlarus;
    public static Materials Bitumen;
    public static Materials Black;
    public static Materials Blizz;
    public static Materials Blueschist;
    public static Materials Bluestone;
    public static Materials Bloodstone;
    public static Materials Blutonium;
    public static Materials Carmot;
    public static Materials Celenegil;
    public static Materials CertusQuartz;
    public static Materials CertusQuartzCharged;
    public static Materials Ceruclase;
    public static Materials Citrine;
    public static Materials CobaltHexahydrate;
    public static Materials ConstructionFoam;
    public static Materials Chert;
    public static Materials Chimerite;
    public static Materials Coral;
    public static Materials CrudeOil;
    public static Materials Chrysocolla;
    public static Materials CrystalFlux;
    public static Materials Cyanite;
    public static Materials Dacite;
    public static Materials DarkIron;
    public static Materials DarkStone;
    public static Materials Demonite;
    public static Materials Desh;
    public static Materials Desichalkos;
    public static Materials Dilithium;
    public static Materials Draconic;
    public static Materials Drulloy;
    public static Materials Duranium;
    public static Materials Eclogite;
    public static Materials ElectrumFlux;
    public static Materials Emery;
    public static Materials EnderiumBase;
    public static Materials Energized;
    public static Materials Epidote;
    public static Materials Eximite;
    public static Materials FierySteel;
    public static Materials Firestone;
    public static Materials Fluorite;
    public static Materials FoolsRuby;
    public static Materials Force;
    public static Materials Forcicium;
    public static Materials Forcillium;
    public static Materials Gabbro;
    public static Materials Glowstone;
    public static Materials Gneiss;
    public static Materials Graphite;
    public static Materials Graphene;
    public static Materials Greenschist;
    public static Materials Greenstone;
    public static Materials Greywacke;
    public static Materials Haderoth;
    public static Materials Hematite;
    public static Materials Hepatizon;
    public static Materials HSLA;
    public static Materials Ignatius;
    public static Materials Infernal;
    public static Materials Infuscolium;
    public static Materials InfusedGold;
    public static Materials InfusedAir;
    public static Materials InfusedFire;
    public static Materials InfusedEarth;
    public static Materials InfusedWater;
    public static Materials InfusedEntropy;
    public static Materials InfusedOrder;
    public static Materials InfusedVis;
    public static Materials InfusedDull;
    public static Materials Inolashite;
    public static Materials Invisium;
    public static Materials Jade;
    public static Materials Kalendrite;
    public static Materials Komatiite;
    public static Materials Lava;
    public static Materials Lemurite;
    public static Materials Limestone;
    public static Materials Magma;
    public static Materials Mawsitsit;
    public static Materials Mercassium;
    public static Materials MeteoricIron;
    public static Materials MeteoricSteel;
    public static Materials Meteorite;
    public static Materials Meutoite;
    public static Materials Migmatite;
    public static Materials Mimichite;
    public static Materials Moonstone;
    public static Materials Naquadah;
    public static Materials NaquadahAlloy;
    public static Materials NaquadahEnriched;
    public static Materials Naquadria;
    public static Materials Nether;
    public static Materials NetherBrick;
    public static Materials NetherQuartz;
    public static Materials NetherStar;
    public static Materials ObsidianFlux;
    public static Materials Oilsands;
    public static Materials Onyx;
    public static Materials Orichalcum;
    public static Materials Osmonium;
    public static Materials Oureclase;
    public static Materials Painite;
    public static Materials Peanutwood;
    public static Materials Petroleum;
    public static Materials Pewter;
    public static Materials Phoenixite;
    public static Materials Prometheum;
    public static Materials Quartzite;
    public static Materials Randomite;
    public static Materials Rhyolite;
    public static Materials Rubracium;
    public static Materials Sand;
    public static Materials Sanguinite;
    public static Materials Siltstone;
    public static Materials Sunstone;
    public static Materials Tar;
    public static Materials Tartarite;
    public static Materials UUAmplifier;
    public static Materials UUMatter;
    public static Materials Void;
    public static Materials Voidstone;
    public static Materials Vulcanite;
    public static Materials Vyroxeres;
    public static Materials Yellorium;
    public static Materials Zectium;
    public static Materials Ultimate                = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Ultimate"                ,   "Ultimate"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.MACHINA, 8)));
    public static Materials Advanced                = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Advanced"                ,   "Advanced"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.MACHINA, 4)));

    /**
     * Tiered materials, primarily Circuitry, Batteries and other Technical things
     */
    public static Materials ULV                     = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Primitive"               ,   "Primitive"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.MACHINA, 1)));
    public static Materials LV                      = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Basic"                   ,   "Basic"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.MACHINA, 2)));
    public static Materials MV                      = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Good"                    ,   "Good"                          ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.MACHINA, 3)));
    public static Materials HV                      = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Advanced"                ,   "Advanced"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.MACHINA, 4)));
    public static Materials EV                      = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Data"                    ,   "Data"                          ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.MACHINA, 5)));
    public static Materials IV                      = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Elite"                   ,   "Elite"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.MACHINA, 6)));
    public static Materials LuV                     = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Master"                  ,   "Master"                        ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.MACHINA, 7)));
    public static Materials ZPM                     = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Ultimate"                ,   "Ultimate"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.MACHINA, 8)));
    public static Materials UV                      = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Superconductor"          ,   "Superconductor"                ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.MACHINA, 9)));
    public static Materials UHV                     = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Infinite"                ,   "Infinite"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 10)));
    public static Materials UEV                     = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Bio"                     ,   "Bio"                           ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 11)));
    public static Materials UIV                     = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Optical"                 ,   "Optical"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 12)));
    public static Materials UMV                     = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Exotic"                  ,   "Exotic"                        ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 13)));
    public static Materials UXV                     = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Cosmic"                  ,   "Cosmic"                        ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 14)));
    public static Materials MAX                     = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Transcendent"            ,   "Transcendent"                  ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 15)));

    public static Materials Resistor                = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Resistor"                ,   "Resistor"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 1)));
    public static Materials Diode                   = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Diode"                   ,   "Diode"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 1)));
    public static Materials Transistor              = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Transistor"              ,   "Transistor"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 1)));
    public static Materials Capacitor               = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Capacitor"               ,   "Capacitor"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 1)));
    public static Materials Inductor                = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Inductor"                ,   "Inductor"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 1)));

    public static Materials Nano                    = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Nano"                    ,   "Bio"                           ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 11)));
    public static Materials Piko                    = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Piko"                    ,   "Bio"                           ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 12)));
    public static Materials Quantum                 = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Quantum"                 ,   "Bio"                           ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 13)));


    /**
     * Aliases for the old style of tiered circuits
     */
    @Deprecated
    public static Materials Primitive = Materials.ULV;
    @Deprecated
    public static Materials Basic = Materials.LV;
    @Deprecated
    public static Materials Good = Materials.MV;
    @Deprecated
    public static Materials Data = Materials.EV;
    @Deprecated
    public static Materials Elite = Materials.IV;
    @Deprecated
    public static Materials Master = Materials.LuV;
    @Deprecated
    public static Materials Infinite = Materials.UHV;
    @Deprecated
    public static Materials Bio = Materials.UEV;
    @Deprecated
    public static Materials Optical = Materials.UIV;
    @Deprecated
    public static Materials Exotic = Materials.UMV;
    @Deprecated
    public static Materials Cosmic = Materials.UXV;
    @Deprecated
    public static Materials Transcendent = Materials.MAX;




    /**
     * Not possible to determine exact Components
     */
    public static Materials Antimatter              = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Antimatter"              ,   "Antimatter"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyePink        , Arrays.asList(new TC_AspectStack(TC_Aspects.POTENTIA, 9), new TC_AspectStack(TC_Aspects.PERFODIO, 8)));
    public static Materials AdvancedGlue            = new MaterialBuilder(567, TextureSet.SET_FLUID         ,                                                                                                         "Advanced Glue").setName("AdvancedGlue").addCell().addFluid().setRGB(255, 255, 185).setColor(Dyes.dyeYellow).setAspects(Collections.singletonList(new TC_AspectStack(TC_Aspects.LIMUS, 5))).constructMaterial();
    public static Materials BioFuel                 = new Materials( 705, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 128,   0,   0,   "BioFuel"                 ,   "Biofuel"                       ,    0,       6,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      );
    public static Materials Biomass                 = new Materials( 704, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,   0, 255,   0,   0,   "Biomass"        	     ,   "Forestry Biomass"              ,    3,       8,         -1,    0, false, false,   1,   1,   1, Dyes.dyeGreen       );
    public static Materials CharcoalByproducts      = new MaterialBuilder(675, TextureSet.SET_FLUID             ,                                                                                                     "Charcoal Byproducts").addCell().setRGB(120, 68, 33).setColor(Dyes.dyeBrown).constructMaterial();
    public static Materials Cheese                  = new Materials( 894, TextureSet.SET_FINE              ,   1.0F,      0,  0, 1    |8                   , 255, 255,   0,   0,   "Cheese"                  ,   "Cheese"                        ,    0,       0,        320,    0, false, false,   1,   1,   1, Dyes.dyeYellow      );
    public static Materials Chili                   = new Materials( 895, TextureSet.SET_FINE              ,   1.0F,      0,  0, 1                         , 200,   0,   0,   0,   "Chili"                   ,   "Chili"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeRed         );
    public static Materials Chocolate               = new Materials( 886, TextureSet.SET_FINE              ,   1.0F,      0,  0, 1                         , 190,  95,   0,   0,   "Chocolate"               ,   "Chocolate"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       );
    public static Materials Cluster                 = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255, 127,   "Cluster"                 ,   "Cluster"                       ,    0,       0,         -1,    0, false,  true,   1,   1,   1, Dyes.dyeWhite       );
    public static Materials CoalFuel                = new Materials( 710, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,  50,  50,  70,   0,   "CoalFuel"                ,   "Coalfuel"                      ,    0,      16,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       );
    public static Materials Cocoa                   = new Materials( 887, TextureSet.SET_FINE              ,   1.0F,      0,  0, 1                         , 190,  95,   0,   0,   "Cocoa"                   ,   "Cocoa"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       );
    public static Materials Coffee                  = new Materials( 888, TextureSet.SET_FINE              ,   1.0F,      0,  0, 1                         , 150,  75,   0,   0,   "Coffee"                  ,   "Coffee"                        ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       );
    public static Materials Creosote                = new Materials( 712, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 128,  64,   0,   0,   "Creosote"                ,   "Creosote"                      ,    3,       8,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       );
    public static Materials Ethanol                 = new Materials( 706, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 128,   0,   0,   "Ethanol"                 ,   "Ethanol"                       ,    0,     192,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 1, Arrays.asList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.VENENUM, 1), new TC_AspectStack(TC_Aspects.AQUA, 1)));
    public static Materials FishOil                 = new Materials( 711, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 196,   0,   0,   "FishOil"                 ,   "Fish Oil"                      ,    3,       2,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      , Collections.singletonList(new TC_AspectStack(TC_Aspects.CORPUS, 2)));
    public static Materials FermentedBiomass        = new MaterialBuilder(691, TextureSet.SET_FLUID             ,                                                                                                     "Fermented Biomass").addCell().addFluid().setRGB(68, 85, 0).setColor(Dyes.dyeBrown).constructMaterial();
    public static Materials Fuel                    = new Materials( 708, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 255,   0,   0,   "Fuel"                  ,   "Diesel"                        ,    0,     480,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      );
    public static Materials Glue                    = new Materials( 726, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 200, 196,   0,   0,   "Glue"                    ,   "Refined Glue"                  ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , Collections.singletonList(new TC_AspectStack(TC_Aspects.LIMUS, 2)));
    public static Materials Gunpowder               = new Materials( 800, TextureSet.SET_DULL              ,   1.0F,      0,  0, 1                         , 128, 128, 128,   0,   "Gunpowder"               ,   "Gunpowder"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeGray        , Arrays.asList(new TC_AspectStack(TC_Aspects.PERDITIO, 3), new TC_AspectStack(TC_Aspects.IGNIS, 4)));
    public static Materials FryingOilHot            = new Materials( 727, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 200, 196,   0,   0,   "FryingOilHot"            ,   "Hot Frying Oil"                ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , Arrays.asList(new TC_AspectStack(TC_Aspects.AQUA, 1), new TC_AspectStack(TC_Aspects.IGNIS, 1)));
    public static Materials Honey                   = new Materials( 725, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 210, 200,   0,   0,   "Honey"                   ,   "Honey"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      );
    public static Materials Leather                 = new Materials(  -1, TextureSet.SET_ROUGH             ,   1.0F,      0,  0, 1                         , 150, 150,  80, 127,   "Leather"                 ,   "Leather"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      );
    public static Materials Lubricant               = new Materials( 724, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 196,   0,   0,   "Lubricant"               ,   "Lubricant"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , Arrays.asList(new TC_AspectStack(TC_Aspects.AQUA, 2), new TC_AspectStack(TC_Aspects.MACHINA, 1)));
    public static Materials McGuffium239            = new Materials( 999, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 200,  50, 150,   0,   "McGuffium239"            ,   "Mc Guffium 239"                ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyePink        , Arrays.asList(new TC_AspectStack(TC_Aspects.ALIENIS, 8), new TC_AspectStack(TC_Aspects.PERMUTATIO, 8), new TC_AspectStack(TC_Aspects.SPIRITUS, 8), new TC_AspectStack(TC_Aspects.AURAM, 8), new TC_AspectStack(TC_Aspects.VITIUM, 8), new TC_AspectStack(TC_Aspects.RADIO, 8), new TC_AspectStack(TC_Aspects.MAGNETO, 8), new TC_AspectStack(TC_Aspects.ELECTRUM, 8), new TC_AspectStack(TC_Aspects.NEBRISUM, 8), new TC_AspectStack(TC_Aspects.STRONTIO, 8)));
    public static Materials MeatRaw                 = new Materials( 892, TextureSet.SET_FINE              ,   1.0F,      0,  0, 1                         , 255, 100, 100,   0,   "MeatRaw"                 ,   "Raw Meat"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyePink        );
    public static Materials MeatCooked              = new Materials( 893, TextureSet.SET_FINE              ,   1.0F,      0,  0, 1                         , 150,  60,  20,   0,   "MeatCooked"              ,   "Cooked Meat"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyePink        );
    public static Materials Milk                    = new Materials( 885, TextureSet.SET_FINE              ,   1.0F,      0,  0, 1      |16                , 254, 254, 254,   0,   "Milk"                    ,   "Milk"                          ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , Collections.singletonList(new TC_AspectStack(TC_Aspects.SANO, 2)));
    public static Materials Mud                     = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Mud"                     ,   "Mud"                           ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       );
    public static Materials Oil                     = new Materials( 707, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,  10,  10,  10,   0,   "Oil"                     ,   "Oil"                           ,    3,      20,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       );
    public static Materials Paper                   = new Materials( 879, TextureSet.SET_PAPER             ,   1.0F,      0,  0, 1                         , 250, 250, 250,   0,   "Paper"                   ,   "Paper"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , Collections.singletonList(new TC_AspectStack(TC_Aspects.COGNITIO, 1)));
    public static Materials Peat                    = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Peat"                    ,   "Peat"                          ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       , Arrays.asList(new TC_AspectStack(TC_Aspects.POTENTIA, 2), new TC_AspectStack(TC_Aspects.IGNIS, 2)));
    public static Materials RareEarth               = new Materials( 891, TextureSet.SET_FINE              ,   1.0F,      0,  0, 1                         , 128, 128, 100,   0,   "RareEarth"               ,   "Rare Earth"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeGray        , Arrays.asList(new TC_AspectStack(TC_Aspects.VITREUS, 1), new TC_AspectStack(TC_Aspects.LUCRUM, 1)));
    public static Materials Red                     = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255,   0,   0,   0,   "Red"                     ,   "Red"                           ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeRed         );
    public static Materials Reinforced              = new Materials( 383, TextureSet.SET_METALLIC          ,   7.0F,    480,  4, 1|2          |64|128      , 105, 141, 165,   0,   "Reinforced"              ,   "Reinforced"                    ,    0,       0,         -1, 1700,  true, false,   1,   1,   1, Dyes.dyeBlue        ).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials SeedOil                 = new Materials( 713, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 196, 255,   0,   0,   "SeedOil"                 ,   "Seed Oil"                      ,    3,       2,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLime        , Collections.singletonList(new TC_AspectStack(TC_Aspects.GRANUM, 2)));
    public static Materials SeedOilHemp             = new Materials( 722, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 196, 255,   0,   0,   "SeedOilHemp"             ,   "Hemp Seed Oil"                 ,    3,       2,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLime        , Collections.singletonList(new TC_AspectStack(TC_Aspects.GRANUM, 2)));
    public static Materials SeedOilLin              = new Materials( 723, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 196, 255,   0,   0,   "SeedOilLin"              ,   "Lin Seed Oil"                  ,    3,       2,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLime        , Collections.singletonList(new TC_AspectStack(TC_Aspects.GRANUM, 2)));
    public static Materials Stone                   = new Materials( 299, TextureSet.SET_ROUGH             ,   4.0F,     32,  1, 1            |64|128      , 205, 205, 205,   0,   "Stone"                   ,   "Stone"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TC_Aspects.TERRA, 1)));
    public static Materials TNT                     = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "TNT"                     ,   "TNT"                           ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeRed         , Arrays.asList(new TC_AspectStack(TC_Aspects.PERDITIO, 7), new TC_AspectStack(TC_Aspects.IGNIS, 4)));
    public static Materials Unstable                = new Materials( 396, TextureSet.SET_SHINY             ,   1.0F,      0,  4, 1                         , 220, 220, 220, 127,   "Unstable"                ,   "Unstable"                      ,    0,       0,         -1,    0, false,  true,   1,   1,   1, Dyes.dyeWhite       , Collections.singletonList(new TC_AspectStack(TC_Aspects.PERDITIO, 4)));
    public static Materials Unstableingot           = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  4, 0                         , 255, 255, 255, 127,   "Unstableingot"           ,   "Unstable"                      ,    0,       0,         -1,    0, false,  true,   1,   1,   1, Dyes.dyeWhite       , Collections.singletonList(new TC_AspectStack(TC_Aspects.PERDITIO, 4)));
    public static Materials Vinegar                 = new MaterialBuilder(690, TextureSet.SET_FLUID             ,                                                                                                     "Vinegar").setColor(Dyes.dyeBrown).constructMaterial();
    public static Materials Wheat                   = new Materials( 881, TextureSet.SET_POWDER            ,   1.0F,      0,  0, 1                         , 255, 255, 196,   0,   "Wheat"                   ,   "Wheat"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      , Collections.singletonList(new TC_AspectStack(TC_Aspects.MESSIS, 2)));
    public static Materials WoodGas                 = new MaterialBuilder(660, TextureSet.SET_FLUID             ,                                                                                                     "Wood Gas").addCell().addGas().setRGB(222, 205, 135).setColor(Dyes.dyeBrown).setFuelType(MaterialBuilder.GAS).setFuelPower(24).constructMaterial();
    public static Materials WoodTar                 = new MaterialBuilder(662, TextureSet.SET_FLUID             ,                                                                                                     "Wood Tar").addCell().addFluid().setRGB(40, 23, 11).setColor(Dyes.dyeBrown).constructMaterial();
    public static Materials WoodVinegar             = new MaterialBuilder(661, TextureSet.SET_FLUID             ,                                                                                                     "Wood Vinegar").addCell().addFluid().setRGB(212, 85, 0).setColor(Dyes.dyeBrown).constructMaterial();
    public static Materials WeedEX9000              = new MaterialBuilder(242, TextureSet.SET_FLUID             ,                                                                                                     "Weed-EX 9000").addFluid().setRGB(64, 224, 86).setColor(Dyes.dyeGreen).constructMaterial();

    /**
     * TODO: This
     */
    public static Materials AluminiumBrass          = new Materials(  -1, TextureSet.SET_METALLIC          ,   6.0F,     64,  2, 1|2          |64          , 255, 255, 255,   0,   "AluminiumBrass"          ,   "Aluminium Brass"               ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      );
    public static Materials Osmiridium              = new Materials( 317, TextureSet.SET_METALLIC          ,   7.0F,   1600,  3, 1|2          |64|128      , 100, 100, 255,   0,   "Osmiridium"              ,   "Osmiridium"                    ,    0,       0,       3500, 4500,  true, false,   1,   1,   1, Dyes.dyeLightBlue   , 1, Arrays.asList(new MaterialStack(Iridium, 3), new MaterialStack(Osmium, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Sunnarium               = new Materials( 318, TextureSet.SET_SHINY             ,   1.0F,      0,  1, 1|2          |64|128      , 255, 255,   0,   0,   "Sunnarium"               ,   "Sunnarium"                     ,    0,       0,       4200, 4200,  true, false,   1,   1,   1, Dyes.dyeYellow      ).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials Endstone                = new Materials( 808, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1                         , 255, 255, 255,   0,   "Endstone"                ,   "Endstone"                      ,    0,       0,         -1,    0, false, false,   0,   1,   1, Dyes.dyeYellow      );
    public static Materials Netherrack              = new Materials( 807, TextureSet.SET_DULL              ,   1.0F,      0,  0, 1                         , 200,   0,   0,   0,   "Netherrack"              ,   "Netherrack"                    ,    0,       0,         -1,    0, false, false,   0,   1,   1, Dyes.dyeRed         );
    public static Materials SoulSand                = new Materials(  -1, TextureSet.SET_DULL              ,   1.0F,      0,  0, 1                         , 255, 255, 255,   0,   "SoulSand"                ,   "Soulsand"                      ,    0,       0,         -1,    0, false, false,   0,   1,   1, Dyes.dyeBrown       );
    /**
     * First Degree Compounds
     */
    public static Materials Methane                 = new Materials( 715, TextureSet.SET_FLUID             ,   1.0F,      0,  1,         16                , 255, 255, 255,   0,   "Methane"                 ,   "Methane"                       ,    1,     104,         -1,    0, false, false,   3,   1,   1, Dyes.dyeMagenta     , 1, Arrays.asList(new MaterialStack(Carbon, 1), new MaterialStack(Hydrogen, 4)));
    public static Materials CarbonDioxide           = new Materials( 497, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16|32             , 169, 208, 245, 240,   "CarbonDioxide"           ,   "Carbon Dioxide"                ,    0,       0,         25,    1, false,  true,   1,   1,   1, Dyes.dyeLightBlue   , 0, Arrays.asList(new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 2))).setHasCorrespondingGas(true);
    public static Materials NobleGases              = new Materials( 496, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16|32             , 169, 208, 245, 240,   "NobleGases"              ,   "Noble Gases"                   ,    0,       0,          4,    0, false,  true,   1,   1,   1, Dyes.dyeLightBlue   , 2, Arrays.asList(new MaterialStack(CarbonDioxide,21),new MaterialStack(Helium, 9), new MaterialStack(Methane, 3), new MaterialStack(Deuterium, 1))).setHasCorrespondingGas(true);
    public static Materials Air                     = new Materials(  -1, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16|32             , 169, 208, 245, 240,   "Air"                     ,   "Air"                           ,    0,       0,         -1,    0, false,  true,   1,   1,   1, Dyes.dyeLightBlue   , 0, Arrays.asList(new MaterialStack(Nitrogen, 40), new MaterialStack(Oxygen, 11), new MaterialStack(Argon, 1),new MaterialStack(NobleGases,1)));
    public static Materials LiquidAir               = new Materials( 495, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16|32             , 169, 208, 245, 240,   "LiquidAir"               ,   "Liquid Air"                    ,    0,       0,          4,    0, false,  true,   1,   1,   1, Dyes.dyeLightBlue   , 2, Arrays.asList(new MaterialStack(Nitrogen, 40), new MaterialStack(Oxygen, 11), new MaterialStack(Argon, 1),new MaterialStack(NobleGases,1)));
    public static Materials LiquidNitrogen          = new Materials( 494, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16|32             , 169, 208, 245, 240,   "LiquidNitrogen"          ,   "Liquid Nitrogen"               ,    0,       0,          4,    0, false,  true,   1,   1,   1, Dyes.dyeLightBlue   , 1, Collections.singletonList(new MaterialStack(Nitrogen, 1)));
    public static Materials LiquidOxygen            = new Materials( 493, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16|32             , 169, 208, 245, 240,   "LiquidOxygen"            ,   "Liquid Oxygen"                 ,    0,       0,          4,    0, false,  true,   1,   1,   1, Dyes.dyeLightBlue   , 1, Collections.singletonList(new MaterialStack(Oxygen, 1)));
    public static Materials SiliconDioxide          = new MaterialBuilder(837, TextureSet.SET_QUARTZ, "Silicon Dioxide").setToolSpeed(1.0F).setDurability(0).setToolQuality(1).addDustItems().setRGB(255, 255, 255).setColor(Dyes.dyeLightGray).setOreValue(1).setExtraData(0).setMaterialList(new MaterialStack(Silicon, 1), new MaterialStack(Oxygen, 2)).constructMaterial();
    public static Materials Jasper                  = new Materials( 511, TextureSet.SET_EMERALD           ,   1.0F,      0,  2, 1  |4|8      |64          , 200,  80,  80, 100,   "Jasper"                  ,   "Jasper"                        ,    0,       0,         -1,    0, false,  true,   3,   1,   1, Dyes.dyeRed         , 1, Collections.singletonList(new MaterialStack(SiliconDioxide, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.LUCRUM, 4), new TC_AspectStack(TC_Aspects.VITREUS, 2)));
    public static Materials Almandine               = new Materials( 820, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1    |8                   , 255,   0,   0,   0,   "Almandine"               ,   "Almandine"                     ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeRed         , 0, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Iron, 3), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12)));
    public static Materials Andradite               = new Materials( 821, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1    |8                   , 150, 120,   0,   0,   "Andradite"               ,   "Andradite"                     ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeYellow      , 1, Arrays.asList(new MaterialStack(Calcium, 3), new MaterialStack(Iron, 2), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12)));
    public static Materials AnnealedCopper          = new Materials( 345, TextureSet.SET_SHINY             ,   1.0F,      0,  2, 1|2             |128      , 255, 120,  20,   0,   "AnnealedCopper"          ,   "Annealed Copper"               ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeOrange      , 2, Collections.singletonList(new MaterialStack(Copper, 1)));
    public static Materials Asbestos                = new Materials( 946, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   , 230, 230, 230,   0,   "Asbestos"                ,   "Asbestos"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Magnesium, 3), new MaterialStack(Silicon, 2), new MaterialStack(Hydrogen, 4), new MaterialStack(Oxygen, 9))); // Mg3Si2O5(OH)4
    public static Materials Ash                     = new Materials( 815, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1                         , 150, 150, 150,   0,   "Ash"                     ,   "Ashes"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , 2, Collections.singletonList(new MaterialStack(Carbon, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.PERDITIO, 1)));
    public static Materials BandedIron              = new Materials( 917, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 145,  90,  90,   0,   "BandedIron"              ,   "Banded Iron"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       , 1, Arrays.asList(new MaterialStack(Iron, 2), new MaterialStack(Oxygen, 3)));
    public static Materials BatteryAlloy            = new Materials( 315, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1|2                       , 156, 124, 160,   0,   "BatteryAlloy"            ,   "Battery Alloy"                 ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyePurple      , 2, Arrays.asList(new MaterialStack(Lead, 4), new MaterialStack(Antimony, 1)));
    public static Materials BlueTopaz               = new Materials( 513, TextureSet.SET_GEM_HORIZONTAL    ,   7.0F,    256,  3, 1  |4|8      |64          ,   0,   0, 255, 127,   "BlueTopaz"               ,   "Blue Topaz"                    ,    0,       0,         -1,    0, false,  true,   3,   1,   1, Dyes.dyeBlue        , 0, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 1), new MaterialStack(Fluorine, 2), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 6)), Arrays.asList(new TC_AspectStack(TC_Aspects.LUCRUM, 6), new TC_AspectStack(TC_Aspects.VITREUS, 4)));
    public static Materials Bone                    = new Materials( 806, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1                         , 250, 250, 250,   0,   "Bone"                    ,   "Bone"                          ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Collections.singletonList(new MaterialStack(Calcium, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.MORTUUS, 2), new TC_AspectStack(TC_Aspects.CORPUS, 1)));
    public static Materials Brass                   = new Materials( 301, TextureSet.SET_METALLIC          ,   7.0F,     96,  1, 1|2          |64|128      , 255, 180,   0,   0,   "Brass"                   ,   "Brass"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Zinc, 1), new MaterialStack(Copper, 3)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1)));
    public static Materials Bronze                  = new Materials( 300, TextureSet.SET_METALLIC          ,   6.0F,    192,  2, 1|2          |64|128      , 255, 128,   0,   0,   "Bronze"                  ,   "Bronze"                        ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 2, Arrays.asList(new MaterialStack(Tin, 1), new MaterialStack(Copper, 3)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1)));
    public static Materials BrownLimonite           = new Materials( 930, TextureSet.SET_METALLIC          ,   1.0F,      0,  1, 1    |8                   , 200, 100,   0,   0,   "BrownLimonite"           ,   "Brown Limonite"                ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       , 2, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Hydrogen, 1), new MaterialStack(Oxygen, 2))); // FeO(OH)
    public static Materials Calcite                 = new Materials( 823, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   , 250, 230, 220,   0,   "Calcite"                 ,   "Calcite"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 1, Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 3)));
    public static Materials Cassiterite             = new Materials( 824, TextureSet.SET_METALLIC          ,   1.0F,      0,  3, 1    |8                   , 220, 220, 220,   0,   "Cassiterite"             ,   "Cassiterite"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Tin, 1), new MaterialStack(Oxygen, 2)));
    public static Materials CassiteriteSand         = new Materials( 937, TextureSet.SET_SAND              ,   1.0F,      0,  1, 1    |8                   , 220, 220, 220,   0,   "CassiteriteSand"         ,   "Cassiterite Sand"              ,    0,       0,         -1,    0, false, false,   4,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Tin, 1), new MaterialStack(Oxygen, 2)));
    public static Materials Chalcopyrite            = new Materials( 855, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   , 160, 120,  40,   0,   "Chalcopyrite"            ,   "Chalcopyrite"                  ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      , 1, Arrays.asList(new MaterialStack(Copper, 1), new MaterialStack(Iron, 1), new MaterialStack(Sulfur, 2)));
    public static Materials Charcoal                = new Materials( 536, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1  |4                     , 100,  70,  70,   0,   "Charcoal"                ,   "Charcoal"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 1, Collections.singletonList(new MaterialStack(Carbon, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.POTENTIA, 2), new TC_AspectStack(TC_Aspects.IGNIS, 2)));
    public static Materials Chromite                = new Materials( 825, TextureSet.SET_METALLIC          ,   1.0F,      0,  1, 1    |8                   ,  35,  20,  15,   0,   "Chromite"                ,   "Chromite"                      ,    0,       0,       1700, 1700,  true, false,   6,   1,   1, Dyes.dyePink        , 1, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Chrome, 2), new MaterialStack(Oxygen, 4)));
    public static Materials ChromiumDioxide         = new Materials( 361, TextureSet.SET_DULL              ,  11.0F,    256,  3, 1|2                       , 230, 200, 200,   0,   "ChromiumDioxide"         ,   "Chromium Dioxide"              ,    0,       0,        650,  650, false, false,   5,   1,   1, Dyes.dyePink        , 1, Arrays.asList(new MaterialStack(Chrome, 1), new MaterialStack(Oxygen, 2)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.MACHINA, 1)));
    public static Materials Cinnabar                = new Materials( 826, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1    |8                   , 150,   0,   0,   0,   "Cinnabar"                ,   "Cinnabar"                      ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeBrown       , 2, Arrays.asList(new MaterialStack(Mercury, 1), new MaterialStack(Sulfur, 1)));
    public static Materials Water                   = new Materials( 701, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,   0,   0, 255,   0,   "Water"                   ,   "Water"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlue        , 0, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.AQUA, 2)));
    public static Materials Clay                    = new Materials( 805, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1                         , 200, 200, 220,   0,   "Clay"                    ,   "Clay"                          ,    0,       0,         -1,    0, false, false,   5,   1,   1, Dyes.dyeLightBlue   , 0, Arrays.asList(new MaterialStack(Sodium, 2), new MaterialStack(Lithium, 1), new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 2),new MaterialStack(Oxygen,7),new MaterialStack(Water,2)));
    public static Materials Coal                    = new Materials( 535, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1  |4|8                   ,  70,  70,  70,   0,   "Coal"                    ,   "Coal"                          ,    0,       0,         -1,    0, false, false,   2,   2,   1, Dyes.dyeBlack       , 1, Collections.singletonList(new MaterialStack(Carbon, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.POTENTIA, 2), new TC_AspectStack(TC_Aspects.IGNIS, 2)));
    public static Materials Cobaltite               = new Materials( 827, TextureSet.SET_METALLIC          ,   1.0F,      0,  1, 1    |8                   ,  80,  80, 250,   0,   "Cobaltite"               ,   "Cobaltite"                     ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeBlue        , 1, Arrays.asList(new MaterialStack(Cobalt, 1), new MaterialStack(Arsenic, 1), new MaterialStack(Sulfur, 1)));
    public static Materials Cooperite               = new Materials( 828, TextureSet.SET_METALLIC          ,   1.0F,      0,  1, 1    |8                   , 255, 255, 200,   0,   "Cooperite"               ,   "Sheldonite"                    ,    0,       0,         -1,    0, false, false,   5,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Platinum, 3), new MaterialStack(Nickel, 1), new MaterialStack(Sulfur, 1), new MaterialStack(Palladium, 1)));
    public static Materials Cupronickel             = new Materials( 310, TextureSet.SET_METALLIC          ,   6.0F,     64,  1, 1|2          |64          , 227, 150, 128,   0,   "Cupronickel"             ,   "Cupronickel"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 2, Arrays.asList(new MaterialStack(Copper, 1), new MaterialStack(Nickel, 1)));
    public static Materials DarkAsh                 = new Materials( 816, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1                         ,  50,  50,  50,   0,   "DarkAsh"                 ,   "Dark Ashes"                    ,    0,       0,         -1,    0, false, false,   1,   2,   1, Dyes.dyeGray        , 1, Collections.singletonList(new MaterialStack(Carbon, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.IGNIS, 1), new TC_AspectStack(TC_Aspects.PERDITIO, 1)));
    public static Materials DeepIron                = new Materials( 829, TextureSet.SET_METALLIC          ,   6.0F,    384,  2, 1|2  |8      |64          , 150, 140, 140,   0,   "DeepIron"                ,   "Deep Iron"                     ,    0,       0,       7500, 7500,  true, false,   3,   1,   1, Dyes.dyePink        , 2, Collections.singletonList(new MaterialStack(Iron, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.MAGNETO, 1))).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Diamond                 = new Materials( 500, TextureSet.SET_DIAMOND           ,   8.0F,   1280,  4, 1  |4|8      |64|128      , 200, 255, 255, 127,   "Diamond"                 ,   "Diamond"                       ,    0,       0,         -1,    0, false,  true,   5,  64,   1, Dyes.dyeWhite       , 1, Collections.singletonList(new MaterialStack(Carbon, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.VITREUS, 3), new TC_AspectStack(TC_Aspects.LUCRUM, 4)));
    public static Materials Electrum                = new Materials( 303, TextureSet.SET_SHINY             ,  12.0F,     64,  2, 1|2  |8      |64|128      , 255, 255, 100,   0,   "Electrum"                ,   "Electrum"                      ,    0,       0,         -1,    0, false, false,   4,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Silver, 1), new MaterialStack(Gold, 1)));
    public static Materials Emerald                 = new Materials( 501, TextureSet.SET_EMERALD           ,   7.0F,    256,  4, 1  |4|8      |64          ,  80, 255,  80, 127,   "Emerald"                 ,   "Emerald"                       ,    0,       0,         -1,    0, false,  true,   5,   1,   1, Dyes.dyeGreen       , 0, Arrays.asList(new MaterialStack(Beryllium, 3), new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 6), new MaterialStack(Oxygen, 18)), Arrays.asList(new TC_AspectStack(TC_Aspects.VITREUS, 3), new TC_AspectStack(TC_Aspects.LUCRUM, 5)));
    public static Materials FreshWater              = new Materials(  -1, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,   0,   0, 255,   0,   "FreshWater"              ,   "Fresh Water"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlue        , 0, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.AQUA, 2)));
    public static Materials Galena                  = new Materials( 830, TextureSet.SET_DULL              ,   1.0F,      0,  3, 1 |8                      , 100,  60, 100,   0,   "Galena"                  ,   "Galena"                        ,    0,       0,         -1,    0, false, false,   4,   1,   1, Dyes.dyePurple      , 1, Arrays.asList(new MaterialStack(Lead, 1), new MaterialStack(Sulfur, 1)));
    public static Materials Garnierite              = new Materials( 906, TextureSet.SET_METALLIC          ,   1.0F,      0,  3, 1    |8                   ,  50, 200,  70,   0,   "Garnierite"              ,   "Garnierite"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightBlue   , 1, Arrays.asList(new MaterialStack(Nickel, 1), new MaterialStack(Oxygen, 1)));
    public static Materials Glyceryl                = new Materials( 714, TextureSet.SET_FLUID             ,   1.0F,      0,  1,         16                ,   0, 150, 150,   0,   "Glyceryl"                ,   "Glyceryl Trinitrate"           ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeCyan        , 1, Arrays.asList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 5), new MaterialStack(Nitrogen, 3), new MaterialStack(Oxygen, 9)));
    public static Materials GreenSapphire           = new MaterialBuilder(504, TextureSet.SET_GEM_HORIZONTAL, "Green Sapphire").setToolSpeed(7.0F).setDurability(256).setToolQuality(2).addDustItems().addGemItems().setTransparent(true).addOreItems().addToolHeadItems().setRGBA(100, 200, 130, 127).setColor(Dyes.dyeCyan).setOreValue(5).setExtraData(0).setMaterialList(new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3)).setAspects(Arrays.asList(new TC_AspectStack(TC_Aspects.LUCRUM, 5), new TC_AspectStack(TC_Aspects.VITREUS, 3))).constructMaterial().disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials Grossular               = new Materials( 831, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1    |8                   , 200, 100,   0,   0,   "Grossular"               ,   "Grossular"                     ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeOrange      , 0, Arrays.asList(new MaterialStack(Calcium, 3), new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12)));
    public static Materials HolyWater               = new Materials( 729, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,   0,   0, 255,   0,   "HolyWater"               ,   "Holy Water"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlue        , 0, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.AQUA, 2), new TC_AspectStack(TC_Aspects.AURAM, 1)));
    public static Materials Ice                     = new Materials( 702, TextureSet.SET_SHINY             ,   1.0F,      0,  0, 1|      16                , 200, 200, 255,   0,   "Ice"                     ,   "Ice"                           ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlue        , 0, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.GELUM, 2)));
    public static Materials Ilmenite                = new Materials( 918, TextureSet.SET_METALLIC          ,   1.0F,      0,  3, 1    |8                   ,  70,  55,  50,   0,   "Ilmenite"                ,   "Ilmenite"                      ,    0,       0,         -1,    0, false, false,   1,   2,   1, Dyes.dyePurple      , 0, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Titanium, 1), new MaterialStack(Oxygen, 3)));
    public static Materials Rutile                  = new Materials( 375, TextureSet.SET_GEM_HORIZONTAL    ,   1.0F,      0,  2, 1    |8                   , 212,  13,  92,   0,   "Rutile"                  ,   "Rutile"                        ,    0,       0,         -1,    0, false, false,   1,   2,   1, Dyes.dyeRed         , 0, Arrays.asList(new MaterialStack(Titanium, 1), new MaterialStack(Oxygen, 2)));
    public static Materials Bauxite                 = new Materials( 822, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   , 200, 100,   0,   0,   "Bauxite"                 ,   "Bauxite"                       ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeBrown       , 1, Arrays.asList(new MaterialStack(Rutile, 2), new MaterialStack(Aluminium, 16), new MaterialStack(Hydrogen, 10), new MaterialStack(Oxygen, 11)));
    public static Materials Titaniumtetrachloride   = new Materials( 376, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16                , 212,  13,  92,   0,   "Titaniumtetrachloride"   ,   "Titaniumtetrachloride"         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeRed         , 0, Arrays.asList(new MaterialStack(Titanium, 1), new MaterialStack(Chlorine, 4)));
    public static Materials Magnesiumchloride       = new Materials( 377, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1|16                      , 212,  13,  92,   0,   "Magnesiumchloride"       ,   "Magnesiumchloride"             ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeRed         , 0, Arrays.asList(new MaterialStack(Magnesium, 1), new MaterialStack(Chlorine, 2)));
    public static Materials Invar                   = new Materials( 302, TextureSet.SET_METALLIC          ,   6.0F,    256,  2, 1|2          |64|128      , 180, 180, 120,   0,   "Invar"                   ,   "Invar"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       , 2, Arrays.asList(new MaterialStack(Iron, 2), new MaterialStack(Nickel, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.GELUM, 1)));
    public static Materials Kanthal                 = new Materials( 312, TextureSet.SET_METALLIC          ,   6.0F,     64,  2, 1|2          |64          , 194, 210, 223,   0,   "Kanthal"                 ,   "Kanthal"                       ,    0,       0,       1800, 1800,  true, false,   1,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Aluminium, 1), new MaterialStack(Chrome, 1))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials Lazurite                = new Materials( 524, TextureSet.SET_LAPIS             ,   1.0F,      0,  1, 1  |4|8                   , 100, 120, 255,   0,   "Lazurite"                ,   "Lazurite"                      ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeCyan        , 1, Arrays.asList(new MaterialStack(Aluminium, 6), new MaterialStack(Silicon, 6), new MaterialStack(Calcium, 8), new MaterialStack(Sodium, 8)));
    public static Materials Magnalium               = new Materials( 313, TextureSet.SET_DULL              ,   6.0F,    256,  2, 1|2          |64|128      , 200, 190, 255,   0,   "Magnalium"               ,   "Magnalium"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightBlue   , 2, Arrays.asList(new MaterialStack(Magnesium, 1), new MaterialStack(Aluminium, 2)));
    public static Materials Magnesite               = new Materials( 908, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   , 250, 250, 180,   0,   "Magnesite"               ,   "Magnesite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyePink        , 1, Arrays.asList(new MaterialStack(Magnesium, 1), new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 3)));
    public static Materials Magnetite               = new Materials( 870, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   ,  30,  30,  30,   0,   "Magnetite"               ,   "Magnetite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeGray        , 1, Arrays.asList(new MaterialStack(Iron, 3), new MaterialStack(Oxygen, 4)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.MAGNETO, 1)));
    public static Materials Molybdenite             = new Materials( 942, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   ,  25,  25,  25,   0,   "Molybdenite"             ,   "Molybdenite"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlue        , 1, Arrays.asList(new MaterialStack(Molybdenum, 1), new MaterialStack(Sulfur, 2))); // MoS2 (also source of Re)
    public static Materials Nichrome                = new Materials( 311, TextureSet.SET_METALLIC          ,   6.0F,     64,  2, 1|2          |64          , 205, 206, 246,   0,   "Nichrome"                ,   "Nichrome"                      ,    0,       0,       2700, 2700,  true, false,   1,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(Nickel, 4), new MaterialStack(Chrome, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials NiobiumNitride          = new Materials( 359, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1|2                       ,  29,  41,  29,   0,   "NiobiumNitride"          ,   "Niobium Nitride"               ,    0,       0,       2573, 2573,  true, false,   1,   1,   1, Dyes.dyeBlack       , 1, Arrays.asList(new MaterialStack(Niobium, 1), new MaterialStack(Nitrogen, 1))); // Anti-Reflective Material
    public static Materials NiobiumTitanium         = new Materials( 360, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1|2                       ,  29,  29,  41,   0,   "NiobiumTitanium"         ,   "Niobium-Titanium"              ,    0,       0,       4500, 4500,  true, false,   1,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Niobium, 1), new MaterialStack(Titanium, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials NitroCarbon             = new Materials( 716, TextureSet.SET_FLUID             ,   1.0F,      0,  1,         16                ,   0,  75, 100,   0,   "NitroCarbon"             ,   "Nitro-Carbon"                  ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeCyan        , 1, Arrays.asList(new MaterialStack(Nitrogen, 1), new MaterialStack(Carbon, 1)));
    public static Materials NitrogenDioxide         = new Materials( 717, TextureSet.SET_FLUID             ,   1.0F,      0,  1,         16                , 100, 175, 255,   0,   "NitrogenDioxide"         ,   "Nitrogen Dioxide"              ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeCyan        , 1, Arrays.asList(new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 2)));
    public static Materials Obsidian                = new Materials( 804, TextureSet.SET_DULL              ,   1.0F,      0,  3, 1|2                       ,  80,  50, 100,   0,   "Obsidian"                ,   "Obsidian"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 1, Arrays.asList(new MaterialStack(Magnesium, 1), new MaterialStack(Iron, 1), new MaterialStack(Silicon, 2), new MaterialStack(Oxygen, 8)));
    public static Materials Phosphate               = new Materials( 833, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8|16                , 255, 255,   0,   0,   "Phosphate"               ,   "Phosphate"                     ,    0,       0,         -1,    0, false, false,   2,   1,   1, Dyes.dyeYellow      , 1, Arrays.asList(new MaterialStack(Phosphorus, 1), new MaterialStack(Oxygen, 4)));
    public static Materials PigIron                 = new Materials( 307, TextureSet.SET_METALLIC          ,   6.0F,    384,  2, 1|2  |8      |64          , 200, 180, 180,   0,   "PigIron"                 ,   "Pig Iron"                      ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyePink        , 2, Collections.singletonList(new MaterialStack(Iron, 1)));
    public static Materials Plastic                 = new Materials( 874, TextureSet.SET_DULL              ,   3.0F,     32,  1, 1|2          |64|128      , 200, 200, 200,   0,   "Plastic"                 ,   "Polyethylene"                  ,    0,       0,        400,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Carbon, 1), new MaterialStack(Hydrogen, 2)), Collections.singletonList(new TC_AspectStack(TC_Aspects.MOTUS, 2)));
    public static Materials Epoxid                  = new Materials( 470, TextureSet.SET_DULL              ,   3.0F,     32,  1, 1|2          |64|128      , 200, 140,  20,   0,   "Epoxid"                  ,   "Epoxid"                        ,    0,       0,        400,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Carbon, 21), new MaterialStack(Hydrogen, 24), new MaterialStack(Oxygen, 4)), Collections.singletonList(new TC_AspectStack(TC_Aspects.MOTUS, 2)));
    public static Materials Polydimethylsiloxane    = new MaterialBuilder(633, TextureSet.SET_FLUID         ,                                                                                                     "Polydimethylsiloxane").addDustItems().setRGB(245, 245, 245).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 1), new MaterialStack(Silicon, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Silicone                = new Materials( 471, TextureSet.SET_DULL              ,   3.0F,    128,  1, 1|2          |64|128      , 220, 220, 220,   0,   "Silicone"                ,   "Silicone Rubber"               ,    0,       0,        900,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 1), new MaterialStack(Silicon, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.MOTUS, 2)));
    public static Materials Polycaprolactam         = new Materials( 472, TextureSet.SET_DULL              ,   3.0F,     32,  1, 1|2          |64|128      ,  50,  50,  50,   0,   "Polycaprolactam"         ,   "Polycaprolactam"               ,    0,       0,        500,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 11), new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.MOTUS, 2)));
    public static Materials Polytetrafluoroethylene = new Materials( 473, TextureSet.SET_DULL              ,   3.0F,     32,  1, 1|2          |64|128      , 100, 100, 100,   0,   "Polytetrafluoroethylene" ,   "Polytetrafluoroethylene"       ,    0,       0,       1400,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Carbon, 2), new MaterialStack(Fluorine, 4)), Collections.singletonList(new TC_AspectStack(TC_Aspects.MOTUS, 2)));
    public static Materials Powellite               = new Materials( 883, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 255, 255,   0,   0,   "Powellite"               ,   "Powellite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Molybdenum, 1), new MaterialStack(Oxygen, 4)));
    public static Materials Pumice                  = new Materials( 926, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 230, 185, 185,   0,   "Pumice"                  ,   "Pumice"                        ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeGray        , 2, Collections.singletonList(new MaterialStack(Stone, 1)));
    public static Materials Pyrite                  = new Materials( 834, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1    |8                   , 150, 120,  40,   0,   "Pyrite"                  ,   "Pyrite"                        ,    0,       0,         -1,    0, false, false,   2,   1,   1, Dyes.dyeOrange      , 1, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Sulfur, 2)));
    public static Materials Pyrolusite              = new Materials( 943, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 150, 150, 170,   0,   "Pyrolusite"              ,   "Pyrolusite"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , 1, Arrays.asList(new MaterialStack(Manganese, 1), new MaterialStack(Oxygen, 2)));
    public static Materials Pyrope                  = new Materials( 835, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   , 120,  50, 100,   0,   "Pyrope"                  ,   "Pyrope"                        ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyePurple      , 0, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Magnesium, 3), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12)));
    public static Materials RockSalt                = new Materials( 944, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1    |8                   , 240, 200, 200,   0,   "RockSalt"                ,   "Rock Salt"                     ,    0,       0,         -1,    0, false, false,   2,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Chlorine, 1)));
    public static Materials Rubber                  = new Materials( 880, TextureSet.SET_SHINY             ,   1.5F,     32,  0, 1|2          |64|128      ,   0,   0,   0,   0,   "Rubber"                  ,   "Rubber"                        ,    0,       0,        400,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 0, Arrays.asList(new MaterialStack(Carbon, 5), new MaterialStack(Hydrogen, 8)), Collections.singletonList(new TC_AspectStack(TC_Aspects.MOTUS, 2)));
    public static Materials RawRubber               = new Materials( 896, TextureSet.SET_DULL              ,   1.0F,      0,  0, 1                         , 204, 199, 137,   0,   "RawRubber"               ,   "Raw Rubber"                    ,    0,       0,        400,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Carbon, 5), new MaterialStack(Hydrogen, 8)), Collections.singletonList(new TC_AspectStack(TC_Aspects.MOTUS, 2)));
    public static Materials Ruby                    = new Materials( 502, TextureSet.SET_RUBY              ,   7.0F,    256,  2, 1  |4|8      |64          , 255, 100, 100, 127,   "Ruby"                    ,   "Ruby"                          ,    0,       0,         -1,    0, false,  true,   5,   1,   1, Dyes.dyeRed         , 0, Arrays.asList(new MaterialStack(Chrome, 1), new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3)), Arrays.asList(new TC_AspectStack(TC_Aspects.LUCRUM, 6), new TC_AspectStack(TC_Aspects.VITREUS, 4))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials Salt                    = new Materials( 817, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1    |8                   , 250, 250, 250,   0,   "Salt"                    ,   "Salt"                          ,    0,       0,         -1,    0, false, false,   2,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Sodium, 1), new MaterialStack(Chlorine, 1)));
    public static Materials Saltpeter               = new Materials( 836, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1    |8                   , 230, 230, 230,   0,   "Saltpeter"               ,   "Saltpeter"                     ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 3)));
    public static Materials Sapphire                = new MaterialBuilder(503, TextureSet.SET_GEM_VERTICAL, "Sapphire").setToolSpeed(7.0F).setDurability(256).setToolQuality(2).addDustItems().addGemItems().setTransparent(true).addOreItems().addToolHeadItems().setRGBA(100, 100, 200, 127).setColor(Dyes.dyeBlue).setOreValue(5).setExtraData(0).setMaterialList(new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3)).setAspects(Arrays.asList(new TC_AspectStack(TC_Aspects.LUCRUM, 5), new TC_AspectStack(TC_Aspects.VITREUS, 3))).constructMaterial().disableAutoGeneratedBlastFurnaceRecipes();
    //public static Materials Sapphire              = new Materials( 503, TextureSet.SET_GEM_VERTICAL      ,   7.0F,    256,  2, 1  |4|8      |64          , 100, 100, 200, 127,   "Sapphire"                ,   "Sapphire"                      ,    0,       0,         -1,    0, false,  true,   5,   1,   1, Dyes.dyeBlue        , 1, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3)), Arrays.asList(new TC_AspectStack(TC_Aspects.LUCRUM, 5), new TC_AspectStack(TC_Aspects.VITREUS, 3)));
    public static Materials Scheelite               = new Materials( 910, TextureSet.SET_DULL              ,   1.0F,      0,  3, 1    |8                   , 200, 140,  20,   0,   "Scheelite"               ,   "Scheelite"                     ,    0,       0,       2500, 2500, false, false,   4,   1,   1, Dyes.dyeBlack       , 0, Arrays.asList(new MaterialStack(Tungsten, 1), new MaterialStack(Calcium, 1), new MaterialStack(Oxygen, 4)));
    public static Materials Snow                    = new Materials( 728, TextureSet.SET_FINE              ,   1.0F,      0,  0, 1|      16                , 250, 250, 250,   0,   "Snow"                    ,   "Snow"                          ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 0, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.GELUM, 1)));
    public static Materials Sodalite                = new Materials( 525, TextureSet.SET_LAPIS             ,   1.0F,      0,  1, 1  |4|8                   ,  20,  20, 255,   0,   "Sodalite"                ,   "Sodalite"                      ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeBlue        , 1, Arrays.asList(new MaterialStack(Aluminium, 3), new MaterialStack(Silicon, 3), new MaterialStack(Sodium, 4), new MaterialStack(Chlorine, 1)));
    public static Materials SodiumPersulfate        = new Materials( 718, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16                , 255, 255, 255,   0,   "SodiumPersulfate"        ,   "Sodium Persulfate"             ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 1, Arrays.asList(new MaterialStack(Sodium, 2), new MaterialStack(Sulfur, 2), new MaterialStack(Oxygen, 8)));
    public static Materials SodiumSulfide           = new Materials( 719, TextureSet.SET_FLUID             ,   1.0F,      0,  2, 1                         , 255, 230, 128,   0,   "SodiumSulfide"           ,   "Sodium Sulfide"                ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 1, Arrays.asList(new MaterialStack(Sodium, 2), new MaterialStack(Sulfur, 1)));
    public static Materials HydricSulfide           = new Materials( 460, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16                , 255, 255, 255,   0,   "HydricSulfide"           ,   "Hydrogen Sulfide"              ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 0, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Sulfur, 1)));

    public static Materials OilHeavy                = new Materials( 730, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,  10,  10,  10,   0,   "OilHeavy"                ,   "Heavy Oil"                     ,    3,      40,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       );
    public static Materials OilMedium               = new Materials( 731, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,  10,  10,  10,   0,   "OilMedium"               ,   "Raw Oil"                       ,    3,      30,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       );
    public static Materials OilLight                = new Materials( 732, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                ,  10,  10,  10,   0,   "OilLight"                ,   "Light Oil"                     ,    3,      20,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       );

    public static Materials NatruralGas             = new Materials( 733, TextureSet.SET_FLUID             ,   1.0F,      0,  1,         16                , 255, 255, 255,   0,   "NatruralGas"             ,   "Natural Gas"                   ,    1,      20,         -1,    0, false, false,   3,   1,   1, Dyes.dyeWhite       );
    public static Materials SulfuricGas             = new Materials( 734, TextureSet.SET_FLUID             ,   1.0F,      0,  1,         16                , 255, 255, 255,   0,   "SulfuricGas"             ,   "Sulfuric Gas"                  ,    1,      25,         -1,    0, false, false,   3,   1,   1, Dyes.dyeWhite       );
    public static Materials Gas                     = new Materials( 735, TextureSet.SET_FLUID             ,   1.0F,      0,  1,         16                , 255, 255, 255,   0,   "Gas"                     ,   "Refinery Gas"                  ,    1,     160,         -1,    0, false, false,   3,   1,   1, Dyes.dyeWhite).setCanBeCracked(true);
    public static Materials SulfuricNaphtha         = new Materials( 736, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 255,   0,   0,   "SulfuricNaphtha"         ,   "Sulfuric Naphtha"              ,    1,      40,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      );
    public static Materials SulfuricLightFuel       = new Materials( 737, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 255,   0,   0,   "SulfuricLightFuel"       ,   "Sulfuric Light Fuel"           ,    0,      40,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      );
    public static Materials SulfuricHeavyFuel       = new Materials( 738, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 255,   0,   0,   "SulfuricHeavyFuel"       ,   "Sulfuric Heavy Fuel"           ,    3,      40,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       );
    public static Materials Naphtha                 = new Materials( 739, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 255,   0,   0,   "Naphtha"                 ,   "Naphtha"                       ,    1,     220,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow).setCanBeCracked(true);
    public static Materials LightFuel               = new Materials( 740, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 255,   0,   0,   "LightFuel"               ,   "Light Fuel"                    ,    0,     305,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow).setCanBeCracked(true);
    public static Materials HeavyFuel               = new Materials( 741, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 255,   0,   0,   "HeavyFuel"               ,   "Heavy Fuel"                    ,    3,     240,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack).setCanBeCracked(true);
    public static Materials LPG                     = new Materials( 742, TextureSet.SET_FLUID             ,   1.0F,      0,  0,         16                , 255, 255,   0,   0,   "LPG"                     ,   "LPG"                           ,    1,     320,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      );

    public static Materials FluidNaquadahFuel       = new MaterialBuilder(600, TextureSet.SET_FLUID      ,                                                                                                 "Naquadah Fuel").setName("FluidNaqudahFuel").addCell().addFluid().setRGB(62, 62, 62).setColor(Dyes.dyeBlack).constructMaterial();
    public static Materials EnrichedNaquadria       = new MaterialBuilder(601, TextureSet.SET_FLUID      ,                                                                                                 "Enriched Naquadria").setName("EnrichedNaquadria").addCell().addFluid().setRGB(52, 52, 52).setColor(Dyes.dyeBlack).constructMaterial();

    public static Materials ReinforceGlass          = new MaterialBuilder(602, TextureSet.SET_FLUID      ,                                                                                                     "Reinforced Glass").setName("ReinforcedGlass").setRGB(192, 245, 254).setColor(Dyes.dyeWhite).setMeltingPoint(2000).constructMaterial().disableAutoGeneratedRecycleRecipes();
    public static Materials BioMediumRaw            = new MaterialBuilder(603, TextureSet.SET_FLUID      ,                                                                                                     "Raw Bio Catalyst Medium").setName("BioMediumRaw").addCell().addFluid().setRGB(97, 147, 46).setColor(Dyes.dyeLime).constructMaterial();
    public static Materials BioMediumSterilized     = new MaterialBuilder(604, TextureSet.SET_FLUID      ,                                                                                                     "Sterilized Bio Catalyst Medium").setName("BiohMediumSterilized").addCell().addFluid().setRGB(162, 253, 53).setColor(Dyes.dyeLime).constructMaterial();

    public static Materials Chlorobenzene             = new MaterialBuilder(605, TextureSet.SET_FLUID      ,                                                                                                     "Chlorobenzene").addCell().addFluid().setRGB(0, 50, 65).setColor(Dyes.dyeGray).setMaterialList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 5), new MaterialStack(Chlorine, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials DilutedHydrochloricAcid   = new MaterialBuilder(606, TextureSet.SET_FLUID      ,                                                                                                     "Diluted Hydrochloric Acid").setName("DilutedHydrochloricAcid_GT5U").addCell().addFluid().setRGB(153, 167, 163).setColor(Dyes.dyeLightGray).setMaterialList(new MaterialStack(Hydrogen, 1), new MaterialStack(Chlorine, 1)).constructMaterial();
    public static Materials Pyrochlore                = new MaterialBuilder(607, TextureSet.SET_METALLIC   ,                                                                                                     "Pyrochlore").addDustItems().addOreItems().setRGB(43, 17, 0).setColor(Dyes.dyeBlack).setMaterialList(new MaterialStack(Calcium, 2), new MaterialStack(Niobium, 2), new MaterialStack(Oxygen, 7)).addElectrolyzerRecipe().constructMaterial();

    public static Materials GrowthMediumRaw           = new MaterialBuilder(608, TextureSet.SET_FLUID      ,                                                                                                     "Raw Growth Catalyst Medium").setName("GrowthMediumRaw").addCell().addFluid().setRGB(211, 141, 95).setColor(Dyes.dyeOrange).constructMaterial();
    public static Materials GrowthMediumSterilized    = new MaterialBuilder(609, TextureSet.SET_FLUID      ,                                                                                                     "Growth Catalyst Medium").setName("GrowthMediumSterilized").addCell().addFluid().setRGB(222, 170, 135).setColor(Dyes.dyeOrange).constructMaterial();

    public static Materials FerriteMixture            = new MaterialBuilder(612, TextureSet.SET_METALLIC   ,                                                                                                     "Ferrite Mixture").addDustItems().setRGB(180, 180, 180).setColor(Dyes.dyeGray).setMaterialList(new MaterialStack(Nickel, 1), new MaterialStack(Zinc, 1), new MaterialStack(Iron, 4)).constructMaterial();
    public static Materials NickelZincFerrite         = new MaterialBuilder(613, TextureSet.SET_ROUGH      ,                                                                                                     "Nickel-Zinc Ferrite").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(3.0f).setDurability(32).setRGB(60, 60, 60).setColor(Dyes.dyeBlack).setBlastFurnaceRequired(true).setBlastFurnaceTemp(1500).setMaterialList(new MaterialStack(Nickel, 1), new MaterialStack(Zinc, 1), new MaterialStack(Iron, 4), new MaterialStack(Oxygen, 8)).constructMaterial();

    public static Materials Massicot                  = new MaterialBuilder(614, TextureSet.SET_DULL       ,                                                                                                     "Massicot").addDustItems().setRGB(255, 221, 85).setColor(Dyes.dyeYellow).setMaterialList(new MaterialStack(Lead, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials ArsenicTrioxide           = new MaterialBuilder(615, TextureSet.SET_SHINY      ,                                                                                                     "Arsenic Trioxide").addDustItems().setRGB(255, 255, 255).setColor(Dyes.dyeGreen).setMaterialList(new MaterialStack(Arsenic, 2), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();
    public static Materials CobaltOxide               = new MaterialBuilder(616, TextureSet.SET_DULL       ,                                                                                                     "Cobalt Oxide").addDustItems().setRGB(102, 128, 0).setColor(Dyes.dyeGreen).setMaterialList(new MaterialStack(Cobalt, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Zincite                   = new MaterialBuilder(617, TextureSet.SET_DULL       ,                                                                                                     "Zincite").addDustItems().setRGB(255, 255, 245).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Zinc, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials AntimonyTrioxide          = new MaterialBuilder(618, TextureSet.SET_DULL       ,                                                                                                     "Antimony Trioxide").addDustItems().setRGB(230, 230, 240).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Antimony, 2), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();
    public static Materials CupricOxide               = new MaterialBuilder(619, TextureSet.SET_DULL       ,                                                                                                     "Cupric Oxide").addDustItems().setRGB(15, 15, 15).setColor(Dyes.dyeBlack).setMeltingPoint(1599).setMaterialList(new MaterialStack(Copper, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Ferrosilite               = new MaterialBuilder(620, TextureSet.SET_DULL       ,                                                                                                     "Ferrosilite").addDustItems().setRGB(151, 99, 42).setColor(Dyes.dyeBrown).setMaterialList(new MaterialStack(Iron, 1), new MaterialStack(Silicon, 1), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();

    public static Materials Magnesia                  = new MaterialBuilder(621, TextureSet.SET_DULL       ,                                                                                                     "Magnesia").addDustItems().setRGB(255, 225, 225).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Magnesium, 1), new MaterialStack(Oxygen, 1)).constructMaterial();
    public static Materials Quicklime                 = new MaterialBuilder(622, TextureSet.SET_DULL       ,                                                                                                     "Quicklime").addDustItems().setRGB(240, 240, 240).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Calcium, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Potash                    = new MaterialBuilder(623, TextureSet.SET_DULL       ,                                                                                                     "Potash").addDustItems().setRGB(120, 66, 55).setColor(Dyes.dyeBrown).setMaterialList(new MaterialStack(Potassium, 2), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials SodaAsh                   = new MaterialBuilder(624, TextureSet.SET_DULL       ,                                                                                                     "Soda Ash").addDustItems().setRGB(220, 220, 255).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Sodium, 2), new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();

    public static Materials BioDiesel                 = new MaterialBuilder(627, TextureSet.SET_FLUID      ,                                                                                                     "Bio Diesel").addCell().addFluid().setRGB(255, 128, 0).setColor(Dyes.dyeOrange).setFuelType(MaterialBuilder.DIESEL).setFuelPower(320).constructMaterial();
    public static Materials NitrationMixture          = new MaterialBuilder(628, TextureSet.SET_FLUID      ,                                                                                                     "Nitration Mixture").addCell().setRGB(230, 226, 171).setColor(Dyes.dyeBrown).constructMaterial();
    public static Materials Glycerol                  = new MaterialBuilder(629, TextureSet.SET_FLUID      ,                                                                                                     "Glycerol").addCell().addFluid().setRGB(135, 222, 135).setColor(Dyes.dyeLime).setFuelType(MaterialBuilder.SEMIFLUID).setFuelPower(164).setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 8), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();
    public static Materials SodiumBisulfate           = new MaterialBuilder(630, TextureSet.SET_FLUID      ,                                                                                                     "Sodium Bisulfate").addDustItems().setRGB(0, 68, 85).setColor(Dyes.dyeBlue).setMaterialList(new MaterialStack(Sodium, 1), new MaterialStack(Hydrogen, 1), new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 4)).constructMaterial();
    public static Materials PolyphenyleneSulfide      = new MaterialBuilder(631, TextureSet.SET_DULL       ,                                                                                                     "Polyphenylene Sulfide").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(3.0f).setDurability(32).setToolQuality(1).setRGB(170, 136, 0).setColor(Dyes.dyeBrown).setMaterialList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 4), new MaterialStack(Sulfur, 1)).constructMaterial();
    public static Materials Dichlorobenzene           = new MaterialBuilder(632, TextureSet.SET_FLUID      ,                                                                                                     "Dichlorobenzene").addCell().addFluid().setRGB(0, 68, 85).setColor(Dyes.dyeBlue).setMaterialList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 4), new MaterialStack(Chlorine, 2)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Polystyrene               = new MaterialBuilder(636, TextureSet.SET_DULL       ,                                                                                                     "Polystyrene").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(3.0f).setDurability(32).setToolQuality(1).setRGB(190, 180, 170).setColor(Dyes.dyeLightGray).setMaterialList(new MaterialStack(Carbon, 8), new MaterialStack(Hydrogen, 8)).constructMaterial();
    public static Materials Styrene                   = new MaterialBuilder(637, TextureSet.SET_FLUID      ,                                                                                                     "Styrene").addCell().addFluid().setRGB(210, 200, 190).setColor(Dyes.dyeBlack).setMaterialList(new MaterialStack(Carbon, 8), new MaterialStack(Hydrogen, 8)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Isoprene                  = new MaterialBuilder(638, TextureSet.SET_FLUID      ,                                                                                                     "Isoprene").addCell().addFluid().setRGB(20, 20, 20).setColor(Dyes.dyeBlack).setMaterialList(new MaterialStack(Carbon, 5), new MaterialStack(Hydrogen, 8)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Tetranitromethane         = new MaterialBuilder(639, TextureSet.SET_FLUID      ,                                                                                                     "Tetranitromethane").addCell().addFluid().setRGB(15, 40, 40).setColor(Dyes.dyeBlack).setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(Nitrogen, 4), new MaterialStack(Oxygen, 8)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Ethenone                  = new MaterialBuilder(641, TextureSet.SET_FLUID      ,                                                                                                     "Ethenone").addCell().addGas().setRGB(20, 20, 70).setColor(Dyes.dyeBlack).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Ethane                    = new MaterialBuilder(642, TextureSet.SET_FLUID      ,                                                                                                     "Ethane").addCell().addGas().setRGB(200, 200, 255).setColor(Dyes.dyeLightBlue).setFuelType(MaterialBuilder.GAS).setFuelPower(168).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 6)).addElectrolyzerRecipe().setCanBeCracked(true).constructMaterial();
    public static Materials Propane                   = new MaterialBuilder(643, TextureSet.SET_FLUID      ,                                                                                                     "Propane").addCell().addGas().setRGB(250, 226, 80).setColor(Dyes.dyeYellow).setFuelType(MaterialBuilder.GAS).setFuelPower(232).setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 8)).addElectrolyzerRecipe().setCanBeCracked(true).constructMaterial();
    public static Materials Butane                    = new MaterialBuilder(644, TextureSet.SET_FLUID      ,                                                                                                     "Butane").addCell().addGas().setRGB(182, 55, 30).setColor(Dyes.dyeOrange).setFuelType(MaterialBuilder.GAS).setFuelPower(296).setMaterialList(new MaterialStack(Carbon, 4), new MaterialStack(Hydrogen, 10)).addElectrolyzerRecipe().setCanBeCracked(true).constructMaterial();
    public static Materials Butene                    = new MaterialBuilder(645, TextureSet.SET_FLUID      ,                                                                                                     "Butene").addCell().addGas().setRGB(207, 80, 5).setColor(Dyes.dyeOrange).setFuelType(MaterialBuilder.GAS).setFuelPower(256).setMaterialList(new MaterialStack(Carbon, 4), new MaterialStack(Hydrogen, 8)).addElectrolyzerRecipe().setCanBeCracked(true).constructMaterial();
    public static Materials Butadiene                 = new MaterialBuilder(646, TextureSet.SET_FLUID      ,                                                                                                     "Butadiene").addCell().addGas().setRGB(232, 105, 0).setColor(Dyes.dyeOrange).setFuelType(MaterialBuilder.GAS).setFuelPower(206).setMaterialList(new MaterialStack(Carbon, 4), new MaterialStack(Hydrogen, 6)).addElectrolyzerRecipe().setCanBeCracked(true).constructMaterial();
    public static Materials RawStyreneButadieneRubber = new MaterialBuilder(634, TextureSet.SET_SHINY      ,                                                                                                     "Raw Styrene-Butadiene Rubber").addDustItems().setRGB(84, 64, 61).setColor(Dyes.dyeGray).setMaterialList(new MaterialStack(Styrene, 1), new MaterialStack(Butadiene, 3)).constructMaterial();
    public static Materials StyreneButadieneRubber    = new MaterialBuilder(635, TextureSet.SET_SHINY      ,                                                                                                     "Styrene-Butadiene Rubber").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(3.0f).setDurability(128).setToolQuality(1).setRGB(33, 26, 24).setColor(Dyes.dyeBlack).setMaterialList(new MaterialStack(Styrene, 1), new MaterialStack(Butadiene, 3)).constructMaterial();
    public static Materials Toluene                   = new MaterialBuilder(647, TextureSet.SET_FLUID      ,                                                                                                     "Toluene").addCell().setRGB(80, 29, 5).setColor(Dyes.dyeBrown).setFuelType(MaterialBuilder.GAS).setFuelPower(328).setMaterialList(new MaterialStack(Carbon, 7), new MaterialStack(Hydrogen, 8)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Epichlorohydrin           = new MaterialBuilder(648, TextureSet.SET_FLUID      ,                                                                                                     "Epichlorohydrin").addCell().setRGB(80, 29, 5).setColor(Dyes.dyeBrown).setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 5), new MaterialStack(Chlorine, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials PolyvinylChloride         = new MaterialBuilder(649, TextureSet.SET_DULL       ,                                                                                                     "Polyvinyl Chloride").addDustItems().addMetalItems().addToolHeadItems().addGearItems().setToolSpeed(3.0f).setDurability(32).setToolQuality(1).setRGB(215, 230, 230).setColor(Dyes.dyeLightGray).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 3), new MaterialStack(Chlorine, 1)).constructMaterial();
    public static Materials VinylChloride             = new MaterialBuilder(650, TextureSet.SET_FLUID      ,                                                                                                     "Vinyl Chloride").addCell().addGas().setRGB(225, 240, 240).setColor(Dyes.dyeLightGray).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 3), new MaterialStack(Chlorine, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials SulfurDioxide             = new MaterialBuilder(651, TextureSet.SET_FLUID      ,                                                                                                     "Sulfur Dioxide").addCell().addGas().setRGB(200, 200, 25).setColor(Dyes.dyeYellow).setMaterialList(new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 2)).constructMaterial();
    public static Materials SulfurTrioxide            = new MaterialBuilder(652, TextureSet.SET_FLUID      ,                                                                                                     "Sulfur Trioxide").addCell().addGas().setGasTemperature(344).setRGB(160, 160, 20).setColor(Dyes.dyeYellow).setMaterialList(new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();
    public static Materials NitricAcid                = new MaterialBuilder(653, TextureSet.SET_FLUID      ,                                                                                                     "Nitric Acid").addCell().addFluid().setRGB(230, 226, 171).setMaterialList(new MaterialStack(Hydrogen, 1), new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Dimethylhydrazine         = new MaterialBuilder(654, TextureSet.SET_FLUID      ,                                                                                                     "1,1-Dimethylhydrazine").addCell().addFluid().setRGB(0, 0, 85).setColor(Dyes.dyeBlue).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 8), new MaterialStack(Nitrogen, 2)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Chloramine                = new MaterialBuilder(655, TextureSet.SET_FLUID      ,                                                                                                     "Chloramine").addCell().addFluid().setRGB(63, 159, 128).setColor(Dyes.dyeCyan).setMaterialList(new MaterialStack(Nitrogen, 1), new MaterialStack(Hydrogen, 2), new MaterialStack(Chlorine, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Dimethylamine             = new MaterialBuilder(656, TextureSet.SET_FLUID      ,                                                                                                     "Dimethylamine").addCell().addGas().setRGB(85, 68, 105).setColor(Dyes.dyeGray).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 7), new MaterialStack(Nitrogen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials DinitrogenTetroxide       = new MaterialBuilder(657, TextureSet.SET_FLUID      ,                                                                                                     "Dinitrogen Tetroxide").addCell().addGas().setRGB(0, 65, 132).setColor(Dyes.dyeBlue).setMaterialList(new MaterialStack(Nitrogen, 2), new MaterialStack(Oxygen, 4)).addElectrolyzerRecipe().constructMaterial();
    public static Materials NitricOxide               = new MaterialBuilder(658, TextureSet.SET_FLUID      ,                                                                                                     "Nitric Oxide").addCell().addGas().setRGB(125, 200, 240).setColor(Dyes.dyeCyan).setMaterialList(new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Ammonia                   = new MaterialBuilder(659, TextureSet.SET_FLUID      ,                                                                                                     "Ammonia").addCell().addGas().setRGB(63, 52, 128).setColor(Dyes.dyeBlue).setMaterialList(new MaterialStack(Nitrogen, 1), new MaterialStack(Hydrogen, 3)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Dimethyldichlorosilane    = new MaterialBuilder(663, TextureSet.SET_FLUID      ,                                                                                                     "Dimethyldichlorosilane").addCell().addFluid().setRGB(68, 22, 80).setColor(Dyes.dyePurple).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 6), new MaterialStack(Chlorine, 2), new MaterialStack(Silicon, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Chloromethane             = new MaterialBuilder(664, TextureSet.SET_FLUID      ,                                                                                                     "Chloromethane").addCell().addGas().setRGB(200, 44, 160).setColor(Dyes.dyeMagenta).setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(Hydrogen, 3), new MaterialStack(Chlorine, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials PhosphorousPentoxide      = new MaterialBuilder(665, TextureSet.SET_FLUID      ,                                                                                                     "Phosphorous Pentoxide").addCell().addDustItems().setRGB(220, 220, 0).setColor(Dyes.dyeYellow).setMaterialList(new MaterialStack(Phosphorus, 4), new MaterialStack(Oxygen, 10)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Tetrafluoroethylene       = new MaterialBuilder(666, TextureSet.SET_FLUID      ,                                                                                                     "Tetrafluoroethylene").addCell().addGas().setRGB(125, 125, 125).setColor(Dyes.dyeGray).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Fluorine, 4)).addElectrolyzerRecipe().constructMaterial();
    public static Materials HydrofluoricAcid          = new MaterialBuilder(667, TextureSet.SET_FLUID      ,                                                                                                     "Hydrofluoric Acid").setName("HydrofluoricAcid_GT5U").addCell().addFluid().setRGB(0, 136, 170).setColor(Dyes.dyeLightBlue).setMaterialList(new MaterialStack(Hydrogen, 1), new MaterialStack(Fluorine, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Chloroform                = new MaterialBuilder(668, TextureSet.SET_FLUID      ,                                                                                                     "Chloroform").addCell().addFluid().setRGB(137, 44, 160).setColor(Dyes.dyePurple).setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(Hydrogen, 1), new MaterialStack(Chlorine, 3)).addElectrolyzerRecipe().constructMaterial();
    public static Materials BisphenolA                = new MaterialBuilder(669, TextureSet.SET_FLUID      ,                                                                                                     "Bisphenol A").addCell().setRGB(212, 170, 0).setColor(Dyes.dyeBrown).setMaterialList(new MaterialStack(Carbon, 15), new MaterialStack(Hydrogen, 16), new MaterialStack(Oxygen, 2)).addElectrolyzerRecipe().constructMaterial();
    public static Materials AceticAcid                = new MaterialBuilder(670, TextureSet.SET_FLUID      ,                                                                                                     "Acetic Acid").addCell().addFluid().setRGB(200, 180, 160).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 4), new MaterialStack(Oxygen, 2)).addElectrolyzerRecipe().constructMaterial();
    public static Materials CalciumAcetateSolution    = new MaterialBuilder(671, TextureSet.SET_RUBY       ,                                                                                                     "Calcium Acetate Solution").addCell().addFluid().setRGB(220, 200, 180).setColor(Dyes.dyeCyan).setMaterialList(new MaterialStack(Calcium, 1), new MaterialStack(Carbon, 4), new MaterialStack(Oxygen, 4), new MaterialStack(Hydrogen, 6)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Acetone                   = new MaterialBuilder(672, TextureSet.SET_FLUID      ,                                                                                                     "Acetone").addCell().addFluid().setRGB(175, 175, 175).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Methanol                  = new MaterialBuilder(673, TextureSet.SET_FLUID      ,                                                                                                     "Methanol").addCell().addFluid().setRGB(170, 136, 0).setColor(Dyes.dyeBrown).setFuelPower(84).setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(Hydrogen, 4), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials CarbonMonoxide            = new MaterialBuilder(674, TextureSet.SET_FLUID      ,                                                                                                     "Carbon Monoxide").addCell().addGas().setRGB(14, 72, 128).setColor(Dyes.dyeBrown).setFuelType(MaterialBuilder.GAS).setFuelPower(24).setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 1)).constructMaterial();
    public static Materials MetalMixture              = new MaterialBuilder(676, TextureSet.SET_METALLIC   ,                                                                                                     "Metal Mixture").addDustItems().setRGB(80, 45, 22).setColor(Dyes.dyeBrown).constructMaterial();
    public static Materials Ethylene                  = new MaterialBuilder(677, TextureSet.SET_FLUID      ,                                                                                                     "Ethylene").addCell().addGas().setRGB(225, 225, 225).setColor(Dyes.dyeWhite).setFuelType(MaterialBuilder.GAS).setFuelPower(128).setMaterialList(new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 4)).addElectrolyzerRecipe().setCanBeCracked(true).constructMaterial();
    public static Materials Propene                   = new MaterialBuilder(678, TextureSet.SET_FLUID      ,                                                                                                     "Propene").addCell().addGas().setRGB(255, 221, 85).setColor(Dyes.dyeYellow).setFuelType(MaterialBuilder.GAS).setFuelPower(192).setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 6)).addElectrolyzerRecipe().setCanBeCracked(true).constructMaterial();
    public static Materials VinylAcetate              = new MaterialBuilder(679, TextureSet.SET_FLUID      ,                                                                                                     "Vinyl Acetate").addCell().addFluid().setRGB(255, 179, 128).setColor(Dyes.dyeOrange).setMaterialList(new MaterialStack(Carbon, 4), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 2)).addElectrolyzerRecipe().constructMaterial();
    public static Materials PolyvinylAcetate          = new MaterialBuilder(680, TextureSet.SET_FLUID      ,                                                                                                     "Polyvinyl Acetate").addCell().addFluid().setRGB(255, 153, 85).setColor(Dyes.dyeOrange).setMaterialList(new MaterialStack(Carbon, 4), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 2)).constructMaterial();
    public static Materials MethylAcetate             = new MaterialBuilder(681, TextureSet.SET_FLUID      ,                                                                                                     "Methyl Acetate").addCell().addFluid().setRGB(238, 198, 175).setColor(Dyes.dyeOrange).setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 2)).addElectrolyzerRecipe().constructMaterial();
    public static Materials AllylChloride             = new MaterialBuilder(682, TextureSet.SET_FLUID      ,                                                                                                     "Allyl Chloride").addCell().addFluid().setRGB(135, 222, 170).setColor(Dyes.dyeCyan).setMaterialList(new MaterialStack(Carbon, 3), new MaterialStack(Hydrogen, 5), new MaterialStack(Chlorine, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials HydrochloricAcid          = new MaterialBuilder(683, TextureSet.SET_FLUID      ,                                                                                                     "Hydrochloric Acid").setName("HydrochloricAcid_GT5U").addCell().addFluid().setRGB(183, 200, 196).setColor(Dyes.dyeLightGray).setMaterialList(new MaterialStack(Hydrogen, 1), new MaterialStack(Chlorine, 1)).constructMaterial();
    public static Materials HypochlorousAcid          = new MaterialBuilder(684, TextureSet.SET_FLUID      ,                                                                                                     "Hypochlorous Acid").addCell().addFluid().setRGB(111, 138, 145).setColor(Dyes.dyeGray).setMaterialList(new MaterialStack(Hydrogen, 1), new MaterialStack(Chlorine, 1), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials SodiumOxide               = new MaterialBuilder(744, TextureSet.SET_DULL       ,                                                                                                     "Sodium Oxide").setName("SodiumOxide").addDustItems().setRGB(255, 255, 235).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Sodium, 2), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials SodiumHydroxide           = new MaterialBuilder(685, TextureSet.SET_DULL       ,                                                                                                     "Sodium Hydroxide").setName("SodiumHydroxide_GT5U").addDustItems().setRGB(0, 51, 128).setColor(Dyes.dyeBlue).setMaterialList(new MaterialStack(Sodium, 1), new MaterialStack(Oxygen, 1), new MaterialStack(Hydrogen, 1)).constructMaterial();
    public static Materials Benzene                   = new MaterialBuilder(686, TextureSet.SET_FLUID      ,                                                                                                     "Benzene").addCell().addFluid().setRGB(26, 26, 26).setColor(Dyes.dyeGray).setFuelType(MaterialBuilder.GAS).setFuelPower(360).setMaterialList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 6)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Phenol                    = new MaterialBuilder(687, TextureSet.SET_FLUID      ,                                                                                                     "Phenol").addCell().addFluid().setRGB(120, 68, 33).setColor(Dyes.dyeBrown).setFuelType(MaterialBuilder.GAS).setFuelPower(288).setMaterialList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Cumene                    = new MaterialBuilder(688, TextureSet.SET_FLUID      ,                                                                                                     "Isopropylbenzene").addCell().addFluid().setRGB(85, 34, 0).setColor(Dyes.dyeBrown).setMaterialList(new MaterialStack(Carbon, 9), new MaterialStack(Hydrogen, 12)).addElectrolyzerRecipe().constructMaterial();
    public static Materials PhosphoricAcid            = new MaterialBuilder(689, TextureSet.SET_FLUID      ,                                                                                                     "Phosphoric Acid").setName("PhosphoricAcid_GT5U").addCell().addFluid().setRGB(220, 220, 0).setColor(Dyes.dyeYellow).setMaterialList(new MaterialStack(Hydrogen, 3), new MaterialStack(Phosphorus, 1), new MaterialStack(Oxygen, 4)).constructMaterial();
    public static Materials SaltWater                 = new MaterialBuilder(692, TextureSet.SET_FLUID      ,                                                                                                     "Salt Water").addCell().addFluid().setRGB(0, 0, 200).setColor(Dyes.dyeBlue).constructMaterial();
    public static Materials IronIIIChloride           = new MaterialBuilder(693, TextureSet.SET_FLUID      ,                                                                                                     "Iron III Chloride").setName("IronIIIChloride").addCell().addFluid().setRGB(22, 21, 14).setColor(Dyes.dyeBlack).setMaterialList(new MaterialStack(Chlorine, 3), new MaterialStack(Iron, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials LifeEssence               = new MaterialBuilder(694, TextureSet.SET_FLUID      ,                                                                                                     "Life").setName("lifeessence").addCell().addFluid().setFuelPower(100).setFuelType(5).setRGB(110, 3, 3).setColor(Dyes.dyeRed).setMaterialList().constructMaterial();

    //Roasted Ore Dust
    public static Materials RoastedCopper             = new MaterialBuilder(546, TextureSet.SET_DULL    , "Roasted Copper").setName("RoastedCopper").addDustItems().setRGB(77, 18, 18).constructMaterial();
    public static Materials RoastedAntimony           = new MaterialBuilder(547, TextureSet.SET_DULL    , "Roasted Antimony").setName("RoastedAntimony").addDustItems().setRGB(196, 178, 194).constructMaterial();
    public static Materials RoastedIron               = new MaterialBuilder(548, TextureSet.SET_DULL    , "Roasted Iron").setName("RoastedIron").addDustItems().setRGB(148, 98, 98).addOreItems().constructMaterial();
    public static Materials RoastedNickel             = new MaterialBuilder(549, TextureSet.SET_METALLIC, "Roasted Nickel").setName("RoastedNickel").addDustItems().setRGB(70, 140, 45).addOreItems().constructMaterial();
    public static Materials RoastedZinc               = new MaterialBuilder(550, TextureSet.SET_DULL    , "Roasted Zinc").setName("RoastedZinc").addDustItems().setRGB(209, 209, 209).constructMaterial();
    public static Materials RoastedCobalt             = new MaterialBuilder(551, TextureSet.SET_METALLIC, "Roasted Cobalt").setName("RoastedCobalt").addDustItems().setRGB(8, 64, 9).constructMaterial();
    public static Materials RoastedArsenic            = new MaterialBuilder(552, TextureSet.SET_SHINY   , "Roasted Arsenic").setName("RoastedArsenic").addDustItems().setRGB(240, 240, 240).constructMaterial();
    public static Materials RoastedLead               = new MaterialBuilder(553, TextureSet.SET_SHINY   , "Roasted Lead").setName("RoastedLead").addDustItems().setRGB(168, 149, 43).constructMaterial();

    //Silicon Line
    public static Materials SiliconSG               = new Materials(  856, TextureSet.SET_METALLIC         ,   1.0F,      0,  2, 1|2  |8   |32             ,  80,  80, 100,   0,   "SiliconSolarGrade"  ,   "Silicon Solar Grade (Poly SI)"      ,    0,       0,         2273,   2273, true, false,   1,   1,   1, Dyes.dyeBlack       , Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 4), new TC_AspectStack(TC_Aspects.TENEBRAE, 2))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials CalciumDisilicide       = new Materials(  971, TextureSet.SET_METALLIC         ,   1.0F,      0,  2, 1    |8                   , 180, 180, 180,   0,   "CalciumDisilicide"       ,   "Calcium Disilicide"            ,    0,       0,       1313,   -1, false, false,   1,   1,   1, Dyes.dyeGray        ,1         , Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Silicon, 2)), Arrays.asList(new TC_AspectStack(TC_Aspects.TERRA, 1), new TC_AspectStack(TC_Aspects.ORDO, 1)));//CaSi2
    public static Materials SiliconTetrafluoride    = new MaterialBuilder(  967, TextureSet.SET_FLUID            , "Silicon Tetrafluoride" ).setName("SiliconTetrafluoride").addCell().addGas().setTransparent(true).setRGB(200, 200, 200).setColor(Dyes.dyeWhite).setMeltingPoint(178).setMaterialList(new MaterialStack(Silicon, 1), new MaterialStack(Fluorine, 4)).setAspects(Arrays.asList(new TC_AspectStack(TC_Aspects.AQUA, 1), new TC_AspectStack(TC_Aspects.VENENUM, 1))).constructMaterial();//SIF4
    public static Materials SiliconTetrachloride    = new MaterialBuilder(  968, TextureSet.SET_FLUID            ,  "Silicon Tetrachloride").setName("SiliconTetrachloride").addCell().addFluid().setRGB(220, 220, 220).setColor(Dyes.dyeWhite).setMeltingPoint(204).setMaterialList(new MaterialStack(Silicon, 1), new MaterialStack(Chlorine, 4)).setAspects(Arrays.asList(new TC_AspectStack(TC_Aspects.AQUA, 1), new TC_AspectStack(TC_Aspects.VENENUM, 1))).constructMaterial();//SICL4
    public static Materials Trichlorosilane         = new MaterialBuilder(  972, TextureSet.SET_FLUID            ,   "Trichlorosilane" ).setName("Trichlorosilane" ).addCell().addFluid().setRGB( 255, 255, 255).setColor(Dyes.dyeWhite).setMeltingPoint(139).setMaterialList(new MaterialStack(Hydrogen, 1), new MaterialStack(Silicon, 1), new MaterialStack(Chlorine, 3)).setAspects(Arrays.asList(new TC_AspectStack(TC_Aspects.AQUA, 1), new TC_AspectStack(TC_Aspects.VENENUM, 1))).constructMaterial();//HSICL3
    public static Materials Hexachlorodisilane      = new MaterialBuilder(  973, TextureSet.SET_FLUID            ,  "Hexachlorodisilane").setName("Hexachlorodisilane" ).addCell().addFluid().setRGB( 255, 255, 255).setColor(Dyes.dyeWhite).setMeltingPoint(272).setExtraData(1).setMaterialList(new MaterialStack(Silicon, 2), new MaterialStack(Chlorine, 6)).setAspects(Collections.singletonList(new TC_AspectStack(TC_Aspects.AQUA, 1))).constructMaterial();//SI2CL6
    public static Materials Dichlorosilane          = new MaterialBuilder(  799, TextureSet.SET_FLUID            ,   "Dichlorosilane").setName("Dichlorosilane").addCell().addGas().setTransparent(true).setRGB( 255, 255, 255).setColor(Dyes.dyeWhite).setMeltingPoint(151).setExtraData(1).setMaterialList(new MaterialStack(Silicon, 1), new MaterialStack(Hydrogen, 2), new MaterialStack(Chlorine, 2)).setAspects(Arrays.asList(new TC_AspectStack(TC_Aspects.AQUA, 1), new TC_AspectStack(TC_Aspects.VENENUM, 1))).constructMaterial();//SIH2CL2
    public static Materials Silane                  = new MaterialBuilder(  798, TextureSet.SET_FLUID            ,   "Silane").setName( "Silane").addCell().addGas().setRGB( 255, 255, 255).setColor(Dyes.dyeWhite).setMeltingPoint(88).setMaterialList(new MaterialStack(Silicon, 1), new MaterialStack(Hydrogen, 4)).setAspects(Collections.singletonList(new TC_AspectStack(TC_Aspects.AQUA, 1))).constructMaterial();//SIH4
    public static Materials Calciumhydride          = new Materials(  797, TextureSet.SET_METALLIC         ,   1.0F,      0,  2, 1    |8                   , 220, 220, 220,   0,   "CalciumHydride"       ,   "Calcium Hydride"            ,    0,       0,       1089,   -1, false, false,   1,   1,   1, Dyes.dyeGray        ,1         , Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Hydrogen, 2)), Arrays.asList(new TC_AspectStack(TC_Aspects.TERRA, 1), new TC_AspectStack(TC_Aspects.ORDO, 1)));//CaH2
    public static Materials AluminiumFluoride       = new Materials(  969, TextureSet.SET_METALLIC         ,   1.0F,      0,  2, 1    |8                   , 255, 255, 255,   0,   "Aluminiumfluoride"        ,   "Aluminium Fluoride"             ,    0,       0,       1533,   -1, false, false,   1,   1,   1, Dyes.dyeWhite       ,1         , Arrays.asList(new MaterialStack(Aluminium, 1), new MaterialStack(Fluorine, 3)), Arrays.asList(new TC_AspectStack(TC_Aspects.TERRA, 1), new TC_AspectStack(TC_Aspects.ORDO, 1)));//ALF3

    public static Materials SolderingAlloy          = new Materials( 314, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1|2                       , 220, 220, 230,   0,   "SolderingAlloy"          ,   "Soldering Alloy"               ,    0,       0,        400,  400, false, false,   1,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Tin, 9), new MaterialStack(Antimony, 1)));
    public static Materials GalliumArsenide         = new Materials( 980, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1|2                       , 160, 160, 160,   0,   "GalliumArsenide"         ,   "Gallium Arsenide"              ,    0,       0,         -1, 1200,  true, false,   1,   1,   1, Dyes.dyeGray        , 2, Arrays.asList(new MaterialStack(Arsenic, 1), new MaterialStack(Gallium, 1)));
    public static Materials IndiumGalliumPhosphide  = new Materials( 981, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1|2                       , 160, 140, 190,   0,   "IndiumGalliumPhosphide"  ,   "Indium Gallium Phosphide"      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , 2, Arrays.asList(new MaterialStack(Indium, 1), new MaterialStack(Gallium, 1), new MaterialStack(Phosphorus, 1)));
    public static Materials Spessartine             = new Materials( 838, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 255, 100, 100,   0,   "Spessartine"             ,   "Spessartine"                   ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeRed         , 0, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Manganese, 3), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12)));
    public static Materials Sphalerite              = new Materials( 839, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   , 255, 255, 255,   0,   "Sphalerite"              ,   "Sphalerite"                    ,    0,       0,         -1,    0, false, false,   2,   1,   1, Dyes.dyeYellow      , 1, Arrays.asList(new MaterialStack(Zinc, 1), new MaterialStack(Sulfur, 1)));
    public static Materials StainlessSteel          = new Materials( 306, TextureSet.SET_SHINY             ,   7.0F,    480,  4, 1|2          |64|128      , 200, 200, 220,   0,   "StainlessSteel"          ,   "Stainless Steel"               ,    0,       0,         -1, 1700,  true, false,   1,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Iron, 6), new MaterialStack(Chrome, 1), new MaterialStack(Manganese, 1), new MaterialStack(Nickel, 1)));
    public static Materials Steel                   = new Materials( 305, TextureSet.SET_METALLIC          ,   6.0F,    512,  3, 1|2          |64|128      , 128, 128, 128,   0,   "Steel"                   ,   "Steel"                         ,    0,       0,       1811, 1000,  true, false,   4,  51,  50, Dyes.dyeGray        , 1, Arrays.asList(new MaterialStack(Iron, 50), new MaterialStack(Carbon, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.ORDO, 1)));
    public static Materials Stibnite                = new Materials( 945, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   ,  70,  70,  70,   0,   "Stibnite"                ,   "Stibnite"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Antimony, 2), new MaterialStack(Sulfur, 3)));
    public static Materials SulfuricAcid            = new Materials( 720, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16                , 255, 128,   0,   0,   "SulfuricAcid"            ,   "Sulfuric Acid"                 ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 1, Arrays.asList(new MaterialStack(Hydrogen, 2), new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 4)));
    public static Materials Tanzanite               = new Materials( 508, TextureSet.SET_GEM_VERTICAL      ,   7.0F,    256,  2, 1  |4|8      |64          ,  64,   0, 200, 127,   "Tanzanite"               ,   "Tanzanite"                     ,    0,       0,         -1,    0, false,  true,   5,   1,   1, Dyes.dyePurple      , 0, Arrays.asList(new MaterialStack(Calcium, 2), new MaterialStack(Aluminium, 3), new MaterialStack(Silicon, 3), new MaterialStack(Hydrogen, 1), new MaterialStack(Oxygen, 13)), Arrays.asList(new TC_AspectStack(TC_Aspects.LUCRUM, 5), new TC_AspectStack(TC_Aspects.VITREUS, 3)));
    public static Materials Tetrahedrite            = new Materials( 840, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 200,  32,   0,   0,   "Tetrahedrite"            ,   "Tetrahedrite"                  ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(Copper, 3), new MaterialStack(Antimony, 1), new MaterialStack(Sulfur, 3), new MaterialStack(Iron, 1))); //Cu3SbS3 + x(Fe,Zn)6Sb2S9
    public static Materials TinAlloy                = new Materials( 363, TextureSet.SET_METALLIC          ,   6.5F,     96,  2, 1|2          |64|128      , 200, 200, 200,   0,   "TinAlloy"                ,   "Tin Alloy"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Tin, 1), new MaterialStack(Iron, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1)));
    public static Materials Topaz                   = new Materials( 507, TextureSet.SET_GEM_HORIZONTAL    ,   7.0F,    256,  3, 1  |4|8      |64          , 255, 128,   0, 127,   "Topaz"                   ,   "Topaz"                         ,    0,       0,         -1,    0, false,  true,   5,   1,   1, Dyes.dyeOrange      , 0, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 1), new MaterialStack(Fluorine, 2), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 6)), Arrays.asList(new TC_AspectStack(TC_Aspects.LUCRUM, 6), new TC_AspectStack(TC_Aspects.VITREUS, 4)));
    public static Materials Tungstate               = new Materials( 841, TextureSet.SET_DULL              ,   1.0F,      0,  3, 1    |8                   ,  55,  50,  35,   0,   "Tungstate"               ,   "Tungstate"                     ,    0,       0,       2500, 2500,  true, false,   4,   1,   1, Dyes.dyeBlack       , 0, Arrays.asList(new MaterialStack(Tungsten, 1), new MaterialStack(Lithium, 2), new MaterialStack(Oxygen, 4)));
    public static Materials Ultimet                 = new Materials( 344, TextureSet.SET_SHINY             ,   9.0F,   2048,  4, 1|2          |64|128      , 180, 180, 230,   0,   "Ultimet"                 ,   "Ultimet"                       ,    0,       0,       2700, 2700,  true, false,   1,   1,   1, Dyes.dyeLightBlue   , 1, Arrays.asList(new MaterialStack(Cobalt, 5), new MaterialStack(Chrome, 2), new MaterialStack(Nickel, 1), new MaterialStack(Molybdenum, 1))); // 54% Cobalt, 26% Chromium, 9% Nickel, 5% Molybdenum, 3% Iron, 2% Tungsten, 0.8% Manganese, 0.3% Silicon, 0.08% Nitrogen and 0.06% Carbon
    public static Materials Uraninite               = new Materials( 922, TextureSet.SET_METALLIC          ,   1.0F,      0,  3, 1    |8                   ,  35,  35,  35,   0,   "Uraninite"               ,   "Uraninite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLime        , 2, Arrays.asList(new MaterialStack(Uranium, 1), new MaterialStack(Oxygen, 2)));
    public static Materials Uvarovite               = new Materials( 842, TextureSet.SET_DIAMOND           ,   1.0F,      0,  2, 1    |8                   , 180, 255, 180,   0,   "Uvarovite"               ,   "Uvarovite"                     ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeGreen       , 1, Arrays.asList(new MaterialStack(Calcium, 3), new MaterialStack(Chrome, 2), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 12)));
    public static Materials VanadiumGallium         = new Materials( 357, TextureSet.SET_SHINY             ,   1.0F,      0,  2, 1|2             |128      , 128, 128, 140,   0,   "VanadiumGallium"         ,   "Vanadium-Gallium"              ,    0,       0,       4500, 4500,  true, false,   1,   1,   1, Dyes.dyeGray        , 2, Arrays.asList(new MaterialStack(Vanadium, 3), new MaterialStack(Gallium, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Wood                    = new Materials( 809, TextureSet.SET_WOOD              ,   2.0F,     16,  0, 1|2          |64|128      , 100,  50,   0,   0,   "Wood"                    ,   "Wood"                          ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       , 0, Arrays.asList(new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 1), new MaterialStack(Hydrogen, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.ARBOR, 2)));
    public static Materials WroughtIron             = new Materials( 304, TextureSet.SET_METALLIC          ,   6.0F,    384,  2, 1|2          |64|128      , 200, 180, 180,   0,   "WroughtIron"             ,   "Wrought Iron"                  ,    0,       0,       1811,    0, false, false,   3,   1,   1, Dyes.dyeLightGray   , 2, Collections.singletonList(new MaterialStack(Iron, 1)));
    public static Materials Wulfenite               = new Materials( 882, TextureSet.SET_DULL              ,   1.0F,      0,  3, 1    |8                   , 255, 128,   0,   0,   "Wulfenite"               ,   "Wulfenite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 2, Arrays.asList(new MaterialStack(Lead, 1), new MaterialStack(Molybdenum, 1), new MaterialStack(Oxygen, 4)));
    public static Materials YellowLimonite          = new Materials( 931, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   , 200, 200,   0,   0,   "YellowLimonite"          ,   "Yellow Limonite"               ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Hydrogen, 1), new MaterialStack(Oxygen, 2))); // FeO(OH) + a bit of Ni and Co
    public static Materials YttriumBariumCuprate    = new Materials( 358, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1|2                       ,  80,  64,  70,   0,   "YttriumBariumCuprate"    ,   "Yttrium Barium Cuprate"        ,    0,       0,       4500, 4500,  true, false,   1,   1,   1, Dyes.dyeGray        , 0, Arrays.asList(new MaterialStack(Yttrium, 1), new MaterialStack(Barium, 2), new MaterialStack(Copper, 3), new MaterialStack(Oxygen, 7))).disableAutoGeneratedVacuumFreezerRecipe();

    /**
     * Second Degree Compounds
     */
    public static Materials WoodSealed              = new Materials( 889, TextureSet.SET_WOOD              ,   3.0F,     24,  0, 1|2          |64|128      ,  80,  40,   0,   0,   "WoodSealed"              ,   "Sealed Wood"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBrown       , 0, Collections.singletonList(new MaterialStack(Wood, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.ARBOR, 2), new TC_AspectStack(TC_Aspects.FABRICO, 1)));
    public static Materials LiveRoot                = new Materials( 832, TextureSet.SET_WOOD              ,   1.0F,      0,  1, 1                         , 220, 200,   0,   0,   "LiveRoot"                ,   "Liveroot"                      ,    5,      16,         -1,    0, false, false,   2,   4,   3, Dyes.dyeBrown       , 2, Arrays.asList(new MaterialStack(Wood, 3), new MaterialStack(Magic, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.ARBOR, 2), new TC_AspectStack(TC_Aspects.VICTUS, 2), new TC_AspectStack(TC_Aspects.PRAECANTATIO, 1)));
    public static Materials IronWood                = new Materials( 338, TextureSet.SET_WOOD              ,   6.0F,    384,  2, 1|2          |64|128      , 150, 140, 110,   0,   "IronWood"                ,   "Ironwood"                      ,    5,       8,         -1,    0, false, false,   2,  19,  18, Dyes.dyeBrown       , 2, Arrays.asList(new MaterialStack(Iron, 9), new MaterialStack(LiveRoot, 9), new MaterialStack(Gold, 1)));
    public static Materials Glass                   = new Materials( 890, TextureSet.SET_GLASS             ,   1.0F,      4,  0, 1  |4                     , 250, 250, 250, 220,   "Glass"                   ,   "Glass"                         ,    0,       0,       1500,    0, false,  true,   1,   1,   1, Dyes.dyeWhite       , 2, Collections.singletonList(new MaterialStack(SiliconDioxide, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.VITREUS, 2)));
    public static Materials BorosilicateGlass  = new MaterialBuilder(611, TextureSet.SET_GLASS             ,                                                                                                     "Borosilicate Glass").addDustItems().addMetalItems().setRGB(230, 243, 230).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Boron, 1), new MaterialStack(Glass, 7)).addCentrifugeRecipe().constructMaterial();
    public static Materials Perlite                 = new Materials( 925, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   ,  30,  20,  30,   0,   "Perlite"                 ,   "Perlite"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Obsidian, 2), new MaterialStack(Water, 1)));
    public static Materials Borax                   = new Materials( 941, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1    |8                   , 250, 250, 250,   0,   "Borax"                   ,   "Borax"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Sodium, 2), new MaterialStack(Boron, 4), new MaterialStack(Oxygen, 7), new MaterialStack(Water, 10)));
    public static Materials Lignite                 = new Materials( 538, TextureSet.SET_LIGNITE           ,   1.0F,      0,  0, 1  |4|8                   , 100,  70,  70,   0,   "Lignite"                 ,   "Lignite Coal"                  ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 1, Arrays.asList(new MaterialStack(Carbon, 3), new MaterialStack(Water, 1)));
    public static Materials Olivine                 = new Materials( 505, TextureSet.SET_RUBY              ,   7.0F,    256,  2, 1  |4|8      |64          , 150, 255, 150, 127,   "Olivine"                 ,   "Olivine"                       ,    0,       0,         -1,    0, false,  true,   5,   1,   1, Dyes.dyeLime        , 1, Arrays.asList(new MaterialStack(Magnesium, 2), new MaterialStack(Iron, 1), new MaterialStack(SiliconDioxide, 2)), Arrays.asList(new TC_AspectStack(TC_Aspects.LUCRUM, 4), new TC_AspectStack(TC_Aspects.VITREUS, 2)));
    public static Materials Opal                    = new Materials( 510, TextureSet.SET_OPAL              ,   7.0F,    256,  2, 1  |4|8      |64          ,   0,   0, 255,   0,   "Opal"                    ,   "Opal"                          ,    0,       0,         -1,    0, false,  true,   3,   1,   1, Dyes.dyeBlue        , 1, Collections.singletonList(new MaterialStack(SiliconDioxide, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.LUCRUM, 5), new TC_AspectStack(TC_Aspects.VITREUS, 3)));
    public static Materials Amethyst                = new Materials( 509, TextureSet.SET_FLINT             ,   7.0F,    256,  3, 1  |4|8      |64          , 210,  50, 210, 127,   "Amethyst"                ,   "Amethyst"                      ,    0,       0,         -1,    0, false,  true,   3,   1,   1, Dyes.dyePink        , 1, Arrays.asList(new MaterialStack(SiliconDioxide, 4), new MaterialStack(Iron, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.LUCRUM, 6), new TC_AspectStack(TC_Aspects.VITREUS, 4)));
    public static Materials Redstone                = new Materials( 810, TextureSet.SET_ROUGH             ,   1.0F,      0,  2, 1    |8                   , 200,   0,   0,   0,   "Redstone"                ,   "Redstone"                      ,    0,       0,        500,    0, false, false,   3,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(Silicon, 1), new MaterialStack(Pyrite, 5), new MaterialStack(Ruby, 1), new MaterialStack(Mercury, 3)), Arrays.asList(new TC_AspectStack(TC_Aspects.MACHINA, 1), new TC_AspectStack(TC_Aspects.POTENTIA, 2)));
    public static Materials Lapis                   = new Materials( 526, TextureSet.SET_LAPIS             ,   1.0F,      0,  1, 1  |4|8                   ,  70,  70, 220,   0,   "Lapis"                   ,   "Lapis"                         ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeBlue        , 2, Arrays.asList(new MaterialStack(Lazurite, 12), new MaterialStack(Sodalite, 2), new MaterialStack(Pyrite, 1), new MaterialStack(Calcite, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.SENSUS, 1)));
    public static Materials Blaze                   = new Materials( 801, TextureSet.SET_POWDER            ,   2.0F,     16,  1, 1            |64          , 255, 200,   0,   0,   "Blaze"                   ,   "Blaze"                         ,    0,       0,       6400,    0, false, false,   2,   3,   2, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(DarkAsh, 1), new MaterialStack(Sulfur, 1), new MaterialStack(Magic, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.PRAECANTATIO, 2), new TC_AspectStack(TC_Aspects.IGNIS, 4)));
    public static Materials EnderPearl              = new Materials( 532, TextureSet.SET_SHINY             ,   1.0F,     16,  1, 1  |4                     , 108, 220, 200,   0,   "EnderPearl"              ,   "Enderpearl"                    ,    0,       0,         -1,    0, false, false,   1,  16,  10, Dyes.dyeGreen       , 1, Arrays.asList(new MaterialStack(Beryllium, 1), new MaterialStack(Potassium, 4), new MaterialStack(Nitrogen, 5), new MaterialStack(Magic, 6)), Arrays.asList(new TC_AspectStack(TC_Aspects.ALIENIS, 4), new TC_AspectStack(TC_Aspects.ITER, 4), new TC_AspectStack(TC_Aspects.PRAECANTATIO, 2)));
    public static Materials EnderEye                = new Materials( 533, TextureSet.SET_SHINY             ,   1.0F,     16,  1, 1  |4                     , 160, 250, 230,   0,   "EnderEye"                ,   "Endereye"                      ,    5,      10,         -1,    0, false, false,   1,   2,   1, Dyes.dyeGreen       , 2, Arrays.asList(new MaterialStack(EnderPearl, 1), new MaterialStack(Blaze, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.SENSUS, 4), new TC_AspectStack(TC_Aspects.ALIENIS, 4), new TC_AspectStack(TC_Aspects.ITER, 4), new TC_AspectStack(TC_Aspects.PRAECANTATIO, 3), new TC_AspectStack(TC_Aspects.IGNIS, 2)));
    public static Materials Flint                   = new Materials( 802, TextureSet.SET_FLINT             ,   2.5F,    128,  1, 1            |64          ,   0,  32,  64,   0,   "Flint"                   ,   "Flint"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeGray        , 2, Collections.singletonList(new MaterialStack(SiliconDioxide, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.TERRA, 1), new TC_AspectStack(TC_Aspects.INSTRUMENTUM, 1)));
    public static Materials Diatomite               = new Materials( 948, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   , 225, 225, 225,   0,   "Diatomite"               ,   "Diatomite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeGray        , 2, Arrays.asList(new MaterialStack(Flint, 8), new MaterialStack(BandedIron, 1), new MaterialStack(Sapphire, 1)));
    public static Materials VolcanicAsh             = new Materials( 940, TextureSet.SET_FLINT             ,   1.0F,      0,  0, 1                         ,  60,  50,  50,   0,   "VolcanicAsh"             ,   "Volcanic Ashes"                ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Flint, 6), new MaterialStack(Iron, 1), new MaterialStack(Magnesium, 1)));
    public static Materials Niter                   = new Materials( 531, TextureSet.SET_FLINT             ,   1.0F,      0,  1, 1  |4|8                   , 255, 200, 200,   0,   "Niter"                   ,   "Niter"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyePink        , 2, Collections.singletonList(new MaterialStack(Saltpeter, 1)));
    public static Materials Pyrotheum               = new Materials( 843, TextureSet.SET_FIERY             ,   1.0F,      0,  1, 1                         , 255, 128,   0,   0,   "Pyrotheum"               ,   "Pyrotheum"                     ,    2,      62,         -1,    0, false, false,   2,   3,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Coal, 1), new MaterialStack(Redstone, 1), new MaterialStack(Blaze, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.PRAECANTATIO, 2), new TC_AspectStack(TC_Aspects.IGNIS, 1)));
    public static Materials Cryotheum               = new Materials( 898, TextureSet.SET_SHINY             ,   1.0F,      0,  1, 1                         ,   0, 148, 203,   0,   "Cryotheum"               ,   "Cryotheum"                     ,    2,      62,         -1,    0, false, false,   2,   3,   1, Dyes.dyeLightBlue   , 2, Arrays.asList(new MaterialStack(Saltpeter, 1), new MaterialStack(Redstone, 1), new MaterialStack(Snow, 1), new MaterialStack(Blizz, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.PRAECANTATIO, 2), new TC_AspectStack(TC_Aspects.ELECTRUM, 1),  new TC_AspectStack(TC_Aspects.GELUM, 1)));
    public static Materials HydratedCoal            = new Materials( 818, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1                         ,  70,  70, 100,   0,   "HydratedCoal"            ,   "Hydrated Coal"                 ,    0,       0,         -1,    0, false, false,   1,   9,   8, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Coal, 8), new MaterialStack(Water, 1)));
    public static Materials Apatite                 = new Materials( 530, TextureSet.SET_DIAMOND           ,   1.0F,      0,  1, 1  |4|8                   , 200, 200, 255,   0,   "Apatite"                 ,   "Apatite"                       ,    0,       0,         -1,    0, false, false,   2,   1,   1, Dyes.dyeCyan        , 1, Arrays.asList(new MaterialStack(Calcium, 5), new MaterialStack(Phosphate, 3), new MaterialStack(Chlorine, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.MESSIS, 2)));
    public static Materials Alumite                 = new Materials( 400, TextureSet.SET_METALLIC          ,   5.0F,    768,  2, 1|2          |64|128      , 255, 105, 180,   0,   "Alumite"                 ,   "Alumite"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyePink        , 2, Arrays.asList(new MaterialStack(Aluminium, 5), new MaterialStack(Steel, 2), new MaterialStack(Obsidian, 2)), Collections.singletonList(new TC_AspectStack(TC_Aspects.STRONTIO, 2)));
    public static Materials Manyullyn               = new Materials( 386, TextureSet.SET_SHINY             ,  25.0F,   2048,  5, 1|2  |8      |64|128      , 154,  76, 185,   0,   "Manyullyn"               ,   "Manyullyn"                     ,    0,       0,       3600, 3600,  true, false,   1,   1,   1, Dyes.dyePurple      , 2, Arrays.asList(new MaterialStack(Cobalt, 1), new MaterialStack(Ardite, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.STRONTIO, 2))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials Steeleaf                = new Materials( 339, TextureSet.SET_LEAF              ,   8.0F,    768,  3, 1|2          |64|128      ,  50, 127,  50,   0,   "Steeleaf"                ,   "Steeleaf"                      ,    5,      24,         -1,    0, false, false,   4,   1,   1, Dyes.dyeGreen       , 2, Arrays.asList(new MaterialStack(Steel, 1), new MaterialStack(Magic, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.HERBA, 2), new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.PRAECANTATIO, 1)));
    public static Materials Knightmetal             = new Materials( 362, TextureSet.SET_METALLIC          ,   8.0F,   1024,  3, 1|2          |64|128      , 210, 240, 200,   0,   "Knightmetal"             ,   "Knightmetal"                   ,    5,      24,         -1,    0, false, false,   4,   1,   1, Dyes.dyeLime        , 2, Arrays.asList(new MaterialStack(Steel, 2), new MaterialStack(Magic, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.LUCRUM, 1), new TC_AspectStack(TC_Aspects.METALLUM, 2)));
    public static Materials SterlingSilver          = new Materials( 350, TextureSet.SET_SHINY             ,  13.0F,    128,  2, 1|2          |64|128      , 250, 220, 225,   0,   "SterlingSilver"          ,   "Sterling Silver"               ,    0,       0,         -1, 1700,  true, false,   4,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Copper, 1), new MaterialStack(Silver, 4)));
    public static Materials RoseGold                = new Materials( 351, TextureSet.SET_SHINY             ,  14.0F,    128,  2, 1|2          |64|128      , 255, 230,  30,   0,   "RoseGold"                ,   "Rose Gold"                     ,    0,       0,         -1, 1600,  true, false,   4,   1,   1, Dyes.dyeOrange      , 2, Arrays.asList(new MaterialStack(Copper, 1), new MaterialStack(Gold, 4)));
    public static Materials BlackBronze             = new Materials( 352, TextureSet.SET_DULL              ,  12.0F,    256,  2, 1|2          |64|128      , 100,  50, 125,   0,   "BlackBronze"             ,   "Black Bronze"                  ,    0,       0,         -1, 2000,  true, false,   4,   1,   1, Dyes.dyePurple      , 2, Arrays.asList(new MaterialStack(Gold, 1), new MaterialStack(Silver, 1), new MaterialStack(Copper, 3)));
    public static Materials BismuthBronze           = new Materials( 353, TextureSet.SET_DULL              ,   8.0F,    256,  2, 1|2          |64|128      , 100, 125, 125,   0,   "BismuthBronze"           ,   "Bismuth Bronze"                ,    0,       0,         -1, 1100,  true, false,   4,   1,   1, Dyes.dyeCyan        , 2, Arrays.asList(new MaterialStack(Bismuth, 1), new MaterialStack(Zinc, 1), new MaterialStack(Copper, 3)));
    public static Materials BlackSteel              = new Materials( 334, TextureSet.SET_METALLIC          ,   6.5F,    768,  3, 1|2          |64|128      , 100, 100, 100,   0,   "BlackSteel"              ,   "Black Steel"                   ,    0,       0,         -1, 1200,  true, false,   4,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Nickel, 1), new MaterialStack(BlackBronze, 1), new MaterialStack(Steel, 3))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials RedSteel                = new Materials( 348, TextureSet.SET_METALLIC          ,   7.0F,    896,  4, 1|2          |64|128      , 140, 100, 100,   0,   "RedSteel"                ,   "Red Steel"                     ,    0,       0,         -1, 1300,  true, false,   4,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(SterlingSilver, 1), new MaterialStack(BismuthBronze, 1), new MaterialStack(Steel, 2), new MaterialStack(BlackSteel, 4)));
    public static Materials BlueSteel               = new Materials( 349, TextureSet.SET_METALLIC          ,   7.5F,   1024,  4, 1|2          |64|128      , 100, 100, 140,   0,   "BlueSteel"               ,   "Blue Steel"                    ,    0,       0,         -1, 1400,  true, false,   4,   1,   1, Dyes.dyeBlue        , 2, Arrays.asList(new MaterialStack(RoseGold, 1), new MaterialStack(Brass, 1), new MaterialStack(Steel, 2), new MaterialStack(BlackSteel, 4)));
    public static Materials DamascusSteel           = new Materials( 335, TextureSet.SET_METALLIC          ,   8.0F,   1280,  3, 1|2          |64|128      , 110, 110, 110,   0,   "DamascusSteel"           ,   "Damascus Steel"                ,    0,       0,       2000, 1500,  true, false,   4,   1,   1, Dyes.dyeGray        , 2, Collections.singletonList(new MaterialStack(Steel, 1))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials TungstenSteel           = new Materials( 316, TextureSet.SET_METALLIC          ,   8.0F,   2560,  4, 1|2          |64|128      , 100, 100, 160,   0,   "TungstenSteel"           ,   "Tungstensteel"                 ,    0,       0,       4000, 4000,  true, false,   4,   1,   1, Dyes.dyeBlue        , 2, Arrays.asList(new MaterialStack(Steel, 1), new MaterialStack(Tungsten, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials NitroCoalFuel           = new Materials(  -1, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16                ,  50,  70,  50,   0,   "NitroCoalFuel"           ,   "Nitro-Coalfuel"                ,    0,      48,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 0, Arrays.asList(new MaterialStack(Glyceryl, 1), new MaterialStack(CoalFuel, 4)));
    public static Materials NitroFuel               = new Materials( 709, TextureSet.SET_FLUID             ,   1.0F,      0,  2,         16                , 200, 255,   0,   0,   "NitroFuel"               ,   "Cetane-Boosted Diesel"         ,    0,    1000,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLime        );
    public static Materials RedAlloy                = new Materials( 308, TextureSet.SET_DULL              ,   1.0F,      0,  0, 1|2                       , 200,   0,   0,   0,   "RedAlloy"                ,   "Red Alloy"                     ,    0,       0,        500,    0, false, false,   3,   5,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(Copper, 1), new MaterialStack(Redstone, 4)), Collections.singletonList(new TC_AspectStack(TC_Aspects.MACHINA, 3)));
    public static Materials CobaltBrass             = new Materials( 343, TextureSet.SET_METALLIC          ,   8.0F,    256,  2, 1|2          |64|128      , 180, 180, 160,   0,   "CobaltBrass"             ,   "Cobalt Brass"                  ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeOrange      , 2, Arrays.asList(new MaterialStack(Brass, 7), new MaterialStack(Aluminium, 1), new MaterialStack(Cobalt, 1)));
    public static Materials TricalciumPhosphate     = new Materials( 534, TextureSet.SET_FLINT             ,   1.0F,      0, 2,  1|4|8|16                  , 255, 255,   0,   0,   "TricalciumPhosphate"     ,   "Tricalcium Phosphate"          ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Calcium, 3), new MaterialStack(Phosphate, 2)));
    public static Materials Basalt                  = new Materials( 844, TextureSet.SET_ROUGH             ,   1.0F,     64,  1, 1            |64|128      ,  30,  20,  20,   0,   "Basalt"                  ,   "Basalt"                        ,    0,       0,         -1,    0, false, false,   2,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Olivine, 1), new MaterialStack(Calcite, 3), new MaterialStack(Flint, 8), new MaterialStack(DarkAsh, 4)), Collections.singletonList(new TC_AspectStack(TC_Aspects.TENEBRAE, 1))).disableAutoGeneratedRecycleRecipes();
    public static Materials GarnetRed               = new Materials( 527, TextureSet.SET_RUBY              ,   7.0F,    128,  2, 1  |4|8      |64          , 200,  80,  80, 127,   "GarnetRed"               ,   "Red Garnet"                    ,    0,       0,         -1,    0, false,  true,   4,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(Pyrope, 3), new MaterialStack(Almandine, 5), new MaterialStack(Spessartine, 8)), Collections.singletonList(new TC_AspectStack(TC_Aspects.VITREUS, 3)));
    public static Materials GarnetYellow            = new Materials( 528, TextureSet.SET_RUBY              ,   7.0F,    128,  2, 1  |4|8      |64          , 200, 200,  80, 127,   "GarnetYellow"            ,   "Yellow Garnet"                 ,    0,       0,         -1,    0, false,  true,   4,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Andradite, 5), new MaterialStack(Grossular, 8), new MaterialStack(Uvarovite, 3)), Collections.singletonList(new TC_AspectStack(TC_Aspects.VITREUS, 3)));
    public static Materials Marble                  = new Materials( 845, TextureSet.SET_FINE              ,   1.0F,     16,  1, 1            |64|128      , 200, 200, 200,   0,   "Marble"                  ,   "Marble"                        ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Magnesium, 1), new MaterialStack(Calcite, 7)), Collections.singletonList(new TC_AspectStack(TC_Aspects.PERFODIO, 1)));
    public static Materials Sugar                   = new Materials( 803, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1                         , 250, 250, 250,   0,   "Sugar"                   ,   "Sugar"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(Carbon, 2), new MaterialStack(Water, 5), new MaterialStack(Oxygen, 25)), Arrays.asList(new TC_AspectStack(TC_Aspects.HERBA, 1), new TC_AspectStack(TC_Aspects.AQUA, 1), new TC_AspectStack(TC_Aspects.AER, 1)));
    public static Materials Thaumium                = new Materials( 330, TextureSet.SET_METALLIC          ,  12.0F,    256,  3, 1|2          |64|128      , 150, 100, 200,   0,   "Thaumium"                ,   "Thaumium"                      ,    0,       0,         -1,    0, false, false,   5,   2,   1, Dyes.dyePurple      , 0, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Magic, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.PRAECANTATIO, 1)));
    public static Materials Vinteum                 = new Materials( 529, TextureSet.SET_METALLIC          ,  10.0F,    128,  3, 1|2|4|8      |64|128      , 100, 200, 255,   0,   "Vinteum"                 ,   "Vinteum"                       ,    5,      32,         -1,    0, false, false,   4,   1,   1, Dyes.dyeLightBlue   , 2, Collections.singletonList(new MaterialStack(Thaumium, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.VITREUS, 2), new TC_AspectStack(TC_Aspects.PRAECANTATIO, 1)));
    public static Materials Vis                     = new Materials(  -1, TextureSet.SET_SHINY             ,   1.0F,      0,  3, 0                         , 128,   0, 255,   0,   "Vis"                     ,   "Vis"                           ,    5,      32,         -1,    0, false, false,   1,   1,   1, Dyes.dyePurple      , 2, Collections.singletonList(new MaterialStack(Magic, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.AURAM, 2), new TC_AspectStack(TC_Aspects.PRAECANTATIO, 1)));
    public static Materials Redrock                 = new Materials( 846, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1                         , 255,  80,  50,   0,   "Redrock"                 ,   "Redrock"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(Calcite, 2), new MaterialStack(Flint, 1), new MaterialStack(Clay, 1)));
    public static Materials PotassiumFeldspar       = new Materials( 847, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1                         , 120,  40,  40,   0,   "PotassiumFeldspar"       ,   "Potassium Feldspar"            ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyePink        , 0, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Aluminium, 1), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 8)));
    public static Materials Biotite                 = new Materials( 848, TextureSet.SET_METALLIC          ,   1.0F,      0,  1, 1                         ,  20,  30,  20,   0,   "Biotite"                 ,   "Biotite"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeGray        , 0, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Magnesium, 3), new MaterialStack(Aluminium, 3), new MaterialStack(Fluorine, 2), new MaterialStack(Silicon, 3), new MaterialStack(Oxygen, 10)));
    public static Materials GraniteBlack            = new Materials( 849, TextureSet.SET_ROUGH             ,   4.0F,     64,  3, 1            |64|128      ,  10,  10,  10,   0,   "GraniteBlack"            ,   "Black Granite"                 ,    0,       0,         -1,    0, false, false,   0,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(SiliconDioxide, 4), new MaterialStack(Biotite, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.TUTAMEN, 1)));
    public static Materials GraniteRed              = new Materials( 850, TextureSet.SET_ROUGH             ,   4.0F,     64,  3, 1            |64|128      , 255,   0, 128,   0,   "GraniteRed"              ,   "Red Granite"                   ,    0,       0,         -1,    0, false, false,   0,   1,   1, Dyes.dyeMagenta     , 0, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(PotassiumFeldspar, 1), new MaterialStack(Oxygen, 3)), Collections.singletonList(new TC_AspectStack(TC_Aspects.TUTAMEN, 1)));
    public static Materials Chrysotile              = new Materials( 912, TextureSet.SET_DULL              ,  32.0F,  10240,  3, 1|2  |8      |64|128      , 110, 140, 110,   0,   "Chrysotile"              ,   "Chrysotile"                    ,    0,       0,       9400, 9400,  true, false,   1,   1,   1, Dyes.dyeWhite       , 2, Collections.singletonList(new MaterialStack(Asbestos, 1))).disableAutoGeneratedBlastFurnaceRecipes().setTurbineMultipliers(280, 280, 1);
    public static Materials Realgar                 = new Materials( 913, TextureSet.SET_DULL              ,   1.0F,     32,  1, 1|2  |8      |64|128      , 140, 100, 100,   0,   "Realgar"                 ,   "Realgar"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Arsenic, 4), new MaterialStack(Sulfur,4)));
    public static Materials VanadiumMagnetite       = new Materials( 923, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   ,  35,  35,  60,   0,   "VanadiumMagnetite"       ,   "Vanadium Magnetite"            ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Magnetite, 1), new MaterialStack(Vanadium, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.MAGNETO, 1))); // Mixture of Fe3O4 and V2O5
    public static Materials BasalticMineralSand     = new Materials( 935, TextureSet.SET_SAND              ,   1.0F,      0,  1, 1    |8                   ,  40,  50,  40,   0,   "BasalticMineralSand"     ,   "Basaltic Mineral Sand"         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Magnetite, 1), new MaterialStack(Basalt, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.MAGNETO, 1)));
    public static Materials GraniticMineralSand     = new Materials( 936, TextureSet.SET_SAND              ,   1.0F,      0,  1, 1    |8                   ,  40,  60,  60,   0,   "GraniticMineralSand"     ,   "Granitic Mineral Sand"         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Magnetite, 1), new MaterialStack(GraniteBlack, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.MAGNETO, 1)));
    public static Materials GarnetSand              = new Materials( 938, TextureSet.SET_SAND              ,   1.0F,      0,  1, 1    |8                   , 200, 100,   0,   0,   "GarnetSand"              ,   "Garnet Sand"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeOrange      , 2, Arrays.asList(new MaterialStack(GarnetRed, 1), new MaterialStack(GarnetYellow, 1)));
    public static Materials QuartzSand              = new Materials( 939, TextureSet.SET_SAND              ,   1.0F,      0,  1, 1    |8                   , 194, 178, 128,   0,   "QuartzSand"              ,   "Quartz Sand"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(CertusQuartz, 1), new MaterialStack(Quartzite, 1)));
    public static Materials Bastnasite              = new Materials( 905, TextureSet.SET_FINE              ,   1.0F,      0,  2, 1    |8                   , 200, 110,  45,   0,   "Bastnasite"              ,   "Bastnasite"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Cerium, 1), new MaterialStack(Carbon, 1), new MaterialStack(Fluorine, 1), new MaterialStack(Oxygen, 3))); // (Ce, La, Y)CO3F
    public static Materials Pentlandite             = new Materials( 909, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 165, 150,   5,   0,   "Pentlandite"             ,   "Pentlandite"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Nickel, 9), new MaterialStack(Sulfur, 8))); // (Fe,Ni)9S8
    public static Materials Spodumene               = new Materials( 920, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 190, 170, 170,   0,   "Spodumene"               ,   "Spodumene"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Lithium, 1), new MaterialStack(Aluminium, 1), new MaterialStack(Silicon, 2), new MaterialStack(Oxygen, 6))); // LiAl(SiO3)2
    public static Materials Pollucite               = new Materials( 919, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 240, 210, 210,   0,   "Pollucite"               ,   "Pollucite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Caesium, 2), new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 4), new MaterialStack(Water, 2), new MaterialStack(Oxygen, 12))); // (Cs,Na)2Al2Si4O12 2H2O (also a source of Rb)
    public static Materials Tantalite               = new Materials( 921, TextureSet.SET_METALLIC          ,   1.0F,      0,  3, 1    |8                   , 145,  80,  40,   0,   "Tantalite"               ,   "Tantalite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Manganese, 1), new MaterialStack(Tantalum, 2), new MaterialStack(Oxygen, 6))); // (Fe, Mn)Ta2O6 (also source of Nb)
    public static Materials Lepidolite              = new Materials( 907, TextureSet.SET_FINE              ,   1.0F,      0,  2, 1    |8                   , 240,  50, 140,   0,   "Lepidolite"              ,   "Lepidolite"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Lithium, 3), new MaterialStack(Aluminium, 4), new MaterialStack(Fluorine, 2), new MaterialStack(Oxygen, 10))); // K(Li,Al,Rb)3(Al,Si)4O10(F,OH)2
    public static Materials Glauconite              = new Materials( 933, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 130, 180,  60,   0,   "Glauconite"              ,   "Glauconite"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Magnesium, 2), new MaterialStack(Aluminium, 4), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 12))); // (K,Na)(Fe3+,Al,Mg)2(Si,Al)4O10(OH)2
    public static Materials GlauconiteSand          = new Materials( 949, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 130, 180,  60,   0,   "GlauconiteSand"          ,   "Glauconite Sand"               ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Magnesium, 2), new MaterialStack(Aluminium, 4), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 12))); // (K,Na)(Fe3+,Al,Mg)2(Si,Al)4O10(OH)2
    public static Materials Vermiculite             = new Materials( 932, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   , 200, 180,  15,   0,   "Vermiculite"             ,   "Vermiculite"                   ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Iron, 3), new MaterialStack(Aluminium, 4), new MaterialStack(Silicon, 4), new MaterialStack(Hydrogen, 2), new MaterialStack(Water, 4), new MaterialStack(Oxygen, 12))); // (Mg+2, Fe+2, Fe+3)3 [(AlSi)4O10] (OH)2 4H2O)
    public static Materials Bentonite               = new Materials( 927, TextureSet.SET_ROUGH             ,   1.0F,      0,  2, 1    |8                   , 245, 215, 210,   0,   "Bentonite"               ,   "Bentonite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Sodium, 1), new MaterialStack(Magnesium, 6), new MaterialStack(Silicon, 12), new MaterialStack(Hydrogen, 6), new MaterialStack(Water, 5), new MaterialStack(Oxygen, 36))); // (Na,Ca)0.33(Al,Mg)2(Si4O10)(OH)2 nH2O
    public static Materials FullersEarth            = new Materials( 928, TextureSet.SET_FINE              ,   1.0F,      0,  2, 1    |8                   , 160, 160, 120,   0,   "FullersEarth"            ,   "Fullers Earth"                 ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Magnesium, 1), new MaterialStack(Silicon, 4), new MaterialStack(Hydrogen, 1), new MaterialStack(Water, 4), new MaterialStack(Oxygen, 11))); // (Mg,Al)2Si4O10(OH) 4(H2O)
    public static Materials Pitchblende             = new Materials( 873, TextureSet.SET_DULL              ,   1.0F,      0,  3, 1    |8                   , 200, 210,   0,   0,   "Pitchblende"             ,   "Pitchblende"                   ,    0,       0,         -1,    0, false, false,   5,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(Uraninite, 3), new MaterialStack(Thorium, 1), new MaterialStack(Lead, 1)));
    public static Materials Monazite                = new Materials( 520, TextureSet.SET_DIAMOND           ,   1.0F,      0,  1, 1  |4|8                   ,  50,  70,  50,   0,   "Monazite"                ,   "Monazite"                      ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeGreen       , 1, Arrays.asList(new MaterialStack(RareEarth, 1), new MaterialStack(Phosphate, 1))); // Wikipedia: (Ce, La, Nd, Th, Sm, Gd)PO4 Monazite also smelt-extract to Helium, it is brown like the rare earth Item Monazite sand deposits are inevitably of the monazite-(Ce) composition. Typically, the lanthanides in such monazites contain about 45.8% cerium, about 24% lanthanum, about 17% neodymium, about 5% praseodymium, and minor quantities of samarium, gadolinium, and yttrium. Europium concentrations tend to be low, about 0.05% Thorium content of monazite is variable.
    public static Materials Malachite               = new Materials( 871, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   ,   5,  95,   5,   0,   "Malachite"               ,   "Malachite"                     ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeGreen       , 1, Arrays.asList(new MaterialStack(Copper, 2), new MaterialStack(Carbon, 1), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 5))); // Cu2CO3(OH)2
    public static Materials Mirabilite              = new Materials( 900, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 240, 250, 210,   0,   "Mirabilite"              ,   "Mirabilite"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Sodium, 2), new MaterialStack(Sulfur, 1), new MaterialStack(Water, 10), new MaterialStack(Oxygen, 4))); // Na2SO4 10H2O
    public static Materials Mica                    = new Materials( 901, TextureSet.SET_FINE              ,   1.0F,      0,  1, 1    |8                   , 195, 195, 205,   0,   "Mica"                    ,   "Mica"                          ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Aluminium, 3), new MaterialStack(Silicon, 3), new MaterialStack(Fluorine, 2), new MaterialStack(Oxygen, 10))); // KAl2(AlSi3O10)(F,OH)2
    public static Materials Trona                   = new Materials( 903, TextureSet.SET_METALLIC          ,   1.0F,      0,  1, 1    |8                   , 135, 135,  95,   0,   "Trona"                   ,   "Trona"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Sodium, 3), new MaterialStack(Carbon, 2), new MaterialStack(Hydrogen, 1), new MaterialStack(Water, 2), new MaterialStack(Oxygen, 6))); // Na3(CO3)(HCO3) 2H2O
    public static Materials Barite                  = new Materials( 904, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 230, 235, 255,   0,   "Barite"                  ,   "Barite"                        ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Barium, 1), new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 4)));
    public static Materials Gypsum                  = new Materials( 934, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   , 230, 230, 250,   0,   "Gypsum"                  ,   "Gypsum"                        ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Sulfur, 1), new MaterialStack(Oxygen, 4), new MaterialStack(Water, 2))); // CaSO4 2H2O
    public static Materials Alunite                 = new Materials( 911, TextureSet.SET_METALLIC          ,   1.0F,      0,  2, 1    |8                   , 225, 180,  65,   0,   "Alunite"                 ,   "Alunite"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Potassium, 1), new MaterialStack(Aluminium, 3), new MaterialStack(Silicon, 2), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 14))); // KAl3(SO4)2(OH)6
    public static Materials Dolomite                = new Materials( 914, TextureSet.SET_FLINT             ,   1.0F,      0,  1, 1    |8                   , 225, 205, 205,   0,   "Dolomite"                ,   "Dolomite"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Magnesium, 1), new MaterialStack(Carbon, 2), new MaterialStack(Oxygen, 6))); // CaMg(CO3)2
    public static Materials Wollastonite            = new Materials( 915, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 240, 240, 240,   0,   "Wollastonite"            ,   "Wollastonite"                  ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Calcium, 1), new MaterialStack(Silicon, 1), new MaterialStack(Oxygen, 3))); // CaSiO3
    public static Materials Zeolite                 = new Materials( 916, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 240, 230, 230,   0,   "Zeolite"                 ,   "Zeolite"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Sodium, 1), new MaterialStack(Calcium, 4), new MaterialStack(Silicon, 27), new MaterialStack(Aluminium, 9), new MaterialStack(Oxygen, 72))); // NaCa4(Si27Al9)O72
    public static Materials Kyanite                 = new Materials( 924, TextureSet.SET_FLINT             ,   1.0F,      0,  2, 1    |8                   , 110, 110, 250,   0,   "Kyanite"                 ,   "Kyanite"                       ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 1), new MaterialStack(Oxygen, 5))); // Al2SiO5
    public static Materials Kaolinite               = new Materials( 929, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   , 245, 235, 235,   0,   "Kaolinite"               ,   "Kaolinite"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 0, Arrays.asList(new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 2), new MaterialStack(Hydrogen, 4), new MaterialStack(Oxygen, 9))); // Al2Si2O5(OH)4
    public static Materials Talc                    = new Materials( 902, TextureSet.SET_DULL              ,   1.0F,      0,  2, 1    |8                   ,  90, 180,  90,   0,   "Talc"                    ,   "Talc"                          ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Magnesium, 3), new MaterialStack(Silicon, 4), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 12))); // H2Mg3(SiO3)4
    public static Materials Soapstone               = new Materials( 877, TextureSet.SET_DULL              ,   1.0F,      0,  1, 1    |8                   ,  95, 145,  95,   0,   "Soapstone"               ,   "Soapstone"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes._NULL          , 1, Arrays.asList(new MaterialStack(Magnesium, 3), new MaterialStack(Silicon, 4), new MaterialStack(Hydrogen, 2), new MaterialStack(Oxygen, 12))); // H2Mg3(SiO3)4
    public static Materials Concrete                = new Materials( 947, TextureSet.SET_ROUGH             ,   1.0F,      0,  1, 1                         , 100, 100, 100,   0,   "Concrete"                ,   "Concrete"                      ,    0,       0,        300,    0, false, false,   0,   1,   1, Dyes.dyeGray        , 0, Collections.singletonList(new MaterialStack(Stone, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.TERRA, 1)));
    public static Materials IronMagnetic            = new Materials( 354, TextureSet.SET_MAGNETIC          ,   6.0F,    256,  2, 1|2          |64|128      , 200, 200, 200,   0,   "IronMagnetic"            ,   "Magnetic Iron"                 ,    0,       0,         -1,    0, false, false,   4,  51,  50, Dyes.dyeGray        , 1, Collections.singletonList(new MaterialStack(Iron, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.MAGNETO, 1)));
    public static Materials SteelMagnetic           = new Materials( 355, TextureSet.SET_MAGNETIC          ,   6.0F,    512,  3, 1|2          |64|128      , 128, 128, 128,   0,   "SteelMagnetic"           ,   "Magnetic Steel"                ,    0,       0,       1000, 1000,  true, false,   4,  51,  50, Dyes.dyeGray        , 1, Collections.singletonList(new MaterialStack(Steel, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 1), new TC_AspectStack(TC_Aspects.ORDO, 1), new TC_AspectStack(TC_Aspects.MAGNETO, 1)));
    public static Materials NeodymiumMagnetic       = new Materials( 356, TextureSet.SET_MAGNETIC          ,   7.0F,    512,  2, 1|2          |64|128      , 100, 100, 100,   0,   "NeodymiumMagnetic"       ,   "Magnetic Neodymium"            ,    0,       0,       1297, 1297,  true, false,   4,  51,  50, Dyes.dyeGray        , 1, Collections.singletonList(new MaterialStack(Neodymium, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 1), new TC_AspectStack(TC_Aspects.MAGNETO, 3)));
    public static Materials SamariumMagnetic        = new Materials( 399, TextureSet.SET_MAGNETIC          ,   1.0F,      0,  2, 1|2          |64|128      , 255, 255, 204,   0,   "SamariumMagnetic"        ,   "Magnetic Samarium"             ,    0,       0,       1345, 1345,  true, false,   4,   1,   1, Dyes.dyeWhite       , 1, Collections.singletonList(new MaterialStack(Samarium, 1)),Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.RADIO, 1), new TC_AspectStack(TC_Aspects.MAGNETO,10)));
    public static Materials TungstenCarbide         = new Materials( 370, TextureSet.SET_METALLIC          ,  14.0F,   1280,  4, 1|2          |64|128      ,  51,   0, 102,   0,   "TungstenCarbide"         ,   "Tungstencarbide"               ,    0,       0,       2460, 2460,  true, false,   4,   1,   1, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Tungsten, 1), new MaterialStack(Carbon, 1))).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials VanadiumSteel           = new Materials( 371, TextureSet.SET_METALLIC          ,   3.0F,   1920,  3, 1|2          |64|128      , 192, 192, 192,   0,   "VanadiumSteel"           ,   "Vanadiumsteel"                 ,    0,       0,       1453, 1453,  true, false,   4,   1,   1, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Vanadium, 1), new MaterialStack(Chrome, 1), new MaterialStack(Steel, 7))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials HSSG                    = new Materials( 372, TextureSet.SET_METALLIC          ,  10.0F,   4000,  3, 1|2          |64|128      , 153, 153,   0,   0,   "HSSG"                    ,   "HSS-G"                         ,    0,       0,       4500, 4500,  true, false,   4,   1,   1, Dyes.dyeYellow      , 2, Arrays.asList(new MaterialStack(TungstenSteel, 5), new MaterialStack(Chrome, 1), new MaterialStack(Molybdenum, 2), new MaterialStack(Vanadium, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials HSSE                    = new Materials( 373, TextureSet.SET_METALLIC          ,  32.0F,  10240,  7, 1|2          |64|128      ,  51, 102,   0,   0,   "HSSE"                    ,   "HSS-E"                         ,    0,       0,       5400, 5400,  true, false,   4,   1,   1, Dyes.dyeGreen       , 2, Arrays.asList(new MaterialStack(HSSG, 6), new MaterialStack(Cobalt, 1),new MaterialStack(Manganese, 1), new MaterialStack(Silicon, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials HSSS                    = new Materials( 374, TextureSet.SET_METALLIC          ,  32.0F,  10240,  8, 1|2          |64|128      , 102,   0,  51,   0,   "HSSS"                    ,   "HSS-S"                         ,    0,       0,       5400, 5400,  true, false,   4,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(HSSG, 6), new MaterialStack(Iridium, 2), new MaterialStack(Osmium, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials TPV                     = new Materials( 576, TextureSet.SET_METALLIC          ,  16.0F,   4000,  5, 1|2          |64|128      , 250,  170,250,   0,   "TPVAlloy"                ,   "TPV-Alloy"                     ,    0,       0,       3000, 3000,  true, false,   4,   1,   1, Dyes.dyeRed         , 2, Arrays.asList(new MaterialStack(Titanium, 3), new MaterialStack(Platinum, 3), new MaterialStack(Vanadium, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials DilutedSulfuricAcid     = new MaterialBuilder(640, TextureSet.SET_FLUID             ,                                                                                                     "Diluted Sulfuric Acid").addCell().addFluid().setRGB(192, 120, 32).setColor(Dyes.dyeOrange).setMaterialList(new MaterialStack(SulfuricAcid, 1)).constructMaterial();
    public static Materials EpoxidFiberReinforced   = new Materials( 610, TextureSet.SET_DULL                 ,3.0F,     64,  1, 1|2          |64|128      , 160, 112,  16,   0,   "EpoxidFiberReinforced"   ,   "Fiber-Reinforced Epoxy Resin"  ,    0,       0,        400,    0, false, false,   1,   1,   1, Dyes.dyeBrown       , 2, Collections.singletonList(new MaterialStack(Epoxid, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.MOTUS, 2)));
    public static Materials SodiumCarbonate         = new MaterialBuilder(695, TextureSet.SET_QUARTZ, "Sodium Carbonate").setToolSpeed(1.0F).setDurability(64).setToolQuality(1).addDustItems().setRGB(255, 255, 235).setColor(Dyes.dyeWhite).setBlastFurnaceTemp(851 ).setMeltingPoint(851 ).setBlastFurnaceRequired(false).setOreValue(1).setExtraData(1).setMaterialList(new MaterialStack(Sodium, 2), new MaterialStack(Carbon, 1), new MaterialStack(Oxygen, 3)).constructMaterial().disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials SodiumAluminate         = new MaterialBuilder(696, TextureSet.SET_QUARTZ, "Sodium Aluminate").setToolSpeed(1.0F).setDurability(64).setToolQuality(1).addDustItems().setRGB(255, 235, 255).setColor(Dyes.dyeWhite).setBlastFurnaceTemp(1800).setMeltingPoint(1800).setBlastFurnaceRequired(false).setOreValue(1).setExtraData(0).setMaterialList(new MaterialStack(Sodium, 1), new MaterialStack(Aluminium, 1), new MaterialStack(Oxygen, 2)).constructMaterial().disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials Aluminiumoxide          = new MaterialBuilder(697, TextureSet.SET_QUARTZ, "Alumina").setToolSpeed(1.0F).setDurability(64).setToolQuality(1).addDustItems().setRGB(235, 255, 255).setColor(Dyes.dyeWhite).setBlastFurnaceTemp(2054).setMeltingPoint(2054).setBlastFurnaceRequired(true).setOreValue(1).setExtraData(0).setMaterialList(new MaterialStack(Aluminium, 2), new MaterialStack(Oxygen, 3)).setAspects(Collections.singletonList(new TC_AspectStack(TC_Aspects.GELUM, 2))).constructMaterial().disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials Aluminiumhydroxide      = new MaterialBuilder(698, TextureSet.SET_QUARTZ, "Aluminium Hydroxide").setToolSpeed(1.0F).setDurability(64).setToolQuality(1).addDustItems().setRGB(235, 235, 255).setColor(Dyes.dyeWhite).setBlastFurnaceTemp(1200).setMeltingPoint(1200).setBlastFurnaceRequired(true).setOreValue(1).setExtraData(0).setMaterialList(new MaterialStack(Aluminium, 1), new MaterialStack(Oxygen, 3),  new MaterialStack(Hydrogen, 3)).setAspects(Collections.singletonList(new TC_AspectStack(TC_Aspects.GELUM, 2))).constructMaterial().disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials Cryolite                = new MaterialBuilder(699, TextureSet.SET_QUARTZ, "Cryolite").setToolSpeed(1.0F).setDurability(64).setToolQuality(1).addOreItems().setRGB(191, 239, 255).setColor(Dyes.dyeLightBlue).setMeltingPoint(1012).setBlastFurnaceTemp(1012).setExtraData(0).setMaterialList(new MaterialStack(Sodium, 3), new MaterialStack(Aluminium, 1), new MaterialStack(Fluorine, 6)).constructMaterial().disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials RedMud                  = new MaterialBuilder(743, TextureSet.SET_FLUID, "Red Mud").addCell().addFluid().setRGB(140, 22, 22).setColor(Dyes.dyeRed).constructMaterial();

    public static Materials Brick                   = new MaterialBuilder(625, TextureSet.SET_ROUGH      ,                                                                                                     "Brick").addDustItems().setRGB(155, 86, 67).setColor(Dyes.dyeBrown).setExtraData(0).setMaterialList(new MaterialStack(Aluminium, 2), new MaterialStack(Silicon, 4), new MaterialStack(Oxygen, 11)).constructMaterial();
    public static Materials Fireclay                = new MaterialBuilder(626, TextureSet.SET_ROUGH      ,                                                                                                     "Fireclay").addDustItems().setRGB(173, 160, 155).setExtraData(2).setColor(Dyes.dyeBrown).setMaterialList(new MaterialStack(Brick, 1), new MaterialStack(Clay, 1)).constructMaterial();

    // Polybenzimidazole stuff
    public static Materials PotassiumNitrade     = new MaterialBuilder(590, TextureSet.SET_DULL       ,                                                                                                     "Potassium Nitrate").setName("PotassiumNitrate").addDustItems().setRGB(129, 34, 141).setColor(Dyes.dyePurple).setMaterialList(new MaterialStack(Potassium, 1), new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();
    public static Materials ChromiumTrioxide = new MaterialBuilder(591, TextureSet.SET_DULL       ,                                                                                                     "Chromium Trioxide").setName("Chromiumtrioxide").addDustItems().setRGB(255, 228, 225).setColor(Dyes.dyePink).setMaterialList(new MaterialStack(Chrome, 1), new MaterialStack(Oxygen, 3)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Nitrochlorobenzene = new MaterialBuilder(592, TextureSet.SET_FLUID             ,                                                                                                     "2-Nitrochlorobenzene").addCell().addFluid().setRGB(143, 181, 26).setColor(Dyes.dyeLime).setMaterialList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 4), new MaterialStack(Chlorine, 1), new MaterialStack(Nitrogen, 1), new MaterialStack(Oxygen, 2)).constructMaterial();
    public static Materials Dimethylbenzene = new MaterialBuilder(593, TextureSet.SET_FLUID             ,                                                                                                     "1,2-Dimethylbenzene").setName("Dimethylbenzene").addCell().addFluid().setRGB(102, 156, 64).setColor(Dyes.dyeLime).setMeltingPoint(248).setMaterialList(new MaterialStack(Carbon, 8), new MaterialStack(Hydrogen, 10)).addElectrolyzerRecipe().constructMaterial();
    public static Materials Potassiumdichromate = new MaterialBuilder(594, TextureSet.SET_DULL       ,                                                                                                     "Potassium Dichromate").setName("PotassiumDichromate").addDustItems().setRGB(255, 8, 127).setColor(Dyes.dyePink).setMaterialList(new MaterialStack(Potassium, 2), new MaterialStack(Chrome, 2), new MaterialStack(Oxygen, 7)).addElectrolyzerRecipe().constructMaterial();
    public static Materials PhthalicAcid = new MaterialBuilder(595, TextureSet.SET_FLUID             ,                                                                                                     "Phthalic Acid").setName("phtalicacid").addCell().addFluid().setRGB(54, 133, 71).setColor(Dyes.dyeOrange).setMaterialList(new MaterialStack(Carbon, 8), new MaterialStack(Hydrogen, 6), new MaterialStack(Oxygen, 4)).constructMaterial();
    public static Materials Dichlorobenzidine = new MaterialBuilder(596, TextureSet.SET_FLUID             ,                                                                                                     "3,3-Dichlorobenzidine").addCell().addFluid().setRGB(161, 222, 166).setColor(Dyes.dyeOrange).setMaterialList(new MaterialStack(Carbon, 12),new MaterialStack(Hydrogen, 10), new MaterialStack(Nitrogen, 2), new MaterialStack(Chlorine, 2)).constructMaterial();
    public static Materials Diaminobenzidin = new MaterialBuilder(597, TextureSet.SET_FLUID             ,                                                                                                     "3,3-Diaminobenzidine").addCell().addFluid().setRGB(51, 125, 89).setColor(Dyes.dyeOrange).setMaterialList(new MaterialStack(Carbon, 12),new MaterialStack(Hydrogen, 14),new MaterialStack(Nitrogen, 4)).constructMaterial();
    public static Materials Diphenylisophthalate = new MaterialBuilder(598, TextureSet.SET_FLUID             ,                                                                                                     "Diphenyl Isophtalate").addCell().addFluid().setRGB(36, 110, 87).setColor(Dyes.dyeOrange).setMaterialList(new MaterialStack(Carbon, 20),new MaterialStack(Hydrogen, 14),new MaterialStack(Oxygen, 4)).constructMaterial();
    public static Materials Polybenzimidazole   = new Materials(599, TextureSet.SET_DULL                 ,3.0F,     64,  1, 1|2          |64|128      , 45, 45,  45,   0,   "Polybenzimidazole"   ,   "Polybenzimidazole"  ,    0,       0,        1450,    0, false, false,   1,   1,   1, Dyes.dyeBlack       , 0, Arrays.asList(new MaterialStack(Carbon, 20), new MaterialStack(Nitrogen, 4), new MaterialStack(Hydrogen, 12)), Arrays.asList(new TC_AspectStack(TC_Aspects.ORDO, 2),new TC_AspectStack(TC_Aspects.VOLATUS, 1)));

    //Gasoline
    public static Materials MTBEMixture        = new MaterialBuilder(983, TextureSet.SET_FLUID             ,                                                                                                      "MTBE Reaction Mixture (Butene)").addCell().addGas().setRGB(255, 255, 255).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Carbon, 5), new MaterialStack(Hydrogen, 12), new MaterialStack(Oxygen, 1)).constructMaterial();
    public static Materials MTBEMixtureAlt = makeMTBEMixtureAlt();
    private static Materials makeMTBEMixtureAlt() {
        return new MaterialBuilder(425, TextureSet.SET_FLUID , "MTBE Reaction Mixture (Butane)").addCell().addGas().setRGB(255, 255, 255).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Carbon, 5), new MaterialStack(Hydrogen, 14), new MaterialStack(Oxygen, 1)).constructMaterial();
    }
    public static Materials NitrousOxide       = new MaterialBuilder(993, TextureSet.SET_FLUID             ,                                                                                                      "Nitrous Oxide").addCell().addGas().setRGB(125, 200, 255).setColor(Dyes.dyeBlue).setMaterialList(new MaterialStack(Nitrogen, 2), new MaterialStack(Oxygen, 1)).addElectrolyzerRecipe().constructMaterial();
    public static Materials AntiKnock          = new MaterialBuilder(994, TextureSet.SET_FLUID             ,                                                                                                      "Anti-Knock Agent").setName("EthylTertButylEther").addCell().addFluid().setRGB(255, 255, 255).setColor(Dyes.dyeWhite).setMaterialList(new MaterialStack(Carbon, 6), new MaterialStack(Hydrogen, 14), new MaterialStack(Oxygen, 1)).constructMaterial();
    public static Materials Octane             = new MaterialBuilder(995, TextureSet.SET_FLUID             ,                                                                                                      "Octane").addCell().addFluid().setRGB(255, 255, 255).setColor(Dyes.dyeWhite).setFuelType(MaterialBuilder.DIESEL).setFuelPower(80).setMaterialList(new MaterialStack(Carbon, 8), new MaterialStack(Hydrogen, 18)).constructMaterial();
    public static Materials GasolineRaw        = new MaterialBuilder(996, TextureSet.SET_FLUID             ,                                                                                                      "Raw Gasoline").addCell().addFluid().setRGB(255,100,0).setColor(Dyes.dyeOrange).constructMaterial();
    public static Materials GasolineRegular    = new MaterialBuilder(997, TextureSet.SET_FLUID             ,                                                                                                      "Gasoline").addCell().addFluid().setRGB(255,165,0).setColor(Dyes.dyeOrange).setFuelType(MaterialBuilder.DIESEL).setFuelPower(576).constructMaterial();
    public static Materials GasolinePremium    = new MaterialBuilder(998, TextureSet.SET_FLUID             ,                                                                                                      "High Octane Gasoline").addCell().addFluid().setRGB(255,165,0).setColor(Dyes.dyeOrange).setFuelType(MaterialBuilder.DIESEL).setFuelPower(2500).constructMaterial();

    //ADDED
    public static Materials Electrotine             = new Materials( 812, TextureSet.SET_SHINY             ,   1.0F,      0,  1, 1    |8                   ,  60, 180, 200,   0,   "Electrotine"             ,   "Electrotine"                   ,    0,       0,         -1,    0, false, false,   3,   1,   1, Dyes.dyeCyan        , 0, Arrays.asList(new MaterialStack(Redstone, 1), new MaterialStack(Electrum, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 2)));
    public static Materials Galgadorian             = new Materials( 384, TextureSet.SET_METALLIC          ,  16.0F,   3600,  3, 1|2          |64|128      , 154, 105, 119,   0,   "Galgadorian"             ,   "Galgadorian"                   ,    0,       0,       3000, 3000,  true, false,   1,   1,   1, Dyes.dyePink        ).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials EnhancedGalgadorian     = new Materials( 385, TextureSet.SET_METALLIC          ,  32.0F,   7200,  5, 1|2|          64|128      , 152, 93, 133,    0,   "EnhancedGalgadorian"     ,   "Enhanced Galgadorian"          ,    0,       0,       4500, 4500,  true, false,   1,   1,   1, Dyes.dyePink        ).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials BloodInfusedIron        = new Materials( 977, TextureSet.SET_METALLIC          ,  10.0F,    384,  2, 1|2          |64|128      ,  69,   9,  10,   0,   "BloodInfusedIron"        ,   "Blood Infused Iron"            ,    0,       0,       2400,    0, false, false,   3,   1,   1, Dyes.dyeRed         , Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 3),  new TC_AspectStack(TC_Aspects.PRAECANTATIO, 1)));
    public static Materials Shadow                  = new Materials( 368, TextureSet.SET_METALLIC          ,  32.0F,   8192,  4, 1|2  |8      |64|128      ,  16,   3,  66,   0,   "Shadow"                  ,   "Shadow Metal"                  ,    0,       0,       1800, 1800,  true, false,   3,   4,   3, Dyes.dyeBlue        );

    /**
     * Galaxy Space 1.10 compat from Version 2.6
     */
    public static Materials Ledox                   = new Materials( 390, TextureSet.SET_SHINY             ,  15.0F,   1024,  4, 1|2  |8      |64|128      ,   0, 116, 255,   0,   "Ledox"                   ,   "Ledox"                         ,     0,       0,        -1,    0, false, false,  4,   1,   1, Dyes.dyeBlue         );
    public static Materials Quantium                = new Materials( 391, TextureSet.SET_SHINY             ,  18.0F,   2048,  4, 1|2  |8      |64|128      ,   0, 209,  11,   0,   "Quantium"                ,   "Quantium"                      ,     0,       0,     9900,  9900,  true, false,  4,   1,   1, Dyes.dyeLime         ).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Mytryl                  = new Materials( 387, TextureSet.SET_SHINY             ,   8.0F,    512,  4, 1|2  |8      |64|128      , 242, 100,   4,   0,   "Mytryl"                  ,   "Mytryl"                        ,     0,       0,     3600,  3600,  true, false,  4,   1,   1, Dyes.dyeOrange       );
    public static Materials BlackPlutonium          = new Materials( 388, TextureSet.SET_DULL              ,  36.0F,   8192,  8, 1|2  |8      |64|128      ,  50,  50,  50,   0,   "BlackPlutonium"          ,   "Black Plutonium"               ,     0,       0,     9000,  9000,  true, false,  4,   1,   1, Dyes.dyeBlack        ).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials CallistoIce             = new Materials( 389, TextureSet.SET_SHINY             ,   9.0F,   1024,  4, 1|2  |8      |64|128      ,  30, 177, 255,   0,   "CallistoIce"             ,   "Callisto Ice"                  ,     0,       0,       -1,     0, false, false,  4,   1,   1, Dyes.dyeLightBlue    );
    public static Materials Duralumin               = new Materials( 392, TextureSet.SET_SHINY             ,  16.0F,    512,  3, 1|2  |8      |64|128      , 235, 209, 160,   0,   "Duralumin"               ,   "Duralumin"                     ,     0,       0,     1600,  1600,  true, false,  4,   1,   1, Dyes.dyeOrange       , 2, Arrays.asList(new MaterialStack(Aluminium, 6), new MaterialStack(Copper, 1), new MaterialStack(Manganese, 1), new MaterialStack(Magnesium, 1)));
    public static Materials Oriharukon              = new Materials( 393, TextureSet.SET_SHINY             ,  32.0F,  10240,  5, 1|2  |8   |32|64|128      , 103, 125, 104,   0,   "Oriharukon"              ,   "Oriharukon"                    ,     0,       0,     5400,  5400,  true, false,  4,   1,   1, Dyes.dyeLime         , Element.Oh, Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2),new TC_AspectStack(TC_Aspects.LUCRUM, 2), new TC_AspectStack(TC_Aspects.ALIENIS, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials MysteriousCrystal       = new Materials( 398, TextureSet.SET_SHINY             ,   8.0F,    256,  6, 1|2  |8      |64|128      ,  22, 133, 108,   0,   "MysteriousCrystal"       ,   "Mysterious Crystal"            ,     0,       0,     7200,  7200,  true, false,  4,   1,   1, Dyes.dyeCyan         ).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();

    //\/HAD TO MOVE DOWN SECTION
    public static Materials RedstoneAlloy           = new Materials( 381, TextureSet.SET_METALLIC          ,   3.0F,    128,  2, 1|2          |64|128      , 181,  51,  51,   0,   "RedstoneAlloy"           ,   "Redstone Alloy"                ,    0,       0,        671, 1000,  true, false,   1,   1,   1, Dyes.dyeRed         , 1, Arrays.asList(new MaterialStack(Redstone, 1), new MaterialStack(Silicon, 1), new MaterialStack(Coal, 1))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials Soularium               = new Materials( 379, TextureSet.SET_METALLIC          ,   8.0F,    256,  2, 1|2          |64|128      ,  65,  46,  29,   0,   "Soularium"               ,   "Soularium"                     ,    0,       0,        800, 1000,  true, false,   3,   1,   1, Dyes.dyeBrown       , 1, Arrays.asList(new MaterialStack(SoulSand, 1), new MaterialStack(Gold, 1), new MaterialStack(Ash, 1))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials ConductiveIron          = new Materials( 369, TextureSet.SET_METALLIC          ,   6.0F,    256,  3, 1|2          |64|128      , 217, 178, 171,   0,   "ConductiveIron"          ,   "Conductive Iron"               ,    0,       0,         -1, 1200,  true, false,   4,   1,   1, Dyes.dyeRed         , 1, Arrays.asList(new MaterialStack(RedstoneAlloy, 1), new MaterialStack(Iron, 1), new MaterialStack(Silver, 1))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials ElectricalSteel         = new Materials( 365, TextureSet.SET_METALLIC          ,   6.0F,    512,  2, 1|2          |64|128      , 185, 185, 185,   0,   "ElectricalSteel"         ,   "Electrical Steel"              ,    0,       0,       1811, 1000,  true, false,   4,   1,   1, Dyes.dyeGray        , 1, Arrays.asList(new MaterialStack(Steel, 1), new MaterialStack(Coal, 1), new MaterialStack(Silicon, 1))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials EnergeticAlloy          = new Materials( 366, TextureSet.SET_METALLIC          ,  12.0F,   1024,  3, 1|2          |64|128      , 255, 170,  81,   0,   "EnergeticAlloy"          ,   "Energetic Alloy"               ,    0,       0,         -1, 2200,  true, false,   3,   1,   1, Dyes.dyeOrange      , 1, Arrays.asList(new MaterialStack(ConductiveIron, 1), new MaterialStack(Gold, 1), new MaterialStack(BlackSteel, 1))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials VibrantAlloy            = new Materials( 367, TextureSet.SET_METALLIC          ,  18.0F,   4048,  4, 1|2          |64|128      , 157, 188,  53,   0,   "VibrantAlloy"            ,   "Vibrant Alloy"                 ,    0,       0,       3300, 3300,  true, false,   4,   1,   1, Dyes.dyeLime        , 1, Arrays.asList(new MaterialStack(EnergeticAlloy, 1), new MaterialStack(EnderEye, 1), new MaterialStack(Chrome, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials PulsatingIron           = new Materials( 378, TextureSet.SET_METALLIC          ,   6.0F,    256,  3, 1|2          |64|128      , 128, 246, 155,   0,   "PulsatingIron"           ,   "Pulsating Iron"                ,    0,       0,         -1, 1800,  true, false,   4,   1,   1, Dyes.dyeLime        , 1, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(EnderPearl, 1), new MaterialStack(RedstoneAlloy, 1))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials DarkSteel               = new Materials( 364, TextureSet.SET_METALLIC          ,   8.0F,    512,  3, 1|2          |64|128      ,  80,  70,  80,   0,   "DarkSteel"               ,   "Dark Steel"                    ,    0,       0,         -1, 1800,  true, false,   3,   1,   1, Dyes.dyePurple      , 1, Arrays.asList(new MaterialStack(ElectricalSteel, 1), new MaterialStack(Coal, 1), new MaterialStack(Obsidian, 1))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials EndSteel                = new Materials( 401, TextureSet.SET_METALLIC          ,  12.0F,   2000,  4, 1|2          |64|128      , 223, 217, 165,   0,   "EndSteel"                ,   "End Steel"                     ,    0,       0,        940, 3600,  true, false,   3,   1,   1, Dyes.dyeYellow      , 1, Arrays.asList(new MaterialStack(DarkSteel, 1), new MaterialStack(Tungsten, 1), new MaterialStack(Endstone, 1))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials CrudeSteel              = new Materials( 402, TextureSet.SET_METALLIC          ,   2.0F,     64,  2, 1|2          |64|128      , 163, 158, 154,   0,   "CrudeSteel"              ,   "Clay Compound"                 ,    0,       0,         -1, 1000, false, false,   4,   1,   1, Dyes.dyeGray        , 1, Arrays.asList(new MaterialStack(Stone, 1), new MaterialStack(Clay, 1), new MaterialStack(Flint, 1))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials CrystallineAlloy        = new Materials( 403, TextureSet.SET_METALLIC          ,  18.0F,    768,  4, 1|2          |64|128      , 114, 197, 197,   0,   "CrystallineAlloy"        ,   "Crystalline Alloy"             ,    0,       0,       4500, 4500,  true, false,   4,   1,   1, Dyes.dyeCyan        , 1, Arrays.asList(new MaterialStack(Gold, 1), new MaterialStack(Diamond, 1), new MaterialStack(PulsatingIron, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials MelodicAlloy            = new Materials( 404, TextureSet.SET_METALLIC          ,  24.0F,   1024,  5, 1|2          |64|128      , 136,  98, 136,   0,   "MelodicAlloy"            ,   "Melodic Alloy"                 ,    0,       0,       5400, 5400,  true, false,   4,   1,   1, Dyes.dyeMagenta     , 1, Arrays.asList(new MaterialStack(EndSteel, 1), new MaterialStack(EnderEye, 1), new MaterialStack(Oriharukon, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials StellarAlloy            = new Materials( 405, TextureSet.SET_METALLIC          ,  96.0F,  10240,  7, 1|2          |64|128      , 217, 220, 203,   0,   "StellarAlloy"            ,   "Stellar Alloy"                 ,    0,       0,       7200, 7200,  true, false,   4,   1,   1, Dyes.dyeWhite       , 1, Arrays.asList(new MaterialStack(NetherStar, 1), new MaterialStack(MelodicAlloy, 1), new MaterialStack(Naquadah, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials CrystallinePinkSlime    = new Materials( 406, TextureSet.SET_METALLIC          ,   6.0F,    128,  3, 1|2          |64|128      , 231, 158, 219,   0,   "CrystallinePinkSlime"    ,   "Crystalline Pink Slime"        ,    0,       0,       5000, 5000,  true, false,   4,   1,   1, Dyes.dyePink        , 1, Arrays.asList(new MaterialStack(CrystallineAlloy, 1), new MaterialStack(Diamond, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials EnergeticSilver         = new Materials( 407, TextureSet.SET_METALLIC          ,   8.0F,    512,  3, 1|2          |64|128      , 149, 183, 205,   0,   "EnergeticSilver"         ,   "Energetic Silver"              ,    0,       0,         -1, 2200,  true, false,   4,   1,   1, Dyes.dyeLightBlue   , 1, Arrays.asList(new MaterialStack(Silver, 1), new MaterialStack(ConductiveIron, 1), new MaterialStack(BlackSteel, 1))).disableAutoGeneratedBlastFurnaceRecipes();
    public static Materials VividAlloy              = new Materials( 408, TextureSet.SET_METALLIC          ,  12.0F,    768,  4, 1|2          |64|128      ,  70, 188, 219,   0,   "VividAlloy"              ,   "Vivid Alloy"                   ,    0,       0,       3300, 3300,  true, false,   4,   1,   1, Dyes.dyeBlue        , 1, Arrays.asList(new MaterialStack(EnergeticSilver, 1), new MaterialStack(EnderEye, 1), new MaterialStack(Chrome, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Enderium                = new Materials( 321, TextureSet.SET_DULL              ,   8.0F,   1500,  3, 1|2          |64|128      ,  89, 145, 135,   0,   "Enderium"                ,   "Enderium"                      ,    0,       0,       4500, 4500,  true, false,   1,   1,   1, Dyes.dyeGreen       , 1, Arrays.asList(new MaterialStack(EnderiumBase, 2), new MaterialStack(Thaumium, 1), new MaterialStack(EnderPearl, 1)), Arrays.asList(new TC_AspectStack(TC_Aspects.METALLUM, 2), new TC_AspectStack(TC_Aspects.ALIENIS, 1))).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Mithril                 = new Materials( 331, TextureSet.SET_SHINY             ,  32.0F,     64,  2, 1|2  |8      |64          , 255, 255, 210,   0,   "Mithril"                 ,   "Mithril"                       ,    0,       0,       6600, 6600,  true, false,   4,   3,   2, Dyes.dyeLightBlue   , 2, Arrays.asList(new MaterialStack(Platinum, 2), new MaterialStack(Thaumium, 1))).disableAutoGeneratedBlastFurnaceRecipes().setTurbineMultipliers(22, 1, 1);
    public static Materials BlueAlloy               = new Materials( 309, TextureSet.SET_DULL              ,   1.0F,      0,  0, 1|2                       , 100, 180, 255,   0,   "BlueAlloy"               ,   "Blue Alloy"                    ,    0,       0,         -1,    0, false, false,   3,   5,   1, Dyes.dyeLightBlue   , 2, Arrays.asList(new MaterialStack(Silver, 1), new MaterialStack(Electrotine, 4)), Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 3)));
    public static Materials ShadowIron              = new Materials( 336, TextureSet.SET_METALLIC          ,  32.0F,  10240,  2, 1|2  |8      |64          , 120, 120, 120,   0,   "ShadowIron"              ,   "Shadow Iron"                   ,    0,       0,       8400, 8400,  true, false,   3,   4,   3, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Iron, 1), new MaterialStack(Thaumium, 3))).disableAutoGeneratedBlastFurnaceRecipes().setTurbineMultipliers(1, 76, 1);
    public static Materials ShadowSteel             = new Materials( 337, TextureSet.SET_METALLIC          ,   6.0F,    768,  4, 1|2          |64          ,  90,  90,  90,   0,   "ShadowSteel"             ,   "Shadow Steel"                  ,    0,       0,         -1, 1700,  true, false,   4,   4,   3, Dyes.dyeBlack       , 2, Arrays.asList(new MaterialStack(Steel, 1), new MaterialStack(Thaumium, 3)));
    public static Materials AstralSilver            = new Materials( 333, TextureSet.SET_SHINY             ,  10.0F,     64,  2, 1|2          |64          , 230, 230, 255,   0,   "AstralSilver"            ,   "Astral Silver"                 ,    0,       0,         -1,    0, false, false,   4,   3,   2, Dyes.dyeWhite       , 2, Arrays.asList(new MaterialStack(Silver, 2), new MaterialStack(Thaumium, 1)));

    /**
     * Op materials (draconic evolution above)
     */
    public static Materials InfinityCatalyst        = new Materials( 394, TextureSet.SET_SHINY             ,  64.0F,1310720,  10, 1|2    |8    |64|128      , 255, 255, 255,   0,   "InfinityCatalyst"        ,   "Infinity Catalyst"             ,     5,  500000,     10800,  10800,  true, false, 20,   1,   1, Dyes.dyeLightGray    ).setProcessingMaterialTierEU(TierEU.RECIPE_UV).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Infinity                = new Materials( 397, new TextureSet("infinity", true), 256.0F,2621440,  17, 1|2          |64|128      , 255, 255, 255,   0,   "Infinity"                ,   "Infinity"                      ,     5, 5000000,     10800,  10800,  true, false, 40,   1,   1, Dyes.dyeLightGray    ).setProcessingMaterialTierEU(TierEU.RECIPE_UV).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Bedrockium              = new MaterialBuilder(395,TextureSet.SET_DULL, "Bedrockium").addOreItems().addDustItems().addMetalItems().setDurability(327680).setToolSpeed(8f).setToolQuality(9).setRGB(50,50,50).setName("Bedrockium").setBlastFurnaceRequired(true).setBlastFurnaceTemp(9900).setMeltingPoint(9900).setColor(Dyes.dyeBlack).setOreValue(4).setDensityDivider(1).setDensityMultiplier(1).constructMaterial().setProcessingMaterialTierEU(TierEU.RECIPE_EV).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Trinium                 = new Materials( 868, TextureSet.SET_SHINY             , 128.0F,  51200,  8, 1|2    |8    |64|128      , 200, 200, 210,   0,   "Trinium"                 ,   "Trinium"                       ,     0,       0,      7200,   7200,  true, false,  4,   1,   1, Dyes.dyeLightGray    ).disableAutoGeneratedBlastFurnaceRecipes().disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Ichorium                = new Materials( 978, TextureSet.SET_SHINY             ,  32.0F, 850000,  12, 1|2    |8 |32|64|128      , 211, 120,   6,   0,   "Ichorium"                ,   "Ichorium"                      ,     5,  250000,      9000,   9000,  true, false,  4,   1,   1, Dyes.dyeOrange       ).setTurbineMultipliers(6, 6, 3).setHasCorrespondingPlasma(true);
    public static Materials CosmicNeutronium        = new Materials( 982, TextureSet.SET_SHINY             ,  96.0F, 163840,  12, 1|2    |8 |32|64|128      ,  50,  50,  50,   0,   "CosmicNeutronium"        ,   "Cosmic Neutronium"             ,     0,       0,      9900,   9900,  true, false,  4,   1,   1, Dyes.dyeBlack        ).setProcessingMaterialTierEU(TierEU.RECIPE_ZPM).disableAutoGeneratedVacuumFreezerRecipe().setHasCorrespondingPlasma(true);

    // Superconductor base.
    public static Materials Pentacadmiummagnesiumhexaoxid                                   = new Materials( 987, TextureSet.SET_SHINY          ,   1.0F,      0,  3, 1|2                ,  85, 85,  85,   0,   "Pentacadmiummagnesiumhexaoxid"                                 ,   "Superconductor Base MV"       ,     0,       0,     2500,  2500,  true,  false,  1,   1,   1, Dyes.dyeGray       , 1, Arrays.asList(new MaterialStack(Cadmium, 5), new MaterialStack(Magnesium, 1), new MaterialStack(Oxygen, 6)), Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 3))).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Titaniumonabariumdecacoppereikosaoxid                           = new Materials( 988, TextureSet.SET_METALLIC       ,   1.0F,      0,  3, 1|2                ,  51, 25,   0,   0,   "Titaniumonabariumdecacoppereikosaoxid"                         ,   "Superconductor Base HV"       ,     0,       0,     3300,  3300,  true,  false,  1,   1,   1, Dyes.dyeBrown      , 1, Arrays.asList(new MaterialStack(Titanium, 1), new MaterialStack(Barium, 9),  new MaterialStack(Copper, 10), new MaterialStack(Oxygen, 20)), Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 6))).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Uraniumtriplatinid                                              = new Materials( 989, TextureSet.SET_SHINY          ,   1.0F,      0,  3, 1|2                ,   0,135,   0,   0,   "Uraniumtriplatinid"                                            ,   "Superconductor Base EV"       ,     0,       0,     4400,  4400,  true,  false,  1,   1,   1, Dyes.dyeLime       , 1, Arrays.asList(new MaterialStack(Uranium, 1), new MaterialStack(Platinum, 3)), Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 9))).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Vanadiumtriindinid                                              = new Materials( 990, TextureSet.SET_SHINY          ,   1.0F,      0,  3, 1|2                ,  51,  0,  51,   0,   "Vanadiumtriindinid"                                            ,   "Superconductor Base IV"       ,     0,       0,     5200,  5200,  true,  false,  1,   1,   1, Dyes.dyeMagenta    , 1, Arrays.asList(new MaterialStack(Vanadium , 1), new MaterialStack(Indium, 3)), Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 12))).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid     = new Materials( 991, TextureSet.SET_METALLIC       ,   1.0F,      0,  3, 1|2                , 153, 76,   0,   0,   "Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid"   ,   "Superconductor Base LuV"      ,     0,       0,     6000,  6000,  true,  false,  1,   1,   1, Dyes.dyeBrown      , 1, Arrays.asList(new MaterialStack(Indium, 4), new MaterialStack(Tin, 2), new MaterialStack(Barium, 2), new MaterialStack(Titanium, 1), new MaterialStack(Copper, 7), new MaterialStack(Oxygen, 14)), Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 15))).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Tetranaquadahdiindiumhexaplatiumosminid                         = new Materials( 992, TextureSet.SET_METALLIC       ,   1.0F,      0,  3, 1|2                ,  10, 10,  10,   0,   "Tetranaquadahdiindiumhexaplatiumosminid"                       ,   "Superconductor Base ZPM"      ,     0,       0,     9000,  9000,  true,  false,  1,   1,   1, Dyes.dyeBlack      , 1, Arrays.asList(new MaterialStack(Naquadah, 4), new MaterialStack(Indium, 2), new MaterialStack(Palladium, 6), new MaterialStack(Osmium, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 18))).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Longasssuperconductornameforuvwire                              = new Materials( 986, TextureSet.SET_METALLIC       ,   1.0F,      0,  3, 1|2                , 224,210,   7,   0,   "Longasssuperconductornameforuvwire"                            ,   "Superconductor Base UV"       ,     0,       0,     9900,  9900,  true,  false,  1,   1,   1, Dyes.dyeYellow     , 1, Arrays.asList(new MaterialStack(Naquadria, 4), new MaterialStack(Osmiridium, 3), new MaterialStack(Europium, 1), new MaterialStack(Samarium, 1)), Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 21))).setProcessingMaterialTierEU(TierEU.RECIPE_LuV).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials Longasssuperconductornameforuhvwire                             = new Materials( 985, TextureSet.SET_SHINY          ,   1.0F,      0,  3, 1|2                ,  38,129, 189,   0,   "Longasssuperconductornameforuhvwire"                           ,   "Superconductor Base UHV"      ,     0,       0,     10800,  10800,  true,  false,  1,   1,   1, Dyes.dyeWhite      , 1, Arrays.asList(new MaterialStack(Draconium, 6), new MaterialStack(CosmicNeutronium, 7), new MaterialStack(Tritanium, 5), new MaterialStack(Americium, 6)), Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 24))).setProcessingMaterialTierEU(TierEU.RECIPE_ZPM).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials SuperconductorUEVBase                                           = new Materials( 974, TextureSet.SET_SHINY          ,   1.0F,      0,  3, 1|2                ,  174, 8,   8,   0,   "SuperconductorUEVBase"                                         ,   "Superconductor Base UEV"      ,     0,       0,     11700,  11800,  true,  false,  1,   1,   1, Dyes.dyeWhite, Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 27))).setProcessingMaterialTierEU(TierEU.RECIPE_UV).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials SuperconductorUIVBase                                           = new Materials( 131, TextureSet.SET_SHINY          ,   1.0F,      0,  3, 1|2                ,  229, 88,   177,   0,   "SuperconductorUIVBase"                                         ,   "Superconductor Base UIV"      ,     0,       0,     12700,  12700,  true,  false,  1,   1,   1, Dyes.dyeWhite, Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 34))).setProcessingMaterialTierEU(TierEU.RECIPE_UHV).disableAutoGeneratedVacuumFreezerRecipe();
    public static Materials SuperconductorUMVBase                                           = new Materials( 134, TextureSet.SET_SHINY          ,   1.0F,      0,  3, 1|2                ,  181, 38,   205,   0,   "SuperconductorUMVBase"                                         ,   "Superconductor Base UMV"      ,     0,       0,     13600,  13600,  true,  false,  1,   1,   1, Dyes.dyeWhite, Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 40))).setProcessingMaterialTierEU(TierEU.RECIPE_UEV).disableAutoGeneratedVacuumFreezerRecipe();

    // Superconductors.
    public static Materials SuperconductorMV      = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                ,  85, 85,  85,   0,   "SuperconductorMV"   ,   "Superconductor MV"       ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeGray       , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 6)));
    public static Materials SuperconductorHV      = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                ,  51, 25,   0,   0,   "SuperconductorHV"   ,   "Superconductor HV"       ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeBrown      , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 12)));
    public static Materials SuperconductorEV      = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                ,   0,135,   0,   0,   "SuperconductorEV"   ,   "Superconductor EV"       ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeLime       , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 18)));
    public static Materials SuperconductorIV      = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                ,  51,  0,  51,   0,   "SuperconductorIV"   ,   "Superconductor IV"       ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeMagenta    , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 24)));
    public static Materials SuperconductorLuV     = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                , 153, 76,   0,   0,   "SuperconductorLuV"  ,   "Superconductor LuV"      ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeBrown      , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 30)));
    public static Materials SuperconductorZPM     = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                ,  10, 10,  10,   0,   "SuperconductorZPM"  ,   "Superconductor ZPM"      ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeBlack      , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 36)));
    public static Materials SuperconductorUV      = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                , 224,210,   7,   0,   "SuperconductorUV"   ,   "Superconductor UV"       ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeYellow     , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 42)));
    public static Materials SuperconductorUHV     = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                ,  38,129, 189,   0,   "Superconductor"     ,   "Superconductor UHV"      ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeWhite      , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 48)));
    public static Materials SuperconductorUEV     = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                , 174,  8,   8,   0,   "SuperconductorUEV"  ,   "Superconductor UEV"      ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeWhite      , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 54)));
    public static Materials SuperconductorUIV     = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                , 229, 88, 177,   0,   "SuperconductorUIV"  ,   "Superconductor UIV"      ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeWhite      , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 60)));
    public static Materials SuperconductorUMV     = new Materials( -1, TextureSet.SET_SHINY       ,   1.0F,      0,  0, 0                , 181, 38, 205,   0,   "SuperconductorUMV"  ,   "Superconductor UMV"      ,     0,       0,     -1,  -1,  false,  false,  1,   1,   1, Dyes.dyeWhite      , Collections.singletonList(new TC_AspectStack(TC_Aspects.ELECTRUM, 66)));

    public static Materials SuperCoolant = new MaterialBuilder( 140, TextureSet.SET_DULL,"Super Coolant").setRGB(2, 91, 111).addCell().addFluid().constructMaterial().setLiquidTemperature(1);

    public static Materials EnrichedHolmium       = new Materials(582, TextureSet.SET_METALLIC    ,   1.0F,      0,  2,    2             ,  18,  100, 255,   0,   "EnrichedHolmium"    ,   "Enriched Holmium"        ,    -1,      -1,      0, 3000,  true,  false,200,   1,   1, Dyes.dyePurple);

    public static Materials TengamPurified = new MaterialBuilder(111, TextureSet.SET_METALLIC, "Purified Tengam").addDustItems().addGearItems().addMetalItems().addToolHeadItems().setAspects(Arrays.asList(new TC_AspectStack(TC_Aspects.MAGNETO, 2), new TC_AspectStack(TC_Aspects.ELECTRUM, 2))).setColor(Dyes.dyeLime).setName("TengamPurified").setRGB(186, 223, 112).constructMaterial().setProcessingMaterialTierEU(TierEU.RECIPE_UV);
    public static Materials TengamAttuned  = new MaterialBuilder(112, TextureSet.SET_MAGNETIC, "Attuned Tengam") .addDustItems().addGearItems().addMetalItems().addToolHeadItems().setAspects(Arrays.asList(new TC_AspectStack(TC_Aspects.MAGNETO, 4), new TC_AspectStack(TC_Aspects.ELECTRUM, 1))).setColor(Dyes.dyeLime).setName("TengamAttuned") .setRGB(213, 255, 128).constructMaterial().setProcessingMaterialTierEU(TierEU.RECIPE_UV);
    public static Materials TengamRaw      = new MaterialBuilder(110, TextureSet.SET_ROUGH,    "Raw Tengam")     .addOreItems()                                                   .setAspects(Arrays.asList(new TC_AspectStack(TC_Aspects.MAGNETO, 1), new TC_AspectStack(TC_Aspects.ELECTRUM, 4))).setColor(Dyes.dyeLime).setName("TengamRaw")     .setRGB(160, 191,  96).constructMaterial().setProcessingMaterialTierEU(TierEU.RECIPE_UV);

    // Activated Carbon Line
    public static Materials ActivatedCarbon = new MaterialBuilder(563, TextureSet.SET_DULL, "Activated Carbon")
        .addDustItems()
        .setRGB(20, 20, 20)
        .setName("ActivatedCarbon")
        .setOreValue(0)
        .setMaterialList(new MaterialStack(Carbon, 1))
        .constructMaterial()
        .disableAutoGeneratedRecycleRecipes();
    public static Materials PreActivatedCarbon = new MaterialBuilder(564, TextureSet.SET_DULL, "Pre-Activated Carbon")
        .addDustItems()
        .setRGB(15, 51, 65)
        .setName("PreActivatedCarbon")
        .setOreValue(0)
        .setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(PhosphoricAcid, 1))
        .constructMaterial()
        .disableAutoGeneratedRecycleRecipes();
    public static Materials DirtyActivatedCarbon = new MaterialBuilder(565, TextureSet.SET_DULL, "Dirty Activated Carbon")
        .addDustItems()
        .setRGB(110, 110, 110)
        .setName("carbonactivateddirty") // don't change this to the more sensible name or a centrifuge recipe appears
        .setOreValue(0)
        .setMaterialList(new MaterialStack(Carbon, 1), new MaterialStack(PhosphoricAcid, 1))
        .constructMaterial()
        .disableAutoGeneratedRecycleRecipes();

    // Advanced glue uses id 567?? ok
    public static Materials PolyAluminiumChloride = new MaterialBuilder(566, TextureSet.SET_FLUID, "Polyaluminium Chloride")
        .addFluid()
        .addCell()
        .setRGB(252, 236, 5)
        .setName("PolyaluminiumChloride")
        .constructMaterial();

    public static Materials Ozone = new MaterialBuilder(568, TextureSet.SET_FLUID, "Ozone")
        .addGas()
        .addCell()
        .setRGB(190, 244, 250)
        .setName("Ozone")
        .setMaterialList(new MaterialStack(Oxygen, 3))
        .constructMaterial();

    public static Materials StableBaryonicMatter = new MaterialBuilder(569,  new TextureSet("stablebaryonicmatter", true), "Stabilised Baryonic Matter")
        .addFluid()
        .addCell()
        .setRGBA(255, 255, 255, 0)
        .setTransparent(true)
        .setName("stablebaryonicmatter")
        .setColor(Dyes._NULL)
        .constructMaterial()
        .setHasCorrespondingFluid(true);

    // spotless:on

    static {
        MaterialsBotania.init();
    }

    static {
        MaterialsKevlar.init();
    }

    static {
        MaterialsOreAlum.init();
    }

    static {
        MaterialsUEVplus.init();
    }

    /**
     * Superconductor re-routed for mod compatibility. Circuits are re-routed into SuperconductorUHV as well.
     * <p>
     * Internal name is now Superconductor while translated name is SuperconductorUHV.
     * </p>
     *
     * @deprecated Use {@link #SuperconductorUHV} instead
     */
    @Deprecated
    public static Materials Superconductor = new Materials(SuperconductorUHV, true); // new Materials( -1,
                                                                                     // TextureSet.SET_NONE , 1.0F, 0,
                                                                                     // 0, 0
    // , 255, 255, 255, 0, "Superconductor" , "Superconductor" , 0,
    // 0, -1, 0, false, false, 1, 1, 1, Dyes.dyeLightGray , Arrays.asList(new
    // TC_AspectStack(TC_Aspects.ELECTRUM, 9)));

    private static Materials[] MATERIALS_ARRAY = new Materials[] {};

    static {
        // Load all materials, this has been split into different classes to make
        // class too large errors disappear
        MaterialsInit1.load();

        initSubTags();

        setReRegistration();
        setMaceratingInto();
        setSmeltingInto();
        setDirectSmelting();
        setOthers();
        setMultipliers();
        setEnchantments();
        setHeatDamage();
        setByProducts();
        setColors();

        overrideChemicalFormulars();
    }

    public final short[] mRGBa = new short[] { 255, 255, 255, 0 }, mMoltenRGBa = new short[] { 255, 255, 255, 0 };
    public TextureSet mIconSet;
    public GT_GeneratedMaterial_Renderer renderer;
    public List<MaterialStack> mMaterialList = new ArrayList<>();
    public List<Materials> mOreByProducts = new ArrayList<>(), mOreReRegistrations = new ArrayList<>();
    public List<TC_Aspects.TC_AspectStack> mAspects = new ArrayList<>();
    public ArrayList<ItemStack> mMaterialItems = new ArrayList<>();
    public Collection<SubTag> mSubTags = new LinkedHashSet<>();
    public Enchantment mEnchantmentTools = null, mEnchantmentArmors = null;
    public boolean mUnificatable, mBlastFurnaceRequired = false, mAutoGenerateBlastFurnaceRecipes = true,
        mAutoGenerateVacuumFreezerRecipes = true, mAutoGenerateRecycleRecipes = true, mTransparent = false,
        mHasParentMod = true, mHasPlasma = false, mHasGas = false, mCustomOre = false;
    public byte mEnchantmentToolsLevel = 0, mEnchantmentArmorsLevel = 0, mToolQuality = 0;
    public short mBlastFurnaceTemp = 0;
    public int mMeltingPoint = 0;
    public int mGasTemp = 0;
    public int mMetaItemSubID;
    public int mTypes = 0;
    public int mDurability = 16;
    public int mFuelPower = 0;
    public int mFuelType = 0;
    public int mExtraData = 0;
    public int mOreValue = 0;
    public int mOreMultiplier = 1;
    public int mByProductMultiplier = 1;
    public int mSmeltingMultiplier = 1;
    public int mDensityMultiplier = 1;
    public int mDensityDivider = 1;

    public int getProcessingMaterialTierEU() {
        return processingMaterialTierEU;
    }

    public Materials setProcessingMaterialTierEU(final long processingMaterialTierEU) {
        this.processingMaterialTierEU = (int) processingMaterialTierEU;
        return this;
    }

    public int processingMaterialTierEU = 0;
    public long mDensity = M;
    public float mToolSpeed = 1.0F, mHeatDamage = 0.0F, mSteamMultiplier = 1.0F, mGasMultiplier = 1.0F,
        mPlasmaMultiplier = 1.0F;
    public String mChemicalFormula = "?", mName, mDefaultLocalName, mCustomID = "null", mConfigSection = "null",
        mLocalizedName = "null";
    public Dyes mColor = Dyes._NULL;
    public Element mElement = null;
    public Materials mDirectSmelting = this, mOreReplacement = this, mMacerateInto = this, mSmeltInto = this,
        mArcSmeltInto = this, mHandleMaterial = this, mMaterialInto;
    public Fluid mSolid = null, mFluid = null, mGas = null, mPlasma = null;
    /**
     * This Fluid is used as standard Unit for Molten Materials. 1296 is a Molten Block, that means 144 is one Material
     * Unit worth of fluid.
     */
    public Fluid mStandardMoltenFluid = null;

    private boolean hasCorrespondingFluid = false, hasCorrespondingGas = false, canBeCracked = false;
    private Fluid[] hydroCrackedFluids = new Fluid[3], steamCrackedFluids = new Fluid[3];

    public Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality,
        boolean aUnificatable, String aName, String aDefaultLocalName) {
        this(
            aMetaItemSubID,
            aIconSet,
            aToolSpeed,
            aDurability,
            aToolQuality,
            aUnificatable,
            aName,
            aDefaultLocalName,
            "ore",
            false,
            "null");
    }

    public Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality,
        boolean aUnificatable, String aName, String aDefaultLocalName, String aConfigSection, boolean aCustomOre,
        String aCustomID) {
        mMetaItemSubID = aMetaItemSubID;
        mDefaultLocalName = aDefaultLocalName;
        mName = aName;
        MATERIALS_MAP.put(mName, this);
        mCustomOre = aCustomOre;
        mCustomID = aCustomID;
        mConfigSection = aConfigSection;
        mUnificatable = aUnificatable;
        mDurability = aDurability;
        mToolSpeed = aToolSpeed;
        mToolQuality = (byte) aToolQuality;
        mMaterialInto = this;
        mIconSet = aIconSet;
    }

    public Materials(Materials aMaterialInto, boolean aReRegisterIntoThis) {
        mUnificatable = false;
        mDefaultLocalName = aMaterialInto.mDefaultLocalName;
        mName = aMaterialInto.mName;
        mMaterialInto = aMaterialInto.mMaterialInto;
        if (aReRegisterIntoThis) mMaterialInto.mOreReRegistrations.add(this);
        mChemicalFormula = aMaterialInto.mChemicalFormula;
        mMetaItemSubID = -1;
        mIconSet = TextureSet.SET_NONE;
    }

    public Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality,
        int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType,
        int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent,
        int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor) {
        this(
            aMetaItemSubID,
            aIconSet,
            aToolSpeed,
            aDurability,
            aToolQuality,
            aTypes,
            aR,
            aG,
            aB,
            aA,
            aName,
            aDefaultLocalName,
            aFuelType,
            aFuelPower,
            aMeltingPoint,
            aBlastFurnaceTemp,
            aBlastFurnaceRequired,
            aTransparent,
            aOreValue,
            aDensityMultiplier,
            aDensityDivider,
            aColor,
            "ore",
            false,
            "null");
    }

    public Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality,
        int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType,
        int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent,
        int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, String aConfigSection) {
        this(
            aMetaItemSubID,
            aIconSet,
            aToolSpeed,
            aDurability,
            aToolQuality,
            aTypes,
            aR,
            aG,
            aB,
            aA,
            aName,
            aDefaultLocalName,
            aFuelType,
            aFuelPower,
            aMeltingPoint,
            aBlastFurnaceTemp,
            aBlastFurnaceRequired,
            aTransparent,
            aOreValue,
            aDensityMultiplier,
            aDensityDivider,
            aColor,
            aConfigSection,
            false,
            "null");
    }

    /**
     * @param aMetaItemSubID        the Sub-ID used in my own MetaItems. Range 0-1000. -1 for no Material
     * @param aTypes                which kind of Items should be generated. Bitmask as follows: 1 = Dusts of all kinds.
     *                              2 = Dusts, Ingots, Plates, Rods/Sticks, Machine Components and other Metal specific
     *                              things. 4 = Dusts, Gems, Plates, Lenses (if transparent). 8 = Dusts, Impure Dusts,
     *                              crushed Ores, purified Ores, centrifuged Ores etc. 16 = Cells 32 = Plasma Cells 64 =
     *                              Tool Heads 128 = Gears 256 = Designates something as empty (only used for the Empty
     *                              material)
     * @param aR,                   aG, aB Color of the Material 0-255 each.
     * @param aA                    transparency of the Material Texture. 0 = fully visible, 255 = Invisible.
     * @param aName                 The Name used as Default for localization.
     * @param aFuelType             Type of Generator to get Energy from this Material.
     * @param aFuelPower            EU generated. Will be multiplied by 1000, also additionally multiplied by 2 for
     *                              Gems.
     * @param aMeltingPoint         Used to determine the smelting Costs in furnace. >>>>**ADD 20000 to remove EBF
     *                              recipes to add them MANUALLY ! :D**<<<<
     * @param aBlastFurnaceTemp     Used to determine the needed Heat capacity Costs in Blast Furnace.
     * @param aBlastFurnaceRequired If this requires a Blast Furnace.
     * @param aColor                Vanilla MC Wool Color which comes the closest to this.
     */
    public Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality,
        int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType,
        int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent,
        int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, String aConfigSection,
        boolean aCustomOre, String aCustomID) {
        this(
            aMetaItemSubID,
            aIconSet,
            aToolSpeed,
            aDurability,
            aToolQuality,
            true,
            aName,
            aDefaultLocalName,
            aConfigSection,
            aCustomOre,
            aCustomID);
        mMeltingPoint = aMeltingPoint;
        mBlastFurnaceRequired = aBlastFurnaceRequired;
        mBlastFurnaceTemp = (short) aBlastFurnaceTemp;
        mTransparent = aTransparent;
        mFuelPower = aFuelPower;
        mFuelType = aFuelType;
        mOreValue = aOreValue;
        mDensityMultiplier = aDensityMultiplier;
        mDensityDivider = aDensityDivider;
        mDensity = (M * aDensityMultiplier) / aDensityDivider;
        mColor = aColor;
        mRGBa[0] = mMoltenRGBa[0] = (short) aR;
        mRGBa[1] = mMoltenRGBa[1] = (short) aG;
        mRGBa[2] = mMoltenRGBa[2] = (short) aB;
        mRGBa[3] = mMoltenRGBa[3] = (short) aA;
        mTypes = aTypes;
        if (mColor != null) add(SubTag.HAS_COLOR);
        if (mTransparent) add(SubTag.TRANSPARENT);
        if ((mTypes & 2) != 0) add(SubTag.SMELTING_TO_FLUID);
    }

    public Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality,
        int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType,
        int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent,
        int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor,
        List<TC_Aspects.TC_AspectStack> aAspects) {
        this(
            aMetaItemSubID,
            aIconSet,
            aToolSpeed,
            aDurability,
            aToolQuality,
            aTypes,
            aR,
            aG,
            aB,
            aA,
            aName,
            aDefaultLocalName,
            aFuelType,
            aFuelPower,
            aMeltingPoint,
            aBlastFurnaceTemp,
            aBlastFurnaceRequired,
            aTransparent,
            aOreValue,
            aDensityMultiplier,
            aDensityDivider,
            aColor);
        mAspects.addAll(aAspects);
    }

    public Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality,
        int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType,
        int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent,
        int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, Element aElement,
        List<TC_Aspects.TC_AspectStack> aAspects) {
        this(
            aMetaItemSubID,
            aIconSet,
            aToolSpeed,
            aDurability,
            aToolQuality,
            aTypes,
            aR,
            aG,
            aB,
            aA,
            aName,
            aDefaultLocalName,
            aFuelType,
            aFuelPower,
            aMeltingPoint,
            aBlastFurnaceTemp,
            aBlastFurnaceRequired,
            aTransparent,
            aOreValue,
            aDensityMultiplier,
            aDensityDivider,
            aColor);
        mElement = aElement;
        mElement.mLinkedMaterials.add(this);
        if (aElement == Element._NULL) {
            mChemicalFormula = "Empty";
        } else {
            mChemicalFormula = aElement.toString();
            mChemicalFormula = mChemicalFormula.replaceAll("_", "-");
        }
        mAspects.addAll(aAspects);
    }

    public Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality,
        int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType,
        int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent,
        int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, int aExtraData,
        List<MaterialStack> aMaterialList) {
        this(
            aMetaItemSubID,
            aIconSet,
            aToolSpeed,
            aDurability,
            aToolQuality,
            aTypes,
            aR,
            aG,
            aB,
            aA,
            aName,
            aDefaultLocalName,
            aFuelType,
            aFuelPower,
            aMeltingPoint,
            aBlastFurnaceTemp,
            aBlastFurnaceRequired,
            aTransparent,
            aOreValue,
            aDensityMultiplier,
            aDensityDivider,
            aColor,
            aExtraData,
            aMaterialList,
            null);
    }

    public Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality,
        int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType,
        int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent,
        int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, int aExtraData,
        List<MaterialStack> aMaterialList, List<TC_Aspects.TC_AspectStack> aAspects) {
        this(
            aMetaItemSubID,
            aIconSet,
            aToolSpeed,
            aDurability,
            aToolQuality,
            aTypes,
            aR,
            aG,
            aB,
            aA,
            aName,
            aDefaultLocalName,
            aFuelType,
            aFuelPower,
            aMeltingPoint,
            aBlastFurnaceTemp,
            aBlastFurnaceRequired,
            aTransparent,
            aOreValue,
            aDensityMultiplier,
            aDensityDivider,
            aColor);
        mExtraData = aExtraData;
        mMaterialList.addAll(aMaterialList);
        if (mMaterialList.size() == 1) mChemicalFormula = mMaterialList.get(0)
            .toString(true);
        else mChemicalFormula = mMaterialList.stream()
            .map(MaterialStack::toString)
            .collect(Collectors.joining())
            .replaceAll("_", "-");

        int tAmountOfComponents = 0, tMeltingPoint = 0;
        for (MaterialStack tMaterial : mMaterialList) {
            tAmountOfComponents += tMaterial.mAmount;
            if (tMaterial.mMaterial.mMeltingPoint > 0)
                tMeltingPoint += tMaterial.mMaterial.mMeltingPoint * tMaterial.mAmount;
            if (aAspects == null) for (TC_Aspects.TC_AspectStack tAspect : tMaterial.mMaterial.mAspects)
                tAspect.addToAspectList(mAspects);
        }

        if (mMeltingPoint < 0) mMeltingPoint = (short) (tMeltingPoint / tAmountOfComponents);

        tAmountOfComponents *= aDensityMultiplier;
        tAmountOfComponents /= aDensityDivider;
        if (aAspects == null) for (TC_Aspects.TC_AspectStack tAspect : mAspects)
            tAspect.mAmount = Math.max(1, tAspect.mAmount / Math.max(1, tAmountOfComponents));
        else mAspects.addAll(aAspects);
    }

    private static void setSmeltingInto() {
        SamariumMagnetic.setSmeltingInto(Samarium)
            .setMaceratingInto(Samarium)
            .setArcSmeltingInto(Samarium);
        NeodymiumMagnetic.setSmeltingInto(Neodymium)
            .setMaceratingInto(Neodymium)
            .setArcSmeltingInto(Neodymium);
        SteelMagnetic.setSmeltingInto(Steel)
            .setMaceratingInto(Steel)
            .setArcSmeltingInto(Steel);
        Iron.setSmeltingInto(Iron)
            .setMaceratingInto(Iron)
            .setArcSmeltingInto(WroughtIron);
        AnyIron.setSmeltingInto(Iron)
            .setMaceratingInto(Iron)
            .setArcSmeltingInto(WroughtIron);
        PigIron.setSmeltingInto(Iron)
            .setMaceratingInto(Iron)
            .setArcSmeltingInto(WroughtIron);
        WroughtIron.setSmeltingInto(WroughtIron)
            .setMaceratingInto(WroughtIron)
            .setArcSmeltingInto(WroughtIron);
        IronMagnetic.setSmeltingInto(Iron)
            .setMaceratingInto(Iron)
            .setArcSmeltingInto(WroughtIron);
        Copper.setSmeltingInto(Copper)
            .setMaceratingInto(Copper)
            .setArcSmeltingInto(AnnealedCopper);
        AnyCopper.setSmeltingInto(Copper)
            .setMaceratingInto(Copper)
            .setArcSmeltingInto(AnnealedCopper);
        AnnealedCopper.setSmeltingInto(AnnealedCopper)
            .setMaceratingInto(AnnealedCopper)
            .setArcSmeltingInto(AnnealedCopper);
        Netherrack.setSmeltingInto(NetherBrick);
        MeatRaw.setSmeltingInto(MeatCooked);
        Sand.setSmeltingInto(Glass);
        Ice.setSmeltingInto(Water);
        Snow.setSmeltingInto(Water);
        TengamAttuned.setSmeltingInto(TengamPurified)
            .setMaceratingInto(TengamPurified)
            .setArcSmeltingInto(TengamPurified);
    }

    private static void setOthers() {
        Mercury.add(SubTag.SMELTING_TO_GEM);
        BandedIron.setOreReplacement(RoastedIron);
        Garnierite.setOreReplacement(RoastedNickel);
    }

    private static void setDirectSmelting() {
        Cinnabar.setDirectSmelting(Mercury)
            .add(SubTag.INDUCTIONSMELTING_LOW_OUTPUT)
            .add(SubTag.SMELTING_TO_GEM);
        Tetrahedrite.setDirectSmelting(Copper)
            .add(SubTag.INDUCTIONSMELTING_LOW_OUTPUT)
            .add(SubTag.DONT_ADD_DEFAULT_BBF_RECIPE);
        Chalcopyrite.setDirectSmelting(Copper)
            .add(SubTag.INDUCTIONSMELTING_LOW_OUTPUT)
            .add(SubTag.DONT_ADD_DEFAULT_BBF_RECIPE);
        Malachite.setDirectSmelting(Copper)
            .add(SubTag.INDUCTIONSMELTING_LOW_OUTPUT);
        Pentlandite.setDirectSmelting(Nickel)
            .add(SubTag.INDUCTIONSMELTING_LOW_OUTPUT);
        Sphalerite.setDirectSmelting(Zinc)
            .add(SubTag.INDUCTIONSMELTING_LOW_OUTPUT);
        Pyrite.setDirectSmelting(Iron)
            .add(SubTag.INDUCTIONSMELTING_LOW_OUTPUT);
        BasalticMineralSand.setDirectSmelting(Iron)
            .add(SubTag.INDUCTIONSMELTING_LOW_OUTPUT);
        GraniticMineralSand.setDirectSmelting(Iron)
            .add(SubTag.INDUCTIONSMELTING_LOW_OUTPUT);
        YellowLimonite.setDirectSmelting(Iron)
            .add(SubTag.INDUCTIONSMELTING_LOW_OUTPUT);
        BrownLimonite.setDirectSmelting(Iron);
        BandedIron.setDirectSmelting(Iron);
        Magnetite.setDirectSmelting(Iron);
        Cassiterite.setDirectSmelting(Tin);
        CassiteriteSand.setDirectSmelting(Tin);
        Chromite.setDirectSmelting(Chrome);
        Garnierite.setDirectSmelting(Nickel);
        Cobaltite.setDirectSmelting(Cobalt);
        Stibnite.setDirectSmelting(Antimony);
        Cooperite.setDirectSmelting(Platinum)
            .add(SubTag.DONT_ADD_DEFAULT_BBF_RECIPE);
        Molybdenite.setDirectSmelting(Molybdenum)
            .add(SubTag.DONT_ADD_DEFAULT_BBF_RECIPE);
        Galena.setDirectSmelting(Lead);
        RoastedIron.setDirectSmelting(Iron);
        RoastedAntimony.setDirectSmelting(Antimony);
        RoastedLead.setDirectSmelting(Lead);
        RoastedArsenic.setDirectSmelting(Arsenic);
        RoastedCobalt.setDirectSmelting(Cobalt);
        RoastedZinc.setDirectSmelting(Zinc);
        RoastedNickel.setDirectSmelting(Nickel);
        RoastedCopper.setDirectSmelting(Copper);
    }

    private static void setMultipliers() {
        Amber.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        InfusedAir.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        InfusedFire.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        InfusedEarth.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        InfusedWater.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        InfusedEntropy.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        InfusedOrder.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        InfusedVis.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        InfusedDull.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        Salt.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        RockSalt.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        Scheelite.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        Tungstate.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        Cassiterite.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        CassiteriteSand.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        NetherQuartz.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        CertusQuartz.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        CertusQuartzCharged.setOreMultiplier(2)
            .setSmeltingMultiplier(2);
        TricalciumPhosphate.setOreMultiplier(3)
            .setSmeltingMultiplier(3);
        Saltpeter.setOreMultiplier(4)
            .setSmeltingMultiplier(4);
        Apatite.setOreMultiplier(4)
            .setSmeltingMultiplier(4)
            .setByProductMultiplier(2);
        Electrotine.setOreMultiplier(5)
            .setSmeltingMultiplier(5);
        Teslatite.setOreMultiplier(5)
            .setSmeltingMultiplier(5);
        Redstone.setOreMultiplier(5)
            .setSmeltingMultiplier(5);
        Glowstone.setOreMultiplier(5)
            .setSmeltingMultiplier(5);
        Lapis.setOreMultiplier(6)
            .setSmeltingMultiplier(6)
            .setByProductMultiplier(4);
        Sodalite.setOreMultiplier(6)
            .setSmeltingMultiplier(6)
            .setByProductMultiplier(4);
        Lazurite.setOreMultiplier(6)
            .setSmeltingMultiplier(6)
            .setByProductMultiplier(4);
        Monazite.setOreMultiplier(8)
            .setSmeltingMultiplier(8)
            .setByProductMultiplier(2);
        Cryolite.setOreMultiplier(4)
            .setByProductMultiplier(4);
    }

    private static void setEnchantmentKnockbackTools() {
        Plastic.setEnchantmentForTools(Enchantment.knockback, 1);
        PolyvinylChloride.setEnchantmentForTools(Enchantment.knockback, 1);
        Polystyrene.setEnchantmentForTools(Enchantment.knockback, 1);
        Rubber.setEnchantmentForTools(Enchantment.knockback, 2);
        StyreneButadieneRubber.setEnchantmentForTools(Enchantment.knockback, 2);
        InfusedAir.setEnchantmentForTools(Enchantment.knockback, 2);
    }

    private static void setEnchantmentFortuneTools() {
        IronWood.setEnchantmentForTools(Enchantment.fortune, 1);
        Steeleaf.setEnchantmentForTools(Enchantment.fortune, 2);
        Mithril.setEnchantmentForTools(Enchantment.fortune, 3);
        Vinteum.setEnchantmentForTools(Enchantment.fortune, 1);
        Thaumium.setEnchantmentForTools(Enchantment.fortune, 2);
        InfusedWater.setEnchantmentForTools(Enchantment.fortune, 3);
    }

    private static void setEnchantmentFireAspectTools() {
        Flint.setEnchantmentForTools(Enchantment.fireAspect, 1);
        DarkIron.setEnchantmentForTools(Enchantment.fireAspect, 2);
        Firestone.setEnchantmentForTools(Enchantment.fireAspect, 3);
        FierySteel.setEnchantmentForTools(Enchantment.fireAspect, 3);
        Pyrotheum.setEnchantmentForTools(Enchantment.fireAspect, 3);
        Blaze.setEnchantmentForTools(Enchantment.fireAspect, 3);
        InfusedFire.setEnchantmentForTools(Enchantment.fireAspect, 3);
    }

    private static void setEnchantmentSilkTouchTools() {
        Force.setEnchantmentForTools(Enchantment.silkTouch, 1);
        Amber.setEnchantmentForTools(Enchantment.silkTouch, 1);
        EnderPearl.setEnchantmentForTools(Enchantment.silkTouch, 1);
        Enderium.setEnchantmentForTools(Enchantment.silkTouch, 1);
        NetherStar.setEnchantmentForTools(Enchantment.silkTouch, 1);
        InfusedOrder.setEnchantmentForTools(Enchantment.silkTouch, 1);
    }

    private static void setEnchantmentSmiteTools() {
        BlackBronze.setEnchantmentForTools(Enchantment.smite, 2);
        Gold.setEnchantmentForTools(Enchantment.smite, 3);
        RoseGold.setEnchantmentForTools(Enchantment.smite, 4);
        Platinum.setEnchantmentForTools(Enchantment.smite, 5);
        InfusedVis.setEnchantmentForTools(Enchantment.smite, 5);
        Ichorium.setEnchantmentForTools(Enchantment.smite, 8);
    }

    private static void setEnchantmentBaneOfArthropodsTools() {
        Lead.setEnchantmentForTools(Enchantment.baneOfArthropods, 2);
        Nickel.setEnchantmentForTools(Enchantment.baneOfArthropods, 2);
        Invar.setEnchantmentForTools(Enchantment.baneOfArthropods, 3);
        Antimony.setEnchantmentForTools(Enchantment.baneOfArthropods, 3);
        BatteryAlloy.setEnchantmentForTools(Enchantment.baneOfArthropods, 4);
        Bismuth.setEnchantmentForTools(Enchantment.baneOfArthropods, 4);
        BismuthBronze.setEnchantmentForTools(Enchantment.baneOfArthropods, 5);
        InfusedEarth.setEnchantmentForTools(Enchantment.baneOfArthropods, 5);
    }

    private static void setEnchantmentSharpnessTools() {
        Iron.setEnchantmentForTools(Enchantment.sharpness, 1);
        Bronze.setEnchantmentForTools(Enchantment.sharpness, 1);
        Brass.setEnchantmentForTools(Enchantment.sharpness, 2);
        HSLA.setEnchantmentForTools(Enchantment.sharpness, 2);
        Steel.setEnchantmentForTools(Enchantment.sharpness, 2);
        WroughtIron.setEnchantmentForTools(Enchantment.sharpness, 2);
        StainlessSteel.setEnchantmentForTools(Enchantment.sharpness, 3);
        Knightmetal.setEnchantmentForTools(Enchantment.sharpness, 3);
        ShadowIron.setEnchantmentForTools(Enchantment.sharpness, 3);
        ShadowSteel.setEnchantmentForTools(Enchantment.sharpness, 4);
        BlackSteel.setEnchantmentForTools(Enchantment.sharpness, 4);
        RedSteel.setEnchantmentForTools(Enchantment.sharpness, 4);
        BlueSteel.setEnchantmentForTools(Enchantment.sharpness, 5);
        DamascusSteel.setEnchantmentForTools(Enchantment.sharpness, 5);
        InfusedEntropy.setEnchantmentForTools(Enchantment.sharpness, 5);
        TungstenCarbide.setEnchantmentForTools(Enchantment.sharpness, 5);
        HSSE.setEnchantmentForTools(Enchantment.sharpness, 5);
        HSSG.setEnchantmentForTools(Enchantment.sharpness, 4);
        HSSS.setEnchantmentForTools(Enchantment.sharpness, 5);
    }

    /**
     * DO NOT ADD MORE THAN 1 TOOL AND ARMOR ENCHANTMENT PER MATERIAL! It will get overwritten!
     */
    private static void setEnchantments() {
        setToolEnchantments();
        setArmorEnchantments();
    }

    private static void setToolEnchantments() {
        setEnchantmentKnockbackTools();
        setEnchantmentFortuneTools();
        setEnchantmentFireAspectTools();
        setEnchantmentSilkTouchTools();
        setEnchantmentSmiteTools();
        setEnchantmentBaneOfArthropodsTools();
        setEnchantmentSharpnessTools();
    }

    private static void setArmorEnchantments() {
        InfusedAir.setEnchantmentForArmors(Enchantment.respiration, 3);

        InfusedFire.setEnchantmentForArmors(Enchantment.featherFalling, 4);

        Steeleaf.setEnchantmentForArmors(Enchantment.protection, 2);
        Knightmetal.setEnchantmentForArmors(Enchantment.protection, 1);
        InfusedEarth.setEnchantmentForArmors(Enchantment.protection, 4);

        InfusedEntropy.setEnchantmentForArmors(Enchantment.thorns, 3);

        InfusedWater.setEnchantmentForArmors(Enchantment.aquaAffinity, 1);
        IronWood.setEnchantmentForArmors(Enchantment.aquaAffinity, 1);

        InfusedOrder.setEnchantmentForArmors(Enchantment.projectileProtection, 4);

        InfusedDull.setEnchantmentForArmors(Enchantment.blastProtection, 4);

        InfusedVis.setEnchantmentForArmors(Enchantment.protection, 4);
    }

    private static void setMaceratingInto() {
        Peanutwood.setMaceratingInto(Wood);
        WoodSealed.setMaceratingInto(Wood);
        NetherBrick.setMaceratingInto(Netherrack);
        AnyRubber.setMaceratingInto(Rubber);
    }

    private static void setReRegistration() {
        Iron.mOreReRegistrations.add(AnyIron);
        PigIron.mOreReRegistrations.add(AnyIron);
        WroughtIron.mOreReRegistrations.add(AnyIron);
        Copper.mOreReRegistrations.add(AnyCopper);
        AnnealedCopper.mOreReRegistrations.add(AnyCopper);
        Bronze.mOreReRegistrations.add(AnyBronze);
        Rubber.mOreReRegistrations.add(AnyRubber);
        StyreneButadieneRubber.mOreReRegistrations.add(AnyRubber);
        Silicone.mOreReRegistrations.add(AnyRubber);
        StyreneButadieneRubber.mOreReRegistrations.add(AnySyntheticRubber);
        Silicone.mOreReRegistrations.add(AnySyntheticRubber);
    }

    private static void setHeatDamage() {
        FryingOilHot.setHeatDamage(1.0F);
        Lava.setHeatDamage(3.0F);
        Firestone.setHeatDamage(5.0F);
        Pyrotheum.setHeatDamage(5.0F);
    }

    private static void setByProducts() {
        Mytryl.addOreByProducts(Samarium, Samarium, Zinc, Zinc);
        Rubracium.addOreByProducts(Samarium, Samarium, Samarium, Samarium);
        Chalcopyrite.addOreByProducts(Pyrite, Cobalt, Cadmium, Gold);
        Sphalerite.addOreByProducts(GarnetYellow, Cadmium, Gallium, Zinc);
        MeteoricIron.addOreByProducts(Iron, Nickel, Iridium, Platinum);
        GlauconiteSand.addOreByProducts(Sodium, Aluminiumoxide, Iron);
        Glauconite.addOreByProducts(Sodium, Aluminiumoxide, Iron);
        Vermiculite.addOreByProducts(Iron, Aluminiumoxide, Magnesium);
        FullersEarth.addOreByProducts(Aluminiumoxide, SiliconDioxide, Magnesium);
        Bentonite.addOreByProducts(Aluminiumoxide, Calcium, Magnesium);
        Uraninite.addOreByProducts(Uranium, Thorium, Uranium235);
        Pitchblende.addOreByProducts(Thorium, Uranium, Lead);
        Galena.addOreByProducts(Sulfur, Silver, Lead);
        Lapis.addOreByProducts(Lazurite, Sodalite, Pyrite);
        Pyrite.addOreByProducts(Sulfur, TricalciumPhosphate, Iron);
        Copper.addOreByProducts(Cobalt, Gold, Nickel);
        Nickel.addOreByProducts(Cobalt, Platinum, Iron);
        GarnetRed.addOreByProducts(Spessartine, Pyrope, Almandine);
        GarnetYellow.addOreByProducts(Andradite, Grossular, Uvarovite);
        Cooperite.addOreByProducts(Palladium, Nickel, Iridium);
        Cinnabar.addOreByProducts(Redstone, Sulfur, Glowstone);
        Tantalite.addOreByProducts(Manganese, Niobium, Tantalum);
        Pollucite.addOreByProducts(Caesium, Aluminiumoxide, Rubidium);
        Chrysotile.addOreByProducts(Asbestos, SiliconDioxide, Magnesium);
        Asbestos.addOreByProducts(Asbestos, SiliconDioxide, Magnesium);
        Pentlandite.addOreByProducts(Iron, Sulfur, Cobalt);
        Uranium.addOreByProducts(Lead, Uranium235, Thorium);
        Scheelite.addOreByProducts(Manganese, Molybdenum, Calcium);
        Tungstate.addOreByProducts(Manganese, Silver, Lithium);
        Bauxite.addOreByProducts(Grossular, Rutile, Gallium);
        QuartzSand.addOreByProducts(CertusQuartz, Quartzite, Barite);
        Redstone.addOreByProducts(Cinnabar, RareEarth, Glowstone);
        Monazite.addOreByProducts(Thorium, Neodymium, RareEarth);
        Forcicium.addOreByProducts(Thorium, Neodymium, RareEarth);
        Forcillium.addOreByProducts(Thorium, Neodymium, RareEarth);
        Malachite.addOreByProducts(Copper, BrownLimonite, Calcite);
        YellowLimonite.addOreByProducts(Nickel, BrownLimonite, Cobalt);
        Lepidolite.addOreByProducts(Lithium, Caesium);
        Andradite.addOreByProducts(GarnetYellow, Iron);
        Pyrolusite.addOreByProducts(Manganese, Tantalite, Niobium)
            .add(SubTag.DONT_ADD_DEFAULT_BBF_RECIPE);
        TricalciumPhosphate.addOreByProducts(Apatite, Phosphate, Pyrochlore);
        Apatite.addOreByProducts(TricalciumPhosphate, Phosphate, Pyrochlore);
        Pyrochlore.addOreByProducts(Apatite, Calcite, Niobium);
        Quartzite.addOreByProducts(CertusQuartz, Barite);
        CertusQuartz.addOreByProducts(Quartzite, Barite);
        CertusQuartzCharged.addOreByProducts(CertusQuartz, Quartzite, Barite);
        BrownLimonite.addOreByProducts(Malachite, YellowLimonite);
        Neodymium.addOreByProducts(Monazite, RareEarth);
        Bastnasite.addOreByProducts(Neodymium, RareEarth);
        Glowstone.addOreByProducts(Redstone, Gold);
        Zinc.addOreByProducts(Tin, Gallium);
        Tungsten.addOreByProducts(Manganese, Molybdenum);
        Diatomite.addOreByProducts(BandedIron, Sapphire);
        Iron.addOreByProducts(Nickel, Tin);
        Gold.addOreByProducts(Copper, Nickel);
        Tin.addOreByProducts(Iron, Zinc);
        Antimony.addOreByProducts(Zinc, Iron);
        Silver.addOreByProducts(Lead, Sulfur);
        Lead.addOreByProducts(Silver, Sulfur);
        Thorium.addOreByProducts(Uranium, Lead);
        Plutonium.addOreByProducts(Uranium, Lead);
        Electrum.addOreByProducts(Gold, Silver);
        Electrotine.addOreByProducts(Redstone, Electrum);
        Bronze.addOreByProducts(Copper, Tin);
        Brass.addOreByProducts(Copper, Zinc);
        Coal.addOreByProducts(Lignite, Thorium);
        Ilmenite.addOreByProducts(Iron, Rutile);
        Manganese.addOreByProducts(Chrome, Iron);
        Sapphire.addOreByProducts(Aluminiumoxide, GreenSapphire);
        GreenSapphire.addOreByProducts(Aluminiumoxide, Sapphire);
        Platinum.addOreByProducts(Nickel, Iridium);
        Emerald.addOreByProducts(Beryllium, Aluminiumoxide);
        Olivine.addOreByProducts(Pyrope, Magnesium);
        Chrome.addOreByProducts(Iron, Magnesium);
        Chromite.addOreByProducts(Iron, Magnesium);
        Tetrahedrite.addOreByProducts(Antimony, Zinc);
        GarnetSand.addOreByProducts(GarnetRed, GarnetYellow);
        Magnetite.addOreByProducts(Iron, Gold);
        GraniticMineralSand.addOreByProducts(GraniteBlack, Magnetite);
        BasalticMineralSand.addOreByProducts(Basalt, Magnetite);
        Basalt.addOreByProducts(Olivine, DarkAsh);
        VanadiumMagnetite.addOreByProducts(Magnetite, Vanadium);
        Lazurite.addOreByProducts(Sodalite, Lapis);
        Sodalite.addOreByProducts(Lazurite, Lapis);
        Spodumene.addOreByProducts(Aluminiumoxide, Lithium);
        Ruby.addOreByProducts(Chrome, GarnetRed);
        Iridium.addOreByProducts(Platinum, Osmium);
        Pyrope.addOreByProducts(GarnetRed, Magnesium);
        Almandine.addOreByProducts(GarnetRed, Aluminiumoxide);
        Spessartine.addOreByProducts(GarnetRed, Manganese);
        Grossular.addOreByProducts(GarnetYellow, Calcium);
        Uvarovite.addOreByProducts(GarnetYellow, Chrome);
        Calcite.addOreByProducts(Andradite, Malachite);
        NaquadahEnriched.addOreByProducts(Naquadah, Naquadria);
        Salt.addOreByProducts(RockSalt, Borax);
        RockSalt.addOreByProducts(Salt, Borax);
        Naquadah.addOreByProducts(NaquadahEnriched);
        Molybdenite.addOreByProducts(Molybdenum);
        Stibnite.addOreByProducts(Antimony);
        Garnierite.addOreByProducts(Nickel);
        Lignite.addOreByProducts(Coal);
        Diamond.addOreByProducts(Graphite);
        Beryllium.addOreByProducts(Emerald);
        Electrotine.addOreByProducts(Diamond);
        Teslatite.addOreByProducts(Diamond);
        Magnesite.addOreByProducts(Magnesium);
        NetherQuartz.addOreByProducts(Netherrack);
        PigIron.addOreByProducts(Iron);
        DeepIron.addOreByProducts(Trinium, Iron, Trinium);
        ShadowIron.addOreByProducts(Iron);
        DarkIron.addOreByProducts(Iron);
        MeteoricIron.addOreByProducts(Iron);
        Steel.addOreByProducts(Iron);
        HSLA.addOreByProducts(Iron);
        Mithril.addOreByProducts(Platinum);
        AstralSilver.addOreByProducts(Silver);
        Graphite.addOreByProducts(Carbon);
        Netherrack.addOreByProducts(Sulfur);
        Flint.addOreByProducts(Obsidian);
        Cobaltite.addOreByProducts(Cobalt);
        Cobalt.addOreByProducts(Cobaltite);
        Sulfur.addOreByProducts(Sulfur);
        Saltpeter.addOreByProducts(Saltpeter);
        Endstone.addOreByProducts(Helium_3);
        Osmium.addOreByProducts(Iridium);
        Magnesium.addOreByProducts(Olivine);
        Aluminium.addOreByProducts(Bauxite);
        Titanium.addOreByProducts(Almandine);
        Obsidian.addOreByProducts(Olivine);
        Ash.addOreByProducts(Carbon);
        DarkAsh.addOreByProducts(Carbon);
        Redrock.addOreByProducts(Clay);
        Marble.addOreByProducts(Calcite);
        Clay.addOreByProducts(Clay);
        Cassiterite.addOreByProducts(Tin);
        CassiteriteSand.addOreByProducts(Tin);
        GraniteBlack.addOreByProducts(Biotite);
        GraniteRed.addOreByProducts(PotassiumFeldspar);
        Phosphate.addOreByProducts(Phosphorus);
        Phosphorus.addOreByProducts(Phosphate);
        Tanzanite.addOreByProducts(Opal);
        Opal.addOreByProducts(Tanzanite);
        Amethyst.addOreByProducts(Amethyst);
        FoolsRuby.addOreByProducts(Jasper);
        Amber.addOreByProducts(Amber);
        Topaz.addOreByProducts(BlueTopaz);
        BlueTopaz.addOreByProducts(Topaz);
        Niter.addOreByProducts(Saltpeter);
        Vinteum.addOreByProducts(Vinteum);
        Force.addOreByProducts(Force);
        Dilithium.addOreByProducts(Dilithium);
        Neutronium.addOreByProducts(Neutronium);
        Lithium.addOreByProducts(Lithium);
        Silicon.addOreByProducts(SiliconDioxide);
        InfusedGold.addOreByProduct(Gold);
        Cryolite.addOreByProducts(Aluminiumoxide, Sodium);
        Naquadria.addOreByProduct(Naquadria);
        RoastedNickel.addOreByProduct(Nickel);
        TengamRaw.addOreByProducts(NeodymiumMagnetic, SamariumMagnetic);
    }

    private static void setColors() {
        Naquadah.mMoltenRGBa[0] = 0;
        Naquadah.mMoltenRGBa[1] = 255;
        Naquadah.mMoltenRGBa[2] = 0;
        Naquadah.mMoltenRGBa[3] = 0;
        NaquadahEnriched.mMoltenRGBa[0] = 64;
        NaquadahEnriched.mMoltenRGBa[1] = 255;
        NaquadahEnriched.mMoltenRGBa[2] = 64;
        NaquadahEnriched.mMoltenRGBa[3] = 0;
        Naquadria.mMoltenRGBa[0] = 128;
        Naquadria.mMoltenRGBa[1] = 255;
        Naquadria.mMoltenRGBa[2] = 128;
        Naquadria.mMoltenRGBa[3] = 0;
    }

    private static void overrideChemicalFormulars() {
        Glue.mChemicalFormula = "No Horses were harmed for the Production";
        AdvancedGlue.mChemicalFormula = "A chemically approved glue!";
        UUAmplifier.mChemicalFormula = "Accelerates the Mass Fabricator";
        LiveRoot.mChemicalFormula = "";
        WoodSealed.mChemicalFormula = "";
        Wood.mChemicalFormula = "";
        Electrotine.mChemicalFormula = "Rp";
        Trinium.mChemicalFormula = "Ke";
        Naquadah.mChemicalFormula = "Nq";
        NaquadahEnriched.mChemicalFormula = "Nq+";
        Naquadria.mChemicalFormula = "Nq*";
        NaquadahAlloy.mChemicalFormula = "Nq\u2082KeC";
        Sunnarium.mChemicalFormula = "Su";
        Adamantium.mChemicalFormula = "Ad";
        InfusedGold.mChemicalFormula = "AuMa*";
        MeteoricIron.mChemicalFormula = "SpFe";
        MeteoricSteel.mChemicalFormula = "SpFe\u2085\u2080C";
        Duranium.mChemicalFormula = "Du";
        Tritanium.mChemicalFormula = "Tn";
        Ardite.mChemicalFormula = "Ai";
        Manyullyn.mChemicalFormula = "AiCo";
        Mytryl.mChemicalFormula = "SpPt\u2082FeMa";
        BlackPlutonium.mChemicalFormula = "SpPu";
        Ledox.mChemicalFormula = "SpPb";
        CallistoIce.mChemicalFormula = "SpH\u2082O";
        Quantium.mChemicalFormula = "Qt";
        Desh.mChemicalFormula = "De";
        Oriharukon.mChemicalFormula = "Oh";
        Draconium.mChemicalFormula = "D";
        DraconiumAwakened.mChemicalFormula = "D*";
        BlueAlloy.mChemicalFormula = "AgRp\u2084";
        RedAlloy.mChemicalFormula = "Cu(" + Redstone.mChemicalFormula + ")\u2084";
        AnyIron.mChemicalFormula = "Fe";
        AnyCopper.mChemicalFormula = "Cu";
        ElectrumFlux.mChemicalFormula = "The formula is too long...";
        DeepIron.mChemicalFormula = "Sp\u2082Fe";
        Ichorium.mChemicalFormula = "IcMa";
        Infinity.mChemicalFormula = "If*";
        InfinityCatalyst.mChemicalFormula = "If";
        CosmicNeutronium.mChemicalFormula = "SpNt";
        Aluminiumhydroxide.mChemicalFormula = "Al\u0028OH\u0029\u2083";
        MaterialsKevlar.LiquidCrystalKevlar.mChemicalFormula = "[-CO-C\u2086H\u2084-CO-NH-C\u2086H\u2084-NH-]n";
        MaterialsKevlar.RhodiumChloride.mChemicalFormula = "RhCl\u2083";
        MaterialsKevlar.OrganorhodiumCatalyst.mChemicalFormula = "RhHCO(P(C\u2086H\u2085)\u2083)\u2083";
        MaterialsKevlar.CobaltIINitrate.mChemicalFormula = "Co(NO\u2083)\u2082";
        MaterialsKevlar.CobaltIIHydroxide.mChemicalFormula = "Co(OH)\u2082";
        SiliconSG.mChemicalFormula = "Si*";
        NetherQuartz.mChemicalFormula = "SiO\u2082";
        Quartzite.mChemicalFormula = "SiO\u2082";
        CertusQuartz.mChemicalFormula = "SiO\u2082";
        CertusQuartzCharged.mChemicalFormula = "SiO\u2082";
        MaterialsUEVplus.SpaceTime.mChemicalFormula = "Reality itself distilled into physical form";
        MaterialsUEVplus.Universium.mChemicalFormula = "A tear into the space beyond space";
        MaterialsUEVplus.Eternity.mChemicalFormula = "En\u29BC";
        MaterialsUEVplus.MagMatter.mChemicalFormula = "M\u238B";
        Longasssuperconductornameforuvwire.mChemicalFormula = "Nq*\u2084(Ir\u2083Os)\u2083EuSm";
        Longasssuperconductornameforuhvwire.mChemicalFormula = "D\u2086(SpNt)\u2087Tn\u2085Am\u2086";
        SuperconductorUEVBase.mChemicalFormula = "D*\u2085If*\u2085(\u2726\u25C6\u2726)(\u26B7\u2699\u26B7 Ni4Ti6)";
        SuperconductorUIVBase.mChemicalFormula = "(C\u2081\u2084Os\u2081\u2081O\u2087Ag\u2083SpH\u2082O)\u2084?\u2081\u2080(Fs\u26B6)\u2086(\u2318\u262F\u262F\u2318)\u2085";
        SuperconductorUMVBase.mChemicalFormula = "?\u2086Or\u2083(Hy\u26B6)\u2081\u2081(((CW)\u2087Ti\u2083)\u2083???)\u2085\u06DE\u2082";
        Diatomite.mChemicalFormula = "(SiO\u2082)\u2088Fe\u2082O\u2083(Al\u2082O\u2083)";
        EnrichedHolmium.mChemicalFormula = "Nq+\u2088Ho\u2082";
        Grade1PurifiedWater.mChemicalFormula = "H\u2082O";
        Grade2PurifiedWater.mChemicalFormula = "H\u2082O";
        Grade3PurifiedWater.mChemicalFormula = "H\u2082O";
        Grade4PurifiedWater.mChemicalFormula = "H\u2082O";
        Grade5PurifiedWater.mChemicalFormula = "H\u2082O";
        Grade6PurifiedWater.mChemicalFormula = "H\u2082O";
        Grade7PurifiedWater.mChemicalFormula = "H\u2082O";
        Grade8PurifiedWater.mChemicalFormula = "H\u2082O";
        FlocculationWasteLiquid.mChemicalFormula = "Al\u2082(OH)\u2083??Cl\u2083";
        TengamRaw.mChemicalFormula = "";
        TengamPurified.mChemicalFormula = "M";
        TengamAttuned.mChemicalFormula = "M";
        MaterialsUEVplus.ExcitedDTSC.mChemicalFormula = "[-Stellar-Stellar-]";
        MaterialsUEVplus.DimensionallyTranscendentStellarCatalyst.mChemicalFormula = "Stellar";
        PolyAluminiumChloride.mChemicalFormula = "Al\u2082(OH)\u2083Cl\u2083";
    }

    private static void initSubTags() {
        SubTag.ELECTROMAGNETIC_SEPERATION_NEODYMIUM.addTo(Bastnasite, Monazite, Forcicium, Forcillium);

        SubTag.ELECTROMAGNETIC_SEPERATION_GOLD
            .addTo(Magnetite, VanadiumMagnetite, BasalticMineralSand, GraniticMineralSand);

        SubTag.NO_RECIPES.addTo(MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter);

        SubTag.ELECTROMAGNETIC_SEPERATION_IRON.addTo(
            YellowLimonite,
            BrownLimonite,
            Pyrite,
            BandedIron,
            Nickel,
            Vermiculite,
            Glauconite,
            GlauconiteSand,
            Pentlandite,
            Tin,
            Antimony,
            Ilmenite,
            Manganese,
            Chrome,
            Chromite,
            Andradite);

        SubTag.BLASTFURNACE_CALCITE_DOUBLE
            .addTo(Pyrite, BrownLimonite, YellowLimonite, BasalticMineralSand, GraniticMineralSand, Magnetite);

        SubTag.BLASTFURNACE_CALCITE_TRIPLE.addTo(Iron, PigIron, DeepIron, ShadowIron, WroughtIron, MeteoricIron);

        SubTag.WASHING_MERCURY.addTo(Gold, Osmium, Mithril, Platinum, Cooperite, AstralSilver);

        SubTag.WASHING_MERCURY_99_PERCENT.addTo(Silver);

        SubTag.WASHING_SODIUMPERSULFATE.addTo(Zinc, Nickel, Copper, Cobalt, Cobaltite, Tetrahedrite);
        SubTag.METAL.addTo(
            AnyIron,
            AnyCopper,
            AnyBronze,
            Metal,
            Aluminium,
            Americium,
            Antimony,
            Beryllium,
            Bismuth,
            Caesium,
            Cerium,
            Chrome,
            Cobalt,
            Copper,
            Dysprosium,
            Erbium,
            Europium,
            Gadolinium,
            Gallium,
            Gold,
            Holmium,
            Indium,
            Iridium,
            Iron,
            Lanthanum,
            Lead,
            Lutetium,
            Magnesium,
            Manganese,
            Mercury,
            Niobium,
            Molybdenum,
            Neodymium,
            Neutronium,
            Nickel,
            Osmium,
            Palladium,
            Platinum,
            Plutonium,
            Plutonium241,
            Praseodymium,
            Promethium,
            Rubidium,
            Samarium,
            Scandium,
            Silicon,
            Silver,
            Tantalum,
            Tellurium,
            Terbium,
            Thorium,
            Thulium,
            Tin,
            Titanium,
            Tungsten,
            Uranium,
            Uranium235,
            Vanadium,
            Ytterbium,
            Yttrium,
            Zinc,
            Flerovium,
            PhasedIron,
            PhasedGold,
            DarkSteel,
            TinAlloy,
            ConductiveIron,
            ElectricalSteel,
            EnergeticAlloy,
            VibrantAlloy,
            MelodicAlloy,
            StellarAlloy,
            VividAlloy,
            EnergeticSilver,
            CrystallinePinkSlime,
            CrystallineAlloy,
            CrudeSteel,
            EndSteel,
            PulsatingIron,
            DarkThaumium,
            Adamantium,
            Amordrine,
            Angmallen,
            Ardite,
            Aredrite,
            Atlarus,
            Carmot,
            Celenegil,
            Ceruclase,
            DarkIron,
            Desh,
            Desichalkos,
            Duranium,
            ElectrumFlux,
            Enderium,
            EnderiumBase,
            Eximite,
            FierySteel,
            Force,
            Haderoth,
            Hematite,
            Hepatizon,
            HSLA,
            Infuscolium,
            InfusedGold,
            Inolashite,
            Mercassium,
            MeteoricIron,
            BloodInfusedIron,
            MaterialsUEVplus.Universium,
            MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter,
            MeteoricSteel,
            Naquadah,
            NaquadahAlloy,
            NaquadahEnriched,
            Naquadria,
            ObsidianFlux,
            Orichalcum,
            Osmonium,
            Oureclase,
            Phoenixite,
            Prometheum,
            Sanguinite,
            CosmicNeutronium,
            Tartarite,
            Ichorium,
            Tritanium,
            Vulcanite,
            Vyroxeres,
            Yellorium,
            Zectium,
            AluminiumBrass,
            Osmiridium,
            Sunnarium,
            AnnealedCopper,
            BatteryAlloy,
            Brass,
            Bronze,
            ChromiumDioxide,
            Cupronickel,
            DeepIron,
            Electrum,
            Invar,
            Kanthal,
            Magnalium,
            Nichrome,
            NiobiumNitride,
            NiobiumTitanium,
            PigIron,
            SolderingAlloy,
            StainlessSteel,
            Steel,
            Ultimet,
            VanadiumGallium,
            WroughtIron,
            YttriumBariumCuprate,
            IronWood,
            Alumite,
            Manyullyn,
            ShadowIron,
            Shadow,
            ShadowSteel,
            Steeleaf,
            SterlingSilver,
            RoseGold,
            BlackBronze,
            BismuthBronze,
            BlackSteel,
            RedSteel,
            BlueSteel,
            DamascusSteel,
            TungstenSteel,
            TPV,
            AstralSilver,
            Mithril,
            BlueAlloy,
            RedAlloy,
            CobaltBrass,
            Thaumium,
            Void,
            IronMagnetic,
            SteelMagnetic,
            NeodymiumMagnetic,
            SamariumMagnetic,
            Knightmetal,
            HSSG,
            HSSE,
            HSSS,
            TungstenCarbide,
            HeeEndium,
            VanadiumSteel,
            Kalendrite,
            Ignatius,
            Trinium,
            Infinity,
            InfinityCatalyst,
            Realgar,
            Chrysotile,
            BlackPlutonium,
            Alduorite,
            Adluorite,
            Vinteum,
            Rubracium,
            Draconium,
            DraconiumAwakened,
            Pentacadmiummagnesiumhexaoxid,
            Titaniumonabariumdecacoppereikosaoxid,
            Uraniumtriplatinid,
            Vanadiumtriindinid,
            Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid,
            Tetranaquadahdiindiumhexaplatiumosminid,
            Longasssuperconductornameforuvwire,
            Longasssuperconductornameforuhvwire,
            SuperconductorUEVBase,
            SuperconductorUIVBase,
            SuperconductorUMVBase,
            Quantium,
            RedstoneAlloy,
            Bedrockium,
            EnrichedHolmium,
            TengamPurified,
            TengamAttuned,
            MaterialsUEVplus.Eternity,
            MaterialsUEVplus.MagMatter);

        SubTag.FOOD.addTo(
            MeatRaw,
            MeatCooked,
            Ice,
            Water,
            Salt,
            Chili,
            Cocoa,
            Cheese,
            Coffee,
            Chocolate,
            Milk,
            Honey,
            FryingOilHot,
            FishOil,
            SeedOil,
            SeedOilLin,
            SeedOilHemp,
            Wheat,
            Sugar,
            FreshWater);

        Wood.add(SubTag.WOOD, SubTag.FLAMMABLE, SubTag.NO_SMELTING, SubTag.NO_SMASHING);
        WoodSealed.add(SubTag.WOOD, SubTag.FLAMMABLE, SubTag.NO_SMELTING, SubTag.NO_SMASHING, SubTag.NO_WORKING);
        Peanutwood.add(SubTag.WOOD, SubTag.FLAMMABLE, SubTag.NO_SMELTING, SubTag.NO_SMASHING);
        LiveRoot.add(
            SubTag.WOOD,
            SubTag.FLAMMABLE,
            SubTag.NO_SMELTING,
            SubTag.NO_SMASHING,
            SubTag.MAGICAL,
            SubTag.MORTAR_GRINDABLE);
        IronWood.add(SubTag.WOOD, SubTag.FLAMMABLE, SubTag.MAGICAL, SubTag.MORTAR_GRINDABLE);
        Steeleaf.add(SubTag.WOOD, SubTag.FLAMMABLE, SubTag.MAGICAL, SubTag.MORTAR_GRINDABLE, SubTag.NO_SMELTING);

        MeatRaw.add(SubTag.NO_SMASHING);
        MeatCooked.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Snow.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.NO_RECYCLING);
        Ice.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.NO_RECYCLING);
        Water.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.NO_RECYCLING);
        Sulfur.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.FLAMMABLE);
        Saltpeter.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.FLAMMABLE);
        Graphite.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.FLAMMABLE, SubTag.NO_SMELTING);

        Wheat.add(SubTag.FLAMMABLE, SubTag.MORTAR_GRINDABLE);
        Paper.add(SubTag.FLAMMABLE, SubTag.NO_SMELTING, SubTag.NO_SMASHING, SubTag.MORTAR_GRINDABLE, SubTag.PAPER);
        Coal.add(SubTag.FLAMMABLE, SubTag.NO_SMELTING, SubTag.NO_SMASHING, SubTag.MORTAR_GRINDABLE);
        Charcoal.add(SubTag.FLAMMABLE, SubTag.NO_SMELTING, SubTag.NO_SMASHING, SubTag.MORTAR_GRINDABLE);
        Lignite.add(SubTag.FLAMMABLE, SubTag.NO_SMELTING, SubTag.NO_SMASHING, SubTag.MORTAR_GRINDABLE);

        Rubber.add(SubTag.FLAMMABLE, SubTag.NO_SMASHING, SubTag.BOUNCY, SubTag.STRETCHY);
        StyreneButadieneRubber.add(SubTag.FLAMMABLE, SubTag.NO_SMASHING, SubTag.BOUNCY, SubTag.STRETCHY);
        Plastic.add(SubTag.FLAMMABLE, SubTag.NO_SMASHING, SubTag.BOUNCY, SubTag.STRETCHY);
        PolyvinylChloride.add(SubTag.FLAMMABLE, SubTag.NO_SMASHING, SubTag.BOUNCY, SubTag.STRETCHY);
        Polystyrene.add(SubTag.FLAMMABLE, SubTag.NO_SMASHING, SubTag.BOUNCY, SubTag.STRETCHY);
        Silicone.add(SubTag.FLAMMABLE, SubTag.NO_SMASHING, SubTag.BOUNCY, SubTag.STRETCHY);
        Polytetrafluoroethylene.add(SubTag.FLAMMABLE, SubTag.NO_SMASHING, SubTag.STRETCHY);
        Polybenzimidazole.add(SubTag.FLAMMABLE, SubTag.NO_SMASHING, SubTag.STRETCHY);
        PolyphenyleneSulfide.add(SubTag.FLAMMABLE, SubTag.NO_SMASHING, SubTag.STRETCHY);
        MaterialsKevlar.Kevlar.add(SubTag.FLAMMABLE, SubTag.NO_SMASHING, SubTag.STRETCHY);

        TNT.add(SubTag.FLAMMABLE, SubTag.EXPLOSIVE, SubTag.NO_SMELTING, SubTag.NO_SMASHING);
        Gunpowder.add(SubTag.FLAMMABLE, SubTag.EXPLOSIVE, SubTag.NO_SMELTING, SubTag.NO_SMASHING);
        Glyceryl.add(SubTag.FLAMMABLE, SubTag.EXPLOSIVE, SubTag.NO_SMELTING, SubTag.NO_SMASHING);
        NitroCoalFuel.add(SubTag.FLAMMABLE, SubTag.EXPLOSIVE, SubTag.NO_SMELTING, SubTag.NO_SMASHING);
        NitroFuel.add(SubTag.FLAMMABLE, SubTag.EXPLOSIVE, SubTag.NO_SMELTING, SubTag.NO_SMASHING);
        NitroCarbon.add(SubTag.FLAMMABLE, SubTag.EXPLOSIVE, SubTag.NO_SMELTING, SubTag.NO_SMASHING);

        Lead.add(SubTag.MORTAR_GRINDABLE, SubTag.SOLDERING_MATERIAL, SubTag.SOLDERING_MATERIAL_BAD);
        Tin.add(SubTag.MORTAR_GRINDABLE, SubTag.SOLDERING_MATERIAL);
        SolderingAlloy.add(SubTag.MORTAR_GRINDABLE, SubTag.SOLDERING_MATERIAL, SubTag.SOLDERING_MATERIAL_GOOD);

        Cheese.add(SubTag.SMELTING_TO_FLUID);
        Sugar.add(SubTag.SMELTING_TO_FLUID);

        Concrete.add(SubTag.STONE, SubTag.NO_SMASHING, SubTag.SMELTING_TO_FLUID);
        ConstructionFoam.add(SubTag.STONE, SubTag.NO_SMASHING, SubTag.EXPLOSIVE, SubTag.NO_SMELTING);
        ReinforceGlass.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.SMELTING_TO_FLUID);
        BorosilicateGlass.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_RECYCLING, SubTag.SMELTING_TO_FLUID);

        Redstone.add(
            SubTag.STONE,
            SubTag.NO_SMASHING,
            SubTag.UNBURNABLE,
            SubTag.SMELTING_TO_FLUID,
            SubTag.PULVERIZING_CINNABAR);
        Glowstone.add(SubTag.STONE, SubTag.NO_SMASHING, SubTag.UNBURNABLE, SubTag.SMELTING_TO_FLUID);
        Electrotine.add(SubTag.STONE, SubTag.NO_SMASHING, SubTag.UNBURNABLE, SubTag.SMELTING_TO_FLUID);
        Teslatite.add(SubTag.STONE, SubTag.NO_SMASHING, SubTag.UNBURNABLE, SubTag.SMELTING_TO_FLUID);
        Netherrack.add(SubTag.STONE, SubTag.NO_SMASHING, SubTag.UNBURNABLE, SubTag.FLAMMABLE);
        Stone.add(SubTag.STONE, SubTag.NO_SMASHING, SubTag.NO_RECYCLING);
        Brick.add(SubTag.STONE, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        NetherBrick.add(SubTag.STONE, SubTag.NO_SMASHING);
        Endstone.add(SubTag.STONE, SubTag.NO_SMASHING);
        Marble.add(SubTag.STONE, SubTag.NO_SMASHING);
        Basalt.add(SubTag.STONE, SubTag.NO_SMASHING);
        Redrock.add(SubTag.STONE, SubTag.NO_SMASHING);
        Obsidian.add(SubTag.STONE, SubTag.NO_SMASHING);
        Flint.add(SubTag.STONE, SubTag.NO_SMASHING, SubTag.MORTAR_GRINDABLE);
        GraniteRed.add(SubTag.STONE, SubTag.NO_SMASHING);
        GraniteBlack.add(SubTag.STONE, SubTag.NO_SMASHING);
        Salt.add(SubTag.STONE, SubTag.NO_SMASHING);
        RockSalt.add(SubTag.STONE, SubTag.NO_SMASHING);

        Sand.add(SubTag.NO_RECYCLING);

        Gold.add(SubTag.MORTAR_GRINDABLE);
        Silver.add(SubTag.MORTAR_GRINDABLE);
        Iron.add(SubTag.MORTAR_GRINDABLE);
        IronMagnetic.add(SubTag.MORTAR_GRINDABLE);
        HSLA.add(SubTag.MORTAR_GRINDABLE);
        Steel.add(SubTag.MORTAR_GRINDABLE);
        SteelMagnetic.add(SubTag.MORTAR_GRINDABLE);
        Zinc.add(SubTag.MORTAR_GRINDABLE);
        Antimony.add(SubTag.MORTAR_GRINDABLE);
        Copper.add(SubTag.MORTAR_GRINDABLE);
        AnnealedCopper.add(SubTag.MORTAR_GRINDABLE);
        Bronze.add(SubTag.MORTAR_GRINDABLE);
        Nickel.add(SubTag.MORTAR_GRINDABLE);
        Invar.add(SubTag.MORTAR_GRINDABLE);
        Brass.add(SubTag.MORTAR_GRINDABLE);
        WroughtIron.add(SubTag.MORTAR_GRINDABLE);
        Electrum.add(SubTag.MORTAR_GRINDABLE);
        Clay.add(SubTag.MORTAR_GRINDABLE);

        Glass.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_RECYCLING, SubTag.SMELTING_TO_FLUID);
        Diamond.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.FLAMMABLE);
        Emerald.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Amethyst.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Tanzanite.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Topaz.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        BlueTopaz.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Amber.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        GreenSapphire.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Sapphire.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Ruby.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        FoolsRuby.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Opal.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Olivine.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Jasper.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        GarnetRed.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        GarnetYellow.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Mimichite.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        CrystalFlux.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Crystal.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Niter.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Apatite.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.CRYSTALLISABLE);
        Lapis.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.CRYSTALLISABLE);
        Sodalite.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.CRYSTALLISABLE);
        Lazurite.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.CRYSTALLISABLE);
        Monazite.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.CRYSTALLISABLE);
        Quartzite.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.CRYSTALLISABLE, SubTag.QUARTZ);
        Quartz.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.CRYSTALLISABLE, SubTag.QUARTZ);
        SiliconDioxide
            .add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.CRYSTALLISABLE, SubTag.QUARTZ);
        Dilithium.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.CRYSTALLISABLE, SubTag.QUARTZ);
        NetherQuartz.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.CRYSTALLISABLE, SubTag.QUARTZ);
        CertusQuartz.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.CRYSTALLISABLE, SubTag.QUARTZ);
        CertusQuartzCharged
            .add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.CRYSTALLISABLE, SubTag.QUARTZ);
        Fluix.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.CRYSTALLISABLE, SubTag.QUARTZ);
        TricalciumPhosphate
            .add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.FLAMMABLE, SubTag.EXPLOSIVE);
        Phosphate.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.FLAMMABLE, SubTag.EXPLOSIVE);
        InfusedAir.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.MAGICAL, SubTag.UNBURNABLE);
        InfusedFire.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.MAGICAL, SubTag.UNBURNABLE);
        InfusedEarth.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.MAGICAL, SubTag.UNBURNABLE);
        InfusedWater.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.MAGICAL, SubTag.UNBURNABLE);
        InfusedEntropy.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.MAGICAL, SubTag.UNBURNABLE);
        InfusedOrder.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.MAGICAL, SubTag.UNBURNABLE);
        InfusedVis.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.MAGICAL, SubTag.UNBURNABLE);
        InfusedDull.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.MAGICAL, SubTag.UNBURNABLE);
        NetherStar.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.MAGICAL, SubTag.UNBURNABLE);
        EnderPearl.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.MAGICAL, SubTag.PEARL);
        EnderEye.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.MAGICAL, SubTag.PEARL);
        Firestone.add(
            SubTag.CRYSTAL,
            SubTag.NO_SMASHING,
            SubTag.NO_SMELTING,
            SubTag.CRYSTALLISABLE,
            SubTag.MAGICAL,
            SubTag.QUARTZ,
            SubTag.UNBURNABLE,
            SubTag.BURNING);
        Forcicium.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.CRYSTALLISABLE, SubTag.MAGICAL);
        Forcillium.add(SubTag.CRYSTAL, SubTag.NO_SMASHING, SubTag.NO_SMELTING, SubTag.CRYSTALLISABLE, SubTag.MAGICAL);
        Force.add(SubTag.CRYSTAL, SubTag.MAGICAL, SubTag.UNBURNABLE);
        Magic.add(SubTag.CRYSTAL, SubTag.MAGICAL, SubTag.UNBURNABLE);

        Primitive.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Basic.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Good.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Advanced.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Data.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Elite.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Master.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Ultimate.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Superconductor.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING); // Todo: remove this once it will be fully
                                                                    // deprecated
        Infinite.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        Bio.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        SuperconductorMV.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        SuperconductorHV.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        SuperconductorEV.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        SuperconductorIV.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        SuperconductorLuV.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        SuperconductorZPM.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        // SuperconductorUV .add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        SuperconductorUHV.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);

        Blaze.add(SubTag.MAGICAL, SubTag.SMELTING_TO_FLUID, SubTag.MORTAR_GRINDABLE, SubTag.UNBURNABLE, SubTag.BURNING);
        FierySteel.add(SubTag.MAGICAL, SubTag.UNBURNABLE, SubTag.BURNING);
        DarkThaumium.add(SubTag.MAGICAL);
        Thaumium.add(SubTag.MAGICAL);
        Void.add(SubTag.MAGICAL);
        Enderium.add(SubTag.MAGICAL);
        AstralSilver.add(SubTag.MAGICAL);
        Mithril.add(SubTag.MAGICAL);

        Carbon.add(SubTag.NO_SMELTING);
        Boron.add(SubTag.SMELTING_TO_FLUID);
    }

    public static void init() {
        new ProcessingConfig();
        if (!GT_Mod.gregtechproxy.mEnableAllMaterials) new ProcessingModSupport();
        mMaterialHandlers.forEach(IMaterialHandler::onMaterialsInit); // This is where addon mods can add/manipulate
                                                                      // materials
        initMaterialProperties(); // No more material addition or manipulation should be done past this point!
        MATERIALS_ARRAY = MATERIALS_MAP.values()
            .toArray(new Materials[0]); // Generate standard object array. This is a
                                        // lot faster to loop over.
        VALUES = Arrays.asList(MATERIALS_ARRAY);

        disableUnusedHotIngots();
        fillGeneratedMaterialsMap();
    }

    private static void disableUnusedHotIngots() {
        OrePrefixes.ingotHot.mDisabledItems.addAll(
            Arrays.stream(Materials.values())
                .parallel()
                .filter(OrePrefixes.ingotHot::doGenerateItem)
                .filter(m -> m.mBlastFurnaceTemp < 1750 && m.mAutoGenerateBlastFurnaceRecipes)
                .collect(Collectors.toSet()));
        OrePrefixes.ingotHot.disableComponent(Materials.Reinforced);
        OrePrefixes.ingotHot.disableComponent(Materials.ConductiveIron);
        OrePrefixes.ingotHot.disableComponent(Materials.FierySteel);
        OrePrefixes.ingotHot.disableComponent(Materials.ElectricalSteel);
        OrePrefixes.ingotHot.disableComponent(Materials.EndSteel);
        OrePrefixes.ingotHot.disableComponent(Materials.Soularium);
        OrePrefixes.ingotHot.disableComponent(Materials.EnergeticSilver);
        OrePrefixes.ingotHot.disableComponent(Materials.Cheese);
        OrePrefixes.ingotHot.disableComponent(Materials.Calcium);
        OrePrefixes.ingotHot.disableComponent(Materials.Flerovium);
        OrePrefixes.ingotHot.disableComponent(Materials.Cobalt);
        OrePrefixes.ingotHot.disableComponent(Materials.RedstoneAlloy);
        OrePrefixes.ingotHot.disableComponent(Materials.Ardite);
        OrePrefixes.ingotHot.disableComponent(Materials.DarkSteel);
        OrePrefixes.ingotHot.disableComponent(Materials.BlackSteel);
        OrePrefixes.ingotHot.disableComponent(Materials.EnergeticAlloy);
        OrePrefixes.ingotHot.disableComponent(Materials.PulsatingIron);
        OrePrefixes.ingotHot.disableComponent(Materials.CrudeSteel);
    }

    /**
     * Init rendering properties. Will be called at pre init by GT client proxy.
     */
    public static void initClient() {
        MaterialsUEVplus.TranscendentMetal.renderer = new TranscendentMetalRenderer();
        MaterialsBotania.GaiaSpirit.renderer = new GaiaSpiritRenderer();
        Infinity.renderer = new InfinityRenderer();
        CosmicNeutronium.renderer = new CosmicNeutroniumRenderer();
        MaterialsUEVplus.Universium.renderer = new UniversiumRenderer();
        MaterialsUEVplus.Eternity.renderer = new InfinityRenderer();
        MaterialsUEVplus.MagMatter.renderer = new InfinityRenderer();
    }

    private static void fillGeneratedMaterialsMap() {
        for (Materials aMaterial : MATERIALS_ARRAY) {
            if (aMaterial.mMetaItemSubID >= 0) {
                if (aMaterial.mMetaItemSubID < 1000) {
                    if (aMaterial.mHasParentMod) {
                        if (GregTech_API.sGeneratedMaterials[aMaterial.mMetaItemSubID] == null) {
                            GregTech_API.sGeneratedMaterials[aMaterial.mMetaItemSubID] = aMaterial;
                        } else throw new IllegalArgumentException(
                            "The Material Index " + aMaterial.mMetaItemSubID
                                + " for "
                                + aMaterial.mName
                                + " is already used!");
                    }
                } else throw new IllegalArgumentException(
                    "The Material Index " + aMaterial.mMetaItemSubID
                        + " for "
                        + aMaterial.mName
                        + " is/over the maximum of 1000");
            }
        }
    }

    private static void addToolValues(Materials aMaterial) {
        // Moved from GT_Proxy? (Not sure)
        aMaterial.mHandleMaterial = (aMaterial == Desh ? aMaterial.mHandleMaterial
            : aMaterial == Diamond || aMaterial == Thaumium ? Wood
                : aMaterial.contains(SubTag.BURNING) ? Blaze
                    : aMaterial.contains(SubTag.MAGICAL) && aMaterial.contains(SubTag.CRYSTAL)
                        && Thaumcraft.isModLoaded() ? Thaumium
                            : aMaterial.getMass() > Element.Tc.getMass() * 2 ? TungstenSteel
                                : aMaterial.getMass() > Element.Tc.getMass() ? Steel : Wood);

        if (aMaterial == MaterialsUEVplus.SpaceTime) {
            aMaterial.mHandleMaterial = Materials.Infinity;
        }

        if (aMaterial == MaterialsUEVplus.TranscendentMetal) {
            aMaterial.mHandleMaterial = Materials.DraconiumAwakened;
        }

        if (aMaterial == MaterialsUEVplus.Eternity) {
            aMaterial.mHandleMaterial = MaterialsUEVplus.SpaceTime;
        }

        if (aMaterial == MaterialsUEVplus.MagMatter) {
            aMaterial.mHandleMaterial = MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter;
        }
    }

    private static void addEnchantmentValues(Materials aMaterial) {
        String aEnchantmentName = aMaterial.mEnchantmentTools != null ? aMaterial.mEnchantmentTools.getName() : "";
        if (aMaterial.mEnchantmentTools != null && !aEnchantmentName.equals(aMaterial.mEnchantmentTools.getName()))
            IntStream.range(0, Enchantment.enchantmentsList.length)
                .filter(i -> aEnchantmentName.equals(Enchantment.enchantmentsList[i].getName()))
                .forEach(i -> aMaterial.mEnchantmentTools = Enchantment.enchantmentsList[i]);
    }

    private static void addHasGasFluid(Materials aMaterial) {
        if (aMaterial.mIconSet.is_custom) {
            return;
        }

        if (aMaterial.mHasPlasma) {
            GT_Mod.gregtechproxy.addAutogeneratedPlasmaFluid(aMaterial);
        }
        if (aMaterial.mHasGas) {
            GT_FluidFactory
                .of(aMaterial.mName.toLowerCase(), aMaterial.mDefaultLocalName, aMaterial, GAS, aMaterial.mGasTemp);
        }
    }

    private static String getConfigPath(Materials aMaterial) {
        String cOre = aMaterial.mCustomOre ? aMaterial.mCustomID : aMaterial.mName;
        return "materials." + aMaterial.mConfigSection + "." + cOre;
    }

    private static void addHarvestLevelNerfs(Materials aMaterial) {
        /* Moved the harvest level changes from GT_Mod to have fewer things iterating over MATERIALS_ARRAY */
        if (GT_Mod.gregtechproxy.mChangeHarvestLevels && aMaterial.mToolQuality > 0
            && aMaterial.mMetaItemSubID < GT_Mod.gregtechproxy.mHarvestLevel.length
            && aMaterial.mMetaItemSubID >= 0) {
            GT_Mod.gregtechproxy.mHarvestLevel[aMaterial.mMetaItemSubID] = aMaterial.mToolQuality;
        }
    }

    private static void addHarvestLevels() {
        GT_Mod.gregtechproxy.mChangeHarvestLevels = ConfigHarvestLevel.activateHarvestLevelChange;
        GT_Mod.gregtechproxy.mMaxHarvestLevel = Math.min(15, ConfigHarvestLevel.maxHarvestLevel);
        GT_Mod.gregtechproxy.mGraniteHavestLevel = ConfigHarvestLevel.graniteHarvestLevel;
    }

    public static void initMaterialProperties() {
        addHarvestLevels();
        for (Materials aMaterial : MATERIALS_MAP.values()) {
            if (aMaterial == null || aMaterial == Materials._NULL || aMaterial == Materials.Empty) {
                continue;
            }

            addToolValues(aMaterial);
            addEnchantmentValues(aMaterial);
            addHasGasFluid(aMaterial);
            addHarvestLevelNerfs(aMaterial);
        }
    }

    /**
     * This is for keeping compatibility with addons mods (Such as TinkersGregworks etc.) that looped over the old
     * materials enum
     */
    @Deprecated
    public static Materials valueOf(String aMaterialName) {
        return getMaterialsMap().get(aMaterialName);
    }

    /**
     * This is for keeping compatibility with addons mods (Such as TinkersGregworks etc.) that looped over the old
     * materials enum
     */
    public static Materials[] values() {
        return MATERIALS_ARRAY;
    }

    /**
     * This should only be used for getting a Material by its name as a String. Do not loop over this map, use values().
     */
    public static Map<String, Materials> getMaterialsMap() {
        return MATERIALS_MAP;
    }

    @Nonnull
    public static Materials get(String aMaterialName) {
        return getWithFallback(aMaterialName, Materials._NULL);
    }

    @Nonnull
    public static Materials getWithFallback(String name, @Nonnull Materials fallback) {
        Materials material = getMaterialsMap().get(name);
        if (material != null) {
            return material;
        }
        return fallback;
    }

    public static Materials getRealMaterial(String aMaterialName) {
        return get(aMaterialName).mMaterialInto;
    }

    /**
     * Adds a Class implementing IMaterialRegistrator to the master list
     */
    public static boolean add(IMaterialHandler aRegistrator) {
        if (aRegistrator == null) return false;
        return mMaterialHandlers.add(aRegistrator);
    }

    public static String getLocalizedNameForItem(String aFormat, int aMaterialID) {
        if (aMaterialID >= 0 && aMaterialID < 1000) {
            Materials aMaterial = GregTech_API.sGeneratedMaterials[aMaterialID];
            if (aMaterial != null) return aMaterial.getLocalizedNameForItem(aFormat);
        }
        return aFormat;
    }

    public static Collection<Materials> getAll() {
        return MATERIALS_MAP.values();
    }

    public Materials disableAutoGeneratedBlastFurnaceRecipes() {
        mAutoGenerateBlastFurnaceRecipes = false;
        return this;
    }

    public Materials disableAutoGeneratedVacuumFreezerRecipe() {
        mAutoGenerateVacuumFreezerRecipes = false;
        return this;
    }

    public Materials setTurbineMultipliers(float steamMultiplier, float gasMultiplier, float plasmaMultiplier) {
        mSteamMultiplier = steamMultiplier;
        mGasMultiplier = gasMultiplier;
        mPlasmaMultiplier = plasmaMultiplier;
        return this;
    }

    public Materials disableAutoGeneratedRecycleRecipes() {
        mAutoGenerateRecycleRecipes = false;
        return this;
    }

    /**
     * This is for keeping compatibility with addons mods (Such as TinkersGregworks etc.) that looped over the old
     * materials enum
     */
    @Deprecated
    public String name() {
        return mName;
    }

    public boolean isRadioactive() {
        if (mElement != null) return mElement.mHalfLifeSeconds >= 0;

        return mMaterialList.stream()
            .map(stack -> stack.mMaterial)
            .anyMatch(Materials::isRadioactive);
    }

    public long getProtons() {
        if (mElement != null) return mElement.getProtons();
        if (mMaterialList.size() == 0) return Element.Tc.getProtons();
        long rAmount = 0, tAmount = 0;
        for (MaterialStack tMaterial : mMaterialList) {
            tAmount += tMaterial.mAmount;
            rAmount += tMaterial.mAmount * tMaterial.mMaterial.getProtons();
        }
        return (getDensity() * rAmount) / (tAmount * M);
    }

    public long getNeutrons() {
        if (mElement != null) return mElement.getNeutrons();
        if (mMaterialList.size() == 0) return Element.Tc.getNeutrons();
        long rAmount = 0, tAmount = 0;
        for (MaterialStack tMaterial : mMaterialList) {
            tAmount += tMaterial.mAmount;
            rAmount += tMaterial.mAmount * tMaterial.mMaterial.getNeutrons();
        }
        return (getDensity() * rAmount) / (tAmount * M);
    }

    public long getMass() {
        if (mElement != null) return mElement.getMass();
        if (mMaterialList.size() == 0) return Element.Tc.getMass();
        long rAmount = 0, tAmount = 0;
        for (MaterialStack tMaterial : mMaterialList) {
            tAmount += tMaterial.mAmount;
            rAmount += tMaterial.mAmount * tMaterial.mMaterial.getMass();
        }
        return (getDensity() * rAmount) / (tAmount * M);
    }

    public long getDensity() {
        return mDensity;
    }

    public String getToolTip() {
        return getToolTip(1, false);
    }

    public String getToolTip(boolean aShowQuestionMarks) {
        return getToolTip(1, aShowQuestionMarks);
    }

    public String getToolTip(long aMultiplier) {
        return getToolTip(aMultiplier, false);
    }

    public String getToolTip(long aMultiplier, boolean aShowQuestionMarks) {
        if (!aShowQuestionMarks && mChemicalFormula.equals("?")) return "";
        if (aMultiplier >= M * 2 && !mMaterialList.isEmpty()) {
            return ((mElement != null || (mMaterialList.size() < 2 && mMaterialList.get(0).mAmount == 1))
                ? mChemicalFormula
                : "(" + mChemicalFormula + ")") + aMultiplier;
        }
        return mChemicalFormula;
    }

    /**
     * Adds an ItemStack to this Material.
     */
    public Materials add(ItemStack aStack) {
        if (aStack != null && !contains(aStack)) mMaterialItems.add(aStack);
        return this;
    }

    /**
     * This is used to determine if any of the ItemStacks belongs to this Material.
     */
    public boolean contains(ItemStack... aStacks) {
        if (aStacks == null || aStacks.length == 0) return false;
        return mMaterialItems.stream()
            .anyMatch(
                tStack -> Arrays.stream(aStacks)
                    .anyMatch(aStack -> GT_Utility.areStacksEqual(aStack, tStack, !tStack.hasTagCompound())));
    }

    /**
     * This is used to determine if an ItemStack belongs to this Material.
     */
    public boolean remove(ItemStack aStack) {
        if (aStack == null) return false;
        boolean temp = false;
        int mMaterialItems_sS = mMaterialItems.size();
        for (int i = 0; i < mMaterialItems_sS; i++) if (GT_Utility.areStacksEqual(aStack, mMaterialItems.get(i))) {
            mMaterialItems.remove(i--);
            temp = true;
        }
        return temp;
    }

    /**
     * Adds a SubTag to this Material
     */
    @Override
    public ISubTagContainer add(SubTag... aTags) {
        if (aTags != null) for (SubTag aTag : aTags) if (aTag != null && !contains(aTag)) {
            aTag.addContainerToList(this);
            mSubTags.add(aTag);
        }
        return this;
    }

    /**
     * If this Material has this exact SubTag
     */
    @Override
    public boolean contains(SubTag aTag) {
        return mSubTags.contains(aTag);
    }

    /**
     * Removes a SubTag from this Material
     */
    @Override
    public boolean remove(SubTag aTag) {
        return mSubTags.remove(aTag);
    }

    /**
     * Sets the Heat Damage for this Material (negative = frost)
     */
    @SuppressWarnings("UnusedReturnValue") // Maintains signature
    public Materials setHeatDamage(float aHeatDamage) {
        mHeatDamage = aHeatDamage;
        return this;
    }

    /**
     * Adds a Material to the List of Byproducts when grinding this Ore. Is used for more precise Ore grinding, so that
     * it is possible to choose between certain kinds of Materials.
     */
    @SuppressWarnings("UnusedReturnValue") // Maintains signature
    public Materials addOreByProduct(Materials aMaterial) {
        if (!mOreByProducts.contains(aMaterial.mMaterialInto)) mOreByProducts.add(aMaterial.mMaterialInto);
        return this;
    }

    /**
     * Adds multiple Materials to the List of Byproducts when grinding this Ore. Is used for more precise Ore grinding,
     * so that it is possible to choose between certain kinds of Materials.
     */
    public Materials addOreByProducts(Materials... aMaterials) {
        for (Materials tMaterial : aMaterials) if (tMaterial != null) addOreByProduct(tMaterial);
        return this;
    }

    /**
     * If this Ore gives multiple drops of its Main Material. Lapis Ore for example gives about 6 drops.
     */
    public Materials setOreMultiplier(int aOreMultiplier) {
        if (aOreMultiplier > 0) mOreMultiplier = aOreMultiplier;
        return this;
    }

    /**
     * If this Ore gives multiple drops of its Byproduct Material.
     */
    @SuppressWarnings("UnusedReturnValue") // Maintains signature
    public Materials setByProductMultiplier(int aByProductMultiplier) {
        if (aByProductMultiplier > 0) mByProductMultiplier = aByProductMultiplier;
        return this;
    }

    /**
     * If this Ore gives multiple drops of its Main Material. Lapis Ore for example gives about 6 drops.
     */
    public Materials setSmeltingMultiplier(int aSmeltingMultiplier) {
        if (aSmeltingMultiplier > 0) mSmeltingMultiplier = aSmeltingMultiplier;
        return this;
    }

    /**
     * This Ore should be molten directly into an Ingot of this Material instead of an Ingot of itself.
     */
    public Materials setDirectSmelting(Materials aMaterial) {
        if (aMaterial != null) mDirectSmelting = aMaterial.mMaterialInto.mDirectSmelting;
        return this;
    }

    /**
     * This Material should be the Main Material this Ore gets ground into. Example, Chromite giving Chrome or Tungstate
     * giving Tungsten.
     */
    @SuppressWarnings("UnusedReturnValue") // Maintains signature
    public Materials setOreReplacement(Materials aMaterial) {
        if (aMaterial != null) mOreReplacement = aMaterial.mMaterialInto.mOreReplacement;
        return this;
    }

    /**
     * This Material smelts always into an instance of aMaterial. Used for Magnets.
     */
    public Materials setSmeltingInto(Materials aMaterial) {
        if (aMaterial != null) mSmeltInto = aMaterial.mMaterialInto.mSmeltInto;
        return this;
    }

    /**
     * This Material arc smelts always into an instance of aMaterial. Used for Wrought Iron.
     */
    @SuppressWarnings("UnusedReturnValue") // Maintains signature
    public Materials setArcSmeltingInto(Materials aMaterial) {
        if (aMaterial != null) mArcSmeltInto = aMaterial.mMaterialInto.mArcSmeltInto;
        return this;
    }

    /**
     * This Material macerates always into an instance of aMaterial.
     */
    public Materials setMaceratingInto(Materials aMaterial) {
        if (aMaterial != null) mMacerateInto = aMaterial.mMaterialInto.mMacerateInto;
        return this;
    }

    public Materials setEnchantmentForTools(Enchantment aEnchantment, int aEnchantmentLevel) {
        mEnchantmentTools = aEnchantment;
        mEnchantmentToolsLevel = (byte) aEnchantmentLevel;
        return this;
    }

    @SuppressWarnings("UnusedReturnValue") // Maintains signature
    public Materials setEnchantmentForArmors(Enchantment aEnchantment, int aEnchantmentLevel) {
        mEnchantmentArmors = aEnchantment;
        mEnchantmentArmorsLevel = (byte) aEnchantmentLevel;
        return this;
    }

    public FluidStack getSolid(long aAmount) {
        if (mSolid == null) return null;
        return new FluidStack(mSolid, (int) aAmount);
    }

    public FluidStack getFluid(long aAmount) {
        if (mFluid == null) return null;
        return new FluidStack(mFluid, (int) aAmount);
    }

    public FluidStack getGas(long aAmount) {
        if (mGas == null) return null;
        return new FluidStack(mGas, (int) aAmount);
    }

    public FluidStack getPlasma(long aAmount) {
        if (mPlasma == null) return null;
        return new FluidStack(mPlasma, (int) aAmount);
    }

    public FluidStack getMolten(long aAmount) {
        if (mStandardMoltenFluid == null) return null;
        return new FluidStack(mStandardMoltenFluid, (int) aAmount);
    }

    @Override
    public short[] getRGBA() {
        return mRGBa;
    }

    @Override
    public String toString() {
        return this.mName;
    }

    public String getDefaultLocalizedNameForItem(String aFormat) {
        try {
            return String.format(
                aFormat.replace("%s", "%temp")
                    .replace("%material", "%s"),
                this.mDefaultLocalName)
                .replace("%temp", "%s");
        } catch (IllegalFormatException ignored) {
            return aFormat;
        }
    }

    public String getLocalizedNameForItem(String aFormat) {
        try {
            return String.format(
                aFormat.replace("%s", "%temp")
                    .replace("%material", "%s"),
                this.mLocalizedName)
                .replace("%temp", "%s");
        } catch (IllegalFormatException ignored) {
            return aFormat;
        }
    }

    public boolean hasCorrespondingFluid() {
        return hasCorrespondingFluid;
    }

    public Materials setHasCorrespondingFluid(boolean hasCorrespondingFluid) {
        this.hasCorrespondingFluid = hasCorrespondingFluid;
        return this;
    }

    public boolean hasCorrespondingGas() {
        return hasCorrespondingGas;
    }

    public Materials setHasCorrespondingGas(boolean hasCorrespondingGas) {
        this.hasCorrespondingGas = hasCorrespondingGas;
        return this;
    }

    public Materials setHasCorrespondingPlasma(boolean hasCorrespondingPlasma) {
        this.mHasPlasma = hasCorrespondingPlasma;
        return this;
    }

    public boolean canBeCracked() {
        return canBeCracked;
    }

    public Materials setCanBeCracked(boolean canBeCracked) {
        this.canBeCracked = canBeCracked;
        return this;
    }

    public int getLiquidTemperature() {
        return mMeltingPoint == 0 ? 295 : mMeltingPoint;
    }

    public Materials setLiquidTemperature(int liquidTemperature) {
        this.mMeltingPoint = liquidTemperature;
        return this;
    }

    public int getGasTemperature() {
        return mGasTemp == 0 ? 295 : mMeltingPoint;
    }

    public Materials setGasTemperature(int gasTemperature) {
        this.mGasTemp = gasTemperature;
        return this;
    }

    public Materials setHydroCrackedFluids(Fluid[] hydroCrackedFluids) {
        this.hydroCrackedFluids = hydroCrackedFluids;
        return this;
    }

    public FluidStack getLightlyHydroCracked(int amount) {
        if (hydroCrackedFluids[0] == null) {
            return null;
        }
        return new FluidStack(hydroCrackedFluids[0], amount);
    }

    public FluidStack getModeratelyHydroCracked(int amount) {
        if (hydroCrackedFluids[0] == null) {
            return null;
        }
        return new FluidStack(hydroCrackedFluids[1], amount);
    }

    public FluidStack getSeverelyHydroCracked(int amount) {
        if (hydroCrackedFluids[0] == null) {
            return null;
        }
        return new FluidStack(hydroCrackedFluids[2], amount);
    }

    public Materials setSteamCrackedFluids(Fluid[] steamCrackedFluids) {
        this.steamCrackedFluids = steamCrackedFluids;
        return this;
    }

    public FluidStack getLightlySteamCracked(int amount) {
        if (hydroCrackedFluids[0] == null) {
            return null;
        }
        return new FluidStack(steamCrackedFluids[0], amount);
    }

    public FluidStack getModeratelySteamCracked(int amount) {
        if (hydroCrackedFluids[0] == null) {
            return null;
        }
        return new FluidStack(steamCrackedFluids[1], amount);
    }

    public FluidStack getSeverelySteamCracked(int amount) {
        if (hydroCrackedFluids[0] == null) {
            return null;
        }
        return new FluidStack(steamCrackedFluids[2], amount);
    }

    /**
     * Check that the material is a proper soldering fluid
     **
     * @return true if Materials is a proper soldering fluid
     */
    public boolean isProperSolderingFluid() {
        return mStandardMoltenFluid != null && contains(SubTag.SOLDERING_MATERIAL)
            && !(GregTech_API.mUseOnlyGoodSolderingMaterials && !contains(SubTag.SOLDERING_MATERIAL_GOOD));
    }

    public ItemStack getCells(int amount) {
        return GT_OreDictUnificator.get(OrePrefixes.cell, this, amount);
    }

    public ItemStack getDust(int amount) {
        return GT_OreDictUnificator.get(OrePrefixes.dust, this, amount);
    }

    public ItemStack getDustSmall(int amount) {
        return GT_OreDictUnificator.get(OrePrefixes.dustSmall, this, amount);
    }

    public ItemStack getDustTiny(int amount) {
        return GT_OreDictUnificator.get(OrePrefixes.dustTiny, this, amount);
    }

    public ItemStack getGems(int amount) {
        return GT_OreDictUnificator.get(OrePrefixes.gem, this, amount);
    }

    public ItemStack getIngots(int amount) {
        return GT_OreDictUnificator.get(OrePrefixes.ingot, this, amount);
    }

    public ItemStack getNuggets(int amount) {
        return GT_OreDictUnificator.get(OrePrefixes.nugget, this, amount);
    }

    public ItemStack getBlocks(int amount) {
        return GT_OreDictUnificator.get(OrePrefixes.block, this, amount);
    }

    public ItemStack getPlates(int amount) {
        return GT_OreDictUnificator.get(OrePrefixes.plate, this, amount);
    }

    public static Materials getGtMaterialFromFluid(Fluid fluid) {
        return FLUID_MAP.get(fluid);
    }

    public ItemStack getNanite(int amount) {
        return GT_OreDictUnificator.get(OrePrefixes.nanite, this, amount);
    }
}
