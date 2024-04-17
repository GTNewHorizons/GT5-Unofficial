package gregtech.api.recipe;

import static gregtech.api.enums.Mods.GTNHIntergalactic;
import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.NEICustomDiagrams;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.util.GT_RecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GT_RecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GT_RecipeMapUtil.FIRST_FLUIDSTACK_INPUT;
import static gregtech.api.util.GT_RecipeMapUtil.FIRST_FLUIDSTACK_OUTPUT;
import static gregtech.api.util.GT_RecipeMapUtil.FIRST_FLUID_OUTPUT;
import static gregtech.api.util.GT_RecipeMapUtil.FIRST_ITEM_INPUT;
import static gregtech.api.util.GT_RecipeMapUtil.FIRST_ITEM_OR_FLUID_INPUT;
import static gregtech.api.util.GT_RecipeMapUtil.FIRST_ITEM_OR_FLUID_OUTPUT;
import static gregtech.api.util.GT_RecipeMapUtil.FIRST_ITEM_OUTPUT;
import static gregtech.api.util.GT_RecipeMapUtil.GT_RecipeTemplate;
import static gregtech.api.util.GT_RecipeMapUtil.asTemplate;
import static gregtech.api.util.GT_RecipeMapUtil.buildOrEmpty;
import static gregtech.api.util.GT_Utility.clamp;
import static gregtech.api.util.GT_Utility.copyAmount;
import static gregtech.api.util.GT_Utility.getFluidForFilledItem;
import static gregtech.api.util.GT_Utility.isArrayEmptyOrNull;
import static gregtech.api.util.GT_Utility.isArrayOfLength;
import static gregtech.api.util.GT_Utility.multiplyStack;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.IRecipeMap;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.maps.AssemblerBackend;
import gregtech.api.recipe.maps.AssemblyLineFrontend;
import gregtech.api.recipe.maps.DistillationTowerFrontend;
import gregtech.api.recipe.maps.FluidCannerBackend;
import gregtech.api.recipe.maps.FluidOnlyFrontend;
import gregtech.api.recipe.maps.FormingPressBackend;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.recipe.maps.FurnaceBackend;
import gregtech.api.recipe.maps.LargeBoilerFuelBackend;
import gregtech.api.recipe.maps.LargeBoilerFuelFrontend;
import gregtech.api.recipe.maps.LargeNEIFrontend;
import gregtech.api.recipe.maps.MicrowaveBackend;
import gregtech.api.recipe.maps.OilCrackerBackend;
import gregtech.api.recipe.maps.PrinterBackend;
import gregtech.api.recipe.maps.RecyclerBackend;
import gregtech.api.recipe.maps.ReplicatorBackend;
import gregtech.api.recipe.maps.SpaceProjectFrontend;
import gregtech.api.recipe.maps.TranscendentPlasmaMixerFrontend;
import gregtech.api.recipe.maps.UnpackagerBackend;
import gregtech.api.recipe.metadata.PCBFactoryTierKey;
import gregtech.api.util.GT_Config;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_RecipeConstants;
import gregtech.api.util.GT_RecipeMapUtil;
import gregtech.api.util.GT_Utility;
import gregtech.nei.formatter.FuelSpecialValueFormatter;
import gregtech.nei.formatter.FusionSpecialValueFormatter;
import gregtech.nei.formatter.HeatingCoilSpecialValueFormatter;
import gregtech.nei.formatter.SimpleSpecialValueFormatter;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;

@SuppressWarnings("SimplifyOptionalCallChains")
public final class RecipeMaps {

