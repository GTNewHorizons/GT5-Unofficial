package gtPlusPlus.core.common.compat;

import static gregtech.api.enums.Mods.ForbiddenMagic;

import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.core.lib.GTPPCore.ConfigSwitches;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class CompatThaumcraft {

    public static void OreDict() {

        if (ConfigSwitches.enableThaumcraftShardUnification) {
            run();
        }
    }

    private static void run() {

        for (int i = 0; i <= 6; i++) {
            ItemUtils.getItemForOreDict("Thaumcraft:ItemShard", "shardAny", "TC Shard " + i, i);
            GTOreDictUnificator.registerOre("shardAny", ItemUtils.getItemStackFromFQRN("Thaumcraft:ItemShard:" + i, 1));
            ItemUtils.getItemForOreDict("Thaumcraft:ItemShard", "gemInfusedAnything", "TC Shard " + i, i);
            GTOreDictUnificator
                .registerOre("gemInfusedAnything", ItemUtils.getItemStackFromFQRN("Thaumcraft:ItemShard:" + i, 1));
        }

        if (ForbiddenMagic.isModLoaded()) {
            for (int i = 0; i <= 6; i++) {
                ItemUtils.getItemForOreDict("ForbiddenMagic:NetherShard", "shardAny", "FM Shard " + i, i);
                GTOreDictUnificator
                    .registerOre("shardAny", ItemUtils.getItemStackFromFQRN("ForbiddenMagic:NetherShard:" + i, 1));
                ItemUtils.getItemForOreDict("ForbiddenMagic:NetherShard", "gemInfusedAnything", "FM Shard " + i, i);
                GTOreDictUnificator.registerOre(
                    "gemInfusedAnything",
                    ItemUtils.getItemStackFromFQRN("ForbiddenMagic:NetherShard:" + i, 1));
            }
            ItemUtils.getItemForOreDict("ForbiddenMagic:GluttonyShard", "shardAny", "FM Gluttony Shard", 0);
            GTOreDictUnificator
                .registerOre("shardAny", ItemUtils.getItemStackFromFQRN("ForbiddenMagic:GluttonyShard", 1));
            ItemUtils.getItemForOreDict("ForbiddenMagic:GluttonyShard", "gemInfusedAnything", "FM Gluttony Shard", 0);
            GTOreDictUnificator
                .registerOre("gemInfusedAnything", ItemUtils.getItemStackFromFQRN("ForbiddenMagic:GluttonyShard", 1));
        }
    }
}
