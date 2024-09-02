package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.AlgaeFarm_Controller;
import static gregtech.api.enums.MetaTileEntityIDs.Bus_Catalysts;
import static gregtech.api.enums.MetaTileEntityIDs.ChemicalPlant_Controller;

import gregtech.api.GregTechAPI;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.MTEHatchCatalysts;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.algae.MTEAlgaePondBase;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.MTEChemicalPlant;

public class GregtechAlgaeContent {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Algae Content.");
        run1();
    }

    private static void run1() {

        // Algae Pond
        GregtechItemList.AlgaeFarm_Controller.set(
            new MTEAlgaePondBase(AlgaeFarm_Controller.ID, "algaefarm.controller.tier.single", "Algae Farm")
                .getStackForm(1L));

        // Chemical Plant
        GregtechItemList.ChemicalPlant_Controller.set(
            new MTEChemicalPlant(
                ChemicalPlant_Controller.ID,
                "chemicalplant.controller.tier.single",
                "ExxonMobil Chemical Plant").getStackForm(1L));

        GregtechItemList.Bus_Catalysts
            .set((new MTEHatchCatalysts(Bus_Catalysts.ID, "hatch.catalysts", "Catalyst Housing")).getStackForm(1L));

        // Bronze
        MTEChemicalPlant.registerMachineCasingForTier(0, ModBlocks.blockCustomMachineCasings, 0, 10);
        // Steel
        MTEChemicalPlant.registerMachineCasingForTier(1, GregTechAPI.sBlockCasings2, 0, 16);
        // Aluminium
        MTEChemicalPlant.registerMachineCasingForTier(2, ModBlocks.blockCustomMachineCasings, 1, 17);
        // Stainless
        MTEChemicalPlant.registerMachineCasingForTier(3, GregTechAPI.sBlockCasings4, 1, 49);
        // Titanium
        MTEChemicalPlant.registerMachineCasingForTier(4, GregTechAPI.sBlockCasings4, 2, 50);
        // Tungsten
        MTEChemicalPlant.registerMachineCasingForTier(5, GregTechAPI.sBlockCasings4, 0, 48);
        // Laurenium
        MTEChemicalPlant.registerMachineCasingForTier(6, ModBlocks.blockCustomMachineCasings, 2, 84);
        // Botmium
        MTEChemicalPlant.registerMachineCasingForTier(7, ModBlocks.blockCustomMachineCasings, 3, 11);
    }
}
