package gregtech.api.objects;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.Materials;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.material.MU;
import gregtech.api.util.GTUtility;
import gregtech.loaders.materials.RecognitionMaterials.RecognitionMarker;

public class MaterialStack implements Cloneable {

    public long mAmount;
    public Material mMaterial;

    public MaterialStack(Material material, long amount) {
        mMaterial = material == null ? Materials2Materials.NULL : material;
        mAmount = amount;
    }

    /// Transitional: accepts a legacy [Materials] through [MU#toMaterial] until every caller passes a
    /// [Material] directly. An unbacked legacy material collapses to the `_NULL` sentinel.
    public MaterialStack(Materials material, long amount) {
        this(MU.toMaterial(material), amount);
    }

    /// [#MaterialStack(Materials, long)] for a recognition marker, resolving its registered backing.
    public MaterialStack(RecognitionMarker material, long amount) {
        this(MU.toMaterial(material), amount);
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
        if (object instanceof Material) return object == mMaterial;
        if (object instanceof Materials legacy) return MU.material(legacy) == mMaterial;
        if (object instanceof MaterialStack stack)
            return stack.mMaterial == mMaterial && (mAmount < 0 || stack.mAmount < 0 || stack.mAmount == mAmount);
        return false;
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean single) {
        String temp1 = "", temp2 = MU.chemicalTooltip(mMaterial, true), temp3 = "", temp4 = "";
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
        var list = MU.materialList(materialStack.mMaterial);
        if (list.size() > 1) {
            return true;
        }
        if (list.isEmpty()) {
            return false;
        }
        return isMaterialListComplex(list.get(0));
    }

    @Override
    public int hashCode() {
        return mMaterial.hashCode();
    }
}
