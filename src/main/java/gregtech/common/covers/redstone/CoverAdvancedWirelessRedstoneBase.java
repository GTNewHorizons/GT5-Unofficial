package gregtech.common.covers.redstone;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

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
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverPosition;
import io.netty.buffer.ByteBuf;

public abstract class CoverAdvancedWirelessRedstoneBase extends Cover {

    protected String frequency;

    /**
     * If UUID is set to null, the cover frequency is public, rather than private
     *
     **/
    protected UUID uuid;

    public CoverAdvancedWirelessRedstoneBase(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
        this.frequency = "0";
        this.uuid = null;
    }

    public String getFrequency() {
        return frequency;
    }

    public CoverAdvancedWirelessRedstoneBase setFrequency(String frequency) {
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
    protected void readDataFromNbt(NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        ICoverable cover = coveredTile.get();;
        if (cover != null && cover.getWorld() != null) {
            GregTechAPI.sAdvancedWirelessRedstone
                .computeIfAbsent(uuid == null ? "null" : uuid.toString(), (k) -> new ConcurrentHashMap<>())
                .computeIfAbsent(frequency, (k) -> new ConcurrentHashMap<>())
                .remove(getCoverKey(cover, coverSide));
        }
        frequency = tag.getString("frequency");
        if (tag.hasKey("uuid")) {
            uuid = UUID.fromString(tag.getString("uuid"));
        } else {
            uuid = null;
        }
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        if (byteData.readBoolean()) {
            uuid = new UUID(byteData.readLong(), byteData.readLong());
        }
        int length = byteData.readInt();
        StringBuilder freqBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            freqBuilder.append(byteData.readChar());
        }
        this.frequency = freqBuilder.toString();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("frequency", frequency);
        if (uuid != null) {
            tag.setString("uuid", uuid.toString());
        }

        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeBoolean(uuid != null);
        if (uuid != null) {
            byteBuf.writeLong(uuid.getMostSignificantBits());
            byteBuf.writeLong(uuid.getLeastSignificantBits());
        }
        byteBuf.writeInt(frequency.length());
        for (int i = 0; i < frequency.length(); i++) {
            byteBuf.writeChar(frequency.charAt(i));
        }
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
        frequencies.computeIfPresent(frequency, (freq, coverPositionByteMap) -> {
            coverPositionByteMap.remove(key);
            return coverPositionByteMap.isEmpty() ? null : coverPositionByteMap;
        });
    }

    public static void setSignalAt(UUID uuid, String frequency, CoverPosition key, byte value) {
        Map<String, Map<CoverPosition, Byte>> frequencies = GregTechAPI.sAdvancedWirelessRedstone
            .computeIfAbsent(String.valueOf(uuid), k -> new ConcurrentHashMap<>());
        Map<CoverPosition, Byte> signals = frequencies.computeIfAbsent(frequency, k -> new ConcurrentHashMap<>());
        signals.put(key, value);
    }

    public static CoverPosition getCoverKey(@NotNull ICoverable tile, ForgeDirection side) {
        return new CoverPosition(
            tile.getCoords(),
            tile.getWorld().provider.getDimensionName(),
            tile.getWorld().provider.dimensionId,
            side.ordinal());
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
        return translateToLocalFormatted(
            "gt.interact.desc.freq_perm",
            frequency,
            uuid == null ? translateToLocal("gt.interact.desc.public") : translateToLocal("gt.interact.desc.private"));
    }

    @Override
    public int getMinimumTickRate() {
        return 1;
    }

    @Override
    public int getDefaultTickRate() {
        return 5;
    }

    public void syncPrivacyState(boolean b, UUID uuid) {
        this.uuid = b ? uuid : null;
    }

    public boolean getPrivacyState() {
        return this.uuid != null;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

}
