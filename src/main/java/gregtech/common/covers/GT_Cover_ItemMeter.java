package gregtech.common.covers;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiFakeItemButton;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.net.GT_Packet_TileEntityCover;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;

public class GT_Cover_ItemMeter
        extends GT_CoverBehavior {

    // left bit (normal/invert)
    // >> 1: 0=all items, else slot-1

    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        int[] tSlots;
        if ((aCoverVariable >> 1) > 0)
            tSlots = new int[]{(aCoverVariable >> 1) - 1};
        else
            tSlots = aTileEntity.getAccessibleSlotsFromSide(aSide);

        int tMax = 0;
        int tUsed = 0;
        for (int i : tSlots) {
            if (i >= 0 && i < aTileEntity.getSizeInventory()) {
                tMax+=64;
                ItemStack tStack = aTileEntity.getStackInSlot(i);
                if (tStack != null)
                    tUsed += (tStack.stackSize<<6)/tStack.getMaxStackSize();
            }
        }

        boolean inverted = (aCoverVariable & 0x1)>0;
        if(tUsed==0)//nothing
            aTileEntity.setOutputRedstoneSignal(aSide, (byte)(inverted ? 15 : 0));
        else if(tUsed >= tMax)//full
            aTileEntity.setOutputRedstoneSignal(aSide, (byte)(inverted ? 0 : 15));
        else//1-14 range
            aTileEntity.setOutputRedstoneSignal(aSide, (byte)(inverted ? 14-((14*tUsed)/tMax) : 1+((14*tUsed)/tMax)) );
        return aCoverVariable;
    }

    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            if ((0x1 & aCoverVariable)>0) {
                aCoverVariable = aCoverVariable & ~0x1;
                GT_Utility.sendChatToPlayer(aPlayer, trans("NORMAL","Normal"));
            }
            else {
                aCoverVariable = aCoverVariable | 0x1;
                GT_Utility.sendChatToPlayer(aPlayer, trans("INVERTED","Inverted"));
            }
            return aCoverVariable;
        }

        int val = aCoverVariable >> 1;
        int lsb = aCoverVariable & 0x1;

        if (val < 0)
            aCoverVariable = lsb;
        else
            aCoverVariable = ((val + 1) << 1) | lsb;

        if ((aCoverVariable >> 1) > aTileEntity.getSizeInventory())
            aCoverVariable = lsb;

        if ((aCoverVariable >> 1) == 0)
            GT_Utility.sendChatToPlayer(aPlayer, trans("053", "Slot: ") + trans("ALL", "All"));
        else
            GT_Utility.sendChatToPlayer(aPlayer, trans("053", "Slot: ") + ((aCoverVariable >> 1) - 1));

        //aCoverVariable = (aCoverVariable + (aPlayer.isSneaking()? -1 : 1)) % (aTileEntity.getSizeInventory() + 2);
        //switch(aCoverVariable) {
        //    case 0: GT_Utility.sendChatToPlayer(aPlayer, trans("051", "Normal")); break;
        //    case 1: GT_Utility.sendChatToPlayer(aPlayer, trans("052", "Inverted")); break;
        //    default: GT_Utility.sendChatToPlayer(aPlayer, trans("053", "Slot: ") + (aCoverVariable >> 1)); break;
        //}
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
        private final GT_GuiIconCheckButton button;
        private final GT_GuiIntegerTextBox intSlot;
        private final GT_GuiFakeItemButton intSlotIcon;
        private int coverVariable;

        private final static int startX = 10;
        private final static int startY = 25;
        private final static int spaceX = 18;
        private final static int spaceY = 18;

        private final int maxSlot;

        private final String ALL = trans("ALL", "All");
        private final String INVERTED = trans("INVERTED","Inverted");
        private final String NORMAL = trans("NORMAL","Normal");

        public GUI(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;

            button = new GT_GuiIconCheckButton(this, 0, startX + spaceX*0, startY+spaceY*0, GT_GuiIcon.REDSTONE_ON, GT_GuiIcon.REDSTONE_OFF);

            intSlot = new GT_GuiIntegerTextBox(this, 1, startX + spaceX * 0, startY + spaceY * 1 + 2, spaceX * 2+5, 12);
            intSlot.setMaxStringLength(6);

            //only shows if opened gui of block sadly, should've used container.
            intSlotIcon = new GT_GuiFakeItemButton(this, startX + spaceX * 8-4, startY + spaceY * 1, GT_GuiIcon.SLOT_GRAY);

            if (tile instanceof TileEntity && !super.tile.isDead())
                maxSlot = tile.getSizeInventory() - 1;
            else
                maxSlot = -1;

            if (maxSlot == -1)
                intSlot.setEnabled(false);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            if ((coverVariable & 0x1) > 0)
                this.getFontRenderer().drawString(INVERTED,  startX + spaceX*3, 4+startY+spaceY*0, 0xFF555555);
            else
                this.getFontRenderer().drawString(NORMAL,  startX + spaceX*3, 4+startY+spaceY*0, 0xFF555555);

            this.getFontRenderer().drawString(trans("254", "Internal slot#"),     startX + spaceX*3, 4+startY+spaceY*1, 0xFF555555);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            update();
            if (intSlot.isEnabled())
                intSlot.setFocused(true);
        }

        @Override
        public void buttonClicked(GuiButton btn) {
            if (isInverted())
                coverVariable = (coverVariable & ~0x1);
            else
                coverVariable = (coverVariable | 0x1);

            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
            update();
        }

        private void update() {
            resetTextBox(intSlot);
            button.setChecked(isInverted());

            int slot = getSlot();
            if (slot < 0) {
                intSlotIcon.setItem(null);
                return;
            }
            if (tile instanceof TileEntity && !super.tile.isDead()) {
                if (tile.getSizeInventory() >= slot) {
                    ItemStack item = tile.getStackInSlot(slot);
                    intSlotIcon.setItem(item);
                    return;
                }
            }
            intSlotIcon.setItem(null);
        }

        @Override
        public void onMouseWheel(int x, int y, int delta) {
            for (GT_GuiIntegerTextBox box : textBoxes) {
                if (box.isFocused()) {
                    int step = Math.max(1, Math.abs(delta / 120));
                    step = (isShiftKeyDown() ? 50 : isCtrlKeyDown() ? 5 : 1) * (delta > 0 ? step : -step);
                    int val = parseTextBox(box);

                    if (val < 0)
                        val = -1;

                    val = val + step;

                    if (val < 0)
                        val = -1;
                    else if (val > maxSlot )
                        val = maxSlot;

                    box.setText(val < 0 ? ALL : String.valueOf(val));
                    return;
                }
            }
        }

        @Override
        public void applyTextBox(GT_GuiIntegerTextBox box) {
            int val = parseTextBox(box);

            if (val < 0)
                coverVariable = coverVariable & 0x1;
            else
                coverVariable = ((val+1) << 1) | (coverVariable & 0x1);

            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
            update();
        }

        @Override
        public void resetTextBox(GT_GuiIntegerTextBox box) {
            box.setText(getSlot() < 0 ? ALL : String.valueOf(getSlot()));
        }

        private int parseTextBox(GT_GuiIntegerTextBox box) {
            String text = box.getText();
            if (text == null)
                return -1;
            text = text.trim();
            if (text.startsWith(ALL))
                text = text.substring(ALL.length());

            if (text.isEmpty())
                return -1;

            int val;
            try {
                val = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return -1;
            }

            if (val < 0)
                return -1;
            else if (maxSlot < val)
                return maxSlot;
            return val;
        }

        private boolean isInverted() {
            return ((coverVariable & 0x1) > 0);
        }

        private int getSlot() {
            if ((coverVariable & ~0x1) == 0)
                return -1;
            return (coverVariable >> 1) - 1;
        }
    }
}
