package gregtech.common.ores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;
import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;
import com.ruling_0.materiallib.api.BlockMaterialInfo;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials2.Materials2OreShapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.common.GTProxy.OreDropSystem;

/// The gtPlusPlus-material [IOreAdapter], reimplemented over MaterialLib the same way [BWOreAdapter]
/// reimplements bartworks ore: worldgen placement, mining drops, and prospecting
/// dispatch to this singleton via [OreManager] for any material carrying [GTMaterialProperties#GTPP_STATE]
/// with no live id-backed [gregtech.api.enums.Materials] counterpart (see [#isGtpp]).
///
/// Unlike [gregtech.api.enums.Materials]/bartworks ore, gtpp ore only ever existed
/// on [StoneType#Stone] and never had a small-ore variant, both [#supports(OreInfo)] and [#getBlock] enforce
/// this, and no gtpp material ever claims `Materials2OreShapes#oreSmall`.
///
/// [#isGtpp] only ever matches a *pure* gtpp material (no live [gregtech.api.enums.Materials] counterpart --
/// see [MU#oldSubId]): a name-merge material's ore is already owned by [GTOreAdapter], tried first in
/// [OreManager]'s adapter list, and this adapter's own [Materials2OreShapes] drop/harvest-level dispatch (see
/// that class) gates on the same discriminator so a merge material's ore keeps GT's per-material formulas
/// instead of gtpp's flat ones.
public final class GTPPOreAdapter implements IOreAdapter<Material> {

    public static GTPPOreAdapter INSTANCE = new GTPPOreAdapter();

    private GTPPOreAdapter() {}

    @Override
    public boolean supports(Block block, int meta) {
        BlockMaterialInfo info = MaterialLibAPI.lookupBlock(block, meta);
        return info != null && info.shape() == Materials2OreShapes.ore && isGtpp(info.material());
    }

    @Override
    public boolean supports(OreInfo<?> info) {
        if (info.stoneType != null && info.stoneType != StoneType.Stone) return false;
        if (info.isSmall) return false;

        return info.material instanceof Material material && isGtpp(material)
            && material.hasShape(Materials2OreShapes.ore);
    }

    @Override
    public OreInfo<Material> getOreInfo(Block block, int meta) {
        BlockMaterialInfo blockInfo = MaterialLibAPI.lookupBlock(block, meta);
        if (blockInfo == null || blockInfo.shape() != Materials2OreShapes.ore || !isGtpp(blockInfo.material())) {
            return null;
        }

        OreInfo<Material> info = OreInfo.getNewInfo();
        info.material = blockInfo.material();
        info.stoneType = StoneType.Stone;
        info.isNatural = true;
        return info;
    }

    @Override
    public ImmutableBlockMeta getBlock(OreInfo<?> info) {
        if (!supports(info)) return null;

        ItemStack stack = oreStack((Material) info.material);
        if (stack == null) return null;

        return new BlockMeta(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
    }

    /// The `Materials2OreShapes#ore` stack for a gtpp ore material at its (sole) [StoneType#Stone] variant --
    /// that shape carries per-stone-type variants, so it cannot resolve through the plain [MU#stack] overload.
    private static @Nullable ItemStack oreStack(Material material) {
        return MaterialLibAPI
            .getStack(material, Materials2OreShapes.ore, Materials2OreShapes.variantOf(StoneType.Stone.name()), 1);
    }

    /// The drops for one MaterialLib gtpp ore block, called from [Materials2OreShapes]'s drop hook when the
    /// material carries [GTMaterialProperties#GTPP_STATE] but has no live gregtech counterpart -- see
    /// [BWOreAdapter#shapeDrops]/[GTOreAdapter#shapeDrops] for the equivalent on the other two ore families.
    public List<ItemStack> shapeDrops(Material material, int fortune, boolean isSilkTouch) {
        try (OreInfo<Material> info = OreInfo.getNewInfo()) {
            info.material = material;
            info.stoneType = StoneType.Stone;
            info.isNatural = true;

            return getOreDrops(ThreadLocalRandom.current(), info, isSilkTouch, fortune);
        }
    }

    /// The harvest level for a MaterialLib gtpp ore material -- the retired legacy `BlockBaseOre` used
    /// `Math.min(Math.max(material.vTier, 1), 6)` for every material via its `BasicBlock` mining-level
    /// constructor argument, a flat per-material formula unlike GT's/BW's own.
    public int harvestLevel(Material material) {
        Integer tier = material.getProperty(GTMaterialProperties.TIER);
        return Math.min(Math.max(tier == null ? 0 : tier, 1), 6);
    }

    /// Whether a MaterialLib material belongs to the gtPlusPlus ore family -- carries
    /// [GTMaterialProperties#GTPP_STATE] and has no live id-backed [gregtech.api.enums.Materials] counterpart.
    /// A bridge [gregtech.api.enums.Materials] also carries no real legacy id ([MU#oldSubId] stays at its `-1`
    /// default, since a [gregtech.api.enums.MaterialBuilder] material is never assigned one) -- ore-block
    /// concerns still belong to this adapter for those, so only a positive-id resolution excludes.
    private static boolean isGtpp(@Nullable Material material) {
        if (material == null || material.getProperty(GTMaterialProperties.GTPP_STATE) == null) return false;
        return MU.oldSubId(material) < 0;
    }

    @Override
    public @NotNull ArrayList<ItemStack> getOreDrops(Random random, OreInfo<?> info2, boolean silktouch, int fortune) {
        if (!supports(info2)) return new ArrayList<>();

        @SuppressWarnings("unchecked")
        OreInfo<Material> info = (OreInfo<Material>) info2;

        if (info.stoneType == null) info.stoneType = StoneType.Stone;

        OreDropSystem oreDropSystem = GTMod.proxy.oreDropSystem;

        if (silktouch) oreDropSystem = OreDropSystem.Block;

        return getBigOreDrops(random, oreDropSystem, info, fortune);
    }

    @Override
    public List<ItemStack> getPotentialDrops(OreInfo<?> info2) {
        if (!supports(info2)) return new ArrayList<>();

        @SuppressWarnings("unchecked")
        OreInfo<Material> info = (OreInfo<Material>) info2;

        return getBigOreDrops(ThreadLocalRandom.current(), GTMod.proxy.oreDropSystem, info, 0);
    }

    private ArrayList<ItemStack> getBigOreDrops(Random random, OreDropSystem oreDropMode, OreInfo<Material> info,
        int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();

        switch (oreDropMode) {
            case Item -> drops.add(MU.stack(OrePrefixes.rawOre, info.material, 1));
            case FortuneItem -> {
                if (fortune > 0) {
                    int aMinAmount = 1;
                    if (fortune > 3) fortune = 3;
                    long amount = (long) random.nextInt(fortune) + aMinAmount;
                    for (int i = 0; i < amount; i++) {
                        drops.add(MU.stack(OrePrefixes.rawOre, info.material, 1));
                    }
                } else {
                    drops.add(MU.stack(OrePrefixes.rawOre, info.material, 1));
                }
            }
            case UnifiedBlock, PerDimBlock, Block -> drops.add(oreStack(info.material));
        }

        return drops;
    }
}
