package gregtech.common.items;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.items.GT_MetaGenerated_Tool;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.common.tools.GT_Tool_Axe;
import gregtech.common.tools.GT_Tool_BranchCutter;
import gregtech.common.tools.GT_Tool_ButcheryKnife;
import gregtech.common.tools.GT_Tool_BuzzSaw;
import gregtech.common.tools.GT_Tool_Chainsaw_HV;
import gregtech.common.tools.GT_Tool_Chainsaw_LV;
import gregtech.common.tools.GT_Tool_Chainsaw_MV;
import gregtech.common.tools.GT_Tool_Crowbar;
import gregtech.common.tools.GT_Tool_Drill_HV;
import gregtech.common.tools.GT_Tool_Drill_LV;
import gregtech.common.tools.GT_Tool_Drill_MV;
import gregtech.common.tools.GT_Tool_File;
import gregtech.common.tools.GT_Tool_HardHammer;
import gregtech.common.tools.GT_Tool_Hoe;
import gregtech.common.tools.GT_Tool_JackHammer;
import gregtech.common.tools.GT_Tool_Knife;
import gregtech.common.tools.GT_Tool_Mortar;
import gregtech.common.tools.GT_Tool_Pickaxe;
import gregtech.common.tools.GT_Tool_Plow;
import gregtech.common.tools.GT_Tool_Plunger;
import gregtech.common.tools.GT_Tool_RollingPin;
import gregtech.common.tools.GT_Tool_Saw;
import gregtech.common.tools.GT_Tool_Scoop;
import gregtech.common.tools.GT_Tool_Screwdriver;
import gregtech.common.tools.GT_Tool_Screwdriver_LV;
import gregtech.common.tools.GT_Tool_Sense;
import gregtech.common.tools.GT_Tool_Shovel;
import gregtech.common.tools.GT_Tool_SoftHammer;
import gregtech.common.tools.GT_Tool_Soldering_Iron;
import gregtech.common.tools.GT_Tool_Sword;
import gregtech.common.tools.GT_Tool_Turbine_Huge;
import gregtech.common.tools.GT_Tool_Turbine_Large;
import gregtech.common.tools.GT_Tool_Turbine_Normal;
import gregtech.common.tools.GT_Tool_Turbine_Small;
import gregtech.common.tools.GT_Tool_UniversalSpade;
import gregtech.common.tools.GT_Tool_WireCutter;
import gregtech.common.tools.GT_Tool_Wrench;
import gregtech.common.tools.GT_Tool_Wrench_HV;
import gregtech.common.tools.GT_Tool_Wrench_LV;
import gregtech.common.tools.GT_Tool_Wrench_MV;
import gregtech.common.tools.pocket.GT_Tool_Pocket_BranchCutter;
import gregtech.common.tools.pocket.GT_Tool_Pocket_File;
import gregtech.common.tools.pocket.GT_Tool_Pocket_Knife;
import gregtech.common.tools.pocket.GT_Tool_Pocket_Multitool;
import gregtech.common.tools.pocket.GT_Tool_Pocket_Saw;
import gregtech.common.tools.pocket.GT_Tool_Pocket_Screwdriver;
import gregtech.common.tools.pocket.GT_Tool_Pocket_WireCutter;

public class GT_MetaGenerated_Tool_01 extends GT_MetaGenerated_Tool {

    public static final short SWORD = 0;
    public static final short PICKAXE = 2;
    public static final short SHOVEL = 4;
    public static final short AXE = 6;
    public static final short HOE = 8;
    public static final short SAW = 10;
    public static final short HARDHAMMER = 12;
    public static final short SOFTMALLET = 14;

    @Deprecated
    public static final short SOFTHAMMER = SOFTMALLET;

    public static final short WRENCH = 16;
    public static final short FILE = 18;
    public static final short CROWBAR = 20;
    public static final short SCREWDRIVER = 22;
    public static final short MORTAR = 24;
    public static final short WIRECUTTER = 26;
    public static final short SCOOP = 28;
    public static final short BRANCHCUTTER = 30;
    public static final short UNIVERSALSPADE = 32;
    public static final short KNIFE = 34;
    public static final short BUTCHERYKNIFE = 36;

