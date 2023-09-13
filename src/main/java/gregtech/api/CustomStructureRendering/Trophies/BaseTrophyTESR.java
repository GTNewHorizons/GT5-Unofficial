package gregtech.api.CustomStructureRendering.Trophies;


import gregtech.api.CustomStructureRendering.Base.BaseRenderTESR;
import gregtech.api.CustomStructureRendering.Structures.BaseModelStructure;

public class BaseTrophyTESR extends BaseRenderTESR {

    @Override
    protected BaseModelStructure getModel(String modelName) {
        return Trophies.getModel(modelName);
    }

}
