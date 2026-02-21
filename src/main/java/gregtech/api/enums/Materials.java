package gregtech.api.enums;

import static gregtech.api.enums.FluidState.GAS;
import static gregtech.api.enums.GTValues.M;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.util.GTUtility.formatStringSafe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
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
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.common.config.Client;
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
public class Materials implements IColorModulationContainer, ISubTagContainer, IOreMaterial {

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
    public static Materials _NULL; // Not a real element
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
    public static Materials Helium3;
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
    public static Materials Endium;
    public static Materials Fluix;
    public static Materials Flux;
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
    public static Materials AncientDebris;
    public static Materials Andesite;
    public static Materials Ardite;
    public static Materials Aredrite;
    public static Materials Bitumen;
    public static Materials Black;
    public static Materials Blizz;
    public static Materials Bloodstone;
    public static Materials Bluestone;
    public static Materials Blutonium;
    public static Materials CertusQuartz;
    public static Materials CertusQuartzCharged;
    public static Materials Ceruclase;
    public static Materials Chimerite;
    public static Materials Chrysocolla;
    public static Materials Citrine;
    public static Materials CobaltHexahydrate;
    public static Materials ConstructionFoam;
    public static Materials Coral;
    public static Materials CrudeOil;
    public static Materials CrystalFlux;
    public static Materials Cyanite;
    public static Materials DarkIron;
    public static Materials DarkStone;
    public static Materials Demonite;
    public static Materials Desichalkos;
    public static Materials Dilithium;
    public static Materials Draconic;
    public static Materials Drulloy;
    public static Materials Duranium;
    public static Materials ElectrumFlux;
    public static Materials Emery;
    public static Materials EnderiumBase;
    public static Materials Energized;
    public static Materials FierySteel;
    public static Materials Firestone;
    public static Materials Fluorite;
    public static Materials Force;
    public static Materials Forcicium;
    public static Materials Forcillium;
    public static Materials Glowstone;
    public static Materials Graphene;
    public static Materials Graphite;
    public static Materials Greenstone;
    public static Materials Hematite;
    public static Materials HSLA;
    public static Materials Infernal;
    public static Materials InfusedAir;
    public static Materials InfusedDull;
    public static Materials InfusedEarth;
    public static Materials InfusedEntropy;
    public static Materials InfusedFire;
    public static Materials InfusedGold;
    public static Materials InfusedOrder;
    public static Materials InfusedVis;
    public static Materials InfusedWater;
    public static Materials Invisium;
    public static Materials Jade;
    public static Materials Lava;
    public static Materials Limestone;
    public static Materials Magma;
    public static Materials Mawsitsit;
    public static Materials Mercassium;
    public static Materials MeteoricSteel;
    public static Materials Meteorite;
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
    public static Materials Painite;
    public static Materials Peanutwood;
    public static Materials Petroleum;
    public static Materials Pewter;
    public static Materials Phoenixite;
    public static Materials Quartzite;
    public static Materials Randomite;
    public static Materials Rubracium;
    public static Materials Sand;
    public static Materials Siltstone;
    public static Materials Spinel;
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

    // Tier materials
    public static Materials ULV;
    public static Materials LV;
    public static Materials MV;
    public static Materials HV;
    public static Materials EV;
    public static Materials IV;
    public static Materials LuV;
    public static Materials ZPM;
    public static Materials UV;
    public static Materials UHV;
    public static Materials UEV;
    public static Materials UIV;
    public static Materials UMV;
    public static Materials UXV;
    public static Materials MAX;

    // Circuitry
    public static Materials Resistor;
    public static Materials Diode;
    public static Materials Transistor;
    public static Materials Capacitor;
    public static Materials Inductor;
    public static Materials Nano;
    public static Materials Piko;

    // Not possible to determine exact Components
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
    public static Materials Diesel;
    public static Materials Ethanol;
    public static Materials FermentedBiomass;
    public static Materials FishOil;
    public static Materials FryingOilHot;
    public static Materials Glue;
    public static Materials GlueAdvanced;
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
    public static Materials RareEarth;
    public static Materials Red;
    public static Materials Reinforced;
    public static Materials SeedOil;
    public static Materials SeedOilHemp;
    public static Materials SeedOilLin;
    public static Materials Stone;
    public static Materials TNT;
    public static Materials Unstable;
    public static Materials UnstableIngot;
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
    public static Materials AshDark;
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
    public static Materials Polycaprolactam;
    public static Materials Polydimethylsiloxane;
    public static Materials Polyethylene;
    public static Materials Polytetrafluoroethylene;
    public static Materials Potash;
    public static Materials Powellite;
    public static Materials Pumice;
    public static Materials Pyrite;
    public static Materials Pyrochlore;
    public static Materials Pyrolusite;
    public static Materials Pyrope;
    public static Materials Quicklime;
    public static Materials RockSalt;
    public static Materials Rubber;
    public static Materials RubberRaw;
    public static Materials RubberSilicone;
    public static Materials Ruby;
    public static Materials Rutile;
    public static Materials Salt;
    public static Materials Saltpeter;
    public static Materials Sapphire;
    public static Materials Scheelite;
    public static Materials SiliconDioxide;
    public static Materials Snow;
    public static Materials SodaAsh;
    public static Materials Sodalite;
    public static Materials SodiumPersulfate;
    public static Materials SodiumSulfide;
    public static Materials Titaniumtetrachloride;
    public static Materials Water, Steam; // Steam.getGas(..) reads better than Water.getGas(..)
    public static Materials Zincite;

