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

    public static final int ARMOR_SLOTS_COUNT = 4;

    private final EntityPlayer player;
    private final PanelSyncManager syncManager;
    private final NBTTagCompound nbt;

    private final Set<String> visibleSet = new HashSet<>();
    private final List<String> currentOrder = new ArrayList<>();

    private final StringSyncValue actionTriggerSync;
    private StringSyncValue syncedOrder;
    private StringSyncValue syncedVisible;

    private Runnable onMenuUpdateCallback;

    public ArmorRadialMenuSession(GuiData data, PanelSyncManager syncManager, UISettings settings) {
        this.player = data.getPlayer();
        this.syncManager = syncManager;
        this.nbt = getSettingsNBT(player);

        this.actionTriggerSync = createActionSync();

        Set<BehaviorName> visibleBehaviors = getVisibleArmorBehaviors();
        List<String> validActionIds = filterValidActions(visibleBehaviors);
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

    public boolean isVisible(String actionId) {
        return visibleSet.contains(actionId);
    }

    public void toggleVisibleState(String actionId, boolean isVisible) {
        if (isVisible) {
            this.visibleSet.add(actionId);
        } else {
            this.visibleSet.remove(actionId);
        }
        this.syncedVisible.setStringValue(String.join(",", this.visibleSet) + ",");
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

    public boolean getActionToggleState(String actionId) {
        ArmorAction action = ArmorActionManager.getAction(actionId);
        if (action == null) return false;

        for (int i = 0; i < ARMOR_SLOTS_COUNT; i++) {
            ItemStack armorPiece = player.getCurrentArmor(i);

            if (armorPiece == null) continue;
            if (!(armorPiece.getItem() instanceof MechArmorBase)) continue;

            ArmorState state = new ArmorState();
            ArmorContext.ArmorContextImpl context = new ArmorContext.ArmorContextImpl(player, armorPiece, state);
            ArmorState.load(context);

            if (action.getBehaviorName() != null) {
                if (context.hasBehavior(action.getBehaviorName())) {
                    return context.isBehaviorActive(action.getBehaviorName());
                }
            }
        }

        return false;
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

    private Set<BehaviorName> getVisibleArmorBehaviors() {
        Set<BehaviorName> visibleBehaviors = new HashSet<>();
        for (int i = 0; i < ARMOR_SLOTS_COUNT; i++) {
            ItemStack armorPiece = this.player.getCurrentArmor(i);
            if (armorPiece != null) {
                visibleBehaviors.addAll(ArmorState.load(armorPiece).behaviors.keySet());
            }
        }
        return visibleBehaviors;
    }

    private List<String> filterValidActions(Set<BehaviorName> visibleBehaviors) {
        List<String> validActionIds = new ArrayList<>();
        for (ArmorAction action : ArmorActionManager.getAllActions()) {
            if (action.getBehaviorName() == null || visibleBehaviors.contains(action.getBehaviorName())) {
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

        this.syncedVisible = new StringSyncValue(
            () -> this.nbt.hasKey("RadialVisible") ? this.nbt.getString("RadialVisible") : defaultString,
            (newVisible) -> this.nbt.setString("RadialVisible", newVisible)).allowC2S();

        this.syncManager.syncValue("orderSync", this.syncedOrder);
        this.syncManager.syncValue("visibleSync", this.syncedVisible);

        parseCurrentOrder(validActionIds);
        parseVisibleSet();
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

    private void parseVisibleSet() {
        this.visibleSet.clear();
        for (String s : this.syncedVisible.getStringValue()
            .split(",")) {
            if (!s.isEmpty()) {
                this.visibleSet.add(s);
            }
        }
    }
}
