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

        Fluid mutagen = FluidRegistry.getFluid("mutagen") != null
                ? FluidRegistry.getFluid("mutagen")
                : Materials.Radon.getGas(1).getFluid();

        Fluid solderLuV = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");

        Fluid growthCatalyst = Materials.GrowthMediumSterilized.getFluid(1).getFluid();

        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Circuit_Crystalmainframe.get(1),
                144000,
                new Object[] {
                    new Object[] {GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.SuperconductorUHV, 16)},
                    ItemList.Robot_Arm_UV.get(16),
                    ItemList.Circuit_Chip_Stemcell.get(32),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 32),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.NaquadahAlloy, 16),
                    Materials.Carbon.getDust(64)
                },
                new FluidStack[] {new FluidStack(solderLuV, 144 * 12)},
                Materials.Carbon.getNanite(2),
                5 * 20,
                400000);

        GT_Values.RA.addNanoForgeRecipe(
                new ItemStack[] {
                    Materials.Carbon.getDust(16),
                    ItemList.Circuit_Chip_SoC.get(4),
                    ItemList.Circuit_Chip_Stemcell.get(32)
                },
                new FluidStack[] {new FluidStack(mutagen, 1000)},
                new ItemStack[] {
                    Materials.Carbon.getNanite(16),
                },
                null,
                null,
                25 * 20,
                10000000,
                1);
        GT_Values.RA.addNanoForgeRecipe(
                new ItemStack[] {
                    Materials.Silver.getDust(64),
                    ItemList.Circuit_Chip_SoC.get(16),
                    ItemList.Circuit_Chip_Stemcell.get(64)
                },
                new FluidStack[] {new FluidStack(mutagen, 2000)},
                new ItemStack[] {Materials.Silver.getNanite(4)},
                null,
                null,
                50 * 20,
                10000000,
                1);
        GT_Values.RA.addNanoForgeRecipe(
                new ItemStack[] {
                    Materials.Neutronium.getDust(64),
                    ItemList.Circuit_Chip_SoC2.get(32),
                    ItemList.Circuit_Chip_Biocell.get(64)
                },
                new FluidStack[] {new FluidStack(growthCatalyst, 2000)},
                new ItemStack[] {Materials.Neutronium.getNanite(1)},
                null,
                null,
                100 * 20,
                100000000,
                1);
        GT_Values.RA.addNanoForgeRecipe(
                new ItemStack[] {
                    Materials.Copper.getDust(64),
                    ItemList.Circuit_Chip_SoC.get(16),
                    ItemList.Circuit_Chip_Stemcell.get(64)
                },
                new FluidStack[] {new FluidStack(growthCatalyst, 2000)},
                new ItemStack[] {Materials.Copper.getNanite(64), Materials.Copper.getNanite(64)},
                null,
                null,
                100 * 20,
                10000000,
                2);
    }
}
