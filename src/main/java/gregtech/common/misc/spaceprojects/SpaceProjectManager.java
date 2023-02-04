package gregtech.common.misc.spaceprojects;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.MinecraftServer;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;

public class SpaceProjectManager {

    /**
     * Do not use! Only meant to be used in SpaceProjectWorldSavedData.java
     */
    public static Map<UUID, Map<Pair<ISpaceBody, String>, ISpaceProject>> mSpaceTeamProjects = new HashMap<>();
    /**
     * Do not use! Only meant to be used in SpaceProjectWorldSavedData.java Stores a Players UUID to the Leader UUID,
     * players in lone groups give back their own UUID.
     */
    public static Map<UUID, UUID> mSpaceTeams = new HashMap<>();

    private static final Map<String, ISpaceProject> mSpaceProjects = new HashMap<>();

    public static ISpaceProject getTeamProject(UUID aTeam, ISpaceBody aLocation, String aProjectName) {
        Map<Pair<ISpaceBody, String>, ISpaceProject> tMap = mSpaceTeamProjects.get(aTeam);
        if (tMap == null) {
            return null;
        }
        return tMap.get(Pair.of(aLocation, aProjectName));
    }

    public static boolean addTeamProject(UUID aTeam, ISpaceBody aLocation, String aProjectName,
            ISpaceProject aProject) {
        if (!mSpaceTeamProjects.containsKey(aTeam)) {
            mSpaceTeamProjects.put(getLeader(aTeam), new HashMap<Pair<ISpaceBody, String>, ISpaceProject>());
        }
        Map<Pair<ISpaceBody, String>, ISpaceProject> tMap = mSpaceTeamProjects.get(aTeam);
        if (tMap.containsKey(Pair.of(aLocation, aProjectName))) return false;
        tMap.put(Pair.of(aLocation, aProjectName), aProject);
        return true;
    }

    public static void addProject(ISpaceProject aProject) {
        mSpaceProjects.put(aProject.getProjectName(), aProject);
    }

    public static ISpaceProject getProject(String aProjectName) {
        return mSpaceProjects.get(aProjectName).copy();
    }

    /**
     * Should only be used for GUIs!
     */
    public static Map<String, ISpaceProject> getAllProjects() {
        return mSpaceProjects;
    }

    public static UUID getPlayerUUIDFromName(String aPlayerName) {
        return MinecraftServer.getServer().func_152358_ax().func_152655_a(aPlayerName).getId();
    }

    public static String getPlayerNameFromUUID(UUID aPlayerUUID) {
        return MinecraftServer.getServer().func_152358_ax().func_152652_a(aPlayerUUID).getName();
    }

    public static void putInTeam(UUID aTeamMember, UUID aTeamLeader) {
        if (aTeamMember.equals(aTeamLeader)) {
            mSpaceTeams.put(aTeamMember, aTeamLeader);
        } else if (!mSpaceTeams.get(aTeamLeader).equals(aTeamLeader)) {
            putInTeam(aTeamMember, mSpaceTeams.get(aTeamLeader));
        } else {
            mSpaceTeams.put(aTeamMember, aTeamLeader);
        }
    }

    public static UUID getLeader(UUID aTeamMember) {
        return mSpaceTeams.get(aTeamMember);
    }
}
