package gregtech.common.covers.redstone;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.GregTech_API;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.GT_Cover_ItemMeter;
import gregtech.common.tileentities.storage.GT_MetaTileEntity_DigitalChestBase;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.UUID;

public class GT_Cover_WirelessItemDetector extends GT_Cover_AdvancedRedstoneTransmitterBase<GT_Cover_WirelessItemDetector.ItemTransmitterData> {

    public GT_Cover_WirelessItemDetector(ITexture coverTexture) {
        super(ItemTransmitterData.class, coverTexture);
    }

    @Override
    public ItemTransmitterData createDataObject() {
        return new ItemTransmitterData();
    }

    @Override
    public ItemTransmitterData createDataObject(int aLegacyData) {
        return createDataObject();
    }

    @Override
    public ItemTransmitterData doCoverThingsImpl(byte aSide, byte aInputRedstone, int aCoverID,
                                                 ItemTransmitterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        byte signal = GT_Cover_ItemMeter.computeSignalBasedOnItems(
                aTileEntity, aCoverVariable.invert, aCoverVariable.threshold, aCoverVariable.slot, aSide);
        long hash = GregTech_API.hashCoverCoords(aTileEntity, aSide);
        setSignalAt(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), hash, signal);

        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoOutImpl(byte aSide, int aCoverID, ItemTransmitterData aCoverVariable,
                                         ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(byte aSide, int aCoverID, ItemTransmitterData aCoverVariable,
                                                         ICoverable aTileEntity) {
        return true;
    }

    public static class ItemTransmitterData extends GT_Cover_AdvancedRedstoneTransmitterBase.TransmitterData {
        /**
         * The special value {@code -1} means all slots.
         */
        private int slot;
        /**
         * The special value {@code 0} means threshold check is disabled.
         */
        private int threshold;

        public ItemTransmitterData(int frequency, UUID uuid, boolean invert, int threshold, int slot) {
            super(frequency, uuid, invert);
            this.threshold = threshold;
            this.slot = slot;
        }

        public ItemTransmitterData() {
            super();
            this.threshold = 0;
            this.slot = -1;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new ItemTransmitterData(frequency, uuid, invert, threshold, slot);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = (NBTTagCompound) super.saveDataToNBT();
            tag.setInteger("threshold", threshold);
            tag.setInteger("slot", slot);

            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            super.writeToByteBuf(aBuf);
            aBuf.writeInt(threshold);
            aBuf.writeInt(slot);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            super.loadDataFromNBT(aNBT);

            NBTTagCompound tag = (NBTTagCompound) aNBT;
            threshold = tag.getInteger("threshold");
            slot = tag.getInteger("slot");
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            super.readFromPacket(aBuf, aPlayer);
            threshold = aBuf.readInt();
            slot = aBuf.readInt();

            return this;
        }
    }

    /**
     * GUI Stuff
     */

    @Override
    public Object getClientGUIImpl(byte aSide, int aCoverID, ItemTransmitterData aCoverVariable, ICoverable aTileEntity,
                                   EntityPlayer aPlayer, World aWorld) {
        return new ItemTransmitterGUI(aSide, aCoverID, aCoverVariable, aTileEntity);
    }

    private class ItemTransmitterGUI extends TransmitterGUI<ItemTransmitterData> {
        private final GT_GuiIntegerTextBox thresholdBox;
        private final GT_GuiIntegerTextBox slotBox;

        private final int maxSlot;

        public ItemTransmitterGUI(byte aSide, int aCoverID, ItemTransmitterData aCoverVariable, ICoverable aTileEntity) {
            super(aSide, aCoverID, aCoverVariable, aTileEntity, 0, 1);

            if (tile instanceof TileEntity && !super.tile.isDead() && tile instanceof IGregTechTileEntity &&
                    !(((IGregTechTileEntity) tile).getMetaTileEntity() instanceof GT_MetaTileEntity_DigitalChestBase)) {
                maxSlot = tile.getSizeInventory() - 1;
            } else {
                maxSlot = -1;
            }

            thresholdBox = new GT_GuiShortTextBox(this, 1, 1 + startX, 2 + startY + spaceY * 2, spaceX * 5 - 4, 12, 0, getMaxCount());
            slotBox = new GT_GuiShortTextBox(this, 2, 1 + startX, 2 + startY + spaceY * 3, spaceX * 3 - 4, 12, -1, maxSlot,
                    Collections.singletonMap("-1", "All"));

            slotBox.setEnabled(maxSlot >= 0);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.getFontRenderer().drawString(
                    GT_Utility.trans("221", "Item Threshold"),
                    startX + spaceX * 5,
                    4 + startY + spaceY * 2,
                    textColor);
            this.getFontRenderer().drawString(
                    GT_Utility.trans("254", "Detect Slot"),
                    startX + spaceX * 5,
                    4 + startY + spaceY * 3,
                    textColor);
        }

        @Override
        public void onMouseWheel(int x, int y, int delta) {
            super.onMouseWheel(x, y, delta);
            if (thresholdBox.isFocused()) {
                genericMouseWheel(thresholdBox, delta, 0, getMaxCount(), 1, 10, 64);
            } else if (slotBox.isFocused()) {
                genericMouseWheel(slotBox, delta, -1, maxSlot, 1, 5, 50);
            }
        }

        @Override
        public void applyTextBox(GT_GuiIntegerTextBox box) {
            if (box == thresholdBox) {
                coverVariable.threshold = parseTextBox(thresholdBox, 0, getMaxCount());
            } else if (box == slotBox) {
                coverVariable.slot = parseTextBox(slotBox, -1, maxSlot);
            }

            super.applyTextBox(box);
        }

        @Override
        public void resetTextBox(GT_GuiIntegerTextBox box) {
            super.resetTextBox(box);
            if (box == thresholdBox) {
                thresholdBox.setText(Integer.toString(coverVariable.threshold));
            } else if (box == slotBox) {
                slotBox.setText(Integer.toString(coverVariable.slot));
            }
        }

        @Override
        protected void update() {
            super.update();
            resetTextBox(thresholdBox);
            resetTextBox(slotBox);
        }

        private int getMaxCount() {
            return maxSlot > 0 ? maxSlot * 64 : Integer.MAX_VALUE;
        }
    }
}
