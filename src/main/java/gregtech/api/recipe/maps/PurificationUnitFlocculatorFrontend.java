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
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.tileentities.machines.multi.purification.GT_MetaTileEntity_PurificationUnitFlocculation;
import gregtech.nei.GT_NEI_DefaultHandler;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PurificationUnitFlocculatorFrontend extends PurificationUnitRecipeMapFrontend {

    public PurificationUnitFlocculatorFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            80,
            uiPropertiesBuilder.logoPos(new Pos2d(160, 100))
                .progressBarTexture(new FallbackableUITexture(GT_UITextures.PROGRESSBAR_FLOCCULATION))
                .logoPos(new Pos2d(152, 100)),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 120)));
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        ArrayList<Pos2d> positions = new ArrayList<>();
        positions.add(new Pos2d(9, 39));
        return positions;
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        ArrayList<Pos2d> positions = new ArrayList<>();
        positions.add(new Pos2d(151, 39));
        return positions;
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 115, 80, 3, 1);
    }

    @Override
    @NotNull
    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        if (stack
            .isItemEqual(GT_Utility.getFluidDisplayStack(Materials.PolyAluminiumChloride.getFluid(1000L), false))) {
            currentTip.add("Consumed during operation");
            currentTip.add(
                "+" + GT_MetaTileEntity_PurificationUnitFlocculation.SUCCESS_PER_LEVEL
                    + "%/"
                    + GT_MetaTileEntity_PurificationUnitFlocculation.IRON_III_PER_LEVEL
                    + "L");
        } else if (stack
            .isItemEqual(GT_Utility.getFluidDisplayStack(Materials.FerrousWastewater.getFluid(1000L), false))) {
                currentTip.add("Returned in amount equivalent to consumed flocculant.");
            }
        return super.handleNEIItemTooltip(stack, currentTip, neiCachedRecipe);
    }

    @Override
    public void drawNEIOverlays(GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        super.drawNEIOverlays(neiCachedRecipe);

        // Display flocculation chemical
        neiCachedRecipe.mInputs.add(
            new PositionedStack(
                GT_Utility.getFluidDisplayStack(Materials.PolyAluminiumChloride.getFluid(100000L), true),
                5,
                -1,
                false));

        // Display waste output
        neiCachedRecipe.mOutputs.add(
            new PositionedStack(
                GT_Utility.getFluidDisplayStack(Materials.FerrousWastewater.getFluid(100000L), true),
                147,
                48,
                false));
    }
}
