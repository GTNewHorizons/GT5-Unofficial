package gregtech.api.enums;

import java.util.Locale;

import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.common.Loader;

public enum Mods {

    AE2FluidCraft(Names.A_E2_FLUID_CRAFT),
    AE2Stuff(Names.AE2STUFF),
    AE2WCT(Names.AE2WCT),
    AFSU(Names.A_F_S_U),
    AdvancedSolarPanel(Names.ADVANCED_SOLAR_PANEL),
    AdventureBackpack(Names.ADVENTURE_BACKPACK),
    AppleCore(Names.APPLE_CORE),
    AppliedEnergistics2(Names.APPLIED_ENERGISTICS2),
    ArchitectureCraft(Names.ARCHITECTURE_CRAFT),
    Aroma1997Core(Names.AROMA1997_CORE),
    Automagy(Names.AUTOMAGY),
    Avaritia(Names.AVARITIA),
    AvaritiaAddons(Names.AVARITIA_ADDONS),
    Backpack(Names.BACKPACK),
    BartWorks(Names.BART_WORKS),
    Baubles(Names.BAUBLES),
    BetterBuildersWands(Names.BETTER_BUILDERS_WANDS),
    BetterLoadingScreen(Names.BETTER_LOADING_SCREEN),
    BetterQuesting(Names.BETTER_QUESTING),
    BiblioCraft(Names.BIBLIO_CRAFT),
    BiblioWoodsBoPEdition(Names.BIBLIO_WOODS_BO_P_EDITION),
    BiblioWoodsForestryEdition(Names.BIBLIO_WOODS_FORESTRY_EDITION),
    BiblioWoodsNaturaEdition(Names.BIBLIO_WOODS_NATURA_EDITION),
    BinnieCore(Names.BINNIE_CORE),
    BiomesOPlenty(Names.BIOMES_O_PLENTY),
    BloodArsenal(Names.BLOOD_ARSENAL),
    BloodMagic(Names.BLOOD_MAGIC),
    Botania(Names.BOTANIA),
    Botany(Names.BOTANY),
    BuildCraftBuilders(Names.BUILD_CRAFT_BUILDERS),
    BuildCraftCompat(Names.BUILD_CRAFT_COMPAT),
    BuildCraftCore(Names.BUILD_CRAFT_CORE),
    BuildCraftFactory(Names.BUILD_CRAFT_FACTORY),
    BuildCraftRobotics(Names.BUILD_CRAFT_ROBOTICS),
    BuildCraftSilicon(Names.BUILD_CRAFT_SILICON),
    BuildCraftTransport(Names.BUILD_CRAFT_TRANSPORT),
    COFHCore(Names.C_O_F_H_CORE),
    CarpentersBlocks(Names.CARPENTERS_BLOCKS),
    CatWalks(Names.CAT_WALKS),
    Chisel(Names.CHISEL),
    CompactKineticGenerators(Names.COMPACT_KINETIC_GENERATORS),
    Computronics(Names.COMPUTRONICS),
    CraftTweaker(Names.CRAFT_TWEAKER),
    CropLoadCore(Names.CROP_LOAD_CORE),
    CropsPlusPlus(Names.CROPS_PLUS_PLUS),
    DetravScannerMod(Names.DETRAV_SCANNER_MOD),
    DraconicEvolution(Names.DRACONIC_EVOLUTION),
    ElectroMagicTools(Names.ELECTRO_MAGIC_TOOLS),
    EnderIO(Names.ENDER_I_O),
    EnderStorage(Names.ENDER_STORAGE),
    EnderZoo(Names.ENDER_ZOO),
    EnhancedLootBags(Names.ENHANCED_LOOT_BAGS),
    EternalSingularity(Names.ETERNAL_SINGULARITY),
    ExtraBees(Names.EXTRA_BEES),
    ExtraCells2(Names.EXTRA_CELLS2),
    ExtraTrees(Names.EXTRA_TREES),
    ExtraUtilities(Names.EXTRA_UTILITIES),
    FloodLights(Names.FLOOD_LIGHTS),
    ForbiddenMagic(Names.FORBIDDEN_MAGIC),
    Forestry(Names.FORESTRY),
    ForgeMicroblocks(Names.FORGE_MICROBLOCKS),
    ForgeRelocation(Names.FORGE_RELOCATION),
    GTNHIntergalactic(Names.G_T_N_H_INTERGALACTIC),
    GTNHLanthanides(Names.G_T_N_H_LANTHANIDES),
    GTPlusPlus(Names.G_T_PLUS_PLUS),
    GTPlusPlusEverglades(Names.G_T_PLUS_PLUS_EVERGLADES),
    Gadomancy(Names.GADOMANCY),
    GalactiGreg(Names.GALACTI_GREG),
    GalacticraftAmunRa(Names.GALACTICRAFT_AMUN_RA),
    GalacticraftCore(Names.GALACTICRAFT_CORE),
    GalacticraftMars(Names.GALACTICRAFT_MARS),
    GalaxySpace(Names.GALAXY_SPACE),
    Gendustry(Names.GENDUSTRY),
    Genetics(Names.GENETICS),
    GoodGenerator(Names.GOOD_GENERATOR),
    GraviSuite(Names.GRAVI_SUITE),
    GraviSuiteNEO(Names.GRAVI_SUITE_NEO),
    GregTech(Names.GREG_TECH),
    HardcoreEnderExpansion(Names.HARDCORE_ENDER_EXPANSION),
    HodgePodge(Names.HODGE_PODGE),
    HoloInventory(Names.HOLO_INVENTORY),
    IC2CropPlugin(Names.I_C2_CROP_PLUGIN),
    IC2NuclearControl(Names.I_C2_NUCLEAR_CONTROL),
    IguanaTweaksTinkerConstruct(Names.IGUANA_TWEAKS_TINKER_CONSTRUCT),
    IndustrialCraft2(Names.INDUSTRIAL_CRAFT2),
    InfernalMobs(Names.INFERNAL_MOBS),
    IronChests(Names.IRON_CHESTS),
    IronChestsMinecarts(Names.IRON_CHESTS_MINECARTS),
    IronTanks(Names.IRON_TANKS),
    JABBA(Names.J_A_B_B_A),
    KekzTech(Names.KEKZ_TECH),
    KubaTech(Names.KUBA_TECH),
    LogisticsPipes(Names.LOGISTICS_PIPES),
    MCFrames(Names.MC_FRAMES),
    MagicBees(Names.MAGIC_BEES),
    MalisisDoors(Names.MALISIS_DOORS),
    Mantle(Names.MANTLE),
    MineAndBladeBattleGear2(Names.MINE_AND_BLADE_BATTLE_GEAR2),
    Minecraft(Names.MINECRAFT),
    MineTweaker(Names.MINE_TWEAKER),
    NEICustomDiagrams(Names.N_E_I_CUSTOM_DIAGRAMS),
    NEIOrePlugin(Names.N_E_I_ORE_PLUGIN),
    Natura(Names.NATURA),
    NaturesCompass(Names.NATURES_COMPASS),
    NewHorizonsCoreMod(Names.NEW_HORIZONS_CORE_MOD),
    NotEnoughItems(Names.NOT_ENOUGH_ITEMS),
    OpenBlocks(Names.OPEN_BLOCKS),
    OpenComputers(Names.OPEN_COMPUTERS),
    OpenGlasses(Names.OPEN_GLASSES),
    OpenModularTurrets(Names.OPEN_MODULAR_TURRETS),
    OpenPrinters(Names.OPEN_PRINTERS),
    OpenSecurity(Names.OPEN_SECURITY),
    PamsHarvestCraft(Names.PAMS_HARVEST_CRAFT),
    PamsHarvestTheNether(Names.PAMS_HARVEST_THE_NETHER),
    PlayerAPI(Names.PLAYER_API),
    ProjectBlue(Names.PROJECT_BLUE),
    ProjectRedCore(Names.PROJECT_RED_CORE),
    ProjectRedExpansion(Names.PROJECT_RED_EXPANSION),
    ProjectRedExploration(Names.PROJECT_RED_EXPLORATION),
    ProjectRedFabrication(Names.PROJECT_RED_FABRICATION),
    ProjectRedIllumination(Names.PROJECT_RED_ILLUMINATION),
    ProjectRedIntegration(Names.PROJECT_RED_INTEGRATION),
    ProjectRedTransmission(Names.PROJECT_RED_TRANSMISSION),
    ProjectRedTransportation(Names.PROJECT_RED_TRANSPORTATION),
    QuestBook(Names.QUEST_BOOK),
    RWG(Names.RWG),
    Railcraft(Names.RAILCRAFT),
    RandomThings(Names.RANDOM_THINGS),
    RemoteIO(Names.REMOTE_IO),
    SGCraft(Names.S_G_CRAFT),
    SleepingBags(Names.SLEEPING_BAGS),
    SpiceOfLife(Names.SPICE_OF_LIFE),
    StevesAddons(Names.STEVES_ADDONS),
    StevesCarts2(Names.STEVES_CARTS2),
    StevesFactoryManager(Names.STEVES_FACTORY_MANAGER),
    StorageDrawers(Names.STORAGE_DRAWERS),
    StructureLib(Names.STRUCTURE_LIB),
    SuperSolarPanels(Names.SUPER_SOLAR_PANELS),
    TaintedMagic(Names.TAINTED_MAGIC),
    TecTech(Names.TECTECH),
    Thaumcraft(Names.THAUMCRAFT),
    ThaumicBases(Names.THAUMIC_BASES),
    ThaumicBoots(Names.THAUMIC_BOOTS),
    ThaumicEnergistics(Names.THAUMIC_ENERGISTICS),
    ThaumicExploration(Names.THAUMIC_EXPLORATION),
    ThaumicHorizons(Names.THAUMIC_HORIZONS),
    ThaumicMachina(Names.THAUMIC_MACHINA),
    ThaumicTinkerer(Names.THAUMIC_TINKERER),
    TinkerConstruct(Names.TINKER_CONSTRUCT),
    TinkersDefence(Names.TINKERS_DEFENCE),
    TinkersGregworks(Names.TINKERS_GREGWORKS),
    TinkersMechworks(Names.TINKERS_MECHWORKS),
    Translocator(Names.TRANSLOCATOR),
    TravellersGear(Names.TRAVELLERS_GEAR),
    TwilightForest(Names.TWILIGHT_FOREST),
    UniversalSingularities(Names.UNIVERSAL_SINGULARITIES),
    Waila(Names.WAILA),
    WarpTheory(Names.WARP_THEORY),
    WirelessRedstoneCBEAddons(Names.WIRELESS_REDSTONE_CBE_ADDONS),
    WirelessRedstoneCBECore(Names.WIRELESS_REDSTONE_CBE_CORE),
    WirelessRedstoneCBELogic(Names.WIRELESS_REDSTONE_CBE_LOGIC),
    Witchery(Names.WITCHERY),
    WitchingGadgets(Names.WITCHING_GADGETS),
    ZTones(Names.Z_TONES),

