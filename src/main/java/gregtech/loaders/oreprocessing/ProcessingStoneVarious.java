package gregtech.loaders.oreprocessing;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.OrePrefixes;

public class ProcessingStoneVarious implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public ProcessingStoneVarious() {
        OrePrefixes.stone.add(this);
        OrePrefixes.stoneCobble.add(this);
        OrePrefixes.stoneBricks.add(this);
        OrePrefixes.stoneChiseled.add(this);
        OrePrefixes.stoneCracked.add(this);
        OrePrefixes.stoneMossy.add(this);
        OrePrefixes.stoneMossyBricks.add(this);
        OrePrefixes.stoneSmooth.add(this);
    }

    @Override
    public void registerOre(OrePrefixes prefix, gregtech.api.enums.Materials material, String oreDictName,
        String modName, ItemStack stack) {
        // no recipes currently.
    }
}
