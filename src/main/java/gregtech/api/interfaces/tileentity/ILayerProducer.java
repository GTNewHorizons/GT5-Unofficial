package gregtech.api.interfaces.tileentity;

import java.util.List;

import gregtech.common.tileentities.machines.MTELayerSignal;

/*
 * An interface to use Layer Signal Hatch
 */
public interface ILayerProducer {

    /**
     * Returns an immutable list of all Layer Signal Hatches
     */
    List<MTELayerSignal> getLayerSignalHatches();

    boolean addLayerSignalHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex);

}
