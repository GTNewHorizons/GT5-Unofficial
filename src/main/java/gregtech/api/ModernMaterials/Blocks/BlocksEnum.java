package gregtech.api.ModernMaterials.Blocks;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.PartsClasses.IGetItem;
import net.minecraft.item.Item;

import java.util.ArrayList;

public enum BlocksEnum implements IGetItem {

    FrameBox("% Frame Box");

    public String getLocalisedName(final ModernMaterial material) {
        return unlocalisedName.replace("%", material.getName());
    }

    final private String unlocalisedName;
    private Item item;
    public final ArrayList<ModernMaterial> associatedMaterials = new ArrayList<>();

    BlocksEnum(final String unlocalisedName) {
        this.unlocalisedName = unlocalisedName;
    }

    @Override
    public void setAssociatedItem(Item item) {
        this.item = item;
    }

    @Override
    public Item getItem() {
        return item;
    }
}
