package tectech.voidcraft.render;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.gtnhlib.client.renderer.vao.IVertexArrayObject;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.render.shader.MeshBuilder;
import gregtech.common.render.shader.RenderState;
import gregtech.common.render.shader.ShaderHandle;
import gregtech.common.render.shader.SharedShaders;
import tectech.rendering.EOH.EOHRenderingUtils;
import tectech.thing.block.TileEntityEyeOfHarmony;
import tectech.voidcraft.loader.VoidcraftLoader;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftEngineType;
import tectech.voidcraft.ship.VoidcraftNbt;
import tectech.voidcraft.uss.USSConstants;
import tectech.voidcraft.uss.USSFleetOrbit;
import tectech.voidcraft.uss.USSPosition;
import tectech.voidcraft.uss.USSShipState;
import tectech.voidcraft.uss.USSWorkKind;

/**
 * Renders the ship fleet hologram (Phase 4 pass 5): the USS's WHOLE in-flight fleet (dozens–hundreds of ships, see
 * {@link USSConstants#MAX_SHIPS_PER_USS}) as face-culled block VBOs (see {@link ShipModelBuilder}) animated around
 * the Unstable Solar System — one invisible fleet anchor block carries the entry list (payload + state +
 * gateway/hover rel per ship), and this renderer draws every entry.
 *
 * <p>
 * Animation is fully client-side: the server writes each entry's state on every transition, and the client
 * advances a local progress value per leg using the same {@link USSConstants#legTicks} durations the server ticks
 * with (both read the ship speed / mining power from the payload NBT), so no per-tick sync is needed.
 *
 * <ul>
 * <li>DOCKED — hovers at the anchor (near the star)</li>
 * <li>OUTBOUND — gateway → hover point (the per-mission target's live position, inside the space shell)</li>
 * <li>MINING — hovers 0.5 blocks above the target planet (Starlifters 2.5 above the star), nose forward (keeps
 * the arrival heading — it does not turn to face the body), tracking its rendered position</li>
 * <li>RETURNING — hover point → gateway</li>
 * </ul>
 *
 * <p>
 * <strong>Dynamic destinations (pass 7):</strong> each entry carries a mission TARGET (a planet index or
 * {@code -1} for the star, chosen by the USS at launch), and the fleet TE carries the system's planet specs +
 * star size. The hover point is resolved client-side every frame — the planet's LIVE rendered position (the exact
 * orbit math of {@code EOHRenderingUtils.renderUSSOrbits}, see {@link USSFleetOrbit#planetAnchorPosition}) — so
 * the ship hovers precisely above the planet it sees, and the OUTBOUND/RETURNING legs continuously re-aim at
 * where the planet IS, not where it was at launch.
 *
 * <p>
 * Swarm spread: with a large fleet every ship would otherwise stack at its target's hover point. Each ship
 * instead hovers at a stable per-ship offset around that point ({@link USSFleetOrbit#offsetFor}) — deterministic,
 * shell-bounded, computed client-side from the per-launch SEED the USS assigned (unique per flight even for
 * duplicated ship items; the item UUID is only the legacy fallback).
 *
 * <p>
 * Orientation: the nose (the ship's visual front — the blueprint's FAR end, grid +Z, the end built AWAY from the
 * assembler; pass 24 flip) follows the
 * flight: direction of travel on OUTBOUND/RETURNING, and the ship keeps that ARRIVAL heading while MINING (pass
 * 10: it does not rotate to face the body being worked), unrotated while DOCKED. Heading changes are eased per
 * ship (keyed by the per-launch seed), so the flip on the RETURNING leg reads as a deliberate turn (there is no
 * constant display spin).
 *
 * <p>
 * <strong>Effects:</strong> while a MINE or SIPHON leg is in its MINING state, a thin additive laser rod
 * runs from the ship's middle to the body's middle (fading over the leg's ends — see {@link VoidcraftShipFx});
 * while a ship moves (OUTBOUND / RETURNING), a fading tube trail runs behind it, opposite its travel direction:
 * 9 sections, alpha 0.7 at the ship fading by one step per section so the last is still visible, total length
 * scaled by the ship's speed, colored by the ship's engine type (standard yellow, ion/xenon light blue, fusion
 * white, antimatter purple).
 * A CONSTRUCT leg builds at the site (it fires no mining beam, per user spec).
 */
@SideOnly(Side.CLIENT)
public class RenderVoidcraftShip extends TileEntitySpecialRenderer {

    /**
     * Hologram scale: each ship cell renders at this fraction of a normal block — the ships are distant specks on
     * their flight path (user request: "very small" — 1/16 originally, pass 10: even smaller, 1/48 = 3× smaller).
     */
    private static final double CELL_SIZE = 1.0 / 48.0;

    /**
     * Heading ease time constant (ticks): the ship turns toward its target heading with an exponential approach
     * ({@code 1 - e^(-dt/τ)}), so a heading change (e.g. the flip between the outbound and returning legs) reads
     * as a deliberate maneuver over ~2 seconds instead of an instant snap.
     */
    private static final double HEADING_EASE_TICKS = 8.0;

    /** Scratch model matrix (the render thread is single; one instance serves every per-draw upload). */
    private static final Matrix4f MODEL_MATRIX = new Matrix4f();

    /**
     * Cached per-gateway geometry (keyed by the gateway's anchor-relative block coords): the event-plane disc +
     * the opaque tube, baked ONCE in ANCHOR-LOCAL coordinates and positioned per frame by the model matrix
     * (translation to the anchor block center) — the same cached-VAO pattern as the ship hulls.
     */
    private static final class GatewayGeometry {

        final IVertexArrayObject plane;
        final IVertexArrayObject tube;

        GatewayGeometry(IVertexArrayObject plane, IVertexArrayObject tube) {
            this.plane = plane;
            this.tube = tube;
        }
    }

    private static final Map<String, GatewayGeometry> GATEWAY_GEOS = new HashMap<String, GatewayGeometry>();

    /** Clears the cached gateway geometry (also called on resource reload, where VAOs are deleted outright). */
    public static void releaseGeometry() {
        for (GatewayGeometry geo : GATEWAY_GEOS.values()) {
            geo.plane.delete();
            geo.tube.delete();
        }
        GATEWAY_GEOS.clear();
    }

    /** Per-ship animation phase (last seen state + the tick it was first seen, plus the eased heading). */
    private static final class LegPhase {

        int lastState = -1;
        // Phase C (programming framework): the leg id last seen — legs of the SAME state (a program doing MOVE →
        // MOVE) must each animate from their own start, so the progress phase resets on the leg id, not the state.
        int lastLegId = -1;
        // Pass 29 (subtick-smooth flight): the tick the state was first seen, as a FRACTIONAL render time
        // (worldTime + partialTicks) — so leg progress advances smoothly within a tick, like the planets' orbit.
        double startTick = -1.0;
        double yaw = 0.0;
        double pitch = 0.0;
        boolean headingInit = false;
        double lastFrame = -1.0;
        // Pass 32: the ship's last rendered position (fleet-anchor coords) — the visual start point of its next
        // travel leg. A return leg departs from the LIVE hover above the body the ship just worked, not from the
        // server's static launch-time-resolved point (a planet that has orbited since launch can sit on the
        // opposite side of the system — lerping from the stale point read as a teleport). See renderShip.
        double lastX = 0.0;
        double lastY = 0.0;
        double lastZ = 0.0;
        boolean hasLastPos = false;
        // FIX (flight desync): a travel leg lerps from a FIXED start point (the ship's position at the leg's
        // FIRST frame) to its destination, with cumulative progress as t. Using the LIVE lastPos (updated every
        // frame) as the lerp start made it self-referential — pos_N = lerp(pos_{N-1}, dest, progress_N) with
        // CUMULATIVE progress — which converged the ship to the destination in a few seconds while the counter
        // ran the full leg. The start is captured once (see renderShip) and held for the whole leg.
        double legStartX = 0.0;
        double legStartY = 0.0;
        double legStartZ = 0.0;
        boolean hasLegStart = false;
    }

    /** Per-ship animation phases, keyed by the ship's UUID (stable across fleet pushes and chunk reloads). */
    private final Map<String, LegPhase> phases = new HashMap<String, LegPhase>();

    /**
     * UUIDs present in the fleet last frame — a ship that REAPPEARS (relaunched after completion) gets a fresh phase.
     */
    private final Set<String> seenUuids = new HashSet<String>();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        if (!(tileEntity instanceof TileEntityVoidcraftShip)) {
            return;
        }
        // Guard against ghost TE renders: 1.7.10 only rebuilds its client tile-entity render list when chunks
        // load/unload, NOT when a block is removed. So if the anchor block is gone (USS destroyed, fleet cleared)
        // the stale TE would otherwise keep rendering until a chunk event. Skip it.
        if (tileEntity.isInvalid() || tileEntity.getWorldObj() == null
            || tileEntity.getWorldObj()
                .getBlock(tileEntity.xCoord, tileEntity.yCoord, tileEntity.zCoord)
                != VoidcraftLoader.sBlockVoidcraftShipRender) {
            return;
        }
        TileEntityVoidcraftShip fleet = (TileEntityVoidcraftShip) tileEntity;
        List<NBTTagCompound> ships = fleet.getShips();
        // The Explorer pass: the revealed spacetime ripples render even when NO ship is in flight (the ripples are a
        // property of the revealed solar system, not of the fleet) — skip only when there is nothing at all to draw.
        List<float[]> ripples = fleet.getRevealedRipples();
        // Phase D: the Voidbase construction sites + standing bases render from this same anchor.
        List<NBTTagCompound> sites = fleet.getBaseSites();
        List<NBTTagCompound> bases = fleet.getBases();
        // The system's gateways (the MTE registers each locked-on gateway machine): a permanent part of the system
        // view — they render even when no ship is in flight.
        List<int[]> gateways = fleet.getGateways();
        if (ships.isEmpty() && ripples.isEmpty() && sites.isEmpty() && bases.isEmpty() && gateways.isEmpty()) {
            return;
        }
        // The shaders are (re)baked by the resource-reload hook before the first render pass — a missing bake
        // (GL context hiccup) simply skips this frame's draw instead of failing the render loop.
        if (!VoidcraftShaders.ready() || !SharedShaders.ready()) {
            return;
        }

        long worldTime;
        if (fleet.getUssOrbitTime() > 0L) {
            // The USS virtual orbit clock (synced by the fleet TE): advance from the last sync at the normal rate
            // so the planet phases match the star render TE (the clock only ever runs faster than the world
            // during a stellar-acceleration second, which the machine re-syncs every tick of).
            worldTime = fleet.getUssOrbitTime() + (tileEntity.getWorldObj()
                .getTotalWorldTime() - fleet.getUssSyncedWorldTime());
        } else {
            worldTime = tileEntity.getWorldObj()
                .getTotalWorldTime();
        }
        if (worldTime < 0) {
            worldTime = 0;
        }

        // 1.7.10's TileEntityRendererDispatcher passes the block's CORNER in camera-relative coordinates
        // (xCoord − staticPlayerX — NO +0.5); every special renderer centers itself (vanilla's beacon, and EoH's
        // own EOHTileEntitySR:43). Our whole anchor-relative frame (star center at (0,−2,0), the planets' live
        // orbit positions, the beam endpoints, the exhaust) is defined about the anchor block's CENTER — so
        // center it here, once. Without this the entire ship/beam system sits (−0.5,−0.5,−0.5) off the EoH's
        // star-centered geometry: the ships hover 0.87 off their bodies and the mining beam ends ~0.87 blocks
        // away from the visible planet/star center ("right length, wrong direction" — the user's playtest).
        x += 0.5;
        y += 0.5;
        z += 0.5;

        // Cheap bounded reset: discard stale phases for ships that left the fleet (completed, destroyed, or a
        // chunk unload dropped them out of seenUuids) — both collections stay bounded by the live fleet size.
        if (phases.size() > ships.size() * 2 + 64) {
            phases.clear();
            seenUuids.clear();
        }

        // The Explorer pass: the revealed ripples — a pulsating dark-blue transparent TRIANGLE at each point. They
        // live in the same anchor frame as the star/ships (the centered (x, y, z) above), so they render in the same
        // pass. Drawn first (behind the ships) so the fleet stays legible.
        renderRipples(ripples, x, y, z, worldTime);

        // The infrastructure-builder pass: the ripple-scale infrastructure shells — a small gray triangle shell
        // with dark purple cores at each revealed ripple carrying a built Continuum Stabilizer (the same anchor
        // frame as the revealed ripples; drawn behind the ships like the ripples).
        renderRippleInfraShells(fleet.getRippleInfraShells(), x, y, z, worldTime);

