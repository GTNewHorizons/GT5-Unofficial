package gregtech.common.items.tools;

import java.util.ArrayList;
import java.util.List;

import gregtech.common.tools.ToolBranchCutter;
import gregtech.common.tools.ToolButcheryKnife;
import gregtech.common.tools.ToolBuzzSawHV;
import gregtech.common.tools.ToolBuzzSawLV;
import gregtech.common.tools.ToolBuzzSawMV;
import gregtech.common.tools.ToolChainsawHV;
import gregtech.common.tools.ToolChainsawLV;
import gregtech.common.tools.ToolChainsawMV;
import gregtech.common.tools.ToolCrowbar;
import gregtech.common.tools.ToolDrillHV;
import gregtech.common.tools.ToolDrillLV;
import gregtech.common.tools.ToolDrillMV;
import gregtech.common.tools.ToolFile;
import gregtech.common.tools.ToolFileHV;
import gregtech.common.tools.ToolFileLV;
import gregtech.common.tools.ToolFileMV;
import gregtech.common.tools.ToolHardHammer;
import gregtech.common.tools.ToolJackHammerHV;
import gregtech.common.tools.ToolJackHammerLV;
import gregtech.common.tools.ToolJackHammerMV;
import gregtech.common.tools.ToolKnife;
import gregtech.common.tools.ToolMortar;
import gregtech.common.tools.ToolPlunger;
import gregtech.common.tools.ToolRollingPin;
import gregtech.common.tools.ToolSaw;
import gregtech.common.tools.ToolScoop;
import gregtech.common.tools.ToolScrewdriver;
import gregtech.common.tools.ToolScrewdriverHV;
import gregtech.common.tools.ToolScrewdriverLV;
import gregtech.common.tools.ToolScrewdriverMV;
import gregtech.common.tools.ToolSoftMallet;
import gregtech.common.tools.ToolSolderingIron;
import gregtech.common.tools.ToolTrowel;
import gregtech.common.tools.ToolTurbineHuge;
import gregtech.common.tools.ToolTurbineLarge;
import gregtech.common.tools.ToolTurbineNormal;
import gregtech.common.tools.ToolTurbineSmall;
import gregtech.common.tools.ToolWireCutter;
import gregtech.common.tools.ToolWireCutterHV;
import gregtech.common.tools.ToolWireCutterLV;
import gregtech.common.tools.ToolWireCutterMV;
import gregtech.common.tools.ToolWrench;
import gregtech.common.tools.ToolWrenchHV;
import gregtech.common.tools.ToolWrenchLV;
import gregtech.common.tools.ToolWrenchMV;

/**
 * The standalone tool items: one registered Forge item per tool type and tier, with the crafting material encoded in
 * the metadata.
 * <p/>
 * Tool types move here from {@link gregtech.common.items.MetaGeneratedTool01} one at a time; the wrench is the pilot.
 */
public final class GTToolItems {

    public static ToolWrenchItem WRENCH;
    public static ToolWrenchElectricItem WRENCH_LV;
    public static ToolWrenchElectricItem WRENCH_MV;
    public static ToolWrenchElectricItem WRENCH_HV;
    public static ToolSoftMalletItem SOFT_MALLET;
    public static ToolScrewdriverItem SCREWDRIVER;
    public static ToolScrewdriverElectricItem SCREWDRIVER_LV;
    public static ToolScrewdriverElectricItem SCREWDRIVER_MV;
    public static ToolScrewdriverElectricItem SCREWDRIVER_HV;
    public static ToolCrowbarItem CROWBAR;
    public static ToolWireCutterItem WIRE_CUTTER;
    public static ToolWireCutterElectricItem WIRE_CUTTER_LV;
    public static ToolWireCutterElectricItem WIRE_CUTTER_MV;
    public static ToolWireCutterElectricItem WIRE_CUTTER_HV;
    public static ToolHardHammerItem HARD_HAMMER;
    public static ToolFileItem FILE;
    public static ToolFileElectricItem FILE_LV;
    public static ToolFileElectricItem FILE_MV;
    public static ToolFileElectricItem FILE_HV;
    public static ToolSawItem SAW;
    public static ToolMortarItem MORTAR;
    public static ToolScoopItem SCOOP;
    public static ToolBranchCutterItem BRANCH_CUTTER;
    public static ToolKnifeItem KNIFE;
    public static ToolButcheryKnifeItem BUTCHERY_KNIFE;
    public static ToolPlungerItem PLUNGER;
    public static ToolRollingPinItem ROLLING_PIN;
    public static ToolTrowelItem TROWEL;
    public static ToolDrillItem DRILL_LV;
    public static ToolDrillItem DRILL_MV;
    public static ToolDrillItem DRILL_HV;
    public static ToolChainsawItem CHAINSAW_LV;
    public static ToolChainsawItem CHAINSAW_MV;
    public static ToolChainsawItem CHAINSAW_HV;
    public static ToolJackHammerItem JACKHAMMER_LV;
    public static ToolJackHammerItem JACKHAMMER_MV;
    public static ToolJackHammerItem JACKHAMMER_HV;
    public static ToolBuzzSawItem BUZZSAW_LV;
    public static ToolBuzzSawItem BUZZSAW_MV;
    public static ToolBuzzSawItem BUZZSAW_HV;
    public static ToolSolderingIronItem SOLDERING_IRON_LV;
    public static ToolSolderingIronItem SOLDERING_IRON_MV;
    public static ToolSolderingIronItem SOLDERING_IRON_HV;
    public static ToolTurbineItem TURBINE_SMALL;
    public static ToolTurbineItem TURBINE_NORMAL;
    public static ToolTurbineItem TURBINE_LARGE;
    public static ToolTurbineItem TURBINE_HUGE;

