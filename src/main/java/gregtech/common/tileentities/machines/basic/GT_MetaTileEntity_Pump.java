package gregtech.common.tileentities.machines.basic;

import gregtech.api.enums.Textures;
import gregtech.api.gui.GT_Container_BasicTank;
import gregtech.api.gui.GT_GUIContainer_BasicTank;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.ArrayDeque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static gregtech.api.enums.GT_Values.D1;
import static gregtech.api.enums.GT_Values.V;

public class GT_MetaTileEntity_Pump extends GT_MetaTileEntity_Hatch {
    private static final ItemStack MINING_PIPE = GT_ModHandler.getIC2Item("miningPipe", 0);
    private static final Block MINING_PIPE_BLOCK = GT_Utility.getBlockFromStack(MINING_PIPE);
    private static final Block MINING_PIPE_TIP_BLOCK = GT_Utility.getBlockFromStack(GT_ModHandler.getIC2Item("miningPipeTip", 0));

    public static int getMaxDistanceForTier(byte aTier) {
        return (10 * ((int) Math.pow(1.6D, aTier)));
    }

    public static long getEuUsagePerTier(byte aTier) {
        return (16 * ((long) Math.pow(4, aTier)));
    }

    public ArrayDeque<ChunkPosition> mPumpList = new ArrayDeque<ChunkPosition>();
    public boolean wasPumping = false;
    public int mPumpTimer = 0;
    public int mPumpCountBelow = 0;
    public Block mPrimaryPumpedBlock = null;
    public Block mSecondaryPumpedBlock = null;

