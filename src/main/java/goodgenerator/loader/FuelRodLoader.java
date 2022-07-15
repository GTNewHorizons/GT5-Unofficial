package goodgenerator.loader;

import static goodgenerator.util.DescTextLocalization.addText;

import cpw.mods.fml.common.registry.GameRegistry;
import goodgenerator.items.DepletedFuelRod;
import goodgenerator.items.FuelRod;
import goodgenerator.main.GoodGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FuelRodLoader {

    public static Item rodCompressedUraniumDepleted;
    public static Item rodCompressedUraniumDepleted_2;
    public static Item rodCompressedUraniumDepleted_4;
    public static Item rodCompressedPlutoniumDepleted;
    public static Item rodCompressedPlutoniumDepleted_2;
    public static Item rodCompressedPlutoniumDepleted_4;
    public static Item rodLiquidUraniumDepleted;
    public static Item rodLiquidUraniumDepleted_2;
    public static Item rodLiquidUraniumDepleted_4;
    public static Item rodLiquidPlutoniumDepleted;
    public static Item rodLiquidPlutoniumDepleted_2;
    public static Item rodLiquidPlutoniumDepleted_4;
    public static Item rodCompressedUranium;
    public static Item rodCompressedUranium_2;
    public static Item rodCompressedUranium_4;
    public static Item rodCompressedPlutonium;
    public static Item rodCompressedPlutonium_2;
    public static Item rodCompressedPlutonium_4;
    public static Item rodLiquidUranium;
    public static Item rodLiquidUranium_2;
    public static Item rodLiquidUranium_4;
    public static Item rodLiquidPlutonium;
    public static Item rodLiquidPlutonium_2;
    public static Item rodLiquidPlutonium_4;

    public static void RegisterRod() {
        rodCompressedUraniumDepleted = new DepletedFuelRod(
                "rodCompressedUraniumDepleted", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 100);
        rodCompressedUraniumDepleted_2 = new DepletedFuelRod(
                "rodCompressedUraniumDepleted2", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 200);
        rodCompressedUraniumDepleted_4 = new DepletedFuelRod(
                "rodCompressedUraniumDepleted4", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 400);
        rodCompressedPlutoniumDepleted = new DepletedFuelRod(
                "rodCompressedPlutoniumDepleted", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 120);
        rodCompressedPlutoniumDepleted_2 = new DepletedFuelRod(
                "rodCompressedPlutoniumDepleted2", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 240);
        rodCompressedPlutoniumDepleted_4 = new DepletedFuelRod(
                "rodCompressedPlutoniumDepleted4", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 480);
        rodLiquidUraniumDepleted = new DepletedFuelRod(
                "rodLiquidUraniumDepleted", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 800);
        rodLiquidUraniumDepleted_2 = new DepletedFuelRod(
                "rodLiquidUraniumDepleted2", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 1600);
        rodLiquidUraniumDepleted_4 = new DepletedFuelRod(
                "rodLiquidUraniumDepleted4", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 3200);
        rodLiquidPlutoniumDepleted = new DepletedFuelRod(
                "rodLiquidPlutoniumDepleted", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 1000);
        rodLiquidPlutoniumDepleted_2 = new DepletedFuelRod(
                "rodLiquidPlutoniumDepleted2", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 2000);
        rodLiquidPlutoniumDepleted_4 = new DepletedFuelRod(
                "rodLiquidPlutoniumDepleted4", addText("depletedfuelrod.tooltip", 1), GoodGenerator.GG, 4000);

        GameRegistry.registerItem(rodCompressedUraniumDepleted, "rodCompressedUraniumDepleted", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(
                rodCompressedUraniumDepleted_2, "rodCompressedUraniumDepleted2", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(
                rodCompressedUraniumDepleted_4, "rodCompressedUraniumDepleted4", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(
                rodCompressedPlutoniumDepleted, "rodCompressedPlutoniumDepleted", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(
                rodCompressedPlutoniumDepleted_2, "rodCompressedPlutoniumDepleted2", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(
                rodCompressedPlutoniumDepleted_4, "rodCompressedPlutoniumDepleted4", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodLiquidUraniumDepleted, "rodLiquidUraniumDepleted", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodLiquidUraniumDepleted_2, "rodLiquidUraniumDepleted2", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodLiquidUraniumDepleted_4, "rodLiquidUraniumDepleted4", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodLiquidPlutoniumDepleted, "rodLiquidPlutoniumDepleted", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodLiquidPlutoniumDepleted_2, "rodLiquidPlutoniumDepleted2", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodLiquidPlutoniumDepleted_4, "rodLiquidPlutoniumDepleted4", GoodGenerator.MOD_ID);

        rodCompressedUranium = new FuelRod(
                "rodCompressedUranium",
                1,
                100,
                4,
                800,
                70000,
                new ItemStack(rodCompressedUraniumDepleted, 1),
                GoodGenerator.GG);
        rodCompressedUranium_2 = new FuelRod(
                "rodCompressedUranium2",
                2,
                100,
                4,
                1600,
                70000,
                new ItemStack(rodCompressedUraniumDepleted_2, 1),
                GoodGenerator.GG);
        rodCompressedUranium_4 = new FuelRod(
                "rodCompressedUranium4",
                4,
                100,
                4,
                3200,
                70000,
                new ItemStack(rodCompressedUraniumDepleted_4, 1),
                GoodGenerator.GG);
        rodCompressedPlutonium = new FuelRod(
                "rodCompressedPlutonium",
                1,
                50,
                4,
                1000,
                30000,
                6,
                new ItemStack(rodCompressedPlutoniumDepleted, 1),
                GoodGenerator.GG);
        rodCompressedPlutonium_2 = new FuelRod(
                "rodCompressedPlutonium2",
                2,
                50,
                4,
                2000,
                30000,
                6,
                new ItemStack(rodCompressedPlutoniumDepleted_2, 1),
                GoodGenerator.GG);
        rodCompressedPlutonium_4 = new FuelRod(
                "rodCompressedPlutonium4",
                4,
                50,
                4,
                4000,
                30000,
                6,
                new ItemStack(rodCompressedPlutoniumDepleted_4, 1),
                GoodGenerator.GG);
        rodLiquidUranium = new FuelRod(
                "rodLiquidUranium",
                1,
                1200,
                64,
                8000,
                6000,
                new ItemStack(rodLiquidUraniumDepleted, 1),
                GoodGenerator.GG);
        rodLiquidUranium_2 = new FuelRod(
                "rodLiquidUranium2",
                2,
                1200,
                64,
                8000,
                6000,
                new ItemStack(rodLiquidUraniumDepleted_2, 1),
                GoodGenerator.GG);
        rodLiquidUranium_4 = new FuelRod(
                "rodLiquidUranium4",
                4,
                1200,
                64,
                8000,
                6000,
                new ItemStack(rodLiquidUraniumDepleted_4, 1),
                GoodGenerator.GG);
        rodLiquidPlutonium = new FuelRod(
                "rodLiquidPlutonium",
                1,
                1600,
                64,
                10000,
                10000,
                2,
                new ItemStack(rodLiquidPlutoniumDepleted, 1),
                GoodGenerator.GG);
        rodLiquidPlutonium_2 = new FuelRod(
                "rodLiquidPlutonium2",
                2,
                1600,
                64,
                10000,
                10000,
                2,
                new ItemStack(rodLiquidPlutoniumDepleted_2, 1),
                GoodGenerator.GG);
        rodLiquidPlutonium_4 = new FuelRod(
                "rodLiquidPlutonium4",
                4,
                1600,
                64,
                10000,
                10000,
                2,
                new ItemStack(rodLiquidPlutoniumDepleted_4, 1),
                GoodGenerator.GG);

        GameRegistry.registerItem(rodCompressedUranium, "rodCompressedUranium", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodCompressedUranium_2, "rodCompressedUranium2", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodCompressedUranium_4, "rodCompressedUranium4", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodCompressedPlutonium, "rodCompressedPlutonium", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodCompressedPlutonium_2, "rodCompressedPlutonium2", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodCompressedPlutonium_4, "rodCompressedPlutonium4", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodLiquidUranium, "rodLiquidUranium", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodLiquidUranium_2, "rodLiquidUranium2", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodLiquidUranium_4, "rodLiquidUranium4", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodLiquidPlutonium, "rodLiquidPlutonium", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodLiquidPlutonium_2, "rodLiquidPlutonium2", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rodLiquidPlutonium_4, "rodLiquidPlutonium4", GoodGenerator.MOD_ID);
    }
}