    // Do we keep compat of those?
    ArsMagica2(Names.ARS_MAGICA2),
    GanysSurface(Names.GANYS_SURFACE),
    IndustrialCraft2Classic(Names.INDUSTRIAL_CRAFT2_CLASSIC),
    MagicalCrops(Names.MAGICAL_CROPS),
    Metallurgy(Names.METALLURGY),
    RotaryCraft(Names.ROTARY_CRAFT),
    ThermalExpansion(Names.THERMAL_EXPANSION),
    ThermalFondation(Names.THERMAL_FONDATION),
    UndergroundBiomes(Names.UNDERGROUND_BIOMES),

    ;

    public static class Names {

        public static final String A_E2_FLUID_CRAFT = "ae2fc";
        public static final String AE2STUFF = "ae2stuff";
        public static final String AE2WCT = "ae2wct";
        public static final String A_F_S_U = "AFSU";
        public static final String ADVANCED_SOLAR_PANEL = "AdvancedSolarPanel";
        public static final String ADVENTURE_BACKPACK = "adventurebackpack";
        public static final String APPLE_CORE = "AppleCore";
        public static final String APPLIED_ENERGISTICS2 = "appliedenergistics2";
        public static final String ARCHITECTURE_CRAFT = "ArchitectureCraft";
        public static final String AROMA1997_CORE = "Aroma1997Core";
        public static final String AUTOMAGY = "Automagy";
        public static final String AVARITIA = "Avaritia";
        public static final String AVARITIA_ADDONS = "avaritiaddons";
        public static final String BACKPACK = "Backpack";
        public static final String BART_WORKS = "bartworks";
        public static final String BAUBLES = "Baubles";
        public static final String BETTER_BUILDERS_WANDS = "betterbuilderswands";
        public static final String BETTER_LOADING_SCREEN = "betterloadingscreen";
        public static final String BETTER_QUESTING = "betterquesting";
        public static final String BIBLIO_CRAFT = "BiblioCraft";
        public static final String BIBLIO_WOODS_BO_P_EDITION = "BiblioWoodsBoP";
        public static final String BIBLIO_WOODS_FORESTRY_EDITION = "BiblioWoodsForestry";
        public static final String BIBLIO_WOODS_NATURA_EDITION = "BiblioWoodsNatura";
        public static final String BINNIE_CORE = "BinnieCore";
        public static final String BIOMES_O_PLENTY = "BiomesOPlenty";
        public static final String BLOOD_ARSENAL = "BloodArsenal";
        public static final String BLOOD_MAGIC = "AWWayofTime";
        public static final String BOTANIA = "Botania";
        public static final String BOTANY = "Botany";
        public static final String BUILD_CRAFT_BUILDERS = "BuildCraft|Builders";
        public static final String BUILD_CRAFT_COMPAT = "BuildCraft|Compat";
        public static final String BUILD_CRAFT_CORE = "BuildCraft|Core";
        public static final String BUILD_CRAFT_FACTORY = "BuildCraft|Factory";
        public static final String BUILD_CRAFT_ROBOTICS = "BuildCraft|Robotics";
        public static final String BUILD_CRAFT_SILICON = "BuildCraft|Silicon";
        public static final String BUILD_CRAFT_TRANSPORT = "BuildCraft|Transport";
        public static final String C_O_F_H_CORE = "CoFHCore";
        public static final String CARPENTERS_BLOCKS = "CarpentersBlocks";
        public static final String CAT_WALKS = "catwalks";
        public static final String CHISEL = "chisel";
        public static final String COMPACT_KINETIC_GENERATORS = "compactkineticgenerators";
        public static final String COMPUTRONICS = "computronics";
        public static final String CRAFT_TWEAKER = "MineTweaker3";
        public static final String CROP_LOAD_CORE = "croploadcore";
        public static final String CROPS_PLUS_PLUS = "berriespp";
        public static final String DETRAV_SCANNER_MOD = "detravscannermod";
        public static final String DRACONIC_EVOLUTION = "DraconicEvolution";
        public static final String ELECTRO_MAGIC_TOOLS = "EMT";
        public static final String ENDER_I_O = "EnderIO";
        public static final String ENDER_STORAGE = "EnderStorage";
        public static final String ENDER_ZOO = "EnderZoo";
        public static final String ENHANCED_LOOT_BAGS = "enhancedlootbags";
        public static final String ETERNAL_SINGULARITY = "eternalsingularity";
        public static final String EXTRA_BEES = "ExtraBees";
        public static final String EXTRA_CELLS2 = "extracells";
        public static final String EXTRA_TREES = "ExtraTrees";
        public static final String EXTRA_UTILITIES = "ExtraUtilities";
        public static final String FLOOD_LIGHTS = "FloodLights";
        public static final String FORBIDDEN_MAGIC = "ForbiddenMagic";
        public static final String FORESTRY = "Forestry";
        public static final String FORGE_MICROBLOCKS = "ForgeMicroblock";
        public static final String FORGE_RELOCATION = "ForgeRelocation";
        public static final String G_T_N_H_INTERGALACTIC = "gtnhintergalactic";
        public static final String G_T_N_H_LANTHANIDES = "gtnhlanth";
        public static final String G_T_PLUS_PLUS = "miscutils";
        public static final String G_T_PLUS_PLUS_EVERGLADES = "ToxicEverglades";
        public static final String GADOMANCY = "gadomancy";
        public static final String GALACTI_GREG = "galacticgreg";
        public static final String GALACTICRAFT_AMUN_RA = "GalacticraftAmunRa";
        public static final String GALACTICRAFT_CORE = "GalacticraftCore";
        public static final String GALACTICRAFT_MARS = "GalacticraftMars";
        public static final String GALAXY_SPACE = "GalaxySpace";
        public static final String GENDUSTRY = "gendustry";
        public static final String GENETICS = "Genetics";
        public static final String GOOD_GENERATOR = "GoodGenerator";
        public static final String GRAVI_SUITE = "GraviSuite";
        public static final String GRAVI_SUITE_NEO = "gravisuiteneo";
        public static final String GREG_TECH = "gregtech";
        public static final String HARDCORE_ENDER_EXPANSION = "HardcoreEnderExpansion";
        public static final String HODGE_PODGE = "hodgepodge";
        public static final String HOLO_INVENTORY = "holoinventory";
        public static final String I_C2_CROP_PLUGIN = "Ic2Nei";
        public static final String I_C2_NUCLEAR_CONTROL = "IC2NuclearControl";
        public static final String IGUANA_TWEAKS_TINKER_CONSTRUCT = "IguanaTweaksTConstruct";
        public static final String INDUSTRIAL_CRAFT2 = "IC2";
        public static final String INFERNAL_MOBS = "InfernalMobs";
        public static final String IRON_CHESTS = "IronChest";
        public static final String IRON_CHESTS_MINECARTS = "ironchestminecarts";
        public static final String IRON_TANKS = "irontank";
        public static final String J_A_B_B_A = "JABBA";
        public static final String KEKZ_TECH = "kekztech";
        public static final String KUBA_TECH = "kubatech";
        public static final String LOGISTICS_PIPES = "LogisticsPipes";
        public static final String MC_FRAMES = "MCFrames";
        public static final String MAGIC_BEES = "MagicBees";
        public static final String MALISIS_DOORS = "malisisdoors";
        public static final String MANTLE = "Mantle";
        public static final String MINE_AND_BLADE_BATTLE_GEAR2 = "battlegear2";
        public static final String MINECRAFT = "minecraft";
        public static final String MINE_TWEAKER = "MineTweaker3";
        public static final String N_E_I_CUSTOM_DIAGRAMS = "neicustomdiagram";
        public static final String N_E_I_ORE_PLUGIN = "gtneioreplugin";
        public static final String NATURA = "Natura";
        public static final String NATURES_COMPASS = "naturescompass";
        public static final String NEW_HORIZONS_CORE_MOD = "dreamcraft";
        public static final String NOT_ENOUGH_ITEMS = "NotEnoughItems";
        public static final String OPEN_BLOCKS = "OpenBlocks";
        public static final String OPEN_COMPUTERS = "OpenComputers";
        public static final String OPEN_GLASSES = "openglasses";
        public static final String OPEN_MODULAR_TURRETS = "openmodularturrets";
        public static final String OPEN_PRINTERS = "openprinter";
        public static final String OPEN_SECURITY = "opensecurity";
        public static final String PAMS_HARVEST_CRAFT = "harvestcraft";
        public static final String PAMS_HARVEST_THE_NETHER = "harvestthenether";
        public static final String PLAYER_API = "PlayerAPI";
        public static final String PROJECT_BLUE = "ProjectBlue";
        public static final String PROJECT_RED_CORE = "ProjRed|Core";
        public static final String PROJECT_RED_EXPANSION = "ProjRed|Expansion";
        public static final String PROJECT_RED_EXPLORATION = "ProjRed|Exploration";
        public static final String PROJECT_RED_FABRICATION = "ProjRed|Fabrication";
        public static final String PROJECT_RED_ILLUMINATION = "ProjRed|Illumination";
        public static final String PROJECT_RED_INTEGRATION = "ProjRed|Integration";
        public static final String PROJECT_RED_TRANSMISSION = "ProjRed|Transmission";
        public static final String PROJECT_RED_TRANSPORTATION = "ProjRed|Transportation";
        public static final String QUEST_BOOK = "questbook";
        public static final String RWG = "RWG";
        public static final String RAILCRAFT = "Railcraft";
        public static final String RANDOM_THINGS = "RandomThings";
        public static final String REMOTE_IO = "RIO";
        public static final String S_G_CRAFT = "SGCraft";
        public static final String SLEEPING_BAGS = "sleepingbag";
        public static final String SPICE_OF_LIFE = "SpiceOfLife";
        public static final String STEVES_ADDONS = "StevesAddons";
        public static final String STEVES_CARTS2 = "StevesCarts";
        public static final String STEVES_FACTORY_MANAGER = "StevesFactoryManager";
        public static final String STRUCTURE_LIB = "structurelib";
        public static final String STORAGE_DRAWERS = "StorageDrawers";
        public static final String SUPER_SOLAR_PANELS = "supersolarpanel";
        public static final String TAINTED_MAGIC = "TaintedMagic";
        public static final String TECTECH = "tectech";
        public static final String THAUMCRAFT = "Thaumcraft";
        public static final String THAUMIC_BASES = "thaumicbases";
        public static final String THAUMIC_ENERGISTICS = "thaumicenergistics";
        public static final String THAUMIC_EXPLORATION = "ThaumicExploration";
        public static final String THAUMIC_HORIZONS = "ThaumicHorizons";
        public static final String THAUMIC_BOOTS = "thaumicboots";
        public static final String THAUMIC_MACHINA = "ThaumicMachina";
        public static final String THAUMIC_TINKERER = "ThaumicTinkerer";
        public static final String TINKER_CONSTRUCT = "TConstruct";
        public static final String TINKERS_DEFENCE = "tinkersdefense";
        public static final String TINKERS_GREGWORKS = "TGregworks";
        public static final String TINKERS_MECHWORKS = "TMechworks";
        public static final String TRANSLOCATOR = "Translocator";
        public static final String TRAVELLERS_GEAR = "TravellersGear";
        public static final String TWILIGHT_FOREST = "TwilightForest";
        public static final String UNIVERSAL_SINGULARITIES = "universalsingularities";
        public static final String WAILA = "Waila";
        public static final String WARP_THEORY = "WarpTheory";
        public static final String WIRELESS_REDSTONE_CBE_ADDONS = "WR-CBE|Addons";
        public static final String WIRELESS_REDSTONE_CBE_CORE = "WR-CBE|Core";
        public static final String WIRELESS_REDSTONE_CBE_LOGIC = "WR-CBE|Logic";
        public static final String WITCHERY = "witchery";
        public static final String WITCHING_GADGETS = "WitchingGadgets";
        public static final String Z_TONES = "Ztones";

