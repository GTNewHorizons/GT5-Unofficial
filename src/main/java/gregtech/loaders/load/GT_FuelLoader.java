package gregtech.loaders.load;

import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class GT_FuelLoader
        implements Runnable {
    public void run() {
        GT_Log.out.println("GT_Mod: Initializing various Fuels.");
        ItemList.sNitricAcid = GT_Mod.gregtechproxy.addFluid("nitricacid", "Nitric Acid ", Materials.NitricAcid, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NitricAcid, 1), ItemList.Cell_Empty.get(1, new Object[0]), 1000);
        ItemList.sBlueVitriol = GT_Mod.gregtechproxy.addFluid("solution.bluevitriol", "Blue Vitriol water solution", null, 1, 295);
        ItemList.sNickelSulfate = GT_Mod.gregtechproxy.addFluid("solution.nickelsulfate", "Nickel sulfate water solution", null, 1, 295);
        ItemList.sIndiumConcentrate = GT_Mod.gregtechproxy.addFluid("indiumconcentrate", "Indium Concentrate", null, 1, 295);
        ItemList.sLeadZincSolution = GT_Mod.gregtechproxy.addFluid("leadzincsolution", "Lead-Zinc solution", null, 1, 295);
        ItemList.sRocketFuel = GT_Mod.gregtechproxy.addFluid("rocket_fuel", "Rocket Fuel", null, 1, 295);
        new GT_Recipe(new ItemStack(Items.lava_bucket), new ItemStack(Blocks.obsidian), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Copper, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Tin, 1L), GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Electrum, 1L), 30, 2);

        GT_Recipe.GT_Recipe_Map.sSmallNaquadahReactorFuels.addRecipe(true, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.NaquadahEnriched, 1L)}, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Naquadah, 1L)}, null, null, null, 0, 0, 25000);
        GT_Recipe.GT_Recipe_Map.sLargeNaquadahReactorFuels.addRecipe(true, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.NaquadahEnriched, 1L)}, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Naquadah, 1L)}, null, null, null, 0, 0, 200000);
        GT_Recipe.GT_Recipe_Map.sFluidNaquadahReactorFuels.addRecipe(true, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NaquadahEnriched, 1L)}, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.cell,  Materials.Naquadah, 1L)}, null, null, null, 0, 0, 1400000);
        GT_Recipe.GT_Recipe_Map.sHugeNaquadahReactorFuels.addRecipe(true, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Naquadria, 1L)}, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.stick,  Materials.Naquadah, 1L)}, null, null, null, 0, 0, 500000);
        GT_Recipe.GT_Recipe_Map.sExtraHugeNaquadahReactorFuels.addRecipe(true, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Naquadria, 1L)}, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.stickLong,  Materials.Naquadah, 1L)}, null, null, null, 0, 0, 1000000);
        GT_Recipe.GT_Recipe_Map.sUltraHugeNaquadahReactorFuels.addRecipe(true, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.cell, Materials.FluidNaquadahFuel, 1L)}, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.cell,  Materials.Naquadah, 1L)}, null, null, null, 0, 0, 2000000);
        GT_Recipe.GT_Recipe_Map.sExtremeHugeNaquadahReactorFuels.addRecipe(true, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.cell, Materials.FluidEnrichedNaquadahFuel, 1L)}, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.cell,  Materials.Naquadah, 1L)}, null, null, null, 0, 0, 4000000);
        GT_Recipe.GT_Recipe_Map.sInfiniteHugeNaquadahReactorFuels.addRecipe(true, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.cell, Materials.FluidNaquadriaFuel, 1L)}, new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.cell,  Materials.Naquadah, 1L)}, null, null, null, 0, 0, 8000000);
        
        //GT_Recipe.GT_Recipe_Map.sFluidDarkMatterReactorFuels.addRecipe(true, null, null, null, new FluidStack[]{Materials.FluidDarkMatterFuel.getFluid(10)}, new FluidStack[]{Materials.UUMatter.getFluid(5)}, 0, 0, 8000000);
        
        GT_Recipe.GT_Recipe_Map.sAntimatterReactorFuels.addRecipe(true, new ItemStack[]{ItemList.Magnetic_Confinement_Pod_AntiHydrogen.get(1L, new Object[0]), ItemList.Magnetic_Confinement_Pod_Hydrogen.get(1L, new Object[0])},  new ItemStack[]{ItemList.Magnetic_Confinement_Pod.get(2L, new Object[0])}, null, new FluidStack[]{Materials.Nitrogen.getGas(4)}, new FluidStack[]{Materials.Nitrogen.getPlasma(4)}, 0, 0, 1800);
        
        GT_Recipe.GT_Recipe_Map.sNuclearReactor1Fuels.addRecipe(true, new ItemStack[]{ItemList.Uraniumcell_1.get(1L, new Object[0])},  new ItemStack[]{GT_ModHandler.getIC2Item("reactorDepletedUraniumSimple", 1)}, null, new FluidStack[]{Materials.Water.getFluid(20)}, new FluidStack[]{GT_ModHandler.getSteam(20)}, 0, 0, 1000);
        
        GT_Recipe.GT_Recipe_Map.sMagicFuels.addRecipe(true, new ItemStack[]{GT_ModHandler.getModItem("AWWayofTime","reinforcedSlate",1L)},  new ItemStack[]{GT_ModHandler.getModItem("AWWayofTime","blankSlate",1L)}, null, null, null, 0, 0, 100);
        GT_Recipe.GT_Recipe_Map.sMagicFuels.addRecipe(true, new ItemStack[]{GT_ModHandler.getModItem("AWWayofTime","imbuedSlate",1L)},  new ItemStack[]{GT_ModHandler.getModItem("AWWayofTime","reinforcedSlate",1L)}, null, null, null, 0, 0, 400);
        GT_Recipe.GT_Recipe_Map.sMagicFuels.addRecipe(true, new ItemStack[]{GT_ModHandler.getModItem("AWWayofTime","demonicSlate",1L)},  new ItemStack[]{GT_ModHandler.getModItem("AWWayofTime","imbuedSlate",1L)}, null, null, null, 0, 0, 1000);
        GT_Recipe.GT_Recipe_Map.sMagicFuels.addRecipe(true, new ItemStack[]{GT_ModHandler.getModItem("AWWayofTime","bloodMagicBaseItems",1L,27)},  new ItemStack[]{GT_ModHandler.getModItem("AWWayofTime","demonicSlate",1L)}, null, null, null, 0, 0, 5000);
        
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("Thaumcraft", "ItemResource", 1L, 4), null, 4, 5);
        GT_Values.RA.addFuel(new ItemStack(Items.experience_bottle, 1), null, 10, 5);
        GT_Values.RA.addFuel(new ItemStack(Items.ghast_tear, 1), null, 50, 5);
        GT_Values.RA.addFuel(new ItemStack(Blocks.beacon, 1), null, Materials.NetherStar.mFuelPower * 2, Materials.NetherStar.mFuelType);
        GT_Values.RA.addFuel(GT_ModHandler.getModItem("EnderIO", "bucketRocket_fuel", 1), null, 250, 1);
        if(GregTech_API.mMagneticraft){
        	 GT_Values.RA.addFuel(GT_ModHandler.getModItem("Magneticraft", "item.bucket_light_oil", 1), null, 256, 0);
        	 GT_Values.RA.addFuel(GT_ModHandler.getModItem("Magneticraft", "item.bucket_heavy_oil", 1), null, 192, 3);
        }
        if(GregTech_API.mImmersiveEngineering){
       	 GT_Values.RA.addFuel(GT_ModHandler.getModItem("ImmersiveEngineering", "fluidContainers", 1, 7), null, 128, 0);
        }
        if(Loader.isModLoaded("PneumaticCraft")){
          	 GT_Values.RA.addFuel(GT_ModHandler.getModItem("PneumaticCraft", "pgBucket", 1), null, 512, 1);
          	 GT_Values.RA.addFuel(GT_ModHandler.getModItem("PneumaticCraft", "fuelBucket", 1), null, 400, 0);
          	 GT_Values.RA.addFuel(GT_ModHandler.getModItem("PneumaticCraft", "fuelBucket", 1, 1), null, 400, 0);
          	 GT_Values.RA.addFuel(GT_ModHandler.getModItem("PneumaticCraft", "keroseneBucket", 1), null, 256, 0);
          	 GT_Values.RA.addFuel(GT_ModHandler.getModItem("PneumaticCraft", "dieselBucket", 1), null, 200, 0);        	
        }
    }
}
