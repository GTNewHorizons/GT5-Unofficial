package gregtech.api.enums;

import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

public class MaterialsIDMap extends Object2IntOpenHashMap<Materials> {

    MaterialsIDMap() {}

    public void register() {
        // spotless:off
        r(32, Materials.Iron);
        // spotless:on

        this.forEach((a, b) -> a.mMetaItemSubID = b);
    }

    void r(int ID, Materials material) {
        this.put(material, ID);
    }
}
