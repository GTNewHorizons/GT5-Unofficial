package gtPlusPlus.core.common.compat;

import static gtPlusPlus.core.handler.COMPAT_HANDLER.RemoveRecipeQueue;

import net.minecraft.item.ItemStack;

import gtPlusPlus.core.lib.CORE.ConfigSwitches;
import gtPlusPlus.core.recipe.ShapedRecipeObject;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class COMPAT_IC2 {

    private static ItemStack itemCropnalyzer = ItemUtils.simpleMetaStack("IC2:itemCropnalyzer", 0, 1);
    private static ItemStack itemSolarHelmet = ItemUtils.simpleMetaStack("IC2:itemSolarHelmet", 0, 1);

    public static ShapedRecipeObject Cropnalyzer = new ShapedRecipeObject(
            "ore:cableGt02Copper",
            "ore:cableGt02Copper",
            null,
            "minecraft:redstone",
            "ore:blockGlass",
            "minecraft:redstone",
            "minecraft:redstone",
            "ore:circuitBasic",
            "minecraft:redstone",
            itemCropnalyzer);
    public static ShapedRecipeObject SolarHelmet = new ShapedRecipeObject(
            "ore:plateIron",
            "ore:plateIron",
            "ore:plateIron",
            "ore:plateIron",
            "gregtech:gt.metaitem.01:32750",
            "ore:plateIron",
            "ore:craftingWireCopper",
            "ore:craftingWireCopper",
            "ore:craftingWireCopper",
            itemSolarHelmet);

    public static void OreDict() {
        run();
    }

    private static void run() {

        if (ConfigSwitches.disableIC2Recipes) {

            // Remove these.
            RemoveRecipeQueue.add("IC2:itemCable");
            RemoveRecipeQueue.add("IC2:itemCable:1");
            RemoveRecipeQueue.add("IC2:itemCable:2");
            RemoveRecipeQueue.add("IC2:itemCable:3");
            RemoveRecipeQueue.add("IC2:itemCable:5");
            RemoveRecipeQueue.add("IC2:itemCable:6");
            RemoveRecipeQueue.add("IC2:itemCable:10");
            RemoveRecipeQueue.add("IC2:itemCable:13");
        }
    }
}
