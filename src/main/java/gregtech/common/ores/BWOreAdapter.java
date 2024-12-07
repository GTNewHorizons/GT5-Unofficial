package gregtech.common.ores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import bartworks.system.material.BWMetaGeneratedOres;
import bartworks.system.material.BWMetaGeneratedSmallOres;
import bartworks.system.material.Werkstoff;
import bartworks.system.material.WerkstoffLoader;
import gregtech.GTMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.common.GTProxy.OreDropSystem;
import it.unimi.dsi.fastutil.objects.ObjectIntPair;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public enum BWOreAdapter implements IOreAdapter<Werkstoff> {
    INSTANCE;

    @Override
    public boolean supports(Block block, int meta) {
        return block instanceof BWMetaGeneratedOres;
    }

    @Override
    public boolean supports(OreInfo<?> info) {
        if (info.stoneType != null && info.stoneType != StoneType.Stone) return false;
        if (!(info.material instanceof Werkstoff w)) return false;
        if (!w.hasItemType(OrePrefixes.ore)) return false;
        if ((w.getGenerationFeatures().blacklist & 0b1000) != 0) return false;

        return true;
    }

    @Override
    public OreInfo<Werkstoff> getOreInfo(Block block, int meta) {
        if (!supports(block, meta)) return null;

        OreInfo<Werkstoff> info = OreInfo.getNewInfo();

        info.stoneType = StoneType.Stone;
        info.material = Werkstoff.werkstoffHashMap.get((Short) (short) meta);
        info.isSmall = block instanceof BWMetaGeneratedSmallOres;
        info.isNatural = ((BWMetaGeneratedOres) block).isNatural;

        return info;
    }

    @Override
    public ObjectIntPair<Block> getBlock(OreInfo<?> info) {
        if (info.stoneType != null && info.stoneType != StoneType.Stone) return null;
        if (!(info.material instanceof Werkstoff w)) return null;
        if (!w.hasItemType(OrePrefixes.ore)) return null;
        if ((w.getGenerationFeatures().blacklist & 0b1000) != 0) return null;

        Block block;

        if (info.isSmall) {
            if (info.isNatural) {
                block = WerkstoffLoader.BWSmallOresNatural;
            } else {
                block = WerkstoffLoader.BWSmallOres;
            }
        } else {
            if (info.isNatural) {
                block = WerkstoffLoader.BWOresNatural;
            } else {
                block = WerkstoffLoader.BWOres;
            }
        }

        return ObjectIntPair.of(block, w.getmID());
    }

    @Override
    public List<ItemStack> getOreDrops(OreInfo<?> info2, boolean silktouch, int fortune) {
        if (!supports(info2)) return null;

        @SuppressWarnings("unchecked")
        OreInfo<Werkstoff> info = (OreInfo<Werkstoff>) info2;

        if (info.stoneType == null) info.stoneType = StoneType.Stone;
        if (info.stoneType != StoneType.Stone) return null;

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
        OreInfo<Werkstoff> info = (OreInfo<Werkstoff>) info2;

        if (info.isSmall) {
            List<ItemId> drops = new ArrayList<>();

            for (ItemStack stack : SmallOreDrops.getDropList(info.material.getBridgeMaterial())) {
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

    public ArrayList<ItemStack> getSmallOreDrops(Random random, OreInfo<Werkstoff> info, int fortune) {
        Materials bridge = info.material.getBridgeMaterial();

        ArrayList<ItemStack> possibleDrops = SmallOreDrops.getDropList(bridge);
        ArrayList<ItemStack> drops = new ArrayList<>();

        if (!possibleDrops.isEmpty()) {
            int dropCount = Math.max(1, bridge.mOreMultiplier + (fortune > 0 ? random.nextInt(1 + fortune * bridge.mOreMultiplier) : 0) / 2);

            for (int i = 0; i < dropCount; i++) {
                drops.add(GTUtility.copyAmount(1, possibleDrops.get(random.nextInt(possibleDrops.size()))));
            }
        }

        if (random.nextInt(3 + fortune) > 1) {
            drops.add(info.stoneType.getDust(random.nextInt(3) == 0, 1));
        }

        return drops;
    }

    public ArrayList<ItemStack> getBigOreDrops(Random random, OreDropSystem oreDropMode, OreInfo<Werkstoff> info, int fortune) {
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
                OreInfo<Werkstoff> info2 = info.clone();

                for (int i = 0; i < (info.stoneType.isRich() ? 2 : 1); i++) {
                    info.stoneType = StoneType.Stone;
                    drops.add(getStack(info, 1));
                }

                info2.release();
            }
            case PerDimBlock -> {
                OreInfo<Werkstoff> info2 = info.clone();

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
