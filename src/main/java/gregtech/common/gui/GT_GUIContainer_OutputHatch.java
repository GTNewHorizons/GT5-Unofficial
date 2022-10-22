package gregtech.common.gui;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.IDragAndDropSupport;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_SetLockedFluid;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

public class GT_GUIContainer_OutputHatch extends GT_GUIContainerMetaTile_Machine implements IDragAndDropSupport {

    private final String mName;
    private final int textColor = this.getTextColorOrDefault("text", 0xFAFAFF),
            textColorTitle = this.getTextColorOrDefault("title", 0x404040),
            textColorValue = this.getTextColorOrDefault("value", 0xFAFAFF);

    public GT_GUIContainer_OutputHatch(
            InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
        super(new GT_Container_OutputHatch(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "OutputHatch.png");
        mName = aName;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(
                StatCollector.translateToLocal("container.inventory"), 8, ySize - 96 + 2, textColorTitle);
        fontRendererObj.drawString(mName, 8, 6, textColorTitle);
        if (mContainer != null) {
            fontRendererObj.drawString("Liquid Amount", 10, 20, textColor);
            fontRendererObj.drawString(
                    GT_Utility.parseNumberToString(((GT_Container_OutputHatch) mContainer).mContent),
                    10,
                    30,
                    textColorValue);
            fontRendererObj.drawString("Locked Fluid", 101, 20, textColor);
            ItemStack tLockedDisplayStack =
                    (ItemStack) mContainer.getInventory().get(3);
            String fluidName = tLockedDisplayStack == null ? "None" : tLockedDisplayStack.getDisplayName();
            fontRendererObj.drawString(fluidName, 101, 30, textColorValue);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float parTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(parTicks, mouseX, mouseY);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
    }

    @Override
    public boolean handleDragAndDropGT(
            GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button, boolean isGhost) {
        if (isGhost
                && gui instanceof GT_GUIContainer_OutputHatch
                && ((GT_GUIContainer_OutputHatch) gui).isMouseOverSlot(3, mousex, mousey)) {
            // the instanceof check should be unnecessary, but we will do it regardless, just in case.
            FluidStack tFluidStack = GT_Utility.getFluidFromContainerOrFluidDisplay(draggedStack);
            if (tFluidStack != null) {
                GT_Values.NW.sendToServer(new GT_Packet_SetLockedFluid(
                        ((GT_GUIContainer_OutputHatch) gui).mContainer.mTileEntity, tFluidStack));
                draggedStack.stackSize = 0;
                return true;
            }
        }
        return false;
    }
}
