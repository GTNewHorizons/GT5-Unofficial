package gregtech.api.objects;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.util.GTUtility;

public class MaterialStack implements Cloneable {

    public long mAmount;
    public IOreMaterial mMaterial;

    public MaterialStack(IOreMaterial material, long amount) {
        mMaterial = material == null ? Materials._NULL : material;
        mAmount = amount;
    }

    public MaterialStack copy(long amount) {
        return new MaterialStack(mMaterial, amount);
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
    public boolean equals(Object object) {
        if (object == this) return true;
        if (object == null) return false;
        if (object instanceof Materials) return object == mMaterial;
        if (object instanceof MaterialStack) return ((MaterialStack) object).mMaterial == mMaterial
            && (mAmount < 0 || ((MaterialStack) object).mAmount < 0 || ((MaterialStack) object).mAmount == mAmount);
        return false;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean single) {
        String temp1 = "", temp2 = mMaterial.getChemicalTooltip(true), temp3 = "", temp4 = "";
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
        if (materialStack.mMaterial.getMaterialList()
            .size() > 1) {
            return true;
        }
        if (materialStack.mMaterial.getMaterialList()
            .isEmpty()) {
            return false;
        }
        return isMaterialListComplex(
            materialStack.mMaterial.getMaterialList()
                .get(0));
    }

    @Override
    public int hashCode() {
        return mMaterial.hashCode();
    }
}
