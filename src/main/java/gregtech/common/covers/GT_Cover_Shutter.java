package gregtech.common.covers;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.net.GT_Packet_TileEntityCover;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

public class GT_Cover_Shutter extends GT_CoverBehavior {
    @Override
    public boolean isRedstoneSensitive(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return aCoverVariable;
    }

    @Override
    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking()? -1 : 1)) % 4;
        if(aCoverVariable <0){aCoverVariable = 3;}
        switch(aCoverVariable) {
            case 0: GT_Utility.sendChatToPlayer(aPlayer, trans("082", "Open if work enabled")); break;
            case 1: GT_Utility.sendChatToPlayer(aPlayer, trans("083", "Open if work disabled")); break;
            case 2: GT_Utility.sendChatToPlayer(aPlayer, trans("084", "Only Output allowed")); break;
            case 3: GT_Utility.sendChatToPlayer(aPlayer, trans("085", "Only Input allowed")); break;
        }
        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 3 : !(aTileEntity instanceof IMachineProgress) || (((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0));
    }

    @Override
    public boolean letsRedstoneGoOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 2 : !(aTileEntity instanceof IMachineProgress) || (((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0));
    }

    @Override
    public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 3 : !(aTileEntity instanceof IMachineProgress) || (((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0));
    }

    @Override
    public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 2 : !(aTileEntity instanceof IMachineProgress) || ((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0);
    }

    @Override
    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 3 : !(aTileEntity instanceof IMachineProgress) || ((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0);
    }

    @Override
    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 2 : !(aTileEntity instanceof IMachineProgress) || ((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0);
    }

    @Override
    public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 3 : !(aTileEntity instanceof IMachineProgress) || ((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0);
    }

    @Override
    public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 2 : !(aTileEntity instanceof IMachineProgress) || ((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0);
    }

    @Override
    public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 0;
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

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public GUI(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;

            new GT_GuiIconCheckButton(this, 0, startX + spaceX*0, startY+spaceY*0, GT_GuiIcon.CHECKMARK, null);
            new GT_GuiIconCheckButton(this, 1, startX + spaceX*0, startY+spaceY*1, GT_GuiIcon.CHECKMARK, null);
            new GT_GuiIconCheckButton(this, 2, startX + spaceX*0, startY+spaceY*2, GT_GuiIcon.CHECKMARK, null);
            new GT_GuiIconCheckButton(this, 3, startX + spaceX*0, startY+spaceY*3, GT_GuiIcon.CHECKMARK, null);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.fontRendererObj.drawString(trans("082", "Open if work enabled"),
                    3+startX + spaceX*1, 4+startY+spaceY*0, 0xFF555555);
            this.fontRendererObj.drawString(trans("083", "Open if work disabled"),
                    3+startX + spaceX*1, 4+startY+spaceY*1, 0xFF555555);
            this.fontRendererObj.drawString(trans("084", "Only Output allowed"),
                    3+startX + spaceX*1, 4+startY+spaceY*2, 0xFF555555);
            this.fontRendererObj.drawString(trans("085", "Only Input allowed"),
                    3+startX + spaceX*1, 4+startY+spaceY*3, 0xFF555555);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            updateButtons();
        }

        @Override
        public void buttonClicked(GuiButton btn){
            if (!isEnabled(btn.id)){
                coverVariable = getNewCoverVariable(btn.id, ((GT_GuiIconCheckButton) btn).isChecked());
                GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
            }
            updateButtons();
        }

        private void updateButtons(){
            for (Object o : buttonList)
                ((GT_GuiIconCheckButton) o).setChecked(isEnabled(((GT_GuiIconCheckButton) o).id));
        }

        private int getNewCoverVariable(int id, boolean checked) {
            return id;
        }

        private boolean isEnabled(int id) {
            return coverVariable == id;
        }
    }
}
