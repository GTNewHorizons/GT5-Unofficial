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
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalChestBase;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;

public class GT_Cover_ItemMeter extends GT_CoverBehavior {

    // format:
    private static final int SLOT_MASK = 0x3FFFFFF; // 0 = all, 1 = 0 ...
    private static final int CONVERTED_BIT = 0x80000000;
    private static final int INVERT_BIT = 0x40000000;

    @Override
    public boolean isRedstoneSensitive(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        //Convert from ver. 5.09.33.50
        if ((CONVERTED_BIT & aCoverVariable) == 0)
            if (aCoverVariable == 0)
                aCoverVariable = CONVERTED_BIT;
            else if (aCoverVariable == 1)
                aCoverVariable = CONVERTED_BIT | INVERT_BIT;
            else if (aCoverVariable > 1)
                aCoverVariable = CONVERTED_BIT | Math.min((aCoverVariable - 2), SLOT_MASK);

        long tMax = 0;
        long tUsed = 0;
        if (aTileEntity instanceof GT_MetaTileEntity_DigitalChestBase) {
            GT_MetaTileEntity_DigitalChestBase dc = (GT_MetaTileEntity_DigitalChestBase)aTileEntity;
            tMax = dc.getMaxItemCount(); // currently it is limited by int, but there is not much reason for that
            ItemStack[] inv = dc.getStoredItemData();
            if (inv != null && inv.length > 1 && inv[1] != null)
                tUsed = inv[1].stackSize;
        } else {
            int[] tSlots = (aCoverVariable & SLOT_MASK) > 0 ?
                 new int[] {(aCoverVariable & SLOT_MASK) - 1} :
                 aTileEntity.getAccessibleSlotsFromSide(aSide);

            for (int i : tSlots) {
                if (i >= 0 && i < aTileEntity.getSizeInventory()) {
                    tMax += 64;
                    ItemStack tStack = aTileEntity.getStackInSlot(i);
                    if (tStack != null)
                        tUsed += (tStack.stackSize << 6) / tStack.getMaxStackSize();
                }
            }
        }

        boolean inverted = (aCoverVariable & INVERT_BIT) == INVERT_BIT;
        if(tUsed==0)//nothing
            aTileEntity.setOutputRedstoneSignal(aSide, (byte)(inverted ? 15 : 0));
        else if(tUsed >= tMax)//full
            aTileEntity.setOutputRedstoneSignal(aSide, (byte)(inverted ? 0 : 15));
        else//1-14 range
            aTileEntity.setOutputRedstoneSignal(aSide, (byte)(inverted ? 14-((14*tUsed)/tMax) : 1+((14*tUsed)/tMax)) );
        return aCoverVariable;
    }

    @Override
    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            if ((aCoverVariable & INVERT_BIT) == INVERT_BIT) {
                aCoverVariable = aCoverVariable & ~INVERT_BIT;
                GT_Utility.sendChatToPlayer(aPlayer, trans("NORMAL","Normal"));
            }
            else {
                aCoverVariable = aCoverVariable | INVERT_BIT;
                GT_Utility.sendChatToPlayer(aPlayer, trans("INVERTED","Inverted"));
            }
            return aCoverVariable;
        }

        int slot = (aCoverVariable & SLOT_MASK) + 1;
        if (slot > aTileEntity.getSizeInventory() || slot > SLOT_MASK)
            slot = 0;

        if (slot == 0)
            GT_Utility.sendChatToPlayer(aPlayer, trans("053", "Slot: ") + trans("ALL", "All"));
        else
            GT_Utility.sendChatToPlayer(aPlayer, trans("053", "Slot: ") + (slot - 1));
        return CONVERTED_BIT | (aCoverVariable & INVERT_BIT) | slot;
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

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

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
                maxSlot = Math.min(tile.getSizeInventory() - 1, SLOT_MASK-1);
            else
                maxSlot = -1;

            if (maxSlot == -1 || tile instanceof GT_MetaTileEntity_DigitalChestBase)
                intSlot.setEnabled(false);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            if (isInverted())
                this.getFontRenderer().drawString(INVERTED,  startX + spaceX*3, 4+startY+spaceY*0, 0xFF555555);
            else
                this.getFontRenderer().drawString(NORMAL,  startX + spaceX*3, 4+startY+spaceY*0, 0xFF555555);

            this.getFontRenderer().drawString(trans("254", "Detect slot#"),     startX + spaceX*3, 4+startY+spaceY*1, 0xFF555555);
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
                coverVariable = (coverVariable & ~INVERT_BIT);
            else
                coverVariable = (coverVariable | INVERT_BIT);

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
            int val = parseTextBox(box)+1;

            if (val > SLOT_MASK)
                val = SLOT_MASK;
            else if (val < 0)
                val = 0;

            coverVariable = val | CONVERTED_BIT | (coverVariable & INVERT_BIT);

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
            return ((coverVariable & INVERT_BIT) != 0);
        }

        private int getSlot() {
            return (coverVariable & SLOT_MASK) - 1;
        }
    }
}
