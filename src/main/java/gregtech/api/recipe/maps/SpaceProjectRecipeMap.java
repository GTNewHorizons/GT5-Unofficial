package gregtech.api.recipe.maps;

import static gregtech.api.util.GT_Utility.formatNumbers;
import static net.minecraft.util.EnumChatFormatting.GRAY;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.forge.IItemHandlerModifiable;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import appeng.util.ReadableNumberConverter;
import codechicken.lib.gui.GuiDraw;
import codechicken.nei.PositionedStack;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.util.GT_Recipe;
import gregtech.common.gui.modularui.UIHelper;
import gregtech.common.items.GT_FluidDisplayItem;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;
import gregtech.nei.GT_NEI_DefaultHandler;

public class SpaceProjectRecipeMap extends GT_Recipe.GT_Recipe_Map {

    IDrawable projectTexture;

    public SpaceProjectRecipeMap(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
        String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems,
        int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier,
        String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed) {
        super(
            aRecipeList,
            aUnlocalizedName,
            aLocalName,
            aNEIName,
            aNEIGUIPath,
            aUsualInputCount,
            aUsualOutputCount,
            aMinimalInputItems,
            aMinimalInputFluids,
            aAmperage,
            aNEISpecialValuePre,
            aNEISpecialValueMultiplier,
            aNEISpecialValuePost,
            aShowVoltageAmperageInNEI,
            aNEIAllowed);
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
        addRecipeSpecificDrawable(builder, windowOffset, () -> projectTexture, new Pos2d(124, 28), new Size(18, 18));
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
        GT_NEI_DefaultHandler.FixedPositionedStack pStack) {
        super.handleNEIItemOutputTooltip(currentTip, pStack);
        if (pStack.item != null && pStack.item.getItem() instanceof GT_FluidDisplayItem) return currentTip;
        currentTip.add(GRAY + translateToLocal("Item Count: ") + formatNumbers(pStack.realStackSize));
        return currentTip;
    }

    @Override
    public void drawNEIOverlays(GT_NEI_DefaultHandler.CachedDefaultRecipe neiCachedRecipe) {
        for (PositionedStack stack : neiCachedRecipe.mInputs) {
            if (stack instanceof GT_NEI_DefaultHandler.FixedPositionedStack && stack.item != null
                && !(stack.item.getItem() instanceof GT_FluidDisplayItem)) {
                int stackSize = ((GT_NEI_DefaultHandler.FixedPositionedStack) stack).realStackSize;
                String displayString;
                if (stack.item.stackSize > 9999) {
                    displayString = ReadableNumberConverter.INSTANCE.toWideReadableForm(stackSize);
                } else {
                    displayString = String.valueOf(stackSize);
                }
                drawNEIOverlayText(displayString, stack, 0xffffff, 0.5f, true, Alignment.BottomRight);
            }
        }
        if (neiCachedRecipe.mRecipe instanceof GT_FakeSpaceProjectRecipe) {
            ISpaceProject project = SpaceProjectManager
                .getProject(((GT_FakeSpaceProjectRecipe) neiCachedRecipe.mRecipe).projectName);
            if (project != null) {
                projectTexture = project.getTexture();
                GuiDraw.drawStringC(EnumChatFormatting.BOLD + project.getLocalizedName(), 85, 0, 0x404040, false);
            }
        }
    }

    @Override
    public void addProgressBarUI(ModularWindow.Builder builder, Supplier<Float> progressSupplier, Pos2d windowOffset) {
        int bar1Width = 17;
        int bar2Width = 18;
        builder.widget(
            new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_1, 17)
                .setDirection(ProgressBar.Direction.RIGHT)
                .setProgress(() -> progressSupplier.get() * ((float) (bar1Width + bar2Width) / bar1Width))
                .setSynced(false, false)
                .setPos(new Pos2d(70, 28).add(windowOffset))
                .setSize(bar1Width, 72));
        builder.widget(
            new ProgressBar().setTexture(GT_UITextures.PROGRESSBAR_ASSEMBLY_LINE_2, 18)
                .setDirection(ProgressBar.Direction.RIGHT)
                .setProgress(
                    () -> (progressSupplier.get() - ((float) bar1Width / (bar1Width + bar2Width)))
                        * ((float) (bar1Width + bar2Width) / bar2Width))
                .setSynced(false, false)
                .setPos(new Pos2d(106, 28).add(windowOffset))
                .setSize(bar2Width, 72));
    }
}
