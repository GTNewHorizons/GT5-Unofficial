package gregtech.api.recipe.maps;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import codechicken.nei.PositionedStack;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitPhAdjustment;
import gregtech.nei.GT_NEI_DefaultHandler;

@ParametersAreNonnullByDefault
public class PurificationUnitPhAdjustmentFrontend extends RecipeMapFrontend {

    public PurificationUnitPhAdjustmentFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    @NotNull
    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        // Add pH adjustment values
        if (stack.isItemEqual(Materials.SodiumHydroxide.getDust(1))) {
            currentTip
                .add("+" + GT_MetaTileEntity_PurificationUnitPhAdjustment.PH_PER_ALKALINE_DUST * 64 + " pH/stack");
        } else
            if (stack.isItemEqual(GT_Utility.getFluidDisplayStack(Materials.HydrochloricAcid.getFluid(1000L), false))) {
                currentTip.add(GT_MetaTileEntity_PurificationUnitPhAdjustment.PH_PER_10_ACID_LITER * 100 + " pH/1000L");
            }
        return super.handleNEIItemTooltip(stack, currentTip, neiCachedRecipe);
    }

    @Override
    public void drawNEIOverlays(GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        neiCachedRecipe.mInputs.add(new PositionedStack(Materials.SodiumHydroxide.getDust(64), 46, 13, false));
        neiCachedRecipe.mInputs.add(
            new PositionedStack(
                GT_Utility.getFluidDisplayStack(Materials.HydrochloricAcid.getFluid(1000L), true),
                102,
                13,
                false));
    }
}
