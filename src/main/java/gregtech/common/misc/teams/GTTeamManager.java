package gregtech.common.misc.teams;

import java.util.ArrayList;
import java.util.List;

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
}
