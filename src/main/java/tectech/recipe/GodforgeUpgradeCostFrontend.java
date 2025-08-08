package tectech.recipe;

import static gregtech.api.util.GTRecipeConstants.FOG_UPGRADE_NAME_SHORT;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;

import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.nei.RecipeDisplayInfo;
import tectech.thing.gui.TecTechUITextures;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GodforgeUpgradeCostFrontend extends RecipeMapFrontend {

    public GodforgeUpgradeCostFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder, Pos2d windowOffset) {
        builder.widget(
            new DrawableWidget().setDrawable(TecTechUITextures.PICTURE_GODFORGE_LOGO)
                .setSize(18, 18)
                .setPos(new Pos2d(151, 63).add(windowOffset)));
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 8, 17, 4);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 116, 26, 1);
    }

    @Override
    public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
        return UIHelper.getGridPositions(fluidOutputCount, 134, 26, 1);
    }

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    protected void drawSpecialInfo(RecipeDisplayInfo recipeInfo) {
        String upgradeName = recipeInfo.recipe.getMetadataOrDefault(FOG_UPGRADE_NAME_SHORT, "");
        int width = Minecraft.getMinecraft().fontRenderer.getStringWidth(upgradeName);
        if (width % 2 == 1) width -= 1;
        int xOffset = 18 - width / 2 - 1;
        recipeInfo.drawText(" ", 83, -76);
        recipeInfo.drawText(
            EnumChatFormatting.BLUE.toString() + EnumChatFormatting.UNDERLINE + EnumChatFormatting.BOLD + upgradeName,
            110 + xOffset,
            0);
    }
}
