package com.detrav.utils;

import com.detrav.DetravScannerMod;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TC_Aspects;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by wital_000 on 19.03.2016.
 */
public abstract class DetravMetaGeneratedItem extends GT_MetaBase_Item {

    public static final HashMap<String, DetravMetaGeneratedItem> sInstances = new HashMap<String, DetravMetaGeneratedItem>();
    public IIcon[][] mIconList;


    public DetravMetaGeneratedItem(String aUnlocalized) {
        super(aUnlocalized);
        setCreativeTab(DetravScannerMod.TAB_DETRAV);
        setMaxStackSize(64);

        mIconList = new IIcon[1][1];

        sInstances.put(getUnlocalizedName(), this);
    }

    public final ItemStack addItem(int aID, String aEnglish, String aToolTip, Object... aOreDictNamesAndAspects)
    {
        if(aToolTip == null)
            aToolTip = "";
        if(aID >= 0 && aID < 32766) {
            GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." +  aID    + ".name"     , aEnglish);
            GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." +  aID    + ".tooltip"  , aToolTip);
            GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + (aID+1) + ".name"     , aEnglish + " (Broken)");
            GT_LanguageManager.addStringLocalization(getUnlocalizedName() + "." + (aID+1) + ".tooltip"  , "Perhaps you can salvage some?");
            ItemStack rStack = new ItemStack(this, 1, aID);
            List<TC_Aspects.TC_AspectStack> tAspects = new ArrayList<TC_Aspects.TC_AspectStack>();
            for (Object tOreDictNameOrAspect : aOreDictNamesAndAspects)
            {
                if (tOreDictNameOrAspect instanceof TC_Aspects.TC_AspectStack)
                    ((TC_Aspects.TC_AspectStack)tOreDictNameOrAspect).addToAspectList(tAspects);
                else
                    GT_OreDictUnificator.registerOre(tOreDictNameOrAspect, rStack);
            }
            if (GregTech_API.sThaumcraftCompat != null)
                GregTech_API.sThaumcraftCompat.registerThaumcraftAspectsToItem(rStack, tAspects, false);
            return rStack;
        }
        return null;
    }

    public final ItemStack getItem(int aID, int aAmount, Materials aPrimaryMaterial)
    {
        ItemStack rStack = new ItemStack(this, aAmount, aID);
        return rStack;
    }


    @Override
    public Long[] getElectricStats(ItemStack itemStack) {
        return null;
    }

    @Override
    public Long[] getFluidContainerStats(ItemStack itemStack) {
        return null;
    }

    @Override
    public final int getItemStackLimit(ItemStack aStack)
    {
        return 64;
    }

    public IIconContainer getIconContainer(short aMetaData) {
        //return
        //return mIconList[aMetaData][1];
        switch (aMetaData)
        {
            case 1:
                return Textures.ItemIcons.PRO_PICK_HEAD;
            default:
                return Textures.ItemIcons.RENDERING_ERROR;
        }
    }

    public short[] getRGBa(ItemStack aStack) {
        return Materials._NULL.getRGBA();
    }


    @Override
    @SideOnly(Side.CLIENT)
    public final void registerIcons(IIconRegister aIconRegister) {
        //mIconList[1][1] = aIconRegister.registerIcon()
    }
}
