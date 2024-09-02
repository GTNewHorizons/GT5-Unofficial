package tectech.recipe;

import static gregtech.api.util.GTUtility.trans;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTUtility;
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
        if (recipeInfo.recipe.mSpecialItems.toString()
            .equals("true")) {
            multistep = "Yes";
        }
        String requiredUpgrade = switch (recipeInfo.recipe.mSpecialValue) {
            case 1 -> "T4-T5";
            case 2 -> "Exotic";
            default -> "T1-T3";
        };

        recipeInfo.drawText(trans("152", "Total: ") + GTUtility.formatNumbers(eut * duration) + " EU");
        recipeInfo.drawText(trans("153", "Usage: ") + GTUtility.formatNumbers(eut) + " EU/t");
        recipeInfo.drawText(
            trans("158", "Time: ") + GTUtility.formatNumbers(duration / 20d)
                + " secs"
                + (duration < 20 ? " (" + duration + " ticks)" : ""));
        recipeInfo.drawText(translateToLocal("gt.blockmachines.multimachine.FOG.plasmamultistep") + ": " + multistep);
        recipeInfo
            .drawText(translateToLocal("gt.blockmachines.multimachine.FOG.plasmarecipetier") + ": " + requiredUpgrade);
    }

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {}

}
