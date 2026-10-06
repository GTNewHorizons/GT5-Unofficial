package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.material.MaterialData;
import gregtech.api.objects.ItemData;

public class ProcessingItem implements PartRecipeGenerator {

    public static final ProcessingItem INSTANCE = new ProcessingItem();

    private ProcessingItem() {}

    @Override
    public void generate(OrePrefixes aPrefix, Material aMaterial, String aOreDictName, ItemStack aStack) {

        switch (aOreDictName) {
            case "itemSilicon" -> MaterialData.add(aStack, new ItemData(Materials.Silicon, 3628800L));
            case "itemWheat" -> MaterialData.add(aStack, new ItemData(Materials.Wheat, 3628800L));
            case "itemManganese" -> MaterialData.add(aStack, new ItemData(Materials.Manganese, 3628800L));
            case "itemMagnesium" -> MaterialData.add(aStack, new ItemData(Materials.Magnesium, 3628800L));
            case "itemPhosphorite" -> MaterialData.add(aStack, new ItemData(Materials.TricalciumPhosphate, 3628800L));
            case "itemSulfur" -> MaterialData.add(aStack, new ItemData(Materials.Sulfur, 3628800L));
            case "itemAluminum" -> MaterialData.add(aStack, new ItemData(Materials.Aluminium, 3628800L));
            case "itemSaltpeter" -> MaterialData.add(aStack, new ItemData(Materials.Saltpeter, 3628800L));
            case "itemUranium" -> MaterialData.add(aStack, new ItemData(Materials.Uranium, 3628800L));
        }
    }
}
