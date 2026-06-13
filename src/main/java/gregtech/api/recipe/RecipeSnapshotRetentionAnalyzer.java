package gregtech.api.recipe;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import gregtech.api.recipe.RecipeSnapshotDiff.DiffResult;
import gregtech.api.recipe.RecipeSnapshotDiff.LostEntry;

/**
 * Classifies every snapshot "lost" fingerprint as safe or unverified gameplay loss.
 */
public final class RecipeSnapshotRetentionAnalyzer {

    public static final String ANALYSIS_PROPERTY = "gt.recipe.snapshot.retention.analysis";
    public static final String ENFORCE_TRUE_LOSS_PROPERTY = "gt.recipe.snapshot.retention.enforce.true_loss";
    public static final String TRUE_LOSSES_PROPERTY = "gt.recipe.snapshot.retention.true_losses";
    public static final String OUTPUT_VARIANTS_PROPERTY = "gt.recipe.snapshot.retention.output_variants";
    public static final String EXPECTED_LOSSES_PROPERTY = "gt.recipe.snapshot.retention.expected_losses";

    private static final int REPORTED_UNVERIFIED_LIMIT = 128;

    private static final Logger LOGGER = LogManager.getLogger("GregTech GTNH");
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping()
        .setPrettyPrinting()
        .create();

    private RecipeSnapshotRetentionAnalyzer() {}

    public static AnalysisResult analyze(Path baselinePath, Path candidatePath, DiffResult diff) {
        return analyze(baselinePath, candidatePath, diff, null);
    }

    public static AnalysisResult analyze(Path baselinePath, Path candidatePath, DiffResult diff,
        @Nullable RecipeSnapshotNoiseCalibration.NoiseProfile noiseProfile) {
        return analyze(baselinePath, candidatePath, diff, noiseProfile, null);
    }

    public static AnalysisResult analyze(Path baselinePath, Path candidatePath, DiffResult diff,
        @Nullable RecipeSnapshotNoiseCalibration.NoiseProfile noiseProfile,
        @Nullable ExpectedLossProfile expectedLossProfile) {
        List<LostEntry> allLost = RecipeSnapshotDiff.allLostEntries(baselinePath, candidatePath);
        CandidateMatchIndex candidateIndex = CandidateMatchIndex.load(candidatePath);

        AnalysisResult result = new AnalysisResult();
        result.diffSummary = RecipeSnapshotDiff.describeSummary(diff);
        result.lostUniqueKeys = diff.lostRecipes;
        result.lostInstances = diff.lostInstances;
        result.dedupedInstances = diff.dedupedInstances;
        result.deduplicationConfirmed = diff.dedupedInstances;
        result.gainedUniqueKeys = diff.gainedUniqueKeys;
        result.lostByMap.putAll(diff.lostRecipesByMap);

        for (LostEntry lost : allLost) {
            String normalizedCanonical = normalizeForMatch(lost.canonical);
            String body = extractRecipeBody(normalizedCanonical);
            String core = extractGameplayCore(normalizedCanonical);
            ClassifiedLoss entry = new ClassifiedLoss();
            entry.mapName = lost.mapName;
            entry.baselineCount = lost.baselineCount;
            entry.baselineCanonical = lost.canonical;
            entry.gameplayFingerprint = lost.gameplayFingerprint;

            if (noiseProfile != null
                && RecipeSnapshotNoiseCalibration.isNoisy(noiseProfile, lost.mapName, lost.gameplayFingerprint)) {
                entry.classification = "baseline_noise";
                result.baselineNoise++;
                result.baselineNoiseByMap.merge(lost.mapName, 1, Integer::sum);
                if (result.classifiedLosses.size() < REPORTED_UNVERIFIED_LIMIT) {
                    result.classifiedLosses.add(entry);
                }
                continue;
            }

            String bodyMatch = candidateIndex.bodyMatch(lost.mapName, body);
            if (bodyMatch != null) {
                entry.classification = "equivalent_full_body";
                entry.candidateCanonical = bodyMatch;
                result.equivalentFullBody++;
            } else {
                String coreMatch = candidateIndex.coreMatch(lost.mapName, core);
                if (coreMatch != null) {
                    entry.classification = "equivalent_output_core";
                    entry.candidateCanonical = coreMatch;
                    result.equivalentOutputCore++;
                } else {
                    String inputs = extractRecipeInputs(normalizedCanonical);
                    String inputMatch = candidateIndex.inputMatch(lost.mapName, inputs);
                    if (inputMatch != null) {
                        entry.candidateCanonical = inputMatch;
                        if (noiseProfile != null && noiseProfile.outputVariantKeys.contains(
                            RecipeSnapshotNoiseCalibration.noiseKey(lost.mapName, lost.gameplayFingerprint))) {
                            entry.classification = "baseline_noise";
                            result.baselineNoise++;
                            result.baselineNoiseByMap.merge(lost.mapName, 1, Integer::sum);
                        } else {
                            entry.classification = "output_variant";
                            result.outputVariant++;
                            result.outputVariantsByMap.merge(lost.mapName, 1, Integer::sum);
                            if (result.outputVariants.size() < REPORTED_UNVERIFIED_LIMIT) {
                                result.outputVariants.add(entry);
                            }
                        }
                    } else if (isExpectedLoss(expectedLossProfile, lost)) {
                        entry.classification = "expected_loss";
                        entry.candidateCanonical = null;
                        result.expectedLoss++;
                        result.expectedLossesByMap.merge(lost.mapName, 1, Integer::sum);
                        if (result.expectedLosses.size() < REPORTED_UNVERIFIED_LIMIT) {
                            result.expectedLosses.add(entry);
                        }
                    } else {
                        entry.classification = "unverified_loss";
                        entry.candidateCanonical = null;
                        result.unverifiedLoss++;
                        result.unverifiedLossesByMap.merge(lost.mapName, 1, Integer::sum);
                        result.unverifiedLosses.add(entry);
                    }
                }
            }
            if (result.classifiedLosses.size() < REPORTED_UNVERIFIED_LIMIT) {
                result.classifiedLosses.add(entry);
            }
        }

        if (allLost.size() != diff.lostRecipes) {
            result.note = "lost entry count mismatch: diff=" + diff.lostRecipes + " collected=" + allLost.size();
        }
        return result;
    }

