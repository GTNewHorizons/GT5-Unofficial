package gregtech.api.recipe;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.formatter.DefaultSpecialValueFormatter;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

/**
 * Builder class for {@link NEIRecipeProperties}.
 */
@SuppressWarnings("UnusedReturnValue")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NEIRecipePropertiesBuilder {

    private boolean registerNEI = true;

    private Size recipeBackgroundSize = new Size(170, 82);
    private Pos2d recipeBackgroundOffset = new Pos2d(3, 3);

    private boolean showVoltageAmperage = true;
    private INEISpecialInfoFormatter neiSpecialInfoFormatter = DefaultSpecialValueFormatter.INSTANCE;

    private boolean unificateOutput = true;
    private boolean useComparator;
    private boolean renderRealStackSizes = true;

    NEIRecipePropertiesBuilder() {}

    public NEIRecipeProperties build() {
        return new NEIRecipeProperties(
            registerNEI,
            recipeBackgroundSize,
            recipeBackgroundOffset,
            showVoltageAmperage,
            neiSpecialInfoFormatter,
            unificateOutput,
            useComparator,
            renderRealStackSizes);
    }

    public NEIRecipePropertiesBuilder disableRegisterNEI() {
        this.registerNEI = false;
        return this;
    }

    public NEIRecipePropertiesBuilder recipeBackgroundSize(Size recipeBackgroundSize) {
        this.recipeBackgroundSize = recipeBackgroundSize;
        return this;
    }

    public NEIRecipePropertiesBuilder recipeBackgroundOffset(Pos2d recipeBackgroundOffset) {
        this.recipeBackgroundOffset = recipeBackgroundOffset;
        return this;
    }

    public NEIRecipePropertiesBuilder disableVoltageAmperageInNEI() {
        this.showVoltageAmperage = false;
        return this;
    }

    public NEIRecipePropertiesBuilder neiSpecialInfoFormatter(INEISpecialInfoFormatter neiSpecialInfoFormatter) {
        this.neiSpecialInfoFormatter = neiSpecialInfoFormatter;
        return this;
    }

    public NEIRecipePropertiesBuilder unificateOutputNEI(boolean unificateOutputNEI) {
        this.unificateOutput = unificateOutputNEI;
        return this;
    }

    public NEIRecipePropertiesBuilder useComparatorForNEI() {
        this.useComparator = true;
        return this;
    }

    public NEIRecipePropertiesBuilder disableRenderRealStackSizes() {
        this.renderRealStackSizes = false;
        return this;
    }
}
