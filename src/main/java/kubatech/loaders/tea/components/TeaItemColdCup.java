package kubatech.loaders.tea.components;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import codechicken.nei.api.API;

public class TeaItemColdCup extends TeaItemCup {

    public TeaItemColdCup() {
        super();
        API.hideItem(new ItemStack(this));
    }

    @Override
    protected String componentName() {
        return "cold_tea_cup";
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer entity) {
        if (!entity.capabilities.isCreativeMode) --stack.stackSize;
        entity.getFoodStats()
            .addStats(1, 0f);
        world.playSoundAtEntity(entity, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 40;
    }
}
