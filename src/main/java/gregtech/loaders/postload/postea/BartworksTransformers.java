package gregtech.loaders.postload.postea;

import static gregtech.GTLoggers.GT_FML_LOGGER;
import static gregtech.api.enums.OrePrefixes.___placeholder___;
import static gregtech.loaders.postload.postea.Transformers.remapItem;
import static gregtech.loaders.postload.postea.Transformers.replaceItem;
import static gregtech.loaders.postload.postea.Transformers.transformBlock;
import static gregtech.loaders.postload.postea.Transformers.transformItem;

import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizons.postea.api.BlockReplacementManager;
import com.gtnewhorizons.postea.api.IDExtenderCompat;
import com.gtnewhorizons.postea.api.ItemStackReplacementManager;
import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.LegacyWerkstoffIndex;
import gregtech.api.material.MaterialParts;

final class BartworksTransformers {

    // Legacy bartworks werkstoff id offset (see LegacyWerkstoffIndex)
    private static final int OFFSET_ID_3 = 11_300;
    private static final int[] WERKSTOFFS_REMOVED_IN_2_9 = new int[] { OFFSET_ID_3, OFFSET_ID_3 + 2, OFFSET_ID_3 + 6,
        OFFSET_ID_3 + 7, OFFSET_ID_3 + 8, OFFSET_ID_3 + 11, OFFSET_ID_3 + 12 };

    /// One legacy bartworks meta item per prefix, registered as `bartworks:gt.bwMetaGenerated<prefix>`.
    private static final OrePrefixes[] LEGACY_WERKSTOFF_ITEM_PREFIXES = { OrePrefixes.dust, OrePrefixes.dustTiny,
        OrePrefixes.dustSmall, OrePrefixes.ingot, OrePrefixes.ingotHot, OrePrefixes.nugget, OrePrefixes.gem,
        OrePrefixes.gemChipped, OrePrefixes.gemExquisite, OrePrefixes.gemFlawed, OrePrefixes.gemFlawless,
        OrePrefixes.lens, OrePrefixes.crushed, OrePrefixes.crushedPurified, OrePrefixes.crushedCentrifuged,
        OrePrefixes.dustPure, OrePrefixes.dustImpure, OrePrefixes.rawOre, OrePrefixes.cell, OrePrefixes.cellPlasma,
        OrePrefixes.plate, OrePrefixes.foil, OrePrefixes.stick, OrePrefixes.stickLong, OrePrefixes.toolHeadWrench,
        OrePrefixes.toolHeadHammer, OrePrefixes.toolHeadSaw, OrePrefixes.turbineBlade, OrePrefixes.gearGt,
        OrePrefixes.gearGtSmall, OrePrefixes.bolt, OrePrefixes.screw, OrePrefixes.ring, OrePrefixes.spring,
        OrePrefixes.springSmall, OrePrefixes.rotor, OrePrefixes.wireFine, OrePrefixes.plateDouble,
        OrePrefixes.plateDense, OrePrefixes.plateSuperdense, OrePrefixes.plateTriple, OrePrefixes.plateQuadruple,
        OrePrefixes.plateQuintuple, OrePrefixes.cellMolten };

    private static int tieredGlassBlockId = -1;
    private static int extraGlassBlockId = -1;
    private static int tieredGlassItemId = -1;
    private static int extraGlassItemId = -1;

    private BartworksTransformers() {}

    static void register() {
        registerRemovedWerkstoffTransformers();
        registerBorosilicateGlassTransformers();
        registerBartworksLabPartTransformer();
        registerWerkstoffBlockCutoverTransformers();
        registerWerkstoffItemCutoverTransformers();
    }

    /// Stacks of the werkstoffs removed in 2.9 become dirt.
    private static void registerRemovedWerkstoffTransformers() {
        String placeholderName = ___placeholder___.getName();
        Arrays.stream(OrePrefixes.VALUES)
            .map(OrePrefixes::getName)
            .filter(prefix -> !placeholderName.equals(prefix))
            .map(prefix -> "bartworks:gt.bwMetaGenerated" + prefix)
            .forEach(BartworksTransformers::replaceRemovedWerkstoffs);
        replaceRemovedWerkstoffs("bartworks:bw.werkstoffblocks.01");
    }

