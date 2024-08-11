package gtPlusPlus.api.recipe;

import javax.annotation.ParametersAreNonnullByDefault;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.maps.LargeNEIFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GT_NEI_DefaultHandler;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_QuantumForceTransformer;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class QuantumForceTransformerFrontend extends LargeNEIFrontend {

    public QuantumForceTransformerFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    public String getChanceFormat(int chance) {
        return GT_NEI_DefaultHandler.FixedPositionedStack.chanceFormat.format((float) chance / 10000);
    }

    @Override
    public void drawNEIOverlays(GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        // Replicates the default behaviour, but since we cannot actually modify the mChance variable we need to
        // essentially re-implement it.
        int chance = GregtechMetaTileEntity_QuantumForceTransformer.getBaseOutputChance(neiCachedRecipe.mRecipe);
        String chanceFormat = getChanceFormat(chance);
        for (PositionedStack stack : neiCachedRecipe.mOutputs) {
            if (stack instanceof GT_NEI_DefaultHandler.FixedPositionedStack) {
                super.drawNEIOverlayText(chanceFormat, stack);
            }
        }
        super.drawNEIOverlays(neiCachedRecipe);
    }
}
