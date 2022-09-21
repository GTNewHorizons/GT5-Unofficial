package gregtech.common.covers.redstone;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.GregTech_API;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.UUID;

public abstract class GT_Cover_AdvancedRedstoneTransmitterBase extends GT_Cover_AdvancedWirelessRedstoneBase<GT_Cover_AdvancedRedstoneTransmitterBase.TransmitterData> {

    public GT_Cover_AdvancedRedstoneTransmitterBase(ITexture coverTexture) {
        super(TransmitterData.class, coverTexture);
    }

    @Override
    public TransmitterData createDataObject() {
        return new TransmitterData();
    }

    @Override
    public TransmitterData createDataObject(int aLegacyData) {
        return createDataObject();
    }

    @Override
    public boolean onCoverRemovalImpl(byte aSide, int aCoverID, TransmitterData aCoverVariable, ICoverable aTileEntity,
                                      boolean aForced) {
        long hash = GregTech_API.hashCoverCoords(aTileEntity, aSide);
        GregTech_API.removeAdvancedRedstone(aCoverVariable.uuid, aCoverVariable.frequency, hash);
        return true;
    }

    @Override
    public String getDescriptionImpl(byte aSide, int aCoverID, TransmitterData aCoverVariable, ICoverable aTileEntity) {
        return GT_Utility.trans("081", "Frequency: ") + aCoverVariable.frequency + ", Transmission: " + (aCoverVariable.uuid == null ? "Public" : "Private");
    }

    /**
     * GUI Stuff
     */

    @Override
    public Object getClientGUIImpl(byte aSide, int aCoverID, TransmitterData aCoverVariable, ICoverable aTileEntity,
                                   EntityPlayer aPlayer, World aWorld) {
        return new TransmitterGUI(aSide, aCoverID, aCoverVariable, aTileEntity);
    }

    public static class TransmitterData implements IWirelessObject {
        private int frequency;

        /**
         * If UUID is set to null, the cover frequency is public, rather than private
         **/
        private UUID uuid;
        private boolean invert;

        public TransmitterData(int frequency, UUID uuid, boolean invert) {
            this.frequency = frequency;
            this.uuid = uuid;
            this.invert = invert;
        }

        public TransmitterData() {
            this(0, null, false);
        }

        @Override
        public UUID getUuid() {
            return uuid;
        }

        @Override
        public void setUuid(UUID uuid) {
            this.uuid = uuid;
        }

        @Override
        public int getFrequency() {
            return frequency;
        }

        @Override
        public void setFrequency(int frequency) {
            this.frequency = frequency;
        }

        public boolean isInvert() {
            return invert;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new TransmitterData(frequency, uuid, invert);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("frequency", frequency);
            if (uuid != null) {
                tag.setString("uuid", uuid.toString());
            }
            tag.setBoolean("invert", invert);

            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeInt(frequency);
            aBuf.writeBoolean(uuid != null);
            if (uuid != null) {
                aBuf.writeLong(uuid.getLeastSignificantBits());
                aBuf.writeLong(uuid.getMostSignificantBits());
            }
            aBuf.writeBoolean(invert);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            NBTTagCompound tag = (NBTTagCompound) aNBT;
            frequency = tag.getInteger("frequency");
            if (tag.hasKey("uuid")) {
                uuid = UUID.fromString(tag.getString("uuid"));
            }
            invert = tag.getBoolean("invert");
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            int oldFrequency = frequency;
            UUID oldUuid = uuid;
            boolean oldInvert = invert;

            frequency = aBuf.readInt();
            if (aBuf.readBoolean()) {
                uuid = new UUID(aBuf.readLong(), aBuf.readLong());
            }
            invert = aBuf.readBoolean();

            if (oldFrequency != frequency || !Objects.equals(oldUuid, uuid) || oldInvert != invert) {
                GregTech_API.resetAdvancedRedstoneFrequency(uuid, frequency);
                GregTech_API.resetAdvancedRedstoneFrequency(oldUuid, oldFrequency);
            }

            return this;
        }
    }

    private class TransmitterGUI extends WirelessGUI {

        private final GT_GuiIconCheckButton invertButton;

        private final String INVERTED = GT_Utility.trans("INVERTED", "Inverted");
        private final String NORMAL = GT_Utility.trans("NORMAL", "Normal");

        public TransmitterGUI(byte aSide, int aCoverID, TransmitterData aCoverVariable, ICoverable aTileEntity) {
            super(aSide, aCoverID, aCoverVariable, aTileEntity);
            invertButton = new GT_GuiIconCheckButton(this, 1, startX, startY + spaceY * 2, GT_GuiIcon.REDSTONE_ON, GT_GuiIcon.REDSTONE_OFF, INVERTED, NORMAL);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.getFontRenderer().drawString(
                GT_Utility.trans("246", "Frequency"),
                startX + spaceX * 5,
                4 + startY,
                textColor);
            this.getFontRenderer().drawString(
                GT_Utility.trans("601", "Use Private Frequency"),
                startX + spaceX,
                startY + spaceY * 1 + 4,
                textColor);
            this.getFontRenderer().drawString(
                coverVariable.invert ? INVERTED : NORMAL,
                startX + spaceX,
                startY + spaceY * 2 + 4,
                textColor);
        }

        @Override
        protected void update() {
            super.update();
            invertButton.setChecked(coverVariable.invert);
        }

        @Override
        public void buttonClicked(GuiButton btn) {
            if (btn == invertButton) {
                coverVariable.invert = !coverVariable.invert;
            }

            super.buttonClicked(btn);
        }
    }
}
