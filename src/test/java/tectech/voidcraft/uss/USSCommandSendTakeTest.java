package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

/**
 * The SEND / TAKE handlers (ship-to-ship cargo transfer) against the fake context: the param contract (amount /
 * filter / target), the begin → RUNNING/FAILED flow, the tick polling, and the tolerant numeric param read (the
 * 1.7.10 NBT shape the editor and hand-edited programs produce).
 */
final class USSCommandSendTakeTest {

    private static NBTTagCompound params(long amount, String filter, String target) {
        NBTTagCompound p = new NBTTagCompound();
        p.setLong(USSProgramDefaults.PARAM_AMOUNT, amount);
        p.setString(USSProgramDefaults.PARAM_FILTER, filter);
        p.setString(USSProgramDefaults.PARAM_TARGET, target);
        return p;
    }

    @Test
    void testSendWithoutTargetFailsAndLogs() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSCommandSend send = new USSCommandSend();
        assertEquals(
            USSCommandStatus.FAILED,
            send.begin(ctx, USSNode.command(USSCommand.SEND, new NBTTagCompound()), new NBTTagCompound()));
        assertEquals(0, ctx.transferStartCalls, "no transfer may start without a target");
        assertTrue(ctx.loggedContains("SEND: missing target"));
    }

    @Test
    void testTakeWithoutTargetFailsAndLogs() {
        FakeUSSContext ctx = new FakeUSSContext();
        assertEquals(
            USSCommandStatus.FAILED,
            new USSCommandTake()
                .begin(ctx, USSNode.command(USSCommand.TAKE, new NBTTagCompound()), new NBTTagCompound()));
        assertTrue(ctx.loggedContains("TAKE: missing target"));
    }

    @Test
    void testSendStartsRunningAndPassesTheArgsThrough() {
        FakeUSSContext ctx = new FakeUSSContext();
        NBTTagCompound p = params(7L, "copper", "ship-two");
        USSCommandSend send = new USSCommandSend();
        assertEquals(
            USSCommandStatus.RUNNING,
            send.begin(ctx, USSNode.command(USSCommand.SEND, p), new NBTTagCompound()));
        assertEquals(1, ctx.transferStartCalls);
        assertEquals(USSCommand.SEND, ctx.transferStartCommandId);
        assertEquals("ship-two", ctx.transferStartTarget);
        assertEquals(7L, ctx.transferStartAmount);
        assertEquals("copper", ctx.transferStartFilter);
    }

    @Test
    void testTakeStartsRunningAndPassesTheArgsThrough() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSCommandTake take = new USSCommandTake();
        assertEquals(
            USSCommandStatus.RUNNING,
            take.begin(ctx, USSNode.command(USSCommand.TAKE, params(-1L, "*", "ship-two")), new NBTTagCompound()));
        assertEquals(USSCommand.TAKE, ctx.transferStartCommandId);
        assertEquals("ship-two", ctx.transferStartTarget);
        assertEquals(-1L, ctx.transferStartAmount);
        assertEquals("*", ctx.transferStartFilter);
    }

    @Test
    void testDefaultsAreAllUnitsAndMatchAllFilter() {
        FakeUSSContext ctx = new FakeUSSContext();
        NBTTagCompound p = new NBTTagCompound();
        p.setString(USSProgramDefaults.PARAM_TARGET, "ship-two");
        assertEquals(
            USSCommandStatus.RUNNING,
            new USSCommandSend().begin(ctx, USSNode.command(USSCommand.SEND, p), new NBTTagCompound()));
        assertEquals(-1L, ctx.transferStartAmount, "no amount param = ALL");
        assertEquals("", ctx.transferStartFilter, "no filter param = match all (the normalized form)");
    }

    @Test
    void testRefusedStartFailsAndLogs() {
        FakeUSSContext ctx = new FakeUSSContext();
        ctx.transferStartResult = false;
        USSCommandSend send = new USSCommandSend();
        assertEquals(
            USSCommandStatus.FAILED,
            send.begin(ctx, USSNode.command(USSCommand.SEND, params(-1L, "*", "ship-two")), new NBTTagCompound()));
        assertTrue(ctx.loggedContains("cannot transfer to 'ship-two'"));
        ctx.transferStartResult = false;
        assertEquals(
            USSCommandStatus.FAILED,
            new USSCommandTake()
                .begin(ctx, USSNode.command(USSCommand.TAKE, params(-1L, "*", "ship-two")), new NBTTagCompound()));
        assertTrue(ctx.loggedContains("cannot take from 'ship-two'"));
    }

    @Test
    void testTickPollsUntilDone() {
        FakeUSSContext ctx = new FakeUSSContext();
        USSCommandSend send = new USSCommandSend();
        assertEquals(USSCommandStatus.RUNNING, send.tick(ctx, null, new NBTTagCompound()));
        assertEquals(1, ctx.transferTickCalls);
        ctx.transferTickResult = false;
        assertEquals(USSCommandStatus.DONE, send.tick(ctx, null, new NBTTagCompound()));
        assertEquals(
            USSCommandStatus.DONE,
            new USSCommandTake().tick(ctx, null, new NBTTagCompound()),
            "the same polling contract for TAKE");
    }

    @Test
    void testCommandIdsAndRegistration() {
        assertEquals(USSCommand.SEND, new USSCommandSend().commandId());
        assertEquals(USSCommand.TAKE, new USSCommandTake().commandId());
        assertTrue(USSCommandRegistry.has(USSCommand.SEND));
        assertTrue(USSCommandRegistry.has(USSCommand.TAKE));
        assertEquals(
            new USSCommandSend().commandId(),
            USSCommandRegistry.handler(USSCommand.SEND)
                .commandId());
        assertEquals(
            new USSCommandTake().commandId(),
            USSCommandRegistry.handler(USSCommand.TAKE)
                .commandId());
    }

    // region readLongParam (the 1.7.10 NBT shape contract)

    @Test
    void testReadLongParamToleratesEveryShapeTheEditorAndHandEditsProduce() {
        NBTTagCompound intTag = new NBTTagCompound();
        intTag.setInteger("amount", 42);
        assertEquals(42L, USSCommandSend.readLongParam(intTag, "amount", -1L), "int tag reads its int");

        NBTTagCompound longTag = new NBTTagCompound();
        longTag.setLong("amount", 9_000_000_000L);
        assertEquals(9_000_000_000L, USSCommandSend.readLongParam(longTag, "amount", -1L), "long tag reads its long");

        NBTTagCompound stringTag = new NBTTagCompound();
        stringTag.setString("amount", " 42 ");
        assertEquals(
            42L,
            USSCommandSend.readLongParam(stringTag, "amount", -1L),
            "a parseable string is trimmed and parsed");

        NBTTagCompound badString = new NBTTagCompound();
        badString.setString("amount", "lots");
        assertEquals(-1L, USSCommandSend.readLongParam(badString, "amount", -1L), "an unparseable string falls back");

        assertEquals(
            -1L,
            USSCommandSend.readLongParam(new NBTTagCompound(), "amount", -1L),
            "a missing key falls back");
        assertEquals(-1L, USSCommandSend.readLongParam(null, "amount", -1L), "a missing compound falls back");

        NBTTagCompound wrongType = new NBTTagCompound();
        wrongType.setBoolean("amount", true);
        assertEquals(-1L, USSCommandSend.readLongParam(wrongType, "amount", -1L), "a non-numeric type falls back");
    }

    // endregion
}
