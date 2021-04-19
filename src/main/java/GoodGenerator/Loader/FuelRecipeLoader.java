package GoodGenerator.Loader;

import GoodGenerator.util.MyRecipeAdder;
import gregtech.api.enums.Materials;

public class FuelRecipeLoader {
    public static void RegisterFuel(){
        MyRecipeAdder.instance.addLiquidMentalFuel(Materials.NaquadahEnriched.getMolten(1L),Materials.Naquadah.getMolten(1L),32768,1200);
        MyRecipeAdder.instance.addLiquidMentalFuel(Materials.Naquadria.getMolten(1L),Materials.Naquadah.getMolten(1L),262144,100);
    }

}
