package tectech.voidcraft.render;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntitySmokeFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tectech.thing.block.TileEntityEyeOfHarmony;
import tectech.voidcraft.loader.VoidcraftLoader;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftNbt;
import tectech.voidcraft.ship.VoidcraftRole;
import tectech.voidcraft.uss.USSConstants;
import tectech.voidcraft.uss.USSFleetOrbit;
import tectech.voidcraft.uss.USSPosition;
import tectech.voidcraft.uss.USSShipState;

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
 * <strong>Pass 8 effects:</strong> while a MINER or STARLIFTER is in its MINING leg, a thin additive laser rod
 * runs from the ship's middle to the body's middle (fading over the leg's ends — see {@link VoidcraftShipFx});
 * while a ship moves (OUTBOUND / RETURNING), smoke exhaust is emitted behind it, the opposite of its travel
 * direction. The Constructor is the only role that does not fire the beam (it builds, per user spec).
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
        /** Tick of the last exhaust burst (render runs many frames per tick — one burst per active tick max). */
        long lastExhaustTick = -1;
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
        if (ships.isEmpty() && ripples.isEmpty() && sites.isEmpty() && bases.isEmpty()) {
            return;
        }

        long worldTime = tileEntity.getWorldObj()
            .getTotalWorldTime();
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

        // Pass 7: the system the fleet works (specs + star size ride with the fleet TE — no world lookups) —
        // fetched before the Voidbase renders: a PLANET-anchored site/base tracks the planet's live orbit.
        List<TileEntityEyeOfHarmony.PlanetSpec> planets = fleet.getSystemPlanets();
        float starSize = fleet.getStarSize();

        // Phase D: the Voidbase construction sites (gray wireframe + progressive fill) and the standing bases
        // (static models) — drawn before the ships so the fleet renders on top of them.
        renderSites(sites, planets, starSize, x, y, z, worldTime, partialTicks);
        renderBases(bases, planets, starSize, x, y, z, worldTime, partialTicks);

        if (ships.isEmpty()) {
            return; // only ripples and Voidbase renders were present — done
        }

        // Gateway render pass: a star-facing opaque TUBE (0.1 blocks deep) with a flat 25% cyan event plane in
        // its bore, at the DOME EDGE in each fleet gateway's direction, embedded 0.25 blocks into the shell — the
        // visual spawn/arrival point for the ship animations (the actual gateway block sits outside the dome).
        // One per unique gateway (a fleet may serve several); drawn before the ships so the fleet renders on top.
        Set<String> gatewayKeys = new HashSet<String>();
        for (int i = 0; i < ships.size(); i++) {
            int[] gwArr = ships.get(i)
                .getIntArray(TileEntityVoidcraftShip.TAG_ENTRY_GW_REL);
            if (gwArr == null || gwArr.length != 3) {
                continue;
            }
            String gwKey = gwArr[0] + "," + gwArr[1] + "," + gwArr[2];
            if (!gatewayKeys.add(gwKey)) {
                continue;
            }
            renderGateway(new double[] { gwArr[0], gwArr[1], gwArr[2] }, x, y, z);
        }

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
                sites);
        }
    }

    /**
     * The revealed spacetime ripples (the Explorer pass): each is a camera-facing (billboard) equilateral TRIANGLE in
     * a pulsating dark blue, semi-transparent — the "spacetime ripple" reading. The pulse (size + alpha) is a function
     * of world time, so every client animates it identically without any per-tick sync.
     *
     * <p>
     * Same GL discipline as the mining beam ({@link #renderBeam}): texture OFF (color-only quads), lighting OFF
     * (emissive), culling OFF (billboard winding), blend ON (standard alpha — transparent, NOT additive glow), depth
     * writes OFF (a pure overlay, still depth-TESTED so opaque geometry correctly occludes it). The triangle is
     * billboarded using the camera's right/up axes read from the model-view matrix (so it always faces the player,
     * regardless of where it sits on a shell).
     *
     * @param ripples   the revealed ripple positions — each {@code [x, y, z]} in fleet-anchor blocks (never null)
     * @param x,y,z     the anchor block CENTER in camera-relative coordinates (the ripple positions are added to it)
     * @param worldTime the world's total tick count (drives the pulse)
     */
    private static void renderRipples(List<float[]> ripples, double x, double y, double z, long worldTime) {
        if (ripples == null || ripples.isEmpty()) {
            return;
        }
        boolean lightingOn = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean blendOn = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean cullOn = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        boolean textureOn = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA); // standard alpha (transparent) — NOT additive
        GL11.glDepthMask(false);
        try {
            // Camera-facing basis (billboard): the model-view matrix's first two columns are the camera's right/up
            // axes in world space (translation lives in column 4, so it does not pollute them).
            //
            // The FLOAT matrix overload + org.lwjgl.BufferUtils — the exact pattern GT5U's own FrameMatrices uses
            // (glGetFloat(GL_MODELVIEW_MATRIX, scratch) with BufferUtils.createFloatBuffer(16)), which is proven
            // to work in this 1.7.10 modpack. The double variant (GL11.glGetDouble into a java.nio.DoubleBuffer)
            // threw IllegalArgumentException "DoubleBuffer is not direct" in the user's runtime (LWJGL
            // 2.9.4-nightly + the lwjgl3ify coremod) — this environment's buffer check rejects it, so avoid it
            // entirely and read the matrix as floats (plenty of precision for a billboard basis).
            //
            // The whole read is additionally guarded: this is a decorative effect — it degrades to a fixed
            // world-up orientation rather than crashing the game over a GL/environment quirk.
            // Default = the fixed world-up frame (the fallback); the matrix read overwrites it when it succeeds.
            double rx = 1.0, ry = 0.0, rz = 0.0, ux = 0.0, uy = 1.0, uz = 0.0;
            try {
                java.nio.FloatBuffer mv = BufferUtils.createFloatBuffer(16);
                GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, mv);
                mv.rewind();
                // Pass 26 (user: "the ripples are not properly following/facing the camera"): the model-view
                // matrix is column-major in memory. The camera's RIGHT and UP axes in WORLD space are the first two
                // ROWS of the rotation part — (m00,m01,m02) = (buf[0],buf[4],buf[8]) and (m10,m11,m12) =
                // (buf[1],buf[5],buf[9]) — NOT the first two columns (buf[0..2]/buf[4..6], which are the images of
                // the world X/Y axes and are only correct for an unrotated camera). Using the columns made the
                // triangles tilt with the world instead of tracking the camera. Read everything first, commit after
                // — a partial failure must not leave a mixed frame.
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

            // The pulse: a gentle sinusoid over world time (size + alpha breathe together). Pass 26 (user: "the
            // ripples should be ~20% of their current size, and the pulsating effect should be much smaller"):
            // circumradius 0.45…0.80 → 0.10…0.13 (~20%), and the breathing amplitude 0.35 → 0.03 (much subtler).
            double pulse = 0.5 + 0.5 * Math.sin(worldTime / 20.0);
            double s = 0.10 + 0.03 * pulse; // triangle circumradius in blocks (0.10 … 0.13)
            float alpha = (float) (0.40 + 0.10 * pulse); // subtle pulsating transparency (0.40 … 0.50)

            Tessellator tessellator = Tessellator.instance;
            for (float[] r : ripples) {
                double cx = x + r[0];
                double cy = y + r[1];
                double cz = z + r[2];
                // Equilateral triangle (apex up) in the camera plane: angles 90°, 210°, 330°.
                double v0x = cx + (ux) * s, v0y = cy + (uy) * s, v0z = cz + (uz) * s;
                double v1x = cx + (-0.866 * rx - 0.5 * ux) * s, v1y = cy + (-0.866 * ry - 0.5 * uy) * s,
                    v1z = cz + (-0.866 * rz - 0.5 * uz) * s;
                double v2x = cx + (0.866 * rx - 0.5 * ux) * s, v2y = cy + (0.866 * ry - 0.5 * uy) * s,
                    v2z = cz + (0.866 * rz - 0.5 * uz) * s;
                tessellator.startDrawing(GL11.GL_TRIANGLES);
                tessellator.setColorRGBA_F(0.08F, 0.22F, 0.95F, alpha); // dark blue
                tessellator.addVertex(v0x, v0y, v0z);
                tessellator.addVertex(v1x, v1y, v1z);
                tessellator.addVertex(v2x, v2y, v2z);
                tessellator.draw();
            }
        } finally {
            GL11.glDepthMask(true);
            if (!cullOn) {
                GL11.glDisable(GL11.GL_CULL_FACE);
            } else {
                GL11.glEnable(GL11.GL_CULL_FACE);
            }
            if (blendOn) {
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            } else {
                GL11.glDisable(GL11.GL_BLEND);
            }
            if (lightingOn) {
                GL11.glEnable(GL11.GL_LIGHTING);
            } else {
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if (textureOn) {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            } else {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
        }
    }

    /**
     * Offset of the visual gate center along the dome normal, in blocks. NEGATIVE = toward the star center, so
     * the gate is EMBEDDED in the shell (0.25 blocks inside the surface).
     */
    private static final double GATEWAY_INWARD_OFFSET = -0.25;

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
     * GL discipline: color-only (texture off), unlit (lighting off), culling off (the tube is double-sided).
     * The cyan plane is a standard-alpha blend (25%), depth-TESTED (real geometry occludes it) with depth
     * WRITES off (a pure overlay); the tube is a solid draw (blend off, depth writes on). All state is restored
     * in the finally block.
     *
     * @param gw    the ACTUAL gateway position (fleet-anchor blocks) — projected onto the dome here
     * @param x,y,z the anchor block CENTER in camera-relative coordinates (the gate point is added to it)
     */
    private static void renderGateway(double[] gw, double x, double y, double z) {
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
        double cx = x + center[0], cy = y + center[1], cz = z + center[2];

        final double RO = 0.125; // outer radius — ~0.25 blocks outer diameter
        final double RI = 0.105; // bore radius (the flat event plane fills it); wall thickness RO − RI = 0.02
        final double H = 0.05; // half the tube depth — 0.1 blocks long along the star-facing axis
        final int SEGS = 32;

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

        boolean lightingOn = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean blendOn = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean cullOn = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        boolean textureOn = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        try {
            Tessellator tessellator = Tessellator.instance;

            // 1) The CYAN EVENT PLANE: a flat disc at the tube center, standard alpha at 25% opacity, depth writes
            // off (a pure overlay, still depth-tested).
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glDepthMask(false);
            tessellator.startDrawing(GL11.GL_TRIANGLE_FAN);
            tessellator.setColorRGBA_F(0.0F, 1.0F, 1.0F, 0.25F);
            tessellator.addVertex(cx, cy, cz);
            for (int i = 0; i <= SEGS; i++) {
                double a = (2.0 * Math.PI * i) / SEGS;
                double ca = Math.cos(a), sa = Math.sin(a);
                tessellator.addVertex(
                    cx + (ux * ca + vx * sa) * RI,
                    cy + (uy * ca + vy * sa) * RI,
                    cz + (uz * ca + vz * sa) * RI);
            }
            tessellator.draw();

            // 2) The OPAQUE TUBE: a short cylinder from −H to +H along the star-facing axis — outer wall + bore wall
            // + both end caps, one color, a solid draw (blend off, depth writes on) on top of the plane.
            // Very dark gray (0x060606). The color is set after each startDrawing below: startDrawing resets
            // the Tessellator's color state, so a color set before it is dropped and the draw renders white.
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDepthMask(true);
            final float tube = 6.0F / 255.0F;

            // a) the OUTER WALL (radius RO): a strip around the circle between the two end faces.
            tessellator.startDrawing(GL11.GL_TRIANGLE_STRIP);
            tessellator.setColorRGBA_F(tube, tube, tube, 1.0F);
            for (int i = 0; i <= SEGS; i++) {
                double a = (2.0 * Math.PI * i) / SEGS;
                double ca = Math.cos(a), sa = Math.sin(a);
                tessellator.addVertex(
                    cx + (ux * ca + vx * sa) * RO - nx * H,
                    cy + (uy * ca + vy * sa) * RO - ny * H,
                    cz + (uz * ca + vz * sa) * RO - nz * H);
                tessellator.addVertex(
                    cx + (ux * ca + vx * sa) * RO + nx * H,
                    cy + (uy * ca + vy * sa) * RO + ny * H,
                    cz + (uz * ca + vz * sa) * RO + nz * H);
            }
            tessellator.draw();
            // b) the BORE WALL (radius RI): the inner surface of the tube.
            tessellator.startDrawing(GL11.GL_TRIANGLE_STRIP);
            tessellator.setColorRGBA_F(tube, tube, tube, 1.0F);
            for (int i = 0; i <= SEGS; i++) {
                double a = (2.0 * Math.PI * i) / SEGS;
                double ca = Math.cos(a), sa = Math.sin(a);
                tessellator.addVertex(
                    cx + (ux * ca + vx * sa) * RI - nx * H,
                    cy + (uy * ca + vy * sa) * RI - ny * H,
                    cz + (uz * ca + vz * sa) * RI - nz * H);
                tessellator.addVertex(
                    cx + (ux * ca + vx * sa) * RI + nx * H,
                    cy + (uy * ca + vy * sa) * RI + ny * H,
                    cz + (uz * ca + vz * sa) * RI + nz * H);
            }
            tessellator.draw();
            // c) the END CAPS: the annulus at each end face (a strip between the bore and outer circles).
            for (int side = -1; side <= 1; side += 2) {
                tessellator.startDrawing(GL11.GL_TRIANGLE_STRIP);
                tessellator.setColorRGBA_F(tube, tube, tube, 1.0F);
                for (int i = 0; i <= SEGS; i++) {
                    double a = (2.0 * Math.PI * i) / SEGS;
                    double ca = Math.cos(a), sa = Math.sin(a);
                    tessellator.addVertex(
                        cx + (ux * ca + vx * sa) * RI + nx * side * H,
                        cy + (uy * ca + vy * sa) * RI + ny * side * H,
                        cz + (uz * ca + vz * sa) * RI + nz * side * H);
                    tessellator.addVertex(
                        cx + (ux * ca + vx * sa) * RO + nx * side * H,
                        cy + (uy * ca + vy * sa) * RO + ny * side * H,
                        cz + (uz * ca + vz * sa) * RO + nz * side * H);
                }
                tessellator.draw();
            }
        } finally {
            GL11.glDepthMask(true);
            if (!cullOn) {
                GL11.glDisable(GL11.GL_CULL_FACE);
            } else {
                GL11.glEnable(GL11.GL_CULL_FACE);
            }
            if (blendOn) {
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            } else {
                GL11.glDisable(GL11.GL_BLEND);
            }
            if (lightingOn) {
                GL11.glEnable(GL11.GL_LIGHTING);
            } else {
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if (textureOn) {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            } else {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
        }
    }

    private void renderShip(NBTTagCompound entry, int index, double x, double y, double z, long worldTime,
        float partialTicks, List<TileEntityEyeOfHarmony.PlanetSpec> planets, float starSize, World world,
        List<NBTTagCompound> sites) {
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
        // keeps orbiting — the client tracks it). The old isExplorer ROLE check is gone: a ship's target is now
        // decided by its PROGRAM (MOVE target), not by its role.
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
        // body / the gateway).
        double[] pos;
        switch (state) {
            case OUTBOUND:
                pos = lerp(
                    travelFrom,
                    hover,
                    legProgress(phase, payload, travelDistance, shipRenderTime, USSShipState.OUTBOUND, legId));
                break;
            case RETURNING:
                // Gateway render pass: the return leg ends at the DOME-EDGE gateway render (the gray circle),
                // not the actual (dome-external) gateway block.
                pos = lerp(
                    travelFrom,
                    gwRender,
                    legProgress(phase, payload, travelDistance, shipRenderTime, USSShipState.RETURNING, legId));
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
        }

        boolean lightingEnabled = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean cullEnabled = GL11.glIsEnabled(GL11.GL_CULL_FACE);

        GL11.glPushMatrix();
        try {
            GL11.glTranslated(x + pos[0], y + pos[1], z + pos[2]);
            GL11.glScalef((float) CELL_SIZE, (float) CELL_SIZE, (float) CELL_SIZE);
            // Orientation: headingFor returns (yaw, pitch) whose derivation applies YAW to the model first, then
            // PITCH — i.e. the model rotation is R_pitch * R_yaw (yaw on the right, applied to the vertex first).
            // OpenGL post-multiplies each glRotated, so to get M = R_pitch * R_yaw we must issue PITCH first and
            // YAW second. (Emitting yaw-then-pitch yields R_yaw * R_pitch, which points the nose off-target for any
            // diagonal direction — verified empirically; axis-aligned headings are unaffected because one angle is 0.)
            GL11.glRotated((float) pitch, 1.0f, 0.0f, 0.0f);
            GL11.glRotated((float) yaw, 0.0f, 1.0f, 0.0f);
            // Center the model (cells span 0..n-1 on each axis).
            GL11.glTranslated(-(model.width - 1) / 2.0, -(model.height - 1) / 2.0, -(model.depth - 1) / 2.0);

            Minecraft.getMinecraft()
                .getTextureManager()
                .bindTexture(TextureMap.locationBlocksTexture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            // Culling off for the ship: the hull is a hollow shell of blocks plus thin cover quads, and we cannot
            // assume every face's winding from the outside — the back sides are depth-occluded by the cube volume,
            // so disabling culling only guarantees the faces we want (covers included) actually draw.
            GL11.glDisable(GL11.GL_CULL_FACE);

            model.vao.render();

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        } finally {
            if (!cullEnabled) {
                GL11.glDisable(GL11.GL_CULL_FACE);
            } else {
                GL11.glEnable(GL11.GL_CULL_FACE);
            }
            if (!lightingEnabled) {
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            GL11.glPopMatrix();
        }

        // Pass 8: the mining laser — a thin glowing rod from the MIDDLE of the ship to the MIDDLE of the body it
        // works (user spec), for MINERS and STARLIFTERS during the MINING leg, fading in over the leg's start and
        // out over its end (VoidcraftShipFx.beamFade) so OUTBOUND→MINING→RETURNING reads as the beam engaging
        // and releasing.
        if (state == USSShipState.MINING && VoidcraftShipFx.minesWithBeam(payload)) {
            double fade = VoidcraftShipFx
                .beamFade(legProgress(phase, payload, travelDistance, shipRenderTime, USSShipState.MINING, legId));
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
        // leg for its real duration and the working-state effects render for their full length. Role-based (not
        // target-based) so it also shows for the rare all-points-scanned fallback (target -1). Pass 26 also
        // de-rotated it (user: "it's a 'twisted' cube, it should be a cube") — see renderScanCube.
        if (state == USSShipState.MINING
            && VoidcraftRole.EXPLORER.isActive(VoidcraftNbt.readInt(payload, VoidcraftNbt.TAG_ROLES))) {
            // Pass 28 (user: "the cube itself should be half the size") + pass 31 (user: "make the cube half the
            // size — it can be quite small around the ship"): 0.25× the pass-26/27 wrap radius.
            double half = (Math.max(0.75, 0.5 * model.maxAxis() * CELL_SIZE + 0.25)) * 0.25;
            // Pass 31: shipRenderTime (worldTime + partialTicks, FRACTIONAL) drives the size pulse and the spin.
            renderScanCube(new double[] { x + pos[0], y + pos[1], z + pos[2] }, shipRenderTime, half, seed);
        }

        // Pass 8: exhaust — smoke emitted BEHIND the ship, the opposite of its travel direction (user spec),
        // only on the legs it actually moves (OUTBOUND / RETURNING — not while it hovers on the body).
        //
        // NOTE (the fix for "no particles showed up"): World.spawnParticle(String, ...) is GT5U's machine-output
        // hook — on a CLIENT world it just loops an empty IWorldAccess list and spawns NOTHING. Real client
        // particles are EntityFX instances added to the effect renderer (the codebase pattern, cf.
        // ClientProxy.em_particle): EntitySmokeFX + Minecraft.effectRenderer.addEffect.
        if (state == USSShipState.OUTBOUND || state == USSShipState.RETURNING) {
            // Phase C: exhaust is opposite the leg's direction of travel (legFrom → leg end), not the legacy
            // gateway→hover chord (a MOVE→MOVE leg's gateway is irrelevant to its path). Gateway render pass:
            // the return leg travels to the DOME-EDGE gateway render.
            double[] legTo2 = (state == USSShipState.OUTBOUND) ? hover : gwRender;
            double dx = legTo2[0] - legFrom[0];
            double dy = legTo2[1] - legFrom[1];
            double dz = legTo2[2] - legFrom[2];
            double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
            // The render loop runs MANY frames per tick — the gate is re-evaluated every frame, so without this
            // dedupe each active tick would spawn dozens of puffs per ship. One burst per active tick.
            if (len > 1e-9 && VoidcraftShipFx.exhaustGate(worldTime, seed) && phase.lastExhaustTick != worldTime) {
                phase.lastExhaustTick = worldTime;
                double speed = 0.05 + (Math.abs(seed) % 5) * 0.01; // 0.05–0.09 blocks/tick — a visible plume
                for (int puff = 0; puff < 3; puff++) { // a small burst per tick → a continuous-looking trail
                    EntitySmokeFX fx = new EntitySmokeFX(
                        world,
                        x + pos[0] + (Math.random() - 0.5) * 0.2,
                        y + pos[1] + (Math.random() - 0.5) * 0.2,
                        z + pos[2] + (Math.random() - 0.5) * 0.2,
                        -dx / len * speed,
                        -dy / len * speed,
                        -dz / len * speed,
                        1.0F);
                    Minecraft.getMinecraft().effectRenderer.addEffect(fx);
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

    private static void queueBeam(double[] start, double[] end, double fade, long worldTime, double red, double green,
        double blue) {
        BEAM_QUEUE.add(
            new double[] { start[0], start[1], start[2], end[0], end[1], end[2], fade, (double) worldTime, red, green,
                blue });
    }

    /**
     * Draws the queued lasers ({@link #BEAM_QUEUE}) in the {@code RenderWorldLastEvent} pass, once per frame. The
     * event fires with the world's modelview still active and the captured endpoints are in the same
     * camera-relative frame the tile-entity pass used, so no matrix adjustment is needed.
     */
    @SideOnly(Side.CLIENT)
    public static class BeamWorldLastRenderer {

        @SubscribeEvent
        public void onRenderWorldLast(RenderWorldLastEvent event) {
            if (BEAM_QUEUE.isEmpty()) {
                return;
            }
            int depthFunc = GL11.glGetInteger(GL11.GL_DEPTH_FUNC);
            GL11.glPushAttrib(
                GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT
                    | GL11.GL_DEPTH_BUFFER_BIT
                    | GL11.GL_CURRENT_BIT
                    | GL11.GL_TEXTURE_BIT);
            GL11.glPushMatrix();
            try {
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthFunc(GL11.GL_LEQUAL);
                for (int i = 0; i < BEAM_QUEUE.size(); i++) {
                    double[] b = BEAM_QUEUE.get(i);
                    renderBeam(
                        new double[] { b[0], b[1], b[2] },
                        new double[] { b[3], b[4], b[5] },
                        b[6],
                        (long) b[7],
                        b[8],
                        b[9],
                        b[10]);
                }
            } finally {
                BEAM_QUEUE.clear();
                GL11.glDepthFunc(depthFunc);
                GL11.glPopMatrix();
                GL11.glPopAttrib();
            }
        }
    }

    /**
     * The mining laser rod between two WORLD points: a thin box (four side quads) in a bright cyan with
     * additive blending and a gentle pulse — the classic "laser" reading. Culling is disabled for the rod (its
     * winding must not fight the world's GL_CULL_FACE), lighting off (it is emissive), and depth writes off — the
     * beam is drawn in the world-last pass (see {@link BeamWorldLastRenderer}), after every opaque geometry has
     * written its depth, so the depth test alone gives the correct picture (the planet's surface and the hull
     * occlude the part of the rod behind them, the starfield shows behind it) and no later opaque draw can
     * overpaint it.
     *
     * <p>
     * <strong>Texture OFF while drawing</strong> (the fix for "no beam showed up"): the ship model pass leaves
     * the block atlas bound with GL_TEXTURE_2D enabled; color-only Tessellator quads then get modulated by the
     * atlas pixel at UV (0,0) and render dark/invisible. Vanilla's own thin-bright-rod renderer
     * ({@code RenderLightningBolt}) does exactly this: disable the texture, draw the color quads, re-enable it.
     */
    private static void renderBeam(double[] start, double[] end, double fade, long worldTime) {
        renderBeam(start, end, fade, worldTime, 0.15, 0.75, 1.0);
    }

    /**
     * One beam of a custom color between two WORLD points (same rod as {@link #renderBeam(double[], double[],
     * double, long)}: thin box, additive blending, gentle pulse, depth writes off, texture off).
     *
     * @param start     the beam's origin (world x/y/z)
     * @param end       the beam's target (world x/y/z)
     * @param fade      0..1 — 0 is invisible (the fade ramp); 1 fully bright
     * @param worldTime the render time (the pulse phase)
     * @param red       the beam's color (0..1)
     * @param green     the beam's color (0..1)
     * @param blue      the beam's color (0..1)
     */
    private static void renderBeam(double[] start, double[] end, double fade, long worldTime, double red, double green,
        double blue) {
        double[] b = VoidcraftShipFx.beamBasis(start, end);
        if (b == null) {
            return; // endpoints coincide — degenerate geometry, nothing to draw
        }
        double p1x = b[3];
        double p1y = b[4];
        double p1z = b[5];
        double p2x = b[6];
        double p2y = b[7];
        double p2z = b[8];
        double w = VoidcraftShipFx.BEAM_HALF_WIDTH;

        boolean lightingOn = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean blendOn = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean cullOn = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        boolean textureOn = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_TEXTURE_2D); // see the javadoc — REQUIRED, or the rod samples the block atlas
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE); // additive — the beam glows instead of washing out
        GL11.glDepthMask(false);
        try {
            double pulse = 0.85 + 0.15 * Math.sin(worldTime / 2.5);
            float alpha = (float) (0.9 * fade * pulse);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F((float) red, (float) green, (float) blue, alpha);
            double[] sx = { -1.0, 1.0, 1.0, -1.0 };
            double[] sz = { -1.0, -1.0, 1.0, 1.0 };
            for (int i = 0; i < 4; i++) {
                int j = (i + 1) % 4;
                tessellator.addVertex(
                    start[0] + w * (sx[i] * p1x + sz[i] * p2x),
                    start[1] + w * (sx[i] * p1y + sz[i] * p2y),
                    start[2] + w * (sx[i] * p1z + sz[i] * p2z));
                tessellator.addVertex(
                    end[0] + w * (sx[i] * p1x + sz[i] * p2x),
                    end[1] + w * (sx[i] * p1y + sz[i] * p2y),
                    end[2] + w * (sx[i] * p1z + sz[i] * p2z));
                tessellator.addVertex(
                    end[0] + w * (sx[j] * p1x + sz[j] * p2x),
                    end[1] + w * (sx[j] * p1y + sz[j] * p2y),
                    end[2] + w * (sx[j] * p1z + sz[j] * p2z));
                tessellator.addVertex(
                    start[0] + w * (sx[j] * p1x + sz[j] * p2x),
                    start[1] + w * (sx[j] * p1y + sz[j] * p2y),
                    start[2] + w * (sx[j] * p1z + sz[j] * p2z));
            }
            tessellator.draw();
        } finally {
            GL11.glDepthMask(true);
            if (!cullOn) {
                GL11.glDisable(GL11.GL_CULL_FACE);
            } else {
                GL11.glEnable(GL11.GL_CULL_FACE);
            }
            if (blendOn) {
                // Vanilla's default alpha blend — the additive func above only served the beam.
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            } else {
                GL11.glDisable(GL11.GL_BLEND);
            }
            if (lightingOn) {
                GL11.glEnable(GL11.GL_LIGHTING);
            } else {
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if (textureOn) {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            } else {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
        }
    }

    // Pass 31 (user: the scan field "works" but is "visually a bit boring" — add gentle, living motion).
    /** One full size-BREATH cycle, in ticks — ~3 s per pulse (a slow, gentle breathing, not a strobe). */
    private static final double SCAN_PULSE_PERIOD_TICKS = 60.0;
    /** Breathing AMPLITUDE: the half-size oscillates ±10% about the base (user: "pulsate slightly ~10%"). */
    private static final double SCAN_PULSE_AMPLITUDE = 0.80;
    /** One full SPIN about the world up (Y) axis, in ticks — ~24 s per revolution (a slow, steady turn). */
    private static final double SCAN_SPIN_PERIOD_TICKS = 240.0;
    private static final double SCAN_TWO_PI = 2.0 * Math.PI;

    /**
     * The Explorer's scanning effect (user spec: "a small visual effect — maybe a rotating, transparent cube
     * around the ship while scanning — would help identifying the ships"): a translucent cube around the ship.
     *
     * <p>
     * Pass 26 (user: "the scanning texture is some kind of a 'twisted' cube — it should be a cube"): the steady
     * Y-axis spin made the box read as a twisting solid, so it became a STATIC, axis-aligned cube — the clearest
     * possible "cube" silhouette. ({@code worldTime}/{@code seed} are retained for signature stability; a static
     * cube needs neither.)
     *
     * <p>
     * Pass 28 (user: "half the size, half the transparency, no 'wireframe' borders, just the transparent faces,
     * and rotated 45 degrees on all axis to look like a diamond"): the half-size is halved at the call site, the
     * face alpha is halved (0.16 → 0.08), the 12-edge outline is GONE (faces only), and a STATIC 45° rotation
     * about all three axes is applied — the cube is still (a fixed pose, not the removed Y-spin) but corner-up,
     * reading as a diamond.
     *
     * <p>
     * Pass 30 (user: "the scanning effect seems to not render anymore after the latest changes"): the code was
     * intact — the pass-28 spec (half size + half alpha + no edge outline) simply left it with almost no visible
     * ink, and the smaller diamond is mostly swallowed by the planet the ship hovers over (it only clears the
     * surface by ~0.1 blocks). The faces now render as an ADDITIVE GLOW (the beam's pattern —
     * {@code GL_SRC_ALPHA, GL_ONE}) at a visible intensity: the scan field reads as an energy halo that glows
     * against ANY background (space or planet surface), instead of a faint glass box that depth-testing and the
     * 8% alpha erased.
     *
     * <p>
     * Pass 31 (user: the effect "works now" but is "visually a bit boring" — add life): the field is now a LIVING
     * halo that (a) gently BREATHES — its size oscillates ±10% around the base on a ~3-second sine
     * ({@link #SCAN_PULSE_PERIOD_TICKS} / {@link #SCAN_PULSE_AMPLITUDE}), (b) SPINS slowly about the world up (Y)
     * axis — the pass-26 Y-spin is back, but now on the smaller, translucent, diamond-posed field it reads as a
     * rotating gem rather than a twisting solid (one revolution per {@link #SCAN_SPIN_PERIOD_TICKS}), and (c) is
     * both smaller (call site halved again) and more translucent (glow intensity ×0.25). The fractional render
     * time ({@code worldTime + partialTicks}) drives (a) and (b), so they animate smoothly frame-to-frame.
     *
     * <p>
     * GL discipline mirrors {@link #renderBeam}: texture OFF (a color-only overlay — the block atlas would
     * modulate it), lighting OFF (emissive), cull OFF (winding-independent), ADDITIVE blend (pass 30), and depth
     * WRITES off (a pure overlay; it is still depth-TESTED, so the dome/shell occlusion stays correct).
     *
     * @param center     the cube center in world coordinates (the ship's current position)
     * @param renderTime fractional render time in TICKS ({@code worldTime + partialTicks}) — drives the size
     *                   pulse (pass 31) and the spin about the up axis (pass 31)
     * @param half       the cube half-size in blocks (the call site already halves it per pass 28/31)
     * @param seed       the per-launch seed (unused — the field is a pure function of renderTime)
     */
    private static void renderScanCube(double[] center, double renderTime, double half, int seed) {
        boolean lightingOn = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean blendOn = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean cullOn = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        boolean textureOn = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE); // Pass 30: additive — the scan field GLOWS (the beam's
                                                          // pattern), visible against any background
        GL11.glDepthMask(false);
        try {
            GL11.glPushMatrix();
            GL11.glTranslated(center[0], center[1], center[2]);
            // Pass 31 (user: "make it rotate on the horizontal plane (around the upwards axis)"): spin about the
            // world UP (Y) axis. GL post-multiplies, so issuing it FIRST (outermost) makes it the WORLD-SPACE
            // transform: the whole diamond (the static 45/45/45 pose below) turns about the vertical axis, like a
            // slowly rotating gem. (Pass 26 dropped this spin because it read as a "twisting" SOLID on the big
            // glass cube; on the small translucent halo it now reads as gentle life, per the pass-31 spec.)
            double spinDeg = (renderTime * (360.0 / SCAN_SPIN_PERIOD_TICKS)) % 360.0;
            GL11.glRotated(spinDeg, 0.0, 1.0, 0.0);
            // Pass 28 (user: "it should be rotated 45 degrees on all axis to look like a diamond"): a STATIC 45°
            // pose about all three axes — the cube's long diagonal points along the system axes and it reads as a
            // diamond. (The fixed 45/45/45 pose is the gem shape; the pass-31 spin above turns it.)
            GL11.glRotated(45.0, 1.0, 0.0, 0.0);
            GL11.glRotated(45.0, 0.0, 1.0, 0.0);
            GL11.glRotated(45.0, 0.0, 0.0, 1.0);

            // The 8 corners (±half on each axis). v0..v3 = the z = -h square, v4..v7 = the z = +h square, each
            // with the same x/y pattern — so the 12 edges / 6 square faces below form a true cube. (Pass 26: the
            // old vz pattern {-h,h,-h,h,h,h,-h,h} made v1==v5, v2==v6, v3==v7 — only FOUR distinct corners, which
            // rendered as a tetrahedron: the "prism shape instead of a cube" the user reported.)
            // Pass 31 (user: "make the size of the cube pulsate slightly up and down (~10%)"): a smooth ±10%
            // BREATHING driven by the fractional render time (one cycle per SCAN_PULSE_PERIOD_TICKS ≈ 3 s) — the
            // field reads as alive, not a frozen box. (renderTime % period keeps the sine argument small even for
            // huge world times.)
            double phase = (renderTime % SCAN_PULSE_PERIOD_TICKS) / SCAN_PULSE_PERIOD_TICKS;
            double h = half * (1.0 + SCAN_PULSE_AMPLITUDE * Math.sin(SCAN_TWO_PI * phase));
            double[] vx = { -h, -h, h, h, -h, -h, h, h };
            double[] vy = { -h, h, -h, h, -h, h, -h, h };
            double[] vz = { -h, -h, -h, -h, h, h, h, h };

            // The translucent faces (the "glass" box).
            int[][] faces = {
                // z = -h
                { 0, 1, 3, 2 },
                // z = +h
                { 4, 5, 7, 6 },
                // x = -h
                { 0, 4, 5, 1 },
                // x = +h
                { 2, 6, 7, 3 },
                // y = -h
                { 0, 2, 6, 4 },
                // y = +h
                { 1, 5, 7, 3 } };
            Tessellator tess = Tessellator.instance;
            tess.startDrawingQuads();
            // Pass 30 made this a VISIBLE additive glow (0.45). Pass 31 (user: "multiply the transparency by
            // 0.25x"): 0.45 × 0.25 = 0.1125 — a subtle, soft halo. The smaller size + the spin + the breathing
            // (pass 31) carry the "life", so the lower intensity is fine — it glows gently instead of a neon sign.
            tess.setColorRGBA_F(0.30F, 0.70F, 1.0F, 0.1125F);
            for (int[] f : faces) {
                tess.addVertex(vx[f[0]], vy[f[0]], vz[f[0]]);
                tess.addVertex(vx[f[1]], vy[f[1]], vz[f[1]]);
                tess.addVertex(vx[f[2]], vy[f[2]], vz[f[2]]);
                tess.addVertex(vx[f[3]], vy[f[3]], vz[f[3]]);
            }
            tess.draw();
            // Pass 28 (user: "it should not have the 'wireframe' borders, just the transparent faces"): the
            // 12-edge outline is gone — the diamond reads by its faces alone (now an additive glow, pass 30/31).
            GL11.glPopMatrix();
        } finally {
            GL11.glDepthMask(true);
            if (!cullOn) {
                GL11.glDisable(GL11.GL_CULL_FACE);
            } else {
                GL11.glEnable(GL11.GL_CULL_FACE);
            }
            if (blendOn) {
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            } else {
                GL11.glDisable(GL11.GL_BLEND);
            }
            if (lightingOn) {
                GL11.glEnable(GL11.GL_LIGHTING);
            } else {
                GL11.glDisable(GL11.GL_LIGHTING);
            }
            if (textureOn) {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            } else {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            }
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
        USSShipState state, int legId) {
        // Phase C: a new leg id resets the progress even for the SAME state (MOVE → MOVE legs of one program).
        boolean fresh = phase.lastState != state.getId() || phase.lastLegId != legId || phase.startTick < 0;
        if (fresh) {
            phase.lastState = state.getId();
            phase.lastLegId = legId;
            phase.startTick = renderTime;
        }
        double speed = VoidcraftNbt.readDouble(payload, VoidcraftNbt.TAG_SPEED);
        long mining = VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_MINING);
        // The Explorer pass: the WORK leg is role-aware — an Explorer SCANS (scanTicks, from vc_scan), everything
        // else MINES (mineTicks, from vc_mining). The client must match the server's work-leg duration or the
        // hover/leg progress drifts.
        int roles = VoidcraftNbt.readInt(payload, VoidcraftNbt.TAG_ROLES);
        long scan = VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_SCAN);
        long leg = USSConstants.legTicks(state, travelDistance, speed, mining, roles, scan);
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
     * with the site's progress (the parts filling in). Same GL discipline as the ripples: texture OFF (color
     * only), lighting OFF, culling OFF, standard alpha blend, depth writes OFF (pure overlays, still
     * depth-TESTED so the bodies occlude them).
     */
    private static void renderSites(List<NBTTagCompound> sites, List<TileEntityEyeOfHarmony.PlanetSpec> planets,
        float starSize, double x, double y, double z, long worldTime, float partialTicks) {
        if (sites == null || sites.isEmpty()) {
            return;
        }
        double renderTime = (double) worldTime + partialTicks;
        boolean lightingOn = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean blendOn = GL11.glIsEnabled(GL11.GL_BLEND);
        boolean cullOn = GL11.glIsEnabled(GL11.GL_CULL_FACE);
        boolean textureOn = GL11.glIsEnabled(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDepthMask(false);
        try {
            Tessellator tess = Tessellator.instance;
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
                double[] cx = { px - sx, px + sx, px + sx, px - sx, px - sx, px + sx, px + sx, px - sx };
                double[] cy = { py - sy, py - sy, py + sy, py + sy, py - sy, py - sy, py + sy, py + sy };
                double[] cz = { pz - sz, pz - sz, pz - sz, pz - sz, pz + sz, pz + sz, pz + sz, pz + sz };
                int[] edges = { 0, 1, 1, 2, 2, 3, 3, 0, 4, 5, 5, 6, 6, 7, 7, 4, 0, 4, 1, 5, 2, 6, 3, 7 };
                // Wireframe: a gentle time-driven alpha pulse (deterministic — every client pulses identically).
                float pulse = (float) Math.sin(worldTime * 0.15 + i * 1.7);
                float wireAlpha = 0.6F + 0.25F * pulse;
                tess.startDrawing(GL11.GL_LINES);
                tess.setColorRGBA_F(0.55F, 0.55F, 0.55F, wireAlpha); // startDrawing resets the color — set it after
                for (int e = 0; e < edges.length; e++) {
                    int c = edges[e];
                    tess.addVertex(cx[c], cy[c], cz[c]);
                }
                tess.draw();
                // Progressive fill: a translucent box scaled by the site's progress (the parts filling in).
                if (progress > 0.001) {
                    double fw = sx * progress;
                    double fh = sy * progress;
                    double fd = sz * progress;
                    double ax = px - fw, bx = px + fw, ay = py - fh, by = py + fh, az = pz - fd, bz = pz + fd;
                    tess.startDrawing(7);
                    tess.setColorRGBA_F(0.42F, 0.42F, 0.42F, 0.16F);
                    tess.addVertex(ax, ay, az);
                    tess.addVertex(bx, ay, az);
                    tess.addVertex(bx, ay, bz);
                    tess.addVertex(ax, ay, bz);
                    tess.addVertex(ax, by, bz);
                    tess.addVertex(bx, by, bz);
                    tess.addVertex(bx, by, az);
                    tess.addVertex(ax, by, az);
                    tess.addVertex(ax, ay, az);
                    tess.addVertex(bx, ay, az);
                    tess.addVertex(bx, by, az);
                    tess.addVertex(ax, by, az);
                    tess.addVertex(bx, ay, bz);
                    tess.addVertex(ax, ay, bz);
                    tess.addVertex(ax, by, bz);
                    tess.addVertex(bx, by, bz);
                    tess.addVertex(ax, ay, bz);
                    tess.addVertex(ax, ay, az);
                    tess.addVertex(ax, by, az);
                    tess.addVertex(ax, by, bz);
                    tess.addVertex(bx, ay, az);
                    tess.addVertex(bx, ay, bz);
                    tess.addVertex(bx, by, bz);
                    tess.addVertex(bx, by, az);
                    tess.draw();
                }
            }
        } finally {
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDepthMask(true);
            if (!textureOn) {
                GL11.glDisable(GL11.GL_TEXTURE_2D);
            } else {
                GL11.glEnable(GL11.GL_TEXTURE_2D);
            }
            if (!lightingOn) {
                GL11.glDisable(GL11.GL_LIGHTING);
            } else {
                GL11.glEnable(GL11.GL_LIGHTING);
            }
            if (!cullOn) {
                GL11.glDisable(GL11.GL_CULL_FACE);
            } else {
                GL11.glEnable(GL11.GL_CULL_FACE);
            }
            if (!blendOn) {
                GL11.glDisable(GL11.GL_BLEND);
            }
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
        float starSize, double x, double y, double z, long worldTime, float partialTicks) {
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
            long integrity = entry.getLong(TileEntityVoidcraftShip.TAG_BASE_INTEGRITY);
            long maxIntegrity = entry.getLong(TileEntityVoidcraftShip.TAG_BASE_INTEGRITY_MAX);
            float f = maxIntegrity > 0 ? (float) Math.max(0.0, Math.min(1.0, (double) integrity / maxIntegrity)) : 1.0F;
            float tint = 0.35F + 0.65F * f;
            boolean lightingEnabled = GL11.glIsEnabled(GL11.GL_LIGHTING);
            boolean cullEnabled = GL11.glIsEnabled(GL11.GL_CULL_FACE);
            GL11.glPushMatrix();
            try {
                GL11.glTranslated(x + pos[0], y + pos[1], z + pos[2]);
                GL11.glScalef((float) CELL_SIZE, (float) CELL_SIZE, (float) CELL_SIZE);
                // Center the model (cells span 0..n-1 on each axis).
                GL11.glTranslated(-(model.width - 1) / 2.0, -(model.height - 1) / 2.0, -(model.depth - 1) / 2.0);
                Minecraft.getMinecraft()
                    .getTextureManager()
                    .bindTexture(TextureMap.locationBlocksTexture);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glColor4f(1.0F, tint, tint, 1.0F);
                GL11.glDisable(GL11.GL_CULL_FACE);
                model.vao.render();
            } finally {
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glDisable(GL12.GL_RESCALE_NORMAL);
                if (!cullEnabled) {
                    GL11.glDisable(GL11.GL_CULL_FACE);
                } else {
                    GL11.glEnable(GL11.GL_CULL_FACE);
                }
                if (!lightingEnabled) {
                    GL11.glDisable(GL11.GL_LIGHTING);
                }
                GL11.glPopMatrix();
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
