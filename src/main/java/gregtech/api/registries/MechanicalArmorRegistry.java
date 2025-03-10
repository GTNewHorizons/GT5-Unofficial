package gregtech.api.registries;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.registries.MechanicalArmorRegistry.AugmentIDs.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.items.ItemAugmentBase;
import gregtech.api.items.ItemAugmentCore;
import gregtech.api.items.ItemAugmentFrame;
import gregtech.api.items.armor.behaviors.CreativeFlightBehavior;
import gregtech.api.items.armor.behaviors.FireImmunityBehavior;
import gregtech.api.items.armor.behaviors.JetpackBehavior;
import gregtech.api.items.armor.behaviors.JetpackPerfectHoverBehavior;
import gregtech.api.items.armor.behaviors.NightVisionBehavior;
import gregtech.api.util.GTLog;
import gregtech.common.items.armor.MechBoots;
import gregtech.common.items.armor.MechChestplate;
import gregtech.common.items.armor.MechHelmet;
import gregtech.common.items.armor.MechLeggings;

public class MechanicalArmorRegistry implements Runnable {

    private static final Map<Integer, ItemAugmentBase> augments = new HashMap<>();

    public static Item MechArmorHelmet;
    public static Item MechArmorChestplate;
    public static Item MechArmorLeggings;
    public static Item MechArmorBoots;

    @Override
    public void run() {
        GTLog.out.println("GTMod: Registering Mechanical Armor & Augments");
        registerMechanicalArmor();
        registerMechanicalArmorAugments();
    }

    public static ItemAugmentBase getMechanicalAugment(int id) {
        return augments.get(id);
    }

    public static void registerMechanicalArmorAugments() {
        ItemList.Armor_Core_T1.set(
            new ItemAugmentCore(
                "armorcore1",
                "Armor Core (Nano)",
                "Basic core for Mechanical Armor",
                Collections.emptyList(),
                CORE_T1.ID));
        augments.put(CORE_T1.ID, (ItemAugmentBase) ItemList.Armor_Core_T1.getItem());

        ItemList.Armor_Core_T2.set(
            new ItemAugmentCore(
                "armorcore2",
                "Armor Core (Quantum)",
                "Advanced core for Mechanical Armor",
                Collections.emptyList(),
                CORE_T2.ID));
        augments.put(CORE_T2.ID, (ItemAugmentBase) ItemList.Armor_Core_T2.getItem());

        ItemList.Armor_Core_T3.set(
            new ItemAugmentCore(
                "armorcore3",
                "Armor Core (Living)",
                "Supreme core for Mechanical Armor",
                Collections.emptyList(),
                CORE_T3.ID));
        augments.put(CORE_T3.ID, (ItemAugmentBase) ItemList.Armor_Core_T3.getItem());

        ItemList.Augment_NightVision.set(
            new ItemAugmentBase(
                "augmentnightvision",
                "Augment: Night Vision",
                "blah",
                Collections.singletonList(NightVisionBehavior.INSTANCE),
                AUG_NIGHT_VISION.ID));
        augments.put(AUG_NIGHT_VISION.ID, (ItemAugmentBase) ItemList.Augment_NightVision.getItem());

        ItemList.Augment_CreativeFlight.set(
            new ItemAugmentBase(
                "augmentcreativeflight",
                "Augment: Gravity Manipulation Module",
                "blah",
                Collections.singletonList(CreativeFlightBehavior.INSTANCE),
                AUG_CREATIVE_FLIGHT.ID));
        augments.put(AUG_CREATIVE_FLIGHT.ID, (ItemAugmentBase) ItemList.Augment_CreativeFlight.getItem());

        ItemList.Augment_Jetpack.set(
            new ItemAugmentBase(
                "augmentjetpack",
                "Augment: Jetpack",
                "blah",
                Collections.singletonList(JetpackBehavior.INSTANCE),
                AUG_JETPACK.ID));
        augments.put(AUG_JETPACK.ID, (ItemAugmentBase) ItemList.Augment_Jetpack.getItem());

        ItemList.Augment_Jetpack_PerfectHover.set(
            new ItemAugmentBase(
                "augmentjetpackperfecthover",
                "Augment: Jetpack Perfect Hover",
                "blah",
                Collections.singletonList(JetpackPerfectHoverBehavior.INSTANCE),
                Collections.singletonList(JetpackBehavior.INSTANCE),
                Collections.emptyList(),
                AUG_JETPACK_PERFECT.ID));
        augments.put(AUG_JETPACK_PERFECT.ID, (ItemAugmentBase) ItemList.Augment_Jetpack_PerfectHover.getItem());

        ItemList.Augment_FireImmunity.set(
            new ItemAugmentBase(
                "augmentfireimmunity",
                "Augment: Fire Immunity",
                "blah",
                Collections.singletonList(FireImmunityBehavior.INSTANCE),
                AUG_FIRE_IMMUNITY.ID));
        augments.put(AUG_FIRE_IMMUNITY.ID, (ItemAugmentBase) ItemList.Augment_FireImmunity.getItem());

        ItemList.Armor_Frame_Titanium.set(
            new ItemAugmentFrame(
                "augmentframetitanium",
                "Titanium Mechanical Frame",
                "",
                Collections.emptyList(),
                FRAME_TITANIUM.ID,
                new String[] { "####~~~~", "####~~~~", "####~~~~", "########", "########", "########" }));
        augments.put(FRAME_TITANIUM.ID, (ItemAugmentBase) ItemList.Armor_Frame_Titanium.getItem());
    }

    public static void registerMechanicalArmor() {
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

    public enum AugmentIDs {

        // Hopefully 999 of each is enough
        CORE_T1(1),
        CORE_T2(2),
        CORE_T3(3),

        AUG_NIGHT_VISION(1000),
        AUG_CREATIVE_FLIGHT(1001),
        AUG_JETPACK(1002),
        AUG_JETPACK_PERFECT(1003),
        AUG_FIRE_IMMUNITY(1004),

        FRAME_TITANIUM(2000),;

        public final int ID;

        AugmentIDs(int ID) {
            this.ID = ID;
        }
    }
}
