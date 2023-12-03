package gtPlusPlus.xmod.gregtech.registration.gregtech;

import net.minecraftforge.fluids.FluidStack;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.GregtechMetaTileEntity_SolarTower;
import gtPlusPlus.xmod.gregtech.common.tileentities.misc.TileEntitySolarHeater;

public class GregtechSolarTower {

    public static void run() {
        Logger.INFO("Gregtech5u Content | Registering Solar Tower.");
        run1();
    }

    private static void run1() {
        // Solar Tower
        GregtechItemList.Industrial_Solar_Tower.set(
                new GregtechMetaTileEntity_SolarTower(863, "solartower.controller.tier.single", "Solar Tower")
                        .getStackForm(1L));
        GregtechItemList.Solar_Tower_Reflector.set(
                new TileEntitySolarHeater(
                        864,
                        "solarreflector.simple.single",
                        "Solar Reflector",
                        8,
                        "Part of the Clean Green energy movement",
                        0).getStackForm(1L));

        // NEI recipe
        GTPPRecipeMaps.solarTowerRecipes.addRecipe(
                false,
                null,
                null,
                null,
                null,
                new FluidStack[] { MISC_MATERIALS.SOLAR_SALT_COLD.getFluidStack(1000) },
                new FluidStack[] { MISC_MATERIALS.SOLAR_SALT_HOT.getFluidStack(1000) },
                0,
                0,
                0);
    }
}
