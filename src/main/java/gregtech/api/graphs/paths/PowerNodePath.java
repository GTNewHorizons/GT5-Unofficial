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

    // voltage of the packet currently being pushed through this path, after loss.
    // applyVoltage() is always called on a path right before addAmps() (see PowerNodes#processNextNode and
    // #processNodeInject), so this is what the amps recorded next are travelling at.
    private long mCurrentVoltageAfterLoss = 0;

    private final AveragePerTickCounter avgAmperageCounter = new AveragePerTickCounter(TickTime.SECOND);
    private final AveragePerTickCounter avgVoltageCounter = new AveragePerTickCounter(TickTime.SECOND);
    private final AveragePerTickCounter avgEnergyCounter = new AveragePerTickCounter(TickTime.SECOND);

    public PowerNodePath(MetaPipeEntity[] aCables) {
        super(aCables);
    }

    public long getLoss() {
        return mLoss;
    }

    public void applyVoltage(long aVoltage, boolean aCountUp) {

        mCurrentVoltageAfterLoss = Math.max(aVoltage - mLoss, 0);
        // a voltage does not add up when several packets go through in the same tick, unlike amps
        avgVoltageCounter.addMaxValue(mCurrentVoltageAfterLoss);

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
        avgEnergyCounter.addValue(aAmps * mCurrentVoltageAfterLoss);

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

    public long getMaxAmperage() {
        return mMaxAmps;
    }

    public double getAvgAmperage() {
        return avgAmperageCounter.getAverage();
    }

    /**
     * @return the highest voltage that went through on the previous tick, after the loss of the whole path segment
     */
    public long getVoltage() {
        return avgVoltageCounter.getLast();
    }

    public long getMaxVoltage() {
        return mMaxVoltage;
    }

    public double getAvgVoltage() {
        return avgVoltageCounter.getAverage();
    }

    /**
     * @return the energy that went through on the previous tick, in EU/t
     */
    public long getEnergy() {
        return avgEnergyCounter.getLast();
    }

    /**
     * @return the energy that went through, averaged over the last 20 ticks, in EU/t
     */
    public double getAvgEnergy() {
        return avgEnergyCounter.getAverage();
    }

    /**
     * @return a consistent snapshot of what this path carries, for the Portable Scanner and Waila. Performs no
     *         mutation, so it is safe to call on every Waila poll.
     */
    public CableReadout getReadout() {
        final long maxVoltageAfterLoss = Math.max(0, mMaxVoltage - mLoss);
        return new CableReadout(
            getAmperage(),
            mMaxAmps,
            getVoltage(),
            maxVoltageAfterLoss,
            getEnergy(),
            maxVoltageAfterLoss * mMaxAmps,
            getAvgAmperage(),
            getAvgEnergy());
    }

    @Override
    protected void processPipes() {
        super.processPipes();
        mMaxAmps = Integer.MAX_VALUE;
        mMaxVoltage = Integer.MAX_VALUE;
        mLoss = 0;
        for (MetaPipeEntity tCable : mPipes) {
            if (tCable instanceof MTECable) {
                mMaxAmps = Math.min(((MTECable) tCable).mAmperage, mMaxAmps);
                mLoss += ((MTECable) tCable).mCableLossPerMeter;
                mMaxVoltage = Math.min(((MTECable) tCable).mVoltage, mMaxVoltage);
            }
        }
    }
}
