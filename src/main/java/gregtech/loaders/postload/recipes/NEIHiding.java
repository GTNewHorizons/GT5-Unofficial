package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.NotEnoughItems;

import codechicken.nei.api.API;
import gregtech.api.enums.ItemList;

public class NEIHiding implements Runnable {

    @Override
    public void run() {
        if (!NotEnoughItems.isModLoaded() || !Forestry.isModLoaded()) {
            return;
        }

        for (ItemList cover : ItemList.FORESTRY_DECORATIVE_PLANKS) {
            API.hideItem(cover.get(1));
        }

    }
}
