package gregtech.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldInfo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import gregtech.common.GTWorldgenerator.OregenPattern;
import gregtech.common.GTWorldgenerator.OregenPatternSavedData;
import gregtech.common.GTWorldgenerator.PatternSource;

/**
 * Getting this wrong silently relocates every ore vein in a world, so the paths that can overwrite the stored pattern
 * are pinned here.
 */
class OregenPatternSavedDataTest {

    private static final String KEY = "oregenPattern";

    /**
     * The pattern and its source are static, so hand them back the way a client that joined nothing would find them.
     */
    @AfterEach
    void clearSharedPatternState() {
        new OregenPatternSavedData("reset").onClientDisconnect(new ClientDisconnectionFromServerEvent(remoteManager()));
    }

    @Test
    void legacyOrdinalsMigrateToNames() {
        OregenPatternSavedData legacy = read(withByte(0));
        assertEquals("AXISSYMMETRICAL", written(legacy));
        assertTrue(legacy.isDirty(), "a migrated ordinal has to be rewritten by name");

        OregenPatternSavedData current = read(withByte(1));
        assertEquals("EQUAL_SPACING", written(current));
        assertTrue(current.isDirty());
    }

    @Test
    void outOfRangeOrdinalIsNotPersisted() {
        OregenPatternSavedData tooHigh = read(withByte(7));
        assertEquals(GTWorldgenerator.DEFAULT_PATTERN.name(), written(tooHigh));
        assertFalse(tooHigh.isDirty(), "a guess must never overwrite the stored pattern");

        // Clamping this one lands on a different pattern than the default, so the value alone catches a regression
        OregenPatternSavedData negative = read(withByte(-3));
        assertEquals(GTWorldgenerator.DEFAULT_PATTERN.name(), written(negative));
        assertFalse(negative.isDirty(), "a guess must never overwrite the stored pattern");
    }

    @Test
    void unknownNameIsNotPersisted() {
        OregenPatternSavedData data = read(withString("NOT_A_PATTERN"));
        assertEquals(GTWorldgenerator.DEFAULT_PATTERN.name(), written(data));
        assertFalse(data.isDirty(), "a guess must never overwrite the stored pattern");
    }

    @Test
    void knownNameIsKept() {
        OregenPatternSavedData data = read(withString("AXISSYMMETRICAL"));
        assertEquals("AXISSYMMETRICAL", written(data));
        assertFalse(data.isDirty(), "nothing changed, so there is nothing to write back");
    }

    @Test
    void missingKeyFallsBackToTheDefault() {
        OregenPatternSavedData data = read(new NBTTagCompound());
        assertEquals(GTWorldgenerator.DEFAULT_PATTERN.name(), written(data));
        assertFalse(data.isDirty());
    }

    @Test
    void existingWorldWithoutPatternUsesLegacyGridWithoutPersistingAGuess() {
        World world = mock(World.class);
        WorldInfo info = mock(WorldInfo.class);
        when(world.getWorldInfo()).thenReturn(info);
        when(info.getWorldTotalTime()).thenReturn(100L);
        world.mapStorage = new MapStorage(null);

        OregenPatternSavedData.loadData(world);

        assertEquals(OregenPattern.AXISSYMMETRICAL, GTWorldgenerator.getOregenPattern());
        assertEquals(PatternSource.UNVERIFIED, GTWorldgenerator.getOregenPatternSource());
        assertFalse(GTWorldgenerator.isOregenPatternVerified());
        assertTrue(GTWorldgenerator.isOreChunk(-1, 1));
        assertFalse(GTWorldgenerator.isOreChunk(-2, 1));
        assertFalse(world.mapStorage.loadData(OregenPatternSavedData.class, "GregTech_OregenPattern").isDirty());
    }

    @Test
    void newWorldWithoutPatternPersistsEqualSpacing() {
        World world = mock(World.class);
        WorldInfo info = mock(WorldInfo.class);
        when(world.getWorldInfo()).thenReturn(info);
        when(info.getWorldTotalTime()).thenReturn(0L);
        world.mapStorage = new MapStorage(null);

        OregenPatternSavedData.loadData(world);

        assertEquals(OregenPattern.EQUAL_SPACING, GTWorldgenerator.getOregenPattern());
        assertEquals(PatternSource.NEW_WORLD, GTWorldgenerator.getOregenPatternSource());
        assertTrue(GTWorldgenerator.isOregenPatternVerified());
        assertTrue(world.mapStorage.loadData(OregenPatternSavedData.class, "GregTech_OregenPattern").isDirty());
    }

