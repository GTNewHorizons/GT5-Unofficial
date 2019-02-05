package gtPlusPlus.core.item.bauble;

import java.util.List;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.xmod.gregtech.common.helpers.ChargingHelper;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

@Optional.InterfaceList(value = { @Optional.Interface(iface = "baubles.api.IBauble", modid = "Baubles"),
		@Optional.Interface(iface = "baubles.api.BaubleType", modid = "Baubles") })
public class BatteryPackBaseBauble extends BaseBauble implements IElectricItem, IElectricItemManager, IBauble {

	final int mTier;
	private final double maxValueEU;

	public BatteryPackBaseBauble(int tier) {		
		super(BaubleType.BELT, "GTPP.BattPack.0" + tier + ".name", 0);
		mTier = tier;
		maxValueEU = GT_Values.V[mTier] * 20 * 300;
		String aUnlocalName = "GTPP.BattPack.0" + mTier + ".name";
		this.setUnlocalizedName(aUnlocalName);
		this.setTextureName(CORE.MODID + ":" + "chargepack/"+tier);
		this.setMaxDamage(27);
		this.setMaxStackSize(1);
		this.setNoRepair();
		this.setCreativeTab(AddToCreativeTab.tabMachines);		
		if (GameRegistry.findItem(CORE.MODID, aUnlocalName) == null) {
			GameRegistry.registerItem(this, aUnlocalName);			
		}		
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs, List itemList) {
		ItemStack itemStack = new ItemStack(this, 1);
		ItemStack charged;
		if (this.getEmptyItem(itemStack) == this) {
			charged = new ItemStack(this, 1);
			ElectricItem.manager.charge(charged, 0.0D, Integer.MAX_VALUE, true, false);
			itemList.add(charged);
		}
		if (this.getChargedItem(itemStack) == this) {
			charged = new ItemStack(this, 1);
			ElectricItem.manager.charge(charged, Double.POSITIVE_INFINITY, Integer.MAX_VALUE, true, false);
			itemList.add(charged);
		}

	}

	@Override
	public void onUpdate(final ItemStack itemStack, final World worldObj, final Entity player, final int p_77663_4_,
			final boolean p_77663_5_) {
		super.onUpdate(itemStack, worldObj, player, p_77663_4_, p_77663_5_);
	}

	@Override
	public boolean canProvideEnergy(final ItemStack itemStack) {
		double aItemCharge = ElectricItem.manager.getCharge(itemStack);		
		return aItemCharge > 0;
	}

	@Override
	public Item getChargedItem(final ItemStack itemStack) {
		final ItemStack x = itemStack.copy();
		x.setItemDamage(27);
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
		return mTier;
	}

	@Override
	public double getTransferLimit(final ItemStack itemStack) {
		return GT_Values.V[mTier];
	}

	@Override
	public String getItemStackDisplayName(final ItemStack p_77653_1_) {
		return (EnumChatFormatting.BLUE + super.getItemStackDisplayName(p_77653_1_) + EnumChatFormatting.GRAY);
	}

	@Override
	public double getDurabilityForDisplay(final ItemStack stack) {
		// return 1.0D - getEnergyStored(stack) / this.capacity;
		return 1.0D - (this.getCharge(stack) / this.getMaxCharge(stack));
	}

	@Override
	public boolean showDurabilityBar(final ItemStack stack) {
		return true;
	}

	public int secondsLeft(final ItemStack stack) {
		double r = 0;
		r = this.getCharge(stack) / (10000 * 20);
		return (int) MathUtils.decimalRounding(r);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
		list.add("");
		String aString1 = StatCollector.translateToLocal("GTPP.battpack.tooltip.1");
		String aString2 = StatCollector.translateToLocal("GTPP.battpack.tooltip.2");
		String aString3 = StatCollector.translateToLocal("GTPP.battpack.tooltip.3");
		String aString4 = StatCollector.translateToLocal("GTPP.battpack.tooltip.4");

		String aEuInfo = StatCollector.translateToLocal("GTPP.info.euInfo");
		String aTier = StatCollector.translateToLocal("GTPP.machines.tier");
		String aInputLimit = StatCollector.translateToLocal("GTPP.info.inputLimit");
		String aCurrentPower = StatCollector.translateToLocal("GTPP.info.currentPower");
		String aEU = StatCollector.translateToLocal("GTPP.info.eu");	
		String aEUT = aEU+"/t";

		list.add(EnumChatFormatting.GREEN + aString1 + EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.GREEN + aString2+" " + (int) getTransferLimit(stack) + aEUT +" "+ aString3 + EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.GREEN + aString4 + EnumChatFormatting.GRAY);
		list.add("");
		list.add(EnumChatFormatting.GOLD + aEuInfo + EnumChatFormatting.GRAY);
		list.add(EnumChatFormatting.GRAY + aTier+": [" + EnumChatFormatting.YELLOW + this.getTier(stack)
		+ EnumChatFormatting.GRAY + "] "+aInputLimit+": [" + EnumChatFormatting.YELLOW
		+ this.getTransferLimit(stack) + EnumChatFormatting.GRAY + aEUT);
		list.add(EnumChatFormatting.GRAY + aCurrentPower +": [" + EnumChatFormatting.YELLOW + (long) this.getCharge(stack)
		+ EnumChatFormatting.GRAY + aEU +"] [" + EnumChatFormatting.YELLOW
		+ MathUtils.findPercentage(this.getCharge(stack), this.getMaxCharge(stack)) + EnumChatFormatting.GRAY
		+ "%]");
		super.addInformation(stack, aPlayer, list, bool);
	}

