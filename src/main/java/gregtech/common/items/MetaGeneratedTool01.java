package gregtech.common.items;

import static gregtech.common.items.IDMetaTool01.BUTCHERYKNIFE;
import static gregtech.common.items.IDMetaTool01.BUZZSAW_HV;
import static gregtech.common.items.IDMetaTool01.BUZZSAW_LV;
import static gregtech.common.items.IDMetaTool01.BUZZSAW_MV;
import static gregtech.common.items.IDMetaTool01.CHAINSAW_HV;
import static gregtech.common.items.IDMetaTool01.CHAINSAW_LV;
import static gregtech.common.items.IDMetaTool01.CHAINSAW_MV;
import static gregtech.common.items.IDMetaTool01.DRILL_HV;
import static gregtech.common.items.IDMetaTool01.DRILL_LV;
import static gregtech.common.items.IDMetaTool01.DRILL_MV;
import static gregtech.common.items.IDMetaTool01.JACKHAMMER_HV;
import static gregtech.common.items.IDMetaTool01.JACKHAMMER_LV;
import static gregtech.common.items.IDMetaTool01.JACKHAMMER_MV;
import static gregtech.common.items.IDMetaTool01.PLUNGER;
import static gregtech.common.items.IDMetaTool01.POCKET_BRANCHCUTTER;
import static gregtech.common.items.IDMetaTool01.POCKET_FILE;
import static gregtech.common.items.IDMetaTool01.POCKET_KNIFE;
import static gregtech.common.items.IDMetaTool01.POCKET_MULTITOOL;
import static gregtech.common.items.IDMetaTool01.POCKET_SAW;
import static gregtech.common.items.IDMetaTool01.POCKET_SCREWDRIVER;
import static gregtech.common.items.IDMetaTool01.POCKET_WIRECUTTER;
import static gregtech.common.items.IDMetaTool01.ROLLING_PIN;
import static gregtech.common.items.IDMetaTool01.SOLDERING_IRON_HV;
import static gregtech.common.items.IDMetaTool01.SOLDERING_IRON_LV;
import static gregtech.common.items.IDMetaTool01.SOLDERING_IRON_MV;
import static gregtech.common.items.IDMetaTool01.TROWEL;
import static gregtech.common.items.IDMetaTool01.TURBINE;
import static gregtech.common.items.IDMetaTool01.TURBINE_HUGE;
import static gregtech.common.items.IDMetaTool01.TURBINE_LARGE;
import static gregtech.common.items.IDMetaTool01.TURBINE_SMALL;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.common.items.tools.GTToolItems;
import gregtech.common.tools.ItemNetworkAnalyzer;
import gregtech.common.tools.ToolButcheryKnife;
import gregtech.common.tools.ToolBuzzSawHV;
import gregtech.common.tools.ToolBuzzSawLV;
import gregtech.common.tools.ToolBuzzSawMV;
import gregtech.common.tools.ToolChainsawHV;
import gregtech.common.tools.ToolChainsawLV;
import gregtech.common.tools.ToolChainsawMV;
import gregtech.common.tools.ToolDrillHV;
import gregtech.common.tools.ToolDrillLV;
import gregtech.common.tools.ToolDrillMV;
import gregtech.common.tools.ToolJackHammerHV;
import gregtech.common.tools.ToolJackHammerLV;
import gregtech.common.tools.ToolJackHammerMV;
import gregtech.common.tools.ToolPlunger;
import gregtech.common.tools.ToolRollingPin;
import gregtech.common.tools.ToolSolderingIron;
import gregtech.common.tools.ToolTrowel;
import gregtech.common.tools.ToolTurbineHuge;
import gregtech.common.tools.ToolTurbineLarge;
import gregtech.common.tools.ToolTurbineNormal;
import gregtech.common.tools.ToolTurbineSmall;
import gregtech.common.tools.ToolVajra;
import gregtech.common.tools.pocket.ToolPocketBranchCutter;
import gregtech.common.tools.pocket.ToolPocketFile;
import gregtech.common.tools.pocket.ToolPocketKnife;
import gregtech.common.tools.pocket.ToolPocketMultitool;
import gregtech.common.tools.pocket.ToolPocketSaw;
import gregtech.common.tools.pocket.ToolPocketScrewdriver;
import gregtech.common.tools.pocket.ToolPocketWireCutter;

public class MetaGeneratedTool01 extends MetaGeneratedTool {

    public static MetaGeneratedTool01 INSTANCE;

