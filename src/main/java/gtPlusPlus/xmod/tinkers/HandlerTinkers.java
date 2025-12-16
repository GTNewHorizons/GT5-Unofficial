package gtPlusPlus.xmod.tinkers;

import java.util.ArrayList;

import net.minecraftforge.fluids.Fluid;

import gregtech.api.enums.Mods;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import gtPlusPlus.xmod.tinkers.material.BaseTinkersMaterial;
import gtPlusPlus.xmod.tinkers.util.TinkersDryingRecipe;
import gtPlusPlus.xmod.tinkers.util.TinkersUtils;

public class HandlerTinkers {

    public static ArrayList<BaseTinkersMaterial> mTinkerMaterials = new ArrayList<>();

    public static void postInit() {
        if (Mods.TinkerConstruct.isModLoaded()) {

            Fluid pyrotheumFluid = TFFluids.fluidPyrotheum;
            if (pyrotheumFluid != null) {
                // Enable Pyrotheum as Fuel for the Smeltery
                TinkersUtils.addSmelteryFuel(pyrotheumFluid, 5000, 70); // pyrotheum lasts 3.5 seconds per 15 mb
            }

            // Generate Drying Rack recipes
            TinkersDryingRecipe.generateAllDryingRecipes();
        }
    }
}
