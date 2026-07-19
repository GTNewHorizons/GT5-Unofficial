package gregtech.api.objects;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.util.GTUtility;

public class MaterialStack implements Cloneable {

    public long mAmount;
    public IOreMaterial mMaterial;

    public MaterialStack(IOreMaterial aMaterial, long aAmount) {
        mMaterial = aMaterial == null ? Materials._NULL : aMaterial;
        mAmount = aAmount;
    }

    public MaterialStack copy(long aAmount) {
        return new MaterialStack(mMaterial, aAmount);
    }

    @Override
    public MaterialStack clone() {
        try {
            return (MaterialStack) super.clone();
        } catch (Exception e) {
            return new MaterialStack(mMaterial, mAmount);
        }
    }

    @Override
    public boolean equals(Object aObject) {
        if (aObject == this) return true;
        return switch (aObject) {
            case null -> false;
            case Materials materials -> aObject == mMaterial;
            case MaterialStack materialStack -> materialStack.mMaterial == mMaterial
                && (mAmount < 0 || materialStack.mAmount < 0 || materialStack.mAmount == mAmount);
            default -> false;
        };
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean single) {
        String temp1 = "", temp2 = ((Materials) mMaterial).getChemicalTooltip(true), temp3 = "", temp4 = "";
        if (mAmount > 1) {
            temp4 = GTUtility.toSubscript(mAmount);
        }
        if ((!single || mAmount > 1) && isMaterialListComplex(this)) {
            temp1 = "(";
            temp3 = ")";
        }
        return temp1 + temp2 + temp3 + temp4;
    }

    private boolean isMaterialListComplex(MaterialStack materialStack) {
        if (((Materials) materialStack.mMaterial).mMaterialList.size() > 1) {
            return true;
        }
        if (((Materials) materialStack.mMaterial).mMaterialList.isEmpty()) {
            return false;
        }
        return isMaterialListComplex(((Materials) materialStack.mMaterial).mMaterialList.get(0));
    }

    @Override
    public int hashCode() {
        return mMaterial.hashCode();
    }
}
