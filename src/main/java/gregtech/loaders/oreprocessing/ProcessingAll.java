package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;

public class ProcessingAll implements gregtech.api.interfaces.IOreRecipeRegistrator { // TODO ACTUALLY COMPARE ALL THE
                                                                                      // PROCESSING CLASSES

    public static final ProcessingAll INSTANCE = new ProcessingAll();

    private ProcessingAll() {}

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
