package gregtech.globalenergymap;

import static gregtech.common.misc.GlobalVariableStorage.GlobalEnergy;
import static gregtech.common.misc.GlobalVariableStorage.GlobalEnergyName;
import static gregtech.common.misc.GlobalVariableStorage.GlobalEnergyTeam;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;

import org.junit.jupiter.api.Test;

import gregtech.api.interfaces.IGlobalWirelessEnergy;

class IGlobalWirelessEnergy_UnitTest implements IGlobalWirelessEnergy {

    static final String message = "Comparison failed";

    @Test
    void IGlobalWirelessEnergy_AddingEU() {

        String test_id = "TEST_ID";

        addEUToGlobalEnergyMap(test_id, new BigInteger("1"));
        assertEquals(GlobalEnergy.get(test_id), new BigInteger("1"), message);

        addEUToGlobalEnergyMap(test_id, 1);
        assertEquals(GlobalEnergy.get(test_id), new BigInteger("2"), message);

        addEUToGlobalEnergyMap(test_id, 1L);
        assertEquals(GlobalEnergy.get(test_id), new BigInteger("3"), message);

        IGlobalWirelessEnergy.clearGlobalEnergyInformationMaps();
    }

    @Test
    void IGlobalWirelessEnergy_NoNegativeEU() {

        String user_uuid = "TEST_ID";
        String user_user = "TEST";

        strongCheckOrAddUser(user_uuid, user_user);

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

        IGlobalWirelessEnergy.clearGlobalEnergyInformationMaps();
    }

    @Test
    void IGlobalWirelessEnergy_StrongCheckOrAddUser() {
        String user_uuid = "12345";
        String user_name = "Colen";

        strongCheckOrAddUser(user_uuid, user_name);
        assertEquals(GlobalEnergy.get(user_uuid), BigInteger.ZERO, message);
        assertEquals(GlobalEnergyTeam.get(user_uuid), user_uuid, message);
        assertEquals(GlobalEnergyName.get(user_uuid), user_name, message);

        IGlobalWirelessEnergy.clearGlobalEnergyInformationMaps();
    }

    @Test
    void IGlobalWirelessEnergy_NameChange() {

        String user_uuid = "12345";
        String user_name_0 = "Colen";

        strongCheckOrAddUser(user_uuid, user_name_0);
        assertEquals(getUserEU(user_uuid), BigInteger.ZERO, message);
        assertEquals(GlobalEnergyTeam.get(user_uuid), user_uuid, message);
        assertEquals(GlobalEnergyName.get(user_uuid), user_name_0, message);
        assertEquals(GlobalEnergyName.get(user_name_0), user_uuid, message);

        String user_name_1 = "Steve";
        strongCheckOrAddUser(user_uuid, user_name_1);
        assertEquals(getUserEU(user_uuid), BigInteger.ZERO, message);
        assertEquals(GlobalEnergyTeam.get(user_uuid), user_uuid, message);
        assertEquals(GlobalEnergyName.get(user_uuid), user_name_1, message);
        assertEquals(GlobalEnergyName.get(user_name_1), user_uuid, message);

        strongCheckOrAddUser(user_uuid, user_name_0);
        assertEquals(getUserEU(user_uuid), BigInteger.ZERO, message);
        assertEquals(GlobalEnergyTeam.get(user_uuid), user_uuid, message);
        assertEquals(GlobalEnergyName.get(user_uuid), user_name_0, message);
        assertEquals(GlobalEnergyName.get(user_name_0), user_uuid, message);

        IGlobalWirelessEnergy.clearGlobalEnergyInformationMaps();
    }

    @Test
    void IGlobalWirelessEnergy_TeamChange() {

        String user_uuid_0 = "12345";
        String user_name_0 = "Colen";

        String user_uuid_1 = "54321";
        String user_name_1 = "Steve";

        String user_uuid_2 = "12321";
        String user_name_2 = "Sarah";

        strongCheckOrAddUser(user_uuid_0, user_name_0);
        strongCheckOrAddUser(user_uuid_1, user_name_1);
        strongCheckOrAddUser(user_uuid_2, user_name_2);

        assertEquals(getUserEU(user_uuid_0), BigInteger.ZERO, message);
        assertEquals(getUserEU(user_uuid_1), BigInteger.ZERO, message);
        assertEquals(getUserEU(user_uuid_2), BigInteger.ZERO, message);

        joinUserNetwork(user_uuid_0, user_uuid_1);
        joinUserNetwork(user_uuid_2, user_uuid_1);

        assertEquals(GlobalEnergyTeam.get(user_uuid_0), user_uuid_1, message);
        assertEquals(GlobalEnergyTeam.get(user_uuid_2), user_uuid_1, message);

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

        strongCheckOrAddUser(user_uuid_0, user_name_0);
        strongCheckOrAddUser(user_uuid_1, user_name_1);
        strongCheckOrAddUser(user_uuid_2, user_name_2);

        assertEquals(getUserEU(user_uuid_0), BigInteger.ONE, message);
        assertEquals(getUserEU(user_uuid_1), BigInteger.ONE, message);
        assertEquals(getUserEU(user_uuid_2), BigInteger.ONE, message);

        IGlobalWirelessEnergy.clearGlobalEnergyInformationMaps();
    }

    @Test
    void IGlobalWirelessEnergy_UUID() {
        String user_uuid_0 = "12345";
        String user_name_0 = "Colen";

        String user_uuid_1 = "54321";
        String user_name_1 = "Steve";

        strongCheckOrAddUser(user_uuid_0, user_name_0);
        strongCheckOrAddUser(user_uuid_1, user_name_1);

        assertEquals(GetUsernameFromUUID(user_uuid_0), user_name_0, message);
        assertEquals(GetUsernameFromUUID(user_uuid_1), user_name_1, message);

        joinUserNetwork(user_uuid_0, user_uuid_1);

        assertEquals(GetUsernameFromUUID(user_uuid_0), user_name_1, message);
        assertEquals(GetUsernameFromUUID(user_uuid_1), user_name_1, message);

        IGlobalWirelessEnergy.clearGlobalEnergyInformationMaps();
    }
}
