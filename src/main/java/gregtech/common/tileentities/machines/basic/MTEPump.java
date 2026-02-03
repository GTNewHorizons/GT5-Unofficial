package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GTValues.debugBlockPump;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.ChunkPosition;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidBlock;

import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.misc.DrillingLogicDelegate;

public class MTEPump extends MTEBasicMachine {

    private static final ItemStack MINING_PIPE = GTModHandler.getIC2Item("miningPipe", 0);

    private static final ItemStack MINING_PIPE_ONE = GTModHandler.getIC2Item("miningPipe", 1);
    private static final Block MINING_PIPE_BLOCK = GTUtility.getBlockFromStack(MINING_PIPE);
    private static final Block MINING_PIPE_TIP_BLOCK = GTUtility
        .getBlockFromStack(GTModHandler.getIC2Item("miningPipeTip", 0));

    public static int getMaxDistanceForTier(int aTier) {
        return (10 * ((int) GTUtility.powInt(1.6D, aTier)));
    }

    public static long getEuUsagePerTier(int aTier) {
        return (16 * ((long) GTUtility.powInt(4, aTier)));
    }

    public ArrayDeque<ChunkPosition> mPumpList = new ArrayDeque<>();
    public boolean wasPumping = false;
    public int mPumpTimer = 0;
    public int mPumpCountBelow = 0;
    public Block mPrimaryPumpedBlock = null;
    public Block mSecondaryPumpedBlock = null;

    private int radiusConfig; // Pump configured radius
    private boolean mRetractDone = false;

    private boolean mDisallowRetract = true;

