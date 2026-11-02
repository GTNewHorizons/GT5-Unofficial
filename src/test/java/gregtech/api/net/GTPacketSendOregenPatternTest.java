package gregtech.api.net;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import gregtech.common.GTWorldgenerator.OregenPattern;
import gregtech.common.GTWorldgenerator.PatternSource;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * A client that only learned the pattern would rewrite stored vein coordinates against a value the server itself had
 * to guess, so the provenance has to survive the wire.
 */
class GTPacketSendOregenPatternTest {

    @Test
    void provenanceSurvivesTheWire() {
        for (PatternSource source : PatternSource.values()) {
            GTPacketSendOregenPattern decoded = roundTrip(OregenPattern.AXISSYMMETRICAL, source);
            assertEquals(OregenPattern.AXISSYMMETRICAL, decoded.pattern);
            assertEquals(source, decoded.source, "the server's provenance has to reach the client intact");
        }
    }

    @Test
    void aServersGuessArrivesAsAGuess() {
        assertFalse(
            roundTrip(OregenPattern.EQUAL_SPACING, PatternSource.UNVERIFIED).source.verified,
            "a pattern the server guessed must not look verified to a client");
        assertTrue(roundTrip(OregenPattern.EQUAL_SPACING, PatternSource.SAVED).source.verified);
    }

    @Test
    void anInvalidPatternChangesNothing() {
        GTPacketSendOregenPattern decoded = decode(bytes(7, PatternSource.SAVED.ordinal()));
        assertNull(decoded.pattern, "an undecodable pattern must leave the client's value alone");
    }

    @Test
    void anUnknownSourceIsTreatedAsAGuess() {
        GTPacketSendOregenPattern decoded = decode(bytes(OregenPattern.EQUAL_SPACING.ordinal(), 42));
        assertEquals(OregenPattern.EQUAL_SPACING, decoded.pattern);
        assertEquals(PatternSource.SYNCED, decoded.source);
        assertFalse(decoded.source.verified);
    }

    private static GTPacketSendOregenPattern roundTrip(OregenPattern pattern, PatternSource source) {
        ByteBuf buffer = Unpooled.buffer();
        new GTPacketSendOregenPattern(pattern, source).encode(buffer);
        return decode(buffer.array());
    }

    private static byte[] bytes(int pattern, int source) {
        ByteBuf buffer = Unpooled.buffer();
        buffer.writeInt(pattern);
        buffer.writeInt(source);
        return buffer.array();
    }

    private static GTPacketSendOregenPattern decode(byte[] payload) {
        // Mirrors GTNetwork.decode, which hands the whole backing array to the packet
        ByteArrayDataInput in = ByteStreams.newDataInput(payload);
        return (GTPacketSendOregenPattern) new GTPacketSendOregenPattern().decode(in);
    }
}
