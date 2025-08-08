package gregtech.api.recipe.maps;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import com.gtnewhorizons.modularui.api.math.Pos2d;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class DistillationTowerFrontend extends RecipeMapFrontend {

    public DistillationTowerFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return Collections.singletonList(new Pos2d(106, 62));
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        List<Pos2d> results = new ArrayList<>();
        for (int i = 1; i < fluidOutputCount + 1; i++) {
            results.add(new Pos2d(106 + (i % 3) * 18, 62 - (i / 3) * 18));
        }
        return results;
    }
}
