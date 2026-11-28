package gregtech.api.recipe.maps.boules;

import static gregtech.api.recipe.RecipeMaps.WAFER_DOPING_PARTICLE_MODE;
import static gregtech.api.recipe.RecipeMaps.WAFER_DOPING_PARTICLE_MODE_ANOMALY;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.MAX_COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.STRUCTURE_TIER;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.api.math.Size;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTRecipe;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
public final class WaferDopingFacilityRecipeFrontend extends RecipeMapFrontend {

    private static final int DESCRIPTION_WIDTH = 160;

    public WaferDopingFacilityRecipeFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 82)));
    }

    @Override
    protected void drawMetadataInfo(RecipeDisplayInfo recipeInfo) {
        GTRecipe recipe = recipeInfo.recipe;
        int tier = recipe.getMetadataOrDefault(STRUCTURE_TIER, 1);
        int minimumTemperature = recipe.getMetadataOrDefault(COIL_HEAT, 0);
        int maximumTemperature = recipe.getMetadataOrDefault(MAX_COIL_HEAT, 0);
        int particleMode = recipe.getMetadataOrDefault(WAFER_DOPING_PARTICLE_MODE, 0);
        List<String> lines = new ArrayList<>();
        lines.add(StatCollector.translateToLocalFormatted("GT5U.text.structure_tier", tier));
        if (tier >= 2) {
            lines.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.wafer_doping.recipe.temperature",
                    minimumTemperature,
                    maximumTemperature));
        }
        if (tier >= 3) {
            lines.add(StatCollector.translateToLocal("GT5U.wafer_doping.recipe.signal_fluids"));
        }
        if (tier >= 4) {
            String particleKey = particleMode == WAFER_DOPING_PARTICLE_MODE_ANOMALY
                ? "GT5U.wafer_doping.recipe.particle_anomaly"
                : "GT5U.wafer_doping.recipe.particle_unknown";
            lines.add(StatCollector.translateToLocal(particleKey));
        }
        for (String line : lines) {
            for (String wrappedLine : Minecraft.getMinecraft().fontRenderer
                .listFormattedStringToWidth(EnumChatFormatting.BLACK + line, DESCRIPTION_WIDTH)) {
                recipeInfo.drawText(wrappedLine);
            }
        }
    }
}
