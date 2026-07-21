package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

public class ProcessingAll implements gregtech.api.interfaces.IOreRecipeRegistrator { // TODO ACTUALLY COMPARE ALL THE
                                                                                      // PROCESSING CLASSES

    public ProcessingAll() {
        for (OrePrefixes tPrefix : OrePrefixes.VALUES) tPrefix.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        if (((stack.getItem() instanceof net.minecraft.item.ItemBlock))
            && (prefix.getDefaultStackSize() < stack.getItem()
                .getItemStackLimit(stack)))
            stack.getItem()
                .setMaxStackSize(prefix.getDefaultStackSize());
    }
}
