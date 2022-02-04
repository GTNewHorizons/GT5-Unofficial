package gregtech.common.covers;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiFakeItemButton;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GT_Packet_TileEntityCoverNew;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.tileentities.machines.GT_MetaTileEntity_Hatch_OutputBus_ME;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalChestBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;

public class GT_Cover_ItemMeter extends GT_CoverBehaviorBase<GT_Cover_ItemMeter.ItemMeterData> {

    // Legacy data format
    private static final int SLOT_MASK = 0x3FFFFFFF; // 0 = all, 1 = 0 ...
    private static final int CONVERTED_BIT = 0x80000000;
    private static final int INVERT_BIT = 0x40000000;

    public GT_Cover_ItemMeter() {
        super(ItemMeterData.class);
    }

    @Override
    public ItemMeterData createDataObject(int aLegacyData) {
        //Convert from ver. 5.09.33.50
        if ((CONVERTED_BIT & aLegacyData) == 0)
            if (aLegacyData == 0)
                aLegacyData = CONVERTED_BIT;
            else if (aLegacyData == 1)
                aLegacyData = CONVERTED_BIT | INVERT_BIT;
            else if (aLegacyData > 1)
                aLegacyData = CONVERTED_BIT | Math.min((aLegacyData - 2), SLOT_MASK);

        boolean invert = (aLegacyData & INVERT_BIT) == INVERT_BIT;
        int slot = (aLegacyData & SLOT_MASK) - 1;

        return new ItemMeterData(invert, slot, 0);
    }

