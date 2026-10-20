package gregtech.api.objects;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTUtility;

public class MaterialStack implements Cloneable {

    public long amount;
    public Material material;

    public MaterialStack(Material material, long amount) {
        this.material = material == null ? Materials._NULL : material;
        this.amount = amount;
    }

    public MaterialStack copy(long amount) {
        return new MaterialStack(material, amount);
    }

    @Override
    public MaterialStack clone() {
        try {
            return (MaterialStack) super.clone();
        } catch (Exception e) {
            return new MaterialStack(material, amount);
        }
    }

    @Override
    public boolean equals(Object object) {
        if (object == this) return true;
        return switch (object) {
            case null -> false;
            case Material other -> other == material;
            case MaterialStack stack -> stack.material == material
                && (amount < 0 || stack.amount < 0 || stack.amount == amount);
            default -> false;
        };
    }

    @Override
    public String toString() {
        return toString(false);
    }

    public String toString(boolean single) {
        String temp1 = "", temp2 = MaterialUtils.chemicalTooltip(material, true), temp3 = "", temp4 = "";
        if (amount > 1) {
            temp4 = GTUtility.toSubscript(amount);
        }
        if ((!single || amount > 1) && isMaterialListComplex(this)) {
            temp1 = "(";
            temp3 = ")";
        }
        return temp1 + temp2 + temp3 + temp4;
    }

    private boolean isMaterialListComplex(MaterialStack materialStack) {
        var list = MaterialUtils.materialList(materialStack.material);
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
        return material.hashCode();
    }
}
