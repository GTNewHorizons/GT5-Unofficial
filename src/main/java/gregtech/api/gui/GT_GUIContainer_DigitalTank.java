package gregtech.api.gui;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.widgets.GT_GuiSlotTooltip;
import gregtech.api.interfaces.IDragAndDropSupport;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_SetLockedFluid;
import gregtech.api.util.GT_Utility;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalTankBase;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GT_GUIContainer_DigitalTank extends GT_GUIContainerMetaTile_Machine implements IDragAndDropSupport {

    private final String mName;
    private final int textColor = this.getTextColorOrDefault("text", 0xFAFAFF);
    private final int textColorTitle = this.getTextColorOrDefault("title", 0x404040);
    private final int textColorValue = this.getTextColorOrDefault("value", 0xFAFAFF);

    private static final String DIGITALTANK_AUTOOUTPUT_TOOLTIP = "GT5U.machines.digitaltank.autooutput.tooltip";
    private static final String DIGITALTANK_LOCKFLUID_TOOLTIP = "GT5U.machines.digitaltank.lockfluid.tooltip";
    private static final String DIGITALTANK_VOIDOVERFLOW_TOOLTIP = "GT5U.machines.digitaltank.voidoverflow.tooltip";
    private static final String DIGITALTANK_VOIDFULL_TOOLTIP = "GT5U.machines.digitaltank.voidfull.tooltip";
    private static final String DIGITALTANK_INPUTFROMOUTPUT_TOOLTIP =
            "GT5U.machines.digitaltank.inputfromoutput.tooltip";

    public GT_GUIContainer_DigitalTank(
            InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName) {
        super(new GT_Container_DigitalTank(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "DigitalTank.png");
        mName = aName;
    }

    @Override
    protected void setupTooltips() {
        addToolTip(new GT_GuiSlotTooltip(
                getContainer().slotAutoOutput, mTooltipCache.getData(DIGITALTANK_AUTOOUTPUT_TOOLTIP)));
        addToolTip(new GT_GuiSlotTooltip(
                getContainer().slotLockFLuid, mTooltipCache.getData(DIGITALTANK_LOCKFLUID_TOOLTIP)));
        addToolTip(new GT_GuiSlotTooltip(
                getContainer().slotVoidOverFlow, mTooltipCache.getData(DIGITALTANK_VOIDOVERFLOW_TOOLTIP)));
        addToolTip(new GT_GuiSlotTooltip(
                getContainer().slotVoidFull, mTooltipCache.getData(DIGITALTANK_VOIDFULL_TOOLTIP)));
        addToolTip(new GT_GuiSlotTooltip(
                getContainer().slotInputFromOutput, mTooltipCache.getData(DIGITALTANK_INPUTFROMOUTPUT_TOOLTIP)));
    }

    private GT_Container_DigitalTank getContainer() {
        return (GT_Container_DigitalTank) mContainer;
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
                drawTexturedModalRect(x + 151, y + 7, 176, 36, 18, 18);
            }
            if (((GT_Container_DigitalTank) mContainer).mVoidFluidFull) {
                drawTexturedModalRect(x + 151, y + 25, 176, 54, 18, 18);
            }
            if (((GT_Container_DigitalTank) mContainer).mAllowInputFromOutputSide) {
                drawTexturedModalRect(x + 43, y + 63, 176, 72, 18, 18);
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
