package gregtech.api.interfaces;

import net.minecraftforge.common.util.ForgeDirection;

public interface IKineticProducer {

    /**
     * Get the current KU output of the producer
     *
     * @return KU out amount
     */
    long currentKU();

    /**
     * Get the direction the producer outputs it's current KU.
     * Used by generators to match their input to the producer's output
     *
     * @return Side KU is outputted
     */
    ForgeDirection getKUOutSide();
}
