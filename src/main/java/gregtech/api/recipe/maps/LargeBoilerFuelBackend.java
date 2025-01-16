package gregtech.api.recipe.maps;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.tileentities.machines.multi.MTELargeBoilerBronze;
import gregtech.common.tileentities.machines.multi.MTELargeBoilerSteel;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LargeBoilerFuelBackend extends RecipeMapBackend {

    private static boolean addedGeneralDesc = false;

    private static final List<String> ALLOWED_SOLID_FUELS = Arrays
        .asList("gregtech:gt.blockreinforced:6", "gregtech:gt.blockreinforced:7");

    public LargeBoilerFuelBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
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

    public GTRecipe addDenseLiquidRecipe(GTRecipe recipe) {
        return addRecipe(recipe, ((double) recipe.mSpecialValue) / 10, true, false);
    }

    public GTRecipe addDieselRecipe(GTRecipe recipe) {
        return addRecipe(recipe, ((double) recipe.mSpecialValue) / 40, true, false);
    }

    public void addSolidRecipes(ItemStack... itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            addSolidRecipe(itemStack);
        }
    }

    @Nullable
    public GTRecipe addSolidRecipe(@Nullable ItemStack fuelItemStack) {
        if (fuelItemStack == null) {
            return null;
        }

        // only fuels with a burn time larger than the bronze boilers' eu/t should be considered
        if (GTModHandler.getFuelValue(fuelItemStack) < MTELargeBoilerBronze.EUT_GENERATED) {
            return null;
        }

        if (!addedGeneralDesc) {
            GTValues.RA.stdBuilder()
                .duration(1)
                .eut(1)
                .specialValue(1)
                .setNEIDesc(
                    "Not all solid fuels are listed.",
                    "Any burnable item with a burn",
                    "time of at least " + MTELargeBoilerBronze.EUT_GENERATED
                        + " / "
                        + MTELargeBoilerSteel.EUT_GENERATED,
                    "will burn in a Large Bronze",
                    "/ Steel Boiler, respectively.")
                .build()
                .map(this::compileRecipe);
            addedGeneralDesc = true;
        }

        boolean isAllowedInSteelBoiler = GTModHandler.getFuelValue(fuelItemStack) >= MTELargeBoilerSteel.EUT_GENERATED;

        String registryName = Item.itemRegistry.getNameForObject(fuelItemStack.getItem());
        boolean isHighTierAllowed = ALLOWED_SOLID_FUELS.contains(registryName + ":" + fuelItemStack.getItemDamage());
        return GTValues.RA.stdBuilder()
            .itemInputs(fuelItemStack)
            .duration(1)
            .eut(0)
            .specialValue(GTModHandler.getFuelValue(fuelItemStack) / 1600)
            .build()
            .map(
                r -> addRecipe(
                    r,
                    ((double) GTModHandler.getFuelValue(fuelItemStack)) / 1600,
                    isAllowedInSteelBoiler,
                    isHighTierAllowed))
            .orElse(null);
    }

    private GTRecipe addRecipe(GTRecipe recipe, double baseBurnTime, boolean isAllowedInSteelBoiler,
        boolean isHighTierAllowed) {
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

        recipe.setNeiDesc(
            "Burn time in seconds:",
            String.format("Bronze Boiler: %.4f", bronzeBurnTime),
            "Steel Boiler: " + (isAllowedInSteelBoiler ? String.format("%.4f", steelBurnTime) : "Not allowed"),
            "Titanium Boiler: " + (isHighTierAllowed ? String.format("%.4f", titaniumBurnTime) : "Not allowed"),
            "Tungstenst. Boiler: "
                + (isHighTierAllowed ? String.format("%.4f", tungstensteelBurnTime) : "Not allowed"));

        return compileRecipe(recipe);
    }
}
