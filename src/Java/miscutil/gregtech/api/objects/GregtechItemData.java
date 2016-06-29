package miscutil.gregtech.api.objects;

import gregtech.api.objects.GT_ArrayList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import miscutil.gregtech.api.enums.GregtechOrePrefixes;
import miscutil.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import net.minecraft.item.ItemStack;

public class GregtechItemData {
    private static final GregtechMaterialStack[] EMPTY_GT_MaterialStack_ARRAY = new GregtechMaterialStack[0];

    public final List<Object> mExtraData = new GT_ArrayList<Object>(false, 1);
    public final GregtechOrePrefixes mPrefix;
    public final GregtechMaterialStack mMaterial;
    public final GregtechMaterialStack[] mByProducts;
    public boolean mBlackListed = false;
    public ItemStack mUnificationTarget = null;

    public GregtechItemData(GregtechOrePrefixes aPrefix, GT_Materials aMaterial, boolean aBlackListed) {
        mPrefix = aPrefix;
        mMaterial = aMaterial == null ? null : new GregtechMaterialStack(aMaterial, aPrefix.mMaterialAmount);
        mBlackListed = aBlackListed;
        mByProducts = aPrefix.mSecondaryMaterial == null || aPrefix.mSecondaryMaterial.mMaterial == null ? EMPTY_GT_MaterialStack_ARRAY : new GregtechMaterialStack[]{aPrefix.mSecondaryMaterial.clone()};
    }

    public GregtechItemData(GregtechOrePrefixes aPrefix, GT_Materials aMaterial) {
        this(aPrefix, aMaterial, false);
    }

    public GregtechItemData(GregtechMaterialStack aMaterial, GregtechMaterialStack... aByProducts) {
        mPrefix = null;
        mMaterial = aMaterial.mMaterial == null ? null : aMaterial.clone();
        mBlackListed = true;
        if (aByProducts == null) {
            mByProducts = EMPTY_GT_MaterialStack_ARRAY;
        } else {
            GregtechMaterialStack[] tByProducts = aByProducts.length < 1 ? EMPTY_GT_MaterialStack_ARRAY : new GregtechMaterialStack[aByProducts.length];
            int j = 0;
            for (int i = 0; i < aByProducts.length; i++)
                if (aByProducts[i] != null && aByProducts[i].mMaterial != null)
                    tByProducts[j++] = aByProducts[i].clone();
            mByProducts = j > 0 ? new GregtechMaterialStack[j] : EMPTY_GT_MaterialStack_ARRAY;
            for (int i = 0; i < mByProducts.length; i++) mByProducts[i] = tByProducts[i];
        }
    }

    public GregtechItemData(GT_Materials aMaterial, long aAmount, GregtechMaterialStack... aByProducts) {
        this(new GregtechMaterialStack(aMaterial, aAmount), aByProducts);
    }

    public GregtechItemData(GT_Materials aMaterial, long aAmount, GT_Materials aByProduct, long aByProductAmount) {
        this(new GregtechMaterialStack(aMaterial, aAmount), new GregtechMaterialStack(aByProduct, aByProductAmount));
    }

    public GregtechItemData(GregtechItemData... aData) {
        mPrefix = null;
        mBlackListed = true;

        ArrayList<GregtechMaterialStack> aList = new ArrayList<GregtechMaterialStack>(), rList = new ArrayList<GregtechMaterialStack>();

        for (GregtechItemData tData : aData)
            if (tData != null) {
                if (tData.hasValidMaterialData() && tData.mMaterial.mAmount > 0) aList.add(tData.mMaterial.clone());
                for (GregtechMaterialStack tMaterial : tData.mByProducts)
                    if (tMaterial.mAmount > 0) aList.add(tMaterial.clone());
            }

        for (GregtechMaterialStack aMaterial : aList) {
            boolean temp = true;
            for (GregtechMaterialStack tMaterial : rList)
                if (aMaterial.mMaterial == tMaterial.mMaterial) {
                    tMaterial.mAmount += aMaterial.mAmount;
                    temp = false;
                    break;
                }
            if (temp) rList.add(aMaterial.clone());
        }

        Collections.sort(rList, new Comparator<GregtechMaterialStack>() {
            @Override
            public int compare(GregtechMaterialStack a, GregtechMaterialStack b) {
                return a.mAmount == b.mAmount ? 0 : a.mAmount > b.mAmount ? -1 : +1;
            }
        });

        if (rList.isEmpty()) {
            mMaterial = null;
        } else {
            mMaterial = rList.get(0);
            rList.remove(0);
        }

        mByProducts = rList.toArray(new GregtechMaterialStack[rList.size()]);
    }

    public boolean hasValidPrefixMaterialData() {
        return mPrefix != null && mMaterial != null && mMaterial.mMaterial != null;
    }

    public boolean hasValidPrefixData() {
        return mPrefix != null;
    }

    public boolean hasValidMaterialData() {
        return mMaterial != null && mMaterial.mMaterial != null;
    }

    public ArrayList<GregtechMaterialStack> getAllGT_MaterialStacks() {
        ArrayList<GregtechMaterialStack> rList = new ArrayList();
        if (hasValidMaterialData()) rList.add(mMaterial);
        rList.addAll(Arrays.asList(mByProducts));
        return rList;
    }

    public GregtechMaterialStack getByProduct(int aIndex) {
        return aIndex >= 0 && aIndex < mByProducts.length ? mByProducts[aIndex] : null;
    }

    @Override
    public String toString() {
        if (mPrefix == null || mMaterial == null || mMaterial.mMaterial == null) return "";
        return mPrefix.name() + mMaterial.mMaterial.name();
    }
}