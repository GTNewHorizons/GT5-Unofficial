package goodgenerator.util;

import codechicken.nei.PositionedStack;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

public class MyRecipeAdder {

    public static final MyRecipeAdder instance = new MyRecipeAdder();

    public final liquidMentalFuelMapper NqGFuels = new liquidMentalFuelMapper(
            new HashSet<>(50),
            "gg.recipe.naquadah_reactor",
            StatCollector.translateToLocal("tile.recipe.naquadah_reactor"),
            null,
            "goodgenerator:textures/gui/naquadah_reactor",
            0,
            0,
            0,
            1,
            1,
            StatCollector.translateToLocal("value.naquadah_reactor") + " ",
            1,
            " EU/t",
            false,
            true);

    public final NaqFuelRefineMapper FRF = new NaqFuelRefineMapper(
            new HashSet<>(50),
            "gg.recipe.naquadah_fuel_refine_factory",
            StatCollector.translateToLocal("tile.naquadah_fuel_refine_factory"),
            null,
            "gregtech:textures/gui/basicmachines/FusionReactor",
            6,
            0,
            0,
            1,
            1,
            StatCollector.translateToLocal("value.naquadah_fuel_refine_factory.0") + " ",
            1,
            StatCollector.translateToLocal("value.naquadah_fuel_refine_factory.1"),
            true,
            true);

    public final NeutronActivatorMapper NA = new NeutronActivatorMapper(
            new HashSet<>(150),
            "gg.recipe.neutron_activator",
            StatCollector.translateToLocal("tile.neutron_activator"),
            null,
            "goodgenerator:textures/gui/neutron_activator",
            9,
            9,
            0,
            0,
            0,
            null,
            0,
            null,
            false,
            false);

    public final ExtremeHeatExchangerMapper XHE = new ExtremeHeatExchangerMapper(
            new HashSet<>(50),
            "gg.recipe.extreme_heat_exchanger",
            StatCollector.translateToLocal("tile.extreme_heat_exchanger"),
            null,
            "goodgenerator:textures/gui/extreme_heat_exchanger",
            0,
            0,
            0,
            0,
            0,
            null,
            0,
            null,
            false,
            false);

    public final PreciseAssemblerMapper PA = new PreciseAssemblerMapper(
            new HashSet<>(120),
            "gg.recipe.precise_assembler",
            StatCollector.translateToLocal("tile.precise_assembler"),
            null,
            "goodgenerator:textures/gui/precise_assembler",
            4,
            1,
            1,
            0,
            1,
            StatCollector.translateToLocal("value.precise_assembler.0"),
            1,
            StatCollector.translateToLocal("value.precise_assembler.1"),
            true,
            false);

    public static class liquidMentalFuelMapper extends GT_Recipe.GT_Recipe_Map_Fuel {
        public liquidMentalFuelMapper(
                Collection<GT_Recipe> aRecipeList,
                String aUnlocalizedName,
                String aLocalName,
                String aNEIName,
                String aNEIGUIPath,
                int aUsualInputCount,
                int aUsualOutputCount,
                int aMinimalInputItems,
                int aMinimalInputFluids,
                int aAmperage,
                String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier,
                String aNEISpecialValuePost,
                boolean aShowVoltageAmperageInNEI,
                boolean aNEIAllowed) {
            super(
                    aRecipeList,
                    aUnlocalizedName,
                    aLocalName,
                    aNEIName,
                    aNEIGUIPath,
                    aUsualInputCount,
                    aUsualOutputCount,
                    aMinimalInputItems,
                    aMinimalInputFluids,
                    aAmperage,
                    aNEISpecialValuePre,
                    aNEISpecialValueMultiplier,
                    aNEISpecialValuePost,
                    aShowVoltageAmperageInNEI,
                    aNEIAllowed);
        }

        public void addFuel(FluidStack input, FluidStack output, int EUt, int ticks) {
            super.addRecipe(true, null, null, null, new FluidStack[] {input}, new FluidStack[] {output}, ticks, 0, EUt);
        }
    }

    public void addLiquidMentalFuel(FluidStack input, FluidStack output, int EUt, int ticks) {
        NqGFuels.addFuel(input, output, EUt, ticks);
    }

