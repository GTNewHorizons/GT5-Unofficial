package kekztech.common.recipeLoaders;

import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.Circuits;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TieredItems;
import gregtech.api.enums.materials2.Materials;
import gregtech.api.enums.materials2.Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import kekztech.common.Blocks;
import kekztech.common.TileEntities;
import kekztech.util.Util;

public class Crafting implements Runnable {

    @Override
    public void run() {
        // TFFT Controller
        GTModHandler.addCraftingRecipe(
            TileEntities.tfft.getStackForm(1),
            new Object[] { "ESE", "FTF", "CVC", 'E',
                MaterialLibAPI.getStack(Materials.EnderEye, Shapes.screw, (int) (1)), 'S', ItemList.Cover_Screen.get(1),
                'F', ItemList.Field_Generator_LV.get(1), 'T', new ItemStack(Blocks.tfftStorageField, 1), 'C',
                "circuitData", 'V', MaterialLibAPI.getStack(Materials.VibrantAlloy, Shapes.rotor, (int) (1)), });

        // SOFC Controller mk1
        final Object[] mk1_recipe = { "CCC", "PHP", "FBL", 'C', Circuits.HV.getIngredient(), 'P',
            ItemList.Electric_Pump_HV.get(1L), 'H', ItemList.Hull_HV.get(1L), 'F',
            GTOreDictUnificator.get(OrePrefixes.pipeSmall, Materials.StainlessSteel, 1), 'B',
            GTOreDictUnificator.get(OrePrefixes.cableGt02, Materials.Gold, 1), 'L',
            GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.StainlessSteel, 1) };
        GTModHandler.addCraftingRecipe(TileEntities.sofc1.getStackForm(1), mk1_recipe);

        // SOFC Controller mk2
        final Object[] mk2_recipe = { "CCC", "PHP", "FBL", 'C', Circuits.LuV.getIngredient(), 'P',
            ItemList.Electric_Pump_IV.get(1L), 'H', ItemList.Hull_IV.get(1L), 'F', TieredItems.ZPM.getPipeSmall(1), 'B',
            Util.getStackofAmountFromOreDict("wireGt04SuperconductorEV", 1), 'L', TieredItems.ZPM.getPipeMedium(1) };
        GTModHandler.addCraftingRecipe(TileEntities.sofc2.getStackForm(1), mk2_recipe);

        // LSC Controller
        final Object[] lsc_recipe = { "LPL", "CBC", "LPL", 'L', ItemList.IC2_LapotronCrystal.getWildcard(1L), 'P',
            ItemList.Circuit_Chip_PIC.get(1L), 'C', Circuits.LuV.getIngredient(), 'B',
            new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 0), };
        GTModHandler.addCraftingRecipe(TileEntities.lsc.getStackForm(1), lsc_recipe);

        // LSC casing
        final Object[] lcBase_recipe = { "WBW", "RLR", "WBW", 'W', OrePrefixes.plate.ingredient(Materials.Tantalum),
            'B', OrePrefixes.frameGt.ingredient(Materials.TungstenSteel), 'R',
            OrePrefixes.stickLong.ingredient(Materials.TungstenSteel), 'L',
            OrePrefixes.block.ingredient(Materials.Lapis) };
        GTModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 0), lcBase_recipe);

        // Empty Capacitor
        final Object[] lcEmpty_recipe = { "SLS", "L L", "SLS", 'S', OrePrefixes.screw.ingredient(Materials.Lapis), 'L',
            OrePrefixes.plate.ingredient(Materials.Lapis) };
        GTModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6), lcEmpty_recipe);

        // EV Capacitor
        final Object[] lcEV_recipe = { "SLS", "LCL", "SLS", 'S', OrePrefixes.screw.ingredient(Materials.Lapis), 'L',
            OrePrefixes.plate.ingredient(Materials.Lapis), 'C',
            GTModHandler.getIC2Item("lapotronCrystal", 1L, WILDCARD) };
        GTModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 7), lcEV_recipe);

        // IV Capacitor
        final Object[] lcIV_recipe = { "SLS", "LOL", "SLS", 'S', OrePrefixes.screw.ingredient(Materials.Lapis), 'L',
            OrePrefixes.plate.ingredient(Materials.Lapis), 'O', ItemList.Energy_LapotronicOrb.get(1L) };
        GTModHandler.addCraftingRecipe(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1), lcIV_recipe);

    }
}
