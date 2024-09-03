package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.HardcoreEnderExpansion;
import static gregtech.api.util.GTModHandler.getModItem;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;

public class OreDictUnification implements Runnable {

    @Override
    public void run() {
        if (HardcoreEnderExpansion.isModLoaded()) {
            GTOreDictUnificator.set(
                OrePrefixes.ingot,
                Materials.HeeEndium,
                getModItem(HardcoreEnderExpansion.ID, "endium_ingot", 1),
                true,
                true);
        }
    }
}
