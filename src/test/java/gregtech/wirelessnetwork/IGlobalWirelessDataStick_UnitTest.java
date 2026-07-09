package gregtech.wirelessnetwork;

import static gregtech.common.misc.WirelessNetworkManager.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import com.gtnewhorizon.gtnhlib.teams.Team;
import com.gtnewhorizon.gtnhlib.teams.TeamDataTransferReason;
import com.gtnewhorizon.gtnhlib.teams.TeamManager;

import gregtech.api.util.GTRecipe;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;

class IGlobalWirelessDataStick_UnitTest implements GlobalWirelessTestBase {

    private static boolean initialized = false;
    private static GTRecipe.RecipeAssemblyLine FAKE_1;
    private static GTRecipe.RecipeAssemblyLine FAKE_2;
    private static GTRecipe.RecipeAssemblyLine FAKE_3;
    private static GTRecipe.RecipeAssemblyLine FAKE_4;

    @BeforeAll
    public static void beforeAll() {
        if (!initialized) {
            FAKE_1 = mock(GTRecipe.RecipeAssemblyLine.class);
            FAKE_2 = mock(GTRecipe.RecipeAssemblyLine.class);
            FAKE_3 = mock(GTRecipe.RecipeAssemblyLine.class);
            FAKE_4 = mock(GTRecipe.RecipeAssemblyLine.class);
            initialized = true;
        }
    }

    @Override
    public void clearData() {
        // super clearData gets called by Junit afterward
        clearWirelessDataSticks();
    }

    @Override
    @MethodSource("randomUserUUID1")
    @ParameterizedTest
    public void testAddToDataSuccessfully(UUID user_uuid) {
        TeamManager.createTeam("UnitTest", user_uuid);

        int COORD_0 = 0;

        assertFalse(hasDirtyDataSticks(user_uuid), MESSAGE);

        uploadDataSticks(COORD_0, new ALRecipeDataPacket(new GTRecipe.RecipeAssemblyLine[] { FAKE_1 }), user_uuid);

        assertTrue(hasDirtyDataSticks(user_uuid), MESSAGE);
        assertEquals(1, accessCache(user_uuid).size(), MESSAGE);
        assertTrue(accessCache(user_uuid).contains(FAKE_1), MESSAGE);

        forceSetDirtySticks(false, user_uuid);

        assertFalse(hasDirtyDataSticks(user_uuid), MESSAGE);

        uploadDataSticks(
            COORD_0,
            new ALRecipeDataPacket(new GTRecipe.RecipeAssemblyLine[] { FAKE_1, FAKE_2 }),
            user_uuid);

        assertTrue(hasDirtyDataSticks(user_uuid), MESSAGE);
        assertEquals(2, accessCache(user_uuid).size(), MESSAGE);
        assertTrue(accessCache(user_uuid).contains(FAKE_1), MESSAGE);
        assertTrue(accessCache(user_uuid).contains(FAKE_2), MESSAGE);
    }

    @Override
    @MethodSource("randomUserUUID1")
    @ParameterizedTest
    public void testAddToDataFailure(UUID user_uuid) {
        TeamManager.createTeam("UnitTest", user_uuid);

        int COORD_0 = 0;
        int COORD_1 = 1;

        assertFalse(hasDirtyDataSticks(user_uuid), MESSAGE);

        uploadDataSticks(COORD_0, new ALRecipeDataPacket(new GTRecipe.RecipeAssemblyLine[] { FAKE_1 }), user_uuid);

        assertTrue(hasDirtyDataSticks(user_uuid), MESSAGE);
        assertEquals(1, accessCache(user_uuid).size(), MESSAGE);
        assertTrue(accessCache(user_uuid).contains(FAKE_1), MESSAGE);

        uploadDataSticks(COORD_1, new ALRecipeDataPacket(new GTRecipe.RecipeAssemblyLine[] { FAKE_1 }), user_uuid);

        assertTrue(hasDirtyDataSticks(user_uuid), MESSAGE);
        assertEquals(1, accessCache(user_uuid).size(), MESSAGE);
        assertTrue(accessCache(user_uuid).contains(FAKE_1), MESSAGE);
    }

