package gregtech.loaders.postload;

import net.minecraft.nbt.NBTTagCompound;

/**
 * The NBT surgery half of migrating a tool off {@code MetaGeneratedTool01}, kept apart from {@link PosteaTransformers}
 * so that it can be exercised without dragging in the item classes and their optional cross-mod interfaces.
 */
final class MetaToolStackMigration {

    private MetaToolStackMigration() {}

    static NBTTagCompound readToolStats(NBTTagCompound nbt) {
        return nbt.getCompoundTag("tag")
            .getCompoundTag("GT.ToolStats");
    }

    /** As {@link #rewriteToolStack(NBTTagCompound, int, boolean, String)}, for a tool with no mode key of its own. */
    static void rewriteToolStack(NBTTagCompound nbt, int newMeta, boolean electric) {
        rewriteToolStack(nbt, newMeta, electric, null);
    }

    /**
     * Rewrites a serialized old-style tool stack in place: the metadata becomes the material, and the
     * {@code GT.ToolStats} compound is replaced by the handful of keys the new item actually reads. Does not touch the
     * stack's item id, which the caller sets.
     *
     * @param newMeta       the material's metadata on the new item.
     * @param electric      whether the target is an electric tool, which stores energy instead of durability.
     * @param legacyModeKey an extra key inside {@code GT.ToolStats} that held this tool's mode, or null. The
     *                      Prospector's Scanners kept theirs under a name of their own.
     */
    static void rewriteToolStack(NBTTagCompound nbt, int newMeta, boolean electric, String legacyModeKey) {
        final NBTTagCompound tag = nbt.getCompoundTag("tag");
        final NBTTagCompound toolStats = tag.getCompoundTag("GT.ToolStats");

        final NBTTagCompound newTag = new NBTTagCompound();
        // The material grants the same enchantments either way, so carrying them over saves recomputing them.
        if (tag.hasKey("ench")) newTag.setTag("ench", tag.getTag("ench"));
        final long mode = legacyModeKey == null ? toolStats.getByte("Mode") : toolStats.getLong(legacyModeKey);
        if (mode != 0) newTag.setInteger("GT.ToolMode", (int) mode);

        if (electric) {
            // Electric tools no longer wear out, so any stored durability damage is simply dropped.
            // Capacity is the tier's now, whatever battery built the tool, so the old MaxCharge is dropped and a
            // tool crafted with a cheaper battery comes across holding its tier's full capacity.
            final long charge = tag.getLong("GT.ItemCharge");
            if (charge > 0) newTag.setLong("GT.ItemCharge", charge);
        } else {
            // The old item counted durability in hundredths of a point, the new one in whole points, and the maximum
            // is the same number of points either way, so a hundredth of the old figure is the same share of the bar.
            final long damage = toolStats.getLong("Damage") / 100L;
            if (damage > 0) newTag.setLong("GT.ToolDamage", damage);
        }

        nbt.setShort("Damage", (short) newMeta);
        if (newTag.hasNoTags()) {
            nbt.removeTag("tag");
        } else {
            nbt.setTag("tag", newTag);
        }
    }
}
