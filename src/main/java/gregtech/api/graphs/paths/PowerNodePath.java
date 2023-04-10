package gregtech.api.graphs.paths;

import net.minecraft.server.MinecraftServer;

import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Cable;

// path for cables
// all calculations like amp and voltage happens here
public class PowerNodePath extends NodePath {

    long mMaxAmps;
    long mAmps = 0;
    long mLoss;
    long mVoltage = 0;
    long mMaxVoltage;
    int mTick = 0;
    boolean mCountUp = true;

    public PowerNodePath(MetaPipeEntity[] aCables) {
        super(aCables);
    }

    public long getLoss() {
        return mLoss;
    }

    public void applyVoltage(long aVoltage, boolean aCountUp) {
        int tNewTime = MinecraftServer.getServer()
            .getTickCounter();
        if (mTick != tNewTime) {
            reset(tNewTime - mTick);
            mTick = tNewTime;
            this.mVoltage = aVoltage;
            this.mCountUp = aCountUp;
        } else if (this.mCountUp != aCountUp && (aVoltage - mLoss) > this.mVoltage || aVoltage > this.mVoltage) {
            this.mCountUp = aCountUp;
            this.mVoltage = aVoltage;
        }
        if (aVoltage > mMaxVoltage) {
            lock.addTileEntity(null);
            for (MetaPipeEntity tCable : mPipes) {
                if (((GT_MetaPipeEntity_Cable) tCable).mVoltage < this.mVoltage) {
                    BaseMetaPipeEntity tBaseCable = (BaseMetaPipeEntity) tCable.getBaseMetaTileEntity();
                    if (tBaseCable != null) {
                        tBaseCable.setToFire();
                    }
                }
            }
        }
    }

    private void reset(int aTimePassed) {
        if (aTimePassed < 0 || aTimePassed > 100) {
            mAmps = 0;
            return;
        }
        mAmps = Math.max(0, mAmps - (mMaxAmps * aTimePassed));
    }

    public void addAmps(long aAmps) {
        this.mAmps += aAmps;
        if (this.mAmps > mMaxAmps * 40) {
            lock.addTileEntity(null);
            for (MetaPipeEntity tCable : mPipes) {
                if (((GT_MetaPipeEntity_Cable) tCable).mAmperage * 40 < this.mAmps) {
                    BaseMetaPipeEntity tBaseCable = (BaseMetaPipeEntity) tCable.getBaseMetaTileEntity();
                    if (tBaseCable != null) {
                        tBaseCable.setToFire();
                    }
                }
            }
        }
    }

    // if no amps pass through for more than 0.5 second reduce them to minimize wrong results
    // but still allow the player to see if activity is happening
    public long getAmps() {
        int tTime = MinecraftServer.getServer()
            .getTickCounter() - 10;
        if (mTick < tTime) {
            reset(tTime - mTick);
            mTick = tTime;
        }
        return mAmps;
    }

    public long getVoltage(MetaPipeEntity aCable) {
        int tLoss = 0;
        if (mCountUp) {
            for (MetaPipeEntity mPipe : mPipes) {
                GT_MetaPipeEntity_Cable tCable = (GT_MetaPipeEntity_Cable) mPipe;
                tLoss += tCable.mCableLossPerMeter;
                if (aCable == tCable) {
                    return Math.max(mVoltage - tLoss, 0);
                }
            }
        } else {
            for (int i = mPipes.length - 1; i >= 0; i--) {
                GT_MetaPipeEntity_Cable tCable = (GT_MetaPipeEntity_Cable) mPipes[i];
                tLoss += tCable.mCableLossPerMeter;
                if (aCable == tCable) {
                    return Math.max(mVoltage - tLoss, 0);
                }
            }
        }
        return -1;
    }

    @Override
    protected void processPipes() {
        super.processPipes();
        mMaxAmps = Integer.MAX_VALUE;
        mMaxVoltage = Integer.MAX_VALUE;
        for (MetaPipeEntity tCable : mPipes) {
            if (tCable instanceof GT_MetaPipeEntity_Cable) {
                mMaxAmps = Math.min(((GT_MetaPipeEntity_Cable) tCable).mAmperage, mMaxAmps);
                mLoss += ((GT_MetaPipeEntity_Cable) tCable).mCableLossPerMeter;
                mMaxVoltage = Math.min(((GT_MetaPipeEntity_Cable) tCable).mVoltage, mMaxVoltage);
            }
        }
    }
}
