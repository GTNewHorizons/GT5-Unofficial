package gregtech.api.recipe.maps;

import static gregtech.api.util.GTRecipeConstants.FOUNDRY_MODULE;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;

import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipeProperties;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.tileentities.machines.multi.foundry.FoundryModule;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FoundryModuleFrontend extends RecipeMapFrontend {

    public FoundryModuleFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder, Pos2d windowOffset) {
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_EXOFOUNDRY_LOGO)
                .setSize(18, 18)
                .setPos(new Pos2d(151, 60).add(windowOffset)));
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 8, 17, 4);
    }

    @Override
    public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
        return UIHelper.getGridPositions(itemOutputCount, 124, 35, 1);
    }

    @Override
    protected void drawEnergyInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    protected void drawDurationInfo(RecipeDisplayInfo recipeInfo) {}

    @Override
    protected void drawSpecialInfo(RecipeDisplayInfo recipeInfo) {
        FoundryModule module = recipeInfo.recipe.getMetadataOrDefault(FOUNDRY_MODULE, null);
        if (module == null) return;
        int width = Minecraft.getMinecraft().fontRenderer.getStringWidth(module.shorthand);
        if (width % 2 == 1) width -= 1;
        int xOffset = 18 - width / 2 - 1;
        recipeInfo.drawText(" ", 83, -76);
        recipeInfo.drawText(
            module.color.toString() + EnumChatFormatting.UNDERLINE + EnumChatFormatting.BOLD + module.shorthand,
            110 + xOffset,
            0);
        super.drawSpecialInfo(recipeInfo);
    }

    @Override
    protected List<String> handleNEIItemInputTooltip(List<String> currentTip,
        GTNEIDefaultHandler.FixedPositionedStack pStack) {
        super.handleNEIItemInputTooltip(currentTip, pStack);
        if (ItemList.Magnetic_Chassis_T1_ExoFoundry.isStackEqual(pStack.item)
            || ItemList.Magnetic_Chassis_T2_ExoFoundry.isStackEqual(pStack.item)
            || ItemList.Magnetic_Chassis_T3_ExoFoundry.isStackEqual(pStack.item)) {
            currentTip.add(EnumChatFormatting.GOLD + "Chassis must match Foundry Tier!");
        }
        return currentTip;
    }

    @Override
    public NEIRecipeProperties getNEIProperties() {
        return super.getNEIProperties();
    }
}
