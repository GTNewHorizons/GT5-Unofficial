package gregtech.api.recipe;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import gregtech.api.recipe.RecipeSnapshotExporter.InstanceEntry;
import gregtech.api.recipe.RecipeSnapshotExporter.KeyEntry;
import gregtech.api.recipe.RecipeSnapshotExporter.MapSnapshot;
import gregtech.api.recipe.RecipeSnapshotExporter.RecipeSnapshot;

/**
 * Compares recipe snapshots to detect lost unique recipes after dedupe or registration changes.
 * Uses normalized canonical fingerprints so cross-run diff is not polluted by unstable metadata
 * (e.g. {@code Sievert@identity} from default {@code Object#toString()}).
 */
public final class RecipeSnapshotDiff {

    public static final String BASELINE_PROPERTY = "gt.recipe.snapshot.baseline";
    public static final String CANDIDATE_PROPERTY = "gt.recipe.snapshot.candidate";
    public static final String ENFORCE_PROPERTY = "gt.recipe.snapshot.retention.enforce";
    public static final String REPORT_PROPERTY = "gt.recipe.snapshot.retention.report";
    private static final int REPORTED_LOSS_LIMIT = 64;
    private static final int REPORTED_SAMPLE_LIMIT = 3;

    private static final Logger LOGGER = LogManager.getLogger("GregTech GTNH");
    private static final Gson GSON = new GsonBuilder().create();
    private static final Pattern METADATA_OBJECT_IDENTITY = Pattern.compile(",?\\s*[\\w_]+=[\\w.$]+@[0-9a-f]+");
    private static final Pattern SPECIAL_OBJECT_IDENTITY = Pattern.compile("special=[\\w.$]+@[0-9a-f]+");

    private RecipeSnapshotDiff() {}

    public static boolean shouldValidateRetention() {
        String baseline = System.getProperty(BASELINE_PROPERTY);
        return baseline != null && !baseline.trim()
            .isEmpty();
    }

    public static Path baselinePath() {
        return Paths.get(
            System.getProperty(BASELINE_PROPERTY)
                .trim());
    }

    public static RecipeSnapshot load(Path path) {
        try (Reader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
            RecipeSnapshot snapshot = GSON.fromJson(reader, RecipeSnapshot.class);
            if (snapshot == null || snapshot.maps == null) {
                throw new IllegalStateException("Recipe snapshot is empty or malformed: " + path.toAbsolutePath());
            }
            return snapshot;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read recipe snapshot from " + path.toAbsolutePath(), e);
        }
    }

    public static DiffResult compare(Path baselinePath, Path candidatePath) {
        ensureDistinctSnapshotPaths(baselinePath, candidatePath);
        Map<String, Map<String, MultisetEntry>> baselineIndex = loadMultisetIndexFromKeys(baselinePath);
        Map<String, Map<String, MultisetEntry>> candidateIndex = loadMultisetIndexFromKeys(candidatePath);
        return compareGameplayMultisetFromIndex(baselineIndex, candidateIndex);
    }

    /** Every lost gameplay fingerprint from a streamed snapshot compare (no report cap). */
    public static List<LostEntry> allLostEntries(Path baselinePath, Path candidatePath) {
        ensureDistinctSnapshotPaths(baselinePath, candidatePath);
        return collectAllLostEntries(loadMultisetIndexFromKeys(baselinePath), loadMultisetIndexFromKeys(candidatePath));
    }

    public static DiffResult compare(RecipeSnapshot baseline, RecipeSnapshot candidate) {
        requireCurrentFormat(baseline, "baseline");
        requireCurrentFormat(candidate, "candidate");
        return compareGameplayMultiset(baseline, candidate);
    }

    public static String gameplayFingerprint(String canonical) {
        return normalizeCanonicalForDiff(canonical);
    }

