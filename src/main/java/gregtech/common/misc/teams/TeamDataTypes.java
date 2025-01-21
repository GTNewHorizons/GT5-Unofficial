package gregtech.common.misc.teams;

import java.util.function.Supplier;

public enum TeamDataTypes {

    // EU, unused
    WIRELESS(() -> null),

    // Steam
    PIPELESS(PipelessSteamManager::new),

    ;

    private final Supplier<ITeamData> teamDataCreator;

    TeamDataTypes(Supplier<ITeamData> teamDataCreator) {
        this.teamDataCreator = teamDataCreator;
    }

    public ITeamData construct() {
        return teamDataCreator.get();
    }
}
