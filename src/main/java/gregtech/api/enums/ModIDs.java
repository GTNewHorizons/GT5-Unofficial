package gregtech.api.enums;

import cpw.mods.fml.common.Loader;

public enum ModIDs {
    AppliedEnergistics2("appliedenergistics2"),
    AvaritiaAddons("avaritiaddons"),
    BartWorks("bartworks"),
    BuildCraftFactory("BuildCraft|Factory"),
    BuildCraftTransport("BuildCraft|Transport"),
    EnderIO("EnderIO"),
    ExtraUtilities("ExtraUtilities"),
    Forestry("Forestry"),
    GalacticraftMars("GalacticraftMars"),
    GalaxySpace("GalaxySpace"),
    GregTech("gregtech"),
    GTNHLanthanides("gtnhlanth"),
    GTPlusPlus("miscutils"),
    HardcoreEnderExpansion("HardcoreEnderExpansion"),
    IC2NuclearControl("IC2NuclearControl"),
    IndustrialCraft2("IC2"),
    IronChests("IronChest"),
    IronTanks("irontank"),
    Minecraft("minecraft"),
    NewHorizonsCoreMod("dreamcraft"),
    NotEnoughItems("NotEnoughItems"),
    Railcraft("Railcraft"),
    Thaumcraft("Thaumcraft"),
    TinkerConstruct("TConstruct");

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
