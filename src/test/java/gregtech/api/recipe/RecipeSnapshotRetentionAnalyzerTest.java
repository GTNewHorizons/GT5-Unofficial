package gregtech.api.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Test;

import gregtech.api.recipe.RecipeSnapshotExporter.KeyEntry;
import gregtech.api.recipe.RecipeSnapshotExporter.MapSnapshot;
import gregtech.api.recipe.RecipeSnapshotExporter.RecipeSnapshot;
import gregtech.api.recipe.RecipeSnapshotRetentionAnalyzer.AnalysisResult;

class RecipeSnapshotRetentionAnalyzerTest {

    private static final String MAP = "gt.recipe.test.retention";
    private static final String OUTPUT = "outputs=[dust], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null";

    @Test
    void equivalentFullBodyIsNotUnverifiedLoss() {
        String sharedBody = "inputs=[x], fluidInputs=[], " + OUTPUT;
        String baselineCanonical = "category=" + MAP + ", metadata=[stable=1], " + sharedBody;
        String candidateCanonical = "category=" + MAP + ", metadata=[], " + sharedBody;
        Path baseline = writeTempSnapshot("baseline-full-body.json", baselineCanonical);
        Path candidate = writeTempSnapshot("candidate-full-body.json", candidateCanonical);

        AnalysisResult analysis = analyzePaths(baseline, candidate);
        assertEquals(1, analysis.lostUniqueKeys);
        assertEquals(1, analysis.equivalentFullBody);
        assertEquals(0, analysis.unverifiedLoss);
        RecipeSnapshotRetentionAnalyzer.validateNoUnverifiedLoss(analysis);
    }

    @Test
    void sameInputWithDifferentOutputIsOutputVariant() {
        String inputs = "inputs=[oreBlock], fluidInputs=";
        String baselineCanonical = "category=" + MAP
            + ", metadata=[], "
            + inputs
            + "[], outputs=[dustA, byproductX], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null";
        String candidateCanonical = "category=" + MAP
            + ", metadata=[], "
            + inputs
            + "[], outputs=[dustA, byproductY], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null";
        Path baseline = writeTempSnapshot("baseline-same-input.json", baselineCanonical);
        Path candidate = writeTempSnapshot("candidate-same-input.json", candidateCanonical);

        AnalysisResult analysis = analyzePaths(baseline, candidate);
        assertEquals(1, analysis.lostUniqueKeys);
        assertEquals(1, analysis.outputVariant);
        assertEquals(0, analysis.unverifiedLoss);
        assertThrows(
            IllegalStateException.class,
            () -> RecipeSnapshotRetentionAnalyzer.validateNoGameplayRegression(analysis, baseline, candidate));
    }

    @Test
    void sameOutputCoreWithDifferentInputIsInputVariant() {
        String baselineCanonical = "category=" + MAP
            + ", metadata=[], inputs=[moda:zincGravel], fluidInputs=[], "
            + OUTPUT;
        String candidateCanonical = "category=" + MAP
            + ", metadata=[], inputs=[modb:zincGravel], fluidInputs=[], "
            + OUTPUT;
        Path baseline = writeTempSnapshot("baseline-different-input.json", baselineCanonical);
        Path candidate = writeTempSnapshot("candidate-different-input.json", candidateCanonical);

        AnalysisResult analysis = analyzePaths(baseline, candidate);
        assertEquals(1, analysis.lostUniqueKeys);
        assertEquals(0, analysis.equivalentOutputCore);
        assertEquals(1, analysis.inputVariant);
        assertEquals(0, analysis.outputVariant);
        assertEquals(0, analysis.unverifiedLoss);
        IllegalStateException thrown = assertThrows(
            IllegalStateException.class,
            () -> RecipeSnapshotRetentionAnalyzer.validateNoGameplayRegression(analysis, baseline, candidate));
        assertTrue(
            thrown.getMessage()
                .contains("input variant"));
    }

