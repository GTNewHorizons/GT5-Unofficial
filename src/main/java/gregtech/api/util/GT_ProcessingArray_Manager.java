package gregtech.api.util;

import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

import java.util.HashMap;

public class GT_ProcessingArray_Manager {

    private static final HashMap<Integer, String> mMetaKeyMap = new HashMap<Integer, String>();
    private static final HashMap<String, GT_Recipe_Map> mRecipeCache = new HashMap<String, GT_Recipe_Map>();

    public static boolean registerRecipeMapForMeta(int aMeta, GT_Recipe_Map aMap) {
        if (aMeta < 0 || aMeta > Short.MAX_VALUE || aMap == null) {
            return false;
        }
        if (mMetaKeyMap.containsKey(aMeta)) {
            return false;
        }
        String aMapNameKey = aMap.mUnlocalizedName;
        mMetaKeyMap.put(aMeta, aMapNameKey);
        if (!mRecipeCache.containsKey(aMapNameKey)) {
            mRecipeCache.put(aMapNameKey, aMap);
        }
        return true;
    }

    public static GT_Recipe_Map getRecipeMapForMeta(int aMeta) {
        return mRecipeCache.get(mMetaKeyMap.get(aMeta));
    }

}
