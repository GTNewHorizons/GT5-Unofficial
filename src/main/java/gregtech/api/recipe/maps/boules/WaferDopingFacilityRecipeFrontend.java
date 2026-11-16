package gregtech.api.recipe.maps.boules;

import static gregtech.api.recipe.RecipeMaps.WAFER_DOPING_FACILITY_METADATA;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.modularui.api.math.Size;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTRecipe;
import gregtech.loaders.postload.recipes.WaferDopingFacilityMetadata;
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
        WaferDopingFacilityMetadata metadata = recipe
            .getMetadataOrDefault(WAFER_DOPING_FACILITY_METADATA, WaferDopingFacilityMetadata.DEFAULT);
        int tier = metadata.structureTier;
        List<String> lines = new ArrayList<>();
        lines.add("Structure tier: " + tier);
        if (tier >= 2) {
            lines.add("Temperature: " + metadata.minimumTemperature + "-" + metadata.maximumTemperature + "K");
        }
        if (tier >= 3) {
            lines.add("Signal fluids: Gelid Cryotheum, Blazing Pyrotheum, Oxygen Plasma (10 mB each)");
        }
        if (tier >= 4) {
            lines.add(
                metadata.particleMode == WaferDopingFacilityMetadata.PARTICLE_MODE_ANOMALY
                    ? "Particle input: Quantum Anomaly (up to 16)"
                    : "Particle input: Unknown Particle (up to 16)");
        }
        for (String line : lines) {
            for (String wrappedLine : Minecraft.getMinecraft().fontRenderer
                .listFormattedStringToWidth(EnumChatFormatting.BLACK + line, DESCRIPTION_WIDTH)) {
                recipeInfo.drawText(wrappedLine);
            }
        }
    }
}
