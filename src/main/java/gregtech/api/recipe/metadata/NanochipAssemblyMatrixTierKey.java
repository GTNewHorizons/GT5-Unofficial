package gregtech.api.recipe.metadata;

import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMetadataKey;
import gregtech.api.util.GTUtility;
import gregtech.nei.RecipeDisplayInfo;

public class NanochipAssemblyMatrixTierKey extends RecipeMetadataKey<Integer> {

    public static final NanochipAssemblyMatrixTierKey INSTANCE = new NanochipAssemblyMatrixTierKey();

    protected NanochipAssemblyMatrixTierKey() {
        super(Integer.class, "nanochip_assembly_matrix_tier");
    }

    @Override
    public void drawInfo(RecipeDisplayInfo recipeInfo, @Nullable Object value) {
        int tier = cast(value, 1);
        byte sanitizedTier = (byte) GTUtility.clamp(tier, 1, GTValues.TIER_COLORS.length);
        String tierString = GTUtility.getColoredTierNameFromTier((byte) (sanitizedTier - 1));
        recipeInfo.drawText("Casing Tier: " + tierString);
    }
}
