package gregtech.common.misc;

import java.math.BigInteger;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizon.gtnhlib.teams.ITeamData;
import com.gtnewhorizon.gtnhlib.teams.Team;
import com.gtnewhorizon.gtnhlib.teams.TeamDataTransferReason;

public class WirelessEnergyTeamData implements ITeamData {

    public static final String DATA_KEY = "wirelessEnergy";
    public static final String NBT_KEY = "value";

    BigInteger wirelessEnergy = BigInteger.ZERO;

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setString(NBT_KEY, wirelessEnergy.toString());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        wirelessEnergy = new BigInteger(tag.getString(NBT_KEY));
    }

    @Override
    public void mergeData(Team consumed, Team surviving, ITeamData oldTeamData) {
        // do nothing for now
    }

    @Override
    public void transferData(Team prevTeam, Team newTeam, UUID playerId, ITeamData prevTeamData,
        TeamDataTransferReason reason) {
        // do nothing for now
    }
}
