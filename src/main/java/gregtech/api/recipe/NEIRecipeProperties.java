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
    public Size neiBackgroundSize;
    /**
     * Offset of background shown.
     */
    public final Pos2d neiBackgroundOffset;

    /**
     * If NEI description should show amperage.
     */
    public final boolean showVoltageAmperageInNEI;
    /**
     * Formats special description for the recipe, mainly {@link gregtech.api.util.GT_Recipe#mSpecialValue}.
     */
    public final INEISpecialInfoFormatter neiSpecialInfoFormatter;

    /**
     * Whether to show oredict equivalent item outputs.
     */
    public final boolean unificateOutputNEI;
    /**
     * If a comparator should be used to search the recipe (which is defined in {@link Power}).
     * Else only the voltage will be used to find recipes.
     */
    public final boolean useComparatorForNEI;
    /**
     * Whether to render the actual stack size of items or not.
     */
    public final boolean renderRealStackSizes;

    NEIRecipeProperties(boolean registerNEI, Size neiBackgroundSize, Pos2d neiBackgroundOffset,
        boolean showVoltageAmperageInNEI, INEISpecialInfoFormatter neiSpecialInfoFormatter, boolean unificateOutputNEI,
        boolean useComparatorForNEI, boolean renderRealStackSizes) {
        this.registerNEI = registerNEI;
        this.neiBackgroundOffset = neiBackgroundOffset;
        this.neiBackgroundSize = neiBackgroundSize;
        this.showVoltageAmperageInNEI = showVoltageAmperageInNEI;
        this.neiSpecialInfoFormatter = neiSpecialInfoFormatter;
        this.unificateOutputNEI = unificateOutputNEI;
        this.useComparatorForNEI = useComparatorForNEI;
        this.renderRealStackSizes = renderRealStackSizes;
    }
}
