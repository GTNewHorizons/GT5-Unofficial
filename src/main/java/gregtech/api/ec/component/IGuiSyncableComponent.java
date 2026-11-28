package gregtech.api.ec.component;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IGuiSyncableComponent extends IComponent {

    ComponentKey.ListKey<IGuiSyncableComponent> KEY = ComponentKey.list();

    void registerGuiSyncValues(@NotNull PanelSyncManager syncManager);
}
