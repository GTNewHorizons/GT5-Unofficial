package gregtech.api.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import gregtech.api.recipe.RecipeSnapshotExporter.KeyEntry;
import gregtech.api.recipe.RecipeSnapshotExporter.MapSnapshot;
import gregtech.api.recipe.RecipeSnapshotExporter.RecipeSnapshot;

class RecipeSnapshotDiffTest {

    @TempDir
    Path tempDir;

    @Test
    void normalizeCanonicalStripsCropMutationIdentity() {
        String first = "category=cropsnh.recipes.cropBreeder, metadata=[cropsnh_crop_mutation=com.gtnewhorizon.cropsnh.farming.mutation.CropMutation@7bf009a8], inputs=[a]";
        String second = "category=cropsnh.recipes.cropBreeder, metadata=[cropsnh_crop_mutation=com.gtnewhorizon.cropsnh.farming.mutation.CropMutation@deadbeef], inputs=[a]";
        assertEquals(
            RecipeSnapshotDiff.normalizeCanonicalForDiff(first),
            RecipeSnapshotDiff.normalizeCanonicalForDiff(second));
    }

    @Test
    void normalizeCanonicalSortsOutputsAsMultiset() {
        String first = "category=gt.recipe.centrifuge, metadata=[], inputs=[a], fluidInputs=[], outputs=[paper x1, wax x1, beeswax x1], fluidOutputs=[], duration=128, eut=5, specialValue=0, special=null";
        String second = "category=gt.recipe.centrifuge, metadata=[], inputs=[a], fluidInputs=[], outputs=[wax x1, paper x1, beeswax x1], fluidOutputs=[], duration=128, eut=5, specialValue=0, special=null";
        assertEquals(
            RecipeSnapshotDiff.normalizeCanonicalForDiff(first),
            RecipeSnapshotDiff.normalizeCanonicalForDiff(second));
    }

    @Test
    void normalizeCanonicalStripsUnstableSpecialIdentity() {
        String first = "category=gt.recipe.eyeofharmony, metadata=[], inputs=[a], fluidInputs=[], outputs=[b], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=tectech.recipe.EyeOfHarmonyRecipe@aaaa";
        String second = "category=gt.recipe.eyeofharmony, metadata=[], inputs=[a], fluidInputs=[], outputs=[b], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=tectech.recipe.EyeOfHarmonyRecipe@bbbb";
        assertEquals(
            RecipeSnapshotDiff.normalizeCanonicalForDiff(first),
            RecipeSnapshotDiff.normalizeCanonicalForDiff(second));
    }

    @Test
    void normalizeCanonicalStripsLegacySievertIdentity() {
        String canonical = "category=bw.recipe.BacteriaVat, metadata=[GLASS=6, SIEVERT=gregtech.api.util.recipe.Sievert@26d16ab7], inputs=[a], fluidInputs=[], outputs=[], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null";
        assertEquals(
            "category=bw.recipe.BacteriaVat, metadata=[GLASS=6], inputs=[a], fluidInputs=[], outputs=[], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null",
            RecipeSnapshotDiff.normalizeCanonicalForDiff(canonical));
    }

    @Test
    void compareDetectsLostUniqueRecipe() {
        RecipeSnapshot baseline = snapshot("gt.recipe.macerator", entry("recipe-a", 1), entry("recipe-b", 1));
        RecipeSnapshot candidate = snapshot("gt.recipe.macerator", entry("recipe-a", 1));

        RecipeSnapshotDiff.DiffResult result = RecipeSnapshotDiff.compare(baseline, candidate);
        assertEquals(2, result.baselineUniqueKeys);
        assertEquals(1, result.candidateUniqueKeys);
        assertEquals(1, result.retainedUniqueKeys);
        assertEquals(1, result.lostRecipes);
        assertEquals(1, result.lostInstances);
    }

    @Test
    void compareTreatsDuplicateInstanceReductionAsDedupeNotLoss() {
        String canonical = "category=gt.recipe.macerator, metadata=[], inputs=[ore], fluidInputs=[], outputs=[dust], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null";
        RecipeSnapshot baseline = snapshot("gt.recipe.macerator", keyedEntry(canonical, 3));
        RecipeSnapshot candidate = snapshot("gt.recipe.macerator", keyedEntry(canonical, 1));

        RecipeSnapshotDiff.DiffResult result = RecipeSnapshotDiff.compare(baseline, candidate);
        assertEquals(0, result.lostRecipes);
        assertEquals(1, result.retainedUniqueKeys);
        assertEquals(2, result.dedupedInstances);
        assertEquals(1, result.dedupedUniqueKeys);
    }

