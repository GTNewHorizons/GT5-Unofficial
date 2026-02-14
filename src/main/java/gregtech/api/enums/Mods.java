package gregtech.api.enums;

import java.util.Locale;

import net.minecraft.util.ResourceLocation;

import com.gtnewhorizon.gtnhlib.util.data.IMod;

import cpw.mods.fml.common.Loader;

public enum Mods implements IMod {

    // See inner class "ModIDs" below to see why the mods are in this specific order

    AE2FluidCraft(ModIDs.A_E2_FLUID_CRAFT),
    AFSU(ModIDs.A_F_S_U),
    AdventureBackpack(ModIDs.ADVENTURE_BACKPACK),
    AkashicTome(ModIDs.AKASHIC_TOME),
    AlchemyGrate(ModIDs.ALCHEMY_GRATE),
    AmazingTrophies(ModIDs.AMAZING_TROPHIES),
    Angelica(ModIDs.ANGELICA),
    AngerMod(ModIDs.ANGER_MOD),
    AppleCore(ModIDs.APPLE_CORE),
    AppliedEnergistics2(ModIDs.APPLIED_ENERGISTICS2),
    ArchitectureCraft(ModIDs.ARCHITECTURE_CRAFT),
    AsieLib(ModIDs.ASIE_LIB),
    Avaritia(ModIDs.AVARITIA),
    AvaritiaAddons(ModIDs.AVARITIA_ADDONS),
    MineAndBladeBattleGear2(ModIDs.MINE_AND_BLADE_BATTLE_GEAR2),
    Backhand(ModIDs.BACKHAND),
    Baubles(ModIDs.BAUBLES),
    BeeBetterAtBees(ModIDs.BEE_BETTER_AT_BEES),
    BetterAchievements(ModIDs.BETTER_ACHIEVEMENTS),
    BetterBuildersWands(ModIDs.BETTER_BUILDERS_WANDS),
    BetterCrashes(ModIDs.BETTER_CRASHES),
    BetterLoadingScreen(ModIDs.BETTER_LOADING_SCREEN),
    BetterP2P(ModIDs.BETTER_P_2_P),
    BetterQuesting(ModIDs.BETTER_QUESTING),
    BinnieCore(ModIDs.BINNIE_CORE),
    Botany(ModIDs.BOTANY),
    ExtraBees(ModIDs.EXTRA_BEES),
    ExtraTrees(ModIDs.EXTRA_TREES),
    Genetics(ModIDs.GENETICS),
    BlockLimiter(ModIDs.BLOCK_LIMITER),
    BlockRenderer6343(ModIDs.BLOCK_RENDERER_6343),
    BloodArsenal(ModIDs.BLOOD_ARSENAL),
    BloodMagic(ModIDs.BLOOD_MAGIC),
    Botania(ModIDs.BOTANIA),
    BotanicHorizons(ModIDs.BOTANIC_HORIZONS),
    BrandonsCore(ModIDs.BRANDONS_CORE),
    BuildCraftCore(ModIDs.BUILD_CRAFT_CORE),
    BuildCraftBuilders(ModIDs.BUILD_CRAFT_BUILDERS),
    BuildCraftFactory(ModIDs.BUILD_CRAFT_FACTORY),
    BuildCraftRobotics(ModIDs.BUILD_CRAFT_ROBOTICS),
    BuildCraftSilicon(ModIDs.BUILD_CRAFT_SILICON),
    BuildCraftTransport(ModIDs.BUILD_CRAFT_TRANSPORT),
    BuildCraftCompat(ModIDs.BUILD_CRAFT_COMPAT),
    BuildCraftOilTweak(ModIDs.BUILD_CRAFT_OIL_TWEAK),
    CarpentersBlocks(ModIDs.CARPENTERS_BLOCKS),
    CatWalks(ModIDs.CAT_WALKS),
    Chisel(ModIDs.CHISEL),
    ChiselTones(ModIDs.CHISEL_TONES),
    CodeChickenCore(ModIDs.CODE_CHICKEN_CORE),
    Computronics(ModIDs.COMPUTRONICS),
    Controlling(ModIDs.CONTROLLING),
    CookingForBlockheads(ModIDs.COOKING_FOR_BLOCKHEADS),
    CoreTweaks(ModIDs.CORE_TWEAKS),
    CraftTweaker(ModIDs.CRAFT_TWEAKER),
    MineTweaker(ModIDs.MINE_TWEAKER),
    CreativeCore(ModIDs.CREATIVE_CORE),
    CropLoadCore(ModIDs.CROP_LOAD_CORE),
    CropsPlusPlus(ModIDs.CROPS_PLUS_PLUS),
    CropsNH(ModIDs.CROPS_NH),
    CustomMainMenu(ModIDs.CUSTOM_MAIN_MENU),
    Darkerer(ModIDs.DARKERER),
    DefaultConfigs(ModIDs.DEFAULT_CONFIGS),
    DefaultServerList(ModIDs.DEFAULT_SERVER_LIST),
    DefaultWorldGenerator(ModIDs.DEFAULT_WORLD_GENERATOR),
    DraconicEvolution(ModIDs.DRACONIC_EVOLUTION),
    DummyCore(ModIDs.DUMMY_CORE),
    DuraDisplay(ModIDs.DURA_DISPLAY),
    ElectroMagicTools(ModIDs.ELECTRO_MAGIC_TOOLS),
    EnderCore(ModIDs.ENDER_CORE),
    EnderIO(ModIDs.ENDER_I_O),
    EnderStorage(ModIDs.ENDER_STORAGE),
    EnderZoo(ModIDs.ENDER_ZOO),
    EnhancedLootBags(ModIDs.ENHANCED_LOOT_BAGS),
    EtFuturumRequiem(ModIDs.ET_FUTURUM_REQUIEM),
    EternalSingularity(ModIDs.ETERNAL_SINGULARITY),
    FindIt(ModIDs.FIND_IT),
    FloodLights(ModIDs.FLOOD_LIGHTS),
    ForbiddenMagic(ModIDs.FORBIDDEN_MAGIC),
    Forestry(ModIDs.FORESTRY),
    ForgeMicroblocks(ModIDs.FORGE_MICROBLOCKS),
    ForgeRelocation(ModIDs.FORGE_RELOCATION),
    Forgelin(ModIDs.FORGELIN),
    GregTech(ModIDs.GREG_TECH),
    BartWorks(ModIDs.BART_WORKS),
    DetravScannerMod(ModIDs.DETRAV_SCANNER_MOD),
    GalactiGreg(ModIDs.GALACTI_GREG),
    GGFab(ModIDs.G_G_FAB),
    GoodGenerator(ModIDs.GOOD_GENERATOR),
    GTNHLanthanides(ModIDs.G_T_N_H_LANTHANIDES),
    GTPlusPlus(ModIDs.G_T_PLUS_PLUS),
    GTPlusPlusEverglades(ModIDs.G_T_PLUS_PLUS_EVERGLADES),
    KekzTech(ModIDs.KEKZ_TECH),
    KubaTech(ModIDs.KUBA_TECH),
    NEIOrePlugin(ModIDs.N_E_I_ORE_PLUGIN),
    TecTech(ModIDs.TECTECH),
    GTNHIntergalactic(ModIDs.G_T_N_H_INTERGALACTIC),
    GTNHTCWands(ModIDs.G_T_N_H_T_C_WANDS),
    GTNHLib(ModIDs.G_T_N_H_LIB),
    Gadomancy(ModIDs.GADOMANCY),
    GalacticraftCore(ModIDs.GALACTICRAFT_CORE),
    GalacticraftMars(ModIDs.GALACTICRAFT_MARS),
    GalaxySpace(ModIDs.GALAXY_SPACE),
    GraviSuiteNEO(ModIDs.GRAVI_SUITE_NEO),
    HardcoreEnderExpansion(ModIDs.HARDCORE_ENDER_EXPANSION),
    HelpFixer(ModIDs.HELP_FIXER),
    HodgePodge(ModIDs.HODGE_PODGE),
    HoloInventory(ModIDs.HOLO_INVENTORY),
    HydroEnergy(ModIDs.HYDRO_ENERGY),
    IWillFindYou(ModIDs.I_WILL_FIND_YOU),
    IguanaTweaksTinkerConstruct(ModIDs.IGUANA_TWEAKS_TINKER_CONSTRUCT),
    InGameInfoXML(ModIDs.IN_GAME_INFO_X_M_L),
    InfernalMobs(ModIDs.INFERNAL_MOBS),
    InventoryBogoSorter(ModIDs.INVENTORY_BOGO_SORTER),
    IronChestsMinecarts(ModIDs.IRON_CHESTS_MINECARTS),
    IronTanksMinecarts(ModIDs.IRON_TANKS_MINECARTS),
    IronTanks(ModIDs.IRON_TANKS),
    JABBA(ModIDs.J_A_B_B_A),
    LittleTiles(ModIDs.LITTLE_TILES),
    LogisticsPipes(ModIDs.LOGISTICS_PIPES),
    LootGames(ModIDs.LOOT_GAMES),
    LunatriusCore(ModIDs.LUNATRIUS_CORE),
    MagicBees(ModIDs.MAGIC_BEES),
    MalisisDoors(ModIDs.MALISIS_DOORS),
    Mantle(ModIDs.MANTLE),
    MatterManipulator(ModIDs.MATTER_MANIPULATOR),
    Backpack(ModIDs.BACKPACK),
    MinetweakerGT5Addon(ModIDs.MINETWEAKER_G_T_5_ADDON),
    MobsInfo(ModIDs.MOBS_INFO),
    ModTweaker(ModIDs.MOD_TWEAKER),
    ModularUI(ModIDs.MODULAR_U_I),
    ModularUI2(ModIDs.MODULAR_U_I_2),
    MrTJPCore(ModIDs.MR_TJP_CORE),
    NEIIntegration(ModIDs.N_E_I_INTEGRATION),
    Natura(ModIDs.NATURA),
    NaturesCompass(ModIDs.NATURES_COMPASS),
    Navigator(ModIDs.NAVIGATOR),
    NetherPortalFix(ModIDs.NETHER_PORTAL_FIX),
    NewHorizonsCoreMod(ModIDs.NEW_HORIZONS_CORE_MOD),
    NodalMechanics(ModIDs.NODAL_MECHANICS),
    NotEnoughEnergistics(ModIDs.NOT_ENOUGH_ENERGISTICS),
    NotEnoughIDs(ModIDs.NOT_ENOUGH_I_DS),
    NotEnoughItems(ModIDs.NOT_ENOUGH_ITEMS),
    IC2NuclearControl(ModIDs.I_C2_NUCLEAR_CONTROL),
    NuclearHorizons(ModIDs.NUCLEAR_HORIZONS),
    Nutrition(ModIDs.NUTRITION),
    OpenGlasses(ModIDs.OPEN_GLASSES),
    OpenBlocks(ModIDs.OPEN_BLOCKS),
    OpenComputers(ModIDs.OPEN_COMPUTERS),
    OpenModsLib(ModIDs.OPEN_MODS_LIB),
    OpenModularTurrets(ModIDs.OPEN_MODULAR_TURRETS),
    OpenPrinters(ModIDs.OPEN_PRINTERS),
    OpenSecurity(ModIDs.OPEN_SECURITY),
    Opis(ModIDs.OPIS),
    OverloadedArmorBar(ModIDs.OVERLOADED_ARMOR_BAR),
    PersonalSpace(ModIDs.PERSONAL_SPACE),
    PlayerAPI(ModIDs.PLAYER_API),
    Postea(ModIDs.POSTEA),
    ProjectBlue(ModIDs.PROJECT_BLUE),
    ProjectRedCore(ModIDs.PROJECT_RED_CORE),
    ProjectRedExpansion(ModIDs.PROJECT_RED_EXPANSION),
    ProjectRedExploration(ModIDs.PROJECT_RED_EXPLORATION),
    ProjectRedFabrication(ModIDs.PROJECT_RED_FABRICATION),
    ProjectRedIllumination(ModIDs.PROJECT_RED_ILLUMINATION),
    ProjectRedIntegration(ModIDs.PROJECT_RED_INTEGRATION),
    ProjectRedTransmission(ModIDs.PROJECT_RED_TRANSMISSION),
    ProjectRedTransportation(ModIDs.PROJECT_RED_TRANSPORTATION),
    Railcraft(ModIDs.RAILCRAFT),
    RandomBoubles(ModIDs.RANDOM_BOUBLES),
    RandomThings(ModIDs.RANDOM_THINGS),
    RWG(ModIDs.RWG),
    RemoteIO(ModIDs.REMOTE_IO),
    RoguelikeDungeons(ModIDs.ROGUELIKE_DUNGEONS),
    StevesCarts2(ModIDs.STEVES_CARTS2),
    SGCraft(ModIDs.S_G_CRAFT),
    SalisArcana(ModIDs.SALIS_ARCANA),
    Schematica(ModIDs.SCHEMATICA),
    ServerUtilities(ModIDs.SERVER_UTILITIES),
    ShareWhereIAm(ModIDs.SHARE_WHERE_I_AM),
    SleepingBags(ModIDs.SLEEPING_BAGS),
    SpecialMobs(ModIDs.SPECIAL_MOBS),
    SpiceOfLife(ModIDs.SPICE_OF_LIFE),
    StevesFactoryManager(ModIDs.STEVES_FACTORY_MANAGER),
    StevesAddons(ModIDs.STEVES_ADDONS),
    StorageDrawers(ModIDs.STORAGE_DRAWERS),
    StructureCompat(ModIDs.STRUCTURE_COMPAT),
    StructureLib(ModIDs.STRUCTURE_LIB),
    SuperTiC(ModIDs.SUPER_T_I_C),
    TCNEIAdditions(ModIDs.T_C_N_E_I_ADDITIONS),
    TCNodeTracker(ModIDs.T_C_NODE_TRACKER),
    TXLoader(ModIDs.T_X_LOADER),
    TaintedMagic(ModIDs.TAINTED_MAGIC),
    ThaumcraftMobAspects(ModIDs.THAUMCRAFT_MOB_ASPECTS),
    ThaumicBases(ModIDs.THAUMIC_BASES),
    ThaumicBoots(ModIDs.THAUMIC_BOOTS),
    ThaumicEnergistics(ModIDs.THAUMIC_ENERGISTICS),
    ThaumicHorizons(ModIDs.THAUMIC_HORIZONS),
    ThaumicTinkerer(ModIDs.THAUMIC_TINKERER),
    ThaumicExploration(ModIDs.THAUMIC_EXPLORATION),
    TiCTooltips(ModIDs.T_I_C_TOOLTIPS),
    TinkersDefence(ModIDs.TINKERS_DEFENCE),
    TinkerConstruct(ModIDs.TINKER_CONSTRUCT),
    TinkersMechworks(ModIDs.TINKERS_MECHWORKS),
    TooMuchLoot(ModIDs.TOO_MUCH_LOOT),
    ToroHealth(ModIDs.TORO_HEALTH),
    Translocator(ModIDs.TRANSLOCATOR),
    UniversalSingularities(ModIDs.UNIVERSAL_SINGULARITIES),
    VendingMachine(ModIDs.VENDING_MACHINE),
    VisualProspecting(ModIDs.VISUAL_PROSPECTING),
    WailaPlugins(ModIDs.WAILA_PLUGINS),
    WailaHarvestability(ModIDs.WAILA_HARVESTABILITY),
    WanionLib(ModIDs.WANION_LIB),
    WarpTheory(ModIDs.WARP_THEORY),
    AE2WCT(ModIDs.AE2WCT),
    WirelessRedstoneCBECore(ModIDs.WIRELESS_REDSTONE_CBE_CORE),
    WirelessRedstoneCBEAddons(ModIDs.WIRELESS_REDSTONE_CBE_ADDONS),
    WirelessRedstoneCBELogic(ModIDs.WIRELESS_REDSTONE_CBE_LOGIC),
    WitcheryExtras(ModIDs.WITCHERY_EXTRAS),
    WitchingGadgets(ModIDs.WITCHING_GADGETS),
    YAMCore(ModIDs.YAM_CORE),

