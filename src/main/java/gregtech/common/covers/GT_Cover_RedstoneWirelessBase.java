package gregtech.common.covers;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.IGuiScreen;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.net.GT_Packet_TileEntityCover;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

public abstract class GT_Cover_RedstoneWirelessBase
        extends GT_CoverBehavior {
    public boolean onCoverRemoval(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, boolean aForced) {
        GregTech_API.sWirelessRedstone.put(Integer.valueOf(aCoverVariable), Byte.valueOf((byte) 0));
        return true;
    }

    public boolean onCoverRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (((aX > 0.375D) && (aX < 0.625D)) || ((aSide > 3) && ((aY > 0.375D) && (aY < 0.625D)))) {
            GregTech_API.sWirelessRedstone.put(Integer.valueOf(aCoverVariable), Byte.valueOf((byte) 0));
            aCoverVariable = GT_Utility.stackToInt(aPlayer.inventory.getCurrentItem());
            aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
            GT_Utility.sendChatToPlayer(aPlayer, trans("081", "Frequency: ") + aCoverVariable);
            return true;
        }
        return false;
    }

    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (((aX > 0.375D) && (aX < 0.625D)) || ((aSide <= 3) || (((aY > 0.375D) && (aY < 0.625D)) || ((((aZ <= 0.375D) || (aZ >= 0.625D))))))) {
            GregTech_API.sWirelessRedstone.put(Integer.valueOf(aCoverVariable), Byte.valueOf((byte) 0));
            float[] tCoords = GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ);
            switch ((byte) ((byte) (int) (tCoords[0] * 2.0F) + 2 * (byte) (int) (tCoords[1] * 2.0F))) {
                case 0:
                    aCoverVariable -= 32;
                    break;
                case 1:
                    aCoverVariable += 32;
                    break;
                case 2:
                    aCoverVariable -= 1024;
                    break;
                case 3:
                    aCoverVariable += 1024;
            }
        }
        GT_Utility.sendChatToPlayer(aPlayer, trans("081", "Frequency: ") + aCoverVariable);
        return aCoverVariable;
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

    public String getDescription(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return trans("081", "Frequency: ") + aCoverVariable;
    }

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
        return new GUI(aSide, aCoverID, coverData, aTileEntity);
    }

    private class GUI extends GT_GUICover {
        private final byte side;
        private final int coverID;
        private GT_GuiIntegerTextBox fBox;
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

            fBox = new GT_GuiIntegerTextBoxWithMinus(this, 2,startX + spaceX*0,startY+spaceY*0 + 2, spaceX*4-3,12);
            fBox.setText(String.valueOf(coverVariable));
            fBox.setMaxStringLength(12);

        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.getFontRenderer().drawString(trans("246","Frequency" ),  startX + spaceX*4, 4+startY+spaceY*0, 0xFF555555);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            fBox.setFocused(true);
        }

        @Override
        public void onMouseWheel(int x, int y, int delta) {
            for (GT_GuiIntegerTextBox box : textBoxes){
                if (box.isFocused()) {
                    int step = Math.max(1, Math.abs(delta / 120));
                    step = (isShiftKeyDown() ? 1000 : isCtrlKeyDown() ? 50 : 1) * (delta > 0 ? step : -step);
                    long i;
                    try {
                        i = Long.parseLong(box.getText());
                    } catch (NumberFormatException e) {
                        return;
                    }
                    i = i + step;
                    if (i > Integer.MAX_VALUE)
                        i = Integer.MAX_VALUE;
                    else if (i < Integer.MIN_VALUE)
                        i = Integer.MIN_VALUE;

                    box.setText(String.valueOf(i));
                    return;
                }
            }
        }

        @Override
        public void applyTextBox(GT_GuiIntegerTextBox box) {
            long i;
            String s = box.getText().trim();
            try {
                i = Long.parseLong(s);
            } catch (NumberFormatException e) {
                resetTextBox(box);
                return;
            }

            if (i > Integer.MAX_VALUE)
                i = Integer.MAX_VALUE;
            else if (i < Integer.MIN_VALUE)
                i = Integer.MIN_VALUE;


            coverVariable = (int) i;
            fBox.setText(String.valueOf(coverVariable));
            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
        }

        @Override
        public void resetTextBox(GT_GuiIntegerTextBox box) {
            box.setText(String.valueOf(coverVariable));
        }

        private class GT_GuiIntegerTextBoxWithMinus extends GT_GuiIntegerTextBox {

            public GT_GuiIntegerTextBoxWithMinus(IGuiScreen gui, int id, int x, int y, int width, int height) {
                super(gui, id, x, y, width, height);
            }

            @Override
            public boolean validChar(char c, int key) {
                if (getCursorPosition() == 0 && key == 12) // minus first allowed.
                    return true;
                return super.validChar(c, key);
            }
        }
    }
}