    @Test
    void expectedOutputVariantProfileRemovesOnlyExactVariant() {
        String sharedInputs = "inputs=[oreBlock], fluidInputs=";
        String baselineCanonical = "category=" + MAP
            + ", metadata=[], "
            + sharedInputs
            + "[], outputs=[dustA, byproductX], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null";
        String expectedCandidateCanonical = "category=" + MAP
            + ", metadata=[], "
            + sharedInputs
            + "[], outputs=[dustA, byproductY], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null";
        String unexpectedCandidateCanonical = "category=" + MAP
            + ", metadata=[], inputs=[oreOther], fluidInputs=[], outputs=[dustA], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null";
        Path baseline = writeTempSnapshot("baseline-expected-output-variant.json", baselineCanonical);
        Path candidate = writeTempSnapshot(
            "candidate-expected-output-variant.json",
            expectedCandidateCanonical,
            unexpectedCandidateCanonical);
        RecipeSnapshotDiff.DiffResult diff = RecipeSnapshotDiff.compare(baseline, candidate);
        RecipeSnapshotRetentionAnalyzer.ExpectedLossProfile expectedLosses = new RecipeSnapshotRetentionAnalyzer.ExpectedLossProfile();
        expectedLosses.expectedOutputVariantKeys.add(
            RecipeSnapshotRetentionAnalyzer.expectedOutputVariantKey(
                MAP,
                RecipeSnapshotDiff.gameplayFingerprint(baselineCanonical),
                expectedCandidateCanonical));

        AnalysisResult analysis = RecipeSnapshotRetentionAnalyzer
            .analyze(baseline, candidate, diff, null, expectedLosses);

        assertEquals(0, analysis.outputVariant);
        assertEquals(1, analysis.expectedOutputVariant);
        assertEquals(0, analysis.unverifiedLoss);
        RecipeSnapshotRetentionAnalyzer.validateNoGameplayRegression(analysis, baseline, candidate);
    }

    @Test
    void expectedLossProfileRemovesOnlyExactUnverifiedLoss() {
        String expectedCanonical = "category=" + MAP
            + ", metadata=[], inputs=[expected], fluidInputs=[], outputs=[dust], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null";
        String unexpectedCanonical = "category=" + MAP
            + ", metadata=[], inputs=[unexpected], fluidInputs=[], outputs=[dust], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null";
        Path baseline = writeTempSnapshot("baseline-expected-loss.json", expectedCanonical, unexpectedCanonical);
        Path candidate = writeTempSnapshot("candidate-expected-loss.json");
        RecipeSnapshotDiff.DiffResult diff = RecipeSnapshotDiff.compare(baseline, candidate);
        RecipeSnapshotRetentionAnalyzer.ExpectedLossProfile expectedLosses = new RecipeSnapshotRetentionAnalyzer.ExpectedLossProfile();
        expectedLosses.expectedKeys.add(RecipeSnapshotRetentionAnalyzer.expectedKey(MAP, expectedCanonical));

        AnalysisResult analysis = RecipeSnapshotRetentionAnalyzer
            .analyze(baseline, candidate, diff, null, expectedLosses);

        assertEquals(1, analysis.expectedLoss);
        assertEquals(1, analysis.unverifiedLoss);
        assertThrows(
            IllegalStateException.class,
            () -> RecipeSnapshotRetentionAnalyzer.validateNoGameplayRegression(analysis, baseline, candidate));
    }

    @Test
    void outputOrderPermutationIsNotGameplayLoss() {
        String sharedInputs = "inputs=[paper], fluidInputs=[], ";
        String baselineCanonical = "category=" + MAP
            + ", metadata=[], "
            + sharedInputs
            + "outputs=[minecraft:paper:0 x1 (item.paper), MagicBees:wax:0 x1 (item.wax)], fluidOutputs=[], duration=128, eut=5, specialValue=0, special=null";
        String candidateCanonical = "category=" + MAP
            + ", metadata=[], "
            + sharedInputs
            + "outputs=[MagicBees:wax:0 x1 (item.wax), minecraft:paper:0 x1 (item.paper)], fluidOutputs=[], duration=128, eut=5, specialValue=0, special=null";
        Path baseline = writeTempSnapshot("baseline-output-order.json", baselineCanonical);
        Path candidate = writeTempSnapshot("candidate-output-order.json", candidateCanonical);

        RecipeSnapshotDiff.DiffResult diff = RecipeSnapshotDiff.compare(baseline, candidate);
        assertEquals(0, diff.lostRecipes);
        AnalysisResult analysis = RecipeSnapshotRetentionAnalyzer.analyze(baseline, candidate, diff, null);
        assertEquals(0, analysis.outputVariant);
        RecipeSnapshotRetentionAnalyzer.validateNoGameplayRegression(analysis, baseline, candidate);
    }