    private static void replaceRemovedWerkstoffs(String legacyId) {
        for (int removedWerkstoff : WERKSTOFFS_REMOVED_IN_2_9) {
            replaceItem(legacyId, removedWerkstoff, new ItemStack(Blocks.dirt));
        }
    }

    private static void registerBorosilicateGlassTransformers() {
        ItemStackReplacementManager.registerIDResolver("bartworks:BW_TieredGlass", i -> tieredGlassItemId = i);
        ItemStackReplacementManager.registerIDResolver("bartworks:BW_ExtraGlass", i -> extraGlassItemId = i);
        BlockReplacementManager.registerIDResolver("bartworks:BW_TieredGlass", i -> tieredGlassBlockId = i);
        BlockReplacementManager.registerIDResolver("bartworks:BW_ExtraGlass", i -> extraGlassBlockId = i);

        transformBlock("bartworks:BW_GlasBlocks", info -> {
            // Normal through osmium glass metadata unchanged
            if (info.metadata >= 0 && info.metadata <= 5) {
                info.blockID = tieredGlassBlockId;
            }

            // Old colored glass transformed into non-colored glass
            if (info.metadata >= 6 && info.metadata <= 11) {
                info.blockID = tieredGlassBlockId;
                info.metadata = 0;
            }

            // Thorium-Yttrium Glass moved to its new place
            if (info.metadata == 12) {
                info.blockID = extraGlassBlockId;
                info.metadata = 0;
            }

            // Old neutronium glass moved to new neutronium glass
            if (info.metadata == 13) {
                info.blockID = tieredGlassBlockId;
                info.metadata = 6;
            }

            // Old cosmic glass turned into new infinity glass
            if (info.metadata == 14) {
                info.blockID = tieredGlassBlockId;
                info.metadata = 7;
            }

            // Old infinity glass turned into new transcendent glass
            if (info.metadata == 15) {
                info.blockID = tieredGlassBlockId;
                info.metadata = 8;
            }

            return true;
        });

        transformBlock("bartworks:BW_GlasBlocks2", info -> {
            // Turn old transcendent glass into new hexanite glass
            if (info.metadata == 0) {
                info.blockID = tieredGlassBlockId;
                info.metadata = 9;
                return true;
            }
            return false;
        });

        transformItem("bartworks:BW_GlasBlocks", (name, nbt) -> {
            int metadata = nbt.getShort("Damage");

            // Normal through osmium glass metadata unchanged
            if (metadata >= 0 && metadata <= 5) {
                IDExtenderCompat.setItemStackID(nbt, tieredGlassItemId);
            }

            // Old colored glass transformed into non-colored glass
            if (metadata >= 6 && metadata <= 11) {
                IDExtenderCompat.setItemStackID(nbt, tieredGlassItemId);
                nbt.setShort("Damage", (short) 0);
            }

            // Thorium-Yttrium Glass moved to its new place
            if (metadata == 12) {
                IDExtenderCompat.setItemStackID(nbt, extraGlassItemId);
                nbt.setShort("Damage", (short) 0);
            }

            // Old neutronium glass moved to new neutronium glass
            if (metadata == 13) {
                IDExtenderCompat.setItemStackID(nbt, tieredGlassItemId);
                nbt.setShort("Damage", (short) 6);
            }

            // Old cosmic glass turned into new infinity glass
            if (metadata == 14) {
                IDExtenderCompat.setItemStackID(nbt, tieredGlassItemId);
                nbt.setShort("Damage", (short) 7);
            }

            // Old infinity glass turned into new transcendent glass
            if (metadata == 15) {
                IDExtenderCompat.setItemStackID(nbt, tieredGlassItemId);
                nbt.setShort("Damage", (short) 8);
            }

            return true;
        });

        transformItem("bartworks:BW_GlasBlocks2", (name, nbt) -> {
            // Turn old transcendent glass into new hexanite glass
            if (nbt.getShort("Damage") == 0) {
                IDExtenderCompat.setItemStackID(nbt, tieredGlassItemId);
                nbt.setShort("Damage", (short) 9);
                return true;
            }
            return false;
        });
    }

