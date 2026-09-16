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

    /**
     * Rewrites a serialized old-style tool stack in place: the metadata becomes the material, and the
     * {@code GT.ToolStats} compound is replaced by the handful of keys the new item actually reads. Does not touch the
     * stack's item id, which the caller sets.
     *
     * @param newMeta          the material's metadata on the new item.
     * @param electric         whether the target is an electric tool, which stores energy instead of durability.
     * @param defaultMaxCharge the electric target's default capacity; a stack that held less keeps its own value.
     */
    static void rewriteToolStack(NBTTagCompound nbt, int newMeta, boolean electric, long defaultMaxCharge) {
        final NBTTagCompound tag = nbt.getCompoundTag("tag");
        final NBTTagCompound toolStats = tag.getCompoundTag("GT.ToolStats");

        final NBTTagCompound newTag = new NBTTagCompound();
        // The material grants the same enchantments either way, so carrying them over saves recomputing them.
        if (tag.hasKey("ench")) newTag.setTag("ench", tag.getTag("ench"));
        final byte mode = toolStats.getByte("Mode");
        if (mode != 0) newTag.setInteger("GT.ToolMode", mode);

        if (electric) {
            // Electric tools no longer wear out, so any stored durability damage is simply dropped.
            final long charge = tag.getLong("GT.ItemCharge");
            if (charge > 0) newTag.setLong("GT.ItemCharge", charge);
            final long maxCharge = toolStats.getLong("MaxCharge");
            if (maxCharge > 0 && maxCharge != defaultMaxCharge) newTag.setLong("GT.MaxCharge", maxCharge);
        } else {
            // Max durability is computed from the material and the formula is unchanged, so damage carries over as is.
            final long damage = toolStats.getLong("Damage");
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
