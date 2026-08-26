package tectech.voidcraft.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.voidcraft.item.ItemVoidcraft;
import tectech.voidcraft.loader.VoidcraftLoader;
import tectech.voidcraft.ship.VoidcraftNbt;
import tectech.voidcraft.ship.VoidcraftRole;
import tectech.voidcraft.uss.MTEUnstableSolarSystem;
import tectech.voidcraft.uss.USSConstants;
import tectech.voidcraft.uss.USSInfrastructure;
import tectech.voidcraft.uss.USSLoadout;
import tectech.voidcraft.uss.USSProject;
import tectech.voidcraft.uss.USSShipCargo;

/**
 * Voidcraft Gateway (EoH rework, Phase 3).
 *
 * <p>
 * A 3×3×3 BA0-cased shell with the controller (and its single ship slot) at the front-face center. Insert a digitized
 * {@link ItemVoidcraft} and — when a valid Unstable Solar System (star ignited) and a Storage Bay are both within
 * range — the gateway launches the ship
 * on a mining mission: the ship flies out to the star, mines, and returns with cargo, which the USS delivers to the
 * nearest bay. A ship's integrity is its time limit (it drops 1 per second while in the USS): a ship that finishes
 * before it expires is re-emitted into the gateway slot (integrity back at maximum); one that hits 0 is lost with
 * its cargo.
 *
 * <p>
 * No energy hatches, no recipes — the interaction surface is the ship slot (right-click with the ship item in
 * hand, same pattern as the USS controller slot) plus the front-face hatch ring: input buses (dust for Constructor
 * loadouts) and input hatches (Stellar Plasma) feed the Phase 4 pass 2 Constructor missions — MINER / STARLIFTER
 * ships launch without any inputs at all.
 *
 * <p>
 * The gateway renders nothing itself: a ship's hologram exists only while it is in flight, and that in-flight anchor
 * is owned by the Unstable Solar System (see {@link MTEUnstableSolarSystem}).
 */
