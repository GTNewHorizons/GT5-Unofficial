package gtPlusPlus.api.recipe;

import javax.annotation.ParametersAreNonnullByDefault;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.maps.LargeNEIFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GTNEIDefaultHandler;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEQuantumForceTransformer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class QuantumForceTransformerFrontend extends LargeNEIFrontend {

    public QuantumForceTransformerFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    public String getChanceFormat(int chance) {
        return GTNEIDefaultHandler.FixedPositionedStack.chanceFormat.format((float) chance / 10000);
    }

    @Override
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        // Replicates the default behaviour, but since we cannot actually modify the mChance variable we need to
        // essentially re-implement it.
        int chance = MTEQuantumForceTransformer.getBaseOutputChance(neiCachedRecipe.mRecipe);
        String chanceFormat = getChanceFormat(chance);
        for (PositionedStack stack : neiCachedRecipe.mOutputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack) {
                super.drawNEIOverlayText(chanceFormat, stack);
            }
        }
        super.drawNEIOverlays(neiCachedRecipe);
    }
}
