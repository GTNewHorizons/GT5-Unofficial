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
    public static Materials Antimatter;
    public static Materials AdvancedGlue;
    public static Materials BioFuel;
    public static Materials Biomass;
    public static Materials CharcoalByproducts;
    public static Materials Cheese;
    public static Materials Chili;
    public static Materials Chocolate;
    public static Materials Cluster;
    public static Materials CoalFuel;
    public static Materials Cocoa;
    public static Materials Coffee;
    public static Materials Creosote;
    public static Materials Ethanol;
    public static Materials FishOil;
    public static Materials FermentedBiomass;
    public static Materials Fuel;
    public static Materials Glue;
    public static Materials Gunpowder;
    public static Materials FryingOilHot;
    public static Materials Honey;
    public static Materials Leather;
    public static Materials Lubricant;
    public static Materials McGuffium239;
    public static Materials MeatRaw;
    public static Materials MeatCooked;
    public static Materials Milk;
    public static Materials Mud;
    public static Materials Oil;
    public static Materials Paper;
    public static Materials Peat;
    public static Materials RareEarth;
    public static Materials Red;
    public static Materials Reinforced;
    public static Materials SeedOil;
    public static Materials SeedOilHemp;
    public static Materials SeedOilLin;
    public static Materials Stone;
    public static Materials TNT;
    public static Materials Unstable;
    public static Materials Unstableingot;
    public static Materials Vinegar;
    public static Materials Wheat;
    public static Materials WoodGas;
    public static Materials WoodTar;
    public static Materials WoodVinegar;
    public static Materials WeedEX9000;

    /**
     * TODO: This
     */
    public static Materials AluminiumBrass;
    public static Materials Osmiridium;
    public static Materials Sunnarium;
    public static Materials Endstone;
    public static Materials Netherrack;
    public static Materials SoulSand;
    /**
     * First Degree Compounds
     */
    public static Materials Methane;
    public static Materials CarbonDioxide;
    public static Materials NobleGases;
    public static Materials Air;
    public static Materials LiquidAir;
    public static Materials LiquidNitrogen;
    public static Materials LiquidOxygen;
    public static Materials SiliconDioxide;
    public static Materials Jasper;
    public static Materials Almandine;
    public static Materials Andradite;
    public static Materials AnnealedCopper;
    public static Materials Asbestos;
    public static Materials Ash;
    public static Materials BandedIron;
    public static Materials BatteryAlloy;
    public static Materials BlueTopaz;
    public static Materials Bone;
    public static Materials Brass;
    public static Materials Bronze;
    public static Materials BrownLimonite;
    public static Materials Calcite;
    public static Materials Cassiterite;
    public static Materials CassiteriteSand;
    public static Materials Chalcopyrite;
    public static Materials Charcoal;
    public static Materials Chromite;
    public static Materials ChromiumDioxide;
    public static Materials Cinnabar;
    public static Materials Water;
    public static Materials Clay;
    public static Materials Coal;
    public static Materials Cobaltite;
    public static Materials Cooperite;
    public static Materials Cupronickel;
    public static Materials DarkAsh;
    public static Materials DeepIron;
    public static Materials Diamond;
    public static Materials Electrum;
    public static Materials Emerald;
    public static Materials FreshWater;
    public static Materials Galena;
    public static Materials Garnierite;
    public static Materials Glyceryl;
    public static Materials GreenSapphire;
    public static Materials Grossular;
    public static Materials HolyWater;
    public static Materials Ice;
    public static Materials Ilmenite;
    public static Materials Rutile;
    public static Materials Bauxite;
    public static Materials Titaniumtetrachloride;
    public static Materials Magnesiumchloride;
    public static Materials Invar;
    public static Materials Kanthal;
    public static Materials Lazurite;
    public static Materials Magnalium;
    public static Materials Magnesite;
    public static Materials Magnetite;
    public static Materials Molybdenite;
    public static Materials Nichrome;
    public static Materials NiobiumNitride;
    public static Materials NiobiumTitanium;
    public static Materials NitroCarbon;
    public static Materials NitrogenDioxide;
    public static Materials Obsidian;
    public static Materials Phosphate;
    public static Materials PigIron;
    public static Materials Plastic;
    public static Materials Epoxid;
    public static Materials Polydimethylsiloxane;
    public static Materials Silicone;
    public static Materials Polycaprolactam;
    public static Materials Polytetrafluoroethylene;
    public static Materials Powellite;
    public static Materials Pumice;
    public static Materials Pyrite;
    public static Materials Pyrolusite;
    public static Materials Pyrope;
    public static Materials RockSalt;
    public static Materials Rubber;
    public static Materials RawRubber;
    public static Materials Ruby;
    public static Materials Salt;
    public static Materials Saltpeter;
    public static Materials Sapphire;
    public static Materials Scheelite;
    public static Materials Snow;
    public static Materials Sodalite;
    public static Materials SodiumPersulfate;
    public static Materials SodiumSulfide;
    public static Materials HydricSulfide;

    public static Materials OilHeavy;
    public static Materials OilMedium;
    public static Materials OilLight;

    public static Materials NatruralGas;
    public static Materials SulfuricGas;
    public static Materials Gas;
    public static Materials SulfuricNaphtha;
    public static Materials SulfuricLightFuel;
    public static Materials SulfuricHeavyFuel;
    public static Materials Naphtha;
    public static Materials LightFuel;
    public static Materials HeavyFuel;
    public static Materials LPG;

    public static Materials FluidNaquadahFuel;
    public static Materials EnrichedNaquadria;

    public static Materials ReinforceGlass;
    public static Materials BioMediumRaw;
    public static Materials BioMediumSterilized;

    public static Materials Chlorobenzene;
    public static Materials DilutedHydrochloricAcid;
    public static Materials Pyrochlore;

    public static Materials GrowthMediumRaw;
    public static Materials GrowthMediumSterilized;

    public static Materials FerriteMixture;
    public static Materials NickelZincFerrite;

    public static Materials Massicot;
    public static Materials ArsenicTrioxide;
    public static Materials CobaltOxide;
    public static Materials Zincite;
    public static Materials AntimonyTrioxide;
    public static Materials CupricOxide;
    public static Materials Ferrosilite;

    public static Materials Magnesia;
    public static Materials Quicklime;
    public static Materials Potash;
    public static Materials SodaAsh;

    public static Materials BioDiesel;
    public static Materials NitrationMixture;
    public static Materials Glycerol;
    public static Materials SodiumBisulfate;
    public static Materials PolyphenyleneSulfide;
    public static Materials Dichlorobenzene;
    public static Materials Polystyrene;
    public static Materials Styrene;
    public static Materials Isoprene;
    public static Materials Tetranitromethane;
    public static Materials Ethenone;
    public static Materials Ethane;
    public static Materials Propane;
    public static Materials Butane;
    public static Materials Butene;
    public static Materials Butadiene;
    public static Materials RawStyreneButadieneRubber;
    public static Materials StyreneButadieneRubber;
    public static Materials Toluene;
    public static Materials Epichlorohydrin;
    public static Materials PolyvinylChloride;
    public static Materials VinylChloride;
    public static Materials SulfurDioxide;
    public static Materials SulfurTrioxide;
    public static Materials NitricAcid;
    public static Materials Dimethylhydrazine;
    public static Materials Chloramine;
    public static Materials Dimethylamine;
    public static Materials DinitrogenTetroxide;
    public static Materials NitricOxide;
    public static Materials Ammonia;
    public static Materials Dimethyldichlorosilane;
    public static Materials Chloromethane;
    public static Materials PhosphorousPentoxide;
    public static Materials Tetrafluoroethylene;
    public static Materials HydrofluoricAcid;
    public static Materials Chloroform;
    public static Materials BisphenolA;
    public static Materials AceticAcid;
    public static Materials CalciumAcetateSolution;
    public static Materials Acetone;
    public static Materials Methanol;
    public static Materials CarbonMonoxide;
    public static Materials MetalMixture;
    public static Materials Ethylene;
    public static Materials Propene;
    public static Materials VinylAcetate;
    public static Materials PolyvinylAcetate;
    public static Materials MethylAcetate;
    public static Materials AllylChloride;
    public static Materials HydrochloricAcid;
    public static Materials HypochlorousAcid;
    public static Materials SodiumOxide;
    public static Materials SodiumHydroxide;
    public static Materials Benzene;
    public static Materials Phenol;
    public static Materials Cumene;
    public static Materials PhosphoricAcid;
    public static Materials SaltWater;
    public static Materials IronIIIChloride;
    public static Materials LifeEssence;

    //Roasted Ore Dust
    public static Materials RoastedCopper;
    public static Materials RoastedAntimony;
    public static Materials RoastedIron;
    public static Materials RoastedNickel;
    public static Materials RoastedZinc;
    public static Materials RoastedCobalt;
    public static Materials RoastedArsenic;
    public static Materials RoastedLead;

    //Silicon Line
    public static Materials SiliconSG;
    public static Materials CalciumDisilicide;
    public static Materials SiliconTetrafluoride;
    public static Materials SiliconTetrachloride;
    public static Materials Trichlorosilane;
    public static Materials Hexachlorodisilane;
    public static Materials Dichlorosilane;
    public static Materials Silane;
    public static Materials Calciumhydride;
    public static Materials AluminiumFluoride;

    public static Materials SolderingAlloy;
    public static Materials GalliumArsenide;
    public static Materials IndiumGalliumPhosphide;
    public static Materials Spessartine;
    public static Materials Sphalerite;
    public static Materials StainlessSteel;
    public static Materials Steel;
    public static Materials Stibnite;
    public static Materials SulfuricAcid;
    public static Materials Tanzanite;
    public static Materials Tetrahedrite;
    public static Materials TinAlloy;
    public static Materials Topaz;
    public static Materials Tungstate;
    public static Materials Ultimet;
    public static Materials Uraninite;
    public static Materials Uvarovite;
    public static Materials VanadiumGallium;
    public static Materials Wood;
    public static Materials WroughtIron;
    public static Materials Wulfenite;
    public static Materials YellowLimonite;
    public static Materials YttriumBariumCuprate;

    /**
     * Second Degree Compounds
     */
    public static Materials WoodSealed;
    public static Materials LiveRoot;
    public static Materials IronWood;
    public static Materials Glass;
    public static Materials BorosilicateGlass;
    public static Materials Perlite;
    public static Materials Borax;
    public static Materials Lignite;
    public static Materials Olivine;
    public static Materials Opal;
    public static Materials Amethyst;
    public static Materials Redstone;
    public static Materials Lapis;
    public static Materials Blaze;
    public static Materials EnderPearl;
    public static Materials EnderEye;
    public static Materials Flint;
    public static Materials Diatomite;
    public static Materials VolcanicAsh;
    public static Materials Niter;
    public static Materials Pyrotheum;
    public static Materials Cryotheum;
    public static Materials HydratedCoal;
    public static Materials Apatite;
    public static Materials Alumite;
    public static Materials Manyullyn;
    public static Materials Steeleaf;
    public static Materials Knightmetal;
    public static Materials SterlingSilver;
    public static Materials RoseGold;
    public static Materials BlackBronze;
    public static Materials BismuthBronze;
    public static Materials BlackSteel;
    public static Materials RedSteel;
    public static Materials BlueSteel;
    public static Materials DamascusSteel;
    public static Materials TungstenSteel;
    public static Materials NitroCoalFuel;
    public static Materials NitroFuel;
    public static Materials RedAlloy;
    public static Materials CobaltBrass;
    public static Materials TricalciumPhosphate;
    public static Materials Basalt;
    public static Materials GarnetRed;
    public static Materials GarnetYellow;
    public static Materials Marble;
    public static Materials Sugar;
    public static Materials Thaumium;
    public static Materials Vinteum;
    public static Materials Vis;
    public static Materials Redrock;
    public static Materials PotassiumFeldspar;
    public static Materials Biotite;
    public static Materials GraniteBlack;
    public static Materials GraniteRed;
    public static Materials Chrysotile;
    public static Materials Realgar;
    public static Materials VanadiumMagnetite;
    public static Materials BasalticMineralSand;
    public static Materials GraniticMineralSand;
    public static Materials GarnetSand;
    public static Materials QuartzSand;
    public static Materials Bastnasite;
    public static Materials Pentlandite;
    public static Materials Spodumene;
    public static Materials Pollucite;
    public static Materials Tantalite;
    public static Materials Lepidolite;
    public static Materials Glauconite;
    public static Materials GlauconiteSand;
    public static Materials Vermiculite;
    public static Materials Bentonite;
    public static Materials FullersEarth;
    public static Materials Pitchblende;
    public static Materials Monazite;
    public static Materials Malachite;
    public static Materials Mirabilite;
    public static Materials Mica;
    public static Materials Trona;
    public static Materials Barite;
    public static Materials Gypsum;
    public static Materials Alunite;
    public static Materials Dolomite;
    public static Materials Wollastonite;
    public static Materials Zeolite;
    public static Materials Kyanite;
    public static Materials Kaolinite;
    public static Materials Talc;
    public static Materials Soapstone;
    public static Materials Concrete;
    public static Materials IronMagnetic;
    public static Materials SteelMagnetic;
    public static Materials NeodymiumMagnetic;
    public static Materials SamariumMagnetic;
    public static Materials TungstenCarbide;
    public static Materials VanadiumSteel;
    public static Materials HSSG;
    public static Materials HSSE;
    public static Materials HSSS;
    public static Materials TPV;
    public static Materials DilutedSulfuricAcid;
    public static Materials EpoxidFiberReinforced;
    public static Materials SodiumCarbonate;
    public static Materials SodiumAluminate;
    public static Materials Aluminiumoxide;
    public static Materials Aluminiumhydroxide;
    public static Materials Cryolite;
    public static Materials RedMud;

    public static Materials Brick;
    public static Materials Fireclay;

    // Polybenzimidazole stuff
    public static Materials PotassiumNitrade;
    public static Materials ChromiumTrioxide;
    public static Materials Nitrochlorobenzene;
    public static Materials Dimethylbenzene;
    public static Materials Potassiumdichromate;
    public static Materials PhthalicAcid;
    public static Materials Dichlorobenzidine;
    public static Materials Diaminobenzidin;
    public static Materials Diphenylisophthalate;
    public static Materials Polybenzimidazole;

    //Gasoline
    public static Materials MTBEMixture;
    public static Materials MTBEMixtureAlt;
    public static Materials NitrousOxide;
    public static Materials AntiKnock;
    public static Materials Octane;
    public static Materials GasolineRaw;
    public static Materials GasolineRegular;
    public static Materials GasolinePremium;

    //ADDED
    public static Materials Electrotine;
    public static Materials Galgadorian;
    public static Materials EnhancedGalgadorian;
    public static Materials BloodInfusedIron;
    public static Materials Shadow;

    /**
     * Galaxy Space 1.10 compat from Version 2.6
     */
    public static Materials Ledox;
    public static Materials Quantium;
    public static Materials Mytryl;
    public static Materials BlackPlutonium;
    public static Materials CallistoIce;
    public static Materials Duralumin;
    public static Materials Oriharukon;
    public static Materials MysteriousCrystal;

    public static Materials RedstoneAlloy;
    public static Materials Soularium;
    public static Materials ConductiveIron;
    public static Materials ElectricalSteel;
    public static Materials EnergeticAlloy;
    public static Materials VibrantAlloy;
    public static Materials PulsatingIron;
    public static Materials DarkSteel;
    public static Materials EndSteel;
    public static Materials CrudeSteel;
    public static Materials CrystallineAlloy;
    public static Materials MelodicAlloy;
    public static Materials StellarAlloy;
    public static Materials CrystallinePinkSlime;
    public static Materials EnergeticSilver;
    public static Materials VividAlloy;
    public static Materials Enderium;
    public static Materials Mithril;
    public static Materials BlueAlloy;
    public static Materials ShadowIron;
    public static Materials ShadowSteel;
    public static Materials AstralSilver;

    /**
     * Op materials (draconic evolution above)
     */
    public static Materials InfinityCatalyst;
    public static Materials Infinity;
    public static Materials Bedrockium;
    public static Materials Trinium;
    public static Materials Ichorium;
    public static Materials CosmicNeutronium;

    // Superconductor base.
    public static Materials Pentacadmiummagnesiumhexaoxid;
    public static Materials Titaniumonabariumdecacoppereikosaoxid;
    public static Materials Uraniumtriplatinid;
    public static Materials Vanadiumtriindinid;
    public static Materials Tetraindiumditindibariumtitaniumheptacoppertetrakaidekaoxid;
    public static Materials Tetranaquadahdiindiumhexaplatiumosminid;
    public static Materials Longasssuperconductornameforuvwire;
    public static Materials Longasssuperconductornameforuhvwire;
    public static Materials SuperconductorUEVBase;
    public static Materials SuperconductorUIVBase;
    public static Materials SuperconductorUMVBase;

    // Superconductors.
    public static Materials SuperconductorMV;
    public static Materials SuperconductorHV;
    public static Materials SuperconductorEV;
    public static Materials SuperconductorIV;
    public static Materials SuperconductorLuV;
    public static Materials SuperconductorZPM;
    public static Materials SuperconductorUV;
    public static Materials SuperconductorUHV;
    public static Materials SuperconductorUEV;
    public static Materials SuperconductorUIV;
    public static Materials SuperconductorUMV;

    public static Materials SuperCoolant;

    public static Materials EnrichedHolmium;

    public static Materials TengamPurified;
    public static Materials TengamAttuned;
    public static Materials TengamRaw;

    // Activated Carbon Line and waterline chemicals
    public static Materials ActivatedCarbon;
    public static Materials PreActivatedCarbon;
    public static Materials DirtyActivatedCarbon;
    public static Materials PolyAluminiumChloride;
    public static Materials Ozone;
    public static Materials StableBaryonicMatter;

    public static Materials RawRadox;
    public static Materials RadoxSuperLight;
    public static Materials RadoxLight;
    public static Materials RadoxHeavy;
    public static Materials RadoxSuperHeavy;
    public static Materials Xenoxene;
    public static Materials DilutedXenoxene;
    public static Materials RadoxCracked;
    public static Materials RadoxGas;
    public static Materials RadoxPolymer;


    // spotless:on

    static {
        // Load all materials, this has been split into different classes to make
        // class too large errors disappear
        MaterialsInit1.load();
    }

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
