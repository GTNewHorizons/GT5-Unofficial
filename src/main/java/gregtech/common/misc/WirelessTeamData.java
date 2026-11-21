package gregtech.common.misc;

import java.math.BigInteger;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

import com.gtnewhorizon.gtnhlib.teams.ITeamData;
import com.gtnewhorizon.gtnhlib.teams.Team;
import com.gtnewhorizon.gtnhlib.teams.TeamDataTransferReason;

public class WirelessTeamData implements ITeamData {

    public static final String DATA_KEY = "wirelessNetwork";
    public static final String ENERGY_TAG = "energy";

    BigInteger wirelessEnergy = BigInteger.ZERO;

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        tag.setString(ENERGY_TAG, wirelessEnergy.toString());
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        if (tag.hasKey(ENERGY_TAG, Constants.NBT.TAG_STRING)) {
            wirelessEnergy = new BigInteger(tag.getString(ENERGY_TAG));
        }
    }

    @Override
    public void mergeData(Team consumed, Team surviving, ITeamData oldTeamData) {
        if (!(oldTeamData instanceof WirelessTeamData oldWirelessTeamData)) return;

        // TODO: decide if energy should be merged or not
        this.wirelessEnergy = this.wirelessEnergy.add(oldWirelessTeamData.wirelessEnergy);
    }

    @Override
    public void transferData(Team prevTeam, Team newTeam, UUID playerId, ITeamData prevTeamData,
        TeamDataTransferReason reason) {
        // needed for further migration of the wireless systems
        if (!(prevTeamData instanceof WirelessTeamData prevWirelessTeamData)) return;
    }
}
