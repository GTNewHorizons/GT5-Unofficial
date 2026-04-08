package gregtech.loaders.postload.recipes.beamcrafter;

import static gregtech.api.gui.modularui.GTUITextures.PROGRESSBAR_LHC_EM;
import static gregtech.api.gui.modularui.GTUITextures.PROGRESSBAR_LHC_G;
import static gregtech.api.gui.modularui.GTUITextures.PROGRESSBAR_LHC_S;
import static gregtech.api.gui.modularui.GTUITextures.PROGRESSBAR_LHC_W;
import static gregtech.api.recipe.RecipeMaps.LARGE_HADRON_COLLIDER_METADATA;

import java.awt.Rectangle;

import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;

import codechicken.nei.recipe.TemplateRecipeHandler;
import gregtech.api.recipe.RecipeCategory;
import gregtech.api.util.GTRecipe;
import gregtech.nei.GTNEIDefaultHandler;

public class LHCNEIHandler extends GTNEIDefaultHandler {

    // Note: this was written almost from scratch so it might be different to standard implementations (if there are
    // any)

    public LHCNEIHandler(RecipeCategory recipeCategory) {
        super(recipeCategory);
    }

    @Override
    public TemplateRecipeHandler newInstance() {
        return new LHCNEIHandler(recipeCategory);
    }

    @Override
    public void drawBackground(int recipeIndex) {
        super.drawExtras(recipeIndex);

        CachedDefaultRecipe cached = (CachedDefaultRecipe) this.arecipes.get(recipeIndex);
        GTRecipe recipe = cached.mRecipe;

        drawCustomProgressBar(recipe);
    }

    @Override
    public void loadTransferRects() {
        transferRects.add(new RecipeTransferRect(new Rectangle(65, 30, 101, 22), "gt.recipe.large_hadron_collider"));
    }

    private void drawCustomProgressBar(GTRecipe recipe) {
        LargeHadronColliderMetadata metadata = recipe.getMetadata(LARGE_HADRON_COLLIDER_METADATA);
        assert metadata != null;
        int textureIndex = metadata.progressBarTextureIndex;
        UITexture dynamicProgressBar = PROGRESSBAR_LHC_EM;

        // hardcoded indices because there shouldn't ever be more LHC modules
        if (textureIndex == 1) {
            dynamicProgressBar = PROGRESSBAR_LHC_W;
        }
        if (textureIndex == 2) {
            dynamicProgressBar = PROGRESSBAR_LHC_S;
        }
        if (textureIndex == 3) {
            dynamicProgressBar = PROGRESSBAR_LHC_G;
        }

        Pos2d progressPos = new Pos2d(-2, 0);
        // fixed dimensions because there shouldn't ever be more LHC modules
        int progressWidth = 170;
        int progressHeight = 82;

        float progress = (float) (getDrawTicks() % 100) / 100f;
        // minFraction and arrowWidth exist so that there isn't 3 seconds of dead time when the progress bar resets
        float minFraction = 64 / 170f;
        float arrowWidth = (progressWidth * (minFraction + (1f - minFraction) * progress));

        // draw the background (upper half of texture file)
        dynamicProgressBar
            .drawSubArea(progressPos.getX(), progressPos.getY(), progressWidth, progressHeight, 0f, 0f, 1f, 0.5f);

        // draw the progress arrow (bottom half of texture file)
        dynamicProgressBar.drawSubArea(
            progressPos.getX(),
            progressPos.getY(),
            arrowWidth,
            progressHeight,
            0f,
            0.5f,
            minFraction + (1f - minFraction) * progress,
            1f);
    }
}
