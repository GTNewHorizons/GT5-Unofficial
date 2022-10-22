package gregtech.globalenergymap;

import gregtech.api.interfaces.IGlobalWirelessEnergy;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.*;

class IGlobalWirelessEnergy_UnitTest implements IGlobalWirelessEnergy {

    static final String message = "Comparison failed";

    @Test
    void IGlobalWirelessEnergy_AddingEU() {

        String test_id = "TEST_ID";

        IGlobalWirelessEnergy.super.addEUToGlobalEnergyMap(test_id, new BigInteger("1"));
        assertEquals(GlobalEnergy.get(test_id), new BigInteger("1"), message);

        IGlobalWirelessEnergy.super.addEUToGlobalEnergyMap(test_id, 1);
        assertEquals(GlobalEnergy.get(test_id), new BigInteger("2"), message);

        IGlobalWirelessEnergy.super.addEUToGlobalEnergyMap(test_id, 1L);
        assertEquals(GlobalEnergy.get(test_id), new BigInteger("3"), message);

        IGlobalWirelessEnergy.super.ClearMaps();
    }

    @Test
    void IGlobalWirelessEnergy_NoNegativeEU() {

        String user_uuid = "TEST_ID";
        String user_user = "TEST";

        IGlobalWirelessEnergy.super.StrongCheckOrAddUser(user_uuid, user_user);

        assertFalse(IGlobalWirelessEnergy.super.addEUToGlobalEnergyMap(user_uuid, new BigInteger("-1")));
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid), BigInteger.ZERO, message);

        assertTrue(IGlobalWirelessEnergy.super.addEUToGlobalEnergyMap(user_uuid, new BigInteger("1")));
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid), BigInteger.ONE, message);

        assertFalse(IGlobalWirelessEnergy.super.addEUToGlobalEnergyMap(user_uuid, new BigInteger("-2")));
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid), BigInteger.ONE, message);

        assertTrue(IGlobalWirelessEnergy.super.addEUToGlobalEnergyMap(user_uuid, new BigInteger("1")));
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid), BigInteger.valueOf(2L), message);

        assertTrue(IGlobalWirelessEnergy.super.addEUToGlobalEnergyMap(user_uuid, new BigInteger("-2")));
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid), BigInteger.ZERO, message);

        IGlobalWirelessEnergy.super.ClearMaps();
    }

    @Test
    void IGlobalWirelessEnergy_StrongCheckOrAddUser() {
        String user_uuid = "12345";
        String user_name = "Colen";

        IGlobalWirelessEnergy.super.StrongCheckOrAddUser(user_uuid, user_name);
        assertEquals(GlobalEnergy.get(user_uuid), BigInteger.ZERO, message);
        assertEquals(GlobalEnergyTeam.get(user_uuid), user_uuid, message);
        assertEquals(GlobalEnergyName.get(user_uuid), user_name, message);

        IGlobalWirelessEnergy.super.ClearMaps();
    }

    @Test
    void IGlobalWirelessEnergy_NameChange() {


        String user_uuid = "12345";
        String user_name_0 = "Colen";

        IGlobalWirelessEnergy.super.StrongCheckOrAddUser(user_uuid, user_name_0);
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid), BigInteger.ZERO, message);
        assertEquals(GlobalEnergyTeam.get(user_uuid), user_uuid, message);
        assertEquals(GlobalEnergyName.get(user_uuid), user_name_0, message);
        assertEquals(GlobalEnergyName.get(user_name_0), user_uuid, message);

        String user_name_1 = "Steve";
        IGlobalWirelessEnergy.super.StrongCheckOrAddUser(user_uuid, user_name_1);
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid), BigInteger.ZERO, message);
        assertEquals(GlobalEnergyTeam.get(user_uuid), user_uuid, message);
        assertEquals(GlobalEnergyName.get(user_uuid), user_name_1, message);
        assertEquals(GlobalEnergyName.get(user_name_1), user_uuid, message);

        IGlobalWirelessEnergy.super.StrongCheckOrAddUser(user_uuid, user_name_0);
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid), BigInteger.ZERO, message);
        assertEquals(GlobalEnergyTeam.get(user_uuid), user_uuid, message);
        assertEquals(GlobalEnergyName.get(user_uuid), user_name_0, message);
        assertEquals(GlobalEnergyName.get(user_name_0), user_uuid, message);

        IGlobalWirelessEnergy.super.ClearMaps();
    }

    @Test
    void IGlobalWirelessEnergy_TeamChange() {

        String user_uuid_0 = "12345";
        String user_name_0 = "Colen";

        String user_uuid_1 = "54321";
        String user_name_1 = "Steve";

        String user_uuid_2 = "12321";
        String user_name_2 = "Sarah";

        IGlobalWirelessEnergy.super.StrongCheckOrAddUser(user_uuid_0, user_name_0);
        IGlobalWirelessEnergy.super.StrongCheckOrAddUser(user_uuid_1, user_name_1);
        IGlobalWirelessEnergy.super.StrongCheckOrAddUser(user_uuid_2, user_name_2);

        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid_0), BigInteger.ZERO, message);
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid_1), BigInteger.ZERO, message);
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid_2), BigInteger.ZERO, message);

        IGlobalWirelessEnergy.super.JoinUserNetwork(user_uuid_0, user_uuid_1);
        IGlobalWirelessEnergy.super.JoinUserNetwork(user_uuid_2, user_uuid_1);

        assertEquals(GlobalEnergyTeam.get(user_uuid_0), user_uuid_1, message);
        assertEquals(GlobalEnergyTeam.get(user_uuid_2), user_uuid_1, message);

        assertTrue(IGlobalWirelessEnergy.super.addEUToGlobalEnergyMap(user_uuid_0, BigInteger.ONE));
        assertTrue(IGlobalWirelessEnergy.super.addEUToGlobalEnergyMap(user_uuid_2, BigInteger.ONE));

        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid_0), BigInteger.valueOf(2L), message);
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid_1), BigInteger.valueOf(2L), message);
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid_2), BigInteger.valueOf(2L), message);

        assertTrue(IGlobalWirelessEnergy.super.addEUToGlobalEnergyMap(user_uuid_0, BigInteger.valueOf(-1L)));

        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid_0), BigInteger.ONE, message);
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid_1), BigInteger.ONE, message);
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid_2), BigInteger.ONE, message);

        assertFalse(IGlobalWirelessEnergy.super.addEUToGlobalEnergyMap(user_uuid_0, BigInteger.valueOf(-2L)));

        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid_0), BigInteger.ONE, message);
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid_1), BigInteger.ONE, message);
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid_2), BigInteger.ONE, message);

        IGlobalWirelessEnergy.super.StrongCheckOrAddUser(user_uuid_0, user_name_0);
        IGlobalWirelessEnergy.super.StrongCheckOrAddUser(user_uuid_1, user_name_1);
        IGlobalWirelessEnergy.super.StrongCheckOrAddUser(user_uuid_2, user_name_2);

        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid_0), BigInteger.ONE, message);
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid_1), BigInteger.ONE, message);
        assertEquals(IGlobalWirelessEnergy.super.GetUserEU(user_uuid_2), BigInteger.ONE, message);

        IGlobalWirelessEnergy.super.ClearMaps();
    }

    @Test
    void IGlobalWirelessEnergy_UUID() {
        String user_uuid_0 = "12345";
        String user_name_0 = "Colen";

        String user_uuid_1 = "54321";
        String user_name_1 = "Steve";

        IGlobalWirelessEnergy.super.StrongCheckOrAddUser(user_uuid_0, user_name_0);
        IGlobalWirelessEnergy.super.StrongCheckOrAddUser(user_uuid_1, user_name_1);

        assertEquals(IGlobalWirelessEnergy.super.GetUsernameFromUUID(user_uuid_0), user_name_0, message);
        assertEquals(IGlobalWirelessEnergy.super.GetUsernameFromUUID(user_uuid_1), user_name_1, message);

        IGlobalWirelessEnergy.super.JoinUserNetwork(user_uuid_0, user_uuid_1);

        assertEquals(IGlobalWirelessEnergy.super.GetUsernameFromUUID(user_uuid_0), user_name_1, message);
        assertEquals(IGlobalWirelessEnergy.super.GetUsernameFromUUID(user_uuid_1), user_name_1, message);

        IGlobalWirelessEnergy.super.ClearMaps();
    }
}
