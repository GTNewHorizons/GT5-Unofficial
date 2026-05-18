package gregtech.nei.formatter;

import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;
import net.minecraft.util.StatCollector;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CuttingRecipesFormatter implements INEISpecialInfoFormatter {

    public static final CuttingRecipesFormatter INSTANCE = new CuttingRecipesFormatter();

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        return Collections
            .singletonList(StatCollector.translateToLocal("GT5U.nei.cutter_fluids_warning"));
    }
}
