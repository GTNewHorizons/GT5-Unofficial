package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GTOreDictUnificator;

public class ProcessingItem implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingItem() {
        OrePrefixes.item.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, Materials material, String oreDictName, String modName,
        ItemStack stack) {
        if (GTOreDictUnificator.getItemData(stack) == null && !oreDictName.equals("itemCertusQuartz")
            && !oreDictName.equals("itemNetherQuartz")) {
            switch (oreDictName) {
                case "itemSilicon":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials.Silicon, 3628800L));
                case "itemWheat":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials.Wheat, 3628800L));
                case "itemManganese":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials.Manganese, 3628800L));
                case "itemSalt":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials.Salt, 3628800L));
                case "itemMagnesium":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials.Magnesium, 3628800L));
                case "itemPhosphorite":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials.TricalciumPhosphate, 3628800L));
                case "itemSulfur":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials.Sulfur, 3628800L));
                case "itemAluminum":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials.Aluminium, 3628800L));
                case "itemSaltpeter":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials.Saltpeter, 3628800L));
                case "itemUranium":
                    GTOreDictUnificator.addItemData(stack, new ItemData(Materials.Uranium, 3628800L));
            }
        }
    }
}
