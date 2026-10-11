package gregtech.common.ores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;
import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;
import com.ruling_0.materiallib.api.BlockMaterialInfo;
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.GTMod;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneCategory;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials.OreShapes;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.api.util.OreDictUtils;
import gregtech.common.GTProxy.OreDropSystem;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;

/// The [IOreAdapter] for every material carrying the [OreShapes#ore] or [OreShapes#oreSmall] shape: worldgen,
/// mining, prospecting, and the void miner place and read those blocks through this singleton, via [OreManager].
/// A block's stone type is its shape variant ([OreShapes#stoneTypeOf]).
///
/// Every material follows the same rules, driven by its declared data: its ore exists on the stones
/// [MaterialUtils#isValidStone] accepts, its small ore on the same stones except ice ones, and only where it
/// carries the shape. Its harvest level is [GTMaterialProperties#ORE_HARVEST_LEVEL], or else its tool quality.
public final class GTOreAdapter implements IOreAdapter {

    public static GTOreAdapter INSTANCE = new GTOreAdapter();

    private GTOreAdapter() {}

    /// Registers every material's ore on every stone type under that stone type's ore-dictionary prefix,
    /// associating it with the material where the prefix unifies.
    public void registerOreNames() {
        for (Material material : MaterialLibAPI.getMaterials()) {
            for (StoneType stoneType : StoneType.VALUES) {
                ItemStack stack = getVariantStack(material, stoneType, false);
                if (stack == null) continue;

                OrePrefixes prefix = stoneType.getPrefix();
                if (prefix.isUnifiable()) {
                    OreDictUtils.registerAndAssociate(prefix, material, stack);
                } else {
                    OreDictUtils.registerOre(prefix, stack);
                }
            }
        }
    }

    /// The runtime config gate for the four config-gated stone types, which are declared unconditionally as
    /// [OreShapes] variants. Complements [StoneType#isEnabled]'s mod-loaded gate.
    private static boolean isStoneConfigEnabled(StoneType stoneType) {
        return switch (stoneType) {
            case BlackGranite -> GTMod.proxy.enableBlackGraniteOres;
            case RedGranite -> GTMod.proxy.enableRedGraniteOres;
            case Marble -> GTMod.proxy.enableMarbleOres;
            case Basalt -> GTMod.proxy.enableBasaltOres;
            default -> true;
        };
    }

    /// Whether `material` has an ore block, or a small-ore block when `small`, on `stone`: the stone is enabled,
    /// the material's ore may generate on it ([MaterialUtils#isValidStone]), small ore is not on ice stone, and
    /// the material carries the shape.
    private static boolean hasOre(@Nullable Material material, @Nullable IStoneType stone, boolean small) {
        if (material == null || !(stone instanceof StoneType stoneType)) return false;
        if (!stoneType.isEnabled() || !isStoneConfigEnabled(stoneType)) return false;
        if (!MaterialUtils.isValidStone(material, stoneType)) return false;
        if (small && stoneType.getCategory() == StoneCategory.Ice) return false;
        return material.hasShape(small ? OreShapes.oreSmall : OreShapes.ore);
    }

    @Override
    public boolean supports(Block block, int meta) {
        BlockMaterialInfo info = MaterialLibAPI.lookupBlock(block, meta);
        return info != null && isOreShape(info.shape());
    }

    private static boolean isOreShape(Shape shape) {
        return shape == OreShapes.ore || shape == OreShapes.oreSmall;
    }

    /// Whether `info` names an ore block. A null stone type stands for the first of the material's
    /// [MaterialUtils#validStones].
    @Override
    public boolean supports(OreInfo info) {
        if (info.material == null) return false;

        IStoneType stoneType = info.stoneType;
        if (stoneType == null) {
            List<IStoneType> validStones = MaterialUtils.validStones(info.material);
            if (validStones.isEmpty()) return false;
            stoneType = validStones.get(0);
        }

        return hasOre(info.material, stoneType, info.isSmall);
    }

    @Override
    public OreInfo getOreInfo(Block block, int meta) {
        BlockMaterialInfo blockInfo = MaterialLibAPI.lookupBlock(block, meta);
        if (blockInfo == null || !isOreShape(blockInfo.shape()) || blockInfo.material() == null) return null;

        StoneType stoneType = OreShapes.stoneTypeOf(blockInfo.variant());
        if (stoneType == null) return null;

        OreInfo info = OreInfo.getNewInfo();

        info.material = blockInfo.material();
        info.stoneType = stoneType;
        info.isSmall = blockInfo.shape() == OreShapes.oreSmall;
        info.isNatural = true;

        return info;
    }

    /// The ore block `info` names, or null when there is none. A null stone type stands for
    /// [StoneType#Stone].
    @Override
    public ImmutableBlockMeta getBlock(OreInfo info) {
        if (info.stoneType == null) info.stoneType = StoneType.Stone;
        if (!(info.stoneType instanceof StoneType stoneType)) return null;

        ItemStack stack = getVariantStack(info.material, stoneType, info.isSmall);
        if (stack == null) return null;

        return new BlockMeta(Block.getBlockFromItem(stack.getItem()), stack.getItemDamage());
    }

    /// The ore stack a (material, stone type, small-ore) triple resolves to, or null when that stone type carries no
    /// such block for the material. Unlike [#getStack], never falls back to another stone type.
    public @Nullable ItemStack getVariantStack(@Nullable Material material, StoneType stoneType, boolean small) {
        if (!hasOre(material, stoneType, small)) return null;

        Shape shape = small ? OreShapes.oreSmall : OreShapes.ore;
        return MaterialLibAPI.getStack(material, shape, OreShapes.variantOf(stoneType.name()), 1);
    }

