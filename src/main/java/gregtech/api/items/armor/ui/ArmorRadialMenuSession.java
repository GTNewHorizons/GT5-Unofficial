package gregtech.api.items.armor.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.factory.GuiData;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.StringSyncValue;

import gregtech.api.items.armor.ArmorAction;
import gregtech.api.items.armor.ArmorActionManager;
import gregtech.api.items.armor.ArmorContext;
import gregtech.api.items.armor.ArmorState;
import gregtech.api.items.armor.behaviors.BehaviorName;
import gregtech.api.items.armor.behaviors.IArmorBehavior;
import gregtech.common.items.armor.MechArmorBase;

public class ArmorRadialMenuSession {

    private static final int ARMOR_SLOTS_COUNT = 4;

    private final EntityPlayer player;
    private final PanelSyncManager syncManager;
    private final NBTTagCompound nbt;

    private final Set<String> activeSet = new HashSet<>();
    private final List<String> currentOrder = new ArrayList<>();

    private final StringSyncValue actionTriggerSync;
    private StringSyncValue syncedOrder;
    private StringSyncValue syncedActive;

    private Runnable onMenuUpdateCallback;

    public ArmorRadialMenuSession(GuiData data, PanelSyncManager syncManager, UISettings settings) {
        this.player = data.getPlayer();
        this.syncManager = syncManager;
        this.nbt = getSettingsNBT(player);

        this.actionTriggerSync = createActionSync();

        Set<BehaviorName> activeBehaviors = getActiveArmorBehaviors();
        List<String> validActionIds = filterValidActions(activeBehaviors);
        initSyncAndCollections(validActionIds);
    }

    public PanelSyncManager getSyncManager() {
        return syncManager;
    }

    public List<String> getCurrentOrder() {
        return currentOrder;
    }

    public StringSyncValue getActionTriggerSync() {
        return actionTriggerSync;
    }

    public boolean isActive(String actionId) {
        return activeSet.contains(actionId);
    }

    public void toggleActiveState(String actionId, boolean isActive) {
        if (isActive) {
            this.activeSet.add(actionId);
        } else {
            this.activeSet.remove(actionId);
        }
        this.syncedActive.setStringValue(String.join(",", this.activeSet) + ",");
        triggerUiUpdate();
    }

    public void updateOrder(List<String> newOrder) {
        this.currentOrder.clear();
        this.currentOrder.addAll(newOrder);
        this.syncedOrder.setStringValue(String.join(",", newOrder) + ",");
        triggerUiUpdate();
    }

    public void setUiUpdateCallback(Runnable callback) {
        this.onMenuUpdateCallback = callback;
    }

    private void triggerUiUpdate() {
        if (onMenuUpdateCallback != null) {
            onMenuUpdateCallback.run();
        }
    }

    private void executeArmorAction(String clickedActionId) {
        if (clickedActionId == null || clickedActionId.isEmpty()) return;

        ArmorAction action = ArmorActionManager.getAction(clickedActionId);
        if (action == null) return;

        for (int i = 0; i < ARMOR_SLOTS_COUNT; i++) {
            ItemStack armorPiece = player.getCurrentArmor(i);

            if (armorPiece == null) continue;
            if (!(armorPiece.getItem() instanceof MechArmorBase)) continue;

            ArmorState state = new ArmorState();
            ArmorContext.ArmorContextImpl context = new ArmorContext.ArmorContextImpl(player, armorPiece, state);
            ArmorState.load(context);

            boolean stateChanged = false;

            if (action.isToggle() && action.getBehaviorName() != null) {
                if (context.hasBehavior(action.getBehaviorName())) {
                    context.toggleBehavior(action.getBehaviorName());
                    stateChanged = true;
                }
            } else if (!action.isToggle() && action.getKeybind() != null) {
                for (IArmorBehavior behavior : state.behaviors.values()) {
                    if (behavior.getListenedKeys(context)
                        .contains(action.getKeybind())) {
                        behavior.onKeyPressed(context, action.getKeybind(), true);
                        stateChanged = true;
                    }
                }
            }

            if (stateChanged || context.isDirty()) {
                context.save();
            }
        }
    }

    private StringSyncValue createActionSync() {
        StringSyncValue sync = new StringSyncValue(() -> "", this::executeArmorAction).allowC2S();
        syncManager.syncValue("actionTrigger", sync);
        return sync;
    }

    private NBTTagCompound getSettingsNBT(EntityPlayer player) {
        NBTTagCompound forgeData = player.getEntityData();
        if (!forgeData.hasKey("PlayerPersisted")) {
            forgeData.setTag("PlayerPersisted", new NBTTagCompound());
        }
        NBTTagCompound persisted = forgeData.getCompoundTag("PlayerPersisted");
        if (!persisted.hasKey("ArmorRadialSettings")) {
            persisted.setTag("ArmorRadialSettings", new NBTTagCompound());
        }
        return persisted.getCompoundTag("ArmorRadialSettings");
    }

    private Set<BehaviorName> getActiveArmorBehaviors() {
        Set<BehaviorName> activeBehaviors = new HashSet<>();
        for (int i = 0; i < ARMOR_SLOTS_COUNT; i++) {
            ItemStack armorPiece = this.player.getCurrentArmor(i);
            if (armorPiece != null) {
                activeBehaviors.addAll(ArmorState.load(armorPiece).behaviors.keySet());
            }
        }
        return activeBehaviors;
    }

    private List<String> filterValidActions(Set<BehaviorName> activeBehaviors) {
        List<String> validActionIds = new ArrayList<>();
        for (ArmorAction action : ArmorActionManager.getAllActions()) {
            if (action.getBehaviorName() == null || activeBehaviors.contains(action.getBehaviorName())) {
                validActionIds.add(action.getId());
            }
        }
        return validActionIds;
    }

    private void initSyncAndCollections(List<String> validActionIds) {
        String defaultString = String.join(",", validActionIds) + ",";

        this.syncedOrder = new StringSyncValue(
            () -> this.nbt.hasKey("RadialOrder") ? this.nbt.getString("RadialOrder") : defaultString,
            (newOrder) -> this.nbt.setString("RadialOrder", newOrder)).allowC2S();

        this.syncedActive = new StringSyncValue(
            () -> this.nbt.hasKey("RadialActive") ? this.nbt.getString("RadialActive") : defaultString,
            (newActive) -> this.nbt.setString("RadialActive", newActive)).allowC2S();

        this.syncManager.syncValue("orderSync", this.syncedOrder);
        this.syncManager.syncValue("activeSync", this.syncedActive);

        parseCurrentOrder(validActionIds);
        parseActiveSet();
    }

    private void parseCurrentOrder(List<String> validActionIds) {
        this.currentOrder.clear();
        for (String s : this.syncedOrder.getStringValue()
            .split(",")) {
            if (!s.isEmpty() && validActionIds.contains(s)) {
                this.currentOrder.add(s);
            }
        }
        for (String action : validActionIds) {
            if (!this.currentOrder.contains(action)) {
                this.currentOrder.add(action);
            }
        }
    }

    private void parseActiveSet() {
        this.activeSet.clear();
        for (String s : this.syncedActive.getStringValue()
            .split(",")) {
            if (!s.isEmpty()) {
                this.activeSet.add(s);
            }
        }
    }
}
