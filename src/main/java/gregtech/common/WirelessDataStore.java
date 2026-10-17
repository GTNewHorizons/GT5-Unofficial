package gregtech.common;

import static gregtech.common.misc.GlobalVariableStorage.GlobalWirelessDataSticks;

import java.util.ArrayList;
import java.util.Collections;
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
    private List<RecipeAssemblyLine> dataSticks = Collections.emptyList();

    public void uploadData(List<RecipeAssemblyLine> recipes, long tick) {
        if (lastUploadTick < tick) {
            uploadedSticks.clear();
            lastUploadTick = tick;
        }
        uploadedSticks.addAll(recipes);
    }

    public List<RecipeAssemblyLine> downloadData(long tick) {
        if (lastDownloadTick < tick) {
            // Receivers retain the previous download to detect changes. Publish a new snapshot so the first
            // receiver's download cannot overwrite the previous recipes of every other receiver.
            // Empty databanks skip uploads. Expire the previous cycle if no bank refreshes it, including when
            // every transmitter is unloaded, so removed sticks cannot remain available indefinitely.
            dataSticks = tick - lastUploadTick < IO_TICK_RATE
                ? Collections.unmodifiableList(new ArrayList<>(uploadedSticks))
                : Collections.emptyList();
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
