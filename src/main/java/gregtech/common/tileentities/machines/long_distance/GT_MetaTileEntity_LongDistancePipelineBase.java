/**
 *
 * Inspired/ported from GregTech 6 under the LGPL license
 *
 * Copyright (c) 2020 GregTech-6 Team
 *
 * This file is part of GregTech.
 *
 * GregTech is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * GregTech is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with GregTech. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package gregtech.common.tileentities.machines.long_distance;

import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.GOLD;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
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

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.GregTech_API;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GT_Block_LongDistancePipe;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicHull_NonElectric;
import gregtech.api.util.GT_Utility;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public abstract class GT_MetaTileEntity_LongDistancePipelineBase extends GT_MetaTileEntity_BasicHull_NonElectric {

    public static int minimalDistancePoints = 64;
    protected GT_MetaTileEntity_LongDistancePipelineBase mTarget = null;
    // these two are updated by machine block update thread, so must be volatile
    protected volatile GT_MetaTileEntity_LongDistancePipelineBase mSender = null;
    protected volatile ChunkCoordinates mTargetPos = null;
    protected GT_MetaTileEntity_LongDistancePipelineBase mTooCloseTarget = null, mTooCloseSender = null;

    public GT_MetaTileEntity_LongDistancePipelineBase(int aID, String aName, String aNameRegional, int aTier,
        String aDescription) {
        super(aID, aName, aNameRegional, aTier, aDescription);
    }

    public GT_MetaTileEntity_LongDistancePipelineBase(String aName, int aTier, String aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
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

    public boolean isSameClass(GT_MetaTileEntity_LongDistancePipelineBase other) {
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        ItemStack tCurrentItem = aPlayer.inventory.getCurrentItem();
        if (tCurrentItem != null) {
            if (GT_Utility.isStackInList(tCurrentItem, GregTech_API.sSoftHammerList)) {
                scanPipes();
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
                    && ((tMeta = ((BaseMetaTileEntity) te)
                        .getMetaTileEntity()) instanceof GT_MetaTileEntity_LongDistancePipelineBase)
                    && isSameClass((GT_MetaTileEntity_LongDistancePipelineBase) tMeta)) {
                    // It's the right type!
                    mTarget = (GT_MetaTileEntity_LongDistancePipelineBase) tMeta;
                } else if (te != null) {
                    // It isn't the right type, kill the target position
                    mTargetPos = null;
                }
            }
        }
        if (mTooCloseTarget != null && mTooCloseTarget.mSender == null) mTooCloseTarget.mTooCloseSender = this;
        if (mTooCloseSender != null && (mTooCloseSender.isDead() || mTooCloseSender.mTarget != null))
            mTooCloseSender = null;
        if (mTarget == null || mTarget == this) return false;
        if (mTarget.mSender == null || mTarget.mSender.isDead()
            || mTarget.mSender.mTarget == null
            || mTarget.mSender.mTarget.isDead()) {
            mTarget.mSender = this;
            mTarget.mTooCloseSender = null;
        }

        return mTarget.mSender == this;
    }

    @Override
    public ArrayList<String> getSpecialDebugInfo(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer,
        int aLogLevel, ArrayList<String> aList) {
        if (mSender != null && !mSender.isDead() && mSender.mTarget == this) {
            final ChunkCoordinates coords = mSender.getCoords();
            aList.addAll(
                Arrays.asList(
                    "Is the Target",
                    "Sender is at: X: " + coords.posX + " Y: " + coords.posY + " Z: " + coords.posZ));
        } else {
            aList.addAll(
                Arrays.asList(
                    checkTarget() ? "Has Target" : "Has no loaded Target",
                    "Target should be around: X: " + mTargetPos.posX
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
        if (mSender != null && !mSender.isDead() && mSender.mTarget == this) return;

        // Check if we need to scan anything
        final IGregTechTileEntity gtTile = getBaseMetaTileEntity();
        if (gtTile == null) return;

        final World world = gtTile.getWorld();
        if (world == null) return;

        mTargetPos = getCoords();
        mTarget = this;
        mSender = null;

        // Start scanning from the output side
        Block aBlock = gtTile.getBlockAtSide(gtTile.getBackFacing());

        if (aBlock instanceof GT_Block_LongDistancePipe) {
            byte aMetaData = gtTile.getMetaIDAtSide(gtTile.getBackFacing());
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

                    // For each direction, if we haven't already visisted that coordinate, add it to the end of the
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
                            .getMetaTileEntity() instanceof GT_MetaTileEntity_LongDistancePipelineBase tGtTile) {
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
            + Math.abs(getBaseMetaTileEntity().getYCoord() - aCoords.posY) / 2
            + Math.abs(getBaseMetaTileEntity().getZCoord() - aCoords.posZ);
    }

    public ChunkCoordinates getFacingOffset(IGregTechTileEntity gt_tile, byte aSide) {
        return new ChunkCoordinates(
            gt_tile.getOffsetX(aSide, 1),
            gt_tile.getOffsetY(aSide, 1),
            gt_tile.getOffsetZ(aSide, 1));
    }

    public ChunkCoordinates getCoords() {
        final IGregTechTileEntity gt_tile = getBaseMetaTileEntity();
        return new ChunkCoordinates(gt_tile.getXCoord(), gt_tile.getYCoord(), gt_tile.getZCoord());
    }

    @Override
    public void onMachineBlockUpdate() {
        // GT_Mod.GT_FML_LOGGER.info("You're dead to me");
        mTargetPos = null;
        mSender = null;
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    @Override
    public boolean shouldTriggerBlockUpdate() {
        return true;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        final NBTTagCompound tag = accessor.getNBTData();
        final int facing = getBaseMetaTileEntity().getFrontFacing();
        final int side = (byte) accessor.getSide()
            .ordinal();

        if (side == facing) currentTip.add(GOLD + "Pipeline Input" + RESET);
        else if (side == ForgeDirection.OPPOSITES[facing]) currentTip.add(BLUE + "Pipeline Output" + RESET);
        else currentTip.add("Pipeline Side");

        if (tag.getBoolean("hasSender")) currentTip.add("Other End of Input: " + GREEN + "distance" + RESET);
        else if (tag.getBoolean("hasTooCloseSender"))
            currentTip.add("Other End of Input: " + RED + "too close" + RESET);
        else currentTip.add("Other End of Input: " + YELLOW + "cannot found(may need to update other end)" + RESET);

        if (tag.getBoolean("hasTarget")) currentTip.add("Other End of Output: " + GREEN + "distance" + RESET);
        else if (tag.getBoolean("hasTooCloseTarget"))
            currentTip.add("Other End of Output: " + RED + "too close" + RESET);
        else currentTip.add("Other End of Output: " + YELLOW + "cannot found" + RESET);

        super.getWailaBody(itemStack, currentTip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        tag.setBoolean("hasSender", mSender != null);
        tag.setBoolean("hasTooCloseSender", mTooCloseSender != null);
        tag.setBoolean("hasTarget", mTarget != null && mTarget != this);
        tag.setBoolean("hasTooCloseTarget", mTooCloseTarget != null);
    }
}
