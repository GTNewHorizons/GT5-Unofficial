package gregtech.common.misc.spaceprojects;

import static gregtech.common.misc.spaceprojects.SpaceProjectManager.spaceTeamProjects;
import static gregtech.common.misc.spaceprojects.SpaceProjectManager.spaceTeams;
import static gregtech.common.misc.spaceprojects.enums.JsonVariables.MAP_MAP;
import static gregtech.common.misc.spaceprojects.enums.JsonVariables.MAP_PAIR;
import static gregtech.common.misc.spaceprojects.enums.JsonVariables.MAP_PROJECT;
import static gregtech.common.misc.spaceprojects.enums.JsonVariables.MAP_UUID;
import static gregtech.common.misc.spaceprojects.enums.JsonVariables.PAIR_LEFT;
import static gregtech.common.misc.spaceprojects.enums.JsonVariables.PAIR_RIGHT;
import static gregtech.common.misc.spaceprojects.enums.JsonVariables.PROJECT_CURRENT_STAGE;
import static gregtech.common.misc.spaceprojects.enums.JsonVariables.PROJECT_CURRENT_UPGRADE;
import static gregtech.common.misc.spaceprojects.enums.JsonVariables.PROJECT_LOCATION;
import static gregtech.common.misc.spaceprojects.enums.JsonVariables.PROJECT_NAME;
import static gregtech.common.misc.spaceprojects.enums.JsonVariables.PROJECT_UPGRADES_BUILT;
import static gregtech.common.misc.spaceprojects.enums.JsonVariables.UPGRADE_CURRENT_STAGE;
import static gregtech.common.misc.spaceprojects.enums.JsonVariables.UPGRADE_NAME;
import static gregtech.common.misc.spaceprojects.enums.JsonVariables.UPGRADE_PROJECT_PARENT;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.stream.JsonReader;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.common.misc.spaceprojects.enums.SolarSystem;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject.ISP_Upgrade;

/**
 * This class is used so that I can write and read to a json file before the world is opened. On server starting is too
 * late for this as the data stored in the files is needed before entities load their nbt data
 *
 * @author BlueWeabo
 */
public class SpaceProjectWorldSavedData extends WorldSavedData {

    public static SpaceProjectWorldSavedData INSTANCE;

    private static final Gson GSON_SPACE_PROJECT = new GsonBuilder().serializeNulls()
        .enableComplexMapKeySerialization()
        .registerTypeAdapter(spaceTeamProjects.getClass(), new SpaceTeamProjectsMapAdapter())
        .registerTypeAdapter(Map.class, new SpaceTeamProjectsMapAdapter())
        .registerTypeAdapter(
            Pair.of((ISpaceBody) SolarSystem.Ariel, "")
                .getClass(),
            new PairAdapter())
        .registerTypeAdapter(Pair.class, new PairAdapter())
        .registerTypeAdapter(ISpaceProject.class, new SpaceProjectAdapter())
        .registerTypeAdapter(ISP_Upgrade.class, new SP_UpgradeAdapter())
        .registerTypeHierarchyAdapter(ISpaceProject.class, new SpaceProjectAdapter())
        .registerTypeHierarchyAdapter(ISP_Upgrade.class, new SP_UpgradeAdapter())
        .create();
    private static final Gson GSON_TEAMS = new GsonBuilder().serializeNulls()
        .registerTypeAdapter(spaceTeams.getClass(), new SpaceTeamAdapter())
        .create();

    private static final String DATA_NAME = "GT_SpaceProjectData";

    private static final String SPACE_TEAM_PROJECTS_JSON = "spaceTeamProject.json";

    private static final String SPACE_TEAMS_JSON = "spaceTeams.json";

    private static final String SPACE_TEAM_PROJECTS_NBT = "spaceTeamProjects";

    private static final String SPACE_TEAMS_NBT = "spaceTeams";

    private static File spaceTeamsFile;
    private static File teamProjectsFile;

