package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The registration-based catalog of Voidcraft stars (the registration-based replacement for the fixed
 * {@link USSStarType} enum as the definition source).
 *
 * <p>
 * Stars are {@link USSStarDefinition}s registered by a stable {@code id}. The registry is a simple global,
 * insertion-ordered map: register once (see {@link USSStarCatalog#registerAll()}), then look up by id, list all, or
 * resolve a star's evolution target.
 *
 * <p>
 * Deliberately NOT wired into the star/evolution mechanics yet (that is a later pass) — this is the pure
 * registration layer. Thread-safe (synchronized); the backing store is a {@link LinkedHashMap} so {@link #all()} is
 * stable in registration order (important for deterministic generation and tests).
 *
 * <p>
 * Bare-JVM safe: only {@link USSStarDefinition}/{@link gregtech.api.enums.Materials} data — no Forge fluid/block
 * objects.
 */
public final class USSStarRegistry {

    private static final Map<String, USSStarDefinition> STARS = new LinkedHashMap<>();

    private USSStarRegistry() {
        throw new AssertionError("Static registry");
    }

    /**
     * Register a star definition.
     *
     * @param definition the definition to register (must not be null)
     * @throws NullPointerException     if {@code definition} is null
     * @throws IllegalArgumentException if a star with the same id is already registered
     */
    public static synchronized void register(USSStarDefinition definition) {
        Objects.requireNonNull(definition, "definition");
        if (STARS.containsKey(definition.getId())) {
            throw new IllegalArgumentException("Duplicate star id: " + definition.getId());
        }
        STARS.put(definition.getId(), definition);
    }

    /**
     * @param id the star id (null → null)
     * @return the registered definition, or null when no star with that id is registered
     */
    public static synchronized USSStarDefinition get(String id) {
        return id == null ? null : STARS.get(id);
    }

    /**
     * @param id the star id
     * @return true if a star with that id is registered
     */
    public static synchronized boolean contains(String id) {
        return id != null && STARS.containsKey(id);
    }

    /**
     * Look up a star by the legacy {@link USSStarType} enum — the enum name lowercased is the star's id
     * (e.g. {@code MAIN_SEQUENCE} → {@code "main_sequence"}). This is the bridge between the still-live enum
     * identity (planet pools, infodata, the render TE) and the registration-based definition source.
     *
     * @param starType the star type (null → null)
     * @return the registered definition, or null when no star with that id is registered
     */
    public static synchronized USSStarDefinition byType(USSStarType starType) {
        if (starType == null) {
            return null;
        }
        return STARS.get(
            starType.name()
                .toLowerCase());
    }

    /**
     * @return all registered stars in registration order (unmodifiable, never null; empty if none registered).
     */
    public static synchronized List<USSStarDefinition> all() {
        return Collections.unmodifiableList(new ArrayList<>(STARS.values()));
    }

    /**
     * @return the number of registered stars
     */
    public static synchronized int size() {
        return STARS.size();
    }

    /**
     * Resolve the evolution target of a star.
     *
     * @param star the star (null → null)
     * @return the registered star this one evolves into, or null when the star has no target or the target id is not
     *         registered
     */
    public static synchronized USSStarDefinition evolutionTargetOf(USSStarDefinition star) {
        if (star == null || star.getEvolutionTarget() == null) {
            return null;
        }
        return STARS.get(star.getEvolutionTarget());
    }

    /**
     * Remove all registered stars. Intended for tests (and for a future "re-register with different data" flow); not
     * part of normal runtime use.
     */
    public static synchronized void clear() {
        STARS.clear();
    }
}
