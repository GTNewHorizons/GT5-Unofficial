package goodgenerator.util;

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Supplier;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;

import goodgenerator.client.GUI.GG_UITextures;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.gui.modularui.UIHelper;

public class MyRecipeAdder {

    public static final MyRecipeAdder instance = new MyRecipeAdder();

    public final liquidMentalFuelMapper NqGFuels = (liquidMentalFuelMapper) new liquidMentalFuelMapper(
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
            true) {

        @Override
        public void addProgressBarUI(ModularWindow.Builder builder, Supplier<Float> progressSupplier,
                Pos2d windowOffset) {
            builder.widget(
                    new DrawableWidget().setDrawable(GG_UITextures.PICTURE_NAQUADAH_REACTOR)
                            .setPos(new Pos2d(59, 20).add(windowOffset)).setSize(58, 42));
        }
    }.useModularUI(true);

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

    public final NeutronActivatorMapper NA = (NeutronActivatorMapper) new NeutronActivatorMapper(
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
            true).setNEISpecialInfoFormatter((recipeInfo, applyPrefixAndSuffix) -> {
                int minNKE = recipeInfo.recipe.mSpecialValue % 10000;
                int maxNKE = recipeInfo.recipe.mSpecialValue / 10000;
                return Arrays.asList(
                        StatCollector.translateToLocal("value.neutron_activator.0"),
                        GT_Utility.formatNumbers(minNKE) + StatCollector.translateToLocal("value.neutron_activator.2"),
                        StatCollector.translateToLocal("value.neutron_activator.1"),
                        GT_Utility.formatNumbers(maxNKE) + StatCollector.translateToLocal("value.neutron_activator.2"));
            });

