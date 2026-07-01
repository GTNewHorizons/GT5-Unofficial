package gregtech.api.items.armor;

import com.cleanroommc.modularui.drawable.Icon;
import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;
import com.gtnewhorizons.angelica.shadow.javax.annotation.Nullable;

import gregtech.api.items.armor.behaviors.BehaviorName;

public class ArmorAction {

    private final String id;
    private final String displayName;
    private final SyncedKeybind keybind;
    private final Icon icon;
    private final boolean isToggle;
    private final BehaviorName behaviorName;

    public ArmorAction(String id, String displayName, Icon icon, boolean isToggle, SyncedKeybind keybind) {
        this(id, displayName, icon, isToggle, keybind, null);
    }

    public ArmorAction(String id, String displayName, Icon icon, boolean isToggle, SyncedKeybind keybind,
        BehaviorName behaviorName) {
        this.id = id;
        this.displayName = displayName;
        this.keybind = keybind;
        this.icon = icon;
        this.isToggle = isToggle;
        this.behaviorName = behaviorName;
    }

    public Icon getIcon() {
        return icon;
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
