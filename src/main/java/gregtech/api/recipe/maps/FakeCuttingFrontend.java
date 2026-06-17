package gregtech.api.recipe.maps;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.loaders.postload.recipes.FakeCuttingSpecialInfo;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FakeCuttingFrontend extends RecipeMapFrontend {

    public FakeCuttingFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        super.handleNEIItemTooltip(stack, currentTip, neiCachedRecipe);

        FluidStack fluid = GTUtility.getFluidFromContainerOrFluidDisplay(stack);
        if (fluid == null) return currentTip;

        GTRecipe currentRecipe = neiCachedRecipe.mRecipe;
        FakeCuttingSpecialInfo specialInfo = (FakeCuttingSpecialInfo) currentRecipe.mSpecialItems;
        float displayTime = (float) specialInfo.getDurationForFluid(fluid) / SECONDS;

        currentTip.add(
            EnumChatFormatting.AQUA
                + translateToLocalFormatted("GT5U.nei.display.duration.seconds", formatNumber(displayTime)));
        return currentTip;
    }
}
