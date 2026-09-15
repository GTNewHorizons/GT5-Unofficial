package gregtech.common;

import static gregtech.common.misc.GlobalVariableStorage.GlobalWirelessDataSticks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import gregtech.common.misc.spaceprojects.SpaceProjectManager;

public class WirelessDataStore {

    public static final long IO_TICK_RATE = 200;
    public static final long DOWNLOAD_TICK_OFFSET = 1;

    private long lastUploadTick = -1;
    private long lastDownloadTick = -1;
    private final ArrayList<RecipeAssemblyLine> uploadedSticks = new ArrayList<>();
    private final ArrayList<RecipeAssemblyLine> dataSticks = new ArrayList<>();

    public void uploadData(List<RecipeAssemblyLine> recipes, long tick) {
        if (lastUploadTick < tick) {
            uploadedSticks.clear();
            lastUploadTick = tick;
        }
        uploadedSticks.addAll(recipes);
    }

    public List<RecipeAssemblyLine> downloadData(long tick) {
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
