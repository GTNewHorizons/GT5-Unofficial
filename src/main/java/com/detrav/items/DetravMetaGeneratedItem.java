package com.detrav.items;

/**
 * Created by wital_000 on 19.03.2016.
 */
import com.detrav.enums.DetravSimpleItems;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
//import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaGenerated_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.Arrays;
import java.util.List;

import static gregtech.api.enums.GT_Values.M;

/**
 * @author Gregorius Techneticies
 *         <p/>
 *         One Item for everything!
 *         <p/>
 *         This brilliant Item Class is used for automatically generating all possible variations of Material Items, like Dusts, Ingots, Gems, Plates and similar.
 *         It saves me a ton of work, when adding Items, because I always have to make a new Item SubType for each OreDict Prefix, when adding a new Material.
 *         <p/>
 *         As you can see, up to 32766 Items can be generated using this Class. And the last 766 Items can be custom defined, just to save space and MetaData.
 *         <p/>
 *         These Items can also have special RightClick abilities, electric Charge or even be set to become a Food alike Item.
 */
public abstract class DetravMetaGeneratedItem extends GT_MetaGenerated_Item {

    protected final DetravSimpleItems[] mGeneratedItemList;


    public DetravMetaGeneratedItem(String aUnlocalized, DetravSimpleItems... aGeneratedItemList) {
        super(aUnlocalized, (short) 32000, (short) 766);
        mGeneratedItemList = Arrays.copyOf(aGeneratedItemList, 32);

        for (int i = 0; i < 32000; i++) {
            DetravSimpleItems tPrefix = mGeneratedItemList[i / 1000];
            if (tPrefix == null) continue;
            Materials tMaterial = GregTech_API.sGeneratedMaterials[i % 1000];
            if (tMaterial == null) continue;
            if (doesMaterialAllowGeneration(tPrefix, tMaterial)) {
                ItemStack tStack = new ItemStack(this, 1, i);
                GT_LanguageManager.addStringLocalization(getUnlocalizedName(tStack) + ".name", getDefaultLocalization(tPrefix, tMaterial, i));
                GT_LanguageManager.addStringLocalization(getUnlocalizedName(tStack) + ".tooltip", tMaterial.getToolTip(tPrefix.getOrePrefixes().mMaterialAmount / M));
                //Не знаю можно ли регистрировать, т.к. уже должен быть зареганы паренты с моего класса
                if (tPrefix.mIsUnificatable) {
                    GT_OreDictUnificator.set(tPrefix.getOrePrefixes(), tMaterial, tStack);
                } else {
                    GT_OreDictUnificator.registerOre(tPrefix.get(tMaterial), tStack);
                }/*
                if ((tPrefix == OrePrefixes.stick || tPrefix == OrePrefixes.wireFine || tPrefix == OrePrefixes.ingot) && (tMaterial == Materials.Lead || tMaterial == Materials.Tin || tMaterial == Materials.SolderingAlloy)) {
                    GregTech_API.sSolderingMetalList.add(tStack);
                }*/
            }
        }
    }

	/* ---------- OVERRIDEABLE FUNCTIONS ---------- */

    /**
     * @return the Color Modulation the Material is going to be rendered with.
     */
    @Override
    public short[] getRGBa(ItemStack aStack) {
        Materials tMaterial = GregTech_API.sGeneratedMaterials[getDamage(aStack) % 1000];
        return tMaterial == null ? Materials._NULL.mRGBa : tMaterial.mRGBa;
    }

    /**
     * @param aPrefix   this can be null, you have to return false in that case
     * @param aMaterial this can be null, you have to return false in that case
     * @return if this Item should be generated and visible.
     */
    public boolean doesMaterialAllowGeneration(DetravSimpleItems aPrefix, Materials aMaterial) {
        // You have to check for at least these Conditions in every Case! So add a super Call like the following for this before executing your Code:
        // if (!super.doesMaterialAllowGeneration(aPrefix, aMaterial)) return false;
        return aPrefix != null && aMaterial != null && aPrefix.getOrePrefixes().doGenerateItem(aMaterial);
    }

