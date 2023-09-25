package gregtech.api.recipe;

import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.Mods.*;
import static gregtech.api.recipe.check.FindRecipeResult.NOT_FOUND;
import static gregtech.api.recipe.check.FindRecipeResult.ofSuccess;
import static gregtech.api.util.GT_RecipeBuilder.handleRecipeCollision;
import static gregtech.api.util.GT_RecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GT_RecipeMapUtil.*;
import static gregtech.api.util.GT_Utility.*;
import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Iterables;
import com.gtnewhorizons.modularui.api.GlStateManager;
import com.gtnewhorizons.modularui.api.ModularUITextures;
import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import codechicken.nei.PositionedStack;
import gnu.trove.map.TByteObjectMap;
import gnu.trove.map.hash.TByteObjectHashMap;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SteamVariant;
import gregtech.api.gui.GT_GUIColorOverride;
import gregtech.api.gui.modularui.FallbackableSteamTexture;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.gui.modularui.SteamTexture;
import gregtech.api.interfaces.IGT_RecipeMap;
import gregtech.api.interfaces.tileentity.IHasWorldObjectAndCoords;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.check.FindRecipeResult;
import gregtech.api.recipe.maps.AssemblerRecipeMap;
import gregtech.api.recipe.maps.AssemblyLineFakeRecipeMap;
import gregtech.api.recipe.maps.DistillationTowerRecipeMap;
import gregtech.api.recipe.maps.FluidCannerRecipeMap;
import gregtech.api.recipe.maps.FluidOnlyDisplayRecipeMap;
import gregtech.api.recipe.maps.FormingPressRecipeMap;
import gregtech.api.recipe.maps.FuelRecipeMap;
import gregtech.api.recipe.maps.FurnaceRecipeMap;
import gregtech.api.recipe.maps.IC2NuclearFakeRecipeMap;
import gregtech.api.recipe.maps.LargeBoilerFuelFakeRecipeMap;
import gregtech.api.recipe.maps.LargeChemicalReactorRecipeMap;
import gregtech.api.recipe.maps.LargeNEIDisplayRecipeMap;
import gregtech.api.recipe.maps.MaceratorRecipeMap;
import gregtech.api.recipe.maps.MicrowaveRecipeMap;
import gregtech.api.recipe.maps.OilCrackerRecipeMap;
import gregtech.api.recipe.maps.PrinterRecipeMap;
import gregtech.api.recipe.maps.RecyclerRecipeMap;
import gregtech.api.recipe.maps.ReplicatorFakeMap;
import gregtech.api.recipe.maps.SpaceProjectRecipeMap;
import gregtech.api.recipe.maps.TranscendentPlasmaMixerRecipeMap;
import gregtech.api.recipe.maps.UnboxinatorRecipeMap;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeBuilder;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.api.util.GT_RecipeMapUtil;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.power.EUPower;
import gregtech.common.power.Power;
import gregtech.common.power.UnspecifiedEUPower;
import gregtech.nei.FusionSpecialValueFormatter;
import gregtech.nei.GT_NEI_DefaultHandler;
import gregtech.nei.HeatingCoilSpecialValueFormatter;
import gregtech.nei.INEISpecialInfoFormatter;
import gregtech.nei.NEIRecipeInfo;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;

@SuppressWarnings("StaticInitializerReferencesSubClass")
public class RecipeMap implements IGT_RecipeMap {

    /**
     * Contains all Recipe Maps
     */
    public static final Collection<RecipeMap> sMappings = new ArrayList<>();
    /**
     * All recipe maps indexed by their {@link #mUniqueIdentifier}.
     */
    public static final Map<String, RecipeMap> sIndexedMappings = new HashMap<>();

