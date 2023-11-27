package gregtech.common.trophies.definitions;

import static gregtech.api.enums.Mods.IndustrialCraft2;

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.common.registry.GameRegistry;
import glowredman.amazingtrophies.model.complex.BaseModelStructure;
import gregtech.api.GregTech_API;

public class Model_Cleanroom extends BaseModelStructure {

    public Model_Cleanroom() {
        charToBlock.put('C', Pair.of(GregTech_API.sBlockReinforced, 2));
        charToBlock.put('A', Pair.of(GameRegistry.findBlock(IndustrialCraft2.ID, "blockAlloyGlass"), 0));
        charToBlock.put('D', Pair.of(GregTech_API.sBlockMachines, 4127));
        charToBlock.put('B', Pair.of(GregTech_API.sBlockCasings3, 11));

        charToBlock.put('E', Pair.of(GregTech_API.sBlockMachines, 1182));
        charToBlock.put('~', Pair.of(GregTech_API.sBlockMachines, 1172));

        reverseInnerArrays(structure);
        processStructureMap();
    }

    @Override
    public String[][] getStructureString() {
        return structure;
    }

    private final String[][] structure = new String[][] { { "CCCCC", "CAAAC", "CAAAC", "CAAAC", "CCCCC", "D   D" },
        { "CBBBC", "C   C", "C   C", "C   C", "CCCCC", "     " },
        { "CB~BC", "C   C", "C   C", "C   C", "CCCCC", "     " },
        { "CBBBC", "C   C", "C   C", "C E C", "CCCCC", "     " },
        { "CCCCC", "CCCCC", "CCCCC", "CCCCC", "CCCCC", "D   D" } };
}
