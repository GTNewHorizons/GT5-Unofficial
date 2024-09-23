package gregtech.common;

import static gregtech.common.misc.GlobalVariableStorage.GlobalWirelessDataSticks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.item.ItemStack;

import gregtech.common.misc.spaceprojects.SpaceProjectManager;

public class WirelessDataStore {

    public static final long UPLOAD_TICK = 200;
    public static final long DOWNLOAD_TICK = UPLOAD_TICK + 1;

    private long lastUploadTick = -1;
    private long lastDownloadTick = -1;
    private final ArrayList<ItemStack> uploadedSticks = new ArrayList<>();
    private final ArrayList<ItemStack> dataSticks = new ArrayList<>();

    public void uploadData(List<ItemStack> sticks, long tick) {
        if (lastUploadTick < tick) {
            uploadedSticks.clear();
            lastUploadTick = tick;
        }
        uploadedSticks.addAll(sticks);
    }

    public List<ItemStack> downloadData(long tick) {
        if (lastDownloadTick < tick) {
            dataSticks.clear();
            dataSticks.addAll(uploadedSticks);
            lastDownloadTick = tick;
        }
        return dataSticks;
    }

    public static WirelessDataStore getWirelessDataSticks(UUID uuid) {
        UUID team = SpaceProjectManager.getLeader(uuid);
        if (GlobalWirelessDataSticks.get(team) == null) {
            GlobalWirelessDataSticks.put(team, new WirelessDataStore());
        }
        return GlobalWirelessDataSticks.get(team);
    }
}
