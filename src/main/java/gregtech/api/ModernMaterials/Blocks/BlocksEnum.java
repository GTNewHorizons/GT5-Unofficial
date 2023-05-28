package gregtech.api.ModernMaterials.Blocks;

import gregtech.api.ModernMaterials.ModernMaterial;
import gregtech.api.ModernMaterials.PartsClasses.IAssociatedMaterials;
import gregtech.api.ModernMaterials.PartsClasses.IGetItem;
import net.minecraft.item.Item;

import java.util.ArrayList;

public enum BlocksEnum implements IGetItem, IAssociatedMaterials {

    // Define new blocks here.
    FrameBox("% LARP Box");

    public String getLocalisedName(final ModernMaterial material) {
        return unlocalisedName.replace("%", material.getMaterialName());
    }

    final private String unlocalisedName;
    private Item item;
    private final ArrayList<ModernMaterial> associatedMaterials = new ArrayList<>();

    BlocksEnum(final String unlocalisedName) {
        this.unlocalisedName = unlocalisedName;
    }

    @Override
    public void setAssociatedItem(final Item item) {
        this.item = item;
    }

    @Override
    public Item getItem() {
        return item;
    }

    @Override
    public ArrayList<ModernMaterial> getAssociatedMaterials() {
        return associatedMaterials;
    }

    @Override
    public void addAssociatedMaterial(final ModernMaterial modernMaterial) {
        associatedMaterials.add(modernMaterial);
    }
}
