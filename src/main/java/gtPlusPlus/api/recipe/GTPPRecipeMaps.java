package gtPlusPlus.api.recipe;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeConstants.LFTR_OUTPUT_POWER;
import static gregtech.api.util.GTRecipeConstants.QFT_CATALYST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import com.cleanroommc.modularui.widgets.ProgressWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.maps.FluidOnlyFrontend;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.api.recipe.maps.LargeNEIFrontend;
import gregtech.api.util.GTRecipe;
import gregtech.nei.formatter.FuelSpecialValueFormatter;
import gregtech.nei.formatter.HeatingCoilSpecialValueFormatter;
import gregtech.nei.formatter.SimpleSpecialValueFormatter;
import gtPlusPlus.xmod.gregtech.api.gui.GTPPUITextures;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTETreeFarm;

public class GTPPRecipeMaps {

    public static final RecipeMap<RecipeMapBackend> cokeOvenRecipes = RecipeMapBuilder.of("gtpp.recipe.cokeoven")
        .maxIO(2, 9, 2, 1)
        .minInputs(0, 0)
        .progressBar(GTUITextures.PROGRESSBAR_SIFT, ProgressBar.Direction.DOWN)
        .progressBarMUI2(GTGuiTextures.PROGRESSBAR_SIFT, ProgressWidget.Direction.DOWN)
        .build();
    public static final RecipeMap<RecipeMapBackend> multiblockMassFabricatorRecipes = RecipeMapBuilder
        .of("gtpp.recipe.matterfab2")
        .maxIO(2, 0, 1, 1)
        .build();
    public static final RecipeMap<FuelBackend> rocketFuels = RecipeMapBuilder
        .of("gtpp.recipe.rocketenginefuel", FuelBackend::new)
        .maxIO(0, 0, 1, 0)
        .neiSpecialInfoFormatter(
            recipeInfo -> Collections.singletonList(
                StatCollector
                    .translateToLocalFormatted("GT5U.nei.fuel", formatNumber(recipeInfo.recipe.mSpecialValue * 3000L))))
        .build();
    public static final RecipeMap<RecipeMapBackend> quantumForceTransformerRecipes = RecipeMapBuilder
        .of("gtpp.recipe.quantumforcesmelter")
        .maxIO(6, 6, 6, 6)
        .minInputs(0, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .progressBarMUI2(GTGuiTextures.PROGRESSBAR_ARROW_MULTIPLE)
        .recipeTransformer(recipe -> {
            ItemStack catalyst = recipe.getMetadata(QFT_CATALYST);
            if (catalyst == null) {
                throw new IllegalStateException("QFT catalyst must be set via metadata QFT_CATALYST");
            }
        })
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("GT5U.nei.tier"))
        .neiItemInputsGetter(recipe -> {
            ItemStack catalyst = recipe.getMetadata(QFT_CATALYST);
            assert catalyst != null;
            List<ItemStack> inputs = new ArrayList<>(Arrays.asList(recipe.mInputs));
            inputs.add(catalyst);
            return inputs.toArray(new ItemStack[0]);
        })
        .frontend(QuantumForceTransformerFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> chemicalDehydratorRecipes = RecipeMapBuilder
        .of("gtpp.recipe.chemicaldehydrator")
        .maxIO(2, 9, 1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_SIFT, ProgressBar.Direction.DOWN)
        .progressBarMUI2(GTGuiTextures.PROGRESSBAR_SIFT, ProgressWidget.Direction.DOWN)
        .build();
    public static final RecipeMap<RecipeMapBackend> vacuumFurnaceRecipes = RecipeMapBuilder.of("gtpp.recipe.vacfurnace")
        .maxIO(9, 9, 3, 3)
        .minInputs(0, 1)
        .neiSpecialInfoFormatter(HeatingCoilSpecialValueFormatter.INSTANCE)
        .frontend(LargeNEIFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> alloyBlastSmelterRecipes = RecipeMapBuilder
        .of("gtpp.recipe.alloyblastsmelter")
        .maxIO(9, 9, 3, 3)
        .minInputs(1, 0)
        .frontend(LargeNEIFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> liquidFluorineThoriumReactorRecipes = RecipeMapBuilder
        .of("gtpp.recipe.lftr")
        .maxIO(0, 0, 6, 6)
        .minInputs(0, 2)
        .frontend(FluidOnlyFrontend::new)
        .neiSpecialInfoFormatter(recipeInfo -> {
            final long eut = recipeInfo.recipe.getMetadataOrDefault(LFTR_OUTPUT_POWER, 0);
            final int duration = recipeInfo.recipe.mDuration;
            return Arrays.asList(
                StatCollector.translateToLocalFormatted("gtpp.nei.lftr.power", formatNumber(eut)),
                StatCollector.translateToLocalFormatted("gtpp.nei.lftr.dynamo", formatNumber(duration * eut)),
                StatCollector.translateToLocalFormatted("gtpp.nei.lftr.total", formatNumber(duration * eut * 4)));
        })
        .build();
    public static final RecipeMap<RecipeMapBackend> nuclearSaltProcessingPlantRecipes = RecipeMapBuilder
        .of("gtpp.recipe.nuclearsaltprocessingplant")
        .maxIO(1, 6, 2, 3)
        .frontend(LargeNEIFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> millingRecipes = RecipeMapBuilder.of("gtpp.recipe.oremill")
        .maxIO(3, 1, 0, 0)
        .minInputs(1, 0)
        .frontend(MillingFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> fissionFuelProcessingRecipes = RecipeMapBuilder
        .of("gtpp.recipe.fissionfuel")
        .maxIO(0, 0, 6, 1)
        .frontend(FluidOnlyFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> coldTrapRecipes = RecipeMapBuilder.of("gtpp.recipe.coldtrap")
        .maxIO(2, 9, 1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_SIFT, ProgressBar.Direction.DOWN)
        .progressBarMUI2(GTGuiTextures.PROGRESSBAR_SIFT, ProgressWidget.Direction.DOWN)
        .build();
    public static final RecipeMap<RecipeMapBackend> reactorProcessingUnitRecipes = RecipeMapBuilder
        .of("gtpp.recipe.reactorprocessingunit")
        .maxIO(2, 9, 1, 1)
        .progressBar(GTUITextures.PROGRESSBAR_SIFT, ProgressBar.Direction.DOWN)
        .build();
    public static final RecipeMap<RecipeMapBackend> simpleWasherRecipes = RecipeMapBuilder
        .of("gtpp.recipe.simplewasher")
        .maxIO(1, 1, 1, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_CAULDRON : null)
        .slotOverlaysMUI2(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTGuiTextures.OVERLAY_SLOT_CAULDRON : null)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .progressBarMUI2(GTGuiTextures.PROGRESSBAR_ARROW_MULTIPLE)
        .build();
    public static final RecipeMap<RecipeMapBackend> molecularTransformerRecipes = RecipeMapBuilder
        .of("gtpp.recipe.moleculartransformer")
        .maxIO(1, 1, 0, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_MICROSCOPE
                : null)
        .slotOverlaysMUI2(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTGuiTextures.OVERLAY_SLOT_MICROSCOPE
                : null)
        .neiHandlerInfo(
            builder -> builder.setDisplayStack(getModItem(Mods.AdvancedSolarPanel.ID, "BlockMolecularTransformer", 1)))
        .build();
    public static final RecipeMap<RecipeMapBackend> chemicalPlantRecipes = RecipeMapBuilder
        .of("gtpp.recipe.fluidchemicaleactor")
        .maxIO(4, 6, 4, 3)
        .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
            if (isFluid) {
                if (isOutput) {
                    return GTUITextures.OVERLAY_SLOT_VIAL_2;
                }
                return GTUITextures.OVERLAY_SLOT_MOLECULAR_3;
            }
            if (isOutput) {
                return GTUITextures.OVERLAY_SLOT_VIAL_1;
            }
            return GTUITextures.OVERLAY_SLOT_MOLECULAR_1;
        })
        .slotOverlaysMUI2((index, isFluid, isOutput, isSpecial) -> {
            if (isFluid) {
                if (isOutput) {
                    return GTGuiTextures.OVERLAY_SLOT_VIAL_2;
                }
                return GTGuiTextures.OVERLAY_SLOT_MOLECULAR_3;
            }
            if (isOutput) {
                return GTGuiTextures.OVERLAY_SLOT_VIAL_1;
            }
            return GTGuiTextures.OVERLAY_SLOT_MOLECULAR_1;
        })
        .progressBar(GTPPUITextures.PROGRESSBAR_FLUID_REACTOR, ProgressBar.Direction.CIRCULAR_CW)
        .progressBarMUI2(GTGuiTextures.PROGRESSBAR_FLUID_REACTOR, ProgressWidget.Direction.CIRCULAR_CW)
        .progressBarPos(82, 24)
        .neiSpecialInfoFormatter(recipeInfo -> {
            int tier = recipeInfo.recipe.mSpecialValue + 1;
            String materialName = StatCollector.translateToLocal("gtpp.nei.chemplant.tier." + tier);
            return Collections
                .singletonList(StatCollector.translateToLocalFormatted("GT5U.nei.tier", tier + " - " + materialName));
        })
        .frontend(ChemicalPlantFrontend::new)
        .build();
    public static final RecipeMap<FuelBackend> rtgFuels = RecipeMapBuilder
        .of("gtpp.recipe.RTGgenerators", FuelBackend::new)
        .maxIO(1, 0, 0, 0)
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("gtpp.nei.rtg.days", 365))
        .build();
    public static final RecipeMap<RecipeMapBackend> thermalBoilerRecipes = RecipeMapBuilder
        .of("gtpp.recipe.thermalgeneratorfuel")
        .maxIO(0, 9, 2, 3)
        .frontend(ThermalBoilerFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> solarTowerRecipes = RecipeMapBuilder.of("gtpp.recipe.solartower")
        .maxIO(0, 0, 1, 1)
        .neiSpecialInfoFormatter(
            recipeInfo -> Arrays.asList(
                StatCollector.translateToLocal("gtpp.nei.solar_tower.1"),
                StatCollector.translateToLocal("gtpp.nei.solar_tower.2"),
                StatCollector.translateToLocal("gtpp.nei.solar_tower.3")))
        .frontend(FluidOnlyFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> cyclotronRecipes = RecipeMapBuilder.of("gtpp.recipe.cyclotron")
        .maxIO(9, 9, 1, 1)
        .build();
    public static final RecipeMap<RecipeMapBackend> fishPondRecipes = RecipeMapBuilder.of("gtpp.recipe.fishpond")
        .maxIO(1, 25, 0, 0)
        .slotOverlays(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTUITextures.OVERLAY_SLOT_CAULDRON : null)
        .slotOverlaysMUI2(
            (index, isFluid, isOutput, isSpecial) -> !isFluid && !isOutput ? GTGuiTextures.OVERLAY_SLOT_CAULDRON : null)
        // Bottom left of the recipe
        .logoPos(7, 81)
        .progressBarPos(52, 44)
        .frontend(ZhuhaiFrontend::new)
        .progressBar(GTUITextures.PROGRESSBAR_FISHING)
        .progressBarMUI2(GTGuiTextures.PROGRESSBAR_FISHING)
        .build();
    public static final RecipeMap<RecipeMapBackend> spargeTowerRecipes = RecipeMapBuilder
        .of("gtpp.recipe.lftr.sparging")
        .frontend(SpargeTowerFrontend::new)
        .maxIO(0, 0, 9, 9)
        .build();
    public static final RecipeMap<RecipeMapBackend> advancedFreezerRecipes = RecipeMapBuilder
        .of("gtpp.recipe.cryogenicfreezer")
        .maxIO(1, 1, 2, 1)
        .build();
    public static final RecipeMap<RecipeMapBackend> centrifugeNonCellRecipes = RecipeMapBuilder
        .of("gtpp.recipe.multicentrifuge")
        .maxIO(6, 6, 6, 6)
        .progressBar(GTUITextures.PROGRESSBAR_EXTRACT)
        .progressBarMUI2(GTGuiTextures.PROGRESSBAR_EXTRACT)
        .frontend(LargeNEIFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> electrolyzerNonCellRecipes = RecipeMapBuilder
        .of("gtpp.recipe.multielectro")
        .maxIO(6, 6, 6, 6)
        .progressBar(GTUITextures.PROGRESSBAR_EXTRACT)
        .progressBarMUI2(GTGuiTextures.PROGRESSBAR_EXTRACT)
        .frontend(LargeNEIFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> mixerNonCellRecipes = RecipeMapBuilder.of("gtpp.recipe.multimixer")
        .maxIO(9, 9, 6, 6)
        .progressBar(GTUITextures.PROGRESSBAR_MIXER, ProgressBar.Direction.CIRCULAR_CW)
        .progressBarMUI2(GTGuiTextures.PROGRESSBAR_MIXER, ProgressWidget.Direction.CIRCULAR_CW)
        .frontend(LargeNEIFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> algaePondRecipes = RecipeMapBuilder.of("gtpp.recipe.algae_pond")
        .maxIO(0, 15, 0, 0)
        .progressBar(GTUITextures.PROGRESSBAR_SIFT, ProgressBar.Direction.UP)
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("gtpp.nei.ap.tier"))
        .frontend(AlgaePondFrontend::new)
        .neiRecipeComparator(
            (a, b) -> Comparator.<GTRecipe, Integer>comparing(recipe -> recipe.mSpecialValue)
                .thenComparing(GTRecipe::compareTo)
                .compare(a, b))
        .build();

    public static final RecipeMap<RecipeMapBackend> chemicalDehydratorNonCellRecipes = RecipeMapBuilder
        .of("gtpp.recipe.multidehydrator")
        .maxIO(6, 9, 3, 3)
        .frontend(LargeNEIFrontend::new)
        .build();
    public static final RecipeMap<FuelBackend> semiFluidFuels = RecipeMapBuilder
        .of("gtpp.recipe.semifluidgeneratorfuels", FuelBackend::new)
        .maxIO(0, 0, 1, 0)
        .neiSpecialInfoFormatter(FuelSpecialValueFormatter.INSTANCE)
        .build();
    public static final RecipeMap<RecipeMapBackend> flotationCellRecipes = RecipeMapBuilder
        .of("gtpp.recipe.flotationcell")
        .maxIO(6, 0, 1, 1)
        .build();
    public static final RecipeMap<RecipeMapBackend> treeGrowthSimulatorFakeRecipes = RecipeMapBuilder
        .of("gtpp.recipe.treefarm")
        .maxIO(MTETreeFarm.Mode.values().length, MTETreeFarm.Mode.values().length, 0, 0)
        .minInputs(1, 0)
        .useSpecialSlot()
        .frontend(TGSFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> multiblockRockBreakerRecipes = RecipeMapBuilder
        .of("gtpp.recipe.multiblockrockbreaker")
        .maxIO(3, 1, 2, 0)
        .progressBar(GTUITextures.PROGRESSBAR_MACERATE)
        .neiFluidInputsGetter(gtRecipe -> new FluidStack[] { Materials.Water.getFluid(0), Materials.Lava.getFluid(0) })
        .build();
}
