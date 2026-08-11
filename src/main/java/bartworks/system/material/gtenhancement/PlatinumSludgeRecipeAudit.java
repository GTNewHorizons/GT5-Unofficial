package bartworks.system.material.gtenhancement;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidStack;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.mixin.interfaces.accessors.IRecipeMutableAccess;

/** Temporary audit used to verify the removal of {@link PlatinumSludgeOverHaul}. */
public final class PlatinumSludgeRecipeAudit {

    private static final Gson GSON = new Gson();
    private static final String OUTPUT_DIRECTORY = "platinum-sludge-overhaul";

    private PlatinumSludgeRecipeAudit() {}

    public static void run(Runnable overhaul) {
        Snapshot before = capture();
        overhaul.run();
        Snapshot after = capture();

        Path output = Loader.instance()
            .getConfigDir()
            .toPath()
            .getParent()
            .resolve("dumps")
            .resolve(OUTPUT_DIRECTORY);
        try {
            Files.createDirectories(output);
            write(output.resolve("before.jsonl"), before.records.values());
            write(output.resolve("after.jsonl"), after.records.values());
            write(output.resolve("changes.jsonl"), changes(before, after));
        } catch (IOException e) {
            throw new IllegalStateException("Failed to dump Platinum Sludge Overhaul recipes", e);
        }
    }

    private static Snapshot capture() {
        Snapshot snapshot = new Snapshot();
        IdentityHashMap<Object, Map<String, Integer>> occurrences = new IdentityHashMap<>();

        for (RecipeMap<?> map : RecipeMap.ALL_RECIPE_MAPS.values()) {
            for (GTRecipe recipe : map.getAllRecipes()) {
                add(snapshot, occurrences, "machine", map.unlocalizedName, recipe, machineRecipe(map, recipe));
            }
        }

        for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.smelting()
            .getSmeltingList()
            .entrySet()) {
            JsonObject record = record(
                "furnace",
                null,
                entry.getValue() == null ? null
                    : entry.getValue()
                        .getClass());
            record.add("input", item(entry.getKey()));
            record.add("output", item(entry.getValue()));
            add(snapshot, occurrences, "furnace", "", entry.getKey(), record);
        }

        for (Object candidate : CraftingManager.getInstance()
            .getRecipeList()) {
            if (candidate instanceof IRecipe recipe) {
                add(snapshot, occurrences, "crafting", "", recipe, craftingRecipe("crafting", recipe));
            }
        }

        for (IRecipe recipe : GTModHandler.sBufferRecipeList) {
            add(snapshot, occurrences, "gt-crafting-buffer", "", recipe, craftingRecipe("gt-crafting-buffer", recipe));
        }

