package gregtech.common.items.behaviors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.enums.ItemList;
import gregtech.api.items.MetaBaseItem;

public class BehaviourFoodCan extends BehaviourNone {

    @Override
    public ItemStack onItemRightClick(MetaBaseItem aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if (aPlayer.getFoodStats()
            .needFood()) {
            aPlayer.getFoodStats()
                .addStats(2, 0.5f);
            aWorld.playSoundAtEntity(aPlayer, "random.eat", 0.5F, aWorld.rand.nextFloat() * 0.1F + 0.9F);

            if (!aPlayer.capabilities.isCreativeMode) {
                aStack.stackSize--;

                ItemStack emptyCan = ItemList.FoodCanEmpty.get(1);

                if (!aPlayer.inventory.addItemStackToInventory(emptyCan)) {
                    aPlayer.dropPlayerItemWithRandomChoice(emptyCan, false);
                }
            }
        } ;

        return aStack;
    }

}
