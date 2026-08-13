package gregtech.api.util;

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
import java.util.function.Supplier;

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
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.mixin.interfaces.accessors.IRecipeMutableAccess;

import static gregtech.GTLoggers.GT_FML_LOGGER;

/**
 * Optional recipe-state dumper for auditing a recipe mutation.
 *
 * <p>
 * Usage: call {@link #run(String, String, Runnable)} around the mutation:
 * </p>
 *
 * <pre>
 * RecipeChangeAudit.run("audit-name", "Audit description", recipeLoader::applyChanges);
 * </pre>
 *
 * <p>
 * Writes {@code before.jsonl}, {@code after.jsonl}, {@code changes.jsonl}, and
 * {@code timing.json} below the instance {@code dumps/outputDirectory} directory.
 * The dumper is intentionally not invoked during normal startup.
 * </p>
 */
@SuppressWarnings("unused")
public final class RecipeChangeAudit {

    private static final Gson GSON = new Gson();

    private RecipeChangeAudit() {}

    public static void run(String outputDirectory, String description, Runnable change) {
        Snapshot before;
        try {
            before = capture();
        } catch (RuntimeException e) {
            GT_FML_LOGGER.error("Failed to capture recipes before {}; running change without dump", description);
            GT_FML_LOGGER.error(e);
            long started = System.nanoTime();
            try {
                change.run();
            } finally {
                writeTiming(outputDirectory, description, System.nanoTime() - started);
            }
            return;
        }

        long started = System.nanoTime();
        try {
            change.run();
        } finally {
            writeTiming(outputDirectory, description, System.nanoTime() - started);
        }

        try {
            Snapshot after = capture();
            Path output = outputPath(outputDirectory);
            Files.createDirectories(output);
            write(output.resolve("before.jsonl"), before.records.values());
            write(output.resolve("after.jsonl"), after.records.values());
            write(output.resolve("changes.jsonl"), changes(before, after));
        } catch (IOException | RuntimeException e) {
            GT_FML_LOGGER.error("Failed to dump {} recipes", description);
            GT_FML_LOGGER.error(e);
        }
    }

    private static void writeTiming(String outputDirectory, String description, long elapsedNanos) {
        try {
            Path output = outputPath(outputDirectory);
            Files.createDirectories(output);
            JsonObject timing = new JsonObject();
            timing.addProperty("description", description);
            timing.addProperty("elapsedNanos", elapsedNanos);
            timing.addProperty("elapsedMillis", elapsedNanos / 1_000_000.0);
            Files.writeString(output.resolve("timing.json"), GSON.toJson(timing), StandardCharsets.UTF_8);
        } catch (IOException | RuntimeException e) {
            GT_FML_LOGGER.error("Failed to dump {} timing", description);
            GT_FML_LOGGER.error(e);
        }
    }

    private static Path outputPath(String outputDirectory) {
        return Loader.instance()
            .getConfigDir()
            .toPath()
            .getParent()
            .resolve("dumps")
            .resolve(outputDirectory);
    }

    private static Snapshot capture() {
        Snapshot snapshot = new Snapshot();
        IdentityHashMap<Object, Map<String, Integer>> occurrences = new IdentityHashMap<>();

        for (RecipeMap<?> map : RecipeMap.ALL_RECIPE_MAPS.values()) {
            for (GTRecipe recipe : map.getAllRecipes()) {
                add(
                    snapshot,
                    occurrences,
                    "machine",
                    map.unlocalizedName,
                    recipe,
                    safely("machine", map.unlocalizedName, recipe.getClass(), () -> machineRecipe(map, recipe)));
            }
        }

        for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.smelting()
            .getSmeltingList()
            .entrySet()) {
            JsonObject record = safely("furnace", null, null, () -> {
                JsonObject furnace = record("furnace", null, null);
                furnace.add("input", item(entry.getKey()));
                furnace.add("output", item(entry.getValue()));
                return furnace;
            });
            addSemantic(snapshot, "furnace", "", GSON.toJson(record.get("input")), record);
        }

        for (Object candidate : CraftingManager.getInstance()
            .getRecipeList()) {
            if (candidate instanceof IRecipe recipe) {
                add(
                    snapshot,
                    occurrences,
                    "crafting",
                    "",
                    recipe,
                    safely("crafting", null, recipe.getClass(), () -> craftingRecipe("crafting", recipe)));
            }
        }

        for (IRecipe recipe : GTModHandler.sBufferRecipeList) {
            add(
                snapshot,
                occurrences,
                "gt-crafting-buffer",
                "",
                recipe,
                safely(
                    "gt-crafting-buffer",
                    null,
                    recipe.getClass(),
                    () -> craftingRecipe("gt-crafting-buffer", recipe)));
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

    private static JsonObject safely(String registry, String map, Class<?> recipeClass,
        Supplier<JsonObject> serializer) {
        try {
            return serializer.get();
        } catch (RuntimeException e) {
            JsonObject record = record(registry, map, recipeClass);
            record.addProperty(
                "serializationError",
                e.getClass()
                    .getName());
            return record;
        }
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
        Item stackItem = stack.getItem();
        if (stackItem == null) {
            result.addProperty("invalid", "null-item");
            result.addProperty("amount", stack.stackSize);
            return result;
        }
        GameRegistry.UniqueIdentifier identifier = GameRegistry.findUniqueIdentifierFor(stackItem);
        result.addProperty(
            "id",
            identifier == null ? stackItem.getClass()
                .getName() + ":"
                + Item.getIdFromItem(stackItem) : identifier.toString());
        result.addProperty("damage", stack.getItemDamage());
        result.addProperty("amount", stack.stackSize);
        ItemData association = GTOreDictUnificator.getAssociation(stack);
        if (association != null && association.mPrefix != null && association.mMaterial != null) {
            result.addProperty("prefix", association.mPrefix.getName());
            result.addProperty("material", association.mMaterial.mMaterial.mName);
        }
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
            .map(RecipeChangeAudit::item)
            .forEach(result::add);
        return result;
    }

    private static JsonArray fluids(FluidStack[] stacks) {
        JsonArray result = new JsonArray();
        if (stacks != null) Arrays.stream(stacks)
            .map(RecipeChangeAudit::fluid)
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
        snapshot.records.put(new IdentityKey(registry, location, identity, occurrence, false), record);
    }

    private static void addSemantic(Snapshot snapshot, String registry, String location, Object identity,
        JsonObject record) {
        snapshot.records.put(new IdentityKey(registry, location, identity, 0, true), record);
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
        private final boolean semanticIdentity;

        private IdentityKey(String registry, String location, Object identity, int occurrence,
            boolean semanticIdentity) {
            this.registry = registry;
            this.location = location;
            this.identity = identity;
            this.occurrence = occurrence;
            this.semanticIdentity = semanticIdentity;
        }

        @Override
        public boolean equals(Object other) {
            return other instanceof IdentityKey key && semanticIdentity == key.semanticIdentity
                && (semanticIdentity ? identity.equals(key.identity) : identity == key.identity)
                && occurrence == key.occurrence
                && registry.equals(key.registry)
                && location.equals(key.location);
        }

        @Override
        public int hashCode() {
            return Objects.hash(
                registry,
                location,
                semanticIdentity ? identity : System.identityHashCode(identity),
                occurrence,
                semanticIdentity);
        }
    }
}
