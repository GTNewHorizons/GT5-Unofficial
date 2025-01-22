package gregtech.common.misc.teams;

import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.event.world.WorldEvent;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class TeamWorldSavedData extends WorldSavedData {

    public static TeamWorldSavedData INSTANCE;

    private static final String DATA_NAME = "GT_TeamWorldSavedData";

    private static void loadInstance(World world) {
        MapStorage storage = world.mapStorage;
        INSTANCE = (TeamWorldSavedData) storage.loadData(TeamWorldSavedData.class, DATA_NAME);
        if (INSTANCE == null) {
            INSTANCE = new TeamWorldSavedData();
            storage.setData(DATA_NAME, INSTANCE);
        }
        INSTANCE.markDirty();
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            loadInstance(event.world);
        }
    }

    public TeamWorldSavedData() {
        super(DATA_NAME);
    }

    @Override
    public void readFromNBT(NBTTagCompound NBT) {
        NBTTagList teamList = NBT.getTagList("TeamList", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < teamList.tagCount(); i++) {
            NBTTagCompound teamTag = teamList.getCompoundTagAt(i);
            String teamName = teamTag.getString("TeamName");

            GTTeam team = new GTTeam(teamName);
            team.initializeData(TeamDataTypes.values());

            // Owners
            NBTTagList ownersList = teamTag.getTagList("Owners", Constants.NBT.TAG_STRING);
            for (int j = 0; i < ownersList.tagCount(); j++) {
                team.addOwner(UUID.fromString(ownersList.getStringTagAt(j)));
            }

            // Members
            NBTTagList membersList = teamTag.getTagList("Members", Constants.NBT.TAG_STRING);
            for (int j = 0; i < membersList.tagCount(); j++) {
                team.addMember(UUID.fromString(membersList.getStringTagAt(j)));
            }

            // Team Data
            NBTTagCompound teamData = teamTag.getCompoundTag("TeamData");
            for (ITeamData data : team.getAllData()) {
                data.readFromNBT(teamData);
            }

            GTTeamManager.TEAMS.add(team);
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound NBT) {
        NBTTagList teamList = new NBTTagList();
        for (GTTeam team : GTTeamManager.TEAMS) {
            NBTTagCompound teamTag = new NBTTagCompound();
            teamTag.setString("TeamName", team.getTeamName());

            // Owners
            NBTTagList ownersList = new NBTTagList();
            for (UUID owner : team.getOwners()) {
                ownersList.appendTag(new NBTTagString(owner.toString()));
            }
            teamTag.setTag("Owners", ownersList);

            // Members
            NBTTagList membersList = new NBTTagList();
            for (UUID member : team.getMembers()) {
                membersList.appendTag(new NBTTagString(member.toString()));
            }
            teamTag.setTag("Members", membersList);

            // Team Data
            NBTTagCompound dataTag = new NBTTagCompound();
            for (ITeamData data : team.getAllData()) {
                data.writeToNBT(dataTag);
            }
            teamTag.setTag("TeamData", dataTag);

            teamList.appendTag(teamTag);
        }
        NBT.setTag("TeamList", teamList);
    }
}
