package tectech.voidcraft.uss;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftComponent;
import tectech.voidcraft.ship.VoidcraftCoverComponent;

/**
 * Unit tests for the Voidbase construction site: creation from a blueprint parts list, partial deliveries
 * with overflow discarded, completion, and the NBT round-trip (bare-JVM).
 */
public class USSBaseSiteTest {

    /** A 1x1x4 test base: controller + 3 frames, two power cells + one solar panel on the frames. */
    private static VoidcraftBlueprint testBase() {
        byte[] grid = { (byte) VoidcraftComponent.CONTROLLER.toGridValue(),
            (byte) VoidcraftComponent.FRAME.toGridValue(), (byte) VoidcraftComponent.FRAME.toGridValue(),
            (byte) VoidcraftComponent.FRAME.toGridValue(), };
        byte[] covers = new byte[4 * 6];
        covers[1 * 6 + 0] = (byte) VoidcraftCoverComponent.POWER_CELL.toGridValue();
        covers[1 * 6 + 1] = (byte) VoidcraftCoverComponent.POWER_CELL.toGridValue();
        covers[2 * 6 + 0] = (byte) VoidcraftCoverComponent.SOLAR_PANEL.toGridValue();
        return VoidcraftBlueprint.ofBase(1, 1, 4, grid, null, covers);
    }

    @Test
    public void testCreateInitializesPartsFromBlueprint() {
        USSBaseSite site = USSBaseSite.create(USSBaseAnchor.planet(1), "Test Station", testBase(), 1234L);
        // partsList counts: controller x1, frame x3, power cell x2, solar x1
        assertEquals(1L, site.required("block.CONTROLLER"));
        assertEquals(3L, site.required("block.FRAME"));
        assertEquals(2L, site.required("cover.POWER_CELL"));
        assertEquals(1L, site.required("cover.SOLAR_PANEL"));
        assertEquals(7L, site.totalRequired());
        assertEquals(0L, site.totalReceived());
        assertFalse(site.isComplete());
        assertEquals(0.0, site.progressFraction(), 1e-9);
        assertEquals(USSBaseAnchor.planet(1), site.anchor());
        assertEquals("Test Station", site.name());
        assertEquals(1234L, site.createdAt());
    }

    @Test
    public void testPartialFillAndOverflowDiscarded() {
        USSBaseSite site = USSBaseSite.create(USSBaseAnchor.star(), "B", testBase(), 1L);
        // Over-delivering a part credits only up to the required count (the excess is consumed, not stored)
        assertEquals(3L, site.add("block.FRAME", 3), "all three frames credited");
        assertEquals(0L, site.add("block.FRAME", 5), "frames already complete - excess discarded");
        assertEquals(2L, site.add("cover.POWER_CELL", 5), "only the required two power cells credited");
        assertEquals(3L, site.received("block.FRAME"));
        assertEquals(2L, site.received("cover.POWER_CELL"));
        assertEquals(0L, site.remaining("block.FRAME"));
        assertEquals(1L, site.remaining("block.CONTROLLER"));
        assertEquals(0L, site.add("cover.NONEXISTENT", 9), "unknown keys credit nothing");
        assertEquals(0L, site.add("block.CONTROLLER", 0), "zero delivery credits nothing");
        assertFalse(site.isComplete());
        assertEquals(5L, site.totalReceived());
        assertEquals(5.0 / 7.0, site.progressFraction(), 1e-9);
        // Complete it: the controller + the solar panel
        assertEquals(1L, site.add("block.CONTROLLER", 1));
        assertEquals(1L, site.add("cover.SOLAR_PANEL", 1));
        assertTrue(site.isComplete());
        assertEquals(1.0, site.progressFraction(), 1e-9);
        assertEquals(0L, site.remaining("cover.SOLAR_PANEL"));
    }

