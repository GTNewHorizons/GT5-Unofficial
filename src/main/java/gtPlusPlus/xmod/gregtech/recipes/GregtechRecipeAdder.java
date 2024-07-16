package gtPlusPlus.xmod.gregtech.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.SemiFluidFuelHandler;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.IGregtech_RecipeAdder;
import gtPlusPlus.xmod.gregtech.common.helpers.FlotationRecipeHandler;

import static gregtech.api.util.GT_RecipeConstants.RTG_DURATION_IN_DAYS;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.flotationCellRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.millingRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.rtgFuels;

public class GregtechRecipeAdder implements IGregtech_RecipeAdder {

    @Override
    public boolean addSemifluidFuel(ItemStack aFuelItem, int aFuelValue) {
        return SemiFluidFuelHandler.addSemiFluidFuel(aFuelItem, aFuelValue);
    }

    @Override
    public boolean addSemifluidFuel(FluidStack aFuelItem, int aFuelValue) {
        return SemiFluidFuelHandler.addSemiFluidFuel(aFuelItem, aFuelValue);
    }

    @Override
    public boolean addMillingRecipe(Material aMat, int aEU) {

        ItemStack aOreStack = aMat.getOre(16);
        ItemStack aCrushedStack = aMat.getCrushed(16);

        ItemStack aMilledStackOres1 = aMat.getMilled(64);
        ItemStack aMilledStackCrushed1 = aMat.getMilled(32);
        ItemStack aMilledStackOres2 = aMat.getMilled(48);
        ItemStack aMilledStackCrushed2 = aMat.getMilled(16);

        ItemStack aMillingBall_Alumina = GregtechItemList.Milling_Ball_Alumina.get(0);
        ItemStack aMillingBall_Soapstone = GregtechItemList.Milling_Ball_Soapstone.get(0);

        // Inputs
        ItemStack[] aInputsOre1 = new ItemStack[] { GT_Utility.getIntegratedCircuit(10), aOreStack,
            aMillingBall_Alumina };

        ItemStack[] aInputsOre2 = new ItemStack[] { GT_Utility.getIntegratedCircuit(11), aOreStack,
            aMillingBall_Soapstone };

        ItemStack[] aInputsCrushed1 = new ItemStack[] { GT_Utility.getIntegratedCircuit(10), aCrushedStack,
            aMillingBall_Alumina };

        ItemStack[] aInputsCrushed2 = new ItemStack[] { GT_Utility.getIntegratedCircuit(11), aCrushedStack,
            aMillingBall_Soapstone };

        // Outputs
        ItemStack[] aOutputsOre1 = new ItemStack[] { aMilledStackOres1 };

        ItemStack[] aOutputsOre2 = new ItemStack[] { aMilledStackOres2 };

        ItemStack[] aOutputsCrushed1 = new ItemStack[] { aMilledStackCrushed1 };

        ItemStack[] aOutputsCrushed2 = new ItemStack[] { aMilledStackCrushed2 };

        ItemStack[][] aInputArray = new ItemStack[][] { aInputsOre1, aInputsOre2, aInputsCrushed1, aInputsCrushed2 };
        ItemStack[][] aOutputArray = new ItemStack[][] { aOutputsOre1, aOutputsOre2, aOutputsCrushed1,
            aOutputsCrushed2 };
        int[] aTime = new int[] { 2400, 3000, 1200, 1500 };

        for (int i = 0; i < 4; i++) {
            GT_Values.RA.stdBuilder()
                .itemInputs(aInputArray[i])
                .itemOutputs(aOutputArray[i])
                .duration(aTime[i])
                .eut(aEU)
                .noOptimize()
                .addTo(millingRecipes);
        }
        return true;
    }

    @Override
    public boolean addFlotationRecipe(Materials aMat, ItemStack aXanthate, FluidStack[] aInputFluids,
        FluidStack[] aOutputFluids, int aTime, int aEU) {
        return addFlotationRecipe(
            MaterialUtils.generateMaterialFromGtENUM(aMat),
            aXanthate,
            aInputFluids,
            aOutputFluids,
            aTime,
            aEU);
    }

    @Override
    public boolean addFlotationRecipe(Material aMat, ItemStack aXanthate, FluidStack[] aInputFluids,
        FluidStack[] aOutputFluids, int aTime, int aEU) {

        FlotationRecipeHandler.registerOreType(aMat);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemUtils.getSimpleStack(aXanthate, 32),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64),
                aMat.getMilled(64)
            )
            .fluidInputs(aInputFluids)
            .fluidOutputs(aOutputFluids)
            .duration(aTime)
            .eut(aEU)
            .noOptimize()
            .addTo(flotationCellRecipes);
        return true;
    }

    @Override
    @Deprecated
    public boolean addFuelForRTG(ItemStack aFuelPellet, int aFuelDays, int aVoltage) {
        GT_Values.RA.stdBuilder()
            .itemInputs(aFuelPellet)
            .duration(0)
            .eut(aVoltage)
            .metadata(RTG_DURATION_IN_DAYS, aFuelDays)
            .addTo(rtgFuels);
        return true;
    }
}
