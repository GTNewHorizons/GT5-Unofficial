package gregtech.api.recipe;

import java.util.Comparator;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import codechicken.nei.recipe.HandlerInfo;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.formatter.DefaultSpecialValueFormatter;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

/**
 * Builder class for {@link NEIRecipeProperties}.
 */
@SuppressWarnings("UnusedReturnValue")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public final class NEIRecipePropertiesBuilder {

    private boolean registerNEI = true;
    @Nullable
    private UnaryOperator<HandlerInfo.Builder> handlerInfoCreator;

    private Size recipeBackgroundSize = new Size(170, 82);
    private Pos2d recipeBackgroundOffset = new Pos2d(3, 3);

    private INEISpecialInfoFormatter neiSpecialInfoFormatter = DefaultSpecialValueFormatter.INSTANCE;

    private boolean unificateOutput = true;
    private boolean useCustomFilter;
    private boolean renderRealStackSizes = true;

    private Function<GTRecipe, ItemStack[]> itemInputsGetter = recipe -> recipe.mInputs;
    private Function<GTRecipe, FluidStack[]> fluidInputsGetter = recipe -> recipe.mFluidInputs;
    private Function<GTRecipe, ItemStack[]> itemOutputsGetter = recipe -> recipe.mOutputs;
    private Function<GTRecipe, FluidStack[]> fluidOutputsGetter = recipe -> recipe.mFluidOutputs;

    private Comparator<GTRecipe> comparator = GTRecipe::compareTo;

    NEIRecipePropertiesBuilder() {}

    public NEIRecipeProperties build() {
        return new NEIRecipeProperties(
            registerNEI,
            handlerInfoCreator,
            recipeBackgroundSize,
            recipeBackgroundOffset,
            neiSpecialInfoFormatter,
            unificateOutput,
            useCustomFilter,
            renderRealStackSizes,
            itemInputsGetter,
            fluidInputsGetter,
            itemOutputsGetter,
            fluidOutputsGetter,
            comparator);
    }

    public NEIRecipePropertiesBuilder disableRegisterNEI() {
        this.registerNEI = false;
        return this;
    }

    public NEIRecipePropertiesBuilder handlerInfoCreator(UnaryOperator<HandlerInfo.Builder> builderCreator) {
        this.handlerInfoCreator = builderCreator;
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

    public NEIRecipePropertiesBuilder neiSpecialInfoFormatter(INEISpecialInfoFormatter neiSpecialInfoFormatter) {
        this.neiSpecialInfoFormatter = neiSpecialInfoFormatter;
        return this;
    }

    public NEIRecipePropertiesBuilder unificateOutput(boolean unificateOutput) {
        this.unificateOutput = unificateOutput;
        return this;
    }

    public NEIRecipePropertiesBuilder useCustomFilter() {
        this.useCustomFilter = true;
        return this;
    }

    public NEIRecipePropertiesBuilder disableRenderRealStackSizes() {
        this.renderRealStackSizes = false;
        return this;
    }

    public NEIRecipePropertiesBuilder itemInputsGetter(Function<GTRecipe, ItemStack[]> itemInputsGetter) {
        this.itemInputsGetter = itemInputsGetter;
        return this;
    }

    public NEIRecipePropertiesBuilder fluidInputsGetter(Function<GTRecipe, FluidStack[]> fluidInputsGetter) {
        this.fluidInputsGetter = fluidInputsGetter;
        return this;
    }

    public NEIRecipePropertiesBuilder itemOutputsGetter(Function<GTRecipe, ItemStack[]> itemOutputsGetter) {
        this.itemOutputsGetter = itemOutputsGetter;
        return this;
    }

    public NEIRecipePropertiesBuilder fluidOutputsGetter(Function<GTRecipe, FluidStack[]> fluidOutputsGetter) {
        this.fluidOutputsGetter = fluidOutputsGetter;
        return this;
    }

    public NEIRecipePropertiesBuilder recipeComparator(Comparator<GTRecipe> comparator) {
        this.comparator = comparator;
        return this;
    }
}
