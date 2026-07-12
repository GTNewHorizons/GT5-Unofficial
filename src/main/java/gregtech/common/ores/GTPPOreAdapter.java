package gregtech.common.ores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;
import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;
import com.ruling_0.materiallib.api.BlockMaterialInfo;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.GTMod;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials2.Materials2OreShapes;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.common.GTProxy.OreDropSystem;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialReconstruction;

/// The gtPlusPlus-material [IOreAdapter], reimplemented over MaterialLib the same way stage 10.2's
/// [BWOreAdapter] ported [bartworks.system.material.Werkstoff] ore: resolves through a material's MaterialLib
/// counterpart (see [#materialOf]) before falling back to the legacy [BlockBaseOre] instance, which stays
/// constructed and registered (see that class) for pre-migration saves and for any gtpp material that never
/// gained [Materials2OreShapes#ore] membership.
///
/// Unlike [gregtech.api.enums.Materials]/[bartworks.system.material.Werkstoff] ore, gtpp ore only ever
/// existed on [StoneType#Stone] (the legacy adapter this replaces hardcoded `StoneType.Stone` unconditionally)
/// and never had a small-ore variant -- both [#supports(OreInfo)] and [#getBlock] enforce this exactly as the
/// legacy class did, and no gtpp material ever claims `Materials2OreShapes#oreSmall`. [BlockBaseOre] is
/// also one distinct registered `Block` instance PER MATERIAL (like
/// [gtPlusPlus.core.block.base.BlockBaseModular], unlike the shared meta-block
/// [gregtech.common.blocks.GTBlockOre]/[bartworks.system.material.BWMetaGeneratedOres] use), so no per-meta
/// decode is needed anywhere in this class.
///
/// [#materialOf] only ever resolves a *pure* gtpp material (no live [gregtech.api.enums.Materials] counterpart
/// -- see [MU#materialOf]): a name-merge material's ore is already owned by [GTOreAdapter], tried first in
/// [OreManager]'s adapter list, and this adapter's own [Materials2OreShapes] drop/harvest-level dispatch (see
/// that class) gates on the same discriminator so a merge material's ore keeps GT's per-material formulas
/// instead of gtpp's flat ones.
public final class GTPPOreAdapter implements IOreAdapter<Material> {

    public static GTPPOreAdapter INSTANCE = new GTPPOreAdapter();

    private GTPPOreAdapter() {}

    @Override
    public boolean supports(Block block, int meta) {
        if (block instanceof BlockBaseOre) return true;

        BlockMaterialInfo info = MaterialLibAPI.lookupBlock(block, meta);
        return info != null && info.shape() == Materials2OreShapes.ore && materialOf(info.material()) != null;
    }

    @Override
    public boolean supports(OreInfo<?> info) {
        if (info.stoneType != null && info.stoneType != StoneType.Stone) return false;
        if (!(info.material instanceof Material gtppMat)) return false;
        if (info.isSmall) return false;

        return gtppMat.hasOre();
    }

    @Override
    public OreInfo<Material> getOreInfo(Block block, int meta) {
        if (block instanceof BlockBaseOre gtppOre) {
            OreInfo<Material> info = OreInfo.getNewInfo();
            info.stoneType = StoneType.Stone;
            info.material = gtppOre.getMaterialEx();
            info.isNatural = true;
            return info;
        }

        BlockMaterialInfo blockInfo = MaterialLibAPI.lookupBlock(block, meta);
        if (blockInfo == null || blockInfo.shape() != Materials2OreShapes.ore) return null;

        Material material = materialOf(blockInfo.material());
        if (material == null) return null;

        OreInfo<Material> info = OreInfo.getNewInfo();
        info.stoneType = StoneType.Stone;
        info.material = material;
        info.isNatural = true;
        return info;
    }

    @Override
    public ImmutableBlockMeta getBlock(OreInfo<?> info) {
        if (!supports(info)) return null;

        Material gtppMat = (Material) info.material;

        com.ruling_0.materiallib.api.Material ml = MaterialReconstruction.materialLibOf(gtppMat.getUnlocalizedName());
        if (ml != null && ml.hasShape(Materials2OreShapes.ore)) {
            ItemStack stack = MaterialLibAPI
                .getStack(ml, Materials2OreShapes.ore, Materials2OreShapes.variantOf(StoneType.Stone.name()), 1);
            if (stack != null) {
                return new BlockMeta(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
            }
        }

        Block ore = gtppMat.getOreBlock(1);
        if (ore == null) return null;

        return new BlockMeta(ore, 0);
    }

    /// The drops for one MaterialLib gtpp ore block, called from [Materials2OreShapes]'s drop hook when the
    /// material carries [GTMaterialProperties#GTPP] but has no live gregtech counterpart -- see
    /// [BWOreAdapter#shapeDrops]/[GTOreAdapter#shapeDrops] for the equivalent on the other two ore families.
    public List<ItemStack> shapeDrops(com.ruling_0.materiallib.api.Material mlMaterial, int fortune,
        boolean isSilkTouch) {
        Material material = materialOf(mlMaterial);
        if (material == null) return List.of();

        try (OreInfo<Material> info = OreInfo.getNewInfo()) {
            info.material = material;
            info.stoneType = StoneType.Stone;
            info.isNatural = true;

            return getOreDrops(ThreadLocalRandom.current(), info, isSilkTouch, fortune);
        }
    }

    /// The harvest level for a MaterialLib gtpp ore material -- legacy [BlockBaseOre] used
    /// `Math.min(Math.max(material.vTier, 1), 6)` for every material via its `BasicBlock` mining-level
    /// constructor argument, a flat per-material formula unlike GT's/BW's own.
    public int harvestLevel(com.ruling_0.materiallib.api.Material mlMaterial) {
        Material material = materialOf(mlMaterial);
        if (material == null) return 0;

        return Math.min(Math.max(material.vTier, 1), 6);
    }

    /// The gtpp material a MaterialLib material was reconstructed from, or null if it carries no
    /// [GTMaterialProperties#GTPP] data, is not a [MaterialReconstruction]-owned name, or is a name-merge
    /// already claimed by a live [gregtech.api.enums.Materials] counterpart (see this class's javadoc).
    private static Material materialOf(com.ruling_0.materiallib.api.Material mlMaterial) {
        if (mlMaterial == null || mlMaterial.getProperty(GTMaterialProperties.GTPP) == null) return null;
        if (MU.materialOf(mlMaterial) != null) return null;

        String name = mlMaterial.getName();
        if (!MaterialReconstruction.isReconstructed(name)) return null;

        return MaterialReconstruction.byName(name);
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
            case Item -> drops.add(info.material.getRawOre(1));
            case FortuneItem -> {
                if (fortune > 0) {
                    int aMinAmount = 1;
                    if (fortune > 3) fortune = 3;
                    long amount = (long) random.nextInt(fortune) + aMinAmount;
                    for (int i = 0; i < amount; i++) {
                        drops.add(info.material.getRawOre(1));
                    }
                } else {
                    drops.add(info.material.getRawOre(1));
                }
            }
            case UnifiedBlock, PerDimBlock, Block -> {
                drops.add(info.material.getOre(1));
            }
        }

        return drops;
    }
}
