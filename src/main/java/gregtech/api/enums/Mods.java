package gregtech.api.enums;

import java.util.Locale;

import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.common.Loader;

public enum Mods {

    // See "Names" below to see why the mods are in this specific order

    AE2FluidCraft(Names.A_E2_FLUID_CRAFT),
    AFSU(Names.A_F_S_U),
    AdventureBackpack(Names.ADVENTURE_BACKPACK),
    AkashicTome(Names.AKASHIC_TOME),
    AlchemyGrate(Names.ALCHEMY_GRATE),
    AmazingTrophies(Names.AMAZING_TROPHIES),
    Angelica(Names.ANGELICA),
    AngerMod(Names.ANGER_MOD),
    AppleCore(Names.APPLE_CORE),
    AppliedEnergistics2(Names.APPLIED_ENERGISTICS2),
    ArchitectureCraft(Names.ARCHITECTURE_CRAFT),
    AsieLib(Names.ASIE_LIB),
    Avaritia(Names.AVARITIA),
    AvaritiaAddons(Names.AVARITIA_ADDONS),
    MineAndBladeBattleGear2(Names.MINE_AND_BLADE_BATTLE_GEAR2),
    Baubles(Names.BAUBLES),
    BeeBetterAtBees(Names.BEE_BETTER_AT_BEES),
    BetterAchievements(Names.BETTER_ACHIEVEMENTS),
    BetterBuildersWands(Names.BETTER_BUILDERS_WANDS),
    BetterCrashes(Names.BETTER_CRASHES),
    BetterLoadingScreen(Names.BETTER_LOADING_SCREEN),
    BetterP2P(Names.BETTER_P_2_P),
    BetterQuesting(Names.BETTER_QUESTING),
    BinnieCore(Names.BINNIE_CORE),
    Botany(Names.BOTANY),
    ExtraBees(Names.EXTRA_BEES),
    ExtraTrees(Names.EXTRA_TREES),
    Genetics(Names.GENETICS),
    BlockLimiter(Names.BLOCK_LIMITER),
    BlockRenderer6343(Names.BLOCK_RENDERER_6343),
    BloodArsenal(Names.BLOOD_ARSENAL),
    BloodMagic(Names.BLOOD_MAGIC),
    Botania(Names.BOTANIA),
    BotanicHorizons(Names.BOTANIC_HORIZONS),
    BrandonsCore(Names.BRANDONS_CORE),
    BuildCraftCore(Names.BUILD_CRAFT_CORE),
    BuildCraftBuilders(Names.BUILD_CRAFT_BUILDERS),
    BuildCraftFactory(Names.BUILD_CRAFT_FACTORY),
    BuildCraftRobotics(Names.BUILD_CRAFT_ROBOTICS),
    BuildCraftSilicon(Names.BUILD_CRAFT_SILICON),
    BuildCraftTransport(Names.BUILD_CRAFT_TRANSPORT),
    BuildCraftCompat(Names.BUILD_CRAFT_COMPAT),
    BuildCraftOilTweak(Names.BUILD_CRAFT_OIL_TWEAK),
    CarpentersBlocks(Names.CARPENTERS_BLOCKS),
    CatWalks(Names.CAT_WALKS),
    Chisel(Names.CHISEL),
    ChiselTones(Names.CHISEL_TONES),
    CodeChickenCore(Names.CODE_CHICKEN_CORE),
    Computronics(Names.COMPUTRONICS),
    Controlling(Names.CONTROLLING),
    CookingForBlockheads(Names.COOKING_FOR_BLOCKHEADS),
    CoreTweaks(Names.CORE_TWEAKS),
    CraftTweaker(Names.CRAFT_TWEAKER),
    MineTweaker(Names.MINE_TWEAKER),
    CreativeCore(Names.CREATIVE_CORE),
    CropLoadCore(Names.CROP_LOAD_CORE),
    CropsPlusPlus(Names.CROPS_PLUS_PLUS),
    CustomMainMenu(Names.CUSTOM_MAIN_MENU),
    Darkerer(Names.DARKERER),
    DefaultConfigs(Names.DEFAULT_CONFIGS),
    DefaultServerList(Names.DEFAULT_SERVER_LIST),
    DefaultWorldGenerator(Names.DEFAULT_WORLD_GENERATOR),
    DraconicEvolution(Names.DRACONIC_EVOLUTION),
    DummyCore(Names.DUMMY_CORE),
    DuraDisplay(Names.DURA_DISPLAY),
    ElectroMagicTools(Names.ELECTRO_MAGIC_TOOLS),
    EnderCore(Names.ENDER_CORE),
    EnderIO(Names.ENDER_I_O),
    EnderStorage(Names.ENDER_STORAGE),
    EnderZoo(Names.ENDER_ZOO),
    EnhancedLootBags(Names.ENHANCED_LOOT_BAGS),
    EtFuturumRequiem(Names.ET_FUTURUM_REQUIEM),
    EternalSingularity(Names.ETERNAL_SINGULARITY),
    FindIt(Names.FIND_IT),
    FloodLights(Names.FLOOD_LIGHTS),
    ForbiddenMagic(Names.FORBIDDEN_MAGIC),
    Forestry(Names.FORESTRY),
    ForgeMicroblocks(Names.FORGE_MICROBLOCKS),
    ForgeRelocation(Names.FORGE_RELOCATION),
    Forgelin(Names.FORGELIN),
    GregTech(Names.GREG_TECH),
    BartWorks(Names.BART_WORKS),
    DetravScannerMod(Names.DETRAV_SCANNER_MOD),
    GalactiGreg(Names.GALACTI_GREG),
    GGFab(Names.G_G_FAB),
    GoodGenerator(Names.GOOD_GENERATOR),
    GTNHLanthanides(Names.G_T_N_H_LANTHANIDES),
    GTPlusPlus(Names.G_T_PLUS_PLUS),
    GTPlusPlusEverglades(Names.G_T_PLUS_PLUS_EVERGLADES),
    KekzTech(Names.KEKZ_TECH),
    KubaTech(Names.KUBA_TECH),
    NEIOrePlugin(Names.N_E_I_ORE_PLUGIN),
    TecTech(Names.TECTECH),
    GTNHIntergalactic(Names.G_T_N_H_INTERGALACTIC),
    GTNHTCWands(Names.G_T_N_H_T_C_WANDS),
    GTNHLib(Names.G_T_N_H_LIB),
    Gadomancy(Names.GADOMANCY),
    GalacticraftCore(Names.GALACTICRAFT_CORE),
    GalacticraftMars(Names.GALACTICRAFT_MARS),
    GalaxySpace(Names.GALAXY_SPACE),
    GraviSuiteNEO(Names.GRAVI_SUITE_NEO),
    HardcoreEnderExpansion(Names.HARDCORE_ENDER_EXPANSION),
    HelpFixer(Names.HELP_FIXER),
    HodgePodge(Names.HODGE_PODGE),
    HoloInventory(Names.HOLO_INVENTORY),
    HydroEnergy(Names.HYDRO_ENERGY),
    IWillFindYou(Names.I_WILL_FIND_YOU),
    IguanaTweaksTinkerConstruct(Names.IGUANA_TWEAKS_TINKER_CONSTRUCT),
    InGameInfoXML(Names.IN_GAME_INFO_X_M_L),
    InfernalMobs(Names.INFERNAL_MOBS),
    InventoryBogoSorter(Names.INVENTORY_BOGO_SORTER),
    IronChestsMinecarts(Names.IRON_CHESTS_MINECARTS),
    IronTanksMinecarts(Names.IRON_TANKS_MINECARTS),
    IronTanks(Names.IRON_TANKS),
    JABBA(Names.J_A_B_B_A),
    LittleTiles(Names.LITTLE_TILES),
    LogisticsPipes(Names.LOGISTICS_PIPES),
    LootGames(Names.LOOT_GAMES),
    LunatriusCore(Names.LUNATRIUS_CORE),
    MagicBees(Names.MAGIC_BEES),
    MalisisDoors(Names.MALISIS_DOORS),
    Mantle(Names.MANTLE),
    MatterManipulator(Names.MATTER_MANIPULATOR),
    Backpack(Names.BACKPACK),
    MinetweakerGT5Addon(Names.MINETWEAKER_G_T_5_ADDON),
    MobsInfo(Names.MOBS_INFO),
    ModTweaker(Names.MOD_TWEAKER),
    ModularUI(Names.MODULAR_U_I),
    ModularUI2(Names.MODULAR_U_I_2),
    MrTJPCore(Names.MR_TJP_CORE),
    NEIIntegration(Names.N_E_I_INTEGRATION),
    Natura(Names.NATURA),
    NaturesCompass(Names.NATURES_COMPASS),
    Navigator(Names.NAVIGATOR),
    NetherPortalFix(Names.NETHER_PORTAL_FIX),
    NewHorizonsCoreMod(Names.NEW_HORIZONS_CORE_MOD),
    NodalMechanics(Names.NODAL_MECHANICS),
    NotEnoughEnergistics(Names.NOT_ENOUGH_ENERGISTICS),
    NotEnoughIDs(Names.NOT_ENOUGH_I_DS),
    NotEnoughItems(Names.NOT_ENOUGH_ITEMS),
    IC2NuclearControl(Names.I_C2_NUCLEAR_CONTROL),
    Nutrition(Names.NUTRITION),
    OpenGlasses(Names.OPEN_GLASSES),
    OpenBlocks(Names.OPEN_BLOCKS),
    OpenComputers(Names.OPEN_COMPUTERS),
    OpenModsLib(Names.OPEN_MODS_LIB),
    OpenModularTurrets(Names.OPEN_MODULAR_TURRETS),
    OpenPrinters(Names.OPEN_PRINTERS),
    OpenSecurity(Names.OPEN_SECURITY),
    Opis(Names.OPIS),
    OverloadedArmorBar(Names.OVERLOADED_ARMOR_BAR),
    PersonalSpace(Names.PERSONAL_SPACE),
    PlayerAPI(Names.PLAYER_API),
    Postea(Names.POSTEA),
    ProjectBlue(Names.PROJECT_BLUE),
    ProjectRedCore(Names.PROJECT_RED_CORE),
    ProjectRedExpansion(Names.PROJECT_RED_EXPANSION),
    ProjectRedExploration(Names.PROJECT_RED_EXPLORATION),
    ProjectRedFabrication(Names.PROJECT_RED_FABRICATION),
    ProjectRedIllumination(Names.PROJECT_RED_ILLUMINATION),
    ProjectRedIntegration(Names.PROJECT_RED_INTEGRATION),
    ProjectRedTransmission(Names.PROJECT_RED_TRANSMISSION),
    ProjectRedTransportation(Names.PROJECT_RED_TRANSPORTATION),
    Railcraft(Names.RAILCRAFT),
    RandomThings(Names.RANDOM_THINGS),
    RWG(Names.RWG),
    RemoteIO(Names.REMOTE_IO),
    RoguelikeDungeons(Names.ROGUELIKE_DUNGEONS),
    StevesCarts2(Names.STEVES_CARTS2),
    SGCraft(Names.S_G_CRAFT),
    SalisArcana(Names.SALIS_ARCANA),
    Schematica(Names.SCHEMATICA),
    ServerUtilities(Names.SERVER_UTILITIES),
    ShareWhereIAm(Names.SHARE_WHERE_I_AM),
    SleepingBags(Names.SLEEPING_BAGS),
    SpecialMobs(Names.SPECIAL_MOBS),
    SpiceOfLife(Names.SPICE_OF_LIFE),
    StevesFactoryManager(Names.STEVES_FACTORY_MANAGER),
    StevesAddons(Names.STEVES_ADDONS),
    StorageDrawers(Names.STORAGE_DRAWERS),
    StructureCompat(Names.STRUCTURE_COMPAT),
    StructureLib(Names.STRUCTURE_LIB),
    SuperTiC(Names.SUPER_T_I_C),
    TCNEIAdditions(Names.T_C_N_E_I_ADDITIONS),
    TCNodeTracker(Names.T_C_NODE_TRACKER),
    TXLoader(Names.T_X_LOADER),
    TaintedMagic(Names.TAINTED_MAGIC),
    ThaumcraftMobAspects(Names.THAUMCRAFT_MOB_ASPECTS),
    ThaumicBases(Names.THAUMIC_BASES),
    ThaumicBoots(Names.THAUMIC_BOOTS),
    ThaumicEnergistics(Names.THAUMIC_ENERGISTICS),
    ThaumicHorizons(Names.THAUMIC_HORIZONS),
    ThaumicTinkerer(Names.THAUMIC_TINKERER),
    ThaumicExploration(Names.THAUMIC_EXPLORATION),
    TiCTooltips(Names.T_I_C_TOOLTIPS),
    TinkersDefence(Names.TINKERS_DEFENCE),
    TinkerConstruct(Names.TINKER_CONSTRUCT),
    TinkersMechworks(Names.TINKERS_MECHWORKS),
    TooMuchLoot(Names.TOO_MUCH_LOOT),
    ToroHealth(Names.TORO_HEALTH),
    Translocator(Names.TRANSLOCATOR),
    UniversalSingularities(Names.UNIVERSAL_SINGULARITIES),
    VisualProspecting(Names.VISUAL_PROSPECTING),
    WailaPlugins(Names.WAILA_PLUGINS),
    WailaHarvestability(Names.WAILA_HARVESTABILITY),
    WanionLib(Names.WANION_LIB),
    WarpTheory(Names.WARP_THEORY),
    AE2WCT(Names.AE2WCT),
    WirelessRedstoneCBECore(Names.WIRELESS_REDSTONE_CBE_CORE),
    WirelessRedstoneCBEAddons(Names.WIRELESS_REDSTONE_CBE_ADDONS),
    WirelessRedstoneCBELogic(Names.WIRELESS_REDSTONE_CBE_LOGIC),
    WitcheryExtras(Names.WITCHERY_EXTRAS),
    WitchingGadgets(Names.WITCHING_GADGETS),
    YAMCore(Names.YAM_CORE),

