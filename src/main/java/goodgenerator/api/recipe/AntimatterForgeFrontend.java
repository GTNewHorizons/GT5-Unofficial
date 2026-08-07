package goodgenerator.api.recipe;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.PositionedStack;
import codechicken.nei.recipe.Badge;
import goodgenerator.blocks.tileEntity.AntimatterForge;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AntimatterForgeFrontend extends RecipeMapFrontend {

    public AntimatterForgeFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return ImmutableList
            .of(new Pos2d(15, 12), new Pos2d(68, 44), new Pos2d(86, 44), new Pos2d(68, 62), new Pos2d(86, 62));
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return Collections.singletonList(new Pos2d(139, 12));
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe recipe) {
        super.prepareRecipe(recipe);
        int inputIndex = 0;
        for (PositionedStack pStack : recipe.mInputs) {
            if (pStack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed && fixed.isFluid()) {
                if (inputIndex == 0) {
                    fixed.setCustomBadge(
                        new Badge("+", StatCollector.translateToLocal("gg.recipe.antimatter_forge.protomatter_tooltip"))
                            .setShadow(true)
                            .setAlignment(NEIClientUtils.Alignment.TopRight));
                } else {
                    String consumptionExponent = String
                        .format("%.2f", AntimatterForge.getFluidConsumptionExponents()[inputIndex - 1]);
                    fixed.setCustomBadge(
                        new Badge(
                            consumptionExponent,
                            StatCollector.translateToLocal("gg.recipe.antimatter_forge.stabilizer_tooltip"))
                                .setShadow(true)
                                .setAlignment(NEIClientUtils.Alignment.BottomLeft));
                }
            }
            inputIndex++;
        }

        for (PositionedStack stack : recipe.mOutputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed && fixed.isFluid()) {
                fixed.setCustomBadge(
                    new Badge("+", StatCollector.translateToLocal("gg.recipe.antimatter_forge.antimatter_tooltip"))
                        .setShadow(true)
                        .setAlignment(NEIClientUtils.Alignment.TopRight));
            }
        }
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {}
}