        // Pass 7: the system the fleet works (specs + star size ride with the fleet TE — no world lookups) —
        // fetched before the Voidbase renders: a PLANET-anchored site/base tracks the planet's live orbit.
        List<TileEntityEyeOfHarmony.PlanetSpec> planets = fleet.getSystemPlanets();
        float starSize = fleet.getStarSize();

        // The rendered (camera-relative) position of every fleet member this frame, keyed by uuid — filled by the
        // bases and the ships as they place, read by the cargo-transfer beams once both endpoints are known.
        Map<String, double[]> transferPositions = new HashMap<String, double[]>();

        // Phase D: the Voidbase construction sites (gray wireframe + progressive fill) and the standing bases
        // (static models) — drawn before the ships so the fleet renders on top of them.
        renderSites(sites, planets, starSize, x, y, z, worldTime, partialTicks);
        renderBases(bases, planets, starSize, x, y, z, worldTime, partialTicks, transferPositions);

        // Gateway render pass: a star-facing opaque TUBE (0.1 blocks deep) with a flat 25% cyan event plane in its
        // bore,
        // at the DOME EDGE in each system gateway's direction, embedded 0.25 blocks into the shell — the visual
        // spawn/arrival point for the ship animations (the actual gateway machine sits outside the dome). The
        // gateways are a permanent part of the system (one per locked-on gateway machine, independent of the fleet
        // ship list), so they draw here — before the ships-empty early return — so the dome shows its gates even
        // with no ship in flight. Drawn before the ships so the fleet renders on top.
        for (int[] gw : gateways) {
            renderGateway(new double[] { gw[0], gw[1], gw[2] }, x, y, z);
        }

        if (ships.isEmpty()) {
            return; // only ripples, Voidbase renders and gateways were present — done
        }

