package com.detrav.enums;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import net.minecraft.item.ItemStack;

/**
 * Created by wital_000 on 19.03.2016.
 */
public enum DetravSimpleItems {
    toolProPickHead(OrePrefixes.toolHeadPickaxe,"Prospector's Pick Heads",""," Prospector's Pick Head",0);

    static {


    }

    private final String mLocalName;
    private final OrePrefixes parent;
    private final String mPrefix;
    private final String mPostFix;
    public ItemStack mContainerItem;
    public int mDefaultStackSize = 64;
    public boolean mIsUnificatable = false;
    public int mTextureIndex;


    DetravSimpleItems(OrePrefixes orePrefixes, String s,String aPrefix, String aPostfix, int aTextureIndex) {
        parent = orePrefixes;
        mLocalName = s;
        mPrefix = aPrefix;
        mPostFix = aPostfix;
        mTextureIndex = aTextureIndex;
    }

    public OrePrefixes getOrePrefixes()
    {
        return parent;
    }

    public String getDefaultLocalNameForItem(Materials aMaterial) {
        String result = mPrefix + aMaterial.toString() + mPostFix;
        return result;
    }

    public Object get(Materials tMaterial) {
        Object result = toString()+tMaterial.toString();
        return result;
    }
}
