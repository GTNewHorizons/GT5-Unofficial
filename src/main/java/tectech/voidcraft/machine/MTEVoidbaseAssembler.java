package tectech.voidcraft.machine;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.casing.Casings;
import gregtech.api.enums.HatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.covers.Cover;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.voidcraft.cover.CoverVoidcraftComponent;
import tectech.voidcraft.item.ItemVoidbaseBlueprint;
import tectech.voidcraft.multiblock.MultiblockControllerRef;
import tectech.voidcraft.multiblock.VoidcraftMultiblockRegistry;
import tectech.voidcraft.render.AssemblerVisuals;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftComponent;
import tectech.voidcraft.ship.VoidcraftComponentRegistry;
import tectech.voidcraft.ship.VoidcraftConstants;
import tectech.voidcraft.ship.VoidcraftNbt;
import tectech.voidcraft.uss.USSProgram;

/**
 * Voidbase Assembler.
 *
 * <p>
 * A 15×15×3 multiblock whose front face (15×15) looks out over a 15×15×15 build volume. It scans that volume,
 * validates the placed components with the BASE rules (the ship rules minus the thruster audit — a Voidbase is an
 * immobile station) — exactly one controller, a frame, component/cover tier ≤ the assembler circuit tier — then
 * digitizes the build into a single non-stackable, REUSABLE {@code ItemVoidbaseBlueprint} and clears the component
 * blocks. Constructor voidcrafts carry the blueprint data and a parts loadout when they are launched from the
 * gateway; several constructors can build the same base (the first one creates the construction site, the rest
 * fill it).
 */
