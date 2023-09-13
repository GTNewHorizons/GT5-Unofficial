package gregtech.api.CustomStructureRendering.Structures;

import net.minecraft.init.Blocks;

public class Model_Default extends BaseModelStructure {

    public Model_Default() {
        charToBlock.put('x', new BlockInfo(Blocks.coal_block, 0));
        processStructureMap();
    }

    @Override
    public String[][] getStructureString() {
        return structure;
    }


    private final String[][] structure = new String[][] {
            { "x" }
    };

}
