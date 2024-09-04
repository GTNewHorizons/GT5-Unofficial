package tectech.recipe;

import static gregtech.api.util.GTUtility.trans;
import static tectech.loader.recipe.Godforge.magmatterItemsForNEI;
import static tectech.loader.recipe.Godforge.magmatterSpaceFluidItemsForNEI;
import static tectech.loader.recipe.Godforge.magmatterTimeFluidItemsForNEI;
import static tectech.loader.recipe.Godforge.quarkGluonFluidItemsForNEI;
import static tectech.loader.recipe.Godforge.quarkGluonItemsForNEI;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;

import codechicken.nei.PositionedStack;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;
import tectech.thing.gui.TecTechUITextures;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GodforgeExoticFrontend extends RecipeMapFrontend {

    public GodforgeExoticFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
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
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        if (neiCachedRecipe.mRecipe.mFluidOutputs[0].equals(MaterialsUEVplus.QuarkGluonPlasma.getFluid(1000))) {
            neiCachedRecipe.mInputs.set(0, new PositionedStack(quarkGluonItemsForNEI, 48, 23, true));
            neiCachedRecipe.mInputs.set(1, new PositionedStack(quarkGluonFluidItemsForNEI, 48, 52, true));
        } else {
            neiCachedRecipe.mInputs.set(0, new PositionedStack(magmatterItemsForNEI, 48, 23, true));
            neiCachedRecipe.mInputs.set(1, new PositionedStack(magmatterSpaceFluidItemsForNEI, 30, 52, true));
            neiCachedRecipe.mInputs.set(2, new PositionedStack(magmatterTimeFluidItemsForNEI, 48, 52, true));
        }
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
        recipeInfo.drawText(trans("152", "Total: ") + GTUtility.formatNumbers(eut * duration) + " EU");
        recipeInfo.drawText(trans("153", "Usage: ") + GTUtility.formatNumbers(eut) + " EU/t");
        recipeInfo.drawText(trans("158", "Time: ") + GTUtility.formatNumbers(duration / 20) + " secs");

    }

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {}

}
