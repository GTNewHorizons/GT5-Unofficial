package gtPlusPlus.xmod.bop;

import net.minecraft.item.ItemStack;

import biomesoplenty.api.content.BOPCItems;
import gregtech.api.enums.Mods;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.bop.blocks.BOPBlockRegistrator;

public class BiomesOPlentyHandler {

    public static void preInit() {
        BOPBlockRegistrator.run();
        if (Mods.BiomesOPlenty.isModLoaded()) {
            if (BOPCItems.misc != null) {
                ItemStack aPinecone = ItemUtils.simpleMetaStack(BOPCItems.misc, 13, 1);
                if (aPinecone != null) {
                    ItemUtils.addItemToOreDictionary(aPinecone, "pinecone");
                }
            }
        }
    }

    public static void postInit() {
        BOPBlockRegistrator.recipes();
    }
}
