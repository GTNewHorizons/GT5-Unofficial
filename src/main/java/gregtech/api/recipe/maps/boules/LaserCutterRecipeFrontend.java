package gregtech.api.recipe.maps.boules;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.modularui.api.math.Size;

import gregtech.api.enums.VoltageIndex;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
public final class LaserCutterRecipeFrontend extends RecipeMapFrontend {

    private static final int DESCRIPTION_WIDTH = 160;

    public LaserCutterRecipeFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder.recipeBackgroundSize(new Size(170, 82)));
    }

    @Override
    protected void drawMetadataInfo(RecipeDisplayInfo recipeInfo) {
        GTRecipe recipe = recipeInfo.recipe;
        int recipeTier = GTUtility.getTier(recipe.mEUt);
        String coolant = recipeTier <= VoltageIndex.LuV ? "Coolant"
            : recipeTier <= VoltageIndex.UV ? "Super Coolant" : "Liquid Helium";
        String line = EnumChatFormatting.BLACK + "Coolant: " + coolant;
        List<String> wrappedLines = Minecraft.getMinecraft().fontRenderer
            .listFormattedStringToWidth(line, DESCRIPTION_WIDTH);
        for (String wrappedLine : wrappedLines) {
            recipeInfo.drawText(wrappedLine);
        }
    }
}
