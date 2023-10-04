package gregtech.common.misc.spaceprojects;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;

import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Recipe;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;

/**
 * @author BlueWeabo
 */
public class SpaceProjectManager {

    /**
     * Do not use! Only meant to be used in SpaceProjectWorldSavedData.java
     */
    public static Map<UUID, Map<Pair<ISpaceBody, String>, ISpaceProject>> spaceTeamProjects = new HashMap<>();
    /**
     * Do not use! Only meant to be used in SpaceProjectWorldSavedData.java Stores a Players UUID to the Leader UUID,
     * players in lone groups give back their own UUID.
     */
    public static Map<UUID, UUID> spaceTeams = new HashMap<>();

    /**
     * Stores all the locations to a hash map to be accessed easier instead of through an enum
     */
    private static final HashMap<String, ISpaceBody> spaceLocations = new HashMap<>();

    /**
     * Stores all projects that have been made. Only adds them to this map if {@link #addProject(ISpaceProject)} has
     * been used
     */
    private static final Map<String, ISpaceProject> spaceProjects = new HashMap<>();

    // #region Space Project Team Helper methods

    /**
     * Used to get a specific project of the team dependent on the location and the project's name
     */
    public static ISpaceProject getTeamProject(UUID member, ISpaceBody location, String projectName) {
        Map<Pair<ISpaceBody, String>, ISpaceProject> map = spaceTeamProjects.get(getLeader(member));
        if (map == null) {
            return null;
        }
        return map.get(Pair.of(location, projectName));
    }

    /**
     * Makes a new Map for the teams if they don't have one. Adds a project to the team's project map.
     *
     * @param member      Member of the team.
     * @param location    The location of where the project will belong to.
     * @param projectName The name of the project being added.
     * @param project     Project which will be added to the team.
     * @return Returns true when a project was added to the map of the player. Returns false otherwise.
     */
    public static boolean addTeamProject(UUID member, ISpaceBody location, String projectName, ISpaceProject project) {
        if (!spaceTeamProjects.containsKey(getLeader(member)) || spaceTeamProjects.get(getLeader(member)) == null) {
            spaceTeamProjects.put(getLeader(member), new HashMap<>());
        }

        Map<Pair<ISpaceBody, String>, ISpaceProject> map = spaceTeamProjects.get(getLeader(member));
        if (map.containsKey(Pair.of(location, projectName))) {
            return false;
        }

        project.setProjectLocation(location);
        map.put(Pair.of(location, projectName), project);
        SpaceProjectWorldSavedData.INSTANCE.markDirty();
        return true;
    }

    /**
     * Check whether a team has a project or not
     *
     * @param member  Member of the team
     * @param project The project, which you are checking for. This only compares the internal names of the project.
     * @return True if the team has said project, false otherwise
     */
    public static boolean teamHasProject(UUID member, ISpaceProject project) {
        Map<Pair<ISpaceBody, String>, ISpaceProject> map = spaceTeamProjects.get(getLeader(member));
        if (map == null) {
            return false;
        }

        return map.containsValue(project);
    }

    /**
     * Used to handle when 2 players want to join together in a team. Player A can join player B's team. If player C
     * gets an invite from A, C will join player B's team.
     *
     * @param teamMember Member which is joining the teamLeader
     * @param teamLeader Leader of the party
     */
    public static void putInTeam(UUID teamMember, UUID teamLeader) {
        if (teamMember.equals(teamLeader)) {
            spaceTeams.put(teamMember, teamLeader);
        } else if (!spaceTeams.get(teamLeader)
            .equals(teamLeader)) {
                putInTeam(teamMember, spaceTeams.get(teamLeader));
            } else {
                spaceTeams.put(teamMember, teamLeader);
            }

        SpaceProjectWorldSavedData.INSTANCE.markDirty();
    }

    /**
     * Used to give back the UUID of the team leader.
     *
     * @return The UUID of the team leader.
     */
    public static UUID getLeader(UUID teamMember) {
        checkOrCreateTeam(teamMember);
        return spaceTeams.get(teamMember);
    }

    /**
     * Used the multiblocks to check whether a given player has a team or not. If they don't have a team create one
     * where they are their own leader.
     *
     * @param teamMember Member to check for.
     */
    public static void checkOrCreateTeam(UUID teamMember) {
        if (spaceTeams.containsKey(teamMember)) {
            return;
        }

        spaceTeams.put(teamMember, teamMember);
        SpaceProjectWorldSavedData.INSTANCE.markDirty();
    }