    @Override
    public ItemMeterData createDataObject() {
        return new ItemMeterData();
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(byte aSide, int aCoverID, ItemMeterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    protected ItemMeterData doCoverThingsImpl(byte aSide, byte aInputRedstone, int aCoverID, ItemMeterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        long tMax = 0;
        long tUsed = 0;
        IMetaTileEntity mte = ((IGregTechTileEntity) aTileEntity).getMetaTileEntity();
        if (mte instanceof GT_MetaTileEntity_DigitalChestBase) {
            GT_MetaTileEntity_DigitalChestBase dc = (GT_MetaTileEntity_DigitalChestBase) mte;
            tMax = dc.getMaxItemCount(); // currently it is limited by int, but there is not much reason for that
            ItemStack[] inv = dc.getStoredItemData();
            if (inv != null && inv.length > 1 && inv[1] != null)
                tUsed = inv[1].stackSize;
        } else if (GregTech_API.mAE2 && mte instanceof GT_MetaTileEntity_Hatch_OutputBus_ME) {
            if (((GT_MetaTileEntity_Hatch_OutputBus_ME) mte).isLastOutputFailed()) {
                tMax = 64;
                tUsed = 64;
            }
        } else {
            int[] tSlots = aCoverVariable.slot >= 0 ?
                    new int[]{aCoverVariable.slot} :
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

        long redstoneSignal;
        if (tUsed == 0L) {
            // nothing
            redstoneSignal = 0;
        } else if (tUsed >= tMax) {
            // full
            redstoneSignal = 15;
        } else {
            // 1-14 range
            redstoneSignal = 1 + (14 * tUsed) / tMax;
        }

        if (aCoverVariable.inverted) {
            redstoneSignal = 15 - redstoneSignal;
        }

        if (aCoverVariable.threshold > 0) {
            if (aCoverVariable.inverted && tUsed >= aCoverVariable.threshold) {
                redstoneSignal = 0;
            } else if (!aCoverVariable.inverted && tUsed < aCoverVariable.threshold) {
                redstoneSignal = 0;
            }
        }

        aTileEntity.setOutputRedstoneSignal(aSide, (byte) redstoneSignal);
        return aCoverVariable;
    }

    @Override
    protected ItemMeterData onCoverScrewdriverClickImpl(byte aSide, int aCoverID, ItemMeterData aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            if (aCoverVariable.inverted) {
                aCoverVariable.inverted = false;
				GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("055", "Normal"));
            }
            else {
                aCoverVariable.inverted = true;
				GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("054", "Inverted"));
            }
        } else {
            aCoverVariable.slot++;
            if (aCoverVariable.slot > aTileEntity.getSizeInventory())
                aCoverVariable.slot = -1;

            if (aCoverVariable.slot == -1)
				GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("053", "Slot: ") + GT_Utility.trans("ALL", "All"));
            else
				GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("053", "Slot: ") + aCoverVariable.slot);
        }

        return aCoverVariable;
    }

    @Override
    protected boolean letsEnergyInImpl(byte aSide, int aCoverID, ItemMeterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsEnergyOutImpl(byte aSide, int aCoverID, ItemMeterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidInImpl(byte aSide, int aCoverID, ItemMeterData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsFluidOutImpl(byte aSide, int aCoverID, ItemMeterData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsInImpl(byte aSide, int aCoverID, ItemMeterData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean letsItemsOutImpl(byte aSide, int aCoverID, ItemMeterData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(byte aSide, int aCoverID, ItemMeterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected int getTickRateImpl(byte aSide, int aCoverID, ItemMeterData aCoverVariable, ICoverable aTileEntity) {
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
    protected Object getClientGUIImpl(byte aSide, int aCoverID, ItemMeterData coverData, ICoverable aTileEntity, EntityPlayer aPlayer, World aWorld) {
        return new GUI(aSide, aCoverID, coverData, aTileEntity);
    }

    public static class ItemMeterData implements ISerializableObject {
        private boolean inverted;
        /** The special value {@code -1} means all slots. */
        private int slot;
        /** The special value {@code 0} means threshold check is disabled. */
        private int threshold;

        public ItemMeterData() {
            inverted = false;
            slot = -1;
            threshold = 0;
        }

        public ItemMeterData(boolean inverted, int slot, int threshold) {
            this.inverted = inverted;
            this.slot = slot;
            this.threshold = threshold;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new ItemMeterData(inverted, slot, threshold);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setBoolean("invert", inverted);
            tag.setInteger("slot", slot);
            tag.setInteger("threshold", threshold);
            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeBoolean(inverted);
            aBuf.writeInt(slot);
            aBuf.writeInt(threshold);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            NBTTagCompound tag = (NBTTagCompound) aNBT;
            inverted = tag.getBoolean("invert");
            slot = tag.getInteger("slot");
            threshold = tag.getInteger("threshold");
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            inverted = aBuf.readBoolean();
            slot = aBuf.readInt();
            threshold = aBuf.readInt();
            return this;
        }
    }

    private class GUI extends GT_GUICover {
        private final byte side;
        private final int coverID;
        private final GT_GuiIconCheckButton invertedButton;
        private final GT_GuiIntegerTextBox intSlot;
        private final GT_GuiFakeItemButton intSlotIcon;
        private final GT_GuiIntegerTextBox thresholdSlot;
        private final ItemMeterData coverVariable;

        private final int maxSlot;

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        private final String ALL = GT_Utility.trans("ALL", "All");
		private final String INVERTED = GT_Utility.trans("INVERTED", "Inverted");
		private final String NORMAL = GT_Utility.trans("NORMAL", "Normal");

		public GUI(byte aSide, int aCoverID, ItemMeterData aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;

            invertedButton = new GT_GuiIconCheckButton(this, 0, startX, startY, GT_GuiIcon.REDSTONE_ON, GT_GuiIcon.REDSTONE_OFF, INVERTED, NORMAL);

            intSlot = new GT_GuiIntegerTextBox(this, 1, startX, startY + spaceY + 2, spaceX * 2 + 5, 12);
            intSlot.setMaxStringLength(6);

            //only shows if opened gui of block sadly, should've used container.
            intSlotIcon = new GT_GuiFakeItemButton(this, startX + spaceX * 8 - 4, startY + spaceY, GT_GuiIcon.SLOT_GRAY);
            intSlotIcon.setMimicSlot(true);

            if (tile instanceof TileEntity && !super.tile.isDead() && tile instanceof IGregTechTileEntity
                && !(((IGregTechTileEntity) tile).getMetaTileEntity() instanceof GT_MetaTileEntity_DigitalChestBase))
                maxSlot = Math.min(tile.getSizeInventory() - 1, SLOT_MASK-1);
            else
                maxSlot = -1;

            intSlot.setEnabled(maxSlot >= 0);

            thresholdSlot = new GT_GuiIntegerTextBox(this, 2, startX, startY + spaceY * 2 + 2, spaceX * 2 + 5, 12);
            thresholdSlot.setMaxStringLength(6);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.getFontRenderer().drawString(coverVariable.inverted ? INVERTED : NORMAL, startX + spaceX * 3, 4 + startY, 0xFF555555);
			this.getFontRenderer().drawString(GT_Utility.trans("254", "Detect slot#"), startX + spaceX * 3, 4 + startY + spaceY, 0xFF555555);
			this.getFontRenderer().drawString(GT_Utility.trans("221", "Item threshold"), startX + spaceX * 3, startY + spaceY * 2 + 4, 0xFF555555);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            update();
            if (intSlot.isEnabled())
                intSlot.setFocused(true);
        }

        @Override
        public void buttonClicked(GuiButton btn) {
            coverVariable.inverted = !coverVariable.inverted;
            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCoverNew(side, coverID, coverVariable, tile));
            update();
        }

        @Override
        public void onMouseWheel(int x, int y, int delta) {
            if (intSlot.isFocused()) {
                int step = Math.max(1, Math.abs(delta / 120));
                step = (isShiftKeyDown() ? 50 : isCtrlKeyDown() ? 5 : 1) * (delta > 0 ? step : -step);
                int val = parseTextBox(intSlot);

                if (val < 0)
                    val = -1;

                val = val + step;

                if (val < 0)
                    val = -1;
                else if (val > maxSlot)
                    val = maxSlot;

                intSlot.setText(val < 0 ? ALL : Integer.toString(val));
            } else if (thresholdSlot.isFocused()) {
                int val = parseTextBox(thresholdSlot);

                int step = 1;
                if (isShiftKeyDown()) {
                    step *= 64;
                }
                if (isCtrlKeyDown()) {
                    step *= 10;
                }

                val += step * Integer.signum(delta);

                val = GT_Utility.clamp(val, 0, getUpperBound());
                thresholdSlot.setText(Integer.toString(val));
            }
        }

        @Override
        public void applyTextBox(GT_GuiIntegerTextBox box) {
            if (box == intSlot) {
                coverVariable.slot = parseTextBox(box);
            } else if (box == thresholdSlot) {
                coverVariable.threshold = parseTextBox(thresholdSlot);
            }

            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCoverNew(side, coverID, coverVariable, tile));
            update();
        }

        @Override
        public void resetTextBox(GT_GuiIntegerTextBox box) {
            if (box == intSlot) {
                intSlot.setText(coverVariable.slot < 0 ? ALL : Integer.toString(coverVariable.slot));
            } else if (box == thresholdSlot) {
                thresholdSlot.setText(Integer.toString(coverVariable.threshold));
            }
        }

        private void update() {
            invertedButton.setChecked(coverVariable.inverted);
            resetTextBox(intSlot);
            resetTextBox(thresholdSlot);

            if (coverVariable.slot < 0) {
                intSlotIcon.setItem(null);
                return;
            }
            if (tile instanceof TileEntity && !super.tile.isDead()) {
                if (tile.getSizeInventory() >= coverVariable.slot) {
                    ItemStack item = tile.getStackInSlot(coverVariable.slot);
                    intSlotIcon.setItem(item);
                    return;
                }
            }
            intSlotIcon.setItem(null);
        }

        private int parseTextBox(GT_GuiIntegerTextBox box) {
            if (box == intSlot) {
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
            } else if (box == thresholdSlot) {
                String text = box.getText();
                if (text == null) {
                    return 0;
                }

                int val;
                try {
                    val = Integer.parseInt(text.trim());
                } catch (NumberFormatException e) {
                    return 0;
                }

                return GT_Utility.clamp(val, 0, getUpperBound());
            }

            throw new UnsupportedOperationException("Unknown text box: " + box);
        }

        private int getUpperBound() {
            return maxSlot > 0 ? maxSlot * 64 : 999_999;
        }
    }
}