    private static boolean isExpectedLoss(@Nullable ExpectedLossProfile profile, LostEntry lost) {
        return profile != null && profile.expectedKeys.contains(expectedKey(lost.mapName, lost.gameplayFingerprint));
    }

    public static String expectedKey(String mapName, String gameplayFingerprint) {
        return mapName + "|" + gameplayFingerprint;
    }

    public static void validateNoUnverifiedLoss(AnalysisResult result) {
        validateNoGameplayRegression(result, null, null);
    }

    public static void validateNoGameplayRegression(AnalysisResult result, @Nullable Path baselinePath,
        @Nullable Path candidatePath) {
        if (result.unverifiedLoss <= 0 && result.outputVariant <= 0) {
            LOGGER.info(
                "GT recipe snapshot retention gate: passed with 0 unverified loss(es), 0 output variant(s), and {} confirmed deduplication(s) out of {} diff lost key(s).",
                result.deduplicationConfirmed,
                result.lostUniqueKeys);
            return;
        }
        StringBuilder message = new StringBuilder();
        if (result.unverifiedLoss > 0) {
            message.append("GT recipe snapshot retention gate found ")
                .append(result.unverifiedLoss)
                .append(" unverified gameplay loss(es)");
        }
        if (result.outputVariant > 0) {
            if (message.length() > 0) {
                message.append(" and ");
            } else {
                message.append("GT recipe snapshot retention gate found ");
            }
            message.append(result.outputVariant)
                .append(" output variant(s) (same inputs, different outputs/byproducts)");
        }
        message.append(" out of ")
            .append(result.lostUniqueKeys)
            .append(" diff lost key(s)");
        if (baselinePath != null && candidatePath != null) {
            message.append(" between baseline ")
                .append(baselinePath.toAbsolutePath())
                .append(" and candidate ")
                .append(candidatePath.toAbsolutePath());
        }
        message.append(". equivalentFullBody=")
            .append(result.equivalentFullBody)
            .append(", equivalentOutputCore=")
            .append(result.equivalentOutputCore)
            .append(", outputVariant=")
            .append(result.outputVariant)
            .append(", deduplicationConfirmed=")
            .append(result.deduplicationConfirmed)
            .append('.');
        if (!result.outputVariants.isEmpty()) {
            message.append("\nFirst ")
                .append(result.outputVariants.size())
                .append(" output variant(s):");
            for (int i = 0; i < result.outputVariants.size(); i++) {
                ClassifiedLoss entry = result.outputVariants.get(i);
                message.append('\n')
                    .append(i + 1)
                    .append(") map=")
                    .append(entry.mapName)
                    .append("\n  baseline: ")
                    .append(extractGameplayCore(entry.baselineCanonical))
                    .append("\n  candidate: ")
                    .append(extractGameplayCore(entry.candidateCanonical));
            }
            if (result.outputVariant > result.outputVariants.size()) {
                message.append("\n... and ")
                    .append(result.outputVariant - result.outputVariants.size())
                    .append(" more output variant(s).");
            }
        }
        if (!result.unverifiedLosses.isEmpty()) {
            message.append("\nFirst ")
                .append(result.unverifiedLosses.size())
                .append(" unverified loss(es):");
            for (int i = 0; i < result.unverifiedLosses.size(); i++) {
                ClassifiedLoss entry = result.unverifiedLosses.get(i);
                message.append('\n')
                    .append(i + 1)
                    .append(") map=")
                    .append(entry.mapName)
                    .append('\n')
                    .append(entry.baselineCanonical);
            }
            if (result.unverifiedLoss > result.unverifiedLosses.size()) {
                message.append("\n... and ")
                    .append(result.unverifiedLoss - result.unverifiedLosses.size())
                    .append(" more unverified loss(es).");
            }
        }
        throw new IllegalStateException(message.toString());
    }

