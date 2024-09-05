package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Geothermal_Engine_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Geothermal_Engine_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Geothermal_Engine_LuV;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.MTEGeothermalGenerator;

public class GregtechGeothermalThermalGenerator {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Geothermal Engines.");
        if (GTPPCore.ConfigSwitches.enableMachine_GeothermalEngines) {
            run1();
        }
    }

    private static void run1() {
        GregtechItemList.Geothermal_Engine_EV.set(
            new MTEGeothermalGenerator(
                Geothermal_Engine_EV.ID,
                "advancedgenerator.geothermalFuel.tier.01",
                "Basic Geothermal Engine",
                4).getStackForm(1L));
        GregtechItemList.Geothermal_Engine_IV.set(
            new MTEGeothermalGenerator(
                Geothermal_Engine_IV.ID,
                "advancedgenerator.geothermalFuel.tier.02",
                "Turbo Geothermal Engine",
                5).getStackForm(1L));
        GregtechItemList.Geothermal_Engine_LuV.set(
            new MTEGeothermalGenerator(
                Geothermal_Engine_LuV.ID,
                "advancedgenerator.geothermalFuel.tier.03",
                "Vulcan Geothermal Engine",
                6).getStackForm(1L));

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Geothermal_Engine_EV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "CEC", "GMG", "PWP", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                ItemList.Electric_Motor_EV, 'C', OrePrefixes.circuit.get(Materials.ZPM), 'W',
                OrePrefixes.cableGt04.get(Materials.Aluminium), 'G', MaterialsAlloy.TANTALLOY_61.getGear(1) });

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Geothermal_Engine_IV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "CEC", "GMG", "PWP", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Piston_IV, 'E',
                ItemList.Electric_Motor_IV, 'C', OrePrefixes.circuit.get(Materials.UV), 'W',
                OrePrefixes.cableGt04.get(Materials.Platinum), 'G', MaterialsAlloy.STELLITE.getGear(1) });

        final ItemStack INGREDIENT_1 = CI.electricPiston_LuV;
        final ItemStack INGREDIENT_2 = CI.electricMotor_LuV;
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Geothermal_Engine_LuV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "CEC", "GMG", "PWP", 'M', ItemList.Hull_LuV, 'P', INGREDIENT_1, 'E', INGREDIENT_2, 'C',
                OrePrefixes.circuit.get(Materials.UHV), 'W', OrePrefixes.cableGt04.get(Materials.Tungsten), 'G',
                MaterialsAlloy.ZERON_100.getGear(1) });
    }
}
