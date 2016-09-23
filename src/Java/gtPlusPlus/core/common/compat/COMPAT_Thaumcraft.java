package gtPlusPlus.core.common.compat;

import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.lib.CORE.configSwitches;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.item.UtilsItems;

public class COMPAT_Thaumcraft {

	public static void OreDict(){
		
		if (configSwitches.enableThaumcraftShardUnification){
			run();
		}
	}

	private static final void run(){

		for(int i=0; i<=6; i++){
			//Utils.LOG_INFO(""+i);
			UtilsItems.getItemForOreDict("Thaumcraft:ItemShard", "shardAny", "TC Shard "+i, i);
			GT_OreDictUnificator.registerOre("shardAny", UtilsItems.getItemStack("Thaumcraft:ItemShard:"+i, 1));
			UtilsItems.getItemForOreDict("Thaumcraft:ItemShard", "gemInfusedAnything", "TC Shard "+i, i);
			GT_OreDictUnificator.registerOre("gemInfusedAnything", UtilsItems.getItemStack("Thaumcraft:ItemShard:"+i, 1));
			//System.out.println("TC Shard registration count is: "+i);        
		}

		if (LoadedMods.ForbiddenMagic){
			for(int i=0; i<=6; i++){
				//Utils.LOG_INFO(""+i);
				UtilsItems.getItemForOreDict("ForbiddenMagic:NetherShard", "shardAny", "FM Shard "+i, i);
				GT_OreDictUnificator.registerOre("shardAny", UtilsItems.getItemStack("ForbiddenMagic:NetherShard:"+i, 1));
				UtilsItems.getItemForOreDict("ForbiddenMagic:NetherShard", "gemInfusedAnything", "FM Shard "+i, i);
				GT_OreDictUnificator.registerOre("gemInfusedAnything", UtilsItems.getItemStack("ForbiddenMagic:NetherShard:"+i, 1));
				//System.out.println("TC Shard registration count is: "+i);        
			}
			UtilsItems.getItemForOreDict("ForbiddenMagic:GluttonyShard", "shardAny", "FM Gluttony Shard", 0);
			GT_OreDictUnificator.registerOre("shardAny", UtilsItems.getItemStack("ForbiddenMagic:GluttonyShard", 1));
			UtilsItems.getItemForOreDict("ForbiddenMagic:GluttonyShard", "gemInfusedAnything", "FM Gluttony Shard", 0);
			GT_OreDictUnificator.registerOre("gemInfusedAnything", UtilsItems.getItemStack("ForbiddenMagic:GluttonyShard", 1));	
		}
	}

}