    protected static final String TEXTURES_GUI_BASICMACHINES = "textures/gui/basicmachines";
    public static final RecipeMap sOreWasherRecipes = new RecipeMap(
        new HashSet<>(500),
        "gt.recipe.orewasher",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "OreWasher"),
        1,
        3,
        1,
        1,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE)
            .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_DUST)
            .setRecipeConfigFile("orewasher", FIRST_ITEM_INPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_BATH, ProgressBar.Direction.CIRCULAR_CW);
    public static final RecipeMap sThermalCentrifugeRecipes = new RecipeMap(
        new HashSet<>(1000),
        "gt.recipe.thermalcentrifuge",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "ThermalCentrifuge"),
        1,
        3,
        1,
        0,
        2,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE)
            .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_DUST)
            .setRecipeConfigFile("thermalcentrifuge", FIRST_ITEM_INPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sCompressorRecipes = new RecipeMap(
        new HashSet<>(750),
        "gt.recipe.compressor",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Compressor"),
        1,
        1,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_COMPRESSOR)
            .setRecipeConfigFile("compressor", FIRST_ITEM_INPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_COMPRESS, ProgressBar.Direction.RIGHT)
            .setSlotOverlaySteam(false, GT_UITextures.OVERLAY_SLOT_COMPRESSOR_STEAM)
            .setProgressBarSteam(GT_UITextures.PROGRESSBAR_COMPRESS_STEAM);
    public static final RecipeMap sExtractorRecipes = new RecipeMap(
        new HashSet<>(250),
        "gt.recipe.extractor",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Extractor"),
        1,
        1,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CENTRIFUGE)
            .setRecipeConfigFile("extractor", FIRST_ITEM_INPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_EXTRACT, ProgressBar.Direction.RIGHT)
            .setSlotOverlaySteam(false, GT_UITextures.OVERLAY_SLOT_CENTRIFUGE_STEAM)
            .setProgressBarSteam(GT_UITextures.PROGRESSBAR_EXTRACT_STEAM);
    public static final RecipeMap sRecyclerRecipes = new RecyclerRecipeMap(
        new HashSet<>(0),
        "ic.recipe.recycler",
        null,
        "ic2.recycler",
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Recycler"),
        1,
        1,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        false).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_RECYCLE)
            .setProgressBar(GT_UITextures.PROGRESSBAR_RECYCLE, ProgressBar.Direction.CIRCULAR_CW);
    public static final RecipeMap sFurnaceRecipes = new FurnaceRecipeMap(
        new HashSet<>(0),
        "mc.recipe.furnace",
        "Furnace",
        "smelting",
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "E_Furnace"),
        1,
        1,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        false).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_FURNACE)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
            .setSlotOverlaySteam(false, GT_UITextures.OVERLAY_SLOT_FURNACE_STEAM)
            .setProgressBarSteam(GT_UITextures.PROGRESSBAR_ARROW_STEAM);
    public static final RecipeMap sMicrowaveRecipes = new MicrowaveRecipeMap(
        new HashSet<>(0),
        "gt.recipe.microwave",
        null,
        "smelting",
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "E_Furnace"),
        1,
        1,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        false).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_FURNACE)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);

    public static final RecipeMap sScannerFakeRecipes = new RecipeMap(
        new HashSet<>(300),
        "gt.recipe.scanner",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Scanner"),
        1,
        1,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_MICROSCOPE)
            .setSlotOverlay(false, false, true, true, GT_UITextures.OVERLAY_SLOT_DATA_ORB)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sRockBreakerFakeRecipes = new RecipeMap(
        new HashSet<>(200),
        "gt.recipe.rockbreaker",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "RockBreaker"),
        2,
        1,
        0,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_DUST)
            .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE)
            .setProgressBar(GT_UITextures.PROGRESSBAR_MACERATE, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sReplicatorFakeRecipes = new ReplicatorFakeMap(
        new HashSet<>(100),
        "gt.recipe.replicator",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Replicator"),
        0,
        1,
        0,
        1,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CANISTER)
            .setSlotOverlay(true, false, GT_UITextures.OVERLAY_SLOT_UUM)
            .setSlotOverlay(false, false, true, true, GT_UITextures.OVERLAY_SLOT_DATA_ORB)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sAssemblylineVisualRecipes = new AssemblyLineFakeRecipeMap(
        new HashSet<>(110),
        "gt.recipe.fakeAssemblylineProcess",
        "Assemblyline Process",
        null,
        GregTech.getResourcePath("textures", "gui", "FakeAssemblyline"),
        16,
        1,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, true, true, GT_UITextures.OVERLAY_SLOT_DATA_ORB)
            .setUsualFluidInputCount(4)
            .setDisableOptimize(true);
    /**
     * Usually, but not always, you should use {@link GT_RecipeConstants#UniversalArcFurnace} instead.
     */
    public static final RecipeMap sPlasmaArcFurnaceRecipes = new RecipeMap(
        new HashSet<>(20000),
        "gt.recipe.plasmaarcfurnace",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "PlasmaArcFurnace"),
        1,
        9,
        1,
        1,
        1,
        E,
        1,
        E,
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
            .setRecipeConfigFile("arcfurnace", FIRST_ITEM_INPUT);
    /**
     * Usually, but not always, you should use {@link GT_RecipeConstants#UniversalArcFurnace} instead.
     */
    public static final RecipeMap sArcFurnaceRecipes = new RecipeMap(
        new HashSet<>(20000),
        "gt.recipe.arcfurnace",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "ArcFurnace"),
        1,
        9,
        1,
        1,
        3,
        E,
        1,
        E,
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
            .setRecipeConfigFile("arcfurnace", FIRST_ITEM_INPUT);
    public static final RecipeMap sPrinterRecipes = new PrinterRecipeMap(
        new HashSet<>(5),
        "gt.recipe.printer",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Printer"),
        1,
        1,
        1,
        1,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_PAGE_BLANK)
            .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_PAGE_PRINTED)
            .setSlotOverlay(false, false, true, true, GT_UITextures.OVERLAY_SLOT_DATA_STICK)
            .setRecipeConfigFile("printer", FIRST_ITEM_INPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sSifterRecipes = new RecipeMap(
        new HashSet<>(105),
        "gt.recipe.sifter",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Sifter"),
        1,
        9,
        0,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_SIFT, ProgressBar.Direction.DOWN)
            .setRecipeConfigFile("sifter", FIRST_ITEM_INPUT);
    public static final RecipeMap sPressRecipes = new FormingPressRecipeMap(
        new HashSet<>(300),
        "gt.recipe.press",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Press3"),
        6,
        1,
        2,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_PRESS_1)
            .setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_PRESS_2)
            .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_PRESS_3)
            .setRecipeConfigFile("press", FIRST_ITEM_OUTPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_COMPRESS, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sLaserEngraverRecipes = new RecipeMap(
        new HashSet<>(810),
        "gt.recipe.laserengraver",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "LaserEngraver2"),
        4,
        4,
        0,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_LENS)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
            .setRecipeConfigFile("laserengraving", FIRST_ITEM_OUTPUT)
            .setUsualFluidInputCount(2)
            .setUsualFluidOutputCount(2);
    public static final RecipeMap sMixerRecipes = new RecipeMap(
        new HashSet<>(900),
        "gt.recipe.mixer",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Mixer6"),
        9,
        4,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_DUST)
            .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_DUST)
            .setRecipeConfigFile("mixer", FIRST_ITEM_OR_FLUID_OUTPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_MIXER, ProgressBar.Direction.CIRCULAR_CW);
    public static final RecipeMap sAutoclaveRecipes = new RecipeMap(
        new HashSet<>(300),
        "gt.recipe.autoclave",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Autoclave4"),
        2,
        4,
        1,
        1,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_DUST)
            .setSlotOverlay(false, true, true, GT_UITextures.OVERLAY_SLOT_GEM)
            .setSlotOverlay(false, true, false, GT_UITextures.OVERLAY_SLOT_DUST)
            .setRecipeConfigFile("autoclave", FIRST_ITEM_INPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sElectroMagneticSeparatorRecipes = new RecipeMap(
        new HashSet<>(50),
        "gt.recipe.electromagneticseparator",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "ElectromagneticSeparator"),
        1,
        3,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE)
            .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_DUST)
            .setRecipeConfigFile("electromagneticseparator", FIRST_ITEM_INPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_MAGNET, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sPolarizerRecipes = new RecipeMap(
        new HashSet<>(300),
        "gt.recipe.polarizer",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Polarizer"),
        1,
        1,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_MAGNET, ProgressBar.Direction.RIGHT)
            .setRecipeConfigFile("polarizer", FIRST_ITEM_INPUT);
    public static final RecipeMap sMaceratorRecipes = new MaceratorRecipeMap(
        new HashSet<>(16600),
        "gt.recipe.macerator",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Macerator4"),
        1,
        4,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE)
            .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_DUST)
            .setProgressBar(GT_UITextures.PROGRESSBAR_MACERATE, ProgressBar.Direction.RIGHT)
            .setRecipeConfigFile("pulveriser", FIRST_ITEM_INPUT)
            .setSlotOverlaySteam(false, GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE_STEAM)
            .setSlotOverlaySteam(true, GT_UITextures.OVERLAY_SLOT_DUST_STEAM)
            .setProgressBarSteam(GT_UITextures.PROGRESSBAR_MACERATE_STEAM);
    public static final RecipeMap sChemicalBathRecipes = new RecipeMap(
        new HashSet<>(2550),
        "gt.recipe.chemicalbath",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "ChemicalBath"),
        1,
        3,
        1,
        1,
        1,
        E,
        1,
        E,
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_BATH, ProgressBar.Direction.CIRCULAR_CW)
            .setRecipeConfigFile("chemicalbath", FIRST_ITEM_INPUT);
    public static final RecipeMap sFluidCannerRecipes = new FluidCannerRecipeMap(
        new HashSet<>(2100),
        "gt.recipe.fluidcanner",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "FluidCanner"),
        1,
        1,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CANISTER)
            .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_CANISTER)
            .setRecipeConfigFile("canning", FIRST_ITEM_INPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_CANNER, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sBrewingRecipes = new RecipeMap(
        new HashSet<>(450),
        "gt.recipe.brewer",
        "Brewing Machine",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "PotionBrewer"),
        1,
        0,
        1,
        1,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CAULDRON)
            .setRecipeConfigFile("brewing", FIRST_FLUIDSTACK_OUTPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sFluidHeaterRecipes = new RecipeMap(
        new HashSet<>(10),
        "gt.recipe.fluidheater",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "FluidHeater"),
        1,
        0,
        0,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(true, false, GT_UITextures.OVERLAY_SLOT_HEATER_1)
            .setSlotOverlay(true, true, GT_UITextures.OVERLAY_SLOT_HEATER_2)
            .setRecipeConfigFile("fluidheater", FIRST_FLUIDSTACK_OUTPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sDistilleryRecipes = new RecipeMap(
        new HashSet<>(400),
        "gt.recipe.distillery",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Distillery"),
        1,
        1,
        1,
        1,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(true, false, GT_UITextures.OVERLAY_SLOT_BEAKER_1)
            .setSlotOverlay(true, true, GT_UITextures.OVERLAY_SLOT_BEAKER_2)
            .setRecipeConfigFile("distillery", FIRST_FLUIDSTACK_OUTPUT)
            .setRecipeSpecialHandler(r -> {
                int aInput = r.mFluidInputs[0].amount, aOutput = r.mFluidOutputs[0].amount, aDuration = r.mDuration;

                // reduce the batch size if fluid amount is exceeding
                int tScale = (Math.max(aInput, aOutput) + 999) / 1000;
                if (tScale <= 0) tScale = 1;
                if (tScale > 1) {
                    // trying to find whether there is a better factor
                    for (int i = tScale; i <= 5; i++) {
                        if (aInput % i == 0 && aDuration % i == 0) {
                            tScale = i;
                            break;
                        }
                    }
                    for (int i = tScale; i <= 5; i++) {
                        if (aInput % i == 0 && aDuration % i == 0 && aOutput % i == 0) {
                            tScale = i;
                            break;
                        }
                    }
                    aInput = (aInput + tScale - 1) / tScale;
                    aOutput = aOutput / tScale;
                    if (!isArrayEmptyOrNull(r.mOutputs)) {
                        ItemData tData = GT_OreDictUnificator.getItemData(r.mOutputs[0]);
                        if (tData != null && (tData.mPrefix == OrePrefixes.dust
                            || OrePrefixes.dust.mFamiliarPrefixes.contains(tData.mPrefix))) {
                            r.mOutputs[0] = GT_OreDictUnificator.getDust(
                                tData.mMaterial.mMaterial,
                                tData.mMaterial.mAmount * r.mOutputs[0].stackSize / tScale);
                        } else {
                            if (r.mOutputs[0].stackSize / tScale == 0) r.mOutputs[0] = GT_Values.NI;
                            else r.mOutputs[0] = copyAmount(r.mOutputs[0].stackSize / tScale, r.mOutputs[0]);
                        }
                    }
                    aDuration = (aDuration + tScale - 1) / tScale;
                    r.mFluidInputs[0] = copyAmount(aInput, r.mFluidInputs[0]);
                    r.mFluidOutputs[0] = copyAmount(aOutput, r.mFluidOutputs[0]);
                    r.mDuration = aDuration;
                }
            })
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sFermentingRecipes = new RecipeMap(
        new HashSet<>(50),
        "gt.recipe.fermenter",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Fermenter"),
        0,
        0,
        0,
        1,
        1,
        E,
        1,
        E,
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT)
            .setRecipeConfigFile("fermenting", FIRST_FLUIDSTACK_OUTPUT);
    public static final RecipeMap sFluidSolidficationRecipes = new RecipeMap(
        new HashSet<>(35000),
        "gt.recipe.fluidsolidifier",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "FluidSolidifier"),
        1,
        1,
        1,
        1,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_MOLD)
            .setRecipeConfigFile("fluidsolidifier", FIRST_ITEM_OUTPUT)
            .setRecipeSpecialHandler(r -> {
                if (ArrayUtils.isNotEmpty(r.mFluidInputs)) {
                    if (Materials.PhasedGold.getMolten(1)
                        .isFluidEqual(r.mFluidInputs[0]))
                        r.mFluidInputs = new FluidStack[] {
                            Materials.VibrantAlloy.getMolten(r.mFluidInputs[0].amount) };
                    else if (Materials.PhasedIron.getMolten(1)
                        .isFluidEqual(r.mFluidInputs[0]))
                        r.mFluidInputs = new FluidStack[] {
                            Materials.PulsatingIron.getMolten(r.mFluidInputs[0].amount) };
                }
            })
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sFluidExtractionRecipes = new RecipeMap(
        new HashSet<>(15000),
        "gt.recipe.fluidextractor",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "FluidExtractor"),
        1,
        1,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CENTRIFUGE)
            .setRecipeConfigFile("fluidextractor", FIRST_ITEM_INPUT)
            .setRecipeSpecialHandler(r -> {
                if (ArrayUtils.isNotEmpty(r.mFluidInputs)) {
                    if (Materials.PhasedGold.getMolten(1)
                        .isFluidEqual(r.mFluidInputs[0]))
                        r.mFluidInputs = new FluidStack[] {
                            Materials.VibrantAlloy.getMolten(r.mFluidInputs[0].amount) };
                    else if (Materials.PhasedIron.getMolten(1)
                        .isFluidEqual(r.mFluidInputs[0]))
                        r.mFluidInputs = new FluidStack[] {
                            Materials.PulsatingIron.getMolten(r.mFluidInputs[0].amount) };
                }
            })
            .setProgressBar(GT_UITextures.PROGRESSBAR_EXTRACT, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sBoxinatorRecipes = new RecipeMap(
        new HashSet<>(2500),
        "gt.recipe.packager",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Packager"),
        2,
        1,
        2,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_BOX)
            .setRecipeConfigFile("boxing", FIRST_ITEM_OUTPUT)
            .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_BOXED)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sUnboxinatorRecipes = new UnboxinatorRecipeMap(
        new HashSet<>(2500),
        "gt.recipe.unpackager",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Unpackager"),
        1,
        2,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_BOXED)
            .setRecipeConfigFile("unboxing", FIRST_ITEM_OUTPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sFusionRecipes = new FluidOnlyDisplayRecipeMap(
        new HashSet<>(50),
        "gt.recipe.fusionreactor",
        "Fusion Reactor",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "FusionReactor"),
        0,
        0,
        0,
        2,
        1,
        "Start: ",
        1,
        " EU",
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
            .useComparatorForNEI(true)
            .setUsualFluidInputCount(2)
            .setRecipeConfigFile("fusion", FIRST_FLUID_OUTPUT)
            .setDisableOptimize(true)
            .setNEISpecialInfoFormatter(FusionSpecialValueFormatter.INSTANCE);
    public static final RecipeMap sCentrifugeRecipes = new RecipeMap(
        new HashSet<>(1200),
        "gt.recipe.centrifuge",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Centrifuge"),
        2,
        6,
        0,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_CENTRIFUGE)
            .setRecipeConfigFile("centrifuge", FIRST_ITEM_OR_FLUID_INPUT)
            .setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_CANISTER)
            .setSlotOverlay(true, false, GT_UITextures.OVERLAY_SLOT_CENTRIFUGE_FLUID)
            .setProgressBar(GT_UITextures.PROGRESSBAR_EXTRACT, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sElectrolyzerRecipes = new RecipeMap(
        new HashSet<>(300),
        "gt.recipe.electrolyzer",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Electrolyzer"),
        2,
        6,
        0,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_CHARGER)
            .setRecipeConfigFile("electrolyzer", FIRST_ITEM_OR_FLUID_INPUT)
            .setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_CANISTER)
            .setSlotOverlay(true, false, GT_UITextures.OVERLAY_SLOT_CHARGER_FLUID)
            .setProgressBar(GT_UITextures.PROGRESSBAR_EXTRACT, ProgressBar.Direction.RIGHT);
    /**
     * Use {@link GT_RecipeConstants#COIL_HEAT} as heat level.
     */
    public static final RecipeMap sBlastRecipes = new RecipeMap(
        new HashSet<>(800),
        "gt.recipe.blastfurnace",
        "Blast Furnace",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        6,
        6,
        1,
        0,
        1,
        "Heat Capacity: ",
        1,
        " K",
        false,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
            .setRecipeConfigFile("blastfurnace", FIRST_ITEM_INPUT)
            .setNEISpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE);
    /**
     * Use {@link GT_RecipeConstants#COIL_HEAT} as heat level.
     */
    public static final RecipeMap sPlasmaForgeRecipes = new LargeNEIDisplayRecipeMap(
        new HashSet<>(20),
        "gt.recipe.plasmaforge",
        "DTPF",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "PlasmaForge"),
        9,
        9,
        0,
        0,
        1,
        "Heat Capacity: ",
        1,
        " K",
        false,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
            .setUsualFluidInputCount(9)
            .setUsualFluidOutputCount(9)
            .setDisableOptimize(true)
            .setNEISpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE);

    public static final RecipeMap sTranscendentPlasmaMixerRecipes = new TranscendentPlasmaMixerRecipeMap(
        new HashSet<>(20),
        "gt.recipe.transcendentplasmamixerrecipes",
        "Transcendent Plasma Mixer",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "PlasmaForge"),
        1,
        0,
        0,
        0,
        1,
        "",
        0,
        "",
        false,
        true).setDisableOptimize(true);

    public static class GT_FakeSpaceProjectRecipe extends GT_Recipe {

        public final String projectName;

        public GT_FakeSpaceProjectRecipe(boolean aOptimize, ItemStack[] aInputs, FluidStack[] aFluidInputs,
            int aDuration, int aEUt, int aSpecialValue, String projectName) {
            super(aOptimize, aInputs, null, null, null, aFluidInputs, null, aDuration, aEUt, aSpecialValue);
            this.projectName = projectName;
        }
    }

    public static final RecipeMap sFakeSpaceProjectRecipes = new SpaceProjectRecipeMap(
        new HashSet<>(20),
        "gt.recipe.fakespaceprojects",
        "Space Projects",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        12,
        0,
        0,
        0,
        1,
        translateToLocal("gt.specialvalue.stages") + " ",
        1,
        "",
        false,
        true).useModularUI(true)
            .setRenderRealStackSizes(false)
            .setUsualFluidInputCount(4)
            .setNEIBackgroundOffset(2, 23)
            .setLogoPos(152, 83)
            .setDisableOptimize(true);

    /**
     * Uses {@link GT_RecipeConstants#ADDITIVE_AMOUNT} for coal/charcoal amount.
     */
    public static final RecipeMap sPrimitiveBlastRecipes = new RecipeMap(
        new HashSet<>(200),
        "gt.recipe.primitiveblastfurnace",
        "Primitive Blast Furnace",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        3,
        3,
        1,
        0,
        1,
        E,
        1,
        E,
        false,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
            .setRecipeEmitter(builder -> {
                Optional<GT_Recipe> rr = builder.validateInputCount(1, 2)
                    .validateOutputCount(1, 2)
                    .validateNoInputFluid()
                    .validateNoOutputFluid()
                    .noOptimize()
                    .build();
                if (!rr.isPresent()) return Collections.emptyList();
                ItemStack aInput1 = builder.getItemInputBasic(0);
                ItemStack aInput2 = builder.getItemInputBasic(1);
                ItemStack aOutput1 = builder.getItemOutput(0);
                ItemStack aOutput2 = builder.getItemOutput(1);
                if ((aInput1 == null && aInput2 == null) || (aOutput1 == null && aOutput2 == null))
                    return Collections.emptyList();
                int aCoalAmount = builder.getMetadata(ADDITIVE_AMOUNT);
                if (aCoalAmount <= 0) return Collections.emptyList();
                GT_RecipeTemplate coll = asTemplate(rr.get());
                for (Materials coal : new Materials[] { Materials.Coal, Materials.Charcoal }) {
                    coll.derive()
                        .setInputs(aInput1, aInput2, coal.getGems(aCoalAmount))
                        .setOutputs(aOutput1, aOutput2, Materials.DarkAsh.getDustTiny(aCoalAmount));
                    coll.derive()
                        .setInputs(aInput1, aInput2, coal.getDust(aCoalAmount))
                        .setOutputs(aOutput1, aOutput2, Materials.DarkAsh.getDustTiny(aCoalAmount));
                }
                int aDuration = builder.getDuration();
                if (Railcraft.isModLoaded()) {
                    coll.derive()
                        .setInputs(aInput1, aInput2, RailcraftToolItems.getCoalCoke(aCoalAmount / 2))
                        .setOutputs(aOutput1, aOutput2, Materials.Ash.getDustTiny(aCoalAmount / 2))
                        .setDuration(aDuration * 2 / 3);
                }
                if (GTPlusPlus.isModLoaded()) {
                    ItemStack cactusCoke = GT_ModHandler.getModItem(GTPlusPlus.ID, "itemCactusCoke", aCoalAmount * 2L);
                    ItemStack sugarCoke = GT_ModHandler.getModItem(GTPlusPlus.ID, "itemSugarCoke", aCoalAmount * 2L);
                    coll.derive()
                        .setInputs(aInput1, aInput2, cactusCoke)
                        .setOutputs(aOutput1, aOutput2, Materials.Ash.getDustTiny(aCoalAmount * 2))
                        .setDuration(aDuration * 2 / 3);
                    coll.derive()
                        .setInputs(aInput1, aInput2, sugarCoke)
                        .setOutputs(aOutput1, aOutput2, Materials.Ash.getDustTiny(aCoalAmount * 2))
                        .setDuration(aDuration * 2 / 3);
                }
                if ((aInput1 == null || aInput1.stackSize <= 6) && (aInput2 == null || aInput2.stackSize <= 6)
                    && (aOutput1 == null || aOutput1.stackSize <= 6)
                    && (aOutput2 == null || aOutput2.stackSize <= 6)) {
                    // we don't use GT_Utility.mul() here. It does not have the truncating we need here.
                    aInput1 = multiplyStack(10L, aInput1);
                    aInput2 = multiplyStack(10L, aInput2);
                    aOutput1 = multiplyStack(10L, aOutput1);
                    aOutput2 = multiplyStack(10L, aOutput2);
                    for (Materials coal : new Materials[] { Materials.Coal, Materials.Charcoal }) {
                        coll.derive()
                            .setInputs(aInput1, aInput2, coal.getBlocks(aCoalAmount))
                            .setOutputs(aOutput1, aOutput2, Materials.DarkAsh.getDust(aCoalAmount))
                            .setDuration(aDuration * 10);
                        coll.derive()
                            .setInputs(aInput1, aInput2, coal.getBlocks(aCoalAmount))
                            .setOutputs(aOutput1, aOutput2, Materials.DarkAsh.getDust(aCoalAmount))
                            .setDuration(aDuration * 10);
                    }
                    if (Railcraft.isModLoaded()) {
                        coll.derive()
                            .setInputs(aInput1, aInput2, EnumCube.COKE_BLOCK.getItem(aCoalAmount / 2))
                            .setOutputs(aOutput1, aOutput2, Materials.Ash.getDust(aCoalAmount / 2))
                            .setDuration(aDuration * 20 / 3);
                    }
                }
                return coll.getAll();
            })
            .setRecipeConfigFile("primitiveblastfurnace", FIRST_ITEM_INPUT);
    /**
     * Uses {@link GT_RecipeConstants#ADDITIVE_AMOUNT} for TNT/ITNT/... amount. Value is truncated to [0, 64]
     */
    public static final RecipeMap sImplosionRecipes = new RecipeMap(
        new HashSet<>(900),
        "gt.recipe.implosioncompressor",
        "Implosion Compressor",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        2,
        2,
        2,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_IMPLOSION)
            .setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_EXPLOSIVE)
            .setRecipeConfigFile("implosion", FIRST_ITEM_INPUT)
            .setRecipeEmitter(b -> {
                switch (b.getItemInputsBasic().length) {
                    case 0:
                        return Collections.emptyList();
                    case 1:
                        break;
                    default:
                        return b.build()
                            .map(Collections::singletonList)
                            .orElse(Collections.emptyList());
                }
                Optional<GT_Recipe> t = b.noOptimize()
                    .duration(20)
                    .eut(30)
                    .validateInputCount(1, 1)
                    .validateOutputCount(1, 2)
                    .build();
                if (!t.isPresent()) return Collections.emptyList();
                ItemStack input = b.getItemInputBasic(0);
                GT_RecipeTemplate coll = asTemplate(t.get());
                int tExplosives = Math.min(b.getMetadata(ADDITIVE_AMOUNT), 64);
                int tGunpowder = tExplosives << 1; // Worst
                int tDynamite = Math.max(1, tExplosives >> 1); // good
                @SuppressWarnings("UnnecessaryLocalVariable")
                int tTNT = tExplosives; // Slightly better
                int tITNT = Math.max(1, tExplosives >> 2); // the best
                if (tGunpowder < 65) coll.derive()
                    .setInputs(input, ItemList.Block_Powderbarrel.get(tGunpowder));
                if (tDynamite < 17) coll.derive()
                    .setInputs(input, GT_ModHandler.getIC2Item("dynamite", tDynamite, null));
                coll.derive()
                    .setInputs(input, new ItemStack(Blocks.tnt, tTNT));
                coll.derive()
                    .setInputs(input, GT_ModHandler.getIC2Item("industrialTnt", tITNT, null));
                return coll.getAll();
            })
            .setDisableOptimize(true)
            .setProgressBar(GT_UITextures.PROGRESSBAR_COMPRESS, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sVacuumRecipes = new RecipeMap(
        new HashSet<>(305),
        "gt.recipe.vacuumfreezer",
        "Vacuum Freezer",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        1,
        1,
        0,
        0,
        1,
        E,
        1,
        E,
        false,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
            .setRecipeConfigFile("vacuumfreezer", FIRST_ITEM_INPUT)
            .setRecipeEmitter(b -> {
                b.noOptimize();
                FluidStack in, out;
                if (isArrayOfLength(b.getItemInputsBasic(), 1) && isArrayOfLength(b.getItemOutputs(), 1)
                    && isArrayEmptyOrNull(b.getFluidInputs())
                    && isArrayEmptyOrNull(b.getFluidOutputs())
                    && (in = getFluidForFilledItem(b.getItemInputBasic(0), true)) != null
                    && (out = getFluidForFilledItem(b.getItemOutput(0), true)) != null) {
                    return Arrays.asList(
                        b.build()
                            .get(),
                        b.fluidInputs(in)
                            .fluidOutputs(out)
                            .build()
                            .get());
                }
                return buildOrEmpty(b);
            })
            .setUsualFluidInputCount(2);
    /**
     * using {@code .addTo(sChemicalRecipes)} will cause the recipe to be added to single block recipe map ONLY!
     * use {@link GT_RecipeConstants#UniversalChemical} to add to both.
     */
    public static final RecipeMap sChemicalRecipes = new RecipeMap(
        new HashSet<>(1170),
        "gt.recipe.chemicalreactor",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "ChemicalReactor"),
        2,
        2,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_MOLECULAR_1)
            .setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_MOLECULAR_2)
            .setSlotOverlay(true, false, GT_UITextures.OVERLAY_SLOT_MOLECULAR_3)
            .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_VIAL_1)
            .setSlotOverlay(true, true, GT_UITextures.OVERLAY_SLOT_VIAL_2)
            .setRecipeConfigFile("chemicalreactor", FIRST_ITEM_OR_FLUID_OUTPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT)
            .setDisableOptimize(true);
    /**
     * using {@code .addTo(sMultiblockChemicalRecipes)} will cause the recipe to be added to multiblock recipe map
     * ONLY!
     * use {@link GT_RecipeConstants#UniversalChemical} to add to both.
     */
    public static final RecipeMap sMultiblockChemicalRecipes = //
        new LargeChemicalReactorRecipeMap()
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT)
            .setUsualFluidInputCount(6)
            .setUsualFluidOutputCount(6)
            .setDisableOptimize(true);
    public static final RecipeMap sDistillationRecipes = //
        new DistillationTowerRecipeMap().setRecipeConfigFile("distillation", FIRST_FLUIDSTACK_INPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT)
            .setUsualFluidOutputCount(11)
            .setDisableOptimize(true);
    public static final OilCrackerRecipeMap sCrackingRecipes = (OilCrackerRecipeMap) //
    new OilCrackerRecipeMap().setRecipeConfigFile("cracking", FIRST_FLUIDSTACK_INPUT)
        .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT)
        .setUsualFluidInputCount(2);

    public static final RecipeMap sPyrolyseRecipes = new RecipeMap(
        new HashSet<>(150),
        "gt.recipe.pyro",
        "Pyrolyse Oven",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        2,
        1,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
            .setDisableOptimize(true)
            .setRecipeConfigFile("pyrolyse", FIRST_ITEM_INPUT);
    public static final RecipeMap sWiremillRecipes = new RecipeMap(
        new HashSet<>(450),
        "gt.recipe.wiremill",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Wiremill"),
        2,
        1,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_WIREMILL)
            .setRecipeConfigFile("wiremill", FIRST_ITEM_INPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_WIREMILL, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sBenderRecipes = new RecipeMap(
        new HashSet<>(5000),
        "gt.recipe.metalbender",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Bender"),
        2,
        1,
        2,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_BENDER)
            .setRecipeConfigFile("bender", FIRST_ITEM_INPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_BENDING, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sAlloySmelterRecipes = new RecipeMap(
        new HashSet<>(12000),
        "gt.recipe.alloysmelter",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "AlloySmelter"),
        2,
        1,
        2,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_FURNACE)
            .setRecipeEmitter(b -> {
                if (Materials.Graphite.contains(b.getItemInputBasic(0))) return Collections.emptyList();
                if (GT_Utility.isArrayOfLength(b.getItemInputsBasic(), 1)) {
                    ItemStack aInput1 = b.getItemInputBasic(0);
                    if (((OrePrefixes.ingot.contains(aInput1)) || (OrePrefixes.dust.contains(aInput1))
                        || (OrePrefixes.gem.contains(aInput1)))) return Collections.emptyList();
                }
                return buildOrEmpty(
                    b.validateNoInputFluid()
                        .validateNoOutputFluid()
                        .validateInputCount(1, 2)
                        .validateOutputCount(1, 1));
            })
            .setRecipeConfigFile(
                "alloysmelting",
                r -> GT_Config
                    .getStackConfigName(GT_Utility.isArrayOfLength(r.mInputs, 1) ? r.mInputs[0] : r.mOutputs[0]))
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
            .setSlotOverlaySteam(false, GT_UITextures.OVERLAY_SLOT_FURNACE_STEAM)
            .setProgressBarSteam(GT_UITextures.PROGRESSBAR_ARROW_STEAM);
    public static final RecipeMap sAssemblerRecipes = new AssemblerRecipeMap(
        new HashSet<>(8200),
        "gt.recipe.assembler",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Assembler2"),
        9,
        1,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CIRCUIT)
            .setRecipeConfigFile("assembling", FIRST_ITEM_OUTPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ASSEMBLE, ProgressBar.Direction.RIGHT)
            .setDisableOptimize(true);
    public static final RecipeMap sCircuitAssemblerRecipes = new AssemblerRecipeMap(
        new HashSet<>(605),
        "gt.recipe.circuitassembler",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "CircuitAssembler"),
        6,
        1,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setNEIUnificateOutput(!NEICustomDiagrams.isModLoaded())
            .setRecipeConfigFile("circuitassembler", FIRST_ITEM_OUTPUT)
            .setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CIRCUIT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_CIRCUIT_ASSEMBLER, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sCannerRecipes = new RecipeMap(
        new HashSet<>(900),
        "gt.recipe.canner",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Canner"),
        2,
        2,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_CANNER)
            .setRecipeConfigFile("canning", FIRST_ITEM_INPUT)
            .setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_CANISTER)
            .setProgressBar(GT_UITextures.PROGRESSBAR_CANNER, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sLatheRecipes = new RecipeMap(
        new HashSet<>(1150),
        "gt.recipe.lathe",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Lathe"),
        1,
        2,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_ROD_1)
            .setSlotOverlay(false, true, true, GT_UITextures.OVERLAY_SLOT_ROD_2)
            .setSlotOverlay(false, true, false, GT_UITextures.OVERLAY_SLOT_DUST)
            .setRecipeConfigFile("lathe", FIRST_ITEM_INPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_LATHE, ProgressBar.Direction.RIGHT)
            .addSpecialTexture(5, 18, 98, 24, GT_UITextures.PROGRESSBAR_LATHE_BASE);
    public static final RecipeMap sCutterRecipes = new RecipeMap(
        new HashSet<>(5125),
        "gt.recipe.cuttingsaw",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Cutter4"),
        2,
        4,
        1,
        1,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_BOX)
            .setSlotOverlay(false, true, true, GT_UITextures.OVERLAY_SLOT_CUTTER_SLICED)
            .setSlotOverlay(false, true, false, GT_UITextures.OVERLAY_SLOT_DUST)
            .setRecipeEmitter(b -> {
                b.validateInputCount(1, 2)
                    .validateOutputCount(1, 4)
                    .validateNoOutputFluid();
                if ((b.getFluidInputs() != null && b.getFluidInputs().length > 0) || !b.isValid())
                    return buildOrEmpty(b.validateInputFluidCount(1, 1));
                int aDuration = b.getDuration(), aEUt = b.getEUt();
                Collection<GT_Recipe> ret = new ArrayList<>();
                b.copy()
                    .fluidInputs(Materials.Water.getFluid(clamp(aDuration * aEUt / 320, 4, 1000)))
                    .duration(aDuration * 2)
                    .build()
                    .ifPresent(ret::add);
                b.copy()
                    .fluidInputs(GT_ModHandler.getDistilledWater(clamp(aDuration * aEUt / 426, 3, 750)))
                    .duration(aDuration * 2)
                    .build()
                    .ifPresent(ret::add);
                b.fluidInputs(Materials.Lubricant.getFluid(clamp(aDuration * aEUt / 1280, 1, 250)))
                    .duration(aDuration)
                    .build()
                    .ifPresent(ret::add);
                return ret;
            })
            .setRecipeConfigFile("cutting", FIRST_ITEM_INPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_CUT, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sSlicerRecipes = new RecipeMap(
        new HashSet<>(20),
        "gt.recipe.slicer",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Slicer"),
        2,
        1,
        2,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_SQUARE)
            .setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_SLICE_SHAPE)
            .setSlotOverlay(false, true, GT_UITextures.OVERLAY_SLOT_SLICER_SLICED)
            .setRecipeConfigFile("slicer", FIRST_ITEM_OUTPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_SLICE, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sExtruderRecipes = new RecipeMap(
        new HashSet<>(13000),
        "gt.recipe.extruder",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Extruder"),
        2,
        1,
        2,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, false, GT_UITextures.OVERLAY_SLOT_EXTRUDER_SHAPE)
            .setRecipeConfigFile("extruder", FIRST_ITEM_OUTPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_EXTRUDE, ProgressBar.Direction.RIGHT);

    public static final RecipeMap sHammerRecipes = new RecipeMap(
        new HashSet<>(3800),
        "gt.recipe.hammer",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Hammer"),
        2,
        2,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setUsualFluidInputCount(2)
            .setUsualFluidOutputCount(2)
            .setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_HAMMER)
            .setRecipeConfigFile("forgehammer", FIRST_ITEM_OUTPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_HAMMER, ProgressBar.Direction.DOWN)
            .addSpecialTexture(20, 6, 78, 42, GT_UITextures.PROGRESSBAR_HAMMER_BASE)
            .setSlotOverlaySteam(false, GT_UITextures.OVERLAY_SLOT_HAMMER_STEAM)
            .setProgressBarSteam(GT_UITextures.PROGRESSBAR_HAMMER_STEAM)
            .addSpecialTextureSteam(20, 6, 78, 42, GT_UITextures.PROGRESSBAR_HAMMER_BASE_STEAM);
    public static final RecipeMap sAmplifiers = new RecipeMap(
        new HashSet<>(2),
        "gt.recipe.uuamplifier",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Amplifabricator"),
        1,
        0,
        1,
        0,
        1,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(false, false, GT_UITextures.OVERLAY_SLOT_CENTRIFUGE)
            .setSlotOverlay(true, true, GT_UITextures.OVERLAY_SLOT_UUA)
            .setRecipeConfigFile("amplifier", FIRST_ITEM_INPUT)
            .setProgressBar(GT_UITextures.PROGRESSBAR_EXTRACT, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sMassFabFakeRecipes = new RecipeMap(
        new HashSet<>(2),
        "gt.recipe.massfab",
        null,
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Massfabricator"),
        1,
        0,
        1,
        0,
        8,
        E,
        1,
        E,
        true,
        true).setSlotOverlay(true, false, GT_UITextures.OVERLAY_SLOT_UUA)
            .setSlotOverlay(true, true, GT_UITextures.OVERLAY_SLOT_UUM)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final FuelRecipeMap sDieselFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(20),
        "gt.recipe.dieselgeneratorfuel",
        "Combustion Generator Fuels",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        1,
        1,
        0,
        0,
        1,
        "Fuel Value: ",
        1000,
        " EU",
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final FuelRecipeMap sExtremeDieselFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(20),
        "gt.recipe.extremedieselgeneratorfuel",
        "Extreme Diesel Engine Fuel",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        1,
        1,
        0,
        0,
        1,
        "Fuel Value: ",
        1000,
        " EU",
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final FuelRecipeMap sTurbineFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(25),
        "gt.recipe.gasturbinefuel",
        "Gas Turbine Fuel",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        1,
        1,
        0,
        0,
        1,
        "Fuel Value: ",
        1000,
        " EU",
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final FuelRecipeMap sHotFuels = new FuelRecipeMap(
        new HashSet<>(10),
        "gt.recipe.thermalgeneratorfuel",
        "Thermal Generator Fuels",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        1,
        1,
        0,
        0,
        1,
        "Fuel Value: ",
        1000,
        " EU",
        true,
        false);
    public static final FuelRecipeMap sDenseLiquidFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(15),
        "gt.recipe.semifluidboilerfuels",
        "Semifluid Boiler Fuels",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        1,
        1,
        0,
        0,
        1,
        "Fuel Value: ",
        1000,
        " EU",
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final FuelRecipeMap sPlasmaFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(100),
        "gt.recipe.plasmageneratorfuels",
        "Plasma Generator Fuels",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        1,
        1,
        0,
        0,
        1,
        "Fuel Value: ",
        1000,
        " EU",
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final FuelRecipeMap sMagicFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(100),
        "gt.recipe.magicfuels",
        "Magic Energy Absorber Fuels",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        1,
        1,
        0,
        0,
        1,
        "Fuel Value: ",
        1000,
        " EU",
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final FuelRecipeMap sSmallNaquadahReactorFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(1),
        "gt.recipe.smallnaquadahreactor",
        "Naquadah Reactor MkI",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        1,
        1,
        0,
        0,
        1,
        "Fuel Value: ",
        1000,
        " EU",
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final FuelRecipeMap sLargeNaquadahReactorFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(1),
        "gt.recipe.largenaquadahreactor",
        "Naquadah Reactor MkII",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        1,
        1,
        0,
        0,
        1,
        "Fuel Value: ",
        1000,
        " EU",
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final FuelRecipeMap sHugeNaquadahReactorFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(1),
        "gt.recipe.fluidnaquadahreactor",
        "Naquadah Reactor MkIII",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        1,
        1,
        0,
        0,
        1,
        "Fuel Value: ",
        1000,
        " EU",
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final FuelRecipeMap sExtremeNaquadahReactorFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(1),
        "gt.recipe.hugenaquadahreactor",
        "Naquadah Reactor MkIV",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        1,
        1,
        0,
        0,
        1,
        "Fuel Value: ",
        1000,
        " EU",
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final FuelRecipeMap sUltraHugeNaquadahReactorFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(1),
        "gt.recipe.extrahugenaquadahreactor",
        "Naquadah Reactor MkV",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        1,
        1,
        0,
        0,
        1,
        "Fuel Value: ",
        1000,
        " EU",
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final FuelRecipeMap sFluidNaquadahReactorFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(1),
        "gt.recipe.fluidfuelnaquadahreactor",
        "Fluid Naquadah Reactor",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
        1,
        1,
        0,
        0,
        1,
        "Fuel Value: ",
        1000,
        " EU",
        true,
        true).setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT);
    public static final RecipeMap sMultiblockElectrolyzerRecipes = new RecipeMap(
        new HashSet<>(300),
        "gt.recipe.largeelectrolyzer",
        "Large(PA) Electrolyzer",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
        1,
        9,
        0,
        0,
        1,
        "",
        0,
        "",
        true,
        false).setRecipeEmitter(GT_RecipeMapUtil::buildRecipeForMultiblock);

    public static final RecipeMap sMultiblockCentrifugeRecipes = new RecipeMap(
        new HashSet<>(1200),
        "gt.recipe.largecentrifuge",
        "Large(PA) Centrifuge",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
        1,
        9,
        0,
        0,
        1,
        "",
        0,
        "",
        true,
        false).setRecipeEmitter(GT_RecipeMapUtil::buildRecipeForMultiblock)
            .setDisableOptimize(true);
    public static final RecipeMap sMultiblockMixerRecipes = new RecipeMap(
        new HashSet<>(900),
        "gt.recipe.largemixer",
        "Large(PA) Mixer",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
        9,
        3,
        0,
        0,
        1,
        "",
        0,
        "",
        true,
        false).setRecipeEmitter(GT_RecipeMapUtil::buildRecipeForMultiblockNoCircuit)
            .setDisableOptimize(true);
    public static final LargeBoilerFuelFakeRecipeMap sLargeBoilerFakeFuels = (LargeBoilerFuelFakeRecipeMap) new LargeBoilerFuelFakeRecipeMap()
        .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
        .setDisableOptimize(true);

    public static final RecipeMap sNanoForge = new RecipeMap(
        new HashSet<>(10),
        "gt.recipe.nanoforge",
        "Nano Forge",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
        6,
        2,
        2,
        1,
        1,
        "Tier: ",
        1,
        "",
        false,
        true).useModularUI(true)
            .setUsualFluidInputCount(3)
            .setDisableOptimize(true)
            .setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_LENS)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ASSEMBLE, ProgressBar.Direction.RIGHT);

    public static final RecipeMap sPCBFactory = new RecipeMap(
        new HashSet<>(10),
        "gt.recipe.pcbfactory",
        "PCB Factory",
        null,
        GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
        6,
        9,
        3,
        1,
        1,
        E,
        0,
        E,
        true,
        true).useModularUI(true)
            .setUsualFluidInputCount(3)
            .setUsualFluidOutputCount(0)
            .setDisableOptimize(true)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ASSEMBLE, ProgressBar.Direction.RIGHT)
            .setNEISpecialInfoFormatter((recipeInfo, applyPrefixAndSuffix) -> {
                List<String> result = new ArrayList<>();
                int bitmap = recipeInfo.recipe.mSpecialValue;
                if ((bitmap & 0b1) > 0) {
                    result.add(trans("336", "PCB Factory Tier: ") + 1);
                } else if ((bitmap & 0b10) > 0) {
                    result.add(trans("336", "PCB Factory Tier: ") + 2);
                } else if ((bitmap & 0b100) > 0) {
                    result.add(trans("336", "PCB Factory Tier: ") + 3);
                }
                if ((bitmap & 0b1000) > 0) {
                    result.add(trans("337", "Upgrade Required: ") + trans("338", "Bio"));
                }
                return result;
            });

    public static final IC2NuclearFakeRecipeMap sIC2NuclearFakeRecipe = (IC2NuclearFakeRecipeMap) new IC2NuclearFakeRecipeMap()
        .setDisableOptimize(true);

    static {
        sCentrifugeRecipes.addDownstream(sMultiblockCentrifugeRecipes.deepCopyInput());
        sMixerRecipes.addDownstream(sMultiblockMixerRecipes.deepCopyInput());
        sElectrolyzerRecipes.addDownstream(sMultiblockElectrolyzerRecipes.deepCopyInput());
        sDieselFuels.addDownstream(
            IGT_RecipeMap.newRecipeMap(
                b -> b.build()
                    .map(sLargeBoilerFakeFuels::addDieselRecipe)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList())));
        sDenseLiquidFuels.addDownstream(
            IGT_RecipeMap.newRecipeMap(
                b -> b.build()
                    .map(sLargeBoilerFakeFuels::addDenseLiquidRecipe)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList())));
    }

    @Nullable
    public static RecipeMap findRecipeMap(@Nonnull String unlocalizedName) {
        return sMappings.stream()
            .filter(m -> unlocalizedName.equals(m.mUnlocalizedName))
            .findFirst()
            .orElse(null);
    }

    /**
     * HashMap of Recipes based on their Items
     */
    public final Map<GT_ItemStack, Collection<GT_Recipe>> mRecipeItemMap = new /* Concurrent */ HashMap<>();
    /**
     * HashMap of Recipes based on their Fluids
     */
    public final Map<String, Collection<GT_Recipe>> mRecipeFluidMap = new HashMap<>();

    public final HashSet<String> mRecipeFluidNameMap = new HashSet<>();
    /**
     * The List of all Recipes
     */
    public final Collection<GT_Recipe> mRecipeList;
    /**
     * String used as an unlocalised Name.
     */
    public final String mUnlocalizedName;
    /**
     * String used in NEI for the Recipe Lists. If null it will use the unlocalised Name instead
     */
    public final String mNEIName;
    /**
     * GUI used for NEI Display. Usually the GUI of the Machine itself
     */
    public final String mNEIGUIPath;

    public final String mNEISpecialValuePre, mNEISpecialValuePost;
    public final int mUsualInputCount, mUsualOutputCount, mNEISpecialValueMultiplier, mMinimalInputItems,
        mMinimalInputFluids, mAmperage;
    public final boolean mNEIAllowed, mShowVoltageAmperageInNEI;

    /**
     * Whether to show oredict equivalent outputs when NEI is queried to show recipe
     */
    public boolean mNEIUnificateOutput = true;

    /**
     * Unique identifier for this recipe map. Generated from aUnlocalizedName and a few other parameters. See
     * constructor for details.
     */
    public final String mUniqueIdentifier;

    /**
     * Whether this recipe map contains any fluid outputs.
     */
    private boolean mHasFluidOutputs = false;

    /**
     * Whether this recipe map contains special slot inputs.
     */
    private boolean mUsesSpecialSlot = false;

    /**
     * Whether this recipemap checks for equality of special slot when searching recipe.
     */
    private boolean isSpecialSlotSensitive = false;

    /**
     * How many fluid inputs does this recipemap has at most. Currently used only for NEI slot placements and does
     * not actually restrict number of fluids used in the recipe.
     */
    private int usualFluidInputCount;

    /**
     * How many fluid outputs does this recipemap has at most. Currently used only for NEI slot placements and does
     * not actually restrict number of fluids used in the recipe.
     */
    private int usualFluidOutputCount;

    /**
     * Whether to use ModularUI for slot placements.
     */
    public boolean useModularUI = false;

    /**
     * Overlays used for GUI. 1 = If it's fluid slot. 2 = If it's output slot. 4 = If it's first slot in the same
     * section, e.g. first slot in the item output slots 8 = If it's special item slot.
     */
    private final TByteObjectMap<IDrawable> slotOverlays = new TByteObjectHashMap<>();

    /**
     * Overlays used for GUI on steam machine. 1 = If it's fluid slot. 2 = If it's output slot. 4 = If it's first
     * slot in the same section, e.g. first slot in the item output slots 8 = If it's special item slot.
     */
    private final TByteObjectMap<SteamTexture> slotOverlaysSteam = new TByteObjectHashMap<>();

    /**
     * Progressbar used for BasicMachine GUI and/or NEI. Unless specified, size should be (20, 36), consisting of
     * two parts; First is (20, 18) size of "empty" image at the top, Second is (20, 18) size of "filled" image at
     * the bottom.
     */
    private FallbackableUITexture progressBarTexture;

    /**
     * Progressbar used for steam machine GUI and/or NEI. Unless specified, size should be (20, 36), consisting of
     * two parts; First is (20, 18) size of "empty" image at the top, Second is (20, 18) size of "filled" image at
     * the bottom.
     */
    private FallbackableSteamTexture progressBarTextureSteam;

    public ProgressBar.Direction progressBarDirection = ProgressBar.Direction.RIGHT;

    public Size progressBarSize = new Size(20, 18);

    public Pos2d progressBarPos = new Pos2d(78, 24);

    public Rectangle neiTransferRect = new Rectangle(
        progressBarPos.x - (16 / 2),
        progressBarPos.y,
        progressBarSize.width + 16,
        progressBarSize.height);

    /**
     * Image size in direction of progress. Used for non-smooth rendering.
     */
    private int progressBarImageSize;

    /**
     * Additional textures shown on GUI.
     */
    public final List<Pair<IDrawable, Pair<Size, Pos2d>>> specialTextures = new ArrayList<>();

    /**
     * Additional textures shown on steam machine GUI.
     */
    public final List<Pair<SteamTexture, Pair<Size, Pos2d>>> specialTexturesSteam = new ArrayList<>();

    public IDrawable logo = GT_UITextures.PICTURE_GT_LOGO_17x17_TRANSPARENT;

    public Pos2d logoPos = new Pos2d(152, 63);

    public Size logoSize = new Size(17, 17);

    public Pos2d neiBackgroundOffset = new Pos2d(2, 3);

    public Size neiBackgroundSize = new Size(172, 82);

    protected final GT_GUIColorOverride colorOverride;
    private int neiTextColorOverride = -1;

    private INEISpecialInfoFormatter neiSpecialInfoFormatter;

    private final boolean checkForCollision = true;
    private boolean disableOptimize = false;
    private Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> recipeEmitter = this::defaultBuildRecipe;
    private Function<? super GT_Recipe, ? extends GT_Recipe> specialHandler;
    private String recipeConfigCategory;
    private Function<? super GT_Recipe, String> recipeConfigKeyConvertor;
    private final List<IGT_RecipeMap> downstreams = new ArrayList<>(0);

    /**
     * Flag if a comparator should be used to search the recipe in NEI (which is defined in {@link Power}). Else
     * only the voltage will be used to find recipes
     */
    public boolean useComparatorForNEI;

    /**
     * Whether to render the actual size of stacks or a size of 1.
     */
    public boolean renderRealStackSizes = true;

    /**
     * Initialises a new type of Recipe Handler.
     *
     * @param aRecipeList                a List you specify as Recipe List. Usually just an ArrayList with a
     *                                   pre-initialised Size.
     * @param aUnlocalizedName           the unlocalised Name of this Recipe Handler, used mainly for NEI.
     * @param aLocalName                 @deprecated the displayed Name inside the NEI Recipe GUI for optionally
     *                                   registering aUnlocalizedName
     *                                   with the language manager
     * @param aNEIGUIPath                the displayed GUI Texture, usually just a Machine GUI. Auto-Attaches ".png"
     *                                   if forgotten.
     * @param aUsualInputCount           the usual amount of Input Slots this Recipe Class has.
     * @param aUsualOutputCount          the usual amount of Output Slots this Recipe Class has.
     * @param aNEISpecialValuePre        the String in front of the Special Value in NEI.
     * @param aNEISpecialValueMultiplier the Value the Special Value is getting Multiplied with before displaying
     * @param aNEISpecialValuePost       the String after the Special Value. Usually for a Unit or something.
     * @param aNEIAllowed                if NEI is allowed to display this Recipe Handler in general.
     */
    public RecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName,
        String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
        int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
        String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
        sMappings.add(this);
        mNEIAllowed = aNEIAllowed;
        mShowVoltageAmperageInNEI = aShowVoltageAmperageInNEI;
        mRecipeList = aRecipeList;
        mNEIName = aNEIName == null ? aUnlocalizedName : aNEIName;
        mNEIGUIPath = aNEIGUIPath.endsWith(".png") ? aNEIGUIPath : aNEIGUIPath + ".png";
        mNEISpecialValuePre = aNEISpecialValuePre;
        mNEISpecialValueMultiplier = aNEISpecialValueMultiplier;
        mNEISpecialValuePost = aNEISpecialValuePost;
        mAmperage = aAmperage;
        mUsualInputCount = aUsualInputCount;
        mUsualOutputCount = aUsualOutputCount;
        mMinimalInputItems = aMinimalInputItems;
        mMinimalInputFluids = aMinimalInputFluids;
        GregTech_API.sItemStackMappings.add(mRecipeItemMap);
        mUnlocalizedName = aUnlocalizedName;
        if (aLocalName != null) {
            GT_LanguageManager.addStringLocalization(mUnlocalizedName, aLocalName);
        }
        mUniqueIdentifier = String.format(
            "%s_%d_%d_%d_%d_%d",
            aUnlocalizedName,
            aAmperage,
            aUsualInputCount,
            aUsualOutputCount,
            aMinimalInputFluids,
            aMinimalInputItems);
        progressBarTexture = new FallbackableUITexture(
            UITexture.fullImage(GregTech.ID, "gui/progressbar/" + mUnlocalizedName),
            GT_UITextures.PROGRESSBAR_ARROW);
        colorOverride = GT_GUIColorOverride.get(ModularUITextures.VANILLA_BACKGROUND.location);
        if (sIndexedMappings.put(mUniqueIdentifier, this) != null)
            throw new IllegalArgumentException("Duplicate recipe map registered: " + mUniqueIdentifier);
    }

    public RecipeMap setDisableOptimize(boolean disableOptimize) {
        this.disableOptimize = disableOptimize;
        return this;
    }

    public RecipeMap setSpecialSlotSensitive(boolean isSpecialSlotSensitive) {
        this.isSpecialSlotSensitive = isSpecialSlotSensitive;
        return this;
    }

    public RecipeMap setNEIUnificateOutput(boolean mNEIUnificateOutput) {
        this.mNEIUnificateOutput = mNEIUnificateOutput;
        return this;
    }

    public RecipeMap useComparatorForNEI(boolean use) {
        this.useComparatorForNEI = use;
        return this;
    }

    public RecipeMap setRenderRealStackSizes(boolean renderRealStackSizes) {
        this.renderRealStackSizes = renderRealStackSizes;
        return this;
    }

    public RecipeMap useModularUI(boolean use) {
        this.useModularUI = use;
        return this;
    }

    public RecipeMap setSlotOverlay(boolean isFluid, boolean isOutput, boolean isFirst, boolean isSpecial,
        IDrawable slotOverlay) {
        useModularUI(true);
        this.slotOverlays.put(
            (byte) ((isFluid ? 1 : 0) + (isOutput ? 2 : 0) + (isFirst ? 4 : 0) + (isSpecial ? 8 : 0)),
            slotOverlay);
        return this;
    }

    public RecipeMap setSlotOverlay(boolean isFluid, boolean isOutput, boolean isFirst, IDrawable slotOverlay) {
        return setSlotOverlay(isFluid, isOutput, isFirst, false, slotOverlay);
    }

    public RecipeMap setSlotOverlay(boolean isFluid, boolean isOutput, IDrawable slotOverlay) {
        return setSlotOverlay(isFluid, isOutput, true, slotOverlay)
            .setSlotOverlay(isFluid, isOutput, false, slotOverlay);
    }

    public RecipeMap setSlotOverlaySteam(boolean isFluid, boolean isOutput, boolean isFirst, boolean isSpecial,
        SteamTexture slotOverlay) {
        useModularUI(true);
        this.slotOverlaysSteam.put(
            (byte) ((isFluid ? 1 : 0) + (isOutput ? 2 : 0) + (isFirst ? 4 : 0) + (isSpecial ? 8 : 0)),
            slotOverlay);
        return this;
    }

    public RecipeMap setSlotOverlaySteam(boolean isOutput, boolean isFirst, SteamTexture slotOverlay) {
        return setSlotOverlaySteam(false, isOutput, isFirst, false, slotOverlay);
    }

    public RecipeMap setSlotOverlaySteam(boolean isOutput, SteamTexture slotOverlay) {
        return setSlotOverlaySteam(false, isOutput, true, false, slotOverlay)
            .setSlotOverlaySteam(false, isOutput, false, false, slotOverlay);
    }

    public RecipeMap setProgressBar(UITexture progressBarTexture, ProgressBar.Direction progressBarDirection) {
        return setProgressBarWithFallback(
            new FallbackableUITexture(
                UITexture.fullImage(GregTech.ID, "gui/progressbar/" + mUnlocalizedName),
                progressBarTexture),
            progressBarDirection);
    }

    public RecipeMap setProgressBar(UITexture progressBarTexture) {
        return setProgressBar(progressBarTexture, ProgressBar.Direction.RIGHT);
    }

    /**
     * Some resource packs want to use custom progress bar textures even for plain arrow. This method allows them to
     * add unique textures, yet other packs don't need to make textures for every recipemap.
     */
    public RecipeMap setProgressBarWithFallback(FallbackableUITexture progressBarTexture,
        ProgressBar.Direction progressBarDirection) {
        useModularUI(true);
        this.progressBarTexture = progressBarTexture;
        this.progressBarDirection = progressBarDirection;
        return this;
    }

    public RecipeMap setProgressBarSteam(SteamTexture progressBarTexture) {
        return setProgressBarSteamWithFallback(
            new FallbackableSteamTexture(
                SteamTexture.fullImage(GregTech.ID, "gui/progressbar/" + mUnlocalizedName + "_%s"),
                progressBarTexture));
    }

    public RecipeMap setProgressBarSteamWithFallback(FallbackableSteamTexture progressBarTexture) {
        this.progressBarTextureSteam = progressBarTexture;
        return this;
    }

    public RecipeMap setProgressBarSize(int x, int y) {
        useModularUI(true);
        this.progressBarSize = new Size(x, y);
        return this;
    }

    public RecipeMap setProgressBarPos(int x, int y) {
        useModularUI(true);
        this.progressBarPos = new Pos2d(x, y);
        return this;
    }

    public RecipeMap setProgressBarImageSize(int progressBarImageSize) {
        useModularUI(true);
        this.progressBarImageSize = progressBarImageSize;
        return this;
    }

    public RecipeMap setNEITransferRect(Rectangle neiTransferRect) {
        useModularUI(true);
        this.neiTransferRect = neiTransferRect;
        return this;
    }

    public RecipeMap addSpecialTexture(int width, int height, int x, int y, IDrawable texture) {
        useModularUI(true);
        specialTextures
            .add(new ImmutablePair<>(texture, new ImmutablePair<>(new Size(width, height), new Pos2d(x, y))));
        return this;
    }

    public RecipeMap addSpecialTextureSteam(int width, int height, int x, int y, SteamTexture texture) {
        useModularUI(true);
        specialTexturesSteam
            .add(new ImmutablePair<>(texture, new ImmutablePair<>(new Size(width, height), new Pos2d(x, y))));
        return this;
    }

    public RecipeMap setUsualFluidInputCount(int usualFluidInputCount) {
        useModularUI(true);
        this.usualFluidInputCount = usualFluidInputCount;
        return this;
    }

    public RecipeMap setUsualFluidOutputCount(int usualFluidOutputCount) {
        useModularUI(true);
        this.usualFluidOutputCount = usualFluidOutputCount;
        return this;
    }

    public RecipeMap setLogo(IDrawable logo) {
        useModularUI(true);
        this.logo = logo;
        return this;
    }

    public RecipeMap setLogoPos(int x, int y) {
        useModularUI(true);
        this.logoPos = new Pos2d(x, y);
        return this;
    }

    public RecipeMap setLogoSize(int width, int height) {
        useModularUI(true);
        this.logoSize = new Size(width, height);
        return this;
    }

    public RecipeMap setNEIBackgroundOffset(int x, int y) {
        useModularUI(true);
        this.neiBackgroundOffset = new Pos2d(x, y);
        return this;
    }

    public RecipeMap setNEIBackgroundSize(int width, int height) {
        useModularUI(true);
        this.neiBackgroundSize = new Size(width, height);
        return this;
    }

    public RecipeMap setNEISpecialInfoFormatter(INEISpecialInfoFormatter neiSpecialInfoFormatter) {
        this.neiSpecialInfoFormatter = neiSpecialInfoFormatter;
        return this;
    }

    /**
     * Change how recipes are emitted by a particular recipe builder. Can emit multiple recipe per builder.
     */
    public RecipeMap setRecipeEmitter(
        Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> func) {
        this.recipeEmitter = func;
        return this;
    }

    /**
     * Change how recipes are emitted by a particular recipe builder. Can emit multiple recipe per builder.
     * <p>
     * Unlike {@link #setRecipeEmitter(Function)}, this one does not clear the existing recipe being emitted, if any
     */
    public RecipeMap combineRecipeEmitter(
        Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> func) {
        // move recipeEmitter to local variable, so lambda capture the function itself instead of this
        Function<? super GT_RecipeBuilder, ? extends Iterable<? extends GT_Recipe>> cur = recipeEmitter;
        this.recipeEmitter = b -> Iterables.concat(cur.apply(b), func.apply(b));
        return this;
    }

    /**
     * Change how recipes are emitted by a particular recipe builder. Should not return null.
     */
    public RecipeMap setRecipeEmitterSingle(Function<? super GT_RecipeBuilder, ? extends GT_Recipe> func) {
        return setRecipeEmitter(func.andThen(Collections::singletonList));
    }

    /**
     * Change how recipes are emitted by a particular recipe builder. Effectively add a new recipe per recipe added.
     * func must not return null.
     * <p>
     * Unlike {@link #setRecipeEmitter(Function)}, this one does not clear the existing recipe being emitted, if any
     */
    public RecipeMap combineRecipeEmitterSingle(Function<? super GT_RecipeBuilder, ? extends GT_Recipe> func) {
        return combineRecipeEmitter(func.andThen(Collections::singletonList));
    }

    private static <T> Function<? super T, ? extends T> withIdentityReturn(Consumer<T> func) {
        return r -> {
            func.accept(r);
            return r;
        };
    }

    /**
     * Run a custom hook on all recipes added <b>via builder</b>. For more complicated behavior subclass this, then
     * override {@link #doAdd(GT_RecipeBuilder)}
     * <p>
     * Recipes added via one of the overloads of addRecipe will NOT be affected by this function.
     */
    public RecipeMap setRecipeSpecialHandler(Function<? super GT_Recipe, ? extends GT_Recipe> func) {
        this.specialHandler = func;
        return this;
    }

    /**
     * Run a custom hook on all recipes added <b>via builder</b>. For more complicated behavior, create a subclass
     * and override {@link #doAdd(GT_RecipeBuilder)}
     * <p>
     * Recipes added via one of the overloads of addRecipe will NOT be affected by this function.
     */
    public RecipeMap setRecipeSpecialHandler(Consumer<GT_Recipe> func) {
        return setRecipeSpecialHandler(withIdentityReturn(func));
    }

    /**
     * Run a custom hook on all recipes added <b>via builder</b>. For more complicated behavior subclass this, then
     * override {@link #doAdd(GT_RecipeBuilder)}.
     * <p>
     * Recipes added via one of the overloads of addRecipe will NOT be affected by this function.
     * <p>
     * Unlike {@link #setRecipeSpecialHandler(Function)}, this one will not replace the existing special handler.
     * The supplied function will be given the output of existing handler when a recipe is added.
     */
    public RecipeMap chainRecipeSpecialHandler(Function<? super GT_Recipe, ? extends GT_Recipe> func) {
        this.specialHandler = specialHandler == null ? func : specialHandler.andThen(func);
        return this;
    }

    /**
     * Run a custom hook on all recipes added <b>via builder</b>. For more complicated behavior subclass this, then
     * override {@link #doAdd(GT_RecipeBuilder)}.
     * <p>
     * Recipes added via one of the overloads of addRecipe will NOT be affected by this function.
     * <p>
     * Unlike {@link #setRecipeSpecialHandler(Function)}, this one will not replace the existing special handler.
     * The supplied function will be given the output of existing handler when a recipe is added.
     */
    public RecipeMap chainRecipeSpecialHandler(Consumer<GT_Recipe> func) {
        return chainRecipeSpecialHandler(withIdentityReturn(func));
    }

    public RecipeMap setRecipeConfigFile(String category, Function<? super GT_Recipe, String> keyConvertor) {
        if (StringUtils.isBlank(category) || keyConvertor == null) throw new IllegalArgumentException();
        this.recipeConfigCategory = category;
        this.recipeConfigKeyConvertor = keyConvertor;
        return this;
    }

    @Override
    public void addDownstream(IGT_RecipeMap downstream) {
        this.downstreams.add(downstream);
    }

    public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial,
        int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue) {
        return addRecipe(
            new GT_Recipe(
                aOptimize,
                aInputs,
                aOutputs,
                aSpecial,
                aOutputChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue));
    }

    public GT_Recipe addRecipe(int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs,
        int aDuration, int aEUt, int aSpecialValue) {
        return addRecipe(
            new GT_Recipe(
                false,
                null,
                null,
                null,
                aOutputChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue),
            false,
            false,
            false);
    }

    public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial,
        FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt, int aSpecialValue) {
        return addRecipe(
            new GT_Recipe(
                aOptimize,
                aInputs,
                aOutputs,
                aSpecial,
                null,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue));
    }

    public GT_Recipe addRecipe(GT_Recipe aRecipe) {
        return addRecipe(aRecipe, true, false, false);
    }

    protected GT_Recipe addRecipe(GT_Recipe aRecipe, boolean aCheckForCollisions, boolean aFakeRecipe,
        boolean aHidden) {
        aRecipe.mHidden = aHidden;
        aRecipe.mFakeRecipe = aFakeRecipe;
        if (aRecipe.mFluidInputs.length < mMinimalInputFluids && aRecipe.mInputs.length < mMinimalInputItems)
            return null;
        if (aCheckForCollisions
            && findRecipe(null, false, true, Long.MAX_VALUE, aRecipe.mFluidInputs, aRecipe.mInputs) != null)
            return null;
        return add(aRecipe);
    }

    /**
     * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes!
     * findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
     */
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
        Object aSpecial, int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration,
        int aEUt, int aSpecialValue) {
        return addFakeRecipe(
            aCheckForCollisions,
            new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                aSpecial,
                aOutputChances,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue));
    }

    /**
     * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes!
     * findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
     */
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
        Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue) {
        return addFakeRecipe(
            aCheckForCollisions,
            new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                aSpecial,
                null,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue));
    }

    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
        Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue, boolean hidden) {
        return addFakeRecipe(
            aCheckForCollisions,
            new GT_Recipe(
                false,
                aInputs,
                aOutputs,
                aSpecial,
                null,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue),
            hidden);
    }

    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, ItemStack[] aInputs, ItemStack[] aOutputs,
        Object aSpecial, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
        int aSpecialValue, ItemStack[][] aAlt, boolean hidden) {
        return addFakeRecipe(
            aCheckForCollisions,
            new GT_Recipe.GT_Recipe_WithAlt(
                false,
                aInputs,
                aOutputs,
                aSpecial,
                null,
                aFluidInputs,
                aFluidOutputs,
                aDuration,
                aEUt,
                aSpecialValue,
                aAlt),
            hidden);
    }

    /**
     * Only used for fake Recipe Handlers to show something in NEI, do not use this for adding actual Recipes!
     * findRecipe wont find fake Recipes, containsInput WILL find fake Recipes
     */
    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, GT_Recipe aRecipe) {
        return addRecipe(aRecipe, aCheckForCollisions, true, false);
    }

    public GT_Recipe addFakeRecipe(boolean aCheckForCollisions, GT_Recipe aRecipe, boolean hidden) {
        return addRecipe(aRecipe, aCheckForCollisions, true, hidden);
    }

    @Nonnull
    @Override
    public Collection<GT_Recipe> doAdd(GT_RecipeBuilder builder) {
        Iterable<? extends GT_Recipe> recipes = recipeEmitter.apply(builder);
        Collection<GT_Recipe> ret = new ArrayList<>();
        for (GT_Recipe r : recipes) {
            if (recipeConfigCategory != null) {
                String configKey = recipeConfigKeyConvertor.apply(r);
                if (configKey != null
                    && (r.mDuration = GregTech_API.sRecipeFile.get(recipeConfigCategory, configKey, r.mDuration))
                        <= 0) {
                    continue;
                }
            }
            if (r.mFluidInputs.length < mMinimalInputFluids && r.mInputs.length < mMinimalInputItems) return null;
            if (r.mSpecialValue == 0) {
                // new style cleanroom/lowgrav handling
                int specialValue = 0;
                if (builder.getMetadata(GT_RecipeConstants.LOW_GRAVITY, false)) specialValue -= 100;
                if (builder.getMetadata(GT_RecipeConstants.CLEANROOM, false)) specialValue -= 200;
                for (GT_RecipeBuilder.MetadataIdentifier<Integer> ident : SPECIAL_VALUE_ALIASES) {
                    Integer metadata = builder.getMetadata(ident, null);
                    if (metadata != null) {
                        specialValue = metadata;
                        break;
                    }
                }
                r.mSpecialValue = specialValue;
            }
            if (specialHandler != null) r = specialHandler.apply(r);
            if (r == null) continue;
            if (checkForCollision && findRecipe(null, false, true, Long.MAX_VALUE, r.mFluidInputs, r.mInputs) != null) {
                StringBuilder errorInfo = new StringBuilder();
                boolean hasAnEntry = false;
                for (FluidStack fStack : r.mFluidInputs) {
                    if (fStack == null) {
                        continue;
                    }
                    String s = fStack.getLocalizedName();
                    if (s == null) {
                        continue;
                    }
                    if (hasAnEntry) {
                        errorInfo.append("+")
                            .append(s);
                    } else {
                        errorInfo.append(s);
                    }
                    hasAnEntry = true;
                }
                for (ItemStack iStack : r.mInputs) {
                    if (iStack == null) {
                        continue;
                    }
                    String s = iStack.getDisplayName();
                    if (hasAnEntry) {
                        errorInfo.append("+")
                            .append(s);
                    } else {
                        errorInfo.append(s);
                    }
                    hasAnEntry = true;
                }
                handleRecipeCollision(errorInfo.toString());
                continue;
            }
            ret.add(add(r));
        }
        if (!ret.isEmpty()) {
            builder.clearInvalid();
            for (IGT_RecipeMap downstream : downstreams) {
                downstream.doAdd(builder);
            }
        }
        return ret;
    }

    public final Iterable<? extends GT_Recipe> defaultBuildRecipe(GT_RecipeBuilder builder) {
        // TODO sensible validation
        GT_RecipeBuilder b = builder;
        if (disableOptimize && builder.isOptimize()) {
            b = copy(builder, b).noOptimize();
        }
        return buildOrEmpty(b);
    }

    private static GT_RecipeBuilder copy(GT_RecipeBuilder original, GT_RecipeBuilder b) {
        return b == original ? b.copy() : b;
    }

    public GT_Recipe add(GT_Recipe aRecipe) {
        mRecipeList.add(aRecipe);
        for (FluidStack aFluid : aRecipe.mFluidInputs) {
            if (aFluid != null) {
                Collection<GT_Recipe> tList = mRecipeFluidMap.computeIfAbsent(
                    aFluid.getFluid()
                        .getName(),
                    k -> new HashSet<>(1));
                tList.add(aRecipe);
                mRecipeFluidNameMap.add(
                    aFluid.getFluid()
                        .getName());
            }
        }
        if (aRecipe.mFluidOutputs.length != 0) {
            this.mHasFluidOutputs = true;
        }
        if (aRecipe.mSpecialItems != null) {
            this.mUsesSpecialSlot = true;
        }
        return addToItemMap(aRecipe);
    }

    public void reInit() {
        mRecipeItemMap.clear();
        for (GT_Recipe tRecipe : mRecipeList) {
            GT_OreDictUnificator.setStackArray(true, tRecipe.mInputs);
            GT_OreDictUnificator.setStackArray(true, tRecipe.mOutputs);
            addToItemMap(tRecipe);
        }
    }

    /**
     * @return if this Item is a valid Input for any for the Recipes
     */
    public boolean containsInput(ItemStack aStack) {
        return aStack != null && (mRecipeItemMap.containsKey(new GT_ItemStack(aStack))
            || mRecipeItemMap.containsKey(new GT_ItemStack(aStack, true)));
    }

    /**
     * @return if this Fluid is a valid Input for any for the Recipes
     */
    public boolean containsInput(FluidStack aFluid) {
        return aFluid != null && containsInput(aFluid.getFluid());
    }

    /**
     * @return if this Fluid is a valid Input for any for the Recipes
     */
    public boolean containsInput(Fluid aFluid) {
        return aFluid != null && mRecipeFluidNameMap.contains(aFluid.getName());
    }

    @Nullable
    public final GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, boolean aNotUnificated, long aVoltage,
        FluidStack[] aFluids, ItemStack... aInputs) {
        return findRecipe(aTileEntity, null, aNotUnificated, aVoltage, aFluids, null, aInputs);
    }

    @Nullable
    public final GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, boolean aNotUnificated,
        boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack... aInputs) {
        return findRecipe(aTileEntity, null, aNotUnificated, aDontCheckStackSizes, aVoltage, aFluids, null, aInputs);
    }

    @Nullable
    public final GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GT_Recipe aRecipe, boolean aNotUnificated,
        long aVoltage, FluidStack[] aFluids, ItemStack... aInputs) {
        return findRecipe(aTileEntity, aRecipe, aNotUnificated, aVoltage, aFluids, null, aInputs);
    }

    @Nullable
    public final GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GT_Recipe aRecipe, boolean aNotUnificated,
        boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack... aInputs) {
        return findRecipe(aTileEntity, aRecipe, aNotUnificated, aDontCheckStackSizes, aVoltage, aFluids, null, aInputs);
    }

    @Nullable
    public final GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GT_Recipe aRecipe, boolean aNotUnificated,
        long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot, ItemStack... aInputs) {
        return findRecipe(aTileEntity, aRecipe, aNotUnificated, false, aVoltage, aFluids, aSpecialSlot, aInputs);
    }

    // TODO: make this final after migrating BW
    @SuppressWarnings("unused")
    @Nullable
    public GT_Recipe findRecipe(IHasWorldObjectAndCoords aTileEntity, GT_Recipe aRecipe, boolean aNotUnificated,
        boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
        ItemStack... aInputs) {
        FindRecipeResult result = findRecipeWithResult(
            aRecipe,
            aNotUnificated,
            aDontCheckStackSizes,
            aVoltage,
            aFluids,
            aSpecialSlot,
            aInputs);
        return result.isSuccessful() ? result.getRecipe() : null;
    }

    /**
     * finds a Recipe matching the aFluid and ItemStack Inputs.
     *
     * @param aRecipe              in case this is != null it will try to use this Recipe first when looking things
     *                             up.
     * @param aNotUnificated       if this is T the Recipe searcher will unificate the ItemStack Inputs
     * @param aDontCheckStackSizes if set to false will only return recipes that can be executed at least once with
     *                             the provided input
     * @param aVoltage             Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
     * @param aFluids              the Fluid Inputs
     * @param aSpecialSlot         the content of the Special Slot, the regular Manager doesn't do anything with
     *                             this, but some custom ones do.
     * @param aInputs              the Item Inputs
     * @return Result of the recipe search
     */
    @Nonnull
    public final FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, boolean aNotUnificated,
        boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids, ItemStack aSpecialSlot,
        ItemStack... aInputs) {
        return findRecipeWithResult(
            aRecipe,
            recipe -> aVoltage * mAmperage >= recipe.mEUt,
            aNotUnificated,
            aDontCheckStackSizes,
            aVoltage,
            aFluids,
            aSpecialSlot,
            aInputs);
    }

    /**
     * finds a Recipe matching the aFluid and ItemStack Inputs.
     *
     * @param aRecipe              in case this is != null it will try to use this Recipe first when looking things
     *                             up.
     * @param aIsValidRecipe       predicate to help identify, if the recipe matches our machine
     * @param aNotUnificated       if this is T the Recipe searcher will unificate the ItemStack Inputs
     * @param aDontCheckStackSizes if set to false will only return recipes that can be executed at least once with
     *                             the provided input
     * @param aVoltage             Voltage of the Machine or Long.MAX_VALUE if it has no Voltage
     * @param aFluids              the Fluid Inputs
     * @param aSpecialSlot         the content of the Special Slot, the regular Manager doesn't do anything with
     *                             this, but some custom ones do.
     * @param aInputs              the Item Inputs
     * @return Result of the recipe search
     */
    @Nonnull
    public FindRecipeResult findRecipeWithResult(GT_Recipe aRecipe, Predicate<GT_Recipe> aIsValidRecipe,
        boolean aNotUnificated, boolean aDontCheckStackSizes, long aVoltage, FluidStack[] aFluids,
        ItemStack aSpecialSlot, ItemStack... aInputs) {
        // No Recipes? Well, nothing to be found then.
        if (mRecipeList.isEmpty()) return NOT_FOUND;

        // Some Recipe Classes require a certain amount of Inputs of certain kinds. Like "at least 1 Fluid + 1
        // Stack" or "at least 2 Stacks" before they start searching for Recipes.
        // This improves Performance massively, especially if people leave things like Circuits, Molds or Shapes in
        // their Machines to select Sub Recipes.
        if (GregTech_API.sPostloadFinished) {
            if (mMinimalInputFluids > 0) {
                if (aFluids == null) return NOT_FOUND;
                int tAmount = 0;
                for (FluidStack aFluid : aFluids) if (aFluid != null) tAmount++;
                if (tAmount < mMinimalInputFluids) return NOT_FOUND;
            }
            if (mMinimalInputItems > 0) {
                if (aInputs == null) return NOT_FOUND;
                int tAmount = 0;
                for (ItemStack aInput : aInputs) if (aInput != null) tAmount++;
                if (tAmount < mMinimalInputItems) return NOT_FOUND;
            }
        }

        // Unification happens here in case the Input isn't already unificated.
        if (aNotUnificated) aInputs = GT_OreDictUnificator.getStackArray(true, (Object[]) aInputs);

        // Check the Recipe which has been used last time in order to not have to search for it again, if possible.
        if (aRecipe != null) if (!aRecipe.mFakeRecipe && aRecipe.mCanBeBuffered
            && aRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                if (!isSpecialSlotSensitive || areStacksEqualOrNull((ItemStack) aRecipe.mSpecialItems, aSpecialSlot)) {
                    if (aRecipe.mEnabled && aIsValidRecipe.test(aRecipe)) {
                        return ofSuccess(aRecipe);
                    }
                }
            }

        // Now look for the Recipes inside the Item HashMaps, but only when the Recipes usually have Items.
        if (mUsualInputCount > 0 && aInputs != null) for (ItemStack tStack : aInputs) if (tStack != null) {
            Collection<GT_Recipe> tRecipes = mRecipeItemMap.get(new GT_ItemStack(tStack));
            if (tRecipes != null) for (GT_Recipe tRecipe : tRecipes)
                if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                    if (!isSpecialSlotSensitive
                        || areStacksEqualOrNull((ItemStack) tRecipe.mSpecialItems, aSpecialSlot)) {
                        if (tRecipe.mEnabled && aIsValidRecipe.test(tRecipe)) {
                            return ofSuccess(tRecipe);
                        }
                    }
                }
            tRecipes = mRecipeItemMap.get(new GT_ItemStack(tStack, true));
            if (tRecipes != null) for (GT_Recipe tRecipe : tRecipes)
                if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                    if (!isSpecialSlotSensitive
                        || areStacksEqualOrNull((ItemStack) tRecipe.mSpecialItems, aSpecialSlot)) {
                        if (tRecipe.mEnabled && aIsValidRecipe.test(tRecipe)) {
                            return ofSuccess(tRecipe);
                        }
                    }
                }
        }

        // If the minimal Amount of Items for the Recipe is 0, then it could be a Fluid-Only Recipe, so check that
        // Map too.
        if (mMinimalInputItems == 0 && aFluids != null) for (FluidStack aFluid : aFluids) if (aFluid != null) {
            Collection<GT_Recipe> tRecipes = mRecipeFluidMap.get(
                aFluid.getFluid()
                    .getName());
            if (tRecipes != null) for (GT_Recipe tRecipe : tRecipes)
                if (!tRecipe.mFakeRecipe && tRecipe.isRecipeInputEqual(false, aDontCheckStackSizes, aFluids, aInputs)) {
                    if (!isSpecialSlotSensitive
                        || areStacksEqualOrNull((ItemStack) tRecipe.mSpecialItems, aSpecialSlot)) {
                        if (tRecipe.mEnabled && aIsValidRecipe.test(tRecipe)) {
                            return ofSuccess(tRecipe);
                        }
                    }
                }
        }

        // And nothing has been found.
        return NOT_FOUND;
    }

    protected GT_Recipe addToItemMap(GT_Recipe aRecipe) {
        for (ItemStack aStack : aRecipe.mInputs) if (aStack != null) {
            GT_ItemStack tStack = new GT_ItemStack(aStack);
            Collection<GT_Recipe> tList = mRecipeItemMap.computeIfAbsent(tStack, k -> new HashSet<>(1));
            tList.add(aRecipe);
        }
        return aRecipe;
    }

    /**
     * Whether this recipe map contains any fluid outputs.
     */
    public boolean hasFluidOutputs() {
        return mHasFluidOutputs;
    }

    /**
     * Whether this recipe map contains any fluid inputs.
     */
    public boolean hasFluidInputs() {
        return mRecipeFluidNameMap.size() != 0;
    }

    /**
     * Whether this recipe map contains special slot inputs.
     */
    public boolean usesSpecialSlot() {
        return mUsesSpecialSlot;
    }

    public int getUsualFluidInputCount() {
        return Math.max(usualFluidInputCount, hasFluidInputs() ? 1 : 0);
    }

    public int getUsualFluidOutputCount() {
        return Math.max(usualFluidOutputCount, hasFluidOutputs() ? 1 : 0);
    }

    @Nullable
    public IDrawable getOverlayForSlot(boolean isFluid, boolean isOutput, int index, boolean isSpecial) {
        byte overlayKey = (byte) ((isFluid ? 1 : 0) + (isOutput ? 2 : 0) + (index == 0 ? 4 : 0) + (isSpecial ? 8 : 0));
        if (slotOverlays.containsKey(overlayKey)) {
            return slotOverlays.get(overlayKey);
        }
        return null;
    }

    @Nullable
    public SteamTexture getOverlayForSlotSteam(boolean isFluid, boolean isOutput, int index, boolean isSpecial) {
        byte overlayKey = (byte) ((isFluid ? 1 : 0) + (isOutput ? 2 : 0) + (index == 0 ? 4 : 0) + (isSpecial ? 8 : 0));
        if (slotOverlaysSteam.containsKey(overlayKey)) {
            return slotOverlaysSteam.get(overlayKey);
        }
        return null;
    }

    @Nullable
    public SteamTexture getOverlayForSlotSteam(boolean isOutput, boolean isFirst) {
        byte overlayKey = (byte) ((isOutput ? 2 : 0) + (isFirst ? 4 : 0));
        if (slotOverlaysSteam.containsKey(overlayKey)) {
            return slotOverlaysSteam.get(overlayKey);
        }
        return null;
    }

    public UITexture getProgressBarTexture() {
        return progressBarTexture.get();
    }

    public FallbackableUITexture getProgressBarTextureRaw() {
        return progressBarTexture;
    }

    public UITexture getProgressBarTextureSteam(SteamVariant steamVariant) {
        return progressBarTextureSteam.get(steamVariant);
    }

    public int getProgressBarImageSize() {
        if (progressBarImageSize != 0) {
            return progressBarImageSize;
        }
        return switch (progressBarDirection) {
            case UP, DOWN -> progressBarSize.height;
            case CIRCULAR_CW -> Math.max(progressBarSize.width, progressBarSize.height);
            default -> progressBarSize.width;
        };
    }

    /**
     * Adds slot backgrounds, progressBar, etc.
     */
    public ModularWindow.Builder createNEITemplate(IItemHandlerModifiable itemInputsInventory,
        IItemHandlerModifiable itemOutputsInventory, IItemHandlerModifiable specialSlotInventory,
        IItemHandlerModifiable fluidInputsInventory, IItemHandlerModifiable fluidOutputsInventory,
        Supplier<Float> progressSupplier, Pos2d windowOffset) {
        ModularWindow.Builder builder = ModularWindow.builder(neiBackgroundSize)
            .setBackground(ModularUITextures.VANILLA_BACKGROUND);

        UIHelper.forEachSlots(
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(itemInputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(itemOutputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            (i, backgrounds, pos) -> {
                if (usesSpecialSlot()) builder.widget(
                    SlotWidget.phantom(specialSlotInventory, 0)
                        .setBackground(backgrounds)
                        .setPos(pos)
                        .setSize(18, 18));
            },
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(fluidInputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            (i, backgrounds, pos) -> builder.widget(
                SlotWidget.phantom(fluidOutputsInventory, i)
                    .setBackground(backgrounds)
                    .setPos(pos)
                    .setSize(18, 18)),
            ModularUITextures.ITEM_SLOT,
            ModularUITextures.FLUID_SLOT,
            this,
            mUsualInputCount,
            mUsualOutputCount,
            getUsualFluidInputCount(),
            getUsualFluidOutputCount(),
            SteamVariant.NONE,
            windowOffset);

        addProgressBarUI(builder, progressSupplier, windowOffset);
        addGregTechLogoUI(builder, windowOffset);

        for (Pair<IDrawable, Pair<Size, Pos2d>> specialTexture : specialTextures) {
            builder.widget(
                new DrawableWidget().setDrawable(specialTexture.getLeft())
                    .setSize(
                        specialTexture.getRight()
                            .getLeft())
                    .setPos(
                        specialTexture.getRight()
                            .getRight()
                            .add(windowOffset)));
        }

        return builder;
    }

    public void addProgressBarUI(ModularWindow.Builder builder, Supplier<Float> progressSupplier, Pos2d windowOffset) {
        builder.widget(
            new ProgressBar().setTexture(getProgressBarTexture(), 20)
                .setDirection(progressBarDirection)
                .setProgress(progressSupplier)
                .setSynced(false, false)
                .setPos(progressBarPos.add(windowOffset))
                .setSize(progressBarSize));
    }

    public void addGregTechLogoUI(ModularWindow.Builder builder, Pos2d windowOffset) {
        builder.widget(
            new DrawableWidget().setDrawable(logo)
                .setSize(logoSize)
                .setPos(logoPos.add(windowOffset)));
    }

    public void addRecipeSpecificDrawable(ModularWindow.Builder builder, Pos2d windowOffset,
        Supplier<IDrawable> supplier, Pos2d pos, Size size) {
        builder.widget(
            new DrawableWidget().setDrawable(supplier)
                .setSize(size)
                .setPos(pos.add(windowOffset)));
    }

    /**
     * Overriding this method allows custom NEI stack placement
     */
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getItemInputPositions(itemInputCount);
    }

    /**
     * Overriding this method allows custom NEI stack placement
     */
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getItemOutputPositions(itemOutputCount);
    }

    /**
     * Overriding this method allows custom NEI stack placement
     */
    public Pos2d getSpecialItemPosition() {
        return UIHelper.getSpecialItemPosition();
    }

    /**
     * Overriding this method allows custom NEI stack placement
     */
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getFluidInputPositions(fluidInputCount);
    }

    /**
     * Overriding this method allows custom NEI stack placement
     */
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getFluidOutputPositions(fluidOutputCount);
    }

    public void drawNEIDescription(NEIRecipeInfo recipeInfo) {
        drawNEIEnergyInfo(recipeInfo);
        drawNEIDurationInfo(recipeInfo);
        drawNEISpecialInfo(recipeInfo);
        drawNEIRecipeOwnerInfo(recipeInfo);
    }

    protected void drawNEIEnergyInfo(NEIRecipeInfo recipeInfo) {
        GT_Recipe recipe = recipeInfo.recipe;
        Power power = recipeInfo.power;
        if (power.getEuPerTick() > 0) {
            drawNEIText(recipeInfo, trans("152", "Total: ") + power.getTotalPowerString());

            String amperage = power.getAmperageString();
            String powerUsage = power.getPowerUsageString();
            if (amperage == null || amperage.equals("unspecified") || powerUsage.contains("(OC)")) {
                drawNEIText(recipeInfo, trans("153", "Usage: ") + powerUsage);
                if (GT_Mod.gregtechproxy.mNEIOriginalVoltage) {
                    Power originalPower = getPowerFromRecipeMap();
                    if (!(originalPower instanceof UnspecifiedEUPower)) {
                        originalPower.computePowerUsageAndDuration(recipe.mEUt, recipe.mDuration);
                        drawNEIText(recipeInfo, trans("275", "Original voltage: ") + originalPower.getVoltageString());
                    }
                }
                if (amperage != null && !amperage.equals("unspecified") && !amperage.equals("1")) {
                    drawNEIText(recipeInfo, trans("155", "Amperage: ") + amperage);
                }
            } else if (amperage.equals("1")) {
                drawNEIText(recipeInfo, trans("154", "Voltage: ") + power.getVoltageString());
            } else {
                drawNEIText(recipeInfo, trans("153", "Usage: ") + powerUsage);
                drawNEIText(recipeInfo, trans("154", "Voltage: ") + power.getVoltageString());
                drawNEIText(recipeInfo, trans("155", "Amperage: ") + amperage);
            }
        }
    }

    protected void drawNEIDurationInfo(NEIRecipeInfo recipeInfo) {
        Power power = recipeInfo.power;
        if (power.getDurationTicks() > 0) {
            String textToDraw = trans("158", "Time: ");
            if (GT_Mod.gregtechproxy.mNEIRecipeSecondMode) {
                textToDraw += power.getDurationStringSeconds();
                if (power.getDurationSeconds() <= 1.0d) {
                    textToDraw += String.format(" (%s)", power.getDurationStringTicks());
                }
            } else {
                textToDraw += power.getDurationStringTicks();
            }
            drawNEIText(recipeInfo, textToDraw);
        }
    }

    protected void drawNEISpecialInfo(NEIRecipeInfo recipeInfo) {
        String[] recipeDesc = recipeInfo.recipe.getNeiDesc();
        if (recipeDesc != null) {
            for (String s : recipeDesc) {
                drawOptionalNEIText(recipeInfo, s);
            }
        } else if (neiSpecialInfoFormatter != null) {
            drawNEITextMultipleLines(recipeInfo, neiSpecialInfoFormatter.format(recipeInfo, this::formatSpecialValue));
        } else {
            drawOptionalNEIText(recipeInfo, getNEISpecialInfo(recipeInfo.recipe.mSpecialValue));
        }
    }

    protected String getNEISpecialInfo(int specialValue) {
        if (specialValue == -100 && GT_Mod.gregtechproxy.mLowGravProcessing) {
            return trans("159", "Needs Low Gravity");
        } else if (specialValue == -200 && GT_Mod.gregtechproxy.mEnableCleanroom) {
            return trans("160", "Needs Cleanroom");
        } else if (specialValue == -201) {
            return trans("206", "Scan for Assembly Line");
        } else if (specialValue == -300 && GT_Mod.gregtechproxy.mEnableCleanroom) {
            return trans("160.1", "Needs Cleanroom & LowGrav");
        } else if (specialValue == -400) {
            return trans("216", "Deprecated Recipe");
        } else if (hasSpecialValueFormat()) {
            return formatSpecialValue(specialValue);
        }
        return null;
    }

    private boolean hasSpecialValueFormat() {
        return (isStringValid(mNEISpecialValuePre)) || (isStringValid(mNEISpecialValuePost));
    }

    protected String formatSpecialValue(int specialValue) {
        return mNEISpecialValuePre + formatNumbers((long) specialValue * mNEISpecialValueMultiplier)
            + mNEISpecialValuePost;
    }

    protected void drawNEIRecipeOwnerInfo(NEIRecipeInfo recipeInfo) {
        GT_Recipe recipe = recipeInfo.recipe;
        if (GT_Mod.gregtechproxy.mNEIRecipeOwner) {
            if (recipe.owners.size() > 1) {
                drawNEIText(
                    recipeInfo,
                    EnumChatFormatting.ITALIC + trans("273", "Original Recipe by: ")
                        + recipe.owners.get(0)
                            .getName());
                for (int i = 1; i < recipe.owners.size(); i++) {
                    drawNEIText(
                        recipeInfo,
                        EnumChatFormatting.ITALIC + trans("274", "Modified by: ")
                            + recipe.owners.get(i)
                                .getName());
                }
            } else if (recipe.owners.size() > 0) {
                drawNEIText(
                    recipeInfo,
                    EnumChatFormatting.ITALIC + trans("272", "Recipe by: ")
                        + recipe.owners.get(0)
                            .getName());
            }
        }
        if (GT_Mod.gregtechproxy.mNEIRecipeOwnerStackTrace && recipe.stackTraces != null
            && !recipe.stackTraces.isEmpty()) {
            drawNEIText(recipeInfo, "stackTrace:");
            // todo: good way to show all stacktraces
            for (StackTraceElement stackTrace : recipe.stackTraces.get(0)) {
                drawNEIText(recipeInfo, stackTrace.toString());
            }
        }
    }

    protected void drawNEIText(NEIRecipeInfo recipeInfo, String text) {
        drawNEIText(recipeInfo, text, 10);
    }

    /**
     * Draws text on NEI recipe.
     *
     * @param yShift y position to shift after this text
     */
    @SuppressWarnings("SameParameterValue")
    protected void drawNEIText(NEIRecipeInfo recipeInfo, String text, int yShift) {
        drawNEIText(recipeInfo, text, 10, yShift);
    }

    /**
     * Draws text on NEI recipe.
     *
     * @param xStart x position to start drawing
     * @param yShift y position to shift after this text
     */
    @SuppressWarnings("SameParameterValue")
    protected void drawNEIText(NEIRecipeInfo recipeInfo, String text, int xStart, int yShift) {
        net.minecraft.client.Minecraft.getMinecraft().fontRenderer
            .drawString(text, xStart, recipeInfo.yPos, neiTextColorOverride != -1 ? neiTextColorOverride : 0x000000);
        recipeInfo.yPos += yShift;
    }

    protected void drawOptionalNEIText(NEIRecipeInfo recipeInfo, String text) {
        if (isStringValid(text) && !text.equals("unspecified")) {
            drawNEIText(recipeInfo, text, 10);
        }
    }

    protected void drawNEITextMultipleLines(NEIRecipeInfo recipeInfo, List<String> texts) {
        for (String text : texts) {
            drawNEIText(recipeInfo, text, 10);
        }
    }

    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        for (PositionedStack pStack : neiCachedRecipe.mInputs) {
            if (stack == pStack.item) {
                if (pStack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                    currentTip = handleNEIItemInputTooltip(
                        currentTip,
                        (GT_NEI_DefaultHandler.FixedPositionedStack) pStack);
                }
                break;
            }
        }
        for (PositionedStack pStack : neiCachedRecipe.mOutputs) {
            if (stack == pStack.item) {
                if (pStack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                    currentTip = handleNEIItemOutputTooltip(
                        currentTip,
                        (GT_NEI_DefaultHandler.FixedPositionedStack) pStack);
                }
                break;
            }
        }
        return currentTip;
    }

    protected List<String> handleNEIItemInputTooltip(List<String> currentTip,
        GT_NEI_DefaultHandler.FixedPositionedStack pStack) {
        if (pStack.isNotConsumed()) {
            currentTip.add(GRAY + trans("151", "Does not get consumed in the process"));
        }
        return currentTip;
    }

    protected List<String> handleNEIItemOutputTooltip(List<String> currentTip,
        GT_NEI_DefaultHandler.FixedPositionedStack pStack) {
        if (pStack.isChanceBased()) {
            currentTip.add(GRAY + trans("150", "Chance: ") + pStack.getChanceText());
        }
        return currentTip;
    }

    public void drawNEIOverlays(GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        for (PositionedStack stack : neiCachedRecipe.mInputs) {
            if (stack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                drawNEIOverlayForInput((GT_NEI_DefaultHandler.FixedPositionedStack) stack);
            }
        }
        for (PositionedStack stack : neiCachedRecipe.mOutputs) {
            if (stack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                drawNEIOverlayForOutput((GT_NEI_DefaultHandler.FixedPositionedStack) stack);
            }
        }
    }

    protected void drawNEIOverlayForInput(GT_NEI_DefaultHandler.FixedPositionedStack stack) {
        if (stack.isNotConsumed()) {
            drawNEIOverlayText("NC", stack);
        }
    }

    protected void drawNEIOverlayForOutput(GT_NEI_DefaultHandler.FixedPositionedStack stack) {
        if (stack.isChanceBased()) {
            drawNEIOverlayText(stack.getChanceText(), stack);
        }
    }

    @SuppressWarnings("SameParameterValue")
    protected void drawNEIOverlayText(String text, PositionedStack stack, int color, float scale, boolean shadow,
        Alignment alignment) {
        FontRenderer fontRenderer = net.minecraft.client.Minecraft.getMinecraft().fontRenderer;
        int width = fontRenderer.getStringWidth(text);
        int x = (int) ((stack.relx + 8 + 8 * alignment.x) / scale) - (width / 2 * (alignment.x + 1));
        int y = (int) ((stack.rely + 8 + 8 * alignment.y) / scale) - (fontRenderer.FONT_HEIGHT / 2 * (alignment.y + 1))
            - (alignment.y - 1) / 2;

        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1);
        fontRenderer.drawString(text, x, y, color, shadow);
        GlStateManager.popMatrix();
    }

    protected void drawNEIOverlayText(String text, PositionedStack stack) {
        drawNEIOverlayText(
            text,
            stack,
            colorOverride.getTextColorOrDefault("nei_overlay_yellow", 0xFDD835),
            0.5f,
            false,
            Alignment.TopLeft);
    }

    public void updateNEITextColorOverride() {
        neiTextColorOverride = colorOverride.getTextColorOrDefault("nei", -1);
    }

    public Power getPowerFromRecipeMap() {
        // By default, assume generic EU LV power with no overclocks
        Power power;
        if (mShowVoltageAmperageInNEI) {
            power = new EUPower((byte) 1, mAmperage);
        } else {
            power = new UnspecifiedEUPower((byte) 1, mAmperage);
        }
        return power;
    }

    public void addRecipe(Object o, FluidStack[] fluidInputArray, FluidStack[] fluidOutputArray) {}
}