    @Test
    public void testNbtRoundTrip() {
        USSBaseSite site = USSBaseSite.create(USSBaseAnchor.ripple(9), "Ripple Station", testBase(), 987L);
        site.add("block.FRAME", 2);
        site.add("cover.POWER_CELL", 1);
        NBTTagCompound nbt = new NBTTagCompound();
        site.writeToNBT(nbt);
        USSBaseSite back = USSBaseSite.readFromNBT(nbt);
        assertTrue(back != null, "the site round-trips");
        assertEquals(site.anchor(), back.anchor());
        assertEquals(site.name(), back.name());
        assertEquals(site.createdAt(), back.createdAt());
        assertEquals(site.totalReceived(), back.totalReceived());
        assertEquals(site.totalRequired(), back.totalRequired());
        assertEquals(site.progressFraction(), back.progressFraction(), 1e-9);
        assertEquals(2L, back.received("block.FRAME"));
        assertEquals(1L, back.received("cover.POWER_CELL"));
        assertEquals(1L, back.remaining("block.FRAME"));
        assertFalse(back.isComplete());
        // The blueprint round-trips through the site payload (the full grid is preserved)
        assertEquals(site.blueprint(), back.blueprint());
        // The progress keeps working after a round-trip (the site is still mutable)
        assertEquals(1L, back.add("block.CONTROLLER", 1));
    }

    @Test
    public void testConstructLegArmsTicksDownAndRestarts() {
        USSBaseSite site = USSBaseSite.create(USSBaseAnchor.planet(3), "Leg Station", testBase(), 1L);
        assertEquals(0, site.constructLegId(), "no leg before the first start");
        assertEquals(0L, site.constructTicksLeft());
        site.startConstructLeg(200L, 20L, 42);
        assertEquals(1, site.constructLegId());
        assertEquals(200L, site.constructTotal());
        assertEquals(200L, site.constructTicksLeft());
        assertEquals(20L, site.constructTicksPerItem());
        assertEquals(42, site.constructSeed());
        // The countdown decrements by one machine tick and holds at 0 (a finished leg never goes negative).
        site.tickConstruct();
        assertEquals(199L, site.constructTicksLeft());
        // The deposit schedule: with total = ticksPerItem * parts, the countdown hits a multiple of the pacing
        // on every deposit tick - including the final 0 (20-tick pacing, 10 parts -> deposits at 180, 160, .., 0).
        long deposits = 0;
        while (site.constructTicksLeft() > 0L) {
            site.tickConstruct();
            if (site.constructTicksLeft() % site.constructTicksPerItem() == 0L) {
                deposits++;
            }
        }
        assertEquals(10L, deposits, "exactly parts deposits run over the leg");
        site.tickConstruct(); // past zero
        assertEquals(0L, site.constructTicksLeft(), "the countdown holds at 0");
        // A new leg re-arms the countdown and bumps the leg id (the client's phase key).
        site.startConstructLeg(40L, 10L, 7);
        assertEquals(2, site.constructLegId());
        assertEquals(40L, site.constructTicksLeft());
        assertEquals(7, site.constructSeed());
        // Degenerate totals clamp to a one-tick leg (never zero - a zero duration would never end).
        site.startConstructLeg(0L, 0L, 9);
        assertEquals(3, site.constructLegId());
        assertEquals(1L, site.constructTotal());
        assertEquals(1L, site.constructTicksLeft());
        assertEquals(1L, site.constructTicksPerItem());
        // finishConstructLeg zeroes the pacing (the client draws no beam) but keeps the id.
        site.finishConstructLeg();
        assertEquals(3, site.constructLegId());
        assertEquals(0L, site.constructTotal());
        assertEquals(0L, site.constructTicksLeft());
        assertEquals(0L, site.constructTicksPerItem());
        assertEquals(0, site.constructSeed());
    }

