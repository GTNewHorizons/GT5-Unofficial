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
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitFlocculation;
import gregtech.nei.GTNEIDefaultHandler;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PurificationUnitFlocculatorFrontend extends PurificationUnitRecipeMapFrontend {

    public PurificationUnitFlocculatorFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            80,
            uiPropertiesBuilder.logoPos(new Pos2d(160, 100))
                .progressBarTexture(new FallbackableUITexture(GTUITextures.PROGRESSBAR_FLOCCULATION))
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
        GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        if (stack.isItemEqual(GTUtility.getFluidDisplayStack(Materials.PolyAluminiumChloride.getFluid(1000L), false))) {
            currentTip.add("Consumed during operation");
            currentTip.add(
                "+" + MTEPurificationUnitFlocculation.SUCCESS_PER_LEVEL
                    + "%/"
                    + MTEPurificationUnitFlocculation.INPUT_CHEMICAL_PER_LEVEL
                    + "L");
        } else if (stack
            .isItemEqual(GTUtility.getFluidDisplayStack(Materials.FlocculationWasteLiquid.getFluid(1000L), false))) {
                currentTip.add("Returned in amount equivalent to consumed flocculant.");
            }
        return super.handleNEIItemTooltip(stack, currentTip, neiCachedRecipe);
    }

    @Override
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        super.drawNEIOverlays(neiCachedRecipe);

        // Display flocculation chemical
        neiCachedRecipe.mInputs.add(
            new PositionedStack(
                GTUtility.getFluidDisplayStack(Materials.PolyAluminiumChloride.getFluid(100000L), true),
                5,
                -1,
                false));

        // Display waste output
        neiCachedRecipe.mOutputs.add(
            new PositionedStack(
                GTUtility.getFluidDisplayStack(Materials.FlocculationWasteLiquid.getFluid(100000L), true),
                147,
                48,
                false));
    }
}