    // sounds stupid, but this needs to persist, so renamed items don't break janky bartworks & recipe map code
    private static void registerBartworksLabPartTransformer() {
        transformItem("bartworks:BioLabParts", (name, nbt) -> {
            if (nbt.hasKey("tag")) {
                var tag = nbt.getCompoundTag("tag");
                // skip special NEI recipe item for BioLab Clonal Cellular Synthesis
                if (tag.hasKey("NEI")) return false;
                tag.func_150296_c()
                    .removeIf(key -> !key.equals("Name"));
            }
            return true;
        });
    }

    private static void registerWerkstoffBlockCutoverTransformers() {
        registerWerkstoffBlockCutoverTransformer(
            "bw.werkstoffblockTE",
            "bartworks:bw.werkstoffblocks.01",
            OrePrefixes.block);
        registerWerkstoffBlockCutoverTransformer(
            "bw.werkstoffblockcasingTE",
            "bartworks:bw.werkstoffblockscasing.01",
            OrePrefixes.blockCasing);
        registerWerkstoffBlockCutoverTransformer(
            "bw.werkstoffblockscasingadvancedTE",
            "bartworks:bw.werkstoffblockscasingadvanced.01",
            OrePrefixes.blockCasingAdvanced);
    }

    /// A placed block has the werkstoff id in its tile entity (`m`) or, when saved after bartworks dropped that tile
    /// entity, in its block metadata. An item stack has it in `Damage`. A casing whose material has no MaterialLib
    /// casing becomes the material's storage block. A placed block of a werkstoff removed from the pack becomes dirt,
    /// and one whose id resolves to no block becomes air.
    ///
    /// Postea runs tile-entity transformers before block transformers, so the metadata handler only sees blocks that
    /// had no tile entity.
    private static void registerWerkstoffBlockCutoverTransformer(String teId, String itemId, OrePrefixes prefix) {
        TileEntityReplacementManager
            .tileEntityTransformer(teId, (tag, world, chunk) -> werkstoffBlockInfo(prefix, tag.getShort("m")));

        transformBlock(itemId, info -> {
            BlockInfo cutover = werkstoffBlockInfo(prefix, info.metadata);
            info.blockID = Block.getIdFromBlock(cutover.block);
            info.metadata = cutover.metadata;
            return true;
        });

        remapItem(itemId, damage -> werkstoffBlockStack(prefix, damage));
    }

    private static BlockInfo werkstoffBlockInfo(OrePrefixes prefix, int werkstoffId) {
        if (ArrayUtils.contains(WERKSTOFFS_REMOVED_IN_2_9, werkstoffId)) return new BlockInfo(Blocks.dirt, 0);
        ItemStack cutover = werkstoffBlockStack(prefix, werkstoffId);
        if (cutover == null) return new BlockInfo(Blocks.air, 0);
        return new BlockInfo(Block.getBlockFromItem(cutover.getItem()), cutover.getItemDamage());
    }

    private static @Nullable ItemStack werkstoffBlockStack(OrePrefixes prefix, int werkstoffId) {
        Material material = LegacyWerkstoffIndex.get(werkstoffId);
        if (material == null) return null;
        ItemStack cutover = MaterialParts.stack(prefix, material, 1);
        return cutover != null ? cutover : MaterialLibAPI.findStack(material, BlockShapes.block, 1);
    }

    /// `Damage` is the werkstoff id. Ids unknown to [LegacyWerkstoffIndex] (a third-party mod's own werkstoffs) pass
    /// through unchanged. Ores migrate through [OreTransformers], and storage blocks and casings through
    /// [#registerWerkstoffBlockCutoverTransformer].
    private static void registerWerkstoffItemCutoverTransformers() {
        for (OrePrefixes prefix : LEGACY_WERKSTOFF_ITEM_PREFIXES) {
            remapItem("bartworks:gt.bwMetaGenerated" + prefix.getName(), damage -> {
                Material material = LegacyWerkstoffIndex.get(damage);
                if (material == null) return null;
                return prefix == OrePrefixes.cell ? MaterialParts.cell(material, 1)
                    : MaterialParts.stack(prefix, material, 1);
            });
        }
        GT_FML_LOGGER.debug(
            "PosteaTransformers: registered werkstoff item transformers for {} legacy items",
            LEGACY_WERKSTOFF_ITEM_PREFIXES.length);
    }
}
