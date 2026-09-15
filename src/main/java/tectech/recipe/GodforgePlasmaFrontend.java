package tectech.recipe;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.util.GTRecipeConstants.FOG_PLASMA_MULTISTEP;
import static gregtech.api.util.GTRecipeConstants.FOG_PLASMA_TIER;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;
import tectech.thing.gui.TecTechUITextures;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GodforgePlasmaFrontend extends RecipeMapFrontend {

    public GodforgePlasmaFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder, Pos2d windowOffset) {
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_GODFORGE_LOGO)
                .setSize(18, 18)
                .setPos(new Pos2d(151, 63).add(windowOffset)));
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return Collections.singletonList(new Pos2d(52, 33));
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return Collections.singletonList(new Pos2d(106, 33));
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {
        long eut = recipeInfo.recipe.mEUt;
        long duration = recipeInfo.recipe.mDuration;
        String multistep = "No";
        if (recipeInfo.recipe.getMetadataOrDefault(FOG_PLASMA_MULTISTEP, false)) {
            multistep = "Yes";
        }
        String requiredUpgrade = switch (recipeInfo.recipe.getMetadataOrDefault(FOG_PLASMA_TIER, 0)) {
            case 1 -> "T4-T5";
            case 2 -> "Exotic";
            default -> "T1-T3";
        };

        recipeInfo.drawText(
            StatCollector.translateToLocalFormatted("GT5U.gui.text.total_line", formatNumber(eut * duration)));

        recipeInfo.drawText(StatCollector.translateToLocalFormatted("GT5U.gui.text.usage_line", formatNumber(eut)));

        if (duration < 20) {
            recipeInfo.drawText(
                StatCollector.translateToLocalFormatted(
                    "GT5U.gui.text.time_line_with_ticks",
                    formatNumber(duration / 20d),
                    duration));
        } else {
            recipeInfo.drawText(
                StatCollector.translateToLocalFormatted("GT5U.gui.text.time_line", formatNumber(duration / 20d)));
        }

        recipeInfo.drawText(
            StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.FOG.plasmamultistep", multistep));

        recipeInfo.drawText(
            StatCollector
                .translateToLocalFormatted("gt.blockmachines.multimachine.FOG.plasmarecipetier", requiredUpgrade));

    }

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {}

}
