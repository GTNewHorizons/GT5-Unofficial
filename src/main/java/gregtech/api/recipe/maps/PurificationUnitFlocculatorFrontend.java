package gregtech.api.recipe.maps;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

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
        return ImmutableList.of(new Pos2d(9, 39), new Pos2d(5 + 4, -1 + 10));
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return ImmutableList.of(new Pos2d(151, 39), new Pos2d(147 + 4, 48 + 10));
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 115, 80, 3, 1);
    }

    @Override
    @NotNull
    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        if (stack.isItemEqual(GTUtility.getFluidDisplayStack(Materials.PolyAluminiumChloride.getFluid(1_000), false))) {
            currentTip.add(StatCollector.translateToLocal("GT5U.nei.purified_water.grade_3.0"));
            currentTip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.nei.purified_water.grade_3.1",
                    MTEPurificationUnitFlocculation.SUCCESS_PER_LEVEL,
                    MTEPurificationUnitFlocculation.INPUT_CHEMICAL_PER_LEVEL));
        } else if (stack
            .isItemEqual(GTUtility.getFluidDisplayStack(Materials.FlocculationWasteLiquid.getFluid(1_000), false))) {
                currentTip.add(StatCollector.translateToLocal("GT5U.nei.purified_water.grade_3.2"));
            }
        return super.handleNEIItemTooltip(stack, currentTip, neiCachedRecipe);
    }
}
