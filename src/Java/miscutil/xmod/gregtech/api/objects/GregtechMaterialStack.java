package miscutil.xmod.gregtech.api.objects;

import miscutil.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;

public class GregtechMaterialStack implements Cloneable {
    public long mAmount;
    public GT_Materials mMaterial;

    public GregtechMaterialStack(GT_Materials aMaterial, long aAmount) {
        mMaterial = aMaterial == null ? GT_Materials._NULL : aMaterial;
        mAmount = aAmount;
    }

    public GregtechMaterialStack copy(long aAmount) {
        return new GregtechMaterialStack(mMaterial, aAmount);
    }

    @Override
    public GregtechMaterialStack clone() {
        return new GregtechMaterialStack(mMaterial, mAmount);
    }

    @Override
    public boolean equals(Object aObject) {
        if (aObject == this) return true;
        if (aObject == null) return false;
        if (aObject instanceof GT_Materials) return aObject == mMaterial;
        if (aObject instanceof GregtechMaterialStack)
            return ((GregtechMaterialStack) aObject).mMaterial == mMaterial && (mAmount < 0 || ((GregtechMaterialStack) aObject).mAmount < 0 || ((GregtechMaterialStack) aObject).mAmount == mAmount);
        return false;
    }

    @Override
    public String toString() {
        return (mMaterial.mMaterialList.size() > 1 && mAmount > 1 ? "(" : "") + mMaterial.getToolTip(true) + (mMaterial.mMaterialList.size() > 1 && mAmount > 1 ? ")" : "") + (mAmount > 1 ? mAmount : "");
    }

    @Override
    public int hashCode() {
        return mMaterial.hashCode();
    }
}