package gregtech.api.recipe.maps;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.GregTech_API;
import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.GT_Values;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
public class LargeBoilerFuelBackend extends RecipeMapBackend {

    private static final List<String> ALLOWED_SOLID_FUELS = Arrays.asList(
        GregTech_API.sMachineFile.mConfig.getStringList(
            "LargeBoiler.allowedFuels",
            ConfigCategories.machineconfig.toString(),
            new String[] { "gregtech:gt.blockreinforced:6", "gregtech:gt.blockreinforced:7" },
            "Allowed fuels for the Large Titanium Boiler and Large Tungstensteel Boiler"));

    public LargeBoilerFuelBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
        GT_Values.RA.stdBuilder()
            .duration(1)
            .eut(1)
            .specialValue(1)
            .setNEIDesc(
                "Not all solid fuels are listed.",
                "Any item that burns in a",
                "vanilla furnace will burn in",
                "a Large Bronze or Steel Boiler.")
            .build()
            .map(this::doAdd);
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
        return addRecipe(recipe, ((double) recipe.mSpecialValue) / 10, false);
    }

    public GT_Recipe addDieselRecipe(GT_Recipe recipe) {
        return addRecipe(recipe, ((double) recipe.mSpecialValue) / 40, false);
    }

    public void addSolidRecipes(ItemStack... itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            addSolidRecipe(itemStack);
        }
    }

    @Nullable
    public GT_Recipe addSolidRecipe(ItemStack fuelItemStack) {
        if (fuelItemStack == null) {
            return null;
        }
        String registryName = Item.itemRegistry.getNameForObject(fuelItemStack.getItem());
        boolean isHighTierAllowed = ALLOWED_SOLID_FUELS.contains(registryName + ":" + fuelItemStack.getItemDamage());
        return GT_Values.RA.stdBuilder()
            .itemInputs(fuelItemStack)
            .duration(1)
            .eut(0)
            .specialValue(GT_ModHandler.getFuelValue(fuelItemStack) / 1600)
            .build()
            .map(r -> addRecipe(r, ((double) GT_ModHandler.getFuelValue(fuelItemStack)) / 1600, isHighTierAllowed))
            .orElse(null);
    }

    private GT_Recipe addRecipe(GT_Recipe recipe, double baseBurnTime, boolean isHighTierAllowed) {
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

        if (isHighTierAllowed) {
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

        return super.doAdd(recipe);
    }
}
