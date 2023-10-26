package gregtech.api.ModernMaterials.PartsClasses;

import java.util.HashSet;

import gregtech.api.ModernMaterials.ModernMaterial;
import net.minecraft.item.Item;

public interface IEnumPart {

    HashSet<ModernMaterial> getAssociatedMaterials();

    void addAssociatedMaterial(final ModernMaterial modernMaterial);

    void setAssociatedItem(final Item item);

    Item getItem();

}
