package gregtech.api.util;

import java.util.HashMap;

import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

public class GT_ProcessingArray_Manager {

    private static final HashMap<String, GT_Recipe_Map> mRecipeSaves = new HashMap<String, GT_Recipe_Map>();

    // Adds recipe Maps to the PA using the machines unlocalized name.
    // Example: basicmachine.electrolyzer, with its recipe map will add the electrolyzer's recipe map to the PA
    public static void addRecipeMapToPA(String aMachineName, GT_Recipe_Map aMap) {
        if (aMachineName != null) {
            mRecipeSaves.put(aMachineName, aMap);
        }
    }

    // Allows the PA to extract the recipe map for the machine inside it.
    public static GT_Recipe_Map giveRecipeMap(String aMachineName) {
        if (aMachineName != null) {
            return mRecipeSaves.get(aMachineName);
        }
        return null;
    }
}
