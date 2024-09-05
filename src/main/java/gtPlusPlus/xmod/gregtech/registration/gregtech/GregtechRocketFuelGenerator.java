package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Controller_RocketEngine;
import static gregtech.api.enums.MetaTileEntityIDs.Rocket_Engine_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Rocket_Engine_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Rocket_Engine_LuV;

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
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.MTERocketFuelGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTELargeRocketEngine;

public class GregtechRocketFuelGenerator {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Rocket Engines.");
        if (GTPPCore.ConfigSwitches.enableMachine_RocketEngines) {
            run1();
        }
    }

    private static void run1() {
        GregtechItemList.Controller_RocketEngine.set(
            new MTELargeRocketEngine(
                Controller_RocketEngine.ID,
                "gtpp.multimachine.rocketengine",
                "Rocketdyne F-1A Engine").getStackForm(1L));

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_RocketEngine.get(1L),
            CI.bitsd,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Machine_Multi_DieselEngine.get(1), 'P',
                ItemList.Electric_Piston_IV, 'E', ItemList.Field_Generator_EV, 'C',
                OrePrefixes.circuit.get(Materials.LuV), 'W', OrePrefixes.cableGt08.get(Materials.Platinum), 'G',
                MaterialsAlloy.MARAGING350.getGear(1) });

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_RocketEngine.get(1L),
            CI.bitsd,
            new Object[] { "PhP", "RFR", "PWP", 'R', OrePrefixes.pipeMedium.get(Materials.TungstenSteel), 'F',
                ItemList.Casing_RobustTungstenSteel, 'P', MaterialsAlloy.NITINOL_60.getGear(1), 'W',
                OrePrefixes.stickLong.get(Materials.TungstenSteel) });

        GregtechItemList.Rocket_Engine_EV.set(
            new MTERocketFuelGenerator(
                Rocket_Engine_EV.ID,
                "advancedgenerator.rocketFuel.tier.01",
                "Basic Rocket Engine",
                4).getStackForm(1L));
        GregtechItemList.Rocket_Engine_IV.set(
            new MTERocketFuelGenerator(
                Rocket_Engine_IV.ID,
                "advancedgenerator.rocketFuel.tier.02",
                "Advanced Rocket Engine",
                5).getStackForm(1L));
        GregtechItemList.Rocket_Engine_LuV.set(
            new MTERocketFuelGenerator(
                Rocket_Engine_LuV.ID,
                "advancedgenerator.rocketFuel.tier.03",
                "Turbo Rocket Engine",
                6).getStackForm(1L));
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Rocket_Engine_EV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                ItemList.Electric_Motor_EV, 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt02.get(Materials.Aluminium), 'G', MaterialsAlloy.TANTALLOY_61.getGear(1) });

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Rocket_Engine_IV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Piston_IV, 'E',
                ItemList.Electric_Motor_IV, 'C', OrePrefixes.circuit.get(Materials.LuV), 'W',
                OrePrefixes.cableGt02.get(Materials.Platinum), 'G', MaterialsAlloy.STELLITE.getGear(1) });
        final ItemStack INGREDIENT_1 = CI.electricPiston_LuV;
        final ItemStack INGREDIENT_2 = CI.electricMotor_LuV;
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Rocket_Engine_LuV.get(1L),
            GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.NOT_REMOVABLE
                | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_LuV, 'P', INGREDIENT_1, 'E', INGREDIENT_2, 'C',
                OrePrefixes.circuit.get(Materials.ZPM), 'W', OrePrefixes.cableGt02.get(Materials.Tungsten), 'G',
                MaterialsAlloy.ZERON_100.getGear(1) });
    }
}
