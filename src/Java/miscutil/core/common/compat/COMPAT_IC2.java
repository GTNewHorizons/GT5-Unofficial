package miscutil.core.common.compat;

import static miscutil.core.common.compat.COMPAT_HANDLER.RemoveRecipeQueue;
import miscutil.core.util.UtilsItems;
import net.minecraft.item.ItemStack;


public class COMPAT_IC2 {

	private static ItemStack temp_1;
	private static ItemStack temp_2;
	private static ItemStack temp_3;
	private static ItemStack temp_4;
	private static ItemStack temp_5;
	
	
	
	public static void OreDict(){
		//Get ItemStacks for results
		temp_1 = UtilsItems.getItemStack("IC2:itemCropnalyzer", 1);	
		temp_2 = UtilsItems.getItemStack("IC2:itemSolarHelmet", 1);	
		run();
	}
	
	private static final void run(){
		//Fuck these right off.
		RemoveRecipeQueue.add("IC2:itemCable");
		RemoveRecipeQueue.add("IC2:itemCable:1");
		RemoveRecipeQueue.add("IC2:itemCable:2");
		RemoveRecipeQueue.add("IC2:itemCable:3");
		RemoveRecipeQueue.add("IC2:itemCable:5");
		RemoveRecipeQueue.add("IC2:itemCable:6");
		RemoveRecipeQueue.add("IC2:itemCable:10");
		RemoveRecipeQueue.add("IC2:itemCable:13");
		
		RemoveRecipeQueue.add(temp_1);
		UtilsItems.recipeBuilder("ore:craftingWireCopper", "ore:craftingWireCopper", null, 
				"minecraft:redstone", "minecraft:glass", "minecraft:redstone", 
				"minecraft:redstone", "ore:circuitBasic", "minecraft:redstone", 
				temp_1);
		RemoveRecipeQueue.add(temp_2);
		UtilsItems.recipeBuilder("ore:plateIron", "ore:plateIron", "ore:plateIron", 
				"ore:plateIron", "gregtech:gt.metaitem.01:32750", "ore:plateIron", 
				"ore:craftingWireCopper", "ore:craftingWireCopper", "ore:craftingWireCopper",
				temp_2);
	}
	
}
