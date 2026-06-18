package gtPlusPlus.core.item.wearable;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.item.wearable.armor.ItemArmorTinFoilHat;

public class WearableLoader {

    public static void run() {

        Item TinFoilHat = new ItemArmorTinFoilHat().setUnlocalizedName("itemHatTinFoil");
        GameRegistry.registerItem(TinFoilHat, "itemHatTinFoil", GTPlusPlus.ID);
    }

}