    // Unclassified 01 materials
    public static Materials DenseSteam;
    public static Materials DenseSuperheatedSteam;
    public static Materials DenseSupercriticalSteam;
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
    public static Materials NaturalGas;
    public static Materials SulfuricGas;
    public static Materials SulfuricHeavyFuel;
    public static Materials SulfuricLightFuel;
    public static Materials SulfuricNaphtha;

    // Unclassified 03 materials
    public static Materials BioMediumRaw;
    public static Materials BioMediumSterilized;
    public static Materials ReinforcedGlass;

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
    public static Materials LiquidAir;
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
    public static Materials Galgadorian;
    public static Materials GalgadorianEnhanced;
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
    public static Materials ClayCompound;
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
    public static Materials SuperconductorMVBase;
    public static Materials SuperconductorHVBase;
    public static Materials SuperconductorEVBase;
    public static Materials SuperconductorIVBase;
    public static Materials SuperconductorLuVBase;
    public static Materials SuperconductorZPMBase;
    public static Materials SuperconductorUVBase;
    public static Materials SuperconductorUHVBase;
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

    // Waterline Chemicals
    public static Materials ActivatedCarbon;
    public static Materials PreActivatedCarbon;
    public static Materials DirtyActivatedCarbon;
    public static Materials PolyAluminiumChloride;
    public static Materials Ozone;
    public static Materials StableBaryonicMatter;

    // Radox Line
    public static Materials RadoxCracked;
    public static Materials RadoxGas;
    public static Materials RadoxHeavy;
    public static Materials RadoxLight;
    public static Materials RadoxPolymer;
    public static Materials RadoxRaw;
    public static Materials RadoxSuperHeavy;
    public static Materials RadoxSuperLight;
    public static Materials Xenoxene;
    public static Materials XenoxeneDiluted;

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
    public static Materials SoulInfusedMedium;

    // Botania Materials
    public static Materials Manasteel;
    public static Materials Terrasteel;
    public static Materials ElvenElementium;
    public static Materials Livingrock;
    public static Materials GaiaSpirit;
    public static Materials Livingwood;
    public static Materials Dreamwood;
    public static Materials ManaDiamond;
    public static Materials Dragonstone;

    // Kevlar Line
    public static Materials DiphenylmethaneDiisocyanate;
    public static Materials DiaminodiphenylmethanMixture;
    public static Materials DiphenylmethaneDiisocyanateMixture;
    public static Materials Butyraldehyde;
    public static Materials Isobutyraldehyde;
    public static Materials NickelTetracarbonyl;
    public static Materials KevlarCatalyst;
    public static Materials EthyleneOxide;
    public static Materials SiliconOil;
    public static Materials Ethyleneglycol;
    public static Materials Acetaldehyde;
    public static Materials Pentaerythritol;
    public static Materials PolyurethaneResin;
    public static Materials NMethylIIPyrrolidone;
    public static Materials TerephthaloylChloride;
    public static Materials Acetylene;
    public static Materials IVNitroaniline;
    public static Materials ParaPhenylenediamine;
    public static Materials Methylamine;
    public static Materials Trimethylamine;
    public static Materials GammaButyrolactone;
    public static Materials CalciumCarbide;
    public static Materials LiquidCrystalKevlar;
    public static Materials IIButinIIVdiol;
    public static Materials NickelAluminide;
    public static Materials RaneyNickelActivated;
    public static Materials BismuthIIIOxide;
    public static Materials ThionylChloride;
    public static Materials SulfurDichloride;
    public static Materials DimethylTerephthalate;
    public static Materials Kevlar;
    public static Materials TerephthalicAcid;
    public static Materials IIIDimethylbenzene;
    public static Materials IVDimethylbenzene;
    public static Materials CobaltIINaphthenate;
    public static Materials NaphthenicAcid;
    public static Materials CobaltIIHydroxide;
    public static Materials CobaltIIAcetate;
    public static Materials CobaltIINitrate;
    public static Materials OrganorhodiumCatalyst;
    public static Materials SodiumBorohydride;
    public static Materials RhodiumChloride;
    public static Materials Triphenylphosphene;
    public static Materials PhosphorusTrichloride;
    public static Materials SodiumHydride;
    public static Materials TrimethylBorate;
    public static Materials SodiumMethoxide;

