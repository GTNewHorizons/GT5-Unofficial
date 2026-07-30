package tectech.loader.recipe;

import gregtech.api.enums.materials2.Materials;
import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.StructureLibAPI;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import tectech.thing.CustomItemList;
import tectech.thing.item.ItemEuMeterGT;

public class Crafting implements Runnable {

    @Override
    public void run() {

        // Front Rotation Tool
        GTModHandler.addCraftingRecipe(
            GTModHandler.getModItem(StructureLibAPI.MOD_ID, "item.structurelib.frontRotationTool", 1L, 0),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "fPR", " RP", "S h", 'P', OrePrefixes.plate.ingredient(Materials.Cobalt), 'R',
                OrePrefixes.stick.ingredient(Materials.Cobalt), 'S',
                OrePrefixes.stick.ingredient(Materials.Wood), });

        // GT EU reader
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemEuMeterGT.INSTANCE, 1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "PGW", "SCW", "BRN", 'P', OrePrefixes.plateDouble.ingredient(Materials.Steel), 'G',
                OrePrefixes.plate.ingredient(Materials.Glass), 'W',
                OrePrefixes.cableGt01.ingredient(Materials.Copper), 'S',
                OrePrefixes.stick.ingredient(Materials.Brass), 'C', ItemList.Casing_Coil_Cupronickel.get(1),
                'B', Dyes.dyeBlue, 'R', Dyes.dyeRed, 'N', Dyes.dyeBlack, });

        // Owner detector
        GTModHandler.addCraftingRecipe(
            CustomItemList.Machine_OwnerDetector.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "PPP", "GEG", "PPP", 'P', OrePrefixes.plate.ingredient(Materials.IronMagnetic),
                'G', OrePrefixes.plate.ingredient(Materials.Glass), 'E',
                OrePrefixes.gem.ingredient(Materials.EnderPearl) });

        // Tesla Base
        GTModHandler.addCraftingRecipe(
            CustomItemList.tM_TeslaBase.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "PhP", "PFP", "PwP", 'P',
                OrePrefixes.plate.ingredient(Materials.NickelZincFerrite), 'F',
                OrePrefixes.frameGt.ingredient(Materials.NickelZincFerrite) });

        // Tesla Toroid
        GTModHandler.addCraftingRecipe(
            CustomItemList.tM_TeslaToroid.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "PhP", "PFP", "PwP", 'P', OrePrefixes.foil.ingredient(Materials.Aluminium), 'F',
                OrePrefixes.frameGt.ingredient(Materials.Aluminium) });
    }
}
