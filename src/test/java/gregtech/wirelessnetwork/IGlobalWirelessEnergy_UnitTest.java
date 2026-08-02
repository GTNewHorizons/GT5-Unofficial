package gregtech.wirelessnetwork;

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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.gtnewhorizon.gtnhlib.teams.Team;
import com.gtnewhorizon.gtnhlib.teams.TeamDataRegistry;
import com.gtnewhorizon.gtnhlib.teams.TeamDataTransferReason;
import com.gtnewhorizon.gtnhlib.teams.TeamManager;

import gregtech.common.misc.WirelessTeamData;

class IGlobalWirelessEnergy_UnitTest implements GlobalWirelessTestBase {

    private static final BigInteger MINUS_ONE = BigInteger.valueOf(-1L);
    private static final BigInteger MINUS_TWO = BigInteger.valueOf(-2L);
    private static final BigInteger MINUS_TEN = BigInteger.valueOf(-10L);

    @BeforeAll
    static void registerWirelessTeamData() {
        if (!TeamDataRegistry.getRegisteredKeys()
            .contains(WirelessTeamData.DATA_KEY))
            TeamDataRegistry.register(WirelessTeamData.DATA_KEY, WirelessTeamData::new);
    }

    @AfterEach
    @Override
    public void clearData() {
        // super clearData gets called by Junit afterward
        clearGlobalEnergyInformationMaps();
    }

    @Override
    @MethodSource("randomUserUUID1")
    @ParameterizedTest
    public void testAddToDataSuccessfully(UUID user_uuid) {
        TeamManager.createTeam("UnitTest", user_uuid);

        addEUToGlobalEnergyMap(user_uuid, BigInteger.ONE);
        assertEquals(getUserEU(user_uuid), BigInteger.ONE, MESSAGE);

        addEUToGlobalEnergyMap(user_uuid, 1);
        assertEquals(getUserEU(user_uuid), BigInteger.TWO, MESSAGE);

        addEUToGlobalEnergyMap(user_uuid, 1L);
        assertEquals(getUserEU(user_uuid), BigInteger.valueOf(3L), MESSAGE);
    }

    @Override
    @MethodSource("randomUserUUID1")
    @ParameterizedTest
    public void testAddToDataFailure(UUID user_uuid) {
        TeamManager.createTeam("UnitTest", user_uuid);

        assertFalse(addEUToGlobalEnergyMap(user_uuid, MINUS_ONE));
        assertEquals(getUserEU(user_uuid), BigInteger.ZERO, MESSAGE);

        assertTrue(addEUToGlobalEnergyMap(user_uuid, BigInteger.ONE));
        assertEquals(getUserEU(user_uuid), BigInteger.ONE, MESSAGE);

        assertFalse(addEUToGlobalEnergyMap(user_uuid, MINUS_TWO));
        assertEquals(getUserEU(user_uuid), BigInteger.ONE, MESSAGE);

        assertTrue(addEUToGlobalEnergyMap(user_uuid, BigInteger.ONE));
        assertEquals(getUserEU(user_uuid), BigInteger.TWO, MESSAGE);
    }

    @Override
    @MethodSource("randomUserUUID1")
    @ParameterizedTest
    public void testRemoveFromDataSuccessfully(final UUID user_uuid) {
        TeamManager.createTeam("UnitTest", user_uuid);

        assertTrue(addEUToGlobalEnergyMap(user_uuid, BigInteger.TEN));
        assertEquals(getUserEU(user_uuid), BigInteger.TEN, MESSAGE);

        assertTrue(addEUToGlobalEnergyMap(user_uuid, MINUS_ONE));
        assertEquals(getUserEU(user_uuid), BigInteger.valueOf(9L), MESSAGE);

        assertTrue(addEUToGlobalEnergyMap(user_uuid, MINUS_TWO));
        assertEquals(getUserEU(user_uuid), BigInteger.valueOf(7L), MESSAGE);
    }

    @Override
    @MethodSource("randomUserUUID1")
    @ParameterizedTest
    public void testRemoveFromDataFailure(final UUID user_uuid) {
        TeamManager.createTeam("UnitTest", user_uuid);

        assertTrue(addEUToGlobalEnergyMap(user_uuid, BigInteger.ONE));
        assertEquals(getUserEU(user_uuid), BigInteger.ONE, MESSAGE);

        assertFalse(addEUToGlobalEnergyMap(user_uuid, MINUS_TWO));
        assertEquals(getUserEU(user_uuid), BigInteger.ONE, MESSAGE);

        assertTrue(addEUToGlobalEnergyMap(user_uuid, BigInteger.valueOf(4)));
        assertEquals(getUserEU(user_uuid), BigInteger.valueOf(5), MESSAGE);

        assertFalse(addEUToGlobalEnergyMap(user_uuid, MINUS_TEN));
        assertEquals(getUserEU(user_uuid), BigInteger.valueOf(5L), MESSAGE);
    }

