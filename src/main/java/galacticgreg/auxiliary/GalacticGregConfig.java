package galacticgreg.auxiliary;

import java.io.File;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;
import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;

import cpw.mods.fml.common.registry.GameRegistry;
import galacticgreg.GalacticGreg;

public class GalacticGregConfig extends ConfigManager {

    public GalacticGregConfig(File pConfigBaseDirectory, String pModCollectionDirectory, String pModID) {
        super(pConfigBaseDirectory, pModCollectionDirectory, pModID);

    }

    public boolean ProfileOreGen;
    public boolean ReportOreGenFailures;
    public boolean PrintDebugMessagesToFMLLog;
    public boolean PrintTraceMessagesToFMLLog;

    public boolean LootChestsEnabled;
    public boolean EnableAEExportCommand;
    public boolean SchematicsEnabled;
    public String LootChestItemOverride;
    public boolean QuietMode;

    public ImmutableBlockMeta CustomLootChest;

    @Override
    protected void PreInit() {
        ProfileOreGen = false;
        ReportOreGenFailures = false;
        PrintDebugMessagesToFMLLog = false;
        PrintTraceMessagesToFMLLog = false;

        LootChestsEnabled = true;

        // Default false, as it is WiP
        EnableAEExportCommand = false;
        SchematicsEnabled = false;

        LootChestItemOverride = "";
        QuietMode = false;
    }

    @Override
    protected void Init() {
        ProfileOreGen = _mainConfig.getBoolean(
            "ProfileOreGen",
            "Debug",
            ProfileOreGen,
            "Enable to profile oregen and register the ingame command ggregprofiler");
        ReportOreGenFailures = _mainConfig.getBoolean(
            "ReportOreGenFailures",
            "Debug",
            ReportOreGenFailures,
            "Report if a ore tileentity could not be placed");
        PrintDebugMessagesToFMLLog = _mainConfig.getBoolean(
            "PrintDebugMessagesToFMLLog",
            "Debug",
            PrintDebugMessagesToFMLLog,
            "Enable debug output, not recommended for servers");
        PrintTraceMessagesToFMLLog = _mainConfig.getBoolean(
            "PrintTraceMessagesToFMLLog",
            "Debug",
            PrintTraceMessagesToFMLLog,
            "Enable trace output. Warning: This will produce gazillions of log entries");
        QuietMode = _mainConfig.getBoolean(
            "QuietMode",
            "Debug",
            QuietMode,
            "In quiet-mode only errors, warnings and fatals will be printed to the logfile/console");

        LootChestsEnabled = _mainConfig.getBoolean(
            "LootChestsEnabled",
            "Extras",
            LootChestsEnabled,
            "Enables/disables the dungeon-chest generator system for asteroids. New config values will be generated if set to true");
        EnableAEExportCommand = _mainConfig.getBoolean(
            "EnableAEExportCommand",
            "Extras",
            EnableAEExportCommand,
            "If set to true, you can export any structure stored on a AE2 spatial storage disk. (Can't be spawned yet, WiP). Requires SchematicsEnabled to be true");
        SchematicsEnabled = _mainConfig.getBoolean(
            "SchematicsEnabled",
            "Extras",
            SchematicsEnabled,
            "Enable the experimental Schematics-handler to spawn exported schematics in dimensions. This is WiP, use at own risk");
        LootChestItemOverride = _mainConfig.getString(
            "CustomLootChest",
            "Extras",
            LootChestItemOverride,
            "Define the chest you wish to use as LootChest. use the <ModID>:<Name>:<meta> format or leave empty for the default Minecraft Chest");

        GalacticGreg.Logger.setDebugOutput(PrintDebugMessagesToFMLLog);
        GalacticGreg.Logger.setTraceOutput(PrintTraceMessagesToFMLLog);
        GalacticGreg.Logger.setQuietMode(QuietMode);
    }

    @Override
    protected void PostInit() {

    }

    public boolean serverPostInit() {
        CustomLootChest = new BlockMeta(Blocks.chest);
        try {
            if (LootChestItemOverride != null && !LootChestItemOverride.isEmpty()) {
                String[] args = LootChestItemOverride.split(":");
                String tMod;
                String tName;
                int tMeta;

                if (args.length >= 2) {
                    tMod = args[0];
                    tName = args[1];
                    if (args.length == 3) tMeta = Integer.parseInt(args[2]);
                    else tMeta = 0;

                    Block tBlock = GameRegistry.findBlock(tMod, tName);
                    if (tBlock != null) {
                        GalacticGreg.Logger
                            .debug("Found valid ChestOverride: %s. LootChest replaced", LootChestItemOverride);
                        CustomLootChest = new BlockMeta(tBlock, tMeta);
                    }
                }
            }

            return true;
        } catch (Exception e) {
            GalacticGreg.Logger.error(
                "Unable to find custom chest override %s. Make sure item exists. Defaulting to Minecraft:chest",
                LootChestItemOverride);
            e.printStackTrace();
            return false;
        }
    }
}
