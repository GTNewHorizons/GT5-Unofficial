package tectech.voidcraft.uss;

/**
 * The supernova/hypernova explosion render treatment — the pure phase math (bare-JVM safe, unit-tested without a
 * world): the star's short lifetime runs one show — pre-collapse (the star shrinks to a tiny ember, detonating as
 * the shrinking ends, while its brightness pulse decays into the flash) → detonation (the star body's layer
 * gains overdrive into a clipped white-hot flash) → expansion (an additive shock disc travels from the star's rim
 * to the dome radius while the flash decays to an afterglow floor and the body re-inflates from the ember to full
 * size; the GRB jets flare out and fade, the surface churns, and the orbit rings flash as the disc crosses) →
 * afterglow (the gains settle back to the base) → collapse (the core shrinks into the remnant behind a final
 * flash).
 *
 * <p>
 * Every quantity is driven by the star's REMAINING LIFESPAN (the machine re-pushes it to the render block every
 * machine tick while burning), so a stellar-acceleration second — which burns lifespan faster than the world
 * clock — fast-forwards the whole show.
 *
 * <p>
 * The phase tables are per-class ({@code index 0 = supernova, index 1 = hypernova}): the hypernova runs the same
 * show hotter and faster.
 */
public final class USSSupernovaExplosion {

    private USSSupernovaExplosion() {
        throw new AssertionError("Math holder");
    }

    /** The lifetime progress at which the detonation fires (the flash attack ramps and the shock shell launches). */
    public static final float[] DETONATION_START = { 0.05f, 0.04f };

    /** The flash attack window (fraction of the lifetime): the gains ramp from the base to the peak over it. */
    public static final float[] FLASH_ATTACK = { 0.005f, 0.004f };

    /** The detonation flash's peak layer gain (clipped past full-bright by the framebuffer — the white-hot look). */
    public static final float[] FLASH_GAIN = { 6.0f, 9.0f };

    /** The dome flash's decay window (fraction of the lifetime) — the additive wash over the space shell. */
    public static final float[] FLASH_WINDOW = { 0.015f, 0.01f };

    /**
     * The pre-collapse ember scale: the star's rendered scale (× the registered size) — the star shrinks to this
     * and holds it until the detonation's flash.
     */
    public static final float PRE_COLLAPSE_SCALE = 0.05f;

    /**
     * The pre-collapse lead, a fraction of the lifetime: the shrink ends at {@code DETONATION_START − this}, the
     * ember is held until the flash — 0 = the explosion fires as the shrinking ends.
     */
    public static final float[] PRE_COLLAPLE_LEAD = { 0f, 0f };

    /** The lifetime progress at which the shock shell reaches the dome radius (its travel ends). */
    public static final float[] SHELL_TRAVEL_END = { 0.21f, 0.16f };

    /** The shock shell's peak alpha: it blooms to this in the first moments, then fades as it expands. */
    public static final float SHELL_ALPHA_PEAK = 0.8f;

    /** The lifetime progress over which the shell blooms from 0 to its peak alpha (a few virtual seconds). */
    public static final float SHELL_ALPHA_ATTACK = 0.0075f;

    /**
     * The shell's alpha fade exponent: {@code alpha = SHELL_ALPHA_PEAK × (1 − travel)^power} after the bloom —
     * 1.0 = linear (the shell stays visible all the way to the dome radius instead of dying out early).
     */
    public static final float SHELL_ALPHA_FADE_POWER = 1.0f;

    /** The shock shell's tint per class: a light purple supernova, a neon green hypernova. */
    public static final int[] SHELL_COLOR = { 0xFFB070FF, 0xFF39FF14 };

    /** The lifetime progress at which the GRB jets have fully faded out (they ignite with the detonation flash). */
    public static final float[] JET_FADE_END = { 0.20f, 0.15f };

    /**
     * The GRB jet core layer's peak alpha (the outer layers fade by {@code JET_LAYER_ALPHA_DECAY} — the hypernova's
     * jets are the stronger show).
     */
    public static final float[] JET_ALPHA = { 0.5f, 0.8f };

    /** The jet's reach as a fraction of the dome radius (each jet spans most of the system view). */
    public static final float JET_LENGTH_FACTOR = 0.85f;

    /** The outermost jet layer's radius, as a fraction of its length (the ellipsoid beam is a thin capsule). */
    public static final float JET_THICKNESS_FACTOR = 0.05f;

    /** The GRB jet's layer count: the near-white core beam plus the glow shells out to the outermost layer. */
    public static final int JET_LAYERS = 4;

