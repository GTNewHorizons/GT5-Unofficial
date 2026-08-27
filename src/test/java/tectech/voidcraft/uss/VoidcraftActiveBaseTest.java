package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

import tectech.voidcraft.item.ItemVoidbaseBlueprint;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftComponent;
import tectech.voidcraft.ship.VoidcraftConstants;
import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * Unit tests for the active Voidbase (bare-JVM): launch, the integrity time limit (1 per second, 0 =
 * decommissioned), the repair command math, and the NBT round-trip.
 */
public class VoidcraftActiveBaseTest {

    private static final USSPosition POS = USSPosition.of(1.0, 2.0, 3.0);

    /** A 1x1x4 base blueprint: controller + 3 frames (integrity = 10 + 3x10 = 40). */
    private static VoidcraftBlueprint testBase() {
        byte[] grid = { (byte) VoidcraftComponent.CONTROLLER.toGridValue(),
            (byte) VoidcraftComponent.FRAME.toGridValue(), (byte) VoidcraftComponent.FRAME.toGridValue(),
            (byte) VoidcraftComponent.FRAME.toGridValue(), };
        return VoidcraftBlueprint.ofBase(1, 1, 4, grid);
    }

    private static NBTTagCompound testPayload() {
        NBTTagCompound payload = new NBTTagCompound();
        VoidcraftNbt.write(payload, testBase(), "base-uuid", "Test Station", 42L);
        return payload;
    }

    private static VoidcraftActiveBase base(String uuid) {
        return VoidcraftActiveBase.launch(uuid, "Test Station", USSBaseAnchor.planet(2), testPayload(), 7, POS);
    }

    @Test
    public void testLaunchTakesIntegrityFromPayload() {
        VoidcraftActiveBase b = base("base-1");
        assertEquals("base-1", b.uuid());
        assertEquals("Test Station", b.name());
        assertEquals(7, b.seed());
        assertEquals(USSBaseAnchor.planet(2), b.anchor());
        assertEquals(POS, b.position());
        assertEquals(40L, b.maxIntegrity(), "controller 10 + 3 frames x 10");
        assertEquals(40L, b.integrity(), "fresh base starts at its maximum");
        assertNull(b.cargo());
        assertNotNull(b.payload());
    }

    /**
     * A blueprint item whose payload lost its integrity stat (a stale or hand-edited item): the launch must fall
     * back to the stat re-derived from the blueprint grid, not spawn at 1.
     */
    @Test
    public void testLaunchFallsBackToDerivedIntegrityWhenStatMissing() {
        NBTTagCompound payload = testPayload();
        payload.removeTag(VoidcraftNbt.TAG_INTEGRITY);
        VoidcraftActiveBase b = VoidcraftActiveBase
            .launch("base-6", "Test Station", USSBaseAnchor.planet(2), payload, 7, POS);
        assertEquals(40L, b.maxIntegrity(), "re-derived from the blueprint grid when the stat is missing");
        assertEquals(40L, b.integrity(), "the derived value is the spawn value");
    }

    /**
     * The full construction seam: the assembler's blueprint item, copied the way the gateway loads it into the ship
     * payload, read back nested the way the construct handler reads it at launch time - the integrity must survive
     * the whole item -> payload -> nested-copy chain.
     */
    @Test
    public void testBlueprintItemPayloadLaunchesWithFullIntegrity() {
        ItemStack item = ItemVoidbaseBlueprint.fromBlueprint(testBase(), "Voidbase", "item-uuid", 1L, null);
        NBTTagCompound blueprintNbt = item.getTagCompound();
        NBTTagCompound shipPayload = new NBTTagCompound();
        shipPayload.setTag(VoidcraftNbt.TAG_BUILD_BLUEPRINT, blueprintNbt.copy());
        NBTTagCompound bpTag = shipPayload.getCompoundTag(VoidcraftNbt.TAG_BUILD_BLUEPRINT);
        VoidcraftActiveBase b = VoidcraftActiveBase
            .launch("item-uuid", "Voidbase", USSBaseAnchor.planet(0), bpTag, 7, POS);
        assertEquals(40L, b.maxIntegrity(), "the item's stat survives the item -> payload copy chain");
        assertEquals(40L, b.integrity());
    }

    /** A payload with no usable stat and no readable grid falls back to the default floor. */
    @Test
    public void testLaunchFallsBackToDefaultIntegrityForCorruptPayload() {
        NBTTagCompound payload = new NBTTagCompound();
        payload.setInteger(VoidcraftNbt.TAG_FORMAT, VoidcraftConstants.NBT_FORMAT_VERSION);
        VoidcraftActiveBase b = VoidcraftActiveBase.launch("base-7", "Corrupt", USSBaseAnchor.star(), payload, 7, POS);
        assertEquals(VoidcraftActiveBase.DEFAULT_INTEGRITY, b.maxIntegrity(), "no stat and no grid -> the default");
        assertEquals(VoidcraftActiveBase.DEFAULT_INTEGRITY, b.integrity());
    }

