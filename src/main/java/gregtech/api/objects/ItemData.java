package gregtech.api.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MaterialUtils;

/// What material an [ItemStack] is made of and how much of it, for any item in the game.
///
/// This is GregTech's cross-mod material index, and it is not scaffolding awaiting a MaterialLib replacement.
/// A [com.ruling_0.materiallib.api.Shape] knows only the items MaterialLib itself generated; this answers for
/// a foreign mod's items too, which is what lets [gregtech.api.util.GTRecipeRegistrator] derive recycling for
/// them. The arc-furnace recipe turning a TConstruct metal block into nine nether bricks exists because that
/// block picked up an association here.
///
/// Keyed by [OrePrefixes] rather than by shape for the same reason: the association is created from an
/// ore-dictionary *name*, and a tenth of the prefixes named here have no shape at all. [#mMaterial]'s amount
/// comes from [OrePrefixes#getMaterialAmount], which for a prefix a shape serves is the shape's own value; see
/// [OrePrefixes]'s class javadoc for how the two spaces divide.
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

    /// An ingredient that names an ore-dictionary entry and carries no composition, for the entries whose
    /// material side is not a material at all.
    public ItemData(OrePrefixes prefix, String materialName) {
        this(prefix, materialName, false);
    }

    /// An ingredient that names an ore-dictionary entry and carries no composition, blacklisted from unification
    /// the way [#ItemData(OrePrefixes,Material,boolean)] blacklists a material-backed one.
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
        return mPrefix.getName() + MaterialUtils.internalName(mMaterial.mMaterial);
    }
}
