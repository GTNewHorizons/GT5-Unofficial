package gregtech.api.recipe.maps;

import java.util.List;

import net.minecraft.client.Minecraft;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.GTRecipeConstants;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.items.ItemScrapbox;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;

public class ScrapboxFrontend extends RecipeMapFrontend {

    public ScrapboxFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 44, 18, 1);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 114, 18, 1);
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    protected void drawMetadataInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    protected void drawRecipeOwnerInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder, Pos2d windowOffset) {
        builder.widget(
            new DrawableWidget().setDrawable(uiProperties.logo)
                .setSize(18, 18)
                .setPos(new Pos2d(151, 31).add(windowOffset)));
    }

    @Override
    public void addProgressBar(ModularWindow.Builder builder, GTNEIDefaultHandler.NEITemplateContext ctx) {
        assert uiProperties.progressBarTexture != null;
        builder.widget(
            new ProgressBar().setTexture(uiProperties.progressBarTexture.get(), 20)
                .setDirection(uiProperties.progressBarDirection)
                .setProgress(0)
                .setSynced(false, false)
                .setPos(
                    uiProperties.progressBarPos.add(ctx.windowOffset)
                        .subtract(0, 6))
                .setSize(uiProperties.progressBarSize));

    }

    @Override
    protected void drawSpecialInfo(RecipeDisplayInfo recipeInfo) {
        Float itemWeight = recipeInfo.recipe.getMetadataOrDefault(GTRecipeConstants.SCRAPBOX_DROP_WEIGHT, 0f);
        float chance = itemWeight / ItemScrapbox.weightTotal;
        String chanceString = String.format("%.2f", chance * 100) + "%";
        int width = Minecraft.getMinecraft().fontRenderer.getStringWidth(chanceString);
        if (width % 2 == 1) width -= 1;
        int xOffset = 18 - width / 2 - 1;
        Minecraft.getMinecraft().fontRenderer.drawString(chanceString, 68 + xOffset, 30, 0x707070);
    }
}
