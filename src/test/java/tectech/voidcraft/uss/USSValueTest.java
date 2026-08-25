package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/**
 * Bare-JVM tests for the program VALUE ({@link USSValue}): the three kinds, clamping/truncation, null-safety and
 * the NBT round-trip.
 */
public class USSValueTest {

    @Test
    public void testLiteralHoldsTheString() {
        USSValue v = USSValue.literal("hello");
        assertEquals(USSValue.Kind.LITERAL, v.kind());
        assertEquals("hello", v.literal());
    }

    @Test
    public void testLiteralNullBecomesEmpty() {
        assertEquals(
            "",
            USSValue.literal(null)
                .literal());
    }

    @Test
    public void testLiteralIsTruncatedToTheCap() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            sb.append('a');
        }
        USSValue v = USSValue.literal(sb.toString());
        assertEquals(
            USSProgram.MAX_LITERAL_LENGTH,
            v.literal()
                .length(),
            "a literal may not exceed 255 chars");
        assertEquals(
            sb.toString()
                .substring(0, USSProgram.MAX_LITERAL_LENGTH),
            v.literal());
    }

    @Test
    public void testVariableClampsTheSlot() {
        assertEquals(
            USSValue.Kind.VAR,
            USSValue.variable(42)
                .kind());
        assertEquals(
            42,
            USSValue.variable(42)
                .slot());
        assertEquals(
            0,
            USSValue.variable(-5)
                .slot(),
            "negative slots clamp to 0");
        assertEquals(
            USSVariableSpace.SLOT_COUNT - 1,
            USSValue.variable(999)
                .slot(),
            "oversized slots clamp to 255");
    }

    @Test
    public void testStatClampsTheId() {
        assertEquals(
            USSValue.Kind.STAT,
            USSValue.stat(7)
                .kind());
        assertEquals(
            7,
            USSValue.stat(7)
                .statId());
        assertEquals(
            0,
            USSValue.stat(-1)
                .statId(),
            "negative stat ids clamp to 0");
    }

    @Test
    public void testKindByIdRoundTrip() {
        for (USSValue.Kind kind : USSValue.Kind.values()) {
            assertEquals(kind, USSValue.Kind.byId(kind.getId()));
        }
        assertNull(USSValue.Kind.byId(99), "unknown ids give null (no backwards-compat)");
    }

    @Test
    public void testNbtRoundTripAllKinds() {
        USSValue[] values = { USSValue.literal("x"), USSValue.variable(123), USSValue.stat(5), USSValue.literal("") };
        for (USSValue v : values) {
            assertEquals(v, USSValue.readFromNBT(v.writeToNBT()), "round-trip: " + v);
        }
    }

    @Test
    public void testReadNullGivesEmptyLiteral() {
        assertEquals(USSValue.literal(""), USSValue.readFromNBT(null));
    }

    @Test
    public void testReadUnknownKindFallsBackToLiteral() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("k", 99);
        nbt.setString("s", "junk");
        assertEquals(
            "junk",
            USSValue.readFromNBT(nbt)
                .literal());
    }

    @Test
    public void testReadMissingFieldsGivesSafeDefaults() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("k", USSValue.Kind.VAR.getId());
        assertEquals(
            0,
            USSValue.readFromNBT(nbt)
                .slot(),
            "missing slot reads as 0 (clamped)");
    }

    @Test
    public void testEquality() {
        assertEquals(USSValue.literal("a"), USSValue.literal("a"));
        assertNotEquals(USSValue.literal("a"), USSValue.literal("b"));
        assertEquals(USSValue.variable(3), USSValue.variable(3));
        assertNotEquals(USSValue.variable(3), USSValue.variable(4));
        assertNotEquals(USSValue.variable(3), USSValue.stat(3), "VAR and STAT with the same number differ");
        assertEquals(
            USSValue.literal("a")
                .hashCode(),
            USSValue.literal("a")
                .hashCode());
    }
}
