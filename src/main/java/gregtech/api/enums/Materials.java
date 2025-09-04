package gregtech.api.enums;

import static gregtech.api.enums.FluidState.GAS;
import static gregtech.api.enums.GTValues.M;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.util.GTUtility.formatStringSafe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.TCAspects.TC_AspectStack;
import gregtech.api.fluid.GTFluidFactory;
import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.IMaterialHandler;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Gregtech;
import gregtech.common.render.items.CosmicNeutroniumRenderer;
import gregtech.common.render.items.GaiaSpiritRenderer;
import gregtech.common.render.items.GeneratedMaterialRenderer;
import gregtech.common.render.items.GlitchEffectRenderer;
import gregtech.common.render.items.InfinityRenderer;
import gregtech.common.render.items.RainbowOverlayRenderer;
import gregtech.common.render.items.TranscendentMetalRenderer;
import gregtech.common.render.items.UniversiumRenderer;
import gregtech.loaders.materialprocessing.ProcessingConfig;
import gregtech.loaders.materialprocessing.ProcessingModSupport;
import gregtech.loaders.materials.MaterialsInit;

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
    public static Materials _NULL = new Materials(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "NULL", "NULL", 0, 0, 0, 0, false, false, 1, 1, 1, Dyes._NULL, Element._NULL, Collections.singletonList(new TC_AspectStack(TCAspects.VACUOS, 1)));
    // spotless:on

    // Elements
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
    public static Materials Desh; // Not a real element
    public static Materials Dysprosium;
    public static Materials Empty; // Not a real element
    public static Materials Erbium;
    public static Materials Europium;
    public static Materials Flerovium;
    public static Materials Fluorine;
    public static Materials Gadolinium;
    public static Materials Gallium;
    public static Materials Gold;
    public static Materials Holmium;
    public static Materials Hydrogen;
    public static Materials Helium;
    public static Materials Indium;
    public static Materials Iridium;
    public static Materials Iron;
    public static Materials Lanthanum;
    public static Materials Lead;
    public static Materials Lithium;
    public static Materials Lutetium;
    public static Materials Magic; // Not a real element
    public static Materials Magnesium;
    public static Materials Manganese;
    public static Materials Mercury;
    public static Materials MeteoricIron; // Not a real element
    public static Materials Molybdenum;
    public static Materials Naquadah; // Not a real element
    public static Materials Neodymium;
    public static Materials Neutronium;
    public static Materials Nickel;
    public static Materials Niobium;
    public static Materials Nitrogen;
    public static Materials Oriharukon; // Not a real element
    public static Materials Osmium;
    public static Materials Oxygen;
    public static Materials Palladium;
    public static Materials Phosphorus;
    public static Materials Platinum;
    public static Materials Plutonium;
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
    public static Materials Tritanium; // Not a real element
    public static Materials Tungsten;
    public static Materials Uranium;
    public static Materials Vanadium;
    public static Materials Ytterbium;
    public static Materials Yttrium;
    public static Materials Zinc;

    // Isotopes
    public static Materials Deuterium;
    public static Materials Helium_3;
    public static Materials Plutonium241;
    public static Materials Tritium;
    public static Materials Uranium235;

    // Water Line
    public static Materials FlocculationWasteLiquid;
    public static Materials Grade1PurifiedWater;
    public static Materials Grade2PurifiedWater;
    public static Materials Grade3PurifiedWater;
    public static Materials Grade4PurifiedWater;
    public static Materials Grade5PurifiedWater;
    public static Materials Grade6PurifiedWater;
    public static Materials Grade7PurifiedWater;
    public static Materials Grade8PurifiedWater;

    // Random
    public static Materials AnyBronze;
    public static Materials AnyCopper;
    public static Materials AnyIron;
    public static Materials AnyRubber;
    public static Materials AnySyntheticRubber;
    public static Materials BrickNether;
    public static Materials Cobblestone;
    public static Materials Crystal;
    public static Materials Metal;
    public static Materials Organic;
    public static Materials Quartz;
    public static Materials Unknown;

    /*
     * The "I don't care" Section, everything I don't want to do anything with right now, is right here. Just to make
     * the Material Finder shut up about them. But I do see potential uses in some of these Materials.
     */
    public static Materials Alfium;
    public static Materials Aquamarine;
    public static Materials DarkThaumium;
    public static Materials Draconium;
    public static Materials DraconiumAwakened;
    public static Materials Ender;
    public static Materials Fluix;
    public static Materials Flux;
    public static Materials HeeEndium;
    public static Materials InfusedTeslatite;
    public static Materials IridiumSodiumOxide;
    public static Materials Mutation;
    public static Materials OsmiumTetroxide;
    public static Materials PhasedGold;
    public static Materials PhasedIron;
    public static Materials PlatinumGroupSludge;
    public static Materials PurpleAlloy;
    public static Materials RubberTreeSap;
    public static Materials Serpentine;
    public static Materials SodiumPeroxide;
    public static Materials Teslatite;

    // Unknown Material Components. Dead End Section.
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
    public static Materials Bloodstone;
    public static Materials Blueschist;
    public static Materials Bluestone;
    public static Materials Blutonium;
    public static Materials Carmot;
    public static Materials Celenegil;
    public static Materials CertusQuartz;
    public static Materials CertusQuartzCharged;
    public static Materials Ceruclase;
    public static Materials Chert;
    public static Materials Chimerite;
    public static Materials Chrysocolla;
    public static Materials Citrine;
    public static Materials CobaltHexahydrate;
    public static Materials ConstructionFoam;
    public static Materials Coral;
    public static Materials CrudeOil;
    public static Materials CrystalFlux;
    public static Materials Cyanite;
    public static Materials Dacite;
    public static Materials DarkIron;
    public static Materials DarkStone;
    public static Materials Demonite;
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
    public static Materials Graphene;
    public static Materials Graphite;
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
    public static Materials InfusedAir;
    public static Materials InfusedDull;
    public static Materials InfusedEarth;
    public static Materials InfusedEntropy;
    public static Materials InfusedFire;
    public static Materials InfusedGold;
    public static Materials InfusedOrder;
    public static Materials InfusedVis;
    public static Materials InfusedWater;
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
    public static Materials MeteoricSteel;
    public static Materials Meteorite;
    public static Materials Meutoite;
    public static Materials Migmatite;
    public static Materials Mimichite;
    public static Materials Moonstone;
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

    // spotless:off
    // Tier materials and aliases for the old style of tiered circuits
    public static Materials ULV; @Deprecated public static Materials Primitive;
    public static Materials  LV; @Deprecated public static Materials Basic;
    public static Materials  MV; @Deprecated public static Materials Good;
    public static Materials  HV; @Deprecated public static Materials Advanced;
    public static Materials  EV; @Deprecated public static Materials Data;
    public static Materials  IV; @Deprecated public static Materials Elite;
    public static Materials LuV; @Deprecated public static Materials Master;
    public static Materials ZPM; @Deprecated public static Materials Ultimate;
    public static Materials  UV;
    public static Materials UHV; @Deprecated public static Materials Infinite;
    public static Materials UEV; @Deprecated public static Materials Bio;
    public static Materials UIV; @Deprecated public static Materials Optical;
    public static Materials UMV; @Deprecated public static Materials Exotic;
    public static Materials UXV; @Deprecated public static Materials Cosmic;
    public static Materials MAX; @Deprecated public static Materials Transcendent;

    // Circuitry, Batteries and other Technical things
    public static Materials Resistor                = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Resistor"                ,   "Resistor"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TCAspects.ELECTRUM, 1)));
    public static Materials Diode                   = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Diode"                   ,   "Diode"                         ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TCAspects.ELECTRUM, 1)));
    public static Materials Transistor              = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Transistor"              ,   "Transistor"                    ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TCAspects.ELECTRUM, 1)));
    public static Materials Capacitor               = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Capacitor"               ,   "Capacitor"                     ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TCAspects.ELECTRUM, 1)));
    public static Materials Inductor                = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Inductor"                ,   "Inductor"                      ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TCAspects.ELECTRUM, 1)));

    public static Materials Nano                    = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Nano"                    ,   "Bio"                           ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TCAspects.ELECTRUM, 11)));
    public static Materials Piko                    = new Materials(  -1, TextureSet.SET_NONE              ,   1.0F,      0,  0, 0                         , 255, 255, 255,   0,   "Piko"                    ,   "Bio"                           ,    0,       0,         -1,    0, false, false,   1,   1,   1, Dyes.dyeLightGray   , Collections.singletonList(new TC_AspectStack(TCAspects.ELECTRUM, 12)));
    // spotless:on

    // Not possible to determine exact Components
    public static Materials AdvancedGlue;
    public static Materials Antimatter;
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
    public static Materials FermentedBiomass;
    public static Materials FishOil;
    public static Materials FryingOilHot;
    public static Materials Fuel;
    public static Materials Glue;
    public static Materials Gunpowder;
    public static Materials Honey;
    public static Materials Leather;
    public static Materials Lubricant;
    public static Materials McGuffium239;
    public static Materials MeatCooked;
    public static Materials MeatRaw;
    public static Materials Milk;
    public static Materials Mud;
    public static Materials Oil;
    public static Materials Paper;
    public static Materials Peat;
    public static Materials Protomatter;
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
    public static Materials WeedEX9000;
    public static Materials Wheat;
    public static Materials WoodGas;
    public static Materials WoodTar;
    public static Materials WoodVinegar;

    // TODO: this
    public static Materials AluminiumBrass;
    public static Materials Endstone;
    public static Materials Netherrack;
    public static Materials Osmiridium;
    public static Materials SoulSand;
    public static Materials Sunnarium;

    // Degree 1 Compounds
    public static Materials AceticAcid;
    public static Materials Acetone;
    public static Materials Air;
    public static Materials AllylChloride;
    public static Materials Almandine;
    public static Materials Ammonia;
    public static Materials Andradite;
    public static Materials AnnealedCopper;
    public static Materials AntimonyTrioxide;
    public static Materials ArsenicTrioxide;
    public static Materials Asbestos;
    public static Materials Ash;
    public static Materials BandedIron;
    public static Materials BatteryAlloy;
    public static Materials Benzene;
    public static Materials BlueTopaz;
    public static Materials Bone;
    public static Materials Brass;
    public static Materials Brick;
    public static Materials Bronze;
    public static Materials BrownLimonite;
    public static Materials Calcite;
    public static Materials CarbonDioxide;
    public static Materials Cassiterite;
    public static Materials CassiteriteSand;
    public static Materials Chalcopyrite;
    public static Materials Charcoal;
    public static Materials Chlorobenzene;
    public static Materials Chromite;
    public static Materials ChromiumDioxide;
    public static Materials Cinnabar;
    public static Materials Coal;
    public static Materials CobaltOxide;
    public static Materials Cobaltite;
    public static Materials Cooperite;
    public static Materials CupricOxide;
    public static Materials Cupronickel;
    public static Materials DarkAsh;
    public static Materials DeepIron;
    public static Materials Diamond;
    public static Materials DilutedHydrochloricAcid;
    public static Materials Electrum;
    public static Materials Emerald;
    public static Materials Epoxid;
    public static Materials FerriteMixture;
    public static Materials Ferrosilite;
    public static Materials FreshWater;
    public static Materials Galena;
    public static Materials Garnierite;
    public static Materials Glyceryl;
    public static Materials GreenSapphire;
    public static Materials Grossular;
    public static Materials HolyWater;
    public static Materials HydricSulfide;
    public static Materials Ice;
    public static Materials Ilmenite;
    public static Materials Invar;
    public static Materials Kanthal;
    public static Materials Lazurite;
    public static Materials LiquidAir;
    public static Materials LiquidNitrogen;
    public static Materials LiquidOxygen;
    public static Materials Magnalium;
    public static Materials Magnesia;
    public static Materials Magnesite;
    public static Materials Magnesiumchloride;
    public static Materials Magnetite;
    public static Materials Massicot;
    public static Materials Methane;
    public static Materials Molybdenite;
    public static Materials Nichrome;
    public static Materials NickelZincFerrite;
    public static Materials NiobiumNitride;
    public static Materials NiobiumTitanium;
    public static Materials NitroCarbon;
    public static Materials NitrogenDioxide;
    public static Materials NobleGases;
    public static Materials Obsidian;
    public static Materials Phosphate;
    public static Materials PigIron;
    public static Materials Plastic;
    public static Materials Polycaprolactam;
    public static Materials Polydimethylsiloxane;
    public static Materials Polytetrafluoroethylene;
    public static Materials Potash;
    public static Materials Powellite;
    public static Materials Pumice;
    public static Materials Pyrite;
    public static Materials Pyrochlore;
    public static Materials Pyrolusite;
    public static Materials Pyrope;
    public static Materials Quicklime;
    public static Materials RawRubber;
    public static Materials RockSalt;
    public static Materials Rubber;
    public static Materials Ruby;
    public static Materials Rutile;
    public static Materials Salt;
    public static Materials Saltpeter;
    public static Materials Sapphire;
    public static Materials Scheelite;
    public static Materials SiliconDioxide;
    public static Materials Silicone;
    public static Materials Snow;
    public static Materials SodaAsh;
    public static Materials Sodalite;
    public static Materials SodiumPersulfate;
    public static Materials SodiumSulfide;
    public static Materials Titaniumtetrachloride;
    public static Materials Water, Steam; // Steam.getGas(..) reads better than Water.getGas(..)
    public static Materials Zincite;

    // Unclassified 01 materials
    public static Materials OilExtraHeavy;
    public static Materials OilHeavy;
    public static Materials OilLight;
    public static Materials OilMedium;
    public static Materials SuperCoolant;
    public static Materials EnrichedHolmium;
    public static Materials TengamPurified;
    public static Materials TengamAttuned;
    public static Materials TengamRaw;

    // Unclassified 02 materials
    public static Materials Gas;
    public static Materials HeavyFuel;
    public static Materials LightFuel;
    public static Materials LPG;
    public static Materials Naphtha;
    public static Materials NatruralGas;
    public static Materials SulfuricGas;
    public static Materials SulfuricHeavyFuel;
    public static Materials SulfuricLightFuel;
    public static Materials SulfuricNaphtha;

    // Unclassified 03 materials
    public static Materials BioMediumRaw;
    public static Materials BioMediumSterilized;
    public static Materials ReinforceGlass;

    // Unclassified 04 materials
    public static Materials GrowthMediumRaw;
    public static Materials GrowthMediumSterilized;

    // Unclassified 05 materials
    public static Materials BioDiesel;
    public static Materials BisphenolA;
    public static Materials Butadiene;
    public static Materials Butane;
    public static Materials Butene;
    public static Materials CalciumAcetateSolution;
    public static Materials CarbonMonoxide;
    public static Materials Chloramine;
    public static Materials Chloroform;
    public static Materials Chloromethane;
    public static Materials Cumene;
    public static Materials Dichlorobenzene;
    public static Materials Dimethylamine;
    public static Materials Dimethyldichlorosilane;
    public static Materials Dimethylhydrazine;
    public static Materials DinitrogenTetroxide;
    public static Materials Epichlorohydrin;
    public static Materials Ethane;
    public static Materials Ethenone;
    public static Materials Ethylene;
    public static Materials Glycerol;
    public static Materials HydrochloricAcid;
    public static Materials HydrofluoricAcid;
    public static Materials HypochlorousAcid;
    public static Materials IronIIIChloride;
    public static Materials Isoprene;
    public static Materials LifeEssence;
    public static Materials MetalMixture;
    public static Materials Methanol;
    public static Materials MethylAcetate;
    public static Materials NitrationMixture;
    public static Materials NitricAcid;
    public static Materials NitricOxide;
    public static Materials Phenol;
    public static Materials PhosphoricAcid;
    public static Materials PhosphorousPentoxide;
    public static Materials PolyphenyleneSulfide;
    public static Materials Polystyrene;
    public static Materials PolyvinylAcetate;
    public static Materials PolyvinylChloride;
    public static Materials Propane;
    public static Materials Propene;
    public static Materials SaltWater;
    public static Materials SodiumBisulfate;
    public static Materials SodiumHydroxide;
    public static Materials SodiumOxide;
    public static Materials Styrene;
    public static Materials StyreneButadieneRubber;
    public static Materials SulfurDioxide;
    public static Materials SulfurTrioxide;
    public static Materials Tetrafluoroethylene;
    public static Materials Tetranitromethane;
    public static Materials Toluene;
    public static Materials VinylAcetate;
    public static Materials VinylChloride;

    // Roasted Ores
    public static Materials RoastedAntimony;
    public static Materials RoastedArsenic;
    public static Materials RoastedCobalt;
    public static Materials RoastedCopper;
    public static Materials RoastedIron;
    public static Materials RoastedLead;
    public static Materials RoastedNickel;
    public static Materials RoastedZinc;

    // Silicon Line
    public static Materials AluminiumFluoride;
    public static Materials CalciumDisilicide;
    public static Materials Calciumhydride;
    public static Materials Dichlorosilane;
    public static Materials Hexachlorodisilane;
    public static Materials Silane;
    public static Materials SiliconSG;
    public static Materials SiliconTetrachloride;
    public static Materials SiliconTetrafluoride;
    public static Materials Trichlorosilane;

    // Unclassified 06 materials
    public static Materials GalliumArsenide;
    public static Materials IndiumGalliumPhosphide;
    public static Materials SolderingAlloy;
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

    // Degree 2 Compounds
    public static Materials Aluminiumhydroxide;
    public static Materials Aluminiumoxide;
    public static Materials Alumite;
    public static Materials Alunite;
    public static Materials Amethyst;
    public static Materials Apatite;
    public static Materials Barite;
    public static Materials Bastnasite;
    public static Materials Bauxite;
    public static Materials Bentonite;
    public static Materials Biotite;
    public static Materials BismuthBronze;
    public static Materials BlackBronze;
    public static Materials BlackSteel;
    public static Materials Blaze;
    public static Materials Borax;
    public static Materials Chrysotile;
    public static Materials Clay;
    public static Materials CobaltBrass;
    public static Materials Concrete;
    public static Materials Cryolite;
    public static Materials DamascusSteel;
    public static Materials Diatomite;
    public static Materials DilutedSulfuricAcid;
    public static Materials Dolomite;
    public static Materials ElectricalSteel;
    public static Materials EnderPearl;
    public static Materials EpoxidFiberReinforced;
    public static Materials Flint;
    public static Materials FullersEarth;
    public static Materials GarnetRed;
    public static Materials GarnetYellow;
    public static Materials Glass;
    public static Materials Glauconite;
    public static Materials GlauconiteSand;
    public static Materials GraniteBlack;
    public static Materials GraniticMineralSand;
    public static Materials Gypsum;
    public static Materials HydratedCoal;
    public static Materials IronMagnetic;
    public static Materials Jasper;
    public static Materials Kaolinite;
    public static Materials Knightmetal;
    public static Materials Kyanite;
    public static Materials Lapis;
    public static Materials Lepidolite;
    public static Materials Lignite;
    public static Materials LiveRoot;
    public static Materials Malachite;
    public static Materials Manyullyn;
    public static Materials Marble;
    public static Materials Mica;
    public static Materials Mirabilite;
    public static Materials Monazite;
    public static Materials NeodymiumMagnetic;
    public static Materials Niter;
    public static Materials NitroCoalFuel;
    public static Materials NitroFuel;
    public static Materials Olivine;
    public static Materials Opal;
    public static Materials Pentlandite;
    public static Materials Perlite;
    public static Materials Pitchblende;
    public static Materials Pollucite;
    public static Materials PotassiumFeldspar;
    public static Materials QuartzSand;
    public static Materials RawStyreneButadieneRubber;
    public static Materials Realgar;
    public static Materials RedMud;
    public static Materials Redrock;
    public static Materials Redstone;
    public static Materials RoseGold;
    public static Materials SamariumMagnetic;
    public static Materials Soapstone;
    public static Materials SodiumAluminate;
    public static Materials SodiumCarbonate;
    public static Materials Spodumene;
    public static Materials SteelMagnetic;
    public static Materials Steeleaf;
    public static Materials SterlingSilver;
    public static Materials Sugar;
    public static Materials Talc;
    public static Materials Tantalite;
    public static Materials Thaumium;
    public static Materials TPV;
    public static Materials TricalciumPhosphate;
    public static Materials Trona;
    public static Materials TungstenCarbide;
    public static Materials TungstenSteel;
    public static Materials VanadiumMagnetite;
    public static Materials VanadiumSteel;
    public static Materials Vermiculite;
    public static Materials Vinteum;
    public static Materials Vis;
    public static Materials VolcanicAsh;
    public static Materials Wollastonite;
    public static Materials WoodSealed;
    public static Materials Zeolite;

    // Degree 3 Compounds
    public static Materials Basalt;
    public static Materials BlueSteel;
    public static Materials BorosilicateGlass;
    public static Materials Cryotheum;
    public static Materials DarkSteel;
    public static Materials EnderEye;
    public static Materials Fireclay;
    public static Materials GarnetSand;
    public static Materials HSSG;
    public static Materials IronWood;
    public static Materials Pyrotheum;
    public static Materials GraniteRed;
    public static Materials RedAlloy;
    public static Materials RedSteel;
    public static Materials RedstoneAlloy;

    // Degree 4 Compounds
    public static Materials BasalticMineralSand;
    public static Materials ConductiveIron;
    public static Materials EndSteel;
    public static Materials HSSE;
    public static Materials HSSS;
    public static Materials PulsatingIron;

    // Degree 5 Compounds
    public static Materials CrystallineAlloy;
    public static Materials EnergeticAlloy;
    public static Materials EnergeticSilver;
    public static Materials MelodicAlloy;

    // Degree 6 Compounds
    public static Materials CrystallinePinkSlime;
    public static Materials StellarAlloy;
    public static Materials VibrantAlloy;
    public static Materials VividAlloy;

    // Polybenzimidazole Line
    public static Materials ChromiumTrioxide;
    public static Materials Diaminobenzidin;
    public static Materials Dichlorobenzidine;
    public static Materials Dimethylbenzene;
    public static Materials Diphenylisophthalate;
    public static Materials Nitrochlorobenzene;
    public static Materials PhthalicAcid;
    public static Materials Polybenzimidazole;
    public static Materials PotassiumNitrade;
    public static Materials Potassiumdichromate;

    // Gasoline Line
    public static Materials AntiKnock;
    public static Materials GasolinePremium;
    public static Materials GasolineRaw;
    public static Materials GasolineRegular;
    public static Materials MTBEMixture;
    public static Materials MTBEMixtureAlt;
    public static Materials NitrousOxide;
    public static Materials Octane;

    // Added
    public static Materials BloodInfusedIron;
    public static Materials Electrotine;
    public static Materials EnhancedGalgadorian;
    public static Materials Galgadorian;
    public static Materials Shadow;

    // Galaxy Space 1.10 compat from Version 2.6
    public static Materials BlackPlutonium;
    public static Materials CallistoIce;
    public static Materials Duralumin;
    public static Materials Ledox;
    public static Materials MysteriousCrystal;
    public static Materials Mytryl;
    public static Materials Quantium;

    // Unclassified 07 materials
    public static Materials AstralSilver;
    public static Materials BlueAlloy;
    public static Materials CrudeSteel;
    public static Materials Enderium;
    public static Materials Mithril;
    public static Materials ShadowIron;
    public static Materials ShadowSteel;
    public static Materials Soularium;

    // Overpowered Materials (Draconic Evolution & above)
    public static Materials Bedrockium;
    public static Materials CosmicNeutronium;
    public static Materials Ichorium;
    public static Materials Infinity;
    public static Materials InfinityCatalyst;
    public static Materials Trinium;

    // Superconductor Bases
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

    // Superconductors
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

    public static Materials DenseSteam = makeDenseSteam();

    private static Materials makeDenseSteam() {
        return new MaterialBuilder(232, TextureSet.SET_FLUID, "Dense Steam").addCell()
            .addGas()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .constructMaterial();
    }

    public static Materials DenseSuperheatedSteam = makeDenseSuperheatedSteam();

    private static Materials makeDenseSuperheatedSteam() {
        return new MaterialBuilder(233, TextureSet.SET_FLUID, "Dense Superheated Steam").addCell()
            .addGas()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .constructMaterial();
    }

    public static Materials DenseSupercriticalSteam = makeDenseSupercriticalSteam();

    private static Materials makeDenseSupercriticalSteam() {
        return new MaterialBuilder(234, TextureSet.SET_FLUID, "Dense Supercritical Steam").addCell()
            .addGas()
            .setRGB(255, 255, 255)
            .setColor(Dyes.dyeWhite)
            .constructMaterial();
    }

    // Waterline Chemicals
    public static Materials ActivatedCarbon;
    public static Materials PreActivatedCarbon;
    public static Materials DirtyActivatedCarbon;
    public static Materials PolyAluminiumChloride;
    public static Materials Ozone;
    public static Materials StableBaryonicMatter;

    // Radox Line
    public static Materials DilutedXenoxene;
    public static Materials RadoxCracked;
    public static Materials RadoxGas;
    public static Materials RadoxHeavy;
    public static Materials RadoxLight;
    public static Materials RadoxPolymer;
    public static Materials RadoxSuperHeavy;
    public static Materials RadoxSuperLight;
    public static Materials RawRadox;
    public static Materials Xenoxene;

    // Netherite Line
    public static Materials NetherAir;
    public static Materials NetherSemiFluid;
    public static Materials NefariousGas;
    public static Materials NefariousOil;
    public static Materials PoorNetherWaste;
    public static Materials RichNetherWaste;
    public static Materials HellishMetal;
    public static Materials Netherite;

    // Prismatic Acid Line
    public static Materials ActivatedNetherite;
    public static Materials PrismarineSolution;
    public static Materials PrismarineContaminatedHydrogenPeroxide;
    public static Materials PrismarineRichNitrobenzeneSolution;
    public static Materials PrismarineContaminatedNitrobenzeSolution;
    public static Materials PrismaticGas;
    public static Materials PrismaticAcid;
    public static Materials PrismaticNaquadah;
    public static Materials PrismaticNaquadahCompositeSlurry;

    // Magic Materials
    public static Materials ComplexityCatalyst;
    public static Materials EntropicCatalyst;

    static {
        MaterialsInit.load();
        MaterialsBotania.load();
        MaterialsKevlar.load();
        MaterialsOreAlum.load();
        MaterialsUEVplus.load();
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
    public static Materials Superconductor = new Materials(SuperconductorUHV, true);

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
        setByProducts();
        setColors();
    }

    public final short[] mRGBa = new short[] { 255, 255, 255, 0 }, mMoltenRGBa = new short[] { 255, 255, 255, 0 };
    public TextureSet mIconSet;
    public GeneratedMaterialRenderer renderer;
    public List<MaterialStack> mMaterialList = new ArrayList<>();
    public List<Materials> mOreByProducts = new ArrayList<>(), mOreReRegistrations = new ArrayList<>();
    public List<TCAspects.TC_AspectStack> mAspects = new ArrayList<>();
    public ArrayList<ItemStack> mMaterialItems = new ArrayList<>();
    public Collection<SubTag> mSubTags = new LinkedHashSet<>();
    public Enchantment mEnchantmentTools = null, mEnchantmentArmors = null;
    public boolean mUnificatable, mBlastFurnaceRequired = false, mAutoGenerateBlastFurnaceRecipes = true,
        mAutoGenerateVacuumFreezerRecipes = true, mAutoGenerateRecycleRecipes = true, mHasParentMod = true,
        mHasPlasma = false, mHasGas = false, mCustomOre = false;
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

    /*
     * DOCUMENTATION OUTDATED
     * @param aMetaItemSubID the Sub-ID used in my own MetaItems. Range 0-1000. -1 for no Material
     * @param aTypes which kind of Items should be generated. Bitmask as follows: 1 = Dusts of all kinds.
     * 2 = Dusts, Ingots, Plates, Rods/Sticks, Machine Components and other Metal specific
     * things. 4 = Dusts, Gems, Plates, Lenses (if transparent). 8 = Dusts, Impure Dusts,
     * crushed Ores, purified Ores, centrifuged Ores etc. 16 = Cells 32 = Plasma Cells 64 =
     * Tool Heads 128 = Gears 256 = Designates something as empty (only used for the Empty
     * material)
     * @param aR, aG, aB Color of the Material 0-255 each.
     * @param aA transparency of the Material Texture. 0 = fully visible, 255 = Invisible.
     * @param aName The Name used as Default for localization.
     * @param aFuelType Type of Generator to get Energy from this Material.
     * @param aFuelPower EU generated. Will be multiplied by 1000, also additionally multiplied by 2 for
     * Gems.
     * @param aMeltingPoint Used to determine the smelting Costs in furnace. >>>>**ADD 20000 to remove EBF
     * recipes to add them MANUALLY ! :D**<<<<
     * @param aBlastFurnaceTemp Used to determine the needed Heat capacity Costs in Blast Furnace.
     * @param aBlastFurnaceRequired If this requires a Blast Furnace.
     * @param aColor Vanilla MC Wool Color which comes the closest to this.
     */
    protected Materials(
        // spotless:off
        String name,
        String defaultLocalName,
        @Nullable Element element,
        @Nullable String chemicalFormula,
        int metaItemSubID,
        TextureSet iconSet,
        Dyes color,
        int argb,
        int toolDurability, int toolQuality, float toolSpeed,
        float steamMultiplier, float gasMultiplier, float plasmaMultiplier,
        int fuelType, int fuelPower,
        boolean hasFluid,
        boolean hasGas,
        int types,
        int extraData,
        int meltingPoint,
        int blastFurnaceTemp,
        boolean blastFurnaceRequired,
        boolean autoGenerateBlastFurnaceRecipes,
        boolean autoGenerateVacuumFreezerRecipes,
        int densityMultiplier,
        int densityDivider,
        List<MaterialStack> materialList,
        List<TCAspects.TC_AspectStack> aspects,
        LinkedHashSet<SubTag> subTags
        // spotless:on
    ) {

        // Set names
        mName = name;
        mDefaultLocalName = defaultLocalName;
        MATERIALS_MAP.put(mName, this);
        mMetaItemSubID = metaItemSubID;

        // Set element
        if (element != null) {
            mElement = element;
            mElement.mLinkedMaterials.add(this);
        }

        // Set Chemical formula
        if (chemicalFormula != null) {
            mChemicalFormula = chemicalFormula;
        } else if (element != null) {
            mChemicalFormula = element.toString();
        } else if (materialList.size() == 1) {
            mChemicalFormula = materialList.get(0)
                .toString(true);
        } else if (materialList.size() > 1) {
            mChemicalFormula = materialList.stream()
                .map(MaterialStack::toString)
                .collect(Collectors.joining())
                .replaceAll("_", "-");
        }

        // Set texture and colors
        mIconSet = iconSet;
        mColor = color;
        mRGBa[0] = mMoltenRGBa[0] = (short) ((argb >>> 16) & 0xFF);
        mRGBa[1] = mMoltenRGBa[1] = (short) ((argb >>> 8) & 0xFF);
        mRGBa[2] = mMoltenRGBa[2] = (short) (argb & 0xFF);
        mRGBa[3] = mMoltenRGBa[3] = (short) ((argb >>> 24) & 0xFF);

        // Set tool properties
        mDurability = toolDurability;
        mToolSpeed = toolSpeed;
        mToolQuality = (byte) toolQuality;

        // Set turbine properties
        mSteamMultiplier = steamMultiplier;
        mGasMultiplier = gasMultiplier;
        mPlasmaMultiplier = plasmaMultiplier;

        // Set fuel properties
        mFuelPower = fuelPower;
        mFuelType = fuelType;

        // Set auto-generated parts and recipes
        mTypes = types;
        mExtraData = extraData;
        hasCorrespondingFluid = hasFluid;
        hasCorrespondingGas = hasGas;
        mBlastFurnaceRequired = blastFurnaceRequired;
        mBlastFurnaceTemp = (short) blastFurnaceTemp;
        mAutoGenerateBlastFurnaceRecipes = autoGenerateBlastFurnaceRecipes;
        mAutoGenerateVacuumFreezerRecipes = autoGenerateVacuumFreezerRecipes;

        // Set what materials this material is composed of
        mMaterialList = materialList;

        // Set material density
        mDensityMultiplier = densityMultiplier;
        mDensityDivider = densityDivider;
        mDensity = (M * densityMultiplier) / densityDivider;

        // Constant fields
        mCustomOre = false;
        mCustomID = "null";
        mConfigSection = "ore";
        mUnificatable = true;
        mMaterialInto = this;

        // Set material SubTags
        mSubTags = subTags;
        if (mColor != null) mSubTags.add(SubTag.HAS_COLOR);
        if ((mTypes & 2) != 0) mSubTags.add(SubTag.SMELTING_TO_FLUID);

        // No clue what is going on here...
        mMeltingPoint = meltingPoint;

        int numberOfComponents = 0;
        int tMeltingPoint = 0;
        for (MaterialStack tMaterial : mMaterialList) {
            numberOfComponents += tMaterial.mAmount;
            if (tMaterial.mMaterial.mMeltingPoint > 0) {
                tMeltingPoint += tMaterial.mMaterial.mMeltingPoint * tMaterial.mAmount;
            }
            if (aspects == null) {
                for (TC_AspectStack tAspect : tMaterial.mMaterial.mAspects) {
                    tAspect.addToAspectList(mAspects);
                }
            }
        }

        if (mMeltingPoint < 0) mMeltingPoint = 0;

        numberOfComponents *= densityMultiplier;
        numberOfComponents /= densityDivider;
        if (aspects == null) {
            for (TC_AspectStack tAspect : mAspects) {
                tAspect.mAmount = Math.max(1, tAspect.mAmount / Math.max(1, numberOfComponents));
            }
        } else {
            mAspects.addAll(aspects);
        }
    }

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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

    @Deprecated
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
        mFuelPower = aFuelPower;
        mFuelType = aFuelType;
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
        if ((mTypes & 2) != 0) add(SubTag.SMELTING_TO_FLUID);
    }

    @Deprecated
    public Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality,
        int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType,
        int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent,
        int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor,
        List<TCAspects.TC_AspectStack> aAspects) {
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

    @Deprecated
    public Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality,
        int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType,
        int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent,
        int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, Element aElement,
        List<TCAspects.TC_AspectStack> aAspects) {
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

    @Deprecated
    public Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality,
        int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType,
        int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent,
        int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, int aExtraData,
        List<MaterialStack> aMaterialList) {
        this(
            // spotless:off
            aName,
            aDefaultLocalName,
            null,
            "?",
            aMetaItemSubID,
            aIconSet,
            aColor,
            ((aA & 0xFF) << 24) | ((aR & 0xFF) << 16) | ((aG & 0xFF) << 8) | (aB & 0xFF),
            aDurability, aToolQuality, aToolSpeed,
            1.0f, 1.0f, 1.0f,
            aFuelType, aFuelPower,
            false,
            false,
            aTypes,
            aExtraData,
            aMeltingPoint,
            aBlastFurnaceTemp,
            aBlastFurnaceRequired,
            true,
            true,
            aDensityMultiplier,
            aDensityDivider,
            aMaterialList,
            new ArrayList<>(),
            new LinkedHashSet<>()
            // spotless:on
        );
    }

    @Deprecated
    public Materials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality,
        int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType,
        int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent,
        int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, int aExtraData,
        List<MaterialStack> aMaterialList, List<TCAspects.TC_AspectStack> aAspects) {
        this(
            // spotless:off
            aName,
            aDefaultLocalName,
            null,
            "?",
            aMetaItemSubID,
            aIconSet,
            aColor,
            ((aA & 0xFF) << 24) | ((aR & 0xFF) << 16) | ((aG & 0xFF) << 8) | (aB & 0xFF),
            aDurability, aToolQuality, aToolSpeed,
            1.0f, 1.0f, 1.0f,
            aFuelType, aFuelPower,
            false,
            false,
            aTypes,
            aExtraData,
            aMeltingPoint,
            aBlastFurnaceTemp,
            aBlastFurnaceRequired,
            true,
            true,
            aDensityMultiplier,
            aDensityDivider,
            aMaterialList,
            aAspects,
            new LinkedHashSet<>()
            // spotless:on
        );
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
        Coal.setOreMultiplier(2)
            .setByProductMultiplier(2);
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

    private static void initSubTags() {
        Superconductor.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING); // Todo: remove this once it will be fully
        // deprecated
        // SuperconductorUV .add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        SuperconductorUHV.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
    }

    public static void init() {
        new ProcessingConfig();
        if (!GTMod.proxy.mEnableAllMaterials) new ProcessingModSupport();
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
        OrePrefixes.ingotHot.disableComponent(MaterialsUEVplus.HotProtoHalkonite);
        OrePrefixes.ingotHot.disableComponent(MaterialsUEVplus.ProtoHalkonite);
        OrePrefixes.ingotHot.disableComponent(MaterialsUEVplus.HotExoHalkonite);
        OrePrefixes.ingotHot.disableComponent(MaterialsUEVplus.ExoHalkonite);
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
        MaterialsUEVplus.SixPhasedCopper.renderer = new GlitchEffectRenderer();
        MaterialsUEVplus.GravitonShard.renderer = new InfinityRenderer();
        MaterialsUEVplus.ExoHalkonite.renderer = new InfinityRenderer();
        MaterialsUEVplus.HotExoHalkonite.renderer = new InfinityRenderer();
        Materials.PrismaticNaquadah.renderer = new RainbowOverlayRenderer(Materials.PrismaticNaquadah.getRGBA());
    }

    private static void fillGeneratedMaterialsMap() {
        for (Materials aMaterial : MATERIALS_ARRAY) {
            if (aMaterial.mMetaItemSubID >= 0) {
                if (aMaterial.mMetaItemSubID < 1000) {
                    if (aMaterial.mHasParentMod) {
                        if (GregTechAPI.sGeneratedMaterials[aMaterial.mMetaItemSubID] == null) {
                            GregTechAPI.sGeneratedMaterials[aMaterial.mMetaItemSubID] = aMaterial;
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
        // Moved from GTProxy? (Not sure)
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

        if (aMaterial.mHasGas) {
            GTFluidFactory
                .of(aMaterial.mName.toLowerCase(), aMaterial.mDefaultLocalName, aMaterial, GAS, aMaterial.mGasTemp);
        }
    }

    private static String getConfigPath(Materials aMaterial) {
        String cOre = aMaterial.mCustomOre ? aMaterial.mCustomID : aMaterial.mName;
        return "materials." + aMaterial.mConfigSection + "." + cOre;
    }

    private static void addHarvestLevelNerfs(Materials aMaterial) {
        /* Moved the harvest level changes from GTMod to have fewer things iterating over MATERIALS_ARRAY */
        if (GTMod.proxy.mChangeHarvestLevels && aMaterial.mToolQuality > 0
            && aMaterial.mMetaItemSubID < GTMod.proxy.mHarvestLevel.length
            && aMaterial.mMetaItemSubID >= 0) {
            GTMod.proxy.mHarvestLevel[aMaterial.mMetaItemSubID] = aMaterial.mToolQuality;
        }
    }

    private static void addHarvestLevels() {
        GTMod.proxy.mChangeHarvestLevels = Gregtech.harvestLevel.activateHarvestLevelChange;
        GTMod.proxy.mMaxHarvestLevel = Math.min(15, Gregtech.harvestLevel.maxHarvestLevel);
        GTMod.proxy.mGraniteHavestLevel = Gregtech.harvestLevel.graniteHarvestLevel;
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

    public static @NotNull Materials get(String aMaterialName) {
        return getWithFallback(aMaterialName, Materials._NULL);
    }

    public static @NotNull Materials getWithFallback(String name, @NotNull Materials fallback) {
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
            Materials aMaterial = GregTechAPI.sGeneratedMaterials[aMaterialID];
            if (aMaterial != null) return aMaterial.getLocalizedNameForItem(aFormat);
        }
        return aFormat;
    }

    public static Collection<Materials> getAll() {
        return MATERIALS_MAP.values();
    }

    /**
     * @deprecated Use {@link MaterialBuilder#setAutoGenerateBlastFurnaceRecipes(boolean)} on a `MaterialBuilder`
     *             instead.
     */
    @Deprecated
    public Materials disableAutoGeneratedBlastFurnaceRecipes() {
        mAutoGenerateBlastFurnaceRecipes = false;
        return this;
    }

    /**
     * @deprecated Use {@link MaterialBuilder#setAutoGeneratedVacuumFreezerRecipe(boolean)} on a `MaterialBuilder`
     *             instead.
     */
    @Deprecated
    public Materials disableAutoGeneratedVacuumFreezerRecipe() {
        mAutoGenerateVacuumFreezerRecipes = false;
        return this;
    }

    /** @deprecated Use {@link MaterialBuilder#setTurbine(float, float, float)} on a `MaterialBuilder` instead. */
    @Deprecated
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
        if (mMaterialList.isEmpty()) return Element.Tc.getProtons();
        long rAmount = 0, tAmount = 0;
        for (MaterialStack tMaterial : mMaterialList) {
            tAmount += tMaterial.mAmount;
            rAmount += tMaterial.mAmount * tMaterial.mMaterial.getProtons();
        }
        return (getDensity() * rAmount) / (tAmount * M);
    }

    public long getNeutrons() {
        if (mElement != null) return mElement.getNeutrons();
        if (mMaterialList.isEmpty()) return Element.Tc.getNeutrons();
        long rAmount = 0, tAmount = 0;
        for (MaterialStack tMaterial : mMaterialList) {
            tAmount += tMaterial.mAmount;
            rAmount += tMaterial.mAmount * tMaterial.mMaterial.getNeutrons();
        }
        return (getDensity() * rAmount) / (tAmount * M);
    }

    public long getMass() {
        if (mElement != null) return mElement.getMass();
        if (mMaterialList.isEmpty()) return Element.Tc.getMass();
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
                    .anyMatch(aStack -> GTUtility.areStacksEqual(aStack, tStack, !tStack.hasTagCompound())));
    }

    /**
     * This is used to determine if an ItemStack belongs to this Material.
     */
    public boolean remove(ItemStack aStack) {
        if (aStack == null) return false;
        boolean temp = false;
        int mMaterialItems_sS = mMaterialItems.size();
        for (int i = 0; i < mMaterialItems_sS; i++) if (GTUtility.areStacksEqual(aStack, mMaterialItems.get(i))) {
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
        return formatStringSafe(
            aFormat.replace("%s", "%temp")
                .replace("%material", "%s"),
            this.mDefaultLocalName).replace("%temp", "%s");
    }

    public String getLocalizedNameForItem(String aFormat) {
        return formatStringSafe(
            aFormat.replace("%s", "%temp")
                .replace("%material", "%s"),
            this.mLocalizedName).replace("%temp", "%s");
    }

    public boolean hasCorrespondingFluid() {
        return hasCorrespondingFluid;
    }

    /** @deprecated Use {@link MaterialBuilder#addFluid()} on a `MaterialBuilder` instead. */
    @Deprecated
    public Materials setHasCorrespondingFluid(boolean hasCorrespondingFluid) {
        this.hasCorrespondingFluid = hasCorrespondingFluid;
        return this;
    }

    public boolean hasCorrespondingGas() {
        return hasCorrespondingGas;
    }

    /** @deprecated Use {@link MaterialBuilder#addGas()} on a `MaterialBuilder` instead. */
    @Deprecated
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
            && !(GregTechAPI.mUseOnlyGoodSolderingMaterials && !contains(SubTag.SOLDERING_MATERIAL_GOOD));
    }

    public ItemStack getCells(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.cell, this, amount);
    }

    public ItemStack getDust(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.dust, this, amount);
    }

    public ItemStack getDustSmall(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.dustSmall, this, amount);
    }

    public ItemStack getDustTiny(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.dustTiny, this, amount);
    }

    public ItemStack getGems(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.gem, this, amount);
    }

    public ItemStack getIngots(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.ingot, this, amount);
    }

    public ItemStack getNuggets(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.nugget, this, amount);
    }

    public ItemStack getBlocks(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.block, this, amount);
    }

    public ItemStack getPlates(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.plate, this, amount);
    }

    public static Materials getGtMaterialFromFluid(Fluid fluid) {
        return FLUID_MAP.get(fluid);
    }

    public ItemStack getNanite(int amount) {
        return GTOreDictUnificator.get(OrePrefixes.nanite, this, amount);
    }
}
