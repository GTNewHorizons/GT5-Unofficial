package goodgenerator.api.recipe;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import net.minecraft.util.StatCollector;

import goodgenerator.client.GUI.GG_UITextures;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.util.GT_Utility;
import gregtech.nei.formatter.SimpleSpecialValueFormatter;

public class GoodGeneratorRecipeMaps {

    public static final RecipeMap<RecipeMapBackend> naquadahReactorFuels = RecipeMapBuilder
        .of("gg.recipe.naquadah_reactor")
        .maxIO(0, 0, 1, 1)
        .minInputs(0, 1)
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("value.naquadah_reactor"))
        .neiRecipeComparator(Comparator.comparing(recipe -> recipe.mSpecialValue))
        .dontUseProgressBar()
        .addSpecialTexture(59, 20, 58, 42, GG_UITextures.PICTURE_NAQUADAH_REACTOR)
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
        .addSpecialTexture(73, 22, 31, 21, GG_UITextures.PICTURE_NEUTRON_ACTIVATOR)
        .neiSpecialInfoFormatter(recipeInfo -> {
            int minNKE = recipeInfo.recipe.mSpecialValue % 10000;
            int maxNKE = recipeInfo.recipe.mSpecialValue / 10000;
            return Arrays.asList(
                StatCollector.translateToLocal("value.neutron_activator.0"),
                GT_Utility.formatNumbers(minNKE) + StatCollector.translateToLocal("value.neutron_activator.2"),
                StatCollector.translateToLocal("value.neutron_activator.1"),
                GT_Utility.formatNumbers(maxNKE) + StatCollector.translateToLocal("value.neutron_activator.2"));
        })
        .build();
    public static final RecipeMap<ExtremeHeatExchangerBackend> extremeHeatExchangerFuels = RecipeMapBuilder
        .of("gg.recipe.extreme_heat_exchanger", ExtremeHeatExchangerBackend::new)
        .maxIO(0, 0, 2, 3)
        .dontUseProgressBar()
        .addSpecialTexture(47, 13, 78, 59, GG_UITextures.PICTURE_EXTREME_HEAT_EXCHANGER)
        .frontend(ExtremeHeatExchangerFrontend::new)
        .build();
    public static final RecipeMap<RecipeMapBackend> preciseAssemblerRecipes = RecipeMapBuilder
        .of("gg.recipe.precise_assembler")
        .maxIO(4, 1, 4, 0)
        .progressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE)
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
                    GT_Values.VN[recipeInfo.recipe.mSpecialValue])))
        .dontUseProgressBar()
        .addSpecialTexture(70, 11, 72, 40, GG_UITextures.PICTURE_COMPONENT_ASSLINE)
        .frontend(ComponentAssemblyLineFrontend::new)
        .build();
}
