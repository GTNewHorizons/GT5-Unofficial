package gtPlusPlus.xmod.bop;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import biomesoplenty.api.content.BOPCItems;
import gregtech.api.enums.Mods;
import gtPlusPlus.xmod.bop.blocks.BOPBlockRegistrator;

public class BiomesOPlentyHandler {

    public static void preInit() {
        BOPBlockRegistrator.run();
        if (Mods.BiomesOPlenty.isModLoaded()) {
            if (BOPCItems.misc != null) {
                ItemStack pinecone = new ItemStack(BOPCItems.misc, 1, 13);
                if (pinecone != null) {
                    OreDictionary.registerOre("pinecone", pinecone);
                }
            }
        }
    }

    public static void postInit() {
        BOPBlockRegistrator.recipes();
    }
}
