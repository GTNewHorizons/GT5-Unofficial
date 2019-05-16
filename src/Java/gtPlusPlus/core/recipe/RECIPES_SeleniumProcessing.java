package gtPlusPlus.core.recipe;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.MISC_MATERIALS;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.core.util.reflect.AddGregtechRecipe;
import net.minecraft.item.ItemStack;

public class RECIPES_SeleniumProcessing {

    public static void init() {
        
        //We need this
        MaterialUtils.generateSpecialDustAndAssignToAMaterial(MISC_MATERIALS.SELENIUM_DIOXIDE);       
        
        // Makes Selenium Dioxide        
        processCopperRecipes();
        
        //Liquify the Dried Dioxide
        AddGregtechRecipe.addCokeAndPyrolyseRecipes(MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1), 13, FluidUtils.getSteam(500), null, MISC_MATERIALS.SELENIUM_DIOXIDE.getFluid(1000), 120, 1024);
        
        // Produce Selenious Acid        
        AddGregtechRecipe.addCokeAndPyrolyseRecipes(MISC_MATERIALS.SELENIUM_DIOXIDE.getCell(1), 14, FluidUtils.getHotWater(4000), CI.emptyCells(1), MISC_MATERIALS.SELENIOUS_ACID.getFluid(1000), 120, 2048);
        
        // Make Selenium        
        CORE.RA.addBlastSmelterRecipe(
                new ItemStack[] { 
                        ItemUtils.getGregtechCircuit(14),
                        ItemUtils.getItemStackOfAmountFromOreDict("cellSulfuricAcid", 8),
                        ELEMENT.getInstance().CARBON.getDust(16),
                },
                MISC_MATERIALS.SELENIOUS_ACID.getFluid(750),
                ELEMENT.getInstance().SELENIUM.getFluid(144 * 1),
                new ItemStack[] {
                        CI.emptyCells(8),
                        ELEMENT.getInstance().SELENIUM.getIngot(1),
                        ELEMENT.getInstance().SELENIUM.getIngot(1),    
                },
                new int[] {10000, 2000, 2000},
                20 * 300,
                7200);  
        
        
        /*// Old recipes for Selenium Roasting
        CORE.RA.addBlastSmelterRecipe(
                new ItemStack[] { 
                        ItemUtils.getGregtechCircuit(16),
                        ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedPyrite", 8),
                        ELEMENT.getInstance().CARBON.getDust(32),
                },
                Materials.SulfuricAcid.getFluid(4000),
                ELEMENT.getInstance().SELENIUM.getFluid(144),
                0,
                20 * 300,
                2000);      
        CORE.RA.addBlastSmelterRecipe(
                new ItemStack[] { 
                        ItemUtils.getGregtechCircuit(17),
                        ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedChalcopyrite", 8),
                        ELEMENT.getInstance().CARBON.getDust(32),
                },
                Materials.SulfuricAcid.getFluid(4000),
                ELEMENT.getInstance().SELENIUM.getFluid(144),
                0,
                20 * 300,
                2000);
        CORE.RA.addBlastSmelterRecipe(
                new ItemStack[] { 
                        ItemUtils.getGregtechCircuit(18),
                        ItemUtils.getItemStackOfAmountFromOreDict("crushedPurifiedGalena", 8),
                        ELEMENT.getInstance().CARBON.getDust(32),
                },
                Materials.SulfuricAcid.getFluid(4000),
                ELEMENT.getInstance().SELENIUM.getFluid(144),
                0,
                20 * 300,
                2000);*/   
    }
    
    
    public static void processCopperRecipes() {
        
        //Copper
        CORE.RA.addDehydratorRecipe(
                new ItemStack[]{
                    ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Copper, 1), // Item Input
                },
                FluidUtils.getHotWater(1000), // Fluid
                MISC_MATERIALS.SELENIUM_DIOXIDE.getFluid(20), // Fluid
                new ItemStack[] {
                        ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Copper, 1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                        }, // Output     
                new int[] {
                       10000,
                       100,
                       100,
                       500,
                       500,
                       500,
                       1000,
                       1000,
                       1000
                },
                40 * 20, // Time in ticks
                1024); // EU
        
        
        //Tetra
        CORE.RA.addDehydratorRecipe(
                new ItemStack[]{
                    ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Tetrahedrite, 1), // Item Input
                },
                FluidUtils.getHotWater(1000), // Fluid
                MISC_MATERIALS.SELENIUM_DIOXIDE.getFluid(10), // Fluid
                new ItemStack[] {
                        ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Tetrahedrite, 1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                        }, // Output     
                new int[] {
                       10000,
                       100,
                       100,
                       300,
                       300,
                       300,
                       800,
                       800,
                       800
                },
                40 * 20, // Time in ticks
                1024); // EU
        //Chalco
        CORE.RA.addDehydratorRecipe(
                new ItemStack[]{
                    ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Chalcopyrite, 1), // Item Input
                },
                FluidUtils.getHotWater(1000), // Fluid
                MISC_MATERIALS.SELENIUM_DIOXIDE.getFluid(10), // Fluid
                new ItemStack[] {
                        ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Chalcopyrite, 1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                        }, // Output     
                new int[] {
                       10000,
                       100,
                       100,
                       300,
                       300,
                       300,
                       800,
                       800,
                       800
                },
                40 * 20, // Time in ticks
                1024); // EU
        //Malachite
        CORE.RA.addDehydratorRecipe(
                new ItemStack[]{
                    ItemUtils.getOrePrefixStack(OrePrefixes.crushedCentrifuged, Materials.Malachite, 1), // Item Input
                },
                FluidUtils.getHotWater(1000), // Fluid
                MISC_MATERIALS.SELENIUM_DIOXIDE.getFluid(10), // Fluid
                new ItemStack[] {
                        ItemUtils.getOrePrefixStack(OrePrefixes.crushedPurified, Materials.Malachite, 1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getSmallDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                        MISC_MATERIALS.SELENIUM_DIOXIDE.getTinyDust(1),
                        }, // Output     
                new int[] {
                       10000,
                       100,
                       100,
                       300,
                       300,
                       300,
                       800,
                       800,
                       800
                },
                40 * 20, // Time in ticks
                1024); // EU
    }

}
