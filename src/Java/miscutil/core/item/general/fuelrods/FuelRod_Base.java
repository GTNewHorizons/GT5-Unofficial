package miscutil.core.item.general.fuelrods;

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

public class FuelRod_Base extends Item{

	public int fuelRemaining = 0;
	public int maximumFuel = 0;
	public String fuelType = "";
	public float heat = 0;
	public float maxHeat = getMaxHeat();
	private ItemStack thisStack = null;
	public FuelRod_Base(String unlocalizedName, String type, int fuelLeft, int maxFuel) {
		this.setUnlocalizedName(unlocalizedName);
		this.setTextureName(CORE.MODID + ":" + unlocalizedName);
		this.setMaxStackSize(1);
		this.setMaxDamage(maxFuel);
		this.maximumFuel = maxFuel;
		this.fuelRemaining = fuelLeft;
		this.fuelType = type;
		this.setCreativeTab(AddToCreativeTab.tabMachines);
	}

	/*@Override
	public void setDamage(ItemStack stack, int damage) {
		this.heat=heat+5;
		super.setDamage(stack, damage);
	}*/	
	
	private float getMaxHeat(){
		float tempvar;
		if (fuelType == "Thorium"){
			tempvar = 2500;
		}
		
		if (fuelType == "Uranium"){
			tempvar = 5000;
		}
		
		if (fuelType == "Plutonium"){
			tempvar = 10000;
		}
		
		else {
			tempvar = 5000;
		}
		return tempvar;
		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {

		Float NBT_Heat = heat;
		Float NBT_MaxHeat = maxHeat;
		int NBT_Fuel = fuelRemaining;
		String NBT_Type= fuelType;

		thisStack = stack;
		if (stack.stackTagCompound != null) {
			NBT_Heat = stack.stackTagCompound.getFloat("heat");
			NBT_MaxHeat = stack.stackTagCompound.getFloat("maxHeat");
			NBT_Fuel = stack.stackTagCompound.getInteger("fuelRemaining");
			NBT_Type = stack.stackTagCompound.getString("fuelType");}

		String tempHeat = String.valueOf(NBT_Heat);
		String tempMaxHeat = String.valueOf(NBT_MaxHeat);
		String tempFuel = String.valueOf(NBT_Fuel);
		String formattedType = EnumChatFormatting.DARK_RED+NBT_Type+EnumChatFormatting.GRAY;
		String formattedHeat = EnumChatFormatting.RED+tempHeat+EnumChatFormatting.GRAY;
		String formattedMaxHeat = EnumChatFormatting.RED+tempMaxHeat+EnumChatFormatting.GRAY;
		String formattedFuelLeft = tempFuel+EnumChatFormatting.GRAY;

		int tempMax = maximumFuel;
		float tempCurrentHeat = heat;
		int tempFuelLeft = fuelRemaining;
		if (tempFuelLeft <= maximumFuel/3){
			formattedFuelLeft = EnumChatFormatting.RED+tempFuel+EnumChatFormatting.GRAY;
		}
		else if (tempFuelLeft >= maximumFuel/3 && tempFuelLeft <= (maximumFuel/3)*2){
			formattedFuelLeft = EnumChatFormatting.YELLOW+tempFuel+EnumChatFormatting.GRAY;
		}
		else if (tempFuelLeft >= (maximumFuel/3)*2 && tempFuelLeft <= maximumFuel){
			formattedFuelLeft = EnumChatFormatting.GREEN+tempFuel+EnumChatFormatting.GRAY;
		}
		else {
			formattedFuelLeft = EnumChatFormatting.GRAY+tempFuel+EnumChatFormatting.GRAY;
		}
		if (tempCurrentHeat <= maxHeat/3 && tempCurrentHeat != 0){
			formattedHeat = EnumChatFormatting.GRAY+tempHeat+EnumChatFormatting.GRAY;
		}
		else if (tempCurrentHeat >= maxHeat/3 && tempMax <= (maxHeat/3)*2 && tempCurrentHeat != 0){
			formattedHeat = EnumChatFormatting.YELLOW+tempHeat+EnumChatFormatting.GRAY;
		}
		else if (tempCurrentHeat <= (maxHeat/3)*2 && tempMax <= maxHeat && tempCurrentHeat != 0){
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

	public String getType(){
		return fuelType;
	}

	public int getFuelRemaining(){
		return fuelRemaining;
	}

	public int getMaxFuel(){
		return maximumFuel;
	}

	public int getFuel(){
		if (thisStack != null){
			int i = thisStack.getItemDamage();
			int r = maximumFuel - i;
			return r;
		}
		return fuelRemaining;
	}

	public boolean setFuelRemainingExplicitly(int i){
		int tempFuel = fuelRemaining;
		fuelRemaining = i;
		if (i != tempFuel){
			return true;
		}		
		return false;
	}

	public boolean addFuel(int i){
		int tempFuel = fuelRemaining;
		fuelRemaining = tempFuel+i;
		if (fuelRemaining != tempFuel){
			return true;
		}		
		return false;
	}

	public float getHeat(){
		return heat;
	}

	public boolean addHeat(float i){
		float tempFuel = heat;
		heat = tempFuel+i;
		if (heat != tempFuel){
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
	public void onCreated(ItemStack itemStack, World world, EntityPlayer player) { 
		itemStack.stackTagCompound = new NBTTagCompound();
		itemStack.stackTagCompound.setInteger("fuelRemaining", fuelRemaining);
		itemStack.stackTagCompound.setInteger("maximumFuel", maximumFuel);
		itemStack.stackTagCompound.setFloat("heat", heat);
		itemStack.stackTagCompound.setFloat("maxHeat", maxHeat);
		itemStack.stackTagCompound.setString("fuelType", fuelType);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World par2World, Entity par3Entity, int par4, boolean par5) {
		itemStack.stackTagCompound = new NBTTagCompound();
		itemStack.stackTagCompound.setInteger("fuelRemaining", fuelRemaining);
		itemStack.stackTagCompound.setInteger("maximumFuel", maximumFuel);
		itemStack.stackTagCompound.setFloat("heat", heat);
		itemStack.stackTagCompound.setFloat("maxHeat", getMaxHeat());
		itemStack.stackTagCompound.setString("fuelType", fuelType);

	}
	
	

}
