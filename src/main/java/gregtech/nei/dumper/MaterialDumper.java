package gregtech.nei.dumper;

import java.util.ArrayList;
import java.util.List;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.Materials;
import gregtech.api.material.MU;

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
            Material material = MU.byId(i);
            Materials legacy = material == null ? null : MU.materialOf(material);
            // A slot whose material lost its parent mod counts as free, reproducing the mHasParentMod gate
            // of Materials.fillGeneratedMaterialsMap.
            boolean used = legacy != null && legacy.mHasParentMod;
            if (mode == Mode.FREE && !used) {
                dump.add(new String[] { String.valueOf(i), "", });
            } else if (mode == Mode.USED && used) {
                dump.add(new String[] { String.valueOf(i), MU.legacyName(material), });
            }
        }
        return dump;
    }
}
