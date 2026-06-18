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
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitPhAdjustment;
import gregtech.nei.GTNEIDefaultHandler;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class PurificationUnitPhAdjustmentFrontend extends PurificationUnitRecipeMapFrontend {

    public PurificationUnitPhAdjustmentFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(
            80,
            uiPropertiesBuilder.logoPos(new Pos2d(160, 100))
                .progressBarTexture(new FallbackableUITexture(GTUITextures.PROGRESSBAR_PH_NEUTRALIZATION))
                .logoPos(new Pos2d(152, 90)),
            neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 120)));
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return ImmutableList.of(new Pos2d(3 + 4, 1 + 10));
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return ImmutableList.of(new Pos2d(42, 44), new Pos2d(147 + 4, 1 + 10));
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return ImmutableList.of(new Pos2d(116, 44));
    }

    @Override
    @NotNull
    public List<String> handleNEIItemTooltip(ItemStack stack, List<String> currentTip,
        GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        // Add pH adjustment values
        if (stack.isItemEqual(Materials.SodiumHydroxide.getDust(1))) {
            currentTip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.nei.purified_water.grade_4.0",
                    MTEPurificationUnitPhAdjustment.PH_PER_ALKALINE_DUST * 64));
        } else
            if (stack.isItemEqual(GTUtility.getFluidDisplayStack(Materials.HydrochloricAcid.getFluid(1_000), false))) {
                currentTip.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.nei.purified_water.grade_4.1",
                        MTEPurificationUnitPhAdjustment.PH_PER_10_ACID_LITER * 100));
            }
        return super.handleNEIItemTooltip(stack, currentTip, neiCachedRecipe);
    }
}
