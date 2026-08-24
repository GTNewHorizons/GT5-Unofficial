package tectech.voidcraft.render;

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

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tectech.thing.block.TileEntityEyeOfHarmony;
import tectech.voidcraft.loader.VoidcraftLoader;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftNbt;
import tectech.voidcraft.uss.USSConstants;
import tectech.voidcraft.uss.USSFleetOrbit;
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
 * Orientation: the nose (the ship's visual front — the blueprint depth axis, confirmed in playtest) follows the
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
        long startTick = -1;
        double yaw = 0.0;
        double pitch = 0.0;
        boolean headingInit = false;
        double lastFrame = -1.0;
        /** Tick of the last exhaust burst (render runs many frames per tick — one burst per active tick max). */
        long lastExhaustTick = -1;
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
        if (ships.isEmpty()) {
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

        // Pass 7: the system the fleet works (specs + star size ride with the fleet TE — no world lookups).
        List<TileEntityEyeOfHarmony.PlanetSpec> planets = fleet.getSystemPlanets();
        float starSize = fleet.getStarSize();

        for (int i = 0; i < ships.size(); i++) {
            renderShip(ships.get(i), i, x, y, z, worldTime, partialTicks, planets, starSize, tileEntity.getWorldObj());
        }
    }

    private void renderShip(NBTTagCompound entry, int index, double x, double y, double z, long worldTime,
        float partialTicks, List<TileEntityEyeOfHarmony.PlanetSpec> planets, float starSize, World world) {
        NBTTagCompound payload = entry.getCompoundTag(TileEntityVoidcraftShip.TAG_ENTRY_PAYLOAD);
        if (payload == null) {
            return;
        }
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
        double[] body = targetBody(target, planets, starSize, renderTime);
        boolean isPlanet = target >= 0 && planets != null && target < planets.size();
        // Pass 8: "0.5 blocks above that planet" = 0.5 above the SURFACE. Pass 9: the rendered planet is a unit
        // CUBE of size spec.scale (±0.5·scale — its surface sits 0.5·scale above its center), so hovering a flat
        // 0.5 above the CENTER would put the ship INSIDE the planets and depth-occlude the miner beam.
        double hoverAbove = isPlanet ? USSConstants.HOVER_ABOVE_PLANET + 0.5 * planets.get(target).scale
            : USSConstants.HOVER_ABOVE_STAR;

        // Swarm spread: each ship hovers at its own stable spot AROUND the shared hover point, so a large fleet
        // reads as a swarm around its target instead of a stack (USSFleetOrbit keeps it inside the space shell).
        double[] gw = { gwArr[0], gwArr[1], gwArr[2] };
        double[] hover = { body[0] + spread[0], body[1] + hoverAbove + spread[1], body[2] + spread[2] };

        LegPhase phase = phases.get(key);
        if (phase == null || !seenUuids.contains(key)) {
            // Fresh appearance (first frame, chunk reload, or a RELAUNCHED ship after its previous mission
            // completed): reset the leg progress, or a stale startTick would jump the ship.
            phase = new LegPhase();
            phases.put(key, phase);
        }

        // Pass 11 (user: "the bobbing is a bit aggressive now — remove it completely, it doesn't make much sense
        // for the ships to go up and down"): no vertical bob — ships hold a fixed hover altitude.
        double[] pos;
        switch (state) {
            case OUTBOUND:
                pos = lerp(gw, hover, legProgress(phase, payload, worldTime, USSShipState.OUTBOUND));
                break;
            case RETURNING:
                pos = lerp(hover, gw, legProgress(phase, payload, worldTime, USSShipState.RETURNING));
                break;
            case MINING:
                // Work the body: hover just above it (0.5 over the planet SURFACE / 2.5 over the star), fixed
                // altitude (pass 11: the vertical bob is gone), nose forward (pass 10: the ship keeps its arrival
                // heading — it does not turn to face the body; headingFor).
                pos = new double[] { hover[0], hover[1], hover[2] };
                break;
            case DOCKED:
            default:
                // Hover just above the anchor (the fleet anchor carries no docked ship; kept for completeness).
                // Pass 11: the bob is gone — a fixed hover height.
                pos = new double[] { 0.0, 0.9, 0.0 };
                break;
        }

        double yaw = 0.0;
        double pitch = 0.0;
        double[] heading = headingFor(gw, hover, state);
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

        GL11.glPushMatrix();
        try {
            GL11.glTranslated(x + pos[0], y + pos[1], z + pos[2]);
            GL11.glScalef((float) CELL_SIZE, (float) CELL_SIZE, (float) CELL_SIZE);
            // Yaw (around +Y) first, then pitch (around +X) — maps the model nose (+Z) onto the target
            // direction; see headingFor for the derivation.
            GL11.glRotated((float) yaw, 0.0f, 1.0f, 0.0f);
            GL11.glRotated((float) pitch, 1.0f, 0.0f, 0.0f);
            // Center the model (cells span 0..n-1 on each axis).
            GL11.glTranslated(-(model.width - 1) / 2.0, -(model.height - 1) / 2.0, -(model.depth - 1) / 2.0);

            Minecraft.getMinecraft()
                .getTextureManager()
                .bindTexture(TextureMap.locationBlocksTexture);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);

            model.vao.render();

            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        } finally {
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
            double fade = VoidcraftShipFx.beamFade(legProgress(phase, payload, worldTime, USSShipState.MINING));
            if (fade > 0.0) {
                renderBeam(
                    new double[] { x + pos[0], y + pos[1], z + pos[2] },
                    new double[] { x + body[0], y + body[1], z + body[2] },
                    fade,
                    worldTime);
            }
        }

        // Pass 8: exhaust — smoke emitted BEHIND the ship, the opposite of its travel direction (user spec),
        // only on the legs it actually moves (OUTBOUND / RETURNING — not while it hovers on the body).
        //
        // NOTE (the fix for "no particles showed up"): World.spawnParticle(String, ...) is GT5U's machine-output
        // hook — on a CLIENT world it just loops an empty IWorldAccess list and spawns NOTHING. Real client
        // particles are EntityFX instances added to the effect renderer (the codebase pattern, cf.
        // ClientProxy.em_particle): EntitySmokeFX + Minecraft.effectRenderer.addEffect.
        if (state == USSShipState.OUTBOUND || state == USSShipState.RETURNING) {
            double dx = state == USSShipState.OUTBOUND ? hover[0] - gw[0] : gw[0] - hover[0];
            double dy = state == USSShipState.OUTBOUND ? hover[1] - gw[1] : gw[1] - hover[1];
            double dz = state == USSShipState.OUTBOUND ? hover[2] - gw[2] : gw[2] - hover[2];
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
     * The mining laser rod between two WORLD points (pass 8): a thin box (four side quads) in a bright cyan with
     * additive blending and a gentle pulse — the classic "laser" reading. Culling is disabled for the rod (its
     * winding must not fight the world's GL_CULL_FACE), lighting off (it is emissive), and depth writes off (it is
     * a pure overlay between the ship and the body, still depth-TESTED so the planet's surface correctly occludes
     * the part of the rod inside it).
     *
     * <p>
     * <strong>Texture OFF while drawing</strong> (the fix for "no beam showed up"): the ship model pass leaves
     * the block atlas bound with GL_TEXTURE_2D enabled; color-only Tessellator quads then get modulated by the
     * atlas pixel at UV (0,0) and render dark/invisible. Vanilla's own thin-bright-rod renderer
     * ({@code RenderLightningBolt}) does exactly this: disable the texture, draw the color quads, re-enable it.
     */
    private static void renderBeam(double[] start, double[] end, double fade, long worldTime) {
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
            tessellator.setColorRGBA_F(0.15F, 0.75F, 1.0F, alpha);
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

    /**
     * Local leg progress in [0, 1], derived from the same leg duration the server ticks with.
     */
    private double legProgress(LegPhase phase, NBTTagCompound payload, long worldTime, USSShipState state) {
        if (phase.lastState != state.getId() || phase.startTick < 0) {
            phase.lastState = state.getId();
            phase.startTick = worldTime;
            return 0.0;
        }
        double speed = VoidcraftNbt.readDouble(payload, VoidcraftNbt.TAG_SPEED);
        long mining = VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_MINING);
        long leg = USSConstants.legTicks(state, speed, mining);
        if (leg <= 0) {
            return 1.0;
        }
        return Math.min(1.0, (worldTime - phase.startTick) / (double) leg);
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
     * turn to face the body) is computed first, then FLIPPED: playtest confirmed the ship's visual nose sits on
     * the model -Z side (the blueprint depth axis, toward the assembler — not out of it), so aligning +Z with the
     * path made the ships fly "backwards". Aiming +Z at -d puts the nose along the path. Applying yaw (around +Y)
     * then pitch (around +X) maps +Z onto -d via: yaw = atan2(dx, dz), pitch = -asin(dy / |d|) — the negation is
     * applied to the components first.
     */
    private double[] headingFor(double[] gw, double[] hover, USSShipState state) {
        double dx, dy, dz;
        switch (state) {
            case OUTBOUND:
                dx = hover[0] - gw[0];
                dy = hover[1] - gw[1];
                dz = hover[2] - gw[2];
                break;
            case RETURNING:
                dx = gw[0] - hover[0];
                dy = gw[1] - hover[1];
                dz = gw[2] - hover[2];
                break;
            case MINING:
                // Pass 10 (user: "the ships don't need to rotate to face the object they are mining"): keep the
                // ARRIVAL heading — the same direction of travel as the OUTBOUND leg — so the ship hovers nose
                // forward while it works the body instead of pitching down at it. The RETURNING leg's target
                // heading still flips it around with the eased turn.
                dx = hover[0] - gw[0];
                dy = hover[1] - gw[1];
                dz = hover[2] - gw[2];
                break;
            default:
                return null; // DOCKED: unrotated
        }
        // Ship nose is on the model -Z side (user playtest) — aim +Z at the opposite of the intended facing.
        dx = -dx;
        dy = -dy;
        dz = -dz;
        double len = Math.sqrt(dx * dx + dy * dy + dz * dz);
        if (len < 1e-6) {
            return null; // gateway on top of the hover point (degenerate) — no heading
        }
        double yaw = Math.toDegrees(Math.atan2(dx, dz));
        double pitch = -Math.toDegrees(Math.asin(clamp(dy / len, -1.0, 1.0)));
        return new double[] { yaw, pitch };
    }

    private static double clamp(double v, double lo, double hi) {
        return v < lo ? lo : (v > hi ? hi : v);
    }

    private static double[] lerp(double[] from, double[] to, double t) {
        return new double[] { from[0] + (to[0] - from[0]) * t, from[1] + (to[1] - from[1]) * t,
            from[2] + (to[2] - from[2]) * t };
    }
}