    @Deprecated
    public static final short SICKLE = 38;

    public static final short SENSE = 40;
    public static final short PLOW = 42;
    public static final short PLUNGER = 44;
    public static final short ROLLING_PIN = 46;
    public static final short DRILL_LV = 100;
    public static final short DRILL_MV = 102;
    public static final short DRILL_HV = 104;
    public static final short CHAINSAW_LV = 110;
    public static final short CHAINSAW_MV = 112;
    public static final short CHAINSAW_HV = 114;
    public static final short WRENCH_LV = 120;
    public static final short WRENCH_MV = 122;
    public static final short WRENCH_HV = 124;
    public static final short JACKHAMMER = 130;
    public static final short BUZZSAW_LV = 140;
    public static final short BUZZSAW_MV = 142;
    public static final short BUZZSAW_HV = 144;

    @Deprecated
    public static final short BUZZSAW = BUZZSAW_LV;

    public static final short SCREWDRIVER_LV = 150;
    public static final short SCREWDRIVER_MV = 152;
    public static final short SCREWDRIVER_HV = 154;
    public static final short SOLDERING_IRON_LV = 160;
    public static final short SOLDERING_IRON_MV = 162;
    public static final short SOLDERING_IRON_HV = 164;
    public static final short TURBINE_SMALL = 170;
    public static final short TURBINE = 172;
    public static final short TURBINE_LARGE = 174;
    public static final short TURBINE_HUGE = 176;

    @Deprecated
    public static final short TURBINE_BLADE = 178;

    public static final short POCKET_MULTITOOL = 180;
    public static final short POCKET_BRANCHCUTTER = 182;
    public static final short POCKET_FILE = 184;
    public static final short POCKET_KNIFE = 186;
    public static final short POCKET_SAW = 188;
    public static final short POCKET_SCREWDRIVER = 190;
    public static final short POCKET_WIRECUTTER = 192;

    public static GT_MetaGenerated_Tool_01 INSTANCE;

