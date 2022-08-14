package kubatech.api;

import cpw.mods.fml.common.Loader;

public class LoaderReference {
    public static final boolean BloodMagic = Loader.isModLoaded("AWWayofTime");
    public static final boolean EnderIO = Loader.isModLoaded("EnderIO");
    public static final boolean ExtraUtilities = Loader.isModLoaded("ExtraUtilities");
    public static final boolean InfernalMobs = Loader.isModLoaded("InfernalMobs");
    public static final boolean Thaumcraft = Loader.isModLoaded("Thaumcraft");
    public static final boolean MineTweaker = Loader.isModLoaded("MineTweaker3");
}
