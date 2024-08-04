package net.glease.ggfab.items;

import static gregtech.api.enums.GT_Values.D1;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import net.glease.ggfab.GGConstants;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import gregtech.api.GregTech_API;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

// mostly stolen from gt5 itself.
public class GGMetaItem_DumbItems extends GT_MetaBase_Item {

    public static final int MAX_ID = 32766;
    private final BitSet mEnabledItems = new BitSet();
    private final BitSet mVisibleItems = new BitSet();
    private final ArrayList<IIcon> mIconList = new ArrayList<>();
    private final TIntObjectMap<IItemContainer> mIconOverride = new TIntObjectHashMap<>();

    public GGMetaItem_DumbItems(String aUnlocalized) {
        super(aUnlocalized);
    }

    /**
     * This adds a Custom Item to the ending Range.
     *
     * @param aID         The Id of the assigned Item [0 - mItemAmount] (The MetaData gets auto-shifted by +mOffset)
     * @param aEnglish    The Default Localized Name of the created Item
     * @param aToolTip    The Default ToolTip of the created Item, you can also insert null for having no ToolTip
     * @param aRandomData The OreDict Names you want to give the Item. Also used for TC Aspects and some other things.
     * @return An ItemStack containing the newly created Item.
     */
    public final ItemStack addItem(int aID, String aEnglish, String aToolTip, Object... aRandomData) {
        if (aID < 0 || aID > MAX_ID) return null;

        if (aToolTip == null) aToolTip = "";
        ItemStack rStack = new ItemStack(this, 1, aID);
        mEnabledItems.set(aID);
        mVisibleItems.set(aID);
        GT_LanguageManager.addStringLocalization(getUnlocalizedName(rStack) + ".name", aEnglish);
        GT_LanguageManager.addStringLocalization(getUnlocalizedName(rStack) + ".tooltip", aToolTip);
        List<TC_Aspects.TC_AspectStack> tAspects = new ArrayList<>();
        // Important Stuff to do first
        for (Object tRandomData : aRandomData) if (tRandomData instanceof SubTag) {
            if (tRandomData == SubTag.INVISIBLE) {
                mVisibleItems.set(aID, false);
                continue;
            }
            if (tRandomData == SubTag.NO_UNIFICATION) {
                GT_OreDictUnificator.addToBlacklist(rStack);
            }
        }
        // now check for the rest
        for (Object tRandomData : aRandomData) if (tRandomData != null) {
            boolean tUseOreDict = true;
            if (tRandomData instanceof IItemBehaviour) {
                @SuppressWarnings("unchecked")
                IItemBehaviour<GT_MetaBase_Item> behavior = (IItemBehaviour<GT_MetaBase_Item>) tRandomData;
                addItemBehavior(aID, behavior);
                tUseOreDict = false;
            }
            if (tRandomData instanceof IItemContainer) {
                ((IItemContainer) tRandomData).set(rStack);
                tUseOreDict = false;
            }
            if (tRandomData instanceof SubTag) {
                continue;
            }
            if (tRandomData instanceof IItemContainer) {
                mIconOverride.put(aID, (IItemContainer) tRandomData);
            } else if (tRandomData instanceof TC_Aspects.TC_AspectStack) {
                ((TC_Aspects.TC_AspectStack) tRandomData).addToAspectList(tAspects);
            } else if (tRandomData instanceof ItemData) {
                if (GT_Utility.isStringValid(tRandomData)) {
                    GT_OreDictUnificator.registerOre(tRandomData, rStack);
                } else {
                    GT_OreDictUnificator.addItemData(rStack, (ItemData) tRandomData);
                }
            } else if (tUseOreDict) {
                GT_OreDictUnificator.registerOre(tRandomData, rStack);
            }
        }
        if (GregTech_API.sThaumcraftCompat != null)
            GregTech_API.sThaumcraftCompat.registerThaumcraftAspectsToItem(rStack, tAspects, false);
        return rStack;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void registerIcons(IIconRegister aIconRegister) {
        short j = (short) mEnabledItems.length();
        mIconList.clear();
        mIconList.ensureCapacity(j);
        for (short i = 0; i < j; i++) {
            if (mEnabledItems.get(i)) {
                mIconList.add(aIconRegister.registerIcon(GGConstants.RES_PATH_ITEM + getUnlocalizedName() + "/" + i));
            } else {
                mIconList.add(null);
            }
        }
    }

    @Override
    public IIcon getIconFromDamage(int aMetaData) {
        if (aMetaData < 0 || aMetaData >= mIconList.size() || mIconList.get(aMetaData) == null)
            return super.getIconFromDamage(aMetaData);
        return mIconList.get(aMetaData);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item aItem, CreativeTabs aCreativeTab, List aList) {
        int j = mEnabledItems.length();
        for (int i = 0; i < j; i++) {
            if (mVisibleItems.get(i) || (D1 && mEnabledItems.get(i))) {
                ItemStack tStack = new ItemStack(this, 1, i);
                isItemStackUsable(tStack);
                aList.add(tStack);
            }
        }
    }

    @Override
    public Long[] getElectricStats(ItemStack aStack) {
        return null;
    }

    @Override
    public Long[] getFluidContainerStats(ItemStack aStack) {
        return null;
    }
}
