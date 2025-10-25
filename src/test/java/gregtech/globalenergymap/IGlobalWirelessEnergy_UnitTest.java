package gregtech.globalenergymap;

import static gregtech.common.misc.GlobalVariableStorage.GlobalEnergy;
import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.clearGlobalEnergyInformationMaps;
import static gregtech.common.misc.WirelessNetworkManager.getUserEU;
import static gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.UUID;

import org.junit.jupiter.api.Test;

import gregtech.common.misc.GlobalEnergyWorldSavedData;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;

class IGlobalWirelessEnergy_UnitTest {

    static final String message = "Comparison failed";

    @Test
    void IGlobalWirelessEnergy_AddingEU() {

        UUID test_id = UUID.randomUUID();

        addEUToGlobalEnergyMap(test_id, new BigInteger("1"));
        assertEquals(GlobalEnergy.get(test_id), new BigInteger("1"), message);

        addEUToGlobalEnergyMap(test_id, 1);
        assertEquals(GlobalEnergy.get(test_id), new BigInteger("2"), message);

        addEUToGlobalEnergyMap(test_id, 1L);
        assertEquals(GlobalEnergy.get(test_id), new BigInteger("3"), message);

        clearGlobalEnergyInformationMaps();
    }

    @Test
    void IGlobalWirelessEnergy_NoNegativeEU() {

        UUID user_uuid = UUID.randomUUID();

        strongCheckOrAddUser(user_uuid);

        assertFalse(addEUToGlobalEnergyMap(user_uuid, new BigInteger("-1")));
        assertEquals(getUserEU(user_uuid), BigInteger.ZERO, message);

        assertTrue(addEUToGlobalEnergyMap(user_uuid, new BigInteger("1")));
        assertEquals(getUserEU(user_uuid), BigInteger.ONE, message);

        assertFalse(addEUToGlobalEnergyMap(user_uuid, new BigInteger("-2")));
        assertEquals(getUserEU(user_uuid), BigInteger.ONE, message);

        assertTrue(addEUToGlobalEnergyMap(user_uuid, new BigInteger("1")));
        assertEquals(getUserEU(user_uuid), BigInteger.valueOf(2L), message);

        assertTrue(addEUToGlobalEnergyMap(user_uuid, new BigInteger("-2")));
        assertEquals(getUserEU(user_uuid), BigInteger.ZERO, message);

        clearGlobalEnergyInformationMaps();
    }

    @Test
    void IGlobalWirelessEnergy_StrongCheckOrAddUser() {
        UUID user_uuid = UUID.randomUUID();

        strongCheckOrAddUser(user_uuid);
        assertEquals(GlobalEnergy.get(user_uuid), BigInteger.ZERO, message);

        clearGlobalEnergyInformationMaps();
    }

    @Test
    void IGlobalWirelessEnergy_TeamChange() {

        GlobalEnergyWorldSavedData.INSTANCE = new GlobalEnergyWorldSavedData();

        UUID user_uuid_0 = UUID.randomUUID();

        UUID user_uuid_1 = UUID.randomUUID();

        UUID user_uuid_2 = UUID.randomUUID();

        strongCheckOrAddUser(user_uuid_0);
        strongCheckOrAddUser(user_uuid_1);
        strongCheckOrAddUser(user_uuid_2);

        assertEquals(getUserEU(user_uuid_0), BigInteger.ZERO, message);
        assertEquals(getUserEU(user_uuid_1), BigInteger.ZERO, message);
        assertEquals(getUserEU(user_uuid_2), BigInteger.ZERO, message);

        SpaceProjectManager.putInTeam(user_uuid_0, user_uuid_1);
        SpaceProjectManager.putInTeam(user_uuid_2, user_uuid_1);

        assertEquals(SpaceProjectManager.getLeader(user_uuid_0), user_uuid_1, message);
        assertEquals(SpaceProjectManager.getLeader(user_uuid_2), user_uuid_1, message);

        assertTrue(addEUToGlobalEnergyMap(user_uuid_0, BigInteger.ONE));
        assertTrue(addEUToGlobalEnergyMap(user_uuid_2, BigInteger.ONE));

        assertEquals(getUserEU(user_uuid_0), BigInteger.valueOf(2L), message);
        assertEquals(getUserEU(user_uuid_1), BigInteger.valueOf(2L), message);
        assertEquals(getUserEU(user_uuid_2), BigInteger.valueOf(2L), message);

        assertTrue(addEUToGlobalEnergyMap(user_uuid_0, BigInteger.valueOf(-1L)));

        assertEquals(getUserEU(user_uuid_0), BigInteger.ONE, message);
        assertEquals(getUserEU(user_uuid_1), BigInteger.ONE, message);
        assertEquals(getUserEU(user_uuid_2), BigInteger.ONE, message);

        assertFalse(addEUToGlobalEnergyMap(user_uuid_0, BigInteger.valueOf(-2L)));

        assertEquals(getUserEU(user_uuid_0), BigInteger.ONE, message);
        assertEquals(getUserEU(user_uuid_1), BigInteger.ONE, message);
        assertEquals(getUserEU(user_uuid_2), BigInteger.ONE, message);

        SpaceProjectManager.putInTeam(user_uuid_0, user_uuid_0);
        SpaceProjectManager.putInTeam(user_uuid_1, user_uuid_1);
        SpaceProjectManager.putInTeam(user_uuid_2, user_uuid_2);

        assertEquals(getUserEU(user_uuid_0), BigInteger.ZERO, message);
        assertEquals(getUserEU(user_uuid_1), BigInteger.ONE, message);
        assertEquals(getUserEU(user_uuid_2), BigInteger.ZERO, message);

        clearGlobalEnergyInformationMaps();
    }
}
