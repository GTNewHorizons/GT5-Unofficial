package kubatech.loaders.tea.components;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class TeaItemBucket extends TeaItem {

    public TeaItemBucket() {
        super();
        setMaxStackSize(1);
    }

    @Override
    protected String componentName() {
        return "tea_bucket";
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.drink;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer entity) {
        entity.setItemInUse(stack, getMaxItemUseDuration(stack));
        return stack;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer entity) {
        if (!entity.capabilities.isCreativeMode) --stack.stackSize;
        entity.getFoodStats()
            .addStats(20, 2.f);
        world.playSoundAtEntity(entity, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
        return stack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack stack) {
        return 160;
    }

}
