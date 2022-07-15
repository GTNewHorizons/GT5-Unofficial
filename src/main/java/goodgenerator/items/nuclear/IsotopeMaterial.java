// package goodgenerator.items.nuclear;
//
// import goodgenerator.loader.Loaders;
// import net.minecraft.item.ItemStack;
//
// import java.util.HashMap;
// import java.util.HashSet;
//
// public class IsotopeMaterial {
//
//    public static final HashSet<IsotopeMaterial> mIsotopeMaterial = new HashSet<>();
//    public static final HashMap<Integer, IsotopeMaterial> mIDMap = new HashMap<>();
//    public static final HashMap<String, IsotopeMaterial> mNameMap = new HashMap<>();
//
//    public final int mID;
//    public final int mNeutron;
//    public final String mName;
//    public final String mLocalizedName;
//    public final String mMaterialName;
//    public final short[] mRGB;
//    public final short[] mRGBO;
//    public final NuclearTextures mTexture;
//
//    public IsotopeMaterial(int aID, String aName, String aMaterialName, String aLocalizedName, NuclearTextures
// aTexture, int aR, int aG, int aB, int aNeutron) {
//        if (mIDMap.get(aID) != null)
//            throw new UnsupportedOperationException("ID:" + aID + " is already used!");
//        this.mID = aID;
//        this.mNeutron = aNeutron;
//        this.mName = aName;
//        this.mMaterialName = aMaterialName;
//        this.mLocalizedName = aLocalizedName;
//        this.mRGB = new short[] {(short) (aR * 0.6), (short) (aG * 0.6), (short) (aB * 0.6), 0};
//        this.mRGBO = new short[] {(short) aR, (short) aG, (short) aB, 0};
//        this.mTexture = aTexture;
//        mIsotopeMaterial.add(this);
//        mIDMap.put(this.mID, this);
//        mNameMap.put(this.mName, this);
//    }
//
//    public ItemStack getFull(int aAmount) {
//        if (aAmount > 64) aAmount = 64;
//        return new ItemStack(Loaders.Isotope, aAmount, mID + 1000);
//    }
//
//    public ItemStack getTiny(int aAmount) {
//        if (aAmount > 64) aAmount = 64;
//        return new ItemStack(Loaders.Isotope, aAmount, mID + 2000);
//    }
//
//    public ItemStack getFullOxide(int aAmount) {
//        if (aAmount > 64) aAmount = 64;
//        return new ItemStack(Loaders.Isotope, aAmount, mID + 3000);
//    }
//
//    public ItemStack getTinyOxide(int aAmount) {
//        if (aAmount > 64) aAmount = 64;
//        return new ItemStack(Loaders.Isotope, aAmount, mID + 4000);
//    }
// }
