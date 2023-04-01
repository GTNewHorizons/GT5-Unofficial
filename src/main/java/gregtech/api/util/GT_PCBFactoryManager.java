package gregtech.api.util;

import com.google.common.collect.HashBiMap;
import gregtech.api.enums.Materials;

public class GT_PCBFactoryManager {

    private static final HashBiMap<Materials, Integer> mPlasticTiers = HashBiMap.create();
    public static int mTiersOfPlastics = 0;

    public static void addPlasticTier(Materials aMaterial, int aTier) {
        mPlasticTiers.put(aMaterial, aTier);
        mTiersOfPlastics++;
    }

    public static int getPlasticTier(Materials aMaterial) {
        return mPlasticTiers.get(aMaterial);
    }

    public static Materials getPlasticMaterialFromTier(int aTier) {
        return mPlasticTiers.inverse()
                            .get(aTier);
    }
}
