package gregtech.common.items.behaviors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

import gregtech.api.enums.ItemList;
import gregtech.api.items.MetaBaseItem;

public class BehaviourSpoiledFoodCan extends BehaviourNone {

    @Override
    public ItemStack onItemRightClick(MetaBaseItem aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if (aPlayer.getFoodStats()
            .needFood()) {
            aPlayer.getFoodStats()
                .addStats(2, 0.0f);
            aWorld.playSoundAtEntity(aPlayer, "random.burp", 0.75F, aWorld.rand.nextFloat() * 0.1F + 0.9F);

            aPlayer.addPotionEffect(new PotionEffect(9, 100, 1));
            aPlayer.addPotionEffect(new PotionEffect(17, 2400, 2));
            aPlayer.addPotionEffect(new PotionEffect(19, 40, 2));

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