    /** The GRB jet core's radius, × the outermost layer's radius (the innermost of the layered beams). */
    public static final float JET_CORE_RADIUS_FACTOR = 1f / 3f;

    /** The GRB jet's per-layer alpha decay towards the outer (layer {@code i}'s factor is {@code this^i}). */
    public static final float JET_LAYER_ALPHA_DECAY = 0.55f;

    /** The GRB jet core's colour: near-white (both classes). */
    public static final int JET_CORE_COLOR = 0xFFE8F4FF;

    /** The GRB jet outer's colour: light blue (both classes). */
    public static final int JET_OUTER_COLOR = 0xFF70B8FF;

    /** The churn layer's radius, × the star's rendered radius (a close roiling layer just above the surface). */
    public static final float CHURN_RADIUS_FACTOR = 1.05f;

    /** The churn layer's peak alpha (additive over the star's own surface). */
    public static final float CHURN_ALPHA = 0.4f;

    /** The churn's brightness-pulse period in ticks (the fast roll of the surface). */
    public static final float CHURN_PULSE_PERIOD = 8f;

    /** The churn's brightness-pulse depth (the alpha swings to ±this fraction of its peak). */
    public static final float CHURN_PULSE_DEPTH = 0.5f;

    /** The churn layer's radius wobble, × the star's rendered radius (the surface breathing, at its own pace). */
    public static final float CHURN_SWELL = 0.03f;

    /** The churn's radius-wobble period in ticks (decorrelated from the brightness pulse). */
    public static final float CHURN_SWELL_PERIOD = 5f;

    /** The orbit ring's flash peak alpha (as the shock shell crosses it). */
    public static final float RING_FLASH_ALPHA = 0.7f;

    /** The ring flash's decay length in blocks: how far PAST the crossing the ring keeps glowing. */
    public static final float RING_FLASH_DECAY = 8f;

    /** The shock shell's launch radius, × the star's rendered radius (it emerges from the star's rim). */
    public static final float SHELL_START_FACTOR = 1.05f;

    /** The afterglow floor the flash decays to once the shock shell's travel ends. */
    public static final float[] AFTERGLOW_GAIN = { 1.6f, 2.2f };

    /** The lifetime progress at which the collapse starts (the core's shrink + the final flash) — the shell's end. */
    public static final float[] COLLAPSE_START = { 0.21f, 0.16f };

    /**
     * The afterglow's settle window, a fraction of the lifetime: the gains decay from the afterglow floor back to
     * the base gain over it, starting at the shell's end — the collapse can begin inside the settle (it does not
     * truncate it).
     */
    public static final float[] AFTERGLOW_SETTLE = { 0.05f, 0.04f };

    /** The collapse's depth: the core's final scale is {@code 1 − this} (the remnant takes over at that size). */
    public static final float[] COLLAPSE_DEPTH = { 0.85f, 0.88f };

    /** The final flash's peak layer gain (held at the end of the lifetime, until the remnant takes over). */
    public static final float[] FINAL_FLASH_GAIN = { 5.0f, 7.0f };

    /**
     * The final flash's ramp, a fraction of the collapse window (the gains hold 1.0 until it, then ramp to the
     * peak).
     */
    public static final float[] FINAL_FLASH_FRACTION = { 0.06f, 0.05f };

    /** The dome flash's peak alpha. */
    public static final float DOME_FLASH_ALPHA = 0.5f;

    /**
     * The dome flash's radius, × the dome radius — a hair INSIDE the space shell (which writes depth at the dome
     * radius; an equal radius would z-fight its quads).
     */
    public static final float DOME_FLASH_RADIUS_FACTOR = 0.995f;

    /** The brightness pulse's period in ticks (the swelling's rumble, the afterglow's breathing). */
    public static final float PULSE_PERIOD_TICKS = 60f;

    /** The pulse's amplitude during the swelling (decayed to 0 into the detonation). */
    public static final float PULSE_AMP_SWELL = 0.35f;

    /** The pulse's amplitude at the start of the afterglow (decayed to 0 over the shell's travel). */
    public static final float PULSE_AMP_AFTERGLOW = 0.2f;

