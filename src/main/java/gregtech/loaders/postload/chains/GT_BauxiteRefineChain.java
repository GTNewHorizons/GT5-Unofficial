package gregtech.loaders.postload.chains;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GT_BauxiteRefineChain {

    public static void run() {

        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Bauxite, 32),
                Materials.SodiumHydroxide.getDust(9),
                Materials.Quicklime.getDust(4),
                GT_Utility.getIntegratedCircuit(8),
                Materials.Water.getFluid(5000),
                MaterialsOreAlum.BauxiteSlurry.getFluid(8000),
                GT_Values.NI,
                200,
                120);

        GT_Values.RA.addMixerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Bauxite, 32),
                Materials.SodiumHydroxide.getDust(9),
                Materials.Quicklime.getDust(4),
                GT_Utility.getIntegratedCircuit(8),
                Materials.Water.getFluid(5000),
                MaterialsOreAlum.BauxiteSlurry.getFluid(8000),
                GT_Values.NI,
                200,
                120);

        GT_Values.RA.addCrackingRecipe(
                1,
                MaterialsOreAlum.BauxiteSlurry.getFluid(32000),
                GT_ModHandler.getSteam(2000),
                MaterialsOreAlum.HeatedBauxiteSlurry.getFluid(32000),
                160,
                1800);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] {
                    Materials.Aluminiumhydroxide.getDust(1),
                },
                new FluidStack[] {
                    Materials.CarbonDioxide.getGas(5000), MaterialsOreAlum.HeatedBauxiteSlurry.getFluid(8000)
                },
                new FluidStack[] {
                    MaterialsOreAlum.SluiceJuice.getFluid(5000),
                },
                new ItemStack[] {
                    Materials.Aluminiumoxide.getDust(64),
                    Materials.Aluminiumoxide.getDust(16),
                    Materials.SodiumCarbonate.getDust(9),
                    Materials.Calcite.getDust(10),
                    MaterialsOreAlum.BauxiteSlag.getDust(16)
                },
                300,
                480);

        GT_Values.RA.addCentrifugeRecipe(
                MaterialsOreAlum.BauxiteSlag.getDust(1),
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                Materials.Rutile.getDust(1),
                Materials.Gallium.getDust(1),
                Materials.Quicklime.getDust(1),
                Materials.SiliconDioxide.getDust(1),
                Materials.Iron.getDust(1),
                GT_Values.NI,
                new int[] {8000, 6000, 2000, 9000, 8000},
                40,
                120);

        GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Ilmenite, 1),
                Materials.SulfuricAcid.getFluid(1000),
                new FluidStack(ItemList.sGreenVitriol, 2000),
                Materials.Rutile.getDust(1),
                MaterialsOreAlum.IlmeniteSlag.getDust(1),
                GT_Values.NI,
                new int[] {8500, 3000},
                420,
                1000);

        GT_Values.RA.addChemicalBathRecipe(
                GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Ilmenite, 1),
                Materials.SulfuricAcid.getFluid(1000),
                new FluidStack(ItemList.sGreenVitriol, 2000),
                Materials.Rutile.getDust(1),
                MaterialsOreAlum.IlmeniteSlag.getDust(1),
                GT_Values.NI,
                new int[] {8500, 6000},
                420,
                1000);

        GT_Values.RA.addCentrifugeRecipe(
                MaterialsOreAlum.IlmeniteSlag.getDust(1),
                GT_Values.NI,
                GT_Values.NF,
                GT_Values.NF,
                Materials.Iron.getDust(1),
                Materials.Niobium.getDust(1),
                Materials.Tantalum.getDust(1),
                Materials.Manganese.getDust(1),
                Materials.Magnesium.getDust(1),
                GT_Values.NI,
                new int[] {8000, 1000, 2000, 5000, 6000},
                40,
                120);
    }
}