    // Aluminium Ores
    public static Materials BauxiteSlurry;
    public static Materials HeatedBauxiteSlurry;
    public static Materials SluiceJuice;
    public static Materials SluiceSand;
    public static Materials BauxiteSlag;
    public static Materials IlmeniteSlag;
    public static Materials GreenSapphireJuice;
    public static Materials SapphireJuice;
    public static Materials RubyJuice;

    // UEV+ Materials
    public static Materials DTCC;
    public static Materials DTPC;
    public static Materials DTRC;
    public static Materials DTEC;
    public static Materials DTSC;
    public static Materials ExcitedDTCC;
    public static Materials ExcitedDTPC;
    public static Materials ExcitedDTRC;
    public static Materials ExcitedDTEC;
    public static Materials ExcitedDTSC;
    public static Materials DTR;
    public static Materials SpaceTime;
    public static Materials TranscendentMetal;
    public static Materials MHDCSM;
    public static Materials RawStarMatter;
    public static Materials WhiteDwarfMatter;
    public static Materials BlackDwarfMatter;
    public static Materials Time;
    public static Materials Space;
    public static Materials Universium;
    public static Materials Eternity;
    public static Materials PrimordialMatter;
    public static Materials MagMatter;
    public static Materials QuarkGluonPlasma;
    public static Materials PhononMedium;
    public static Materials PhononCrystalSolution;
    public static Materials SixPhasedCopper;
    public static Materials Mellion;
    public static Materials Creon;
    public static Materials GravitonShard;
    public static Materials DimensionallyShiftedSuperfluid;
    public static Materials MoltenProtoHalkoniteBase;
    public static Materials HotProtoHalkonite;
    public static Materials ProtoHalkonite;
    public static Materials MoltenExoHalkoniteBase;
    public static Materials HotExoHalkonite;
    public static Materials ExoHalkonite;
    public static Materials Protomatter;
    public static Materials StargateCrystalSlurry;
    public static Materials LumipodExtract;
    public static Materials BiocatalyzedPropulsionFluid;

    // GTNH Materials
    public static Materials Signalum;
    public static Materials Lumium;
    public static Materials Prismarine;
    public static Materials AquaRegia;
    public static Materials SolutionBlueVitriol;
    public static Materials SolutionNickelSulfate;
    public static Materials Lodestone;
    public static Materials Luminite;
    public static Materials Chlorite;
    public static Materials Staurolite;
    public static Materials Cordierite;
    public static Materials Datolite;
    public static Materials MetamorphicMineralMixture;
    public static Materials Plagioclase;
    public static Materials Epidote;

    public static final List<IMaterialHandler> mMaterialHandlers = new ArrayList<>();
    public static final Map<Fluid, Materials> FLUID_MAP = new LinkedHashMap<>();
    /** @deprecated This is for keeping compatibility with addons mods (Such as TinkersGregworks etc.) */
    @Deprecated
    public static Collection<Materials> VALUES = new LinkedHashSet<>();

    private static final Map<String, Materials> MATERIALS_MAP = new LinkedHashMap<>();
    private static Materials[] MATERIALS_ARRAY = new Materials[] {};

    static {
        MaterialsInit.load();

        setOreByproducts();
        setSmeltingInto();
        setMaceratingInto();
        setArcSmeltingInto();
        setDirectSmelting();
        setReRegistration();
        setMultipliers();
    }

