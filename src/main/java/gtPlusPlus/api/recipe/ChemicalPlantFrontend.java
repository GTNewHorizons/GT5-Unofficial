package gtPlusPlus.api.recipe;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import codechicken.nei.PositionedStack;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.GTNEIDefaultHandler;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.MTEChemicalPlant;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ChemicalPlantFrontend extends RecipeMapFrontend {

    public ChemicalPlantFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 7, 6, itemInputCount, 1);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 106, 6, 2);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 7, 41, fluidInputCount, 1);
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getGridPositions(fluidOutputCount, 142, 6, 1, fluidOutputCount);
    }

    @Override
    public void prepareRecipe(GTNEIDefaultHandler.CachedDefaultRecipe recipe) {
        for (PositionedStack pStack : recipe.mInputs) {
            if (pStack instanceof GTNEIDefaultHandler.FixedPositionedStack fixed
                && MTEChemicalPlant.isCatalyst(pStack.item)) {
                fixed.setCustomBadge(
                    "NC*",
                    StatCollector.translateToLocal("gtpp.nei.chemical_plant.not_consumed"),
                    StatCollector.translateToLocal("gtpp.nei.chemical_plant.higher_tier_casing"));
            }
        }
    }

}
