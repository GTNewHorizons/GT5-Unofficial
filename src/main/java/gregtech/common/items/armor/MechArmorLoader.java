package gregtech.common.items.armor;

import static gregtech.api.enums.Mods.GregTech;

import java.util.List;

import com.google.common.collect.ImmutableList;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;

public class MechArmorLoader {

    public static MechArmorBase MechArmorHelmet;
    public static MechArmorBase MechArmorChestplate;
    public static MechArmorBase MechArmorLeggings;
    public static MechArmorBase MechArmorBoots;

    public static List<MechArmorBase> AllMechArmor;

    public static void run() {
        MechArmorHelmet = (MechArmorBase) new MechHelmet().setUnlocalizedName("itemHelmetMech");
        MechArmorChestplate = (MechArmorBase) new MechChestplate().setUnlocalizedName("itemChestplateMech");
        MechArmorLeggings = (MechArmorBase) new MechLeggings().setUnlocalizedName("itemLeggingsMech");
        MechArmorBoots = (MechArmorBase) new MechBoots().setUnlocalizedName("itemBootsMech");

        GameRegistry.registerItem(MechArmorHelmet, "itemHelmetMech", GregTech.ID);
        GameRegistry.registerItem(MechArmorChestplate, "itemChestplateMech", GregTech.ID);
        GameRegistry.registerItem(MechArmorLeggings, "itemLeggingsMech", GregTech.ID);
        GameRegistry.registerItem(MechArmorBoots, "itemBootsMech", GregTech.ID);

        ItemList.Mechanical_Helmet.set(MechArmorHelmet);
        ItemList.Mechanical_Chestplate.set(MechArmorChestplate);
        ItemList.Mechanical_Leggings.set(MechArmorLeggings);
        ItemList.Mechanical_Boots.set(MechArmorBoots);

        AllMechArmor = ImmutableList.of(MechArmorHelmet, MechArmorChestplate, MechArmorLeggings, MechArmorBoots);
    }
}
