package miscutil.core.handler;

public class CraftingManager {

	public static void mainRegistry() {
		addCraftingRecipies();
		addSmeltingRecipies();
	}

	public static void addCraftingRecipies() {
		// Shaped Recipie
		//GameRegistry.addRecipe(new ItemStack(ModItems.tutPickaxe, 1), new Object[] { "###", " S ", " S ", '#', ModItems.tutItem, 'S', Items.stick });
		
		//Shapeless Recipie
		//GameRegistry.addShapelessRecipe(new ItemStack(ModItems.tutItem, 10), new Object[]{Blocks.dirt , Blocks.cobblestone});
	}

	public static void addSmeltingRecipies() {
		//GameRegistry.addSmelting(ModItems.tutItem, new ItemStack(Blocks.diamond_block, 5), 20.0F);
	}
}
