package gregtech.api.items.armor.behaviors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import gregtech.api.items.armor.ArmorContext;

public class AutoFeederBehavior implements IArmorBehavior {

    public static final AutoFeederBehavior INSTANCE = new AutoFeederBehavior();

    private static final int HUNGER_THRESHOLD = 14;
    private static final int CHECK_INTERVAL = 40;
    private static final double EU_PER_MEAL = 1000;

    protected AutoFeederBehavior() {}

    @Override
    public BehaviorName getName() {
        return BehaviorName.AutoFeeder;
    }

    @Override
    public void onArmorTick(@NotNull ArmorContext context) {
        if (context.isRemote()) return;

        World world = context.getWorld();
        if (world.getTotalWorldTime() % CHECK_INTERVAL != 0) return;

        EntityPlayer player = context.getPlayer();
        if (player.getFoodStats()
            .getFoodLevel() >= HUNGER_THRESHOLD) return;

        int slot = findFood(player);
        if (slot == -1) return;
        if (!context.drainEnergy(EU_PER_MEAL)) return;

        ItemStack food = player.inventory.mainInventory[slot];
        ItemStack result = food.getItem()
            .onEaten(food, world, player);
        player.inventory.mainInventory[slot] = (result == null || result.stackSize <= 0) ? null : result;
    }

    private static int findFood(EntityPlayer player) {
        ItemStack[] inv = player.inventory.mainInventory;
        for (int i = 9; i < 36; i++) if (isSafeFood(inv[i])) return i;
        for (int i = 0; i < 9; i++) if (isSafeFood(inv[i])) return i;
        return -1;
    }

    private static boolean isSafeFood(ItemStack stack) {
        if (stack == null) return false;
        Item item = stack.getItem();
        if (item.getItemUseAction(stack) != EnumAction.eat) return false;
        if (item == Items.rotten_flesh || item == Items.spider_eye || item == Items.poisonous_potato) return false;
        if (item == Items.golden_apple) return false;
        if (item == Items.fish && stack.getItemDamage() == 3) return false;
        return true;
    }
}
