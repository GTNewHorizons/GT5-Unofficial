package gregtech.common.ores;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.common.GTProxy.OreDropSystem;
import gregtech.common.blocks.BlockOresAbstract;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public enum GTOreAdapter implements IOreAdapter<Materials> {
    INSTANCE;

    private final Map<StoneType, BlockOresAbstract> oreBlocksByStoneType = new EnumMap<>(StoneType.class);

    public BlockOresAbstract ores1, ores2, ores3, ores4, ores5, ores6;

    public BlockOresAbstract[] ores;

    public void init() {
        ores1 = new BlockOresAbstract(2, new StoneType[] {
            StoneType.Stone,
            StoneType.Netherrack,
            StoneType.Endstone,
            StoneType.BlackGranite,
            StoneType.RedGranite,
            StoneType.Marble,
            StoneType.Basalt,
            StoneType.Moon,
        });
        ores2 = new BlockOresAbstract(3, new StoneType[] {
            StoneType.Mars,
            StoneType.Asteroid,
            StoneType.Phobos,
            StoneType.Deimos,
            StoneType.Ceres,
            StoneType.Io,
            StoneType.Europa,
            StoneType.Ganymede,
        });
        ores3 = new BlockOresAbstract(4, new StoneType[] {
            StoneType.Callisto,
            StoneType.Enceladus,
            StoneType.Titan,
            StoneType.Miranda,
            StoneType.Oberon,
            StoneType.Proteus,
            StoneType.Triton,
            StoneType.Pluto,
        });
        ores4 = new BlockOresAbstract(5, new StoneType[] {
            StoneType.Callisto,
            StoneType.Enceladus,
            StoneType.Titan,
            StoneType.Miranda,
            StoneType.Oberon,
            StoneType.Proteus,
            StoneType.Triton,
            StoneType.Pluto,
        });
        ores5 = new BlockOresAbstract(6, new StoneType[] {
            StoneType.Haumea,
            StoneType.MakeMake,
            StoneType.AlphaCentauri,
            StoneType.TCetiE,
            StoneType.VegaB,
            StoneType.BarnardaE,
            StoneType.BarnardaF,
            StoneType.Horus,
        });
        ores6 = new BlockOresAbstract(7, new StoneType[] {
            StoneType.Anubis,
            StoneType.PackedIce,
        });

        ores = new BlockOresAbstract[] { ores1, ores2, ores3, ores4, ores5, ores6 };
    }

    public void registerOre(StoneType stoneType, BlockOresAbstract oreBlock) {
        oreBlocksByStoneType.put(stoneType, oreBlock);
    }

    @Override
    public boolean supports(Block block, int meta) {
        return GTUtility.contains(ores, block);
    }

    @Override
    public boolean supports(OreInfo<?> info) {
        if (!(info.material instanceof Materials gtMat)) return false;
        if (!OrePrefixes.ore.doGenerateItem(gtMat)) return false;

        IStoneType stoneType = info.stoneType == null ? gtMat.getValidStones().get(0) : info.stoneType;

        if (!(stoneType instanceof StoneType stoneType2)) return false;
        if (!oreBlocksByStoneType.containsKey(stoneType2)) return false;
        if (!stoneType2.isEnabled()) return false;
        if (!info.material.isValidForStone(stoneType2)) return false;

        return true;
    }

    public static final int SMALL_ORE_META_OFFSET = 16000;
    public static final int NATURAL_ORE_META_OFFSET = 8000;

    @Override
    public OreInfo<Materials> getOreInfo(Block block, int meta) {
        if (!(block instanceof BlockOresAbstract oreBlock)) return null;

        int matId = meta % 1000;
        int stoneId = ((meta % SMALL_ORE_META_OFFSET) % NATURAL_ORE_META_OFFSET) / 1000;
        boolean small = meta >= SMALL_ORE_META_OFFSET;
        boolean natural = (meta % SMALL_ORE_META_OFFSET) >= NATURAL_ORE_META_OFFSET;

        Materials mat = GregTechAPI.sGeneratedMaterials[matId];

        if (!OrePrefixes.ore.doGenerateItem(mat)) return null;

        StoneType stoneType = GTUtility.getIndexSafe(oreBlock.stoneTypes, stoneId);
        if (stoneType == null || !stoneType.isEnabled()) return null;

        OreInfo<Materials> info = OreInfo.getNewInfo();

        info.material = mat;
        info.stoneType = stoneType;
        info.isSmall = small;
        info.isNatural = natural;

        return info;
    }

    @Override
    public ObjectIntPair<Block> getBlock(OreInfo<?> info) {
        if (info.stoneType == null) info.stoneType = StoneType.Stone;

        if (!(info.material instanceof Materials gtMat)) return null;
        if (!OrePrefixes.ore.doGenerateItem(gtMat)) return null;
        
        if (!(info.stoneType instanceof StoneType stoneType)) return null;
        if (!stoneType.isEnabled()) return null;

        BlockOresAbstract oreBlock = oreBlocksByStoneType.get(stoneType);

        if (oreBlock == null) return null;
        int stoneIndex = oreBlock.stoneTypes.indexOf(stoneType);
        if (stoneIndex == -1) return null;

        int meta = gtMat.mMetaItemSubID;
        meta += stoneIndex * 1000;
        if (info.isSmall) meta += SMALL_ORE_META_OFFSET;
        if (info.isNatural) meta += NATURAL_ORE_META_OFFSET;

        return ObjectIntPair.of(oreBlock, meta);
    }

    @Override
    public List<ItemStack> getOreDrops(OreInfo<?> info2, boolean silktouch, int fortune) {
        if (!supports(info2)) return null;

        @SuppressWarnings("unchecked")
        OreInfo<Materials> info = (OreInfo<Materials>) info2;

        if (info.stoneType == null) info.stoneType = StoneType.Stone;

        BlockOresAbstract oreBlock = oreBlocksByStoneType.get(info.stoneType);

        if (oreBlock == null) return null;

        if (!info.isNatural) fortune = 0;

        if (info.isSmall) {
            return getSmallOreDrops(ThreadLocalRandom.current(), info, fortune);
        } else {
            OreDropSystem oreDropSystem = GTMod.gregtechproxy.oreDropSystem;
    
            if (silktouch) oreDropSystem = OreDropSystem.Block;
    
            return getBigOreDrops(ThreadLocalRandom.current(), oreDropSystem, info, fortune);
        }
    }
    
    @Override
    public List<ItemStack> getPotentialDrops(OreInfo<?> info2) {
        if (!supports(info2)) return null;

        @SuppressWarnings("unchecked")
        OreInfo<Materials> info = (OreInfo<Materials>) info2;

        if (info.isSmall) {
            List<ItemId> drops = new ArrayList<>();

            for (ItemStack stack : SmallOreDrops.getDropList(info.material)) {
                ItemId id = ItemId.create(stack);

                if (!drops.contains(id)) drops.add(id);
            }

            List<ItemStack> drops2 = new ArrayList<>();

            for (ItemId id : drops) {
                drops2.add(id.getItemStack());
            }

            return drops2;
        } else {
            return getBigOreDrops(ThreadLocalRandom.current(), GTMod.gregtechproxy.oreDropSystem, info, 0);
        }
    }

    public ArrayList<ItemStack> getSmallOreDrops(Random random, OreInfo<Materials> info, int fortune) {
        ArrayList<ItemStack> possibleDrops = SmallOreDrops.getDropList(info.material);
        ArrayList<ItemStack> drops = new ArrayList<>();

        if (!possibleDrops.isEmpty()) {
            int dropCount = Math.max(1, info.material.mOreMultiplier + (fortune > 0 ? random.nextInt(1 + fortune * info.material.mOreMultiplier) : 0) / 2);

            for (int i = 0; i < dropCount; i++) {
                drops.add(GTUtility.copyAmount(1, possibleDrops.get(random.nextInt(possibleDrops.size()))));
            }
        }

        if (random.nextInt(3 + fortune) > 1) {
            drops.add(info.stoneType.getDust(random.nextInt(3) == 0, 1));
        }

        return drops;
    }

    public ArrayList<ItemStack> getBigOreDrops(Random random, OreDropSystem oreDropMode, OreInfo<Materials> info, int fortune) {
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
                OreInfo<Materials> info2 = info.clone();

                for (int i = 0; i < (info.stoneType.isRich() ? 2 : 1); i++) {
                    info.stoneType = StoneType.Stone;
                    drops.add(getStack(info, 1));
                }

                info2.release();
            }
            case PerDimBlock -> {
                OreInfo<Materials> info2 = info.clone();

                if (!info.stoneType.isDimensionSpecific()) {
                    info2.stoneType = StoneType.Stone;
                }

                drops.add(getStack(info, 1));

                info2.release();
            }
            case Block -> {
                drops.add(getStack(info, 1));
            }
        }

        return drops;
    }
}
