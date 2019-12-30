package gtPlusPlus.core.item.general;

import java.util.List;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.core.util.sys.KeyboardUtils;
import gtPlusPlus.xmod.gregtech.common.helpers.ChargingHelper;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
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
		super.onUpdate(itemStack, worldObj, player, p_77663_4_, p_77663_5_);
	}

	@Override
	public boolean canProvideEnergy(final ItemStack itemStack) {
		return false;
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		
		String aString1 = StatCollector.translateToLocal("GTPP.nanohealer.tooltip.1");
		String aString2 = StatCollector.translateToLocal("GTPP.nanohealer.tooltip.2");
		String aString3 = StatCollector.translateToLocal("GTPP.nanohealer.tooltip.3");
		String aString4 = StatCollector.translateToLocal("GTPP.nanohealer.tooltip.4");
		String aString5 = StatCollector.translateToLocal("GTPP.nanohealer.tooltip.5");
		String aStringTooltip = StatCollector.translateToLocal("GTPP.nanohealer.hidden");
		String aEuInfo = StatCollector.translateToLocal("GTPP.info.euInfo");
		String aTier = StatCollector.translateToLocal("GTPP.machines.tier");
		String aInputLimit = StatCollector.translateToLocal("GTPP.info.inputLimit");
		String aCurrentPower = StatCollector.translateToLocal("GTPP.info.currentPower");
		String aEU = StatCollector.translateToLocal("GTPP.info.eu");
		String aEUT = aEU+"/t";
		boolean isShowing = getShowMessages(stack);	
		
		list.add("");	
		
		list.add(EnumChatFormatting.GREEN + aString1 + EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.GREEN + aString2 + EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.GREEN + aString3 + EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.RED + aString4 + EnumChatFormatting.GRAY);
		
		list.add("");
		
		list.add(EnumChatFormatting.GOLD + aEuInfo + EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.GRAY + aTier+": [" + EnumChatFormatting.YELLOW + this.getTier(stack)
		+ EnumChatFormatting.GRAY + "] "+aInputLimit+": [" + EnumChatFormatting.YELLOW
		+ this.getTransferLimit(stack) + EnumChatFormatting.GRAY + aEUT);
		list.add(EnumChatFormatting.GRAY + aCurrentPower +": [" + EnumChatFormatting.YELLOW + (long) this.getCharge(stack)
		+ EnumChatFormatting.GRAY + aEU +"] [" + EnumChatFormatting.YELLOW
		+ MathUtils.findPercentage(this.getCharge(stack), this.getMaxCharge(stack)) + EnumChatFormatting.GRAY
		+ "%]");
		list.add(EnumChatFormatting.GOLD + aString5 + EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.GOLD + aStringTooltip + " " + (!isShowing ? EnumChatFormatting.DARK_GREEN : EnumChatFormatting.DARK_RED) + !isShowing + EnumChatFormatting.GRAY);
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
	public void onWornTick(final ItemStack aBaubleStack, final EntityLivingBase arg1) {
		if (arg1 != null && arg1.worldObj != null && !arg1.worldObj.isRemote){
			
			//Try Charge First

			//Inv Slots
			for (final ItemStack aInvStack : ((EntityPlayer) arg1).inventory.mainInventory) {
				if (aInvStack == aBaubleStack) {
					continue;
				}
				
				if (this.getCharge(aBaubleStack) == this.getMaxCharge(aBaubleStack)) {
					break;
				}
				
				if (aInvStack != null) {
					if (ChargingHelper.isItemValid(aInvStack)) {
						
						
						double aTransferRate;	
						double aCurrentChargeForThisBauble;
						int mTier;
						final IElectricItem electricItem = (IElectricItem) aInvStack.getItem();
						
						if (electricItem != null) {
							
							aTransferRate = electricItem.getTransferLimit(aInvStack);	
							mTier = electricItem.getTier(aInvStack);	
							aCurrentChargeForThisBauble = ElectricItem.manager.getCharge(aBaubleStack);
							
							if (aCurrentChargeForThisBauble < maxValueEU) {								
								if ((ElectricItem.manager.getCharge(aInvStack) >= aTransferRate)) {
									if (electricItem.canProvideEnergy(aInvStack)) {
										double d = ElectricItem.manager.discharge(aInvStack, aTransferRate, mTier, false, true, false);
										//Logger.INFO("Charging from "+aInvStack.getDisplayName() +" | "+d);
										ElectricItem.manager.charge(aBaubleStack, d, mTier, true, false);	
									}
								}																
							}	
						}
					}
				}
				if (this.getCharge(aBaubleStack) <= (this.getMaxCharge(aBaubleStack) - getTransferLimit(aBaubleStack))) {
					continue;
				}
				else {
					break;
				}						
			}
			
			
			//Try Heal
			if (this.getCharge(aBaubleStack) > 0){

				//health Check
				float hp = 0;
				if (arg1.getHealth() < arg1.getMaxHealth()){
					final float rx = arg1.getMaxHealth()-arg1.getHealth();
					Logger.INFO("rx:"+rx);
					arg1.heal(rx*2);	
					hp = rx;
					this.discharge(aBaubleStack, (1638400)*rx, 6, true, true, false);
				}

				//Hunger Check
				int hunger = 0;
				if (arg1 instanceof EntityPlayerMP) {
					EntityPlayerMP g = (EntityPlayerMP) arg1;
					if (g != null) {
						hunger = 20 - g.getFoodStats().getFoodLevel();
						g.getFoodStats().setFoodLevel(g.getFoodStats().getFoodLevel() + hunger);
						this.discharge(aBaubleStack, (1638400) * hunger, 6, true, true, false);
					}
				}

				//Saturation Check
				float saturation = 0;
				if (arg1 instanceof EntityPlayerMP) {
					EntityPlayerMP g = (EntityPlayerMP) arg1;
					if (g != null) {
						saturation = 5f - g.getFoodStats().getSaturationLevel();
						g.getFoodStats().setFoodSaturationLevel(g.getFoodStats().getSaturationLevel() + saturation);
						this.discharge(aBaubleStack, (1638400) * saturation, 6, true, true, false);
					}
				}


				saturation = (int) saturation;
				
				//Only show Messages if they're enabled.
				if (getShowMessages(aBaubleStack)) {
					if (hp > 0 || hunger > 0 || saturation > 0)
						PlayerUtils.messagePlayer((EntityPlayer) arg1,
								"Your NanoBooster Whirs! Leaving you feeling stronger.");
					
					if (hp > 0)
						PlayerUtils.messagePlayer((EntityPlayer) arg1, "Healed " + hp + " hp.");
					
					if (hunger > 0)
						PlayerUtils.messagePlayer((EntityPlayer) arg1, "Healed " + hunger + " hunger.");
					
					if (saturation > 0)
						PlayerUtils.messagePlayer((EntityPlayer) arg1, "Satured Hunger by " + saturation + ".");
					
					if (hp > 0 || hunger > 0 || saturation > 0)
						PlayerUtils.messagePlayer((EntityPlayer) arg1,
								"You check it's remaining uses, it has " + (int) this.secondsLeft(aBaubleStack) + ".");

				}




			}
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	
	private static boolean createNBT(ItemStack rStack){
		final NBTTagCompound tagMain = new NBTTagCompound();
		tagMain.setBoolean("ShowMSG", false);
		rStack.setTagCompound(tagMain);		
		return true;
	}
	
	public static final boolean getShowMessages(final ItemStack aStack) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT == null) {
			if (!createNBT(aStack)) {
				return false;
			}	
			else {
				aNBT = aStack.getTagCompound();
			}
		}
		return aNBT.getBoolean("ShowMSG");
		
	}

	public static final boolean setShowMessages(final ItemStack aStack, final boolean aShow) {
		NBTTagCompound aNBT = aStack.getTagCompound();
		if (aNBT == null) {
			if (!createNBT(aStack)) {
				return false;
			} else {
				aNBT = aStack.getTagCompound();
			}
		}
		aNBT.setBoolean("ShowMSG", aShow);
		return true;
	}

	@Override
	public boolean onItemUse(ItemStack p_77648_1_, EntityPlayer p_77648_2_, World p_77648_3_, int p_77648_4_,
			int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		// TODO Auto-generated method stub
		return super.onItemUse(p_77648_1_, p_77648_2_, p_77648_3_, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_,
				p_77648_9_, p_77648_10_);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
		ItemStack superStack = super.onItemRightClick(aStack, aWorld, aPlayer);		
		boolean isShiftHeld = KeyboardUtils.isShiftKeyDown();		
		if (isShiftHeld) {
			boolean oldState = getShowMessages(superStack);
			boolean newState = Utils.invertBoolean(oldState);
			ItemHealingDevice.setShowMessages(superStack, newState);
			PlayerUtils.messagePlayer(aPlayer, (!oldState ? "Showing info messages" : "Hiding info messages"));
		}		
		return superStack;
	}

	@Override
	public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
		return true;
	}
	
	
	

}
