package gregtech.common.items.armor;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;

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

        ItemList.Mechanical_Helmet.set(MechArmorHelmet);
        ItemList.Mechanical_Chestplate.set(MechArmorChestplate);
        ItemList.Mechanical_Leggings.set(MechArmorLeggings);
        ItemList.Mechanical_Boots.set(MechArmorBoots);
    }
}
