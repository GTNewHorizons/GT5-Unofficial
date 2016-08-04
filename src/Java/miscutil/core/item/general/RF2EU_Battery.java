package miscutil.core.item.general;

import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;

import java.util.List;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import miscutil.core.util.item.UtilsItems;
import miscutil.core.util.math.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import cofh.api.energy.ItemEnergyContainer;
import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

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
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName(unlocalizedName);
		this.setMaxStackSize(1);
		this.setTextureName(CORE.MODID + ":" + "itemIngot");
		this.thisStack = UtilsItems.getSimpleStack(this);
		GameRegistry.registerItem(this, unlocalizedName);
	}

	@Override
	public void onUpdate(ItemStack itemStack, World worldObj, Entity player, int p_77663_4_, boolean p_77663_5_) {
		getEnergyStored(itemStack);
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
		return 3;
	}

	@Override
	public double getTransferLimit(ItemStack itemStack) {
		return 8196;
	}

	@Override
	public String getItemStackDisplayName(ItemStack p_77653_1_) {

		return ("Universally chargeable battery");
	}

	@Override
	public double getDurabilityForDisplay(ItemStack stack)
	{
		//return 1.0D - getEnergyStored(stack) / this.capacity;
		return MathUtils.findPercentage(getEnergyStored(stack), getMaxEnergyStored(stack));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
	{
		int i = 30;

		float f13 = (float)(Minecraft.getSystemTime() % 6000L) / 3000.0F * 3.141592F * 2.0F;

		float t = 0.9F + 0.1F * MathHelper.cos(f13);

		double v = 1.0D - getDurabilityForDisplay(par1ItemStack);

		int r = i + (int)(v * (255 - i) * t);
		if (r > 255) {
			r = 255;
		}
		int g = i + (int)(v * (64 - i) * t);

		return r << 16 | g << 8 | i;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack)
	{
		return false;
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {			
		list.add("IC2/EU Information");	
		list.add("Tier: ["+getTier(thisStack)+"]"+" Current Power: ["+(long) getCharge(stack)+"/EU]");
		list.add("Transfer Limit: ["+getTransferLimit(thisStack)+"Eu/t]"+" Burn Time: ["+getBurnTime(stack)/20+"s]");
		list.add("");
		list.add("RF Information");
		list.add("Extraction Rate: [" + this.maxExtract + "Rf/t]" + " Insert Rate: [" + this.maxReceive+"Rf/t]");
		list.add("Current Charge: ["+getEnergyStored(stack) + "Rf / " + getMaxEnergyStored(stack)+"Rf] "+MathUtils.findPercentage(getEnergyStored(stack), getMaxEnergyStored(stack))+"%");
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public ItemStack getContainerItem(ItemStack itemStack)
	{
		ItemStack newItem = itemStack.copy();
		newItem.stackSize = 1;
		extractEnergy(newItem, 150000, false);
		return newItem;
	}

	@Override
	public boolean hasContainerItem(ItemStack stack)
	{
		return true;
	}

	@Override
	public int getBurnTime(ItemStack fuel) {
		if ((fuel == null) || (fuel.getItem() != this)) {
			return 0;
		}
		return extractEnergy(fuel, 150000, true) / 50 / 100;
	}

	@Override
	public double charge(ItemStack stack, double amount, int tier,
			boolean ignoreTransferLimit, boolean simulate) {		 
		if (stack.stackTagCompound == null) {
			stack.stackTagCompound = new NBTTagCompound();
		}
		int energy = stack.stackTagCompound.getInteger("Energy");
		int energyReceived = Math.min(this.capacity - energy, Math.min(this.maxReceive, maxReceive));
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
	public double discharge(ItemStack stack, double amount, int tier,
			boolean ignoreTransferLimit, boolean externally, boolean simulate) {
		if ((stack.stackTagCompound == null) || (!stack.stackTagCompound.hasKey("Energy"))) {
			return 0;
		}
		int energy = stack.stackTagCompound.getInteger("Energy");
		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
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
	public int receiveEnergy(ItemStack container, int maxReceive, boolean simulate)
	{
		if (container.stackTagCompound == null) {
			container.stackTagCompound = new NBTTagCompound();
		}
		int energy = container.stackTagCompound.getInteger("Energy");
		int energyReceived = Math.min(this.capacity - energy, Math.min(this.maxReceive, maxReceive));
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
	public int extractEnergy(ItemStack container, int maxExtract, boolean simulate)
	{
		if ((container.stackTagCompound == null) || (!container.stackTagCompound.hasKey("Energy"))) {
			return 0;
		}
		int energy = container.stackTagCompound.getInteger("Energy");
		int energyExtracted = Math.min(energy, Math.min(this.maxExtract, maxExtract));
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
	public int getEnergyStored(ItemStack container)
	{
		if ((container.stackTagCompound == null) || (!container.stackTagCompound.hasKey("Energy"))) {
			return 0;
		}
		int energy = container.stackTagCompound.getInteger("Energy");
		ElectricItem.manager.discharge(container, ElectricItem.manager.getCharge(container), 3, true, true, false);
		ElectricItem.manager.charge(container, energy/rfPerEU, 3, true, false);
		return energy;
	}

}