    @Test
    public void testIntegrityDecaysOnePerSecond() {
        VoidcraftActiveBase b = base("base-2");
        for (int i = 0; i < 19; i++) {
            assertFalse(b.tickIntegrity(), "no integrity lost within the first second");
        }
        assertEquals(40L, b.integrity());
        assertTrue(b.tickIntegrity() == false, "tick 20 completes the first second");
        assertEquals(39L, b.integrity(), "1 integrity per 20 ticks");
    }

    @Test
    public void testIntegrityZeroDecommissions() {
        VoidcraftActiveBase b = base("base-3");
        b.repair(-999); // no-op: negative amounts are ignored
        // force the integrity down: 39 seconds of decay
        boolean lost = false;
        for (int i = 0; i < 40 * VoidcraftActiveBase.TICKS_PER_INTEGRITY; i++) {
            lost = b.tickIntegrity() || lost;
        }
        assertTrue(lost, "the base decommissions at zero integrity");
        assertEquals(0L, b.integrity());
    }

    /**
     * The exact return-value contract the USS tick wiring uses for the decommission decision: tickIntegrity
     * returns false on EVERY tick while the integrity is still above zero (a healthy base, however worn, never
     * reports decommission) and true from the tick the integrity reaches 0 onward.
     */
    @Test
    public void testTickIntegrityReportsFalseWhileAlive() {
        VoidcraftActiveBase b = base("base-8");
        // 39 full seconds: integrity 40 -> 1, no decommission report anywhere along the way.
        for (int i = 0; i < 39 * VoidcraftActiveBase.TICKS_PER_INTEGRITY; i++) {
            assertFalse(b.tickIntegrity(), "integrity " + b.integrity() + " is still above zero - no decommission");
        }
        assertEquals(1L, b.integrity());
        // One integrity per FULL second: the last point drops only on the 40th second's completing tick.
        for (int i = 0; i < VoidcraftActiveBase.TICKS_PER_INTEGRITY - 1; i++) {
            assertFalse(b.tickIntegrity(), "the last second is still running - no decommission");
        }
        assertTrue(b.tickIntegrity(), "reaching 0 reports decommission");
        assertEquals(0L, b.integrity());
        assertTrue(b.tickIntegrity(), "stays decommissioned");
    }

    @Test
    public void testRepairRefillsIntegrity() {
        VoidcraftActiveBase b = base("base-4");
        assertFalse(b.repair(5), "no effect on a fresh base at maximum");
        // decay one second, then repair well above the maximum - clamped to the maximum
        for (int i = 0; i < VoidcraftActiveBase.TICKS_PER_INTEGRITY; i++) {
            b.tickIntegrity();
        }
        assertEquals(39L, b.integrity());
        assertTrue(b.repair(200));
        assertEquals(40L, b.integrity(), "repair above the maximum is clamped to the maximum");
        assertFalse(b.repair(1), "no effect at the maximum");
        assertFalse(b.repair(0), "zero repair is a no-op");
        assertFalse(b.repair(-1), "negative repair is a no-op");
    }

    @Test
    public void testNbtRoundTrip() {
        VoidcraftActiveBase b = base("base-5");
        b.repair(-1);
        for (int i = 0; i < VoidcraftActiveBase.TICKS_PER_INTEGRITY * 3; i++) {
            b.tickIntegrity();
        }
        b.setPosition(USSPosition.of(9.0, 8.0, 7.0));
        NBTTagCompound cargo = new NBTTagCompound();
        cargo.setString("vc_items_present", "yes");
        b.setCargo(cargo);
        NBTTagCompound nbt = b.writeToNBT();
        VoidcraftActiveBase back = VoidcraftActiveBase.readFromNBT(nbt);
        assertTrue(back != null, "the base round-trips");
        assertEquals("base-5", back.uuid());
        assertEquals("Test Station", back.name());
        assertEquals(7, back.seed());
        assertEquals(USSBaseAnchor.planet(2), back.anchor());
        assertEquals(37L, back.integrity(), "3 seconds of decay survived the round-trip");
        assertEquals(USSPosition.of(9.0, 8.0, 7.0), back.position());
        assertNotNull(back.cargo());
        assertEquals(
            "yes",
            back.cargo()
                .getString("vc_items_present"));
        // The payload (the station grid + stats + program) survives the round-trip
        assertEquals(
            b.payload()
                .getInteger(VoidcraftNbt.TAG_WIDTH),
            back.payload()
                .getInteger(VoidcraftNbt.TAG_WIDTH));
        // And the re-read base still ticks
        for (int i = 0; i < VoidcraftActiveBase.TICKS_PER_INTEGRITY; i++) {
            back.tickIntegrity();
        }
        assertEquals(36L, back.integrity());
    }

