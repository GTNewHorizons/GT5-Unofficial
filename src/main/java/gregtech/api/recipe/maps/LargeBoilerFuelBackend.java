package gregtech.api.recipe.maps;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBackendPropertiesBuilder;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MethodsReturnNonnullByDefault;
import gregtech.common.tileentities.machines.multi.MTELargeBoilerBronze;
import gregtech.common.tileentities.machines.multi.MTELargeBoilerSteel;

@SuppressWarnings({ "unused", "UnusedReturnValue" })
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class LargeBoilerFuelBackend extends RecipeMapBackend {

    private static boolean addedGeneralDesc = false;
    // 500 represents mSpecialValue of the fuel, which is energy density of 1L of fuel, or in this case,
    // 500k EU per 1000L
    private static final int HIGH_TIER_FLUID_THRESHOLD = 500;

    private static final List<String> ALLOWED_FUELS = Arrays.asList(
        "gregtech:gt.blockreinforced:6",
        "gregtech:gt.blockreinforced:7",
        "ether",
        "gasoline",
        "nitrofuel",
        "ethanol gasoline",
        "jet fuel no.3",
        "jet fuel a",
        "highoctanegasoline");

    public LargeBoilerFuelBackend(RecipeMapBackendPropertiesBuilder propertiesBuilder) {
        super(propertiesBuilder);
    }

    public static boolean isAllowedFuel(ItemStack stack) {
        return isAllowedFuel(Item.itemRegistry.getNameForObject(stack.getItem()), stack.getItemDamage());
    }

    public static boolean isAllowedFuel(String itemRegistryName, int meta) {
        return ALLOWED_FUELS.contains(itemRegistryName + ":" + meta);
    }

    public static boolean isAllowedFuel(FluidStack stack) {
        return ALLOWED_FUELS.contains(
            stack.getFluid()
                .getName());
    }

    public GTRecipe addDenseLiquidRecipe(GTRecipe recipe) {
        return addRecipe(recipe, ((double) recipe.mSpecialValue) / 10, true, false);
    }

    public GTRecipe addDieselRecipe(GTRecipe recipe) {
        return addRecipe(
            recipe,
            ((double) recipe.mSpecialValue) / 20,
            true,
            (recipe.mSpecialValue > HIGH_TIER_FLUID_THRESHOLD));
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
                    GTUtility.breakLines(
                        StatCollector.translateToLocalFormatted(
                            "GT5U.nei.large_boiler.solid",
                            MTELargeBoilerBronze.EUT_GENERATED,
                            MTELargeBoilerSteel.EUT_GENERATED)))
                .build()
                .map(this::compileRecipe);
            addedGeneralDesc = true;
        }

        boolean isAllowedInSteelBoiler = GTModHandler.getFuelValue(fuelItemStack) >= MTELargeBoilerSteel.EUT_GENERATED;

        String registryName = Item.itemRegistry.getNameForObject(fuelItemStack.getItem());
        boolean isHighTierAllowed = ALLOWED_FUELS.contains(registryName + ":" + fuelItemStack.getItemDamage());
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

    public static double getBurntimeRatio(double fuelValue, int dividerMult) {
        // 10 in this formula is 10 seconds of burn time in steel boiler, or 16000 Burn Time value
        return fuelValue * Math.max(1, 1 + Math.log(fuelValue / 10 / dividerMult) * 0.025);
    }

    private GTRecipe addRecipe(GTRecipe recipe, double baseBurnTime, boolean isAllowedInSteelBoiler,
        boolean isHighTierAllowed) {
        // Some recipes will have a burn time like 15.9999999 and % always rounds down
        double floatErrorCorrection = 0.0001;

        double bronzeBurnTime = getBurntimeRatio(baseBurnTime, 1) * 2 + floatErrorCorrection;
        bronzeBurnTime -= bronzeBurnTime % 0.05;
        double steelBurnTime = getBurntimeRatio(baseBurnTime, 1) + floatErrorCorrection;
        steelBurnTime -= steelBurnTime % 0.05;
        double titaniumBurnTime = getBurntimeRatio(baseBurnTime, 1) * 0.3 + floatErrorCorrection;
        titaniumBurnTime -= titaniumBurnTime % 0.05;
        double tungstensteelBurnTime = getBurntimeRatio(baseBurnTime, 1) * 0.15 + floatErrorCorrection;
        tungstensteelBurnTime -= tungstensteelBurnTime % 0.05;

        recipe.setNeiDesc(
            StatCollector.translateToLocal("GT5U.nei.large_boiler.burn_time"),
            StatCollector
                .translateToLocalFormatted("GT5U.nei.large_boiler.bronze_boiler", formatNumber(bronzeBurnTime)),
            isAllowedInSteelBoiler
                ? StatCollector
                    .translateToLocalFormatted("GT5U.nei.large_boiler.steel_boiler", formatNumber(steelBurnTime))
                : StatCollector.translateToLocal("GT5U.nei.large_boiler.steel_boiler.ban"),
            isHighTierAllowed
                ? StatCollector
                    .translateToLocalFormatted("GT5U.nei.large_boiler.titanium_boiler", formatNumber(titaniumBurnTime))
                : StatCollector.translateToLocal("GT5U.nei.large_boiler.titanium_boiler.ban"),
            isHighTierAllowed
                ? StatCollector.translateToLocalFormatted(
                    "GT5U.nei.large_boiler.tungstensteel_boiler",
                    formatNumber(tungstensteelBurnTime))
                : StatCollector.translateToLocal("GT5U.nei.large_boiler.tungstensteel_boiler.ban"));

        return compileRecipe(recipe);
    }
}
