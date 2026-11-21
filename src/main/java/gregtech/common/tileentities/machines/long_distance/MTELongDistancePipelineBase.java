/**
 *
 * Inspired/ported from GregTech 6 under the LGPL license
 * <p>
 * Copyright (c) 2020 GregTech-6 Team
 * <p>
 * This file is part of GregTech.
 * <p>
 * GregTech is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 * <p>
 * GregTech is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * <p>
 * You should have received a copy of the GNU Lesser General Public License along with GregTech. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package gregtech.common.tileentities.machines.long_distance;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.GOLD;
import static mcp.mobius.waila.api.SpecialChars.RED;
import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.YELLOW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.BlockLongDistancePipe;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicHullNonElectric;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class MTELongDistancePipelineBase extends MTEBasicHullNonElectric {

    protected static final int INPUT_INDEX = 0;
    protected static final int OUTPUT_INDEX = 1;
    protected static final int SIDE_UP_DOWN_INDEX = 2;
    protected static final int SIDE_LEFT_RIGHT_INDEX = 3;

    public static int minimalDistancePoints = 64;

    protected MTELongDistancePipelineBase mTarget = null;
    // these two are updated by machine block update thread, so must be volatile or Concurrent hash map
    protected final Set<MTELongDistancePipelineBase> mSenders = Collections.newSetFromMap(new ConcurrentHashMap<>());
    protected volatile ChunkCoordinates mTargetPos = null;
    protected MTELongDistancePipelineBase mTooCloseTarget = null;
    protected final Set<MTELongDistancePipelineBase> mTooCloseSenders = Collections
        .newSetFromMap(new ConcurrentHashMap<>());
    protected String tooltipPrefixKey;

    public MTELongDistancePipelineBase(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTELongDistancePipelineBase(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        if (tooltipPrefixKey != null) return GTSplit.splitLocalizedFormattedWithPrefix(
            "gt.blockmachines.long_distance_pipeline.desc",
            StatCollector.translateToLocal(tooltipPrefixKey),
            minimalDistancePoints);
        return GTSplit.splitLocalizedFormatted("gt.blockmachines.long_distance_pipeline.desc", minimalDistancePoints);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (mTargetPos != null && mTarget != this) {
            aNBT.setBoolean("target", true);
            aNBT.setInteger("target.x", mTargetPos.posX);
            aNBT.setInteger("target.y", mTargetPos.posY);
            aNBT.setInteger("target.z", mTargetPos.posZ);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("target")) {
            mTargetPos = new ChunkCoordinates(
                aNBT.getInteger("target.x"),
                aNBT.getInteger("target.y"),
                aNBT.getInteger("target.z"));
            if (getDistanceToSelf(mTargetPos) < minimalDistancePoints) mTargetPos = null;
        }
    }

    public boolean isSameClass(MTELongDistancePipelineBase other) {
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
        if (tCurrentItem != null) {
            if (GTUtility.isStackInList(tCurrentItem, GregTechAPI.sSoftMalletList)) {
                scanPipes();
                return true;
            }
        }
        return false;
    }

    private boolean targetingThis(Set<MTELongDistancePipelineBase> senders) {
        for (MTELongDistancePipelineBase sender : senders) {
            if (sender.mTarget != this) {
                return false;
            }
        }
        return true;
    }

    private boolean areAnyDead(Set<MTELongDistancePipelineBase> items) {
        for (MTELongDistancePipelineBase sender : items) {
            if (sender.isDead()) {
                return true;
            }
        }
        return false;
    }

    public boolean isDead() {
        return getBaseMetaTileEntity() == null || getBaseMetaTileEntity().isDead();
    }

    public boolean checkTarget() {
        final IGregTechTileEntity gt_tile = getBaseMetaTileEntity();
        if (gt_tile == null || !gt_tile.isAllowedToWork() || gt_tile.isClientSide()) return false;
        World world = gt_tile.getWorld();
        if (world == null) return false;

        if (mTargetPos == null) {
            // We don't have a target position, scan the pipes
            scanPipes();
        } else if (mTarget == null || mTarget.isDead()) {
            // We don't have a target, or it's dead. Try checking the target position
            mTarget = null;
            if (world.blockExists(mTargetPos.posX, mTargetPos.posY, mTargetPos.posZ)) {
                // Only check if the target position is loaded
                TileEntity te = world.getTileEntity(mTargetPos.posX, mTargetPos.posY, mTargetPos.posZ);
                final IMetaTileEntity tMeta;
                if (te instanceof BaseMetaTileEntity
                    && ((tMeta = ((BaseMetaTileEntity) te).getMetaTileEntity()) instanceof MTELongDistancePipelineBase)
                    && isSameClass((MTELongDistancePipelineBase) tMeta)) {
                    // It's the right type!
                    mTarget = (MTELongDistancePipelineBase) tMeta;
                } else if (te != null) {
                    // It isn't the right type, kill the target position
                    mTargetPos = null;
                }
            }
        }
        if (mTooCloseTarget != null && !mTooCloseTarget.mSenders.contains(this)) {
            mTooCloseTarget.mTooCloseSenders.add(this);
        }

        // prune stale too-close senders pointing at us
        mTooCloseSenders.removeIf(p -> p.isDead() || p.mTarget != null);

        if (mTarget == null || mTarget == this) return false;

        // prune stale senders on the target, then register ourselves always
        mTarget.mSenders.removeIf(p -> p.isDead() || p.mTarget != mTarget);
        mTarget.mSenders.add(this);
        mTarget.mTooCloseSenders.remove(this);

        return mTarget.mSenders.contains(this);
    }

    @Override
    public ArrayList<String> getSpecialDebugInfo(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer,
        int aLogLevel, ArrayList<String> aList) {
        if (!mSenders.isEmpty()) {
            aList.add("Is Pipeline Output");
            for (MTELongDistancePipelineBase sender : mSenders) {
                final ChunkCoordinates coords = sender.getCoords();
                aList.add("Pipeline Input is at: X: " + coords.posX + " Y: " + coords.posY + " Z: " + coords.posZ);
            }
        } else {
            aList.addAll(
                Arrays.asList(
                    checkTarget() ? "Is connected to Pipeline Output" : "Pipeline Output is not connected/chunkloaded",
                    "Pipeline Output should be around: X: " + mTargetPos.posX
                        + " Y: "
                        + mTargetPos.posY
                        + " Z: "
                        + mTargetPos.posZ));
        }
        return aList;
    }

    // What meta should the pipes for this pipeline have
    public abstract int getPipeMeta();

    protected void scanPipes() {
        if (!mSenders.isEmpty() && !areAnyDead(mSenders) && targetingThis(mSenders)) return;

        // Check if we need to scan anything
        final IGregTechTileEntity gtTile = getBaseMetaTileEntity();
        if (gtTile == null) return;

        final World world = gtTile.getWorld();
        if (world == null) return;

        mTargetPos = getCoords();
        mTarget = this;
        mSenders.removeIf(p -> p.isDead() || p.mTarget != this);

        // Start scanning from the output side
        Block aBlock = gtTile.getBlockAtSide(gtTile.getBackFacing());

        if (aBlock instanceof BlockLongDistancePipe) {
            int aMetaData = gtTile.getMetaIDAtSide(gtTile.getBackFacing());
            if (aMetaData != getPipeMeta()) return;

            HashSet<ChunkCoordinates> tVisited = new HashSet<>(Collections.singletonList(getCoords())),
                tWires = new HashSet<>();
            Queue<ChunkCoordinates> tQueue = new LinkedList<>(
                Collections.singletonList(getFacingOffset(gtTile, gtTile.getBackFacing())));

            while (!tQueue.isEmpty()) {
                final ChunkCoordinates aCoords = tQueue.poll();

                if (world.getBlock(aCoords.posX, aCoords.posY, aCoords.posZ) == aBlock
                    && world.getBlockMetadata(aCoords.posX, aCoords.posY, aCoords.posZ) == aMetaData) {
                    // We've got another pipe/wire block
                    // TODO: Make sure it's the right type of pipe/wire via meta
                    ChunkCoordinates tCoords;
                    tWires.add(aCoords);

                    // For each direction, if we haven't already visited that coordinate, add it to the end of the
                    // queue
                    if (tVisited.add(tCoords = new ChunkCoordinates(aCoords.posX + 1, aCoords.posY, aCoords.posZ)))
                        tQueue.add(tCoords);
                    if (tVisited.add(tCoords = new ChunkCoordinates(aCoords.posX - 1, aCoords.posY, aCoords.posZ)))
                        tQueue.add(tCoords);
                    if (tVisited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY + 1, aCoords.posZ)))
                        tQueue.add(tCoords);
                    if (tVisited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY - 1, aCoords.posZ)))
                        tQueue.add(tCoords);
                    if (tVisited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY, aCoords.posZ + 1)))
                        tQueue.add(tCoords);
                    if (tVisited.add(tCoords = new ChunkCoordinates(aCoords.posX, aCoords.posY, aCoords.posZ - 1)))
                        tQueue.add(tCoords);
                } else {
                    // It's not a block - let's see if it's a tile entity
                    TileEntity tTileEntity = world.getTileEntity(aCoords.posX, aCoords.posY, aCoords.posZ);
                    if (tTileEntity != gtTile && tTileEntity instanceof BaseMetaTileEntity
                        && ((BaseMetaTileEntity) tTileEntity)
                            .getMetaTileEntity() instanceof MTELongDistancePipelineBase tGtTile) {
                        if (isSameClass(tGtTile) && tWires.contains(
                            tGtTile.getFacingOffset(
                                (BaseMetaTileEntity) tTileEntity,
                                ((BaseMetaTileEntity) tTileEntity).getFrontFacing()))) {
                            // If it's the same class, and we've scanned a wire in front of it (the input side), we've
                            // found our target
                            // still need to check if it's distant enough
                            int distance = getDistanceToSelf(aCoords);
                            if (distance > minimalDistancePoints) {
                                mTarget = tGtTile;
                                mTargetPos = tGtTile.getCoords();
                                mTooCloseTarget = null;
                                return;
                            } else {
                                if (mTooCloseTarget == null) {
                                    mTooCloseTarget = tGtTile;
                                }
                            }
                        }

                        // Remove this block from the visited because we might end up back here from another wire that
                        // IS connected to the
                        // input side
                        tVisited.remove(aCoords);
                    }
                }
            }
        }
    }

    protected int getDistanceToSelf(ChunkCoordinates aCoords) {
        return Math.abs(getBaseMetaTileEntity().getXCoord() - aCoords.posX)
            + Math.abs(getBaseMetaTileEntity().getYCoord() - aCoords.posY)
            + Math.abs(getBaseMetaTileEntity().getZCoord() - aCoords.posZ);
    }

    public ChunkCoordinates getFacingOffset(IGregTechTileEntity gt_tile, ForgeDirection side) {
        return new ChunkCoordinates(
            gt_tile.getOffsetX(side, 1),
            gt_tile.getOffsetY(side, 1),
            gt_tile.getOffsetZ(side, 1));
    }

    public ChunkCoordinates getCoords() {
        final IGregTechTileEntity gt_tile = getBaseMetaTileEntity();
        return new ChunkCoordinates(gt_tile.getXCoord(), gt_tile.getYCoord(), gt_tile.getZCoord());
    }

    @Override
    public void onMachineBlockUpdate() {
        mTargetPos = null;
        mSenders.clear();
    }

    @Override
    public boolean shouldTriggerBlockUpdate() {
        return true;
    }

    abstract public ITexture[] getTextureOverlays();

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        ITexture[][][] rTextures = new ITexture[4][17][];
        ITexture[] overlays = getTextureOverlays();
        for (int i = 0; i < rTextures[0].length; i++) {
            rTextures[INPUT_INDEX][i] = new ITexture[] { MACHINE_CASINGS[mTier][i], overlays[INPUT_INDEX] };
            rTextures[OUTPUT_INDEX][i] = new ITexture[] { MACHINE_CASINGS[mTier][i], overlays[OUTPUT_INDEX] };
            rTextures[SIDE_UP_DOWN_INDEX][i] = new ITexture[] { MACHINE_CASINGS[mTier][i],
                overlays[SIDE_UP_DOWN_INDEX] };
            rTextures[SIDE_LEFT_RIGHT_INDEX][i] = new ITexture[] { MACHINE_CASINGS[mTier][i],
                overlays[SIDE_LEFT_RIGHT_INDEX] };
        }
        return rTextures;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        colorIndex += 1;
        if (sideDirection == facingDirection) return mTextures[INPUT_INDEX][colorIndex];
        else if (sideDirection == facingDirection.getOpposite()) return mTextures[OUTPUT_INDEX][colorIndex];
        else {
            switch (facingDirection) {
                case UP, DOWN -> {
                    return mTextures[SIDE_UP_DOWN_INDEX][colorIndex];
                }
                case NORTH -> {
                    switch (sideDirection) {
                        case DOWN, UP -> {
                            return mTextures[SIDE_UP_DOWN_INDEX][colorIndex];
                        }
                        case EAST, WEST -> {
                            return mTextures[SIDE_LEFT_RIGHT_INDEX][colorIndex];
                        }
                        default -> {}
                    }
                }
                case SOUTH -> {
                    switch (sideDirection) {
                        case DOWN, UP -> {
                            return mTextures[SIDE_UP_DOWN_INDEX][colorIndex];
                        }
                        case EAST, WEST -> {
                            return mTextures[SIDE_LEFT_RIGHT_INDEX][colorIndex];
                        }
                        default -> {}
                    }
                }
                case EAST, WEST -> {
                    return mTextures[SIDE_LEFT_RIGHT_INDEX][colorIndex];
                }
                default -> {}
            }
        }
        return mTextures[INPUT_INDEX][colorIndex]; // dummy
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final ForgeDirection facing = getBaseMetaTileEntity().getFrontFacing();
        final ForgeDirection side = accessor.getSide();

        final NBTTagCompound tag = accessor.getNBTData();
        final boolean hasInput = tag.getBoolean("hasInput");
        final boolean hasInputTooClose = tag.getBoolean("hasInputTooClose");
        final boolean hasOutput = tag.getBoolean("hasOutput");
        final boolean hasOutputTooClose = tag.getBoolean("hasOutputTooClose");

        if (side == facing)
            currentTip.add(GOLD + StatCollector.translateToLocal("GT5U.waila.long_distance_pipe.input") + RESET);
        else if (side == facing.getOpposite())
            currentTip.add(BLUE + StatCollector.translateToLocal("GT5U.waila.long_distance_pipe.output") + RESET);
        else currentTip.add(StatCollector.translateToLocal("GT5U.waila.long_distance_pipe.side"));

        if (!hasInput && !hasInputTooClose && !hasOutput && !hasOutputTooClose) {
            currentTip
                .add(YELLOW + StatCollector.translateToLocal("GT5U.waila.long_distance_pipe.not_connected") + RESET);
        }

        if (hasInput)
            currentTip.add(StatCollector.translateToLocal("GT5U.waila.long_distance_pipe.connected_to.input"));
        else if (hasInputTooClose) currentTip.add(
            RED + StatCollector.translateToLocal("GT5U.waila.long_distance_pipe.connected_to.input.too_close") + RESET);
        else if (hasOutput)
            currentTip.add(StatCollector.translateToLocal("GT5U.waila.long_distance_pipe.connected_to.output"));
        else if (hasOutputTooClose) currentTip.add(
            RED + StatCollector.translateToLocal("GT5U.waila.long_distance_pipe.connected_to.output.too_close")
                + RESET);

        super.getWailaBody(itemStack, currentTip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        tag.setBoolean("hasInput", !mSenders.isEmpty());
        tag.setBoolean("hasInputTooClose", !mTooCloseSenders.isEmpty());
        tag.setBoolean("hasOutput", mTarget != null && mTarget != this);
        tag.setBoolean("hasOutputTooClose", mTooCloseTarget != null);
    }
}
