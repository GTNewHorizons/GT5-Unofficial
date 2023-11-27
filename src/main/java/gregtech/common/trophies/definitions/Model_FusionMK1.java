package gregtech.common.trophies.definitions;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;

import org.apache.commons.lang3.tuple.Pair;

import glowredman.amazingtrophies.model.complex.BaseModelStructure;
import gregtech.api.GregTech_API;

public class Model_FusionMK1 extends BaseModelStructure {

    public Model_FusionMK1() {
        charToBlock.put('i', Pair.of(GregTech_API.sBlockCasings1, 6));
        charToBlock.put('h', Pair.of(GregTech_API.sBlockCasings1, 6));
        charToBlock.put('e', Pair.of(GregTech_API.sBlockCasings1, 6));
        charToBlock.put('x', Pair.of(GregTech_API.sBlockCasings1, 6));
        charToBlock.put('c', Pair.of(GregTech_API.sBlockCasings1, 15));
        charToBlock.put('~', Pair.of(GregTech_API.sBlockMachines, 1193));

        reverseInnerArrays(structure);
        processStructureMap();
    }

    @Override
    public String[][] getStructureString() {
        return structure;
    }

    private final String[][] structure = transpose(
        new String[][] {
            { "               ", "      ihi      ", "    hh   hh    ", "   h       h   ", "  h         h  ",
                "  h         h  ", " i           i ", " h           h ", " i           i ", "  h         h  ",
                "  h         h  ", "   h       h   ", "    hh   hh    ", "      ihi      ", "               ", },
            { "      xhx      ", "    hhccchh    ", "   eccxhxcce   ", "  eceh   hece  ", " hce       ech ",
                " hch       hch ", "xcx         xcx", "hch         hch", "xcx         xcx", " hch       hch ",
                " hce       ech ", "  eceh   hece  ", "   eccx~xcce   ", "    hhccchh    ", "      xhx      ", },
            { "               ", "      ihi      ", "    hh   hh    ", "   h       h   ", "  h         h  ",
                "  h         h  ", " i           i ", " h           h ", " i           i ", "  h         h  ",
                "  h         h  ", "   h       h   ", "    hh   hh    ", "      ihi      ", "               ", } });

}
