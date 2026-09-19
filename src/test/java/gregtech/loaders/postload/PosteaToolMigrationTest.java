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
 * The old item counted durability in hundredths of a point and the new one counts whole points, so a worn tool's
 * damage comes across divided by a hundred, which leaves it the same share of the bar.
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

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, false);

        assertEquals(STEEL_META, stack.getShort("Damage"), "metadata should become the material");
        assertEquals((byte) 1, stack.getByte("Count"), "stack size must be untouched");

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(256L, tag.getLong("GT.ToolDamage"), "durability damage should come across as whole points");
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

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, false);

        assertEquals(STEEL_META, stack.getShort("Damage"));
        assertFalse(
            stack.hasKey("tag"),
            "nothing worth keeping means no tag at all, which is what a fresh one looks like");
    }

    @Test
    void electricWrenchKeepsItsChargeAndDropsItsDurability() {
        // An LV wrench built with a Lithium battery: the tier default capacity, half charged, and somewhat worn.
        NBTTagCompound stack = oldTool(120, "Steel", 4_000L, 51_200L, (byte) 0, 50_000L, 100_000L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true);

        assertEquals(STEEL_META, stack.getShort("Damage"));

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(50_000L, tag.getLong("GT.ItemCharge"), "stored energy should carry over");
        assertFalse(tag.hasKey("GT.ToolDamage"), "electric wrenches no longer wear out");
        assertFalse(tag.hasKey("GT.MaxCharge"), "capacity is the tier's, so nothing is recorded on the stack");
    }

    @Test
    void electricWrenchBuiltWithACheaperBatteryComesAcrossAtItsTiersCapacity() {
        // The Sodium battery variant of the same LV wrench: half the capacity on the old item, its tier's full
        // capacity on the new one, since the battery no longer decides how much a tool holds.
        NBTTagCompound stack = oldTool(120, "Steel", 0L, 51_200L, (byte) 0, 10_000L, 50_000L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true);

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertFalse(tag.hasKey("GT.MaxCharge"), "capacity is the tier's, so nothing is recorded on the stack");
        assertEquals(10_000L, tag.getLong("GT.ItemCharge"), "stored energy still carries over");
    }

    @Test
    void dischargedElectricWrenchStillConverts() {
        // Metadata 121: the odd, "empty" twin of the LV wrench that setCharge() switched the stack to.
        NBTTagCompound stack = oldTool(121, "Steel", 0L, 51_200L, (byte) 1, null, 100_000L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true);

        assertEquals(STEEL_META, stack.getShort("Damage"));
        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(0L, tag.getLong("GT.ItemCharge"), "an empty wrench stores no charge key");
        assertEquals(1, tag.getInteger("GT.ToolMode"), "the selected mode should carry over");
    }

    @Test
    void softMalletKeepsItsDurabilityAndMode() {
        // A Wood soft mallet in deactivate mode, a quarter worn. Max durability is 100 * mDurability * 8.
        NBTTagCompound stack = oldTool(14, "Wood", 4_000L, 16_000L, (byte) 2, null, null);

        MetaToolStackMigration.rewriteToolStack(stack, WOOD_META, false);

        assertEquals(WOOD_META, stack.getShort("Damage"), "metadata should become the material");

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(40L, tag.getLong("GT.ToolDamage"), "durability damage should come across as whole points");
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

        MetaToolStackMigration.rewriteToolStack(stack, RUBBER_META, false);

        assertEquals(RUBBER_META, stack.getShort("Damage"));
        assertFalse(stack.hasKey("tag"), "a fresh mallet needs no tag at all");
    }

    @Test
    void handScrewdriverKeepsItsDurability() {
        // An Iron screwdriver, nearly worn out. Screwdrivers have no modes, so none is recorded.
        NBTTagCompound stack = oldTool(22, "Iron", 12_500L, 12_800L, (byte) 0, null, null);

        MetaToolStackMigration.rewriteToolStack(stack, IRON_META, false);

        assertEquals(IRON_META, stack.getShort("Damage"), "metadata should become the material");

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(125L, tag.getLong("GT.ToolDamage"), "durability damage should come across as whole points");
        assertFalse(tag.hasKey("GT.ToolStats"), "the old stats compound should be gone");
        assertFalse(tag.hasKey("GT.ItemCharge"), "a hand screwdriver holds no charge");
    }

    @Test
    void electricScrewdriverKeepsItsChargeAndDropsItsDurability() {
        // An MV screwdriver built with a Lithium battery: the tier default capacity, part charged, somewhat worn.
        NBTTagCompound stack = oldTool(152, "Steel", 3_000L, 51_200L, (byte) 0, 250_000L, 400_000L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true);

        assertEquals(STEEL_META, stack.getShort("Damage"));

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(250_000L, tag.getLong("GT.ItemCharge"), "stored energy should carry over");
        assertFalse(tag.hasKey("GT.ToolDamage"), "electric screwdrivers no longer wear out");
        assertFalse(tag.hasKey("GT.MaxCharge"), "capacity is the tier's, so nothing is recorded on the stack");
    }

    @Test
    void electricScrewdriverBuiltWithACheaperBatteryComesAcrossAtItsTiersCapacity() {
        // The Sodium battery variant of the same MV screwdriver: half the capacity on the old item, its tier's full
        // capacity on the new one.
        NBTTagCompound stack = oldTool(152, "Steel", 0L, 51_200L, (byte) 0, 20_000L, 200_000L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true);

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertFalse(tag.hasKey("GT.MaxCharge"), "capacity is the tier's, so nothing is recorded on the stack");
        assertEquals(20_000L, tag.getLong("GT.ItemCharge"));
    }

    @Test
    void handCrowbarKeepsItsDurability() {
        NBTTagCompound stack = oldTool(20, "Iron", 5_000L, 12_800L, (byte) 0, null, null);

        MetaToolStackMigration.rewriteToolStack(stack, IRON_META, false);

        assertEquals(IRON_META, stack.getShort("Damage"));
        assertEquals(
            50L,
            stack.getCompoundTag("tag")
                .getLong("GT.ToolDamage"));
    }

    @Test
    void electricWireCutterKeepsItsCharge() {
        // An HV wire cutter with the Cadmium battery, which used to mean a smaller capacity than the tier's.
        NBTTagCompound stack = oldTool(200, "Steel", 800L, 51_200L, (byte) 0, 900_000L, 1_200_000L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true);

        assertEquals(STEEL_META, stack.getShort("Damage"));
        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(900_000L, tag.getLong("GT.ItemCharge"));
        assertFalse(tag.hasKey("GT.MaxCharge"), "capacity is the tier's, so nothing is recorded on the stack");
        assertFalse(tag.hasKey("GT.ToolDamage"), "electric wire cutters no longer wear out");
    }

    @Test
    void handHardHammerKeepsItsDurability() {
        NBTTagCompound stack = oldTool(12, "Iron", 9_900L, 12_800L, (byte) 0, null, null);

        MetaToolStackMigration.rewriteToolStack(stack, IRON_META, false);

        assertEquals(IRON_META, stack.getShort("Damage"));
        assertEquals(
            99L,
            stack.getCompoundTag("tag")
                .getLong("GT.ToolDamage"));
    }

    @Test
    void electricFileKeepsItsCharge() {
        NBTTagCompound stack = oldTool(204, "Steel", 2_500L, 51_200L, (byte) 0, 123_456L, 400_000L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true);

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(123_456L, tag.getLong("GT.ItemCharge"));
        assertFalse(tag.hasKey("GT.MaxCharge"), "capacity is the tier's, so nothing is recorded on the stack");
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

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, false, "DetravData");

        assertEquals(STEEL_META, stack.getShort("Damage"));
        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(15L, tag.getLong("GT.ToolDamage"), "durability damage should come across as whole points");
        assertEquals(2, tag.getInteger("GT.ToolMode"), "the scanner mode should carry over");
    }

    /**
     * A rotor counts durability in the same hundredths of a point the old meta-item did, so unlike every other tool
     * its stored wear is copied across rather than divided down -- exactly, with no rounding either way.
     */
    @Test
    void turbineRotorCarriesItsWearAcrossExactly() {
        // A Steel small rotor: max durability 100 * 512 * 1, worn 12345 hundredths, which is not a round number of
        // whole points and does not need to be.
        NBTTagCompound stack = oldTool(170, "Steel", 12_345L, 51_200L, (byte) 0, null, null);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, false, null, true);

        assertEquals(STEEL_META, stack.getShort("Damage"), "metadata should become the material");

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(12_345L, tag.getLong("GT.ToolDamage"), "a rotor's wear should come across untouched");
        assertFalse(tag.hasKey("GT.ToolStats"), "the old stats compound should be gone");
    }

    @Test
    void hugeTurbineRotorCarriesItsWearAcrossExactly() {
        // The same rotor three sizes up, worn 20000 hundredths of its 100 * 256 * 4.
        NBTTagCompound stack = oldTool(176, "Iron", 20_000L, 102_400L, (byte) 0, null, null);

        MetaToolStackMigration.rewriteToolStack(stack, IRON_META, false, null, true);

        assertEquals(IRON_META, stack.getShort("Damage"));
        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(20_000L, tag.getLong("GT.ToolDamage"));
    }

    /**
     * The counterpart to the above: a tool that does count whole points still has its wear scaled down, and the
     * hundredths that do not make up a point are forgiven.
     */
    @Test
    void anOrdinaryToolStillHasItsWearScaledDown() {
        NBTTagCompound stack = oldTool(16, "Steel", 12_345L, 51_200L, (byte) 0, null, null);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, false);

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(123L, tag.getLong("GT.ToolDamage"), "a hundredth of the old figure is the same share of the bar");
    }

    @Test
    void freshTurbineRotorLosesItsTagEntirely() {
        NBTTagCompound stack = oldTool(174, "Steel", 0L, 153_600L, (byte) 0, null, null);
        stack.getCompoundTag("tag")
            .removeTag("ench");

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, false, null, true);

        assertEquals(STEEL_META, stack.getShort("Damage"));
        assertFalse(stack.hasKey("tag"), "an unused rotor needs no tag at all");
    }

    @Test
    void electricProspectorKeepsItsChargeAndScannerMode() {
        // A LuV electric scanner at its default capacity, part charged, set to pollution.
        NBTTagCompound stack = oldTool(100, "Iridium", 900L, 51_200L, (byte) 0, 50_000_000L, 102_400_000L);
        stack.getCompoundTag("tag")
            .getCompoundTag("GT.ToolStats")
            .setLong("DetravData", 3L);

        MetaToolStackMigration.rewriteToolStack(stack, STEEL_META, true, "DetravData");

        NBTTagCompound tag = stack.getCompoundTag("tag");
        assertEquals(50_000_000L, tag.getLong("GT.ItemCharge"), "stored energy should carry over");
        assertEquals(3, tag.getInteger("GT.ToolMode"), "the scanner mode should carry over");
        assertFalse(tag.hasKey("GT.ToolDamage"), "electric scanners no longer wear out");
    }
}
