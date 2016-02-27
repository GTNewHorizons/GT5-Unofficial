package miscutil.gregtech.objects;

import miscutil.gregtech.enums.Materials2;


public class GregMaterialStack implements Cloneable {
	public long mAmount;
	public Materials2 mMaterial;
	
	public GregMaterialStack(Materials2 nitrogen, long aAmount) {
		mMaterial = nitrogen==null?Materials2._NULL:nitrogen;
		mAmount = aAmount;
	}
	
	public GregMaterialStack copy(long aAmount) {
		return new GregMaterialStack(mMaterial, aAmount);
	}
	
	@Override
	public GregMaterialStack clone() {
		return new GregMaterialStack(mMaterial, mAmount);
	}
	
	@Override
	public boolean equals(Object aObject) {
		if (aObject == this) return true;
		if (aObject == null) return false;
		if (aObject instanceof Materials2) return aObject == mMaterial;
		if (aObject instanceof GregMaterialStack) return ((GregMaterialStack)aObject).mMaterial == mMaterial && (mAmount < 0 || ((GregMaterialStack)aObject).mAmount < 0 || ((GregMaterialStack)aObject).mAmount == mAmount);
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