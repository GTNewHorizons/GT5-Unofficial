package tectech.voidcraft.uss;

import java.util.function.Supplier;

import gregtech.api.enums.Materials;

/**
 * A registered star: the definition of a star type in a Voidcraft system (the registration-based replacement for the
 * fixed {@link USSStarType} enum as the definition source).
 *
 * <p>
 * Each star is defined by an internal {@code id} (the unique registration key) plus these fields:
 * <ol>
 * <li><b>Name method</b> — a string-returning function ({@link Supplier}) that produces the star's name (so names can
 * be generated procedurally per instance rather than fixed).</li>
 * <li><b>Type</b> — the <em>display</em> type name (e.g. "Blue Giant", "White Dwarf"). Distinct from the
 * {@code id}.</li>
 * <li><b>Size range</b> — a {@code [min, max]} pair of floats, each in {@code [0.0, 10.0]}.</li>
 * <li><b>Main / secondary / tertiary materials</b> — three {@link USSStarMaterial} slots (material + weight + fluid
 * capacity in millions of mB; a zero-capacity slot produces no fluid, so a star produces 1–3 fluids), primarily
 * fluids.</li>
 * <li><b>Luminosity</b> — a float in {@code [0.0, 10.0]}.</li>
 * <li><b>Planet range</b> — a {@code [min, max]} pair of ints, each in {@code [0, 16]} (how many planets the star
 * hosts).</li>
 * <li><b>Texture</b> — the texture reference (a string key for now).</li>
 * <li><b>Evolution target</b> — the {@code id} of the star this one evolves into, or null when it is a terminal
 * type.</li>
 * <li><b>Spacetime ripples</b> — a {@code [min, max]} pair of ints, each in {@code [0, 128]}: the range the system's
 * spacetime-ripple count is sampled from at creation (see {@link USSRipples}; the Explorer ships' target).</li>
 * <li><b>Color</b> — an opaque ARGB int: the color the star's render core is tinted with (what visually
 * distinguishes the star classes — e.g. yellow dwarf orange, white dwarf light blue, blue giant light blue).</li>
 * <li><b>Shell color</b> — an opaque ARGB int (0 = unset): the color the star's outer halo layers are tinted with
 * (the glow that fades out of the core; falls back to the core {@code color} when unset).</li>
 * <li><b>Render type</b> — the {@link USSStarRenderType} custom render treatment of the star (the extra geometry
 * the renderer draws on top of the shared three-layer sphere; default
 * {@link USSStarRenderType#STANDARD} — no extra geometry).</li>
 * </ol>
 *
 * <p>
 * Pure data + validation: no game world, no RNG, no Forge fluid/block objects — unit-testable in a bare JVM. The
 * registration system (see {@link USSStarRegistry}) stores these; the mechanics that CONSUME them (star evolution,
 * luminosity scaling, planet-count drawing) are wired in a later pass.
 *
 * <p>
 * Immutable: use {@link #builder()} to construct.
 */
public final class USSStarDefinition {

    /** Minimum allowed size (user spec: 0.0–10.0). */
    public static final float MIN_SIZE = 0.0f;
    /** Maximum allowed size (user spec: 0.0–10.0). */
    public static final float MAX_SIZE = 10.0f;
    /** Minimum allowed luminosity (user spec: 0.0–10.0). */
    public static final float MIN_LUMINOSITY = 0.0f;
    /** Maximum allowed luminosity (user spec: 0.0–10.0). */
    public static final float MAX_LUMINOSITY = 10.0f;
    /** Minimum allowed planet count (user spec: 0–16). */
    public static final int MIN_PLANETS = 0;
    /** Maximum allowed planet count (user spec: 0–16). */
    public static final int MAX_PLANETS = 16;
    /** Minimum allowed spacetime-ripple count (user spec: 0–128, inclusive). */
    public static final int MIN_RIPPLES = 0;
    /** Maximum allowed spacetime-ripple count (user spec: 0–128, inclusive). */
    public static final int MAX_RIPPLES = 128;

    /** Stable identifier (unique across the registry, e.g. {@code "main_sequence"}). */
    private final String id;

    /** The string-returning name method (field 1). */
    private final Supplier<String> nameMethod;

    /** The display type name (field 2, e.g. "Blue Giant"). Distinct from {@link #id}. */
    private final String type;

    /** Lower bound of the size range (field 3, in {@code [0.0, 10.0]}). */
    private final float sizeMin;
    /** Upper bound of the size range (field 3, in {@code [0.0, 10.0]}), &gt;= {@link #sizeMin}. */
    private final float sizeMax;