    @Test
    void reducedDuplicateCountIsConfirmedDeduplication() {
        String canonical = "category=" + MAP + ", metadata=[], inputs=[duplicate], fluidInputs=[], " + OUTPUT;
        Path baseline = writeTempSnapshotForMapWithCount("baseline-confirmed-dedupe.json", MAP, canonical, 2);
        Path candidate = writeTempSnapshotForMapWithCount("candidate-confirmed-dedupe.json", MAP, canonical, 1);

        AnalysisResult analysis = analyzePaths(baseline, candidate);
        assertEquals(0, analysis.lostUniqueKeys);
        assertEquals(1, analysis.deduplicationConfirmed);
        assertEquals(0, analysis.unverifiedLoss);
        assertEquals(0, analysis.outputVariant);
        RecipeSnapshotRetentionAnalyzer.validateNoGameplayRegression(analysis, baseline, candidate);
    }

    @Test
    void equivalentOutputCoreWithDifferentInputIsInputVariant() {
        String baselineCanonical = "category=" + MAP + ", metadata=[], inputs=[oreA], fluidInputs=[], " + OUTPUT;
        String candidateCanonical = "category=" + MAP
            + ", metadata=[], inputs=[oreB], fluidInputs=[water x100], "
            + OUTPUT;
        Path baseline = writeTempSnapshot("baseline-output-core.json", baselineCanonical);
        Path candidate = writeTempSnapshot("candidate-output-core.json", candidateCanonical);

        AnalysisResult analysis = analyzePaths(baseline, candidate);
        assertEquals(1, analysis.lostUniqueKeys);
        assertEquals(0, analysis.equivalentFullBody);
        assertEquals(0, analysis.equivalentOutputCore);
        assertEquals(1, analysis.inputVariant);
        assertEquals(0, analysis.unverifiedLoss);
        assertThrows(
            IllegalStateException.class,
            () -> RecipeSnapshotRetentionAnalyzer.validateNoUnverifiedLoss(analysis));
    }

    @Test
    void equivalentFullBodyDoesNotMatchAcrossRecipeMaps() {
        String sharedBody = "inputs=[wire x4], fluidInputs=[rubber x144], "
            + "outputs=[cable x4], fluidOutputs=[], duration=400, eut=7, specialValue=0, special=null";
        String baselineCanonical = "category=gt.recipe.assembler, metadata=[], " + sharedBody;
        String candidateCanonical = "category=gt.recipe.cable, metadata=[], " + sharedBody;
        Path baseline = writeTempSnapshotForMap("baseline-cross-map.json", "gt.recipe.assembler", baselineCanonical);
        Path candidate = writeTempSnapshotForMap("candidate-cross-map.json", "gt.recipe.cable", candidateCanonical);

        AnalysisResult analysis = analyzePaths(baseline, candidate);
        assertEquals(1, analysis.lostUniqueKeys);
        assertEquals(0, analysis.equivalentFullBody);
        assertEquals(1, analysis.unverifiedLoss);
        assertThrows(
            IllegalStateException.class,
            () -> RecipeSnapshotRetentionAnalyzer.validateNoUnverifiedLoss(analysis));
    }

    @Test
    void missingCandidateRecipeIsUnverifiedLoss() {
        String baselineCanonical = "category=" + MAP
            + ", metadata=[], inputs=[onlyBaseline], fluidInputs=[], "
            + "outputs=[unique], fluidOutputs=[], duration=9, eut=9, specialValue=0, special=null";
        Path baseline = writeTempSnapshot("baseline-true-loss.json", baselineCanonical);
        Path candidate = writeTempSnapshot("candidate-true-loss.json");

        AnalysisResult analysis = analyzePaths(baseline, candidate);
        assertEquals(1, analysis.lostUniqueKeys);
        assertEquals(1, analysis.unverifiedLoss);
        assertThrows(
            IllegalStateException.class,
            () -> RecipeSnapshotRetentionAnalyzer.validateNoUnverifiedLoss(analysis));
    }