    /**
     * Will give back all the projects a team has made or is making.
     *
     * @param member UUID of the team member, used to find the leader of the team.
     * @return All the projects a team has.
     */
    public static Collection<ISpaceProject> getTeamSpaceProjects(UUID member) {
        Map<Pair<ISpaceBody, String>, ISpaceProject> map = spaceTeamProjects.get(getLeader(member));
        if (map == null) {
            return null;
        }

        return map.values();
    }

    /**
     * Getting the project of a Team or a new copy.
     *
     * @param member      UUID of the team member, which is used to find the team leader.
     * @param projectName The name of the project, which needs to be found.
     * @param location    The location at which the project is found at.
     * @return the project that the team has or a copy of a project.
     */
    public static ISpaceProject getTeamProjectOrCopy(UUID member, String projectName, ISpaceBody location) {
        Map<Pair<ISpaceBody, String>, ISpaceProject> map = spaceTeamProjects.get(getLeader(member));
        if (map == null) {
            return getProject(projectName);
        }

        return map.getOrDefault(Pair.of(location, projectName), getProject(projectName));
    }

    // #endregion

    // #region Project Helper methods

    public static class FakeSpaceProjectRecipe extends GT_Recipe {

        public final String projectName;

        public FakeSpaceProjectRecipe(boolean aOptimize, ItemStack[] aInputs, FluidStack[] aFluidInputs, int aDuration,
            int aEUt, int aSpecialValue, String projectName) {
            super(aOptimize, aInputs, null, null, null, aFluidInputs, null, aDuration, aEUt, aSpecialValue);
            this.projectName = projectName;
        }
    }

    /**
     * Used to add projects to the internal map.
     *
     * @param project Newly created project.
     */
    public static void addProject(ISpaceProject project) {
        spaceProjects.put(project.getProjectName(), project);
        RecipeMaps.spaceProjectFakeRecipes.add(
            new FakeSpaceProjectRecipe(
                false,
                project.getTotalItemsCost(),
                project.getTotalFluidsCost(),
                project.getProjectBuildTime(),
                (int) project.getProjectVoltage(),
                project.getTotalStages(),
                project.getProjectName()));
    }

    /**
     * @param projectName Internal name of the project.
     * @return a copy of the stored project.
     */
    public static ISpaceProject getProject(String projectName) {
        ISpaceProject tProject = spaceProjects.get(projectName);
        return tProject != null ? tProject.copy() : null;
    }

    /**
     * Should only be used for GUIs!
     *
     * @return The Map that the projects are stored at.
     */
    public static Map<String, ISpaceProject> getProjectsMap() {
        return spaceProjects;
    }

    /**
     * Should only be used for GUIs!
     *
     * @return A Collection of all the projects contained in the map.
     */
    public static Collection<ISpaceProject> getAllProjects() {
        return spaceProjects.values();
    }

    // #endregion

    // #region Location Helper methods

    /**
     * Adds a location to the internal map. For it to be used later
     *
     * @param location to add to the internal map
     */
    public static void addLocation(ISpaceBody location) {
        spaceLocations.put(location.getName(), location);
    }

    /**
     *
     * @return a Collection of all locations, which have been registered.
     */
    public static Collection<ISpaceBody> getLocations() {
        return spaceLocations.values();
    }

    /**
     *
     * @return a Collection fo all location names, which have been registered
     */
    public static Collection<String> getLocationNames() {
        return spaceLocations.keySet();
    }

    /**
     *
     * @param locationName Name used to search for the location
     * @return The location, which has been registered with said name
     */
    public static ISpaceBody getLocation(String locationName) {
        return spaceLocations.get(locationName);
    }

    // #endregion

    // #region General Helper methods

    /**
     * Gets the UUID using the player's username
     */
    public static UUID getPlayerUUIDFromName(String playerName) {
        return MinecraftServer.getServer()
            .func_152358_ax()
            .func_152655_a(playerName)
            .getId();
    }

    /**
     * Gets the player's name using their UUID
     */
    public static String getPlayerNameFromUUID(UUID playerUUID) {
        return MinecraftServer.getServer()
            .func_152358_ax()
            .func_152652_a(playerUUID)
            .getName();
    }

    // #endregion
}
