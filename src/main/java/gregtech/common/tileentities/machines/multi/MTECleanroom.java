package gregtech.common.tileentities.machines.multi;

import static gregtech.api.enums.GTValues.debugCleanroom;
import static gregtech.api.enums.Textures.BlockIcons.BLOCK_PLASCRETE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM_GLOW;
import static gregtech.api.util.GlassTier.getGlassBlockTier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.capability.Capabilities;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ICleanroom;
import gregtech.api.interfaces.ICleanroomReceiver;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicHull;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTETooltipMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLog;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.config.MachineStats;
import gregtech.common.gui.modularui.multiblock.MTECleanRoomGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class MTECleanroom extends MTETooltipMultiBlockBase
    implements IConstructable, ISecondaryDescribable, ICleanroom {

    /**
     * Maximum width (horizontal size) of the cleanroom. Includes walls.
     */
    public static final int MAX_WIDTH = 15;

    /**
     * Maximum height of the cleanroom. Includes floor and ceiling.
     */
    public static final int MAX_HEIGHT = 15;

    /**
     * List of other blocks allowed in the cleanroom. Format of entries is either just the block's unlocalized name, or
     * <unlocalized name>:<meta>. The former matches all blocks of that name regardless of meta value. Read from config
     * file.
     */
    public static final HashSet<String> ALLOWED_BLOCKS = new HashSet<>();

    // Plascrete blocks.
    protected static Block CASING_BLOCK;
    protected static final int CASING_META = 2;
    // To color hatches.
    protected static final int CASING_INDEX = 210;
    // Filter casings.
    protected static Block FILTER_BLOCK;
    protected static final int FILTER_META = 11;
    // Minimum valid tier of glass. All glasses of at least this tier are always allowed;
    // lower tier glasses can be added separately in ALLOWED_BLOCKS.
    protected static final int MIN_GLASS_TIER = 4; // EV

    private final Set<ICleanroomReceiver> cleanroomReceivers = new HashSet<>();
    private int mHeight = -1;

    public MTECleanroom(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTECleanroom(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECleanroom(mName);
    }

    @Override
    public int getCleanness() {
        return mEfficiency;
    }

    @Override
    public boolean isValidCleanroom() {
        return isValid() && mMachine;
    }

    @Override
    public void pollute() {
        mEfficiency = 0;
        mWrench = false;
        mScrewdriver = false;
        mSoftMallet = false;
        mHardHammer = false;
        mSolderingTool = false;
        mCrowbar = false;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("machtype.cleanroom")
            .addInfo("gt.cleanroom.tips")
            .beginVariableStructureBlock(3, MAX_WIDTH, 4, MAX_HEIGHT, 3, MAX_WIDTH, true)
            .addController("top_center")
            .addStructureInfo("gt.cleanroom.info.controller")
            .addStructurePart(
                ItemList.Casing_Vent.get(1)
                    .getDisplayName(),
                "gt.cleanroom.info.filter")
            .addStructureInfo(
                "gt.cleanroom.info.plascrete",
                ItemList.Block_Plascrete.get(1)
                    .getDisplayName(),
                MachineStats.cleanroom.minCasingCount)
            .addEnergyHatch("gt.cleanroom.info.energy")
            .addMaintenanceHatch("gt.cleanroom.info.energy")
            .addStructureInfo("")
            .addStructureInfo("gt.cleanroom.info.replacements", MachineStats.cleanroom.maxReplacementPercentage)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return new String[] { "The base can be rectangular." };
    }

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        mEfficiencyIncrease = 100;
        final long inputVoltage = getMaxInputVoltage();

        // only allow LV+ energy hatches
        if (inputVoltage < TierEU.LV) {
            return CheckRecipeResultRegistry.insufficientPower(40);
        }

        // use the standard overclock mechanism to determine duration and estimate a maximum consumption
        // if the cleanroom is powered by an LV energy hatch, it will actually accept 2A instead of just 1A.
        int amperage = inputVoltage == TierEU.LV ? 2 : 1;
        OverclockCalculator calculator = new OverclockCalculator().setRecipeEUt(40)
            .setEUt(inputVoltage * amperage)
            .setDuration(45 * Math.max(1, mHeight - 1))
            .calculate();
        mEUt = (int) calculator.getConsumption();
        mMaxProgresstime = calculator.getDuration();
        // negate it to trigger the special energy consumption function. divide by 10 to get the actual final
        // consumption.
        mEUt /= -10;
        return SimpleCheckRecipeResult.ofSuccess("cleanroom_running");
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return (facing.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0;
    }

    /*
     * Structure check
     */

    // Extent in all directions. Specifically the offset from the controller to each wall.
    // Min values will always be negative, Max values positive.
    protected int dxMin = 0, dxMax = 0, dzMin = 0, dzMax = 0, dyMin = 0;
    // Total number of plascrete blocks in the structure.
    protected int casingCount;
    // Total number of other blocks in the structure. Does NOT count filter casings or the controller.
    protected int otherCount;
    // Whether the cleanroom contains a door that is "open", efficiency is constantly reduced.
    protected boolean isDoorOpen;

    private enum CleanroomBlockType {
        CASING, // Plascrete block.
        FILTER, // Filter casing.
        GLASS, // Any EV+ tiered glass.
        OTHER, // Another allowed replacement block.
        DOOR, // Reinforced door (IC2).
        HATCH_ENERGY, // Energy hatch.
        HATCH_MAINTENANCE, // Maintenance hatch.
        HATCH_DIODE, // Diode or machine hull.
        INVALID // Invalid block.
    }

    // Specify which blocks are allowed where. This skips checks for other blocks.
    private static final int MASK_CASING = 1;
    private static final int MASK_FILTER = 1 << 1;
    private static final int MASK_GLASS = 1 << 2;
    private static final int MASK_OTHER = 1 << 3;
    private static final int MASK_DOOR = 1 << 4;
    private static final int MASK_HATCH = 1 << 5;

    // Ceiling blocks NOT including edges.
    private static final int MASK_CEILING_INTERNAL = MASK_FILTER;
    // Edges of the ceiling layer. Includes corners of the top layer.
    private static final int MASK_CEILING_EDGE = MASK_CASING | MASK_GLASS | MASK_OTHER | MASK_HATCH;
    // Blocks in the wall, not including vertical edges.
    private static final int MASK_WALL_INTERNAL = MASK_CASING | MASK_GLASS | MASK_OTHER | MASK_DOOR | MASK_HATCH;
    // Vertical edges of walls, not including any corners.
    private static final int MASK_WALL_EDGE = MASK_CASING | MASK_GLASS | MASK_OTHER | MASK_HATCH;
    // Floor, not including edges or corners.
    private static final int MASK_FLOOR_INTERNAL = MASK_CASING | MASK_GLASS | MASK_OTHER | MASK_HATCH;
    // Bottom horizontal edges and corners.
    private static final int MASK_FLOOR_EDGE = MASK_CASING | MASK_GLASS | MASK_OTHER | MASK_HATCH;

    /**
     * Determines the type of the block at a specified offset from the controller. Only types specified by allowedMask
     * are checked, for efficiency. If a block is not one of the allowed types, CleanroomBlockType.INVALID is returned.
     */
    private CleanroomBlockType getBlockType(IGregTechTileEntity aBaseMetaTileEntity, int dx, int dy, int dz,
        int allowedMask) {
        Block block = aBaseMetaTileEntity.getBlockOffset(dx, dy, dz);
        int meta = aBaseMetaTileEntity.getMetaIDOffset(dx, dy, dz);

        if ((allowedMask & MASK_CASING) != 0 && block == CASING_BLOCK && meta == CASING_META)
            return CleanroomBlockType.CASING;

        if ((allowedMask & MASK_FILTER) != 0 && block == FILTER_BLOCK && meta == FILTER_META)
            return CleanroomBlockType.FILTER;

        if ((allowedMask & MASK_GLASS) != 0) {
            Integer glassTier = getGlassBlockTier(block, meta);
            if (glassTier != null && glassTier >= MIN_GLASS_TIER) {
                return CleanroomBlockType.GLASS;
            }
        }

        if ((allowedMask & MASK_OTHER) != 0 && (ALLOWED_BLOCKS.contains(block.getUnlocalizedName())
            || ALLOWED_BLOCKS.contains(block.getUnlocalizedName() + ":" + meta))) return CleanroomBlockType.OTHER;

        if ((allowedMask & MASK_DOOR) != 0
            // This allows doors on the edges, although their open/closed status will not be calculated correctly.
            // The intent is that the wall check calling this method will not allow doors on edges.
            && block instanceof ic2.core.block.BlockIC2Door) {

            if (!isDoorOpen) { // No need to check again if there is already an open door somewhere else.
                int doorOrientation = getDoorOrientation(aBaseMetaTileEntity, dx, dy, dz);
                if (doorOrientation < 0) {
                    // Somehow an invalid door block.
                    if (debugCleanroom)
                        GTLog.out.println("Cleanroom: Invalid block at offset (" + dx + ", " + dy + ", " + dz + ").");
                    return CleanroomBlockType.INVALID;
                }
                if (doorOrientation % 2 == 0) {
                    // Door on the W or E side (aligned with Z axis).
                    if (dx != dxMin && dx != dxMax)
                        // Door is in the N or S wall, definitely open.
                        isDoorOpen = true;
                    // Otherwise check adjacent blocks for other doors.
                    else if (dz > dzMin
                        && aBaseMetaTileEntity.getBlockOffset(dx, dy, dz - 1) instanceof ic2.core.block.BlockIC2Door
                        && doorOrientation != getDoorOrientation(aBaseMetaTileEntity, dx, dy, dz - 1))
                        isDoorOpen = true;
                    else if (dz < dzMax
                        && aBaseMetaTileEntity.getBlockOffset(dx, dy, dz + 1) instanceof ic2.core.block.BlockIC2Door
                        && doorOrientation != getDoorOrientation(aBaseMetaTileEntity, dx, dy, dz + 1))
                        isDoorOpen = true;
                } else {
                    // Door on the N or S side (aligned with X axis).
                    if (dz != dzMin && dz != dzMax)
                        // Door is in the N or S wall, definitely open.
                        isDoorOpen = true;
                    // Check adjacent blocks for other doors.
                    else if (dx > dxMin
                        && aBaseMetaTileEntity.getBlockOffset(dx - 1, dy, dz) instanceof ic2.core.block.BlockIC2Door
                        && doorOrientation != getDoorOrientation(aBaseMetaTileEntity, dx - 1, dy, dz))
                        isDoorOpen = true;
                    else if (dx < dxMax
                        && aBaseMetaTileEntity.getBlockOffset(dx + 1, dy, dz) instanceof ic2.core.block.BlockIC2Door
                        && doorOrientation != getDoorOrientation(aBaseMetaTileEntity, dx + 1, dy, dz))
                        isDoorOpen = true;
                }

                if (debugCleanroom && isDoorOpen) {
                    GTLog.out.println("Cleanroom: Open door at offset (" + dx + ", " + dy + ", " + dz + ").");
                }
            }
            return CleanroomBlockType.DOOR;
        }

        if ((allowedMask & MASK_HATCH) != 0) {
            IGregTechTileEntity te = aBaseMetaTileEntity.getIGregTechTileEntityOffset(dx, dy, dz);
            if (te != null) {
                IMetaTileEntity mte = te.getMetaTileEntity();
                if (mte instanceof MTEHatchMaintenance) return CleanroomBlockType.HATCH_MAINTENANCE;
                else if (mte instanceof MTEHatchEnergy) return CleanroomBlockType.HATCH_ENERGY;
                // Both hulls and diodes are instanceof MTEBasicHull.
                else if (mte instanceof MTEBasicHull) return CleanroomBlockType.HATCH_DIODE;
                else return CleanroomBlockType.INVALID;
            }
        }

        return CleanroomBlockType.INVALID;
    }

    /**
     * Add a block to the cleanroom which is at the specified offset. This properly increases the count of
     * casings/non-casings, and if the block is a hatch, also adds it to the appropriate list.
     *
     * @param allowedMask specifies which types of blocks should be allowed at this position. Any other type of block is
     *                    considered invalid.
     * @return True on success (block was correctly added), false on failure (invalid block type).
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean addStructureBlock(IGregTechTileEntity aBaseMetaTileEntity, int dx, int dy, int dz,
        int allowedMask) {
        switch (getBlockType(aBaseMetaTileEntity, dx, dy, dz, allowedMask)) {
            case CASING:
                ++casingCount;
                return true;

            case FILTER:
                return true;

            case GLASS:
            case OTHER:
            case DOOR:
            case HATCH_DIODE:
                ++otherCount;
                return true;

            case HATCH_ENERGY:
                addEnergyInputToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(dx, dy, dz), CASING_INDEX);
                ++otherCount;
                return true;

            case HATCH_MAINTENANCE:
                addMaintenanceToMachineList(aBaseMetaTileEntity.getIGregTechTileEntityOffset(dx, dy, dz), CASING_INDEX);
                ++otherCount;
                return true;

            case INVALID:
                if (debugCleanroom)
                    GTLog.out.println("Cleanroom: Invalid block at offset (" + dx + ", " + dy + ", " + dz + ").");
                return false;

            default:
                throw new IllegalArgumentException(
                    "Cleanroom error: unknown block type at at offset (" + dx + ", " + dy + ", " + dz + ").");
        }
    }

    /**
     * Find the horizontal size of the cleanroom. Populates values dxMin, dxMax, dzMin, and dzMax.
     *
     * @return True on success, false on failure (which means an invalid structure).
     */
    protected boolean checkSize(IGregTechTileEntity aBaseMetaTileEntity) {
        // Footprint must be a rectangle. If the width is odd, the controller must be in the middle.
        // If the width is even, controller must be one of the two middle blocks.

        // X direction

        for (dxMin = -1; dxMin >= -MAX_WIDTH / 2; --dxMin) {
            if (getBlockType(aBaseMetaTileEntity, dxMin, 0, 0, MASK_CEILING_INTERNAL) == CleanroomBlockType.INVALID) {
                break;
            }
        }
        if (dxMin < -MAX_WIDTH / 2) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Too large (x-axis).");
            return false;
        }

        for (dxMax = 1; dxMax <= MAX_WIDTH / 2; ++dxMax) {
            if (getBlockType(aBaseMetaTileEntity, dxMax, 0, 0, MASK_CEILING_INTERNAL) == CleanroomBlockType.INVALID) {
                break;
            }
        }
        if (dxMax > MAX_WIDTH / 2) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Too large (x-axis).");
            return false;
        }

        if (Math.abs(dxMin + dxMax) > 1) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Controller not centered (x-axis).");
            return false;
        }

        // Z direction

        for (dzMin = -1; dzMin >= -MAX_WIDTH / 2; --dzMin) {
            if (getBlockType(aBaseMetaTileEntity, 0, 0, dzMin, MASK_CEILING_INTERNAL) == CleanroomBlockType.INVALID) {
                break;
            }
        }
        if (dzMin < -MAX_WIDTH / 2) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Too large (z-axis).");
            return false;
        }

        for (dzMax = 1; dzMax <= MAX_WIDTH / 2; ++dzMax) {
            if (getBlockType(aBaseMetaTileEntity, 0, 0, dzMax, MASK_CEILING_INTERNAL) == CleanroomBlockType.INVALID) {
                break;
            }
        }
        if (dzMax > MAX_WIDTH / 2) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Too large (z-axis).");
            return false;
        }

        if (Math.abs(dzMin + dzMax) > 1) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Controller not centered (z-axis).");
            return false;
        }

        if (debugCleanroom) GTLog.out.println(
            "Cleanroom: dxMin = " + dxMin + ", dxMax = " + dxMax + ", dzMin = " + dzMin + ", dzMax = " + dzMax + ".");
        return true;
    }

    /**
     * Checks whether the ceiling layer of the cleanroom is complete. Assumes that
     * {@link #checkSize(IGregTechTileEntity)} has already been run.
     *
     * @return True on success, false on failure.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean checkCeiling(IGregTechTileEntity aBaseMetaTileEntity) {
        // Edges must be plascrete, everything else must be filters (except for the controller).
        for (int dx = dxMin; dx <= dxMax; ++dx) {
            for (int dz = dzMin; dz <= dzMax; ++dz) {
                if (dx == 0 && dz == 0) {
                    // Controller.
                } else if (dx == dxMin || dx == dxMax || dz == dzMin || dz == dzMax) {
                    // Edge.
                    if (!addStructureBlock(aBaseMetaTileEntity, dx, 0, dz, MASK_CEILING_EDGE)) return false;
                } else {
                    // Internal block.
                    if (!addStructureBlock(aBaseMetaTileEntity, dx, 0, dz, MASK_CEILING_INTERNAL)) return false;
                }
            }
        }

        return true;
    }

    /**
     * Checks the floor of the cleanroom. Note that if this fails, it is not necessarily because the structure is
     * invalid, maybe the floor just isn't where we thought it was, and we're looking at a wall.
     *
     * @param dy Vertical offset of the floor from the controller.
     * @return True on success, false on failure.
     */
    protected boolean checkFloor(IGregTechTileEntity aBaseMetaTileEntity, int dy) {
        // Save maintenance and energy hatches, if the check fails, we don't want to add them.

        // We always add all hatches, even if we find more than one. This allows for better error reporting: if there
        // are two energy hatches in the floor layer, we add both, and report the floor as complete. This way, the
        // structure check fails due to multiple hatches, and not due to missing floor.
        int addedCasings = 0;
        int addedOther = 0;
        ArrayList<IGregTechTileEntity> energy = new ArrayList<>();
        ArrayList<IGregTechTileEntity> maintenance = new ArrayList<>();

        for (int dx = dxMin + 1; dx <= dxMax - 1; ++dx) {
            for (int dz = dzMin + 1; dz <= dzMax - 1; ++dz) {
                switch (getBlockType(aBaseMetaTileEntity, dx, dy, dz, MASK_FLOOR_INTERNAL)) {
                    case CASING:
                        ++addedCasings;
                        break;

                    case GLASS:
                    case OTHER:
                    case HATCH_DIODE:
                    case FILTER:
                    case DOOR: // Filters and doors should not be valid in the floor, but are included for completeness.
                        ++addedOther;
                        break;

                    case HATCH_ENERGY:
                        energy.add(aBaseMetaTileEntity.getIGregTechTileEntityOffset(dx, dy, dz));
                        ++addedOther;
                        break;

                    case HATCH_MAINTENANCE:
                        maintenance.add(aBaseMetaTileEntity.getIGregTechTileEntityOffset(dx, dy, dz));
                        ++addedOther;
                        break;

                    case INVALID:
                        // Do not log an error, we might not be at the correct floor level yet.
                        return false;

                    default:
                        throw new IllegalArgumentException(
                            "Cleanroom error: unknown block type at at offset (" + dx + ", " + dy + ", " + dz + ").");
                }
            }
        }

        // If we get here, the entire floor is valid. Add hatches to the machine.
        casingCount += addedCasings;
        otherCount += addedOther;
        for (var te : energy) addEnergyInputToMachineList(te, CASING_INDEX);
        for (var te : maintenance) addMaintenanceToMachineList(te, CASING_INDEX);
        return true;
    }

    /**
     * Checks the walls of the cleanroom at a specified vertical offset.
     *
     * @param dy Vertical offset of the floor from the controller.
     * @return True on success, false on failure.
     */
    protected boolean checkWall(IGregTechTileEntity aBaseMetaTileEntity, int dy) {
        for (int dx = dxMin + 1; dx <= dxMax - 1; ++dx) {
            if (!addStructureBlock(aBaseMetaTileEntity, dx, dy, dzMin, MASK_WALL_INTERNAL)) return false;
            if (!addStructureBlock(aBaseMetaTileEntity, dx, dy, dzMax, MASK_WALL_INTERNAL)) return false;
        }
        for (int dz = dzMin + 1; dz <= dzMax - 1; ++dz) {
            if (!addStructureBlock(aBaseMetaTileEntity, dxMin, dy, dz, MASK_WALL_INTERNAL)) return false;
            if (!addStructureBlock(aBaseMetaTileEntity, dxMax, dy, dz, MASK_WALL_INTERNAL)) return false;
        }

        if (!addStructureBlock(aBaseMetaTileEntity, dxMin, dy, dzMin, MASK_WALL_EDGE)) return false;
        if (!addStructureBlock(aBaseMetaTileEntity, dxMin, dy, dzMax, MASK_WALL_EDGE)) return false;
        if (!addStructureBlock(aBaseMetaTileEntity, dxMax, dy, dzMin, MASK_WALL_EDGE)) return false;
        if (!addStructureBlock(aBaseMetaTileEntity, dxMax, dy, dzMax, MASK_WALL_EDGE)) return false;

        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mUpdate = 100;
        cleanroomReceivers.forEach(r -> r.setCleanroom(null));
        cleanroomReceivers.clear();

        casingCount = 0;
        otherCount = 0;
        isDoorOpen = false;

        if (debugCleanroom) GTLog.out.println("Cleanroom: Starting structure check.");

        // Optimization: a vast majority of the time, the size of the CR won't change. Try checking it using the old
        // size, and only if that fails, try to find a new size.
        if (dyMin == 0 || !checkCeiling(aBaseMetaTileEntity)) {
            if (!checkSize(aBaseMetaTileEntity)) return false;
            if (!checkCeiling(aBaseMetaTileEntity)) return false;
        }

        // Check downward until we find a valid floor.
        // We check specifically internal blocks for a valid floor. This means that in most cases this check
        // immediately falls through, as the first block we check is already invalid (e.g., air or machine).
        for (dyMin = -1; dyMin >= -(MAX_HEIGHT - 1); --dyMin) {
            if (dyMin < -2 && checkFloor(aBaseMetaTileEntity, dyMin)) {
                // Found a valid floor. Add its edges and finish.
                for (int dx = dxMin; dx <= dxMax; ++dx) {
                    if (!addStructureBlock(aBaseMetaTileEntity, dx, dyMin, dzMin, MASK_FLOOR_EDGE)) return false;
                    if (!addStructureBlock(aBaseMetaTileEntity, dx, dyMin, dzMax, MASK_FLOOR_EDGE)) return false;
                }
                for (int dz = dzMin + 1; dz <= dzMax - 1; ++dz) {
                    if (!addStructureBlock(aBaseMetaTileEntity, dxMin, dyMin, dz, MASK_FLOOR_EDGE)) return false;
                    if (!addStructureBlock(aBaseMetaTileEntity, dxMax, dyMin, dz, MASK_FLOOR_EDGE)) return false;
                }
                break;
            } else {
                // Not floor yet, check for a wall.
                if (!checkWall(aBaseMetaTileEntity, dyMin)) return false;
            }
        }
        if (dyMin < -(MAX_HEIGHT - 1)) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Too tall.");
            return false;
        }
        mHeight = -dyMin + 1;

        if (debugCleanroom) GTLog.out.println(
            "Cleanroom: Structure complete. Found " + casingCount + " casings, " + otherCount + " other blocks.");

        // Validate structure.

        if (this.mMaintenanceHatches.size() != 1 || this.mEnergyHatches.size() != 1) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Incorrect number of hatches.");
            return false;
        }

        if (casingCount < MachineStats.cleanroom.minCasingCount) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Not enough plascrete blocks.");
            return false;
        }

        if ((otherCount * 100) / (casingCount + otherCount) > MachineStats.cleanroom.maxReplacementPercentage) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Too many non-plascrete blocks.");
            return false;
        }

        if (isDoorOpen) {
            this.mEfficiency = Math.max(0, this.mEfficiency - 200);
        }

        for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
            final byte t = (byte) Math.max(1, (byte) (15 / (10000f / this.mEfficiency)));
            aBaseMetaTileEntity.setInternalOutputRedstoneSignal(tSide, t);
        }

        // Re-add machines inside the cleanroom.

        for (int dy = dyMin + 1; dy < 0; ++dy) {
            for (int dx = dxMin + 1; dx <= dxMax - 1; ++dx) {
                for (int dz = dzMin + 1; dz <= dzMax - 1; dz++) {
                    TileEntity te = aBaseMetaTileEntity.getTileEntityOffset(dx, dy, dz);
                    ICleanroomReceiver receiver = Capabilities.getCapability(te, ICleanroomReceiver.class);
                    if (receiver != null) {
                        receiver.setCleanroom(this);
                        cleanroomReceivers.add(receiver);
                    }
                }
            }
        }

        if (debugCleanroom) GTLog.out.println("Cleanroom: Check successful.");

        return true;
    }

    @Override
    public boolean allowGeneralRedstoneOutput() {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if ((sideDirection.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) != 0) {
            return new ITexture[] { TextureFactory.of(BLOCK_PLASCRETE), active
                ? TextureFactory.of(
                    TextureFactory.of(OVERLAY_TOP_CLEANROOM_ACTIVE),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_TOP_CLEANROOM_ACTIVE_GLOW)
                        .glow()
                        .build())
                : TextureFactory.of(
                    TextureFactory.of(OVERLAY_TOP_CLEANROOM),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_TOP_CLEANROOM_GLOW)
                        .glow()
                        .build()) };
        }
        return new ITexture[] { TextureFactory.of(BLOCK_PLASCRETE) };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        int i = Math.min(stackSize.stackSize, 7);
        IGregTechTileEntity baseEntity = this.getBaseMetaTileEntity();
        World world = baseEntity.getWorld();
        int x = baseEntity.getXCoord();
        int y = baseEntity.getYCoord();
        int z = baseEntity.getZCoord();
        int yoff = Math.max(i * 2, 3);
        for (int X = x - i; X <= x + i; X++) for (int Y = y; Y >= y - yoff; Y--) for (int Z = z - i; Z <= z + i; Z++) {
            if (X == x && Y == y && Z == z) continue;
            if (X == x - i || X == x + i || Z == z - i || Z == z + i || Y == y - yoff) {
                if (hintsOnly) StructureLibAPI.hintParticle(world, X, Y, Z, CASING_BLOCK, CASING_META);
                else world.setBlock(X, Y, Z, CASING_BLOCK, CASING_META, 2);
            } else if (Y == y) {
                if (hintsOnly) StructureLibAPI.hintParticle(world, X, Y, Z, FILTER_BLOCK, FILTER_META);
                else world.setBlock(X, Y, Z, FILTER_BLOCK, FILTER_META, 2);
            }
        }
    }

    @Override
    public void onConfigLoad() {
        ALLOWED_BLOCKS.clear();
        Collections.addAll(ALLOWED_BLOCKS, MachineStats.cleanroom.allowedBlocks);

        CASING_BLOCK = GregTechAPI.sBlockReinforced;
        FILTER_BLOCK = GregTechAPI.sBlockCasings3;
    }

    /**
     * Doors are funny. So the meta value of the bottom part of the door determines where in the block the door is, when
     * in the "closed" (inactive) position. 0 = lower x coordinate (west). 1 = lower z coordinate (north). 2 = upper x
     * coordinate (east). 3 = upper z coordinate (south). If the door is opened, a 4 is added to this value.
     * <p>
     * The meta of the top part of the door determines which way the door opens. 8 = opens counterclockwise. 9 = opens
     * clockwise.
     * <p>
     * Therefore, to find out where in the block the door currently is, we need to know both the top and the bottom
     * part, as a door that is "closed" on the north side can "open" to either the west or east side. In both cases the
     * meta of the bottom part will be the same (5).
     * <p>
     * This method takes the coordinates of a door block (it is already assumed that this is a door), and returns the
     * direction where the door is. Return value is the same as a default closed door: 0 = west, 1 = north, 2 = east, 3
     * = north.
     */
    protected int getDoorOrientation(IGregTechTileEntity aBaseMetaTileEntity, int dx, int dy, int dz) {
        int meta = aBaseMetaTileEntity.getMetaIDOffset(dx, dy, dz);
        if (meta < 4) {
            // Closed door, easy.
            return meta;
        } else if (meta < 8) {
            // Bottom part of an open door.
            if (aBaseMetaTileEntity.getBlockOffset(dx, dy + 1, dz) instanceof ic2.core.block.BlockIC2Door) {
                return getDoorOrientation(meta, aBaseMetaTileEntity.getMetaIDOffset(dx, dy + 1, dz));
            } else {
                // Bottom part of a door without the top part? Cheater!
                return -1;
            }
        } else if (meta < 10) {
            // Top part of a door.
            if (aBaseMetaTileEntity.getBlockOffset(dx, dy - 1, dz) instanceof ic2.core.block.BlockIC2Door) {
                return getDoorOrientation(aBaseMetaTileEntity.getMetaIDOffset(dx, dy - 1, dz), meta);
            } else {
                // Top part of a door without the bottom part? Cheater!
                return -1;
            }
        } else {
            // Invalid meta value?
            return -1;
        }
    }

    protected int getDoorOrientation(int bottomMeta, int topMeta) {
        if (bottomMeta < 4) {
            // Closed door, easy.
            return bottomMeta;
        } else if (bottomMeta < 8) {
            // Open door.
            if (topMeta == 8) {
                // Opens CCW, add one.
                return (bottomMeta + 1) % 4;
            } else if (topMeta == 9) {
                // Opens CW, subtract one.
                return (bottomMeta - 1) % 4;
            }
        }
        // Invalid combination?
        return -1;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTECleanRoomGui(this);
    }
}
