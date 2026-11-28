package gregtech.api.ec.component;

import gregtech.api.ec.ComponentKey;

public interface IEnergyInputLogicComponent extends IUniqueComponent {

    ComponentKey.UniqueKey<IEnergyInputLogicComponent> KEY = ComponentKey.unique();

    long getMaxInputVoltage();

    long getAverageInputVoltage();

    long getMaxInputAmps();

    long getMaxInputEu();

    /**
     * Sums up max input EU/t of energy hatches, amperage included,
     * but excludes exotic hatches.
     */
    long getMaxInputPower();

    long getInputVoltageTier();

    boolean drainEnergyInput(long aEU);
}
