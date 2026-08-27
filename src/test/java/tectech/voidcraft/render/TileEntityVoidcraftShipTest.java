package tectech.voidcraft.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import org.junit.jupiter.api.Test;

import tectech.voidcraft.uss.USSPosition;

/**
 * Unit tests for the fleet anchor's Voidbase entries (Phase D, bare-JVM): the construction sites + the standing
 * bases round-trip through the tile entity's NBT (the description packet carries the same NBT).
 */
public class TileEntityVoidcraftShipTest {

    /**
     * {@link TileEntity#writeToNBT(NBTTagCompound)} looks the class up in the global class<->name map — register
     * the fleet anchor class (a test-only name) so the round trip works in a bare JVM.
     */
    static {
        TileEntity.addMapping(TileEntityVoidcraftShip.class, "voidcraft.test:fleetAnchor");
    }

    private static NBTTagCompound siteEntry(int target, boolean staticBody, double progress, int w, int h, int d) {
        NBTTagCompound entry = new NBTTagCompound();
        entry.setInteger(TileEntityVoidcraftShip.TAG_ENTRY_TARGET, target);
        if (staticBody) {
            entry.setBoolean(TileEntityVoidcraftShip.TAG_ENTRY_STATIC, true);
            NBTTagCompound dest = new NBTTagCompound();
            USSPosition.of(1.5, 2.5, 3.5)
                .writeToNBT(dest);
            entry.setTag(TileEntityVoidcraftShip.TAG_ENTRY_DEST, dest);
        }
        entry.setDouble(TileEntityVoidcraftShip.TAG_SITE_PROGRESS, progress);
        entry.setIntArray(TileEntityVoidcraftShip.TAG_SITE_DIMS, new int[] { w, h, d });
        return entry;
    }

    private static NBTTagCompound baseEntry(int target, boolean staticBody, long integrity, long maxIntegrity) {
        NBTTagCompound entry = new NBTTagCompound();
        entry.setInteger(TileEntityVoidcraftShip.TAG_ENTRY_TARGET, target);
        if (staticBody) {
            entry.setBoolean(TileEntityVoidcraftShip.TAG_ENTRY_STATIC, true);
            NBTTagCompound dest = new NBTTagCompound();
            USSPosition.of(-4.0, 1.0, 7.0)
                .writeToNBT(dest);
            entry.setTag(TileEntityVoidcraftShip.TAG_ENTRY_DEST, dest);
        }
        NBTTagCompound payload = new NBTTagCompound();
        payload.setString("vc_uuid", "base-uuid");
        entry.setTag(TileEntityVoidcraftShip.TAG_ENTRY_PAYLOAD, payload);
        entry.setLong(TileEntityVoidcraftShip.TAG_BASE_INTEGRITY, integrity);
        entry.setLong(TileEntityVoidcraftShip.TAG_BASE_INTEGRITY_MAX, maxIntegrity);
        return entry;
    }

    private static TileEntityVoidcraftShip readBack(TileEntityVoidcraftShip from) {
        NBTTagCompound nbt = new NBTTagCompound();
        from.writeToNBT(nbt);
        TileEntityVoidcraftShip to = new TileEntityVoidcraftShip();
        to.readFromNBT(nbt);
        return to;
    }

