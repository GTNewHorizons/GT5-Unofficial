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

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.Fluid;

import org.apache.commons.lang3.tuple.Pair;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.InstanceCreator;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonWriter;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.common.misc.spaceprojects.base.SpaceProject;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;

public class SpaceProjectWorldSavedData extends WorldSavedData {

    public static SpaceProjectWorldSavedData INSTANCE;

    private static final Gson GSON_READER = new GsonBuilder().serializeNulls()
            .registerTypeAdapter(EnumRarity.class, new EnumRarityDeserializer())
            .registerTypeAdapter(OrePrefixes.class, new OrePrefixesDeserializer())
            .registerTypeAdapter(Pair.class, new PairDeserializer())
            .setExclusionStrategies(
                    new CustomExclusion(CreativeTabs.class),
                    new CustomExclusion(IIcon.class),
                    new CustomExclusion(ResourceLocation.class),
                    new CustomExclusion(Item.class),
                    new CustomExclusion(Block.class),
                    new CustomExclusion(Fluid.class))
            .create();

    private static final Gson GSON_WRITER = new GsonBuilder().serializeNulls()
            .registerTypeAdapter(EnumRarity.class, new EnumRaritySerializer())
            .registerTypeAdapter(OrePrefixes.class, new OrePrefixesSerializer())
            .registerTypeAdapter(Pair.class, new PairSerializer())
            .setExclusionStrategies(
                    new CustomExclusion(CreativeTabs.class),
                    new CustomExclusion(IIcon.class),
                    new CustomExclusion(ResourceLocation.class),
                    new CustomExclusion(Item.class),
                    new CustomExclusion(Block.class),
                    new CustomExclusion(Fluid.class))
            .create();

    private static final String DATA_NAME = "GT_SpaceProjectData";

    private static final String SPACE_TEAM_PROJECTS = "GT_SpaceTeamProjectsNBT";

    private static final String SPACE_TEAMS = "GT_SpaceTeamsNBT";

    private static final String SPACE_TEAM_PROJECTS_JSON = "spaceTeamProject.json";

    private static final String SPACE_TEAMS_JSON = "spaceTeams.json";

    private static final Map<String, String> rarityMap = new HashMap<>();
    private static final Map<String, String> orePrefixMap = new HashMap<>();

    private static File mSpaceTeamsFile;
    private static File mTeamProjectsFile;

    public SpaceProjectWorldSavedData() {
        super(DATA_NAME);
    }

    public SpaceProjectWorldSavedData(String aData) {
        super(aData);
    }

    @Override
    public void readFromNBT(NBTTagCompound aNBT) {
        Type tTeamProjectsType = new TypeToken<Map<UUID, Map<Pair<ISpaceBody, String>, SpaceProject>>>() {}.getType();
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
        
        try (JsonWriter writer = new JsonWriter(new FileWriter(mTeamProjectsFile))) {
            GSON_WRITER.toJson(mSpaceTeamProjects, mSpaceTeamProjects.getClass(), writer);
        } catch (IOException ex) {
            System.out.print("FAILED TO SAVE: " + SPACE_TEAM_PROJECTS_JSON);
            ex.printStackTrace();
        }

        String tSpaceTeamProjectsJsonString = "";
        try (FileReader reader = new FileReader(mTeamProjectsFile); BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                tSpaceTeamProjectsJsonString += line;
            }
        } catch (IOException e) {
            System.out.print("FAILED TO SAVE: " + SPACE_TEAM_PROJECTS_JSON);
            e.printStackTrace();
        }
        aNBT.setString(SPACE_TEAM_PROJECTS, tSpaceTeamProjectsJsonString);

        
        try (JsonWriter writer = new JsonWriter(new FileWriter(mSpaceTeamsFile))) {
            GSON_WRITER.toJson(mSpaceTeams, mSpaceTeams.getClass(), writer);
        } catch (IOException ex) {
            System.out.print("FAILED TO SAVE: " + SPACE_TEAMS_JSON);
            ex.printStackTrace();
        }

        String tSpaceTeamsJsonString = "";
        try (FileReader reader = new FileReader(mSpaceTeamsFile); BufferedReader br = new BufferedReader(reader)) {
            String line;
            while ((line = br.readLine()) != null) {
                tSpaceTeamsJsonString += line;
            }
        } catch (IOException e) {
            System.out.print("FAILED TO SAVE: " + SPACE_TEAMS_JSON);
            e.printStackTrace();
        }

        aNBT.setString(SPACE_TEAMS, tSpaceTeamsJsonString);
    }

    private static void loadInstance(World aWorld) {
        mSpaceTeamProjects.clear();
        mSpaceTeams.clear();
        mSpaceTeamsFile = new File(aWorld.getSaveHandler().getWorldDirectory(), SPACE_TEAMS_JSON);
        mTeamProjectsFile = new File(aWorld.getSaveHandler().getWorldDirectory(), SPACE_TEAM_PROJECTS_JSON);
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

    private static class EnumRaritySerializer implements JsonSerializer<EnumRarity> {

        @Override
        public JsonElement serialize(EnumRarity src, Type typeOfSrc, JsonSerializationContext context) {
            rarityMap.put(src.name(), src.toString());
            return new JsonPrimitive(src.name());
        }
    }

    private static class EnumRarityDeserializer implements JsonDeserializer<EnumRarity> {

        @Override
        public EnumRarity deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return EnumRarity.valueOf(rarityMap.get(json.getAsString()));
        }
    }

    private static class OrePrefixesSerializer implements JsonSerializer<OrePrefixes> {

        @Override
        public JsonElement serialize(OrePrefixes src, Type typeOfSrc, JsonSerializationContext context) {
            orePrefixMap.put(src.name(), src.toString());
            return new JsonPrimitive(src.name());
        }
    }

    private static class OrePrefixesDeserializer implements JsonDeserializer<OrePrefixes> {

        @Override
        public OrePrefixes deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            return OrePrefixes.valueOf(orePrefixMap.get(json.getAsString()));
        }
    }

    private static class PairSerializer implements JsonSerializer<Pair<ISpaceBody, String>> {

        @Override
        public JsonElement serialize(Pair<ISpaceBody, String> src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject pair = new JsonObject();
            pair.addProperty("left", src.getLeft().getName());
            pair.addProperty("right", src.getRight());
            return pair;
        }
    }

    private static class PairDeserializer implements JsonDeserializer<Pair<ISpaceBody, String>> {

        @Override
        public Pair<ISpaceBody, String> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            Pair<ISpaceBody, String> pair = null;
            if (json instanceof JsonObject) {
                JsonObject obj = json.getAsJsonObject();
                pair = Pair.of(
                        SpaceProjectManager.getLocation(obj.get("left").getAsString()),
                        obj.get("right").getAsString());
            }
            return pair;
        }
    }

    

    private static class CustomExclusion implements ExclusionStrategy {

        private final Class<?> typeToExclude;

        private CustomExclusion(Class<?> typeToSkip) {
            this.typeToExclude = typeToSkip;
        }

        @Override
        public boolean shouldSkipField(FieldAttributes f) {
            return false;
        }

        @Override
        public boolean shouldSkipClass(Class<?> clazz) {
            return (clazz == typeToExclude);
        }
    }
}
