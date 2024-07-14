package gregtech.common.misc;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.UUID;

import gregtech.common.WirelessComputationPacket;

public abstract class GlobalVariableStorage {
    // --------------------- NEVER access these maps! Use the methods provided! ---------------------

    // Global EU map.
    public static HashMap<UUID, BigInteger> GlobalEnergy = new HashMap<>(100, 0.9f);

    // Global Wireless Data map
    public static HashMap<UUID, WirelessComputationPacket> GlobalWirelessComputation = new HashMap<>(100, 0.9f);

    // ----------------------------------------------------------------------------------------------
}
