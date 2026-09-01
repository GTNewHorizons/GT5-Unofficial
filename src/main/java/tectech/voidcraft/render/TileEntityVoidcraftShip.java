package tectech.voidcraft.render;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

import tectech.thing.block.TileEntityEyeOfHarmony;

/**
 * The ship fleet anchor (Phase 4 pass 5): ONE invisible, non-collidable block per Unstable Solar System carrying
 * the WHOLE in-flight fleet (up to {@code USSConstants.MAX_SHIPS_PER_USS} ships) as a list of entries, each with
 * the ship payload + state + mission target + anchor-relative gateway the client renderer needs — plus the
 * system's planet specs + star size the client resolves the targets against (pass 7).
 *
 * <p>
 * Pass 4 stored one ship per block (a block per slot); pass 5 scales to dozens–hundreds of ships with a single
 * fleet block — the cost of a 100-ship fleet is 100 small draw calls, not 100 world blocks / NBT blobs.
 *
 * <p>
 * Pattern mirrors the legacy EoH render block (description packet + {@code onDataPacket} sync), but this is a
 * brand-new TE — the legacy render classes are untouched.
 *
 * <p>
 * Client animation (OUTBOUND/MINING/RETURNING) is driven locally from each entry's {@code state} plus the ship's
 * speed/mining power (both inside the payload NBT) using the same {@code USSConstants} leg durations the server
 * ticks with — the server only has to update the entry state on each transition (and each ship's animation phase +
 * swarm spread are keyed client-side on the per-launch seed — see {@code tectech.voidcraft.uss.USSFleetOrbit} and
 * {@link #TAG_ENTRY_SEED}).
 *
 * <p>
 * Pass 7: the TE also carries the SYSTEM (planet specs + star size, {@link #setSystem}) — the client resolves
 * each entry's mission target ({@link #TAG_ENTRY_TARGET}) against it to hover above the planet's live rendered
 * position (or 2.5 blocks above the star center for Starlifters).
 */
public class TileEntityVoidcraftShip extends TileEntity {

    private static final String TAG_FLEET = "vc_fleet";

    /** Entry tags — written by the USS MTE, read by the client renderer. */
    public static final String TAG_ENTRY_PAYLOAD = "vc_payload";
    public static final String TAG_ENTRY_STATE = "vc_state";
    public static final String TAG_ENTRY_GW_REL = "vc_gw_rel";

    /**
     * Entry tag (pass 7): the ship's mission target — a planet index into {@link #getSystemPlanets()} (the ship
     * hovers 0.5 blocks above that planet's rendered position), a RIPPLE-POINT index (0..342, the Explorer pass)
     * for Explorers, or {@code -1} for the star itself (Starlifters hover 2.5 above the star center). The
     * destination is resolved CLIENT-side against the system below.
     */
    public static final String TAG_ENTRY_TARGET = "vc_target";

    /**
     * Entry tag (the Explorer pass): the ship's RESOLVED destination in fleet-anchor blocks (the server resolved it
     * at launch — the planet shell point, the star center, or the ripple point's position). Present when the ship
     * has a destination; the client uses it directly (the ripple point index in {@link #TAG_ENTRY_TARGET} is NOT a
     * planet index, so {@code targetBody} cannot resolve it). Absent for legacy entries → the client falls back to
     * {@code targetBody(target)}.
     */
    public static final String TAG_ENTRY_DEST = "vc_dest";

    /**
     * Entry tag (pass 26 — the travel-time rendering fix): the ship's TRAVEL DISTANCE in fleet-anchor blocks
     * (the server's {@code VoidcraftActiveShip.travelDistance}, the block-separation between the launch origin and
     * the destination). Written by the USS MTE, read by the client's leg-progress math. The client USED to read
     * {@code vc_tdist} off the ship PAYLOAD, where it was never written — so every travel leg animated at the
     * minimum floor (the ship "zipped" across the system in 1 s regardless of the server's real minutes-long
     * duration, and the working-state beam/scan never had a visible window). Now the ACTUAL distance rides along
     * in the entry and the client animates each leg for its true length.
     */
    public static final String TAG_ENTRY_TDIST = "vc_tdist";

    /**
     * System tags (pass 7): the planet specs + star size the client renders the planets AND the ships' dynamic
     * hover targets from (the same specs the star render TE draws — self-contained, no world lookups).
     */
    public static final String TAG_SYSTEM_PLANETS = "vc_system_planets";
    public static final String TAG_STAR_SIZE = "vc_star_size";