@IMetaTileEntity.SkipGenerateDescription
public class MTEVoidcraftGateway extends TTMultiblockBase implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    /** How far (blocks) the gateway scans for a USS / Storage Bay. */
    private static final int SCAN_RADIUS = 32;

    /**
     * Minimum interval (ticks) between target scans while a ship is docked. The scan sweeps the whole sphere at
     * full resolution, so the result is cached instead of re-scanning every tick.
     */
    private static final long SCAN_COOLDOWN_TICKS = 20;

    /**
     * 3×3×3 shell: the <strong>front face</strong> (z=0) is the controller (center) ringed by the hatch-capable
     * perimeter 'C' (input buses + input hatches — the Phase 4 pass 2 Constructor loadout inputs; non-hatch ring
     * cells render as HighPowerCasing, same family convention as the Storage Bay and the Energy Infuser), the two
     * back planes are BA0 casing meta 10. The controller sits at the front-face center (x=1, y=1, z=0 — the same
     * convention as the Storage Bay and the Assembler; a buried center controller would be unreachable once the
     * shell is closed).
     */
    private static final IStructureDefinition<MTEVoidcraftGateway> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEVoidcraftGateway>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(new String[][] { { "CCC", "AAA", "AAA" }, { "C~C", "AAA", "AAA" }, { "CCC", "AAA", "AAA" } }))
        .addElement('A', ofBlock(TTCasingsContainer.sBlockCasingsBA0, 10))
        .addElement(
            'C',
            buildHatchAdder(MTEVoidcraftGateway.class)
                // InputBus: the dust path (Constructor loadout items, incl. ME item buses). InputHatch: the fluid
                // path (Stellar Plasma). Non-hatch ring cells fall back to the HighPowerCasing render.
                .atLeast(InputBus, InputHatch)
                .casingIndex(Casings.HighPowerCasing.getTextureId())
                .hint(1)
                .buildAndChain(Casings.HighPowerCasing.asElement()))
        .build();

    /** Rate-limits launch-failure chat (ticks). */
    private long lastErrorTick = -1000;

    /**
     * Cached launch targets, refreshed at most every {@link #SCAN_COOLDOWN_TICKS} (see {@link #refreshLaunchTargets}).
     */
    private MTEUnstableSolarSystem targetUSS;
    private MTEVoidcraftStorageBay targetBay;
    private long lastTargetScan = Long.MIN_VALUE;

    /** Scan detail buffer (per-sweep diagnostics for failed target searches). */
    private final StringBuilder diag = new StringBuilder();

    /** Rate-limits scan-diagnostic chat (ticks). */
    private long lastDiagTick = Long.MIN_VALUE;

    /**
     * Plain log4j logger at INFO (NOT {@code GTLog.conditionalLogger}, which forces Level.OFF) — gateway lifecycle
     * and scan diagnostics must actually reach the game log.
     */
    private static final Logger LOGGER = LogManager.getLogger("Voidcraft Gateway");

    public MTEVoidcraftGateway(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEVoidcraftGateway(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEVoidcraftGateway(mName);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0, errors)) {
            return;
        }
        // Phase 4 pass 2: the Constructor loadout needs at least one input bus (dust) and one input hatch
        // (Stellar Plasma) on the front-face ring.
        checkHasInputBus(errors);
        checkHasInputHatch(errors);
    }

    @Override
    public CheckRecipeResult checkProcessing_EM() {
        // Not a recipe machine — the launch logic runs in onPostTick.
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        // Insert the ship item into the empty ship slot (same pattern as the USS controller slot).
        if (getControllerSlot() == null) {
            ItemStack heldItem = aPlayer.getHeldItem();
            if (heldItem != null && heldItem.getItem() == ItemVoidcraft.INSTANCE) {
                mInventory[getControllerSlotIndex()] = heldItem.copy();
                mInventory[getControllerSlotIndex()].stackSize = 1;
                aPlayer.setCurrentItemOrArmor(0, ItemUtils.depleteStack(heldItem, 1));
                return true;
            }
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) {
            return;
        }
        cleanupLegacyDockAnchor();
        if (!mMachine) {
            return;
        }

        ItemStack ship = getControllerSlot();
        if (ship == null || ship.getItem() != ItemVoidcraft.INSTANCE) {
            return;
        }
        attemptLaunch(aTick);
    }

    // region launch

    /**
     * Try to launch the ship currently in the slot. On success the slot is consumed; on failure the ship stays
     * docked and the error is reported (rate-limited) to nearby players.
     */
    private void attemptLaunch(long aTick) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        World world = base == null ? null : base.getWorld();
        if (world == null) {
            return;
        }
        ItemStack ship = getControllerSlot();
        if (ship == null) {
            return; // idle — no ship in the slot
        }

        // The ship payload is the item's tag compound (the vc_* keys sit at its top level — see
        // ItemVoidcraft.getBlueprint). The full ItemStack NBT nests it one level deeper under the vanilla
        // "tag" key, so writeToNBT must NOT be used here.
        NBTTagCompound payload = ship.getTagCompound();
        if (payload == null || VoidcraftNbt.read(payload) == null) {
            reportError(aTick, "invalid_ship");
            return;
        }
        int roles = VoidcraftNbt.readInt(payload, VoidcraftNbt.TAG_ROLES);
        boolean constructor = VoidcraftRole.CONSTRUCTOR.isActive(roles);
        // MINER / STARLIFTER (mining cargo), EXPLORER (spacetime-ripple scanning) or CONSTRUCTOR
        // (infrastructure loadout) may launch; a ship with none of those roles cannot be sent out.
        boolean regular = VoidcraftRole.MINER.isActive(roles) || VoidcraftRole.STARLIFTER.isActive(roles)
            || VoidcraftRole.EXPLORER.isActive(roles);
        if (!constructor && !regular) {
            reportError(aTick, "no_mission_role");
            return;
        }
        int cx = base.getXCoord();
        int cy = base.getYCoord();
        int cz = base.getZCoord();

        refreshLaunchTargets(aTick, world, cx, cy, cz);
        if (targetUSS == null) {
            reportError(aTick, "no_uss");
            return;
        }
        if (targetBay == null) {
            reportError(aTick, "no_bay");
            return;
        }

        // Phase 4 pass 2: a CONSTRUCTOR with work to do takes priority over its mining roles — the ship leaves
        // loaded with a loadout of the USS's first incomplete infrastructure project, pulled from this gateway's
        // input buses (dust) and input hatches (Stellar Plasma). When the whole catalog is already built, a
        // CONSTRUCTOR+MINER/STARLIFTER hybrid falls back to a regular mining mission; a pure CONSTRUCTOR with
        // nothing to build is rejected (the ship stays docked, no wasted flights).
        if (constructor) {
            USSInfrastructure infrastructure = targetUSS.getInfrastructure();
            USSProject project = infrastructure == null ? null : infrastructure.firstIncomplete();
            if (project != null) {
                if (!prepareConstructorMission(aTick, payload, infrastructure, project)) {
                    return; // the launch is aborted (error already reported)
                }
                payload.setBoolean(VoidcraftNbt.TAG_CONSTRUCTOR_MISSION, true);
            } else {
                payload.setBoolean(VoidcraftNbt.TAG_CONSTRUCTOR_MISSION, false);
                if (!regular) {
                    reportError(aTick, "nothing_to_build");
                    return;
                }
            }
        } else {
            payload.setBoolean(VoidcraftNbt.TAG_CONSTRUCTOR_MISSION, false);
        }

        int[] bayPos = new int[] { targetBay.getBaseMetaTileEntity()
            .getXCoord(),
            targetBay.getBaseMetaTileEntity()
                .getYCoord(),
            targetBay.getBaseMetaTileEntity()
                .getZCoord() };

        if (targetUSS.launchShip(payload, new int[] { cx, cy, cz }, bayPos)) {
            mInventory[getControllerSlotIndex()] = null;
        } else {
            reportError(aTick, "uss_busy");
        }
    }

    /**
     * Phase 4 pass 2 — load a Constructor mission: compute the loadout from the USS's current project, pull exactly
     * that much material from the input buses / input hatches, and write the loadout (abstract entries) + the
     * project id into the ship payload.
     *
     * <p>
     * "Take what's there, report what's missing": the take is capped by the remaining project need, the ship's
     * per-mission capacity (construction-power scaled, same window as the Starlifter), and what is actually in the
     * inputs. Nothing is voided — whatever is not taken simply stays in the buses/hatch for the next mission.
     *
     * @return true when the payload is ready for launch; false when the launch must be aborted (the error was
     *         reported to the players)
     */
    private boolean prepareConstructorMission(long aTick, NBTTagCompound payload, USSInfrastructure infrastructure,
        USSProject project) {
        // The ship's per-mission caps: its construction power on the Starlifter scale (creative-loop friendly).
        long constructionPower = Math.max(1L, VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_CONSTRUCTION));
        long plasmaCap = USSConstants.starlifterPlasmaAmount(constructionPower);
        long dustCap = USSConstants.starlifterMatterAmount(constructionPower);

        Map<String, Long> consumed = new LinkedHashMap<>();
        Map<String, Long> available = new LinkedHashMap<>();
        for (USSProject.Cost cost : project.costs) {
            consumed.put(cost.materialName, infrastructure.consumed(project.id, cost.materialName));
            Materials material = Materials.get(cost.materialName);
            if (material == null || material == Materials._NULL) {
                available.put(cost.materialName, 0L); // unresolvable material — the entry simply cannot be loaded
                continue;
            }
            if (cost.kind == USSProject.Kind.ITEM) {
                ItemStack dust = material.getDust(1);
                available.put(cost.materialName, dust == null ? 0L : countItemInput(dust));
            } else {
                FluidStack plasma = material.getFluid(1);
                available.put(cost.materialName, plasma == null ? 0L : countFluidInput(plasma));
            }
        }

        Map<String, Long> take = USSLoadout.compute(project, consumed, plasmaCap, dustCap, available);
        if (take.isEmpty()) {
            reportError(aTick, "no_materials");
            return false;
        }

        // Deplete exactly the computed loadout (the same slot set the availability scan counted).
        for (USSProject.Cost cost : project.costs) {
            Long amount = take.get(cost.materialName);
            if (amount == null || amount <= 0L) {
                continue;
            }
            Materials material = Materials.get(cost.materialName);
            if (material == null || material == Materials._NULL) {
                continue;
            }
            if (cost.kind == USSProject.Kind.ITEM) {
                pullItemInput(material.getDust(1), amount);
            } else {
                pullFluidInput(material.getFluid(1), amount);
            }
        }

        writeConstructorLoadout(payload, project, take);
        try {
            LOGGER.info("[Voidcraft] gateway loaded constructor for project " + project.id + " with " + take);
        } catch (Throwable ignored) {}
        return true;
    }

    /**
     * Sum the matching dust stacks over the slots the depletion can reach: input-hatch slot 0 of every input hatch
     * plus every slot of every input bus (the same slot set {@code depleteInput} walks).
     */
    private long countItemInput(ItemStack match) {
        long total = 0L;
        if (match == null) {
            return 0L;
        }
        for (MTEHatchInput hatch : GTUtility.validMTEList(mInputHatches)) {
            IGregTechTileEntity base = hatch.getBaseMetaTileEntity();
            if (base == null) {
                continue;
            }
            ItemStack inSlot = base.getStackInSlot(0);
            if (inSlot != null && GTUtility.areStacksEqual(match, inSlot)) {
                total += inSlot.stackSize;
            }
        }
        for (MTEHatchInputBus bus : GTUtility.validMTEList(mInputBusses)) {
            IGregTechTileEntity base = bus.getBaseMetaTileEntity();
            if (base == null) {
                continue;
            }
            for (int i = 0; i < base.getSizeInventory(); i++) {
                ItemStack inSlot = base.getStackInSlot(i);
                if (inSlot != null && GTUtility.areStacksEqual(match, inSlot)) {
                    total += inSlot.stackSize;
                }
            }
        }
        return total;
    }

    /**
     * Sum the matching fluid over every input hatch tank (ME input hatch amounts are best-effort — the actual
     * drain at depletion time is the authoritative amount).
     */
    private long countFluidInput(FluidStack match) {
        long total = 0L;
        if (match == null) {
            return 0L;
        }
        for (FluidStack stored : getStoredFluids()) {
            if (stored != null && stored.getFluid() == match.getFluid()) {
                total += stored.amount;
            }
        }
        return total;
    }

    /**
     * Deplete {@code amount} of the matching dust from the input slots (hatch slot 0 first, then bus slots — partial
     * per-slot takes, so split stacks drain completely; returns the amount actually taken).
     */
    private long pullItemInput(ItemStack match, long amount) {
        if (match == null || amount <= 0L) {
            return 0L;
        }
        long remaining = amount;
        for (MTEHatchInput hatch : GTUtility.validMTEList(mInputHatches)) {
            if (remaining <= 0L) {
                break;
            }
            IGregTechTileEntity base = hatch.getBaseMetaTileEntity();
            if (base == null) {
                continue;
            }
            ItemStack inSlot = base.getStackInSlot(0);
            if (inSlot != null && GTUtility.areStacksEqual(match, inSlot)) {
                int take = (int) Math.min(remaining, inSlot.stackSize);
                base.decrStackSize(0, take);
                remaining -= take;
            }
        }
        for (MTEHatchInputBus bus : GTUtility.validMTEList(mInputBusses)) {
            if (remaining <= 0L) {
                break;
            }
            IGregTechTileEntity base = bus.getBaseMetaTileEntity();
            if (base == null) {
                continue;
            }
            for (int i = 0; i < base.getSizeInventory() && remaining > 0L; i++) {
                ItemStack inSlot = base.getStackInSlot(i);
                if (inSlot != null && GTUtility.areStacksEqual(match, inSlot)) {
                    int take = (int) Math.min(remaining, inSlot.stackSize);
                    base.decrStackSize(i, take);
                    remaining -= take;
                }
            }
        }
        return amount - remaining;
    }

    /**
     * Drain {@code amount} mB of the matching fluid from the input hatches (returns the amount actually drained —
     * an ME hatch may deliver less than the scanned amount).
     */
    private long pullFluidInput(FluidStack match, long amount) {
        if (match == null || amount <= 0L) {
            return 0L;
        }
        FluidStack toDrain = match.copy();
        toDrain.amount = (int) Math.min(Integer.MAX_VALUE, amount);
        return depleteInputQuantity(toDrain, false);
    }

    /**
     * Write the mission loadout into the ship payload in the same abstract entry format as the mining cargo
     * ({@code USSShipCargo} lists) plus the project id — the USS applies it at mission completion.
     */
    private void writeConstructorLoadout(NBTTagCompound payload, USSProject project, Map<String, Long> take) {
        payload.setInteger(VoidcraftNbt.TAG_PROJECT, project.id);
        NBTTagCompound loadout = new NBTTagCompound();
        NBTTagList items = new NBTTagList();
        NBTTagList fluids = new NBTTagList();
        for (USSProject.Cost cost : project.costs) {
            Long amount = take.get(cost.materialName);
            if (amount == null || amount <= 0L) {
                continue;
            }
            if (cost.kind == USSProject.Kind.ITEM) {
                Materials material = Materials.get(cost.materialName);
                if (material == null || material == Materials._NULL) {
                    continue;
                }
                ItemStack one = material.getDust(1);
                if (one == null) {
                    continue;
                }
                NBTTagCompound entry = new NBTTagCompound();
                entry.setShort(USSShipCargo.ENTRY_ID, (short) Item.getIdFromItem(one.getItem()));
                entry.setShort(USSShipCargo.ENTRY_DAMAGE, (short) one.getItemDamage());
                entry.setInteger(USSShipCargo.ENTRY_AMOUNT, (int) Math.min(Integer.MAX_VALUE, amount));
                entry.setString(USSShipCargo.ITEM_ENTRY_MATERIAL, cost.materialName); // self-describing (no reverse
                                                                                      // lookup at apply time)
                items.appendTag(entry);
            } else {
                NBTTagCompound entry = new NBTTagCompound();
                entry.setString(USSShipCargo.FLUID_ENTRY_MATERIAL, cost.materialName);
                entry.setLong(USSShipCargo.FLUID_ENTRY_AMOUNT, Math.min(Integer.MAX_VALUE, amount));
                fluids.appendTag(entry);
            }
        }
        loadout.setTag(USSShipCargo.TAG_ITEMS, items);
        loadout.setTag(USSShipCargo.TAG_FLUIDS, fluids);
        payload.setTag(VoidcraftNbt.TAG_LOADOUT, loadout);
    }

    /**
     * (Re-)scan for the nearest ignited USS and a valid Storage Bay within {@link #SCAN_RADIUS}, at most once every
     * {@link #SCAN_COOLDOWN_TICKS} — the sweep checks every position in the sphere (full resolution), which is too
     * expensive to repeat every tick while a ship is docked. Between sweeps the cached result (including "not
     * found") is reused.
     */
    private void refreshLaunchTargets(long aTick, World world, int cx, int cy, int cz) {
        // NB: the `!= Long.MIN_VALUE` checks are NOT optional — the sentinels start at Long.MIN_VALUE, so
        // `aTick - Long.MIN_VALUE` overflows to a large negative and `< SCAN_COOLDOWN_TICKS` is always true,
        // which would early-return on every call and the scan (and the diagnostics below) would never run.
        // Short-circuiting on the sentinel guarantees the first call always scans.
        if (lastTargetScan != Long.MIN_VALUE && aTick - lastTargetScan < SCAN_COOLDOWN_TICKS) {
            return;
        }
        lastTargetScan = aTick;
        diag.setLength(0);
        // A FULL USS (all ship slots occupied) is NOT a valid launch target: it would reject the launch
        // (uss_busy) — and a Constructor launch is rejected only AFTER its loadout was already pulled from the
        // inputs (material loss). A USS with free slots is a valid target even while other ships are in flight
        // (Phase 4 pass 4 — multiple ships per star system).
        targetUSS = findNearest(
            world,
            cx,
            cy,
            cz,
            MTEUnstableSolarSystem.class,
            m -> m.isStarIgnited() && m.hasFreeShipSlot());
        diag.append(" | ");
        targetBay = findNearest(world, cx, cy, cz, MTEVoidcraftStorageBay.class, b -> b.mMachine);
        if ((targetUSS == null || targetBay == null)
            && (lastDiagTick == Long.MIN_VALUE || aTick - lastDiagTick >= 100)) {
            lastDiagTick = aTick;
            emitScanDiagnostics(diag.toString());
        }
    }

    /**
     * Broadcast a failed target scan's details (game log + chat for nearby players) so "no USS / no bay" can be
     * diagnosed in-game without the server console.
     */
    private void emitScanDiagnostics(String details) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return;
        }
        String msg = "launch-target scan @ " + base
            .getXCoord() + "," + base.getYCoord() + "," + base.getZCoord() + " — " + details;
        try {
            LOGGER.info("[Voidcraft] gateway " + msg);
        } catch (Throwable ignored) {
            // Logging must never break the tick.
        }
        World world = base.getWorld();
        if (world == null) {
            return;
        }
        double cx = base.getXCoord() + 0.5;
        double cy = base.getYCoord() + 0.5;
        double cz = base.getZCoord() + 0.5;
        for (EntityPlayer player : world.playerEntities) {
            double dx = player.posX - cx;
            double dy = player.posY - cy;
            double dz = player.posZ - cz;
            if (dx * dx + dy * dy + dz * dz <= 16.0D * 16.0D) {
                player.addChatMessage(new ChatComponentText("[Voidcraft] " + msg));
            }
        }
    }

    /**
     * Scan the full {@link #SCAN_RADIUS} sphere at full resolution for the nearest valid MTE of the given type
     * (null when none is found).
     *
     * <p>
     * A coarse-grid scan (step &gt; 1) must NOT be used here: it only tests positions exactly on grid lines, so a
     * perfectly valid target a few blocks off a line (e.g. the USS 5–10 blocks away) is silently invisible to it.
     * The sweep is therefore rate-limited by the caller ({@link #refreshLaunchTargets}).
     */
    private <T> T findNearest(World world, int cx, int cy, int cz, Class<T> type,
        java.util.function.Predicate<T> valid) {
        T best = null;
        long bestDist = Long.MAX_VALUE;
        int candidates = 0;
        int r = SCAN_RADIUS;
        for (int x = cx - r; x <= cx + r; x++) {
            for (int y = Math.max(1, cy - r); y <= Math.min(world.getHeight() - 1, cy + r); y++) {
                for (int z = cz - r; z <= cz + r; z++) {
                    // Skip positions outside the scan sphere (the loop walks a box).
                    long dx = x - cx;
                    long dy = y - cy;
                    long dz = z - cz;
                    if (dx * dx + dy * dy + dz * dz > (long) r * r) {
                        continue;
                    }
                    TileEntity te = world.getTileEntity(x, y, z);
                    IMetaTileEntity mte = GTUtility.getMetaTileEntity(te);
                    if (mte == null || !type.isInstance(mte)) {
                        continue;
                    }
                    T candidate = type.cast(mte);
                    boolean ok;
                    try {
                        ok = valid.test(candidate);
                    } catch (Throwable t) {
                        ok = false;
                    }
                    candidates++;
                    diag.append(type.getSimpleName())
                        .append("@(")
                        .append(x)
                        .append(',')
                        .append(y)
                        .append(',')
                        .append(z)
                        .append(")[")
                        .append(ok ? "valid" : "rejected")
                        .append("] ");
                    if (!ok) {
                        continue;
                    }
                    long dist = (long) (te.xCoord - cx) * (te.xCoord - cx) + (long) (te.yCoord - cy) * (te.yCoord - cy)
                        + (long) (te.zCoord - cz) * (te.zCoord - cz);
                    if (dist < bestDist) {
                        bestDist = dist;
                        best = candidate;
                    }
                }
            }
        }
        if (candidates == 0) {
            diag.append(type.getSimpleName())
                .append(": 0 candidates within ")
                .append(r)
                .append(" blocks");
        }
        return best;
    }

    private void reportError(long aTick, String key) {
        if (aTick - lastErrorTick < 40) {
            return;
        }
        lastErrorTick = aTick;
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null) {
            return;
        }
        World world = base.getWorld();
        if (world == null) {
            return;
        }
        double cx = base.getXCoord() + 0.5;
        double cy = base.getYCoord() + 0.5;
        double cz = base.getZCoord() + 0.5;
        for (EntityPlayer player : world.playerEntities) {
            double dx = player.posX - cx;
            double dy = player.posY - cy;
            double dz = player.posZ - cz;
            if (dx * dx + dy * dy + dz * dz <= 16.0D * 16.0D) {
                player.addChatMessage(new ChatComponentTranslation("tt.voidcraft.gateway.error." + key));
            }
        }
    }

    // endregion

    // region legacy dock anchor cleanup

    /** Once per MTE lifetime (in-memory; the check is idempotent either way). */
    private boolean legacyDockCleaned;

    /**
     * Older builds rendered a docked-ship "preview" hologram anchored two blocks above the gateway controller. That
     * feature is gone — the gateway renders nothing; the ship's hologram exists only in flight and is owned by the
     * USS — so clear a stray legacy anchor if one is still in the world (otherwise it renders forever: nothing else
     * knows about it).
     */
    private void cleanupLegacyDockAnchor() {
        if (legacyDockCleaned) {
            return;
        }
        legacyDockCleaned = true;
        IGregTechTileEntity base = getBaseMetaTileEntity();
        World world = base == null ? null : base.getWorld();
        if (world == null || world.isRemote) {
            return;
        }
        int x = base.getXCoord();
        int y = base.getYCoord() + 2;
        int z = base.getZCoord();
        if (world.getBlock(x, y, z) == VoidcraftLoader.sBlockVoidcraftShipRender) {
            world.setBlockToAir(x, y, z);
            try {
                LOGGER.info("[Voidcraft] gateway removed legacy dock anchor @ " + x + "," + y + "," + z);
            } catch (Throwable ignored) {}
        }
    }

    // endregion

    @Override
    public String[] getInfoData() {
        List<String> str = new java.util.ArrayList<>(java.util.Arrays.asList(super.getInfoData()));
        str.add("tt.voidcraft.gateway.infodata.header");
        ItemStack ship = getControllerSlot();
        if (ship != null) {
            str.add(IGregTechDeviceInformation.encode("tt.voidcraft.gateway.infodata.ship", ship.getDisplayName()));
        } else {
            str.add(IGregTechDeviceInformation.encode("tt.voidcraft.gateway.infodata.ship", ""));
        }
        return str.toArray(new String[0]);
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(translateToLocal("gt.mbtt.machine_type.launchpad"))
            .addMarkdown(new ResourceLocation("gregtech", "voidcraft-gateway"))
            .beginStructureBlock(3, 3, 3, false)
            .addController(translateToLocal("tt.keyword.Structure.FrontCenter3rd"))
            .addCasing("18", new ItemStack(TTCasingsContainer.sBlockCasingsBA0, 1, 10).getDisplayName(), false)
            .addInputBus("1+", translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1)
            .addInputHatch("1+", translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) {
            return -1;
        }
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 0, elementBudget, source, actor, false, true);
    }

    @Override
    public IStructureDefinition<MTEVoidcraftGateway> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        // No energy input at all → the maintenance (damage/repair) system does not apply; it also cannot be
        // sensibly serviced through a maintenance hatch. Existing issues are auto-fixed on load.
        return false;
    }

    @Override
    public boolean isSafeVoidButtonEnabled() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

}