    @Override
    @MethodSource("randomUserUUID1")
    @ParameterizedTest
    public void testRemoveFromDataSuccessfully(UUID user_uuid) {
        TeamManager.createTeam("UnitTest", user_uuid);

        assertFalse(hasDirtyDataSticks(user_uuid), MESSAGE);

        uploadDataSticks(0, new ALRecipeDataPacket(new GTRecipe.RecipeAssemblyLine[] { FAKE_1 }), user_uuid);

        assertTrue(hasDirtyDataSticks(user_uuid), MESSAGE);
        assertEquals(1, accessCache(user_uuid).size(), MESSAGE);
        assertTrue(accessCache(user_uuid).contains(FAKE_1), MESSAGE);

        forceSetDirtySticks(false, user_uuid);

        uploadDataSticks(0, null, user_uuid);

        assertTrue(hasDirtyDataSticks(user_uuid), MESSAGE);
        assertEquals(0, accessCache(user_uuid).size(), MESSAGE);
        assertFalse(accessCache(user_uuid).contains(FAKE_1), MESSAGE);
    }

    @Override
    @MethodSource("randomUserUUID1")
    @ParameterizedTest
    public void testRemoveFromDataFailure(UUID user_uuid) {

        TeamManager.createTeam("UnitTest", user_uuid);
        int COORD_0 = 0;
        int COORD_1 = 1;

        assertFalse(hasDirtyDataSticks(user_uuid), MESSAGE);

        uploadDataSticks(
            COORD_0,
            new ALRecipeDataPacket(new GTRecipe.RecipeAssemblyLine[] { FAKE_1, FAKE_3, }),
            user_uuid);

        assertTrue(hasDirtyDataSticks(user_uuid), MESSAGE);
        assertEquals(2, accessCache(user_uuid).size(), MESSAGE);
        assertTrue(accessCache(user_uuid).containsAll(List.of(FAKE_1, FAKE_3)), MESSAGE);

        uploadDataSticks(
            COORD_1,
            new ALRecipeDataPacket(new GTRecipe.RecipeAssemblyLine[] { FAKE_2, FAKE_4 }),
            user_uuid);
        assertTrue(hasDirtyDataSticks(user_uuid), MESSAGE);
        assertEquals(4, accessCache(user_uuid).size(), MESSAGE);
        assertTrue(accessCache(user_uuid).containsAll(List.of(FAKE_1, FAKE_2, FAKE_3, FAKE_4)), MESSAGE);

        uploadDataSticks(COORD_1, null, user_uuid);

        assertTrue(hasDirtyDataSticks(user_uuid), MESSAGE);
        assertEquals(4, accessCache(user_uuid).size(), MESSAGE);
    }

    // TODO: Test upload & download by same uuid, preferably with several simulated "downloading hatches"
    // TODO: Test upload & download by different uuids, preferably with several simulated "downloading hatches"

