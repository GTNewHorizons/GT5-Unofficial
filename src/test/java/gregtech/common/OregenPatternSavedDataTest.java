package gregtech.common;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;

import org.junit.jupiter.api.Test;

import gregtech.common.GTWorldgenerator.OregenPatternSavedData;

/**
 * Getting this wrong silently relocates every ore vein in a world, so the paths that can overwrite the stored pattern
 * are pinned here.
 */
class OregenPatternSavedDataTest {

    private static final String KEY = "oregenPattern";

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
