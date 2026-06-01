package gregtech.api.recipe;

import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.jetbrains.annotations.Nullable;

import gregtech.api.util.GTLog;

public class RecipeMapBackendProfiler {

    static final class RecipeLookupProfile {

        private static final boolean ENABLED = Boolean.getBoolean("gt.recipe.lookup.profile");
        private static final long REPORT_INTERVAL_CALLS = Long
            .getLong("gt.recipe.lookup.profile.interval_calls", 5_000L);
        private static final long REPORT_INTERVAL_NANOS = Math
            .max(1L, Long.getLong("gt.recipe.lookup.profile.interval_seconds", 15L)) * 1_000_000_000L;
        private static final ConcurrentMap<String, RecipeLookupProfileStats> STATS = new ConcurrentHashMap<>();

        private final RecipeLookupProfileStats stats;

        private RecipeLookupProfile(RecipeLookupProfileStats stats) {
            this.stats = stats;
        }

        @Nullable
        static RecipeLookupProfile start(@Nullable RecipeMap<?> recipeMap, boolean collisionCheck) {
            if (!ENABLED) {
                return null;
            }
            String mapName = recipeMap == null ? "<unbound>" : recipeMap.unlocalizedName;
            RecipeLookupProfileStats stats = STATS.computeIfAbsent(mapName, RecipeLookupProfileStats::new);
            stats.recordCall(collisionCheck);
            return new RecipeLookupProfile(stats);
        }

        void recordOverwrite() {
            stats.recordOverwrite();
        }

        void recordEmptyMapReject() {
            stats.recordEmptyMapReject();
        }

        void recordMinInputReject() {
            stats.recordMinInputReject();
        }

        void addSetupNanos(long nanos) {
            stats.addSetupNanos(nanos);
        }

        void addUnificationNanos(long nanos) {
            stats.addUnificationNanos(nanos);
        }

        void addEnsureLookupNanos(long nanos) {
            stats.addEnsureLookupNanos(nanos);
        }

        void recordCachedRecipeCandidate() {
            stats.recordCachedRecipeCandidate();
        }

        void recordCacheMapProbe() {
            stats.recordCacheMapProbe();
        }

        void recordCacheMapCandidate() {
            stats.recordCacheMapCandidate();
        }

        void recordTrieLookupSetup() {
            stats.recordTrieLookupSetup();
        }

        void addTrieLookupSetupNanos(long nanos) {
            stats.addTrieLookupSetupNanos(nanos);
        }

        void recordTrieCandidate() {
            stats.recordTrieCandidate();
        }

        void recordMatchedCandidate() {
            stats.recordMatchedCandidate();
        }

        void recordFallbackProbe() {
            stats.recordFallbackProbe();
        }

        void recordFallbackHit() {
            stats.recordFallbackHit();
        }

        void addFilterNanos(long nanos, boolean matched) {
            stats.addFilterNanos(nanos, matched);
        }

        void addModifyNanos(long nanos, boolean returnedRecipe) {
            stats.addModifyNanos(nanos, returnedRecipe);
        }
    }

    private static final class RecipeLookupProfileStats {

        private final String mapName;
        private long lastReportNanos = System.nanoTime();
        private long lastReportCalls;
        private long calls;
        private long collisionCalls;
        private long overwriteCalls;
        private long emptyMapRejects;
        private long minInputRejects;
        private long setupNanos;
        private long unificationNanos;
        private long ensureLookupNanos;
        private long cachedRecipeCandidates;
        private long cacheMapProbes;
        private long cacheMapCandidates;
        private long trieLookupSetups;
        private long trieLookupSetupNanos;
        private long trieCandidates;
        private long matchedCandidates;
        private long fallbackProbes;
        private long fallbackHits;
        private long filterCalls;
        private long filterMatches;
        private long filterNanos;
        private long modifyCalls;
        private long modifyHits;
        private long modifyNanos;

        private RecipeLookupProfileStats(String mapName) {
            this.mapName = mapName;
        }

        private synchronized void recordCall(boolean collisionCheck) {
            calls++;
            if (collisionCheck) {
                collisionCalls++;
            }
            maybeReport();
        }

        private synchronized void recordOverwrite() {
            overwriteCalls++;
        }