        // Do we keep compat of those mods?
        public static final String ARS_MAGICA2 = "arsmagica2";
        public static final String GANYS_SURFACE = "ganyssurface";
        public static final String INDUSTRIAL_CRAFT2_CLASSIC = "IC2-Classic-Spmod";
        public static final String MAGICAL_CROPS = "magicalcrops";
        public static final String METALLURGY = "Metallurgy";
        public static final String ROTARY_CRAFT = "RotaryCraft";
        public static final String THERMAL_EXPANSION = "ThermalExpansion";
        public static final String THERMAL_FONDATION = "ThermalFoundation";
        public static final String UNDERGROUND_BIOMES = "UndergroundBiomes";

    }

    public final String ID;
    public final String resourceDomain;
    private Boolean modLoaded;

    Mods(String ID) {
        this.ID = ID;
        this.resourceDomain = ID.toLowerCase(Locale.ENGLISH);
    }

    public boolean isModLoaded() {
        if (this.modLoaded == null) {
            this.modLoaded = Loader.isModLoaded(ID);
        }
        return this.modLoaded;
    }

    public String getResourcePath(String... path) {
        return this.getResourceLocation(path)
            .toString();
    }

    public ResourceLocation getResourceLocation(String... path) {
        return new ResourceLocation(this.resourceDomain, String.join("/", path));
    }
}
