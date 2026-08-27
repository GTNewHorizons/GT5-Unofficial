package tectech.voidcraft.uss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import gregtech.api.enums.Materials;

/**
 * A registered planet: everything that defines a mineable body available around a Voidcraft star.
 *
 * <p>
 * This is the <strong>registration-based</strong> replacement for the old fixed {@link USSPlanetType} enum. Each
 * planet is defined by exactly five things:
 * <ol>
 * <li><b>Texture</b> — the reference drawn as the planet hologram (currently the IORE dimension abbreviation, the
 * {@code gtneioreplugin.ModBlocks} key of the dimension-display block; see {@link USSPlanetType#getVisual()}).</li>
 * <li><b>Size range</b> — a {@code [min, max]} pair of floats, each in {@code [0.0, 5.0]} (the rendered cube scale
 * is drawn from this range).</li>
 * <li><b>Allowed star types</b> — the {@link USSStarType}s this planet may orbit (a planet may be allowed around
 * several star classes, unlike the old one-star-per-type pool).</li>
 * <li><b>Available ores</b> — a list of {@link USSPlanetOre} entries ({@code [ore type, amount (in millions),
 * weight]}).</li>
 * <li><b>Available fluids</b> — a list of {@link Materials} that yield a fluid (resolved later via
 * {@code Materials.get(name).getFluid(mB)} — kept abstract here, never a Forge {@code Fluid}, so this stays
 * bare-JVM-testable).</li>
 * </ol>
 *
 * <p>
 * Pure data + validation: no game world, no RNG, no Forge fluid/block objects — unit-testable in a bare JVM. The
 * registration system (see {@link USSPlanetRegistry}) stores these; the mining mechanism that CONSUMES them is
 * wired in a later pass.
 *
 * <p>
 * Immutable: use {@link #builder()} to construct; all collections are copied and returned unmodifiable.
 */
public final class USSPlanetDefinition {

    /** Minimum allowed value of the planet size range (user spec: float 0.0–5.0). */
    public static final float MIN_SIZE = 0.0f;

    /** Maximum allowed value of the planet size range (user spec: float 0.0–5.0). */
    public static final float MAX_SIZE = 5.0f;

    /** Stable identifier (unique across the registry, e.g. {@code "earth"}). */
    private final String id;

    /** The planet's texture (the resource path of its {@code stitched.png}, relative to the mod's texture root). */
    private final String texture;

    /** The rendered-size tier, set by the texture resolution (determines base scale and the orbit-ring set). */
    private final PlanetTier tier;

    /** Whether this is a gas giant (affects the orbit-ring probability). */
    private final boolean gasGiant;

    /** Lower bound of the size range (inclusive, in {@code [0.0, 5.0]}). */
    private final float sizeMin;

    /** Upper bound of the size range (inclusive, in {@code [0.0, 5.0]}), &gt;= {@link #sizeMin}. */
    private final float sizeMax;

    /** The star types this planet may orbit (never empty). */
    private final List<USSStarType> allowedStarTypes;

    /** The available ores (never null; may be empty). */
    private final List<USSPlanetOre> ores;

    /** The available fluids (materials that yield a fluid; never null; may be empty). */
    private final List<Materials> fluids;

    private USSPlanetDefinition(Builder b) {
        this.id = b.id;
        this.texture = b.texture;
        this.tier = b.tier;
        this.gasGiant = b.gasGiant;
        this.sizeMin = b.sizeMin;
        this.sizeMax = b.sizeMax;
        this.allowedStarTypes = Collections.unmodifiableList(new ArrayList<>(b.allowedStarTypes));
        this.ores = Collections.unmodifiableList(new ArrayList<>(b.ores));
        this.fluids = Collections.unmodifiableList(new ArrayList<>(b.fluids));
    }

