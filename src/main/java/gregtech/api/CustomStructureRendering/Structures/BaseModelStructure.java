package gregtech.api.CustomStructureRendering.Structures;

import gregtech.api.CustomStructureRendering.Base.Util.RenderFacesInfo;
import net.minecraft.block.Block;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public abstract class BaseModelStructure {

    public final int getXLength() {
        return getStructureString().length;
    }

    public final int getYLength() {
        return getStructureString()[0][0].length();
    }

    public final int getZLength() {
        return getStructureString()[0].length;
    }

    public String[][] getStructureString() {
        return null;
    }

    public final float maxAxisSize() {
        return Math.max(getXLength(), Math.max(getYLength(), getZLength()));
    }

    public final BlockInfo getAssociatedBlockInfo(final char letter) {
        return charToBlock.get(letter);
    }

    protected HashMap<Character, BlockInfo> charToBlock = new HashMap<>();

    public static class BlockInfo {

        public final int metadata;
        public final Block block;

        BlockInfo(Block block, int metadata) {
            this.block = block;
            this.metadata = metadata;
        }
    }

    protected static void reverseInnerArrays(String[][] array) {

        for (String[] innerArray : array) {
            int start = 0;
            int end = innerArray.length - 1;

            while (start < end) {
                String temp = innerArray[start];
                innerArray[start] = innerArray[end];
                innerArray[end] = temp;

                start++;
                end--;
            }
        }
    }

    private static String[][] deepCopy(String[][] original) {
        if (original == null) {
            return null;
        }

        final String[][] result = new String[original.length][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }
        return result;
    }

    protected void processStructureMap() {

        String[][] structureCopy = deepCopy(getStructureString());
        HashSet<Character> transparentBlocks = getTransparentBlocks();

        // These will be replaced with air, so that blocks behind
        // them are rendered as normal.
        removeTransparentBlocks(structureCopy, transparentBlocks);

        transparentStructure = structureCopy;

        generateRenderFacesInfo(structureCopy);
    }

    public RenderFacesInfo[][][] renderFacesArray;

    private void generateRenderFacesInfo(String[][] structureCopy) {

        renderFacesArray = new RenderFacesInfo[getXLength()][getZLength()][getYLength()];

        for (int x = 0; x < getXLength(); x++) {
            for (int y = 0; y < getYLength(); y++) {
                for (int z = 0; z < getZLength(); z++) {

                    RenderFacesInfo renderFacesInfo = new RenderFacesInfo(true);

                    // yNeg Face
                    char yNegBlock = ' ';
                    if (z != 0) {
                        yNegBlock = structureCopy[x][z-1].charAt(y);
                    }

                    if (yNegBlock != ' ') renderFacesInfo.yNeg = false;


                    // yPos Face
                    char yPosBlock = ' ';
                    if (z != getZLength() - 1) {
                        yPosBlock = structureCopy[x][z+1].charAt(y);
                    }

                    if (yPosBlock != ' ') renderFacesInfo.yPos = false;





                    // xNeg Face
                    char xNegBlock = ' ';
                    if (y != 0) {
                        xNegBlock = structureCopy[x][z].charAt(y-1);
                    }

                    if (xNegBlock != ' ') renderFacesInfo.zNeg = false;


                    // xPos Face
                    char xPosBlock = ' ';
                    if (y != getYLength() - 1) {
                        xPosBlock = structureCopy[x][z].charAt(y+1);
                    }

                    if (xPosBlock != ' ') renderFacesInfo.zPos = false;




                    // zNeg Face
                    char zNegBlock = ' ';
                    if (x != 0) {
                        zNegBlock = structureCopy[x-1][z].charAt(y);
                    }

                    if (zNegBlock != ' ') renderFacesInfo.xNeg = false;


                    // zPos Face
                    char zPosBlock = ' ';
                    if (x != getXLength() - 1) {
                        zPosBlock = structureCopy[x+1][z].charAt(y);
                    }

                    if (zPosBlock != ' ') renderFacesInfo.xPos = false;



                    renderFacesArray[x][z][y] = renderFacesInfo;

                }
            }
        }


    }

    String[][] transparentStructure;


    private void removeTransparentBlocks(String[][] structure, HashSet<Character> transparentBlocks) {
        if (structure == null || transparentBlocks == null) {
            return;  // Nothing to do if either of them is null
        }

        for (int i = 0; i < structure.length; i++) {
            for (int j = 0; j < structure[i].length; j++) {
                StringBuilder newStr = new StringBuilder();

                // Check each character in the string
                for (char c : structure[i][j].toCharArray()) {
                    if (!transparentBlocks.contains(c)) {
                        // If the character is not in the transparentBlocks, append it to the new string.
                        newStr.append(c);
                    } else {
                        // Otherwise air block.
                        newStr.append(' ');
                    }
                }

                // Update the string in the structure.
                structure[i][j] = newStr.toString();
            }
        }
    }

    private HashSet<Character> getTransparentBlocks() {
        HashSet<Character> transparentBlocks = new HashSet<>();

        // Iterate over all blocks to find transparent ones.
        for (Map.Entry<Character, BlockInfo> entry : charToBlock.entrySet()) {

            Block block = entry.getValue().block;

            // Block cannot be seen through.
            if (!block.isOpaqueCube()) {
                transparentBlocks.add(entry.getKey());
            }
        }

        return transparentBlocks;
    }
}
