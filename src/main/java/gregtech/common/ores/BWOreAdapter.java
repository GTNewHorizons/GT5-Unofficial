package gregtech.common.ores;

import java.util.EnumMap;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;
import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;
import com.gtnewhorizons.postea.api.BlockReplacementManager;
import com.gtnewhorizons.postea.api.IDExtenderCompat;
import com.gtnewhorizons.postea.api.ItemStackReplacementManager;
import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.BWItemMetaGeneratedOre;
import bartworks.system.material.BWMetaGeneratedOres;
import codechicken.nei.api.API;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials.LegacyWerkstoffIndex;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialData;
import gregtech.api.material.MaterialParts;

/// The legacy `bw.blockores*` ore blocks, kept registered so a saved world's placed and inventory blocks still
/// resolve. A legacy meta is a werkstoff id, decoded to its material through [LegacyWerkstoffIndex]; the
/// block's stone type and size come from which of the eight blocks it is.
///
/// [#init] constructs the eight [BWMetaGeneratedOres] instances (2 stone types x {big, small} x {natural,
/// non-natural}) under their original ids, and registers Postea transformers that convert each placed or
/// inventory block, as it loads, into the ore [GTOreAdapter] resolves for the same material, stone type and
/// size. A block with no such ore stays as it is.
public final class BWOreAdapter {

    public static final BWOreAdapter INSTANCE = new BWOreAdapter();

    private BWOreAdapter() {}

    private final EnumMap<StoneType, Ores> legacyOres = new EnumMap<>(StoneType.class);

    private static final boolean[] BOOLEANS = { false, true };

    private static class Ores {

        final StoneType stoneType;
        final String bigSuffix, smallSuffix;
        final BWMetaGeneratedOres big, bigNatural, small, smallNatural;

        Ores(StoneType stoneType, String bigSuffix, String smallSuffix) {
            this.stoneType = stoneType;
            this.bigSuffix = bigSuffix;
            this.smallSuffix = smallSuffix;

            big = new BWMetaGeneratedOres("bw.blockores", stoneType, false, false);
            bigNatural = new BWMetaGeneratedOres("bw.blockores.natural", stoneType, false, true);
            small = new BWMetaGeneratedOres("bw.blockoresSmall", stoneType, true, false);
            smallNatural = new BWMetaGeneratedOres("bw.blockoresSmall.natural", stoneType, true, true);

            GameRegistry.registerBlock(big, BWItemMetaGeneratedOre.class, "bw.blockores." + bigSuffix);
            GameRegistry.registerBlock(bigNatural, BWItemMetaGeneratedOre.class, "bw.blockores.natural." + bigSuffix);
            GameRegistry.registerBlock(small, BWItemMetaGeneratedOre.class, "bw.blockores." + smallSuffix);
            GameRegistry
                .registerBlock(smallNatural, BWItemMetaGeneratedOre.class, "bw.blockores.natural." + smallSuffix);
        }

        void registerOredict() {
            big.registerOredict();
            bigNatural.registerOredict();
            small.registerOredict();
            smallNatural.registerOredict();
        }

        BWMetaGeneratedOres get(boolean small, boolean natural) {
            if (small) {
                return natural ? this.smallNatural : this.small;
            } else {
                return natural ? this.bigNatural : this.big;
            }
        }

        /// The registry name for one of this stone type's four legacy ore blocks, distinct from
        /// [BWMetaGeneratedOres#blockName] (the shared unlocalized name).
        String registryName(boolean small, boolean natural) {
            String suffix = small ? smallSuffix : bigSuffix;
            return natural ? "bw.blockores.natural." + suffix : "bw.blockores." + suffix;
        }
    }

    public void init() {
        legacyOres.put(StoneType.Stone, new Ores(StoneType.Stone, "01", "02"));
        legacyOres.put(StoneType.Moon, new Ores(StoneType.Moon, "03", "04"));

        TileEntityReplacementManager.tileEntityTransformer("bw.blockoresTE", (tag, world, chunk) -> {
            ImmutableBlockMeta bm = resolveLegacyTE(tag.getInteger("m"), false);
            return new BlockInfo(bm.getBlock(), bm.getBlockMeta());
        });
        TileEntityReplacementManager.tileEntityTransformer("bw.blockoresSmallTE", (tag, world, chunk) -> {
            ImmutableBlockMeta bm = resolveLegacyTE(tag.getInteger("m"), true);
            return new BlockInfo(bm.getBlock(), bm.getBlockMeta());
        });

        registerCurrentGenTransformers();
    }