    public static void writeAnalysis(Path analysisPath, AnalysisResult result) {
        try {
            if (analysisPath.getParent() != null) {
                Files.createDirectories(analysisPath.getParent());
            }
            Files.write(
                analysisPath,
                GSON.toJson(result)
                    .getBytes(StandardCharsets.UTF_8));
            LOGGER.info(
                "GT recipe snapshot retention analysis: lost={} fullBody={} outputCore={} sameInput={} deduplicationConfirmed={} unverified={} wrote {}",
                result.lostUniqueKeys,
                result.equivalentFullBody,
                result.equivalentOutputCore,
                result.outputVariant,
                result.deduplicationConfirmed,
                result.unverifiedLoss,
                analysisPath.toAbsolutePath());
        } catch (IOException e) {
            throw new IllegalStateException(
                "Failed to write recipe retention analysis to " + analysisPath.toAbsolutePath(),
                e);
        }
    }

    public static void writeOutputVariants(Path outputVariantsPath, AnalysisResult result) {
        OutputVariantReport report = new OutputVariantReport();
        report.lostUniqueKeys = result.lostUniqueKeys;
        report.outputVariant = result.outputVariant;
        report.deduplicationConfirmed = result.deduplicationConfirmed;
        report.outputVariantsByMap.putAll(result.outputVariantsByMap);
        report.outputVariants.addAll(result.outputVariants);
        try {
            if (outputVariantsPath.getParent() != null) {
                Files.createDirectories(outputVariantsPath.getParent());
            }
            Files.write(
                outputVariantsPath,
                GSON.toJson(report)
                    .getBytes(StandardCharsets.UTF_8));
            LOGGER.info(
                "GT recipe snapshot output-variant report: variants={} wrote {}",
                result.outputVariant,
                outputVariantsPath.toAbsolutePath());
        } catch (IOException e) {
            throw new IllegalStateException(
                "Failed to write output-variant report to " + outputVariantsPath.toAbsolutePath(),
                e);
        }
    }