        private synchronized void recordEmptyMapReject() {
            emptyMapRejects++;
        }

        private synchronized void recordMinInputReject() {
            minInputRejects++;
        }

        private synchronized void addSetupNanos(long nanos) {
            setupNanos += nanos;
        }

        private synchronized void addUnificationNanos(long nanos) {
            unificationNanos += nanos;
        }

        private synchronized void addEnsureLookupNanos(long nanos) {
            ensureLookupNanos += nanos;
        }

        private synchronized void recordCachedRecipeCandidate() {
            cachedRecipeCandidates++;
        }

        private synchronized void recordCacheMapProbe() {
            cacheMapProbes++;
        }

        private synchronized void recordCacheMapCandidate() {
            cacheMapCandidates++;
        }

        private synchronized void recordTrieLookupSetup() {
            trieLookupSetups++;
        }

        private synchronized void addTrieLookupSetupNanos(long nanos) {
            trieLookupSetupNanos += nanos;
        }

        private synchronized void recordTrieCandidate() {
            trieCandidates++;
        }

        private synchronized void recordMatchedCandidate() {
            matchedCandidates++;
        }

        private synchronized void recordFallbackProbe() {
            fallbackProbes++;
        }

        private synchronized void recordFallbackHit() {
            fallbackHits++;
        }

        private synchronized void addFilterNanos(long nanos, boolean matched) {
            filterCalls++;
            if (matched) {
                filterMatches++;
            }
            filterNanos += nanos;
        }

        private synchronized void addModifyNanos(long nanos, boolean returnedRecipe) {
            modifyCalls++;
            if (returnedRecipe) {
                modifyHits++;
            }
            modifyNanos += nanos;
        }

        private void maybeReport() {
            long now = System.nanoTime();
            boolean enoughCalls = RecipeLookupProfile.REPORT_INTERVAL_CALLS > 0
                && calls - lastReportCalls >= RecipeLookupProfile.REPORT_INTERVAL_CALLS;
            boolean enoughTime = now - lastReportNanos >= RecipeLookupProfile.REPORT_INTERVAL_NANOS;
            if (!enoughCalls && !enoughTime) {
                return;
            }

            GTLog.out.println(
                String.format(
                    Locale.ROOT,
                    "[GTRecipeLookupProfile] map=%s calls=%d collision=%d overwrite=%d empty=%d minReject=%d "
                        + "setupAvgUs=%.3f ensureAvgUs=%.3f unifyMs=%.3f cachedCandidates=%d "
                        + "cacheMapProbes=%d cacheMapCandidates=%d trieLookupSetups=%d trieLookupSetupMs=%.3f "
                        + "trieCandidates=%d trieCandidatesPerLookupSetup=%.3f matched=%d fallbackProbes=%d fallbackHits=%d "
                        + "filterCalls=%d filterMatches=%d filterMs=%.3f modifyCalls=%d modifyHits=%d modifyMs=%.3f",
                    mapName,
                    calls,
                    collisionCalls,
                    overwriteCalls,
                    emptyMapRejects,
                    minInputRejects,
                    averageMicros(setupNanos, calls),
                    averageMicros(ensureLookupNanos, calls),
                    nanosToMillis(unificationNanos),
                    cachedRecipeCandidates,
                    cacheMapProbes,
                    cacheMapCandidates,
                    trieLookupSetups,
                    nanosToMillis(trieLookupSetupNanos),
                    trieCandidates,
                    average(trieCandidates, trieLookupSetups),
                    matchedCandidates,
                    fallbackProbes,
                    fallbackHits,
                    filterCalls,
                    filterMatches,
                    nanosToMillis(filterNanos),
                    modifyCalls,
                    modifyHits,
                    nanosToMillis(modifyNanos)));
            lastReportCalls = calls;
            lastReportNanos = now;
        }

        private static double average(long value, long count) {
            return count == 0 ? 0.0D : (double) value / count;
        }

        private static double averageMicros(long nanos, long count) {
            return count == 0 ? 0.0D : (double) nanos / count / 1_000.0D;
        }

        private static double nanosToMillis(long nanos) {
            return (double) nanos / 1_000_000.0D;
        }
    }
}
