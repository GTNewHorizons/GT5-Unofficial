package gregtech.loaders.postload;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import org.junit.jupiter.api.Test;

/**
 * Drives {@link MetaToolStackMigration#rewriteToolStack} over serialized stacks shaped exactly the way
 * {@code MetaGeneratedTool.getToolWithStats()} wrote them, and checks what comes out the other side.
 * <p/>
 * Covers the shapes that differ: a worn hand tool, whose durability has to survive; a part-charged electric tool,
 * whose energy has to survive while its durability is deliberately dropped; and a soft mallet, whose mode is the
 * thing a player would most notice losing.
 */
class PosteaToolMigrationTest {

    /** Sub ids of the materials used below; the values themselves only have to land in the Damage field. */
    private static final int STEEL_META = 305;

    private static final int WOOD_META = 809;

    private static final int RUBBER_META = 880;

    private static final int IRON_META = 32;

    /**
     * Builds the NBT a tool of the old system serialized to.
     *
     * @param toolMeta the old tool-type metadata, e.g. 16 for the hand wrench or 14 for the soft mallet.
     */
    private static NBTTagCompound oldTool(int toolMeta, String material, long damage, long maxDamage, byte mode,
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
        NBTTagCompound stack = oldTool(16, "Steel", 25_600L, 51_200L, (byte) 2, null, null);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, false, 0L);

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
        NBTTagCompound stack = oldTool(16, "Steel", 0L, 51_200L, (byte) 0, null, null);
        stack.getCompoundTag("tag")
            .removeTag("ench");

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, false, 0L);

        assertEquals(STEEL_META, stack.getShort("Damage"));
        assertFalse(
            stack.hasKey("tag"),
            "nothing worth keeping means no tag at all, which is what a fresh one looks like");
    }

    @Test
    void electricWrenchKeepsItsChargeAndDropsItsDurability() {
        // An LV wrench built with a Lithium battery: the tier default capacity, half charged, and somewhat worn.
        NBTTagCompound stack = oldTool(120, "Steel", 4_000L, 51_200L, (byte) 0, 50_000L, 100_000L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true, 100_000L);

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
        NBTTagCompound stack = oldTool(120, "Steel", 0L, 51_200L, (byte) 0, 10_000L, 50_000L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true, 100_000L);

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(50_000L, tag.getLong("GT.MaxCharge"), "the smaller capacity must be recorded on the stack");
        assertEquals(10_000L, tag.getLong("GT.ItemCharge"));
    }

    @Test
    void dischargedElectricWrenchStillConverts() {
        // Metadata 121: the odd, "empty" twin of the LV wrench that setCharge() switched the stack to.
        NBTTagCompound stack = oldTool(121, "Steel", 0L, 51_200L, (byte) 1, null, 100_000L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true, 100_000L);

        assertEquals(STEEL_META, stack.getShort("Damage"));
        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(0L, tag.getLong("GT.ItemCharge"), "an empty wrench stores no charge key");
        assertEquals(1, tag.getInteger("GT.ToolMode"), "the selected mode should carry over");
    }

    @Test
    void softMalletKeepsItsDurabilityAndMode() {
        // A Wood soft mallet in deactivate mode, a quarter worn. Max durability is 100 * mDurability * 8.
        NBTTagCompound stack = oldTool(14, "Wood", 4_000L, 16_000L, (byte) 2, null, null);

        MetaToolStackMigration.rewriteToolStack(stack, WOOD_META, false, 0L);

        assertEquals(WOOD_META, stack.getShort("Damage"), "metadata should become the material");

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(4_000L, tag.getLong("GT.ToolDamage"), "durability damage should carry over unscaled");
        assertEquals(
            2,
            tag.getInteger("GT.ToolMode"),
            "deactivate mode should survive: a mallet swept along a row of machines relies on it");
        assertFalse(tag.hasKey("GT.ToolStats"), "the old stats compound should be gone");
        assertFalse(tag.hasKey("GT.ItemCharge"), "there is no electric soft mallet");
    }

    @Test
    void softMalletOddTwinConverts() {
        // Metadata 15: the odd id addTool() reserved next to every tool type, even a tool that is never electric.
        NBTTagCompound stack = oldTool(15, "Rubber", 0L, 16_000L, (byte) 0, null, null);
        stack.getCompoundTag("tag")
            .removeTag("ench");

        MetaToolStackMigration.rewriteToolStack(stack, RUBBER_META, false, 0L);

        assertEquals(RUBBER_META, stack.getShort("Damage"));
        assertFalse(stack.hasKey("tag"), "a fresh mallet needs no tag at all");
    }

    @Test
    void handScrewdriverKeepsItsDurability() {
        // An Iron screwdriver, nearly worn out. Screwdrivers have no modes, so none is recorded.
        NBTTagCompound stack = oldTool(22, "Iron", 12_500L, 12_800L, (byte) 0, null, null);

        MetaToolStackMigration.rewriteToolStack(stack, IRON_META, false, 0L);

        assertEquals(IRON_META, stack.getShort("Damage"), "metadata should become the material");

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(12_500L, tag.getLong("GT.ToolDamage"), "durability damage should carry over unscaled");
        assertFalse(tag.hasKey("GT.ToolStats"), "the old stats compound should be gone");
        assertFalse(tag.hasKey("GT.ItemCharge"), "a hand screwdriver holds no charge");
    }

    @Test
    void electricScrewdriverKeepsItsChargeAndDropsItsDurability() {
        // An MV screwdriver built with a Lithium battery: the tier default capacity, part charged, somewhat worn.
        NBTTagCompound stack = oldTool(152, "Steel", 3_000L, 51_200L, (byte) 0, 250_000L, 400_000L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true, 400_000L);

        assertEquals(STEEL_META, stack.getShort("Damage"));

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(250_000L, tag.getLong("GT.ItemCharge"), "stored energy should carry over");
        assertFalse(tag.hasKey("GT.ToolDamage"), "electric screwdrivers no longer wear out");
        assertFalse(
            tag.hasKey("GT.MaxCharge"),
            "a screwdriver at its tier's default capacity should not carry a capacity override");
    }

    @Test
    void electricScrewdriverBelowDefaultCapacityRecordsIt() {
        // The Sodium battery variant of the same MV screwdriver: half the capacity, so the stack has to remember it.
        NBTTagCompound stack = oldTool(152, "Steel", 0L, 51_200L, (byte) 0, 20_000L, 200_000L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true, 400_000L);

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(200_000L, tag.getLong("GT.MaxCharge"), "the smaller capacity must be recorded on the stack");
        assertEquals(20_000L, tag.getLong("GT.ItemCharge"));
    }

    @Test
    void handCrowbarKeepsItsDurability() {
        NBTTagCompound stack = oldTool(20, "Iron", 5_000L, 12_800L, (byte) 0, null, null);

        MetaToolStackMigration.rewriteToolStack(stack, IRON_META, false, 0L);

        assertEquals(IRON_META, stack.getShort("Damage"));
        assertEquals(
            5_000L,
            stack.getCompoundTag("tag")
                .getLong("GT.ToolDamage"));
    }

    @Test
    void electricWireCutterKeepsItsCharge() {
        // An HV wire cutter with the Cadmium battery: below its tier's default capacity, so that has to be recorded.
        NBTTagCompound stack = oldTool(200, "Steel", 800L, 51_200L, (byte) 0, 900_000L, 1_200_000L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true, 1_600_000L);

        assertEquals(STEEL_META, stack.getShort("Damage"));
        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(900_000L, tag.getLong("GT.ItemCharge"));
        assertEquals(1_200_000L, tag.getLong("GT.MaxCharge"));
        assertFalse(tag.hasKey("GT.ToolDamage"), "electric wire cutters no longer wear out");
    }

    @Test
    void handHardHammerKeepsItsDurability() {
        NBTTagCompound stack = oldTool(12, "Iron", 9_900L, 12_800L, (byte) 0, null, null);

        MetaToolStackMigration.rewriteToolStack(stack, IRON_META, false, 0L);

        assertEquals(IRON_META, stack.getShort("Damage"));
        assertEquals(
            9_900L,
            stack.getCompoundTag("tag")
                .getLong("GT.ToolDamage"));
    }

    @Test
    void electricFileKeepsItsCharge() {
        NBTTagCompound stack = oldTool(204, "Steel", 2_500L, 51_200L, (byte) 0, 123_456L, 400_000L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true, 400_000L);

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(123_456L, tag.getLong("GT.ItemCharge"));
        assertFalse(tag.hasKey("GT.MaxCharge"), "this one is at its tier's default capacity");
        assertFalse(tag.hasKey("GT.ToolDamage"), "electric files no longer wear out");
    }

    /**
     * The Prospector's Scanners kept their mode under a key of their own inside the stats compound, so the migration
     * is told which key to read. A scanner left set to "underground fluids" should come back set to it.
     */
    @Test
    void handProspectorKeepsItsScannerMode() {
        NBTTagCompound stack = oldTool(2, "Steel", 1_500L, 51_200L, (byte) 0, null, null);
        stack.getCompoundTag("tag")
            .getCompoundTag("GT.ToolStats")
            .setLong("DetravData", 2L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, false, 0L, "DetravData");

        assertEquals(STEEL_META, stack.getShort("Damage"));
        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(1_500L, tag.getLong("GT.ToolDamage"), "durability damage should carry over unscaled");
        assertEquals(2, tag.getInteger("GT.ToolMode"), "the scanner mode should carry over");
    }

    @Test
    void electricProspectorKeepsItsChargeAndScannerMode() {
        // A LuV electric scanner at its default capacity, part charged, set to pollution.
        NBTTagCompound stack = oldTool(100, "Iridium", 900L, 51_200L, (byte) 0, 50_000_000L, 102_400_000L);
        stack.getCompoundTag("tag")
            .getCompoundTag("GT.ToolStats")
            .setLong("DetravData", 3L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true, 102_400_000L, "DetravData");

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(50_000_000L, tag.getLong("GT.ItemCharge"), "stored energy should carry over");
        assertEquals(3, tag.getInteger("GT.ToolMode"), "the scanner mode should carry over");
        assertFalse(tag.hasKey("GT.ToolDamage"), "electric scanners no longer wear out");
    }
}
