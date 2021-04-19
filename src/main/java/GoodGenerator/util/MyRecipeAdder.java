package GoodGenerator.util;

import gregtech.api.util.GT_Recipe;
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
            "gregtech:textures/gui/basicmachines/Default",
            0,0,1,1,1,
            "Basic Output Voltage: ",
            1,
            " EU/t",
            false,
            true
    );

    public static class liquidMentalFuelMapper extends GT_Recipe.GT_Recipe_Map_Fuel{
        int lasting = 0;

        public liquidMentalFuelMapper(Collection<GT_Recipe> aRecipeList, String aUnlocalizedName, String aLocalName, String aNEIName, String aNEIGUIPath, int aUsualInputCount, int aUsualOutputCount, int aMinimalInputItems, int aMinimalInputFluids, int aAmperage, String aNEISpecialValuePre, int aNEISpecialValueMultiplier, String aNEISpecialValuePost, boolean aShowVoltageAmperageInNEI, boolean aNEIAllowed){
            super(aRecipeList, aUnlocalizedName, aLocalName, aNEIName, aNEIGUIPath, aUsualInputCount, aUsualOutputCount, aMinimalInputItems, aMinimalInputFluids, aAmperage, aNEISpecialValuePre, aNEISpecialValueMultiplier, aNEISpecialValuePost, aShowVoltageAmperageInNEI, aNEIAllowed);
        }

        public GT_Recipe addFuel(FluidStack input,FluidStack output,int EUt,int ticks){
            lasting = ticks;
            return super.addRecipe(true,null,null,null,new FluidStack[]{input},new FluidStack[]{output},ticks,0,EUt);
        }
    }

    public boolean addLiquidMentalFuel(FluidStack input,FluidStack output,int EUt,int ticks){
        return NqGFuels.addFuel(input,output,EUt,ticks)!=null;
    }
}
