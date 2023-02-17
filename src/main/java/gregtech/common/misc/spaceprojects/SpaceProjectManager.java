package gregtech.common.misc.spaceprojects;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.server.MinecraftServer;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.common.misc.spaceprojects.base.SpaceProject;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;

public class SpaceProjectManager {

    /**
     * Do not use! Only meant to be used in SpaceProjectWorldSavedData.java
     */
    public static Map<UUID, Map<Pair<ISpaceBody, String>, SpaceProject>> spaceTeamProjects = new HashMap<>();
    /**
     * Do not use! Only meant to be used in SpaceProjectWorldSavedData.java Stores a Players UUID to the Leader UUID,
     * players in lone groups give back their own UUID.
     */
    public static Map<UUID, UUID> spaceTeams = new HashMap<>();

    private static final HashMap<String, ISpaceBody> spaceLocations = new HashMap<>();

    private static final Map<String, SpaceProject> spaceProjects = new HashMap<>();

    /*
     * Team Section
     */
    public static SpaceProject getTeamProject(UUID team, ISpaceBody location, String projectName) {
        Map<Pair<ISpaceBody, String>, SpaceProject> map = spaceTeamProjects.get(team);
        if (map == null) {
            return null;
        }
        return map.get(Pair.of(location, projectName));
    }

    public static boolean addTeamProject(UUID team, ISpaceBody location, String projectName, SpaceProject project) {
        if (!spaceTeamProjects.containsKey(getLeader(team)) || spaceTeamProjects.get(getLeader(team)) == null) {
            spaceTeamProjects.put(getLeader(team), new HashMap<Pair<ISpaceBody, String>, SpaceProject>());
        }

        Map<Pair<ISpaceBody, String>, SpaceProject> map = spaceTeamProjects.get(getLeader(team));
        if (map.containsKey(Pair.of(location, projectName))) {
            return false;
        }

        project.setProjectLocation(location);
        map.put(Pair.of(location, projectName), project);
        SpaceProjectWorldSavedData.INSTANCE.markDirty();
        return true;
    }

    public static boolean teamHasProject(UUID team, SpaceProject project) {
        Map<Pair<ISpaceBody, String>, SpaceProject> map = spaceTeamProjects.get(getLeader(team));
        if (map == null) {
            return false;
        }

        return map.containsValue(project);
    }

    public static void putInTeam(UUID teamMember, UUID teamLeader) {
        if (teamMember.equals(teamLeader)) {
            spaceTeams.put(teamMember, teamLeader);
        } else if (!spaceTeams.get(teamLeader).equals(teamLeader)) {
            putInTeam(teamMember, spaceTeams.get(teamLeader));
        } else {
            spaceTeams.put(teamMember, teamLeader);
        }

        SpaceProjectWorldSavedData.INSTANCE.markDirty();
    }

    public static UUID getLeader(UUID teamMember) {
        return spaceTeams.get(teamMember);
    }

    public static void checkOrCreateTeam(UUID teamMember) {
        if (spaceTeams.containsKey(teamMember)) {
            return;
        }

        spaceTeams.put(teamMember, teamMember);
        SpaceProjectWorldSavedData.INSTANCE.markDirty();
    }

    public static Collection<SpaceProject> getTeamSpaceProjects(UUID team) {
        Map<Pair<ISpaceBody, String>, SpaceProject> map = spaceTeamProjects.get(getLeader(team));
        if (map == null) {
            return null;
        }

        return map.values();
    }

    public static SpaceProject getTeamProjectOrCopy(UUID team, String projectName, ISpaceBody location) {
        Map<Pair<ISpaceBody, String>, SpaceProject> map = spaceTeamProjects.get(getLeader(team));
        if (map == null) {
            return getProject(projectName);
        }

        return map.getOrDefault(Pair.of(location, projectName), getProject(projectName));
    }

    /*
     * General Project Helpers
     */
    public static void addProject(SpaceProject project) {
        spaceProjects.put(project.getProjectName(), project);
    }

    /**
     * @param projectName Internal name of the project
     * @return a copy of the stored project
     */
    public static SpaceProject getProject(String projectName) {
        SpaceProject tProject = spaceProjects.get(projectName);
        return tProject != null ? tProject.copy() : null;
    }

    /**
     * Should only be used for GUIs!
     */
    public static Map<String, SpaceProject> getAllProjects() {
        return spaceProjects;
    }

    public static Collection<SpaceProject> getProjects() {
        return spaceProjects.values();
    }

    /*
     * Location Helper
     */
    public static void addLocation(ISpaceBody location) {
        spaceLocations.put(location.getName(), location);
    }

    public static Collection<ISpaceBody> getLocations() {
        return spaceLocations.values();
    }

    public static Collection<String> getLocationNames() {
        return spaceLocations.keySet();
    }

    public static ISpaceBody getLocation(String locationName) {
        return spaceLocations.get(locationName);
    }

    /*
     * Helpers
     */
    public static UUID getPlayerUUIDFromName(String playerName) {
        return MinecraftServer.getServer().func_152358_ax().func_152655_a(playerName).getId();
    }

    public static String getPlayerNameFromUUID(UUID playerUUID) {
        return MinecraftServer.getServer().func_152358_ax().func_152652_a(playerUUID).getName();
    }
}