    public static class NaqFuelRefineMapper extends GT_Recipe.GT_Recipe_Map {
        public NaqFuelRefineMapper(
                Collection<GT_Recipe> aRecipeList,
                String aUnlocalizedName,
                String aLocalName,
                String aNEIName,
                String aNEIGUIPath,
                int aUsualInputCount,
                int aUsualOutputCount,
                int aMinimalInputItems,
                int aMinimalInputFluids,
                int aAmperage,
                String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier,
                String aNEISpecialValuePost,
                boolean aShowVoltageAmperageInNEI,
                boolean aNEIAllowed) {
            super(
                    aRecipeList,
                    aUnlocalizedName,
                    aLocalName,
                    aNEIName,
                    aNEIGUIPath,
                    aUsualInputCount,
                    aUsualOutputCount,
                    aMinimalInputItems,
                    aMinimalInputFluids,
                    aAmperage,
                    aNEISpecialValuePre,
                    aNEISpecialValueMultiplier,
                    aNEISpecialValuePost,
                    aShowVoltageAmperageInNEI,
                    aNEIAllowed);
        }

        public void addNaqFuelRefineRecipe(
                FluidStack[] input1, ItemStack[] input2, FluidStack output, int EUt, int ticks, int tier) {
            super.addRecipe(false, input2, null, null, input1, new FluidStack[] {output}, ticks, EUt, tier);
        }
    }

    public void addNaquadahFuelRefineRecipe(
            FluidStack[] input1, ItemStack[] input2, FluidStack output, int EUt, int ticks, int tier) {
        FRF.addNaqFuelRefineRecipe(input1, input2, output, EUt, ticks, tier);
    }

    public static class NeutronActivatorMapper extends GT_Recipe.GT_Recipe_Map {
        public NeutronActivatorMapper(
                Collection<GT_Recipe> aRecipeList,
                String aUnlocalizedName,
                String aLocalName,
                String aNEIName,
                String aNEIGUIPath,
                int aUsualInputCount,
                int aUsualOutputCount,
                int aMinimalInputItems,
                int aMinimalInputFluids,
                int aAmperage,
                String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier,
                String aNEISpecialValuePost,
                boolean aShowVoltageAmperageInNEI,
                boolean aNEIAllowed) {
            super(
                    aRecipeList,
                    aUnlocalizedName,
                    aLocalName,
                    aNEIName,
                    aNEIGUIPath,
                    aUsualInputCount,
                    aUsualOutputCount,
                    aMinimalInputItems,
                    aMinimalInputFluids,
                    aAmperage,
                    aNEISpecialValuePre,
                    aNEISpecialValueMultiplier,
                    aNEISpecialValuePost,
                    aShowVoltageAmperageInNEI,
                    aNEIAllowed);
        }

        public void addNARecipe(
                FluidStack[] input1,
                ItemStack[] input2,
                FluidStack[] output1,
                ItemStack[] output2,
                int ticks,
                int special) {
            super.addRecipe(false, input2, output2, null, input1, output1, ticks, 0, special);
        }
    }

    public void addNeutronActivatorRecipe(
            FluidStack[] input1,
            ItemStack[] input2,
            FluidStack[] output1,
            ItemStack[] output2,
            int ticks,
            int maxNKE,
            int minNKE) {
        if (maxNKE <= 0) maxNKE = 1;
        if (maxNKE >= 1100) maxNKE = 1100;
        if (minNKE < 0) minNKE = 0;
        if (minNKE >= maxNKE) minNKE = maxNKE - 1;
        NA.addNARecipe(input1, input2, output1, output2, ticks, maxNKE * 10000 + minNKE);
    }

    public static HashMap<Fluid, ExtremeHeatExchangerRecipe> mXHeatExchangerFuelMap = new HashMap<>();

