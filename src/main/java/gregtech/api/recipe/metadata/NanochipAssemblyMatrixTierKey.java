package gregtech.api.recipe.metadata;

import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.VoltageIndex;
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
        byte sanitizedTier = (byte) GTUtility.clamp(tier, 1, VoltageIndex.UXV);
        String tierString = GTUtility.getColoredTierNameFromTier(sanitizedTier);
        recipeInfo.drawText("Casing Tier: " + tierString);
    }
}
