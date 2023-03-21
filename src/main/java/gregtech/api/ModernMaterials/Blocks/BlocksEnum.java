package gregtech.api.ModernMaterials.Blocks;

import gregtech.api.ModernMaterials.ModernMaterial;
import net.minecraft.item.Item;

import java.util.ArrayList;

public enum BlocksEnum {

    FrameBox("% Frame Box");

    public String getLocalisedName(final ModernMaterial material) {
        return unlocalisedName.replace("%", material.getName());
    }

    public void setAssociatedItem(final Item item) {
        this.item = item;
    }

    final private String unlocalisedName;
    private Item item;
    public final ArrayList<ModernMaterial> associatedMaterials = new ArrayList<>();

    BlocksEnum(final String unlocalisedName) {
        this.unlocalisedName = unlocalisedName;
    }
}
