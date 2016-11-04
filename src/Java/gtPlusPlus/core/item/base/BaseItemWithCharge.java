package gtPlusPlus.core.item.base;

import java.util.List;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BaseItemWithCharge extends Item{

	public int int_Charge = 0;
	public int int_Max_Charge = 0;
	
	public BaseItemWithCharge(String unlocalizedName, int constructor_Charge, int constructor_Max_Charge) {
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setMaxStackSize(1);	
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		this.int_Charge = constructor_Charge;
		this.int_Max_Charge = constructor_Max_Charge;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		int NBT_Charge = int_Charge;
		int NBT_Max_Charge = int_Max_Charge;
		if (stack.stackTagCompound != null) {
			NBT_Charge = stack.stackTagCompound.getInteger("charge_Current");
			NBT_Max_Charge = stack.stackTagCompound.getInteger("charge_Max");
		String tempX = String.valueOf(NBT_Charge);
		String tempY = String.valueOf(NBT_Max_Charge);
		String formattedX = EnumChatFormatting.RED+tempX+EnumChatFormatting.GRAY;
		String formattedY = EnumChatFormatting.DARK_RED+tempY+EnumChatFormatting.GRAY;
		list.add(EnumChatFormatting.GRAY+"Charge:"+formattedX+"/"+formattedY+".");
		super.addInformation(stack, aPlayer, list, bool);
	}
	}

	//Ticking and NBT Handling
	/* Called each tick as long the item is on a player inventory. Uses by maps to check if is on a player hand and
	 * update it's contents.
	 * 
	 *  public int fuelRemaining = 0;
	public int maximumFuel = 0;
	public String fuelType = "";
	public float heat = 0;
	public float maxHeat = 5000;
	 *  
	 */
	@Override
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) { 

	}

	@Override
	public void onUpdate(ItemStack itemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer par3Entity) {
		itemStack.stackTagCompound = new NBTTagCompound();
		return super.onItemRightClick(itemStack, world, par3Entity);
	}



}
