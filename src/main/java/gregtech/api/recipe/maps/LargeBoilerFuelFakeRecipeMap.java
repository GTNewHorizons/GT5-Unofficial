package gregtech.api.recipe.maps;

import static gregtech.api.enums.GT_Values.E;
import static gregtech.api.enums.Mods.GregTech;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;

public class LargeBoilerFuelFakeRecipeMap extends GT_Recipe.GT_Recipe_Map {

    private static final List<String> ALLOWED_SOLID_FUELS = Arrays.asList(
        GregTech_API.sMachineFile.mConfig.getStringList(
            "LargeBoiler.allowedFuels",
            ConfigCategories.machineconfig.toString(),
            new String[] { "gregtech:gt.blockreinforced:6", "gregtech:gt.blockreinforced:7" },
            "Allowed fuels for the Large Titanium Boiler and Large Tungstensteel Boiler"));

    public LargeBoilerFuelFakeRecipeMap() {
        super(
            new HashSet<>(55),
            "gt.recipe.largeboilerfakefuels",
            "Large Boiler",
            null,
            GregTech.getResourcePath(TEXTURES_GUI_BASICMACHINES, "Default"),
            1,
            1,
            1,
            0,
            1,
            E,
            1,
            E,
            true,
            true);
        GT_Recipe explanatoryRecipe = new GT_Recipe(
            true,
            new ItemStack[] {},
            new ItemStack[] {},
            null,
            null,
            null,
            null,
            1,
            1,
            1);
        explanatoryRecipe.setNeiDesc(
            "Not all solid fuels are listed.",
            "Any item that burns in a",
            "vanilla furnace will burn in",
            "a Large Bronze or Steel Boiler.");
        addRecipe(explanatoryRecipe);
    }

    public static boolean isAllowedSolidFuel(ItemStack stack) {
        return isAllowedSolidFuel(Item.itemRegistry.getNameForObject(stack.getItem()), stack.getItemDamage());
    }

    public static boolean isAllowedSolidFuel(String itemRegistryName, int meta) {
        return ALLOWED_SOLID_FUELS.contains(itemRegistryName + ":" + meta);
    }

    public static boolean addAllowedSolidFuel(ItemStack stack) {
        return addAllowedSolidFuel(Item.itemRegistry.getNameForObject(stack.getItem()), stack.getItemDamage());
    }

    public static boolean addAllowedSolidFuel(String itemregistryName, int meta) {
        return ALLOWED_SOLID_FUELS.add(itemregistryName + ":" + meta);
    }

    public GT_Recipe addDenseLiquidRecipe(GT_Recipe recipe) {
        return addRecipe(recipe, ((double) recipe.mSpecialValue) / 10);
    }

    public GT_Recipe addDieselRecipe(GT_Recipe recipe) {
        return addRecipe(recipe, ((double) recipe.mSpecialValue) / 40);
    }

    public void addSolidRecipes(ItemStack... itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            addSolidRecipe(itemStack);
        }
    }

    public GT_Recipe addSolidRecipe(ItemStack fuelItemStack) {
        boolean allowedFuel = false;
        if (fuelItemStack != null) {
            String registryName = Item.itemRegistry.getNameForObject(fuelItemStack.getItem());
            allowedFuel = ALLOWED_SOLID_FUELS.contains(registryName + ":" + fuelItemStack.getItemDamage());
        }
        return addRecipe(
            new GT_Recipe(
                true,
                new ItemStack[] { fuelItemStack },
                new ItemStack[] {},
                null,
                null,
                null,
                null,
                1,
                0,
                GT_ModHandler.getFuelValue(fuelItemStack) / 1600),
            ((double) GT_ModHandler.getFuelValue(fuelItemStack)) / 1600,
            allowedFuel);
    }

    private GT_Recipe addRecipe(GT_Recipe recipe, double baseBurnTime, boolean isAllowedFuel) {
        recipe = recipe.copyShallow();
        // Some recipes will have a burn time like 15.9999999 and % always rounds down
        double floatErrorCorrection = 0.0001;

        double bronzeBurnTime = baseBurnTime * 2 + floatErrorCorrection;
        bronzeBurnTime -= bronzeBurnTime % 0.05;
        double steelBurnTime = baseBurnTime + floatErrorCorrection;
        steelBurnTime -= steelBurnTime % 0.05;
        double titaniumBurnTime = baseBurnTime * 0.3 + floatErrorCorrection;
        titaniumBurnTime -= titaniumBurnTime % 0.05;
        double tungstensteelBurnTime = baseBurnTime * 0.15 + floatErrorCorrection;
        tungstensteelBurnTime -= tungstensteelBurnTime % 0.05;

        if (isAllowedFuel) {
            recipe.setNeiDesc(
                "Burn time in seconds:",
                String.format("Bronze Boiler: %.4f", bronzeBurnTime),
                String.format("Steel Boiler: %.4f", steelBurnTime),
                String.format("Titanium Boiler: %.4f", titaniumBurnTime),
                String.format("Tungstensteel Boiler: %.4f", tungstensteelBurnTime));
        } else {
            recipe.setNeiDesc(
                "Burn time in seconds:",
                String.format("Bronze Boiler: %.4f", bronzeBurnTime),
                String.format("Steel Boiler: %.4f", steelBurnTime),
                "Titanium Boiler: Not allowed",
                "Tungstenst. Boiler: Not allowed");
        }

        return super.addRecipe(recipe);
    }

    private GT_Recipe addRecipe(GT_Recipe recipe, double baseBurnTime) {
        recipe = recipe.copyShallow();
        // Some recipes will have a burn time like 15.9999999 and % always rounds down
        double floatErrorCorrection = 0.0001;

        double bronzeBurnTime = baseBurnTime * 2 + floatErrorCorrection;
        bronzeBurnTime -= bronzeBurnTime % 0.05;
        double steelBurnTime = baseBurnTime + floatErrorCorrection;
        steelBurnTime -= steelBurnTime % 0.05;

        recipe.setNeiDesc(
            "Burn time in seconds:",
            String.format("Bronze Boiler: %.4f", bronzeBurnTime),
            String.format("Steel Boiler: %.4f", steelBurnTime),
            "Titanium Boiler: Not allowed",
            "Tungstenst. Boiler: Not allowed");

        return super.addRecipe(recipe);
    }
}