    public static final RecipeMap<RecipeMapBackend> oreWasherRecipes = RecipeMapBuilder.of("gt.recipe.orewasher")
        .maxIO(1, 3, 1, 0)
        .minInputs(1, 1)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isFluid) {
                return null;
            }
            if (isOutput) {
                return GT_UITextures.OVERLAY_SLOT_DUST;
            } else {
                return GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE;
            }
        })
        .progressBar(GT_UITextures.PROGRESSBAR_BATH, ProgressBar.Direction.CIRCULAR_CW)
        .recipeConfigFile("orewasher", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> thermalCentrifugeRecipes = RecipeMapBuilder
        .of("gt.recipe.thermalcentrifuge")
        .maxIO(1, 3, 0, 0)
        .minInputs(1, 0)
        .amperage(2)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isFluid) {
                return null;
            }
            if (isOutput) {
                return GT_UITextures.OVERLAY_SLOT_DUST;
            } else {
                return GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE;
            }
        })
        .recipeConfigFile("thermalcentrifuge", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> compressorRecipes = RecipeMapBuilder.of("gt.recipe.compressor")
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_COMPRESSOR
                : null)
        .progressBar(GT_UITextures.PROGRESSBAR_COMPRESS)
        .slotOverlaysSteam(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_COMPRESSOR_STEAM
                : null)
        .progressBarSteam(GT_UITextures.PROGRESSBAR_COMPRESS_STEAM)
        // Avoid steam machine being used as handler icon
        .neiHandlerInfo(builder -> builder.setDisplayStack(ItemList.Machine_LV_Compressor.get(1)))
        .recipeConfigFile("compressor", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> extractorRecipes = RecipeMapBuilder.of("gt.recipe.extractor")
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_CENTRIFUGE
                : null)
        .progressBar(GT_UITextures.PROGRESSBAR_EXTRACT)
        .slotOverlaysSteam(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_CENTRIFUGE_STEAM
                : null)
        .progressBarSteam(GT_UITextures.PROGRESSBAR_EXTRACT_STEAM)
        // Avoid steam machine being used as handler icon
        .neiHandlerInfo(builder -> builder.setDisplayStack(ItemList.Machine_LV_Extractor.get(1)))
        .recipeConfigFile("extractor", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecyclerBackend> recyclerRecipes = RecipeMapBuilder
        .of("ic.recipe.recycler", RecyclerBackend::new)
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_RECYCLE : null)
        .progressBar(GT_UITextures.PROGRESSBAR_RECYCLE, ProgressBar.Direction.CIRCULAR_CW)
        .neiTransferRectId("ic2.recycler")
        .disableRegisterNEI()
        .build();
    public static final RecipeMap<FurnaceBackend> furnaceRecipes = RecipeMapBuilder
        .of("mc.recipe.furnace", FurnaceBackend::new)
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 9)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_FURNACE : null)
        .slotOverlaysSteam(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_FURNACE_STEAM
                : null)
        .progressBarSteam(GT_UITextures.PROGRESSBAR_ARROW_STEAM)
        .neiTransferRectId("smelting")
        .disableRegisterNEI()
        .build();
    public static final RecipeMap<MicrowaveBackend> microwaveRecipes = RecipeMapBuilder
        .of("gt.recipe.microwave", MicrowaveBackend::new)
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_FURNACE : null)
        .neiTransferRectId("smelting")
        .disableRegisterNEI()
        .build();
    public static final RecipeMap<RecipeMapBackend> scannerFakeRecipes = RecipeMapBuilder.of("gt.recipe.scanner")
        .maxIO(1, 1, 1, 0)
        .minInputs(1, 0)
        .useSpecialSlot()
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isSpecial) {
                return GT_UITextures.OVERLAY_SLOT_DATA_ORB;
            }
            if (!isFluid && !isOutput) {
                return GT_UITextures.OVERLAY_SLOT_MICROSCOPE;
            }
            return null;
        })
        .build();
    public static final RecipeMap<RecipeMapBackend> rockBreakerFakeRecipes = RecipeMapBuilder
        .of("gt.recipe.rockbreaker")
        .maxIO(2, 1, 0, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput) {
                return GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE;
            } else {
                return GT_UITextures.OVERLAY_SLOT_DUST;
            }
        })
        .progressBar(GT_UITextures.PROGRESSBAR_MACERATE)
        .build();
    public static final RecipeMap<ReplicatorBackend> replicatorRecipes = RecipeMapBuilder
        .of("gt.recipe.replicator", ReplicatorBackend::new)
        .maxIO(0, 1, 1, 1)
        .minInputs(0, 1)
        .useSpecialSlot()
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isSpecial) {
                return GT_UITextures.OVERLAY_SLOT_DATA_ORB;
            }
            if (isFluid && !isOutput) {
                return GT_UITextures.OVERLAY_SLOT_UUM;
            }
            return null;
        })
        .build();
    /**
     * Use {@link GT_RecipeConstants#AssemblyLine} for recipe addition.
     */
    public static final RecipeMap<RecipeMapBackend> assemblylineVisualRecipes = RecipeMapBuilder
        .of("gt.recipe.fakeAssemblylineProcess")
        .maxIO(16, 1, 4, 0)
        .minInputs(1, 0)
        .useSpecialSlot()
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> isSpecial ? GT_UITextures.OVERLAY_SLOT_DATA_ORB : null)
        .disableOptimize()
        .neiTransferRect(88, 8, 18, 72)
        .neiTransferRect(124, 8, 18, 72)
        .neiTransferRect(142, 26, 18, 18)
        .frontend(AssemblyLineFrontend::new)
        .build();
    /**
     * Usually, but not always, you should use {@link GT_RecipeConstants#UniversalArcFurnace} instead.
     */
    public static final RecipeMap<RecipeMapBackend> plasmaArcFurnaceRecipes = RecipeMapBuilder
        .of("gt.recipe.plasmaarcfurnace")
        .maxIO(1, 9, 1, 1)
        .minInputs(1, 1)
        .recipeConfigFile("arcfurnace", FIRST_ITEM_INPUT)
        .build();
    /**
     * Usually, but not always, you should use {@link GT_RecipeConstants#UniversalArcFurnace} instead.
     */
    public static final RecipeMap<RecipeMapBackend> arcFurnaceRecipes = RecipeMapBuilder.of("gt.recipe.arcfurnace")
        .maxIO(1, 9, 1, 0)
        .minInputs(1, 1)
        .amperage(3)
        .recipeConfigFile("arcfurnace", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<PrinterBackend> printerRecipes = RecipeMapBuilder
        .of("gt.recipe.printer", PrinterBackend::new)
        .maxIO(1, 1, 1, 0)
        .minInputs(1, 1)
        .useSpecialSlot()
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isSpecial) {
                return GT_UITextures.OVERLAY_SLOT_DATA_STICK;
            }
            if (isFluid) {
                return null;
            }
            if (isOutput) {
                return GT_UITextures.OVERLAY_SLOT_PAGE_PRINTED;
            }
            return GT_UITextures.OVERLAY_SLOT_PAGE_BLANK;
        })
        .recipeConfigFile("printer", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> sifterRecipes = RecipeMapBuilder.of("gt.recipe.sifter")
        .maxIO(1, 9, 1, 1)
        .progressBar(GT_UITextures.PROGRESSBAR_SIFT, ProgressBar.Direction.DOWN)
        .recipeConfigFile("sifter", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<FormingPressBackend> formingPressRecipes = RecipeMapBuilder
        .of("gt.recipe.press", FormingPressBackend::new)
        .maxIO(6, 1, 0, 0)
        .minInputs(2, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput) {
                return GT_UITextures.OVERLAY_SLOT_PRESS_3;
            }
            if (index == 0) {
                return GT_UITextures.OVERLAY_SLOT_PRESS_1;
            }
            return GT_UITextures.OVERLAY_SLOT_PRESS_2;
        })
        .progressBar(GT_UITextures.PROGRESSBAR_COMPRESS)
        .recipeConfigFile("press", FIRST_ITEM_OUTPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> laserEngraverRecipes = RecipeMapBuilder
        .of("gt.recipe.laserengraver")
        .maxIO(4, 4, 2, 2)
        .slotOverlays(
            (index, isFluid, isOutput,
                isSpecial) -> !isFluid && !isOutput && index != 0 ? GT_UITextures.OVERLAY_SLOT_LENS : null)
        .recipeConfigFile("laserengraving", FIRST_ITEM_OUTPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> mixerRecipes = RecipeMapBuilder.of("gt.recipe.mixer")
        .maxIO(9, 4, 1, 1)
        .minInputs(1, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> !isFluid ? GT_UITextures.OVERLAY_SLOT_DUST : null)
        .progressBar(GT_UITextures.PROGRESSBAR_MIXER, ProgressBar.Direction.CIRCULAR_CW)
        .recipeConfigFile("mixer", FIRST_ITEM_OR_FLUID_OUTPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> autoclaveRecipes = RecipeMapBuilder.of("gt.recipe.autoclave")
        .maxIO(2, 4, 1, 1)
        .minInputs(1, 1)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isFluid) {
                return null;
            }
            if (isOutput) {
                if (index == 0) {
                    return GT_UITextures.OVERLAY_SLOT_GEM;
                }
                return GT_UITextures.OVERLAY_SLOT_DUST;
            }
            return GT_UITextures.OVERLAY_SLOT_DUST;
        })

        .recipeConfigFile("autoclave", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> electroMagneticSeparatorRecipes = RecipeMapBuilder
        .of("gt.recipe.electromagneticseparator")
        .maxIO(1, 3, 0, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> isOutput ? GT_UITextures.OVERLAY_SLOT_DUST
                : GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE)
        .progressBar(GT_UITextures.PROGRESSBAR_MAGNET)
        .recipeConfigFile("electromagneticseparator", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> polarizerRecipes = RecipeMapBuilder.of("gt.recipe.polarizer")
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 0)
        .progressBar(GT_UITextures.PROGRESSBAR_MAGNET)
        .recipeConfigFile("polarizer", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> maceratorRecipes = RecipeMapBuilder.of("gt.recipe.macerator")
        .maxIO(1, 4, 0, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> isOutput ? GT_UITextures.OVERLAY_SLOT_DUST
                : GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE)
        .slotOverlaysSteam(
            (index, isFluid, isOutput, isSpecial) -> isOutput ? GT_UITextures.OVERLAY_SLOT_DUST_STEAM
                : GT_UITextures.OVERLAY_SLOT_CRUSHED_ORE_STEAM)
        .progressBar(GT_UITextures.PROGRESSBAR_MACERATE)
        .progressBarSteam(GT_UITextures.PROGRESSBAR_MACERATE_STEAM)
        // Avoid steam machine being used as handler icon
        .neiHandlerInfo(builder -> builder.setDisplayStack(ItemList.Machine_LV_Macerator.get(1)))
        .recipeConfigFile("pulveriser", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> chemicalBathRecipes = RecipeMapBuilder.of("gt.recipe.chemicalbath")
        .maxIO(1, 3, 1, 1)
        .minInputs(1, 1)
        .progressBar(GT_UITextures.PROGRESSBAR_BATH, ProgressBar.Direction.CIRCULAR_CW)
        .recipeConfigFile("chemicalbath", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<FluidCannerBackend> fluidCannerRecipes = RecipeMapBuilder
        .of("gt.recipe.fluidcanner", FluidCannerBackend::new)
        .maxIO(1, 1, 1, 1)
        .minInputs(1, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> !isFluid ? GT_UITextures.OVERLAY_SLOT_CANISTER : null)
        .progressBar(GT_UITextures.PROGRESSBAR_CANNER)
        .recipeConfigFile("canning", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> brewingRecipes = RecipeMapBuilder.of("gt.recipe.brewer")
        .maxIO(1, 0, 1, 1)
        .minInputs(1, 1)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_CAULDRON : null)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .recipeConfigFile("brewing", FIRST_FLUIDSTACK_OUTPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> fluidHeaterRecipes = RecipeMapBuilder.of("gt.recipe.fluidheater")
        .maxIO(1, 0, 1, 1)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (!isFluid) {
                return null;
            }
            if (isOutput) {
                return GT_UITextures.OVERLAY_SLOT_HEATER_2;
            }
            return GT_UITextures.OVERLAY_SLOT_HEATER_1;
        })
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .recipeConfigFile("fluidheater", FIRST_FLUIDSTACK_OUTPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> distilleryRecipes = RecipeMapBuilder.of("gt.recipe.distillery")
        .maxIO(1, 1, 1, 1)
        .minInputs(1, 1)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (!isFluid) {
                return null;
            }
            if (isOutput) {
                return GT_UITextures.OVERLAY_SLOT_BEAKER_2;
            }
            return GT_UITextures.OVERLAY_SLOT_BEAKER_1;
        })
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .recipeTransformer(r -> {
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
        .recipeConfigFile("distillery", FIRST_FLUIDSTACK_OUTPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> fermentingRecipes = RecipeMapBuilder.of("gt.recipe.fermenter")
        .maxIO(0, 0, 1, 1)
        .minInputs(0, 1)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .recipeConfigFile("fermenting", FIRST_FLUIDSTACK_OUTPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> fluidSolidifierRecipes = RecipeMapBuilder
        .of("gt.recipe.fluidsolidifier")
        .maxIO(1, 1, 1, 0)
        .minInputs(1, 1)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_MOLD : null)
        .recipeTransformer(r -> {
            if (ArrayUtils.isNotEmpty(r.mFluidInputs)) {
                if (Materials.PhasedGold.getMolten(1)
                    .isFluidEqual(r.mFluidInputs[0]))
                    r.mFluidInputs = new FluidStack[] { Materials.VibrantAlloy.getMolten(r.mFluidInputs[0].amount) };
                else if (Materials.PhasedIron.getMolten(1)
                    .isFluidEqual(r.mFluidInputs[0]))
                    r.mFluidInputs = new FluidStack[] { Materials.PulsatingIron.getMolten(r.mFluidInputs[0].amount) };
            }
        })
        .recipeConfigFile("fluidsolidifier", FIRST_ITEM_OUTPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> fluidExtractionRecipes = RecipeMapBuilder
        .of("gt.recipe.fluidextractor")
        .maxIO(1, 1, 0, 1)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_CENTRIFUGE
                : null)
        .progressBar(GT_UITextures.PROGRESSBAR_EXTRACT)
        .recipeTransformer(r -> {
            if (ArrayUtils.isNotEmpty(r.mFluidOutputs)) {
                if (Materials.PhasedGold.getMolten(1)
                    .isFluidEqual(r.mFluidOutputs[0]))
                    r.mFluidOutputs = new FluidStack[] { Materials.VibrantAlloy.getMolten(r.mFluidOutputs[0].amount) };
                else if (Materials.PhasedIron.getMolten(1)
                    .isFluidEqual(r.mFluidOutputs[0]))
                    r.mFluidOutputs = new FluidStack[] { Materials.PulsatingIron.getMolten(r.mFluidOutputs[0].amount) };
            }
        })
        .recipeConfigFile("fluidextractor", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> packagerRecipes = RecipeMapBuilder.of("gt.recipe.packager")
        .maxIO(2, 1, 0, 0)
        .minInputs(2, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput) {
                return GT_UITextures.OVERLAY_SLOT_BOXED;
            }
            if (index != 0) {
                return GT_UITextures.OVERLAY_SLOT_BOX;
            }
            return null;
        })
        .recipeConfigFile("boxing", FIRST_ITEM_OUTPUT)
        .build();
    public static final RecipeMap<UnpackagerBackend> unpackagerRecipes = RecipeMapBuilder
        .of("gt.recipe.unpackager", UnpackagerBackend::new)
        .maxIO(1, 2, 0, 0)
        .minInputs(1, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> !isOutput ? GT_UITextures.OVERLAY_SLOT_BOXED : null)
        .recipeConfigFile("unboxing", FIRST_ITEM_OUTPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> fusionRecipes = RecipeMapBuilder.of("gt.recipe.fusionreactor")
        .maxIO(0, 0, 2, 1)
        .minInputs(0, 2)
        .disableOptimize()
        .useCustomFilterForNEI()
        .neiSpecialInfoFormatter(FusionSpecialValueFormatter.INSTANCE)
        .neiRecipeComparator(
            Comparator
                .<GT_Recipe, Integer>comparing(
                    recipe -> FusionSpecialValueFormatter.getFusionTier(recipe.mSpecialValue, recipe.mEUt))
                .thenComparing(GT_Recipe::compareTo))
        .frontend(FluidOnlyFrontend::new)
        .recipeConfigFile("fusion", FIRST_FLUID_OUTPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> centrifugeRecipes = RecipeMapBuilder.of("gt.recipe.centrifuge")
        .maxIO(2, 6, 1, 1)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput) {
                return null;
            }
            if (isFluid) {
                return GT_UITextures.OVERLAY_SLOT_CENTRIFUGE_FLUID;
            } else {
                if (index == 0) {
                    return GT_UITextures.OVERLAY_SLOT_CENTRIFUGE;
                }
                return GT_UITextures.OVERLAY_SLOT_CANISTER;
            }
        })
        .progressBar(GT_UITextures.PROGRESSBAR_EXTRACT)
        .recipeConfigFile("centrifuge", FIRST_ITEM_OR_FLUID_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> electrolyzerRecipes = RecipeMapBuilder.of("gt.recipe.electrolyzer")
        .maxIO(2, 6, 1, 1)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput) {
                return null;
            }
            if (isFluid) {
                return GT_UITextures.OVERLAY_SLOT_CHARGER_FLUID;
            } else {
                if (index == 0) {
                    return GT_UITextures.OVERLAY_SLOT_CHARGER;
                }
                return GT_UITextures.OVERLAY_SLOT_CANISTER;
            }
        })
        .progressBar(GT_UITextures.PROGRESSBAR_EXTRACT)
        .recipeConfigFile("electrolyzer", FIRST_ITEM_OR_FLUID_INPUT)
        .build();
    /**
     * Use {@link GT_RecipeConstants#COIL_HEAT} as heat level.
     */
    public static final RecipeMap<RecipeMapBackend> blastFurnaceRecipes = RecipeMapBuilder.of("gt.recipe.blastfurnace")
        .maxIO(6, 6, 1, 1)
        .minInputs(1, 0)
        .neiSpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE)
        .recipeConfigFile("blastfurnace", FIRST_ITEM_INPUT)
        .build();
    /**
     * Use {@link GT_RecipeConstants#COIL_HEAT} as heat level.
     */
    public static final RecipeMap<RecipeMapBackend> plasmaForgeRecipes = RecipeMapBuilder.of("gt.recipe.plasmaforge")
        .maxIO(9, 9, 9, 9)
        .disableOptimize()
        .neiSpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(ItemList.Machine_Multi_PlasmaForge.get(1))
                .setMaxRecipesPerPage(1))
        .frontend(LargeNEIFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> transcendentPlasmaMixerRecipes = RecipeMapBuilder
        .of("gt.recipe.transcendentplasmamixerrecipes")
        .maxIO(1, 0, 20, 1)
        .progressBarPos(86, 44)
        .logoPos(87, 99)
        .neiRecipeBackgroundSize(170, 118)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(ItemList.Machine_Multi_TranscendentPlasmaMixer.get(1))
                .setMaxRecipesPerPage(1))
        .frontend(TranscendentPlasmaMixerFrontend::new)
        .disableOptimize()
        .build();
    public static final RecipeMap<RecipeMapBackend> spaceProjectFakeRecipes = RecipeMapBuilder
        .of("gt.recipe.fakespaceprojects")
        .maxIO(12, 0, 4, 0)
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("GT5U.nei.stages"))
        .neiRecipeBackgroundOffset(3, 23)
        .logo(UITexture.fullImage(GTNHIntergalactic.ID, "gui/picture/space_elevator_logo.png"))
        .logoSize(18, 18)
        .logoPos(152, 83)
        .neiTransferRect(70, 28, 18, 72)
        .neiTransferRect(106, 28, 18, 72)
        .frontend(SpaceProjectFrontend::new)
        .disableRenderRealStackSizes()
        .disableOptimize()
        .build();
    /**
     * Uses {@link GT_RecipeConstants#ADDITIVE_AMOUNT} for coal/charcoal amount.
     */
    public static final RecipeMap<RecipeMapBackend> primitiveBlastRecipes = RecipeMapBuilder
        .of("gt.recipe.primitiveblastfurnace")
        .maxIO(3, 3, 0, 0)
        .minInputs(1, 0)
        .recipeEmitter(builder -> {
            Optional<GT_Recipe> rr = builder.eut(0)
                .validateInputCount(1, 2)
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
            int aCoalAmount = builder.getMetadataOrDefault(ADDITIVE_AMOUNT, 0);
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
                aInput1 = multiplyStack(10, aInput1);
                aInput2 = multiplyStack(10, aInput2);
                aOutput1 = multiplyStack(10, aOutput1);
                aOutput2 = multiplyStack(10, aOutput2);
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
        .recipeConfigFile("primitiveblastfurnace", FIRST_ITEM_INPUT)
        .build();
    /**
     * Uses {@link GT_RecipeConstants#ADDITIVE_AMOUNT} for TNT/ITNT/... amount. Value is truncated to [0, 64]
     */
    public static final RecipeMap<RecipeMapBackend> implosionRecipes = RecipeMapBuilder
        .of("gt.recipe.implosioncompressor")
        .maxIO(2, 2, 0, 0)
        .minInputs(2, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (!isFluid && !isOutput) {
                if (index == 0) {
                    return GT_UITextures.OVERLAY_SLOT_IMPLOSION;
                }
                return GT_UITextures.OVERLAY_SLOT_EXPLOSIVE;
            }
            return null;
        })
        .progressBar(GT_UITextures.PROGRESSBAR_COMPRESS)
        .disableOptimize()
        .recipeEmitter(b -> {
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
            int tExplosives = Math.min(b.getMetadataOrDefault(ADDITIVE_AMOUNT, 0), 64);
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
        .recipeConfigFile("implosion", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> vacuumFreezerRecipes = RecipeMapBuilder
        .of("gt.recipe.vacuumfreezer")
        .maxIO(1, 1, 2, 1)
        .recipeEmitter(b -> {
            b.noOptimize();
            FluidStack in, out;
            if (isArrayOfLength(b.getItemInputsBasic(), 1) && isArrayOfLength(b.getItemOutputs(), 1)
                && isArrayEmptyOrNull(b.getFluidInputs())
                && isArrayEmptyOrNull(b.getFluidOutputs())
                && (in = getFluidForFilledItem(b.getItemInputBasic(0), true)) != null
                && (out = getFluidForFilledItem(b.getItemOutput(0), true)) != null) {
                Collection<GT_Recipe> ret = new ArrayList<>();
                b.build()
                    .ifPresent(ret::add);
                b.itemInputs()
                    .itemOutputs()
                    .fluidInputs(in)
                    .fluidOutputs(out)
                    .build()
                    .ifPresent(ret::add);
                return ret;
            }
            return buildOrEmpty(b);
        })
        .recipeConfigFile("vacuumfreezer", FIRST_ITEM_INPUT)
        .build();
    /**
     * Using {@code .addTo(chemicalReactorRecipes)} will cause the recipe to be added to single block recipe map ONLY!
     * Use {@link GT_RecipeConstants#UniversalChemical} to add to both.
     */
    public static final RecipeMap<RecipeMapBackend> chemicalReactorRecipes = RecipeMapBuilder
        .of("gt.recipe.chemicalreactor")
        .maxIO(2, 2, 1, 1)
        .minInputs(1, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isFluid) {
                if (isOutput) {
                    return GT_UITextures.OVERLAY_SLOT_VIAL_2;
                }
                return GT_UITextures.OVERLAY_SLOT_MOLECULAR_3;
            } else {
                if (isOutput) {
                    return GT_UITextures.OVERLAY_SLOT_VIAL_1;
                }
                if (index == 0) {
                    return GT_UITextures.OVERLAY_SLOT_MOLECULAR_1;
                }
                return GT_UITextures.OVERLAY_SLOT_MOLECULAR_2;
            }
        })
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .disableOptimize()
        .recipeConfigFile("chemicalreactor", FIRST_ITEM_OR_FLUID_OUTPUT)
        .build();
    /**
     * Using {@code .addTo(multiblockChemicalReactorRecipes)} will cause the recipe to be added to
     * multiblock recipe map ONLY! Use {@link GT_RecipeConstants#UniversalChemical} to add to both.
     */
    public static final RecipeMap<RecipeMapBackend> multiblockChemicalReactorRecipes = RecipeMapBuilder
        .of("gt.recipe.largechemicalreactor")
        .maxIO(6, 6, 6, 6)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .disableOptimize()
        .frontend(LargeNEIFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> distillationTowerRecipes = RecipeMapBuilder
        .of("gt.recipe.distillationtower")
        .maxIO(2, 1, 1, 11)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (!isOutput) {
                return null;
            }
            if (isFluid) {
                return GT_UITextures.OVERLAY_SLOTS_NUMBER[index + 1];
            } else {
                return GT_UITextures.OVERLAY_SLOTS_NUMBER[0];
            }
        })
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .logoPos(80, 62)
        .frontend(DistillationTowerFrontend::new)
        .disableOptimize()
        .recipeConfigFile("distillation", FIRST_FLUIDSTACK_INPUT)
        .build();
    public static final RecipeMap<OilCrackerBackend> crackingRecipes = RecipeMapBuilder
        .of("gt.recipe.craker", OilCrackerBackend::new)
        .maxIO(1, 1, 2, 1)
        .minInputs(1, 2)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .recipeConfigFile("cracking", FIRST_FLUIDSTACK_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> pyrolyseRecipes = RecipeMapBuilder.of("gt.recipe.pyro")
        .maxIO(2, 1, 1, 1)
        .minInputs(1, 0)
        .disableOptimize()
        .recipeConfigFile("pyrolyse", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> wiremillRecipes = RecipeMapBuilder.of("gt.recipe.wiremill")
        .maxIO(2, 1, 0, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_WIREMILL : null)
        .progressBar(GT_UITextures.PROGRESSBAR_WIREMILL)
        .recipeConfigFile("wiremill", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> benderRecipes = RecipeMapBuilder.of("gt.recipe.metalbender")
        .maxIO(2, 1, 0, 0)
        .minInputs(2, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_BENDER : null)
        .progressBar(GT_UITextures.PROGRESSBAR_BENDING)
        .recipeConfigFile("bender", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> alloySmelterRecipes = RecipeMapBuilder.of("gt.recipe.alloysmelter")
        .maxIO(2, 1, 0, 0)
        .minInputs(2, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_FURNACE : null)
        .slotOverlaysSteam((index, isFluid, isOutput, isSpecial) -> GT_UITextures.OVERLAY_SLOT_FURNACE_STEAM)
        .progressBarSteam(GT_UITextures.PROGRESSBAR_ARROW_STEAM)
        .recipeEmitter(b -> {
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
        // Avoid steam machine being used as handler icon
        .neiHandlerInfo(builder -> builder.setDisplayStack(ItemList.Machine_LV_AlloySmelter.get(1)))
        .recipeConfigFile(
            "alloysmelting",
            r -> GT_Config.getStackConfigName(GT_Utility.isArrayOfLength(r.mInputs, 1) ? r.mInputs[0] : r.mOutputs[0]))
        .build();
    public static final RecipeMap<AssemblerBackend> assemblerRecipes = RecipeMapBuilder
        .of("gt.recipe.assembler", AssemblerBackend::new)
        .maxIO(9, 1, 1, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_CIRCUIT : null)
        .progressBar(GT_UITextures.PROGRESSBAR_ASSEMBLE)
        .disableOptimize()
        .recipeConfigFile("assembling", FIRST_ITEM_OUTPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> circuitAssemblerRecipes = RecipeMapBuilder
        .of("gt.recipe.circuitassembler")
        .maxIO(6, 1, 1, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_CIRCUIT : null)
        .progressBar(GT_UITextures.PROGRESSBAR_CIRCUIT_ASSEMBLER)
        .unificateOutputNEI(!NEICustomDiagrams.isModLoaded())
        .recipeConfigFile("circuitassembler", FIRST_ITEM_OUTPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> cannerRecipes = RecipeMapBuilder.of("gt.recipe.canner")
        .maxIO(2, 2, 0, 0)
        .minInputs(1, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput) {
                return null;
            }
            if (index == 0) {
                return GT_UITextures.OVERLAY_SLOT_CANNER;
            }
            return GT_UITextures.OVERLAY_SLOT_CANISTER;
        })
        .progressBar(GT_UITextures.PROGRESSBAR_CANNER)
        .recipeConfigFile("canning", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> latheRecipes = RecipeMapBuilder.of("gt.recipe.lathe")
        .maxIO(1, 2, 0, 0)
        .minInputs(1, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput) {
                if (index == 0) {
                    return GT_UITextures.OVERLAY_SLOT_ROD_2;
                }
                return GT_UITextures.OVERLAY_SLOT_DUST;
            }
            return GT_UITextures.OVERLAY_SLOT_ROD_1;
        })
        .progressBar(GT_UITextures.PROGRESSBAR_LATHE)
        .addSpecialTexture(98, 24, 5, 18, GT_UITextures.PROGRESSBAR_LATHE_BASE)
        .recipeConfigFile("lathe", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> cutterRecipes = RecipeMapBuilder.of("gt.recipe.cuttingsaw")
        .maxIO(2, 4, 1, 0)
        .minInputs(1, 1)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isFluid) {
                return null;
            }
            if (isOutput) {
                if (index == 0) {
                    return GT_UITextures.OVERLAY_SLOT_CUTTER_SLICED;
                }
                return GT_UITextures.OVERLAY_SLOT_DUST;
            }
            return GT_UITextures.OVERLAY_SLOT_BOX;
        })
        .progressBar(GT_UITextures.PROGRESSBAR_CUT)
        .recipeEmitter(b -> {
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
        .recipeConfigFile("cutting", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> slicerRecipes = RecipeMapBuilder.of("gt.recipe.slicer")
        .maxIO(2, 1, 0, 0)
        .minInputs(2, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput) {
                return GT_UITextures.OVERLAY_SLOT_SLICER_SLICED;
            }
            if (index == 0) {
                return GT_UITextures.OVERLAY_SLOT_SQUARE;
            }
            return GT_UITextures.OVERLAY_SLOT_SLICE_SHAPE;
        })
        .progressBar(GT_UITextures.PROGRESSBAR_SLICE)
        .recipeConfigFile("slicer", FIRST_ITEM_OUTPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> extruderRecipes = RecipeMapBuilder.of("gt.recipe.extruder")
        .maxIO(2, 1, 0, 0)
        .minInputs(2, 0)
        .slotOverlays(
            (index, isFluid, isOutput,
                isSpecial) -> !isFluid && !isOutput && index != 0 ? GT_UITextures.OVERLAY_SLOT_EXTRUDER_SHAPE : null)
        .progressBar(GT_UITextures.PROGRESSBAR_EXTRUDE)
        .recipeConfigFile("extruder", FIRST_ITEM_OUTPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> hammerRecipes = RecipeMapBuilder.of("gt.recipe.hammer")
        .maxIO(2, 2, 2, 2)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GT_UITextures.OVERLAY_SLOT_HAMMER : null)
        .progressBar(GT_UITextures.PROGRESSBAR_HAMMER, ProgressBar.Direction.DOWN)
        .addSpecialTexture(78, 42, 20, 6, GT_UITextures.PROGRESSBAR_HAMMER_BASE)
        .slotOverlaysSteam(
            (index, isFluid, isOutput, isSpecial) -> !isOutput ? GT_UITextures.OVERLAY_SLOT_HAMMER_STEAM : null)
        .progressBarSteam(GT_UITextures.PROGRESSBAR_HAMMER_STEAM)
        .addSpecialTextureSteam(78, 42, 20, 6, GT_UITextures.PROGRESSBAR_HAMMER_BASE_STEAM)
        // Avoid steam machine being used as handler icon
        .neiHandlerInfo(builder -> builder.setDisplayStack(ItemList.Machine_LV_Hammer.get(1)))
        .recipeConfigFile("forgehammer", FIRST_ITEM_OUTPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> amplifierRecipes = RecipeMapBuilder.of("gt.recipe.uuamplifier")
        .maxIO(1, 0, 0, 1)
        .minInputs(1, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isFluid) {
                return GT_UITextures.OVERLAY_SLOT_UUA;
            }
            if (!isOutput) {
                return GT_UITextures.OVERLAY_SLOT_CENTRIFUGE;
            }
            return null;
        })
        .progressBar(GT_UITextures.PROGRESSBAR_EXTRACT)
        .recipeConfigFile("amplifier", FIRST_ITEM_INPUT)
        .build();
    public static final RecipeMap<RecipeMapBackend> massFabFakeRecipes = RecipeMapBuilder.of("gt.recipe.massfab")
        .maxIO(1, 0, 1, 1)
        .minInputs(1, 0)
        .amperage(8)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (!isFluid) {
                return null;
            }
            if (isOutput) {
                return GT_UITextures.OVERLAY_SLOT_UUM;
            }
            return GT_UITextures.OVERLAY_SLOT_UUA;
        })
        .build();
    public static final RecipeMap<FuelBackend> dieselFuels = RecipeMapBuilder
        .of("gt.recipe.dieselgeneratorfuel", FuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(FuelSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<FuelBackend> extremeDieselFuels = RecipeMapBuilder
        .of("gt.recipe.extremedieselgeneratorfuel", FuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(FuelSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<FuelBackend> gasTurbineFuels = RecipeMapBuilder
        .of("gt.recipe.gasturbinefuel", FuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(FuelSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<FuelBackend> hotFuels = RecipeMapBuilder
        .of("gt.recipe.thermalgeneratorfuel", FuelBackend::new)
        .maxIO(1, 4, 0, 0)
        .neiSpecialInfoFormatter(FuelSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<FuelBackend> denseLiquidFuels = RecipeMapBuilder
        .of("gt.recipe.semifluidboilerfuels", FuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .disableRegisterNEI()
        .build();
    public static final RecipeMap<FuelBackend> plasmaFuels = RecipeMapBuilder
        .of("gt.recipe.plasmageneratorfuels", FuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(FuelSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<FuelBackend> magicFuels = RecipeMapBuilder
        .of("gt.recipe.magicfuels", FuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(FuelSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<FuelBackend> smallNaquadahReactorFuels = RecipeMapBuilder
        .of("gt.recipe.smallnaquadahreactor", FuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(FuelSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<FuelBackend> largeNaquadahReactorFuels = RecipeMapBuilder
        .of("gt.recipe.largenaquadahreactor", FuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(FuelSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<FuelBackend> hugeNaquadahReactorFuels = RecipeMapBuilder
        .of("gt.recipe.fluidnaquadahreactor", FuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(FuelSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<FuelBackend> extremeNaquadahReactorFuels = RecipeMapBuilder
        .of("gt.recipe.hugenaquadahreactor", FuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(FuelSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<FuelBackend> ultraHugeNaquadahReactorFuels = RecipeMapBuilder
        .of("gt.recipe.extrahugenaquadahreactor", FuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(FuelSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<FuelBackend> fluidNaquadahReactorFuels = RecipeMapBuilder
        .of("gt.recipe.fluidfuelnaquadahreactor", FuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .neiSpecialInfoFormatter(FuelSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<RecipeMapBackend> electrolyzerNonCellRecipes = RecipeMapBuilder
        .of("gt.recipe.largeelectrolyzer")
        .maxIO(1, 6, 1, 6)
        .disableRegisterNEI()
        .recipeEmitter(GT_RecipeMapUtil::buildRecipeForMultiblock)
        .build();
    public static final RecipeMap<RecipeMapBackend> centrifugeNonCellRecipes = RecipeMapBuilder
        .of("gt.recipe.largecentrifuge")
        .maxIO(2, 6, 1, 6)
        .disableOptimize()
        .disableRegisterNEI()
        .recipeEmitter(GT_RecipeMapUtil::buildRecipeForMultiblock)
        .build();
    public static final RecipeMap<RecipeMapBackend> mixerNonCellRecipes = RecipeMapBuilder.of("gt.recipe.largemixer")
        .maxIO(9, 4, 6, 4)
        .disableOptimize()
        .disableRegisterNEI()
        .recipeEmitter(GT_RecipeMapUtil::buildRecipeForMultiblockNoCircuit)
        .build();
    public static final RecipeMap<LargeBoilerFuelBackend> largeBoilerFakeFuels = RecipeMapBuilder
        .of("gt.recipe.largeboilerfakefuels", LargeBoilerFuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 0)
        .disableOptimize()
        .frontend(LargeBoilerFuelFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> nanoForgeRecipes = RecipeMapBuilder.of("gt.recipe.nanoforge")
        .maxIO(6, 2, 3, 0)
        .minInputs(2, 1)
        .slotOverlays(
            (index, isFluid, isOutput,
                isSpecial) -> !isFluid && !isOutput && index == 0 ? GT_UITextures.OVERLAY_SLOT_LENS : null)
        .progressBar(GT_UITextures.PROGRESSBAR_ASSEMBLE)
        .disableOptimize()
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("GT5U.nei.tier"))
        .build();
    public static final RecipeMap<RecipeMapBackend> pcbFactoryRecipes = RecipeMapBuilder.of("gt.recipe.pcbfactory")
        .maxIO(6, 9, 3, 0)
        .minInputs(3, 1)
        .progressBar(GT_UITextures.PROGRESSBAR_ASSEMBLE)
        .disableOptimize()
        .neiRecipeComparator(
            Comparator
                .<GT_Recipe, Integer>comparing(recipe -> recipe.getMetadataOrDefault(PCBFactoryTierKey.INSTANCE, 1))
                .thenComparing(GT_Recipe::compareTo))
        .build();
    public static final RecipeMap<RecipeMapBackend> purificationPlantGrade1Recipes = RecipeMapBuilder
        .of("gt.recipe.purificationplantgrade1")
        .maxIO(2, 9, 3, 3)
        .minInputs(0, 1)
        .progressBar(GT_UITextures.PROGRESSBAR_SIFT)
        .disableOptimize()
        .build();
    public static final RecipeMap<RecipeMapBackend> ic2NuclearFakeRecipes = RecipeMapBuilder.of("gt.recipe.ic2nuke")
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 0)
        .disableOptimize()
        .logo(GT_UITextures.PICTURE_RADIATION_WARNING)
        .logoPos(152, 41)
        .neiRecipeBackgroundSize(170, 60)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GT_ModHandler.getIC2Item("nuclearReactor", 1, null)))
        .build();

    static {
        RecipeMaps.centrifugeRecipes.addDownstream(RecipeMaps.centrifugeNonCellRecipes.deepCopyInput());
        RecipeMaps.mixerRecipes.addDownstream(RecipeMaps.mixerNonCellRecipes.deepCopyInput());
        RecipeMaps.electrolyzerRecipes.addDownstream(RecipeMaps.electrolyzerNonCellRecipes.deepCopyInput());
        RecipeMaps.dieselFuels.addDownstream(
            IRecipeMap.newRecipeMap(
                b -> b.build()
                    .map(
                        r -> RecipeMaps.largeBoilerFakeFuels.getBackend()
                            .addDieselRecipe(r))
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList())));
        RecipeMaps.dieselFuels.addDownstream(IRecipeMap.newRecipeMap(b -> {
            if (b.getMetadataOrDefault(FUEL_VALUE, 0) < 1500) return Collections.emptyList();
            return b.addTo(RecipeMaps.extremeDieselFuels);
        }));
        RecipeMaps.denseLiquidFuels.addDownstream(
            IRecipeMap.newRecipeMap(
                b -> b.build()
                    .map(
                        r -> RecipeMaps.largeBoilerFakeFuels.getBackend()
                            .addDenseLiquidRecipe(r))
                    .map(Collections::singletonList)
                    .orElse(Collections.emptyList())));
    }
}