    public final short[] mRGBa = new short[] { 255, 255, 255, 0 };
    public final short[] mMoltenRGBa = new short[] { 255, 255, 255, 0 };
    public TextureSet mIconSet;
    public GeneratedMaterialRenderer renderer;
    public List<MaterialStack> mMaterialList = new ArrayList<>();
    public List<Materials> mOreReRegistrations = new ArrayList<>();
    public List<TCAspects.TC_AspectStack> mAspects = new ArrayList<>();
    public ArrayList<ItemStack> mMaterialItems = new ArrayList<>();
    public LinkedHashSet<SubTag> mSubTags = new LinkedHashSet<>();
    public List<Supplier<Materials>> mPendingOreByproducts = new ArrayList<>();
    public List<Materials> mOreByProducts = new ArrayList<>();
    public int mDurability = 0;
    public byte mToolQuality = 0;
    public float mToolSpeed = 1.0F;
    private Supplier<Enchantment> mPendingToolEnchantment;
    public Enchantment mToolEnchantment;
    public byte mToolEnchantmentLevel = 0;
    private Supplier<Enchantment> mPendingArmorEnchantment;
    public Enchantment mArmorEnchantment;
    public byte mArmorEnchantmentLevel = 0;
    public boolean mUnifiable;
    public boolean mBlastFurnaceRequired = false;
    public boolean mAutoGenerateBlastFurnaceRecipes = true;
    public boolean mAutoGenerateVacuumFreezerRecipes = true;
    public boolean mAutoGenerateRecycleRecipes = true;
    public boolean mHasParentMod = true;
    private boolean mGenerateDustItems = false;
    private boolean mGenerateMetalItems = false;
    private boolean mGenerateGemItems = false;
    private boolean mGenerateOreItems = false;
    private boolean mGenerateCell = false;
    private boolean mGeneratePlasma = false;
    private boolean mGenerateToolHeadItems = false;
    private boolean mGenerateGearItems = false;
    private boolean mGenerateEmpty = false;
    public boolean mHasGas = false;
    public boolean mCustomOre = false;
    public short mBlastFurnaceTemp = 0;
    public int mMeltingPoint = 0;
    public int mGasTemp = 0;
    public int mMetaItemSubID = -1;
    public int mFuelPower = 0;
    public int mFuelType = 0;
    public int mExtraData = 0;
    public int mOreMultiplier = 1;
    public int mByProductMultiplier = 1;
    public int mSmeltingMultiplier = 1;
    public int mDensityMultiplier = 1;
    public int mDensityDivider = 1;
    public int processingMaterialTierEU = 0;
    public long mDensity = M;
    public float mHeatDamage = 0.0F;
    public float mSteamMultiplier = 1.0F;
    public float mGasMultiplier = 1.0F;
    public float mPlasmaMultiplier = 1.0F;
    private String mChemicalFormula = "?";
    private boolean isFormulaNeededLocalized = false;
    public String mName;
    public String mDefaultLocalName;
    public String mCustomID = "null";
    public String mConfigSection = "null";
    public Dyes mColor = Dyes._NULL;
    public Element mElement = null;
    public Materials mOreReplacement = this;
    private Supplier<Materials> mPendingSmeltingInto;
    public Materials mSmeltInto = this;
    private Supplier<Materials> mPendingMaceratingInto;
    public Materials mMacerateInto = this;
    private Supplier<Materials> mPendingArcSmeltingInto;
    public Materials mArcSmeltInto = this;
    private Supplier<Materials> mPendingDirectSmelting;
    public Materials mDirectSmelting = this;
    public Materials mHandleMaterial = this;
    public Materials mMaterialInto;
    public Fluid mSolid = null;
    public Fluid mFluid = null;
    public Fluid mGas = null;
    public Fluid mPlasma = null;
    /**
     * This Fluid is used as standard Unit for Molten Materials. 1296 is a Molten Block, that means 144 is one Material
     * Unit worth of fluid.
     */
    public Fluid mStandardMoltenFluid = null;
    private boolean hasCorrespondingFluid = false;
    private boolean hasCorrespondingGas = false;
    private boolean mCanBeCracked = false;
    private Fluid[] hydroCrackedFluids = new Fluid[3];
    private Fluid[] steamCrackedFluids = new Fluid[3];

