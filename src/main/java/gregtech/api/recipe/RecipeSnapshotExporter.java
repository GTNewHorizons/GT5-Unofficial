package gregtech.api.recipe;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonWriter;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;
import gregtech.api.recipe.RecipeLookupValidator.RecipeLookupValidationTarget;
import gregtech.api.util.GTRecipe;

public final class RecipeSnapshotExporter {

    public static final String EXPORT_PROPERTY = "gt.recipe.snapshot.export";
    public static final int FORMAT_VERSION = 2;

    private static final Logger LOGGER = LogManager.getLogger("GregTech GTNH");
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping()
        .create();

    private RecipeSnapshotExporter() {}

    public static boolean shouldExportSnapshot() {
        String path = System.getProperty(EXPORT_PROPERTY);
        return path != null && !path.trim()
            .isEmpty();
    }

    public static void exportSnapshot() {
        exportSnapshot(RecipeMap.ALL_RECIPE_MAPS.values());
    }

    public static void exportSnapshot(Collection<? extends RecipeMap<?>> recipeMaps) {
        Path outputPath = exportPath();
        RecipeSnapshot snapshot = buildSnapshot(recipeMaps);
        snapshot.gregtechVersion = gregtechVersion();
        writeSnapshot(outputPath, snapshot);
        LOGGER.info(
            "GT recipe snapshot: wrote {} map(s), {} retention instance(s), {} raw instance(s), {} unique key(s) to {}",
            snapshot.summary.mapCount,
            snapshot.summary.instanceCount,
            snapshot.summary.rawInstanceCount,
            snapshot.summary.uniqueKeyCount,
            outputPath.toAbsolutePath());
    }

    static RecipeSnapshot buildSnapshot(Collection<? extends RecipeMap<?>> recipeMaps) {
        List<RecipeLookupValidationTarget> targets = new ArrayList<>();
        for (RecipeMap<?> recipeMap : recipeMaps) {
            targets.add(
                new RecipeLookupValidationTarget(
                    recipeMapName(recipeMap),
                    recipeMap.getBackend(),
                    recipeMap.getBackend()
                        .doesOverwriteFindRecipe()));
        }
        return buildSnapshot(targets);
    }

    static RecipeSnapshot buildSnapshot(List<RecipeLookupValidationTarget> targets) {
        TreeMap<String, MapSnapshot> maps = new TreeMap<>();
        int instanceCount = 0;
        int uniqueKeyCount = 0;
        int rawInstanceCount = 0;
        int skippedDisabledRecipes = 0;
        int skippedFakeRecipes = 0;
        int skippedNoInputRecipes = 0;

        for (RecipeLookupValidationTarget target : targets) {
            MapSnapshot mapSnapshot = new MapSnapshot();
            TreeMap<String, KeyEntry> keys = new TreeMap<>();
            List<InstanceEntry> instances = new ArrayList<>();
            mapSnapshot.customLookup = target.customLookup;

            int index = 0;
            for (GTRecipe recipe : target.backend.allRecipes()) {
                rawInstanceCount++;
                boolean enabled = recipe.mEnabled;
                boolean fake = recipe.mFakeRecipe;
                boolean hasInputs = hasInputs(recipe);

                String canonical = RecipeLookupValidator.describeRecipeCanonical(recipe);
                String gameplay = RecipeSnapshotDiff.gameplayFingerprint(canonical);
                InstanceEntry instanceEntry = new InstanceEntry();
                instanceEntry.index = index++;
                instanceEntry.enabled = enabled;
                instanceEntry.fake = fake;
                instanceEntry.hasInputs = hasInputs;
                instanceEntry.gameplay = gameplay;
                instanceEntry.canonical = canonical;
                instanceEntry.raw = RecipeLookupValidator.describeRecipeForValidation(recipe);
                instances.add(instanceEntry);

                if (!enabled) {
                    skippedDisabledRecipes++;
                    continue;
                }
                if (fake) {
                    skippedFakeRecipes++;
                    continue;
                }
                if (!hasInputs) {
                    skippedNoInputRecipes++;
                    continue;
                }

                String keyHash = hashCanonical(canonical);
                KeyEntry entry = keys.get(keyHash);
                if (entry == null) {
                    entry = new KeyEntry();
                    entry.canonical = canonical;
                    entry.count = 0;
                    keys.put(keyHash, entry);
                }
                entry.count++;
                mapSnapshot.instanceCount++;
            }

            mapSnapshot.uniqueKeyCount = keys.size();
            mapSnapshot.keys = keys;
            mapSnapshot.instances = instances;
            maps.put(target.mapName, mapSnapshot);
            instanceCount += mapSnapshot.instanceCount;
            uniqueKeyCount += mapSnapshot.uniqueKeyCount;
        }

        Summary summary = new Summary();
        summary.mapCount = maps.size();
        summary.instanceCount = instanceCount;
        summary.rawInstanceCount = rawInstanceCount;
        summary.uniqueKeyCount = uniqueKeyCount;
        summary.skippedDisabledRecipes = skippedDisabledRecipes;
        summary.skippedFakeRecipes = skippedFakeRecipes;
        summary.skippedNoInputRecipes = skippedNoInputRecipes;

        RecipeSnapshot snapshot = new RecipeSnapshot();
        snapshot.formatVersion = FORMAT_VERSION;
        snapshot.exportedAt = Instant.now()
            .toString();
        snapshot.maps = maps;
        snapshot.summary = summary;
        return snapshot;
    }

