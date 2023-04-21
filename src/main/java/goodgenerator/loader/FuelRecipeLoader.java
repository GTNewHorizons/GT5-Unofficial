package goodgenerator.loader;

import static goodgenerator.main.GG_Config_Loader.NaquadahFuelTime;
import static goodgenerator.main.GG_Config_Loader.NaquadahFuelVoltage;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import goodgenerator.items.MyMaterial;
import goodgenerator.util.ItemRefer;
import goodgenerator.util.MyRecipeAdder;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class FuelRecipeLoader {

    public static void RegisterFuel() {
        // MyRecipeAdder.instance.addLiquidMentalFuel(Materials.NaquadahEnriched.getMolten(1L),Materials.Naquadah.getMolten(1L),32768,100);
        // MyRecipeAdder.instance.addLiquidMentalFuel(Materials.Naquadria.getMolten(1L),Materials.Naquadah.getMolten(1L),262144,120);
        MyRecipeAdder.instance.addLiquidMentalFuel(
                MyMaterial.uraniumBasedLiquidFuelExcited.getFluidOrGas(1),
                MyMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(1),
                NaquadahFuelVoltage[0],
                NaquadahFuelTime[0]);
        MyRecipeAdder.instance.addLiquidMentalFuel(
                MyMaterial.thoriumBasedLiquidFuelExcited.getFluidOrGas(1),
                MyMaterial.thoriumBasedLiquidFuelDepleted.getFluidOrGas(1),
                NaquadahFuelVoltage[1],
                NaquadahFuelTime[1]);
        MyRecipeAdder.instance.addLiquidMentalFuel(
                MyMaterial.plutoniumBasedLiquidFuelExcited.getFluidOrGas(1),
                MyMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(1),
                NaquadahFuelVoltage[2],
                NaquadahFuelTime[2]);
        MyRecipeAdder.instance.addLiquidMentalFuel(
                MyMaterial.naquadahBasedFuelMkI.getFluidOrGas(1),
                MyMaterial.naquadahBasedFuelMkIDepleted.getFluidOrGas(1),
                NaquadahFuelVoltage[3],
                NaquadahFuelTime[3]);
        MyRecipeAdder.instance.addLiquidMentalFuel(
                MyMaterial.naquadahBasedFuelMkII.getFluidOrGas(1),
                MyMaterial.naquadahBasedFuelMkIIDepleted.getFluidOrGas(1),
                NaquadahFuelVoltage[4],
                NaquadahFuelTime[4]);
        MyRecipeAdder.instance.addLiquidMentalFuel(
                MyMaterial.naquadahBasedFuelMkIII.getFluidOrGas(1),
                MyMaterial.naquadahBasedFuelMkIIIDepleted.getFluidOrGas(1),
                NaquadahFuelVoltage[5],
                NaquadahFuelTime[5]);
        MyRecipeAdder.instance.addLiquidMentalFuel(
                MyMaterial.naquadahBasedFuelMkIV.getFluidOrGas(1),
                MyMaterial.naquadahBasedFuelMkIVDepleted.getFluidOrGas(1),
                NaquadahFuelVoltage[6],
                NaquadahFuelTime[6]);
        MyRecipeAdder.instance.addLiquidMentalFuel(
                MyMaterial.naquadahBasedFuelMkV.getFluidOrGas(1),
                MyMaterial.naquadahBasedFuelMkVDepleted.getFluidOrGas(1),
                NaquadahFuelVoltage[7],
                NaquadahFuelTime[7]);

        MyRecipeAdder.instance.addNaquadahFuelRefineRecipe(
                new FluidStack[] { MyMaterial.heavyNaquadahFuel.getFluidOrGas(800),
                        MyMaterial.lightNaquadahFuel.getFluidOrGas(1000), },
                new ItemStack[] { MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 4),
                        WerkstoffLoader.Tiberium.get(OrePrefixes.dust, 27), ItemRefer.High_Density_Uranium.get(2),
                        ItemRefer.High_Density_Plutonium.get(1), },
                MyMaterial.naquadahBasedFuelMkIII.getFluidOrGas(100),
                1100000,
                100,
                1);

        MyRecipeAdder.instance.addNaquadahFuelRefineRecipe(
                new FluidStack[] { MyMaterial.naquadahBasedFuelMkIII.getFluidOrGas(2000),
                        Materials.Praseodymium.getMolten(9216L) },
                new ItemStack[] { MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 27),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherStar, 64),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64),
                        MyMaterial.orundum.get(OrePrefixes.dust, 32), },
                MyMaterial.naquadahBasedFuelMkIV.getFluidOrGas(250),
                46000000,
                160,
                2);

        // Alternate higher tier recipe
        MyRecipeAdder.instance.addNaquadahFuelRefineRecipe(
                new FluidStack[] { MyMaterial.naquadahBasedFuelMkIII.getFluidOrGas(2000),
                        new FluidStack(FluidRegistry.getFluid("molten.hypogen"), 1440) },
                new ItemStack[] { MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust, 27),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bedrockium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DraconiumAwakened, 64),
                        MyMaterial.orundum.get(OrePrefixes.dust, 64), },
                MyMaterial.naquadahBasedFuelMkIV.getFluidOrGas(500),
                75000000,
                160,
                2);

        MyRecipeAdder.instance.addNaquadahFuelRefineRecipe(
                new FluidStack[] { MyMaterial.naquadahBasedFuelMkIV.getFluidOrGas(2000),
                        FluidRegistry.getFluidStack("heavyradox", 1000), },
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Infinity, 16),
                        MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.dust, 32), },
                MyMaterial.naquadahBasedFuelMkV.getFluidOrGas(500),
                100000000,
                200,
                2);

        // Alternate higher tier recipe
        MyRecipeAdder.instance.addNaquadahFuelRefineRecipe(
                new FluidStack[] { MyMaterial.naquadahBasedFuelMkIV.getFluidOrGas(2000),
                        FluidRegistry.getFluidStack("heavyradox", 1000), },
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsUEVplus.SpaceTime, 8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsUEVplus.TranscendentMetal, 16),
                        MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.dust, 48), },
                MyMaterial.naquadahBasedFuelMkV.getFluidOrGas(750),
                300000000,
                200,
                3);
    }
}
