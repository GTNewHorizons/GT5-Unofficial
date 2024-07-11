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
import static gregtech.api.util.GT_RecipeConstants.FUEL_TYPE;
import static gregtech.api.util.GT_RecipeConstants.FUEL_VALUE;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.api.enums.FluidState;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.fluid.GT_FluidFactory;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_RecipeConstants;

public class GT_FuelLoader implements Runnable {

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Initializing various Fuels.");
        ItemList.sBlueVitriol = GT_FluidFactory
            .of("solution.bluevitriol", "Blue Vitriol water solution", null, FluidState.LIQUID, 295);
        ItemList.sNickelSulfate = GT_FluidFactory
            .of("solution.nickelsulfate", "Nickel sulfate water solution", null, FluidState.LIQUID, 295);
        ItemList.sGreenVitriol = GT_FluidFactory
            .of("solution.greenvitriol", "Green Vitriol water solution", null, FluidState.LIQUID, 295);
        ItemList.sIndiumConcentrate = GT_FluidFactory
            .of("indiumconcentrate", "Indium Concentrate", null, FluidState.LIQUID, 295); // TODO CHECK NEW x3
        ItemList.sLeadZincSolution = GT_FluidFactory
            .of("leadzincsolution", "Lead-Zinc solution", null, FluidState.LIQUID, 295);
        ItemList.sRocketFuel = GT_FluidFactory.of("rocket_fuel", "Rocket Fuel", null, FluidState.LIQUID, 295);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.NaquadahEnriched, 1L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadah, 1L))
            .duration(0)
            .eut(0)
            .specialValue(50_000)
            .addTo(smallNaquadahReactorFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.NaquadahEnriched, 1L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Naquadah, 1L))
            .duration(0)
            .eut(0)
            .specialValue(250_000)
            .addTo(largeNaquadahReactorFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.NaquadahEnriched, 1L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Naquadah, 1L))
            .duration(0)
            .eut(0)
            .specialValue(500_000)
            .addTo(hugeNaquadahReactorFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadria, 1L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadah, 1L))
            .duration(0)
            .eut(0)
            .specialValue(250_000)
            .addTo(extremeNaquadahReactorFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Naquadria, 1L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Naquadah, 1L))
            .duration(0)
            .eut(0)
            .specialValue(1_000_000)
            .addTo(ultraHugeNaquadahReactorFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NaquadahEnriched, 1L))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Naquadah, 1L))
            .duration(0)
            .eut(0)
            .specialValue(1_400_000)
            .addTo(RecipeMaps.fluidNaquadahReactorFuels);

        // BloodMagic
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getModItem(BloodMagic.ID, "reinforcedSlate", 1L))
            .itemOutputs(GT_ModHandler.getModItem(BloodMagic.ID, "blankSlate", 1L))
            .duration(0)
            .eut(0)
            .specialValue(400)
            .addTo(magicFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getModItem(BloodMagic.ID, "imbuedSlate", 1L))
            .itemOutputs(GT_ModHandler.getModItem(BloodMagic.ID, "reinforcedSlate", 1L))
            .duration(0)
            .eut(0)
            .specialValue(1000)
            .addTo(magicFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getModItem(BloodMagic.ID, "demonicSlate", 1L))
            .itemOutputs(GT_ModHandler.getModItem(BloodMagic.ID, "imbuedSlate", 1L))
            .duration(0)
            .eut(0)
            .specialValue(8000)
            .addTo(magicFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getModItem(BloodMagic.ID, "bloodMagicBaseItems", 1L, 27))
            .itemOutputs(GT_ModHandler.getModItem(BloodMagic.ID, "demonicSlate", 1L))
            .duration(0)
            .eut(0)
            .specialValue(20000)
            .addTo(magicFuels);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1L, 4))
            .metadata(FUEL_VALUE, 4)
            .metadata(FUEL_TYPE, 5)
            .addTo(GT_RecipeConstants.Fuel);
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.experience_bottle, 1))
            .metadata(FUEL_VALUE, 10)
            .metadata(FUEL_TYPE, 5)
            .addTo(GT_RecipeConstants.Fuel);
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.ghast_tear, 1))
            .metadata(FUEL_VALUE, 50)
            .metadata(FUEL_TYPE, 5)
            .addTo(GT_RecipeConstants.Fuel);
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.beacon, 1))
            .metadata(FUEL_VALUE, Materials.NetherStar.mFuelPower * 2)
            .metadata(FUEL_TYPE, Materials.NetherStar.mFuelType)
            .addTo(GT_RecipeConstants.Fuel);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getModItem(EnderIO.ID, "bucketRocket_fuel", 1))
            .metadata(FUEL_VALUE, 250)
            .metadata(FUEL_TYPE, 1)
            .addTo(GT_RecipeConstants.Fuel);
    }
}
