package gregtech.api.util.scanner;

import com.gtnewhorizon.gtnhlib.config.Config;

import gregtech.api.enums.Mods;

@Config(modid = Mods.ModIDs.GREG_TECH, category = "Scanner", configSubDirectory = "GregTech", filename = "Scanner")
public class ScannerConfig {

    @Config.Comment("If true, shows Base Info in Scanner")
    @Config.DefaultBoolean(true)
    public static boolean showBaseInfo;

    @Config.Comment("If true, shows Fluid Handler Info in Scanner")
    @Config.DefaultBoolean(true)
    public static boolean showFluidHandlerInfo;

    @Config.Comment("If true, shows Reactor Info in Scanner")
    @Config.DefaultBoolean(true)
    public static boolean showReactorInfo;

    @Config.Comment("If true, shows Alignment Info in Scanner")
    @Config.DefaultBoolean(true)
    public static boolean showAlignmentInfo;

    @Config.Comment("If true, shows Wrench Info in Scanner")
    @Config.DefaultBoolean(true)
    public static boolean showWrenchInfo;

    @Config.Comment("If true, shows IC2 Info in Scanner")
    @Config.DefaultBoolean(true)
    public static boolean showIC2Info;

    @Config.Comment("If true, shows Coverable Info in Scanner")
    @Config.DefaultBoolean(true)
    public static boolean showCoverableInfo;

    @Config.Comment("If true, shows Energy Container Info in Scanner")
    @Config.DefaultBoolean(true)
    public static boolean showEnergyContainerInfo;

    @Config.Comment("If true, shows Machine Info in Scanner")
    @Config.DefaultBoolean(true)
    public static boolean showMachineInfo;

    @Config.Comment("If true, shows Custom Info in Scanner")
    @Config.DefaultBoolean(true)
    public static boolean showCustomInfo;

    @Config.Comment("If true, shows IC2 Crop Info in Scanner")
    @Config.DefaultBoolean(true)
    public static boolean showIC2CropInfo;

    @Config.Comment("If true, shows Forestry Leaves Info in Scanner")
    @Config.DefaultBoolean(true)
    public static boolean showForestryLeavesInfo;

    @Config.Comment("If true, shows Chunk Info in Scanner")
    @Config.DefaultBoolean(true)
    public static boolean showChunkInfo;

    @Config.Comment("If true, shows Debug Info in Scanner")
    @Config.DefaultBoolean(true)
    public static boolean showDebugInfo;
}
