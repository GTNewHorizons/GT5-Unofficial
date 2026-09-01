package tectech.voidcraft.uss;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraft.util.EnumChatFormatting.RESET;
import static net.minecraft.util.EnumChatFormatting.YELLOW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.casing.Casings;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.ErrorType;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.common.misc.GTStructureChannels;
import gregtech.common.tileentities.machines.MTEHatchInputBusME;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import tectech.thing.block.TileEntityEyeOfHarmony;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;
import tectech.voidcraft.debug.VoidcraftDebugEffectRegistry;
import tectech.voidcraft.item.ItemUSSController;
import tectech.voidcraft.item.ItemVoidbaseBlueprint;
import tectech.voidcraft.item.ItemVoidcraft;
import tectech.voidcraft.loader.VoidcraftLoader;
import tectech.voidcraft.machine.MTEVoidcraftGateway;
import tectech.voidcraft.machine.MTEVoidcraftStorageBay;
import tectech.voidcraft.render.TileEntityVoidcraftShip;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftComponent;
import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * Unstable Solar System (EoH rework, Phase 2 vertical slice).
 *
 * <p>
 * A new machine parallel to the legacy Eye of Harmony (the legacy code stays untouched, plan §1.1): the shell is
 * the legacy sphere scaled 2× about its center (pass 12: 65×65×65 — same casings and field generators, same hatch
 * rules, same per-type block counts, anchor at the scaled {@code ~} slot 32,32,0) — but instead of mining planets
 * for star matter it hosts an <em>ignitable star</em>:
 * <ul>
 * <li>COLD until a {@link ItemUSSController} is inserted into the controller slot;</li>
 * <li>IGNITED — the star burns: the render block is placed and the lifespan from {@link USSConstants} is counted
 * down every machine tick. The spacetime compression field tier decides the star's rendering size and the miner
 * ore band, the controller item decides the star class;</li>
 * <li>lifespan reaches zero — the expiry pipeline ({@link #starExpires()}) pays out the Spacetime yield +
 * Universium, evolves the star (auto-ignited on the upgraded controller) or terminates the system (controller
 * consumed, state returns to COLD).</li>
 * </ul>
 *
 * <p>
 * This slice deliberately has <b>no energy consumption</b> (the global energy model lands in Phase 6;
 * {@link USSConstants#starDrawEUt(int)} holds the placeholder table) and no recipes
 * ({@link #checkProcessing_EM()} returns NO_RECIPE — this is a state machine, not a recipe machine). The legacy EoH
 * keeps working unchanged in the same world.
 *
 * @see VoidcraftUSS
 * @see USSConstants
 * @see ItemUSSController
 * @see docs/Voidcraft_Implementation_Plan.md
 */
@SuppressWarnings("SpellCheckingInspection")
@IMetaTileEntity.SkipGenerateDescription
public class MTEUnstableSolarSystem extends TTMultiblockBase implements ISurvivalConstructable, USSPilotWorld {

    /** Structure piece name (same as the legacy EoH — the shell is identical). */
    protected static final String STRUCTURE_PIECE_MAIN = "main";

    // Region variables (same field names as the legacy EoH — the structure callbacks below set them).
    private static IIconContainer ScreenOFF;
    private static IIconContainer ScreenON;

    private int spacetimeCompressionFieldMetadata = -1;
    private int timeAccelerationFieldMetadata = -1;
    private int stabilisationFieldMetadata = -1;

    /** Render-block animation on/off (screwdriver toggle, persisted in NBT). */
    private boolean animationsEnabled = true;

    /** Star lifecycle model (persisted in NBT — the state survives chunk/world reloads). */
    private VoidcraftUSS uss = VoidcraftUSS.cold();

    /**
     * Voidbase construction sites (one per anchor — a Constructor's CONSTRUCT leg creates or fills them; a
     * completed site spawns the base and is removed). Persists in NBT; discarded on burnout/teardown like the
     * ships.
     */
    private final List<USSBaseSite> baseSites = new ArrayList<USSBaseSite>();

    /**
     * The fleet render signature last pushed to the fleet anchor (Phase D): a hash of the fleet count + every
     * entity's integrity + every site progress. The fleet tick resyncs the anchor exactly when it changes
     * (integrity decay/repair, site fill), so the client tint/fill follows the server without per-tick packets.
     */
    private long lastFleetRenderSignature = -1L;

    /**
     * The system's GATEWAYS, registered by the {@code MTEVoidcraftGateway} machines that locked onto this USS (each
     * re-registers on its launch-target scan). Keyed by world-block coords, value the same coords. This is the
     * server-side source of the gateway list the fleet anchor renders (a permanent part of the system view, even
     * with an empty fleet). Pruned of destroyed gateways (see {@link #pruneGateways(int[])}). In-memory: a world
     * reload simply re-runs the gateways' launch-target scans and re-registers.
     */
    private final Map<String, int[]> gatewayBlocks = new LinkedHashMap<String, int[]>();

    /**
     * Ticks elapsed since the last stellar-acceleration drain (0..ACCELERATION_INTERVAL_TICKS-1). Server
     * bookkeeping, not persisted — a reload simply resumes the cycle.
     */
    private long accelerationTicks = 0L;

    /**
     * Ticks elapsed since the last Stellar Injector size step (0..INJECTOR_STEP_INTERVAL_TICKS-1). Server
     * bookkeeping, not persisted — a reload simply resumes the cycle.
     */
    private long injectorStepTicks = 0L;

    /**
     * The Tachyon Rich Temporal Fluid drained by the last completed second (mB): while it is &gt; 0 the virtual orbit
     * clock advances at the proportional rate for the rest of that second. Reset on star death / machine stop.
     */
    private long lastAccelerationSecondMB = 0L;

    /**
     * The orbit-clock phase of the current acceleration window (the start/end smoothing of the virtual orbit
     * clock — ramp up on the first acceleration second, ramp down on the last). Server bookkeeping, not
     * persisted — a reload simply resumes the cycle.
     */
    private USSStellarEvolution.AccelerationPhase accelPhase = USSStellarEvolution.AccelerationPhase.IDLE;

    /**
     * Whether an acceleration second has completed since ignition (distinguishes the FIRST acceleration second —
     * the ramp-up — from the middle seconds). Server bookkeeping, not persisted.
     */
    private boolean accelerationActive = false;

    /**
     * The sub-tick remainder of the virtual orbit clock's advance: the per-tick advance is fractional while the
     * acceleration's start/end ramps run (the whole part goes into the clock, this part carries over). Server
     * bookkeeping, not persisted.
     */
    private double orbitFractionalAccumulator = 0.0;

    // Region mining mission (Phase 4 pass 5 — up to USSConstants.MAX_SHIPS_PER_USS ships in flight per USS; a large
    // fleet (dozens–hundreds) rendered by ONE fleet anchor block, not one block per ship).

    /**
     * The fleet entities: every in-flight ship AND every anchored Voidbase (a non-null anchor on the entity
     * marks the base). List index = entity SLOT (launch order for ships; bases join the tail). Each entity
     * carries its own cargo and, for a ship, its own return targets (captured at launch), so a mission from any
     * gateway/bay pair is routed back to its own launchers.
     */
    private final List<VoidcraftActiveShip> activeShips = new ArrayList<>();

    /**
     * The pilots — one per fleet entity, index-parallel to {@link #activeShips}: each pilot runs its entity's
     * program (the controller's instruction list) against this MTE (the {@link USSPilotWorld} game seam) and
     * decides its legs; the entities themselves are passive leg drivers.
     */
    private final List<USSShipPilot> pilots = new ArrayList<>();

    /**
     * In-flight SEND / TAKE transfers (ship-to-ship cargo transfer): one per executing ship, keyed by the
     * executing ship's uuid. Transient bookkeeping — NOT persisted: on a chunk reload a mid-transfer simply
     * disappears (the executing ship's command observes a false tick and reports DONE; the units already moved
     * stay moved).
     */
    private final Map<String, USSCargoTransferState> cargoTransfers = new HashMap<String, USSCargoTransferState>();

    /** One in-flight transfer: the paced leg + the target ship's identity (re-resolved by uuid every tick). */
    private static final class USSCargoTransferState {

        USSCargoTransfer leg;
        String targetUuid;
        String targetName;

        /** The target is the star's Stellar Injector cargo buffer (the SEND / TAKE star pass — no target ship). */
        boolean starTarget;
    }

    /**
     * In-flight REPAIR sessions, keyed by the EXECUTING entity's uuid: one anchored station repairing itself or
     * a co-located fleet member. Persisted (the executor's command resumes its poll after a reload).
     */
    private final Map<String, USSRepairState> repairs = new HashMap<String, USSRepairState>();

    /** One in-flight repair: the pacing countdown + the target entity's identity (re-resolved by uuid every tick). */
    private static final class USSRepairState {

        /** Ticks until the next integrity step (20 = one game second). */
        int ticks;
        /** The target entity's uuid (the entity being repaired). */
        String targetUuid;
    }

    /**
     * In-flight STABILIZE windows (the matrix's activation command), keyed by the executing base's uuid: one
     * per base (a base's own program re-arms the window per program loop). Persisted (the executor's command
     * resumes its poll after a reload; the expiry weight read queries the live sessions).
     */
    private final Map<String, USSStabilize.Session> stabilizes = new HashMap<String, USSStabilize.Session>();

    /**
     * The state id last pushed to each slot's ship render TE (avoids per-tick description packets). Starts at -1
     * ("nothing pushed yet"); on NBT load it is set to each restored ship's current state — the slot's render TE
     * already holds it (it is a separate world block with its own NBT), so no re-push is needed after a reload.
     */
    private final int[] lastPushedShipStates = initLastPushedShipStates();

    private static int[] initLastPushedShipStates() {
        int[] states = new int[USSConstants.MAX_SHIPS_PER_USS];
        Arrays.fill(states, -1);
        return states;
    }

    /**
     * The leg id last pushed to each slot's ship render TE (programming framework, Phase C): the leg ID
     * ({@link VoidcraftActiveShip#getLegId()}) is pushed alongside the state so a leg of the SAME state as the
     * previous one (a program doing MOVE → MOVE) still animates from its own start — the client resets its
     * leg-progress phase when the leg id changes.
     */
    private final int[] lastPushedLegIds = initLastPushedLegIds();

    private static int[] initLastPushedLegIds() {
        int[] ids = new int[USSConstants.MAX_SHIPS_PER_USS];
        Arrays.fill(ids, -1);
        return ids;
    }

    /**
     * Fleet anchor dirty flag (Phase 4 pass 5): set when any ship's state changes during a tick — the WHOLE fleet
     * is pushed to its one render block at most once per tick (a full-fleet description packet beats N per-ship
     * packets once the fleet has dozens–hundreds of ships).
     */
    private boolean fleetDirty;

    /**
     * Once per MTE lifetime (in-memory; the check is idempotent) — see {@link #cleanupLegacyShipRender()}.
     */
    private boolean legacyShipRenderCleaned;

    /**
     * Plain log4j logger at INFO (NOT {@code GTLog.conditionalLogger}, which forces Level.OFF) — mission lifecycle
     * must actually reach the game log.
     */
    private static final Logger LOGGER = LogManager.getLogger("Voidcraft USS");

    /**
     * The interval in MTE TICKS between per-ship progress heartbeats (100 ticks = 5 in-game seconds): a line per
     * in-flight ship at most every 100 ticks keeps the game log informative without per-tick spam.
     */
    private static final long PROGRESS_LOG_INTERVAL = 100L;

    /**
     * Ticks since the last progress heartbeat — a counter kept by {@link #tickShips()} (advanced once per machine
     * tick while at least one ship is in flight, reset while none is): the heartbeat is the tick on which it
     * reaches {@link #PROGRESS_LOG_INTERVAL}.
     */
    private long progressLogTicks = 0L;

    /**
     * Per-base launch countdown (the Dyson Swarm pass): ship UUID -> machine ticks until its next Satellite Rail
     * Launcher launch. Kept by {@link #tickSatelliteLauncher(VoidcraftActiveShip)}; cleared when the star burns
     * out (every mission in flight is lost).
     */
    private final java.util.Map<String, Long> satelliteLaunchCountdowns = new java.util.HashMap<>();

    /**
     * Per-base infrastructure-builder countdown (the infrastructure-builder pass): ship UUID + '#' + type ->
     * machine ticks until the base's next structure unit. Kept by
     * {@link #tickInfrastructureBuilder(VoidcraftActiveShip)}; cleared with the fleet.
     */
    private final java.util.Map<String, Long> infraBuildCountdowns = new java.util.HashMap<>();

    // endregion

    // NBT tag names (voidcraft "vc_" naming convention).
    private static final String USS_NBT_TAG = "vc_uss";
    private static final String ANIMATIONS_ENABLED_NBT_TAG = "vc_animations_enabled";
    /** Phase 4 pass 4: NBTTagList of in-flight ships (slot order); the render anchors are derived, not stored. */
    private static final String ACTIVE_SHIPS_NBT_TAG = "vc_active_ships";
    /** NBTTagList of Voidbase construction sites (anchor + per-part delivered counts). */
    private static final String BASE_SITES_NBT_TAG = "vc_uss_base_sites";
    /**
     * NBTTagList of the in-flight SEND / TAKE transfers: one entry per transfer carrying the executing ship's
     * uuid, the target ship's uuid + name, and the transfer's own state (see
     * {@link USSCargoTransfer#writeToNBT}).
     */
    private static final String CARGO_TRANSFERS_NBT_TAG = "vc_cargo_transfers";
    private static final String CARGO_TRANSFER_SRC_UUID_NBT_TAG = "vc_tr_src_uuid";
    private static final String CARGO_TRANSFER_TGT_UUID_NBT_TAG = "vc_tr_tgt_uuid";
    private static final String CARGO_TRANSFER_TGT_NAME_NBT_TAG = "vc_tr_tgt_name";
    private static final String CARGO_TRANSFER_LEG_NBT_TAG = "vc_tr_leg";
    private static final String CARGO_TRANSFER_STAR_NBT_TAG = "vc_tr_star";
    /**
     * NBTTagList of the in-flight REPAIR sessions: one entry per session carrying the executing entity's uuid,
     * the target entity's uuid, and the pacing countdown.
     */
    private static final String REPAIRS_NBT_TAG = "vc_uss_repairs";
    private static final String REPAIR_SRC_UUID_NBT_TAG = "vc_rp_src_uuid";
    private static final String REPAIR_TGT_UUID_NBT_TAG = "vc_rp_tgt_uuid";
    private static final String REPAIR_TICKS_NBT_TAG = "vc_rp_ticks";
    /**
     * NBTTagList of the in-flight STABILIZE windows: one entry per window carrying the executing base's uuid,
     * the remaining duration, the Field Generator countdown, and the last consumed Field Generator's tier (the
     * window's weight).
     */
    private static final String STABILIZES_NBT_TAG = "vc_uss_stabilizes";
    private static final String STABILIZE_SRC_UUID_NBT_TAG = "vc_st_src_uuid";
    private static final String STABILIZE_TICKS_NBT_TAG = "vc_st_ticks";
    private static final String STABILIZE_FIELD_GENERATOR_TICKS_NBT_TAG = "vc_st_fieldgen_ticks";
    private static final String STABILIZE_WEIGHT_NBT_TAG = "vc_st_weight";

    // Multiblock structure.
    /**
     * The legacy 33×33×33 EoH sphere (transposed into the structurelib layout) — the base shape for pass 12.
     */
    private static final String[][] LEGACY_EOH_SHAPE = transpose(
        new String[][] {
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "               C C               ", "            CCCCCCCCC            ",
                "               C C               ", "            CCCCCCCCC            ",
                "               C C               ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "               C C               ", "               C C               ",
                "               C C               ", "              DDDDD              ",
                "             DDCDCDD             ", "         CCCCDCCDCCDCCCC         ",
                "             DDDDDDD             ", "         CCCCDCCDCCDCCCC         ",
                "             DDCDCDD             ", "              DDDDD              ",
                "               C C               ", "               C C               ",
                "               C C               ", "               C C               ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "               C C               ", "               C C               ",
                "                D                ", "                D                ",
                "             DDDDDDD             ", "            DD     DD            ",
                "            D  EEE  D            ", "       CCC  D EAAAE D  CCC       ",
                "          DDD EAAAE DDD          ", "       CCC  D EAAAE D  CCC       ",
                "            D  EEE  D            ", "            DD     DD            ",
                "             DDDDDDD             ", "                D                ",
                "                D                ", "               C C               ",
                "               C C               ", "               C C               ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "                D                ", "                D                ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "      CC                 CC      ",
                "        DD             DD        ", "      CC                 CC      ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                D                ",
                "                D                ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "              CCCCC              ", "                D                ",
                "                A                ", "                A                ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "      C                   C      ", "     CC                   CC     ",
                "      CDAA             AADC      ", "     CC                   CC     ",
                "      C                   C      ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                A                ",
                "                A                ", "                D                ",
                "              CCCCC              ", "               C C               ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "                D                ", "             SEEAEES             ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "       S                 S       ",
                "       E                 E       ", "    CC E                 E CC    ",
                "      DA                 AD      ", "    CC E                 E CC    ",
                "       E                 E       ", "       S                 S       ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "             SEEAEES             ",
                "                D                ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "              CCCCC              ", "                D                ",
                "                A                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "    C                       C    ", "   CC                       CC   ",
                "    CDA                   ADC    ", "   CC                       CC   ",
                "    C                       C    ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                A                ", "                D                ",
                "              CCCCC              ", "               C C               ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "                D                ", "             SEEAEES             ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "     S                     S     ",
                "     E                     E     ", "  CC E                     E CC  ",
                "    DA                     AD    ", "  CC E                     E CC  ",
                "     E                     E     ", "     S                     S     ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "             SEEAEES             ",
                "                D                ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "               C C               ", "                D                ",
                "                A                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "  C                           C  ",
                "   DA                       AD   ", "  C                           C  ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                A                ", "                D                ",
                "               C C               ", "                                 ",
                "                                 " },
            { "                                 ", "               C C               ",
                "               C C               ", "                D                ",
                "                A                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", " CC                           CC ",
                "   DA                       AD   ", " CC                           CC ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                A                ", "                D                ",
                "               C C               ", "               C C               ",
                "                                 " },
            { "                                 ", "               C C               ",
                "                D                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", " C                             C ",
                "  D                           D  ", " C                             C ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                D                ", "               C C               ",
                "                                 " },
            { "                                 ", "               C C               ",
                "                D                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", " C                             C ",
                "  D                           D  ", " C                             C ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                D                ", "               C C               ",
                "                                 " },
            { "             CCCCCCC             ", "               C C               ",
                "             DDDDDDD             ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "  D                           D  ",
                "  D                           D  ", "CCD                           DCC",
                "  D                           D  ", "CCD                           DCC",
                "  D                           D  ", "  D                           D  ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "             DDDDDDD             ", "               C C               ",
                "               C C               " },
            { "            CCHHHHHCC            ", "              DDDDD              ",
                "            DD     DD            ", "                                 ",
                "                                 ", "       S                 S       ",
                "                                 ", "     S                     S     ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "  D                           D  ", "  D                           D  ",
                " D                             D ", "CD                             DC",
                " D                             D ", "CD                             DC",
                " D                             D ", "  D                           D  ",
                "  D                           D  ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "     S                     S     ",
                "                                 ", "       S                 S       ",
                "                                 ", "                                 ",
                "            DD     DD            ", "              DDDDD              ",
                "               C C               " },
            { "            CHHHHHHHC            ", "             DDCDCDD             ",
                "            D  EEE  D            ", "                                 ",
                "      C                   C      ", "       E                 E       ",
                "    C                       C    ", "     E                     E     ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "  D                           D  ", " D                             D ",
                " D                             D ", "CCE                           ECC",
                " DE                           ED ", "CCE                           ECC",
                " D                             D ", " D                             D ",
                "  D                           D  ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "     E                     E     ",
                "    C                       C    ", "       E                 E       ",
                "      C                   C      ", "                                 ",
                "            D  EEE  D            ", "             DDCDCDD             ",
                "               C C               " },
            { "            CHHCCCHHC            ", "         CCCCDCCDCCDCCCC         ",
                "       CCC  D EAAAE D  CCC       ", "      CC                 CC      ",
                "     CC                   CC     ", "    CC E                 E CC    ",
                "   CC                       CC   ", "  CC E                     E CC  ",
                "  C                           C  ", " CC                           CC ",
                " C                             C ", " C                             C ",
                "CCD                           DCC", "CD                             DC",
                "CCE                           ECC", "CCA                           ACC",
                "CDA                           ADC", "CCA                           ACC",
                "CCE                           ECC", "CD                             DC",
                "CCD                           DCC", " C                             C ",
                " C                             C ", " CC                           CC ",
                "  C                           C  ", "  CC E                     E CC  ",
                "   CC                       CC   ", "    CC E                 E CC    ",
                "     CC                   CC     ", "      CC                 CC      ",
                "       CCC  D EAAAE D  CCC       ", "         CCCCDCCDCCDCCCC         ",
                "            CCCCCCCCC            " },
            { "            CHHC~CHHC            ", "             DDDDDDD             ",
                "          DDD EAAAE DDD          ", "        DD             DD        ",
                "      CDAA             AADC      ", "      DA                 AD      ",
                "    CDA                   ADC    ", "    DA                     AD    ",
                "   DA                       AD   ", "   DA                       AD   ",
                "  D                           D  ", "  D                           D  ",
                "  D                           D  ", " D                             D ",
                " DE                           ED ", "CDA                           ADC",
                " DA                           AD ", "CDA                           ADC",
                " DE                           ED ", " D                             D ",
                "  D                           D  ", "  D                           D  ",
                "  D                           D  ", "   DA                       AD   ",
                "   DA                       AD   ", "    DA                     AD    ",
                "    CDA                   ADC    ", "      DA                 AD      ",
                "      CDAA             AADC      ", "        DD             DD        ",
                "          DDD EAAAE DDD          ", "             DDDDDDD             ",
                "               C C               " },
            { "            CHHCCCHHC            ", "         CCCCDCCDCCDCCCC         ",
                "       CCC  D EAAAE D  CCC       ", "      CC                 CC      ",
                "     CC                   CC     ", "    CC E                 E CC    ",
                "   CC                       CC   ", "  CC E                     E CC  ",
                "  C                           C  ", " CC                           CC ",
                " C                             C ", " C                             C ",
                "CCD                           DCC", "CD                             DC",
                "CCE                           ECC", "CCA                           ACC",
                "CDA                           ADC", "CCA                           ACC",
                "CCE                           ECC", "CD                             DC",
                "CCD                           DCC", " C                             C ",
                " C                             C ", " CC                           CC ",
                "  C                           C  ", "  CC E                     E CC  ",
                "   CC                       CC   ", "    CC E                 E CC    ",
                "     CC                   CC     ", "      CC                 CC      ",
                "       CCC  D EAAAE D  CCC       ", "         CCCCDCCDCCDCCCC         ",
                "            CCCCCCCCC            " },
            { "            CHHHHHHHC            ", "             DDCDCDD             ",
                "            D  EEE  D            ", "                                 ",
                "      C                   C      ", "       E                 E       ",
                "    C                       C    ", "     E                     E     ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "  D                           D  ", " D                             D ",
                " D                             D ", "CCE                           ECC",
                " DE                           ED ", "CCE                           ECC",
                " D                             D ", " D                             D ",
                "  D                           D  ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "     E                     E     ",
                "    C                       C    ", "       E                 E       ",
                "      C                   C      ", "                                 ",
                "            D  EEE  D            ", "             DDCDCDD             ",
                "               C C               " },
            { "            CCHHHHHCC            ", "              DDDDD              ",
                "            DD     DD            ", "                                 ",
                "                                 ", "       S                 S       ",
                "                                 ", "     S                     S     ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "  D                           D  ", "  D                           D  ",
                " D                             D ", "CD                             DC",
                " D                             D ", "CD                             DC",
                " D                             D ", "  D                           D  ",
                "  D                           D  ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "     S                     S     ",
                "                                 ", "       S                 S       ",
                "                                 ", "                                 ",
                "            DD     DD            ", "              DDDDD              ",
                "               C C               " },
            { "             CCCCCCC             ", "               C C               ",
                "             DDDDDDD             ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "  D                           D  ",
                "  D                           D  ", "CCD                           DCC",
                "  D                           D  ", "CCD                           DCC",
                "  D                           D  ", "  D                           D  ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "             DDDDDDD             ", "               C C               ",
                "               C C               " },
            { "                                 ", "               C C               ",
                "                D                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", " C                             C ",
                "  D                           D  ", " C                             C ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                D                ", "               C C               ",
                "                                 " },
            { "                                 ", "               C C               ",
                "                D                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", " C                             C ",
                "  D                           D  ", " C                             C ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                D                ", "               C C               ",
                "                                 " },
            { "                                 ", "               C C               ",
                "               C C               ", "                D                ",
                "                A                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", " CC                           CC ",
                "   DA                       AD   ", " CC                           CC ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                A                ", "                D                ",
                "               C C               ", "               C C               ",
                "                                 " },
            { "                                 ", "                                 ",
                "               C C               ", "                D                ",
                "                A                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "  C                           C  ",
                "   DA                       AD   ", "  C                           C  ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                A                ", "                D                ",
                "               C C               ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "                D                ", "             SEEAEES             ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "     S                     S     ",
                "     E                     E     ", "  CC E                     E CC  ",
                "    DA                     AD    ", "  CC E                     E CC  ",
                "     E                     E     ", "     S                     S     ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "             SEEAEES             ",
                "                D                ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "              CCCCC              ", "                D                ",
                "                A                ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "    C                       C    ", "   CC                       CC   ",
                "    CDA                   ADC    ", "   CC                       CC   ",
                "    C                       C    ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                A                ", "                D                ",
                "              CCCCC              ", "               C C               ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "                D                ", "             SEEAEES             ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "       S                 S       ",
                "       E                 E       ", "    CC E                 E CC    ",
                "      DA                 AD      ", "    CC E                 E CC    ",
                "       E                 E       ", "       S                 S       ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "             SEEAEES             ",
                "                D                ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "              CCCCC              ", "                D                ",
                "                A                ", "                A                ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "      C                   C      ", "     CC                   CC     ",
                "      CDAA             AADC      ", "     CC                   CC     ",
                "      C                   C      ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                A                ",
                "                A                ", "                D                ",
                "              CCCCC              ", "               C C               ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "                D                ", "                D                ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "      CC                 CC      ",
                "        DD             DD        ", "      CC                 CC      ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                D                ",
                "                D                ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "               C C               ", "               C C               ",
                "                D                ", "                D                ",
                "             DDDDDDD             ", "            DD     DD            ",
                "            D  EEE  D            ", "       CCC  D EAAAE D  CCC       ",
                "          DDD EAAAE DDD          ", "       CCC  D EAAAE D  CCC       ",
                "            D  EEE  D            ", "            DD     DD            ",
                "             DDDDDDD             ", "                D                ",
                "                D                ", "               C C               ",
                "               C C               ", "               C C               ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "               C C               ",
                "               C C               ", "               C C               ",
                "               C C               ", "              DDDDD              ",
                "             DDCDCDD             ", "         CCCCDCCDCCDCCCC         ",
                "             DDDDDDD             ", "         CCCCDCCDCCDCCCC         ",
                "             DDCDCDD             ", "              DDDDD              ",
                "               C C               ", "               C C               ",
                "               C C               ", "               C C               ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " },
            { "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "               C C               ", "               C C               ",
                "               C C               ", "            CCCCCCCCC            ",
                "               C C               ", "            CCCCCCCCC            ",
                "               C C               ", "               C C               ",
                "               C C               ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 ", "                                 ",
                "                                 " } });

    /**
     * Pass 12 (user: "the old EoH structure is a bit small for the increased scope of Voidcraft — 2× bigger
     * radius"): the Voidcraft structure — the legacy sphere scaled uniformly 2× about its center (65×65×65). Same
     * shape and block composition — every block simply moves to 2× its distance from the sphere center, so the
     * per-type counts are unchanged — and the `~` controller slot lands at (32,32,0), the new anchor.
     */
    private static final String[][] STRUCTURE_SHAPE = scaleShape2x(LEGACY_EOH_SHAPE);

    private static final IStructureDefinition<MTEUnstableSolarSystem> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEUnstableSolarSystem>builder()
        .addShape(STRUCTURE_PIECE_MAIN, STRUCTURE_SHAPE)
        .addElement(
            'A',
            GTStructureChannels.EOH_COMPRESSION.use(
                ofBlocksTiered(
                    (block, meta) -> block == TTCasingsContainer.SpacetimeCompressionFieldGenerators ? meta : null,
                    ImmutableList.of(
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 0),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 1),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 2),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 3),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 4),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 5),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 6),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 7),
                        Pair.of(TTCasingsContainer.SpacetimeCompressionFieldGenerators, 8)),
                    -1,
                    (t, meta) -> t.spacetimeCompressionFieldMetadata = meta,
                    t -> t.spacetimeCompressionFieldMetadata)))
        .addElement(
            'S',
            GTStructureChannels.EOH_STABILISATION.use(
                ofBlocksTiered(
                    (block, meta) -> block == TTCasingsContainer.StabilisationFieldGenerators ? meta : null,
                    ImmutableList.of(
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 0),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 1),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 2),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 3),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 4),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 5),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 6),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 7),
                        Pair.of(TTCasingsContainer.StabilisationFieldGenerators, 8)),
                    -1,
                    (t, meta) -> t.stabilisationFieldMetadata = meta,
                    t -> t.stabilisationFieldMetadata)))
        .addElement('C', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 11))
        .addElement('D', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 10))
        .addElement(
            'H',
            buildHatchAdder(MTEUnstableSolarSystem.class)
                .atLeast(InputBus, InputHatch, InputHatch, OutputBus, OutputHatch)
                .casingIndex(Casings.InfiniteSpacetimeEnergyBoundaryCasing.getTextureId())
                .hint(1)
                .buildAndChain(Casings.InfiniteSpacetimeEnergyBoundaryCasing.asElement()))
        .addElement(
            'E',
            GTStructureChannels.EOH_DILATION.use(
                ofBlocksTiered(
                    (block, meta) -> block == TTCasingsContainer.TimeAccelerationFieldGenerator ? meta : null,
                    ImmutableList.of(
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 0),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 1),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 2),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 3),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 4),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 5),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 6),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 7),
                        Pair.of(TTCasingsContainer.TimeAccelerationFieldGenerator, 8)),
                    -1,
                    (t, meta) -> t.timeAccelerationFieldMetadata = meta,
                    t -> t.timeAccelerationFieldMetadata)))
        .build();

    /**
     * Pass 12 (user: "2x bigger radius"): uniform 2x point-scale of the legacy 33x33x33 shape about its center.
     *
     * <p>
     * Every non-space character at (i, j, k) is re-placed at (2i, 2j, 2k) of the output 65x65x65 grid: the shape
     * center maps 16 -&gt; 32, the {@code ~} controller slot maps (16,16,0) -&gt; (32,32,0), and every per-type block
     * count is unchanged - this is a point-scale (same one-block-thick shell, twice the radius), not a thickening.
     * The output grid is space-filled; the scaling commutes with {@code transpose}, so the legacy literal stays
     * readable and only the derived constant grows.
     */
    private static String[][] scaleShape2x(String[][] shape) {
        final int n = shape.length;
        if (n != 33) {
            throw new IllegalArgumentException("scaleShape2x expects a 33x33x33 shape, got " + n + "x...");
        }
        // A 33-cell axis (indices 0–32) scales to indices 0–64 = 65 cells, NOT 66 (2·33 would add a phantom space
        // slice).
        final int m = 2 * n - 1;
        final char[][][] grid = new char[m][m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                Arrays.fill(grid[i][j], ' ');
            }
        }
        for (int i = 0; i < n; i++) {
            final String[] rows = shape[i];
            if (rows.length != n) {
                throw new IllegalArgumentException(
                    "scaleShape2x expects 33x33x33, got " + n + "x" + rows.length + "x...");
            }
            for (int j = 0; j < n; j++) {
                final String row = rows[j];
                if (row.length() != n) {
                    throw new IllegalArgumentException("scaleShape2x expects 33-char rows, got " + row.length());
                }
                for (int k = 0; k < n; k++) {
                    final char ch = row.charAt(k);
                    if (ch != ' ') {
                        grid[2 * i][2 * j][2 * k] = ch;
                    }
                }
            }
        }
        final String[][] out = new String[m][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < m; j++) {
                out[i][j] = new String(grid[i][j]);
            }
        }
        return out;
    }

    // Region machine logic.

    @Override
    public IStructureDefinition<MTEUnstableSolarSystem> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    public MTEUnstableSolarSystem(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEUnstableSolarSystem(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEUnstableSolarSystem(mName);
    }

    /**
     * Same structural rules as the legacy EoH (copied verbatim): 32,32,0 anchor (pass 12: the legacy 16,16,0 scaled
     * 2× with the 65×65×65 shape), no CRIb, no energy hatches, exactly 1 non-stocking input bus, 2 non-stocking
     * input hatches, 1 output bus, 1 output hatch.
     */
    @Override
    public void checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack,
        List<StructureError> errors) {
        spacetimeCompressionFieldMetadata = -1;
        timeAccelerationFieldMetadata = -1;
        stabilisationFieldMetadata = -1;

        // Check structure of multi.
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 32, 32, 0, errors)) return;

        // Make sure there are no Crafting Input Buffers/Buses/Slaves.
        if (!mDualInputHatches.isEmpty()) {
            errors.add(StructureErrors.of("GT5U.gui.text.structure_error.crib_not_allowed"));
        }

        // Make sure there are no energy hatches (the Phase 6 energy model uses the global energy map, not hatches).
        if (!mEnergyHatches.isEmpty() || !mExoticEnergyHatches.isEmpty()) {
            errors.add(StructureErrorRegistry.NO_ENERGY_HATCH_NEEDED);
        }

        // Check there is 1 input bus, and it is not a stocking input bus.
        {
            if (mInputBusses.size() != 1) {
                errors.add(StructureErrors.hatchCount(ErrorType.NOT_MATCH, InputBus, mInputBusses.size(), 1));
            } else if (mInputBusses.get(0) instanceof MTEHatchInputBusME) {
                errors.add(StructureErrors.of("GT5U.gui.text.structure_error.stocking_input_bus_not_allowed"));
            }
        }

        if (mInputHatches.size() != 2) {
            errors.add(StructureErrors.hatchCount(ErrorType.NOT_MATCH, InputHatch, mInputHatches.size(), 2));
        }
        for (MTEHatchInput inputHatch : mInputHatches) {
            if (inputHatch instanceof MTEHatchInputME) {
                errors.add(StructureErrors.of("GT5U.gui.text.structure_error.stocking_input_hatch_not_allowed"));
                break;
            }
        }
        checkOneOutputBus(errors);
        checkOneOutputHatch(errors);
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        animationsEnabled = !animationsEnabled;
        if (uss != null && uss.isIgnited()) {
            if (animationsEnabled) {
                createRenderBlock(uss.getTier());
            } else {
                destroyRenderBlock();
            }
        }
        GTUtility.sendChatTrans(aPlayer, "GT5U.machines.animations." + (animationsEnabled ? "enabled" : "disabled"));
    }

    /**
     * Right-click handling: a registered debug item applies its effect to the machine; otherwise insert the USS
     * Controller into the empty controller slot (sneak or plain right-click with the item in hand); otherwise the
     * machine UI.
     */
    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        // A registered debug item applies its effect to the machine instead of opening the UI (server side —
        // the star state is server-authoritative).
        VoidcraftDebugEffectRegistry.Effect debugEffect = VoidcraftDebugEffectRegistry.effectFor(aPlayer.getHeldItem());
        if (debugEffect != null) {
            if (aBaseMetaTileEntity.isServerSide()) {
                debugEffect.apply(this, aPlayer);
            }
            return true;
        }
        if (getControllerSlot() == null) {
            ItemStack heldItem = aPlayer.getHeldItem();
            if (heldItem != null && heldItem.getItem() == ItemUSSController.INSTANCE) {
                mInventory[getControllerSlotIndex()] = heldItem.copy();
                mInventory[getControllerSlotIndex()].stackSize = 1;
                aPlayer.setCurrentItemOrArmor(0, ItemUtils.depleteStack(heldItem, 1));
                return true;
            }
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public CheckRecipeResult checkProcessing_EM() {
        // Not a recipe machine — the star lifecycle runs in onPostTick. (NO_RECIPE = normal idle.)
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    // Region star state machine.

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) return;
        cleanupLegacyShipRender();
        if (!mMachine) return;
        if (uss == null) {
            uss = VoidcraftUSS.cold();
        }
        if (uss.isIgnited()) {
            // The virtual orbit clock (the orbit time base, server and client): +1 per tick normally, proportionally
            // more during a stellar-acceleration second — the acceleration's first/last second ramp the rate up /
            // down (fractional per-tick advance, accumulated into the whole-tick clock).
            orbitFractionalAccumulator += USSStellarEvolution.orbitAdvanceSmoothed(
                USSStellarEvolution.orbitAdvancePerTick(lastAccelerationSecondMB),
                accelPhase,
                accelerationTicks / (double) USSConstants.ACCELERATION_INTERVAL_TICKS);
            long orbitAdvance = (long) orbitFractionalAccumulator;
            orbitFractionalAccumulator -= orbitAdvance;
            uss = uss.withVirtualTime(uss.getVirtualTime() + orbitAdvance);
            tickStellarAcceleration();
            // The star burns: one machine tick of lifespan per tick (no power draw yet — Phase 6).
            long remaining = uss.getLifespanRemaining() - 1;
            if (remaining <= 0) {
                starExpires();
            } else {
                uss = uss.withLifespan(remaining);
                tickStarInfrastructure();
                tickInjector();
                tickShips();
                // The client extrapolates the orbit clock at the normal rate from the last sync — re-sync every
                // tick while the clock runs faster than the world (an active stellar-acceleration second).
                if (lastAccelerationSecondMB > 0L) {
                    syncOrbitClock();
                }
            }
            reapplyStarRenderState();
        } else if (getControllerSlot() != null) {
            // COLD + controller in slot + structure valid → ignite.
            igniteStar();
        }
    }

    /**
     * Stellar acceleration (every machine tick while the star is ignited): every
     * {@link USSConstants#ACCELERATION_INTERVAL_TICKS} ticks drain ALL the Tachyon Rich Temporal Fluid out of the
     * input hatches. A non-empty second shortens the star's lifespan by the square root of the drained amount
     * (minimum 1 tick) and wipes the entire fleet — ships AND voidbases ({@link #discardAllShips()}); the star, the
     * planets and the infrastructure registrations persist, and the virtual orbit clock runs at the proportional
     * rate during that second.
     */
    private void tickStellarAcceleration() {
        if (++accelerationTicks < USSConstants.ACCELERATION_INTERVAL_TICKS) {
            return;
        }
        accelerationTicks = 0;
        long consumed = drainTachyonFluid();
        lastAccelerationSecondMB = consumed;
        if (consumed <= 0L) {
            accelPhase = USSStellarEvolution.AccelerationPhase.IDLE;
            return;
        }
        int lost = activeShips.size();
        long reduction = USSStellarEvolution.lifespanReductionPerSecond(consumed);
        uss = uss.withLifespan(uss.getLifespanRemaining() - reduction);
        // Orbit-clock phase of the window this drain starts (its rate applies from the next tick): ramp up on the
        // first acceleration second, ramp down on the last — the one the star's lifespan runs out within.
        if (!accelerationActive) {
            accelerationActive = true;
            accelPhase = USSStellarEvolution.AccelerationPhase.RAMP_UP;
        } else if (uss.getLifespanRemaining() <= USSConstants.ACCELERATION_INTERVAL_TICKS + reduction) {
            accelPhase = USSStellarEvolution.AccelerationPhase.RAMP_DOWN;
        } else {
            accelPhase = USSStellarEvolution.AccelerationPhase.FULL;
        }
        discardAllShips();
        if (lost > 0) {
            try {
                LOGGER.info(
                    "[Voidcraft] USS stellar acceleration: {} mB tachyon fluid consumed, lifespan -{} ticks, {} ship(s) wiped",
                    consumed,
                    reduction,
                    lost);
            } catch (Throwable ignored) {}
        }
    }

    /**
     * Drain ALL the Tachyon Rich Temporal Fluid out of the input hatches (the acceleration feed — the same
     * input-hatch drain pattern as the Gateway's launch fuel).
     *
     * @return the mB drained
     */
    private long drainTachyonFluid() {
        Fluid tachyon = Materials.Time.getMolten(1)
            .getFluid();
        long consumed = 0L;
        for (MTEHatchInput hatch : GTUtility.validMTEList(mInputHatches)) {
            FluidStack held = hatch.getFluid();
            if (held == null || held.getFluid() != tachyon) {
                continue;
            }
            FluidStack drained = hatch.drain(held.amount, true);
            if (drained != null) {
                consumed += drained.amount;
            }
        }
        return consumed;
    }

    /**
     * Ignite the star with the star class carried by the controller item in the controller slot (Phase 4 pass 1 —
     * the ITEM decides the star type; the structure's tier sets the star's rendering size, and the star type
     * decides the planet system a Miner can work). An unrecognized item in the slot leaves the star cold and keeps
     * the item.
     */
    private void igniteStar() {
        USSStarType starType = ItemUSSController.starTypeOf(getControllerSlot());
        if (starType == null) {
            return; // not a controller — the star stays COLD, the slot keeps whatever is in it
        }
        int tier = USSConstants.clampTier(spacetimeCompressionFieldMetadata);
        uss = VoidcraftUSS.ignite(tier, starType, System.currentTimeMillis());
        // The virtual orbit clock starts at the current world tick: the planet phases stay continuous with the
        // old world-time clock (acceleration only ever ADDS extra ticks on top), and the client's extrapolation
        // (synced clock + world ticks since the sync) stays exact.
        IGregTechTileEntity igniteBase = getBaseMetaTileEntity();
        if (igniteBase != null && igniteBase.getWorld() != null) {
            uss = uss.withVirtualTime(
                igniteBase.getWorld()
                    .getTotalWorldTime());
        }
        resetAccelerationCycle();
        if (animationsEnabled) {
            createRenderBlock(tier);
        }
    }

    /**
     * The star's lifespan has expired — the expiry pipeline: the system's primary fraction, size factor and the
     * infrastructure BUILT around the targets decide the OUTCOME (the star evolves to a new class and auto-ignites
     * on the same controller, upgraded — or the system terminates: controller consumed, star cold); the Spacetime
     * yield is paid out with the Universium conversion on the scanned ripples. Both paths wipe the fleet and the
     * system's internal state (the model swap — the injector buffer, the star size, the Dyson count, the scanned
     * ripples — is gone with the old system).
     */
    private void starExpires() {
        USSStarType expiring = uss.getStarType();
        // Capabilities read from what is BUILT (a fully complete shell), not what the bases merely carry: the
        // expiry conversion counts the active scanned ripples that carry a finished Stabilizer shell, the Lens
        // when the star's Lens shell is complete.
        double renderSize = starShellRenderSize();
        USSInfrastructure infra = uss.getInfrastructure();
        int ripples = USSStellarEvolution.activeScannedRipples(getRippleField(), uss.getScannedRipples());
        int stabilizedRipples = USSInfraBuild
            .builtOnActiveRipples(getRippleField(), uss.getScannedRipples(), infra, USSInfraBuild.STABILIZER);
        boolean lensPresent = USSInfraBuild.isBuilt(
            infra,
            USSInfraBuild.LENS,
            USSInfraBuild.TARGET_STAR,
            -1,
            USSInfraBuild.starCapacity(USSInfraBuild.LENS, renderSize));

        long yield = USSConstants.spacetimeYieldForType(expiring);
        long litres = USSStellarEvolution.universiumLiters(yield, stabilizedRipples, ripples);
        long consumed = USSStellarEvolution.spacetimeConsumedMB(yield, stabilizedRipples, ripples);
        long universiumMB = USSStellarEvolution
            .universiumOutputMB(litres, USSStellarEvolution.matrixMultiplier(stabilizeWeightSum()));
        Optional<USSStarType> outcome = USSStellarEvolution
            .resolve(expiring, uss.primaryFraction(), uss.sizeFactor(), lensPresent, new Random());

        int lost = activeShips.size();
        discardAllShips();

        long virtualTime = uss.getVirtualTime();
        if (outcome.isPresent()) {
            uss = VoidcraftUSS.ignite(spacetimeCompressionFieldMetadata, outcome.get(), System.currentTimeMillis())
                .withVirtualTime(virtualTime);
            upgradeController(outcome.get());
            resetAccelerationCycle();
            if (animationsEnabled) {
                createRenderBlock(USSConstants.clampTier(spacetimeCompressionFieldMetadata));
            }
        } else {
            uss = uss.toCold();
            resetAccelerationCycle();
            destroyRenderBlock();
            if (mInventory[getControllerSlotIndex()] != null) {
                mInventory[getControllerSlotIndex()] = null;
            }
            updateSlots();
        }

        List<FluidStack> outputs = new ArrayList<FluidStack>();
        long spacetimeOut = yield - consumed;
        if (spacetimeOut > 0L) {
            outputs.add(Materials.SpaceTime.getMolten(spacetimeOut));
        }
        if (universiumMB > 0L) {
            outputs.add(Materials.Universium.getMolten(universiumMB));
        }
        if (!outputs.isEmpty()) {
            addFluidOutputs(outputs.toArray(new FluidStack[0]));
        }

        try {
            LOGGER.info(
                "[Voidcraft] USS star expiry: {} -> {} ({} ship(s) wiped, {} mB spacetime + {} mB universium yielded)",
                expiring,
                outcome.map(USSStarType::name)
                    .orElse("terminal"),
                lost,
                Math.max(0L, spacetimeOut),
                universiumMB);
        } catch (Throwable ignored) {}
    }

    /**
     * Upgrade the controller in the controller slot to the given star class (the star's evolutions continue on
     * the same controller item — only the terminal death consumes it).
     */
    private void upgradeController(USSStarType to) {
        ItemStack stack = mInventory[getControllerSlotIndex()];
        if (stack != null && stack.getItem() == ItemUSSController.INSTANCE) {
            stack.setItemDamage(to.ordinal());
        }
        updateSlots();
    }

    /**
     * How many of the given station infrastructure component the STANDING bases carry — the capability the bases
     * contribute to the system (the in-world component structures are dormant; the base's blueprint is the record).
     */
    private long infrastructureCount(VoidcraftComponent component) {
        long count = 0L;
        for (VoidcraftActiveShip ship : activeShips) {
            if (ship == null || !ship.isBase()) {
                continue;
            }
            VoidcraftBlueprint blueprint = VoidcraftNbt.readBase(ship.getPayload());
            if (blueprint != null) {
                count += blueprint.count(component);
            }
        }
        return count;
    }

    @Override
    public void stopMachine(@Nonnull ShutDownReason reason) {
        super.stopMachine(reason);
        if (uss != null && uss.isIgnited()) {
            uss = uss.toCold();
        }
        discardAllShips();
        resetAccelerationCycle();
        destroyRenderBlock();
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
        discardAllShips();
        resetAccelerationCycle();
        destroyRenderBlock();
    }

    /** Reset the per-second stellar-acceleration bookkeeping (new star life / star death / machine stop / teardown). */
    private void resetAccelerationCycle() {
        accelerationTicks = 0L;
        lastAccelerationSecondMB = 0L;
        accelPhase = USSStellarEvolution.AccelerationPhase.IDLE;
        accelerationActive = false;
        orbitFractionalAccumulator = 0.0;
    }

    // endregion

    /**
     * Place the render block 32 blocks behind the controller (pass 12: the legacy 16-block star offset scaled 2×
     * with the 65×65×65 shape). The shared {@link TileEntityEyeOfHarmony} render TE is used as-is, configured for
     * the star tier AND for the doubled space-shell radius (legacy EoH keeps the 12.95 default).
     */
    private void createRenderBlock(int tier) {
        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();
        if (gregTechTileEntity == null) return;

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        double xOffset = 32 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        double zOffset = 32 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        double yOffset = 32 * getExtendedFacing().getRelativeBackInWorld().offsetY;

        gregTechTileEntity.getWorld()
            .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), Blocks.air);
        gregTechTileEntity.getWorld()
            .setBlock(
                (int) (x + xOffset),
                (int) (y + yOffset),
                (int) (z + zOffset),
                TTCasingsContainer.eyeOfHarmonyRenderBlock);
        TileEntityEyeOfHarmony rendererTileEntity = (TileEntityEyeOfHarmony) gregTechTileEntity.getWorld()
            .getTileEntity((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset));

        if (rendererTileEntity != null) {
            rendererTileEntity.setTier(tier);
            // Star size: the USS's current size rendered ((2/3)·√(size) — see VoidcraftUSS.starSize).
            rendererTileEntity.setStarSize(USSPlanets.starRenderSize(uss.getStarSize()));
            // Star color: from the star's registered definition (null → the legacy orange fallback) — the shared
            // star mesh is a single texture, so the colors are what distinguish the star classes visually.
            rendererTileEntity.setStarColor(USSStarColor.colorFor(USSStarRegistry.byType(uss.getStarType())));
            rendererTileEntity.setStarShellColor(USSStarColor.shellColorFor(USSStarRegistry.byType(uss.getStarType())));
            // Halo stars (the near-black cores — black dwarf, black hole, gravastar): their shell layers render
            // outside-in as a glow ring — the flag rides the description packet like the colors so chunk reloads
            // keep it.
            rendererTileEntity.setStarHalo(USSStarColor.isHaloStar(uss.getStarType()));
            // Custom render treatment (the magnetar's magnetic field loops): the extra geometry the renderer draws
            // on top of the standard star body — rides the description packet like the halo flag so chunk reloads
            // keep it.
            rendererTileEntity.setStarRenderType(USSStarColor.renderTypeFor(USSStarRegistry.byType(uss.getStarType())));
            // Pass 12: the Voidcraft structure is 2× the legacy radius, so the space shell doubles with it
            // (star and planet sizes stay unchanged).
            rendererTileEntity.setDomeRadius(USSConstants.SPACE_SHELL_RADIUS);
            // Phase 4 pass 3: the system's PLANETS — deterministic (star type + ignition timestamp), so the legacy
            // orbit renderer draws exactly the bodies the miner works (see getPlanets / USSPlanets).
            rendererTileEntity.setPlanets(planetSpecsFor(getPlanets()));
            // Seed the virtual orbit clock so the client's first frame runs on it (0 virtual time at ignition).
            rendererTileEntity.setUssOrbitTime(
                uss.getVirtualTime(),
                gregTechTileEntity.getWorld()
                    .getTotalWorldTime());
        }
        // Push the TE data (tier/size/planets) to the client now — in 1.7.10 nothing else does until a chunk reload
        // (markBlockForUpdate sends the description packet to nearby players; see
        // TileEntityVoidcraftShip.syncToClient).
        int sx = (int) (x + xOffset);
        int sy = (int) (y + yOffset);
        int sz = (int) (z + zOffset);
        gregTechTileEntity.getWorld()
            .markBlockForUpdate(sx, sy, sz);
    }

    /**
     * Re-assert the star render block's full client state (tier, rendered size, the star class's color/shell/halo/
     * render treatment, dome radius, planet specs, orbit clock) and push the description packet to the chunk's
     * watchers. The client render TE's star class and planets have no other source than the description packet —
     * createRenderBlock's push is one-shot, so a client TE that misses it renders the default star until the next
     * re-assert converges it.
     */
    private void reapplyStarRenderState() {
        IGregTechTileEntity mte = getBaseMetaTileEntity();
        if (mte == null || !animationsEnabled || uss == null || !uss.isIgnited()) {
            return;
        }
        int x = mte.getXCoord();
        int y = mte.getYCoord();
        int z = mte.getZCoord();
        double xOffset = 32 * getExtendedFacing().getRelativeBackInWorld().offsetX;
        double yOffset = 32 * getExtendedFacing().getRelativeBackInWorld().offsetY;
        double zOffset = 32 * getExtendedFacing().getRelativeBackInWorld().offsetZ;
        int sx = (int) (x + xOffset);
        int sy = (int) (y + yOffset);
        int sz = (int) (z + zOffset);
        TileEntityEyeOfHarmony te = (TileEntityEyeOfHarmony) mte.getWorld()
            .getTileEntity(sx, sy, sz);
        if (te == null) {
            return;
        }
        te.setTier(USSConstants.clampTier(spacetimeCompressionFieldMetadata));
        te.setStarSize(USSPlanets.starRenderSize(uss.getStarSize()));
        te.setStarColor(USSStarColor.colorFor(USSStarRegistry.byType(uss.getStarType())));
        te.setStarShellColor(USSStarColor.shellColorFor(USSStarRegistry.byType(uss.getStarType())));
        te.setStarHalo(USSStarColor.isHaloStar(uss.getStarType()));
        te.setStarRenderType(USSStarColor.renderTypeFor(USSStarRegistry.byType(uss.getStarType())));
        te.setDomeRadius(USSConstants.SPACE_SHELL_RADIUS);
        te.setPlanets(planetSpecsFor(getPlanets()));
        te.setUssOrbitTime(
            uss.getVirtualTime(),
            mte.getWorld()
                .getTotalWorldTime());
        mte.getWorld()
            .markBlockForUpdate(sx, sy, sz);
    }

    /**
     * Convert the system's planets into render specs (hologram texture + orbit parameters + fallback tint + ring
     * texture) for the shared EoH render TE. The texture is the planet's bundled {@code stitched.png} (the renderer
     * binds it and draws the shared cube); the ring texture is the planet's orbit-ring image when it has one
     * (empty otherwise). The color from {@link USSPlanetColor} is the tinted-sphere fallback tint if a texture is
     * missing. The specs keep the exact orbit math the ship hover/beam track (USSFleetOrbit).
     */
    private static List<TileEntityEyeOfHarmony.PlanetSpec> planetSpecsFor(List<USSPlanets.USSPlanet> planets) {
        List<TileEntityEyeOfHarmony.PlanetSpec> specs = new ArrayList<>();
        for (USSPlanets.USSPlanet planet : planets) {
            final String ringTexture = planet.hasRing && planet.ringVariant > 0
                ? "textures/uss/rings/" + planet.definition.getTier()
                    .ringDir() + "/" + planet.ringVariant + ".png"
                : "";
            specs.add(
                new TileEntityEyeOfHarmony.PlanetSpec(
                    planet.definition.getTexture(),
                    (float) planet.distance,
                    (float) planet.scale,
                    (float) planet.orbitSpeed,
                    (float) planet.rotationSpeed,
                    (float) planet.xAngle,
                    (float) planet.zAngle,
                    USSPlanetColor.colorFor(planet.definition),
                    ringTexture));
        }
        return specs;
    }

    /**
     * Remove the render block (if present) at the standard offset — pass 12: both the new 32-block offset and the
     * legacy 16-block offset (one-time cleanup of render blocks left by pre-pass-12 33×33×33 builds).
     */
    private void destroyRenderBlock() {
        IGregTechTileEntity gregTechTileEntity = this.getBaseMetaTileEntity();
        if (gregTechTileEntity == null) return;

        int x = gregTechTileEntity.getXCoord();
        int y = gregTechTileEntity.getYCoord();
        int z = gregTechTileEntity.getZCoord();

        for (int offset : new int[] { 16, 32 }) {
            double xOffset = offset * getExtendedFacing().getRelativeBackInWorld().offsetX;
            double zOffset = offset * getExtendedFacing().getRelativeBackInWorld().offsetZ;
            double yOffset = offset * getExtendedFacing().getRelativeBackInWorld().offsetY;

            gregTechTileEntity.getWorld()
                .setBlock((int) (x + xOffset), (int) (y + yOffset), (int) (z + zOffset), Blocks.air);
        }
        // The gateways belong to the dome: without it there is nothing for them to sit in (they re-register on the
        // gateways' next launch-target scan once the star is ignited again).
        gatewayBlocks.clear();
        syncFleetRenderBlock();
    }

    // Region mining mission (Phase 3).

    /**
     * @return true if the star is currently ignited (the gateway's radius scan uses this).
     */
    public boolean isStarIgnited() {
        return uss != null && uss.isIgnited();
    }

    /** @return true if at least one ship is currently in flight. */
    public boolean hasActiveShip() {
        return !activeShips.isEmpty();
    }

    /**
     * @return true when the USS can accept another launch (Phase 4 pass 4 — up to
     *         {@code USSConstants.MAX_SHIPS_PER_USS} ships fly simultaneously). The gateway's radius scan skips
     *         FULL systems, so a rejected launch (which would already have pulled its cargo from the inputs) can
     *         only happen on a capacity race, not on the normal "already busy" path.
     */
    public boolean hasFreeShipSlot() {
        return activeShips.size() < USSConstants.MAX_SHIPS_PER_USS;
    }

    /** @return the number of ships currently in flight (0…{@code MAX_SHIPS_PER_USS}). */
    public int getActiveShipCount() {
        return activeShips.size();
    }

    /** @return the ships in flight, in launch order (read-only). */
    public List<VoidcraftActiveShip> getActiveShips() {
        return Collections.unmodifiableList(activeShips);
    }

    /** @return the Voidbase construction sites (read-only). */
    public List<USSBaseSite> getBaseSites() {
        return Collections.unmodifiableList(baseSites);
    }

    /** @return the construction site at the given anchor, or null when there is no site there yet. */
    public USSBaseSite getBaseSite(USSBaseAnchor anchor) {
        if (anchor == null) {
            return null;
        }
        for (USSBaseSite site : baseSites) {
            if (site.anchor()
                .equals(anchor)) {
                return site;
            }
        }
        return null;
    }

    /**
     * Create (or reuse) the construction site at the anchor from the mission blueprint's parts needs (one site
     * per anchor). The site carries the FULL requirement — the CONSTRUCT leg credits it part by part from the
     * constructor's hold.
     *
     * @return the site (freshly created, or the existing one)
     */
    public USSBaseSite createOrGetBaseSite(USSBaseAnchor anchor, VoidcraftBlueprint blueprint, String name) {
        USSBaseSite site = getBaseSite(anchor);
        if (site != null) {
            return site;
        }
        site = USSBaseSite.create(anchor, name, blueprint, System.currentTimeMillis());
        baseSites.add(site);
        syncFleetRenderBlock();
        return site;
    }

    /** Remove a completed site (the base spawned in its place). */
    public void completeBaseSite(USSBaseAnchor anchor) {
        baseSites.removeIf(
            site -> site.anchor()
                .equals(anchor));
        syncFleetRenderBlock();
    }

    /** @return the base standing at the given anchor (one base per anchor), or null. */
    public VoidcraftActiveShip getBase(USSBaseAnchor anchor) {
        if (anchor == null) {
            return null;
        }
        for (VoidcraftActiveShip entity : activeShips) {
            if (entity.getAnchor() != null && entity.getAnchor()
                .equals(anchor)) {
                return entity;
            }
        }
        return null;
    }

    /**
     * Join a completed Voidbase to the fleet at its anchor: the unified entity (anchor set, speed 0) enters
     * {@link #activeShips} — the fleet cap counts it — with a pilot running its station program (the digitized
     * controller program), and its construction site is removed. No-op when a base already stands there or the
     * fleet is full.
     *
     * @param entity the base entity (created via {@link VoidcraftActiveShip#spawnBase})
     */
    public void spawnBase(VoidcraftActiveShip entity) {
        if (entity == null || entity.getAnchor() == null
            || getBase(entity.getAnchor()) != null
            || activeShips.size() >= USSConstants.MAX_SHIPS_PER_USS) {
            return;
        }
        int slot = activeShips.size();
        activeShips.add(entity);
        NBTTagCompound payload = entity.getPayload();
        NBTTagList list = (payload != null && payload.hasKey(VoidcraftNbt.TAG_PROGRAM))
            ? payload.getTagList(VoidcraftNbt.TAG_PROGRAM, 10)
            : null;
        pilots.add(USSShipPilot.create(entity, USSProgram.readFromNBT(list), this, entity.getSeed()));
        lastPushedShipStates[slot] = -1;
        lastPushedLegIds[slot] = -1;
        // The site's infrastructure cargo (the Dyson Swarm pass) moves into the finished base's hold (clamped by
        // the hold's capacity) before the site is completed away.
        injectSiteCargo(entity);
        completeBaseSite(entity.getAnchor());
        try {
            LOGGER.info(
                "[Voidcraft] VOIDBASE {} commissioned at {} — station joined the fleet",
                entity.getName(),
                entity.getAnchor());
        } catch (Throwable ignored) {}
        syncFleetRenderBlock();
    }

    /**
     * The system's planets (Phase 4 pass 3): a deterministic pure function of the star's TYPE and ignition
     * timestamp — the same list the Miner works (cargo), the render TE draws (its explicit planet system) and the
     * infodata displays. Empty while the star is COLD.
     *
     * @see USSPlanets#generate(USSStarType, long)
     */
    public List<USSPlanets.USSPlanet> getPlanets() {
        if (uss == null || !uss.isIgnited()) {
            return Collections.emptyList();
        }
        return USSPlanets.generate(uss.getStarType(), uss.getIgnitedAt());
    }

    /**
     * The system's spacetime-ripple FIELD (the Explorer pass): a deterministic pure function of the star's TYPE and
     * ignition timestamp — the same 343-point grid the Explorers scan (mechanics) and the client renders (the three
     * shells). The SCAN STATE (which points are revealed) lives on the {@link VoidcraftUSS} model (see
     * {@link #uss}), not here. Null while the star is COLD.
     *
     * @see USSRipples#generate(USSStarType, long)
     */
    public USSRippleField getRippleField() {
        if (uss == null || !uss.isIgnited()) {
            return null;
        }
        return USSRipples.generate(uss.getStarType(), uss.getIgnitedAt());
    }

    /**
     * The REVEALED spacetime-ripple positions (the Explorer pass): the ripple points that have been SCANNED (revealed)
     * and ARE ripples, as {@code [x, y, z]} in fleet-anchor blocks (the same frame the client star/ship renderer
     * draws in). Hidden ripples (not yet scanned) and revealed NON-ripples are absent — only revealed ripples render.
     *
     * @return the revealed ripple positions (never null; empty when nothing is revealed)
     */
    private List<float[]> revealedRipplePositions() {
        List<float[]> out = new ArrayList<>();
        USSRippleField field = getRippleField();
        if (field == null || uss == null) {
            return out;
        }
        for (int index : uss.getScannedRipples()) {
            if (index >= 0 && index < field.size() && field.isRipple(index)) {
                USSPosition p = field.positionOf(index);
                out.add(new float[] { (float) p.x(), (float) p.y(), (float) p.z() });
            }
        }
        return out;
    }

    /**
     * Launch a mission with the given ship payload (called by the {@code MTEVoidcraftGateway} after validating the
     * ship).
     *
     * <p>
     * Programming framework (Phase C): the ship no longer gets a mission TARGET at launch — it gets a PILOT that
     * runs the ship's PROGRAM (the controller's instruction list, carried in the payload's
     * {@code vc_program} tag). The ship starts HOLDING at its launch origin (the gateway, in fleet-anchor
     * coordinates) and moves only when its program tells it to. A ship without a program holds forever (the
     * program UI is the next pass).
     *
     * @param payload    the ship payload — the item's tag compound (blueprint + denormalized stats, vc_* keys at its
     *                   top level, + the optional vc_program), as written by the Assembler and read back by
     *                   {@code VoidcraftNbt}
     * @param gatewayPos gateway world position (the OUTBOUND/RETURNING endpoint + the surviving ship's
     *                   re-emission target) — carried by the SHIP, so each mission routes back to its own launcher
     * @param bayPos     storage-bay world position (the cargo delivery target) — carried by the SHIP
     * @return true when the ship is in flight; false when the USS rejects the launch (cold star, full, or invalid
     *         payload)
     */
    public boolean launchShip(NBTTagCompound payload, int[] gatewayPos, int[] bayPos) {
        if (uss == null || !uss.isIgnited() || activeShips.size() >= USSConstants.MAX_SHIPS_PER_USS) {
            return false;
        }
        if (payload == null || VoidcraftNbt.read(payload) == null) {
            return false;
        }
        String uuid = payload.hasKey(VoidcraftNbt.TAG_UUID) ? payload.getString(VoidcraftNbt.TAG_UUID) : "voidcraft";
        String name = payload.hasKey(VoidcraftNbt.TAG_NAME) ? payload.getString(VoidcraftNbt.TAG_NAME) : uuid;
        double speed = VoidcraftNbt.readDouble(payload, VoidcraftNbt.TAG_SPEED);
        long mining = VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_MINING);
        // The integrity time limit (user design): the ship enters the USS at its MAXIMUM (the blueprint's total)
        // and it counts down 1 per second until it is either gone (lost) or back (survived).
        long integrity = VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_INTEGRITY);
        int slot = activeShips.size();
        // Pass 5.1: a fresh per-launch identity seed — duplicated ship items share the item UUID, so the client's
        // per-ship animation phases + swarm spread must be keyed on this (unique per flight), not the UUID.
        int seed = new Random().nextInt();
        // Phase C: the launch origin — the gateway in fleet-anchor coordinates (the same rel() the client's
        // TAG_ENTRY_GW_REL uses, so the client draws the fresh ship exactly where the server has it). A null
        // gateway (defensive) degrades to the anchor itself.
        IGregTechTileEntity base = getBaseMetaTileEntity();
        int[] anchor = (base != null) ? shipAnchorPos(base) : new int[] { 0, 0, 0 };
        int[] gatewayWorld = (gatewayPos != null) ? gatewayPos : anchor;
        int[] gwRel = rel(anchor, gatewayWorld);
        USSPosition origin = USSPosition.of(gwRel[0], gwRel[1], gwRel[2]);
        VoidcraftActiveShip ship = VoidcraftActiveShip
            .launch(uuid, name, speed, mining, payload, gatewayPos, bayPos, seed, origin);
        // Phase C: the ship's program (the controller's instruction list) + the pilot that runs it. A corrupt
        // program degrades to an empty one (the ship HOLDS at the origin — never a half-run).
        NBTTagList programTag = payload.hasKey(VoidcraftNbt.TAG_PROGRAM)
            ? payload.getTagList(VoidcraftNbt.TAG_PROGRAM, 10)
            : null;
        USSProgram program = USSProgram.readFromNBT(programTag);
        USSShipPilot pilot = USSShipPilot.create(ship, program, this, seed);
        activeShips.add(ship);
        pilots.add(pilot);
        // Pass 26 (user: "add logging for the different missions and movements, that displays the calculated times
        // and progress"): one line at launch with the ship's identity and its program — the leg durations are
        // calculated per leg by the pilot (the legs no longer exist at launch; they are the program's).
        try {
            int instructions = program == null ? 0 : program.nodeCount();
            LOGGER.info(
                "[Voidcraft] LAUNCH {} — origin={} blocks, speed={}, program={} instruction(s), "
                    + "integrity time limit={}s",
                name,
                String.format("%.3f", origin.x()) + ","
                    + String.format("%.3f", origin.y())
                    + ","
                    + String.format("%.3f", origin.z()),
                String.format("%.3f", speed),
                instructions,
                integrity);
        } catch (Throwable ignored) {}
        lastPushedShipStates[slot] = -1;
        lastPushedLegIds[slot] = -1;
        syncFleetRenderBlock(); // Phase 4 pass 5: the whole fleet (now including this ship) goes into ONE anchor block
        return true;
    }

    /**
     * Advance every fleet entity one tick (called from {@link #onPostTick} while the star is ignited): the
     * in-flight ships AND the anchored Voidbases (one loop — the base's pilot runs its station program, its
     * position is re-derived from the live anchor). Entities that end this tick — COMPLETED (a ship's program
     * HOME leg just finished; a base never completes) or LOST (integrity reached 0) — are finished AFTER the
     * tick loop, in one index-safe reverse pass (completed → {@link #completeShip}, lost → {@link #loseShip}).
     *
     * <p>
     * Programming framework (Phase C): the loop is a PILOT loop — each pilot ticks its entity (the energy
     * buffer, the leg countdown, the program executor) and reports when a HOME leg completes (the mission is
     * over — {@link #completeShip} delivers). The work leg's yield (cargo / the Explorer reveal) is applied by
     * the pilot exactly once, through {@link #onWorkComplete}.
     *
     * <p>
     * Integrity time limit (user design): every entity's integrity drops per second
     * ({@link VoidcraftActiveShip#tickIntegrity(int)}) — at the base rate of 1/s, or the star-type-tuned rate
     * while the entity's hover body is the star (the star-proximity penalty,
     * {@link USSConstants#starIntegrityLossPerSecond(USSStarType)}) — an entity that hits 0 is removed
     * immediately and its cargo is discarded (a base is decommissioned).
     *
     * @param ship the fleet entity
     * @return true when the entity's hover body is the star: a base anchored to the star, or a ship whose
     *         resolved MOVE target is the star (the pilot's hover-body descriptor {@code -1}) and that has left
     *         the launch origin (a leg has started).
     */
    private boolean hoverBodyIsStar(VoidcraftActiveShip ship) {
        if (ship.isBase()) {
            return ship.getAnchor() != null && ship.getAnchor()
                .isStar();
        }
        return ship.getTargetPlanet() == -1 && ship.getLegId() > 0;
    }

    private void tickShips() {
        if (!activeShips.isEmpty()) {
            // Progress heartbeat pace: the counter advances once per machine tick and the heartbeat is the tick
            // on which it reaches PROGRESS_LOG_INTERVAL (deterministic — no world-clock dependency).
            progressLogTicks++;
            boolean progressTick = progressLogTicks >= PROGRESS_LOG_INTERVAL;
            if (progressTick) {
                progressLogTicks = 0L;
            }
            List<Integer> completed = new ArrayList<>();
            List<Integer> lost = new ArrayList<>();
            for (int slot = 0; slot < activeShips.size(); slot++) {
                VoidcraftActiveShip ship = activeShips.get(slot);
                // The integrity time limit: 1 per second while the entity's hover body is NOT the star, and the
                // star-type-tuned rate (USSConstants.starIntegrityLossPerSecond) while it IS — hovering around the
                // star corrodes the hull faster (the entity counts its own ticks — even while HOLDING). At 0 the
                // entity is LOST: removed below, its cargo discarded (no delivery, no drop, no re-emission).
                int integrityLoss = hoverBodyIsStar(ship) ? USSConstants.starIntegrityLossPerSecond(uss.getStarType())
                    : 1;
                if (ship.tickIntegrity(integrityLoss)) {
                    lost.add(slot);
                    continue;
                }
                // An anchored Voidbase stands at its anchor's hover point (within ±30° of the orbital plane),
                // re-derived from the live anchor each tick (a planet anchor orbits).
                if (ship.isBase()) {
                    USSPosition hover = anchorHoverPoint(ship.getAnchor());
                    if (hover != null) {
                        ship.setPosition(hover);
                    }
                    // A station anchored to the star runs its Satellite Rail Launchers: Power Satellites leave the
                    // base's hold for the star's Dyson Swarm (the infrastructure pass). A station anchored to
                    // the star OR a ripple runs its infrastructure builders: the base's builder components
                    // join structure units of their type on the anchor target (the infrastructure-builder pass).
                    if (ship.getAnchor() != null && (ship.getAnchor()
                        .isStar()
                        || ship.getAnchor()
                            .isRipple())) {
                        if (ship.getAnchor()
                            .isStar()) {
                            tickSatelliteLauncher(ship);
                        }
                        tickInfrastructureBuilder(ship);
                    }
                }
                USSShipPilot pilot = pilots.get(slot);
                if (pilot.tick()) {
                    // HOME leg complete — the mission is over (delivery + re-emission below).
                    completed.add(slot);
                    continue;
                }
                // Mark the fleet dirty when a ship's state OR leg id changes — pushed ONCE per tick at the end (no
                // per-tick packets, and one full-fleet push instead of one per ship — Phase 4 pass 5). The leg id
                // (Phase C) makes consecutive legs of the SAME state (MOVE → MOVE) animate from their own start.
                int stateId = ship.getState()
                    .getId();
                int legId = ship.getLegId();
                if (lastPushedShipStates[slot] != stateId || lastPushedLegIds[slot] != legId) {
                    // Diagnostic (pass 7): one line per state transition. If a ship appears to "turn back without
                    // mining", the server's truth is right here — OUTBOUND → MINING → RETURNING in order, with the
                    // MINING leg's duration in ticks (a long gap before RETURNING = the mission logic ran
                    // correctly).
                    try {
                        LOGGER.info(
                            "[Voidcraft] {} mission: {} ({} ticks left, target={})",
                            ship.getName(),
                            ship.getState()
                                .name(),
                            ship.getTicksRemaining(),
                            ship.getTargetPlanet());
                    } catch (Throwable ignored) {}
                    lastPushedShipStates[slot] = stateId;
                    lastPushedLegIds[slot] = legId;
                    fleetDirty = true;
                }
                // A periodic progress heartbeat — once per PROGRESS_LOG_INTERVAL machine ticks (the counter
                // advanced at the top of this method), one line per in-flight ship showing the current leg and
                // its PROGRESS fraction (the ship's ticks-remaining against the leg's total calculated
                // duration), so the movement's progress is visible in the game log. The state-transition log
                // above already prints the leg's total (its "ticks left" at the transition); this adds the
                // running progress in between.
                if (progressTick) {
                    try {
                        long legTotal = ship.getLegTotal();
                        double progress = legTotal > 0 ? (1.0 - (double) ship.getTicksRemaining() / (double) legTotal)
                            : 1.0;
                        LOGGER.info(
                            "[Voidcraft] PROGRESS {} — {} {}% ({} ticks left of {})",
                            ship.getName(),
                            ship.getState()
                                .name(),
                            String.format("%.0f", progress * 100.0),
                            ship.getTicksRemaining(),
                            legTotal);
                    } catch (Throwable ignored) {}
                }
            }
            // Resolve the tick's endings (completions + losses) in ONE index-safe reverse pass — a ship cannot
            // be both (a lost ship is skipped before the pilot tick), but the two slot lists must not shift
            // each other's indices as the lists are mutated.
            List<Integer> ending = new ArrayList<>(completed);
            for (Integer s : lost) {
                if (!ending.contains(s)) {
                    ending.add(s);
                }
            }
            ending.sort(java.util.Collections.reverseOrder());
            for (int slot : ending) {
                if (completed.contains(slot)) {
                    completeShip(slot);
                } else {
                    loseShip(slot);
                }
            }
            if (fleetDirty || !completed.isEmpty() || !lost.isEmpty()) {
                syncFleetRenderBlock(); // one push for every state change + ending of this tick
            }
        } else {
            progressLogTicks = 0L;
        }
        // Resync the fleet anchor when the render-visible fleet state changed (integrity decay or repair, a
        // site advancing, a gateway registering or being destroyed) — prune dead gateways first so the
        // signature reflects their removal.
        IGregTechTileEntity mteBase = getBaseMetaTileEntity();
        if (mteBase != null) {
            pruneGateways(shipAnchorPos(mteBase));
        }
        if (fleetRenderSignature() != lastFleetRenderSignature) {
            syncFleetRenderBlock();
        }
    }

    /**
     * Fleet entity LOST (the integrity time limit hit 0): it is removed from the USS and its cargo is
     * DISCARDED — no delivery to the bay, no drop at the USS, no re-emission. The fleet anchor is resynced by
     * the CALLER (one push for the whole fleet).
     */
    private void loseShip(int slot) {
        if (slot < 0 || slot >= activeShips.size()) {
            return;
        }
        VoidcraftActiveShip lostShip = activeShips.get(slot);
        activeShips.remove(slot);
        if (slot < pilots.size()) {
            pilots.remove(slot);
        }
        stabilizes.remove(lostShip.getUuid());
        lastPushedShipStates[slot] = -1;
        lastPushedLegIds[slot] = -1;
        try {
            if (lostShip.isBase()) {
                LOGGER.info(
                    "[Voidcraft] VOIDBASE {} decommissioned at {} — integrity reached 0, base removed",
                    lostShip.getName(),
                    lostShip.getAnchor());
            } else {
                LOGGER.info(
                    "[Voidcraft] LOST {} (slot {}) — integrity reached 0: ship removed, cargo discarded",
                    lostShip.getName(),
                    slot);
            }
        } catch (Throwable ignored) {}
    }

    /**
     * Whether the given mission is a Voidbase construction mission (the gateway set the flag at launch after
     * writing the blueprint data into the ship's payload). Such a ship produces no WORK-leg cargo —
     * its parts are drawn from the hold in flight by the CONSTRUCT leg (create-or-fill the site at the anchor).
     */
    private boolean isVoidbaseMission(VoidcraftActiveShip ship) {
        if (ship == null || ship.getPayload() == null) {
            return false;
        }
        NBTTagCompound payload = ship.getPayload();
        return payload.getBoolean(VoidcraftNbt.TAG_BUILD_MISSION);
    }

    /**
     * Build the cargo of a completed MINER mission (the mechanics pass): the ship mines its ONE target planet
     * (pass 7 — {@link VoidcraftActiveShip#getTargetPlanet()}), and the cargo is that planet's registered ores,
     * weighed by their weights, each capped by the planet's remaining reserve. The reserve is initialized from the
     * planet definition on the first mine ({@code ore.amount × planetSize²}) and then decremented — so ores deplete
     * over the planet's lifetime (the reserve lives on the {@link VoidcraftUSS} model and is persisted).
     *
     * @param ship the miner ship (its target planet + mining power)
     * @return the cargo compound (never null; empty when the planet is absent or fully depleted)
     */
    private NBTTagCompound buildMinerCargo(VoidcraftActiveShip ship, int target) {
        List<USSPlanets.USSPlanet> planets = getPlanets();
        if (ship == null || planets.isEmpty() || target < 0 || target >= planets.size()) {
            return new NBTTagCompound(); // no planet to mine (defensive)
        }
        USSPlanets.USSPlanet planet = planets.get(target);
        VoidcraftUSS.MaterialReserve currentReserve = uss.getPlanetReserve(target);
        USSShipCargo.MinerResult result = USSShipCargo.minePlanet(planet, ship.getMiningPower(), currentReserve);
        // Persist the updated reserve (the planet depletes).
        uss = uss.withPlanetReserve(target, result.newReserve);
        return result.cargo;
    }

    /**
     * Build the cargo of a completed STARLIFTER mission (the starlifter pass): the star's PRODUCED fluids, each
     * capped by the star's remaining fluid reserve. The reserve is initialized from the star definition on the
     * first siphon ({@code material.amount × 1_000_000 × starSize²}) and then decremented — so the fluids deplete
     * over the star's life (the amount left later feeds the Dyson swarm output and stellar evolution; the reserve
     * lives on the {@link VoidcraftUSS} model and is persisted).
     *
     * @param ship the starlifter ship (its siphon power)
     * @return the cargo compound (never null; empty when the star yields nothing)
     */
    private NBTTagCompound buildStarlifterCargo(VoidcraftActiveShip ship) {
        if (ship == null || uss == null) {
            return new NBTTagCompound(); // no star to siphon (defensive)
        }
        USSStarDefinition star = USSStarRegistry.byType(uss.getStarType());
        double starSize = USSPlanets.sampleStarSize(uss.getStarType(), uss.getIgnitedAt());
        VoidcraftUSS.MaterialReserve currentReserve = uss.getStarFluidReserve();
        USSShipCargo.StarlifterResult result = USSShipCargo
            .siphonStar(star, starSize, ship.getStarlifterPower(), currentReserve);
        // Persist the updated reserve (the star depletes).
        uss = uss.withStarFluidReserve(result.newReserve);
        return result.cargo;
    }

    // region USSPilotWorld (programming framework, Phase C — the game seam the pilots run against)

    /**
     * A ship's WORK leg just completed (the pilot calls this EXACTLY ONCE per work leg — its side-effect fires
     * here). The yield is keyed by the leg's WORK KIND (owned by the work command that started the leg):
     * <ul>
     * <li>MINE — the current target planet's registered ores (the reserve depletes), clamped by the ship's hold;
     * a non-planet target (a MINE at the star / a ripple) delivers nothing but logs the reason;</li>
     * <li>SCAN — the current ripple point is REVEALED (the yield is the reveal itself, not cargo); a non-ripple
     * target logs the reason;</li>
     * <li>SIPHON — the star's produced fluids (each capped by the star's remaining fluid reserve — the reserve
     * depletes) when the target is the star; anything else logs the reason.</li>
     * </ul>
     * A Voidbase construction mission carries no cargo either (its parts are drawn from the hold in flight by the
     * CONSTRUCT leg).
     */
    @Override
    public void onWorkComplete(VoidcraftActiveShip ship, int workKind, String targetKind, int targetIndex) {
        switch (workKind) {
            case USSWorkKind.SCAN: {
                boolean ripple = USSProgramDefaults.TARGET_RIPPLE.equals(targetKind)
                    || USSProgramDefaults.TARGET_RIPPLE_UNSCANNED.equals(targetKind);
                if (ripple && targetIndex >= 0 && uss != null && !uss.isRippleScanned(targetIndex)) {
                    // The scan leg finished — mark the ripple point as REVEALED. The point is marked exactly once
                    // (a re-scanned point is a no-op) and the fleet is resynced so the client starts rendering it.
                    uss = uss.withRippleScanned(targetIndex);
                    boolean isRipple = getRippleField() != null && getRippleField().isRipple(targetIndex);
                    try {
                        LOGGER.info(
                            "[Voidcraft] Explorer {} revealed ripple point {} (ripple={})",
                            ship.getName(),
                            targetIndex,
                            isRipple);
                    } catch (Throwable ignored) {}
                    syncFleetRenderBlock();
                } else if (!ripple || targetIndex < 0) {
                    log(ship, "SCAN: nothing to scan here");
                }
                return; // no cargo for a scan (the reveal is the yield)
            }
            case USSWorkKind.MINE: {
                if (isVoidbaseMission(ship)) {
                    return; // no cargo: the parts are drawn from the hold in flight by the CONSTRUCT leg
                }
                boolean planet = USSProgramDefaults.TARGET_PLANET.equals(targetKind)
                    || USSProgramDefaults.TARGET_NEAREST_PLANET.equals(targetKind)
                    || USSProgramDefaults.TARGET_RANDOM_PLANET.equals(targetKind);
                if (!planet || targetIndex < 0) {
                    log(ship, "MINE: nothing to mine here");
                    return;
                }
                applyWorkCargo(ship, buildMinerCargo(ship, targetIndex));
                return;
            }
            case USSWorkKind.SIPHON: {
                if (USSProgramDefaults.TARGET_STAR.equals(targetKind)) {
                    applyWorkCargo(ship, buildStarlifterCargo(ship));
                } else {
                    log(ship, "SIPHON: nothing to siphon here");
                }
                return;
            }
            default:
                return; // a travel leg has no work yield (defensive — the pilot only calls this for work legs)
        }
    }

    /** Fills the ship's hold with a work yield (clamped by capacity) and derives the deliverable cargo. */
    private void applyWorkCargo(VoidcraftActiveShip ship, NBTTagCompound cargo) {
        if (cargo == null) {
            return;
        }
        // CARGO CAPACITY (the cargo-capacity pass): the ship's internal hold is filled with the yield, clamped by
        // the hold's capacity — "mining fills their internal cargo capacity, and they cannot mine if it is full"
        // (a full hold simply accepts 0 more). The deliverable cargo is then derived from the hold (the source of
        // truth).
        CargoHold hold = USSShipCargo.fillHold(ship.getHold(), cargo);
        ship.setHold(hold);
        ship.setCargo(USSShipCargo.cargoFromHold(hold));
    }

    @Override
    public String readVar(int slot) {
        if (uss == null) {
            return "";
        }
        return uss.getVariables()
            .get(slot);
    }

    @Override
    public void writeVar(int slot, String value) {
        if (uss == null) {
            return;
        }
        // The variable space is fleet-SHARED data (ships' in/out channel) — immutable update on the model.
        uss = uss.withVariables(
            uss.getVariables()
                .set(slot, value));
    }

    @Override
    public int unscannedRipples() {
        USSRippleField field = getRippleField();
        if (field == null || uss == null) {
            return 0;
        }
        int count = 0;
        for (int i = 0; i < field.size(); i++) {
            if (!uss.isRippleScanned(i)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public USSTargetResult resolveTarget(String target, int index, VoidcraftActiveShip ship) {
        if (target == null || ship == null) {
            return null;
        }
        if (USSProgramDefaults.TARGET_STAR.equals(target)) {
            return new USSTargetResult(USSFleetOrbit.starPosition(), USSTargetResult.INDEX_STAR);
        }
        if (USSProgramDefaults.TARGET_PLANET.equals(target)) {
            if (index < 0) {
                return null;
            }
            USSPosition dest = destinationFor(index, ship.getSeed());
            if (dest == null) {
                return null; // out of range / no planets
            }
            return new USSTargetResult(dest, index);
        }
        if (USSProgramDefaults.TARGET_NEAREST_PLANET.equals(target)) {
            List<USSPlanets.USSPlanet> planets = getPlanets();
            if (planets == null || planets.isEmpty()) {
                return null;
            }
            USSPosition shipPos = ship.getPosition();
            float time = worldTimeTicks();
            float starSize = USSPlanets.starRenderSize(uss.getStarSize());
            int nearest = -1;
            double best = Double.MAX_VALUE;
            for (int i = 0; i < planets.size(); i++) {
                USSPosition center = USSFleetOrbit.planetPosition(planets.get(i), starSize, time);
                double d = (shipPos == null) ? 0.0 : shipPos.distanceTo(center);
                if (d < best) {
                    best = d;
                    nearest = i;
                }
            }
            USSPosition dest = destinationFor(nearest, ship.getSeed());
            return (dest == null) ? null : new USSTargetResult(dest, nearest);
        }
        if (USSProgramDefaults.TARGET_RANDOM_PLANET.equals(target)) {
            // A random planet of THIS USS (pass-33 UI helper): one-shot pick at resolution (the same pattern as
            // RIPPLE_UNSCANNED); the RESOLVED index is what the WORK leg then mines.
            List<USSPlanets.USSPlanet> planets = getPlanets();
            if (planets == null || planets.isEmpty()) {
                return null;
            }
            int idx = new Random().nextInt(planets.size());
            USSPosition dest = destinationFor(idx, ship.getSeed());
            return (dest == null) ? null : new USSTargetResult(dest, idx);
        }
        if (USSProgramDefaults.TARGET_RIPPLE.equals(target)) {
            USSRippleField field = getRippleField();
            if (field == null || index < 0 || index >= field.size()) {
                return null;
            }
            // Fixed point (the points do not orbit) — the client hovers here exactly.
            return new USSTargetResult(field.positionOf(index), index, true);
        }
        if (USSProgramDefaults.TARGET_RIPPLE_UNSCANNED.equals(target)) {
            int point = pickUnscannedRipplePoint();
            if (point >= 0) {
                USSRippleField field = getRippleField();
                if (field != null) {
                    return new USSTargetResult(field.positionOf(point), point, true);
                }
            }
            // Nothing left to scan — the ship works the star instead (the old pickMissionTarget fallback).
            return new USSTargetResult(USSFleetOrbit.starPosition(), USSTargetResult.INDEX_STAR);
        }
        if (USSProgramDefaults.TARGET_SHIP.equals(target)) {
            if (index < 0 || index >= activeShips.size()) {
                return null;
            }
            USSPosition base = activeShips.get(index)
                .getPosition();
            if (base == null) {
                return null;
            }
            // A small deterministic offset (per the waiting ship's seed) so two ships never occupy the same point.
            return new USSTargetResult(
                USSFleetOrbit.nudge(base, 2.0, ship.getSeed()),
                USSTargetResult.INDEX_SHIP,
                true);
        }
        // TARGET_HOME is resolved by the pilot itself (its launch origin) — it never reaches this seam.
        return null;
    }

    @Override
    public long legTicks(int workKind, VoidcraftActiveShip ship, double distance) {
        if (ship == null) {
            return 0L;
        }
        // The same tables the client animates with (USSConstants) — server and client agree on every leg's length.
        // The work KIND (owned by the work command) picks the work table.
        USSShipState state = USSWorkKind.isWork(workKind) ? USSShipState.MINING : USSShipState.OUTBOUND;
        long ticks = USSConstants.legTicks(
            state,
            distance,
            ship.getSpeed(),
            workKind,
            ship.getMiningPower(),
            ship.getScanPower(),
            ship.getStarlifterPower());
        return ticks > 0 ? ticks : 1L;
    }

    @Override
    public void log(VoidcraftActiveShip ship, String message) {
        try {
            LOGGER.info("[Voidcraft] {} — {}", ship != null ? ship.getName() : "ship", message);
        } catch (Throwable ignored) {}
    }

    // region star-scale infrastructure (the Dyson Swarm pass — the Satellite Rail Launcher)

    /**
     * One machine tick of the star's infrastructure: the satellite swarm decays proportionally to its count (the
     * per-unit rate is {@link USSInfra#decayPerUnitPerTick()}) — the swarm is the only decaying structure; the
     * built infrastructure shells (injector / stabilizer / lens) never decay.
     */
    private void tickStarInfrastructure() {
        if (uss == null || !uss.isIgnited()) {
            return;
        }
        USSInfrastructure infra = uss.getInfrastructure();
        if (infra == null || infra.count(USSInfrastructure.DYSON_STAR_KEY) <= 0L) {
            return;
        }
        // The step's infrastructure carries the (possibly advanced) decay accumulator even when no whole unit was
        // lost — dropping it would stall the accumulator.
        USSInfrastructure.DecayStep step = infra
            .applyDecay(USSInfrastructure.DYSON_STAR_KEY, USSInfra.decayPerUnitPerTick());
        if (step.infrastructure != infra) {
            uss = uss.withInfrastructure(step.infrastructure);
        }
        if (step.lost > 0L) {
            syncStarRenderBlock();
        }
    }

    /**
     * One machine tick of the Stellar Injector (active when the star's Injector shell is FULLY BUILT — a shell
     * under construction injects nothing): on the step pace, one size step leaves the injector buffer — the step's
     * cost is the size-scaled cargo amount (the star's CURRENT size squared, see USSStellarEvolution), the star's
     * size rises by one step up to the 1.5x cap (the original sampled size). A step the buffer cannot pay is
     * skipped (no partial consumption); a step that would cross the cap is not started.
     */
    private void tickInjector() {
        if (uss == null || !uss.isIgnited()) {
            return;
        }
        if (!USSInfraBuild.isBuilt(
            uss.getInfrastructure(),
            USSInfraBuild.INJECTOR,
            USSInfraBuild.TARGET_STAR,
            -1,
            infraShellCapacity(USSInfraBuild.INJECTOR, USSInfraBuild.TARGET_STAR, -1))) {
            return;
        }
        double cap = USSStellarEvolution.sizeCap(USSPlanets.sampleStarSize(uss.getStarType(), uss.getIgnitedAt()));
        if (uss.getStarSize() + USSConstants.INJECTOR_SIZE_STEP > cap + 1e-9) {
            return;
        }
        if (++injectorStepTicks < USSConstants.INJECTOR_STEP_INTERVAL_TICKS) {
            return;
        }
        injectorStepTicks = 0L;
        long cost = USSStellarEvolution.cargoUnitsForSizeDelta(uss.getStarSize(), USSConstants.INJECTOR_SIZE_STEP);
        CargoHold buffer = uss.getInjectorBuffer();
        if (buffer == null || buffer.usedUnits() < cost) {
            return;
        }
        double oldSize = uss.getStarSize();
        uss = uss.withInjectorBuffer(buffer.removeUnits(cost))
            .withStarSize(oldSize + USSConstants.INJECTOR_SIZE_STEP);
        try {
            LOGGER.info(
                "[Voidcraft] USS Stellar Injector: star size {} -> {} (buffer -{} units)",
                String.format("%.2f", oldSize),
                String.format("%.2f", oldSize + USSConstants.INJECTOR_SIZE_STEP),
                cost);
        } catch (Throwable ignored) {}
        syncStarRenderBlock();
    }

    /**
     * One machine tick of a star-anchored base's Satellite Rail Launchers: a Power Satellite leaves the base's
     * hold for the star's swarm on a fixed pace (one launch per launcher interval), capped at the star's satellite
     * capacity.
     *
     * @param ship the star-anchored base (the caller guarantees an ignited star and a star anchor)
     */
    private void tickSatelliteLauncher(VoidcraftActiveShip ship) {
        VoidcraftBlueprint blueprint = VoidcraftNbt.readBase(ship.getPayload());
        if (blueprint == null || blueprint.count(VoidcraftComponent.SATELLITE_LAUNCHER) <= 0) {
            return;
        }
        long capacity = USSInfra.starSatelliteCapacity(USSPlanets.starRenderSize(uss.getStarSize()));
        long current = uss.getInfrastructure()
            .count(USSInfrastructure.DYSON_STAR_KEY);
        if (current >= capacity) {
            return; // the swarm is saturated — no further launches
        }
        // The star's shell slot is exclusive (Dyson Swarm / Stellar Injector / Stellar Gravitational Lens):
        // an injector or lens built on the star — even a partial shell — blocks the swarm.
        USSInfrastructure infra = uss.getInfrastructure();
        if (infra.count(USSInfraBuild.key(USSInfraBuild.INJECTOR, USSInfraBuild.TARGET_STAR, -1)) > 0L
            || infra.count(USSInfraBuild.key(USSInfraBuild.LENS, USSInfraBuild.TARGET_STAR, -1)) > 0L) {
            return;
        }
        String uuid = ship.getUuid();
        long countdown = satelliteLaunchCountdowns.getOrDefault(uuid, USSConstants.DYSON_SATELLITE_LAUNCH_INTERVAL) - 1;
        if (countdown > 0L) {
            satelliteLaunchCountdowns.put(uuid, countdown);
            return;
        }
        satelliteLaunchCountdowns.put(uuid, USSConstants.DYSON_SATELLITE_LAUNCH_INTERVAL);
        CargoHold hold = ship.getHold();
        if (hold == null || hold.itemsOf(USSInfra.KEY_POWER_SATELLITE) <= 0L) {
            return; // nothing on board — the countdown advanced, no launch
        }
        ship.setHold(hold.removeItem(USSInfra.KEY_POWER_SATELLITE, 1L));
        uss = uss.withInfrastructure(
            uss.getInfrastructure()
                .addUnits(USSInfrastructure.DYSON_STAR_KEY, 1L));
        if (current % 20L == 0L) {
            log(ship, "LAUNCHER: a Power Satellite joined the Dyson Swarm (" + (current + 1L) + "/" + capacity + ")");
        }
        syncStarRenderBlock();
    }

    /**
     * One machine tick of a standing base's infrastructure builders (the infrastructure-builder pass): each
     * builder component the base's blueprint carries joins ONE structure unit of its type on the base's anchor
     * target (the star, or the anchor ripple for the Stabilizer) per build interval, drawing the matching
     * component from the base's hold — clamped by the target's shell capacity (its triangle count) and the
     * one-structure-per-target rule. A base anchored to a planet builds nothing.
     *
     * @param ship the standing base (the caller guarantees a star- or ripple-anchored base on an ignited star)
     */
    private void tickInfrastructureBuilder(VoidcraftActiveShip ship) {
        USSBaseAnchor anchor = ship.getAnchor();
        int targetKind = anchor.isStar() ? USSInfraBuild.TARGET_STAR : USSInfraBuild.TARGET_RIPPLE;
        int index = anchor.isRipple() ? anchor.index() : -1;
        VoidcraftBlueprint blueprint = VoidcraftNbt.readBase(ship.getPayload());
        if (blueprint == null) {
            return;
        }
        double renderSize = targetKind == USSInfraBuild.TARGET_STAR ? starShellRenderSize()
            : USSPlanets.starRenderSize(uss.getStarSize());
        for (int type = USSInfraBuild.INJECTOR; type <= USSInfraBuild.LENS; type++) {
            if (blueprint.count(USSInfraBuild.builderComponent(type)) <= 0L) {
                continue;
            }
            if (!USSInfraBuild.isValidTarget(type, targetKind)) {
                continue; // this builder does not build on this target kind
            }
            long capacity = USSInfraBuild.capacity(type, targetKind, renderSize);
            String key = USSInfraBuild.key(type, targetKind, index);
            long current = uss.getInfrastructure()
                .count(key);
            if (current >= capacity) {
                continue; // the structure is complete
            }
            if (USSInfraBuild.targetOccupiedByOther(uss.getInfrastructure(), type, targetKind, index)) {
                continue; // the target already hosts another structure
            }
            String countdownKey = ship.getUuid() + '#' + type;
            long countdown = infraBuildCountdowns.getOrDefault(countdownKey, USSConstants.INFRA_BUILD_INTERVAL) - 1L;
            if (countdown > 0L) {
                infraBuildCountdowns.put(countdownKey, countdown);
                continue;
            }
            infraBuildCountdowns.put(countdownKey, USSConstants.INFRA_BUILD_INTERVAL);
            CargoHold hold = ship.getHold();
            String cargoKey = USSInfra.componentKey(type);
            if (hold == null || hold.itemsOf(cargoKey) <= 0L) {
                continue; // nothing on board — the countdown advanced, no unit built
            }
            ship.setHold(hold.removeItem(cargoKey, 1L));
            uss = uss.withInfrastructure(
                uss.getInfrastructure()
                    .addUnits(key, 1L));
            if (current % 20L == 0L) {
                log(
                    ship,
                    "BUILDER: " + USSInfraBuild
                        .name(type) + " structure " + (current + 1L) + "/" + capacity + " at " + anchor);
            }
            syncStarRenderBlock();
        }
    }

    /**
     * Deliver the constructor's on-board infrastructure cargo (the dedicated hold keys) to the build site
     * (unpaced — the site holds them until the finished base's hold receives them at spawn).
     */
    private void deliverInfraCargo(VoidcraftActiveShip ship, USSBaseSite site) {
        CargoHold hold = ship.getHold();
        if (hold == null) {
            return;
        }
        CargoHold next = hold;
        for (String key : USSInfra.INFRA_CARGO_KEYS) {
            long onBoard = next.itemsOf(key);
            if (onBoard <= 0L) {
                continue;
            }
            long delivered = site.addCargo(key, onBoard);
            if (delivered > 0L) {
                next = next.removeItem(key, delivered);
            }
        }
        if (next != hold) {
            ship.setHold(next);
        }
    }

    /**
     * Move a build site's infrastructure cargo into the finished base's hold (clamped by the hold's capacity —
     * overflow is discarded).
     */
    private void injectSiteCargo(VoidcraftActiveShip base) {
        USSBaseAnchor anchor = base.getAnchor();
        USSBaseSite site = anchor != null ? getBaseSite(anchor) : null;
        if (site == null || site.cargoView()
            .isEmpty()) {
            return;
        }
        base.initializeHold();
        CargoHold hold = base.getHold();
        if (hold == null) {
            return;
        }
        for (Map.Entry<String, Long> entry : site.cargoView()
            .entrySet()) {
            hold = hold.addItem(entry.getKey(), entry.getValue());
        }
        base.setHold(hold);
    }

    /**
     * Push the star's Dyson Swarm state (satellite count + capacity) to the star's render block — the client's
     * shell redraws from the description packet (markBlockForUpdate, the same mechanism createRenderBlock uses).
     */
    private void syncStarRenderBlock() {
        IGregTechTileEntity mte = getBaseMetaTileEntity();
        if (mte == null || !animationsEnabled) {
            return;
        }
        int x = mte.getXCoord();
        int y = mte.getYCoord();
        int z = mte.getZCoord();
        int sx = (int) (x + 32 * getExtendedFacing().getRelativeBackInWorld().offsetX);
        int sy = (int) (y + 32 * getExtendedFacing().getRelativeBackInWorld().offsetY);
        int sz = (int) (z + 32 * getExtendedFacing().getRelativeBackInWorld().offsetZ);
        TileEntityEyeOfHarmony te = (TileEntityEyeOfHarmony) mte.getWorld()
            .getTileEntity(sx, sy, sz);
        if (te == null || uss == null || !uss.isIgnited()) {
            return;
        }
        long capacity = USSInfra.starSatelliteCapacity(USSPlanets.starRenderSize(uss.getStarSize()));
        long count = uss.getInfrastructure()
            .count(USSInfrastructure.DYSON_STAR_KEY);
        te.setDysonSwarm(count, capacity);
        // The star's rendered size (the Stellar Injector grows the star — the star TE is the one render that
        // needs it; the fleet TE gets the same value with its system specs).
        te.setStarSize(USSPlanets.starRenderSize(uss.getStarSize()));
        te.setInfraShell(starInfraShellType(), starInfraShellCount(), starInfraShellCapacity());
        te.setUssOrbitTime(
            uss.getVirtualTime(),
            mte.getWorld()
                .getTotalWorldTime());
        mte.getWorld()
            .markBlockForUpdate(sx, sy, sz);
        // The ripple-scale infrastructure shells ride the fleet anchor (the same frame as the revealed ripples).
        int[] anchor = shipAnchorPos(mte);
        TileEntity fleetTe = mte.getWorld()
            .getTileEntity(anchor[0], anchor[1], anchor[2]);
        if (fleetTe instanceof TileEntityVoidcraftShip) {
            ((TileEntityVoidcraftShip) fleetTe).setRippleInfraShells(rippleInfraShells());
            mte.getWorld()
                .markBlockForUpdate(anchor[0], anchor[1], anchor[2]);
        }
    }

    /**
     * The star's ORIGINAL sampled render size (the Stellar Evolution pass): the Stellar Injector and Stellar
     * Gravitational Lens shells are built ONCE around the ignition-size star (the star never outgrows its 1.5x cap),
     * so their geometry and capacity stay pinned to the ignition size — a live-size capacity would grow behind the
     * built shell and read it as incomplete (the Dyson Swarm keeps the LIVE size: the swarm tracks the star).
     */
    private double starShellRenderSize() {
        double original = USSPlanets.sampleStarSize(uss.getStarType(), uss.getIgnitedAt());
        return USSPlanets.starRenderSize(original > 0.0 ? original : uss.getStarSize());
    }

    /**
     * The star's non-Dyson infrastructure shell (the infrastructure-builder pass): the Stellar Injector or the
     * Stellar Gravitational Lens built on the star — at most one (the star's shell slot is exclusive) — as the
     * shell TYPE to render (-1 when the star's shell is the Dyson Swarm or none).
     */
    private int starInfraShellType() {
        if (uss == null || !uss.isIgnited()) {
            return -1;
        }
        USSInfrastructure infra = uss.getInfrastructure();
        long injector = infra.count(USSInfraBuild.key(USSInfraBuild.INJECTOR, USSInfraBuild.TARGET_STAR, -1));
        long lens = infra.count(USSInfraBuild.key(USSInfraBuild.LENS, USSInfraBuild.TARGET_STAR, -1));
        if (injector > 0L) {
            return USSInfraBuild.INJECTOR;
        }
        if (lens > 0L) {
            return USSInfraBuild.LENS;
        }
        return -1;
    }

    /** @return the built unit count of the star's non-Dyson infrastructure shell (0 when none) */
    private long starInfraShellCount() {
        int type = starInfraShellType();
        if (type < 0) {
            return 0L;
        }
        return uss.getInfrastructure()
            .count(USSInfraBuild.key(type, USSInfraBuild.TARGET_STAR, -1));
    }

    /** @return the build capacity of the star's non-Dyson infrastructure shell (0 when none) */
    private long starInfraShellCapacity() {
        int type = starInfraShellType();
        if (type < 0) {
            return 0L;
        }
        return USSInfraBuild.starCapacity(type, starShellRenderSize());
    }

    /**
     * The ripple-scale infrastructure shell state (the infrastructure-builder pass): each
     * {@code [x, y, z, count, capacity]} — one entry per revealed ripple carrying a built Stabilizer shell
     * (fleet-anchor blocks, the same frame as the revealed ripples; hidden ripples stay absent).
     */
    private List<float[]> rippleInfraShells() {
        List<float[]> out = new ArrayList<>();
        if (uss == null || !uss.isIgnited()) {
            return out;
        }
        USSRippleField field = getRippleField();
        if (field == null) {
            return out;
        }
        long capacity = USSInfraBuild.rippleCapacity();
        // The progress key of a ripple target is "<name>:ripple:<index>" (USSInfraBuild.key) — strip the whole
        // target part before parsing the index, not just the type prefix.
        String ripplePrefix = USSInfraBuild.prefix(USSInfraBuild.STABILIZER) + "ripple:";
        for (Map.Entry<String, Long> entry : uss.getInfrastructure()
            .counts()
            .entrySet()) {
            String k = entry.getKey();
            if (!k.startsWith(ripplePrefix) || entry.getValue() <= 0L) {
                continue;
            }
            int index;
            try {
                index = Integer.parseInt(k.substring(ripplePrefix.length()));
            } catch (NumberFormatException e) {
                continue;
            }
            if (index < 0 || index >= field.size()) {
                continue;
            }
            if (!uss.getScannedRipples()
                .contains(index)) {
                continue;
            }
            USSPosition p = field.positionOf(index);
            out.add(
                new float[] { (float) p.x(), (float) p.y(), (float) p.z(), (float) entry.getValue(),
                    (float) capacity });
        }
        return out;
    }

    /**
     * Push the virtual orbit clock pair to both render TEs (the star block + the fleet anchor): the client
     * extrapolates at the normal rate (1 tick/tick) from the last sync, so a push is only needed while the clock
     * runs faster than the world (a stellar-acceleration second) — everything else keeps the world-clock phase.
     */
    private void syncOrbitClock() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return;
        }
        World world = base.getWorld();
        if (world == null || world.isRemote || uss == null || !uss.isIgnited()) {
            return;
        }
        long orbitTime = uss.getVirtualTime();
        long worldTime = world.getTotalWorldTime();
        int sx = (int) (base.getXCoord() + 32 * getExtendedFacing().getRelativeBackInWorld().offsetX);
        int sy = (int) (base.getYCoord() + 32 * getExtendedFacing().getRelativeBackInWorld().offsetY);
        int sz = (int) (base.getZCoord() + 32 * getExtendedFacing().getRelativeBackInWorld().offsetZ);
        TileEntity starTe = world.getTileEntity(sx, sy, sz);
        if (starTe instanceof TileEntityEyeOfHarmony) {
            ((TileEntityEyeOfHarmony) starTe).setUssOrbitTime(orbitTime, worldTime);
            world.markBlockForUpdate(sx, sy, sz);
        }
        int[] anchor = shipAnchorPos(base);
        TileEntity fleetTe = world.getTileEntity(anchor[0], anchor[1], anchor[2]);
        if (fleetTe instanceof TileEntityVoidcraftShip) {
            ((TileEntityVoidcraftShip) fleetTe).setUssOrbitTime(orbitTime, worldTime);
            world.markBlockForUpdate(anchor[0], anchor[1], anchor[2]);
        }
    }

    /**
     * @return the current star's satellite capacity (0 when the star is not ignited)
     */
    public long starSatelliteCapacity() {
        if (uss == null || !uss.isIgnited()) {
            return 0L;
        }
        return USSInfra.starSatelliteCapacity(USSPlanets.starRenderSize(uss.getStarSize()));
    }

    /**
     * @return the current star's satellite count (0 when there is no swarm)
     */
    public long starSatelliteCount() {
        if (uss == null) {
            return 0L;
        }
        return uss.getInfrastructure()
            .count(USSInfrastructure.DYSON_STAR_KEY);
    }

    /**
     * Debug path: add Dyson Swarm satellites to this star (no resource cost), clamped by the remaining capacity.
     *
     * @param amount the satellites to add
     * @return the satellites actually added (0 when the star is not ignited, the swarm is saturated, or the
     *         amount is non-positive)
     */
    public long debugAddDysonSatellites(long amount) {
        if (uss == null || !uss.isIgnited() || amount <= 0L) {
            return 0L;
        }
        long capacity = USSInfra.starSatelliteCapacity(USSPlanets.starRenderSize(uss.getStarSize()));
        long toAdd = Math.min(amount, capacity - starSatelliteCount());
        if (toAdd <= 0L) {
            return 0L;
        }
        uss = uss.withInfrastructure(
            uss.getInfrastructure()
                .addUnits(USSInfrastructure.DYSON_STAR_KEY, toAdd));
        syncStarRenderBlock();
        return toAdd;
    }

    /**
     * Debug path: add units to the star's non-Dyson infrastructure shell (the Stellar Injector or the Stellar
     * Gravitational Lens — no resource cost), clamped by the remaining capacity.
     *
     * @param type   the shell's type (a star-scale type)
     * @param amount the units to add
     * @return the units actually added (0 when the star is not ignited, the shell is saturated, or the amount is
     *         non-positive)
     */
    public long debugAddStarShellUnits(int type, long amount) {
        if (uss == null || !uss.isIgnited() || amount <= 0L) {
            return 0L;
        }
        String key = USSInfraBuild.key(type, USSInfraBuild.TARGET_STAR, -1);
        long toAdd = Math.min(
            amount,
            infraShellCapacity(type, USSInfraBuild.TARGET_STAR, -1)
                - infraShellCount(type, USSInfraBuild.TARGET_STAR, -1));
        if (toAdd <= 0L) {
            return 0L;
        }
        uss = uss.withInfrastructure(
            uss.getInfrastructure()
                .addUnits(key, toAdd));
        syncStarRenderBlock();
        return toAdd;
    }

    /**
     * Debug path: one cycle of the Stellar Injector's size step with no tick pacing and no resource cost — the
     * injector buffer is bypassed. Requires the star's shell to be fully built and the star below its size cap.
     *
     * @return the star's new size (0.0 when nothing was stepped)
     */
    public double debugStepInjector() {
        if (uss == null || !uss.isIgnited()) {
            return 0.0;
        }
        if (!USSInfraBuild.isBuilt(
            uss.getInfrastructure(),
            USSInfraBuild.INJECTOR,
            USSInfraBuild.TARGET_STAR,
            -1,
            infraShellCapacity(USSInfraBuild.INJECTOR, USSInfraBuild.TARGET_STAR, -1))) {
            return 0.0;
        }
        if (uss.getStarSize() + USSConstants.INJECTOR_SIZE_STEP > starSizeCap() + 1e-9) {
            return 0.0;
        }
        uss = uss.withStarSize(uss.getStarSize() + USSConstants.INJECTOR_SIZE_STEP);
        syncStarRenderBlock();
        return uss.getStarSize();
    }

    /**
     * @return the star's current size (0.0 when not ignited)
     */
    public double starSize() {
        return uss != null && uss.isIgnited() ? uss.getStarSize() : 0.0;
    }

    /**
     * @return the star's size cap — 1.5× the original sampled size (0.0 when not ignited)
     */
    public double starSizeCap() {
        if (uss == null || !uss.isIgnited()) {
            return 0.0;
        }
        return USSStellarEvolution.sizeCap(USSPlanets.sampleStarSize(uss.getStarType(), uss.getIgnitedAt()));
    }

    /**
     * @return the remaining fraction (0..1) of the star's primary material reserve (1.0 when not ignited)
     */
    public double starPrimaryFraction() {
        return uss != null && uss.isIgnited() ? uss.primaryFraction() : 1.0;
    }

    /**
     * Debug path: deplete a fraction of the star's PRIMARY material reserve — the stellar evolution's
     * {@code primaryFraction} read (no resource cost). Each primary material loses the fraction of its
     * ignition-time amount (clamped at 0); a never-siphoned star starts from the full reserve.
     *
     * @param fraction the depletion fraction (negative clamped to 0)
     * @return the units depleted across all materials (0 when not ignited or nothing left to deplete)
     */
    public long debugDepleteStarPrimary(double fraction) {
        if (uss == null || !uss.isIgnited() || fraction <= 0.0) {
            return 0L;
        }
        USSStarDefinition definition = USSStarRegistry.byType(uss.getStarType());
        if (definition == null) {
            return 0L;
        }
        VoidcraftUSS.MaterialReserve initial = VoidcraftUSS.MaterialReserve
            .fromStar(definition, USSPlanets.sampleStarSize(uss.getStarType(), uss.getIgnitedAt()));
        VoidcraftUSS.MaterialReserve current = uss.getStarFluidReserve();
        if (current == null) {
            current = initial; // a never-siphoned star starts from the full reserve
        }
        long depleted = 0L;
        VoidcraftUSS.MaterialReserve next = current;
        for (Map.Entry<Materials, Long> entry : initial.getRemaining()
            .entrySet()) {
            long amount = Math.round(entry.getValue() * fraction);
            if (amount <= 0L) {
                continue;
            }
            depleted += Math.min(amount, current.remaining(entry.getKey()));
            next = next.mine(entry.getKey(), amount);
        }
        if (depleted <= 0L) {
            return 0L;
        }
        uss = uss.withStarFluidReserve(next);
        return depleted;
    }

    /**
     * Debug path: reveal (scan) the first ripple whose Continuum Stabilizer shell is not fully built and add units
     * there (no resource cost) — the target's saturation moves the effect to the next ripple.
     *
     * @param amount the units to add (clamped by that shell's remaining capacity)
     * @return the units actually added (0 when the star is not ignited, no ripple has shell room, or the amount is
     *         non-positive)
     */
    public long debugAddStabilizerToNextRipple(long amount) {
        int index = debugStabilizerTargetRipple();
        if (index < 0 || amount <= 0L) {
            return 0L;
        }
        if (!uss.isRippleScanned(index)) {
            uss = uss.withRippleScanned(index);
        }
        long toAdd = Math.min(
            amount,
            infraShellCapacity(USSInfraBuild.STABILIZER, USSInfraBuild.TARGET_RIPPLE, index)
                - infraShellCount(USSInfraBuild.STABILIZER, USSInfraBuild.TARGET_RIPPLE, index));
        if (toAdd <= 0L) {
            return 0L;
        }
        uss = uss.withInfrastructure(
            uss.getInfrastructure()
                .addUnits(USSInfraBuild.key(USSInfraBuild.STABILIZER, USSInfraBuild.TARGET_RIPPLE, index), toAdd));
        syncFleetRenderBlock();
        return toAdd;
    }

    /**
     * @return the index of the first ripple whose Continuum Stabilizer shell is not fully built (-1 when the star
     *         is not ignited or every ripple's shell is full)
     */
    public int debugStabilizerTargetRipple() {
        if (uss == null || !uss.isIgnited()) {
            return -1;
        }
        USSRippleField field = getRippleField();
        if (field == null) {
            return -1;
        }
        return USSInfraBuild.firstIncompleteStabilizerRipple(uss.getInfrastructure(), field.rippleIndices());
    }

    /**
     * @param type  the shell's type
     * @param kind  the target kind (the star, or a ripple point by index)
     * @param index the ripple point index (ignored for the star)
     * @return the built unit count of that shell (0 when the star is not ignited or none is built)
     */
    public long infraShellCount(int type, int kind, int index) {
        if (uss == null || !uss.isIgnited()) {
            return 0L;
        }
        return uss.getInfrastructure()
            .count(USSInfraBuild.key(type, kind, index));
    }

    /**
     * @param type  the shell's type
     * @param kind  the target kind (the star, or a ripple point by index)
     * @param index the ripple point index (ignored for the star)
     * @return the build capacity of that shell (0 when the star is not ignited or the type does not build there)
     */
    public long infraShellCapacity(int type, int kind, int index) {
        if (uss == null || !uss.isIgnited()) {
            return 0L;
        }
        double renderSize = kind == USSInfraBuild.TARGET_STAR ? starShellRenderSize()
            : USSPlanets.starRenderSize(uss.getStarSize());
        return USSInfraBuild.capacity(type, kind, renderSize);
    }

    /**
     * @return the ripple points of the field (0 when the star is not ignited or the field is absent)
     */
    public int ripplePointCount() {
        if (uss == null || !uss.isIgnited()) {
            return 0;
        }
        USSRippleField field = getRippleField();
        return field == null ? 0
            : field.rippleIndices()
                .size();
    }

    /**
     * @return how many of the field's ripple points are revealed (0 when the star is not ignited or the field is
     *         absent)
     */
    public int rippleRevealedCount() {
        if (uss == null || !uss.isIgnited()) {
            return 0;
        }
        USSRippleField field = getRippleField();
        if (field == null) {
            return 0;
        }
        int revealed = 0;
        for (int index : field.rippleIndices()) {
            if (uss.isRippleScanned(index)) {
                revealed++;
            }
        }
        return revealed;
    }

    /**
     * Debug path: reveal (scan) random ripple points — up to {@code count} UNREVEALED ripples picked at random (no
     * resource cost). A point is revealed exactly once (an already-revealed point is never re-picked).
     *
     * @param count the maximum number of ripple points to reveal
     * @return the ripple points actually revealed (0 when the star is not ignited or every ripple point is already
     *         revealed)
     */
    public int debugScanRandomRipples(int count) {
        if (uss == null || !uss.isIgnited() || count <= 0) {
            return 0;
        }
        USSRippleField field = getRippleField();
        if (field == null) {
            return 0;
        }
        List<Integer> candidates = new ArrayList<Integer>();
        for (int index : field.rippleIndices()) {
            if (!uss.isRippleScanned(index)) {
                candidates.add(index);
            }
        }
        if (candidates.isEmpty()) {
            return 0;
        }
        Collections.shuffle(candidates, new Random());
        int revealed = 0;
        for (int i = 0; i < Math.min(count, candidates.size()); i++) {
            uss = uss.withRippleScanned(candidates.get(i));
            revealed++;
        }
        syncFleetRenderBlock();
        return revealed;
    }

    /**
     * Debug path: set the star's size to the injector's cap (1.5× the original sampled size — no resource cost, the
     * injector's shell and buffer requirements are bypassed).
     *
     * @return the star's new size (0.0 when the star is not ignited or already at the cap)
     */
    public double debugSetStarSizeToCap() {
        double cap = starSizeCap();
        if (cap <= 0.0 || uss.getStarSize() + 1e-9 >= cap) {
            return 0.0;
        }
        uss = uss.withStarSize(cap);
        syncStarRenderBlock();
        return uss.getStarSize();
    }

    /**
     * Debug path: set the star's remaining lifespan (no resource cost).
     *
     * @param ticks the new remaining lifespan in machine ticks
     * @return the new remaining lifespan (0 when the star is not ignited or the ticks are non-positive)
     */
    public long debugSetLifespanTicks(long ticks) {
        if (uss == null || !uss.isIgnited() || ticks <= 0L) {
            return 0L;
        }
        uss = uss.withLifespan(ticks);
        return uss.getLifespanRemaining();
    }

    /**
     * Debug path: force the star's expiry NOW (the expiry pipeline runs on the spot — evolution auto-ignites the
     * new star on the upgraded controller, or the system terminates and the controller is consumed).
     *
     * @return true when the expiry pipeline ran (false when the star is not ignited)
     */
    public boolean debugForceExpiry() {
        if (uss == null || !uss.isIgnited()) {
            return false;
        }
        starExpires();
        return true;
    }

    // endregion

    @Override
    public boolean constructStart(VoidcraftActiveShip ship, String targetKind, int targetIndex) {
        if (ship == null) {
            return false;
        }
        // The site is built at the ship hover body (the preceding MOVE).
        USSBaseAnchor anchor = USSBaseAnchor.fromMoveTarget(targetKind, targetIndex);
        if (anchor == null) {
            log(ship, "CONSTRUCT: no build anchor at this hover - skipping");
            return false;
        }
        if (getBase(anchor) != null) {
            log(ship, "CONSTRUCT: a base already stands at " + anchor + " - skipping");
            return false;
        }
        NBTTagCompound payload = ship.getPayload();
        if (payload == null || !payload.hasKey(VoidcraftNbt.TAG_BUILD_BLUEPRINT)) {
            log(ship, "CONSTRUCT: no Voidbase blueprint on board - skipping");
            return false;
        }
        NBTTagCompound bpTag = payload.getCompoundTag(VoidcraftNbt.TAG_BUILD_BLUEPRINT);
        VoidcraftBlueprint blueprint = VoidcraftNbt.readBase(bpTag);
        if (blueprint == null) {
            log(ship, "CONSTRUCT: corrupt Voidbase blueprint on board - skipping");
            return false;
        }
        String name = bpTag.hasKey(VoidcraftNbt.TAG_NAME) ? bpTag.getString(VoidcraftNbt.TAG_NAME) : "Voidbase";
        USSBaseSite site = createOrGetBaseSite(anchor, blueprint, name);
        // The base's infrastructure cargo (the dedicated hold keys) lands on the site NOW (unpaced — the
        // finished base's hold receives it at spawn) and never enters the parts transfer total.
        deliverInfraCargo(ship, site);
        // The parts this ship will actually deposit: per parts-list key, the site's remaining need capped at what
        // is on board in the hold (a part already satisfied by another Constructor is skipped and stays on board).
        CargoHold hold = ship.getHold();
        long total = 0L;
        for (String key : new ArrayList<String>(
            blueprint.partsList()
                .keySet())) {
            long need = site.remaining(key);
            if (need <= 0L) {
                continue;
            }
            total += Math.min(need, hold != null ? hold.itemsOf(key) : 0L);
        }
        if (total <= 0L) {
            // Nothing to transfer (no parts on board, or every part is already delivered): a site that just completed
            // by another ship spawns now; otherwise the site simply stands.
            if (site.isComplete()) {
                spawnBaseFromSite(site, ship);
            } else {
                log(
                    ship,
                    "CONSTRUCT: nothing to transfer at " + anchor
                        + " (site "
                        + (int) (site.progressFraction() * 100)
                        + "% complete)");
            }
            syncFleetRenderBlock();
            return true; // the command's first tick settles DONE
        }
        long power = ship.getConstructionPower();
        long perItem = USSConstants.constructTicksPerItem(power);
        site.startConstructLeg(perItem * total, perItem, ship.getSeed());
        log(
            ship,
            "CONSTRUCT: building at " + anchor
                + " ("
                + total
                + " parts, construction power "
                + power
                + ", ~"
                + (perItem * total / 20L)
                + "s)");
        syncFleetRenderBlock();
        return true;
    }

    @Override
    public boolean constructTick(VoidcraftActiveShip ship, String targetKind, int targetIndex) {
        if (ship == null) {
            return false;
        }
        USSBaseAnchor anchor = USSBaseAnchor.fromMoveTarget(targetKind, targetIndex);
        USSBaseSite site = anchor != null ? getBaseSite(anchor) : null;
        // No site, or no leg (nothing was transferred, or the site completed and the base took its place): done.
        if (site == null || site.constructLegId() <= 0) {
            return false;
        }
        if (getBase(anchor) != null || site.isComplete()) {
            // A leg started between ticks while another Constructor finished the site: spawn (a no-op when a base
            // already stands) and end this leg.
            site.finishConstructLeg();
            spawnBaseFromSite(site, ship);
            syncFleetRenderBlock();
            return false;
        }
        // The construction leg runs on the executor's energy buffer: the site's countdown (and its part
        // deposits) advance only while the buffer covers the leg's draw (the stall model).
        if (!ship.spendEnergy(USSConstants.CONSTRUCT_ENERGY_PER_TICK)) {
            return true; // stalled — the command keeps polling
        }
        site.tickConstruct();
        // One part per ticksPerItem machine ticks: the countdown (total = ticksPerItem * parts) hits a multiple
        // of the pacing on every deposit tick - including the final 0, so exactly `parts` deposits run.
        if (site.constructTicksPerItem() > 0L && site.constructTicksLeft() % site.constructTicksPerItem() == 0L) {
            depositOneBuildPart(ship, site);
        }
        if (site.isComplete()) {
            site.finishConstructLeg();
            spawnBaseFromSite(site, ship);
            log(
                ship,
                "CONSTRUCT: construction complete at " + anchor
                    + " - VOIDBASE "
                    + site.name()
                    + " standing"
                    + buildLeftoverLog(ship));
            syncFleetRenderBlock();
            return false;
        }
        if (site.constructTicksLeft() <= 0L) {
            // The leg counted down before the site filled (its parts covered less than the remaining need).
            site.finishConstructLeg();
            log(
                ship,
                "CONSTRUCT: construction leg over at " + anchor
                    + " (site "
                    + (int) (site.progressFraction() * 100)
                    + "% complete, "
                    + siteRemainingTotal(site)
                    + " parts still on the site)"
                    + buildLeftoverLog(ship));
            syncFleetRenderBlock();
            return false;
        }
        return true;
    }

    /**
     * Deposit ONE part from the ship's hold into the site: the first parts-list key the site still needs and the
     * hold still carries (unknown / saturated keys are skipped).
     */
    private void depositOneBuildPart(VoidcraftActiveShip ship, USSBaseSite site) {
        VoidcraftBlueprint blueprint = site.blueprint();
        CargoHold hold = ship.getHold();
        if (blueprint == null || hold == null) {
            return;
        }
        for (String key : new ArrayList<String>(
            blueprint.partsList()
                .keySet())) {
            if (site.remaining(key) <= 0L || hold.itemsOf(key) <= 0L) {
                continue;
            }
            ship.setHold(hold.removeItem(key, 1L));
            site.add(key, 1L);
            return;
        }
    }

    /** @return the site's parts still missing (all keys, 0 when complete) */
    private long siteRemainingTotal(USSBaseSite site) {
        long total = 0L;
        for (java.util.Map.Entry<String, Long> entry : site.blueprint()
            .partsList()
            .entrySet()) {
            total += site.remaining(entry.getKey());
        }
        return total;
    }

    /** @return the " (N parts remain on board)" log suffix ("" when the ship carries no parts of its mission) */
    private String buildLeftoverLog(VoidcraftActiveShip ship) {
        long parts = partsRemainderOnBoard(ship);
        return parts > 0L ? " (" + parts + " parts remain on board)" : "";
    }

    /**
     * The parts the constructor still carries for its mission (the parts-list keys present in its hold; 0 for a
     * ship without a blueprint mission).
     */
    private long partsRemainderOnBoard(VoidcraftActiveShip ship) {
        NBTTagCompound payload = ship.getPayload();
        if (payload == null || !payload.hasKey(VoidcraftNbt.TAG_BUILD_BLUEPRINT)) {
            return 0L;
        }
        VoidcraftBlueprint blueprint = VoidcraftNbt.readBase(payload.getCompoundTag(VoidcraftNbt.TAG_BUILD_BLUEPRINT));
        if (blueprint == null) {
            return 0L;
        }
        CargoHold hold = ship.getHold();
        if (hold == null) {
            return 0L;
        }
        long total = 0L;
        for (String key : blueprint.partsList()
            .keySet()) {
            total += hold.itemsOf(key);
        }
        return total;
    }

    // region ship-to-ship cargo transfer (SEND / TAKE — the USSPilotWorld seams)

    @Override
    public boolean cargoTransferStart(VoidcraftActiveShip ship, int commandId, String target, long amount,
        String filter) {
        if (ship == null || target == null
            || target.trim()
                .isEmpty()) {
            return false;
        }
        String label = USSCommand.label(commandId);
        if (cargoTransfers.containsKey(ship.getUuid())) {
            log(ship, label + ": a transfer is already in flight - skipping");
            return false;
        }
        String rawTarget = target.trim();
        if (USSProgramDefaults.TARGET_STAR.equalsIgnoreCase(rawTarget)) {
            return cargoTransferStartStar(ship, commandId, amount, filter);
        }
        VoidcraftActiveShip targetShip;
        if (USSProgramDefaults.TARGET_NEARBY.equalsIgnoreCase(rawTarget)) {
            targetShip = resolveNearbyFleetTarget(ship, commandId);
            if (targetShip == null) {
                String none = USSCommand.SEND == commandId
                    ? "no nearby ship with free cargo space at a shared location - skipping"
                    : "no nearby ship with cargo at a shared location - skipping";
                log(ship, label + ": " + none);
                return false;
            }
        } else {
            targetShip = resolveFleetTarget(rawTarget);
            if (targetShip == null) {
                log(ship, label + ": no ship '" + rawTarget + "' in this fleet - skipping");
                return false;
            }
        }
        if (targetShip.getUuid() != null && targetShip.getUuid()
            .equals(ship.getUuid())) {
            log(ship, label + ": the target is this ship - skipping");
            return false;
        }
        long power = ship.getLogisticsPower();
        if (power <= 0L) {
            log(ship, label + ": no logistics power (Cargo Drone Bay covers) - skipping");
            return false;
        }
        // A ship mid-MOVE is not properly at any location, even though its hover body already reads the
        // destination — the transfer waits until it settles.
        if (ship.isTraveling()) {
            log(ship, label + ": this ship is in transit - skipping");
            return false;
        }
        if (targetShip.isTraveling()) {
            log(ship, label + ": '" + targetShip.getName() + "' is in transit - skipping");
            return false;
        }
        if (!sharesLocation(ship, targetShip)) {
            log(ship, label + ": '" + targetShip.getName() + "' is not at a shared location - skipping");
            return false;
        }
        USSCargoTransferState state = new USSCargoTransferState();
        state.leg = USSCargoTransfer.arm(filter, amount, (int) USSConstants.transferTicksPerUnit(power));
        state.targetUuid = targetShip.getUuid();
        state.targetName = targetShip.getName();
        cargoTransfers.put(ship.getUuid(), state);
        String normFilter = USSCargoTransfer.normalizeFilter(filter);
        log(
            ship,
            label + ": transferring "
                + (amount < 0L ? "all" : String.valueOf(amount))
                + " units to '"
                + state.targetName
                + "' (filter '"
                + (normFilter.isEmpty() ? USSCargoTransfer.FILTER_ALL : normFilter)
                + "', logistics power "
                + power
                + ")");
        syncFleetRenderBlock(); // the new transfer beam appears as soon as it starts (no ship-state change to ride on)
        return true;
    }

    /**
     * A SEND / TAKE whose target is the STAR: the counterparty is the Stellar Injector's cargo buffer (the
     * injector's delivery point) — SEND moves the ship's cargo into the buffer, TAKE retrieves buffer cargo into
     * the ship. The ship must be at the star (its own location IS the star) and the star's Injector shell must
     * be FULLY BUILT (a shell under construction is not a delivery point).
     *
     * @param ship      the executing ship
     * @param commandId the command id ({@code USSCommand.SEND} or {@code USSCommand.TAKE})
     * @param amount    the unit limit (-1 = ALL)
     * @param filter    the material filter (null / empty / "*" = all)
     * @return true when the transfer started
     */
    private boolean cargoTransferStartStar(VoidcraftActiveShip ship, int commandId, long amount, String filter) {
        String label = USSCommand.label(commandId);
        if (!uss.isIgnited()) {
            log(ship, label + ": the star is not ignited - skipping");
            return false;
        }
        if (!USSInfraBuild.isBuilt(
            uss.getInfrastructure(),
            USSInfraBuild.INJECTOR,
            USSInfraBuild.TARGET_STAR,
            -1,
            infraShellCapacity(USSInfraBuild.INJECTOR, USSInfraBuild.TARGET_STAR, -1))) {
            log(ship, label + ": the star's Stellar Injector shell is not fully built - skipping");
            return false;
        }
        if (ship.isTraveling()) {
            log(ship, label + ": this ship is in transit - skipping");
            return false;
        }
        if (locationOf(ship).getKind() != USSLocation.Kind.STAR) {
            log(ship, label + ": this ship is not at the star - skipping");
            return false;
        }
        long power = ship.getLogisticsPower();
        if (power <= 0L) {
            log(ship, label + ": no logistics power (Cargo Drone Bay covers) - skipping");
            return false;
        }
        USSCargoTransferState state = new USSCargoTransferState();
        state.leg = USSCargoTransfer.arm(filter, amount, (int) USSConstants.transferTicksPerUnit(power));
        state.starTarget = true;
        state.targetName = "the star's injector buffer";
        cargoTransfers.put(ship.getUuid(), state);
        String normFilter = USSCargoTransfer.normalizeFilter(filter);
        log(
            ship,
            label + ": transferring "
                + (amount < 0L ? "all" : String.valueOf(amount))
                + " units to "
                + state.targetName
                + " (filter '"
                + (normFilter.isEmpty() ? USSCargoTransfer.FILTER_ALL : normFilter)
                + "', logistics power "
                + power
                + ")");
        syncFleetRenderBlock();
        return true;
    }

    @Override
    public boolean cargoTransferTick(VoidcraftActiveShip ship, int commandId) {
        if (ship == null) {
            return false;
        }
        USSCargoTransferState state = cargoTransfers.get(ship.getUuid());
        if (state == null) {
            return false; // nothing in flight (a mid-transfer chunk reload degrades gracefully to DONE)
        }
        String label = USSCommand.label(commandId);
        if (state.starTarget) {
            // The star cannot leave: the transfer ends when the EXECUTING ship leaves the star or goes in transit.
            if (ship.isTraveling() || locationOf(ship).getKind() != USSLocation.Kind.STAR) {
                cargoTransfers.remove(ship.getUuid());
                log(
                    ship,
                    label + ": the ship left the star - transfer over (" + state.leg.transferred() + " units moved)");
                syncFleetRenderBlock();
                return false;
            }
            if (!ship.spendEnergy(ship.getLogisticsPower())) {
                return true; // stalled — keep polling
            }
            // SEND moves ship -> buffer; TAKE the inverse (the CALLER owns the direction).
            CargoHold source = commandId == USSCommand.SEND ? ship.getHold() : uss.getInjectorBuffer();
            CargoHold dest = commandId == USSCommand.SEND ? uss.getInjectorBuffer() : ship.getHold();
            if (source == null || dest == null) {
                cargoTransfers.remove(ship.getUuid());
                log(ship, label + ": no cargo hold - transfer over (" + state.leg.transferred() + " units moved)");
                syncFleetRenderBlock();
                return false;
            }
            USSCargoTransfer.Result result = state.leg.tick(source, dest);
            if (result.source != null) {
                if (commandId == USSCommand.SEND) {
                    ship.setHold(result.source);
                    ship.setCargo(USSShipCargo.cargoFromHold(result.source));
                } else {
                    uss = uss.withInjectorBuffer(result.source);
                }
            }
            if (result.target != null) {
                if (commandId == USSCommand.SEND) {
                    uss = uss.withInjectorBuffer(result.target);
                } else {
                    ship.setHold(result.target);
                    ship.setCargo(USSShipCargo.cargoFromHold(result.target));
                }
            }
            if (result.running) {
                return true;
            }
            cargoTransfers.remove(ship.getUuid());
            log(
                ship,
                label + ": transfer over ("
                    + state.leg.transferred()
                    + " units moved"
                    + (result.reason == null ? "" : " - " + result.reason)
                    + ")");
            syncFleetRenderBlock();
            return false;
        }
        VoidcraftActiveShip target = findFleetShip(state.targetUuid);
        if (target == null) {
            cargoTransfers.remove(ship.getUuid());
            log(
                ship,
                label + ": '"
                    + state.targetName
                    + "' is no longer in the fleet - transfer over ("
                    + state.leg.transferred()
                    + " units moved)");
            syncFleetRenderBlock(); // the transfer beam disappears as soon as it ends
            return false;
        }
        if (!sharesLocation(ship, target)) {
            cargoTransfers.remove(ship.getUuid());
            log(
                ship,
                label + ": '"
                    + target.getName()
                    + "' left the shared location - transfer over ("
                    + state.leg.transferred()
                    + " units moved)");
            syncFleetRenderBlock(); // the transfer beam disappears as soon as it ends
            return false;
        }
        // The shared location can still READ as shared while a ship is en route (its hover body reads the
        // destination) — a started MOVE leg ends the transfer.
        if (ship.isTraveling() || target.isTraveling()) {
            cargoTransfers.remove(ship.getUuid());
            log(ship, label + ": a ship is in transit - transfer over (" + state.leg.transferred() + " units moved)");
            syncFleetRenderBlock(); // the transfer beam disappears as soon as it ends
            return false;
        }
        // The transfer runs on the executor's energy buffer: a unit only moves while the buffer covers the
        // executor's logistics-power draw (the stall model).
        if (!ship.spendEnergy(ship.getLogisticsPower())) {
            return true; // stalled — keep polling
        }
        // SEND moves executing -> target; TAKE the inverse (the CALLER owns the direction).
        VoidcraftActiveShip sourceShip = commandId == USSCommand.SEND ? ship : target;
        VoidcraftActiveShip destShip = commandId == USSCommand.SEND ? target : ship;
        USSCargoTransfer.Result result = state.leg.tick(sourceShip.getHold(), destShip.getHold());
        if (result.source != null) {
            sourceShip.setHold(result.source);
            sourceShip.setCargo(USSShipCargo.cargoFromHold(result.source));
        }
        if (result.target != null) {
            destShip.setHold(result.target);
            destShip.setCargo(USSShipCargo.cargoFromHold(result.target));
        }
        if (result.running) {
            return true;
        }
        cargoTransfers.remove(ship.getUuid());
        log(
            ship,
            label + ": transfer over ("
                + state.leg.transferred()
                + " units moved"
                + (result.reason == null ? "" : " - " + result.reason)
                + ")");
        syncFleetRenderBlock(); // the transfer beam disappears as soon as it ends
        return false;
    }

    /**
     * Resolve a fleet target for a SEND / TAKE: a pure non-negative int in the fleet's index range = a fleet
     * slot (launch order); anything else = a case-insensitive ship name (first match).
     *
     * @param target the raw target param (already trimmed)
     * @return the target ship (null when unresolvable)
     */
    private VoidcraftActiveShip resolveFleetTarget(String target) {
        try {
            int index = Integer.parseInt(target);
            if (index >= 0 && index < activeShips.size()) {
                return activeShips.get(index);
            }
            return null;
        } catch (NumberFormatException ignored) {
            // not an index - fall through to a name match
        }
        for (VoidcraftActiveShip s : activeShips) {
            if (s != null && s.getName()
                .equalsIgnoreCase(target)) {
                return s;
            }
        }
        return null;
    }

    /**
     * The {@code NEARBY} transfer target ({@link USSProgramDefaults#TARGET_NEARBY}): the first fleet ship in
     * fleet order that is not in transit, shares the executing ship's location, and is a viable counterparty —
     * a hold with cargo for {@code TAKE} (it is the source), free hold capacity for {@code SEND} (it is the
     * sink).
     *
     * @param ship      the executing ship
     * @param commandId the command id ({@code USSCommand.SEND} or {@code USSCommand.TAKE})
     * @return the target ship (null when no candidate qualifies)
     */
    private VoidcraftActiveShip resolveNearbyFleetTarget(VoidcraftActiveShip ship, int commandId) {
        for (VoidcraftActiveShip s : activeShips) {
            if (s == null || s == ship || s.isTraveling()) {
                continue;
            }
            CargoHold hold = s.getHold();
            if (hold == null) {
                continue;
            }
            long units = USSCommand.SEND == commandId ? hold.remainingUnits() : hold.usedUnits();
            if (units <= 0L) {
                continue;
            }
            if (!sharesLocation(ship, s)) {
                continue;
            }
            return s;
        }
        return null;
    }

    /** @return the in-flight ship with the given uuid (null when none) */
    private VoidcraftActiveShip findFleetShip(String uuid) {
        if (uuid == null) {
            return null;
        }
        for (VoidcraftActiveShip s : activeShips) {
            if (s != null && uuid.equals(s.getUuid())) {
                return s;
            }
        }
        return null;
    }

    /**
     * The ship's EFFECTIVE point for location checks: its leg's destination while a WORK leg runs (a working
     * ship hovers at the destination; the server position lags at the departure point until the leg
     * completes), its position otherwise.
     */
    private USSPosition effectivePoint(VoidcraftActiveShip ship) {
        if (ship == null) {
            return null;
        }
        if (ship.isLegActive() && USSWorkKind.isWork(ship.getLegWorkKind())) {
            return ship.getDestination();
        }
        return ship.getPosition();
    }

    /**
     * The ship's LOCATION (the shared-location rule): its body descriptor + effective point, with the fleet
     * snapshot (every in-flight ship's position) for the rendezvous scan.
     */
    private USSLocation locationOf(VoidcraftActiveShip ship) {
        if (ship == null) {
            return USSLocation.none();
        }
        List<USSLocation.Entry> fleet = new ArrayList<USSLocation.Entry>(activeShips.size());
        for (VoidcraftActiveShip s : activeShips) {
            if (s != null) {
                fleet.add(new USSLocation.Entry(s.getUuid(), s.getPosition()));
            }
        }
        return USSLocation.of(ship.isBodyStatic(), ship.getTargetPlanet(), effectivePoint(ship), fleet, ship.getUuid());
    }

    /** @return true when the two ships share a location (the SEND / TAKE co-location rule) */
    private boolean sharesLocation(VoidcraftActiveShip a, VoidcraftActiveShip b) {
        return USSLocation.shared(effectivePoint(a), locationOf(a), effectivePoint(b), locationOf(b));
    }

    // endregion

    /**
     * Spawn the finished Voidbase from a completed site (the base payload is the ship's mission blueprint - the
     * site's re-encoded blueprint when the ship carries no tag; the base stands at the anchor's band point).
     */
    private void spawnBaseFromSite(USSBaseSite site, VoidcraftActiveShip ship) {
        USSBaseAnchor anchor = site.anchor();
        NBTTagCompound payload = ship != null ? ship.getPayload() : null;
        NBTTagCompound bpTag = (payload != null && payload.hasKey(VoidcraftNbt.TAG_BUILD_BLUEPRINT))
            ? payload.getCompoundTag(VoidcraftNbt.TAG_BUILD_BLUEPRINT)
            : siteBlueprintPayload(site);
        String uuid = bpTag.getString(VoidcraftNbt.TAG_UUID);
        if (uuid.isEmpty()) {
            uuid = ItemVoidbaseBlueprint.newUuid();
            bpTag.setString(VoidcraftNbt.TAG_UUID, uuid);
        }
        USSPosition hover = anchorHoverPoint(anchor);
        VoidcraftActiveShip entity = VoidcraftActiveShip
            .spawnBase(uuid, site.name(), bpTag, anchor, ship != null ? ship.getSeed() : 0, hover);
        spawnBase(entity);
    }

    /** The site's blueprint re-encoded as a base payload tag (the fallback when the completing ship carries none). */
    private NBTTagCompound siteBlueprintPayload(USSBaseSite site) {
        NBTTagCompound payload = new NBTTagCompound();
        // The uuid slot stays empty — spawnBaseFromSite mints (and persists) the base identity.
        VoidcraftNbt.write(payload, site.blueprint(), "", site.name(), site.createdAt());
        return payload;
    }

    @Override
    public boolean repairStart(VoidcraftActiveShip ship, String target) {
        if (ship == null || !ship.isBase()) {
            return false; // a repair bay is a station capability (a flying ship has no bay)
        }
        if (repairs.containsKey(ship.getUuid())) {
            log(ship, "REPAIR: a repair is already in flight - skipping");
            return false;
        }
        String raw = (target == null) ? "" : target.trim();
        VoidcraftActiveShip targetShip;
        if (raw.isEmpty() || USSCommandRepair.TARGET_SELF.equalsIgnoreCase(raw)) {
            targetShip = ship; // the station repairs itself
        } else {
            targetShip = resolveFleetTarget(raw); // fleet index or name, like SEND / TAKE
        }
        if (targetShip == null) {
            log(ship, "REPAIR: no fleet member '" + raw + "' to repair - skipping");
            return false;
        }
        if (targetShip != ship && !sharesLocation(ship, targetShip)) {
            log(ship, "REPAIR: '" + targetShip.getName() + "' is not at a shared location - skipping");
            return false;
        }
        if (targetShip.getIntegrity() >= targetShip.maxIntegrity()) {
            log(ship, "REPAIR: '" + targetShip.getName() + "' is at full integrity - skipping");
            return false;
        }
        if (ship.getEnergy() < USSConstants.REPAIR_DRAW && ship.getEnergyGen() < USSConstants.REPAIR_DRAW) {
            log(ship, "REPAIR: not enough energy for the " + USSConstants.REPAIR_DRAW + " EU draw - skipping");
            return false;
        }
        USSRepairState state = new USSRepairState();
        state.ticks = 0;
        state.targetUuid = targetShip.getUuid();
        repairs.put(ship.getUuid(), state);
        log(
            ship,
            "REPAIR: repairing '" + targetShip.getName()
                + "' ("
                + targetShip.getIntegrity()
                + "/"
                + targetShip.maxIntegrity()
                + " integrity, one per "
                + USSConstants.REPAIR_DRAW
                + " EU)");
        return true;
    }

    @Override
    public boolean repairTick(VoidcraftActiveShip ship) {
        if (ship == null) {
            return false;
        }
        USSRepairState state = repairs.get(ship.getUuid());
        if (state == null) {
            return false; // nothing in flight (the command DONEs)
        }
        VoidcraftActiveShip target = findFleetShip(state.targetUuid);
        if (target == null) {
            repairs.remove(ship.getUuid());
            log(ship, "REPAIR: the target left the fleet - repair over");
            return false;
        }
        if (target != ship && !sharesLocation(ship, target)) {
            repairs.remove(ship.getUuid());
            log(ship, "REPAIR: '" + target.getName() + "' left the shared location - repair over");
            return false;
        }
        if (target.getIntegrity() >= target.maxIntegrity()) {
            repairs.remove(ship.getUuid());
            log(ship, "REPAIR: '" + target.getName() + "' is at full integrity - repair over");
            return false;
        }
        state.ticks++;
        if (state.ticks >= VoidcraftActiveShip.TICKS_PER_INTEGRITY) {
            state.ticks = 0;
            // One integrity per second, the draw paid at the boundary: a buffer that cannot cover it stalls
            // this second (the draw retries at the next boundary).
            if (ship.spendEnergy(USSConstants.REPAIR_DRAW)) {
                target.repair(1);
            }
        }
        return target.getIntegrity() < target.maxIntegrity();
    }

    @Override
    public boolean stabilizeStart(VoidcraftActiveShip ship, long ticks) {
        if (ship == null || !ship.isBase()) {
            return false; // the matrix is a station capability (a flying ship carries no matrix)
        }
        if (stabilizes.containsKey(ship.getUuid())) {
            log(ship, "STABILIZE: a stabilization window is already in flight - skipping");
            return false;
        }
        VoidcraftBlueprint blueprint = VoidcraftNbt.readBase(ship.getPayload());
        if (blueprint == null || blueprint.count(VoidcraftComponent.STABILIZATION_MATRIX) <= 0) {
            log(ship, "STABILIZE: no Stabilization Matrix in the blueprint - skipping");
            return false;
        }
        USSBaseAnchor anchor = ship.getAnchor();
        if (anchor == null || !anchor.isRipple()) {
            log(ship, "STABILIZE: the base is not anchored to a ripple - skipping");
            return false;
        }
        int index = anchor.index();
        USSRippleField field = getRippleField();
        if (uss == null || field == null
            || index < 0
            || index >= field.size()
            || !uss.isRippleScanned(index)
            || !field.isRipple(index)) {
            log(ship, "STABILIZE: the anchor ripple is not revealed - skipping");
            return false;
        }
        long capacity = USSInfraBuild.rippleCapacity();
        long built = uss.getInfrastructure()
            .count(USSInfraBuild.key(USSInfraBuild.STABILIZER, USSInfraBuild.TARGET_RIPPLE, index));
        if (capacity <= 0L || built < capacity) {
            log(ship, "STABILIZE: no built Continuum Stabilizer on the anchor ripple - skipping");
            return false;
        }
        CargoHold hold = ship.getHold();
        long umv = hold != null ? hold.itemsOf(USSConstants.FIELD_GENERATOR_UMV) : 0L;
        long uxv = hold != null ? hold.itemsOf(USSConstants.FIELD_GENERATOR_UXV) : 0L;
        if (!USSStabilize.hasFieldGenerators(umv, uxv)) {
            log(ship, "STABILIZE: no Field Generators on board - skipping");
            return false;
        }
        USSStabilize.Session session = new USSStabilize.Session();
        session.ticks = ticks;
        session.fieldGeneratorTicks = USSConstants.STABILIZE_FIELD_GENERATOR_INTERVAL_TICKS;
        session.weight = 0;
        stabilizes.put(ship.getUuid(), session);
        log(
            ship,
            "STABILIZE: stabilizing the anchor ripple for " + ticks
                + " ticks (one Field Generator per "
                + USSConstants.STABILIZE_FIELD_GENERATOR_INTERVAL_TICKS
                + " ticks)");
        return true;
    }

    @Override
    public boolean stabilizeTick(VoidcraftActiveShip ship) {
        if (ship == null) {
            return false;
        }
        USSStabilize.Session session = stabilizes.get(ship.getUuid());
        if (session == null) {
            return false; // nothing in flight (the command DONEs)
        }
        CargoHold hold = ship.getHold();
        long umv = hold != null ? hold.itemsOf(USSConstants.FIELD_GENERATOR_UMV) : 0L;
        long uxv = hold != null ? hold.itemsOf(USSConstants.FIELD_GENERATOR_UXV) : 0L;
        boolean paid = ship.spendEnergy(USSConstants.STABILIZE_ENERGY_PER_TICK);
        USSStabilize.TickResult result = USSStabilize.tick(session, paid, umv, uxv);
        if (result.consumeUxv) {
            ship.setHold(
                ship.getHold()
                    .removeItem(USSConstants.FIELD_GENERATOR_UXV, 1L));
        } else if (result.consumeUmv) {
            ship.setHold(
                ship.getHold()
                    .removeItem(USSConstants.FIELD_GENERATOR_UMV, 1L));
        }
        if (!result.running) {
            stabilizes.remove(ship.getUuid());
            log(ship, "STABILIZE: stabilization window complete (weight " + session.weight + ")");
            return false;
        }
        return true;
    }

    /**
     * The expiry weight read (the matrix's effective weight at star expiry): the sum of the last-consumed Field
     * Generator weights over ALL live STABILIZE windows (multiple bases stabilize concurrently; the weights sum).
     */
    private int stabilizeWeightSum() {
        int sum = 0;
        for (USSStabilize.Session session : stabilizes.values()) {
            if (session != null) {
                sum += session.weight;
            }
        }
        return sum;
    }

    // endregion

    /**
     * Mission complete for ONE ship (slot) — the ship SURVIVED its integrity time limit (it is here with
     * integrity still &gt; 0): the mission delivers its cargo — whatever the ship's hold still carries (a
     * construction mission's parts leftover rides it too) — to ITS OWN bay (captured at launch).
     * Then: re-emit the surviving ship into ITS OWN gateway's output bus (or drop it at the USS when the bus
     * cannot absorb it) — its integrity is back at maximum for the next flight (the item carries the blueprint
     * full integrity). The fleet anchor is resynced by the CALLER (one push for the whole fleet). Other ships in
     * flight are untouched.
     */
    private void completeShip(int slot) {
        if (slot < 0 || slot >= activeShips.size()) {
            return;
        }
        VoidcraftActiveShip completedShip = activeShips.get(slot);
        activeShips.remove(slot);
        if (slot < pilots.size()) {
            pilots.remove(slot);
        }
        stabilizes.remove(completedShip.getUuid());
        lastPushedShipStates[slot] = -1;
        lastPushedLegIds[slot] = -1;

        String shipName = completedShip.getName();
        // The cargo delivered is whatever the ship's hold still carries at mission end (the mining / starlifting
        // haul, plus the parts a constructor's mission left behind).
        NBTTagCompound cargo = USSShipCargo.cargoFromHold(completedShip.getHold());
        NBTTagList items = USSShipCargo.readItems(cargo);
        NBTTagList fluids = USSShipCargo.readFluids(cargo);
        long integrity = completedShip.getIntegrity(); // > 0 — the ship made it back inside its time limit
        ItemStack shipItem = rebuildShipItem(completedShip);

        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return;
        }
        World world = base.getWorld();
        float dropX = base.getXCoord() + 0.5f;
        float dropY = base.getYCoord() + 0.5f;
        float dropZ = base.getZCoord() + 0.5f;

        if (items.tagCount() > 0 || fluids.tagCount() > 0) {
            // Cargo → the bay captured at launch; if the bay is gone, drop the cargo at the USS (no silent loss).
            MTEVoidcraftStorageBay bay = mteAt(world, completedShip.getBayPos(), MTEVoidcraftStorageBay.class);
            if (bay != null && bay.mMachine) {
                bay.deliver(items);
                if (fluids.tagCount() > 0) {
                    bay.deliverFluids(fluids); // Phase 4 pass 1: Starlifter fluid cargo through the same three-tier
                                               // contract
                }
            } else {
                List<ItemStack> stacks = new ArrayList<>();
                for (int i = 0; i < items.tagCount(); i++) {
                    NBTTagCompound entry = items.getCompoundTagAt(i);
                    if (entry == null) continue;
                    ItemStack stack = ItemStack.loadItemStackFromNBT(entry);
                    if (stack != null) {
                        stacks.add(stack);
                    }
                }
                if (!stacks.isEmpty()) {
                    GTUtility.dropItemsOrClusters(world, dropX, dropY, dropZ, stacks);
                }
                if (fluids.tagCount() > 0) {
                    // A fluid cannot be dropped as an entity — the bay (a required launch target) is gone, so this
                    // is the one loud-loss escape hatch (the player is told, the cargo does not vanish silently).
                    try {
                        LOGGER.warn(
                            "[Voidcraft] Mission complete but the storage bay is gone: {} unit(s) of fluid cargo lost",
                            fluids.tagCount());
                    } catch (Throwable ignored) {}
                }
            }
        }

        MTEVoidcraftGateway gateway = mteAt(world, completedShip.getGatewayPos(), MTEVoidcraftGateway.class);

        // The ship SURVIVED (integrity > 0 at completion) → back into ITS OWN gateway's output bus, with its
        // integrity back at maximum for the next flight. Whatever the bus cannot absorb (no gateway, no output
        // bus, or a full buffer) drops at the USS instead of being lost.
        if (shipItem != null) {
            if (gateway != null && gateway.mMachine) {
                gateway.outputItem(shipItem);
            }
            if (shipItem.stackSize > 0) {
                GTUtility
                    .dropItemsOrClusters(world, dropX, dropY, dropZ, java.util.Collections.singletonList(shipItem));
            }
        }

        updateSlots();
        String shipOutcome = shipItem == null ? "no ship item to return"
            : shipItem.stackSize > 0 ? "ship dropped at the USS (output bus full)"
                : "ship returned to the gateway output bus";
        try {
            LOGGER.info(
                "[Voidcraft] USS mission complete (slot {}): ship '{}' survived (integrity {}s left), cargo {} items, {}",
                slot,
                shipName,
                integrity,
                items.tagCount(),
                shipOutcome);
        } catch (Throwable ignored) {}
    }

    /**
     * Give up ALL missions in flight without delivering (star burnout / structure teardown: every ship is lost,
     * the Voidbases and their construction sites with it, the fleet anchor removed).
     */
    private void discardAllShips() {
        activeShips.clear();
        pilots.clear();
        Arrays.fill(lastPushedShipStates, -1);
        Arrays.fill(lastPushedLegIds, -1);
        fleetDirty = false;
        baseSites.clear();
        cargoTransfers.clear();
        repairs.clear();
        stabilizes.clear();
        satelliteLaunchCountdowns.clear();
        infraBuildCountdowns.clear();
        syncFleetRenderBlock(); // empty fleet → the anchor block is cleared
    }

    /**
     * Rebuild the ship item from the payload captured at launch (the payload IS the item NBT written by the
     * assembler).
     */
    private ItemStack rebuildShipItem(VoidcraftActiveShip ship) {
        NBTTagCompound payload = ship.getPayload();
        if (payload == null) {
            return null;
        }
        ItemStack item = new ItemStack(ItemVoidcraft.INSTANCE);
        item.setTagCompound((NBTTagCompound) payload.copy());
        return item;
    }

    /**
     * The MTE of the given type at a world position (null-safe).
     */
    private <T> T mteAt(World world, int[] pos, Class<T> type) {
        if (world == null || pos == null) {
            return null;
        }
        IMetaTileEntity mte = GTUtility.getMetaTileEntity(world.getTileEntity(pos[0], pos[1], pos[2]));
        if (mte == null) {
            return null;
        }
        return type.isInstance(mte) ? type.cast(mte) : null;
    }

    /**
     * Pick a still-hidden (unscanned) ripple point for an Explorer (the Explorer pass): a random point index (0..342)
     * that is NOT yet in the system's scanned set. {@code -1} when every point is already revealed (nothing left to
     * scan) or the field is absent.
     *
     * @return an unscanned ripple point index, or {@code -1} when none remain
     */
    private int pickUnscannedRipplePoint() {
        USSRippleField field = getRippleField();
        if (field == null || uss == null) {
            return -1;
        }
        int size = field.size();
        // Collect the unscanned indices (all-but-the-revealed) and pick one at random.
        java.util.List<Integer> unscanned = new java.util.ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            if (!uss.isRippleScanned(i)) {
                unscanned.add(i);
            }
        }
        if (unscanned.isEmpty()) {
            return -1;
        }
        return unscanned.get(new Random().nextInt(unscanned.size()));
    }

    /**
     * A planet's work point in the solar system (programming framework, Phase C — used by
     * {@link #resolveTarget}): a random SPHERICAL-SHELL point around the planet's LIVE position ("a random target
     * position around the orbit of the planet, within a spherical shell — not only above it, but on any side").
     *
     * <p>
     * Deterministic in {@code seed} (the ship's per-launch seed) — the server's destination and the client's
     * render agree on the same point.
     *
     * @param planet the planet index into {@link #getPlanets()}
     * @param seed   the ship's per-launch seed
     * @return the destination, or null when the planet is absent
     */
    private USSPosition destinationFor(int planet, int seed) {
        List<USSPlanets.USSPlanet> planets = getPlanets();
        if (planets == null || planet < 0 || planet >= planets.size()) {
            return null; // out of range (defensive) — the caller reports an unresolvable target
        }
        USSPlanets.USSPlanet world = planets.get(planet);
        float time = worldTimeTicks();
        float starSize = USSPlanets.starRenderSize(uss.getStarSize());
        USSPosition planetCenter = USSFleetOrbit.planetPosition(world, starSize, time);
        // The hover distance: the planet's rendered radius (scale · 0.375, the legacy EoH body half) + 0.5, so the
        // ship hovers just off the planet's surface on ANY side (the shell radius).
        double hoverRadius = 0.5 + 0.375 * world.scale;
        return USSFleetOrbit.shellPoint(planetCenter, hoverRadius, seed);
    }

    /**
     * The HOVER POINT of the anchor — where a Voidbase (site and base alike) stands: a star is the fixed star
     * position; a planet i is the deterministic EQUATORIAL-BAND point around its orbit (the station never floats
     * directly above the planet — it sits within ±30° of the orbital plane at the same hover radius a ship
     * hovers at, seeded by the planet index so site + base share one point); a ripple j is the fixed grid
     * point. Ships keep their own hover law ({@link #destinationFor}) — this is the base law.
     *
     * @param anchor the anchor
     * @return the hover point, or null when the anchor is absent (out of range / system cold)
     */
    private USSPosition anchorHoverPoint(USSBaseAnchor anchor) {
        if (anchor == null || uss == null || !uss.isIgnited()) {
            return null;
        }
        if (anchor.isStar()) {
            return USSFleetOrbit.starPosition();
        }
        if (anchor.isPlanet()) {
            int index = anchor.index();
            List<USSPlanets.USSPlanet> planets = getPlanets();
            if (planets == null || index < 0 || index >= planets.size()) {
                return null; // out of range (defensive) — the caller treats the base as unanchored
            }
            USSPlanets.USSPlanet planet = planets.get(index);
            float starSize = USSPlanets.starRenderSize(uss.getStarSize());
            USSPosition planetCenter = USSFleetOrbit.planetPosition(planet, starSize, worldTimeTicks());
            double hoverRadius = 0.5 + 0.375 * planet.scale; // the same hover distance a ship keeps off the surface
            return USSFleetOrbit
                .orbitalBandPoint(planetCenter, hoverRadius, index, (float) planet.xAngle, (float) planet.zAngle);
        }
        // A ripple: the fixed grid point of the field.
        USSRippleField field = getRippleField();
        if (field == null || !field.isRipple(anchor.index())) {
            return null;
        }
        return field.positionOf(anchor.index());
    }

    /**
     * The shared orbit clock in TICKS for the orbit math — the USS's virtual orbit clock (it advances +1 per
     * machine tick and proportionally faster during a stellar-acceleration second). The SAME time base the client
     * render and {@code USSFleetOrbit.planetAnchorPosition} expect (the client renders at the synced
     * {@code ussOrbitTime} advanced from the last sync, + partialTicks — see the render TEs). The total world tick
     * count is NOT usable: it does not include the acceleration seconds, so the server's planet positions would
     * drift off the rendered ones while the star is being accelerated.
     */
    private float worldTimeTicks() {
        try {
            if (uss != null && uss.isIgnited()) {
                return (float) uss.getVirtualTime();
            }
        } catch (Throwable ignored) {}
        return 0.0f;
    }

    /**
     * The fleet-hologram anchor (Phase 4 pass 5 — ONE per USS, not per ship): the EoH render position, 32 behind
     * the controller (pass 12: the legacy 16 offset scaled 2× with the 65×65×65 shape), plus two blocks up so it
     * does not collide with the star render block. The WHOLE fleet (dozens–hundreds of ships) lives in this one
     * block's TE as an entry list; the ships themselves hover at their per-mission target CLIENT-side (see
     * {@link USSFleetOrbit}), so no per-ship world blocks are needed anymore.
     */
    private int[] shipAnchorPos(IGregTechTileEntity base) {
        ForgeDirection back = getExtendedFacing().getRelativeBackInWorld();
        return new int[] { (int) (base.getXCoord() + 32 * back.offsetX),
            (int) (base.getYCoord() + 32 * back.offsetY) + 2, (int) (base.getZCoord() + 32 * back.offsetZ) };
    }

    private static int[] rel(int[] from, int[] to) {
        if (to == null) {
            return new int[] { 0, 0, 0 };
        }
        return new int[] { to[0] - from[0], to[1] - from[1], to[2] - from[2] };
    }

    /**
     * Build the fleet entry list for the render TE: one entry per ship in flight — payload (blueprint + stats +
     * UUID) + state + seed (the per-launch identity key, pass 5.1) + mission target (pass 7) + the
     * anchor-relative gateway (each mission routes back to its own launcher/bay, Phase 4 pass 4).
     *
     * <p>
     * Pass 7: the DESTINATION is no longer in the entry — it is derived CLIENT-side from the target: a planet
     * index → that planet's live rendered position (the system specs + star size ride along in the TE, see
     * {@link #syncFleetRenderBlock}); {@code -1} → the star center (Starlifters hover 2.5 above it). The
     * per-ship swarm spread around the hover point is computed CLIENT-side from the seed
     * ({@link USSFleetOrbit}).
     */
    private List<NBTTagCompound> buildFleetEntries(IGregTechTileEntity base) {
        List<NBTTagCompound> entries = new ArrayList<NBTTagCompound>(activeShips.size());
        if (base == null) {
            return entries;
        }
        int[] anchor = shipAnchorPos(base);
        for (VoidcraftActiveShip ship : activeShips) {
            // Anchored Voidbases have their own entry list (buildBaseEntries) — the ship list stays flying-ships-only.
            if (ship.getAnchor() != null) {
                continue;
            }
            NBTTagCompound payload = ship.getPayload();
            if (payload == null) {
                continue;
            }
            NBTTagCompound entry = new NBTTagCompound();
            entry.setTag(TileEntityVoidcraftShip.TAG_ENTRY_PAYLOAD, payload);
            entry.setInteger(
                TileEntityVoidcraftShip.TAG_ENTRY_STATE,
                ship.getState()
                    .getId());
            // Pass 5.1: per-launch identity — the client keys this ship's animation phase + swarm spread on it
            // (duplicated ship items share the item UUID; the seed does not).
            entry.setInteger(TileEntityVoidcraftShip.TAG_ENTRY_SEED, ship.getSeed());
            // Pass 7: the mission target — a planet index, a ripple-point index (the Explorer pass), or -1 (the
            // star). The client resolves it against the system specs this TE carries.
            entry.setInteger(TileEntityVoidcraftShip.TAG_ENTRY_TARGET, ship.getTargetPlanet());
            // The Explorer pass: the ship's RESOLVED destination (the planet shell point, the star center, or the
            // ripple point's position). The client uses it directly — an Explorer's target is a ripple-point index,
            // not a planet index, so the client's targetBody() cannot resolve it (the server already did).
            if (ship.getDestination() != null) {
                NBTTagCompound destTag = new NBTTagCompound();
                ship.getDestination()
                    .writeToNBT(destTag);
                entry.setTag(TileEntityVoidcraftShip.TAG_ENTRY_DEST, destTag);
            }
            // Pass 26 (the travel-time rendering fix): the ACTUAL travel distance, so the client animates each
            // OUTBOUND/RETURNING leg for the ship's real duration instead of the minimum floor (the client used to
            // read vc_tdist off the payload, where it was never written → distance 0 → minimum time).
            entry.setDouble(TileEntityVoidcraftShip.TAG_ENTRY_TDIST, ship.getTravelDistance());
            // Phase C (the programming framework): the ship's CURRENT position (fleet-anchor coordinates — its
            // launch origin, then the last leg's endpoint). The client renders the ship EXACTLY here while it
            // HOLDS (a fresh ship at the gateway, or a finished program at its last body).
            if (ship.getPosition() != null) {
                NBTTagCompound posTag = new NBTTagCompound();
                ship.getPosition()
                    .writeToNBT(posTag);
                entry.setTag(TileEntityVoidcraftShip.TAG_ENTRY_POS, posTag);
            }
            // Phase C: static hover — true when the ship's hover body is a FIXED point (ripple / ship rendezvous)
            // and the client must hover the resolved DESTINATION exactly (not the body's live position).
            entry.setBoolean(TileEntityVoidcraftShip.TAG_ENTRY_STATIC, ship.isBodyStatic());
            // Phase C: the leg identity — the client resets its leg-progress phase when this changes, so legs of
            // the SAME state (MOVE → MOVE) animate from their own start.
            entry.setInteger(TileEntityVoidcraftShip.TAG_ENTRY_LEG_ID, ship.getLegId());
            // The command-split pass: the current leg's WORK KIND (owned by the work command) — the client
            // derives the SAME work-leg duration the server ticks.
            entry.setInteger(TileEntityVoidcraftShip.TAG_ENTRY_WORK_KIND, ship.getLegWorkKind());
            int[] gatewayWorld = ship.getGatewayPos() != null ? ship.getGatewayPos()
                : new int[] { anchor[0], anchor[1], anchor[2] };
            entry.setIntArray(TileEntityVoidcraftShip.TAG_ENTRY_GW_REL, rel(anchor, gatewayWorld));
            entries.add(entry);
        }
        return entries;
    }

    /**
     * The in-flight ship-to-ship cargo transfers (SEND / TAKE) for the fleet render anchor: one entry per transfer,
     * pairing the executing ship's uuid with the target ship's uuid (the client draws a gray beam between the two
     * rendered ships). A transfer whose executing or target ship is no longer in the fleet is skipped here; the
     * executing ship's own completion/loss drops it on the next transfer tick.
     */
    private List<NBTTagCompound> buildTransferEntries() {
        List<NBTTagCompound> entries = new ArrayList<NBTTagCompound>();
        for (Map.Entry<String, USSCargoTransferState> e : cargoTransfers.entrySet()) {
            USSCargoTransferState state = e.getValue();
            if (state == null) {
                continue;
            }
            VoidcraftActiveShip source = findFleetShip(e.getKey());
            if (source == null) {
                continue;
            }
            NBTTagCompound entry = new NBTTagCompound();
            entry.setString(TileEntityVoidcraftShip.TAG_TRANSFER_SOURCE, e.getKey());
            if (state.starTarget) {
                // The star's injector buffer: the client resolves the endpoint to the star center itself.
                entry.setString(TileEntityVoidcraftShip.TAG_TRANSFER_TARGET, "");
                entry.setBoolean(TileEntityVoidcraftShip.TAG_TRANSFER_STAR, true);
            } else {
                VoidcraftActiveShip target = findFleetShip(state.targetUuid);
                if (target == null) {
                    continue;
                }
                entry.setString(TileEntityVoidcraftShip.TAG_TRANSFER_TARGET, state.targetUuid);
            }
            entries.add(entry);
        }
        return entries;
    }

    /**
     * The construction-site entries for the fleet render anchor (Phase D): each site's anchor target (the
     * ship-entry protocol — the client resolves the star/planet hover live and renders a ripple site exactly at
     * the resolved fixed point), the site's fill progress and the blueprint dimensions (the client wireframe
     * box size), and the active CONSTRUCT leg (leg id / duration / Constructor seed - the client's constructor
     * beam + its fade).
     */
    private List<NBTTagCompound> buildBaseSiteEntries() {
        List<NBTTagCompound> entries = new ArrayList<NBTTagCompound>();
        for (USSBaseSite site : baseSites) {
            if (site == null || site.blueprint() == null) {
                continue;
            }
            NBTTagCompound entry = new NBTTagCompound();
            writeAnchorTarget(entry, site.anchor());
            entry.setDouble(TileEntityVoidcraftShip.TAG_SITE_PROGRESS, site.progressFraction());
            // The active CONSTRUCT leg (the client's constructor beam: leg id 0 = no beam, the seed pairs the
            // beam's ship endpoint, the total is the beam fade's duration).
            entry.setInteger(TileEntityVoidcraftShip.TAG_SITE_CONSTRUCT_LEG, site.constructLegId());
            entry.setLong(TileEntityVoidcraftShip.TAG_SITE_CONSTRUCT_TOTAL, site.constructTotal());
            entry.setInteger(TileEntityVoidcraftShip.TAG_SITE_CONSTRUCT_SEED, site.constructSeed());
            VoidcraftBlueprint blueprint = site.blueprint();
            entry.setIntArray(
                TileEntityVoidcraftShip.TAG_SITE_DIMS,
                new int[] { blueprint.width, blueprint.height, blueprint.depth });
            entries.add(entry);
        }
        return entries;
    }

    /**
     * The standing-base entries for the fleet render anchor (Phase D): each anchored fleet member's anchor target
     * (the ship-entry protocol), the full base payload (the client renders its blueprint as a static model from it)
     * and the current/max integrity (the client tints the model red as integrity drops).
     */
    private List<NBTTagCompound> buildBaseEntries() {
        List<NBTTagCompound> entries = new ArrayList<NBTTagCompound>();
        for (int i = 0; i < activeShips.size(); i++) {
            VoidcraftActiveShip base = activeShips.get(i);
            if (base.getAnchor() == null || base.getPayload() == null) {
                continue;
            }
            NBTTagCompound entry = new NBTTagCompound();
            writeAnchorTarget(entry, base.getAnchor());
            entry.setTag(
                TileEntityVoidcraftShip.TAG_ENTRY_PAYLOAD,
                base.getPayload()
                    .copy());
            entry.setLong(TileEntityVoidcraftShip.TAG_BASE_INTEGRITY, base.getIntegrity());
            entry.setLong(TileEntityVoidcraftShip.TAG_BASE_INTEGRITY_MAX, base.maxIntegrity());
            entry.setInteger(TileEntityVoidcraftShip.TAG_BASE_SEED, base.getSeed());
            // The active mining-leg id (0 = not mining) - the client animates the mining beam from it.
            entry.setInteger(
                TileEntityVoidcraftShip.TAG_BASE_MINING_LEG,
                i < pilots.size() ? pilots.get(i)
                    .miningLegId() : 0);
            entries.add(entry);
        }
        return entries;
    }

    /**
     * Write the anchor target of a render entry (Phase D, shared by sites and bases): STAR — target -1 (the
     * client resolves the star center live); PLANET i — target i (the client tracks the planet's live orbit
     * position); RIPPLE j — the static flag + the server-resolved hover point (the client renders the fixed
     * point exactly).
     */
    private void writeAnchorTarget(NBTTagCompound entry, USSBaseAnchor anchor) {
        if (anchor != null && anchor.isRipple()) {
            entry.setBoolean(TileEntityVoidcraftShip.TAG_ENTRY_STATIC, true);
            entry.setInteger(TileEntityVoidcraftShip.TAG_ENTRY_TARGET, -1);
            USSPosition hover = anchorHoverPoint(anchor);
            if (hover != null) {
                NBTTagCompound dest = new NBTTagCompound();
                hover.writeToNBT(dest);
                entry.setTag(TileEntityVoidcraftShip.TAG_ENTRY_DEST, dest);
            }
            return;
        }
        entry.setInteger(
            TileEntityVoidcraftShip.TAG_ENTRY_TARGET,
            anchor != null && anchor.isPlanet() ? anchor.index() : -1);
    }

    /**
     * The render-visible fleet signature (Phase D): the fleet count + every base integrity + every base
     * mining-leg id + every site progress (quantized to 0.1%) + every site CONSTRUCT leg identity (leg id + seed)
     * + the system's gateway set + the star's ignition state (the anchor is born on ignition and dies on burnout).
     * The fleet tick resyncs the anchor exactly when it changes (integrity decay or
     * repair, a site advancing, a mining leg or a construction leg starting or ending, a gateway registering or
     * being destroyed, the star igniting or going cold) — never per tick (the client animates the beams locally
     * from the leg ids + durations).
     */
    private long fleetRenderSignature() {
        long sig = activeShips.size();
        for (int i = 0; i < activeShips.size(); i++) {
            VoidcraftActiveShip base = activeShips.get(i);
            if (base.getAnchor() == null) {
                continue;
            }
            long mining = (i < pilots.size()) ? pilots.get(i)
                .miningLegId() : 0L;
            sig = sig * 31 + base.getIntegrity() * 31 + mining;
        }
        for (USSBaseSite site : baseSites) {
            sig = sig * 31 + (long) (site != null ? Math.round(site.progressFraction() * 1000.0) : 0.0);
            // The CONSTRUCT leg identity (resync exactly when a leg starts or ends; the per-part progress above
            // already resyncs the deposit ticks).
            sig = sig * 31 + (site != null ? (long) site.constructLegId() * 31L + site.constructSeed() : 0L);
        }
        for (Map.Entry<String, int[]> gw : gatewayBlocks.entrySet()) {
            sig = sig * 31 + gw.getKey()
                .hashCode();
        }
        sig = sig * 31 + (uss != null && uss.isIgnited() ? 1L : 0L);
        return sig;
    }

    /**
     * Register one of this system's gateways (called by a {@code MTEVoidcraftGateway} after its launch-target scan
     * resolves to this USS). Idempotent in position — re-registering the same block is a no-op. The gateway renders
     * as a permanent part of the system view, so it is independent of the fleet's ship list.
     *
     * @param x world block x of the gateway
     * @param y world block y of the gateway
     * @param z world block z of the gateway
     */
    public void registerGateway(int x, int y, int z) {
        gatewayBlocks.put(x + ":" + y + ":" + z, new int[] { x, y, z });
    }

    /**
     * The system's gateways as fleet-anchor-relative positions, pruned of any whose block is no longer a live
     * {@code MTEVoidcraftGateway} (destroyed / replaced). Pruning also updates the registry in place, so the next
     * {@link #fleetRenderSignature()} reflects the removal.
     *
     * @param anchor the fleet anchor block (see {@link #shipAnchorPos})
     * @return the live gateways as {@code [x, y, z]} in fleet-anchor blocks (never null; empty when none)
     */
    private List<int[]> pruneGateways(int[] anchor) {
        List<int[]> out = new ArrayList<int[]>();
        IGregTechTileEntity base = getBaseMetaTileEntity();
        World world = base != null ? base.getWorld() : null;
        if (world == null) {
            return out;
        }
        Iterator<Map.Entry<String, int[]>> it = gatewayBlocks.entrySet()
            .iterator();
        while (it.hasNext()) {
            int[] pos = it.next()
                .getValue();
            if (mteAt(world, pos, MTEVoidcraftGateway.class) == null) {
                it.remove();
                continue;
            }
            out.add(rel(anchor, pos));
        }
        return out;
    }

    /**
     * Push the WHOLE fleet to its one render anchor (Phase 4 pass 5 — replaces pass 4's per-slot blocks): creates
     * or adopts the anchor block, rebuilds its entry list, and syncs it ONCE; it clears the anchor only when the
     * star is cold and the fleet, the sites and the gateways are all empty — while the star burns, the anchor is
     * the system view's host (revealed ripples, gateways, sites) and stays up. Called at most once per MTE tick
     * (launch / state change / completion / discard / one-time cleanup) — one full-fleet description packet
     * instead of one per ship.
     */
    private void syncFleetRenderBlock() {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return;
        }
        World world = base.getWorld();
        if (world == null || world.isRemote) {
            return;
        }
        int[] anchor = shipAnchorPos(base);
        List<int[]> gateways = pruneGateways(anchor);
        // The anchor hosts the whole system view (revealed ripples, gateways, sites, ships) — it stays up for the
        // star's whole life, not only while ships fly; only a cold star with nothing to show loses it.
        if ((uss == null || !uss.isIgnited()) && activeShips.isEmpty() && baseSites.isEmpty() && gateways.isEmpty()) {
            if (world.getBlock(anchor[0], anchor[1], anchor[2]) == VoidcraftLoader.sBlockVoidcraftShipRender) {
                world.setBlockToAir(anchor[0], anchor[1], anchor[2]);
                try {
                    LOGGER.info("[Voidcraft] USS fleet anchor removed @ {},{},{}", anchor[0], anchor[1], anchor[2]);
                } catch (Throwable ignored) {}
            }
            lastFleetRenderSignature = fleetRenderSignature();
            return;
        }
        Block atAnchor = world.getBlock(anchor[0], anchor[1], anchor[2]);
        if (atAnchor != VoidcraftLoader.sBlockVoidcraftShipRender) {
            if (atAnchor != Blocks.air) {
                lastFleetRenderSignature = fleetRenderSignature();
                return; // occupied by something else — the fleet is invisible (rare; missions still run)
            }
            world.setBlock(anchor[0], anchor[1], anchor[2], VoidcraftLoader.sBlockVoidcraftShipRender);
        }
        // else: a (possibly stale) fleet anchor from an earlier state already sits here — adopt it below.
        TileEntity te = world.getTileEntity(anchor[0], anchor[1], anchor[2]);
        if (!(te instanceof TileEntityVoidcraftShip)) {
            lastFleetRenderSignature = fleetRenderSignature();
            return;
        }
        TileEntityVoidcraftShip fleetTe = (TileEntityVoidcraftShip) te;
        fleetTe.setShips(buildFleetEntries(base));
        // Pass 7: the system's planet specs + star size ride with the fleet so the client can resolve each ship's
        // mission target to the planet's live rendered position (no world lookups client-side).
        fleetTe.setSystem(planetSpecsFor(getPlanets()), USSPlanets.starRenderSize(uss.getStarSize()));
        // The virtual orbit clock pair (0/0 when the star is not ignited — the client falls back to the world
        // clock, and a stale clock from a previous life never survives a burnout).
        if (uss.isIgnited()) {
            fleetTe.setUssOrbitTime(uss.getVirtualTime(), world.getTotalWorldTime());
        } else {
            fleetTe.setUssOrbitTime(0L, 0L);
        }
        // The Explorer pass: the REVEALED ripple positions (the ripple field ∩ the scanned set) — the client renders
        // each as a pulsating dark-blue transparent triangle. Only ripples that have been scanned ride here (hidden
        // ripples + revealed non-ripples stay absent).
        fleetTe.setRevealedRipples(revealedRipplePositions());
        // The infrastructure-builder pass: the ripple-scale shells (one entry per revealed ripple carrying a built
        // Continuum Stabilizer) ride the same anchor.
        fleetTe.setRippleInfraShells(rippleInfraShells());
        // Phase D: the Voidbase construction sites (wireframe + fill) and the standing bases (static models) —
        // rendered by the client from this same anchor.
        fleetTe.setBaseSites(buildBaseSiteEntries());
        fleetTe.setBases(buildBaseEntries());
        // The system's gateways — a permanent part of the system view (they render even with an empty fleet).
        fleetTe.setGateways(gateways);
        // Ship-to-ship cargo transfers (SEND / TAKE) — the client's gray transfer beams (absent when none in flight).
        fleetTe.setTransfers(buildTransferEntries());
        lastFleetRenderSignature = fleetRenderSignature();
        // 1.7.10: updateEntity() is a tick hook — the real client push is markBlockForUpdate (see syncToClient).
        fleetTe.syncToClient();
    }

    /**
     * Once per MTE lifetime (in-memory; the check is idempotent):
     * <ul>
     * <li>clear legacy per-slot anchors from pass 4 (slots 1–2 of the old 2-blocks-per-slot lateral geometry —
     * still present in older test worlds; pass 5 uses ONE fleet anchor at the old slot-0 position), and any
     * stray anchor left with the fleet empty AND the star cold (older builds could leave one behind with stale
     * state, rendering a frozen ship forever and blocking future launches);</li>
     * <li>pass 12: the fleet anchor moved 16 → 32 behind the controller (the structure doubled) — clear the
     * old 16-offset anchor and its lateral slots from pre-pass-12 test worlds;</li>
     * <li>make sure the fleet anchor EXISTS and holds the current fleet (covers a load-time edge case where the
     * anchor block was lost while the MTE's NBT survived).</li>
     * </ul>
     */
    private void cleanupLegacyShipRender() {
        if (legacyShipRenderCleaned) {
            return;
        }
        legacyShipRenderCleaned = true;
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return;
        }
        World world = base.getWorld();
        int[] anchor = shipAnchorPos(base);
        // Pass-4 lateral slot anchors: side = up × back = (back.offsetZ, 0, -back.offsetX); 2 blocks per slot.
        int sideX = getExtendedFacing().getRelativeBackInWorld().offsetZ;
        int sideZ = -getExtendedFacing().getRelativeBackInWorld().offsetX;
        if (sideX == 0 && sideZ == 0) {
            sideX = 1; // back is parallel to up (facing straight down) — any fixed horizontal axis works
        }
        // Pass 12: the fleet anchor now sits 32 behind the controller; pre-pass-12 test worlds have the old
        // 16-offset anchor (and its lateral slots) orphaned — clear them (one-time, idempotent).
        ForgeDirection back = getExtendedFacing().getRelativeBackInWorld();
        int oldAnchorX = (int) (base.getXCoord() + 16 * back.offsetX);
        int oldAnchorY = (int) (base.getYCoord() + 16 * back.offsetY) + 2;
        int oldAnchorZ = (int) (base.getZCoord() + 16 * back.offsetZ);
        for (int slot = 0; slot <= 2; slot++) {
            int px = oldAnchorX + sideX * 2 * slot;
            int pz = oldAnchorZ + sideZ * 2 * slot;
            if (world.getBlock(px, oldAnchorY, pz) == VoidcraftLoader.sBlockVoidcraftShipRender) {
                world.setBlockToAir(px, oldAnchorY, pz);
                try {
                    LOGGER.info(
                        "[Voidcraft] USS removed pre-pass-12 ship-render anchor (slot {}) @ {},{},{}",
                        slot,
                        px,
                        oldAnchorY,
                        pz);
                } catch (Throwable ignored) {}
            }
        }
        for (int slot = 1; slot <= 2; slot++) {
            int px = anchor[0] + sideX * 2 * slot;
            int pz = anchor[2] + sideZ * 2 * slot;
            if (world.getBlock(px, anchor[1], pz) == VoidcraftLoader.sBlockVoidcraftShipRender) {
                world.setBlockToAir(px, anchor[1], pz);
                try {
                    LOGGER.info(
                        "[Voidcraft] USS removed legacy ship-render anchor (slot {}) @ {},{},{}",
                        slot,
                        px,
                        anchor[1],
                        pz);
                } catch (Throwable ignored) {}
            }
        }
        if (activeShips.isEmpty() && (uss == null || !uss.isIgnited())) {
            if (world.getBlock(anchor[0], anchor[1], anchor[2]) == VoidcraftLoader.sBlockVoidcraftShipRender) {
                world.setBlockToAir(anchor[0], anchor[1], anchor[2]);
                try {
                    LOGGER
                        .info("[Voidcraft] USS removed stray fleet anchor @ {},{},{}", anchor[0], anchor[1], anchor[2]);
                } catch (Throwable ignored) {}
            }
        } else {
            syncFleetRenderBlock(); // ensure the anchor exists and holds the current fleet
        }
    }

    // endregion

    // Region info data & misc.

    @Override
    public String[] getInfoData() {
        ArrayList<String> str = new ArrayList<>(Arrays.asList(super.getInfoData()));
        str.add("tt.voidcraft_uss.infodata.header");
        VoidcraftUSS model = uss == null ? VoidcraftUSS.cold() : uss;
        str.add(
            IGregTechDeviceInformation.encode(
                "tt.voidcraft_uss.state.label",
                IGregTechDeviceInformation.translatable(
                    model.getState()
                        .getLangKey())));
        if (model.isIgnited()) {
            str.add(
                IGregTechDeviceInformation
                    .encode("tt.voidcraft_uss.tier.label", "" + YELLOW + (model.getTier() + 1) + RESET));
            str.add(
                IGregTechDeviceInformation.encode(
                    "tt.voidcraft_uss.star.label",
                    IGregTechDeviceInformation.translatable(
                        model.getStarType()
                            .getLangKey())));
            str.add(
                IGregTechDeviceInformation
                    .encode("tt.voidcraft_uss.lifespan.label", "" + YELLOW + model.getLifespanRemaining() + RESET));
            // Stellar acceleration: the last completed second — the tachyon fluid drained, the lifespan ticks it
            // shortened, and the orbit clock's rate (ticks per tick) while that second runs.
            str.add("tt.voidcraft_uss.acceleration.header");
            if (lastAccelerationSecondMB > 0L) {
                str.add(
                    IGregTechDeviceInformation.encode(
                        "tt.voidcraft_uss.acceleration.tachyon",
                        "" + YELLOW + lastAccelerationSecondMB + RESET));
                str.add(
                    IGregTechDeviceInformation.encode(
                        "tt.voidcraft_uss.acceleration.reduction",
                        "-" + YELLOW
                            + USSStellarEvolution.lifespanReductionPerSecond(lastAccelerationSecondMB)
                            + RESET));
                str.add(
                    IGregTechDeviceInformation.encode(
                        "tt.voidcraft_uss.acceleration.orbit",
                        "x" + USSStellarEvolution.orbitAdvancePerTick(lastAccelerationSecondMB)));
            } else {
                str.add(IGregTechDeviceInformation.encode("tt.voidcraft_uss.acceleration.tachyon", "0"));
                str.add(IGregTechDeviceInformation.encode("tt.voidcraft_uss.acceleration.reduction", "0"));
                str.add(IGregTechDeviceInformation.encode("tt.voidcraft_uss.acceleration.orbit", "x1"));
            }
            // Stellar Injector (the Stellar Evolution pass): the star's size progress against the 1.5x cap, the
            // buffer's
            // fill, and the shell's build state (the injector is active once the star's shell is fully built).
            str.add("tt.voidcraft_uss.injector.header");
            str.add(
                IGregTechDeviceInformation.encode(
                    "tt.voidcraft_uss.injector.size",
                    String.format(
                        "%.2f / %.2f",
                        model.getStarSize(),
                        USSStellarEvolution
                            .sizeCap(USSPlanets.sampleStarSize(model.getStarType(), model.getIgnitedAt())))));
            CargoHold buffer = model.getInjectorBuffer();
            str.add(
                IGregTechDeviceInformation.encode(
                    "tt.voidcraft_uss.injector.buffer",
                    (buffer == null ? 0L : buffer.usedUnits()) + " / " + (buffer == null ? 0L : buffer.getCapacity())));
            long shellCount = model.getInfrastructure()
                .count(USSInfraBuild.key(USSInfraBuild.INJECTOR, USSInfraBuild.TARGET_STAR, -1));
            long shellCap = infraShellCapacity(USSInfraBuild.INJECTOR, USSInfraBuild.TARGET_STAR, -1);
            str.add(
                IGregTechDeviceInformation.encode(
                    "tt.voidcraft_uss.injector.shell",
                    shellCount + " / "
                        + shellCap
                        + (shellCap > 0L && shellCount >= shellCap
                            ? " " + IGregTechDeviceInformation.translatable("tt.voidcraft_uss.injector.active")
                            : " " + IGregTechDeviceInformation.translatable("tt.voidcraft_uss.injector.building"))));
            // Phase 4 pass 3: the system's PLANETS — what a Miner can work here (planet → its registered ores).
            str.add("tt.voidcraft_uss.planets.header");
            for (USSPlanets.USSPlanet planet : getPlanets()) {
                StringBuilder oreList = new StringBuilder();
                for (USSPlanetOre ore : planet.definition.getOres()) {
                    if (oreList.length() > 0) {
                        oreList.append(", ");
                    }
                    oreList.append(displayNameForMaterial(ore.getOreType()));
                }
                str.add(
                    IGregTechDeviceInformation.encode(
                        "tt.voidcraft_uss.planet.line",
                        IGregTechDeviceInformation.translatable("tt.voidcraft_uss.planet." + planet.definition.getId())
                            + " — "
                            + oreList));
            }
        }
        str.add(
            IGregTechDeviceInformation.encode(
                "tt.voidcraft_uss.controller.label",
                IGregTechDeviceInformation.translatable(
                    getControllerSlot() != null ? "tt.voidcraft_uss.controller.present"
                        : "tt.voidcraft_uss.controller.absent")));
        // Phase 4 pass 5: the fleet in flight (dozens–hundreds possible) — the COUNT plus a few sample lines,
        // not a 100-line list.
        int shipCount = 0;
        int baseCount = 0;
        for (VoidcraftActiveShip entity : activeShips) {
            if (entity.getAnchor() == null) {
                shipCount++;
            } else {
                baseCount++;
            }
        }
        if (shipCount > 0) {
            str.add(IGregTechDeviceInformation.encode("tt.voidcraft_uss.ships.header", String.valueOf(shipCount)));
            int shown = 0;
            for (VoidcraftActiveShip ship : activeShips) {
                if (ship.getAnchor() != null) {
                    continue; // anchored stations get their own section below
                }
                if (shown++ >= 3) {
                    str.add(
                        IGregTechDeviceInformation
                            .encode("tt.voidcraft_uss.ships.more", String.valueOf(shipCount - 3)));
                    break;
                }
                str.add(
                    IGregTechDeviceInformation.encode(
                        "tt.voidcraft_uss.ship.line",
                        ship.getName() + " — "
                            + IGregTechDeviceInformation.translatable(
                                ship.getState()
                                    .getLangKey())
                            + " "
                            + YELLOW
                            + ship.getTicksRemaining()
                            + RESET
                            + "t"));
            }
        }
        // Voidbases: the construction sites (in progress) and the anchored stations.
        if (!baseSites.isEmpty() || baseCount > 0) {
            str.add("tt.voidcraft_uss.infodata.bases.header");
            for (USSBaseSite site : baseSites) {
                str.add(
                    IGregTechDeviceInformation.encode(
                        "tt.voidcraft_uss.infodata.site.line",
                        anchorName(site.anchor()) + " — "
                            + site.name()
                            + " "
                            + YELLOW
                            + (int) (site.progressFraction() * 100)
                            + RESET
                            + "%"));
            }
            for (VoidcraftActiveShip base : activeShips) {
                if (base.getAnchor() == null) {
                    continue;
                }
                str.add(
                    IGregTechDeviceInformation.encode(
                        "tt.voidcraft_uss.infodata.base.line",
                        base.getName() + " "
                            + anchorName(base.getAnchor())
                            + " — integrity "
                            + YELLOW
                            + base.getIntegrity()
                            + "/"
                            + base.maxIntegrity()
                            + RESET));
            }
        }
        return str.toArray(new String[0]);
    }

    /**
     * The in-game display name of a planet ore material (the dust stack's localized name; the raw material name
     * is the fallback).
     */
    private static String displayNameForMaterial(Materials material) {
        if (material != null && material != Materials._NULL) {
            ItemStack dust = material.getDust(1);
            if (dust != null) {
                return dust.getDisplayName();
            }
        }
        return material == null ? "?" : material.getName();
    }

    /** The localized in-game name of a Voidbase anchor (star / planet i / ripple j). */
    private static String anchorName(USSBaseAnchor anchor) {
        if (anchor.isStar()) {
            return StatCollector.translateToLocal("tt.voidcraft_uss.anchor.star");
        }
        if (anchor.isPlanet()) {
            return StatCollector.translateToLocal("tt.voidcraft_uss.anchor.planet") + " " + anchor.index();
        }
        return StatCollector.translateToLocal("tt.voidcraft_uss.anchor.ripple") + " " + anchor.index();
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.spacetime_manipulator"))
            .addMarkdown(new ResourceLocation("gregtech", "unstable-solar-system"))
            .beginStructureBlock(65, 65, 65, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center_17th_layer"))
            .addCasing("896", new ItemStack(TTCasingsContainer.sBlockCasingsBA0, 1, 11).getDisplayName(), false)
            .addCasing("534", new ItemStack(TTCasingsContainer.sBlockCasingsBA0, 1, 10).getDisplayName(), false)
            .addCasing("168", TTCasingsContainer.TimeAccelerationFieldGenerator.getLocalizedName(), true)
            .addCasing("138", TTCasingsContainer.SpacetimeCompressionFieldGenerators.getLocalizedName(), true)
            .addCasing("48", TTCasingsContainer.StabilisationFieldGenerators.getLocalizedName(), true)
            .addCasing("31", Casings.InfiniteSpacetimeEnergyBoundaryCasing.getLocalizedName(), false)
            .addInputBus("1", StatCollector.translateToLocal("GT5U.tooltip.eye-of-harmony.boundary-no-stocking-bus"), 1)
            .addInputHatch("2", StatCollector.translateToLocal("GT5U.tooltip.eye-of-harmony.boundary-no-stocking-hatch"), 1)
            .addOutputBus("1", StatCollector.translateToLocal("GT5U.tooltip.eye-of-harmony.any-boundary-casing"), 1)
            .addOutputHatch("1", StatCollector.translateToLocal("GT5U.tooltip.eye-of-harmony.any-boundary-casing"), 1)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.EOH_STABILISATION)
            .addSubChannel(GTStructureChannels.EOH_DILATION)
            .addSubChannel(GTStructureChannels.EOH_COMPRESSION)
            .toolTipFinisher(EnumChatFormatting.GOLD, 87, GTAuthors.AuthorColen);
        // spotless:on
        return tt;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = Textures.BlockIcons.custom("iconsets/EM_BHG");
        ScreenON = Textures.BlockIcons.custom("iconsets/EM_BHG_ACTIVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][12],
                new TTRenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][12] };
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        // Same loop sound as the legacy EoH (the file is shared).
        return SoundResource.GT_MACHINES_EYE_OF_HARMONY_LOOP;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean isSafeVoidButtonEnabled() {
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 32, 32, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5); // 200 blocks max
                                                                                                  // per placement.
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 32, 32, 0, realBudget, source, actor, false, true);
    }

    // Region NBT.

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        if (uss != null) {
            NBTTagCompound ussTag = new NBTTagCompound();
            uss.writeToNBT(ussTag);
            aNBT.setTag(USS_NBT_TAG, ussTag);
        }
        aNBT.setBoolean(ANIMATIONS_ENABLED_NBT_TAG, animationsEnabled);
        if (!activeShips.isEmpty()) {
            NBTTagList ships = new NBTTagList();
            for (int i = 0; i < activeShips.size(); i++) {
                NBTTagCompound shipTag = activeShips.get(i)
                    .writeToNBT();
                // Phase C: the pilot's state (the executor's cursor + the in-flight leg's bookkeeping) nests
                // under the SHIP's tag — a ship saved mid-leg / mid-loop resumes exactly once after a reload.
                if (i < pilots.size()) {
                    shipTag.setTag(
                        USSShipPilot.TAG_PILOT,
                        pilots.get(i)
                            .writeToNBT());
                }
                ships.appendTag(shipTag);
            }
            aNBT.setTag(ACTIVE_SHIPS_NBT_TAG, ships);
        }
        // Voidbase construction sites + completed bases (a fresh USS with no base omits both tags).
        if (!baseSites.isEmpty()) {
            NBTTagList sites = new NBTTagList();
            for (USSBaseSite site : baseSites) {
                NBTTagCompound siteTag = new NBTTagCompound();
                site.writeToNBT(siteTag);
                sites.appendTag(siteTag);
            }
            aNBT.setTag(BASE_SITES_NBT_TAG, sites);
        }
        // The in-flight SEND / TAKE transfers: the pilots' cursors (persisted per ship) resume the nodes, but the
        // transfers' own progress + pacing state lives here — without it a resumed node finds nothing in flight
        // and abandons the remainder.
        if (!cargoTransfers.isEmpty()) {
            NBTTagList transferTags = new NBTTagList();
            for (Map.Entry<String, USSCargoTransferState> entry : cargoTransfers.entrySet()) {
                USSCargoTransferState state = entry.getValue();
                if (state == null || state.leg == null
                    || (!state.starTarget && (state.targetUuid == null || state.targetUuid.isEmpty()))) {
                    continue;
                }
                NBTTagCompound transferTag = new NBTTagCompound();
                transferTag.setString(CARGO_TRANSFER_SRC_UUID_NBT_TAG, entry.getKey());
                transferTag
                    .setString(CARGO_TRANSFER_TGT_UUID_NBT_TAG, state.targetUuid == null ? "" : state.targetUuid);
                transferTag
                    .setString(CARGO_TRANSFER_TGT_NAME_NBT_TAG, state.targetName == null ? "" : state.targetName);
                transferTag.setBoolean(CARGO_TRANSFER_STAR_NBT_TAG, state.starTarget);
                NBTTagCompound legTag = new NBTTagCompound();
                state.leg.writeToNBT(legTag);
                transferTag.setTag(CARGO_TRANSFER_LEG_NBT_TAG, legTag);
                transferTags.appendTag(transferTag);
            }
            if (transferTags.tagCount() > 0) {
                aNBT.setTag(CARGO_TRANSFERS_NBT_TAG, transferTags);
            }
        }
        // The in-flight REPAIR sessions: the same rationale as the transfers (the pilots' cursors resume the
        // nodes; the sessions' own pacing + target identity live here).
        if (!repairs.isEmpty()) {
            NBTTagList repairTags = new NBTTagList();
            for (Map.Entry<String, USSRepairState> entry : repairs.entrySet()) {
                USSRepairState state = entry.getValue();
                if (state == null || state.targetUuid == null || state.targetUuid.isEmpty()) {
                    continue;
                }
                NBTTagCompound repairTag = new NBTTagCompound();
                repairTag.setString(REPAIR_SRC_UUID_NBT_TAG, entry.getKey());
                repairTag.setString(REPAIR_TGT_UUID_NBT_TAG, state.targetUuid);
                repairTag.setInteger(REPAIR_TICKS_NBT_TAG, state.ticks);
                repairTags.appendTag(repairTag);
            }
            if (repairTags.tagCount() > 0) {
                aNBT.setTag(REPAIRS_NBT_TAG, repairTags);
            }
        }
        // The in-flight STABILIZE windows: the expiry weight read queries the live sessions, so they persist with
        // the same rationale as the repair sessions.
        if (!stabilizes.isEmpty()) {
            NBTTagList stabilizeTags = new NBTTagList();
            for (Map.Entry<String, USSStabilize.Session> entry : stabilizes.entrySet()) {
                USSStabilize.Session state = entry.getValue();
                if (state == null || state.ticks <= 0L) {
                    continue;
                }
                NBTTagCompound stabilizeTag = new NBTTagCompound();
                stabilizeTag.setString(STABILIZE_SRC_UUID_NBT_TAG, entry.getKey());
                stabilizeTag.setLong(STABILIZE_TICKS_NBT_TAG, state.ticks);
                stabilizeTag.setLong(STABILIZE_FIELD_GENERATOR_TICKS_NBT_TAG, state.fieldGeneratorTicks);
                stabilizeTag.setInteger(STABILIZE_WEIGHT_NBT_TAG, state.weight);
                stabilizeTags.appendTag(stabilizeTag);
            }
            if (stabilizeTags.tagCount() > 0) {
                aNBT.setTag(STABILIZES_NBT_TAG, stabilizeTags);
            }
        }
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey(USS_NBT_TAG)) {
            VoidcraftUSS loaded = VoidcraftUSS.readFromNBT(aNBT.getCompoundTag(USS_NBT_TAG));
            uss = loaded != null ? loaded : VoidcraftUSS.cold();
        }
        animationsEnabled = !aNBT.hasKey(ANIMATIONS_ENABLED_NBT_TAG) || aNBT.getBoolean(ANIMATIONS_ENABLED_NBT_TAG);
        // Phase 4 pass 4/5: the fleet (slot order). Corrupt entries are skipped; the list is capped at capacity.
        // The fleet render anchor is a pure function of (block position, facing), so nothing else to restore —
        // the fleet TE persists itself in the world (and the one-time cleanup re-creates it if it was lost).
        activeShips.clear();
        pilots.clear();
        Arrays.fill(lastPushedShipStates, -1);
        Arrays.fill(lastPushedLegIds, -1);
        if (aNBT.hasKey(ACTIVE_SHIPS_NBT_TAG)) {
            NBTTagList ships = aNBT.getTagList(ACTIVE_SHIPS_NBT_TAG, 10);
            for (int i = 0; i < ships.tagCount() && activeShips.size() < USSConstants.MAX_SHIPS_PER_USS; i++) {
                NBTTagCompound shipTag = ships.getCompoundTagAt(i);
                VoidcraftActiveShip ship = VoidcraftActiveShip.readFromNBT(shipTag);
                if (ship != null) {
                    activeShips.add(ship);
                    // Phase C: re-attach the pilot (its program comes from the ship's payload; its cursor + leg
                    // bookkeeping from the nested vc_pilot tag — a missing one degrades to a fresh pilot, a corrupt
                    // one fails safe to a COMPLETED program → the ship holds).
                    pilots.add(USSShipPilot.attach(ship, this, shipTag));
                    // The slot's render TE already holds the state (its own NBT) — mark it pushed, no re-push.
                    lastPushedShipStates[activeShips.size() - 1] = ship.getState()
                        .getId();
                    lastPushedLegIds[activeShips.size() - 1] = ship.getLegId();
                }
            }
        }
        // Voidbase construction sites (absent tag = fresh/empty; corrupt entries are skipped, no migration path).
        baseSites.clear();
        if (aNBT.hasKey(BASE_SITES_NBT_TAG)) {
            NBTTagList sites = aNBT.getTagList(BASE_SITES_NBT_TAG, 10);
            for (int i = 0; i < sites.tagCount(); i++) {
                NBTTagCompound siteTag = sites.getCompoundTagAt(i);
                if (siteTag == null) {
                    continue;
                }
                USSBaseSite site = USSBaseSite.readFromNBT(siteTag);
                if (site != null) {
                    baseSites.add(site);
                }
            }
        }
        // The in-flight SEND / TAKE transfers (AFTER the fleet, so the orphan check can see the rebuilt ships):
        // a record whose executing or target ship did not survive the reload is dropped.
        cargoTransfers.clear();
        if (aNBT.hasKey(CARGO_TRANSFERS_NBT_TAG)) {
            NBTTagList transferTags = aNBT.getTagList(CARGO_TRANSFERS_NBT_TAG, 10);
            for (int i = 0; i < transferTags.tagCount(); i++) {
                NBTTagCompound transferTag = transferTags.getCompoundTagAt(i);
                if (transferTag == null) {
                    continue;
                }
                String sourceUuid = transferTag.getString(CARGO_TRANSFER_SRC_UUID_NBT_TAG);
                String targetUuid = transferTag.getString(CARGO_TRANSFER_TGT_UUID_NBT_TAG);
                boolean starTarget = transferTag.getBoolean(CARGO_TRANSFER_STAR_NBT_TAG);
                if (sourceUuid.isEmpty() || (!starTarget && targetUuid.isEmpty()) || sourceUuid.equals(targetUuid)) {
                    continue; // a self-target is rejected at start — such a record is corrupt
                }
                if (findFleetShip(sourceUuid) == null || (!starTarget && findFleetShip(targetUuid) == null)) {
                    continue; // orphaned — a ship did not survive the reload
                }
                USSCargoTransfer leg = transferTag.hasKey(CARGO_TRANSFER_LEG_NBT_TAG, 10)
                    ? USSCargoTransfer.readFromNBT(transferTag.getCompoundTag(CARGO_TRANSFER_LEG_NBT_TAG))
                    : null;
                if (leg == null) {
                    continue;
                }
                USSCargoTransferState state = new USSCargoTransferState();
                state.leg = leg;
                state.targetUuid = targetUuid;
                state.targetName = transferTag.getString(CARGO_TRANSFER_TGT_NAME_NBT_TAG);
                state.starTarget = starTarget;
                cargoTransfers.put(sourceUuid, state);
            }
        }
        // The in-flight REPAIR sessions (AFTER the fleet, so the orphan check can see the rebuilt entities):
        // a record whose executing or target entity did not survive the reload is dropped. A self-target
        // (source == target, the station repairing itself) is valid.
        repairs.clear();
        if (aNBT.hasKey(REPAIRS_NBT_TAG)) {
            NBTTagList repairTags = aNBT.getTagList(REPAIRS_NBT_TAG, 10);
            for (int i = 0; i < repairTags.tagCount(); i++) {
                NBTTagCompound repairTag = repairTags.getCompoundTagAt(i);
                if (repairTag == null) {
                    continue;
                }
                String sourceUuid = repairTag.getString(REPAIR_SRC_UUID_NBT_TAG);
                String targetUuid = repairTag.getString(REPAIR_TGT_UUID_NBT_TAG);
                if (sourceUuid.isEmpty() || targetUuid.isEmpty()) {
                    continue;
                }
                if (findFleetShip(sourceUuid) == null || findFleetShip(targetUuid) == null) {
                    continue; // orphaned — an entity did not survive the reload
                }
                USSRepairState state = new USSRepairState();
                state.targetUuid = targetUuid;
                state.ticks = Math.max(
                    0,
                    Math.min(VoidcraftActiveShip.TICKS_PER_INTEGRITY - 1, repairTag.getInteger(REPAIR_TICKS_NBT_TAG)));
                repairs.put(sourceUuid, state);
            }
        }
        // The in-flight STABILIZE windows (AFTER the fleet, so the orphan check can see the rebuilt bases): a
        // record whose executing base did not survive the reload is dropped.
        stabilizes.clear();
        if (aNBT.hasKey(STABILIZES_NBT_TAG)) {
            NBTTagList stabilizeTags = aNBT.getTagList(STABILIZES_NBT_TAG, 10);
            for (int i = 0; i < stabilizeTags.tagCount(); i++) {
                NBTTagCompound stabilizeTag = stabilizeTags.getCompoundTagAt(i);
                if (stabilizeTag == null) {
                    continue;
                }
                String sourceUuid = stabilizeTag.getString(STABILIZE_SRC_UUID_NBT_TAG);
                long ticks = stabilizeTag.getLong(STABILIZE_TICKS_NBT_TAG);
                if (sourceUuid.isEmpty() || ticks <= 0L) {
                    continue;
                }
                if (findFleetShip(sourceUuid) == null) {
                    continue; // orphaned — the base did not survive the reload
                }
                USSStabilize.Session state = new USSStabilize.Session();
                state.ticks = ticks;
                state.fieldGeneratorTicks = Math.max(
                    1L,
                    Math.min(
                        USSConstants.STABILIZE_FIELD_GENERATOR_INTERVAL_TICKS,
                        stabilizeTag.getLong(STABILIZE_FIELD_GENERATOR_TICKS_NBT_TAG)));
                state.weight = Math.max(
                    0,
                    Math.min(USSConstants.MATRIX_WEIGHT_UXV, stabilizeTag.getInteger(STABILIZE_WEIGHT_NBT_TAG)));
                stabilizes.put(sourceUuid, state);
            }
        }
        super.loadNBTData(aNBT);
    }

    // endregion
}
