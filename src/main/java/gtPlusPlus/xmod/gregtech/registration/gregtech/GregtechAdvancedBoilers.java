package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Boiler_Advanced_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Boiler_Advanced_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Boiler_Advanced_MV;
import static gregtech.api.enums.Mods.IronTanks;
import static gregtech.api.util.GTModHandler.getModItem;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.MTEAdvancedBoilerHV;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.MTEAdvancedBoilerLV;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.MTEAdvancedBoilerMV;

public class GregtechAdvancedBoilers {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Advanced Boilers.");
        run1();
    }

    private static void run1() {
        // Boilers
        GregtechItemList.Boiler_Advanced_LV
            .set(new MTEAdvancedBoilerLV(Boiler_Advanced_LV.ID, "Advanced Boiler [LV]", 1).getStackForm(1L));
        GregtechItemList.Boiler_Advanced_MV
            .set(new MTEAdvancedBoilerMV(Boiler_Advanced_MV.ID, "Advanced Boiler [MV]", 2).getStackForm(1L));
        GregtechItemList.Boiler_Advanced_HV
            .set(new MTEAdvancedBoilerHV(Boiler_Advanced_HV.ID, "Advanced Boiler [HV]", 3).getStackForm(1L));
    }

    public static void addRecipes() {

        // Chassis Recipes
        GTModHandler.addCraftingRecipe(
            GregtechItemList.BoilerChassis_Tier0.get(1),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "DSD", "BTB", "DSD", 'D',
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Lead, 1L), 'S',
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Steel, 1L), 'B',
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Bronze, 1L), 'T',
                getModItem(IronTanks.ID, "silverTank", 1, 0) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.BoilerChassis_Tier1.get(1),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "DSD", "BTB", "DSD", 'D',
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.StainlessSteel, 1L), 'S',
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel, 1L), 'B',
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Steel, 1L), 'T',
                getModItem(IronTanks.ID, "stainlesssteelTank", 1, 0) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.BoilerChassis_Tier2.get(1),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "DSD", "BTB", "DSD", 'D',
                GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Titanium, 1L), 'S',
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Titanium, 1L), 'B',
                GTOreDictUnificator.get(OrePrefixes.pipeMedium, Materials.StainlessSteel, 1L), 'T',
                getModItem(IronTanks.ID, "titaniumTank", 1, 0) });

        // Boiler Recipes
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Boiler_Advanced_LV.get(1),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "EXE", "CMC", "PBP", 'E', ItemList.Electric_Pump_LV.get(1L), 'X', "circuitBasic", 'C',
                GregtechItemList.BoilerChassis_Tier0, 'M', ItemList.Casing_LV.get(1L), 'P',
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Steel, 1L), 'B',
                ItemList.Machine_Steel_Boiler.get(1L) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Boiler_Advanced_MV.get(1),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "EXE", "CMC", "PBP", 'E', ItemList.Electric_Pump_MV.get(1L), 'X', "circuitGood", 'C',
                GregtechItemList.BoilerChassis_Tier1, 'M', ItemList.Casing_MV.get(1L), 'P',
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.StainlessSteel, 1L), 'B',
                ItemList.Machine_Steel_Boiler.get(1L) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Boiler_Advanced_HV.get(1),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "EXE", "CMC", "PBP", 'E', ItemList.Electric_Pump_HV.get(1L), 'X', "circuitAdvanced", 'C',
                GregtechItemList.BoilerChassis_Tier2, 'M', ItemList.Casing_HV.get(1L), 'P',
                GTOreDictUnificator.get(OrePrefixes.pipeHuge, Materials.Titanium, 1L), 'B',
                ItemList.Machine_Steel_Boiler.get(1L) });
    }
}
