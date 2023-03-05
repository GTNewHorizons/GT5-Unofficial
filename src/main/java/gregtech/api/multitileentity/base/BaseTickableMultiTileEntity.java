package gregtech.api.multitileentity.base;

import static gregtech.GT_Mod.GT_FML_LOGGER;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;

import gregtech.api.multitileentity.interfaces.IMultiTileEntity.IMTE_OnNeighborBlockChange;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_Util;

public abstract class BaseTickableMultiTileEntity extends BaseMultiTileEntity implements IMTE_OnNeighborBlockChange {

    /** Variable for seeing if the Tick Function is called right now. */
    public boolean isRunningTick = false;
    /** Gets set to true when the Block received a Block Update. */
    public boolean blockUpdated = false;
    /** Timer Value */
    protected long timer = 0;
    /** Variable for updating Data to the Client */
    private boolean mSendClientData = false;

    public BaseTickableMultiTileEntity() {
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
            if (!isDead()) onPreTick(timer, isServerSide);
            if (!isDead()) {
                timer++;
                super.updateEntity();
            }
            if (!isServerSide) {
                if (needsUpdate) {
                    worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
                    // worldObj.func_147479_m(xCoord, yCoord, zCoord);
                    needsUpdate = false;
                }
            }
            if (!isDead()) onTick(timer, isServerSide);
            if (!isDead() && isServerSide && timer > 2 && mSendClientData) {
                sendClientData(null);
            }
            if (!isDead()) onPostTick(timer, isServerSide);

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
        if (mSendClientData) {
            GT_FML_LOGGER.info("Sending client data");
            super.sendClientData(aPlayer);
            mSendClientData = false;
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
    public void onPreTick(long aTick, boolean isServerSide) {
        /* Do nothing */
    }

    /** The regular Tick. */
    public void onTick(long aTimer, boolean isServerSide) {
        /* Do nothing */
    }

    /** The absolute last part of the Tick. */
    public void onPostTick(long aTick, boolean isServerSide) {
        /* Do nothing */
    }

    /** Gets called when there is an Exception happening during one of the Tick Functions. */
    public void onTickFailed(long aTimer, boolean isServerSide) {
        /* Do nothing */
    }

    @Override
    public void onNeighborBlockChange(World aWorld, Block aBlock) {
        blockUpdated = true;
    }

    @Override
    public void issueClientUpdate() {
        mSendClientData = true;
    }

    @Override
    public byte getComparatorValue(byte aSide) {
        return 0;
    }
}
