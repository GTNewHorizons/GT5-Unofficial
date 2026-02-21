package gregtech.loaders.postload.recipes.beamcrafter;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gtnhlanth.common.beamline.Particle.isParticle;

import java.util.List;

import net.minecraft.util.StatCollector;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;
import gtnhlanth.util.Util;

public class BeamCrafterFrontend extends RecipeMapFrontend {

    public BeamCrafterFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    public void drawDescription(RecipeDisplayInfo recipeInfo) {
        drawEnergyInfo(recipeInfo);
        // drawDurationInfo(recipeInfo);
        drawSpecialInfo(recipeInfo);
        drawMetadataInfo(recipeInfo);
        drawRecipeOwnerInfo(recipeInfo);
    }

    @Override
    public void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        if (recipeInfo.calculator.getConsumption() <= 0) return;
        recipeInfo.drawText(getEUtDisplay(recipeInfo.calculator));
        recipeInfo.drawText(getAmperageString(recipeInfo.calculator));

    }

    // items
    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return Util.getGridPositions(itemInputCount, 24, 20, 2, 2, 1);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 128, 20, 2, 1);
    }

    // fluids
    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return Util.getGridPositions(fluidInputCount, 24, 60, 2, 1, 1);
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getGridPositions(fluidOutputCount, 128, 40, 2, 1);
    }

    @Override
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        List<Pos2d> positions = ImmutableList.of(new Pos2d(70, -6), new Pos2d(90, -6));

        int i = 0;
        for (PositionedStack stack : neiCachedRecipe.mInputs) {
            if (isParticle(stack.item)) {
                stack.relx = (int) positions.get(i)
                    .getX();
                stack.rely = (int) positions.get(i)
                    .getY();
                drawNEIOverlayForInput((GTNEIDefaultHandler.FixedPositionedStack) stack);
                i++;
            } else if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack) {
                drawNEIOverlayForInput((GTNEIDefaultHandler.FixedPositionedStack) stack);
            }
        }
        for (PositionedStack stack : neiCachedRecipe.mOutputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack) {
                drawNEIOverlayForOutput((GTNEIDefaultHandler.FixedPositionedStack) stack);
            }
        }

    }

    private String getEUtDisplay(OverclockCalculator calculator) {
        long eut = calculator.getConsumption();
        return StatCollector.translateToLocalFormatted(
            "GT5U.nei.display.usage",
            formatNumber(eut),
            GTUtility.getTierNameWithParentheses(eut));
    }

    private String getAmperageString(OverclockCalculator calculator) {
        return StatCollector.translateToLocalFormatted("GT5U.nei.display.amperage", formatNumber(1));
    }

}
