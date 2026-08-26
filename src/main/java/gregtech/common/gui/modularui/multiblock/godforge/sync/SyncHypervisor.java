package gregtech.common.gui.modularui.multiblock.godforge.sync;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;

import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.DynamicSyncHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widget.WidgetTree;
import com.cleanroommc.modularui.widgets.DynamicSyncedWidget;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;
import tectech.thing.metaTileEntity.multi.godforge.MTEForgeOfGods;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

// spotless:off
/**
 * The hypervisor does 5 things:<br>
 * 1. Allows reusing sync values and actions from parent panels<br>
 * 2. Allows panels to check sync values of other panels, allowing panels to communicate<br>
 * 3. Allows access to any sub-panel, allowing for things like parent-relative positioning<br>
 * 4. Allows sharing panels between different GUI classes<br>
 * 5. Allows tracking who is the "main panel," and storing sub-panel data per "module," resulting
 *    in allowing for a multiblock to open the UI of another multiblock, or even multiple other
 *    multiblocks at the same time, and allowing all of their syncing and sub-paneling to still
 *    function exactly like if it were opened normally as a main panel.
 */
// spotless:on
public final class SyncHypervisor {

    private MTEForgeOfGods multiblock;
    private ForgeOfGodsData data;
    private final Map<Submodule, MTEBaseModule> modules = new HashMap<>();

    private final Modules<?> mainModule;
    private final Submodule mainSubmodule;
    private final Panels mainPanel;

    private final Table<Submodule, Panels, ModularPanel> panels = HashBasedTable.create();
    private final Table<Submodule, Panels, PanelSyncManager> syncManagers = HashBasedTable.create();

    public SyncHypervisor(Panels mainPanel) {
        this(Modules.CORE, mainPanel);
    }

    public SyncHypervisor(Modules<?> mainModule, Panels mainPanel) {
        this.mainModule = mainModule;
        this.mainSubmodule = new Submodule(mainModule, -1);
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

    public <T extends MTEBaseModule> void setModule(Modules<T> module, int moduleIndex, T moduleMultiblock) {
        modules.put(new Submodule(module, moduleIndex), moduleMultiblock);
    }

    public <T extends MTEBaseModule> T getModule(Modules<T> module, int moduleIndex) {
        if (module == Modules.ANY) {
            return modules.values()
                .stream()
                .map(module::cast)
                .findAny()
                .orElseThrow(IllegalStateException::new); // shouldn't ever happen
        }

        return module.cast(modules.get(new Submodule(module, moduleIndex)));
    }

    public Modules<?> getMainModule() {
        return mainModule;
    }

    public Panels getMainPanel() {
        return mainPanel;
    }

    public void setModularPanel(Panels panel, ModularPanel modularPanel) {
        setModularPanel(getMainModule(), -1, panel, modularPanel);
    }

    public void setModularPanel(Modules<?> module, int moduleIndex, Panels panel, ModularPanel modularPanel) {
        panels.put(new Submodule(module, moduleIndex), panel, modularPanel);
    }

    public ModularPanel getModularPanel(Panels panel) {
        return getModularPanel(getMainModule(), -1, panel);
    }

    public ModularPanel getModularPanel(Modules<?> module, int moduleIndex, Panels panel) {
        return panels.get(new Submodule(module, moduleIndex), panel);
    }

    public void setSyncManager(Panels panel, PanelSyncManager syncManager) {
        setSyncManager(getMainModule(), -1, panel, syncManager);
    }

    public void setSyncManager(Modules<?> module, int moduleIndex, Panels panel, PanelSyncManager syncManager) {
        syncManagers.put(new Submodule(module, moduleIndex), panel, syncManager);
    }

    public PanelSyncManager getSyncManager(Panels panel) {
        return getSyncManager(getMainModule(), -1, panel);
    }

    public PanelSyncManager getSyncManager(Modules<?> module, int moduleIndex, Panels panel) {
        return syncManagers.get(new Submodule(module, moduleIndex), panel);
    }

    public void onPanelDispose(Modules<?> module, int moduleIndex, Panels panel) {
        Submodule submodule = new Submodule(module, moduleIndex);
        panels.remove(submodule, panel);
        syncManagers.remove(submodule, panel);
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

    public void closeAll(Panels panel) {
        if (panel == mainPanel) throw new IllegalArgumentException("Cannot close main panel!");
        for (Modules<?> module : Modules.getAll()) {
            for (int i = 0; i < 16; i++) {
                ModularPanel cachedPanel = this.panels.get(new Submodule(module, i), panel);
                if (cachedPanel != null) {
                    cachedPanel.closeIfOpen();
                }
            }
        }
    }

    public boolean isClient() {
        return syncManagers.get(mainSubmodule, mainPanel)
            .isClient();
    }

    public EntityPlayer getPlayer() {
        return syncManagers.get(mainSubmodule, mainPanel)
            .getPlayer();
    }
}