    @Test
    void canAnalyzeRealSnapshotsWhenPathsAreProvided() {
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
        AnalysisResult analysis = analyzePaths(baselinePath, candidatePath);
        System.out.printf(
            "retention gate analysis: lost=%d fullBody=%d outputCore=%d inputVariant=%d outputVariant=%d baselineNoise=%d unverified=%d%n",
            analysis.lostUniqueKeys,
            analysis.equivalentFullBody,
            analysis.equivalentOutputCore,
            analysis.inputVariant,
            analysis.outputVariant,
            analysis.baselineNoise,
            analysis.unverifiedLoss);
        analysis.unverifiedLossesByMap.entrySet()
            .stream()
            .sorted((left, right) -> Integer.compare(right.getValue(), left.getValue()))
            .limit(20)
            .forEach(entry -> System.out.printf("unverified by map: %s=%d%n", entry.getKey(), entry.getValue()));

        String trueLossesProperty = firstNonBlank(
            System.getenv("GT_RECIPE_SNAPSHOT_RETENTION_TRUE_LOSSES"),
            System.getProperty(RecipeSnapshotRetentionAnalyzer.TRUE_LOSSES_PROPERTY));
        if (trueLossesProperty != null) {
            RecipeSnapshotRetentionAnalyzer.writeTrueLosses(Paths.get(trueLossesProperty), analysis);
        }
        String outputVariantsProperty = firstNonBlank(
            System.getenv("GT_RECIPE_SNAPSHOT_RETENTION_OUTPUT_VARIANTS"),
            System.getProperty(RecipeSnapshotRetentionAnalyzer.OUTPUT_VARIANTS_PROPERTY));
        if (outputVariantsProperty != null) {
            RecipeSnapshotRetentionAnalyzer.writeOutputVariants(Paths.get(outputVariantsProperty), analysis);
        }
        if (shouldEnforceTrueLoss()) {
            RecipeSnapshotRetentionAnalyzer.validateNoGameplayRegression(analysis, baselinePath, candidatePath);
        }
    }

    private static AnalysisResult analyzePaths(Path baselinePath, Path candidatePath) {
        RecipeSnapshotDiff.DiffResult diff = RecipeSnapshotDiff.compare(baselinePath, candidatePath);
        RecipeSnapshotNoiseCalibration.NoiseProfile noiseProfile = loadNoiseProfile();
        return RecipeSnapshotRetentionAnalyzer
            .analyze(baselinePath, candidatePath, diff, noiseProfile, loadExpectedLossProfile());
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
    private static RecipeSnapshotRetentionAnalyzer.ExpectedLossProfile loadExpectedLossProfile() {
        String expectedLossesProperty = firstNonBlank(
            System.getenv("GT_RECIPE_SNAPSHOT_RETENTION_EXPECTED_LOSSES"),
            System.getProperty(RecipeSnapshotRetentionAnalyzer.EXPECTED_LOSSES_PROPERTY));
        if (expectedLossesProperty == null) {
            return null;
        }
        return RecipeSnapshotRetentionAnalyzer.loadExpectedLosses(Paths.get(expectedLossesProperty));
    }

    private static Path writeTempSnapshot(String fileName, String... canonicals) {
        return writeTempSnapshotForMap(fileName, MAP, canonicals);
    }

    private static Path writeTempSnapshotForMap(String fileName, String mapName, String... canonicals) {
        return writeTempSnapshotForMapWithCount(fileName, mapName, 1, canonicals);
    }

    private static Path writeTempSnapshotForMapWithCount(String fileName, String mapName, String canonical, int count) {
        return writeTempSnapshotForMapWithCount(fileName, mapName, count, canonical);
    }

    private static Path writeTempSnapshotForMapWithCount(String fileName, String mapName, int count,
        String... canonicals) {
        try {
            Path path = Paths.get(System.getProperty("java.io.tmpdir"), fileName);
            RecipeSnapshot snapshot = new RecipeSnapshot();
            snapshot.formatVersion = RecipeSnapshotExporter.FORMAT_VERSION;
            MapSnapshot mapSnapshot = new MapSnapshot();
            mapSnapshot.keys = new java.util.TreeMap<>();
            for (String canonical : canonicals) {
                KeyEntry entry = new KeyEntry();
                entry.canonical = canonical;
                entry.count = count;
                mapSnapshot.keys.put(RecipeSnapshotExporter.hashCanonical(canonical), entry);
            }
            mapSnapshot.instanceCount = canonicals.length * count;
            mapSnapshot.uniqueKeyCount = canonicals.length;
            snapshot.maps = new java.util.TreeMap<>();
            snapshot.maps.put(mapName, mapSnapshot);
            java.nio.file.Files.writeString(path, new com.google.gson.Gson().toJson(snapshot));
            return path;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    private static boolean shouldEnforceTrueLoss() {
        String env = System.getenv("GT_RECIPE_SNAPSHOT_RETENTION_ENFORCE_TRUE_LOSS");
        if (env != null && !env.trim()
            .isEmpty()) {
            return Boolean.parseBoolean(env.trim());
        }
        return Boolean.getBoolean(RecipeSnapshotRetentionAnalyzer.ENFORCE_TRUE_LOSS_PROPERTY);
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
}
