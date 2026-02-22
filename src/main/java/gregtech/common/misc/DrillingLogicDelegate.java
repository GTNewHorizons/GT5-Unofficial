package gregtech.common.misc;

import static gregtech.api.enums.GTValues.debugBlockMiner;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;

import org.jetbrains.annotations.NotNull;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.common.ores.OreManager;

/** @author Relvl on 27.01.2022 */
@SuppressWarnings("ObjectEquality")
public class DrillingLogicDelegate {

    public static final ItemStack MINING_PIPE_STACK = GTModHandler.getIC2Item("miningPipe", 0);
    public static final Block MINING_PIPE_BLOCK = GTUtility.getBlockFromStack(MINING_PIPE_STACK);
    public static final Block MINING_PIPE_TIP_BLOCK = GTUtility
        .getBlockFromStack(GTModHandler.getIC2Item("miningPipeTip", 0));

    /** The owner machine pointer */
    private final IDrillingLogicDelegateOwner owner;

    /** Is pipe retracting process done and halts? */
    private boolean isRetractDone;
    /** Is machine ran out of mining pipes in its inventory and halts? */
    private boolean isWaitingForPipeItem;
    /** Pipe tip depth (relative to machine Y position, NEGATIVE). */
    private int tipDepth;
    /** Cached fake player */
    private FakePlayer mFakePlayer;

    private final XSTR rng = new XSTR();

    public DrillingLogicDelegate(IDrillingLogicDelegateOwner owner) {
        this.owner = owner;
    }

    /** Descents a pipe tip one plock deeper. */
    public boolean descent(IGregTechTileEntity te) {
        if (!te.isAllowedToWork()) {
            return false;
        }

        int xCoord = te.getXCoord();
        int zCoord = te.getZCoord();
        int yCoord = te.getYCoord();
        int checkY = yCoord + tipDepth - 1;
        boolean isHitsTheVoid = checkY < 0;
        boolean isHitsBedrock = GTUtility.getBlockHardnessAt(te.getWorld(), xCoord, checkY, zCoord) < 0;
        boolean isFakePlayerAllowed = canFakePlayerInteract(te, xCoord, checkY, zCoord);

        if (isHitsTheVoid || isHitsBedrock || !isFakePlayerAllowed) {
            // Disable and start retracting process.
            te.disableWorking();
            if (debugBlockMiner) {
                if (isHitsTheVoid) {
                    GTLog.out.println("MINER: Hit bottom");
                }
                if (isHitsBedrock) {
                    GTLog.out.println("MINER: Hit block with -1 hardness");
                }
                if (!isFakePlayerAllowed) {
                    GTLog.out.println("MINER: Unable to set mining pipe tip");
                }
            }
            return false;
        }

        // Replace the tip onto pipe
        if (te.getBlockOffset(0, tipDepth, 0) == MINING_PIPE_TIP_BLOCK) {
            te.getWorld()
                .setBlock(xCoord, yCoord + tipDepth, zCoord, MINING_PIPE_BLOCK);
        }
        // Get and decrease pipe from the machine
        boolean pipeTaken = owner.pullInputs(MINING_PIPE_STACK.getItem(), 1, false);
        if (!pipeTaken) {
            // If there was nothing - waiting for the pipes (just for prevent unnecessary checks)
            isWaitingForPipeItem = true;
            return false;
        }

        // If there is something - mine it
        Block block = te.getBlockOffset(0, tipDepth - 1, 0);
        if (!block.isAir(te.getWorld(), xCoord, yCoord, zCoord)) {
            mineBlock(te, block, xCoord, yCoord + tipDepth - 1, zCoord);
        }

        // Descent the pipe tip
        te.getWorld()
            .setBlock(xCoord, yCoord + tipDepth - 1, zCoord, MINING_PIPE_TIP_BLOCK);
        tipDepth--;
        return true;
    }

    public void onOwnerPostTick(IGregTechTileEntity te, long tick) {
        // If the machine was disabled - try to retract pipe
        if (!te.isAllowedToWork()) {
            onPostTickRetract(te, tick);
            return;
        }
        // If the machine was re-enabled - we should reset the retracting process
        isRetractDone = false;
    }

