package gregtech.api.enums;

import cpw.mods.fml.common.Loader;

public enum ModIDs {

    EnderIO("EnderIO"),
    TinkerConstruct("TConstruct"),
    IndustrialCraft2("IC2"),
    NotEnoughItems("NotEnoughItems"),
    Thaumcraft("Thaumcraft"),
    BartWorks("bartworks"),
    GTNHLanthanides("gtnhlanth"),
    GTPlusPlus("miscutils"),
    GalaxySpace("GalaxySpace"),
    GalacticraftMars("GalacticraftMars"),
    IronChests("IronChest"),
    NewHorizonsCoreMod("dreamcraft"),
    BuildCraftFactory("BuildCraft|Factory"),
    IronTanks("irontank"),
    ExtraUtilities("ExtraUtilities"),
    ExtraBiomesXL("ExtrabiomesXL"),
    Railcraft("Railcraft"),
    Forestry("Forestry"),
    IC2NuclearControl("IC2NuclearControl"),
    AvaritiaAddons("avaritiaddons"),
    HardcoreEnderExpansion("HardcoreEnderExpansion");

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
