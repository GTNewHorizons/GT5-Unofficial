package gregtech.nei.dumper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;

public class MaterialDumper extends GregTechIDDumper {

    public MaterialDumper() {
        super("material");
    }

    @Override
    public String[] header() {
        return new String[] { "id", "name", };
    }

    @Override
    protected Iterable<String[]> dump(Mode mode) {
        List<String[]> dump = new ArrayList<>();
        Map<Integer, Materials> idMap = Arrays.stream(GregTechAPI.sGeneratedMaterials)
            .filter(Objects::nonNull)
            .collect(Collectors.toMap(m -> m.mMetaItemSubID, m -> m));
        for (int i = 0; i < 1000; i++) {
            if (mode == Mode.FREE && !idMap.containsKey(i)) {
                dump.add(new String[] { String.valueOf(i), "", });
            } else if (mode == Mode.USED && idMap.containsKey(i)) {
                dump.add(new String[] { String.valueOf(i), idMap.get(i).mName, });
            }
        }
        return dump;
    }
}