    public static class ExtremeHeatExchangerMapper extends GT_Recipe.GT_Recipe_Map {
        public ExtremeHeatExchangerMapper(
                Collection<GT_Recipe> aRecipeList,
                String aUnlocalizedName,
                String aLocalName,
                String aNEIName,
                String aNEIGUIPath,
                int aUsualInputCount,
                int aUsualOutputCount,
                int aMinimalInputItems,
                int aMinimalInputFluids,
                int aAmperage,
                String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier,
                String aNEISpecialValuePost,
                boolean aShowVoltageAmperageInNEI,
                boolean aNEIAllowed) {
            super(
                    aRecipeList,
                    aUnlocalizedName,
                    aLocalName,
                    aNEIName,
                    aNEIGUIPath,
                    aUsualInputCount,
                    aUsualOutputCount,
                    aMinimalInputItems,
                    aMinimalInputFluids,
                    aAmperage,
                    aNEISpecialValuePre,
                    aNEISpecialValueMultiplier,
                    aNEISpecialValuePost,
                    aShowVoltageAmperageInNEI,
                    aNEIAllowed);
        }

        @Override
        public GT_Recipe addRecipe(
                boolean aOptimize,
                ItemStack[] aInputs,
                ItemStack[] aOutputs,
                Object aSpecial,
                int[] aOutputChances,
                FluidStack[] aFluidInputs,
                FluidStack[] aFluidOutputs,
                int aDuration,
                int aEUt,
                int aSpecialValue) {
            ExtremeHeatExchangerRecipe tRecipe =
                    new ExtremeHeatExchangerRecipe(aFluidInputs, aFluidOutputs, aSpecialValue);
            mXHeatExchangerFuelMap.put(aFluidInputs[0].getFluid(), tRecipe);
            return addRecipe(tRecipe);
        }
    }

    public static class ExtremeHeatExchangerRecipe extends GT_Recipe {

        public ExtremeHeatExchangerRecipe(FluidStack[] input, FluidStack[] output, int special) {
            super(false, null, null, null, null, input, output, 0, 0, special);
        }

        @Override
        public ArrayList<PositionedStack> getInputPositionedStacks() {
            ArrayList<PositionedStack> inputStacks = new ArrayList<>();
            if (this.mFluidInputs != null && this.mFluidInputs.length == 2) {
                inputStacks.add(
                        new PositionedStack(GT_Utility.getFluidDisplayStack(this.mFluidInputs[0], true), 22, 3));
                inputStacks.add(
                        new PositionedStack(GT_Utility.getFluidDisplayStack(this.mFluidInputs[1], true), 22, 27));
            }
            return inputStacks;
        }

        @Override
        public ArrayList<PositionedStack> getOutputPositionedStacks() {
            ArrayList<PositionedStack> outputStacks = new ArrayList<>();
            if (this.mFluidOutputs != null && this.mFluidOutputs.length == 3) {
                outputStacks.add(
                        new PositionedStack(GT_Utility.getFluidDisplayStack(this.mFluidOutputs[0], true), 124, 3));
                outputStacks.add(
                        new PositionedStack(GT_Utility.getFluidDisplayStack(this.mFluidOutputs[1], true), 124, 21));
                outputStacks.add(
                        new PositionedStack(GT_Utility.getFluidDisplayStack(this.mFluidOutputs[2], true), 124, 44));
            }
            return outputStacks;
        }

        public int getMaxHotFluidConsume() {
            if (this.mFluidInputs != null) {
                return this.mFluidInputs[0].amount;
            }
            return 0;
        }

        public Fluid getNormalSteam() {
            if (this.mFluidOutputs != null) {
                return this.mFluidOutputs[0].getFluid();
            }
            return null;
        }

        public Fluid getHeatedSteam() {
            if (this.mFluidOutputs != null) {
                return this.mFluidOutputs[1].getFluid();
            }
            return null;
        }

        public Fluid getCooledFluid() {
            if (this.mFluidOutputs != null) {
                return this.mFluidOutputs[2].getFluid();
            }
            return null;
        }

        public int getEUt() {
            if (getNormalSteam() != null) {
                switch (getNormalSteam().getName()) {
                    case "steam": {
                        int tVal = this.mFluidInputs[1].amount * 4;
                        if (tVal < 0) tVal = -tVal;
                        return tVal;
                    }
                    case "ic2superheatedsteam": {
                        int tVal = this.mFluidInputs[1].amount * 8;
                        if (tVal < 0) tVal = -tVal;
                        return tVal;
                    }
                    case "supercriticalsteam": {
                        int tVal = this.mFluidInputs[1].amount * 800;
                        if (tVal < 0) tVal = -tVal;
                        return tVal;
                    }
                    default:
                        return 0;
                }
            }
            return 0;
        }
    }

