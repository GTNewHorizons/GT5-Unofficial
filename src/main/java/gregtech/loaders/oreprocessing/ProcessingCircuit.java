package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.Mods.GregTech;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class ProcessingCircuit implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingCircuit() {
        OrePrefixes.circuit.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        registerOre(prefix, MU.material(material), oreDictName, modName, stack);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (GTOreDictUnificator.isBlacklisted(stack) && GregTech.ID.equals(modName)) return;
        switch (MU.internalName(material)) {
            case "Good", "Data", "Elite", "Master", "Ultimate", "Superconductor", "Infinite", "Bio" -> {
                if (!GTOreDictUnificator.isBlacklisted(stack) && !GregTech.ID.equals(modName))
                    GTModHandler.removeRecipeByOutputDelayed(stack);
            }
            case "Primitive", "Basic", "Advanced" -> GTModHandler.removeRecipeByOutputDelayed(stack);
        }
    }
}
