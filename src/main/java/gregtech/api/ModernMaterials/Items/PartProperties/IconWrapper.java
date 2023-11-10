package gregtech.api.ModernMaterials.Items.PartProperties;

import net.minecraft.util.IIcon;

public class IconWrapper {

    public int getPriority() {
        return priority;
    }

    public boolean isColoured() {
        return isColoured;
    }

    public IIcon getIcon() {
        return icon;
    }

    public final int priority;
    public final boolean isColoured;
    public final IIcon icon;

    public IconWrapper(int priority, boolean isColoured, IIcon icon) {
        this.priority = priority;
        this.isColoured = isColoured;
        this.icon = icon;
    }

}
