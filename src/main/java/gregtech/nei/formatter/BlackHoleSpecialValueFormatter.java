package gregtech.nei.formatter;

import gregtech.api.util.GT_Utility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;
import net.minecraft.util.StatCollector;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlackHoleSpecialValueFormatter implements INEISpecialInfoFormatter{

    public static final BlackHoleSpecialValueFormatter INSTANCE = new BlackHoleSpecialValueFormatter();

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        int blackholeTier = recipeInfo.recipe.mSpecialValue;
        String text;

        switch (blackholeTier) {
            case 1 -> text = "Requires Stabilized Black Hole";
            default -> text = "";
        }

        return Collections.singletonList(text);
            //StatCollector.translateToLocalFormatted("GT5U.nei.start_eu", GT_Utility.formatNumbers(), ));
    }
}
