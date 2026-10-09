package gregtech;

import cpw.mods.fml.common.Mod;

// Mod class to provide a mod id in the GTNH GT that makes resolving mod id collisions with GT6 easier
// Use this for Loader.isModLoaded and @Optional when you know the feature it is gating is GT5/NH specific
// Anything that uses basic oredict (e.g. plateThaumium) or the gregtech.api.interfaces.tileentity APIs
// (IBasicEnergyContainer, IColoredTileEntity, IEnergyConnected, IGregTechTileEntity, IHasWorldObjectAndCoords)
// should NOT use gregtech_nh, and instead just use a "gregtech" check, as these are common. (so EU is ok)
// To check if GT6 is present, do NOT check gregtech && !gregtech_nh, check for the modid "gregapi"
@SuppressWarnings("unused")
@Mod(modid = "gregtech_nh", name = "GregTech GTNH", version = GT_Version.VERSION)
public class GTNHMod {
}