    AE2Stuff(ModIDs.AE2STUFF),
    GalacticraftAmunRa(ModIDs.GALACTICRAFT_AMUN_RA),
    BDLib(ModIDs.B_D_LIB),
    Gendustry(ModIDs.GENDUSTRY),
    PamsHarvestCraft(ModIDs.PAMS_HARVEST_CRAFT),
    IronChests(ModIDs.IRON_CHESTS),
    LWJGL3ify(ModIDs.L_W_J_G_L_3IFY),
    NEICustomDiagrams(ModIDs.N_E_I_CUSTOM_DIAGRAMS),
    NEIAddons(ModIDs.N_E_I_ADDONS),
    OAuth(ModIDs.OAUTH),
    SuperSolarPanels(ModIDs.SUPER_SOLAR_PANELS),
    ThaumcraftResearchTweaks(ModIDs.THAUMCRAFT_RESEARCH_TWEAKS),
    ThaumicInsurgence(ModIDs.THAUMIC_INSURGENCE),
    TwilightForest(ModIDs.TWILIGHT_FOREST),
    Waila(ModIDs.WAILA),

    AdvancedSolarPanel(ModIDs.ADVANCED_SOLAR_PANEL),
    ArchaicFix(ModIDs.ARCHAIC_FIX),
    Automagy(ModIDs.AUTOMAGY),
    BiblioCraft(ModIDs.BIBLIO_CRAFT),
    BiblioWoodsBoPEdition(ModIDs.BIBLIO_WOODS_BO_P_EDITION),
    BiblioWoodsForestryEdition(ModIDs.BIBLIO_WOODS_FORESTRY_EDITION),
    BiblioWoodsNaturaEdition(ModIDs.BIBLIO_WOODS_NATURA_EDITION),
    BiomesOPlenty(ModIDs.BIOMES_O_PLENTY),
    BugTorch(ModIDs.BUG_TORCH),
    COFHCore(ModIDs.C_O_F_H_CORE),
    CompactKineticGenerators(ModIDs.COMPACT_KINETIC_GENERATORS),
    CraftPresence(ModIDs.CRAFT_PRESENCE),
    ExtraUtilities(ModIDs.EXTRA_UTILITIES),
    GraviSuite(ModIDs.GRAVI_SUITE),
    HungerOverhaul(ModIDs.HUNGER_OVERHAUL),
    IC2CropPlugin(ModIDs.I_C2_CROP_PLUGIN),
    IndustrialCraft2(ModIDs.INDUSTRIAL_CRAFT2),
    JourneyMap(ModIDs.JOURNEY_MAP),
    Morpheus(ModIDs.MORPHEUS),
    PamsHarvestTheNether(ModIDs.PAMS_HARVEST_THE_NETHER),
    TC4Tweaks(ModIDs.T_C_4_TWEAKS),
    ThaumcraftNEIPlugin(ModIDs.THAUMCRAFT_N_E_I_PLUGIN),
    Thaumcraft(ModIDs.THAUMCRAFT),
    ThaumicMachina(ModIDs.THAUMIC_MACHINA),
    TinkersGregworks(ModIDs.TINKERS_GREGWORKS),
    UniLib(ModIDs.UNI_LIB),
    UniMixins(ModIDs.UNI_MIXINS),
    Witchery(ModIDs.WITCHERY),
    ZTones(ModIDs.Z_TONES),

