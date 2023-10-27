package gregtech.api.ModernMaterials.PartsClasses;

import java.util.HashSet;

import net.minecraft.item.Item;

import gregtech.api.ModernMaterials.ModernMaterial;

public interface IEnumPart {

    HashSet<ModernMaterial> getAssociatedMaterials();

    void addAssociatedMaterial(final ModernMaterial modernMaterial);

    void setAssociatedItem(final Item item);

    Item getItem();

}
