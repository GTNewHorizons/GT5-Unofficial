package gregtech.common.misc.spaceprojects;

import static gregtech.common.misc.spaceprojects.SpaceProjectManager.mSpaceTeamProjects;
import static gregtech.common.misc.spaceprojects.SpaceProjectManager.mSpaceTeams;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;

import org.apache.commons.lang3.tuple.Pair;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceProject;

public class SpaceProjectWorldSavedData extends WorldSavedData {

    public static SpaceProjectWorldSavedData INSTANCE;

    private static final String DATA_NAME = "GT_SpaceProjectData";

    private static final String SPACE_TEAM_PROJECTS = "GT_SpaceTeamProjectsNBT";

    private static final String SPACE_TEAMS = "GT_SpaceTeamsNBT";

    public SpaceProjectWorldSavedData() {
        super(DATA_NAME);
    }

    @Override
    public void readFromNBT(NBTTagCompound aNBT) {
        try {
            byte[] ba = aNBT.getByteArray(SPACE_TEAM_PROJECTS);
            InputStream byteArrayInputStream = new ByteArrayInputStream(ba);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object data = objectInputStream.readObject();
            mSpaceTeamProjects = (Map<UUID, Map<Pair<ISpaceBody, String>, ISpaceProject>>) data;
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(SPACE_TEAM_PROJECTS + " FAILED");
            exception.printStackTrace();
        }
        try {
            byte[] ba = aNBT.getByteArray(SPACE_TEAMS);
            InputStream byteArrayInputStream = new ByteArrayInputStream(ba);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object data = objectInputStream.readObject();
            mSpaceTeams = (Map<UUID, UUID>) data;
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(SPACE_TEAMS + " FAILED");
            exception.printStackTrace();
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound aNBT) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(mSpaceTeamProjects);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            aNBT.setByteArray(SPACE_TEAM_PROJECTS, data);
        } catch (IOException exception) {
            System.out.println(SPACE_TEAM_PROJECTS + " SAVE FAILED");
            exception.printStackTrace();
        }
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            objectOutputStream.writeObject(mSpaceTeams);
            objectOutputStream.flush();
            byte[] data = byteArrayOutputStream.toByteArray();
            aNBT.setByteArray(SPACE_TEAMS, data);
        } catch (IOException exception) {
            System.out.println(SPACE_TEAMS + " SAVE FAILED");
            exception.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load aEvent) {
        if (!aEvent.world.isRemote && aEvent.world.provider.dimensionId == 0) {
            loadInstance(aEvent.world);
        }
    }

    private static void loadInstance(World aWorld) {
        mSpaceTeamProjects.clear();
        mSpaceTeams.clear();
        MapStorage tStorage = aWorld.mapStorage;
        INSTANCE = (SpaceProjectWorldSavedData) tStorage.loadData(SpaceProjectWorldSavedData.class, DATA_NAME);
        if (INSTANCE == null) {
            INSTANCE = new SpaceProjectWorldSavedData();
            tStorage.setData(DATA_NAME, INSTANCE);
        }
        INSTANCE.markDirty();
    }
}
