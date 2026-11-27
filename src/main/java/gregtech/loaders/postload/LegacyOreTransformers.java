package gregtech.loaders.postload;

import java.util.function.IntFunction;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;
import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;
import com.gtnewhorizons.postea.api.IDExtenderCompat;
import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;
import com.ruling_0.materiallib.api.Material;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Mods;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials.LegacyMaterialIDIndex;
import gregtech.api.enums.materials.LegacyWerkstoffIndex;
import gregtech.api.enums.materials.Materials;
import gregtech.api.util.GTUtility;
import gregtech.common.ores.GTOreAdapter;
import gregtech.common.ores.OreInfo;

/// Migrates the legacy ore blocks into MaterialLib ores: GregTech's tile-entity `gt.blockores`, its
/// `gt.blockores2`..`gt.blockores7` blocks, and the bartworks `bw.blockores*` blocks. An ore keeps its material,
/// stone type and size. HeeEndium ores become Hardcore Ender Expansion's endium ore, and any other ore with no
/// MaterialLib counterpart becomes its stone.
///
/// [#resolveTileEntityOre] and [#resolveWerkstoffTileEntityOre] answer the same question for callers that read
/// saved chunks before Postea migrates them.
public final class LegacyOreTransformers {

    private static final int SMALL_ORE_META_OFFSET = 16000;
    private static final int NATURAL_ORE_META_OFFSET = 8000;

    /// The stone types of the tile-entity `gt.blockores` format, by thousands digit.
    private static final StoneType[] TILE_ENTITY_STONES = { StoneType.Stone, StoneType.Netherrack, StoneType.Endstone,
        StoneType.BlackGranite, StoneType.RedGranite, StoneType.Marble, StoneType.Basalt };

    /// The stone types of `gt.blockores2`..`gt.blockores7`, by thousands digit once the small and natural offsets
    /// are removed.
    // spotless:off
    private static final StoneType[][] SERIES_STONES = {
        { StoneType.Stone, StoneType.Netherrack, StoneType.Endstone, StoneType.BlackGranite, StoneType.RedGranite,
            StoneType.Marble, StoneType.Basalt, StoneType.Moon },
        { StoneType.Mars, StoneType.Asteroid, StoneType.Phobos, StoneType.Deimos, StoneType.Ceres, StoneType.Io,
            StoneType.Europa, StoneType.Ganymede },
        { StoneType.Callisto, StoneType.Enceladus, StoneType.Titan, StoneType.Miranda, StoneType.Oberon,
            StoneType.Proteus, StoneType.Triton, StoneType.Pluto },
        { StoneType.Venus, StoneType.Mercury },
        { StoneType.Haumea, StoneType.MakeMake, StoneType.AlphaCentauri, StoneType.TCetiE, StoneType.VegaB,
            StoneType.BarnardaE, StoneType.BarnardaF, StoneType.Horus },
        { StoneType.AnubisAndMaahes, StoneType.PackedIce, StoneType.SethIce, StoneType.SethClay, StoneType.Deepslate,
            StoneType.Tuff, StoneType.BlueIce },
    };
    // spotless:on

    private LegacyOreTransformers() {}

    static void register() {
        TileEntityReplacementManager.tileEntityTransformer(
            "GT_TileEntity_Ores",
            (tag, world, chunk) -> toBlockInfo(resolveTileEntityOre(tag.getInteger("m"))));
        PosteaTransformers.addItemTransformer(
            "gregtech:gt.blockores",
            (originalId, tag) -> setStack(tag, resolveTileEntityOre(tag.getInteger("Damage"))));

        for (int series = 0; series < SERIES_STONES.length; series++) {
            StoneType[] stones = SERIES_STONES[series];
            registerBlock("gregtech:gt.blockores" + (series + 2), meta -> {
                int stoneIndex = meta % SMALL_ORE_META_OFFSET % NATURAL_ORE_META_OFFSET / 1000;
                return resolve(
                    LegacyMaterialIDIndex.get(meta % 1000),
                    GTUtility.getIndexSafe(stones, stoneIndex),
                    meta >= SMALL_ORE_META_OFFSET);
            });
        }

        TileEntityReplacementManager.tileEntityTransformer(
            "bw.blockoresTE",
            (tag, world, chunk) -> toBlockInfo(resolveWerkstoffTileEntityOre(tag.getInteger("m"), false)));
        TileEntityReplacementManager.tileEntityTransformer(
            "bw.blockoresSmallTE",
            (tag, world, chunk) -> toBlockInfo(resolveWerkstoffTileEntityOre(tag.getInteger("m"), true)));
        registerWerkstoffOres(StoneType.Stone, "01", "02");
        registerWerkstoffOres(StoneType.Moon, "03", "04");
    }

