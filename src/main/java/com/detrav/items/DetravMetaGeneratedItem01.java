package com.detrav.items;

//import gregtech.api.items.DetravMetaGeneratedItem;

import com.detrav.DetravScannerMod;
import com.detrav.enums.DetravItemList;
import com.detrav.enums.DetravSimpleItems;
import com.detrav.enums.Textures01;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IIconContainer;

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
}
