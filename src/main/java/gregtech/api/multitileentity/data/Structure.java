package gregtech.api.multitileentity.data;

import com.badlogic.ashley.core.Component;

import gregtech.api.multitileentity.StructureHandler;

public class Structure implements Component {

    private Class<? extends StructureHandler> handlerClass;

    public Structure(Class<? extends StructureHandler> handlerClass) {
        this.handlerClass = handlerClass;
    }

    public Class<? extends StructureHandler> getHandlerClass() {
        return handlerClass;
    }
}
