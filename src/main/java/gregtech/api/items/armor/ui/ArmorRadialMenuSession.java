package gregtech.api.items.armor.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

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
    private List<ArmorContext.ArmorContextImpl> cachedContexts = null;

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

        Set<BehaviorName> visibleBehaviors = getActiveArmorBehaviors();
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
        if (action == null || action.getBehaviorName() == null) return false;

        for (ArmorContext.ArmorContextImpl context : getLoadedArmorContexts()) {
            if (context.hasBehavior(action.getBehaviorName())) {
                return context.isBehaviorActive(action.getBehaviorName());
            }
        }
        return false;
    }

    private List<ArmorContext.ArmorContextImpl> getLoadedArmorContexts() {
        if (this.cachedContexts != null) {
            return this.cachedContexts;
        }

        this.cachedContexts = new ArrayList<>();
        for (int i = 0; i < ARMOR_SLOTS_COUNT; i++) {
            ItemStack armorPiece = player.getCurrentArmor(i);

            if (armorPiece != null && armorPiece.getItem() instanceof MechArmorBase) {
                ArmorState state = new ArmorState();
                ArmorContext.ArmorContextImpl context = new ArmorContext.ArmorContextImpl(player, armorPiece, state);
                ArmorState.load(context);
                this.cachedContexts.add(context);
            }
        }
        return this.cachedContexts;
    }

    private void triggerUiUpdate() {
        this.cachedContexts = null;
        if (onMenuUpdateCallback != null) {
            onMenuUpdateCallback.run();
        }
    }

    private void executeArmorAction(String clickedActionId) {
        if (player.getEntityWorld().isRemote || clickedActionId == null || clickedActionId.isEmpty()) return;

        ArmorAction action = ArmorActionManager.getAction(clickedActionId);
        if (action == null) return;

        boolean uiNeedsUpdate = false;

        for (ArmorContext.ArmorContextImpl context : getLoadedArmorContexts()) {
            boolean stateChanged = false;

            if (action.isToggle() && action.getBehaviorName() != null) {
                if (context.hasBehavior(action.getBehaviorName())) {
                    context.toggleBehavior(action.getBehaviorName());
                    stateChanged = true;
                }
            } else if (!action.isToggle() && action.getKeybind() != null) {
                for (IArmorBehavior behavior : context.getArmorState().behaviors.values()) {
                    if (behavior.getListenedKeys(context)
                        .contains(action.getKeybind())) {
                        behavior.onKeyPressed(context, action.getKeybind(), true);
                        stateChanged = true;
                    }
                }
            }

            if (stateChanged || context.isDirty()) {
                context.save();
                uiNeedsUpdate = true;
            }
        }

        if (uiNeedsUpdate) {
            triggerUiUpdate();
        }

        this.actionTriggerSync.setStringValue("");
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
        for (ArmorContext.ArmorContextImpl context : getLoadedArmorContexts()) {
            activeBehaviors.addAll(context.getArmorState().behaviors.keySet());
        }
        return activeBehaviors;
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
            () -> loadListFromNBT("RadialOrder", defaultString),
            (newOrder) -> saveListToNBT("RadialOrder", newOrder)).allowC2S();

        this.syncedVisible = new StringSyncValue(
            () -> loadListFromNBT("RadialVisible", defaultString),
            (newVisible) -> saveListToNBT("RadialVisible", newVisible)).allowC2S();

        this.syncManager.syncValue("orderSync", this.syncedOrder);
        this.syncManager.syncValue("visibleSync", this.syncedVisible);

        parseCurrentOrder(validActionIds);
        parseVisibleSet(validActionIds);

        this.syncedOrder.changeListener(() -> {
            parseCurrentOrder(validActionIds);
            triggerUiUpdate();
        });

        this.syncedVisible.changeListener(() -> {
            parseVisibleSet(validActionIds);
            triggerUiUpdate();
        });
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

    private void parseVisibleSet(List<String> validActionIds) {
        this.visibleSet.clear();
        for (String s : this.syncedVisible.getStringValue()
            .split(",")) {
            if (!s.isEmpty()) {
                this.visibleSet.add(s);
            }
        }

        List<String> savedOrderList = Arrays.asList(
            this.syncedOrder.getStringValue()
                .split(","));

        for (String action : validActionIds) {
            if (!savedOrderList.contains(action)) {
                this.visibleSet.add(action);
            }
        }
    }

    private void saveListToNBT(String key, String csvString) {
        NBTTagList tagList = new NBTTagList();
        if (csvString != null && !csvString.isEmpty()) {
            for (String s : csvString.split(",")) {
                if (!s.isEmpty()) {
                    tagList.appendTag(new NBTTagString(s));
                }
            }
        }
        this.nbt.setTag(key, tagList);
    }

    private String loadListFromNBT(String key, String defaultString) {
        if (this.nbt.hasKey(key, Constants.NBT.TAG_LIST)) {
            NBTTagList tagList = this.nbt.getTagList(key, Constants.NBT.TAG_STRING);
            List<String> loadedList = new ArrayList<>();
            for (int i = 0; i < tagList.tagCount(); i++) {
                loadedList.add(tagList.getStringTagAt(i));
            }

            return String.join(",", loadedList) + ",";
        }

        return defaultString;
    }
}
