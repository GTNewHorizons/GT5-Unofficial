package gregtech.api.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.material.MU;

public class ItemData {

    private static final MaterialStack[] EMPTY_MATERIALSTACK_ARRAY = new MaterialStack[0];

    public final List<Object> mExtraData = new GTArrayList<>(false, 1);
    public final OrePrefixes mPrefix;
    public final MaterialStack mMaterial;
    public final MaterialStack[] mByProducts;
    public boolean mBlackListed = false;
    public ItemStack mUnificationTarget = null;
    private final String oreDictName;

    public ItemData(OrePrefixes prefix, Material material, boolean blackListed) {
        mPrefix = prefix;
        mMaterial = material == null ? null : new MaterialStack(material, prefix.getMaterialAmount());
        mBlackListed = blackListed;
        mByProducts = prefix.mSecondaryMaterial == null || prefix.mSecondaryMaterial.mMaterial == null
            ? EMPTY_MATERIALSTACK_ARRAY
            : new MaterialStack[] { prefix.mSecondaryMaterial.clone() };
        oreDictName = null;
    }

    public ItemData(OrePrefixes prefix, Material material) {
        this(prefix, material, false);
    }

    /// Transitional: accepts the legacy material types through [MU#toMaterial] until every caller passes a
    /// [Material] directly.
    public ItemData(OrePrefixes prefix, IOreMaterial material, boolean blackListed) {
        this(prefix, MU.toMaterial(material), blackListed);
    }

    public ItemData(OrePrefixes prefix, IOreMaterial material) {
        this(prefix, MU.toMaterial(material), false);
    }

    /// An ingredient that names an ore-dictionary entry and carries no composition, for the entries whose
    /// material side is not a material at all.
    public ItemData(OrePrefixes prefix, String materialName) {
        this(prefix, materialName, false);
    }

    /// An ingredient that names an ore-dictionary entry and carries no composition, blacklisted from unification
    /// the way [#ItemData(OrePrefixes,IOreMaterial,boolean)] blacklists a material-backed one.
    public ItemData(OrePrefixes prefix, String materialName, boolean blackListed) {
        mPrefix = prefix;
        mMaterial = null;
        mBlackListed = blackListed;
        mByProducts = EMPTY_MATERIALSTACK_ARRAY;
        oreDictName = prefix.oreDictName(materialName);
    }

    public ItemData(MaterialStack material, MaterialStack... byProducts) {
        mPrefix = null;
        mMaterial = material.mMaterial == null ? null : material.clone();
        mBlackListed = true;
        if (byProducts == null) {
            mByProducts = EMPTY_MATERIALSTACK_ARRAY;
        } else {
            MaterialStack[] tByProducts = byProducts.length < 1 ? EMPTY_MATERIALSTACK_ARRAY
                : new MaterialStack[byProducts.length];
            int j = 0;
            for (MaterialStack byProduct : byProducts)
                if (byProduct != null && byProduct.mMaterial != null) tByProducts[j++] = byProduct.clone();
            mByProducts = j > 0 ? new MaterialStack[j] : EMPTY_MATERIALSTACK_ARRAY;
            System.arraycopy(tByProducts, 0, mByProducts, 0, mByProducts.length);
        }
        oreDictName = null;
    }

    public ItemData(Material material, long amount, MaterialStack... byProducts) {
        this(new MaterialStack(material, amount), byProducts);
    }

    public ItemData(Material material, long amount, Material byProduct, long byProductAmount) {
        this(new MaterialStack(material, amount), new MaterialStack(byProduct, byProductAmount));
    }

    /// Transitional: accepts the legacy material types through [MU#toMaterial] until every caller passes a
    /// [Material] directly.
    public ItemData(IOreMaterial material, long amount, MaterialStack... byProducts) {
        this(new MaterialStack(material, amount), byProducts);
    }

    public ItemData(IOreMaterial material, long amount, IOreMaterial byProduct, long byProductAmount) {
        this(new MaterialStack(material, amount), new MaterialStack(byProduct, byProductAmount));
    }

    public ItemData(ItemData... data) {
        mPrefix = null;
        mBlackListed = true;

        ArrayList<MaterialStack> list = new ArrayList<>(), rList = new ArrayList<>();

        for (ItemData tData : data) if (tData != null) {
            if (tData.hasValidMaterialData() && tData.mMaterial.mAmount > 0) list.add(tData.mMaterial.clone());
            for (MaterialStack tMaterial : tData.mByProducts) if (tMaterial.mAmount > 0) list.add(tMaterial.clone());
        }

        for (MaterialStack material : list) {
            boolean temp = true;
            for (MaterialStack tMaterial : rList) if (material.mMaterial == tMaterial.mMaterial) {
                tMaterial.mAmount += material.mAmount;
                temp = false;
                break;
            }
            if (temp) rList.add(material.clone());
        }

        rList.sort((a, b) -> Long.compare(b.mAmount, a.mAmount));

        if (rList.isEmpty()) {
            mMaterial = null;
        } else {
            mMaterial = rList.get(0);
            rList.remove(0);
        }

        mByProducts = rList.toArray(new MaterialStack[0]);
        oreDictName = null;
    }

    public final boolean hasValidPrefixMaterialData() {
        return mPrefix != null && mMaterial != null && mMaterial.mMaterial != null;
    }

    public final boolean hasValidPrefixData() {
        return mPrefix != null;
    }

    public final boolean hasValidMaterialData() {
        return mMaterial != null && mMaterial.mMaterial != null;
    }

    public final ArrayList<MaterialStack> getAllMaterialStacks() {
        ArrayList<MaterialStack> rList = new ArrayList<>();
        if (hasValidMaterialData()) rList.add(mMaterial);
        rList.addAll(Arrays.asList(mByProducts));
        return rList;
    }

    public final MaterialStack getByProduct(int index) {
        return index >= 0 && index < mByProducts.length ? mByProducts[index] : null;
    }

    @Override
    public String toString() {
        if (oreDictName != null) return oreDictName;
        if (mPrefix == null || mMaterial == null || mMaterial.mMaterial == null) return "";
        return mPrefix.getName() + MU.internalName(mMaterial.mMaterial);
    }
}
