package gregtech.api.items.armor;

import com.gtnewhorizon.gtnhlib.keybind.SyncedKeybind;
import net.minecraft.util.IIcon;

public class ArmorAction {
    private final String id;
    private final String displayName;
    private final SyncedKeybind keybind;
    private final IIcon icon;

    public ArmorAction(String id, String displayName, IIcon icon, SyncedKeybind keybind) {
        this.id = id;
        this.displayName = displayName;
        this.keybind = keybind;
        this.icon = icon;
    }

    public IIcon getIcon() { return icon; }
    public String getId() { return id; }
    public String getDisplayName() { return displayName; }
    public SyncedKeybind getKeybind() { return keybind; }
}
