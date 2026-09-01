package tectech.voidcraft.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.voidcraft.item.ItemVoidbaseBlueprint;
import tectech.voidcraft.item.ItemVoidcraft;
import tectech.voidcraft.loader.VoidcraftLoader;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftCoverComponent;
import tectech.voidcraft.ship.VoidcraftEngineType;
import tectech.voidcraft.ship.VoidcraftFuel;
import tectech.voidcraft.ship.VoidcraftNbt;
import tectech.voidcraft.uss.CargoHold;
import tectech.voidcraft.uss.MTEUnstableSolarSystem;
import tectech.voidcraft.uss.USSBaseAnchor;
import tectech.voidcraft.uss.USSCommand;
import tectech.voidcraft.uss.USSItemCargo;
import tectech.voidcraft.uss.USSNode;
import tectech.voidcraft.uss.USSProgram;
import tectech.voidcraft.uss.USSProgramDefaults;
import tectech.voidcraft.uss.VoidcraftActiveShip;

/**
 * Voidcraft Gateway (EoH rework, Phase 3).
 *
 * <p>
 * A 3×3×3 BA0-cased shell with the controller at the front-face center, ringed by the hatch-capable perimeter.
 * The INPUT buses are the ship source: feed a digitized
 * {@link ItemVoidcraft} through them and — when a valid Unstable Solar System (star ignited) and a Storage Bay
 * are both within range — the gateway launches the ship
 * on a mining mission: the ship flies out to the star, mines, and returns with cargo, which the USS delivers to
 * the nearest bay. A ship's integrity is its time limit (it drops 1 per second while in the USS): a ship that
 * finishes before it expires is re-emitted into the gateway's OUTPUT bus (integrity back at maximum); one that
 * hits 0 is lost with its cargo.
 *
 * <p>
 * No energy hatches, no recipes — the interaction surface is the BLUEPRINT slot (right-click with a Voidbase
 * blueprint item in hand — the blueprint is KEPT, the constructor ship carries a data copy) plus the front-face
 * hatch ring: the input side (buses + hatches) feeds the ship and its cargo — everything on the input side but
 * the ship item loads into the ship's cargo hold at launch (no validity checks; the user loads what the mission
 * needs, a Constructor the parts it carries). The output bus is the return path for the surviving ship — the
 * structure check requires an input bus AND an output bus.
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
     * Minimum interval (ticks) between target scans while a ship waits on the input side. The scan sweeps the
     * whole sphere at full resolution, so the result is cached instead of re-scanning every tick.
     */
    private static final long SCAN_COOLDOWN_TICKS = 20;

    /**
     * 3×3×3 shell: the <strong>front face</strong> (z=0) is the controller (center) ringed by the hatch-capable
     * perimeter 'C' (input buses + input hatches — the SHIP source, a launch consumes one Voidcraft from them,
     * and the CARGO source: everything on the input side but the ship loads into the ship's cargo hold at
     * launch; and the output bus — the return path for the surviving ship; non-hatch ring
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
                // InputBus: the cargo source (everything but the ship item, incl. ME item buses).
                // InputHatch slot 0 is also read for items (the standard GT hatch slot).
                // OutputBus: the return path for the surviving ship (mission complete).
                // Non-hatch ring cells fall back to the HighPowerCasing render.
                .atLeast(InputBus, InputHatch, OutputBus)
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

    /**
     * The Voidbase blueprint in the blueprint slot (a single {@code ItemVoidbaseBlueprint} stack — REUSABLE: it
     * stays here between launches; each Constructor launch copies its data into the ship payload). Server-only:
     * the gateway has no GUI and renders nothing of its own, so no client sync.
     */
    private ItemStack blueprint;

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
        // The input bus is the ship source (and the Constructor parts path); the output bus is the return path
        // (surviving ships + unused Constructor parts) — both are required.
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
    }

    @Override
    public CheckRecipeResult checkProcessing_EM() {
        // Not a recipe machine — the launch logic runs in onPostTick.
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        // Insert a Voidbase blueprint into the empty blueprint slot (the item is consumed from the hand; the
        // blueprint is KEPT in the slot for the next launch). Ships are not inserted by hand — the gateway
        // consumes them from its input buses.
        if (blueprint == null && aBaseMetaTileEntity.isServerSide()) {
            ItemStack heldItem = aPlayer.getHeldItem();
            if (heldItem != null && heldItem.getItem() == ItemVoidbaseBlueprint.INSTANCE
                && !ItemVoidbaseBlueprint.isEmptyBlueprint(heldItem)) {
                blueprint = heldItem.copy();
                blueprint.stackSize = 1;
                aPlayer.setCurrentItemOrArmor(0, ItemUtils.depleteStack(heldItem, 1));
                return true;
            }
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    /** Persist the blueprint slot (ships live in the input buses, nothing of the machine's own to save). */
    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (blueprint != null) {
            NBTTagCompound stackTag = new NBTTagCompound();
            blueprint.writeToNBT(stackTag);
            aNBT.setTag("vc_blueprint", stackTag);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        blueprint = null;
        if (aNBT.hasKey("vc_blueprint")) {
            ItemStack stack = ItemStack.loadItemStackFromNBT(aNBT.getCompoundTag("vc_blueprint"));
            if (stack != null && stack.getItem() == ItemVoidbaseBlueprint.INSTANCE
                && !ItemVoidbaseBlueprint.isEmptyBlueprint(stack)) {
                blueprint = stack;
            }
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) {
            return;
        }
        cleanupLegacyDockAnchor();
        migrateLegacyShipSlot();
        if (!mMachine) {
            return;
        }

        attemptLaunch(aTick);
    }

    // region launch

    /**
     * Try to launch a ship from the input side (input hatch slot 0 + input buses). The ship item is consumed
     * from the input ONLY when the launch succeeds — a rejected launch leaves the ship in the input (and the
     * error is reported, rate-limited, to nearby players).
     *
     * <p>
     * Launch fuel (validated + drained against the INPUT HATCHES only, same tick as the successful launch): a
     * ship with a fuel-burning engine needs its fuel tank FULL (the blueprint's Fuel Storage capacity, mB of the
     * engine's fluid) and every reactor cover its per-reactor launch fuel (Deuterium / Semi-Stable Antimatter,
     * scaled by the reactor count per type). A shortfall rejects the launch — ship and fuel stay in place.
     */
    private void attemptLaunch(long aTick) {
        IGregTechTileEntity base = getBaseMetaTileEntity();
        World world = base == null ? null : base.getWorld();
        if (world == null) {
            return;
        }
        ItemStack ship = findShipInput();
        if (ship == null) {
            return; // idle — no ship on the input side
        }

        // The ship payload is the item's tag compound (the vc_* keys sit at its top level — see
        // ItemVoidcraft.getBlueprint). The full ItemStack NBT nests it one level deeper under the vanilla
        // "tag" key, so writeToNBT must NOT be used here. It is COPIED before the mission is loaded: the
        // launch mutates the payload (the cargo hold, the Constructor's blueprint data + flag), and the input
        // item is consumed by full-NBT equality against the bus's stack — a mutated payload would never match.
        NBTTagCompound payload = (NBTTagCompound) ship.getTagCompound()
            .copy();
        if (payload == null || VoidcraftNbt.read(payload) == null) {
            reportError(aTick, "invalid_ship");
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

        // The launch fuel (validated + drained against the INPUT HATCHES only — the ship's fuel tank is filled
        // at launch, the reactor fees are paid at launch): the ship's fuel-burning engine needs its tank FULL
        // (the blueprint's Fuel Storage capacity), and every reactor cover needs its per-reactor launch fuel.
        // A shortfall rejects the launch (the ship AND the fuel stay in place).
        VoidcraftBlueprint launchBlueprint = VoidcraftNbt.read(payload);
        if (!preflightLaunchFuel(aTick, launchBlueprint, payload)) {
            return;
        }

        // The ship's cargo: everything on the input side (the input-hatch slot 0 + every input-bus slot) but the
        // ship item itself loads into the ship's cargo hold — no validity checks (it is up to the user to load
        // what the mission needs, for whatever role the ship is), clamped by the ship's cargo space (a full hold
        // simply stops accepting). The hold enters the USS as part of the ship payload.
        loadCargoFromInput(payload);

        // A Voidbase blueprint in the blueprint slot turns the launch into a construction mission: the ship
        // leaves loaded with a DATA COPY of the blueprint (the item stays in the slot — reusable). The parts it
        // builds with are its cargo — the CONSTRUCT leg credits the site part by part, drawing from the hold (the
        // first Constructor creates the construction site, the rest fill it). Without a blueprint the launch is a
        // plain mission.
        if (prepareVoidbaseMission(payload)) {
            payload.setBoolean(VoidcraftNbt.TAG_BUILD_MISSION, true);
        }

        int[] bayPos = new int[] { targetBay.getBaseMetaTileEntity()
            .getXCoord(),
            targetBay.getBaseMetaTileEntity()
                .getYCoord(),
            targetBay.getBaseMetaTileEntity()
                .getZCoord() };

        if (targetUSS.launchShip(payload, new int[] { cx, cy, cz }, bayPos)) {
            // The launch is in flight — consume the ship from the input side. The pull matches on item +
            // payload NBT (the consumed ship IS the found ship, even when several ships wait on the input),
            // and the find + pull run in the same tick, so the found stack cannot vanish in between.
            if (!pullShipInput(ship)) {
                try {
                    LOGGER.warn("[Voidcraft] gateway launched a ship but could not consume its item from the input");
                } catch (Throwable ignored) {}
            }
            // The validated launch fuel leaves the input hatches in the same tick (the tank + the reactor fees).
            pullLaunchFuel(launchBlueprint, payload);
        } else {
            reportError(aTick, "uss_busy");
        }
    }

    /**
     * The launch fuel preflight (input hatches only): the ship's fuel-burning engine needs its tank FULL
     * (capacity = the blueprint's Fuel Storage stat) and every reactor cover needs its per-reactor launch fuel
     * (scaled by the reactor count, per type).
     *
     * @return false (error reported, rate-limited) when an input hatch cannot cover a requirement.
     */
    private boolean preflightLaunchFuel(long aTick, VoidcraftBlueprint blueprint, NBTTagCompound payload) {
        long tankCapacity = Math.max(0L, VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_FUEL));
        Fluid tankFluid = VoidcraftFuel
            .engineFuel(VoidcraftEngineType.byId(VoidcraftNbt.readInt(payload, VoidcraftNbt.TAG_ENGINE)));
        if (tankFluid != null && tankCapacity > 0L && countFluidInput(tankFluid) < tankCapacity) {
            reportError(aTick, "fuel_short", " " + tankFluid.getLocalizedName());
            return false;
        }
        if (blueprint != null) {
            for (Map.Entry<VoidcraftCoverComponent, Long> fee : blueprint.reactorLaunchFuel()
                .entrySet()) {
                Fluid feeFluid = VoidcraftFuel.reactorLaunchFluid(fee.getKey());
                long required = Math.max(0L, fee.getValue());
                if (feeFluid == null || countFluidInput(feeFluid) < required) {
                    reportError(aTick, "reactor_fuel_short", feeFluid == null ? "" : " " + feeFluid.getLocalizedName());
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Drain the validated launch fuel from the input hatches (same tick as the successful launch): the tank
     * fill + every reactor's launch fuel.
     */
    private void pullLaunchFuel(VoidcraftBlueprint blueprint, NBTTagCompound payload) {
        long tankCapacity = Math.max(0L, VoidcraftNbt.readLong(payload, VoidcraftNbt.TAG_FUEL));
        Fluid tankFluid = VoidcraftFuel
            .engineFuel(VoidcraftEngineType.byId(VoidcraftNbt.readInt(payload, VoidcraftNbt.TAG_ENGINE)));
        if (tankFluid != null && tankCapacity > 0L) {
            pullFluidInput(tankFluid, tankCapacity);
        }
        if (blueprint != null) {
            for (Map.Entry<VoidcraftCoverComponent, Long> fee : blueprint.reactorLaunchFuel()
                .entrySet()) {
                Fluid feeFluid = VoidcraftFuel.reactorLaunchFluid(fee.getKey());
                long required = Math.max(0L, fee.getValue());
                if (feeFluid != null && required > 0L) {
                    pullFluidInput(feeFluid, required);
                }
            }
        }
    }

    /**
     * The total mB of a fluid across the INPUT HATCHES (the ship's fuel + reactor fees come from hatches only —
     * not the buses).
     */
    private long countFluidInput(Fluid fluid) {
        if (fluid == null) {
            return 0L;
        }
        long total = 0L;
        for (MTEHatchInput hatch : GTUtility.validMTEList(mInputHatches)) {
            FluidStack held = hatch.getFluid();
            if (held != null && held.getFluid() == fluid) {
                total += held.amount;
            }
        }
        return total;
    }

    /**
     * Drain up to {@code amount} mB of a fluid from the INPUT HATCHES.
     *
     * @return the mB actually drained (the shortfall stays in the hatches — a launch that was preflight-checked
     *         cannot short).
     */
    private long pullFluidInput(Fluid fluid, long amount) {
        if (fluid == null || amount <= 0L) {
            return 0L;
        }
        long remaining = amount;
        for (MTEHatchInput hatch : GTUtility.validMTEList(mInputHatches)) {
            if (remaining <= 0L) {
                break;
            }
            FluidStack held = hatch.getFluid();
            if (held == null || held.getFluid() != fluid) {
                continue;
            }
            FluidStack drained = hatch.drain((int) Math.min(remaining, held.amount), true);
            if (drained != null) {
                remaining -= drained.amount;
            }
        }
        return amount - remaining;
    }

    /**
     * Load the ship's cargo from the input side (input-hatch slot 0 + every input-bus slot — the same slot set
     * the ship is found on): everything there but the digitized ship items becomes cargo (keyed by item identity
     * — {@link USSItemCargo#keyOf}), clamped by the ship's cargo space (a full hold simply stops accepting; the
     * rest stays in the input). The gateway does no validity checks: it is up to the user to load what the
     * mission needs — the parts a Voidbase Constructor has to carry, the infrastructure payloads a base has to
     * build, the Field Generators a matrix consumes, or anything else the user wants carried. The updated hold
     * is written into the payload; the consumed stacks leave the input in the same tick.
     */
    private void loadCargoFromInput(NBTTagCompound payload) {
        CargoHold hold = CargoHold.of(VoidcraftActiveShip.holdCapacityFor(payload));
        for (MTEHatchInput hatch : GTUtility.validMTEList(mInputHatches)) {
            IGregTechTileEntity base = hatch.getBaseMetaTileEntity();
            if (base != null) {
                hold = pullCargo(hold, base, 0);
            }
        }
        for (MTEHatchInputBus bus : GTUtility.validMTEList(mInputBusses)) {
            IGregTechTileEntity base = bus.getBaseMetaTileEntity();
            if (base == null) {
                continue;
            }
            for (int i = 0; i < base.getSizeInventory(); i++) {
                hold = pullCargo(hold, base, i);
            }
        }
        NBTTagCompound holdTag = new NBTTagCompound();
        hold.writeToNBT(holdTag);
        payload.setTag(VoidcraftNbt.TAG_HOLD, holdTag);
    }

    /**
     * Pull one input slot into the hold (a digitized ship item is never cargo — the launch consumes it
     * separately; a full hold keeps the slot untouched).
     *
     * @return the updated hold (unchanged when the slot is empty, a ship item, or the hold has no room)
     */
    private CargoHold pullCargo(CargoHold hold, IGregTechTileEntity base, int slot) {
        if (hold.isFull()) {
            return hold;
        }
        ItemStack stack = base.getStackInSlot(slot);
        if (stack == null || stack.getItem() == ItemVoidcraft.INSTANCE) {
            return hold;
        }
        String key = USSItemCargo.keyOf(stack);
        if (key == null) {
            return hold;
        }
        long take = Math.min(stack.stackSize, hold.remainingUnits());
        if (take <= 0L) {
            return hold;
        }
        base.decrStackSize(slot, (int) take);
        return hold.addItem(key, take);
    }

    /**
     * Load a Voidbase construction mission into the ship payload: copy the blueprint's data (the blueprint item
     * stays in the slot — reusable). The parts a Constructor has to carry are plain cargo (see
     * {@link #loadCargoFromInput}) — the CONSTRUCT leg credits the site part by part, drawing them from the
     * ship's hold.
     *
     * @return true when the payload carries the blueprint data; false when there is no (valid) blueprint in the
     *         blueprint slot (the launch then proceeds as a plain mission)
     */
    private boolean prepareVoidbaseMission(NBTTagCompound payload) {
        if (blueprint == null || blueprint.getItem() != ItemVoidbaseBlueprint.INSTANCE
            || ItemVoidbaseBlueprint.isEmptyBlueprint(blueprint)) {
            return false;
        }
        NBTTagCompound blueprintNbt = blueprint.getTagCompound();
        if (blueprintNbt == null || VoidcraftNbt.readBase(blueprintNbt) == null) {
            return false;
        }

        payload.setTag(VoidcraftNbt.TAG_BUILD_BLUEPRINT, blueprintNbt.copy());
        try {
            USSBaseAnchor anchor = resolveBuildAnchor(blueprintNbt);
            LOGGER.info(
                "[Voidcraft] gateway loaded a constructor for the Voidbase at "
                    + (anchor != null ? anchor : "?dynamic anchor"));
        } catch (Throwable ignored) {}
        return true;
    }

    /**
     * The construction anchor implied by the blueprint's stored program: the first MOVE instruction's target,
     * when it resolves to a STATIC anchor (STAR, a specific PLANET index, a specific RIPPLE index). Dynamic
     * targets (nearest/random planet, unscanned ripple) and non-anchor targets (HOME, SHIP) cannot be resolved
     * here — null (the launch log reports the anchor as dynamic).
     */
    @Nullable
    private static USSBaseAnchor resolveBuildAnchor(NBTTagCompound blueprintNbt) {
        if (!blueprintNbt.hasKey(VoidcraftNbt.TAG_PROGRAM)) {
            return null;
        }
        USSProgram program = USSProgram.readFromNBT(blueprintNbt.getTagList(VoidcraftNbt.TAG_PROGRAM, 10));
        if (program == null) {
            return null;
        }
        for (USSNode node : program.nodes()) {
            if (!node.isCommand() || node.cmdId() != USSCommand.MOVE) {
                continue;
            }
            NBTTagCompound params = node.params();
            String target = params.getString(USSProgramDefaults.PARAM_TARGET);
            switch (target) {
                case USSProgramDefaults.TARGET_STAR:
                    return USSBaseAnchor.star();
                case USSProgramDefaults.TARGET_PLANET:
                    return USSBaseAnchor.planet(params.getInteger(USSProgramDefaults.PARAM_INDEX));
                case USSProgramDefaults.TARGET_RIPPLE:
                    return USSBaseAnchor.ripple(params.getInteger(USSProgramDefaults.PARAM_INDEX));
                default:
                    return null;
            }
        }
        return null;
    }

    /**
     * The first Voidcraft on the input side (input-hatch slot 0 of every input hatch, then every slot of every
     * input bus). A copy is returned (the bus's stack is not touched); null when the input side holds no ship (the
     * gateway idles — no error).
     */
    @Nullable
    private ItemStack findShipInput() {
        for (MTEHatchInput hatch : GTUtility.validMTEList(mInputHatches)) {
            IGregTechTileEntity base = hatch.getBaseMetaTileEntity();
            if (base == null) {
                continue;
            }
            ItemStack inSlot = base.getStackInSlot(0);
            if (inSlot != null && inSlot.getItem() == ItemVoidcraft.INSTANCE) {
                return inSlot.copy();
            }
        }
        for (MTEHatchInputBus bus : GTUtility.validMTEList(mInputBusses)) {
            IGregTechTileEntity base = bus.getBaseMetaTileEntity();
            if (base == null) {
                continue;
            }
            for (int i = 0; i < base.getSizeInventory(); i++) {
                ItemStack inSlot = base.getStackInSlot(i);
                if (inSlot != null && inSlot.getItem() == ItemVoidcraft.INSTANCE) {
                    return inSlot.copy();
                }
            }
        }
        return null;
    }

    /**
     * Consume one ship from the input side — the stack equal to {@code match} (item + payload NBT, so the
     * consumed ship IS the found ship even when several ships wait on the input side). Called only on a
     * successful launch, in the same tick as the find.
     *
     * @param match the ship found by {@link #findShipInput()}
     * @return true when a ship was consumed
     */
    private boolean pullShipInput(ItemStack match) {
        for (MTEHatchInput hatch : GTUtility.validMTEList(mInputHatches)) {
            IGregTechTileEntity base = hatch.getBaseMetaTileEntity();
            if (base == null) {
                continue;
            }
            ItemStack inSlot = base.getStackInSlot(0);
            if (inSlot != null && GTUtility.areStacksEqual(match, inSlot)) {
                base.decrStackSize(0, 1);
                return true;
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
                    base.decrStackSize(i, 1);
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Output an item stack into the output bus buffers — each bus absorbs as much as it can, and whatever does
     * not fit stays in the given stack (its {@code stackSize} is reduced by the absorbed amount). Used for the
     * returned ship (mission complete).
     *
     * @param stack the stack to output (mutated in place: only the absorbed amount is removed)
     * @return the amount actually absorbed by the output buses (0 when the gateway has none)
     */
    public int outputItem(ItemStack stack) {
        if (stack == null || stack.stackSize <= 0) {
            return 0;
        }
        int before = stack.stackSize;
        for (MTEHatchOutputBus bus : GTUtility.validMTEList(mOutputBusses)) {
            bus.storePartial(stack, false);
            if (stack.stackSize <= 0) {
                break;
            }
        }
        return before - stack.stackSize;
    }

    /**
     * (Re-)scan for the nearest ignited USS and a valid Storage Bay within {@link #SCAN_RADIUS}, at most once every
     * {@link #SCAN_COOLDOWN_TICKS} — the sweep checks every position in the sphere (full resolution), which is too
     * expensive to repeat every tick while a ship waits on the input side. Between sweeps the cached result
     * (including "not found") is reused.
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
        // (uss_busy) — and a launch is rejected only AFTER its cargo was already pulled from the inputs
        // (material loss). A USS with free slots is a valid target even while other ships are in flight
        // (Phase 4 pass 4 — multiple ships per star system).
        targetUSS = findNearest(
            world,
            cx,
            cy,
            cz,
            MTEUnstableSolarSystem.class,
            m -> m.isStarIgnited() && m.hasFreeShipSlot());
        // Register this gateway with the USS it locked on to: the USS renders the gateway as a permanent part of
        // the system view (the fleet anchor's gateway list, independent of the fleet's ship list). The client-side
        // MTE mirror runs the same scan, but its registration is a no-op there (the fleet anchor sync is
        // server-only).
        if (!world.isRemote && targetUSS != null) {
            targetUSS.registerGateway(cx, cy, cz);
        }
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
        reportError(aTick, key, null);
    }

    /**
     * Same as {@link #reportError(long, String)} with a plain-text suffix appended (the fuel/fees errors carry
     * the fluid's name).
     */
    private void reportError(long aTick, String key, String suffix) {
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
        String suffixText = (suffix == null || suffix.isEmpty()) ? "" : suffix;
        for (EntityPlayer player : world.playerEntities) {
            double dx = player.posX - cx;
            double dy = player.posY - cy;
            double dz = player.posZ - cz;
            if (dx * dx + dy * dy + dz * dz <= 16.0D * 16.0D) {
                ChatComponentText message = new ChatComponentText(
                    translateToLocal("tt.voidcraft.gateway.error." + key) + suffixText);
                player.addChatMessage(message);
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

    /**
     * Once per MTE lifetime: a ship still sitting in the old docked slot (the controller's inventory slot) is
     * unreachable now that ships come from the input buses — hand it to the output bus (drop what does not
     * fit), so the item is not lost in an existing save.
     */
    private boolean legacyShipSlotMigrated;

    private void migrateLegacyShipSlot() {
        if (legacyShipSlotMigrated) {
            return;
        }
        legacyShipSlotMigrated = true;
        ItemStack docked = getControllerSlot();
        if (docked == null) {
            return;
        }
        mInventory[getControllerSlotIndex()] = null;
        updateSlots();
        int absorbed = outputItem(docked);
        if (docked.stackSize > 0) {
            IGregTechTileEntity base = getBaseMetaTileEntity();
            World world = base == null ? null : base.getWorld();
            if (world != null) {
                GTUtility.dropItemsOrClusters(
                    world,
                    base.getXCoord() + 0.5f,
                    base.getYCoord() + 0.5f,
                    base.getZCoord() + 0.5f,
                    java.util.Collections.singletonList(docked));
            }
        }
        try {
            LOGGER.info(
                "[Voidcraft] gateway moved the legacy docked ship to the output bus ({} absorbed, {} dropped)",
                absorbed,
                docked.stackSize);
        } catch (Throwable ignored) {}
    }

    // endregion

    @Override
    public String[] getInfoData() {
        List<String> str = new java.util.ArrayList<>(java.util.Arrays.asList(super.getInfoData()));
        str.add("tt.voidcraft.gateway.infodata.header");
        if (blueprint != null) {
            str.add(
                IGregTechDeviceInformation
                    .encode("tt.voidcraft.gateway.infodata.blueprint", blueprint.getDisplayName()));
        } else {
            str.add(IGregTechDeviceInformation.encode("tt.voidcraft.gateway.infodata.blueprint", ""));
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
            .addOutputBus("1+", translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1)
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
