package gtPlusPlus.xmod.gregtech.api.objects;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;

public class GregtechMaterialStack implements Cloneable {
	public long			mAmount;
	public GT_Materials	mMaterial;

	public GregtechMaterialStack(final GT_Materials aMaterial, final long aAmount) {
		this.mMaterial = aMaterial == null ? GT_Materials._NULL : aMaterial;
		this.mAmount = aAmount;
	}

	@Override
	public GregtechMaterialStack clone() {
		return new GregtechMaterialStack(this.mMaterial, this.mAmount);
	}

	public GregtechMaterialStack copy(final long aAmount) {
		return new GregtechMaterialStack(this.mMaterial, aAmount);
	}

	@Override
	public boolean equals(final Object aObject) {
		if (aObject == this) {
			return true;
		}
		if (aObject == null) {
			return false;
		}
		if (aObject instanceof GT_Materials) {
			return aObject == this.mMaterial;
		}
		if (aObject instanceof GregtechMaterialStack) {
			return ((GregtechMaterialStack) aObject).mMaterial == this.mMaterial
					&& (this.mAmount < 0 || ((GregtechMaterialStack) aObject).mAmount < 0
							|| ((GregtechMaterialStack) aObject).mAmount == this.mAmount);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return this.mMaterial.hashCode();
	}

	@Override
	public String toString() {
		return (this.mMaterial.mMaterialList.size() > 1 && this.mAmount > 1 ? "(" : "")
				+ this.mMaterial.getToolTip(true)
				+ (this.mMaterial.mMaterialList.size() > 1 && this.mAmount > 1 ? ")" : "")
				+ (this.mAmount > 1 ? this.mAmount : "");
	}
}