package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MU;

public class ProcessingAll implements gregtech.api.interfaces.IOreRecipeRegistrator { // TODO ACTUALLY COMPARE ALL THE
                                                                                      // PROCESSING CLASSES

    public ProcessingAll() {
        for (OrePrefixes tPrefix : OrePrefixes.VALUES) tPrefix.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        registerOre(prefix, MU.material(material), oreDictName, modName, stack);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (((stack.getItem() instanceof net.minecraft.item.ItemBlock))
            && (prefix.getDefaultStackSize() < stack.getItem()
                .getItemStackLimit(stack)))
            stack.getItem()
                .setMaxStackSize(prefix.getDefaultStackSize());
    }
}
