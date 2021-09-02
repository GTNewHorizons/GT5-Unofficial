package GoodGenerator.Loader;

import GoodGenerator.Items.MyMaterial;
import GoodGenerator.util.CrackRecipeAdder;
import GoodGenerator.util.ItemRefer;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;

public class RecipeLoader_02 {

    public static void RecipeLoad(){

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel,1),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.BlueAlloy,1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorHV,32),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Beryllium,32),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite,1)
                },
                null,
                ItemRefer.Speeding_Pipe.get(1),
                300,
                1920
        );

        GT_Values.RA.addMixerRecipe(
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 34),
                Materials.Tin.getDust(5),
                Materials.Iron.getDust(2),
                Materials.Chrome.getDust(1),
                GT_Utility.getIntegratedCircuit(4),
                null, null, null,
                MyMaterial.zircaloy4.get(OrePrefixes.dust, 42),
                200,
                120
        );

        GT_Values.RA.addMixerRecipe(
                WerkstoffLoader.Zirconium.get(OrePrefixes.dust, 34),
                Materials.Tin.getDust(4),
                Materials.Iron.getDust(1),
                Materials.Chrome.getDust(1),
                Materials.Nickel.getDust(1),
                GT_Utility.getIntegratedCircuit(2),
                 null, null,
                MyMaterial.zircaloy2.get(OrePrefixes.dust, 41),
                200,
                120
        );

        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.zircaloy2, 500, 480, 2800, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.zircaloy2, 513, 480, 2800, false);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.zircaloy4, 500, 480, 2800, true);
        CrackRecipeAdder.reAddBlastRecipe(MyMaterial.zircaloy4, 513, 480, 2800, false);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        MyMaterial.zircaloy4.get(OrePrefixes.plate, 4),
                        MyMaterial.zircaloy2.get(OrePrefixes.ring, 2),
                        GT_Utility.getIntegratedCircuit(2)
                },
                null,
                ItemRefer.Advanced_Fuel_Rod.get(1),
                200,
                120
        );

        GT_Values.RA.addCannerRecipe(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                ItemRefer.High_Density_Uranium.get(1),
                ItemRefer.Fuel_Rod_U_1.get(1),
                null,
                400,
                120
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        ItemRefer.Fuel_Rod_U_1.get(2),
                        MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                        GT_Utility.getIntegratedCircuit(2)
                },
                null,
                ItemRefer.Fuel_Rod_U_2.get(1),
                200,
                1920
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        ItemRefer.Fuel_Rod_U_2.get(2),
                        MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                        GT_Utility.getIntegratedCircuit(5)
                },
                null,
                ItemRefer.Fuel_Rod_U_4.get(1),
                200,
                1920
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        ItemRefer.Fuel_Rod_U_1.get(4),
                        MyMaterial.zircaloy2.get(OrePrefixes.stickLong, 6),
                        GT_Utility.getIntegratedCircuit(4)
                },
                null,
                ItemRefer.Fuel_Rod_U_4.get(1),
                220,
                1920
        );

        GT_Values.RA.addCannerRecipe(
                ItemRefer.Advanced_Fuel_Rod.get(1),
                ItemRefer.High_Density_Plutonium.get(1),
                ItemRefer.Fuel_Rod_Pu_1.get(1),
                null,
                400,
                120
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        ItemRefer.Fuel_Rod_Pu_1.get(2),
                        MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                        GT_Utility.getIntegratedCircuit(2)
                },
                null,
                ItemRefer.Fuel_Rod_Pu_2.get(1),
                200,
                1920
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        ItemRefer.Fuel_Rod_Pu_2.get(2),
                        MyMaterial.zircaloy2.get(OrePrefixes.stick, 4),
                        GT_Utility.getIntegratedCircuit(5)
                },
                null,
                ItemRefer.Fuel_Rod_Pu_4.get(1),
                200,
                1920
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] {
                        ItemRefer.Fuel_Rod_Pu_1.get(4),
                        MyMaterial.zircaloy2.get(OrePrefixes.stickLong, 6),
                        GT_Utility.getIntegratedCircuit(4)
                },
                null,
                ItemRefer.Fuel_Rod_Pu_4.get(1),
                220,
                1920
        );
    }

    public static void InitLoadRecipe() {
        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_U_Depleted_1.get(1),
                null,
                null,
                WerkstoffLoader.Neon.getFluidOrGas(32),
                ItemRefer.Advanced_Fuel_Rod.get(1),
                Materials.Uranium.getDust(8),
                Materials.Plutonium.getDust(2),
                Materials.Graphite.getDust(8),
                Materials.Uranium235.getDust(1),
                Materials.Plutonium241.getDust(1),
                new int[]{10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920
        );

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_U_Depleted_2.get(1),
                null,
                null,
                WerkstoffLoader.Neon.getFluidOrGas(64),
                ItemRefer.Advanced_Fuel_Rod.get(2),
                Materials.Uranium.getDust(16),
                Materials.Plutonium.getDust(4),
                Materials.Graphite.getDust(16),
                Materials.Uranium235.getDust(2),
                Materials.Plutonium241.getDust(2),
                new int[]{10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920
        );

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_U_Depleted_4.get(1),
                null,
                null,
                WerkstoffLoader.Neon.getFluidOrGas(128),
                ItemRefer.Advanced_Fuel_Rod.get(4),
                Materials.Uranium.getDust(32),
                Materials.Plutonium.getDust(8),
                Materials.Graphite.getDust(32),
                Materials.Uranium235.getDust(4),
                Materials.Plutonium241.getDust(4),
                new int[]{10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920
        );

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_Pu_Depleted_1.get(1),
                null,
                null,
                WerkstoffLoader.Krypton.getFluidOrGas(32),
                ItemRefer.Advanced_Fuel_Rod.get(1),
                Materials.Plutonium.getDust(5),
                Materials.Plutonium241.getDust(2),
                Materials.Carbon.getDust(2),
                Materials.Uranium.getDust(1),
                Materials.Uranium235.getDust(1),
                new int[]{10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920
        );

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_Pu_Depleted_2.get(1),
                null,
                null,
                WerkstoffLoader.Krypton.getFluidOrGas(64),
                ItemRefer.Advanced_Fuel_Rod.get(2),
                Materials.Plutonium.getDust(10),
                Materials.Plutonium241.getDust(4),
                Materials.Carbon.getDust(4),
                Materials.Uranium.getDust(2),
                Materials.Uranium235.getDust(2),
                new int[]{10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920
        );

        GT_Values.RA.addCentrifugeRecipe(
                ItemRefer.Fuel_Rod_Pu_Depleted_4.get(1),
                null,
                null,
                WerkstoffLoader.Krypton.getFluidOrGas(128),
                ItemRefer.Advanced_Fuel_Rod.get(4),
                Materials.Plutonium.getDust(20),
                Materials.Plutonium241.getDust(8),
                Materials.Carbon.getDust(8),
                Materials.Uranium.getDust(4),
                Materials.Uranium235.getDust(4),
                new int[]{10000, 10000, 10000, 9000, 5000, 3000},
                200,
                1920
        );
    }
}
