package gtPlusPlus.core.item.general.fuelrods;

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

public class FuelRod_Base extends Item{

	public int fuelRemaining = 0;
	public int maximumFuel = 0;
	public String fuelType = "";
	public float heat = 0;
	public float maxHeat = this.getMaxHeat();
	public FuelRod_Base(final String unlocalizedName, final String type, final int fuelLeft, final int maxFuel) {
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setMaxStackSize(1);
		this.setMaxDamage(maxFuel);
		this.maximumFuel = maxFuel;
		this.fuelRemaining = fuelLeft;
		this.fuelType = type;
		this.setCreativeTab(AddToCreativeTab.tabMachines);
	}

	private float getMaxHeat(){
		float tempvar;
		if (this.fuelType == "Thorium"){
			tempvar = 2500;
		}

		else if (this.fuelType == "Uranium"){
			tempvar = 5000;
		}

		else if (this.fuelType == "Plutonium"){
			tempvar = 10000;
		}

		else {
			tempvar = 5000;
		}
		return tempvar;

	}

	private void updateVars(final ItemStack stack){
		if (stack.stackTagCompound != null) {
			this.heat = stack.stackTagCompound.getFloat("heat");
			this.fuelRemaining = stack.stackTagCompound.getInteger("fuelRemaining");
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {

		Float NBT_Heat = this.heat;
		Float NBT_MaxHeat = this.maxHeat;
		int NBT_Fuel = this.fuelRemaining;
		String NBT_Type= this.fuelType;

		if (stack.stackTagCompound != null) {
			NBT_Heat = stack.stackTagCompound.getFloat("heat");
			NBT_MaxHeat = stack.stackTagCompound.getFloat("maxHeat");
			NBT_Fuel = stack.stackTagCompound.getInteger("fuelRemaining");
			NBT_Type = stack.stackTagCompound.getString("fuelType");
		}

		final String tempHeat = String.valueOf(NBT_Heat);
		final String tempMaxHeat = String.valueOf(NBT_MaxHeat);
		final String tempFuel = String.valueOf(NBT_Fuel);
		final String formattedType = EnumChatFormatting.DARK_RED+NBT_Type+EnumChatFormatting.GRAY;
		String formattedHeat = EnumChatFormatting.RED+tempHeat+EnumChatFormatting.GRAY;
		final String formattedMaxHeat = EnumChatFormatting.RED+tempMaxHeat+EnumChatFormatting.GRAY;
		String formattedFuelLeft = tempFuel+EnumChatFormatting.GRAY;

		final int tempMax = this.maximumFuel;
		final float tempCurrentHeat = this.heat;
		final int tempFuelLeft = this.fuelRemaining;

		//Fuel Usage Formatting
		if (tempFuelLeft <= (this.maximumFuel/3)){
			formattedFuelLeft = EnumChatFormatting.RED+tempFuel+EnumChatFormatting.GRAY;
		}
		else if ((tempFuelLeft >= (this.maximumFuel/3)) && (tempFuelLeft <= ((this.maximumFuel/3)*2))){
			formattedFuelLeft = EnumChatFormatting.YELLOW+tempFuel+EnumChatFormatting.GRAY;
		}
		else if ((tempFuelLeft >= ((this.maximumFuel/3)*2)) && (tempFuelLeft <= this.maximumFuel)){
			formattedFuelLeft = EnumChatFormatting.GREEN+tempFuel+EnumChatFormatting.GRAY;
		}
		else {
			formattedFuelLeft = EnumChatFormatting.GRAY+tempFuel+EnumChatFormatting.GRAY;
		}

		//Heat Formatting
		if ((tempCurrentHeat <= 200) && (tempCurrentHeat >= 0)){
			formattedHeat = EnumChatFormatting.GRAY+tempHeat+EnumChatFormatting.GRAY;
		}
		else if ((tempCurrentHeat <= (this.maxHeat/3)) && (tempCurrentHeat > 200)){
			formattedHeat = EnumChatFormatting.YELLOW+tempHeat+EnumChatFormatting.GRAY;
		}
		else if ((tempCurrentHeat >= (this.maxHeat/3)) && (tempMax < ((this.maxHeat/3)*2)) && (tempCurrentHeat != 0)){
			formattedHeat = EnumChatFormatting.GOLD+tempHeat+EnumChatFormatting.GRAY;
		}
		else if ((tempCurrentHeat >= ((this.maxHeat/3)*2)) && (tempMax <= this.maxHeat) && (tempCurrentHeat != 0)){
			formattedHeat = EnumChatFormatting.RED+tempHeat+EnumChatFormatting.GRAY;
		}
		else {
			formattedHeat = EnumChatFormatting.BLUE+tempHeat+EnumChatFormatting.GRAY;
		}
		list.add(EnumChatFormatting.GRAY+"A "+formattedType+" Fuel Rod.");
		list.add(EnumChatFormatting.GRAY+"Running at "+formattedHeat+"/"+formattedMaxHeat+" Kelvin.");
		list.add(EnumChatFormatting.GRAY+"Fuel Remaining: "+formattedFuelLeft+"L.");
		super.addInformation(stack, aPlayer, list, bool);
	}

	public String getType(final ItemStack stack){
		if (stack.stackTagCompound != null){
			return stack.stackTagCompound.getString("fuelType");
		}
		return this.fuelType;
	}

	public int getFuelRemaining(final ItemStack stack){
		if (stack.stackTagCompound != null){
			return stack.stackTagCompound.getInteger("fuelRemaining");
		}
		return 0;
	}

	public int getMaxFuel(){
		return this.maximumFuel;
	}

	public int getFuel(final ItemStack stack){
		if (stack != null){
			final int i = stack.getItemDamage();
			final int r = this.maximumFuel - i;
			return r;
		}
		return 0;
	}

	public boolean setFuelRemainingExplicitly(final int i){
		final int tempFuel = this.fuelRemaining;
		this.fuelRemaining = i;
		if (i != tempFuel){
			return true;
		}
		return false;
	}

	public boolean addFuel(final int i){
		final int tempFuel = this.fuelRemaining;
		this.fuelRemaining = tempFuel+i;
		if (this.fuelRemaining != tempFuel){
			return true;
		}
		return false;
	}

	public float getHeat(final ItemStack value){
		if (value.stackTagCompound != null){
			return value.stackTagCompound.getFloat("heat");
		}
		return 0f;
	}

	public boolean addHeat(final float i){
		final float tempFuel = this.heat;
		this.heat = tempFuel+i;
		if (this.heat != tempFuel){
			return true;
		}
		return false;
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
		itemStack.stackTagCompound.setInteger("fuelRemaining", this.getFuelRemaining(itemStack));
		itemStack.stackTagCompound.setInteger("maximumFuel", this.maximumFuel);
		itemStack.stackTagCompound.setFloat("heat", this.getHeat(itemStack));
		itemStack.stackTagCompound.setFloat("maxHeat", this.getMaxHeat());
		itemStack.stackTagCompound.setString("fuelType", this.getType(itemStack));
		this.updateVars(itemStack);
	}

	@Override
	public void onUpdate(final ItemStack itemStack, final World par2World, final Entity par3Entity, final int par4, final boolean par5) {
		itemStack.stackTagCompound = new NBTTagCompound();
		itemStack.stackTagCompound.setInteger("fuelRemaining", this.getFuelRemaining(itemStack));
		itemStack.stackTagCompound.setInteger("maximumFuel", this.maximumFuel);
		itemStack.stackTagCompound.setFloat("heat", this.getHeat(itemStack));
		itemStack.stackTagCompound.setFloat("maxHeat", this.getMaxHeat());
		itemStack.stackTagCompound.setString("fuelType", this.getType(itemStack));
		this.updateVars(itemStack);
	}



}
