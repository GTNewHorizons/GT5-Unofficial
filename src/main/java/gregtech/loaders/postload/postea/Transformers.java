package gregtech.loaders.postload.postea;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.IntFunction;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.postea.api.BlockReplacementManager;
import com.gtnewhorizons.postea.api.IBlockTransformationHandler;
import com.gtnewhorizons.postea.api.IDExtenderCompat;
import com.gtnewhorizons.postea.api.IItemStackTransformationHandler;
import com.gtnewhorizons.postea.api.ItemStackReplacementManager;
import com.gtnewhorizons.postea.utility.BlockConversionInfo;

/// Each helper that takes a legacy registry name also claims the FML missing mapping of that name.
final class Transformers {

    private static final Set<String> claimedIds = new HashSet<>();

    private Transformers() {}

    static void replaceItem(String legacyId, ItemStack replacement) {
        claim(legacyId);
        ItemStackReplacementManager.addSimpleReplacement(legacyId, requireStack(legacyId, replacement));
    }

    static void replaceItem(String legacyId, int legacyMeta, ItemStack replacement) {
        claim(legacyId);
        requireStack(legacyId, replacement);
        ItemStackReplacementManager
            .addSimpleReplacement(legacyId, legacyMeta, replacement.getItem(), replacement.getItemDamage());
    }

    static void replaceBlock(String legacyId, Block replacement, int meta) {
        claim(legacyId);
        BlockReplacementManager.addSimpleReplacement(legacyId, replacement, meta);
    }

    static void replaceBlock(String legacyId, int legacyMeta, Block replacement, int meta) {
        claim(legacyId);
        BlockReplacementManager.addSimpleReplacement(legacyId, legacyMeta, replacement, meta);
    }

    /// Replaces the block with the block and metadata that `replacement` places.
    static void replaceBlock(String legacyId, int legacyMeta, ItemStack replacement) {
        requireStack(legacyId, replacement);
        replaceBlock(legacyId, legacyMeta, Block.getBlockFromItem(replacement.getItem()), replacement.getItemDamage());
    }

    static void transformItem(String legacyId, IItemStackTransformationHandler handler) {
        claim(legacyId);
        ItemStackReplacementManager.addTransformationHandler(legacyId, handler);
    }

    static void transformBlock(String legacyId, IBlockTransformationHandler handler) {
        claim(legacyId);
        BlockReplacementManager.addTransformationHandler(legacyId, handler);
    }

    /// Converts each saved stack into the stack that `target` returns for the stack's damage, keeping the stack size
    /// and NBT tag. When `target` returns null, the stack isn't changed.
    static void remapItem(String legacyId, IntFunction<ItemStack> target) {
        transformItem(legacyId, (originalId, tag) -> setStack(tag, target.apply(tag.getInteger("Damage"))));
    }

    /// Converts each placed block into the block of the stack that `target` returns for the block's metadata. When
    /// `target` returns null, the block isn't changed.
    static void remapBlock(String legacyId, IntFunction<ItemStack> target) {
        transformBlock(legacyId, info -> setBlock(info, target.apply(info.metadata)));
    }

    private static ItemStack requireStack(String legacyId, @Nullable ItemStack replacement) {
        return Objects.requireNonNull(replacement, () -> "No replacement stack for " + legacyId);
    }

    private static boolean setStack(NBTTagCompound tag, @Nullable ItemStack target) {
        if (target == null) return false;
        IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(target.getItem()));
        tag.setShort("Damage", (short) target.getItemDamage());
        return true;
    }

    private static boolean setBlock(BlockConversionInfo info, @Nullable ItemStack target) {
        if (target == null) return false;
        info.blockID = Block.getIdFromBlock(Block.getBlockFromItem(target.getItem()));
        info.metadata = target.getItemDamage();
        return true;
    }

    /// Without the claim FML discards the saved id before Postea's chunk and stack passes run.
    /// [BlockReplacementManager#ignoreMissingMapping] and [ItemStackReplacementManager#ignoreMissingMapping] feed one
    /// shared ignore set, so a single call covers both the block and the item leg of a name.
    private static void claim(String legacyId) {
        if (claimedIds.add(legacyId)) BlockReplacementManager.ignoreMissingMapping(legacyId);
    }

    static int countClaimedIds() {
        return claimedIds.size();
    }
}