    /** Every rotor size says the same thing, so it is written once. */
    private static final String TURBINE_TOOLTIP = "Turbine rotor for your power station";

    /**
     * Ore dictionary registrations that are waiting for {@link #flushOreDictRegistrations()}.
     * <p/>
     * Materials are declared from inside the ore dictionary handlers (see
     * {@link gregtech.loaders.oreprocessing.ProcessingToolOther} and friends), which run while
     * {@link gregtech.common.GTProxy#activateOreDictHandler()} is iterating its own event collection. Registering a
     * new ore from there re-enters {@link gregtech.common.GTProxy#onOreRegistration} and mutates that collection
     * mid-iteration, which throws a ConcurrentModificationException. So the registrations are collected here and
     * replayed once the handler is done.
     */
    private static final List<Runnable> PENDING_ORE_DICT_REGISTRATIONS = new ArrayList<>();

    private static boolean oreDictRegistrationsFlushed = false;

    private GTToolItems() {}

    /**
     * Must run during pre-init, alongside the other item registrations, and before any recipe loader asks for a
     * wrench stack.
     */
    public static void register() {
        WRENCH = new ToolWrenchItem(
            "tool.wrench",
            new ToolWrench(),
            "%material Wrench",
            "Hold left-click to dismantle machines");
        WRENCH_LV = new ToolWrenchElectricItem(
            "tool.wrench_lv",
            new ToolWrenchLV(),
            "%material Wrench (LV)",
            "Hold left-click to dismantle machines",
            100_000L,
            32L,
            1);
        WRENCH_MV = new ToolWrenchElectricItem(
            "tool.wrench_mv",
            new ToolWrenchMV(),
            "%material Wrench (MV)",
            "Hold left-click to dismantle machines",
            400_000L,
            128L,
            2);
        WRENCH_HV = new ToolWrenchElectricItem(
            "tool.wrench_hv",
            new ToolWrenchHV(),
            "%material Wrench (HV)",
            "Hold left-click to dismantle machines",
            1_600_000L,
            512L,
            3);
        SOFT_MALLET = new ToolSoftMalletItem("tool.soft_mallet", new ToolSoftMallet(), "%material Soft Mallet");
        SCREWDRIVER = new ToolScrewdriverItem("tool.screwdriver", new ToolScrewdriver(), "%material Screwdriver");
        SCREWDRIVER_LV = new ToolScrewdriverElectricItem(
            "tool.screwdriver_lv",
            new ToolScrewdriverLV(),
            "%material Screwdriver (LV)",
            100_000L,
            32L,
            1);
        SCREWDRIVER_MV = new ToolScrewdriverElectricItem(
            "tool.screwdriver_mv",
            new ToolScrewdriverMV(),
            "%material Screwdriver (MV)",
            400_000L,
            128L,
            2);
        SCREWDRIVER_HV = new ToolScrewdriverElectricItem(
            "tool.screwdriver_hv",
            new ToolScrewdriverHV(),
            "%material Screwdriver (HV)",
            1_600_000L,
            512L,
            3);
        CROWBAR = new ToolCrowbarItem("tool.crowbar", new ToolCrowbar(), "%material Crowbar");
        WIRE_CUTTER = new ToolWireCutterItem("tool.wire_cutter", new ToolWireCutter(), "%material Wire Cutter", "");
        WIRE_CUTTER_LV = new ToolWireCutterElectricItem(
            "tool.wire_cutter_lv",
            new ToolWireCutterLV(),
            "%material Wire Cutter (LV)",
            "Hand-held electric wire cutter",
            100_000L,
            32L,
            1);
        WIRE_CUTTER_MV = new ToolWireCutterElectricItem(
            "tool.wire_cutter_mv",
            new ToolWireCutterMV(),
            "%material Wire Cutter (MV)",
            "Hand-held electric wire cutter",
            400_000L,
            128L,
            2);
        WIRE_CUTTER_HV = new ToolWireCutterElectricItem(
            "tool.wire_cutter_hv",
            new ToolWireCutterHV(),
            "%material Wire Cutter (HV)",
            "Hand-held electric wire cutter",
            1_600_000L,
            512L,
            3);
        HARD_HAMMER = new ToolHardHammerItem("tool.hard_hammer", new ToolHardHammer(), "%material Hammer");
        FILE = new ToolFileItem("tool.file", new ToolFile(), "%material File", "");
        FILE_LV = new ToolFileElectricItem(
            "tool.file_lv",
            new ToolFileLV(),
            "%material File (LV)",
            "Hand-held electric filing device",
            100_000L,
            32L,
            1);
        FILE_MV = new ToolFileElectricItem(
            "tool.file_mv",
            new ToolFileMV(),
            "%material File (MV)",
            "Hand-held electric filing device",
            400_000L,
            128L,
            2);
        FILE_HV = new ToolFileElectricItem(
            "tool.file_hv",
            new ToolFileHV(),
            "%material File (HV)",
            "Hand-held electric filing device",
            1_600_000L,
            512L,
            3);
        SAW = new ToolSawItem("tool.saw", new ToolSaw(), "%material Saw", "Can also harvest ice");
        MORTAR = new ToolMortarItem("tool.mortar", new ToolMortar(), "%material Mortar", "");
        SCOOP = new ToolScoopItem("tool.scoop", new ToolScoop(), "%material Scoop", "");
        BRANCH_CUTTER = new ToolBranchCutterItem(
            "tool.branch_cutter",
            new ToolBranchCutter(),
            "%material Branch Cutter",
            "");
        KNIFE = new ToolKnifeItem("tool.knife", new ToolKnife(), "%material Knife", "");
        BUTCHERY_KNIFE = new ToolButcheryKnifeItem(
            "tool.butchery_knife",
            new ToolButcheryKnife(),
            "%material Butchery Knife",
            "Has a slow attack rate");
        PLUNGER = new ToolPlungerItem("tool.plunger", new ToolPlunger(), "%material Plunger", "");
        ROLLING_PIN = new ToolRollingPinItem("tool.rolling_pin", new ToolRollingPin(), "%material Rolling Pin", "");
        TROWEL = new ToolTrowelItem("tool.trowel", new ToolTrowel(), "%material Decorator's Trowel", "");
        DRILL_LV = new ToolDrillItem("tool.drill_lv", new ToolDrillLV(), "%material Drill (LV)", 100_000L, 32L, 1);
        DRILL_MV = new ToolDrillItem("tool.drill_mv", new ToolDrillMV(), "%material Drill (MV)", 400_000L, 128L, 2);
        DRILL_HV = new ToolDrillItem("tool.drill_hv", new ToolDrillHV(), "%material Drill (HV)", 1_600_000L, 512L, 3);
        CHAINSAW_LV = new ToolChainsawItem(
            "tool.chainsaw_lv",
            new ToolChainsawLV(),
            "%material Chainsaw (LV)",
            "Can also harvest ice",
            100_000L,
            32L,
            1);
        CHAINSAW_MV = new ToolChainsawItem(
            "tool.chainsaw_mv",
            new ToolChainsawMV(),
            "%material Chainsaw (MV)",
            "Can also harvest ice",
            400_000L,
            128L,
            2);
        CHAINSAW_HV = new ToolChainsawItem(
            "tool.chainsaw_hv",
            new ToolChainsawHV(),
            "%material Chainsaw (HV)",
            "Can also harvest ice",
            1_600_000L,
            512L,
            3);
        JACKHAMMER_LV = new ToolJackHammerItem(
            "tool.jackhammer_lv",
            new ToolJackHammerLV(),
            "%material Jackhammer (LV)",
            "Breaks rocks into pieces",
            100_000L,
            32L,
            1);
        JACKHAMMER_MV = new ToolJackHammerItem(
            "tool.jackhammer_mv",
            new ToolJackHammerMV(),
            "%material Jackhammer (MV)",
            "Breaks rocks into pieces",
            400_000L,
            128L,
            2);
        JACKHAMMER_HV = new ToolJackHammerItem(
            "tool.jackhammer_hv",
            new ToolJackHammerHV(),
            "%material Jackhammer (HV)",
            "Breaks rocks into pieces",
            1_600_000L,
            512L,
            3);
        BUZZSAW_LV = new ToolBuzzSawItem(
            "tool.buzzsaw_lv",
            new ToolBuzzSawLV(),
            "%material Buzzsaw (LV)",
            "Not suitable for harvesting blocks",
            100_000L,
            32L,
            1);
        BUZZSAW_MV = new ToolBuzzSawItem(
            "tool.buzzsaw_mv",
            new ToolBuzzSawMV(),
            "%material Buzzsaw (MV)",
            "Not suitable for harvesting blocks",
            400_000L,
            128L,
            2);
        BUZZSAW_HV = new ToolBuzzSawItem(
            "tool.buzzsaw_hv",
            new ToolBuzzSawHV(),
            "%material Buzzsaw (HV)",
            "Not suitable for harvesting blocks",
            1_600_000L,
            512L,
            3);
        SOLDERING_IRON_LV = new ToolSolderingIronItem(
            "tool.soldering_iron_lv",
            new ToolSolderingIron(),
            "%material Soldering Iron (LV)",
            "Fixes burned out circuits. Needs soldering material in inventory.",
            100_000L,
            32L,
            1);
        SOLDERING_IRON_MV = new ToolSolderingIronItem(
            "tool.soldering_iron_mv",
            new ToolSolderingIron(),
            "%material Soldering Iron (MV)",
            "Fixes burned out circuits. Needs soldering material in inventory.",
            400_000L,
            128L,
            2);
        SOLDERING_IRON_HV = new ToolSolderingIronItem(
            "tool.soldering_iron_hv",
            new ToolSolderingIron(),
            "%material Soldering Iron (HV)",
            "Fixes burned out circuits. Needs soldering material in inventory.",
            1_600_000L,
            512L,
            3);
        TURBINE_SMALL = new ToolTurbineItem(
            "tool.turbine_small",
            new ToolTurbineSmall(),
            "%material Small Turbine",
            TURBINE_TOOLTIP,
            1);
        TURBINE_NORMAL = new ToolTurbineItem(
            "tool.turbine_normal",
            new ToolTurbineNormal(),
            "%material Turbine",
            TURBINE_TOOLTIP,
            2);
        TURBINE_LARGE = new ToolTurbineItem(
            "tool.turbine_large",
            new ToolTurbineLarge(),
            "%material Large Turbine",
            TURBINE_TOOLTIP,
            3);
        TURBINE_HUGE = new ToolTurbineItem(
            "tool.turbine_huge",
            new ToolTurbineHuge(),
            "%material Huge Turbine",
            TURBINE_TOOLTIP,
            4);
    }

    /**
     * Runs an ore dictionary registration for a tool stack, either now or -- if the ore dictionary handler has not
     * finished yet -- once it has. See {@link #PENDING_ORE_DICT_REGISTRATIONS}.
     */
    static synchronized void registerOreDictEntry(Runnable registration) {
        if (oreDictRegistrationsFlushed) registration.run();
        else PENDING_ORE_DICT_REGISTRATIONS.add(registration);
    }

    /**
     * Registers every ore dictionary entry that was declared while the ore dictionary handler was running. Must be
     * called once, immediately after {@link gregtech.common.GTProxy#activateOreDictHandler()} returns; anything
     * declared after that point registers straight away.
     */
    public static synchronized void flushOreDictRegistrations() {
        oreDictRegistrationsFlushed = true;
        for (Runnable registration : PENDING_ORE_DICT_REGISTRATIONS) registration.run();
        PENDING_ORE_DICT_REGISTRATIONS.clear();
    }
}
