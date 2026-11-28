package gregtech.common.items.behaviors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;

import gregtech.api.enums.ItemList;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.util.GTUtility;

public class BehaviourTinCan extends BehaviourNone {

    private static final int FULL = 20;
    private static final float SATURATION = 0.4F;

    private final boolean spoiled;

    public BehaviourTinCan(boolean spoiled) {
        this.spoiled = spoiled;
    }

    @Override
    public ItemStack onItemRightClick(MetaBaseItem aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if (aWorld.isRemote) return aStack;

        int eaten = feed(aPlayer, aStack.stackSize, spoiled);
        if (eaten > 0) {
            aStack.stackSize -= eaten;
            GTUtility.addItemToPlayerInventory(aPlayer, ItemList.TinCan_Empty.get(eaten));
        }
        return aStack;
    }

    public static int missingFood(EntityPlayer player) {
        return Math.max(
            0,
            FULL - player.getFoodStats()
                .getFoodLevel());
    }

    public static int feed(EntityPlayer player, int available, boolean spoiled) {
        int eaten = Math.min(available, missingFood(player));
        if (eaten <= 0) return 0;

        FoodStats stomach = player.getFoodStats();
        stomach.addStats(eaten, SATURATION);
        player.worldObj.playSoundAtEntity(player, "random.burp", 0.5F, 0.9F + player.worldObj.rand.nextFloat() * 0.1F);
        if (spoiled) sicken(player);
        return eaten;
    }

    private static void sicken(EntityPlayer player) {
        player.addPotionEffect(new PotionEffect(Potion.hunger.id, 30 * 20, 0));
        player.addPotionEffect(new PotionEffect(Potion.confusion.id, 10 * 20, 0));
        if (player.worldObj.rand.nextInt(100) < 30) {
            player.addPotionEffect(new PotionEffect(Potion.poison.id, 5 * 20, 0));
        }
    }
}
