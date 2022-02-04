package gregtech.common.covers;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiFakeItemButton;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.net.GT_Packet_TileEntityCover;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;

public class GT_Cover_Arm extends GT_CoverBehavior {
    public final int mTickRate;
    //msb converted, 2nd : direction (1=export)
    //right 14 bits: internalSlot, next 14 bits adjSlot, 0 = all, slot = -1
    protected static final int EXPORT_MASK = 0x40000000;
    protected static final int SLOT_ID_MASK = 0x3FFF;
    protected static final int SLOT_ID_MIN = 0;
    protected static final int CONVERTED_BIT = 0x80000000;

    public GT_Cover_Arm(int aTickRate) {
        this.mTickRate = aTickRate;
    }

    @Override
    public boolean isRedstoneSensitive(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if ((((aTileEntity instanceof IMachineProgress)) && (!((IMachineProgress) aTileEntity).isAllowedToWork()))) {
            return aCoverVariable;
        }

        //Convert from ver. 5.09.33.50, check if 3 last bits are equal
        if ((aCoverVariable >>> 29) == 0) {
            aCoverVariable = CONVERTED_BIT | (((aCoverVariable+1) & SLOT_ID_MASK) << 14) | EXPORT_MASK;
        } else if ((aCoverVariable >>> 29) == 7) {
            aCoverVariable = CONVERTED_BIT | Math.min(Math.abs(aCoverVariable-1), SLOT_ID_MASK);
        }

        TileEntity toTile, fromTile;
        int toSlot, fromSlot;

        if ((aCoverVariable & EXPORT_MASK) > 0) {
            fromTile = (TileEntity) aTileEntity;
            toTile = aTileEntity.getTileEntityAtSide(aSide);
            fromSlot = aCoverVariable & SLOT_ID_MASK;
            toSlot = (aCoverVariable>>14) & SLOT_ID_MASK;
        } else {
            fromTile = aTileEntity.getTileEntityAtSide(aSide);
            toTile = (TileEntity) aTileEntity;
            fromSlot = (aCoverVariable>>14) & SLOT_ID_MASK;
            toSlot = aCoverVariable & SLOT_ID_MASK;
        }

        byte movedItems = 0;
        if(fromSlot > 0 && toSlot > 0) {
            if (fromTile instanceof IInventory && toTile instanceof IInventory)
                movedItems = GT_Utility.moveFromSlotToSlot((IInventory) fromTile, (IInventory) toTile, fromSlot-1, toSlot-1, null, false, (byte)64, (byte)1, (byte)64, (byte)1);
        } else if (toSlot > 0) {
            byte side;
            if ((aCoverVariable & EXPORT_MASK) > 0)
                side = aSide;
            else
                side = GT_Utility.getOppositeSide(aSide);
            movedItems = GT_Utility.moveOneItemStackIntoSlot(fromTile, toTile, side, toSlot-1, null, false, (byte)64, (byte)1, (byte)64, (byte)1);
        } else if (fromSlot > 0) {
            byte toSide;
            if ((aCoverVariable & EXPORT_MASK) > 0)
                toSide = aSide;
            else
                toSide = GT_Utility.getOppositeSide(aSide);
            if (fromTile instanceof IInventory)
                movedItems = GT_Utility.moveFromSlotToSide((IInventory) fromTile, toTile, fromSlot-1, toSide, null, false, (byte)64, (byte)1, (byte)64, (byte)1);
        } else {
            byte fromSide, toSide;
            if ((aCoverVariable & EXPORT_MASK) > 0) {
                fromSide = aSide;
                toSide = GT_Utility.getOppositeSide(aSide);
            } else {
                fromSide = GT_Utility.getOppositeSide(aSide);
                toSide = aSide;
            }
            movedItems = GT_Utility.moveOneItemStack(fromTile, toTile, fromSide, toSide, null, false, (byte)64, (byte)1, (byte)64, (byte)1);
        }

        return aCoverVariable;
    }

