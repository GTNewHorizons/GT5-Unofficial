package gtPlusPlus.core.item.base;

import static gregtech.api.enums.GT_Values.D1;
import static gregtech.api.enums.GT_Values.V;

import java.util.*;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.objects.ItemData;
import gregtech.api.util.*;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import ic2.api.item.*;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;

public class BaseEuItem extends Item implements ISpecialElectricItem, IElectricItemManager {

	/* ---------- CONSTRUCTOR AND MEMBER VARIABLES ---------- */
	private final HashMap<Short, ArrayList<IItemBehaviour<BaseEuItem>>> mItemBehaviors = new HashMap<>();
	public final short mOffset, mItemAmount;
	public final BitSet mEnabledItems;
	public final BitSet mVisibleItems;
	public final IIcon[][] mIconList;
	/** The unlocalized name of this item. */
	private String unlocalizedName;

	private final ArrayList<Pair<Integer, EnumRarity>> rarity = new ArrayList<>();
	private final ArrayList<Pair<Integer, EnumChatFormatting>> descColour = new ArrayList<>();
	private final ArrayList<Pair<Integer, String>> itemName = new ArrayList<>();
	private final ArrayList<Pair<Integer, String>> itemDescription = new ArrayList<>();
	private final ArrayList<Pair<Integer, Boolean>> hasEffect = new ArrayList<>();

	public final HashMap<Short, Long[]> mElectricStats = new HashMap<>();
	public final HashMap<Short, Short> mBurnValues = new HashMap<>();

	public BaseEuItem() {
		this("MU-metaitem.02", AddToCreativeTab.tabOther, (short) 1000, (short) 31766);
	}

	public BaseEuItem(final String unlocalizedName, final CreativeTabs creativeTab, final short aOffset, final short aItemAmount) {
		this.mEnabledItems = new BitSet(aItemAmount);
		this.mVisibleItems = new BitSet(aItemAmount);
		this.mOffset = (short) Math.min(32766, aOffset);
		this.mItemAmount = (short) Math.min(aItemAmount, 32766 - this.mOffset);
		this.mIconList = new IIcon[aItemAmount][1];
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(creativeTab);
		this.setMaxStackSize(1);
		GameRegistry.registerItem(this, unlocalizedName);
	}


	public void registerItem(final int id, final String localizedName, final long euStorage, final int tier, final String description) {
		this.registerItem(id, localizedName, euStorage, (short) tier, description, EnumRarity.common, EnumChatFormatting.GRAY, false);
	}

	public void registerItem(final int id, final String localizedName, final long euStorage, final int tier, final String description, final int burnTime) {
		this.registerItem(id, localizedName, euStorage, (short) tier, description, EnumRarity.common, EnumChatFormatting.GRAY, false);
		this.setBurnValue(id, burnTime);
	}


