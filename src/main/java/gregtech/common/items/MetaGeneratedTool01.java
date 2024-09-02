package gregtech.common.items;

import static gregtech.common.items.IDMetaTool01.AXE;
import static gregtech.common.items.IDMetaTool01.BRANCHCUTTER;
import static gregtech.common.items.IDMetaTool01.BUTCHERYKNIFE;
import static gregtech.common.items.IDMetaTool01.BUZZSAW_HV;
import static gregtech.common.items.IDMetaTool01.BUZZSAW_LV;
import static gregtech.common.items.IDMetaTool01.BUZZSAW_MV;
import static gregtech.common.items.IDMetaTool01.CHAINSAW_HV;
import static gregtech.common.items.IDMetaTool01.CHAINSAW_LV;
import static gregtech.common.items.IDMetaTool01.CHAINSAW_MV;
import static gregtech.common.items.IDMetaTool01.CROWBAR;
import static gregtech.common.items.IDMetaTool01.DRILL_HV;
import static gregtech.common.items.IDMetaTool01.DRILL_LV;
import static gregtech.common.items.IDMetaTool01.DRILL_MV;
import static gregtech.common.items.IDMetaTool01.FILE;
import static gregtech.common.items.IDMetaTool01.HARDHAMMER;
import static gregtech.common.items.IDMetaTool01.HOE;
import static gregtech.common.items.IDMetaTool01.JACKHAMMER;
import static gregtech.common.items.IDMetaTool01.KNIFE;
import static gregtech.common.items.IDMetaTool01.MORTAR;
import static gregtech.common.items.IDMetaTool01.PICKAXE;
import static gregtech.common.items.IDMetaTool01.PLOW;
import static gregtech.common.items.IDMetaTool01.PLUNGER;
import static gregtech.common.items.IDMetaTool01.POCKET_BRANCHCUTTER;
import static gregtech.common.items.IDMetaTool01.POCKET_FILE;
import static gregtech.common.items.IDMetaTool01.POCKET_KNIFE;
import static gregtech.common.items.IDMetaTool01.POCKET_MULTITOOL;
import static gregtech.common.items.IDMetaTool01.POCKET_SAW;
import static gregtech.common.items.IDMetaTool01.POCKET_SCREWDRIVER;
import static gregtech.common.items.IDMetaTool01.POCKET_WIRECUTTER;
import static gregtech.common.items.IDMetaTool01.ROLLING_PIN;
import static gregtech.common.items.IDMetaTool01.SAW;
import static gregtech.common.items.IDMetaTool01.SCOOP;
import static gregtech.common.items.IDMetaTool01.SCREWDRIVER;
import static gregtech.common.items.IDMetaTool01.SCREWDRIVER_HV;
import static gregtech.common.items.IDMetaTool01.SCREWDRIVER_LV;
import static gregtech.common.items.IDMetaTool01.SCREWDRIVER_MV;
import static gregtech.common.items.IDMetaTool01.SENSE;
import static gregtech.common.items.IDMetaTool01.SHOVEL;
import static gregtech.common.items.IDMetaTool01.SOFTMALLET;
import static gregtech.common.items.IDMetaTool01.SOLDERING_IRON_HV;
import static gregtech.common.items.IDMetaTool01.SOLDERING_IRON_LV;
import static gregtech.common.items.IDMetaTool01.SOLDERING_IRON_MV;
import static gregtech.common.items.IDMetaTool01.SWORD;
import static gregtech.common.items.IDMetaTool01.TURBINE;
import static gregtech.common.items.IDMetaTool01.TURBINE_HUGE;
import static gregtech.common.items.IDMetaTool01.TURBINE_LARGE;
import static gregtech.common.items.IDMetaTool01.TURBINE_SMALL;
import static gregtech.common.items.IDMetaTool01.UNIVERSALSPADE;
import static gregtech.common.items.IDMetaTool01.WIRECUTTER;
import static gregtech.common.items.IDMetaTool01.WRENCH;
import static gregtech.common.items.IDMetaTool01.WRENCH_HV;
import static gregtech.common.items.IDMetaTool01.WRENCH_LV;
import static gregtech.common.items.IDMetaTool01.WRENCH_MV;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.tools.ToolAxe;
import gregtech.common.tools.ToolBranchCutter;
import gregtech.common.tools.ToolButcheryKnife;
import gregtech.common.tools.ToolBuzzSaw;
import gregtech.common.tools.ToolChainsawHV;
import gregtech.common.tools.ToolChainsawLV;
import gregtech.common.tools.ToolChainsawMV;
import gregtech.common.tools.ToolCrowbar;
import gregtech.common.tools.ToolDrillHV;
import gregtech.common.tools.ToolDrillLV;
import gregtech.common.tools.ToolDrillMV;
import gregtech.common.tools.ToolFile;
import gregtech.common.tools.ToolHardHammer;
import gregtech.common.tools.ToolHoe;
import gregtech.common.tools.ToolJackHammer;
import gregtech.common.tools.ToolKnife;
import gregtech.common.tools.ToolMortar;
import gregtech.common.tools.ToolPickaxe;
import gregtech.common.tools.ToolPlow;
import gregtech.common.tools.ToolPlunger;
import gregtech.common.tools.ToolRollingPin;
import gregtech.common.tools.ToolSaw;
import gregtech.common.tools.ToolScoop;
import gregtech.common.tools.ToolScrewdriver;
import gregtech.common.tools.ToolScrewdriverLV;
import gregtech.common.tools.ToolSense;
import gregtech.common.tools.ToolShovel;
import gregtech.common.tools.ToolSoftHammer;
import gregtech.common.tools.ToolSolderingIron;
import gregtech.common.tools.ToolSword;
import gregtech.common.tools.ToolTurbineHuge;
import gregtech.common.tools.ToolTurbineLarge;
import gregtech.common.tools.ToolTurbineNormal;
import gregtech.common.tools.ToolTurbineSmall;
import gregtech.common.tools.ToolUniversalSpade;
import gregtech.common.tools.ToolWireCutter;
import gregtech.common.tools.ToolWrench;
import gregtech.common.tools.ToolWrenchHV;
import gregtech.common.tools.ToolWrenchLV;
import gregtech.common.tools.ToolWrenchMV;
import gregtech.common.tools.pocket.ToolPocketBranchCutter;
import gregtech.common.tools.pocket.ToolPocketFile;
import gregtech.common.tools.pocket.ToolPocketKnife;
import gregtech.common.tools.pocket.ToolPocketMultitool;
import gregtech.common.tools.pocket.ToolPocketSaw;
import gregtech.common.tools.pocket.ToolPocketScrewdriver;
import gregtech.common.tools.pocket.ToolPocketWireCutter;

