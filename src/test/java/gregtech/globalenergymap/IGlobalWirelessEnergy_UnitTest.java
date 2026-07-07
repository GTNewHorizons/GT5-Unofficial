package gregtech.globalenergymap;

import static gregtech.common.misc.WirelessNetworkManager.addEUToGlobalEnergyMap;
import static gregtech.common.misc.WirelessNetworkManager.clearGlobalEnergyInformationMaps;
import static gregtech.common.misc.WirelessNetworkManager.getUserEU;
import static gregtech.common.misc.WirelessNetworkManager.strongCheckOrAddUser;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigInteger;
import java.util.UUID;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import com.gtnewhorizon.gtnhlib.teams.Team;
import com.gtnewhorizon.gtnhlib.teams.TeamDataRegistry;
import com.gtnewhorizon.gtnhlib.teams.TeamManager;

import gregtech.common.misc.WirelessEnergyTeamData;

class IGlobalWirelessEnergy_UnitTest {

    static final String message = "Comparison failed";

    @BeforeAll
    static void registerWirelessTeamData() {
        TeamDataRegistry.register(WirelessEnergyTeamData.DATA_KEY, WirelessEnergyTeamData::new);
    }

    @AfterEach
    void clearData() {
        // technically unneeded as all teams get cleared afterward
        clearGlobalEnergyInformationMaps();
        TeamManager.clear();
    }

    @Test
    void IGlobalWirelessEnergy_AddingEU() {
        UUID user_uuid = UUID.randomUUID();
        TeamManager.createTeam("UnitTest", user_uuid);

        addEUToGlobalEnergyMap(user_uuid, BigInteger.ONE);
        assertEquals(getUserEU(user_uuid), BigInteger.ONE, message);

        addEUToGlobalEnergyMap(user_uuid, 1);
        assertEquals(getUserEU(user_uuid), BigInteger.TWO, message);

        addEUToGlobalEnergyMap(user_uuid, 1L);
        assertEquals(getUserEU(user_uuid), BigInteger.valueOf(3L), message);
    }

    @Test
    void IGlobalWirelessEnergy_NoNegativeEU() {

        UUID user_uuid = UUID.randomUUID();
        TeamManager.createTeam("UnitTest", user_uuid);

        strongCheckOrAddUser(user_uuid);

        assertFalse(addEUToGlobalEnergyMap(user_uuid, new BigInteger("-1")));
        assertEquals(getUserEU(user_uuid), BigInteger.ZERO, message);

        assertTrue(addEUToGlobalEnergyMap(user_uuid, BigInteger.ONE));
        assertEquals(getUserEU(user_uuid), BigInteger.ONE, message);

        assertFalse(addEUToGlobalEnergyMap(user_uuid, new BigInteger("-2")));
        assertEquals(getUserEU(user_uuid), BigInteger.ONE, message);

        assertTrue(addEUToGlobalEnergyMap(user_uuid, BigInteger.ONE));
        assertEquals(getUserEU(user_uuid), BigInteger.TWO, message);

        assertTrue(addEUToGlobalEnergyMap(user_uuid, new BigInteger("-2")));
        assertEquals(getUserEU(user_uuid), BigInteger.ZERO, message);
    }

    @Test
    void IGlobalWirelessEnergy_StrongCheckOrAddUser() {
        UUID user_uuid = UUID.randomUUID();
        TeamManager.createTeam("UnitTest", user_uuid);

        strongCheckOrAddUser(user_uuid);
        assertEquals(getUserEU(user_uuid), BigInteger.ZERO, message);
    }

    @Test
    void IGlobalWirelessEnergy_TeamChange() {

        UUID user_uuid_0 = UUID.randomUUID();
        Team unit_team_0 = TeamManager.createTeam("UnitTest0", user_uuid_0);

        UUID user_uuid_1 = UUID.randomUUID();
        Team unit_team_1 = TeamManager.createTeam("UnitTest1", user_uuid_1);

        UUID user_uuid_2 = UUID.randomUUID();
        Team unit_team_2 = TeamManager.createTeam("UnitTest2", user_uuid_2);

        strongCheckOrAddUser(user_uuid_0);
        strongCheckOrAddUser(user_uuid_1);
        strongCheckOrAddUser(user_uuid_2);

        assertEquals(getUserEU(user_uuid_0), BigInteger.ZERO, message);
        assertEquals(getUserEU(user_uuid_1), BigInteger.ZERO, message);
        assertEquals(getUserEU(user_uuid_2), BigInteger.ZERO, message);

        // SpaceProjectManager.putInTeam(user_uuid_0, user_uuid_1);
        // SpaceProjectManager.putInTeam(user_uuid_2, user_uuid_1);
        TeamManager.mergeTeams(unit_team_1, unit_team_0);
        TeamManager.mergeTeams(unit_team_1, unit_team_2);

        // assertEquals(SpaceProjectManager.getLeader(user_uuid_0), user_uuid_1, message);
        // assertEquals(SpaceProjectManager.getLeader(user_uuid_2), user_uuid_1, message);
        assertEquals(TeamManager.getTeamByPlayer(user_uuid_0), unit_team_1, message);
        assertEquals(TeamManager.getTeamByPlayer(user_uuid_2), unit_team_1, message);

        assertTrue(addEUToGlobalEnergyMap(user_uuid_0, BigInteger.ONE));
        assertTrue(addEUToGlobalEnergyMap(user_uuid_2, BigInteger.ONE));

        assertEquals(getUserEU(user_uuid_0), BigInteger.TWO, message);
        assertEquals(getUserEU(user_uuid_1), BigInteger.TWO, message);
        assertEquals(getUserEU(user_uuid_2), BigInteger.TWO, message);

        assertTrue(addEUToGlobalEnergyMap(user_uuid_0, BigInteger.valueOf(-1L)));

        assertEquals(getUserEU(user_uuid_0), BigInteger.ONE, message);
        assertEquals(getUserEU(user_uuid_1), BigInteger.ONE, message);
        assertEquals(getUserEU(user_uuid_2), BigInteger.ONE, message);

        assertFalse(addEUToGlobalEnergyMap(user_uuid_0, BigInteger.valueOf(-2L)));

        assertEquals(getUserEU(user_uuid_0), BigInteger.ONE, message);
        assertEquals(getUserEU(user_uuid_1), BigInteger.ONE, message);
        assertEquals(getUserEU(user_uuid_2), BigInteger.ONE, message);

        // SpaceProjectManager.putInTeam(user_uuid_0, user_uuid_0);
        // SpaceProjectManager.putInTeam(user_uuid_2, user_uuid_2);

        // recreating this step of the unit test would probably void the team & it's data
        // SpaceProjectManager.putInTeam(user_uuid_1, user_uuid_1);

        unit_team_1.removeMember(user_uuid_0);
        TeamManager.createTeam("UnitTest0", user_uuid_0);
        unit_team_1.removeMember(user_uuid_2);
        TeamManager.createTeam("UnitTest2", user_uuid_2);

        assertEquals(getUserEU(user_uuid_0), BigInteger.ZERO, message);
        assertEquals(getUserEU(user_uuid_1), BigInteger.ONE, message);
        assertEquals(getUserEU(user_uuid_2), BigInteger.ZERO, message);
    }
}
