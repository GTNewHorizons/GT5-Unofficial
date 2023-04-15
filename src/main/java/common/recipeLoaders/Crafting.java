package common.recipeLoaders;

import net.minecraft.item.ItemStack;

import util.Util;

import common.Blocks;
import common.TileEntities;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

public class Crafting implements Runnable {

    @Override
    public void run() {
        // TFFT Controller
        GT_ModHandler.addCraftingRecipe(
                TileEntities.tfft.getStackForm(1),
                new Object[] { "ESE", "FTF", "CVC", 'E',
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.EnderEye, 1), 'S',
                        ItemList.Cover_Screen.get(1), 'F', ItemList.Field_Generator_LV.get(1), 'T',
                        new ItemStack(Blocks.tfftStorageField, 1), 'C', "circuitData", 'V',
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.VibrantAlloy, 1), });

        // SOFC Controller mk1
        final Object[] mk1_recipe = { "CCC", "PHP", "FBL", 'C', OrePrefixes.circuit.get(Materials.Advanced), 'P',
                ItemList.Electric_Pump_HV.get(1L), 'H', ItemList.Hull_HV.get(1L), 'F',
                GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.StainlessSteel, 1), 'B',
                GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Gold, 1), 'L',
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel, 1) };
        GT_ModHandler.addCraftingRecipe(TileEntities.sofc1.getStackForm(1), mk1_recipe);

        // SOFC Controller mk2
        final Object[] mk2_recipe = { "CCC", "PHP", "FBL", 'C', OrePrefixes.circuit.get(Materials.Master), 'P',
                ItemList.Electric_Pump_IV.get(1L), 'H', ItemList.Hull_IV.get(1L), 'F',
                GT_OreDictUnificator.get(OrePrefixes.pipeSmall, Materials.Ultimate, 1), 'B',
                Util.getStackofAmountFromOreDict("wireGt04SuperconductorEV", 1), 'L',
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Ultimate, 1) };
        GT_ModHandler.addCraftingRecipe(TileEntities.sofc2.getStackForm(1), mk2_recipe);

        // LSC Controller
        final Object[] lsc_recipe = { "LPL", "CBC", "LPL", 'L', ItemList.IC2_LapotronCrystal.getWildcard(1L), 'P',
                ItemList.Circuit_Chip_PIC.get(1L), 'C', OrePrefixes.circuit.get(Materials.Master), 'B',
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 0), };
        GT_ModHandler.addCraftingRecipe(TileEntities.lsc.getStackForm(1), lsc_recipe);

        // LSC casing
        final Object[] lcBase_recipe = { "WBW", "RLR", "WBW", 'W', OrePrefixes.plate.get(Materials.Tantalum), 'B',
                OrePrefixes.frameGt.get(Materials.TungstenSteel), 'R',
                OrePrefixes.stickLong.get(Materials.TungstenSteel), 'L', OrePrefixes.block.get(Materials.Lapis) };
        GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 0), lcBase_recipe);

        // Empty Capacitor
        final Object[] lcEmpty_recipe = { "SLS", "L L", "SLS", 'S', OrePrefixes.screw.get(Materials.Lapis), 'L',
                OrePrefixes.plate.get(Materials.Lapis) };
        GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6), lcEmpty_recipe);

        // EV Capacitor
        final Object[] lcEV_recipe = { "SLS", "LCL", "SLS", 'S', OrePrefixes.screw.get(Materials.Lapis), 'L',
                OrePrefixes.plate.get(Materials.Lapis), 'C',
                GT_ModHandler.getIC2Item("lapotronCrystal", 1L, GT_Values.W) };
        GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 7), lcEV_recipe);

        // IV Capacitor
        final Object[] lcIV_recipe = { "SLS", "LOL", "SLS", 'S', OrePrefixes.screw.get(Materials.Lapis), 'L',
                OrePrefixes.plate.get(Materials.Lapis), 'O', ItemList.Energy_LapotronicOrb.get(1L) };
        GT_ModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1), lcIV_recipe);

    }
}