    @Test
    public void testSitesAndBasesRoundTrip() {
        TileEntityVoidcraftShip te = new TileEntityVoidcraftShip();
        List<NBTTagCompound> sites = Arrays.asList(
            siteEntry(3, false, 0.25, 15, 8, 10), // planet anchor, in progress
            siteEntry(-1, true, 0.9, 6, 6, 6)); // ripple anchor (static resolved point), nearly done
        List<NBTTagCompound> bases = Arrays.asList(
            baseEntry(0, false, 120, 300), // planet anchor, damaged
            baseEntry(-1, true, 500, 500)); // ripple anchor, full integrity
        te.setBaseSites(sites);
        te.setBases(bases);

        TileEntityVoidcraftShip back = readBack(te);
        assertEquals(
            2,
            back.getBaseSites()
                .size());
        assertEquals(
            2,
            back.getBases()
                .size());

        NBTTagCompound s0 = back.getBaseSites()
            .get(0);
        assertEquals(3, s0.getInteger(TileEntityVoidcraftShip.TAG_ENTRY_TARGET));
        assertEquals(false, s0.getBoolean(TileEntityVoidcraftShip.TAG_ENTRY_STATIC));
        assertEquals(0.25, s0.getDouble(TileEntityVoidcraftShip.TAG_SITE_PROGRESS), 1e-9);
        assertEquals(15, s0.getIntArray(TileEntityVoidcraftShip.TAG_SITE_DIMS)[0]);
        assertEquals(8, s0.getIntArray(TileEntityVoidcraftShip.TAG_SITE_DIMS)[1]);
        assertEquals(10, s0.getIntArray(TileEntityVoidcraftShip.TAG_SITE_DIMS)[2]);

        NBTTagCompound s1 = back.getBaseSites()
            .get(1);
        assertTrue(s1.getBoolean(TileEntityVoidcraftShip.TAG_ENTRY_STATIC));
        USSPosition dest1 = USSPosition.readFromNBT(s1.getCompoundTag(TileEntityVoidcraftShip.TAG_ENTRY_DEST));
        assertEquals(1.5, dest1.x(), 1e-9);
        assertEquals(2.5, dest1.y(), 1e-9);
        assertEquals(3.5, dest1.z(), 1e-9);
        assertEquals(0.9, s1.getDouble(TileEntityVoidcraftShip.TAG_SITE_PROGRESS), 1e-9);

        NBTTagCompound b0 = back.getBases()
            .get(0);
        assertEquals(0, b0.getInteger(TileEntityVoidcraftShip.TAG_ENTRY_TARGET));
        assertEquals(
            "base-uuid",
            b0.getCompoundTag(TileEntityVoidcraftShip.TAG_ENTRY_PAYLOAD)
                .getString("vc_uuid"));
        assertEquals(120, b0.getLong(TileEntityVoidcraftShip.TAG_BASE_INTEGRITY));
        assertEquals(300, b0.getLong(TileEntityVoidcraftShip.TAG_BASE_INTEGRITY_MAX));

        NBTTagCompound b1 = back.getBases()
            .get(1);
        assertTrue(b1.getBoolean(TileEntityVoidcraftShip.TAG_ENTRY_STATIC));
        USSPosition destB = USSPosition.readFromNBT(b1.getCompoundTag(TileEntityVoidcraftShip.TAG_ENTRY_DEST));
        assertEquals(-4.0, destB.x(), 1e-9);
        assertEquals(1.0, destB.y(), 1e-9);
        assertEquals(7.0, destB.z(), 1e-9);
        assertEquals(500, b1.getLong(TileEntityVoidcraftShip.TAG_BASE_INTEGRITY));
        assertEquals(500, b1.getLong(TileEntityVoidcraftShip.TAG_BASE_INTEGRITY_MAX));
    }

    @Test
    public void testEmptyRoundTrip() {
        TileEntityVoidcraftShip back = readBack(new TileEntityVoidcraftShip());
        assertTrue(
            back.getShips()
                .isEmpty());
        assertTrue(
            back.getBaseSites()
                .isEmpty());
        assertTrue(
            back.getBases()
                .isEmpty());
    }

    @Test
    public void testSettersSkipNullEntries() {
        TileEntityVoidcraftShip te = new TileEntityVoidcraftShip();
        te.setBaseSites(Arrays.asList(siteEntry(1, false, 0.5, 4, 4, 4), null));
        te.setBases(Arrays.asList(null, baseEntry(-1, false, 10, 10)));
        assertEquals(
            1,
            te.getBaseSites()
                .size());
        assertEquals(
            1,
            te.getBases()
                .size());
        assertEquals(
            -1,
            te.getBases()
                .get(0)
                .getInteger(TileEntityVoidcraftShip.TAG_ENTRY_TARGET));
    }

    @Test
    public void testSettersReplaceWholeLists() {
        TileEntityVoidcraftShip te = new TileEntityVoidcraftShip();
        te.setBaseSites(Arrays.asList(siteEntry(0, false, 0.1, 2, 2, 2), siteEntry(0, false, 0.2, 3, 3, 3)));
        te.setBaseSites(Arrays.asList(siteEntry(9, false, 0.7, 5, 5, 5)));
        assertEquals(
            1,
            te.getBaseSites()
                .size());
        assertEquals(
            9,
            te.getBaseSites()
                .get(0)
                .getInteger(TileEntityVoidcraftShip.TAG_ENTRY_TARGET));
        te.setBases(Arrays.asList(baseEntry(2, false, 1, 1)));
        te.setBases(null);
        assertTrue(
            te.getBases()
                .isEmpty());
    }
}
