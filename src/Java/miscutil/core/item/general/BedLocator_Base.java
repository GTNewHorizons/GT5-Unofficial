package miscutil.core.item.general;

import java.util.List;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BedLocator_Base extends Item{

	public int bed_X = 0;
	public int bed_Y = 0;
	public int bed_Z = 0;
	
	public BedLocator_Base(String unlocalizedName) {
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setMaxStackSize(1);	
		this.setCreativeTab(AddToCreativeTab.tabMachines);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {

		
		int NBT_X = bed_X;
		int NBT_Y = bed_Y;
		int NBT_Z = bed_Z;
		

		if (stack.stackTagCompound != null) {
			NBT_X = stack.stackTagCompound.getInteger("pos_x");
			NBT_Y = stack.stackTagCompound.getInteger("pos_y");
			NBT_Z = stack.stackTagCompound.getInteger("pos_z");

		String tempX = String.valueOf(NBT_X);
		String tempY = String.valueOf(NBT_Y);
		String tempZ = String.valueOf(NBT_Z);
		String formattedX = EnumChatFormatting.DARK_RED+tempX+EnumChatFormatting.GRAY;
		String formattedY = EnumChatFormatting.RED+tempY+EnumChatFormatting.GRAY;
		String formattedZ = EnumChatFormatting.RED+tempZ+EnumChatFormatting.GRAY;

		list.add(EnumChatFormatting.GRAY+"X: "+formattedX+".");
		list.add(EnumChatFormatting.GRAY+"Y: "+formattedY+".");
		list.add(EnumChatFormatting.GRAY+"Z: "+formattedZ+".");
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
		itemStack.stackTagCompound = new NBTTagCompound();
		this.bed_X = 0;
		this.bed_Y = 0;
		this.bed_Z = 0;
		itemStack.stackTagCompound.setInteger("pos_x", bed_X);
		itemStack.stackTagCompound.setInteger("pos_y", bed_Y);
		itemStack.stackTagCompound.setInteger("pos_z", bed_Z);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		

	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer par3Entity) {
		itemStack.stackTagCompound = new NBTTagCompound();
		if (par3Entity.getBedLocation() != null){
		this.bed_X = par3Entity.getBedLocation().posX;
		this.bed_Y = par3Entity.getBedLocation().posY;
		this.bed_Z = par3Entity.getBedLocation().posZ;
		}
		else {
			this.bed_X = 0;
			this.bed_Y = 0;
			this.bed_Z = 0;
		}
		itemStack.stackTagCompound.setInteger("pos_x", bed_X);
		itemStack.stackTagCompound.setInteger("pos_y", bed_Y);
		itemStack.stackTagCompound.setInteger("pos_z", bed_Z);
		return super.onItemRightClick(itemStack, world, par3Entity);
	}



}
