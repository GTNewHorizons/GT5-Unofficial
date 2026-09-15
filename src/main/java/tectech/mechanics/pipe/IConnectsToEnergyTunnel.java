package tectech.mechanics.pipe;

import net.minecraftforge.common.util.ForgeDirection;

/**
 * Created by Tec on 26.02.2017.
 */
public interface IConnectsToEnergyTunnel {

    boolean canConnect(ForgeDirection side);
}
