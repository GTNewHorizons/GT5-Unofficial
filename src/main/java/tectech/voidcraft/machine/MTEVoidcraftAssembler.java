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
import net.minecraft.nbt.NBTTagCompound;
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
import tectech.voidcraft.item.ItemVoidcraft;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftComponentRegistry;
import tectech.voidcraft.ship.VoidcraftConstants;

/**
 * Voidcraft Assembler (EoH rework, Phase 1).
 *
 * <p>
 * A 5×5×3 multiblock whose front face (5×5) looks out over a 5×5×10 build volume. It scans that volume, validates
 * the placed components — full blocks plus any covers mounted on their faces (exactly one controller, at least one
 * engine, component/cover tier ≤ the assembler circuit tier, net thrust not fully cancelled) — then digitizes the
 * ship into a single non-stackable {@code ItemVoidcraft} and clears the component blocks.
 *
 * <p>
 * This machine is a complete parallel to the legacy Eye of Harmony: it shares no code paths with the EoH MTE and
 * never touches the legacy classes.
 */
@IMetaTileEntity.SkipGenerateDescription
public class MTEVoidcraftAssembler extends TTMultiblockBase implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    /** Scan volume: 5 wide × 5 tall × 10 deep in front of the machine's front face. */
    private static final int SCAN_WIDTH = 5;
    private static final int SCAN_HEIGHT = 5;
    private static final int SCAN_DEPTH = 10;

    /**
     * 5 wide × 5 tall × 3 deep. Controller at the front-center (x=2, y=2, z=0). Hatch slots (C) ring the front face,
     * 'A' (TT casing meta 4) fills the two back planes, 'B' (TT casing meta 7) pads the front ring.
     */
    private static final IStructureDefinition<MTEVoidcraftAssembler> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEVoidcraftAssembler>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "CCCCC", "AAAAA", "AAAAA" }, { "CBBBC", "AAAAA", "AAAAA" },
                    { "CB~BC", "AAAAA", "AAAAA" }, { "CBBBC", "AAAAA", "AAAAA" }, { "CCCCC", "AAAAA", "AAAAA" } }))
        .addElement('A', ofBlock(TTCasingsContainer.sBlockCasingsTT, 4))
        .addElement('B', ofBlock(TTCasingsContainer.sBlockCasingsTT, 7))
        .addElement(
            'C',
            buildHatchAdder(MTEVoidcraftAssembler.class)
                .atLeast(Energy.or(HatchElement.EnergyMulti), Maintenance, InputBus, OutputBus)
                .casingIndex(Casings.HighPowerCasing.getTextureId())
                .hint(1)
                .buildAndChain(Casings.HighPowerCasing.asElement()))
        .build();

    // Digitized-but-not-yet-output ship (persisted across chunk reloads)
    private @Nullable VoidcraftBlueprint pendingShip;
    private long pendingCreatedAt;

    public MTEVoidcraftAssembler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEVoidcraftAssembler(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEVoidcraftAssembler(mName);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 2, 0, errors)) {
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
     * World position of the scan cell at (i, j, depth): i/j in [-2, 2] across the front face, depth in [1, 10]
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
     * Scan the 5×5×10 volume in front of the machine's front face.
     *
     * <p>
     * Captures each component block (and its facing) plus every Voidcraft cover mounted on any of its six faces —
     * covers are part of the ship and contribute their stats.
     *
     * @return the scanned blueprint, or null if the volume contains something that is neither air nor a Voidcraft
     *         component block (or the machine is not built against a clear front)
     */
    @Nullable
    private VoidcraftBlueprint scanRegion() {
        World world = getBaseMetaTileEntity().getWorld();
        int cells = SCAN_WIDTH * SCAN_HEIGHT * SCAN_DEPTH;
        byte[] grid = new byte[cells];
        byte[] facingGrid = new byte[cells];
        byte[] coverGrid = new byte[cells * 6];
        for (int depth = 1; depth <= SCAN_DEPTH; depth++) {
            for (int j = -SCAN_HEIGHT / 2; j <= SCAN_HEIGHT / 2; j++) {
                for (int i = -SCAN_WIDTH / 2; i <= SCAN_WIDTH / 2; i++) {
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
                    if (!(mte instanceof MTEVoidcraftComponent hull)) {
                        return null; // foreign block in the volume
                    }
                    int idx = i + 2 + SCAN_WIDTH * (j + 2 + SCAN_HEIGHT * (depth - 1));
                    grid[idx] = (byte) hull.getComponent()
                        .toGridValue();
                    facingGrid[idx] = (byte) (hull.getBaseMetaTileEntity()
                        .getFrontFacing()
                        .ordinal() + 1);

                    if (hull.getBaseMetaTileEntity() instanceof ICoverable coverable) {
                        for (int side = 0; side < 6; side++) {
                            Cover cover = coverable.getCoverAtSide(ForgeDirection.getOrientation(side));
                            if (cover instanceof CoverVoidcraftComponent vc && vc.getComponent() != null) {
                                coverGrid[idx * 6 + side] = (byte) vc.getComponent()
                                    .toGridValue();
                            }
                        }
                    }
                }
            }
        }
        try {
            return VoidcraftBlueprint.of(SCAN_WIDTH, SCAN_HEIGHT, SCAN_DEPTH, grid, facingGrid, coverGrid);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /**
     * Clear the component blocks of a digitized ship from the world (only cells that still hold a Voidcraft
     * component MTE, so foreign blocks placed meanwhile are never destroyed).
     */
    private void clearShipBlocks() {
        World world = getBaseMetaTileEntity().getWorld();
        for (int depth = 1; depth <= SCAN_DEPTH; depth++) {
            for (int j = -SCAN_HEIGHT / 2; j <= SCAN_HEIGHT / 2; j++) {
                for (int i = -SCAN_WIDTH / 2; i <= SCAN_WIDTH / 2; i++) {
                    Vec3Impl cell = scanCell(i, j, depth);
                    int x = cell.get0(), y = cell.get1(), z = cell.get2();
                    if (y < 0 || y >= world.getHeight()) {
                        continue;
                    }
                    IMetaTileEntity mte = GTUtility.getMetaTileEntity(world.getTileEntity(x, y, z));
                    if (mte instanceof MTEVoidcraftComponent) {
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
            return SimpleCheckRecipeResult.ofFailure("voidcraft_scan_failed");
        }
        List<String> errors = new ArrayList<>();
        if (!scanned.validate(maxTier, errors)) {
            return SimpleCheckRecipeResult.ofFailure(errors.isEmpty() ? "voidcraft_invalid" : errors.get(0));
        }

        int cells = scanned.componentCount();
        long totalEU = (long) cells * VoidcraftConstants.DIGITIZE_EU_PER_CELL;
        int ticks = Math.max(VoidcraftConstants.DIGITIZE_MIN_TICKS, cells * VoidcraftConstants.DIGITIZE_TICKS_PER_CELL);
        long euPerTick = Math.max(1L, totalEU / ticks);

        pendingShip = scanned.trim();
        pendingCreatedAt = System.currentTimeMillis();

        mEUt = (int) -euPerTick;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = ticks;
        return SimpleCheckRecipeResult.ofSuccess("voidcraft_digitizing");
    }

    @Override
    public void outputAfterRecipe_EM() {
        if (pendingShip == null) {
            afterRecipeCheckFailed();
            return;
        }
        ItemStack result = ItemVoidcraft
            .fromBlueprint(pendingShip, "Voidcraft", ItemVoidcraft.newUuid(), pendingCreatedAt);
        if (addOutputAtomic(result)) {
            clearShipBlocks();
        } else {
            afterRecipeCheckFailed();
        }
        pendingShip = null;
        pendingCreatedAt = 0;
    }

    // region NBT persistence

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (pendingShip != null) {
            NBTTagCompound tag = new NBTTagCompound();
            tag.setInteger("w", pendingShip.width);
            tag.setInteger("h", pendingShip.height);
            tag.setInteger("d", pendingShip.depth);
            tag.setByteArray("grid", pendingShip.copyGrid());
            tag.setByteArray("facing", pendingShip.copyFacingGrid());
            tag.setByteArray("covers", pendingShip.copyCoverGrid());
            tag.setLong("created", pendingCreatedAt);
            aNBT.setTag("voidcraft_pending", tag);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        pendingShip = null;
        pendingCreatedAt = 0;
        NBTTagCompound tag = aNBT.getCompoundTag("voidcraft_pending");
        if (tag.hasKey("grid")) {
            try {
                byte[] facing = tag.hasKey("facing") ? tag.getByteArray("facing") : null;
                byte[] covers = tag.hasKey("covers") ? tag.getByteArray("covers") : null;
                pendingShip = VoidcraftBlueprint.of(
                    tag.getInteger("w"),
                    tag.getInteger("h"),
                    tag.getInteger("d"),
                    tag.getByteArray("grid"),
                    facing,
                    covers);
                pendingCreatedAt = tag.getLong("created");
            } catch (IllegalArgumentException ignored) {
                pendingShip = null;
            }
        }
    }

    // endregion

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        // spotless:off
        tt.addMachineType(translateToLocal("gt.mbtt.machine_type.digitizer"))
            .addMarkdown(new ResourceLocation("gregtech", "voidcraft-assembler"))
            .addSupportAny()
            .beginStructureBlock(5, 5, 3, false)
            .addController(translateToLocal("tt.keyword.Structure.FrontCenter3rd"))
            .addCasing("8", translateToLocal("gt.blockcasingsTT.7.name"), false)
            .addCasing("45", translateToLocal("gt.blockcasingsTT.4.name"), false)
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
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) {
            return -1;
        }
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 2, 0, elementBudget, source, actor, false, true);
    }

    @Override
    public IStructureDefinition<MTEVoidcraftAssembler> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        // Like the Gateway/Bay: no meaningful energy flow in the vertical slice → the maintenance (damage/repair)
        // system does not apply and a maintenance hatch would have nothing to service. Existing issues are
        // auto-fixed on load.
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
