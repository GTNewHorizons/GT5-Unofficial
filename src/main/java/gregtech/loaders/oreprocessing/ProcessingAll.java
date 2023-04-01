package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;

public class ProcessingAll implements gregtech.api.interfaces.IOreRecipeRegistrator { // TODO ACTUALLY COMPARE ALL THE
                                                                                      // PROCESSING CLASSES

    public ProcessingAll() {
        for (OrePrefixes tPrefix : OrePrefixes.values()) tPrefix.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
            ItemStack aStack) {
        if (((aStack.getItem() instanceof net.minecraft.item.ItemBlock)) && (aPrefix.mDefaultStackSize
                < aStack.getItem()
                        .getItemStackLimit(aStack)))
            aStack.getItem()
                  .setMaxStackSize(aPrefix.mDefaultStackSize);
    }
}
