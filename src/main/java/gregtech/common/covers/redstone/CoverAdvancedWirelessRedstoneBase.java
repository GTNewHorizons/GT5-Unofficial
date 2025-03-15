package gregtech.common.covers.redstone;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverBehaviorBase;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerTextFieldWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public abstract class CoverAdvancedWirelessRedstoneBase<T extends CoverAdvancedWirelessRedstoneBase.WirelessData>
    extends CoverBehaviorBase<T> {

    public CoverAdvancedWirelessRedstoneBase(CoverContext context, Class<T> typeToken, ITexture coverTexture) {
        super(context, typeToken, coverTexture);
    }

    public static Byte getSignalAt(UUID uuid, String frequency, CoverAdvancedRedstoneReceiverBase.GateMode mode) {
        Map<String, Map<CoverPosition, Byte>> frequencies = GregTechAPI.sAdvancedWirelessRedstone
            .get(String.valueOf(uuid));
        if (frequencies == null) return 0;

        Map<CoverPosition, Byte> signals = frequencies.get(frequency);
        if (signals == null) signals = new ConcurrentHashMap<>();

        switch (mode) {
            case AND -> {
                return (byte) (signals.values()
                    .stream()
                    .map(signal -> signal > 0)
                    .reduce(true, (signalA, signalB) -> signalA && signalB) ? 15 : 0);
            }
            case NAND -> {
                return (byte) (signals.values()
                    .stream()
                    .map(signal -> signal > 0)
                    .reduce(true, (signalA, signalB) -> signalA && signalB) ? 0 : 15);
            }
            case OR -> {
                return (byte) (signals.values()
                    .stream()
                    .map(signal -> signal > 0)
                    .reduce(false, (signalA, signalB) -> signalA || signalB) ? 15 : 0);
            }
            case NOR -> {
                return (byte) (signals.values()
                    .stream()
                    .map(signal -> signal > 0)
                    .reduce(false, (signalA, signalB) -> signalA || signalB) ? 0 : 15);
            }
            case SINGLE_SOURCE -> {
                if (signals.values()
                    .isEmpty()) {
                    return 0;
                }
                return signals.values()
                    .iterator()
                    .next();
            }
            default -> {
                return 0;
            }
        }
    }

    public static void removeSignalAt(UUID uuid, String frequency, CoverPosition key) {
        Map<String, Map<CoverPosition, Byte>> frequencies = GregTechAPI.sAdvancedWirelessRedstone
            .get(String.valueOf(uuid));
        if (frequencies == null) return;
        frequencies.computeIfPresent(String.valueOf(frequency), (freq, longByteMap) -> {
            longByteMap.remove(key);
            return longByteMap.isEmpty() ? null : longByteMap;
        });
    }

    public static void setSignalAt(UUID uuid, String frequency, CoverPosition key, byte value) {
        Map<String, Map<CoverPosition, Byte>> frequencies = GregTechAPI.sAdvancedWirelessRedstone
            .computeIfAbsent(String.valueOf(uuid), k -> new ConcurrentHashMap<>());
        Map<CoverPosition, Byte> signals = frequencies.computeIfAbsent(frequency, k -> new ConcurrentHashMap<>());
        signals.put(key, value);
    }

    public static CoverPosition getCoverKey(@NotNull ICoverable tile, ForgeDirection side) {
        return new CoverPosition(tile.getCoords(), tile.getWorld().provider.dimensionId, side.ordinal());
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public String getDescription() {
        return GTUtility.trans("081", "Frequency: ") + coverData.frequency
            + ", Transmission: "
            + (coverData.uuid == null ? "Public" : "Private");
    }

    @Override
    public int getMinimumTickRate() {
        return 1;
    }

    @Override
    public int getDefaultTickRate() {
        return 5;
    }

    public static class WirelessData implements ISerializableObject {

        protected String frequency;
        protected String label = "";

        /**
         * If UUID is set to null, the cover frequency is public, rather than private
         **/
        protected UUID uuid;

        public WirelessData(String frequency, UUID uuid, String label) {
            this.frequency = frequency;
            this.uuid = uuid;
            this.label = label;
        }

        public WirelessData(String frequency, UUID uuid) {
            this.frequency = frequency;
            this.uuid = uuid;
        }

        public UUID getUuid() {
            return uuid;
        }

        public String getLabel() {
            return this.label;
        }

        public String getFrequency() {
            return frequency;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new WirelessData(frequency, uuid, label);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("frequency", frequency);
            tag.setString("label", this.label);
            if (uuid != null) {
                tag.setString("uuid", uuid.toString());
            }

            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            aBuf.writeBoolean(uuid != null);
            if (uuid != null) {
                aBuf.writeLong(uuid.getLeastSignificantBits());
                aBuf.writeLong(uuid.getMostSignificantBits());
            }
            aBuf.writeInt(frequency.length());
            for (int i = 0; i < frequency.length(); i++) {
                aBuf.writeChar(frequency.charAt(i));
            }
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            NBTTagCompound tag = (NBTTagCompound) aNBT;
            frequency = tag.getString("frequency");
            this.label = tag.getString("label");
            if (tag.hasKey("uuid")) {
                uuid = UUID.fromString(tag.getString("uuid"));
            }
        }

        @Override
        public void readFromPacket(ByteArrayDataInput aBuf) {
            if (aBuf.readBoolean()) {
                uuid = new UUID(aBuf.readLong(), aBuf.readLong());
            }
            int length = aBuf.readInt();
            StringBuilder freqBuilder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                freqBuilder.append(aBuf.readChar());
            }
            this.frequency = freqBuilder.toString();
        }
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    protected abstract class AdvancedWirelessRedstoneBaseUIFactory extends UIFactory {

        protected static final int startX = 10;
        protected static final int startY = 25;
        protected static final int spaceX = 18;
        protected static final int spaceY = 18;

        public AdvancedWirelessRedstoneBaseUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected int getGUIWidth() {
            return 250;
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            final int privateExtraColumn = isShiftPrivateLeft() ? 1 : 5;

            CoverDataControllerWidget<T> dataController = new CoverDataControllerWidget<>(
                this::getCoverData,
                this::setCoverData,
                CoverAdvancedWirelessRedstoneBase.this::loadFromNbt);
            dataController.setPos(startX, startY);
            addUIForDataController(dataController);

            builder.widget(dataController)
                .widget(
                    new TextWidget(GTUtility.trans("246", "Frequency")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 5, 4 + startY + spaceY * getFrequencyRow()))
                .widget(
                    new TextWidget(GTUtility.trans("602", "Use Private Frequency"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * privateExtraColumn, 4 + startY + spaceY * getButtonRow()));
        }

        protected void addUIForDataController(CoverDataControllerWidget<T> controller) {
            controller.addFollower(
                new CoverDataFollowerTextFieldWidget<>(),
                coverData -> coverData.frequency,
                (coverData, newFrequency) -> {
                    coverData.frequency = newFrequency.trim();
                    return coverData;
                },
                widget -> widget.setPos(1, 2 + spaceY * (getFrequencyRow()))
                    .setSize(spaceX * 5 - 4, 12))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofCheck(),
                    coverData -> coverData.uuid != null,
                    (coverData, state) -> {
                        if (state) {
                            coverData.uuid = getUIBuildContext().getPlayer()
                                .getUniqueID();
                        } else {
                            coverData.uuid = null;
                        }
                        return coverData;
                    },
                    widget -> widget.setPos(0, spaceY * getButtonRow()));
        }

        protected abstract int getFrequencyRow();

        protected abstract int getButtonRow();

        protected abstract boolean isShiftPrivateLeft();
    }

    public static class CoverPosition {

        public final int x, y, z;
        public final int dim;
        public final int side;
        private final int hash;

        public CoverPosition(ChunkCoordinates coords, int dim, int side) {
            this.x = coords.posX;
            this.y = coords.posY;
            this.z = coords.posZ;
            this.dim = dim;
            this.side = side;
            this.hash = Objects.hash(x, y, z, dim, side);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CoverPosition that = (CoverPosition) o;
            return this.x == that.x && this.y == that.y
                && this.z == that.z
                && this.dim == that.dim
                && this.side == that.side;
        }

        @Override
        public int hashCode() {
            return this.hash;
        }

        public String getInfo() {
            return String.format("%d, %d, %d DIM: %d", x, y, z, dim);
        }
    }
}
