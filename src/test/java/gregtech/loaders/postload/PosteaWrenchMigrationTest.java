package gregtech.loaders.postload;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.Test;

/**
 * Drives {@link WrenchStackMigration#rewriteWrenchStack} over serialized stacks shaped exactly the way
 * {@code MetaGeneratedTool.getToolWithStats()} wrote them, and checks what comes out the other side.
 * <p/>
 * Covers the two shapes that differ: a worn hand wrench, whose durability has to survive, and a part-charged
 * electric wrench, whose energy has to survive while its durability is deliberately dropped.
 */
class PosteaWrenchMigrationTest {

    /** Steel's sub id; the value itself does not matter here, only that it lands in the Damage field. */
    private static final int STEEL_META = 305;

    /**
     * Builds the NBT a wrench of the old system serialized to.
     *
     * @param toolMeta the old tool-type metadata, e.g. 16 for the hand wrench.
     */
    private static NBTTagCompound oldWrench(int toolMeta, String material, long damage, long maxDamage, byte mode,
        Long charge, Long maxCharge) {
        NBTTagCompound toolStats = new NBTTagCompound();
        toolStats.setByte("Mode", mode);
        toolStats.setString("PrimaryMaterial", material);
        toolStats.setString("SecondaryMaterial", material);
        toolStats.setLong("MaxDamage", maxDamage);
        toolStats.setLong("Damage", damage);
        if (maxCharge != null) {
            toolStats.setBoolean("Electric", true);
            toolStats.setLong("MaxCharge", maxCharge);
            toolStats.setLong("Voltage", 32L);
            toolStats.setLong("Tier", 1L);
            toolStats.setLong("SpecialData", -1L);
        }

        NBTTagCompound tag = new NBTTagCompound();
        tag.setTag("GT.ToolStats", toolStats);
        if (charge != null) tag.setLong("GT.ItemCharge", charge);
        tag.setTag("ench", new NBTTagList());

        NBTTagCompound stack = new NBTTagCompound();
        stack.setShort("id", (short) 4096);
        stack.setByte("Count", (byte) 1);
        stack.setShort("Damage", (short) toolMeta);
        stack.setTag("tag", tag);
        return stack;
    }

    @Test
    void handWrenchKeepsItsDurabilityAndMode() {
        // A Steel hand wrench, half worn (max durability 100 * 512 = 51200 in the internal unit), in precise mode.
        NBTTagCompound stack = oldWrench(16, "Steel", 25_600L, 51_200L, (byte) 2, null, null);

        WrenchStackMigration.rewriteWrenchStack(stack, STEEL_META, false, 0L);

        assertEquals(STEEL_META, stack.getShort("Damage"), "metadata should become the material");
        assertEquals((byte) 1, stack.getByte("Count"), "stack size must be untouched");

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(25_600L, tag.getLong("GT.ToolDamage"), "durability damage should carry over unscaled");
        assertEquals(2, tag.getInteger("GT.ToolMode"), "the selected mode should carry over");
        assertTrue(tag.hasKey("ench"), "material enchantments should be kept");
        assertFalse(tag.hasKey("GT.ToolStats"), "the old stats compound should be gone");
        assertFalse(tag.hasKey("GT.ItemCharge"), "a hand wrench holds no charge");
    }

    @Test
    void undamagedHandWrenchLosesItsTagEntirely() {
        NBTTagCompound stack = oldWrench(16, "Steel", 0L, 51_200L, (byte) 0, null, null);
        stack.getCompoundTag("tag")
            .removeTag("ench");

        WrenchStackMigration.rewriteWrenchStack(stack, STEEL_META, false, 0L);

        assertEquals(STEEL_META, stack.getShort("Damage"));
        assertFalse(
            stack.hasKey("tag"),
            "nothing worth keeping means no tag at all, which is what a fresh one looks like");
    }

    @Test
    void electricWrenchKeepsItsChargeAndDropsItsDurability() {
        // An LV wrench built with a Lithium battery: the tier default capacity, half charged, and somewhat worn.
        NBTTagCompound stack = oldWrench(120, "Steel", 4_000L, 51_200L, (byte) 0, 50_000L, 100_000L);

        WrenchStackMigration.rewriteWrenchStack(stack, STEEL_META, true, 100_000L);

        assertEquals(STEEL_META, stack.getShort("Damage"));

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(50_000L, tag.getLong("GT.ItemCharge"), "stored energy should carry over");
        assertFalse(tag.hasKey("GT.ToolDamage"), "electric wrenches no longer wear out");
        assertFalse(
            tag.hasKey("GT.MaxCharge"),
            "a wrench at its tier's default capacity should not carry a capacity override");
    }

    @Test
    void electricWrenchBelowDefaultCapacityRecordsIt() {
        // The Sodium battery variant of the same LV wrench: half the capacity, so the stack has to remember it.
        NBTTagCompound stack = oldWrench(120, "Steel", 0L, 51_200L, (byte) 0, 10_000L, 50_000L);

        WrenchStackMigration.rewriteWrenchStack(stack, STEEL_META, true, 100_000L);

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(50_000L, tag.getLong("GT.MaxCharge"), "the smaller capacity must be recorded on the stack");
        assertEquals(10_000L, tag.getLong("GT.ItemCharge"));
    }

    @Test
    void dischargedElectricWrenchStillConverts() {
        // Metadata 121: the odd, "empty" twin of the LV wrench that setCharge() switched the stack to.
        NBTTagCompound stack = oldWrench(121, "Steel", 0L, 51_200L, (byte) 1, null, 100_000L);

        WrenchStackMigration.rewriteWrenchStack(stack, STEEL_META, true, 100_000L);

        assertEquals(STEEL_META, stack.getShort("Damage"));
        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(0L, tag.getLong("GT.ItemCharge"), "an empty wrench stores no charge key");
        assertEquals(1, tag.getInteger("GT.ToolMode"), "the selected mode should carry over");
    }
}
