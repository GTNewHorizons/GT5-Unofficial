package ggfab;

import static gregtech.api.enums.ToolDictNames.*;
import static gregtech.common.items.IDMetaTool01.*;
import static gregtech.common.items.MetaGeneratedTool01.INSTANCE;

import com.gtnewhorizon.gtnhlib.config.ConfigException;
import com.gtnewhorizon.gtnhlib.config.ConfigurationManager;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import ggfab.api.GGFabRecipeMaps;
import ggfab.api.GigaGramFabAPI;
import ggfab.items.GGMetaItemDumbItems;
import ggfab.items.SingleUseTool;
import ggfab.mte.MTEAdvAssLine;
import ggfab.mte.MTELinkedInputBus;
import ggfab.util.GGUtils;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.metatileentity.implementations.MTEBasicMachineWithRecipe;

@Mod(
    modid = GGConstants.MODID,
    version = GGConstants.VERSION,
    name = GGConstants.MODNAME,
    guiFactory = "ggfab.GGFabGUIFactory",
    acceptedMinecraftVersions = "[1.7.10]",
    dependencies = "required-after:IC2;required-before:gregtech")
public class GigaGramFab {

    static {
        try {
            ConfigurationManager.registerConfig(ConfigurationHandler.class);

        } catch (ConfigException e) {
            throw new RuntimeException(e);
        }
    }

    public GigaGramFab() {
        // initialize the textures
        // noinspection ResultOfMethodCallIgnored
        BlockIcons.OVERLAY_FRONT_ADV_ASSLINE.name();
    }

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        GregTechAPI.sAfterGTPreload.add(() -> {
            GGItemList.AdvAssLine
                .set(new MTEAdvAssLine(13532, "ggfab.machine.adv_assline", "Advanced Assembly Line").getStackForm(1));
            GGItemList.LinkedInputBus.set(
                new MTELinkedInputBus(13533, "ggfab.machine.linked_input_bus", "Linked Input Bus", 5).getStackForm(1));
            GGItemList.ToolCast_MV.set(
                new MTEBasicMachineWithRecipe(
                    13534,
                    "ggfab.toolcast.tier.mv",
                    "Basic Tool Casting Machine",
                    2,
                    "Cheap Crafting Tool for you!",
                    GGFabRecipeMaps.toolCastRecipes,
                    1,
                    4,
                    32000,
                    SoundResource.NONE,
                    MTEBasicMachineWithRecipe.SpecialEffects.MAIN_RANDOM_SPARKS,
                    "TOOL_CAST",
                    new Object[] { "PGP", "WMW", "CBC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                        MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                        MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS, 'B',
                        ItemList.Shape_Empty.get(1L) }).getStackForm(1L));
            GGItemList.ToolCast_HV.set(
                new MTEBasicMachineWithRecipe(
                    13535,
                    "ggfab.toolcast.tier.hv",
                    "Advanced Tool Casting Machine",
                    3,
                    "Cheap Crafting Tool for you!",
                    GGFabRecipeMaps.toolCastRecipes,
                    1,
                    4,
                    64000,
                    SoundResource.NONE,
                    MTEBasicMachineWithRecipe.SpecialEffects.MAIN_RANDOM_SPARKS,
                    "TOOL_CAST",
                    new Object[] { "PGP", "WMW", "CBC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                        MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                        MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS, 'B',
                        ItemList.Shape_Empty.get(1L) }).getStackForm(1L));
            GGItemList.ToolCast_EV.set(
                new MTEBasicMachineWithRecipe(
                    13536,
                    "ggfab.toolcast.tier.ev",
                    "Master Tool Casting Machine",
                    4,
                    "Cheap Crafting Tool for you!",
                    GGFabRecipeMaps.toolCastRecipes,
                    1,
                    4,
                    128000,
                    SoundResource.NONE,
                    MTEBasicMachineWithRecipe.SpecialEffects.MAIN_RANDOM_SPARKS,
                    "TOOL_CAST",
                    new Object[] { "PGP", "WMW", "CBC", 'M', MTEBasicMachineWithRecipe.X.HULL, 'P',
                        MTEBasicMachineWithRecipe.X.PUMP, 'C', MTEBasicMachineWithRecipe.X.CIRCUIT, 'W',
                        MTEBasicMachineWithRecipe.X.WIRE, 'G', MTEBasicMachineWithRecipe.X.GLASS, 'B',
                        ItemList.Shape_Empty.get(1L) }).getStackForm(1L));
            long plate = OrePrefixes.plate.mMaterialAmount, ingot = OrePrefixes.ingot.mMaterialAmount,
                screw = OrePrefixes.screw.mMaterialAmount, rod = OrePrefixes.stick.mMaterialAmount;
            GigaGramFabAPI.addSingleUseToolType(craftingToolFile, INSTANCE.mToolStats.get((short) FILE.ID), 2 * plate);
            GigaGramFabAPI
                .addSingleUseToolType(craftingToolWrench, INSTANCE.mToolStats.get((short) WRENCH.ID), 6 * ingot);
            GigaGramFabAPI
                .addSingleUseToolType(craftingToolCrowbar, INSTANCE.mToolStats.get((short) CROWBAR.ID), 3 * rod);
            GigaGramFabAPI.addSingleUseToolType(
                craftingToolWireCutter,
                INSTANCE.mToolStats.get((short) WIRECUTTER.ID),
                3 * plate + 2 * rod + screw);
            GigaGramFabAPI.addSingleUseToolType(
                craftingToolHardHammer,
                INSTANCE.mToolStats.get((short) HARDHAMMER.ID),
                6 * ingot);
            GigaGramFabAPI.addSingleUseToolType(
                craftingToolSoftMallet,
                INSTANCE.mToolStats.get((short) SOFTMALLET.ID),
                6 * ingot);
            GigaGramFabAPI.addSingleUseToolType(
                craftingToolScrewdriver,
                INSTANCE.mToolStats.get((short) SCREWDRIVER.ID),
                2 * rod);
            GigaGramFabAPI.addSingleUseToolType(craftingToolSaw, INSTANCE.mToolStats.get((short) SAW.ID), 2 * plate);
        });
        GregTechAPI.sBeforeGTPostload.add(new ComponentRecipeLoader());
        GregTechAPI.sBeforeGTPostload.add(new SingleUseToolRecipeLoader());

        initDumbItem1();
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {}

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {}

    private void initDumbItem1() {
        GGMetaItemDumbItems i1 = new GGMetaItemDumbItems("ggfab.d1");
        for (SingleUseTool singleUseTool : SingleUseTool.values()) {
            GGItemList tool = singleUseTool.tool;
            tool.set(
                i1.addItem(
                    singleUseTool.toolID,
                    GGUtils.processSentence(tool.name(), ' ', true, true),
                    null,
                    tool,
                    singleUseTool.toolDictName.name()));

            GGItemList mold = singleUseTool.mold;
            String moldLabel = "Mold (" + GGUtils.processSentence(tool.name() + ")", ' ', true, true);
            mold.set(i1.addItem(singleUseTool.moldID, moldLabel, null, mold, mold.name()));
        }
    }
}
