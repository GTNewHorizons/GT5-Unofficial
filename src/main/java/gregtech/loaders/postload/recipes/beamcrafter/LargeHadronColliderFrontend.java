package gregtech.loaders.postload.recipes.beamcrafter;

import static gregtech.api.gui.modularui.GTUITextures.PROGRESSBAR_LHC_EM;
import static gregtech.api.gui.modularui.GTUITextures.PROGRESSBAR_LHC_G;
import static gregtech.api.gui.modularui.GTUITextures.PROGRESSBAR_LHC_S;
import static gregtech.api.gui.modularui.GTUITextures.PROGRESSBAR_LHC_W;
import static gregtech.api.recipe.RecipeMaps.LARGE_HADRON_COLLIDER_METADATA;

import java.util.List;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.GTNEIDefaultHandler.CachedDefaultRecipe;
import gregtech.nei.RecipeDisplayInfo;

public class LargeHadronColliderFrontend extends RecipeMapFrontend {

    private static final int PROGRESS_WIDTH = 170;
    private static final int PROGRESS_HEIGHT = 82;
    private static final float MIN_FRACTION = 64 / 170f;

    public LargeHadronColliderFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            uiPropertiesBuilder.logoPos(new Pos2d(138, 97)),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 164)));
    }

    @Override
    public void drawDescription(RecipeDisplayInfo recipeInfo) {}

    @Override
    public void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return ImmutableList.of(new Pos2d(28, 39));
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemInputCount) {
        return ImmutableList.of(
            new Pos2d(96, 23),
            new Pos2d(115, 19),
            new Pos2d(134, 19),
            new Pos2d(153, 23),
            new Pos2d(96, 63),
            new Pos2d(115, 67),
            new Pos2d(134, 67),
            new Pos2d(153, 63));
    }

    @Override
    public ModularWindow.Builder createNEITemplate(GTNEIDefaultHandler.NEITemplateContext ctx) {
        // Override regular createNEITemplate method, so we can remove the background texture with the ugly border.
        return super.createNEITemplate(ctx).setBackground();
    }

    @Override
    public void addProgressBar(ModularWindow.Builder builder, GTNEIDefaultHandler.NEITemplateContext ctx) {
        final IDrawable progressDrawable = (x, y, width, height, partialTicks) -> {
            float progress = ctx.progressSupplier.get();
            final CachedDefaultRecipe cachedRecipe = ctx.recipeSupplier.get();
            final UITexture currentProgressBarTexture = getProgressBarTexture(cachedRecipe);
            float filledWidth = PROGRESS_WIDTH * (MIN_FRACTION + (1f - MIN_FRACTION) * progress);

            // draw background (upper half of texture file)
            currentProgressBarTexture.drawSubArea(x, y, width, height, 0f, 0f, 1f, 0.5f);
            // draw progress arrow (lower half of texture file, clipped to progress)
            currentProgressBarTexture
                .drawSubArea(x, y, filledWidth, height, 0f, 0.5f, MIN_FRACTION + (1f - MIN_FRACTION) * progress, 1f);
        };

        builder.widget(
            new DrawableWidget().setDrawable(progressDrawable)
                .setPos(new Pos2d(3, 11).add(ctx.windowOffset))
                .setSize(PROGRESS_WIDTH, PROGRESS_HEIGHT));
    }

    private static UITexture getProgressBarTexture(CachedDefaultRecipe cachedRecipe) {
        final LargeHadronColliderMetadata metadata = cachedRecipe.mRecipe.getMetadata(LARGE_HADRON_COLLIDER_METADATA);
        if (metadata == null) return PROGRESSBAR_LHC_EM;

        return switch (metadata.progressBarTextureIndex) {
            case 1 -> PROGRESSBAR_LHC_W;
            case 2 -> PROGRESSBAR_LHC_S;
            case 3 -> PROGRESSBAR_LHC_G;
            default -> PROGRESSBAR_LHC_EM;
        };
    }

}