    /// Registers the legacy ore blocks under their ore-dictionary names and associates each werkstoff-origin
    /// material's ore and small ore with its MaterialLib stack.
    public void registerOredict() {
        legacyOres.values()
            .forEach(Ores::registerOredict);
        for (Material material : MaterialLibAPI.getMaterials()) {
            List<String> prefixes = material.getProperty(GTMaterialProperties.WERKSTOFF_PREFIXES);
            if (prefixes == null) continue;
            if (prefixes.contains(OrePrefixes.ore.name())) associate(material, OrePrefixes.ore);
            if (prefixes.contains(OrePrefixes.oreSmall.name())) associate(material, OrePrefixes.oreSmall);
        }
    }

    private static void associate(Material material, OrePrefixes prefix) {
        ItemStack stack = MaterialParts.stack(prefix, material, 1);
        if (stack != null && stack.getItem() != null) MaterialData.addAssociation(prefix, material, stack);
    }

    /// Decodes one of the legacy blocks at `meta`, or null when the meta names no ore. The returned [OreInfo]
    /// comes from the pool and belongs to the caller (try-with-resources).
    public @Nullable OreInfo getOreInfo(BWMetaGeneratedOres block, int meta) {
        return decode(LegacyWerkstoffIndex.get(meta), block.stoneType, block.isSmall, block.isNatural);
    }

    private static @Nullable OreInfo decode(@Nullable Material material, StoneType stoneType, boolean small,
        boolean natural) {
        if (material == null) return null;

        OreInfo info = OreInfo.getNewInfo();

        info.material = material;
        info.stoneType = stoneType;
        info.isSmall = small;
        info.isNatural = natural;

        if (!GTOreAdapter.INSTANCE.supports(info)) {
            info.release();
            return null;
        }

        return info;
    }

    /// Decodes a TE-based `bw.blockoresTE`/`bw.blockoresSmallTE` meta, or null when the meta names no ore. That
    /// format only ever existed for [StoneType#Stone] and never encoded a natural flag, so the decoded ore is
    /// always marked natural. The returned [OreInfo] comes from the pool and belongs to the caller
    /// (try-with-resources).
    public @Nullable OreInfo getLegacyOreInfo(int meta, boolean small) {
        return decode(LegacyWerkstoffIndex.get(meta), StoneType.Stone, small, true);
    }

    private static ImmutableBlockMeta resolveLegacyTE(int meta, boolean small) {
        ImmutableBlockMeta bm = resolve(LegacyWerkstoffIndex.get(meta), StoneType.Stone, small);
        return bm == null ? new BlockMeta(Blocks.air, 0) : bm;
    }

    private static @Nullable ImmutableBlockMeta resolve(@Nullable Material material, StoneType stoneType,
        boolean small) {
        try (OreInfo info = decode(material, stoneType, small, true)) {
            return info == null ? null : GTOreAdapter.INSTANCE.getBlock(info);
        }
    }

    /// Converts placed and inventory legacy `bw.blockores*` blocks into the MaterialLib equivalent as a chunk
    /// or item loads.
    private void registerCurrentGenTransformers() {
        for (Ores ores : legacyOres.values()) {
            for (boolean small : BOOLEANS) {
                for (boolean natural : BOOLEANS) {
                    registerCurrentGenTransformer(ores, small, natural);
                }
            }
        }
    }

    private static void registerCurrentGenTransformer(Ores ores, boolean small, boolean natural) {
        String originalId = "bartworks:" + ores.registryName(small, natural);

        BlockReplacementManager.addTransformationHandler(originalId, info -> {
            ImmutableBlockMeta bm = resolve(LegacyWerkstoffIndex.get(info.metadata), ores.stoneType, small);
            if (bm == null) return false;
            info.blockID = Block.getIdFromBlock(bm.getBlock());
            info.metadata = bm.getBlockMeta();
            return true;
        });

        ItemStackReplacementManager.addTransformationHandler(originalId, (originalId2, tag) -> {
            ImmutableBlockMeta bm = resolve(LegacyWerkstoffIndex.get(tag.getInteger("Damage")), ores.stoneType, small);
            if (bm == null) return false;
            IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(Item.getItemFromBlock(bm.getBlock())));
            tag.setShort("Damage", (short) bm.getBlockMeta());
            return true;
        });
    }

    /// Hides from NEI every legacy block slot whose (material, stone, small-ore) combination resolves to a
    /// MaterialLib block; combinations without a MaterialLib counterpart stay visible.
    public void hideOres() {
        for (Ores ores : legacyOres.values()) {
            for (Material material : MaterialLibAPI.getMaterials()) {
                int legacyId = LegacyWerkstoffIndex.idOf(material);
                if (legacyId < 0) continue;

                for (boolean small : BOOLEANS) {
                    if (resolve(material, ores.stoneType, small) == null) continue;

                    for (boolean natural : BOOLEANS) {
                        API.hideItem(new ItemStack(ores.get(small, natural), 1, legacyId));
                    }
                }
            }
        }
    }
}
