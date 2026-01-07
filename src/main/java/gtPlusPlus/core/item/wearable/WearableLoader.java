package gtPlusPlus.core.item.wearable;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.item.wearable.armor.ItemArmourTinFoilHat;

public class WearableLoader {

    public static void run() {

        Item TinFoilHat = new ItemArmourTinFoilHat().setUnlocalizedName("itemHatTinFoil");
        GameRegistry.registerItem(TinFoilHat, "itemHatTinFoil", GTPlusPlus.ID);
    }

}
