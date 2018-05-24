package gtPlusPlus.core.item.wearable.armour;

import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;

import net.minecraftforge.common.util.EnumHelper;

public class ArmourLoader {

	public static Item ClearGlassChestplate;
	public static Item ClearGlassBoots;
	public static Item ClearGlassLeggings;
	public static Item ClearGlassHelmet;
	
	public static ArmorMaterial ClearGlassArmor = EnumHelper.addArmorMaterial("ClearGlassArmor", 1, new int[] { 1, 4, 2, 1 }, 100);
	
	
	public static void run() {
		glassArmour();
	}
	
	private static void glassArmour() {
		//RenderingRegistry.addNewArmourRendererPrefix("ClearGlassArmor"); This needs to be client side only TODO
		//ClearGlassHelmet = new ClearGlassArmor(2055, ClearGlassArmor, 5, 0).setUnlocalizedName("amethyst_helmet");
	}
	
}