    /** Meta Value of a charged electric tool to the name it spells, for the families that only differ by tier. */
    private static final Map<Integer, Supplier<String>> TIERED_NAMES = new HashMap<>();
    private static final Map<Integer, String> TIERED_TOOLTIPS = new HashMap<>();

    public MetaGeneratedTool01() {
        super("metatool.01");
        INSTANCE = this;
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
            "",
            "",
            new ToolDrillLV(),
            ToolDictNames.craftingToolMiningDrill,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            DRILL_MV.ID,
            "",
            "",
            new ToolDrillMV(),
            ToolDictNames.craftingToolMiningDrill,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            DRILL_HV.ID,
            "",
            "",
            new ToolDrillHV(),
            ToolDictNames.craftingToolMiningDrill,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 4L));
        addTool(
            CHAINSAW_LV.ID,
            "",
            "",
            new ToolChainsawLV(),
            ToolDictNames.craftingToolSaw,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
        addTool(
            CHAINSAW_MV.ID,
            "",
            "",
            new ToolChainsawMV(),
            ToolDictNames.craftingToolSaw,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
        addTool(
            CHAINSAW_HV.ID,
            "",
            "",
            new ToolChainsawHV(),
            ToolDictNames.craftingToolSaw,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.METO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
        GregTechAPI.registerTool(
            addTool(
                JACKHAMMER_LV.ID,
                "",
                "",
                new ToolJackHammerLV(),
                ToolDictNames.craftingToolJackHammer,
                ToolDictNames.craftingToolHardHammer,
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L)),
            GregTechAPI.sJackhammerList);
        GregTechAPI.registerTool(
            addTool(
                JACKHAMMER_MV.ID,
                "",
                "",
                new ToolJackHammerMV(),
                ToolDictNames.craftingToolJackHammer,
                ToolDictNames.craftingToolHardHammer,
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L)),
            GregTechAPI.sJackhammerList);
        GregTechAPI.registerTool(
            addTool(
                JACKHAMMER_HV.ID,
                "",
                "",
                new ToolJackHammerHV(),
                ToolDictNames.craftingToolJackHammer,
                ToolDictNames.craftingToolHardHammer,
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PERFODIO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L)),
            GregTechAPI.sJackhammerList);
        addTool(
            BUZZSAW_LV.ID,
            "",
            "",
            new ToolBuzzSawLV(),
            ToolDictNames.craftingToolSaw,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
        addTool(
            BUZZSAW_MV.ID,
            "",
            "",
            new ToolBuzzSawMV(),
            ToolDictNames.craftingToolSaw,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
        addTool(
            BUZZSAW_HV.ID,
            "",
            "",
            new ToolBuzzSawHV(),
            ToolDictNames.craftingToolSaw,
            new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.ARBOR, 2L));
        GregTechAPI.registerTool(
            addTool(
                SOLDERING_IRON_LV.ID,
                "",
                "",
                new ToolSolderingIron(),
                ToolDictNames.craftingToolSolderingIron,
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L)),
            GregTechAPI.sSolderingToolList);
        GregTechAPI.registerTool(
            addTool(
                SOLDERING_IRON_MV.ID,
                "",
                "",
                new ToolSolderingIron(),
                ToolDictNames.craftingToolSolderingIron,
                new TCAspects.TC_AspectStack(TCAspects.MACHINA, 2L),
                new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
                new TCAspects.TC_AspectStack(TCAspects.ORDO, 2L)),
            GregTechAPI.sSolderingToolList);
        GregTechAPI.registerTool(
            addTool(
                SOLDERING_IRON_HV.ID,
                "",
                "",
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
        addTool(
            TROWEL.ID,
            "Decorator's Trowel",
            "",
            new ToolTrowel(),
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 8),
            new TCAspects.TC_AspectStack(TCAspects.SENSUS, 4),
            new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2));
        ItemList.Tool_Vajra.set(new ToolVajra("Tool_Vajra", "Vajra", "", 0, 20, true));
        ItemList.NetworkAnalyzer.set(new ItemNetworkAnalyzer("Network Analyzer", "", 0, 0, true));

        initCraftingShapedRecipes();
        initCraftingShapelessRecipes();
    }

    /**
     * Registers the three tiers of an electric tool family under one name key, so that the tier becomes an argument
     * instead of its own translation. The Meta Values are listed one by one because their layout is irregular. A family
     * whose tiers share a tooltip reads it from one key as well, or passes null when it has none.
     */
    private static void addTieredFamily(String aNameKey, String aToolTipKey, int aLV, int aMV, int aHV) {
        int[] tMetas = { aLV, aMV, aHV };
        for (int i = 0; i < tMetas.length; i++) {
            final String tTier = GTValues.VN[i + 1];
            TIERED_NAMES.put(tMetas[i], () -> StatCollector.translateToLocalFormatted(aNameKey, tTier));
            if (aToolTipKey != null) TIERED_TOOLTIPS.put(tMetas[i], aToolTipKey);
        }
    }

    static {
        addTieredFamily("gt.metatool.01.drill.name", null, DRILL_LV.ID, DRILL_MV.ID, DRILL_HV.ID);
        addTieredFamily(
            "gt.metatool.01.chainsaw.name",
            "gt.metatool.01.chainsaw.tooltip",
            CHAINSAW_LV.ID,
            CHAINSAW_MV.ID,
            CHAINSAW_HV.ID);
        addTieredFamily(
            "gt.metatool.01.jackhammer.name",
            "gt.metatool.01.jackhammer.tooltip",
            JACKHAMMER_LV.ID,
            JACKHAMMER_MV.ID,
            JACKHAMMER_HV.ID);
        addTieredFamily(
            "gt.metatool.01.buzzsaw.name",
            "gt.metatool.01.buzzsaw.tooltip",
            BUZZSAW_LV.ID,
            BUZZSAW_MV.ID,
            BUZZSAW_HV.ID);
        addTieredFamily(
            "gt.metatool.01.soldering_iron.name",
            "gt.metatool.01.soldering_iron.tooltip",
            SOLDERING_IRON_LV.ID,
            SOLDERING_IRON_MV.ID,
            SOLDERING_IRON_HV.ID);
    }

    @Override
    protected String getChargedName(int aMeta) {
        Supplier<String> tName = TIERED_NAMES.get(aMeta);
        return tName == null ? super.getChargedName(aMeta) : tName.get();
    }

    @Override
    protected Function<ItemStack, String> getToolTipLocalizationFunction(ItemStack aStack) {
        final String tKey = TIERED_TOOLTIPS.get(getDamage(aStack));
        return tKey == null ? super.getToolTipLocalizationFunction(aStack)
            : tStack -> StatCollector.translateToLocal(tKey);
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
            mortar(Materials.Flint),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', new ItemStack(Items.flint, 1), 'S', OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.Bronze),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Bronze), 'S', OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.Iron),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Iron), 'S', OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.Steel),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Steel), 'S', OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.CastIron),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.CastIron), 'S',
                OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.RedSteel),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.RedSteel), 'S',
                OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.BlueSteel),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.BlueSteel), 'S',
                OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.BlackSteel),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.BlackSteel), 'S',
                OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.DamascusSteel),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.DamascusSteel), 'S',
                OrePrefixes.stone });
        GTModHandler.addCraftingRecipe(
            mortar(Materials.Thaumium),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { " I ", "SIS", "SSS", 'I', OrePrefixes.ingot.get(Materials.Thaumium), 'S',
                OrePrefixes.stone });

        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN.ID, 1, Materials.Wood, Materials.Wood, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.plank.get(Materials.Wood), 'S',
                OrePrefixes.stick.get(Materials.Wood) });
        GTModHandler.addCraftingRecipe(
            INSTANCE.getToolWithStats(ROLLING_PIN.ID, 1, Materials.Polyethylene, Materials.Polyethylene, null),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "  S", " I ", "S f", 'I', OrePrefixes.ingot.get(Materials.Polyethylene), 'S',
                OrePrefixes.stick.get(Materials.Polyethylene) });
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
            GTToolItems.KNIFE.registerMaterial(
                Materials.Flint,
                new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.TELUM, 2L),
                new TCAspects.TC_AspectStack(TCAspects.CORPUS, 2L)),
            GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "F", "S", 'S', OrePrefixes.stick.get(Materials.Wood), 'F', new ItemStack(Items.flint, 1) });
    }

    /**
     * Declares the mortar for this material and returns a stack of it, for the fixed-material recipes below. The
     * mortar is its own item now, so the material is its metadata rather than NBT.
     */
    private static ItemStack mortar(Materials material) {
        return GTToolItems.MORTAR.registerMaterial(
            material,
            new TCAspects.TC_AspectStack(TCAspects.INSTRUMENTUM, 2L),
            new TCAspects.TC_AspectStack(TCAspects.FABRICO, 2L),
            new TCAspects.TC_AspectStack(TCAspects.PERDITIO, 2L));
    }
}
