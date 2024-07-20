package gregtech.common;

import static gregtech.common.misc.GlobalVariableStorage.GlobalWirelessDataSticks;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.item.ItemStack;

import gregtech.common.misc.spaceprojects.SpaceProjectManager;

public class WirelessDataStore {

    private final ArrayList<ItemStack> dataSticks = new ArrayList<>();

    public void clearData() {
        dataSticks.clear();
    }

    public void uploadData(List<ItemStack> sticks) {
        dataSticks.addAll(sticks);
    }

    public List<ItemStack> downloadData() {
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
