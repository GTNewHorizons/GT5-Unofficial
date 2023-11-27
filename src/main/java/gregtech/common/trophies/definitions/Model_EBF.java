package gregtech.common.trophies.definitions;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;

import org.apache.commons.lang3.tuple.Pair;

import glowredman.amazingtrophies.model.complex.BaseModelStructure;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;

public class Model_EBF extends BaseModelStructure {

    public Model_EBF(int coilMeta) {
        charToBlock.put('t', Pair.of(GregTech_API.sBlockCasings1, 11));
        charToBlock.put('C', Pair.of(ItemList.Casing_Coil_Eternal.getBlock(), coilMeta));
        charToBlock.put('~', Pair.of(GregTech_API.sBlockMachines, 1000));

        reverseInnerArrays(structure);
        processStructureMap();
    }

    @Override
    public String[][] getStructureString() {
        return structure;
    }

    private final String[][] structure = transpose(
        new String[][] { { "ttt", "ttt", "ttt" }, { "CCC", "C C", "CCC" }, { "CCC", "C C", "CCC" },
            { "t~t", "ttt", "ttt" } });

}
