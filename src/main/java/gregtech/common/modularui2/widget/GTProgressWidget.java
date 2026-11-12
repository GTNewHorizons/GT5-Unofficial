package gregtech.common.modularui2.widget;

import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.widget.Interactable;
import com.cleanroommc.modularui.theme.WidgetThemeEntry;
import com.cleanroommc.modularui.widgets.ProgressWidget;

import codechicken.nei.recipe.GuiCraftingRecipe;
import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMap;
import gregtech.common.modularui2.theme.ProgressbarWidgetTheme;

/**
 * Progressbar widget that can:
 * <ol>
 * <li>Have NEI transferrect; An area which player can click to see relevant recipes on NEI.</li>
 * <li>Specify widget theme to use for progressbar texture. Textures specified by {@link ProgressWidget#texture} will be
 * ignored in such a case.</li>
 * </ol>
 */
public class GTProgressWidget extends ProgressWidget implements Interactable {

    private String transferRectID;
    private Object[] transferRectArgs;

    /**
     * Sets NEI transferrect; An area which player can click to see relevant recipes on NEI.
     *
     * @param transferRectID      {@code outputId} that gets passed to
     *                            {@link codechicken.nei.recipe.ICraftingHandler#getRecipeHandler
     *                            ICraftingHandler.getRecipeHandler}.
     * @param transferRectArgs    {@code results} that gets passed to
     *                            {@link codechicken.nei.recipe.ICraftingHandler#getRecipeHandler
     *                            ICraftingHandler.getRecipeHandler}.
     * @param transferRectTooltip Tooltip shown when hovering over this widget.
     */
    public GTProgressWidget neiTransferRect(String transferRectID, Object[] transferRectArgs,
        String transferRectTooltip) {
        this.transferRectID = transferRectID;
        this.transferRectArgs = transferRectArgs == null ? GTValues.emptyObjectArray : transferRectArgs;
        if (transferRectTooltip != null) {
            tooltipBuilder(tooltip -> tooltip.add(transferRectTooltip));
        }
        return this;
    }

    /**
     * Sets NEI transferrect; An area which player can click to see relevant recipes on NEI.
     *
     * @param transferRectID {@code outputId} that gets passed to
     *                       {@link codechicken.nei.recipe.ICraftingHandler#getRecipeHandler
     *                       ICraftingHandler.getRecipeHandler}.
     */
    public GTProgressWidget neiTransferRect(String transferRectID) {
        return neiTransferRect(
            transferRectID,
            GTValues.emptyObjectArray,
            StatCollector.translateToLocal("nei.recipe.tooltip"));
    }

    /**
     * Sets NEI transferrect; An area which player can click to see relevant recipes on NEI.
     *
     * @param recipeMap RecipeMap whose recipes get displayed on widget click.
     */
    public GTProgressWidget neiTransferRect(RecipeMap<?> recipeMap) {
        return neiTransferRect(
            recipeMap.getFrontend()
                .getUIProperties().neiTransferRectId);
    }

    @Override
    public void onInit() {
        WidgetThemeEntry<?> widgetTheme = getWidgetTheme(getContext().getTheme());
        if (widgetTheme.getTheme() instanceof ProgressbarWidgetTheme progressbarWidgetTheme) {
            texture(
                progressbarWidgetTheme.getEmptyTexture(),
                progressbarWidgetTheme.getFullTexture(),
                progressbarWidgetTheme.getImageSize());
        }
        super.onInit();
    }

    @Override
    public @NotNull Result onMousePressed(int mouseButton) {
        if (transferRectID != null) {
            GuiCraftingRecipe.openRecipeGui(transferRectID, transferRectArgs);
            return Result.SUCCESS;
        }
        return Interactable.super.onMousePressed(mouseButton);
    }
}
