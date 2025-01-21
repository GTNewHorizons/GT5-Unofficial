package gregtech.common.misc.teams;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectLists;

public class GTTeam {

    private String teamName;
    private final ObjectList<UUID> owners = new ObjectArrayList<>();
    private final ObjectList<UUID> members = new ObjectArrayList<>();
    private final Map<TeamDataTypes, ITeamData> teamData = new HashMap<>();

    public GTTeam(String teamName) {
        this.teamName = teamName;
        GTTeamManager.TEAMS.add(this);
    }

    public String getTeamName() {
        return teamName;
    }

    public boolean renameTeam(String newName) {
        if (GTTeamManager.isTeamNameValid(newName)) {
            this.teamName = newName;
            return true;
        }
        return false;
    }

    public boolean isTeamMember(EntityPlayer player) {
        return members.contains(player.getUniqueID());
    }

    public void addMember(EntityPlayer player) {
        addMember(player.getUniqueID());
    }

    public void addMember(UUID uuid) {
        members.add(uuid);
    }

    public List<UUID> getMembers() {
        return ObjectLists.unmodifiable(members);
    }

    public boolean isTeamOwner(EntityPlayer player) {
        return owners.contains(player.getUniqueID());
    }

    public void addOwner(EntityPlayer player) {
        addOwner(player.getUniqueID());
    }

    public void addOwner(UUID uuid) {
        owners.add(uuid);
        // owners are also always members
        members.add(uuid);
    }

    public List<UUID> getOwners() {
        return ObjectLists.unmodifiable(owners);
    }

    public void initializeData(TeamDataTypes... types) {
        for (TeamDataTypes type : types) {
            if (!teamData.containsKey(type)) {
                teamData.put(type, type.construct());
            }
        }
    }

    public ITeamData getData(TeamDataTypes type) {
        return teamData.get(type);
    }

    public List<ITeamData> getAllData() {
        return new ArrayList<>(teamData.values());
    }
}
