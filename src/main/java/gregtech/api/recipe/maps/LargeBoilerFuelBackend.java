package gregtech.api.recipe.maps;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import gregtech.api.recipe.RecipeMaps;
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
        return addRecipe(
            recipe,
            ((double) recipe.mSpecialValue) / 10,
            true,
            (recipe.mSpecialValue > HIGH_TIER_FLUID_THRESHOLD));
    }

    public GTRecipe addDieselGasRecipe(GTRecipe recipe) {
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

    @Override
    public boolean containsInput(ItemStack itemInput) {
        // Function for recipe collision in RecipeMaps when the same fluid is used in different generators
        return RecipeMaps.largeBoilerFakeFuels.containsInput(GTUtility.getFluidForFilledItem(itemInput, true));
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
                .fake()
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

    public static int getBurntimeRatioTicks(double fuelValueTicks, int divider) {
        // 10 in this formula is 10 seconds of burn time in steel boiler, or 16000 Burn Time value
        return new BigDecimal(
            ((fuelValueTicks / divider * Math.max(1, 1 + Math.log(fuelValueTicks / divider / 10) * 0.025))))
                .setScale(2, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(20))
                .intValue();
    }

    private static double getBurntimeRatio(double fuelValue) {
        // 10 in this formula is 10 seconds of burn time in steel boiler, or 16000 Burn Time value
        return fuelValue * Math.max(1, 1 + Math.log(fuelValue / 10) * 0.025);
    }

    private GTRecipe addRecipe(GTRecipe recipe, double baseBurnTime, boolean isAllowedInSteelBoiler,
        boolean isHighTierAllowed) {
        BigDecimal ticksDecimal = new BigDecimal("20");
        // the initial non modified time is now stored in ticks
        BigDecimal correctedBurnTime = new BigDecimal(
            new BigDecimal(getBurntimeRatio(baseBurnTime)).setScale(2, RoundingMode.HALF_UP)
                .multiply(ticksDecimal)
                .intValue());

        BigDecimal bronzeBurnTime = correctedBurnTime.multiply(new BigDecimal("2"))
            .divide(ticksDecimal);
        BigDecimal steelBurnTime = correctedBurnTime.divide(ticksDecimal);
        BigDecimal titaniumBurnTime = correctedBurnTime.multiply(new BigDecimal("0.3"))
            .divide(ticksDecimal);
        titaniumBurnTime = titaniumBurnTime.subtract(titaniumBurnTime.remainder(new BigDecimal("0.05")));
        BigDecimal tungstensteelBurnTime = correctedBurnTime.multiply(new BigDecimal("0.15"))
            .divide(ticksDecimal);
        tungstensteelBurnTime = tungstensteelBurnTime.subtract(tungstensteelBurnTime.remainder(new BigDecimal("0.05")));

        FluidStack foundFluid = GTUtility.getFluidForFilledItem(recipe.getRepresentativeInput(0), true);
        if (foundFluid != null) {
            // Removes item, adds fluid for NEI and sets mDuration in ticks for usage in LargeBoilerBase
            recipe.setFluidInputs(foundFluid);
            recipe.mInputs = new ItemStack[0];
            recipe.mDuration = correctedBurnTime.intValue();
        }
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