public class MetaGeneratedTool01 extends MetaGeneratedTool {

    public static MetaGeneratedTool01 INSTANCE;

    public MetaGeneratedTool01() {
        super("metatool.01");
        INSTANCE = this;
        addTool(
            SWORD.ID,
            "Sword",
            "",
            new ToolSword(),
            ToolDictNames.craftingToolSword,
            ToolDictNames.craftingToolBlade,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.TELUM, 4L));
        addTool(
            PICKAXE.ID,
            "Pickaxe",
            "",
            new ToolPickaxe(),
            ToolDictNames.craftingToolPickaxe,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            SHOVEL.ID,
            "Shovel",
            "",
            new ToolShovel(),
            ToolDictNames.craftingToolShovel,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            AXE.ID,
            "Axe",
            "",
            new ToolAxe(),
            ToolDictNames.craftingToolAxe,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
        addTool(
            HOE.ID,
            "Hoe",
            "",
            new ToolHoe(),
            ToolDictNames.craftingToolHoe,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.MESSIS, 4L));
        addTool(
            SAW.ID,
            "Saw",
            "Can also harvest Ice",
            new ToolSaw(),
            ToolDictNames.craftingToolSaw,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
        GregTechAPI.registerTool(
            addTool(
                HARDHAMMER.ID,
                "Hammer",
                "Crushes Ores instead of harvesting them",
                new ToolHardHammer(),
                ToolDictNames.craftingToolHardHammer,
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L)),
            GregTechAPI.sHardHammerList);
        GregTechAPI.registerTool(
            addTool(
                SOFTMALLET.ID,
                "Soft Mallet",
                "",
                new ToolSoftHammer(),
                ToolDictNames.craftingToolSoftHammer,
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.LIMUS, 4L)),
            GregTechAPI.sSoftHammerList);
        GregTechAPI.registerTool(
            addTool(
                WRENCH.ID,
                "Wrench",
                "Hold Leftclick to dismantle Machines",
                new ToolWrench(),
                ToolDictNames.craftingToolWrench,
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L)),
            GregTechAPI.sWrenchList);
        addTool(
            FILE.ID,
            "File",
            "",
            new ToolFile(),
            ToolDictNames.craftingToolFile,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L));
        GregTechAPI.registerTool(
            addTool(
                CROWBAR.ID,
                "Crowbar",
                "Dismounts Covers and Rotates Rails",
                new ToolCrowbar(),
                ToolDictNames.craftingToolCrowbar,
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TELUM, 2L)),
            GregTechAPI.sCrowbarList);
        GregTechAPI.registerTool(
            addTool(
                SCREWDRIVER.ID,
                "Screwdriver",
                "Adjusts Covers and Machines",
                new ToolScrewdriver(),
                ToolDictNames.craftingToolScrewdriver,
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L)),
            GregTechAPI.sScrewdriverList);
        addTool(
            MORTAR.ID,
            "Mortar",
            "",
            new ToolMortar(),
            ToolDictNames.craftingToolMortar,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L));
        GregTechAPI.registerTool(
            addTool(
                WIRECUTTER.ID,
                "Wire Cutter",
                "",
                new ToolWireCutter(),
                ToolDictNames.craftingToolWireCutter,
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L)),
            GregTechAPI.sWireCutterList);
        addTool(
            SCOOP.ID,
            "Scoop",
            "",
            new ToolScoop(),
            ToolDictNames.craftingToolScoop,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.BESTIA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.PANNUS, 2L));
        addTool(
            BRANCHCUTTER.ID,
            "Branch Cutter",
            "",
            new ToolBranchCutter(),
            ToolDictNames.craftingToolBranchCutter,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.HERBA, 2L));
        GregTechAPI.registerTool(
            addTool(
                UNIVERSALSPADE.ID,
                "Universal Spade",
                "",
                new ToolUniversalSpade(),
                ToolDictNames.craftingToolBlade,
                ToolDictNames.craftingToolShovel,
                ToolDictNames.craftingToolCrowbar,
                ToolDictNames.craftingToolSaw,
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TELUM, 1L),
                new TCAspects.TC_AspectStack(TCAspects.METO, 1L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 1L)),
            GregTechAPI.sCrowbarList);
        addTool(
            KNIFE.ID,
            "Knife",
            "",
            new ToolKnife(),
            ToolDictNames.craftingToolBlade,
            ToolDictNames.craftingToolKnife,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.TELUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.CORPUS, 2L));
        addTool(
            BUTCHERYKNIFE.ID,
            "Butchery Knife",
            "Has a slow Attack Rate",
            new ToolButcheryKnife(),
            ToolDictNames.craftingToolBlade,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.CORPUS, 4L));

        addTool(
            SENSE.ID,
            "Sense",
            "Because a Scythe doesn't make Sense",
            new ToolSense(),
            ToolDictNames.craftingToolBlade,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.HERBA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.MORTUUS, 2L));
        addTool(
            PLOW.ID,
            "Plow",
            "Used to get rid of Snow",
            new ToolPlow(),
            ToolDictNames.craftingToolPlow,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.GELUM, 2L));
        addTool(
            PLUNGER.ID,
            "Plunger",
            "",
            new ToolPlunger(),
            ToolDictNames.craftingToolPlunger,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.VACUOS, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ITER, 2L));
        addTool(
            ROLLING_PIN.ID,
            "Rolling Pin",
            "",
            new ToolRollingPin(),
            ToolDictNames.craftingToolRollingPin,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.LIMUS, 4L));

        addTool(
            DRILL_LV.ID,
            "Drill (LV)",
            "",
            new ToolDrillLV(),
            ToolDictNames.craftingToolMiningDrill,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            DRILL_MV.ID,
            "Drill (MV)",
            "",
            new ToolDrillMV(),
            ToolDictNames.craftingToolMiningDrill,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            DRILL_HV.ID,
            "Drill (HV)",
            "",
            new ToolDrillHV(),
            ToolDictNames.craftingToolMiningDrill,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            CHAINSAW_LV.ID,
            "Chainsaw (LV)",
            "Can also harvest Ice",
            new ToolChainsawLV(),
            ToolDictNames.craftingToolSaw,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
        addTool(
            CHAINSAW_MV.ID,
            "Chainsaw (MV)",
            "Can also harvest Ice",
            new ToolChainsawMV(),
            ToolDictNames.craftingToolSaw,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
        addTool(
            CHAINSAW_HV.ID,
            "Chainsaw (HV)",
            "Can also harvest Ice",
            new ToolChainsawHV(),
            ToolDictNames.craftingToolSaw,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
        GregTechAPI.registerTool(
            addTool(
                WRENCH_LV.ID,
                "Wrench (LV)",
                "Hold Left Button to dismantle Machines",
                new ToolWrenchLV(),
                ToolDictNames.craftingToolWrench,
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L)),
            GregTechAPI.sWrenchList);
        GregTechAPI.registerTool(
            addTool(
                WRENCH_MV.ID,
                "Wrench (MV)",
                "Hold Left Button to dismantle Machines",
                new ToolWrenchMV(),
                ToolDictNames.craftingToolWrench,
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L)),
            GregTechAPI.sWrenchList);
        GregTechAPI.registerTool(
            addTool(
                WRENCH_HV.ID,
                "Wrench (HV)",
                "Hold Left Button to dismantle Machines",
                new ToolWrenchHV(),
                ToolDictNames.craftingToolWrench,
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 4L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L)),
            GregTechAPI.sWrenchList);
        GregTechAPI.registerTool(
            addTool(
                JACKHAMMER.ID,
                "JackHammer (HV)",
                "Breaks Rocks into pieces",
                new ToolJackHammer(),
                ToolDictNames.craftingToolJackHammer,
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L)),
            GregTechAPI.sJackhammerList);
        addTool(
            BUZZSAW_LV.ID,
            "Buzzsaw (LV)",
            "Not suitable for harvesting Blocks",
            new ToolBuzzSaw(),
            ToolDictNames.craftingToolSaw,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
        addTool(
            BUZZSAW_MV.ID,
            "Buzzsaw (MV)",
            "Not suitable for harvesting Blocks",
            new ToolBuzzSaw(),
            ToolDictNames.craftingToolSaw,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
        addTool(
            BUZZSAW_HV.ID,
            "Buzzsaw (HV)",
            "Not suitable for harvesting Blocks",
            new ToolBuzzSaw(),
            ToolDictNames.craftingToolSaw,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
        GregTechAPI.registerTool(
            addTool(
                SCREWDRIVER_LV.ID,
                "Screwdriver (LV)",
                "Adjusts Covers and Machines",
                new ToolScrewdriverLV(),
                ToolDictNames.craftingToolScrewdriver,
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L)),
            GregTechAPI.sScrewdriverList);
        GregTechAPI.registerTool(
            addTool(
                SCREWDRIVER_MV.ID,
                "Screwdriver (MV)",
                "Adjusts Covers and Machines",
                new ToolScrewdriverLV(),
                ToolDictNames.craftingToolScrewdriver,
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L)),
            GregTechAPI.sScrewdriverList);
        GregTechAPI.registerTool(
            addTool(
                SCREWDRIVER_HV.ID,
                "Screwdriver (HV)",
                "Adjusts Covers and Machines",
                new ToolScrewdriverLV(),
                ToolDictNames.craftingToolScrewdriver,
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L)),
            GregTechAPI.sScrewdriverList);
        GregTechAPI.registerTool(
            addTool(
                SOLDERING_IRON_LV.ID,
                "Soldering Iron (LV)",
                "Fixes burned out Circuits. Needs soldering materials in inventory.",
                new ToolSolderingIron(),
                ToolDictNames.craftingToolSolderingIron,
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L)),
            GregTechAPI.sSolderingToolList);
        GregTechAPI.registerTool(
            addTool(
                SOLDERING_IRON_MV.ID,
                "Soldering Iron (MV)",
                "Fixes burned out Circuits. Needs soldering materials in inventory.",
                new ToolSolderingIron(),
                ToolDictNames.craftingToolSolderingIron,
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L)),
            GregTechAPI.sSolderingToolList);
        GregTechAPI.registerTool(
            addTool(
                SOLDERING_IRON_HV.ID,
                "Soldering Iron (HV)",
                "Fixes burned out Circuits. Needs soldering materials in inventory.",
                new ToolSolderingIron(),
                ToolDictNames.craftingToolSolderingIron,
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L)),
            GregTechAPI.sSolderingToolList);

        addTool(TURBINE_SMALL.ID, "Small Turbine", "Turbine Rotors for your power station", new ToolTurbineSmall());
        addTool(TURBINE.ID, "Turbine", "Turbine Rotors for your power station", new ToolTurbineNormal());
        addTool(TURBINE_LARGE.ID, "Large Turbine", "Turbine Rotors for your power station", new ToolTurbineLarge());
        addTool(TURBINE_HUGE.ID, "Huge Turbine", "Turbine Rotors for your power station", new ToolTurbineHuge());

        addTool(
            POCKET_MULTITOOL.ID,
            "Pocket Multitool",
            "6 useful Tools in one!",
            new ToolPocketMultitool(POCKET_KNIFE.ID),
            null,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 6),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 3),
            new TCAspects.TC_AspectStack(TCAspects.ORDO, 3));
        addTool(
            POCKET_KNIFE.ID,
            "Pocket Multitool (Knife)",
            "",
            new ToolPocketKnife(POCKET_SAW.ID),
            ToolDictNames.craftingToolKnife,
            ToolDictNames.craftingToolBlade,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 6),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 3),
            new TCAspects.TC_AspectStack(TCAspects.ORDO, 3));
        addTool(
            POCKET_SAW.ID,
            "Pocket Multitool (Saw)",
            "Can also harvest Ice",
            new ToolPocketSaw(POCKET_FILE.ID),
            ToolDictNames.craftingToolSaw,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 6),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 3),
            new TCAspects.TC_AspectStack(TCAspects.ORDO, 3));
        addTool(
            POCKET_FILE.ID,
            "Pocket Multitool (File)",
            "",
            new ToolPocketFile(POCKET_SCREWDRIVER.ID),
            ToolDictNames.craftingToolFile,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 6),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 3),
            new TCAspects.TC_AspectStack(TCAspects.ORDO, 3));
        GregTechAPI.registerTool(
            addTool(
                POCKET_SCREWDRIVER.ID,
                "Pocket Multitool (Screwdriver)",
                "Adjusts Covers and Machines",
                new ToolPocketScrewdriver(POCKET_WIRECUTTER.ID),
                ToolDictNames.craftingToolScrewdriver,
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 6),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 3),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 3)),
            GregTechAPI.sScrewdriverList);
        GregTechAPI.registerTool(
            addTool(
                POCKET_WIRECUTTER.ID,
                "Pocket Multitool (Wire Cutter)",
                "",
                new ToolPocketWireCutter(POCKET_BRANCHCUTTER.ID),
                ToolDictNames.craftingToolWireCutter,
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 6),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 3),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 3)),
            GregTechAPI.sWireCutterList);
        addTool(
            POCKET_BRANCHCUTTER.ID,
            "Pocket Multitool (Branch Cutter)",
            "",
            new ToolPocketBranchCutter(POCKET_MULTITOOL.ID),
            ToolDictNames.craftingToolBranchCutter,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 6),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 3),
            new TCAspects.TC_AspectStack(TCAspects.ORDO, 3));
        initCraftingShapedRecipes();
        initCraftingShapelessRecipes();
    }

    private void initCraftingShapelessRecipes() {
        GTModHandler.addShapelessCraftingRecipe(
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Coal, 1L),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Items.coal, 1) });
        GTModHandler.addShapelessCraftingRecipe(
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Clay, 1L),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Blocks.clay, 1) });
        GTModHandler.addShapelessCraftingRecipe(
            GTOreDictUnificator.get(OrePrefixes.dust, Materials.Wheat, 1L),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Items.wheat, 1) });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Items.flint, 1),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Blocks.gravel, 1) });
        GTModHandler.addShapelessCraftingRecipe(
            new ItemStack(Items.blaze_powder, 2),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { ToolDictNames.craftingToolMortar, new ItemStack(Items.blaze_rod, 1) });
    }

    private void initCraftingShapedRecipes() {
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.Flint, Materials.Stone, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', new ItemStack(Items.flint, 1), 'S', OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.Bronze, Materials.Stone, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Bronze), 'S', OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.Iron, Materials.Stone, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Iron), 'S', OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.Steel, Materials.Stone, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Steel), 'S', OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.WroughtIron, Materials.Stone, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.WroughtIron), 'S',
                OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.RedSteel, Materials.Stone, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.RedSteel), 'S',
                OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.BlueSteel, Materials.Stone, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.BlueSteel), 'S',
                OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.BlackSteel, Materials.Stone, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.BlackSteel), 'S',
                OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.DamascusSteel, Materials.Stone, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.DamascusSteel), 'S',
                OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(MORTAR.ID, 1, Materials.Thaumium, Materials.Stone, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Thaumium), 'S',
                OrePrefixes.stone });

        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN.ID, 1, Materials.Wood, Materials.Wood, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.plank.get(Materials.Wood), 'S',
                OrePrefixes.stick.get(Materials.Wood) });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN.ID, 1, Materials.Plastic, Materials.Plastic, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.Plastic), 'S',
                OrePrefixes.stick.get(Materials.Plastic) });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN.ID, 1, Materials.Aluminium, Materials.Aluminium, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.Aluminium), 'S',
                OrePrefixes.stick.get(Materials.Aluminium) });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN.ID, 1, Materials.StainlessSteel, Materials.StainlessSteel, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.StainlessSteel), 'S',
                OrePrefixes.stick.get(Materials.StainlessSteel) });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN.ID, 1, Materials.IronWood, Materials.IronWood, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.IronWood), 'S',
                OrePrefixes.stick.get(Materials.IronWood) });

        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(SWORD.ID, 1, Materials.Flint, Materials.Wood, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "F", "F", "S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                new ItemStack(Items.flint, 1) });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(PICKAXE.ID, 1, Materials.Flint, Materials.Wood, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "FFF", " S ", " S ", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                new ItemStack(Items.flint, 1) });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(SHOVEL.ID, 1, Materials.Flint, Materials.Wood, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "F", "S", "S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                new ItemStack(Items.flint, 1) });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(AXE.ID, 1, Materials.Flint, Materials.Wood, null),
            GTModHandler.RecipeBits.MIRRORED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "FF", "FS", " S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                new ItemStack(Items.flint, 1) });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(HOE.ID, 1, Materials.Flint, Materials.Wood, null),
            GTModHandler.RecipeBits.MIRRORED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "FF", " S", " S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F',
                new ItemStack(Items.flint, 1) });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(KNIFE.ID, 1, Materials.Flint, Materials.Wood, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "F", "S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F', new ItemStack(Items.flint, 1) });
    }
}
