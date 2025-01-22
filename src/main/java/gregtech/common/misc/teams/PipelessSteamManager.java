package gregtech.common.misc.teams;

import java.math.BigInteger;

import net.minecraft.nbt.NBTTagCompound;

public class PipelessSteamManager implements ITeamData {

    private static final BigInteger ONE_THOUSAND = BigInteger.valueOf(1000);

    private BigInteger storedSteam = BigInteger.ZERO;
    private BigInteger storedSuperheatedSteam = BigInteger.ZERO;
    private BigInteger storedSupercriticalSteam = BigInteger.ZERO;

    @Override
    public void writeToNBT(NBTTagCompound NBT) {
        NBT.setByteArray("PipelessSteam", storedSteam.toByteArray());
        NBT.setByteArray("PipelessSuperheatedSteam", storedSuperheatedSteam.toByteArray());
        NBT.setByteArray("PipelessSupercriticalSteam", storedSupercriticalSteam.toByteArray());
    }

    @Override
    public void readFromNBT(NBTTagCompound NBT) {
        storedSteam = new BigInteger(NBT.getByteArray("PipelessSteam"));
        storedSuperheatedSteam = new BigInteger(NBT.getByteArray("PipelessSuperheatedSteam"));
        storedSupercriticalSteam = new BigInteger(NBT.getByteArray("PipelessSupercriticalSteam"));
    }

    public void setSteam(BigInteger steam) {
        this.storedSteam = steam;
        TeamWorldSavedData.INSTANCE.markDirty();
    }

    public void setSuperheatedSteam(BigInteger superheatedSteam) {
        this.storedSuperheatedSteam = superheatedSteam;
        TeamWorldSavedData.INSTANCE.markDirty();
    }

    public void setSupercriticalSteam(BigInteger supercriticalSteam) {
        this.storedSupercriticalSteam = supercriticalSteam;
        TeamWorldSavedData.INSTANCE.markDirty();
    }

    public void fillSteam(long steam) {
        fillSteam(BigInteger.valueOf(steam));
    }

    public void fillDenseSteam(long denseSteam) {
        fillSteam(
            BigInteger.valueOf(denseSteam)
                .multiply(ONE_THOUSAND));
    }

    private void fillSteam(BigInteger steam) {
        storedSteam = storedSteam.add(steam);
        TeamWorldSavedData.INSTANCE.markDirty();
    }

    public void fillSuperheatedSteam(long superheatedSteam) {
        fillSuperheatedSteam(BigInteger.valueOf(superheatedSteam));
    }

    public void fillDenseSuperheatedSteam(long denseSuperheatedSteam) {
        fillSuperheatedSteam(
            BigInteger.valueOf(denseSuperheatedSteam)
                .multiply(ONE_THOUSAND));
    }

    private void fillSuperheatedSteam(BigInteger superheatedSteam) {
        storedSuperheatedSteam = storedSuperheatedSteam.add(superheatedSteam);
        TeamWorldSavedData.INSTANCE.markDirty();
    }

    public void fillSupercriticalSteam(long supercriticalSteam) {
        fillSupercriticalSteam(BigInteger.valueOf(supercriticalSteam));
    }

    public void fillDenseSupercriticalSteam(long denseSupercriticalSteam) {
        fillSupercriticalSteam(
            BigInteger.valueOf(denseSupercriticalSteam)
                .multiply(ONE_THOUSAND));
    }

    private void fillSupercriticalSteam(BigInteger supercriticalSteam) {
        storedSupercriticalSteam = storedSupercriticalSteam.add(supercriticalSteam);
        TeamWorldSavedData.INSTANCE.markDirty();
    }

    public long drainSteam(long steam, boolean simulate) {
        return drainSteam(BigInteger.valueOf(steam), simulate).longValue();
    }

    public long drainDenseSteam(long denseSteam, boolean simulate) {
        return drainSteam(
            BigInteger.valueOf(denseSteam)
                .multiply(ONE_THOUSAND),
            simulate).divide(ONE_THOUSAND)
                .longValue();
    }

