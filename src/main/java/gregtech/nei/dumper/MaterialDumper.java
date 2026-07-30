package gregtech.nei.dumper;

import java.util.ArrayList;
import java.util.List;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.materials.LegacyMaterialIDIndex;
import gregtech.api.enums.materials.MaterialParentMods;
import gregtech.api.material.MaterialUtils;

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
        for (int i = 0; i < 1000; i++) {
            Material material = LegacyMaterialIDIndex.get(i);
            // A slot whose material has no available parent mod never generates an item, so it counts as free
            // rather than used.
            boolean used = material != null && MaterialParentMods.hasParentMod(material);
            if (mode == Mode.FREE && !used) {
                dump.add(new String[] { String.valueOf(i), "", });
            } else if (mode == Mode.USED && used) {
                dump.add(new String[] { String.valueOf(i), MaterialUtils.internalName(material), });
            }
        }
        return dump;
    }
}