@IMetaTileEntity.SkipGenerateDescription
public class MTEVoidbaseAssembler extends TTMultiblockBase implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    /** Scan volume: 15 wide × 15 tall × 15 deep in front of the machine's front face (the base volume). */
    private static final int SCAN_WIDTH = 15;
    private static final int SCAN_HEIGHT = 15;
    private static final int SCAN_DEPTH = 15;

    /**
     * 15 wide × 15 tall × 3 deep. Controller at the front-center (x=7, y=7, z=0). Hatch slots (C) ring the front
     * face, 'B' (TT casing meta 7) pads the front interior, 'A' (TT casing meta 4) fills the two back planes.
     */
    private static final IStructureDefinition<MTEVoidbaseAssembler> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEVoidbaseAssembler>builder()
        .addShape(STRUCTURE_PIECE_MAIN, transpose(buildShape()))
        .addElement('A', ofBlock(TTCasingsContainer.sBlockCasingsTT, 4))
        .addElement('B', ofBlock(TTCasingsContainer.sBlockCasingsTT, 7))
        .addElement(
            'C',
            buildHatchAdder(MTEVoidbaseAssembler.class)
                .atLeast(Energy.or(HatchElement.EnergyMulti), Maintenance, InputBus, OutputBus)
                .casingIndex(Casings.HighPowerCasing.getTextureId())
                .hint(1)
                .buildAndChain(Casings.HighPowerCasing.asElement()))
        .build();

    /** The 15×15×3 shape: hatch ring on the front face, controller at the front-center, padded interior. */
    private static String[][] buildShape() {
        int half = SCAN_WIDTH / 2;
        String back = "AAAAAAAAAAAAAAA";
        String ring = "CCCCCCCCCCCCCCC";
        String pad = "CBBBBBBBBBBBBBC";
        String center = "CBBBBBB~BBBBBBC";
        String[][] shape = new String[SCAN_WIDTH][];
        for (int x = 0; x < SCAN_WIDTH; x++) {
            String front;
            if (x == half) {
                front = center;
            } else if (x == 0 || x == SCAN_WIDTH - 1) {
                front = ring;
            } else {
                front = pad;
            }
            shape[x] = new String[] { front, back, back };
        }
        return shape;
    }

    // Digitized-but-not-yet-output base (persisted across chunk reloads)
    private @Nullable VoidcraftBlueprint pendingBase;
    private long pendingCreatedAt;

    /**
     * The controller's stored program captured from the scanned build volume — written into the digitized
     * blueprint item's NBT ({@link VoidcraftNbt#TAG_PROGRAM}) at output. Null = the controller had no program.
     */
    private @Nullable NBTTagList pendingProgram;

    /**
     * The multiblock component controllers the last scan found in the volume — the scan audit forces each one's own
     * structure check, then applies the component's stats only to the formed structures.
     */
    private final List<MultiblockControllerRef> multiblockControllers = new ArrayList<>();

    public MTEVoidbaseAssembler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEVoidbaseAssembler(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEVoidbaseAssembler(mName);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 7, 7, 0, errors)) {
            return;
        }
        checkHasAnyEnergy(errors);
        checkHasMaintenanceHatch(errors);
        checkHasOutputBus(errors);
    }

    /**
     * Highest component tier allowed by the integrated circuits in the input buses.
     * No circuit → tier 0 (base components only).
     */
    private int readCircuitTier() {
        int tier = 0;
        for (MTEHatchInputBus inputBus : mInputBusses) {
            for (int i = 0; i < inputBus.getSizeInventory(); i++) {
                ItemStack stack = inputBus.getStackInSlot(i);
                if (GTUtility.isAnyIntegratedCircuit(stack)) {
                    tier = Math.max(tier, VoidcraftComponentRegistry.maxTierForCircuit(stack.getItemDamage()));
                }
            }
        }
        return tier;
    }

    /**
     * World position of the scan cell at (i, j, depth): i/j in [-7, 7] across the front face, depth in [1, 15]
     * along the facing direction.
     */
    private Vec3Impl scanCell(int i, int j, int depth) {
        Vec3Impl base = getPos();
        ForgeDirection front = getBaseMetaTileEntity().getFrontFacing();
        // Two perpendicular axes spanning the front face
        Vec3Impl a1, a2;
        if (front.offsetY != 0) {
            // facing up/down: the build plane is horizontal (X-Z)
            a1 = new Vec3Impl(1, 0, 0);
            a2 = new Vec3Impl(0, 0, 1);
        } else if (front.offsetX != 0) {
            // facing east/west: the build plane is vertical (Y-Z)
            a1 = new Vec3Impl(0, 1, 0);
            a2 = new Vec3Impl(0, 0, 1);
        } else {
            // facing north/south: the build plane is vertical (X-Y)
            a1 = new Vec3Impl(1, 0, 0);
            a2 = new Vec3Impl(0, 1, 0);
        }
        return base.offset(front, depth)
            .add(a1.get0() * i, a1.get1() * i, a1.get2() * i)
            .add(a2.get0() * j, a2.get1() * j, a2.get2() * j);
    }

    /**
     * Scan the 15×15×15 volume in front of the machine's front face.
     *
     * <p>
     * Captures each component block (and its facing) plus every Voidcraft cover mounted on any of its six faces —
     * covers are part of the station and contribute their stats. Multiblock component blocks (their own GT
     * multiblocks) are captured too; each multiblock controller found in the volume is collected for the scan
     * audit, which applies the component's stats only when the component's own structure is formed.
     *
     * @return the scanned blueprint, or null if the volume contains something that is neither air nor a Voidcraft
     *         component block
     */
    @Nullable
    private VoidcraftBlueprint scanRegion() {
        World world = getBaseMetaTileEntity().getWorld();
        int half = SCAN_WIDTH / 2;
        byte[] grid = new byte[SCAN_WIDTH * SCAN_HEIGHT * SCAN_DEPTH];
        byte[] facingGrid = new byte[SCAN_WIDTH * SCAN_HEIGHT * SCAN_DEPTH];
        byte[] coverGrid = new byte[SCAN_WIDTH * SCAN_HEIGHT * SCAN_DEPTH * 6];
        ForgeDirection front = getBaseMetaTileEntity().getFrontFacing();
        NBTTagList program = null;
        multiblockControllers.clear();
        for (int depth = 1; depth <= SCAN_DEPTH; depth++) {
            for (int j = -half; j <= half; j++) {
                for (int i = -half; i <= half; i++) {
                    Vec3Impl cell = scanCell(i, j, depth);
                    int x = cell.get0(), y = cell.get1(), z = cell.get2();
                    if (y < 0 || y >= world.getHeight()) {
                        continue; // out of world counts as empty
                    }
                    Block block = world.getBlock(x, y, z);
                    if (block == null || block == Blocks.air) {
                        continue;
                    }
                    IMetaTileEntity mte = GTUtility.getMetaTileEntity(world.getTileEntity(x, y, z));
                    MTEVoidcraftComponent hull = null;
                    if (mte instanceof MTEVoidcraftComponent component) {
                        hull = component;
                    }
                    VoidcraftComponent scanned = hull != null ? hull.getComponent()
                        : VoidcraftMultiblockRegistry.componentOf(mte);
                    if (scanned == null) {
                        return null; // foreign block in the volume
                    }
                    int idx = i + half + SCAN_WIDTH * (j + half + SCAN_HEIGHT * (depth - 1));
                    grid[idx] = (byte) scanned.toGridValue();
                    facingGrid[idx] = (byte) (mte.getBaseMetaTileEntity()
                        .getFrontFacing()
                        .ordinal() + 1);
                    if (hull != null) {
                        if (hull.getComponent() == VoidcraftComponent.CONTROLLER) {
                            NBTTagList p = hull.getProgramTag();
                            if (p != null) {
                                NBTBase copy = p.copy();
                                program = (copy instanceof NBTTagList) ? (NBTTagList) copy : null;
                            }
                        }

                        if (hull.getBaseMetaTileEntity() instanceof ICoverable coverable) {
                            for (int worldSide = 0; worldSide < 6; worldSide++) {
                                Cover cover = coverable.getCoverAtSide(ForgeDirection.getOrientation(worldSide));
                                if (cover instanceof CoverVoidcraftComponent vc && vc.getComponent() != null) {
                                    // store the cover's side in GRID space (the blueprint's depth axis is the
                                    // assembler's front, not a world axis), so the same world direction maps to the
                                    // same grid side no matter which way the assembler itself faces.
                                    coverGrid[idx * 6 + VoidcraftBlueprint
                                        .toGridSide(front.offsetX, front.offsetY, front.offsetZ, worldSide)] = (byte) vc
                                            .getComponent()
                                            .toGridValue();
                                }
                            }
                        }
                    } else {
                        // A multiblock component controller — its stats only count when its own structure is
                        // formed (the audit in checkProcessing_EM forces the structure check).
                        multiblockControllers
                            .add(new MultiblockControllerRef(scanned, mte, mte.getBaseMetaTileEntity()));
                    }
                }
            }
        }
        try {
            VoidcraftBlueprint blueprint = VoidcraftBlueprint
                .ofBase(SCAN_WIDTH, SCAN_HEIGHT, SCAN_DEPTH, grid, facingGrid, coverGrid);
            if (blueprint != null) {
                pendingProgram = (program != null && USSProgram.readFromNBT(program) != null) ? program : null;
            }
            return blueprint;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Clear the component blocks of a digitized base from the world (only cells that still hold a Voidcraft
     * component MTE — classic full blocks or multiblock component blocks — so foreign blocks placed meanwhile are
     * never destroyed).
     */
    private void clearShipBlocks() {
        World world = getBaseMetaTileEntity().getWorld();
        int half = SCAN_WIDTH / 2;
        for (int depth = 1; depth <= SCAN_DEPTH; depth++) {
            for (int j = -half; j <= half; j++) {
                for (int i = -half; i <= half; i++) {
                    Vec3Impl cell = scanCell(i, j, depth);
                    int x = cell.get0(), y = cell.get1(), z = cell.get2();
                    if (y < 0 || y >= world.getHeight()) {
                        continue;
                    }
                    IMetaTileEntity mte = GTUtility.getMetaTileEntity(world.getTileEntity(x, y, z));
                    if (mte instanceof MTEVoidcraftComponent || VoidcraftMultiblockRegistry.componentOf(mte) != null) {
                        world.setBlock(x, y, z, Blocks.air);
                    }
                }
            }
        }
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        int maxTier = readCircuitTier();
        VoidcraftBlueprint scanned = scanRegion();
        if (scanned == null) {
            pendingProgram = null;
            return SimpleCheckRecipeResult.ofFailure("voidbase_scan_failed");
        }
        // The multiblock components found in the volume: force each controller's own structure check, then audit
        // (unformed structure / structure reaching beyond the volume → the scan fails with that error key).
        List<String> multiblockErrors = VoidcraftMultiblockRegistry.auditScan(scanned, multiblockControllers);
        if (!multiblockErrors.isEmpty()) {
            pendingProgram = null;
            return SimpleCheckRecipeResult.ofFailure(multiblockErrors.get(0));
        }
        List<String> errors = new ArrayList<>();
        if (!scanned.validateForBase(maxTier, errors)) {
            pendingProgram = null;
            return SimpleCheckRecipeResult.ofFailure(errors.isEmpty() ? "voidbase_invalid" : errors.get(0));
        }

        int cells = scanned.componentCount();
        long totalEU = (long) cells * VoidcraftConstants.DIGITIZE_EU_PER_CELL;
        int ticks = Math.max(VoidcraftConstants.DIGITIZE_MIN_TICKS, cells * VoidcraftConstants.DIGITIZE_TICKS_PER_CELL);
        long euPerTick = Math.max(1L, totalEU / ticks);

        pendingBase = scanned.trim();
        pendingCreatedAt = System.currentTimeMillis();

        mEUt = (int) -euPerTick;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = ticks;
        return SimpleCheckRecipeResult.ofSuccess("voidbase_digitizing");
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (pendingBase == null) {
            afterRecipeCheckFailed();
            return;
        }
        // The controller's program rides the item NBT (vc_program) into the base payload at launch.
        ItemStack result = ItemVoidbaseBlueprint
            .fromBlueprint(pendingBase, "Voidbase", ItemVoidbaseBlueprint.newUuid(), pendingCreatedAt, pendingProgram);
        if (addOutputAtomic(result)) {
            clearShipBlocks();
        } else {
            afterRecipeCheckFailed();
        }
        pendingBase = null;
        pendingCreatedAt = 0;
        pendingProgram = null;
    }

    // ---- client visuals (scan wireframe / scanning planes / preview hologram — see RenderVoidcraftAssembler)

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            AssemblerVisuals.publish(
                this,
                aBaseMetaTileEntity.getWorld().provider.dimensionId,
                aBaseMetaTileEntity.getXCoord(),
                aBaseMetaTileEntity.getYCoord(),
                aBaseMetaTileEntity.getZCoord(),
                aBaseMetaTileEntity.getFrontFacing()
                    .ordinal(),
                SCAN_WIDTH,
                SCAN_HEIGHT,
                SCAN_DEPTH,
                true,
                mMachine,
                mMaxProgresstime > 0 && mProgresstime < mMaxProgresstime);
        }
    }

    @Override
    public void onRemoval() {
        super.onRemoval();
        AssemblerVisuals.unpublish(this);
    }

    // region NBT persistence

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (pendingBase != null) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("w", pendingBase.width);
            tag.setInteger("h", pendingBase.height);
            tag.setInteger("d", pendingBase.depth);
            tag.setByteArray("grid", pendingBase.copyGrid());
            tag.setByteArray("facing", pendingBase.copyFacingGrid());
            tag.setByteArray("covers", pendingBase.copyCoverGrid());
            tag.setLong("created", pendingCreatedAt);
            if (pendingProgram != null) {
                tag.setTag(VoidcraftNbt.TAG_PROGRAM, pendingProgram);
            }
            aNBT.setTag("voidbase_pending", tag);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        pendingBase = null;
        pendingProgram = null;
        pendingCreatedAt = 0;
        NBTTagCompound tag = aNBT.getCompoundTag("voidbase_pending");
        if (tag.hasKey("grid")) {
            try {
                byte[] facing = tag.hasKey("facing") ? tag.getByteArray("facing") : null;
                byte[] covers = tag.hasKey("covers") ? tag.getByteArray("covers") : null;
                pendingBase = VoidcraftBlueprint.ofBase(
                    tag.getInteger("w"),
                    tag.getInteger("h"),
                    tag.getInteger("d"),
                    tag.getByteArray("grid"),
                    facing,
                    covers);
                pendingCreatedAt = tag.getLong("created");
                if (tag.hasKey(VoidcraftNbt.TAG_PROGRAM)) {
                    NBTBase p = tag.getTag(VoidcraftNbt.TAG_PROGRAM);
                    if (p instanceof NBTTagList && USSProgram.readFromNBT((NBTTagList) p) != null) {
                        pendingProgram = (NBTTagList) p;
                    }
                }
            } catch (IllegalArgumentException ignored) {
                pendingBase = null;
                pendingProgram = null;
            }
        }
    }

    // endregion

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(translateToLocal("gt.mbtt.machine_type.digitizer"))
            .addMarkdown(new ResourceLocation("gregtech", "voidbase-assembler"))
            .addSupportAny()
            .beginStructureBlock(15, 15, 3, false)
            .addController(translateToLocal("tt.keyword.Structure.FrontCenter3rd"))
            .addCasing("8", translateToLocal("gt.blockcasingsTT.7.name"), false)
            .addCasing("320", translateToLocal("gt.blockcasingsTT.4.name"), false)
            .addEnergyHatch("1+", translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1)
            .addMaintenanceHatch("1", translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1)
            .addInputBus("1+", translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1)
            .addOutputBus("1+", translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1)
            .toolTipFinisher();
        // spotless:on
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 7, 7, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) {
            return -1;
        }
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 7, 7, 0, elementBudget, source, actor, false, true);
    }

    @Override
    public IStructureDefinition<MTEVoidbaseAssembler> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        // Like the Assembler/Gateway/Bay: no meaningful energy flow in the vertical slice → the maintenance
        // (damage/repair) system does not apply and a maintenance hatch would have nothing to service.
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
