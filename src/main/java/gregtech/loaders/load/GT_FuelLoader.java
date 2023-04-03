package gregtech.loaders.load;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import gregtech.GT_Mod;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;

import static gregtech.api.enums.ModIDs.BloodMagic;
import static gregtech.api.enums.ModIDs.EnderIO;
import static gregtech.api.enums.ModIDs.Thaumcraft;

public class GT_FuelLoader implements Runnable {

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Initializing various Fuels.");
        ItemList.sBlueVitriol = GT_Mod.gregtechproxy.addFluid(
                "solution.bluevitriol",
                "Blue Vitriol water solution",
                null,
                1,
                295);
        ItemList.sNickelSulfate = GT_Mod.gregtechproxy.addFluid(
                "solution.nickelsulfate",
                "Nickel sulfate water solution",
                null,
                1,
                295);
        ItemList.sGreenVitriol = GT_Mod.gregtechproxy.addFluid(
                "solution.greenvitriol",
                "Green Vitriol water solution",
                null,
                1,
                295);
        ItemList.sIndiumConcentrate = GT_Mod.gregtechproxy.addFluid(
                "indiumconcentrate",
                "Indium Concentrate",
                null,
                1,
                295); // TODO CHECK NEW x3
        ItemList.sLeadZincSolution = GT_Mod.gregtechproxy.addFluid(
                "leadzincsolution",
                "Lead-Zinc solution",
                null,
                1,
                295);
        ItemList.sRocketFuel = GT_Mod.gregtechproxy.addFluid("rocket_fuel", "Rocket Fuel", null, 1, 295);
        new GT_Recipe(
                new ItemStack(Items.lava_bucket),
                new ItemStack(Blocks.obsidian),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1L),
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Electrum, 1L),
                30,
                2);

        GT_Recipe.GT_Recipe_Map.sSmallNaquadahReactorFuels.addRecipe(
                true,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.NaquadahEnriched, 1L) },
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadah, 1L) },
                null,
                null,
                null,
                0,
                0,
                50000);
        GT_Recipe.GT_Recipe_Map.sLargeNaquadahReactorFuels.addRecipe(
                true,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.NaquadahEnriched, 1L) },
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Naquadah, 1L) },
                null,
                null,
                null,
                0,
                0,
                250000);
        GT_Recipe.GT_Recipe_Map.sHugeNaquadahReactorFuels.addRecipe(
                true,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.NaquadahEnriched, 1L) },
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Naquadah, 1L) },
                null,
                null,
                null,
                0,
                0,
                500000);
        GT_Recipe.GT_Recipe_Map.sExtremeNaquadahReactorFuels.addRecipe(
                true,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadria, 1L) },
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadah, 1L) },
                null,
                null,
                null,
                0,
                0,
                250000);
        GT_Recipe.GT_Recipe_Map.sUltraHugeNaquadahReactorFuels.addRecipe(
                true,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Naquadria, 1L) },
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Naquadah, 1L) },
                null,
                null,
                null,
                0,
                0,
                1000000);
        GT_Recipe.GT_Recipe_Map.sFluidNaquadahReactorFuels.addRecipe(
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
        GT_Recipe.GT_Recipe_Map.sMagicFuels.addRecipe(
                true,
                new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.modID, "reinforcedSlate", 1L) },
                new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.modID, "blankSlate", 1L) },
                null,
                null,
                null,
                0,
                0,
                400);
        GT_Recipe.GT_Recipe_Map.sMagicFuels.addRecipe(
                true,
                new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.modID, "imbuedSlate", 1L) },
                new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.modID, "reinforcedSlate", 1L) },
                null,
                null,
                null,
                0,
                0,
                1000);
        GT_Recipe.GT_Recipe_Map.sMagicFuels.addRecipe(
                true,
                new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.modID, "demonicSlate", 1L) },
                new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.modID, "imbuedSlate", 1L) },
                null,
                null,
                null,
                0,
                0,
                8000);
        GT_Recipe.GT_Recipe_Map.sMagicFuels.addRecipe(
                true,
                new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.modID, "bloodMagicBaseItems", 1L, 27) },
                new ItemStack[] { GT_ModHandler.getModItem(BloodMagic.modID, "demonicSlate", 1L) },
                null,
                null,
                null,
                0,
                0,
                20000);

        GT_Values.RA.addFuel(GT_ModHandler.getModItem(Thaumcraft.modID, "ItemResource", 1L, 4), null, 4, 5);
        GT_Values.RA.addFuel(new ItemStack(Items.experience_bottle, 1), null, 10, 5);
        GT_Values.RA.addFuel(new ItemStack(Items.ghast_tear, 1), null, 50, 5);
        GT_Values.RA.addFuel(
                new ItemStack(Blocks.beacon, 1),
                null,
                Materials.NetherStar.mFuelPower * 2,
                Materials.NetherStar.mFuelType);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem(EnderIO.modID, "bucketRocket_fuel", 1), null, 250, 1);
    }
}
