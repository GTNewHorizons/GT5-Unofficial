package gregtech.nei;

import static gregtech.api.util.GT_Utility.isStringInvalid;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.Minecraft;

import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;

/**
 * Holds info used for drawing descriptions on NEI.
 */
public class NEIRecipeInfo {

    /**
     * Recipe to show description.
     */
    public final GT_Recipe recipe;

    /**
     * RecipeMap the recipe belongs to.
     */
    public final RecipeMap<?> recipeMap;

    /**
     * Recipe object for NEI.
     */
    public final GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe;

    /**
     * When user looks up usage for machine, NEI will show all the recipes that the machine can process, taking tier of
     * the machine into consideration. This object can be used to show info around overclocked EU/t and duration.
     */
    public final OverclockDescriber overclockDescriber;

    /**
     * Pre-built overclock calculator, used for drawing OC information. Do not calculate it again.
     */
    public final GT_OverclockCalculator calculator;

    /**
     * Current Y position for drawing description.
     */
    public int yPos;

    private final int neiTextColorOverride;

    NEIRecipeInfo(GT_Recipe recipe, RecipeMap<?> recipeMap, GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe,
        OverclockDescriber overclockDescriber, GT_OverclockCalculator calculator, int descriptionYOffset,
        int neiTextColorOverride) {
        this.recipe = recipe;
        this.recipeMap = recipeMap;
        this.neiCachedRecipe = neiCachedRecipe;
        this.overclockDescriber = overclockDescriber;
        this.calculator = calculator;
        this.yPos = descriptionYOffset;
        this.neiTextColorOverride = neiTextColorOverride;
    }

    public void drawNEIText(@Nullable String text) {
        drawNEIText(text, 10);
    }

    /**
     * Draws text on NEI recipe.
     *
     * @param yShift y position to shift after this text
     */
    public void drawNEIText(@Nullable String text, int yShift) {
        drawNEIText(text, 5, yShift);
    }

    /**
     * Draws text on NEI recipe.
     *
     * @param xStart x position to start drawing
     * @param yShift y position to shift after this text
     */
    public void drawNEIText(@Nullable String text, int xStart, int yShift) {
        if (isStringInvalid(text)) return;
        Minecraft.getMinecraft().fontRenderer
            .drawString(text, xStart, yPos, neiTextColorOverride != -1 ? neiTextColorOverride : 0x000000);
        yPos += yShift;
    }

    public void drawNEITextMultipleLines(List<String> texts) {
        for (String text : texts) {
            drawNEIText(text, 10);
        }
    }
}
