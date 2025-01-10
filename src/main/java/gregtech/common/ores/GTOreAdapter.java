package gregtech.common.ores;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.gtnhlib.util.data.BlockMeta;
import com.gtnewhorizon.gtnhlib.util.data.ImmutableBlockMeta;
import com.gtnewhorizons.postea.api.ItemStackReplacementManager;
import com.gtnewhorizons.postea.api.TileEntityReplacementManager;
import com.gtnewhorizons.postea.utility.BlockInfo;

import codechicken.nei.api.API;
import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneCategory;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.common.GTProxy.OreDropSystem;
import gregtech.common.blocks.BlockOresAbstract;

public final class GTOreAdapter implements IOreAdapter<Materials> {

    public static GTOreAdapter INSTANCE = new GTOreAdapter();

    private GTOreAdapter() {}

    private final Map<StoneType, BlockOresAbstract> oreBlocksByStoneType = new EnumMap<>(StoneType.class);

    private BlockOresAbstract[] ores;

    public void init() {
        // spotless:off
        BlockOresAbstract ores1 = new BlockOresAbstract(2, new StoneType[] {
            StoneType.Stone,
            StoneType.Netherrack,
            StoneType.Endstone,
            StoneType.BlackGranite,
            StoneType.RedGranite,
            StoneType.Marble,
            StoneType.Basalt,
            StoneType.Moon,
        });
        BlockOresAbstract ores2 = new BlockOresAbstract(3, new StoneType[] {
            StoneType.Mars,
            StoneType.Asteroid,
            StoneType.Phobos,
            StoneType.Deimos,
            StoneType.Ceres,
            StoneType.Io,
            StoneType.Europa,
            StoneType.Ganymede,
        });
        BlockOresAbstract ores3 = new BlockOresAbstract(4, new StoneType[] {
            StoneType.Callisto,
            StoneType.Enceladus,
            StoneType.Titan,
            StoneType.Miranda,
            StoneType.Oberon,
            StoneType.Proteus,
            StoneType.Triton,
            StoneType.Pluto,
        });
        BlockOresAbstract ores4 = new BlockOresAbstract(5, new StoneType[] {
            StoneType.Callisto,
            StoneType.Enceladus,
            StoneType.Titan,
            StoneType.Miranda,
            StoneType.Oberon,
            StoneType.Proteus,
            StoneType.Triton,
            StoneType.Pluto,
        });
        BlockOresAbstract ores5 = new BlockOresAbstract(6, new StoneType[] {
            StoneType.Haumea,
            StoneType.MakeMake,
            StoneType.AlphaCentauri,
            StoneType.TCetiE,
            StoneType.VegaB,
            StoneType.BarnardaE,
            StoneType.BarnardaF,
            StoneType.Horus,
        });
        BlockOresAbstract ores6 = new BlockOresAbstract(7, new StoneType[] {
            StoneType.AnubisAndMaahes,
            StoneType.PackedIce,
            StoneType.SethIce,
            StoneType.SethClay,
        });

        ores = new BlockOresAbstract[] { ores1, ores2, ores3, ores4, ores5, ores6 };

        StoneType[] legacyStones = {
            StoneType.Stone,
            StoneType.Netherrack,
            StoneType.Endstone,
            StoneType.BlackGranite,
            StoneType.RedGranite,
            StoneType.Marble,
            StoneType.Basalt,
        };
        // spotless:on

        TileEntityReplacementManager.tileEntityTransformer("GT_TileEntity_Ores", (tag, world) -> {
            int meta = tag.getInteger("m");
            boolean natural = tag.getBoolean("n");

            try (OreInfo<Materials> info = OreInfo.getNewInfo()) {
                info.stoneType = GTUtility.getIndexSafe(legacyStones, (meta % 16000) / 1000);
                info.material = GregTechAPI.sGeneratedMaterials[meta % 1000];
                info.isSmall = meta >= 16000;
                info.isNatural = natural;

                if (!INSTANCE.supports(info)) {
                    return new BlockInfo(Blocks.air, 0);
                } else {
                    ImmutableBlockMeta bm = INSTANCE.getBlock(info);

                    return new BlockInfo(bm.getBlock(), bm.getBlockMeta());
                }
            }
        });

        ItemStackReplacementManager.addItemReplacement("gregtech:gt.blockores", (tag) -> {
            int itemId = Item.getIdFromItem(Item.getItemFromBlock(ores1));
            tag.setInteger("id", itemId);
            return tag;
        });
    }

