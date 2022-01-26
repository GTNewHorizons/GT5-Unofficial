package goodgenerator.util;

import com.github.bartimaeusnek.bartworks.system.material.Werkstoff;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
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

    public static void reAddBlastRecipe(Werkstoff material, int duration, int EUt, int level, boolean gas) {
        ItemStack input = material.get(OrePrefixes.dust, 1);
        ItemStack output = level > 1750 ? material.get(OrePrefixes.ingotHot, 1) : material.get(OrePrefixes.ingot, 1);
        if (gas) {
            GT_Values.RA.addBlastRecipe(input, GT_Utility.getIntegratedCircuit(11), Materials.Helium.getGas(1000), null, output, null, duration, EUt, level);
        } else {
            GT_Values.RA.addBlastRecipe(input, GT_Utility.getIntegratedCircuit(1), null, null, output, null, duration, EUt, level);
        }
    }

    public static FluidStack copyFluidWithAmount(FluidStack fluid, int amount) {
        if (fluid == null || amount <= 0) return null;
        return new FluidStack(fluid.getFluid(), amount);
    }

    public static void registerPipe(int ID, Werkstoff material, int flow, int temp, boolean gas) {
        String unName = material.getDefaultName().replace(" ", "_");
        String Name = material.getDefaultName();
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeTiny.get(material.getBridgeMaterial()), new GT_MetaPipeEntity_Fluid(ID, "GT_Pipe_" + unName + "_Tiny", "Tiny " + Name + " Fluid Pipe", 0.25F, 	material.getBridgeMaterial(), flow / 6, temp, gas).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeSmall.get(material.getBridgeMaterial()), new GT_MetaPipeEntity_Fluid(ID + 1, "GT_Pipe_" + unName + "_Small", "Small " + Name + " Fluid Pipe", 0.375F, material.getBridgeMaterial(), flow / 3, temp, gas).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeMedium.get(material.getBridgeMaterial()), new GT_MetaPipeEntity_Fluid(ID + 2, "GT_Pipe_" + unName, Name + " Fluid Pipe", 0.5F, material.getBridgeMaterial(), flow, temp, gas).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeLarge.get(material.getBridgeMaterial()), new GT_MetaPipeEntity_Fluid(ID + 3, "GT_Pipe_" + unName + "_Large", "Large " + Name + " Fluid Pipe", 0.75F, material.getBridgeMaterial(), flow * 2, temp, gas).getStackForm(1L));
        GT_OreDictUnificator.registerOre(OrePrefixes.pipeHuge.get(material.getBridgeMaterial()), new GT_MetaPipeEntity_Fluid(ID + 4, "GT_Pipe_" + unName + "_Huge", "Huge " + Name + " Fluid Pipe", 0.875F, material.getBridgeMaterial(), flow * 4, temp, gas).getStackForm(1L));
        GT_Values.RA.addExtruderRecipe(material.get(OrePrefixes.ingot, 1), ItemList.Shape_Extruder_Pipe_Tiny.get(0), material.get(OrePrefixes.pipeTiny, 2), (int) material.getStats().getMass(), 120);
        GT_Values.RA.addExtruderRecipe(material.get(OrePrefixes.ingot, 1), ItemList.Shape_Extruder_Pipe_Small.get(0), material.get(OrePrefixes.pipeSmall, 1), (int) material.getStats().getMass() * 2, 120);
        GT_Values.RA.addExtruderRecipe(material.get(OrePrefixes.ingot, 3), ItemList.Shape_Extruder_Pipe_Medium.get(0), material.get(OrePrefixes.pipeMedium, 1), (int) material.getStats().getMass() * 6, 120);
        GT_Values.RA.addExtruderRecipe(material.get(OrePrefixes.ingot, 6), ItemList.Shape_Extruder_Pipe_Large.get(0), material.get(OrePrefixes.pipeLarge, 1), (int) material.getStats().getMass() * 12, 120);
        GT_Values.RA.addExtruderRecipe(material.get(OrePrefixes.ingot, 12), ItemList.Shape_Extruder_Pipe_Huge.get(0), material.get(OrePrefixes.pipeHuge, 1), (int) material.getStats().getMass() * 24, 120);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Pipe_Tiny.get(0), material.getMolten(72), material.get(OrePrefixes.pipeTiny, 1), (int) material.getStats().getMass(), 30);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Pipe_Small.get(0), material.getMolten(144), material.get(OrePrefixes.pipeSmall, 1), (int) material.getStats().getMass() * 2, 30);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Pipe_Medium.get(0), material.getMolten(432), material.get(OrePrefixes.pipeMedium, 1), (int) material.getStats().getMass() * 6, 30);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Pipe_Large.get(0), material.getMolten(864), material.get(OrePrefixes.pipeLarge, 1), (int) material.getStats().getMass() * 12, 30);
        GT_Values.RA.addFluidSolidifierRecipe(ItemList.Shape_Mold_Pipe_Huge.get(0), material.getMolten(1728), material.get(OrePrefixes.pipeHuge, 1), (int) material.getStats().getMass() * 24, 30);
    }
}