    public static void writeRetentionReport(Path reportPath, DiffResult result, int baselineFormatVersion,
        int candidateFormatVersion) {
        RetentionReport report = new RetentionReport();
        report.comparisonMode = result.comparisonMode;
        report.baselineFormatVersion = baselineFormatVersion;
        report.candidateFormatVersion = candidateFormatVersion;
        report.summary = describeSummary(result);
        report.lostRecipes = result.lostRecipes;
        report.lostInstances = result.lostInstances;
        report.dedupedInstances = result.dedupedInstances;
        report.gainedUniqueKeys = result.gainedUniqueKeys;
        report.gainedInstances = result.gainedInstances;
        report.lostRecipesByMap.putAll(result.lostRecipesByMap);
        report.lostEntries.addAll(result.lostEntries);
        report.dedupedEntries.addAll(result.dedupedEntries);
        report.gainedEntries.addAll(result.gainedEntries);
        try {
            Files.createDirectories(reportPath.getParent());
            Gson pretty = new GsonBuilder().disableHtmlEscaping()
                .setPrettyPrinting()
                .create();
            Files.write(
                reportPath,
                pretty.toJson(report)
                    .getBytes(StandardCharsets.UTF_8));
            LOGGER.info("GT recipe snapshot retention report: wrote {}", reportPath.toAbsolutePath());
        } catch (IOException e) {
            throw new IllegalStateException(
                "Failed to write recipe retention report to " + reportPath.toAbsolutePath(),
                e);
        }
    }

    private static void requireCurrentFormat(RecipeSnapshot snapshot, String role) {
        if (snapshot.formatVersion != RecipeSnapshotExporter.FORMAT_VERSION) {
            throw new IllegalStateException(
                "Recipe snapshot " + role
                    + " uses unsupported formatVersion="
                    + snapshot.formatVersion
                    + "; expected "
                    + RecipeSnapshotExporter.FORMAT_VERSION);
        }
    }

    private static DiffResult compareGameplayMultiset(RecipeSnapshot baseline, RecipeSnapshot candidate) {
        DiffResult result = compareGameplayMultisetFromIndex(
            indexByGameplayMultiset(baseline),
            indexByGameplayMultiset(candidate));
        result.comparisonMode = "gameplay-multiset";
        return result;
    }

    private static DiffResult compareGameplayMultisetFromIndex(Map<String, Map<String, MultisetEntry>> baselineIndex,
        Map<String, Map<String, MultisetEntry>> candidateIndex) {
        DiffResult result = new DiffResult();
        result.baselineUniqueKeys = countMultisetKeys(baselineIndex);
        result.candidateUniqueKeys = countMultisetKeys(candidateIndex);

        for (Map.Entry<String, Map<String, MultisetEntry>> mapEntry : baselineIndex.entrySet()) {
            String mapName = mapEntry.getKey();
            Map<String, MultisetEntry> baselineKeys = mapEntry.getValue();
            Map<String, MultisetEntry> candidateKeys = candidateIndex.getOrDefault(mapName, Collections.emptyMap());

            for (Map.Entry<String, MultisetEntry> keyEntry : baselineKeys.entrySet()) {
                String gameplay = keyEntry.getKey();
                MultisetEntry baselineEntry = keyEntry.getValue();
                MultisetEntry candidateEntry = candidateKeys.get(gameplay);
                int baselineCount = baselineEntry.count;
                int candidateCount = candidateEntry == null ? 0 : candidateEntry.count;

                if (candidateCount == 0) {
                    result.lostRecipes++;
                    result.lostInstances += baselineCount;
                    result.lostRecipesByMap.merge(mapName, 1, Integer::sum);
                    if (result.lostEntries.size() < REPORTED_LOSS_LIMIT) {
                        result.lostEntries.add(
                            new LostEntry(
                                mapName,
                                gameplay,
                                baselineEntry.canonical,
                                baselineCount,
                                0,
                                baselineEntry.sampleRaw));
                    }
                    continue;
                }

                result.retainedUniqueKeys++;
                result.retainedInstances += Math.min(baselineCount, candidateCount);
                if (candidateCount < baselineCount) {
                    int deduped = baselineCount - candidateCount;
                    result.dedupedInstances += deduped;
                    result.dedupedUniqueKeys++;
                    if (result.dedupedEntries.size() < REPORTED_LOSS_LIMIT) {
                        result.dedupedEntries.add(
                            new DedupedEntry(
                                mapName,
                                gameplay,
                                baselineEntry.canonical,
                                baselineCount,
                                candidateCount,
                                baselineEntry.sampleRaw));
                    }
                } else if (candidateCount > baselineCount) {
                    result.gainedInstances += candidateCount - baselineCount;
                }
            }
        }

        for (Map.Entry<String, Map<String, MultisetEntry>> mapEntry : candidateIndex.entrySet()) {
            String mapName = mapEntry.getKey();
            Map<String, MultisetEntry> candidateKeys = mapEntry.getValue();
            Map<String, MultisetEntry> baselineKeys = baselineIndex.getOrDefault(mapName, Collections.emptyMap());
            for (Map.Entry<String, MultisetEntry> keyEntry : candidateKeys.entrySet()) {
                if (!baselineKeys.containsKey(keyEntry.getKey())) {
                    MultisetEntry candidateEntry = keyEntry.getValue();
                    result.gainedUniqueKeys++;
                    result.gainedInstances += candidateEntry.count;
                    if (result.gainedEntries.size() < REPORTED_LOSS_LIMIT) {
                        result.gainedEntries.add(
                            new GainedEntry(
                                mapName,
                                keyEntry.getKey(),
                                candidateEntry.canonical,
                                candidateEntry.count,
                                candidateEntry.sampleRaw));
                    }
                }
            }
        }

        return result;
    }

