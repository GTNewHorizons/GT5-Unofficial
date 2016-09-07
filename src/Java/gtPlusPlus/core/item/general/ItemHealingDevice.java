package gtPlusPlus.core.item.general;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.item.UtilsItems;
import gtPlusPlus.core.util.math.MathUtils;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.common.registry.GameRegistry;

public class ItemHealingDevice extends Item implements IElectricItem, IElectricItemManager, IBauble{

	private final String unlocalizedName = "personalHealingDevice";
	private final ItemStack thisStack;
	private final static int maxValueEU = 1000000000;
	protected double chargeEU = 0;

	public ItemHealingDevice(){
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		this.setUnlocalizedName(unlocalizedName);
		this.setMaxStackSize(1);
		this.setTextureName(CORE.MODID + ":" + "personalCloakingDevice");
		this.thisStack = UtilsItems.getSimpleStack(this);
		GameRegistry.registerItem(this, unlocalizedName);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World worldObj, Entity player, int p_77663_4_, boolean p_77663_5_) {
		if (worldObj.isRemote) {
			return;
		}

		if (player instanceof EntityPlayer){
			for (ItemStack is : ((EntityPlayer) player).inventory.mainInventory) {
				if (is == itemStack) {
					continue;
				}
				if (is != null) {
					if (is.getItem() instanceof IElectricItem) {
						IElectricItem electricItem = (IElectricItem) is.getItem();
						chargeEU = ElectricItem.manager.getCharge(is);
					}

				}
			}
		}


		super.onUpdate(itemStack, worldObj, player, p_77663_4_, p_77663_5_);
	}

	@Override
	public boolean canProvideEnergy(ItemStack itemStack) {
		return true;
	}

	@Override
	public Item getChargedItem(ItemStack itemStack) {
		ItemStack x = itemStack.copy();
		x.setItemDamage(maxValueEU);
		return x.getItem();
	}

	@Override
	public Item getEmptyItem(ItemStack itemStack) {
		ItemStack x = itemStack.copy();
		x.setItemDamage(0);
		return x.getItem();
	}

	@Override
	public double getMaxCharge(ItemStack itemStack) {
		return maxValueEU;
	}

	@Override
	public int getTier(ItemStack itemStack) {
		return 5;
	}

	@Override
	public double getTransferLimit(ItemStack itemStack) {
		return 32784;
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return (EnumChatFormatting.BLUE+"Personal Healing NanoBooster"+EnumChatFormatting.RESET);
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		//return 1.0D - getEnergyStored(stack) / this.capacity;
		return  1.0D - (double)getCharge(stack) / (double)getMaxCharge(stack);
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return true;
	}
	
	public double secondsLeft(ItemStack stack){
		
		double r = 0;
		r = getCharge(stack)/(1638400/4);
		return (int) r;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {				
		list.add("");				
		list.add(EnumChatFormatting.GREEN+"Worn as a Necklace within Baubles."+EnumChatFormatting.GRAY);	
		list.add(EnumChatFormatting.GREEN+"Drains 1638400eu to restore hunger."+EnumChatFormatting.GRAY);			
		list.add("");			
		list.add(EnumChatFormatting.GOLD+"IC2/EU Information"+EnumChatFormatting.GRAY);	
		list.add(EnumChatFormatting.GRAY+"Tier: ["+EnumChatFormatting.YELLOW+getTier(thisStack)+EnumChatFormatting.GRAY+"] Transfer Limit: ["+EnumChatFormatting.YELLOW+getTransferLimit(thisStack)+EnumChatFormatting.GRAY +"Eu/t]");
		list.add(EnumChatFormatting.GRAY+"Current Power: ["+EnumChatFormatting.YELLOW+(long) getCharge(stack)+EnumChatFormatting.GRAY+"Eu] ["+EnumChatFormatting.YELLOW+MathUtils.findPercentage(getCharge(stack), getMaxCharge(stack))+EnumChatFormatting.GRAY +"%]");
		list.add(EnumChatFormatting.GRAY+"Uses Remaining: ["+EnumChatFormatting.YELLOW+secondsLeft(stack)+ EnumChatFormatting.GRAY +"]");
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public double charge(ItemStack stack, double amount, int tier,
			boolean ignoreTransferLimit, boolean simulate) {		 

		if (!simulate)
		{
			ElectricItem.manager.charge(stack, amount, tier, true, simulate);

		}
		return ElectricItem.manager.charge(stack, amount, tier, true, simulate);
	}

	@Override
	public double discharge(ItemStack stack, double amount, int tier,
			boolean ignoreTransferLimit, boolean externally, boolean simulate) {		
		if (!simulate)
		{
			ElectricItem.manager.discharge(stack, amount, tier, ignoreTransferLimit, externally, simulate);
		}

		return ElectricItem.manager.discharge(stack, amount, tier, ignoreTransferLimit, externally, simulate);
	}

	@Override
	public double getCharge(ItemStack stack) {
		return ElectricItem.manager.getCharge(stack);
	}

	@Override
	public boolean canUse(ItemStack stack, double amount) {
		return ElectricItem.manager.canUse(stack, amount);
	}

	@Override
	public boolean use(ItemStack stack, double amount, EntityLivingBase entity) {
		return ElectricItem.manager.use(stack, amount, entity);
	}

	@Override
	public void chargeFromArmor(ItemStack stack, EntityLivingBase entity) {
		ElectricItem.manager.chargeFromArmor(stack, entity);
	}

	@Override
	public String getToolTip(ItemStack stack) {
		return ElectricItem.manager.getToolTip(stack);
	}

	@Override
	public boolean canEquip(ItemStack arg0, EntityLivingBase arg1) {
		return true;
	}

	@Override
	public boolean canUnequip(ItemStack arg0, EntityLivingBase arg1) {
		return true;
	}

	@Override
	public BaubleType getBaubleType(ItemStack arg0) {
		return BaubleType.AMULET;
	}

	@Override //TODO
	public void onEquipped(ItemStack arg0, EntityLivingBase arg1) {

	}

	@Override //TODO
	public void onUnequipped(ItemStack arg0, EntityLivingBase arg1) {

	}

	@Override //TODO
	public void onWornTick(ItemStack arg0, EntityLivingBase arg1) {
		if (!arg1.worldObj.isRemote){
			if (getCharge(arg0) >= 1638400/4){
				if (arg1.getHealth() < arg1.getMaxHealth()){
					float rx = arg1.getMaxHealth()-arg1.getHealth();
					Utils.LOG_INFO("rx:"+rx);
				arg1.heal(rx*2);
				discharge(arg0, (1638400/4)*rx, 6, true, true, false);
				Utils.messagePlayer((EntityPlayer) arg1, "Your NanoBooster Whirs! Leaving you feeling stronger. It Healed "+rx+" hp.");
				Utils.messagePlayer((EntityPlayer) arg1, "You check it's remaining uses, it has "+secondsLeft(arg0)+".");
				}
			}
		}
	}

}