	/* ---------- OVERRIDEABLE FUNCTIONS ---------- */

    /**
     * @param aPrefix   the OreDict Prefix
     * @param aMaterial the Material
     * @param aMetaData a Index from [0 - 31999]
     * @return the Localized Name when default LangFiles are used.
     */
    public String getDefaultLocalization(DetravSimpleItems aPrefix, Materials aMaterial, int aMetaData) {
        return aPrefix.getDefaultLocalNameForItem(aMaterial);
    }

    /**
     * @param aMetaData a Index from [0 - 31999]
     * @param aMaterial the Material
     * @return an Icon Container for the Item Display.
     */
    public IIconContainer getIconContainer(int aMetaData, Materials aMaterial) {
        return mGeneratedItemList[aMetaData / 1000] != null && mGeneratedItemList[aMetaData / 1000].getOrePrefixes().mTextureIndex >= 0 ? aMaterial.mIconSet.mTextures[mGeneratedItemList[aMetaData / 1000].getOrePrefixes().mTextureIndex] : null;
    }

    /**
     * @param aPrefix         always != null
     * @param aMaterial       always != null
     * @param aDoShowAllItems this is the Configuration Setting of the User, if he wants to see all the Stuff like Tiny Dusts or Crushed Ores as well.
     * @return if this Item should be visible in NEI or Creative
     */
    public boolean doesShowInCreative(DetravSimpleItems aPrefix, Materials aMaterial, boolean aDoShowAllItems) {
        return true;
    }

	/* ---------- INTERNAL OVERRIDES ---------- */

    @Override
    public ItemStack getContainerItem(ItemStack aStack) {
        int aDamage = aStack.getItemDamage();
        if (aDamage < 32000 && aDamage >= 0) {
            Materials aMaterial = GregTech_API.sGeneratedMaterials[aDamage % 1000];
            if (aMaterial != null && aMaterial != Materials.Empty && aMaterial != Materials._NULL) {
                DetravSimpleItems aPrefix = mGeneratedItemList[aDamage / 1000];
                if (aPrefix != null) return GT_Utility.copyAmount(1, aPrefix.mContainerItem);
            }
        }
        return null;
    }

    @Override
    public final IIconContainer getIconContainer(int aMetaData) {
        return GregTech_API.sGeneratedMaterials[aMetaData % 1000] == null ? null : getIconContainer(aMetaData, GregTech_API.sGeneratedMaterials[aMetaData % 1000]);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public final void getSubItems(Item var1, CreativeTabs aCreativeTab, List aList) {
        for (int i = 0; i < 32000; i++)
            if (doesMaterialAllowGeneration(mGeneratedItemList[i / 1000], GregTech_API.sGeneratedMaterials[i % 1000]) && doesShowInCreative(mGeneratedItemList[i / 1000], GregTech_API.sGeneratedMaterials[i % 1000], GregTech_API.sDoShowAllItemsInCreative)) {
                ItemStack tStack = new ItemStack(this, 1, i);
                isItemStackUsable(tStack);
                aList.add(tStack);
            }
        super.getSubItems(var1, aCreativeTab, aList);
    }

    @Override
    public final IIcon getIconFromDamage(int aMetaData) {
        if (aMetaData < 0) return null;
        if (aMetaData < 32000) {
            Materials tMaterial = GregTech_API.sGeneratedMaterials[aMetaData % 1000];
            if (tMaterial == null) return null;
            IIconContainer tIcon = getIconContainer(aMetaData, tMaterial);
            if (tIcon != null) return tIcon.getIcon();
            return null;
        }
        return aMetaData - 32000 < mIconList.length ? mIconList[aMetaData - 32000][0] : null;
    }

    @Override
    public int getItemStackLimit(ItemStack aStack) {
        int tDamage = getDamage(aStack);
        if (tDamage < 32000 && mGeneratedItemList[tDamage / 1000] != null)
            return Math.min(super.getItemStackLimit(aStack), mGeneratedItemList[tDamage / 1000].mDefaultStackSize);
        return super.getItemStackLimit(aStack);
    }
}