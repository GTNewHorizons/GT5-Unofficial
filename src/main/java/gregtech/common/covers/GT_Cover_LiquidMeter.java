package gregtech.common.covers;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.net.GT_Packet_TileEntityCover;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class GT_Cover_LiquidMeter
        extends GT_CoverBehavior {
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if ((aTileEntity instanceof IFluidHandler)) {
            FluidTankInfo[] tTanks = ((IFluidHandler) aTileEntity).getTankInfo(ForgeDirection.UNKNOWN);
            long tMax = 0;
            long tUsed = 0;
            if (tTanks != null) {
                for (FluidTankInfo tTank : tTanks) {
                    if (tTank != null) {
                        tMax += tTank.capacity;
                        FluidStack tLiquid = tTank.fluid;
                        if (tLiquid != null) {
                            tUsed += tLiquid.amount;
                        }
                    }
                }
            }
            if(tUsed==0L)//nothing
                aTileEntity.setOutputRedstoneSignal(aSide, (byte)(aCoverVariable == 0 ? 15 : 0));
            else if(tUsed >= tMax)//full
                aTileEntity.setOutputRedstoneSignal(aSide, (byte)(aCoverVariable == 0 ? 0 : 15));
            else//1-14 range
                aTileEntity.setOutputRedstoneSignal(aSide, (byte)(aCoverVariable == 0 ? 14-((14*tUsed)/tMax) : 1+((14*tUsed)/tMax)) );
        } else {
            aTileEntity.setOutputRedstoneSignal(aSide, (byte)0);
        }
        return aCoverVariable;
    }

    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aCoverVariable == 0) {
            GT_Utility.sendChatToPlayer(aPlayer, trans("055", "Normal"));
        } else {
            GT_Utility.sendChatToPlayer(aPlayer, trans("054", "Inverted"));
        }
        return aCoverVariable == 0 ? 1 : 0;
    }

    public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    public boolean manipulatesSidedRedstoneOutput(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 5;
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
        return new GUI(aSide, aCoverID, coverData, aTileEntity);
    }

    private class GUI extends GT_GUICover {
        private final byte side;
        private final int coverID;
        private int coverVariable;

        private final static int startX = 10;
        private final static int startY = 25;
        private final static int spaceX = 18;
        private final static int spaceY = 18;

        public GUI(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;
            new GT_GuiIconCheckButton(this, 0, startX + spaceX*0, startY+spaceY*0, GT_GuiIcon.REDSTONE_ON, GT_GuiIcon.REDSTONE_OFF);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            String s2;
            if (coverVariable == 0)
                s2 = trans("INVERTED","Inverted");
            else
                s2 = trans("NORMAL","Normal");

            this.fontRendererObj.drawString(s2,  startX + spaceX*1, 4+startY+spaceY*0, 0xFF555555);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            updateButtons();
        }

        public void buttonClicked(GuiButton btn){
            boolean state = false;
            if (btn.id == 0)
                state = ((GT_GuiIconCheckButton) btn).isChecked();

            coverVariable = getNewCoverVariable(btn.id, state);
            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
            updateButtons();
        }

        private void updateButtons(){
            GuiButton b;
            for (Object o : buttonList) {
                b = (GuiButton) o;
                if(b.id == 0)
                    ((GT_GuiIconCheckButton) b).setChecked(coverVariable == 0);
            }
        }

        private int getNewCoverVariable(int id, boolean buttonState) {
            if (id == 0) {
                if (buttonState)
                    return coverVariable | 0x1;
                else
                    return coverVariable & ~0x1;
            }
            return coverVariable;
        }
    }
}