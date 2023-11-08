package gregtech.api.recipe;

import java.util.HashMap;
import java.util.Map;
import java.util.function.UnaryOperator;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import codechicken.nei.drawable.DrawableBuilder;
import codechicken.nei.drawable.DrawableResource;
import codechicken.nei.recipe.HandlerInfo;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import gregtech.api.util.FieldsAreNonnullByDefault;
import gregtech.api.util.MethodsReturnNonnullByDefault;

/**
 * Used to display recipes on NEI on different tabs.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public final class RecipeCategory {

    public static final Map<String, RecipeCategory> ALL_RECIPE_CATEGORIES = new HashMap<>();

    public final String unlocalizedName;
    public final RecipeMap<?> recipeMap;
    public final ModContainer ownerMod;
    @Nullable
    public final UnaryOperator<HandlerInfo.Builder> handlerInfoCreator;

    /**
     * @param unlocalizedName    Unlocalized name of this category. Must be unique.
     * @param recipeMap          RecipeMap this category belongs to.
     * @param handlerInfoCreator Supplier of handler info for the NEI handler this category belongs to.
     */
    public RecipeCategory(String unlocalizedName, RecipeMap<?> recipeMap,
        @Nullable UnaryOperator<HandlerInfo.Builder> handlerInfoCreator) {
        this.unlocalizedName = unlocalizedName;
        this.recipeMap = recipeMap;
        this.ownerMod = Loader.instance()
            .activeModContainer();
        this.handlerInfoCreator = handlerInfoCreator;
        if (ALL_RECIPE_CATEGORIES.containsKey(unlocalizedName)) {
            throw new IllegalArgumentException(
                "Cannot register recipe category with duplicated unlocalized name: " + unlocalizedName);
        }
        ALL_RECIPE_CATEGORIES.put(unlocalizedName, this);
    }

    /**
     * Creates icon for recipe category. Size is 16px.
     */
    public static DrawableResource createIcon(String resourceLocation) {
        return new DrawableBuilder(resourceLocation, 0, 0, 16, 16)
            // GuiRecipeTab#drawForeground draws icon with 1px offset to make fuel icon (14px) prettier
            .addPadding(-1, 0, -1, 0)
            .setTextureSize(16, 16)
            .build();
    }
}
