package gregtech.common.items.armor;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.items.armor.ArmorHelper.REGISTER_BOOTS;
import static gregtech.api.items.armor.ArmorHelper.REGISTER_CHEST;
import static gregtech.api.items.armor.ArmorHelper.REGISTER_HELMET;
import static gregtech.api.items.armor.ArmorHelper.REGISTER_LEGS;
import static gregtech.api.items.armor.ArmorHelper.SLOT_BOOTS;
import static gregtech.api.items.armor.ArmorHelper.SLOT_CHEST;
import static gregtech.api.items.armor.ArmorHelper.SLOT_HELMET;
import static gregtech.api.items.armor.ArmorHelper.SLOT_LEGS;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.items.armor.MechArmorAugmentRegistries;

public class MechArmorLoader {

    public static MechArmorBase MechArmorHelmet;
    public static MechArmorBase MechArmorChestplate;
    public static MechArmorBase MechArmorLeggings;
    public static MechArmorBase MechArmorBoots;

    public static void run() {
        MechArmorHelmet = new MechArmorBase(SLOT_HELMET, "helmet", REGISTER_HELMET);
        MechArmorChestplate = new MechArmorBase(SLOT_CHEST, "chestplate", REGISTER_CHEST);
        MechArmorLeggings = new MechArmorBase(SLOT_LEGS, "leggings", REGISTER_LEGS);
        MechArmorBoots = new MechArmorBase(SLOT_BOOTS, "boots", REGISTER_BOOTS);

        MechArmorHelmet.setUnlocalizedName("itemHelmetMech");
        MechArmorChestplate.setUnlocalizedName("itemChestplateMech");
        MechArmorLeggings.setUnlocalizedName("itemLeggingsMech");
        MechArmorBoots.setUnlocalizedName("itemBootsMech");

        GameRegistry.registerItem(MechArmorHelmet, "itemHelmetMech", GregTech.ID);
        GameRegistry.registerItem(MechArmorChestplate, "itemChestplateMech", GregTech.ID);
        GameRegistry.registerItem(MechArmorLeggings, "itemLeggingsMech", GregTech.ID);
        GameRegistry.registerItem(MechArmorBoots, "itemBootsMech", GregTech.ID);

        ItemList.Mechanical_Helmet.set(MechArmorHelmet.getStack());
        ItemList.Mechanical_Chestplate.set(MechArmorChestplate.getStack());
        ItemList.Mechanical_Leggings.set(MechArmorLeggings.getStack());
        ItemList.Mechanical_Boots.set(MechArmorBoots.getStack());

        MechArmorAugmentRegistries.init();
    }
}
