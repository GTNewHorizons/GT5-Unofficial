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
import gregtech.api.objects.overclockdescriber.OverclockDescriber;
import gregtech.api.util.FieldsAreNonnullByDefault;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.formatter.INEISpecialInfoFormatter;

/**
 * Data object storing info exclusively used to draw NEI recipe GUI. Not all the properties used to draw NEI are present
 * here. See {@link BasicUIProperties} for the rest.
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
     * Formats special description for the recipe, mainly {@link GTRecipe#mSpecialValue}.
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
     * Specifies what item inputs get displayed on NEI.
     */
    public final Function<GTRecipe, ItemStack[]> itemInputsGetter;
    /**
     * Specifies what fluid inputs get displayed on NEI.
     */
    public final Function<GTRecipe, FluidStack[]> fluidInputsGetter;
    /**
     * Specifies what item outputs get displayed on NEI.
     */
    public final Function<GTRecipe, ItemStack[]> itemOutputsGetter;
    /**
     * Specifies what fluid outputs get displayed on NEI.
     */
    public final Function<GTRecipe, FluidStack[]> fluidOutputsGetter;

    /**
     * Comparator for NEI recipe sort. {@link GTRecipe#compareTo(GTRecipe)} by default.
     */
    public final Comparator<GTRecipe> comparator;

    NEIRecipeProperties(boolean registerNEI, @Nullable UnaryOperator<HandlerInfo.Builder> handlerInfoCreator,
        Size recipeBackgroundSize, Pos2d recipeBackgroundOffset, INEISpecialInfoFormatter neiSpecialInfoFormatter,
        boolean unificateOutput, boolean useCustomFilter, boolean renderRealStackSizes,
        Function<GTRecipe, ItemStack[]> itemInputsGetter, Function<GTRecipe, FluidStack[]> fluidInputsGetter,
        Function<GTRecipe, ItemStack[]> itemOutputsGetter, Function<GTRecipe, FluidStack[]> fluidOutputsGetter,
        Comparator<GTRecipe> comparator) {
        this.registerNEI = registerNEI;
        this.handlerInfoCreator = handlerInfoCreator;
        this.recipeBackgroundOffset = recipeBackgroundOffset;
        this.recipeBackgroundSize = recipeBackgroundSize;
        this.neiSpecialInfoFormatter = neiSpecialInfoFormatter;
        this.unificateOutput = unificateOutput;
        this.useCustomFilter = useCustomFilter;
        this.renderRealStackSizes = renderRealStackSizes;
        this.itemInputsGetter = itemInputsGetter;
        this.fluidInputsGetter = fluidInputsGetter;
        this.itemOutputsGetter = itemOutputsGetter;
        this.fluidOutputsGetter = fluidOutputsGetter;
        this.comparator = comparator;
    }
}
