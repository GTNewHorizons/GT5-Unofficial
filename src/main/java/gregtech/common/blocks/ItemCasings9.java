package gregtech.common.blocks;

import net.minecraft.block.Block;

/**
 * The casings are split into separate files because they are registered as regular blocks, and a regular block can have
 * 16 subtypes at most.
 */
public class ItemCasings9 extends ItemCasingsAbstract {

    public ItemCasings9(Block block) {
        super(block);
    }

    /*
     * @Override
     * public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
     * // Add tooltip info if it was given
     * String localizedTooltip = GTLanguageManager.getTranslation(aStack.getUnlocalizedName() + ".tooltip");
     * // getTranslation returns the key if no translation was found, but this just means
     * // no tooltip was set.
     * if (localizedTooltip.startsWith("gt.")) {
     * aList.add(localizedTooltip);
     * }
     * super.addInformation(aStack, aPlayer, aList, aF3_H);
     * }
     */
}