    private static void registerWerkstoffOres(StoneType stoneType, String oreSuffix, String smallOreSuffix) {
        for (String natural : new String[] { "", "natural." }) {
            registerBlock(
                "bartworks:bw.blockores." + natural + oreSuffix,
                meta -> resolveWerkstoffOre(meta, stoneType, false));
            registerBlock(
                "bartworks:bw.blockores." + natural + smallOreSuffix,
                meta -> resolveWerkstoffOre(meta, stoneType, true));
        }
    }

    private static void registerBlock(String registryName, IntFunction<ImmutableBlockMeta> resolver) {
        PosteaTransformers.addBlockTransformer(registryName, info -> {
            ImmutableBlockMeta replacement = resolver.apply(info.metadata);
            if (replacement == null) return false;
            info.blockID = Block.getIdFromBlock(replacement.getBlock());
            info.metadata = replacement.getBlockMeta();
            return true;
        });
        PosteaTransformers.addItemTransformer(
            registryName,
            (originalId, tag) -> setStack(tag, resolver.apply(tag.getInteger("Damage"))));
    }

    /// The block a saved `GT_TileEntity_Ores` tile entity with metadata `meta` migrates to, or null when the metadata
    /// names no stone type.
    public static @Nullable ImmutableBlockMeta resolveTileEntityOre(int meta) {
        return resolve(
            LegacyMaterialIDIndex.get(meta % 1000),
            GTUtility.getIndexSafe(TILE_ENTITY_STONES, meta % SMALL_ORE_META_OFFSET / 1000),
            meta >= SMALL_ORE_META_OFFSET);
    }

    /// The block a saved `bw.blockoresTE` (or, when `small`, `bw.blockoresSmallTE`) tile entity for werkstoff
    /// `werkstoffId` migrates to.
    public static @Nullable ImmutableBlockMeta resolveWerkstoffTileEntityOre(int werkstoffId, boolean small) {
        return resolveWerkstoffOre(werkstoffId, StoneType.Stone, small);
    }

    private static @Nullable ImmutableBlockMeta resolveWerkstoffOre(int werkstoffId, StoneType stoneType,
        boolean small) {
        return resolve(LegacyWerkstoffIndex.get(werkstoffId), stoneType, small);
    }

    private static @Nullable ImmutableBlockMeta resolve(@Nullable Material material, @Nullable StoneType stoneType,
        boolean small) {
        if (stoneType == null) return null;
        if (material != null) {
            try (OreInfo info = OreInfo.getNewInfo()) {
                info.material = material;
                info.stoneType = stoneType;
                info.isSmall = small;
                info.isNatural = true;
                ImmutableBlockMeta ore = GTOreAdapter.INSTANCE.getBlock(info);
                if (ore != null) return ore;
            }
            if (material == Materials.Endium && Mods.HardcoreEnderExpansion.isModLoaded()) {
                return new BlockMeta(GameRegistry.findBlock(Mods.HardcoreEnderExpansion.ID, "endium_ore"), 0);
            }
        }
        ImmutableBlockMeta stone = stoneType.getStone();
        return stone == null || stone.getBlock() == null ? null : stone;
    }

    private static BlockInfo toBlockInfo(@Nullable ImmutableBlockMeta replacement) {
        return replacement == null ? new BlockInfo(Blocks.air, 0)
            : new BlockInfo(replacement.getBlock(), replacement.getBlockMeta());
    }

    private static boolean setStack(NBTTagCompound tag, @Nullable ImmutableBlockMeta replacement) {
        if (replacement == null) return false;
        IDExtenderCompat.setItemStackID(tag, Item.getIdFromItem(Item.getItemFromBlock(replacement.getBlock())));
        tag.setShort("Damage", (short) replacement.getBlockMeta());
        return true;
    }
}
