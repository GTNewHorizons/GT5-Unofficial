package GoodGenerator.util;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class CrackRecipeAdder {
    static float[] coe1 = {1.25f,1.2f,1.1f,0.9f,0.85f,0.8f,0.75f};
    static float[] coe2 = {1.4f,1.25f,1.2f,0.8f,0.75f,0.7f,0.65f};
    static float[] coe3 = {1.6f,1.5f,1.45f,0.7f,0.6f,0.55f,0.45f};
    public static void crackerAdder(FluidStack inputFluid, FluidStack cracker, FluidStack[] outputFluids, ItemStack outputItem, int num, int EUt, int Duration){

        String name;
        FluidStack[] actOutput = new FluidStack[num];
        name = inputFluid.getFluid().getName().replaceAll(" ","");

        GT_Values.RA.addCrackingRecipe(1,inputFluid,cracker, FluidRegistry.getFluidStack("lightlycracked"+name,1000),(int)(Duration * 0.8),EUt);
        GT_Values.RA.addCrackingRecipe(2,inputFluid,cracker, FluidRegistry.getFluidStack("moderatelycracked"+name,1000),Duration,EUt);
        GT_Values.RA.addCrackingRecipe(3,inputFluid,cracker, FluidRegistry.getFluidStack("heavilycracked"+name,1000),(int)(Duration * 1.2),EUt);

        for ( int i = num - 1, j = 0; i >= 0; i --, j ++ ){
            Fluid tmp1 = outputFluids[i].getFluid();
            int tmp2 = (int)(outputFluids[i].amount * coe1[i]);
            actOutput[j] = new FluidStack(tmp1, tmp2);
        }

        GT_Values.RA.addUniversalDistillationRecipe(FluidRegistry.getFluidStack("lightlycracked"+name,1000),actOutput,outputItem,Duration / 2,EUt / 3);

        for ( int i = num - 1, j = 0; i >= 0; i --, j ++ ){
            Fluid tmp1 = outputFluids[i].getFluid();
            int tmp2 = (int)(outputFluids[i].amount * coe2[i]);
            actOutput[j] = new FluidStack(tmp1, tmp2);
        }

        GT_Values.RA.addUniversalDistillationRecipe(FluidRegistry.getFluidStack("moderatelycracked"+name,1000),actOutput,outputItem,Duration / 2,EUt / 3);

        for ( int i = num - 1, j = 0; i >= 0; i --, j ++ ){
            Fluid tmp1 = outputFluids[i].getFluid();
            int tmp2 = (int)(outputFluids[i].amount * coe3[i]);
            actOutput[j] = new FluidStack(tmp1, tmp2);
        }

        GT_Values.RA.addUniversalDistillationRecipe(FluidRegistry.getFluidStack("heavilycracked"+name,1000),actOutput,outputItem,Duration / 2,EUt / 3);
    }

    public static void addUniversalCircuitAssemblerRecipe(ItemStack[] inputs, ItemStack output, int solders, int duration, int EUt, boolean isClean) {
        GT_Values.RA.addCircuitAssemblerRecipe(inputs, Materials.SolderingAlloy.getMolten(solders), output, duration, EUt, isClean);
        GT_Values.RA.addCircuitAssemblerRecipe(inputs, Materials.Tin.getMolten(solders * 2), output, duration, EUt, isClean);
        GT_Values.RA.addCircuitAssemblerRecipe(inputs, Materials.Lead.getMolten(solders * 4), output, duration, EUt, isClean);
    }

    public static void addUniversalAssemblerRecipe(ItemStack[] inputs, ItemStack output, int solders, int duration, int EUt, boolean isClean) {
        GT_Values.RA.addAssemblerRecipe(inputs, Materials.SolderingAlloy.getMolten(solders), output, duration, EUt, isClean);
        GT_Values.RA.addAssemblerRecipe(inputs, Materials.Tin.getMolten(solders * 2), output, duration, EUt, isClean);
        GT_Values.RA.addAssemblerRecipe(inputs, Materials.Lead.getMolten(solders * 4), output, duration, EUt, isClean);
    }
}