    public GT_MetaTileEntity_Pump(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 3,
                new String[]{"The best way to empty Oceans! Outputs on top",
                        "Pumping Area: " + (GT_MetaTileEntity_Pump.getMaxDistanceForTier((byte)aTier) * 2 + 1) + "x" + 
                                           (GT_MetaTileEntity_Pump.getMaxDistanceForTier((byte)aTier) * 2 + 1)});
    }

    public GT_MetaTileEntity_Pump(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Pump(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Pump(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    public void saveNBTData(NBTTagCompound aNBT) {
        boolean wasPumping = this.wasPumping || !this.mPumpList.isEmpty();
        if (D1) {
            GT_Log.out.println("PUMP: NBT:Save - WasPumping - " + wasPumping + " blocks (" + this.mPrimaryPumpedBlock + ", " + this.mSecondaryPumpedBlock + ")");
        }
        super.saveNBTData(aNBT);
        aNBT.setString("mPumpedBlock1", this.mPrimaryPumpedBlock == null ? "" : Block.blockRegistry.getNameForObject(this.mPrimaryPumpedBlock));
        aNBT.setString("mPumpedBlock2", this.mSecondaryPumpedBlock == null ? "" : Block.blockRegistry.getNameForObject(this.mSecondaryPumpedBlock));
        aNBT.setBoolean("wasPumping", wasPumping);
    }

    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.wasPumping = aNBT.getBoolean("wasPumping");
        this.mPrimaryPumpedBlock = Block.getBlockFromName(aNBT.getString("mPumpedBlock1"));
        this.mSecondaryPumpedBlock = Block.getBlockFromName(aNBT.getString("mPumpedBlock2"));

        if (D1) {
            GT_Log.out.println("PUMP: NBT:Load - WasPumping - " + this.wasPumping + "(" + aNBT.getString("mPumpedBlock1") + ") " + this.mPrimaryPumpedBlock);
        }

    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_BasicTank(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_BasicTank(aPlayerInventory, aBaseMetaTileEntity, getLocalName());
    }

    @Override
    public boolean doesFillContainers() {
        return true;
    }

    @Override
    public boolean doesEmptyContainers() {
        return false;
    }

    @Override
    public boolean canTankBeFilled() {
        return false;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }

    @Override
    public boolean displaysItemStack() {
        return true;
    }

    @Override
    public boolean displaysStackSize() {
        return false;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return false;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (getBaseMetaTileEntity().isServerSide()) {
            this.mPumpTimer -= 1;
            if ((getBaseMetaTileEntity() instanceof BaseTileEntity)) {
                ((BaseTileEntity) getBaseMetaTileEntity()).ignoreUnloadedChunks = false;
            }
            this.doTickProfilingInThisTick = true;
            this.mPumpCountBelow = 0;

            IGregTechTileEntity tTileEntity;
            for (int i = 1 ;
                 (i < 21) && ((tTileEntity = getBaseMetaTileEntity().getIGregTechTileEntityAtSideAndDistance((byte) 0, i)) != null)
                    && ((tTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_Pump)) ; i++) 
            {
                // Apparently someone might stack 21 pumps on top of each other, so let's check for that
                getBaseMetaTileEntity().setActive(tTileEntity.isActive());
                this.mPumpCountBelow += 1;
                // The more pumps we have stacked, the faster the ones below go
                ((GT_MetaTileEntity_Pump) tTileEntity.getMetaTileEntity()).mPumpTimer -= 1;
            }
            if (this.mPumpCountBelow <= 0) {
                // Only the bottom most pump does anything
                if ((getBaseMetaTileEntity().isAllowedToWork()) && (getBaseMetaTileEntity().isUniversalEnergyStored(this.getEuUsagePerAction()))
                        && ((this.mFluid == null) || (this.mFluid.amount + 1000 <= getCapacity()))) {
                    boolean tMovedOneDown = false;
                    if ((this.mPumpList.isEmpty()) && (getBaseMetaTileEntity().getTimer() % 100L == 0L)) {
                        if (!this.wasPumping){
                            tMovedOneDown = moveOneDown();
                            if (D1) {
                                GT_Log.out.println("PUMP: Moved down");
                            }
                        } else if (D1) {
                            GT_Log.out.println("PUMP: Was pumping, didn't move down");
                        }
                    }
                    int x = getBaseMetaTileEntity().getXCoord(), z = getBaseMetaTileEntity().getZCoord();

                    if (!this.hasValidFluid()) {
                        // We don't have a valid block, let's try to find one
                        int y = getYOfPumpHead();

                        if (D1 && this.mPrimaryPumpedBlock != null) {
                            GT_Log.out.println("PUMP: Had an invalid pump block. Trying to find a fluid at Y: " + y + 
                                    " Previous blocks 1: " + this.mPrimaryPumpedBlock + " 2: " + this.mSecondaryPumpedBlock);
                        }
                        // First look down
                        checkForFluidToPump(x,     y - 1, z    );

                        // Then look all around
                        checkForFluidToPump(x,     y,     z + 1);
                        checkForFluidToPump(x,     y,     z - 1);
                        checkForFluidToPump(x + 1, y,     z    );
                        checkForFluidToPump(x - 1, y,     z    );
                        this.clearQueue(false);

                        if(this.hasValidFluid()) {
                            // Don't move down and rebuild the queue if we now have a valid fluid
                            this.wasPumping = true;
                        }

                    } else if (getYOfPumpHead() < getBaseMetaTileEntity().getYCoord()) {
                        // We didn't just look for a block, and the pump head is below the pump
                        if ((tMovedOneDown) || this.wasPumping ||
                                ((this.mPumpList.isEmpty()) && (getBaseMetaTileEntity().getTimer() % 200L == 100L)) ||
                                (getBaseMetaTileEntity().getTimer() % 72000L == 100L)) 
                        {
                            // Rebuild the list to pump if any of the following conditions are true:
                            //  1) We just moved down
                            //  2) We were previously pumping (and possibly just reloaded)
                            //  3) We have an empty queue and enough time has passed
                            //  4) A long while has has passed
                            if (D1) {
                                GT_Log.out.println("PUMP: Rebuilding pump list - Size " +
                                        this.mPumpList.size() + " WasPumping: " + this.wasPumping + " Timer " + getBaseMetaTileEntity().getTimer());
                            }
                            int yPump = getBaseMetaTileEntity().getYCoord() - 1, yHead = getYOfPumpHead();

                            this.rebuildPumpQueue(x, yPump, z, yHead);

                            if (D1) {
                                GT_Log.out.println("PUMP: Rebuilt pump list - Size " + this.mPumpList.size());
                            }

                        }
                        if ((!tMovedOneDown) && (this.mPumpTimer <= 0)) {
                            while ((!this.mPumpList.isEmpty())) {
                                ChunkPosition pos = this.mPumpList.pollLast();
                                if (consumeFluid(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ)) {
                                    // Keep trying until we consume something, or the list is empty
                                    break;
                                }
                            }
                            this.mPumpTimer = GT_Utility.safeInt(160 / (long)Math.pow(2, this.mTier) );
                            this.mPumpTimer = mPumpTimer==0 ? 1 : mPumpTimer;
                        }
                    } else {
                        // We somehow have a valid fluid, but the head of the pump isn't below the pump.  Perhaps someone broke some pipes
                        // -- Clear the queue and we should try to move down until we can find a valid fluid
                        this.clearQueue(false);
                    }
                }
                getBaseMetaTileEntity().setActive(!this.mPumpList.isEmpty());
            }

            if (this.mFluid != null && (aTick % 20 == 0)) {
                // auto outputs on top every second or so
                IFluidHandler tTank = aBaseMetaTileEntity.getITankContainerAtSide((byte)1); //1 is up.
                if (tTank != null) {
                    FluidStack tDrained = drain(1000, false);
                    if (tDrained != null) {
                        int tFilledAmount = tTank.fill(ForgeDirection.DOWN, tDrained, false);
                        if (tFilledAmount > 0)
                            tTank.fill(ForgeDirection.DOWN, drain(tFilledAmount, true), true);
                    }
                }
            }
        }
    }

    private int getMaxPumpableDistance() {
        return GT_MetaTileEntity_Pump.getMaxDistanceForTier(this.mTier);
    }

    private long getEuUsagePerAction() {
        return GT_MetaTileEntity_Pump.getEuUsagePerTier(this.mTier); 
    }

    private boolean hasValidFluid() {
        return (!GT_Utility.isBlockInvalid(this.mPrimaryPumpedBlock) && !GT_Utility.isBlockInvalid(this.mSecondaryPumpedBlock));
    }

    private boolean moveOneDown() {
        if ((this.mInventory[0] == null) || (this.mInventory[0].stackSize < 1) || (!GT_Utility.areStacksEqual(this.mInventory[0], MINING_PIPE))) {
            // No mining pipes
            return false;
        }

        int yHead = getYOfPumpHead();
        if (yHead <= 1) {
            // Let's not punch through bedrock
            return false;
        }

        int x = getBaseMetaTileEntity().getXCoord(), z = getBaseMetaTileEntity().getZCoord();

        if ((!consumeFluid(x, yHead - 1, z)) && (!getBaseMetaTileEntity().getBlock(x, yHead - 1, z).isAir(getBaseMetaTileEntity().getWorld(), x, yHead - 1, z))) {
            // Either we didn't consume a fluid, or it's a non Air block
            return false;
        }
        // Try to set the block below us to a a tip 
        if (!GT_Utility.setBlockByFakePlayer(getFakePlayer(getBaseMetaTileEntity()), x, yHead - 1, z, MINING_PIPE_TIP_BLOCK, 0, false)) {
            return false;
        }
        // And change the previous block to a pipe -- as long as it isn't the pump itself!
        if (yHead != getBaseMetaTileEntity().getYCoord()) {
            getBaseMetaTileEntity().getWorld().setBlock(x, yHead, z, MINING_PIPE_BLOCK);
        }
        getBaseMetaTileEntity().decrStackSize(0, 1);
        return true;
    }

    private int getYOfPumpHead() {
        // Let's play find the pump head!

        // TODO: Handle pipe|pipe|head|pipe|pipe
        int y = getBaseMetaTileEntity().getYCoord() - 1, x = getBaseMetaTileEntity().getXCoord(), z = getBaseMetaTileEntity().getZCoord();

        while(y > 0) {
            Block curBlock = getBaseMetaTileEntity().getBlock(x, y, z);
            if(curBlock == MINING_PIPE_BLOCK) {
                y--;
            } else if (curBlock == MINING_PIPE_TIP_BLOCK) {
                Block nextBlock = getBaseMetaTileEntity().getBlock(x, y - 1 , z);
                if (nextBlock == MINING_PIPE_BLOCK || nextBlock == MINING_PIPE_TIP_BLOCK) {
                    // We're running into an existing set of pipes -- Turn this block into a pipe and keep going
                    this.clearQueue(true);
                    getBaseMetaTileEntity().getWorld().setBlock(x, y, z, MINING_PIPE_BLOCK);
                }
                y--;

            } else {
                break;
            }
        }

        if (getBaseMetaTileEntity().getBlock(x, y, z) != MINING_PIPE_TIP_BLOCK) {
            if (y != getBaseMetaTileEntity().getYCoord() - 1 && getBaseMetaTileEntity().getBlock(x, y + 1, z) == MINING_PIPE_BLOCK) {
                // We're below the pump at the bottom of the pipes, we haven't found a tip; make the previous pipe a tip!
                this.clearQueue(true);
                getBaseMetaTileEntity().getWorld().setBlock(x, y + 1, z, MINING_PIPE_TIP_BLOCK);
            }
            return y + 1;
        }
        return y;
    }

    private void clearQueue(boolean checkPumping) {
        if(checkPumping) {
            this.wasPumping = !this.mPumpList.isEmpty();
        } else {
            this.wasPumping = false;
        }
        this.mPumpList.clear();
    }

    private void rebuildPumpQueue(int aX, int yStart, int aZ, int yEnd) {
        int mDist = this.getMaxPumpableDistance();
        doTickProfilingInThisTick = false;
        ArrayDeque<ChunkPosition> fluidsToSearch = new ArrayDeque<ChunkPosition>();
        ArrayDeque<ChunkPosition> fluidsFound = new ArrayDeque<ChunkPosition>();
        Set<ChunkPosition> checked = new HashSet<ChunkPosition>();
        this.clearQueue(false);

        for (int aY = yStart ; this.mPumpList.isEmpty() && aY >= yEnd ; aY--) {
            // Start at the top (presumably the block below the pump), and work our way down to the end (presumably the location of the pump Head)
            // and build up a queue of fluids to pump
            fluidsToSearch.add(new ChunkPosition(aX, aY, aZ));

            while (!fluidsToSearch.isEmpty()) {
                Iterator<ChunkPosition> i$ = fluidsToSearch.iterator();
                while(i$.hasNext()) {
                    ChunkPosition tPos = i$.next();

                    // Look all around
                    if (tPos.chunkPosX < aX + mDist) queueFluid(tPos.chunkPosX + 1, tPos.chunkPosY, tPos.chunkPosZ,     fluidsFound, checked);
                    if (tPos.chunkPosX > aX - mDist) queueFluid(tPos.chunkPosX - 1, tPos.chunkPosY, tPos.chunkPosZ,     fluidsFound, checked);
                    if (tPos.chunkPosZ < aZ + mDist) queueFluid(tPos.chunkPosX,     tPos.chunkPosY, tPos.chunkPosZ + 1, fluidsFound, checked);
                    if (tPos.chunkPosZ > aZ - mDist) queueFluid(tPos.chunkPosX,     tPos.chunkPosY, tPos.chunkPosZ - 1, fluidsFound, checked);

                    // And then look up
                    queueFluid(tPos.chunkPosX, tPos.chunkPosY + 1, tPos.chunkPosZ, this.mPumpList, checked);
                }
                this.mPumpList.addAll(fluidsFound);
                fluidsToSearch = fluidsFound;
                fluidsFound = new ArrayDeque<ChunkPosition>();
            }

            // Make sure we don't have the pipe location in the queue
            this.mPumpList.remove(new ChunkPosition(aX, aY, aZ));
        }
    }

    private boolean queueFluid(int aX, int aY, int aZ, ArrayDeque<ChunkPosition> fluidsFound, Set<ChunkPosition> checked) {
        // If we haven't already looked at this coordinate set, and it's not already in the list of fluids found, see if there is
        // a valid fluid and add it to the fluids found
        ChunkPosition tCoordinate = new ChunkPosition(aX, aY, aZ);
        if (checked.add(tCoordinate) && !fluidsFound.contains(tCoordinate)) {
            Block aBlock = getBaseMetaTileEntity().getBlock(aX, aY, aZ);
            if ((this.mPrimaryPumpedBlock == aBlock) || (this.mSecondaryPumpedBlock == aBlock)) {
                fluidsFound.addFirst(tCoordinate);
                return true;
            }
        }
        return false;
    }

    private void checkForFluidToPump(int aX, int aY, int aZ) {
        // If we don't currently have a valid fluid to pump, try pumping the fluid at the given coordinates
        if(this.hasValidFluid())
            return;

        Block aBlock = getBaseMetaTileEntity().getBlock(aX, aY, aZ);
        if (GT_Utility.isBlockValid(aBlock)) {
            if ((aBlock == Blocks.water) || (aBlock == Blocks.flowing_water)) {
                this.mPrimaryPumpedBlock = Blocks.water;
                this.mSecondaryPumpedBlock = Blocks.flowing_water;
                return;
            }
            if ((aBlock == Blocks.lava) || (aBlock == Blocks.flowing_lava)) {
                this.mPrimaryPumpedBlock = Blocks.lava;
                this.mSecondaryPumpedBlock = Blocks.flowing_lava;
                return;
            }
            if ((aBlock instanceof IFluidBlock)) {
                this.mPrimaryPumpedBlock = aBlock;
                this.mSecondaryPumpedBlock = aBlock;
                return;
            }
        }
        this.mPrimaryPumpedBlock = null;
        this.mSecondaryPumpedBlock = null;
    }

    private boolean consumeFluid(int aX, int aY, int aZ) {
        // Try to consume a fluid at a location
        // Returns true if something was consumed, otherwise false
        if (!GT_Utility.eraseBlockByFakePlayer(getFakePlayer(getBaseMetaTileEntity()), aX, aY, aZ, true)) return false;

        Block aBlock = getBaseMetaTileEntity().getBlock(aX, aY, aZ);

        if ((GT_Utility.isBlockValid(aBlock)) && ((this.mPrimaryPumpedBlock == aBlock) || (this.mSecondaryPumpedBlock == aBlock))) {
            boolean isWaterOrLava = ((this.mPrimaryPumpedBlock == Blocks.water || this.mPrimaryPumpedBlock == Blocks.lava));

            if (isWaterOrLava && getBaseMetaTileEntity().getMetaID(aX, aY, aZ) != 0) {
                // Water/Lava that isn't a source block - do nothing here, but set the block to air and consume energy below
                if (D1) {
                    GT_Log.out.println("PUMP: Water/Lava - Not a source block");
                }
            } else if (this.mFluid == null) {
                // The pump has no internal fluid
                if (this.mPrimaryPumpedBlock == Blocks.water)
                    this.mFluid = GT_ModHandler.getWater(1000L);
                else if (this.mPrimaryPumpedBlock == Blocks.lava)
                    this.mFluid = GT_ModHandler.getLava(1000L);
                else {
                    // Not water or lava; try to drain and set to air
                    this.mFluid = ((IFluidBlock) aBlock).drain(getBaseMetaTileEntity().getWorld(), aX, aY, aZ, true);
                }
            } else if (GT_ModHandler.isWater(this.mFluid) || GT_ModHandler.isLava(this.mFluid) ||
                    this.mFluid.isFluidEqual(((IFluidBlock) aBlock).drain(getBaseMetaTileEntity().getWorld(), aX, aY, aZ, false))) 
            {
                if (!isWaterOrLava) {
                    // Only set Block to Air for non lava/water fluids
                    this.getBaseMetaTileEntity().getWorld().setBlockToAir(aX, aY, aZ);
                }
                this.mFluid.amount += 1000;

            } else {
                if (D1) {
                    GT_Log.out.println("PUMP: Couldn't consume " + aBlock);
                }
                // We didn't do anything
                return false;
            }

            getBaseMetaTileEntity().decreaseStoredEnergyUnits(this.getEuUsagePerAction(), true);
            getBaseMetaTileEntity().getWorld().setBlock(aX, aY, aZ, Blocks.air, 0, 2);
            return true;
        }
        return false;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) return true;
        aBaseMetaTileEntity.openGUI(aPlayer);
        return true;
    }


    @Override
    public boolean isSimpleMachine() {
        return false;
    }

    @Override
    public boolean isOverclockerUpgradable() {
        return false;
    }

    @Override
    public boolean isTransformerUpgradable() {
        return false;
    }

    @Override
    public boolean isElectric() {
        return true;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(byte aSide) {
        return true;
    }

    @Override
    public boolean isOutputFacing(byte aSide) {
        return false;
    }

    @Override
    public boolean isTeleporterCompatible() {
        return false;
    }

    @Override
    public long getMinimumStoredEU() {
        return V[mTier] * 16;
    }

    @Override
    public long maxEUStore() {
        return V[mTier] * 64;
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxSteamStore() {
        return maxEUStore();
    }

    @Override
    public long maxAmperesIn() {
        return 2;
    }

    @Override
    public int getStackDisplaySlot() {
        return 2;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }


    @Override
    public int getCapacity() {
        return 16000 * this.mTier;
    }

    @Override
    public int getTankPressure() {
        return 100;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        return new ITexture[]{Textures.BlockIcons.MACHINE_CASINGS[mTier][aColorIndex + 1], (aSide == 0 || aSide == 1) ? new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_PIPE_OUT) : new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ADV_PUMP)};
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return getTexturesInactive(aBaseTexture);
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ADV_PUMP), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ADV_PUMP),
                new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ADV_PUMP), new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_ADV_PUMP),};
    }
    private FakePlayer mFakePlayer = null;

    protected FakePlayer getFakePlayer(IGregTechTileEntity aBaseTile) {
        if (mFakePlayer == null) mFakePlayer = GT_Utility.getFakePlayer(aBaseTile);
        mFakePlayer.setWorld(aBaseTile.getWorld());
        mFakePlayer.setPosition(aBaseTile.getXCoord(), aBaseTile.getYCoord(), aBaseTile.getZCoord());
        return mFakePlayer;
    }
}
