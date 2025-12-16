package gregtech.api.interfaces;

import java.util.List;

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gtPlusPlus.core.material.Material;

/**
 * A temporary material interface to unify the three material systems.
 * Once the new material system is finished, this should be removed and all the code referencing this should be migrated
 * to the new system.
 */
public interface IOreMaterial {

    String getLocalizedName();

    int getId();

    String getInternalName();

    short[] getRGBA();

    TextureSet getTextureSet();

    List<IStoneType> getValidStones();

    public static IOreMaterial findMaterial(String name) {
        Werkstoff bw = Werkstoff.werkstoffVarNameHashMap.get(name);

        if (bw != null) return bw;

        IOreMaterial gtpp = Material.mMaterialsByName.get(name);

        if (gtpp != null) return gtpp;

        return Materials.getMaterialsMap()
            .get(name);
    }
}