    @Test
    void compareMatchesAcrossLegacySievertNoise() {
        String withLegacySievert = "category=bw.recipe.BacteriaVat, metadata=[GLASS=6, SIEVERT=gregtech.api.util.recipe.Sievert@aaaa], inputs=[x], fluidInputs=[], outputs=[], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null";
        String withOtherSievert = "category=bw.recipe.BacteriaVat, metadata=[GLASS=6, SIEVERT=gregtech.api.util.recipe.Sievert@bbbb], inputs=[x], fluidInputs=[], outputs=[], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null";
        RecipeSnapshot baseline = snapshot("bw.recipe.BacteriaVat", keyedEntry(withLegacySievert, 1));
        RecipeSnapshot candidate = snapshot("bw.recipe.BacteriaVat", keyedEntry(withOtherSievert, 1));

        RecipeSnapshotDiff.DiffResult result = RecipeSnapshotDiff.compare(baseline, candidate);
        assertEquals(0, result.lostRecipes);
        assertEquals(1, result.retainedUniqueKeys);
    }

    @Test
    void validateRetentionThrowsWhenRecipesAreLost() {
        RecipeSnapshot baseline = snapshot("gt.recipe.macerator", entry("lost-recipe", 1));
        RecipeSnapshot candidate = snapshot("gt.recipe.macerator");

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeSnapshotDiff.validateRetention(baseline, candidate));
        assertTrue(
            error.getMessage()
                .contains("lost unique recipe key"));
    }

    @Test
    void compareRejectsSameSnapshotPath() throws IOException {
        Path snapshot = tempDir.resolve("snapshot.json");
        Files.writeString(snapshot, minimalSnapshotJson(), StandardCharsets.UTF_8);

        IllegalStateException error = assertThrows(
            IllegalStateException.class,
            () -> RecipeSnapshotDiff.compare(snapshot, snapshot));

        assertTrue(
            error.getMessage()
                .contains("baseline and candidate snapshot paths must be different"));
    }

    @Test
    void canCompareRealSnapshotFilesWhenPathsAreProvided() {
        String baselineProperty = firstNonBlank(
            System.getenv("GT_RECIPE_SNAPSHOT_BASELINE"),
            System.getProperty(RecipeSnapshotDiff.BASELINE_PROPERTY));
        String candidateProperty = firstNonBlank(
            System.getenv("GT_RECIPE_SNAPSHOT_CANDIDATE"),
            System.getProperty(RecipeSnapshotDiff.CANDIDATE_PROPERTY));
        if (baselineProperty == null || candidateProperty == null) {
            return;
        }

        Path baselinePath = Paths.get(baselineProperty);
        Path candidatePath = Paths.get(candidateProperty);
        RecipeSnapshotDiff.DiffResult result = RecipeSnapshotDiff.compare(baselinePath, candidatePath);
        System.out.printf(
            "snapshot retention: mode=%s baselineUniqueKeys=%d candidateUniqueKeys=%d retained=%d lost=%d dedupedInstances=%d gainedUniqueKeys=%d%n",
            result.comparisonMode,
            result.baselineUniqueKeys,
            result.candidateUniqueKeys,
            result.retainedUniqueKeys,
            result.lostRecipes,
            result.dedupedInstances,
            result.gainedUniqueKeys);
        result.lostRecipesByMap.entrySet()
            .stream()
            .sorted((left, right) -> Integer.compare(right.getValue(), left.getValue()))
            .limit(20)
            .forEach(entry -> System.out.printf("lost by map: %s=%d%n", entry.getKey(), entry.getValue()));
        if (!result.lostEntries.isEmpty()) {
            RecipeSnapshotDiff.LostEntry first = result.lostEntries.get(0);
            System.out.println("first lost: map=" + first.mapName + " canonical=" + first.canonical);
        }
        String reportProperty = firstNonBlank(
            System.getenv("GT_RECIPE_SNAPSHOT_RETENTION_REPORT"),
            System.getProperty(RecipeSnapshotDiff.REPORT_PROPERTY));
        String analysisProperty = firstNonBlank(
            System.getenv("GT_RECIPE_SNAPSHOT_RETENTION_ANALYSIS"),
            System.getProperty(RecipeSnapshotRetentionAnalyzer.ANALYSIS_PROPERTY));
        String expectedLossesProperty = firstNonBlank(
            System.getenv("GT_RECIPE_SNAPSHOT_RETENTION_EXPECTED_LOSSES"),
            System.getProperty(RecipeSnapshotRetentionAnalyzer.EXPECTED_LOSSES_PROPERTY));
        RecipeSnapshotRetentionAnalyzer.ExpectedLossProfile expectedLosses = loadExpectedLossProfile(
            expectedLossesProperty);
        if (reportProperty != null) {
            System.setProperty(RecipeSnapshotDiff.REPORT_PROPERTY, reportProperty);
        }
        if (shouldEnforceRetention()) {
            RecipeSnapshotDiff.validateRetention(baselinePath, candidatePath);
        } else if (reportProperty != null) {
            RecipeSnapshotDiff.writeRetentionReport(
                java.nio.file.Paths.get(reportProperty),
                result,
                RecipeSnapshotDiff.peekFormatVersion(baselinePath),
                RecipeSnapshotDiff.peekFormatVersion(candidatePath));
        }
        if (analysisProperty != null) {
            RecipeSnapshotRetentionAnalyzer.AnalysisResult analysis = RecipeSnapshotRetentionAnalyzer
                .analyze(baselinePath, candidatePath, result, loadNoiseProfile(), expectedLosses);
            RecipeSnapshotRetentionAnalyzer.writeAnalysis(java.nio.file.Paths.get(analysisProperty), analysis);
            System.out.printf(
                "retention analysis: lost=%d fullBody=%d outputCore=%d sameInput=%d expected=%d unverified=%d%n",
                analysis.lostUniqueKeys,
                analysis.equivalentFullBody,
                analysis.equivalentOutputCore,
                analysis.outputVariant,
                analysis.expectedLoss,
                analysis.unverifiedLoss);
        }
        String trueLossesProperty = firstNonBlank(
            System.getenv("GT_RECIPE_SNAPSHOT_RETENTION_TRUE_LOSSES"),
            System.getProperty(RecipeSnapshotRetentionAnalyzer.TRUE_LOSSES_PROPERTY));
        if (trueLossesProperty != null) {
            RecipeSnapshotRetentionAnalyzer.AnalysisResult analysis = RecipeSnapshotRetentionAnalyzer
                .analyze(baselinePath, candidatePath, result, loadNoiseProfile(), expectedLosses);
            RecipeSnapshotRetentionAnalyzer.writeTrueLosses(java.nio.file.Paths.get(trueLossesProperty), analysis);
        }
        if (shouldEnforceTrueLoss()) {
            RecipeSnapshotRetentionAnalyzer.validateNoGameplayRegression(
                RecipeSnapshotRetentionAnalyzer
                    .analyze(baselinePath, candidatePath, result, loadNoiseProfile(), expectedLosses),
                baselinePath,
                candidatePath);
        }
    }

    @Test
    void compareV2UsesGameplayMultisetWithRawSamples() {
        String canonical = "category=gt.recipe.macerator, metadata=[], inputs=[ore], fluidInputs=[], outputs=[dust], fluidInputs=[], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null";
        RecipeSnapshot baseline = snapshotV2(
            "gt.recipe.macerator",
            instance(true, false, true, canonical, "raw-a"),
            instance(true, false, true, canonical, "raw-b"),
            instance(true, false, true, canonical, "raw-c"));
        RecipeSnapshot candidate = snapshotV2("gt.recipe.macerator", instance(true, false, true, canonical, "raw-a"));

        RecipeSnapshotDiff.DiffResult result = RecipeSnapshotDiff.compare(baseline, candidate);
        assertEquals("gameplay-multiset", result.comparisonMode);
        assertEquals(0, result.lostRecipes);
        assertEquals(2, result.dedupedInstances);
        assertEquals(1, result.dedupedEntries.size());
        assertEquals("raw-a", result.dedupedEntries.get(0).sampleRaw.get(0));
    }

    private static RecipeSnapshot snapshotV2(String mapName, RecipeSnapshotExporter.InstanceEntry... instances) {
        RecipeSnapshot snapshot = new RecipeSnapshot();
        snapshot.formatVersion = RecipeSnapshotExporter.FORMAT_VERSION;
        MapSnapshot mapSnapshot = new MapSnapshot();
        mapSnapshot.instances = java.util.Arrays.asList(instances);
        mapSnapshot.keys = new java.util.TreeMap<>();
        mapSnapshot.instanceCount = 0;
        for (RecipeSnapshotExporter.InstanceEntry instance : instances) {
            if (!instance.enabled || instance.fake || !instance.hasInputs) {
                continue;
            }
            String hash = RecipeSnapshotExporter.hashCanonical(instance.canonical);
            RecipeSnapshotExporter.KeyEntry keyEntry = mapSnapshot.keys.get(hash);
            if (keyEntry == null) {
                keyEntry = new RecipeSnapshotExporter.KeyEntry();
                keyEntry.canonical = instance.canonical;
                keyEntry.count = 0;
                mapSnapshot.keys.put(hash, keyEntry);
            }
            keyEntry.count++;
            mapSnapshot.instanceCount++;
        }
        mapSnapshot.uniqueKeyCount = mapSnapshot.keys.size();
        snapshot.maps = new java.util.TreeMap<>();
        snapshot.maps.put(mapName, mapSnapshot);
        return snapshot;
    }

    private static RecipeSnapshotExporter.InstanceEntry instance(boolean enabled, boolean fake, boolean hasInputs,
        String canonical, String raw) {
        RecipeSnapshotExporter.InstanceEntry instance = new RecipeSnapshotExporter.InstanceEntry();
        instance.enabled = enabled;
        instance.fake = fake;
        instance.hasInputs = hasInputs;
        instance.canonical = canonical;
        instance.gameplay = RecipeSnapshotDiff.gameplayFingerprint(canonical);
        instance.raw = raw;
        return instance;
    }

    private static RecipeSnapshot snapshot(String mapName, KeyEntry... entries) {
        RecipeSnapshot snapshot = new RecipeSnapshot();
        MapSnapshot mapSnapshot = new MapSnapshot();
        mapSnapshot.keys = new java.util.TreeMap<>();
        mapSnapshot.instanceCount = 0;
        for (KeyEntry entry : entries) {
            String hash = RecipeSnapshotExporter.hashCanonical(entry.canonical);
            mapSnapshot.keys.put(hash, entry);
            mapSnapshot.instanceCount += entry.count;
        }
        mapSnapshot.uniqueKeyCount = mapSnapshot.keys.size();
        snapshot.maps = new java.util.TreeMap<>();
        snapshot.maps.put(mapName, mapSnapshot);
        return snapshot;
    }

    private static KeyEntry entry(String canonical, int count) {
        KeyEntry entry = new KeyEntry();
        entry.canonical = canonical;
        entry.count = count;
        return entry;
    }

    private static KeyEntry keyedEntry(String canonical, int count) {
        return entry(canonical, count);
    }

    private static String minimalSnapshotJson() {
        return """
            {
              "formatVersion": 2,
              "maps": {
                "gt.recipe.macerator": {
                  "keys": {
                    "recipe-a": {
                      "canonical": "category=gt.recipe.macerator, metadata=[], inputs=[ore], fluidInputs=[], outputs=[dust], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null",
                      "count": 1
                    }
                  }
                }
              }
            }
            """;
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.trim()
                .isEmpty()) {
                return value.trim();
            }
        }
        return null;
    }

    private static boolean shouldEnforceRetention() {
        String env = System.getenv("GT_RECIPE_SNAPSHOT_RETENTION_ENFORCE");
        if (env != null && !env.trim()
            .isEmpty()) {
            return Boolean.parseBoolean(env.trim());
        }
        return Boolean.getBoolean(RecipeSnapshotDiff.ENFORCE_PROPERTY);
    }

    @Nullable
    private static RecipeSnapshotNoiseCalibration.NoiseProfile loadNoiseProfile() {
        String profileProperty = firstNonBlank(
            System.getenv("GT_RECIPE_SNAPSHOT_NOISE_PROFILE"),
            System.getProperty(RecipeSnapshotNoiseCalibration.NOISE_PROFILE_PROPERTY));
        if (profileProperty == null) {
            return null;
        }
        return RecipeSnapshotNoiseCalibration.load(Paths.get(profileProperty));
    }

    @Nullable
    private static RecipeSnapshotRetentionAnalyzer.ExpectedLossProfile loadExpectedLossProfile(
        @Nullable String expectedLossesProperty) {
        if (expectedLossesProperty == null) {
            return null;
        }
        return RecipeSnapshotRetentionAnalyzer.loadExpectedLosses(Paths.get(expectedLossesProperty));
    }

    private static boolean shouldEnforceTrueLoss() {
        String env = System.getenv("GT_RECIPE_SNAPSHOT_RETENTION_ENFORCE_TRUE_LOSS");
        if (env != null && !env.trim()
            .isEmpty()) {
            return Boolean.parseBoolean(env.trim());
        }
        return Boolean.getBoolean(RecipeSnapshotRetentionAnalyzer.ENFORCE_TRUE_LOSS_PROPERTY);
    }
}
