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

    private Size neiBackgroundSize = new Size(172, 82);
    private Pos2d neiBackgroundOffset = new Pos2d(2, 3);

    private boolean showVoltageAmperageInNEI = true;
    private INEISpecialInfoFormatter neiSpecialInfoFormatter = DefaultSpecialValueFormatter.INSTANCE;

    private boolean unificateOutputNEI = true;
    private boolean useComparatorForNEI;
    private boolean renderRealStackSizes = true;

    NEIRecipePropertiesBuilder() {}

    public NEIRecipeProperties build() {
        return new NEIRecipeProperties(
            registerNEI,
            neiBackgroundSize,
            neiBackgroundOffset,
            showVoltageAmperageInNEI,
            neiSpecialInfoFormatter,
            unificateOutputNEI,
            useComparatorForNEI,
            renderRealStackSizes);
    }

    public NEIRecipePropertiesBuilder disableRegisterNEI() {
        this.registerNEI = false;
        return this;
    }

    public NEIRecipePropertiesBuilder neiBackgroundSize(Size neiBackgroundSize) {
        this.neiBackgroundSize = neiBackgroundSize;
        return this;
    }

    public NEIRecipePropertiesBuilder neiBackgroundOffset(Pos2d neiBackgroundOffset) {
        this.neiBackgroundOffset = neiBackgroundOffset;
        return this;
    }

    public NEIRecipePropertiesBuilder disableVoltageAmperageInNEI() {
        this.showVoltageAmperageInNEI = false;
        return this;
    }

    public NEIRecipePropertiesBuilder neiSpecialInfoFormatter(INEISpecialInfoFormatter neiSpecialInfoFormatter) {
        this.neiSpecialInfoFormatter = neiSpecialInfoFormatter;
        return this;
    }

    public NEIRecipePropertiesBuilder unificateOutputNEI(boolean unificateOutputNEI) {
        this.unificateOutputNEI = unificateOutputNEI;
        return this;
    }

    public NEIRecipePropertiesBuilder useComparatorForNEI() {
        this.useComparatorForNEI = true;
        return this;
    }

    public NEIRecipePropertiesBuilder disableRenderRealStackSizes() {
        this.renderRealStackSizes = false;
        return this;
    }
}
