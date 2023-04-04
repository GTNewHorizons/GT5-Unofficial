package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.Forestry;
import static gregtech.api.enums.Mods.NotEnoughItems;

import net.minecraft.item.ItemStack;

import codechicken.nei.api.API;
import gregtech.api.enums.ItemList;
import gregtech.common.items.GT_MetaGenerated_Item_03;

public class NEIHiding implements Runnable {

    @Override
    public void run() {
        if (!NotEnoughItems.isModLoaded()) {
            return;
        }

        for (int i = 0; i < 16; i++) {
            API.hideItem(new ItemStack(GT_MetaGenerated_Item_03.INSTANCE, 1, i));
        }

        if (Forestry.isModLoaded()) {
            ItemStack[] coverIDs = { ItemList.Plank_Larch.get(2L), ItemList.Plank_Teak.get(2L),
                    ItemList.Plank_Acacia_Green.get(2L), ItemList.Plank_Lime.get(2L), ItemList.Plank_Chestnut.get(2L),
                    ItemList.Plank_Wenge.get(2L), ItemList.Plank_Baobab.get(2L), ItemList.Plank_Sequoia.get(2L),
                    ItemList.Plank_Kapok.get(2L), ItemList.Plank_Ebony.get(2L), ItemList.Plank_Mahagony.get(2L),
                    ItemList.Plank_Balsa.get(2L), ItemList.Plank_Willow.get(2L), ItemList.Plank_Walnut.get(2L),
                    ItemList.Plank_Greenheart.get(2L), ItemList.Plank_Cherry.get(2L), ItemList.Plank_Mahoe.get(2L),
                    ItemList.Plank_Poplar.get(2L), ItemList.Plank_Palm.get(2L), ItemList.Plank_Papaya.get(2L),
                    ItemList.Plank_Pine.get(2L), ItemList.Plank_Plum.get(2L), ItemList.Plank_Maple.get(2L),
                    ItemList.Plank_Citrus.get(2L) };

            for (ItemStack cover : coverIDs) {
                API.hideItem(cover);
            }
        }
    }
}
