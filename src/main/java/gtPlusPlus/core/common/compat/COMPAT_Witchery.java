package gtPlusPlus.core.common.compat;

import static gregtech.api.enums.Mods.Witchery;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class COMPAT_Witchery {

    public static void OreDict() {
        run();
    }

    private static void run() {
        // Koboldite
        ItemStack aKobolditeDust = ItemUtils
                .getItemStackWithMeta(Witchery.isModLoaded(), "witchery:ingredient", "Koboldite Dust", 148, 1);
        ItemStack aKobolditeNugget = ItemUtils
                .getItemStackWithMeta(Witchery.isModLoaded(), "witchery:ingredient", "Koboldite Nugget", 149, 1);
        ItemStack aKobolditeIngot = ItemUtils
                .getItemStackWithMeta(Witchery.isModLoaded(), "witchery:ingredient", "Koboldite Ingot", 150, 1);
        if (aKobolditeDust != null) GT_OreDictUnificator.registerOre("dust" + "Koboldite", aKobolditeDust);
        if (aKobolditeNugget != null) GT_OreDictUnificator.registerOre("nugget" + "Koboldite", aKobolditeNugget);
        if (aKobolditeIngot != null) GT_OreDictUnificator.registerOre("ingot" + "Koboldite", aKobolditeIngot);
    }
}
