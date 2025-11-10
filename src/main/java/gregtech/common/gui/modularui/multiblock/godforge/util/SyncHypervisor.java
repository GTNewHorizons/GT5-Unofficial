package gregtech.common.gui.modularui.multiblock.godforge.util;

import java.util.HashMap;
import java.util.Map;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public final class SyncHypervisor {

    private final ForgeOfGodsData data;
    private final Map<Panels, ModularPanel> panels = new HashMap<>();
    private final Map<Panels, PanelSyncManager> syncManagers = new HashMap<>();

    public SyncHypervisor(ForgeOfGodsData data) {
        this.data = data;
    }

    public ForgeOfGodsData getData() {
        return data;
    }

    public void setModularPanel(Panels panel, ModularPanel modularPanel) {
        panels.put(panel, modularPanel);
    }

    public ModularPanel getModularPanel(Panels panel) {
        return panels.get(panel);
    }

    public void setSyncManager(Panels panel, PanelSyncManager syncManager) {
        syncManagers.put(panel, syncManager);
    }

    public PanelSyncManager getSyncManager(Panels panel) {
        return syncManagers.get(panel);
    }

    public void onPanelDispose(Panels panel) {
        panels.remove(panel);
        syncManagers.remove(panel);
    }
}