    AE2Stuff(Names.AE2STUFF),
    GalacticraftAmunRa(Names.GALACTICRAFT_AMUN_RA),
    BDLib(Names.B_D_LIB),
    Gendustry(Names.GENDUSTRY),
    PamsHarvestCraft(Names.PAMS_HARVEST_CRAFT),
    IronChests(Names.IRON_CHESTS),
    LWJGL3ify(Names.L_W_J_G_L_3IFY),
    NEICustomDiagrams(Names.N_E_I_CUSTOM_DIAGRAMS),
    NEIAddons(Names.N_E_I_ADDONS),
    OAuth(Names.OAUTH),
    SuperSolarPanels(Names.SUPER_SOLAR_PANELS),
    ThaumcraftResearchTweaks(Names.THAUMCRAFT_RESEARCH_TWEAKS),
    ThaumicInsurgence(Names.THAUMIC_INSURGENCE),
    TwilightForest(Names.TWILIGHT_FOREST),
    Waila(Names.WAILA),

    AdvancedSolarPanel(Names.ADVANCED_SOLAR_PANEL),
    ArchaicFix(Names.ARCHAIC_FIX),
    Automagy(Names.AUTOMAGY),
    BiblioCraft(Names.BIBLIO_CRAFT),
    BiblioWoodsBoPEdition(Names.BIBLIO_WOODS_BO_P_EDITION),
    BiblioWoodsForestryEdition(Names.BIBLIO_WOODS_FORESTRY_EDITION),
    BiblioWoodsNaturaEdition(Names.BIBLIO_WOODS_NATURA_EDITION),
    BiomesOPlenty(Names.BIOMES_O_PLENTY),
    BugTorch(Names.BUG_TORCH),
    COFHCore(Names.C_O_F_H_CORE),
    CompactKineticGenerators(Names.COMPACT_KINETIC_GENERATORS),
    CraftPresence(Names.CRAFT_PRESENCE),
    ExtraUtilities(Names.EXTRA_UTILITIES),
    GraviSuite(Names.GRAVI_SUITE),
    HungerOverhaul(Names.HUNGER_OVERHAUL),
    IC2CropPlugin(Names.I_C2_CROP_PLUGIN),
    IndustrialCraft2(Names.INDUSTRIAL_CRAFT2),
    JourneyMap(Names.JOURNEY_MAP),
    Morpheus(Names.MORPHEUS),
    PamsHarvestTheNether(Names.PAMS_HARVEST_THE_NETHER),
    TC4Tweaks(Names.T_C_4_TWEAKS),
    ThaumcraftNEIPlugin(Names.THAUMCRAFT_N_E_I_PLUGIN),
    Thaumcraft(Names.THAUMCRAFT),
    ThaumicMachina(Names.THAUMIC_MACHINA),
    TinkersGregworks(Names.TINKERS_GREGWORKS),
    UniLib(Names.UNI_LIB),
    UniMixins(Names.UNI_MIXINS),
    Witchery(Names.WITCHERY),
    ZTones(Names.Z_TONES),

