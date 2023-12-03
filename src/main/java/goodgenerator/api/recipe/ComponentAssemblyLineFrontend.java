package goodgenerator.api.recipe;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ComponentAssemblyLineFrontend extends RecipeMapFrontend {

    public ComponentAssemblyLineFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
            NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 16, 8, 3);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return Collections.singletonList(new Pos2d(142, 8));
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {

        return UIHelper.getGridPositions(fluidInputCount, 88, 26, 4);
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder, Pos2d windowOffset) {}
}
