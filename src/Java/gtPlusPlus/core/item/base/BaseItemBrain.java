package gtPlusPlus.core.item.base;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

/*
 *
 *
 	Key Point: You can access the NBT compound data from the Item class (in those methods that pass an ItemStack), but the NBT compound can only be set on an ItemStack.

	The steps to add NBT data to an ItemStack:
    Create or otherwise get an ItemStack of the desired item
    Create an NBTTagCompound and fill it with the appropriate data
    Call ItemStack#setTagCompound() method to set it.

 *
 */

public class BaseItemBrain extends Item{
	// This is an array of all the types I am going to be adding.
	String[] brainTypes = { "dead", "preserved", "fresh", "tasty" };

	// This method allows us to have different language translation keys for
	// each item we add.
	@Override
	public String getUnlocalizedName(final ItemStack stack)
	{
		// This makes sure that the stack has a tag compound. This is how data
		// is stored on items.
		if (stack.hasTagCompound())
		{
			// This is the object holding all of the item data.
			final NBTTagCompound itemData = stack.getTagCompound();
			// This checks to see if the item has data stored under the
			// brainType key.
			if (itemData.hasKey("brainType"))
			{
				// This retrieves data from the brainType key and uses it in
				// the return value
				return "item." + itemData.getString("brainType");
			}
		}
		// This will be used if the item is obtained without nbt data on it.
		return "item.nullBrain";
	}


	// This is a fun method which allows us to run some code when our item is
	// shown in a creative tab. I am going to use it to add all the brain
	// types.
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(final Item item, final CreativeTabs tab, final List itemList)
	{
		// This creates a loop with a counter. It will go through once for
		// every listing in brainTypes,  and gives us a number associated
		// with each listing.
		for (int pos = 0; pos < this.brainTypes.length; pos++)
		{
			// This creates a new ItemStack instance. The item parameter
			// supplied is this item.
			final ItemStack brainStack = new ItemStack(item);
			// By default, a new ItemStack does not have any nbt compound data.
			// We need to give it some.
			brainStack.setTagCompound(new NBTTagCompound());
			// Now we set the type of the item, brainType is the key, and
			// brainTypes[pos] is grabbing a
			// entry from the brainTypes array.
			brainStack.getTagCompound().setString("brainType",
					this.brainTypes[pos]);
			// And this adds it to the itemList, which is a list of all items
			// in the creative tab.
			itemList.add(brainStack);
		}
	}

	// This code will allow us to tell the items apart in game. You can change
	@SuppressWarnings({ "rawtypes", "unchecked" })
	// texture based on nbt data, but I won't be covering that.
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(final ItemStack stack, final EntityPlayer player, final List tooltip, final boolean isAdvanced){
		if ( stack.hasTagCompound()
				&& stack.getTagCompound().hasKey("brainType"))
		{
			// StatCollector is a class which allows us to handle string
			// language translation. This requires that you fill out the
			// translation in you language class.
			tooltip.add(StatCollector.translateToLocal("tooltip.yourmod."
					+ stack.getTagCompound().getString("brainType") + ".desc"));
		}
		else // If the brain does not have valid tag data, a default message
		{
			tooltip.add(StatCollector.translateToLocal(
					"tooltip.yourmod.nullbrain.desc"));
		}
	}
}

