package gregtech.common.items;

import static gregtech.common.items.ID_MetaTool_01.AXE;
import static gregtech.common.items.ID_MetaTool_01.BRANCHCUTTER;
import static gregtech.common.items.ID_MetaTool_01.BUTCHERYKNIFE;
import static gregtech.common.items.ID_MetaTool_01.BUZZSAW_HV;
import static gregtech.common.items.ID_MetaTool_01.BUZZSAW_LV;
import static gregtech.common.items.ID_MetaTool_01.BUZZSAW_MV;
import static gregtech.common.items.ID_MetaTool_01.CHAINSAW_HV;
import static gregtech.common.items.ID_MetaTool_01.CHAINSAW_LV;
import static gregtech.common.items.ID_MetaTool_01.CHAINSAW_MV;
import static gregtech.common.items.ID_MetaTool_01.CROWBAR;
import static gregtech.common.items.ID_MetaTool_01.DRILL_HV;
import static gregtech.common.items.ID_MetaTool_01.DRILL_LV;
import static gregtech.common.items.ID_MetaTool_01.DRILL_MV;
import static gregtech.common.items.ID_MetaTool_01.FILE;
import static gregtech.common.items.ID_MetaTool_01.HARDHAMMER;
import static gregtech.common.items.ID_MetaTool_01.HOE;
import static gregtech.common.items.ID_MetaTool_01.JACKHAMMER;
import static gregtech.common.items.ID_MetaTool_01.KNIFE;
import static gregtech.common.items.ID_MetaTool_01.MORTAR;
import static gregtech.common.items.ID_MetaTool_01.PICKAXE;
import static gregtech.common.items.ID_MetaTool_01.PLOW;
import static gregtech.common.items.ID_MetaTool_01.PLUNGER;
import static gregtech.common.items.ID_MetaTool_01.POCKET_BRANCHCUTTER;
import static gregtech.common.items.ID_MetaTool_01.POCKET_FILE;
import static gregtech.common.items.ID_MetaTool_01.POCKET_KNIFE;
import static gregtech.common.items.ID_MetaTool_01.POCKET_MULTITOOL;
import static gregtech.common.items.ID_MetaTool_01.POCKET_SAW;
import static gregtech.common.items.ID_MetaTool_01.POCKET_SCREWDRIVER;
import static gregtech.common.items.ID_MetaTool_01.POCKET_WIRECUTTER;
import static gregtech.common.items.ID_MetaTool_01.ROLLING_PIN;
import static gregtech.common.items.ID_MetaTool_01.SAW;
import static gregtech.common.items.ID_MetaTool_01.SCOOP;
import static gregtech.common.items.ID_MetaTool_01.SCREWDRIVER;
import static gregtech.common.items.ID_MetaTool_01.SCREWDRIVER_HV;
import static gregtech.common.items.ID_MetaTool_01.SCREWDRIVER_LV;
import static gregtech.common.items.ID_MetaTool_01.SCREWDRIVER_MV;
import static gregtech.common.items.ID_MetaTool_01.SENSE;
import static gregtech.common.items.ID_MetaTool_01.SHOVEL;
import static gregtech.common.items.ID_MetaTool_01.SOFTMALLET;
import static gregtech.common.items.ID_MetaTool_01.SOLDERING_IRON_HV;
import static gregtech.common.items.ID_MetaTool_01.SOLDERING_IRON_LV;
import static gregtech.common.items.ID_MetaTool_01.SOLDERING_IRON_MV;
import static gregtech.common.items.ID_MetaTool_01.SWORD;
import static gregtech.common.items.ID_MetaTool_01.TURBINE;
import static gregtech.common.items.ID_MetaTool_01.TURBINE_HUGE;
import static gregtech.common.items.ID_MetaTool_01.TURBINE_LARGE;
import static gregtech.common.items.ID_MetaTool_01.TURBINE_SMALL;
import static gregtech.common.items.ID_MetaTool_01.UNIVERSALSPADE;
import static gregtech.common.items.ID_MetaTool_01.WIRECUTTER;
import static gregtech.common.items.ID_MetaTool_01.WRENCH;
import static gregtech.common.items.ID_MetaTool_01.WRENCH_HV;
import static gregtech.common.items.ID_MetaTool_01.WRENCH_LV;
import static gregtech.common.items.ID_MetaTool_01.WRENCH_MV;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
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

    public static GT_MetaGenerated_Tool_01 INSTANCE;

    public GT_MetaGenerated_Tool_01() {
        super("metatool.01");
        INSTANCE = this;
        addTool(
            SWORD.ID,
            "Sword",
            "",
            new GT_Tool_Sword(),
            ToolDictNames.craftingToolSword,
            ToolDictNames.craftingToolBlade,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 4L));
        addTool(
            PICKAXE.ID,
            "Pickaxe",
            "",
            new GT_Tool_Pickaxe(),
            ToolDictNames.craftingToolPickaxe,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(
            SHOVEL.ID,
            "Shovel",
            "",
            new GT_Tool_Shovel(),
            ToolDictNames.craftingToolShovel,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(
            AXE.ID,
            "Axe",
            "",
            new GT_Tool_Axe(),
            ToolDictNames.craftingToolAxe,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        addTool(
            HOE.ID,
            "Hoe",
            "",
            new GT_Tool_Hoe(),
            ToolDictNames.craftingToolHoe,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.MESSIS, 4L));
        addTool(
            SAW.ID,
            "Saw",
            "Can also harvest Ice",
            new GT_Tool_Saw(),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        GregTech_API.registerTool(
            addTool(
                HARDHAMMER.ID,
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
                SOFTMALLET.ID,
                "Soft Mallet",
                "",
                new GT_Tool_SoftHammer(),
                ToolDictNames.craftingToolSoftHammer,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.LIMUS, 4L)),
            GregTech_API.sSoftHammerList);
        GregTech_API.registerTool(
            addTool(
                WRENCH.ID,
                "Wrench",
                "Hold Leftclick to dismantle Machines",
                new GT_Tool_Wrench(),
                ToolDictNames.craftingToolWrench,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sWrenchList);
        addTool(
            FILE.ID,
            "File",
            "",
            new GT_Tool_File(),
            ToolDictNames.craftingToolFile,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L));
        GregTech_API.registerTool(
            addTool(
                CROWBAR.ID,
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
                SCREWDRIVER.ID,
                "Screwdriver",
                "Adjusts Covers and Machines",
                new GT_Tool_Screwdriver(),
                ToolDictNames.craftingToolScrewdriver,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sScrewdriverList);
        addTool(
            MORTAR.ID,
            "Mortar",
            "",
            new GT_Tool_Mortar(),
            ToolDictNames.craftingToolMortar,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 2L));
        GregTech_API.registerTool(
            addTool(
                WIRECUTTER.ID,
                "Wire Cutter",
                "",
                new GT_Tool_WireCutter(),
                ToolDictNames.craftingToolWireCutter,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sWireCutterList);
        addTool(
            SCOOP.ID,
            "Scoop",
            "",
            new GT_Tool_Scoop(),
            ToolDictNames.craftingToolScoop,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.BESTIA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.PANNUS, 2L));
        addTool(
            BRANCHCUTTER.ID,
            "Branch Cutter",
            "",
            new GT_Tool_BranchCutter(),
            ToolDictNames.craftingToolBranchCutter,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.HERBA, 2L));
        GregTech_API.registerTool(
            addTool(
                UNIVERSALSPADE.ID,
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
            KNIFE.ID,
            "Knife",
            "",
            new GT_Tool_Knife(),
            ToolDictNames.craftingToolBlade,
            ToolDictNames.craftingToolKnife,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.TELUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.CORPUS, 2L));
        addTool(
            BUTCHERYKNIFE.ID,
            "Butchery Knife",
            "Has a slow Attack Rate",
            new GT_Tool_ButcheryKnife(),
            ToolDictNames.craftingToolBlade,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.CORPUS, 4L));

        addTool(
            SENSE.ID,
            "Sense",
            "Because a Scythe doesn't make Sense",
            new GT_Tool_Sense(),
            ToolDictNames.craftingToolBlade,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.HERBA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.MORTUUS, 2L));
        addTool(
            PLOW.ID,
            "Plow",
            "Used to get rid of Snow",
            new GT_Tool_Plow(),
            ToolDictNames.craftingToolPlow,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.GELUM, 2L));
        addTool(
            PLUNGER.ID,
            "Plunger",
            "",
            new GT_Tool_Plunger(),
            ToolDictNames.craftingToolPlunger,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.VACUOS, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ITER, 2L));
        addTool(
            ROLLING_PIN.ID,
            "Rolling Pin",
            "",
            new GT_Tool_RollingPin(),
            ToolDictNames.craftingToolRollingPin,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.LIMUS, 4L));

        addTool(
            DRILL_LV.ID,
            "Drill (LV)",
            "",
            new GT_Tool_Drill_LV(),
            ToolDictNames.craftingToolMiningDrill,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(
            DRILL_MV.ID,
            "Drill (MV)",
            "",
            new GT_Tool_Drill_MV(),
            ToolDictNames.craftingToolMiningDrill,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(
            DRILL_HV.ID,
            "Drill (HV)",
            "",
            new GT_Tool_Drill_HV(),
            ToolDictNames.craftingToolMiningDrill,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 4L));
        addTool(
            CHAINSAW_LV.ID,
            "Chainsaw (LV)",
            "Can also harvest Ice",
            new GT_Tool_Chainsaw_LV(),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        addTool(
            CHAINSAW_MV.ID,
            "Chainsaw (MV)",
            "Can also harvest Ice",
            new GT_Tool_Chainsaw_MV(),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        addTool(
            CHAINSAW_HV.ID,
            "Chainsaw (HV)",
            "Can also harvest Ice",
            new GT_Tool_Chainsaw_HV(),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.METO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        GregTech_API.registerTool(
            addTool(
                WRENCH_LV.ID,
                "Wrench (LV)",
                "Hold Left Button to dismantle Machines",
                new GT_Tool_Wrench_LV(),
                ToolDictNames.craftingToolWrench,
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sWrenchList);
        GregTech_API.registerTool(
            addTool(
                WRENCH_MV.ID,
                "Wrench (MV)",
                "Hold Left Button to dismantle Machines",
                new GT_Tool_Wrench_MV(),
                ToolDictNames.craftingToolWrench,
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sWrenchList);
        GregTech_API.registerTool(
            addTool(
                WRENCH_HV.ID,
                "Wrench (HV)",
                "Hold Left Button to dismantle Machines",
                new GT_Tool_Wrench_HV(),
                ToolDictNames.craftingToolWrench,
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 4L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sWrenchList);
        GregTech_API.registerTool(
            addTool(
                JACKHAMMER.ID,
                "JackHammer (HV)",
                "Breaks Rocks into pieces",
                new GT_Tool_JackHammer(),
                ToolDictNames.craftingToolJackHammer,
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERFODIO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.PERDITIO, 2L)),
            GregTech_API.sJackhammerList);
        addTool(
            BUZZSAW_LV.ID,
            "Buzzsaw (LV)",
            "Not suitable for harvesting Blocks",
            new GT_Tool_BuzzSaw(),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        addTool(
            BUZZSAW_MV.ID,
            "Buzzsaw (MV)",
            "Not suitable for harvesting Blocks",
            new GT_Tool_BuzzSaw(),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        addTool(
            BUZZSAW_HV.ID,
            "Buzzsaw (HV)",
            "Not suitable for harvesting Blocks",
            new GT_Tool_BuzzSaw(),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ARBOR, 2L));
        GregTech_API.registerTool(
            addTool(
                SCREWDRIVER_LV.ID,
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
                SCREWDRIVER_MV.ID,
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
                SCREWDRIVER_HV.ID,
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
                SOLDERING_IRON_LV.ID,
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
                SOLDERING_IRON_MV.ID,
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
                SOLDERING_IRON_HV.ID,
                "Soldering Iron (HV)",
                "Fixes burned out Circuits. Needs soldering materials in inventory.",
                new GT_Tool_Soldering_Iron(),
                ToolDictNames.craftingToolSolderingIron,
                new TC_Aspects.TC_AspectStack(TC_Aspects.MACHINA, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 2L),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 2L)),
            GregTech_API.sSolderingToolList);

        addTool(
            TURBINE_SMALL.ID,
            "Small Turbine",
            "Turbine Rotors for your power station",
            new GT_Tool_Turbine_Small());
        addTool(TURBINE.ID, "Turbine", "Turbine Rotors for your power station", new GT_Tool_Turbine_Normal());
        addTool(
            TURBINE_LARGE.ID,
            "Large Turbine",
            "Turbine Rotors for your power station",
            new GT_Tool_Turbine_Large());
        addTool(TURBINE_HUGE.ID, "Huge Turbine", "Turbine Rotors for your power station", new GT_Tool_Turbine_Huge());

        addTool(
            POCKET_MULTITOOL.ID,
            "Pocket Multitool",
            "6 useful Tools in one!",
            new GT_Tool_Pocket_Multitool(POCKET_KNIFE.ID),
            null,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 6),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 3),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3));
        addTool(
            POCKET_KNIFE.ID,
            "Pocket Multitool (Knife)",
            "",
            new GT_Tool_Pocket_Knife(POCKET_SAW.ID),
            ToolDictNames.craftingToolKnife,
            ToolDictNames.craftingToolBlade,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 6),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 3),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3));
        addTool(
            POCKET_SAW.ID,
            "Pocket Multitool (Saw)",
            "Can also harvest Ice",
            new GT_Tool_Pocket_Saw(POCKET_FILE.ID),
            ToolDictNames.craftingToolSaw,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 6),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 3),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3));
        addTool(
            POCKET_FILE.ID,
            "Pocket Multitool (File)",
            "",
            new GT_Tool_Pocket_File(POCKET_SCREWDRIVER.ID),
            ToolDictNames.craftingToolFile,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 6),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 3),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3));
        GregTech_API.registerTool(
            addTool(
                POCKET_SCREWDRIVER.ID,
                "Pocket Multitool (Screwdriver)",
                "Adjusts Covers and Machines",
                new GT_Tool_Pocket_Screwdriver(POCKET_WIRECUTTER.ID),
                ToolDictNames.craftingToolScrewdriver,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 6),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 3),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3)),
            GregTech_API.sScrewdriverList);
        GregTech_API.registerTool(
            addTool(
                POCKET_WIRECUTTER.ID,
                "Pocket Multitool (Wire Cutter)",
                "",
                new GT_Tool_Pocket_WireCutter(POCKET_BRANCHCUTTER.ID),
                ToolDictNames.craftingToolWireCutter,
                new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 6),
                new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 3),
                new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3)),
            GregTech_API.sWireCutterList);
        addTool(
            POCKET_BRANCHCUTTER.ID,
            "Pocket Multitool (Branch Cutter)",
            "",
            new GT_Tool_Pocket_BranchCutter(POCKET_MULTITOOL.ID),
            ToolDictNames.craftingToolBranchCutter,
            new TC_Aspects.TC_AspectStack(TC_Aspects.INSTRUMENTUM, 6),
            new TC_Aspects.TC_AspectStack(TC_Aspects.FABRICO, 3),
            new TC_Aspects.TC_AspectStack(TC_Aspects.ORDO, 3));
        initCraftingShapedRecipes();
        initCraftingShapelessRecipes();
    }

    private void initCraftingShapelessRecipes() {
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Items.coal, 1) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Blocks.clay, 1) });
        GT_ModHandler.addShapelessCraftingRecipe(
            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Items.wheat, 1) });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Items.flint, 1),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Blocks.gravel, 1) });
        GT_ModHandler.addShapelessCraftingRecipe(
            new ItemStack(Items.blaze_powder, 2),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Items.blaze_rod, 1) });
    }

    private void initCraftingShapedRecipes() {
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.Flint, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', new ItemStack(Items.flint, 1), 'S', OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.Bronze, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Bronze), 'S', OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.Iron, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Iron), 'S', OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.Steel, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Steel), 'S', OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.WroughtIron, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.WroughtIron), 'S',
                OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.RedSteel, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.RedSteel), 'S',
                OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.BlueSteel, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.BlueSteel), 'S',
                OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.BlackSteel, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.BlackSteel), 'S',
                OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.DamascusSteel, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.DamascusSteel), 'S',
                OrePrefixes.stone });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.Thaumium, Materials.Stone, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Thaumium), 'S',
                OrePrefixes.stone });

        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN.ID, 1, Materials.Wood, Materials.Wood, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.plank.get(Materials.Wood), 'S',
                OrePrefixes.stick.get(Materials.Wood) });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN.ID, 1, Materials.Plastic, Materials.Plastic, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.Plastic), 'S',
                OrePrefixes.stick.get(Materials.Plastic) });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN.ID, 1, Materials.Aluminium, Materials.Aluminium, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.Aluminium), 'S',
                OrePrefixes.stick.get(Materials.Aluminium) });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN.ID, 1, Materials.StainlessSteel, Materials.StainlessSteel, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.StainlessSteel), 'S',
                OrePrefixes.stick.get(Materials.StainlessSteel) });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN.ID, 1, Materials.IronWood, Materials.IronWood, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.IronWood), 'S',
                OrePrefixes.stick.get(Materials.IronWood) });

        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(SWORD.ID, 1, Materials.Flint, Materials.Wood, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "F", "F", "S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                new ItemStack(Items.flint, 1) });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(PICKAXE.ID, 1, Materials.Flint, Materials.Wood, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "FFF", " S ", " S ", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                new ItemStack(Items.flint, 1) });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(SHOVEL.ID, 1, Materials.Flint, Materials.Wood, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "F", "S", "S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                new ItemStack(Items.flint, 1) });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(AXE.ID, 1, Materials.Flint, Materials.Wood, null),
            GT_ModHandler.RecipeBits.MIRRORED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "FF", "FS", " S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                new ItemStack(Items.flint, 1) });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(HOE.ID, 1, Materials.Flint, Materials.Wood, null),
            GT_ModHandler.RecipeBits.MIRRORED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "FF", " S", " S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                new ItemStack(Items.flint, 1) });
        GT_ModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(KNIFE.ID, 1, Materials.Flint, Materials.Wood, null),
            GT_ModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "F", "S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F', new ItemStack(Items.flint, 1) });
    }
}
