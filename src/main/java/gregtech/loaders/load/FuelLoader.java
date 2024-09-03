package gregtech.loaders.load;

import static gregtech.api.enums.Mods.BloodMagic;
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.recipe.RecipeMaps.extremeNaquadahReactorFuels;
import static gregtech.api.recipe.RecipeMaps.hugeNaquadahReactorFuels;
import static gregtech.api.recipe.RecipeMaps.largeNaquadahReactorFuels;
import static gregtech.api.recipe.RecipeMaps.magicFuels;
import static gregtech.api.recipe.RecipeMaps.smallNaquadahReactorFuels;
import static gregtech.api.recipe.RecipeMaps.ultraHugeNaquadahReactorFuels;
import static gregtech.api.util.GTRecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GTRecipeConstants.FUEL_VALUE;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.FluidState;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.fluid.GTFluidFactory;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;

public class FuelLoader implements Runnable {

    @Override
    public void run() {
        GTLog.out.println("GTMod: Initializing various Fuels.");
        ItemList.sBlueVitriol = GTFluidFactory
            .of("solution.bluevitriol", "Blue Vitriol water solution", null, FluidState.LIQUID, 295);
        ItemList.sNickelSulfate = GTFluidFactory
            .of("solution.nickelsulfate", "Nickel sulfate water solution", null, FluidState.LIQUID, 295);
        ItemList.sGreenVitriol = GTFluidFactory
            .of("solution.greenvitriol", "Green Vitriol water solution", null, FluidState.LIQUID, 295);
        ItemList.sIndiumConcentrate = GTFluidFactory
            .of("indiumconcentrate", "Indium Concentrate", null, FluidState.LIQUID, 295); // TODO CHECK NEW x3
        ItemList.sLeadZincSolution = GTFluidFactory
            .of("leadzincsolution", "Lead-Zinc solution", null, FluidState.LIQUID, 295);
        ItemList.sRocketFuel = GTFluidFactory.of("rocket_fuel", "Rocket Fuel", null, FluidState.LIQUID, 295);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.bolt, Materials.NaquadahEnriched, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadah, 1L))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 50_000)
            .addTo(smallNaquadahReactorFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.NaquadahEnriched, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.Naquadah, 1L))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 250_000)
            .addTo(largeNaquadahReactorFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.NaquadahEnriched, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Naquadah, 1L))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 500_000)
            .addTo(hugeNaquadahReactorFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadria, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadah, 1L))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 250_000)
            .addTo(extremeNaquadahReactorFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.Naquadria, 1L))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stick, Materials.Naquadah, 1L))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 1_000_000)
            .addTo(ultraHugeNaquadahReactorFuels);

        // BloodMagic
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(BloodMagic.ID, "reinforcedSlate", 1L))
            .itemOutputs(GTModHandler.getModItem(BloodMagic.ID, "blankSlate", 1L))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 400)
            .addTo(magicFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(BloodMagic.ID, "imbuedSlate", 1L))
            .itemOutputs(GTModHandler.getModItem(BloodMagic.ID, "reinforcedSlate", 1L))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 1000)
            .addTo(magicFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(BloodMagic.ID, "demonicSlate", 1L))
            .itemOutputs(GTModHandler.getModItem(BloodMagic.ID, "imbuedSlate", 1L))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 8000)
            .addTo(magicFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(BloodMagic.ID, "bloodMagicBaseItems", 1L, 27))
            .itemOutputs(GTModHandler.getModItem(BloodMagic.ID, "demonicSlate", 1L))
            .duration(0)
            .eut(0)
            .metadata(FUEL_VALUE, 20000)
            .addTo(magicFuels);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1L, 4))
            .metadata(FUEL_VALUE, 4)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.experience_bottle, 1))
            .metadata(FUEL_VALUE, 10)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.ghast_tear, 1))
            .metadata(FUEL_VALUE, 50)
            .metadata(FUEL_TYPE, 5)
            .addTo(GTRecipeConstants.Fuel);
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.beacon, 1))
            .metadata(FUEL_VALUE, Materials.NetherStar.mFuelPower * 2)
            .metadata(FUEL_TYPE, Materials.NetherStar.mFuelType)
            .addTo(GTRecipeConstants.Fuel);
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(EnderIO.ID, "bucketRocket_fuel", 1))
            .metadata(FUEL_VALUE, 250)
            .metadata(FUEL_TYPE, 1)
            .addTo(GTRecipeConstants.Fuel);
    }
}
