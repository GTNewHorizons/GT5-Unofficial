package gregtech.api.multitileentity.base;

import static gregtech.GT_Mod.GT_FML_LOGGER;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_OnNeighborBlockChange;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Util;

public abstract class TickableMultiTileEntity extends MultiTileEntity implements IMTE_OnNeighborBlockChange {

    /** Variable for seeing if the Tick Function is called right now. */
    public boolean isRunningTick = false;
    /** Gets set to true when the Block received a Block Update. */
    public boolean blockUpdated = false;
    /** Timer Value */
    protected long timer = 0;
    /** Variable for updating Data to the Client */
    private boolean sendClientData = false;

    public TickableMultiTileEntity() {
        super(true);
    }

    @Override
    public final void updateEntity() {
        isRunningTick = true;
        final boolean isServerSide = isServerSide();
        try {
            if (timer++ == 0) {
                markDirty();
                GT_Util.markChunkDirty(this);
                onFirstTick(isServerSide);
            }
            if (isDead()) {
                return;
            }
            onPreTick(timer, isServerSide);
            super.updateEntity();
            if (!isServerSide && needsUpdate) {
                worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                needsUpdate = false;
            }
            onTick(timer, isServerSide);
            if (isServerSide && timer > 2 && sendClientData) {
                sendClientData(null);
            }
            onPostTick(timer, isServerSide);

        } catch (Throwable e) {
            GT_FML_LOGGER.error("UpdateEntity Failed", e);
            e.printStackTrace(GT_Log.err);
            try {
                onTickFailed(timer, isServerSide);
            } catch (Throwable e2) {
                GT_FML_LOGGER.error("UpdateEntity:onTickFailed Failed", e);
            }
        }

        isRunningTick = false;
    }

    @Override
    public void sendClientData(EntityPlayerMP aPlayer) {
        if (sendClientData) {
            // GT_FML_LOGGER.info("Sending client data");
            super.sendClientData(aPlayer);
            sendClientData = false;
        }
    }

    /** The very first Tick happening to this TileEntity */
    public void onFirstTick(boolean isServerSide) {
        if (isServerSide) {
            checkDropCover();
        } else {
            requestCoverDataIfNeeded();
        }
    }

    /** The first part of the Tick. */
    public void onPreTick(long aTick, boolean isServerSide) {}

    /** The regular Tick. */
    public void onTick(long tick, boolean isServerSide) {

    }

    /** The absolute last part of the Tick. */
    public void onPostTick(long aTick, boolean isServerSide) {}

    /** Gets called when there is an Exception happening during one of the Tick Functions. */
    public void onTickFailed(long aTimer, boolean isServerSide) {}

    @Override
    public void onNeighborBlockChange(World aWorld, Block aBlock) {
        blockUpdated = true;
    }

    @Override
    public void issueClientUpdate() {
        sendClientData = true;
    }

    @Override
    public byte getComparatorValue(ForgeDirection side) {
        return 0;
    }
}
