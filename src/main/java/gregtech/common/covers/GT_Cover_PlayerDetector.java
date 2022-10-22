package gregtech.common.covers;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_TileEntityCover;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fluids.Fluid;

public class GT_Cover_PlayerDetector extends GT_CoverBehavior {

    private String placer = "";
    private int range = 8;

    @Override
    public boolean isRedstoneSensitive(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        boolean playerDetected = false;

        if (aTileEntity instanceof IGregTechTileEntity) {
            if (aTileEntity.isUniversalEnergyStored(20)) {
                aTileEntity.decreaseStoredEnergyUnits(20, true);
                range = 32;
            } else {
                range = 8;
            }
            placer = ((IGregTechTileEntity) aTileEntity).getOwnerName();
        }
        for (Object tObject : aTileEntity.getWorld().playerEntities) {
            if ((tObject instanceof EntityPlayerMP)) {
                EntityPlayerMP tEntity = (EntityPlayerMP) tObject;
                int dist = Math.max(1, (int) tEntity.getDistance(aTileEntity.getXCoord() + 0.5D, aTileEntity.getYCoord() + 0.5D, aTileEntity.getZCoord() + 0.5D));
                if (dist < range) {
                    if (aCoverVariable == 0) {
                        playerDetected = true;
                        break;
                    }
                    if (tEntity.getDisplayName().equalsIgnoreCase(placer)) {
                        if (aCoverVariable == 1) {
                            playerDetected = true;
                            break;
                        }
                    } else if (aCoverVariable == 2) {
                        playerDetected = true;
                        break;
                    }
                }
            }
        }


        aTileEntity.setOutputRedstoneSignal(aSide, (byte) (playerDetected ? 15 : 0));
        return aCoverVariable;
    }

    @Override
    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking()? -1 : 1)) % 3;
        if(aCoverVariable <0){aCoverVariable = 2;}
        switch(aCoverVariable) {
            case 0:
				GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("068", "Emit if any Player is close")); break;
            case 1:
				GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("069", "Emit if other Player is close")); break;
            case 2:
				GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("070", "Emit if you are close")); break;
        }
        return aCoverVariable;
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
    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
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
    public boolean manipulatesSidedRedstoneOutput(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 20;
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

			new GT_GuiIconCheckButton(this, 0, startX + spaceX*0, startY+spaceY*0, GT_GuiIcon.CHECKMARK, null)
                    .setTooltipText(GT_Utility.trans("068", "Emit if any Player is close"));
			new GT_GuiIconCheckButton(this, 1, startX + spaceX*0, startY+spaceY*1, GT_GuiIcon.CHECKMARK, null)
                    .setTooltipText(GT_Utility.trans("069", "Emit if other Player is close"));
			new GT_GuiIconCheckButton(this, 2, startX + spaceX*0, startY+spaceY*2, GT_GuiIcon.CHECKMARK, null)
                    .setTooltipText(GT_Utility.trans("070", "Emit if you are close"));
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
			this.fontRendererObj.drawString(GT_Utility.trans("319", "Any player"),
                    startX + spaceX*1, 4+startY+spaceY*0, 0xFF555555);
			this.fontRendererObj.drawString(GT_Utility.trans("320", "Other players"),
                    startX + spaceX*1, 4+startY+spaceY*1, 0xFF555555);
			this.fontRendererObj.drawString(GT_Utility.trans("321", "Only owner"),
                    startX + spaceX*1, 4+startY+spaceY*2, 0xFF555555);
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
