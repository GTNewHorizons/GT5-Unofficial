package gregtech.api.gui;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import gregtech.api.enums.GT_Values;
import gregtech.api.interfaces.IDragAndDropSupport;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_SetLockedFluid;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalTankBase;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

public class GT_GUIContainer_DigitalTank extends GT_GUIContainerMetaTile_Machine implements IDragAndDropSupport {

    private final String mName;
    private final int textColor = this.getTextColorOrDefault("text", 0xFAFAFF),
            textColorTitle = this.getTextColorOrDefault("title", 0x404040),
            textColorValue = this.getTextColorOrDefault("value", 0xFAFAFF);

    public GT_GUIContainer_DigitalTank(
            InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
        super(new GT_Container_DigitalTank(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "DigitalTank.png");
        mName = aName;
    }

    @Override
    public void drawScreen(int par1, int par2, float par3) {
        super.drawScreen(par1, par2, par3);
        drawTooltip(par1, par2);
    }

    private void drawTooltip(int x2, int y2) {
        int xStart = (width - xSize) / 2;
        int yStart = (height - ySize) / 2;
        int x = x2 - xStart;
        int y = y2 - yStart + 5;
        List<String> list = new ArrayList<>();
        if (y >= 68 && y <= 84) {
            if (x >= 8 && x <= 24) {
                list.add(StatCollector.translateToLocal("GT5U.machines.digitaltank.autooutput.name"));
            } else if (x >= 26 && x <= 42) {
                list.add(StatCollector.translateToLocal("GT5U.machines.digitaltank.lockfluid.name"));
                list.add(StatCollector.translateToLocal("GT5U.machines.digitaltank.lockfluid.tooltip"));
                list.add(StatCollector.translateToLocal("GT5U.machines.digitaltank.lockfluid.tooltip1"));
            }
            if (x >= 44 && x <= 60) {
                list.add(StatCollector.translateToLocal("GT5U.machines.digitaltank.voidoverflow.name"));
                list.add(StatCollector.translateToLocal("GT5U.machines.digitaltank.voidoverflow.tooltip"));
            }
            if (x >= 62 && x <= 78) {
                list.add(StatCollector.translateToLocal("GT5U.machines.digitaltank.voidfull.name"));
                list.add(StatCollector.translateToLocal("GT5U.machines.digitaltank.voidfull.tooltip"));
            }
            if (x >= 80 && x <= 96) {
                list.add(StatCollector.translateToLocal("GT5U.machines.digitaltank.inputfromoutput.name"));
            }
        }
        if (!list.isEmpty()) drawHoveringText(list, x2, y2, fontRendererObj);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(mName, 8, 6, textColorTitle);
        if (mContainer != null) {
            fontRendererObj.drawString("Liquid Amount", 10, 20, textColor);
            fontRendererObj.drawString(
                    GT_Utility.parseNumberToString(((GT_Container_DigitalTank) mContainer).mContent),
                    10,
                    30,
                    textColorValue);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float parTicks, int mouseX, int mouseY) {
        super.drawGuiContainerBackgroundLayer(parTicks, mouseX, mouseY);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        if (mContainer != null) {
            if (((GT_Container_DigitalTank) mContainer).mLockFluid) {
                drawTexturedModalRect(x + 25, y + 63, 176, 0, 18, 18);
            }
            if (((GT_Container_DigitalTank) mContainer).outputFluid) {
                drawTexturedModalRect(x + 7, y + 63, 176, 18, 18, 18);
            }
            if (((GT_Container_DigitalTank) mContainer).mVoidFluidPart) {
                drawTexturedModalRect(x + 43, y + 63, 176, 36, 18, 18);
            }
            if (((GT_Container_DigitalTank) mContainer).mVoidFluidFull) {
                drawTexturedModalRect(x + 61, y + 63, 176, 54, 18, 18);
            }
            if (((GT_Container_DigitalTank) mContainer).mAllowInputFromOutputSide) {
                drawTexturedModalRect(x + 79, y + 63, 176, 72, 18, 18);
            }
        }
    }

    @Override
    public boolean handleDragAndDropGT(
            GuiContainer gui, int mousex, int mousey, ItemStack draggedStack, int button, boolean isGhost) {
        if (!(gui instanceof GT_GUIContainer_DigitalTank)
                || !((GT_GUIContainer_DigitalTank) gui).isMouseOverSlot(2, mousex, mousey)
                || !isGhost) return false;
        FluidStack fluidStack = GT_Utility.getFluidFromContainerOrFluidDisplay(draggedStack);
        if (fluidStack == null) return false;
        IGregTechTileEntity te = ((GT_GUIContainer_DigitalTank) gui).mContainer.mTileEntity;
        GT_MetaTileEntity_DigitalTankBase mte = (GT_MetaTileEntity_DigitalTankBase) te.getMetaTileEntity();
        if (mte == null || !mte.allowChangingLockedFluid(fluidStack.getFluid().getName())) return false;

        GT_Values.NW.sendToServer(new GT_Packet_SetLockedFluid(te, fluidStack));
        draggedStack.stackSize = 0;
        // propagate to client too
        mte.setLockedFluidName(fluidStack.getFluid().getName());
        return true;
    }
}
