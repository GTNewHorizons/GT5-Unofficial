package GoodGenerator.Loader;

import gregtech.api.enums.*;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeLoader {
    public static void RecipeLoad(){
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Lanthanum, 16L),
                                GT_OreDictUnificator.get(OrePrefixes.plate,Materials.NaquadahAlloy,8L),
                                GT_OreDictUnificator.get(OrePrefixes.foil,Materials.Neutronium,1L)},
                Materials.Lead.getMolten(1152),
                new ItemStack(Loaders.radiationProtectionPlate),
                400,
                1919
        );

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
                114514
        );

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[]{
                        new ItemStack(Loaders.radiationProtectionPlate,6),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt,Materials.Europium,1),
                        ItemList.Field_Generator_MV.get(1)
                },
                null,
                new ItemStack(Loaders.MAR_Casing),
                400,
                1919
        );
    }
}