    private static List<LostEntry> collectAllLostEntries(Map<String, Map<String, MultisetEntry>> baselineIndex,
        Map<String, Map<String, MultisetEntry>> candidateIndex) {
        List<LostEntry> all = new ArrayList<>();
        for (Map.Entry<String, Map<String, MultisetEntry>> mapEntry : baselineIndex.entrySet()) {
            String mapName = mapEntry.getKey();
            Map<String, MultisetEntry> baselineKeys = mapEntry.getValue();
            Map<String, MultisetEntry> candidateKeys = candidateIndex.getOrDefault(mapName, Collections.emptyMap());
            for (Map.Entry<String, MultisetEntry> keyEntry : baselineKeys.entrySet()) {
                MultisetEntry baselineEntry = keyEntry.getValue();
                MultisetEntry candidateEntry = candidateKeys.get(keyEntry.getKey());
                if (candidateEntry == null || candidateEntry.count == 0) {
                    all.add(
                        new LostEntry(
                            mapName,
                            keyEntry.getKey(),
                            baselineEntry.canonical,
                            baselineEntry.count,
                            0,
                            Collections.emptyList()));
                }
            }
        }
        return all;
    }

    public static void validateRetention(Path baselinePath, Path candidatePath) {
        ensureDistinctSnapshotPaths(baselinePath, candidatePath);
        DiffResult result = compare(baselinePath, candidatePath);
        maybeWriteReport(result, baselinePath, candidatePath);
        validateRetention(result, baselinePath, candidatePath);
    }

    private static void ensureDistinctSnapshotPaths(Path baselinePath, Path candidatePath) {
        Path normalizedBaseline = baselinePath.toAbsolutePath()
            .normalize();
        Path normalizedCandidate = candidatePath.toAbsolutePath()
            .normalize();
        try {
            if (Files.exists(normalizedBaseline) && Files.exists(normalizedCandidate)
                && Files.isSameFile(normalizedBaseline, normalizedCandidate)) {
                throw sameSnapshotPathException(normalizedBaseline, normalizedCandidate);
            }
        } catch (IOException e) {
            throw new IllegalStateException(
                "Failed to compare recipe snapshot paths: baseline " + normalizedBaseline
                    + ", candidate "
                    + normalizedCandidate,
                e);
        }
        if (normalizedBaseline.equals(normalizedCandidate)) {
            throw sameSnapshotPathException(normalizedBaseline, normalizedCandidate);
        }
    }

    private static IllegalStateException sameSnapshotPathException(Path baselinePath, Path candidatePath) {
        return new IllegalStateException(
            "Recipe snapshot baseline and candidate snapshot paths must be different: baseline "
                + baselinePath.toAbsolutePath()
                + ", candidate "
                + candidatePath.toAbsolutePath());
    }

    public static void validateRetention(RecipeSnapshot baseline, RecipeSnapshot candidate) {
        DiffResult result = compare(baseline, candidate);
        maybeWriteReport(result, RecipeSnapshotExporter.FORMAT_VERSION, RecipeSnapshotExporter.FORMAT_VERSION);
        validateRetention(result, null, null);
    }

    private static void maybeWriteReport(DiffResult result, Path baselinePath, Path candidatePath) {
        maybeWriteReport(result, RecipeSnapshotExporter.FORMAT_VERSION, RecipeSnapshotExporter.FORMAT_VERSION);
    }

    private static void maybeWriteReport(DiffResult result, int baselineFormatVersion, int candidateFormatVersion) {
        String reportProperty = System.getProperty(REPORT_PROPERTY);
        if (reportProperty == null || reportProperty.trim()
            .isEmpty()) {
            return;
        }
        writeRetentionReport(Paths.get(reportProperty.trim()), result, baselineFormatVersion, candidateFormatVersion);
    }

