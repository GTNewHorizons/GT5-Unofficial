package com.detrav.items;

//import gregtech.api.items.DetravMetaGeneratedItem;

import com.detrav.DetravScannerMod;
import com.detrav.enums.DetravItemList;
import com.detrav.enums.DetravSimpleItems;
import com.detrav.enums.Textures01;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IIconContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

/**
 * Created by wital_000 on 19.03.2016.
 */
public class DetravMetaGeneratedItem01 extends DetravMetaGeneratedItem {

    public static DetravMetaGeneratedItem01 INSTANCE;
    //private final String mToolTipPurify = GT_LanguageManager.addStringLocalization("metaitem.01.tooltip.purify", "Throw into Cauldron to get clean Dust");

    public DetravMetaGeneratedItem01() {
        super("detrav.metaitem.01", new DetravSimpleItems[]{DetravSimpleItems.toolHeadProPick});
        INSTANCE = this;
        this.setCreativeTab(DetravScannerMod.TAB_DETRAV);
        int tLastID;
        DetravItemList.Shape_Extruder_ProPick.set(addItem(tLastID = 0, "Extruder Shape (Prospector's Pick Head)", "Extruder Shape for making Prospector's Picks", new Object[0]));
        DetravItemList.ConfiguredCircuit.set(addItem(tLastID = 1, "Configured Circuit", "", new Object[0]));
        //DetravItemList.Anvil.set(addItem(tLastID = 1, "Detrav Anvil","",new Object[0]));
        //addItemBehavior(tLastID,new BahaviourDetravAnvil());
    }

    @Override
    public final IIconContainer getIconContainer(int aMetaData, Materials aMaterial) {
        return mGeneratedItemList[aMetaData / 1000] != null && mGeneratedItemList[aMetaData / 1000].mTextureIndex >= 0 ? Textures01.mTextures[mGeneratedItemList[aMetaData / 1000].mTextureIndex] : null;
    }

    public boolean doesShowInCreative(DetravSimpleItems aPrefix, Materials aMaterial, boolean aDoShowAllItems) {
        return aDoShowAllItems || !aPrefix.name().startsWith("toolHead");
    }

    public boolean isConfiguredCircuit(ItemStack aStack)
    {
        return aStack.getUnlocalizedName().indexOf("gt.detrav.metaitem.01." + (mOffset+1)) == 0;
    }



    public void addAdditionalToolTips(List aList, ItemStack aStack, EntityPlayer aPlayer) {
        int tOffset = aList.size();
        String name = aStack.getUnlocalizedName();
        String num = name.substring("gt.detrav.metaitem.01.".length());
        int meta = Integer.parseInt(num) - mOffset;
        switch (meta) {
            case 1: {
                NBTTagCompound aNBT = aStack.getTagCompound();
                if (aNBT != null) {
                    NBTTagCompound detravPosition = aNBT.getCompoundTag("DetravPosition");
                    if (detravPosition != null) {
                        tOffset = addToList(tOffset, aList, detravPosition, "Finished", false);
                        tOffset = addToList(tOffset, aList, detravPosition, "Percent", true);
                        tOffset = addToList(tOffset, aList, detravPosition, "XCurrent", true);
                        tOffset = addToList(tOffset, aList, detravPosition, "ZCurrent", true);
                        tOffset = addToList(tOffset, aList, detravPosition, "XFrom", true);
                        tOffset = addToList(tOffset, aList, detravPosition, "XTo", true);
                        tOffset = addToList(tOffset, aList, detravPosition, "ZFrom", true);
                        tOffset = addToList(tOffset, aList, detravPosition, "ZTo", true);
                        tOffset = addToList(tOffset, aList, detravPosition, "Configurated", false);
                    }
                }
            }
            break;
        }
    }

    public int addToList(int tOffset, List aList, NBTTagCompound tag,String name, boolean integer) {
        if (!tag.hasKey(name))
            return tOffset;
        if (integer) {
            int value = tag.getInteger(name);
            aList.add(tOffset, EnumChatFormatting.WHITE + name + " = " + value);
        } else {
            if (tag.getBoolean(name)) {
                aList.add(tOffset, EnumChatFormatting.WHITE + name + EnumChatFormatting.GRAY);
            }
        }
        return  tOffset + 1;
    }
}