    /**
     * System tag (the Explorer pass): the REVEALED spacetime-ripple positions — one entry per ripple point that has
     * been scanned (revealed) and IS a ripple. Each entry is {@code [x, y, z]} in fleet-anchor blocks (the same
     * coordinate frame the ships + star use). The client renders each as a pulsating dark-blue transparent triangle.
     * Hidden ripples (not yet scanned) and revealed NON-ripples are absent — only revealed ripples render.
     */
    public static final String TAG_RIPPLES = "vc_ripples";

    /**
     * System tag (the infrastructure-builder pass): the RIPPLE-scale infrastructure shells — one entry per revealed
     * spacetime ripple carrying a built Stabilizer shell. Each entry is {@code [x, y, z, count, capacity]} in
     * fleet-anchor blocks (the same frame the revealed ripples ride in). The client renders each as a small gray /
     * dark-purple triangle shell at a fill of count/capacity. Only ripples that are revealed (scanned) and carry a
     * built shell ride here (hidden ripples + shells without a revealed ripple stay absent).
     */
    public static final String TAG_RIPPLE_INFRA = "vc_ripple_infra";

    /**
     * System tag: the system's GATEWAY positions — each entry is {@code [x, y, z]} in fleet-anchor blocks, one per
     * gateway machine that is locked onto this USS (the MTE registers them on its launch-target scan). The client
     * renders each as the space-dome gateway (gray tube + cyan event plane) — the gateways are a permanent part of
     * the system view, so they ride with the system data and render even when no ship is in flight.
     */
    public static final String TAG_GATEWAYS = "vc_gateways";

    /**
     * Pass 5.1: the per-launch identity seed (unique per flight even for duplicated ship items, which share the
     * item's {@code vc_uuid}). The client keys this ship's animation phase and swarm spread on it (0 = legacy →
     * item-UUID fallback).
     */
    public static final String TAG_ENTRY_SEED = "vc_seed";

    /**
     * Entry tag (programming framework, Phase C): the ship's CURRENT position in fleet-anchor coordinates (its
     * launch origin — the gateway — then the last leg's endpoint). A HOLDING ship (state {@code HOVERING}, and a
     * fresh ship at launch) renders EXACTLY here (+ swarm spread) — the body below is irrelevant for a hold.
     * Written by the USS MTE, read by the client renderer.
     */
    public static final String TAG_ENTRY_POS = "vc_pos";

    /**
     * Entry tag (programming framework, Phase C): static hover — true when the ship's hover body is a FIXED point
     * (a ripple point, a ship rendezvous) and the client must hover the resolved DESTINATION exactly; false when
     * the client should track the body's LIVE position (a planet keeps orbiting; the star is static either way).
     */
    public static final String TAG_ENTRY_STATIC = "vc_static";

    /**
     * Entry tag (programming framework, Phase C): the leg identity — the ship's monotonic leg counter. The client
     * resets its leg-progress phase when this changes, so consecutive legs of the SAME state (a program doing
     * MOVE → MOVE) animate from their own start instead of continuing the previous leg's progress.
     */
    public static final String TAG_ENTRY_LEG_ID = "vc_leg_id";

    /**
     * Entry tag (the command-split pass): the ship's CURRENT leg's WORK KIND (see
     * {@code USSWorkKind}: 0 travel / 1 mine / 2 scan / 3 siphon). A work leg's duration depends on the KIND (the
     * work command that started it) — a ship with both mining and scan power mines at its mining
     * power and scans at its scan power. The client derives the SAME duration the server ticks from this. 0 /
     * absent → the travel table.
     */
    public static final String TAG_ENTRY_WORK_KIND = "vc_work_kind";

    // Voidbase (Phase D): the in-USS construction sites + standing bases render from this SAME fleet anchor —
    // each entry carries the anchor target with the SHIP-ENTRY PROTOCOL (TAG_ENTRY_TARGET: -1 star / planet
    // index; TAG_ENTRY_STATIC + TAG_ENTRY_DEST: a resolved fixed point) plus the tags below.
    public static final String TAG_SITES = "vc_sites";
    public static final String TAG_BASES = "vc_bases";
    public static final String TAG_SITE_PROGRESS = "vc_site_progress";
    public static final String TAG_SITE_DIMS = "vc_site_dims";
    public static final String TAG_BASE_INTEGRITY = "vc_base_integrity";
    public static final String TAG_BASE_INTEGRITY_MAX = "vc_base_integrity_max";
    /** The base's seed (the client's per-base animation-phase key). */
    public static final String TAG_BASE_SEED = "vc_base_seed";
    /** The active mining-leg id (0 = no mining leg; >0 = the base is mining - the client draws the beam). */
    public static final String TAG_BASE_MINING_LEG = "vc_base_mining_leg";

