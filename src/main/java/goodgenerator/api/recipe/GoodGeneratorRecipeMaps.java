package goodgenerator.api.recipe;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import net.minecraft.util.StatCollector;

import goodgenerator.client.GUI.GGUITextures;
import gregtech.api.enums.GTValues;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.maps.FuelBackend;
import gregtech.nei.formatter.SimpleSpecialValueFormatter;

public class GoodGeneratorRecipeMaps {

    public static final RecipeMap<FuelBackend> naquadahReactorFuels = RecipeMapBuilder
        .of("gg.recipe.naquadah_reactor", FuelBackend::new)
        .maxIO(0, 0, 1, 1)
        .minInputs(0, 1)
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("value.naquadah_reactor"))
        .neiRecipeComparator(Comparator.comparing(recipe -> recipe.mSpecialValue))
        .dontUseProgressBar()
        .addSpecialTexture(59, 20, 58, 42, GGUITextures.PICTURE_NAQUADAH_REACTOR)
        .build();
    public static final RecipeMap<RecipeMapBackend> naquadahFuelRefineFactoryRecipes = RecipeMapBuilder
        .of("gg.recipe.naquadah_fuel_refine_factory")
        .maxIO(6, 0, 4, 1)
        .minInputs(0, 1)
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("value.naquadah_fuel_refine_factory"))
        .build();
    public static final RecipeMap<?> neutronActivatorRecipes = RecipeMapBuilder.of("gg.recipe.neutron_activator")
        .maxIO(9, 9, 1, 1)
        .dontUseProgressBar()
        .addSpecialTexture(73, 22, 31, 21, GGUITextures.PICTURE_NEUTRON_ACTIVATOR)
        .neiSpecialInfoFormatter(recipeInfo -> {
            int minNKE = recipeInfo.recipe.mSpecialValue % 10000;
            int maxNKE = recipeInfo.recipe.mSpecialValue / 10000;
            return Arrays.asList(
                StatCollector.translateToLocal("value.neutron_activator.0"),
                formatNumber(minNKE) + StatCollector.translateToLocal("value.neutron_activator.2"),
                StatCollector.translateToLocal("value.neutron_activator.1"),
                formatNumber(maxNKE) + StatCollector.translateToLocal("value.neutron_activator.2"));
        })
        .build();
    public static final RecipeMap<ExtremeHeatExchangerBackend> extremeHeatExchangerFuels = RecipeMapBuilder
        .of("gg.recipe.extreme_heat_exchanger", ExtremeHeatExchangerBackend::new)
        .maxIO(0, 0, 2, 3)
        .dontUseProgressBar()
        .addSpecialTexture(47, 13, 78, 59, GGUITextures.PICTURE_EXTREME_HEAT_EXCHANGER)
        .frontend(ExtremeHeatExchangerFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> preciseAssemblerRecipes = RecipeMapBuilder
        .of("gg.recipe.precise_assembler")
        .maxIO(4, 1, 4, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .progressBarPos(85, 30)
        .neiTransferRect(80, 30, 35, 18)
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("value.precise_assembler"))
        .frontend(PreciseAssemblerFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> componentAssemblyLineRecipes = RecipeMapBuilder
        .of("gg.recipe.componentassemblyline")
        .maxIO(12, 1, 12, 0)
        .neiTransferRect(70, 15, 18, 54)
        .neiSpecialInfoFormatter(
            recipeInfo -> Collections.singletonList(
                StatCollector.translateToLocalFormatted(
                    "value.component_assembly_line",
                    GTValues.VN[recipeInfo.recipe.mSpecialValue])))
        .dontUseProgressBar()
        .addSpecialTexture(70, 11, 72, 40, GGUITextures.PICTURE_COMPONENT_ASSLINE)
        .frontend(ComponentAssemblyLineFrontend::new)
        .build();
}