    protected Materials(
        // spotless:off
        String name,
        String defaultLocalName,
        @Nullable Element element,
        @Nullable String chemicalFormula,
        boolean unifiable,
        TextureSet iconSet,
        Dyes color,
        int argb, int argbMolten,
        int toolDurability, int toolQuality, float toolSpeed,
        @Nullable Supplier<Enchantment> pendingToolEnchantment, int toolEnchantmentLevel,
        @Nullable Supplier<Enchantment> pendingArmorEnchantment, int armorEnchantmentLevel,
        float steamMultiplier, float gasMultiplier, float plasmaMultiplier,
        int fuelType, int fuelPower,
        boolean generateDustItems,
        boolean generateMetalItems,
        boolean generateGemItems,
        boolean generateOreItems,
        boolean generateCell,
        boolean generatePlasma,
        boolean generateToolHeadItems,
        boolean generateGearItems,
        boolean generateEmpty,
        boolean generateFluid,
        boolean generateGas,
        int extraData,
        boolean canBeCracked,
        float heatDamage,
        int meltingPoint,
        int blastFurnaceTemp,
        boolean blastFurnaceRequired,
        boolean autoGenerateBlastFurnaceRecipes,
        boolean autoGenerateVacuumFreezerRecipes,
        boolean autoGeneratedRecycleRecipes,
        int densityMultiplier, int densityDivider,
        int oreMultiplier,
        List<MaterialStack> materialList,
        List<TCAspects.TC_AspectStack> aspects,
        List<Supplier<Materials>> pendingOreByproducts,
        Supplier<Materials> pendingSmeltingInto,
        Supplier<Materials> pendingMaceratingInto,
        Supplier<Materials> pendingArcSmeltingInto,
        Supplier<Materials> pendingDirectSmelting,
        LinkedHashSet<SubTag> subTags
        // spotless:on
    ) {

        // Set names
        mName = name;
        mDefaultLocalName = defaultLocalName;
        MATERIALS_MAP.put(mName, this);

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
        mRGBa[0] = (short) ((argb >>> 16) & 0xFF);
        mRGBa[1] = (short) ((argb >>> 8) & 0xFF);
        mRGBa[2] = (short) (argb & 0xFF);
        mRGBa[3] = (short) ((argb >>> 24) & 0xFF);
        mMoltenRGBa[0] = (short) ((argbMolten >>> 16) & 0xFF);
        mMoltenRGBa[1] = (short) ((argbMolten >>> 8) & 0xFF);
        mMoltenRGBa[2] = (short) (argbMolten & 0xFF);
        mMoltenRGBa[3] = (short) ((argbMolten >>> 24) & 0xFF);

        // Set tool properties
        mDurability = toolDurability;
        mToolSpeed = toolSpeed;
        mToolQuality = (byte) toolQuality;
        mPendingToolEnchantment = pendingToolEnchantment;
        mToolEnchantmentLevel = (byte) toolEnchantmentLevel;
        mPendingArmorEnchantment = pendingArmorEnchantment;
        mArmorEnchantmentLevel = (byte) armorEnchantmentLevel;

        // Set turbine properties
        mSteamMultiplier = steamMultiplier;
        mGasMultiplier = gasMultiplier;
        mPlasmaMultiplier = plasmaMultiplier;

        // Set fuel properties
        mFuelPower = fuelPower;
        mFuelType = fuelType;

        // Set auto-generated parts and recipes
        mExtraData = extraData;
        mCanBeCracked = canBeCracked;
        mGenerateDustItems = generateDustItems;
        mGenerateMetalItems = generateMetalItems;
        mGenerateGemItems = generateGemItems;
        mGenerateOreItems = generateOreItems;
        mGenerateCell = generateCell;
        mGeneratePlasma = generatePlasma;
        mGenerateToolHeadItems = generateToolHeadItems;
        mGenerateGearItems = generateGearItems;
        mGenerateEmpty = generateEmpty;
        hasCorrespondingFluid = generateFluid;
        hasCorrespondingGas = generateGas;
        mBlastFurnaceRequired = blastFurnaceRequired;
        mAutoGenerateBlastFurnaceRecipes = autoGenerateBlastFurnaceRecipes;
        mAutoGenerateVacuumFreezerRecipes = autoGenerateVacuumFreezerRecipes;
        mAutoGenerateRecycleRecipes = autoGeneratedRecycleRecipes;

        // Set what materials this material is composed of
        mMaterialList = materialList;
        mPendingOreByproducts = pendingOreByproducts;
        mPendingSmeltingInto = pendingSmeltingInto;
        mPendingMaceratingInto = pendingMaceratingInto;
        mPendingArcSmeltingInto = pendingArcSmeltingInto;
        mPendingDirectSmelting = pendingDirectSmelting;

        // Set material density
        mDensityMultiplier = densityMultiplier;
        mDensityDivider = densityDivider;
        mDensity = (M * densityMultiplier) / densityDivider;

        // Set material temperatures
        mBlastFurnaceTemp = (short) blastFurnaceTemp;
        mMeltingPoint = meltingPoint;

        // Constant fields
        mCustomOre = false;
        mCustomID = "null";
        mConfigSection = "ore";
        mMaterialInto = this;

        // Set material SubTags
        mSubTags = subTags;
        if (mColor != null) mSubTags.add(SubTag.HAS_COLOR);
        if (generateMetalItems) mSubTags.add(SubTag.SMELTING_TO_FLUID);

        // Uncategorized fields
        mHeatDamage = heatDamage;
        mOreMultiplier = oreMultiplier;
        mUnifiable = unifiable;

        // No clue what is going on here...
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

    private static void setOreByproducts() {
        for (Materials material : MATERIALS_MAP.values()) {
            if (material.mPendingOreByproducts == null) continue;

            material.mOreByProducts = material.mPendingOreByproducts.stream()
                .map(Supplier::get)
                .map(byproduct -> byproduct.mMaterialInto)
                .distinct()
                .collect(Collectors.toCollection(ArrayList::new));

            material.mPendingOreByproducts = null;
        }
    }

    private static void setSmeltingInto() {
        for (Materials material : MATERIALS_MAP.values()) {
            if (material.mPendingSmeltingInto == null) continue;
            material.mSmeltInto = material.mPendingSmeltingInto.get().mMaterialInto.mSmeltInto;
            material.mPendingSmeltingInto = null;
        }
    }

    private static void setMaceratingInto() {
        for (Materials material : MATERIALS_MAP.values()) {
            if (material.mPendingMaceratingInto == null) continue;
            material.mMacerateInto = material.mPendingMaceratingInto.get().mMaterialInto.mMacerateInto;
            material.mPendingMaceratingInto = null;
        }
    }

    private static void setArcSmeltingInto() {
        for (Materials material : MATERIALS_MAP.values()) {
            if (material.mPendingArcSmeltingInto == null) continue;
            material.mArcSmeltInto = material.mPendingArcSmeltingInto.get().mMaterialInto.mArcSmeltInto;
            material.mPendingArcSmeltingInto = null;
        }
    }

    private static void setDirectSmelting() {
        for (Materials material : MATERIALS_MAP.values()) {
            if (material.mPendingDirectSmelting == null) continue;
            material.mDirectSmelting = material.mPendingDirectSmelting.get().mMaterialInto.mDirectSmelting;
            material.mPendingDirectSmelting = null;
        }
    }

    private static void setMultipliers() {
        Amber.setSmeltingMultiplier(2);
        InfusedAir.setSmeltingMultiplier(2);
        InfusedFire.setSmeltingMultiplier(2);
        InfusedEarth.setSmeltingMultiplier(2);
        InfusedWater.setSmeltingMultiplier(2);
        InfusedEntropy.setSmeltingMultiplier(2);
        InfusedOrder.setSmeltingMultiplier(2);
        InfusedVis.setSmeltingMultiplier(2);
        InfusedDull.setSmeltingMultiplier(2);
        Salt.setSmeltingMultiplier(2);
        RockSalt.setSmeltingMultiplier(2);
        Scheelite.setSmeltingMultiplier(2);
        Tungstate.setSmeltingMultiplier(2);
        Cassiterite.setSmeltingMultiplier(2);
        CassiteriteSand.setSmeltingMultiplier(2);
        NetherQuartz.setSmeltingMultiplier(2);
        CertusQuartz.setSmeltingMultiplier(2);
        CertusQuartzCharged.setSmeltingMultiplier(2);
        TricalciumPhosphate.setSmeltingMultiplier(3);
        Saltpeter.setSmeltingMultiplier(4);
        Apatite.setSmeltingMultiplier(4)
            .setByProductMultiplier(2);
        Electrotine.setSmeltingMultiplier(5);
        Teslatite.setSmeltingMultiplier(5);
        Redstone.setSmeltingMultiplier(5);
        Glowstone.setSmeltingMultiplier(5);
        Lapis.setSmeltingMultiplier(6)
            .setByProductMultiplier(4);
        Sodalite.setSmeltingMultiplier(6)
            .setByProductMultiplier(4);
        Lazurite.setSmeltingMultiplier(6)
            .setByProductMultiplier(4);
        Monazite.setSmeltingMultiplier(8)
            .setByProductMultiplier(2);
        Cryolite.setByProductMultiplier(4);
        Coal.setByProductMultiplier(2);
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
        RubberSilicone.mOreReRegistrations.add(AnyRubber);
        StyreneButadieneRubber.mOreReRegistrations.add(AnySyntheticRubber);
        RubberSilicone.mOreReRegistrations.add(AnySyntheticRubber);
    }

    private static void setToolEnchantments() {
        for (Materials material : MATERIALS_MAP.values()) {
            if (material.mPendingToolEnchantment == null) continue;
            material.mToolEnchantment = material.mPendingToolEnchantment.get();
            material.mPendingToolEnchantment = null;
        }
    }

    private static void setArmorEnchantments() {
        for (Materials material : MATERIALS_MAP.values()) {
            if (material.mPendingArmorEnchantment == null) continue;
            material.mArmorEnchantment = material.mPendingArmorEnchantment.get();
            material.mPendingArmorEnchantment = null;
        }
    }

    public static void init() {
        new MaterialsIDMap().register();

        setToolEnchantments();
        setArmorEnchantments();

        new ProcessingConfig();
        if (!GTMod.proxy.mEnableAllMaterials) new ProcessingModSupport();

        // This is where addon mods can add/manipulate materials
        mMaterialHandlers.forEach(IMaterialHandler::onMaterialsInit);

        initMaterialProperties();
        // No more material addition or manipulation should be done past this point!

        // Generate standard object array. This is a lot faster to loop over.
        MATERIALS_ARRAY = MATERIALS_MAP.values()
            .toArray(new Materials[0]);
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
        OrePrefixes.ingotHot.disableComponent(Materials.ClayCompound);
        OrePrefixes.ingotHot.disableComponent(Materials.Netherite);
        OrePrefixes.ingotHot.disableComponent(Materials.HotProtoHalkonite);
        OrePrefixes.ingotHot.disableComponent(Materials.ProtoHalkonite);
        OrePrefixes.ingotHot.disableComponent(Materials.HotExoHalkonite);
        OrePrefixes.ingotHot.disableComponent(Materials.ExoHalkonite);
    }

    /**
     * Init rendering properties. Will be called at pre init by GT client proxy.
     */
    public static void initClient() {
        Materials.TranscendentMetal.renderer = new TranscendentMetalRenderer();
        Materials.GaiaSpirit.renderer = new GaiaSpiritRenderer();
        Materials.Infinity.renderer = new InfinityRenderer();
        Materials.CosmicNeutronium.renderer = new CosmicNeutroniumRenderer();
        Materials.Universium.renderer = new UniversiumRenderer();
        Materials.Eternity.renderer = new InfinityRenderer();
        Materials.MagMatter.renderer = new InfinityRenderer();
        Materials.SixPhasedCopper.renderer = new GlitchEffectRenderer();
        Materials.GravitonShard.renderer = new InfinityRenderer();
        Materials.ExoHalkonite.renderer = new InfinityRenderer();
        Materials.HotExoHalkonite.renderer = new InfinityRenderer();
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

        if (aMaterial == Materials.SpaceTime) {
            aMaterial.mHandleMaterial = Materials.Infinity;
        }

        if (aMaterial == Materials.TranscendentMetal) {
            aMaterial.mHandleMaterial = Materials.DraconiumAwakened;
        }

        if (aMaterial == Materials.Eternity) {
            aMaterial.mHandleMaterial = Materials.SpaceTime;
        }

        if (aMaterial == Materials.MagMatter) {
            aMaterial.mHandleMaterial = Materials.MHDCSM;
        }
    }

    private static void addEnchantmentValues(Materials aMaterial) {
        String aEnchantmentName = aMaterial.mToolEnchantment != null ? aMaterial.mToolEnchantment.getName() : "";
        if (aMaterial.mToolEnchantment != null && !aEnchantmentName.equals(aMaterial.mToolEnchantment.getName()))
            IntStream.range(0, Enchantment.enchantmentsList.length)
                .filter(i -> aEnchantmentName.equals(Enchantment.enchantmentsList[i].getName()))
                .forEach(i -> aMaterial.mToolEnchantment = Enchantment.enchantmentsList[i]);
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

    public int getProcessingMaterialTierEU() {
        return processingMaterialTierEU;
    }

    public Materials setProcessingMaterialTierEU(final long processingMaterialTierEU) {
        this.processingMaterialTierEU = (int) processingMaterialTierEU;
        return this;
    }

    /**
     * This is for keeping compatibility with addons mods (Such as TinkersGregworks etc.) that looped over the old
     * materials enum
     *
     * @deprecated Use {@link Materials#getName()} instead, but realistically you should never even need the name.
     */
    @Deprecated
    public String name() {
        return mName;
    }

    /**
     * Gets the 'mName' field of the Material.
     *
     * @return mName
     */
    public String getName() {
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

    /**
     * Set material's chemical formula.
     * 
     * @param aChemicalFormula     the Chemical Formula want to set
     * @param isNeededLocalization if it's true, will generate a localized key.
     */
    public void setChemicalFormula(String aChemicalFormula, boolean isNeededLocalization) {
        this.mChemicalFormula = aChemicalFormula;
        if (isNeededLocalization) {
            this.isFormulaNeededLocalized = true;
            GTLanguageManager.addStringLocalization(getLocalizedNameKey() + ".ChemicalFormula", aChemicalFormula);
        }
    }

    public void setChemicalFormula(String aChemicalFormula) {
        setChemicalFormula(aChemicalFormula, false);
    }

    public String getChemicalFormula() {
        return isFormulaNeededLocalized ? StatCollector.translateToLocal(getLocalizedNameKey() + ".ChemicalFormula")
            : mChemicalFormula;
    }

    public String getChemicalTooltip() {
        return getChemicalTooltip(1, false);
    }

    public String getChemicalTooltip(boolean aShowQuestionMarks) {
        return getChemicalTooltip(1, aShowQuestionMarks);
    }

    public String getChemicalTooltip(long aMultiplier) {
        return getChemicalTooltip(aMultiplier, false);
    }

    public String getChemicalTooltip(long aMultiplier, boolean aShowQuestionMarks) {
        final String aChemicalFormula = getChemicalFormula();
        if (!aShowQuestionMarks && aChemicalFormula.equals("?")) return "";
        if (aMultiplier >= M * 2 && !mMaterialList.isEmpty()) {
            return ((mElement != null || (mMaterialList.size() < 2 && mMaterialList.get(0).mAmount == 1))
                ? aChemicalFormula
                : "(" + aChemicalFormula + ")") + aMultiplier;
        }
        return aChemicalFormula;
    }

    @Nullable
    public String getFlavorText() {
        final String key = getLocalizedNameKey() + ".flavorText";
        return StatCollector.canTranslate(key) ? StatCollector.translateToLocal(key) : null;
    }

    public void addTooltips(List<String> list, long aMultiplier) {
        if (Client.tooltip.showFormula) {
            final String chemicalTooltip = getChemicalTooltip(aMultiplier);
            if (GTUtility.isStringValid(chemicalTooltip)) list.add(chemicalTooltip);
        }
        if (Client.tooltip.showFlavorText) {
            final String flavorTooltip = getFlavorText() != null ? "§8§o" + getFlavorText() : null;
            if (flavorTooltip != null) list.add(flavorTooltip);
        }
    }

    @Override
    public void addTooltips(List<String> list) {
        addTooltips(list, 1);
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
    @Deprecated
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
     * This Material should be the Main Material this Ore gets ground into. Example, Chromite giving Chrome or Tungstate
     * giving Tungsten.
     */
    @SuppressWarnings("UnusedReturnValue") // Maintains signature
    public Materials setOreReplacement(Materials aMaterial) {
        if (aMaterial != null) mOreReplacement = aMaterial.mMaterialInto.mOreReplacement;
        return this;
    }

    /** @deprecated Use {@link MaterialBuilder#setToolEnchantment(Supplier, int)} instead. */
    @Deprecated
    public Materials setEnchantmentForTools(Enchantment aEnchantment, int aEnchantmentLevel) {
        mToolEnchantment = aEnchantment;
        mToolEnchantmentLevel = (byte) aEnchantmentLevel;
        return this;
    }

    /** @deprecated Use {@link MaterialBuilder#setArmorEnchantment(Supplier, int)} instead. */
    @Deprecated
    public Materials setEnchantmentForArmors(Enchantment aEnchantment, int aEnchantmentLevel) {
        mArmorEnchantment = aEnchantment;
        mArmorEnchantmentLevel = (byte) aEnchantmentLevel;
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
    public TextureSet getTextureSet() {
        return mIconSet;
    }

    @Override
    public String toString() {
        return this.mName;
    }

    @Override
    public int getId() {
        return mMetaItemSubID;
    }

    public boolean isValidForStone(IStoneType stoneType) {
        if (contains(SubTag.ICE_ORE)) {
            return stoneType.getCategory() == StoneCategory.Ice;
        } else {
            return stoneType.getCategory() == StoneCategory.Stone;
        }
    }

    @Override
    public @Nullable Materials getGTMaterial() {
        return this;
    }

    @Override
    public List<IStoneType> getValidStones() {
        if (contains(SubTag.ICE_ORE)) {
            return StoneType.ICES;
        } else {
            return StoneType.STONES;
        }
    }

    @Override
    public String getInternalName() {
        return mName;
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
            this.getLocalizedName()).replace("%temp", "%s");
    }

    public static boolean isMaterialItem(int meta) {
        return meta >= 0 && meta < 32000;
    }

    public static boolean isMaterialItem(@NotNull ItemStack stack) {
        return isMaterialItem(stack.getItemDamage());
    }

    @Override
    public boolean generatesPrefix(OrePrefixes prefix) {
        return prefix.doGenerateItem(this);
    }

    public boolean hasDustItems() {
        return mGenerateDustItems;
    }

    public boolean hasMetalItems() {
        return mGenerateMetalItems;
    }

    public boolean hasGemItems() {
        return mGenerateGemItems;
    }

    public boolean hasOresItems() {
        return mGenerateOreItems;
    }

    public boolean hasCell() {
        return mGenerateCell;
    }

    public boolean hasPlasma() {
        return mGeneratePlasma;
    }

    public boolean hasToolHeadItems() {
        return mGenerateToolHeadItems;
    }

    public boolean hasGearItems() {
        return mGenerateGearItems;
    }

    public boolean hasEmpty() {
        return mGenerateEmpty;
    }

    public boolean hasCorrespondingFluid() {
        return hasCorrespondingFluid;
    }

    /** @deprecated Use {@link MaterialBuilder#addFluid()} on {@link MaterialBuilder} instead. */
    @Deprecated
    public Materials setHasCorrespondingFluid(boolean hasCorrespondingFluid) {
        this.hasCorrespondingFluid = hasCorrespondingFluid;
        return this;
    }

    public boolean hasCorrespondingGas() {
        return hasCorrespondingGas;
    }

    /** @deprecated Use {@link MaterialBuilder#addGas()} on {@link MaterialBuilder} instead. */
    @Deprecated
    public Materials setHasCorrespondingGas(boolean hasCorrespondingGas) {
        this.hasCorrespondingGas = hasCorrespondingGas;
        return this;
    }

    public boolean canBeCracked() {
        return mCanBeCracked;
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
