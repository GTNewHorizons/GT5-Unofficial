package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.objects.ItemData;
import gregtech.api.util.GT_OreDictUnificator;

public class ProcessingItem implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingItem() {
        OrePrefixes.item.add(this);
    }

    @Override
    public void registerOre(OrePrefixes aPrefix, Materials aMaterial, String aOreDictName, String aModName,
        ItemStack aStack) {
        if (GT_OreDictUnificator.getItemData(aStack) == null && !aOreDictName.equals("itemCertusQuartz")
            && !aOreDictName.equals("itemNetherQuartz")) {
            switch (aOreDictName) {
                case "itemSilicon":
                    GT_OreDictUnificator.addItemData(aStack, new ItemData(Materials.Silicon, 3628800L));
                case "itemWheat":
                    GT_OreDictUnificator.addItemData(aStack, new ItemData(Materials.Wheat, 3628800L));
                case "itemManganese":
                    GT_OreDictUnificator.addItemData(aStack, new ItemData(Materials.Manganese, 3628800L));
                case "itemSalt":
                    GT_OreDictUnificator.addItemData(aStack, new ItemData(Materials.Salt, 3628800L));
                case "itemMagnesium":
                    GT_OreDictUnificator.addItemData(aStack, new ItemData(Materials.Magnesium, 3628800L));
                case "itemPhosphorite":
                    GT_OreDictUnificator.addItemData(aStack, new ItemData(Materials.TricalciumPhosphate, 3628800L));
                case "itemSulfur":
                    GT_OreDictUnificator.addItemData(aStack, new ItemData(Materials.Sulfur, 3628800L));
                case "itemAluminum":
                    GT_OreDictUnificator.addItemData(aStack, new ItemData(Materials.Aluminium, 3628800L));
                case "itemSaltpeter":
                    GT_OreDictUnificator.addItemData(aStack, new ItemData(Materials.Saltpeter, 3628800L));
                case "itemUranium":
                    GT_OreDictUnificator.addItemData(aStack, new ItemData(Materials.Uranium, 3628800L));
            }
        }
    }
}
