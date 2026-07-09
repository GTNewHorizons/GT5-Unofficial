package gregtech.wirelessnetwork;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.UUID;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.provider.Arguments;

import com.gtnewhorizon.gtnhlib.teams.TeamDataRegistry;
import com.gtnewhorizon.gtnhlib.teams.TeamManager;

import gregtech.common.misc.WirelessTeamData;

public interface GlobalWirelessTestBase {

    String MESSAGE = "Comparison failed";
    int TEST_CASES = 3;

    @BeforeAll
    static void registerWirelessTeamData() {
        if (!TeamDataRegistry.getRegisteredKeys()
            .contains(WirelessTeamData.DATA_KEY))
            TeamDataRegistry.register(WirelessTeamData.DATA_KEY, WirelessTeamData::new);
    }

    @AfterEach
    default void clearData() {
        TeamManager.clear();
    }

    void testAddToDataSuccessfully(final UUID user_uuid);

    void testAddToDataFailure(final UUID user_uuid);

    void testRemoveFromDataSuccessfully(final UUID user_uuid);

    void testRemoveFromDataFailure(final UUID user_uuid);

    /// Each user's UUID creates its individual team.
    /// Team #:
    /// 0. Should be merged into team 1
    /// 1. Other teams should merge into this team
    /// 2. Should be merged into team 1
    /// 3. Or higher should be left untouched
    void testTeamChange(final UUID... user_uuids);

    default void testTeamChangePost(final UUID unmodified, UUID... user_uuids) {
        if (unmodified == null || user_uuids.length < 4) return;
        assertEquals(
            unmodified,
            TeamManager.getTeamByPlayer(user_uuids[3])
                .getTeamId(),
            MESSAGE);
    }

    static Stream<Arguments> randomUserUUIDs(int count) {
        return Stream.generate(
            () -> Arguments.of(
                (Object) Stream.generate(UUID::randomUUID)
                    .limit(count)
                    .toArray(UUID[]::new)))
            .limit(TEST_CASES);
    }

    static Stream<Arguments> randomUserUUID1() {
        // ugly unpacking from Object[] { UUID[1] } to Object[] { UUID }
        return randomUserUUIDs(1).map(arguments -> Arguments.of(((UUID[]) arguments.get()[0])[0]));
    }

    static Stream<Arguments> randomUserUUID4() {
        return randomUserUUIDs(4);
    }
}