    @Override
    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int step = 0;
        if (GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ)[0] >= 0.5F) {
            step += aPlayer.isSneaking() ? 256 : 16;
        } else {
            step -= aPlayer.isSneaking() ? 256 : 16;
        }
        aCoverVariable = getNewVar(aCoverVariable, step);
        sendMessageToPlayer(aPlayer, aCoverVariable);
        return aCoverVariable;
    }

    @Override
    protected boolean onCoverRightClickImpl(byte aSide, int aCoverID, ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int step = (GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ)[0] >= 0.5F) ? 1 : -1;
        int tCoverVariable = getNewVar(aCoverVariable.get(), step);
        sendMessageToPlayer(aPlayer, tCoverVariable);
        aCoverVariable.set(tCoverVariable);
        return true;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCoverRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int step = (GT_Utility.getClickedFacingCoords(aSide, aX, aY, aZ)[0] >= 0.5F) ? 1 : -1;
        aCoverVariable = getNewVar(aCoverVariable, step);
        sendMessageToPlayer(aPlayer, aCoverVariable);
        aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
        return true;
    }

    private void sendMessageToPlayer(EntityPlayer aPlayer, int var) {
        if ((var & EXPORT_MASK) != 0)
            GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("001", "Puts out into adjacent Slot #") + (((var >> 14) & SLOT_ID_MASK) - 1));
        else
            GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("002", "Grabs in for own Slot #") + ((var & SLOT_ID_MASK) - 1));
    }

    private int getNewVar(int var, int step) {
        int intSlot = (var & SLOT_ID_MASK);
        int adjSlot = (var >> 14) & SLOT_ID_MASK;
        if ((var & EXPORT_MASK) == 0) {
            int x = (intSlot + step);
            if (x > SLOT_ID_MASK )
                return createVar(0, SLOT_ID_MASK, 0);
            else if (x < 1)
                return createVar(-step - intSlot + 1, 0, EXPORT_MASK);
            else
                return createVar(0, x, 0);
        } else {
            int x = (adjSlot - step);
            if (x > SLOT_ID_MASK)
                return createVar(SLOT_ID_MASK, 0, EXPORT_MASK);
            else if (x < 1)
                return createVar(0, +step - adjSlot + 1, 0);
            else
                return createVar(x, 0, EXPORT_MASK);
        }
    }

    private int createVar(int adjSlot, int intSlot, int export){
        return  CONVERTED_BIT | export | ((adjSlot & SLOT_ID_MASK) << 14) | (intSlot & SLOT_ID_MASK);
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
    public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return this.mTickRate;
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
        return new GT_Cover_Arm.GUI(aSide, aCoverID, coverData, aTileEntity);
    }

    private class GUI extends GT_GUICover {
        private final byte side;
        private final int coverID;
        private GT_GuiIntegerTextBox intSlot, adjSlot;
        private GT_GuiFakeItemButton intSlotIcon, adjSlotIcon;
        private int coverVariable;

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        private final String ANY_TEXT = GT_Utility.trans("ANY", "Any");

        private boolean export;
        private int internalSlotID, adjacentSlotID;

        private final int maxIntSlot, maxAdjSlot;

        public GUI(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;

            export = (coverVariable & EXPORT_MASK) > 0;
            internalSlotID = (coverVariable & SLOT_ID_MASK);
            adjacentSlotID = (coverVariable >> 14) & SLOT_ID_MASK;

            new GT_GuiIconButton(this, 0, startX + spaceX * 0, startY + spaceY * 0, GT_GuiIcon.EXPORT).setTooltipText(GT_Utility.trans("006", "Export"));
            new GT_GuiIconButton(this, 1, startX + spaceX * 1, startY + spaceY * 0, GT_GuiIcon.IMPORT).setTooltipText(GT_Utility.trans("007", "Import"));

            intSlot = new GT_GuiIntegerTextBox(this, 2, startX + spaceX * 0, startY + spaceY * 1 + 2, spaceX * 2+5, 12);
            setBoxText(intSlot, internalSlotID-1);
            intSlot.setMaxStringLength(6);

            adjSlot = new GT_GuiIntegerTextBox(this, 3, startX + spaceX * 0, startY + spaceY * 2 + 2, spaceX * 2+5, 12);
            setBoxText(adjSlot, adjacentSlotID-1);
            adjSlot.setMaxStringLength(6);

            //intSlotIcon = new GT_GuiFakeItemButton(this, startX + spaceX * 8-4, startY + spaceY * 1, GT_GuiIcon.SLOT_GRAY);
            //adjSlotIcon = new GT_GuiFakeItemButton(this, startX + spaceX * 8-4, startY + spaceY * 2, GT_GuiIcon.SLOT_GRAY);

            if (super.tile instanceof TileEntity && !super.tile.isDead()) {
                maxIntSlot = tile.getSizeInventory()-1;

                TileEntity adj = super.tile.getTileEntityAtSide(side);
                if (adj instanceof IInventory)
                    maxAdjSlot = ((IInventory) adj).getSizeInventory()-1;
                else
                    maxAdjSlot = -1;
            } else {
                maxIntSlot = -1;
                maxAdjSlot = -1;
            }
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            if (export)
                this.getFontRenderer().drawString(GT_Utility.trans("006", "Export"),  startX + spaceX*3, 4+startY+spaceY*0, 0xFF555555);
            else
                this.getFontRenderer().drawString(GT_Utility.trans("007", "Import"),  startX + spaceX*3, 4+startY+spaceY*0, 0xFF555555);

            this.getFontRenderer().drawString(GT_Utility.trans("254", "Internal slot#"),     startX + spaceX*3, 4+startY+spaceY*1, 0xFF555555);
            this.getFontRenderer().drawString(GT_Utility.trans("255", "Adjacent slot#"),  startX + spaceX*3, 4+startY+spaceY*2, 0xFF555555);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            intSlot.setFocused(true);
            updateButtons();

            //updateInventorySlots();
        }

        @Override
        public void buttonClicked(GuiButton btn) {
            if (buttonClickable(btn.id)) {
                export = btn.id == 0;
                coverVariable = getNewCoverVariable();
                GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
            }
            updateButtons();
        }

        private void updateButtons() {
            GuiButton b;
            for (Object o : buttonList) {
                b = (GuiButton) o;
                b.enabled = buttonClickable(b.id);
            }
        }

        @Override
        public void onMouseWheel(int x, int y, int delta) {
            for (GT_GuiIntegerTextBox box : textBoxes) {
                if (box.isFocused()) {
                    int step = Math.max(1, Math.abs(delta / 120));
                    step = (isShiftKeyDown() ? 50 : isCtrlKeyDown() ? 5 : 1) * (delta > 0 ? step : -step);
                    int maxSlot = box.id == 3 ? maxAdjSlot : maxIntSlot;
                    int val = parseTextBox(box, maxSlot);
                    if (val < 0)
                        val = -1;
                    val = val + step;

                    if (maxSlot < val)
                        if (maxSlot < 0)
                            val =  -1;
                        else
                            val = maxSlot;
                    else if (val < SLOT_ID_MIN)
                        val =  -1;

                    setBoxText(box, val);
                    return;
                }
            }
        }

        @Override
        public void applyTextBox(GT_GuiIntegerTextBox box) {
            int val = -1;

            if (box.id == 2) {
                val = parseTextBox(box, maxIntSlot);
                internalSlotID = val+1;
            }
            else if (box.id == 3) {
                val = parseTextBox(box, maxAdjSlot);
                adjacentSlotID = val+1;
            }

            setBoxText(box, val);
            coverVariable = getNewCoverVariable();
            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
            //updateInventorySlots();
        }

        @Override
        public void resetTextBox(GT_GuiIntegerTextBox box) {
            int val = 0;
            if (box.id == 2)
                val = internalSlotID-1;
            else if (box.id == 3)
                val = adjacentSlotID-1;
            setBoxText(box, val);
        }

        private void setBoxText(GT_GuiIntegerTextBox box, int val) {
            box.setText( val < 0 ? ANY_TEXT : String.valueOf(val));
        }

        private int parseTextBox(GT_GuiIntegerTextBox box, int maxSlot) {
            String text = box.getText();
            if (text == null)
                return -1;
            text = text.trim();
            if (text.startsWith(ANY_TEXT))
                text = text.substring(ANY_TEXT.length());

            if (text.isEmpty())
                return -1;

            int val;
            try {
                val = Integer.parseInt(text);
            } catch (NumberFormatException e) {
                return -1;
            }

            if (maxSlot < val)
                if (maxSlot < 0)
                    return -1;
                else
                    return maxSlot;
            else if (val < SLOT_ID_MIN)
                return SLOT_ID_MIN;
            return val;
        }

        private int getNewCoverVariable() {
            return (export ? EXPORT_MASK : 0) | ((adjacentSlotID & SLOT_ID_MASK) << 14) | (internalSlotID & SLOT_ID_MASK) | CONVERTED_BIT;
        }

        private boolean buttonClickable(int id) {
            if (id == 0)
                return !export;
            return export;
        }
    }
}
