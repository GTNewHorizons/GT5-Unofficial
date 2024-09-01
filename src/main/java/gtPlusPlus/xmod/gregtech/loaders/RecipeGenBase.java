package gtPlusPlus.xmod.gregtech.loaders;

import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.core.material.Material;

public abstract class RecipeGenBase implements RunnableWithInfo<Material> {

    protected Material toGenerate;
    protected boolean disableOptional;

    @Override
    public Material getInfoData() {
        return toGenerate;
    }
}
