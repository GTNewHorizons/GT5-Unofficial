package gtPlusPlus.core.item.general;

import java.util.List;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import ic2.api.item.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

@Optional.InterfaceList(value = {@Optional.Interface(iface = "baubles.api.IBauble", modid = "Baubles"), @Optional.Interface(iface = "baubles.api.BaubleType", modid = "Baubles")})
public class ItemHealingDevice extends Item implements IElectricItem, IElectricItemManager, IBauble{

	private final String unlocalizedName = "personalHealingDevice";
	private final ItemStack thisStack;
	private final static int maxValueEU = 1000000000;
	protected double chargeEU = 0;

	public ItemHealingDevice(){
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		this.setUnlocalizedName(this.unlocalizedName);
		this.setMaxStackSize(1);
		this.setTextureName(CORE.MODID + ":" + "personalCloakingDevice");
		this.thisStack = ItemUtils.getSimpleStack(this);
		GameRegistry.registerItem(this, this.unlocalizedName);
	}

	@Override
	public void onUpdate(final ItemStack itemStack, final World worldObj, final Entity player, final int p_77663_4_, final boolean p_77663_5_) {
		if (worldObj.isRemote) {
			return;
		}

		if (player instanceof EntityPlayer){
			for (final ItemStack is : ((EntityPlayer) player).inventory.mainInventory) {
				if (is == itemStack) {
					continue;
				}
				if (is != null) {
					if (is.getItem() instanceof IElectricItem) {
						final IElectricItem electricItem = (IElectricItem) is.getItem();
						this.chargeEU = ElectricItem.manager.getCharge(is);
					}

				}
			}
		}


		super.onUpdate(itemStack, worldObj, player, p_77663_4_, p_77663_5_);
	}

	@Override
	public boolean canProvideEnergy(final ItemStack itemStack) {
		return true;
	}

	@Override
	public Item getChargedItem(final ItemStack itemStack) {
		final ItemStack x = itemStack.copy();
		x.setItemDamage(maxValueEU);
		return x.getItem();
	}

	@Override
	public Item getEmptyItem(final ItemStack itemStack) {
		final ItemStack x = itemStack.copy();
		x.setItemDamage(0);
		return x.getItem();
	}

	@Override
	public double getMaxCharge(final ItemStack itemStack) {
		return maxValueEU;
	}

	@Override
	public int getTier(final ItemStack itemStack) {
		return 5;
	}

	@Override
	public double getTransferLimit(final ItemStack itemStack) {
		return 32784;
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {

		return (EnumChatFormatting.BLUE+"Personal Healing NanoBooster"+EnumChatFormatting.RESET);
	}

	@Override
	public double getDurabilityForDisplay(final ItemStack stack)
	{
		//return 1.0D - getEnergyStored(stack) / this.capacity;
		return  1.0D - (this.getCharge(stack) / this.getMaxCharge(stack));
	}

	@Override
	public boolean showDurabilityBar(final ItemStack stack)
	{
		return true;
	}

	public double secondsLeft(final ItemStack stack){

		double r = 0;
		r = this.getCharge(stack)/(1638400/4);
		return (int) r;
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add("");
		list.add(EnumChatFormatting.GREEN+"Worn as a Necklace within Baubles."+EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.GREEN+"Drains 1638400eu to restore hunger."+EnumChatFormatting.GRAY);
		list.add("");
		list.add(EnumChatFormatting.GOLD+"IC2/EU Information"+EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.GRAY+"Tier: ["+EnumChatFormatting.YELLOW+this.getTier(this.thisStack)+EnumChatFormatting.GRAY+"] Transfer Limit: ["+EnumChatFormatting.YELLOW+this.getTransferLimit(this.thisStack)+EnumChatFormatting.GRAY +"Eu/t]");
		list.add(EnumChatFormatting.GRAY+"Current Power: ["+EnumChatFormatting.YELLOW+(long) this.getCharge(stack)+EnumChatFormatting.GRAY+"Eu] ["+EnumChatFormatting.YELLOW+MathUtils.findPercentage(this.getCharge(stack), this.getMaxCharge(stack))+EnumChatFormatting.GRAY +"%]");
		list.add(EnumChatFormatting.GRAY+"Uses Remaining: ["+EnumChatFormatting.YELLOW+this.secondsLeft(stack)+ EnumChatFormatting.GRAY +"]");
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public double charge(final ItemStack stack, final double amount, final int tier,
			final boolean ignoreTransferLimit, final boolean simulate) {

		if (!simulate)
		{
			ElectricItem.manager.charge(stack, amount, tier, true, simulate);

		}
		return ElectricItem.manager.charge(stack, amount, tier, true, simulate);
	}

	@Override
	public double discharge(final ItemStack stack, final double amount, final int tier,
			final boolean ignoreTransferLimit, final boolean externally, final boolean simulate) {
		if (!simulate)
		{
			ElectricItem.manager.discharge(stack, amount, tier, ignoreTransferLimit, externally, simulate);
		}

		return ElectricItem.manager.discharge(stack, amount, tier, ignoreTransferLimit, externally, simulate);
	}

	@Override
	public double getCharge(final ItemStack stack) {
		return ElectricItem.manager.getCharge(stack);
	}

	@Override
	public boolean canUse(final ItemStack stack, final double amount) {
		return ElectricItem.manager.canUse(stack, amount);
	}

	@Override
	public boolean use(final ItemStack stack, final double amount, final EntityLivingBase entity) {
		return ElectricItem.manager.use(stack, amount, entity);
	}

	@Override
	public void chargeFromArmor(final ItemStack stack, final EntityLivingBase entity) {
		ElectricItem.manager.chargeFromArmor(stack, entity);
	}

	@Override
	public String getToolTip(final ItemStack stack) {
		return ElectricItem.manager.getToolTip(stack);
	}

	@Override
	public boolean canEquip(final ItemStack arg0, final EntityLivingBase arg1) {
		return true;
	}

	@Override
	public boolean canUnequip(final ItemStack arg0, final EntityLivingBase arg1) {
		return true;
	}

	@Override
	public BaubleType getBaubleType(final ItemStack arg0) {
		return BaubleType.AMULET;
	}

	@Override //TODO
	public void onEquipped(final ItemStack arg0, final EntityLivingBase arg1) {

	}

	@Override //TODO
	public void onUnequipped(final ItemStack arg0, final EntityLivingBase arg1) {

	}

	@Override //TODO
	public void onWornTick(final ItemStack arg0, final EntityLivingBase arg1) {
		if (!arg1.worldObj.isRemote){
			if (this.getCharge(arg0) >= (1638400/4)){
				if (arg1.getHealth() < arg1.getMaxHealth()){
					final float rx = arg1.getMaxHealth()-arg1.getHealth();
					Logger.INFO("rx:"+rx);
					arg1.heal(rx*2);
					this.discharge(arg0, (1638400/4)*rx, 6, true, true, false);
					PlayerUtils.messagePlayer((EntityPlayer) arg1, "Your NanoBooster Whirs! Leaving you feeling stronger. It Healed "+rx+" hp.");
					PlayerUtils.messagePlayer((EntityPlayer) arg1, "You check it's remaining uses, it has "+this.secondsLeft(arg0)+".");
				}
			}
		}
	}

}
