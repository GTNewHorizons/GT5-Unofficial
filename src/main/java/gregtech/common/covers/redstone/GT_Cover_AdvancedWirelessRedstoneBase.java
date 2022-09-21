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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

import javax.annotation.Nonnull;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public abstract class GT_Cover_AdvancedWirelessRedstoneBase<T extends GT_Cover_AdvancedWirelessRedstoneBase.WirelessData> extends GT_CoverBehaviorBase<T> {

    public GT_Cover_AdvancedWirelessRedstoneBase(Class<T> typeToken, ITexture coverTexture) {
        super(typeToken, coverTexture);
    }

    public static Byte getSignalAt(UUID uuid, int frequency, GT_Cover_AdvancedRedstoneReceiverBase.GateMode mode) {
        Map<Integer, Map<Long, Byte>> frequencies = GregTech_API.sAdvancedWirelessRedstone.get(String.valueOf(uuid));
        if (frequencies == null) return 0;

        Map<Long, Byte> signals = frequencies.get(frequency);
        if (signals == null) signals = new ConcurrentHashMap<>();

        switch (mode) {
            case AND:
                return (byte) (signals.values().stream()
                    .map(signal -> signal > 0)
                    .reduce(true, (signalA, signalB) -> signalA && signalB) ? 15 : 0);
            case NAND:
                return (byte) (signals.values().stream()
                    .map(signal -> signal > 0)
                    .reduce(true, (signalA, signalB) -> signalA && signalB) ? 0 : 15);
            case OR:
                return (byte) (signals.values().stream()
                    .map(signal -> signal > 0)
                    .reduce(false, (signalA, signalB) -> signalA || signalB) ? 15 : 0);
            case NOR:
                return (byte) (signals.values().stream()
                    .map(signal -> signal > 0)
                    .reduce(false, (signalA, signalB) -> signalA || signalB) ? 0 : 15);
            default:
                return 0;
        }
    }

    public static void removeSignalAt(UUID uuid, int frequency, long hash) {
        Map<Integer, Map<Long, Byte>> frequencies = GregTech_API.sAdvancedWirelessRedstone.get(String.valueOf(uuid));
        if (frequencies == null) return;
        frequencies.computeIfPresent(frequency, (freq, longByteMap) -> {
            longByteMap.remove(hash);
            return longByteMap.isEmpty() ? null : longByteMap;
        });
    }

    public static void setSignalAt(UUID uuid, int frequency, long hash, byte value) {
        Map<Integer, Map<Long, Byte>> frequencies = GregTech_API.sAdvancedWirelessRedstone.computeIfAbsent(String.valueOf(uuid), k -> new ConcurrentHashMap<>());
        Map<Long, Byte> signals = frequencies.computeIfAbsent(frequency, k -> new ConcurrentHashMap<>());
        signals.put(hash, value);
    }

    @Override
    public boolean letsEnergyInImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOutImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidInImpl(byte aSide, int aCoverID, T aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOutImpl(byte aSide, int aCoverID, T aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsInImpl(byte aSide, int aCoverID, T aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOutImpl(byte aSide, int aCoverID, T aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public String getDescriptionImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return GT_Utility.trans("081", "Frequency: ") + aCoverVariable.frequency + ", Transmission: " + (aCoverVariable.uuid == null ? "Public" : "Private");
    }

    @Override
    public int getTickRateImpl(byte aSide, int aCoverID, T aCoverVariable, ICoverable aTileEntity) {
        return 5;
    }


    public static abstract class WirelessData implements ISerializableObject {
        protected int frequency;

        /**
         * If UUID is set to null, the cover frequency is public, rather than private
         **/
        protected UUID uuid;

        public WirelessData(int frequency, UUID uuid) {
            this.frequency = frequency;
            this.uuid = uuid;
        }

        public UUID getUuid() {
            return uuid;
        }

        public int getFrequency() {
            return frequency;
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("frequency", frequency);
            if (uuid != null) {
                tag.setString("uuid", uuid.toString());
            }

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
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            NBTTagCompound tag = (NBTTagCompound) aNBT;
            frequency = tag.getInteger("frequency");
            if (tag.hasKey("uuid")) {
                uuid = UUID.fromString(tag.getString("uuid"));
            }
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            frequency = aBuf.readInt();
            if (aBuf.readBoolean()) {
                uuid = new UUID(aBuf.readLong(), aBuf.readLong());
            }

            return this;
        }
    }

    /**
     * GUI Stuff
     */
    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    protected abstract static class WirelessGUI<X extends WirelessData> extends GT_GUICover {

        protected final byte side;
        protected final int coverID;
        protected final GT_GuiIntegerTextBox frequencyBox;
        protected final GT_GuiIconCheckButton privateButton;
        protected final X coverVariable;

        protected static final int startX = 10;
        protected static final int startY = 25;
        protected static final int spaceX = 18;
        protected static final int spaceY = 18;

        protected final int frequencyRow;
        protected final int buttonRow;
        private final int privateExtraColumn;

        protected final int textColor = this.getTextColorOrDefault("text", 0xFF555555);

        private static final String guiTexturePath = "gregtech:textures/gui/GuiCoverLong.png";

        public WirelessGUI(byte aSide, int aCoverID, X aCoverVariable, ICoverable aTileEntity, int frequencyRow, int buttonRow, boolean shiftPrivateLeft) {
            super(aTileEntity, 250, 107, GT_Utility.intToStack(aCoverID));
            this.mGUIbackgroundLocation = new ResourceLocation(guiTexturePath);
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;
            this.frequencyRow = frequencyRow;
            this.buttonRow = buttonRow;
            this.privateExtraColumn = shiftPrivateLeft ? 1 : 5;

            frequencyBox = new GT_GuiShortTextBox(this, 0, 1 + startX, 2 + startY + spaceY * frequencyRow, spaceX * 5 - 4, 12);
            privateButton = new GT_GuiIconCheckButton(this, 0, startX, startY + spaceY * buttonRow, GT_GuiIcon.CHECKMARK, null);
        }

        public WirelessGUI(byte aSide, int aCoverID, X aCoverVariable, ICoverable aTileEntity) {
            this(aSide, aCoverID, aCoverVariable, aTileEntity, 0, 1, false);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.getFontRenderer().drawString(
                GT_Utility.trans("246", "Frequency"),
                startX + spaceX * 5,
                4 + startY + spaceY * frequencyRow,
                textColor);
            this.getFontRenderer().drawString(
                GT_Utility.trans("601", "Use Private Frequency"),
                startX + spaceX * privateExtraColumn,
                4 + startY + spaceY * buttonRow,
                textColor);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            update();
            frequencyBox.setFocused(true);
        }

        protected void genericMouseWheel(GT_GuiIntegerTextBox box, int delta, int minValue, int maxValue,
                                         int baseStep, int ctrlStep, int shiftStep) {
            long step = Math.max(1, Math.abs(delta / 120));
            step = (isShiftKeyDown() ? shiftStep : isCtrlKeyDown() ? ctrlStep : baseStep) * (delta > 0 ? step : -step);

            long value = parseTextBox(box) + step;
            if (value > maxValue) value = maxValue;
            else if (value < minValue) value = minValue;

            box.setText(Long.toString(value));
        }

        protected void genericMouseWheel(GT_GuiIntegerTextBox box, int delta, int minValue, int maxValue) {
            genericMouseWheel(box, delta, minValue, maxValue, 1, 50, 1000);
        }

        @Override
        public void onMouseWheel(int x, int y, int delta) {
            if (frequencyBox.isFocused()) {
                genericMouseWheel(frequencyBox, delta, 0, Integer.MAX_VALUE);
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

        protected void update() {
            privateButton.setChecked(coverVariable.uuid != null);
            resetTextBox(frequencyBox);
        }

        @Override
        public void buttonClicked(GuiButton btn) {
            if (btn == privateButton) {
                coverVariable.uuid = coverVariable.uuid == null ? Minecraft.getMinecraft().thePlayer.getUniqueID() : null;
            }

            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCoverNew(side, coverID, coverVariable, tile));
            update();
        }

        protected int parseTextBox(GT_GuiIntegerTextBox box) {
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
