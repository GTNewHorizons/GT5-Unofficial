package gregtech.api.recipe;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import gregtech.api.recipe.RecipeSnapshotDiff.LostEntry;
import gregtech.api.recipe.RecipeSnapshotRetentionAnalyzer.AnalysisResult;
import gregtech.api.recipe.RecipeSnapshotRetentionAnalyzer.ClassifiedLoss;

/**
 * Measures snapshot diff noise by comparing multiple baseline exports with identical flags.
 */
public final class RecipeSnapshotNoiseCalibration {

    public static final String NOISE_PROFILE_PROPERTY = "gt.recipe.snapshot.noise.profile";
    public static final String BASELINE_RUNS_PROPERTY = "gt.recipe.snapshot.noise.baseline.runs";

    private static final Logger LOGGER = LogManager.getLogger("GregTech GTNH");
    private static final Gson GSON = new GsonBuilder().disableHtmlEscaping()
        .setPrettyPrinting()
        .create();

    private RecipeSnapshotNoiseCalibration() {}

    public static NoiseProfile calibrate(List<Path> baselineSnapshots) {
        if (baselineSnapshots.size() < 2) {
            throw new IllegalArgumentException("Need at least 2 baseline snapshots for noise calibration");
        }
        NoiseProfile profile = new NoiseProfile();
        for (Path baselineSnapshot : baselineSnapshots) {
            profile.baselineSnapshots.add(
                baselineSnapshot.toAbsolutePath()
                    .toString());
        }
        profile.pairCount = 0;

        for (int left = 0; left < baselineSnapshots.size(); left++) {
            for (int right = left + 1; right < baselineSnapshots.size(); right++) {
                Path baselineA = baselineSnapshots.get(left);
                Path baselineB = baselineSnapshots.get(right);
                profile.pairCount++;
                accumulateNoise(profile, baselineA, baselineB);
                accumulateNoise(profile, baselineB, baselineA);
            }
        }
        LOGGER.info(
            "GT recipe snapshot noise calibration: {} baseline(s), {} pair(s), {} noisy fingerprint(s).",
            baselineSnapshots.size(),
            profile.pairCount,
            profile.noisyFingerprints.size());
        return profile;
    }

    public static NoiseProfile load(Path path) {
        try {
            NoiseProfile profile = GSON.fromJson(Files.readString(path, StandardCharsets.UTF_8), NoiseProfile.class);
            if (profile == null || profile.noisyFingerprints == null) {
                throw new IllegalStateException("Noise profile is empty: " + path.toAbsolutePath());
            }
            return profile;
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read noise profile from " + path.toAbsolutePath(), e);
        }
    }

    public static void write(Path path, NoiseProfile profile) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.write(
                path,
                GSON.toJson(profile)
                    .getBytes(StandardCharsets.UTF_8));
            LOGGER.info(
                "GT recipe snapshot noise profile: wrote {} noisy fingerprint(s) to {}",
                profile.noisyFingerprints.size(),
                path.toAbsolutePath());
        } catch (IOException e) {
            throw new IllegalStateException("Failed to write noise profile to " + path.toAbsolutePath(), e);
        }
    }

    public static boolean isNoisy(NoiseProfile profile, String mapName, String gameplayFingerprint) {
        return profile.noisyKeys.contains(noiseKey(mapName, gameplayFingerprint));
    }

    public static String noiseKey(String mapName, String gameplayFingerprint) {
        return mapName + "|" + gameplayFingerprint;
    }

    public static Path noiseProfilePath() {
        String property = System.getProperty(NOISE_PROFILE_PROPERTY);
        if (property == null || property.trim()
            .isEmpty()) {
            throw new IllegalStateException("Missing -D" + NOISE_PROFILE_PROPERTY);
        }
        return Paths.get(property.trim());
    }

    private static void accumulateNoise(NoiseProfile profile, Path baselinePath, Path otherBaselinePath) {
        RecipeSnapshotDiff.DiffResult diff = RecipeSnapshotDiff.compare(baselinePath, otherBaselinePath);
        profile.maxDiffLost = Math.max(profile.maxDiffLost, diff.lostRecipes);
        for (LostEntry lost : RecipeSnapshotDiff.allLostEntries(baselinePath, otherBaselinePath)) {
            String key = noiseKey(lost.mapName, lost.gameplayFingerprint);
            if (profile.noisyKeys.add(key)) {
                NoisyFingerprint entry = new NoisyFingerprint();
                entry.mapName = lost.mapName;
                entry.gameplayFingerprint = lost.gameplayFingerprint;
                entry.sampleCanonical = lost.canonical;
                profile.noisyFingerprints.add(entry);
            }
            profile.noisyByMap.merge(lost.mapName, 1, Integer::sum);
        }

        AnalysisResult analysis = RecipeSnapshotRetentionAnalyzer.analyze(baselinePath, otherBaselinePath, diff);
        profile.maxOutputVariant = Math.max(profile.maxOutputVariant, analysis.outputVariant);
        profile.maxUnverifiedLoss = Math.max(profile.maxUnverifiedLoss, analysis.unverifiedLoss);
        for (ClassifiedLoss variant : analysis.outputVariants) {
            String key = noiseKey(variant.mapName, variant.gameplayFingerprint);
            if (profile.outputVariantKeys.add(key)) {
                profile.outputVariantNoise.add(variant);
            }
        }
    }

    public static final class NoiseProfile {

        public final List<String> baselineSnapshots = new ArrayList<>();
        public int pairCount;
        public int maxDiffLost;
        public int maxOutputVariant;
        public int maxUnverifiedLoss;
        public final Set<String> noisyKeys = new LinkedHashSet<>();
        public final Set<String> outputVariantKeys = new LinkedHashSet<>();
        public final List<NoisyFingerprint> noisyFingerprints = new ArrayList<>();
        public final List<ClassifiedLoss> outputVariantNoise = new ArrayList<>();
        public final Map<String, Integer> noisyByMap = new TreeMap<>();
    }

    public static final class NoisyFingerprint {

        public String mapName;
        public String gameplayFingerprint;
        public String sampleCanonical;
    }
}