    @Test
    public void testConstructLegSurvivesNbtRoundTrip() {
        USSBaseSite site = USSBaseSite.create(USSBaseAnchor.star(), "Persisted Leg", testBase(), 1L);
        site.add("block.FRAME", 2);
        site.startConstructLeg(100L, 25L, 99);
        site.tickConstruct();
        site.tickConstruct();
        NBTTagCompound nbt = new NBTTagCompound();
        site.writeToNBT(nbt);
        USSBaseSite back = USSBaseSite.readFromNBT(nbt);
        assertTrue(back != null, "the site round-trips with an active leg");
        assertEquals(1, back.constructLegId());
        assertEquals(100L, back.constructTotal());
        assertEquals(98L, back.constructTicksLeft(), "the in-flight countdown resumes");
        assertEquals(25L, back.constructTicksPerItem());
        assertEquals(99, back.constructSeed());
        // A site with no leg writes no leg tags (absent = never constructed).
        USSBaseSite plain = USSBaseSite.create(USSBaseAnchor.star(), "No Leg", testBase(), 1L);
        NBTTagCompound nbt2 = new NBTTagCompound();
        plain.writeToNBT(nbt2);
        assertFalse(nbt2.hasKey("vc_site_construct_leg"));
        USSBaseSite back2 = USSBaseSite.readFromNBT(nbt2);
        assertTrue(back2 != null);
        assertEquals(0, back2.constructLegId());
        // A corrupt leg (the id present but the totals missing) degrades to NO leg - the parts survive.
        nbt.removeTag("vc_site_construct_total");
        nbt.removeTag("vc_site_construct_per_item");
        USSBaseSite back3 = USSBaseSite.readFromNBT(nbt);
        assertTrue(back3 != null, "the parts progress survives a corrupt leg");
        assertEquals(0, back3.constructLegId());
        assertEquals(2L, back3.received("block.FRAME"));
        // A corrupt leg (a non-positive total) degrades the same way.
        NBTTagCompound nbt4 = new NBTTagCompound();
        site.writeToNBT(nbt4);
        nbt4.setLong("vc_site_construct_total", 0L);
        USSBaseSite back4 = USSBaseSite.readFromNBT(nbt4);
        assertTrue(back4 != null);
        assertEquals(0, back4.constructLegId());
    }

    @Test
    public void testNbtRejectsCorruptTags() {
        assertNull(USSBaseSite.readFromNBT(null));
        assertNull(USSBaseSite.readFromNBT(new NBTTagCompound()), "missing payload/parts -> null");
        // A site whose parts list no longer matches the blueprint is rejected
        USSBaseSite site = USSBaseSite.create(USSBaseAnchor.star(), "C", testBase(), 1L);
        NBTTagCompound nbt = new NBTTagCompound();
        site.writeToNBT(nbt);
        net.minecraft.nbt.NBTTagList parts = nbt.getTagList("vc_site_parts", 10);
        nbt.removeTag("vc_site_parts");
        nbt.setTag("vc_site_parts", parts);
        // delete one part entry (drop the solar panel) - the count no longer matches the blueprint
        net.minecraft.nbt.NBTTagList trimmed = new net.minecraft.nbt.NBTTagList();
        for (int i = 0; i < parts.tagCount(); i++) {
            net.minecraft.nbt.NBTTagCompound part = parts.getCompoundTagAt(i);
            if (!part.getString("key")
                .equals("cover.SOLAR_PANEL")) {
                trimmed.appendTag(part);
            }
        }
        nbt.setTag("vc_site_parts", trimmed);
        assertNull(USSBaseSite.readFromNBT(nbt), "parts list must match the blueprint parts list exactly");
        // a received count above the required count is corrupt
        USSBaseSite site2 = USSBaseSite.create(USSBaseAnchor.star(), "D", testBase(), 1L);
        NBTTagCompound nbt2 = new NBTTagCompound();
        site2.writeToNBT(nbt2);
        net.minecraft.nbt.NBTTagList parts2 = nbt2.getTagList("vc_site_parts", 10);
        parts2.getCompoundTagAt(0)
            .setLong("received", 999L);
        assertNull(USSBaseSite.readFromNBT(nbt2), "received > required -> null");
    }
}