    private static Map<String, Map<String, MultisetEntry>> loadMultisetIndexFromKeys(Path path) {
        Map<String, Map<String, MultisetEntry>> index = new TreeMap<>();
        boolean sawFormatVersion = false;
        boolean sawMaps = false;
        try (JsonReader reader = new JsonReader(Files.newBufferedReader(path, StandardCharsets.UTF_8))) {
            reader.beginObject();
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "formatVersion" -> {
                        int formatVersion = reader.nextInt();
                        sawFormatVersion = true;
                        if (formatVersion != RecipeSnapshotExporter.FORMAT_VERSION) {
                            throw new IllegalStateException(
                                "Recipe snapshot " + path.toAbsolutePath()
                                    + " uses unsupported formatVersion="
                                    + formatVersion
                                    + "; expected "
                                    + RecipeSnapshotExporter.FORMAT_VERSION);
                        }
                    }
                    case "maps" -> {
                        sawMaps = true;
                        readMapKeysIntoIndex(reader, index);
                    }
                    default -> reader.skipValue();
                }
            }
            reader.endObject();
        } catch (IOException e) {
            throw new IllegalStateException("Failed to stream recipe snapshot keys from " + path.toAbsolutePath(), e);
        }
        if (!sawFormatVersion || !sawMaps) {
            throw new IllegalStateException(
                "Recipe snapshot " + path.toAbsolutePath() + " is missing current formatVersion/maps fields");
        }
        return index;
    }

    private static void readMapKeysIntoIndex(JsonReader reader, Map<String, Map<String, MultisetEntry>> index)
        throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            String mapName = reader.nextName();
            Map<String, MultisetEntry> keys = new LinkedHashMap<>();
            reader.beginObject();
            while (reader.hasNext()) {
                if ("keys".equals(reader.nextName())) {
                    readKeyEntriesIntoIndex(reader, keys);
                } else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            index.put(mapName, keys);
        }
        reader.endObject();
    }

    private static void readKeyEntriesIntoIndex(JsonReader reader, Map<String, MultisetEntry> keys) throws IOException {
        reader.beginObject();
        while (reader.hasNext()) {
            reader.nextName();
            reader.beginObject();
            String canonical = null;
            int count = 0;
            while (reader.hasNext()) {
                switch (reader.nextName()) {
                    case "canonical" -> canonical = reader.nextString();
                    case "count" -> count = reader.nextInt();
                    default -> reader.skipValue();
                }
            }
            reader.endObject();
            if (canonical == null || count <= 0) {
                continue;
            }
            String gameplay = gameplayFingerprint(canonical);
            MultisetEntry entry = keys.get(gameplay);
            if (entry == null) {
                entry = new MultisetEntry();
                entry.canonical = canonical;
                entry.sampleRaw = Collections.emptyList();
                keys.put(gameplay, entry);
            }
            entry.count += count;
        }
        reader.endObject();
    }

    private static void validateRetention(DiffResult result, @Nullable Path baselinePath,
        @Nullable Path candidatePath) {
        if (baselinePath != null && candidatePath != null) {
            logSummary(baselinePath, candidatePath, result);
        }
        if (result.lostRecipes > 0) {
            throw buildException(baselinePath, candidatePath, result);
        }
    }

    /**
     * Normalizes canonical recipe fingerprints for cross-run snapshot comparison.
     */
    public static String normalizeCanonicalForDiff(String canonical) {
        if (canonical == null) {
            return "";
        }
        String normalized = METADATA_OBJECT_IDENTITY.matcher(canonical)
            .replaceAll("");
        normalized = SPECIAL_OBJECT_IDENTITY.matcher(normalized)
            .replaceAll("special=null");
        normalized = normalized.replace("metadata=[,", "metadata=[");
        normalized = normalized.replace("metadata=[ ", "metadata=[");
        normalized = normalized.replace(", ]", "]");
        normalized = normalized.replace("[,", "[");
        normalized = sortRecipeList(normalized, "outputs=[", "], fluidOutputs=");
        normalized = sortRecipeList(normalized, "fluidOutputs=[", "], duration=");
        return normalized;
    }

    static String sortRecipeList(String canonical, String listPrefix, String listSuffix) {
        int prefixIndex = canonical.indexOf(listPrefix);
        if (prefixIndex < 0) {
            return canonical;
        }
        int contentStart = prefixIndex + listPrefix.length();
        int suffixIndex = canonical.indexOf(listSuffix, contentStart);
        if (suffixIndex < 0) {
            return canonical;
        }
        String inner = canonical.substring(contentStart, suffixIndex);
        if (inner.isEmpty()) {
            return canonical;
        }
        String[] items = inner.split(", ");
        Arrays.sort(items);
        return canonical.substring(0, contentStart) + String.join(", ", items) + canonical.substring(suffixIndex);
    }

    private static Map<String, Map<String, MultisetEntry>> indexByGameplayMultiset(RecipeSnapshot snapshot) {
        Map<String, Map<String, MultisetEntry>> index = new TreeMap<>();
        for (Map.Entry<String, MapSnapshot> mapEntry : snapshot.maps.entrySet()) {
            Map<String, MultisetEntry> keys = new LinkedHashMap<>();
            MapSnapshot mapSnapshot = mapEntry.getValue();
            if (mapSnapshot.instances != null) {
                for (InstanceEntry instance : mapSnapshot.instances) {
                    if (!instance.enabled || instance.fake || !instance.hasInputs) {
                        continue;
                    }
                    String gameplay = instance.gameplay != null ? instance.gameplay
                        : gameplayFingerprint(instance.canonical);
                    MultisetEntry entry = keys.get(gameplay);
                    if (entry == null) {
                        entry = new MultisetEntry();
                        entry.canonical = instance.canonical;
                        entry.sampleRaw = new ArrayList<>();
                        keys.put(gameplay, entry);
                    }
                    entry.count++;
                    if (instance.raw != null && entry.sampleRaw.size() < REPORTED_SAMPLE_LIMIT) {
                        entry.sampleRaw.add(instance.raw);
                    }
                }
            } else if (mapSnapshot.keys != null) {
                for (KeyEntry keyEntry : mapSnapshot.keys.values()) {
                    String gameplay = gameplayFingerprint(keyEntry.canonical);
                    MultisetEntry entry = keys.get(gameplay);
                    if (entry == null) {
                        entry = new MultisetEntry();
                        entry.canonical = keyEntry.canonical;
                        entry.sampleRaw = new ArrayList<>();
                        keys.put(gameplay, entry);
                    }
                    entry.count += keyEntry.count;
                }
            }
            index.put(mapEntry.getKey(), keys);
        }
        return index;
    }

    private static int countMultisetKeys(Map<String, Map<String, MultisetEntry>> index) {
        int count = 0;
        for (Map<String, MultisetEntry> keys : index.values()) {
            count += keys.size();
        }
        return count;
    }

    public static String describeSummary(DiffResult result) {
        StringBuilder builder = new StringBuilder();
        builder.append("mode=")
            .append(result.comparisonMode)
            .append(", retainedUniqueKeys=")
            .append(result.retainedUniqueKeys)
            .append(", lostUniqueKeys=")
            .append(result.lostRecipes)
            .append(", dedupedInstances=")
            .append(result.dedupedInstances)
            .append(", gainedUniqueKeys=")
            .append(result.gainedUniqueKeys)
            .append(", baselineUniqueKeys=")
            .append(result.baselineUniqueKeys)
            .append(", candidateUniqueKeys=")
            .append(result.candidateUniqueKeys);
        for (Map.Entry<String, Integer> entry : result.lostRecipesByMap.entrySet()) {
            builder.append("\n  lost ")
                .append(entry.getKey())
                .append('=')
                .append(entry.getValue());
        }
        return builder.toString();
    }

    private static void logSummary(Path baselinePath, Path candidatePath, DiffResult result) {
        LOGGER.info(
            "GT recipe snapshot retention: baseline={} candidate={} {}",
            baselinePath.toAbsolutePath(),
            candidatePath.toAbsolutePath(),
            describeSummary(result).replace('\n', ' '));
    }

    private static IllegalStateException buildException(@Nullable Path baselinePath, @Nullable Path candidatePath,
        DiffResult result) {
        StringBuilder message = new StringBuilder();
        message.append("GT recipe snapshot retention found ")
            .append(result.lostRecipes)
            .append(" lost unique recipe key(s) (")
            .append(result.lostInstances)
            .append(" instance(s))");
        if (baselinePath != null && candidatePath != null) {
            message.append(" between baseline ")
                .append(baselinePath.toAbsolutePath())
                .append(" and candidate ")
                .append(candidatePath.toAbsolutePath());
        }
        message.append(". retainedUniqueKeys=")
            .append(result.retainedUniqueKeys)
            .append(", dedupedInstances=")
            .append(result.dedupedInstances)
            .append(", gainedUniqueKeys=")
            .append(result.gainedUniqueKeys)
            .append('.');
        if (!result.lostEntries.isEmpty()) {
            message.append("\nFirst ")
                .append(result.lostEntries.size())
                .append(" lost recipe(s):");
            for (int i = 0; i < result.lostEntries.size(); i++) {
                LostEntry entry = result.lostEntries.get(i);
                message.append('\n')
                    .append(i + 1)
                    .append(") map=")
                    .append(entry.mapName)
                    .append(" baselineCount=")
                    .append(entry.baselineCount)
                    .append(" candidateCount=")
                    .append(entry.candidateCount)
                    .append('\n')
                    .append(entry.canonical);
                if (!entry.sampleRaw.isEmpty()) {
                    message.append("\n  raw sample: ")
                        .append(entry.sampleRaw.get(0));
                }
            }
            if (result.lostRecipes > result.lostEntries.size()) {
                message.append("\n... and ")
                    .append(result.lostRecipes - result.lostEntries.size())
                    .append(" more lost recipe key(s).");
            }
        }
        return new IllegalStateException(message.toString());
    }

    public static final class DiffResult {

        public String comparisonMode = "gameplay-multiset";
        public int baselineUniqueKeys;
        public int candidateUniqueKeys;
        public int retainedUniqueKeys;
        public int retainedInstances;
        public int lostRecipes;
        public int lostInstances;
        public int dedupedUniqueKeys;
        public int dedupedInstances;
        public int gainedUniqueKeys;
        public int gainedInstances;
        public final List<LostEntry> lostEntries = new ArrayList<>();
        public final List<DedupedEntry> dedupedEntries = new ArrayList<>();
        public final List<GainedEntry> gainedEntries = new ArrayList<>();
        public final Map<String, Integer> lostRecipesByMap = new TreeMap<>();
    }

    public static final class LostEntry {

        public final String mapName;
        public final String gameplayFingerprint;
        public final String canonical;
        public final int baselineCount;
        public final int candidateCount;
        public final List<String> sampleRaw;

        LostEntry(String mapName, String gameplayFingerprint, String canonical, int baselineCount, int candidateCount,
            List<String> sampleRaw) {
            this.mapName = mapName;
            this.gameplayFingerprint = gameplayFingerprint;
            this.canonical = canonical;
            this.baselineCount = baselineCount;
            this.candidateCount = candidateCount;
            this.sampleRaw = sampleRaw == null ? Collections.emptyList() : sampleRaw;
        }
    }

    public static final class DedupedEntry {

        public final String mapName;
        public final String gameplayFingerprint;
        public final String canonical;
        public final int baselineCount;
        public final int candidateCount;
        public final List<String> sampleRaw;

        DedupedEntry(String mapName, String gameplayFingerprint, String canonical, int baselineCount,
            int candidateCount, List<String> sampleRaw) {
            this.mapName = mapName;
            this.gameplayFingerprint = gameplayFingerprint;
            this.canonical = canonical;
            this.baselineCount = baselineCount;
            this.candidateCount = candidateCount;
            this.sampleRaw = sampleRaw == null ? Collections.emptyList() : sampleRaw;
        }
    }

    public static final class GainedEntry {

        public final String mapName;
        public final String gameplayFingerprint;
        public final String canonical;
        public final int candidateCount;
        public final List<String> sampleRaw;

        GainedEntry(String mapName, String gameplayFingerprint, String canonical, int candidateCount,
            List<String> sampleRaw) {
            this.mapName = mapName;
            this.gameplayFingerprint = gameplayFingerprint;
            this.canonical = canonical;
            this.candidateCount = candidateCount;
            this.sampleRaw = sampleRaw == null ? Collections.emptyList() : sampleRaw;
        }
    }

    private static final class MultisetEntry {

        int count;
        String canonical;
        List<String> sampleRaw;
    }

    static final class RetentionReport {

        String comparisonMode;
        int baselineFormatVersion;
        int candidateFormatVersion;
        String summary;
        int lostRecipes;
        int lostInstances;
        int dedupedInstances;
        int gainedUniqueKeys;
        int gainedInstances;
        final Map<String, Integer> lostRecipesByMap = new TreeMap<>();
        final List<LostEntry> lostEntries = new ArrayList<>();
        final List<DedupedEntry> dedupedEntries = new ArrayList<>();
        final List<GainedEntry> gainedEntries = new ArrayList<>();
    }
}
