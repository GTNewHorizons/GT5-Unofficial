package gregtech.api.interfaces.tileentity;

/**
 * IC2 Energy Compat
 */
public interface IIC2Enet {

    /**
     * Should this tile/block join the ic2 enet
     */
    boolean shouldJoinIc2Enet();

    /**
     * Update the ic2 enet
     */
    void doEnetUpdate();
}
