package gregtech.loaders.postload.chains;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GT_NaniteChain {

    public static void run() {

        ItemStack aUVTierLens = GT_ModHandler.getModItem("dreamcrtaft", "MysteriousCrystalLens", 0);
        ItemStack aUHVTierLens = GT_OreDictUnificator.get(OrePrefixes.lens, Materials.get("ChromaticGlass"), 0, false);
        ItemStack aUEVTierLens = GT_OreDictUnificator.get(OrePrefixes.lens, Materials.get("RadoxPoly"), 0, false);
        ItemStack aUIVTierLens = ItemList.EnergisedTesseract.get(0);
        ItemStack aUMVTierLens = GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Dilithium, 0, false);

        // Carbon Nanite Recipe Before Nano Forge
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Circuit_Crystalmainframe.get(1),
                144000,
                new Object[] {
                    new Object[] {GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Superconductor, 16)},
                    ItemList.Robot_Arm_UV.get(16),
                    ItemList.Circuit_Chip_Stemcell.get(32),
                    GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NaquadahAlloy, 32),
                    GT_OreDictUnificator.get(OrePrefixes.stick, Materials.NaquadahAlloy, 16),
                    Materials.Carbon.getDust(64)
                },
                new FluidStack[] {Materials.UUMatter.getFluid(10000)},
                Materials.Carbon.getNanite(2),
                50 * 20,
                400000);

        /*
         * General Rules for making nanite recipes:
         * 1. Never make a nanite that takes a long time to make and only gives 1, just to be consumed.
         * 2. Nanites meant to be consumed should either have a short duration or a big output.
         * 3. Nanites which aren't consumed should have a long duration and output less than 16.
         * 4. Nanites should always take UUM as a fluid and a lot of power to make.
         */

        // Carbon Nanites - Used to make more Nano Forge Controllers
        GT_Values.RA.addNanoForgeRecipe(
                new ItemStack[] {
                    GT_ModHandler.getModItem("bartworks", "werksoffblockcasing.01", 8, 31776),
                    ItemList.Circuit_Chip_SoC.get(64),
                    aUVTierLens
                },
                new FluidStack[] {Materials.UUMatter.getFluid(200000)},
                new ItemStack[] {
                    Materials.Carbon.getNanite(64),
                },
                null,
                null,
                500 * 20,
                10000000,
                1);

        // Silver Nanites - Used in Tier 2 PCB Factory to improve board production
        GT_Values.RA.addNanoForgeRecipe(
                new ItemStack[] {Materials.Silver.getBlocks(8), ItemList.Circuit_Chip_SoC.get(16), aUHVTierLens},
                new FluidStack[] {Materials.UUMatter.getFluid(200000)},
                new ItemStack[] {Materials.Silver.getNanite(1)},
                null,
                null,
                750 * 20,
                10000000,
                2);

        // Neutronium Nanites - Used to upgrade the Nano Forge to Tier 2
        GT_Values.RA.addNanoForgeRecipe(
                new ItemStack[] {
                    Materials.Neutronium.getBlocks(8),
                    ItemList.Circuit_Chip_SoC2.get(64),
                    ItemList.Circuit_Chip_SoC2.get(32),
                    aUEVTierLens
                },
                new FluidStack[] {Materials.UUMatter.getFluid(200000)},
                new ItemStack[] {Materials.Neutronium.getNanite(1)},
                null,
                null,
                100 * 20,
                100000000,
                2);

        // Gold Nanites - Used in Tier 3 PCB Factory to improve board production
        GT_Values.RA.addNanoForgeRecipe(
                new ItemStack[] {Materials.Gold.getBlocks(8), ItemList.Circuit_Chip_SoC.get(16), aUIVTierLens},
                new FluidStack[] {Materials.UUMatter.getFluid(300000)},
                new ItemStack[] {Materials.Gold.getNanite(1)},
                null,
                null,
                1000 * 20,
                100000000,
                3);

        // Transcendent Metal Nanites - Used to upgrade the Nano Forge to Tier 3
        GT_Values.RA.addNanoForgeRecipe(
                new ItemStack[] {
                    Materials.TranscendentMetal.getBlocks(8),
                    ItemList.Circuit_Chip_SoC2.get(64),
                    ItemList.Circuit_Chip_SoC2.get(64),
                    ItemList.Circuit_Chip_SoC2.get(64),
                    aUMVTierLens
                },
                new FluidStack[] {Materials.UUMatter.getFluid(2000000)},
                new ItemStack[] {Materials.TranscendentMetal.getNanite(1)},
                null,
                null,
                750 * 20,
                1000000000,
                2);
    }
}