    private BigInteger drainSteam(BigInteger request, boolean simulate) {
        int cmp = storedSteam.compareTo(request);

        if (cmp > 0) {
            // we can accommodate the full request
            if (!simulate) {
                storedSteam = storedSteam.subtract(request);
                TeamWorldSavedData.INSTANCE.markDirty();
            }
            return request;
        } else if (cmp == 0) {
            // we can exactly fulfill the request
            if (!simulate) {
                storedSteam = BigInteger.ZERO;
                TeamWorldSavedData.INSTANCE.markDirty();
            }
            return request;
        } else {
            // we can only partially fulfill the request
            BigInteger oldVal = storedSteam;
            if (!simulate) {
                storedSteam = BigInteger.ZERO;
                TeamWorldSavedData.INSTANCE.markDirty();
            }
            return oldVal;
        }
    }

    public long drainSuperheatedSteam(long superheatedSteam, boolean simulate) {
        return drainSuperheatedSteam(BigInteger.valueOf(superheatedSteam), simulate).longValue();
    }

    public long drainDenseSuperheatedSteam(long denseSuperheatedSteam, boolean simulate) {
        return drainSuperheatedSteam(
            BigInteger.valueOf(denseSuperheatedSteam)
                .multiply(ONE_THOUSAND),
            simulate).divide(ONE_THOUSAND)
                .longValue();
    }

    private BigInteger drainSuperheatedSteam(BigInteger request, boolean simulate) {
        int cmp = storedSuperheatedSteam.compareTo(request);

        if (cmp > 0) {
            // we can accommodate the full request
            if (!simulate) {
                storedSuperheatedSteam = storedSuperheatedSteam.subtract(request);
                TeamWorldSavedData.INSTANCE.markDirty();
            }
            return request;
        } else if (cmp == 0) {
            // we can exactly fulfill the request
            if (!simulate) {
                storedSuperheatedSteam = BigInteger.ZERO;
                TeamWorldSavedData.INSTANCE.markDirty();
            }
            return request;
        } else {
            // we can only partially fulfill the request
            BigInteger oldVal = storedSuperheatedSteam;
            if (!simulate) {
                storedSuperheatedSteam = BigInteger.ZERO;
                TeamWorldSavedData.INSTANCE.markDirty();
            }
            return oldVal;
        }
    }

    public long drainSupercriticalSteam(long supercriticalSteam, boolean simulate) {
        return drainSupercriticalSteam(BigInteger.valueOf(supercriticalSteam), simulate).longValue();
    }

    public long drainDenseSupercriticalSteam(long denseSupercriticalSteam, boolean simulate) {
        return drainSupercriticalSteam(
            BigInteger.valueOf(denseSupercriticalSteam)
                .multiply(ONE_THOUSAND),
            simulate).divide(ONE_THOUSAND)
                .longValue();
    }

    private BigInteger drainSupercriticalSteam(BigInteger request, boolean simulate) {
        int cmp = storedSupercriticalSteam.compareTo(request);

        if (cmp > 0) {
            // we can accommodate the full request
            if (!simulate) {
                storedSupercriticalSteam = storedSupercriticalSteam.subtract(request);
                TeamWorldSavedData.INSTANCE.markDirty();
            }
            return request;
        } else if (cmp == 0) {
            // we can exactly fulfill the request
            if (!simulate) {
                storedSupercriticalSteam = BigInteger.ZERO;
                TeamWorldSavedData.INSTANCE.markDirty();
            }
            return request;
        } else {
            // we can only partially fulfill the request
            BigInteger oldVal = storedSupercriticalSteam;
            if (!simulate) {
                storedSupercriticalSteam = BigInteger.ZERO;
                TeamWorldSavedData.INSTANCE.markDirty();
            }
            return oldVal;
        }
    }
}
