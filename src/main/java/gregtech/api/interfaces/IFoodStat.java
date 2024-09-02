package gregtech.api.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

import gregtech.api.items.MetaBaseItem;

public interface IFoodStat {

    /**
     * Warning the "aPlayer" Parameter may be null!
     */
    int getFoodLevel(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer);

    /**
     * Warning the "aPlayer" Parameter may be null!
     */
    float getSaturation(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer);

    /**
     * Warning the "aPlayer" Parameter may be null!
     */
    boolean alwaysEdible(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer);

    /**
     * Warning the "aPlayer" Parameter may be null!
     */
    boolean isRotten(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer);

    /**
     * Warning the "aPlayer" Parameter may be null!
     */
    EnumAction getFoodAction(MetaBaseItem aItem, ItemStack aStack);

    void onEaten(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer);
}
