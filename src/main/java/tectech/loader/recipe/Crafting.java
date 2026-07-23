package tectech.loader.recipe;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.StructureLibAPI;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Materials;
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
            new Object[] { "fPR", " RP", "S h", 'P', OrePrefixes.plate.ingredient(Materials2Materials.Cobalt), 'R',
                OrePrefixes.stick.ingredient(Materials2Materials.Cobalt), 'S',
                OrePrefixes.stick.ingredient(Materials2Materials.Wood), });

        // GT EU reader
        GTModHandler.addCraftingRecipe(
            new ItemStack(ItemEuMeterGT.INSTANCE, 1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "PGW", "SCW", "BRN", 'P', OrePrefixes.plateDouble.ingredient(Materials2Materials.Steel), 'G',
                OrePrefixes.plate.ingredient(Materials2Materials.Glass), 'W',
                OrePrefixes.cableGt01.ingredient(Materials2Materials.Copper), 'S',
                OrePrefixes.stick.ingredient(Materials2Materials.Brass), 'C', ItemList.Casing_Coil_Cupronickel.get(1),
                'B', Dyes.dyeBlue, 'R', Dyes.dyeRed, 'N', Dyes.dyeBlack, });

        // Owner detector
        GTModHandler.addCraftingRecipe(
            CustomItemList.Machine_OwnerDetector.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "PPP", "GEG", "PPP", 'P', OrePrefixes.plate.ingredient(Materials2Materials.IronMagnetic),
                'G', OrePrefixes.plate.ingredient(Materials2Materials.Glass), 'E',
                OrePrefixes.gem.ingredient(Materials2Materials.EnderPearl) });

        // Tesla Base
        GTModHandler.addCraftingRecipe(
            CustomItemList.tM_TeslaBase.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "PhP", "PFP", "PwP", 'P',
                OrePrefixes.plate.ingredient(Materials2Materials.NickelZincFerrite), 'F',
                OrePrefixes.frameGt.ingredient(Materials2Materials.NickelZincFerrite) });

        // Tesla Toroid
        GTModHandler.addCraftingRecipe(
            CustomItemList.tM_TeslaToroid.get(1),
            GTModHandler.RecipeBits.BUFFERED | GTModHandler.RecipeBits.NOT_REMOVABLE,
            new Object[] { "PhP", "PFP", "PwP", 'P', OrePrefixes.foil.ingredient(Materials2Materials.Aluminium), 'F',
                OrePrefixes.frameGt.ingredient(Materials2Materials.Aluminium) });
    }
}