        return snapshot;
    }

    private static JsonObject machineRecipe(RecipeMap<?> map, GTRecipe recipe) {
        JsonObject record = record("machine", map.unlocalizedName, recipe.getClass());
        record.addProperty(
            "category",
            recipe.getRecipeCategory() == null ? null : recipe.getRecipeCategory().unlocalizedName);
        record.add("itemInputs", items(recipe.mInputs));
        record.add("itemOutputs", items(recipe.mOutputs));
        record.add("fluidInputs", fluids(recipe.mFluidInputs));
        record.add("fluidOutputs", fluids(recipe.mFluidOutputs));
        record.add("itemInputChances", ints(recipe.mInputChances));
        record.add("itemOutputChances", ints(recipe.mOutputChances));
        record.add("fluidInputChances", ints(recipe.mFluidInputChances));
        record.add("fluidOutputChances", ints(recipe.mFluidOutputChances));
        record.addProperty("duration", recipe.mDuration);
        record.addProperty("eut", recipe.mEUt);
        record.addProperty("specialValue", recipe.mSpecialValue);
        record.addProperty("enabled", recipe.mEnabled);
        record.addProperty("hidden", recipe.mHidden);
        record.addProperty("fake", recipe.mFakeRecipe);
        record.addProperty("canBeBuffered", recipe.mCanBeBuffered);
        record.addProperty("needsEmptyOutput", recipe.mNeedsEmptyOutput);
        record.addProperty("nbtSensitive", recipe.isNBTSensitive);
        record.add("fluidInputAlternatives", fluidAlternatives(recipe.mAltFluidInputs));
        if (recipe instanceof GTRecipe.GTRecipe_WithAlt withAlt) {
            record.add("oreDictAlternatives", itemAlternatives(withAlt.mOreDictAlt));
            record.add("oreDictIds", ints(withAlt.mOreDictIds));
        }
        return record;
    }

    private static JsonObject craftingRecipe(String registry, IRecipe recipe) {
        JsonObject record = record(registry, null, recipe.getClass());
        record.add("output", item(recipe.getRecipeOutput()));
        if (recipe instanceof IRecipeMutableAccess access) {
            record.add("inputs", ingredient(access.gt5u$getRecipeInputs(), false));
        } else {
            record.addProperty("inputAccess", "unsupported");
        }
        return record;
    }

    private static JsonObject record(String registry, String map, Class<?> recipeClass) {
        JsonObject record = new JsonObject();
        record.addProperty("registry", registry);
        if (map != null) record.addProperty("map", map);
        if (recipeClass != null) record.addProperty("recipeClass", recipeClass.getName());
        return record;
    }

    private static JsonElement ingredient(Object value, boolean sortList) {
        if (value == null) return JsonNull.INSTANCE;
        if (value instanceof ItemStack stack) return item(stack);
        if (value instanceof Object[]array) {
            JsonArray result = new JsonArray();
            for (Object entry : array) result.add(ingredient(entry, entry instanceof List<?>));
            return result;
        }
        if (value instanceof List<?>list) {
            List<JsonElement> entries = new ArrayList<>(list.size());
            for (Object entry : list) entries.add(ingredient(entry, false));
            if (sortList) entries.sort(
                (left, right) -> GSON.toJson(left)
                    .compareTo(GSON.toJson(right)));
            JsonArray result = new JsonArray();
            entries.forEach(result::add);
            return result;
        }
        if (value instanceof Number number) return GSON.toJsonTree(number);
        if (value instanceof Boolean bool) return GSON.toJsonTree(bool);
        if (value instanceof Character character) return GSON.toJsonTree(character);
        if (value instanceof String string) return GSON.toJsonTree(string);

        JsonObject unsupported = new JsonObject();
        unsupported.addProperty(
            "unsupportedClass",
            value.getClass()
                .getName());
        return unsupported;
    }

    private static JsonElement item(ItemStack stack) {
        if (stack == null) return JsonNull.INSTANCE;
        JsonObject result = new JsonObject();
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(stack.getItem());
        result.addProperty(
            "id",
            identifier == null ? stack.getItem()
                .getClass()
                .getName() + ":"
                + Item.getIdFromItem(stack.getItem()) : identifier.toString());
        result.addProperty("damage", stack.getItemDamage());
        result.addProperty("amount", stack.stackSize);
        if (stack.hasTagCompound()) result.addProperty(
            "nbt",
            stack.getTagCompound()
                .toString());
        return result;
    }

    private static JsonElement fluid(FluidStack stack) {
        if (stack == null) return JsonNull.INSTANCE;
        JsonObject result = new JsonObject();
        result.addProperty(
            "id",
            stack.getFluid() == null ? null
                : stack.getFluid()
                    .getName());
        result.addProperty("amount", stack.amount);
        if (stack.tag != null) result.addProperty("nbt", stack.tag.toString());
        return result;
    }

    private static JsonArray items(ItemStack[] stacks) {
        JsonArray result = new JsonArray();
        if (stacks != null) Arrays.stream(stacks)
            .map(PlatinumSludgeRecipeAudit::item)
            .forEach(result::add);
        return result;
    }

    private static JsonArray fluids(FluidStack[] stacks) {
        JsonArray result = new JsonArray();
        if (stacks != null) Arrays.stream(stacks)
            .map(PlatinumSludgeRecipeAudit::fluid)
            .forEach(result::add);
        return result;
    }

    private static JsonElement ints(int[] values) {
        if (values == null) return JsonNull.INSTANCE;
        JsonArray result = new JsonArray();
        Arrays.stream(values)
            .forEach(value -> result.add(new JsonPrimitive(value)));
        return result;
    }

    private static JsonElement itemAlternatives(ItemStack[][] alternatives) {
        if (alternatives == null) return JsonNull.INSTANCE;
        JsonArray result = new JsonArray();
        for (ItemStack[] alternative : alternatives) result.add(sorted(items(alternative)));
        return result;
    }

    private static JsonElement fluidAlternatives(FluidStack[][] alternatives) {
        if (alternatives == null) return JsonNull.INSTANCE;
        JsonArray result = new JsonArray();
        for (FluidStack[] alternative : alternatives) result.add(sorted(fluids(alternative)));
        return result;
    }

    private static JsonArray sorted(JsonArray values) {
        List<JsonElement> sorted = new ArrayList<>();
        values.forEach(sorted::add);
        sorted.sort(
            (left, right) -> GSON.toJson(left)
                .compareTo(GSON.toJson(right)));
        JsonArray result = new JsonArray();
        sorted.forEach(result::add);
        return result;
    }

    private static void add(Snapshot snapshot, IdentityHashMap<Object, Map<String, Integer>> occurrences,
        String registry, String location, Object identity, JsonObject record) {
        Map<String, Integer> identityOccurrences = occurrences.computeIfAbsent(identity, ignored -> new HashMap<>());
        String scope = registry + '\0' + location;
        int occurrence = identityOccurrences.merge(scope, 1, Integer::sum) - 1;
        snapshot.records.put(new IdentityKey(registry, location, identity, occurrence), record);
    }

    private static List<JsonObject> changes(Snapshot before, Snapshot after) {
        Set<IdentityKey> keys = new HashSet<>(before.records.keySet());
        keys.addAll(after.records.keySet());

        List<JsonObject> changes = new ArrayList<>();
        for (IdentityKey key : keys) {
            JsonObject oldRecord = before.records.get(key);
            JsonObject newRecord = after.records.get(key);
            if (Objects.equals(oldRecord, newRecord)) continue;

            JsonObject change = new JsonObject();
            change.addProperty("action", oldRecord == null ? "added" : newRecord == null ? "removed" : "changed");
            change.addProperty("registry", key.registry);
            if (!key.location.isEmpty()) change.addProperty("map", key.location);
            if (oldRecord != null) change.add("before", oldRecord);
            if (newRecord != null) change.add("after", newRecord);
            changes.add(change);
        }
        return changes;
    }

    private static void write(Path path, Iterable<JsonObject> records) throws IOException {
        List<String> lines = new ArrayList<>();
        records.forEach(record -> lines.add(GSON.toJson(record)));
        lines.sort(String::compareTo);
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    private static final class Snapshot {

        private final Map<IdentityKey, JsonObject> records = new HashMap<>();
    }

    private static final class IdentityKey {

        private final String registry;
        private final String location;
        private final Object identity;
        private final int occurrence;

        private IdentityKey(String registry, String location, Object identity, int occurrence) {
            this.registry = registry;
            this.location = location;
            this.identity = identity;
            this.occurrence = occurrence;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof IdentityKey key && identity == key.identity
                && occurrence == key.occurrence
                && registry.equals(key.registry)
                && location.equals(key.location);
        }

        @Override
        public int hashCode() {
            return Objects.hash(registry, location, System.identityHashCode(identity), occurrence);
        }
    }
}
