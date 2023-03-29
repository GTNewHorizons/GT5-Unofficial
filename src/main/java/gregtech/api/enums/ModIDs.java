package gregtech.api.enums;

import cpw.mods.fml.common.Loader;

public enum ModIDs {
    AdvancedSolarPanel("AdvancedSolarPanel"),
    AE2FluidCraft("ae2fc"),
    AppleCore("AppleCore"),
    AppliedEnergistics2("appliedenergistics2"),
    Avaritia("Avaritia"),
    AvaritiaAddons("avaritiaddons"),
    BartWorks("bartworks"),
    BetterLoadingScreen("betterloadingscreen"),
    BiomesOPlanty("BiomesOPlenty"),
    BuildCraftFactory("BuildCraft|Factory"),
    BuildCraftSilicon("BuildCraft|Silicon"),
    BuildCraftTransport("BuildCraft|Transport"),
    Computronics("computronics"),
    CraftTweaker("MineTweaker3"),
    EnderIO("EnderIO"),
    EnderStorage("EnderStorage"),
    EternalSingularity("eternalsingularity"),
    ExtraCells2("extracells"),
    ExtraUtilities("ExtraUtilities"),
    ForbiddenMagic("ForbiddenMagic"),
    Forestry("Forestry"),
    GalacticraftCore("GalacticraftCore"),
    GalacticraftMars("GalacticraftMars"),
    GalaxySpace("GalaxySpace"),
    Gendustry("gendustry"),
    GoodGenerator("GoodGenerator"),
    GregTech("gregtech"),
    GraviSuite("GraviSuite"),
    GTNHLanthanides("gtnhlanth"),
    GTPlusPlus("miscutils"),
    PamsHarvestCraft("harvestcraft"),
    HardcoreEnderExpansion("HardcoreEnderExpansion"),
    HodgePodge("hodgepodge"),
    IC2CropPlugin("Ic2Nei"),
    IC2NuclearControl("IC2NuclearControl"),
    IguanaTweaksTinkerConstruct("IguanaTweaksTConstruct"),
    IndustrialCraft2("IC2"),
    IronChests("IronChest"),
    IronTanks("irontank"),
    Minecraft("minecraft"),
    Natura("Natura"),
    NEICustomDiagrams("neicustomdiagram"),
    NewHorizonsCoreMod("dreamcraft"),
    NotEnoughItems("NotEnoughItems"),
    OpenComputers("OpenComputers"),
    ProjectRedCore("ProjRed|Core"),
    Railcraft("Railcraft"),
    TaintedMagic("TaintedMagic"),
    Thaumcraft("Thaumcraft"),
    ThaumicBases("thaumicbases"),
    ThaumicTinkerer("ThaumicTinkerer"),
    TinkerConstruct("TConstruct"),
    TinkersGregworks("TGregworks"),
    Translocator("Translocator"),
    TwilightForest("TwilightForest"),
    Waila("Waila"),
    //Do we keep compat of those?
    IndustrialCraft2Classic("IC2-Classic-Spmod"),
    Metallurgy("Metallurgy"),
    RotaryCraft("RotaryCraft"),
    ThermalExpansion("ThermalExpansion"),
    ThermalFondation("ThermalFoundation"),
    UndergroundBiomes("UndergroundBiomes");

    public final String modID;
    private Boolean modLoaded;

    ModIDs(String modID) {
        this.modID = modID;
    }

    public boolean isModLoaded() {
        if (this.modLoaded == null) {
            this.modLoaded = Loader.isModLoaded(modID);
        }
        return this.modLoaded;
    }
}
