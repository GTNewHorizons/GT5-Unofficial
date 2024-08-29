package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.AlgaeFarm_Controller;
import static gregtech.api.enums.MetaTileEntityIDs.Bus_Catalysts;
import static gregtech.api.enums.MetaTileEntityIDs.ChemicalPlant_Controller;

import gregtech.api.GregTech_API;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.GT_MetaTileEntity_Hatch_Catalysts;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.algae.GregtechMTE_AlgaePondBase;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.GregtechMTE_ChemicalPlant;

public class GregtechAlgaeContent {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Algae Content.");
        run1();
    }

    private static void run1() {

        // Algae Pond
        GregtechItemList.AlgaeFarm_Controller.set(
            new GregtechMTE_AlgaePondBase(AlgaeFarm_Controller.ID, "algaefarm.controller.tier.single", "Algae Farm")
                .getStackForm(1L));

        // Chemical Plant
        GregtechItemList.ChemicalPlant_Controller.set(
            new GregtechMTE_ChemicalPlant(
                ChemicalPlant_Controller.ID,
                "chemicalplant.controller.tier.single",
                "ExxonMobil Chemical Plant").getStackForm(1L));

        GregtechItemList.Bus_Catalysts.set(
            (new GT_MetaTileEntity_Hatch_Catalysts(Bus_Catalysts.ID, "hatch.catalysts", "Catalyst Housing"))
                .getStackForm(1L));

        // Bronze
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(0, ModBlocks.blockCustomMachineCasings, 0, 10);
        // Steel
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(1, GregTech_API.sBlockCasings2, 0, 16);
        // Aluminium
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(2, ModBlocks.blockCustomMachineCasings, 1, 17);
        // Stainless
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(3, GregTech_API.sBlockCasings4, 1, 49);
        // Titanium
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(4, GregTech_API.sBlockCasings4, 2, 50);
        // Tungsten
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(5, GregTech_API.sBlockCasings4, 0, 48);
        // Laurenium
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(6, ModBlocks.blockCustomMachineCasings, 2, 84);
        // Botmium
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(7, ModBlocks.blockCustomMachineCasings, 3, 11);
    }
}