    @Test
    void localDisconnectPreservesWorldgenUntilConnectingToARemoteServer() {
        World world = mock(World.class);
        world.mapStorage = new MapStorage(null);
        OregenPatternSavedData data = read(withByte(0));
        world.mapStorage.setData("GregTech_OregenPattern", data);
        OregenPatternSavedData.loadData(world);

        NetworkManager localConnection = mock(NetworkManager.class);
        when(localConnection.isLocalChannel()).thenReturn(true);
        data.onClientConnect(new ClientConnectedToServerEvent(localConnection, "MODDED"));
        assertEquals(OregenPattern.AXISSYMMETRICAL, GTWorldgenerator.getOregenPattern());
        assertTrue(GTWorldgenerator.isOregenPatternResolved());

        data.onClientDisconnect(new ClientDisconnectionFromServerEvent(localConnection));
        OregenPatternSavedData.ensureLoaded(world);
        assertEquals(OregenPattern.AXISSYMMETRICAL, GTWorldgenerator.getOregenPattern());
        assertTrue(GTWorldgenerator.isOreChunk(-1, 1), "the server must keep its grid while finishing shutdown");

        data.onClientConnect(new ClientConnectedToServerEvent(remoteManager(), "MODDED"));
        assertEquals(GTWorldgenerator.DEFAULT_PATTERN, GTWorldgenerator.getOregenPattern());
        assertFalse(GTWorldgenerator.isOregenPatternResolved(), "the new server has not synced its pattern yet");
        assertEquals("AXISSYMMETRICAL", written(data), "client lifecycle events must not change the saved pattern");
    }

    /**
     * The bug this pins: MapStorage hands back the instance it already read, so a second load has to take the pattern
     * from that instance instead of guessing one again. EQUAL_SPACING on purpose, the old guess was AXISSYMMETRICAL
     * and a world storing that would pass even while broken.
     */
    @Test
    void loadingTwiceKeepsTheStoredPattern() {
        World world = mock(World.class);
        world.mapStorage = new MapStorage(null);
        world.mapStorage.setData("GregTech_OregenPattern", read(withString("EQUAL_SPACING")));

        OregenPatternSavedData.loadData(world);
        assertEquals(OregenPattern.EQUAL_SPACING, GTWorldgenerator.getOregenPattern());
        assertEquals(PatternSource.SAVED, GTWorldgenerator.getOregenPatternSource());

        OregenPatternSavedData.loadData(world);
        assertEquals(OregenPattern.EQUAL_SPACING, GTWorldgenerator.getOregenPattern());
        assertEquals(PatternSource.SAVED, GTWorldgenerator.getOregenPatternSource());
    }

    /**
     * A file this build cannot read has to survive untouched, otherwise upgrading and rolling back once loses the
     * pattern the same way the second saved data load used to.
     */
    @Test
    void aNewerFormatIsNeitherReadNorRewritten() {
        NBTTagCompound future = withString("AXISSYMMETRICAL");
        future.setInteger("version", 99);

        OregenPatternSavedData data = read(future);
        assertEquals(GTWorldgenerator.DEFAULT_PATTERN.name(), written(data), "an unreadable file must not be decoded");

        data.markDirty();
        assertFalse(data.isDirty(), "a file from a newer GregTech must never be written back");
    }

    private static NetworkManager remoteManager() {
        NetworkManager manager = mock(NetworkManager.class);
        when(manager.isLocalChannel()).thenReturn(false);
        return manager;
    }

    private static OregenPatternSavedData read(NBTTagCompound nbt) {
        OregenPatternSavedData data = new OregenPatternSavedData("test");
        data.readFromNBT(nbt);
        return data;
    }

    private static String written(OregenPatternSavedData data) {
        NBTTagCompound out = new NBTTagCompound();
        data.writeToNBT(out);
        return out.getString(KEY);
    }

    private static NBTTagCompound withByte(int value) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setByte(KEY, (byte) value);
        return nbt;
    }

    private static NBTTagCompound withString(String value) {
        NBTTagCompound nbt = new NBTTagCompound();
        nbt.setString(KEY, value);
        return nbt;
    }
}
