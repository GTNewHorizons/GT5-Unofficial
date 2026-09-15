package gregtech.nei;

import static gregtech.api.util.GTUtility.isStringInvalid;

import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;

import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.FieldsAreNonnullByDefault;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.api.util.OverclockCalculator;

/**
 * Holds info used for drawing descriptions on NEI.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public class RecipeDisplayInfo {

    /**
     * Recipe to show description.
     */
    public final GTRecipe recipe;

    /**
     * RecipeMap the recipe belongs to.
     */
    public final RecipeMap<?> recipeMap;

    /**
     * When user looks up usage for machine, NEI will show all the recipes that the machine can process, taking tier of
     * the machine into consideration. This object can be used to show info around overclocked EU/t and duration.
     */
    public final OverclockDescriber overclockDescriber;

    /**
     * Pre-built overclock calculator, used for drawing OC information. Do not calculate it again.
     */
    public final OverclockCalculator calculator;

    /**
     * Current Y position for drawing description.
     */
    private int yPos;

    private final int neiTextColorOverride;

    RecipeDisplayInfo(GTRecipe recipe, RecipeMap<?> recipeMap, OverclockDescriber overclockDescriber,
        OverclockCalculator calculator, int descriptionYOffset, int neiTextColorOverride) {
        this.recipe = recipe;
        this.recipeMap = recipeMap;
        this.overclockDescriber = overclockDescriber;
        this.calculator = calculator;
        this.yPos = descriptionYOffset;
        this.neiTextColorOverride = neiTextColorOverride;
    }

    /**
     * Draws text.
     */
    public void drawText(@Nullable String text) {
        drawText(text, 10);
    }

    /**
     * Draws text.
     *
     * @param yShift y position to shift after this text
     */
    public void drawText(@Nullable String text, int yShift) {
        drawText(text, 5, yShift);
    }

    /**
     * Draws text.
     *
     * @param xStart x position to start drawing
     * @param yShift y position to shift after this text
     */
    public void drawText(@Nullable String text, int xStart, int yShift) {
        if (isStringInvalid(text)) return;
        Minecraft.getMinecraft().fontRenderer
            .drawString(text, xStart, yPos, neiTextColorOverride != -1 ? neiTextColorOverride : 0x000000);
        yPos += yShift;
    }

    public void drawTextMultipleLines(List<String> texts) {
        for (String text : texts) {
            drawText(text, 10);
        }
    }
}
