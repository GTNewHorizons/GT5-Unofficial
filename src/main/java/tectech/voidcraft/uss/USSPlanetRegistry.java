package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * The registration-based catalog of Voidcraft planets (the replacement for the old fixed {@link USSPlanetType}
 * enum).
 *
 * <p>
 * Planets are {@link USSPlanetDefinition}s registered by a stable {@code id}. The registry is a simple global,
 * insertion-ordered map: register once (see {@link USSPlanetCatalog#registerAll()}), then look up by id, list all,
 * or take the pool of planets a given {@link USSStarType} may host.
 *
 * <p>
 * Deliberately NOT wired into the mining/voidcraft mechanism yet (that is a later pass) — this is the pure
 * registration layer. Thread-safe (synchronized); the backing store is a {@link LinkedHashMap} so {@link #all()} is
 * stable in registration order (important for deterministic generation and tests).
 *
 * <p>
 * Bare-JVM safe: only {@link USSPlanetDefinition}/{@link USSStarType}/{@link gregtech.api.enums.Materials} data —
 * no Forge fluid/block objects.
 */
public final class USSPlanetRegistry {

    private static final Map<String, USSPlanetDefinition> PLANETS = new LinkedHashMap<>();

    private USSPlanetRegistry() {
        throw new AssertionError("Static registry");
    }

    /**
     * Register a planet definition.
     *
     * <p>
     * The definition must carry a non-blank {@code id} (validated by {@link USSPlanetDefinition#builder()}); a
     * duplicate id is a programming error and rejected.
     *
     * @param definition the definition to register (must not be null)
     * @throws NullPointerException     if {@code definition} is null
     * @throws IllegalArgumentException if a planet with the same id is already registered
     */
    public static synchronized void register(USSPlanetDefinition definition) {
        Objects.requireNonNull(definition, "definition");
        if (PLANETS.containsKey(definition.getId())) {
            throw new IllegalArgumentException("Duplicate planet id: " + definition.getId());
        }
        PLANETS.put(definition.getId(), definition);
    }

    /**
     * @param id the planet id (null → null)
     * @return the registered definition, or null when no planet with that id is registered
     */
    public static synchronized USSPlanetDefinition get(String id) {
        return id == null ? null : PLANETS.get(id);
    }

    /**
     * @return true if a planet with that id is registered
     */
    public static synchronized boolean contains(String id) {
        return id != null && PLANETS.containsKey(id);
    }

    /**
     * @return all registered planets in registration order (unmodifiable, never null; empty if none registered).
     */
    public static synchronized List<USSPlanetDefinition> all() {
        return Collections.unmodifiableList(new ArrayList<>(PLANETS.values()));
    }

    /**
     * The pool of planets a star type may host — every registered planet that allows that star type, in registration
     * order.
     *
     * @param starType the star type (null → empty list, defensive)
     * @return the matching planets (unmodifiable, never null; may be empty)
     */
    public static synchronized List<USSPlanetDefinition> pool(USSStarType starType) {
        if (starType == null) {
            return Collections.emptyList();
        }
        List<USSPlanetDefinition> pool = new ArrayList<>();
        for (USSPlanetDefinition planet : PLANETS.values()) {
            if (planet.allowsStarType(starType)) {
                pool.add(planet);
            }
        }
        return Collections.unmodifiableList(pool);
    }

    /**
     * @return the number of registered planets
     */
    public static synchronized int size() {
        return PLANETS.size();
    }

    /**
     * Remove all registered planets. Intended for tests (and for a future "re-register with different data" flow);
     * not part of normal runtime use.
     */
    public static synchronized void clear() {
        PLANETS.clear();
    }
}