    public static void writeTrueLosses(Path trueLossesPath, AnalysisResult result) {
        TrueLossReport report = new TrueLossReport();
        report.unverifiedLoss = result.unverifiedLoss;
        report.outputVariant = result.outputVariant;
        report.deduplicationConfirmed = result.deduplicationConfirmed;
        report.expectedLoss = result.expectedLoss;
        report.lostUniqueKeys = result.lostUniqueKeys;
        report.equivalentFullBody = result.equivalentFullBody;
        report.equivalentOutputCore = result.equivalentOutputCore;
        report.unverifiedLosses.addAll(result.unverifiedLosses);
        report.unverifiedLossesByMap.putAll(result.unverifiedLossesByMap);
        report.expectedLosses.addAll(result.expectedLosses);
        report.expectedLossesByMap.putAll(result.expectedLossesByMap);
        report.outputVariantsByMap.putAll(result.outputVariantsByMap);
        try {
            if (trueLossesPath.getParent() != null) {
                Files.createDirectories(trueLossesPath.getParent());
            }
            Files.write(
                trueLossesPath,
                GSON.toJson(report)
                    .getBytes(StandardCharsets.UTF_8));
            LOGGER.info(
                "GT recipe snapshot true-loss report: unverified={} wrote {}",
                result.unverifiedLoss,
                trueLossesPath.toAbsolutePath());
        } catch (IOException e) {
            throw new IllegalStateException(
                "Failed to write true-loss report to " + trueLossesPath.toAbsolutePath(),
                e);
        }
    }

    public static ExpectedLossProfile loadExpectedLosses(Path expectedLossesPath) {
        try (JsonReader reader = new JsonReader(Files.newBufferedReader(expectedLossesPath, StandardCharsets.UTF_8))) {
            ExpectedLossProfile profile = GSON.fromJson(reader, ExpectedLossProfile.class);
            if (profile == null || profile.expectedKeys == null) {
                throw new IllegalStateException(
                    "Expected loss profile is empty: " + expectedLossesPath.toAbsolutePath());
            }
            return profile;
        } catch (IOException e) {
            throw new IllegalStateException(
                "Failed to read expected loss profile from " + expectedLossesPath.toAbsolutePath(),
                e);
        }
    }

    static String normalizeForMatch(String canonical) {
        return RecipeSnapshotDiff.normalizeCanonicalForDiff(canonical);
    }

    static String extractRecipeBody(String canonical) {
        if (canonical == null) {
            return "";
        }
        int inputs = canonical.indexOf("inputs=[");
        if (inputs < 0) {
            return canonical;
        }
        return canonical.substring(inputs);
    }

    static String extractGameplayCore(String canonical) {
        if (canonical == null) {
            return "";
        }
        int outputs = canonical.indexOf("outputs=[");
        if (outputs < 0) {
            return canonical;
        }
        return canonical.substring(outputs);
    }

    static String extractRecipeInputs(String canonical) {
        if (canonical == null) {
            return "";
        }
        int inputs = canonical.indexOf("inputs=[");
        int outputs = canonical.indexOf("outputs=[");
        if (inputs < 0 || outputs < 0 || outputs <= inputs) {
            return "";
        }
        return canonical.substring(inputs, outputs);
    }

    private static final class CandidateMatchIndex {

        private final Map<String, Map<String, String>> bodiesByMap = new TreeMap<>();
        private final Map<String, Map<String, String>> coresByMap = new TreeMap<>();
        private final Map<String, Map<String, String>> inputsByMap = new TreeMap<>();

        @Nullable
        String bodyMatch(String mapName, String body) {
            Map<String, String> bodies = bodiesByMap.get(mapName);
            if (bodies == null) {
                return null;
            }
            return bodies.get(body);
        }

        @Nullable
        String coreMatch(String mapName, String core) {
            Map<String, String> cores = coresByMap.get(mapName);
            if (cores == null) {
                return null;
            }
            return cores.get(core);
        }

        @Nullable
        String inputMatch(String mapName, String inputs) {
            Map<String, String> inputsIndex = inputsByMap.get(mapName);
            if (inputsIndex == null || inputs.isEmpty()) {
                return null;
            }
            return inputsIndex.get(inputs);
        }

