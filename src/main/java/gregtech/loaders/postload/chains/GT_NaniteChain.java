package gregtech.loaders.postload.chains;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GT_NaniteChain {

    public static void run() {

        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Circuit_Crystalmainframe.get(1),
                144000,
                new ItemStack[] {
                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 16),
                    ItemList.Robot_Arm_UV.get(16),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Naquadria, 32),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.NaquadahAlloy, 16),
                    Materials.Carbon.getDust(64)
                },
                new FluidStack[] {new FluidStack(solderIndalloy, 144 * 8)},
                Materials.Carbon.getNanite(4),
                5 * 20,
                400000);

        GT_Values.RA.addNanoForgeRecipe(
                new ItemStack[] {Materials.Carbon.getDust(64)},
                new FluidStack[] {new FluidStack(FluidRegistry.getFluid("mutagen"), 1000)},
                new ItemStack[] {
                    Materials.Carbon.getNanite(8),
                    Materials.Carbon.getNanite(4),
                    Materials.Carbon.getNanite(2),
                    Materials.Carbon.getNanite(1)
                },
                null,
                new int[] {10000, 5000, 2500, 1000},
                25 * 20,
                100000000,
                1);
    }
}
