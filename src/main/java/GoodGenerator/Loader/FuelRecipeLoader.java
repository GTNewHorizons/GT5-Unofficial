package GoodGenerator.Loader;

import GoodGenerator.Items.MyMaterial;
import GoodGenerator.util.ItemRefer;
import GoodGenerator.util.MyRecipeAdder;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class FuelRecipeLoader {
    public static void RegisterFuel(){
        //MyRecipeAdder.instance.addLiquidMentalFuel(Materials.NaquadahEnriched.getMolten(1L),Materials.Naquadah.getMolten(1L),32768,100);
        //MyRecipeAdder.instance.addLiquidMentalFuel(Materials.Naquadria.getMolten(1L),Materials.Naquadah.getMolten(1L),262144,120);
        MyRecipeAdder.instance.addLiquidMentalFuel(MyMaterial.uraniumBasedLiquidFuelExcited.getFluidOrGas(1),MyMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(1),12960,100);
        MyRecipeAdder.instance.addLiquidMentalFuel(MyMaterial.thoriumBasedLiquidFuelExcited.getFluidOrGas(1),MyMaterial.thoriumBasedLiquidFuelDepleted.getFluidOrGas(1),9800,200);
        MyRecipeAdder.instance.addLiquidMentalFuel(MyMaterial.plutoniumBasedLiquidFuelExcited.getFluidOrGas(1),MyMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(1),32400,150);
        MyRecipeAdder.instance.addLiquidMentalFuel(MyMaterial.naquadahBasedFuelMkI.getFluidOrGas(1),MyMaterial.naquadahBasedFuelMkIDepleted.getFluidOrGas(1),220000,20);
        MyRecipeAdder.instance.addLiquidMentalFuel(MyMaterial.naquadahBasedFuelMkII.getFluidOrGas(1),MyMaterial.naquadahBasedFuelMkIIDepleted.getFluidOrGas(1),380000,20);
        MyRecipeAdder.instance.addLiquidMentalFuel(MyMaterial.naquadahBasedFuelMkIII.getFluidOrGas(1),MyMaterial.naquadahBasedFuelMkIIIDepleted.getFluidOrGas(1),8511000,60);
        MyRecipeAdder.instance.addLiquidMentalFuel(MyMaterial.naquadahBasedFuelMkIV.getFluidOrGas(1),MyMaterial.naquadahBasedFuelMkIVDepleted.getFluidOrGas(1),88540000,80);
        MyRecipeAdder.instance.addLiquidMentalFuel(MyMaterial.naquadahBasedFuelMkV.getFluidOrGas(1),MyMaterial.naquadahBasedFuelMkVDepleted.getFluidOrGas(1),389576000,100);

        MyRecipeAdder.instance.addNaquadahFuelRefineRecipe(
                new FluidStack[]{
                        MyMaterial.heavyNaquadahFuel.getFluidOrGas(800),
                        MyMaterial.lightNaquadahFuel.getFluidOrGas(1000),
                },
                new ItemStack[]{
                        MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust,4),
                        WerkstoffLoader.Tiberium.get(OrePrefixes.dust,27),
                        ItemRefer.High_Density_Uranium.get(16),
                        ItemRefer.High_Density_Plutonium.get(3),
                },
                MyMaterial.naquadahBasedFuelMkIII.getFluidOrGas(100),
                1900000,
                1800,
                1
        );

        MyRecipeAdder.instance.addNaquadahFuelRefineRecipe(
                new FluidStack[]{
                        MyMaterial.naquadahBasedFuelMkIII.getFluidOrGas(2000),
                        Materials.Praseodymium.getMolten(9216L)
                },
                new ItemStack[]{
                        MyMaterial.extremelyUnstableNaquadah.get(OrePrefixes.dust,27),
                        GT_OreDictUnificator.get(OrePrefixes.dust,Materials.NetherStar,64),
                        GT_OreDictUnificator.get(OrePrefixes.dust,Materials.DraconiumAwakened,64),
                        MyMaterial.orundum.get(OrePrefixes.dust,32),
                },
                MyMaterial.naquadahBasedFuelMkIV.getFluidOrGas(250),
                56000000,
                700,
                2
        );

        MyRecipeAdder.instance.addNaquadahFuelRefineRecipe(
                new FluidStack[]{
                        MyMaterial.naquadahBasedFuelMkIV.getFluidOrGas(2000),
                        FluidRegistry.getFluidStack("heavyradox",5000),
                },
                new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Infinity,64),
                        MyMaterial.atomicSeparationCatalyst.get(OrePrefixes.dust, 64),
                },
                MyMaterial.naquadahBasedFuelMkV.getFluidOrGas(500),
                140000000,
                800,
                2
        );
    }
}
