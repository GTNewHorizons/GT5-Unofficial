package gtPlusPlus.xmod.gregtech.registration.gregtech;

import static gregtech.api.enums.MetaTileEntityIDs.Industrial_Solar_Tower;
import static gregtech.api.enums.MetaTileEntityIDs.Solar_Tower_Reflector;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialUtils;
import gregtech.api.recipe.RecipeMaps;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.MTESolarTower;
import gtPlusPlus.xmod.gregtech.common.tileentities.misc.MTESolarHeater;

public class GregtechSolarTower {

    public static void run() {
        run1();
    }

    private static void run1() {
        // Solar Tower
        GregtechItemList.Industrial_Solar_Tower.set(
            new MTESolarTower(Industrial_Solar_Tower.ID, "solartower.controller.tier.single", "Solar Tower")
                .getStackForm(1L));
        GregtechItemList.Solar_Tower_Reflector.set(
            new MTESolarHeater(
                Solar_Tower_Reflector.ID,
                "solarreflector.simple.single",
                "Solar Reflector",
                8,
                "Part of the Clean Green energy movement",
                0).getStackForm(1L));

        // NEI recipe
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialUtils.anyFluid(Materials.SolarSaltCold, 1000))
            .fluidOutputs(MaterialUtils.anyFluid(Materials.SolarSaltHot, 1000))
            .duration(0)
            .eut(0)
            .addTo(RecipeMaps.solarTowerRecipes);
    }
}