    /// The harvest level for an ore or small-ore block of `material`: its [GTMaterialProperties#ORE_HARVEST_LEVEL],
    /// or else its tool quality, plus `bonus` (`-1` for small ore, `0` for ore).
    public int harvestLevel(Material material, int bonus) {
        Integer declared = material.getProperty(GTMaterialProperties.ORE_HARVEST_LEVEL);
        int harvestLevel = declared != null ? declared : MaterialUtils.toolQuality(material);

        return GTUtility.clamp(harvestLevel + bonus, 0, GTMod.proxy.mMaxHarvestLevel);
    }

    /// The drops for one ore or small-ore block. `variant` resolves back to a [StoneType] via
    /// [OreShapes#stoneTypeOf].
    public List<ItemStack> shapeDrops(Material material, String variant, int fortune, boolean isSilkTouch,
        boolean isSmall) {
        StoneType stoneType = OreShapes.stoneTypeOf(variant);
        if (material == null || stoneType == null) return List.of();

        try (OreInfo info = OreInfo.getNewInfo()) {
            info.material = material;
            info.stoneType = stoneType;
            info.isSmall = isSmall;
            info.isNatural = true;

            return getOreDrops(ThreadLocalRandom.current(), info, isSilkTouch, fortune);
        }
    }

    @Override
    public @NotNull ArrayList<ItemStack> getOreDrops(Random random, OreInfo info, boolean silktouch, int fortune) {
        if (!supports(info)) return new ArrayList<>();

        if (info.stoneType == null) info.stoneType = StoneType.Stone;

        if (info.isSmall) {
            return getSmallOreDrops(random, info, fortune);
        } else {
            OreDropSystem oreDropSystem = GTMod.proxy.oreDropSystem;

            if (silktouch) oreDropSystem = OreDropSystem.Block;

            return getBigOreDrops(random, oreDropSystem, info, fortune);
        }
    }

    @Override
    public List<ItemStack> getPotentialDrops(OreInfo info) {
        if (!supports(info)) return new ArrayList<>();

        if (info.isSmall) {
            ObjectLinkedOpenHashSet<ItemId> drops = new ObjectLinkedOpenHashSet<>();

            for (ItemStack stack : SmallOreDrops.getDropList(info.material)) {
                ItemId id = ItemId.create(stack);

                drops.add(id);
            }

            ArrayList<ItemStack> drops2 = new ArrayList<>();

            for (ItemId id : drops) {
                drops2.add(id.getItemStack());
            }

            return drops2;
        } else {
            return getBigOreDrops(ThreadLocalRandom.current(), GTMod.proxy.oreDropSystem, info, 0);
        }
    }

    private ArrayList<ItemStack> getSmallOreDrops(Random random, OreInfo info, int fortune) {
        ArrayList<ItemStack> possibleDrops = SmallOreDrops.getDropList(info.material);
        ArrayList<ItemStack> drops = new ArrayList<>();

        if (!possibleDrops.isEmpty()) {
            int oreMultiplier = MaterialUtils.oreMultiplier(info.material);
            int dropCount = Math
                .max(1, oreMultiplier + (fortune > 0 ? random.nextInt(1 + fortune * oreMultiplier) : 0) / 2);

            for (int i = 0; i < dropCount; i++) {
                drops.add(GTUtility.copyAmount(1, possibleDrops.get(random.nextInt(possibleDrops.size()))));
            }
        }

        if (random.nextInt(3 + fortune) > 1) {
            drops.add(info.stoneType.getDust(random.nextInt(3) == 0, 1));
        }

        return drops;
    }

    private ArrayList<ItemStack> getBigOreDrops(Random random, OreDropSystem oreDropMode, OreInfo info, int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();

        // For Sake of god of balance!

        switch (oreDropMode) {
            case Item -> drops
                .add(MaterialLibAPI.getStack(info.material, Shapes.rawOre, info.stoneType.isRich() ? 2 : 1));
            case FortuneItem -> {
                if (fortune > 0) {
                    // Max applicable fortune
                    if (fortune > 3) fortune = 3;

                    int addedDrops = random.nextInt(fortune + 2) - 1;
                    if (addedDrops < 0) addedDrops = 0;

                    int amount = (info.stoneType.isRich() ? 2 : 1) * (addedDrops + 1);

                    for (int i = 0; i < amount; i++) {
                        drops.add(MaterialLibAPI.getStack(info.material, Shapes.rawOre, 1));
                    }
                } else {
                    for (int i = 0; i < (info.stoneType.isRich() ? 2 : 1); i++) {
                        drops.add(MaterialLibAPI.getStack(info.material, Shapes.rawOre, 1));
                    }
                }
            }
            case UnifiedBlock -> {
                try (OreInfo info2 = info.clone()) {
                    for (int i = 0; i < (info2.stoneType.isRich() ? 2 : 1); i++) {
                        info2.stoneType = StoneType.Stone;
                        drops.add(getStack(info2, 1));
                    }
                }
            }
            case PerDimBlock -> {
                try (OreInfo info2 = info.clone()) {
                    if (!info2.stoneType.isDimensionSpecific()) {
                        info2.stoneType = StoneType.Stone;
                    }

                    drops.add(getStack(info2, 1));
                }
            }
            case Block -> {
                try (OreInfo info2 = info.clone()) {
                    drops.add(getStack(info2, 1));
                }
            }
        }

        return drops;
    }
}
