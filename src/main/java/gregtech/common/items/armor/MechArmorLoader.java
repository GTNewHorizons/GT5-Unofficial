package gregtech.common.items.armor;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

import static gregtech.api.enums.Mods.GregTech;

public class MechArmorLoader {
    public static Item MechArmorHelmet;

    public static void run() {
        MechArmorHelmet = new MechHelmet().setUnlocalizedName("itemHelmetMech");
        GameRegistry.registerItem(MechArmorHelmet, "itemHelmetMech", GregTech.ID);
    }
}
