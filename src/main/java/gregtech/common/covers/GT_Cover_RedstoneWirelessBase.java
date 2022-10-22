package gregtech.common.covers;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.IGuiScreen;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.net.GT_Packet_WirelessRedstoneCover;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

public abstract class GT_Cover_RedstoneWirelessBase extends GT_CoverBehavior {

    private static final int MAX_CHANNEL = 65535;
    private static final int PRIVATE_MASK = 0xFFFE0000;
    private static final int PUBLIC_MASK = 0x0000FFFF;
    private static final int CHECKBOX_MASK = 0x00010000;

    @Override
    public boolean onCoverRemoval(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, boolean aForced) {
        GregTech_API.sWirelessRedstone.put(aCoverVariable, (byte) 0);
        return true;
    }

    @Override
    protected boolean onCoverRightClickImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (((aX > 0.375D) && (aX < 0.625D)) || ((aSide > 3) && ((aY > 0.375D) && (aY < 0.625D)))) {
            GregTech_API.sWirelessRedstone.put(aCoverVariable.get(), (byte) 0);
            aCoverVariable.set((aCoverVariable.get() & (PRIVATE_MASK | CHECKBOX_MASK)) | (((Integer)GT_Utility.stackToInt(aPlayer.inventory.getCurrentItem())).hashCode() & PUBLIC_MASK));
            GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("081", "Frequency: ") + aCoverVariable);
            return true;
        }
        return false;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCoverRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (((aX > 0.375D) && (aX < 0.625D)) || ((aSide > 3) && ((aY > 0.375D) && (aY < 0.625D)))) {
            GregTech_API.sWirelessRedstone.put(aCoverVariable, (byte) 0);

            int val = GT_Utility.stackToInt(aPlayer.inventory.getCurrentItem()) * (1 + aPlayer.inventory.getCurrentItem().getItemDamage());

            aCoverVariable = (aCoverVariable & (PRIVATE_MASK | CHECKBOX_MASK)) | (val & PUBLIC_MASK);

            aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
            GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("081", "Frequency: ") + (aCoverVariable & PUBLIC_MASK));
            return true;
        }
        return false;
    }

    @Override
    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (((aX > 0.375D) && (aX < 0.625D)) || ((aSide <= 3) || (((aY > 0.375D) && (aY < 0.625D)) || ((((aZ <= 0.375D) || (aZ >= 0.625D))))))) {
            GregTech_API.sWirelessRedstone.put(aCoverVariable, (byte) 0);
            float[] tCoords = GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ);

            short tAdjustVal = 0;

            switch ((byte) ((byte) (int) (tCoords[0] * 2.0F) + 2 * (byte) (int) (tCoords[1] * 2.0F))) {
                case 0:
                    tAdjustVal = -32;
                    break;
                case 1:
                    tAdjustVal = 32;
                    break;
                case 2:
                    tAdjustVal = -1024;
                    break;
                case 3:
                    tAdjustVal = 1024;
            }

            int tPublicChannel = (aCoverVariable & PUBLIC_MASK) + tAdjustVal;

            if (tPublicChannel < 0) {
                aCoverVariable = aCoverVariable & ~PUBLIC_MASK;
            }
            else if (tPublicChannel > MAX_CHANNEL) {
                aCoverVariable = (aCoverVariable & (PRIVATE_MASK | CHECKBOX_MASK)) | MAX_CHANNEL;
            }
            else {
                aCoverVariable = (aCoverVariable & (PRIVATE_MASK | CHECKBOX_MASK)) | tPublicChannel;
            }
        }
        GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("081", "Frequency: ") + (aCoverVariable & PUBLIC_MASK));
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
    public String getDescription(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return GT_Utility.trans("081", "Frequency: ") + aCoverVariable;
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
        return new GUI(aSide, aCoverID, coverData, aTileEntity);
    }

    private class GUI extends GT_GUICover {
        private final byte side;
        private final int coverID;
        private GT_GuiIntegerTextBox fBox;
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

            fBox = new GT_GuiShortTextBox(this, 2,startX + spaceX*0,startY+spaceY*0 + 2, spaceX*4-3,12);
            fBox.setText(String.valueOf(coverVariable & PUBLIC_MASK));
            fBox.setMaxStringLength(12);

            GuiButton b;
            b = new GT_GuiIconCheckButton(this, 0, startX + spaceX * 0, startY + spaceY * 2, GT_GuiIcon.CHECKMARK, null);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.getFontRenderer().drawString(GT_Utility.trans("246", "Frequency"),  startX + spaceX*4, 4+startY+spaceY*0, 0xFF555555);
            this.getFontRenderer().drawString(GT_Utility.trans("601", "Use Private Frequency"), startX + spaceX * 1, startY + spaceY * 2 + 4, 0xFF555555);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            fBox.setFocused(true);
            ((GT_GuiIconCheckButton) buttonList.get(0)).setChecked((coverVariable & CHECKBOX_MASK) > 0);
        }

        @Override
        public void onMouseWheel(int x, int y, int delta) {
            for (GT_GuiIntegerTextBox box : textBoxes){
                if (box.isFocused()) {
                    int step = Math.max(1, Math.abs(delta / 120));
                    step = (isShiftKeyDown() ? 1000 : isCtrlKeyDown() ? 50 : 1) * (delta > 0 ? step : -step);
                    long tCoverVariable;
                    try {
                        tCoverVariable = Long.parseLong(box.getText());
                    } catch (NumberFormatException e) {
                        return;
                    }
                    tCoverVariable = tCoverVariable + step;
                    if (tCoverVariable > MAX_CHANNEL)
                        tCoverVariable = MAX_CHANNEL;
                    else if (tCoverVariable < 0)
                        tCoverVariable = 0;

                    box.setText(String.valueOf(tCoverVariable));
                    return;
                }
            }
        }

        @Override
        public void applyTextBox(GT_GuiIntegerTextBox box) {
            int tPublicChannel;
            String s = box.getText().trim();
            try {
                tPublicChannel = Integer.parseInt(s);
            } catch (NumberFormatException e) {
                resetTextBox(box);
                return;
            }

            if (tPublicChannel > MAX_CHANNEL)
                tPublicChannel = MAX_CHANNEL;
            else if (tPublicChannel < 0)
                tPublicChannel = 0;

            int tCheckBoxValue = ((GT_GuiIconCheckButton)this.buttonList.get(0)).isChecked() ? CHECKBOX_MASK : 0;

            coverVariable = tCheckBoxValue | tPublicChannel;

            fBox.setText(Integer.toString(tPublicChannel));
            GT_Values.NW.sendToServer(new GT_Packet_WirelessRedstoneCover(side, coverID, tile, tPublicChannel, tCheckBoxValue));
        }

        @Override
        public void resetTextBox(GT_GuiIntegerTextBox box) {
            box.setText(String.valueOf(coverVariable & PUBLIC_MASK));
        }

        @Override
        public void buttonClicked(GuiButton btn) {

            final GT_GuiIconCheckButton tBtn = (GT_GuiIconCheckButton) btn;

            tBtn.setChecked(!tBtn.isChecked());

            int tPublicChannel = 0;
            String tText = fBox.getText().trim();

            if (tText.length() > 0) {
                tPublicChannel = Integer.parseInt(tText);
            }

            int tCheckBoxValue = tBtn.isChecked() ? CHECKBOX_MASK : 0;

            coverVariable = tCheckBoxValue | tPublicChannel;

            GT_Values.NW.sendToServer(new GT_Packet_WirelessRedstoneCover(side, coverID, tile, tPublicChannel, tCheckBoxValue));
        }

        private class GT_GuiShortTextBox extends GT_GuiIntegerTextBox {

            public GT_GuiShortTextBox(IGuiScreen gui, int id, int x, int y, int width, int height) {
                super(gui, id, x, y, width, height);
            }

            @Override
            public boolean textboxKeyTyped(char c, int key) {
                int tValue = 0;

                if(!super.textboxKeyTyped(c, key))
                    return false;

                int cursorPos = this.getCursorPosition();

                String tText = getText().trim();
                if (tText.length() > 0) {
                    try {
                        tValue = Integer.parseInt(tText);
                    } catch (NumberFormatException ignored) {}

                    if (tValue > MAX_CHANNEL)
                        setText(String.valueOf(MAX_CHANNEL));
                    else
                        setText(String.valueOf(tValue));

                    setCursorPosition(cursorPos);
                }

                return true;
            }
        }
    }
}