    /**
     * A base whose payload carries an energy buffer (POWER_CELL covers) and a generation rate (SOLAR_PANEL
     * covers) - the derived stats the assembler writes.
     */
    private static VoidcraftActiveBase poweredBase(String uuid) {
        NBTTagCompound payload = testPayload();
        payload.setLong(VoidcraftNbt.TAG_ENERGY_BUFFER, 400_000L);
        payload.setLong(VoidcraftNbt.TAG_ENERGY_GEN, 2_000L);
        return VoidcraftActiveBase.launch(uuid, "Powered Station", USSBaseAnchor.star(), payload, 7, POS);
    }

    @Test
    public void testEnergyStartsFullAndTicksGeneration() {
        VoidcraftActiveBase b = poweredBase("energy-1");
        assertEquals(400_000L, b.energyCapacity());
        assertEquals(2_000L, b.energyGen());
        assertEquals(400_000L, b.energy(), "a fresh base starts with a full buffer");
        // drain, then regenerate (capped at capacity).
        b.setEnergy(10_000L);
        for (int i = 0; i < 3; i++) {
            b.tickEnergy();
        }
        assertEquals(16_000L, b.energy(), "3 ticks x 2000 EU/t");
        b.setEnergy(399_500L);
        b.tickEnergy();
        assertEquals(400_000L, b.energy(), "generation is capped at the buffer capacity");
    }

    @Test
    public void testSetEnergyClampsToCapacity() {
        VoidcraftActiveBase b = poweredBase("energy-2");
        b.setEnergy(999_999L);
        assertEquals(400_000L, b.energy(), "above capacity clamps down");
        b.setEnergy(-5L);
        assertEquals(0L, b.energy(), "negative clamps to zero");
    }

    @Test
    public void testRepairDrawRestoresOneIntegrityPerSecond() {
        VoidcraftActiveBase b = poweredBase("energy-3");
        // decay 1 integrity.
        for (int i = 0; i < VoidcraftActiveBase.TICKS_PER_INTEGRITY; i++) {
            b.tickIntegrity();
        }
        assertEquals(39L, b.integrity());
        assertEquals(400_000L, b.energy());
        // 20 ticks of repair (2000 EU/t) restore exactly 1 integrity.
        for (int i = 0; i < VoidcraftActiveBase.TICKS_PER_INTEGRITY - 1; i++) {
            assertTrue(b.addRepair(), "a repair tick consumes energy");
            assertEquals(39L, b.integrity(), "no integrity before the 20th tick");
        }
        assertTrue(b.addRepair(), "the 20th repair tick");
        assertEquals(40L, b.integrity(), "20 ticks restore 1 integrity");
        assertEquals(400_000L - 20L * VoidcraftActiveBase.REPAIR_DRAW, b.energy(), "20 x 2000 EU drawn");
    }

    @Test
    public void testRepairDrawNeedsEnergy() {
        VoidcraftActiveBase b = poweredBase("energy-4");
        b.setEnergy(0L);
        assertFalse(b.addRepair(), "no energy -> no repair");
        b.setEnergy(VoidcraftActiveBase.REPAIR_DRAW - 1L);
        assertFalse(b.addRepair(), "insufficient energy -> no repair");
        // and a full base does not accrue repair ticks.
        assertFalse(b.addRepair(), "nothing to repair at maximum integrity");
    }

    @Test
    public void testEnergySurvivesTheRoundTrip() {
        VoidcraftActiveBase b = poweredBase("energy-5");
        for (int i = 0; i < VoidcraftActiveBase.TICKS_PER_INTEGRITY; i++) {
            b.tickIntegrity(); // decay 1 integrity so the repair ticks accrue
        }
        b.setEnergy(123_456L);
        for (int i = 0; i < 5; i++) {
            assertTrue(b.addRepair());
        }
        NBTTagCompound nbt = b.writeToNBT();
        VoidcraftActiveBase back = VoidcraftActiveBase.readFromNBT(nbt);
        assertNotNull(back);
        assertEquals(39L, back.integrity());
        assertEquals(123_456L - 5L * VoidcraftActiveBase.REPAIR_DRAW, back.energy(), "the drained buffer survives");
    }

    @Test
    public void testReadRejectsCorruptTags() {
        assertNull(VoidcraftActiveBase.readFromNBT(null));
        assertNull(VoidcraftActiveBase.readFromNBT(new NBTTagCompound()), "missing payload -> null");
        // a payload with the right format tag but no grids is not a base
        NBTTagCompound bad = new NBTTagCompound();
        bad.setString("vc_base_uuid", "x");
        NBTTagCompound payload = new NBTTagCompound();
        payload.setInteger(VoidcraftNbt.TAG_FORMAT, VoidcraftConstants.NBT_FORMAT_VERSION);
        bad.setTag("vc_base_payload", payload);
        assertNull(VoidcraftActiveBase.readFromNBT(bad), "an empty payload is not a base");
    }
}
