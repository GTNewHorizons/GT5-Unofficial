package gregtech.loaders.postload.recipes;

import static gregtech.api.recipe.RecipeMaps.czPullerRecipes;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.STRUCTURE_TIER;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.util.GTRecipeBuilder;

public final class CZPullerRecipes implements Runnable {

    public static final GrowthRecipe SILICON = new GrowthRecipe(
        "silicon",
        Materials.Silicon.getDust(1),
        ItemList.Silicon_Seed_Crystal.get(1),
        Materials.Nitrogen.getGas(1),
        ItemList.Crude_Silicon_Boule,
        64,
        8_000,
        1,
        2_500,
        VoltageIndex.MV,
        120,
        240 * GTRecipeBuilder.SECONDS,
        HeatingCoilLevel.MV);

    public static final GrowthRecipe MICROWAVED_SILICON = new GrowthRecipe(
        "microwaved_silicon",
        ItemList.Microwaved_Silicon_Dust.get(1),
        ItemList.Microwaved_Silicon_Seed_Crystal.get(1),
        Materials.Helium.getGas(1),
        ItemList.Crude_Microwaved_Silicon_Boule,
        64,
        8_000,
        1,
        2_500,
        VoltageIndex.HV,
        480,
        360 * GTRecipeBuilder.SECONDS,
        HeatingCoilLevel.MV);

    public static final GrowthRecipe ALGAAS = new GrowthRecipe(
        "algaas",
        ItemList.AlGaAs_Dust.get(1),
        ItemList.AlGaAs_Seed_Crystal.get(1),
        Materials.UUMatter.getFluid(1),
        ItemList.Crude_AlGaAs_Boule,
        64,
        8_000,
        2,
        10_000,
        VoltageIndex.UEV,
        7_864_320,
        60 * GTRecipeBuilder.SECONDS,
        HeatingCoilLevel.UIV);

    public static final List<GrowthRecipe> ALL = List.of(SILICON, MICROWAVED_SILICON, ALGAAS);

    public CZPullerRecipes() {}

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Silicon.getDust(64), ItemList.Silicon_Seed_Crystal.get(1))
            .fluidInputs(Materials.Nitrogen.getGas(8_000))
            .itemOutputs(ItemList.Crude_Silicon_Boule.get(1))
            .duration(240 * GTRecipeBuilder.SECONDS)
            .eut(120)
            .metadata(STRUCTURE_TIER, 1)
            .metadata(COIL_HEAT, 2_500)
            .addTo(czPullerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Microwaved_Silicon_Dust.get(64), ItemList.Microwaved_Silicon_Seed_Crystal.get(1))
            .fluidInputs(Materials.Helium.getGas(8_000))
            .itemOutputs(ItemList.Crude_Microwaved_Silicon_Boule.get(1))
            .duration(360 * GTRecipeBuilder.SECONDS)
            .eut(480)
            .metadata(STRUCTURE_TIER, 1)
            .metadata(COIL_HEAT, 2_500)
            .addTo(czPullerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.AlGaAs_Dust.get(64), ItemList.AlGaAs_Seed_Crystal.get(1))
            .fluidInputs(Materials.UUMatter.getFluid(8_000))
            .itemOutputs(ItemList.Crude_AlGaAs_Boule.get(1))
            .duration(60 * GTRecipeBuilder.SECONDS)
            .eut(7_864_320)
            .metadata(STRUCTURE_TIER, 2)
            .metadata(COIL_HEAT, 10_000)
            .addTo(czPullerRecipes);
    }

    public static final class GrowthRecipe {

        public final String id;
        public final ItemStack materialDust;
        public final ItemStack seed;
        public final FluidStack growthFluid;
        public final ItemList bouleOutput;
        public final int materialAmount;
        public final int gasAmountMb;
        public final int requiredStructureTier;
        public final int targetTemperature;
        public final int energyTier;
        public final long eut;
        public final int durationTicks;
        public final HeatingCoilLevel minimumCoil;

        private GrowthRecipe(String id, ItemStack materialDust, ItemStack seed, FluidStack growthFluid,
            ItemList bouleOutput, int materialAmount, int gasAmountMb, int requiredStructureTier, int targetTemperature,
            int energyTier, long eut, int durationTicks, HeatingCoilLevel minimumCoil) {
            this.id = id;
            this.materialDust = materialDust.copy();
            this.seed = seed.copy();
            this.growthFluid = growthFluid.copy();
            this.bouleOutput = bouleOutput;
            this.materialAmount = materialAmount;
            this.gasAmountMb = gasAmountMb;
            this.requiredStructureTier = requiredStructureTier;
            this.targetTemperature = targetTemperature;
            this.energyTier = energyTier;
            this.eut = eut;
            this.durationTicks = durationTicks;
            this.minimumCoil = minimumCoil;
        }

        public FluidStack growthFluid(int amountMb) {
            return new FluidStack(growthFluid.getFluid(), amountMb);
        }

        public ItemStack materialDust(int amount) {
            ItemStack result = materialDust.copy();
            result.stackSize = amount;
            return result;
        }

        public ItemStack output(int amount) {
            return bouleOutput.get(amount);
        }
    }
}
