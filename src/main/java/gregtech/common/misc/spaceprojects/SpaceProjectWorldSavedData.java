package gregtech.common.misc.spaceprojects;

import static gregtech.common.misc.spaceprojects.SpaceProjectManager.mSpaceTeamProjects;

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

    private static final String SPACE_PROJECT_MANAGER = "GT_SpaceProjectManagerNBT";

    public SpaceProjectWorldSavedData() {
        super(DATA_NAME);
    }

    @Override
    public void readFromNBT(NBTTagCompound aNBT) {
        try {
            byte[] ba = aNBT.getByteArray(SPACE_PROJECT_MANAGER);
            InputStream byteArrayInputStream = new ByteArrayInputStream(ba);
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            Object data = objectInputStream.readObject();
            mSpaceTeamProjects = (Map<UUID, Map<Pair<ISpaceBody, String>, ISpaceProject>>) data;
        } catch (IOException | ClassNotFoundException exception) {
            System.out.println(SPACE_PROJECT_MANAGER + " FAILED");
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
            aNBT.setByteArray(SPACE_PROJECT_MANAGER, data);
        } catch (IOException exception) {
            System.out.println(SPACE_PROJECT_MANAGER + " SAVE FAILED");
            exception.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            loadInstance(event.world);
        }
    }

    private static void loadInstance(World aWorld) {
        mSpaceTeamProjects.clear();
        MapStorage tStorage = aWorld.mapStorage;
        INSTANCE = (SpaceProjectWorldSavedData) tStorage.loadData(SpaceProjectWorldSavedData.class, DATA_NAME);
        if (INSTANCE == null) {
            INSTANCE = new SpaceProjectWorldSavedData();
            tStorage.setData(DATA_NAME, INSTANCE);
        }
        INSTANCE.markDirty();
    }
}
