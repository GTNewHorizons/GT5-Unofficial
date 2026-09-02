package gregtech.api.net;

import static gregtech.GTLoggers.GT_FML_LOGGER;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.common.GTWorldgenerator;
import gregtech.common.GTWorldgenerator.OregenPattern;
import io.netty.buffer.ByteBuf;

public class GTPacketSendOregenPattern extends GTPacket {

    /** Null when decoding failed, so a broken packet leaves the client's pattern alone instead of guessing one. */
    protected final OregenPattern pattern;

    public GTPacketSendOregenPattern() {
        this(null);
    }

    public GTPacketSendOregenPattern(OregenPattern pattern) {
        super();
        this.pattern = pattern;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(this.pattern.ordinal());
    }

    @Override
    public GTPacket decode(ByteArrayDataInput aData) {
        int ordinal = aData.readInt();
        // make sure we get valid data:
        if (ordinal >= 0 && ordinal < OregenPattern.values().length) {
            return new GTPacketSendOregenPattern(OregenPattern.values()[ordinal]);
        }
        // A guess here would be indistinguishable from a real answer, so report nothing rather than the default
        GT_FML_LOGGER.error(
            "Received invalid data! Received {} but value must be between 0 and {}! The ore vein pattern is left unchanged.",
            ordinal,
            OregenPattern.values().length - 1);
        return new GTPacketSendOregenPattern();
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.SEND_OREGEN_PATTERN.id;
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (this.pattern == null) return;
        GTWorldgenerator.setClientOregenPattern(this.pattern);
    }

}
