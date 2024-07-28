package gregtech.api.recipe.maps;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import codechicken.nei.PositionedStack;
import gregtech.api.enums.Materials;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitPhAdjustment;
import gregtech.nei.GT_NEI_DefaultHandler;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PurificationUnitPhAdjustmentFrontend extends PurificationUnitRecipeMapFrontend {

    public PurificationUnitPhAdjustmentFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            80,
            uiPropertiesBuilder.logoPos(new Pos2d(160, 100))
                .progressBarTexture(new FallbackableUITexture(GT_UITextures.PROGRESSBAR_PH_NEUTRALIZATION))
                .logoPos(new Pos2d(152, 90)),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 120)));
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        ArrayList<Pos2d> positions = new ArrayList<>();
        positions.add(new Pos2d(42, 44));
        return positions;
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        ArrayList<Pos2d> positions = new ArrayList<>();
        positions.add(new Pos2d(116, 44));
        return positions;
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
        neiCachedRecipe.mInputs.add(new PositionedStack(Materials.SodiumHydroxide.getDust(64), 3, 1, false));
        neiCachedRecipe.mInputs.add(
            new PositionedStack(
                GT_Utility.getFluidDisplayStack(Materials.HydrochloricAcid.getFluid(1000L), true),
                147,
                1,
                false));
    }
}
