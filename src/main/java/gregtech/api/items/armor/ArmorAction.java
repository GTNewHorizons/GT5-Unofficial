package gregtech.api.items.armor;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;

import gregtech.api.items.armor.behaviors.BehaviorName;

public class ArmorAction {

    private final String id;
    private final String displayName;
    private final SyncedKeybind keybind;
    private final boolean isToggle;
    private final BehaviorName behaviorName;

    public ArmorAction(String id, String displayName, boolean isToggle, SyncedKeybind keybind) {
        this(id, displayName, isToggle, keybind, null);
    }

    public ArmorAction(String id, String displayName, boolean isToggle, SyncedKeybind keybind,
        BehaviorName behaviorName) {
        this.id = id;
        this.displayName = displayName;
        this.keybind = keybind;
        this.isToggle = isToggle;
        this.behaviorName = behaviorName;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public SyncedKeybind getKeybind() {
        return keybind;
    }

    @Nullable
    public BehaviorName getBehaviorName() {
        return behaviorName;
    }

    public boolean isToggle() {
        return isToggle;
    }
}