    Minecraft(ModIDs.MINECRAFT) {

        {
            // instance initializer to avoid having to override isModLoaded
            checked = true;
            modLoaded = true;
        }

    },

    Aroma1997Core(ModIDs.AROMA1997_CORE),
    ExtraCells2(ModIDs.EXTRA_CELLS2),
    MCFrames(ModIDs.MC_FRAMES),
    Metallurgy(ModIDs.METALLURGY),
    QuestBook(ModIDs.QUEST_BOOK),
    RotaryCraft(ModIDs.ROTARY_CRAFT),
    TravellersGear(ModIDs.TRAVELLERS_GEAR),
    UndergroundBiomes(ModIDs.UNDERGROUND_BIOMES),

    ;

    public final String ID;
    public final String resourceDomain;
    protected boolean checked, modLoaded;

    Mods(String ID) {
        this.ID = ID;
        this.resourceDomain = ID.toLowerCase(Locale.ENGLISH);
    }

    // isModLoaded is final to allow the JIT to inline this
    @Override
    public final boolean isModLoaded() {
        if (!this.checked) {
            this.modLoaded = Loader.isModLoaded(ID);
            this.checked = true;
        }
        return this.modLoaded;
    }

    @Override
    public String getID() {
        return ID;
    }

    @Override
    public String getResourceLocation() {
        return resourceDomain;
    }

    public String getResourcePath(String path) {
        return this.getResourceLocation(path)
            .toString();
    }

    public String getResourcePath(String... path) {
        return this.getResourceLocation(path)
            .toString();
    }

    public ResourceLocation getResourceLocation(String path) {
        return new ResourceLocation(this.resourceDomain, path);
    }

    public ResourceLocation getResourceLocation(String... path) {
        return new ResourceLocation(this.resourceDomain, String.join("/", path));
    }

    public static class ModIDs {

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
        public static final String BACKHAND = "backhand";
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
        public static final String BUILD_CRAFT_CORE = "BuildCraft|Core",
            BUILD_CRAFT_BUILDERS = "BuildCraft|Builders",
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
        public static final String CROPS_NH = "cropsnh";
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
        public static final String NUCLEAR_HORIZONS = "nuclear_horizons";
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
        public static final String RANDOM_BOUBLES = "randomboubles";
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
        public static final String VENDING_MACHINE = "vendingmachine";
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

}