	public void registerItem(final int id, final String localizedName, final long euStorage, final short tier, final String description, final EnumRarity regRarity, final EnumChatFormatting colour, final boolean Effect) {
		this.addItem(id, localizedName, EnumChatFormatting.YELLOW+"Electric", new Object[]{});
		this.setElectricStats(this.mOffset + id, euStorage, GT_Values.V[tier], tier, -3L, true);
		this.rarity.add(new Pair<>(id, regRarity));
		this.itemName.add(new Pair<>(id, localizedName));
		this.itemDescription.add(new Pair<>(id, description));
		this.descColour.add(new Pair<>(id, colour));
		this.hasEffect.add(new Pair<>(id, Effect));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack){
		if (this.rarity.get(par1ItemStack.getItemDamage()-this.mOffset) != null) {
			return this.rarity.get(par1ItemStack.getItemDamage()-this.mOffset).getValue();
		}
		return EnumRarity.common;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack){
		if (this.hasEffect.get(par1ItemStack.getItemDamage()-this.mOffset) != null) {
			return this.hasEffect.get(par1ItemStack.getItemDamage()-this.mOffset).getValue();
		}
		return false;
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public final void addInformation(final ItemStack aStack, final EntityPlayer aPlayer, List aList, final boolean aF3_H) {
		//aList.add("Meta: "+(aStack.getItemDamage()-mOffset));
		if ((this.descColour.get(aStack.getItemDamage()-this.mOffset) != null) && (this.itemDescription.get(aStack.getItemDamage()-this.mOffset) != null)) {
			aList.add(this.descColour.get(aStack.getItemDamage()-this.mOffset).getValue()+this.itemDescription.get(aStack.getItemDamage()-this.mOffset).getValue());
		}
		final String tKey = this.getUnlocalizedName(aStack) + ".tooltip", tString = GT_LanguageManager.getTranslation(tKey);
		if (GT_Utility.isStringValid(tString) && !tKey.equals(tString)) {
			aList.add(tString);
		}
		final Long[] tStats = this.getElectricStats(aStack);
		if (tStats != null) {
			if (tStats[3] > 0) {
				aList.add(EnumChatFormatting.AQUA + "Contains " + GT_Utility.formatNumbers(tStats[3]) + " EU   Tier: " + (tStats[2] >= 0 ? tStats[2] : 0) + EnumChatFormatting.GRAY);
			} else {
				final long tCharge = this.getRealCharge(aStack);
				if ((tStats[3] == -2) && (tCharge <= 0)) {
					aList.add(EnumChatFormatting.AQUA + "Empty. You should recycle it properly." + EnumChatFormatting.GRAY);
				} else {
					aList.add(EnumChatFormatting.AQUA + "" + GT_Utility.formatNumbers(tCharge) + " / " + GT_Utility.formatNumbers(Math.abs(tStats[0])) + " EU - Voltage: " + V[(int) (tStats[2] >= 0 ? tStats[2] < V.length ? tStats[2] : V.length - 1 : 1)] + EnumChatFormatting.GRAY);
				}
			}
		}
		final ArrayList<IItemBehaviour<BaseEuItem>> tList = this.mItemBehaviors.get((short) this.getDamage(aStack));
		if (tList != null) {
			for (final IItemBehaviour<BaseEuItem> tBehavior : tList) {
				aList = tBehavior.getAdditionalToolTips(this, aList, aStack);
			}
		}
	}


	@Override
	public final Item getChargedItem(final ItemStack itemStack) {
		return this;
	}

	@Override
	public final Item getEmptyItem(final ItemStack itemStack) {
		return this;
	}

	@Override
	public final double getMaxCharge(final ItemStack aStack) {
		final Long[] tStats = this.getElectricStats(aStack);
		if (tStats == null) {
			return 0;
		}
		return Math.abs(tStats[0]);
	}

	@Override
	public final double getTransferLimit(final ItemStack aStack) {
		final Long[] tStats = this.getElectricStats(aStack);
		if (tStats == null) {
			return 0;
		}
		return Math.max(tStats[1], tStats[3]);
	}

	@Override
	public final int getTier(final ItemStack aStack) {
		final Long[] tStats = this.getElectricStats(aStack);
		return (int) (tStats == null ? Integer.MAX_VALUE : tStats[2]);
	}

	@Override
	public final double charge(final ItemStack aStack, final double aCharge, final int aTier, final boolean aIgnoreTransferLimit, final boolean aSimulate) {
		final Long[] tStats = this.getElectricStats(aStack);
		if ((tStats == null) || (tStats[2] > aTier) || !((tStats[3] == -1) || (tStats[3] == -3) || ((tStats[3] < 0) && (aCharge == Integer.MAX_VALUE))) || (aStack.stackSize != 1)) {
			return 0;
		}
		final long tChargeBefore = this.getRealCharge(aStack), tNewCharge = aCharge == Integer.MAX_VALUE ? Long.MAX_VALUE : Math.min(Math.abs(tStats[0]), tChargeBefore + (aIgnoreTransferLimit ? (long) aCharge : Math.min(tStats[1], (long) aCharge)));
		if (!aSimulate) {
			this.setCharge(aStack, tNewCharge);
		}
		return tNewCharge - tChargeBefore;
	}

	@Override
	public final double discharge(final ItemStack aStack, final double aCharge, final int aTier, final boolean aIgnoreTransferLimit, final boolean aBatteryAlike, final boolean aSimulate) {
		final Long[] tStats = this.getElectricStats(aStack);
		if ((tStats == null) || (tStats[2] > aTier)) {
			return 0;
		}
		if (aBatteryAlike && !this.canProvideEnergy(aStack)) {
			return 0;
		}
		if (tStats[3] > 0) {
			if ((aCharge < tStats[3]) || (aStack.stackSize < 1)) {
				return 0;
			}
			if (!aSimulate) {
				aStack.stackSize--;
			}
			return tStats[3];
		}
		final long tChargeBefore = this.getRealCharge(aStack), tNewCharge = Math.max(0, tChargeBefore - (aIgnoreTransferLimit ? (long) aCharge : Math.min(tStats[1], (long) aCharge)));
		if (!aSimulate) {
			this.setCharge(aStack, tNewCharge);
		}
		return tChargeBefore - tNewCharge;
	}

	@Override
	public final double getCharge(final ItemStack aStack) {
		return this.getRealCharge(aStack);
	}

	@Override
	public final boolean canUse(final ItemStack aStack, final double aAmount) {
		return this.getRealCharge(aStack) >= aAmount;
	}

	@Override
	public final boolean use(final ItemStack aStack, final double aAmount, final EntityLivingBase aPlayer) {
		this.chargeFromArmor(aStack, aPlayer);
		if ((aPlayer instanceof EntityPlayer) && ((EntityPlayer) aPlayer).capabilities.isCreativeMode) {
			return true;
		}
		final double tTransfer = this.discharge(aStack, aAmount, Integer.MAX_VALUE, true, false, true);
		if (tTransfer == aAmount) {
			this.discharge(aStack, aAmount, Integer.MAX_VALUE, true, false, false);
			this.chargeFromArmor(aStack, aPlayer);
			return true;
		}
		this.discharge(aStack, aAmount, Integer.MAX_VALUE, true, false, false);
		this.chargeFromArmor(aStack, aPlayer);
		return false;
	}

	@Override
	public final boolean canProvideEnergy(final ItemStack aStack) {
		final Long[] tStats = this.getElectricStats(aStack);
		if (tStats == null) {
			return false;
		}
		return (tStats[3] > 0) || ((aStack.stackSize == 1) && ((tStats[3] == -2) || (tStats[3] == -3)));
	}

	@Override
	public final void chargeFromArmor(final ItemStack aStack, final EntityLivingBase aPlayer) {
		if ((aPlayer == null) || aPlayer.worldObj.isRemote) {
			return;
		}
		for (int i = 1; i < 5; i++) {
			final ItemStack tArmor = aPlayer.getEquipmentInSlot(i);
			if (GT_ModHandler.isElectricItem(tArmor)) {
				final IElectricItem tArmorItem = (IElectricItem) tArmor.getItem();
				if (tArmorItem.canProvideEnergy(tArmor) && (tArmorItem.getTier(tArmor) >= this.getTier(aStack))) {
					final double tCharge = ElectricItem.manager.discharge(tArmor, this.charge(aStack, Integer.MAX_VALUE - 1, Integer.MAX_VALUE, true, true), Integer.MAX_VALUE, true, true, false);
					if (tCharge > 0) {
						this.charge(aStack, tCharge, Integer.MAX_VALUE, true, false);
						if (aPlayer instanceof EntityPlayer) {
							final Container tContainer = ((EntityPlayer) aPlayer).openContainer;
							if (tContainer != null) {
								tContainer.detectAndSendChanges();
							}
						}
					}
				}
			}
		}
	}

	public final long getRealCharge(final ItemStack aStack) {
		final Long[] tStats = this.getElectricStats(aStack);
		if (tStats == null) {
			return 0;
		}
		if (tStats[3] > 0) {
			return (int) (long) tStats[3];
		}
		final NBTTagCompound tNBT = aStack.getTagCompound();
		return tNBT == null ? 0 : tNBT.getLong("GT.ItemCharge");
	}

	public final boolean setCharge(final ItemStack aStack, long aCharge) {
		final Long[] tStats = this.getElectricStats(aStack);
		if ((tStats == null) || (tStats[3] > 0)) {
			return false;
		}
		NBTTagCompound tNBT = aStack.getTagCompound();
		if (tNBT == null) {
			tNBT = new NBTTagCompound();
		}
		tNBT.removeTag("GT.ItemCharge");
		aCharge = Math.min(tStats[0] < 0 ? Math.abs(tStats[0] / 2) : aCharge, Math.abs(tStats[0]));
		if (aCharge > 0) {
			aStack.setItemDamage(this.getChargedMetaData(aStack));
			tNBT.setLong("GT.ItemCharge", aCharge);
		} else {
			aStack.setItemDamage(this.getEmptyMetaData(aStack));
		}
		if (tNBT.hasNoTags()) {
			aStack.setTagCompound(null);
		} else {
			aStack.setTagCompound(tNBT);
		}
		this.isItemStackUsable(aStack);
		return true;
	}

	@SuppressWarnings("static-method")
	public short getChargedMetaData(final ItemStack aStack) {
		return (short) aStack.getItemDamage();
	}

	@SuppressWarnings("static-method")
	public short getEmptyMetaData(final ItemStack aStack) {
		return (short) aStack.getItemDamage();
	}


	public boolean isItemStackUsable(final ItemStack aStack) {
		final ArrayList<IItemBehaviour<BaseEuItem>> tList = this.mItemBehaviors.get((short) this.getDamage(aStack));
		if (tList != null) {
			for (final IItemBehaviour<BaseEuItem> tBehavior : tList) {
				if (!tBehavior.isItemStackUsable(this, aStack)) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public final String getToolTip(final ItemStack aStack) {
		return null;
	} // This has its own ToolTip Handler, no need to let the IC2 Handler screw us up at this Point

	@Override
	public final IElectricItemManager getManager(final ItemStack aStack) {
		return this;
	} // We are our own Manager

	/**
	 * Sets the Furnace Burn Value for the Item.
	 *
	 * @param aMetaValue the Meta Value of the Item you want to set it to. [0 - 32765]
	 * @param aValue     200 = 1 Burn Process = 500 EU, max = 32767 (that is 81917.5 EU)
	 * @return the Item itself for convenience in constructing.
	 */
	public final BaseEuItem setBurnValue(final int aMetaValue, final int aValue) {
		if ((aMetaValue < 0) || (aValue < 0)) {
			return this;
		}
		if (aValue == 0) {
			this.mBurnValues.remove((short) aMetaValue);
		} else {
			this.mBurnValues.put((short) aMetaValue, aValue > Short.MAX_VALUE ? Short.MAX_VALUE : (short) aValue);
		}
		return this;
	}

	/**
	 * @param aMetaValue     the Meta Value of the Item you want to set it to. [0 - 32765]
	 * @param aMaxCharge     Maximum Charge. (if this is == 0 it will remove the Electric Behavior)
	 * @param aTransferLimit Transfer Limit.
	 * @param aTier          The electric Tier.
	 * @param aSpecialData   If this Item has a Fixed Charge, like a SingleUse Battery (if > 0).
	 *                       Use -1 if you want to make this Battery chargeable (the use and canUse Functions will still discharge if you just use this)
	 *                       Use -2 if you want to make this Battery dischargeable.
	 *                       Use -3 if you want to make this Battery charge/discharge-able.
	 * @return the Item itself for convenience in constructing.
	 */
	public final BaseEuItem setElectricStats(final int aMetaValue, final long aMaxCharge, final long aTransferLimit, final long aTier, final long aSpecialData, final boolean aUseAnimations) {
		if (aMetaValue < 0) {
			return this;
		}
		if (aMaxCharge == 0) {
			this.mElectricStats.remove((short) aMetaValue);
		} else {
			this.mElectricStats.put((short) aMetaValue, new Long[]{aMaxCharge, Math.max(0, aTransferLimit), Math.max(-1, aTier), aSpecialData});
		}
		return this;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(final Item var1, final CreativeTabs aCreativeTab, final List aList) {
		for (int i = 0, j = this.mEnabledItems.length(); i < j; i++) {
			if (this.mVisibleItems.get(i) || (D1 && this.mEnabledItems.get(i))) {
				final Long[] tStats = this.mElectricStats.get((short) (this.mOffset + i));
				if ((tStats != null) && (tStats[3] < 0)) {
					final ItemStack tStack = new ItemStack(this, 1, this.mOffset + i);
					this.setCharge(tStack, Math.abs(tStats[0]));
					this.isItemStackUsable(tStack);
					aList.add(tStack);
				}
				if ((tStats == null) || (tStats[3] != -2)) {
					final ItemStack tStack = new ItemStack(this, 1, this.mOffset + i);
					this.isItemStackUsable(tStack);
					aList.add(tStack);
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public final void registerIcons(final IIconRegister aIconRegister) {
		for (short i = 0, j = (short) this.mEnabledItems.length(); i < j; i++) {
			if (this.mEnabledItems.get(i)) {
				for (byte k = 1; k < this.mIconList[i].length; k++) {
					this.mIconList[i][k] = aIconRegister.registerIcon(CORE.MODID+":" + (this.getUnlocalizedName() + "/" + i + "/" + k));
				}
				this.mIconList[i][0] = aIconRegister.registerIcon(CORE.MODID+":" + (this.getUnlocalizedName() + "/" + i));
			}
		}
	}


	@Override
	public final IIcon getIconFromDamage(final int aMetaData) {
		if (aMetaData < 0) {
			return null;
		}
		return (aMetaData - this.mOffset) < this.mIconList.length ? this.mIconList[aMetaData - this.mOffset][0] : null;
	}

	/**
	 * Sets the unlocalized name of this item to the string passed as the parameter"
	 */
	@Override
	public Item setUnlocalizedName(final String p_77655_1_){
		this.unlocalizedName = p_77655_1_;
		super.setUnlocalizedName(p_77655_1_);
		return this;
	}

	/**
	 * Returns the unlocalized name of this item.
	 */
	@Override
	public String getUnlocalizedName()
	{
		return this.unlocalizedName;
	}

	public final Long[] getElectricStats(final ItemStack aStack) {
		return this.mElectricStats.get((short) aStack.getItemDamage());
	}

	@Override
	public int getItemEnchantability() {
		return 0;
	}

	@Override
	public boolean isBookEnchantable(final ItemStack aStack, final ItemStack aBook) {
		return false;
	}

	@Override
	public boolean getIsRepairable(final ItemStack aStack, final ItemStack aMaterial) {
		return false;
	}


	/**
	 * Adds a special Item Behaviour to the Item.
	 * <p/>
	 * Note: the boolean Behaviours sometimes won't be executed if another boolean Behaviour returned true before.
	 *
	 * @param aMetaValue the Meta Value of the Item you want to add it to. [0 - 32765]
	 * @param aBehavior  the Click Behavior you want to add.
	 * @return the Item itself for convenience in constructing.
	 */
	public final BaseEuItem addItemBehavior(final int aMetaValue, final IItemBehaviour<BaseEuItem> aBehavior) {
		if ((aMetaValue < 0) || (aMetaValue >= 32766) || (aBehavior == null)) {
			return this;
		}
		ArrayList<IItemBehaviour<BaseEuItem>> tList = this.mItemBehaviors.get((short) aMetaValue);
		if (tList == null) {
			tList = new ArrayList<>(1);
			this.mItemBehaviors.put((short) aMetaValue, tList);
		}
		tList.add(aBehavior);
		return this;
	}

	/**
	 * This adds a Custom Item to the ending Range.
	 *
	 * @param aID           The Id of the assigned Item [0 - mItemAmount] (The MetaData gets auto-shifted by +mOffset)
	 * @param aEnglish      The Default Localized Name of the created Item
	 * @param aToolTip      The Default ToolTip of the created Item, you can also insert null for having no ToolTip
	 * @param aFoodBehavior The Food Value of this Item. Can be null aswell. Just a convenience thing.
	 * @param aRandomData   The OreDict Names you want to give the Item. Also used for TC Aspects and some other things.
	 * @return An ItemStack containing the newly created Item.
	 */
	@SuppressWarnings("unchecked")
	public final ItemStack addItem(final int aID, final String aEnglish, String aToolTip, final Object... aRandomData) {
		if (aToolTip == null) {
			aToolTip = "";
		}
		if ((aID >= 0) && (aID < this.mItemAmount)) {
			final ItemStack rStack = new ItemStack(this, 1, this.mOffset + aID);
			this.mEnabledItems.set(aID);
			this.mVisibleItems.set(aID);
			GT_LanguageManager.addStringLocalization(this.getUnlocalizedName(rStack) + ".name", aEnglish);
			GT_LanguageManager.addStringLocalization(this.getUnlocalizedName(rStack) + ".tooltip", aToolTip);
			final List<TC_AspectStack> tAspects = new ArrayList<>();
			// Important Stuff to do first
			for (final Object tRandomData : aRandomData) {
				if (tRandomData instanceof SubTag) {
					if (tRandomData == SubTag.INVISIBLE) {
						this.mVisibleItems.set(aID, false);
						continue;
					}
					if (tRandomData == SubTag.NO_UNIFICATION) {
						GT_OreDictUnificator.addToBlacklist(rStack);
						continue;
					}
				}
			}
			// now check for the rest
			for (final Object tRandomData : aRandomData) {
				if (tRandomData != null) {
					boolean tUseOreDict = true;
					if (tRandomData instanceof IItemBehaviour) {
						this.addItemBehavior(this.mOffset + aID, (IItemBehaviour<BaseEuItem>) tRandomData);
						tUseOreDict = false;
					}
					if (tRandomData instanceof IItemContainer) {
						((IItemContainer) tRandomData).set(rStack);
						tUseOreDict = false;
					}
					if (tRandomData instanceof SubTag) {
						continue;
					}
					if (tRandomData instanceof TC_AspectStack) {
						((TC_AspectStack) tRandomData).addToAspectList(tAspects);
						continue;
					}
					if (tRandomData instanceof ItemData) {
						if (GT_Utility.isStringValid(tRandomData)) {
							GT_OreDictUnificator.registerOre(tRandomData, rStack);
						} else {
							GT_OreDictUnificator.addItemData(rStack, (ItemData) tRandomData);
						}
						continue;
					}
					if (tUseOreDict) {
						GT_OreDictUnificator.registerOre(tRandomData, rStack);
						continue;
					}
				}
			}
			if (GregTech_API.sThaumcraftCompat != null) {
				GregTech_API.sThaumcraftCompat.registerThaumcraftAspectsToItem(rStack, tAspects, false);
			}
			return rStack;
		}
		return null;
	}

	@Override
	public String getItemStackDisplayName(final ItemStack par1ItemStack) {
		return this.itemName.get(par1ItemStack.getItemDamage()-this.mOffset).getValue();
	}

}