    public void registerOre(StoneType stoneType, BlockOresAbstract oreBlock) {
        oreBlocksByStoneType.put(stoneType, oreBlock);
    }

    public void hideOres() {
        for (BlockOresAbstract ore : ores) {
            API.hideItem(new ItemStack(ore, 1, 0));
        }
    }

    @Override
    public boolean supports(Block block, int meta) {
        return GTUtility.contains(ores, block);
    }

    @Override
    public boolean supports(OreInfo<?> info) {
        if (!(info.material instanceof Materials gtMat)) return false;
        if (!OrePrefixes.ore.doGenerateItem(gtMat)) return false;

        IStoneType stoneType = info.stoneType == null ? gtMat.getValidStones()
            .get(0) : info.stoneType;

        if (!(stoneType instanceof StoneType stoneType2)) return false;
        if (!oreBlocksByStoneType.containsKey(stoneType2)) return false;
        if (!stoneType2.isEnabled()) return false;
        if (!gtMat.isValidForStone(stoneType2)) return false;
        if (stoneType2.getCategory() == StoneCategory.Ice && info.isSmall) return false;

        return true;
    }

    public static final int SMALL_ORE_META_OFFSET = 16000;
    public static final int NATURAL_ORE_META_OFFSET = 8000;

    @Override
    public OreInfo<Materials> getOreInfo(Block block, int meta) {
        if (!(block instanceof BlockOresAbstract oreBlock)) return null;

        if (meta < 0) throw new IllegalArgumentException(
            "illegal metadata: " + meta + "; a tool may be casting an int to a byte, which is incompatible with NEID");

        int matId = meta % 1000;
        int stoneId = ((meta % SMALL_ORE_META_OFFSET) % NATURAL_ORE_META_OFFSET) / 1000;
        boolean small = meta >= SMALL_ORE_META_OFFSET;
        boolean natural = (meta % SMALL_ORE_META_OFFSET) >= NATURAL_ORE_META_OFFSET;

        Materials mat = GregTechAPI.sGeneratedMaterials[matId];

        if (!OrePrefixes.ore.doGenerateItem(mat)) return null;

        StoneType stoneType = GTUtility.getIndexSafe(oreBlock.stoneTypes, stoneId);
        if (stoneType == null || !stoneType.isEnabled()) return null;
        if (stoneType.getCategory() == StoneCategory.Ice && small) return null;

        OreInfo<Materials> info = OreInfo.getNewInfo();

        info.material = mat;
        info.stoneType = stoneType;
        info.isSmall = small;
        info.isNatural = natural;

        return info;
    }

    @Override
    public ImmutableBlockMeta getBlock(OreInfo<?> info) {
        if (info.stoneType == null) info.stoneType = StoneType.Stone;

        if (!(info.material instanceof Materials gtMat)) return null;
        if (!OrePrefixes.ore.doGenerateItem(gtMat)) return null;

        if (!(info.stoneType instanceof StoneType stoneType)) return null;
        if (!stoneType.isEnabled()) return null;
        if (stoneType.getCategory() == StoneCategory.Ice && info.isSmall) return null;

        BlockOresAbstract oreBlock = oreBlocksByStoneType.get(stoneType);

        if (oreBlock == null) return null;
        int stoneIndex = oreBlock.stoneTypes.indexOf(stoneType);
        if (stoneIndex == -1) return null;

        int meta = gtMat.mMetaItemSubID;
        meta += stoneIndex * 1000;
        if (info.isSmall) meta += SMALL_ORE_META_OFFSET;
        if (info.isNatural) meta += NATURAL_ORE_META_OFFSET;

        return new BlockMeta(oreBlock, meta);
    }

