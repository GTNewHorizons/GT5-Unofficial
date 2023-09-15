package gregtech.crossmod.holoinventory;

import net.dries007.holoInventory.compat.InventoryDecoderRegistry;

public class HoloInventory {

    public static void init() {
        InventoryDecoderRegistry.register(new GT_InventoryDecoder());
    }
}
