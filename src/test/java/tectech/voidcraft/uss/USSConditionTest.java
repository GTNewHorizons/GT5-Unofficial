package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/**
 * Bare-JVM tests for the program CONDITION ({@link USSCondition}): the op enum, the null-safe factory, the pure
 * string/numeric evaluation rules, and the NBT round-trip.
 */
public class USSConditionTest {

    private static USSCondition cond(USSConditionOp op, String left, String right) {
        return USSCondition.of(USSValue.literal(left), op, USSValue.literal(right));
    }

    @Test
    public void testOpByIdRoundTrip() {
        for (USSConditionOp op : USSConditionOp.values()) {
            assertEquals(op, USSConditionOp.byId(op.getId()));
        }
        assertNull(USSConditionOp.byId(99), "unknown op ids give null (no backwards-compat)");
    }

    @Test
    public void testFactoryIsNullSafe() {
        USSCondition c = USSCondition.of(null, null, null);
        assertEquals(USSConditionOp.EQ, c.op());
        assertEquals(
            "",
            c.left()
                .literal());
        assertEquals(
            "",
            c.right()
                .literal());
    }

    @Test
    public void testEqIsExactAndCaseSensitive() {
        assertTrue(cond(USSConditionOp.EQ, "abc", "abc").evaluate("abc", "abc"));
        assertFalse(cond(USSConditionOp.EQ, "abc", "abd").evaluate("abc", "abd"));
        assertFalse(cond(USSConditionOp.EQ, "abc", "abc").evaluate("Abc", "abc"), "EQ is case-sensitive");
    }

    @Test
    public void testEqTreatsNullSidesAsEmpty() {
        assertTrue(cond(USSConditionOp.EQ, "x", "x").evaluate(null, null));
        assertFalse(cond(USSConditionOp.EQ, "x", "x").evaluate(null, "x"));
    }

    @Test
    public void testNeq() {
        assertTrue(cond(USSConditionOp.NEQ, "a", "b").evaluate("a", "b"));
        assertFalse(cond(USSConditionOp.NEQ, "a", "b").evaluate("a", "a"));
    }

    @Test
    public void testLtIsNumericNotLexicographic() {
        USSCondition lt = cond(USSConditionOp.LT, "3", "10");
        assertTrue(lt.evaluate("3", "10"), "'3' < '10' numerically (lexicographic compare would fail)");
        assertTrue(cond(USSConditionOp.LT, "3", "10").evaluate("3.5", "4"));
        assertTrue(cond(USSConditionOp.LT, "3", "10").evaluate(" 2 ", "3"), "sides are trimmed before parsing");
        assertTrue(cond(USSConditionOp.LT, "3", "10").evaluate("-5", "1"), "negatives compare numerically");
        assertFalse(cond(USSConditionOp.LT, "3", "10").evaluate("10", "3"));
        assertFalse(cond(USSConditionOp.LT, "3", "10").evaluate("5", "5"), "equal values are neither LT");
    }

    @Test
    public void testGtIsNumeric() {
        assertTrue(cond(USSConditionOp.GT, "9", "10").evaluate("10", "9"));
        assertFalse(cond(USSConditionOp.GT, "9", "10").evaluate("9", "10"));
        assertFalse(cond(USSConditionOp.GT, "9", "10").evaluate("5", "5"));
    }

    @Test
    public void testLtGtAreFalseWhenEitherSideIsUnparseable() {
        assertFalse(cond(USSConditionOp.LT, "x", "y").evaluate("abc", "abd"), "unparseable sides → false (LT)");
        assertFalse(cond(USSConditionOp.GT, "x", "y").evaluate("3", "abc"), "one unparseable side → false (GT)");
    }

    @Test
    public void testNbtRoundTrip() {
        USSCondition c = USSCondition.of(USSValue.literal("x"), USSConditionOp.GT, USSValue.variable(5));
        assertEquals(c, USSCondition.readFromNBT(c.writeToNBT()));
    }

    @Test
    public void testReadNullGivesTheDefaultCondition() {
        USSCondition c = USSCondition.readFromNBT(null);
        assertEquals(USSConditionOp.EQ, c.op());
        assertEquals(
            "",
            c.left()
                .literal());
        assertEquals(
            "",
            c.right()
                .literal());
    }

    @Test
    public void testReadUnknownOpFallsBackToEq() {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setInteger("op", 99);
        assertEquals(
            USSConditionOp.EQ,
            USSCondition.readFromNBT(nbt)
                .op());
    }

    @Test
    public void testEquality() {
        assertEquals(cond(USSConditionOp.EQ, "a", "b"), cond(USSConditionOp.EQ, "a", "b"));
        assertFalse(cond(USSConditionOp.EQ, "a", "b").equals(cond(USSConditionOp.NEQ, "a", "b")));
        assertEquals(cond(USSConditionOp.EQ, "a", "b").hashCode(), cond(USSConditionOp.EQ, "a", "b").hashCode());
    }
}
