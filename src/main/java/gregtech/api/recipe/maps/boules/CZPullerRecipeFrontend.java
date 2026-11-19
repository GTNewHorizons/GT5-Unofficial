package gregtech.api.recipe.maps.boules;

import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.STRUCTURE_TIER;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Size;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
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
        int structureTier = recipeInfo.recipe.getMetadataOrDefault(STRUCTURE_TIER, 1);
        int temperature = recipeInfo.recipe.getMetadataOrDefault(COIL_HEAT, 0);
        String[] lines = { StatCollector.translateToLocalFormatted("GT5U.text.structure_tier", structureTier),
            StatCollector.translateToLocalFormatted("GT5U.text.minimum_temperature", temperature) };
        for (String line : lines) {
            for (String wrappedLine : Minecraft.getMinecraft().fontRenderer
                .listFormattedStringToWidth(EnumChatFormatting.BLACK + line, DESCRIPTION_WIDTH)) {
                recipeInfo.drawText(wrappedLine);
            }
        }
    }
}