    /** Main material (field 4). */
    private final USSStarMaterial main;
    /** Secondary material (field 4). */
    private final USSStarMaterial secondary;
    /** Tertiary material (field 4). */
    private final USSStarMaterial tertiary;

    /** Luminosity (field 5, in {@code [0.0, 10.0]}). */
    private final float luminosity;

    /** Minimum planet count (field 6, in {@code [0, 16]}). */
    private final int planetMin;
    /** Maximum planet count (field 6, in {@code [0, 16]}), &gt;= {@link #planetMin}. */
    private final int planetMax;

    /** Texture reference (field 7). */
    private final String texture;

    /** Evolution target: the {@code id} of the star this one evolves into, or null (field 8). */
    private final String evolutionTarget;

    /**
     * Minimum spacetime-ripple count (field 9, in {@code [0, 128]}) — the lower bound of the star's
     * {@code spacetimeRipples} range.
     */
    private final int rippleMin;
    /**
     * Maximum spacetime-ripple count (field 9, in {@code [0, 128]}), &gt;= {@link #rippleMin} — the upper bound of
     * the star's {@code spacetimeRipples} range. The system's ripple count is sampled inclusively from this range
     * at creation (see {@link USSRipples}).
     */
    private final int rippleMax;

    /**
     * Opaque ARGB color the star's render core is tinted with (field 10) — the per-class visual identity (default
     * white when unset).
     */
    private final int color;

    /**
     * Opaque ARGB color the star's outer halo layers are tinted with (field 11; 0 = unset → the core color).
     */
    private final int shellColor;

    /**
     * The custom render treatment of the star (field 12) — the extra geometry the renderer draws on top of the
     * shared three-layer sphere (default {@link USSStarRenderType#STANDARD}).
     */
    private final USSStarRenderType renderType;

