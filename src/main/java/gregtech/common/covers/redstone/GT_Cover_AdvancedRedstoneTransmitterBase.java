package gregtech.common.covers.redstone;

import com.google.common.io.ByteArrayDataInput;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconCheckButton;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.IGuiScreen;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.net.GT_Packet_TileEntityCoverNew;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import java.util.UUID;

public class GT_Cover_AdvancedRedstoneTransmitterBase extends GT_CoverBehaviorBase<GT_Cover_AdvancedRedstoneTransmitterBase.TransmitterData> {

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
    public boolean letsEnergyInImpl(byte aSide, int aCoverID, TransmitterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOutImpl(byte aSide, int aCoverID, TransmitterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidInImpl(byte aSide, int aCoverID, TransmitterData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOutImpl(byte aSide, int aCoverID, TransmitterData aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsInImpl(byte aSide, int aCoverID, TransmitterData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOutImpl(byte aSide, int aCoverID, TransmitterData aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public String getDescriptionImpl(byte aSide, int aCoverID, TransmitterData aCoverVariable, ICoverable aTileEntity) {
        return GT_Utility.trans("081", "Frequency: ") + aCoverVariable.frequency + ", Transmission: " + (aCoverVariable.uuid == null ? "Public" : "Private");
    }

    @Override
    public int getTickRateImpl(byte aSide, int aCoverID, TransmitterData aCoverVariable, ICoverable aTileEntity) {
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
    public Object getClientGUIImpl(byte aSide, int aCoverID, TransmitterData aCoverVariable, ICoverable aTileEntity,
                                   EntityPlayer aPlayer, World aWorld) {
        return new GT_Cover_AdvancedRedstoneTransmitterBase.GUI(aSide, aCoverID, aCoverVariable, aTileEntity);
    }

    public static class TransmitterData implements ISerializableObject {
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

        public UUID getUuid() {
            return uuid;
        }

        public int getFrequency() {
            return frequency;
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
            frequency = aBuf.readInt();
            if (aBuf.readBoolean()) {
                uuid = new UUID(aBuf.readLong(), aBuf.readLong());
            }
            invert = aBuf.readBoolean();

            return this;
        }
    }

    private class GUI extends GT_GUICover {

        private final byte side;
        private final int coverID;
        private final GT_GuiIntegerTextBox frequencyBox;
        private final GT_GuiIconCheckButton privateButton;
        private final GT_GuiIconCheckButton invertButton;
        private final TransmitterData coverVariable;

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        private final String INVERTED = GT_Utility.trans("INVERTED", "Inverted");
        private final String NORMAL = GT_Utility.trans("NORMAL", "Normal");

        private static final String guiTexturePath = "gregtech:textures/gui/GuiCoverLong.png";

        private final int textColor = this.getTextColorOrDefault("text", 0xFF555555);

        public GUI(byte aSide, int aCoverID, TransmitterData aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 250, 107, GT_Utility.intToStack(aCoverID));
            this.mGUIbackgroundLocation = new ResourceLocation(guiTexturePath);
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;

            privateButton = new GT_GuiIconCheckButton(this, 0, startX, startY + spaceY * 1, GT_GuiIcon.CHECKMARK, null);
            invertButton = new GT_GuiIconCheckButton(this, 0, startX, startY + spaceY * 2, GT_GuiIcon.REDSTONE_ON, GT_GuiIcon.REDSTONE_OFF, INVERTED, NORMAL);
            frequencyBox = new GT_Cover_AdvancedRedstoneTransmitterBase.GUI.GT_GuiShortTextBox(this, 2, startX, startY + 2, spaceX * 5 - 3, 12);
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
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            update();
            frequencyBox.setFocused(true);
        }

        @Override
        public void onMouseWheel(int x, int y, int delta) {
            if (frequencyBox.isFocused()) {
                long step = Math.max(1, Math.abs(delta / 120));
                step = (isShiftKeyDown() ? 1000 : isCtrlKeyDown() ? 50 : 1) * (delta > 0 ? step : -step);

                long frequency = parseTextBox(frequencyBox) + step;
                if (frequency > Integer.MAX_VALUE) frequency = Integer.MAX_VALUE;
                else if (frequency < 0) frequency = 0;

                frequencyBox.setText(Long.toString(frequency));
            }
        }

        @Override
        public void applyTextBox(GT_GuiIntegerTextBox box) {
            if (box == frequencyBox) {
                coverVariable.frequency = parseTextBox(frequencyBox);
            }

            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCoverNew(side, coverID, coverVariable, tile));
            update();
        }

        @Override
        public void resetTextBox(GT_GuiIntegerTextBox box) {
            if (box == frequencyBox) {
                frequencyBox.setText(Integer.toString(coverVariable.frequency));
            }
        }

        private void update() {
            privateButton.setChecked(coverVariable.uuid != null);
            invertButton.setChecked(coverVariable.invert);
            resetTextBox(frequencyBox);
            GregTech_API.resetAdvancedRedstoneFrequency(coverVariable.uuid, coverVariable.frequency);
        }

        @Override
        public void buttonClicked(GuiButton btn) {
            if (btn == privateButton) {
                coverVariable.uuid = coverVariable.uuid == null ? Minecraft.getMinecraft().thePlayer.getUniqueID() : null;
            } else if (btn == invertButton) {
                coverVariable.invert = !coverVariable.invert;
            }

            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCoverNew(side, coverID, coverVariable, tile));
            update();
        }

        private int parseTextBox(GT_GuiIntegerTextBox box) {
            if (box == frequencyBox) {
                String text = box.getText();
                if (text == null) {
                    return 0;
                }

                long frequency;
                try {
                    frequency = Long.parseLong(text.trim());
                } catch (NumberFormatException e) {
                    return 0;
                }

                if (frequency > Integer.MAX_VALUE) frequency = Integer.MAX_VALUE;
                else if (frequency < 0) frequency = 0;

                return (int) frequency;
            }

            throw new UnsupportedOperationException("Unknown text box: " + box);
        }

        private class GT_GuiShortTextBox extends GT_GuiIntegerTextBox {

            public GT_GuiShortTextBox(IGuiScreen gui, int id, int x, int y, int width, int height) {
                super(gui, id, x, y, width, height);
            }

            @Override
            public boolean textboxKeyTyped(char c, int key) {
                if (!super.textboxKeyTyped(c, key)) return false;

                String text = getText().trim();
                if (text.length() > 0) {
                    setText(String.valueOf(parseTextBox(this)));
                }

                return true;
            }
        }
    }
}
