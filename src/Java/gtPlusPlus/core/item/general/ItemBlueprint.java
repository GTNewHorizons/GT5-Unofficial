package gtPlusPlus.core.item.general;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.interfaces.IItemBlueprint;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.player.PlayerUtils;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemBlueprint extends Item implements IItemBlueprint{

	public ItemBlueprint(String unlocalizedName) {
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setMaxStackSize(1);	
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		//this.bpID = MathUtils.randInt(0, 1000);
		GameRegistry.registerItem(this, unlocalizedName);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack itemStack, EntityPlayer aPlayer, List list, boolean bool) {
		//Create some NBT if it's not there, otherwise this does nothing.
		if (!itemStack.hasTagCompound()){
		createNBT(itemStack);
		}
		//Set up some default variables.
		int id = -1;
		String name = "";
		boolean blueprint = false;
		//Get proper display vars from NBT if it's there
		if (itemStack.hasTagCompound()){
			//Utils.LOG_WARNING("Found TagCompound");
			id = (int) getNBT(itemStack, "mID");
			name = (String) getNBT(itemStack, "mName");
			blueprint = (boolean) getNBT(itemStack, "mBlueprint");
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
	public String getItemStackDisplayName(ItemStack p_77653_1_) {
		return "Blueprint [I am useless]";
	}

	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) {
		createNBT(itemStack);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World par2World, Entity par3Entity, int par4, boolean par5) {

	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer par3Entity) {
		//Let the player know what blueprint is held
		if (itemStack.hasTagCompound()) {
			PlayerUtils.messagePlayer(par3Entity, "This Blueprint holds NBT data. "+"|"+getNBT(itemStack, "mID")+"|"+getNBT(itemStack, "mBlueprint")+"|"+getNBT(itemStack, "mName")+"|"+UtilsItems.getArrayStackNames(readItemsFromNBT(itemStack)));
		}
		else {
			createNBT(itemStack);
			PlayerUtils.messagePlayer(par3Entity, "This is a placeholder. "+getNBT(itemStack, "mID"));			
		}


		return super.onItemRightClick(itemStack, world, par3Entity);
	}

	public ItemStack[] readItemsFromNBT(ItemStack itemStack){
		ItemStack[] blueprint = new ItemStack[9];
		if (itemStack.hasTagCompound()){
			NBTTagCompound nbt = itemStack.getTagCompound();
			NBTTagList list = nbt.getTagList("Items", 10);
			blueprint = new ItemStack[INV_SIZE];
			for(int i = 0;i<list.tagCount();i++)
			{
				NBTTagCompound data = list.getCompoundTagAt(i);
				int slot = data.getInteger("Slot");
				if(slot >= 0 && slot < INV_SIZE)
				{
					blueprint[slot] = ItemStack.loadItemStackFromNBT(data);
				}
			}
			return blueprint;
		}
		return null;
	}

	public ItemStack writeItemsToNBT(ItemStack itemStack, ItemStack[] craftingGrid){
		ItemStack[] blueprint = craftingGrid;
		if (itemStack.hasTagCompound()){
			NBTTagCompound nbt = itemStack.getTagCompound();
			NBTTagList list = new NBTTagList();
			for(int i = 0;i<INV_SIZE;i++)
			{
				ItemStack stack = blueprint[i];
				if(stack != null)
				{
					NBTTagCompound data = new NBTTagCompound();
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
	public boolean isBlueprint(ItemStack stack) {
		return true;
	}

	@Override
	public boolean setBlueprint(ItemStack stack, IInventory craftingTable, ItemStack output) {
		boolean hasBP = false;
		ItemStack[] blueprint = new ItemStack[9];

		if (stack.hasTagCompound()){
			hasBP = (boolean) getNBT(stack, "mBlueprint");
			blueprint = readItemsFromNBT(stack);
		}

		if (!hasBP){
			try {
				for (int o=0; o<craftingTable.getSizeInventory(); o++){
					blueprint[o] = craftingTable.getStackInSlot(o);
					if (blueprint[0] != null){
						blueprint[0].stackSize = 0;
					}
				}
				writeItemsToNBT(stack, blueprint);
				if (stack.hasTagCompound()){
					if(stack.getTagCompound().getCompoundTag("Items") != null){
						stack.stackTagCompound.setBoolean("mBlueprint", true);
					}
					else {
						//Invalid BP saved?
					}
					hasBP = (boolean) getNBT(stack, "mBlueprint");
				}
				
				if (output != null){
					setBlueprintName(stack, output.getDisplayName());
					return (hasBP = true);
				}
				return false;
			} catch (Throwable t){
				return false;			
			}
		}
		return false;
	}

	@Override
	public void setBlueprintName(ItemStack stack, String name) {
		stack.stackTagCompound.setString("mName", name);
	}

	@Override
	public boolean hasBlueprint(ItemStack stack) {
		if (stack.hasTagCompound()){
			return (boolean) getNBT(stack, "mBlueprint");
		}
		return false;
	}

	@Override
	public ItemStack[] getBlueprint(ItemStack stack) {
		ItemStack[] blueprint = new ItemStack[9];
		if (stack.hasTagCompound()){
			blueprint = readItemsFromNBT(stack);
		}		
		try {
			ItemStack[] returnStack = new ItemStack[9];
			for (int o=0; o<blueprint.length; o++){
				returnStack[o] = blueprint[o];
				if (returnStack[0] != null){
					returnStack[0].stackSize = 1;
				}
			}
			return returnStack;
		} catch (Throwable t){
			return null;			
		}
	}

	public boolean createNBT(ItemStack itemStack){   
		if (itemStack.hasTagCompound()){
			if (!itemStack.stackTagCompound.getBoolean("mBlueprint") && !itemStack.stackTagCompound.getString("mName").equals("")){
				//No Blueprint and no name Set
				Utils.LOG_WARNING("No Blueprint and no name Set");
				return false;
			}
			else if (itemStack.stackTagCompound.getBoolean("mBlueprint") && !itemStack.stackTagCompound.getString("mName").equals("")){
				//Has Blueprint but invalid name set
				Utils.LOG_WARNING("Has Blueprint but invalid name set");
				//itemStack.stackTagCompound = null;
				//createNBT(itemStack);
				return false;
			}
			else if (!itemStack.stackTagCompound.getBoolean("mBlueprint") && itemStack.stackTagCompound.getString("mName").equals("")){
				//Has no Blueprint, but strangely has a name
				Utils.LOG_WARNING("Has no Blueprint, but strangely has a name");
				//itemStack.stackTagCompound = null;
				//createNBT(itemStack);
				return false;
			}
			return false;
		}
		else if(!itemStack.hasTagCompound()){
			int bpID = MathUtils.randInt(0, 1000);
			boolean hasRecipe = false;
			String recipeName = "";
			Utils.LOG_WARNING("Creating Blueprint, setting up it's NBT data. "+bpID);
			itemStack.stackTagCompound = new NBTTagCompound();
			itemStack.stackTagCompound.setInteger("mID", bpID);
			itemStack.stackTagCompound.setBoolean("mBlueprint", hasRecipe);
			itemStack.stackTagCompound.setString("mName", recipeName);
			return true;
		}
		else {
			int bpID = MathUtils.randInt(0, 1000);
			boolean hasRecipe = false;
			String recipeName = "";
			Utils.LOG_WARNING("Creating a Blueprint, setting up it's NBT data. "+bpID);
			itemStack.stackTagCompound = new NBTTagCompound();
			itemStack.stackTagCompound.setInteger("mID", bpID);
			itemStack.stackTagCompound.setBoolean("mBlueprint", hasRecipe);
			itemStack.stackTagCompound.setString("mName", recipeName);
			return true;
		}		
	}

	public Object getNBT(ItemStack itemStack, String tagNBT){   
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
		if (o != null)
			return o;
		return null;	}

}
