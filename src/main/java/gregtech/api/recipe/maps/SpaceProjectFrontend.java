package gregtech.api.recipe.maps;

import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;
import java.util.function.Supplier;

import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
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

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class SpaceProjectFrontend extends RecipeMapFrontend {

    IDrawable projectTexture;

    public SpaceProjectFrontend(BasicUIPropertiesBuilder uiPropertiesBuilder,
        NEIRecipePropertiesBuilder neiPropertiesBuilder) {
        super(uiPropertiesBuilder, neiPropertiesBuilder);
    }

    @Override
    public ModularWindow.Builder createNEITemplate(IItemHandlerModifiable itemInputsInventory,
        IItemHandlerModifiable itemOutputsInventory, IItemHandlerModifiable specialSlotInventory,
        IItemHandlerModifiable fluidInputsInventory, IItemHandlerModifiable fluidOutputsInventory,
        Supplier<Float> progressSupplier, Pos2d windowOffset) {
        ModularWindow.Builder builder = super.createNEITemplate(
            itemInputsInventory,
            itemOutputsInventory,
            specialSlotInventory,
            fluidInputsInventory,
            fluidOutputsInventory,
            progressSupplier,
            windowOffset);
        builder.widget(
            new DrawableWidget().setDrawable(() -> projectTexture)
                .setSize(18, 18)
                .setPos(new Pos2d(124, 28).add(windowOffset)));
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
                formatNumbers(pStack.realStackSize)));
        return currentTip;
    }

    @Override
    public void drawNEIOverlays(GTNEIDefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        super.drawNEIOverlays(neiCachedRecipe);
        if (neiCachedRecipe.mRecipe instanceof FakeSpaceProjectRecipe) {
            ISpaceProject project = SpaceProjectManager
                .getProject(((FakeSpaceProjectRecipe) neiCachedRecipe.mRecipe).projectName);
            if (project != null) {
                projectTexture = project.getTexture();
                GuiDraw.drawStringC(EnumChatFormatting.BOLD + project.getLocalizedName(), 85, 0, 0x404040, false);
            }
        }
    }

    @Override
    public void addProgressBar(ModularWindow.Builder builder, Supplier<Float> progressSupplier, Pos2d windowOffset) {
        int bar1Width = 17;
        int bar2Width = 18;
        List<Supplier<Float>> splitProgress = splitProgress(progressSupplier, bar1Width, bar2Width);
        builder.widget(
            new ProgressBar().setTexture(GTUITextures.PROGRESSBAR_ASSEMBLY_LINE_1, 17)
                .setDirection(ProgressBar.Direction.RIGHT)
                .setProgress(splitProgress.get(0))
                .setSynced(false, false)
                .setPos(new Pos2d(70, 28).add(windowOffset))
                .setSize(bar1Width, 72));
        builder.widget(
            new ProgressBar().setTexture(GTUITextures.PROGRESSBAR_ASSEMBLY_LINE_2, 18)
                .setDirection(ProgressBar.Direction.RIGHT)
                .setProgress(splitProgress.get(1))
                .setSynced(false, false)
                .setPos(new Pos2d(106, 28).add(windowOffset))
                .setSize(bar2Width, 72));
    }
}
