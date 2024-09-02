package gregtech.api.items;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GTModHandler;

/**
 * This is just a basic Tool, which has normal durability and could break Blocks.
 */
public class ItemTool extends GTGenericItem {

    public ItemTool(String aUnlocalized, String aEnglish, String aTooltip, int aMaxDamage, int aEntityDamage,
        boolean aSwingIfUsed) {
        this(aUnlocalized, aEnglish, aTooltip, aMaxDamage, aEntityDamage, aSwingIfUsed, -1, -1);
    }

    public ItemTool(String aUnlocalized, String aEnglish, String aTooltip, int aMaxDamage, int aEntityDamage,
        boolean aSwingIfUsed, int aChargedGTID, int aDisChargedGTID) {
        this(
            aUnlocalized,
            aEnglish,
            aTooltip,
            aMaxDamage,
            aEntityDamage,
            aSwingIfUsed,
            aChargedGTID,
            aDisChargedGTID,
            0,
            0.0F);
    }

    public ItemTool(String aUnlocalized, String aEnglish, String aTooltip, int aMaxDamage, int aEntityDamage,
        boolean aSwingIfUsed, int aChargedGTID, int aDisChargedGTID, int aToolQuality, float aToolStrength) {
        super(aUnlocalized, aEnglish, aTooltip);
        setMaxDamage(aMaxDamage);
        setMaxStackSize(1);
        setNoRepair();
        setFull3D();
        GTModHandler.registerBoxableItemToToolBox(new ItemStack(this));
    }
}