    /**
     * The show's lifetime progress from the synced remaining lifespan: {@code 1 − remaining/nominal}, clamped to
     * [0..1].
     *
     * @param nominalLifespan the star class's nominal lifespan in machine ticks
     *                        ({@link USSConstants#lifespanForType}; &le; 0 → 0)
     * @param remaining       the synced remaining lifespan in machine ticks (negative = not synced → the show's
     *                        start; the machine re-pushes it every tick while burning)
     * @return the progress in [0..1]
     */
    public static float progress(long nominalLifespan, long remaining) {
        if (nominalLifespan <= 0L) {
            return 0f;
        }
        // A negative remaining (not yet synced, or the star's dying frame) reads as the show's start, not its end:
        // 0 remaining is a real value — the last tick before the remnant takes over.
        if (remaining < 0L) {
            remaining = nominalLifespan;
        }
        if (remaining > nominalLifespan) {
            remaining = nominalLifespan;
        }
        return (float) (1.0 - (double) remaining / (double) nominalLifespan);
    }

    /**
     * The star body's layer-gain multiplier for the show (the renderer feeds it to the shared three-layer
     * sphere): the swelling's pulse, the detonation's ramp to the flash peak, the afterglow's decay to the floor,
     * and the final flash's ramp + hold. Continuous at every phase boundary.
     *
     * @param progress  the show's lifetime progress (0..1)
     * @param hypernova the class variant (false = supernova)
     * @param time      the animation clock in ticks (the virtual orbit time — it runs faster than the world
     *                  during a stellar-acceleration second, fast-forwarding the show)
     * @return the gain multiplier (&ge; 0)
     */
    public static float bodyGain(float progress, boolean hypernova, float time) {
        final int v = hypernova ? 1 : 0;
        final float p = clamp01(progress);
        final float det = DETONATION_START[v];
        if (p < det) {
            // The pulse dies out into the detonation so the flash attack starts from the base gain (no brightness
            // dip or jump at the boundary).
            return 1f + PULSE_AMP_SWELL * (1f - p / det) * pulse(time);
        }
        final float attackEnd = det + FLASH_ATTACK[v];
        if (p < attackEnd) {
            return lerp(1f, FLASH_GAIN[v], smoothstep((p - det) / FLASH_ATTACK[v]));
        }
        final float shellEnd = SHELL_TRAVEL_END[v];
        if (p < shellEnd) {
            final float t = (p - attackEnd) / (shellEnd - attackEnd);
            final float decay = lerp(FLASH_GAIN[v], AFTERGLOW_GAIN[v], t);
            return decay + PULSE_AMP_AFTERGLOW * (1f - t) * pulse(time);
        }
        final float collapse = COLLAPSE_START[v];
        final float settleEnd = Math.max(collapse, shellEnd + AFTERGLOW_SETTLE[v]);
        if (p < settleEnd) {
            final float t = (p - shellEnd) / (settleEnd - shellEnd);
            return lerp(AFTERGLOW_GAIN[v], 1f, t);
        }
        if (p < collapse) {
            return 1f;
        }
        final float w = 1f - collapse;
        if (w <= 0f) {
            return FINAL_FLASH_GAIN[v];
        }
        final float t = (p - collapse) / w;
        final float flashStart = 1f - FINAL_FLASH_FRACTION[v];
        if (t < flashStart) {
            return 1f;
        }
        return lerp(1f, FINAL_FLASH_GAIN[v], (t - flashStart) / (1f - flashStart));
    }

    /**
     * The star body's rendered scale for the show (× the registered star size): the pre-collapse shrink to the
     * ember scale (the flash fires as the shrink ends), the re-inflation to 1.0 while the shock disc expands, and
     * the finale's collapse (see {@link #collapseScale}). Continuous at every phase boundary.
     *
     * @param progress  the show's lifetime progress (0..1)
     * @param hypernova the class variant (false = supernova)
     * @return the scale (&gt; 0)
     */
    public static float bodyScale(float progress, boolean hypernova) {
        final int v = hypernova ? 1 : 0;
        final float p = clamp01(progress);
        final float det = DETONATION_START[v];
        final float preEnd = Math.min(det, det - PRE_COLLAPLE_LEAD[v]);
        if (preEnd > 0f && p < preEnd) {
            return lerp(1f, PRE_COLLAPSE_SCALE, smoothstep(p / preEnd));
        }
        final float shellEnd = SHELL_TRAVEL_END[v];
        if (p < det) {
            return PRE_COLLAPSE_SCALE;
        }
        if (p < shellEnd) {
            return lerp(PRE_COLLAPSE_SCALE, 1f, smoothstep((p - det) / (shellEnd - det)));
        }
        return collapseScale(p, hypernova);
    }

