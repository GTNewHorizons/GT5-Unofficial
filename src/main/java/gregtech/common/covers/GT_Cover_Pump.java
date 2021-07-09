package gregtech.common.covers;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.net.GT_Packet_TileEntityCover;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

public class GT_Cover_Pump extends GT_CoverBehavior{
    public final int mTransferRate;

    public GT_Cover_Pump(int aTransferRate) {
        this.mTransferRate = aTransferRate;
    }

    @Override
    public boolean isRedstoneSensitive(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if ((aCoverVariable % 6 > 1) && ((aTileEntity instanceof IMachineProgress))) {
            if (((IMachineProgress) aTileEntity).isAllowedToWork() != aCoverVariable % 6 < 4) {
                return aCoverVariable;
            }
        }
        if ((aTileEntity instanceof IFluidHandler)) {
            IFluidHandler tTank2 = aTileEntity.getITankContainerAtSide(aSide);
            if (tTank2 != null) {
                //aTileEntity.decreaseStoredEnergyUnits(GT_Utility.getTier(this.mTransferRate), true);
                IFluidHandler tTank1 = (IFluidHandler) aTileEntity;
                if (aCoverVariable % 2 == 0) {
                    FluidStack tLiquid = tTank1.drain(ForgeDirection.getOrientation(aSide), this.mTransferRate, false);
                    if (tLiquid != null) {
                        tLiquid = tLiquid.copy();
                        tLiquid.amount = tTank2.fill(ForgeDirection.getOrientation(aSide).getOpposite(), tLiquid, false);
                        if (tLiquid.amount > 0 && canTransferFluid(tLiquid)) {
                            tTank2.fill(ForgeDirection.getOrientation(aSide).getOpposite(), tTank1.drain(ForgeDirection.getOrientation(aSide), tLiquid.amount, true), true);
                        }
                    }
                } else {
                    FluidStack tLiquid = tTank2.drain(ForgeDirection.getOrientation(aSide).getOpposite(), this.mTransferRate, false);
                    if (tLiquid != null) {
                        tLiquid = tLiquid.copy();
                        tLiquid.amount = tTank1.fill(ForgeDirection.getOrientation(aSide), tLiquid, false);
                        if (tLiquid.amount > 0 && canTransferFluid(tLiquid)) {
                            tTank1.fill(ForgeDirection.getOrientation(aSide), tTank2.drain(ForgeDirection.getOrientation(aSide).getOpposite(), tLiquid.amount, true), true);
                        }
                    }
                }
            }
        }
        return aCoverVariable;
    }

    protected boolean canTransferFluid(FluidStack fluid) {
        return true;
    }