    Minecraft(Names.MINECRAFT),

    Aroma1997Core(Names.AROMA1997_CORE),
    ExtraCells2(Names.EXTRA_CELLS2),
    MCFrames(Names.MC_FRAMES),
    Metallurgy(Names.METALLURGY),
    QuestBook(Names.QUEST_BOOK),
    RotaryCraft(Names.ROTARY_CRAFT),
    TravellersGear(Names.TRAVELLERS_GEAR),
    UndergroundBiomes(Names.UNDERGROUND_BIOMES),

    ;

    public static class Names {

        // spotless:off

        // Ordered based on the list from DAXXL Actions.
        // If a jar contains multiple mods in this list, they will be done as "one declaration"
        // to preserve order vs the DAXXL Actions list.

        public static final String A_E2_FLUID_CRAFT = "ae2fc";
        public static final String A_F_S_U = "AFSU";
        public static final String ADVENTURE_BACKPACK = "adventurebackpack";
        public static final String AKASHIC_TOME = "akashictome";
        public static final String ALCHEMY_GRATE = "alchgrate";
        public static final String AMAZING_TROPHIES = "amazingtrophies";
        public static final String ANGELICA = "angelica";
        public static final String ANGER_MOD = "angermod";
        public static final String APPLE_CORE = "AppleCore";
        public static final String APPLIED_ENERGISTICS2 = "appliedenergistics2";
        public static final String ARCHITECTURE_CRAFT = "ArchitectureCraft";
        public static final String ASIE_LIB = "asielib";
        public static final String AVARITIA = "Avaritia";
        public static final String AVARITIA_ADDONS = "avaritiaddons";
        public static final String MINE_AND_BLADE_BATTLE_GEAR2 = "battlegear2"; // "Battlegear2"
        public static final String BAUBLES = "Baubles";
        public static final String BEE_BETTER_AT_BEES = "beebetteratbees";
        public static final String BETTER_ACHIEVEMENTS = "BetterAchievements";
        public static final String BETTER_BUILDERS_WANDS = "betterbuilderswands";
        public static final String BETTER_CRASHES = "bettercrashes";
        public static final String BETTER_LOADING_SCREEN = "betterloadingscreen";
        public static final String BETTER_P_2_P = "betterp2p";
        public static final String BETTER_QUESTING = "betterquesting";
        public static final String BINNIE_CORE = "BinnieCore",
            BOTANY = "Botany",
            EXTRA_BEES = "ExtraBees",
            EXTRA_TREES = "ExtraTrees",
            GENETICS = "Genetics";
        public static final String BLOCK_LIMITER = "blocklimiter";
        public static final String BLOCK_RENDERER_6343 = "blockrenderer6343";
        public static final String BLOOD_ARSENAL = "BloodArsenal";
        public static final String BLOOD_MAGIC = "AWWayofTime";
        public static final String BOTANIA = "Botania";
        public static final String BOTANIC_HORIZONS = "botanichorizons";
        public static final String BRANDONS_CORE = "BrandonsCore";
        public static final String BUILD_CRAFT = "BuildCraft",
            BUILD_CRAFT_BUILDERS = "BuildCraft|Builders",
            BUILD_CRAFT_CORE = "BuildCraft|Core",
            BUILD_CRAFT_FACTORY = "BuildCraft|Factory",
            BUILD_CRAFT_ROBOTICS = "BuildCraft|Robotics",
            BUILD_CRAFT_SILICON = "BuildCraft|Silicon",
            BUILD_CRAFT_TRANSPORT = "BuildCraft|Transport";
        public static final String BUILD_CRAFT_COMPAT = "BuildCraft|Compat";
        public static final String BUILD_CRAFT_OIL_TWEAK = "OilTweak";
        public static final String CARPENTERS_BLOCKS = "CarpentersBlocks";
        public static final String CAT_WALKS = "catwalks";
        public static final String CHISEL = "chisel",
            CHISEL_API = "ChiselAPI";
        public static final String CHISEL_TONES = "chiseltones";
        public static final String CODE_CHICKEN_CORE = "CodeChickenCore";
        public static final String COMPUTRONICS = "computronics";
        public static final String CONTROLLING = "controlling";
        public static final String COOKING_FOR_BLOCKHEADS = "cookingforblockheads";
        public static final String CORE_TWEAKS = "coretweaks";
        public static final String CRAFT_TWEAKER = "MineTweaker3",
            MINE_TWEAKER = "MineTweaker3";
        public static final String CREATIVE_CORE = "creativecore";
        public static final String CROP_LOAD_CORE = "croploadcore";
        public static final String CROPS_PLUS_PLUS = "berriespp";
        public static final String CUSTOM_MAIN_MENU = "custommainmenu";
        public static final String DARKERER = "darkerer";
        public static final String DEFAULT_CONFIGS = "defaultkeys";
        public static final String DEFAULT_SERVER_LIST = "defaultserverlist";
        public static final String DEFAULT_WORLD_GENERATOR = "defaultworldgenerator";
        public static final String DRACONIC_EVOLUTION = "DraconicEvolution";
        public static final String DUMMY_CORE = "DummyCore";
        public static final String DURA_DISPLAY = "duradisplay";
        public static final String ELECTRO_MAGIC_TOOLS = "EMT";
        public static final String ENDER_CORE = "endercore";
        public static final String ENDER_I_O = "EnderIO";
        public static final String ENDER_STORAGE = "EnderStorage";
        public static final String ENDER_ZOO = "EnderZoo";
        public static final String ENHANCED_LOOT_BAGS = "enhancedlootbags";
        public static final String ET_FUTURUM_REQUIEM = "etfuturum";
        public static final String ETERNAL_SINGULARITY = "eternalsingularity";
        public static final String FIND_IT = "findit";
        public static final String FLOOD_LIGHTS = "FloodLights";
        public static final String FORBIDDEN_MAGIC = "ForbiddenMagic";
        public static final String FORESTRY = "Forestry";
        public static final String FORGE_MICROBLOCKS = "ForgeMicroblock";
        public static final String FORGE_RELOCATION = "ForgeRelocation";
        public static final String FORGELIN = "forgelin";
        public static final String GREG_TECH = "gregtech",
            BART_WORKS = "bartworks",
            DETRAV_SCANNER_MOD = "detravscannermod",
            GALACTI_GREG = "galacticgreg",
            G_G_FAB = "ggfab",
            GOOD_GENERATOR = "GoodGenerator",
            G_T_N_H_LANTHANIDES = "gtnhlanth",
            G_T_PLUS_PLUS = "miscutils",
            G_T_PLUS_PLUS_EVERGLADES = "ToxicEverglades",
            KEKZ_TECH = "kekztech",
            KUBA_TECH = "kubatech",
            N_E_I_ORE_PLUGIN = "gtneioreplugin",
            TECTECH = "tectech";
        public static final String G_T_N_H_INTERGALACTIC = "gtnhintergalactic";
        public static final String G_T_N_H_T_C_WANDS = "gtnhtcwands";
        public static final String G_T_N_H_LIB = "gtnhlib";
        public static final String GADOMANCY = "gadomancy";
        public static final String GALACTICRAFT_CORE = "GalacticraftCore",
            GALACTICRAFT_MARS = "GalacticraftMars";
        public static final String GALAXY_SPACE = "GalaxySpace";
        public static final String GRAVI_SUITE_NEO = "gravisuiteneo";
        public static final String HARDCORE_ENDER_EXPANSION = "HardcoreEnderExpansion";
        public static final String HELP_FIXER = "HelpFixer";
        public static final String HODGE_PODGE = "hodgepodge";
        public static final String HOLO_INVENTORY = "holoinventory";
        public static final String HYDRO_ENERGY = "hydroenergy";
        public static final String I_WILL_FIND_YOU = "ifu"; // "IFU"
        public static final String IGUANA_TWEAKS_TINKER_CONSTRUCT = "IguanaTweaksTConstruct";
        public static final String IN_GAME_INFO_X_M_L = "InGameInfoXML";
        public static final String INFERNAL_MOBS = "InfernalMobs";
        public static final String INVENTORY_BOGO_SORTER = "bogosorter";
        public static final String IRON_CHESTS_MINECARTS = "ironchestminecarts";
        public static final String IRON_TANKS_MINECARTS = "irontankminecarts";
        public static final String IRON_TANKS = "irontank";
        public static final String J_A_B_B_A = "JABBA";
        public static final String LITTLE_TILES = "littletiles";
        public static final String LOGISTICS_PIPES = "LogisticsPipes";
        public static final String LOOT_GAMES = "lootgames";
        public static final String LUNATRIUS_CORE = "LunatriusCore";
        public static final String MAGIC_BEES = "MagicBees";
        public static final String MALISIS_DOORS = "malisisdoors";
        public static final String MANTLE = "Mantle";
        public static final String MATTER_MANIPULATOR = "matter-manipulator";
        public static final String BACKPACK = "Backpack"; // "Minecraft Backpack Mod"
        public static final String MINETWEAKER_G_T_5_ADDON = "GTTweaker";
        public static final String MOBS_INFO = "mobsinfo";
        public static final String MOD_TWEAKER = "modtweaker2";
        public static final String MODULAR_U_I = "modularui";
        public static final String MODULAR_U_I_2 = "modularui2";
        public static final String MR_TJP_CORE = "MrTJPCoreMod";
        public static final String N_E_I_INTEGRATION = "neiintegration";
        public static final String NATURA = "Natura";
        public static final String NATURES_COMPASS = "naturescompass";
        public static final String NAVIGATOR = "navigator";
        public static final String NETHER_PORTAL_FIX = "netherportalfix";
        public static final String NEW_HORIZONS_CORE_MOD = "dreamcraft";
        public static final String NODAL_MECHANICS = "NodalMechanics";
        public static final String NOT_ENOUGH_ENERGISTICS = "neenergistics";
        public static final String NOT_ENOUGH_I_DS = "neid";
        public static final String NOT_ENOUGH_ITEMS = "NotEnoughItems";
        public static final String I_C2_NUCLEAR_CONTROL = "IC2NuclearControl"; // "Nuclear-Control"
        public static final String NUTRITION = "nutrition";
        public static final String OPEN_GLASSES = "openglasses"; // "OCGlasses"
        public static final String OPEN_BLOCKS = "OpenBlocks";
        public static final String OPEN_COMPUTERS = "OpenComputers";
        public static final String OPEN_MODS_LIB = "OpenMods";
        public static final String OPEN_MODULAR_TURRETS = "openmodularturrets";
        public static final String OPEN_PRINTERS = "openprinter";
        public static final String OPEN_SECURITY = "opensecurity";
        public static final String OPIS = "Opis";
        public static final String OVERLOADED_ARMOR_BAR = "overloadedarmorbar";
        public static final String PERSONAL_SPACE = "personalspace";
        public static final String PLAYER_API = "PlayerAPI";
        public static final String POSTEA = "postea";
        public static final String PROJECT_BLUE = "ProjectBlue";
        public static final String PROJECT_RED_CORE = "ProjRed|Core",
            PROJECT_RED_EXPANSION = "ProjRed|Expansion",
            PROJECT_RED_EXPLORATION = "ProjRed|Exploration",
            PROJECT_RED_FABRICATION = "ProjRed|Fabrication",
            PROJECT_RED_ILLUMINATION = "ProjRed|Illumination",
            PROJECT_RED_INTEGRATION = "ProjRed|Integration",
            PROJECT_RED_TRANSMISSION = "ProjRed|Transmission",
            PROJECT_RED_TRANSPORTATION = "ProjRed|Transportation";
        public static final String RAILCRAFT = "Railcraft";
        public static final String RANDOM_THINGS = "RandomThings";
        public static final String RWG = "RWG";
        public static final String REMOTE_IO = "RIO";
        public static final String ROGUELIKE_DUNGEONS = "Roguelike";
        public static final String STEVES_CARTS2 = "StevesCarts"; // "SC2"
        public static final String S_G_CRAFT = "SGCraft";
        public static final String SALIS_ARCANA = "salisarcana";
        public static final String SCHEMATICA = "Schematica";
        public static final String SERVER_UTILITIES = "serverutilities";
        public static final String SHARE_WHERE_I_AM = "sharewhereiam";
        public static final String SLEEPING_BAGS = "sleepingbag";
        public static final String SPECIAL_MOBS = "SpecialMobs";
        public static final String SPICE_OF_LIFE = "SpiceOfLife";
        public static final String STEVES_FACTORY_MANAGER = "StevesFactoryManager";
        public static final String STEVES_ADDONS = "StevesAddons";
        public static final String STORAGE_DRAWERS = "StorageDrawers";
        public static final String STRUCTURE_COMPAT = "structurecompat";
        public static final String STRUCTURE_LIB = "structurelib";
        public static final String SUPER_T_I_C = "SuperTic";
        public static final String T_C_N_E_I_ADDITIONS = "tcneiadditions";
        public static final String T_C_NODE_TRACKER = "tcnodetracker";
        public static final String T_X_LOADER = "txloader";
        public static final String TAINTED_MAGIC = "TaintedMagic";
        public static final String THAUMCRAFT_MOB_ASPECTS = "ThaumcraftMobAspects";
        public static final String THAUMIC_BASES = "thaumicbases";
        public static final String THAUMIC_BOOTS = "thaumicboots";
        public static final String THAUMIC_ENERGISTICS = "thaumicenergistics";
        public static final String THAUMIC_HORIZONS = "ThaumicHorizons";
        public static final String THAUMIC_TINKERER = "ThaumicTinkerer";
        public static final String THAUMIC_EXPLORATION = "ThaumicExploration";
        public static final String T_I_C_TOOLTIPS = "TiCTooltips";
        public static final String TINKERS_DEFENCE = "tinkersdefense";
        public static final String TINKER_CONSTRUCT = "TConstruct";
        public static final String TINKERS_MECHWORKS = "TMechworks";
        public static final String TOO_MUCH_LOOT = "TML";
        public static final String TORO_HEALTH = "torohealthmod";
        public static final String TRANSLOCATOR = "Translocator";
        public static final String UNIVERSAL_SINGULARITIES = "universalsingularities";
        public static final String VISUAL_PROSPECTING = "visualprospecting";
        public static final String WAILA_PLUGINS = "wailaplugins";
        public static final String WAILA_HARVESTABILITY = "WailaHarvestability";
        public static final String WANION_LIB = "wanionlib";
        public static final String WARP_THEORY = "WarpTheory";
        public static final String AE2WCT = "ae2wct"; // "WirelessCraftingTerminal"
        public static final String WIRELESS_REDSTONE_CBE_CORE = "WR-CBE|Core",
            WIRELESS_REDSTONE_CBE_ADDONS = "WR-CBE|Addons",
            WIRELESS_REDSTONE_CBE_LOGIC = "WR-CBE|Logic";
        public static final String WITCHERY_EXTRAS = "WitcheryExtras";
        public static final String WITCHING_GADGETS = "WitchingGadgets";
        public static final String YAM_CORE = "YAMCore"; // "Yamcl"

