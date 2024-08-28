package gregtech.nei.formatter;

import java.util.Collections;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CompressorSpecialValueFormatter implements INEISpecialInfoFormatter {

    public static final CompressorSpecialValueFormatter INSTANCE = new CompressorSpecialValueFormatter();

    @Override
    public List<String> format(RecipeDisplayInfo recipeInfo) {
        int compressionTier = recipeInfo.recipe.mSpecialValue;
        String text;

        switch (compressionTier) {
            case 1 -> text = "Requires Stabilized Black Hole";
            case 2 -> text = "Requires HIP Compression";
            default -> text = "";
        }

        return Collections.singletonList(text);
    }
}
