package gregtech.loaders.oreprocessing;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.item.ItemStack;

public class ProcessingCircuit implements gregtech.api.interfaces.IOreRecipeRegistrator {
    public ProcessingCircuit() {
        OrePrefixes.circuit.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName, ItemStack aStack) {
    	if(gregtech.api.util.GT_OreDictUnificator.isBlacklisted(aStack)&&aModName.equals("gregtech"))return;
        switch (aMaterial.mName) {
            case "Good":
            case "Data":
            case "Elite":
            case "Master":
            case "Ultimate":
            case "Superconductor":
            case "Infinite":
            case "Bio":
                if (!gregtech.api.util.GT_OreDictUnificator.isBlacklisted(aStack)&&!aModName.equals("gregtech"))
                    GT_ModHandler.removeRecipeByOutputDelayed(aStack);
                break;
            case "Primitive":
            case "Advanced":
                GT_ModHandler.removeRecipeByOutputDelayed(aStack);
               break;
            case "Basic":
                GT_ModHandler.removeRecipeByOutputDelayed(aStack);
                GT_ModHandler.addCraftingRecipe(aStack, GT_ModHandler.RecipeBits.BUFFERED,  new Object[]{"RIR","VBV","CCC",'R',ItemList.Circuit_Parts_Resistor.get(1),'C',GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.RedAlloy, 1),'V', ItemList.Circuit_Parts_Vacuum_Tube.get(1),'B',ItemList.Circuit_Board_Coated_Basic.get(1),'I',ItemList.IC2_Item_Casing_Steel.get(1)});
                GT_ModHandler.addShapelessCraftingRecipe(GT_ModHandler.getIC2Item("electronicCircuit", 1L), new Object[]{ItemList.Circuit_Integrated.getWildcard(1L)});
               break;
        }
    }
}