        // Lowercase file names in DAXXL Actions, listed later than the above
        public static final String AE2STUFF = "ae2stuff";
        public static final String GALACTICRAFT_AMUN_RA = "GalacticraftAmunRa"; // "amunra"
        public static final String B_D_LIB = "bdlib";
        public static final String GENDUSTRY = "gendustry";
        public static final String PAMS_HARVEST_CRAFT = "harvestcraft"; // "harvestcraft"
        public static final String IRON_CHESTS = "IronChest";
        public static final String L_W_J_G_L_3IFY = "lwjgl3ify";
        public static final String N_E_I_CUSTOM_DIAGRAMS = "neicustomdiagram";
        public static final String N_E_I_ADDONS = "NEIAddons";
        public static final String OAUTH = "oauth";
        public static final String SUPER_SOLAR_PANELS = "supersolarpanel";
        public static final String THAUMCRAFT_RESEARCH_TWEAKS = "ThaumcraftResearchTweaks";
        public static final String THAUMIC_INSURGENCE = "thaumicinsurgence";
        public static final String TWILIGHT_FOREST = "TwilightForest";
        public static final String WAILA = "Waila";

        // "External mods" section in DAXXL Actions
        public static final String ADVANCED_SOLAR_PANEL = "AdvancedSolarPanel";
        public static final String ARCHAIC_FIX = "archaicfix";
        public static final String AUTOMAGY = "Automagy";
        public static final String BIBLIO_CRAFT = "BiblioCraft";
        public static final String BIBLIO_WOODS_BO_P_EDITION = "BiblioWoodsBoP";
        public static final String BIBLIO_WOODS_FORESTRY_EDITION = "BiblioWoodsForestry";
        public static final String BIBLIO_WOODS_NATURA_EDITION = "BiblioWoodsNatura";
        public static final String BIOMES_O_PLENTY = "BiomesOPlenty";
        public static final String BUG_TORCH = "bugtorch";
        public static final String C_O_F_H_CORE = "CoFHCore";
        public static final String COMPACT_KINETIC_GENERATORS = "compactkineticgenerators";
        public static final String CRAFT_PRESENCE = "craftpresence";
        public static final String EXTRA_UTILITIES = "ExtraUtilities";
        public static final String GRAVI_SUITE = "GraviSuite";
        // "Healer" - Don't know what this is or where it comes from but it's in the list
        public static final String HUNGER_OVERHAUL = "HungerOverhaul";
        public static final String I_C2_CROP_PLUGIN = "Ic2Nei";
        public static final String INDUSTRIAL_CRAFT2 = "IC2";
        // JourneyMap Server
        public static final String JOURNEY_MAP = "journeymap";
        public static final String MORPHEUS = "Morpheus";
        public static final String PAMS_HARVEST_THE_NETHER = "harvestthenether";
        public static final String T_C_4_TWEAKS = "tc4tweak";
        public static final String THAUMCRAFT_N_E_I_PLUGIN = "thaumcraftneiplugin";
        public static final String THAUMCRAFT = "Thaumcraft";
        public static final String THAUMIC_MACHINA = "ThaumicMachina";
        public static final String TINKERS_GREGWORKS = "TGregworks";
        public static final String UNI_LIB = "unilib";
        public static final String UNI_MIXINS = "unimixins";
        public static final String WITCHERY = "witchery";
        public static final String Z_TONES = "Ztones";

        // Minecraft :)
        public static final String MINECRAFT = "minecraft";

        // Misc, should eventually be removed
        public static final String AROMA1997_CORE = "Aroma1997Core";
        public static final String EXTRA_CELLS2 = "extracells";
        public static final String MC_FRAMES = "MCFrames";
        public static final String METALLURGY = "Metallurgy";
        public static final String QUEST_BOOK = "questbook";
        public static final String ROTARY_CRAFT = "RotaryCraft";
        public static final String TRAVELLERS_GEAR = "TravellersGear";
        public static final String UNDERGROUND_BIOMES = "UndergroundBiomes";

        // spotless:on
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
