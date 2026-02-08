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

import gregtech.GTMod;
import gregtech.api.enums.StoneType;
import gregtech.common.GTProxy.OreDropSystem;
import gtPlusPlus.core.block.base.BlockBaseOre;
import gtPlusPlus.core.material.Material;

public final class GTPPOreAdapter implements IOreAdapter<Material> {

    public static GTPPOreAdapter INSTANCE = new GTPPOreAdapter();

    private GTPPOreAdapter() {}

    @Override
    public boolean supports(Block block, int meta) {
        return block instanceof BlockBaseOre;
    }

    @Override
    public boolean supports(OreInfo<?> info) {
        if (info.stoneType != null && info.stoneType != StoneType.Stone) return false;
        if (!(info.material instanceof Material gtppMat)) return false;
        if (info.isSmall) return false;
        if (!gtppMat.hasOre()) return false;

        return true;
    }

    @Override
    public OreInfo<Material> getOreInfo(Block block, int meta) {
        if (!(block instanceof BlockBaseOre gtppOre)) return null;

        OreInfo<Material> info = OreInfo.getNewInfo();

        info.stoneType = StoneType.Stone;
        info.material = gtppOre.getMaterialEx();
        info.isNatural = true;

        return info;
    }

    @Override
    public ImmutableBlockMeta getBlock(OreInfo<?> info) {
        if (!supports(info)) return null;

        if (!(info.material instanceof Material gtppMat)) return null;

        Block ore = gtppMat.getOreBlock(1);

        if (ore == null) return null;

        return new BlockMeta(ore, 0);
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
                // if shouldFortune and isNatural then get fortune drops
                // if not shouldFortune or not isNatural then get normal drops
                // if not shouldFortune and isNatural then get normal drops
                // if shouldFortune and not isNatural then get normal drops
                if (fortune > 0) {
                    int aMinAmount = 1;
                    // Max applicable fortune
                    if (fortune > 3) fortune = 3;
                    long amount = (long) random.nextInt(fortune) + aMinAmount;
                    for (int i = 0; i < amount; i++) {
                        drops.add(info.material.getRawOre(1));
                    }
                } else {
                    drops.add(info.material.getRawOre(1));
                }
            }
            // Unified ore, Per Dimension ore, Regular ore
            case UnifiedBlock, PerDimBlock, Block -> {
                drops.add(info.material.getOre(1));
            }
        }

        return drops;
    }
}
