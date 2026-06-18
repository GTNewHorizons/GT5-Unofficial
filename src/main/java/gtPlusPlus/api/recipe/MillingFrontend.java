package gtPlusPlus.api.recipe;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GTNEIDefaultHandler;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.MTEIsaMill;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MillingFrontend extends RecipeMapFrontend {

    public MillingFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe recipe) {
        for (PositionedStack pStack : recipe.mInputs) {
            if (pStack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed
                && MTEIsaMill.isMillingBall(pStack.item)) {
                fixed.setCustomBadge("NC*", StatCollector.translateToLocal("gtpp.nei.milling.not_consumed"));
            }
        }
    }

}
