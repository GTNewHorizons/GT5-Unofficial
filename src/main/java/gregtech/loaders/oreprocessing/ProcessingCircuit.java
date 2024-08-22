package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.OreDictUnificator;

public class ProcessingCircuit implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingCircuit() {
        OrePrefixes.circuit.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (OreDictUnificator.isBlacklisted(aStack) && aModName.equals(GregTech.ID)) return;
        switch (aMaterial.mName) {
            case "Good", "Data", "Elite", "Master", "Ultimate", "Superconductor", "Infinite", "Bio" -> {
                if (!OreDictUnificator.isBlacklisted(aStack) && !aModName.equals(GregTech.ID))
                    GT_ModHandler.removeRecipeByOutputDelayed(aStack);
            }
            case "Primitive", "Basic", "Advanced" -> GT_ModHandler.removeRecipeByOutputDelayed(aStack);
        }
    }
}
