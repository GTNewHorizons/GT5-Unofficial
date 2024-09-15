package gregtech.common.items.flinttools;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.common.util.EnumHelper;

public class FlintTools {
    public static Item.ToolMaterial FLINT_MATERIAL = EnumHelper.addToolMaterial("FLINT", 1, 131, 4.0F, 1.0F, 5);

    public static void registerTools() {
        GameRegistry.registerItem(new FlintAxe(), "flintAxe");
        GameRegistry.registerItem(new FlintSword(), "flintSword");
        GameRegistry.registerItem(new FlintPickaxe(), "flintPickaxe");
        GameRegistry.registerItem(new FlintShovel(), "flintShovel");
        GameRegistry.registerItem(new FlintHoe(), "flintHoe");
    }
}
