package gregtech.common.misc.teams;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GTTeamManager {

    protected static final List<GTTeam> TEAMS = new ArrayList<>();

    public static boolean isTeamNameValid(String name) {
        if (name == null || name.isEmpty()) return false;
        for (GTTeam team : TEAMS) {
            if (team.getTeamName()
                .equals(name)) {
                return false;
            }
        }
        return true;
    }

    public static boolean isInTeam(UUID playerUuid) {
        for (GTTeam team : TEAMS) {
            if (team.isTeamMember(playerUuid)) {
                return true;
            }
        }
        return false;
    }

    public static GTTeam getTeam(String playerName, UUID playerUuid) {
        for (GTTeam team : TEAMS) {
            if (team.isTeamMember(playerUuid)) {
                return team;
            }
        }
        GTTeam team = new GTTeam(playerName + "'s Team");
        team.initializeData(TeamDataTypes.values());
        team.addOwner(playerUuid);
        TeamWorldSavedData.INSTANCE.markDirty();
        return team;
    }

    public static PipelessSteamManager getSteamData(GTTeam team) {
        return (PipelessSteamManager) team.getData(TeamDataTypes.PIPELESS);
    }

    public static void clear() {
        TEAMS.clear();
    }
}
