package gtPlusPlus.core.handler.workbench;

import java.util.*;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.world.World;

public class Workbench_CraftingHandler {

	private static final Workbench_CraftingHandler instance = new Workbench_CraftingHandler();
	private List recipes = new ArrayList();
	
	public static final Workbench_CraftingHandler getInstance() {
	return instance;
	}
	public Workbench_CraftingHandler() {
	
	//just a example recipe so you know how to add them
	addRecipe(new ItemStack(Blocks.iron_block), new Object[]
	{
	"###",
	"###",
	"###",
	Character.valueOf('#'), Items.iron_ingot
	});
	
	//another example Recipe, but shapeless
	addShapelessRecipe(new ItemStack(Items.cake),new Object[]{Items.stick});
	
	
	}
	
	void addRecipe(ItemStack par1ItemStack, Object par2ArrayOfObj[])
	{
		String s = "";
		int i = 0;
		int j = 0;
		int k = 0;

		if (par2ArrayOfObj[i] instanceof String[])
		{
			String as[] = (String[])par2ArrayOfObj[i++];

			for (int l = 0; l < as.length; l++)
			{
				String s2 = as[l];
				k++;
				j = s2.length();
				s = (new StringBuilder()).append(s).append(s2).toString();
			}
		}
		else
		{
			while (par2ArrayOfObj[i] instanceof String)
			{
				String s1 = (String)par2ArrayOfObj[i++];
				k++;
				j = s1.length();
				s = (new StringBuilder()).append(s).append(s1).toString();
			}
		}

		HashMap hashmap = new HashMap();

		for (; i < par2ArrayOfObj.length; i += 2)
		{
			Character character = (Character)par2ArrayOfObj[i];
			ItemStack itemstack = null;

			if (par2ArrayOfObj[i + 1] instanceof Item)
			{
				itemstack = new ItemStack((Item)par2ArrayOfObj[i + 1]);
			}
			else if (par2ArrayOfObj[i + 1] instanceof Block)
			{
				itemstack = new ItemStack((Block)par2ArrayOfObj[i + 1], 1, -1);
			}
			else if (par2ArrayOfObj[i + 1] instanceof ItemStack)
			{
				itemstack = (ItemStack)par2ArrayOfObj[i + 1];
			}

			hashmap.put(character, itemstack);
		}

		ItemStack aitemstack[] = new ItemStack[j * k];

		for (int i1 = 0; i1 < j * k; i1++)
		{
			char c = s.charAt(i1);

			if (hashmap.containsKey(Character.valueOf(c)))
			{
				aitemstack[i1] = ((ItemStack)hashmap.get(Character.valueOf(c))).copy();
			}
			else
			{
				aitemstack[i1] = null;
			}
		}

		recipes.add(new ShapedRecipes(j, k, aitemstack, par1ItemStack));
	}

	public void addShapelessRecipe(ItemStack par1ItemStack, Object par2ArrayOfObj[])
	{
		ArrayList arraylist = new ArrayList();
		Object aobj[] = par2ArrayOfObj;
		int i = aobj.length;

		for (int j = 0; j < i; j++)
		{
			Object obj = aobj[j];

			if (obj instanceof ItemStack)
			{
				arraylist.add(((ItemStack)obj).copy());
				continue;
			}

			if (obj instanceof Item)
			{
				arraylist.add(new ItemStack((Item)obj));
				continue;
			}

			if (obj instanceof Block)
			{
				arraylist.add(new ItemStack((Block)obj));
			}
			else
			{
				throw new RuntimeException("Invalid shapeless recipe!");
			}
		}

		recipes.add(new ShapelessRecipes(par1ItemStack, arraylist));
	}

	public ItemStack findMatchingRecipe(InventoryCrafting par1InventoryCrafting, World par2World)
	{
		int i = 0;
		ItemStack itemstack = null;
		ItemStack itemstack1 = null;

		for (int j = 0; j < par1InventoryCrafting.getSizeInventory(); j++)
		{
			ItemStack itemstack2 = par1InventoryCrafting.getStackInSlot(j);

			if (itemstack2 == null)
			{
				continue;
			}

			if (i == 0)
			{
				itemstack = itemstack2;
			}

			if (i == 1)
			{
				itemstack1 = itemstack2;
			}

			i++;
		}

		//TODO - Update from itemIDs
		/*if (i == 2 && itemstack.itemID == itemstack1.itemID && itemstack.stackSize == 1 && itemstack1.stackSize == 1 && Item.itemsList[itemstack.itemID].isDamageable())
		{
			Item item = Item.itemsList[itemstack.itemID];
			int l = item.getMaxDamage() - itemstack.getItemDamageForDisplay();
			int i1 = item.getMaxDamage() - itemstack1.getItemDamageForDisplay();
			int j1 = l + i1 + (item.getMaxDamage() * 10) / 100;
			int k1 = item.getMaxDamage() - j1;

			if (k1 < 0)
			{
				k1 = 0;
			}

			return new ItemStack(itemstack.itemID, 1, k1);
		}*/

		for (int k = 0; k < recipes.size(); k++)
		{
			IRecipe irecipe = (IRecipe)recipes.get(k);

			if (irecipe.matches(par1InventoryCrafting, par2World))
			{
				return irecipe.getCraftingResult(par1InventoryCrafting);
			}
		}

		return null;
	}

	
	public List getRecipeList()
	{
		return recipes;
	}
}