    @ParameterizedTest
    @MethodSource("randomUserUUID4")
    @Override
    public void testTeamChange(UUID... user_uuids) {
        assertTrue(user_uuids.length >= 3, "At least 3 users are required for this test");

        UUID user_uuid_0 = user_uuids[0];
        UUID user_uuid_1 = user_uuids[1];
        UUID user_uuid_2 = user_uuids[2];
        UUID user_uuid_3 = user_uuids[3];
        Team unit_team_0 = TeamManager.createTeam("UnitTest0", user_uuid_0);
        Team unit_team_1 = TeamManager.createTeam("UnitTest1", user_uuid_1);
        Team unit_team_2 = TeamManager.createTeam("UnitTest2", user_uuid_2);
        Team unit_team_3 = TeamManager.createTeam("UnitTest3", user_uuid_3);

        assertFalse(hasDirtyDataSticks(user_uuid_0), MESSAGE);
        assertFalse(hasDirtyDataSticks(user_uuid_1), MESSAGE);
        assertFalse(hasDirtyDataSticks(user_uuid_2), MESSAGE);

        uploadDataSticks(0, new ALRecipeDataPacket(new GTRecipe.RecipeAssemblyLine[] { FAKE_1 }), user_uuid_1);
        uploadDataSticks(1, new ALRecipeDataPacket(new GTRecipe.RecipeAssemblyLine[] { FAKE_2 }), user_uuid_1);
        uploadDataSticks(2, new ALRecipeDataPacket(new GTRecipe.RecipeAssemblyLine[] { FAKE_3 }), user_uuid_0);
        uploadDataSticks(3, new ALRecipeDataPacket(new GTRecipe.RecipeAssemblyLine[] { FAKE_4 }), user_uuid_2);

        assertTrue(hasDirtyDataSticks(user_uuid_0), MESSAGE);
        assertTrue(hasDirtyDataSticks(user_uuid_1), MESSAGE);
        assertTrue(hasDirtyDataSticks(user_uuid_2), MESSAGE);

        assertEquals(2, accessCache(user_uuid_1).size(), MESSAGE);
        assertEquals(1, accessCache(user_uuid_0).size(), MESSAGE);
        assertEquals(1, accessCache(user_uuid_2).size(), MESSAGE);

        forceSetDirtySticks(false, user_uuid_1);
        TeamManager.mergeTeams(unit_team_1, unit_team_0);

        assertEquals(3, accessCache(user_uuid_1).size(), MESSAGE);
        assertEquals(3, accessCache(user_uuid_0).size(), MESSAGE);
        assertTrue(hasDirtyDataSticks(user_uuid_1), MESSAGE);
        assertTrue(hasDirtyDataSticks(user_uuid_0), MESSAGE);

        forceSetDirtySticks(false, user_uuid_1);
        TeamManager.mergeTeams(unit_team_1, unit_team_2);

        assertEquals(4, accessCache(user_uuid_1).size(), MESSAGE);
        assertEquals(4, accessCache(user_uuid_0).size(), MESSAGE);
        assertEquals(4, accessCache(user_uuid_2).size(), MESSAGE);
        assertTrue(hasDirtyDataSticks(user_uuid_1), MESSAGE);
        assertTrue(hasDirtyDataSticks(user_uuid_0), MESSAGE);
        assertTrue(hasDirtyDataSticks(user_uuid_2), MESSAGE);

        forceSetDirtySticks(false, user_uuid_1);
        unit_team_1.removeMember(user_uuid_0);
        unit_team_0 = TeamManager.createTeam("UnitTest0", user_uuid_0);
        TeamManager.transferTeamData(unit_team_1, unit_team_0, user_uuid_0, TeamDataTransferReason.JoinedNewTeam);

        assertTrue(hasDirtyDataSticks(user_uuid_0), MESSAGE);
        assertTrue(hasDirtyDataSticks(user_uuid_1), MESSAGE);
        assertEquals(3, accessCache(user_uuid_1).size(), MESSAGE);
        assertEquals(1, accessCache(user_uuid_0).size(), MESSAGE);

        forceSetDirtySticks(false, user_uuid_1);
        forceSetDirtySticks(false, user_uuid_0);
        unit_team_1.removeMember(user_uuid_2);
        unit_team_2 = TeamManager.createTeam("UnitTest2", user_uuid_2);
        TeamManager.transferTeamData(unit_team_1, unit_team_2, user_uuid_2, TeamDataTransferReason.JoinedNewTeam);

        assertFalse(hasDirtyDataSticks(user_uuid_0), MESSAGE);
        assertTrue(hasDirtyDataSticks(user_uuid_1), MESSAGE);
        assertTrue(hasDirtyDataSticks(user_uuid_2), MESSAGE);
        assertEquals(2, accessCache(user_uuid_1).size(), MESSAGE);
        assertEquals(1, accessCache(user_uuid_0).size(), MESSAGE);
        assertEquals(1, accessCache(user_uuid_2).size(), MESSAGE);

        testTeamChangePost(unit_team_3.getTeamId(), user_uuids);
    }
}
