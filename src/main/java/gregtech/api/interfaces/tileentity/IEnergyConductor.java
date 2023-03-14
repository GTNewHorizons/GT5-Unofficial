package gregtech.api.interfaces.tileentity;

import gregtech.api.enums.Materials;

/**
 * Informative Class for Cables. Not used for now.
 * <p/>
 * Not all Data might be reliable. This is just for Information sake.
 */
public interface IEnergyConductor extends IEnergyConnected, IHasWorldObjectAndCoords {

    /**
     * @return if this is actually a Cable. (you must check this)
     */
    boolean isConductor();

    /**
     * @return the maximum Voltage of the Cable.
     */
    long getMaxVoltage();

    /**
     * @return the maximum Amperage of the Cable, per Wire.
     */
    long getMaxAmperage();

    /**
     * @return the Loss of the Cable, per Meter.
     */
    long getLossPerMeter();

    /**
     * @return the Material the Cable consists of. (may return Materials._NULL)
     */
    Materials getCableMaterial();

    /**
     * @return the Material the Cable Insulation consists of. (may return Materials._NULL)
     */
    Materials getInsulationMaterial();
}
