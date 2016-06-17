package miscutil.core.common.compat;


import static miscutil.core.handler.COMPAT_HANDLER.AddRecipeQueue;
import static miscutil.core.handler.COMPAT_HANDLER.RemoveRecipeQueue;
import miscutil.core.util.UtilsItems;
import miscutil.core.util.recipe.ShapedRecipeObject;
import net.minecraft.item.ItemStack;


public class COMPAT_IC2 {

	private static ItemStack itemCropnalyzer = UtilsItems.getItemStack("IC2:itemCropnalyzer", 1);
	private static ItemStack itemSolarHelmet = UtilsItems.getItemStack("IC2:itemSolarHelmet", 1);
	private static ItemStack temp_3;
	private static ItemStack temp_4;
	private static ItemStack temp_5;
	public static ShapedRecipeObject Cropnalyzer = new ShapedRecipeObject(
			"ore:cableGt02Copper", "ore:cableGt02Copper", null, 
			"minecraft:redstone", "ore:blockGlass", "minecraft:redstone", 
			"minecraft:redstone", "ore:circuitBasic", "minecraft:redstone", 
			itemCropnalyzer);
	public static ShapedRecipeObject SolarHelmet = new ShapedRecipeObject(
			"ore:plateIron", "ore:plateIron", "ore:plateIron", 
			"ore:plateIron", "gregtech:gt.metaitem.01:32750", "ore:plateIron", 
			"ore:craftingWireCopper", "ore:craftingWireCopper", "ore:craftingWireCopper",
			itemSolarHelmet);
	
	
	
	public static void OreDict(){
		//Get ItemStacks for results
		/*itemCropnalyzer = UtilsItems.getItemStack("IC2:itemCropnalyzer", 1);	
		itemSolarHelmet = UtilsItems.getItemStack("IC2:itemSolarHelmet", 1);	*/
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
		RemoveRecipeQueue.add(itemCropnalyzer);		
		RemoveRecipeQueue.add(itemSolarHelmet);
		
		AddRecipeQueue.add(Cropnalyzer);
		AddRecipeQueue.add(SolarHelmet);
	}
	
}
