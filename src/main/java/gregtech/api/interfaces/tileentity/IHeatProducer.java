package gregtech.api.interfaces.tileentity;

/*
 * An interface to use HeatSensor Hatch
 */
public interface IHeatProducer {

    int getHeatSensorHatchNum();

    boolean addHeatSensorHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex);

}
