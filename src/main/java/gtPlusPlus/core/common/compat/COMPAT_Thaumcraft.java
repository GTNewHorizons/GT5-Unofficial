package gtPlusPlus.core.common.compat;

import static gregtech.api.enums.Mods.ForbiddenMagic;

import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE.ConfigSwitches;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class COMPAT_Thaumcraft {

    public static void OreDict() {

        if (ConfigSwitches.enableThaumcraftShardUnification) {
            run();
        }
    }

    private static void run() {

        for (int i = 0; i <= 6; i++) {
            ItemUtils.getItemForOreDict("Thaumcraft:ItemShard", "shardAny", "TC Shard " + i, i);
            GT_OreDictUnificator
                    .registerOre("shardAny", ItemUtils.getItemStackFromFQRN("Thaumcraft:ItemShard:" + i, 1));
            ItemUtils.getItemForOreDict("Thaumcraft:ItemShard", "gemInfusedAnything", "TC Shard " + i, i);
            GT_OreDictUnificator
                    .registerOre("gemInfusedAnything", ItemUtils.getItemStackFromFQRN("Thaumcraft:ItemShard:" + i, 1));
        }

        if (ForbiddenMagic.isModLoaded()) {
            for (int i = 0; i <= 6; i++) {
                ItemUtils.getItemForOreDict("ForbiddenMagic:NetherShard", "shardAny", "FM Shard " + i, i);
                GT_OreDictUnificator
                        .registerOre("shardAny", ItemUtils.getItemStackFromFQRN("ForbiddenMagic:NetherShard:" + i, 1));
                ItemUtils.getItemForOreDict("ForbiddenMagic:NetherShard", "gemInfusedAnything", "FM Shard " + i, i);
                GT_OreDictUnificator.registerOre(
                        "gemInfusedAnything",
                        ItemUtils.getItemStackFromFQRN("ForbiddenMagic:NetherShard:" + i, 1));
            }
            ItemUtils.getItemForOreDict("ForbiddenMagic:GluttonyShard", "shardAny", "FM Gluttony Shard", 0);
            GT_OreDictUnificator
                    .registerOre("shardAny", ItemUtils.getItemStackFromFQRN("ForbiddenMagic:GluttonyShard", 1));
            ItemUtils.getItemForOreDict("ForbiddenMagic:GluttonyShard", "gemInfusedAnything", "FM Gluttony Shard", 0);
            GT_OreDictUnificator.registerOre(
                    "gemInfusedAnything",
                    ItemUtils.getItemStackFromFQRN("ForbiddenMagic:GluttonyShard", 1));
        }
    }
}
