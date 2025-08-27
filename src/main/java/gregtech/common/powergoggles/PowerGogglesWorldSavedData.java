package gregtech.common.powergoggles;

import java.util.Map;
import java.util.UUID;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;
import net.minecraftforge.event.world.WorldEvent;

import appeng.api.util.DimensionalCoord;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.common.powergoggles.handlers.PowerGogglesEventHandler;

public class PowerGogglesWorldSavedData extends WorldSavedData {

    public static PowerGogglesWorldSavedData INSTANCE;
    private static final String DATA_NAME = "GregTech_PowerGogglesClientData";

    private static void loadInstance(World world) {

        MapStorage storage = world.mapStorage;
        INSTANCE = (PowerGogglesWorldSavedData) storage.loadData(PowerGogglesWorldSavedData.class, DATA_NAME);
        if (INSTANCE == null) {
            INSTANCE = new PowerGogglesWorldSavedData();
            storage.setData(DATA_NAME, INSTANCE);
        }
        INSTANCE.markDirty();
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (!event.world.isRemote && event.world.provider.dimensionId == 0) {
            loadInstance(event.world);
        }
    }

    public PowerGogglesWorldSavedData() {
        super(DATA_NAME);
    }

    @SuppressWarnings("unused")
    public PowerGogglesWorldSavedData(String name) {
        super(name);
    }

    @Override
    @SuppressWarnings("unchecked")
    public void writeToNBT(NBTTagCompound nbtTagCompound) {
        NBTTagList clientList = new NBTTagList();
        for (Map.Entry<UUID, PowerGogglesClient> client : PowerGogglesEventHandler.getInstance()
            .getClients()
            .entrySet()) {
            clientList.tagList.add(
                client.getValue()
                    .getNBT(client.getKey()));
        }
        nbtTagCompound.setTag("clientList", clientList);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound) {
        int nbtTagCompoundType = 10;
        NBTTagList clientList = nbtTagCompound.getTagList("clientList", nbtTagCompoundType);

        processClientList(clientList);

    }

    private void processClientList(NBTTagList clientList) {
        Map<UUID, PowerGogglesClient> clientMap = PowerGogglesEventHandler.getInstance()
            .getClients();

        for (Object tag : clientList.tagList) {

            NBTTagCompound tagCompound = (NBTTagCompound) tag;

            UUID uuid = UUID.fromString(tagCompound.getString("uuid"));
            DimensionalCoord coords = !tagCompound.hasKey("x") ? null
                : new DimensionalCoord(
                    tagCompound.getInteger("x"),
                    tagCompound.getInteger("y"),
                    tagCompound.getInteger("z"),
                    tagCompound.getInteger("dim"));

            PowerGogglesClient client = new PowerGogglesClient(coords);
            clientMap.put(uuid, client);

        }
    }

}