    public SpaceProjectWorldSavedData() {
        super(DATA_NAME);
    }

    public SpaceProjectWorldSavedData(String aData) {
        super(aData);
    }

    @Override
    public void readFromNBT(NBTTagCompound aNBT) {
        if (!aNBT.hasKey(SPACE_TEAM_PROJECTS_NBT)) {
            // We don't have a key? Try to read from file
            try (JsonReader reader = new JsonReader(new FileReader(teamProjectsFile))) {
                spaceTeamProjects = GSON_SPACE_PROJECT.fromJson(reader, spaceTeamProjects.getClass());
            } catch (Exception e) {
                spaceTeamProjects = null;
            }
        }

        if (spaceTeamProjects == null) {
            spaceTeamProjects = new HashMap<>();
        }

        if (aNBT.hasKey(SPACE_TEAM_PROJECTS_NBT)) {
            spaceTeamProjects = GSON_SPACE_PROJECT
                .fromJson(aNBT.getString(SPACE_TEAM_PROJECTS_NBT), spaceTeamProjects.getClass());
        }

        if (!aNBT.hasKey(SPACE_TEAMS_NBT)) {
            // We don't have a key? Try to read from file
            try (JsonReader reader = new JsonReader(new FileReader(spaceTeamsFile))) {
                HashMap<UUID, UUID> jsonMap = GSON_TEAMS.fromJson(reader, spaceTeams.getClass());
                for (UUID member : jsonMap.keySet()) {
                    spaceTeams.put(member, jsonMap.get(member));
                }
            } catch (Exception e) {
                spaceTeams = null;
            }
        }

        if (spaceTeams == null) {
            spaceTeams = new HashMap<>();
        }

        if (aNBT.hasKey(SPACE_TEAMS_NBT)) {
            spaceTeams = GSON_TEAMS.fromJson(aNBT.getString(SPACE_TEAMS_NBT), spaceTeams.getClass());
        }

        if (spaceTeamProjects == null) {
            spaceTeamProjects = new HashMap<>();
        }

        if (spaceTeams == null) {
            spaceTeams = new HashMap<>();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound aNBT) {
        if (spaceTeamProjects != null) {
            aNBT.setString(SPACE_TEAM_PROJECTS_NBT, GSON_SPACE_PROJECT.toJson(spaceTeamProjects));
        }

        if (spaceTeams != null) {
            aNBT.setString(SPACE_TEAMS_NBT, GSON_TEAMS.toJson(spaceTeams));
        }
    }

    private static void loadInstance(World aWorld) {
        if (spaceTeamProjects != null) {
            spaceTeamProjects.clear();
        } else {
            spaceTeamProjects = new HashMap<>();
        }
        if (spaceTeams != null) {
            spaceTeams.clear();
        } else {
            spaceTeams = new HashMap<>();
        }
        spaceTeamsFile = new File(
            aWorld.getSaveHandler()
                .getWorldDirectory(),
            SPACE_TEAMS_JSON);
        teamProjectsFile = new File(
            aWorld.getSaveHandler()
                .getWorldDirectory(),
            SPACE_TEAM_PROJECTS_JSON);
        MapStorage tStorage = aWorld.mapStorage;
        INSTANCE = (SpaceProjectWorldSavedData) tStorage.loadData(SpaceProjectWorldSavedData.class, DATA_NAME);
        if (INSTANCE == null) {
            INSTANCE = new SpaceProjectWorldSavedData();
            tStorage.setData(DATA_NAME, INSTANCE);
        }
        INSTANCE.markDirty();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load aEvent) {
        if (!aEvent.world.isRemote && aEvent.world.provider.dimensionId == 0) {
            loadInstance(aEvent.world);
        }
    }

    /**
     * gson doesn't want to cast Strings to UUIDs by default, thus we need a deserializer.
     *
     * @author Tctcl
     */
    private static class SpaceTeamAdapter
        implements JsonSerializer<HashMap<UUID, UUID>>, JsonDeserializer<HashMap<UUID, UUID>> {

        @Override
        public JsonElement serialize(HashMap<UUID, UUID> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonArray map = new JsonArray();
            for (Entry<UUID, UUID> entry : src.entrySet()) {
                JsonObject teamMap = new JsonObject();

                teamMap.addProperty(
                    "MEMBER_UUID",
                    entry.getKey()
                        .toString());
                teamMap.addProperty(
                    "LEADER_UUID",
                    entry.getValue()
                        .toString());

                map.add(teamMap);
            }

            return map;
        }

        @Override
        public HashMap<UUID, UUID> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            HashMap<UUID, UUID> map = new HashMap<>();
            JsonArray jsonArray = json.getAsJsonArray();
            for (JsonElement element : jsonArray) {
                JsonObject jsonObject = element.getAsJsonObject();
                UUID memberUuid = UUID.fromString(
                    jsonObject.get("MEMBER_UUID")
                        .getAsString());
                UUID leaderUuid = UUID.fromString(
                    jsonObject.get("LEADER_UUID")
                        .getAsString());
                map.put(memberUuid, leaderUuid);
            }
            return map;
        }
    }

