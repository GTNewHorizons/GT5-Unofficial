package GoodGenerator.Loader;

import GoodGenerator.Items.MyMaterial;
import GoodGenerator.util.MyRecipeAdder;
import gregtech.api.enums.Materials;

public class FuelRecipeLoader {
    public static void RegisterFuel(){
        //MyRecipeAdder.instance.addLiquidMentalFuel(Materials.NaquadahEnriched.getMolten(1L),Materials.Naquadah.getMolten(1L),32768,100);
        MyRecipeAdder.instance.addLiquidMentalFuel(Materials.Naquadria.getMolten(1L),Materials.Naquadah.getMolten(1L),262144,120);
        MyRecipeAdder.instance.addLiquidMentalFuel(MyMaterial.uraniumBasedLiquidFuelExcited.getFluidOrGas(1),MyMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(1),12960,100);
        MyRecipeAdder.instance.addLiquidMentalFuel(MyMaterial.thoriumBasedLiquidFuelExcited.getFluidOrGas(1),MyMaterial.thoriumBasedLiquidFuelDepleted.getFluidOrGas(1),4320,500);
        MyRecipeAdder.instance.addLiquidMentalFuel(MyMaterial.plutoniumBasedLiquidFuelExcited.getFluidOrGas(1),MyMaterial.plutoniumBasedLiquidFuelDepleted.getFluidOrGas(1),32400,150);
        MyRecipeAdder.instance.addLiquidMentalFuel(MyMaterial.naquadahBasedFuelMkI.getFluidOrGas(1),MyMaterial.naquadahBasedFuelMkIDepleted.getFluidOrGas(1),288000,320);
        MyRecipeAdder.instance.addLiquidMentalFuel(MyMaterial.naquadahBasedFuelMkII.getFluidOrGas(1),MyMaterial.naquadahBasedFuelMkIIDepleted.getFluidOrGas(1),1276000,100);
    }
}
