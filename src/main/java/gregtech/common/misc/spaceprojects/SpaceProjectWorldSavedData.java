package gregtech.common.misc.spaceprojects;

import static gregtech.common.misc.spaceprojects.SpaceProjectManager.mSpaceTeamProjects;
import static gregtech.common.misc.spaceprojects.SpaceProjectManager.mSpaceTeams;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.item.EnumRarity;
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
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;

public class SpaceProjectWorldSavedData extends WorldSavedData {

    public static SpaceProjectWorldSavedData INSTANCE;

    private static final Gson GSON_READER = new GsonBuilder()
            .registerTypeAdapter(EnumRarity.class, new EnumRarityDeserializer()).create();

    private static final Gson GSON_WRITER = new GsonBuilder()
            .registerTypeAdapter(EnumRarity.class, new EnumRaritySerializer()).create();

    private static final String DATA_NAME = "GT_SpaceProjectData";

    private static final String SPACE_TEAM_PROJECTS = "GT_SpaceTeamProjectsNBT";

    private static final String SPACE_TEAMS = "GT_SpaceTeamsNBT";

    private static final String SPACE_TEAM_PROJECTS_JSON = "spaceTeamProject.json";

    private static final String SPACE_TEAMS_JSON = "spaceTeams.json";
    private static final Map<EnumRarity, String> rarityToName = new HashMap<>();

    private static final Map<String, EnumRarity> nameToRarity = new HashMap<>();

    private static World mWorld;

    public SpaceProjectWorldSavedData() {
        super(DATA_NAME);
    }

    public SpaceProjectWorldSavedData(String aData) {
        super(aData);
    }

    @Override
    public void readFromNBT(NBTTagCompound aNBT) {
        Type tTeamProjectsType = new TypeToken<Map<UUID, Map<Pair<ISpaceBody, String>, ISpaceProject>>>() {}.getType();
        mSpaceTeamProjects = GSON_READER.fromJson(aNBT.getString(SPACE_TEAM_PROJECTS), tTeamProjectsType);
        if (mSpaceTeamProjects == null) {
            mSpaceTeamProjects = new HashMap<>();
        }
        Type tTeamsType = new TypeToken<Map<UUID, UUID>>() {}.getType();
        mSpaceTeams = GSON_READER.fromJson(aNBT.getString(SPACE_TEAMS), tTeamsType);
        if (mSpaceTeams == null) {
            mSpaceTeams = new HashMap<>();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound aNBT) {
        File tTeamProjectsFile = new File(mWorld.getSaveHandler().getWorldDirectory(), SPACE_TEAM_PROJECTS_JSON);
        try (JsonWriter writer = new JsonWriter(new FileWriter(tTeamProjectsFile))) {
            GSON_WRITER.toJson(mSpaceTeamProjects, mSpaceTeamProjects.getClass(), writer);
        } catch (IOException ex) {
            System.out.print("FAILED TO SAVE: " + SPACE_TEAM_PROJECTS_JSON);
            ex.printStackTrace();
        }

        String tSpaceTeamProjectsJsonString = "";
        try (FileReader reader = new FileReader(tTeamProjectsFile); BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                tSpaceTeamProjectsJsonString += line;
            }
        } catch (IOException e) {
            System.out.print("FAILED TO SAVE: " + SPACE_TEAM_PROJECTS_JSON);
            e.printStackTrace();
        }
        aNBT.setString(SPACE_TEAM_PROJECTS, tSpaceTeamProjectsJsonString);

        File tSpaceTeamsFile = new File(mWorld.getSaveHandler().getWorldDirectory(), SPACE_TEAMS_JSON);
        try (JsonWriter writer = new JsonWriter(new FileWriter(tTeamProjectsFile))) {
            GSON_WRITER.toJson(mSpaceTeams, mSpaceTeams.getClass(), writer);
        } catch (IOException ex) {
            System.out.print("FAILED TO SAVE: " + SPACE_TEAMS_JSON);
            ex.printStackTrace();
        }

        String tSpaceTeamsJsonString = "";
        try (FileReader reader = new FileReader(tSpaceTeamsFile); BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                tSpaceTeamsJsonString += line;
            }
        } catch (IOException e) {
            System.out.print("FAILED TO SAVE: " + SPACE_TEAMS_JSON);
            e.printStackTrace();
        }

        aNBT.setString(SPACE_TEAMS, tSpaceTeamsJsonString);

        mWorld = null;
    }

    private static void loadInstance(World aWorld) {
        mSpaceTeamProjects.clear();
        mSpaceTeams.clear();
        MapStorage tStorage = aWorld.mapStorage;
        INSTANCE = (SpaceProjectWorldSavedData) tStorage.loadData(SpaceProjectWorldSavedData.class, DATA_NAME);
        if (INSTANCE == null) {
            INSTANCE = new SpaceProjectWorldSavedData();
            tStorage.setData(DATA_NAME, INSTANCE);
        }
        mWorld = aWorld;
        INSTANCE.markDirty();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load aEvent) {
        if (!aEvent.world.isRemote && aEvent.world.provider.dimensionId == 0) {
            loadInstance(aEvent.world);
        }
    }

    private static class EnumRaritySerializer implements JsonSerializer<EnumRarity> {

        @Override
        public JsonElement serialize(EnumRarity src, Type typeOfSrc, JsonSerializationContext context) {
            nameToRarity.put(src.name(), src);
            rarityToName.put(src, src.name());
            return new JsonPrimitive("a");
        }

    }

    private static class EnumRarityDeserializer implements JsonDeserializer<EnumRarity> {

        @Override
        public EnumRarity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return nameToRarity.get(json.getAsString());
        }

    }
}
