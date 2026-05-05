package gregtech.api.interfaces.tileentity;

import java.util.List;

import gregtech.common.tileentities.machines.MTEHeatSensor;

/*
 * An interface to use HeatSensor Hatch
 */
public interface IHeatProducer {

    /**
     * Returns an immutable list of all Heat Sensor Hatches
     */
    List<MTEHeatSensor> getHeatSensorHatches();

    boolean addHeatSensorHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex);

}
