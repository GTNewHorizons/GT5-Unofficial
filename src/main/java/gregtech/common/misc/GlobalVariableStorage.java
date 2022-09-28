package gregtech.common.misc;

import java.math.BigInteger;
import java.util.HashMap;

public abstract class GlobalVariableStorage {
    // --------------------- NEVER access these maps! Use the methods provided! ---------------------

    // Global EU map.
    public static HashMap<String, BigInteger> GlobalEnergy = new HashMap<>(100, 0.9f);

    // Maps user IDs to usernames and vice versa. Seems redundant but this makes accessing this
    // easier in certain locations (like gt commands).
    public static HashMap<String, String> GlobalEnergyName = new HashMap<>(100, 0.9f);

    // Maps UUIDs to other UUIDs. This allows users to join a team.
    public static HashMap<String, String> GlobalEnergyTeam = new HashMap<>(100, 0.9f);

    // ----------------------------------------------------------------------------------------------

}
