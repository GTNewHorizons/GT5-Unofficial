package gregtech.api.recipe.maps.boules;

import static gregtech.api.recipe.RecipeMaps.CZ_PULLER_METADATA;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.modularui.api.math.Size;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.loaders.postload.recipes.CZPullerMetadata;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
public final class CZPullerRecipeFrontend extends RecipeMapFrontend {

    private static final int DESCRIPTION_WIDTH = 160;

    public CZPullerRecipeFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 82)));
    }

    @Override
    protected void drawMetadataInfo(RecipeDisplayInfo recipeInfo) {
        CZPullerMetadata metadata = recipeInfo.recipe
            .getMetadataOrDefault(CZ_PULLER_METADATA, CZPullerMetadata.DEFAULT);
        String[] lines = { "Structure tier: " + metadata.structureTier,
            "Min. Temperature: " + metadata.temperature + "K" };
        for (String line : lines) {
            for (String wrappedLine : Minecraft.getMinecraft().fontRenderer
                .listFormattedStringToWidth(EnumChatFormatting.BLACK + line, DESCRIPTION_WIDTH)) {
                recipeInfo.drawText(wrappedLine);
            }
        }
    }
}
