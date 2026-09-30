package goodgenerator.api.recipe;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.gtnhlib.util.numberformatting.options.FormatOptions;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.Badge;
import goodgenerator.blocks.tileEntity.AntimatterGenerator;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AntimatterGeneratorFrontend extends RecipeMapFrontend {

    public AntimatterGeneratorFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return ImmutableList.of(new Pos2d(15, 12), new Pos2d(139, 12));
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe recipe) {
        super.prepareRecipe(recipe);

        int inputIndex = 0;
        for (PositionedStack pStack : recipe.mInputs) {
            if (pStack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed && fixed.isFluid()) {
                if (inputIndex == 0) {
                    fixed.setCustomBadge(
                        new Badge("", "").setShadow(true)
                            .setAlignment(NEIClientUtils.Alignment.TopRight));
                } else {
                    fixed.setCustomBadge(
                        new Badge("", StatCollector.translateToLocal("gg.recipe.antimatter_generator.matter_tooltip"))
                            .setShadow(true)
                            .setAlignment(NEIClientUtils.Alignment.TopRight));
                }
            }
            inputIndex++;
        }
    }

    @Override
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe recipe, int cycletick) {
        super.drawNEIOverlays(recipe, cycletick);

        if (recipe.mInputs.get(1) instanceof GTNEIDefaultHandler.FixedPositionedStack fixed && fixed.isFluid()) {
            int altIndex = fixed.getPermutationIndex(fixed.item);
            FormatOptions format = new FormatOptions();
            format.setDecimalPlaces(2);
            drawNEIOverlayText(
                formatNumber(AntimatterGenerator.catalystExponents[altIndex], format),
                fixed,
                colorOverride.getTextColorOrDefault("nei_overlay_yellow", 0xFDD835),
                0.5f,
                true,
                Alignment.BottomLeft);
        }
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {}
}