    public final ExtremeHeatExchangerMapper XHE = (ExtremeHeatExchangerMapper) new ExtremeHeatExchangerMapper(
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
            true).setNEISpecialInfoFormatter((recipeInfo, applyPrefixAndSuffix) -> {
                FluidStack[] Inputs = recipeInfo.recipe.mFluidInputs;
                FluidStack[] Outputs = recipeInfo.recipe.mFluidOutputs;
                int threshold = recipeInfo.recipe.mSpecialValue;
                return Arrays.asList(
                        StatCollector.translateToLocal("value.extreme_heat_exchanger.0") + " "
                                + GT_Utility.formatNumbers(Inputs[0].amount)
                                + " L/s",
                        StatCollector.translateToLocal("value.extreme_heat_exchanger.1"),
                        GT_Utility.formatNumbers(Outputs[0].amount / 160) + " L/s",
                        StatCollector.translateToLocal("value.extreme_heat_exchanger.2"),
                        GT_Utility.formatNumbers(Outputs[1].amount / 160) + " L/s",
                        StatCollector.translateToLocal("value.extreme_heat_exchanger.4") + " " + threshold + " L/s");
            });

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
            true);

    public static class liquidMentalFuelMapper extends GT_Recipe.GT_Recipe_Map_Fuel {

        public liquidMentalFuelMapper(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
                String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
                int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
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
            super.addRecipe(
                    true,
                    null,
                    null,
                    null,
                    new FluidStack[] { input },
                    new FluidStack[] { output },
                    ticks,
                    0,
                    EUt);
        }
    }

    public void addLiquidMentalFuel(FluidStack input, FluidStack output, int EUt, int ticks) {
        NqGFuels.addFuel(input, output, EUt, ticks);
    }

    public static class NaqFuelRefineMapper extends GT_Recipe.GT_Recipe_Map {

        public NaqFuelRefineMapper(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
                String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
                int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
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
            setUsualFluidInputCount(2);
        }

        public void addNaqFuelRefineRecipe(FluidStack[] input1, ItemStack[] input2, FluidStack output, int EUt,
                int ticks, int tier) {
            super.addRecipe(false, input2, null, null, input1, new FluidStack[] { output }, ticks, EUt, tier);
        }
    }

    public void addNaquadahFuelRefineRecipe(FluidStack[] input1, ItemStack[] input2, FluidStack output, int EUt,
            int ticks, int tier) {
        FRF.addNaqFuelRefineRecipe(input1, input2, output, EUt, ticks, tier);
    }

    public static class NeutronActivatorMapper extends GT_Recipe.GT_Recipe_Map {

        public NeutronActivatorMapper(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
                String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
                int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
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
            useModularUI(true);
        }

        public void addNARecipe(FluidStack[] input1, ItemStack[] input2, FluidStack[] output1, ItemStack[] output2,
                int ticks, int special) {
            super.addRecipe(false, input2, output2, null, input1, output1, ticks, 0, special);
        }

        @Override
        public void addProgressBarUI(ModularWindow.Builder builder, Supplier<Float> progressSupplier,
                Pos2d windowOffset) {
            builder.widget(
                    new DrawableWidget().setDrawable(GG_UITextures.PICTURE_NEUTRON_ACTIVATOR)
                            .setPos(new Pos2d(73, 22).add(windowOffset)).setSize(31, 21));
        }
    }

    public void addNeutronActivatorRecipe(FluidStack[] input1, ItemStack[] input2, FluidStack[] output1,
            ItemStack[] output2, int ticks, int maxNKE, int minNKE) {
        if (maxNKE <= 0) maxNKE = 1;
        if (maxNKE >= 1100) maxNKE = 1100;
        if (minNKE < 0) minNKE = 0;
        if (minNKE >= maxNKE) minNKE = maxNKE - 1;
        NA.addNARecipe(input1, input2, output1, output2, ticks, maxNKE * 10000 + minNKE);
    }

    public static HashMap<Fluid, ExtremeHeatExchangerRecipe> mXHeatExchangerFuelMap = new HashMap<>();

    public static class ExtremeHeatExchangerMapper extends GT_Recipe.GT_Recipe_Map {

        public ExtremeHeatExchangerMapper(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
                String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
                int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
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
            setUsualFluidInputCount(2);
            setUsualFluidOutputCount(3);
        }

        @Override
        public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial,
                int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
                int aSpecialValue) {
            ExtremeHeatExchangerRecipe tRecipe = new ExtremeHeatExchangerRecipe(
                    aFluidInputs,
                    aFluidOutputs,
                    aSpecialValue);
            mXHeatExchangerFuelMap.put(aFluidInputs[0].getFluid(), tRecipe);
            return addRecipe(tRecipe);
        }

        @Override
        public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
            return Arrays.asList(new Pos2d(26, 13), new Pos2d(26, 37));
        }

        @Override
        public List<Pos2d> getFluidOutputPositions(int fluidOutputCount) {
            return Arrays.asList(new Pos2d(128, 13), new Pos2d(128, 31), new Pos2d(128, 54));
        }

        @Override
        public void addProgressBarUI(ModularWindow.Builder builder, Supplier<Float> progressSupplier,
                Pos2d windowOffset) {
            builder.widget(
                    new DrawableWidget().setDrawable(GG_UITextures.PICTURE_EXTREME_HEAT_EXCHANGER)
                            .setPos(new Pos2d(47, 13).add(windowOffset)).setSize(78, 59));
        }
    }

    public static class ExtremeHeatExchangerRecipe extends GT_Recipe {

        public ExtremeHeatExchangerRecipe(FluidStack[] input, FluidStack[] output, int special) {
            super(false, null, null, null, null, input, output, 0, 0, special);
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

    public void addExtremeHeatExchangerRecipe(FluidStack HotFluid, FluidStack ColdFluid, FluidStack WorkFluid,
            FluidStack HeatedWorkFluid, FluidStack OverHeatedWorkFluid, int Threshold) {
        XHE.addRecipe(
                false,
                null,
                null,
                null,
                null,
                new FluidStack[] { HotFluid, WorkFluid },
                new FluidStack[] { HeatedWorkFluid, OverHeatedWorkFluid, ColdFluid },
                0,
                0,
                Threshold);
    }

    public static class PreciseAssemblerMapper extends GT_Recipe.GT_Recipe_Map {

        public PreciseAssemblerMapper(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName,
                String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
                int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
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
            setUsualFluidInputCount(4);
            setProgressBar(GT_UITextures.PROGRESSBAR_ARROW_MULTIPLE);
            setProgressBarPos(85, 30);
            setNEITransferRect(new Rectangle(80, 30, 35, 18));
        }

        @Override
        public GT_Recipe addRecipe(boolean aOptimize, ItemStack[] aInputs, ItemStack[] aOutputs, Object aSpecial,
                int[] aOutputChances, FluidStack[] aFluidInputs, FluidStack[] aFluidOutputs, int aDuration, int aEUt,
                int aSpecialValue) {
            PreciseAssemblerRecipe tRecipe = new PreciseAssemblerRecipe(
                    aInputs,
                    aFluidInputs,
                    aOutputs[0],
                    aEUt,
                    aDuration,
                    aSpecialValue);
            return addRecipe(tRecipe);
        }

        @Override
        public List<Pos2d> getItemInputPositions(int itemInputCount) {
            return UIHelper.getGridPositions(itemInputCount, 8, 13, itemInputCount);
        }

        @Override
        public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
            return Collections.singletonList(new Pos2d(115, 30));
        }

        @Override
        public List<Pos2d> getFluidInputPositions(int fluidInputCount) {
            return UIHelper.getGridPositions(fluidInputCount, 8, 48, fluidInputCount);
        }
    }

    public static class PreciseAssemblerRecipe extends GT_Recipe {

        public PreciseAssemblerRecipe(ItemStack[] input1, FluidStack[] input2, ItemStack output, int EUt, int ticks,
                int tier) {
            super(false, input1, new ItemStack[] { output }, null, null, input2, null, ticks, EUt, tier);
        }
    }

    public void addPreciseAssemblerRecipe(ItemStack[] aItemInputs, FluidStack[] aFluidInputs, ItemStack aOutput,
            int aEUt, int aDuration, int aTier) {
        if (aOutput == null) return;
        PA.addRecipe(
                false,
                aItemInputs,
                new ItemStack[] { aOutput },
                null,
                null,
                aFluidInputs,
                null,
                aDuration,
                aEUt,
                aTier);
    }

    public static class ComponentAssemblyLineMapper extends GT_Recipe.GT_Recipe_Map {

        public ComponentAssemblyLineMapper(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName,
                String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount,
                int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre,
                int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI,
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
            setUsualFluidInputCount(12);
            setNEITransferRect(new Rectangle(70, 15, 18, 54));
            setNEISpecialInfoFormatter(
                    (recipeInfo, applyPrefixAndSuffix) -> Collections.singletonList(
                            recipeInfo.recipeMap.mNEISpecialValuePre + GT_Values.VN[recipeInfo.recipe.mSpecialValue]));
        }

        @Override
        public List<Pos2d> getItemInputPositions(int itemInputCount) {
            return UIHelper.getGridPositions(itemInputCount, 16, 8, 3);
        }

        @Override
        public List<Pos2d> getItemOutputPositions(int itemOutputCount) {
            return Collections.singletonList(new Pos2d(142, 8));
        }

        @Override
        public List<Pos2d> getFluidInputPositions(int fluidInputCount) {

            return UIHelper.getGridPositions(fluidInputCount, 88, 26, 4);
        }

        @Override
        public void addGregTechLogoUI(ModularWindow.Builder builder, Pos2d windowOffset) {}

        @Override
        public void addProgressBarUI(ModularWindow.Builder builder, Supplier<Float> progressSupplier,
                Pos2d windowOffset) {
            builder.widget(
                    new DrawableWidget().setDrawable(GG_UITextures.PICTURE_COMPONENT_ASSLINE)
                            .setPos(new Pos2d(70, 11).add(windowOffset)).setSize(72, 40));
        }
    }

    public final ComponentAssemblyLineMapper COMPASSLINE_RECIPES = new ComponentAssemblyLineMapper(
            new HashSet<>(110),
            "gg.recipe.componentassemblyline",
            "Component Assembly Line",
            null,
            "goodgenerator:textures/gui/ComponentAssline",
            12,
            1,
            0,
            0,
            1,
            "Casing Tier: ",
            1,
            "",
            false,
            true);

    public GT_Recipe addComponentAssemblyLineRecipe(ItemStack[] ItemInputArray, FluidStack[] FluidInputArray,
            ItemStack OutputItem, int aDuration, int aEUt, int casingLevel) {
        return COMPASSLINE_RECIPES.addRecipe(
                false,
                ItemInputArray,
                new ItemStack[] { OutputItem },
                null,
                FluidInputArray,
                null,
                aDuration,
                aEUt,
                casingLevel);
    }
}
