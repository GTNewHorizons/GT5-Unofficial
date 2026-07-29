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
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials2.Materials2OreShapes;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.common.GTProxy.OreDropSystem;

/// The gtPlusPlus-material [IOreAdapter], reimplemented over MaterialLib the same way [BWOreAdapter]
/// reimplements bartworks ore: worldgen placement, mining drops, and prospecting
/// dispatch to this singleton via [OreManager] for any material carrying [GTMaterialProperties#GTPP_STATE]
/// with no live legacy id counterpart (see [#isGtpp]).
///
/// Unlike GT/bartworks ore, gtpp ore only ever existed on [StoneType#Stone] and never had a small-ore
/// variant, both [#supports(OreInfo)] and [#getBlock] enforce this, and no gtpp material ever claims
/// `Materials2OreShapes#oreSmall`.
///
/// [#isGtpp] only ever matches a *pure* gtpp material (no live legacy id -- see [MaterialUtils#oldSubId]): a name-merge
/// material's ore is already owned by [GTOreAdapter], tried first in [OreManager]'s adapter list, and this
/// adapter's own [Materials2OreShapes] drop/harvest-level dispatch (see that class) gates on the same
/// discriminator so a merge material's ore keeps GT's per-material formulas instead of gtpp's flat ones.
public final class GTPPOreAdapter implements IOreAdapter {

    public static GTPPOreAdapter INSTANCE = new GTPPOreAdapter();

    private GTPPOreAdapter() {}

    @Override
    public boolean supports(Block block, int meta) {
        BlockMaterialInfo info = MaterialLibAPI.lookupBlock(block, meta);
        return info != null && info.shape() == Materials2OreShapes.ore && isGtpp(info.material());
    }

    @Override
    public boolean supports(OreInfo info) {
        if (info.stoneType != null && info.stoneType != StoneType.Stone) return false;
        if (info.isSmall) return false;

        return isGtpp(info.material) && info.material.hasShape(Materials2OreShapes.ore);
    }

    @Override
    public OreInfo getOreInfo(Block block, int meta) {
        BlockMaterialInfo blockInfo = MaterialLibAPI.lookupBlock(block, meta);
        if (blockInfo == null || blockInfo.shape() != Materials2OreShapes.ore || !isGtpp(blockInfo.material())) {
            return null;
        }

        OreInfo info = OreInfo.getNewInfo();
        info.material = blockInfo.material();
        info.stoneType = StoneType.Stone;
        info.isNatural = true;
        return info;
    }

    @Override
    public ImmutableBlockMeta getBlock(OreInfo info) {
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
        try (OreInfo info = OreInfo.getNewInfo()) {
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
        return Math.min(Math.max(MaterialUtils.tier(material), 1), 6);
    }

    /// Whether a MaterialLib material belongs to the gtPlusPlus ore family -- carries
    /// [GTMaterialProperties#GTPP_STATE] and has no live id-backed legacy counterpart ([MaterialUtils#oldSubId] stays
    /// at
    /// its `-1` default) -- ore-block concerns still belong to this adapter for those, so only a positive-id
    /// resolution excludes.
    private static boolean isGtpp(@Nullable Material material) {
        if (material == null || material.getProperty(GTMaterialProperties.GTPP_STATE) == null) return false;
        return MaterialUtils.oldSubId(material) < 0;
    }

    @Override
    public @NotNull ArrayList<ItemStack> getOreDrops(Random random, OreInfo info2, boolean silktouch, int fortune) {
        if (!supports(info2)) return new ArrayList<>();

        @SuppressWarnings("unchecked")
        OreInfo info = (OreInfo) info2;

        if (info.stoneType == null) info.stoneType = StoneType.Stone;

        OreDropSystem oreDropSystem = GTMod.proxy.oreDropSystem;

        if (silktouch) oreDropSystem = OreDropSystem.Block;

        return getBigOreDrops(random, oreDropSystem, info, fortune);
    }

    @Override
    public List<ItemStack> getPotentialDrops(OreInfo info2) {
        if (!supports(info2)) return new ArrayList<>();

        @SuppressWarnings("unchecked")
        OreInfo info = (OreInfo) info2;

        return getBigOreDrops(ThreadLocalRandom.current(), GTMod.proxy.oreDropSystem, info, 0);
    }

    private ArrayList<ItemStack> getBigOreDrops(Random random, OreDropSystem oreDropMode, OreInfo info, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();

        switch (oreDropMode) {
            case Item -> drops.add(MaterialParts.stack(Materials2Shapes.rawOre, info.material, 1));
            case FortuneItem -> {
                if (fortune > 0) {
                    int aMinAmount = 1;
                    if (fortune > 3) fortune = 3;
                    long amount = (long) random.nextInt(fortune) + aMinAmount;
                    for (int i = 0; i < amount; i++) {
                        drops.add(MaterialParts.stack(Materials2Shapes.rawOre, info.material, 1));
                    }
                } else {
                    drops.add(MaterialParts.stack(Materials2Shapes.rawOre, info.material, 1));
                }
            }
            case UnifiedBlock, PerDimBlock, Block -> drops.add(oreStack(info.material));
        }

        return drops;
    }
}
