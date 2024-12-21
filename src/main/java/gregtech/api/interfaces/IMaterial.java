package gregtech.api.interfaces;

import com.google.common.collect.ImmutableList;

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.Materials;
import gtPlusPlus.core.material.Material;

public interface IMaterial {

    public String getLocalizedName();

    public int getId();

    public String getInternalName();

    public short[] getRGBA();

    public boolean isValidForStone(IStoneType stoneType);

    public ImmutableList<IStoneType> getValidStones();

    public static IMaterial findMaterial(String name) {
        Werkstoff bw = Werkstoff.werkstoffVarNameHashMap.get(name);

        if (bw != null) return bw;

        IMaterial gtpp = Material.mMaterialsByName.get(name);

        if (gtpp != null) return gtpp;

        return Materials.getMaterialsMap()
            .get(name);
    }
}
