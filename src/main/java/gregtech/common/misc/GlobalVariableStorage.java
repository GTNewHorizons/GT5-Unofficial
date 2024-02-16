package gregtech.common.misc;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.UUID;

public abstract class GlobalVariableStorage {
    // --------------------- NEVER access these maps! Use the methods provided! ---------------------

    // Global EU map.
    public static HashMap<UUID, BigInteger> GlobalEnergy = new HashMap<>(100, 0.9f);

    // ----------------------------------------------------------------------------------------------

}
