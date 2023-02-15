package gregtech.common.misc.spaceprojects;

import static gregtech.common.misc.spaceprojects.SpaceProjectManager.spaceTeamProjects;
import static gregtech.common.misc.spaceprojects.SpaceProjectManager.spaceTeams;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

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
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import gregtech.api.util.GT_ModHandler;
import gregtech.common.misc.spaceprojects.base.SpaceProject;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;

public class SpaceProjectWorldSavedData extends WorldSavedData {

    public static SpaceProjectWorldSavedData INSTANCE;

    private static final Gson GSON_READER = new GsonBuilder().serializeNulls()
            .registerTypeAdapter(Pair.class, new PairDeserializer())
            .registerTypeAdapter(ItemStack.class, new ItemStackDeserializer())
            .registerTypeAdapter(FluidStack.class, new FluidStackDeserializer()).create();

    private static final Gson GSON_WRITER = new GsonBuilder().serializeNulls()
            .registerTypeAdapter(Pair.class, new PairSerializer())
            .registerTypeAdapter(ItemStack.class, new ItemStackSerializer())
            .registerTypeAdapter(FluidStack.class, new FluidStackSerializer()).create();

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
        Type teamProjectsType = new TypeToken<Map<UUID, Map<Pair<ISpaceBody, String>, SpaceProject>>>() {}.getType();
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
            if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                pair = Pair.of(
                        SpaceProjectManager.getLocation(obj.get("left").getAsString()),
                        obj.get("right").getAsString());
            }
            return pair;
        }
    }

    private static class ItemStackSerializer implements JsonSerializer<ItemStack> {

        @Override
        public JsonElement serialize(ItemStack src, Type typeOfSrc, JsonSerializationContext context) {
            UniqueIdentifier identification = GameRegistry.findUniqueIdentifierFor(src.getItem());
            JsonObject itemStack = new JsonObject();
            itemStack.addProperty("modId", identification.modId);
            itemStack.addProperty("itemName", identification.name);
            itemStack.addProperty("stackSize", src.stackSize);
            itemStack.addProperty("meta", src.getItemDamage());
            return itemStack;
        }
    }

    private static class ItemStackDeserializer implements JsonDeserializer<ItemStack> {

        @Override
        public ItemStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            ItemStack itemStack = null;
            if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                itemStack = GT_ModHandler.getModItem(
                        obj.get("modId").getAsString(),
                        obj.get("itemName").getAsString(),
                        obj.get("stackSize").getAsLong(),
                        obj.get("meta").getAsInt());
            }
            return itemStack;
        }
    }

    private static class FluidStackSerializer implements JsonSerializer<FluidStack> {

        @Override
        public JsonElement serialize(FluidStack src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject fluidStack = new JsonObject();
            fluidStack.addProperty("fluidName", src.getFluid().getName());
            fluidStack.addProperty("amount", src.amount);
            return fluidStack;
        }
    }

    private static class FluidStackDeserializer implements JsonDeserializer<FluidStack> {

        @Override
        public FluidStack deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            FluidStack fluidStack = null;
            if (json.isJsonObject()) {
                JsonObject obj = json.getAsJsonObject();
                fluidStack = new FluidStack(
                        FluidRegistry.getFluid(obj.get("fluidName").getAsString()),
                        obj.get("amount").getAsInt());
            }
            return fluidStack;
        }
    }
}
