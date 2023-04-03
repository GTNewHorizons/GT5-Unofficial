package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;

import static gregtech.api.enums.ModIDs.GregTech;

public class ProcessingCircuit implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingCircuit() {
        OrePrefixes.circuit.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
            ItemStack aStack) {
        if (gregtech.api.util.GT_OreDictUnificator.isBlacklisted(aStack) && aModName.equals(GregTech.modID)) return;
        switch (aMaterial.mName) {
            case "Good", "Data", "Elite", "Master", "Ultimate", "Superconductor", "Infinite", "Bio" -> {
                if (!GT_OreDictUnificator.isBlacklisted(aStack) && !aModName.equals(GregTech.modID))
                    GT_ModHandler.removeRecipeByOutputDelayed(aStack);
            }
            case "Primitive", "Advanced" -> GT_ModHandler.removeRecipeByOutputDelayed(aStack);
            case "Basic" -> {
                GT_ModHandler.removeRecipeByOutputDelayed(aStack);
                GT_ModHandler.addCraftingRecipe(
                        aStack,
                        GT_ModHandler.RecipeBits.BUFFERED,
                        new Object[] { "RIR", "VBV", "CCC", 'R', ItemList.Circuit_Parts_Resistor.get(1), 'C',
                                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.RedAlloy, 1), 'V',
                                ItemList.Circuit_Parts_Vacuum_Tube.get(1), 'B',
                                ItemList.Circuit_Board_Coated_Basic.get(1), 'I',
                                ItemList.IC2_Item_Casing_Steel.get(1) });
                GT_ModHandler.addShapelessCraftingRecipe(
                        GT_ModHandler.getIC2Item("electronicCircuit", 1L),
                        new Object[] { ItemList.Circuit_Integrated.getWildcard(1L) });
            }
        }
    }
}
