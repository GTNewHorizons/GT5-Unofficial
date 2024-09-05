package gregtech.common.blocks;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import gregtech.api.util.GTLanguageManager;

/**
 * The glass types are split into separate files because they are registered as regular blocks, and a regular block can
 * have
 * 16 subtypes at most.
 */
public class ItemGlass1 extends ItemCasingsAbstract {

    protected final String chemicalGlassTooltip = GTLanguageManager.addStringLocalization(
        getUnlocalizedName() + ".0.tooltip",
        "Able to resist the most extreme chemical conditions.");
    protected final String hawkingGlassTooltip = GTLanguageManager.addStringLocalization(
        getUnlocalizedName() + ".4.tooltip",
        "Controls the outward flow of Hawking Radiation to stabilize a black hole.");

    public ItemGlass1(Block block) {
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
