package gregtech.api.recipe;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.GTNHIntergalactic;
import static gregtech.api.enums.Mods.NEICustomDiagrams;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.TickTime.TICK;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeConstants.ADDITIVE_AMOUNT;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;
import static gregtech.api.util.GTRecipeConstants.GLASS;
import static gregtech.api.util.GTRecipeConstants.NANO_FORGE_TIER;
import static gregtech.api.util.GTRecipeConstants.PCB_NANITE_MATERIAL;
import static gregtech.api.util.GTRecipeMapUtil.GTRecipeTemplate;
import static gregtech.api.util.GTRecipeMapUtil.asTemplate;
import static gregtech.api.util.GTRecipeMapUtil.buildOrEmpty;
import static gregtech.api.util.GTUtility.clamp;
import static gregtech.api.util.GTUtility.copyAmount;
import static gregtech.api.util.GTUtility.getFluidForFilledItem;
import static gregtech.api.util.GTUtility.isArrayEmptyOrNull;
import static gregtech.api.util.GTUtility.isArrayOfLength;
import static gregtech.api.util.GTUtility.min;
import static gregtech.api.util.GTUtility.multiplyStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.common.loaders.BioCultureLoader;
import bartworks.common.loaders.BioItemList;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.maps.AssemblerBackend;
import gregtech.api.recipe.maps.AssemblyLineFrontend;
import gregtech.api.recipe.maps.CauldronFrontend;
import gregtech.api.recipe.maps.DistillationTowerFrontend;
import gregtech.api.recipe.maps.EFRBlastingBackend;
import gregtech.api.recipe.maps.EFRSmokingBackend;
import gregtech.api.recipe.maps.FluidCannerBackend;
import gregtech.api.recipe.maps.FluidOnlyFrontend;
import gregtech.api.recipe.maps.FormingPressBackend;
import gregtech.api.recipe.maps.FoundryModuleFrontend;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.recipe.maps.FurnaceBackend;
import gregtech.api.recipe.maps.IsotopeDecayFrontend;
import gregtech.api.recipe.maps.LargeBoilerFuelBackend;
import gregtech.api.recipe.maps.LargeBoilerFuelFrontend;
import gregtech.api.recipe.maps.LargeNEIFrontend;
import gregtech.api.recipe.maps.MicrowaveBackend;
import gregtech.api.recipe.maps.OilCrackerBackend;
import gregtech.api.recipe.maps.PrinterBackend;
import gregtech.api.recipe.maps.PurificationUnitClarifierFrontend;
import gregtech.api.recipe.maps.PurificationUnitFlocculatorFrontend;
import gregtech.api.recipe.maps.PurificationUnitLaserFrontend;
import gregtech.api.recipe.maps.PurificationUnitOzonationFrontend;
import gregtech.api.recipe.maps.PurificationUnitParticleExtractorFrontend;
import gregtech.api.recipe.maps.PurificationUnitPhAdjustmentFrontend;
import gregtech.api.recipe.maps.PurificationUnitPlasmaHeaterFrontend;
import gregtech.api.recipe.maps.QuantumComputerFrontend;
import gregtech.api.recipe.maps.RecyclerBackend;
import gregtech.api.recipe.maps.ReplicatorBackend;
import gregtech.api.recipe.maps.SpaceProjectFrontend;
import gregtech.api.recipe.maps.TranscendentPlasmaMixerFrontend;
import gregtech.api.recipe.maps.UnpackagerBackend;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.recipe.metadata.PCBFactoryTierKey;
import gregtech.api.recipe.metadata.PurificationPlantBaseChanceKey;
import gregtech.api.recipe.metadata.SimpleRecipeMetadataKey;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.purification.PurifiedWaterHelpers;
import gregtech.loaders.postload.recipes.beamcrafter.BeamCrafterFrontend;
import gregtech.loaders.postload.recipes.beamcrafter.BeamCrafterMetadata;
import gregtech.nei.formatter.FuelSpecialValueFormatter;
import gregtech.nei.formatter.FusionSpecialValueFormatter;
import gregtech.nei.formatter.HeatingCoilSpecialValueFormatter;
import gregtech.nei.formatter.SimpleSpecialValueFormatter;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.register.LanthItemList;
import mods.railcraft.common.blocks.aesthetics.cube.EnumCube;
import mods.railcraft.common.items.RailcraftToolItems;
import tectech.thing.CustomItemList;

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
                return GTUITextures.OVERLAY_SLOT_DUST;
            } else {
                return GTUITextures.OVERLAY_SLOT_CRUSHED_ORE;
            }
        })
        .progressBar(GTUITextures.PROGRESSBAR_BATH, ProgressBar.Direction.CIRCULAR_CW)
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
                return GTUITextures.OVERLAY_SLOT_DUST;
            } else {
                return GTUITextures.OVERLAY_SLOT_CRUSHED_ORE;
            }
        })
        .build();
    public static final RecipeMap<RecipeMapBackend> compressorRecipes = RecipeMapBuilder.of("gt.recipe.compressor")
        .maxIO(1, 1, 1, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_COMPRESSOR
                : null)
        .progressBar(GTUITextures.PROGRESSBAR_COMPRESS)
        .slotOverlaysSteam(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_COMPRESSOR_STEAM
                : null)
        .progressBarSteam(GTUITextures.PROGRESSBAR_COMPRESS_STEAM)
        .neiRecipeComparator(
            Comparator
                .<GTRecipe, Integer>comparing(recipe -> recipe.getMetadataOrDefault(CompressionTierKey.INSTANCE, 0))
                .thenComparing(GTRecipe::compareTo))
        // Avoid steam machine being used as handler icon
        .neiHandlerInfo(builder -> builder.setDisplayStack(ItemList.Machine_LV_Compressor.get(1)))
        .build();
    public static final RecipeMap<RecipeMapBackend> neutroniumCompressorRecipes = RecipeMapBuilder
        .of("gt.recipe.neutroniumcompressor")
        .maxIO(1, 1, 1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_COMPRESSOR
                : null)
        .progressBar(GTUITextures.PROGRESSBAR_COMPRESS)
        .neiHandlerInfo(builder -> builder.setDisplayStack(getModItem(Avaritia.ID, "Singularity", 1L, 0)))
        .neiRecipeComparator(
            Comparator
                .<GTRecipe, Integer>comparing(recipe -> recipe.getMetadataOrDefault(CompressionTierKey.INSTANCE, 0))
                .thenComparing(GTRecipe::compareTo))
        .build();
    public static final RecipeMap<RecipeMapBackend> extractorRecipes = RecipeMapBuilder.of("gt.recipe.extractor")
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_CENTRIFUGE
                : null)
        .progressBar(GTUITextures.PROGRESSBAR_EXTRACT)
        .slotOverlaysSteam(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_CENTRIFUGE_STEAM
                : null)
        .progressBarSteam(GTUITextures.PROGRESSBAR_EXTRACT_STEAM)
        // Avoid steam machine being used as handler icon
        .neiHandlerInfo(builder -> builder.setDisplayStack(ItemList.Machine_LV_Extractor.get(1)))
        .build();
    public static final RecipeMap<RecyclerBackend> recyclerRecipes = RecipeMapBuilder
        .of("ic.recipe.recycler", RecyclerBackend::new)
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_RECYCLE : null)
        .progressBar(GTUITextures.PROGRESSBAR_RECYCLE, ProgressBar.Direction.CIRCULAR_CW)
        .neiTransferRectId("ic2.recycler")
        .disableRegisterNEI()
        .build();
    public static final RecipeMap<FurnaceBackend> furnaceRecipes = RecipeMapBuilder
        .of("gt.recipe.furnace", FurnaceBackend::new)
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 9)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_FURNACE : null)
        .slotOverlaysSteam(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_FURNACE_STEAM
                : null)
        .progressBarSteam(GTUITextures.PROGRESSBAR_ARROW_STEAM)
        .neiTransferRectId("smelting")
        .disableRegisterNEI()
        .build();
    public static final RecipeMap<EFRBlastingBackend> efrBlastingRecipes = RecipeMapBuilder
        .of("gt.recipe.efrblasting", EFRBlastingBackend::new)
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 9)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_FURNACE : null)
        .slotOverlaysSteam(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_FURNACE_STEAM
                : null)
        .progressBarSteam(GTUITextures.PROGRESSBAR_ARROW_STEAM)
        .neiTransferRectId("smelting")
        .disableRegisterNEI()
        .build();
    public static final RecipeMap<EFRSmokingBackend> efrSmokingRecipes = RecipeMapBuilder
        .of("gt.recipe.efrsmelting", EFRSmokingBackend::new)
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 9)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_FURNACE : null)
        .slotOverlaysSteam(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_FURNACE_STEAM
                : null)
        .progressBarSteam(GTUITextures.PROGRESSBAR_ARROW_STEAM)
        .neiTransferRectId("smelting")
        .disableRegisterNEI()
        .build();
    public static final RecipeMap<MicrowaveBackend> microwaveRecipes = RecipeMapBuilder
        .of("gt.recipe.microwave", MicrowaveBackend::new)
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_FURNACE : null)
        .neiTransferRectId("smelting")
        .disableRegisterNEI()
        .build();
    public static final RecipeMap<RecipeMapBackend> scannerFakeRecipes = RecipeMapBuilder.of("gt.recipe.scanner")
        .maxIO(1, 1, 1, 0)
        .minInputs(1, 0)
        .useSpecialSlot()
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isSpecial) {
                return GTUITextures.OVERLAY_SLOT_DATA_ORB;
            }
            if (!isFluid && !isOutput) {
                return GTUITextures.OVERLAY_SLOT_MICROSCOPE;
            }
            return null;
        })
        .build();
    public static final RecipeMap<RecipeMapBackend> rockBreakerFakeRecipes = RecipeMapBuilder
        .of("gt.recipe.rockbreaker")
        .maxIO(2, 1, 0, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput) {
                return GTUITextures.OVERLAY_SLOT_CRUSHED_ORE;
            } else {
                return GTUITextures.OVERLAY_SLOT_DUST;
            }
        })
        .progressBar(GTUITextures.PROGRESSBAR_MACERATE)
        .build();
    public static final RecipeMap<RecipeMapBackend> quantumComputerFakeRecipes = RecipeMapBuilder
        .of("gt.recipe.quantumcomputer")
        .maxIO(1, 0, 0, 0)
        .minInputs(1, 0)
        .dontUseProgressBar()
        .frontend(QuantumComputerFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setMaxRecipesPerPage(4)
                .setDisplayStack(CustomItemList.Machine_Multi_Computer.get(1)))
        .build();
    public static final RecipeMap<ReplicatorBackend> replicatorRecipes = RecipeMapBuilder
        .of("gt.recipe.replicator", ReplicatorBackend::new)
        .maxIO(0, 1, 1, 1)
        .minInputs(0, 1)
        .useSpecialSlot()
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isSpecial) {
                return GTUITextures.OVERLAY_SLOT_DATA_ORB;
            }
            if (isFluid && !isOutput) {
                return GTUITextures.OVERLAY_SLOT_UUM;
            }
            return null;
        })
        .build();
    /**
     * Use {@link GTRecipeConstants#AssemblyLine} for recipe addition.
     */
    public static final RecipeMap<RecipeMapBackend> assemblylineVisualRecipes = RecipeMapBuilder
        .of("gt.recipe.fakeAssemblylineProcess")
        .maxIO(16, 1, 4, 0)
        .minInputs(1, 0)
        .useSpecialSlot()
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> isSpecial ? GTUITextures.OVERLAY_SLOT_DATA_ORB : null)
        .neiTransferRect(88, 8, 18, 72)
        .neiTransferRect(124, 8, 18, 72)
        .neiTransferRect(142, 26, 18, 18)
        .frontend(AssemblyLineFrontend::new)
        .build();
    /**
     * Usually, but not always, you should use {@link GTRecipeConstants#UniversalArcFurnace} instead.
     */
    public static final RecipeMap<RecipeMapBackend> plasmaArcFurnaceRecipes = RecipeMapBuilder
        .of("gt.recipe.plasmaarcfurnace")
        .maxIO(2, 9, 1, 1)
        .minInputs(1, 1)
        .build();
    /**
     * Usually, but not always, you should use {@link GTRecipeConstants#UniversalArcFurnace} instead.
     */
    public static final RecipeMap<RecipeMapBackend> arcFurnaceRecipes = RecipeMapBuilder.of("gt.recipe.arcfurnace")
        .maxIO(1, 9, 1, 0)
        .minInputs(1, 1)
        .amperage(3)
        .build();
    public static final RecipeMap<PrinterBackend> printerRecipes = RecipeMapBuilder
        .of("gt.recipe.printer", PrinterBackend::new)
        .maxIO(1, 1, 1, 0)
        .minInputs(1, 1)
        .useSpecialSlot()
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isSpecial) {
                return GTUITextures.OVERLAY_SLOT_DATA_STICK;
            }
            if (isFluid) {
                return null;
            }
            if (isOutput) {
                return GTUITextures.OVERLAY_SLOT_PAGE_PRINTED;
            }
            return GTUITextures.OVERLAY_SLOT_PAGE_BLANK;
        })
        .build();
    public static final RecipeMap<RecipeMapBackend> sifterRecipes = RecipeMapBuilder.of("gt.recipe.sifter")
        .maxIO(1, 9, 1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_SIFT, ProgressBar.Direction.DOWN)
        .build();
    public static final RecipeMap<FormingPressBackend> formingPressRecipes = RecipeMapBuilder
        .of("gt.recipe.press", FormingPressBackend::new)
        .maxIO(6, 1, 1, 0)
        .minInputs(2, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput) {
                return GTUITextures.OVERLAY_SLOT_PRESS_3;
            }
            if (index == 0) {
                return GTUITextures.OVERLAY_SLOT_PRESS_1;
            }
            return GTUITextures.OVERLAY_SLOT_PRESS_2;
        })
        .progressBar(GTUITextures.PROGRESSBAR_COMPRESS)
        .build();
    public static final RecipeMap<RecipeMapBackend> laserEngraverRecipes = RecipeMapBuilder
        .of("gt.recipe.laserengraver")
        .maxIO(4, 4, 2, 2)
        .slotOverlays(
            (index, isFluid, isOutput,
                isSpecial) -> !isFluid && !isOutput && index != 0 ? GTUITextures.OVERLAY_SLOT_LENS : null)
        // Add a simple ordering so lower tier purified water is displayed first, otherwise it gets really confusing
        // NEI Catalyst search requires recipes to be sorted by voltage tier. Therefore, we first sort by voltage
        // tier,
        // then by water tier, then the default comparator.
        .neiRecipeComparator(
            (a, b) -> Comparator.<GTRecipe, Integer>comparing(recipe -> recipe.mEUt)
                .thenComparing(
                    Comparator.comparing(PurifiedWaterHelpers::getWaterTierFromRecipe)
                        .thenComparing(GTRecipe::compareTo))
                .compare(a, b))
        .build();
    public static final RecipeMap<RecipeMapBackend> mixerRecipes = RecipeMapBuilder.of("gt.recipe.mixer")
        .maxIO(9, 4, 1, 1)
        .minInputs(1, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> !isFluid ? GTUITextures.OVERLAY_SLOT_DUST : null)
        .progressBar(GTUITextures.PROGRESSBAR_MIXER, ProgressBar.Direction.CIRCULAR_CW)
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
                    return GTUITextures.OVERLAY_SLOT_GEM;
                }
                return GTUITextures.OVERLAY_SLOT_DUST;
            }
            return GTUITextures.OVERLAY_SLOT_DUST;
        })
        .build();
    public static final RecipeMap<RecipeMapBackend> electroMagneticSeparatorRecipes = RecipeMapBuilder
        .of("gt.recipe.electromagneticseparator")
        .maxIO(1, 3, 0, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> isOutput ? GTUITextures.OVERLAY_SLOT_DUST
                : GTUITextures.OVERLAY_SLOT_CRUSHED_ORE)
        .progressBar(GTUITextures.PROGRESSBAR_MAGNET)
        .build();
    public static final RecipeMap<RecipeMapBackend> polarizerRecipes = RecipeMapBuilder.of("gt.recipe.polarizer")
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 0)
        .progressBar(GTUITextures.PROGRESSBAR_MAGNET)
        .build();
    public static final RecipeMap<RecipeMapBackend> maceratorRecipes = RecipeMapBuilder.of("gt.recipe.macerator")
        .maxIO(1, 4, 0, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> isOutput ? GTUITextures.OVERLAY_SLOT_DUST
                : GTUITextures.OVERLAY_SLOT_CRUSHED_ORE)
        .slotOverlaysSteam(
            (index, isFluid, isOutput, isSpecial) -> isOutput ? GTUITextures.OVERLAY_SLOT_DUST_STEAM
                : GTUITextures.OVERLAY_SLOT_CRUSHED_ORE_STEAM)
        .progressBar(GTUITextures.PROGRESSBAR_MACERATE)
        .progressBarSteam(GTUITextures.PROGRESSBAR_MACERATE_STEAM)
        // Avoid steam machine being used as handler icon
        .neiHandlerInfo(builder -> builder.setDisplayStack(ItemList.Machine_LV_Macerator.get(1)))
        .build();
    public static final RecipeMap<RecipeMapBackend> chemicalBathRecipes = RecipeMapBuilder.of("gt.recipe.chemicalbath")
        .maxIO(2, 3, 2, 2)
        .minInputs(1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_BATH, ProgressBar.Direction.CIRCULAR_CW)
        .build();
    public static final RecipeMap<RecipeMapBackend> brewingRecipes = RecipeMapBuilder.of("gt.recipe.brewer")
        .maxIO(1, 0, 1, 1)
        .minInputs(1, 1)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_CAULDRON : null)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .build();
    public static final RecipeMap<RecipeMapBackend> fluidHeaterRecipes = RecipeMapBuilder.of("gt.recipe.fluidheater")
        .maxIO(1, 1, 1, 1)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (!isFluid) {
                return null;
            }
            if (isOutput) {
                return GTUITextures.OVERLAY_SLOT_HEATER_2;
            }
            return GTUITextures.OVERLAY_SLOT_HEATER_1;
        })
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .build();
    public static final RecipeMap<RecipeMapBackend> distilleryRecipes = RecipeMapBuilder.of("gt.recipe.distillery")
        .maxIO(1, 1, 1, 1)
        .minInputs(0, 1)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (!isFluid) {
                return null;
            }
            if (isOutput) {
                return GTUITextures.OVERLAY_SLOT_BEAKER_2;
            }
            return GTUITextures.OVERLAY_SLOT_BEAKER_1;
        })
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
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
                    ItemData tData = GTOreDictUnificator.getItemData(r.mOutputs[0]);
                    if (tData != null && (tData.mPrefix == OrePrefixes.dust
                        || OrePrefixes.dust.mFamiliarPrefixes.contains(tData.mPrefix))) {
                        r.mOutputs[0] = GTOreDictUnificator.getDust(
                            tData.mMaterial.mMaterial,
                            tData.mMaterial.mAmount * r.mOutputs[0].stackSize / tScale);
                    } else {
                        if (r.mOutputs[0].stackSize / tScale == 0) r.mOutputs[0] = GTValues.NI;
                        else r.mOutputs[0] = copyAmount(r.mOutputs[0].stackSize / tScale, r.mOutputs[0]);
                    }
                }
                aDuration = (aDuration + tScale - 1) / tScale;
                r.mFluidInputs[0] = copyAmount(aInput, r.mFluidInputs[0]);
                r.mFluidOutputs[0] = copyAmount(aOutput, r.mFluidOutputs[0]);
                r.mDuration = aDuration;
            }
        })
        .build();
    public static final RecipeMap<RecipeMapBackend> fermentingRecipes = RecipeMapBuilder.of("gt.recipe.fermenter")
        .maxIO(0, 0, 1, 1)
        .minInputs(0, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .builderTransformer(
            b -> b.copy()
                .special(BioItemList.getPetriDish(BioCultureLoader.generalPurposeFermentingBacteria))
                .metadata(GLASS, 3)
                .eut(b.getEUt())
                .addTo(BartWorksRecipeMaps.bacterialVatRecipes))
        .build();
    public static final RecipeMap<RecipeMapBackend> fluidSolidifierRecipes = RecipeMapBuilder
        .of("gt.recipe.fluidsolidifier")
        .maxIO(1, 1, 1, 0)
        .minInputs(1, 1)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_MOLD : null)
        .build();
    public static final RecipeMap<RecipeMapBackend> fluidExtractionRecipes = RecipeMapBuilder
        .of("gt.recipe.fluidextractor")
        .maxIO(1, 1, 0, 1)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_CENTRIFUGE
                : null)
        .progressBar(GTUITextures.PROGRESSBAR_EXTRACT)
        .build();
    public static final RecipeMap<RecipeMapBackend> packagerRecipes = RecipeMapBuilder.of("gt.recipe.packager")
        .maxIO(2, 1, 0, 0)
        .minInputs(2, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput) {
                return GTUITextures.OVERLAY_SLOT_BOXED;
            }
            if (index != 0) {
                return GTUITextures.OVERLAY_SLOT_BOX;
            }
            return null;
        })
        .build();
    public static final RecipeMap<UnpackagerBackend> unpackagerRecipes = RecipeMapBuilder
        .of("gt.recipe.unpackager", UnpackagerBackend::new)
        .maxIO(1, 2, 0, 0)
        .minInputs(1, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> !isOutput ? GTUITextures.OVERLAY_SLOT_BOXED : null)
        .build();
    public static final RecipeMap<RecipeMapBackend> fusionRecipes = RecipeMapBuilder.of("gt.recipe.fusionreactor")
        .maxIO(0, 0, 2, 1)
        .minInputs(0, 2)
        .useCustomFilterForNEI()
        .neiSpecialInfoFormatter(FusionSpecialValueFormatter.INSTANCE)
        .neiRecipeComparator(
            Comparator.<GTRecipe, Integer>comparing(
                recipe -> FusionSpecialValueFormatter
                    .getFusionTier(recipe.getMetadataOrDefault(GTRecipeConstants.FUSION_THRESHOLD, 0L), recipe.mEUt))
                .thenComparing(GTRecipe::compareTo))
        .frontend(FluidOnlyFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> centrifugeRecipes = RecipeMapBuilder.of("gt.recipe.centrifuge")
        .maxIO(2, 6, 1, 1)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput) {
                return null;
            }
            if (isFluid) {
                return GTUITextures.OVERLAY_SLOT_CENTRIFUGE_FLUID;
            } else {
                if (index == 0) {
                    return GTUITextures.OVERLAY_SLOT_CENTRIFUGE;
                }
                return GTUITextures.OVERLAY_SLOT_CANISTER;
            }
        })
        .progressBar(GTUITextures.PROGRESSBAR_EXTRACT)
        .build();
    public static final RecipeMap<RecipeMapBackend> electrolyzerRecipes = RecipeMapBuilder.of("gt.recipe.electrolyzer")
        .maxIO(2, 6, 1, 1)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput) {
                return null;
            }
            if (isFluid) {
                return GTUITextures.OVERLAY_SLOT_CHARGER_FLUID;
            } else {
                if (index == 0) {
                    return GTUITextures.OVERLAY_SLOT_CHARGER;
                }
                return GTUITextures.OVERLAY_SLOT_CANISTER;
            }
        })
        .progressBar(GTUITextures.PROGRESSBAR_EXTRACT)
        .build();
    /**
     * Use {@link GTRecipeConstants#COIL_HEAT} as heat level.
     */
    public static final RecipeMap<RecipeMapBackend> blastFurnaceRecipes = RecipeMapBuilder.of("gt.recipe.blastfurnace")
        .maxIO(6, 6, 1, 1)
        .minInputs(1, 0)
        .neiSpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE)
        .build();
    /**
     * Use {@link GTRecipeConstants#COIL_HEAT} as heat level.
     */
    public static final RecipeMap<RecipeMapBackend> plasmaForgeRecipes = RecipeMapBuilder.of("gt.recipe.plasmaforge")
        .maxIO(9, 9, 9, 9)
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
        .build();
    public static final RecipeMap<RecipeMapBackend> cokeOvenRecipes = RecipeMapBuilder.of("gt.recipe.cokeoven")
        .maxIO(1, 1, 0, 1)
        .minInputs(1, 0)
        .build();
    /**
     * Uses {@link GTRecipeConstants#ADDITIVE_AMOUNT} for coal/charcoal amount.
     */
    public static final RecipeMap<RecipeMapBackend> primitiveBlastRecipes = RecipeMapBuilder
        .of("gt.recipe.primitiveblastfurnace")
        .maxIO(3, 3, 0, 0)
        .minInputs(1, 0)
        .recipeEmitter(builder -> {
            Optional<GTRecipe> rr = builder.eut(0)
                .validateInputCount(1, 2)
                .validateOutputCount(1, 2)
                .validateNoInputFluid()
                .validateNoOutputFluid()
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

            int aDustAmount;
            int[] baseChances = new int[] { 10000, 10000, 10000 };
            if (builder.getChances() != null) {
                System.arraycopy(builder.getChances(), 0, baseChances, 0, min(2, builder.getChances().length));
            }

            int[] coalChances = baseChances.clone();
            if (aCoalAmount < 9) {
                aDustAmount = 1;
                coalChances[2] = aCoalAmount * 10000 / 9;
            } else {
                aDustAmount = aCoalAmount / 9;
                coalChances[2] = 10000;
            }

            GTRecipeTemplate coll = asTemplate(rr.get());
            for (Materials coal : new Materials[] { Materials.Coal, Materials.Charcoal }) {
                coll.derive()
                    .setInputs(aInput1, aInput2, coal.getGems(aCoalAmount))
                    .setOutputs(aOutput1, aOutput2, Materials.AshDark.getDust(aDustAmount))
                    .setChances(coalChances);
                coll.derive()
                    .setInputs(aInput1, aInput2, coal.getDust(aCoalAmount))
                    .setOutputs(aOutput1, aOutput2, Materials.AshDark.getDust(aDustAmount))
                    .setChances(coalChances);
            }
            int aDuration = builder.getDuration();
            if (Railcraft.isModLoaded()) {
                int[] cokeChances = baseChances.clone();
                int aCokeAmount = aCoalAmount / 2;
                if (aCokeAmount < 9) {
                    aDustAmount = 1;
                    cokeChances[2] = aCokeAmount * 10000 / 9;
                } else {
                    aDustAmount = aCokeAmount / 9;
                    cokeChances[2] = 10000;
                }
                coll.derive()
                    .setInputs(aInput1, aInput2, RailcraftToolItems.getCoalCoke(aCokeAmount))
                    .setOutputs(aOutput1, aOutput2, Materials.Ash.getDust(aDustAmount))
                    .setDuration(aDuration * 2 / 3)
                    .setChances(cokeChances);
            }
            long aCactusSugarCokeAmount = aCoalAmount * 2L;
            int[] cactusSugarCokeChances = baseChances.clone();
            if (aCactusSugarCokeAmount < 9) {
                aDustAmount = 1;
                // Simple Cast to Int ok here
                cactusSugarCokeChances[2] = (int) (aCactusSugarCokeAmount * 10000 / 9);
            } else {
                // Simple Cast to Int ok here
                aDustAmount = (int) (aCactusSugarCokeAmount / 9);
                cactusSugarCokeChances[2] = 10000;
            }
            ItemStack cactusCoke = GregtechItemList.CactusCoke.get(aCactusSugarCokeAmount);
            ItemStack sugarCoke = GregtechItemList.SugarCoke.get(aCactusSugarCokeAmount);
            coll.derive()
                .setInputs(aInput1, aInput2, cactusCoke)
                .setOutputs(aOutput1, aOutput2, Materials.Ash.getDust(aDustAmount))
                .setDuration(aDuration * 2 / 3)
                .setChances(cactusSugarCokeChances);
            coll.derive()
                .setInputs(aInput1, aInput2, sugarCoke)
                .setOutputs(aOutput1, aOutput2, Materials.Ash.getDust(aDustAmount))
                .setDuration(aDuration * 2 / 3)
                .setChances(cactusSugarCokeChances);
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
                        .setOutputs(aOutput1, aOutput2, Materials.AshDark.getDust(aCoalAmount))
                        .setDuration(aDuration * 10);
                }
                if (Railcraft.isModLoaded()) {
                    coll.derive()
                        .setInputs(aInput1, aInput2, EnumCube.COKE_BLOCK.getItem(aCoalAmount / 2))
                        .setOutputs(aOutput1, aOutput2, Materials.Ash.getDust(aCoalAmount / 2))
                        .setDuration(aDuration * 20 / 3);
                }
                ItemStack cactusCokeBlock = GregtechItemList.BlockCactusCoke.get(aCoalAmount * 2L);
                ItemStack sugarCokeBlock = GregtechItemList.BlockSugarCoke.get(aCoalAmount * 2L);
                coll.derive()
                    .setInputs(aInput1, aInput2, cactusCokeBlock)
                    .setOutputs(aOutput1, aOutput2, Materials.Ash.getDust(aCoalAmount * 2))
                    .setDuration(aDuration * 20 / 3);
                coll.derive()
                    .setInputs(aInput1, aInput2, sugarCokeBlock)
                    .setOutputs(aOutput1, aOutput2, Materials.Ash.getDust(aCoalAmount * 2))
                    .setDuration(aDuration * 20 / 3);
            }
            return coll.getAll();
        })
        .build();
    /**
     * Uses {@link GTRecipeConstants#ADDITIVE_AMOUNT} for TNT/ITNT/... amount. Value is truncated to [0, 64]
     */
    public static final RecipeMap<RecipeMapBackend> implosionRecipes = RecipeMapBuilder
        .of("gt.recipe.implosioncompressor")
        .maxIO(2, 2, 0, 0)
        .minInputs(2, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (!isFluid && !isOutput) {
                if (index == 0) {
                    return GTUITextures.OVERLAY_SLOT_IMPLOSION;
                }
                return GTUITextures.OVERLAY_SLOT_EXPLOSIVE;
            }
            return null;
        })
        .progressBar(GTUITextures.PROGRESSBAR_COMPRESS)
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
            Optional<GTRecipe> t = b.duration(20)
                .eut(30)
                .validateInputCount(1, 1)
                .validateOutputCount(1, 2)
                .build();
            if (!t.isPresent()) return Collections.emptyList();
            ItemStack input = b.getItemInputBasic(0);
            GTRecipeTemplate coll = asTemplate(t.get());
            int tExplosives = Math.min(b.getMetadataOrDefault(ADDITIVE_AMOUNT, 0), 64);
            int tGunpowder = tExplosives << 1; // Worst
            int tDynamite = Math.max(1, tExplosives >> 1); // good
            @SuppressWarnings("UnnecessaryLocalVariable")
            int tTNT = tExplosives; // Slightly better
            int tITNT = Math.max(1, tExplosives >> 2); // the best
            if (tGunpowder < 65) coll.derive()
                .setInputs(input, ItemList.Block_Powderbarrel.get(tGunpowder));
            if (tDynamite < 17) coll.derive()
                .setInputs(input, GTModHandler.getIC2Item("dynamite", tDynamite, null));
            coll.derive()
                .setInputs(input, new ItemStack(Blocks.tnt, tTNT));
            coll.derive()
                .setInputs(input, GTModHandler.getIC2Item("industrialTnt", tITNT, null));
            return coll.getAll();
        })
        .builderTransformer(
            b -> b.copy()
                .duration(1 * TICK)
                .eut(TierEU.RECIPE_UEV)
                .addTo(BartWorksRecipeMaps.electricImplosionCompressorRecipes))
        .build();
    public static final RecipeMap<RecipeMapBackend> vacuumFreezerRecipes = RecipeMapBuilder
        .of("gt.recipe.vacuumfreezer")
        .maxIO(1, 1, 2, 1)
        .recipeEmitter(b -> {
            FluidStack in, out;
            if (isArrayOfLength(b.getItemInputsBasic(), 1) && isArrayOfLength(b.getItemOutputs(), 1)
                && isArrayEmptyOrNull(b.getFluidInputs())
                && isArrayEmptyOrNull(b.getFluidOutputs())
                && (in = getFluidForFilledItem(b.getItemInputBasic(0), true)) != null
                && (out = getFluidForFilledItem(b.getItemOutput(0), true)) != null) {
                Collection<GTRecipe> ret = new ArrayList<>();
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
        .build();
    /**
     * Using {@code .addTo(chemicalReactorRecipes)} will cause the recipe to be added to single block recipe map ONLY!
     * Use {@link GTRecipeConstants#UniversalChemical} to add to both.
     */
    public static final RecipeMap<RecipeMapBackend> chemicalReactorRecipes = RecipeMapBuilder
        .of("gt.recipe.chemicalreactor")
        .maxIO(2, 2, 1, 1)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isFluid) {
                if (isOutput) {
                    return GTUITextures.OVERLAY_SLOT_VIAL_2;
                }
                return GTUITextures.OVERLAY_SLOT_MOLECULAR_3;
            } else {
                if (isOutput) {
                    return GTUITextures.OVERLAY_SLOT_VIAL_1;
                }
                if (index == 0) {
                    return GTUITextures.OVERLAY_SLOT_MOLECULAR_1;
                }
                return GTUITextures.OVERLAY_SLOT_MOLECULAR_2;
            }
        })
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .build();
    /**
     * Using {@code .addTo(multiblockChemicalReactorRecipes)} will cause the recipe to be added to multiblock recipe map
     * ONLY! Use {@link GTRecipeConstants#UniversalChemical} to add to both.
     */
    public static final RecipeMap<RecipeMapBackend> multiblockChemicalReactorRecipes = RecipeMapBuilder
        .of("gt.recipe.largechemicalreactor")
        .maxIO(6, 6, 6, 6)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
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
                return GTUITextures.OVERLAY_SLOTS_NUMBER[index + 1];
            } else {
                return GTUITextures.OVERLAY_SLOTS_NUMBER[0];
            }
        })
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .logoPos(80, 62)
        .frontend(DistillationTowerFrontend::new)
        .build();
    public static final RecipeMap<OilCrackerBackend> crackingRecipes = RecipeMapBuilder
        .of("gt.recipe.craker", OilCrackerBackend::new)
        .maxIO(1, 1, 2, 1)
        .minInputs(1, 2)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .build();
    public static final RecipeMap<RecipeMapBackend> pyrolyseRecipes = RecipeMapBuilder.of("gt.recipe.pyro")
        .maxIO(2, 1, 1, 1)
        .minInputs(1, 0)
        .build();
    public static final RecipeMap<RecipeMapBackend> solarFactoryRecipes = RecipeMapBuilder.of("gt.recipe.solarfactory")
        .maxIO(9, 1, 3, 0)
        .minInputs(1, 0)
        .build();
    public static final RecipeMap<RecipeMapBackend> wiremillRecipes = RecipeMapBuilder.of("gt.recipe.wiremill")
        .maxIO(2, 1, 0, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_WIREMILL : null)
        .progressBar(GTUITextures.PROGRESSBAR_WIREMILL)
        .build();
    public static final RecipeMap<RecipeMapBackend> benderRecipes = RecipeMapBuilder.of("gt.recipe.metalbender")
        .maxIO(2, 1, 0, 0)
        .minInputs(2, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_BENDER : null)
        .progressBar(GTUITextures.PROGRESSBAR_BENDING)
        .build();
    public static final RecipeMap<RecipeMapBackend> alloySmelterRecipes = RecipeMapBuilder.of("gt.recipe.alloysmelter")
        .maxIO(2, 1, 0, 0)
        .minInputs(2, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_FURNACE : null)
        .slotOverlaysSteam((index, isFluid, isOutput, isSpecial) -> GTUITextures.OVERLAY_SLOT_FURNACE_STEAM)
        .progressBarSteam(GTUITextures.PROGRESSBAR_ARROW_STEAM)
        .recipeEmitter(b -> {
            if (Materials.Graphite.contains(b.getItemInputBasic(0))) return Collections.emptyList();
            if (GTUtility.isArrayOfLength(b.getItemInputsBasic(), 1)) {
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
        .build();
    public static final RecipeMap<AssemblerBackend> assemblerRecipes = RecipeMapBuilder
        .of("gt.recipe.assembler", AssemblerBackend::new)
        .maxIO(9, 1, 1, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_CIRCUIT : null)
        .progressBar(GTUITextures.PROGRESSBAR_ASSEMBLE)
        .build();
    public static final RecipeMap<RecipeMapBackend> circuitAssemblerRecipes = RecipeMapBuilder
        .of("gt.recipe.circuitassembler")
        .maxIO(6, 1, 1, 0)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_CIRCUIT : null)
        .progressBar(GTUITextures.PROGRESSBAR_CIRCUIT_ASSEMBLER)
        .unificateOutputNEI(!NEICustomDiagrams.isModLoaded())
        .build();
    public static final RecipeMap<FluidCannerBackend> cannerRecipes = RecipeMapBuilder
        .of("gt.recipe.canner", FluidCannerBackend::new)
        .maxIO(2, 2, 1, 1)
        .minInputs(1, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput) {
                return null;
            }
            if (index == 0) {
                return GTUITextures.OVERLAY_SLOT_CANNER;
            }
            return GTUITextures.OVERLAY_SLOT_CANISTER;
        })
        .progressBar(GTUITextures.PROGRESSBAR_CANNER)
        .build();

    public static final RecipeMap<RecipeMapBackend> latheRecipes = RecipeMapBuilder.of("gt.recipe.lathe")
        .maxIO(1, 2, 0, 0)
        .minInputs(1, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isOutput) {
                if (index == 0) {
                    return GTUITextures.OVERLAY_SLOT_ROD_2;
                }
                return GTUITextures.OVERLAY_SLOT_DUST;
            }
            return GTUITextures.OVERLAY_SLOT_ROD_1;
        })
        .progressBar(GTUITextures.PROGRESSBAR_LATHE)
        .addSpecialTexture(98, 24, 5, 18, GTUITextures.PROGRESSBAR_LATHE_BASE)
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
                    return GTUITextures.OVERLAY_SLOT_CUTTER_SLICED;
                }
                return GTUITextures.OVERLAY_SLOT_DUST;
            }
            return GTUITextures.OVERLAY_SLOT_BOX;
        })
        .progressBar(GTUITextures.PROGRESSBAR_CUT)
        .recipeEmitter(b -> {
            b.validateInputCount(1, 2)
                .validateOutputCount(1, 4)
                .validateNoOutputFluid();
            if ((b.getFluidInputs() != null && b.getFluidInputs().length > 0) || !b.isValid())
                return buildOrEmpty(b.validateInputFluidCount(1, 1));
            int aDuration = b.getDuration(), aEUt = b.getEUt();
            Collection<GTRecipe> ret = new ArrayList<>();
            b.copy()
                .fluidInputs(Materials.Water.getFluid(clamp(aDuration * aEUt / 320, 4, 1000)))
                .duration(aDuration * 2)
                .build()
                .ifPresent(ret::add);
            b.copy()
                .fluidInputs(GTModHandler.getDistilledWater(clamp(aDuration * aEUt / 426, 3, 750)))
                .duration(aDuration * 2)
                .build()
                .ifPresent(ret::add);
            b.copy()
                .fluidInputs(Materials.Lubricant.getFluid(clamp(aDuration * aEUt / 1280, 1, 250)))
                .duration(aDuration)
                .build()
                .ifPresent(ret::add);
            b.fluidInputs(Materials.DimensionallyShiftedSuperfluid.getFluid(clamp(aDuration * aEUt / 4000, 1, 10)))
                .duration((int) (aDuration / 2.5))
                .build()
                .ifPresent(ret::add);
            return ret;
        })
        .build();
    public static final RecipeMap<RecipeMapBackend> extruderRecipes = RecipeMapBuilder.of("gt.recipe.extruder")
        .maxIO(2, 1, 0, 0)
        .minInputs(2, 0)
        .slotOverlays(
            (index, isFluid, isOutput,
                isSpecial) -> !isFluid && !isOutput && index != 0 ? GTUITextures.OVERLAY_SLOT_EXTRUDER_SHAPE : null)
        .progressBar(GTUITextures.PROGRESSBAR_EXTRUDE)
        .build();
    public static final RecipeMap<RecipeMapBackend> hammerRecipes = RecipeMapBuilder.of("gt.recipe.hammer")
        .maxIO(2, 2, 2, 2)
        .minInputs(1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_HAMMER : null)
        .progressBar(GTUITextures.PROGRESSBAR_HAMMER, ProgressBar.Direction.DOWN)
        .addSpecialTexture(78, 42, 20, 6, GTUITextures.PROGRESSBAR_HAMMER_BASE)
        .slotOverlaysSteam(
            (index, isFluid, isOutput, isSpecial) -> !isOutput ? GTUITextures.OVERLAY_SLOT_HAMMER_STEAM : null)
        .progressBarSteam(GTUITextures.PROGRESSBAR_HAMMER_STEAM)
        .addSpecialTextureSteam(78, 42, 20, 6, GTUITextures.PROGRESSBAR_HAMMER_BASE_STEAM)
        // Avoid steam machine being used as handler icon
        .neiHandlerInfo(builder -> builder.setDisplayStack(ItemList.Machine_LV_Hammer.get(1)))
        .build();
    public static final RecipeMap<RecipeMapBackend> amplifierRecipes = RecipeMapBuilder.of("gt.recipe.uuamplifier")
        .maxIO(1, 0, 0, 1)
        .minInputs(1, 0)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isFluid) {
                return GTUITextures.OVERLAY_SLOT_UUA;
            }
            if (!isOutput) {
                return GTUITextures.OVERLAY_SLOT_CENTRIFUGE;
            }
            return null;
        })
        .progressBar(GTUITextures.PROGRESSBAR_EXTRACT)
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
                return GTUITextures.OVERLAY_SLOT_UUM;
            }
            return GTUITextures.OVERLAY_SLOT_UUA;
        })
        .build();
    public static final RecipeMap<FuelBackend> dieselFuels = RecipeMapBuilder
        .of("gt.recipe.dieselgeneratorfuel", FuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .builderTransformer(b -> {
            b.copy()
                .build()
                .ifPresent(
                    r -> RecipeMaps.largeBoilerFakeFuels.getBackend()
                        .addDieselRecipe(r));
            if (b.getMetadataOrDefault(FUEL_VALUE, 0) >= 1500) {
                b.copy()
                    .addTo(RecipeMaps.extremeDieselFuels);
            }
        })
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
        .builderTransformer(
            b -> b.copy()
                .build()
                .ifPresent(
                    r -> RecipeMaps.largeBoilerFakeFuels.getBackend()
                        .addDenseLiquidRecipe(r)))
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
    public static final RecipeMap<LargeBoilerFuelBackend> largeBoilerFakeFuels = RecipeMapBuilder
        .of("gt.recipe.largeboilerfakefuels", LargeBoilerFuelBackend::new)
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 0)
        .frontend(LargeBoilerFuelFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> nanoForgeRecipes = RecipeMapBuilder.of("gt.recipe.nanoforge")
        .maxIO(6, 2, 3, 0)
        .minInputs(2, 1)
        .slotOverlays(
            (index, isFluid, isOutput,
                isSpecial) -> !isFluid && !isOutput && index == 0 ? GTUITextures.OVERLAY_SLOT_LENS : null)
        .progressBar(GTUITextures.PROGRESSBAR_ASSEMBLE)
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("GT5U.nei.tier"))
        .neiRecipeComparator(
            Comparator.<GTRecipe, Integer>comparing(recipe -> recipe.getMetadataOrDefault(NANO_FORGE_TIER, 1))
                .thenComparing(GTRecipe::compareTo))
        .build();
    public static final RecipeMap<RecipeMapBackend> pcbFactoryRecipes = RecipeMapBuilder.of("gt.recipe.pcbfactory")
        .maxIO(6, 9, 3, 0)
        .minInputs(3, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ASSEMBLE)
        .neiItemInputsGetter(recipe -> {
            Materials naniteMaterial = recipe.getMetadata(PCB_NANITE_MATERIAL);
            if (naniteMaterial == null) {
                return recipe.mInputs;
            }
            List<ItemStack> inputs = new ArrayList<>();
            inputs.add(recipe.mInputs[0]);
            ItemStack naniteStack = naniteMaterial.getNanite(1);
            inputs.add(new ItemStack(naniteStack.getItem(), 0, naniteStack.getItemDamage()));
            inputs.addAll(Arrays.asList(Arrays.copyOfRange(recipe.mInputs, 1, recipe.mInputs.length)));
            return inputs.toArray(new ItemStack[0]);
        })
        .neiRecipeComparator(
            Comparator
                .<GTRecipe, Integer>comparing(recipe -> recipe.getMetadataOrDefault(PCBFactoryTierKey.INSTANCE, 1))
                .thenComparing(GTRecipe::compareTo))
        .build();
    public static final RecipeMap<RecipeMapBackend> purificationClarifierRecipes = RecipeMapBuilder
        .of("gt.recipe.purificationplantclarifier")
        .maxIO(1, 4, 1, 1)
        .minInputs(0, 1)
        .frontend(PurificationUnitClarifierFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> purificationOzonationRecipes = RecipeMapBuilder
        .of("gt.recipe.purificationplantozonation")
        .maxIO(0, 4, 2, 1)
        .minInputs(0, 2)
        .progressBar(GTUITextures.PROGRESSBAR_BATH)
        .neiRecipeComparator(
            Comparator
                .<GTRecipe, Float>comparing(
                    recipe -> recipe.getMetadataOrDefault(PurificationPlantBaseChanceKey.INSTANCE, 0.0f))
                .thenComparing(GTRecipe::compareTo))
        .frontend(PurificationUnitOzonationFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setMaxRecipesPerPage(1)
                // When setting a builder, apparently setting a display stack is also necessary
                .setDisplayStack(ItemList.Machine_Multi_PurificationUnitOzonation.get(1)))
        .build();
    public static final RecipeMap<RecipeMapBackend> purificationFlocculationRecipes = RecipeMapBuilder
        .of("gt.recipe.purificationplantflocculation")
        .maxIO(0, 3, 2, 2)
        .minInputs(0, 1)
        .progressBar(GTUITextures.PROGRESSBAR_BATH)
        .neiRecipeComparator(
            Comparator
                .<GTRecipe, Float>comparing(
                    recipe -> recipe.getMetadataOrDefault(PurificationPlantBaseChanceKey.INSTANCE, 0.0f))
                .thenComparing(GTRecipe::compareTo))
        .frontend(PurificationUnitFlocculatorFrontend::new)
        .build();

    public static final RecipeMap<RecipeMapBackend> purificationPhAdjustmentRecipes = RecipeMapBuilder
        .of("gt.recipe.purificationplantphadjustment")
        .maxIO(1, 0, 2, 1)
        .minInputs(0, 1)
        .progressBar(GTUITextures.PROGRESSBAR_MIXER)
        .frontend(PurificationUnitPhAdjustmentFrontend::new)
        .build();

    public static final RecipeMap<RecipeMapBackend> purificationPlasmaHeatingRecipes = RecipeMapBuilder
        .of("gt.recipe.purificationplantplasmaheating")
        .maxIO(0, 0, 3, 1)
        .minInputs(0, 1)
        .progressBar(GTUITextures.PROGRESSBAR_BOILER_HEAT)
        .frontend(PurificationUnitPlasmaHeaterFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> purificationUVTreatmentRecipes = RecipeMapBuilder
        .of("gt.recipe.purificationplantuvtreatment")
        .maxIO(9, 0, 1, 1)
        .minInputs(0, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW)
        .frontend(PurificationUnitLaserFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> purificationDegasifierRecipes = RecipeMapBuilder
        .of("gt.recipe.purificationplantdegasifier")
        .maxIO(0, 3, 1, 2)
        .minInputs(0, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW)
        .build();
    public static final RecipeMap<RecipeMapBackend> purificationParticleExtractionRecipes = RecipeMapBuilder
        .of("gt.recipe.purificationplantquarkextractor")
        .maxIO(6, 2, 1, 2)
        .minInputs(0, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW)
        .frontend(PurificationUnitParticleExtractorFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> ic2NuclearFakeRecipes = RecipeMapBuilder.of("gt.recipe.ic2nuke")
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 0)
        .logo(GTUITextures.PICTURE_RADIATION_WARNING)
        .logoPos(152, 41)
        .neiRecipeBackgroundSize(170, 60)
        .neiHandlerInfo(builder -> builder.setDisplayStack(GTModHandler.getIC2Item("nuclearReactor", 1, null)))
        .build();

    public static final RecipeMap<RecipeMapBackend> entropicProcessing = RecipeMapBuilder
        .of("gt.recipe.entropic-processing")
        .maxIO(6, 6, 3, 3)
        .minInputs(0, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .frontend(LargeNEIFrontend::new)
        .build();

    public static final RecipeMap<RecipeMapBackend> isotopeDecay = RecipeMapBuilder.of("gt.recipe.isotope-decay")
        .maxIO(1, 1, 0, 0)
        .minInputs(1, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW)
        .frontend(IsotopeDecayFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> cableRecipes = RecipeMapBuilder
        .of("gt.recipe.cable", RecipeMapBackend::new)
        .maxIO(6, 1, 1, 0)
        .minInputs(1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW)
        .build();

    public static final RecipeMetadataKey<BeamCrafterMetadata> BEAMCRAFTER_METADATA = SimpleRecipeMetadataKey
        .create(BeamCrafterMetadata.class, "beamcrafter_metadata");

    public static final RecipeMap<RecipeMapBackend> beamcrafterRecipes = RecipeMapBuilder
        .of("gt.recipe.beamcrafter", RecipeMapBackend::new)
        .minInputs(0, 0)
        .frontend(BeamCrafterFrontend::new)
        .neiSpecialInfoFormatter(((recipeInfo) -> {
            BeamCrafterMetadata metadata = recipeInfo.recipe.getMetadata(BEAMCRAFTER_METADATA);
            if (metadata == null) return Collections.emptyList();

            float minEnergy_A = metadata.minEnergy_A;
            float minEnergy_B = metadata.minEnergy_B;

            float amount_A = metadata.amount_A;
            float amount_B = metadata.amount_B;

            Particle particle_A = Particle.getParticleFromId(metadata.particleID_A);
            Particle particle_B = Particle.getParticleFromId(metadata.particleID_B);

            return Arrays.asList(
                StatCollector.translateToLocal("beamcrafting.energy_A") + ": " + formatNumber(minEnergy_A) + "keV",

                StatCollector.translateToLocal("beamcrafting.energy_B") + ": " + formatNumber(minEnergy_B) + "keV",

                StatCollector.translateToLocal("beamcrafting.amount_A") + ": " + formatNumber(amount_A),
                StatCollector.translateToLocal("beamcrafting.amount_B") + ": " + formatNumber(amount_B)

        );
        }))
        .neiItemInputsGetter(recipe -> {
            BeamCrafterMetadata metadata = recipe.getMetadata(BEAMCRAFTER_METADATA);
            if (metadata == null) return GTValues.emptyItemStackArray;
            ItemStack particleStack_A = new ItemStack(LanthItemList.PARTICLE_ITEM, 1, metadata.particleID_A);
            ItemStack particleStack_B = new ItemStack(LanthItemList.PARTICLE_ITEM, 1, metadata.particleID_B);

            List<ItemStack> ret = new ArrayList<>();
            ret.addAll(Arrays.asList(recipe.mInputs));
            ret.add(particleStack_A);
            ret.add(particleStack_B);

            return ret.toArray(new ItemStack[0]);
        })
        .progressBarPos(70, 22)
        .neiTransferRect(100, 22, 28, 18)
        .maxIO(4, 2, 2, 2)
        .progressBar(GTUITextures.PROGRESSBAR_BEAMCRAFTER)
        .progressBarSize(50, 30)
        .build();

    public static final RecipeMap<RecipeMapBackend> cauldronRecipe = RecipeMapBuilder
        .of("gt.recipe.cauldron", RecipeMapBackend::new)
        .maxIO(1, 1, 1, 0)
        .minInputs(1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW)
        .neiRecipeBackgroundOffset(7, 0)
        .neiRecipeBackgroundSize(165, 60)
        .logoPos(150, 38)
        .frontend(CauldronFrontend::new)
        .neiHandlerInfo(builder -> {
            builder.setWidth(170);
            builder.setHeight(60);
            builder.setDisplayStack(new ItemStack(Items.cauldron));
            builder.setShowFavoritesButton(false);
            builder.setShowOverlayButton(false);
            return builder;
        })
        .build();

    public static final RecipeMap<RecipeMapBackend> foundryFakeModuleCostRecipes = RecipeMapBuilder
        .of("gt.recipe.foundry_modules")
        .maxIO(12, 1, 0, 0)
        .addSpecialTexture(87, 38, 30, 13, GTUITextures.PICTURE_ARROW_GRAY)
        .dontUseProgressBar()
        .neiTransferRect(87, 38, 30, 13)
        .frontend(FoundryModuleFrontend::new)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(ItemList.Machine_Multi_ExoFoundry.get(1))
                .setHeight(100))
        .build();
}
