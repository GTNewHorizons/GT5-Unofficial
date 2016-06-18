package miscutil.core.common.compat;

import gregtech.api.util.GT_OreDictUnificator;
import miscutil.core.item.ModItems;
import miscutil.core.lib.LoadedMods;
import miscutil.core.util.item.UtilsItems;
import net.minecraft.item.ItemStack;

public class COMPAT_Thaumcraft {

	public static void OreDict(){
		run();
	}

	private static final void run(){
		UtilsItems.getItemForOreDict("Thaumcraft:ItemResource", "ingotVoidMetal", "Void Metal Ingot", 16);
		GT_OreDictUnificator.registerOre("plateVoidMetal", new ItemStack(ModItems.itemPlateVoidMetal));

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
