package gtPlusPlus.api.recipe;

import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.maps.LargeNEIFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.GTNEIDefaultHandler;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTEQuantumForceTransformer;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.MTEChemicalPlant;

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
        for (PositionedStack stack : neiCachedRecipe.mInputs) {
            if (stack instanceof GTNEIDefaultHandler.FixedPositionedStack fixedPositionedStack) {
                if (fixedPositionedStack.isFluid()) continue;
                if (!MTEChemicalPlant.isCatalyst(stack.item)) continue;
                super.drawNEIOverlayText("NC(P)", stack);
            }
        }

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

    @Override
    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        for (PositionedStack catalyst : neiCachedRecipe.mInputs) {
            if (catalyst instanceof GTNEIDefaultHandler.FixedPositionedStack fixedPositionedStack) {
                if (fixedPositionedStack.isFluid()) continue;
                if (stack != catalyst.item) continue;
                if (!MTEChemicalPlant.isCatalyst(catalyst.item)) continue;
                currentTip.add(GRAY + StatCollector.translateToLocal("GT5U.recipes.not_consume_parallel"));
                return currentTip;
            }
        }
        return super.handleNEIItemTooltip(stack, currentTip, neiCachedRecipe);
    }
}
