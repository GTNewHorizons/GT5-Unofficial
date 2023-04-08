package gtPlusPlus.core.item.wearable.armour;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.item.wearable.armour.tinfoil.ArmourTinFoilHat;

public class ArmourLoader {

    // Glass
    public static Item ClearGlassChestplate;
    public static Item ClearGlassBoots;
    public static Item ClearGlassLeggings;
    public static Item ClearGlassHelmet;

    // Tin Foil
    public static Item TinFoilHat;

    public static ArmorMaterial ClearGlassArmour = EnumHelper
            .addArmorMaterial("ClearGlassArmor", 1, new int[] { 1, 1, 1, 1 }, 100);
    public static ArmorMaterial TinFoilArmour = EnumHelper.addArmorMaterial("TINFOIL", 5, new int[] { 1, 1, 1, 1 }, 50);

    public static void run() {
        glassArmour();
        tinfoilArmour();
    }

    private static void glassArmour() {}

    private static void tinfoilArmour() {
        TinFoilHat = new ArmourTinFoilHat().setUnlocalizedName("itemHatTinFoil");
        GameRegistry.registerItem(TinFoilHat, "itemHatTinFoil", GTPlusPlus.ID);
    }
}
