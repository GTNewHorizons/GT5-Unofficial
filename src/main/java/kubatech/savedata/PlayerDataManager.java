package kubatech.savedata;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.event.world.WorldEvent;

public class PlayerDataManager extends WorldSavedData {

    private static final String playerDataName = "KubaTech_PlayerData";
    static PlayerDataManager Instance = null;
    private final HashMap<String, PlayerData> players = new HashMap<>();

    public static void Initialize(World world) {
        if (Instance != null) {
            Instance.players.clear();
        }
        Instance = (PlayerDataManager) world.mapStorage.loadData(PlayerDataManager.class, playerDataName);
        if (Instance == null) {
            Instance = new PlayerDataManager();
            world.mapStorage.setData(playerDataName, Instance);
        }
        Instance.markDirty();
    }

    @SuppressWarnings("unused")
    public PlayerDataManager(String p_i2141_1_) {
        super(p_i2141_1_);
    }

    public PlayerDataManager() {
        super(playerDataName);
    }

    @Override
    public void readFromNBT(NBTTagCompound NBTData) {
        if (!NBTData.hasKey("size")) return;
        players.clear();
        for (int i = 0, imax = NBTData.getInteger("size"); i < imax; i++) {
            NBTTagCompound playerNBTData = NBTData.getCompoundTag("Player." + i);
            players.put(playerNBTData.getString("username"), new PlayerData(playerNBTData.getCompoundTag("data")));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound NBTData) {
        NBTData.setInteger("size", players.size());
        int i = 0;
        for (Map.Entry<String, PlayerData> playerDataEntry : players.entrySet()) {
            NBTTagCompound playerNBTData = new NBTTagCompound();
            playerNBTData.setString("username", playerDataEntry.getKey());
            playerNBTData.setTag("data", playerDataEntry.getValue().toNBTData());
            NBTData.setTag("Player." + (i++), playerNBTData);
        }
    }

    public static PlayerData getPlayer(String username) {
        if (Instance == null) return null;
        return Instance.players.computeIfAbsent(username, s -> new PlayerData());
    }

    @SuppressWarnings("unused")
    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        if (event.world.isRemote || event.world.provider.dimensionId != 0) return;
        Initialize(event.world);
    }
}
