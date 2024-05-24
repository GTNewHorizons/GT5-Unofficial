package gtPlusPlus.xmod.gregtech.registration.gregtech;

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
        GregtechItemList.AlgaeFarm_Controller
            .set(new GregtechMTE_AlgaePondBase(997, "algaefarm.controller.tier.single", "Algae Farm").getStackForm(1L));

        // Chemical Plant
        GregtechItemList.ChemicalPlant_Controller.set(
            new GregtechMTE_ChemicalPlant(998, "chemicalplant.controller.tier.single", "ExxonMobil Chemical Plant")
                .getStackForm(1L));

        GregtechItemList.Bus_Catalysts.set(
            (new GT_MetaTileEntity_Hatch_Catalysts(31030, "hatch.catalysts", "Catalyst Housing")).getStackForm(1L));

        int aTier = 0;
        // Bronze
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(aTier++, ModBlocks.blockCustomMachineCasings, 0, 10);
        // Steel
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(aTier++, GregTech_API.sBlockCasings2, 0, 16);
        // Aluminium
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(aTier++, ModBlocks.blockCustomMachineCasings, 1, 17);
        // Stainless
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(aTier++, GregTech_API.sBlockCasings4, 1, 49);
        // Titanium
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(aTier++, GregTech_API.sBlockCasings4, 2, 50);
        // Tungsten
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(aTier++, GregTech_API.sBlockCasings4, 0, 48);
        // Laurenium
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(aTier++, ModBlocks.blockCustomMachineCasings, 2, 84);
        // Botmium
        GregtechMTE_ChemicalPlant.registerMachineCasingForTier(aTier++, ModBlocks.blockCustomMachineCasings, 3, 11);
    }
}
