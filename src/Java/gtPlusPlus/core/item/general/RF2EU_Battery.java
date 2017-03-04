package gtPlusPlus.core.item.general;

import java.util.List;

import cofh.energy.ItemEnergyContainer;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.item.ItemUtils;
import gtPlusPlus.core.util.math.MathUtils;
import ic2.api.item.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class RF2EU_Battery extends ItemEnergyContainer implements IElectricItem, IElectricItemManager, IFuelHandler{

	public static int rfPerEU = 4;
	private final String unlocalizedName = "rfEUBattery";
	private final ItemStack thisStack;
	private final static int maxValueEU = 100000000;
	private final static int maxValueRF = maxValueEU * rfPerEU;
	protected double chargeEU = 0;

	public RF2EU_Battery(){
		super(maxValueRF, maxValueRF, maxValueRF);
		GameRegistry.registerFuelHandler(this);
		//this.setMaxDamage(Integer.MAX_VALUE);
		//this.setDamage(UtilsItems.getSimpleStack(this), 0);
		this.setCreativeTab(AddToCreativeTab.tabMachines);
		this.setUnlocalizedName(this.unlocalizedName);
		this.setMaxStackSize(1);
		this.setTextureName(CORE.MODID + ":" + "itemIngot");
		this.thisStack = ItemUtils.getSimpleStack(this);
		GameRegistry.registerItem(this, this.unlocalizedName);
	}

	@Override
	public void onUpdate(final ItemStack itemStack, final World worldObj, final Entity player, final int p_77663_4_, final boolean p_77663_5_) {
		this.getEnergyStored(itemStack);
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
		return 3;
	}

	@Override
	public double getTransferLimit(final ItemStack itemStack) {
		return 8196;
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {

		return ("Universally Chargeable Battery");
	}

	@Override
	public double getDurabilityForDisplay(final ItemStack stack)
	{
		//return 1.0D - getEnergyStored(stack) / this.capacity;
		return MathUtils.findPercentage(this.getEnergyStored(stack), this.getMaxEnergyStored(stack));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(final ItemStack par1ItemStack, final int par2)
	{
		final int i = 30;

		final float f13 = (((Minecraft.getSystemTime() % 6000L) / 3000.0F) * CORE.PI * 2.0F);

		final float t = 0.9F + (0.1F * MathHelper.cos(f13));

		final double v = 1.0D - this.getDurabilityForDisplay(par1ItemStack);

		int r = i + (int)(v * (255 - i) * t);
		if (r > 255) {
			r = 255;
		}
		final int g = i + (int)(v * (64 - i) * t);

		return (r << 16) | (g << 8) | i;
	}

	@Override
	public boolean showDurabilityBar(final ItemStack stack)
	{
		return false;
	}

	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add(EnumChatFormatting.YELLOW+"IC2/EU Information"+EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.GRAY+"Tier: ["+EnumChatFormatting.YELLOW+this.getTier(this.thisStack)+EnumChatFormatting.GRAY+"] Current Power: ["+EnumChatFormatting.YELLOW+(long) this.getCharge(stack)+EnumChatFormatting.GRAY+"/EU]");
		list.add(EnumChatFormatting.GRAY+"Transfer Limit: ["+EnumChatFormatting.YELLOW+this.getTransferLimit(this.thisStack)+ EnumChatFormatting.GRAY +"Eu/t]" +"Burn Time: ["+EnumChatFormatting.YELLOW+(this.getBurnTime(stack)/20)+EnumChatFormatting.GRAY+"s]");
		list.add("");
		list.add(EnumChatFormatting.RED+"RF Information");
		list.add(EnumChatFormatting.GRAY+"Extraction Rate: [" +EnumChatFormatting.RED+ this.maxExtract + EnumChatFormatting.GRAY + "Rf/t]" + " Insert Rate: [" +EnumChatFormatting.RED+ this.maxReceive+EnumChatFormatting.GRAY+"Rf/t]");
		list.add(EnumChatFormatting.GRAY+"Current Charge: ["+EnumChatFormatting.RED+this.getEnergyStored(stack) + EnumChatFormatting.GRAY + "Rf / " + this.getMaxEnergyStored(stack)+"Rf] "+EnumChatFormatting.RED+MathUtils.findPercentage(this.getEnergyStored(stack), this.getMaxEnergyStored(stack))+EnumChatFormatting.GRAY+"%");
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public ItemStack getContainerItem(final ItemStack itemStack)
	{
		final ItemStack newItem = itemStack.copy();
		newItem.stackSize = 1;
		this.extractEnergy(newItem, 150000, false);
		return newItem;
	}

	@Override
	public boolean hasContainerItem(final ItemStack stack)
	{
		return true;
	}

	@Override
	public int getBurnTime(final ItemStack fuel) {
		if ((fuel == null) || (fuel.getItem() != this)) {
			return 0;
		}
		return this.extractEnergy(fuel, 150000, true) / 50 / 100;
	}

	@Override
	public double charge(final ItemStack stack, final double amount, final int tier,
			final boolean ignoreTransferLimit, final boolean simulate) {
		if (stack.stackTagCompound == null) {
			stack.stackTagCompound = new NBTTagCompound();
		}
		int energy = stack.stackTagCompound.getInteger("Energy");
		final int energyReceived = Math.min(this.capacity - energy, Math.min(this.maxReceive, this.maxReceive));
		if (!simulate)
		{
			energy += energyReceived;
			stack.stackTagCompound.setInteger("Energy", energy);
			ElectricItem.manager.discharge(stack, ElectricItem.manager.getCharge(stack), 3, true, true, false);
			ElectricItem.manager.charge(stack, energy/rfPerEU, 3, true, false);

		}
		return ElectricItem.manager.charge(stack, amount, tier, ignoreTransferLimit, simulate);
	}

	@Override
	public double discharge(final ItemStack stack, final double amount, final int tier,
			final boolean ignoreTransferLimit, final boolean externally, final boolean simulate) {
		if ((stack.stackTagCompound == null) || (!stack.stackTagCompound.hasKey("Energy"))) {
			final double euCharge = this.getCharge(ItemUtils.getSimpleStack(this));
			if ((euCharge != 0) && (euCharge >= 1)){
				return (int) (MathUtils.decimalRoundingToWholes(euCharge*rfPerEU));
			}
			return 0;
		}
		int energy = stack.stackTagCompound.getInteger("Energy");
		final int energyExtracted = Math.min(energy, Math.min(this.maxExtract, this.maxExtract));
		if (!simulate)
		{
			energy -= energyExtracted;
			stack.stackTagCompound.setInteger("Energy", energy);
			ElectricItem.manager.discharge(stack, ElectricItem.manager.getCharge(stack), 3, true, true, false);
			ElectricItem.manager.charge(stack, energy/rfPerEU, 3, true, false);
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
	public int receiveEnergy(final ItemStack container, final int maxReceive, final boolean simulate)
	{
		if ((container.stackTagCompound == null) || (!container.stackTagCompound.hasKey("Energy"))) {
			final double euCharge = this.getCharge(ItemUtils.getSimpleStack(this));
			if ((euCharge != 0) && (euCharge >= 1)){
				return (int) (MathUtils.decimalRoundingToWholes(euCharge*rfPerEU));
			}
			return 0;
		}
		int energy = container.stackTagCompound.getInteger("Energy");
		final int energyReceived = Math.min(this.capacity - energy, Math.min(this.maxReceive, maxReceive));
		if (!simulate)
		{
			energy += energyReceived;
			container.stackTagCompound.setInteger("Energy", energy);
			ElectricItem.manager.discharge(container, ElectricItem.manager.getCharge(container), 3, true, true, false);
			ElectricItem.manager.charge(container, energy/rfPerEU, 3, true, false);

		}
		return energyReceived;
	}

	@Override
	public int extractEnergy(final ItemStack container, final int maxExtract, final boolean simulate)
	{
		if ((container.stackTagCompound == null) || (!container.stackTagCompound.hasKey("Energy"))) {
			final double euCharge = this.getCharge(ItemUtils.getSimpleStack(this));
			if ((euCharge != 0) && (euCharge >= 1)){
				return (int) (MathUtils.decimalRoundingToWholes(euCharge*rfPerEU));
			}
			return 0;
		}
		int energy = container.stackTagCompound.getInteger("Energy");
		final int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
		if (!simulate)
		{
			energy -= energyExtracted;
			container.stackTagCompound.setInteger("Energy", energy);
			ElectricItem.manager.discharge(container, ElectricItem.manager.getCharge(container), 3, true, true, false);
			ElectricItem.manager.charge(container, energy/rfPerEU, 3, true, false);
		}
		return energyExtracted;
	}

	@Override
	public int getEnergyStored(final ItemStack container)
	{
		if ((container.stackTagCompound == null) || (!container.stackTagCompound.hasKey("Energy"))) {
			final double euCharge = this.getCharge(ItemUtils.getSimpleStack(this));
			if ((euCharge != 0) && (euCharge >= 1)){
				return (int) (MathUtils.decimalRoundingToWholes(euCharge*rfPerEU));
			}
			return 0;
		}
		final int energy = container.stackTagCompound.getInteger("Energy");
		ElectricItem.manager.discharge(container, ElectricItem.manager.getCharge(container), 3, true, true, false);
		ElectricItem.manager.charge(container, energy/rfPerEU, 3, true, false);
		return energy;
	}

}
