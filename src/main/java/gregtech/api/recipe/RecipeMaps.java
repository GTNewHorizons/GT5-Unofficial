package gregtech.api.recipe;

import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.Mods.*;
import static gregtech.api.util.GT_RecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GT_RecipeMapUtil.*;
import static gregtech.api.util.GT_Utility.*;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.objects.ItemData;
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
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.api.util.GT_RecipeMapUtil;
import gregtech.api.util.GT_Utility;
import gregtech.nei.FusionSpecialValueFormatter;
import gregtech.nei.HeatingCoilSpecialValueFormatter;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;

public class RecipeMaps {

    public static final RecipeMap oreWasherRecipes = new RecipeMap(
        new HashSet<>(500),
        "gt.recipe.orewasher",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "OreWasher"),
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
    public static final RecipeMap thermalCentrifugeRecipes = new RecipeMap(
        new HashSet<>(1000),
        "gt.recipe.thermalcentrifuge",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "ThermalCentrifuge"),
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
    public static final RecipeMap compressorRecipes = new RecipeMap(
        new HashSet<>(750),
        "gt.recipe.compressor",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Compressor"),
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
    public static final RecipeMap extractorRecipes = new RecipeMap(
        new HashSet<>(250),
        "gt.recipe.extractor",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Extractor"),
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
    public static final RecipeMap recyclerRecipes = new RecyclerRecipeMap(
        new HashSet<>(0),
        "ic.recipe.recycler",
        null,
        "ic2.recycler",
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Recycler"),
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
    public static final RecipeMap furnaceRecipes = new FurnaceRecipeMap(
        new HashSet<>(0),
        "mc.recipe.furnace",
        "Furnace",
        "smelting",
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "E_Furnace"),
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
    public static final RecipeMap microwaveRecipes = new MicrowaveRecipeMap(
        new HashSet<>(0),
        "gt.recipe.microwave",
        null,
        "smelting",
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "E_Furnace"),
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
    public static final RecipeMap scannerFakeRecipes = new RecipeMap(
        new HashSet<>(300),
        "gt.recipe.scanner",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Scanner"),
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
    public static final RecipeMap rockBreakerFakeRecipes = new RecipeMap(
        new HashSet<>(200),
        "gt.recipe.rockbreaker",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "RockBreaker"),
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
    public static final RecipeMap replicatorFakeRecipes = new ReplicatorFakeMap(
        new HashSet<>(100),
        "gt.recipe.replicator",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Replicator"),
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
    public static final RecipeMap assemblylineVisualRecipes = new AssemblyLineFakeRecipeMap(
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
    public static final RecipeMap plasmaArcFurnaceRecipes = new RecipeMap(
        new HashSet<>(20000),
        "gt.recipe.plasmaarcfurnace",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "PlasmaArcFurnace"),
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
    public static final RecipeMap arcFurnaceRecipes = new RecipeMap(
        new HashSet<>(20000),
        "gt.recipe.arcfurnace",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "ArcFurnace"),
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
    public static final RecipeMap printerRecipes = new PrinterRecipeMap(
        new HashSet<>(5),
        "gt.recipe.printer",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Printer"),
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
    public static final RecipeMap sifterRecipes = new RecipeMap(
        new HashSet<>(105),
        "gt.recipe.sifter",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Sifter"),
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
    public static final RecipeMap formingPressRecipes = new FormingPressRecipeMap(
        new HashSet<>(300),
        "gt.recipe.press",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Press3"),
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
    public static final RecipeMap laserEngraverRecipes = new RecipeMap(
        new HashSet<>(810),
        "gt.recipe.laserengraver",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "LaserEngraver2"),
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
    public static final RecipeMap mixerRecipes = new RecipeMap(
        new HashSet<>(900),
        "gt.recipe.mixer",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Mixer6"),
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
    public static final RecipeMap autoclaveRecipes = new RecipeMap(
        new HashSet<>(300),
        "gt.recipe.autoclave",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Autoclave4"),
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
    public static final RecipeMap electroMagneticSeparatorRecipes = new RecipeMap(
        new HashSet<>(50),
        "gt.recipe.electromagneticseparator",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "ElectromagneticSeparator"),
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
    public static final RecipeMap polarizerRecipes = new RecipeMap(
        new HashSet<>(300),
        "gt.recipe.polarizer",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Polarizer"),
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
    public static final RecipeMap maceratorRecipes = new MaceratorRecipeMap(
        new HashSet<>(16600),
        "gt.recipe.macerator",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Macerator4"),
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
    public static final RecipeMap chemicalBathRecipes = new RecipeMap(
        new HashSet<>(2550),
        "gt.recipe.chemicalbath",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "ChemicalBath"),
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
    public static final RecipeMap fluidCannerRecipes = new FluidCannerRecipeMap(
        new HashSet<>(2100),
        "gt.recipe.fluidcanner",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "FluidCanner"),
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
    public static final RecipeMap brewingRecipes = new RecipeMap(
        new HashSet<>(450),
        "gt.recipe.brewer",
        "Brewing Machine",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "PotionBrewer"),
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
    public static final RecipeMap fluidHeaterRecipes = new RecipeMap(
        new HashSet<>(10),
        "gt.recipe.fluidheater",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "FluidHeater"),
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
    public static final RecipeMap distilleryRecipes = new RecipeMap(
        new HashSet<>(400),
        "gt.recipe.distillery",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Distillery"),
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
    public static final RecipeMap fermentingRecipes = new RecipeMap(
        new HashSet<>(50),
        "gt.recipe.fermenter",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Fermenter"),
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
    public static final RecipeMap fluidSolidfierRecipes = new RecipeMap(
        new HashSet<>(35000),
        "gt.recipe.fluidsolidifier",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "FluidSolidifier"),
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
    public static final RecipeMap fluidExtractionRecipes = new RecipeMap(
        new HashSet<>(15000),
        "gt.recipe.fluidextractor",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "FluidExtractor"),
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
    public static final RecipeMap packagerRecipes = new RecipeMap(
        new HashSet<>(2500),
        "gt.recipe.packager",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Packager"),
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
    public static final RecipeMap unpackagerRecipes = new UnboxinatorRecipeMap(
        new HashSet<>(2500),
        "gt.recipe.unpackager",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Unpackager"),
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
    public static final RecipeMap fusionRecipes = new FluidOnlyDisplayRecipeMap(
        new HashSet<>(50),
        "gt.recipe.fusionreactor",
        "Fusion Reactor",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "FusionReactor"),
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
    public static final RecipeMap centrifugeRecipes = new RecipeMap(
        new HashSet<>(1200),
        "gt.recipe.centrifuge",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Centrifuge"),
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
    public static final RecipeMap electrolyzerRecipes = new RecipeMap(
        new HashSet<>(300),
        "gt.recipe.electrolyzer",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Electrolyzer"),
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
    public static final RecipeMap blastRecipes = new RecipeMap(
        new HashSet<>(800),
        "gt.recipe.blastfurnace",
        "Blast Furnace",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final RecipeMap plasmaForgeRecipes = new LargeNEIDisplayRecipeMap(
        new HashSet<>(20),
        "gt.recipe.plasmaforge",
        "DTPF",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "PlasmaForge"),
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
    public static final RecipeMap transcendentPlasmaMixerRecipes = new TranscendentPlasmaMixerRecipeMap(
        new HashSet<>(20),
        "gt.recipe.transcendentplasmamixerrecipes",
        "Transcendent Plasma Mixer",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "PlasmaForge"),
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
    public static final RecipeMap spaceProjectFakeRecipes = new SpaceProjectRecipeMap(
        new HashSet<>(20),
        "gt.recipe.fakespaceprojects",
        "Space Projects",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
        12,
        0,
        0,
        0,
        1,
        translateToLocal("gt.specialvalue.stages") + " ",
        1,
        "",
        false,
        true).setRenderRealStackSizes(false)
            .setUsualFluidInputCount(4)
            .setNEIBackgroundOffset(2, 23)
            .setLogoPos(152, 83)
            .setDisableOptimize(true);
    /**
     * Uses {@link GT_RecipeConstants#ADDITIVE_AMOUNT} for coal/charcoal amount.
     */
    public static final RecipeMap primitiveBlastRecipes = new RecipeMap(
        new HashSet<>(200),
        "gt.recipe.primitiveblastfurnace",
        "Primitive Blast Furnace",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final RecipeMap implosionRecipes = new RecipeMap(
        new HashSet<>(900),
        "gt.recipe.implosioncompressor",
        "Implosion Compressor",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final RecipeMap vacuumRecipes = new RecipeMap(
        new HashSet<>(305),
        "gt.recipe.vacuumfreezer",
        "Vacuum Freezer",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final RecipeMap chemicalReactorRecipes = new RecipeMap(
        new HashSet<>(1170),
        "gt.recipe.chemicalreactor",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "ChemicalReactor"),
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
     * using {@code .addTo(chemicalReactorNonCellRecipes)} will cause the recipe to be added to multiblock recipe map
     * ONLY!
     * use {@link GT_RecipeConstants#UniversalChemical} to add to both.
     */
    public static final RecipeMap multiblockChemicalReactorRecipes = new LargeChemicalReactorRecipeMap()
        .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT)
        .setUsualFluidInputCount(6)
        .setUsualFluidOutputCount(6)
        .setDisableOptimize(true);
    public static final RecipeMap distillationTowerRecipes = new DistillationTowerRecipeMap()
        .setRecipeConfigFile("distillation", FIRST_FLUIDSTACK_INPUT)
        .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT)
        .setUsualFluidOutputCount(11)
        .setDisableOptimize(true);
    public static final OilCrackerRecipeMap crackingRecipes = (OilCrackerRecipeMap) new OilCrackerRecipeMap()
        .setRecipeConfigFile("cracking", FIRST_FLUIDSTACK_INPUT)
        .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE, ProgressBar.Direction.RIGHT)
        .setUsualFluidInputCount(2);
    public static final RecipeMap pyrolyseRecipes = new RecipeMap(
        new HashSet<>(150),
        "gt.recipe.pyro",
        "Pyrolyse Oven",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final RecipeMap wiremillRecipes = new RecipeMap(
        new HashSet<>(450),
        "gt.recipe.wiremill",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Wiremill"),
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
    public static final RecipeMap benderRecipes = new RecipeMap(
        new HashSet<>(5000),
        "gt.recipe.metalbender",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Bender"),
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
    public static final RecipeMap alloySmelterRecipes = new RecipeMap(
        new HashSet<>(12000),
        "gt.recipe.alloysmelter",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "AlloySmelter"),
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
    public static final RecipeMap assemblerRecipes = new AssemblerRecipeMap(
        new HashSet<>(8200),
        "gt.recipe.assembler",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Assembler2"),
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
    public static final RecipeMap circuitAssemblerRecipes = new AssemblerRecipeMap(
        new HashSet<>(605),
        "gt.recipe.circuitassembler",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "CircuitAssembler"),
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
    public static final RecipeMap cannerRecipes = new RecipeMap(
        new HashSet<>(900),
        "gt.recipe.canner",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Canner"),
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
    public static final RecipeMap latheRecipes = new RecipeMap(
        new HashSet<>(1150),
        "gt.recipe.lathe",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Lathe"),
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
    public static final RecipeMap cutterRecipes = new RecipeMap(
        new HashSet<>(5125),
        "gt.recipe.cuttingsaw",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Cutter4"),
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
    public static final RecipeMap slicerRecipes = new RecipeMap(
        new HashSet<>(20),
        "gt.recipe.slicer",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Slicer"),
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
    public static final RecipeMap extruderRecipes = new RecipeMap(
        new HashSet<>(13000),
        "gt.recipe.extruder",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Extruder"),
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
    public static final RecipeMap hammerRecipes = new RecipeMap(
        new HashSet<>(3800),
        "gt.recipe.hammer",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Hammer"),
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
    public static final RecipeMap amplifierRecipes = new RecipeMap(
        new HashSet<>(2),
        "gt.recipe.uuamplifier",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Amplifabricator"),
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
    public static final RecipeMap massFabFakeRecipes = new RecipeMap(
        new HashSet<>(2),
        "gt.recipe.massfab",
        null,
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Massfabricator"),
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
    public static final FuelRecipeMap dieselFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(20),
        "gt.recipe.dieselgeneratorfuel",
        "Combustion Generator Fuels",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final FuelRecipeMap extremeDieselFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(20),
        "gt.recipe.extremedieselgeneratorfuel",
        "Extreme Diesel Engine Fuel",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final FuelRecipeMap gasTurbineFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(25),
        "gt.recipe.gasturbinefuel",
        "Gas Turbine Fuel",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final FuelRecipeMap hotFuels = new FuelRecipeMap(
        new HashSet<>(10),
        "gt.recipe.thermalgeneratorfuel",
        "Thermal Generator Fuels",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final FuelRecipeMap denseLiquidFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(15),
        "gt.recipe.semifluidboilerfuels",
        "Semifluid Boiler Fuels",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final FuelRecipeMap plasmaFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(100),
        "gt.recipe.plasmageneratorfuels",
        "Plasma Generator Fuels",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final FuelRecipeMap magicFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(100),
        "gt.recipe.magicfuels",
        "Magic Energy Absorber Fuels",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final FuelRecipeMap smallNaquadahReactorFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(1),
        "gt.recipe.smallnaquadahreactor",
        "Naquadah Reactor MkI",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final FuelRecipeMap largeNaquadahReactorFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(1),
        "gt.recipe.largenaquadahreactor",
        "Naquadah Reactor MkII",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final FuelRecipeMap hugeNaquadahReactorFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(1),
        "gt.recipe.fluidnaquadahreactor",
        "Naquadah Reactor MkIII",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final FuelRecipeMap extremeNaquadahReactorFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(1),
        "gt.recipe.hugenaquadahreactor",
        "Naquadah Reactor MkIV",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final FuelRecipeMap ultraHugeNaquadahReactorFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(1),
        "gt.recipe.extrahugenaquadahreactor",
        "Naquadah Reactor MkV",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final FuelRecipeMap fluidNaquadahReactorFuels = (FuelRecipeMap) new FuelRecipeMap(
        new HashSet<>(1),
        "gt.recipe.fluidfuelnaquadahreactor",
        "Fluid Naquadah Reactor",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "Default"),
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
    public static final RecipeMap electrolyzerNonCellRecipes = new RecipeMap(
        new HashSet<>(300),
        "gt.recipe.largeelectrolyzer",
        "Large(PA) Electrolyzer",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
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
    public static final RecipeMap centrifugeNonCellRecipes = new RecipeMap(
        new HashSet<>(1200),
        "gt.recipe.largecentrifuge",
        "Large(PA) Centrifuge",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
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
    public static final RecipeMap mixerNonCellRecipes = new RecipeMap(
        new HashSet<>(900),
        "gt.recipe.largemixer",
        "Large(PA) Mixer",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
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
    public static final LargeBoilerFuelFakeRecipeMap largeBoilerFakeFuels = (LargeBoilerFuelFakeRecipeMap) new LargeBoilerFuelFakeRecipeMap()
        .setProgressBar(GT_UITextures.PROGRESSBAR_ARROW, ProgressBar.Direction.RIGHT)
        .setDisableOptimize(true);
    public static final RecipeMap nanoForgeRecipes = new RecipeMap(
        new HashSet<>(10),
        "gt.recipe.nanoforge",
        "Nano Forge",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
        6,
        2,
        2,
        1,
        1,
        "Tier: ",
        1,
        "",
        false,
        true).setUsualFluidInputCount(3)
            .setDisableOptimize(true)
            .setSlotOverlay(false, false, true, GT_UITextures.OVERLAY_SLOT_LENS)
            .setProgressBar(GT_UITextures.PROGRESSBAR_ASSEMBLE, ProgressBar.Direction.RIGHT);
    public static final RecipeMap pcbFactoryRecipes = new RecipeMap(
        new HashSet<>(10),
        "gt.recipe.pcbfactory",
        "PCB Factory",
        null,
        GregTech.getResourcePath(RecipeMap.TEXTURES_GUI_BASICMACHINES, "LCRNEI"),
        6,
        9,
        3,
        1,
        1,
        E,
        0,
        E,
        true,
        true).setUsualFluidInputCount(3)
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
    public static final IC2NuclearFakeRecipeMap ic2NuclearFakeRecipes = (IC2NuclearFakeRecipeMap) new IC2NuclearFakeRecipeMap()
        .setDisableOptimize(true);

    static {
        RecipeMaps.centrifugeRecipes.addDownstream(RecipeMaps.centrifugeNonCellRecipes.deepCopyInput());
        RecipeMaps.mixerRecipes.addDownstream(RecipeMaps.mixerNonCellRecipes.deepCopyInput());
        RecipeMaps.electrolyzerRecipes.addDownstream(RecipeMaps.electrolyzerNonCellRecipes.deepCopyInput());
        RecipeMaps.dieselFuels.addDownstream(
            IRecipeMap.newRecipeMap(
                b -> b.build()
                    .map(RecipeMaps.largeBoilerFakeFuels::addDieselRecipe)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList())));
        RecipeMaps.denseLiquidFuels.addDownstream(
            IRecipeMap.newRecipeMap(
                b -> b.build()
                    .map(RecipeMaps.largeBoilerFakeFuels::addDenseLiquidRecipe)
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList())));
    }
}
