package gtPlusPlus.core.item.general;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.interfaces.IItemBlueprint;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class ItemBlueprint extends Item implements IItemBlueprint{

	public ItemBlueprint(final String unlocalizedName) {
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setMaxStackSize(1);
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		//this.bpID = MathUtils.randInt(0, 1000);
		GameRegistry.registerItem(this, unlocalizedName);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack itemStack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		//Create some NBT if it's not there, otherwise this does nothing.
		if (!itemStack.hasTagCompound()){
			this.createNBT(itemStack);
		}
		//Set up some default variables.
		int id = -1;
		String name = "";
		boolean blueprint = false;
		//Get proper display vars from NBT if it's there
		if (itemStack.hasTagCompound()){
			//Utils.LOG_WARNING("Found TagCompound");
			id = (int) this.getNBT(itemStack, "mID");
			name = (String) this.getNBT(itemStack, "mName");
			blueprint = (boolean) this.getNBT(itemStack, "mBlueprint");
		}
		//Write to tooltip list for each viable setting.
		if (itemStack.hasTagCompound()) {
			if (id != -1){
				list.add(EnumChatFormatting.GRAY+"Technical Document No. "+id);
			}
			if(blueprint){
				list.add(EnumChatFormatting.BLUE+"Currently holding a blueprint for "+name);
			}
			else {
				list.add(EnumChatFormatting.RED+"Currently not holding a blueprint for anything.");
			}
		}
		else {
			list.add(EnumChatFormatting.RED+"Currently not holding a blueprint for anything.");
		}
		super.addInformation(itemStack, aPlayer, list, bool);
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {
		return "Blueprint [I am useless]";
	}

	@Override
	public void onCreated(final ItemStack itemStack, final World world, final EntityPlayer player) {
		this.createNBT(itemStack);
	}

	@Override
	public void onUpdate(final ItemStack itemStack, final World par2World, final Entity par3Entity, final int par4, final boolean par5) {

	}

	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer par3Entity) {
		//Let the player know what blueprint is held
		if (itemStack.hasTagCompound()) {
			PlayerUtils.messagePlayer(par3Entity, "This Blueprint holds NBT data. "+"|"+this.getNBT(itemStack, "mID")+"|"+this.getNBT(itemStack, "mBlueprint")+"|"+this.getNBT(itemStack, "mName")+"|"+ItemUtils.getArrayStackNames(this.readItemsFromNBT(itemStack)));
		}
		else {
			this.createNBT(itemStack);
			PlayerUtils.messagePlayer(par3Entity, "This is a placeholder. "+this.getNBT(itemStack, "mID"));
		}


		return super.onItemRightClick(itemStack, world, par3Entity);
	}

	public ItemStack[] readItemsFromNBT(final ItemStack itemStack){
		ItemStack[] blueprint = new ItemStack[9];
		if (itemStack.hasTagCompound()){
			final NBTTagCompound nbt = itemStack.getTagCompound();
			final NBTTagList list = nbt.getTagList("Items", 10);
			blueprint = new ItemStack[INV_SIZE];
			for(int i = 0;i<list.tagCount();i++)
			{
				final NBTTagCompound data = list.getCompoundTagAt(i);
				final int slot = data.getInteger("Slot");
				if((slot >= 0) && (slot < INV_SIZE))
				{
					blueprint[slot] = ItemStack.loadItemStackFromNBT(data);
				}
			}
			return blueprint;
		}
		return null;
	}

	public ItemStack writeItemsToNBT(final ItemStack itemStack, final ItemStack[] craftingGrid){
		final ItemStack[] blueprint = craftingGrid;
		if (itemStack.hasTagCompound()){
			final NBTTagCompound nbt = itemStack.getTagCompound();
			final NBTTagList list = new NBTTagList();
			for(int i = 0;i<INV_SIZE;i++)
			{
				final ItemStack stack = blueprint[i];
				if(stack != null)
				{
					final NBTTagCompound data = new NBTTagCompound();
					stack.writeToNBT(data);
					data.setInteger("Slot", i);
					list.appendTag(data);
				}
			}
			nbt.setTag("Items", list);
			itemStack.setTagCompound(nbt);
			return itemStack;
		}
		return null;
	}

	@Override
	public boolean isBlueprint(final ItemStack stack) {
		return true;
	}

	@Override
	public boolean setBlueprint(final ItemStack stack, final IInventory craftingTable, final ItemStack output) {
		boolean hasBP = false;
		ItemStack[] blueprint = new ItemStack[9];

		if (stack.hasTagCompound()){
			hasBP = (boolean) this.getNBT(stack, "mBlueprint");
			blueprint = this.readItemsFromNBT(stack);
		}

		if (!hasBP){
			try {
				for (int o=0; o<craftingTable.getSizeInventory(); o++){
					blueprint[o] = craftingTable.getStackInSlot(o);
					if (blueprint[0] != null){
						blueprint[0].stackSize = 0;
					}
				}
				this.writeItemsToNBT(stack, blueprint);
				if (stack.hasTagCompound()){
					if(stack.getTagCompound().getCompoundTag("Items") != null){
						stack.stackTagCompound.setBoolean("mBlueprint", true);
					}
					else {
						//Invalid BP saved?
					}
					hasBP = (boolean) this.getNBT(stack, "mBlueprint");
				}

				if (output != null){
					this.setBlueprintName(stack, output.getDisplayName());
					hasBP = true;
					return true;
				}
				return false;
			} catch (final Throwable t){
				return false;
			}
		}
		return false;
	}

	@Override
	public void setBlueprintName(final ItemStack stack, final String name) {
		stack.stackTagCompound.setString("mName", name);
	}

	@Override
	public boolean hasBlueprint(final ItemStack stack) {
		if (stack.hasTagCompound()){
			return (boolean) this.getNBT(stack, "mBlueprint");
		}
		return false;
	}

	@Override
	public ItemStack[] getBlueprint(final ItemStack stack) {
		ItemStack[] blueprint = new ItemStack[9];
		if (stack.hasTagCompound()){
			blueprint = this.readItemsFromNBT(stack);
		}
		try {
			final ItemStack[] returnStack = new ItemStack[9];
			for (int o=0; o<blueprint.length; o++){
				returnStack[o] = blueprint[o];
				if (returnStack[0] != null){
					returnStack[0].stackSize = 1;
				}
			}
			return returnStack;
		} catch (final Throwable t){
			return null;
		}
	}

	public boolean createNBT(final ItemStack itemStack){
		if (itemStack.hasTagCompound()){
			if (!itemStack.stackTagCompound.getBoolean("mBlueprint") && !itemStack.stackTagCompound.getString("mName").equals("")){
				//No Blueprint and no name Set
				Logger.WARNING("No Blueprint and no name Set");
				return false;
			}
			else if (itemStack.stackTagCompound.getBoolean("mBlueprint") && !itemStack.stackTagCompound.getString("mName").equals("")){
				//Has Blueprint but invalid name set
				Logger.WARNING("Has Blueprint but invalid name set");
				//itemStack.stackTagCompound = null;
				//createNBT(itemStack);
				return false;
			}
			else if (!itemStack.stackTagCompound.getBoolean("mBlueprint") && itemStack.stackTagCompound.getString("mName").equals("")){
				//Has no Blueprint, but strangely has a name
				Logger.WARNING("Has no Blueprint, but strangely has a name");
				//itemStack.stackTagCompound = null;
				//createNBT(itemStack);
				return false;
			}
			return false;
		}
		else if(!itemStack.hasTagCompound()){
			final int bpID = MathUtils.randInt(0, 1000);
			final boolean hasRecipe = false;
			final String recipeName = "";
			Logger.WARNING("Creating Blueprint, setting up it's NBT data. "+bpID);
			itemStack.stackTagCompound = new NBTTagCompound();
			itemStack.stackTagCompound.setInteger("mID", bpID);
			itemStack.stackTagCompound.setBoolean("mBlueprint", hasRecipe);
			itemStack.stackTagCompound.setString("mName", recipeName);
			return true;
		}
		else {
			final int bpID = MathUtils.randInt(0, 1000);
			final boolean hasRecipe = false;
			final String recipeName = "";
			Logger.WARNING("Creating a Blueprint, setting up it's NBT data. "+bpID);
			itemStack.stackTagCompound = new NBTTagCompound();
			itemStack.stackTagCompound.setInteger("mID", bpID);
			itemStack.stackTagCompound.setBoolean("mBlueprint", hasRecipe);
			itemStack.stackTagCompound.setString("mName", recipeName);
			return true;
		}
	}

	public Object getNBT(final ItemStack itemStack, final String tagNBT){
		if (!itemStack.hasTagCompound()){
			return null;
		}
		Object o = null;
		if (tagNBT.equals("mID")){
			o = itemStack.stackTagCompound.getInteger(tagNBT);
		}
		else if (tagNBT.equals("mBlueprint")){
			o = itemStack.stackTagCompound.getBoolean(tagNBT);
		}
		else if (tagNBT.equals("mName")){
			o = itemStack.stackTagCompound.getString(tagNBT);
		}
		else if (tagNBT.equals("")){
			//For More Tag Support
			//o = itemStack.stackTagCompound.getInteger(tagNBT);
		}
		if (o != null) {
			return o;
		}
		return null;	}

}
