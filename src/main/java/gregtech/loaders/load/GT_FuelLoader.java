package gregtech.loaders.load;

import static gregtech.api.enums.Mods.BloodMagic;
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.Thaumcraft;
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
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
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
        new GT_Recipe(
            new ItemStack(Items.lava_bucket),
            new ItemStack(Blocks.obsidian),
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1L),
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1L),
            GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Electrum, 1L),
            30,
            2);

        RecipeMap.sSmallNaquadahReactorFuels.addRecipe(
            true,
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.NaquadahEnriched, 1L) },
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadah, 1L) },
            null,
            null,
            null,
            0,
            0,
            50000);
        RecipeMap.sLargeNaquadahReactorFuels.addRecipe(
            true,
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.NaquadahEnriched, 1L) },
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Naquadah, 1L) },
            null,
            null,
            null,
            0,
            0,
            250000);
        RecipeMap.sHugeNaquadahReactorFuels.addRecipe(
            true,
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.NaquadahEnriched, 1L) },
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Naquadah, 1L) },
            null,
            null,
            null,
            0,
            0,
            500000);
        RecipeMap.sExtremeNaquadahReactorFuels.addRecipe(
            true,
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadria, 1L) },
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadah, 1L) },
            null,
            null,
            null,
            0,
            0,
            250000);
        RecipeMap.sUltraHugeNaquadahReactorFuels.addRecipe(
            true,
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Naquadria, 1L) },
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Naquadah, 1L) },
            null,
            null,
            null,
            0,
            0,
            1000000);
        RecipeMap.sFluidNaquadahReactorFuels.addRecipe(
            true,
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NaquadahEnriched, 1L) },
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Naquadah, 1L) },
            null,
            null,
            null,
            0,
            0,
            1400000);

        // BloodMagic
        RecipeMap.sMagicFuels.addRecipe(
            true,
            new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.ID, "reinforcedSlate", 1L) },
            new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.ID, "blankSlate", 1L) },
            null,
            null,
            null,
            0,
            0,
            400);
        RecipeMap.sMagicFuels.addRecipe(
            true,
            new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.ID, "imbuedSlate", 1L) },
            new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.ID, "reinforcedSlate", 1L) },
            null,
            null,
            null,
            0,
            0,
            1000);
        RecipeMap.sMagicFuels.addRecipe(
            true,
            new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.ID, "demonicSlate", 1L) },
            new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.ID, "imbuedSlate", 1L) },
            null,
            null,
            null,
            0,
            0,
            8000);
        RecipeMap.sMagicFuels.addRecipe(
            true,
            new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.ID, "bloodMagicBaseItems", 1L, 27) },
            new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.ID, "demonicSlate", 1L) },
            null,
            null,
            null,
            0,
            0,
            20000);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getModItem(Thaumcraft.ID, "ItemResource", 1L, 4))
            .metadata(FUEL_VALUE, 4)
            .metadata(FUEL_TYPE, 5)
            .duration(0)
            .eut(0)
            .addTo(GT_RecipeConstants.Fuel);
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.experience_bottle, 1))
            .metadata(FUEL_VALUE, 10)
            .metadata(FUEL_TYPE, 5)
            .duration(0)
            .eut(0)
            .addTo(GT_RecipeConstants.Fuel);
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Items.ghast_tear, 1))
            .metadata(FUEL_VALUE, 50)
            .metadata(FUEL_TYPE, 5)
            .duration(0)
            .eut(0)
            .addTo(GT_RecipeConstants.Fuel);
        GT_Values.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.beacon, 1))
            .metadata(FUEL_VALUE, Materials.NetherStar.mFuelPower * 2)
            .metadata(FUEL_TYPE, Materials.NetherStar.mFuelType)
            .duration(0)
            .eut(0)
            .addTo(GT_RecipeConstants.Fuel);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_ModHandler.getModItem(EnderIO.ID, "bucketRocket_fuel", 1))
            .metadata(FUEL_VALUE, 250)
            .metadata(FUEL_TYPE, 1)
            .duration(0)
            .eut(0)
            .addTo(GT_RecipeConstants.Fuel);
    }
}
