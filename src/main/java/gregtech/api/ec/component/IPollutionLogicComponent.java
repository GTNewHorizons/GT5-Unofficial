package gregtech.api.ec.component;

import gregtech.api.ec.ComponentKey;

public interface IPollutionLogicComponent extends IUniqueComponent {

    ComponentKey.UniqueKey<IPollutionLogicComponent> KEY = ComponentKey.unique();

    int getAveragePollutionPercentage();

    boolean polluteEnvironment(int aPollutionLevel);
}
