package gtPlusPlus.core.common.compat;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class COMPAT_OpenBlocks {

    public static void OreDict() {
        run();
    }

    private static final void run() {

        Item aGraveItem = ItemUtils.getItemFromFQRN("OpenBlocks:grave");
        if (aGraveItem == null) {
            return;
        }
        Block aGraveBlock = Block.getBlockFromItem(aGraveItem);
        if (aGraveBlock == null) {
            return;
        }

        Logger.INFO("[Hungry Node Blacklist] Setting the Hardness of the OpenBlocks Grave to 6.");
        aGraveBlock.setHardness(6f);
    }
}