    /** If the machine are disabled - tried to retract pipe. */
    private void onPostTickRetract(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (isRetractDone) {
            return;
        }
        // If retracting process just touch the miner
        if (tipDepth == 0) {
            isRetractDone = true;
            return;
        }
        // Once per N ticks (depends on tier)
        if ((aTick % (owner.getMachineSpeed() / 5)) != 0) {
            return;
        }

        // Check we can push pipe back to machine (inputs allowed for this case!)
        boolean canPush = owner.pushOutputs(MINING_PIPE_STACK, 1, true, true);
        if (!canPush) {
            return;
        }

        // Inspect target block - it should be a pipe tip, else something went wrong.
        if (!isMiningPipe(aBaseMetaTileEntity, tipDepth)) {
            return;
        }

        if (isMiningPipe(aBaseMetaTileEntity, tipDepth + 1) || isLastPipeSegment()) {
            // Return the pipe back to the machine (inputs allowed for this case!)
            owner.pushOutputs(MINING_PIPE_STACK, 1, false, true);
        }

        // Retract the pipe/tip
        int xCoord = aBaseMetaTileEntity.getXCoord();
        int yCoord = aBaseMetaTileEntity.getYCoord();
        int zCoord = aBaseMetaTileEntity.getZCoord();
        int actualDrillY = yCoord + tipDepth;
        // Move the pipe tip position
        if (actualDrillY < yCoord - 1) {
            owner.getBaseMetaTileEntity()
                .getWorld()
                .setBlock(xCoord, actualDrillY + 1, zCoord, MINING_PIPE_TIP_BLOCK);
        }
        // Remove the old pipe tip
        aBaseMetaTileEntity.getWorld()
            .setBlock(xCoord, actualDrillY, zCoord, Blocks.air, 0, /* send to client without neighbour updates */ 2);

        tipDepth++;
    }

    private boolean isLastPipeSegment() {
        return tipDepth == -1;
    }

    private boolean isMiningPipe(@NotNull IGregTechTileEntity aBaseMetaTileEntity, int yOffset) {
        Block pipeToRemove = aBaseMetaTileEntity.getBlockOffset(0, yOffset, 0);
        return pipeToRemove == MINING_PIPE_BLOCK || pipeToRemove == MINING_PIPE_TIP_BLOCK;
    }

    /** Minings the block if it is possible. */
    public void mineBlock(IGregTechTileEntity te, Block block, int x, int y, int z) {
        if (!GTUtility.eraseBlockByFakePlayer(getFakePlayer(te), x, y, z, true)) {
            return;
        }

        long seed = rng.getSeed();

        // See if we can store all of the outputs
        List<ItemStack> drops = OreManager
            .mineBlock(rng, te.getWorld(), x, y, z, false, owner.getMachineTier(), true, true);

        rng.setSeed(seed);

        for (ItemStack drop : drops) {
            if (!owner.pushOutputs(drop, drop.stackSize, true, false)) {
                return;
            }
        }

        for (ItemStack drop : drops) {
            owner.pushOutputs(drop, drop.stackSize, false, false);
        }

        // Actually mine it
        OreManager.mineBlock(rng, te.getWorld(), x, y, z, false, owner.getMachineTier(), false, true);
    }

    /**
     * Returns NEGATIVE (eg -5) depth of current drilling Y world level. RELATIVELY TO MINER ENTITY! This means '(miner
     * world Y) + depth = (actual world Y)'.
     */
    public int getTipDepth() {
        return tipDepth;
    }

    /** Looking for the lowest continuous pipe. */
    public void findTipDepth() {
        IGregTechTileEntity ownerTe = owner.getBaseMetaTileEntity();
        if (!ownerTe.isServerSide()) {
            return;
        }
        while (true) {
            Block block = ownerTe.getBlockOffset(0, tipDepth - 1, 0);
            if (block != MINING_PIPE_BLOCK && block != MINING_PIPE_TIP_BLOCK) {
                return;
            }
            tipDepth--;
        }
    }

    /**
     * Creates and provides the Fake Player for owners. todo maybe will provide player owner uuid? im sure some servers
     * not allow to fakers, in griefing reasons.
     */
    public FakePlayer getFakePlayer(IGregTechTileEntity te) {
        if (mFakePlayer == null) {
            mFakePlayer = GTUtility.getFakePlayer(te);
        }
        if (mFakePlayer != null) {
            mFakePlayer.setWorld(te.getWorld());
            mFakePlayer.setPosition(te.getXCoord(), te.getYCoord(), te.getZCoord());
        }
        return mFakePlayer;
    }

    public boolean canFakePlayerInteract(IGregTechTileEntity te, int xCoord, int yCoord, int zCoord) {
        return GTUtility
            .setBlockByFakePlayer(getFakePlayer(te), xCoord, yCoord, zCoord, MINING_PIPE_TIP_BLOCK, 0, true);
    }

    /** Can the owner continue doing its work? If we await new pipes - it cannot. */
    public boolean canContinueDrilling(long tick) {
        if (isWaitingForPipeItem) {
            if (tick % 5 != 0) {
                return false;
            }
            boolean hasPipe = owner.pullInputs(MINING_PIPE_STACK.getItem(), 1, true);
            if (hasPipe) {
                isWaitingForPipeItem = false;
            }
            return hasPipe;
        }
        return true;
    }
}
