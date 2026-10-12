package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Controller_RocketEngine;
import static gregtech.api.enums.MetaTileEntityIDs.Rocket_Engine_EV;
import static gregtech.api.enums.MetaTileEntityIDs.Rocket_Engine_IV;
import static gregtech.api.enums.MetaTileEntityIDs.Rocket_Engine_LuV;
import static gregtech.api.util.GTModHandler.RecipeBits.BITS;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Circuits;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.util.GTModHandler;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.MTERocketFuelGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTELargeRocketEngine;

public class GregtechRocketFuelGenerator {

    public static void run() {
        run1();

    }

    private static void run1() {
        GregtechItemList.Controller_RocketEngine.set(
            new MTELargeRocketEngine(
                Controller_RocketEngine.ID,
                "gtpp.multimachine.rocketengine",
                "Rocketdyne F-1A Engine").getStackForm(1L));

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Controller_RocketEngine.get(1L),
            BITS | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.LargeCombustionEngine.get(1), 'P',
                ItemList.Electric_Piston_IV, 'E', ItemList.Field_Generator_EV, 'C', Circuits.LuV.getIngredient(), 'W',
                OrePrefixes.cableGt08.ingredient(Materials.Platinum), 'G',
                MaterialLibAPI.getStack(Materials.MaragingSteel350, Shapes.gearGt, 1) });

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Casing_RocketEngine.get(1L),
            BITS | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PhP", "RFR", "PWP", 'R', OrePrefixes.pipeMedium.ingredient(Materials.TungstenSteel), 'F',
                ItemList.Casing_RobustTungstenSteel, 'P',
                MaterialLibAPI.getStack(Materials.Nitinol60, Shapes.gearGt, 1), 'W',
                OrePrefixes.stickLong.ingredient(Materials.TungstenSteel) });

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
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED
                | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_EV, 'P', ItemList.Electric_Piston_EV, 'E',
                ItemList.Electric_Motor_EV, 'C', Circuits.IV.getIngredient(), 'W',
                OrePrefixes.cableGt02.ingredient(Materials.Aluminium), 'G',
                MaterialLibAPI.getStack(Materials.Tantalloy61, Shapes.gearGt, 1) });

        GTModHandler.addCraftingRecipe(
            GregtechItemList.Rocket_Engine_IV.get(1L),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED
                | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_IV, 'P', ItemList.Electric_Piston_IV, 'E',
                ItemList.Electric_Motor_IV, 'C', Circuits.LuV.getIngredient(), 'W',
                OrePrefixes.cableGt02.ingredient(Materials.Platinum), 'G',
                MaterialLibAPI.getStack(Materials.Stellite, Shapes.gearGt, 1) });
        final ItemStack INGREDIENT_1 = ItemList.Electric_Piston_LuV.get(1);
        final ItemStack INGREDIENT_2 = ItemList.Electric_Motor_LuV.get(1);
        GTModHandler.addCraftingRecipe(
            GregtechItemList.Rocket_Engine_LuV.get(1L),
            GTModHandler.RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.REVERSIBLE
                | GTModHandler.RecipeBits.BUFFERED
                | GTModHandler.RecipeBits.DO_NOT_CHECK_FOR_COLLISIONS,
            new Object[] { "PCP", "EME", "GWG", 'M', ItemList.Hull_LuV, 'P', INGREDIENT_1, 'E', INGREDIENT_2, 'C',
                Circuits.ZPM.getIngredient(), 'W', OrePrefixes.cableGt02.ingredient(Materials.Tungsten), 'G',
                MaterialLibAPI.getStack(Materials.Zeron100, Shapes.gearGt, 1) });
    }
}