    private static void writeSnapshot(Path outputPath, RecipeSnapshot snapshot) {
        try {
            Path parent = outputPath.toAbsolutePath()
                .getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            try (Writer writer = Files.newBufferedWriter(outputPath, StandardCharsets.UTF_8);
                JsonWriter jsonWriter = new JsonWriter(writer)) {
                jsonWriter.setIndent("  ");
                GSON.toJson(snapshot, RecipeSnapshot.class, jsonWriter);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write GT recipe snapshot to " + outputPath.toAbsolutePath(), e);
        }
    }

    private static Path exportPath() {
        return Paths.get(
            System.getProperty(EXPORT_PROPERTY)
                .trim());
    }

    private static String recipeMapName(RecipeMap<?> recipeMap) {
        return recipeMap == null ? "<unbound>" : recipeMap.unlocalizedName;
    }

    private static String gregtechVersion() {
        try {
            ModContainer container = Loader.instance()
                .getIndexedModList()
                .get("gregtech");
            return container == null ? "<unknown>" : container.getVersion();
        } catch (LinkageError | RuntimeException e) {
            return "<unavailable>";
        }
    }

    private static boolean hasInputs(GTRecipe recipe) {
        if (recipe.mInputs != null) {
            for (ItemStack item : recipe.mInputs) {
                if (item != null) {
                    return true;
                }
            }
        }
        if (recipe.mFluidInputs != null) {
            for (FluidStack fluid : recipe.mFluidInputs) {
                if (fluid != null && fluid.getFluid() != null) {
                    return true;
                }
            }
        }
        return false;
    }

    static String hashCanonical(String canonical) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(canonical.getBytes(StandardCharsets.UTF_8));
            StringBuilder builder = new StringBuilder(hash.length * 2);
            for (byte value : hash) {
                builder.append(String.format("%02x", value & 0xff));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 is unavailable", e);
        }
    }

    static final class RecipeSnapshot {

        int formatVersion;
        String exportedAt;
        String gregtechVersion;
        TreeMap<String, MapSnapshot> maps;
        Summary summary;
    }

    static final class MapSnapshot {

        int instanceCount;
        int uniqueKeyCount;
        boolean customLookup;
        TreeMap<String, KeyEntry> keys;
        List<InstanceEntry> instances;
    }

    static final class InstanceEntry {

        int index;
        boolean enabled;
        boolean fake;
        boolean hasInputs;
        String gameplay;
        String canonical;
        String raw;
    }

    static final class KeyEntry {

        int count;
        String canonical;
    }

    static final class Summary {

        int mapCount;
        int instanceCount;
        int rawInstanceCount;
        int uniqueKeyCount;
        int skippedDisabledRecipes;
        int skippedFakeRecipes;
        int skippedNoInputRecipes;
    }

    /**
     * Order-independent aggregation for unit tests.
     */
    static Map<String, Integer> aggregateKeyCounts(RecipeSnapshot snapshot) {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (MapSnapshot mapSnapshot : snapshot.maps.values()) {
            for (Map.Entry<String, KeyEntry> entry : mapSnapshot.keys.entrySet()) {
                counts.merge(entry.getKey(), entry.getValue().count, Integer::sum);
            }
        }
        return Collections.unmodifiableMap(counts);
    }
}
