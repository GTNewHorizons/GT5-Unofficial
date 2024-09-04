package gregtech.api.graphs.paths;

import net.minecraft.server.MinecraftServer;

import gregtech.api.enums.TickTime;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.util.AveragePerTickCounter;

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

    private AveragePerTickCounter avgAmperageCounter = new AveragePerTickCounter(TickTime.SECOND);
    private AveragePerTickCounter avgVoltageCounter = new AveragePerTickCounter(TickTime.SECOND);

    public PowerNodePath(MetaPipeEntity[] aCables) {
        super(aCables);
    }

    public long getLoss() {
        return mLoss;
    }

    public void applyVoltage(long aVoltage, boolean aCountUp) {

        avgVoltageCounter.addValue(Math.max(aVoltage - mLoss, 0));

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
                if (((MTECable) tCable).mVoltage < this.mVoltage) {
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

        avgAmperageCounter.addValue(aAmps);

        this.mAmps += aAmps;
        if (this.mAmps > mMaxAmps * 40) {
            lock.addTileEntity(null);
            for (MetaPipeEntity tCable : mPipes) {
                if (((MTECable) tCable).mAmperage * 40 < this.mAmps) {
                    BaseMetaPipeEntity tBaseCable = (BaseMetaPipeEntity) tCable.getBaseMetaTileEntity();
                    if (tBaseCable != null) {
                        tBaseCable.setToFire();
                    }
                }
            }
        }
    }

    public long getAmperage() {
        return avgAmperageCounter.getLast();
    }

    public double getAvgAmperage() {
        return avgAmperageCounter.getAverage();
    }

    public long getVoltage() {
        return avgVoltageCounter.getLast();
    }

    public double getAvgVoltage() {
        return avgVoltageCounter.getAverage();
    }

    @Override
    protected void processPipes() {
        super.processPipes();
        mMaxAmps = Integer.MAX_VALUE;
        mMaxVoltage = Integer.MAX_VALUE;
        for (MetaPipeEntity tCable : mPipes) {
            if (tCable instanceof MTECable) {
                mMaxAmps = Math.min(((MTECable) tCable).mAmperage, mMaxAmps);
                mLoss += ((MTECable) tCable).mCableLossPerMeter;
                mMaxVoltage = Math.min(((MTECable) tCable).mVoltage, mMaxVoltage);
            }
        }
    }
}
