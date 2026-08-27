package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * Unit tests for the blueprint-item program ({@link USSBlueprintProgram}) — the program stored in a digitized
 * blueprint item NBT after digitization, edited through GUI actions and written back to the item NBT.
 */
public class USSBlueprintProgramTest {

    private static final String INSERT_MOVE_HOME = "{\"op\":\"insert\",\"path\":[],\"index\":0,\"node\":{\"t\":0,\"c\":0,\"p\":{\"target\":\"HOME\"}}}";
    private static final String INSERT_STOP = "{\"op\":\"insert\",\"path\":[],\"index\":1,\"node\":{\"t\":0,\"c\":5}}";

    @Test
    public void testStartsEmptyWithoutAnNbt() {
        USSBlueprintProgram store = new USSBlueprintProgram(null);
        assertTrue(
            store.getProgram()
                .isEmpty());
        assertTrue(
            store.getProgramRows()
                .isEmpty());
        assertEquals("", store.getNote());
    }

    @Test
    public void testStartsEmptyWithAnNbtWithoutTheTag() {
        USSBlueprintProgram store = new USSBlueprintProgram(new NBTTagCompound());
        assertTrue(
            store.getProgram()
                .isEmpty());
    }

    @Test
    public void testReadsTheStoredProgramFromTheItemNbt() {
        USSBlueprintProgram edited = new USSBlueprintProgram(null);
        edited.applyAction(INSERT_MOVE_HOME);
        NBTTagCompound nbt = new NBTTagCompound();
        USSBlueprintProgram.writeProgram(nbt, edited.getProgram());

        USSBlueprintProgram reloaded = new USSBlueprintProgram(nbt);
        assertEquals(edited.getProgram(), reloaded.getProgram());
        assertEquals(
            1,
            reloaded.getProgram()
                .nodeCount());
        assertEquals(
            1,
            reloaded.getProgramRows()
                .size());
    }

    @Test
    public void testAcceptedEditReplacesTheProgramAndClearsTheNote() {
        USSBlueprintProgram store = new USSBlueprintProgram(null);
        assertEquals("", store.getNote());

        store.applyAction("{\"op\":\"bogus\"}");
        assertNotEquals("", store.getNote());

        USSProgramSync.Outcome out = store.applyAction(INSERT_MOVE_HOME);
        assertTrue(out.ok);
        assertEquals(
            1,
            store.getProgram()
                .nodeCount());
        assertEquals("", store.getNote());

        store.applyAction(INSERT_STOP);
        assertEquals(
            2,
            store.getProgram()
                .nodeCount());
    }

    @Test
    public void testRejectedActionKeepsTheProgramAndSetsTheNote() {
        USSBlueprintProgram store = new USSBlueprintProgram(null);
        store.applyAction(INSERT_MOVE_HOME);
        int before = store.getProgram()
            .nodeCount();

        USSProgramSync.Outcome out = store.applyAction("{\"op\":\"remove\",\"path\":[42]}");
        assertFalse(out.ok);
        assertNotEquals("", store.getNote());
        assertEquals(
            before,
            store.getProgram()
                .nodeCount());
    }

    @Test
    public void testWriteProgramRoundTrips() {
        USSBlueprintProgram edited = new USSBlueprintProgram(null);
        edited.applyAction(INSERT_MOVE_HOME);
        edited.applyAction(INSERT_STOP);

        NBTTagCompound nbt = new NBTTagCompound();
        USSBlueprintProgram.writeProgram(nbt, edited.getProgram());
        assertTrue(nbt.hasKey(VoidcraftNbt.TAG_PROGRAM));
        assertEquals(edited.getProgram(), USSBlueprintProgram.readProgram(nbt));
    }

    @Test
    public void testWriteProgramEmptyRemovesTheTag() {
        NBTTagCompound nbt = new NBTTagCompound();
        USSBlueprintProgram store = new USSBlueprintProgram(nbt);
        store.applyAction(INSERT_MOVE_HOME);
        USSBlueprintProgram.writeProgram(nbt, store.getProgram());
        assertTrue(nbt.hasKey(VoidcraftNbt.TAG_PROGRAM));

        USSProgramSync.Outcome out = store.applyAction("{\"op\":\"apply\",\"preset\":\"clear\"}");
        assertTrue(out.ok);
        assertTrue(
            store.getProgram()
                .isEmpty());
        USSBlueprintProgram.writeProgram(nbt, store.getProgram());
        assertFalse(nbt.hasKey(VoidcraftNbt.TAG_PROGRAM));
        assertTrue(
            USSBlueprintProgram.readProgram(nbt)
                .isEmpty());
    }

    @Test
    public void testFullEditSessionSurvivesANbtRoundTrip() {
        USSBlueprintProgram store = new USSBlueprintProgram(null);
        store.applyAction(INSERT_MOVE_HOME);
        store.applyAction(INSERT_STOP);

        NBTTagCompound nbt = new NBTTagCompound();
        USSBlueprintProgram.writeProgram(nbt, store.getProgram());
        USSBlueprintProgram reloaded = new USSBlueprintProgram((NBTTagCompound) nbt.copy());
        assertEquals(store.getProgramRows(), reloaded.getProgramRows());
    }
}
