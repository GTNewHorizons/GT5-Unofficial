package gregtech.common.misc.teams;

import java.math.BigInteger;

import net.minecraft.nbt.NBTTagCompound;

public class PipelessSteamManager implements ITeamData {

    private BigInteger storedSteam = BigInteger.ZERO;

    @Override
    public void writeToNBT(NBTTagCompound NBT) {
        NBT.setByteArray("PipelessSteam", storedSteam.toByteArray());
    }

    @Override
    public void readFromNBT(NBTTagCompound NBT) {
        storedSteam = new BigInteger(NBT.getByteArray("PipelessSteam"));
    }

    public void fillPipelessNet(long steam) {
        storedSteam = storedSteam.add(BigInteger.valueOf(steam));
        TeamWorldSavedData.INSTANCE.markDirty();
    }

    public long drainPipelessNet(long steam, boolean simulate) {
        BigInteger request = BigInteger.valueOf(steam);

        int cmp = storedSteam.compareTo(request);

        if (cmp > 0) {
            // we can accommodate the full request
            if (!simulate) {
                storedSteam = storedSteam.subtract(request);
                TeamWorldSavedData.INSTANCE.markDirty();
            }
            return steam;
        } else if (cmp == 0) {
            // we can exactly fulfill the request
            if (!simulate) {
                storedSteam = BigInteger.ZERO;
                TeamWorldSavedData.INSTANCE.markDirty();
            }
            return steam;
        } else {
            // we can only partially fulfill the request
            BigInteger oldVal = storedSteam;
            if (!simulate) {
                storedSteam = BigInteger.ZERO;
                TeamWorldSavedData.INSTANCE.markDirty();
            }
            return oldVal.longValue();
        }
    }
}
