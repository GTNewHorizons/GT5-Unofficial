package gregtech.api.recipe;

import java.util.Comparator;
import java.util.function.UnaryOperator;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import codechicken.nei.recipe.HandlerInfo;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.util.FieldsAreNonnullByDefault;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

/**
 * Data object storing info exclusively used to draw NEI recipe GUI. Not all the properties used to draw NEI
 * are present here. See {@link BasicUIProperties} for the rest.
 * <p>
 * Use {@link #builder} for creation.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public final class NEIRecipeProperties {

    static NEIRecipePropertiesBuilder builder() {
        return new NEIRecipePropertiesBuilder();
    }

    /**
     * Whether to register dedicated NEI recipe page for the recipemap.
     */
    public final boolean registerNEI;
    public final ModContainer ownerMod;
    @Nullable
    public final UnaryOperator<HandlerInfo.Builder> handlerInfoCreator;

    /**
     * Size of background shown.
     */
    // todo make it final
    public Size recipeBackgroundSize;
    /**
     * Offset of background shown.
     */
    public final Pos2d recipeBackgroundOffset;

    /**
     * Formats special description for the recipe, mainly {@link gregtech.api.util.GT_Recipe#mSpecialValue}.
     */
    public final INEISpecialInfoFormatter neiSpecialInfoFormatter;

    /**
     * Whether to show oredict equivalent item outputs.
     */
    public final boolean unificateOutput;
    /**
     * If a custom filter method {@link OverclockDescriber#canHandle} should be used to limit the shown recipes when
     * searching recipes with recipe catalyst. Else, the voltage of the recipe is the only factor to filter recipes.
     */
    public final boolean useCustomFilter;
    /**
     * Whether to render the actual stack size of items or not.
     */
    public final boolean renderRealStackSizes;

    /**
     * Comparator for NEI recipe sort. {@link GT_Recipe#compareTo(GT_Recipe)} by default.
     */
    public final Comparator<GT_Recipe> comparator;

    NEIRecipeProperties(boolean registerNEI, @Nullable UnaryOperator<HandlerInfo.Builder> handlerInfoCreator,
        Size recipeBackgroundSize, Pos2d recipeBackgroundOffset, INEISpecialInfoFormatter neiSpecialInfoFormatter,
        boolean unificateOutput, boolean useCustomFilter, boolean renderRealStackSizes,
        Comparator<GT_Recipe> comparator) {
        this.registerNEI = registerNEI;
        this.ownerMod = Loader.instance()
            .activeModContainer();
        this.handlerInfoCreator = handlerInfoCreator;
        this.recipeBackgroundOffset = recipeBackgroundOffset;
        this.recipeBackgroundSize = recipeBackgroundSize;
        this.neiSpecialInfoFormatter = neiSpecialInfoFormatter;
        this.unificateOutput = unificateOutput;
        this.useCustomFilter = useCustomFilter;
        this.renderRealStackSizes = renderRealStackSizes;
        this.comparator = comparator;
    }
}
