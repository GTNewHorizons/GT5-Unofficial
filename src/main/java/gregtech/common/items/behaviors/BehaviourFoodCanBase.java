package gregtech.common.items.behaviors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.FoodStats;
import net.minecraft.world.World;

import gregtech.api.enums.ItemList;
import gregtech.api.items.MetaBaseItem;

public abstract class BehaviourFoodCanBase extends BehaviourNone {

    private static final int MAX_FOOD_LEVEL = 20;

    private final int foodPerCan;
    private final float saturationModifier;

    protected BehaviourFoodCanBase(int foodPerCan, float saturationModifier) {
        this.foodPerCan = foodPerCan;
        this.saturationModifier = saturationModifier;
    }

    @Override
    public ItemStack onItemRightClick(MetaBaseItem item, ItemStack stack, World world, EntityPlayer player) {
        if (world.isRemote) return stack;

        FoodStats foodStats = player.getFoodStats();
        if (!foodStats.needFood()) return stack;

        int missingFood = MAX_FOOD_LEVEL - foodStats.getFoodLevel();
        int cansNeeded = (missingFood + foodPerCan - 1) / foodPerCan;

        boolean isCreative = player.capabilities.isCreativeMode;
        int cansEaten = isCreative ? cansNeeded : Math.min(cansNeeded, stack.stackSize);

        foodStats.addStats(cansEaten * foodPerCan, saturationModifier);
        world.playSoundAtEntity(player, getEatSound(), 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        onCansEaten(player, cansEaten);

        if (!isCreative) {
            stack.stackSize -= cansEaten;

            ItemStack emptyCans = ItemList.FoodCanEmpty.get(cansEaten);
            if (!player.inventory.addItemStackToInventory(emptyCans) && emptyCans.stackSize > 0) {
                player.dropPlayerItemWithRandomChoice(emptyCans, false);
            }
        }
        return stack;
    }

    protected String getEatSound() {
        return "random.eat";
    }

    protected void onCansEaten(EntityPlayer player, int cansEaten) {}
}
