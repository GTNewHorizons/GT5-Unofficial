package gtPlusPlus.xmod.gregtech.api.objects;

import java.util.*;

import net.minecraft.item.ItemStack;

import gregtech.api.objects.GT_ArrayList;

import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;

public class GregtechItemData {
	private static final GregtechMaterialStack[] EMPTY_GT_MaterialStack_ARRAY = new GregtechMaterialStack[0];

	public final List<Object> mExtraData = new GT_ArrayList<>(false, 1);
	public final GregtechOrePrefixes mPrefix;
	public final GregtechMaterialStack mMaterial;
	public final GregtechMaterialStack[] mByProducts;
	public boolean mBlackListed = false;
	public ItemStack mUnificationTarget = null;

	public GregtechItemData(final GregtechOrePrefixes aPrefix, final GT_Materials aMaterial, final boolean aBlackListed) {
		this.mPrefix = aPrefix;
		this.mMaterial = aMaterial == null ? null : new GregtechMaterialStack(aMaterial, aPrefix.mMaterialAmount);
		this.mBlackListed = aBlackListed;
		this.mByProducts = (aPrefix.mSecondaryMaterial == null) || (aPrefix.mSecondaryMaterial.mMaterial == null) ? EMPTY_GT_MaterialStack_ARRAY : new GregtechMaterialStack[]{aPrefix.mSecondaryMaterial.clone()};
	}

	public GregtechItemData(final GregtechOrePrefixes aPrefix, final GT_Materials aMaterial) {
		this(aPrefix, aMaterial, false);
	}

	public GregtechItemData(final GregtechMaterialStack aMaterial, final GregtechMaterialStack... aByProducts) {
		this.mPrefix = null;
		this.mMaterial = aMaterial.mMaterial == null ? null : aMaterial.clone();
		this.mBlackListed = true;
		if (aByProducts == null) {
			this.mByProducts = EMPTY_GT_MaterialStack_ARRAY;
		} else {
			final GregtechMaterialStack[] tByProducts = aByProducts.length < 1 ? EMPTY_GT_MaterialStack_ARRAY : new GregtechMaterialStack[aByProducts.length];
			int j = 0;
			for (int i = 0; i < aByProducts.length; i++) {
				if ((aByProducts[i] != null) && (aByProducts[i].mMaterial != null)) {
					tByProducts[j++] = aByProducts[i].clone();
				}
			}
			this.mByProducts = j > 0 ? new GregtechMaterialStack[j] : EMPTY_GT_MaterialStack_ARRAY;
			for (int i = 0; i < this.mByProducts.length; i++) {
				this.mByProducts[i] = tByProducts[i];
			}
		}
	}

	public GregtechItemData(final GT_Materials aMaterial, final long aAmount, final GregtechMaterialStack... aByProducts) {
		this(new GregtechMaterialStack(aMaterial, aAmount), aByProducts);
	}

	public GregtechItemData(final GT_Materials aMaterial, final long aAmount, final GT_Materials aByProduct, final long aByProductAmount) {
		this(new GregtechMaterialStack(aMaterial, aAmount), new GregtechMaterialStack(aByProduct, aByProductAmount));
	}

	public GregtechItemData(final GregtechItemData... aData) {
		this.mPrefix = null;
		this.mBlackListed = true;

		final ArrayList<GregtechMaterialStack> aList = new ArrayList<>(), rList = new ArrayList<>();

		for (final GregtechItemData tData : aData) {
			if (tData != null) {
				if (tData.hasValidMaterialData() && (tData.mMaterial.mAmount > 0)) {
					aList.add(tData.mMaterial.clone());
				}
				for (final GregtechMaterialStack tMaterial : tData.mByProducts) {
					if (tMaterial.mAmount > 0) {
						aList.add(tMaterial.clone());
					}
				}
			}
		}

		for (final GregtechMaterialStack aMaterial : aList) {
			boolean temp = true;
			for (final GregtechMaterialStack tMaterial : rList) {
				if (aMaterial.mMaterial == tMaterial.mMaterial) {
					tMaterial.mAmount += aMaterial.mAmount;
					temp = false;
					break;
				}
			}
			if (temp) {
				rList.add(aMaterial.clone());
			}
		}

		Collections.sort(rList, new Comparator<GregtechMaterialStack>() {
			@Override
			public int compare(final GregtechMaterialStack a, final GregtechMaterialStack b) {
				return a.mAmount == b.mAmount ? 0 : a.mAmount > b.mAmount ? -1 : +1;
			}
		});

		if (rList.isEmpty()) {
			this.mMaterial = null;
		} else {
			this.mMaterial = rList.get(0);
			rList.remove(0);
		}

		this.mByProducts = rList.toArray(new GregtechMaterialStack[rList.size()]);
	}

	public boolean hasValidPrefixMaterialData() {
		return (this.mPrefix != null) && (this.mMaterial != null) && (this.mMaterial.mMaterial != null);
	}

	public boolean hasValidPrefixData() {
		return this.mPrefix != null;
	}

	public boolean hasValidMaterialData() {
		return (this.mMaterial != null) && (this.mMaterial.mMaterial != null);
	}

	public ArrayList<GregtechMaterialStack> getAllGT_MaterialStacks() {
		final ArrayList<GregtechMaterialStack> rList = new ArrayList<GregtechMaterialStack>();
		if (this.hasValidMaterialData()) {
			rList.add(this.mMaterial);
		}
		rList.addAll(Arrays.asList(this.mByProducts));
		return rList;
	}

	public GregtechMaterialStack getByProduct(final int aIndex) {
		return (aIndex >= 0) && (aIndex < this.mByProducts.length) ? this.mByProducts[aIndex] : null;
	}

	@Override
	public String toString() {
		if ((this.mPrefix == null) || (this.mMaterial == null) || (this.mMaterial.mMaterial == null)) {
			return "";
		}
		return this.mPrefix.name() + this.mMaterial.mMaterial.name();
	}
}