package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.util.GT_LanguageManager;

/**
 * The glass types are split into separate files because they are registered as regular blocks, and a regular block can
 * have
 * 16 subtypes at most.
 */
public class GT_Item_Glass1 extends GT_Item_Casings_Abstract {

    protected final String chemicalGlassTooltip = GT_LanguageManager.addStringLocalization(
        getUnlocalizedName() + ".0.tooltip",
        "Able to resist the most extreme chemical conditions.");
    protected final String hawkingGlassTooltip = GT_LanguageManager.addStringLocalization(
        getUnlocalizedName() + ".4.tooltip",
        "Controls the outward flow of Hawking Radiation to stabilize a black hole.");

    public GT_Item_Glass1(Block block) {
        super(block);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        switch (getDamage(aStack)) {
            case 0 -> aList.add(chemicalGlassTooltip);
            case 4 -> aList.add(hawkingGlassTooltip);
        }
    }
}
