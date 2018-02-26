package gtPlusPlus.core.item.general;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;

public class BedLocator_Base extends Item{

	public int bed_X = 0;
	public int bed_Y = 0;
	public int bed_Z = 0;

	public BedLocator_Base(final String unlocalizedName) {
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setMaxStackSize(1);
		this.setCreativeTab(AddToCreativeTab.tabMachines);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {


		int NBT_X = this.bed_X;
		int NBT_Y = this.bed_Y;
		int NBT_Z = this.bed_Z;


		if (stack.stackTagCompound != null) {
			NBT_X = stack.stackTagCompound.getInteger("pos_x");
			NBT_Y = stack.stackTagCompound.getInteger("pos_y");
			NBT_Z = stack.stackTagCompound.getInteger("pos_z");

			final String tempX = String.valueOf(NBT_X);
			final String tempY = String.valueOf(NBT_Y);
			final String tempZ = String.valueOf(NBT_Z);
			final String formattedX = EnumChatFormatting.DARK_RED+tempX+EnumChatFormatting.GRAY;
			final String formattedY = EnumChatFormatting.RED+tempY+EnumChatFormatting.GRAY;
			final String formattedZ = EnumChatFormatting.RED+tempZ+EnumChatFormatting.GRAY;

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
	public void onCreated(final ItemStack itemStack, final World world, final EntityPlayer player) {
		itemStack.stackTagCompound = new NBTTagCompound();
		this.bed_X = 0;
		this.bed_Y = 0;
		this.bed_Z = 0;
		itemStack.stackTagCompound.setInteger("pos_x", this.bed_X);
		itemStack.stackTagCompound.setInteger("pos_y", this.bed_Y);
		itemStack.stackTagCompound.setInteger("pos_z", this.bed_Z);
	}

	@Override
	public void onUpdate(final ItemStack itemStack, final World par2World, final Entity par3Entity, final int par4, final boolean par5) {


	}

	@Override
	public ItemStack onItemRightClick(final ItemStack itemStack, final World world, final EntityPlayer par3Entity) {
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
		itemStack.stackTagCompound.setInteger("pos_x", this.bed_X);
		itemStack.stackTagCompound.setInteger("pos_y", this.bed_Y);
		itemStack.stackTagCompound.setInteger("pos_z", this.bed_Z);
		return super.onItemRightClick(itemStack, world, par3Entity);
	}



}
