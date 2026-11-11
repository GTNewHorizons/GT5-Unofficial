package gregtech.api.net;

import static gregtech.GTLoggers.GT_FML_LOGGER;

import net.minecraft.world.IBlockAccess;

import com.google.common.io.ByteArrayDataInput;

import gregtech.common.GTWorldgenerator;
import gregtech.common.GTWorldgenerator.OregenPattern;
import gregtech.common.GTWorldgenerator.PatternSource;
import io.netty.buffer.ByteBuf;

public class GTPacketSendOregenPattern extends GTPacket {

    /** Null when decoding failed, so a broken packet leaves the client's pattern alone instead of guessing one. */
    protected final OregenPattern pattern;
    /**
     * The server's own provenance. A client that only knew the pattern could not tell one read from the world's saved
     * data from one the server had to guess, and would rewrite stored vein coordinates against either.
     */
    protected final PatternSource source;

    public GTPacketSendOregenPattern() {
        this(null, PatternSource.SYNCED);
    }

    public GTPacketSendOregenPattern(OregenPattern pattern, PatternSource source) {
        super();
        this.pattern = pattern;
        this.source = source;
    }

    @Override
    public void encode(ByteBuf aOut) {
        aOut.writeInt(this.pattern.ordinal());
        aOut.writeInt(this.source.ordinal());
    }

    @Override
    public GTPacket decode(ByteArrayDataInput aData) {
        int ordinal = aData.readInt();
        int sourceOrdinal = aData.readInt();

        if (ordinal < 0 || ordinal >= OregenPattern.values().length) {
            // A guess here would be indistinguishable from a real answer, so report nothing rather than the default
            GT_FML_LOGGER.error(
                "Received invalid data! Received {} but value must be between 0 and {}! The ore vein pattern is left unchanged.",
                ordinal,
                OregenPattern.values().length - 1);
            return new GTPacketSendOregenPattern();
        }

        PatternSource source = PatternSource.SYNCED;
        if (sourceOrdinal >= 0 && sourceOrdinal < PatternSource.values().length) {
            source = PatternSource.values()[sourceOrdinal];
        } else {
            // SYNCED is the honest fallback: the server answered, but not with a provenance this build understands
            GT_FML_LOGGER
                .error("Received unknown oregen pattern source {}, treating the pattern as a guess", sourceOrdinal);
        }

        return new GTPacketSendOregenPattern(OregenPattern.values()[ordinal], source);
    }

    @Override
    public byte getPacketID() {
        return GTPacketTypes.SEND_OREGEN_PATTERN.id;
    }

    @Override
    public void process(IBlockAccess aWorld) {
        if (this.pattern == null) return;
        GTWorldgenerator.setClientOregenPattern(this.pattern, this.source);
    }

}