    private static class PairAdapter
        implements JsonSerializer<Pair<ISpaceBody, String>>, JsonDeserializer<Pair<ISpaceBody, String>> {

        @Override
        public JsonElement serialize(Pair<ISpaceBody, String> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject pair = new JsonObject();
            pair.addProperty(
                PAIR_LEFT,
                src.getLeft()
                    .getName());
            pair.addProperty(PAIR_RIGHT, src.getRight());
            return pair;
        }

        @Override
        public Pair<ISpaceBody, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            Pair<ISpaceBody, String> pair = null;
            if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                pair = Pair.of(
                    SpaceProjectManager.getLocation(
                        obj.get(PAIR_LEFT)
                            .getAsString()),
                    obj.get(PAIR_RIGHT)
                        .getAsString());
            }
            return pair;
        }
    }

    private static class SpaceProjectAdapter implements JsonSerializer<ISpaceProject>, JsonDeserializer<ISpaceProject> {

        @Override
        public JsonElement serialize(ISpaceProject src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty(PROJECT_NAME, src.getProjectName());
            obj.addProperty(PROJECT_CURRENT_STAGE, src.getCurrentStage());
            obj.addProperty(
                PROJECT_LOCATION,
                src.getProjectLocation()
                    .getName());
            obj.add(PROJECT_CURRENT_UPGRADE, context.serialize(src.getUpgradeBeingBuilt()));
            obj.add(PROJECT_UPGRADES_BUILT, context.serialize(src.getAllBuiltUpgrades()));
            return obj;
        }

        @Override
        public ISpaceProject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            if (!json.isJsonObject()) {
                return null;
            }
            JsonObject obj = json.getAsJsonObject();
            String projectName = obj.get(PROJECT_NAME)
                .getAsString();
            ISpaceProject project = SpaceProjectManager.getProject(projectName);
            int projectCurrentStage = obj.get(PROJECT_CURRENT_STAGE)
                .getAsInt();
            ISP_Upgrade[] projectUpgradesBuilt = new ISP_Upgrade[0];
            projectUpgradesBuilt = context
                .deserialize(obj.get(PROJECT_UPGRADES_BUILT), projectUpgradesBuilt.getClass());
            ISP_Upgrade projectCurrentUpgrade = context
                .deserialize(obj.get(PROJECT_CURRENT_UPGRADE), ISP_Upgrade.class);
            ISpaceBody projectLocation = SpaceProjectManager.getLocation(
                obj.get(PROJECT_LOCATION)
                    .getAsString());
            project.setBuiltUpgrade(projectUpgradesBuilt);
            project.setProjectLocation(projectLocation);
            project.setProjectCurrentStage(projectCurrentStage);
            project.setCurrentUpgradeBeingBuilt(projectCurrentUpgrade);
            return project;
        }
    }

    private static class SP_UpgradeAdapter implements JsonSerializer<ISP_Upgrade>, JsonDeserializer<ISP_Upgrade> {

        @Override
        public JsonElement serialize(ISP_Upgrade src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty(UPGRADE_NAME, src.getUpgradeName());
            obj.addProperty(
                UPGRADE_PROJECT_PARENT,
                src.getParentProject()
                    .getProjectName());
            obj.addProperty(UPGRADE_CURRENT_STAGE, src.getCurrentStage());
            return obj;
        }

        @Override
        public ISP_Upgrade deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            if (!json.isJsonObject()) {
                return null;
            }
            JsonObject obj = json.getAsJsonObject();
            String projectName = obj.get(UPGRADE_PROJECT_PARENT)
                .getAsString();
            ISpaceProject project = SpaceProjectManager.getProject(projectName);
            ISP_Upgrade upgrade = project.getUpgrade(
                obj.get(UPGRADE_NAME)
                    .getAsString());
            if (upgrade == null) {
                return null;
            }
            upgrade = upgrade.copy();
            upgrade.setUpgradeCurrentStage(
                obj.get(UPGRADE_CURRENT_STAGE)
                    .getAsInt());
            return upgrade;
        }
    }

    private static class SpaceTeamProjectsMapAdapter
        implements JsonSerializer<Map<UUID, Map<Pair<ISpaceBody, String>, ISpaceProject>>>,
        JsonDeserializer<Map<UUID, Map<Pair<ISpaceBody, String>, ISpaceProject>>> {

        @Override
        public JsonElement serialize(Map<UUID, Map<Pair<ISpaceBody, String>, ISpaceProject>> src, Type typeOfSrc,
            JsonSerializationContext context) {
            JsonArray map = new JsonArray();
            for (Entry<UUID, Map<Pair<ISpaceBody, String>, ISpaceProject>> firstEntry : src.entrySet()) {
                JsonObject teamMap = new JsonObject();
                teamMap.add(MAP_UUID, context.serialize(firstEntry.getKey()));
                JsonArray teamProjectMap = new JsonArray();
                for (Entry<Pair<ISpaceBody, String>, ISpaceProject> secondEntry : firstEntry.getValue()
                    .entrySet()) {
                    JsonObject projectMap = new JsonObject();
                    projectMap.add(MAP_PAIR, context.serialize(secondEntry.getKey()));
                    projectMap.add(MAP_PROJECT, context.serialize(secondEntry.getValue()));
                    teamProjectMap.add(projectMap);
                }
                teamMap.add(MAP_MAP, teamProjectMap);
                map.add(teamMap);
            }
            return map;
        }

        @Override
        public Map<UUID, Map<Pair<ISpaceBody, String>, ISpaceProject>> deserialize(JsonElement json, Type typeOfT,
            JsonDeserializationContext context) throws JsonParseException {
            JsonArray mapArray = json.getAsJsonArray();
            Map<UUID, Map<Pair<ISpaceBody, String>, ISpaceProject>> map = new HashMap<>();
            for (JsonElement teamMapElement : mapArray) {
                JsonObject teamMap = teamMapElement.getAsJsonObject();
                UUID uuid = context.deserialize(teamMap.get(MAP_UUID), UUID.class);
                Map<Pair<ISpaceBody, String>, ISpaceProject> projectMap = new HashMap<>();
                for (JsonElement teamProjectMapElement : teamMap.get(MAP_MAP)
                    .getAsJsonArray()) {
                    JsonObject teamProjectMap = teamProjectMapElement.getAsJsonObject();
                    Pair<ISpaceBody, String> pair = context.deserialize(teamProjectMap.get(MAP_PAIR), Pair.class);
                    ISpaceProject project = context.deserialize(teamProjectMap.get(MAP_PROJECT), ISpaceProject.class);
                    projectMap.put(pair, project);
                }
                map.put(uuid, projectMap);
            }
            return map;
        }
    }
}
