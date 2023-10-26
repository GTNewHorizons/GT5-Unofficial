package gregtech.api.ModernMaterials.PartsClasses;

import net.minecraft.item.Item;

public interface IGetItem {

    void setAssociatedItem(final Item item);

    Item getItem();

}
