package gregtech.api.interfaces.tileentity;

/**
 * IC2 Energy Compat
 */
public interface IIC2Enet {

    /**
     * Should this tile/block join the ic2 e-net?
     */
    boolean shouldJoinIc2Enet();

    /**
     * Update the ic2 e-net
     */
    void doEnetUpdate();
}
