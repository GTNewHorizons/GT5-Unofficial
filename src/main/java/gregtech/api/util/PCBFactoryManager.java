package gregtech.api.util;

import com.google.common.collect.HashBiMap;
import com.ruling_0.materiallib.api.Material;

public class PCBFactoryManager {

    private static final HashBiMap<Material, Integer> mPlasticTiers = HashBiMap.create();
    public static int mTiersOfPlastics = 0;

    public static void addPlasticTier(Material aMaterial, int aTier) {
        mPlasticTiers.put(aMaterial, aTier);
        mTiersOfPlastics++;
    }

    public static int getPlasticTier(Material aMaterial) {
        return mPlasticTiers.get(aMaterial);
    }

    public static Material getPlasticMaterialFromTier(int aTier) {
        return mPlasticTiers.inverse()
            .get(aTier);
    }
}
