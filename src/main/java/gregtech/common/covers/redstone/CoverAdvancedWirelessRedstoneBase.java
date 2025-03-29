package gregtech.common.covers.redstone;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;
import io.netty.buffer.ByteBuf;

public abstract class CoverAdvancedWirelessRedstoneBase extends Cover {

    protected int frequency;

    /**
     * If UUID is set to null, the cover frequency is public, rather than private
     **/
    protected UUID uuid;

    public CoverAdvancedWirelessRedstoneBase(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
        initializeData(context.getCoverInitializer());
    }

    public int getFrequency() {
        return frequency;
    }

    public CoverAdvancedWirelessRedstoneBase setFrequency(int frequency) {
        this.frequency = frequency;
        return this;
    }

    public UUID getUuid() {
        return uuid;
    }

    public CoverAdvancedWirelessRedstoneBase setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    @Override
    protected void initializeData() {
        this.frequency = 0;
        this.uuid = null;
    }

    @Override
    protected void loadFromNbt(NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        frequency = tag.getInteger("frequency");
        if (tag.hasKey("uuid")) {
            uuid = UUID.fromString(tag.getString("uuid"));
        }
    }

    @Override
    protected void readFromPacket(ByteArrayDataInput byteData) {
        frequency = byteData.readInt();
        if (byteData.readBoolean()) {
            uuid = new UUID(byteData.readLong(), byteData.readLong());
        }
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("frequency", frequency);
        if (uuid != null) {
            tag.setString("uuid", uuid.toString());
        }

        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeInt(frequency);
        byteBuf.writeBoolean(uuid != null);
        if (uuid != null) {
            byteBuf.writeLong(uuid.getLeastSignificantBits());
            byteBuf.writeLong(uuid.getMostSignificantBits());
        }
    }

    public static Byte getSignalAt(UUID uuid, int frequency, CoverAdvancedRedstoneReceiverBase.GateMode mode) {
        Map<Integer, Map<Long, Byte>> frequencies = GregTechAPI.sAdvancedWirelessRedstone.get(String.valueOf(uuid));
        if (frequencies == null) return 0;

        Map<Long, Byte> signals = frequencies.get(frequency);
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

    public static void removeSignalAt(UUID uuid, int frequency, long hash) {
        Map<Integer, Map<Long, Byte>> frequencies = GregTechAPI.sAdvancedWirelessRedstone.get(String.valueOf(uuid));
        if (frequencies == null) return;
        frequencies.computeIfPresent(frequency, (freq, longByteMap) -> {
            longByteMap.remove(hash);
            return longByteMap.isEmpty() ? null : longByteMap;
        });
    }

    public static void setSignalAt(UUID uuid, int frequency, long hash, byte value) {
        Map<Integer, Map<Long, Byte>> frequencies = GregTechAPI.sAdvancedWirelessRedstone
            .computeIfAbsent(String.valueOf(uuid), k -> new ConcurrentHashMap<>());
        Map<Long, Byte> signals = frequencies.computeIfAbsent(frequency, k -> new ConcurrentHashMap<>());
        signals.put(hash, value);
    }

    /**
     * x hashed into first 20 bytes y hashed into second 20 bytes z hashed into fifth 10 bytes dim hashed into sixth 10
     * bytes side hashed into last 4 bytes
     */
    public static long hashCoverCoords(@NotNull ICoverable tile, @NotNull ForgeDirection side) {
        return (((((long) tile.getXCoord() << 20) + tile.getZCoord() << 10) + tile.getYCoord() << 10)
            + tile.getWorld().provider.dimensionId << 4) + side.ordinal();
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
        return GTUtility.trans("081", "Frequency: ") + frequency
            + ", Transmission: "
            + (uuid == null ? "Public" : "Private");
    }

    @Override
    public int getMinimumTickRate() {
        return 1;
    }

    @Override
    public int getDefaultTickRate() {
        return 5;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

}