    /**
     * The star body's collapse scale (1.0 = the full rendered radius): holds 1.0 until the collapse starts, then
     * shrinks to {@code 1 − COLLAPSE_DEPTH} with an accelerating (ease-in) curve into the final flash.
     *
     * @param progress  the show's lifetime progress (0..1)
     * @param hypernova the class variant (false = supernova)
     * @return the scale (&gt; 0)
     */
    public static float collapseScale(float progress, boolean hypernova) {
        final int v = hypernova ? 1 : 0;
        final float p = clamp01(progress);
        final float start = COLLAPSE_START[v];
        final float w = 1f - start;
        if (p < start || w <= 0f) {
            return 1f;
        }
        final float t = (p - start) / w;
        return 1f - COLLAPSE_DEPTH[v] * t * t;
    }

    /**
     * The shock shell's travel as a fraction of the rim→dome distance, EASED (fast start, slow end): −1 = not
     * launched yet, 0 = just past the rim, 1 = at the dome radius (held there, never overshooting the dome).
     *
     * @param progress  the show's lifetime progress (0..1)
     * @param hypernova the class variant (false = supernova)
     * @return the eased travel fraction, or −1 when the shell is not visible
     */
    public static float shellRadiusFraction(float progress, boolean hypernova) {
        final int v = hypernova ? 1 : 0;
        final float p = clamp01(progress);
        final float det = DETONATION_START[v];
        final float end = SHELL_TRAVEL_END[v];
        if (p < det || end <= det) {
            return -1f;
        }
        final float t = Math.min(1f, (p - det) / (end - det));
        return 1f - (1f - t) * (1f - t);
    }

    /**
     * The shock shell's alpha (0 = not visible): blooms from 0 to the peak alpha over the first moments of the
     * travel, then fades to 0 as it reaches the dome radius (the fade curve per
     * {@link #SHELL_ALPHA_FADE_POWER}).
     *
     * @param progress  the show's lifetime progress (0..1)
     * @param hypernova the class variant (false = supernova)
     * @return the alpha (&ge; 0)
     */
    public static float shellAlpha(float progress, boolean hypernova) {
        final int v = hypernova ? 1 : 0;
        final float p = clamp01(progress);
        final float det = DETONATION_START[v];
        final float end = SHELL_TRAVEL_END[v];
        if (p <= det || end <= det) {
            return 0f;
        }
        final float attackEnd = det + SHELL_ALPHA_ATTACK;
        final float attack = Math.min(1f, (p - det) / SHELL_ALPHA_ATTACK);
        final float decaySpan = end - attackEnd;
        final float f = decaySpan <= 0f ? 0f : Math.min(1f, Math.max(0f, 1f - (p - attackEnd) / decaySpan));
        return SHELL_ALPHA_PEAK * attack * (float) Math.pow(f, SHELL_ALPHA_FADE_POWER);
    }

    /**
     * The dome flash's alpha (0 = not visible): peaks at the detonation with the full flash alpha, decaying
     * quadratically to 0 over the flash window.
     *
     * @param progress  the show's lifetime progress (0..1)
     * @param hypernova the class variant (false = supernova)
     * @return the alpha (&ge; 0)
     */
    public static float domeFlashAlpha(float progress, boolean hypernova) {
        final int v = hypernova ? 1 : 0;
        final float p = clamp01(progress);
        final float det = DETONATION_START[v];
        final float win = FLASH_WINDOW[v];
        if (p < det || win <= 0f) {
            return 0f;
        }
        final float f = 1f - (p - det) / win;
        if (f <= 0f) {
            return 0f;
        }
        return DOME_FLASH_ALPHA * f * f;
    }

    /**
     * The GRB jets' alpha (0 = off): they ignite with the detonation flash (ramping over the flash attack window)
     * and fade to 0 at the jet's fade end. Continuous at every boundary.
     *
     * @param progress  the show's lifetime progress (0..1)
     * @param hypernova the class variant (false = supernova)
     * @return the alpha (&ge; 0)
     */
    public static float jetAlpha(float progress, boolean hypernova) {
        final int v = hypernova ? 1 : 0;
        final float p = clamp01(progress);
        final float det = DETONATION_START[v];
        final float end = JET_FADE_END[v];
        if (p < det || end <= det) {
            return 0f;
        }
        final float attackEnd = det + FLASH_ATTACK[v];
        if (p < attackEnd) {
            return JET_ALPHA[v] * smoothstep((p - det) / FLASH_ATTACK[v]);
        }
        if (p >= end) {
            return 0f;
        }
        return JET_ALPHA[v] * (1f - smoothstep((p - attackEnd) / (end - attackEnd)));
    }

