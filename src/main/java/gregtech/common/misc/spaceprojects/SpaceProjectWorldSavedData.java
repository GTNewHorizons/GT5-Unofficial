package gregtech.common.misc.spaceprojects;

import static gregtech.common.misc.spaceprojects.SpaceProjectManager.spaceTeamProjects;
import static gregtech.common.misc.spaceprojects.SpaceProjectManager.spaceTeams;
import static gregtech.common.misc.spaceprojects.enums.JsonVariables.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
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

    private static final Gson GSON_READER = new GsonBuilder().serializeNulls()
            .registerTypeAdapter(Pair.class, new PairDeserializer())
            .registerTypeAdapter(ISpaceProject.class, new SpaceProjectDeserializer())
            .registerTypeAdapter(ISP_Upgrade.class, new SP_UpgradeDeserializer()).create();

    private static final Gson GSON_WRITER = new GsonBuilder().serializeNulls()
            .registerTypeAdapter(Pair.class, new PairSerializer())
            .registerTypeAdapter(ISpaceProject.class, new SpaceProjectSerializer())
            .registerTypeAdapter(ISP_Upgrade.class, new SP_UpgradeSerializer()).create();

    private static final String DATA_NAME = "GT_SpaceProjectData";

    private static final String SPACE_TEAM_PROJECTS_JSON = "spaceTeamProject.json";

    private static final String SPACE_TEAMS_JSON = "spaceTeams.json";

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
        Type teamProjectsType = new TypeToken<Map<UUID, Map<Pair<ISpaceBody, String>, ISpaceProject>>>() {}.getType();
        try (JsonReader reader = new JsonReader(new FileReader(teamProjectsFile))) {
            spaceTeamProjects = GSON_READER.fromJson(reader, teamProjectsType);
        } catch (IOException e) {
            System.out.print("FAILED TO LOAD: " + SPACE_TEAM_PROJECTS_JSON);
            e.printStackTrace();
        }

        if (spaceTeamProjects == null) {
            spaceTeamProjects = new HashMap<>();
        }

        Type spaceTeamsType = new TypeToken<Map<UUID, UUID>>() {}.getType();
        try (JsonReader reader = new JsonReader(new FileReader(spaceTeamsFile))) {
            spaceTeams = GSON_READER.fromJson(reader, spaceTeamsType);
        } catch (IOException e) {
            System.out.print("FAILED TO LOAD: " + SPACE_TEAMS_JSON);
            e.printStackTrace();
        }

        if (spaceTeams == null) {
            spaceTeams = new HashMap<>();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound aNBT) {
        try (JsonWriter writer = new JsonWriter(new FileWriter(teamProjectsFile))) {
            GSON_WRITER.toJson(spaceTeamProjects, spaceTeamProjects.getClass(), writer);
        } catch (IOException ex) {
            System.out.print("FAILED TO SAVE: " + SPACE_TEAM_PROJECTS_JSON);
            ex.printStackTrace();
        }

        try (JsonWriter writer = new JsonWriter(new FileWriter(spaceTeamsFile))) {
            GSON_WRITER.toJson(spaceTeams, spaceTeams.getClass(), writer);
        } catch (IOException ex) {
            System.out.print("FAILED TO SAVE: " + SPACE_TEAMS_JSON);
            ex.printStackTrace();
        }
    }

    private static void loadInstance(World aWorld) {
        spaceTeamProjects.clear();
        spaceTeams.clear();
        spaceTeamsFile = new File(aWorld.getSaveHandler().getWorldDirectory(), SPACE_TEAMS_JSON);
        teamProjectsFile = new File(aWorld.getSaveHandler().getWorldDirectory(), SPACE_TEAM_PROJECTS_JSON);
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

    private static class PairSerializer implements JsonSerializer<Pair<ISpaceBody, String>> {

        @Override
        public JsonElement serialize(Pair<ISpaceBody, String> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject pair = new JsonObject();
            pair.addProperty(PAIR_LEFT, src.getLeft().getName());
            pair.addProperty(PAIR_RIGHT, src.getRight());
            return pair;
        }
    }

    private static class PairDeserializer implements JsonDeserializer<Pair<ISpaceBody, String>> {

        @Override
        public Pair<ISpaceBody, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            Pair<ISpaceBody, String> pair = null;
            if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                pair = Pair.of(
                        SpaceProjectManager.getLocation(obj.get(PAIR_LEFT).getAsString()),
                        obj.get(PAIR_RIGHT).getAsString());
            }
            return pair;
        }
    }

    private static class SpaceProjectSerializer implements JsonSerializer<ISpaceProject> {

        @Override
        public JsonElement serialize(ISpaceProject src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty(PROJECT_NAME, src.getProjectName());
            obj.addProperty(PROJECT_CURRENT_STAGE, src.getCurrentStage());
            obj.addProperty(PROJECT_LOCATION, src.getProjectLocation().getName());
            obj.add(PROJECT_CURRENT_UPGRADE, context.serialize(src.getUpgradeBeingBuilt()));
            obj.add(PROJECT_UPGRADES_BUILT, context.serialize(src.getAllBuiltUpgrades()));
            return obj;
        }
    }

    private static class SpaceProjectDeserializer implements JsonDeserializer<ISpaceProject> {

        @Override
        public ISpaceProject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (!json.isJsonObject()) {
                return null;
            }
            JsonObject obj = json.getAsJsonObject();
            String projectName = obj.get(PROJECT_NAME).getAsString();
            ISpaceProject project = SpaceProjectManager.getProject(projectName);
            int projectCurrentStage = obj.get(PROJECT_CURRENT_STAGE).getAsInt();
            ISP_Upgrade[] projectUpgradesBuilt = context
                    .deserialize(obj.get(PROJECT_UPGRADES_BUILT), ISP_Upgrade.class);
            ISP_Upgrade projectCurrentUpgrade = context
                    .deserialize(obj.get(PROJECT_CURRENT_UPGRADE), ISP_Upgrade.class);
            ISpaceBody projectLocation = SpaceProjectManager.getLocation(obj.get(PROJECT_LOCATION).getAsString());
            project.setBuiltUpgrade(projectUpgradesBuilt);
            project.setCurrentUpgradeBeingBuilt(projectCurrentUpgrade);
            project.setProjectLocation(projectLocation);
            project.setProjectCurrentStage(projectCurrentStage);
            return project;
        }
    }

    private static class SP_UpgradeSerializer implements JsonSerializer<ISP_Upgrade> {

        @Override
        public JsonElement serialize(ISP_Upgrade src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject obj = new JsonObject();
            obj.addProperty(UPGRADE_NAME, src.getUpgradeName());
            obj.addProperty(UPGRADE_PROJECT_PARENT, src.getParentProject().getProjectName());
            obj.addProperty(UPGRADE_CURRENT_STAGE, src.getCurrentStage());
            return obj;
        }
    }

    private static class SP_UpgradeDeserializer implements JsonDeserializer<ISP_Upgrade> {

        @Override
        public ISP_Upgrade deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (!json.isJsonObject()) {
                return null;
            }
            JsonObject obj = json.getAsJsonObject();
            String projectName = obj.get(UPGRADE_PROJECT_PARENT).getAsString();
            ISpaceProject project = SpaceProjectManager.getProject(projectName);
            ISP_Upgrade upgrade = project.getUpgrade(obj.get(UPGRADE_NAME).getAsString());
            if (upgrade == null) {
                return null;
            }
            upgrade = upgrade.copy();
            upgrade.setUpgradeCurrentStage(obj.get(UPGRADE_CURRENT_STAGE).getAsInt());
            return upgrade;
        }
    }
}