    private USSStarDefinition(Builder b) {
        this.id = b.id;
        this.nameMethod = b.nameMethod;
        this.type = b.type;
        this.sizeMin = b.sizeMin;
        this.sizeMax = b.sizeMax;
        this.main = b.main;
        this.secondary = b.secondary;
        this.tertiary = b.tertiary;
        this.luminosity = b.luminosity;
        this.planetMin = b.planetMin;
        this.planetMax = b.planetMax;
        this.texture = b.texture;
        this.evolutionTarget = b.evolutionTarget;
        this.rippleMin = b.rippleMin;
        this.rippleMax = b.rippleMax;
        this.color = b.color;
        this.shellColor = b.shellColor;
        this.renderType = b.renderType;
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
     * @return the string-returning name method (never null).
     */
    public Supplier<String> getNameMethod() {
        return nameMethod;
    }

    /**
     * @return a freshly generated name (invokes {@link #getNameMethod()}).
     */
    public String name() {
        return nameMethod.get();
    }

    /**
     * @return the display type name (e.g. "Blue Giant").
     */
    public String getType() {
        return type;
    }

    public float getSizeMin() {
        return sizeMin;
    }

    public float getSizeMax() {
        return sizeMax;
    }

    /**
     * @param size a candidate size
     * @return true if {@code size} is within this star's {@code [sizeMin, sizeMax]} range
     */
    public boolean sizeInRange(float size) {
        return size >= sizeMin && size <= sizeMax;
    }

    public USSStarMaterial getMain() {
        return main;
    }

    public USSStarMaterial getSecondary() {
        return secondary;
    }

    public USSStarMaterial getTertiary() {
        return tertiary;
    }

    /**
     * @return the three material slots in order (main, secondary, tertiary)
     */
    public USSStarMaterial[] getMaterials() {
        return new USSStarMaterial[] { main, secondary, tertiary };
    }

    public float getLuminosity() {
        return luminosity;
    }

    public int getPlanetMin() {
        return planetMin;
    }

    public int getPlanetMax() {
        return planetMax;
    }

    /**
     * @param count a candidate planet count
     * @return true if {@code count} is within this star's {@code [planetMin, planetMax]} range
     */
    public boolean planetCountInRange(int count) {
        return count >= planetMin && count <= planetMax;
    }

    /**
     * @return the lower bound of the {@code spacetimeRipples} range (in {@code [0, 128]})
     */
    public int getRippleMin() {
        return rippleMin;
    }

    /**
     * @return the upper bound of the {@code spacetimeRipples} range (in {@code [0, 128]}, &gt;=
     *         {@link #getRippleMin()})
     */
    public int getRippleMax() {
        return rippleMax;
    }

    /**
     * @param count a candidate ripple count
     * @return true if {@code count} is within this star's {@code [rippleMin, rippleMax]} range
     */
    public boolean rippleCountInRange(int count) {
        return count >= rippleMin && count <= rippleMax;
    }

    public String getTexture() {
        return texture;
    }

    /**
     * @return the {@code id} of the star this one evolves into, or null when it is a terminal type.
     */
    public String getEvolutionTarget() {
        return evolutionTarget;
    }

    /**
     * @return the opaque ARGB color the star's render core is tinted with (field 10).
     */
    public int getColor() {
        return color;
    }

    /**
     * @return the opaque ARGB color the star's outer halo layers are tinted with (field 11; 0 = unset, the halo
     *         falls back to the core color).
     */
    public int getShellColor() {
        return shellColor;
    }

    /**
     * @return the custom render treatment of the star (field 12; never null, default
     *         {@link USSStarRenderType#STANDARD}).
     */
    public USSStarRenderType getRenderType() {
        return renderType;
    }

    @Override
    public String toString() {
        return "USSStarDefinition[id=" + id
            + ", type="
            + type
            + ", size="
            + sizeMin
            + "–"
            + sizeMax
            + ", lum="
            + luminosity
            + ", planets="
            + planetMin
            + "–"
            + planetMax
            + ", ripples="
            + rippleMin
            + "–"
            + rippleMax
            + ", evolvesTo="
            + evolutionTarget
            + ", color=0x"
            + Integer.toHexString(color)
            + (shellColor != 0 ? ", shell=0x" + Integer.toHexString(shellColor) : "")
            + (renderType != USSStarRenderType.STANDARD ? ", render=" + renderType : "")
            + "]";
    }

    /**
     * Fluent builder for {@link USSStarDefinition}. Validates on {@link #build()}.
     */
    public static final class Builder {

        private String id;
        private Supplier<String> nameMethod;
        private String type;
        private float sizeMin = MIN_SIZE;
        private float sizeMax = MAX_SIZE;
        private USSStarMaterial main;
        private USSStarMaterial secondary;
        private USSStarMaterial tertiary;
        private float luminosity = 1.0f;
        private int planetMin = MIN_PLANETS;
        private int planetMax = MAX_PLANETS;
        private String texture;
        private String evolutionTarget;
        private int rippleMin = MIN_RIPPLES;
        private int rippleMax = MAX_RIPPLES;
        private int color = 0xFFFFFFFF;
        private int shellColor = 0;
        private USSStarRenderType renderType = USSStarRenderType.STANDARD;

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
         * @param nameMethod the string-returning name method (field 1)
         * @return this builder
         */
        public Builder nameMethod(Supplier<String> nameMethod) {
            this.nameMethod = nameMethod;
            return this;
        }

        /**
         * @param type the display type name (field 2, e.g. "Blue Giant")
         * @return this builder
         */
        public Builder type(String type) {
            this.type = type;
            return this;
        }

        /**
         * @param sizeMin lower bound of the size range (field 3, in {@code [0.0, 10.0]})
         * @param sizeMax upper bound of the size range (field 3, in {@code [0.0, 10.0]}, &gt;= sizeMin)
         * @return this builder
         */
        public Builder sizeRange(float sizeMin, float sizeMax) {
            this.sizeMin = sizeMin;
            this.sizeMax = sizeMax;
            return this;
        }

        /**
         * @param material the main material (field 4)
         * @param weight   its relative weight
         * @param amount   its fluid capacity in millions of mB (0 = produces no fluid)
         * @return this builder
         */
        public Builder main(Materials material, double weight, long amount) {
            this.main = new USSStarMaterial(material, weight, amount);
            return this;
        }

        /**
         * @param material the secondary material (field 4)
         * @param weight   its relative weight
         * @param amount   its fluid capacity in millions of mB (0 = produces no fluid)
         * @return this builder
         */
        public Builder secondary(Materials material, double weight, long amount) {
            this.secondary = new USSStarMaterial(material, weight, amount);
            return this;
        }

        /**
         * @param material the tertiary material (field 4)
         * @param weight   its relative weight
         * @param amount   its fluid capacity in millions of mB (0 = produces no fluid)
         * @return this builder
         */
        public Builder tertiary(Materials material, double weight, long amount) {
            this.tertiary = new USSStarMaterial(material, weight, amount);
            return this;
        }

        /**
         * @param luminosity the luminosity (field 5, in {@code [0.0, 10.0]})
         * @return this builder
         */
        public Builder luminosity(float luminosity) {
            this.luminosity = luminosity;
            return this;
        }

        /**
         * @param planetMin lower bound of the planet range (field 6, in {@code [0, 16]})
         * @param planetMax upper bound of the planet range (field 6, in {@code [0, 16]}, &gt;= planetMin)
         * @return this builder
         */
        public Builder planetRange(int planetMin, int planetMax) {
            this.planetMin = planetMin;
            this.planetMax = planetMax;
            return this;
        }

        /**
         * @param rippleMin lower bound of the {@code spacetimeRipples} range (field 9, in {@code [0, 128]})
         * @param rippleMax upper bound of the {@code spacetimeRipples} range (field 9, in {@code [0, 128]}, &gt;=
         *                  rippleMin)
         * @return this builder
         */
        public Builder rippleRange(int rippleMin, int rippleMax) {
            this.rippleMin = rippleMin;
            this.rippleMax = rippleMax;
            return this;
        }

        /**
         * @param texture the texture reference (field 7)
         * @return this builder
         */
        public Builder texture(String texture) {
            this.texture = texture;
            return this;
        }

        /**
         * @param evolutionTarget the {@code id} of the star this one evolves into, or null (field 8)
         * @return this builder
         */
        public Builder evolutionTarget(String evolutionTarget) {
            this.evolutionTarget = evolutionTarget;
            return this;
        }

        /**
         * @param color the opaque ARGB color the star's render core is tinted with (field 10; default white)
         * @return this builder
         */
        public Builder color(int color) {
            this.color = color;
            return this;
        }

        /**
         * @param shellColor the opaque ARGB color the star's outer halo layers are tinted with (field 11; 0 = unset
         *                   → the core color)
         * @return this builder
         */
        public Builder shellColor(int shellColor) {
            this.shellColor = shellColor;
            return this;
        }

        /**
         * @param renderType the custom render treatment of the star (field 12; null →
         *                   {@link USSStarRenderType#STANDARD})
         * @return this builder
         */
        public Builder renderType(USSStarRenderType renderType) {
            this.renderType = (renderType == null ? USSStarRenderType.STANDARD : renderType);
            return this;
        }

        /**
         * Validate and build the definition.
         *
         * @return the immutable {@link USSStarDefinition}
         * @throws IllegalArgumentException if {@code id}/{@code type}/{@code texture} is blank, the name method is
         *                                  null, a size/luminosity bound is out of range, a planet bound is out of
         *                                  range, a range is inverted, or a material slot is missing
         */
        public USSStarDefinition build() {
            if (id == null || id.trim()
                .isEmpty()) {
                throw new IllegalArgumentException("id must be a non-blank string");
            }
            if (nameMethod == null) {
                throw new IllegalArgumentException("nameMethod must not be null");
            }
            if (type == null || type.trim()
                .isEmpty()) {
                throw new IllegalArgumentException("type must be a non-blank string");
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
            if (main == null) {
                throw new IllegalArgumentException("main material is required");
            }
            if (secondary == null) {
                throw new IllegalArgumentException("secondary material is required");
            }
            if (tertiary == null) {
                throw new IllegalArgumentException("tertiary material is required");
            }
            if (!Float.isFinite(luminosity) || luminosity < MIN_LUMINOSITY || luminosity > MAX_LUMINOSITY) {
                throw new IllegalArgumentException(
                    "luminosity must be a finite number in [" + MIN_LUMINOSITY
                        + ", "
                        + MAX_LUMINOSITY
                        + "], got "
                        + luminosity);
            }
            if (planetMin < MIN_PLANETS || planetMin > MAX_PLANETS) {
                throw new IllegalArgumentException(
                    "planetMin must be in [" + MIN_PLANETS + ", " + MAX_PLANETS + "], got " + planetMin);
            }
            if (planetMax < MIN_PLANETS || planetMax > MAX_PLANETS) {
                throw new IllegalArgumentException(
                    "planetMax must be in [" + MIN_PLANETS + ", " + MAX_PLANETS + "], got " + planetMax);
            }
            if (planetMin > planetMax) {
                throw new IllegalArgumentException(
                    "planetMin (" + planetMin + ") must be <= planetMax (" + planetMax + ")");
            }
            if (rippleMin < MIN_RIPPLES || rippleMin > MAX_RIPPLES) {
                throw new IllegalArgumentException(
                    "rippleMin must be in [" + MIN_RIPPLES + ", " + MAX_RIPPLES + "], got " + rippleMin);
            }
            if (rippleMax < MIN_RIPPLES || rippleMax > MAX_RIPPLES) {
                throw new IllegalArgumentException(
                    "rippleMax must be in [" + MIN_RIPPLES + ", " + MAX_RIPPLES + "], got " + rippleMax);
            }
            if (rippleMin > rippleMax) {
                throw new IllegalArgumentException(
                    "rippleMin (" + rippleMin + ") must be <= rippleMax (" + rippleMax + ")");
            }
            if (texture == null || texture.trim()
                .isEmpty()) {
                throw new IllegalArgumentException("texture must be a non-blank string");
            }
            return new USSStarDefinition(this);
        }
    }
}