    @Override
    public ArrayList<ItemStack> getOreDrops(Random random, OreInfo<?> info2, boolean silktouch, int fortune) {
        if (!supports(info2)) return new ArrayList<>();

        @SuppressWarnings("unchecked")
        OreInfo<Materials> info = (OreInfo<Materials>) info2;

        if (info.stoneType == null) info.stoneType = StoneType.Stone;

        BlockOresAbstract oreBlock = oreBlocksByStoneType.get(info.stoneType);

        if (oreBlock == null) return new ArrayList<>();

        if (!info.isNatural) fortune = 0;

        if (info.isSmall) {
            return getSmallOreDrops(random, info, fortune);
        } else {
            OreDropSystem oreDropSystem = GTMod.gregtechproxy.oreDropSystem;

            if (silktouch) oreDropSystem = OreDropSystem.Block;

            return getBigOreDrops(random, oreDropSystem, info, fortune);
        }
    }

    @Override
    public List<ItemStack> getPotentialDrops(OreInfo<?> info2) {
        if (!supports(info2)) return new ArrayList<>();

        @SuppressWarnings("unchecked")
        OreInfo<Materials> info = (OreInfo<Materials>) info2;

        if (info.isSmall) {
            ArrayList<ItemId> drops = new ArrayList<>();

            for (ItemStack stack : SmallOreDrops.getDropList(info.material)) {
                ItemId id = ItemId.create(stack);

                if (!drops.contains(id)) drops.add(id);
            }

            ArrayList<ItemStack> drops2 = new ArrayList<>();

            for (ItemId id : drops) {
                drops2.add(id.getItemStack());
            }

            return drops2;
        } else {
            return getBigOreDrops(ThreadLocalRandom.current(), GTMod.gregtechproxy.oreDropSystem, info, 0);
        }
    }

    private ArrayList<ItemStack> getSmallOreDrops(Random random, OreInfo<Materials> info, int fortune) {
        ArrayList<ItemStack> possibleDrops = SmallOreDrops.getDropList(info.material);
        ArrayList<ItemStack> drops = new ArrayList<>();

        if (!possibleDrops.isEmpty()) {
            int dropCount = Math.max(
                1,
                info.material.mOreMultiplier
                    + (fortune > 0 ? random.nextInt(1 + fortune * info.material.mOreMultiplier) : 0) / 2);

            for (int i = 0; i < dropCount; i++) {
                drops.add(GTUtility.copyAmount(1, possibleDrops.get(random.nextInt(possibleDrops.size()))));
            }
        }

        if (random.nextInt(3 + fortune) > 1) {
            drops.add(info.stoneType.getDust(random.nextInt(3) == 0, 1));
        }

        return drops;
    }

    private ArrayList<ItemStack> getBigOreDrops(Random random, OreDropSystem oreDropMode, OreInfo<Materials> info,
        int fortune) {
        ArrayList<ItemStack> drops = new ArrayList<>();

        // For Sake of god of balance!

        switch (oreDropMode) {
            case Item -> {
                drops.add(GTOreDictUnificator.get(OrePrefixes.rawOre, info.material, info.stoneType.isRich() ? 2 : 1));
            }
            case FortuneItem -> {
                if (fortune > 0) {
                    // Max applicable fortune
                    if (fortune > 3) fortune = 3;

                    int amount = 1 + Math.max(random.nextInt(fortune * (info.stoneType.isRich() ? 2 : 1) + 2) - 1, 0);

                    for (int i = 0; i < amount; i++) {
                        drops.add(GTOreDictUnificator.get(OrePrefixes.rawOre, info.material, 1));
                    }
                } else {
                    for (int i = 0; i < (info.stoneType.isRich() ? 2 : 1); i++) {
                        drops.add(GTOreDictUnificator.get(OrePrefixes.rawOre, info.material, 1));
                    }
                }
            }
            case UnifiedBlock -> {
                try (OreInfo<Materials> info2 = info.clone()) {
                    info2.isNatural = false;

                    for (int i = 0; i < (info2.stoneType.isRich() ? 2 : 1); i++) {
                        info2.stoneType = StoneType.Stone;
                        drops.add(getStack(info2, 1));
                    }
                }
            }
            case PerDimBlock -> {
                try (OreInfo<Materials> info2 = info.clone()) {
                    info2.isNatural = false;

                    if (!info2.stoneType.isDimensionSpecific()) {
                        info2.stoneType = StoneType.Stone;
                    }

                    drops.add(getStack(info2, 1));
                }
            }
            case Block -> {
                try (OreInfo<Materials> info2 = info.clone()) {
                    info2.isNatural = false;

                    drops.add(getStack(info2, 1));
                }
            }
        }

        return drops;
    }
}