    /**
     * @return a fresh {@link Builder} for constructing a definition.
     */
    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
        return id;
    }

    /**
     * @return the planet's texture (the resource path of its {@code stitched.png}, relative to the mod's texture root).
     */
    public String getTexture() {
        return texture;
    }

    /**
     * @return the rendered-size tier (set by the texture resolution).
     */
    public PlanetTier getTier() {
        return tier;
    }

    /**
     * @return true if this is a gas giant (affects the orbit-ring probability).
     */
    public boolean isGasGiant() {
        return gasGiant;
    }

    /**
     * The probability that this planet orbits with a ring, per the signed-off rule: gas giants ring 50% of the time,
     * normal-and-larger non-giants 10%, and tiny/small never.
     *
     * @return the ring probability in {@code [0, 1]}
     */
    public float ringChance() {
        if (gasGiant) {
            return 0.5f;
        }
        if (tier == PlanetTier.TINY || tier == PlanetTier.SMALL) {
            return 0f;
        }
        return 0.1f;
    }

    /**
     * @return the lower bound of the size range (in {@code [0.0, 5.0]}).
     */
    public float getSizeMin() {
        return sizeMin;
    }

    /**
     * @return the upper bound of the size range (in {@code [0.0, 5.0]}), &gt;= {@link #getSizeMin()}.
     */
    public float getSizeMax() {
        return sizeMax;
    }

    /**
     * @param size a candidate size
     * @return true if {@code size} is within this planet's {@code [sizeMin, sizeMax]} range
     */
    public boolean sizeInRange(float size) {
        return size >= sizeMin && size <= sizeMax;
    }

    /**
     * @return the star types this planet may orbit (unmodifiable, never empty).
     */
    public List<USSStarType> getAllowedStarTypes() {
        return allowedStarTypes;
    }

    /**
     * @param starType a star type (null → false)
     * @return true if this planet is allowed to orbit that star type
     */
    public boolean allowsStarType(USSStarType starType) {
        return starType != null && allowedStarTypes.contains(starType);
    }

    /**
     * @return the available ores (unmodifiable, never null; may be empty).
     */
    public List<USSPlanetOre> getOres() {
        return ores;
    }

    /**
     * @return the available fluids (materials that yield a fluid; unmodifiable, never null; may be empty).
     */
    public List<Materials> getFluids() {
        return fluids;
    }

    @Override
    public String toString() {
        return "USSPlanetDefinition[id=" + id
            + ", texture="
            + texture
            + ", tier="
            + tier
            + (gasGiant ? ", gasGiant" : "")
            + ", size="
            + sizeMin
            + "–"
            + sizeMax
            + ", stars="
            + allowedStarTypes
            + ", ores="
            + ores.size()
            + ", fluids="
            + fluids.size()
            + "]";
    }

    /**
     * Fluent builder for {@link USSPlanetDefinition}. Validates on {@link #build()}.
     */
    public static final class Builder {

        private String id;
        private String texture;
        private PlanetTier tier = PlanetTier.NORMAL;
        private boolean gasGiant = false;
        private float sizeMin = 0.35f;
        private float sizeMax = 0.75f;
        private final List<USSStarType> allowedStarTypes = new ArrayList<>();
        private final List<USSPlanetOre> ores = new ArrayList<>();
        private final List<Materials> fluids = new ArrayList<>();

        private Builder() {}

        /**
         * @param id the stable, registry-unique identifier
         * @return this builder
         */
        public Builder id(String id) {
            this.id = id;
            return this;
        }

        /**
         * @param texture the planet's texture (the resource path of its {@code stitched.png}, relative to the mod's
         *                texture root)
         * @return this builder
         */
        public Builder texture(String texture) {
            this.texture = texture;
            return this;
        }

        /**
         * Sets the size tier and derives the {@code sizeRange} from the tier's base scale with a ±10% variation band.
         *
         * @param tier the rendered-size tier (set by the texture resolution)
         * @return this builder
         */
        public Builder tier(PlanetTier tier) {
            if (tier != null) {
                this.tier = tier;
                final float base = tier.baseScale();
                this.sizeMin = base * 0.9f;
                this.sizeMax = base * 1.1f;
            }
            return this;
        }

        /**
         * @param gasGiant whether this is a gas giant (affects the orbit-ring probability)
         * @return this builder
         */
        public Builder gasGiant(boolean gasGiant) {
            this.gasGiant = gasGiant;
            return this;
        }

        /**
         * @param sizeMin lower bound of the size range (in {@code [0.0, 5.0]})
         * @param sizeMax upper bound of the size range (in {@code [0.0, 5.0]}, &gt;= sizeMin)
         * @return this builder
         */
        public Builder sizeRange(float sizeMin, float sizeMax) {
            this.sizeMin = sizeMin;
            this.sizeMax = sizeMax;
            return this;
        }

        /**
         * @param starType a star type this planet may orbit
         * @return this builder
         */
        public Builder allowedStarType(USSStarType starType) {
            if (starType != null) {
                this.allowedStarTypes.add(starType);
            }
            return this;
        }

        /**
         * @param starTypes the star types this planet may orbit (replaces the current list)
         * @return this builder
         */
        public Builder allowedStarTypes(List<USSStarType> starTypes) {
            this.allowedStarTypes.clear();
            if (starTypes != null) {
                for (USSStarType starType : starTypes) {
                    if (starType != null) {
                        this.allowedStarTypes.add(starType);
                    }
                }
            }
            return this;
        }

        /**
         * @param oreType the element
         * @param amount  the amount in millions (&gt;= 0)
         * @param weight  the relative selection weight (&gt; 0)
         * @return this builder
         */
        public Builder ore(Materials oreType, long amount, double weight) {
            this.ores.add(new USSPlanetOre(oreType, amount, weight));
            return this;
        }

        /**
         * @param ores the ore entries (replaces the current list)
         * @return this builder
         */
        public Builder ores(List<USSPlanetOre> ores) {
            this.ores.clear();
            if (ores != null) {
                this.ores.addAll(ores);
            }
            return this;
        }

        /**
         * @param fluid a material that yields a fluid
         * @return this builder
         */
        public Builder fluid(Materials fluid) {
            if (fluid != null && fluid != Materials._NULL) {
                this.fluids.add(fluid);
            }
            return this;
        }

        /**
         * @param fluids the fluid materials (replaces the current list)
         * @return this builder
         */
        public Builder fluids(List<Materials> fluids) {
            this.fluids.clear();
            if (fluids != null) {
                for (Materials fluid : fluids) {
                    if (fluid != null && fluid != Materials._NULL) {
                        this.fluids.add(fluid);
                    }
                }
            }
            return this;
        }

        /**
         * Validate and build the definition.
         *
         * @return the immutable {@link USSPlanetDefinition}
         * @throws NullPointerException     if {@code id} or {@code texture} is null
         * @throws IllegalArgumentException if {@code id}/{@code texture} is blank, the size range is out of
         *                                  {@code [0.0, 5.0]} or inverted, or no star type is allowed
         */
        public USSPlanetDefinition build() {
            if (id == null || id.trim()
                .isEmpty()) {
                throw new IllegalArgumentException("id must be a non-blank string");
            }
            if (texture == null || texture.trim()
                .isEmpty()) {
                throw new IllegalArgumentException("texture must be a non-blank string");
            }
            if (tier == null) {
                throw new IllegalArgumentException("tier must not be null");
            }
            if (!Float.isFinite(sizeMin) || sizeMin < MIN_SIZE || sizeMin > MAX_SIZE) {
                throw new IllegalArgumentException(
                    "sizeMin must be a finite number in [" + MIN_SIZE + ", " + MAX_SIZE + "], got " + sizeMin);
            }
            if (!Float.isFinite(sizeMax) || sizeMax < MIN_SIZE || sizeMax > MAX_SIZE) {
                throw new IllegalArgumentException(
                    "sizeMax must be a finite number in [" + MIN_SIZE + ", " + MAX_SIZE + "], got " + sizeMax);
            }
            if (sizeMin > sizeMax) {
                throw new IllegalArgumentException("sizeMin (" + sizeMin + ") must be <= sizeMax (" + sizeMax + ")");
            }
            if (allowedStarTypes.isEmpty()) {
                throw new IllegalArgumentException("at least one allowed star type is required");
            }
            return new USSPlanetDefinition(this);
        }
    }
}
