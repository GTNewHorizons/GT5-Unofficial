package gregtech.common.tileentities.machines.multi;

import static bartworks.API.GlassTier.getGlassTier;
import static gregtech.api.enums.GTValues.debugCleanroom;
import static gregtech.api.enums.Textures.BlockIcons.BLOCK_PLASCRETE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_CLEANROOM_GLOW;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;

import gregtech.api.GregTechAPI;
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
import gregtech.common.config.MachineStats;

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
     * Minimum number of plascrete blocks. Read from config file.
     */
    public static int MIN_CASING_COUNT = 20;

    /**
     * Maximum percentage of plascrete blocks which can be replaced by other blocks. Read from config file.
     */
    public static int MAX_REPLACEMENT_PERCENTAGE = 30;

    /**
     * List of other blocks allowed in the cleanroom.
     * Format of entries is either just the block's unlocalized name, or <unlocalized name>:<meta>. The former matches
     * all blocks of that name regardless of meta value. Read from config file.
     */
    public static final HashSet<String> ALLOWED_BLOCKS = new HashSet<>();

    // Plascrete blocks.
    protected static Block CASING_BLOCK;
    protected static final int CASING_META = 2;
    // Filter casings.
    protected static Block FILTER_BLOCK;
    protected static final int FILTER_META = 11;

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
        mSoftHammer = false;
        mHardHammer = false;
        mSolderingTool = false;
        mCrowbar = false;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Cleanroom")
            .addInfo("Controller block for the Cleanroom")
            .addInfo("Consumes 40 EU/t when first turned on, and 4 EU/t once at 100% efficiency.")
            .addInfo("Can accept 2A from an LV energy hatch.")
            .addInfo("Will overclock and gain efficiency faster starting from HV.")
            .addSeparator()
            .addInfo(EnumChatFormatting.DARK_RED + "Warning:")
            .addInfo("Below 100% efficiency machines inside have a chance to void outputs!")
            .addInfo("Each maintenance issue reduces maximum efficiency by 10%.")
            .addInfo("Generating any pollution inside causes the cleanroom to shut down.")
            .addSeparator()
            .beginVariableStructureBlock(3, MAX_WIDTH, 4, MAX_HEIGHT, 3, MAX_WIDTH, true)
            .addController("Top center.")
            .addStructureInfo("  If width or length is even, it can be in either of the two middle positions.")
            .addOtherStructurePart("Filter Machine Casing", "Top layer, except for edges.")
            .addEnergyHatch("Any casing except top layer. Exactly one.")
            .addMaintenanceHatch("Any casing except top layer. Exactly one.")
            .addOtherStructurePart(
                "Plascrete Blocks",
                "Edges of top layer, walls, and floor. Minimum " + EnumChatFormatting.GOLD
                    + MIN_CASING_COUNT
                    + EnumChatFormatting.GRAY
                    + ".")
            .addStructureInfo(
                "Up to " + MAX_REPLACEMENT_PERCENTAGE + "% of plascrete in walls and floor can be replaced")
            .addStructureInfo("by hatches and other valid blocks.")
            .addStructureInfo("Try some of the following:")
            .addStructureInfo(
                "- Any " + EnumChatFormatting.DARK_GRAY + "EV+" + EnumChatFormatting.GRAY + " tier glass.")
            .addStructureInfo("- Machine hulls or diodes for power and item transfer.")
            .addStructureInfo(
                "- Reinforced Doors (" + EnumChatFormatting.ITALIC
                    + "IC2"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + "). Keep closed, no gaps allowed or efficiency will drop!")
            .addStructureInfo(
                "- Elevators (" + EnumChatFormatting.ITALIC
                    + "OpenBlocks"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + ") or Travel Anchors ("
                    + EnumChatFormatting.ITALIC
                    + "EnderIO"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + ").")
            .addStructureInfo("See config/GregTech/MachineStats.cfg for more valid blocks.")
            .addStructureInfo(
                EnumChatFormatting.YELLOW
                    + "All non-plascrete blocks now share the same limit. Feel free to mix and match!")
            .toolTipFinisher("Gregtech");
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
        calculateOverclockedNessMultiInternal(
            40,
            45 * Math.max(1, mHeight - 1),
            inputVoltage == TierEU.LV ? 2 : 1,
            inputVoltage,
            false);
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

    // Extent in horizontal directions. Specifically the offset from the controller to each wall.
    // Min values will be negative, Max values positive.
    protected int dxMin = 0, dxMax = 0, dzMin = 0, dzMax = 0, dyMin = 0;
    // Total number of plascrete blocks in the structure.
    protected int casingCount;
    // Total number of other blocks in the structure. Does NOT count filter casings or the controller.
    protected int otherCount;
    // Whether the cleanroom contains a door that is "open", efficiency is constantly reduced.
    protected boolean isDoorOpen;

    /**
     * Find the horizontal size of the cleanroom. Populates values dxMin, dxMax, dzMin, and dzMax.
     *
     * @return True on success, false on failure (which means an invalid structure).
     */
    protected boolean checkSize(IGregTechTileEntity aBaseMetaTileEntity) {
        // Footprint must be a rectangle. If the width is odd, the controller must be in the middle.
        // If the width is even, controller must be one of the two middle blocks.
        Block block;
        int meta;

        // X direction

        for (dxMin = -1; dxMin >= -MAX_WIDTH / 2; --dxMin) {
            block = aBaseMetaTileEntity.getBlockOffset(dxMin, 0, 0);
            meta = aBaseMetaTileEntity.getMetaIDOffset(dxMin, 0, 0);
            if (block == FILTER_BLOCK && meta == FILTER_META) {
                continue;
            } else if (block == CASING_BLOCK && meta == CASING_META) {
                break;
            } else {
                if (debugCleanroom) GTLog.out.println("Cleanroom: Unable to detect width (x-axis).");
                return false;
            }
        }
        if (dxMin < -MAX_WIDTH / 2) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Too large (x-axis).");
            return false;
        }

        for (dxMax = 1; dxMax <= MAX_WIDTH / 2; ++dxMax) {
            block = aBaseMetaTileEntity.getBlockOffset(dxMax, 0, 0);
            meta = aBaseMetaTileEntity.getMetaIDOffset(dxMax, 0, 0);
            if (block == FILTER_BLOCK && meta == FILTER_META) {
                continue;
            } else if (block == CASING_BLOCK && meta == CASING_META) {
                break;
            } else {
                if (debugCleanroom) GTLog.out.println("Cleanroom: Unable to detect width (x-axis).");
                return false;
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
            block = aBaseMetaTileEntity.getBlockOffset(0, 0, dzMin);
            meta = aBaseMetaTileEntity.getMetaIDOffset(0, 0, dzMin);
            if (block == FILTER_BLOCK && meta == FILTER_META) {
                continue;
            } else if (block == CASING_BLOCK && meta == CASING_META) {
                break;
            } else {
                if (debugCleanroom) GTLog.out.println("Cleanroom: Unable to detect width (z-axis).");
                return false;
            }
        }
        if (dzMin < -MAX_WIDTH / 2) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Too large (z-axis).");
            return false;
        }

        for (dzMax = 1; dzMax <= MAX_WIDTH / 2; ++dzMax) {
            block = aBaseMetaTileEntity.getBlockOffset(0, 0, dzMax);
            meta = aBaseMetaTileEntity.getMetaIDOffset(0, 0, dzMax);
            if (block == FILTER_BLOCK && meta == FILTER_META) {
                continue;
            } else if (block == CASING_BLOCK && meta == CASING_META) {
                break;
            } else {
                if (debugCleanroom) GTLog.out.println("Cleanroom: Unable to detect width (z-axis).");
                return false;
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
            if (aBaseMetaTileEntity.getBlockOffset(dx, 0, dzMin) != CASING_BLOCK
                || aBaseMetaTileEntity.getMetaIDOffset(dx, 0, dzMin) != CASING_META
                || aBaseMetaTileEntity.getBlockOffset(dx, 0, dzMax) != CASING_BLOCK
                || aBaseMetaTileEntity.getMetaIDOffset(dx, 0, dzMax) != CASING_META) {
                if (debugCleanroom) GTLog.out.println("Cleanroom: Ceiling edge is not plascrete.");
                return false;
            }
        }

        for (int dz = dzMin + 1; dz <= dzMax - 1; ++dz) {
            if (aBaseMetaTileEntity.getBlockOffset(dxMin, 0, dz) != CASING_BLOCK
                || aBaseMetaTileEntity.getMetaIDOffset(dxMin, 0, dz) != CASING_META
                || aBaseMetaTileEntity.getBlockOffset(dxMax, 0, dz) != CASING_BLOCK
                || aBaseMetaTileEntity.getMetaIDOffset(dxMax, 0, dz) != CASING_META) {
                if (debugCleanroom) GTLog.out.println("Cleanroom: Ceiling edge is not plascrete.");
                return false;
            }
        }

        for (int dx = dxMin + 1; dx <= dxMax - 1; ++dx) {
            for (int dz = dzMin + 1; dz <= dzMax - 1; ++dz) {
                if (dx == 0 && dz == 0) continue; // controller
                if (aBaseMetaTileEntity.getBlockOffset(dx, 0, dz) != FILTER_BLOCK
                    || aBaseMetaTileEntity.getMetaIDOffset(dx, 0, dz) != FILTER_META) {
                    if (debugCleanroom) GTLog.out.println("Cleanroom: Ceiling block is not a filter.");
                    return false;
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
        ArrayList<IGregTechTileEntity> maintenance = new ArrayList<>();
        ArrayList<IGregTechTileEntity> energy = new ArrayList<>();
        int addedCasings = 0;
        int addedOther = 0;

        for (int dx = dxMin + 1; dx <= dxMax - 1; ++dx) {
            for (int dz = dzMin + 1; dz <= dzMax - 1; ++dz) {
                Block block = aBaseMetaTileEntity.getBlockOffset(dx, dy, dz);
                int meta = aBaseMetaTileEntity.getMetaIDOffset(dx, dy, dz);

                if (block == CASING_BLOCK && meta == CASING_META) {
                    // Plascrete block.
                    ++addedCasings;
                } else if (getGlassTier(block, meta) >= 4) {
                    // EV+ glass.
                    ++addedOther;
                } else if (ALLOWED_BLOCKS.contains(block.getUnlocalizedName())
                    || ALLOWED_BLOCKS.contains(block.getUnlocalizedName() + ":" + meta)) {
                        // Another allowed block.
                        ++addedOther;
                    } else {
                        // Possibly hatch or IO.
                        IGregTechTileEntity te = aBaseMetaTileEntity.getIGregTechTileEntityOffset(dx, dy, dz);
                        if (te != null) {
                            IMetaTileEntity mte = te.getMetaTileEntity();
                            if (mte instanceof MTEHatchMaintenance) {
                                maintenance.add(te);
                                ++addedOther;
                            } else if (mte instanceof MTEHatchEnergy) {
                                energy.add(te);
                                ++addedOther;
                            } else if (mte instanceof MTEBasicHull) {
                                // Both hulls and diodes get here.
                                ++addedOther;
                            } else {
                                // Not a valid TE.
                                return false;
                            }
                        } else {
                            // Not a valid floor block.
                            return false;
                        }
                    }
            }
        }

        // If we get here, the entire floor is valid. Add hatches to the machine.
        for (var te : maintenance) addMaintenanceToMachineList(te, 210);
        for (var te : energy) addEnergyInputToMachineList(te, 210);
        casingCount += addedCasings;
        otherCount += addedOther;
        return true;
    }

    /**
     * Doors are funny. So the meta value of the bottom part of the door determines where in the block the door is, when
     * in the "closed" (inactive) position.
     * 0 = lower x coordinate (west).
     * 1 = lower z coordinate (north).
     * 2 = upper x coordinate (east).
     * 3 = upper z coordinate (south).
     * If the door is opened, a 4 is added to this value.
     * <p>
     * The meta of the top part of the door determines which way the door opens.
     * 8 = opens counterclockwise.
     * 9 = opens clockwise.
     * <p>
     * Therefore, to find out where in the block the door currently is, we need to know both the top and the
     * bottom part, as a door that is "closed" on the north side can "open" to either the west or east side.
     * In both cases the meta of the bottom part will be the same (5).
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

    /**
     * Checks a single block in the wall of the CR.
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    protected boolean checkWallBlock(IGregTechTileEntity aBaseMetaTileEntity, int dx, int dy, int dz) {
        Block block = aBaseMetaTileEntity.getBlockOffset(dx, dy, dz);
        int meta = aBaseMetaTileEntity.getMetaIDOffset(dx, dy, dz);

        if (block == CASING_BLOCK && meta == CASING_META) {
            // Plascrete block.
            ++casingCount;
        } else if (getGlassTier(block, meta) >= 4) {
            // EV+ glass.
            ++otherCount;
        } else if (ALLOWED_BLOCKS.contains(block.getUnlocalizedName())
            || ALLOWED_BLOCKS.contains(block.getUnlocalizedName() + ":" + meta)) {
                // Another allowed block.
                ++otherCount;
            } else if (block instanceof ic2.core.block.BlockIC2Door) {
                // IC2 reinforced door. Must be closed; this means parallel to the side it is at.
                // Additionally, if an adjacent block is also an IC2 door, it must be aligned (leaving no gaps).

                if ((dx == dxMin || dx == dxMax) && (dz == dzMin || dz == dzMax)) {
                    // Door can not be on the edges; this allows for gaps.
                    // Technically there are setups where this does not create a gap; but we choose to disallow those,
                    // as checking for them is difficult and this code is already too complex.
                    if (debugCleanroom)
                        GTLog.out.println("Cleanroom: Invalid block at offset (" + dx + ", " + dy + ", " + dz + ").");
                    return false;
                }

                if (!isDoorOpen) {
                    int doorOrientation = getDoorOrientation(aBaseMetaTileEntity, dx, dy, dz);
                    if (doorOrientation < 0) {
                        // Somehow an invalid door block.
                        if (debugCleanroom) GTLog.out
                            .println("Cleanroom: Invalid block at offset (" + dx + ", " + dy + ", " + dz + ").");
                        return false;
                    }
                    if (doorOrientation % 2 == 0) {
                        // Door on the W or E side (aligned with Z axis).
                        if (dx != dxMin && dx != dxMax) isDoorOpen = true;
                        // Check adjacent blocks for other doors.
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
                        if (dz != dzMin && dz != dzMax) isDoorOpen = true;
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
                ++otherCount;
            } else {
                // Possibly hatch or IO.
                IGregTechTileEntity te = aBaseMetaTileEntity.getIGregTechTileEntityOffset(dx, dy, dz);
                if (te != null) {
                    IMetaTileEntity mte = te.getMetaTileEntity();
                    if (mte instanceof MTEHatchMaintenance) {
                        addMaintenanceToMachineList(te, 210);
                        ++otherCount;
                    } else if (mte instanceof MTEHatchEnergy) {
                        addEnergyInputToMachineList(te, 210);
                        ++otherCount;
                    } else if (mte instanceof MTEBasicHull) {
                        // Both hulls and diodes get here.
                        ++otherCount;
                    } else {
                        // Not a valid TE.
                        if (debugCleanroom) GTLog.out
                            .println("Cleanroom: Invalid block at offset (" + dx + ", " + dy + ", " + dz + ").");
                        return false;
                    }
                } else {
                    // Not a valid wall block.
                    if (debugCleanroom)
                        GTLog.out.println("Cleanroom: Invalid block at offset (" + dx + ", " + dy + ", " + dz + ").");
                    return false;
                }
            }

        return true;
    }

    /**
     * Checks the walls of the cleanroom at a specified offset.
     *
     * @param dy Vertical offset of the floor from the controller.
     * @return True on success, false on failure.
     */
    protected boolean checkWall(IGregTechTileEntity aBaseMetaTileEntity, int dy) {
        for (int dx = dxMin; dx <= dxMax; ++dx) {
            if (!checkWallBlock(aBaseMetaTileEntity, dx, dy, dzMin)) return false;
            if (!checkWallBlock(aBaseMetaTileEntity, dx, dy, dzMax)) return false;
        }
        for (int dz = dzMin + 1; dz <= dzMax - 1; ++dz) {
            if (!checkWallBlock(aBaseMetaTileEntity, dxMin, dy, dz)) return false;
            if (!checkWallBlock(aBaseMetaTileEntity, dxMax, dy, dz)) return false;
        }
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

        // Check walls until we find a valid floor.
        for (dyMin = -1; dyMin >= -(MAX_HEIGHT - 1); --dyMin) {
            if (!checkWall(aBaseMetaTileEntity, dyMin)) {
                return false;
            }
            if (dyMin < -2 && checkFloor(aBaseMetaTileEntity, dyMin)) {
                break;
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

        if (casingCount < MIN_CASING_COUNT) {
            if (debugCleanroom) GTLog.out.println("Cleanroom: Not enough plascrete blocks.");
            return false;
        }

        if ((otherCount * 100) / (casingCount + otherCount) > MAX_REPLACEMENT_PERCENTAGE) {
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
                    if (te instanceof ICleanroomReceiver receiver) {
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
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        int i = Math.min(itemStack.stackSize, 7);
        IGregTechTileEntity baseEntity = this.getBaseMetaTileEntity();
        World world = baseEntity.getWorld();
        int x = baseEntity.getXCoord();
        int y = baseEntity.getYCoord();
        int z = baseEntity.getZCoord();
        int yoff = Math.max(i * 2, 3);
        for (int X = x - i; X <= x + i; X++) for (int Y = y; Y >= y - yoff; Y--) for (int Z = z - i; Z <= z + i; Z++) {
            if (X == x && Y == y && Z == z) continue;
            if (X == x - i || X == x + i || Z == z - i || Z == z + i || Y == y - yoff) {
                if (b) StructureLibAPI.hintParticle(world, X, Y, Z, GregTechAPI.sBlockReinforced, 2);
                else world.setBlock(X, Y, Z, GregTechAPI.sBlockReinforced, 2, 2);
            } else if (Y == y) {
                if (b) StructureLibAPI.hintParticle(world, X, Y, Z, GregTechAPI.sBlockCasings3, 11);
                else world.setBlock(X, Y, Z, GregTechAPI.sBlockCasings3, 11, 2);
            }
        }
    }

    /*
     * Configurable values.
     */

    @Override
    public void onConfigLoad() {
        MIN_CASING_COUNT = MachineStats.cleanroom.minCasingCount;
        MAX_REPLACEMENT_PERCENTAGE = MachineStats.cleanroom.maxReplacementPercentage;
        ALLOWED_BLOCKS.clear();
        Collections.addAll(ALLOWED_BLOCKS, MachineStats.cleanroom.allowedBlocks);

        CASING_BLOCK = GregTechAPI.sBlockReinforced;
        FILTER_BLOCK = GregTechAPI.sBlockCasings3;
    }
}
