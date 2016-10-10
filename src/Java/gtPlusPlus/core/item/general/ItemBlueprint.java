package gtPlusPlus.core.item.general;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.interfaces.IItemBlueprint;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;

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

	protected String mName = "";
	protected boolean mHasBlueprint = false;
	private final int bpID;

	/**
	 * The inventory of items the blueprint holds~
	 */
	protected ItemStack[] blueprint = new ItemStack[9];

	public ItemBlueprint(String unlocalizedName) {
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setMaxStackSize(1);	
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		this.bpID = MathUtils.randInt(0, 1000);
		GameRegistry.registerItem(this, unlocalizedName);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (bpID >= 0){
			list.add(EnumChatFormatting.GRAY+"Technical Document No. "+bpID);			
		}
		if(mHasBlueprint){
			list.add(EnumChatFormatting.BLUE+"Currently holding a blueprint for "+mName);
		}
		else {
			list.add(EnumChatFormatting.RED+"Currently not holding a blueprint for anything.");
		}
		super.addInformation(stack, aPlayer, list, bool);
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {
	return "Blueprint";
	}

	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) { 
		itemStack.stackTagCompound = new NBTTagCompound();
		//this.inventory = null;
		//itemStack.stackTagCompound.set("pos_x", bed_X); TODO
	}

	@Override
	public void onUpdate(ItemStack itemStack, World par2World, Entity par3Entity, int par4, boolean par5) {

	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer par3Entity) {
		//Let the player know what blueprint is held
		Utils.messagePlayer(par3Entity, "This is a placeholder.");
		return super.onItemRightClick(itemStack, world, par3Entity);
	}

	public void readFromNBT(NBTTagCompound nbt){
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
	}

	public void writeToNBT(NBTTagCompound nbt){
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
	}

	@Override
	public boolean isBlueprint(ItemStack stack) {
		return true;
	}

	@Override
	public boolean setBlueprint(ItemStack stack, IInventory craftingTable, ItemStack output) {
		if (!mHasBlueprint){
			try {
				for (int o=0; o<craftingTable.getSizeInventory(); o++){
					blueprint[o] = craftingTable.getStackInSlot(o);
					if (blueprint[0] != null){
						blueprint[0].stackSize = 0;
					}
				}
				if (output != null){
					setBlueprintName(output.getDisplayName());
					return (mHasBlueprint = true);
				}
				return false;
			} catch (Throwable t){
				return false;			
			}
		}
		return false;
	}

	@Override
	public void setBlueprintName(String name) {
		this.mName = name;
	}

	@Override
	public boolean hasBlueprint(ItemStack stack) {
		return mHasBlueprint;
	}

	@Override
	public ItemStack[] getBlueprint(ItemStack stack) {
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

}