    // The active CONSTRUCT leg on a construction site (the client's constructor-beam animation): the leg id
    // (0 = no leg - the beam fades out), the leg's full duration in ticks (the client's fade denominator), and
    // the executing Constructor's seed (the client pairs the beam's ship endpoint on it).
    public static final String TAG_SITE_CONSTRUCT_LEG = "vc_site_construct_leg";
    public static final String TAG_SITE_CONSTRUCT_TOTAL = "vc_site_construct_total";
    public static final String TAG_SITE_CONSTRUCT_SEED = "vc_site_construct_seed";

    /**
     * Fleet tag (ship-to-ship cargo transfer): the in-flight SEND / TAKE transfers — the client draws a gray beam
     * for each. Each entry is a compound carrying the EXECUTING ship's uuid ({@link #TAG_TRANSFER_SOURCE}) and the
     * target ship's uuid ({@link #TAG_TRANSFER_TARGET}); the client pairs each to the rendered ship holding that
     * uuid (first match — the same identity the server resolves the transfer against).
     */
    public static final String TAG_TRANSFERS = "vc_transfers";
    public static final String TAG_TRANSFER_SOURCE = "vc_tr_src";
    public static final String TAG_TRANSFER_TARGET = "vc_tr_tgt";

    /**
     * The transfer's target is the STAR's Stellar Injector cargo buffer (not a rendered ship): the client draws
     * the beam's endpoint at the star center.
     */
    public static final String TAG_TRANSFER_STAR = "vc_tr_star";

    /** The USS virtual orbit clock + the world tick it was sampled at (0 = not set — legacy world-time clock). */
    public static final String TAG_ORBIT_TIME = "vc_orbit_time";
    public static final String TAG_ORBIT_SYNC = "vc_orbit_sync";

    /**
     * Render bounding-box half-extent (covers the flight path from gateway to hover point + swarm spread): the
     * gateway scans up to 32 blocks from the USS center and the anchor sits 16 further behind it — 64 covers the
     * worst case (48) plus margin, so the fleet TE never drops out of the render list while the player stands at
     * the gateway.
     */
    private static final double RENDER_RADIUS = 64;

    private final List<NBTTagCompound> ships = new ArrayList<NBTTagCompound>();

    // Pass 7: the system's planets (specs in star-block coordinates) + star size — the client resolves each
    // ship's mission target (TAG_ENTRY_TARGET) against these to hover above the planet's RENDERED position.
    private final List<TileEntityEyeOfHarmony.PlanetSpec> systemPlanets = new ArrayList<TileEntityEyeOfHarmony.PlanetSpec>();
    private float starSize = 0.4f;

    /**
     * The USS virtual orbit clock (machine ticks) + the world tick it was sampled at (0 = not set — the client
     * falls back to the world clock): the same pair the star render TE carries, so the ships' hover math and the
     * star's planet orbits run on ONE clock.
     */
    private long ussOrbitTime = 0;
    private long ussSyncedWorldTime = 0;

    // The Explorer pass: the revealed ripple positions ([x, y, z] in fleet-anchor blocks) — the client renders each
    // as a pulsating dark-blue transparent triangle. Only revealed ripples are present (hidden + non-ripple absent).
    private final List<float[]> revealedRipples = new ArrayList<float[]>();

    // The infrastructure-builder pass: the ripple-scale infrastructure shells ([x, y, z, count, capacity] in
    // fleet-anchor blocks) — the client renders each as a small gray / dark-purple triangle shell at a fill of
    // count/capacity. Only revealed ripples carrying a built shell are present.
    private final List<float[]> rippleInfraShells = new ArrayList<float[]>();

    // The system's gateways ([x, y, z] in fleet-anchor blocks, one per locked-on gateway machine) — the client
    // renders each as the space-dome gateway, always (the gateways render even with an empty fleet).
    private final List<int[]> gateways = new ArrayList<int[]>();

