package gregtech.api.recipe;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import gregtech.api.util.FieldsAreNonnullByDefault;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.power.Power;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

/**
 * Data object storing info exclusively used to draw NEI recipe GUI. Not all the properties used to draw NEI
 * are present here. See {@link BasicUIProperties} for the rest.
 * <p>
 * Use {@link #builder()} for creation.
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
@FieldsAreNonnullByDefault
public class NEIRecipeProperties {

    public static NEIRecipePropertiesBuilder builder() {
        return new NEIRecipePropertiesBuilder();
    }

    /**
     * Whether to register dedicated NEI recipe page for the recipemap.
     */
    public final boolean registerNEI;

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
     * If NEI description should show amperage.
     */
    public final boolean showVoltageAmperage;
    /**
     * Formats special description for the recipe, mainly {@link gregtech.api.util.GT_Recipe#mSpecialValue}.
     */
    public final INEISpecialInfoFormatter neiSpecialInfoFormatter;

    /**
     * Whether to show oredict equivalent item outputs.
     */
    public final boolean unificateOutput;
    /**
     * If a custom filter method {@link Power#canHandle} should be used to limit the shown recipes when searching
     * recipes with recipe catalyst. Else, the voltage of the recipe is the only factor to filter recipes.
     */
    public final boolean useCustomFilter;
    /**
     * Whether to render the actual stack size of items or not.
     */
    public final boolean renderRealStackSizes;

    NEIRecipeProperties(boolean registerNEI, Size recipeBackgroundSize, Pos2d recipeBackgroundOffset,
        boolean showVoltageAmperage, INEISpecialInfoFormatter neiSpecialInfoFormatter, boolean unificateOutput,
        boolean useCustomFilter, boolean renderRealStackSizes) {
        this.registerNEI = registerNEI;
        this.recipeBackgroundOffset = recipeBackgroundOffset;
        this.recipeBackgroundSize = recipeBackgroundSize;
        this.showVoltageAmperage = showVoltageAmperage;
        this.neiSpecialInfoFormatter = neiSpecialInfoFormatter;
        this.unificateOutput = unificateOutput;
        this.useCustomFilter = useCustomFilter;
        this.renderRealStackSizes = renderRealStackSizes;
    }
}