	@Override
	public double charge(final ItemStack stack, final double amount, final int tier, final boolean ignoreTransferLimit,
			final boolean simulate) {

		if (!simulate) {
			ElectricItem.manager.charge(stack, amount, tier, true, simulate);

		}
		return ElectricItem.manager.charge(stack, amount, tier, true, simulate);
	}

	@Override
	public double discharge(final ItemStack stack, final double amount, final int tier,
			final boolean ignoreTransferLimit, final boolean externally, final boolean simulate) {
		if (!simulate) {
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
		return BaubleType.BELT;
	}

	@Override // TODO
	public void onEquipped(final ItemStack arg0, final EntityLivingBase arg1) {

	}

	@Override // TODO
	public void onUnequipped(final ItemStack arg0, final EntityLivingBase arg1) {

	}

	@Override // TODO
	public void onWornTick(final ItemStack aBaubleStack, final EntityLivingBase aPlayer) {
		if (!aPlayer.worldObj.isRemote) {
			if (this.getCharge(aBaubleStack) >= getTransferLimit(aBaubleStack)) {
				// Try Iterate Armour Slots of Player
				if (aPlayer instanceof EntityPlayer) {

					// amour
					for (final ItemStack aInvStack : ((EntityPlayer) aPlayer).inventory.armorInventory) {
						if (aInvStack != null) {
							if (aInvStack == aBaubleStack) {
								continue;
							}
							if (ChargingHelper.isItemValid(aInvStack)) {
								double aTransferRate = 0;
								final IElectricItem electricItem = (IElectricItem) aInvStack.getItem();
								if (electricItem != null) {
									aTransferRate = electricItem.getTransferLimit(aInvStack);
									double aItemCharge = ElectricItem.manager.getCharge(aInvStack);
									if (aItemCharge >= 0 && aItemCharge != electricItem.getMaxCharge(aInvStack)) {
										if (aItemCharge <= (electricItem.getMaxCharge(aInvStack) - aTransferRate)) {
												if (ElectricItem.manager.getCharge(aBaubleStack) >= aTransferRate) {
													if (ElectricItem.manager.getCharge(aInvStack) <= (electricItem.getMaxCharge(aInvStack) - aTransferRate)) {
														double d = ElectricItem.manager.charge(aInvStack, aTransferRate * 16, mTier, false, true);
														if (d > 0) {
														d = ElectricItem.manager.charge(aInvStack, aTransferRate * 16, mTier, false, false);
														ElectricItem.manager.discharge(aBaubleStack, d, mTier, false, true,	false);
														//Logger.INFO("Charging " + aInvStack.getDisplayName() + " | " + d	 + " | "+electricItem.getMaxCharge(aInvStack));
														}
													}
													else {
														//Logger.INFO("5");
													}
												}
												else {
													//Logger.INFO("4");
												}
											}
											else {
												//Logger.INFO("3");
											}

									}
									else {
										//Logger.INFO("1");
									}
								}

							}
						}
						if (this.getCharge(aBaubleStack) > 0) {
							continue;
						} else {
							break;
						}
					}

					// Hotbar Slots
					int aSlotCounter = 0;
					for (final ItemStack aInvStack : ((EntityPlayer) aPlayer).inventory.mainInventory) {
						if (aSlotCounter > (InventoryPlayer.getHotbarSize() - 1)) {
							break;
						}
						aSlotCounter++;
						if (aInvStack != null) {
								if (aInvStack == aBaubleStack) {
									continue;
								}
								if (ChargingHelper.isItemValid(aInvStack)) {
									double aTransferRate = 0;
									final IElectricItem electricItem = (IElectricItem) aInvStack.getItem();
									if (electricItem != null) {
										aTransferRate = electricItem.getTransferLimit(aInvStack);
										double aItemCharge = ElectricItem.manager.getCharge(aInvStack);
										if (aItemCharge >= 0 && aItemCharge != electricItem.getMaxCharge(aInvStack)) {
											if (aItemCharge <= (electricItem.getMaxCharge(aInvStack) - aTransferRate)) {
													if (ElectricItem.manager.getCharge(aBaubleStack) >= aTransferRate) {
														if (ElectricItem.manager.getCharge(aInvStack) <= (electricItem.getMaxCharge(aInvStack) - aTransferRate)) {
															double d = ElectricItem.manager.charge(aInvStack, aTransferRate, mTier, false, true);
															if (d > 0) {
															d = ElectricItem.manager.charge(aInvStack, aTransferRate, mTier, false, false);
															ElectricItem.manager.discharge(aBaubleStack, d, mTier, false, true,	false);
															//Logger.INFO("Charging " + aInvStack.getDisplayName() + " | " + d	 + " | "+electricItem.getMaxCharge(aInvStack));
															}
														}
														else {
															//Logger.INFO("5");
														}
													}
													else {
														//Logger.INFO("4");
													}
												}
												else {
													//Logger.INFO("3");
												}

										}
										else {
											//Logger.INFO("1");
										}
									}
								}							
						}
						if (this.getCharge(aBaubleStack) > 0) {
							continue;
						} else {
							break;
						}
					}
				}
			}
		}
	}

}
