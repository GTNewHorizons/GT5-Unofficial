package gregtech.common.gui.modularui.multiblock.godforge.util;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;

import gregtech.common.gui.modularui.multiblock.godforge.data.Modules;
import gregtech.common.gui.modularui.multiblock.godforge.data.Panels;
import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEForgeOfGods;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

/**
 * Solution to re-use sync handlers across multiple panels. If 10 panels are open, and
 * a lower-down panel requires a sync value that a higher-up panel needs, why should
 * this be re-synced? The hypervisor allows for accessing sync values and actions between
 * panels, as well as allowing access for any panel's ModularPanel.
 */
public final class SyncHypervisor {

    private MTEForgeOfGods multiblock;
    private ForgeOfGodsData data;

    private final Panels mainPanel;
    private final Map<Panels, ModularPanel> panels = new HashMap<>();
    private final Map<Panels, PanelSyncManager> syncManagers = new HashMap<>();
    private final Map<Modules<?>, MTEBaseModule> modules = new HashMap<>();

    public SyncHypervisor(Panels mainPanel) {
        this.mainPanel = mainPanel;
    }

    public void setMultiblock(MTEForgeOfGods multiblock) {
        this.multiblock = multiblock;
        this.data = multiblock != null ? multiblock.getData() : null;
    }

    public MTEForgeOfGods getMultiblock() {
        return multiblock;
    }

    public ForgeOfGodsData getData() {
        return data;
    }

    public <T extends MTEBaseModule> void setModule(Modules<T> module, T moduleMultiblock) {
        modules.put(module, moduleMultiblock);
    }

    public <T extends MTEBaseModule> T getModule(Modules<T> module) {
        if (module == Modules.BASE) {
            return modules.values()
                .stream()
                .map(module::cast)
                .findAny()
                .orElseThrow(IllegalStateException::new); // shouldn't ever happen
        }

        return module.cast(modules.get(module));
    }

    public Panels getMainPanel() {
        return mainPanel;
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

    public void refreshDynamicWidget(Panels panel) {
        ModularPanel modularPanel = getModularPanel(panel);
        if (modularPanel == null) return;
        DynamicSyncedWidget<?> dynamic = WidgetTree.findFirst(modularPanel, DynamicSyncedWidget.class, $ -> true);
        if (dynamic == null) return;
        DynamicSyncHandler handler = (DynamicSyncHandler) dynamic.getSyncHandler();
        if (!handler.isValid()) return;
        handler.notifyUpdate($ -> {});
    }

    public boolean isClient() {
        return syncManagers.get(mainPanel)
            .isClient();
    }

    public EntityPlayer getPlayer() {
        return syncManagers.get(mainPanel)
            .getPlayer();
    }
}
