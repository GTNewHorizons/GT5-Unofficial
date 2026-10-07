package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.GTValues.M;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.material.MaterialData;
import gregtech.api.objects.ItemData;

/// Records an `item`-prefixed foreign item as one unit of its material.
public class ProcessingItem implements PartRecipeGenerator {

    public static final ProcessingItem INSTANCE = new ProcessingItem();

    private ProcessingItem() {}

    @Override
    public void generate(OrePrefixes prefix, Material material, String oreDictName, ItemStack stack) {
        MaterialData.add(stack, new ItemData(material, M));
    }
}
