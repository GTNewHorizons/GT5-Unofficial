package gtPlusPlus.core.item.tool.misc;

import static gregtech.api.enums.GT_Values.V;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicTank;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fluids.IFluidHandler;
import net.minecraftforge.fluids.IFluidTank;

public class GregtechPump extends Item implements ISpecialElectricItem, IElectricItemManager, IFluidContainerItem {

	/**
	 * Right Click Functions
	 */

	@Override
	public boolean onItemUse(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int a4,
			float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		if (aStack == null || aPlayer == null || aWorld == null || aWorld.isRemote) {
			return false;
		}
		if (!aWorld.isRemote && tryDrainTile(aStack, aWorld, aPlayer, aX, aY, aZ)) {
			return true;
		} else {
			//return super.onItemUse(aStack, aPlayer, aWorld, aX, aY, aZ, a4, p_77648_8_, p_77648_9_, p_77648_10_);
			return false;
		}
	}

	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		return p_77659_1_;
	}

	/**
	 * GT Code
	 */

	/* ---------- CONSTRUCTOR AND MEMBER VARIABLES ---------- */
	private final HashMap<Short, ArrayList<IItemBehaviour<GregtechPump>>> mItemBehaviors = new HashMap<>();
	public final short mOffset, mItemAmount;
	public final BitSet mEnabledItems;
	public final BitSet mVisibleItems;
	/** The unlocalized name of this item. */
	private String unlocalizedName;

	private final HashMap<Integer, IIcon> mIconMap = new LinkedHashMap<Integer, IIcon>();	
	private final HashMap<Integer, EnumRarity> rarity = new LinkedHashMap<Integer, EnumRarity>();
	private final HashMap<Integer, EnumChatFormatting> descColour = new LinkedHashMap<Integer, EnumChatFormatting>();
	private final HashMap<Integer, String> itemName = new LinkedHashMap<Integer, String>();
	private final HashMap<Integer, String> itemDescription = new LinkedHashMap<Integer, String>();
	private final HashMap<Integer, Boolean> hasEffect = new LinkedHashMap<Integer, Boolean>();

	public final HashMap<Short, Long[]> mElectricStats = new LinkedHashMap<Short, Long[]>();
	public final HashMap<Short, Short> mBurnValues = new LinkedHashMap<Short, Short>();

	public void registerPumpType(final int aID, final String aPumpName, final int aEuMax, final int aTier) {
		ModItems.toolGregtechPump.registerItem(aID, // ID
				aPumpName, // Name
				aEuMax, // Eu Storage
				(short) aTier, // Tier
				"Can be used to remove fluids from GT machine input & output slots.", // Tooltip
				aTier <= 0 ? EnumRarity.common : aTier == 1 ? EnumRarity.uncommon : aTier == 2 ? EnumRarity.rare : aTier == 3 ? EnumRarity.epic : EnumRarity.common, // Rarity
				EnumChatFormatting.GRAY, // Desc colour
				false // Effect?
				);
	}

	public GregtechPump() {
		this("MU-metatool.01", AddToCreativeTab.tabTools, (short) 1000, (short) 31766);
	}

	public GregtechPump(final String unlocalizedName, final CreativeTabs creativeTab, final short aOffset,
			final short aItemAmount) {
		this.mEnabledItems = new BitSet(aItemAmount);
		this.mVisibleItems = new BitSet(aItemAmount);
		this.mOffset = (short) Math.min(32766, aOffset);
		this.mItemAmount = (short) Math.min(aItemAmount, 32766 - this.mOffset);
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setUnlocalizedName(unlocalizedName);
		this.setCreativeTab(creativeTab);
		this.setMaxStackSize(1);
		if (GameRegistry.findItem(CORE.MODID, unlocalizedName) == null) {
			GameRegistry.registerItem(this, unlocalizedName);
		}

	}

	public void registerItem(final int id, final String localizedName, final long euStorage, final int tier,
			final String description) {
		this.registerItem(id, localizedName, euStorage, (short) tier, description, EnumRarity.common,
				EnumChatFormatting.GRAY, false);
	}

	public void registerItem(final int id, final String localizedName, final long euStorage, final int tier,
			final String description, final int burnTime) {
		this.registerItem(id, localizedName, euStorage, (short) tier, description, EnumRarity.common,
				EnumChatFormatting.GRAY, false);
		this.setBurnValue(id, burnTime);
	}

	public void registerItem(final int id, final String localizedName, final long euStorage, final short tier,
			final String description, final EnumRarity regRarity, final EnumChatFormatting colour,
			final boolean Effect) {
		this.addItem(id, localizedName, EnumChatFormatting.YELLOW + "Electric", new Object[] {});
		if (euStorage > 0 && tier > 0)
			this.setElectricStats(this.mOffset + id, euStorage, GT_Values.V[tier], tier, -3L, true);
		this.rarity.put(id, regRarity);
		this.itemName.put(id, localizedName);
		this.itemDescription.put(id, description);
		this.descColour.put(id, colour);
		this.hasEffect.put(id, Effect);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(final ItemStack par1ItemStack) {
		int h = getCorrectMetaForItemstack(par1ItemStack);
		if (this.rarity.get(h) != null) {
			return this.rarity.get(h);
		}
		return EnumRarity.common;
	}

	@Override
	public boolean hasEffect(final ItemStack par1ItemStack) {
		int h = getCorrectMetaForItemstack(par1ItemStack);
		if (this.hasEffect.get(h) != null) {
			return this.hasEffect.get(h);
		}
		return false;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public void addInformation(final ItemStack aStack, final EntityPlayer aPlayer, List aList, final boolean aF3_H) {
		// aList.add("Meta: "+(aStack.getItemDamage()-mOffset));
		
		int aOffsetMeta = getCorrectMetaForItemstack(aStack);
		
		if ((this.descColour.get(aOffsetMeta) != null)
				&& (this.itemDescription.get(aOffsetMeta) != null)) {
			aList.add(this.descColour.get(aOffsetMeta)
					+ this.itemDescription.get(aOffsetMeta));
		}


		if (aOffsetMeta <= 3) {
			FluidStack f = getFluid(aStack);
			aList.add("Can also drain any other standard fluid container block");
			aList.add("Cannot be emptied via RMB, use inside a tank with GUI");
			aList.add(EnumChatFormatting.DARK_GRAY+"This is technically just a fancy fluid cell");
			aList.add(EnumChatFormatting.BLUE + (f != null ? f.getLocalizedName() : "No Fluids Contained"));
			aList.add(EnumChatFormatting.BLUE + (f != null ? ""+f.amount : ""+0) + "L" + " / " + getCapacity(aStack) + "L");
		}
		
		final Long[] tStats = this.getElectricStats(aStack);
		if (tStats != null) {
			if (tStats[3] > 0) {
				aList.add(EnumChatFormatting.AQUA + "Contains " + GT_Utility.formatNumbers(tStats[3]) + " EU   Tier: "
						+ (tStats[2] >= 0 ? tStats[2] : 0) + EnumChatFormatting.GRAY);
			} else {
				final long tCharge = this.getRealCharge(aStack);
				if ((tStats[3] == -2) && (tCharge <= 0)) {
					aList.add(EnumChatFormatting.AQUA + "Empty. You should recycle it properly."
							+ EnumChatFormatting.GRAY);
				} else {
					aList.add(EnumChatFormatting.AQUA + "" + GT_Utility.formatNumbers(tCharge) + " / "
							+ GT_Utility.formatNumbers(Math.abs(tStats[0])) + " EU - Voltage: "
							+ V[(int) (tStats[2] >= 0 ? tStats[2] < V.length ? tStats[2] : V.length - 1 : 1)]
									+ EnumChatFormatting.GRAY);
				}
			}
		}

		final ArrayList<IItemBehaviour<GregtechPump>> tList = this.mItemBehaviors.get((short) this.getDamage(aStack));
		if (tList != null) {
			for (final IItemBehaviour<GregtechPump> tBehavior : tList) {
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
	public final double charge(final ItemStack aStack, final double aCharge, final int aTier,
			final boolean aIgnoreTransferLimit, final boolean aSimulate) {
		final Long[] tStats = this.getElectricStats(aStack);
		if ((tStats == null) || (tStats[2] > aTier)
				|| !((tStats[3] == -1) || (tStats[3] == -3) || ((tStats[3] < 0) && (aCharge == Integer.MAX_VALUE)))
				|| (aStack.stackSize != 1)) {
			return 0;
		}
		final long tChargeBefore = this.getRealCharge(aStack), tNewCharge = aCharge == Integer.MAX_VALUE
				? Long.MAX_VALUE
						: Math.min(Math.abs(tStats[0]),
								tChargeBefore + (aIgnoreTransferLimit ? (long) aCharge : Math.min(tStats[1], (long) aCharge)));
		if (!aSimulate) {
			this.setCharge(aStack, tNewCharge);
		}
		return tNewCharge - tChargeBefore;
	}

	@Override
	public final double discharge(final ItemStack aStack, final double aCharge, final int aTier,
			final boolean aIgnoreTransferLimit, final boolean aBatteryAlike, final boolean aSimulate) {
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
		final long tChargeBefore = this.getRealCharge(aStack), tNewCharge = Math.max(0,
				tChargeBefore - (aIgnoreTransferLimit ? (long) aCharge : Math.min(tStats[1], (long) aCharge)));
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
					final double tCharge = ElectricItem.manager.discharge(tArmor,
							this.charge(aStack, Integer.MAX_VALUE - 1, Integer.MAX_VALUE, true, true),
							Integer.MAX_VALUE, true, true, false);
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

	public short getChargedMetaData(final ItemStack aStack) {
		return (short) aStack.getItemDamage();
	}

	public short getEmptyMetaData(final ItemStack aStack) {
		return (short) aStack.getItemDamage();
	}

	public boolean isItemStackUsable(final ItemStack aStack) {
		final ArrayList<IItemBehaviour<GregtechPump>> tList = this.mItemBehaviors.get((short) this.getDamage(aStack));
		if (tList != null) {
			for (final IItemBehaviour<GregtechPump> tBehavior : tList) {
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
	} // This has its own ToolTip Handler, no need to let the IC2 Handler screw us up
	// at this Point

	@Override
	public final IElectricItemManager getManager(final ItemStack aStack) {
		return this;
	} // We are our own Manager

	/**
	 * Sets the Furnace Burn Value for the Item.
	 *
	 * @param aMetaValue
	 *            the Meta Value of the Item you want to set it to. [0 - 32765]
	 * @param aValue
	 *            200 = 1 Burn Process = 500 EU, max = 32767 (that is 81917.5 EU)
	 * @return the Item itself for convenience in constructing.
	 */
	public final GregtechPump setBurnValue(final int aMetaValue, final int aValue) {
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
	 * @param aMetaValue
	 *            the Meta Value of the Item you want to set it to. [0 - 32765]
	 * @param aMaxCharge
	 *            Maximum Charge. (if this is == 0 it will remove the Electric
	 *            Behavior)
	 * @param aTransferLimit
	 *            Transfer Limit.
	 * @param aTier
	 *            The electric Tier.
	 * @param aSpecialData
	 *            If this Item has a Fixed Charge, like a SingleUse Battery (if >
	 *            0). Use -1 if you want to make this Battery chargeable (the use
	 *            and canUse Functions will still discharge if you just use this)
	 *            Use -2 if you want to make this Battery dischargeable. Use -3 if
	 *            you want to make this Battery charge/discharge-able.
	 * @return the Item itself for convenience in constructing.
	 */
	public final GregtechPump setElectricStats(final int aMetaValue, final long aMaxCharge, final long aTransferLimit,
			final long aTier, final long aSpecialData, final boolean aUseAnimations) {
		if (aMetaValue < 0) {
			return this;
		}
		if (aMaxCharge == 0) {
			this.mElectricStats.remove((short) aMetaValue);
		} else {
			this.mElectricStats.put((short) aMetaValue,
					new Long[] { aMaxCharge, Math.max(0, aTransferLimit), Math.max(-1, aTier), aSpecialData });
		}
		return this;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(final Item var1, final CreativeTabs aCreativeTab, final List aList) {
		for (int i = 0, j = this.mEnabledItems.length(); i < j; i++) {
			if (this.mVisibleItems.get(i) || (GT_Values.D1 && this.mEnabledItems.get(i))) {
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
				mIconMap.put((int) i, aIconRegister.registerIcon(CORE.MODID + ":" + (this.getUnlocalizedName() + "/" + i)));				
			}
		}
	}

	@Override
	public final IIcon getIconFromDamage(final int aMetaData) {
		if (aMetaData < 0) {
			return null;
		}		
		if (aMetaData < this.mOffset) {
			return mIconMap.get(0);
		}
		else {
			int newMeta = aMetaData - this.mOffset;
			newMeta = (Math.max(0, Math.min(3, newMeta)));
			return mIconMap.get(newMeta);
		}
	}

	/**
	 * Sets the unlocalized name of this item to the string passed as the parameter"
	 */
	@Override
	public Item setUnlocalizedName(final String p_77655_1_) {
		this.unlocalizedName = p_77655_1_;
		super.setUnlocalizedName(p_77655_1_);
		return this;
	}

	/**
	 * Returns the unlocalized name of this item.
	 */
	@Override
	public String getUnlocalizedName() {
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
	 * Note: the boolean Behaviours sometimes won't be executed if another boolean
	 * Behaviour returned true before.
	 *
	 * @param aMetaValue
	 *            the Meta Value of the Item you want to add it to. [0 - 32765]
	 * @param aBehavior
	 *            the Click Behavior you want to add.
	 * @return the Item itself for convenience in constructing.
	 */
	public final GregtechPump addItemBehavior(final int aMetaValue, final IItemBehaviour<GregtechPump> aBehavior) {
		if ((aMetaValue < 0) || (aMetaValue >= 32766) || (aBehavior == null)) {
			return this;
		}
		ArrayList<IItemBehaviour<GregtechPump>> tList = this.mItemBehaviors.get((short) aMetaValue);
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
	 * @param aID
	 *            The Id of the assigned Item [0 - mItemAmount] (The MetaData gets
	 *            auto-shifted by +mOffset)
	 * @param aEnglish
	 *            The Default Localized Name of the created Item
	 * @param aToolTip
	 *            The Default ToolTip of the created Item, you can also insert null
	 *            for having no ToolTip
	 * @param aFoodBehavior
	 *            The Food Value of this Item. Can be null aswell. Just a
	 *            convenience thing.
	 * @param aRandomData
	 *            The OreDict Names you want to give the Item. Also used for TC
	 *            Aspects and some other things.
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
						this.addItemBehavior(this.mOffset + aID, (IItemBehaviour<GregtechPump>) tRandomData);
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
	public String getItemStackDisplayName(final ItemStack aStack) {
		int keyValue = (getCorrectMetaForItemstack(aStack));		
		if (keyValue < 0 || keyValue > 3) {
			keyValue = 0;
		}		
		return this.itemName.get(keyValue);
	}

	/**
	 * Fluid Handling
	 */

	/*
	 * IFluidContainer Functions
	 */

	public void emptyStoredFluid(ItemStack aStack) {		
		if (aStack.hasTagCompound()) {
			NBTTagCompound t = aStack.getTagCompound();
			if (t.hasKey("mInit")) {
				t.removeTag("mInit");
			}
			if (t.hasKey("mFluid")) {
				t.removeTag("mFluid");
			}
			if (t.hasKey("mFluidAmount")) {
				t.removeTag("mFluidAmount");
			}
		}		
	}

	public void storeFluid(ItemStack aStack, FluidStack aFluid) {
		if (aFluid == null) {
			return;
		} else {
			String fluidname = aFluid.getFluid().getName();
			int amount = aFluid.amount;
			if (fluidname != null && fluidname.length() > 0 && amount > 0) {
				NBTUtils.setString(aStack, "mFluid", fluidname);
				NBTUtils.setInteger(aStack, "mFluidAmount", amount);
			}
		}
	}

	@Override
	public FluidStack getFluid(ItemStack container) {
		if (!container.hasTagCompound() || !container.getTagCompound().hasKey("mInit")) {
			initNBT(container);
		}
		if (container.getTagCompound().hasKey("mInit") && container.getTagCompound().getBoolean("mInit")) {
			String fluidname;
			Integer amount = 0;
			fluidname = NBTUtils.getString(container, "mFluid");
			amount = NBTUtils.getInteger(container, "mFluidAmount");
			if (fluidname != null && amount != null && amount > 0) {
				return FluidUtils.getFluidStack(fluidname, amount);
			} else {
				return null;
			}
		}
		return null;
	}

	@Override
	public int getCapacity(ItemStack container) {
		if (!container.hasTagCompound() || !container.getTagCompound().hasKey("mInit")) {
			initNBT(container);
		}
		if (container.getTagCompound().hasKey("mInit") && container.getTagCompound().getBoolean("mInit")) {
			return container.getTagCompound().getInteger("mCapacity");
		}
		int aMeta = this.getCorrectMetaForItemstack(container);
		int aCapacity = (aMeta == 0 ? 2000 : (aMeta == 1 ? 8000 : (aMeta == 2 ? 32000 : 128000)));
		return aCapacity;
	}

	public int fill(ItemStack container, FluidStack resource) {
		return fill(container, resource, true);
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) {
		if (!doFill || resource == null) {
			return 0;
		}

		if (!container.hasTagCompound() || !container.getTagCompound().hasKey("mInit")) {
			initNBT(container);
		}
		if (container.getTagCompound().hasKey("mInit") && container.getTagCompound().getBoolean("mInit")) {
			String aStored;
			int aStoredAmount = 0;
			int aCapacity = getCapacity(container);
			FluidStack aStoredFluid = getFluid(container);
			if (aStoredFluid != null) {
				aStored = aStoredFluid.getFluid().getName();
				aStoredAmount = aStoredFluid.amount;
				if (aStoredAmount == aCapacity) {
					return 0;
				}
			}
			// Handle no stored fluid first
			if (aStoredFluid == null) {
				Logger.WARNING("Pump is empty, filling with tank fluids.");
				FluidStack toConsume;
				int amountToConsume = 0;
				if (resource.amount >= aCapacity) {
					amountToConsume = aCapacity;
				} else {
					amountToConsume = resource.amount;
				}
				toConsume = FluidUtils.getFluidStack(resource, amountToConsume);
				if (toConsume != null && amountToConsume > 0) {
					storeFluid(container, toConsume);
					return amountToConsume;
				}
			} else {
				Logger.WARNING("Pump is Partially full, filling with tank fluids.");
				if (aStoredFluid.isFluidEqual(resource)) {
					Logger.WARNING("Found matching fluids.");
					int aSpaceLeft = (aCapacity - aStoredAmount);
					Logger.WARNING(
							"Capacity: " + aCapacity + " | Stored: " + aStoredAmount + " | Space left: " + aSpaceLeft);
					FluidStack toConsume;
					int amountToConsume = 0;
					if (resource.amount >= aSpaceLeft) {
						amountToConsume = aSpaceLeft;
						Logger.WARNING("More or equal fluid amount to pump container space.");
					} else {
						amountToConsume = resource.amount;
						Logger.WARNING("Less fluid than container space");
					}
					Logger.WARNING("Amount to consume: " + amountToConsume);
					toConsume = FluidUtils.getFluidStack(resource, (aStoredAmount + amountToConsume));
					if (toConsume != null && amountToConsume > 0) {
						Logger.WARNING("Storing Fluid");
						storeFluid(container, toConsume);
						return amountToConsume;
					} else {
						Logger.WARNING("Not storing fluid");
					}
				} else {
					Logger.WARNING("Fluids did not match.");
				}
			}
		}
		return 0;
	}

	public FluidStack drain(ItemStack container, int drainAmt) {
		return drain(container, drainAmt, true);
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
		if (!doDrain || maxDrain == 0) {
			return null;
		}
		if (!container.hasTagCompound() || !container.getTagCompound().hasKey("mInit")) {
			initNBT(container);
		}
		if (container.getTagCompound().hasKey("mInit") && container.getTagCompound().getBoolean("mInit")) {

			String aStored;
			int aStoredAmount = 0;
			FluidStack aStoredFluid = getFluid(container);

			if (aStoredFluid != null) {
				aStored = aStoredFluid.getFluid().getName();
				aStoredAmount = aStoredFluid.amount;
			}
			// We cannot drain this if it's empty.
			else if (aStoredFluid == null) {
				return null;
			}

			if (maxDrain >= aStoredAmount) {
				emptyStoredFluid(container);
				return aStoredFluid;
			} else {
				// Handle Partial removal
				int amountRemaining = (aStoredAmount - maxDrain);
				if (amountRemaining == 0) {
					emptyStoredFluid(container);
				} else {
					FluidStack newAmount = FluidUtils.getFluidStack(aStoredFluid, amountRemaining);
					FluidStack drained = FluidUtils.getFluidStack(aStoredFluid, maxDrain);
					if (newAmount != null && drained != null) {
						storeFluid(container, newAmount);
						return drained;
					}
				}
			}
		}
		return null;
	}

	/*
	 * Handle ItemStack NBT
	 */

	public void initNBT(ItemStack aStack) {
		NBTTagCompound aNewNBT;
		if (!aStack.hasTagCompound()) {
			aNewNBT = new NBTTagCompound();
		} else {
			aNewNBT = aStack.getTagCompound();
		}

		if (!aNewNBT.hasKey("mInit")) {
			int aMeta = this.getCorrectMetaForItemstack(aStack);
			aNewNBT.setInteger("mMeta", aMeta);
			aNewNBT.setBoolean("mInit", true);
			aNewNBT.setString("mFluid", "@@@@@");
			aNewNBT.setInteger("mFluidAmount", 0);
			int aCapacity = (aMeta == 0 ? 2000 : (aMeta == 1 ? 8000 : (aMeta == 2 ? 32000 : 128000)));
			aNewNBT.setInteger("mCapacity", aCapacity);
			aStack.setTagCompound(aNewNBT);
		}
	}

	/**
	 * Tile Handling
	 */

	/*
	 * Custom Fluid Handling for Tiles and GT Tiles.
	 */

	public boolean tryDrainTile(ItemStack aStack, World aWorld, EntityPlayer aPlayer, int aX, int aY, int aZ) {
		try {
			if (aWorld.isRemote || aStack == null) {
				return false;
			} else {
				int aTier = (aStack.getItemDamage() - 1000);
				int removal;
				if (aTier == 0) {
					removal = 0;
				} else if (aTier == 1) {
					removal = 32;
				} else if (aTier == 2) {
					removal = 128;
				} else if (aTier == 3) {
					removal = 512;
				} else {
					removal = 8;
				}
				if (!canUse(aStack, removal) && aTier > 0) {
					PlayerUtils.messagePlayer(aPlayer, "Not enough power.");
					Logger.WARNING("No Power");
					return false;
				}

				final Block aBlock = aWorld.getBlock(aX, aY, aZ);
				if (aBlock == null) {
					return false;
				}
				TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
				if (tTileEntity == null) {
					return false;
				} else {
					double aCharge = this.getCharge(aStack);
					boolean didDrain = false;
					if (aTier > 0 && aCharge > 0) {
						if (discharge(aStack, removal, aTier, true, true, false) > 0) {
							didDrain = true;
						}
					} else if (aTier == 0) {
						didDrain = true;
					} else {
						didDrain = false;
					}

					if (didDrain) {
						if ((tTileEntity instanceof IGregTechTileEntity)) {
							return this.drainTankGT(tTileEntity, aStack, aWorld, aPlayer, aX, aY, aZ);
						} 
						//Try support Standard Fluid Tanks too (May disable if dupes appear again)
						else if ((tTileEntity instanceof IFluidTank || tTileEntity instanceof IFluidHandler)) {
							return this.drainIFluidTank(tTileEntity, aStack, aWorld, aPlayer, aX, aY, aZ);
						}
					}
				}
			}
		} catch (Throwable t) {
		}
		return false;
	}

	/*
	 * Vanilla IFluidTank
	 */

	public boolean drainIFluidTank(TileEntity tTileEntity, ItemStack aStack, World aWorld, EntityPlayer aPlayer, int aX,
			int aY, int aZ) {
		if (tTileEntity == null) {
			Logger.WARNING("Invalid Tile, somehow.");
			return false;
		}
		if ((tTileEntity instanceof IFluidTank || tTileEntity instanceof IFluidHandler)) {
			if (this.getFluid(aStack) == null || (this.getFluid(aStack) != null && this.getFluid(aStack).amount < this.getCapacity(aStack))) {
				Logger.WARNING("Trying to find Stored Fluid - Behaviour Class.");
				FluidStack aStored = getStoredFluidOfVanillaTank(tTileEntity);
				if (aStored != null) {
					int mAmountInserted = fill(aStack, aStored);
					FluidStack newStackRemainingInTank;
					if (mAmountInserted > 0) {
						if (mAmountInserted == aStored.amount) {
							newStackRemainingInTank = null;
						} else {
							newStackRemainingInTank = FluidUtils.getFluidStack(aStored, (aStored.amount - mAmountInserted));
						}
						boolean b = setStoredFluidOfVanillaTank(tTileEntity, newStackRemainingInTank);
						Logger.WARNING("Cleared Tank? " + b + " | mAmountInserted: " + mAmountInserted);
						Logger.WARNING("Returning " + b + " - drainTankVanilla.");						
						if (b) {
							PlayerUtils.messagePlayer(aPlayer, "Drained "+mAmountInserted+"L of "+aStored.getLocalizedName()+".");
						}						
						return b;
					}
				} else {
					Logger.WARNING("Found no valid Fluidstack - drainTankVanilla.");
				}
			}
			else {
				Logger.WARNING("Pump is full.");
			}
		}
		Logger.WARNING("Could not drain vanilla tank.");
		return false;
	}

	/*
	 * GT Tanks
	 */

	public boolean drainTankGT(TileEntity tTileEntity, ItemStack aStack, World aWorld, EntityPlayer aPlayer, int aX,
			int aY, int aZ) {
		if (tTileEntity == null) {
			return false;
		}
		if ((tTileEntity instanceof IGregTechTileEntity)) {
			Logger.WARNING("Right Clicking on GT Tile - drainTankGT.");
			if (((IGregTechTileEntity) tTileEntity).getTimer() < 50L) {
				Logger.WARNING("Returning False - Behaviour Class. Timer < 50");
				return false;
			} else if ((!aWorld.isRemote) && (!((IGregTechTileEntity) tTileEntity).isUseableByPlayer(aPlayer))) {
				Logger.WARNING("Returning True - drainTankGT. NotUsable()");
				return true;
			} else {
				if (this.getFluid(aStack) == null || (this.getFluid(aStack) != null && this.getFluid(aStack).amount < this.getCapacity(aStack))) {
					Logger.WARNING("Trying to find Stored Fluid - drainTankGT.");
					FluidStack aStored = getStoredFluidOfGTMachine((IGregTechTileEntity) tTileEntity);
					if (aStored != null) {
						int mAmountInserted = fill(aStack, aStored);
						FluidStack newStackRemainingInTank;
						if (mAmountInserted > 0) {
							if (mAmountInserted == aStored.amount) {
								newStackRemainingInTank = null;
							} else {
								newStackRemainingInTank = FluidUtils.getFluidStack(aStored, (aStored.amount - mAmountInserted));
							}
							boolean b = setStoredFluidOfGTMachine((IGregTechTileEntity) tTileEntity, newStackRemainingInTank);
							Logger.WARNING("Cleared Tank? " + b + " | mAmountInserted: " + mAmountInserted);
							Logger.WARNING("Returning " + b + " - drainTankGT.");				
							if (b) {
								PlayerUtils.messagePlayer(aPlayer, "Drained "+mAmountInserted+"L of "+aStored.getLocalizedName()+".");
							}			
							return b;
						}
					} else {
						Logger.WARNING("Found no valid Fluidstack - drainTankGT.");
					}
				}
				else {
					Logger.WARNING("Pump is full.");
				}
			}
		}
		Logger.WARNING("Could not drain GT tank.");
		return false;
	}

	/*
	 * Vanilla Tanks
	 */

	public FluidStack getStoredFluidOfVanillaTank(TileEntity aTileEntity) {
		if (aTileEntity == null) {
			return null;
		} else if ((aTileEntity instanceof IFluidTank || aTileEntity instanceof IFluidHandler)) {
			if (aTileEntity instanceof IFluidTank) {
				return getStoredFluidOfVanillaTank((IFluidTank) aTileEntity);
			} else {
				return getStoredFluidOfVanillaTank((IFluidHandler) aTileEntity);
			}
		} else {
			return null;
		}
	}

	public FluidStack getStoredFluidOfVanillaTank(IFluidTank aTileEntity) {
		FluidStack f = aTileEntity.getFluid();
		Logger.WARNING("Returning Fluid stack from tile. Found: "
				+ (f != null ? f.getLocalizedName() + " - " + f.amount + "L" : "Nothing"));
		return f;
	}

	public FluidStack getStoredFluidOfVanillaTank(IFluidHandler aTileEntity) {
		if (aTileEntity instanceof IFluidTank) {
			return getStoredFluidOfVanillaTank((IFluidTank) aTileEntity);
		}
		FluidStack f;
		AutoMap<FluidTankInfo[]> m = new AutoMap<FluidTankInfo[]>();
		for (int i = 0; i < 6; i++) {
			m.put(aTileEntity.getTankInfo(ForgeDirection.getOrientation(i)));
		}
		if (m.get(0) != null && m.get(0)[0] != null && m.get(0)[0].fluid != null) {
			return m.get(0)[0].fluid;
		} else {
			return null;
		}
	}

	public boolean setStoredFluidOfVanillaTank(TileEntity aTileEntity, FluidStack aSetFluid) {
		Logger.WARNING("Trying to clear Tile's tank. - Behaviour Class. [1]");

		if (aTileEntity == null) {
			return false;
		}
		else if ((aTileEntity instanceof IFluidTank || aTileEntity instanceof IFluidHandler)) {
			if (aTileEntity instanceof IFluidTank) {
				Logger.WARNING("Tile Was instanceof IFluidTank.");
				FluidStack f = ((IFluidTank) aTileEntity).getFluid();
				if (aSetFluid == null) {
					aSetFluid = f;
					aSetFluid.amount = f.amount;
				}
				int toDrain = (f.amount - aSetFluid.amount);
				FluidStack newStack;
				if (toDrain <= 0) {
					newStack = f;
				} else {
					newStack = ((IFluidTank) aTileEntity).drain(toDrain, true);
				}

				if (newStack.isFluidEqual(aSetFluid) && newStack.amount == aSetFluid.amount) {
					Logger.WARNING("Removed fluid from vanilla IFluidTank successfully.");
					return true;
				} else {
					Logger.WARNING("Failed trying to remove fluid from vanilla IFluidTank.");
					return false;
				}
			}
			else {			
				
				//Rewrite Fluid handling for Vanilla type tanks
				if (!IFluidHandler.class.isInstance(aTileEntity)) {
					Logger.WARNING("Tile Was not an instance of IFluidHandler.");
					return false;
				}
				
								
				IFluidHandler aTank = (IFluidHandler) aTileEntity;				
				FluidStack aTankContents = null;
				FluidTankInfo[] a1 = aTank.getTankInfo(ForgeDirection.UNKNOWN);
				if (a1 != null) {
					if (a1[0] != null) {
						aTankContents = a1[0].fluid;
						Logger.WARNING("Found Fluid in Tank. "+aTankContents.getLocalizedName()+" - "+aTankContents.amount);
					}
				}
				if (aSetFluid == null) {
					Logger.WARNING("Setting fluid to tank contents, as we're going to empty it totally.");			
					aSetFluid = aTankContents.copy();
				}
				else {
					Logger.WARNING("Setting fluid to tank contents, as we're going to empty it totally.");							
				}
				Logger.WARNING("Tile Was instance of IFluidHandler. Trying to Drain "+aSetFluid.getLocalizedName()+" - "+aSetFluid.amount);
				
				if (a1 == null || aTankContents == null) {
					Logger.WARNING("Tank is empty.");	
					return false;
				}
				//Found some Fluid in the tank
				else {
					FluidStack aDrainedStack = aTank.drain(ForgeDirection.UNKNOWN, aSetFluid, true);					
					if (aDrainedStack.isFluidStackIdentical(aSetFluid)) {
						Logger.WARNING("Drained!");	
						return true;
					}
					else {
						Logger.WARNING("Partially Drained! This is probably an error.");	
						return true;
					}					
				}
			}
		} else {
			Logger.WARNING("Bad Tank Tile to drain.");
			return false;
		}

	}

	/*
	 * GT Tanks
	 */

	public FluidStack getStoredFluidOfGTMachine(IGregTechTileEntity aTileEntity) {
		if (aTileEntity == null) {
			return null;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		;
		if (aMetaTileEntity == null) {
			return null;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_BasicTank) {
			Logger.WARNING("Tile Was Instanceof BasicTank.");
			return getStoredFluidOfGTMachine((GT_MetaTileEntity_BasicTank) aMetaTileEntity);
		} else {
			return null;
		}
	}

	public FluidStack getStoredFluidOfGTMachine(GT_MetaTileEntity_BasicTank aTileEntity) {
		FluidStack f = aTileEntity.mFluid;
		
		//Let's see if this machine has output fluid too
		if (f == null) {
			Logger.WARNING("Could not find any input fluid, checking output if possible.");
			if (aTileEntity instanceof GT_MetaTileEntity_BasicMachine) {
				GT_MetaTileEntity_BasicMachine g = (GT_MetaTileEntity_BasicMachine) aTileEntity;
				Logger.WARNING("Tile is a Basic Machine of some sort - "+g.mNEIName);
				if (g != null) {
					f = g.mOutputFluid;
					if (f != null) {
						Logger.WARNING("Found output fluid! "+f.getLocalizedName());
					}
					else {
						Logger.WARNING("Did not find anything!");
						f = g.getFluid();
						if (f != null) {
							Logger.WARNING("Found fluid! "+f.getLocalizedName());
						}
						else {
							Logger.WARNING("Did not find anything!");
							f = g.getFluid();
						}
					}					
				}
			}
		}
		
		Logger.WARNING("Returning Fluid stack from tile. Found: "
				+ (f != null ? f.getLocalizedName() + " - " + f.amount + "L" : "Nothing"));
		return f;
	}

	public boolean setStoredFluidOfGTMachine(IGregTechTileEntity aTileEntity, FluidStack aSetFluid) {
		Logger.WARNING("Trying to clear Tile's tank. - Behaviour Class. [1]");
		if (aTileEntity == null) {
			return false;
		}
		final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
		if (aMetaTileEntity == null) {
			return false;
		}
		if (aMetaTileEntity instanceof GT_MetaTileEntity_BasicTank) {
			Logger.WARNING("Trying to clear Tile's tank. - Behaviour Class. [2]");
			return setStoredFluidOfGTMachine((GT_MetaTileEntity_BasicTank) aMetaTileEntity, aSetFluid);
		} else {
			return false;
		}
	}

	public boolean setStoredFluidOfGTMachine(GT_MetaTileEntity_BasicTank aTileEntity, FluidStack aSetFluid) {
		try {
			
			//Try Handle Outputs First			
			if (aTileEntity.setDrainableStack(aSetFluid) != null) {
				return true;
			}			
			
			aTileEntity.mFluid = aSetFluid;
			boolean b = aTileEntity.mFluid == aSetFluid;
			Logger.WARNING("Trying to set Tile's tank. - Behaviour Class. [3] " + b);
			return b;
		} catch (Throwable t) {
			Logger.WARNING("Trying to clear Tile's tank. FAILED - Behaviour Class. [x]");
			return false;
		}
	}
	
	
	public int getCorrectMetaForItemstack(ItemStack aStack) {
		if (aStack == null) {
			return 0;
		}
		else {
			if (aStack.getItemDamage() < this.mOffset) {
				return 0;
			}
			else {
				int newMeta = aStack.getItemDamage() - this.mOffset;
				newMeta = (Math.max(0, Math.min(3, newMeta)));
				return newMeta;
			}
		}
		
		
		
		
	}

}
