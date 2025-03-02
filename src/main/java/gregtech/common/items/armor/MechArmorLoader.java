package gregtech.common.items.armor;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class MechArmorLoader {

    public static Item MechArmorHelmet;
    public static Item MechArmorChestplate;
    public static Item MechArmorLeggings;
    public static Item MechArmorBoots;

    public static void run() {
        MechArmorHelmet = new MechHelmet().setUnlocalizedName("itemHelmetMech");
        MechArmorChestplate = new MechChestplate().setUnlocalizedName("itemChestplateMech");
        MechArmorLeggings = new MechLeggings().setUnlocalizedName("itemLeggingsMech");
        MechArmorBoots = new MechBoots().setUnlocalizedName("itemBootsMech");

        GameRegistry.registerItem(MechArmorHelmet, "itemHelmetMech", GregTech.ID);
        GameRegistry.registerItem(MechArmorChestplate, "itemChestplateMech", GregTech.ID);
        GameRegistry.registerItem(MechArmorLeggings, "itemLeggingsMech", GregTech.ID);
        GameRegistry.registerItem(MechArmorBoots, "itemBootsMech", GregTech.ID);
    }
}
