package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

/// Removes IC2's crafting-table recipes for its Electronic and Advanced Circuits, which GregTech makes by its own
/// recipes.
public class ProcessingCircuit implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingCircuit() {
        OrePrefixes.circuit.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (GTUtility.areStacksEqual(stack, GTModHandler.getIC2Item("electronicCircuit", 1L))
            || GTUtility.areStacksEqual(stack, GTModHandler.getIC2Item("advancedCircuit", 1L))) {
            GTModHandler.removeRecipeByOutputDelayed(stack);
        }
    }
}