        static CandidateMatchIndex load(Path path) {
            CandidateMatchIndex index = new CandidateMatchIndex();
            try (JsonReader reader = new JsonReader(Files.newBufferedReader(path, StandardCharsets.UTF_8))) {
                reader.beginObject();
                while (reader.hasNext()) {
                    if ("maps".equals(reader.nextName())) {
                        readMaps(reader, index);
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            } catch (IOException e) {
                throw new IllegalStateException("Failed to stream candidate snapshot from " + path.toAbsolutePath(), e);
            }
            return index;
        }

        private static void readMaps(JsonReader reader, CandidateMatchIndex index) throws IOException {
            reader.beginObject();
            while (reader.hasNext()) {
                String mapName = reader.nextName();
                Map<String, String> bodies = new LinkedHashMap<>();
                Map<String, String> cores = new LinkedHashMap<>();
                Map<String, String> inputs = new LinkedHashMap<>();
                reader.beginObject();
                while (reader.hasNext()) {
                    if ("keys".equals(reader.nextName())) {
                        readKeys(reader, bodies, cores, inputs);
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
                index.bodiesByMap.put(mapName, bodies);
                index.coresByMap.put(mapName, cores);
                index.inputsByMap.put(mapName, inputs);
            }
            reader.endObject();
        }

        private static void readKeys(JsonReader reader, Map<String, String> bodies, Map<String, String> cores,
            Map<String, String> inputs) throws IOException {
            reader.beginObject();
            while (reader.hasNext()) {
                reader.nextName();
                reader.beginObject();
                String canonical = null;
                while (reader.hasNext()) {
                    if ("canonical".equals(reader.nextName())) {
                        canonical = reader.nextString();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
                if (canonical == null) {
                    continue;
                }
                String normalizedCanonical = normalizeForMatch(canonical);
                String body = extractRecipeBody(normalizedCanonical);
                String core = extractGameplayCore(normalizedCanonical);
                String recipeInputs = extractRecipeInputs(normalizedCanonical);
                bodies.putIfAbsent(body, canonical);
                cores.putIfAbsent(core, canonical);
                inputs.putIfAbsent(recipeInputs, canonical);
            }
            reader.endObject();
        }
    }

    public static final class AnalysisResult {

        public String diffSummary;
        public int lostUniqueKeys;
        public int lostInstances;
        public int dedupedInstances;
        public int deduplicationConfirmed;
        public int gainedUniqueKeys;
        public int equivalentFullBody;
        public int equivalentOutputCore;
        public int outputVariant;
        public int baselineNoise;
        public int unverifiedLoss;
        public int expectedLoss;
        public String note;
        public final List<ClassifiedLoss> classifiedLosses = new ArrayList<>();
        public final List<ClassifiedLoss> unverifiedLosses = new ArrayList<>();
        public final List<ClassifiedLoss> expectedLosses = new ArrayList<>();
        public final List<ClassifiedLoss> outputVariants = new ArrayList<>();
        public final Map<String, Integer> lostByMap = new TreeMap<>();
        public final Map<String, Integer> unverifiedLossesByMap = new TreeMap<>();
        public final Map<String, Integer> expectedLossesByMap = new TreeMap<>();
        public final Map<String, Integer> outputVariantsByMap = new TreeMap<>();
        public final Map<String, Integer> baselineNoiseByMap = new TreeMap<>();
    }

    public static final class ExpectedLossProfile {

        public final Set<String> expectedKeys = new HashSet<>();
        public final List<ClassifiedLoss> expectedLosses = new ArrayList<>();
    }

    static final class OutputVariantReport {

        int lostUniqueKeys;
        int outputVariant;
        int deduplicationConfirmed;
        final Map<String, Integer> outputVariantsByMap = new TreeMap<>();
        final List<ClassifiedLoss> outputVariants = new ArrayList<>();
    }

    static final class TrueLossReport {

        int lostUniqueKeys;
        int equivalentFullBody;
        int equivalentOutputCore;
        int outputVariant;
        int deduplicationConfirmed;
        int unverifiedLoss;
        int expectedLoss;
        final Map<String, Integer> unverifiedLossesByMap = new TreeMap<>();
        final Map<String, Integer> expectedLossesByMap = new TreeMap<>();
        final Map<String, Integer> outputVariantsByMap = new TreeMap<>();
        final List<ClassifiedLoss> unverifiedLosses = new ArrayList<>();
        final List<ClassifiedLoss> expectedLosses = new ArrayList<>();
    }

    public static final class ClassifiedLoss {

        public String mapName;
        public String classification;
        public int baselineCount;
        public String gameplayFingerprint;
        public String baselineCanonical;
        public String candidateCanonical;
    }
}
