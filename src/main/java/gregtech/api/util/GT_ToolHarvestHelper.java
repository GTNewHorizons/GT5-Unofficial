package gregtech.api.util;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public class GT_ToolHarvestHelper {

    public static boolean isAppropriateTool(Block aBlock, byte aMetaData, String... tTools) {

        if (aBlock == null || tTools == null) {
            return false;
        }
        String targetTool = aBlock.getHarvestTool(aMetaData);
        return !isStringEmpty(targetTool) && isArrayContains(targetTool, tTools);
    }

    public static boolean isAppropriateMaterial(Block aBlock, Material... tMats) {
        if (aBlock == null || tMats == null) {
            return false;
        }
        return isArrayContains(aBlock.getMaterial(), tMats);
    }

    public static boolean isSpecialBlock(Block aBlock, Block... tBlocks) {
        if (aBlock == null || tBlocks == null) {
            return false;
        }
        return isArrayContains(aBlock, tBlocks);
    }

    public static <T> boolean isArrayContains(T obj, T[] list) {

        if (obj == null || list == null) {
            return false;
        }

        for (T iObj : list) {
            if (obj == iObj || obj.equals(iObj)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isStringEmpty(String s) {
        return s == null || s.length() == 0;
    }

    public static boolean hasNull(Object... obj) {
        for (Object iObj : obj) {
            if (iObj == null) {
                return true;
            }
        }
        return false;
    }
}
