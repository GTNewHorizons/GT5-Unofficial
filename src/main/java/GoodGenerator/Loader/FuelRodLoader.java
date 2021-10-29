package GoodGenerator.Loader;

import GoodGenerator.Items.FuelRod;
import GoodGenerator.Items.RadioactiveItem;
import GoodGenerator.Main.GoodGenerator;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import static GoodGenerator.util.DescTextLocalization.addText;

public class FuelRodLoader {

    public static Item rodCompressedUraniumDepleted;
    public static Item rodCompressedUraniumDepleted_2;
    public static Item rodCompressedUraniumDepleted_4;
    public static Item rodCompressedPlutoniumDepleted;
    public static Item rodCompressedPlutoniumDepleted_2;
    public static Item rodCompressedPlutoniumDepleted_4;
    public static Item rodCompressedUranium;
    public static Item rodCompressedUranium_2;
    public static Item rodCompressedUranium_4;
    public static Item rodCompressedPlutonium;
    public static Item rodCompressedPlutonium_2;
    public static Item rodCompressedPlutonium_4;

    public static void RegisterRod() {
        rodCompressedUraniumDepleted = new RadioactiveItem("rodCompressedUraniumDepleted", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 100);
        rodCompressedUraniumDepleted_2 = new RadioactiveItem("rodCompressedUraniumDepleted2", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 200);
        rodCompressedUraniumDepleted_4 = new RadioactiveItem("rodCompressedUraniumDepleted4", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 400);
        rodCompressedPlutoniumDepleted = new RadioactiveItem("rodCompressedPlutoniumDepleted", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 120);
        rodCompressedPlutoniumDepleted_2 = new RadioactiveItem("rodCompressedPlutoniumDepleted2", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 240);
        rodCompressedPlutoniumDepleted_4 = new RadioactiveItem("rodCompressedPlutoniumDepleted4", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 480);

        GameRegistry.registerItem(rodCompressedUraniumDepleted, "rodCompressedUraniumDepleted", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodCompressedUraniumDepleted_2, "rodCompressedUraniumDepleted2", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodCompressedUraniumDepleted_4, "rodCompressedUraniumDepleted4", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodCompressedPlutoniumDepleted, "rodCompressedPlutoniumDepleted", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodCompressedPlutoniumDepleted_2, "rodCompressedPlutoniumDepleted2", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodCompressedPlutoniumDepleted_4, "rodCompressedPlutoniumDepleted4", GoodGenerator.MOD_ID);

        rodCompressedUranium = new FuelRod("rodCompressedUranium", 1, 100, 4, 800, 70000, new ItemStack(rodCompressedUraniumDepleted, 1), GoodGenerator.GG);
        rodCompressedUranium_2 = new FuelRod("rodCompressedUranium2", 2, 100, 4, 1600, 70000, new ItemStack(rodCompressedUraniumDepleted_2, 1), GoodGenerator.GG);
        rodCompressedUranium_4 = new FuelRod("rodCompressedUranium4", 4, 100, 4, 3200, 70000, new ItemStack(rodCompressedUraniumDepleted_4, 1), GoodGenerator.GG);
        rodCompressedPlutonium = new FuelRod("rodCompressedPlutonium", 1, 50, 4, 1000, 30000, 6, new ItemStack(rodCompressedPlutoniumDepleted, 1), GoodGenerator.GG);
        rodCompressedPlutonium_2 = new FuelRod("rodCompressedPlutonium2", 2, 50, 4, 2000, 30000, 6, new ItemStack(rodCompressedPlutoniumDepleted_2, 1), GoodGenerator.GG);
        rodCompressedPlutonium_4 = new FuelRod("rodCompressedPlutonium4", 4, 50, 4, 4000, 30000, 6, new ItemStack(rodCompressedPlutoniumDepleted_4, 1), GoodGenerator.GG);

        GameRegistry.registerItem(rodCompressedUranium, "rodCompressedUranium", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodCompressedUranium_2, "rodCompressedUranium2", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodCompressedUranium_4, "rodCompressedUranium4", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodCompressedPlutonium, "rodCompressedPlutonium", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodCompressedPlutonium_2, "rodCompressedPlutonium2", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodCompressedPlutonium_4, "rodCompressedPlutonium4", GoodGenerator.MOD_ID);
    }
}