    @Override
    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking()? -1 : 1)) % 12;
        if(aCoverVariable <0){aCoverVariable = 11;}
        switch(aCoverVariable) {
        case 0: GT_Utility.sendChatToPlayer(aPlayer, trans("006", "Export")); break;
        case 1: GT_Utility.sendChatToPlayer(aPlayer, trans("007", "Import")); break;
        case 2: GT_Utility.sendChatToPlayer(aPlayer, trans("008", "Export (conditional)")); break;
        case 3: GT_Utility.sendChatToPlayer(aPlayer, trans("009", "Import (conditional)")); break;
        case 4: GT_Utility.sendChatToPlayer(aPlayer, trans("010", "Export (invert cond)")); break;
        case 5: GT_Utility.sendChatToPlayer(aPlayer, trans("011", "Import (invert cond)")); break;
        case 6: GT_Utility.sendChatToPlayer(aPlayer, trans("012", "Export allow Input")); break;
        case 7: GT_Utility.sendChatToPlayer(aPlayer, trans("013", "Import allow Output")); break;
        case 8: GT_Utility.sendChatToPlayer(aPlayer, trans("014", "Export allow Input (conditional)")); break;
        case 9: GT_Utility.sendChatToPlayer(aPlayer, trans("015", "Import allow Output (conditional)")); break;
        case 10: GT_Utility.sendChatToPlayer(aPlayer, trans("016", "Export allow Input (invert cond)")); break;
        case 11: GT_Utility.sendChatToPlayer(aPlayer, trans("017", "Import allow Output (invert cond)")); break;
        }
        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        if ((aCoverVariable > 1) && ((aTileEntity instanceof IMachineProgress))) {
            if (((IMachineProgress) aTileEntity).isAllowedToWork() != aCoverVariable % 6 < 4) {
                return false;
            }
        }
        return (aCoverVariable >= 6) || (aCoverVariable % 2 != 0);
    }

    @Override
    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        if ((aCoverVariable > 1) && ((aTileEntity instanceof IMachineProgress))) {
            if (((IMachineProgress) aTileEntity).isAllowedToWork() != aCoverVariable % 6 < 4) {
                return false;
            }
        }
        return (aCoverVariable >= 6) || (aCoverVariable % 2 == 0);
    }

    @Override
    public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }

    /**
     * GUI Stuff
     */

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public Object getClientGUI(byte aSide, int aCoverID, int coverData, ICoverable aTileEntity)  {
        return new GT_PumpGUICover(aSide, aCoverID, coverData, aTileEntity);
    }

    private class GT_PumpGUICover extends GT_GUICover {
        private final byte side;
        private final int coverID;
        private int coverVariable;

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public GT_PumpGUICover(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;

            GT_GuiIconButton b;
            b = new GT_GuiIconButton(this, 0, startX + spaceX*0, startY+spaceY*0, GT_GuiIcon.EXPORT).setTooltipText(trans("006","Export"));
            b = new GT_GuiIconButton(this, 1, startX + spaceX*1, startY+spaceY*0, GT_GuiIcon.IMPORT).setTooltipText(trans("007","Import"));
            b = new GT_GuiIconButton(this, 2, startX + spaceX*0, startY+spaceY*1, GT_GuiIcon.CHECKMARK).setTooltipText(trans("224","Ignore"));
            b = new GT_GuiIconButton(this, 3, startX + spaceX*1, startY+spaceY*1, GT_GuiIcon.REDSTONE_ON).setTooltipText(trans("225","Conditional"));
            b = new GT_GuiIconButton(this, 4, startX + spaceX*2, startY+spaceY*1, GT_GuiIcon.REDSTONE_OFF).setTooltipText(trans("226","Invert Condition"));
            b = new GT_GuiIconButton(this, 5, startX + spaceX*0, startY+spaceY*2, GT_GuiIcon.ALLOW_INPUT).setTooltipText(trans("227","Allow Input"));
            b = new GT_GuiIconButton(this, 6, startX + spaceX*1, startY+spaceY*2, GT_GuiIcon.BLOCK_INPUT).setTooltipText(trans("228","Block Input"));
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.fontRendererObj.drawString(trans("229","Import/Export" ),  startX + spaceX*3, 3+startY+spaceY*0, 0xFF555555);
            this.fontRendererObj.drawString(trans("230","Conditional"),     startX + spaceX*3, 3+startY+spaceY*1, 0xFF555555);
            this.fontRendererObj.drawString(trans("231", "Enable Input"),   startX + spaceX*3, 3+startY+spaceY*2, 0xFF555555);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            updateButtons();
        }

        @Override
        public void buttonClicked(GuiButton btn){
            if (getClickable(btn.id)){
                coverVariable = getNewCoverVariable(btn.id);
                GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
            }
            updateButtons();
        }

        private void updateButtons(){
            GuiButton b;
            for (Object o : buttonList) {
                b = (GuiButton) o;
                b.enabled = getClickable(b.id);
            }
        }

        private int getNewCoverVariable(int id) {
            switch (id) {
                case 0:
                    return coverVariable & ~0x1;
                case 1:
                    return coverVariable | 0x1;
                case 2:
                    if (coverVariable > 5)
                        return 0x6 | (coverVariable & ~0xE);
                    return (coverVariable & ~0xE);
                case 3:
                    if (coverVariable > 5)
                        return 0x8 | (coverVariable & ~0xE);
                    return 0x2 | (coverVariable & ~0xE);
                case 4:
                    if (coverVariable > 5)
                        return 0xA | (coverVariable & ~0xE);
                    return (0x4 | (coverVariable & ~0xE));
                case 5:
                    if (coverVariable <= 5)
                        return coverVariable + 6;
                    break;
                case 6:
                    if (coverVariable > 5)
                        return coverVariable - 6;
            }
            return coverVariable;
        }

        private boolean getClickable(int id) {
            if (coverVariable < 0 | 11 < coverVariable)
                return false;

            switch (id) {
                case 0: case 1:
                    return (0x1 & coverVariable) != id;
                case 2:
                    return (coverVariable % 6) >= 2;
                case 3:
                    return (coverVariable % 6) < 2 | 4 <= (coverVariable % 6);
                case 4:
                    return (coverVariable % 6) < 4;
                case 5:
                    return coverVariable < 6;
                case 6:
                    return coverVariable >= 6;
            }
            return false;
        }
    }
}
