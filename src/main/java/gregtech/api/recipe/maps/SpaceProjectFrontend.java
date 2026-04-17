package gregtech.api.recipe.maps;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import codechicken.lib.gui.GuiDraw;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.BasicUIPropertiesBuilder;
import gregtech.api.recipe.NEIRecipePropertiesBuilder;
import gregtech.api.recipe.RecipeMapFrontend;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.SpaceProjectManager.FakeSpaceProjectRecipe;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;
import gregtech.nei.GTNEIDefaultHandler;
import gregtech.nei.GTNEIDefaultHandler.NEITemplateContext;
import gregtech.nei.RecipeDisplayInfo;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpaceProjectFrontend extends RecipeMapFrontend {

    IDrawable projectTexture;

    public SpaceProjectFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public ModularWindow.Builder createNEITemplate(NEITemplateContext ctx) {
        ModularWindow.Builder builder = super.createNEITemplate(ctx);
        builder.widget(
            new DrawableWidget().setDrawable(() -> projectTexture)
                .setSize(18, 18)
                .setPos(new Pos2d(124, 28).add(ctx.windowOffset)));
        return builder;
    }

    @Override
    public List<Pos2d> getItemInputPositions(int itemInputCount) {
        return UIHelper.getGridPositions(itemInputCount, 16, 28, 3);
    }

    @Override
    public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
        return UIHelper.getGridPositions(fluidInputCount, 88, 28, 1);
    }

    @Override
    protected List<String> handleNEIItemInputTooltip(List<String> currentTip,
        GTNEIDefaultHandler.FixedPositionedStack pStack) {
        super.handleNEIItemOutputTooltip(currentTip, pStack);
        if (pStack.isFluid()) return currentTip;
        currentTip.add(
            GRAY + translateToLocalFormatted(
                "GT5U.tooltip.space_project_frontend.item_count",
                formatNumber(pStack.realStackSize)));
        return currentTip;
    }

    @Override
    public void drawDescription(RecipeDisplayInfo recipeInfo) {
        super.drawDescription(recipeInfo);

        if (recipeInfo.recipe instanceof FakeSpaceProjectRecipe recipe) {
            ISpaceProject project = SpaceProjectManager.getProject(recipe.projectName);
            if (project != null) {
                projectTexture = project.getTexture();
                GuiDraw.drawStringC(EnumChatFormatting.BOLD + project.getLocalizedName(), 85, 0, 0x404040, false);
            }
        }
    }

    @Override
    public void addProgressBar(ModularWindow.Builder builder, NEITemplateContext ctx) {
        int bar1Width = 17;
        int bar2Width = 18;
        List<Supplier<Float>> splitProgress = splitProgress(ctx.progressSupplier, bar1Width, bar2Width);
        builder.widget(
            new ProgressBar().setTexture(GTUITextures.PROGRESSBAR_ASSEMBLY_LINE_1, 17)
                .setDirection(ProgressBar.Direction.RIGHT)
                .setProgress(splitProgress.get(0))
                .setSynced(false, false)
                .setPos(new Pos2d(70, 28).add(ctx.windowOffset))
                .setSize(bar1Width, 72));
        builder.widget(
            new ProgressBar().setTexture(GTUITextures.PROGRESSBAR_ASSEMBLY_LINE_2, 18)
                .setDirection(ProgressBar.Direction.RIGHT)
                .setProgress(splitProgress.get(1))
                .setSynced(false, false)
                .setPos(new Pos2d(106, 28).add(ctx.windowOffset))
                .setSize(bar2Width, 72));
    }
}
