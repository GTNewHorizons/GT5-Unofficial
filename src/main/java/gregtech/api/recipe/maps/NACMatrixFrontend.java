package gregtech.api.recipe.maps;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.nei.GTNEIDefaultHandler;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class NACMatrixFrontend extends AssemblyLineFrontend {

    public NACMatrixFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe recipe) {
        super.prepareRecipe(recipe);

        for (PositionedStack neiStack : recipe.mOutputs) {
            ItemStack stack = neiStack.item;
            CircuitComponent cc = CircuitComponent.tryGetFromFakeStack(stack);
            if (cc == null || cc.xorResult == null) continue;
            if (neiStack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed) {
                fixed.setCustomBadge(
                    String.format("%d%%", Math.floorDiv(cc.xorSuccessChance, 100)),
                    StatCollector.translateToLocalFormatted(
                        "GT5U.recipes.chance.unorganized",
                        Math.floorDiv(10000 - cc.xorSuccessChance, 100)));
            }
        }
    }
}
