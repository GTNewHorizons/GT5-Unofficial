package gregtech.api.recipe.maps;

import java.util.Arrays;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.items.ItemFluidDisplay;
import gregtech.nei.GTNEIDefaultHandler;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CauldronFrontend extends RecipeMapFrontend {

    public CauldronFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return Arrays.asList(
            UIHelper.getItemInputPositions(2)
                .get(0));
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getItemOutputPositions(1);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return Arrays.asList(
            UIHelper.getItemInputPositions(2)
                .get(1));
    }

    @Override
    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {

        if (stack.getItem() instanceof ItemFluidDisplay) {
            currentTip.add(EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.nei.cauldron.consumed"));
        }

        return super.handleNEIItemTooltip(stack, currentTip, neiCachedRecipe);
    }
}
