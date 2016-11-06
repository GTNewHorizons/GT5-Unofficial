package gtPlusPlus.core.item.base;

import static gregtech.api.enums.GT_Values.D1;
import static gregtech.api.enums.GT_Values.V;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.enums.TC_Aspects.TC_AspectStack;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.objects.ItemData;
import gregtech.api.util.*;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.array.Pair;
import ic2.api.item.*;

import java.util.*;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BaseEuItem extends Item implements ISpecialElectricItem, IElectricItemManager {

	/* ---------- CONSTRUCTOR AND MEMBER VARIABLES ---------- */
	private final HashMap<Short, ArrayList<IItemBehaviour<BaseEuItem>>> mItemBehaviors = new HashMap<Short, ArrayList<IItemBehaviour<BaseEuItem>>>();
	public final short mOffset, mItemAmount;
	public final BitSet mEnabledItems;
	public final BitSet mVisibleItems;
	public final IIcon[][] mIconList;
	 /** The unlocalized name of this item. */
    private String unlocalizedName;
	
	private ArrayList<Pair<Integer, EnumRarity>> rarity = new ArrayList<Pair<Integer, EnumRarity>>();
	private ArrayList<Pair<Integer, EnumChatFormatting>> descColour = new ArrayList<Pair<Integer, EnumChatFormatting>>();
	private ArrayList<Pair<Integer, String>> itemName = new ArrayList<Pair<Integer, String>>();
	private ArrayList<Pair<Integer, String>> itemDescription = new ArrayList<Pair<Integer, String>>();
	private ArrayList<Pair<Integer, Boolean>> hasEffect = new ArrayList<Pair<Integer, Boolean>>();

	public final HashMap<Short, Long[]> mElectricStats = new HashMap<Short, Long[]>();
	public final HashMap<Short, Short> mBurnValues = new HashMap<Short, Short>();

	public BaseEuItem() {
		this("MU-metaitem.02", AddToCreativeTab.tabOther, (short) 1000, (short) 31766);		
	}	
		
	public BaseEuItem(String unlocalizedName, CreativeTabs creativeTab, short aOffset, short aItemAmount) {
		mEnabledItems = new BitSet(aItemAmount);
		mVisibleItems = new BitSet(aItemAmount);
		mOffset = (short) Math.min(32766, aOffset);
		mItemAmount = (short) Math.min(aItemAmount, 32766 - mOffset);
		mIconList = new IIcon[aItemAmount][1];
		setHasSubtypes(true);
		setMaxDamage(0);
    	setUnlocalizedName(unlocalizedName);
		setCreativeTab(creativeTab);
		setMaxStackSize(1);		
		GameRegistry.registerItem(this, unlocalizedName);
	}	
	
	
	public void registerItem(int id, String localizedName, long euStorage, int tier, String description) {
		registerItem(id, localizedName, euStorage, (short) tier, description, EnumRarity.common, EnumChatFormatting.GRAY, false);
	}
	
	public void registerItem(int id, String localizedName, long euStorage, int tier, String description, int burnTime) {
		registerItem(id, localizedName, euStorage, (short) tier, description, EnumRarity.common, EnumChatFormatting.GRAY, false);
		setBurnValue(id, burnTime);
	}
	
	
	public void registerItem(int id, String localizedName, long euStorage, short tier, String description, EnumRarity regRarity, EnumChatFormatting colour, boolean Effect) {
		addItem(id, localizedName, EnumChatFormatting.YELLOW+"Electric", new Object[]{});
        setElectricStats(mOffset + id, euStorage, GT_Values.V[tier], tier, -3L, true);
		this.rarity.add(new Pair<Integer, EnumRarity>(id, regRarity));
		this.itemName.add(new Pair<Integer, String>(id, localizedName));
		this.itemDescription.add(new Pair<Integer, String>(id, description));
		this.descColour.add(new Pair<Integer, EnumChatFormatting>(id, colour));
		this.hasEffect.add(new Pair<Integer, Boolean>(id, Effect));
	}	

	@Override
	@SideOnly(Side.CLIENT)
	public EnumRarity getRarity(ItemStack par1ItemStack){
		if (rarity.get(par1ItemStack.getItemDamage()-mOffset) != null)
		return rarity.get(par1ItemStack.getItemDamage()-mOffset).getValue();
		return EnumRarity.common;
	}

	@Override
	public boolean hasEffect(ItemStack par1ItemStack){
		if (hasEffect.get(par1ItemStack.getItemDamage()-mOffset) != null)
		return hasEffect.get(par1ItemStack.getItemDamage()-mOffset).getValue();
		return false;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
    public final void addInformation(ItemStack aStack, EntityPlayer aPlayer, List aList, boolean aF3_H) {
		//aList.add("Meta: "+(aStack.getItemDamage()-mOffset));
		if (descColour.get(aStack.getItemDamage()-mOffset) != null && itemDescription.get(aStack.getItemDamage()-mOffset) != null)			
		aList.add(descColour.get(aStack.getItemDamage()-mOffset).getValue()+itemDescription.get(aStack.getItemDamage()-mOffset).getValue());
        String tKey = getUnlocalizedName(aStack) + ".tooltip", tString = GT_LanguageManager.getTranslation(tKey);
        if (GT_Utility.isStringValid(tString) && !tKey.equals(tString)) aList.add(tString);
        Long[] tStats = getElectricStats(aStack);
        if (tStats != null) {
            if (tStats[3] > 0) {
                aList.add(EnumChatFormatting.AQUA + "Contains " + GT_Utility.formatNumbers(tStats[3]) + " EU   Tier: " + (tStats[2] >= 0 ? tStats[2] : 0) + EnumChatFormatting.GRAY);
            } else {
                long tCharge = getRealCharge(aStack);
                if (tStats[3] == -2 && tCharge <= 0) {
                    aList.add(EnumChatFormatting.AQUA + "Empty. You should recycle it properly." + EnumChatFormatting.GRAY);
                } else {
                    aList.add(EnumChatFormatting.AQUA + "" + GT_Utility.formatNumbers(tCharge) + " / " + GT_Utility.formatNumbers(Math.abs(tStats[0])) + " EU - Voltage: " + V[(int) (tStats[2] >= 0 ? tStats[2] < V.length ? tStats[2] : V.length - 1 : 1)] + EnumChatFormatting.GRAY);
                }
            }
        }        
        ArrayList<IItemBehaviour<BaseEuItem>> tList = mItemBehaviors.get((short) getDamage(aStack));
        if (tList != null) for (IItemBehaviour<BaseEuItem> tBehavior : tList)
            aList = tBehavior.getAdditionalToolTips(this, aList, aStack);        
	}
	
	
	@Override
	public final Item getChargedItem(ItemStack itemStack) {
		return this;
	}

	@Override
	public final Item getEmptyItem(ItemStack itemStack) {
		return this;
	}

	@Override
	public final double getMaxCharge(ItemStack aStack) {
		Long[] tStats = getElectricStats(aStack);
		if (tStats == null) return 0;
		return Math.abs(tStats[0]);
	}

	@Override
	public final double getTransferLimit(ItemStack aStack) {
		Long[] tStats = getElectricStats(aStack);
		if (tStats == null) return 0;
		return Math.max(tStats[1], tStats[3]);
	}

	@Override
	public final int getTier(ItemStack aStack) {
		Long[] tStats = getElectricStats(aStack);
		return (int) (tStats == null ? Integer.MAX_VALUE : tStats[2]);
	}

	@Override
	public final double charge(ItemStack aStack, double aCharge, int aTier, boolean aIgnoreTransferLimit, boolean aSimulate) {
		Long[] tStats = getElectricStats(aStack);
		if (tStats == null || tStats[2] > aTier || !(tStats[3] == -1 || tStats[3] == -3 || (tStats[3] < 0 && aCharge == Integer.MAX_VALUE)) || aStack.stackSize != 1)
			return 0;
		long tChargeBefore = getRealCharge(aStack), tNewCharge = aCharge == Integer.MAX_VALUE ? Long.MAX_VALUE : Math.min(Math.abs(tStats[0]), tChargeBefore + (aIgnoreTransferLimit ? (long) aCharge : Math.min(tStats[1], (long) aCharge)));
		if (!aSimulate) setCharge(aStack, tNewCharge);
		return tNewCharge - tChargeBefore;
	}

	@Override
	public final double discharge(ItemStack aStack, double aCharge, int aTier, boolean aIgnoreTransferLimit, boolean aBatteryAlike, boolean aSimulate) {
		Long[] tStats = getElectricStats(aStack);
		if (tStats == null || tStats[2] > aTier) return 0;
		if (aBatteryAlike && !canProvideEnergy(aStack)) return 0;
		if (tStats[3] > 0) {
			if (aCharge < tStats[3] || aStack.stackSize < 1) return 0;
			if (!aSimulate) aStack.stackSize--;
			return tStats[3];
		}
		long tChargeBefore = getRealCharge(aStack), tNewCharge = Math.max(0, tChargeBefore - (aIgnoreTransferLimit ? (long) aCharge : Math.min(tStats[1], (long) aCharge)));
		if (!aSimulate) setCharge(aStack, tNewCharge);
		return tChargeBefore - tNewCharge;
	}

	@Override
	public final double getCharge(ItemStack aStack) {
		return getRealCharge(aStack);
	}

	@Override
	public final boolean canUse(ItemStack aStack, double aAmount) {
		return getRealCharge(aStack) >= aAmount;
	}

	@Override
	public final boolean use(ItemStack aStack, double aAmount, EntityLivingBase aPlayer) {
		chargeFromArmor(aStack, aPlayer);
		if (aPlayer instanceof EntityPlayer && ((EntityPlayer) aPlayer).capabilities.isCreativeMode) return true;
		double tTransfer = discharge(aStack, aAmount, Integer.MAX_VALUE, true, false, true);
		if (tTransfer == aAmount) {
			discharge(aStack, aAmount, Integer.MAX_VALUE, true, false, false);
			chargeFromArmor(aStack, aPlayer);
			return true;
		}
		discharge(aStack, aAmount, Integer.MAX_VALUE, true, false, false);
		chargeFromArmor(aStack, aPlayer);
		return false;
	}

	@Override
	public final boolean canProvideEnergy(ItemStack aStack) {
		Long[] tStats = getElectricStats(aStack);
		if (tStats == null) return false;
		return tStats[3] > 0 || (aStack.stackSize == 1 && (tStats[3] == -2 || tStats[3] == -3));
	}

	@Override
	public final void chargeFromArmor(ItemStack aStack, EntityLivingBase aPlayer) {
		if (aPlayer == null || aPlayer.worldObj.isRemote) return;
		for (int i = 1; i < 5; i++) {
			ItemStack tArmor = aPlayer.getEquipmentInSlot(i);
			if (GT_ModHandler.isElectricItem(tArmor)) {
				IElectricItem tArmorItem = (IElectricItem) tArmor.getItem();
				if (tArmorItem.canProvideEnergy(tArmor) && tArmorItem.getTier(tArmor) >= getTier(aStack)) {
					double tCharge = ElectricItem.manager.discharge(tArmor, charge(aStack, Integer.MAX_VALUE - 1, Integer.MAX_VALUE, true, true), Integer.MAX_VALUE, true, true, false);
					if (tCharge > 0) {
						charge(aStack, tCharge, Integer.MAX_VALUE, true, false);
						if (aPlayer instanceof EntityPlayer) {
							Container tContainer = ((EntityPlayer) aPlayer).openContainer;
							if (tContainer != null) tContainer.detectAndSendChanges();
						}
					}
				}
			}
		}
	}	

	public final long getRealCharge(ItemStack aStack) {
		Long[] tStats = getElectricStats(aStack);
		if (tStats == null) return 0;
		if (tStats[3] > 0) return (int) (long) tStats[3];
		NBTTagCompound tNBT = aStack.getTagCompound();
		return tNBT == null ? 0 : tNBT.getLong("GT.ItemCharge");
	}	

	public final boolean setCharge(ItemStack aStack, long aCharge) {
		Long[] tStats = getElectricStats(aStack);
		if (tStats == null || tStats[3] > 0) return false;
		NBTTagCompound tNBT = aStack.getTagCompound();
		if (tNBT == null) tNBT = new NBTTagCompound();
		tNBT.removeTag("GT.ItemCharge");
		aCharge = Math.min(tStats[0] < 0 ? Math.abs(tStats[0] / 2) : aCharge, Math.abs(tStats[0]));
		if (aCharge > 0) {
			aStack.setItemDamage(getChargedMetaData(aStack));
			tNBT.setLong("GT.ItemCharge", aCharge);
		} else {
			aStack.setItemDamage(getEmptyMetaData(aStack));
		}
		if (tNBT.hasNoTags()) aStack.setTagCompound(null);
		else aStack.setTagCompound(tNBT);
		isItemStackUsable(aStack);
		return true;
	}

	@SuppressWarnings("static-method")
	public short getChargedMetaData(ItemStack aStack) {
		return (short) aStack.getItemDamage();
	}

	@SuppressWarnings("static-method")
	public short getEmptyMetaData(ItemStack aStack) {
		return (short) aStack.getItemDamage();
	}


	public boolean isItemStackUsable(ItemStack aStack) {
		ArrayList<IItemBehaviour<BaseEuItem>> tList = mItemBehaviors.get((short) getDamage(aStack));
		if (tList != null) for (IItemBehaviour<BaseEuItem> tBehavior : tList)
			if (!tBehavior.isItemStackUsable(this, aStack)) return false;
		return true;
	}

	@Override
	public final String getToolTip(ItemStack aStack) {
		return null;
	} // This has its own ToolTip Handler, no need to let the IC2 Handler screw us up at this Point

	@Override
	public final IElectricItemManager getManager(ItemStack aStack) {
		return this;
	} // We are our own Manager

	/**
	 * Sets the Furnace Burn Value for the Item.
	 *
	 * @param aMetaValue the Meta Value of the Item you want to set it to. [0 - 32765]
	 * @param aValue     200 = 1 Burn Process = 500 EU, max = 32767 (that is 81917.5 EU)
	 * @return the Item itself for convenience in constructing.
	 */
	public final BaseEuItem setBurnValue(int aMetaValue, int aValue) {
		if (aMetaValue < 0 || aValue < 0) return this;
		if (aValue == 0) mBurnValues.remove((short) aMetaValue);
		else mBurnValues.put((short) aMetaValue, aValue > Short.MAX_VALUE ? Short.MAX_VALUE : (short) aValue);
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
	public final BaseEuItem setElectricStats(int aMetaValue, long aMaxCharge, long aTransferLimit, long aTier, long aSpecialData, boolean aUseAnimations) {
		if (aMetaValue < 0) return this;
		if (aMaxCharge == 0) mElectricStats.remove((short) aMetaValue);
		else {
			mElectricStats.put((short) aMetaValue, new Long[]{aMaxCharge, Math.max(0, aTransferLimit), Math.max(-1, aTier), aSpecialData});           
		}
		return this;
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item var1, CreativeTabs aCreativeTab, List aList) {
		for (int i = 0, j = mEnabledItems.length(); i < j; i++)
			if (mVisibleItems.get(i) || (D1 && mEnabledItems.get(i))) {
				Long[] tStats = mElectricStats.get((short) (mOffset + i));
				if (tStats != null && tStats[3] < 0) {
					ItemStack tStack = new ItemStack(this, 1, mOffset + i);
					setCharge(tStack, Math.abs(tStats[0]));
					isItemStackUsable(tStack);
					aList.add(tStack);
				}
				if (tStats == null || tStats[3] != -2) {
					ItemStack tStack = new ItemStack(this, 1, mOffset + i);
					isItemStackUsable(tStack);
					aList.add(tStack);
				}
			}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public final void registerIcons(IIconRegister aIconRegister) {
		for (short i = 0, j = (short) mEnabledItems.length(); i < j; i++)
			if (mEnabledItems.get(i)) {
				for (byte k = 1; k < mIconList[i].length; k++) {
					mIconList[i][k] = aIconRegister.registerIcon(CORE.MODID+":" + (getUnlocalizedName() + "/" + i + "/" + k));
				}
				mIconList[i][0] = aIconRegister.registerIcon(CORE.MODID+":" + (getUnlocalizedName() + "/" + i));
			}
	}
	
	
	 @Override
    public final IIcon getIconFromDamage(int aMetaData) {
        if (aMetaData < 0) return null;       
        return aMetaData - mOffset < mIconList.length ? mIconList[aMetaData - mOffset][0] : null;
    }
	
	/**
     * Sets the unlocalized name of this item to the string passed as the parameter"
     */
    @Override
	public Item setUnlocalizedName(String p_77655_1_){
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

	public final Long[] getElectricStats(ItemStack aStack) {
		return mElectricStats.get((short) aStack.getItemDamage());
	}
	
	@Override
    public int getItemEnchantability() {
        return 0;
    }

    @Override
    public boolean isBookEnchantable(ItemStack aStack, ItemStack aBook) {
        return false;
    }

    @Override
    public boolean getIsRepairable(ItemStack aStack, ItemStack aMaterial) {
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
    public final BaseEuItem addItemBehavior(int aMetaValue, IItemBehaviour<BaseEuItem> aBehavior) {
        if (aMetaValue < 0 || aMetaValue >= 32766 || aBehavior == null) return this;
        ArrayList<IItemBehaviour<BaseEuItem>> tList = mItemBehaviors.get((short) aMetaValue);
        if (tList == null) {
            tList = new ArrayList<IItemBehaviour<BaseEuItem>>(1);
            mItemBehaviors.put((short) aMetaValue, tList);
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
	public final ItemStack addItem(int aID, String aEnglish, String aToolTip, Object... aRandomData) {
        if (aToolTip == null) aToolTip = "";
        if (aID >= 0 && aID < mItemAmount) {
            ItemStack rStack = new ItemStack(this, 1, mOffset + aID);
            mEnabledItems.set(aID);
            mVisibleItems.set(aID);
            GT_LanguageManager.addStringLocalization(getUnlocalizedName(rStack) + ".name", aEnglish);
            GT_LanguageManager.addStringLocalization(getUnlocalizedName(rStack) + ".tooltip", aToolTip);
            List<TC_AspectStack> tAspects = new ArrayList<TC_AspectStack>();
            // Important Stuff to do first
            for (Object tRandomData : aRandomData)
                if (tRandomData instanceof SubTag) {
                    if (tRandomData == SubTag.INVISIBLE) {
                        mVisibleItems.set(aID, false);
                        continue;
                    }
                    if (tRandomData == SubTag.NO_UNIFICATION) {
                        GT_OreDictUnificator.addToBlacklist(rStack);
                        continue;
                    }
                }
            // now check for the rest
            for (Object tRandomData : aRandomData)
                if (tRandomData != null) {
                    boolean tUseOreDict = true;                   
                    if (tRandomData instanceof IItemBehaviour) {
                        addItemBehavior(mOffset + aID, (IItemBehaviour<BaseEuItem>) tRandomData);
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
                        if (GT_Utility.isStringValid(tRandomData))
                            GT_OreDictUnificator.registerOre(tRandomData, rStack);
                        else GT_OreDictUnificator.addItemData(rStack, (ItemData) tRandomData);
                        continue;
                    }
                    if (tUseOreDict) {
                        GT_OreDictUnificator.registerOre(tRandomData, rStack);
                        continue;
                    }
                }
            if (GregTech_API.sThaumcraftCompat != null)
                GregTech_API.sThaumcraftCompat.registerThaumcraftAspectsToItem(rStack, tAspects, false);
            return rStack;
        }
        return null;
    }

	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack) {
        return itemName.get(par1ItemStack.getItemDamage()-mOffset).getValue();
	}

}
