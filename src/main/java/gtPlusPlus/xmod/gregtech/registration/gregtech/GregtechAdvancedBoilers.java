package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Boiler_Advanced_HV;
import static gregtech.api.enums.MetaTileEntityIDs.Boiler_Advanced_LV;
import static gregtech.api.enums.MetaTileEntityIDs.Boiler_Advanced_MV;
import static gregtech.api.enums.Mods.IronTanks;
import static gregtech.api.util.GTModHandler.getModItem;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.MTEAdvancedBoilerHV;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.MTEAdvancedBoilerLV;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.MTEAdvancedBoilerMV;

public class GregtechAdvancedBoilers {

    public static void run() {
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
                | GTModHandler.RecipeBits.BUFFERED
                | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "DSD", "BTB", "DSD", 'D', MaterialLibAPI.getStack(Materials.Lead, Shapes.plateDouble, 1),
                'S', MaterialLibAPI.getStack(Materials.Steel, TEBlockShapes.pipeMedium, 1), 'B',
                MaterialLibAPI.getStack(Materials.Bronze, TEBlockShapes.pipeMedium, 1), 'T',
                getModItem(IronTanks.ID, "silverTank", 1, 0) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.BoilerChassis_Tier1.get(1),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED
                | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "DSD", "BTB", "DSD", 'D',
                MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.plateDouble, 1), 'S',
                MaterialLibAPI.getStack(Materials.StainlessSteel, TEBlockShapes.pipeMedium, 1), 'B',
                MaterialLibAPI.getStack(Materials.Steel, TEBlockShapes.pipeMedium, 1), 'T',
                getModItem(IronTanks.ID, "stainlesssteelTank", 1, 0) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.BoilerChassis_Tier2.get(1),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED
                | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "DSD", "BTB", "DSD", 'D', MaterialLibAPI.getStack(Materials.Titanium, Shapes.plateDouble, 1),
                'S', MaterialLibAPI.getStack(Materials.Titanium, TEBlockShapes.pipeMedium, 1), 'B',
                MaterialLibAPI.getStack(Materials.StainlessSteel, TEBlockShapes.pipeMedium, 1), 'T',
                getModItem(IronTanks.ID, "titaniumTank", 1, 0) });

        // Boiler Recipes
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Boiler_Advanced_LV.get(1),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED
                | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "EXE", "CMC", "PBP", 'E', ItemList.Electric_Pump_LV.get(1L), 'X', "circuitBasic", 'C',
                GregtechItemList.BoilerChassis_Tier0, 'M', ItemList.Casing_LV.get(1L), 'P',
                MaterialLibAPI.getStack(Materials.Steel, TEBlockShapes.pipeHuge, 1), 'B',
                ItemList.Machine_Steel_Boiler.get(1L) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Boiler_Advanced_MV.get(1),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED
                | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "EXE", "CMC", "PBP", 'E', ItemList.Electric_Pump_MV.get(1L), 'X', "circuitGood", 'C',
                GregtechItemList.BoilerChassis_Tier1, 'M', ItemList.Casing_MV.get(1L), 'P',
                MaterialLibAPI.getStack(Materials.StainlessSteel, TEBlockShapes.pipeHuge, 1), 'B',
                ItemList.Machine_Steel_Boiler.get(1L) });
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Boiler_Advanced_HV.get(1),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED
                | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "EXE", "CMC", "PBP", 'E', ItemList.Electric_Pump_HV.get(1L), 'X', "circuitAdvanced", 'C',
                GregtechItemList.BoilerChassis_Tier2, 'M', ItemList.Casing_HV.get(1L), 'P',
                MaterialLibAPI.getStack(Materials.Titanium, TEBlockShapes.pipeHuge, 1), 'B',
                ItemList.Machine_Steel_Boiler.get(1L) });
    }
}
