package miscutil.core.item.general.fuelrods;

import java.util.List;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class FuelRod_Base extends Item{

	public int fuelRemaining = 0;
	public int maximumFuel = 0;
	public String fuelType = "";
	public float heat = 0;
	public float maxHeat = 5000;
	private ItemStack thisStack = null;
	private int internalClock = 0;

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
	@Override
	public boolean onEntityItemUpdate(EntityItem entityItem) {
		if (internalClock <= 200){
			internalClock++;
		}
		else {
			if (heat < maxHeat){
				heat++;
			}
			if (fuelRemaining <= maximumFuel){
				fuelRemaining--;
			}
			internalClock = 0;
		}
		return super.onEntityItemUpdate(entityItem);
	}
	@Override
	public void setDamage(ItemStack stack, int damage) {
		this.heat=heat+5;
		super.setDamage(stack, damage);
	}	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		thisStack = stack;
		String tempHeat = String.valueOf(heat);
		String tempFuel = String.valueOf(fuelRemaining);
		String formattedType = EnumChatFormatting.DARK_RED+fuelType+EnumChatFormatting.GRAY;
		String formattedHeat = EnumChatFormatting.RED+tempHeat+EnumChatFormatting.GRAY;
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

		list.add(EnumChatFormatting.GRAY+"A "+formattedType+" fuel rod.");
		list.add(EnumChatFormatting.GRAY+"Running at "+formattedHeat+"K.");
		list.add(EnumChatFormatting.GRAY+"Currently there is: "+formattedFuelLeft+"L of fuel left.");
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

}
