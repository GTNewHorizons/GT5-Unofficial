package gregtech.api.logic.interfaces;

import gregtech.api.logic.ModelRenderLogic;

public interface ModelRenderLogicHost {

    ModelRenderLogic getRenderLogic();

    boolean shouldRenderModel();
}