    public MTEPump(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            new String[] { "The best way to empty Oceans!",
                getEuUsagePerTier(aTier) + " EU/operation, "
                    + GTUtility.safeInt(160 / 20 / (long) GTUtility.powInt(2, aTier))
                    + " sec per bucket, no stuttering",
                "Maximum pumping area: " + (getMaxDistanceForTier(aTier) * 2 + 1)
                    + "x"
                    + (getMaxDistanceForTier(aTier) * 2 + 1),
                "Use Screwdriver to regulate pumping area", "Use Soft Mallet to disable and retract the pipe",
                "Disable the bottom pump to retract the pipe!",
                "Use Soldering Iron to auto retract the pipe when hitting a rock", },
            2,
            2,
            TextureFactory.of(
                TextureFactory
                    .of(Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_SIDE_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_SIDE_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_SIDE")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_SIDE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_FRONT_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_FRONT_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_FRONT")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_FRONT_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_TOP_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_TOP_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_TOP")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_TOP_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_BOTTOM_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_BOTTOM_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_BOTTOM")),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.CustomIcon.createOptional("basicmachines/pump/OVERLAY_BOTTOM_GLOW"))
                    .glow()
                    .build()));

        radiusConfig = getMaxDistanceForTier(mTier);
    }

    public MTEPump(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 2, 2);
        radiusConfig = getMaxDistanceForTier(mTier);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPump(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    private static final FallbackableUITexture progressBarTexture = GTUITextures
        .fallbackableProgressbar("pump", GTUITextures.PROGRESSBAR_CANNER);

    @Override
    protected BasicUIProperties getUIProperties() {
        return BasicUIProperties.builder()
            .maxItemInputs(2)
            .maxItemOutputs(2)
            .slotOverlays((index, isFluid, isOutput, isSpecial) -> {
                if (!isFluid && !isOutput && !isSpecial) {
                    return GTUITextures.OVERLAY_SLOT_MINING_PIPE;
                } else {
                    return null;
                }
            })
            .maxFluidInputs(0)
            .maxFluidOutputs(1)
            .progressBarTexture(progressBarTexture)
            .build();
    }

    @Override
    public int getCapacity() {
        return getCapacityForTier(mTier);
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, side, aStack)
            && aStack.getItem() == DrillingLogicDelegate.MINING_PIPE_STACK.getItem();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        boolean wasPumping = this.wasPumping || !this.mPumpList.isEmpty();
        if (debugBlockPump) {
            GTLog.out.println(
                "PUMP: NBT:Save - WasPumping - " + wasPumping
                    + " blocks ("
                    + this.mPrimaryPumpedBlock
                    + ", "
                    + this.mSecondaryPumpedBlock
                    + ")");
        }
        super.saveNBTData(aNBT);
        aNBT.setString(
            "mPumpedBlock1",
            this.mPrimaryPumpedBlock == null ? "" : Block.blockRegistry.getNameForObject(this.mPrimaryPumpedBlock));
        aNBT.setString(
            "mPumpedBlock2",
            this.mSecondaryPumpedBlock == null ? "" : Block.blockRegistry.getNameForObject(this.mSecondaryPumpedBlock));
        aNBT.setBoolean("wasPumping", wasPumping);
        aNBT.setInteger("radiusConfig", radiusConfig);
        aNBT.setBoolean("mRetractDone", mRetractDone);
        aNBT.setBoolean("mDisallowRetract", mDisallowRetract);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.wasPumping = aNBT.getBoolean("wasPumping");
        if (aNBT.hasKey("radiusConfig")) this.radiusConfig = aNBT.getInteger("radiusConfig");
        this.mPrimaryPumpedBlock = Block.getBlockFromName(aNBT.getString("mPumpedBlock1"));
        this.mSecondaryPumpedBlock = Block.getBlockFromName(aNBT.getString("mPumpedBlock2"));
        this.mRetractDone = aNBT.getBoolean("mRetractDone");
        this.mDisallowRetract = aNBT.getBoolean("mDisallowRetract");

        // Transition from old TE which was derived from MTEHatch
        if (!aNBT.hasKey("mEUt")) {
            // Output of old pump always faces up.
            getBaseMetaTileEntity().setFrontFacing(ForgeDirection.UP);

            // Automatic output on.
            mFluidTransfer = true;

            // Fluid was stored in the hatch, now needs to go to the output.
            if (mFluid != null && mFluid.amount > 0) {
                fluidOutputTank.fill(mFluid, true);
                mFluid = null;
            }

            // Move pipes (or other things) from old slots to new ones.
            if (mInventory[1] != null && mInventory[1].stackSize > 0) {
                mInventory[getInputSlot() + 1] = mInventory[1];
                mInventory[1] = null;
            }

            if (mInventory[0] != null && mInventory[0].stackSize > 0) {
                mInventory[getInputSlot()] = mInventory[0];
                mInventory[0] = null;
            }
        }

        if (debugBlockPump) {
            GTLog.out.println(
                "PUMP: NBT:Load - WasPumping - " + this.wasPumping
                    + "("
                    + aNBT.getString("mPumpedBlock1")
                    + ") "
                    + this.mPrimaryPumpedBlock);
        }
    }

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        if (radiusConfig != getMaxDistanceForTier(mTier)) aNBT.setInteger("radiusConfig", radiusConfig);
        if (!mDisallowRetract) aNBT.setBoolean("mDisallowRetract", false);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);

        if (side == getBaseMetaTileEntity().getFrontFacing() || side == mMainFacing) {
            // Configuring "input from output side allowed".
            return;
        }

        int max = getMaxPumpableDistance();
        if (aPlayer.isSneaking()) {
            if (radiusConfig >= 0) {
                radiusConfig--;
            }
            if (radiusConfig < 0) radiusConfig = max;
        } else {
            if (radiusConfig <= max) {
                radiusConfig++;
            }
            if (radiusConfig > max) radiusConfig = 0;
        }
        GTUtility.sendChatToPlayer(
            aPlayer,
            StatCollector.translateToLocal("GT5U.machines.workareaset") + " "
                + (radiusConfig * 2 + 1)
                + "x"
                + (radiusConfig * 2 + 1)); // TODO Add translation support

        clearQueue(false);
    }

    @Override
    public boolean onSolderingToolRightClick(ForgeDirection side, ForgeDirection wrenchingSide,
        EntityPlayer entityPlayer, float aX, float aY, float aZ, ItemStack aTool) {
        mDisallowRetract = !mDisallowRetract;
        GTUtility.sendChatTrans(
            entityPlayer,
            mDisallowRetract ? "GT5U.machines.autoretract.disabled" : "GT5U.machines.autoretract.enabled");
        return true;
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
            for (int i = 1; (i < 21)
                && ((tTileEntity = getBaseMetaTileEntity()
                    .getIGregTechTileEntityAtSideAndDistance(ForgeDirection.DOWN, i)) != null)
                && ((tTileEntity.getMetaTileEntity() instanceof MTEPump)); i++) {
                // Apparently someone might stack 21 pumps on top of each other, so let's check for that
                getBaseMetaTileEntity().setActive(tTileEntity.isActive());
                this.mPumpCountBelow += 1;
                // The more pumps we have stacked, the faster the ones below go
                ((MTEPump) tTileEntity.getMetaTileEntity()).mPumpTimer -= 1;
                ((MTEPump) tTileEntity.getMetaTileEntity()).mProgresstime += 1;
            }
            if (debugBlockPump && (this.mPumpCountBelow != 0)) {
                GTLog.out.println("PUMP: Detected " + this.mPumpCountBelow + " pumps below this pump.");
            }
            if (this.mPumpCountBelow <= 0) {
                // Only the bottom most pump does anything
                if (getBaseMetaTileEntity().isAllowedToWork()) {
                    mRetractDone = false;
                    if ((getBaseMetaTileEntity().isUniversalEnergyStored(this.getEuUsagePerAction()))
                        && (fluidOutputTank.getFluidAmount() + 1000 <= fluidOutputTank.getCapacity())) {
                        boolean tMovedOneDown = false;
                        if ((this.mPumpList.isEmpty()) && (getBaseMetaTileEntity().getTimer() % 100L == 0L)) {
                            if (!this.wasPumping) {
                                tMovedOneDown = moveOneDown();
                                if (!tMovedOneDown) {
                                    if (canMoveDown(
                                        getBaseMetaTileEntity().getXCoord(),
                                        Math.max(getYOfPumpHead() - 1, 1),
                                        getBaseMetaTileEntity().getZCoord())) {
                                        if (debugBlockPump) {
                                            GTLog.out.println("PUMP: No pipe left. Idle for a little longer.");
                                        }
                                        this.mPumpTimer = 160;
                                    } else {
                                        getBaseMetaTileEntity().disableWorking();
                                        if (debugBlockPump) {
                                            GTLog.out.println("PUMP: Can't move. Retracting in next few ticks");
                                        }
                                    }
                                } else if (debugBlockPump) {
                                    GTLog.out.println("PUMP: Moved down");
                                }
                            } else if (debugBlockPump) {
                                GTLog.out.println("PUMP: Was pumping, didn't move down");
                            }
                        }
                        int x = getBaseMetaTileEntity().getXCoord(), z = getBaseMetaTileEntity().getZCoord();

                        if (!this.hasValidFluid()) {
                            // We don't have a valid block, let's try to find one
                            int y = getYOfPumpHead();

                            if (debugBlockPump && this.mPrimaryPumpedBlock != null) {
                                GTLog.out.println(
                                    "PUMP: Had an invalid pump block. Trying to find a fluid at Y: " + y
                                        + " Previous blocks 1: "
                                        + this.mPrimaryPumpedBlock
                                        + " 2: "
                                        + this.mSecondaryPumpedBlock);
                            }
                            // First look down
                            checkForFluidToPump(x, y - 1, z);

                            // Then look all around
                            checkForFluidToPump(x, y, z + 1);
                            checkForFluidToPump(x, y, z - 1);
                            checkForFluidToPump(x + 1, y, z);
                            checkForFluidToPump(x - 1, y, z);
                            this.clearQueue(false);

                            if (this.hasValidFluid()) {
                                // Don't move down and rebuild the queue if we now have a valid fluid
                                this.wasPumping = true;
                            }

                        } else if (getYOfPumpHead() < getBaseMetaTileEntity().getYCoord()) {
                            // We didn't just look for a block, and the pump head is below the pump
                            if ((tMovedOneDown) || this.wasPumping
                                || ((this.mPumpList.isEmpty()) && (getBaseMetaTileEntity().getTimer() % 200L == 100L))
                                || (getBaseMetaTileEntity().getTimer() % 72000L == 100L)) {
                                // Rebuild the list to pump if any of the following conditions are true:
                                // 1) We just moved down
                                // 2) We were previously pumping (and possibly just reloaded)
                                // 3) We have an empty queue and enough time has passed
                                // 4) A long while has passed
                                if (debugBlockPump) {
                                    GTLog.out.println(
                                        "PUMP: Rebuilding pump list - Size " + this.mPumpList.size()
                                            + " WasPumping: "
                                            + this.wasPumping
                                            + " Timer "
                                            + getBaseMetaTileEntity().getTimer());
                                }
                                int yPump = getBaseMetaTileEntity().getYCoord() - 1, yHead = getYOfPumpHead();

                                this.rebuildPumpQueue(x, yPump, z, yHead);

                                if (debugBlockPump) {
                                    GTLog.out.println("PUMP: Rebuilt pump list - Size " + this.mPumpList.size());
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
                                this.mPumpTimer = GTUtility.safeInt(160 / (long) GTUtility.powInt(2, this.mTier));
                                this.mPumpTimer = mPumpTimer == 0 ? 1 : mPumpTimer;

                                mMaxProgresstime = mPumpTimer;
                                mProgresstime = 0;
                            }
                        } else {
                            // We somehow have a valid fluid, but the head of the pump isn't below the pump. Perhaps
                            // someone broke some pipes
                            // -- Clear the queue and we should try to move down until we can find a valid fluid
                            this.clearQueue(false);
                        }
                    } else if (debugBlockPump) {
                        GTLog.out.println("PUMP: Not enough energy? Free space?");
                    }
                } else {
                    mMaxProgresstime = 0;

                    if (!mRetractDone && ((aTick % 5) == 0) && canOutput(MINING_PIPE_ONE)) {
                        // try retract if all of these conditions are met
                        // 1. not retracted yet
                        // 2. once per 5 tick
                        // 3. can hold retracted pipe in inventory
                        int tHeadY = getYOfPumpHead();
                        if (tHeadY < this.getBaseMetaTileEntity()
                            .getYCoord()) {
                            final int tXCoord = this.getBaseMetaTileEntity()
                                .getXCoord();
                            final int tZCoord = this.getBaseMetaTileEntity()
                                .getZCoord();
                            this.getBaseMetaTileEntity()
                                .getWorld()
                                .setBlockToAir(tXCoord, tHeadY, tZCoord);
                            if (tHeadY < this.getBaseMetaTileEntity()
                                .getYCoord() - 1) {
                                getBaseMetaTileEntity().getWorld()
                                    .setBlock(tXCoord, tHeadY + 1, tZCoord, MINING_PIPE_TIP_BLOCK);
                            }

                            for (int i = 0; i < mOutputItems.length; ++i) {
                                if (aBaseMetaTileEntity.addStackToSlot(getOutputSlot() + i, MINING_PIPE_ONE.copy())) {
                                    break;
                                }
                            }

                            if (debugBlockPump) {
                                GTLog.out.println("PUMP: Retracted one pipe");
                            }
                        } else {
                            mRetractDone = true;
                            if (debugBlockPump) {
                                GTLog.out.println("PUMP: Retract done");
                            }
                        }
                    }
                }
                if (!mDisallowRetract) getBaseMetaTileEntity().setActive(!this.mPumpList.isEmpty());
            }
        }
    }

    private int getMaxPumpableDistance() {
        return getMaxDistanceForTier(this.mTier);
    }

    private long getEuUsagePerAction() {
        return getEuUsagePerTier(this.mTier);
    }

    private boolean hasValidFluid() {
        return mPrimaryPumpedBlock != null && mSecondaryPumpedBlock != null;
    }

    private boolean moveOneDown() {
        boolean foundPipe = false;

        for (int i = 0; i < mInputSlotCount; i++) {
            ItemStack stack = getInputAt(i);
            if (GTUtility.areStacksEqual(stack, MINING_PIPE) && stack.stackSize > 0) {
                foundPipe = true;
                break;
            }
        }

        if (!foundPipe) {
            // No mining pipes
            if (debugBlockPump) {
                GTLog.out.println("PUMP: No mining pipes");
            }
            return false;
        }

        int yHead = getYOfPumpHead();
        if (yHead <= 1) {
            // Let's not punch through bedrock
            if (debugBlockPump) {
                GTLog.out.println("PUMP: At bottom");
            }
            return false;
        }

        int x = getBaseMetaTileEntity().getXCoord(), z = getBaseMetaTileEntity().getZCoord();

        Block aBlock = getBaseMetaTileEntity().getBlock(x, yHead - 1, z);
        boolean canReplaceBlock = aBlock.isReplaceable(getBaseMetaTileEntity().getWorld(), x, yHead - 1, z);

        // We specifically allow replacing water even if we can't consume it
        // (e.g. pump holds a different fluid) to help avoid getting stuck on random water pockets.
        if (!canReplaceBlock || (isFluid(aBlock) && !consumeFluid(x, yHead - 1, z) && !isWater(aBlock))) {
            // Either we didn't consume a fluid, or it's a non-replaceable block, or it's water.
            if (debugBlockPump) {
                GTLog.out.println("PUMP: Did not consume fluid, or non-replaceable block found");
            }
            return false;
        }
        // Try to set the block below us to a tip
        if (!GTUtility.setBlockByFakePlayer(
            getFakePlayer(getBaseMetaTileEntity()),
            x,
            yHead - 1,
            z,
            MINING_PIPE_TIP_BLOCK,
            0,
            false)) {
            if (debugBlockPump) {
                GTLog.out.println("PUMP: Could not set block below to new tip");
            }
            return false;
        }
        // And change the previous block to a pipe -- as long as it isn't the pump itself!
        if (yHead != getBaseMetaTileEntity().getYCoord()) {
            getBaseMetaTileEntity().getWorld()
                .setBlock(x, yHead, z, MINING_PIPE_BLOCK);
        }

        // Remove pipe from inputs.
        foundPipe = false;

        for (int i = 0; i < mInputSlotCount; i++) {
            ItemStack stack = getInputAt(i);
            if (GTUtility.areStacksEqual(stack, MINING_PIPE) && stack.stackSize > 0) {
                foundPipe = true;
                stack.stackSize -= 1;
                if (stack.stackSize == 0) {
                    mInventory[getInputSlot() + i] = null;
                }
                break;
            }
        }

        if (debugBlockPump) {
            if (foundPipe) {
                GTLog.out.println("PUMP: Using 1 pipe");
            } else {
                GTLog.err.println("PUMP: Lowered pipe but could not find pipe in input");
            }
        }

        return true;
    }

    private int getYOfPumpHead() {
        // Let's play find the pump head!

        // TODO: Handle pipe|pipe|head|pipe|pipe
        int y = getBaseMetaTileEntity().getYCoord() - 1, x = getBaseMetaTileEntity().getXCoord(),
            z = getBaseMetaTileEntity().getZCoord();

        while (y > 0) {
            Block curBlock = getBaseMetaTileEntity().getBlock(x, y, z);
            if (curBlock == MINING_PIPE_BLOCK) {
                y--;
            } else if (curBlock == MINING_PIPE_TIP_BLOCK) {
                Block nextBlock = getBaseMetaTileEntity().getBlock(x, y - 1, z);
                if (nextBlock == MINING_PIPE_BLOCK || nextBlock == MINING_PIPE_TIP_BLOCK) {
                    // We're running into an existing set of pipes -- Turn this block into a pipe and keep going
                    this.clearQueue(true);
                    getBaseMetaTileEntity().getWorld()
                        .setBlock(x, y, z, MINING_PIPE_BLOCK);
                    if (debugBlockPump) {
                        GTLog.out.println("PUMP: Hit pipes already in place, trying to merge");
                    }
                }
                y--;

            } else {
                break;
            }
        }

        if (getBaseMetaTileEntity().getBlock(x, y, z) != MINING_PIPE_TIP_BLOCK) {
            if (y != getBaseMetaTileEntity().getYCoord() - 1
                && getBaseMetaTileEntity().getBlock(x, y + 1, z) == MINING_PIPE_BLOCK) {
                // We're below the pump at the bottom of the pipes, we haven't found a tip; make the previous pipe a
                // tip!
                this.clearQueue(true);
                getBaseMetaTileEntity().getWorld()
                    .setBlock(x, y + 1, z, MINING_PIPE_TIP_BLOCK);
                if (debugBlockPump) {
                    GTLog.out.println("PUMP: Did not find a tip at bottom, setting last pipe as tip");
                }
            }
            return y + 1;
        }
        return y;
    }

    private void clearQueue(boolean checkPumping) {
        if (checkPumping) {
            this.wasPumping = !this.mPumpList.isEmpty();
        } else {
            this.wasPumping = false;
        }
        this.mPumpList.clear();
    }

    private void rebuildPumpQueue(int aX, int yStart, int aZ, int yEnd) {
        int mDist = this.radiusConfig;
        doTickProfilingInThisTick = false;
        ArrayDeque<ChunkPosition> fluidsToSearch = new ArrayDeque<>();
        ArrayDeque<ChunkPosition> fluidsFound = new ArrayDeque<>();
        Set<ChunkPosition> checked = new HashSet<>();
        this.clearQueue(false);

        for (int aY = yStart; this.mPumpList.isEmpty() && aY >= yEnd; aY--) {
            // Start at the top (presumably the block below the pump), and work our way down to the end (presumably the
            // location of the pump Head)
            // and build up a queue of fluids to pump
            fluidsToSearch.add(new ChunkPosition(aX, aY, aZ));

            while (!fluidsToSearch.isEmpty()) {
                for (ChunkPosition tPos : fluidsToSearch) {
                    // Look all around
                    if (tPos.chunkPosX < aX + mDist)
                        queueFluid(tPos.chunkPosX + 1, tPos.chunkPosY, tPos.chunkPosZ, fluidsFound, checked);
                    if (tPos.chunkPosX > aX - mDist)
                        queueFluid(tPos.chunkPosX - 1, tPos.chunkPosY, tPos.chunkPosZ, fluidsFound, checked);
                    if (tPos.chunkPosZ < aZ + mDist)
                        queueFluid(tPos.chunkPosX, tPos.chunkPosY, tPos.chunkPosZ + 1, fluidsFound, checked);
                    if (tPos.chunkPosZ > aZ - mDist)
                        queueFluid(tPos.chunkPosX, tPos.chunkPosY, tPos.chunkPosZ - 1, fluidsFound, checked);

                    // And then look up
                    queueFluid(tPos.chunkPosX, tPos.chunkPosY + 1, tPos.chunkPosZ, this.mPumpList, checked);
                }
                this.mPumpList.addAll(fluidsFound);
                fluidsToSearch = fluidsFound;
                fluidsFound = new ArrayDeque<>();
            }

            // Make sure we don't have the pipe location in the queue
            this.mPumpList.remove(new ChunkPosition(aX, aY, aZ));
        }
    }

    private boolean queueFluid(int aX, int aY, int aZ, ArrayDeque<ChunkPosition> fluidsFound,
        Set<ChunkPosition> checked) {
        // If we haven't already looked at this coordinate set, and it's not already in the list of fluids found, see if
        // there is
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
        if (this.hasValidFluid()) return;

        Block aBlock = getBaseMetaTileEntity().getBlock(aX, aY, aZ);
        if (aBlock != null) {
            if (isWater(aBlock)) {
                this.mPrimaryPumpedBlock = Blocks.water;
                this.mSecondaryPumpedBlock = Blocks.flowing_water;
                return;
            }
            if (isLava(aBlock)) {
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

    /** only check if block below can be replaced with pipe tip. pipe stockpile condition is ignored */
    private boolean canMoveDown(int aX, int aY, int aZ) {
        if (!GTUtility.eraseBlockByFakePlayer(getFakePlayer(getBaseMetaTileEntity()), aX, aY, aZ, true)) return false;

        Block aBlock = getBaseMetaTileEntity().getBlock(aX, aY, aZ);

        return aBlock != null && aBlock.isReplaceable(getBaseMetaTileEntity().getWorld(), aX, aY, aZ);
    }

    private boolean consumeFluid(int aX, int aY, int aZ) {
        // Try to consume a fluid at a location
        // Returns true if something was consumed, otherwise false
        if (!GTUtility.eraseBlockByFakePlayer(getFakePlayer(getBaseMetaTileEntity()), aX, aY, aZ, true)) return false;

        Block aBlock = getBaseMetaTileEntity().getBlock(aX, aY, aZ);
        if (!isFluid(aBlock)) {
            return false;
        }

        if (aBlock != null && ((this.mPrimaryPumpedBlock == aBlock) || (this.mSecondaryPumpedBlock == aBlock))) {
            boolean isWaterOrLava = ((this.mPrimaryPumpedBlock == Blocks.water
                || this.mPrimaryPumpedBlock == Blocks.lava));

            if (isWaterOrLava && getBaseMetaTileEntity().getMetaID(aX, aY, aZ) != 0) {
                // Water/Lava that isn't a source block - do nothing here, but set the block to air and consume energy
                // below
                if (debugBlockPump) {
                    GTLog.out.println("PUMP: Water/Lava - Not a source block");
                }

            } else if (getDrainableStack() == null) {
                // The pump has no internal fluid
                if (this.mPrimaryPumpedBlock == Blocks.water) setDrainableStack(Materials.Water.getFluid(1_000));
                else if (this.mPrimaryPumpedBlock == Blocks.lava) setDrainableStack(Materials.Lava.getFluid(1_000));
                else {
                    // Not water or lava; try to drain and set to air
                    setDrainableStack(
                        ((IFluidBlock) aBlock).drain(getBaseMetaTileEntity().getWorld(), aX, aY, aZ, true));
                }

            } else if (GTModHandler.isWater(getDrainableStack()) || GTModHandler.isLava(getDrainableStack())
                || getDrainableStack().isFluidEqual(
                    ((IFluidBlock) aBlock).drain(getBaseMetaTileEntity().getWorld(), aX, aY, aZ, false))) {
                        if (!isWaterOrLava) {
                            // Only set Block to Air for non lava/water fluids
                            this.getBaseMetaTileEntity()
                                .getWorld()
                                .setBlockToAir(aX, aY, aZ);
                        }
                        getDrainableStack().amount += 1000;

                    } else {
                        if (debugBlockPump) {
                            GTLog.out.println("PUMP: Couldn't consume " + aBlock);
                        }
                        // We didn't do anything
                        return false;
                    }

            getBaseMetaTileEntity().decreaseStoredEnergyUnits(this.getEuUsagePerAction(), true);
            getBaseMetaTileEntity().getWorld()
                .setBlock(aX, aY, aZ, Blocks.air, 0, 2);
            return true;
        }
        return false;
    }

    private static boolean isWater(Block aBlock) {
        return aBlock == Blocks.water || aBlock == Blocks.flowing_water;
    }

    private static boolean isLava(Block aBlock) {
        return aBlock == Blocks.lava || aBlock == Blocks.flowing_lava;
    }

    private static boolean isFluid(Block aBlock) {
        return isWater(aBlock) || isLava(aBlock) || aBlock instanceof IFluidBlock;
    }

    @Override
    public ArrayList<String> getSpecialDebugInfo(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer,
        int aLogLevel, ArrayList<String> aList) {
        aList.addAll(
            Arrays.asList(
                EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.machines.pump")
                    + EnumChatFormatting.RESET,
                StatCollector.translateToLocal("GT5U.machines.workarea") + ": "
                    + EnumChatFormatting.GREEN
                    + (radiusConfig * 2 + 1)
                    + EnumChatFormatting.RESET
                    + " "
                    + StatCollector.translateToLocal("GT5U.machines.blocks"),
                "Primary pumping fluid:   "
                    + (this.mPrimaryPumpedBlock != null ? this.mPrimaryPumpedBlock.getLocalizedName() : "None"),
                "Secondary pumping fluid: "
                    + (this.mSecondaryPumpedBlock != null ? this.mSecondaryPumpedBlock.getLocalizedName() : "None"),
                "Pumps below: " + mPumpCountBelow,
                "Queue size: " + mPumpList.size(),
                "Pump head at Y: " + getYOfPumpHead(),
                "Pump timer: " + mPumpTimer,
                "Meta Entity Timer: " + getBaseMetaTileEntity().getTimer()));
        return aList;
    }

    private FakePlayer mFakePlayer = null;

    protected FakePlayer getFakePlayer(IGregTechTileEntity aBaseTile) {
        if (mFakePlayer == null) mFakePlayer = GTUtility.getFakePlayer(aBaseTile);
        mFakePlayer.setWorld(aBaseTile.getWorld());
        mFakePlayer.setPosition(aBaseTile.getXCoord(), aBaseTile.getYCoord(), aBaseTile.getZCoord());
        return mFakePlayer;
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            EnumChatFormatting.BLUE + StatCollector.translateToLocal("GT5U.machines.pump") + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("GT5U.machines.workarea") + ": "
                + EnumChatFormatting.GREEN
                + (radiusConfig * 2 + 1)
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.machines.blocks") };
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_PUMP;
    }

}
