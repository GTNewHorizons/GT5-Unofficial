package gregtech.api.recipe.maps;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GTNEIDefaultHandler;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.MTEChemicalPlant;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class QuantumForceTransformerFrontend extends LargeNEIFrontend {

    public QuantumForceTransformerFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        super.prepareRecipe(neiCachedRecipe);

        for (PositionedStack stack : neiCachedRecipe.mInputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed && !fixed.isFluid()
                && MTEChemicalPlant.isCatalyst(stack.item)) {
                fixed.setCustomBadge("NC(P)", StatCollector.translateToLocal("GT5U.recipes.not_consume_parallel"));
            }
        }

        GTRecipe tRecipe = neiCachedRecipe.mRecipe;
        final int chance = 10000 / (tRecipe.mOutputs.length + tRecipe.mFluidOutputs.length);
        for (PositionedStack stack : neiCachedRecipe.mOutputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed) {
                fixed.setChance(chance * PositionedStack.CHANCE_FULL / 10000);
            }
        }
    }

}