    // Voidbase (Phase D): the in-USS construction sites + standing bases (rendered from this same anchor).
    private final List<NBTTagCompound> baseSites = new ArrayList<NBTTagCompound>();
    private final List<NBTTagCompound> bases = new ArrayList<NBTTagCompound>();

    // Ship-to-ship cargo transfers (SEND / TAKE): each entry pairs the executing + target ship uuids (the client's
    // gray transfer beams).
    private final List<NBTTagCompound> transfers = new ArrayList<NBTTagCompound>();

    private AxisAlignedBB boundingBox;

    @Override
    public double getMaxRenderDistanceSquared() {
        return Double.MAX_VALUE;
    }

    /**
     * Keep the hologram active even when the anchor block itself is off-screen (the ships render elsewhere).
     */
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        if (boundingBox == null) {
            boundingBox = AxisAlignedBB.getBoundingBox(
                xCoord - RENDER_RADIUS,
                yCoord - RENDER_RADIUS,
                zCoord - RENDER_RADIUS,
                xCoord + RENDER_RADIUS + 1,
                yCoord + RENDER_RADIUS + 1,
                zCoord + RENDER_RADIUS + 1);
        }
        return boundingBox;
    }

    /**
     * @return the fleet entries (each: payload + state + gateway/hover rel) in slot order; never null
     */
    public List<NBTTagCompound> getShips() {
        return Collections.unmodifiableList(ships);
    }

    /**
     * Replace the whole fleet (the MTE rebuilds the entry list on every launch/completion/state change).
     */
    public void setShips(List<NBTTagCompound> entries) {
        ships.clear();
        if (entries != null) {
            for (NBTTagCompound entry : entries) {
                ships.add(entry);
            }
        }
    }

    public int getShipCount() {
        return ships.size();
    }

    /**
     * Install the system the fleet works (pass 7): the planet specs (the SAME ones the star render TE draws —
     * star-block coordinates) + the star's rendered size. The client uses both to resolve mission targets to
     * live planet positions, so the ships hover exactly above the rendered bodies.
     */
    public void setSystem(List<TileEntityEyeOfHarmony.PlanetSpec> specs, float starSize) {
        systemPlanets.clear();
        if (specs != null) {
            for (TileEntityEyeOfHarmony.PlanetSpec spec : specs) {
                if (spec != null) {
                    systemPlanets.add(spec);
                }
            }
        }
        this.starSize = starSize > 0 ? starSize : 0.4f;
    }

    /** @return the system planet specs (star-block coordinates), in system order (never null). */
    public List<TileEntityEyeOfHarmony.PlanetSpec> getSystemPlanets() {
        return Collections.unmodifiableList(systemPlanets);
    }

    /** @return the star's rendered size (orbit radius = 0.2 + distance + 0.2·starSize — same as the star TE). */
    public float getStarSize() {
        return starSize;
    }

    /** @return the USS virtual orbit clock (0 = not set — the client uses the world clock). */
    public long getUssOrbitTime() {
        return ussOrbitTime;
    }

    /** @return the world tick the virtual orbit clock was last sampled at (0 when unset). */
    public long getUssSyncedWorldTime() {
        return ussSyncedWorldTime;
    }

    /** Install the USS virtual orbit clock pair (0/0 clears it back to the legacy world-time clock). */
    public void setUssOrbitTime(long orbitTime, long syncedWorldTime) {
        this.ussOrbitTime = orbitTime;
        this.ussSyncedWorldTime = syncedWorldTime;
    }

    /**
     * Install the revealed spacetime-ripple positions (the Explorer pass). Each entry is {@code [x, y, z]} in
     * fleet-anchor blocks. The client renders each as a pulsating dark-blue transparent triangle.
     */
    public void setRevealedRipples(List<float[]> ripples) {
        revealedRipples.clear();
        if (ripples != null) {
            for (float[] r : ripples) {
                if (r != null && r.length == 3) {
                    revealedRipples.add(r);
                }
            }
        }
    }

    /**
     * @return the revealed ripple positions ({@code [x, y, z]} in fleet-anchor blocks), in reveal order (never null)
     */
    public List<float[]> getRevealedRipples() {
        return Collections.unmodifiableList(revealedRipples);
    }

    /**
     * Install the ripple-scale infrastructure shells (the infrastructure-builder pass). Each entry is
     * {@code [x, y, z, count, capacity]} in fleet-anchor blocks. The client renders each as a small triangle shell
     * at a fill of count/capacity.
     */
    public void setRippleInfraShells(List<float[]> shells) {
        rippleInfraShells.clear();
        if (shells != null) {
            for (float[] s : shells) {
                if (s != null && s.length == 5) {
                    rippleInfraShells.add(s);
                }
            }
        }
    }

    /**
     * @return the ripple-scale infrastructure shells ({@code [x, y, z, count, capacity]} in fleet-anchor blocks),
     *         in field order (never null)
     */
    public List<float[]> getRippleInfraShells() {
        return Collections.unmodifiableList(rippleInfraShells);
    }

    /**
     * Replace the whole system-gateway list (the MTE re-registers on every launch-target scan and prunes destroyed
     * gateways). Each entry: {@code [x, y, z]} in fleet-anchor blocks.
     */
    public void setGateways(List<int[]> rels) {
        gateways.clear();
        if (rels != null) {
            for (int[] rel : rels) {
                if (rel != null && rel.length == 3) {
                    gateways.add(rel);
                }
            }
        }
    }

    /** @return the system's gateway positions ({@code [x, y, z]} in fleet-anchor blocks) (never null) */
    public List<int[]> getGateways() {
        return Collections.unmodifiableList(gateways);
    }

    /**
     * Replace the whole construction-site list (the MTE rebuilds the entries on every site create/fill/complete).
     * Each entry: the anchor target (ship-entry protocol) + {@link #TAG_SITE_PROGRESS} + {@link #TAG_SITE_DIMS}.
     */
    public void setBaseSites(List<NBTTagCompound> entries) {
        baseSites.clear();
        if (entries != null) {
            for (NBTTagCompound entry : entries) {
                if (entry != null) {
                    baseSites.add(entry);
                }
            }
        }
    }

    /** @return the construction-site entries (never null). */
    public List<NBTTagCompound> getBaseSites() {
        return Collections.unmodifiableList(baseSites);
    }

    /**
     * Replace the whole standing-base list (the MTE rebuilds the entries on every spawn/discard/integrity
     * change and on a mining-leg start/end). Each entry: the anchor target (ship-entry protocol) +
     * {@link #TAG_ENTRY_PAYLOAD} + the integrity pair ({@link #TAG_BASE_INTEGRITY} /
     * {@link #TAG_BASE_INTEGRITY_MAX}) + the seed ({@link #TAG_BASE_SEED}) + the mining-leg id
     * ({@link #TAG_BASE_MINING_LEG}).
     */
    public void setBases(List<NBTTagCompound> entries) {
        bases.clear();
        if (entries != null) {
            for (NBTTagCompound entry : entries) {
                if (entry != null) {
                    bases.add(entry);
                }
            }
        }
    }

    /** @return the standing-base entries (never null). */
    public List<NBTTagCompound> getBases() {
        return Collections.unmodifiableList(bases);
    }

    /**
     * Replace the whole transfer list (the MTE rebuilds it on every transfer start/end). Each entry: the executing
     * ship's uuid ({@link #TAG_TRANSFER_SOURCE}) + the target ship's uuid ({@link #TAG_TRANSFER_TARGET}).
     */
    public void setTransfers(List<NBTTagCompound> entries) {
        transfers.clear();
        if (entries != null) {
            for (NBTTagCompound entry : entries) {
                if (entry != null) {
                    transfers.add(entry);
                }
            }
        }
    }

    /** @return the in-flight transfer entries (executing + target uuids), in no particular order (never null). */
    public List<NBTTagCompound> getTransfers() {
        return Collections.unmodifiableList(transfers);
    }

    @Override
    public void writeToNBT(NBTTagCompound compound) {
        super.writeToNBT(compound);
        NBTTagList list = new NBTTagList();
        for (NBTTagCompound entry : ships) {
            list.appendTag(entry);
        }
        compound.setTag(TAG_FLEET, list);
        // The system (pass 7) — same spec format the star render TE persists.
        NBTTagList planets = new NBTTagList();
        for (TileEntityEyeOfHarmony.PlanetSpec spec : systemPlanets) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("tex", spec.texture);
            tag.setFloat("distance", spec.distance);
            tag.setFloat("scale", spec.scale);
            tag.setFloat("orbitSpeed", spec.orbitSpeed);
            tag.setFloat("rotationSpeed", spec.rotationSpeed);
            tag.setFloat("xAngle", spec.xAngle);
            tag.setFloat("zAngle", spec.zAngle);
            if (spec.color != 0) {
                tag.setInteger("color", spec.color);
            }
            if (spec.ringTexture != null && !spec.ringTexture.isEmpty()) {
                tag.setString("ring", spec.ringTexture);
            }
            planets.appendTag(tag);
        }
        compound.setTag(TAG_SYSTEM_PLANETS, planets);
        compound.setFloat(TAG_STAR_SIZE, starSize);
        // The USS virtual orbit clock (absent when not set — the legacy world-time clock).
        if (ussOrbitTime > 0) {
            compound.setLong(TAG_ORBIT_TIME, ussOrbitTime);
            compound.setLong(TAG_ORBIT_SYNC, ussSyncedWorldTime);
        }
        // The Explorer pass: the revealed ripple positions (one compound per [x, y, z]).
        if (!revealedRipples.isEmpty()) {
            NBTTagList rippleList = new NBTTagList();
            for (float[] r : revealedRipples) {
                NBTTagCompound entry = new NBTTagCompound();
                entry.setFloat("x", r[0]);
                entry.setFloat("y", r[1]);
                entry.setFloat("z", r[2]);
                rippleList.appendTag(entry);
            }
            compound.setTag(TAG_RIPPLES, rippleList);
        }
        // The infrastructure-builder pass: the ripple-scale infrastructure shells (one compound per [x, y, z,
        // count, capacity]).
        if (!rippleInfraShells.isEmpty()) {
            NBTTagList infraList = new NBTTagList();
            for (float[] s : rippleInfraShells) {
                NBTTagCompound entry = new NBTTagCompound();
                entry.setFloat("x", s[0]);
                entry.setFloat("y", s[1]);
                entry.setFloat("z", s[2]);
                entry.setLong("count", (long) s[3]);
                entry.setLong("cap", (long) s[4]);
                infraList.appendTag(entry);
            }
            compound.setTag(TAG_RIPPLE_INFRA, infraList);
        }
        // The system's gateways (one compound per [x, y, z] in fleet-anchor blocks) — always ride so the gateways
        // render even with an empty fleet.
        if (!gateways.isEmpty()) {
            NBTTagList gwList = new NBTTagList();
            for (int[] gw : gateways) {
                NBTTagCompound entry = new NBTTagCompound();
                entry.setInteger("x", gw[0]);
                entry.setInteger("y", gw[1]);
                entry.setInteger("z", gw[2]);
                gwList.appendTag(entry);
            }
            compound.setTag(TAG_GATEWAYS, gwList);
        }
        // Voidbase (Phase D): the construction sites + standing bases (absent when there are none).
        if (!baseSites.isEmpty()) {
            NBTTagList siteList = new NBTTagList();
            for (NBTTagCompound entry : baseSites) {
                siteList.appendTag(entry);
            }
            compound.setTag(TAG_SITES, siteList);
        }
        if (!bases.isEmpty()) {
            NBTTagList baseList = new NBTTagList();
            for (NBTTagCompound entry : bases) {
                baseList.appendTag(entry);
            }
            compound.setTag(TAG_BASES, baseList);
        }
        if (!transfers.isEmpty()) {
            NBTTagList transferList = new NBTTagList();
            for (NBTTagCompound entry : transfers) {
                transferList.appendTag(entry);
            }
            compound.setTag(TAG_TRANSFERS, transferList);
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound compound) {
        super.readFromNBT(compound);
        ships.clear();
        NBTTagList list = compound.getTagList(TAG_FLEET, 10);
        for (int i = 0; i < list.tagCount(); i++) {
            ships.add(list.getCompoundTagAt(i));
        }
        systemPlanets.clear();
        NBTTagList planets = compound.getTagList(TAG_SYSTEM_PLANETS, 10);
        for (int i = 0; i < planets.tagCount(); i++) {
            NBTTagCompound tag = planets.getCompoundTagAt(i);
            if (tag == null) {
                continue;
            }
            systemPlanets.add(
                new TileEntityEyeOfHarmony.PlanetSpec(
                    tag.getString("tex"),
                    tag.getFloat("distance"),
                    tag.getFloat("scale"),
                    tag.getFloat("orbitSpeed"),
                    tag.getFloat("rotationSpeed"),
                    tag.getFloat("xAngle"),
                    tag.getFloat("zAngle"),
                    tag.hasKey("color") ? tag.getInteger("color") : 0,
                    tag.hasKey("ring") ? tag.getString("ring") : ""));
        }
        starSize = compound.hasKey(TAG_STAR_SIZE) ? compound.getFloat(TAG_STAR_SIZE) : 0.4f;
        if (starSize <= 0) {
            starSize = 0.4f;
        }
        // The USS virtual orbit clock: restore it, or clear a stale one when a legacy / cold-USS NBT arrives over
        // the description-packet wire.
        if (compound.hasKey(TAG_ORBIT_TIME)) {
            setUssOrbitTime(compound.getLong(TAG_ORBIT_TIME), compound.getLong(TAG_ORBIT_SYNC));
        } else if (ussOrbitTime > 0) {
            setUssOrbitTime(0, 0);
        }
        // The Explorer pass: the revealed ripple positions.
        revealedRipples.clear();
        NBTTagList ripples = compound.getTagList(TAG_RIPPLES, 10);
        for (int i = 0; i < ripples.tagCount(); i++) {
            NBTTagCompound tag = ripples.getCompoundTagAt(i);
            if (tag == null) {
                continue;
            }
            revealedRipples.add(new float[] { tag.getFloat("x"), tag.getFloat("y"), tag.getFloat("z") });
        }
        // The infrastructure-builder pass: the ripple-scale infrastructure shells (absent tag = none, so a
        // legacy / no-shell NBT clears a stale list over the description-packet wire).
        rippleInfraShells.clear();
        NBTTagList infra = compound.getTagList(TAG_RIPPLE_INFRA, 10);
        for (int i = 0; i < infra.tagCount(); i++) {
            NBTTagCompound tag = infra.getCompoundTagAt(i);
            if (tag == null) {
                continue;
            }
            rippleInfraShells.add(
                new float[] { tag.getFloat("x"), tag.getFloat("y"), tag.getFloat("z"), (float) tag.getLong("count"),
                    (float) tag.getLong("cap") });
        }
        // The system's gateways (absent tag = none).
        gateways.clear();
        NBTTagList gwList = compound.getTagList(TAG_GATEWAYS, 10);
        for (int i = 0; i < gwList.tagCount(); i++) {
            NBTTagCompound tag = gwList.getCompoundTagAt(i);
            if (tag == null) {
                continue;
            }
            gateways.add(new int[] { tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z") });
        }
        // Voidbase (Phase D): the construction sites + standing bases (absent tags = none).
        baseSites.clear();
        NBTTagList siteList = compound.getTagList(TAG_SITES, 10);
        for (int i = 0; i < siteList.tagCount(); i++) {
            baseSites.add(siteList.getCompoundTagAt(i));
        }
        bases.clear();
        NBTTagList baseList = compound.getTagList(TAG_BASES, 10);
        for (int i = 0; i < baseList.tagCount(); i++) {
            bases.add(baseList.getCompoundTagAt(i));
        }
        transfers.clear();
        NBTTagList transferList = compound.getTagList(TAG_TRANSFERS, 10);
        for (int i = 0; i < transferList.tagCount(); i++) {
            transfers.add(transferList.getCompoundTagAt(i));
        }
    }

    @Override
    public Packet getDescriptionPacket() {
        NBTTagCompound nbt = new NBTTagCompound();
        writeToNBT(nbt);
        return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, 1, nbt);
    }

    @Override
    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
        readFromNBT(pkt.func_148857_g());
    }

    /**
     * Push the current fleet to nearby players.
     *
     * <p>
     * <strong>1.7.10 gotcha:</strong> {@link TileEntity#updateEntity()} is the per-tick hook in this version, NOT a
     * packet push (GT uses it as its machine tick — see {@code CommonBaseMetaTileEntity#updateEntity}). The real sync
     * entry point is {@link net.minecraft.world.World#markBlockForUpdate}, which sends {@link #getDescriptionPacket()}
     * to players around the position (the same recipe GT's {@code issueTileUpdate} uses). Calling
     * {@code updateEntity()} here would be a silent no-op and the client TE would only ever learn its data on chunk
     * reload.
     */
    public void syncToClient() {
        if (worldObj != null && !worldObj.isRemote) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
        }
    }
}