    @MethodSource("randomUserUUID1")
    @ParameterizedTest
    void IGlobalWirelessEnergy_StrongCheckOrAddUser(final UUID user_uuid) {
        TeamManager.createTeam("UnitTest", user_uuid);

        strongCheckOrAddUser(user_uuid);
        assertEquals(getUserEU(user_uuid), BigInteger.ZERO, MESSAGE);
    }

    @Override
    @MethodSource("randomUserUUID4")
    @ParameterizedTest
    public void testTeamChange(final UUID... user_uuids) {
        assertTrue(user_uuids.length >= 3, "At least 3 users are required for this test");

        UUID user_uuid_0 = user_uuids[0];
        UUID user_uuid_1 = user_uuids[1];
        UUID user_uuid_2 = user_uuids[2];
        UUID user_uuid_3 = user_uuids[3];
        Team unit_team_0 = TeamManager.createTeam("UnitTest0", user_uuid_0);
        Team unit_team_1 = TeamManager.createTeam("UnitTest1", user_uuid_1);
        Team unit_team_2 = TeamManager.createTeam("UnitTest2", user_uuid_2);
        Team unit_team_3 = TeamManager.createTeam("UnitTest3", user_uuid_3);

        strongCheckOrAddUser(user_uuid_0);
        strongCheckOrAddUser(user_uuid_1);
        strongCheckOrAddUser(user_uuid_2);

        assertEquals(getUserEU(user_uuid_0), BigInteger.ZERO, MESSAGE);
        assertEquals(getUserEU(user_uuid_1), BigInteger.ZERO, MESSAGE);
        assertEquals(getUserEU(user_uuid_2), BigInteger.ZERO, MESSAGE);

        TeamManager.mergeTeams(unit_team_1, unit_team_0);
        TeamManager.mergeTeams(unit_team_1, unit_team_2);

        assertEquals(TeamManager.getTeamByPlayer(user_uuid_0), unit_team_1, MESSAGE);
        assertEquals(TeamManager.getTeamByPlayer(user_uuid_2), unit_team_1, MESSAGE);

        assertTrue(addEUToGlobalEnergyMap(user_uuid_0, BigInteger.ONE));
        assertTrue(addEUToGlobalEnergyMap(user_uuid_2, BigInteger.ONE));

        assertEquals(getUserEU(user_uuid_0), BigInteger.TWO, MESSAGE);
        assertEquals(getUserEU(user_uuid_1), BigInteger.TWO, MESSAGE);
        assertEquals(getUserEU(user_uuid_2), BigInteger.TWO, MESSAGE);

        assertTrue(addEUToGlobalEnergyMap(user_uuid_0, BigInteger.valueOf(-1L)));

        assertEquals(getUserEU(user_uuid_0), BigInteger.ONE, MESSAGE);
        assertEquals(getUserEU(user_uuid_1), BigInteger.ONE, MESSAGE);
        assertEquals(getUserEU(user_uuid_2), BigInteger.ONE, MESSAGE);

        assertFalse(addEUToGlobalEnergyMap(user_uuid_0, BigInteger.valueOf(-2L)));

        assertEquals(getUserEU(user_uuid_0), BigInteger.ONE, MESSAGE);
        assertEquals(getUserEU(user_uuid_1), BigInteger.ONE, MESSAGE);
        assertEquals(getUserEU(user_uuid_2), BigInteger.ONE, MESSAGE);

        // recreating this step of the unit test would probably void the team & it's data
        // SpaceProjectManager.putInTeam(user_uuid_1, user_uuid_1);

        unit_team_1.removeMember(user_uuid_0);
        unit_team_0 = TeamManager.createTeam("UnitTest0", user_uuid_0);
        TeamManager.transferTeamData(unit_team_1, unit_team_0, user_uuid_0, TeamDataTransferReason.JoinedNewTeam);

        unit_team_1.removeMember(user_uuid_2);
        unit_team_2 = TeamManager.createTeam("UnitTest2", user_uuid_2);
        TeamManager.transferTeamData(unit_team_1, unit_team_2, user_uuid_2, TeamDataTransferReason.JoinedNewTeam);

        assertEquals(getUserEU(user_uuid_0), BigInteger.ZERO, MESSAGE);
        assertEquals(getUserEU(user_uuid_1), BigInteger.ONE, MESSAGE);
        assertEquals(getUserEU(user_uuid_2), BigInteger.ZERO, MESSAGE);

        testTeamChangePost(unit_team_3.getTeamId(), user_uuids);
    }
}