    public GT_MetaGenerated_Tool_01() {
        super("metatool.01");
        INSTANCE = this;
        addTool(
            SWORD,
            "Sword",
            "",
            new GT_Tool_Sword(),
            ToolDictNames.craftingToolSword,
            ToolDictNames.craftingToolBlade,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 4L));
        addTool(
            PICKAXE,
            "Pickaxe",
            "",
            new GT_Tool_Pickaxe(),
            ToolDictNames.craftingToolPickaxe,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(
            SHOVEL,
            "Shovel",
            "",
            new GT_Tool_Shovel(),
            ToolDictNames.craftingToolShovel,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(
            AXE,
            "Axe",
            "",
            new GT_Tool_Axe(),
            ToolDictNames.craftingToolAxe,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        addTool(
            HOE,
            "Hoe",
            "",
            new GT_Tool_Hoe(),
            ToolDictNames.craftingToolHoe,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.MESSIS, 4L));
        addTool(
            SAW,
            "Saw",
            "Can also harvest Ice",
            new GT_Tool_Saw(),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        GregTech_API.registerTool(
            addTool(
                HARDHAMMER,
                "Hammer",
                "Crushes Ores instead of harvesting them",
                new GT_Tool_HardHammer(),
                ToolDictNames.craftingToolHardHammer,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sHardHammerList);
        GregTech_API.registerTool(
            addTool(
                SOFTMALLET,
                "Soft Mallet",
                "",
                new GT_Tool_SoftHammer(),
                ToolDictNames.craftingToolSoftHammer,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LIMUS, 4L)),
            GregTech_API.sSoftHammerList);
        GregTech_API.registerTool(
            addTool(
                WRENCH,
                "Wrench",
                "Hold Leftclick to dismantle Machines",
                new GT_Tool_Wrench(),
                ToolDictNames.craftingToolWrench,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sWrenchList);
        addTool(
            FILE,
            "File",
            "",
            new GT_Tool_File(),
            ToolDictNames.craftingToolFile,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L));
        GregTech_API.registerTool(
            addTool(
                CROWBAR,
                "Crowbar",
                "Dismounts Covers and Rotates Rails",
                new GT_Tool_Crowbar(),
                ToolDictNames.craftingToolCrowbar,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 2L)),
            GregTech_API.sCrowbarList);
        GregTech_API.registerTool(
            addTool(
                SCREWDRIVER,
                "Screwdriver",
                "Adjusts Covers and Machines",
                new GT_Tool_Screwdriver(),
                ToolDictNames.craftingToolScrewdriver,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sScrewdriverList);
        addTool(
            MORTAR,
            "Mortar",
            "",
            new GT_Tool_Mortar(),
            ToolDictNames.craftingToolMortar,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 2L));
        GregTech_API.registerTool(
            addTool(
                WIRECUTTER,
                "Wire Cutter",
                "",
                new GT_Tool_WireCutter(),
                ToolDictNames.craftingToolWireCutter,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sWireCutterList);
        addTool(
            SCOOP,
            "Scoop",
            "",
            new GT_Tool_Scoop(),
            ToolDictNames.craftingToolScoop,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.BESTIA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.PANNUS, 2L));
        addTool(
            BRANCHCUTTER,
            "Branch Cutter",
            "",
            new GT_Tool_BranchCutter(),
            ToolDictNames.craftingToolBranchCutter,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.HERBA, 2L));
        GregTech_API.registerTool(
            addTool(
                UNIVERSALSPADE,
                "Universal Spade",
                "",
                new GT_Tool_UniversalSpade(),
                ToolDictNames.craftingToolBlade,
                ToolDictNames.craftingToolShovel,
                ToolDictNames.craftingToolCrowbar,
                ToolDictNames.craftingToolSaw,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 1L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 1L)),
            GregTech_API.sCrowbarList);
        addTool(
            KNIFE,
            "Knife",
            "",
            new GT_Tool_Knife(),
            ToolDictNames.craftingToolBlade,
            ToolDictNames.craftingToolKnife,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.CORPUS, 2L));
        addTool(
            BUTCHERYKNIFE,
            "Butchery Knife",
            "Has a slow Attack Rate",
            new GT_Tool_ButcheryKnife(),
            ToolDictNames.craftingToolBlade,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.CORPUS, 4L));

        addTool(
            SENSE,
            "Sense",
            "Because a Scythe doesn't make Sense",
            new GT_Tool_Sense(),
            ToolDictNames.craftingToolBlade,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.HERBA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.MORTUUS, 2L));
        addTool(
            PLOW,
            "Plow",
            "Used to get rid of Snow",
            new GT_Tool_Plow(),
            ToolDictNames.craftingToolPlow,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.GELUM, 2L));
        addTool(
            PLUNGER,
            "Plunger",
            "",
            new GT_Tool_Plunger(),
            ToolDictNames.craftingToolPlunger,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 2L));
        addTool(
            ROLLING_PIN,
            "Rolling Pin",
            "",
            new GT_Tool_RollingPin(),
            ToolDictNames.craftingToolRollingPin,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.LIMUS, 4L));

        addTool(
            DRILL_LV,
            "Drill (LV)",
            "",
            new GT_Tool_Drill_LV(),
            ToolDictNames.craftingToolMiningDrill,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(
            DRILL_MV,
            "Drill (MV)",
            "",
            new GT_Tool_Drill_MV(),
            ToolDictNames.craftingToolMiningDrill,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(
            DRILL_HV,
            "Drill (HV)",
            "",
            new GT_Tool_Drill_HV(),
            ToolDictNames.craftingToolMiningDrill,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(
            CHAINSAW_LV,
            "Chainsaw (LV)",
            "Can also harvest Ice",
            new GT_Tool_Chainsaw_LV(),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        addTool(
            CHAINSAW_MV,
            "Chainsaw (MV)",
            "Can also harvest Ice",
            new GT_Tool_Chainsaw_MV(),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        addTool(
            CHAINSAW_HV,
            "Chainsaw (HV)",
            "Can also harvest Ice",
            new GT_Tool_Chainsaw_HV(),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        GregTech_API.registerTool(
            addTool(
                WRENCH_LV,
                "Wrench (LV)",
                "Hold Left Button to dismantle Machines",
                new GT_Tool_Wrench_LV(),
                ToolDictNames.craftingToolWrench,
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sWrenchList);
        GregTech_API.registerTool(
            addTool(
                WRENCH_MV,
                "Wrench (MV)",
                "Hold Left Button to dismantle Machines",
                new GT_Tool_Wrench_MV(),
                ToolDictNames.craftingToolWrench,
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sWrenchList);
        GregTech_API.registerTool(
            addTool(
                WRENCH_HV,
                "Wrench (HV)",
                "Hold Left Button to dismantle Machines",
                new GT_Tool_Wrench_HV(),
                ToolDictNames.craftingToolWrench,
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sWrenchList);
        GregTech_API.registerTool(
            addTool(
                JACKHAMMER,
                "JackHammer (HV)",
                "Breaks Rocks into pieces",
                new GT_Tool_JackHammer(),
                ToolDictNames.craftingToolJackHammer,
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 2L)),
            GregTech_API.sJackhammerList);
        addTool(
            BUZZSAW_LV,
            "Buzzsaw (LV)",
            "Not suitable for harvesting Blocks",
            new GT_Tool_BuzzSaw(),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        addTool(
            BUZZSAW_MV,
            "Buzzsaw (MV)",
            "Not suitable for harvesting Blocks",
            new GT_Tool_BuzzSaw(),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        addTool(
            BUZZSAW_HV,
            "Buzzsaw (HV)",
            "Not suitable for harvesting Blocks",
            new GT_Tool_BuzzSaw(),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        GregTech_API.registerTool(
            addTool(
                SCREWDRIVER_LV,
                "Screwdriver (LV)",
                "Adjusts Covers and Machines",
                new GT_Tool_Screwdriver_LV(),
                ToolDictNames.craftingToolScrewdriver,
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sScrewdriverList);
        GregTech_API.registerTool(
            addTool(
                SCREWDRIVER_MV,
                "Screwdriver (MV)",
                "Adjusts Covers and Machines",
                new GT_Tool_Screwdriver_LV(),
                ToolDictNames.craftingToolScrewdriver,
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sScrewdriverList);
        GregTech_API.registerTool(
            addTool(
                SCREWDRIVER_HV,
                "Screwdriver (HV)",
                "Adjusts Covers and Machines",
                new GT_Tool_Screwdriver_LV(),
                ToolDictNames.craftingToolScrewdriver,
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sScrewdriverList);
        GregTech_API.registerTool(
            addTool(
                SOLDERING_IRON_LV,
                "Soldering Iron (LV)",
                "Fixes burned out Circuits. Needs soldering materials in inventory.",
                new GT_Tool_Soldering_Iron(),
                ToolDictNames.craftingToolSolderingIron,
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sSolderingToolList);
        GregTech_API.registerTool(
            addTool(
                SOLDERING_IRON_MV,
                "Soldering Iron (MV)",
                "Fixes burned out Circuits. Needs soldering materials in inventory.",
                new GT_Tool_Soldering_Iron(),
                ToolDictNames.craftingToolSolderingIron,
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sSolderingToolList);
        GregTech_API.registerTool(
            addTool(
                SOLDERING_IRON_HV,
                "Soldering Iron (HV)",
                "Fixes burned out Circuits. Needs soldering materials in inventory.",
                new GT_Tool_Soldering_Iron(),
                ToolDictNames.craftingToolSolderingIron,
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sSolderingToolList);

        addTool(TURBINE_SMALL, "Small Turbine", "Turbine Rotors for your power station", new GT_Tool_Turbine_Small());
        addTool(TURBINE, "Turbine", "Turbine Rotors for your power station", new GT_Tool_Turbine_Normal());
        addTool(TURBINE_LARGE, "Large Turbine", "Turbine Rotors for your power station", new GT_Tool_Turbine_Large());
        addTool(TURBINE_HUGE, "Huge Turbine", "Turbine Rotors for your power station", new GT_Tool_Turbine_Huge());

        addTool(
            POCKET_MULTITOOL,
            "Pocket Multitool",
            "6 useful Tools in one!",
            new GT_Tool_Pocket_Multitool(POCKET_KNIFE),
            null,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 6),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 3),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3));
        addTool(
            POCKET_KNIFE,
            "Pocket Multitool (Knife)",
            "",
            new GT_Tool_Pocket_Knife(POCKET_SAW),
            ToolDictNames.craftingToolKnife,
            ToolDictNames.craftingToolBlade,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 6),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 3),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3));
        addTool(
            POCKET_SAW,
            "Pocket Multitool (Saw)",
            "Can also harvest Ice",
            new GT_Tool_Pocket_Saw(POCKET_FILE),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 6),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 3),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3));
        addTool(
            POCKET_FILE,
            "Pocket Multitool (File)",
            "",
            new GT_Tool_Pocket_File(POCKET_SCREWDRIVER),
            ToolDictNames.craftingToolFile,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 6),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 3),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3));
        GregTech_API.registerTool(
            addTool(
                POCKET_SCREWDRIVER,
                "Pocket Multitool (Screwdriver)",
                "Adjusts Covers and Machines",
                new GT_Tool_Pocket_Screwdriver(POCKET_WIRECUTTER),
                ToolDictNames.craftingToolScrewdriver,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 6),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 3),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3)),
            GregTech_API.sScrewdriverList);
        GregTech_API.registerTool(
            addTool(
                POCKET_WIRECUTTER,
                "Pocket Multitool (Wire Cutter)",
                "",
                new GT_Tool_Pocket_WireCutter(POCKET_BRANCHCUTTER),
                ToolDictNames.craftingToolWireCutter,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 6),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 3),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3)),
            GregTech_API.sWireCutterList);
        addTool(
            POCKET_BRANCHCUTTER,
            "Pocket Multitool (Branch Cutter)",
            "",
            new GT_Tool_Pocket_BranchCutter(POCKET_MULTITOOL),
            ToolDictNames.craftingToolBranchCutter,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 6),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 3),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3));

        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR, 1, Materials.Flint, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', new ItemStack(Items.flint, 1), 'S', OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR, 1, Materials.Bronze, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Bronze), 'S', OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR, 1, Materials.Iron, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Iron), 'S', OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR, 1, Materials.Steel, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Steel), 'S', OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR, 1, Materials.WroughtIron, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.WroughtIron), 'S',
                OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR, 1, Materials.RedSteel, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.RedSteel), 'S',
                OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR, 1, Materials.BlueSteel, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.BlueSteel), 'S',
                OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR, 1, Materials.BlackSteel, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.BlackSteel), 'S',
                OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR, 1, Materials.DamascusSteel, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.DamascusSteel), 'S',
                OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR, 1, Materials.Thaumium, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Thaumium), 'S',
                OrePrefixes.stone });

        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN, 1, Materials.Wood, Materials.Wood, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.plank.get(Materials.Wood), 'S',
                OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN, 1, Materials.Plastic, Materials.Plastic, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.Plastic), 'S',
                OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN, 1, Materials.Aluminium, Materials.Aluminium, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.Aluminium), 'S',
                OrePrefixes.stick.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN, 1, Materials.StainlessSteel, Materials.StainlessSteel, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.StainlessSteel), 'S',
                OrePrefixes.stick.get(Materials.StainlessSteel) });

        if (!GregTech_API.sSpecialFile.get(ConfigCategories.general, "DisableFlintTools", false)) {
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(SWORD, 1, Materials.Flint, Materials.Wood, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "F", "F", "S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                    new ItemStack(Items.flint, 1) });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(PICKAXE, 1, Materials.Flint, Materials.Wood, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "FFF", " S ", " S ", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                    new ItemStack(Items.flint, 1) });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(SHOVEL, 1, Materials.Flint, Materials.Wood, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "F", "S", "S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                    new ItemStack(Items.flint, 1) });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(AXE, 1, Materials.Flint, Materials.Wood, null),
                GT_ModHandler.RecipeBits.MIRRORED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "FF", "FS", " S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                    new ItemStack(Items.flint, 1) });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(HOE, 1, Materials.Flint, Materials.Wood, null),
                GT_ModHandler.RecipeBits.MIRRORED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "FF", " S", " S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                    new ItemStack(Items.flint, 1) });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(KNIFE, 1, Materials.Flint, Materials.Wood, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "F", "S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                    new ItemStack(Items.flint, 1) });

            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(MORTAR, 1, Materials.Flint, Materials.Stone, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { " I ", "SIS", "SSS", 'I', new ItemStack(Items.flint, 1), 'S', OrePrefixes.stone });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(MORTAR, 1, Materials.Bronze, Materials.Stone, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Bronze), 'S',
                    OrePrefixes.stone });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(MORTAR, 1, Materials.Iron, Materials.Stone, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Iron), 'S',
                    OrePrefixes.stone });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(MORTAR, 1, Materials.Steel, Materials.Stone, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Steel), 'S',
                    OrePrefixes.stone });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(MORTAR, 1, Materials.WroughtIron, Materials.Stone, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.WroughtIron), 'S',
                    OrePrefixes.stone });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(MORTAR, 1, Materials.RedSteel, Materials.Stone, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.RedSteel), 'S',
                    OrePrefixes.stone });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(MORTAR, 1, Materials.BlueSteel, Materials.Stone, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.BlueSteel), 'S',
                    OrePrefixes.stone });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(MORTAR, 1, Materials.BlackSteel, Materials.Stone, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.BlackSteel), 'S',
                    OrePrefixes.stone });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(MORTAR, 1, Materials.DamascusSteel, Materials.Stone, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.DamascusSteel), 'S',
                    OrePrefixes.stone });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(MORTAR, 1, Materials.Thaumium, Materials.Stone, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Thaumium), 'S',
                    OrePrefixes.stone });

            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(ROLLING_PIN, 1, Materials.Wood, Materials.Wood, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.plank.get(Materials.Wood), 'S',
                    OrePrefixes.stick.get(Materials.Wood) });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(ROLLING_PIN, 1, Materials.Plastic, Materials.Plastic, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.Plastic), 'S',
                    OrePrefixes.stick.get(Materials.Plastic) });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(ROLLING_PIN, 1, Materials.Aluminium, Materials.Aluminium, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.Aluminium), 'S',
                    OrePrefixes.stick.get(Materials.Aluminium) });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(ROLLING_PIN, 1, Materials.StainlessSteel, Materials.StainlessSteel, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.StainlessSteel), 'S',
                    OrePrefixes.stick.get(Materials.StainlessSteel) });
            GT_ModHandler.addCraftingRecipe(
                INSTANCE.getToolWithStats(ROLLING_PIN, 1, Materials.IronWood, Materials.IronWood, null),
                GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.IronWood), 'S',
                    OrePrefixes.stick.get(Materials.IronWood) });

            if (GregTech_API.sRecipeFile.get(ConfigCategories.Tools.mortar, "Coal", true)) {
                GT_ModHandler.addShapelessCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Items.coal, 1) });
            }
            if (GregTech_API.sRecipeFile.get(ConfigCategories.Tools.mortar, "Clay", true)) {
                GT_ModHandler.addShapelessCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Blocks.clay, 1) });
            }
            if (GregTech_API.sRecipeFile.get(ConfigCategories.Tools.mortar, "Wheat", true)) {
                GT_ModHandler.addShapelessCraftingRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Items.wheat, 1) });
            }
            if (GregTech_API.sRecipeFile.get(ConfigCategories.Tools.mortar, "Flint", true)) {
                GT_ModHandler.addShapelessCraftingRecipe(
                    new ItemStack(Items.flint, 1),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Blocks.gravel, 1) });
            }
            if (GregTech_API.sRecipeFile.get(ConfigCategories.Tools.mortar, "Blaze", true)) {
                GT_ModHandler.addShapelessCraftingRecipe(
                    new ItemStack(Items.blaze_powder, 2),
                    GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                    new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Items.blaze_rod, 1) });
            }
        }
    }
}
