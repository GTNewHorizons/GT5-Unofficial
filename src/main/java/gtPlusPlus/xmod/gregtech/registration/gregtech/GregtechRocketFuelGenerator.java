package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Controller_RocketEngine;
import static gregtech.api.enums.MetaTileEntityIDs.Rocket_Engine_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Rocket_Engine_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Rocket_Engine_LuV;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.GregtechMetaTileEntityRocketFuelGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_LargeRocketEngine;

public class GregtechRocketFuelGenerator {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Industrial Rocket Engines.");
        if (CORE.ConfigSwitches.enableMachine_RocketEngines) {
            run1();
        }
    }

    private static void run1() {
        GregtechItemList.Controller_RocketEngine.set(
            new GregtechMetaTileEntity_LargeRocketEngine(
                Controller_RocketEngine.ID,
                "gtpp.multimachine.rocketengine",
                "Rocketdyne F-1A Engine").getStackForm(1L));

        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Controller_RocketEngine.get(1L),
            CI.bitsd,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Machine_Multi_DieselEngine.get(1), 'P',
                ItemList.Electric_Piston_IV, 'E', ItemList.Field_Generator_EV, 'C',
                OrePrefixes.circuit.get(Materials.LuV), 'W', OrePrefixes.cableGt08.get(Materials.Platinum), 'G',
                ALLOY.MARAGING350.getGear(1) });

        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Casing_RocketEngine.get(1L),
            CI.bitsd,
            new Object[] { "PhP", "RFR", "PWP", 'R', OrePrefixes.pipeMedium.get(Materials.TungstenSteel), 'F',
                ItemList.Casing_RobustTungstenSteel, 'P', ALLOY.NITINOL_60.getGear(1), 'W',
                OrePrefixes.stickLong.get(Materials.TungstenSteel) });

        GregtechItemList.Rocket_Engine_EV.set(
            new GregtechMetaTileEntityRocketFuelGenerator(
                Rocket_Engine_EV.ID,
                "advancedgenerator.rocketFuel.tier.01",
                "Basic Rocket Engine",
                4).getStackForm(1L));
        GregtechItemList.Rocket_Engine_IV.set(
            new GregtechMetaTileEntityRocketFuelGenerator(
                Rocket_Engine_IV.ID,
                "advancedgenerator.rocketFuel.tier.02",
                "Advanced Rocket Engine",
                5).getStackForm(1L));
        GregtechItemList.Rocket_Engine_LuV.set(
            new GregtechMetaTileEntityRocketFuelGenerator(
                Rocket_Engine_LuV.ID,
                "advancedgenerator.rocketFuel.tier.03",
                "Turbo Rocket Engine",
                6).getStackForm(1L));
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Rocket_Engine_EV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                ItemList.Electric_Motor_EV, 'C', OrePrefixes.circuit.get(Materials.IV), 'W',
                OrePrefixes.cableGt02.get(Materials.Aluminium), 'G', ALLOY.TANTALLOY_61.getGear(1) });

        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Rocket_Engine_IV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Piston_IV, 'E',
                ItemList.Electric_Motor_IV, 'C', OrePrefixes.circuit.get(Materials.LuV), 'W',
                OrePrefixes.cableGt02.get(Materials.Platinum), 'G', ALLOY.STELLITE.getGear(1) });
        final ItemStack INGREDIENT_1 = CI.electricPiston_LuV;
        final ItemStack INGREDIENT_2 = CI.electricMotor_LuV;
        GT_ModHandler.addCraftingRecipe(
            GregtechItemList.Rocket_Engine_LuV.get(1L),
            GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                | GT_ModHandler.RecipeBits.REVERSIBLE
                | GT_ModHandler.RecipeBits.BUFFERED,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_LuV, 'P', INGREDIENT_1, 'E', INGREDIENT_2, 'C',
                OrePrefixes.circuit.get(Materials.ZPM), 'W', OrePrefixes.cableGt02.get(Materials.Tungsten), 'G',
                ALLOY.ZERON_100.getGear(1) });
    }
}
