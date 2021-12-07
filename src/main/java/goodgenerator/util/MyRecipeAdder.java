package goodgenerator.util;

import gregtech.api.util.GT_Recipe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;

import java.util.Collection;
import java.util.HashSet;

public class MyRecipeAdder {

    public static final MyRecipeAdder instance = new MyRecipeAdder();

    public final liquidMentalFuelMapper NqGFuels = new liquidMentalFuelMapper(
            new HashSet<>(50),
            "gg.recipe.naquadah_reactor",
            StatCollector.translateToLocal("tile.recipe.naquadah_reactor"),
            null,
            "goodgenerator:textures/gui/naquadah_reactor",
            0,0,0,1,1,
            StatCollector.translateToLocal("value.naquadah_reactor") + " ",
            1,
            " EU/t",
            false,
            true
    );

    public final NaqFuelRefineMapper FRF = new NaqFuelRefineMapper(
            new HashSet<>(50),
            "gg.recipe.naquadah_fuel_refine_factory",
            StatCollector.translateToLocal("tile.naquadah_fuel_refine_factory"),
            null,
            "gregtech:textures/gui/basicmachines/FusionReactor",
            6,0, 0, 1, 1,
            StatCollector.translateToLocal("value.naquadah_fuel_refine_factory.0") + " ",
            1,
            StatCollector.translateToLocal("value.naquadah_fuel_refine_factory.1"),
            true,
            true
    );

    public final NeutronActivatorMapper NA = new NeutronActivatorMapper(
            new HashSet<>(150),
            "gg.recipe.neutron_activator",
            StatCollector.translateToLocal("tile.neutron_activator"),
            null,
            "goodgenerator:textures/gui/neutron_activator",
            6, 6, 0, 0, 0,
            null, 0, null,
            false,
            false
    );

    public static class liquidMentalFuelMapper extends GT_Recipe.GT_Recipe_Map_Fuel{
        public liquidMentalFuelMapper(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed){
            super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
        }

        public void addFuel(FluidStack input, FluidStack output, int EUt, int ticks){
            super.addRecipe(true, null, null, null, new FluidStack[]{input}, new FluidStack[]{output}, ticks, 0, EUt);
        }
    }

    public void addLiquidMentalFuel(FluidStack input, FluidStack output, int EUt, int ticks){
        NqGFuels.addFuel(input, output, EUt, ticks);
    }

    public static class NaqFuelRefineMapper extends GT_Recipe.GT_Recipe_Map{
        public NaqFuelRefineMapper(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed){
            super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
        }

        public void addNaqFuelRefineRecipe(FluidStack[] input1, ItemStack[] input2, FluidStack output, int EUt, int ticks, int tier){
            super.addRecipe(false, input2, null, null, input1, new FluidStack[]{output}, ticks, EUt, tier);
        }
    }

    public void addNaquadahFuelRefineRecipe(FluidStack[] input1, ItemStack[] input2, FluidStack output, int EUt, int ticks, int tier){
        FRF.addNaqFuelRefineRecipe(input1, input2, output, EUt, ticks, tier);
    }

    public static class NeutronActivatorMapper extends GT_Recipe.GT_Recipe_Map{
        public NeutronActivatorMapper(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed){
            super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
        }

        public void addNARecipe(FluidStack[] input1, ItemStack[] input2, FluidStack[] output1, ItemStack[] output2, int ticks, int special) {
            super.addRecipe(false, input2, output2, null, input1, output1, ticks, 0, special);
        }
    }

    public void addNeutronActivatorRecipe(FluidStack[] input1, ItemStack[] input2, FluidStack[] output1, ItemStack[] output2, int ticks, int maxNKE, int minNKE) {
        if (maxNKE <= 0) maxNKE = 1;
        if (maxNKE >= 1100) maxNKE = 1100;
        if (minNKE < 0) minNKE = 0;
        if (minNKE >= maxNKE) minNKE = maxNKE - 1;
        NA.addNARecipe(input1, input2, output1, output2, ticks, maxNKE * 10000 + minNKE);
    }
}