    /**
     * The GRB jet layer's radius factor (× the outermost layer's radius): linear from the core's radius to 1.0.
     *
     * @param layer the jet layer (0 = the core beam, {@code JET_LAYERS − 1} = the outer)
     * @return the factor (0 &lt; factor &le; 1)
     */
    public static float jetLayerRadiusFactor(int layer) {
        return lerp(JET_CORE_RADIUS_FACTOR, 1f, (float) layer / (JET_LAYERS - 1));
    }

    /**
     * The GRB jet layer's alpha factor (× {@code jetAlpha}'s current fade): geometric decay towards the outer.
     *
     * @param layer the jet layer (0 = the core beam, {@code JET_LAYERS − 1} = the outer)
     * @return the factor (0 &lt; factor &le; 1)
     */
    public static float jetLayerAlphaFactor(int layer) {
        return (float) Math.pow(JET_LAYER_ALPHA_DECAY, layer);
    }

    /**
     * The GRB jet layer's tint (0xRRGGBB): the near-white core to the light-blue outer.
     *
     * @param layer the jet layer (0 = the core beam, {@code JET_LAYERS − 1} = the outer)
     * @return the tint
     */
    public static int jetLayerColor(int layer) {
        return lerpColor(JET_CORE_COLOR, JET_OUTER_COLOR, (float) layer / (JET_LAYERS - 1));
    }

    /**
     * The churn layer's alpha: the peak alpha modulated by the fast surface roll (± the pulse depth).
     *
     * @param time the animation clock in ticks
     * @return the alpha (&ge; 0)
     */
    public static float churnAlpha(float time) {
        return CHURN_ALPHA * (1f + CHURN_PULSE_DEPTH * fastSin(time, CHURN_PULSE_PERIOD));
    }

    /**
     * The churn layer's radius factor, × the star's rendered radius: the base factor with the slow surface
     * breathing (± the swell).
     *
     * @param time the animation clock in ticks
     * @return the factor (&gt; 0)
     */
    public static float churnRadiusFactor(float time) {
        return CHURN_RADIUS_FACTOR + CHURN_SWELL * fastSin(time, CHURN_SWELL_PERIOD);
    }

    /**
     * An orbit ring's flash alpha as the shock shell crosses it: 0 before the crossing, peaking AT the crossing
     * (scaled by the shell's own alpha, so the flash dies with the shell) and decaying exponentially as the shell
     * travels on.
     *
     * @param shellRadius the shock shell's current world radius (blocks; &le; 0 = not launched)
     * @param ringRadius  the planet's orbit radius (blocks)
     * @param shellAlpha  the shell's own alpha (the flash scales with it — dead shell, dead flash)
     * @return the additive alpha (&ge; 0)
     */
    public static float ringFlashAlpha(float shellRadius, float ringRadius, float shellAlpha) {
        if (shellRadius <= 0f || ringRadius <= 0f || shellAlpha <= 0f) {
            return 0f;
        }
        final float past = shellRadius - ringRadius;
        if (past < 0f) {
            return 0f;
        }
        return RING_FLASH_ALPHA * shellAlpha * (float) Math.exp(-past / RING_FLASH_DECAY);
    }

    /** The fast churn clock, reduced into the period before the sin (same float-precision guard as the body pulse). */
    private static float fastSin(float time, float period) {
        final long p = (long) period;
        final long phase = (long) time % p;
        return (float) Math.sin(phase * 2.0 * Math.PI / p);
    }

    private static float pulse(float time) {
        // Reduce into the period before the sin: the world tick outgrows a float's mantissa, and the pulse phase
        // would lose its rhythm on old worlds.
        final long phase = (long) time % (long) PULSE_PERIOD_TICKS;
        return (float) Math.sin(phase * 2.0 * Math.PI / PULSE_PERIOD_TICKS);
    }

    private static float clamp01(float v) {
        return v < 0f ? 0f : (v > 1f ? 1f : v);
    }

    private static float lerp(float a, float b, float t) {
        return a + (b - a) * t;
    }

    private static int lerpColor(int a, int b, float t) {
        final int r = Math.round(lerp((a >> 16) & 0xFF, (b >> 16) & 0xFF, t));
        final int g = Math.round(lerp((a >> 8) & 0xFF, (b >> 8) & 0xFF, t));
        final int bl = Math.round(lerp(a & 0xFF, b & 0xFF, t));
        return 0xFF000000 | (r << 16) | (g << 8) | bl;
    }

    private static float smoothstep(float t) {
        final float s = clamp01(t);
        return s * s * (3f - 2f * s);
    }
}
