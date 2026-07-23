package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;

public class ProcessingItem implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingItem() {
        OrePrefixes.item.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Material material, String oreDictName, String modName,
        ItemStack stack) {
        if (GTOreDictUnificator.getItemData(stack) == null && !oreDictName.equals("itemCertusQuartz")
            && !oreDictName.equals("itemNetherQuartz")) {
            switch (oreDictName) {
                case "itemSilicon":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials2Materials.Silicon, 3628800L));
                case "itemWheat":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials2Materials.Wheat, 3628800L));
                case "itemManganese":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials2Materials.Manganese, 3628800L));
                case "itemSalt":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials2Materials.Salt, 3628800L));
                case "itemMagnesium":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials2Materials.Magnesium, 3628800L));
                case "itemPhosphorite":
                    GTOreDictUnificator
                        .addItemData(stack, new ItemData(Materials2Materials.TricalciumPhosphate, 3628800L));
                case "itemSulfur":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials2Materials.Sulfur, 3628800L));
                case "itemAluminum":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials2Materials.Aluminium, 3628800L));
                case "itemSaltpeter":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials2Materials.Saltpeter, 3628800L));
                case "itemUranium":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials2Materials.Uranium, 3628800L));
            }
        }
    }
}