        // Cargo transfer (SEND / TAKE) beams: the gray rods are drawn once every fleet member is placed (a transfer's
        // endpoints are ships and/or bases, so neither is known while a single member is being drawn).
        for (int i = 0; i < ships.size(); i++) {
            renderShip(
                ships.get(i),
                i,
                x,
                y,
                z,
                worldTime,
                partialTicks,
                planets,
                starSize,
                tileEntity.getWorldObj(),
                sites,
                transferPositions);
        }
        renderTransferBeams(fleet.getTransfers(), transferPositions, x, y, z, worldTime);
    }

    /**
     * The cargo transfer beams (SEND / TAKE): a gray rod from the executing member's rendered position to the
     * target's rendered position, for each in-flight transfer — queued into the same world-last beam pass as the
     * mining (cyan) and construction (orange) lasers. A transfer whose source did not render this frame (not in
     * the fleet, or off-screen) is skipped; a STAR-target transfer (the Stellar Injector's buffer) ends at the
     * star center instead of a rendered ship.
     *
     * @param transfers         the in-flight transfer entries (executing + target uuids, or the star flag) from
     *                          the fleet TE
     * @param transferPositions the rendered (camera-relative) position of every ship this frame, keyed by uuid
     * @param x,                y, z the fleet anchor block center in camera-relative coordinates (the star endpoint)
     * @param worldTime         the world's total tick count (drives the beam's pulse)
     */
    private static void renderTransferBeams(List<NBTTagCompound> transfers, Map<String, double[]> transferPositions,
        double x, double y, double z, long worldTime) {
        if (transfers == null || transfers.isEmpty() || transferPositions.isEmpty()) {
            return;
        }
        for (NBTTagCompound transfer : transfers) {
            double[] source = transferPositions.get(transfer.getString(TileEntityVoidcraftShip.TAG_TRANSFER_SOURCE));
            if (source == null) {
                continue;
            }
            double[] target;
            if (transfer.getBoolean(TileEntityVoidcraftShip.TAG_TRANSFER_STAR)) {
                target = new double[] { x, y + USSFleetOrbit.STAR_CENTER_Y, z };
            } else {
                target = transferPositions.get(transfer.getString(TileEntityVoidcraftShip.TAG_TRANSFER_TARGET));
            }
            if (target == null) {
                continue; // the ship did not render this frame
            }
            queueBeam(
                source,
                target,
                TRANSFER_BEAM_FADE,
                worldTime,
                TRANSFER_BEAM_GRAY,
                TRANSFER_BEAM_GRAY,
                TRANSFER_BEAM_GRAY);
        }
    }

    /**
     * The revealed spacetime ripples (the Explorer pass): each is a camera-facing (billboard) equilateral TRIANGLE in
     * a pulsating dark blue, semi-transparent — the "spacetime ripple" reading. The pulse (size + alpha) is a function
     * of world time, so every client animates it identically without any per-tick sync.
     *
     * <p>
     * Drawn through the ripple shader ({@link VoidcraftShaders#ripple()}): the triangle is billboarded in the
     * vertex stage with the camera's right/up axes read from the model-view matrix (so it always faces the
     * player, regardless of where it sits on a shell). Culling is off (billboard winding), blend is standard
     * alpha (transparent, NOT additive glow), and depth writes are off (a pure overlay, still depth-TESTED so
     * opaque geometry correctly occludes it).
     *
     * @param ripples   the revealed ripple positions — each {@code [x, y, z]} in fleet-anchor blocks (never null)
     * @param x,y,z     the anchor block CENTER in camera-relative coordinates (the ripple positions are added to it)
     * @param worldTime the world's total tick count (drives the pulse)
     */
    private static void renderRipples(List<float[]> ripples, double x, double y, double z, long worldTime) {
        if (ripples == null || ripples.isEmpty()) {
            return;
        }
        final boolean cullOn = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        final long blend = RenderState.savedBlendFunc();
        final boolean depthMaskOn = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // standard alpha (transparent)
        GL11.glDepthMask(false);
        try {
            // Camera-facing basis (billboard): the camera's RIGHT and UP axes in world space are the first two
            // ROWS of the model-view matrix's rotation part — (m00,m01,m02) = (buf[0],buf[4],buf[8]) and
            // (m10,m11,m12) = (buf[1],buf[5],buf[9]). (The first two COLUMNS are the images of the world X/Y
            // axes, correct only for an unrotated camera — that made the triangles tilt with the world instead
            // of tracking the camera.)
            //
            // The FLOAT matrix overload + org.lwjgl.BufferUtils (the FrameMatrices pattern) — the double variant
            // (glGetDouble into a java.nio.DoubleBuffer) is rejected by this LWJGL build ("DoubleBuffer is not
            // direct"). The read is additionally guarded: the effect degrades to a fixed world-up orientation
            // rather than crashing the game over a GL/environment quirk.
            double rx = 1.0, ry = 0.0, rz = 0.0, ux = 0.0, uy = 1.0, uz = 0.0;
            try {
                FloatBuffer mv = BufferUtils.createFloatBuffer(16);
                GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, mv);
                mv.rewind();
                double[] m = new double[6];
                m[0] = mv.get(0); // m00 → right.x
                m[1] = mv.get(4); // m01 → right.y
                m[2] = mv.get(8); // m02 → right.z
                m[3] = mv.get(1); // m10 → up.x
                m[4] = mv.get(5); // m11 → up.y
                m[5] = mv.get(9); // m12 → up.z
                rx = m[0];
                ry = m[1];
                rz = m[2];
                ux = m[3];
                uy = m[4];
                uz = m[5];
            } catch (Throwable t) {
                // any buffer/GL quirk — keep the fixed frame; the triangle still renders (just world-oriented)
            }

            // The pulse: a gentle sinusoid over world time (size + alpha breathe together).
            double pulse = 0.5 + 0.5 * Math.sin(worldTime / 20.0);
            double s = 0.10 + 0.03 * pulse; // triangle circumradius in blocks (0.10 … 0.13)
            float alpha = (float) (0.40 + 0.10 * pulse); // subtle pulsating transparency (0.40 … 0.50)

            // One draw per ripple: the shared unit triangle (VoidcraftGeometry.rippleTriangle), positioned by the
            // ripple shader (center + camera axes + scale) in the camera-relative frame of this pass.
            final ShaderHandle shader = VoidcraftShaders.ripple();
            shader.use();
            GL20.glUniform3f(shader.loc(VoidcraftShaders.RIPPLE_RIGHT), (float) rx, (float) ry, (float) rz);
            GL20.glUniform3f(shader.loc(VoidcraftShaders.RIPPLE_UP), (float) ux, (float) uy, (float) uz);
            shader.uploadModel(MODEL_MATRIX.identity());
            final IVertexArrayObject triangle = VoidcraftGeometry.rippleTriangle();
            for (float[] r : ripples) {
                GL20.glUniform3f(
                    shader.loc(VoidcraftShaders.RIPPLE_CENTER),
                    (float) (x + r[0]),
                    (float) (y + r[1]),
                    (float) (z + r[2]));
                GL20.glUniform1f(shader.loc(VoidcraftShaders.RIPPLE_SCALE), (float) s);
                GL20.glUniform4f(shader.loc(VoidcraftShaders.RIPPLE_COLOR), 0.08F, 0.22F, 0.95F, alpha); // dark blue
                triangle.render();
            }
            ShaderProgram.clear();
        } finally {
            GL11.glDepthMask(depthMaskOn);
            RenderState.restoreBlendFunc(blend);
            RenderState.restore(GL11.GL_CULL_FACE, cullOn);
        }
    }

    /**
     * The ripple-scale infrastructure shells (the infrastructure-builder pass): the Continuum Stabilizer built
     * around a revealed spacetime ripple point — a small triangle shell (gray panels, dark purple cores,
     * {@link USSConstants#STABILIZER_SHELL_RADIUS} around the ripple) filling with its count/capacity.
     *
     * <p>
     * Same frame as {@link #renderRipples}: each shell is centered on its ripple point (the fleet-anchor block
     * center + the entry's offset) in camera-relative coordinates.
     *
     * @param shells    each {@code [x, y, z, count, capacity]} in fleet-anchor blocks (never null)
     * @param x,y,z     the anchor block CENTER in camera-relative coordinates
     * @param worldTime the world's total tick count (drives the shells' spin)
     */
    private static void renderRippleInfraShells(List<float[]> shells, double x, double y, double z, long worldTime) {
        if (shells == null || shells.isEmpty()) {
            return;
        }
        final Matrix4f model = new Matrix4f();
        for (float[] s : shells) {
            model.identity()
                .translation((float) (x + s[0]), (float) (y + s[1]), (float) (z + s[2]));
            EOHRenderingUtils.renderUSSInfraShell(
                model,
                (float) worldTime,
                USSConstants.STABILIZER_SHELL_RADIUS,
                USSConstants.STABILIZER_TRIANGLE_EDGE,
                (long) s[3],
                (long) s[4],
                EOHRenderingUtils.STABILIZER_SHELL_TINT,
                EOHRenderingUtils.STABILIZER_ACCENT_TINT);
        }
    }

    /**
     * Offset of the visual gate center along the dome normal, in blocks. NEGATIVE = toward the star center, so
     * the gate is EMBEDDED in the shell (0.25 blocks inside the surface).
     */
    private static final double GATEWAY_INWARD_OFFSET = -0.25;

    /** Gateway tube outer radius — ~0.25 blocks outer diameter. */
    public static final double GATEWAY_OUTER_RADIUS = 0.125;
    /** Gateway tube bore radius (the flat event plane fills it); wall thickness = outer − bore. */
    public static final double GATEWAY_BORE_RADIUS = 0.105;
    /** Half the tube depth along the star-facing axis — 0.1 blocks long. */
    public static final double GATEWAY_TUBE_HALF_DEPTH = 0.05;
    /** Radial segments of the tube / event-plane circle. */
    public static final int GATEWAY_SEGMENTS = 32;
    /** The tube's solid color (0x060606). */
    public static final float GATEWAY_TUBE_GRAY = 6.0F / 255.0F;
    /** The event plane's opacity. */
    public static final float GATEWAY_PLANE_ALPHA = 0.25F;

    /**
     * The visual gateway center — the dome-surface point in the gateway's direction
     * ({@link USSFleetOrbit#gatewayEdgePoint}) pushed {@link #GATEWAY_INWARD_OFFSET} along the dome normal
     * (toward the star). Shared by the gateway render below and the ship-animation anchor in
     * {@link #renderShip}, so the ships spawn/return exactly at the gate.
     *
     * @param gw the ACTUAL gateway position (fleet-anchor blocks)
     * @return the gate center (a FRESH array; the gateway itself in the degenerate star-center case)
     */
    private static double[] gatewayAnchor(double[] gw) {
        double[] surface = USSFleetOrbit.gatewayEdgePoint(gw);
        double nx = surface[0], ny = surface[1] - USSFleetOrbit.STAR_CENTER_Y, nz = surface[2];
        double nlen = Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (nlen < 1e-9) {
            // Degenerate: the gateway sits AT the star center — no direction, keep the point as-is.
            return new double[] { surface[0], surface[1], surface[2] };
        }
        return new double[] { surface[0] + (nx / nlen) * GATEWAY_INWARD_OFFSET,
            surface[1] + (ny / nlen) * GATEWAY_INWARD_OFFSET, surface[2] + (nz / nlen) * GATEWAY_INWARD_OFFSET };
    }

    /**
     * A small STARGATE-style gateway at the space-dome edge in the gateway's direction:
     * <ul>
     * <li>an OPAQUE TUBE (a short cylinder: outer radius 0.125, bore 0.105 — 0.1 blocks long along the gate
     * axis, very dark gray 0x060606) — outer wall + bore wall + both end caps — whose axis is the dome
     * normal, so the gate face POINTS AT THE STAR (a FIXED orientation, NOT a camera billboard);</li>
     * <li>a FLAT CYAN EVENT PLANE at the tube center, 25% opacity — the "event horizon" the ships pass through.</li>
     * </ul>
     * The gate sits {@link #GATEWAY_INWARD_OFFSET} (−0.25) along the dome normal — embedded in the shell, toward
     * the star.
     *
     * <p>
     * The geometry (event plane + tube) is baked once per gateway position in ANCHOR-LOCAL coordinates into VAOs
     * (see {@link #GATEWAY_GEOS}) and positioned per frame by the model matrix (translation to the anchor block
     * center in camera-relative coordinates — the 1.7.10 TE render pass passes a camera-rotation-only modelview,
     * so the camera-relative translation is the position). Drawn through the flat-color shader: the cyan plane
     * first (standard alpha, 25%, depth writes off — a pure overlay, still depth-TESTED so real geometry
     * occludes it), the opaque tube second (solid draw, depth writes on). Culling is off (the tube is
     * double-sided).
     *
     * @param gw    the ACTUAL gateway position (fleet-anchor blocks) — projected onto the dome here
     * @param x,y,z the anchor block CENTER in camera-relative coordinates
     */
    private static void renderGateway(double[] gw, double x, double y, double z) {
        final String key = gw[0] + "," + gw[1] + "," + gw[2];
        GatewayGeometry geo = GATEWAY_GEOS.get(key);
        if (geo == null) {
            // Bound the cache: each anchor carries few gateways (one per system gateway direction) — past the cap
            // the entries are stale (a rebuilt or destroyed system) and are discarded with the rest.
            if (GATEWAY_GEOS.size() > 64) {
                releaseGeometry();
            }
            geo = buildGatewayGeometry(gw);
            GATEWAY_GEOS.put(key, geo);
        }

        final boolean cullOn = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        final long blend = RenderState.savedBlendFunc();
        final boolean depthMaskOn = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        final boolean alphaTestOn = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_CULL_FACE);
        // The world pass leaves GL_ALPHA_TEST on at a 0.5 GL_GREATER reference (the vanilla block-cutout
        // state); the 0.25-alpha event plane would be discarded by it, leaving the bore an open hole. The
        // plane and the flat-color tube are custom blended overlays, not cutout textures: draw them with the
        // test disabled.
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        try {
            final ShaderHandle shader = VoidcraftShaders.color();
            shader.use();
            shader.uploadModel(
                MODEL_MATRIX.identity()
                    .translate((float) x, (float) y, (float) z));
            // 1) The CYAN EVENT PLANE.
            GL20.glUniform4f(shader.loc(VoidcraftShaders.COLOR_COLOR), 0.0F, 1.0F, 1.0F, GATEWAY_PLANE_ALPHA);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthMask(false);
            geo.plane.render();
            // 2) The OPAQUE TUBE on top of the plane (the world blend state, depth writes on).
            GL20.glUniform4f(
                shader.loc(VoidcraftShaders.COLOR_COLOR),
                GATEWAY_TUBE_GRAY,
                GATEWAY_TUBE_GRAY,
                GATEWAY_TUBE_GRAY,
                1.0F);
            RenderState.restoreBlendFunc(blend);
            GL11.glDepthMask(true);
            geo.tube.render();
            ShaderProgram.clear();
        } finally {
            GL11.glDepthMask(depthMaskOn);
            RenderState.restoreBlendFunc(blend);
            RenderState.restore(GL11.GL_ALPHA_TEST, alphaTestOn);
            RenderState.restore(GL11.GL_CULL_FACE, cullOn);
        }
    }

    /**
     * Bakes the gateway's two meshes (event-plane disc + the opaque tube: outer wall, bore wall, both end caps)
     * in ANCHOR-LOCAL coordinates (the gate point, no anchor-block translation) into VAOs.
     */
    private static GatewayGeometry buildGatewayGeometry(double[] gw) {
        double[] center = gatewayAnchor(gw);
        // The gate axis = the dome normal at the gateway point (outward from the star center) — the gate face
        // points at the star. Degenerate (gateway at the star center): any axis is equally valid.
        double[] surface = USSFleetOrbit.gatewayEdgePoint(gw);
        double nx = surface[0], ny = surface[1] - USSFleetOrbit.STAR_CENTER_Y, nz = surface[2];
        double nlen = Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (nlen < 1e-9) {
            nx = 0.0;
            ny = 1.0;
            nz = 0.0;
        } else {
            nx /= nlen;
            ny /= nlen;
            nz /= nlen;
        }
        // Anchor-LOCAL gate center (the anchor-block translation is applied per frame by the model matrix).
        double cx = center[0], cy = center[1], cz = center[2];

        final double RO = GATEWAY_OUTER_RADIUS;
        final double RI = GATEWAY_BORE_RADIUS;
        final double H = GATEWAY_TUBE_HALF_DEPTH;
        final int SEGS = GATEWAY_SEGMENTS;
        final double TAU = 2.0 * Math.PI;

        // An orthonormal basis (u, v) in the gate plane (perpendicular to the axis n), built with two cross
        // products from a helper axis that is not parallel to n (world up, or world X when n ≈ ±Y).
        double ax = 0.0, ay = 1.0, az = 0.0;
        if (Math.abs(ny) > 0.99) {
            ax = 1.0;
            ay = 0.0;
        }
        double ux = ay * nz - az * ny, uy = az * nx - ax * nz, uz = ax * ny - ay * nx; // u = a × n
        double ulen = Math.sqrt(ux * ux + uy * uy + uz * uz);
        ux /= ulen;
        uy /= ulen;
        uz /= ulen;
        double vx = ny * uz - nz * uy, vy = nz * ux - nx * uz, vz = nx * uy - ny * ux; // v = n × u

        // 1) The event plane: a flat disc at the tube center — a triangle fan around the center.
        final MeshBuilder planeMesh = MeshBuilder.of(VoidcraftShaders.color(), SEGS * 3);
        for (int i = 0; i < SEGS; i++) {
            planeMesh.triangleVertex(cx, cy, cz, 0, 0);
            ringPoint(cx, cy, cz, ux, uy, uz, vx, vy, vz, nx, ny, nz, RI, 0.0, TAU * i / SEGS, RING);
            planeMesh.triangleVertex(RING[0], RING[1], RING[2], 0, 0);
            ringPoint(cx, cy, cz, ux, uy, uz, vx, vy, vz, nx, ny, nz, RI, 0.0, TAU * (i + 1) / SEGS, RING);
            planeMesh.triangleVertex(RING[0], RING[1], RING[2], 0, 0);
        }
        final IVertexArrayObject plane = planeMesh.build();

        // 2) The tube: SEGS quads per ring (outer wall, bore wall) + SEGS quads per end cap annulus.
        final MeshBuilder tubeMesh = MeshBuilder.of(VoidcraftShaders.color(), (2 * SEGS + 2 * SEGS) * 6);
        final double[] a = new double[3], b = new double[3], c = new double[3], d = new double[3];
        // a) the OUTER WALL (radius RO): a ring between the two end faces.
        for (int i = 0; i < SEGS; i++) {
            ringPoint(cx, cy, cz, ux, uy, uz, vx, vy, vz, nx, ny, nz, RO, -H, TAU * i / SEGS, a);
            ringPoint(cx, cy, cz, ux, uy, uz, vx, vy, vz, nx, ny, nz, RO, +H, TAU * i / SEGS, b);
            ringPoint(cx, cy, cz, ux, uy, uz, vx, vy, vz, nx, ny, nz, RO, +H, TAU * (i + 1) / SEGS, c);
            ringPoint(cx, cy, cz, ux, uy, uz, vx, vy, vz, nx, ny, nz, RO, -H, TAU * (i + 1) / SEGS, d);
            tubeMesh.vertex(a[0], a[1], a[2], 0, 0);
            tubeMesh.vertex(b[0], b[1], b[2], 0, 0);
            tubeMesh.vertex(c[0], c[1], c[2], 0, 0);
            tubeMesh.vertex(d[0], d[1], d[2], 0, 0);
        }
        // b) the BORE WALL (radius RI): the inner surface of the tube.
        for (int i = 0; i < SEGS; i++) {
            ringPoint(cx, cy, cz, ux, uy, uz, vx, vy, vz, nx, ny, nz, RI, -H, TAU * i / SEGS, a);
            ringPoint(cx, cy, cz, ux, uy, uz, vx, vy, vz, nx, ny, nz, RI, +H, TAU * i / SEGS, b);
            ringPoint(cx, cy, cz, ux, uy, uz, vx, vy, vz, nx, ny, nz, RI, +H, TAU * (i + 1) / SEGS, c);
            ringPoint(cx, cy, cz, ux, uy, uz, vx, vy, vz, nx, ny, nz, RI, -H, TAU * (i + 1) / SEGS, d);
            tubeMesh.vertex(a[0], a[1], a[2], 0, 0);
            tubeMesh.vertex(b[0], b[1], b[2], 0, 0);
            tubeMesh.vertex(c[0], c[1], c[2], 0, 0);
            tubeMesh.vertex(d[0], d[1], d[2], 0, 0);
        }
        // c) the END CAPS: the annulus at each end face (a ring between the bore and outer circles).
        for (int side = -1; side <= 1; side += 2) {
            for (int i = 0; i < SEGS; i++) {
                ringPoint(cx, cy, cz, ux, uy, uz, vx, vy, vz, nx, ny, nz, RI, side * H, TAU * i / SEGS, a);
                ringPoint(cx, cy, cz, ux, uy, uz, vx, vy, vz, nx, ny, nz, RI, side * H, TAU * (i + 1) / SEGS, b);
                ringPoint(cx, cy, cz, ux, uy, uz, vx, vy, vz, nx, ny, nz, RO, side * H, TAU * (i + 1) / SEGS, c);
                ringPoint(cx, cy, cz, ux, uy, uz, vx, vy, vz, nx, ny, nz, RO, side * H, TAU * i / SEGS, d);
                tubeMesh.vertex(a[0], a[1], a[2], 0, 0);
                tubeMesh.vertex(b[0], b[1], b[2], 0, 0);
                tubeMesh.vertex(c[0], c[1], c[2], 0, 0);
                tubeMesh.vertex(d[0], d[1], d[2], 0, 0);
            }
        }
        return new GatewayGeometry(plane, tubeMesh.build());
    }

    /** One point of the gateway ring: {@code center + (u·cos a + v·sin a)·radius + n·axisOffset}. */
    private static final double[] RING = new double[3];

    private static void ringPoint(double cx, double cy, double cz, double ux, double uy, double uz, double vx,
        double vy, double vz, double nx, double ny, double nz, double radius, double axisOffset, double angle,
        double[] out) {
        double ca = Math.cos(angle), sa = Math.sin(angle);
        out[0] = cx + (ux * ca + vx * sa) * radius + nx * axisOffset;
        out[1] = cy + (uy * ca + vy * sa) * radius + ny * axisOffset;
        out[2] = cz + (uz * ca + vz * sa) * radius + nz * axisOffset;
    }

    private void renderShip(NBTTagCompound entry, int index, double x, double y, double z, long worldTime,
        float partialTicks, List<TileEntityEyeOfHarmony.PlanetSpec> planets, float starSize, World world,
        List<NBTTagCompound> sites, Map<String, double[]> transferPositions) {
        NBTTagCompound payload = entry.getCompoundTag(TileEntityVoidcraftShip.TAG_ENTRY_PAYLOAD);
        if (payload == null) {
            return;
        }
        // Pass 26 (the travel-time rendering fix): the ACTUAL travel distance, now synced by the server in the
        // entry (TAG_ENTRY_TDIST). The client previously read "vc_tdist" off the PAYLOAD, where it was never
        // written, so distance was 0 and every travel leg animated at the minimum floor — the ship zipped across
        // the system in ~1 s while the server ticked its real (minutes-long) duration, and the working-state
        // beam/scan never had a visible window. getDouble returns 0.0 for a missing tag (legacy entry → minimum,
        // the old behaviour, kept as the safe fallback).
        double travelDistance = entry.getDouble(TileEntityVoidcraftShip.TAG_ENTRY_TDIST);
        // The command-split pass: the current leg's WORK KIND (the work command that started it) — a work leg's
        // duration depends on the KIND (mines at mining power, scans at scan power, siphons at siphon power), so
        // the client must derive the SAME duration the server ticks for a ship with several work capabilities.
        int workKind = entry.getInteger(TileEntityVoidcraftShip.TAG_ENTRY_WORK_KIND);
        VoidcraftBlueprint blueprint = VoidcraftNbt.read(payload);
        if (blueprint == null) {
            return;
        }
        ShipModel model = VoidcraftShipModelCache.get(blueprint);
        if (model == null || model.maxAxis() == 0) {
            return;
        }

        USSShipState state = USSShipState.byId(entry.getInteger(TileEntityVoidcraftShip.TAG_ENTRY_STATE));
        if (state == null) {
            state = USSShipState.DOCKED;
        }
        int[] gwArr = entry.getIntArray(TileEntityVoidcraftShip.TAG_ENTRY_GW_REL);
        if (gwArr == null || gwArr.length != 3) {
            return;
        }
        // Pass 7: the mission target — a planet index into the system (or -1 = the star; missing tag = legacy
        // entry → the star). The destination itself is derived below from the system specs + star size.
        int target = entry.hasKey(TileEntityVoidcraftShip.TAG_ENTRY_TARGET)
            ? entry.getInteger(TileEntityVoidcraftShip.TAG_ENTRY_TARGET)
            : -1;

        // Per-ship identity (pass 5.1): the USS-assigned per-launch SEED is primary — duplicated ship items share
        // the item's vc_uuid, and keying phases/spread on the UUID made all duplicates stack at one spot and
        // corrupt each other's leg progress. Seed 0 (legacy save) or a corrupt entry falls back to the UUID.
        int seed = entry.getInteger(TileEntityVoidcraftShip.TAG_ENTRY_SEED);
        String key;
        double[] spread;
        if (seed != 0) {
            key = "s" + seed;
            spread = USSFleetOrbit.offsetFor(seed);
        } else {
            String uuid = payload.getString(VoidcraftNbt.TAG_UUID);
            key = uuid.isEmpty() ? "i" + index : uuid; // corrupt-entry guard (ships are always launched with a UUID)
            spread = USSFleetOrbit.offsetFor(uuid);
        }
        // Pass 7: the dynamic destination — the body the ship works (fleet-anchor coords): its planet's LIVE
        // rendered position (the exact orbit math the star renderer draws it with), or the star center for a
        // Starlifter / legacy entry. Re-evaluated every frame, so the ship tracks the orbiting planet and the
        // OUTBOUND/RETURNING legs aim at where the planet IS right now (user: "the target is dynamic based on
        // the planets rendered location").
        float renderTime = (float) worldTime + partialTicks;
        // Pass 29 (user: "the planet rendering is applying some kind of subtick manipulation to smooth the
        // movement — can the same thing be used with the Voidcraft rendering to make them fly smoother?"): the
        // ship's leg progress now uses the SAME fractional render time (worldTime + partialTicks) instead of the
        // whole-tick worldTime, so the flight glides between per-tick positions exactly like the planets do. A
        // double (NOT float) so the sub-tick fraction survives large world times (float would drop it).
        double shipRenderTime = (double) worldTime + partialTicks;
        // Programming framework (Phase C): the server tells us whether the hover body is a FIXED point (a ripple
        // point, a ship rendezvous — TAG_ENTRY_STATIC, resolved into TAG_ENTRY_DEST) or a LIVE body (a planet that
        // keeps orbiting — the client tracks it). The target is decided by the ship's PROGRAM (MOVE target).
        boolean staticBody = entry.getBoolean(TileEntityVoidcraftShip.TAG_ENTRY_STATIC);
        double[] body;
        double hoverAbove;
        if (staticBody && entry.hasKey(TileEntityVoidcraftShip.TAG_ENTRY_DEST)) {
            NBTTagCompound dest = entry.getCompoundTag(TileEntityVoidcraftShip.TAG_ENTRY_DEST);
            // The server wrote this with USSPosition.writeToNBT (keys vc_pos_x/vc_pos_y/vc_pos_z) — read it back
            // with the SAME reader (a bare getDouble("x") silently returns 0.0 for the missing keys, which sent
            // ships hovering at (0,0,0) instead of their target).
            USSPosition destPos = USSPosition.readFromNBT(dest);
            body = new double[] { destPos.x(), destPos.y(), destPos.z() };
            hoverAbove = 0.0; // hover AT the fixed point (the scan target / rendezvous), no surface offset
        } else {
            // Pass 7: the dynamic destination — the body the ship works (fleet-anchor coords): its planet's LIVE
            // rendered position (the exact orbit math the star renderer draws it with), or the star center for a
            // star target / legacy entry. Re-evaluated every frame, so the ship tracks the orbiting planet.
            body = targetBody(target, planets, starSize, renderTime);
            boolean isPlanet = target >= 0 && planets != null && target < planets.size();
            // Pass 8: "0.5 blocks above that planet" = 0.5 above the SURFACE. Pass 9: the rendered planet is a unit
            // CUBE of size spec.scale (±0.5·scale), so hovering a flat 0.5 above the CENTER would put the ship
            // INSIDE the planets and depth-occlude the miner beam.
            hoverAbove = isPlanet ? USSConstants.HOVER_ABOVE_PLANET + 0.5 * planets.get(target).scale
                : USSConstants.HOVER_ABOVE_STAR;
        }

        // Phase C: the ship's CURRENT position (fleet-anchor coords — the server's truth): its launch origin at
        // launch, then the last leg's endpoint. On a travel leg this is the leg's START (the client lerps from
        // it — a fresh ship departs from the gateway exactly as before, a MOVE→MOVE leg departs from the previous
        // body); while HOLDING (HOVERING) the ship renders here.
        double[] entryPos = null;
        if (entry.hasKey(TileEntityVoidcraftShip.TAG_ENTRY_POS)) {
            USSPosition p = USSPosition.readFromNBT(entry.getCompoundTag(TileEntityVoidcraftShip.TAG_ENTRY_POS));
            entryPos = new double[] { p.x(), p.y(), p.z() };
        }
        // Phase C: the leg identity — the client resets its leg-progress phase when this changes (legs of the SAME
        // state must each animate from their own start).
        int legId = entry.hasKey(TileEntityVoidcraftShip.TAG_ENTRY_LEG_ID)
            ? entry.getInteger(TileEntityVoidcraftShip.TAG_ENTRY_LEG_ID)
            : -1;

        // Swarm spread: each ship hovers at its own stable spot AROUND the shared hover point, so a large fleet
        // reads as a swarm around its target instead of a stack (USSFleetOrbit keeps it inside the space shell).
        double[] gw = { gwArr[0], gwArr[1], gwArr[2] };
        // Gateway render pass (user: the gateway sits OUTSIDE the space dome and is not a good-looking animation
        // anchor): the VISUAL gateway is the gate center in the gateway's direction — where the dark gray ring
        // + cyan event plane render and where the ships spawn/return (round 3: the gate sits 0.25 blocks
        // INSIDE the shell, toward the star). gw stays the server's truth; gwRender is the gate center.
        double[] gwRender = gatewayAnchor(gw);
        double[] hover = { body[0] + spread[0], body[1] + hoverAbove + spread[1], body[2] + spread[2] };
        // Gateway render pass: a FRESH ship is still at its launch origin — the server writes the SAME point to
        // TAG_ENTRY_POS (launch origin) and TAG_ENTRY_GW_REL (gateway) — so it must spawn at, and depart from,
        // the DOME-EDGE gateway render (inside the shell), not the actual dome-external gateway block. A
        // finished ship (entryPos = its last body, elsewhere) is unaffected.
        boolean atGateway = (entryPos == null)
            || (entryPos[0] - gw[0]) * (entryPos[0] - gw[0]) + (entryPos[1] - gw[1]) * (entryPos[1] - gw[1])
                + (entryPos[2] - gw[2]) * (entryPos[2] - gw[2]) < 1e-12;
        // The leg's start point (Phase C): the ship's current position when the leg is a travel leg, else the
        // gateway (legacy entry without a position).
        double[] legFrom = (entryPos != null && !atGateway) ? entryPos : gwRender;

        LegPhase phase = phases.get(key);
        if (phase == null || !seenUuids.contains(key)) {
            // Fresh appearance (first frame, chunk reload, or a RELAUNCHED ship after its previous mission
            // completed): reset the leg progress, or a stale startTick would jump the ship.
            phase = new LegPhase();
            phases.put(key, phase);
        }

        // Pass 32 (user: "the ships teleport much closer to the gateway when doing the rotation to face the
        // return leg — the planet they were mining was at the opposite side of the system"): a travel leg departs
        // from where the ship was LAST SEEN (this key's final rendered position — for a work leg, the live hover
        // above the orbiting body), not from the server's static entryPos. entryPos is the leg's start point as
        // RESOLVED AT LAUNCH; a planet keeps orbiting, so by the time the return leg begins the resolved point can
        // sit on the opposite side of the system from where the ship visibly worked, and lerping from it read as
        // a teleport. A fresh ship (no last-seen position yet) departs from the gateway, as before.
        if (phase.hasLastPos) {
            legFrom = new double[] { phase.lastX, phase.lastY, phase.lastZ };
        }

        // FIX (flight desync): capture the travel leg's start ONCE — on the leg's first frame — and hold it for
        // the whole leg. The "new leg" condition is identical to legProgress's `fresh` (and is evaluated BEFORE
        // legProgress runs this frame, so both see the same previous-frame phase state). This breaks the
        // self-referential lerp (see the legStart field): the leg now lerps FIXED start → destination with the
        // cumulative progress as t, advancing at exactly the server's leg rate instead of converging in seconds.
        // Pass 32 is preserved: a return leg still departs from where the ship was last seen (the live hover
        // above the body it just worked), because that is exactly legFrom on the leg's first frame.
        boolean travelLeg = (state == USSShipState.OUTBOUND || state == USSShipState.RETURNING);
        if (travelLeg && (phase.lastState != state.getId() || phase.lastLegId != legId || phase.startTick < 0)) {
            phase.legStartX = legFrom[0];
            phase.legStartY = legFrom[1];
            phase.legStartZ = legFrom[2];
            phase.hasLegStart = true;
        }
        double[] travelFrom = (travelLeg && phase.hasLegStart)
            ? new double[] { phase.legStartX, phase.legStartY, phase.legStartZ }
            : legFrom;

        // Pass 11 (user: "the bobbing is a bit aggressive now — remove it completely, it doesn't make much sense
        // for the ships to go up and down"): no vertical bob — ships hold a fixed hover altitude.
        // Phase C: travel legs lerp from the LEG'S START (travelFrom — the ship's position at the leg's first
        // frame: the gateway for a fresh ship, the previous body for a MOVE→MOVE leg) to their end point (the
        // body / the gateway). legProgress advances the per-leg phase, so it is called ONCE per frame and its
        // value shared between the lerp and the trail's length ramp.
        double travelProgress = travelLeg
            ? legProgress(phase, payload, travelDistance, shipRenderTime, state, legId, workKind)
            : 0.0;
        double[] pos;
        switch (state) {
            case OUTBOUND:
                pos = lerp(travelFrom, hover, travelProgress);
                break;
            case RETURNING:
                // Gateway render pass: the return leg ends at the DOME-EDGE gateway render (the gray circle),
                // not the actual (dome-external) gateway block.
                pos = lerp(travelFrom, gwRender, travelProgress);
                break;
            case MINING:
                // Work the body: hover just above it (0.5 over the planet SURFACE / 2.5 over the star / AT the
                // fixed point), fixed altitude (pass 11: the vertical bob is gone), nose forward (pass 10: the ship
                // keeps its arrival heading — it does not turn to face the body; headingFor).
                pos = new double[] { hover[0], hover[1], hover[2] };
                break;
            case HOVERING:
                // Phase C: the program ended via STOP (or there is none) — the ship HOLDS at its current position
                // (a fresh ship: the gateway; a finished one: its last body) + its swarm spread. Gateway render
                // pass: a fresh ship holds at the DOME-EDGE gateway render (inside the shell), not the actual
                // (dome-external) gateway block.
                pos = (entryPos != null && !atGateway)
                    ? new double[] { entryPos[0] + spread[0], entryPos[1] + spread[1], entryPos[2] + spread[2] }
                    : new double[] { gwRender[0] + spread[0], gwRender[1] + spread[1], gwRender[2] + spread[2] };
                break;
            case DOCKED:
            default:
                // Hover just above the anchor (the fleet anchor carries no docked ship; kept for completeness).
                // Pass 11: the bob is gone — a fixed hover height.
                pos = new double[] { 0.0, 0.9, 0.0 };
                break;
        }

        // Pass 32: remember this frame's rendered position — the ship's next travel leg departs from here, so
        // MINING→RETURNING and HOVERING→OUTBOUND transitions are seamless (no teleport to a stale resolved
        // point). On a leg's first frame legProgress returns 0, so the ship starts EXACTLY where it was seen.
        phase.lastX = pos[0];
        phase.lastY = pos[1];
        phase.lastZ = pos[2];
        phase.hasLastPos = true;

        // Ship-to-ship transfer beams: register this ship's rendered position under its uuid (the server's
        // transfer identity) so both endpoints of a transfer resolve once the whole fleet has been placed.
        String transferUuid = payload.getString(VoidcraftNbt.TAG_UUID);
        if (!transferUuid.isEmpty()) {
            transferPositions.put(transferUuid, new double[] { x + pos[0], y + pos[1], z + pos[2] });
        }

        double yaw = 0.0;
        double pitch = 0.0;
        // Phase C: the heading is the leg's direction (legFrom → leg end) — OUTBOUND/RETURNING travel it, MINING
        // keeps the arrival heading, HOVERING holds the current attitude.
        double[] legTo = (state == USSShipState.RETURNING) ? gwRender : hover;
        double[] heading = headingFor(legFrom, legTo, state);
        if (heading != null) {
            double targetYaw = heading[0];
            double targetPitch = heading[1];
            double frame = worldTime + partialTicks;
            double dt = phase.lastFrame < 0.0 ? 0.0 : frame - phase.lastFrame;
            if (dt < 0.0) {
                dt = 0.0;
            } else if (dt > 20.0) {
                dt = 20.0; // clamp lag-spike steps (a tab-in after a pause must not whip the nose around)
            }
            if (!phase.headingInit) {
                phase.yaw = targetYaw;
                phase.pitch = targetPitch;
                phase.headingInit = true;
            } else if (dt > 0.0) {
                double k = 1.0 - Math.exp(-dt / HEADING_EASE_TICKS);
                double dyaw = targetYaw - phase.yaw;
                dyaw = (dyaw % 360.0 + 540.0) % 360.0 - 180.0; // shortest arc
                phase.yaw += dyaw * k;
                phase.pitch += (targetPitch - phase.pitch) * k;
            }
            phase.lastFrame = frame;
            yaw = phase.yaw;
            pitch = phase.pitch;
        } else if (phase.headingInit) {
            // No target heading (HOVERING/DOCKED, or degenerate geometry — the hover point did not move this
            // frame, so legFrom and legTo coincide and the direction is undefined): HOLD the current attitude.
            // (Drawing identity here instead would snap the ship to its default facing for that frame.)
            yaw = phase.yaw;
            pitch = phase.pitch;
        }

        // The model matrix: translate to the ship position, scale to hologram cell size, orient, and center. The cell
        // CUBES span 0..n on each axis (cells are indexed 0..n-1, each occupying [i, i+1]) — the VOLUME center
        // n/2, not the cell-center centroid (n−1)/2, must land on the ship's position: centering the centroid
        // leaves the hull offset half a cell along every model axis, off the line of travel for any non-diagonal
        // heading. Orientation: headingFor returns (yaw, pitch) in DEGREES; JOML's rotate()
        // takes RADIANS (the codebase-wide convention — every other JOML caller wraps its degrees in
        // Math.toRadians). The derivation applies YAW to the model first, then PITCH, i.e. the model rotation is
        // R_pitch * R_yaw (yaw on the right, applied to the vertex first). JOML post-multiplies each rotate()
        // call, so PITCH must be issued first and YAW second (yaw-then-pitch yields R_yaw * R_pitch, which points
        // the nose off-target for any diagonal direction).
        final Matrix4f matrix = MODEL_MATRIX.identity()
            .translate((float) (x + pos[0]), (float) (y + pos[1]), (float) (z + pos[2]))
            .scale((float) CELL_SIZE)
            .rotate((float) Math.toRadians(pitch), 1.0F, 0.0F, 0.0F)
            .rotate((float) Math.toRadians(yaw), 0.0F, 1.0F, 0.0F)
            .translate(-(model.width / 2.0F), -(model.height / 2.0F), -(model.depth / 2.0F));

        // Culling off for the ship: the hull is a hollow shell of blocks plus thin cover quads, and we cannot
        // assume every face's winding from the outside — the back sides are depth-occluded by the cube volume,
        // so disabling culling only guarantees the faces we want (covers included) actually draw. Blending off:
        // the hull is opaque (every block atlas pixel is fully opaque), and blending would inherit whatever
        // blend FUNC an earlier tile-entity renderer in the same pass left active (an ambient additive
        // (SRC_ALPHA, ONE) makes an alpha-1.0 fragment read as "slightly transparent": dst = src + dst, the
        // star layer behind bleeds through — visible from the camera angles that look at the ship against the
        // star). Blend OFF is immune to the function and matches the legacy unblended hull look.
        final boolean cullEnabled = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        final boolean blendEnabled = GL11.glIsEnabled(GL11.GL_BLEND);
        // Depth state asserted, not inherited (test ON, LEQUAL, writes ON): the ship's far faces must
        // occlude the near ones within this single VAO draw, and this pass runs behind every other
        // tile-entity renderer — the same ambient-state hazard the cull/blend asserts above defend
        // against (the beam pass below carries the same depth discipline for the same reason).
        final boolean depthTestEnabled = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
        final int depthFunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);
        final boolean depthMaskEnabled = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glDepthMask(true);
        try {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            Minecraft.getMinecraft()
                .getTextureManager()
                .bindTexture(TextureMap.locationBlocksTexture);
            final ShaderHandle shader = SharedShaders.textured();
            shader.use();
            GL20.glUniform4f(shader.loc(SharedShaders.U_TINT), 1.0F, 1.0F, 1.0F, 1.0F);
            shader.uploadModel(matrix);
            model.vao.render();
            ShaderProgram.clear();
        } finally {
            GL11.glDepthMask(depthMaskEnabled);
            GL11.glDepthFunc(depthFunc);
            RenderState.restore(GL11.GL_DEPTH_TEST, depthTestEnabled);
            RenderState.restore(GL11.GL_BLEND, blendEnabled);
            RenderState.restore(GL11.GL_CULL_FACE, cullEnabled);
        }

        // Pass 8: the mining laser — a thin glowing rod from the MIDDLE of the ship to the MIDDLE of the body it
        // works (user spec), on the MINE and SIPHON legs during the MINING state, fading in over the leg's start
        // and out over its end (VoidcraftShipFx.beamFade) so OUTBOUND→MINING→RETURNING reads as the beam
        // engaging and releasing. A CONSTRUCT leg builds at the site (its own orange beam); a SCAN leg scans (its
        // own cube).
        if (state == USSShipState.MINING && (workKind == USSWorkKind.MINE || workKind == USSWorkKind.SIPHON)) {
            double fade = VoidcraftShipFx.beamFade(
                legProgress(phase, payload, travelDistance, shipRenderTime, USSShipState.MINING, legId, workKind));
            if (fade > 0.0) {
                queueBeam(
                    new double[] { x + pos[0], y + pos[1], z + pos[2] },
                    new double[] { x + body[0], y + body[1], z + body[2] },
                    fade,
                    worldTime,
                    0.15,
                    0.75,
                    1.0); // the mining cyan
            }
        }

        // The CONSTRUCTION laser: while a CONSTRUCT leg runs at one of the sites and its Constructor seed is this
        // ship's per-launch seed, a thin ORANGE rod runs from the ship to the site's wireframe center (the same
        // rod as the mining beam, in the construction color), fading in over the leg's start and out over its end.
        // The server paces the leg (constructTicksPerItem * parts) and syncs its total - the client animates that
        // duration locally (the leg id re-syncs the fleet on start/end).
        if (seed != 0 && sites != null && !sites.isEmpty()) {
            // Cheap bounded reset: discard stale phases for Constructors that left the fleet.
            if (SITE_CONSTRUCT_PHASES.size() > sites.size() * 2 + 64) {
                SITE_CONSTRUCT_PHASES.clear();
            }
            for (int i = 0; i < sites.size(); i++) {
                NBTTagCompound siteEntry = sites.get(i);
                if (siteEntry.getInteger(TileEntityVoidcraftShip.TAG_SITE_CONSTRUCT_SEED) != seed) {
                    continue; // another Constructor owns this site's leg
                }
                int constructLeg = siteEntry.getInteger(TileEntityVoidcraftShip.TAG_SITE_CONSTRUCT_LEG);
                long constructTotal = siteEntry.getLong(TileEntityVoidcraftShip.TAG_SITE_CONSTRUCT_TOTAL);
                if (constructLeg <= 0 || constructTotal <= 0L) {
                    continue; // no active leg (the leg finished and the site still stands, or a base took its place)
                }
                BasePhase constructPhase = SITE_CONSTRUCT_PHASES.get(seed);
                if (constructPhase == null) {
                    constructPhase = new BasePhase();
                    SITE_CONSTRUCT_PHASES.put(seed, constructPhase);
                }
                if (constructPhase.lastLegId != constructLeg || constructPhase.startTick < 0.0) {
                    constructPhase.lastLegId = constructLeg;
                    constructPhase.startTick = shipRenderTime;
                }
                double progress = Math.min(1.0, (shipRenderTime - constructPhase.startTick) / (double) constructTotal);
                double fade = VoidcraftShipFx.beamFade(progress);
                if (fade > 0.0) {
                    double[] sitePos = anchorHoverPoint(siteEntry, planets, starSize, shipRenderTime);
                    queueBeam(
                        new double[] { x + pos[0], y + pos[1], z + pos[2] },
                        new double[] { x + sitePos[0], y + sitePos[1], z + sitePos[2] },
                        fade,
                        worldTime,
                        1.0,
                        0.6,
                        0.15); // the construction orange
                }
                break;
            }
        }

        // The Explorer pass (user spec: "a small visual effect — maybe a rotating, transparent cube around the ship
        // while scanning — would help identifying the ships"): a transparent cube surrounds the Explorer during its
        // work leg — for an Explorer the MINING state IS the scan. Pass 26 (user: "make the rendering only work
        // during the 'working' state"): the beam and the scan cube are WORKING-state effects; the reason they
        // previously "only sometimes showed" was the client animating every leg at the minimum travel time (the
        // ship zipped through states), so the working leg was reached almost instantly and the effect had no
        // visible window. Now that the client uses the ACTUAL travel time (see legProgress), the ship holds each
        // leg for its real duration and the working-state effects render for their full length. Keyed on the leg's
        // work KIND (SCAN) rather than the ship, so it also shows for the rare all-points-scanned fallback
        // (target -1). Pass 26 also
        // de-rotated it (user: "it's a 'twisted' cube, it should be a cube") — see renderScanCube.
        if (state == USSShipState.MINING && workKind == USSWorkKind.SCAN) {
            // Pass 28 (user: "the cube itself should be half the size") + pass 31 (user: "make the cube half the
            // size — it can be quite small around the ship"): 0.25× the pass-26/27 wrap radius.
            double half = (Math.max(0.75, 0.5 * model.maxAxis() * CELL_SIZE + 0.25)) * 0.25;
            // Pass 31: shipRenderTime (worldTime + partialTicks, FRACTIONAL) drives the size pulse and the spin.
            renderScanCube(new double[] { x + pos[0], y + pos[1], z + pos[2] }, shipRenderTime, half, seed);
        }

        // The thruster trail: on the legs the ship actually moves (OUTBOUND / RETURNING — not while it hovers
        // on the body), a fading tube trail runs BEHIND the ship, opposite the leg's direction of travel. Queued
        // for the world-last pass with the beams: a trail drawn in the tile-entity pass would be overpainted by
        // the space shell, which renders later in the same pass.
        if (travelLeg) {
            VoidcraftEngineType engine = VoidcraftEngineType
                .byId(VoidcraftNbt.readInt(payload, VoidcraftNbt.TAG_ENGINE));
            if (engine != VoidcraftEngineType.NONE) {
                double[] trailLegTo = (state == USSShipState.OUTBOUND) ? hover : gwRender;
                double tdx = trailLegTo[0] - travelFrom[0];
                double tdy = trailLegTo[1] - travelFrom[1];
                double tdz = trailLegTo[2] - travelFrom[2];
                double chord = Math.sqrt(tdx * tdx + tdy * tdy + tdz * tdz);
                if (chord > 1e-9) {
                    // The rendered speed in blocks/tick: the leg distance over the SAME leg duration the server
                    // ticks with (the trail's length scales with it).
                    double speed = VoidcraftNbt.readDouble(payload, VoidcraftNbt.TAG_SPEED);
                    long leg = USSConstants.legTicks(
                        state,
                        travelDistance,
                        speed,
                        workKind,
                        VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_MINING),
                        VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_SCAN),
                        VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_STARLIFTER));
                    double blocksPerTick = leg > 0 ? travelDistance / (double) leg : 0.0;
                    final double dirNX = tdx / chord;
                    final double dirNY = tdy / chord;
                    final double dirNZ = tdz / chord;
                    // The trail's head is the model's BACK-FACE CENTER: the model is volume-centered on the
                    // ship's position (the model matrix), so the hull's rear face sits depth/2 cells (at the
                    // hologram scale) behind it along the model's own nose axis — the EASED orientation, which
                    // lags the leg's chord while the ship turns, so the chord direction does not generally point
                    // at the back face.
                    double trailBack = model.depth / 2.0 * CELL_SIZE;
                    double yawRad = Math.toRadians(yaw);
                    double pitchRad = Math.toRadians(pitch);
                    double noseX = Math.sin(yawRad);
                    double noseY = -Math.sin(pitchRad) * Math.cos(yawRad);
                    double noseZ = Math.cos(pitchRad) * Math.cos(yawRad);
                    // The trail grows out over the first and shrinks in over the last of the leg (the ship
                    // "speeds up" out of the gateway and "slows down" into it) — the leg progress drives it.
                    double trailLen = VoidcraftShipFx.trailLength(blocksPerTick)
                        * VoidcraftShipFx.trailLengthScale(travelProgress);
                    if (trailLen > 0.0) {
                        queueTrail(
                            new double[] { x + pos[0] - noseX * trailBack, y + pos[1] - noseY * trailBack,
                                z + pos[2] - noseZ * trailBack },
                            dirNX,
                            dirNY,
                            dirNZ,
                            trailLen,
                            trailColor(engine));
                    }
                }
            }
        }

        seenUuids.add(key);
    }

    /**
     * The laser queue: the beam endpoints (mining + construction lasers) are captured here during the tile-entity
     * pass and drawn once per frame in the {@code RenderWorldLastEvent} pass ({@link BeamWorldLastRenderer}) — the
     * one moment when all opaque geometry (the world, the planets, the ships and bases, the EoH space shell) has
     * already rendered and written its depth. A beam drawn there composites over the starfield in any tile-entity
     * draw order (a beam drawn earlier in the frame is overpainted by a shell that renders later), and, writing no
     * depth of its own, leaves nothing behind for the shell to fail its depth test against (a depth-writing beam
     * would leave an unpainted window in the shell). Each entry: start xyz, end xyz, fade, worldTime, r, g, b.
     */
    private static final List<double[]> BEAM_QUEUE = new ArrayList<double[]>();

    /**
     * The steady glow of the ship-to-ship cargo transfer beam (SEND / TAKE): a moderate gray, calmer than the
     * mining (cyan) and construction (orange) beams.
     */
    public static final double TRANSFER_BEAM_FADE = 0.5;
    /** The transfer beam's gray (r = g = b — a neutral gray, no color cast). */
    public static final double TRANSFER_BEAM_GRAY = 0.6;

    private static void queueBeam(double[] start, double[] end, double fade, long worldTime, double red, double green,
        double blue) {
        BEAM_QUEUE.add(
            new double[] { start[0], start[1], start[2], end[0], end[1], end[2], fade, (double) worldTime, red, green,
                blue });
    }

    /**
     * The thruster-trail queue: filled by {@link #renderShip} while a ship moves (the OUTBOUND / RETURNING legs)
     * and drawn once per frame in the {@code RenderWorldLastEvent} pass ({@link BeamWorldLastRenderer}) — the
     * same pass as the beams, for the same reason (the full depth buffer is written by then, so the hull and the
     * bodies occlude the trail and nothing drawn later can overpaint it). Each entry: head xyz (the model's back-face
     * center — the model is volume-centered on the ship's position, so the rear face sits depth/2 cells behind
     * it), the unit direction of travel, the total length in blocks,
     * and the engine's trail color r/g/b.
     */
    private static final List<double[]> TRAIL_QUEUE = new ArrayList<double[]>();

    /** The trail tube's radius, in blocks. */
    public static final double TRAIL_TUBE_RADIUS = 0.003;

    /**
     * Each trail section's length × this factor — the sections overlap so the boundary ring between two
     * separately-drawn sections cannot crack under rasterization.
     */
    public static final double TRAIL_SECTION_OVERLAP = 1.01;

    private static void queueTrail(double[] head, double dirX, double dirY, double dirZ, double length, float[] color) {
        TRAIL_QUEUE
            .add(new double[] { head[0], head[1], head[2], dirX, dirY, dirZ, length, color[0], color[1], color[2] });
    }

    /**
     * The trail color per engine type (user spec): the standard nozzle is yellow, the xenon ion thruster light
     * blue, the fusion torch white, the antimatter engine purple.
     */
    private static float[] trailColor(VoidcraftEngineType engine) {
        switch (engine) {
            case ION:
                return new float[] { 0.45F, 0.75F, 1.0F };
            case FUSION:
                return new float[] { 1.0F, 1.0F, 1.0F };
            case ANTIMATTER:
                return new float[] { 0.65F, 0.30F, 0.95F };
            case STANDARD:
            default:
                return new float[] { 1.0F, 0.85F, 0.10F };
        }
    }

    /**
     * Draws the queued lasers ({@link #BEAM_QUEUE}) in the {@code RenderWorldLastEvent} pass, once per frame. The
     * event fires with the world's modelview still active and the captured endpoints are in the same
     * camera-relative frame the tile-entity pass used, so the rod shader draws them with the identity model
     * matrix.
     *
     * <p>
     * Each beam is the shared rod VAO ({@link VoidcraftGeometry#beamRod()}), positioned entirely by the beam
     * shader (endpoints + cross-section axes + half-width), in a bright cyan with additive blending and a gentle
     * pulse — the classic "laser" reading. Culling is off (the rod's winding must not fight the world's
     * GL_CULL_FACE) and depth writes are off — the beams are drawn in the world-last pass, after every opaque
     * geometry has written its depth, so the depth test alone gives the correct picture (the planet's surface
     * and the hull occlude the part of the rod behind them, the starfield shows behind it) and no later opaque
     * draw can overpaint it.
     */
    @SideOnly(Side.CLIENT)
    public static class BeamWorldLastRenderer {

        @SubscribeEvent
        public void onRenderWorldLast(RenderWorldLastEvent event) {
            if (BEAM_QUEUE.isEmpty() && TRAIL_QUEUE.isEmpty()) {
                return;
            }
            if (!VoidcraftShaders.ready()) {
                BEAM_QUEUE.clear();
                TRAIL_QUEUE.clear();
                return;
            }
            final int depthFunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);
            final boolean cullOn = GL11.glIsEnabled(GL11.GL_CULL_FACE);
            final boolean depthMaskOn = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
            final boolean alphaTestOn = GL11.glIsEnabled(GL11.GL_ALPHA_TEST);
            final long blend = RenderState.savedBlendFunc();
            try {
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
                GL11.glDisable(GL11.GL_CULL_FACE);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE); // additive — the beam glows instead of washing out
                GL11.glDepthMask(false);

                final ShaderHandle shader = VoidcraftShaders.beam();
                shader.use();
                shader.uploadModel(MODEL_MATRIX.identity());
                GL20.glUniform1f(shader.loc(VoidcraftShaders.BEAM_HALF_WIDTH), (float) VoidcraftShipFx.BEAM_HALF_WIDTH);
                final IVertexArrayObject rod = VoidcraftGeometry.beamRod();
                for (int i = 0; i < BEAM_QUEUE.size(); i++) {
                    final double[] b = BEAM_QUEUE.get(i);
                    final double[] basis = VoidcraftShipFx
                        .beamBasis(new double[] { b[0], b[1], b[2] }, new double[] { b[3], b[4], b[5] });
                    if (basis == null) {
                        continue; // endpoints coincide — degenerate geometry, nothing to draw
                    }
                    GL20.glUniform3f(shader.loc(VoidcraftShaders.BEAM_START), (float) b[0], (float) b[1], (float) b[2]);
                    GL20.glUniform3f(shader.loc(VoidcraftShaders.BEAM_END), (float) b[3], (float) b[4], (float) b[5]);
                    GL20.glUniform3f(
                        shader.loc(VoidcraftShaders.BEAM_P1),
                        (float) basis[3],
                        (float) basis[4],
                        (float) basis[5]);
                    GL20.glUniform3f(
                        shader.loc(VoidcraftShaders.BEAM_P2),
                        (float) basis[6],
                        (float) basis[7],
                        (float) basis[8]);
                    final double pulse = 0.85 + 0.15 * Math.sin(b[7] / 2.5);
                    GL20.glUniform4f(
                        shader.loc(VoidcraftShaders.BEAM_COLOR),
                        (float) b[8],
                        (float) b[9],
                        (float) b[10],
                        (float) (0.9 * b[6] * pulse));
                    rod.render();
                }

                // The thruster trails (the same pass, for the same reason as the beams): 9 tube sections per
                // trail, section i covering [i, i+1) of the length back from the ship's head. Standard alpha
                // (each section's alpha IS its fade step — not the beams' additive glow); the world pass's
                // 0.5 GL_GREATER alpha-test reference would discard the low-alpha sections, so the test is off
                // for the trail.
                if (!TRAIL_QUEUE.isEmpty()) {
                    GL11.glDisable(GL11.GL_ALPHA_TEST);
                    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                    final ShaderHandle colorShader = VoidcraftShaders.color();
                    colorShader.use();
                    final IVertexArrayObject tube = VoidcraftGeometry.unitTube();
                    for (int i = 0; i < TRAIL_QUEUE.size(); i++) {
                        final double[] tr = TRAIL_QUEUE.get(i);
                        final double dirX = tr[3];
                        final double dirY = tr[4];
                        final double dirZ = tr[5];
                        // The yaw/pitch pair maps +Z onto the travel direction (the headingFor convention):
                        // pitch about X, then yaw about Y (JOML post-multiplies each rotate).
                        final double yaw = Math.atan2(dirX, Math.sqrt(dirY * dirY + dirZ * dirZ));
                        final double pitch = -Math.atan2(dirY, dirZ);
                        final double section = tr[6] / VoidcraftShipFx.TRAIL_SECTIONS;
                        // The sections overlap by a quarter of a section length (their shared boundary ring is
                        // the edge of two separate meshes, which rasterization can crack) and are drawn
                        // tail-first, so the BRIGHTER section always covers the overlap sliver.
                        final double sectionLen = section * TRAIL_SECTION_OVERLAP;
                        for (int s = VoidcraftShipFx.TRAIL_SECTIONS - 1; s >= 0; s--) {
                            final double alpha = VoidcraftShipFx.trailSectionAlpha(s);
                            if (alpha <= 0.0) {
                                continue;
                            }
                            final double back = s * section;
                            GL20.glUniform4f(
                                colorShader.loc(VoidcraftShaders.COLOR_COLOR),
                                (float) tr[7],
                                (float) tr[8],
                                (float) tr[9],
                                (float) alpha);
                            // The scale is issued LAST (JOML applies it to the vertex first, in the tube's
                            // local axes): a scale issued before the rotations would act in world axes and
                            // squash the tube along the travel direction for any non-vertical flight.
                            colorShader.uploadModel(
                                MODEL_MATRIX.identity()
                                    .translate(
                                        (float) (tr[0] - dirX * back),
                                        (float) (tr[1] - dirY * back),
                                        (float) (tr[2] - dirZ * back))
                                    .rotate((float) pitch, 1.0F, 0.0F, 0.0F)
                                    .rotate((float) yaw, 0.0F, 1.0F, 0.0F)
                                    .scale((float) TRAIL_TUBE_RADIUS, (float) TRAIL_TUBE_RADIUS, (float) sectionLen));
                            tube.render();
                        }
                    }
                }
                ShaderProgram.clear();
            } finally {
                BEAM_QUEUE.clear();
                TRAIL_QUEUE.clear();
                GL11.glDepthFunc(depthFunc);
                GL11.glDepthMask(depthMaskOn);
                RenderState.restore(GL11.GL_ALPHA_TEST, alphaTestOn);
                RenderState.restoreBlendFunc(blend);
                RenderState.restore(GL11.GL_CULL_FACE, cullOn);
            }
        }
    }

    /** One full size-BREATH cycle, in ticks — ~3 s per pulse (a slow, gentle breathing, not a strobe). */
    private static final double SCAN_PULSE_PERIOD_TICKS = 60.0;
    /** Breathing AMPLITUDE: the half-size oscillates ±10% about the base. */
    private static final double SCAN_PULSE_AMPLITUDE = 0.80;
    /** One full SPIN about the world up (Y) axis, in ticks — ~24 s per revolution (a slow, steady turn). */
    private static final double SCAN_SPIN_PERIOD_TICKS = 240.0;
    private static final double SCAN_TWO_PI = 2.0 * Math.PI;

    /**
     * The Explorer's scanning effect: a translucent, additive-glow cube around the ship while it scans — an
     * energy halo that glows against any background (space or planet surface).
     *
     * <p>
     * The field is a LIVING halo: (a) it gently BREATHES — its size oscillates ±10% on a ~3-second sine
     * ({@link #SCAN_PULSE_PERIOD_TICKS} / {@link #SCAN_PULSE_AMPLITUDE}); (b) it SPINS slowly about the world
     * up (Y) axis (one revolution per {@link #SCAN_SPIN_PERIOD_TICKS}) in a static 45°-on-all-three-axes pose —
     * the cube's corner points up, reading as a diamond; the spin is issued first (the world-space transform)
     * so the whole diamond turns about the vertical axis. The fractional render time drives (a) and (b), so
     * they animate smoothly frame-to-frame.
     *
     * <p>
     * Drawn through the flat-color shader ({@link VoidcraftShaders#color()}) with the unit cube: cull OFF
     * (winding-independent), ADDITIVE blend (the glow pattern of the beams), depth WRITES off (a pure
     * overlay; it is still depth-TESTED, so the dome/shell occlusion stays correct).
     *
     * @param center     the cube center in the anchor frame (the ship's current position)
     * @param renderTime fractional render time in TICKS ({@code worldTime + partialTicks}) — drives the size
     *                   pulse and the spin about the up axis
     * @param half       the cube half-size in blocks
     * @param seed       the per-launch seed (unused — the field is a pure function of renderTime)
     */
    private static void renderScanCube(double[] center, double renderTime, double half, int seed) {
        final boolean cullOn = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        final long blend = RenderState.savedBlendFunc();
        final boolean depthMaskOn = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE); // additive — the scan field GLOWS, visible against any
                                                          // background
        GL11.glDepthMask(false);
        try {
            // The pose: spin about the world UP (Y) axis (issued first — the world-space transform, so the whole
            // diamond turns about the vertical axis), then the static 45°-on-all-three-axes pose (the gem shape).
            // JOML's rotate() takes RADIANS (spinDeg is degrees).
            final double spinDeg = (renderTime * (360.0 / SCAN_SPIN_PERIOD_TICKS)) % 360.0;
            // The size: a smooth ±10% breathing driven by the fractional render time (one cycle per
            // SCAN_PULSE_PERIOD_TICKS ≈ 3 s) — the field reads as alive, not a frozen box. (renderTime % period
            // keeps the sine argument small even for huge world times.)
            final double phase = (renderTime % SCAN_PULSE_PERIOD_TICKS) / SCAN_PULSE_PERIOD_TICKS;
            final double h = half * (1.0 + SCAN_PULSE_AMPLITUDE * Math.sin(SCAN_TWO_PI * phase));
            final Matrix4f matrix = MODEL_MATRIX.identity()
                .translate((float) center[0], (float) center[1], (float) center[2])
                .rotate((float) Math.toRadians(spinDeg), 0.0F, 1.0F, 0.0F)
                .rotate((float) Math.toRadians(45.0), 1.0F, 0.0F, 0.0F)
                .rotate((float) Math.toRadians(45.0), 0.0F, 1.0F, 0.0F)
                .rotate((float) Math.toRadians(45.0), 0.0F, 0.0F, 1.0F)
                .scale((float) h);

            final ShaderHandle shader = VoidcraftShaders.color();
            shader.use();
            shader.uploadModel(matrix);
            GL20.glUniform4f(shader.loc(VoidcraftShaders.COLOR_COLOR), 0.30F, 0.70F, 1.0F, 0.1125F);
            VoidcraftGeometry.unitCube()
                .render();
            ShaderProgram.clear();
        } finally {
            GL11.glDepthMask(depthMaskOn);
            RenderState.restoreBlendFunc(blend);
            RenderState.restore(GL11.GL_CULL_FACE, cullOn);
        }
    }

    /**
     * Local leg progress in [0, 1], derived from the same leg duration the server ticks with.
     *
     * @param travelDistance the ship's ACTUAL travel distance in fleet-anchor blocks (the entry's
     *                       {@code TAG_ENTRY_TDIST}, written by the server). Pass 26: this used to be read off the
     *                       PAYLOAD as
     *                       "vc_tdist", where it was never written — so distance was 0 and every travel leg animated at
     *                       the
     *                       minimum floor. Now the real distance is passed in and the client animates each leg for its
     *                       true
     *                       length (matching the server's tick duration).
     * @param renderTime     the current render time as a FRACTIONAL tick (worldTime + partialTicks, a double for
     *                       sub-tick precision). Pass 29 (user: "the planet rendering uses subtick smoothing — use the
     *                       same for
     *                       the Voidcraft so they fly smoother"): progress is now a continuous function of frame time
     *                       instead of
     *                       whole ticks, so the ship glides between the per-tick positions exactly like the planets'
     *                       orbit does.
     */
    private double legProgress(LegPhase phase, NBTTagCompound payload, double travelDistance, double renderTime,
        USSShipState state, int legId, int workKind) {
        // Phase C: a new leg id resets the progress even for the SAME state (MOVE → MOVE legs of one program).
        boolean fresh = phase.lastState != state.getId() || phase.lastLegId != legId || phase.startTick < 0;
        if (fresh) {
            phase.lastState = state.getId();
            phase.lastLegId = legId;
            phase.startTick = renderTime;
        }
        double speed = VoidcraftNbt.readDouble(payload, VoidcraftNbt.TAG_SPEED);
        long mining = VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_MINING);
        long scan = VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_SCAN);
        long siphon = VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_STARLIFTER);
        // The command-split pass: the work leg's duration is KIND-AWARE (the work command that started the leg
        // picks the table) — the client must match the server's duration or the hover/leg progress drifts.
        long leg = USSConstants.legTicks(state, travelDistance, speed, workKind, mining, scan, siphon);
        if (leg <= 0) {
            return 1.0;
        }
        return Math.min(1.0, (renderTime - phase.startTick) / (double) leg);
    }

    /**
     * The center of the body a ship works, in fleet-anchor coordinates (pass 7): the planet at {@code target}'s
     * LIVE rendered position — the exact orbit math {@code EOHRenderingUtils.renderUSSOrbits} uses (radius
     * 0.2 + distance + 0.2·starSize, angle orbitSpeed·0.1·time, tilts xAngle/zAngle — see
     * {@link USSFleetOrbit#planetAnchorPosition}) — or the star center (the anchor sits 2 above the star block,
     * so the star center is at (0, -2, 0) in anchor coordinates).
     *
     * <p>
     * Stale-target guards (defensive, no compat needed): an out-of-range index or an empty system resolves to
     * the star — the ship still works something sensible.
     */
    private static double[] targetBody(int target, List<TileEntityEyeOfHarmony.PlanetSpec> planets, float starSize,
        float time) {
        if (target >= 0 && planets != null && target < planets.size()) {
            TileEntityEyeOfHarmony.PlanetSpec spec = planets.get(target);
            return USSFleetOrbit
                .planetAnchorPosition(spec.distance, spec.orbitSpeed, spec.xAngle, spec.zAngle, starSize, time);
        }
        return new double[] { 0.0, USSFleetOrbit.STAR_CENTER_Y, 0.0 };
    }

    /**
     * Target heading {yawDeg, pitchDeg} for the given state, or null for no rotation (DOCKED / degenerate
     * geometry).
     *
     * <p>
     * The intended facing d (the direction of travel — pass 10: also kept on the MINING leg, the ship does not
     * turn to face the body) is the ship's NOSE direction: pass 24 puts the nose on the model +Z side (the
     * blueprint's FAR end, away from the assembler — a player builds the ship pointing away from the machine), so
     * the rotation maps the model +Z axis straight onto d. Applying yaw (around +Y) then pitch (around +X) maps
     * +Z onto (dx,dy,dz) exactly via: yaw = atan2(dx, sqrt(dy²+dz²)), pitch = −atan2(dy, dz) (derivation:
     * R_x(φ)·R_y(θ)·ẑ = (sinθ, −cosθ·sinφ, cosθ·cosφ); the pass-10 asin form was only exact for level or vertical
     * flight).
     */
    private double[] headingFor(double[] legFrom, double[] legTo, USSShipState state) {
        switch (state) {
            case OUTBOUND:
            case RETURNING:
            case MINING:
                // The leg's direction of travel (legFrom → legTo). MINING (the work leg) keeps the ARRIVAL
                // heading — pass 10 (user: "the ships don't need to rotate to face the object they are mining"):
                // the ship hovers nose forward while it works the body instead of pitching down at it; the
                // RETURNING leg's target heading still flips it around with the eased turn.
                break;
            default:
                return null; // HOVERING / DOCKED: hold the current attitude (the ship is not moving)
        }
        double dx = legTo[0] - legFrom[0];
        double dy = legTo[1] - legFrom[1];
        double dz = legTo[2] - legFrom[2];
        // Pass 24: the nose is on the model +Z side (the blueprint's far end, away from the assembler) — aim +Z
        // straight at the intended facing d. (The pass-10 negation mapped the nose to the assembler end, which
        // playtested as the nozzle "facing the front".)
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len < 1e-6) {
            return null; // gateway on top of the hover point (degenerate) — no heading
        }
        double yaw = Math.toDegrees(Math.atan2(dx, Math.sqrt(dy * dy + dz * dz)));
        double pitch = -Math.toDegrees(Math.atan2(dy, dz));
        return new double[] { yaw, pitch };
    }

    private static double[] lerp(double[] from, double[] to, double t) {
        return new double[] { from[0] + (to[0] - from[0]) * t, from[1] + (to[1] - from[1]) * t,
            from[2] + (to[2] - from[2]) * t };
    }

    /**
     * The anchor hover point of a Voidbase render entry (Phase D, fleet-anchor coords) — the same protocol the
     * ship entries use: STAR (target -1) and PLANET i (target i) resolve LIVE (a planet keeps orbiting, so a
     * planet-anchored site/base tracks it), RIPPLE j is a STATIC fixed point (the server resolved it; the
     * client renders exactly there, no hover offset).
     */
    private static double[] anchorHoverPoint(NBTTagCompound entry, List<TileEntityEyeOfHarmony.PlanetSpec> planets,
        float starSize, double renderTime) {
        int target = entry.hasKey(TileEntityVoidcraftShip.TAG_ENTRY_TARGET)
            ? entry.getInteger(TileEntityVoidcraftShip.TAG_ENTRY_TARGET)
            : -1;
        if (entry.getBoolean(TileEntityVoidcraftShip.TAG_ENTRY_STATIC)
            && entry.hasKey(TileEntityVoidcraftShip.TAG_ENTRY_DEST)) {
            USSPosition p = USSPosition.readFromNBT(entry.getCompoundTag(TileEntityVoidcraftShip.TAG_ENTRY_DEST));
            return new double[] { p.x(), p.y(), p.z() };
        }
        double[] body = targetBody(target, planets, starSize, (float) renderTime);
        boolean isPlanet = target >= 0 && planets != null && target < planets.size();
        if (isPlanet) {
            // The station equatorial band: the same point the server places the base/site at (within ±30° of
            // the orbital plane, seeded by the planet index — one stable point per planet).
            TileEntityEyeOfHarmony.PlanetSpec spec = planets.get(target);
            USSPosition center = USSPosition.of(body[0], body[1], body[2]);
            double hoverRadius = 0.5 + 0.375 * spec.scale; // the same hover distance the server keeps off the surface
            USSPosition band = USSFleetOrbit.orbitalBandPoint(center, hoverRadius, target, spec.xAngle, spec.zAngle);
            return new double[] { band.x(), band.y(), band.z() };
        }
        return new double[] { body[0], body[1] + USSConstants.HOVER_ABOVE_STAR, body[2] };
    }

    /**
     * The Voidbase CONSTRUCTION SITES (Phase D): each renders a gray WIREFRAME BOX at its anchor hover point
     * (sized from the site blueprint's dimensions, gently pulsating) with a semi-transparent FILL box that grows
     * with the site's progress (the parts filling in). Both are unit geometry (the shared line-box and cube
     * VAOs) scaled by the model matrix, drawn through the flat-color shader: culling OFF, standard alpha blend,
     * depth writes OFF (pure overlays, still depth-TESTED so the bodies occlude them).
     */
    private static void renderSites(List<NBTTagCompound> sites, List<TileEntityEyeOfHarmony.PlanetSpec> planets,
        float starSize, double x, double y, double z, long worldTime, float partialTicks) {
        if (sites == null || sites.isEmpty()) {
            return;
        }
        double renderTime = (double) worldTime + partialTicks;
        final boolean cullOn = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        final long blend = RenderState.savedBlendFunc();
        final boolean depthMaskOn = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);
        final ShaderHandle shader = VoidcraftShaders.color();
        shader.use();
        try {
            final Matrix4f matrix = MODEL_MATRIX;
            for (int i = 0; i < sites.size(); i++) {
                NBTTagCompound entry = sites.get(i);
                double[] pos = anchorHoverPoint(entry, planets, starSize, renderTime);
                double progress = entry.getDouble(TileEntityVoidcraftShip.TAG_SITE_PROGRESS);
                if (progress < 0.0) {
                    progress = 0.0;
                } else if (progress > 1.0) {
                    progress = 1.0;
                }
                int[] dims = entry.getIntArray(TileEntityVoidcraftShip.TAG_SITE_DIMS);
                double w = (dims != null && dims.length == 3 && dims[0] > 0) ? dims[0] : 8;
                double h = (dims != null && dims.length == 3 && dims[1] > 0) ? dims[1] : 8;
                double d = (dims != null && dims.length == 3 && dims[2] > 0) ? dims[2] : 8;
                double px = x + pos[0];
                double py = y + pos[1];
                double pz = z + pos[2];
                // Box half-extents: the blueprint's cell span at the hologram scale + a small margin, quartered
                // (0.25x).
                double sx = 0.25 * (0.5 * w * CELL_SIZE + 0.3);
                double sy = 0.25 * (0.5 * h * CELL_SIZE + 0.3);
                double sz = 0.25 * (0.5 * d * CELL_SIZE + 0.3);
                // Wireframe: a gentle time-driven alpha pulse (deterministic — every client pulses identically).
                GL20.glUniform4f(
                    shader.loc(VoidcraftShaders.COLOR_COLOR),
                    0.55F,
                    0.55F,
                    0.55F,
                    (float) (0.6 + 0.25 * Math.sin(worldTime * 0.15 + i * 1.7)));
                shader.uploadModel(
                    matrix.identity()
                        .translate((float) px, (float) py, (float) pz)
                        .scale((float) sx, (float) sy, (float) sz));
                VoidcraftGeometry.unitCubeLines()
                    .render();
                // Progressive fill: a translucent box scaled by the site's progress (the parts filling in).
                if (progress > 0.001) {
                    GL20.glUniform4f(shader.loc(VoidcraftShaders.COLOR_COLOR), 0.42F, 0.42F, 0.42F, 0.16F);
                    shader.uploadModel(
                        matrix.identity()
                            .translate((float) px, (float) py, (float) pz)
                            .scale((float) (sx * progress), (float) (sy * progress), (float) (sz * progress)));
                    VoidcraftGeometry.unitCube()
                        .render();
                }
            }
            ShaderProgram.clear();
        } finally {
            GL11.glDepthMask(depthMaskOn);
            RenderState.restoreBlendFunc(blend);
            RenderState.restore(GL11.GL_CULL_FACE, cullOn);
        }
    }

    /**
     * The client-side animation phase of one base's mining beam (keyed by the base seed): a new mining-leg id
     * resets the beam fade's progress (the same pattern as the ships' {@link LegPhase}).
     */
    private static final class BasePhase {

        int lastLegId = -1;
        double startTick = -1.0;
    }

    // Per-base mining-beam phases (client render thread only).
    private static final Map<Integer, BasePhase> BASE_PHASES = new HashMap<Integer, BasePhase>();

    /**
     * Per-Constructor CONSTRUCT-beam phases (client render thread only), keyed by the Constructor's per-launch
     * seed (the same key the site entry's CONSTRUCT seed pairs on) - a new leg id resets the beam fade's progress.
     */
    private static final Map<Integer, BasePhase> SITE_CONSTRUCT_PHASES = new HashMap<Integer, BasePhase>();

    /**
     * The standing VoidBASES (Phase D): each renders its blueprint as a STATIC hologram model at its anchor
     * hover point — the same transform pipeline as the ships (hologram scale + model centering), with no travel
     * animation (a base sits at its anchor). The model is tinted red as its integrity drops (white at full,
     * deep red near burnout) so a failing station reads from across the system. While the base's program runs
     * a WORK mining leg (the entry's mining-leg id > 0, the station has mining power), a mining laser is drawn
     * from the base to the anchor body — the same beam the ships fire, fading in/out over the leg
     * (VoidcraftShipFx.beamFade).
     */
    private static void renderBases(List<NBTTagCompound> bases, List<TileEntityEyeOfHarmony.PlanetSpec> planets,
        float starSize, double x, double y, double z, long worldTime, float partialTicks,
        Map<String, double[]> transferPositions) {
        if (bases == null || bases.isEmpty()) {
            return;
        }
        double renderTime = (double) worldTime + partialTicks;
        // Cheap bounded reset: discard stale phases for bases that left the fleet (decommissioned mid-mining).
        if (BASE_PHASES.size() > bases.size() * 2 + 64) {
            BASE_PHASES.clear();
        }
        for (NBTTagCompound entry : bases) {
            NBTTagCompound payload = entry.getCompoundTag(TileEntityVoidcraftShip.TAG_ENTRY_PAYLOAD);
            // A BASE payload (15x15x15, cover components included) — the base reader, not the ship reader.
            VoidcraftBlueprint blueprint = payload != null ? VoidcraftNbt.readBase(payload) : null;
            if (blueprint == null) {
                continue;
            }
            ShipModel model = VoidcraftShipModelCache.get(blueprint);
            if (model == null || model.maxAxis() == 0) {
                continue;
            }
            double[] pos = anchorHoverPoint(entry, planets, starSize, renderTime);
            // Transfer beams with the base at either endpoint: register its rendered position under its uuid
            // (the server's transfer identity), the same map the ships fill in renderShip.
            String transferUuid = payload != null ? payload.getString(VoidcraftNbt.TAG_UUID) : "";
            if (!transferUuid.isEmpty()) {
                transferPositions.put(transferUuid, new double[] { x + pos[0], y + pos[1], z + pos[2] });
            }
            long integrity = entry.getLong(TileEntityVoidcraftShip.TAG_BASE_INTEGRITY);
            long maxIntegrity = entry.getLong(TileEntityVoidcraftShip.TAG_BASE_INTEGRITY_MAX);
            float f = maxIntegrity > 0 ? (float) Math.max(0.0, Math.min(1.0, (double) integrity / maxIntegrity)) : 1.0F;
            float tint = 0.35F + 0.65F * f;
            // The model matrix: translate to the base position, scale to hologram cell size, center the model (cells
            // span
            // 0..n-1 on each axis) — a base sits at its anchor, no travel rotation.
            final Matrix4f matrix = MODEL_MATRIX.identity()
                .translate((float) (x + pos[0]), (float) (y + pos[1]), (float) (z + pos[2]))
                .scale((float) CELL_SIZE)
                .translate(-(model.width - 1) / 2.0F, -(model.height - 1) / 2.0F, -(model.depth - 1) / 2.0F);
            // Culling off for the base (the hull is a hollow shell of blocks plus thin cover quads — the same
            // reasoning as the ships). Blending off: the base is opaque, and blending would inherit an ambient
            // (possibly additive) blend FUNC from an earlier tile-entity renderer, making the opaque hull read
            // as semi-transparent against the star layer.
            final boolean cullEnabled = GL11.glIsEnabled(GL11.GL_CULL_FACE);
            final boolean blendEnabled = GL11.glIsEnabled(GL11.GL_BLEND);
            // Depth state asserted, not inherited (test ON, LEQUAL, writes ON) — the same ambient-state
            // hazard as the ship draw above: the base's far faces must occlude the near ones within the
            // single VAO draw.
            final boolean depthTestEnabled = GL11.glIsEnabled(GL11.GL_DEPTH_TEST);
            final int depthFunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);
            final boolean depthMaskEnabled = GL11.glGetBoolean(GL11.GL_DEPTH_WRITEMASK);
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDepthFunc(GL11.GL_LEQUAL);
            GL11.glDepthMask(true);
            try {
                GL13.glActiveTexture(GL13.GL_TEXTURE0);
                Minecraft.getMinecraft()
                    .getTextureManager()
                    .bindTexture(TextureMap.locationBlocksTexture);
                final ShaderHandle shader = SharedShaders.textured();
                shader.use();
                GL20.glUniform4f(shader.loc(SharedShaders.U_TINT), 1.0F, tint, tint, 1.0F);
                shader.uploadModel(matrix);
                model.vao.render();
                ShaderProgram.clear();
            } finally {
                GL11.glDepthMask(depthMaskEnabled);
                GL11.glDepthFunc(depthFunc);
                RenderState.restore(GL11.GL_DEPTH_TEST, depthTestEnabled);
                RenderState.restore(GL11.GL_BLEND, blendEnabled);
                RenderState.restore(GL11.GL_CULL_FACE, cullEnabled);
            }

            // The mining laser (the ship beam, same GL discipline): while the base's program runs a WORK mining
            // leg (mining-leg id > 0) and the station has mining power, a rod runs from the base to the anchor
            // body's center (a star anchor: the star center — a static/fixed anchor has no body, no beam).
            int miningLeg = entry.getInteger(TileEntityVoidcraftShip.TAG_BASE_MINING_LEG);
            if (miningLeg > 0 && !entry.getBoolean(TileEntityVoidcraftShip.TAG_ENTRY_STATIC)) {
                long mining = VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_MINING);
                if (mining > 0) {
                    int seed = entry.getInteger(TileEntityVoidcraftShip.TAG_BASE_SEED);
                    BasePhase phase = BASE_PHASES.get(seed);
                    if (phase == null) {
                        phase = new BasePhase();
                        BASE_PHASES.put(seed, phase);
                    }
                    if (phase.lastLegId != miningLeg || phase.startTick < 0.0) {
                        phase.lastLegId = miningLeg;
                        phase.startTick = renderTime;
                    }
                    // The server mines for mineTicks(mining) machine ticks (USSConstants - the shared table);
                    // the client animates that duration locally (the leg id re-syncs the fleet on start/end).
                    long leg = USSConstants.mineTicks(mining);
                    double progress = (leg > 0) ? Math.min(1.0, (renderTime - phase.startTick) / (double) leg) : 1.0;
                    double fade = VoidcraftShipFx.beamFade(progress);
                    if (fade > 0.0) {
                        int target = entry.hasKey(TileEntityVoidcraftShip.TAG_ENTRY_TARGET)
                            ? entry.getInteger(TileEntityVoidcraftShip.TAG_ENTRY_TARGET)
                            : -1;
                        double[] body = targetBody(target, planets, starSize, (float) renderTime);
                        queueBeam(
                            new double[] { x + pos[0], y + pos[1], z + pos[2] },
                            new double[] { x + body[0], y + body[1], z + body[2] },
                            fade,
                            worldTime,
                            0.15,
                            0.75,
                            1.0); // the mining cyan
                    }
                }
            }
        }
    }
}
