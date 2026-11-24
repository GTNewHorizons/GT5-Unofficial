package gregtech.api.ec.component.multiblock;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.component.IUniqueComponent;

public interface IRecipeEfficiencyLogicComponent extends IMultiblockComponent, IUniqueComponent {

    ComponentKey.UniqueKey<IRecipeEfficiencyLogicComponent> KEY = ComponentKey.unique();

    int getEfficiency();

    int getBaseMaxEfficiency();

    int getEffectiveMaxEfficiency();

    IRecipeEfficiencyLogicComponent setEfficiency(int efficiency);

    IRecipeEfficiencyLogicComponent setEfficiencyDecrease(int efficiencyDecrease);

    IRecipeEfficiencyLogicComponent setEfficiencyIncrease(int efficiencyIncrease);
}
