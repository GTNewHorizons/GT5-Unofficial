package gregtech.api.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import gregtech.api.recipe.RecipeSnapshotExporter.KeyEntry;
import gregtech.api.recipe.RecipeSnapshotExporter.MapSnapshot;
import gregtech.api.recipe.RecipeSnapshotExporter.RecipeSnapshot;
import gregtech.api.recipe.RecipeSnapshotNoiseCalibration.NoiseProfile;
import gregtech.api.recipe.RecipeSnapshotRetentionAnalyzer.AnalysisResult;

class RecipeSnapshotNoiseCalibrationTest {

    private static final String MAP = "gt.recipe.test.noise";

    @Test
    void identicalBaselinesProduceZeroNoise() throws Exception {
        String canonical = "category=" + MAP
            + ", metadata=[], inputs=[a], fluidInputs=[], outputs=[b], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null";
        Path baselineA = writeTempSnapshot("noise-a.json", canonical);
        Path baselineB = writeTempSnapshot("noise-b.json", canonical);

        NoiseProfile profile = RecipeSnapshotNoiseCalibration.calibrate(Arrays.asList(baselineA, baselineB));
        assertEquals(0, profile.noisyFingerprints.size());
        assertEquals(0, profile.maxDiffLost);
    }

    @Test
    void unstableMetadataBetweenBaselinesIsNoise() throws Exception {
        String stable = "inputs=[a], fluidInputs=[], outputs=[b], fluidOutputs=[], duration=1, eut=1, specialValue=0, special=null";
        Path baselineA = writeTempSnapshot("noise-meta-a.json", "category=" + MAP + ", metadata=[foo=bar], " + stable);
        Path baselineB = writeTempSnapshot("noise-meta-b.json", "category=" + MAP + ", metadata=[foo=baz], " + stable);

        NoiseProfile profile = RecipeSnapshotNoiseCalibration.calibrate(Arrays.asList(baselineA, baselineB));
        assertTrue(profile.noisyFingerprints.size() >= 1);

        RecipeSnapshotDiff.DiffResult diff = RecipeSnapshotDiff.compare(baselineA, baselineB);
        AnalysisResult withoutNoise = RecipeSnapshotRetentionAnalyzer.analyze(baselineA, baselineB, diff);
        AnalysisResult withNoise = RecipeSnapshotRetentionAnalyzer.analyze(baselineA, baselineB, diff, profile);
        assertTrue(withoutNoise.lostUniqueKeys > 0 || withoutNoise.outputVariant > 0);
        assertEquals(
            withoutNoise.lostUniqueKeys,
            withNoise.baselineNoise + withNoise.equivalentFullBody
                + withNoise.equivalentOutputCore
                + withNoise.outputVariant
                + withNoise.unverifiedLoss);
    }

    @Test
    void canCalibrateRealBaselineSnapshotsWhenPathsAreProvided() {
        String runsProperty = firstNonBlank(
            System.getenv("GT_RECIPE_SNAPSHOT_NOISE_BASELINES"),
            System.getProperty(RecipeSnapshotNoiseCalibration.BASELINE_RUNS_PROPERTY));
        if (runsProperty == null) {
            return;
        }

        List<Path> baselines = Arrays.stream(runsProperty.split(";"))
            .map(String::trim)
            .filter(s -> !s.isEmpty())
            .map(Paths::get)
            .toList();
        NoiseProfile profile = RecipeSnapshotNoiseCalibration.calibrate(baselines);
        System.out.printf(
            "noise calibration: baselines=%d pairs=%d maxDiffLost=%d maxOutputVariant=%d maxUnverified=%d noisyKeys=%d%n",
            baselines.size(),
            profile.pairCount,
            profile.maxDiffLost,
            profile.maxOutputVariant,
            profile.maxUnverifiedLoss,
            profile.noisyFingerprints.size());
        profile.noisyByMap.entrySet()
            .stream()
            .sorted((left, right) -> Integer.compare(right.getValue(), left.getValue()))
            .limit(20)
            .forEach(entry -> System.out.printf("noise by map: %s=%d%n", entry.getKey(), entry.getValue()));

        String profileProperty = firstNonBlank(
            System.getenv("GT_RECIPE_SNAPSHOT_NOISE_PROFILE"),
            System.getProperty(RecipeSnapshotNoiseCalibration.NOISE_PROFILE_PROPERTY));
        if (profileProperty != null) {
            RecipeSnapshotNoiseCalibration.write(Paths.get(profileProperty), profile);
        }
    }

    private static Path writeTempSnapshot(String fileName, String... canonicals) throws Exception {
        Path path = Paths.get(System.getProperty("java.io.tmpdir"), fileName);
        RecipeSnapshot snapshot = new RecipeSnapshot();
        snapshot.formatVersion = RecipeSnapshotExporter.FORMAT_VERSION;
        MapSnapshot mapSnapshot = new MapSnapshot();
        mapSnapshot.keys = new java.util.TreeMap<>();
        for (String canonical : canonicals) {
            KeyEntry entry = new KeyEntry();
            entry.canonical = canonical;
            entry.count = 1;
            mapSnapshot.keys.put(RecipeSnapshotExporter.hashCanonical(canonical), entry);
        }
        mapSnapshot.instanceCount = canonicals.length;
        mapSnapshot.uniqueKeyCount = canonicals.length;
        snapshot.maps = new java.util.TreeMap<>();
        snapshot.maps.put(MAP, mapSnapshot);
        java.nio.file.Files.writeString(path, new com.google.gson.Gson().toJson(snapshot));
        return path;
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
