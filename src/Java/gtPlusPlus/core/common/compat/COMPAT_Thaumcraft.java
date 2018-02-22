package gtPlusPlus.core.common.compat;

import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE.ConfigSwitches;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.lib.LoadedMods;

public class COMPAT_Thaumcraft {

	public static void OreDict(){

		if (ConfigSwitches.enableThaumcraftShardUnification){
			run();
		}
	}

	private static final void run(){

		for(int i=0; i<=6; i++){
			//Utils.LOG_INFO(""+i);
			ItemUtils.getItemForOreDict("Thaumcraft:ItemShard", "shardAny", "TC Shard "+i, i);
			GT_OreDictUnificator.registerOre("shardAny", ItemUtils.getItemStack("Thaumcraft:ItemShard:"+i, 1));
			ItemUtils.getItemForOreDict("Thaumcraft:ItemShard", "gemInfusedAnything", "TC Shard "+i, i);
			GT_OreDictUnificator.registerOre("gemInfusedAnything", ItemUtils.getItemStack("Thaumcraft:ItemShard:"+i, 1));
			//System.out.println("TC Shard registration count is: "+i);
		}

		if (LoadedMods.ForbiddenMagic){
			for(int i=0; i<=6; i++){
				//Utils.LOG_INFO(""+i);
				ItemUtils.getItemForOreDict("ForbiddenMagic:NetherShard", "shardAny", "FM Shard "+i, i);
				GT_OreDictUnificator.registerOre("shardAny", ItemUtils.getItemStack("ForbiddenMagic:NetherShard:"+i, 1));
				ItemUtils.getItemForOreDict("ForbiddenMagic:NetherShard", "gemInfusedAnything", "FM Shard "+i, i);
				GT_OreDictUnificator.registerOre("gemInfusedAnything", ItemUtils.getItemStack("ForbiddenMagic:NetherShard:"+i, 1));
				//System.out.println("TC Shard registration count is: "+i);
			}
			ItemUtils.getItemForOreDict("ForbiddenMagic:GluttonyShard", "shardAny", "FM Gluttony Shard", 0);
			GT_OreDictUnificator.registerOre("shardAny", ItemUtils.getItemStack("ForbiddenMagic:GluttonyShard", 1));
			ItemUtils.getItemForOreDict("ForbiddenMagic:GluttonyShard", "gemInfusedAnything", "FM Gluttony Shard", 0);
			GT_OreDictUnificator.registerOre("gemInfusedAnything", ItemUtils.getItemStack("ForbiddenMagic:GluttonyShard", 1));
		}
	}

}
