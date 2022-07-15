// package goodgenerator.items.nuclear;
//
// import gregtech.api.enums.Textures;
// import gregtech.api.interfaces.IIconContainer;
//
// public class NuclearTextures {
//    public static final NuclearTextures
//            STABLE1 = new NuclearTextures("stable1"),
//            STABLE2 = new NuclearTextures("stable2"),
//            UNSTABLE1 = new NuclearTextures("unstable1"),
//            UNSTABLE2 = new NuclearTextures("unstable2"),
//            UNSTABLE3 = new NuclearTextures("unstable3"),
//            UNSTABLE4 = new NuclearTextures("unstable4");
//
//    public final IIconContainer[] mTextures = new IIconContainer[4];
//    public final String mSetName;
//    public static final String mTextureDir = "icons/isotope/";
//    public static final int
//            FULL = 0,
//            TINY = 1,
//            FULL_OXIDE = 2,
//            TINY_OXIDE = 3;
//
//    public NuclearTextures(String aName) {
//        mSetName = aName;
//        mTextures[0] = new Textures.ItemIcons.CustomIcon(mTextureDir + aName);
//        mTextures[1] = new Textures.ItemIcons.CustomIcon(mTextureDir + aName + "tiny");
//        mTextures[2] = new Textures.ItemIcons.CustomIcon(mTextureDir + aName);
//        mTextures[3] = new Textures.ItemIcons.CustomIcon(mTextureDir + aName + "tiny");
//    }
// }
