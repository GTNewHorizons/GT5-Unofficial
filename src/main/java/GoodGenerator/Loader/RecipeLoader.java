package GoodGenerator.Loader;

import GoodGenerator.Items.MyMaterial;
import com.github.bartimaeusnek.bartworks.system.material.BW_GT_MaterialReference;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import gregtech.api.util.GT_Utility;

public class RecipeLoader {
    public static void RecipeLoad(){

        //Radiation Protection Plate
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Lanthanum, 16L),
                                GT_OreDictUnificator.get(OrePrefixes.plate,Materials.NaquadahAlloy,8L),
                                GT_OreDictUnificator.get(OrePrefixes.foil,Materials.Neutronium,1L),
                                GT_Utility.getIntegratedCircuit(1)},
                Materials.Lead.getMolten(1152),
                new ItemStack(Loaders.radiationProtectionPlate),
                400,
                1920
        );

        //LNR Controller
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Generator_Naquadah_Mark_V.get(1).copy(),
                100000,
                new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Adamantium,8),
                        new ItemStack(Loaders.radiationProtectionPlate,16),
                        ItemList.Field_Generator_ZPM.get(2),
                        ItemList.Electric_Pump_UV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.circuit,Materials.Infinite,2),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt08,Materials.Tungsten,8),
                        GT_OreDictUnificator.get(OrePrefixes.pipeHuge,Materials.Lead,4),
                        GT_OreDictUnificator.get(OrePrefixes.plate,Materials.NaquadahAlloy,8),
                        GT_OreDictUnificator.get(OrePrefixes.screw,Materials.Osmium,16)
                },
                new FluidStack[]{
                        Materials.Trinium.getMolten(576),
                        Materials.SolderingAlloy.getMolten(4608),
                        Materials.Lubricant.getFluid(8000)
                },
                Loaders.MAR.copy(),
                16200,
                122880
        );

        //LNR Casing
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        new ItemStack(Loaders.radiationProtectionPlate,6),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt,Materials.Europium,1),
                        ItemList.Field_Generator_MV.get(1),
                        GT_Utility.getIntegratedCircuit(1)
                },
                null,
                new ItemStack(Loaders.MAR_Casing),
                400,
                1920
        );

        //Uranium Liquid Fuel Process Line
        GT_Values.RA.addAssemblerRecipe(
               new ItemStack[]{
                        MyMaterial.graphiteUraniumMixture.get(OrePrefixes.dust,4),
                        GT_OreDictUnificator.get(OrePrefixes.foil,Materials.TungstenCarbide,16),
                        GT_Utility.getIntegratedCircuit(1)
               },
                null,
                new ItemStack(Loaders.wrappedUraniumIngot),
                1400,
                480
        );

        GT_Values.RA.addImplosionRecipe(
                new ItemStack(Loaders.wrappedUraniumIngot, 4),
                8,
                new ItemStack(Loaders.highDensityUraniumNugget),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny,Materials.TungstenCarbide,8)
        );

        GT_Values.RA.addCompressorRecipe(
                new ItemStack(Loaders.highDensityUraniumNugget,9),
                new ItemStack(Loaders.highDensityUranium),
                600,
                480
        );

        GT_Values.RA.addMixerRecipe(
                new ItemStack(Loaders.highDensityUranium),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Rubidium,8),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quantium,4),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Radon.getGas(1000L),
                MyMaterial.uraniumBasedLiquidFuel.getFluidOrGas(1000),
                null,
                200,
                15360
        );

        GT_Values.RA.addFusionReactorRecipe(
                MyMaterial.uraniumBasedLiquidFuel.getFluidOrGas(10),
                Materials.Hydrogen.getGas(100L),
                MyMaterial.uraniumBasedLiquidFuelExcited.getFluidOrGas(10),
                40,
                7680,
                200000000
        );

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),null,
                MyMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(1000),
                WerkstoffLoader.Xenon.getFluidOrGas(2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Lead,16L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bismuth,1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Barium,6L),
                null,null,null,
                new int[]{6000,1000,5000},
                1000,1040
        );

        //Thorium Liquid Process Line
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        MyMaterial.uraniumCarbideThoriumMixture.get(OrePrefixes.dust,64),
                        GT_OreDictUnificator.get(OrePrefixes.foil,Materials.TungstenSteel,4),
                        GT_Utility.getIntegratedCircuit(1)
                },
                null,
                new ItemStack(Loaders.wrappedThoriumIngot),
                1000,
                480
        );

        GT_Values.RA.addImplosionRecipe(
                new ItemStack(Loaders.wrappedThoriumIngot),
                4,
                new ItemStack(Loaders.highDensityThoriumNugget),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny,Materials.TungstenSteel,3)
        );

        GT_Values.RA.addCompressorRecipe(
                new ItemStack(Loaders.highDensityThoriumNugget,9),
                new ItemStack(Loaders.highDensityThorium),
                400,
                120
        );

        GT_Values.RA.addMixerRecipe(
                new ItemStack(Loaders.highDensityThorium),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Lithium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Draconium,2),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Mercury.getFluid(1000L),
                MyMaterial.thoriumBasedLiquidFuel.getFluidOrGas(4000),
                null,
                3000,
                240
        );

        GT_Values.RA.addMixerRecipe(
                MyMaterial.thoriumBasedLiquidFuel.get(OrePrefixes.cell,1),
                GT_Utility.getIntegratedCircuit(1),
                null,null,
                Materials.Helium.getPlasma(750L),
                null,
                MyMaterial.thoriumBasedLiquidFuelExcited.get(OrePrefixes.cell,1),
                4500,
                3840
        );

        GT_Values.RA.addCentrifugeRecipe(
                GT_Utility.getIntegratedCircuit(1),null,
                MyMaterial.uraniumBasedLiquidFuelDepleted.getFluidOrGas(1000),
                null,
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Lutetium,8),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Lutetium,8),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Lutetium,4),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Praseodymium,1),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Boron,2),
                GT_OreDictUnificator.get(OrePrefixes.dust,Materials.Indium,4),
                new int[]{9000,7000,1500,800,3000,5000},
                1500,
                1040
        );
    }
}