    public void addExtremeHeatExchangerRecipe(
            FluidStack HotFluid,
            FluidStack ColdFluid,
            FluidStack WorkFluid,
            FluidStack HeatedWorkFluid,
            FluidStack OverHeatedWorkFluid,
            int Threshold) {
        XHE.addRecipe(
                false,
                null,
                null,
                null,
                null,
                new FluidStack[] {HotFluid, WorkFluid},
                new FluidStack[] {HeatedWorkFluid, OverHeatedWorkFluid, ColdFluid},
                0,
                0,
                Threshold);
    }

    public static class PreciseAssemblerMapper extends GT_Recipe.GT_Recipe_Map {
        public PreciseAssemblerMapper(
                Collection<GT_Recipe> aRecipeList,
                String aUnlocalizedName,
                String aLocalName,
                String aNEIName,
                String aNEIGUIPath,
                int aUsualInputCount,
                int aUsualOutputCount,
                int aMinimalInputItems,
                int aMinimalInputFluids,
                int aAmperage,
                String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier,
                String aNEISpecialValuePost,
                boolean aShowVoltageAmperageInNEI,
                boolean aNEIAllowed) {
            super(
                    aRecipeList,
                    aUnlocalizedName,
                    aLocalName,
                    aNEIName,
                    aNEIGUIPath,
                    aUsualInputCount,
                    aUsualOutputCount,
                    aMinimalInputItems,
                    aMinimalInputFluids,
                    aAmperage,
                    aNEISpecialValuePre,
                    aNEISpecialValueMultiplier,
                    aNEISpecialValuePost,
                    aShowVoltageAmperageInNEI,
                    aNEIAllowed);
        }

        @Override
        public GT_Recipe addRecipe(
                boolean aOptimize,
                ItemStack[] aInputs,
                ItemStack[] aOutputs,
                Object aSpecial,
                int[] aOutputChances,
                FluidStack[] aFluidInputs,
                FluidStack[] aFluidOutputs,
                int aDuration,
                int aEUt,
                int aSpecialValue) {
            PreciseAssemblerRecipe tRecipe =
                    new PreciseAssemblerRecipe(aInputs, aFluidInputs, aOutputs[0], aEUt, aDuration, aSpecialValue);
            return addRecipe(tRecipe);
        }
    }

    public static class PreciseAssemblerRecipe extends GT_Recipe {
        public PreciseAssemblerRecipe(
                ItemStack[] input1, FluidStack[] input2, ItemStack output, int EUt, int ticks, int tier) {
            super(false, input1, new ItemStack[] {output}, null, null, input2, null, ticks, EUt, tier);
        }

        @Override
        public ArrayList<PositionedStack> getInputPositionedStacks() {
            ArrayList<PositionedStack> inputStacks = new ArrayList<>();
            if (this.mFluidInputs != null) {
                int index = 0;
                for (FluidStack inFluid : mFluidInputs) {
                    if (inFluid == null) continue;
                    inputStacks.add(
                            new PositionedStack(GT_Utility.getFluidDisplayStack(inFluid, true), 4 + index * 18, 38));
                    index++;
                }
            }
            if (this.mInputs != null) {
                int index = 0;
                for (ItemStack inItem : mInputs) {
                    if (inItem == null) continue;
                    inputStacks.add(new PositionedStack(inItem, 4 + index * 18, 3));
                    index++;
                }
            }
            return inputStacks;
        }

        @Override
        public ArrayList<PositionedStack> getOutputPositionedStacks() {
            ArrayList<PositionedStack> outputStacks = new ArrayList<>();
            if (this.mOutputs != null && this.mOutputs.length > 0)
                outputStacks.add(new PositionedStack(this.mOutputs[0], 111, 20));
            return outputStacks;
        }
    }

    public void addPreciseAssemblerRecipe(
            ItemStack[] aItemInputs, FluidStack[] aFluidInputs, ItemStack aOutput, int aEUt, int aDuration, int aTier) {
        if (aOutput == null) return;
        PA.addRecipe(
                false, aItemInputs, new ItemStack[] {aOutput}, null, null, aFluidInputs, null, aDuration, aEUt, aTier);
    }
}
